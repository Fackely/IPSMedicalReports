package com.princetonsa.dao.sqlbase.facturasVarias;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * Fecha: Abril de 2008
 * @author Mauricio Jaramillo
 */

public class SqlBasePagosFacturasVariasDao 
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBasePagosFacturasVariasDao.class);

	/**
     * Cadena para consultar los documentos pendientes que pertenecen a deudores 
     */
    private static String strConPagFacturasVarias =
    											"SELECT pge.codigo as codigo, "+
		    										"pge.deudor as codigodeudor, "+
		    										"getdescripciontercero(pge.deudor) as nombredeudor, "+
		    										"getnumeroidentificaciontercero(pge.deudor) as identificaciondeudor, "+
		    										"pge.tipo_doc as codigotipo, "+
		    										"tdp.descripcion as destipo, "+
		    										"tdp.acronimo as acronimotipo, "+
			    									"pge.documento as documento, "+
			    									"pge.estado as codigoestado, "+
			    									"ep.descripcion as desestado, "+
			    									"pge.valor as valor, "+
			    									"pge.institucion as institucion, "+
			    									"to_char(pge.fecha_documento,'yyyy-mm-dd') as fechadocumento, "+
			    									"getCodPagoActualFacturasVarias(pge.codigo) as aplicacionactual, "+
			    									"getUltimaApliPagoFacturVarias(pge.codigo) as numeropago, "+
			    									"getTotalPagosFacturVariasAprob(pge.codigo) as valaplicado, "+
			    									"getvalofacaplipagosfactuvarias(pge.codigo," + ConstantesBD.codigoEstadoAplicacionPagosPendiente+") as valfacactual, "+
			    									"getTotalConcApliPagFactVarias(getCodPagoActualFacturasVarias(pge.codigo)) as valconceppagoactual, "+
			    									"coalesce(getObsApliPagoFacturasVarias(getCodPagoActualFacturasVarias(pge.codigo)), '') as observaciones, "+
			    									"(pge.valor "+
			    									"+ getvalconcaplipagosfactvarias(pge.codigo,"+ConstantesBD.codigoTipoConceptoMayorValor+","+ConstantesBD.codigoEstadoAplicacionPagosAprobado+") "+
			    									"- getvalconcaplipagosfactvarias(pge.codigo,"+ConstantesBD.codigoTipoConceptoMenorValor+","+ConstantesBD.codigoEstadoAplicacionPagosAprobado+") "+
			    									"- getvalofacaplipagosfactuvarias(pge.codigo," + ConstantesBD.codigoEstadoAplicacionPagosAprobado+") "+
			    									") as valporaplicar "+ 
		    									"FROM pagos_general_empresa pge "+
			    									"INNER JOIN tipos_doc_pagos tdp on (tdp.codigo=pge.tipo_doc) "+
			    									"INNER JOIN estados_pagos ep on(pge.estado=ep.codigo) "+
			    									"INNER JOIN deudores deu on(deu.codigo=pge.deudor) " +
			    									"left outer join tesoreria.recibos_caja rc ON(rc.numero_recibo_caja = pge.documento and pge.tipo_doc = "+ConstantesBD.codigoTipoDocumentoPagosReciboCaja+") " +
			    									//Se agreagan las siguientes validaciones por tarea 139127
			    									"LEFT OUTER JOIN facturasvarias.aplicacion_pagos_fvarias apfv ON (apfv.pagos_general_fvarias=pge.codigo) " +
			    									"LEFT OUTER JOIN facturasvarias.aplicac_pagos_factura_fvarias ap ON (ap.aplicacion_pagos=apfv.codigo) " +
			    									"LEFT OUTER JOIN facturasvarias.facturas_varias fv ON (fv.codigo_fac_var=ap.factura) " +
			    									"LEFT OUTER JOIN facturasvarias.ajus_facturas_varias afv ON (afv.factura=fv.codigo_fac_var) ";
			    									
	
    /**
     * Cadena WHERE para la consulta del listado de documentos pendientes que pertenecen a deudores
     */
    private static String strWhePagFacturasVarias =
										    	"WHERE "+
										    		"(pge.valor "+
													"+ getvalconcaplipagosfactvarias(pge.codigo,"+ConstantesBD.codigoTipoConceptoMayorValor+","+ConstantesBD.codigoEstadoAplicacionPagosAprobado+") "+
													"- getvalconcaplipagosfactvarias(pge.codigo,"+ConstantesBD.codigoTipoConceptoMenorValor+","+ConstantesBD.codigoEstadoAplicacionPagosAprobado+") "+
													"- getvalofacaplipagosfactuvarias(pge.codigo," + ConstantesBD.codigoEstadoAplicacionPagosAprobado+") "+
													") > 0 "+
													"AND (pge.estado ="+ConstantesBD.codigoEstadoPagosRecaudado+" OR pge.estado ="+ConstantesBD.codigoEstadoPagosAplicado +") " +
													"AND (pge.tipo_doc = "+ConstantesBD.codigoTipoDocumentoPagosReciboCaja+" and rc.estado <> "+ConstantesBD.codigoEstadoReciboCajaAnulado+") "+
													"AND pge.institucion = ? "+
													"AND " +
													"(coalesce(apfv.estado,"+ConstantesBD.codigoNuncaValido+") <> "+ConstantesBD.codigoEstadoAplicacionPagosPendiente+" "+
													"AND " +
													"coalesce(afv.estado,' ') <> '"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"') ";
    /**
     * Cadena ORDER BY para la consulta del listado de documentos pendientes que pertenecen a deudores
     */
    private static String strOrdPagFacturasVarias =
										    	"ORDER BY "+
													"pge.tipo_doc, convertiranumero(pge.documento||'')";
    
    /**
     * Cadena SELECT para consultar los conceptos del documento seleccionado
     */
    private static String strConceptosSelect =
    										"SELECT " +
    											"capfv.concepto_pagos as codigoconcepto, " +
    											"cp.descripcion as desconcepto, " +
    											"cp.tipo as tipoconcepto, " +
    											"capfv.valor_base as valorbase, " +
    											"capfv.valor_concepto as valorconcepto, " +
    											"capfv.porcentaje as porcentaje, " +
    											"'true' as bd "+
    										"FROM conceptos_apli_pagos_fvarias capfv "+
    											"INNER JOIN concepto_pagos cp on(cp.codigo=capfv.concepto_pagos) "+
    										"WHERE "+
    											"capfv.aplicacion_pagos_fvarias = ?";
    
    /**
     * Cadena SELECT para consultar las facturas por deudores
     */
    private static String strConsultaFacturas =
    										"SELECT " +
												"fv.codigo_fac_var AS codigofactura, "+
												"fv.consecutivo AS consecutivofactura, "+
												"to_char(fv.fecha,'yyyy-mm-dd') AS fecha, "+
												"fv.institucion AS institucion, "+
												"fv.deudor AS deudor, "+
												"fv.centro_atencion AS codigocentroatencion, "+
												"to_char(fv.fecha_anulacion, 'DD/MM/YYYY') AS fechaaprobacion, "+
												"getnomcentroatencion(fv.centro_atencion) AS nombrecentroatencion, "+
												"(fv.valor_factura + coalesce(fv.ajustes_debito, 0) - coalesce(fv.ajustes_credito, 0) - coalesce(fv.valor_pagos, 0)) AS saldo, "+
												"'false' AS seleccionado ";
    /**
     * Cadena WHERE para consultar las facturas por deudores 
     */
    private static String strWheConsultaFacturas =
    										" WHERE "+
    											"fv.deudor = ? "+
    											"AND fv.institucion = ? "+
    											"AND (fv.valor_factura + coalesce(fv.ajustes_debito, 0) - coalesce(fv.ajustes_credito, 0) - coalesce(fv.valor_pagos, 0)) > 0 "+
    											"AND fv.estado_factura = '"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"'";
    
    /**
     * Cadena FROM para consultar las facturas por deudores
     */
    private static String strFromConsultaFacturas =
    										" FROM facturas_varias fv ";
    
    
    /**
     * Cadena INSERT para adicionar un pago de aplicacion de facturas varias
     */
    private static String strInsertAplicacionPagosFacturas = 
    														"INSERT INTO "+
    															"aplicac_pagos_factura_fvarias "+
    															"(aplicacion_pagos, factura, valor_pago) "+
    														"VALUES (?,?,?)";
    
    /**
     * Cadena UPDATE de una aplicacion de pago de facturas varias
     */
    private static String strUpdateAplicacionPagosFacturas = 
	    													"UPDATE "+
	    														"aplicac_pagos_factura_fvarias "+
	    													"SET "+
	    														"valor_pago = ? "+
	    													"WHERE "+
	    														"aplicacion_pagos = ? "+
	    														"AND factura = ?";
    
	/**
	 * Metodo que consulta el listado de Pagos de Facturas Varias
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static HashMap consultarPagosGeneralFacturasVarias(Connection con, int codigoInstitucionInt)
    {
        HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(strConPagFacturasVarias+strWhePagFacturasVarias+strOrdPagFacturasVarias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoInstitucionInt);
            logger.info("-------->Consulta: "+strConPagFacturasVarias+strWhePagFacturasVarias+strOrdPagFacturasVarias);
            mapa = cargarValueObjectDocumentosPagos(new ResultSetDecorator(ps.executeQuery()));
            ps.close();
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE PAGOS FACTURAS VARIAS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }

	/**
	 * Metodo que carga el concepto de un Pago de Factura Varias Seleccionado
	 * @param con
	 * @param codigoAplicacionPago
	 * @return
	 */
	public static HashMap cargarConceptosPagosFacturasVarias(Connection con, int codigoAplicacionPago)
    {
        HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        try
        {
            PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConceptosSelect,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoAplicacionPago);
            logger.info("-------->Consulta: "+strConceptosSelect);
            logger.info("\n-------->Codigo Aplicacion Pago: "+codigoAplicacionPago);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
            ps.close();
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE CONCEPTOS APLICACIONES PAGOS FACTURAS VARIAS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }
	
	public static HashMap busquedaAvanzada(Connection con, HashMap vo)
    {
		String condicion = strWhePagFacturasVarias;
        HashMap mapa = new HashMap();
        mapa.put("numRegistros","0");
        if(Utilidades.convertirAEntero(vo.get("tipo")+"")>ConstantesBD.codigoNuncaValido)
            condicion=condicion+" AND pge.tipo_doc="+vo.get("tipo");
        if(!(vo.get("documento")+"").equals(""))
            condicion=condicion+" AND pge.documento='"+vo.get("documento")+"'";
        if(!(vo.get("fecha")+"").equals(""))
            condicion=condicion+" AND to_char(pge.fecha_documento,'yyyy-mm-dd')='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha")+"")+"'";
        if(Utilidades.convertirAEntero(vo.get("deudor")+"")>ConstantesBD.codigoNuncaValido)
            condicion=condicion+" AND pge.deudor="+vo.get("deudor");
        try
        {
        	PreparedStatementDecorator ps= new PreparedStatementDecorator(con, strConPagFacturasVarias+condicion+strOrdPagFacturasVarias);
            ps.setInt(1,Utilidades.convertirAEntero(vo.get("institucion")+""));
            logger.info("consulta \n\n\n"+ps);
            mapa=cargarValueObjectDocumentosPagos(new ResultSetDecorator(ps.executeQuery()));
            ps.close();
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE PAGOS FACTURAS VARIAS EN BUSQUEDA AVANZADA");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }
	
	/**
     * @param rs
     * @return
     * @throws SQLException
     */
    private static HashMap cargarValueObjectDocumentosPagos(ResultSetDecorator rs) throws SQLException
    {
        HashMap mapa = new HashMap<String, Object>();
        int cont = 0;
        while (rs.next())
        {
            mapa.put("codigo_"+cont,rs.getString("codigo"));
            mapa.put("codigodeudor_"+cont,rs.getString("codigodeudor"));
            mapa.put("nombredeudor_"+cont,rs.getString("nombredeudor"));
            mapa.put("identificaciondeudor_"+cont,rs.getString("identificaciondeudor"));
            mapa.put("codigotipo_"+cont,rs.getString("codigotipo"));
            mapa.put("destipo_"+cont,rs.getString("destipo"));
            mapa.put("acronimotipo_"+cont,rs.getString("acronimotipo"));
            mapa.put("documento_"+cont,rs.getString("documento"));
            mapa.put("codigoestado_"+cont,rs.getString("codigoestado"));
            mapa.put("desestado_"+cont,rs.getString("desestado"));
            mapa.put("valordocumento_"+cont,rs.getString("valor"));
            mapa.put("fechadocumento_"+cont,rs.getString("fechadocumento"));
            mapa.put("aplicacionactual_"+cont,rs.getString("aplicacionactual"));
            if(rs.getInt("aplicacionactual")==ConstantesBD.codigoNuncaValido)
            {
                mapa.put("numeroaplicacion_"+cont,(rs.getInt("numeropago")+1)+"");
                mapa.put("modificacion_"+cont,"false");
            }
            else
            {
                mapa.put("numeroaplicacion_"+cont,rs.getString("numeropago"));
                mapa.put("modificacion_"+cont,"true");
            }
            mapa.put("valaplicado_"+cont,rs.getString("valaplicado"));
            mapa.put("valconceppagoactual_"+cont,rs.getString("valconceppagoactual"));
            mapa.put("valfacactual_"+cont,rs.getString("valfacactual"));
            mapa.put("valporaplicar_"+cont,rs.getString("valporaplicar"));
            mapa.put("observaciones_"+cont,rs.getString("observaciones"));
            cont++;
        }
        rs.close();
        mapa.put("numRegistros", cont+"");
        return mapa;
    }
    
    /**
     * @param con
     * @param mapa
     * @param seq
     * @return
     */
    public static String guardarConceptosAplicacionPagosFacturasVarias(Connection con, HashMap mapa)
    {
        PreparedStatementDecorator ps = null;
        boolean enTransaccion = true;
        String resultado = "";
        try
        {
            UtilidadBD.iniciarTransaccion(con);
            if(UtilidadTexto.getBoolean(mapa.get("modificacion")+""))
	        {
	            String actualizarAplicacionPagosEmpresa = "UPDATE aplicacion_pagos_fvarias SET fecha_aplicacion=?, observaciones=? WHERE codigo=?";
	            ps= new PreparedStatementDecorator(con.prepareStatement(actualizarAplicacionPagosEmpresa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                ps.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD( mapa.get("fechaaplicacion")+"")));
                ps.setString(2,mapa.get("observaciones")+"");
                ps.setInt(3, Utilidades.convertirAEntero(mapa.get("codigoaplicacion")+""));
                enTransaccion=(ps.executeUpdate()>0);
                ps.close();
                if(enTransaccion)
                	eliminarConceptosAplicacionFacturasVarias(con, Utilidades.convertirAEntero(mapa.get("codigoaplicacion")+""));
            }
	        else if(!UtilidadTexto.getBoolean(mapa.get("modificacion")+""))
	        {
	        	logger.info("entra a la incsercion d aplicaciones ");
	            String insertaAplicacionPagosFacturasVarias="INSERT INTO aplicacion_pagos_fvarias (" +
	            																	"codigo, " +
	            																	"pagos_general_fvarias, " +
	            																	"estado, " +
	            																	"numero_aplicacion, " +
	            																	"fecha_aplicacion, " +
	            																	"observaciones, " +
	            																	"usuario, " +
	            																	"fecha_grabacion, " +
	            																	"hora_grabacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	            int codigoAplicacion = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_apli_pagos_facturas_varias");
	            mapa.put("codigoaplicacion",codigoAplicacion);
                ps =  new PreparedStatementDecorator(con.prepareStatement(insertaAplicacionPagosFacturasVarias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                ps.setInt(1,codigoAplicacion);
                ps.setInt(2,Utilidades.convertirAEntero(mapa.get("pagogeneraempresa")+""));
                ps.setInt(3,Utilidades.convertirAEntero(mapa.get("estadoaplicacion")+""));
                //ps.setInt(4,Utilidades.convertirAEntero(mapa.get("numeroaplicacion")+""));
                if (UtilidadTexto.isEmpty((String) mapa.get("numeroaplicacion"))) {
					ps.setNull(4,java.sql.Types.INTEGER);
                } else {
                	ps.setInt(4,Utilidades.convertirAEntero(mapa.get("numeroaplicacion")+""));
                }
                ps.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD( mapa.get("fechaaplicacion")+"")));
                ps.setString(6,mapa.get("observaciones")+"");
                ps.setString(7,mapa.get("usuario")+"");
                ps.setDate(8,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD( mapa.get("fechagrabacion")+"")));
                ps.setString(9,mapa.get("horagrabacion")+"");
                enTransaccion=false;
                if(ps.executeUpdate()>0)
                    enTransaccion=(actualizarEstadoPagoEmpresa(con, Utilidades.convertirAEntero(mapa.get("pagogeneraempresa")+""), Utilidades.convertirAEntero(mapa.get("estadopago")+""))>0);
                ps.close();
	        }
            resultado = mapa.get("codigoaplicacion").toString();
	        if(enTransaccion)
	        {
	            for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
	            {
	                if(enTransaccion&&!UtilidadTexto.getBoolean(mapa.get("eliminado_"+i)+""))
	                {
	                    String cadena="INSERT INTO conceptos_apli_pagos_fvarias (" +
	                    									"aplicacion_pagos_fvarias, " +
	                    									"concepto_pagos, " +
	                    									"institucion, " +
	                    									"valor_base, " +
	                    									"porcentaje, " +
	                    									"valor_concepto) VALUES (?, ?, ?, ?, ?, ?)";
	                    
	                    ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	                    
	                    ps.setInt(1,Utilidades.convertirAEntero(mapa.get("codigoaplicacion")+""));
	                    ps.setString(2,mapa.get("codigoconcepto_"+i)+"");
	                    ps.setInt(3,Utilidades.convertirAEntero(mapa.get("institucion")+""));
	                    ps.setDouble(4,Utilidades.convertirADouble(mapa.get("valorbase_"+i)+""));
	                    ps.setDouble(5,Utilidades.convertirADouble(mapa.get("porcentaje_"+i)+""));
	                    ps.setDouble(6,Utilidades.convertirADouble(mapa.get("valorconcepto_"+i)+""));
	                    enTransaccion = ps.executeUpdate()>0;
	                    ps.close();
	                }
	            }
	            
	            if(!enTransaccion)
	            {
	            	resultado = "";
	            }
	            
	            UtilidadBD.finalizarTransaccion(con);
	            return resultado;
	        }
	        else
	        {
	        	resultado = "";
	            UtilidadBD.abortarTransaccion(con);
	        }
        }
        catch (SQLException e)
        {
            logger.error("SE PRODUJO ERROR GUARDANDO LOS CONCEPTOS APLICACION PAGOS PARA FACTURAS VARIAS"+e);
            e.printStackTrace();
            resultado = "";
        }
        return resultado;
    }
    
    /**
     * @param con
     * @param codigoPago
     * @return
     */
    public static int eliminarConceptosAplicacionFacturasVarias(Connection con, int codigoPago)
    {
        PreparedStatementDecorator ps=null;
        String cadena="DELETE FROM conceptos_apli_pagos_fvarias WHERE aplicacion_pagos_fvarias = ?";
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoPago);
            int resp =  ps.executeUpdate();
            ps.close();
            return resp;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return ConstantesBD.codigoNuncaValido;
    }
    
    /**
     * @param con
     * @param codigoPago
     * @param codigoNuevoEstado
     * @return
     */
    public static int actualizarEstadoPagoEmpresa(Connection con,int codigoPago,int codigoNuevoEstado)
    {
        PreparedStatementDecorator ps=null;
        try
        {
            String cadena="UPDATE pagos_general_empresa SET estado = ? WHERE codigo = ?";
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codigoNuevoEstado);
            ps.setInt(2, codigoPago);
            int resp = ps.executeUpdate();
            ps.close();
            return resp;
        }
        catch(SQLException e)
        {
            logger.error("SE PRODUJO ERROR ACTUALIZANDO EL ESTADO DEL PAGO EMPRESA");
            e.printStackTrace();
        }
        return 0;
    }
 
    /**
     * @param con
     * @param codigoAplicacionPago
     * @param codigoNuevoEstado
     * @return
     */
    public static int actualizarEstadoAplicacionPagosFacturasVarias(Connection con,int codigoAplicacionPago,int codigoNuevoEstado)
    {
        PreparedStatementDecorator ps=null;
        try
        {
            String cadena="UPDATE aplicacion_pagos_fvarias SET estado = ? WHERE codigo = ?";
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codigoNuevoEstado);
            ps.setInt(2, codigoAplicacionPago);
            int resp = ps.executeUpdate();
            ps.close();
            return resp;
        }
        catch(SQLException e)
        {
            logger.error("SE PRODUJO ERROR ACTUALIZANDO EL ESTADO DE LA APLICACION DE PAGOS DE FACTURAS VARIAS");
            e.printStackTrace();
        }
        return 0;
    }
    
   
    
    
    /**
     * @param con
     * @param mapaDatos
     * @return
     */
    public static HashMap buscarFacturasDeudor(Connection con, HashMap mapaDatos)
    {
        HashMap mapa=new HashMap();
        mapa.put("numRegistros","0");
        try
        {
        	String select = strConsultaFacturas+", 0 as valpago, 'false' as bd ";
            String from = strFromConsultaFacturas;
            String rest = strWheConsultaFacturas+" AND fv.codigo_fac_var NOT IN("+mapaDatos.get("facturas")+")";
            String order = " ORDER BY fv.codigo_fac_var";
            logger.info("\n=====>Consulta Facturas Deudor: "+select+from+rest+order);
            logger.info("\n=====>Deudor: "+mapaDatos.get("deudor"));
            
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(select+from+rest+order,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,Utilidades.convertirAEntero(mapaDatos.get("deudor")+""));
            ps.setInt(2,Utilidades.convertirAEntero(mapaDatos.get("institucion")+""));
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
            ps.close();
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE FACTURAS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }
    
    /**
     * @param con
     * @param institucion
     * @param convenio
     * @param facturaBusquedaAvanzada
     * @param facturas
     * @return
     */
    public static HashMap buscarFacturaLLave(Connection con, int institucion, int deudor, String facturaBusquedaAvanzada, String facturas)
    {
        HashMap mapa=new HashMap();
        mapa.put("numRegistros","0");
        try
        {
            String select = strConsultaFacturas+", 0 as valpago, 'false' as bd ";
            String from = strFromConsultaFacturas;
            String rest = strWheConsultaFacturas+" AND fv.codigo_fac_var NOT IN("+facturas+") AND fv.consecutivo = ? ";
            
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(select+from+rest,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,deudor);
            ps.setInt(2,institucion);
            ps.setDouble(3, Utilidades.convertirADouble(facturaBusquedaAvanzada));
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
            ps.close();
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA AVANZADA DE FACTURAS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }
   
    /**
     * @param con
     * @param vo
     * @return
     */
    public static int guardarAplicacionFacturas(Connection con, HashMap vo)
    {
        PreparedStatementDecorator psInsert=null;
        PreparedStatementDecorator psUpdate=null;
        //PreparedStatementDecorator psInsertFV=null;
        try
        {
        	logger.info("codigo aplicacion: "+vo.get("codaplicacion"));
            psInsert =  new PreparedStatementDecorator(con.prepareStatement(strInsertAplicacionPagosFacturas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            psUpdate =  new PreparedStatementDecorator(con.prepareStatement(strUpdateAplicacionPagosFacturas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            //psInsertFV =  new PreparedStatementDecorator(con.prepareStatement(strUpdateValorPagoFacturasVarias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            for(int i=0; i<Utilidades.convertirAEntero(vo.get("numRegistros")+"");i++)
            {
            	logger.info("bd_"+i+": "+vo.get("bd_"+i));
            	logger.info("eliminado_"+i+": "+vo.get("eliminado_"+i));
            	logger.info("valpago_"+i+": "+vo.get("valpago_"+i));
            	logger.info("codigofactura_"+i+": "+vo.get("codigofactura_"+i));
                if(UtilidadTexto.getBoolean(vo.get("bd_"+i)+""))
                {
                    if(UtilidadTexto.getBoolean(vo.get("eliminado_"+i)+""))
                    {
                        String cadena = "DELETE FROM aplicac_pagos_factura_fvarias WHERE aplicacion_pagos = ? AND factura = ?";
                        PreparedStatementDecorator psDelete =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                        psDelete.setInt(1, Utilidades.convertirAEntero(vo.get("codaplicacion")+""));
                        psDelete.setInt(2, Utilidades.convertirAEntero(vo.get("codigofactura_"+i)+""));
                        psDelete.executeUpdate();
                        psDelete.close();
                    }
                    else
                    {
                    	if(Utilidades.convertirADouble(vo.get("valpago_"+i)+"")>0)
                    	{
                    		psUpdate.setDouble(1, Utilidades.convertirADouble(vo.get("valpago_"+i)+""));
                    		psUpdate.setInt(2, Utilidades.convertirAEntero(vo.get("codaplicacion")+""));
                    		psUpdate.setInt(3, Utilidades.convertirAEntero(vo.get("codigofactura_"+i)+""));
                    		psUpdate.executeUpdate();
                    		if(psUpdate.executeUpdate()<=0)
                    		{
                    			i=Utilidades.convertirAEntero(vo.get("numRegistros")+"");
                    		}
                    		/*psInsertFV.setString(1, vo.get("valpago_"+i)+"");
                        	psInsertFV.setString(2, vo.get("codigofactura_"+i)+"");
                        	psInsertFV.executeUpdate();*/
                    	}
                    }
                }
                else
                {
                	if(!UtilidadTexto.getBoolean(vo.get("eliminado_"+i)+""))
                    {
	                	if(Double.parseDouble(vo.get("valpago_"+i)+"")>0)
	                    {
	                		logger.info("voy a insertar la aplicacion del pago");
			                psInsert.setInt(1, Utilidades.convertirAEntero(vo.get("codaplicacion")+""));
			                psInsert.setInt(2, Utilidades.convertirAEntero(vo.get("codigofactura_"+i)+""));
			                psInsert.setDouble(3, Utilidades.convertirADouble(vo.get("valpago_"+i)+""));
			                if(psInsert.executeUpdate()<=0)
			                {
			                	logger.info("falló!!!");
			                    i=Utilidades.convertirAEntero(vo.get("numRegistros")+"");
			                }
			                /*psInsertFV.setString(1, vo.get("valpago_"+i)+"");
			            	psInsertFV.setString(2, vo.get("codigofactura_"+i)+"");
			            	psInsertFV.executeUpdate();*/
	                    }
                    }
                }
            	
            }
            psInsert.close();
            psUpdate.close();
            return 1;
        }
        catch (SQLException e)
        {
            logger.error("SE PRODUJO ERROR GUARDANDO LA INFORMACION A NIVEL DE LA FACTURA"+e);
            e.printStackTrace();
        }
        return ConstantesBD.codigoNuncaValido;
    }
    
    /**
     * Metodo que consulta la aplicacion a nivel de facturas de un pago
     * @param con
     * @param aplicacionPago
     * @return
     */
    public static HashMap consultarPagosFacturas(Connection con, int aplicacionPago)
    {
        String cadena = strConsultaFacturas+", apff.valor_pago AS valpago, 'true' AS bd FROM aplicac_pagos_factura_fvarias apff INNER JOIN facturas_varias fv ON (apff.factura = fv.codigo_fac_var) WHERE apff.aplicacion_pagos = ? ORDER BY fv.consecutivo";
        HashMap mapa = new HashMap();
        mapa.put("numRegistros","0");
        try
        {
            PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, aplicacionPago);
            logger.info("===>Consulta Pagos Aplicados Facturas Varias: "+cadena+" ===>Número Aplicación: "+aplicacionPago);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
            ps.close();
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE FACTURAS DE UNA APLICACION DE PAGOS FACTURAS VARIAS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }
    
}