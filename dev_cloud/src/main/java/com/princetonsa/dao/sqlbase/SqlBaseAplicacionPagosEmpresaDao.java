/*
 * Created on 3/11/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.ResultSetMetaData;


import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
/**
 * @version 1.0, 3/11/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class SqlBaseAplicacionPagosEmpresaDao
{

    /**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseAplicacionPagosEmpresaDao.class);
    ////////////////////////////////////CADENAS SQL///////////////////////////////////////////////////////////////
    /**
     * Cadena que contiene la definicion de los campos del select para la consulta de los pagos generales empres
     */
    private static String SelConPagGeneralEmpresa="SELECT pge.codigo as codigo," +
    										"pge.convenio as codigoconvenio," +
    										"getnombreconvenio(pge.convenio) as nombreconvenio," + 
    										"pge.tipo_doc as codigotipo," +
    										"tdp.descripcion as destipo," +
    										"tdp.acronimo as acronimotipo," +
    										"pge.documento as documento," +
    										"case when pge.tipo_doc="+ConstantesBD.codigoTipoDocumentoPagosReciboCaja+" then (SELECT consecutivo_recibo from recibos_caja where numero_recibo_caja = pge.documento ) else pge.documento end as consecutivodoc, " +
    										"pge.estado as codigoestado," +
    										"ep.descripcion as desestado," +
    										"pge.valor as valor," +
    										"pge.institucion as institucion," +
    										"to_char(pge.fecha_documento,'yyyy-mm-dd') as fechadocumento," +
    										"getCodAplicacionPagoActual(pge.codigo) as aplicacionactual, " +
    										"getUltimaApliPago(pge.codigo) as numeropago, "+
    										"gettotalpagosaproba(pge.codigo) as valaplicado," +
    										"getvalofacaplipagos(pge.codigo," + ConstantesBD.codigoEstadoAplicacionPagosPendiente+") as valfacactual, " +
    										"getTotalConceptosApliPagos(getCodAplicacionPagoActual(pge.codigo)) as valconceppagoactual, " +
    										"getObsApliPago(getCodAplicacionPagoActual(pge.codigo)) as observaciones, " +
    										"		(pge.valor " +
    										"		+ getvalconceptosaplipagos(pge.codigo,"+ConstantesBD.codigoTipoConceptoMayorValor+","+ConstantesBD.codigoEstadoAplicacionPagosAprobado+") " +
    										"		- getvalconceptosaplipagos(pge.codigo,"+ConstantesBD.codigoTipoConceptoMenorValor+","+ConstantesBD.codigoEstadoAplicacionPagosAprobado+") " +
    										"		- getvalofacaplipagos(pge.codigo," + ConstantesBD.codigoEstadoAplicacionPagosAprobado+") " +
    										"		) as valporaplicar " + 
    										"FROM pagos_general_empresa pge " +
    										"inner join tipos_doc_pagos tdp on tdp.codigo=pge.tipo_doc " +
    										"inner join estados_pagos ep on(pge.estado=ep.codigo) " +
    										"inner join convenios conv on(conv.codigo=pge.convenio and conv.tipo_contrato<>"+ConstantesBD.codigoTipoContratoCapitado+") ";
    
    /**
     * Variable que contine la condicion que filtra los pagos general empresa de una determinada institucion y
     * pendientes de realizar / aprobar aplicacion de pago. los pagos deben estar en estado recaudado y/o aplicado
     * y que el convenio no corresponda a convenios de capitacion y que tienen saldo pendiente de aplicar.
     */
    private static String wheConPagGeneralEmpresa=" where (pge.valor " +
    										"		+ getvalconceptosaplipagos(pge.codigo,"+ConstantesBD.codigoTipoConceptoMayorValor+","+ConstantesBD.codigoEstadoAplicacionPagosAprobado+") " +
    										"		- getvalconceptosaplipagos(pge.codigo,"+ConstantesBD.codigoTipoConceptoMenorValor+","+ConstantesBD.codigoEstadoAplicacionPagosAprobado+") " +
    										"		- getvalofacaplipagos(pge.codigo," + ConstantesBD.codigoEstadoAplicacionPagosAprobado+") " +
    										"		)>0 " +
    										"and (pge.estado ="+ConstantesBD.codigoEstadoPagosRecaudado+" or pge.estado ="+ConstantesBD.codigoEstadoPagosAplicado +") " +
    										"and pge.institucion=? ";
    
    /**
     * Cadena para ordenar el listado
     */
    private static String ordConPagGeneralEmpresa=" order by pge.tipo_doc,convertiranumero(pge.documento||'')";
    
    /**
     * Cadena que tiene definidos los campos de la consulta del select
     */
    private static String conceptosSelect="SELECT " +
    											"cap.concepto_pagos as codigoconcepto," +
    											"cp.descripcion as desconcepto," +
    											"cp.tipo as tipoconcepto," +
    											"cap.valor_base as valorbase," +
    											"cap.valor_concepto as valorconcepto," +
    											"cap.porcentaje as porcentaje, " +
    											ValoresPorDefecto.getValorTrueParaConsultas()+" as bd ";
    /**
     * Cadena que tiene definido el from de la consulta de conceptos
     */
    private static String conceptosFrom=" from conceptos_apli_pagos cap " +
    									"inner join concepto_pagos cp on(cp.codigo=cap.concepto_pagos)";
    
    /**
     * Cadena que tiene definido la condicion where de la consulta de conceptos";
     */
    private static String conceptosWhere=" where cap.aplica_pagos_empresa=?";
    
    
    /**
     * Select para la consulta de la tabla aplicaciones_pagos_cxc
     */
    private static String selConsultaAplicacionCXC="SELECT " +
    												"apcxc.aplica_pagos_empresa as codaplicacion," +
    												"apcxc.numero_cuenta_cobro as cxc," +
    												"cxc.fecha_radicacion as fecharadicacion," +
    												"cxc.saldo_cuenta||'' as saldocxc," +
    												"apcxc.institucion as institucion," +
    												"apcxc.metodo_pago as codmetodopago," +
    												"totalValPagoCXC(apcxc.aplica_pagos_empresa,apcxc.numero_cuenta_cobro)||'' as valorpagofacturas," +
    												"(" +
    												"	SELECT case when sum(apf.valor_pago) is null then '0' else sum(apf.valor_pago)||'' end " +
    												"	from aplicacion_pagos_factura apf " +
    												"	inner join aplicacion_pagos_cxc apc on" +
    												"		(" +
    												"		apf.aplicacion_pagos=apc.aplica_pagos_empresa and " +
    												"		apf.numero_cuenta_cobro=apc.numero_cuenta_cobro" +
    												"		) " +
    												"	where apc.aplica_pagos_empresa=apcxc.aplica_pagos_empresa and apc.numero_cuenta_cobro<>apcxc.numero_cuenta_cobro" +
    												")||'' as totalvalotrascxc, " + //el total distribuido de la aplicacion en otras cuentas de cobro.
    												"'true' as bd ";
    
    /**
     * From para la consulta de la tabla aplicaciones_pagos_cxc
     */
    private static String froConsultaAplicacionCXC=" from aplicacion_pagos_cxc apcxc " +
    												"inner join cuentas_cobro cxc on (apcxc.numero_cuenta_cobro=cxc.numero_cuenta_cobro)";
    /**
     * Where para la consulta de la tabla aplicaciones_pagos_cxc
     */
    private static String wheConsultaAplicacionCXC=" where apcxc.aplica_pagos_empresa=?";
    
    /**
     * Order by para la consulta de la tabla aplicaciones_pagos_cxc
     */
    private static String ordConsultaAplicacionCCX=" order by cxc";
    
    /**
     * Select de la consulta de cuentas de cobro.
     */
    private static String selConsultaCuntasCobro="SELECT " +
    									"cxc.numero_cuenta_cobro as cxc," +
    									"cxc.fecha_radicacion as fecharadicacion," +
    									"cxc.saldo_cuenta as saldocxc," +
    									"'false' as seleccionado ";
    
    /**
     * From de la consulta de cuentas de cobro.
     */
    private static String froConsultaCuentasCobro=" from cuentas_cobro cxc";
    
    /**
     * where de la consulta de cuentas de cobro
     */
    private static String wheConsultaCuentasCobro=" where cxc.convenio=? and cxc.institucion=? and cxc.saldo_cuenta > 0 and cxc.estado=? and getNumAjusEstadoXCXC(cxc.numero_cuenta_cobro,cxc.institucion,"+ConstantesBD.codigoEstadoCarteraGenerado+") = 0  and getNumPagEstadoXCXC(cxc.numero_cuenta_cobro,cxc.institucion,"+ConstantesBD.codigoEstadoAplicacionPagosPendiente+") < 2";
    
    /**
     * Cadena para realizar la insercion en la BD de una aplicacion de pagos a niver de cuenta de cobro.
     */
    private static String cadenaInsertAplicacionPagosCXC="INSERT INTO aplicacion_pagos_cxc (aplica_pagos_empresa,numero_cuenta_cobro,institucion,metodo_pago) values(?,?,?,?)";
    
    /**
     * Cadena para actualizar la aplicacion de un pago a nivel de Cuenta de Cobro
     */
    private static String cadenaUpdateAplicacionPagosCXC="UPDATE aplicacion_pagos_cxc set metodo_pago=? where aplica_pagos_empresa=? and numero_cuenta_cobro=?";
    
    /**
     * Cadena que define los campos del select para la consulta de las
     * facturas.
     */
    private static String selConsultaFacturas="select " +
    													"f.codigo as codigofactura," +
    													"f.consecutivo_factura as consecutivofactura," +
    													"f.fecha as fecha," +
    													"f.institucion as institucion," +
    													"f.tipo_factura_sistema  as facturasistema," +
    													"f.centro_aten as codigocentroatencion," +
    													"getnomcentroatencion(f.centro_aten) as nombrecentroatencion,"+
    													"(f.valor_cartera+f.ajustes_debito-f.ajustes_credito-f.valor_pagos) as saldo," +
    													"'false' as seleccionado ";
    /**
     * Cadena que de fine el from de la consutla.
     */
    private static String froConsultaFacturas=" from facturas f";
    /**
     * Cadena que filtra las facturas obtenidas en la consulta.
     */
    private static String wheConsultaFacturasCuentaCobro=" where f.numero_cuenta_cobro=? and f.estado_facturacion=? and f.institucion=? and getNumAjusEstadoXFact(f.codigo,"+ConstantesBD.codigoEstadoCarteraGenerado+") = 0 and getNumPagEstadoXFac(f.codigo,"+ConstantesBD.codigoEstadoAplicacionPagosPendiente+") < 2 ";
    
    /**
     * Cadena para realizar la insercion de un pago de facturas.
     */
    private static String cadenaInsertAplicacionPagosFacturas="INSERT INTO aplicacion_pagos_factura (aplicacion_pagos,numero_cuenta_cobro,factura,valor_pago) values (?,?,?,?)";
    
    /**
     * Cadena para actualiza la aplicacion de un pago de facturas.
     */
    private static String cadenaUpdateAplicacionPagosFacturas="UPDATE aplicacion_pagos_factura set valor_pago=? where aplicacion_pagos=? and numero_cuenta_cobro=? and factura=?";
    

    /**
     * where de la consulta de facturas
     */
    private static String wheConsultaFacturas=" where f.convenio=? and f.institucion=? and (f.valor_cartera+f.ajustes_debito-f.ajustes_credito-f.valor_pagos) > 0 and f.estado_facturacion=? and cxc.estado=4 and f.numero_cuenta_cobro is not null and f.factura_cerrada=? and getNumAjusEstadoXFact(f.codigo,"+ConstantesBD.codigoEstadoCarteraGenerado+") = 0 and getNumPagEstadoXFac(f.codigo,"+ConstantesBD.codigoEstadoAplicacionPagosPendiente+") < 2 ";
    //////////////////////////////////FIN CADENAS SQL///////////////////////////////////////////////////////////////
    
    /**
     * @param con
     * @param codigoInstitucionInt
     * @return
     */
    public static HashMap consultarPagosGeneralEmpresa(Connection con, int codigoInstitucionInt)
    {
        HashMap mapa=new HashMap();
        mapa.put("numRegistros","0");
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(SelConPagGeneralEmpresa+wheConPagGeneralEmpresa+ordConPagGeneralEmpresa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoInstitucionInt);
            mapa=cargarValueObjectDocumentosPagos(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE FORMAS DE PAGO",e);
        }
        return (HashMap)mapa.clone();
    }
    
    /**
     * @param con
     * @param vo
     * @return
     */
    public static HashMap busquedaAvanzada(Connection con, HashMap vo)
    {
        String condicion=wheConPagGeneralEmpresa;
        HashMap mapa=new HashMap();
        mapa.put("numRegistros","0");
        if(Integer.parseInt(vo.get("tipo")+"")>ConstantesBD.codigoNuncaValido)
        {
            condicion=condicion+" and pge.tipo_doc="+vo.get("tipo");
        }
        if(!(vo.get("documento")+"").equals(""))
        {
            condicion=condicion+" and pge.documento='"+vo.get("documento")+"'";
        }
        if(!(vo.get("fecha")+"").equals(""))
        {
            condicion=condicion+" and pge.fecha_documento='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha")+"")+"'";
        }
        if(Integer.parseInt(vo.get("convenio")+"")>ConstantesBD.codigoNuncaValido)
        {
            condicion=condicion+" and pge.convenio="+vo.get("convenio");
        }
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(SelConPagGeneralEmpresa+condicion+ordConPagGeneralEmpresa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,Integer.parseInt(vo.get("institucion")+""));
            mapa=cargarValueObjectDocumentosPagos(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERRORE EJECUNTANDO LA CONSULTA DE FORMAS DE PAGO");
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
        HashMap mapa=new HashMap();
        int cont=0;
        while (rs.next())
        {
            mapa.put("codigo_"+cont,rs.getString("codigo"));
            mapa.put("codigoconvenio_"+cont,rs.getString("codigoconvenio"));
            mapa.put("nombreconvenio_"+cont,rs.getString("nombreconvenio"));
            mapa.put("codigotipo_"+cont,rs.getString("codigotipo"));
            mapa.put("destipo_"+cont,rs.getString("destipo"));
            mapa.put("acronimotipo_"+cont,rs.getString("acronimotipo"));
            mapa.put("documento_"+cont,rs.getString("documento"));
            mapa.put("consecutivodoc_"+cont,rs.getString("consecutivodoc"));   
            mapa.put("codigoestado_"+cont,rs.getString("codigoestado"));
            mapa.put("desestado_"+cont,rs.getString("desestado"));
            mapa.put("valordocumento_"+cont,rs.getString("valor"));
            mapa.put("fechadocumento_"+cont,rs.getString("fechadocumento"));
            mapa.put("aplicacionactual_"+cont,rs.getString("aplicacionactual"));
            if(Utilidades.convertirAEntero(rs.getObject("aplicacionactual")+"")==ConstantesBD.codigoNuncaValido)
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
        mapa.put("numRegistros", cont+"");
        return mapa;
    }

    
    /**
     * @param con
     * @param codigoAplicacionPago
     * @return
     */
    public static HashMap cargarConceptosPagos(Connection con, int codigoAplicacionPago)
    {
        HashMap mapa=new HashMap();
        mapa.put("numRegistros","0");
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(conceptosSelect+conceptosFrom+conceptosWhere,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoAplicacionPago);
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
        	logger.error("ERRORE EJECUNTANDO LA CONSULTA DE CONCEPTOS APLICACIONES PAGOS");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }

    


    
    /**
     * Metodo que realiza todo el proceso de guardar el encabezado de un 
     * pago incluyendo sus conceptos, el metodo verifica se es una modificacion,
     * o una insercion y la realiza de acuerdo al caso.
     * @param con
     * @param mapa
     * @param seq
     * @return
     */
    public static int guardarConceptosAplicacionPagos(Connection con, HashMap mapa, String seq)
    {
        PreparedStatementDecorator ps=null;
        boolean enTransaccion=true;
        try
        {
            util.UtilidadBD.iniciarTransaccion(con);
	        if(UtilidadTexto.getBoolean(mapa.get("modificacion")+""))
	        {
	            String actualizarAplicacionPagosEmpresa="UPDATE cartera.aplicacion_pagos_empresa set fecha_aplicacion=?,observaciones=? where codigo=?";
                ps= new PreparedStatementDecorator(con.prepareStatement(actualizarAplicacionPagosEmpresa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                ps.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD( mapa.get("fechaaplicacion")+"")));
                ps.setString(2,mapa.get("observaciones")+"");
                ps.setString(3, mapa.get("codigoaplicacion")+"");
                enTransaccion=(ps.executeUpdate()>0);
                if(enTransaccion)
                {
                	 eliminarConceptosAplicacion(con,Integer.parseInt(mapa.get("codigoaplicacion")+""));
                }
            }
	        else if(!UtilidadTexto.getBoolean(mapa.get("modificacion")+""))
	        {
	            String insertaAplicacionPagosEmpresa="insert into cartera.aplicacion_pagos_empresa (codigo,pagos_general_empresa,estado,numero_aplicacion,fecha_aplicacion,observaciones,usuario,fecha_grabacion,hora_grabacion) values ("+seq+",?,?,?,?,?,?,?,?)";
                ps= new PreparedStatementDecorator(con.prepareStatement(insertaAplicacionPagosEmpresa,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                ps.setString(1,mapa.get("pagogeneraempresa")+"");
                ps.setString(2,mapa.get("estadoaplicacion")+"");
                ps.setString(3,mapa.get("numeroaplicacion")+"");
                ps.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD( mapa.get("fechaaplicacion")+"")));
                ps.setString(5,mapa.get("observaciones")+"");
                ps.setString(6,mapa.get("usuario")+"");
                ps.setDate(7,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD( mapa.get("fechagrabacion")+"")));
                ps.setString(8,mapa.get("horagrabacion")+"");
                enTransaccion=false;
                if(ps.executeUpdate()>0)
                {
                    enTransaccion=(actualizarEstadoPagoEmpresa(con, Integer.parseInt(mapa.get("pagogeneraempresa")+""), Integer.parseInt(mapa.get("estadopago")+""))>0);
                }
	        }
	        if(enTransaccion)
	        {
	            for(int i=0;i<Integer.parseInt(mapa.get("numRegistros")+"");i++)
	            {
	                if(enTransaccion&&!UtilidadTexto.getBoolean(mapa.get("eliminado_"+i)+""))
	                {
	                    String cadena="INSERT INTO conceptos_apli_pagos (aplica_pagos_empresa,concepto_pagos,institucion,valor_base,porcentaje,valor_concepto) VALUES (getCodAplicacionPagoActual(?),?,?,?,?,?)";
	                    ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	                    ps.setString(1,mapa.get("pagogeneraempresa")+"");
	                    ps.setString(2,mapa.get("codigoconcepto_"+i)+"");
	                    ps.setString(3,mapa.get("institucion")+"");
	                    ps.setString(4,mapa.get("valorbase_"+i)+"");
	                    ps.setString(5,mapa.get("porcentaje_"+i)+"");
	                    ps.setString(6,mapa.get("valorconcepto_"+i)+"");
	                    enTransaccion=ps.executeUpdate()>0;
	                }
	            }
	            util.UtilidadBD.finalizarTransaccion(con);
	            return 1;
	        }
	        else
	        {
	            util.UtilidadBD.abortarTransaccion(con);
	        }
        }
        catch (SQLException e)
        {
            logger.error("se produjo un error guardando los ConceptosAplicacionPagos"+e);
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Metodo para eliminar los conceptos de una aplicacion
     * @param con
     * @param codigoPago
     * @return
     */
    public static int eliminarConceptosAplicacion(Connection con,int codigoPago)
    {
        PreparedStatementDecorator ps=null;
        String cadena="DELETE FROM conceptos_apli_pagos where aplica_pagos_empresa = ?";
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoPago);
            return ps.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return ConstantesBD.codigoNuncaValido;
    }
    
    public static int actualizarEstadoPagoEmpresa(Connection con,int codigoPago,int codigoNuevoEstado)
    {
        PreparedStatementDecorator ps=null;
        try
        {
            String cadena="UPDATE pagos_general_empresa SET estado = ? where codigo = ?";
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codigoNuevoEstado);
            ps.setInt(2, codigoPago);
            return ps.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.error("se produjo un error actualizando el estado del pago");
            e.printStackTrace();
        }
        return 0;
    }

    public static int actualizarEstadoAplicacionPago(Connection con,int codigoAplicacionPago,int codigoNuevoEstado)
    {
        PreparedStatementDecorator ps=null;
        try
        {
            String cadena="UPDATE cartera.aplicacion_pagos_empresa SET estado = ? where codigo = ?";
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codigoNuevoEstado);
            ps.setInt(2, codigoAplicacionPago);
            return ps.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.error("se produjo un error actualizando el estado del pago");
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Metodo que realiza la anulacion de un pago.
     * Si al anula la aplicacion, el documento del pago queda sin aplicaciones
     * en estado de pago de cartera aplicado, el sistema debe actualiza el
     * estado del pago de cartera del documento en recaudado.
     * @param con
     * @param mapa
     * @return
     */
    public static int anularAplicacionPago(Connection con, HashMap mapa)
    {
        boolean enTransaccion=true;
        util.UtilidadBD.iniciarTransaccion(con);
        enTransaccion=(actualizarEstadoAplicacionPago(con, Integer.parseInt(mapa.get("codigoaplicacion")+""), ConstantesBD.codigoEstadoAplicacionPagosAnulado)>0);
        if(enTransaccion)
        {
            enTransaccion=(eliminarConceptosAplicacion(con, Integer.parseInt(mapa.get("codigoaplicacion")+""))>0);
            if(enTransaccion)
            {
                enTransaccion=(elimnarAplicacionNivelFactura(con,Integer.parseInt(mapa.get("codigoaplicacion")+""))>0);
                if(enTransaccion)
                {
                    enTransaccion=(elimnarAplicacionNivelCXC(con,Integer.parseInt(mapa.get("codigoaplicacion")+""))>0);
                }
            }
        }
        if(enTransaccion)
        {
            try
            {
                String cadena="INSERT INTO anulacion_apli_pagos (aplica_pagos_empresa,usuario,motivo,fecha,hora) VALUES (?,?,?,?,?)";
                PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                ps.setString(1,mapa.get("codigoaplicacion")+"" );
                ps.setString(2,mapa.get("loginusuario")+"");
                ps.setString(3,mapa.get("motivo")+"" );
                ps.setDate(4,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD( mapa.get("fecha")+"")));
                ps.setString(5,mapa.get("hora")+"" );
                enTransaccion=(ps.executeUpdate()>0);
                cadena="SELECT count(1) as aplicaciones from cartera.aplicacion_pagos_empresa where estado=? and pagos_general_empresa=?";
                ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                ps.setInt(1, ConstantesBD.codigoEstadoAplicacionPagosAprobado);
                ps.setString(2,mapa.get("numeropago")+"" );
               new ResultSetDecorator(ps.executeQuery());
                ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
                if(rs.next())
                {
                    if(rs.getInt("aplicaciones")==0)
                    {
                        actualizarEstadoPagoEmpresa(con, Integer.parseInt(mapa.get("numeropago")+""), ConstantesBD.codigoEstadoPagosRecaudado);
                    }
                }
                
            }
            catch (SQLException e)
            {
                enTransaccion=false;
                logger.error("Error ingresando la anulacion");
                e.printStackTrace();
            }
        }
        if(enTransaccion)
        {
	        util.UtilidadBD.finalizarTransaccion(con);
	        return 1;
	    }
	    else
	    {
	        util.UtilidadBD.abortarTransaccion(con);
	    }
        return 0;
    }

    
    
    /**
     * @param con
     * @param i
     * @return
     */
    private static int elimnarAplicacionNivelCXC(Connection con, int codigoAplicacion)
    {
        PreparedStatementDecorator ps=null;
        String cadena="DELETE FROM aplicacion_pagos_cxc where aplica_pagos_empresa = ?";
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoAplicacion);
            return 1;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return ConstantesBD.codigoNuncaValido;
    }

    /**
     * @param con
     * @param i
     * @return
     */
    private static int elimnarAplicacionNivelFactura(Connection con, int codigoAplicacion)
    {
        PreparedStatementDecorator ps=null;
        String cadena="DELETE FROM aplicacion_pagos_factura where aplicacion_pagos = ?";
        try
        {
            ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codigoAplicacion);
            return 1;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return ConstantesBD.codigoNuncaValido;
    }

    
    /**
     * @param con
     * @param codAplicacionPago
     * @return
     */
    public static HashMap consultarPagosCuentaCobro(Connection con, Integer codAplicacionPago)
    {
        HashMap mapa=new HashMap();
        mapa.put("numRegistros","0");
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(selConsultaAplicacionCXC+froConsultaAplicacionCXC+wheConsultaAplicacionCXC+ordConsultaAplicacionCCX,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1,codAplicacionPago.intValue());
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
            Utilidades.imprimirMapa(mapa);
        }
        catch (SQLException e)
        {
            logger.error("ERRORE EJECUNTANDO LA CONSULTA DE APLICACIONES PAGOS CXC");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }

    
    /**
     * @param con
     * @param mapa
     * @return
     */
    public static HashMap buscarCuentasCobroConvenio(Connection con, HashMap mapaDatos)
    {
        HashMap mapa=new HashMap();
        mapa.put("numRegistros","0");
        try
        {
            String rest=wheConsultaCuentasCobro+" and cxc.numero_cuenta_cobro not in("+mapaDatos.get("cuentas")+")";
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(selConsultaCuntasCobro+froConsultaCuentasCobro+rest+ordConsultaAplicacionCCX,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1,mapaDatos.get("convenio")+"");
            ps.setString(2,mapaDatos.get("institucion")+"");
            ps.setInt(3, ConstantesBD.codigoEstadoCarteraRadicada);
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE CXC");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }

    
    /**
     * @param con
     * @param institucion
     * @param convenio
     * @param cxcBusAvanzada
     * @param cxc
     * @return
     */
    public static HashMap buscarCuentasCobroLLave(Connection con, int institucion, int convenio, String cxcBusAvanzada, String cxc)
    {
        HashMap mapa=new HashMap();
        mapa.put("numRegistros","0");
        try
        {
            String rest=" where cxc.numero_cuenta_cobro=? and cxc.institucion=? and cxc.saldo_cuenta > 0 and cxc.estado=? and cxc.convenio=? and cxc.numero_cuenta_cobro not in("+cxc+") and getNumAjusEstadoXCXC(cxc.numero_cuenta_cobro,cxc.institucion,"+ConstantesBD.codigoEstadoCarteraGenerado+") = 0 and getNumPagEstadoXCXC(cxc.numero_cuenta_cobro,cxc.institucion,"+ConstantesBD.codigoEstadoAplicacionPagosPendiente+") < 2";
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(selConsultaCuntasCobro+froConsultaCuentasCobro+rest+ordConsultaAplicacionCCX,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1,cxcBusAvanzada);
            ps.setInt(2,institucion);
            ps.setInt(3, ConstantesBD.codigoEstadoCarteraRadicada);
            ps.setInt(4, convenio);
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE CXC POR LLAVE");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }

    
    
    /**
     * @param con
     * @param vo
     * @return
     */
    public static int guardarAplicacionCXC(Connection con, HashMap vo)
    {
        PreparedStatementDecorator psInsert=null;
        PreparedStatementDecorator psUpdate=null;
        boolean enTransaccion=true;
        try
        {
            util.UtilidadBD.iniciarTransaccion(con);
            psInsert= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertAplicacionPagosCXC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            psUpdate= new PreparedStatementDecorator(con.prepareStatement(cadenaUpdateAplicacionPagosCXC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            for(int i=0;i<Integer.parseInt(vo.get("numRegistros")+"");i++)
            {
                if(UtilidadTexto.getBoolean(vo.get("bd_"+i)+""))
                {
                    if(UtilidadTexto.getBoolean(vo.get("eliminado_"+i)+""))
                    {
                        String cadena="delete from aplicacion_pagos_factura where aplicacion_pagos=? and numero_cuenta_cobro=?";
                        PreparedStatementDecorator psDelete= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                        psDelete.setString(1,vo.get("codaplicacion_"+i)+"");
                        psDelete.setString(2,vo.get("cxc_"+i)+"");
                        psDelete.executeUpdate();
                        cadena="delete from aplicacion_pagos_cxc where aplica_pagos_empresa=? and numero_cuenta_cobro=?";
                        psDelete= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                        psDelete.setString(1,vo.get("codaplicacion_"+i)+"");
                        psDelete.setString(2,vo.get("cxc_"+i)+"");
                        psDelete.executeUpdate();
                    }
                    else if(UtilidadTexto.getBoolean(vo.get("modificado_"+i)+""))
                    {
	                    psUpdate.setString(1,vo.get("codmetodopago_"+i)+"");
	                    psUpdate.setString(2,vo.get("codaplicacion_"+i)+"");
	                    psUpdate.setString(3,vo.get("cxc_"+i)+"");
	                    if(Integer.parseInt(vo.get("codmetodopago_"+i)+"")==ConstantesBD.tipoMetodoPagoManual||Double.parseDouble(vo.get("valorpagofacturas_"+i)+"")>0)
		                {
			                if(psUpdate.executeUpdate()<=0)
			                {
			                    enTransaccion=false;
			                    i=Integer.parseInt(vo.get("numRegistros")+"");
			                }
			                if(enTransaccion)
			                {
			                    if(Integer.parseInt(vo.get("codmetodopago_"+i)+"")==ConstantesBD.tipoMetodoPagoAutomatico||Integer.parseInt(vo.get("codmetodopago_"+i)+"")==ConstantesBD.tipoMetodoPagoPorcentual)
			                    {
			                        PreparedStatementDecorator psLocal= new PreparedStatementDecorator(con.prepareStatement("DELETE from aplicacion_pagos_factura where aplicacion_pagos=? and numero_cuenta_cobro=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			                        psLocal.setInt(1, Integer.parseInt(vo.get("codaplicacion_"+i)+""));
			                        psLocal.setDouble(2,Double.parseDouble(vo.get("cxc_"+i)+""));
			                        psLocal.executeUpdate();
				                    enTransaccion=(generarDistribucionAutomaticaFacturas(con,vo,i)>0);
			                    }
			                }
		                }
                    }	                
                }
                else
                {
                    psInsert.setString(1,vo.get("codaplicacion_"+i)+"");
	                psInsert.setString(2,vo.get("cxc_"+i)+"");
	                psInsert.setString(3,vo.get("institucion_"+i)+"");
	                psInsert.setString(4,vo.get("codmetodopago_"+i)+"");
	                if(Integer.parseInt(vo.get("codmetodopago_"+i)+"")==ConstantesBD.tipoMetodoPagoManual||Double.parseDouble(vo.get("valorpagofacturas_"+i)+"")>0)
	                {
		                if(psInsert.executeUpdate()<=0)
		                {
		                    enTransaccion=false;
		                    i=Integer.parseInt(vo.get("numRegistros")+"");
		                }
		                if(enTransaccion)
		                {
		                    if(Integer.parseInt(vo.get("codmetodopago_"+i)+"")==ConstantesBD.tipoMetodoPagoAutomatico||Integer.parseInt(vo.get("codmetodopago_"+i)+"")==ConstantesBD.tipoMetodoPagoPorcentual)
		                    {
			                    enTransaccion=(generarDistribucionAutomaticaFacturas(con,vo,i)>0);
		                    }
		                }
	                }
                }
            }
        }
        catch (SQLException e)
        {
            logger.error("se produjo un error guardando los ConceptosAplicacionPagos"+e);
            e.printStackTrace();
            enTransaccion=false;
        }
        if(enTransaccion)
        {
            util.UtilidadBD.finalizarTransaccion(con);
            return 1;
        }
        util.UtilidadBD.abortarTransaccion(con);
        return ConstantesBD.codigoNuncaValido;
    }

    
    
    
    /**
     * @param con
     * @param vo
     * @param i index
     */
    private static int generarDistribucionAutomaticaFacturas(Connection con, HashMap vo, int i)
    {
        HashMap voFacturas=new HashMap();
        double valDistrubuir=Double.parseDouble(vo.get("valorpagofacturas_"+i)+"");
        voFacturas=cargarFacturasCuentaCobro(con,Double.parseDouble(vo.get("cxc_"+i)+""),Integer.parseInt(vo.get("institucion_"+i)+""));
        voFacturas.put("codaplicacion", vo.get("codaplicacion_"+i));
        if(Integer.parseInt(vo.get("codmetodopago_"+i)+"")==ConstantesBD.tipoMetodoPagoAutomatico)
        {
	        for(int j=0;j<Integer.parseInt(voFacturas.get("numRegistros")+"");j++)
	        {
	            if((valDistrubuir-Double.parseDouble(voFacturas.get("saldo_"+j)+"")>0))
	            {
	                voFacturas.put("valpago_"+j,voFacturas.get("saldo_"+j));
	                valDistrubuir=valDistrubuir-Double.parseDouble(voFacturas.get("saldo_"+j)+"");
	            }
	            else
	            {
	                voFacturas.put("valpago_"+j, valDistrubuir+"");
	                voFacturas.put("numRegistros",(j+1)+"");
	            }
	            voFacturas.put("bd_"+j,"false");
	            voFacturas.put("cxc_"+j, vo.get("cxc_"+i));
	        }
        }
        else if(Integer.parseInt(vo.get("codmetodopago_"+i)+"")==ConstantesBD.tipoMetodoPagoPorcentual)
        {
            double valAplicado=0,valAplicar=0;
            int j=0;
            for(j=0;j<Integer.parseInt(voFacturas.get("numRegistros")+"")-1;j++)
	        {
	            valAplicar=Math.round(valDistrubuir*Double.parseDouble(voFacturas.get("saldo_"+j)+"")/Double.parseDouble(vo.get("saldocxc_"+i)+""));
	            voFacturas.put("valpago_"+j,valAplicar+"");
	            valAplicado=valAplicado+valAplicar;
	            voFacturas.put("bd_"+j,"false");
	            voFacturas.put("cxc_"+j, vo.get("cxc_"+i));
	        }
            voFacturas.put("valpago_"+j,(valDistrubuir-valAplicado)+"");
            voFacturas.put("bd_"+j,"false");
            voFacturas.put("cxc_"+j, vo.get("cxc_"+i));
        }
        return guardarAplicacionFacturas(con,voFacturas);
    }

    /**
     * @param cxc
     * @param con
     * @param institucion
     * @return
     */
    private static HashMap cargarFacturasCuentaCobro(Connection con, double cxc, int institucion)
    {
        HashMap mapa=new HashMap();
        mapa.put("numRegistros","0");
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(selConsultaFacturas+froConsultaFacturas+wheConsultaFacturasCuentaCobro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setDouble(1,cxc);
            ps.setInt(2, ConstantesBD.codigoEstadoFacturacionFacturada);
            ps.setInt(3, institucion);
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERRORE EJECUNTANDO LA CONSULTA DE FACTURAS DE UNA CUENTA DE COBRO");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }

    /**
     * Metodo que guarda la aplicacion de un pago a nivel de facturas.
     * no es transaccional, la transaccion debe manejarse desde un metodo anterior.
     * @param con
     * @param voFacturas
     * @return
     */
    private static int guardarAplicacionFacturas(Connection con, HashMap vo)
    {
        PreparedStatementDecorator psInsert=null;
        PreparedStatementDecorator psUpdate=null;
        try
        {
            psInsert= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertAplicacionPagosFacturas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            psUpdate= new PreparedStatementDecorator(con.prepareStatement(cadenaUpdateAplicacionPagosFacturas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            for(int i=0;i<Integer.parseInt(vo.get("numRegistros")+"");i++)
            {
                if(UtilidadTexto.getBoolean(vo.get("bd_"+i)+""))
                {
                    if(UtilidadTexto.getBoolean(vo.get("eliminado_"+i)+""))
                    {
                        String cadena="delete from aplicacion_pagos_factura where aplicacion_pagos=? and factura=?";
                        PreparedStatementDecorator psDelete= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                        psDelete.setString(1,vo.get("codaplicacion")+"");
                        psDelete.setString(2,vo.get("codigofactura_"+i)+"");
                        psDelete.executeUpdate();
                    }
                    else
                    {
	                    //creo que no es necesario, los sabré cuando haga la parate de por facturas
	                    //if(UtilidadTexto.getBoolean(vo.get("modificado_"+i)+""))
	                    {
	                        if(Double.parseDouble(vo.get("valpago_"+i)+"")>0)
	                        {
		                        psUpdate.setString(1,vo.get("valpago_"+i)+"");
			                    psUpdate.setString(2,vo.get("codaplicacion")+"");
			                    psUpdate.setString(3,vo.get("cxc_"+i)==null?vo.get("cxc")+"":vo.get("cxc_"+i)+"");//el numero de la cuenta do cobro podría venir de las 2 formas
			                    psUpdate.setString(4,vo.get("codigofactura_"+i)+"");
			                    psUpdate.executeUpdate();
				                if(psUpdate.executeUpdate()<=0)
				                {
				                    i=Integer.parseInt(vo.get("numRegistros")+"");
				                }
	                        }
	                    }
                    }
                }
                else
                {
                    if(Double.parseDouble(vo.get("valpago_"+i)+"")>0)
                    {
		                psInsert.setString(1,vo.get("codaplicacion")+"");
		                psInsert.setString(2,vo.get("cxc_"+i)==null?vo.get("cxc")+"":vo.get("cxc_"+i)+"");//el numero de la cuenta do cobro podría venir de las 2 formas
		                psInsert.setString(3,vo.get("codigofactura_"+i)+"");
		                psInsert.setString(4,vo.get("valpago_"+i)+"");
		                if(psInsert.executeUpdate()<=0)
		                {
		                    i=Integer.parseInt(vo.get("numRegistros")+"");
		                }
                    }
                }
            }
            return 1;
        }
        catch (SQLException e)
        {
            logger.error("se produjo un error guardando la distribucion a nivel de facturas"+e);
            e.printStackTrace();
        }
        return ConstantesBD.codigoNuncaValido;
    }

    
    /**
     * @param con
     * @param aplicacionPago
     * @param cxc
     * @return
     */
    public static HashMap consultarPagosFacturas(Connection con, int aplicacionPago, double cxc)
    {
        String cadena=selConsultaFacturas+",apf.valor_pago as valpago,'true' as bd from aplicacion_pagos_factura apf inner join facturas f on(apf.factura=f.codigo) where apf.aplicacion_pagos=? and apf.numero_cuenta_cobro=? order by consecutivofactura";
        HashMap mapa=new HashMap();
        mapa.put("numRegistros","0");
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, aplicacionPago);
            ps.setDouble(2, cxc);
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERRORE EJECUNTANDO LA CONSULTA DE FACTURAS DE UNA APLICACION DE PAGOS DE CXC");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }

    /**
     * Metodo que consulta la aplicacion a niver de facturas de un pago sin importar la CXC
     * @param con
     * @param aplicacionPago
     * @param cxc
     * @return
     */
    public static HashMap consultarPagosFacturas(Connection con, int aplicacionPago)
    {
        String cadena=selConsultaFacturas+",apf.numero_cuenta_cobro as cxc,apf.valor_pago as valpago,'true' as bd from aplicacion_pagos_factura apf inner join facturas f on(apf.factura=f.codigo) where apf.aplicacion_pagos=? order by consecutivofactura";
        HashMap mapa=new HashMap();
        mapa.put("numRegistros","0");
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, aplicacionPago);
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERRORE EJECUNTANDO LA CONSULTA DE FACTURAS DE UNA APLICACION DE PAGOS DE CXC");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
    }

    
    /**
     * @param con
     * @param vo
     * @return
     */
    public static int guardarAplicacionPagosFacturas(Connection con, HashMap vo)
    {
        util.UtilidadBD.iniciarTransaccion(con);
        if(guardarAplicacionFacturas(con, vo)>0)
        {
            util.UtilidadBD.finalizarTransaccion(con);
            return 1;
        }
        else
        {
            util.UtilidadBD.abortarTransaccion(con);
            return ConstantesBD.codigoNuncaValido;
        }
    }

    
    /**
     * @param con
     * @param mapa
     * @return
     */
    public static HashMap buscarFacturasConvenio(Connection con, HashMap mapaDatos)
    {
        HashMap mapa=new HashMap();
        mapa.put("numRegistros","0");
        try
        {
            String select=selConsultaFacturas+",f.numero_cuenta_cobro as cxc,0 as valpago,'false' as bd ";
            String rest=wheConsultaFacturas+" and f.codigo not in("+mapaDatos.get("facturas")+")";
            String from=froConsultaFacturas+" inner join cuentas_cobro cxc on(cxc.numero_cuenta_cobro=f.numero_cuenta_cobro) ";
            String order=" order by f.consecutivo_factura";
            if(UtilidadTexto.getBoolean(mapaDatos.get("busxcxc")+""))
            {
                rest=rest+" and f.numero_cuenta_cobro="+mapaDatos.get("cxc");
            }
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(select+from+rest+order,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1,mapaDatos.get("convenio")+"");
            ps.setString(2,mapaDatos.get("institucion")+"");
            ps.setInt(3, ConstantesBD.codigoEstadoFacturacionFacturada);
            ps.setBoolean(4, false);
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
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
    public static HashMap buscarFacturaLLave(Connection con, int institucion, int convenio, String facturaBusquedaAvanzada, String facturas)
    {
        HashMap mapa=new HashMap();
        mapa.put("numRegistros","0");
        try
        {
            String select=selConsultaFacturas+",f.numero_cuenta_cobro as cxc,0 as valpago,'false' as bd ";
            String rest=wheConsultaFacturas+" and f.codigo not in("+facturas+") and f.consecutivo_factura=? ";
            String from=froConsultaFacturas+" inner join cuentas_cobro cxc on(cxc.numero_cuenta_cobro=f.numero_cuenta_cobro) ";
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(select+from+rest,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1,convenio+"");
            ps.setString(2,institucion+"");
            ps.setInt(3, ConstantesBD.codigoEstadoFacturacionFacturada);
            ps.setBoolean(4, false);
            ps.setString(5,facturaBusquedaAvanzada);
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
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
     * @param cxc
     * @param facturaBusquedaAvanzada
     * @param facturas
     * @return
     */
    public static HashMap buscarFacturaLLave(Connection con, int institucion, int convenio, double cxc, String facturaBusquedaAvanzada, String facturas)
    {
        HashMap mapa=new HashMap();
        mapa.put("numRegistros","0");
        try
        {
            String select=selConsultaFacturas+",f.numero_cuenta_cobro as cxc,0 as valpago,'false' as bd ";
            String rest=wheConsultaFacturas+" and f.codigo not in("+facturas+") and f.consecutivo_factura=? and f.numero_cuenta_cobro="+cxc;
            String from=froConsultaFacturas+" inner join cuentas_cobro cxc on(cxc.numero_cuenta_cobro=f.numero_cuenta_cobro) ";
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(select+from+rest,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setString(1,convenio+"");
            ps.setString(2,institucion+"");
            ps.setInt(3, ConstantesBD.codigoEstadoFacturacionFacturada);
            ps.setBoolean(4, false);
            ps.setString(5,facturaBusquedaAvanzada);
            mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
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
     * @param vo
     * @return
     */
    public static int guardarAplicacionFacturasCasoFacturas(Connection con, HashMap vo)
    {
        util.UtilidadBD.iniciarTransaccion(con);
        boolean enTransaccion=true;
        try
        {
            PreparedStatementDecorator psInsertCXC= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertAplicacionPagosCXC,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement("SELECT aplica_pagos_empresa from aplicacion_pagos_cxc where aplica_pagos_empresa = ? and numero_cuenta_cobro= ?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            for(int i=0;i<Integer.parseInt(vo.get("numRegistros")+"");i++)
            {
                ps.setString(1,vo.get("codaplicacion")+"");
                ps.setString(2,vo.get("cxc_"+i)==null?vo.get("cxc")+"":vo.get("cxc_"+i)+"");//el numero de la cuenta do cobro podría venir de las 2 formas
                if(!(new ResultSetDecorator(ps.executeQuery())).next())
                {
                    psInsertCXC.setString(1,vo.get("codaplicacion")+"");
                    psInsertCXC.setString(2,vo.get("cxc_"+i)==null?vo.get("cxc")+"":vo.get("cxc_"+i)+"");//el numero de la cuenta do cobro podría venir de las 2 formas
                    psInsertCXC.setInt(3,Integer.parseInt(vo.get("institucion_"+i)+""));
                    psInsertCXC.setString(4,ConstantesBD.tipoMetodoPagoManual+"");
                    enTransaccion=(psInsertCXC.executeUpdate()>0);
                }
            }
        }
        catch (SQLException e)
        {
            enTransaccion=false;
            logger.error("error ingresando la cuenta cobro en facturas",e);
        }
        if(enTransaccion)
        {
            enTransaccion=(guardarAplicacionFacturas(con, vo)>0);
        }
        if(enTransaccion)
        {
            util.UtilidadBD.finalizarTransaccion(con);
            return 1;
        }
        else
        {
            util.UtilidadBD.abortarTransaccion(con);
            return ConstantesBD.codigoNuncaValido;
        }
    }
}
