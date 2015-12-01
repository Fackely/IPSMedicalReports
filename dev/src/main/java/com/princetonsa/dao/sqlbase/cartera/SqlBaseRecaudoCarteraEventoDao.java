package com.princetonsa.dao.sqlbase.cartera;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;


import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.cartera.RecaudoCarteraEvento;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;




public class SqlBaseRecaudoCarteraEventoDao
{
	

	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
		public static Logger logger = Logger.getLogger(SqlBaseRecaudoCarteraEventoDao.class);
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
		
	/*--------------------------------------------------------------------------
	 * ATRIBUTOS CON LAS CADENAS DE CONSULTA DE REACUDO CARTERA EVENTO
	 ---------------------------------------------------------------------------*/	
		/**
		 * Cadena utilizada para la consulta del reporte
		 * resumido por tipo de convenio
		 */
	private static String strConsultaReporteResumidoXTipoConvenio = " SELECT distinct " +
																		" getdescripciontipoconvenio (pge.convenio) As tipo_convenio0, " +
																		" getacronimotipodocumento(pge.tipo_doc) As tipo_doc1, " +
																		" documento As documento2, " + 
																		" to_char(pge.fecha_documento,'DD/MM/YYYY') As fecha_recaudo3, " +
																		" gettotalpagosaproba(pge.codigo) As valor_recaudo4 " +
																 " FROM pagos_general_empresa pge " +
																 " INNER JOIN cartera.aplicacion_pagos_empresa ape on (ape.pagos_general_empresa=pge.codigo)" +
																 " where ape.estado="+ConstantesBD.codigoEstadoAplicacionPagosAprobado;
	/**
	 * Cadena utilizada para la consulta del reporte resumido 
	 * por convenio
	 */	
	private static String strConsultaReporteResumidoXConvenio = " SELECT  distinct " +
																		" getdescripciontipoconvenio (pge.convenio) As tipo_convenio0, " +
																		" getacronimotipodocumento(pge.tipo_doc) As tipo_doc1, " +
																		" documento As documento2, " +
																		" to_char(pge.fecha_documento,'DD/MM/YYYY') As fecha_recaudo3, " +
																		" gettotalpagosaproba(pge.codigo) As valor_recaudo4, " +
																		" getnombreconvenio(pge.convenio) As convenio5 " +
																" FROM pagos_general_empresa pge " +
																" INNER JOIN cartera.aplicacion_pagos_empresa ape on (ape.pagos_general_empresa=pge.codigo)" +
																" where ape.estado="+ConstantesBD.codigoEstadoAplicacionPagosAprobado;	
	
	
	private static String strConsultaReporteDetalladoXFactura=" SELECT  " +
																		" getdescripciontipoconvenio (f.convenio) As tipo_convenio0," +
																		" getnombreconvenio(f.convenio) As convenio5," +
																		" f.consecutivo_factura As factura6," +
																		" to_char(f.fecha,'DD/MM/YYYY') As fecha_factura7," +
																		" getacronimotipodocumento(pge.tipo_doc) As tipo_doc1," +
																		" documento As documento2," +
																		" to_char(pge.fecha_documento,'DD/MM/YYYY') As fecha_recaudo3," +
																		" apf.valor_pago  As valor_recaudo4 " +
															  " FROM pagos_general_empresa pge " +
															  " INNER JOIN cartera.aplicacion_pagos_empresa ape on (ape.pagos_general_empresa=pge.codigo)" +
															  " INNER JOIN  cartera.aplicacion_pagos_cxc apc on(ape.codigo=apc.aplica_pagos_empresa) " +
															  " INNER JOIN  cartera.aplicacion_pagos_factura apf on(apf.aplicacion_pagos=apc.aplica_pagos_empresa and apf.numero_cuenta_cobro=apc.numero_cuenta_cobro)" +
															  " INNER JOIN facturas f on(f.codigo=apf.factura)" +
															  " WHERE ape.estado="+ConstantesBD.codigoEstadoAplicacionPagosAprobado;

