package com.princetonsa.dao.sqlbase.manejoPaciente;
 
import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import com.princetonsa.dto.manejoPaciente.DtoDiagInfoIniUrg;
import com.princetonsa.dto.manejoPaciente.DtoEnvioInfoAtenIniUrg;
import com.princetonsa.dto.manejoPaciente.DtoInformeAtencionIniUrg;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

public class SqlBaseRegistroEnvioInfAtencionIniUrgDao
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseRegistroEnvioInfAtencionIniUrgDao.class);
	
	/**
	 * Consulta la informacion para la generacion del informe
	 * */
	private static String strConsultaInfoGeneracionReporte = 
											  "SELECT " +
											  "i.id AS ingreso,"+
											  "c.id AS cuenta," +
											  "sc.convenio," +
											  "sc.sub_cuenta," +											  
											  "i.codigo_paciente, " +
											  "coalesce(v.causa_externa,"+ConstantesBD.codigoNuncaValido+") AS causa_externa," +
											  "coalesce(ct.color,"+ConstantesBD.codigoNuncaValido+") AS color, " +
											  "c.origen_admision," +
											  "coalesce(ref.institucion_sirc_solicita,'') AS institucion_sirc_solicita," +
											  "coalesce(ref.institucion,"+ConstantesBD.codigoNuncaValido+") AS institucion," +
											  "coalesce(ref.codigo_departamento,'') AS codigo_departamento," +
											  "coalesce(ref.codigo_ciudad,'') AS codigo_ciudad," +
											  "coalesce(ref.codigo_pais,'') AS codigo_pais," +
											  "coalesce(vo.valor,'') AS motivo_consulta," +
											  "coalesce(vu.codigo_conducta_valoracion,"+ConstantesBD.codigoNuncaValido+") AS codigo_conducta_valoracion," +
											  "coalesce(cval.destino_paciente,"+ConstantesBD.codigoNuncaValido+") AS destino_paciente," +
											  "coalesce(sc.tipo_cobertura,"+ConstantesBD.codigoNuncaValido+") AS tipo_cobertura," +								  
											  "to_char(i.fecha_ingreso,'yyyy-mm-dd') as fecha_ingreso," +
											  "i.hora_ingreso," +
											  "v.numero_solicitud  " +
											  "FROM " +
											  "ingresos i "+
											  "INNER JOIN manejopaciente.cuentas c ON (c.id_ingreso = i.id AND c.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+") "+
											  "INNER JOIN ordenes.solicitudes s  ON (s.cuenta = c.id AND s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialUrgencias+") "+
											  "INNER JOIN historiaclinica.valoraciones v ON (v.numero_solicitud = s.numero_solicitud) " +
											  "INNER JOIN manejopaciente.sub_cuentas sc ON (sc.ingreso = i.id) " +
											  "INNER JOIN facturacion.convenios con ON (con.reporte_atencion_ini_urg = '"+ConstantesBD.acronimoSi+"' AND con.codigo = sc.convenio) " +
											  "INNER JOIN historiaclinica.valoraciones_urgencias vu ON (vu.numero_solicitud = s.numero_solicitud) " +
											  "INNER JOIn conductas_valoracion cval ON (cval.codigo = vu.codigo_conducta_valoracion) " +
											  "LEFT OUTER JOIN admisiones_urgencias au ON (au.cuenta = c.id) " +
											  "LEFT OUTER JOIN triage tr ON (tr.numero_admision = au.codigo) " +
											  "LEFT OUTER JOIN categorias_triage ct ON (ct.consecutivo = tr.categoria_triage) " +
											  "LEFT OUTER JOIN historiaclinica.referencia ref ON (ref.ingreso = i.id AND ref.referencia = '"+ConstantesIntegridadDominio.acronimoExterna+"' AND ref.estado != '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"') " +
											  "LEFT OUTER JOIN valoracion_observaciones vo ON (vo.numero_solicitud = v.numero_solicitud AND vo.tipo = '"+ConstantesIntegridadDominio.acronimoMotivoConsulta+"') " +											 
											  "WHERE i.institucion = ? ";
	
	/**
	 * Consulta la información basica del paciente
	 * */
	public static String strConsultaInfoPaciente = 
											  "SELECT " +											  
											  "coalesce(inf.codigo_pk,"+ConstantesBD.codigoNuncaValido+") AS codigopkinforme, "+
											  "getnombrepersona(i.codigo_paciente) AS nombre_persona,"+
											  "getidpaciente(i.codigo_paciente) AS id_persona," +
											  "i.codigo_paciente,"+
											  "i.id,"+
											  "i.consecutivo," +
											  "i.anio_consecutivo,"+
											  "c.id as cuenta,"+										  
											  "coalesce(inf.estado,'') AS estado_informe," +
											  "c.origen_admision," +
											  "(SELECT COUNT(sb1.numero_referencia) FROM historiaclinica.referencia sb1 WHERE sb1.ingreso = i.id AND sb1.referencia = '"+ConstantesIntegridadDominio.acronimoExterna+"' AND sb1.estado != '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"') AS tiene_referencia," +
											  "sc.convenio," +
											  "con.nombre " +											  
											  "FROM " +
											  "ingresos i "+
											  "INNER JOIN manejopaciente.cuentas c ON (c.id_ingreso = i.id AND c.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+") "+
											  "INNER JOIN manejopaciente.sub_cuentas sc ON (sc.ingreso = i.id) " +
											  "INNER JOIN facturacion.convenios con ON (con.reporte_atencion_ini_urg = '"+ConstantesBD.acronimoSi+"' AND con.codigo = sc.convenio) " +
											  ConstantesBD.separadorSplit+"1 informe_atencion_ini_urg inf ON (inf.cuenta = c.id AND inf.ingreso = i.id AND inf.paciente = i.codigo_paciente AND inf.convenio = sc.convenio) " +
											  ConstantesBD.separadorSplit+"2 envio_info_aten_ini_urg env ON (env.info_atencion_ini_urg = inf.codigo_pk) " +
											  "WHERE i.institucion = ? ";
	
	/**
	 * Consula los envios realizados de un informe
	 * */
	public static String strConsultaEnviosInforme = 
												"SELECT " +
												"codigo_pk," +
												"info_atencion_ini_urg," +
												"to_char(fecha_envio,'dd/mm/yyyy') AS fecha_envio," +
												"hora_envio," +
												"coalesce(entidad_envio,"+ConstantesBD.codigoNuncaValido+") AS entidad_envio," +
												"medio_envio," +
												"coalesce(path_archivo,'') AS patharchivo, " +
												"coalesce(convenio_envio,"+ConstantesBD.codigoNuncaValido+") AS convenio_envio," +
												"CASE WHEN convenio_envio IS NULL THEN '' ELSE getnombreconvenio(convenio_envio) END AS nombre_convenio_envio, " +
												"CASE WHEN entidad_envio  IS NULL THEN '' ELSE e.razon_social END AS nombre_entidad_envio ," +
												"getnombreusuario(usuario_envio) AS nombre_usuario " +
												"FROM " +
												"envio_info_aten_ini_urg " +
												"LEFT OUTER JOIN facturacion.empresas e ON (e.codigo = entidad_envio) " +
												"WHERE info_atencion_ini_urg = ? ";
	
	/**
	 * Consulta el listado de ingresos del paciente
	 * */
	public static String strConsultaListadoPaciente = 
											  "SELECT " +											 
											  "coalesce(inf.codigo_pk,"+ConstantesBD.codigoNuncaValido+") AS codigopkinforme, "+
											  "getnombrepersona(i.codigo_paciente) AS nombre_persona,"+
											  "getidpaciente(i.codigo_paciente) AS id_persona,"+
											  "i.id,"+
											  "i.consecutivo," +
											  "i.anio_consecutivo," +
											  "to_char(i.fecha_ingreso,'dd/mm/yyyy') AS fecha_ingreso," +
											  "i.hora_ingreso AS hora_ingreso," +
											  "coalesce(to_char(i.fecha_egreso,'dd/mm/yyyy'),'') AS fecha_egreso," +
											  "coalesce(to_char(i.hora_egreso||'','hh:mm'),'') AS hora_egreso,"+
											  "c.id as cuenta," +
											  "c.estado_cuenta," +
											  "getnombreestadocuenta(c.estado_cuenta) AS nombre_cuenta,"+
											  "coalesce(inf.estado,'') AS estado_informe," +
											  "c.origen_admision," +
											  "getnombrecentatenxing(i.id) AS nombre_centroatencion, " +
											  "CASE WHEN c.via_ingreso IS NULL THEN '' ELSE getnombreviaingreso(c.via_ingreso) END AS nombre_via_ingreso," +
											  "CASE WHEN inf.convenio  IS NULL THEN '' ELSE getnombreconvenio(inf.convenio) END AS nombre_convenio_responsable " +
											  "FROM " +
											  "ingresos i "+
											  "INNER JOIN manejopaciente.cuentas c ON (c.id_ingreso = i.id AND c.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+") "+
											  "INNER JOIN ordenes.solicitudes s  ON (s.cuenta = c.id AND s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialUrgencias+") "+										  
											  "INNER JOIN historiaclinica.valoraciones v ON (v.numero_solicitud = s.numero_solicitud) " +
											  ConstantesBD.separadorSplit+"1 informe_atencion_ini_urg inf ON (inf.cuenta = c.id AND inf.ingreso = i.id AND inf.paciente = i.codigo_paciente ) " +
											  "LEFT OUTER JOIN envio_info_aten_ini_urg env ON (env.info_atencion_ini_urg = inf.codigo_pk) " +
											  "WHERE i.institucion = ? AND i.codigo_paciente = ? " +											  											  
											  "GROUP BY inf.codigo_pk,i.codigo_paciente,i.id,i.consecutivo,i.anio_consecutivo,i.fecha_ingreso,i.hora_ingreso,i.fecha_egreso,i.hora_egreso,c.id,c.estado_cuenta,inf.estado,c.origen_admision,c.via_ingreso,inf.convenio " +
											  "ORDER BY id DESC ";
	
	/**
	 * Consulta el listado de ingresos del paciente
	 * */
	public static String strConsultaListadoPacienteIngreso = 
											  "SELECT " +											 
											  ConstantesBD.codigoNuncaValido+" AS codigopkinforme, "+
											  "getnombrepersona(i.codigo_paciente) AS nombre_persona,"+
											  "getidpaciente(i.codigo_paciente) AS id_persona,"+
											  "i.id,"+
											  "i.consecutivo," +
											  "i.anio_consecutivo," +
											  "to_char(i.fecha_ingreso,'dd/mm/yyyy') AS fecha_ingreso," +
											  "i.hora_ingreso AS hora_ingreso," +
											  "coalesce(to_char(i.fecha_egreso,'dd/mm/yyyy'),'') AS fecha_egreso," +
											  "coalesce(to_char(i.hora_egreso||'','hh:mm'),'') AS hora_egreso,"+
											  "c.id as cuenta," +
											  "c.estado_cuenta," +
											  "getnombreestadocuenta(c.estado_cuenta) AS nombre_cuenta,"+
											  "'' AS estado_informe," +
											  "c.origen_admision," +
											  "getnombrecentatenxing(i.id) AS nombre_centroatencion, " +
											  "CASE WHEN c.via_ingreso IS NULL THEN '' ELSE getnombreviaingreso(c.via_ingreso) END AS nombre_via_ingreso," +
											  "'' AS nombre_convenio_responsable " +
											  "FROM " +
											  "ingresos i "+
											  "INNER JOIN manejopaciente.cuentas c ON (c.id_ingreso = i.id AND c.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+") "+
											  "INNER JOIN ordenes.solicitudes s  ON (s.cuenta = c.id AND s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialUrgencias+") "+										  
											  "INNER JOIN historiaclinica.valoraciones v ON (v.numero_solicitud = s.numero_solicitud) " +
											  "WHERE i.institucion = ? AND i.codigo_paciente = ? " +
											  "GROUP BY i.codigo_paciente,i.id,i.consecutivo,i.anio_consecutivo,i.fecha_ingreso,i.hora_ingreso,i.fecha_egreso,i.hora_egreso,c.id,c.estado_cuenta,c.origen_admision,c.via_ingreso " +
											  "ORDER BY id DESC ";
	
	/**
	 * Consulta el listado de ingresos del paciente, valida que los que no han sido registrados tengan convenio en indicativo = S
	 * */
	public static String strConsultaListadoPaciente2 = 
											  "SELECT * FROM (" +
											  "" +
											  "SELECT " +											 
											  "coalesce(inf.codigo_pk,"+ConstantesBD.codigoNuncaValido+") AS codigopkinforme, "+
											  "getnombrepersona(i.codigo_paciente) AS nombre_persona,"+
											  "getidpaciente(i.codigo_paciente) AS id_persona,"+
											  "i.id,"+
											  "i.consecutivo," +
											  "i.anio_consecutivo," +
											  "to_char(i.fecha_ingreso,'dd/mm/yyyy') AS fecha_ingreso," +
											  "i.hora_ingreso AS hora_ingreso," +
											  "coalesce(to_char(i.fecha_egreso,'dd/mm/yyyy'),'') AS fecha_egreso," +
											  "coalesce(to_char(i.hora_egreso||'','hh:mm'),'') AS hora_egreso,"+
											  "c.id as cuenta," +
											  "c.estado_cuenta," +
											  "getnombreestadocuenta(c.estado_cuenta) AS nombre_cuenta,"+
											  "coalesce(inf.estado,'') AS estado_informe," +
											  "c.origen_admision," +
											  "getnombrecentatenxing(i.id) AS nombre_centroatencion, " +
											  "CASE WHEN c.via_ingreso IS NULL THEN '' ELSE getnombreviaingreso(c.via_ingreso) END AS nombre_via_ingreso," +
											  "CASE WHEN inf.convenio  IS NULL THEN '' ELSE getnombreconvenio(inf.convenio) END AS nombre_convenio_responsable " +
											  "FROM " +
											  "ingresos i "+
											  "INNER JOIN manejopaciente.cuentas c ON (c.id_ingreso = i.id AND c.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+") "+
											  "INNER JOIN ordenes.solicitudes s  ON (s.cuenta = c.id AND s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialUrgencias+") "+										  
											  "INNER JOIN historiaclinica.valoraciones v ON (v.numero_solicitud = s.numero_solicitud) " +
											  "INNER JOIN informe_atencion_ini_urg inf ON (inf.cuenta = c.id AND inf.ingreso = i.id AND inf.paciente = i.codigo_paciente) " +
											  "LEFT OUTER JOIN envio_info_aten_ini_urg env ON (env.info_atencion_ini_urg = inf.codigo_pk) " +
											  "WHERE i.institucion = ? AND i.codigo_paciente = ? " +											  											  
											  "GROUP BY inf.codigo_pk,i.codigo_paciente,i.id,i.consecutivo,i.anio_consecutivo,i.fecha_ingreso,i.hora_ingreso,i.fecha_egreso,i.hora_egreso,c.id,c.estado_cuenta,inf.estado,c.origen_admision,c.via_ingreso,inf.convenio " +
											  "" +
											  "UNION " +
											  "" +
											  ""+
											  "SELECT " +											 
											  ConstantesBD.codigoNuncaValido+" AS codigopkinforme, "+
											  "getnombrepersona(i.codigo_paciente) AS nombre_persona,"+
											  "getidpaciente(i.codigo_paciente) AS id_persona,"+
											  "i.id,"+
											  "i.consecutivo," +
											  "i.anio_consecutivo," +
											  "to_char(i.fecha_ingreso,'dd/mm/yyyy') AS fecha_ingreso," +
											  "i.hora_ingreso AS hora_ingreso," +
											  "coalesce(to_char(i.fecha_egreso,'dd/mm/yyyy'),'') AS fecha_egreso," +
											  "coalesce(to_char(i.hora_egreso||'','hh:mm'),'') AS hora_egreso,"+
											  "c.id as cuenta," +
											  "c.estado_cuenta," +
											  "getnombreestadocuenta(c.estado_cuenta) AS nombre_cuenta,"+
											  "'' AS estado_informe," +
											  "c.origen_admision," +
											  "getnombrecentatenxing(i.id) AS nombre_centroatencion, " +
											  "CASE WHEN c.via_ingreso IS NULL THEN '' ELSE getnombreviaingreso(c.via_ingreso) END AS nombre_via_ingreso," +
											  "'' AS nombre_convenio_responsable " +
											  "FROM " +
											  "ingresos i "+
											  "INNER JOIN manejopaciente.cuentas c ON (c.id_ingreso = i.id AND c.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+") "+
											  "INNER JOIN ordenes.solicitudes s  ON (s.cuenta = c.id AND s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialUrgencias+") "+	
											  "INNER JOIN manejopaciente.sub_cuentas sc ON (sc.ingreso = i.id) " +
											  "INNER JOIN facturacion.convenios con ON (con.reporte_atencion_ini_urg = '"+ConstantesBD.acronimoSi+"' AND con.codigo = sc.convenio) " +
											  "INNER JOIN historiaclinica.valoraciones v ON (v.numero_solicitud = s.numero_solicitud) " +											  											  
											  "WHERE i.institucion = ? AND i.codigo_paciente = ? " +	
											  "AND (SELECT COUNT(s1.codigo_pk) FROM informe_atencion_ini_urg s1 WHERE s1.cuenta = c.id AND s1.ingreso = i.id AND s1.paciente = i.codigo_paciente ) = 0 "+
											  "GROUP BY i.codigo_paciente,i.id,i.consecutivo,i.anio_consecutivo,i.fecha_ingreso,i.hora_ingreso,i.fecha_egreso,i.hora_egreso,c.id,c.estado_cuenta,c.origen_admision,c.via_ingreso " +
											  "" +
											  ") sco1 " +
											  "" +
											  "ORDER BY id DESC ";	
	
	/**
	 * Consulta el listado de Valoraciones enviadas o no 
	 * dentro del informe de atencion inicial de urgencias
	 * */
	public static String strConsultaListado = "SELECT " +
											  "DISTINCT " +
											  "coalesce(inf.codigo_pk,"+ConstantesBD.codigoNuncaValido+") AS codigopkinforme, "+
											  "getnombrepersona(i.codigo_paciente) AS nombre_persona,"+
											  "getidpaciente(i.codigo_paciente) AS id_persona,"+
											  "i.id," +
											  "i.consecutivo," +
											  "i.anio_consecutivo,"+
											  "c.id as cuenta,"+
											  "to_char(v.fecha_valoracion,'dd/mm/yyyy') AS fecha_valoracion," +
											  "v.hora_valoracion," +
											  "coalesce(inf.estado,'') AS estado_informe," +
											  "c.origen_admision," +
											  "sc.convenio," +
											  "con.nombre AS nombre_convenio," +
											  "CASE WHEN c.via_ingreso IS NULL THEN '' ELSE getnombreviaingreso(c.via_ingreso) END AS nombre_via_ingreso " +											  
											  "FROM " +
											  "ingresos i "+
											  "INNER JOIN manejopaciente.cuentas c ON (c.id_ingreso = i.id AND c.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+") "+
											  "INNER JOIN ordenes.solicitudes s  ON (s.cuenta = c.id AND s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialUrgencias+") "+
											  "INNER JOIN historiaclinica.valoraciones v ON (v.numero_solicitud = s.numero_solicitud) " +
											  "INNER JOIN manejopaciente.sub_cuentas sc ON (sc.ingreso = i.id) " +
											  "INNER JOIN facturacion.convenios con ON (con.reporte_atencion_ini_urg = '"+ConstantesBD.acronimoSi+"' AND con.codigo = sc.convenio) " +
											  ConstantesBD.separadorSplit+"1 informe_atencion_ini_urg inf ON (inf.cuenta = c.id AND inf.ingreso = i.id AND inf.paciente = i.codigo_paciente AND inf.convenio = sc.convenio ) " +
											  ConstantesBD.separadorSplit+"2 envio_info_aten_ini_urg env ON (env.info_atencion_ini_urg = inf.codigo_pk) " +
											  "WHERE i.institucion = ? ";
	
	/**
	 * Consulta el listado de Valoraciones No enviadas en un Informe 
	 * */
	public static String strConsultaListadoNoEnviado = 
											  "SELECT " +
											  "DISTINCT " +
											  +ConstantesBD.codigoNuncaValido+" AS codigopkinforme, "+
											  "getnombrepersona(i.codigo_paciente) AS nombre_persona,"+
											  "getidpaciente(i.codigo_paciente) AS id_persona,"+
											  "i.id,"+
											  "i.consecutivo," +
											  "i.anio_consecutivo,"+
											  "c.id as cuenta,"+
											  "to_char(v.fecha_valoracion,'dd/mm/yyyy') AS fecha_valoracion," +
											  "v.hora_valoracion," +
											  "'' AS estado_informe, "+
											  "c.origen_admision," +
											  "sc.convenio," +
											  "con.nombre AS nombre_convenio, " +
											  "CASE WHEN c.via_ingreso IS NULL THEN '' ELSE getnombreviaingreso(c.via_ingreso) END AS nombre_via_ingreso " +
											  "FROM " +
											  "ingresos i "+
											  "INNER JOIN manejopaciente.cuentas c ON (c.id_ingreso = i.id AND c.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+") "+
											  "INNER JOIN ordenes.solicitudes s  ON (s.cuenta = c.id AND s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialUrgencias+") "+
											  "INNER JOIN historiaclinica.valoraciones v ON (v.numero_solicitud = s.numero_solicitud) " +
											  "INNER JOIN manejopaciente.sub_cuentas sc ON (sc.ingreso = i.id) " +
											  "INNER JOIN facturacion.convenios con ON (con.reporte_atencion_ini_urg = '"+ConstantesBD.acronimoSi+"' AND con.codigo = sc.convenio)  " +											  
											  "WHERE i.institucion = ? AND (SELECT COUNT(s1.codigo_pk) FROM informe_atencion_ini_urg s1 WHERE s1.cuenta = c.id AND s1.ingreso = i.id AND s1.paciente = i.codigo_paciente AND s1.convenio = sc.convenio ) = 0 ";
	
	/**
	 * Inserta informacion del informe
	 * */
	public static String strInsertarInformeAtenIniUrg = 
											  " INSERT INTO informe_atencion_ini_urg " +
											  "(codigo_pk," +
											  "consecutivo," +
											  "anio_consecutivo," +
											  "estado," +
											  "fecha_generacion," +
											  "hora_generacion," +
											  "ingreso," +
											  "cuenta," +
											  "convenio," +
											  "sub_cuenta," +
											  "paciente," +
											  "causa_externa," +
											  "color_triage," +
											  "pac_viene_remitido," +
											  "institucion_solicita," +
											  "institucion," +
											  "codigo_ciudad_referencia," +
											  "codigo_depto_referencia," +
											  "codigo_pais_referencia,"+											  
											  "usuario_generacion," +
											  "motivo_consulta," +
											  "destino_paciente," +
											  "cobertura_salud," +
											  "fecha_ing_urgencias," +
											  "hora_ing_urgencias" +
											  ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	/**
	 * Inserta la informacion del envio del informe
	 * */
	public static String strInsertarEnvioInforme = 
											  "INSERT INTO envio_info_aten_ini_urg " +
											  "(" +
											  "codigo_pk," +
											  "info_atencion_ini_urg," +
											  "fecha_envio," +
											  "hora_envio," +
											  "entidad_envio," +
											  "medio_envio," +
											  "convenio_envio," +
											  "usuario_envio, " +
											  "path_archivo" +
											  ") VALUES (?,?,?,?,?,?,?,?,?) ";
	
	/**
	 * actualiza el estado del informe
	 * */
	public static String StringActualizarEstadoInforme = 
											  "UPDATE informe_atencion_ini_urg " +
											  "SET estado = ? " +
											  "WHERE codigo_pk = ? AND  estado = ? ";
	
	/**
	 * Inserta los diagnosticos
	 * */
	public static String strInsertarDiagnosticos = 
											   "INSERT INTO manejopaciente.diag_info_aten_ini_urg " +
											   "(" +
											   "info_atencion_ini_urg," +
											   "codigo_pk," +
											   "acronimo," +
											   "tipo_cie," +
											   "principal " +
											   ") VALUES (?,?,?,?,?) ";
	
	/**
	 * Consulta la informacion basica de diagnosticos
	 * */
	public static String strConsultarInfoDiagnosticos = 
												"SELECT " +
												"valoracion," +
												"acronimo_diagnostico," +
												"tipo_cie_diagnostico," +
												"principal," +
												"definitivo," +
												"numero " +
												"FROM " +
												"val_diagnosticos " +
												"WHERE " +
												"valoracion = ? ";
	
	/**
	 * Verifica que el paciente tenga valoración
	 * */
	public static String strVerificarPacienteConValoracion = 
												  "SELECT " +
												  "coalesce(con.reporte_atencion_ini_urg,'"+ConstantesBD.acronimoNo+"') as resulta " +												  												  
												  "FROM " +
												  "ingresos i "+
												  "INNER JOIN manejopaciente.cuentas c ON (c.id_ingreso = i.id AND c.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+") "+
												  "INNER JOIN ordenes.solicitudes s  ON (s.cuenta = c.id AND s.tipo = "+ConstantesBD.codigoTipoSolicitudInicialUrgencias+") "+
												  "INNER JOIN historiaclinica.valoraciones v ON (v.numero_solicitud = s.numero_solicitud) " +
												  "INNER JOIN manejopaciente.sub_cuentas sc ON (sc.ingreso = i.id) " +
												  "INNER JOIN facturacion.convenios con ON (con.codigo = sc.convenio) " +
												  "INNER JOIN historiaclinica.valoraciones_urgencias vu ON (vu.numero_solicitud = s.numero_solicitud) " +
												  "INNER JOIn conductas_valoracion cval ON (cval.codigo = vu.codigo_conducta_valoracion) " +												  											 
												  "WHERE i.institucion = ? AND i.codigo_paciente = ? ";
	
	/**
	 * consulta los convenios del paciente
	 * */
	public static String strConsultarConveniosPaciente = 
													"SELECT "+
													"convenio,"+
													"getnombreconvenio(convenio) AS nombre_convenio,"+
													"coalesce(c.reporte_atencion_ini_urg,'"+ConstantesBD.acronimoNo+"') AS  reporte_atencion_ini_urg "+
													"FROM "+
													"sub_cuentas " +
													"INNER JOIN convenios c ON (c.codigo = convenio) "+
													"WHERE "+ 
													"ingreso = ? ";
	
	/**
	 * Consulta el listado de convenios del paciente que poseen o no informe
	 * */
	public static String strConsultarConveniosPacienteReporteInci = " " +
			"SELECT " +
			"s.convenio," +
			"getnombreconvenio(s.convenio) AS nombre_convenio,"+
			"coalesce(c.reporte_atencion_ini_urg,'"+ConstantesBD.acronimoNo+"') AS  reporte_atencion_ini_urg, "+
			"coalesce((SELECT s1.estado " +
			"FROM informe_atencion_ini_urg s1 " +
			"WHERE s1.cuenta = ? AND s1.ingreso = s.ingreso " +
			"AND s1.convenio = c.codigo AND s1.sub_cuenta = s.sub_cuenta ),'') AS estadoinforme "+
			"FROM "+
			"sub_cuentas s " +
			"INNER JOIN convenios c ON (c.codigo = convenio ) "+
			"WHERE "+ 
			"s.ingreso = ? " +
			"ORDER BY nombre_convenio ASC ";
	
	/**
	 * Consulta el informe de la atencion inical de urgencias 
	 */
	public static String strConsultaInformeIniUrg = "SELECT " +
					"inf.codigo_pk AS codigoPKInforme, " +
					"inf.consecutivo AS consecutivo_ingreso, " +
					"inf.estado AS estado, " +
					"to_char(inf.fecha_generacion,'dd/mm/yyyy') AS fecha_generacion, " + 
					"inf.hora_generacion AS hora_generacion, " +
					"con.nombre AS nombre_convenio, " +
					"con.codigo_min_salud AS cod_min_salud_convenio, " +
					"inf.cobertura_salud AS cod_cobertura_salud, " +
					"cors.nombre AS nombre_cobertura_salud, " +
					"inf.causa_externa AS cod_causa_externa, " +
					"ext.nombre AS causa_externa, " +
					"inf.color_triage AS cod_color_triage, " + 
					"coalesce(ct.descripcion, '') AS color_triage, " +
					"to_char(inf.fecha_ing_urgencias,'dd/mm/yyyy') AS fecha_ing_urgencias, " +
					"inf.hora_ing_urgencias AS hora_ing_urgencias, " +
					"coalesce(inf.pac_viene_remitido, '') AS pac_viene_remitido, " +
					"coalesce(inf.motivo_consulta, '') AS motivo_consulta, " +
					"inf.destino_paciente AS cod_destino_paciente, " +
					"des.nombre AS destino_paciente, " +
					"CASE WHEN inf.usuario_generacion IS NULL THEN '' ELSE getnombreusuario2(inf.usuario_generacion) END AS nombre_usua_gene, " +
					"CASE WHEN inf.usuario_generacion IS NULL THEN '' ELSE manejopaciente.gettelefonopersona(inf.usuario_generacion) END AS telfono_usua_gene, " +
					"CASE WHEN inf.usuario_generacion IS NULL THEN '' ELSE manejopaciente.gettelefonocelularpersona(inf.usuario_generacion) END AS telfono_celular_usua_gene, " +
					"CASE WHEN usa.cargo IS NULL THEN '' ELSE getnomcargousuario(usa.cargo) END AS nombre_cargo " +
					"FROM informe_atencion_ini_urg inf  " +
					"INNER JOIN convenios con ON (inf.convenio = con.codigo) " + 
					"INNER JOIN coberturas_salud cors ON (inf.cobertura_salud = cors.codigo) " + 
					"INNER JOIN destino_pac_conducta des ON (inf.destino_paciente = des.codigo_pk) " +  
					"INNER JOIN causas_externas ext ON (inf.causa_externa = ext.codigo) " +
					"LEFT OUTER JOIN colores_triage ct ON (inf.color_triage = ct.codigo) " +
					"INNER JOIN usuarios usa ON (usa.login = inf.usuario_generacion) " +
					"WHERE inf.codigo_pk = ? ";
	
	public static String strConsultaInsRemite = "SELECT " +
					"ints.descripcion AS razon_social, " +
					"ints.codigo AS cod_min_salud, " +
					"ciu.descripcion AS ciudad, " +
					"depto.descripcion AS departamento, " +
					"paises.descripcion AS pais " +
					"FROM informe_atencion_ini_urg inf " +
					"INNER JOIN instituciones_sirc ints on ints.codigo = inf.institucion_solicita " +
					"INNER JOIN paises paises on (paises.codigo_pais = inf.codigo_pais_referencia) " +
					"INNER JOIN departamentos depto on (depto.codigo_departamento = inf.codigo_depto_referencia and depto.codigo_pais = inf.codigo_pais_referencia) " +
					"INNER JOIN ciudades ciu on (ciu.codigo_ciudad= inf.codigo_ciudad_referencia  and ciu.codigo_departamento = inf.codigo_depto_referencia and ciu.codigo_pais = inf.codigo_pais_referencia) " +
					"WHERE inf.codigo_pk = ? ";
	
	public static String strConsultaDiagInfoIniUrg = "SELECT " + 
					"dinf.info_atencion_ini_urg AS info_atencion_ini_urg, " +
					"dinf.codigo_pk AS codigopk, " +
					"diag.acronimo AS acronimo, " +
					"diag.tipo_cie AS tipocie, " +
					"coalesce(diag.nombre,'') AS nombre, " +
					"dinf.principal AS principal " +
					"FROM diagnosticos diag " +
					"INNER JOIN diag_info_aten_ini_urg  dinf ON (dinf.tipo_cie = diag.tipo_cie " +
					"AND dinf.acronimo = diag.acronimo) " +
					"WHERE info_atencion_ini_urg = ?  ORDER BY diag.es_principal "+ConstantesBD.tipoOrdenamientoDescendente;
	//********************************************************************************
	 
	/**
	 * Listado de Informe Atencion Inicial de Urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */

	public static ArrayList<DtoInformeAtencionIniUrg> getListadoInformeInicUrge(Connection con, HashMap parametros)
	{
		ArrayList<DtoInformeAtencionIniUrg> array = new ArrayList<DtoInformeAtencionIniUrg>();
		
		String cadena = strConsultaListado;		
		boolean indicadorInf = false;
		boolean indicadorEnvio = false;		
				
		//valida el filtro de Informes No Generados
		if(parametros.containsKey("informesNoGen") && 
				parametros.get("informesNoGen").toString().equals(ConstantesBD.acronimoSi))
		{
			cadena = strConsultaListadoNoEnviado;
		}
		else
		{
			if(parametros.containsKey("fechaInicialGeneracion") && 
					parametros.containsKey("fechaFinalGeneracion") && 
						!parametros.get("fechaInicialGeneracion").toString().equals("") && 
							!parametros.get("fechaFinalGeneracion").toString().equals(""))
			{
				cadena += " AND inf.fecha_generacion BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialGeneracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalGeneracion").toString())+"' ";
				indicadorInf = true;
			}
			
			if(parametros.containsKey("estadoEnvio") && 
					!parametros.get("estadoEnvio").toString().equals("") && 
						!parametros.get("estadoEnvio").toString().equals(ConstantesIntegridadDominio.acronimoAmbos))
			{
				cadena += " AND inf.estado = '"+parametros.get("estadoEnvio").toString()+"' ";
				indicadorInf = true;				
			}
			
			if(parametros.containsKey("fechaInicialEnvio") && 
					parametros.containsKey("fechaFinalEnvio") && 
						!parametros.get("fechaInicialEnvio").equals("") && 
							!parametros.get("fechaFinalEnvio").equals(""))
			{
				cadena += " AND env.fecha_envio BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialEnvio").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalEnvio").toString())+"' ";
				indicadorInf = true;
				indicadorEnvio = true;
			}	
			
			//evalua el nivel del join con las tablas de envios
			if(indicadorInf)
			{
				cadena = cadena.replace(ConstantesBD.separadorSplit+"1","INNER JOIN");				
				if(indicadorEnvio)
					cadena = cadena.replace(ConstantesBD.separadorSplit+"2"," INNER JOIN ");
				else
					cadena = cadena.replace(ConstantesBD.separadorSplit+"2"," LEFT OUTER JOIN ");
			}
			else
			{
				cadena = cadena.replace(ConstantesBD.separadorSplit+"1","LEFT OUTER JOIN");
				cadena = cadena.replace(ConstantesBD.separadorSplit+"2","LEFT OUTER JOIN");
			}
		}
		
		if(parametros.containsKey("fechaInicialValoracion") && 
				parametros.containsKey("fechaFinalValoracion") && 
					!parametros.get("fechaInicialValoracion").toString().equals("") && 
						!parametros.get("fechaFinalValoracion").toString().equals(""))
			cadena += " AND v.fecha_valoracion BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialValoracion").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalValoracion").toString())+"' ";
				
		if(parametros.containsKey("convenio") && 
				!parametros.get("convenio").toString().equals(""))
			cadena += " AND con.codigo = "+parametros.get("convenio").toString()+" ";
		
		cadena+=" ORDER BY fecha_valoracion,nombre_persona ASC ";
							
		try
		{
			logger.info("\n\nCadena de consulta >> "+cadena);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoInformeAtencionIniUrg dto = new DtoInformeAtencionIniUrg();
				dto.setCodigoPk(rs.getInt("codigopkinforme"));
				dto.setNombrePersona(rs.getString("nombre_persona"));
				dto.setIdPersona(rs.getString("id_persona"));
				dto.setIdIngreso(rs.getInt("id"));
				dto.setCodigoCuenta(rs.getInt("cuenta"));
				dto.setFechaValoracion(rs.getString("fecha_valoracion"));
				dto.setHoraValoracion(rs.getString("hora_valoracion"));
				dto.setEstadoInforme(rs.getString("estado_informe"));
				dto.setOrigenAdmision(rs.getString("origen_admision"));
				dto.setCodigoConvenio(rs.getInt("convenio"));
				dto.setDescripcionConvenio(rs.getString("nombre_convenio"));
				dto.setAnioConsecutivoIngreso(rs.getString("anio_consecutivo"));
				dto.setConsecutivoIngreso(rs.getString("consecutivo"));
				dto.setDescripcionViaIngreso(rs.getString("nombre_via_ingreso"));
				
				array.add(dto);
			}			
		}
		catch (Exception e) {			
			//e.printStackTrace();
			logger.info("error en getListadoInformeInicUrge >> "+parametros+" cadena >> "+cadena+" ");
			
		}
		
		return array;
	}
	
	//**********************************************************************************
	
	/**
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static ArrayList<DtoInformeAtencionIniUrg> getConveniosPacienteReporte(Connection con, HashMap parametros)
	{
		ArrayList<DtoInformeAtencionIniUrg> array = new ArrayList<DtoInformeAtencionIniUrg>(); 
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultarConveniosPacienteReporteInci,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("cuenta").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("ingreso").toString()));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoInformeAtencionIniUrg dto = new DtoInformeAtencionIniUrg();
				dto.setCodigoConvenio(rs.getInt("convenio"));
				dto.setDescripcionConvenio(rs.getString("nombre_convenio"));
				dto.setIndConvInfIniUrg(UtilidadTexto.getBoolean(rs.getString("reporte_atencion_ini_urg")));
				dto.setEstadoInforme(rs.getString("estadoinforme"));
				array.add(dto);
			}
			
			rs.close();
			ps.close();
		}
		catch (Exception e) {
			logger.info("error en getConveniosPacienteReporte >>>>>>> "+parametros+" cadena >> "+strConsultarConveniosPacienteReporteInci+" "+e);
			
		}
		return array;
	}
	
	//**********************************************************************************
	  	  
	/**
	 * Listado de Informe Atencion Inicial de Urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static ArrayList<DtoInformeAtencionIniUrg> getListadoInformeInicUrgeXPaciente(Connection con, HashMap parametros)
	{
		ArrayList<DtoInformeAtencionIniUrg> array = new ArrayList<DtoInformeAtencionIniUrg>();
		String cadena = strConsultaListadoPaciente;
		PreparedStatementDecorator ps;
		
		try
		{
			if(parametros.containsKey("soloConInforme") && 
					parametros.get("soloConInforme").toString().equals(ConstantesBD.acronimoSi))
			{
				cadena = cadena.replace(ConstantesBD.separadorSplit+"1","INNER JOIN ");
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
				ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
				ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoPaciente").toString()));
			}			
			else
			{
				cadena = strConsultaListadoPaciente2;
				
				ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
				ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
				ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoPaciente").toString()));
				ps.setInt(3,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
				ps.setInt(4,Utilidades.convertirAEntero(parametros.get("codigoPaciente").toString()));
			}
						
			logger.info("getListadoInformeInicUrgeXPaciente >> "+parametros+" cadena >> "+cadena+" ");		
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoInformeAtencionIniUrg dto = new DtoInformeAtencionIniUrg();
				dto.setCodigoPk(rs.getInt("codigopkinforme"));
				dto.setNombrePersona(rs.getString("nombre_persona"));
				dto.setIdPersona(rs.getString("id_persona"));
				dto.setIdIngreso(rs.getInt("id"));
				dto.setConsecutivoIngreso(rs.getString("consecutivo"));
				dto.setAnioConsecutivoIngreso(rs.getString("anio_consecutivo"));
				dto.setFechaIngreso(rs.getString("fecha_ingreso"));
				dto.setHoraIngreso(rs.getString("hora_ingreso"));
				dto.setFechaEgreso(rs.getString("fecha_egreso"));
				dto.setHoraEgreso(rs.getString("hora_egreso"));				
				dto.setCodigoCuenta(rs.getInt("cuenta"));
				dto.setDescripcionEstadoCuenta(rs.getString("nombre_cuenta"));			
				dto.setEstadoInforme(rs.getString("estado_informe"));
				dto.setOrigenAdmision(rs.getString("origen_admision"));
				dto.setNombreCentroAtencion(rs.getString("nombre_centroatencion"));
				dto.setDescripcionViaIngreso(rs.getString("nombre_via_ingreso"));
				dto.setDescripcionConvenio(rs.getString("nombre_convenio_responsable"));
				
				array.add(dto);
			}			
		}
		catch (Exception e) {			
			//e.printStackTrace();
			logger.info("error en getListadoInformeInicUrgeXPaciente >> "+parametros+" cadena >> "+cadena+" ");
			
		}
		
		return array;
	}
	
	//**********************************************************************************
	
	/**
	 * Listado de Informe Atencion Inicial de Urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static ArrayList<DtoInformeAtencionIniUrg> getListadoInformeInicUrgeXPacienteIngreso(Connection con, HashMap parametros)
	{
		ArrayList<DtoInformeAtencionIniUrg> array = new ArrayList<DtoInformeAtencionIniUrg>();
		String cadena = strConsultaListadoPacienteIngreso;
		PreparedStatementDecorator ps;
		
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoPaciente").toString()));
						
			logger.info("getListadoInformeInicUrgeXPacienteIngreso >> "+parametros+" cadena >> "+cadena+" ");
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoInformeAtencionIniUrg dto = new DtoInformeAtencionIniUrg();
				dto.setCodigoPk(rs.getInt("codigopkinforme"));
				dto.setNombrePersona(rs.getString("nombre_persona"));
				dto.setIdPersona(rs.getString("id_persona"));
				dto.setIdIngreso(rs.getInt("id"));
				dto.setConsecutivoIngreso(rs.getString("consecutivo"));
				dto.setAnioConsecutivoIngreso(rs.getString("anio_consecutivo"));
				dto.setFechaIngreso(rs.getString("fecha_ingreso"));
				dto.setHoraIngreso(rs.getString("hora_ingreso"));
				dto.setFechaEgreso(rs.getString("fecha_egreso"));
				dto.setHoraEgreso(rs.getString("hora_egreso"));				
				dto.setCodigoCuenta(rs.getInt("cuenta"));
				dto.setDescripcionEstadoCuenta(rs.getString("nombre_cuenta"));			
				dto.setEstadoInforme(rs.getString("estado_informe"));
				dto.setOrigenAdmision(rs.getString("origen_admision"));
				dto.setNombreCentroAtencion(rs.getString("nombre_centroatencion"));
				dto.setDescripcionViaIngreso(rs.getString("nombre_via_ingreso"));
				dto.setDescripcionConvenio(rs.getString("nombre_convenio_responsable"));
				
				array.add(dto);
			}
		}
		catch (Exception e) {
			logger.info("error en getListadoInformeInicUrgeXPaciente >> "+parametros+" cadena >> "+cadena+" "+e);
		}
		
		return array;
	}
	
	//**********************************************************************************
	
	
	/**
	 * Carga la informacion basica del paciente
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static DtoInformeAtencionIniUrg cargarInfoPaciente(Connection con,HashMap parametros)
	{
		DtoInformeAtencionIniUrg dto = new DtoInformeAtencionIniUrg();
		
		String cadena = strConsultaInfoPaciente;
		String cadena2 = strConsultaEnviosInforme;
		boolean indInn = false;
		boolean indicadorEnvio = false;		
		
		if(parametros.containsKey("ingreso") && 
				!parametros.get("ingreso").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("ingreso").toString()) > 0)
			cadena += " AND i.id = "+parametros.get("ingreso").toString();
		
		if(parametros.containsKey("cuenta") && 
				!parametros.get("cuenta").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("cuenta").toString()) > 0)
			cadena += " AND c.id = "+parametros.get("cuenta").toString();
		
		if(parametros.containsKey("codigoPkInforme") && 
				!parametros.get("codigoPkInforme").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("codigoPkInforme").toString()) > 0)
		{
			cadena += " AND inf.codigo_pk = "+parametros.get("codigoPkInforme").toString();
			indInn = true;
		}
		
		if(parametros.containsKey("fechaInicialEnvio") && 
				parametros.containsKey("fechaFinalEnvio") && 
					!parametros.get("fechaInicialEnvio").equals("") && 
						!parametros.get("fechaFinalEnvio").equals(""))
		{
			cadena += " AND env.fecha_envio BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialEnvio").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalEnvio").toString())+"' ";
			indInn = true;
			indicadorEnvio = true;
		}
		
		if(parametros.containsKey("convenio") && 
				Utilidades.convertirAEntero(parametros.get("convenio").toString()) >= 0)
			cadena += " AND sc.convenio = "+parametros.get("convenio").toString()+" ";
									
		try
		{
			if(indInn)
			{
				cadena = cadena.replace(ConstantesBD.separadorSplit+"1"," INNER JOIN ");
				
				if(indicadorEnvio)
					cadena = cadena.replace(ConstantesBD.separadorSplit+"2"," INNER JOIN  ");
				else
					cadena = cadena.replace(ConstantesBD.separadorSplit+"2"," LEFT OUTER JOIN ");
			}
			else
			{
				cadena = cadena.replace(ConstantesBD.separadorSplit+"1"," LEFT OUTER JOIN ");
				cadena = cadena.replace(ConstantesBD.separadorSplit+"2"," LEFT OUTER JOIN ");
			}
			
			logger.info("\n\nCadena de consulta >> "+cadena);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				dto.setCodigoPk(rs.getInt("codigopkinforme"));
				dto.setNombrePersona(rs.getString("nombre_persona"));
				dto.setIdPersona(rs.getString("id_persona"));
				dto.setCodigoPersona(rs.getString("codigo_paciente"));
				dto.setIdIngreso(rs.getInt("id"));
				dto.setCodigoCuenta(rs.getInt("cuenta"));			
				dto.setEstadoInforme(rs.getString("estado_informe"));
				dto.setOrigenAdmision(rs.getString("origen_admision"));
				dto.setPoseeReferencia(rs.getInt("tiene_referencia")>0?true:false);
				dto.setCodigoConvenio(rs.getInt("convenio"));
				dto.setDescripcionConvenio(rs.getString("nombre"));
				dto.setAnioConsecutivoIngreso(rs.getString("anio_consecutivo"));
				dto.setConsecutivoIngreso(rs.getString("consecutivo"));
								
				if(dto.getCodigoPk() > 0)
				{
					if(parametros.containsKey("fechaInicialEnvio") && 
							parametros.containsKey("fechaFinalEnvio") && 
								!parametros.get("fechaInicialEnvio").equals("") && 
									!parametros.get("fechaFinalEnvio").equals(""))
						cadena2 += " AND fecha_envio BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicialEnvio").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinalEnvio").toString())+"' ";
					
					cadena2 += " ORDER BY fecha_envio,hora_envio DESC ";
					logger.info("Consulta 2: "+cadena2 );
					ps =  new PreparedStatementDecorator(con.prepareStatement(cadena2,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1,dto.getCodigoPk());
					ResultSetDecorator rs1 =new ResultSetDecorator(ps.executeQuery());
					
					while(rs1.next())
					{
						DtoEnvioInfoAtenIniUrg dtoe = new DtoEnvioInfoAtenIniUrg();
						
						dtoe.setCodigoPk(rs1.getInt("codigo_pk"));
						dtoe.setCodigoInfoAtencioIniUrg(rs1.getInt("info_atencion_ini_urg"));
						dtoe.setFechaEnvio(rs1.getString("fecha_envio"));
						dtoe.setHoraEnvio(rs1.getString("hora_envio"));
						dtoe.setCodigoEntidadEnvio(rs1.getInt("entidad_envio"));
						dtoe.setMedioEnvio(rs1.getString("medio_envio"));
						dtoe.setUrlArchivoIncoXmlDes(rs1.getString("patharchivo"));
						dtoe.setCodigoConvenioEnvio(rs1.getInt("convenio_envio"));
						dtoe.setNombreConvenioEnvio(rs1.getString("nombre_convenio_envio"));
						dtoe.setNombreEntidadEnvio(rs1.getString("nombre_entidad_envio"));
						dtoe.setNombreUsuarioEnvio(rs1.getString("nombre_usuario"));
						
						dto.getHistorialEnvios().add(dtoe);						
					}					
				}
				
				//Carga los convenios de un paciente
				if(parametros.containsKey("cargarConvenio") && 
						parametros.get("cargarConvenio").toString().equals(ConstantesBD.acronimoSi))
				{
					logger.info("consutla 3: "+strConsultarConveniosPaciente);
					ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultarConveniosPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1,dto.getIdIngreso());					
					ResultSetDecorator rs2 =new ResultSetDecorator(ps.executeQuery());
					
					while(rs2.next())
					{
						InfoDatosString datos = new InfoDatosString();
						datos.setCodigo(rs2.getString("convenio"));
						datos.setActivo(UtilidadTexto.getBoolean(rs2.getString("reporte_atencion_ini_urg")));
						datos.setNombre(rs2.getString("nombre_convenio"));
						dto.getConveniosPaciente().add(datos);																		
					}
				}				
			}
		}
		catch (Exception e) {			
			//e.printStackTrace();
			logger.info("error en cargarInfoPaciente >> "+parametros+" cadena >> "+cadena+" "+" "+cadena2);			
		}
		
		return dto;
	}
	
	//**********************************************************************************
	
	/**
	 * Inserta informacion del informe atencion inicial de urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap insertarInformeAtencionIniUrg(Connection con,HashMap parametros)
	{
		String cadena = strConsultaInfoGeneracionReporte;
		int codigo = ConstantesBD.codigoNuncaValido, tmp = ConstantesBD.codigoNuncaValido;
		ActionErrors errores = new ActionErrors();
		HashMap resultado = new HashMap();
		resultado.put("codigoPk",ConstantesBD.codigoNuncaValido+"");
		resultado.put("error",errores);		
		
		if(parametros.containsKey("ingreso") && 
				!parametros.get("ingreso").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("ingreso").toString()) > 0)
			cadena += " AND i.id = "+parametros.get("ingreso").toString();
		
		if(parametros.containsKey("cuenta") && 
				!parametros.get("cuenta").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("cuenta").toString()) > 0)
			cadena += " AND c.id = "+parametros.get("cuenta").toString();
		
		if(parametros.containsKey("convenio") && 
				!parametros.get("convenio").toString().equals("") && 
					Utilidades.convertirAEntero(parametros.get("convenio").toString()) > 0)
			cadena += " AND sc.convenio = "+parametros.get("convenio").toString();
		
		try
		{
			codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_inf_aten_ini_urg");
			String valorConsecutivoInfo=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoInformeAtencIniUrg,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			String anioConsecutivoInfo=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoInformeAtencIniUrg,Utilidades.convertirAEntero(parametros.get("institucion").toString()),valorConsecutivoInfo);
			
			if(Utilidades.convertirAEntero(valorConsecutivoInfo) < 0)
			{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","Debe Inicializar el consecutivo [Informe Atención Inicial de Urgencias] ubicado en el modulo de Administración."));
				resultado.put("error",errores);
				return resultado;
			}
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery()); 
			
			if(rs.next())
			{
				ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarInformeAtenIniUrg,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,codigo);
				ps.setInt(2,Utilidades.convertirAEntero(valorConsecutivoInfo));
				
				if(Utilidades.convertirAEntero(anioConsecutivoInfo) >= 0)
					ps.setInt(3,Utilidades.convertirAEntero(anioConsecutivoInfo));
				else
					ps.setNull(3,Types.INTEGER);
				
				ps.setString(4,ConstantesIntegridadDominio.acronimoEstadoGenerado);				
				ps.setDate(5,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
				ps.setString(6,UtilidadFecha.getHoraActual());
				ps.setInt(7,rs.getInt("ingreso"));
				ps.setInt(8,rs.getInt("cuenta"));
				ps.setInt(9,rs.getInt("convenio"));
				ps.setInt(10,rs.getInt("sub_cuenta"));
				ps.setInt(11,rs.getInt("codigo_paciente"));
				
				if(rs.getInt("causa_externa") >= 0)
					ps.setInt(12,rs.getInt("causa_externa"));
				else
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Debe Ingresar la Información de la Causa Externa en la Valoración."));
					resultado.put("error",errores);
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoInformeAtencIniUrg,Utilidades.convertirAEntero(parametros.get("institucion").toString()), valorConsecutivoInfo,ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
					return resultado;
				}
				
				if(rs.getInt("color") >= 0)
					ps.setInt(13,rs.getInt("color"));
				else
					ps.setNull(13,Types.INTEGER);
				
				ps.setString(14,rs.getInt("origen_admision")==ConstantesBD.codigoOrigenAdmisionHospitalariaEsRemitido?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				
				if(!rs.getString("institucion_sirc_solicita").equals(""))
					ps.setString(15,rs.getString("institucion_sirc_solicita"));
				else
					ps.setNull(15,Types.VARCHAR);
				
				if(rs.getInt("institucion") >= 0)
					ps.setInt(16,rs.getInt("institucion"));
				else
					ps.setNull(16,Types.INTEGER);
				
				if(!rs.getString("codigo_ciudad").equals(""))
					ps.setString(17,rs.getString("codigo_ciudad"));
				else
					ps.setNull(17,Types.VARCHAR);
				
				if(!rs.getString("codigo_departamento").equals(""))
					ps.setString(18,rs.getString("codigo_departamento"));
				else
					ps.setNull(18,Types.VARCHAR);
				
				if(!rs.getString("codigo_pais").equals(""))
					ps.setString(19,rs.getString("codigo_pais"));
				else
					ps.setNull(19,Types.VARCHAR);
				
				ps.setString(20,parametros.get("usuarioLogin").toString());
				
				if(!rs.getString("motivo_consulta").equals(""))
					ps.setString(21,rs.getString("motivo_consulta"));
				else
					ps.setNull(21,Types.VARCHAR);
							
				if(rs.getInt("origen_admision")==ConstantesBD.codigoOrigenAdmisionHospitalariaEsRemitido && 
						rs.getInt("codigo_conducta_valoracion") == ConstantesBD.codigoConductaSeguirRemitirMayorComplejidad)
					tmp = ConstantesBD.codigoDestinoPacContrarremision;
				else
					tmp = rs.getInt("destino_paciente");
				
				if(tmp >= 0)
					ps.setInt(22,tmp);
				else
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Debe Parametrizar el Destino Paciente en las Conductas a Seguir de la Valoración."));
					resultado.put("error",errores);
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoInformeAtencIniUrg,Utilidades.convertirAEntero(parametros.get("institucion").toString()), valorConsecutivoInfo,ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
					return resultado;
				}	
				
				if(rs.getInt("tipo_cobertura") >= 0)
					ps.setInt(23,rs.getInt("tipo_cobertura"));
				else
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Debe Ingresar el Tipo de Cobertura para el Responsable. "));
					resultado.put("error",errores);
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoInformeAtencIniUrg,Utilidades.convertirAEntero(parametros.get("institucion").toString()), valorConsecutivoInfo,ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
					return resultado;
				}
				
				ps.setDate(24,Date.valueOf(rs.getString("fecha_ingreso")));
				ps.setString(25,rs.getString("hora_ingreso"));				
			}
			else
			{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","No se encontro la Información Requerida para la Generación del Reporte."));
				resultado.put("error",errores);
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoInformeAtencIniUrg,Utilidades.convertirAEntero(parametros.get("institucion").toString()), valorConsecutivoInfo,ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				return resultado;
			}
			
			if(ps.executeUpdate()>0)
			{
				if(insertarDiagnosticosInforme(con,rs.getInt("numero_solicitud"),codigo) > 0)
				{
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoInformeAtencIniUrg,Utilidades.convertirAEntero(parametros.get("institucion").toString()), valorConsecutivoInfo,ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
					resultado.put("codigoPk",codigo);
				}
				else
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","No Fue Posible Ingresar los Diagnosticos"));
					resultado.put("error",errores);
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoInformeAtencIniUrg,Utilidades.convertirAEntero(parametros.get("institucion").toString()), valorConsecutivoInfo,ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
					return resultado;
				}
			}
		}
		catch (Exception e) 
		{
			//e.printStackTrace();
			logger.info("\n\n parametros >> "+parametros+" \n "+cadena);
			logger.info("error >> "+e);
		}
		
		return resultado;
	}	
	
	//**********************************************************************************
	
	/**
	 * Inserta los diagnosticos del informe 
	 * @param Connection con
	 * @param int valoracion
	 * @param int codigoInforme
	 * */
	public static int insertarDiagnosticosInforme(Connection con,int valoracion,int codigoInforme)
	{
		int codigo = ConstantesBD.codigoNuncaValido;
		boolean indInser = false;
		
		try
		{			
			PreparedStatementDecorator ps  =  new PreparedStatementDecorator(con.prepareStatement(strConsultarInfoDiagnosticos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,valoracion);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_diag_inf_ate_ini_urg");
				ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarDiagnosticos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,codigoInforme);
				ps.setInt(2,codigo);
				ps.setString(3,rs.getString("acronimo_diagnostico"));
				ps.setString(4,rs.getString("tipo_cie_diagnostico"));
				ps.setString(5,UtilidadTexto.getBoolean(rs.getString("principal"))?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				
				if(ps.executeUpdate() > 0)
					indInser = true;
				else
					return ConstantesBD.codigoNuncaValido;			
			}
		}
		catch (Exception e) {
			//e.printStackTrace();
			logger.info("error en insertarDiagnosticosInforme >> ");
			logger.info("error >> "+e);
		}
		
		if(indInser)
			return 1;
		else
			return ConstantesBD.codigoNuncaValido;			
	}
	
	//**********************************************************************************
	
	/**
	 * Inserta informacion del envio del informe de atencion inicial de urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static int insertarEnvioInformeAtencionIniUrg(Connection con,HashMap parametros)
	{
		int codigo = ConstantesBD.codigoNuncaValido;
		
		try
		{
			codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_env_inf_ate_ini_urg");			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strInsertarEnvioInforme,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,codigo);
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("infoAtencionIniUrg").toString()));
			ps.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			ps.setString(4,UtilidadFecha.getHoraActual());
			
			if(!parametros.get("entidadEnvio").toString().equals(""))
				ps.setInt(5,Utilidades.convertirAEntero(parametros.get("entidadEnvio").toString()));
			else
				ps.setNull(5,Types.INTEGER);
			
			ps.setString(6,parametros.get("medioEnvio").toString());
			
			if(!parametros.get("convenioEnvio").toString().equals(""))
				ps.setInt(7,Utilidades.convertirAEntero(parametros.get("convenioEnvio").toString()));
			else
				ps.setNull(7,Types.INTEGER);			
			
			ps.setString(8,parametros.get("usuarioEnvio").toString());
			
			if(!parametros.get("pathArchivo").toString().equals(""))
				ps.setString(9, parametros.get("pathArchivo").toString());
			else
				ps.setNull(9, Types.VARCHAR);
			
			if(ps.executeUpdate()>0)
			{
				//cambia el estado del informe a enviado
				ps =  new PreparedStatementDecorator(con.prepareStatement(StringActualizarEstadoInforme,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1,ConstantesIntegridadDominio.acronimoEstadoEnviado);
				ps.setInt(2,Utilidades.convertirAEntero(parametros.get("infoAtencionIniUrg").toString()));
				ps.setString(3,ConstantesIntegridadDominio.acronimoEstadoGenerado);
				
				if(ps.executeUpdate()<0)
					logger.info("NO SE actualizo el estado del informe");
				else
					logger.info("valores "+codigo);
					
				return codigo;
			}
		}
		catch (Exception e) 
		{
			//e.printStackTrace();
			logger.info("consulta: "+strInsertarEnvioInforme+"\n\n parametros >> "+parametros+" \n ");
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	//**********************************************************************************
	
	/**
	 * Verifica si el paciente posee ingresos con valoraciones de urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static InfoDatosString tienePacienteIngresoValoracionUrg(Connection con, HashMap parametros)
	{
		boolean tieneVal = false;
		boolean tieneConv = false;
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strVerificarPacienteConValoracion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoPaciente").toString()));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{				
				tieneVal = true;
				if(rs.getString("resulta").toString().equals(ConstantesBD.acronimoSi))
					tieneConv = true;			
			}			
		}
		catch (Exception e) {
			logger.info("error en tienePacienteIngresoValoracionUrg "+parametros+" "+e);
		}
				
		return new InfoDatosString(tieneVal+"",tieneConv+"");
	}	
	
	
	//**********************************************************************************	
	  
	/**
	 * Informe Atencion Inicial de Urgencias
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static DtoInformeAtencionIniUrg getInformeInicUrge(Connection con, HashMap parametros)
	{
		DtoInformeAtencionIniUrg dto= new DtoInformeAtencionIniUrg(); 
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaInformeIniUrg,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("cod_informe").toString()));
			
			
			logger.info("consulta >>>>>>> "+strConsultaInformeIniUrg+" codigo>>>>>"+parametros.get("cod_informe").toString());
			
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			rs.next();
			dto.setCodigoPk(rs.getInt("codigoPKInforme"));
			dto.setConsecutivoIngreso(rs.getString("consecutivo_ingreso"));
			dto.setEstadoInforme(rs.getString("estado"));
			dto.setFechaGeneracion(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_generacion")));
			dto.setHoraGeneracion(rs.getString("hora_generacion"));
			//se adiciona el convenio
			dto.getConveniosPaciente().add(new InfoDatosString(rs.getString("cod_min_salud_convenio"),rs.getString("nombre_convenio")));
			dto.setCoberturaSalud(new InfoDatosInt(rs.getInt("cod_cobertura_salud"),rs.getString("nombre_cobertura_salud")));
			dto.setCausaExterna(new InfoDatosInt(rs.getInt("cod_causa_externa"),rs.getString("causa_externa")));
			dto.setColorTriage(new InfoDatosInt(rs.getInt("cod_color_triage"),rs.getString("color_triage")));
			dto.setFechaIngUrgencias(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fecha_ing_urgencias")));
			dto.setHoraIngUrgencias(rs.getString("hora_ing_urgencias"));
			dto.setPacVieneRemitido(rs.getString("pac_viene_remitido"));
			dto.setMotivoConsulta(rs.getString("motivo_consulta"));
			dto.setDestinoPaciente(new InfoDatosInt(rs.getInt("cod_destino_paciente"),rs.getString("destino_paciente")));
			dto.setNombreUsuarioGenera(rs.getString("nombre_usua_gene"));
			dto.setTelefono(rs.getString("telfono_usua_gene"));
			dto.setTelefonoCelular(rs.getString("telfono_celular_usua_gene"));
			dto.setCargo(rs.getString("nombre_cargo"));
			
			// se carga carga la  informacion de quien remite si es el caso
			if(!dto.getPacVieneRemitido().equals("") && !dto.getPacVieneRemitido().contains("N"))
			{
				logger.info("El paciente fue remitido y se debe cargar Informacion Adicional");
				dto.setInstitucionRemite(getInstitucionRemite(con,parametros));
			}
			
			// cargar los diagnosticos del informe inicial de urgencias
			dto.setDiagInfIniUrg(getDiagInfoIniUrg(con,parametros));
			
			rs.close();
			ps.close();
		}
		catch (Exception e) {			
			//e.printStackTrace();
			logger.info("error en getInformeInicUrge >>>>>>> "+parametros+" cadena >> "+strConsultaInformeIniUrg+" ");
			
		}
		return dto;
	}
	
	
	public static HashMap<String,Object> getInstitucionRemite(Connection con, HashMap parametros)
	{
		HashMap<String,Object> instremite = new HashMap<String,Object>(); 
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaInsRemite,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("cod_informe").toString()));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			rs.next();
			instremite.put("razon_social",rs.getString("razon_social"));
			instremite.put("cod_min_salud",rs.getString("cod_min_salud"));
			instremite.put("ciudad",rs.getString("ciudad"));
			instremite.put("departamento",rs.getString("departamento"));
			instremite.put("pais",rs.getString("pais"));
			rs.close();
			ps.close();
		}
		catch (Exception e) {			
			//e.printStackTrace();
			logger.info("error en getInstitucionRemite >>>>>>> "+parametros+" cadena >> "+strConsultaInsRemite+" ");
			
		}
		return instremite;
	}
	
	public static ArrayList<DtoDiagInfoIniUrg> getDiagInfoIniUrg(Connection con, HashMap parametros)
	{
		ArrayList<DtoDiagInfoIniUrg> array = new ArrayList<DtoDiagInfoIniUrg>(); 
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaDiagInfoIniUrg,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("cod_informe").toString()));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoDiagInfoIniUrg diagInfoIniUrg = new DtoDiagInfoIniUrg();
				diagInfoIniUrg.setInfIniUrg(rs.getString("info_atencion_ini_urg"));
				diagInfoIniUrg.setCodigoPK(rs.getString("codigopk"));
				diagInfoIniUrg.getDiagnostico().setAcronimo(rs.getString("acronimo"));
				diagInfoIniUrg.getDiagnostico().setTipoCIE(rs.getInt("tipocie"));
				diagInfoIniUrg.getDiagnostico().setNombre(rs.getString("nombre"));
				logger.info("Valor esprincipal: >>>>>> "+rs.getString("principal"));
				diagInfoIniUrg.getDiagnostico().setPrincipal(rs.getString("principal").equals("S")?true:false);
				array.add(diagInfoIniUrg);
			}
			rs.close();
			ps.close();
		}
		catch (Exception e) {			
			//e.printStackTrace();
			logger.info("error en getDiagInfoIniUrg >>>>>>> "+parametros+" cadena >> "+strConsultaDiagInfoIniUrg+" ");
			
		}
		return array;
	}
	
}

