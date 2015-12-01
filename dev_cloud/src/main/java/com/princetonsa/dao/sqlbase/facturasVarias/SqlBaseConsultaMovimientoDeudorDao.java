package com.princetonsa.dao.sqlbase.facturasVarias;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;

/**
 * @author Mauricio Jllo
 * Fecha: Agosto de 2008
 */
public class SqlBaseConsultaMovimientoDeudorDao
{

	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseConsultaMovimientoDeudorDao.class);
	
	/**
	 * Cadena SELECT para consultar los movimientos de deudores
	 */
	private static String strConSelMovimientosDeudores = "SELECT " +
															"fv.deudor AS deudor, " +
															/*
															 * "getdeudor(fv.deudor) AS desdeudor, " +
															 * Según tarea 2129
															 */
															"CASE " +
																"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoPaciente+"' THEN getnombrepersona(d.codigo_paciente) " +
																"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa+"' THEN getnombreempresa(d.codigo_empresa) " +
																"WHEN d.tipo ='"+ConstantesIntegridadDominio.acronimoTipoDeudorOtros+"' THEN getdescripciontercero(d.codigo_tercero) " +
																"WHEN d.tipo= '"+ConstantesIntegridadDominio.acronimoOtro+"' THEN  d.primer_nombre ||'  '|| d.primer_apellido " +
															"END AS desdeudor," +
															"CASE " +
																"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoPaciente+"' THEN getidentificacionpaciente(d.codigo_paciente)  " +
																"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa+"' THEN getnitempresa(d.codigo_empresa)  " +
																"WHEN d.tipo='"+ConstantesIntegridadDominio.acronimoTipoDeudorOtros+"' THEN getnittercero(d.codigo_tercero)  " +
																"WHEN d.tipo= '"+ConstantesIntegridadDominio.acronimoOtro+"' THEN  d.numero_identificacion " +
															"END AS idtercero, " +
															"coalesce(sum(fv.valor_factura), 0) AS valorinicial, " +
															"coalesce(sum(fv.ajustes_debito), 0) AS ajustesdebito, " +
															"coalesce(sum(fv.ajustes_credito), 0) AS ajustescredito, " +
															"coalesce(sum(fv.valor_pagos), 0) AS pagosaplicados, " +
															"(" +
																"coalesce(sum(fv.valor_factura), 0) " +
																"+ coalesce(sum(fv.ajustes_debito), 0) " +
																"- coalesce(sum(fv.ajustes_credito), 0) " +
																"- coalesce(sum(fv.valor_pagos), 0)" +
															") AS saldo, " +
															"d.tipo AS tipodeudor " +
														"FROM " +
															"facturas_varias fv " +
															"INNER JOIN deudores d ON (d.codigo = fv.deudor) " +
														"WHERE 1=1 ";
															//Según Tarea 38281
															//"fv.estado_factura = '"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' ";

	
	/**
	 * Cadena GROUP BY y ORDER BY para consultar los movimientos de deudores
	 */
	private static String strConGroupMovimientosDeudores = "GROUP BY " +
																"fv.deudor, " +
																"d.tipo, " +
																"d.primer_apellido, " +
																"d.primer_nombre," +
																"d.numero_identificacion," +
																"d.codigo_paciente, " +
																"d.codigo_empresa, " +
																"d.codigo_tercero " +
															"ORDER BY " +
																"fv.deudor ";
	
	/**
	 * Cadena SELECT para consultar el detalle de los movimientos por deudor 
	 */
	private static String strConSelDetalleMovimientosDeudores = "SELECT " +
																	"fv.consecutivo AS consecutivofactura, " +
																	"to_char(fv.fecha, 'DD/MM/YYYY') AS fechaelaboracion, " +
																	"coalesce(fv.valor_factura, 0) AS valorinicial, " +
																	"coalesce(fv.ajustes_debito, 0) AS ajustesdebito, " +
																	"coalesce(fv.ajustes_credito, 0) AS ajustescredito, " +
																	"coalesce(fv.valor_pagos, 0) AS pagosaplicados, " +
																	"(" +
																		"coalesce(fv.valor_factura, 0) " +
																		"+ coalesce(fv.ajustes_debito, 0) " +
																		"- coalesce(fv.ajustes_credito, 0) " +
																		"- coalesce(fv.valor_pagos, 0)" +
																	") AS saldo " +
																"FROM " +
																	"facturas_varias fv " +
																"WHERE " +
																	"fv.estado_factura = '"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' " +
																	"AND fv.deudor = ? " +
																	"AND fv.fecha BETWEEN ? AND ? " +
																"ORDER BY "+
																	"fv.consecutivo ";
	
	/**
	 * Método que consulta los movimientos
	 * de deudores según el tipo de deudor seleccionado
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static HashMap consultarMovimientosDeudores(Connection con, HashMap criterios)
	{
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        
        //**********************INICIO ARMANDO LA CONSULTA******************************
        String consulta = strConSelMovimientosDeudores;
        
        if(UtilidadFecha.validarFecha(criterios.get("fechaInicial")+""))
        {	
	        //Filtramos la consulta por el rango de fechas digitado. Como es requerido no es necesario validarlo
	        consulta += " and to_char(fv.fecha,'yyyy-mm-dd') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"' ";
        }    
        //Filtramos la consulta por el tipo de dedudor seleccionado. Como no es requerido es necesario validarlo
        if(UtilidadCadena.noEsVacio(criterios.get("tipoDeudor")+""))
        	consulta += " AND d.tipo = '"+criterios.get("tipoDeudor")+"' ";
        
        //Filtramos la consulta por el deduor seleccionado. Como no es requerido es necesario validarlo
        
        logger.info("CRITERIOS ***********************"+criterios.get("deudor"));
        
        if ((criterios.get("tipoDeudor")+"").equals(ConstantesIntegridadDominio.acronimoPaciente))
        {
        	if(UtilidadCadena.noEsVacio(criterios.get("deudor")+""))
            	consulta += " AND d.codigo_paciente = "+criterios.get("deudor")+" ";
        }else if ((criterios.get("tipoDeudor")+"").equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros) || (criterios.get("tipoDeudor")+"").equals(ConstantesIntegridadDominio.acronimoOtro) )
        {
        	if(UtilidadCadena.noEsVacio(criterios.get("deudor")+""))
        		consulta+= " and fv.deudor = "+criterios.get("deudor")+" ";
        	//XPLANNER 135904------------------
            	//consulta += " AND d.codigo_tercero = "+criterios.get("deudor")+" ";
        }else if ((criterios.get("tipoDeudor")+"").equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa))
        {
        	if(UtilidadCadena.noEsVacio(criterios.get("deudor")+""))
            	consulta += " AND d.codigo_empresa = "+criterios.get("deudor")+" ";
        }
        
        
        //Adicionamos el Group By y el Order By a la consulta
        consulta += strConGroupMovimientosDeudores;
        //**********************FIN ARMANDO LA CONSULTA*********************************
        
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	logger.info("====>Consulta Movimientos Deudores: "+consulta);
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DE MOVIMIENTOS DEUDORES");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}
	
	/**
	 * Método que consulta las condiciones establecidas 
	 * para la consulta de movimientos de deudor para
	 * generar el reporte
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static String consultarCondicionesMovimientosDeudor(Connection con, HashMap criterios)
	{
		String condiciones = " 1=1 ";
		
		//**********************INICIO ARMANDO LAS CONDICIONES******************************
        //Filtramos la consulta por el estado de la factura. El cual debe ser Aprobado
		//condiciones = "fv.estado_factura = '"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' ";
		//Filtramos la consulta por el rango de fechas digitado. Como es requerido no es necesario validarlo
		if(UtilidadFecha.validarFecha(criterios.get("fechaInicial")+""))
			condiciones += " and to_char(fv.fecha, 'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"")+"' ";
        //Filtramos la consulta por el tipo de dedudor seleccionado. Como no es requerido es necesario validarlo
        if(UtilidadCadena.noEsVacio(criterios.get("tipoDeudor")+""))
        	condiciones += " and d.tipo = '"+criterios.get("tipoDeudor")+"' ";
        //Filtramos la consulta por el deduor seleccionado. Como no es requerido es necesario validarlo
        if(UtilidadCadena.noEsVacio(criterios.get("deudor")+""))
        //MT 6351 Se filtra valores desde la tabla 'deudores' en lugar de tabla 'facturas_varias'	
        //condiciones += " AND fv.deudor = "+criterios.get("deudor")+" ";
       	condiciones += " AND d.codigo_paciente = "+criterios.get("deudor")+" ";
        //**********************FIN ARMANDO LAS CONDICIONES*********************************
		
		return condiciones;
	}
	
	/**
	 * Método que consulta el detallle de los 
	 * movimientos de un deudor seleccionado
	 * @param con
	 * @param deudor
	 * @return
	 */
	public static HashMap consultarDetalleMovimientosDeudor(Connection con, int deudor, String fechaInicial, String fechaFinal)
	{
		HashMap mapa = new HashMap<String, Object>();
        mapa.put("numRegistros","0");
        try
        {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConSelDetalleMovimientosDeudores,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        	ps.setInt(1, deudor);
        	//segun xplanner 108688 esto ya no es requerido
        	ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicial)));
        	ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));
        	logger.info("====>Consulta Detalle Movimientos Deudor: "+strConSelDetalleMovimientosDeudores+" ====>Deudor: "+deudor+" ====>Fecha Inicial: "+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+" ====>Fecha Final: "+UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
        }
        catch (SQLException e)
        {
            logger.error("ERROR EJECUNTANDO LA CONSULTA DETALLE DE MOVIMIENTOS DEUDORES");
            e.printStackTrace();
        }
        return (HashMap)mapa.clone();
	}
	
	
}