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
import util.Utilidades;

/**
 * Fecha: Abril de 2008
 * @author axioma
 * Mauricio Jaramillo H.
 */

public class SqlBaseAprobacionAnulacionPagosFacturasVariasDao
{

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger = Logger.getLogger(SqlBasePagosFacturasVariasDao.class);

	/**
	 * Cadena SELECT para consultar las aplicaciones de pagos de facturas varias
	 */
	private static String strConApliPagosFacturasVarias = 
													"SELECT "+
														"pge.tipo_doc as codigotipo, "+
														"tdp.descripcion as destipo, "+
			    										"tdp.acronimo as acronimotipo, "+
			    										"pge.documento as documento, "+
			    										"pge.codigo as codigopagosempresa, "+
			    										"to_char(pge.fecha_documento, 'DD/MM/YYYY') as fechadocumento, "+
			    										"apf.codigo as codigoaplicacion, "+
			    										"coalesce(to_char(apf.fecha_aplicacion, 'DD/MM/YYYY'), '') as fecha, "+
			    										"apf.usuario as usuario, "+
														"apf.pagos_general_fvarias as pagos, "+
														"getTotalConcApliPagFactVarias(apf.codigo) as valor_conceptos, "+
														"getTotalVlrFactVariasApliPagos(apf.codigo) as valor_facturas, "+
														"apf.estado as estado, "+
														"esp.descripcion as nombreestado, "+
														"pge.deudor as codigodeudor, "+
														"coalesce(apf.observaciones,'') as observaciones, "+
														"CASE " +
															"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoPaciente+"' THEN getnombrepersona(d.codigo_paciente)  " +
															"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa+"' THEN getnombreempresa(d.codigo_empresa)  " +
															"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoTipoDeudorOtros+"' THEN getdescripciontercero(d.codigo_tercero)  " +
															"WHEN d.tipo= '"+ConstantesIntegridadDominio.acronimoOtro+"' THEN  d.primer_nombre ||'  '|| d.primer_apellido " +
															"WHEN pge.deudor IS NULL THEN 'No Hay Deudor Registrado' " +
														"END AS nombredeudor, "+
														"CASE " +
															"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoPaciente+"' THEN getidentificacionpaciente(d.codigo_paciente)  " +
															"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa+"' THEN getnitempresa(d.codigo_empresa)  " +
															"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoTipoDeudorOtros+"' THEN getnittercero(d.codigo_tercero)  " +
															"WHEN d.tipo= '"+ConstantesIntegridadDominio.acronimoOtro+"' THEN  d.numero_identificacion " +
															"WHEN pge.deudor IS NULL THEN '' " +
														"END AS identificaciondeudor "+
													"FROM "+
														"aplicacion_pagos_fvarias apf "+
														"INNER JOIN pagos_general_empresa pge ON (apf.pagos_general_fvarias=pge.codigo) "+
														"INNER JOIN tipos_doc_pagos tdp ON (tdp.codigo=pge.tipo_doc) "+
														"INNER JOIN estados_aplicaciones_pagos esp ON (apf.estado=esp.codigo) " +
														"LEFT OUTER JOIN facturasvarias.deudores d ON (d.codigo=pge.deudor) ";
	
	/**
	 * Cadena WHERE para consultar las aplicaciones de pagos de facturas varias
	 */
	private static String strWheApliPagosFacturasVarias =
													"WHERE "+
														"apf.estado = "+ConstantesBD.codigoEstadoAplicacionPagosPendiente+" "+
														"AND pge.institucion = ? ";
	
	/**
     * Cadena ORDER BY para la consulta del listado de documentos pendientes que pertenecen a deudores
     */
    private static String strOrdApliPagosFacturasVarias =
										    	"ORDER BY "+
													"pge.tipo_doc, convertiranumero(pge.documento||'')";
	
    /**
	 * Cadena SELECT para consultar las facturas aplicadas por el deudor seleccionado
	 */
	private static final String strConFactApliPagosDeu =
													"SELECT "+
														"apff.valor_pago as valorpago, "+
														"apff.factura AS codigofactura, "+
														"getconsecutivofacturavarias(apff.factura) as facturas, "+
														"getdescripciontercero(pge.deudor) as nombredeudor "+
													"FROM "+
														"aplicac_pagos_factura_fvarias apff "+
														"INNER JOIN aplicacion_pagos_fvarias apf ON (apff.aplicacion_pagos = apf.codigo) "+
														"INNER JOIN pagos_general_empresa pge ON (apf.pagos_general_fvarias = pge.codigo) "+
													"WHERE "+
														"apf.codigo = ? "+
														"AND pge.deudor = ? "+
													"ORDER BY apff.factura";
	
