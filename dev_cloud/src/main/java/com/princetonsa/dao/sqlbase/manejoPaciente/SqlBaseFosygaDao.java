/*
 * @(#)SqlBaseFosygaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
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
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * Consultas estandar
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public class SqlBaseFosygaDao 
{
	
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger( SqlBaseFosygaDao.class);
	
	/**
	 * Cadena SELECT GENERAL para consultar los tipos de eventos. Se modifico según la Tarea 53432
	 */
	public static String strConsultaFosyga = "SELECT "+
												"t.tipoevento AS tipoevento, " +
												"t.consecutivo AS consecutivo, " +
												"t.cuenta AS cuenta, " +
												"t.ingreso AS ingreso, " + 
												"t.paciente AS paciente, " +
												"t.fechahoraingresoformatobd AS fechahoraingresoformatobd, " +
												"t.fechahoraingreso AS fechahoraingreso, " +
												"t.fechahoraegreso AS fechahoraegreso, " +
												"t.centroatencion AS centroatencion, " +
												"t.codigovia AS codigovia, " +
												"t.via AS via, " +
												"t.estado AS estado " +
											"FROM ";
	
	/**
	 * Cadena SELECT Primera Parte del UNION para consultar los tipos de eventos. Se modifico según la Tarea 53432
	 */
	public static String strConsultaFosygaPrimeraParte = "SELECT " +
															"'ACTE' AS tipoevento, " + 
															"r.codigo AS consecutivo, " + 
															"cue.id AS cuenta, " + 
															"cue.id_ingreso AS ingreso, " +
															"getnombrepersona(cue.codigo_paciente) AS paciente, " +
															"CASE WHEN vi.codigo = 1 THEN adh.fecha_admision ||' '|| adh.hora_admision WHEN vi.codigo = 3 THEN adu.fecha_admision ||' '|| adu.hora_admision ELSE cue.fecha_apertura ||' '|| cue.hora_apertura END AS fechahoraingresoformatobd, " + 
															"CASE WHEN vi.codigo = 1 THEN TO_CHAR(adh.fecha_admision, 'DD/MM/YYYY') ||' '|| SUBSTR(adh.hora_admision, 1,5) WHEN vi.codigo = 3 THEN TO_CHAR(adu.fecha_admision, 'DD/MM/YYYY') ||' '|| SUBSTR(adu.hora_admision,1,5) ELSE TO_CHAR(cue.fecha_apertura, 'DD/MM/YYYY') ||' '|| SUBSTR(cue.hora_apertura,1,5) END AS fechahoraingreso, " + 
															"CASE WHEN vi.codigo IN (1,3) THEN TO_CHAR(eg.fecha_egreso, 'DD/MM/YYYY') ||' '|| SUBSTR(eg.hora_egreso,1,5) ELSE TO_CHAR(cue.fecha_apertura, 'DD/MM/YYYY') ||' '|| SUBSTR(cue.hora_apertura,1,5) END AS fechahoraegreso, " +
															"getnomcentroatencion(ccos.centro_atencion) AS centroatencion, " + 
															"vi.codigo AS codigovia, " + 
															"CASE WHEN vi.codigo = 1 THEN 'HOS' ELSE CASE WHEN vi.codigo = 2 THEN 'AMB' ELSE CASE WHEN vi.codigo = 3 THEN 'URG' ELSE 'CE' END END END AS via, " +
															"CASE WHEN r.estado = 'PEN' THEN 'PENDIENTE' ELSE CASE WHEN r.estado = 'PRO' THEN 'FINALIZADO' ELSE CASE WHEN r.estado = 'ANU' THEN 'ANULADO' ELSE '' END END END AS estado " +
														"FROM " + 
															"cuentas cue " + 
															"INNER JOIN vias_ingreso vi ON (cue.via_ingreso = vi.codigo) " + 
															"LEFT OUTER JOIN view_registro_accid_transito r ON (r.ingreso = cue.id_ingreso) " + 
															"LEFT OUTER JOIN admisiones_hospi adh ON (cue.id = adh.cuenta) " + 
															"LEFT OUTER JOIN admisiones_urgencias adu ON (cue.id = adu.cuenta) " + 
															"LEFT OUTER JOIN egresos eg ON (cue.id = eg.cuenta) " + 
															"INNER JOIN centros_costo ccos ON (cue.area=ccos.codigo) ";
	
	/**
	 * Cadena SELECT Segunda Parte del UNION para consultar los tipos de eventos. Se modifico según la Tarea 53432
	 */
	public static String strConsultaFosygaSegundaParte = "SELECT " +
															"'ECTE' AS tipoevento, " + 
															"re.codigo AS consecutivo, " + 
															"cue.id AS cuenta, " + 
															"cue.id_ingreso AS ingreso, " +
															"getnombrepersona(cue.codigo_paciente) AS paciente, " +
															"CASE WHEN vi.codigo = 1 THEN adh.fecha_admision ||' '|| adh.hora_admision WHEN vi.codigo = 3 THEN adu.fecha_admision ||' '|| adu.hora_admision ELSE cue.fecha_apertura ||' '|| cue.hora_apertura END AS fechahoraingresoformatobd, " + 
															"CASE WHEN vi.codigo = 1 THEN TO_CHAR(adh.fecha_admision, 'DD/MM/YYYY') ||' '|| SUBSTR(adh.hora_admision, 1,5) WHEN vi.codigo = 3 THEN TO_CHAR(adu.fecha_admision, 'DD/MM/YYYY') ||' '|| SUBSTR(adu.hora_admision,1,5) ELSE TO_CHAR(cue.fecha_apertura, 'DD/MM/YYYY') ||' '|| SUBSTR(cue.hora_apertura,1,5) END AS fechahoraingreso, " + 
															"CASE WHEN vi.codigo in (1,3) THEN TO_CHAR(eg.fecha_egreso, 'DD/MM/YYYY') ||' '|| SUBSTR(eg.hora_egreso,1,5) ELSE TO_CHAR(cue.fecha_apertura, 'DD/MM/YYYY') ||' '|| SUBSTR(cue.hora_apertura,1,5) END AS fechahoraegreso, " +
															"getnomcentroatencion(ccos.centro_atencion) AS centroatencion, " + 
															"vi.codigo AS codigovia, " + 
															"CASE WHEN vi.codigo = 1 THEN 'HOS' ELSE CASE WHEN vi.codigo = 2 THEN 'AMB' ELSE CASE WHEN vi.codigo = 3 THEN 'URG' ELSE 'CE' END END END AS via, " +
															"CASE WHEN re.estado = 'PEN' THEN 'PENDIENTE' ELSE CASE WHEN re.estado = 'FIN' THEN 'FINALIZADO' ELSE CASE WHEN re.estado = 'ANU' THEN 'ANULADO' ELSE '' END END END AS estado " +
														"FROM " + 
															"cuentas cue " + 
															"INNER JOIN vias_ingreso vi ON (cue.via_ingreso = vi.codigo) " + 
															"LEFT OUTER JOIN view_reg_evento_catastrofico re ON (re.ingreso = cue.id_ingreso) " + 
															"LEFT OUTER JOIN admisiones_hospi adh ON (cue.id = adh.cuenta) " + 
															"LEFT OUTER JOIN admisiones_urgencias adu ON (cue.id = adu.cuenta) " + 
															"LEFT OUTER JOIN egresos eg ON (cue.id = eg.cuenta) " + 
															"INNER JOIN centros_costo ccos ON (cue.area = ccos.codigo) ";
	
	/**
	 * 
	 */
	public static String cadenaConsultaFOSYGA=			"SELECT " +
															"CASE WHEN r.codigo IS NOT NULL then '"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' ELSE '"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' end as tipoevento, " +
															"CASE WHEN r.codigo IS NOT NULL then r.codigo ELSE re.codigo END AS consecutivo, " +
															"cue.id as cuenta, " +
															"cue.id_ingreso as ingreso, " +
															"getnombrepersona(cue.codigo_paciente) AS paciente, " +
															"CASE WHEN " +
																"vi.codigo="+ConstantesBD.codigoViaIngresoHospitalizacion+" THEN adh.fecha_admision ||' '|| adh.hora_admision " +
															"WHEN " +
																"vi.codigo="+ConstantesBD.codigoViaIngresoUrgencias+" THEN adu.fecha_admision ||' '|| adu.hora_admision " +
															"ELSE " +
																"cue.fecha_apertura ||' '|| cue.hora_apertura " +
															"END AS fechahoraingresoformatobd, " +
															"CASE WHEN " +
																"vi.codigo="+ConstantesBD.codigoViaIngresoHospitalizacion+" THEN TO_CHAR(adh.fecha_admision, 'DD/MM/YYYY') ||' '|| SUBSTR(adh.hora_admision, 1,5) " +
															"WHEN " +
																"vi.codigo="+ConstantesBD.codigoViaIngresoUrgencias+" THEN TO_CHAR(adu.fecha_admision, 'DD/MM/YYYY') ||' '|| SUBSTR(adu.hora_admision,1,5) " +
															"ELSE " +
																"TO_CHAR(cue.fecha_apertura, 'DD/MM/YYYY') ||' '|| SUBSTR(cue.hora_apertura,1,5) " +
															"END AS fechahoraingreso, " +
															"CASE WHEN " +
																"vi.codigo in ("+ConstantesBD.codigoViaIngresoHospitalizacion+","+ConstantesBD.codigoViaIngresoUrgencias+") THEN TO_CHAR(eg.fecha_egreso, 'DD/MM/YYYY') ||' '|| SUBSTR(eg.hora_egreso,1,5) " +
															"ELSE " +
																"TO_CHAR(cue.fecha_apertura, 'DD/MM/YYYY') ||' '|| SUBSTR(cue.hora_apertura,1,5) " +
															"END AS fechahoraegreso, " +
															"getnomcentroatencion(ccos.centro_atencion) AS centroatencion, " +
															"vi.codigo as codigovia, " +
															"CASE WHEN vi.codigo="+ConstantesBD.codigoViaIngresoHospitalizacion+" THEN 'HOS' " +
															"ELSE CASE WHEN vi.codigo="+ConstantesBD.codigoViaIngresoAmbulatorios+" THEN 'AMB' " +
															"ELSE CASE WHEN vi.codigo="+ConstantesBD.codigoViaIngresoUrgencias+" THEN 'URG' " +
															"ELSE 'CE' " +
															"END " +
															"END " +
															"END as via " +
														"FROM " +
															"cuentas cue " +
															"INNER JOIN vias_ingreso vi ON(cue.via_ingreso=vi.codigo) " +
															"LEFT OUTER JOIN view_registro_accid_transito r ON (r.ingreso=cue.id_ingreso) " +
															"LEFT OUTER JOIN view_reg_evento_catastrofico re ON (re.ingreso=cue.id_ingreso) " +
															"LEFT OUTER JOIN admisiones_hospi adh ON (cue.id=adh.cuenta) " +
															"LEFT OUTER JOIN admisiones_urgencias adu ON (cue.id=adu.cuenta) " +
															"LEFT OUTER JOIN egresos eg ON (cue.id=eg.cuenta) " +
															"INNER JOIN centros_costo ccos ON (cue.area=ccos.codigo) ";
														
	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 */
	public static HashMap busquedaAvanzada(Connection con, HashMap criteriosBusquedaMap) 
	{
		try
		{
			
			String consultaArmada="";
			logger.info("tipoEvento-->"+criteriosBusquedaMap.get("tipoEvento"));
			
			//verificamos que contenga el key codigoPersona para saber si se realiza x paciente o por periodo
			if(criteriosBusquedaMap.containsKey("codigoPersona"))
			{
				consultaArmada+= armarConsultaFosygaXPaciente (criteriosBusquedaMap);
			}
			else
			{	
				consultaArmada+= armarConsultaFosygaXPeriodo(criteriosBusquedaMap);
			}	
			logger.info("CONSULTA TOTAL BUSQUEDA AVANZADA FOSYGA--->"+consultaArmada);
			
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
	        rs.close();
	        ps.close();
	        return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn("Error en la búsqueda avanzada" +e.toString());
			return null;
		}
	}
	
	/**
	 * Método que arma la consulta para obtener los tipos
	 * de eventos según el criterio de búsqueda por paciente
	 * @param criteriosBusquedaMap
	 * @return
	 */
	private static String armarConsultaFosygaXPaciente(HashMap criteriosBusquedaMap) 
	{
		//Se modifico la consulta según la Tarea 53432
		/*String consultaArmada = cadenaConsultaFOSYGA;
		consultaArmada+=" WHERE ";
		consultaArmada+=" (r.codigo IS NOT NULL OR re.codigo IS NOT NULL) ";
		consultaArmada+=" AND cue.codigo_paciente = "+criteriosBusquedaMap.get("codigoPersona").toString();
		consultaArmada+= 	" and cue.tipo_evento in('"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"', '"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"') " +
							" AND (r.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"' " +
							" or re.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') ";
		consultaArmada+=" ORDER BY via, paciente ";
		return consultaArmada;*/
		String consultaArmada = strConsultaFosyga+"(" +
													"(" +
														strConsultaFosygaPrimeraParte+"WHERE r.codigo IS NOT NULL " +
																					  		 "AND cue.codigo_paciente = "+criteriosBusquedaMap.get("codigoPersona")+""+"" +
												    ") " +
												    "UNION " +
												    "(" +
												    	strConsultaFosygaSegundaParte+"WHERE re.codigo IS NOT NULL " +
												    								  		 "AND cue.codigo_paciente = "+criteriosBusquedaMap.get("codigoPersona")+""+"" +
												    ")" +	
												   ") t ORDER BY t.via, t.paciente ";
		return consultaArmada;
	}

	/**
	 * 
	 * @param criteriosBusquedaMap
	 * @return
	 */
	private static String armarConsultaFosygaXPeriodo(HashMap criteriosBusquedaMap) 
	{
		//Se modifico la consulta según la Tarea 53432
		/*String consultaArmada= cadenaConsultaFOSYGA;
		consultaArmada+=" WHERE ";
		consultaArmada+=" (r.codigo IS NOT NULL OR re.codigo IS NOT NULL) ";
		
		if(criteriosBusquedaMap.get("tipoEvento").toString().equals(""))
		{
			consultaArmada+= " and cue.tipo_evento in('"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"', '"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"') " +
							"AND (r.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"' " +
							"or re.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') ";
		}
		else if(criteriosBusquedaMap.get("tipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoAccidenteTransito))
		{
			consultaArmada+= " and cue.tipo_evento ='"+ConstantesIntegridadDominio.acronimoAccidenteTransito+"' " +
							" AND r.estado='"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"' ";
		}
		else if(criteriosBusquedaMap.get("tipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoEventoCatastrofico))
		{
			consultaArmada+= " and cue.tipo_evento ='"+ConstantesIntegridadDominio.acronimoEventoCatastrofico+"' " +
							" AND re.estado='"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"' ";;
		}
		
		if(criteriosBusquedaMap.containsKey("fechaInicial") && criteriosBusquedaMap.containsKey("fechaFinal"))
		{
			if( !UtilidadTexto.isEmpty(criteriosBusquedaMap.get("fechaInicial").toString()) 
				&& !UtilidadTexto.isEmpty(criteriosBusquedaMap.get("fechaFinal").toString()))
			{
				if(criteriosBusquedaMap.get("tipoEvento").toString().equals(""))
				{
					consultaArmada+=" AND ((r.fecha_grabacion >= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicial").toString())+"'" +
									" AND r.fecha_grabacion <= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinal").toString())+"') "+
									" or (re.fecha_modifica >= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicial").toString())+"'" +
									" AND re.fecha_modifica <= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinal").toString())+"') )";
				}
				else if(criteriosBusquedaMap.get("tipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoAccidenteTransito))
				{
					consultaArmada+=" AND (r.fecha_grabacion >= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicial").toString())+"'" +
									" AND r.fecha_grabacion <= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinal").toString())+"') ";
				}
				else if(criteriosBusquedaMap.get("tipoEvento").toString().equals(ConstantesIntegridadDominio.acronimoEventoCatastrofico))
				{
					consultaArmada+=" AND (re.fecha_modifica >= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicial").toString())+"'" +
									" AND re.fecha_modifica <= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinal").toString())+"') ";
				}
			}
		}
		
		if(criteriosBusquedaMap.containsKey("viaIngreso"))
		{
			if(!UtilidadTexto.isEmpty(criteriosBusquedaMap.get("viaIngreso").toString()))
			{
				consultaArmada+=" AND vi.codigo = "+criteriosBusquedaMap.get("viaIngreso").toString();
			}
		}
		
		consultaArmada+=" ORDER BY via, paciente ";
		return consultaArmada;*/
		
		String consultaArmada = "", condicionesUno = "", condicionesDos = "";
		
		//Se modificaron por que siempre son requeridos no hace falta validarlos
		//if(criteriosBusquedaMap.containsKey("fechaInicial") && criteriosBusquedaMap.containsKey("fechaFinal"))
		//{
			//if(!UtilidadTexto.isEmpty(criteriosBusquedaMap.get("fechaInicial")+"") && 
			   //!UtilidadTexto.isEmpty(criteriosBusquedaMap.get("fechaFinal")+""))
			//{
		
		//Si se realiza la consulta por cualquier tipo evento
		if((criteriosBusquedaMap.get("tipoEvento")+"").equals(""))
		{
			condicionesUno = "WHERE (r.fecha_grabacion >= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicial")+"")+"' " +
						  	 		"AND r.fecha_grabacion <= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinal")+"")+"') ";
			condicionesDos = "WHERE (re.fecha_modifica >= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicial")+"")+"' " +
						     		"AND re.fecha_modifica <= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinal")+"")+"') ";
			
			if(!UtilidadTexto.isEmpty(criteriosBusquedaMap.get("viaIngreso")+""))
			{
				condicionesUno += "AND vi.codigo = "+criteriosBusquedaMap.get("viaIngreso")+" ";
				condicionesDos += "AND vi.codigo = "+criteriosBusquedaMap.get("viaIngreso")+" ";
			}
			
			if(ValoresPorDefecto.getPermitirFacturarReclamarCuentasConRegistroPendientes(Utilidades.convertirAEntero(criteriosBusquedaMap.get("institucion")+"")).equals(ConstantesBD.acronimoNo)){
				condicionesUno += "AND r.estado = '"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"' ";
				condicionesDos += "AND re.estado = '"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"' ";
			}
			
			consultaArmada = strConsultaFosyga+"(("+strConsultaFosygaPrimeraParte+condicionesUno+") UNION ("+strConsultaFosygaSegundaParte+condicionesDos+")) t ORDER BY t.via, t.paciente ";
		}
		//Si se realiza la consulta solo para el Tipo Evento Accidente
		else if((criteriosBusquedaMap.get("tipoEvento")+"").equals(ConstantesIntegridadDominio.acronimoAccidenteTransito))
		{
			condicionesUno = "WHERE (r.fecha_grabacion >= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicial")+"")+"' " +
							  		"AND r.fecha_grabacion <= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinal")+"")+"') ";
			
			if(!UtilidadTexto.isEmpty(criteriosBusquedaMap.get("viaIngreso")+""))
				condicionesUno += "AND vi.codigo = "+criteriosBusquedaMap.get("viaIngreso")+" ";
			
			if(ValoresPorDefecto.getPermitirFacturarReclamarCuentasConRegistroPendientes(Utilidades.convertirAEntero(criteriosBusquedaMap.get("institucion")+"")).equals(ConstantesBD.acronimoNo)){
				condicionesUno += "AND r.estado = '"+ConstantesIntegridadDominio.acronimoEstadoProcesado+"' ";
			}
			
			consultaArmada = strConsultaFosygaPrimeraParte+condicionesUno+"ORDER BY via, paciente";
		}
		//Si se realiza la consulta solo para el Tipo Evento Catastrofico
		else if((criteriosBusquedaMap.get("tipoEvento")+"").equals(ConstantesIntegridadDominio.acronimoEventoCatastrofico))
		{
			condicionesDos = "WHERE (re.fecha_modifica >= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicial")+"")+"' " +
							 		"AND re.fecha_modifica <= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinal")+"")+"') ";
			
			if(!UtilidadTexto.isEmpty(criteriosBusquedaMap.get("viaIngreso")+""))
				condicionesDos += "AND vi.codigo = "+criteriosBusquedaMap.get("viaIngreso")+" ";
			
			if(ValoresPorDefecto.getPermitirFacturarReclamarCuentasConRegistroPendientes(Utilidades.convertirAEntero(criteriosBusquedaMap.get("institucion")+"")).equals(ConstantesBD.acronimoNo)){
				condicionesDos += "AND re.estado = '"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"' ";
			}
			
			consultaArmada = strConsultaFosygaSegundaParte+condicionesDos+"ORDER BY via, paciente";
		}
		
			//}
		//}
		
		/*if(criteriosBusquedaMap.containsKey("viaIngreso"))
		{
			if(!UtilidadTexto.isEmpty(criteriosBusquedaMap.get("viaIngreso")+""))
			{
				condicionesUno += "AND vi.codigo = "+criteriosBusquedaMap.get("viaIngreso")+" ";
				condicionesDos += "AND vi.codigo = "+criteriosBusquedaMap.get("viaIngreso")+" ";
			}
		}*/
		
		return consultaArmada;
	}

	/**
	 * 
	 * @param codigo
	 * @param esAccidenteTransito
	 * @return
	 */
	public static String obtenerQueryAnexoGastosTransporte(String codigo, boolean esAccidenteTransito) 
	{
		String consulta="";
		if(esAccidenteTransito)
		{
			consulta="select " +
					"r.codigo as numeroInforme1, " +
					"to_char(r.fecha_grabacion, 'DD/MM/YYYY') as fecha1, " +
					"r.placa as placa1, " +
					"r.numero_poliza as numeroPoliza1, " +
					"r.apellido_nombre_transporta as nombresTransporta, " +
					"r.tipo_id_transporta as tipoIdTransporta, " +
					"r.numero_id_transporta as numeroIdTransporta, " +
					"getnombreciudad(r.pais_exp_id_transporta,r.depto_exp_id_transporta, r.ciudad_exp_id_transporta) as ciudadExpIdTransporta, " +
					"r.direccion_transporta as direccionTransporta, " +
					"getnombreciudad(r.pais_transporta,r.departamento_transporta, r.ciudad_transporta) as ciudadTransporta, " +
					"r.telefono_transporta as telefonoTransporta, " +
					"r.transporta_victima_desde as transportaVictimaDesde, " +
					"r.transporta_victima_hasta as transportaVictimaHasta, " +
					"getintegridaddominio(r.tipo_transporte) as tipoTransporte, " +
					"r.placa_vehiculo_tranporta as placaVehiculoTransporta " +
					"from " +
					"registro_accidentes_transito r " +
					"where " +
					"r.codigo="+codigo;
		}
		else
		{
			consulta="select " +
					"r.codigo as numeroInforme1, " +
					"to_char(r.fecha_modifica, 'DD/MM/YYYY') as fecha1, " +
					"'' as placa1, " +
					"'' as numeroPoliza1, " +
					"r.apellido_nombre_transporta as nombresTransporta, " +
					"r.tipo_id_transporta as tipoIdTransporta, " +
					"r.numero_id_transporta as numeroIdTransporta, " +
					"getnombreciudad(r.pais_exp_id_transporta,r.depto_exp_id_transporta, r.ciudad_exp_id_transporta) as ciudadExpIdTransporta, " +
					"r.direccion_transporta as direccionTransporta, " +
					"getnombreciudad(r.pais_transporta,r.departamento_transporta, r.ciudad_transporta) as ciudadTransporta, " +
					"r.telefono_transporta as telefonoTransporta, " +
					"r.transporta_victima_desde as transportaVictimaDesde, " +
					"r.transporta_victima_hasta as transportaVictimaHasta, " +
					"getintegridaddominio(r.tipo_transporte) as tipoTransporte, " +
					"r.placa_vehiculo_tranporta as placaVehiculoTransporta " +
					"from " +
					"registro_evento_catastrofico r " +
					"where " +
					"r.codigo="+codigo;
		}
		return consulta;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param esRegistroAccidenteTransito
	 * @return
	 */
	public static boolean existeInfoGastosTransporteMovilizacionVictima(Connection con, int codigo, boolean esRegistroAccidenteTransito)
	{
		String consulta="SELECT apellido_nombre_transporta as apellidoNombre FROM registro_accidentes_transito WHERE codigo=?";
		if(!esRegistroAccidenteTransito)
			consulta="SELECT apellido_nombre_transporta as apellidoNombre FROM registro_evento_catastrofico WHERE codigo=?";
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigo+""));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				if(!UtilidadTexto.isEmpty(rs.getString("apellidoNombre")))
				{
					return true;
				}
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
}