	/**
	 * cadena utilizada para ingresar los datos
	 * al log cuando se genera un reporte.
	 */
	private static String strInsertarLog = "INSERT INTO logs_reportes " +
												"(reporte," +
												"tipo_reporte," +
												"tipo_salida," +
												"criterios," +
												"nombre_reporte," +
												"institucion," +
												"usuario)" +
											" values (?,?,?,?,?,?,?)" ;
									
	

	
	
	private static String [] indicesCriterios = RecaudoCarteraEvento.indicesCriterios;
	
	
	/*-----------------------------------------------------------------------------------------------------------------------------
	 * 														FIN ATRIBUTOS
	 ----------------------------------------------------------------------------------------------------------------------------*/
	
	
	/**
	 * Metodo encargado de insertar un log cuango
	 * se genera un reporte
	 * -----------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ------------------------------------------
	 * -- reporte7 --> Requerido
	 * -- tipoReporte2 --> Requerido
	 * -- tipoSalida5 --> Requerido
	 * -- fechaIni0 --> Requerido
	 * -- fechaFin1 --> Requerido
	 * -- tipoConvenio3 --> Requerido
	 * -- convenio4 --> Requerido
	 * -- nombreReporte8 --> Requerido
	 * -- institucion6 --> Requerido
	 * -- usuario9 --> Requerido
	 */
	public static boolean insertarLog (Connection connection,HashMap criterios)
	{
		logger.info("\n entro a insertarLog --> "+criterios);
		
		String cadena = strInsertarLog;
		String criteConsul=criterios.get(indicesCriterios[0])+","+criterios.get(indicesCriterios[1]);
		//Tipo Convenio
		if (!(criterios.get(indicesCriterios[3])+"").equals("") && !(criterios.get(indicesCriterios[3])+"").equals("null"))
			criteConsul+=","+criterios.get(indicesCriterios[3]);
		
		//convenio
		if (!(criterios.get(indicesCriterios[4])+"").equals("") && !(criterios.get(indicesCriterios[4])+"").equals("null"))
			criteConsul+=","+criterios.get(indicesCriterios[4]);
		try
		{
		
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//reporte
			
			/**
			 * "INSERT INTO logs_reportes " +
												"(reporte," +
												"tipo_reporte," +
												"tipo_salida," +
												"criterios," +
												"nombre_reporte," +
												"institucion," +
												"usuario)" +
											" values (?,?,?,?,?,?,?)" ;
			 */
			
			ps.setString(1, criterios.get(indicesCriterios[7])+"");
			//tipo Reporte
			ps.setString(2, criterios.get(indicesCriterios[2])+"");
			//tipo Salida
			ps.setString(3, criterios.get(indicesCriterios[5])+"");
			//Criterios
			ps.setString(4, criteConsul);
			//nombre reporte
			if (!(criterios.get(indicesCriterios[8])+"").equals("") && !(criterios.get(indicesCriterios[8])+"").equals("null"))
				ps.setString(5, criterios.get(indicesCriterios[8])+"");
			else
				ps.setNull(5, Types.VARCHAR);
			//institucion
			ps.setInt(6, Utilidades.convertirAEntero(criterios.get(indicesCriterios[6])+""));
			//usuario
			ps.setString(7, criterios.get(indicesCriterios[9])+"");

			if (ps.executeUpdate()>0)
				return true;


			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema insertando fatos al log de reportes "+e);
		}
		
		
		
		return false;
	}
	
	
	