	/**
	 * Cadena UPDATE para actualizar los campos de la aplicacion de pagos de facturas varias
	 */
	private static final String strUpdateApliPagosFacturasVarias = 
													"UPDATE "+
														"aplicacion_pagos_fvarias "+
													"SET "+
														"usuario_anula = ?, "+
														"motivo_anula = ?, "+
														"fecha_anula = ?, "+
														"hora_anula = ?, "+
														"estado = ? "+
													"WHERE "+
														"codigo = ? ";
	
	/**
	 * Cadena DELETE para borrar los conceptos de pagos asociados a la aplicacion de pago anulada
	 */
	private static final String strDelConApliPagosFacturasVarias =
															"DELETE "+
															"FROM "+
																"conceptos_apli_pagos_fvarias "+
															"WHERE "+
																"aplicacion_pagos_fvarias = ?";
	
	/**
	 * Cadena DELETE para borrar las facturas aplicadas asociadas a la aplicacion de pago anulada
	 */
	private static final String strDelFactApliPagosFacturasVarias =
															"DELETE "+
															"FROM "+
																"aplicac_pagos_factura_fvarias "+
															"WHERE "+
																"aplicacion_pagos = ?";
	
	/**
	 * Cadena SELECT que consulta si existe mas aplicaciones en estado anulado o aprobado para volver a recaudado el estado del pago general
	 */
	private static final String strConNumApliPagosFacturasVarias =
															"SELECT "+
																"count(*) as numero "+
															"FROM "+
																"aplicacion_pagos_fvarias "+
															"WHERE "+
																"(estado = "+ConstantesBD.codigoEstadoAplicacionPagosPendiente+" OR estado = "+ConstantesBD.codigoEstadoAplicacionPagosAprobado+") "+
																"AND pagos_general_fvarias = ?";
	
	/**
	 * Cadena SELECT que consulta si existen conceptos de aplicacion de pagos para la aplicacion de pagos de factura seleccionada
	 */
	private static final String strConNumConApliPagosFacturasVarias =
															"SELECT "+
																"count(*) as numero "+
															"FROM "+
																"conceptos_apli_pagos_fvarias "+
															"WHERE "+
																"aplicacion_pagos_fvarias = ?";
	
