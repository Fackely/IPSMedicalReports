/*
 * Mayo 15, 2007
 */
package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.UtilidadesBDDao;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;

/**
 * 
 *  @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad REFERENCIA
 *
 */
public class SqlBaseReferenciaDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseReferenciaDao.class);
	
	/**
	 * Cadena que consulta la informacion general de una referencia
	 */
	private static final String consultarInformacionGeneralStr = "SELECT "+ 
		"r.numero_referencia AS numero_referencia, "+
		"r.codigo_paciente AS codigo_paciente, "+
		"r.institucion AS codigo_institucion, "+
		"CASE WHEN r.ingreso IS NULL THEN '' ELSE r.ingreso || '' END AS id_ingreso, "+
		"r.referencia AS tipo_referencia, "+
		"r.institucion_sirc_solicita AS codigo_sirc, " +
		"i.descripcion AS institucion_sirc, "+
		"CASE WHEN r.anio_consecutivo IS NULL THEN '' ELSE r.anio_consecutivo END AS anio_consecutivo, "+
		"CASE WHEN r.consecutivo_punto_atencion IS NULL THEN '' ELSE  r.consecutivo_punto_atencion END AS consecutivo_punto_atencion, "+
		"r.codigo_departamento || '"+ConstantesBD.separadorSplit+"' || r.codigo_ciudad  As codigo_ciudad, "+
		"getnombreciudad(r.codigo_pais,r.codigo_departamento,r.codigo_ciudad) || ' (' || getnombredepto(r.codigo_pais,r.codigo_departamento) || ' (' || getdescripcionpais(r.codigo_pais) || ')' AS nombre_ciudad, "+
		"to_char(r.fecha_referencia,'"+ConstantesBD.formatoFechaAp+"') AS fecha_referencia, "+
		"r.hora_referencia AS hora_referencia, "+
		"r.profesional_salud AS profesional_salud, "+
		"CASE WHEN r.codigo_profesional IS NULL THEN '' ELSE r.codigo_profesional || '' END AS codigo_profesional, "+
		"CASE WHEN r.codigo_especialidad IS NULL THEN '' ELSE r.codigo_especialidad || '' END AS codigo_especialidad, "+
		"CASE WHEN r.codigo_especialidad IS NULL THEN '' ELSE getnombreespecialidad(r.codigo_especialidad) END AS nombre_especialidad, "+
		"r.registro_medico AS registro_medico, "+
		"r.tipo_usuario AS tipo_usuario, "+
		"r.tipo_referencia AS tipo_ref, "+
		"r.tipo_atencion AS tipo_atencion, "+
		"r.motivo_referencia AS codigo_motivo_referencia, "+
		"ms.descripcion AS descripcion_motivo_referencia, "+
		"r.convenio AS codigo_convenio, "+
		"c.nombre AS nombre_convenio, " +
		"c.tipo_regimen AS codigo_tipo_regimen, " +
		"getnomtiporegimen(c.tipo_regimen) AS nombre_tipo_regimen, "+
		"r.estrato_social AS codigo_estrato_social, "+
		"getnombreestrato(r.estrato_social) AS nombre_estrato_social, "+
		"r.anamnesis AS anamnesis, "+
		"r.antecedentes AS antecedentes, "+
		"CASE WHEN r.estado_conciencia IS NULL THEN '' ELSE r.estado_conciencia || '' END AS codigo_estado_conciencia, "+
		"CASE WHEN r.estado_conciencia IS NULL THEN '' ELSE getnombreestadoconciencia(r.estado_conciencia) END AS nombre_estado_conciencia, "+
		"r.examen_fisico AS examen_fisico, "+
		"r.paciente_con_oxigeno AS paciente_con_oxigeno, "+
		"CASE WHEN r.saturacion_oxigeno IS NULL THEN '' ELSE r.saturacion_oxigeno END AS saturacion_oxigeno, "+
		"CASE WHEN r.tratamiento_complicaciones IS NULL THEN '' ELSE r.tratamiento_complicaciones END AS tratamiento_complicaciones, "+
		"CASE WHEN r.resumen_historia_clinica IS NULL THEN '' ELSE r.resumen_historia_clinica END AS resumen_historia_clinica, "+
		"CASE WHEN r.observaciones IS NULL THEN '' ELSE r.observaciones END AS observaciones, "+
		"CASE WHEN r.documentos_anexos IS NULL THEN '' ELSE r.documentos_anexos END AS documentos_anexos, "+
		"CASE WHEN r.responsable_traslado IS NULL THEN '' ELSE r.responsable_traslado END AS responsable_traslado, "+
		"CASE WHEN r.numero_movil_traslado IS NULL THEN '' ELSE r.numero_movil_traslado END AS numero_movil_traslado, "+
		"CASE WHEN r.placa_traslado IS NULL THEN '' ELSE r.placa_traslado END AS placa_traslado, "+
		"CASE WHEN r.requiere_ambulancia IS NULL THEN '' ELSE r.requiere_ambulancia END AS requiere_ambulancia, "+
		"CASE WHEN r.codigo_tipo_ambulancia IS NULL THEN '' ELSE r.codigo_tipo_ambulancia END AS codigo_tipo_ambulancia, "+
		"CASE WHEN r.codigo_tipo_ambulancia IS NULL THEN '' ELSE getnombretipoambulancia(r.codigo_tipo_ambulancia,r.institucion) END AS nombre_tipo_ambulancia, "+
		"CASE WHEN r.requiere_reservar_cama IS NULL THEN '' ELSE r.requiere_reservar_cama END AS requiere_reservar_cama, "+
		"CASE WHEN r.centro_costo IS NULL THEN '' ELSE r.centro_costo || '' END AS codigo_centro_costo, "+
		"CASE WHEN r.centro_costo IS NULL THEN '' ELSE getnomcentrocosto(r.centro_costo) END AS nombre_centro_costo, "+
		"r.estado AS estado, "+
		"r.usuario_modifica AS usuario_modifica, "+
		"to_char(r.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') AS fecha_modifica, "+
		"r.hora_modifica AS hora_modifica, "+
		"CASE WHEN r.usuario_anulacion IS NULL THEN '' ELSE r.usuario_anulacion END AS usuario_anulacion, "+
		"CASE WHEN r.fecha_anulacion IS NULL THEN '' ELSE to_char(r.fecha_anulacion,'"+ConstantesBD.formatoFechaAp+"') END AS fecha_anulacion, "+
		"CASE WHEN r.hora_anulacion IS NULL THEN '' ELSE r.hora_anulacion END AS hora_anulacion, "+
		"CASE WHEN r.motivo_anulacion IS NULL THEN '' ELSE r.motivo_anulacion END AS motivo_anulacion, " +
		"'"+ConstantesBD.acronimoNo+"' AS anulacion, " +
		"CASE WHEN r.reserva_cama IS NULL THEN '' ELSE r.reserva_cama || '' END AS reserva_cama, "+
		"CASE WHEN (r.referencia = '"+ConstantesIntegridadDominio.acronimoInterna+"' AND r.estado = '"+ConstantesIntegridadDominio.acronimoEstadoSolicitado+"') OR " +
			"(r.referencia = '"+ConstantesIntegridadDominio.acronimoExterna+"' AND r.estado = '"+ConstantesIntegridadDominio.acronimoEstadoEnTramite+"') THEN " +
				"'"+ConstantesBD.acronimoSi+"' " +
		"ELSE " +
			"'"+ConstantesBD.acronimoNo+"' " +
		"END AS editable, "+
		"'"+ConstantesBD.acronimoSi+"' AS existe, " +
		"CASE WHEN r.numero_carnet_afiliacion IS NULL THEN '' ELSE r.numero_carnet_afiliacion END AS numero_carnet, " +
		"CASE WHEN r.naturaleza_paciente IS NULL THEN '' ELSE r.naturaleza_paciente END AS naturaleza_paciente," +
		"CASE WHEN r.edad_gestacional IS NULL THEN '' ELSE r.edad_gestacional || '' END AS edad_gestacional,  " +
		"r.codigo_pais  As codigo_pais " +
		"FROM referencia r " +
		"INNER JOIN instituciones_sirc i ON(i.codigo=r.institucion_sirc_solicita AND i.institucion=r.institucion) "+ 
		"INNER JOIN motivos_sirc ms ON(ms.consecutivo=r.motivo_referencia) " +
		"INNER JOIN convenios c ON(c.codigo=r.convenio) " + 
		"WHERE ";
	
	/**
	 * Cadena que consulta los servicios SIRC
	 */
	private static final String consultarServiciosSircStr = "SELECT "+ 
		"sr.numero_referencia AS numero_referencia, "+
		"sr.institucion AS institucion, "+
		"sr.codigo_servicio_sirc AS codigo_servicio_sirc, "+
		"ss.descripcion AS nombre_servicio_sirc, "+
		"sr.servicio AS codigo_servicio, "+
		"getcodigopropservicio2(sr.servicio,"+ConstantesBD.codigoTarifarioCups+") AS codigo_cups, "+
		"getnombreservicio(sr.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_cups, "+
		"CASE WHEN sr.observaciones IS NULL THEN '' ELSE sr.observaciones END AS observaciones, "+
		"'"+ConstantesBD.acronimoSi+"' AS existe "+ 
		"FROM servicios_referencia sr "+ 
		"INNER JOIN servicios_sirc ss ON(ss.codigo=sr.codigo_servicio_sirc AND ss.institucion=sr.institucion) "+
		"WHERE "+ 
		"sr.numero_referencia = ? " +
		"ORDER BY nombre_servicio_sirc,codigo_cups";
	
	/**
	 * Cadena que consulta los signos vitales
	 */
	private static final String consultarSignosVitalesStr = "SELECT "+ 
		"sv.codigo AS codigo_signo_vital, "+
		"sv.nombre AS nombre_signo_vital, "+
		"sv.unidad_medida AS unidad_medida, "+
		"getultimosignovitalreferencia(?,?,sv.codigo) AS valor," +
		"getExisteSignoVitalReferencia(?,sv.codigo) AS existe  "+ 
		"FROM signos_vitales sv "+ 
		"WHERE "+ 
		"sv.activo = '"+ConstantesBD.acronimoSi+"' AND "+ 
		"sv.codigo_especialidad = "+ConstantesBD.codigoEspecialidadValoracionGeneral+" "+ 
		"ORDER BY sv.orden";
	
	/**
	 * Cadena que consulta los resultados examenes diagnósticos
	 */
	private static final String consultarResultadosExamenesDiagnosticosStr = "SELECT "+ 
		"re.codigo AS codigo, "+
		"re.numero_referencia AS numero_referencia, "+
		"re.descripcion AS descripcion, "+
		"re.interpretacion AS interpretacion, "+
		"CASE WHEN re.numero_sol_proc IS NULL THEN '' ELSE re.numero_sol_proc || '' END AS numero_solicitud, " +
		"CASE WHEN re.fecha IS NULL THEN '' ELSE to_char(re.fecha,'"+ConstantesBD.formatoFechaAp+"') END AS fecha, " +
		"CASE WHEN re.hora IS NULL THEN '' ELSE re.hora END AS hora, "+
		"'"+ConstantesBD.acronimoSi+"' AS existe "+
		"FROM resultado_exam_diag_referencia re "+ 
		"WHERE "+ 
		"re.numero_referencia = ?";
	
	/**
	 * Cadena que consulta los exámenes diagnosticos de la referencia
	 */
	private static final String consultarDiagnosticosStr = "SELECT "+ 
		"dr.codigo AS codigo, "+
		"dr.numero_referencia AS numero_referencia, "+
		"dr.acronimo_diagnostico AS acronimo_diagnostico, "+
		"dr.tipo_cie AS tipo_cie, "+
		"d.nombre AS nombre_diagnostico, "+
		"dr.principal AS principal, "+
		"'"+ConstantesBD.acronimoSi+"' AS existe, "+
		"'true' AS chequeado "+
		"FROM diagnosticos_referencia dr "+ 
		"INNER JOIN diagnosticos d ON(d.acronimo=dr.acronimo_diagnostico AND d.tipo_cie=dr.tipo_cie) "+ 
		"WHERE "+ 
		"dr.numero_referencia = ? "+ 
		"ORDER BY dr.codigo";
	
	/**
	 * Cadena que consulta los diagnosticos de la evolucion
	 */
	private static final String consultarDiagnosticosEvolucionStr = "SELECT "+
		"'' AS codigo, "+
		"'' As numero_referencia, "+
		"ed.acronimo_diagnostico AS acronimo_diagnostico, "+
		"ed.tipo_cie_diagnostico AS tipo_cie, "+
		"getnombrediagnostico(ed.acronimo_diagnostico,ed.tipo_cie_diagnostico) AS nombre_diagnostico, "+
		"CASE WHEN ed.principal = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"'  ELSE '"+ConstantesBD.acronimoNo+"' END AS principal, "+
		"'"+ConstantesBD.acronimoNo+"' AS existe, "+
		"'true' AS chequeado "+
		"FROM evol_diagnosticos ed "+ 
		"WHERE "+
		"ed.evolucion = ? "+  
		"order by ed.numero";
	
	/**
	 * Cadena que consulta los diagnosticos de la valoracion
	 */
	private static final String consultarDiagnosticosValoracionStr = "SELECT "+
		"'' AS codigo, "+
		"'' AS numero_referencia, "+
		"vd.acronimo_diagnostico AS acronimo_diagnostico, "+
		"vd.tipo_cie_diagnostico AS tipo_cie, "+
		"getnombrediagnostico(vd.acronimo_diagnostico,vd.tipo_cie_diagnostico) AS nombre_diagnostico, "+
		"CASE WHEN vd.principal = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"'  ELSE '"+ConstantesBD.acronimoNo+"' END AS principal, "+
		"'"+ConstantesBD.acronimoNo+"' AS existe, "+
		"'true' AS chequeado "+ 
		"FROM val_diagnosticos vd "+ 
		"WHERE "+
		"vd.valoracion = ? "+ 
		"order by vd.numero";
	
	/**
	 * Cadena que inserta una nueva referencia
	 */
	private static final String insertarStr = "INSERT INTO referencia ( "+
		"numero_referencia, "+
		"codigo_paciente, "+
		"institucion, "+
		"ingreso, "+
		"referencia, "+
		"institucion_sirc_solicita, "+
		"anio_consecutivo, "+
		"consecutivo_punto_atencion, "+
		"codigo_departamento, "+
		"codigo_ciudad, "+ 
		"fecha_referencia, "+
		"hora_referencia, "+
		"profesional_salud, "+
		"codigo_profesional, "+
		"codigo_especialidad, "+
		"registro_medico, "+
		"tipo_usuario, "+
		"tipo_referencia, "+
		"tipo_atencion, "+
		"motivo_referencia, "+
		"convenio, "+
		"estrato_social, "+ 
		"antecedentes, "+
		"estado_conciencia, "+
		"examen_fisico, "+
		"paciente_con_oxigeno, "+
		"saturacion_oxigeno, "+
		"tratamiento_complicaciones, "+ 
		"resumen_historia_clinica, "+
		"observaciones, "+
		"documentos_anexos, "+
		"responsable_traslado, "+
		"numero_movil_traslado, "+
		"placa_traslado, "+
		"requiere_ambulancia, "+
		"codigo_tipo_ambulancia, "+
		"centro_costo, "+
		"estado, "+
		"usuario_modifica, "+
		"fecha_modifica, "+
		"hora_modifica, "+
		"usuario_anulacion, "+
		"fecha_anulacion, "+
		"hora_anulacion, "+
		"motivo_anulacion, "+
		"requiere_reservar_cama, "+
		"anamnesis," +
		"reserva_cama," +
		"numero_carnet_afiliacion," +
		"naturaleza_paciente," +
		"edad_gestacional," +
		"codigo_pais) "+
		"VALUES ";
	
	/**
	 * Cadena que consulta el ultimo consecutivo de la referencia
	 */
	private static final String consultarConsecutivoReferenciaStr = "SELECT " +
		"numero_referencia AS numero_referencia " +
		"FROM referencia " +
		"WHERE codigo_paciente = ? " +
		"ORDER BY numero_referencia DESC";
	
	/**
	 * Cadena que modifica un registro de referencia existente
	 */
	private static final String modificarStr = "UPDATE referencia SET "+ 
		"ingreso = ?, "+
		"referencia = ?, "+
		"institucion_sirc_solicita = ?, "+
		"anio_consecutivo = ?, "+
		"consecutivo_punto_atencion = ?, "+
		"codigo_departamento = ?, "+
		"codigo_ciudad = ?, "+
		"fecha_referencia = ?, "+
		"hora_referencia = ?, "+
		"profesional_salud = ?, "+
		"codigo_profesional = ?, "+
		"codigo_especialidad = ?, "+
		"registro_medico = ?, "+
		"tipo_usuario = ?, "+
		"tipo_referencia = ?, "+
		"tipo_atencion = ?, "+
		"motivo_referencia = ?, "+
		"convenio = ?, "+
		"estrato_social = ?, "+
		"antecedentes = ?, "+
		"estado_conciencia = ?, "+
		"examen_fisico = ?, "+
		"paciente_con_oxigeno = ?, "+
		"saturacion_oxigeno = ?, "+
		"tratamiento_complicaciones = ?, "+
		"resumen_historia_clinica = ?, "+
		"observaciones = ?, "+
		"documentos_anexos = ?, "+
		"responsable_traslado = ?, "+
		"numero_movil_traslado = ?, "+
		"placa_traslado = ?, "+
		"requiere_ambulancia = ?, "+
		"codigo_tipo_ambulancia = ?, "+
		"centro_costo = ?, "+
		"estado = ?, "+
		"usuario_modifica = ?, "+
		"fecha_modifica = ?, "+
		"hora_modifica = ?, "+
		"usuario_anulacion = ?, "+
		"fecha_anulacion = ?, "+
		"hora_anulacion = ?, "+
		"motivo_anulacion = ?, "+ 
		"requiere_reservar_cama = ?, "+
		"anamnesis = ?," +
		"reserva_cama = ? ," +
		"numero_carnet_afiliacion = ?, " +
		"naturaleza_paciente = ?, " +
		"edad_gestacional = ?, "+ 
		"codigo_pais = ? "+ 
		"WHERE numero_referencia = ?";
	
	//******************QUERYS RELACIONADAS CON LOS SERVICIOS SIRC******************************************************
	/**
	 * Cadena que elimina un servicio de una referencia
	 */
	private static final String eliminarServicioStr = "DELETE FROM servicios_referencia  WHERE numero_referencia = ? AND codigo_servicio_sirc = ? AND institucion = ? AND servicio = ?";
	/**
	 * Cadena que inserta un servicio de una referencia
	 */
	private static final String insertarServicioStr = "INSERT INTO servicios_referencia (numero_referencia,codigo_servicio_sirc,institucion,servicio,observaciones) VALUES (?,?,?,?,?)";
	/**
	 * Cadena que modifica un servicio de una referencia
	 */
	private static final String modificarServicioStr = "UPDATE servicios_referencia SET observaciones = ? WHERE numero_referencia = ? AND codigo_servicio_sirc = ? AND institucion = ? AND servicio = ?";
	//*******************************************************************************************************************
	//********************QUERYS RELACIONADAS CON LOS SIGNOS VITALES******************************************************
	/**
	 * Cadena que inserta un signo vital a la referencia
	 */
	private static final String insertarSignoVitalStr = "INSERT INTO signos_vitales_referencia (numero_referencia,signo_vital,valor) VALUES (?,?,?)";
	/**
	 * Cadena que modifica un signo vital a la referencia
	 */
	private static final String modificarSignoVitalStr = "UPDATE signos_vitales_referencia SET valor = ? WHERE numero_referencia = ? AND signo_vital = ?";
	//********************************************************************************************************************
	///********************QUERYS RELACIONADAS CON LOS RESULTADOS EXÁMENES DIAGNOSTICOS************************************
	/**
	 * Cadena que inserta un resultado examen diagnostico a la referencia
	 */
	private static final String insertarResultadoStr = "INSERT INTO resultado_exam_diag_referencia (codigo,numero_referencia,descripcion,interpretacion,numero_sol_proc,fecha,hora) VALUES ";
	/**
	 * Cadena que modifica un resultado examen diagnostico a la referencia
	 */
	private static final String modificarResultadoStr = "UPDATE resultado_exam_diag_referencia SET descripcion = ?, interpretacion = ? WHERE codigo = ?";
	
	/**
	 * Cadena que elimina un resultado examen diagnostico de la referencia
	 */
	private static final String eliminarResultadoStr = "DELETE FROM resultado_exam_diag_referencia WHERE codigo = ?";
	//********************************************************************************************************************
	//********************QUERYS RELACIONADAS CON LOS DIAGNOSTICOS************************************
	/**
	 * Cadena que inserta un diagnostico a la referencia
	 */
	private static final String insertarDiagnosticoStr = "INSERT INTO diagnosticos_referencia (codigo,numero_referencia,acronimo_diagnostico,tipo_cie,principal) VALUES ";
	/**
	 * Cadena que modifica un diagnostico a la referencia
	 */
	private static final String modificarDiagnosticoStr = "UPDATE diagnosticos_referencia SET acronimo_diagnostico = ?, tipo_cie = ? , principal = ? WHERE codigo = ? AND numero_referencia = ?";
	
	/**
	 * Cadena que elimina un diagnostico de la referencia
	 */
	private static final String eliminarDiagnosticoStr = "DELETE FROM diagnosticos_referencia WHERE codigo = ? AND numero_referencia = ?";
	//********************************************************************************************************************
	
	/**
	 * Cadena que realiza la busqueda de instituciones SIRC
	 */
	private static final String busquedaInstitucionesSircStr = "SELECT " +
		"codigo,institucion,descripcion " +
		"FROM instituciones_sirc " +
		"WHERE " +
		"institucion = ? AND " +
		"tipo_inst_referencia = '"+ConstantesBD.acronimoSi+"' AND " +
		"activo = '"+ConstantesBD.acronimoSi+"' ";
	
	/**
	 * Cadena que anula las referencias externas caducadas
	 */
	private static final String anularReferenciasExternasCaducadasStr = "UPDATE referencia "+
		"SET " +
		"estado = '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"', " +
		"fecha_anulacion = CURRENT_DATE, " +
		"hora_anulacion = ?, " +
		"usuario_anulacion = ?, " +
		"motivo_anulacion = 'Referencia anulada por caducidad en horas' " +
		"WHERE " +
		"codigo_paciente = ? AND " +
		"referencia = '"+ConstantesIntegridadDominio.acronimoExterna+"' AND " +
		"estado = '"+ConstantesIntegridadDominio.acronimoEstadoEnTramite+"' AND " +
		"(fecha_referencia < ? OR (fecha_referencia = ? AND hora_referencia < ?))";
	
	
	/**
	 * Método que consulta la referencia
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap cargar(Connection con,HashMap campos)
	{
		PreparedStatementDecorator pst = null;
		try
		{
			//Variable necesarias
			HashMap referencia = new HashMap();
			String consulta = "";
			String tipoReferencia = campos.get("tipoReferencia").toString();
			int codigoPaciente = Utilidades.convertirAEntero(campos.get("codigoPaciente").toString());
			int ingreso = Utilidades.convertirAEntero(campos.get("ingreso").toString());
			String numeroReferencia = campos.get("numeroReferencia").toString(); //solo aplica cuando viene de historia Atenciones
			
		
			
			//********CONSULTA DE LA INFORMACION GENERAL DE LA REFERENCIA****************************************
			consulta = consultarInformacionGeneralStr ;
			//dependiendo del tipo de referencia elegido se hace el filtro
			if(numeroReferencia.equals(""))
			{
			
				if(tipoReferencia.equals(ConstantesIntegridadDominio.acronimoInterna))
				{
					
					consulta += " r.ingreso = "+ingreso+" AND " +
						"r.referencia = '"+tipoReferencia+"' AND " +
						"r.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoSolicitado+"','"+ConstantesIntegridadDominio.acronimoEstadoEnTramite+"','"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') ";
					
					 
				}
				else if(tipoReferencia.equals(ConstantesIntegridadDominio.acronimoExterna))
				{
						if(ingreso>0)
						{
							consulta += " r.ingreso = "+ingreso+" AND " +
							"r.referencia = '"+tipoReferencia+"' AND " +
							"r.estado = '"+ConstantesIntegridadDominio.acronimoEstadoAdmitido+"' ";
						}
						else
						{
							consulta += " r.codigo_paciente = "+codigoPaciente+" AND " +
								"r.referencia = '"+tipoReferencia+"' AND " +
								"r.estado = '"+ConstantesIntegridadDominio.acronimoEstadoEnTramite+"' ";
						}
				}
			}
			else
				consulta += " r.numero_referencia = "+numeroReferencia+" ";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			logger.info("INICIO CONSULTA ENCABEZADO REFERENCIA***************************");
			referencia = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), false, true);
			logger.info("FIN CONSULTA ENCABEZADO REFERENCIA***************************");
			//******************************************************************************************************
			
			//*************************************************************************************************************
			//Si no se encontró registro de referencia se postulan los datos por defecto
			if(Utilidades.convertirAEntero(referencia.get("numRegistros").toString())<=0)
			{
				//Se asignan datos por defecto
				referencia.put("numeroReferencia", "");
				referencia.put("codigoPaciente", codigoPaciente+"");
				referencia.put("codigoInstitucion", "");
				referencia.put("idIngreso", ingreso+"");
				referencia.put("tipoReferencia", tipoReferencia);
				referencia.put("codigoSirc", "");
				referencia.put("institucionSirc", "");
				referencia.put("anioConsecutivo", "");
				referencia.put("consecutivoPuntoAtencion", "");
				referencia.put("codigoCiudad", "");
				referencia.put("nombreCiudad", "");
				referencia.put("fechaReferencia", UtilidadFecha.getFechaActual());
				referencia.put("horaReferencia", UtilidadFecha.getHoraActual());
				referencia.put("profesionalSalud", "");
				referencia.put("codigoProfesional", "");
				referencia.put("codigoEspecialidad", "");
				referencia.put("nombreEspecialidad", "");
				referencia.put("registroMedico", "");
				referencia.put("tipoUsuario", "");
				referencia.put("tipoRef", "");
				referencia.put("tipoAtencion", "");
				referencia.put("codigoMotivoReferencia", "");
				referencia.put("descripcionMotivoReferencia", "");
				referencia.put("codigoConvenio", "");
				referencia.put("nombreConvenio", "");
				referencia.put("codigoTipoRegimen", "");
				referencia.put("nombreTipoRegimen", "");
				referencia.put("codigoEstratoSocial", "");
				referencia.put("nombreEstratoSocial", "");
				referencia.put("anamnesis", "");
				referencia.put("antecedentes", "");
				referencia.put("codigoEstadoConciencia", "");
				referencia.put("nombreEstadoConciencia", "");
				referencia.put("examenFisico", "");
				referencia.put("pacienteConOxigeno", ConstantesBD.acronimoNo);
				referencia.put("saturacionOxigeno", "");
				referencia.put("tratamientoComplicaciones", "");
				referencia.put("resumenHistoriaClinica", "");
				referencia.put("observaciones", "");
				referencia.put("documentosAnexos", "");
				referencia.put("responsableTraslado", "");
				referencia.put("numeroMovilTraslado", "");
				referencia.put("placaTraslado", "");
				referencia.put("requiereAmbulancia", "");
				referencia.put("codigoTipoAmbulancia", "");
				referencia.put("nombreTipoAmbulancia", "");
				referencia.put("requiereReservarCama", "");
				referencia.put("codigoCentroCosto", "");
				referencia.put("nombreCentroCosto", "");
				referencia.put("estado", "");
				referencia.put("usuarioModifica", "");
				referencia.put("fechaModifica", "");
				referencia.put("horaModifica", "");
				referencia.put("usuarioAnulacion", "");
				referencia.put("fechaAnulacion", "");
				referencia.put("horaAnulacion", "");
				referencia.put("motivoAnulacion", "");
				referencia.put("anulacion", ConstantesBD.acronimoNo);
				referencia.put("reservaCama", "");
				referencia.put("editable", ConstantesBD.acronimoSi);
				referencia.put("existe", ConstantesBD.acronimoNo);
				referencia.put("numeroCarnet", "");
				referencia.put("naturalezaPaciente", "");
				referencia.put("edadGestacional","");
				referencia.put("codigoPais", "");
				referencia.put("nombrePais", "");
			}
			//**************************************************************************************************************
			
			//***********CONSULTA DE LOS SERVICIOS SIRC**********************************************************************
			HashMap servicios = new HashMap();
			if(!referencia.get("numeroReferencia").toString().equals(""))
			{
				consulta = consultarServiciosSircStr;
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setDouble(1,Utilidades.convertirADouble(referencia.get("numeroReferencia")+""));
				
				logger.info("INICIO CONSULTA SERVICIOS SIRC REFERENCIA***************************");
				servicios = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
				logger.info("FIN CONSULTA SERVICIOS SIRC REFERENCIA***************************");
			}
			else
				servicios.put("numRegistros","0");
			referencia.put("mapaServicios",servicios);
			//*****************************************************************************************************************
			
			//********CONSULTA DE LOS SIGNOS VITALES***************************************************************************
			HashMap signosVitales = new HashMap();
			consulta = consultarSignosVitalesStr;
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			pst.setDouble(1,Utilidades.convertirADouble(referencia.get("numeroReferencia").toString().equals("")?"0":referencia.get("numeroReferencia")+""));
			pst.setInt(2,tipoReferencia.equals(ConstantesIntegridadDominio.acronimoInterna)?ingreso:0);
			pst.setInt(3,Utilidades.convertirAEntero(referencia.get("numeroReferencia").toString().equals("")?"0":referencia.get("numeroReferencia")+""));
			
			logger.info("INICIO CONSULTA SIGNOS VITALES REFERENCIA***************************");
			signosVitales = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
			logger.info("FIN CONSULTA SIGNOS VITALES REFERENCIA***************************");
			referencia.put("mapaSignosVitales",signosVitales);
			//****************************************************************************************************************
			
			//********CONSULTA DE LOS RESULTADOS DE EXAMENES DIAGNOSTICOS*****************************************************
			HashMap resultados = new HashMap();
			if(!referencia.get("numeroReferencia").toString().equals(""))
			{
				consulta = consultarResultadosExamenesDiagnosticosStr;
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setDouble(1,Utilidades.convertirADouble(referencia.get("numeroReferencia")+""));
				
				logger.info("INICIO CONSULTA RESULTADOS EXAMENES REFERENCIA***************************");
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
				logger.info("FIN CONSULTA RESULTADOS EXAMENES REFERENCIA***************************");
			}
			else
				resultados.put("numRegistros","0");
			referencia.put("mapaResultados",resultados);
			//*****************************************************************************************************************
			
			//********CONSULTA DE LOS DIAGNOSTICOS******************************************************************************
			HashMap diagnosticos = new HashMap();
			diagnosticos.put("numRegistros","0");
			
			//Primero se consultan los diagnosticos de la referencia , si existe referencia
			if(!referencia.get("numeroReferencia").toString().equals(""))
			{
				consulta = consultarDiagnosticosStr;
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				pst.setDouble(1,Utilidades.convertirADouble(referencia.get("numeroReferencia")+""));
				
				logger.info("INICIO CONSULTA DIAGNOSTICOS REFERENCIA***************************");
				diagnosticos = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
				logger.info("FIN CONSULTA DIAGNOSTICOS REFERENCIA***************************");
			}
			
			//Si no hay diagnosticos y la referencia es interna se consulta lo último en evolucion y valoracion del ingreso
			if(Utilidades.convertirAEntero(diagnosticos.get("numRegistros").toString())<=0&&tipoReferencia.equals(ConstantesIntegridadDominio.acronimoInterna))
			{
				logger.info("INICIO CONSULTA ULTIMA EVOLUCION INGRESO***************************");
				int evolucion = UtilidadesHistoriaClinica.consultarUltimaEvolucionIngreso(con, ingreso);
				logger.info("FIN CONSULTA ULTIMA EVOLUCION INGRESO***************************");
				if(evolucion>0)
				{
					//Se consultan los diagnosticos de la evolucion
					pst =  new PreparedStatementDecorator(con.prepareStatement(consultarDiagnosticosEvolucionStr));
					pst.setInt(1,evolucion);
					logger.info("INICIO CONSULTA DIAGNOSTICOS EVOLUCION INGRESO***************************");
					diagnosticos = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
					logger.info("FIN CONSULTA DIAGNOSTICOS EVOLUCION INGRESO***************************");
				}
				
				if(Utilidades.convertirAEntero(diagnosticos.get("numRegistros").toString())<=0)
				{
					//Si todavía siguen sin haber diagnosticos se consultan los diagnosticos de la valoracion
					logger.info("INICIO CONSULTA ULTIMA VALORACION INGRESO***************************");
					int valoracion = UtilidadesHistoriaClinica.consultarUltimaValoracionIngreso(con, ingreso);
					logger.info("FIN CONSULTA ULTIMA VALORACION INGRESO***************************");
					if(valoracion>0)
					{
						//Se consultan los diagnosticos de la valoracin
						pst =  new PreparedStatementDecorator(con.prepareStatement(consultarDiagnosticosValoracionStr));
						pst.setInt(1,valoracion);
						logger.info("INICIO CONSULTA DIAGNOSTICOS VALORACION INGRESO***************************");
						diagnosticos = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
						logger.info("FIN CONSULTA DIAGNOSTICOS VALORACION INGRESO***************************");
					}
				}
			}
			referencia.put("mapaDiagnosticos",diagnosticos);
			//*******************************************************************************************************************
			
			//********Si la referencia no existe se postula la secuencia siguiente************************************
			if(referencia.get("numeroReferencia").toString().equals("")){
				logger.info("\n\n\n\n\n llega -> seq_referencia ");
				/*
				 * javrammo
				 * Por MT 6238 : Para Oracle no se puede obtener el CURRVAL de la secuencia ya que no ha sido utilizado el NEXTVAL
				 * durante la sesiOn. Ademas por la logica del codigo la intencion es obtener el valor siguiente de la secuencia 
				 */				
				//referencia.put("numeroReferencia", (UtilidadBD.obtenerUltimoValorSecuencia(con, "seq_referencia")+1)+"");
				referencia.put("numeroReferencia", UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_referencia")+"");
			}
			return referencia;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargar: "+e);
			return null;
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Método implementado para guardar información de referencia
	 * bien sea de inserción , eliminacion o modificacion
	 * @param con
	 * @param campos
	 * @return
	 */
	public static String guardar(Connection con,HashMap campos)
	{
		
		//Se verifica si es un registro nuevo o existente
		boolean existe = UtilidadTexto.getBoolean(campos.get("existe").toString());
		String consecutivo = ""; //consecutivo del registro de referencia
		
		//***********GUARDAR INFORMACION GENERAL****************************
		if(existe)
			consecutivo = modificar(con,campos);
		else
		{
			consecutivo = insertar(con,campos);
			campos.put("numeroReferencia",consecutivo);
		}
		//******************************************************************+
		
		//******GUARDAR SERVICIOS SIRC*****************************************
		//se verifica si el proceso anterior fue exitoso para continuar
		if(!consecutivo.equals(""))
			consecutivo = guardarServicios(con,campos);
		//**********************************************************************
		
		//******GUARDAR SIGNOS VITALES*****************************************
		//se verifica si el proceso anterior fue exitoso para continuar
		if(!consecutivo.equals(""))
			consecutivo = guardarSignosVitales(con,campos);
		//**********************************************************************
		
		//******GUARDAR RESULTADOS EXAMENES DIAGNOSTICOS**************************
		//se verifica si el proceso anterior fue exitoso para continuar
		if(!consecutivo.equals(""))
			consecutivo = guardarResultadosExamenesDiagnosticos(con,campos);
		//**********************************************************************
		
		//******GUARDAR DIAGNOSTICOS**************************
		//se verifica si el proceso anterior fue exitoso para continuar
		if(!consecutivo.equals(""))
			consecutivo = guardarDiagnosticos(con,campos);
		//**********************************************************************
		
		return consecutivo;
		
	}

	/**
	 * Método para realizar la insercion/modificacion/eliminacion de diagnosticos
	 * @param con
	 * @param campos
	 * @return
	 */
	private static String guardarDiagnosticos(Connection con, HashMap campos) 
	{
		PreparedStatementDecorator pst = null;
		try
		{
			String consecutivo = "";
			String consulta = "";
			boolean exito = true;
			int resp = 0;
			
			//*********SE INGRESAN/MODIFICAN/ELIMINAN LOS DIAGNOSTICOS****************
			HashMap diagnosticos = (HashMap)campos.get("mapaDiagnosticos");
			
			for(int i=0;i<Utilidades.convertirAEntero(diagnosticos.get("numRegistros").toString());i++)
			{
				resp = 0;
				//se verifica si el registro ya existe en la base de datos
				if(UtilidadTexto.getBoolean(diagnosticos.get("existe_"+i).toString()))
				{
					//Se verifica si el registro fue chequeado
					if(UtilidadTexto.getBoolean(diagnosticos.get("chequeado_"+i).toString()))
					{					
						//SE REALIZA MODIFICACION************************
						pst =  new PreparedStatementDecorator(con.prepareStatement(modificarDiagnosticoStr));
						
						/**
						 * UPDATE diagnosticos_referencia SET acronimo_diagnostico = ?, tipo_cie = ? , principal = ? WHERE codigo = ? AND numero_referencia = ?
						 */
						
						pst.setString(1,diagnosticos.get("acronimoDiagnostico_"+i)+"");
						pst.setInt(2,Utilidades.convertirAEntero(diagnosticos.get("tipoCie_"+i)+""));
						pst.setString(3,diagnosticos.get("principal_"+i)+"");
						pst.setDouble(4,Utilidades.convertirADouble(diagnosticos.get("codigo_"+i)+""));
						pst.setDouble(5,Utilidades.convertirADouble(diagnosticos.get("numeroReferencia_"+i)+""));
						
						resp = pst.executeUpdate();
					}
					else
					{
						//SE REALIZA ELIMINACION*******************************
						pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarDiagnosticoStr));
						
						/**
						 * DELETE FROM diagnosticos_referencia WHERE codigo = ? AND numero_referencia = ?;
						 */
						
						pst.setDouble(1,Utilidades.convertirADouble(diagnosticos.get("codigo_"+i)+""));
						pst.setDouble(2,Utilidades.convertirADouble(diagnosticos.get("numeroReferencia_"+i)+""));
						
						resp = pst.executeUpdate();
					}
				}
				else
				{
					//Se verifica si el registro fue chequeado
					if(UtilidadTexto.getBoolean(diagnosticos.get("chequeado_"+i).toString()))
					{
						//SE REALIZA INSERCIÓN****************************
						consulta = insertarDiagnosticoStr + " ("+campos.get("secuenciaDiagnosticos")+",?,?,?,?)";
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
						
						/**
						 * INSERT INTO diagnosticos_referencia (codigo,numero_referencia,acronimo_diagnostico,tipo_cie,principal) VALUES 
						 */
						
						pst.setDouble(1,Utilidades.convertirADouble(campos.get("numeroReferencia")+""));
						pst.setString(2,diagnosticos.get("acronimoDiagnostico_"+i)+"");
						pst.setInt(3,Utilidades.convertirAEntero(diagnosticos.get("tipoCie_"+i)+""));
						pst.setString(4,diagnosticos.get("principal_"+i)+"");
						
						resp = pst.executeUpdate();
					}
					else
						resp = 1;
				}
				
				//Se verifica exito de proceso
				if(resp<=0)
					exito = false;
			}
			
			if(exito)
				consecutivo = campos.get("numeroReferencia").toString();
			
			return consecutivo;
		}
		catch(SQLException e)
		{
			logger.error("Error en guardarDiagnosticos: "+e);
			return "";
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
	}

	/**
	 * Método para realizar la inserción de los resultados de exámenes diagnosticos
	 * @param con
	 * @param campos
	 * @return
	 */
	private static String guardarResultadosExamenesDiagnosticos(Connection con, HashMap campos) 
	{
		PreparedStatementDecorator pst = null;
		try
		{
			String consecutivo = "";
			String consulta = "";
			boolean exito = true;
			int resp = 0;
			String descripcion = "", interpretacion = "";
			
			//**************SE ELIMINAN LOS RESULTADOS ******************
			HashMap resultados = (HashMap)campos.get("mapaResultadosEliminados");
			for(int i=0;i<Utilidades.convertirAEntero(resultados.get("numRegistros").toString());i++)
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarResultadoStr));
				pst.setObject(1,resultados.get("codigo_"+i));
				pst.executeUpdate();
			}
			//********************************************************
			
			//*********SE INGRESAN/MODIFICAN LOS RESULTADOS****************
			resultados = (HashMap)campos.get("mapaResultados");
			for(int i=0;i<Utilidades.convertirAEntero(resultados.get("numRegistros").toString());i++)
			{
				resp = 0;
				descripcion = resultados.get("descripcion_"+i).toString();
				interpretacion = resultados.get("interpretacion_"+i).toString();
				
				//se verifica si el registro ya existe en la base de datos
				if(UtilidadTexto.getBoolean(resultados.get("existe_"+i).toString()))
				{
					//Se verifica que sea un registro de REferencia Externa (que no relaciona solicitud)
					if(resultados.get("numeroSolicitud_"+i).toString().equals(""))
					{
						//Se verifica que se haya insertado descripcion e interpretacion
						if(descripcion.equals("")&&interpretacion.equals(""))
						{
							//SE ELIMINA EL REGISTRO PORQUE SE BORRÓ TODA SU INFORMACION************
							pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarResultadoStr));
							pst.setObject(1,resultados.get("codigo_"+i));
						}
						else
						{
							//SE REALIZA MODIFICACION************************
							pst =  new PreparedStatementDecorator(con.prepareStatement(modificarResultadoStr));
							
							/**
							 * UPDATE resultado_exam_diag_referencia SET descripcion = ?, interpretacion = ? WHERE codigo = ?
							 */
							
							pst.setString(1,resultados.get("descripcion_"+i)+"");
							pst.setString(2,resultados.get("interpretacion_"+i)+"");
							pst.setDouble(3,Utilidades.convertirADouble(resultados.get("codigo_"+i)+""));
						}
						
						resp = pst.executeUpdate();
					}
					else
						resp = 1;
				}
				else
				{
					//Se verifica que se haya insertado descripcion o interpretacion
					if(!descripcion.equals("")||!interpretacion.equals(""))
					{
						//SE REALIZA INSERCIÓN****************************
						consulta = insertarResultadoStr + " ("+campos.get("secuenciaResultados")+",?,?,?,?,?,?)";
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
						
						/**
						 * INSERT INTO resultado_exam_diag_referencia (
						 * 	codigo,numero_referencia,descripcion,interpretacion,numero_sol_proc,fecha,hora) VALUES 
						 */
						
						pst.setDouble(1,Utilidades.convertirADouble(campos.get("numeroReferencia")+""));
						pst.setString(2,resultados.get("descripcion_"+i)+"");
						pst.setString(3,resultados.get("interpretacion_"+i)+"");
						if(!resultados.get("numeroSolicitud_"+i).toString().equals(""))
							pst.setDouble(4,Utilidades.convertirADouble(resultados.get("numeroSolicitud_"+i)+""));
						else
							pst.setNull(4,Types.NUMERIC);
						
						if(!resultados.get("fecha_"+i).toString().equals(""))
							pst.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(resultados.get("fecha_"+i).toString())));
						else
							pst.setNull(5,Types.DATE);
						if(!resultados.get("hora_"+i).toString().equals(""))
							pst.setString(6,resultados.get("hora_"+i)+"");
						else
							pst.setNull(6,Types.VARCHAR);
						
						resp = pst.executeUpdate();
					}
					else
						resp = 1;
				}
				
				//Se verifica exito de proceso
				if(resp<=0)
					exito = false;
			}
			
			if(exito)
				consecutivo = campos.get("numeroReferencia").toString();
			
			return consecutivo;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en guardarResultadosExamenesDiagnosticos: "+e);
			return "";
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
	}

	/**
	 * Método implementado para actualizar la informacion de los signos vitales de una referencia
	 * @param con
	 * @param campos
	 * @return
	 */
	private static String guardarSignosVitales(Connection con, HashMap campos) 
	{
		PreparedStatementDecorator pst = null;
		try
		{
			String consecutivo = "";
			boolean exito = true;
			int resp = 0;
			
			//********SE INGRESAN/MODIFICAN LOS SIGNOS VITALES*********************
			HashMap signosVitales = (HashMap)campos.get("mapaSignosVitales");
			for(int i=0;i<Utilidades.convertirAEntero(signosVitales.get("numRegistros").toString());i++)
			{
				resp = 0;
				//se verifica si el registro ya existe en la base de datos
				if(UtilidadTexto.getBoolean(signosVitales.get("existe_"+i).toString()))
				{
					//SE REALIZA MODIFICACION************************
					pst =  new PreparedStatementDecorator(con.prepareStatement(modificarSignoVitalStr));
					
					/**
					 * UPDATE signos_vitales_referencia SET valor = ? WHERE numero_referencia = ? AND signo_vital = ?
					 */
					
					if(!signosVitales.get("valor_"+i).toString().equals(""))
						pst.setString(1,signosVitales.get("valor_"+i)+"");
					else
						pst.setNull(1,Types.VARCHAR);
					pst.setDouble(2,Utilidades.convertirADouble(campos.get("numeroReferencia")+""));
					pst.setInt(3,Utilidades.convertirAEntero(signosVitales.get("codigoSignoVital_"+i)+""));
					
					resp = pst.executeUpdate();
				}
				else
				{
					//SE REALIZA INSERCIÓN****************************
					pst =  new PreparedStatementDecorator(con.prepareStatement(insertarSignoVitalStr));
					
					/**
					 * INSERT INTO signos_vitales_referencia (numero_referencia,signo_vital,valor) VALUES (?,?,?)
					 */
					
					pst.setDouble(1,Utilidades.convertirADouble(campos.get("numeroReferencia")+""));
					pst.setInt(2,Utilidades.convertirAEntero(signosVitales.get("codigoSignoVital_"+i)+""));
					if(!signosVitales.get("valor_"+i).toString().equals(""))
						pst.setString(3,signosVitales.get("valor_"+i)+"");
					else
						pst.setNull(3,Types.VARCHAR);
					
					resp = pst.executeUpdate();
				}
				
				//Se verifica exito de proceso
				if(resp<=0)
					exito = false;
			}
			//********************************************************************
			
			if(exito)
				consecutivo = campos.get("numeroReferencia").toString();
			
			return consecutivo;
		}
		catch(SQLException e)
		{
			logger.error("Error en guardarSignosVitales: "+e);
			return "";
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
	}

	/**
	 * Método implementado para actualizar la informacion de los servicios SIRC de una referencia
	 * @param con
	 * @param campos
	 * @return
	 */
	private static String guardarServicios(Connection con, HashMap campos) 
	{
		PreparedStatementDecorator pst = null;
		try
		{	
			String consecutivo = "";
			boolean exito = true;
			int resp = 0;
			
			//**************SE ELIMINAN LOS SERVICIOS ******************
			HashMap servicios = (HashMap)campos.get("mapaServiciosEliminados");
			for(int i=0;i<Utilidades.convertirAEntero(servicios.get("numRegistros").toString());i++)
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarServicioStr));
				
				/**
				 * DELETE FROM servicios_referencia  WHERE numero_referencia = ? AND codigo_servicio_sirc = ? AND institucion = ? AND servicio = ?
				 */
				
				pst.setDouble(1,Utilidades.convertirADouble(servicios.get("numeroReferencia_"+i)+""));
				pst.setDouble(2,Utilidades.convertirADouble(servicios.get("codigoServicioSirc_"+i)+""));
				pst.setInt(3,Utilidades.convertirAEntero(servicios.get("institucion_"+i)+""));
				pst.setInt(4,Utilidades.convertirAEntero(servicios.get("codigoServicio_"+i)+""));
				pst.executeUpdate();
			}
			//********************************************************
			
			//*********SE INGRESAN/MODIFICAN LOS SERVICIOS****************
			servicios = (HashMap)campos.get("mapaServicios");
			for(int i=0;i<Utilidades.convertirAEntero(servicios.get("numRegistros").toString());i++)
			{
				resp = 0;
				//se verifica si el registro ya existe en la base de datos
				if(UtilidadTexto.getBoolean(servicios.get("existe_"+i).toString()))
				{
					//SE REALIZA MODIFICACION************************
					pst =  new PreparedStatementDecorator(con.prepareStatement(modificarServicioStr));
					
					/**
					 * UPDATE servicios_referencia SET 
					 * observaciones = ? 
					 * WHERE numero_referencia = ? AND codigo_servicio_sirc = ? AND institucion = ? AND servicio = ?
					 */
					
					if(!servicios.get("observaciones_"+i).toString().equals(""))
						pst.setString(1,servicios.get("observaciones_"+i)+"");
					else
						pst.setNull(1,Types.VARCHAR);
					pst.setDouble(2,Utilidades.convertirADouble(servicios.get("numeroReferencia_"+i)+""));
					pst.setDouble(3,Utilidades.convertirADouble(servicios.get("codigoServicioSirc_"+i)+""));
					pst.setInt(4,Utilidades.convertirAEntero(servicios.get("institucion_"+i)+""));
					pst.setInt(5,Utilidades.convertirAEntero(servicios.get("codigoServicio_"+i)+""));
					
					resp = pst.executeUpdate();
				}
				else
				{
					//SE REALIZA INSERCIÓN****************************
					pst =  new PreparedStatementDecorator(con.prepareStatement(insertarServicioStr));
					
					/**
					 * INSERT INTO servicios_referencia (numero_referencia,codigo_servicio_sirc,institucion,servicio,observaciones) VALUES (?,?,?,?,?)
					 */
					
					pst.setDouble(1,Utilidades.convertirADouble(campos.get("numeroReferencia")+""));
					pst.setDouble(2,Utilidades.convertirADouble(servicios.get("codigoServicioSirc_"+i)+""));
					pst.setInt(3,Utilidades.convertirAEntero(servicios.get("institucion_"+i)+""));
					pst.setInt(4,Utilidades.convertirAEntero(servicios.get("codigoServicio_"+i)+""));
					if(!servicios.get("observaciones_"+i).toString().equals(""))
						pst.setString(5,servicios.get("observaciones_"+i)+"");
					else
						pst.setNull(5,Types.VARCHAR);
					
					resp = pst.executeUpdate();
				}
				
				//Se verifica exito de proceso
				if(resp<=0)
					exito = false;
			}
			
			if(exito)
				consecutivo = campos.get("numeroReferencia").toString();
			
			return consecutivo;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en guardarServicios :"+e);
			return "";
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
		
	}

	/**
	 * Método implementado para modificar una referencia existente
	 * @param con
	 * @param campos
	 * @return
	 */
	private static String modificar(Connection con, HashMap campos) 
	{
		PreparedStatementDecorator pst = null;
		try
		{
			String consecutivo = ""; //consecutivo del registro de referencia
			String[] vector = new String[0]; //vector auxiliar
			int resp = 0; 
			
		 pst =  new PreparedStatementDecorator(con.prepareStatement(modificarStr));
			
			/**
			*
		"estado_conciencia = ?, "+
		"examen_fisico = ?, "+
		"paciente_con_oxigeno = ?, "+
		"saturacion_oxigeno = ?, "+
		"tratamiento_complicaciones = ?, "+
		"resumen_historia_clinica = ?, "+
		"observaciones = ?, "+
		"documentos_anexos = ?, "+
		"responsable_traslado = ?, "+
		"numero_movil_traslado = ?, "+
		"placa_traslado = ?, "+
		"requiere_ambulancia = ?, "+
		"codigo_tipo_ambulancia = ?, "+
		"centro_costo = ?, "+
		"estado = ?, "+
		"usuario_modifica = ?, "+
		"fecha_modifica = ?, "+
		"hora_modifica = ?, "+
		"usuario_anulacion = ?, "+
		"fecha_anulacion = ?, "+
		"hora_anulacion = ?, "+
		"motivo_anulacion = ?, "+ 
		"requiere_reservar_cama = ?, "+
		"anamnesis = ?," +
		"reserva_cama = ? ," +
		"numero_carnet_afiliacion = ?, " +
		"naturaleza_paciente = ?, " +
		"edad_gestacional = ?, "+ 
		"codigo_pais = ? "+ 
		"WHERE numero_referencia = ?";
			 */
			
			
			if(!campos.get("idIngreso").toString().equals(""))
				pst.setInt(1, Utilidades.convertirAEntero(campos.get("idIngreso")+""));
			else
				pst.setNull(1, Types.INTEGER);
			
			pst.setString(2,campos.get("tipoReferencia")+"");
			pst.setString(3,campos.get("codigoSirc")+"");
			
			if(!campos.get("anioConsecutivo").toString().equals(""))
				pst.setString(4,campos.get("anioConsecutivo")+"");
			else
				pst.setNull(4,Types.VARCHAR);
			
			if(!campos.get("consecutivoPuntoAtencion").toString().equals(""))
				pst.setString(5,campos.get("consecutivoPuntoAtencion")+"");
			else
				pst.setNull(5,Types.VARCHAR);
			
			
			vector = campos.get("codigoCiudad").toString().split(ConstantesBD.separadorSplit);
			pst.setString(6,vector[0]);
			pst.setString(7,vector[1]);
			pst.setDate(8,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaReferencia").toString())));
			pst.setString(9, campos.get("horaReferencia")+"");
			pst.setString(10, campos.get("profesionalSalud")+"");
			
			if(!campos.get("codigoProfesional").toString().equals(""))
				pst.setInt(11, Utilidades.convertirAEntero(campos.get("codigoProfesional")+""));
			else
				pst.setNull(11, Types.INTEGER);
			
			if(!campos.get("codigoEspecialidad").toString().equals(""))
				pst.setInt(12,Utilidades.convertirAEntero(campos.get("codigoEspecialidad")+""));
			else
				pst.setNull(12,Types.INTEGER);
			
			pst.setString(13,campos.get("registroMedico")+"");
			pst.setString(14,campos.get("tipoUsuario")+"");
			pst.setString(15,campos.get("tipoRef")+"");
			pst.setString(16,campos.get("tipoAtencion")+"");
			pst.setDouble(17,Utilidades.convertirADouble(campos.get("codigoMotivoReferencia")+""));
			pst.setInt(18,Utilidades.convertirAEntero(campos.get("codigoConvenio")+""));
			pst.setInt(19,Utilidades.convertirAEntero(campos.get("codigoEstratoSocial")+""));
			pst.setString(20,campos.get("antecedentes")+"");
			
			if(!campos.get("codigoEstadoConciencia").toString().equals(""))
				pst.setInt(21, Utilidades.convertirAEntero(campos.get("codigoEstadoConciencia")+""));
			else
				pst.setNull(21, Types.INTEGER);
			
			pst.setString(22,campos.get("examenFisico")+"");
			pst.setString(23,campos.get("pacienteConOxigeno")+"");
			
			if(!campos.get("saturacionOxigeno").toString().equals(""))
				pst.setString(24, campos.get("saturacionOxigeno")+"");
			else
				pst.setNull(24, Types.VARCHAR);
			
			if(!campos.get("tratamientoComplicaciones").toString().equals(""))
				pst.setString(25, campos.get("tratamientoComplicaciones")+"");
			else
				pst.setNull(25, Types.VARCHAR);
			
			if(!campos.get("resumenHistoriaClinica").toString().equals(""))
				pst.setString(26, campos.get("resumenHistoriaClinica")+"");
			else
				pst.setNull(26, Types.VARCHAR);
			
			if(!campos.get("observaciones").toString().equals(""))
				pst.setString(27, campos.get("observaciones")+"");
			else
				pst.setNull(27, Types.VARCHAR);
			
			if(!campos.get("documentosAnexos").toString().equals(""))
				pst.setString(28, campos.get("documentosAnexos")+"");
			else
				pst.setNull(28, Types.VARCHAR);
			
			if(!campos.get("responsableTraslado").toString().equals(""))
				pst.setString(29, campos.get("responsableTraslado")+"");
			else
				pst.setNull(29, Types.VARCHAR);
			
			if(!campos.get("numeroMovilTraslado").toString().equals(""))
				pst.setString(30, campos.get("numeroMovilTraslado")+"");
			else
				pst.setNull(30, Types.VARCHAR);
			
			if(!campos.get("placaTraslado").toString().equals(""))
				pst.setString(31, campos.get("placaTraslado")+"");
			else
				pst.setNull(31, Types.VARCHAR);
			
			if(!campos.get("requiereAmbulancia").toString().equals(""))
				pst.setString(32, campos.get("requiereAmbulancia")+"");
			else
				pst.setNull(32, Types.CHAR);
			
			if(!campos.get("codigoTipoAmbulancia").toString().equals(""))
				pst.setString(33, campos.get("codigoTipoAmbulancia")+"");
			else
				pst.setNull(33, Types.VARCHAR);
			
			if(!campos.get("codigoCentroCosto").toString().equals(""))
				pst.setInt(34, Utilidades.convertirAEntero(campos.get("codigoCentroCosto")+""));
			else
				pst.setNull(34, Types.INTEGER);
			
			pst.setString(35,campos.get("estado")+"");
			pst.setString(36,campos.get("usuarioModifica")+"");
			pst.setDate(37,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaModifica").toString())));
			pst.setString(38,campos.get("horaModifica")+"");
			
			if(UtilidadTexto.getBoolean(campos.get("anulacion").toString()))
			{
				pst.setString(39, campos.get("usuarioAnulacion")+"");
				pst.setDate(40, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaAnulacion").toString())));
				pst.setString(41, campos.get("horaAnulacion")+"");
				pst.setString(42, campos.get("motivoAnulacion")+"");
			}
			else
			{
				pst.setNull(39, Types.VARCHAR);
				pst.setNull(40, Types.DATE);
				pst.setNull(41, Types.VARCHAR);
				pst.setNull(42, Types.VARCHAR);
			}
						
			if(!campos.get("requiereReservarCama").toString().equals(""))
				pst.setString(43, campos.get("requiereReservarCama")+"");
			else
				pst.setNull(43, Types.VARCHAR);
			
			pst.setString(44,campos.get("anamnesis")+"");
			
			if(!campos.get("reservaCama").toString().equals("")&&UtilidadTexto.getBoolean(campos.get("requiereReservarCama").toString()))
				pst.setDouble(45, Utilidades.convertirADouble(campos.get("reservaCama")+""));
			else
				pst.setNull(45, Types.NUMERIC);
			
			if(!campos.get("numeroCarnet").toString().equals(""))
				pst.setString(46, campos.get("numeroCarnet")+"");
			else
				pst.setNull(46, Types.VARCHAR);
			
			if(!campos.get("naturalezaPaciente").toString().equals(""))
				pst.setString(47, campos.get("naturalezaPaciente")+"");
			else
				pst.setNull(47, Types.VARCHAR);
			
			if(!campos.get("edadGestacional").toString().equals(""))
				pst.setInt(48, Utilidades.convertirAEntero(campos.get("edadGestacional")+""));
			else
				pst.setNull(48, Types.INTEGER);
			
			if(!campos.get("codigoPais").toString().equals(""))
				pst.setInt(49, Utilidades.convertirAEntero(campos.get("codigoPais")+""));
			else
				pst.setNull(49, Types.INTEGER);
			
			pst.setDouble(50,Utilidades.convertirADouble(campos.get("numeroReferencia")+""));
			
			resp = pst.executeUpdate();
			
			if(resp>0)
				consecutivo = campos.get("numeroReferencia").toString();
			
			return consecutivo;
		}
		catch(SQLException e)
		{
			logger.error("Error en modificar: "+e);
			return "";
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
	}

	/**
	 * Método implementado para insertar una nueva referencia
	 * @param con
	 * @param campos
	 * @return
	 */
	private static String insertar(Connection con, HashMap campos) 
	{
		PreparedStatementDecorator pst = null;
		try
		{
			String consulta = insertarStr + " ("+campos.get("secuenciaReferencia")+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			String consecutivo = ""; //consecutivo del registro de referencia
			String[] vector = new String[0]; //vector auxiliar
			int resp = 0;
			
			pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoPaciente")+""));
			pst.setInt(2,Utilidades.convertirAEntero(campos.get("codigoInstitucion")+""));
			
			if(!campos.get("idIngreso").toString().equals(""))
				pst.setInt(3, Utilidades.convertirAEntero(campos.get("idIngreso")+""));
			else
				pst.setNull(3, Types.INTEGER);
			
			pst.setString(4,campos.get("tipoReferencia")+"");
			pst.setString(5,campos.get("codigoSirc")+"");
			
			if(!campos.get("anioConsecutivo").toString().equals(""))
				pst.setString(6,campos.get("anioConsecutivo")+"");
			else
				pst.setNull(6,Types.VARCHAR);
			
			if(!campos.get("consecutivoPuntoAtencion").toString().equals(""))
				pst.setString(7,campos.get("consecutivoPuntoAtencion")+"");
			else
				pst.setNull(7,Types.VARCHAR);
			
			
			vector = campos.get("codigoCiudad").toString().split(ConstantesBD.separadorSplit);
			pst.setString(8,vector[0]);
			pst.setString(9,vector[1]);
			pst.setDate(10,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaReferencia").toString())));
			pst.setString(11, campos.get("horaReferencia")+"");
			pst.setString(12, campos.get("profesionalSalud")+"");
			
			if(!campos.get("codigoProfesional").toString().equals(""))
				pst.setInt(13, Utilidades.convertirAEntero(campos.get("codigoProfesional")+""));
			else
				pst.setNull(13, Types.INTEGER);
			
			if(!campos.get("codigoEspecialidad").toString().equals(""))
				pst.setInt(14,Utilidades.convertirAEntero(campos.get("codigoEspecialidad")+""));
			else
				pst.setNull(14, Types.INTEGER);
			pst.setString(15,campos.get("registroMedico")+"");
			pst.setString(16,campos.get("tipoUsuario")+"");
			pst.setString(17,campos.get("tipoRef")+"");
			pst.setString(18,campos.get("tipoAtencion")+"");
			pst.setDouble(19,Utilidades.convertirADouble(campos.get("codigoMotivoReferencia")+""));
			pst.setInt(20,Utilidades.convertirAEntero(campos.get("codigoConvenio")+""));
			pst.setInt(21,Utilidades.convertirAEntero(campos.get("codigoEstratoSocial")+""));
			pst.setString(22,campos.get("antecedentes")+"");
			
			if(!campos.get("codigoEstadoConciencia").toString().equals(""))
				pst.setInt(23, Utilidades.convertirAEntero(campos.get("codigoEstadoConciencia")+""));
			else
				pst.setNull(23, Types.INTEGER);
			
			pst.setString(24,campos.get("examenFisico")+"");
			pst.setString(25,campos.get("pacienteConOxigeno")+"");
			
			if(!campos.get("saturacionOxigeno").toString().equals(""))
				pst.setString(26, campos.get("saturacionOxigeno")+"");
			else
				pst.setNull(26, Types.VARCHAR);
			
			if(!campos.get("tratamientoComplicaciones").toString().equals(""))
				pst.setString(27, campos.get("tratamientoComplicaciones")+"");
			else
				pst.setNull(27, Types.VARCHAR);
			
			if(!campos.get("resumenHistoriaClinica").toString().equals(""))
				pst.setString(28, campos.get("resumenHistoriaClinica")+"");
			else
				pst.setNull(28, Types.VARCHAR);
			
			if(!campos.get("observaciones").toString().equals(""))
				pst.setString(29, campos.get("observaciones")+"");
			else
				pst.setNull(29, Types.VARCHAR);
			
			if(!campos.get("documentosAnexos").toString().equals(""))
				pst.setString(30, campos.get("documentosAnexos")+"");
			else
				pst.setNull(30, Types.VARCHAR);
			
			if(!campos.get("responsableTraslado").toString().equals(""))
				pst.setString(31, campos.get("responsableTraslado")+"");
			else
				pst.setNull(31, Types.VARCHAR);
			
			if(!campos.get("numeroMovilTraslado").toString().equals(""))
				pst.setString(32, campos.get("numeroMovilTraslado")+"");
			else
				pst.setNull(32, Types.VARCHAR);
			
			if(!campos.get("placaTraslado").toString().equals(""))
				pst.setString(33, campos.get("placaTraslado")+"");
			else
				pst.setNull(33, Types.VARCHAR);
			
			if(!campos.get("requiereAmbulancia").toString().equals(""))
				pst.setString(34, campos.get("requiereAmbulancia")+"");
			else
				pst.setNull(34, Types.CHAR);
			
			if(!campos.get("codigoTipoAmbulancia").toString().equals(""))
				pst.setString(35, campos.get("codigoTipoAmbulancia")+"");
			else
				pst.setNull(35, Types.VARCHAR);
			
			if(!campos.get("codigoCentroCosto").toString().equals(""))
				pst.setInt(36, Utilidades.convertirAEntero(campos.get("codigoCentroCosto")+""));
			else
				pst.setNull(36, Types.INTEGER);
			
			pst.setString(37,campos.get("estado")+"");
			pst.setString(38,campos.get("usuarioModifica")+"");
			pst.setDate(39,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaModifica").toString())));
			pst.setString(40,campos.get("horaModifica")+"");
			
			if(!campos.get("usuarioAnulacion").toString().equals(""))
				pst.setString(41, campos.get("usuarioAnulacion")+"");
			else
				pst.setNull(41, Types.VARCHAR);
			
			if(!campos.get("fechaAnulacion").toString().equals(""))
				pst.setDate(42, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaAnulacion").toString())));
			else
				pst.setNull(42, Types.DATE);
			
			if(!campos.get("horaAnulacion").toString().equals(""))
				pst.setString(43, campos.get("horaAnulacion")+"");
			else
				pst.setNull(43, Types.VARCHAR);
			
			if(!campos.get("motivoAnulacion").toString().equals(""))
				pst.setString(44, campos.get("motivoAnulacion")+"");
			else
				pst.setNull(44, Types.VARCHAR);
			
			if(!campos.get("requiereReservarCama").toString().equals(""))
				pst.setString(45, campos.get("requiereReservarCama")+"");
			else
				pst.setNull(45, Types.VARCHAR);
			
			pst.setString(46,campos.get("anamnesis")+"");
			
			if(!campos.get("reservaCama").toString().equals("")&&UtilidadTexto.getBoolean(campos.get("requiereReservarCama").toString()))
				pst.setDouble(47, Utilidades.convertirADouble(campos.get("reservaCama")+""));
			else
				pst.setNull(47, Types.NUMERIC);
			
			if(!campos.get("numeroCarnet").toString().equals(""))
				pst.setString(48, campos.get("numeroCarnet")+"");
			else
				pst.setNull(48, Types.VARCHAR);
			
			if(!campos.get("naturalezaPaciente").toString().equals(""))
				pst.setString(49, campos.get("naturalezaPaciente")+"");
			else
				pst.setNull(49, Types.VARCHAR);
			
			if(!campos.get("edadGestacional").toString().equals(""))
				pst.setInt(50, Utilidades.convertirAEntero(campos.get("edadGestacional")+""));
			else
				pst.setNull(50, Types.INTEGER);
			
			if(!campos.get("codigoPais").toString().equals(""))
				pst.setInt(51, Utilidades.convertirAEntero(campos.get("codigoPais")+""));
			else
				pst.setNull(51, Types.INTEGER);
			
			resp = pst.executeUpdate();
			
			if(resp>0)
			{
				//Se consulta el consecutivo de la referencia recién ingresada
				pst =  new PreparedStatementDecorator(con.prepareStatement(consultarConsecutivoReferenciaStr));
				pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoPaciente")+""));
				
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
					consecutivo = rs.getInt("numero_referencia")+"";
			}
				
			return consecutivo;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertar: "+e);
			return "";
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Método implementado para realizar la búsqueda de las instituciones SIRC
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap busquedaInstitucionesSirc(Connection con,HashMap campos)
	{
		PreparedStatementDecorator pst =  null; 
		try
		{
			String consulta = busquedaInstitucionesSircStr;
			if(campos.get("tipoReferencia").toString().equals(ConstantesIntegridadDominio.acronimoInterna))
				consulta += "  AND codigo || '-' || institucion IN ";
			else
				consulta += "  AND codigo || '-' || institucion NOT IN ";
			
			
			consulta += " (SELECT CASE WHEN codigo_inst_sirc IS NULL THEN '"+ConstantesBD.codigoNuncaValido+"' ELSE codigo_inst_sirc || '-' || cod_institucion END FROM centro_atencion) " +
				"ORDER BY descripcion ";
			logger.info("consulta de institucines sirc=> "+consulta+" institucion=> "+campos.get("institucion"));
			 pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("institucion")+""));
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en busquedaInstitucionesSirc: "+e);
			return null;
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
	}
	
	/**
	 * Método que realiza la anulacion de referencias externas caducadas
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean anularReferenciasExternasCaducadas(Connection con,HashMap campos)
	{
		PreparedStatementDecorator pst =  null;
		try
		{
			boolean exito = false;
			logger.info("mapaCamposAnulacion=> "+campos);
			pst =  new PreparedStatementDecorator(con.prepareStatement(anularReferenciasExternasCaducadasStr));
			
			/**
			 * UPDATE referencia "+
					"SET " +
					"estado = '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"', " +
					"fecha_anulacion = CURRENT_DATE, " +
					"hora_anulacion = ?, " +
					"usuario_anulacion = ?, " +
					"motivo_anulacion = 'Referencia anulada por caducidad en horas' " +
					"WHERE " +
					"codigo_paciente = ? AND " +
					"referencia = '"+ConstantesIntegridadDominio.acronimoExterna+"' AND " +
					"estado = '"+ConstantesIntegridadDominio.acronimoEstadoEnTramite+"' AND " +
					"(fecha_referencia < ? OR (fecha_referencia = ? AND hora_referencia < ?))
			 */
			
			pst.setString(1, UtilidadFecha.getHoraActual());
			pst.setString(2,campos.get("usuario")+"");
			pst.setInt(3,Utilidades.convertirAEntero(campos.get("codigoPaciente")+""));
			pst.setDate(4,Date.valueOf(campos.get("fechaBase")+""));
			pst.setDate(5,Date.valueOf(campos.get("fechaBase")+""));
			pst.setString(6,campos.get("horaBase")+"");
			
			if(pst.executeUpdate()>0)
				exito = true;
			
			return exito;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en anularReferenciasExternasCaducadas: "+e);
			return false;
		}finally{
			try {
				if(pst!=null){
					pst.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseReferenciaDao "+sqlException.toString() );
			}
		}
	}
	
}