	/**
	 * Metodo encargado de consultar el reporte 
	 * Resumido por tipo de convenio.
	 * @param criterios
	 * -----------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * -----------------------------------
	 * -- fechaIni0 --> Requerido
	 * -- fechaFin1 --> Requerido
	 * -- tipoReporte2 --> Requerido
	 * -- tipoConvenio3 --> Opcional
	 * -- convenio4 --> Opcional
	 * -- tipoSalida5 --> Requerido
	 * -- institucion6 --> Requerido
	 * ----------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ----------------------------------
	 * tipoConvenio0_, tipoDoc1_,documento2_,
	 * fechaRecaudo3_,valorRecaudo4_
	 */
	public static HashMap consultaReporteResumidoXTipoConvenio (Connection connection, HashMap criterios)
	{
		logger.info("\n ENTRE A consultaReporteXTipoConvenio CRITERIOS --> "+criterios);
		
		String cadena = strConsultaReporteResumidoXTipoConvenio;
		HashMap result = new HashMap ();
		result.put("numRegistros", 0);
		//--------------------------FILTROS-------------------------------
		
		//institucion
		if (!(criterios.get(indicesCriterios[6])+"").equals("") && !(criterios.get(indicesCriterios[6])+"").equals("null"))
			cadena +=" AND pge.institucion="+criterios.get(indicesCriterios[6]);
						
		// Rango Entre Fechas (Fecha Inicial  - Fecha Final )
		if (!(criterios.get(indicesCriterios[0])+"").equals("") && !(criterios.get(indicesCriterios[0])+"").equals("null")
			&& !(criterios.get(indicesCriterios[1])+"").equals("") && !(criterios.get(indicesCriterios[1])+"").equals("null"))	
			cadena+=" AND pge.fecha_documento between '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[0])+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[1])+"")+"'";
		
		// Tipo Convenio
		if (!(criterios.get(indicesCriterios[3])+"").equals("") && !(criterios.get(indicesCriterios[3])+"").equals("null"))
			cadena += " AND gettipoconvenio(pge.convenio)='"+criterios.get(indicesCriterios[3])+"'";
		
		//convenio
		if (!(criterios.get(indicesCriterios[4])+"").equals("") && !(criterios.get(indicesCriterios[4])+"").equals("null"))
			cadena+=" AND pge.convenio="+criterios.get(indicesCriterios[4]);
			
		
		cadena+=" order by tipo_doc1,  documento2";
			logger.info("\n cadena --> "+cadena);
		//----------------------------------------------------------------
			
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			result=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
		}
		catch (Exception e)
		{
			logger.info("\n poblema consultando el reporte resumido por tipo de convenio");
		}
		