	/**
	 * Cadena SELECT que consulta si existen facturas de aplicacion de pagos para la aplicacion de pagos de factura seleccionada
	 */
	private static final String strConNumFactApliPagosFacturasVarias =
															"SELECT "+
																"count(*) as numero "+
															"FROM "+
																"aplicac_pagos_factura_fvarias "+
															"WHERE "+
																"aplicacion_pagos = ?";
	/**
	 * Cadena UPDATE que devuelve el estado del pago general a recaudado si se ha anulado la aplicacion y no existen mas aplicaciones pendientes o aprobadas
	 */
	private static final String strUpdatePagosGeneral = 
													"UPDATE "+
														"pagos_general_empresa "+
													"SET "+
														"estado = "+ConstantesBD.codigoEstadoPagosRecaudado+" "+
													"WHERE "+
														"codigo = ? ";
	/**
	 * Cadena UPDATE valor pagos facturas varias
	 */
	private static final String strUpdateFacturasVarias =
													"UPDATE "+
														"facturas_varias "+
													"SET "+
														"valor_pagos = coalesce(valor_pagos, 0) + ? "+
													"WHERE "+
														"codigo_fac_var = ? ";
	
	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static HashMap consultarAplicacionesPagosFacturasVarias(Connection con, int codigoInstitucionInt)
    {
        HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        try
        {
            PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(strConApliPagosFacturasVarias+strWheApliPagosFacturasVarias+strOrdApliPagosFacturasVarias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ps.setInt(1, codigoInstitucionInt);
            logger.info("-------->Consulta: "+strConApliPagosFacturasVarias+strWheApliPagosFacturasVarias+strOrdApliPagosFacturasVarias);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE APLICACION DE PAGOS DE FACTURAS VARIAS");
            e.printStackTrace();
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
		String condicion = strWheApliPagosFacturasVarias;
		HashMap mapa = new HashMap();
		mapa.put("numRegistros","0");
		if(Utilidades.convertirAEntero(vo.get("tipo")+"")>ConstantesBD.codigoNuncaValido)
			condicion=condicion+" AND pge.tipo_doc="+vo.get("tipo");
		if(!(vo.get("documento")+"").equals(""))
			condicion=condicion+" AND pge.documento="+vo.get("documento");
		if(!(vo.get("fecha")+"").equals(""))
			condicion=condicion+" AND pge.fecha_documento='"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fecha")+"")+"'";
		if(Utilidades.convertirAEntero(vo.get("deudor")+"")>ConstantesBD.codigoNuncaValido)
			condicion=condicion+" AND pge.deudor="+vo.get("deudor");
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(strConApliPagosFacturasVarias+condicion+strOrdApliPagosFacturasVarias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(vo.get("institucion")+""));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("ERROR EJECUNTANDO LA CONSULTA DE PAGOS FACTURAS VARIAS EN BUSQUEDA AVANZADA");
			e.printStackTrace();
		}
		return (HashMap)mapa.clone();
	}
	
	/**
	 * @param con
	 * @param codigoDeudor
	 * @param codigoAplicacionPago
	 * @return
	 */
	public static HashMap busquedaFacturasDeudores(Connection con, int codigoDeudor, int codigoAplicacionPago)
	{
		HashMap mapa = new HashMap();
		mapa.put("numRegistros","0");
		logger.info("====>Consulta Facturas Aplicadas de los Deudores: "+strConFactApliPagosDeu);
		logger.info("====>Codigo Aplicacion Pago: "+codigoAplicacionPago+" =====>Codigo Deudor: "+codigoDeudor);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(strConFactApliPagosDeu,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoAplicacionPago);
			ps.setInt(2, codigoDeudor);
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}
		catch (SQLException e)
		{
			logger.error("ERROR EJECUNTANDO LA CONSULTA DE PAGOS FACTURAS VARIAS EN BUSQUEDA AVANZADA");
			e.printStackTrace();
		}
		return (HashMap)mapa.clone();
	}
	
	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarAprobado(Connection con, HashMap vo)
	{
		boolean enTransaccion = false;
		try
		{
	  		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strUpdateApliPagosFacturasVarias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	  		ps.setString(1, vo.get("usuarioaprobanul")+"");
        	ps.setString(2, "");
        	ps.setDate(3, Date.valueOf(vo.get("fechaaprobanul")+""));
        	ps.setString(4, vo.get("horaaprobanul")+"");
        	ps.setInt(5, Utilidades.convertirAEntero(vo.get("estado")+""));
        	ps.setInt(6, Utilidades.convertirAEntero(vo.get("codigoAplicacion")+""));
        	enTransaccion = (ps.executeUpdate()>0);
        	if(enTransaccion)
        	{
        		logger.info("SE ACTUALIZO EL ESTADO DE APROBACION CORRECTAMENTE");
        		return true;
        	}
        	else
        		return false;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarAprobadoFacturas(Connection con, HashMap vo)
	{
		boolean enTransaccion = false;
		try
		{
			//ACTUALIZACION DEL VALOR DEL PAGO DE LA FACTURA EN FACTURAS VARIAS
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strUpdateFacturasVarias,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		ps.setDouble(1, Utilidades.convertirADouble(vo.get("valorPagoFacturaVarias")+""));
    		ps.setDouble(2, Utilidades.convertirADouble(vo.get("codigoFacturaVarias")+""));
    		logger.info("===>Cadena Actualizacion Facturas: "+strUpdateFacturasVarias+" ===>Valor Pagos Facturas: "+Utilidades.convertirADouble(vo.get("valorPagoFacturaVarias")+"")+" ===>Codigo Facturas: "+Utilidades.convertirADouble(vo.get("codigoFacturaVarias")+""));
    		enTransaccion = (ps.executeUpdate()>0);
        	if(enTransaccion)
        	{
        		logger.info("SE ACTUALIZO LOS PAGOS DE LAS FACTURAS DE LA APLICACION CORRECTAMENTE");
        		return true;
        	}
        	else
        	{
        		logger.info("NO SE ACTUALIZO LOS PAGOS DE LAS FACTURAS DE LA APLICACION CORRECTAMENTE");
        		return false;
        	}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public static boolean modificarAnulado(Connection con, HashMap vo)
	{
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
        boolean enTransaccion = true;
        int tempo = 0;
        try
		{
        	ps =  new PreparedStatementDecorator(con.prepareStatement(strUpdateApliPagosFacturasVarias, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
        	
        	ps.setString(1, vo.get("usuarioaprobanul")+"");
        	ps.setString(2, vo.get("motivoanulacion")+"");
        	ps.setDate(3, Date.valueOf(vo.get("fechaaprobanul")+""));
        	ps.setString(4, vo.get("horaaprobanul")+"");
        	ps.setInt(5, Utilidades.convertirAEntero(vo.get("estado")+""));
        	ps.setInt(6, Utilidades.convertirAEntero(vo.get("codigoAplicacion")+""));
        	enTransaccion = (ps.executeUpdate()>0);
        	if(enTransaccion)
        	{
        		//ELIMINACION CONCEPTOS APLICADOS
        		ps =  new PreparedStatementDecorator(con.prepareStatement(strConNumConApliPagosFacturasVarias, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
        		ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigoAplicacion")+""));
        		rs =new ResultSetDecorator(ps.executeQuery());
        		if(rs.next())
        		{
        			tempo = rs.getInt("numero");
        			if(tempo > 0)
        			{
        				ps =  new PreparedStatementDecorator(con.prepareStatement(strDelConApliPagosFacturasVarias, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
                    	ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigoAplicacion")+""));
                    	enTransaccion = (ps.executeUpdate()>0);
                    	if(!enTransaccion)
                    	{
                    		logger.info("ERROR EJECUTANDO LA ELIMINACION DE LOS CONCEPTOS DE LA APLICACION DE PAGOS");
                    		return false;
                    	}
        			}
        		}
        		
        		//ELIMINACION FACTURAS APLICADAS
        		ps =  new PreparedStatementDecorator(con.prepareStatement(strConNumFactApliPagosFacturasVarias, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
        		ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigoAplicacion")+""));
        		rs =new ResultSetDecorator(ps.executeQuery());
        		if(rs.next())
        		{
        			tempo = rs.getInt("numero");
        			if(tempo > 0)
        			{
        				ps =  new PreparedStatementDecorator(con.prepareStatement(strDelFactApliPagosFacturasVarias, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
                    	ps.setInt(1, Utilidades.convertirAEntero(vo.get("codigoAplicacion")+""));
                    	enTransaccion = (ps.executeUpdate()>0);
                    	if(!enTransaccion)
                    	{
                    		logger.info("ERROR EJECUTANDO LA ELIMINACION DE LAS FACTURAS DE LA APLICACION DE PAGOS");
                    		return false;
                    	}
        			}
        		}
        		
        		//CONSULTA SI EXISTE MAS APLICACIONES
        		if(enTransaccion)
                {
                	ps =  new PreparedStatementDecorator(con.prepareStatement(strConNumApliPagosFacturasVarias, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
                   	ps.setInt(1, Utilidades.convertirAEntero(vo.get("pagos")+""));
                   	rs =new ResultSetDecorator(ps.executeQuery());
                   	if(rs.next())
                   	{
                   		tempo = rs.getInt("numero");
                   		if(tempo == 0)
                   		{
                   			ps =  new PreparedStatementDecorator(con.prepareStatement(strUpdatePagosGeneral, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
                           	ps.setInt(1, Utilidades.convertirAEntero(vo.get("pagos")+""));
                           	enTransaccion = (ps.executeUpdate()>0);
                           	logger.info("SE REALIZO TODO EL PROCESO DE ANULACION CORRECTAMENTE CAMBIANDO ESTADO DE PAGOS GENERAL EMPRESA");
                           	return true;
                   		}
                   		else
                   		{
                   			logger.info("SE REALIZO TODO EL PROCESO DE ANULACION CORRECTAMENTE SIN CAMBIAR EL PAGO GENERAL EMPRESA");
                           	return true;
                   		}
                   	}
                }
        	}
        	else
        	{
        		logger.info("ERROR EJECUTANDO LA ACTUALIZACION DE LA APLICACION DE PAGO");
        		return false;
        	}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
}