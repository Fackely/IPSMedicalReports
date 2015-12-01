/*
 * Ago 13, 2006
 */
package com.princetonsa.dao.sqlbase.pyp;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatos;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.pyp.DtoObservacionProgramaPYP;

/**
 * @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad Programas de Promoción y Prevencion
 */
public class SqlBaseProgramasPYPDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseProgramasPYPDao.class);
	
	/**
	 * Cadena que verifica si un paciente tiene hoja obstétrica sin finalizar
	 */
	private static final String tieneHojaObsSinFinalizarStr = "SELECT count(1) As cuenta FROM hoja_obstetrica WHERE paciente = ? AND fin_embarazo = '"+ConstantesBD.acronimoNo+"'";
	
	/**
	 * Cadena que consulta el diagnostico de la ultima evolucion del paciente
	 * (solo aplica para pacientes de hospitalizacion y urgencias)
	 */
	private static final String consultarDiagnosticoEvolucionStr = "SELECT "+ 
		"ed.acronimo_diagnostico AS acronimo, "+
		"ed.tipo_cie_diagnostico AS tipo_cie "+  
		"FROM solicitudes s "+ 
		"INNER JOIN valoraciones v ON(v.numero_solicitud=s.numero_solicitud) "+
		"INNER JOIN evoluciones e ON (e.valoracion=v.numero_solicitud) "+
		"INNER JOIN evol_diagnosticos ed ON(ed.evolucion=e.codigo and " +
			"ed.principal="+ValoresPorDefecto.getValorTrueParaConsultas()+" and " +
			"ed.definitivo="+ValoresPorDefecto.getValorTrueParaConsultas()+") " +
		"WHERE "+ 
		"s.cuenta = ? order by e.fecha_evolucion DESC,e.hora_evolucion DESC";
	
	/**
	 * Cadena que consulta el diagnostico egreso de las valoraciones del paciente
	 * (aplica para hopsitalizacion, urgencias y consulta externa)
	 */
	private static final String consultarDiagnosticosValoracionStr = "SELECT "+ 
		"vd.acronimo_diagnostico AS acronimo, "+
		"vd.tipo_cie_diagnostico AS tipo_cie "+ 
		"FROM solicitudes s "+ 
		"INNER JOIN valoraciones v ON(v.numero_solicitud=s.numero_solicitud) "+
		"INNER JOIN val_diagnosticos vd ON(vd.valoracion=v.numero_solicitud and " +
			"vd.principal="+ValoresPorDefecto.getValorTrueParaConsultas()+" and " +
			"vd.definitivo="+ValoresPorDefecto.getValorTrueParaConsultas()+") "+ 
		"WHERE "+ 
		"s.cuenta = ?";
	
	
	//****************************************************************************************+
	//************CONSULTAR PARA PROGRAMAS****************************************************+
	//****************************************************************************************+
	/**
	 * Cadena que consulta los programas nuevos a los cuales califica el paciente por convenio
	 */
	private static final String consultarProgramasConvenioPacienteStr = "SELECT DISTINCT "+ 
		"0 AS consecutivo, "+
		"psp.codigo AS codigo_programa, "+
		"psp.institucion AS institucion, "+
		"psp.descripcion AS nombre_programa, "+
		"tpp.descripcion AS tipo_programa, "+
		"CASE WHEN psp.archivo IS NULL THEN '' ELSE psp.archivo END AS archivo, "+
		"CASE WHEN psp.formato IS NULL THEN '' ELSE psp.formato || '' END AS formato, "+
		"'' AS fecha_inicial, "+
		"'' As fecha_final, "+
		"'false' AS existe," +
		"'false' As finalizado, "+
		"'' As motivo_finalizacion "+
		"FROM prog_act_pyp_convenio papc "+ 
		"INNER JOIN actividades_programa_pyp app ON(app.codigo=papc.actividad_programa_pyp) "+
		"INNER JOIN programas_salud_pyp psp ON(psp.codigo=app.programa AND psp.institucion=app.institucion) "+ 
		"INNER JOIN tipos_programa_pyp tpp ON(tpp.codigo=psp.tipo_programa AND tpp.institucion=psp.institucion) "+
		"INNER JOIN grup_etareo_creci_desa gecd ON(gecd.codigo=psp.grupo_etareo) ";
	
	/**
	 * Cadena para consultar los programas que no tenga parametrizado el convenio pero 
	 * que en la parametrizacion de activididades por programa tengan asociada una 
	 * actividad definida como REQUERIDA
	 */
	private static final String consultarProgramasPacienteStr = "SELECT DISTINCT "+ 
		"0 AS consecutivo, "+ 
		"psp.codigo AS codigo_programa, "+ 
		"psp.institucion AS institucion, "+ 
		"psp.descripcion AS nombre_programa, "+ 
		"tpp.descripcion AS tipo_programa, "+ 
		"CASE WHEN psp.archivo IS NULL THEN '' ELSE psp.archivo END AS archivo, "+ 
		"CASE WHEN psp.formato IS NULL THEN '' ELSE psp.formato || '' END AS formato, "+ 
		"'' AS fecha_inicial, "+ 
		"'' As fecha_final, "+ 
		"'false' AS existe, "+
		"'false' As finalizado, "+ 
		"'' As motivo_finalizacion "+ 
		"FROM actividades_programa_pyp app "+ 
		"INNER JOIN programas_salud_pyp psp ON(psp.codigo=app.programa AND psp.institucion=app.institucion) "+ 
		"INNER JOIN tipos_programa_pyp tpp ON(tpp.codigo=psp.tipo_programa AND tpp.institucion=psp.institucion) " +
		"INNER JOIN grup_etareo_creci_desa gecd ON(gecd.codigo=psp.grupo_etareo) ";
	
	
	/**
	 * Cadena que inserta un programa pyp a un paciente
	 */
	private static final String insertarProgramaStr = "INSERT INTO " +
		"programas_pyp_paciente " +
		"(codigo,paciente,programa,usuario,fecha_inicial,fecha_final,hora_inicial,hora_grabacion,fecha_grabacion,institucion,motivo_finalizacion) " ;
	
	/**
	 * Cadena que modifica un programa PYP de un paciente
	 */
	private static final String modificarProgramaStr = "UPDATE programas_pyp_paciente SET " +
		"fecha_final = ? , " +
		"hora_final = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", "+
		"motivo_finalizacion = ?, " +
		"usuario_finaliza = ? " +
		"WHERE codigo = ?";
		
	///****************************************************************************************+
	///*************CONSULTAS 	ADICIONALES***************************************************+
	///****************************************************************************************+
	
	/**
	 * Método que consulta los centros de atención de una actividad
	 */
	private static final String consultarCentrosAtencionActividadStr = "SELECT centro_atencion FROM act_pyp_centro_atencion WHERE actividad_pyp = ? AND institucion = ?";
	
	/**
	 * Cadena que consulta las actividades de un programa por convenio 
	 * NUEVOS registros
	 */
	private static final String consultarActividadesProgramaConvenioStr = " SELECT "+ 
		"0 AS consecutivo, "+
		"app.actividad AS consecutivo_actividad, "+
		"'"+ConstantesBD.acronimoTipoActividadPYPServicio+"' AS tipo_actividad, "+
		"ap.servicio AS codigo_actividad, "+
		"'(' || getcodigoespecialidad(ap.servicio) || '-' || ap.servicio || ') ' || getnombreservicio(ap.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_actividad, "+
		"CASE WHEN app.embarazo = '"+ValoresPorDefecto.getValorTrueParaConsultas()+"' AND app.semanas_gestacion IS NOT NULL THEN app.semanas_gestacion || '' ELSE  '' END AS semanas_gestacion, "+
		"CASE WHEN app.archivo IS NULL THEN '' ELSE app.archivo END AS archivo, "+
		"app.institucion As institucion, "+
		"CASE WHEN appge.frecuencia IS NULL THEN '' ELSE appge.frecuencia || ''  END AS frecuencia, "+
		"0 AS codigo_estado_actividad, "+
		"0 as numero_solicitud, "+
		"0 as numero_orden, "+
		"appvi.solicitar AS solicitar, "+
		"appvi.programar AS programar, "+
		"appvi.ejecutar AS ejecutar, " +
		ValoresPorDefecto.getValorFalseParaConsultas()+" AS cancelar, "+
		"'false' As existe, " +
		"tfp.descripcion AS desc_frecuencia," +
		"appge.tipo_frecuencia AS tipofrecuencia  "+  
		"FROM prog_act_pyp_convenio papc "+ 
		"INNER JOIN convenios c ON(papc.convenio=c.codigo) "+ 
		"INNER JOIN actividades_programa_pyp app ON(app.codigo=papc.actividad_programa_pyp) "+
		"INNER JOIN actividades_pyp ap ON(ap.consecutivo=app.actividad) "+ 
		"INNER JOIN acti_prog_pup_grup_eta appge ON(appge.codigo_act_prog_pyp=app.codigo AND appge.regimen=c.tipo_regimen) "+ 
		"INNER JOIN grup_etareo_creci_desa gecd ON(gecd.codigo=appge.grupo_etareo) "+ 
		"INNER JOIN acti_prog_pyp_vias_ing appvi ON(appvi.codigo_act_prog_pyp=app.codigo) " +
		"LEFT OUTER JOIN pyp.tipo_frecuencia_pyp tfp on (tfp.codigo=appge.tipo_frecuencia ) ";
	
	/**
	 * Cadena que consulta las actividades de un programa que no están en el convenio
	 * pero son requeridas por la ley
	 * NUEVOS registros
	 */
	private static final String consultarActividadesProgramaSinConvenioStr = " SELECT "+ 
		"0 AS consecutivo, "+
		"app.actividad AS consecutivo_actividad, "+
		"'"+ConstantesBD.acronimoTipoActividadPYPServicio+"' AS tipo_actividad, "+
		"ap.servicio AS codigo_actividad, "+
		"'(' || getcodigoespecialidad(ap.servicio) || '-' || ap.servicio || ') ' || getnombreservicio(ap.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_actividad, "+
		"CASE WHEN app.embarazo ='"+ValoresPorDefecto.getValorTrueParaConsultas()+"' AND app.semanas_gestacion IS NOT NULL THEN app.semanas_gestacion || '' ELSE  '' END AS semanas_gestacion, "+
		"CASE WHEN app.archivo IS NULL THEN '' ELSE app.archivo END AS archivo, "+
		"app.institucion As institucion, "+
		"CASE WHEN appge.frecuencia IS NULL THEN '' ELSE appge.frecuencia || '' END AS frecuencia, "+
		"0 AS codigo_estado_actividad, "+
		"0 AS numero_solicitud, "+
		"0 AS numero_orden, "+
		"appvi.solicitar AS solicitar, "+
		"appvi.programar AS programar, "+
		"appvi.ejecutar AS ejecutar, "+
		ValoresPorDefecto.getValorFalseParaConsultas()+" As cancelar, "+
		"'false' As existe, "+
		"tfp.descripcion AS desc_frecuencia, " +
		"appge.tipo_frecuencia AS tipofrecuencia  "+ 
		"FROM actividades_programa_pyp app "+ 
		"INNER JOIN actividades_pyp ap ON(ap.consecutivo=app.actividad) "+ 
		"INNER JOIN acti_prog_pup_grup_eta appge ON(appge.codigo_act_prog_pyp=app.codigo) "+ 
		"INNER JOIN grup_etareo_creci_desa gecd ON(gecd.codigo=appge.grupo_etareo) "+ 
		"INNER JOIN acti_prog_pyp_vias_ing appvi ON(appvi.codigo_act_prog_pyp=app.codigo) " +
		"LEFT OUTER JOIN pyp.tipo_frecuencia_pyp tfp on (tfp.codigo=appge.tipo_frecuencia ) " ;
	
	/**
	 * Cadena que consulta las actividades de un programa existentes del paciente
	 * registros EXISTENTES
	 */
	private static final String consultarActividadesProgramaExistentesStr = " SELECT "+ 
		"appp.codigo AS consecutivo, "+
		"app.actividad AS consecutivo_actividad, "+
		"app.codigo AS codigo_act_pro, "+
		"'"+ConstantesBD.acronimoTipoActividadPYPServicio+"' AS tipo_actividad, "+
		"ap.servicio AS codigo_actividad, "+
		"'(' || getcodigoespecialidad(ap.servicio) || '-' || ap.servicio || ') ' || getnombreservicio(ap.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_actividad, "+
		"CASE WHEN app.embarazo = '"+ValoresPorDefecto.getValorTrueParaConsultas()+"' AND app.semanas_gestacion IS NOT NULL THEN app.semanas_gestacion || '' ELSE  '' END AS semanas_gestacion, "+
		"CASE WHEN app.archivo IS NULL THEN '' ELSE app.archivo END AS archivo, "+
		"app.institucion As institucion, "+
		"CASE WHEN appp.frecuencia IS NULL THEN '' ELSE appp.frecuencia || '' END AS frecuencia, "+
		"appp.estado AS codigo_estado_actividad, "+
		"CASE WHEN appp.numero_solicitud IS NULL THEN 0 ELSE appp.numero_solicitud END AS numero_solicitud, "+
		"CASE WHEN appp.numero_orden IS NULL THEN 0 ELSE appp.numero_orden END AS numero_orden, "+
		"'' AS solicitar, "+
		"'' As programar, "+
		"'' AS ejecutar, "+
		"'' AS cancelar, "+
		"tfp.descripcion AS desc_frecuencia, " +
		"appge.tipo_frecuencia AS tipofrecuencia  "+ 
		"FROM programas_pyp_paciente ppp "+
		"INNER JOIN act_prog_pyp_pac appp ON(appp.codigo_prog_pyp_pac=ppp.codigo) "+
		"INNER JOIN actividades_programa_pyp app ON(app.programa=ppp.programa AND app.institucion=ppp.institucion AND app.actividad=appp.actividad) "+
		"INNER JOIN acti_prog_pup_grup_eta appge ON(appge.codigo_act_prog_pyp=app.codigo) "+ 
		"INNER JOIN actividades_pyp ap ON(ap.consecutivo=app.actividad) "+ 
		"INNER JOIN estados_programas_pyp e ON(e.codigo=appp.estado) "+ 
		"LEFT OUTER JOIN solicitudes s ON(s.numero_solicitud=appp.numero_solicitud) " +
		"LEFT OUTER JOIN pyp.tipo_frecuencia_pyp tfp on (tfp.codigo=appge.tipo_frecuencia ) " ;
	
	/**
	 * Cadena que consulta los articulo x programa del paciente
	 * registros EXISTENTES
	 */
	private static final String consultarArticulosProgramaExistentesStr = "SELECT "+ 
		"appp.codigo AS consecutivo, "+
		"appp.actividad AS consecutivo_actividad, "+
		"'"+ConstantesBD.acronimoTipoActividadPYPArticulo+"' AS tipo_actividad, "+
		"ap.articulo AS codigo_actividad, "+
		"getdescarticulo(ap.articulo) AS nombre_actividad, "+
		"ap.institucion As institucion, "+
		"appp.estado AS codigo_estado_actividad, "+
		"CASE WHEN appp.numero_solicitud IS NULL THEN 0 ELSE appp.numero_solicitud END AS numero_solicitud, "+
		"CASE WHEN appp.numero_orden IS NULL THEN 0 ELSE appp.numero_orden END AS numero_orden, "+
		"CASE WHEN appp.frecuencia IS NULL THEN '' ELSE appp.frecuencia || '' END AS frecuencia, "+
		"'' AS solicitar, "+
		"'' As programar, "+
		"'' AS ejecutar," +
		"'' AS cancelar," +
		"'true' AS existe "+ 
		"FROM programas_pyp_paciente ppp "+
		"INNER JOIN act_prog_pyp_pac appp ON(appp.codigo_prog_pyp_pac=ppp.codigo) "+ 
		"INNER JOIN actividades_pyp ap ON(ap.consecutivo=appp.actividad AND ap.servicio IS NULL) " +
		"LEFT OUTER JOIN pyp.tipo_frecuencia_pyp tfp on (tfp.codigo=appp.tipo_frecuencia ) " ;
	   
	
	/**
	 * Cadena qiue consulta los articulos x programa que oueden asignarse a un paciente
	 */
	private static final String consultarArticulosProgramaNuevosStr = "SELECT "+ 
		"0 as consecutivo, "+ 
		"ap.consecutivo AS consecutivo_actividad, "+ 
		"'"+ConstantesBD.acronimoTipoActividadPYPArticulo+"' AS tipo_actividad, "+ 
		"ap.articulo AS codigo_actividad, "+ 
		"getdescarticulo(ap.articulo) AS nombre_actividad, "+ 
		"ap.institucion As institucion, "+ 
		"0 AS codigo_estado_actividad, "+ 
		"0 AS numero_solicitud, "+ 
		"0 AS numero_orden, "+ 
		"appvi.solicitar AS solicitar, " +
		"appvi.programar AS programar, "+ 
		"appvi.ejecutar AS ejecutar, "+
		ValoresPorDefecto.getValorFalseParaConsultas()+" AS cancelar, "+
		"CASE WHEN appge.frecuencia IS NULL THEN '' ELSE appge.frecuencia || '' END AS frecuencia, "+ 
		"'false' AS existe," +
		"tf.descripcion AS desc_frecuencia, " +
		"appge.tipo_frecuencia "+ 
		"FROM actividades_art_pyp_programa aapp "+ 
		"INNER JOIN actividades_pyp ap ON(ap.consecutivo=aapp.articulo) "+  
		"INNER JOIN acti_art_prog_pyp_grup_eta appge ON(appge.programa=aapp.programa AND appge.articulo_act=aapp.articulo AND appge.institucion = aapp.institucion) "+		
		"INNER JOIN grup_etareo_creci_desa gecd ON(gecd.codigo=appge.grupo_etareo) "+
		"INNER JOIN acti_art_prog_pyp_vias_ing appvi ON(appvi.programa=aapp.programa AND appvi.articulo_act=aapp.articulo AND appvi.institucion=aapp.institucion) "+ 
	    "LEFT OUTER JOIN tipo_frecuencia_pyp tf ON (tf.codigo = appge.tipo_frecuencia ) ";
	/**
	 * Cadena que inserta una nueva actividad PYP al paciente
	 */
	private static final String insertarActividadStr = "INSERT INTO " +
		"act_prog_pyp_pac " +
		"(codigo,codigo_prog_pyp_pac,actividad,estado,numero_orden,numero_solicitud," +
		"fecha_programar,hora_programar,fecha_solicitar,hora_solicitar,fecha_ejecutar,hora_ejecutar,fecha_cancelar,hora_cancelar," +
		"usuario_solicitar,usuario_programar,usuario_ejecutar,usuario_cancelar,centro_atencion,frecuencia) " ;
		
	
	
	/**
	 * Cadena para consultar los históricos de una actividad
	 */
	private static final String consultarHistoricosActividadStr = "SELECT "+ 
		"CASE WHEN appp.estado = "+ConstantesBD.codigoEstadoProgramaPYPSolicitado+" THEN "+ 
			"to_char(appp.fecha_solicitar,'DD/MM/YYYY') " +
		"ELSE "+ 
			"CASE WHEN appp.estado = "+ConstantesBD.codigoEstadoProgramaPYPEjecutado+" THEN "+ 
				"to_char (appp.fecha_ejecutar,'DD/MM/YYYY') " +
			"ELSE "+
				"CASE WHEN appp.estado = "+ConstantesBD.codigoEstadoProgramaPYPProgramado+" THEN "+ 
					"to_char (appp.fecha_programar,'DD/MM/YYYY') " +
				"ELSE "+
					"to_char (appp.fecha_cancelar,'DD/MM/YYYY') " +
				"END " +
			"END " +
		"END As fecha_registro, "+
		"epp.descripcion AS estado, "+
		"CASE WHEN appp.estado = "+ConstantesBD.codigoEstadoProgramaPYPSolicitado+" THEN "+  
			"getnombreusuario(appp.usuario_solicitar) " +
		"ELSE "+ 
			"CASE WHEN appp.estado = "+ConstantesBD.codigoEstadoProgramaPYPEjecutado+" THEN "+ 
				"getnombreusuario(appp.usuario_ejecutar) " +
			"ELSE "+
				"CASE WHEN appp.estado = "+ConstantesBD.codigoEstadoProgramaPYPProgramado+" THEN "+ 
					"getnombreusuario(appp.usuario_programar) " +
				"ELSE "+
					"getnombreusuario(appp.usuario_cancelar) " +
				"END " +
			"END " +
		"END As profesional,"+
		"CASE WHEN appp.numero_orden IS NULL THEN '' ELSE oa.consecutivo_orden || '' END  AS numero_orden, "+
		"CASE WHEN appp.numero_solicitud IS NULL THEN '' ELSE s.consecutivo_ordenes_medicas || '' END  AS numero_solicitud "+ 
		"FROM act_prog_pyp_pac appp "+ 
		"INNER JOIN estados_programas_pyp epp ON(epp.codigo=appp.estado) "+ 
		"LEFT OUTER JOIN solicitudes s ON(s.numero_solicitud=appp.numero_solicitud) "+
		"LEFT OUTER JOIN ordenes_ambulatorias oa ON(oa.codigo=appp.numero_orden) "+
		"WHERE appp.codigo_prog_pyp_pac = ? AND appp.actividad = ? ORDER BY fecha_registro DESC";
	
	//**********QUERYS RELACIONADAS CON LOS ACUMULADOS******************************************************+
	
	/**
	 *Cadena que inserta una actividad acumulada
	 */
	private static final String insertarActividadAcumuladaStr = "INSERT INTO acumulado_act_prog_pac_pyp " +
		"(codigo,centro_atencion,regimen,programa,actividad,convenio,fecha,cantidad,institucion) ";
	
	/**
	 * Cadena que consulta el consecutivo de una actividad acumulada
	 */
	private static final String consultarActividadAcumuladaStr = "SELECT codigo " +
		"FROM acumulado_act_prog_pac_pyp " +
		"WHERE " +
		"centro_atencion = ? and " +
		"regimen = ? and " +
		"programa = ? and " +
		"actividad = ? AND " +
		"convenio = ? and " +
		"institucion = ? and " +
		"fecha = ?";
	
	/**
	 * Cadena que aumenta el acumulado de una actividad
	 */
	private static final String aumentarAcumuladoActividadStr = "UPDATE acumulado_act_prog_pac_pyp SET cantidad = cantidad +1 WHERE codigo = ?";
	
	//******************************************************************************************************
	
	/**
	 * Cadena que verifica si la actividad ya fue ejecutada para la fecha
	 */
	private static final String estaActividadEjecutadaStr = "SELECT " +
		"count(1) as cuenta "+ 
		"FROM programas_pyp_paciente ppp "+ 
		"INNER JOIN act_prog_pyp_pac appp ON(appp.codigo_prog_pyp_pac=ppp.codigo) "+ 
		"WHERE "+ 
		"ppp.paciente = ? AND "+ 
		"ppp.programa = ? AND "+ 
		"ppp.institucion = ? AND "+ 
		"appp.actividad = ? AND "+ 
		"appp.estado = "+ConstantesBD.codigoEstadoProgramaPYPEjecutado+" AND "+ 
		"appp.fecha_ejecutar = CURRENT_DATE ";
	
	/**
	 * Cadena que verifica si la actividad ya fue ejecutada para la fecha,
	 * y si la actividad permite ser ejecutada varias veces al día
	 */
	private static final String permiteEjecutarActividadStr =
		"SELECT " +
		"count(1) as cuenta	" +
		"FROM programas_pyp_paciente ppp " +
		"INNER JOIN act_prog_pyp_pac appp ON(appp.codigo_prog_pyp_pac=ppp.codigo) " +
		"INNER JOIN actividades_pyp actpyp ON(actpyp.consecutivo=appp.actividad) " +
		"INNER JOIN actividades_programa_pyp appyp ON(appyp.actividad=actpyp.consecutivo) " +
		"WHERE " +
		"ppp.paciente = ? AND appyp.programa = ? AND " +
		"ppp.institucion = ? AND " +
		"appp.actividad = ? AND " +
		"appp.estado = "+ConstantesBD.codigoEstadoProgramaPYPEjecutado+" AND " +
		"appp.fecha_ejecutar = CURRENT_DATE AND " +
		"appyp.permitir_ejecutar=false";
	
	
	/**
	 * Cadena que consulta la finalidad consulta de la actividad
	 */
	private static final String consultarFinalidadConsultaStr = "SELECT "+ 
		"finalidad_consulta AS codigo_finalidad, "+
		"getnomfinalidadconsulta(finalidad_consulta) as nombre_finalidad "+ 
		"FROM actividades_programa_pyp "+ 
		"WHERE "+ 
		"programa = ? and institucion = ? and actividad = ?";
	
	/**
	 * Cadena que consulta la finalidad servicio de la actividad
	 */
	private static final String consultarFinalidadServicioStr = "SELECT "+
		"app.finalidad_servicio AS codigo_finalidad, "+
		"fs.nombre AS nombre_finalidad "+ 
		"FROM actividades_programa_pyp app "+ 
		"INNER JOIN finalidades_servicio fs ON(fs.codigo=app.finalidad_servicio) "+ 
		"WHERE "+ 
		"app.programa = ? and app.institucion = ? and app.actividad = ?";
	
	/**
	 * Cadena que consulta la finalidad de una consulta PYP
	 */
	private static final String consultarFinalidadActividadConsultaStr = "SELECT "+ 
		"app.finalidad_consulta AS codigo_finalidad, "+
		"getnomfinalidadconsulta(app.finalidad_consulta) AS nombre_finalidad "+ 
		"FROM act_prog_pyp_pac appp "+ 
		"INNER JOIN programas_pyp_paciente ppp ON(ppp.codigo=appp.codigo_prog_pyp_pac) "+ 
		"INNER JOIN actividades_programa_pyp app ON(app.programa=ppp.programa and app.institucion=ppp.institucion and app.actividad = appp.actividad) "+ 
		"WHERE "+ 
		"appp.numero_solicitud = ?";
	
	/**
	 * Cadena que consulta el consecutivo de un programa existente
	 */
	private static final String consultarConsecutivoProgramaExistenteStr = "SELECT " +
		"codigo As consecutivo " +
		"FROM programas_pyp_paciente " +
		"WHERE paciente = ? AND programa = ? AND institucion = ?";
		
	

	/**
	 * Método implementado para verificar si un paciente tiene hoja obstétrica sin finalizar
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean tieneHojaObsSinFinalizar(Connection con,HashMap campos)
	{
		try
		{
			boolean sinFinalizar = false;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(tieneHojaObsSinFinalizarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoPaciente")+""));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				if(rs.getInt("cuenta")>0)
					sinFinalizar = true;
			}
			
			return sinFinalizar;
		}
		catch(SQLException e)
		{
			logger.error("Error en tieneHojaObsSinFinalizar de SqlBaseProgramasPYPDao: "+e);
			return false;
		}
	}
	
	/**
	 * Método que consulta los diangosticos de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String consultarDiagnosticosPaciente(Connection con,HashMap campos)
	{
		try
		{
			String diagnosticos = "";
			int viaIngreso = Utilidades.convertirAEntero(campos.get("viaIngreso").toString());
			boolean encontro = false;
			PreparedStatementDecorator pst = null;
			ResultSetDecorator rs = null;
			
			//Se consulta el diagnostico de la ultima evolucion
			if(viaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion||viaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consultarDiagnosticoEvolucionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Utilidades.convertirAEntero(campos.get("idCuenta")+""));
			
				rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					diagnosticos = "'"+rs.getString("acronimo")+"-"+rs.getString("tipo_cie")+"'";
					encontro = true;
				}
			}
			
			
			//se consulta el diagnostico de la valoracion, en caso de que la hopsitalizacion o uirgencias no tengan
			//la del egreso o que sea de via de ingreso consulta externa
			if(viaIngreso==ConstantesBD.codigoViaIngresoConsultaExterna||!encontro)
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consultarDiagnosticosValoracionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Utilidades.convertirAEntero(campos.get("idCuenta")+""));
				
				rs = new ResultSetDecorator(pst.executeQuery());
				while(rs.next())
				{
					if(!diagnosticos.equals(""))
						diagnosticos += ",";
					diagnosticos += "'"+rs.getString("acronimo")+"-"+rs.getString("tipo_cie")+"'";
				}
			}
			
			return diagnosticos;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDiagnosticosPaciente de SqlBaseProgramaPYPDao: "+e);
			return "";
		}
	}
	
	/**
	 * Método implementado para consultar los programas x convenio nuevos que califican para el paciente
	 *  + sus programas pyp existentes 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarProgramasPaciente(Connection con,HashMap campos)
	{
		try
		{
			String consulta = "";
			//consulta de los programas x convenio
			String consultaConvenio = "";
			String consultaRequeridos = "";
			String consultaObservaciones="SELECT 0 AS consecutivo, psp.codigo AS codigo_programa, psp.institucion AS institucion, "+
					  							"psp.descripcion AS nombre_programa, tpp.descripcion AS tipo_programa, CASE WHEN psp.archivo IS NULL THEN '' ELSE psp.archivo END AS archivo, "+
					  							"CASE WHEN psp.formato IS NULL THEN '' ELSE psp.formato || '' END AS formato, "+
					  							"'' AS fecha_inicial, '' AS fecha_final, 'false' AS existe, 'false' AS finalizado, '' AS motivo_finalizacion "+
					  						"FROM obser_prog_pyp_pac oppp "+
					  						"INNER JOIN programas_salud_pyp psp ON(psp.codigo=oppp.programa) "+
					  						"INNER JOIN tipos_programa_pyp tpp ON(tpp.codigo=psp.tipo_programa AND tpp.institucion =psp.institucion) ";
			//consulta de los programas existentes del paciente
			String consultaExistentes = "("+campos.get("consultarProgramasExistentes") + " WHERE "+  
				"ppp.paciente = "+campos.get("codigoPaciente")+" AND ppp.institucion = "+campos.get("institucion")+
				" ) UNION "+
				" ("+consultaObservaciones+" WHERE oppp.paciente = "+campos.get("codigoPaciente")+
				" AND oppp.programa not in (select ppp2.programa from programas_pyp_paciente ppp2 where ppp2.paciente = "+campos.get("codigoPaciente")+") "+
				" AND psp.institucion = "+campos.get("institucion")+
				" ) ";
			
			boolean porConvenio = UtilidadTexto.getBoolean(campos.get("porConvenio").toString());
			
			//se verifica si es por convenio
			if(porConvenio)
			{
				/***********CONSULTA PROGRAMAS PACIENTE X CONVENIO*********************************************************/
				consultaConvenio = consultarProgramasConvenioPacienteStr + " WHERE ";
				//Filtro básico (convenio)----------------------------------------------------------------
				consultaConvenio += " papc.convenio IN ("+campos.get("listadoConvenios")+") and " +
					"papc.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" and " +
					"papc.institucion = "+campos.get("institucion")+" and " +
					"app.activo = "+ValoresPorDefecto.getValorTrueParaConsultas();
				
				//Filtro por los diagnósticos ----------------------------------------------------------------------
				String diagnosticos = campos.get("diagnosticos").toString();
				if(!UtilidadTexto.isEmpty(diagnosticos))
					consultaConvenio += " and (" +
						"(SELECT count(1) " +
							"FROM diag_prog_sal_pyp " +
							"WHERE codigo=psp.codigo and institucion = psp.institucion and " +
							"acronimo || '-' || tipo_cie IN ("+diagnosticos+")) > 0 " +
						"OR " +
						"(SELECT count(1) FROM diag_prog_sal_pyp WHERE codigo=psp.codigo and institucion = psp.institucion ) = 0 " +
						") ";
				else
					//si no hay diagnosticos se toman los programas que tienen nulos
					consultaConvenio += " and (SELECT count(1) FROM diag_prog_sal_pyp WHERE codigo=psp.codigo and institucion = psp.institucion ) = 0 ";
				
				//Filtro embarazo --------------------------------------------------------------------------------
				if(UtilidadTexto.getBoolean(campos.get("esEmbarazo")+""))
					consultaConvenio += " and (psp.embarazo = '"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' OR psp.embarazo = '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' OR psp.embarazo IS NULL )";
				else
					consultaConvenio += " and ( psp.embarazo = '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' OR psp.embarazo IS NULL )";
				
				//Filtro Grupo Etareo --------------------------------------------------------------------------
				String edadAnios = campos.get("edadAnio").toString();
				String edadMeses = campos.get("edadMeses").toString();
				String edadDias = campos.get("edadDias").toString();
				
				consultaConvenio += " and ("+
					"(gecd.rango_inicial<= "+edadAnios+" and gecd.rango_final>= "+edadAnios+" and gecd.unidad_medida = "+ConstantesBD.codigoUnidadMedidaFechaAnios+") OR " +
					"(gecd.rango_inicial<= "+edadMeses+" and gecd.rango_final>= "+edadMeses+" and gecd.unidad_medida = "+ConstantesBD.codigoUnidadMedidaFechaMeses+") OR " +
					"(gecd.rango_inicial<= "+edadDias+" and gecd.rango_final>= "+edadDias+" and gecd.unidad_medida = "+ConstantesBD.codigoUnidadMedidaFechaDias+")" +
					") and (gecd.sexo="+campos.get("sexo")+" OR gecd.sexo IS NULL) ";
				
				//Filtro que el programa no haga parte de programas existentes que no han sido finalizados
				consultaConvenio += " AND psp.codigo || '-' || psp.institucion NOT IN " +  
					"(SELECT programa || '-' || institucion from programas_pyp_paciente WHERE paciente = "+campos.get("codigoPaciente")+" and fecha_final is null) ";
				
				/********CONSULTA PROGRAMAS REUQERIDOS DEL PACIENTE QUE NO HAGAN PARTE DEL CONVENIO***********************************************/
				consultaRequeridos = consultarProgramasPacienteStr + " WHERE app.requerido = '"+ValoresPorDefecto.getValorTrueParaConsultas()+"'";
				
				//Filtro por los diagnósticos ----------------------------------------------------------------------
				diagnosticos = campos.get("diagnosticos")+"";
				if(!UtilidadTexto.isEmpty(diagnosticos))
					consultaRequeridos += " and (" +
						"(SELECT count(1) " +
							"FROM diag_prog_sal_pyp " +
							"WHERE codigo=psp.codigo and institucion = psp.institucion and " +
							"acronimo || '-' || tipo_cie IN ("+diagnosticos+")) > 0 " +
						"OR " +
						"(SELECT count(1) FROM diag_prog_sal_pyp WHERE codigo=psp.codigo and institucion = psp.institucion ) = 0 " +
						") ";
				else
					//si no hay diagnosticos se toman los programas que tienen nulos
					consultaRequeridos += " and (SELECT count(1) FROM diag_prog_sal_pyp WHERE codigo=psp.codigo and institucion = psp.institucion ) = 0 ";
				
				
				//Filtro embarazo --------------------------------------------------------------------------------
				if(UtilidadTexto.getBoolean(campos.get("esEmbarazo")+""))
					consultaRequeridos += " and (psp.embarazo = '"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' OR psp.embarazo = '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' OR psp.embarazo IS NULL )";
				else
					consultaRequeridos += " and ( psp.embarazo = '"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' OR psp.embarazo IS NULL )";
				
				//Filtro Grupo Etareo --------------------------------------------------------------------------
				edadAnios = campos.get("edadAnio").toString();
				edadMeses = campos.get("edadMeses").toString();
				edadDias = campos.get("edadDias").toString();
				
				consultaRequeridos += " and ("+
					"(gecd.rango_inicial<= "+edadAnios+" and gecd.rango_final>= "+edadAnios+" and gecd.unidad_medida = "+ConstantesBD.codigoUnidadMedidaFechaAnios+") OR " +
					"(gecd.rango_inicial<= "+edadMeses+" and gecd.rango_final>= "+edadMeses+" and gecd.unidad_medida = "+ConstantesBD.codigoUnidadMedidaFechaMeses+") OR " +
					"(gecd.rango_inicial<= "+edadDias+" and gecd.rango_final>= "+edadDias+" and gecd.unidad_medida = "+ConstantesBD.codigoUnidadMedidaFechaDias+")" +
					") and (gecd.sexo="+campos.get("sexo")+" OR gecd.sexo IS NULL) ";
				
				//Filtro que el programa no haga parte de programas existentes que no han sido finalizados
				consultaRequeridos += " AND psp.codigo || '-' || psp.institucion NOT IN " +
					"(SELECT programa || '-' || institucion from programas_pyp_paciente WHERE paciente = "+campos.get("codigoPaciente")+" and fecha_final is null) ";
				
				//Filtro que el programa no haga parte de programas por convenio del paciente
				consultaRequeridos += " AND psp.codigo || '-' || psp.institucion NOT IN " +
				"(SELECT app.programa || '-' || app.institucion " +
					"FROM prog_act_pyp_convenio papc " +
					"INNER JOIN actividades_programa_pyp app ON(app.codigo=papc.actividad_programa_pyp) " +
					"WHERE " +
					"papc.convenio IN ("+campos.get("listadoConvenios")+") and " +
					"papc.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" and " +
					"papc.institucion = "+campos.get("institucion")+") ";
				
				
				consulta = "("+consultaConvenio+") UNION ("+consultaExistentes+") UNION ("+consultaRequeridos+") ORDER BY tipo_programa ";
			}
			else
				consulta = consultaExistentes + " ORDER BY tipo_programa ";
			
			logger.info("\n\n\n\n\n\n"+consulta+"\n\n\n\n\n\n");
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)),true,false);
	        st.close();
			return mapaRetorno;

		}
		catch(SQLException e)
		{
			logger.error("Error en consultarProgramasConvenioPaciente de SqlBaseProgramasPYPDao: ",e);
			return null;
		}
	}
	
	/**
	 * Método implementado para insertar un programa PYP a un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarPrograma(Connection con,HashMap campos)
	{
		try
		{
			String consulta = insertarProgramaStr + " VALUES ("+campos.get("secuencia")+",?,?,?,CURRENT_DATE,?,"+ValoresPorDefecto.getSentenciaHoraActualBD()+","+ValoresPorDefecto.getSentenciaHoraActualBD()+",CURRENT_DATE,?,?) ";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO " +
				"programas_pyp_paciente " +
				"(codigo,paciente,programa,usuario,fecha_inicial,fecha_final,hora_inicial,hora_grabacion,fecha_grabacion,institucion,motivo_finalizacion)
			 */
			
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoPaciente")+""));
			pst.setString(2,campos.get("codigoPrograma")+"");
			pst.setObject(3,campos.get("usuario"));
			if(campos.get("fechaFinal")!=null)
				pst.setDate(4,Date.valueOf(campos.get("fechaFinal").toString()));
			else
				pst.setNull(4,Types.DATE);
			pst.setInt(5,Utilidades.convertirAEntero(campos.get("institucion")+""));
			if(campos.get("motivoFinalizacion")!=null)
				pst.setString(6,campos.get("motivoFinalizacion")+"");
			else
				pst.setNull(6,Types.VARCHAR);
			
			int resp = pst.executeUpdate();
			
			if(resp>0)
				resp = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con,"seq_prog_pyp_paciente");
			
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarPrograma de SQlBaseProgramasPYPDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para modificar un programa PYP a un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int modificarPrograma(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarProgramaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE programas_pyp_paciente SET " +
					"fecha_final = ? , " +
					"hora_final = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", "+
					"motivo_finalizacion = ?, " +
					"usuario_finaliza = ? " +
					"WHERE codigo = ?
			 */
			
			pst.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString())));
			pst.setString(2,campos.get("motivoFinalizacion")+"");
			pst.setString(3,campos.get("usuarioFinaliza")+"");
			pst.setDouble(4,Utilidades.convertirADouble(campos.get("consecutivo")+""));
			
			return pst.executeUpdate();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en modificarPrograma de SqlBaseProgramasPYPDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para consultar un programa de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarPrograma(Connection con,HashMap campos)
	{
		try
		{
			String consulta = campos.get("consultarProgramasExistentes") + 
				" WHERE ppp.codigo = "+campos.get("consecutivo");
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)),true,false);
	        st.close();
			return mapaRetorno;

		}
		catch(SQLException e)
		{
			logger.error("Error en consultarPrograma de SqlBaseProgramasPYPDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método que consulta las actividades de un programa dependiendo de las
	 * características del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarActividadesProgramaPaciente(Connection con,HashMap campos)
	{
		boolean porConvenio = UtilidadTexto.getBoolean(campos.get("porConvenio").toString());
		
		HashMap mapaExis = consultarActividadesExistentes(con,campos);
		
		//se inician mapas --------------------------
		HashMap mapa = new HashMap();
		mapa.put("numRegistros","0");
		
		//Si es porConvenio se consultarán posibles NUEVAS actividades para el paciente
		if(porConvenio)
			mapa = consultarActividadesNuevas(con,campos);
		
		//se verifica si mapa está lleno
		if(Utilidades.convertirAEntero(mapa.get("numRegistros").toString())>0)
		{
			//Se asigna el mapa a mapaExis
			int pos = Utilidades.convertirAEntero(mapaExis.get("numRegistros").toString());
			
			for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros").toString());i++)
			{
				mapaExis.put("consecutivo_"+pos,mapa.get("consecutivo_"+i));
				mapaExis.put("consecutivo_actividad_"+pos,mapa.get("consecutivo_actividad_"+i));
				mapaExis.put("tipo_actividad_"+pos,mapa.get("tipo_actividad_"+i));
				mapaExis.put("codigo_actividad_"+pos,mapa.get("codigo_actividad_"+i));
				mapaExis.put("nombre_actividad_"+pos,mapa.get("nombre_actividad_"+i));
				mapaExis.put("semanas_gestacion_"+pos,mapa.get("semanas_gestacion_"+i));
				mapaExis.put("archivo_"+pos,mapa.get("archivo_"+i));
				mapaExis.put("institucion_"+pos,mapa.get("institucion_"+i));
				mapaExis.put("frecuencia_"+pos,mapa.get("frecuencia_"+i));
				mapaExis.put("codigo_estado_actividad_"+pos,mapa.get("codigo_estado_actividad_"+i));
				mapaExis.put("numero_solicitud_"+pos,mapa.get("numero_solicitud_"+i));
				mapaExis.put("numero_orden_"+pos,mapa.get("numero_orden_"+i));
				mapaExis.put("solicitar_"+pos,mapa.get("solicitar_"+i));
				mapaExis.put("programar_"+pos,mapa.get("programar_"+i));
				mapaExis.put("ejecutar_"+pos,mapa.get("ejecutar_"+i));
				mapaExis.put("cancelar_"+pos,mapa.get("cancelar_"+i));
				mapaExis.put("existe_"+pos,mapa.get("existe_"+i));
				mapaExis.put("tipo_frecuencia_"+pos, mapa.get("tipo_frecuencia_"+i));
				mapaExis.put("descrp_frecuencia_"+pos, mapa.get("desc_frecuencia_"+i));
				pos++;
			}
			
			mapaExis.put("numRegistros",pos);
		}
			
		return mapaExis;
			
		
	}

	/**
	 * Método implementado para consultar las actividades nuevas que se
	 * pueden asignar a un paciente en PYP
	 * @param con
	 * @param campos
	 * @return
	 */
	private static HashMap consultarActividadesNuevas(Connection con, HashMap campos) 
	{
		HashMap mapa = new HashMap();
		try
		{
			String consulta = "";
			Statement st = null;
			
			/** SE ARMA LA CONSULTA DE ACTIVIDADES POR PROGRAMA POR CONVENIO **/
			String consultaConvenio = consultarActividadesProgramaConvenioStr + " WHERE ";
			//Se crea el filtro principal
			consultaConvenio += " papc.convenio IN ("+campos.get("listadoConvenios")+") and papc.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" and "+
				"app.programa = '"+campos.get("codigoPrograma")+"' and app.institucion = "+campos.get("institucion")+
				" and app.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
			
			//Filtro por los diagnósticos ----------------------------------------------------------------------
			String diagnosticos = campos.get("diagnosticos").toString();
			if(!diagnosticos.equals(""))
				consultaConvenio += " and (" +
					"(SELECT count(1) FROM diag_act_prog_pyp WHERE codigo=app.codigo AND acronimo || '-' || tipo_cie IN ("+diagnosticos+")) > 0 OR" +
					"(SELECT count(1) FROM diag_act_prog_pyp WHERE codigo=app.codigo) = 0 " +
					" )";
			else
				//si no hay diagnosticos se toman las actividades que tienen nulos
				consultaConvenio += " and (SELECT count(1) FROM diag_act_prog_pyp WHERE codigo=app.codigo) = 0 ";
			
			//Filtro embarazo --------------------------------------------------------------------------------
			if(UtilidadTexto.getBoolean(campos.get("esEmbarazo").toString()))
				consultaConvenio += " and (app.embarazo = '"+ValoresPorDefecto.getValorTrueParaConsultas()+"' OR app.embarazo = '"+ValoresPorDefecto.getValorFalseParaConsultas()+"' OR app.embarazo IS NULL ) ";
			else
				consultaConvenio += " and ( app.embarazo = '"+ValoresPorDefecto.getValorFalseParaConsultas()+"' OR app.embarazo IS NULL ) ";
			
			//Filtro Grupo Etareo --------------------------------------------------------------------------
			String edadAnios = campos.get("edadAnio").toString();
			String edadMeses = campos.get("edadMeses").toString();
			String edadDias = campos.get("edadDias").toString();
			
			consultaConvenio += " and ("+
				"(gecd.rango_inicial<= "+edadAnios+" and gecd.rango_final>= "+edadAnios+" and gecd.unidad_medida = "+ConstantesBD.codigoUnidadMedidaFechaAnios+") OR " +
				"(gecd.rango_inicial<= "+edadMeses+" and gecd.rango_final>= "+edadMeses+" and gecd.unidad_medida = "+ConstantesBD.codigoUnidadMedidaFechaMeses+") OR " +
				"(gecd.rango_inicial<= "+edadDias+" and gecd.rango_final>= "+edadDias+" and gecd.unidad_medida = "+ConstantesBD.codigoUnidadMedidaFechaDias+")" +
				") and (gecd.sexo="+campos.get("sexo")+" OR gecd.sexo IS NULL) ";
			
			//Filtro para el permiso de las acciones-------------------------------------------------
			consultaConvenio += " and appvi.via_ingreso = "+campos.get("viaIngreso")+" and appvi.ocupacion = "+campos.get("codigoOcupacion");
			
			//Filtro que las actividades no hagan parte de actividades existentes
			consultaConvenio += " and "+ 
				"app.programa || '-' || app.institucion || '-' || app.actividad NOT IN "+
				" (SELECT ppp.programa || '-' || ppp.institucion || '-' || appp.actividad " +
					"FROM programas_pyp_paciente ppp " +
					"INNER JOIN act_prog_pyp_pac appp ON(appp.codigo_prog_pyp_pac=ppp.codigo) " +
					"WHERE ppp.paciente = " + campos.get("codigoPaciente") + " AND ppp.codigo = "+campos.get("consecutivoPrograma")+
				") ";
			
			/** SE ARMA LA CONSULTA DE ACTIVIDADES POR PROGRAMA SIN CONVENIO **/
			String consultaSinConvenio = consultarActividadesProgramaSinConvenioStr + " WHERE " ;
			//Se crea el filtro principal-----------------------
			consultaSinConvenio += " app.programa = '"+campos.get("codigoPrograma")+"' and app.institucion = "+campos.get("institucion")+" and " +
				"app.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" and app.requerido = '"+ValoresPorDefecto.getValorTrueParaConsultas()+"' ";
			
			//Filtro por los diagnósticos ----------------------------------------------------------------------
			diagnosticos = campos.get("diagnosticos").toString();
			if(!diagnosticos.equals(""))
				consultaSinConvenio += " and (" +
					"(SELECT count(1) FROM diag_act_prog_pyp WHERE codigo=app.codigo AND acronimo || '-' || tipo_cie IN ("+diagnosticos+")) > 0 OR" +
					"(SELECT count(1) FROM diag_act_prog_pyp WHERE codigo=app.codigo) = 0 " +
					" )";
			else
				//si no hay diagnosticos se toman las actividades que tienen nulos
				consultaSinConvenio +=  " and (SELECT count(1) FROM diag_act_prog_pyp WHERE codigo=app.codigo) = 0 ";
			
			//Filtro embarazo --------------------------------------------------------------------------------
			if(UtilidadTexto.getBoolean(campos.get("esEmbarazo").toString()))
				consultaSinConvenio += " and ( app.embarazo = '"+ValoresPorDefecto.getValorTrueParaConsultas()+"' OR app.embarazo = '"+ValoresPorDefecto.getValorFalseParaConsultas()+"' OR app.embarazo IS NULL ) ";
			else
				consultaSinConvenio += " and ( app.embarazo = '"+ValoresPorDefecto.getValorFalseParaConsultas()+"' OR app.embarazo IS NULL ) ";
			
			//Filtro Grupo Etareo --------------------------------------------------------------------------
			edadAnios = campos.get("edadAnio").toString();
			edadMeses = campos.get("edadMeses").toString();
			edadDias = campos.get("edadDias").toString();
			
			consultaSinConvenio += " and appge.regimen IN ("+campos.get("tipoRegimen")+") and ("+
				"(gecd.rango_inicial<= "+edadAnios+" and gecd.rango_final>= "+edadAnios+" and gecd.unidad_medida = "+ConstantesBD.codigoUnidadMedidaFechaAnios+") OR " +
				"(gecd.rango_inicial<= "+edadMeses+" and gecd.rango_final>= "+edadMeses+" and gecd.unidad_medida = "+ConstantesBD.codigoUnidadMedidaFechaMeses+") OR " +
				"(gecd.rango_inicial<= "+edadDias+" and gecd.rango_final>= "+edadDias+" and gecd.unidad_medida = "+ConstantesBD.codigoUnidadMedidaFechaDias+")" +
				") and (gecd.sexo="+campos.get("sexo")+" OR gecd.sexo IS NULL) ";
			
			//Filtro para el permiso de las acciones-------------------------------------------------
			consultaSinConvenio += " and appvi.via_ingreso = "+campos.get("viaIngreso")+" and appvi.ocupacion = "+campos.get("codigoOcupacion");
			
			//Filtro que las actividades no hagan parte de actividades existentes
			consultaSinConvenio += " and "+ 
				"app.programa || '-' || app.institucion || '-' || app.actividad NOT IN "+
				" (SELECT ppp.programa || '-' || ppp.institucion || '-' || appp.actividad " +
					"FROM programas_pyp_paciente ppp " +
					"INNER JOIN act_prog_pyp_pac appp ON(appp.codigo_prog_pyp_pac=ppp.codigo) " +
					"WHERE ppp.paciente = " + campos.get("codigoPaciente") +" AND ppp.codigo = "+campos.get("consecutivoPrograma")+
				") ";
			
			//Filtro que las actividades no hagan parte de las actividades por convenio
			consultaSinConvenio += " and app.codigo NOT IN (SELECT actividad_programa_pyp FROM prog_act_pyp_convenio WHERE convenio IN ("+campos.get("listadoConvenios")+") and activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+") ";
			
			consulta = "("+consultaConvenio+") UNION ("+consultaSinConvenio+")";
			
			st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			logger.info("consulta de las actividades=> "+consulta);
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)),true,false);
			return mapa;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarActividadesNuevas de SqlBaseProgramasPYPDao: "+e);
			mapa.put("numRegistros","0");
			return mapa;
		}
	}

	/**
	 * Método que consulta las actividades existentes de un paciente y filtra las repetidas
	 * con el fin también de definir los permisis del campo accion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static HashMap consultarActividadesExistentes(Connection con, HashMap campos) 
	{
		HashMap mapaExis = new HashMap();
		try
		{
			/** SE REALIZA LA CONSULTA DE LAS ACTIVIDADES EXISTENTES DEL PACIENTE EN PYP **/
			String consecutivoAct = "";
			String consecutivo = "";
			String numeroSolicitud = "";
			String numeroOrden = "";
			String estadoAct = "";
			int codigoActividadPrograma=0;
			int viaIngreso=Utilidades.convertirAEntero(campos.get("viaIngreso")+""); 
			int ocupacion=Utilidades.convertirAEntero(campos.get("codigoOcupacion")+"");
		
			int numS = 0, numP = 0,numE = 0, numReg = 0;
			int cont = 0;
			
			String consulta = consultarActividadesProgramaExistentesStr + " WHERE " +
				" ppp.codigo = "+campos.get("consecutivoPrograma")+" " +
				"GROUP BY appp.codigo, app.codigo, app.actividad, appp.numero_solicitud,ap.servicio, tfp.descripcion,appge.tipo_frecuencia,app.embarazo, app.semanas_gestacion, app.archivo, app.institucion, appp.frecuencia, appp.numero_orden,appp.estado " +
				"ORDER BY consecutivo_actividad, consecutivo desc";
			
			PreparedStatementDecorator st = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery());
			
			logger.info("valor de consulta de actividades >> "+consulta);
			//Se recorren los resultados por que no se pueden postular actividades repetidas
			//también se recorre para validar los permisos de cada accion en actividades existentes
			while(rs.next())
			{
				//registro diferente
				codigoActividadPrograma=rs.getInt("codigo_act_pro");
				if(!consecutivoAct.equals(rs.getString("consecutivo_actividad"))) {

					mapaExis.put("consecutivo_"+cont,rs.getString("consecutivo"));
					mapaExis.put("consecutivo_actividad_"+cont,rs.getString("consecutivo_actividad"));
					mapaExis.put("tipo_actividad_"+cont,rs.getString("tipo_actividad"));
					mapaExis.put("codigo_actividad_"+cont,rs.getString("codigo_actividad"));
					mapaExis.put("nombre_actividad_"+cont,rs.getString("nombre_actividad"));
					mapaExis.put("semanas_gestacion_"+cont,rs.getString("semanas_gestacion"));
					mapaExis.put("archivo_"+cont,rs.getString("archivo"));
					mapaExis.put("institucion_"+cont,rs.getString("institucion"));
					mapaExis.put("frecuencia_"+cont,rs.getString("frecuencia"));
					mapaExis.put("codigo_estado_actividad_"+cont,rs.getString("codigo_estado_actividad"));
					mapaExis.put("numero_solicitud_"+cont,rs.getString("numero_solicitud"));
					mapaExis.put("numero_orden_"+cont,rs.getString("numero_orden"));
					mapaExis.put("existe_"+cont,"true");
					mapaExis.put("desc_frecuencia_"+cont,rs.getString("desc_frecuencia"));
					mapaExis.put("tipofrecuencia_"+cont,rs.getString("tipofrecuencia"));
					cont ++;

					estadoAct = rs.getString("codigo_estado_actividad");

					//segun el estado del registro se aumenta contador
					if(estadoAct.equals(ConstantesBD.codigoEstadoProgramaPYPSolicitado))
					{
						numS ++;
						consecutivo = rs.getString("consecutivo");
						numeroSolicitud = rs.getString("numero_solicitud"); 
						numeroOrden = rs.getString("numero_orden");
					}
					else if(estadoAct.equals(ConstantesBD.codigoEstadoProgramaPYPProgramado))
					{
						numP ++;
						consecutivo = rs.getString("consecutivo");
						numeroSolicitud = rs.getString("numero_solicitud");
						numeroOrden = rs.getString("numero_orden");
					}
					else if(estadoAct.equals(ConstantesBD.codigoEstadoProgramaPYPEjecutado)||
							estadoAct.equals(ConstantesBD.codigoEstadoProgramaPYPCancelado))
						numE ++;
					numReg ++;

					if(!consecutivoAct.equals(rs.getString("consecutivo_actividad"))) {
						verificarPermisos(con, mapaExis, codigoActividadPrograma, numS, numP, numE, numReg, viaIngreso, ocupacion, cont, consecutivo, numeroSolicitud, numeroOrden);
						consecutivoAct = rs.getString("consecutivo_actividad");
						//se reinician valores
						numReg = 0;
						numS = 0;
						numP = 0;
						numE = 0;
					}
				}
			}
			rs.close();
			st.close();
			mapaExis.put("numRegistros",cont);
			
			return mapaExis;
		}
		catch(SQLException e)
		{
			logger.error("Error al consultarActividadesExistentes en SqlBaseProgramaPYPDao: "+e);
			mapaExis.put("numRegistros","0");
			return mapaExis;
		}
	}
	
	private static void verificarPermisos(Connection con, HashMap mapaExis,
			int codigoActividadPrograma, int numS, int numP, int numE,
			int numReg, int viaIngreso, int ocupacion, int cont, String consecutivo, String numeroSolicitud, String numeroOrden) {

		String consultaActividadesViaIngresoOcupacion=
				"SELECT " +
						"solicitar, " +
						"programar, " +
						"ejecutar " +
					"FROM " +
						"acti_prog_pyp_vias_ing " +
					"WHERE " +
						"codigo_act_prog_pyp=? " +
						"AND via_ingreso=? " +
						"AND ocupacion=?";
		String valorTrue = ValoresPorDefecto.getValorTrueParaConsultas();
		String valorFalse = ValoresPorDefecto.getValorFalseParaConsultas();

		try {
			PreparedStatementDecorator psd= new PreparedStatementDecorator(con.prepareStatement(consultaActividadesViaIngresoOcupacion));
			psd.setInt(1, codigoActividadPrograma);
			psd.setInt(2, viaIngreso);
			psd.setInt(3, ocupacion);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			
			
			boolean puedeSolicitar=false;
			boolean puedeProgramar=false;
			boolean puedeEjecutar=false;
			if(rsd.next())
			{
				puedeSolicitar=rsd.getBoolean("solicitar");
				puedeProgramar=rsd.getBoolean("programar");
				puedeEjecutar=rsd.getBoolean("ejecutar");
				
				
				logger.info("puedeEjecutar "+rsd.getBoolean("ejecutar"));
				//segun el tipo de estado
				if(numS>0) //existió estado Solicitar
				{
					logger.info("1");
					//solo se permite accion ejecutar
					mapaExis.put("solicitar_"+(cont-1),valorFalse);
					mapaExis.put("programar_"+(cont-1),valorFalse);
					mapaExis.put("ejecutar_"+(cont-1),valorTrue);
					mapaExis.put("cancelar_"+(cont-1),valorFalse);
					mapaExis.put("codigo_estado_actividad_"+(cont-1),ConstantesBD.codigoEstadoProgramaPYPSolicitado);
					mapaExis.put("consecutivo_"+(cont-1),consecutivo);
					mapaExis.put("numero_solicitud_"+(cont-1),numeroSolicitud);
					mapaExis.put("numero_orden_"+(cont-1),numeroOrden);
				}
				else if(numP>0) //existió estado Programar
				{
					logger.info("2");
					//se permite solicitar y ejecutar
					mapaExis.put("solicitar_"+(cont-1),valorTrue);
					mapaExis.put("programar_"+(cont-1),valorFalse);
					mapaExis.put("ejecutar_"+(cont-1),valorTrue);
					mapaExis.put("cancelar_"+(cont-1),valorTrue);
					mapaExis.put("codigo_estado_actividad_"+(cont-1),ConstantesBD.codigoEstadoProgramaPYPProgramado);
					mapaExis.put("consecutivo_"+(cont-1),consecutivo);
					mapaExis.put("numero_solicitud_"+(cont-1),numeroSolicitud);
					mapaExis.put("numero_orden_"+(cont-1),numeroOrden);
				}
				else if(numReg==numE) //si todas estan ejecutadas
				{
					logger.info("3");
					//se permite hacer todo
					mapaExis.put("solicitar_"+(cont-1),valorTrue);
					mapaExis.put("programar_"+(cont-1),valorTrue);
					mapaExis.put("ejecutar_"+(cont-1),valorTrue);
					mapaExis.put("cancelar_"+(cont-1),valorFalse);
				}
				if(!puedeSolicitar)
				{
					mapaExis.put("solicitar_"+(cont-1),valorFalse);
				}
				if(!puedeProgramar)
				{
					mapaExis.put("programar_"+(cont-1),valorFalse);
				}
				if(!puedeEjecutar)
				{
					mapaExis.put("ejecutar_"+(cont-1),valorFalse);
				}
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * Método que consulta los articulos x programa de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarArticulosProgramaPaciente(Connection con,HashMap campos)
	{
		HashMap mapaExis = consultarArticulosExistentes(con,campos);
		
		//se inician mapas --------------------------
		HashMap mapa = new HashMap();
		mapa.put("numRegistros","0");
		
		if(UtilidadTexto.getBoolean(campos.get("porConvenio").toString()))
			mapa = consultarArticulosNuevos(con,campos);
		
		//se verifica si mapa está lleno
		if(Utilidades.convertirAEntero(mapa.get("numRegistros").toString())>0)
		{
			//Se asigna el mapa a mapaExis
			int pos = Utilidades.convertirAEntero(mapaExis.get("numRegistros").toString());
			
			for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros").toString());i++)
			{
				mapaExis.put("consecutivo_"+pos,mapa.get("consecutivo_"+i));
				mapaExis.put("consecutivo_actividad_"+pos,mapa.get("consecutivo_actividad_"+i));
				mapaExis.put("tipo_actividad_"+pos,mapa.get("tipo_actividad_"+i));
				mapaExis.put("codigo_actividad_"+pos,mapa.get("codigo_actividad_"+i));
				mapaExis.put("nombre_actividad_"+pos,mapa.get("nombre_actividad_"+i));
				mapaExis.put("institucion_"+pos,mapa.get("institucion_"+i));
				mapaExis.put("codigo_estado_actividad_"+pos,mapa.get("codigo_estado_actividad_"+i));
				mapaExis.put("numero_solicitud_"+pos,mapa.get("numero_solicitud_"+i));
				mapaExis.put("numero_orden_"+pos,mapa.get("numero_orden_"+i));
				mapaExis.put("frecuencia_"+pos,mapa.get("frecuencia_"+i));
				mapaExis.put("solicitar_"+pos,mapa.get("solicitar_"+i));
				mapaExis.put("programar_"+pos,mapa.get("programar_"+i));
				mapaExis.put("ejecutar_"+pos,mapa.get("ejecutar_"+i));
				mapaExis.put("cancelar_"+pos,mapa.get("cancelar_"+i));
				mapaExis.put("existe_"+pos,mapa.get("existe_"+i));
				pos++;
			}
			
			mapaExis.put("numRegistros",pos);
		}
			
		return mapaExis;
	}

	/**
	 * Método implementado para consultar los articulos x programa que sirven
	 * para el paciente como registros NUEVOS
	 * @param con
	 * @param campos
	 * @return
	 */
	private static HashMap consultarArticulosNuevos(Connection con, HashMap campos) 
	{
		HashMap mapa = new HashMap();
		try
		{
			//Se toman variables necesarias
			String edadAnios = campos.get("edadAnio").toString();
			String edadMeses = campos.get("edadMeses").toString();
			String edadDias = campos.get("edadDias").toString();
			
			String consultar = consultarArticulosProgramaNuevosStr + " WHERE "+
				"aapp.programa ='"+campos.get("codigoPrograma")+"' and aapp.institucion = "+campos.get("institucion")+" and " +
				"aapp.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" and " + 
				"aapp.programa || '-' || aapp.institucion || '-' || ap.consecutivo NOT IN " +
				"(" +
					"SELECT ppp.programa || '-' || ppp.institucion || '-' || appp.actividad " +
					"FROM programas_pyp_paciente ppp " +
					"INNER JOIN act_prog_pyp_pac appp ON(appp.codigo_prog_pyp_pac=ppp.codigo) " +
					"WHERE ppp.paciente = " +campos.get("codigoPaciente")+" AND ppp.codigo = "+campos.get("consecutivoPrograma")+
				") "+
				//*****SE EFECTUAN VALIDACIONES DEL GRUPO ETAREO*******************************
				" and appge.regimen IN ("+campos.get("tipoRegimen")+") and ("+
				"(gecd.rango_inicial<= "+edadAnios+" and gecd.rango_final>= "+edadAnios+" and gecd.unidad_medida = "+ConstantesBD.codigoUnidadMedidaFechaAnios+") OR " +
				"(gecd.rango_inicial<= "+edadMeses+" and gecd.rango_final>= "+edadMeses+" and gecd.unidad_medida = "+ConstantesBD.codigoUnidadMedidaFechaMeses+") OR " +
				"(gecd.rango_inicial<= "+edadDias+" and gecd.rango_final>= "+edadDias+" and gecd.unidad_medida = "+ConstantesBD.codigoUnidadMedidaFechaDias+")" +
				") and (gecd.sexo="+campos.get("sexo")+" OR gecd.sexo IS NULL) "+
				//*********************************************************************************
				//****SE EFECTUAN VALIDACIONES DE LA VIA DE INGRESO*******************************
				" and appvi.via_ingreso = "+campos.get("viaIngreso")+" and appvi.ocupacion = "+campos.get("codigoOcupacion");
				//********************************************************************************
				
			
			logger.info("SQL / consultarArticulosNuevos / "+consultar);
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consultar)),true,false);
			
			return mapa;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarArticulosNuevos de SQlBaseProgramasPYPDAo: "+e);
			mapa.put("numRegistros","0");
			return mapa;
		}
		
	}
	
	/**
	 * Método que consulta los articulos x programa existentes del paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	private static HashMap consultarArticulosExistentes(Connection con, HashMap campos) 
	{
		HashMap mapaExis = new HashMap();
		try
		{
			/** SE REALIZA LA CONSULTA DE LOS ARTICULOS EXISTENTES DEL PACIENTE EN PYP **/
			String consecutivoAct = "";
			String consecutivo = "";
			String numeroSolicitud = "";
			String numeroOrden = "";
			String estadoAct = "";
			String valorTrue = ValoresPorDefecto.getValorTrueParaConsultas();
			String valorFalse = ValoresPorDefecto.getValorFalseParaConsultas();
		
			int numS = 0, numP = 0,numE = 0, numReg = 0;
			int cont = 0;
			
			String consulta = consultarArticulosProgramaExistentesStr + " WHERE " +
				" ppp.codigo = "+campos.get("consecutivoPrograma")+" ORDER BY consecutivo_actividad ";
			
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			//Se recorren los resultados por que no se pueden postular actividades repetidas
			//también se recorre para validar los permisos de cada accion en actividades existentes
			while(rs.next())
			{
				//registro diferente
				if(!consecutivoAct.equals(rs.getString("consecutivo_actividad")))
				{
					//se verifica si habían registros de la misma actividad repetidos
					if(numReg>0)
					{
						
						//segun el tipo de estado
						if(numS>0) //existió estado Solicitar
						{
							//solo se permite accion ejecutar
							mapaExis.put("solicitar_"+(cont-1),valorFalse);
							mapaExis.put("programar_"+(cont-1),valorFalse);
							mapaExis.put("ejecutar_"+(cont-1),valorFalse);
							mapaExis.put("cancelar_"+(cont-1),valorFalse);
							mapaExis.put("codigo_estado_actividad_"+(cont-1),ConstantesBD.codigoEstadoProgramaPYPSolicitado);
							mapaExis.put("consecutivo_"+(cont-1),consecutivo);
							mapaExis.put("numero_solicitud_"+(cont-1),numeroSolicitud);
							mapaExis.put("numero_orden_"+(cont-1),numeroOrden);
						}
						else if(numP>0) //existió estado Programar
						{
							//se permite solicitar y ejecutar
							mapaExis.put("solicitar_"+(cont-1),valorTrue);
							mapaExis.put("programar_"+(cont-1),valorFalse);
							mapaExis.put("ejecutar_"+(cont-1),valorFalse);
							mapaExis.put("cancelar_"+(cont-1),valorTrue);
							mapaExis.put("codigo_estado_actividad_"+(cont-1),ConstantesBD.codigoEstadoProgramaPYPProgramado);
							mapaExis.put("consecutivo_"+(cont-1),consecutivo);
							mapaExis.put("numero_solicitud_"+(cont-1),numeroSolicitud);
							mapaExis.put("numero_orden_"+(cont-1),numeroOrden);
						}
						else if(numReg==numE) //si todas estan ejecutadas
						{
							//se permite hacer todo
							mapaExis.put("solicitar_"+(cont-1),valorTrue);
							mapaExis.put("programar_"+(cont-1),valorTrue);
							mapaExis.put("ejecutar_"+(cont-1),valorFalse);
							mapaExis.put("cancelar_"+(cont-1),valorFalse);
						}
						
						//se reinician valores
						numReg = 0;
						numS = 0;
						numP = 0;
						numE = 0;
					}
					
					consecutivoAct = rs.getString("consecutivo_actividad");
					mapaExis.put("consecutivo_"+cont,rs.getString("consecutivo"));
					mapaExis.put("consecutivo_actividad_"+cont,consecutivoAct);
					mapaExis.put("tipo_actividad_"+cont,rs.getString("tipo_actividad"));
					mapaExis.put("codigo_actividad_"+cont,rs.getString("codigo_actividad"));
					mapaExis.put("nombre_actividad_"+cont,rs.getString("nombre_actividad"));
					mapaExis.put("institucion_"+cont,rs.getString("institucion"));
					mapaExis.put("frecuencia_"+cont,rs.getString("frecuencia"));
					mapaExis.put("codigo_estado_actividad_"+cont,rs.getString("codigo_estado_actividad"));
					mapaExis.put("numero_solicitud_"+cont,rs.getString("numero_solicitud"));
					mapaExis.put("numero_orden_"+cont,rs.getString("numero_orden"));
					mapaExis.put("existe_"+cont,"true");
					
					cont ++;
				}
				
				estadoAct = rs.getString("codigo_estado_actividad");
				
				//segun el estado del registro se aumenta contador
				if(estadoAct.equals(ConstantesBD.codigoEstadoProgramaPYPSolicitado))
				{
					numS ++;
					consecutivo = rs.getString("consecutivo");
					numeroSolicitud = rs.getString("numero_solicitud");
					numeroOrden = rs.getString("numero_orden");
				}
				else if(estadoAct.equals(ConstantesBD.codigoEstadoProgramaPYPProgramado))
				{
					numP ++;
					consecutivo = rs.getString("consecutivo");
					numeroSolicitud = rs.getString("numero_solicitud");
					numeroOrden = rs.getString("numero_orden");
				}
				else if(estadoAct.equals(ConstantesBD.codigoEstadoProgramaPYPEjecutado)||
						estadoAct.equals(ConstantesBD.codigoEstadoProgramaPYPCancelado))
					numE ++;
				numReg ++;
				
			}
			
			//se verifica ultimo registro
			if(numReg>0)
			{
				
				//segun el tipo de estado
				if(numS>0) //existió estado Solicitar
				{
					//solo se permite accion ejecutar
					mapaExis.put("solicitar_"+(cont-1),valorFalse);
					mapaExis.put("programar_"+(cont-1),valorFalse);
					mapaExis.put("ejecutar_"+(cont-1),valorFalse);
					mapaExis.put("cancelar_"+(cont-1),valorFalse);
					mapaExis.put("codigo_estado_actividad_"+(cont-1),ConstantesBD.codigoEstadoProgramaPYPSolicitado);
					mapaExis.put("consecutivo_"+(cont-1),consecutivo);
					mapaExis.put("numero_solicitud_"+(cont-1),numeroSolicitud);
					mapaExis.put("numero_orden_"+(cont-1),numeroOrden);
				}
				else if(numP>0) //existió estado Programar
				{
					//se permite solicitar y ejecutar
					mapaExis.put("solicitar_"+(cont-1),valorTrue);
					mapaExis.put("programar_"+(cont-1),valorFalse);
					mapaExis.put("ejecutar_"+(cont-1),valorFalse);
					mapaExis.put("cancelar_"+(cont-1),valorTrue);
					mapaExis.put("codigo_estado_actividad_"+(cont-1),ConstantesBD.codigoEstadoProgramaPYPProgramado);
					mapaExis.put("consecutivo_"+(cont-1),consecutivo);
					mapaExis.put("numero_solicitud_"+(cont-1),numeroSolicitud);
					mapaExis.put("numero_orden_"+(cont-1),numeroOrden);
				}
				else if(numReg==numE) //si todas estan ejecutadas
				{
					//se permite hacer todo
					mapaExis.put("solicitar_"+(cont-1),valorTrue);
					mapaExis.put("programar_"+(cont-1),valorTrue);
					mapaExis.put("ejecutar_"+(cont-1),valorFalse);
					mapaExis.put("cancelar_"+(cont-1),valorFalse);
				}
				
				//se reinician valores
				numReg = 0;
				numS = 0;
				numP = 0;
				numE = 0;
			}
			
			mapaExis.put("numRegistros",cont);
			
			return mapaExis;
		}
		catch(SQLException e)
		{
			logger.error("Error al consultarArticulosExistentes en SqlBaseProgramaPYPDao: "+e);
			mapaExis.put("numRegistros","0");
			return mapaExis;
		}
	}
	
	/**
	 * Método implementado para insertar una actividad al paciente PYP
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarActividad(Connection con,HashMap campos)
	{
		try
		{
			String consulta = insertarActividadStr + " VALUES ("+campos.get("secuencia")+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO " +
					"act_prog_pyp_pac " +
					"(codigo,codigo_prog_pyp_pac,actividad,estado,numero_orden,numero_solicitud," +
					"fecha_programar,hora_programar,fecha_solicitar,hora_solicitar,fecha_ejecutar,hora_ejecutar,fecha_cancelar,hora_cancelar," +
					"usuario_solicitar,usuario_programar,usuario_ejecutar,usuario_cancelar,centro_atencion,frecuencia)
			 */
			
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("consecutivoPrograma")+""));
			pst.setDouble(2,Utilidades.convertirADouble(campos.get("consecutivoActividad")+""));
			pst.setDouble(3,Utilidades.convertirADouble(campos.get("estadoPrograma")+""));
			
			if(campos.get("numeroOrden")==null)
				pst.setNull(4,Types.NUMERIC);
			else
				pst.setDouble(4,Utilidades.convertirADouble(campos.get("numeroOrden")+""));
			if(campos.get("numeroSolicitud")==null)
				pst.setNull(5,Types.INTEGER);
			else
				pst.setInt(5,Utilidades.convertirAEntero(campos.get("numeroSolicitud")+""));
			

			if(campos.get("fechaProgramar")==null)
				pst.setNull(6,Types.DATE);
			else
				pst.setDate(6,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaProgramar").toString())));
			
			if(campos.get("horaProgramar")==null)
				pst.setNull(7,Types.CHAR);
			else
				pst.setString(7,campos.get("horaProgramar").toString());
			
			if(campos.get("fechaSolicitar")==null)
				pst.setNull(8,Types.DATE);
			else
				pst.setDate(8,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaSolicitar").toString())));
			
			if(campos.get("horaSolicitar")==null)
				pst.setNull(9,Types.CHAR);
			else
				pst.setString(9,campos.get("horaSolicitar").toString());
			
			if(campos.get("fechaEjecutar")==null)
				pst.setNull(10,Types.DATE);
			else
				pst.setDate(10,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaEjecutar").toString())));
			
			if(campos.get("horaEjecutar")==null)
				pst.setNull(11,Types.CHAR);
			else
				pst.setString(11,campos.get("horaEjecutar").toString());
			
			if(campos.get("fechaCancelar")==null)
				pst.setNull(12,Types.DATE);
			else
				pst.setDate(12,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaCancelar").toString())));
			
			if(campos.get("horaCancelar")==null)
				pst.setNull(13,Types.CHAR);
			else
				pst.setString(13,campos.get("horaCancelar").toString());
			
			
			if(campos.get("usuarioSolicitar")==null)
				pst.setNull(14,Types.VARCHAR);
			else
				pst.setString(14,campos.get("usuarioSolicitar")+"");
			
			if(campos.get("usuarioProgramar")==null)
				pst.setNull(15,Types.VARCHAR);
			else
				pst.setString(15,campos.get("usuarioProgramar")+"");
			
			if(campos.get("usuarioEjecutar")==null)
				pst.setNull(16,Types.VARCHAR);
			else
				pst.setString(16,campos.get("usuarioEjecutar")+"");
			
			if(campos.get("usuarioCancelar")==null)
				pst.setNull(17,Types.VARCHAR);
			else
				pst.setString(17,campos.get("usuarioCancelar")+"");
			
			pst.setInt(18,Utilidades.convertirAEntero(campos.get("centroAtencion")+""));
			if(campos.get("frecuencia")==null)
				pst.setNull(19,Types.NUMERIC);
			else
				pst.setDouble(19,Utilidades.convertirADouble(campos.get("frecuencia")+""));
			
			
			
			int resp = pst.executeUpdate();
			
			if(resp>0)
				resp = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con,"seq_act_prog_pyp_pac");
			
			return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarActividad de SqlBaseProgramasPYPDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para modificar la actividad pyp de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int modificarActividad(Connection con,HashMap campos)
	{
		try
		{
			String consulta = "UPDATE act_prog_pyp_pac SET ";
			boolean hayCond = false;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			if(campos.get("estadoPrograma")!=null)
			{
				consulta += " estado = "+campos.get("estadoPrograma");
				hayCond = true;
			}
			
			if(campos.get("numeroOrden")!=null)
			{
				if(hayCond)
					consulta += ",";
				consulta += " numero_orden = "+campos.get("numeroOrden");
				hayCond = true;
			}
			
			if(campos.get("numeroSolicitud")!=null)
			{
				if(hayCond)
					consulta += ",";
				consulta += " numero_solicitud = "+campos.get("numeroSolicitud");
				hayCond = true;
			}
			
			if(campos.get("fechaProgramar")!=null)
			{
				if(hayCond)
					consulta += ",";
				consulta += " fecha_programar = '"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaProgramar").toString())+"'";
				hayCond = true;
			}
			
			if(campos.get("horaProgramar")!=null)
			{
				if(hayCond)
					consulta += ",";
				consulta += " hora_programar = '"+campos.get("horaProgramar")+"'";
				hayCond = true;
			}
				
			if(campos.get("fechaSolicitar")!=null)
			{
				if(hayCond)
					consulta += ",";
				consulta += " fecha_solicitar = '"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaSolicitar").toString())+"'";
				hayCond = true;
			}
			
			if(campos.get("horaSolicitar")!=null)
			{
				if(hayCond)
					consulta += ",";
				consulta += " hora_solicitar = '"+campos.get("horaSolicitar")+"'";
				hayCond = true;
			}
			
			if(campos.get("fechaEjecutar")!=null)
			{
				if(hayCond)
					consulta += ",";
				consulta += " fecha_ejecutar = '"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaEjecutar").toString())+"'";
				hayCond = true;
			}
			
			if(campos.get("horaEjecutar")!=null)
			{
				if(hayCond)
					consulta += ",";
				consulta += " hora_ejecutar = '"+campos.get("horaEjecutar")+"'";
				hayCond = true;
			}
			
			if(campos.get("fechaCancelar")!=null)
			{
				if(hayCond)
					consulta += ",";
				consulta += " fecha_cancelar = '"+UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaCancelar").toString())+"'";
				hayCond = true;
			}
			
			if(campos.get("horaCancelar")!=null)
			{
				if(hayCond)
					consulta += ",";
				consulta += " hora_cancelar = '"+campos.get("horaCancelar")+"'";
				hayCond = true;
			}
			
			if(campos.get("usuarioSolicitar")!=null)
			{
				if(hayCond)
					consulta += ",";
				consulta += " usuario_solicitar = '"+campos.get("usuarioSolicitar")+"'";
				hayCond = true;
			}
			
			if(campos.get("usuarioProgramar")!=null)
			{
				if(hayCond)
					consulta += ",";
				consulta += " usuario_programar = '"+campos.get("usuarioProgramar")+"'";
				hayCond = true;
			}
			
			if(campos.get("usuarioEjecutar")!=null)
			{
				if(hayCond)
					consulta += ",";
				consulta += " usuario_ejecutar = '"+campos.get("usuarioEjecutar")+"'";
				hayCond = true;
			}
			
			if(campos.get("usuarioCancelar")!=null)
			{
				if(hayCond)
					consulta += ",";
				consulta += " usuario_cancelar = '"+campos.get("usuarioCancelar")+"'";
				hayCond = true;
			}
			
			if(campos.get("motivoCancelacion")!=null)
			{
				if(hayCond)
					consulta += ",";
				consulta += " motivo_cancelacion = '"+campos.get("motivoCancelacion")+"'";
				hayCond = true;
			}
			
			consulta += " WHERE codigo = " + campos.get("consecutivoActividadPYP");
			
			logger.info("\n\n\nMODIFICAR ACTIVIDAD / "+consulta);
			
			return st.executeUpdate(consulta);
		}
		catch(SQLException e)
		{
			logger.error("Error en modificarActividad de SQlBaseProgramasPYPDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método implementado para consultar los centros de atención de una actividad
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String consultarCentrosAtencionActividad(Connection con,HashMap campos)
	{
		try
		{
			String resultado = "";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarCentrosAtencionActividadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("consecutivoActividad")+""));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("institucion")+""));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				if(!resultado.equals(""))
					resultado += ",";
				
				resultado += rs.getString("centro_atencion");	
			}
			
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCentrosAtencionActividad de SQlBaseProgramasPYPDao: "+e);
			return "";
		}
	}
	
	/**
	 * Método implementado para consultar el histórico de una  actividad
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarHistoricosActividad(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarHistoricosActividadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("consecutivoProgramaPYP")+""));
			pst.setDouble(2,Utilidades.convertirADouble(campos.get("consecutivoActividad")+""));
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarHistoricosActividad de SqlBaseProgramasPYPDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método que inserta una actividad acumulada
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarActividadAcumulada(Connection con,HashMap campos)
	{
		try
		{
			String insertar = insertarActividadAcumuladaStr + " VALUES ("+campos.get("secuencia")+",?,?,?,?,?,CURRENT_DATE,1,?)";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO acumulado_act_prog_pac_pyp " +
				"(codigo,centro_atencion,regimen,programa,actividad,convenio,fecha,cantidad,institucion)
			 */
			
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("centroAtencion")+""));
			pst.setString(2,campos.get("tipoRegimen")+"");
			pst.setString(3,campos.get("codigoPrograma")+"");
			pst.setDouble(4,Utilidades.convertirADouble(campos.get("consecutivoActividad")+""));
			pst.setInt(5,Utilidades.convertirAEntero(campos.get("codigoConvenio")+""));
			pst.setInt(6,Utilidades.convertirAEntero(campos.get("institucion")+""));
			
			logger.info("\n\n\nINSERTAR ACTIVIDAD!!!!!!!");
			
			return pst.executeUpdate();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarActividadAcumulada de SqlBaseProgramasPYPDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método que retorna el consecutivo de una actividad acumulada existente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String consultarConsecutivoActividadAcumulada(Connection con,HashMap campos)
	{
		try
		{
			String resultado = "";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarActividadAcumuladaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("centroAtencion")+""));
			pst.setString(2,campos.get("tipoRegimen")+"");
			pst.setString(3,campos.get("codigoPrograma")+"");
			pst.setDouble(4,Utilidades.convertirADouble(campos.get("consecutivoActividad")+""));
			pst.setInt(5,Utilidades.convertirAEntero(campos.get("codigoConvenio")+""));
			pst.setInt(6,Utilidades.convertirAEntero(campos.get("institucion")+""));
			pst.setDate(7,Date.valueOf(campos.get("fecha")+""));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				resultado = rs.getString("codigo");
			
			return resultado;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarConsecutivoActividadAcumulada de SqlBaseProgramasPYPDao: "+e);
			return "";
		}
	}
	
	/**
	 * Método que aumenta el acumulado de una actividad
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int aumentarAcumuladoActividad(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(aumentarAcumuladoActividadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setDouble(1,Utilidades.convertirADouble(campos.get("consecutivoAcumulado")+""));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en aumentarAcumuladoActividad de SqlBaseProgramasPYPDao: "+e);
			return 0;
		}
	}
	
	/**
	 * Método que verifica si una actividad ya fue ejecutada para la fecha actual
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean estaActividadEjecutada(Connection con,HashMap campos)
	{
		try
		{
			boolean existe = false;
			String consulta = estaActividadEjecutadaStr;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoPersona")+""));
			pst.setString(2,campos.get("codigoPrograma")+"");
			pst.setInt(3,Utilidades.convertirAEntero(campos.get("institucion")+""));
			pst.setDouble(4,Utilidades.convertirADouble(campos.get("codigoActividad")+""));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en estaActividadEjecutada de SqlBaseProgramasPYPDao: "+e);
			return false;
		}
	}
	
	/**
	 * Método que verifica si la actividad ya fue ejecutada para la fecha,
	 * y si la actividad permite ser ejecutada varias veces al día
	 *
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean permiteEjecutarActividad(Connection con,HashMap campos)
	{
		try
		{
			boolean existe = false;
			String consulta = permiteEjecutarActividadStr;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoPersona")+""));
			pst.setString(2,campos.get("codigoPrograma")+"");
			pst.setInt(3,Utilidades.convertirAEntero(campos.get("institucion")+""));
			pst.setDouble(4,Utilidades.convertirADouble(campos.get("codigoActividad")+""));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en permiteEjecutarActividad de SqlBaseProgramasPYPDao: "+e);
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Método implementado para consultar la finalidad de una actividad
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String consultarFinalidadActividad(Connection con,HashMap campos)
	{
		try
		{
			String consulta = "";
			String finalidad = "";
			if(campos.get("tipoServicio").toString().equals(ConstantesBD.codigoServicioInterconsulta+""))
				consulta = consultarFinalidadConsultaStr;
			else
				consulta = consultarFinalidadServicioStr;
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,campos.get("codigoPrograma")+"");
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("institucion")+""));
			pst.setDouble(3,Utilidades.convertirADouble(campos.get("consecutivoActividad")+""));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				finalidad = rs.getString("codigo_finalidad") + ConstantesBD.separadorSplit + rs.getString("nombre_finalidad");
			
			return finalidad;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarFinalidadActividad de SQlBaseProgramasPYPDao: "+e);
			return "";
		}
	}
	
	/**
	 * Método que consulta la finalidad de una consulta PYP
	 * basándose de lo parametrizado de actividades x programa
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static InfoDatos consultarFinalidadActividadConsulta(Connection con,String numeroSolicitud)
	{
		InfoDatos finalidad = new InfoDatos(0,"");
		try
		{
			String consulta = consultarFinalidadActividadConsultaStr;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				finalidad.setId(rs.getString("codigo_finalidad"));
				finalidad.setNombre(rs.getString("nombre_finalidad"));
			}
			return finalidad;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarFinalidadActividadConsulta de SqlBaseProgramasPYPDao: "+e);
			return finalidad;
		}
	}
	
	/**
	 * Método que consulta el consecutivo de un programa ya existente
	 * @param con
	 * @param codigoPaciente
	 * @param codigoPrograma
	 * @param institucion
	 * @return
	 */
	public static String consultarConsecutivoProgramaExistente(Connection con,String codigoPaciente,String codigoPrograma,String institucion)
	{
		try
		{
			String consecutivo = "";
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarConsecutivoProgramaExistenteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigoPaciente);
			pst.setString(2,codigoPrograma);
			pst.setInt(3,Utilidades.convertirAEntero(institucion));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				consecutivo = rs.getString("consecutivo");
			
			return consecutivo;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarConsecutivoProgramaExistente de SqlBaseProgramasPYPDao: "+e);
			return "";
		}
	}

	/**
	 * 
	 * @param con
	 * @param filtros
	 * @return
	 */
	public static ArrayList<DtoObservacionProgramaPYP> obtenerObservacionesProgramaPYP(
			Connection con, HashMap filtros) {
		ArrayList<DtoObservacionProgramaPYP> observaciones = new ArrayList<DtoObservacionProgramaPYP>();
		String sql = "SELECT " +
						"oppp.paciente, " +
						"oppp.programa, " +
						"oppp.observacion, " +
						"oppp.usuario, " +
						"oppp.institucion, " +
						"oppp.fecha_grabacion, " +
						"oppp.hora_grabacion," +
						"m.numero_registro " +
					"FROM " +
						"pyp.obser_prog_pyp_pac oppp " +
					"INNER JOIN " +
						"usuarios u ON (u.login=oppp.usuario) " +
					"INNER JOIN " +
						"medicos m ON (m.codigo_medico=u.codigo_persona) " +
					"WHERE " +
						"oppp.paciente="+filtros.get("paciente")+" " +
						"AND oppp.programa='"+filtros.get("programa")+"' " +
						"AND oppp.institucion="+filtros.get("institucion");
		logger.info("SQL / obtenerObservacionesProgramaPYP / "+sql);
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				DtoObservacionProgramaPYP observacion = new DtoObservacionProgramaPYP();
				observacion.setPaciente(rs.getInt("paciente"));
				observacion.setPrograma(rs.getInt("programa"));
				observacion.setObservacion(rs.getString("observacion"));
				observacion.setUsuario(rs.getString("usuario"));
				observacion.setInstitucion(rs.getInt("institucion"));
				observacion.setFechaGrabacion(rs.getString("fecha_grabacion"));
				observacion.setHoraGrabacion(rs.getString("hora_grabacion"));
				observacion.setRegistroMedico(rs.getString("numero_registro"));
				observaciones.add(observacion);
			}
		}
		catch(SQLException e)
		{
			logger.error("ERROR", e);
		}
		return observaciones;
	}

	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean guardarObservacioProgramaPYP(
			Connection con, DtoObservacionProgramaPYP dto) {
		boolean exito=false;
		String sql = "INSERT INTO " +
						"pyp.obser_prog_pyp_pac (paciente, programa, observacion, usuario, institucion, fecha_grabacion, hora_grabacion) " +
						"VALUES ("+dto.getPaciente()+", "+dto.getPrograma()+", '"+dto.getObservacion()+"', '"+dto.getUsuario()+"', "+dto.getInstitucion()+", CURRENT_DATE, '"+UtilidadFecha.getHoraActual()+"') ";
				
		logger.info("SQL / guardarObservacioProgramaPYP / "+sql);
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			if(UtilidadTexto.getBoolean(pst.executeUpdate()))
				exito=true;
		}
		catch(SQLException e)
		{
			logger.error("ERROR", e);
		}
		return exito;
	}
	
}