		return result;
	}
	
	
	
	/**
	 * Metodo encargado de consultar el reporte 
	 * Resumido por convenio.
	 * @param criterios
	 * -----------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * -----------------------------------
	 * -- fechaIni0 --> Requerido
	 * -- fechaFin1 --> Requerido
	 * -- tipoReporte2 --> Requerido
	 * -- tipoConvenio3 --> Opcional
	 * -- convenio4 --> Opcional
	 * -- tipoSalida5 --> Requerido
	 * -- institucion6 --> Requerido
	 * ----------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ----------------------------------
	 * tipoConvenio0_, tipoDoc1_,documento2_,
	 * fechaRecaudo3_,valorRecaudo4_,convenio5_
	 */
	public static HashMap consultarReporteResumidoXConvenio (Connection connection, HashMap criterios)
	{
		logger.info("\n ENTRE A consultaReporteXTipoConvenio CRITERIOS --> "+criterios);
		
		String cadena = strConsultaReporteResumidoXConvenio;
		HashMap result = new HashMap ();
		result.put("numRegistros", 0);
		//--------------------------FILTROS-------------------------------
		
		//institucion
		if (!(criterios.get(indicesCriterios[6])+"").equals("") && !(criterios.get(indicesCriterios[6])+"").equals("null"))
			cadena +=" And pge.institucion="+criterios.get(indicesCriterios[6]);
						
		// Rango Entre Fechas (Fecha Inicial  - Fecha Final )
		if (!(criterios.get(indicesCriterios[0])+"").equals("") && !(criterios.get(indicesCriterios[0])+"").equals("null")
			&& !(criterios.get(indicesCriterios[1])+"").equals("") && !(criterios.get(indicesCriterios[1])+"").equals("null"))	
			cadena+=" AND pge.fecha_documento between '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[0])+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[1])+"")+"'";
		
		// Tipo Convenio
		if (!(criterios.get(indicesCriterios[3])+"").equals("") && !(criterios.get(indicesCriterios[3])+"").equals("null"))
			cadena += " AND gettipoconvenio(pge.convenio)='"+criterios.get(indicesCriterios[3])+"'";
		
		//convenio
		if (!(criterios.get(indicesCriterios[4])+"").equals("") && !(criterios.get(indicesCriterios[4])+"").equals("null"))
			cadena+=" AND pge.convenio="+criterios.get(indicesCriterios[4]);
			
		
		cadena+=" order by tipo_doc1,  documento2";
			logger.info("\n cadena --> "+cadena);
		//----------------------------------------------------------------
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			result=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
		}
		catch (Exception e)
		{
			logger.info("\n poblema consultando el reporte resumido por convenio");
		}
		
		return result;
	}
	
	/**
	 * Metodo encargado de de consultar los datos del reporte
	 * detallado por factura
	 * @param criterios
	 * @param connection
	 * -----------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * -----------------------------------
	 * -- fechaIni0 --> Requerido
	 * -- fechaFin1 --> Requerido
	 * -- tipoReporte2 --> Requerido
	 * -- tipoConvenio3 --> Opcional
	 * -- convenio4 --> Opcional
	 * -- tipoSalida5 --> Requerido
	 * -- institucion6 --> Requerido
	 * ----------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ----------------------------------
	 * tipoConvenio0_, tipoDoc1_,documento2_,
	 * fechaRecaudo3_,valorRecaudo4_,convenio5_
	 * factura6_,fechaFactura7_
	 * @return
	 */
	public static HashMap consultarReporteDetalladoXFactura (Connection connection, HashMap criterios)
	{
		logger.info("\n ENTRE A consultarReporteDetalladoXFactura CRITERIOS --> "+criterios);
		
		String cadena = strConsultaReporteDetalladoXFactura;
		HashMap result = new HashMap ();
		result.put("numRegistros", 0);
		//--------------------------FILTROS-------------------------------
		
		//institucion
		if (!(criterios.get(indicesCriterios[6])+"").equals("") && !(criterios.get(indicesCriterios[6])+"").equals("null"))
			cadena +=" AND pge.institucion="+criterios.get(indicesCriterios[6]);
						
		// Rango Entre Fechas (Fecha Inicial  - Fecha Final )
		if (!(criterios.get(indicesCriterios[0])+"").equals("") && !(criterios.get(indicesCriterios[0])+"").equals("null")
			&& !(criterios.get(indicesCriterios[1])+"").equals("") && !(criterios.get(indicesCriterios[1])+"").equals("null"))	
			cadena+=" AND pge.fecha_documento between '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[0])+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get(indicesCriterios[1])+"")+"'";
		
		// Tipo Convenio
		if (!(criterios.get(indicesCriterios[3])+"").equals("") && !(criterios.get(indicesCriterios[3])+"").equals("null"))
			cadena += " AND gettipoconvenio(pge.convenio)='"+criterios.get(indicesCriterios[3])+"'";
		
		//convenio
		if (!(criterios.get(indicesCriterios[4])+"").equals("") && !(criterios.get(indicesCriterios[4])+"").equals("null"))
			cadena+=" AND pge.convenio="+criterios.get(indicesCriterios[4]);
			
		
		cadena+=" order by tipo_doc1,  documento2";
			logger.info("\n cadena --> "+cadena);
		//----------------------------------------------------------------
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			result=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
		}
		catch (Exception e)
		{
			logger.info("\n poblema consultando el reporte detallado por Factura");
		}
		
		return result;
	}
	
}