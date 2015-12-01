/*
 * Junio 06, 2006
 */
package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.ConstantesBDHistoriaClinica;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * 
 *  @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad INFORMACION DEL PARTO
 *
 */
public class SqlBaseInformacionPartoDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseInformacionPartoDao.class);
	
	/**
	 * Cadena que inserta los datos generales del parto
	 */
	private static final String insertarInformacionPartoGeneralStr = " INSERT INTO informacion_parto ("+
		"consecutivo, "+
		"codigo_paciente, "+
		"cirugia, "+
		"fecha_proceso, "+
		"hora_proceso, "+
		"usuario, "+
		"institucion, "+
		"semana_gestacional, "+
		"control_prenatal, "+
		"numero_embarazo, "+
		"consultas_prenatales, "+
		"en_donde, "+
		"numero_contracciones, "+
		"intervalo, "+
		"fecha_contraccion, "+
		"hora_contraccion, "+
		"fecha_parto, "+
		"hora_parto, "+
		"hosp_embarazo_dias, "+
		"dias_gestacional, "+
		"por_gestacional, "+
		"presentacion, "+
		"percepcion_fetales, "+
		"desde_cuando_fetales, "+
		"exp_tapon_mucoso, "+
		"fecha_exp_tapon, "+
		"hora_exp_tapon, "+
		"ruptura_membrana, "+
		"fecha_ruptura, "+
		"hora_ruptura, "+
		"sangrado, "+
		"fecha_sangrado, "+
		"hora_sangrado, "+
		"corticoides_antenatales, "+
		"semana_inicio, "+
		"inicio_t_parto, "+
		"episiotomia, "+
		"acompanante, "+
		"enema_rasurado, "+
		"desgarros, "+
		"grado_desgarros, "+
		"terminacion, "+
		"placenta, "+
		"oxitocicos_alumbramiento, "+
		"ligadura_cordon, "+
		"fecha_egreso, "+
		"hora_egreso, "+
		"condicion_egreso, "+
		"antirubeola_post_parto, "+
		"anticoncepcion, "+
		"parto, "+
		"tiene_carne, " +
		"fecha_ingreso, " +
		"hora_ingreso, " +
		"posicion_parto, " +
		"indicacion_ppal_parto, " +
		"cantidad_hijos_vivos, " +
		"cantidad_hijos_muertos, " +
		"observaciones, "+
		"finalizado "+
		") VALUES ";
	
	/**
	 * Cadena que modifica la informacion del parto
	 */
	private static final String modificarInformacionPartoGeneralStr = "UPDATE informacion_parto SET "+
		"fecha_proceso = CURRENT_DATE, "+
		"hora_proceso = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", "+
		"usuario = ?, "+ 
		"institucion= ?, "+ 
		"semana_gestacional= ?, "+ 
		"control_prenatal= ?, "+ 
		"numero_embarazo = ?, "+ 
		"consultas_prenatales = ?, "+ 
		"en_donde = ?, "+ 
		"numero_contracciones = ?, "+
		"intervalo = ?, "+
		"fecha_contraccion = ?, "+ 
		"hora_contraccion = ?, "+ 
		"fecha_parto = ?, "+ 
		"hora_parto = ?, "+ 
		"hosp_embarazo_dias = ?, "+ 
		"dias_gestacional = ?, "+ 
		"por_gestacional = ?, "+ 
		"presentacion = ?, "+ 
		"percepcion_fetales = ?, "+ 
		"desde_cuando_fetales = ?, "+ 
		"exp_tapon_mucoso = ?, "+ 
		"fecha_exp_tapon = ?, "+ 
		"hora_exp_tapon = ?, "+
		"ruptura_membrana = ?, "+ 
		"fecha_ruptura = ?, "+ 
		"hora_ruptura = ?, "+ 
		"sangrado = ?, "+ 
		"fecha_sangrado = ?, "+ 
		"hora_sangrado = ?, "+ 
		"corticoides_antenatales = ?, "+ 
		"semana_inicio = ?, "+ 
		"inicio_t_parto = ?, "+ 
		"episiotomia = ?, "+ 
		"acompanante = ?, "+ 
		"enema_rasurado = ?, "+ 
		"desgarros = ?, "+ 
		"grado_desgarros = ?, "+ 
		"terminacion = ?, "+ 
		"placenta = ?, "+ 
		"oxitocicos_alumbramiento = ?, "+ 
		"ligadura_cordon = ?, "+ 
		"fecha_egreso = ?, "+ 
		"hora_egreso = ?, "+ 
		"condicion_egreso = ?, "+ 
		"antirubeola_post_parto = ?, "+ 
		"anticoncepcion = ?, "+
		"parto = ?, "+ 
		"tiene_carne = ?, " +
		"fecha_ingreso = ?, " +
		"hora_ingreso = ?," +
		"posicion_parto = ?, " +
		"indicacion_ppal_parto = ?, " +
		"cantidad_hijos_vivos = ?, " +
		"cantidad_hijos_muertos = ?, " +
		"observaciones = ?, "+
		"finalizado = ? " +
		"WHERE consecutivo = ? ";
	
	/**
	 * Cadena que inserta el detalle del parto
	 */
	private static final String insertarInformacionPartoCamposStr = " INSERT INTO " +
		"info_parto_secciones " +
		"(consecutivo,informacion_parto,campo,institucion,valor,texto) " +
		"VALUES "; 
	
	/**
	 * Cadena que modifica el detalle del parto
	 */
	private static final String modificarInformacionPartoCamposStr = "UPDATE info_parto_secciones SET valor = ?, texto = ? WHERe consecutivo = ?";
	
	/**
	 * Cadena que elimina el detalle del parto
	 */
	private static final String eliminarInformacionPartoCamposStr = "DELETE FROM info_parto_secciones WHERE consecutivo = ?";
	
	/**
	 * Cadena usada para eliminar la informacion de aborto hijos por consecutivo de info. parto
	 */
	private static final String eliminarInfoAbortoHijosStr = "DELETE FROM info_aborto_hijos WHERE informacion_parto = ?";
	
	/**
	 * Cadena usada para insertar informacion de aborto hijos por consecutivo de info. parto
	 */
	private static final String insertarInfoAbortoHijosStr = "INSERT INTO info_aborto_hijos (consecutivo,informacion_parto,sexo,peso, es_aborto) ";
	
	
	
	/**
	 * Cadena que carga los datos generales del informacion del parto
	 */
	private static final String cargarInformacionPartoGeneralStr = "SELECT "+
		"ip.consecutivo, "+ 
		"getnombrepersona(ip.codigo_paciente) AS nombre_paciente, "+ 
		"ip.codigo_paciente as codigo_paciente, "+ 
		"ip.cirugia as codigo_cirugia, "+
		"CASE WHEN ip.numero_embarazo IS NULL THEN '' ELSE ip.numero_embarazo || '' END AS numero_embarazo, "+
		"CASE WHEN ip.control_prenatal IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE ip.control_prenatal END as control_prenatal, "+ 
		"CASE WHEN ip.consultas_prenatales IS NULL THEN '' ELSE ip.consultas_prenatales || '' END AS consultas_prenatal, "+
		"CASE WHEN ip.en_donde IS NULL THEN '' ELSE ip.en_donde END as en_donde, "+ 
		"CASE WHEN ip.numero_contracciones IS NULL THEN '' ELSE ip.numero_contracciones || '' END AS numero_contracciones, "+
		"CASE WHEN ip.intervalo IS NULL THEN '' ELSE ip.intervalo END as intervalo, "+ 
		"CASE WHEN ip.en_donde IS NULL THEN '' ELSE ip.en_donde END as en_donde, "+ 
		"CASE WHEN ip.fecha_contraccion IS NULL THEN '' ELSE to_char(ip.fecha_contraccion,'DD/MM/YYYY') END as fecha_contracciones, "+ 
		"CASE WHEN ip.hora_contraccion IS NULL THEN '' ELSE ip.hora_contraccion END as hora_contracciones, "+ 
		"CASE WHEN ip.parto IS NULL THEN '' ELSE ip.parto END as parto, "+
		"CASE WHEN ip.fecha_parto IS NULL THEN '' ELSE to_char(ip.fecha_parto,'DD/MM/YYYY') END as fecha_parto, "+ 
		"CASE WHEN ip.hora_parto IS NULL THEN '' ELSE ip.hora_parto END as hora_parto, "+ 
		"CASE WHEN ip.hosp_embarazo_dias IS NULL THEN '' ELSE ip.hosp_embarazo_dias || '' END AS hosp_embarazo_dias, "+
		"CASE WHEN ip.semana_gestacional IS NULL THEN '' ELSE ip.semana_gestacional || '' END AS semanas, "+
		"CASE WHEN ip.dias_gestacional IS NULL THEN '' ELSE ip.dias_gestacional || '' END AS dias, "+
		"CASE WHEN ip.por_gestacional IS NULL THEN '' ELSE ip.por_gestacional END AS por_gestacional, "+
		"CASE WHEN ip.presentacion IS NULL THEN '' ELSE ip.presentacion END AS presentacion, "+
		"CASE WHEN ip.percepcion_fetales IS NULL THEN '"+ConstantesBD.acronimoSi+"' ELSE ip.percepcion_fetales END AS percepcion_movimientos_fetales, "+
		"CASE WHEN ip.desde_cuando_fetales IS NULL THEN '' ELSE ip.desde_cuando_fetales END AS desde_cuando_fetales, "+
		"CASE WHEN ip.exp_tapon_mucoso IS NULL THEN '' ELSE ip.exp_tapon_mucoso END AS expulsion_tapon, "+
		"CASE WHEN ip.fecha_exp_tapon IS NULL THEN '' ELSE to_char(ip.fecha_exp_tapon,'DD/MM/YYYY') END as fecha_expulsion, "+ 
		"CASE WHEN ip.hora_exp_tapon IS NULL THEN '' ELSE ip.hora_exp_tapon END as hora_expulsion, "+ 
		"CASE WHEN ip.ruptura_membrana IS NULL THEN '' ELSE ip.ruptura_membrana END AS ruptura_membranas, "+
		"CASE WHEN ip.fecha_ruptura IS NULL THEN '' ELSE to_char(ip.fecha_ruptura,'DD/MM/YYYY') END as fecha_ruptura, "+ 
		"CASE WHEN ip.hora_ruptura IS NULL THEN '' ELSE ip.hora_ruptura END as hora_ruptura, "+ 
		"CASE WHEN ip.sangrado IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE ip.sangrado END AS sangrado, "+
		"CASE WHEN ip.fecha_sangrado IS NULL THEN '' ELSE to_char(ip.fecha_sangrado,'DD/MM/YYYY') END as fecha_sangrado,"+ 
		"CASE WHEN ip.hora_sangrado IS NULL THEN '' ELSE ip.hora_sangrado end as hora_sangrado, "+ 
		"CASE WHEN ip.corticoides_antenatales IS NULL THEN '' ELSE ip.corticoides_antenatales END as corticoides, "+ 
		"CASE WHEN ip.semana_inicio IS NULL THEN '' ELSE ip.semana_inicio || '' END as semana_inicio, "+ 
		"CASE WHEN ip.inicio_t_parto IS NULL THEN '' ELSE ip.inicio_t_parto END as inicio_t_parto, "+ 
		"CASE WHEN ip.episiotomia IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE ip.episiotomia END AS episiotomia, "+
		"CASE WHEN ip.acompanante IS NULL THEN '' ELSE ip.acompanante END as acompanante, "+ 
		"CASE WHEN ip.enema_rasurado IS NULL THEN '' ELSE ip.enema_rasurado END as enema_rasurado, "+ 
		"CASE WHEN ip.desgarros IS NULL THEN '' ELSE ip.desgarros END as desgarros, "+ 
		"CASE WHEN ip.grado_desgarros IS NULL THEN '' ELSE ip.grado_desgarros || '' END as grado_desgarros, "+ 
		"CASE WHEN ip.terminacion IS NULL THEN '' ELSE ip.terminacion END as terminacion, "+ 
		"CASE WHEN ip.placenta IS NULL THEN '' ELSE ip.placenta END as placenta, "+ 
		"CASE WHEN ip.oxitocicos_alumbramiento IS NULL THEN '' ELSE ip.oxitocicos_alumbramiento END as oxitocicos, "+ 
		"CASE WHEN ip.ligadura_cordon IS NULL THEN '' ELSE ip.ligadura_cordon END as ligadura, "+ 
		"CASE WHEN ip.fecha_egreso IS NULL THEN '' ELSE to_char(ip.fecha_egreso,'DD/MM/YYYY') END as fecha_egreso, "+ 
		"CASE WHEN ip.hora_egreso IS NULL THEN '' ELSE ip.hora_egreso END as hora_egreso, "+ 
		"CASE WHEN ip.condicion_egreso IS NULL THEN '' ELSE ip.condicion_egreso END as condicion_egreso, "+ 
		"CASE WHEN ip.antirubeola_post_parto IS NULL THEN '' ELSE ip.antirubeola_post_parto END as antirubeola, "+ 
		"CASE WHEN ip.anticoncepcion IS NULL THEN '' ELSE ip.anticoncepcion END as anticoncepcion, "+ 
		"CASE WHEN ip.finalizado IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE ip.finalizado END as finalizado, "+
		"to_char(ip.fecha_proceso,'DD/MM/YYYY') AS fechaProceso, "+ 
		"ip.hora_proceso as horaProceso, "+ 
		"ip.usuario, "+ 
		"ip.institucion, "+ 
		"CASE WHEN ip.finalizado = '"+ConstantesBD.acronimoSi+"' THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END as es_finalizado, "+
		"CASE WHEN ip.tiene_carne IS NULL THEN '' ELSE ip.tiene_carne END AS tiene_carne, " +
		"CASE WHEN ip.fecha_ingreso IS NULL THEN '' ELSE to_char(ip.fecha_ingreso,'DD/MM/YYYY') END AS fecha_ingreso, " +
		"CASE WHEN ip.hora_ingreso IS NULL THEN '' ELSE ip.hora_ingreso END AS hora_ingreso, " +
		"CASE WHEN ip.posicion_parto IS NULL THEN '' ELSE ip.posicion_parto END AS posicion_parto, " +
		"CASE WHEN ip.indicacion_ppal_parto IS NULL THEN '' ELSE ip.indicacion_ppal_parto END AS indicacion_parto, " +
		"CASE WHEN ip.cantidad_hijos_vivos IS NULL THEN '' ELSE ip.cantidad_hijos_vivos || '' END AS cantidad_hijos_vivos, " +
		"CASE WHEN ip.cantidad_hijos_muertos IS NULL THEN '' ELSE ip.cantidad_hijos_muertos || '' END AS cantidad_hijos_muertos, " +
		"CASE WHEN ip.observaciones IS NULL THEN '' ELSE ip.observaciones END AS observaciones_anteriores, "+
		"'"+ConstantesBD.acronimoSi+"' AS existe_bd "+
		"FROM informacion_parto ip  " +
		"WHERE ";
	
	/**
	 * Cadena que carga los hijos del aborto
	 */
	private static final String cargarHijosAbortoStr = "SELECT "+ 
		"CASE WHEN sexo IS NULL THEN '' ELSE sexo END AS sexo, "+
		"CASE WHEN peso IS NULL THEN '' ELSE peso || '' END AS peso, " +
		"es_aborto AS esAborto "+ 
		"FROM info_aborto_hijos "+ 
		"WHERE informacion_parto = ?";
	
	
	
	/**
	 * Cadena para cargar las solicitudes de informacion parto
	 */
	private static final String cargarSolicitudesInformacionPartoStr = "SELECT DISTINCT "+ 
		"CASE WHEN ah.codigo IS NULL THEN coalesce(au.codigo,c.id) ELSE ah.codigo END AS numero_admision, "+
		"CASE WHEN ah.fecha_admision IS NULL THEN coalesce(au.fecha_admision || '',c.fecha_apertura || '') ELSE ah.fecha_admision || '' END AS fecha_admision, "+
		"CASE WHEN ah.hora_admision IS NULL THEN coalesce(au.hora_admision || '',c.hora_apertura || '') ELSE ah.hora_admision || '' END AS hora_admision, "+
		"s.consecutivo_ordenes_medicas AS orden, "+  
		"s.numero_solicitud AS numero_solicitud, "+
		"CASE WHEN usu.codigo_persona IS NULL THEN '' ELSE getnombrepersona(usu.codigo_persona) END AS medico_interpreto, "+
		"getnombrepersona(c.codigo_paciente) AS paciente, "+
		"p.primer_apellido || ' ' || p.segundo_apellido AS apellidos_paciente, "+
		"p.primer_nombre || ' ' || p.segundo_nombre AS nombres_paciente, "+
		"CASE WHEN ah.fecha_admision IS NULL THEN coalesce(au.fecha_admision || '_' || au.hora_admision,c.fecha_apertura || '_' || c.hora_apertura) ELSE ah.fecha_admision || '_' || ah.hora_admision END AS fecha_hora_admision, "+
		"getnombretipoidentificacion(p.tipo_identificacion) || ' (' || p.tipo_identificacion || ' )'   AS tipo_id, " +
		"p.numero_identificacion AS numero_id, " +
		"p.tipo_identificacion || ' ' || p.numero_identificacion AS tipo_n_id, " +
		"c.codigo_paciente, " +
		"c.id AS cuenta, " +
		"c.estado_cuenta AS estado_cuenta "+
		"FROM cuentas c  "+
		"INNER JOIN centros_costo cc ON(cc.codigo=c.area) "+
		"LEFT OUTER JOIN admisiones_hospi ah ON(c.id=ah.cuenta) "+ 
		"LEFT OUTER JOIN admisiones_urgencias au ON(c.id=au.cuenta) "+
		"INNER JOIN solicitudes s ON(s.cuenta = c.id) "+
		"INNER JOIN solicitudes_cirugia sciru ON(sciru.numero_solicitud = s.numero_solicitud) " +
		"INNER JOIN sol_cirugia_por_servicio sc ON(sc.numero_solicitud=s.numero_solicitud) "+ 
		"INNER JOIN servicios ss ON(ss.codigo = sc.servicio) "+
		"INNER JOIN personas p ON(p.codigo=c.codigo_paciente) "+
		"LEFT OUTER JOIN hoja_quirurgica hq ON(hq.numero_solicitud=s.numero_solicitud) "+ 
		"LEFT OUTER JOIN usuarios usu ON(usu.codigo_persona=hq.medico_finaliza) "+
		"WHERE "+		
		"(sciru.ind_qx='"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"' " +
			" OR sciru.ind_qx='"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"') AND " +		
		"cc.centro_atencion = ? AND "+
		"s.estado_historia_clinica != "+ConstantesBD.codigoEstadoHCAnulada+" AND "+
		"ss.tipo_servicio = '"+ConstantesBD.codigoServicioPartosCesarea+"' "; 
	
	/**
	 * Cadena para obtener la cirugia de partos de la solicitud
	 */
	private static final String obtenerCirugiaPartosStr = "SELECT sc.codigo " +
		"FROM sol_cirugia_por_servicio sc " +
		"INNER JOIN servicios s ON(s.codigo=sc.servicio) " +
		"WHERE sc.numero_solicitud = ? AND s.tipo_servicio = '"+ConstantesBD.codigoServicioPartosCesarea+"'";
	
	/**
	 * Cadena que consulta fecha/hora egreso de la admision del parto
	 * si la tiene
	 */
	private static final String obtenerFechaHoraEgresoAdmisionPartoStr = "SELECT "+ 
		"e.fecha_evolucion, "+
		"e.hora_evolucion "+ 
		"from evoluciones e "+ 
		"INNER JOIN solicitudes s ON(s.numero_solicitud=e.valoracion) "+ 
		"WHERE e.orden_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" and s.cuenta = ?";
	
	/**
	 * Cadena que consulta fecha/hora egreso de la admiison del parto
	 * partiendo del codigo de la cirugia
	 */
	private static final String obtenerFechaHoraEgresoAdmisionPartoCirugiaStr = "SELECT "+ 
		"substr(eg.hora_egreso,0,6) as hora_egreso, "+ 
		"to_char(eg.fecha_egreso,'DD/MM/YYYY') as fecha_egreso "+ 
		"from sol_cirugia_por_servicio scp "+ 
		"inner join solicitudes s ON(s.numero_solicitud=scp.numero_solicitud) "+
		"inner join egresos eg ON(eg.cuenta=s.cuenta) "+ 
		"where scp.codigo = ? and eg.codigo_medico IS NOT null";
	
	//**************QUERYS VALIDACIONES INFORMACION PARTOS****************************************************
	/**
	 * Cadena que verifica que un paciente tenga admisiones de urgencias u hospitalizacion
	 */
	private static final String validarAdmisionesPacienteStr = "SELECT "+ 
		"count(1) AS cuenta "+ 
		"FROM cuentas c "+
		"INNER JOIN centros_costo cc ON(cc.codigo=c.area) "+
		"WHERE " +
		"cc.centro_atencion = ? AND "+
		"c.codigo_paciente = ? AND " +
		"c.via_ingreso IN ("+ConstantesBD.codigoViaIngresoUrgencias+","+ConstantesBD.codigoViaIngresoHospitalizacion+")";
	
	/**
	 * Cadena que verifica si un paciente tiene solicitudes de parto
	 */
	private static final String validarSolicitudesPartoPacienteStr = "SELECT "+ 
		"count(1) as cuenta "+
		"FROM cuentas c "+ 
		"INNER JOIN ingresos i ON(i.id =c.id_ingreso) "+
		"INNER JOIN centros_costo cc ON(cc.codigo=c.area) "+
		"INNER JOIN solicitudes s ON(s.cuenta = c.id) "+ 
		"INNER JOIN sol_cirugia_por_servicio sc ON(sc.numero_solicitud=s.numero_solicitud) "+ 
		"INNER JOIN servicios ss ON(ss.codigo = sc.servicio) "+ 
		"WHERE "+
		"c.codigo_paciente = ? AND " +
		"cc.centro_atencion = ? AND " +
		"s.estado_historia_clinica <> "+ConstantesBD.codigoEstadoHCAnulada+" ";
	//*******************************************************************************************************
		
	/**
	 * Cadena que consulta el numero de embarazo no finalizado del paciente
	 */
	private static final String consultarNumeroEmbarazoHojaObstetricaStr = "SELECT embarazo FROM hoja_obstetrica WHERE paciente = ? AND fin_embarazo = "+ValoresPorDefecto.getValorFalseParaConsultas();
	
	/**
	 * Cadena que cuenta el numero de registros en la hoja obstetrica que tengan el paciente y el numero embarazo indicado
	 */
	private static final String existeNumeroEmbarazoHojaObstetricaStr = " SELECT count(1) AS cuenta FROM hoja_obstetrica WHERE paciente = ? and embarazo = ?";
	
	/**
	 * Cadena que finaliza el embarazo de una hoja obstétrica
	 */
	private static final String finalizarEmbarazoHojaObstetricaStr = "UPDATE hoja_obstetrica SET " +
		"fecha_terminacion = CURRENT_DATE, " +
		"motivo_finalizacion = '' , " +
		"fin_embarazo = "+ValoresPorDefecto.getValorTrueParaConsultas()+", " +
		"edad_gestacional_finalizar = ? WHERE paciente = ? AND embarazo = ? ";
	
	/**
	 * Cadena que consulta los campos de las secciones de la informacion del parto
	 */
	private static final String consultarCamposSeccionesStr = "SELECT "+
		"CASE WHEN s.consecutivo IS NULL THEN '' ELSE s.consecutivo || '' END as consecutivo, "+
		"c.campo, "+ 
		"i.nombre, "+
		"c.institucion, "+
		"CASE WHEN s.valor IS NULL THEN '' ELSE s.valor END AS valor, "+
		"CASE WHEN s.texto IS NULL THEN '' ELSE s.texto END AS texto "+
		"from campos_info_parto_inst c "+ 
		"INNER JOIN campos_info_parto i ON(i.codigo=c.campo) "+ 
		"LEFT OUTER JOIN info_parto_secciones s ON(s.informacion_parto=? AND s.campo=c.campo AND s.institucion = c.institucion) "+
		"WHERE (c.activo = '"+ConstantesBD.acronimoSi+"' OR s.consecutivo IS NOT NULL) AND c.institucion = ? AND c.seccion = ? ORDER BY c.orden ";
	
	/**
	 * Cadena que obtiene el número de controles de la hoja obstétrica
	 */
	private static final String obtenerConsultasPrenatalesHojaObstetricaStr = "SELECT count(1) as cuenta " +
		"FROM hoja_obstetrica h " +
		"INNER JOIN resumen_gestacional r ON(r.hoja_obstetrica=h.codigo) "+ 
		"WHERE h.paciente = ? AND embarazo = ?";
	
	/**
	 * Cadena para cargar vigilancia clinica
	 */
	private static final String cargarVigilanciaClinicaStr=" SELECT v.consecutivo AS consecutivo, " +
																	"v.info_parto AS consecutivoinfoparto, " +
																	"v.hora AS hora, " +
																	"v.fecha AS fechabd, " +
																	"to_char(v.fecha, 'DD/MM/YYYY') AS fecha, " +
																	"v.posicion_materna AS acronimoposicionmaterna, " +
																	"getintegridaddominio(v.posicion_materna) AS posicionmaterna, " +
																	"tension_arterial AS tensionarterial, " +
																	"tension_arterial_1 AS tensionarterial1, " +
																	"CASE WHEN pulso_materno IS NULL THEN '' ELSE v.pulso_materno||'' END AS pulsomaterno, " +
																	"v.frec_cardiaca_fetal AS acronimofreccardiacafetal, " +
																	"getintegridaddominio(v.frec_cardiaca_fetal) AS freccardiacafetal, " +
																	"CASE WHEN v.duracion_contracciones IS NULL THEN '' ELSE v.duracion_contracciones ||'' END AS duracioncontracciones, " +
																	"CASE WHEN v.frec_contracciones IS NULL THEN '' ELSE v.frec_contracciones||'' END AS freccontracciones, " +
																	"v.intensidad_dolor AS acronimointensidaddolor, " +
																	"getintegridaddominio(v.intensidad_dolor) as intensidaddolor, " +
																	"v.localizacion_dolor as acronimolocalizaciondolor, " +
																	"getintegridaddominio(v.localizacion_dolor) as localizaciondolor,  " +
																	"'true' AS estabd, " +
																	"'false' AS fueeliminado, " +
																	"v.dilatacion as dilatacion, " +
																	"vp.codigo AS codigovariedadposicion, " +
																	"vp.descripcion AS variedadposicion," +
																	"v.fcf AS fcf, " +
																	"v.estacion as estacion " +
															"FROM " +
																	"vig_cli_trab_parto v " +
																	"INNER JOIN variedades_posicion vp ON (vp.codigo=v.variedad_posicion) " +
															"WHERE " +
																	"v.info_parto=? " +
															"ORDER BY v.fecha, v.hora";
	
	/**
	 * Cadena que consulta la fecha/hora de ingreso hospitalizacion a partir de la cuenta asociada
	 */
	private static final String consultarFechaIngresoAsocioStr = "SELECT " +
		"to_char(ah.fecha_admision,'DD/MM/YYYY') as fecha_admision, " +
		"substr(ah.hora_admision,0,6) as hora_admision " +
		"from asocios_cuenta ac " +
		"inner join admisiones_hospi ah ON(ac.cuenta_final=ah.cuenta) " +
		"WHERE ac.cuenta_inicial = ?";
	
	
	/**
	 * Método que carga la información de un parto
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap cargarInformacionParto(Connection con,HashMap campos)
	{
		PreparedStatementDecorator ps = null;
		try
		{
			HashMap infoParto = new HashMap();
			HashMap infoHijosAborto = new HashMap();
			
			//*********FILTRO DE LA CONSULTA ***********************************************
			String consulta = cargarInformacionPartoGeneralStr;
			//busqueda por consecutivo
			if(campos.get("consecutivo")!=null&&!campos.get("consecutivo").toString().equals(""))
				consulta += " ip.consecutivo = "+ campos.get("consecutivo");
			
			//busqueda por cirugia
			if(campos.get("cirugia")!=null&&!campos.get("cirugia").toString().equals(""))
				consulta += " ip.cirugia = "+ campos.get("cirugia");
			
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			infoParto = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), false, true);
			
			if(Utilidades.convertirAEntero(infoParto.get("numRegistros").toString())<=0)
			{
				//Se llenan los datos generales
				infoParto.put("consecutivo","");
				infoParto.put("nombrePaciente","");
				infoParto.put("codigoPaciente","");
				infoParto.put("numeroEmbarazo","");
				infoParto.put("controlPrenatal",ConstantesBD.acronimoNo);
				infoParto.put("percepcionMovimientosFetales",ConstantesBD.acronimoSi);
				infoParto.put("expulsionTapon","");
				infoParto.put("rupturaMembranas","");
				infoParto.put("sangrado",ConstantesBD.acronimoNo);
				infoParto.put("episiotomia",ConstantesBD.acronimoNo);
				infoParto.put("finalizado",ConstantesBD.acronimoNo);
				infoParto.put("esFinalizado",ConstantesBD.acronimoNo);
				infoParto.put("existeBd",ConstantesBD.acronimoNo);
				infoParto.put("observacionesAnteriores", "");
				infoParto.put("fechaEgreso", "");
				infoParto.put("horaEgreso", "");
				infoParto.put("fechaIngreso", "");
				infoParto.put("horaIngreso", "");
				infoParto.put("cantidadHijos", "0");
				infoParto.put("numRegistros","1");
				infoParto.put("esAborto", "null");
				
				
			}
			else
			{
				//Se calcula la cantidad de hijos vivos y muertos
				int cantHijosVivos = infoParto.get("cantidadHijosVivos").toString().equals("")?0:Utilidades.convertirAEntero(infoParto.get("cantidadHijosVivos").toString());
				int cantHijosMuertos = infoParto.get("cantidadHijosMuertos").toString().equals("")?0:Utilidades.convertirAEntero(infoParto.get("cantidadHijosMuertos").toString());
				infoParto.put("cantidadHijos", (cantHijosVivos+cantHijosMuertos)+"");
				
				
				//***********SE CONSULTAN LOS HIJOS DEL ABORTO INGRESADOS**************************************+
				ps =  new PreparedStatementDecorator(con.prepareStatement(cargarHijosAbortoStr));
				ps.setDouble(1,Utilidades.convertirADouble(infoParto.get("consecutivo")+""));
				
				infoHijosAborto = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, false);
				
				if(Utilidades.convertirAEntero(infoHijosAborto.get("numRegistros").toString())>0)
				{
					infoParto.put("numeroHijosAborto", infoHijosAborto.get("numRegistros"));
					
					infoParto.put("esAborto", infoParto.get("parto").toString().equals(ConstantesBD.acronimoSi) ? "" : UtilidadTexto.getBoolean(infoHijosAborto.get("esaborto_"+0).toString()));
					
					for(int i=0;i<Utilidades.convertirAEntero(infoHijosAborto.get("numRegistros").toString());i++)
					{
						infoParto.put("sexoAborto_"+i,infoHijosAborto.get("sexo_"+i));
						infoParto.put("pesoAborto_"+i,infoHijosAborto.get("peso_"+i));
					}
				}
				
			}
			//************************************************************************************************
			
			//*************SECCION DE IDENTIFICADORES DE RIESGOS**********************************************
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarCamposSeccionesStr));
			
			
			ps.setDouble(1,Utilidades.convertirADouble(infoParto.get("consecutivo").toString().equals("")?""+ConstantesBD.codigoNuncaValido:infoParto.get("consecutivo")+""));
			ps.setInt(2,Utilidades.convertirAEntero(campos.get("institucion")+""));
			ps.setString(3,ConstantesBDHistoriaClinica.acronimoSecIdentificacionRiesgos);
			
			
			HashMap mapaIdentificacion = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, false);
			infoParto.put("mapaIdentificacion",mapaIdentificacion);
			infoParto.put("numIdentificacion",mapaIdentificacion.get("numRegistros"));
			
			//**********SECCION DE ENFERMEDADES****************************************************************
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarCamposSeccionesStr));
			
			ps.setDouble(1,Utilidades.convertirADouble(infoParto.get("consecutivo").toString().equals("")?""+ConstantesBD.codigoNuncaValido:infoParto.get("consecutivo")+""));
			ps.setInt(2,Utilidades.convertirAEntero(campos.get("institucion")+""));
			ps.setString(3,ConstantesBDHistoriaClinica.acronimoSecEnfermedades);
			
			HashMap mapaEnfermedades = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, false);
			infoParto.put("mapaEnfermedades",mapaEnfermedades);
			infoParto.put("numEnfermedades",mapaEnfermedades.get("numRegistros"));
			
			//**********SECCION DE MEDICACION****************************************************************
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarCamposSeccionesStr));
			
			ps.setDouble(1,Utilidades.convertirADouble(infoParto.get("consecutivo").toString().equals("")?""+ConstantesBD.codigoNuncaValido:infoParto.get("consecutivo")+""));
			ps.setInt(2,Utilidades.convertirAEntero(campos.get("institucion")+""));
			ps.setString(3,ConstantesBDHistoriaClinica.acronimoSecMedicacion);
			
			HashMap mapaMedicacion = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, false);
			infoParto.put("mapaMedicacion",mapaMedicacion);
			infoParto.put("numMedicacion",mapaMedicacion.get("numRegistros"));
			
			//**********SECCION DE TRANSFUSION****************************************************************
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarCamposSeccionesStr));
			
			ps.setDouble(1,Utilidades.convertirADouble(infoParto.get("consecutivo").toString().equals("")?""+ConstantesBD.codigoNuncaValido:infoParto.get("consecutivo")+""));
			ps.setInt(2,Utilidades.convertirAEntero(campos.get("institucion")+""));
			ps.setString(3,ConstantesBDHistoriaClinica.acronimoSecTransfusion);
			
			HashMap mapaTranfusion = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, false);
			infoParto.put("mapaTransfusion",mapaTranfusion);
			infoParto.put("numTransfusion",mapaTranfusion.get("numRegistros"));
			
			
			return infoParto;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarInformacionParto de SqlBaseInformacionPartoDao: "+e);
			return null;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}	
			
		
		}
	}
	
	/**
	 * Método implementado para insertar/modificar la informacion del parto
	 * @param con
	 * @param infoParto
	 * @param secuenciaGeneral
	 * @param secuenciaDetalle
	 * @param estado
	 * @return
	 */
	public static String insertarInformacionParto(Connection con,HashMap infoParto,String secuenciaGeneral,String secuenciaDetalle,String secuenciaHijosAborto,String estado)
	{
		PreparedStatementDecorator pst =  null;
		try
		{
			String consecutivo = "";
			int resp0 = 0, resp1 = 0, resp2 = 0;
			
			if(estado.equals(ConstantesBD.inicioTransaccion))
				UtilidadBD.iniciarTransaccion(con);
			
			if(UtilidadTexto.getBoolean(infoParto.get("existeBd").toString()))
				//Se modifica el registro existente
				resp0 = modificarRegistroGeneral(con,infoParto);
			else
				//Se inserta un nuevo registro
				resp0 = insertarRegistroGeneral(con,infoParto,secuenciaGeneral);
			
			
			if(resp0>0)
			{
				if(UtilidadTexto.getBoolean(infoParto.get("existeBd").toString()))
					consecutivo = infoParto.get("consecutivo").toString();
				else
					consecutivo = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con,"seq_informacion_parto") + "";
				
				int respTemp0 = 0, respTemp1 = 0, respTemp2 = 0, respTemp3 = 0, respTemp4 = 0;
				HashMap mapTemp = new HashMap();
				
				//********INSERTAR INFORMACION DE LOS HIJOS ABORTO (SI SE INGRESÓ)**********************
				respTemp4 = insertarHijosAborto(con,infoParto,secuenciaHijosAborto,consecutivo);
				//***************************************************************************************
				//********INSERTAR INFORMACION DE LA SECCION IDENTIFICADOR DE RIESGOS********************
				mapTemp = (HashMap)infoParto.get("mapaIdentificacion");
				respTemp0 = insertarModificarRegistroCampo(con,mapTemp,secuenciaDetalle,consecutivo);
				//********************************************************************************************
				//********INSERTAR INFORMACION DE LA SECCION ENFERMEDADES********************
				mapTemp = (HashMap)infoParto.get("mapaEnfermedades");
				respTemp1 = insertarModificarRegistroCampo(con,mapTemp,secuenciaDetalle,consecutivo);
				//********************************************************************************************
				//********INSERTAR INFORMACION DE LA SECCION MEDICACION********************
				mapTemp = (HashMap)infoParto.get("mapaMedicacion");
				respTemp2 = insertarModificarRegistroCampo(con,mapTemp,secuenciaDetalle,consecutivo);
				//********************************************************************************************
				//********INSERTAR INFORMACION DE LA SECCION TRANSFUSION********************
				mapTemp = (HashMap)infoParto.get("mapaTransfusion");
				respTemp3 = insertarModificarRegistroCampo(con,mapTemp,secuenciaDetalle,consecutivo);
				//********************************************************************************************
				
				if(respTemp0>0&&respTemp1>0&&respTemp2>0&&respTemp3>0&&respTemp4>0)
					resp1 = 1;
				else
					resp1 = 0;
			}
			
			//********FINALIZACION DE LA HOJA OBSTÉTRICA***********************************
			if(UtilidadTexto.getBoolean(infoParto.get("finalizado").toString())&&
				UtilidadTexto.getBoolean(infoParto.get("controlPrenatal").toString())&&
				existeNumeroEmbarazoHojaObstetrica(con, infoParto.get("codigoPaciente").toString(), Utilidades.convertirAEntero(infoParto.get("numeroEmbarazo").toString())))
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(finalizarEmbarazoHojaObstetricaStr));
				
				
				pst.setDouble(1,Utilidades.convertirADouble(infoParto.get("semanas")+""));
				pst.setInt(2,Utilidades.convertirAEntero(infoParto.get("codigoPaciente")+""));
				pst.setInt(3,Utilidades.convertirAEntero(infoParto.get("numeroEmbarazo")+""));
				
				resp2 = pst.executeUpdate();
			}
			else
				resp2 = 1;
			//*****************************************************************************
			if(resp0>0&&resp1>0&&resp2>0)
				return consecutivo;
			else
				return "";
			
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarInformacionParto de SqlBaseInformacionPartoDao: "+e);
			return "";
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
	
		}
	}
	
	
	
	/**
	 * Método implementado para insertar/actualizar la informacion del parto
	 * @param con
	 * @param infoParto
	 * @param secuenciaHijosAborto
	 * @param consecutivo
	 * @return
	 */
	private static int insertarHijosAborto(Connection con, HashMap infoParto, String secuenciaHijosAborto, String consecutivo) {
		
		PreparedStatementDecorator pst1 = null;
		PreparedStatementDecorator pst2 = null;
		int resp = 1;
		int numRegistros = 0;
		String consulta = "";
		
		try	{
		
			numRegistros = infoParto.get("numeroHijosAborto") != null && !infoParto.get("numeroHijosAborto").toString().equals("") ?
			Utilidades.convertirAEntero(infoParto.get("numeroHijosAborto").toString()):0;
			
			if(numRegistros > 0){
			
				pst1 =  new PreparedStatementDecorator(con.prepareStatement(eliminarInfoAbortoHijosStr));
				pst1.setDouble(1,Utilidades.convertirADouble(consecutivo));
				pst1.executeUpdate();

				consulta = insertarInfoAbortoHijosStr + " VALUES ("+secuenciaHijosAborto+","+consecutivo+",?,?,?)";
				
				for(int i=0; i<numRegistros; i++) {
			
					pst2 =  new PreparedStatementDecorator(con.prepareStatement(consulta));
					
					/**
					 * INSERT INTO info_aborto_hijos (consecutivo,informacion_parto,sexo,peso,es_aborto) 
					 */
					
					if(!infoParto.get("sexoAborto_"+i).toString().equals("")) {
						pst2.setString(1,infoParto.get("sexoAborto_"+i)+"");
					} else {
						pst2.setNull(1,Types.VARCHAR);
					}
					
					if(!infoParto.get("pesoAborto_"+i).toString().equals("")) {
						pst2.setInt(2,Utilidades.convertirAEntero(infoParto.get("pesoAborto_"+i)+""));
					} else {
						pst2.setNull(2, Types.INTEGER);
					}
					
					pst2.setBoolean(3, Boolean.parseBoolean(infoParto.get("esAborto").toString()));
					
					resp = pst2.executeUpdate();
					
					if(resp<=0) {
						i = numRegistros;
					}
				}
				}
				
		}catch(SQLException e){
			Log4JManager.error(e);
					resp = 0; 
		} catch (Exception e) {
			Log4JManager.error(e);
		} finally {
			try {
				if (pst1 != null) {
					pst1.close();
				}
			
			} catch (SQLException sqlException) {
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
			}
			try {
				if (pst2 != null) {
					pst2.close();
				}
				
			} catch (SQLException sqlException) {
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
			}
			
		}
		
		return resp;
	}

	/**
	 * Método implementado para insertar/modificar registro campo
	 * @param con
	 * @param mapTemp
	 * @param secuenciaDetalle
	 * @param consecutivo
	 * @return
	 */
	private static int insertarModificarRegistroCampo(Connection con, HashMap mapTemp, String secuenciaDetalle, String consecutivo) 
	{
		PreparedStatementDecorator pst = null;
		try
		{
			String consulta = "";
			int respGeneral = 1, respTemp = 0;
			
			
			for(int i=0;i<Utilidades.convertirAEntero(mapTemp.get("numRegistros").toString());i++)
			{
				//Se verifica que se haya insertado información
				if(mapTemp.get("valor_"+i)!=null&&!mapTemp.get("valor_"+i).toString().equals(""))
				{
					//Se verifica si es modificacion o insercion
					if(mapTemp.get("consecutivo_"+i).toString().equals(""))
					{
						//****************INSERCION***************************************************************
						consulta = insertarInformacionPartoCamposStr + "("+secuenciaDetalle+",?,?,?,?,?) " ;
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
						
						/**
						 * INSERT INTO " +
										"info_parto_secciones " +
										"(consecutivo,informacion_parto,campo,institucion,valor,texto) " +
										"VALUES 
						 */
						
						pst.setDouble(1, Utilidades.convertirADouble(consecutivo));
						pst.setDouble(2, Utilidades.convertirADouble(mapTemp.get("campo_"+i)+""));
						pst.setInt(3, Utilidades.convertirAEntero(mapTemp.get("institucion_"+i)+""));
						pst.setString(4, mapTemp.get("valor_"+i)+"");
						if(mapTemp.get("texto_"+i)!=null&&!mapTemp.get("texto_"+i).toString().equals(""))
							pst.setString(5, mapTemp.get("texto_"+i)+"");
						else
							pst.setNull(5,Types.VARCHAR);
						
						respTemp = pst.executeUpdate();
						if(respTemp<=0)
							respGeneral = 0;
						//****************************************************************************************
					}
					else
					{
						//****************MODIFICACION***************************************************************
						consulta = modificarInformacionPartoCamposStr;
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
						
						/**
						 * UPDATE info_parto_secciones SET valor = ?, texto = ? WHERe consecutivo = ?
						 */
						
						pst.setString(1, mapTemp.get("valor_"+i)+"");
						if((mapTemp.get("texto_"+i)!=null&&!mapTemp.get("texto_"+i).toString().equals("")&&Utilidades.convertirAEntero(mapTemp.get("campo_"+i).toString())!=ConstantesBDHistoriaClinica.codigoCampoOtrosGraves)
							||
							(Utilidades.convertirAEntero(mapTemp.get("campo_"+i).toString())==ConstantesBDHistoriaClinica.codigoCampoOtrosGraves&&UtilidadTexto.getBoolean(mapTemp.get("valor_"+i).toString())))
							pst.setString(2, mapTemp.get("texto_"+i)+"");
						else
							pst.setNull(2,Types.VARCHAR);
						pst.setDouble(3, Utilidades.convertirADouble(mapTemp.get("consecutivo_"+i)+""));
						
						respTemp = pst.executeUpdate();
						if(respTemp<=0)
							respGeneral = 0;
						//****************************************************************************************
					}
				}
				//si el campo valor venía vacío pero existe como registro, se debe liminar
				else if(!mapTemp.get("consecutivo_"+i).toString().equals(""))
				{
					//*******************ELIMINACION (sucede cuando se inactiva un campo)*********************************************************
					consulta = eliminarInformacionPartoCamposStr;
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta));
					
					pst.setDouble(1,Utilidades.convertirADouble(mapTemp.get("consecutivo_"+i)+""));
					respTemp = pst.executeUpdate();
					if(respTemp<=0)
						respGeneral = 0;
					//****************************************************************************************
				}
			}
			
			return respGeneral;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarModificarRegistroCampo de SqlBaseInformacionPartoDao: "+e);
			return 0;
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}	
			
		
		}
		
	}

	/**
	 * Método que realiza la modificacion de un registro de informacion del parto
	 * @param con
	 * @param infoParto
	 * @return
	 */
	private static int modificarRegistroGeneral(Connection con, HashMap infoParto) 
	{
		PreparedStatementDecorator ps = null;
		
		try
		{
			String consulta = modificarInformacionPartoGeneralStr ;
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			/**
			 * UPDATE informacion_parto SET "+
		
		"semana_gestacional= ?, "+ 
		"control_prenatal= ?, "+ 
		"numero_embarazo = ?, "+ 
		"consultas_prenatales = ?, "+ 
		"en_donde = ?, "+ 
		"numero_contracciones = ?, "+
		"intervalo = ?, "+
		"fecha_contraccion = ?, "+ 
		"hora_contraccion = ?, "+ 
		"fecha_parto = ?, "+ 
		"hora_parto = ?, "+ 
		"hosp_embarazo_dias = ?, "+ 
		"dias_gestacional = ?, "+ 
		"por_gestacional = ?, "+ 
		"presentacion = ?, "+ 
		"percepcion_fetales = ?, "+ 
		"desde_cuando_fetales = ?, "+ 
		"exp_tapon_mucoso = ?, "+ 
		"fecha_exp_tapon = ?, "+ 
		"hora_exp_tapon = ?, "+
		"ruptura_membrana = ?, "+ 
		"fecha_ruptura = ?, "+ 
		"hora_ruptura = ?, "+ 
		"sangrado = ?, "+ 
		"fecha_sangrado = ?, "+ 
		"hora_sangrado = ?, "+ 
		"corticoides_antenatales = ?, "+ 
		"semana_inicio = ?, "+ 
		"inicio_t_parto = ?, "+ 
		"episiotomia = ?, "+ 
		"acompanante = ?, "+ 
		"enema_rasurado = ?, "+ 
		"desgarros = ?, "+ 
		"grado_desgarros = ?, "+ 
		"terminacion = ?, "+ 
		"placenta = ?, "+ 
		"oxitocicos_alumbramiento = ?, "+ 
		"ligadura_cordon = ?, "+ 
		"fecha_egreso = ?, "+ 
		"hora_egreso = ?, "+ 
		"condicion_egreso = ?, "+ 
		"antirubeola_post_parto = ?, "+ 
		"anticoncepcion = ?, "+
		"parto = ?, "+ 
		"tiene_carne = ?, " +
		"fecha_ingreso = ?, " +
		"hora_ingreso = ?," +
		"posicion_parto = ?, " +
		"indicacion_ppal_parto = ?, " +
		"cantidad_hijos_vivos = ?, " +
		"cantidad_hijos_muertos = ?, " +
		"observaciones = ?, "+
		"finalizado = ? " +
		"WHERE consecutivo = ? ";
			 */
			
			
			ps.setString(1,infoParto.get("usuario")+"");
			ps.setInt(2,Utilidades.convertirAEntero(infoParto.get("institucion")+""));
			
			if(!infoParto.get("semanas").toString().equals(""))
				ps.setInt(3, Utilidades.convertirAEntero(infoParto.get("semanas")+""));
			else
				ps.setNull(3, Types.INTEGER);
			
			if(!infoParto.get("controlPrenatal").toString().equals(""))
				ps.setString(4, infoParto.get("controlPrenatal")+"");
			else
				ps.setNull(4, Types.VARCHAR);
			
			if(!infoParto.get("numeroEmbarazo").toString().equals(""))
				ps.setInt(5, Utilidades.convertirAEntero(infoParto.get("numeroEmbarazo")+""));
			else
				ps.setNull(5, Types.INTEGER);
			
			if(!infoParto.get("consultasPrenatal").toString().equals(""))
				ps.setInt(6, Utilidades.convertirAEntero(infoParto.get("consultasPrenatal")+""));
			else
				ps.setNull(6, Types.VARCHAR);
			
			if(infoParto.get("enDonde")!=null&&!infoParto.get("enDonde").toString().equals("")&&
				!UtilidadTexto.getBoolean(infoParto.get("controlPrenatal").toString()))
				ps.setString(7, infoParto.get("enDonde")+"");
			else
				ps.setNull(7, Types.VARCHAR);
			
			if(!infoParto.get("numeroContracciones").toString().equals(""))
				ps.setInt(8, Utilidades.convertirAEntero(infoParto.get("numeroContracciones")+""));
			else
				ps.setNull(8, Types.INTEGER);
			
			if(!infoParto.get("intervalo").toString().equals(""))
				ps.setString(9, infoParto.get("intervalo")+"");
			else
				ps.setNull(9, Types.VARCHAR);
			
			if(!infoParto.get("fechaContracciones").toString().equals(""))
				ps.setDate(10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoParto.get("fechaContracciones").toString())));
			else
				ps.setNull(10, Types.DATE);
			
			if(!infoParto.get("horaContracciones").toString().equals(""))
				ps.setString(11, infoParto.get("horaContracciones")+"");
			else
				ps.setNull(11, Types.VARCHAR);
			
			if(!infoParto.get("fechaParto").toString().equals(""))
				ps.setDate(12, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoParto.get("fechaParto").toString())));
			else
				ps.setNull(12, Types.DATE);
			
			if(!infoParto.get("horaParto").toString().equals(""))
				ps.setString(13, infoParto.get("horaParto")+"");
			else
				ps.setNull(13, Types.VARCHAR);
			
			if(!infoParto.get("hospEmbarazoDias").toString().equals(""))
				ps.setInt(14, Utilidades.convertirAEntero(infoParto.get("hospEmbarazoDias")+""));
			else
				ps.setNull(14, Types.INTEGER);
			
			if(!infoParto.get("dias").toString().equals(""))
				ps.setInt(15, Utilidades.convertirAEntero(infoParto.get("dias")+""));
			else
				ps.setNull(15, Types.INTEGER);
			
			if(!infoParto.get("porGestacional").toString().equals(""))
				ps.setString(16, infoParto.get("porGestacional")+"");
			else
				ps.setNull(16, Types.VARCHAR);
			
			if(!infoParto.get("presentacion").toString().equals(""))
				ps.setString(17, infoParto.get("presentacion")+"");
			else
				ps.setNull(17, Types.VARCHAR);
			
			if(!infoParto.get("percepcionMovimientosFetales").toString().equals(""))
				ps.setString(18, infoParto.get("percepcionMovimientosFetales")+"");
			else
				ps.setNull(18, Types.VARCHAR);
			
			if(infoParto.get("desdeCuandoFetales")!=null&&!infoParto.get("desdeCuandoFetales").toString().equals("")&&
				UtilidadTexto.getBoolean(infoParto.get("percepcionMovimientosFetales").toString()))
				ps.setString(19, infoParto.get("desdeCuandoFetales")+"");
			else
				ps.setNull(19, Types.VARCHAR);
			
			if(!infoParto.get("expulsionTapon").toString().equals(""))
				ps.setString(20, infoParto.get("expulsionTapon")+"");
			else
				ps.setNull(20, Types.VARCHAR);
			
			if(infoParto.get("fechaExpulsion")!=null&&!infoParto.get("fechaExpulsion").toString().equals("")&&
				UtilidadTexto.getBoolean(infoParto.get("expulsionTapon").toString()))
				ps.setDate(21, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoParto.get("fechaExpulsion").toString())));
			else
				ps.setNull(21, Types.DATE);
			
			if(infoParto.get("horaExpulsion")!=null&&!infoParto.get("horaExpulsion").toString().equals("")&&
				UtilidadTexto.getBoolean(infoParto.get("expulsionTapon").toString()))
				ps.setString(22, infoParto.get("horaExpulsion")+"");
			else
				ps.setNull(22, Types.VARCHAR);
			
			if(!infoParto.get("rupturaMembranas").toString().equals(""))
				ps.setString(23, infoParto.get("rupturaMembranas")+"");
			else
				ps.setNull(23, Types.VARCHAR);
			
			if(infoParto.get("fechaRuptura")!=null&&!infoParto.get("fechaRuptura").toString().equals("")&&
				UtilidadTexto.getBoolean(infoParto.get("rupturaMembranas").toString()))
				ps.setDate(24, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoParto.get("fechaRuptura").toString())));
			else
				ps.setNull(24, Types.DATE);
			
			if(infoParto.get("horaRuptura")!=null&&!infoParto.get("horaRuptura").toString().equals("")&&
				UtilidadTexto.getBoolean(infoParto.get("rupturaMembranas").toString()))
				ps.setString(25, infoParto.get("horaRuptura")+"");
			else
				ps.setNull(25, Types.VARCHAR);
			
			if(!infoParto.get("sangrado").toString().equals(""))
				ps.setString(26, infoParto.get("sangrado")+"");
			else
				ps.setNull(26, Types.VARCHAR);
			
			if(infoParto.get("fechaSangrado")!=null&&!infoParto.get("fechaSangrado").toString().equals("")&&
				UtilidadTexto.getBoolean(infoParto.get("sangrado").toString()))
				ps.setDate(27, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoParto.get("fechaSangrado").toString())));
			else
				ps.setNull(27, Types.DATE);
			
			if(infoParto.get("horaSangrado")!=null&&!infoParto.get("horaSangrado").toString().equals("")&&
				UtilidadTexto.getBoolean(infoParto.get("sangrado").toString()))
				ps.setString(28, infoParto.get("horaSangrado")+"");
			else
				ps.setNull(28, Types.VARCHAR);
			
			if(!infoParto.get("corticoides").toString().equals(""))
				ps.setString(29, infoParto.get("corticoides")+"");
			else
				ps.setNull(29, Types.VARCHAR);
			
			if(!infoParto.get("semanaInicio").toString().equals(""))
				ps.setInt(30, Utilidades.convertirAEntero(infoParto.get("semanaInicio")+""));
			else
				ps.setNull(30, Types.INTEGER);
			
			if(!infoParto.get("inicioTParto").toString().equals(""))
				ps.setString(31, infoParto.get("inicioTParto")+"");
			else
				ps.setNull(31, Types.VARCHAR);
			
			if(!infoParto.get("episiotomia").toString().equals(""))
				ps.setString(32, infoParto.get("episiotomia")+"");
			else
				ps.setNull(32, Types.VARCHAR);
			
			if(!infoParto.get("acompanante").toString().equals(""))
				ps.setString(33, infoParto.get("acompanante")+"");
			else
				ps.setNull(33, Types.VARCHAR);
			
			if(!infoParto.get("enemaRasurado").toString().equals(""))
				ps.setString(34, infoParto.get("enemaRasurado")+"");
			else
				ps.setNull(34, Types.VARCHAR);
			
			if(!infoParto.get("desgarros").toString().equals(""))
				ps.setString(35, infoParto.get("desgarros")+"");
			else
				ps.setNull(35, Types.VARCHAR);
			
			if(!infoParto.get("gradoDesgarros").toString().equals(""))
				ps.setInt(36, Utilidades.convertirAEntero(infoParto.get("gradoDesgarros")+""));
			else
				ps.setNull(36, Types.INTEGER);
			
			if(!infoParto.get("terminacion").toString().equals(""))
				ps.setString(37, infoParto.get("terminacion")+"");
			else
				ps.setNull(37, Types.VARCHAR);
			
			if(!infoParto.get("placenta").toString().equals(""))
				ps.setString(38, infoParto.get("placenta")+"");
			else
				ps.setNull(38, Types.VARCHAR);
			
			if(!infoParto.get("oxitocicos").toString().equals(""))
				ps.setString(39, infoParto.get("oxitocicos")+"");
			else
				ps.setNull(39, Types.VARCHAR);
			
			if(!infoParto.get("ligadura").toString().equals(""))
				ps.setString(40, infoParto.get("ligadura")+"");
			else
				ps.setNull(40, Types.VARCHAR);
			
			if(!infoParto.get("fechaEgreso").toString().equals(""))
				ps.setDate(41, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoParto.get("fechaEgreso").toString())));
			else
				ps.setNull(41, Types.DATE);
			
			if(!infoParto.get("horaEgreso").toString().equals(""))
				ps.setString(42, infoParto.get("horaEgreso")+"");
			else
				ps.setNull(42, Types.VARCHAR);
			
			if(!infoParto.get("condicionEgreso").toString().equals(""))
				ps.setString(43, infoParto.get("condicionEgreso")+"");
			else
				ps.setNull(43, Types.VARCHAR);
			
			if(infoParto.get("antirubeola")!=null&&!infoParto.get("antirubeola").toString().equals(""))
				ps.setString(44, infoParto.get("antirubeola")+"");
			else
				ps.setNull(44, Types.VARCHAR);
			
			if(!infoParto.get("anticoncepcion").toString().equals(""))
				ps.setString(45, infoParto.get("anticoncepcion")+"");
			else
				ps.setNull(45, Types.VARCHAR);
			
			if(infoParto.get("parto")!=null&&!infoParto.get("parto").toString().equals(""))
				ps.setString(46, infoParto.get("parto")+"");
			else
				ps.setNull(46, Types.VARCHAR);
			
			if(infoParto.get("tieneCarne")!=null&&!infoParto.get("tieneCarne").toString().equals(""))
				ps.setString(47, infoParto.get("tieneCarne")+"");
			else
				ps.setNull(47, Types.VARCHAR);
			
			if(!infoParto.get("fechaIngreso").toString().equals(""))
				ps.setDate(48, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoParto.get("fechaIngreso").toString())));
			else
				ps.setNull(48, Types.DATE);
			
			if(!infoParto.get("horaIngreso").toString().equals(""))
				ps.setString(49, infoParto.get("horaIngreso")+"");
			else
				ps.setNull(49, Types.VARCHAR);
			
			if(!infoParto.get("posicionParto").toString().equals(""))
				ps.setString(50, infoParto.get("posicionParto")+"");
			else
				ps.setNull(50, Types.VARCHAR);
			
			if(!infoParto.get("indicacionParto").toString().equals(""))
				ps.setString(51, infoParto.get("indicacionParto")+"");
			else
				ps.setNull(51, Types.VARCHAR);
			
			if(!infoParto.get("cantidadHijosVivos").toString().equals(""))
				ps.setInt(52, Utilidades.convertirAEntero(infoParto.get("cantidadHijosVivos")+""));
			else
				ps.setNull(52, Types.INTEGER);
			
			if(!infoParto.get("cantidadHijosMuertos").toString().equals(""))
				ps.setInt(53, Utilidades.convertirAEntero(infoParto.get("cantidadHijosMuertos")+""));
			else
				ps.setNull(53, Types.INTEGER);
			
			if(!infoParto.get("observacionesNuevas").toString().equals(""))
				ps.setString(54, infoParto.get("observacionesNuevas")+"");
			else
				ps.setNull(54, Types.VARCHAR);
			
			if(!infoParto.get("finalizado").toString().equals(""))
				ps.setString(55, infoParto.get("finalizado")+"");
			else
				ps.setNull(55, Types.VARCHAR);
			
			ps.setDouble(56, Utilidades.convertirADouble(infoParto.get("consecutivo")+""));
			return ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarRegistroGeneral de SqlBaseInformacionPartoDao: "+e);
			return 0;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
	
		}
	}

	/**
	 * Método que realiza la inserción de un registro de informacion del parto
	 * @param con
	 * @param infoParto
	 * @param secuenciaGeneral
	 * @return
	 */
	private static int insertarRegistroGeneral(Connection con, HashMap infoParto, String secuenciaGeneral) 
	{
		PreparedStatementDecorator ps = null;
		
		try
		{
			String consulta = insertarInformacionPartoGeneralStr + "("+secuenciaGeneral+",?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta));
			ps.setInt(1,Utilidades.convertirAEntero(infoParto.get("codigoPaciente")+""));
			ps.setInt(2,Utilidades.convertirAEntero(infoParto.get("codigoCirugia")+""));
			ps.setString(3,infoParto.get("usuario")+"");
			ps.setInt(4,Utilidades.convertirAEntero(infoParto.get("institucion")+""));
			
			if(!infoParto.get("semanas").toString().equals(""))
				ps.setInt(5, Utilidades.convertirAEntero(infoParto.get("semanas")+""));
			else
				ps.setNull(5, Types.INTEGER);
			
			if(!infoParto.get("controlPrenatal").toString().equals(""))
				ps.setString(6, infoParto.get("controlPrenatal")+"");
			else
				ps.setNull(6, Types.VARCHAR);
			
			if(!infoParto.get("numeroEmbarazo").toString().equals(""))
				ps.setInt(7, Utilidades.convertirAEntero(infoParto.get("numeroEmbarazo")+""));
			else
				ps.setNull(7, Types.INTEGER);
			
			if(!infoParto.get("consultasPrenatal").toString().equals(""))
				ps.setString(8, infoParto.get("consultasPrenatal")+"");
			else
				ps.setNull(8, Types.VARCHAR);
			
			if(infoParto.get("enDonde")!=null&&!infoParto.get("enDonde").toString().equals("")&&
				!UtilidadTexto.getBoolean(infoParto.get("controlPrenatal").toString()))
				ps.setString(9, infoParto.get("enDonde")+"");
			else
				ps.setNull(9, Types.VARCHAR);
			
			if(!infoParto.get("numeroContracciones").toString().equals(""))
				ps.setInt(10, Utilidades.convertirAEntero(infoParto.get("numeroContracciones")+""));
			else
				ps.setNull(10, Types.INTEGER);
			
			if(!infoParto.get("intervalo").toString().equals(""))
				ps.setString(11, infoParto.get("intervalo")+"");
			else
				ps.setNull(11, Types.VARCHAR);
			
			if(!infoParto.get("fechaContracciones").toString().equals(""))
				ps.setDate(12, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoParto.get("fechaContracciones").toString())));
			else
				ps.setNull(12, Types.DATE);
			
			if(!infoParto.get("horaContracciones").toString().equals(""))
				ps.setString(13, infoParto.get("horaContracciones")+"");
			else
				ps.setNull(13, Types.VARCHAR);
			
			if(!infoParto.get("fechaParto").toString().equals(""))
				ps.setDate(14, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoParto.get("fechaParto").toString())));
			else
				ps.setNull(14, Types.DATE);
			
			if(!infoParto.get("horaParto").toString().equals(""))
				ps.setString(15, infoParto.get("horaParto")+"");
			else
				ps.setNull(15, Types.VARCHAR);
			
			if(!infoParto.get("hospEmbarazoDias").toString().equals(""))
				ps.setInt(16, Utilidades.convertirAEntero(infoParto.get("hospEmbarazoDias")+""));
			else
				ps.setNull(16, Types.INTEGER);
			
			if(!infoParto.get("dias").toString().equals(""))
				ps.setInt(17, Utilidades.convertirAEntero(infoParto.get("dias")+""));
			else
				ps.setNull(17, Types.INTEGER);
			
			if(!infoParto.get("porGestacional").toString().equals(""))
				ps.setString(18, infoParto.get("porGestacional")+"");
			else
				ps.setNull(18, Types.VARCHAR);
			
			if(!infoParto.get("presentacion").toString().equals(""))
				ps.setString(19, infoParto.get("presentacion")+"");
			else
				ps.setNull(19, Types.VARCHAR);
			
			if(!infoParto.get("percepcionMovimientosFetales").toString().equals(""))
				ps.setString(20, infoParto.get("percepcionMovimientosFetales")+"");
			else
				ps.setNull(20, Types.VARCHAR);
			
			if(infoParto.get("desdeCuandoFetales")!=null&&!infoParto.get("desdeCuandoFetales").toString().equals("")
				&&UtilidadTexto.getBoolean(infoParto.get("percepcionMovimientosFetales").toString()))
				ps.setString(21, infoParto.get("desdeCuandoFetales")+"");
			else
				ps.setNull(21, Types.VARCHAR);
			
			if(!infoParto.get("expulsionTapon").toString().equals(""))
				ps.setString(22, infoParto.get("expulsionTapon")+"");
			else
				ps.setNull(22, Types.VARCHAR);
			
			if(infoParto.get("fechaExpulsion")!=null&&!infoParto.get("fechaExpulsion").toString().equals("")&&
				UtilidadTexto.getBoolean(infoParto.get("expulsionTapon").toString()))
				ps.setDate(23, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoParto.get("fechaExpulsion").toString())));
			else
				ps.setNull(23, Types.DATE);
			
			if(infoParto.get("horaExpulsion")!=null&&!infoParto.get("horaExpulsion").toString().equals("")&&
				UtilidadTexto.getBoolean(infoParto.get("expulsionTapon").toString()))
				ps.setString(24, infoParto.get("horaExpulsion")+"");
			else
				ps.setNull(24, Types.VARCHAR);
			
			if(!infoParto.get("rupturaMembranas").toString().equals(""))
				ps.setString(25, infoParto.get("rupturaMembranas")+"");
			else
				ps.setNull(25, Types.VARCHAR);
			
			if(infoParto.get("fechaRuptura")!=null&&!infoParto.get("fechaRuptura").toString().equals("")&&
				UtilidadTexto.getBoolean(infoParto.get("rupturaMembranas").toString()))
				ps.setDate(26, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoParto.get("fechaRuptura").toString())));
			else
				ps.setNull(26, Types.DATE);
			
			if(infoParto.get("horaRuptura")!=null&&!infoParto.get("horaRuptura").toString().equals("")&&
				UtilidadTexto.getBoolean(infoParto.get("rupturaMembranas").toString()))
				ps.setString(27, infoParto.get("horaRuptura")+"");
			else
				ps.setNull(27, Types.VARCHAR);
			
			if(!infoParto.get("sangrado").toString().equals(""))
				ps.setString(28, infoParto.get("sangrado")+"");
			else
				ps.setNull(28, Types.VARCHAR);
			
			if(infoParto.get("fechaSangrado")!=null&&!infoParto.get("fechaSangrado").toString().equals("")&&
				UtilidadTexto.getBoolean(infoParto.get("sangrado").toString()))
				ps.setDate(29, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoParto.get("fechaSangrado").toString())));
			else
				ps.setNull(29, Types.DATE);
			
			if(infoParto.get("horaSangrado")!=null&&!infoParto.get("horaSangrado").toString().equals("")&&
				UtilidadTexto.getBoolean(infoParto.get("sangrado").toString()))
				ps.setString(30, infoParto.get("horaSangrado")+"");
			else
				ps.setNull(30, Types.VARCHAR);
			
			if(!infoParto.get("corticoides").toString().equals(""))
				ps.setString(31, infoParto.get("corticoides")+"");
			else
				ps.setNull(31, Types.VARCHAR);
			
			if(!infoParto.get("semanaInicio").toString().equals(""))
				ps.setInt(32, Utilidades.convertirAEntero(infoParto.get("semanaInicio")+""));
			else
				ps.setNull(32, Types.INTEGER);
			
			if(!infoParto.get("inicioTParto").toString().equals(""))
				ps.setString(33, infoParto.get("inicioTParto")+"");
			else
				ps.setNull(33, Types.VARCHAR);
			
			if(!infoParto.get("episiotomia").toString().equals(""))
				ps.setString(34, infoParto.get("episiotomia")+"");
			else
				ps.setNull(34, Types.VARCHAR);
			
			if(!infoParto.get("acompanante").toString().equals(""))
				ps.setString(35, infoParto.get("acompanante")+"");
			else
				ps.setNull(35, Types.VARCHAR);
			
			if(!infoParto.get("enemaRasurado").toString().equals(""))
				ps.setString(36, infoParto.get("enemaRasurado")+"");
			else
				ps.setNull(36, Types.VARCHAR);
			
			if(!infoParto.get("desgarros").toString().equals(""))
				ps.setString(37, infoParto.get("desgarros")+"");
			else
				ps.setNull(37, Types.VARCHAR);
			
			if(!infoParto.get("gradoDesgarros").toString().equals(""))
				ps.setInt(38, Utilidades.convertirAEntero(infoParto.get("gradoDesgarros")+""));
			else
				ps.setNull(38, Types.INTEGER);
			
			if(!infoParto.get("terminacion").toString().equals(""))
				ps.setString(39, infoParto.get("terminacion")+"");
			else
				ps.setNull(39, Types.VARCHAR);
			
			if(!infoParto.get("placenta").toString().equals(""))
				ps.setString(40, infoParto.get("placenta")+"");
			else
				ps.setNull(40, Types.VARCHAR);
			
			if(!infoParto.get("oxitocicos").toString().equals(""))
				ps.setString(41, infoParto.get("oxitocicos")+"");
			else
				ps.setNull(41, Types.VARCHAR);
			
			if(!infoParto.get("ligadura").toString().equals(""))
				ps.setString(42, infoParto.get("ligadura")+"");
			else
				ps.setNull(42, Types.VARCHAR);
			
			if(!infoParto.get("fechaEgreso").toString().equals(""))
				ps.setDate(43, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoParto.get("fechaEgreso").toString())));
			else
				ps.setNull(43, Types.DATE);
			
			if(!infoParto.get("horaEgreso").toString().equals(""))
				ps.setString(44, infoParto.get("horaEgreso")+"");
			else
				ps.setNull(44, Types.VARCHAR);
			
			if(!infoParto.get("condicionEgreso").toString().equals(""))
				ps.setString(45, infoParto.get("condicionEgreso")+"");
			else
				ps.setNull(45, Types.VARCHAR);
			
			if(infoParto.get("antirubeola")!=null&&!infoParto.get("antirubeola").toString().equals(""))
				ps.setString(46, infoParto.get("antirubeola")+"");
			else
				ps.setNull(46, Types.VARCHAR);
			
			if(!infoParto.get("anticoncepcion").toString().equals(""))
				ps.setString(47, infoParto.get("anticoncepcion")+"");
			else
				ps.setNull(47, Types.VARCHAR);
			
			if(infoParto.get("parto")!=null&&!infoParto.get("parto").toString().equals(""))
				ps.setString(48, infoParto.get("parto")+"");
			else
				ps.setNull(48, Types.VARCHAR);
			
			if(infoParto.get("tieneCarne")!=null&&!infoParto.get("tieneCarne").toString().equals(""))
				ps.setString(49, infoParto.get("tieneCarne")+"");
			else
				ps.setNull(49, Types.VARCHAR);
			
			if(!infoParto.get("fechaIngreso").toString().equals(""))
				ps.setDate(50, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(infoParto.get("fechaIngreso").toString())));
			else
				ps.setNull(50, Types.DATE);
			
			if(!infoParto.get("horaIngreso").toString().equals(""))
				ps.setString(51, infoParto.get("horaIngreso")+"");
			else
				ps.setNull(51, Types.VARCHAR);
			
			if(!infoParto.get("posicionParto").toString().equals(""))
				ps.setString(52, infoParto.get("posicionParto")+"");
			else
				ps.setNull(52, Types.VARCHAR);
			
			if(!infoParto.get("indicacionParto").toString().equals(""))
				ps.setString(53, infoParto.get("indicacionParto")+"");
			else
				ps.setNull(53, Types.VARCHAR);
			
			if(!infoParto.get("cantidadHijosVivos").toString().equals(""))
				ps.setInt(54, Utilidades.convertirAEntero(infoParto.get("cantidadHijosVivos")+""));
			else
				ps.setNull(54, Types.INTEGER);
			
			if(!infoParto.get("cantidadHijosMuertos").toString().equals(""))
				ps.setInt(55, Utilidades.convertirAEntero(infoParto.get("cantidadHijosMuertos")+""));
			else
				ps.setNull(55, Types.INTEGER);
			
			if(!infoParto.get("observacionesNuevas").toString().equals(""))
				ps.setString(56, infoParto.get("observacionesNuevas")+"");
			else
				ps.setNull(56, Types.VARCHAR);
			
			if(!infoParto.get("finalizado").toString().equals(""))
				ps.setString(57, infoParto.get("finalizado")+"");
			else
				ps.setNull(57, Types.VARCHAR);
			
			return ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarRegistroGeneral de SqlBaseInformacionPartoDao: "+e);
			return 0;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
	
		}
	}

	/**
	 * Método que consulta las solicitudes del paciente al cual se la va a ingresar
	 * la informacion del parto
	 * @param con
	 * @param codigoPaciente
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap cargarSolicitudesInformacionParto(Connection con,String codigoPaciente,String fechaInicial,String fechaFinal,int centroAtencion)
	{
		PreparedStatementDecorator ps = null;
		try
		{
			String consulta = cargarSolicitudesInformacionPartoStr;
			if(codigoPaciente.equals(""))
			{
				consulta += " AND p.sexo = "+ConstantesBD.codigoSexoFemenino+
					" AND ((ah.fecha_admision BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"')" +
							" OR (au.fecha_admision BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"')) "+
					"ORDER BY numero_admision DESC";
				ps =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				ps.setInt(1,centroAtencion);
			}
			else
			{
				consulta += " AND c.codigo_paciente = ? ORDER BY numero_admision DESC ";
				ps =  new PreparedStatementDecorator(con.prepareStatement(consulta));
				ps.setInt(1,centroAtencion);
				ps.setInt(2,Utilidades.convertirAEntero(codigoPaciente));
				
			}
			logger.info("CONSULTA => "+consulta+", centroAtencion: "+centroAtencion+", codigoPaciente:"+codigoPaciente);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarSolicitudesInformacionParto de SQlBaseInformacionPartoDao: "+e);
			return null;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
	
		}
	}
	
	/**
	 * Método para obtener el consecutivo de la cirugia
	 * de parto que está dentro de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String obtenerCodigoCirugiaParto(Connection con,String numeroSolicitud)
	{
		PreparedStatementDecorator ps =  null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(obtenerCirugiaPartosStr));
			ps.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				return rs.getString("codigo");
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerConsecutivoCirugia de SqlBaseInformacionPartoDao: "+e);
			return "";
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
	
		}
	}
	
	/**
	 * Método que consulta la fecha/hora del egreso de una admision de un parto
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static String obtenerFechaHoraEgresoAdmisionParto(Connection con,String codigoCuenta)
	{
		PreparedStatementDecorator ps =   null;
		ResultSetDecorator rs = null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(obtenerFechaHoraEgresoAdmisionPartoStr));
			ps.setInt(1,Utilidades.convertirAEntero(codigoCuenta));
			
			rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				return rs.getString("fecha_evolucion")+ConstantesBD.separadorSplit+UtilidadFecha.convertirHoraACincoCaracteres(rs.getString("hora_evolucion"));
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerFechaHoraEgresoAdmisionParto de SQlBaseInformacionPartoDao: "+e);
			return "";
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
	
		}
	}
	
	/**
	 * Método que consulta la fecha y hora egreso de la admision del parto
	 * partiendo desde el codigo de la cirugia
	 * @param con
	 * @param codigoCirugia
	 * @return
	 */
	public static String obtenerFechaHoraEgresoAdmisionPartoCirugia(Connection con,String codigoCirugia)
	{
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs= null;
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(obtenerFechaHoraEgresoAdmisionPartoCirugiaStr));
			ps.setInt(1,Utilidades.convertirAEntero(codigoCirugia));
			
			rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				return rs.getString("fecha_egreso")+ConstantesBD.separadorSplit+rs.getString("hora_egreso");
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerFechaHoraEgresoAdmisionPartoCirugia: "+e);
			return "";
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
	
		}
	}
	
	/**
	 * Método implementado para verificar si el paciente tiene informacion de partos
	 * pendiente de ingresar
	 * @param con
	 * @param codigoPaciente
	 * @param centroAtencion
	 * @return
	 */
	public static String validacionInformacionPartoPaciente(Connection con,String codigoPaciente,int centroAtencion)
	{
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		try
		{
			//1) Se verifican las admisiones del paciente
			int resp = 1;
			String mensaje = "", consulta = "";
			 /** 
			  * Nota * Se quitó esta validación por tarea [id=27996]
	
			String consulta = validarAdmisionesPacienteStr;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,centroAtencion);
			pst.setObject(2,codigoPaciente);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				resp = rs.getInt("cuenta");**/
			
			if(resp>0)
			{
				resp = 0;
				//2) Se verifica si el paciente tiene solicitudes de partos
				consulta = validarSolicitudesPartoPacienteStr + " AND ss.tipo_servicio = '"+ConstantesBD.codigoServicioPartosCesarea+"'";
				ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				
				ps.setInt(1,Utilidades.convertirAEntero(codigoPaciente));
				ps.setInt(2,centroAtencion);
				rs = new ResultSetDecorator(ps.executeQuery());
				
				if(rs.next())
					resp = rs.getInt("cuenta");
				
				logger.info("consulta=> "+consulta+", centroAtencion=> "+centroAtencion+", codigoPaciente=> "+codigoPaciente);
				logger.info("cuenta=> "+resp);
				
				if(resp<=0)
					mensaje = "error.validacionessolicitud.sinPartos";
			}
			else
				mensaje = "error.validacionessolicitud.sinAdmisiones";
			
			return mensaje;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en validacionInformacionPartoPaciente de SQlBaseInformacionPartoDao: "+e);
			return "";
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
	
		}
	}
	
	/**
	 * Método que consulta el numero de embarazo de un embarazo no finalizado del paciente
	 * desde la hoja obstétrica
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static int consultarNumeroEmbarazoHojaObstetrica(Connection con,String codigoPaciente)
	{
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs =  null;
		try
		{
			int embarazo = 0;
			ps =  new PreparedStatementDecorator(con.prepareStatement(consultarNumeroEmbarazoHojaObstetricaStr));
			ps.setInt(1,Utilidades.convertirAEntero(codigoPaciente));
			
			rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				embarazo = rs.getInt("embarazo");
			
			return embarazo;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarNumeroEmbarazoHojaObstetrica de SqlBaseInformacionPartoDao: "+e);
			return 0;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
	}
	
	/**
	 * Método que verifica si ya existe un numero de embarazo en los registros de hoja obstétrica del paciente
	 * @param con
	 * @param codigoPaciente
	 * @param numeroEmbarazo
	 * @return
	 */
	public static boolean existeNumeroEmbarazoHojaObstetrica(Connection con,String codigoPaciente,int numeroEmbarazo)
	{
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs =  null;
		try
		{
			boolean existe = false;
			ps =  new PreparedStatementDecorator(con.prepareStatement(existeNumeroEmbarazoHojaObstetricaStr));
			ps.setInt(1,Utilidades.convertirAEntero(codigoPaciente));
			ps.setInt(2,numeroEmbarazo);
			
			 rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					existe = true;
			
			return existe;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeNumeroEmbarazoHojaObstetrica de SQlBaseInformacionPartoDao: "+e);
			return false;
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
	}
	
	/**
	 * Método implementado para obtener las consultas prenatales de la hoja obstetrica
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int obtenerConsultasPrenatalesHojaObstetrica(Connection con,HashMap campos)
	{
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs =  null;
		try
		{
			int cuenta = 0;
			pst =  new PreparedStatementDecorator(con.prepareStatement(obtenerConsultasPrenatalesHojaObstetricaStr));
			pst.setInt(1,Utilidades.convertirAEntero(campos.get("codigoPaciente")+""));
			pst.setInt(2, Utilidades.convertirAEntero(campos.get("numeroEmbarazo")+""));
			
			 rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				cuenta = rs.getInt("cuenta");
			
			return cuenta;
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerConsultasPrenatalesHojaObstetrica de SqlBaseInformacionPartoDao: "+e);
			return 0;
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public static HashMap cargarVigilanciaClinica(	Connection con, 
					        						String consecutivoInfoParto
												 )
	{
		PreparedStatementDecorator cargarStatement = null;
		
	    try
		{
	    	cargarStatement= new PreparedStatementDecorator(con.prepareStatement(cargarVigilanciaClinicaStr));
			cargarStatement.setString(1, consecutivoInfoParto);
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(cargarStatement.executeQuery()));
			cargarStatement.close();
			return mapaRetorno;
			
			
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta de cargarVigilanciaClinica: SqlBaseInformacionPartoDao "+e.toString());
			return new HashMap();
		}finally {			
			if(cargarStatement!=null){
				try{
					cargarStatement.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @param vigilanciaClinicaMap
	 * @param insertSecuencia
	 * @return
	 */
	public static boolean insertarVigilenciaClinica(Connection con, String consecutivoInfoParto, HashMap vigilanciaClinicaMap, String insertSecuencia, String usuario)
	{
		String insertStr=	"INSERT INTO vig_cli_trab_parto (	consecutivo, " +
															"info_parto, " +
															"hora, " +
															"fecha, " +
															"posicion_materna, " +
															"tension_arterial, " +
															"pulso_materno, " +
															"frec_cardiaca_fetal, " +
															"duracion_contracciones, " +
															"frec_contracciones, " +
															"intensidad_dolor, " +
															"localizacion_dolor, " +
															"dilatacion, " +
															"variedad_posicion," +
															"fecha_proceso," +
															"hora_proceso," +
															"usuario," +
															"tension_arterial_1," +
															"fcf," +
															"estacion ) " +
						     "VALUES ( 	"+insertSecuencia+", " +
						     			"?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?, ?, ?, CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?, ?) ";
		
		// se comienza iterando el mapa
		for(int w=0; w<Utilidades.convertirAEntero(vigilanciaClinicaMap.get("numRegistros").toString()); w++)
		{
			if(vigilanciaClinicaMap.get("estabd_"+w).toString().equals("false") && vigilanciaClinicaMap.get("fueeliminado_"+w).toString().equals("false"))
			{	
				PreparedStatementDecorator ps=  null;
				try
				{
					ps=  new PreparedStatementDecorator(con.prepareStatement(insertStr));
					
					logger.info("consecutivoInfoParto: "+consecutivoInfoParto);
					logger.info("consecutivoInfoParto: "+consecutivoInfoParto);
					
					ps.setDouble(1, Utilidades.convertirADouble(consecutivoInfoParto));
					ps.setString(2, vigilanciaClinicaMap.get("hora_"+w).toString());
					ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vigilanciaClinicaMap.get("fecha_"+w).toString())));
					ps.setString(4, vigilanciaClinicaMap.get("acronimoposicionmaterna_"+w).toString());
					ps.setInt(5, Utilidades.convertirAEntero(vigilanciaClinicaMap.get("tensionarterial_"+w).toString()));
					
					if(!vigilanciaClinicaMap.get("pulsomaterno_"+w).toString().equals(""))
						ps.setInt(6, Utilidades.convertirAEntero(vigilanciaClinicaMap.get("pulsomaterno_"+w).toString()));
					else
						ps.setNull(6, Types.INTEGER);
					
					ps.setString(7, vigilanciaClinicaMap.get("acronimofreccardiacafetal_"+w).toString());
					
					if(!vigilanciaClinicaMap.get("duracioncontracciones_"+w).toString().equals(""))
						ps.setInt(8, Utilidades.convertirAEntero(vigilanciaClinicaMap.get("duracioncontracciones_"+w).toString()));
					else
						ps.setNull(8, Types.INTEGER);
					
					if(!vigilanciaClinicaMap.get("freccontracciones_"+w).toString().equals(""))
						ps.setInt(9, Utilidades.convertirAEntero(vigilanciaClinicaMap.get("freccontracciones_"+w).toString()));
					else
						ps.setNull(9, Types.INTEGER);
					
					ps.setString(10, vigilanciaClinicaMap.get("acronimointensidaddolor_"+w).toString());
					ps.setString(11, vigilanciaClinicaMap.get("acronimolocalizaciondolor_"+w).toString());
					
					ps.setInt(12, Utilidades.convertirAEntero(vigilanciaClinicaMap.get("dilatacion_"+w).toString()));
					ps.setInt(13, Utilidades.convertirAEntero(vigilanciaClinicaMap.get("codigovariedadposicion_"+w).toString()));
					
					ps.setString(14, usuario);
					
					ps.setInt(15, Utilidades.convertirAEntero(vigilanciaClinicaMap.get("tensionarterial1_"+w).toString()));
					
					ps.setInt(16, Utilidades.convertirAEntero(vigilanciaClinicaMap.get("fcf_"+w).toString()));
					
					ps.setInt(17, Utilidades.convertirAEntero(vigilanciaClinicaMap.get("estacion_"+w).toString()));
					
					if(ps.executeUpdate()<=0)
					{
						return false;
					}
				}
				catch (SQLException e) 
				{
					logger.warn(" Error en el insertar codigo Info parto:"+consecutivoInfoParto+" SqlBaseInformacionPartoDao "+e.toString());
					return false;
				}finally {			
					if(ps!=null){
						try{
							ps.close();					
						}catch(SQLException sqlException){
							logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
						}
					}
				}
			}	
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public static boolean existePartogramaDadoConsecutivoInfoParto(Connection con, String consecutivoInfoParto)
	{
		String cadena="select consecutivo_info_parto from  partograma_clap where consecutivo_info_parto=?";
		PreparedStatementDecorator ps=  null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setDouble(1, Utilidades.convertirADouble(consecutivoInfoParto));
			if(ps.executeQuery().next())
				return true;
		}
		catch (SQLException e) 
		{
			logger.warn(" Error en consulta info parto:"+consecutivoInfoParto+" SqlBaseInformacionPartoDao "+e.toString());
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @param partogramaMap
	 * @return
	 */
	public static boolean insertarPartograma (	Connection con,
												String consecutivoInfoParto,
												HashMap partogramaMap
											  )
	{
		String insertarPartogramaClapStr= " INSERT INTO partograma_clap (consecutivo_info_parto, posicion, paridad, membrana) VALUES (?, ?, ?, ?) ";
		PreparedStatementDecorator ps= null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(insertarPartogramaClapStr));
			ps.setDouble(1, Utilidades.convertirADouble(consecutivoInfoParto));
			ps.setInt(2, Utilidades.convertirAEntero(partogramaMap.get("codigoposicion").toString()));
			ps.setInt(3, Utilidades.convertirAEntero(partogramaMap.get("codigoparidad").toString()));
			ps.setInt(4, Utilidades.convertirAEntero(partogramaMap.get("codigomembrana").toString()));
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e) 
		{
			logger.warn(" Error en el insertar partograma consecutivo info parto:"+consecutivoInfoParto+" SqlBaseInformacionPartoDao "+e.toString());
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @param partogramaMap
	 * @return
	 */
	public static boolean modificarPartograma (	Connection con,
												String consecutivoInfoParto,
												HashMap partogramaMap
											  )
	{
		String modificar="UPDATE partograma_clap SET  posicion=?,  paridad=?, membrana=? where consecutivo_info_parto=? ";
		PreparedStatementDecorator ps= null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(modificar));
			ps.setInt(1, Utilidades.convertirAEntero(partogramaMap.get("codigoposicion").toString()));
			ps.setInt(2, Utilidades.convertirAEntero(partogramaMap.get("codigoparidad").toString()));
			ps.setInt(3, Utilidades.convertirAEntero(partogramaMap.get("codigomembrana").toString()));
			ps.setDouble(4, Utilidades.convertirADouble(consecutivoInfoParto));
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e) 
		{
			logger.warn(" Error en el modificar partograma consecutivo info parto:"+consecutivoInfoParto+" SqlBaseInformacionPartoDao "+e.toString());
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public static HashMap cargarPartograma(Connection con, String consecutivoInfoParto)
	{
		HashMap mapa= new HashMap();
		HashMap mapaFinal= new HashMap();
		String cadena="SELECT posicion as codigoposicion, paridad as codigoparidad, membrana as codigomembrana FROM partograma_clap WHERE consecutivo_info_parto=?";
		PreparedStatementDecorator ps = null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setDouble(1, Utilidades.convertirADouble(consecutivoInfoParto));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,false);
			
			mapaFinal.put("codigoposicion", mapa.get("codigoposicion")==null?"":mapa.get("codigoposicion").toString());
			mapaFinal.put("codigoparidad", mapa.get("codigoparidad")==null?"":mapa.get("codigoparidad").toString());
			mapaFinal.put("codigomembrana", mapa.get("codigomembrana")==null?"":mapa.get("codigomembrana").toString());
			
			mapaFinal.put("ultimaFechaHoraProceso", obtenerUltimaFechaHoraProcesoPartograma(con, consecutivoInfoParto));
		}
		catch (SQLException e) 
		{
			logger.warn(" Error en el cargar det partograma consecutivo info parto:"+consecutivoInfoParto+"  SqlBaseInformacionPartoDao "+e.toString());
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
		
		return mapaFinal;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCx
	 * @return
	 */
	public static String obtenerConsecutivoInfoPartoDadoCx(Connection con, String codigoCx)
	{
		String cadena="SELECT consecutivo as cod FROM informacion_parto WHERE cirugia=?";
		PreparedStatementDecorator ps= null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, Utilidades.convertirAEntero(codigoCx));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("cod");
		}
		catch (SQLException e) 
		{
			logger.warn(" Error en el consultar consecutivo info parto para el cod de cx="+codigoCx+" SqlBaseInformacionPartoDao "+e.toString());
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCx
	 * @param codigoInstitucion
	 * @param fromStatement
	 * @return
	 */
	public static String obtenerControlPrenatal(Connection con, String codigoCx, int codigoInstitucion, String fromStatement)
	{
		String cadena=	" SELECT " +
						"CASE WHEN (SELECT ip.control_prenatal FROM informacion_parto ip WHERE ip.cirugia=?) IS NULL " +
						"OR (SELECT ip.control_prenatal FROM informacion_parto ip WHERE ip.cirugia=?)='"+ConstantesBD.acronimoSi+"' " +
						"THEN (SELECT i.razon_social FROM instituciones i WHERE i.codigo=?) " +
						"ELSE (SELECT ip.en_donde FROM informacion_parto ip WHERE ip.cirugia=?) END AS controlPrenatal "+fromStatement;
		logger.info("\nobtenerControlPrenatal-->"+cadena+" ->codCx->"+codigoCx+" ints->"+codigoInstitucion);
		PreparedStatementDecorator ps= null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, Utilidades.convertirAEntero(codigoCx));
			ps.setInt(2, Utilidades.convertirAEntero(codigoCx));
			ps.setInt(3, codigoInstitucion);
			ps.setInt(4, Utilidades.convertirAEntero(codigoCx));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("controlPrenatal");
		}
		catch (SQLException e) 
		{
			logger.warn(" Error en el obtenerControlPrenatal para el cod de cx="+codigoCx+" SqlBaseInformacionPartoDao "+e.toString());
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public static String obtenerUltimaFechaHoraProcesoPartograma(Connection con, String consecutivoInfoParto)
	{
		String cadena="SELECT to_char(fecha_proceso,'DD/MM/YYYY') ||' '|| hora_proceso as fecha " +
								"FROM vig_cli_trab_parto " +
								"WHERE info_parto=? and consecutivo=(SELECT max(consecutivo) FROM vig_cli_trab_parto  WHERE info_parto=?)";
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			ps=  new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setDouble(1, Utilidades.convertirADouble(consecutivoInfoParto));
			ps.setDouble(2, Utilidades.convertirADouble(consecutivoInfoParto));
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getString("fecha");
		}
		catch (SQLException e) 
		{
			logger.warn(" Error en el obtenerUltimaFechaHoraProcesoPartograma para el consecutivo info parto="+consecutivoInfoParto+" SqlBaseInformacionPartoDao "+e.toString());
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
		return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @param vigilanciaClinicaMap
	 * @return
	 */
	public static boolean modificarVigilanciaParto (	Connection con, String consecutivoInfoParto, HashMap vigilanciaClinicaMap, String usuario )
	{
		String modificarStr=	"UPDATE vig_cli_trab_parto set " +
								"hora=?, " +
								"fecha=?, " +
								"posicion_materna=?, " +
								"tension_arterial=?, " +
								"pulso_materno=?, " +
								"frec_cardiaca_fetal=?, " +
								"duracion_contracciones=?, " +
								"frec_contracciones=?, " +
								"intensidad_dolor=?, " +
								"localizacion_dolor=?, " +
								"dilatacion=?, " +
								"variedad_posicion=?, " +
								"fecha_proceso= CURRENT_DATE, " +
								"hora_proceso= "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
								"usuario=?, " +
								"tension_arterial_1=?, " +
								"fcf=?, " +
								"estacion=? " +
								"where consecutivo=? ";

        // se comienza iterando el mapa
		for(int w=0; w<Utilidades.convertirAEntero(vigilanciaClinicaMap.get("numRegistros").toString()); w++)
		{
			if(vigilanciaClinicaMap.get("estabd_"+w).toString().equals("true") && vigilanciaClinicaMap.get("fueeliminado_"+w).toString().equals("false"))
			{	
				PreparedStatementDecorator ps= null;
				try
				{
					ps=  new PreparedStatementDecorator(con.prepareStatement(modificarStr));
					
					ps.setString(1, vigilanciaClinicaMap.get("hora_"+w).toString());
					ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(vigilanciaClinicaMap.get("fecha_"+w).toString())));
					ps.setString(3, vigilanciaClinicaMap.get("acronimoposicionmaterna_"+w).toString());
					ps.setInt(4, Utilidades.convertirAEntero(vigilanciaClinicaMap.get("tensionarterial_"+w).toString()));

					if(!vigilanciaClinicaMap.get("pulsomaterno_"+w).toString().equals(""))
						ps.setInt(5, Utilidades.convertirAEntero(vigilanciaClinicaMap.get("pulsomaterno_"+w).toString()));
					else
						ps.setNull(5, Types.INTEGER);

					ps.setString(6, vigilanciaClinicaMap.get("acronimofreccardiacafetal_"+w).toString());

					if(!vigilanciaClinicaMap.get("duracioncontracciones_"+w).toString().equals(""))
						ps.setString(7, vigilanciaClinicaMap.get("duracioncontracciones_"+w).toString());
					else
						ps.setNull(7, Types.VARCHAR);

					if(!vigilanciaClinicaMap.get("freccontracciones_"+w).toString().equals(""))
						ps.setInt(8, Utilidades.convertirAEntero(vigilanciaClinicaMap.get("freccontracciones_"+w).toString()));
					else
						ps.setNull(8, Types.INTEGER);

					ps.setString(9, vigilanciaClinicaMap.get("acronimointensidaddolor_"+w).toString());
					ps.setString(10, vigilanciaClinicaMap.get("acronimolocalizaciondolor_"+w).toString());

					ps.setInt(11, Utilidades.convertirAEntero(vigilanciaClinicaMap.get("dilatacion_"+w).toString()));
					ps.setInt(12, Utilidades.convertirAEntero(vigilanciaClinicaMap.get("codigovariedadposicion_"+w).toString()));
					
					ps.setString(13, usuario);
					
					ps.setInt(14, Utilidades.convertirAEntero(vigilanciaClinicaMap.get("tensionarterial1_"+w).toString()));
					
					ps.setInt(15, Utilidades.convertirAEntero(vigilanciaClinicaMap.get("fcf_"+w).toString()));
					
					ps.setInt(16, Utilidades.convertirAEntero(vigilanciaClinicaMap.get("estacion_"+w).toString()));
					
					ps.setDouble(17, Utilidades.convertirADouble(vigilanciaClinicaMap.get("consecutivo_"+w).toString()));
					
					if(ps.executeUpdate()<=0)
					{
						return false;
					}
				}
				catch (SQLException e) 
				{
					logger.warn(" Error en el insertar codigo Info parto:"+consecutivoInfoParto+" SqlBaseInformacionPartoDao "+e.toString());
					return false;
				}finally {			
					if(ps!=null){
						try{
							ps.close();					
						}catch(SQLException sqlException){
							logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
						}
					}
				}
			}	
		}
		return true;
	}

	
	/**
	 * 
	 * @param con
	 * @param cirugia
	 * @return
	 */
	public static boolean esCirugiaAborto(Connection con, int cirugia)
	{
		String cadena="SELECT parto from informacion_parto where cirugia = ?";
		logger.info("CIRUGIA -->"+cirugia);

		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, cirugia);
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return !UtilidadTexto.getBoolean(rs.getString(1));
			}
		}
		catch(SQLException e)
		{
			logger.error("ERROR CONSULTADO SI LA CIRUGIA SI ES ABORTO O PARTO "+e.getStackTrace());
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
		return false;
	}	

	
	/**
	 * 
	 * @param con
	 * @param cirugia
	 * @return
	 */
	public static int cantidadHijosVivosMuertos(Connection con, int cirugia)
	{
		String cadena=" SELECT case when cantidad_hijos_vivos is null then 0 else cantidad_hijos_vivos end + case when cantidad_hijos_muertos is null then 0 else cantidad_hijos_muertos end as cantidadhijos from informacion_parto where cirugia=?";
		logger.info("CIRUGIA -->"+cirugia);
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena));
			ps.setInt(1, cirugia);
			ps.executeQuery();
			if(rs.next())
			{
				if(rs.getInt(1)>0)
					return rs.getInt(1);
			}
		}
		catch(SQLException e)
		{
			logger.error("ERROR CONSULTADO LA CANTIDAAD DE HIJOS DE UNA CIRUGIA "+e.getStackTrace());
		}finally {			
			if(ps!=null){
				try{
					ps.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Método implementado para consultar la fecha/hora ingreso a hospitalizacion
	 * a partir de la cuenta asociada
	 * @param con
	 * @param cuentaAsocio
	 * @return
	 */
	public static String[] consultarFechaIngresoCasoAsocio(Connection con,String cuentaAsocio)
	{
		String[] fechas = new String[0];
		PreparedStatementDecorator pst = null;
		ResultSetDecorator rs = null;
		try
		{
			pst =  new PreparedStatementDecorator(con.prepareStatement(consultarFechaIngresoAsocioStr));
			pst.setInt(1,Utilidades.convertirAEntero(cuentaAsocio));
			
			rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				fechas = new String[2];
				fechas[0] =	rs.getString("fecha_admision"); 
				fechas[1] =	rs.getString("hora_admision");
			}
			
			return fechas;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarFechaIngresoCasoAsocio:  "+e);
			return fechas;
		}finally {			
			if(pst!=null){
				try{
					pst.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
			if(rs!=null){
				try{
					rs.close();					
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseInformacionPartoDao "+sqlException.toString() );
				}
			}
		}
	}
	
}