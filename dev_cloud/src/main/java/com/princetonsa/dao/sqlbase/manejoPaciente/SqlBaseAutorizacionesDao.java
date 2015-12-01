package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.dto.manejoPaciente.DtoAdjAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoCuentaAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacionEst;
import com.princetonsa.dto.manejoPaciente.DtoDiagAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoEnvioAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoRespAutorizacion;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.Autorizaciones;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatos;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseAutorizacionesDao {
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseAutorizacionesDao.class);
	
	/**
	 * Insertar Autorizacion 
	 */
	private static String strInsertarAutorizacion="INSERT INTO manejopaciente.autorizaciones (" +
			"codigo, " +
			"consecutivo, " +
			"anio_consecutivo, " +
			"ingreso, " +
			"sub_cuenta, " +
			"tipo_servicio_solicitado, " +
			"observaciones , " +
			"tipo_cobertura, " +
			"origen_atencion, " +
			"fecha_solicitud, " +
			"hora_solicitud, " +
			"usuario_solicitud," +
			"tipo_tramite, " +
			"convenio, " +
			"prioridad_atencion, " +
			"cuenta, " +
			"cama, " +
			"fecha_modifica, " +
			"hora_modifica, " +
			"usuario_modifica, " +
			"persona_solicita," +
			"tipo " +
			") " +
			"VALUES (?,?,?,?,?,?,?,?,?, "+
			"CURRENT_DATE, " +
			""+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
			"?,?,?,?,?,?," +
			"CURRENT_DATE, " +
			""+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?)";
	
	/**
	 * Actualiza la autorizacion
	 * */
	private static String strActualizarAutorizacion = "UPDATE autorizaciones SET " +			
			"ingreso=?, " +
			"sub_cuenta=?, " +
			"tipo_servicio_solicitado=?, " +
			"observaciones=?, " +
			"tipo_cobertura=?, " +
			"origen_atencion=?, " +
			"fecha_solicitud=?, " +
			"hora_solicitud=?, " +
			"usuario_solicitud=?, " +
			"tipo_tramite=?, " +
			"convenio=?, " +
			"prioridad_atencion=?, " +
			"cuenta=?, " +
			"cama=?, " +
			"fecha_modifica=?, " +
			"hora_modifica=?, " +
			"usuario_modifica=?, " +
			"persona_solicita=? " +
			"WHERE codigo = ? "; 

	/**
	 * Insertar Detalle Autorizacion 
	 */
	private static String strInsertarDetalleAutorizacion="INSERT INTO det_autorizaciones (" +
			"codigo, "+
			"autorizacion, "+
			"numero_solicitud, "+
			"orden_ambulatoria, "+
			"det_cargo, "+
			"servicio, "+
			"articulo, "+
			"cantidad, "+
			"estado, "+
			"activo, "+
			"justificacion_solicitud, "+
			"fecha_modifica, "+
			"hora_modifica, "+
			"usuario_modifica,"+
			"tipo_asocio, "+
			"servicio_cx)" +
			"VALUES (?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?)";
	
	/**
	 * Insertar Detalle Autorizacion Estancia
	 */
	private static String strInsertarDetalleAutorizacionEstancia="INSERT INTO det_autorizaciones_estancia (" +
			"codigo, "+
			"det_autorizacion, "+
			"numero_solicitud, "+
			"det_cargo," +
			"activo)" +
			"VALUES (?,?,?,?,?)";
	
	/**
	 * Carga la informacion basica del detalle de autorizacion
	 * */
	private static final String strCargarInfoBasicaDetAutoXCargo = "SELECT " +
			"det.solicitud AS numero_solicitud," +
			ConstantesBD.codigoNuncaValido+" AS orden_ambulatoria," +
			"det.codigo_detalle_cargo AS det_cargo," +
			"coalesce(det.servicio,"+ConstantesBD.codigoNuncaValido+")  AS servicio, " +
			"coalesce(det.articulo,"+ConstantesBD.codigoNuncaValido+") AS articulo, " +
			"CASE WHEN det.cantidad_cargada IS NULL THEN coalesce(ds.cantidad,"+ConstantesBD.codigoNuncaValido +") ELSE coalesce(det.cantidad_cargada,"+ConstantesBD.codigoNuncaValido+") END AS cantidad," +
			"coalesce(det.tipo_asocio,"+ConstantesBD.codigoNuncaValido+") AS tipo_asocio," +
			"coalesce(det.servicio_cx,"+ConstantesBD.codigoNuncaValido+") AS servicio_cx ," +
			"CASE WHEN det.servicio IS NULL THEN '"+ConstantesBD.codigoNuncaValido+"' ELSE gettiposervicio(det.servicio) END AS tipo_servicio, " +
			"s.tipo AS tipo_solicitud, " +
			"to_char(s.fecha_solicitud,'dd/mm/yyyy') AS fecha_solicitud," +
			"s.estado_historia_clinica AS estado," +
			"coalesce(deta.codigo,"+ConstantesBD.codigoNuncaValido+") AS codigo_det_auto," +
			"coalesce(s.justificacion_solicitud,'') AS justificacion_solicitud," +
			"s.urgente||'' AS urgente " +
			"FROM " +
			"det_cargos det " +
			"INNER JOIN solicitudes s ON (s.numero_solicitud = det.solicitud) " +
			"LEFT OUTER JOIN ordenes.detalle_solicitudes ds ON (ds.numero_solicitud = s.numero_solicitud ) " +
			"LEFT OUTER JOIN det_autorizaciones deta ON (deta.det_cargo = det.codigo_detalle_cargo AND deta.activo = '"+ConstantesBD.acronimoSi+"') " +
			"WHERE " +
			"det.codigo_detalle_cargo = ? ";	
	
	/**
	 * Carga la informacion basica del detalle de autorizacion
	 * */
	private static String strCargarInfoBasicaDetAutoXAmbula = "SELECT " +
			ConstantesBD.codigoNuncaValido+" AS numero_solicitud," +
			"oa.codigo AS orden_ambulatoria," +
			ConstantesBD.codigoNuncaValido+" AS det_cargo," +			
			"coalesce(dos.servicio,"+ConstantesBD.codigoNuncaValido+")  AS servicio, " +
			"coalesce(doa.articulo,"+ConstantesBD.codigoNuncaValido+") AS articulo, " +
			"CASE WHEN dos.servicio IS NULL THEN coalesce(doa.cantidad,"+ConstantesBD.codigoNuncaValido+") ELSE coalesce(dos.cantidad,"+ConstantesBD.codigoNuncaValido+") END AS cantidad," +
			ConstantesBD.codigoNuncaValido+" AS tipo_asocio," +
			ConstantesBD.codigoNuncaValido+" AS servicio_cx ," +
			"CASE WHEN dos.servicio IS NULL THEN '"+ConstantesBD.codigoNuncaValido+"' ELSE gettiposervicio(dos.servicio) END AS tipo_servicio, " +
			"coalesce(oa.tipo_orden,"+ConstantesBD.codigoNuncaValido+") AS tipo_solicitud, " +
			"to_char(oa.fecha,'dd/mm/yyyy') AS fecha_solicitud," +
			"oa.estado AS estado," +
			"coalesce(deta.codigo,"+ConstantesBD.codigoNuncaValido+") AS codigo_det_auto," +
			"'' AS justificacion_solicitud, " +
			"'"+ConstantesBD.acronimoNo+"' AS urgente "+
			"FROM " +			
			"ordenes_ambulatorias oa " +			
			"LEFT OUTER JOIN det_orden_amb_articulo doa ON (doa.codigo_orden = oa.codigo  ) " +
			"LEFT OUTER JOIN det_orden_amb_servicio dos ON (dos.codigo_orden = oa.codigo  ) " +
			"LEFT OUTER JOIN det_autorizaciones deta ON (deta.orden_ambulatoria = oa.codigo AND deta.activo = '"+ConstantesBD.acronimoSi+"') " +
			"WHERE " +
			"oa.codigo = ? ";
	
	private static String strConsultarInfoBasicaSolicitud = 
		"SELECT " +
		"s.numero_solicitud," +
		"s.tipo ," +
		"to_char(s.fecha_solicitud,'dd/mm/yyyy') AS fecha_solicitud " +		
		"FROM solicitudes s " +
		"WHERE s.numero_solicitud IN (?) ";
	
	/**
	 * Insertar Envio Autorizaciones
	 */
	private static String strInsertarEnvioAutorizacion="INSERT INTO envio_autorizaciones (" +
			"codigo_pk, "+
			"det_autorizacion, "+
			"entidad_envio, "+
			"convenio_envio, "+
			"medio_envio, "+
			"fecha_modifica, "+
			"hora_modifica, "+
			"usuario_modifica," +
			"path_archivo)" +
			"VALUES (?,?,?,?,?,?,?,?,?)";
	
	private static String strInsertarDiagAutorizacion="INSERT INTO diag_autorizaciones (" +
			"codigo, "+
			"autorizacion, "+
			"acronimo, "+
			"tipo_cie, "+
			"principal, "+
			"fecha_modifica, "+
			"hora_modifica, "+
			"usuario_modifica) "+
			"VALUES (?,?,?,?,?,?,?,?)";
	
	private static String strInsertarAdjuntoAutorizacion="INSERT INTO adj_autorizaciones (" +
			"codigo_pk, "+
			"autorizacion, "+
			"nombre_archivo, "+
			"nombre_original, "+
			"fecha_modifica, "+
			"hora_modifica, "+
			"usuario_modifica) "+
			"VALUES (?,?,?,?,?,?,?)";
	
	private static String strInsertarRespuestaAutorizacion="INSERT INTO resp_autorizaciones (" +
			"det_autorizacion, "+
			"vigencia, "+
			"tipo_vigencia, "+
			"numero_autorizacion, "+
			"persona_autoriza, "+
			"valor_cobertura, "+
			"tipo_cobertura, "+
			"valor_pago_paciente, "+
			"tipo_pago_paciente, "+
			"persona_recibe, "+
			"cargo_pers_recibe, "+
			"persona_registro, "+
			"cargo_pers_registro, "+
			"fecha_registro, "+
			"hora_registro, "+
			"fecha_autorizacion, "+
			"hora_autorizacion, " +
			"cantidad_solicitada, " +
			"cantidad_autorizada, " +
			"fecha_ini_autorizada, " +
			"fecha_fin_autorizada," +
			"observaciones_autorizacion) "+
			"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?,?,?,?)";
	
	private static String strConsultarRespuestasAutorizacion = "SELECT " +
			"det_autorizacion," +
			"coalesce(vigencia||'','') AS vigencia,"+
			"coalesce(tipo_vigencia,"+ConstantesBD.codigoNuncaValido+") AS tipo_vigencia," +
			"CASE WHEN tipo_vigencia IS NULL THEN '' ELSE t.tipo END AS tipo_dia," +
			"coalesce(t.nombre,'') AS nombre_tipo_vigencia," + 
			"numero_autorizacion," +
			"persona_autoriza," +
			"coalesce(valor_cobertura||'','') AS valor_cobertura," +			 
			"coalesce(tipo_cobertura||'','') AS tipo_cobertura," + 
			"coalesce(valor_pago_paciente||'','') AS valor_pago_paciente," + 
			"coalesce(tipo_pago_paciente||'','') AS tipo_pago_paciente," + 
			"coalesce(persona_recibe||'','') AS persona_recibe," + 
			"coalesce(cargo_pers_recibe||'','') AS cargo_pers_recibe," +
			"coalesce(cu.nombre,'') AS nombre_cargo_pers_recibe," +
			"persona_registro," +
			"getnombreusuario(persona_registro) AS nombre_perso_registro," +
			"to_char(fecha_registro,'dd/mm/yyyy') AS fecha_registro," +
			"hora_registro," +
			"to_char(fecha_autorizacion,'dd/mm/yyyy') AS fecha_autorizacion," +
			"hora_autorizacion," +
			"coalesce(to_char(fecha_anulacion,'dd/mm/yyyy'),'') AS fecha_anulacion," + 
			"coalesce(hora_anulacion||'','') AS hora_anulacion," + 
			"coalesce(usuario_anulacion||'','') AS usuario_anulacion," + 
			"coalesce(motivo_anulacion||'','') AS motivo_anulacion," + 
			"cantidad_solicitada," +
			"cantidad_autorizada," +
			"coalesce(to_char(fecha_ini_autorizada,'dd/mm/yyyy'),'') AS fecha_ini_autorizada," + 
			"coalesce(to_char(fecha_fin_autorizada,'dd/mm/yyyy'),'') AS fecha_fin_autorizada," + 
			"cargo_pers_registro," +
			"coalesce(observaciones_autorizacion||'','') AS observaciones_autorizacion, " +
			"CASE WHEN cargo_pers_recibe IS NULL THEN '' ELSE getnomcargousuario(cargo_pers_recibe) END AS nom_cargo_per_rec, " +
			"CASE WHEN cargo_pers_registro IS NULL THEN '' ELSE getnomcargousuario(cargo_pers_registro) END AS nom_cargo_per_reg " +
			"FROM " +
			"resp_autorizaciones " +
			"LEFT OUTER JOIN tipos_vigencia t ON (t.codigo = tipo_vigencia) " +
			"LEFT OUTER JOIN cargos_usuarios cu ON (cu.codigo = cargo_pers_recibe ) " +
			"WHERE " +
			"1=1 ";
	
	private static String strInsertarAdjuntoRespAutorizacion="INSERT INTO adj_resp_autorizaciones (" +
			"codigo_pk, "+
			"resp_autorizacion, "+
			"nombre_archivo, "+
			"nombre_original, "+
			"fecha_modifica, "+
			"hora_modifica, "+
			"usuario_modifica) "+
			"VALUES (?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
	
	/**
	 * Encargado de insertar en los logs en la tabla 
	 * log_actualiz_autorizaciones
	 */
	private final static String ingresarLog=" INSERT  INTO log_actualiz_autorizaciones " +
			"(consecutivo," +
			"solicitud," +
			"servicio_articulo, " +
			"autorizacion_ant," +
			"autorizacion," +
			"sub_cuenta, " +
			"institucion," +
			"fecha_modifica," +
			"hora_modifica, " +
			"usuario_modifica," +
			"asocio," +
			"orden_ambulatoria) " +
			"VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Actualiza la informacion de la cobertura en salud
	 * */
	private final static String actualizarCoberturaSalud = "UPDATE sub_cuentas SET tipo_cobertura = ? WHERE sub_cuenta = ? ";
	
	/**
	 * Encargado de actualizar el numero de autorizacion de un servicio o articulo de la tabla det_cargos
	 */
	private final static String actualizarAutorizacionServicioArticulo = 
		" UPDATE det_cargos SET nro_autorizacion=? WHERE codigo_detalle_cargo=? ";
    
	/**
	 * Encargado de actualizar el numero de Solicitud y cargo de la tabla detalle autorizacion 
	 */
	private final static String actualizarDetAutorizacionSolicitudyCargo =
		"UPDATE det_autorizaciones SET numero_solicitud = ? , det_cargo = ? WHERE orden_ambulatoria = ? ";

	/**
	 * Encargado de actualizar la sub_cuenta , el convenio y la cuenta de la tabla autorizaciones
	 */
	private final static String actualizarAutorizacionSbCuentaConvenioyCuenta =
		"UPDATE autorizaciones SET sub_cuenta = ? , convenio = ? ,  cuenta = ?  WHERE codigo = ? ";
	
	/**
	 * Inserta Informacion de la Anulación
	 * */
	private final static String strActualizarMotivoAnulacion = 
		"UPDATE resp_autorizaciones SET fecha_anulacion = ?, hora_anulacion = ?, usuario_anulacion = ?, motivo_anulacion = ? WHERE det_autorizacion = ? ";

	 
   private static String strDeleteDiagRAutorizacion="DELETE FROM diag_autorizaciones AS diag " +
			"WHERE diag.codigo = ? AND (diag.principal = 'N' OR diag.principal = 'n')";
	
	private static String strDeleteArchivoAdjAutorizacion="DELETE FROM adj_autorizaciones AS adj " +
			"WHERE adj.codigo_pk = ? ";
	
	private final static String cargarAutorizacionStr = "SELECT "+ 
		"a.codigo as codigo, "+
		"a.consecutivo as consecutivo, "+
		"a.anio_consecutivo as anio_consecutivo, "+
		"a.ingreso as id_ingreso, "+
		"a.sub_cuenta as id_sub_cuenta, "+
		"a.tipo_servicio_solicitado as cod_tipo_ser_sol, "+
		"coalesce(tss.nombre,'') as nom_tipo_ser_sol, "+
		"coalesce(a.observaciones,'') as observaciones, "+
		"coalesce(a.tipo_cobertura,"+ConstantesBD.codigoNuncaValido+") as cod_tipo_cobertura, "+
		"coalesce(cs.nombre,'') as nom_tipo_cobertura, "+
		"coalesce(a.origen_atencion,"+ConstantesBD.codigoNuncaValido+") as cod_origen_atencion, "+
		"coalesce(ce.nombre,'') as nom_origen_atencion, "+
		"coalesce(to_char(a.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_solicitud, "+
		"coalesce(a.hora_solicitud,'') as hora_solicitud, "+
		"coalesce(a.usuario_solicitud,'') as usuario_solicitud, "+
		"coalesce(a.tipo_tramite,'') as tipo_tramite, "+
		"a.convenio as codigo_convenio, "+
		"getnombreconvenio(a.convenio) as nombre_convenio, "+
		"coalesce(a.prioridad_atencion,'') as prioridad_atencion, "+
		"a.cuenta as id_cuenta, "+
		"coalesce(a.cama,"+ConstantesBD.codigoNuncaValido+") as codigo_cama, "+
		"coalesce(getdescripcioncamaphc(a.cama),'') as nombre_cama, "+
		"to_char(a.fecha_modifica,'') as fecha_modifica, "+
		"a.hora_modifica, "+
		"a.usuario_modifica, " +
		"coalesce(a.persona_solicita,"+ConstantesBD.codigoNuncaValido+") as persona_solicita," +
		"CASE WHEN a.persona_solicita IS NULL THEN '' ELSE getnombrepersona(a.persona_solicita) END as nom_persona_solicita," +
		"a.tipo AS tipo_autorizacion "+
		"FROM autorizaciones a " +
		"INNER JOIN det_autorizaciones d ON (d.autorizacion = a.codigo) "+ 
		"LEFT OUTER JOIN tipos_ser_sol tss ON(tss.codigo = a.tipo_servicio_solicitado) "+ 
		"LEFT OUTER JOIN coberturas_salud cs ON(cs.codigo = a.tipo_cobertura) "+ 
		"LEFT OUTER JOIN causas_externas ce ON(ce.codigo = a.origen_atencion) "+ 
		"WHERE "+ 
		"1=1 ";
	
		private final static String cargarListadoAutorizacionStr = "SELECT "+ 
		"a.codigo as codigo, "+		
		"coalesce(to_char(a.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_solicitud, "+
		"coalesce(a.hora_solicitud,'') as hora_solicitud, "+		
		"getnombreconvenio(a.convenio) as nombre_convenio, "+
		"a.cuenta as id_cuenta, "+
		"a.ingreso as id_ingreso, "+
		"a.sub_cuenta as id_sub_cuenta, "+
		"a.tipo AS tipo_autorizacion, "+
		"d.estado," +
		"coalesce(a.tipo_tramite,'') AS tipo_tramite "+
		"FROM autorizaciones a " +
		"INNER JOIN det_autorizaciones d ON (d.autorizacion = a.codigo) "+
		"WHERE "+ 
		"1=1 ";
		
	private static String strConsultarDetalleAutorizaciones="SELECT "+ 
		"da.codigo as codigo, "+
		"da.autorizacion as autorizacion, "+
		"coalesce(da.numero_solicitud,"+ConstantesBD.codigoNuncaValido+") as numero_solicitud, "+
		"coalesce(da.orden_ambulatoria,"+ConstantesBD.codigoNuncaValido+") as orden_ambulatoria, "+
		"coalesce(da.det_cargo,"+ConstantesBD.codigoNuncaValido+") as det_cargo, "+
		"coalesce(da.servicio,"+ConstantesBD.codigoNuncaValido+") as codigo_servicio, "+
		"coalesce(getnombreservicio(da.servicio,?),'') as nombre_servicio, "+
		"CASE WHEN da.servicio IS NULL THEN '' ELSE getcodigopropservicio2(da.servicio,"+ConstantesBD.codigoTarifarioCups+") END AS codigo_cups, "+
		"coalesce(da.articulo,"+ConstantesBD.codigoNuncaValido+") as codigo_articulo, "+
		"coalesce(getdescripcionarticulo(da.articulo),'') as nombre_articulo, "+
		"CASE WHEN da.articulo IS NULL THEN '' ELSE getcodarticuloaxiomainterfaz(da.articulo,'"+ConstantesIntegridadDominio.acronimoAmbos+"') END AS codigo_cups_art, "+
		"coalesce(da.cantidad,0) as cantidad, "+
		"coalesce(da.justificacion_solicitud,'') as justificacion_solicitud, "+
		"to_char(da.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') as fecha_modifica, "+
		"da.hora_modifica as hora_modifica, "+
		"da.usuario_modifica as usuario_modifica," +
		"coalesce(da.servicio_cx,"+ConstantesBD.codigoNuncaValido+") as codigo_servicio_cx," +		
		"CASE WHEN da.servicio_cx IS NULL THEN '' ELSE getnombreservicio(da.servicio_cx,?) END as nombre_serviciocx, " +
		"CASE WHEN da.servicio_cx IS NULL THEN '' ELSE getcodigopropservicio2(da.servicio_cx,"+ConstantesBD.codigoTarifarioCups+") END AS codigo_cups_cx, "+
		"coalesce(da.tipo_asocio,"+ConstantesBD.codigoNuncaValido+") as tipo_asocio," +
		"CASE WHEN da.tipo_asocio IS NULL THEN '' ELSE getnomtipoasocio(da.tipo_asocio) END as nombre_tipo_asocio, " +
		"coalesce(da.estado,'') AS estado," +
		"CASE WHEN da.servicio IS NOT NULL THEN gettiposervicio(da.servicio) ELSE '' END AS tipo_servicio "+		
		"FROM det_autorizaciones da "+ 
		"WHERE 1=1 ";
	
	
	private static String strConsultarDetalleAutorizaciones2="SELECT "+ 
	"da.codigo as codigo, "+
	"da.autorizacion as autorizacion, "+
	"coalesce(da.numero_solicitud,"+ConstantesBD.codigoNuncaValido+") as numero_solicitud, "+
	"coalesce(da.orden_ambulatoria,"+ConstantesBD.codigoNuncaValido+") as orden_ambulatoria, "+
	"coalesce(da.det_cargo,"+ConstantesBD.codigoNuncaValido+") as det_cargo, "+
	"coalesce(da.servicio,"+ConstantesBD.codigoNuncaValido+") as codigo_servicio, "+
	"coalesce(da.articulo,"+ConstantesBD.codigoNuncaValido+") as codigo_articulo, "+
	"coalesce(getdescripcionarticulo(da.articulo),'') as nombre_articulo, "+
	"coalesce(da.cantidad,0) as cantidad, "+
	"coalesce(da.justificacion_solicitud,'') as justificacion_solicitud, "+
	"to_char(da.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') as fecha_modifica, "+
	"da.hora_modifica as hora_modifica, "+
	"da.usuario_modifica as usuario_modifica," +
	"coalesce(da.servicio_cx,"+ConstantesBD.codigoNuncaValido+") as codigo_servicio_cx," +		
	"coalesce(da.tipo_asocio,"+ConstantesBD.codigoNuncaValido+") as tipo_asocio," +
	"CASE WHEN da.tipo_asocio IS NULL THEN '' ELSE getnomtipoasocio(da.tipo_asocio) END as nombre_tipo_asocio, " +
	"coalesce(da.estado,'') AS estado," +
	"CASE WHEN da.servicio IS NOT NULL THEN gettiposervicio(da.servicio) ELSE '' END AS tipo_servicio "+		
	"FROM det_autorizaciones da "+ 
	"WHERE 1=1 ";
	
	private static String strConsultarEnvioAutorizaciones="SELECT "+ 
		"ea.codigo_pk as codigo_pk, "+
		"ea.det_autorizacion as det_autorizacion, "+
		"coalesce(ea.entidad_envio,"+ConstantesBD.codigoNuncaValido+") as cod_entidad_envio, "+
		"coalesce(e.razon_social,'') as nom_entidad_envio, "+
		"coalesce(ea.convenio_envio,"+ConstantesBD.codigoNuncaValido+") as cod_convenio_envio, "+
		"coalesce(getnombreconvenio(ea.convenio_envio),'') as nom_convenio_envio, "+
		"ea.medio_envio as medio_envio, " +
		"coalesce(ea.path_archivo,'') as patharchivo, "+
		"to_char(ea.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') as fecha_modifica, "+
		"ea.hora_modifica as hora_modifica, "+
		"ea.usuario_modifica as usuario_modifica "+ 
		"FROM envio_autorizaciones ea "+ 
		"LEFT OUTER JOIN empresas e ON(e.codigo = ea.entidad_envio) "+ 
		"WHERE "+ 
		"ea.det_autorizacion = ? "+ 
		"ORDER BY ea.codigo_pk desc";
	
	private static String strConsultarDiagnosticoAutorizaciones="SELECT "+ 
		"da.codigo as codigo, "+
		"da.autorizacion as autorizacion, "+ 
		"da.acronimo as acronimo, "+
		"da.tipo_cie as tipo_cie, "+
		"getnombrediagnostico(da.acronimo,da.tipo_cie) as nombre, "+
		"da.principal as principal, "+
		"to_char(da.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') as fecha_modifica, "+
		"da.hora_modifica as hora_modifica, "+
		"da.usuario_modifica as usuario_modifica "+ 
		"FROM diag_autorizaciones da "+ 
		"WHERE da.autorizacion = ?";
	
	private static String strConsultarAdjuntoAutorizaciones="SELECT "+ 
		"aa.codigo_pk as codigo_pk, "+
		"aa.autorizacion as autorizacion, "+
		"aa.nombre_archivo as nombre_archivo, "+
		"aa.nombre_original as nombre_original, "+
		"to_char(aa.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') as fecha_modifica, "+
		"aa.hora_modifica as hora_modifica, "+
		"aa.usuario_modifica as usuario_modifica "+ 
		"FROM adj_autorizaciones aa "+  
		"WHERE aa.autorizacion = ? ";
	
	private static String strListarPerSolicita="SELECT DISTINCT s.codigo_medico AS codigo_medico, " +
			"getnombrepersona(s.codigo_medico) AS nombre_persona " + 
			"FROM solicitudes s";
	
	/**
	 * Cadena que consulta la causa externa x tipo evento
	 */
	public static final String obtenerCausaExternaXEventoStr = "SELECT codigo,nombre FROM causas_externas where tipo_evento =  ?";
	
	/**
	 * Cadena que consulta la causa externa de solicitud autorizacion x cuenta
	 */
	public static final String obtenerCausaExternaXCuentaStr = "SELECT "+ 
		"ce.codigo, "+
		"ce.nombre "+ 
		"FROM solicitudes s "+ 
		"INNER JOIN valoraciones v ON(v.numero_solicitud = s.numero_solicitud) "+ 
		"INNER JOIN causas_externas ce ON(ce.codigo = v.causa_externa and ce.sol_autorizacion = '"+ConstantesBD.acronimoSi+"') "+ 
		"WHERE "+ 
		"s.cuenta = ? and s.tipo = ? ORDER BY s.numero_solicitud";
	
	/**
	 * Cadena que obtiene la persona que hace la solicita 
	 */
	public static final String obtenerPersonaSolicitaStr = "SELECT "+ 
		"p.codigo AS codigo "+
		"FROM personas p "; 
	
	/**
	 * Cadena que obtiene la persona que hace la solicita de tipo ambulatorio
	 */
	public static final String obtenerPersonaSolicitaAmbulatorioStr="SELECT  " +
		"coalesce(usu.codigo_persona,"+ConstantesBD.codigoNuncaValido+") AS codigo," +
		"coalesce(oas.numero_solicitud,"+ConstantesBD.codigoNuncaValido+") AS numero_solicitud," +
		"CASE WHEN oas.numero_solicitud IS NOT NULL THEN gettiposolicitud(oas.numero_solicitud) ELSE '' END AS tipo_solicitud " +
		"FROM ordenes_ambulatorias ord "+
		"LEFT OUTER JOIN ordenes_amb_solicitudes oas ON (oas.orden = ord.codigo) " +
		"LEFT OUTER JOIN usuarios usu ON (usu.login = ord.usuario_solicita) " +
		"WHERE ord.codigo = ? " ;
	
	/**
	 * consulta el tipo de formato parametrizado para el convenio
	 * */
	public static final String obtenerFormatoSolicitud = "SELECT " +
			"coalesce(c.formato_autorizacion,'') AS formato_autorizacion," +
			"c.nombre " +
			"FROM " +
			"convenios c " +
			"INNER JOIN sub_cuentas s ON (s.convenio = c.codigo) " +
			"WHERE s.sub_cuenta = ? ";
		
	/**
	 * Busqueda de Estancias y Validacion
	 */
	private static final String strBusquedaEstyVal = "SELECT " +
			"coalesce(getconsecutivosolicitud(bus_est.solicitud)||'','') AS consecutivo_sol," +
			"bus_est.solicitud, " +
			"bus_est.det_cargo, " +
			"coalesce(da.codigo||'','') AS codigo_det_auto, " +
			"CASE WHEN da.codigo IS NOT NULL THEN 'S' ELSE 'N' END AS estado_auto_preg, " +
			"coalesce(est.codigo||'','') AS codigo_det_auto_est, " +
			"CASE WHEN est.codigo IS NOT NULL THEN 'S' ELSE 'N' END AS estado_auto_est_preg " +
			"FROM " +
			"(SELECT s.numero_solicitud AS solicitud, dc.codigo_detalle_cargo AS det_cargo, s.fecha_solicitud, ca.servicio_solicitado AS servicio " +
			"FROM solicitudes s " +
			"INNER JOIN cargos_directos ca ON (s.numero_solicitud = ca.numero_solicitud AND ca.servicio_solicitado = ? ) " +
			"INNER JOIN det_cargos dc ON (dc.solicitud = s.numero_solicitud AND dc.sub_cuenta = ? ) " +
			"WHERE (s.fecha_solicitud BETWEEN ? AND ?) ORDER BY s.fecha_solicitud ASC ) bus_est " +
			"LEFT JOIN det_autorizaciones da ON (da.numero_solicitud = bus_est.solicitud AND da.servicio = bus_est.servicio " +
			"AND da.activo = '"+ConstantesBD.acronimoSi+"' AND (da.estado = '"+ConstantesIntegridadDominio.acronimoEstadoNegado+"' OR da.estado = '"+ConstantesIntegridadDominio.acronimoAutorizado+"'))" +
			"LEFT JOIN det_autorizaciones_estancia est ON (est.numero_solicitud = bus_est.solicitud AND est.activo = '"+ConstantesBD.acronimoSi+"')";
	
	private static final String strBusquedaEstyVal2 = "SELECT " +
			"coalesce(getconsecutivosolicitud(bus_est.solicitud)||'','') AS consecutivo_sol," +
			"bus_est.solicitud, " +
			"bus_est.det_cargo, " +
			"coalesce(da.codigo||'','') AS codigo_det_auto, " +
			"CASE WHEN da.codigo IS NOT NULL THEN 'S' ELSE 'N' END AS estado_auto_preg, " +
			"coalesce(est.codigo||'','') AS codigo_det_auto_est, " +
			"CASE WHEN est.codigo IS NOT NULL THEN 'S' ELSE 'N' END AS estado_auto_est_preg " +
			"FROM (SELECT s.numero_solicitud AS solicitud, dc.codigo_detalle_cargo AS det_cargo, " +
			"s.fecha_solicitud AS fecha_solicitud, ca.servicio_solicitado AS servicio, " +
			"dc.sub_cuenta AS subcuenta FROM solicitudes s " +
			"INNER JOIN cargos_directos ca ON (s.numero_solicitud = ca.numero_solicitud ) " +
			"INNER JOIN det_cargos dc ON (dc.solicitud = s.numero_solicitud AND dc.sub_cuenta = ? )) bus_est " +
			"LEFT JOIN det_autorizaciones da ON (da.activo = '"+ConstantesBD.acronimoSi+"' AND da.estado = '"+ConstantesIntegridadDominio.acronimoAutorizado+"' " +
			"AND da.autorizacion IN (SELECT auto.codigo FROM autorizaciones auto WHERE auto.sub_cuenta = bus_est.subcuenta " +
			"AND (auto.tipo='"+ConstantesIntegridadDominio.acronimoAdmision+"' OR auto.tipo='"+ConstantesIntegridadDominio.acronimoEstancia+"')) " +
			"AND da.codigo IN(SELECT res.det_autorizacion FROM  resp_autorizaciones res " +
			"WHERE (bus_est.fecha_solicitud BETWEEN res.fecha_ini_autorizada AND res.fecha_fin_autorizada))) " +
			"LEFT JOIN det_autorizaciones_estancia est ON (est.numero_solicitud = bus_est.solicitud) ";
		
	private static final String strObtenerCantidadAutorizadaDetAuto = "SELECT "+
			"d.cantidad_autorizacion AS cantidad " +
			"FROM det_autorizaciones d " +
			"WHERE d.codigo = ? ";
	
	/**
	 * Cadena para informe tecnico
	 */
	private static final String strConsultaInformeTecnico = "SELECT " + 
			"auto.codigo AS cod_auto, " +
			"auto.consecutivo AS consecutivo, " +
			"auto.anio_consecutivo AS anioconsecutivo, " +
			"to_char(auto.fecha_solicitud,'dd/mm/yyyy') AS fecha_sol, " +
			"auto.hora_solicitud AS hora_sol, " +
			"con.nombre AS nombre_convenio, " +
			"con.codigo_min_salud AS cod_min_salud_convenio, " +
			"cs.nombre AS nombre_cobertura, " +
			"ce.nombre AS nombre_atencion, " +
			"auto.tipo_servicio_solicitado AS tipo_ser_sol, " +
			"tss.nombre AS nombre_tipo_ser_sol, " + 
			"CASE WHEN auto.prioridad_atencion IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE auto.prioridad_atencion END AS prioridad, " + 
			"CASE WHEN c.via_ingreso IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE c.via_ingreso END AS via_ingreso, " +
			"CASE WHEN c.tipo_paciente IS NULL THEN '' ELSE c.tipo_paciente END AS tip_paciente, " +
			"CASE WHEN c.via_ingreso IS NULL THEN '' ELSE getnombreviaingreso(c.via_ingreso) END AS nombre_via_ingreso, " +
			"CASE WHEN c.area IS NULL THEN '' ELSE getnombrecentroscosto(c.area) END AS servicio, " +
			"CASE WHEN c.via_ingreso=1 AND c.tipo_paciente='H' THEN manejopaciente.getCama(auto.cama) ELSE '' END AS cama, " +
			"getnombrepersona(auto.persona_solicita) AS nombre_usua_sol, " +
			"coalesce(p.telefono,'') AS telfono_usua_sol, " +
			"coalesce(p.telefono_celular||'','') AS telfono_celular_usua_sol, " +
			"CASE WHEN usa.cargo IS NULL THEN '' ELSE getnomcargousuario(usa.cargo) END AS nombre_cargo " +
			"FROM autorizaciones auto " +
			"INNER JOIN convenios con ON (auto.convenio = con.codigo) " +
			"LEFT JOIN coberturas_salud cs ON (cs.codigo = auto.tipo_cobertura) " +
			"LEFT JOIN causas_externas ce ON (ce.codigo = auto.origen_atencion AND ce.sol_autorizacion = '"+ConstantesBD.acronimoSi+"') " +
			"LEFT JOIN tipos_ser_sol tss ON (tss.codigo = auto.tipo_servicio_solicitado) " +
			"LEFT JOIN usuarios usa ON (usa.codigo_persona = auto.persona_solicita) " + 
			"LEFT JOIN personas p ON (p.codigo = auto.persona_solicita) " +
			"LEFT JOIN cuentas c ON (c.id = auto.cuenta ) " +
			"WHERE auto.codigo = ? ";
	
	/**
	 * Cadena para actualizar la cuenta en las autorizaciones
	 */
	private static final String asociarCuentaAAutorizacionesSinCuentaStr = "UPDATE autorizaciones SET cuenta = ? WHERE ingreso = ? and cuenta is null and tipo = '"+ConstantesIntegridadDominio.acronimoAdmision+"'";

	/**
	 * Consulta las cuentas a partir del ingreso
	 * */
	private static final String strConsultarCuentasPacXIngresos = 
		"SELECT " +
		"cu.id AS codigo_cuenta," +
		"cu.tipo_paciente," +
		"cu.id_ingreso," +
		"cu.via_ingreso," +
		"cu.estado_cuenta," +
		"coalesce(ac.cuenta_inicial,"+ConstantesBD.codigoNuncaValido+") AS codigo_inicial_asocio, " +
		"coalesce(ac.cuenta_final,"+ConstantesBD.codigoNuncaValido+") AS codigo_final_asocio, " +
		"CASE WHEN ac.cuenta_inicial IS NOT NULL THEN getviaingresocuenta(ac.cuenta_inicial) ELSE "+ConstantesBD.codigoNuncaValido+" END AS via_ing_cini, "+
		ConstantesBD.codigoNuncaValido+" AS codigo_auto," +
		"'' AS tipo_auto," +
		"'' AS estado_auto," +
		ConstantesBD.codigoNuncaValido+" AS codigo_det_auto," +
		"coalesce(getconveniosubcuenta(?),"+ConstantesBD.codigoNuncaValido+") AS convenio,"+
		"coalesce(getnombreconvenio(getconveniosubcuenta(?)),'') AS nombre_convenio," +
		ConstantesBD.codigoNuncaValido+" AS sub_cuenta " +				
		"FROM "+
		"cuentas cu "+	
		"LEFT OUTER JOIN asocios_cuenta ac ON (ac.ingreso = cu.id_ingreso AND (cu.id = ac.cuenta_inicial OR cu.id = ac.cuenta_final) AND ac.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ) " +
		"WHERE cu.estado_cuenta != "+ConstantesBD.codigoEstadoCuentaCerrada+" AND cu.id_ingreso = ? "+ConstantesBD.separadorSplit+"1 AND " +
		"(SELECT COUNT(a.codigo) FROM autorizaciones a WHERE a.sub_cuenta = ? AND a.ingreso = cu.id_ingreso AND a.cuenta = cu.id AND (a.tipo = '"+ConstantesIntegridadDominio.acronimoAdmision+"' OR a.tipo = '"+ConstantesIntegridadDominio.acronimoEstancia+"')) = 0 " +
		"" +
		"UNION " +
		"" +
		"SELECT " +
		"cu.id AS codigo_cuenta," +
		"cu.tipo_paciente," +
		"cu.id_ingreso," +
		"cu.via_ingreso," +
		"cu.estado_cuenta," +
		"coalesce(ac.cuenta_inicial,"+ConstantesBD.codigoNuncaValido+") AS codigo_inicial_asocio, " +
		"coalesce(ac.cuenta_final,"+ConstantesBD.codigoNuncaValido+") AS codigo_final_asocio, " +
		"CASE WHEN ac.cuenta_inicial IS NOT NULL THEN getviaingresocuenta(ac.cuenta_inicial) ELSE "+ConstantesBD.codigoNuncaValido+" END AS via_ing_cini, "+
		"coalesce(a.codigo,"+ConstantesBD.codigoNuncaValido+") AS codigo_auto," +
		"coalesce(a.tipo,'') AS tipo_auto," +
		"coalesce(d.estado,'') AS estado_auto," +
		"coalesce(d.codigo,"+ConstantesBD.codigoNuncaValido+") AS codigo_det_auto," +
		"CASE WHEN a.codigo IS NULL THEN coalesce(getconveniosubcuenta(?),"+ConstantesBD.codigoNuncaValido+") ELSE a.convenio END AS convenio,"+
		"CASE WHEN a.codigo IS NULL THEN coalesce(getnombreconvenio(getconveniosubcuenta(?)),'') ELSE getnombreconvenio(a.convenio) END AS nombre_convenio," +
		"a.sub_cuenta "+		
		"FROM "+
		"cuentas cu "+
		"INNER JOIN autorizaciones a ON (a.sub_cuenta = ? AND a.ingreso = cu.id_ingreso AND a.cuenta = cu.id AND (a.tipo = '"+ConstantesIntegridadDominio.acronimoAdmision+"' OR a.tipo = '"+ConstantesIntegridadDominio.acronimoEstancia+"' )) "+
		"INNER JOIN det_autorizaciones d ON (d.autorizacion = a.codigo AND d.activo = '"+ConstantesBD.acronimoSi+"' ) " +
		"LEFT OUTER JOIN asocios_cuenta ac ON (ac.ingreso = cu.id_ingreso AND (cu.id = ac.cuenta_inicial OR cu.id = ac.cuenta_final) AND ac.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ) " +
		"WHERE cu.estado_cuenta != "+ConstantesBD.codigoEstadoCuentaCerrada+" AND cu.id_ingreso = ? "+ConstantesBD.separadorSplit+"2 "+
		"";
	
	
	/**
	 * Consulta las cuentas a partir del ingreso y la cuenta
	 * */
	private static final String strConsultarCuentasPacXIngresosXCuenta = 
		"SELECT " +
		"cu.id AS codigo_cuenta," +
		"cu.tipo_paciente," +
		"cu.id_ingreso," +
		"cu.via_ingreso," +
		"cu.estado_cuenta," +
		"coalesce(ac.cuenta_inicial,"+ConstantesBD.codigoNuncaValido+") AS codigo_inicial_asocio, " +
		"coalesce(ac.cuenta_final,"+ConstantesBD.codigoNuncaValido+") AS codigo_final_asocio, " +
		"CASE WHEN ac.cuenta_inicial IS NOT NULL THEN getviaingresocuenta(ac.cuenta_inicial) ELSE "+ConstantesBD.codigoNuncaValido+" END AS via_ing_cini, "+
		ConstantesBD.codigoNuncaValido+" AS codigo_auto," +
		"'' AS tipo_auto," +
		"'' AS estado_auto," +
		ConstantesBD.codigoNuncaValido+" AS codigo_det_auto," +
		"coalesce(getconveniosubcuenta(?),"+ConstantesBD.codigoNuncaValido+") AS convenio,"+
		"coalesce(getnombreconvenio(getconveniosubcuenta(?)),'') AS nombre_convenio," +
		ConstantesBD.codigoNuncaValido+" AS sub_cuenta " +				
		"FROM "+
		"cuentas cu "+	
		"LEFT OUTER JOIN asocios_cuenta ac ON (ac.ingreso = cu.id_ingreso AND (cu.id = ac.cuenta_inicial OR cu.id = ac.cuenta_final) AND ac.activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ) " +
		"WHERE cu.estado_cuenta != "+ConstantesBD.codigoEstadoCuentaCerrada+" AND cu.id_ingreso = ? AND cu.id = ? ";
	
	/**
	 * 
	 * */
	private static final String strFechaConsultaExtAmb = "SELECT to_char(fecha_apertura,'dd/mm/yyyy') as fecha_admision,hora_apertura as hora_admision FROM cuentas WHERE id = ? ";
	
	/**
	 * 
	 * */
	private static final String strFechaUrgencias = "SELECT to_char(fecha_admision,'dd/mm/yyyy') as fecha_admision,hora_admision FROM admisiones_urgencias WHERE cuenta = ? ";
	
	/**
	 * 
	 * */
	private static final String strFechaHospitalizacion = "SELECT to_char(fecha_admision,'dd/mm/yyyy') as fecha_admision,hora_admision FROM admisiones_hospi WHERE cuenta = ? ";
	
	/**
	 * 
	 * */
	private static final String strFechaAsocio = "SELECT to_char(fecha,'dd/mm/yyyy') as fecha_admision,hora as hora_admision FROM asocios_cuenta WHERE cuenta_inicial = ?  ";
	
	/**
	 * 
	 * */
	private static final String strReplicarAutorizacion = "" +
			"INSERT INTO autorizaciones " +
			"(codigo,ingreso,sub_cuenta,tipo_servicio_solicitado,observaciones,tipo_cobertura,origen_atencion,fecha_solicitud,hora_solicitud,usuario_solicitud,tipo_tramite,convenio,prioridad_atencion,cuenta,cama,fecha_modifica,hora_modifica,usuario_modifica,persona_solicita,consecutivo,anio_consecutivo,tipo) " +
			"" +
			"(" +
			"SELECT " +
			"?," +
			"ingreso," +
			"sub_cuenta," +
			"tipo_servicio_solicitado," +
			"observaciones," +
			"tipo_cobertura," +
			"origen_atencion," +
			"fecha_solicitud," +
			"hora_solicitud," +
			"usuario_solicitud," +
			"tipo_tramite," +
			"convenio," +
			"prioridad_atencion," +
			"cuenta," +
			"cama," +
			"?," +
			"?," +
			"?," +
			"persona_solicita," +
			"consecutivo," +
			"anio_consecutivo," +
			"tipo " +
			"FROM autorizaciones " +
			"WHERE codigo = ?) ";
	
	
	/**
	 * 
	 * */
	private static final String strReplicarDetAutorizacion = 
		"INSERT INTO det_autorizaciones(codigo,autorizacion,numero_solicitud,orden_ambulatoria,det_cargo,servicio,articulo,cantidad,estado,activo,justificacion_solicitud,fecha_modifica,hora_modifica,usuario_modifica,tipo_asocio,servicio_cx,cantidad_autorizacion) " +
		"(SELECT " +
		"?," +
		"?," +
		"numero_solicitud,orden_ambulatoria,det_cargo,servicio,articulo,cantidad," +
		"?," +
		"?," +
		"justificacion_solicitud," +
		"?," +
		"?," +
		"?," +
		"tipo_asocio,servicio_cx,cantidad_autorizacion " +
		"FROM det_autorizaciones " +
		"WHERE autorizacion = ? AND activo = '"+ConstantesBD.acronimoSi+"') ";
	
	/**
	 * 
	 * */
	private static final String strReplicarEnvios = 
		"INSERT INTO envio_autorizaciones (codigo_pk,det_autorizacion,entidad_envio,convenio_envio,medio_envio,fecha_modifica,hora_modifica,usuario_modifica)  " +
		"(SELECT " +
		"?," +
		"?," +
		"e.entidad_envio,e.convenio_envio,e.medio_envio," +
		"e.fecha_modifica,e.hora_modifica,e.usuario_modifica " +
		"FROM envio_autorizaciones e " +
		"INNER JOIN det_autorizaciones d ON (d.autorizacion = ?) " +
		"WHERE e.det_autorizacion = d.codigo) ";
	
	/**
	 * 
	 * */
	private static final String strReplicarAdjAutorizaciones  = 
		"INSERT INTO adj_autorizaciones (codigo_pk,autorizacion,nombre_archivo,nombre_original,fecha_modifica,hora_modifica,usuario_modifica) " +
		"(SELECT " +
		"?," +
		"?," +
		"nombre_archivo,nombre_original,fecha_modifica,hora_modifica,usuario_modifica " +
		"FROM adj_autorizaciones WHERE autorizacion = ?) ";
	
	//******************************************************************************************************
	//****************************************************************************************
	//*************************************************************
	
	/**
	 * Replica una autorizacion
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap replicarAutorizacion(Connection con,HashMap parametros)
	{
		HashMap respuesta = new HashMap();
		respuesta.put("exito",true);
		
		try
		{
			int codigoAuto = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_autorizaciones");
			String fechaModifica =  UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual());
			String horaModifica = UtilidadFecha.getHoraActual();
			UsuarioBasico usuarioModifica = (UsuarioBasico)parametros.get("usuarioModifica");
			
			//replica la autorizacion
			PreparedStatementDecorator pre =  new PreparedStatementDecorator(con.prepareStatement(strReplicarAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			pre.setInt(1,codigoAuto);
			pre.setDate(2,Date.valueOf(fechaModifica));
			pre.setString(3,horaModifica);
			pre.setString(4,usuarioModifica.getLoginUsuario());
			pre.setInt(5,Utilidades.convertirAEntero(parametros.get("codigoPkAutoOld").toString()));
			
			//replicar el detalle de la autorización
			if(pre.executeUpdate()>0)
			{
				int codigoDetAuto = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_det_autorizaciones");
				pre =  new PreparedStatementDecorator(con.prepareStatement(strReplicarDetAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pre.setInt(1,codigoDetAuto);
				pre.setInt(2,codigoAuto);
				pre.setString(3,parametros.get("estado").toString());
				pre.setString(4,ConstantesBD.acronimoSi);				
				pre.setDate(5,Date.valueOf(fechaModifica));
				pre.setString(6,horaModifica);
				pre.setString(7,usuarioModifica.getLoginUsuario());				
				pre.setInt(8,Utilidades.convertirAEntero(parametros.get("codigoPkAutoOld").toString()));
				
				if(pre.executeUpdate() > 0)
				{
					HashMap tmp = new HashMap();
					
					//**************SE CARGAN LOS ENVÍOS DE AUTORIZACION**********************************
					tmp.put("autorizacion", parametros.get("codigoPkDetAutoOld").toString());
					ArrayList<DtoEnvioAutorizacion> envios = cargarEnvioAutorizacion(con,tmp);
					//************************************************************************************
					for(DtoEnvioAutorizacion dto : envios)
					{						
						dto.setFechaModifica(fechaModifica);
						dto.setHoraModifica(horaModifica);
						dto.setUsuarioModifica(usuarioModifica);
						dto.setDetAutorizacion(codigoDetAuto+"");
						insertarEnvioAutorizacion(con,dto);
					}
					
					//*************SE CARGAN LOS ADJUNTOS DE LA AUTORIZACION********************************+
					tmp.put("autorizacion", parametros.get("codigoPkAutoOld").toString());
					ArrayList<DtoAdjAutorizacion> adjuntos =caragarAdjAutorizacion(con,tmp);
					//****************************************************************************************
					for(DtoAdjAutorizacion dto: adjuntos)
					{
						dto.setFechaModifica(fechaModifica);
						dto.setHoraModifica(horaModifica);
						dto.setUsuarioModifica(usuarioModifica);
						dto.setAutorizacion(codigoAuto+"");
						insertarAdjuntoAutorizacion(con, dto);
					}
					
					//**************SE CARGAN LOS DIAGNÓSTICOAS DE LA AUTORIZACION***************************
					tmp.put("autorizacion", parametros.get("codigoPkAutoOld").toString());
					ArrayList<DtoDiagAutorizacion> diag = cargarDiagAutorizacion(con,tmp);
					//***************************************************************************************+
					for(DtoDiagAutorizacion dto : diag)
					{
						dto.setFechaModifica(fechaModifica);
						dto.setHoraModifica(horaModifica);
						dto.setUsuarioModifica(usuarioModifica);
						dto.setAutorizacion(codigoAuto+"");
						insertarDiagnosticoAutorizacion(con,dto);
					}
					
					respuesta.put("exito",true);
					parametros.put("codigoPkAuto",codigoAuto);
					parametros.put("codigoPkDetAuto",codigoDetAuto);
				}
			}
			else
				respuesta.put("exito",false);
			
		}
		catch (Exception e) {
			respuesta.put("exito",false);
			logger.info("error en replicarAutorizacion "+e);
		}
		
		return respuesta;
	}
	
	
	/**
	 * Actualizar una Autorizacion
	 * @param Connection con
	 * @param DtoAutorizacion dtoAutorizacion
	 */
	public static int actualizarAutorizacion(Connection con, DtoAutorizacion dtoAutorizacion) 
	{		
		try{
			PreparedStatementDecorator insertarAutorizacion =  new PreparedStatementDecorator(con.prepareStatement(strActualizarAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
			insertarAutorizacion.setInt(1,Utilidades.convertirAEntero(dtoAutorizacion.getIdIngreso()));
			insertarAutorizacion.setInt(2,Utilidades.convertirAEntero(dtoAutorizacion.getIdSubCuenta()));
			
			if(dtoAutorizacion.getTipoServicioSolicitado().getCodigo()>0)
				insertarAutorizacion.setInt(3,dtoAutorizacion.getTipoServicioSolicitado().getCodigo());
			else
				insertarAutorizacion.setNull(3,Types.INTEGER);

			if(!dtoAutorizacion.getNuevaObservacion().equals(""))
			{
				String obser = UtilidadTexto.agregarTextoAObservacion(
						UtilidadTexto.deshacerCodificacionHTML(dtoAutorizacion.getObservaciones()),
						UtilidadTexto.deshacerCodificacionHTML(dtoAutorizacion.getNuevaObservacion()),
						dtoAutorizacion.getUsuarioModifica(),
						true);
				
				insertarAutorizacion.setString(4,obser.length()>4000?obser.substring(0,3998):obser);
			}
			else
				insertarAutorizacion.setNull(4,Types.VARCHAR);
			
			if(dtoAutorizacion.getTipoCobertura().getCodigo()>0)
				insertarAutorizacion.setInt(5,dtoAutorizacion.getTipoCobertura().getCodigo());
			else
				insertarAutorizacion.setNull(5,Types.INTEGER);
			
			if(dtoAutorizacion.getOrigenAtencion().getCodigo()>0)
				insertarAutorizacion.setInt(6,dtoAutorizacion.getOrigenAtencion().getCodigo());
			else
				insertarAutorizacion.setNull(6,Types.INTEGER);
			
			insertarAutorizacion.setDate(7,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			insertarAutorizacion.setString(8,UtilidadFecha.getHoraActual());
			
			if(!dtoAutorizacion.getUsuarioSolicitud().getLoginUsuario().equals(""))
				insertarAutorizacion.setString(9,dtoAutorizacion.getUsuarioSolicitud().getLoginUsuario());
			else
				insertarAutorizacion.setNull(9,Types.VARCHAR);
			
			if(!dtoAutorizacion.getTipoTramite().equals(""))
				insertarAutorizacion.setString(10,dtoAutorizacion.getTipoTramite());
			else
				insertarAutorizacion.setNull(10,Types.VARCHAR);
					
			insertarAutorizacion.setInt(11,dtoAutorizacion.getCodigoConvenio());
			
			if(!dtoAutorizacion.getPrioridadAtencion().equals(""))
				insertarAutorizacion.setString(12,dtoAutorizacion.getPrioridadAtencion());
			else
				insertarAutorizacion.setNull(12,Types.VARCHAR);
			
			insertarAutorizacion.setInt(13,Utilidades.convertirAEntero(dtoAutorizacion.getIdCuenta()));
			
			if(dtoAutorizacion.getCama().getCodigo()>0)
				insertarAutorizacion.setInt(14,dtoAutorizacion.getCama().getCodigo());
			else
				insertarAutorizacion.setNull(14,Types.INTEGER);
			
			insertarAutorizacion.setDate(15,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			insertarAutorizacion.setString(16,UtilidadFecha.getHoraActual());
			
			insertarAutorizacion.setString(17,dtoAutorizacion.getUsuarioModifica().getLoginUsuario());
			
			if(dtoAutorizacion.getPersonaSolicita().getCodigo()>0)
				insertarAutorizacion.setInt(18,dtoAutorizacion.getPersonaSolicita().getCodigo());
			else
				insertarAutorizacion.setNull(18,Types.INTEGER);
			
			insertarAutorizacion.setInt(19,Utilidades.convertirAEntero(dtoAutorizacion.getCodigoPK()));
				
			if(insertarAutorizacion.executeUpdate()>0)
			{				
				insertarAutorizacion.close();
				return 1;
			}
			else
			{				
				insertarAutorizacion.close();
			}
		}catch(SQLException e){
			logger.info("Error en el actualizarAutorizacion de Solicitud de Autorizacion !!!!!!!!!");
			logger.error(e);
			return 0;
		}
		return 0;	
	}
	
	/**
	 * Insertar una Autorizacion
	 * @param Connection con
	 * @param DtoAutorizacion dtoAutorizacion
	 */
	public static int insertarAutorizacion(Connection con, DtoAutorizacion dtoAutorizacion) 
	{
		try{
			PreparedStatementDecorator insertarAutorizacion =  new PreparedStatementDecorator(con.prepareStatement(strInsertarAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_autorizaciones");
			String valorConsecutivoInfo=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoSolicitudAutori,dtoAutorizacion.getUsuarioModifica().getCodigoInstitucionInt());
			String anioConsecutivoInfo=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoSolicitudAutori,dtoAutorizacion.getUsuarioModifica().getCodigoInstitucionInt(),valorConsecutivoInfo);
			
			insertarAutorizacion.setInt(1,codigo);
			insertarAutorizacion.setString(2,valorConsecutivoInfo);
			insertarAutorizacion.setString(3,anioConsecutivoInfo);
			
			insertarAutorizacion.setInt(4,Utilidades.convertirAEntero(dtoAutorizacion.getIdIngreso()));
			insertarAutorizacion.setInt(5,Utilidades.convertirAEntero(dtoAutorizacion.getIdSubCuenta()));
			
			if(dtoAutorizacion.getTipoServicioSolicitado().getCodigo()>0)
				insertarAutorizacion.setInt(6,dtoAutorizacion.getTipoServicioSolicitado().getCodigo());
			else
				insertarAutorizacion.setNull(6,Types.INTEGER);

			if(!dtoAutorizacion.getNuevaObservacion().equals(""))
			{
				String obser = UtilidadTexto.agregarTextoAObservacion(
						UtilidadTexto.deshacerCodificacionHTML(dtoAutorizacion.getObservaciones()),
						UtilidadTexto.deshacerCodificacionHTML(dtoAutorizacion.getNuevaObservacion()),
						dtoAutorizacion.getUsuarioModifica(),
						true);
				
				insertarAutorizacion.setString(7,obser.length()>4000?obser.substring(0,3998):obser);
			}
			else
				insertarAutorizacion.setNull(7,Types.VARCHAR);
			
			if(dtoAutorizacion.getTipoCobertura().getCodigo()>0)
				insertarAutorizacion.setInt(8,dtoAutorizacion.getTipoCobertura().getCodigo());
			else
				insertarAutorizacion.setNull(8,Types.INTEGER);
			
			if(dtoAutorizacion.getOrigenAtencion().getCodigo()>0)
				insertarAutorizacion.setInt(9,dtoAutorizacion.getOrigenAtencion().getCodigo());
			else
				insertarAutorizacion.setNull(9,Types.INTEGER);
			
			if(!dtoAutorizacion.getUsuarioSolicitud().getLoginUsuario().equals(""))
				insertarAutorizacion.setString(10,dtoAutorizacion.getUsuarioSolicitud().getLoginUsuario());
			else
				insertarAutorizacion.setNull(10,Types.VARCHAR);
			
			logger.info("\n\n..:tipo de tramite >> "+dtoAutorizacion.getTipoTramite());
			if(!dtoAutorizacion.getTipoTramite().equals(""))
				insertarAutorizacion.setString(11,dtoAutorizacion.getTipoTramite());
			else
				insertarAutorizacion.setNull(11,Types.VARCHAR);
					
			insertarAutorizacion.setInt(12,dtoAutorizacion.getCodigoConvenio());
			
			if(!dtoAutorizacion.getPrioridadAtencion().equals(""))
				insertarAutorizacion.setString(13,dtoAutorizacion.getPrioridadAtencion());
			else
				insertarAutorizacion.setNull(13,Types.VARCHAR);
			
			insertarAutorizacion.setInt(14,Utilidades.convertirAEntero(dtoAutorizacion.getIdCuenta()));
			
			logger.info("valor de la cama en el sql >> "+dtoAutorizacion.getCama().getCodigo());
			if(dtoAutorizacion.getCama().getCodigo()>0)
				insertarAutorizacion.setInt(15,dtoAutorizacion.getCama().getCodigo());
			else
				insertarAutorizacion.setNull(15,Types.INTEGER);
			
			insertarAutorizacion.setString(16,dtoAutorizacion.getUsuarioModifica().getLoginUsuario());
			
			if(dtoAutorizacion.getPersonaSolicita().getCodigo()>0)
				insertarAutorizacion.setInt(17,dtoAutorizacion.getPersonaSolicita().getCodigo());
			else
				insertarAutorizacion.setNull(17,Types.INTEGER);
				
			logger.info("\n\n..:tipo de solicitud >> "+dtoAutorizacion.getTipo());
			insertarAutorizacion.setString(18,dtoAutorizacion.getTipo());
			
			if(insertarAutorizacion.executeUpdate()>0)
			{			
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(
						con,
						ConstantesBD.nombreConsecutivoSolicitudAutori,
						dtoAutorizacion.getUsuarioModifica().getCodigoInstitucionInt(),
						valorConsecutivoInfo,
						ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
				
				insertarAutorizacion.close();
				return codigo;
			}
			else
			{
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(
						con,
						ConstantesBD.nombreConsecutivoSolicitudAutori,
						dtoAutorizacion.getUsuarioModifica().getCodigoInstitucionInt(),
						valorConsecutivoInfo,
						ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				
				insertarAutorizacion.close();
			}
		}catch(SQLException e){
			logger.info("Error en el ingreso de Solicitud de Autorizacion !!!!!!!!! "+strInsertarAutorizacion);
			logger.error(e);
			return 0;
		}
		return 0;	
	}
	
	/**
	 * Insertar Detalle Autorizacion
	 */
	public static int insertarDetalleAutorizacion(Connection con, DtoDetAutorizacion dtoDetAutorizacion) 
	{
		try
		{
			PreparedStatementDecorator infoBasica = null;
			
			//Carga la información basica			
			if(dtoDetAutorizacion.getTipoOrden().equals(Autorizaciones.codInternoAutoSolicSerMed))			
				infoBasica =  new PreparedStatementDecorator(con.prepareStatement(strCargarInfoBasicaDetAutoXCargo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			else if(dtoDetAutorizacion.getTipoOrden().equals(Autorizaciones.codInternoAutoSolicAmbula))
				infoBasica =  new PreparedStatementDecorator(con.prepareStatement(strCargarInfoBasicaDetAutoXAmbula,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			else 
			{
				logger.info("No se envio informacion del tipo de orden indicador");
				return ConstantesBD.codigoNuncaValido;
			}
			
			infoBasica.setInt(1,Utilidades.convertirAEntero(dtoDetAutorizacion.getCodigoEvaluar()));			
			ResultSetDecorator rs = new ResultSetDecorator(infoBasica.executeQuery());
			
			logger.info("..:insertarDetalleAutorizacion. codigo a evaluar>> "+dtoDetAutorizacion.getCodigoEvaluar());
			
			if(rs.next())
			{			
				PreparedStatementDecorator insertarDetalleAutorizacion =  new PreparedStatementDecorator(con.prepareStatement(strInsertarDetalleAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_det_autorizaciones");
				
				insertarDetalleAutorizacion.setInt(1,codigo);
				insertarDetalleAutorizacion.setInt(2,Utilidades.convertirAEntero(dtoDetAutorizacion.getAutorizacion()));
				
				if(rs.getInt("numero_solicitud")>0)
					insertarDetalleAutorizacion.setInt(3,rs.getInt("numero_solicitud"));
				else
					insertarDetalleAutorizacion.setNull(3,Types.INTEGER);
				
				if(rs.getInt("orden_ambulatoria")>0)				
					insertarDetalleAutorizacion.setInt(4,rs.getInt("orden_ambulatoria"));
				else
					insertarDetalleAutorizacion.setNull(4,Types.NUMERIC);
					
				if(rs.getInt("det_cargo")>0)				
					insertarDetalleAutorizacion.setInt(5,rs.getInt("det_cargo"));
				else
					insertarDetalleAutorizacion.setNull(5,Types.NUMERIC);
				
				if(rs.getInt("servicio")>0)			
					insertarDetalleAutorizacion.setInt(6,rs.getInt("servicio")); // 6 servicio
				else
					insertarDetalleAutorizacion.setNull(6,Types.INTEGER);
								
				if(rs.getInt("articulo")>0)				
					insertarDetalleAutorizacion.setInt(7,rs.getInt("articulo")); // 7 articulo
				else
					insertarDetalleAutorizacion.setNull(7,Types.INTEGER);
				
				if(rs.getInt("cantidad")>0)
					insertarDetalleAutorizacion.setInt(8,rs.getInt("cantidad"));
				else
					insertarDetalleAutorizacion.setNull(8,Types.INTEGER);
				
				if(!dtoDetAutorizacion.getEstadoSolDetAuto().equals(""))
					insertarDetalleAutorizacion.setString(9,dtoDetAutorizacion.getEstadoSolDetAuto());
				else
					insertarDetalleAutorizacion.setNull(9,Types.VARCHAR);
				
				insertarDetalleAutorizacion.setString(10,dtoDetAutorizacion.getActivo());
				
				if(!rs.getString("justificacion_solicitud").equals(""))
					insertarDetalleAutorizacion.setString(11,rs.getString("justificacion_solicitud"));
				else
					insertarDetalleAutorizacion.setNull(11,Types.VARCHAR);
								
				insertarDetalleAutorizacion.setString(12,dtoDetAutorizacion.getUsuarioModifica().getLoginUsuario());
							
				if(rs.getInt("tipo_asocio")>0)
					insertarDetalleAutorizacion.setInt(13,rs.getInt("tipo_asocio")); // 15 tipo asocio
				else
					insertarDetalleAutorizacion.setNull(13,Types.INTEGER);
					
				if(rs.getInt("servicio_cx")>0)
					insertarDetalleAutorizacion.setInt(14,rs.getInt("servicio_cx")); // 16 sevicio_cx
				else
					insertarDetalleAutorizacion.setNull(14,Types.INTEGER);
				
				if(insertarDetalleAutorizacion.executeUpdate()>0){
					insertarDetalleAutorizacion.close();
					return codigo;
				}
			}
			else
			{
				logger.info("No se encontro informacion del servicio/articulo");
				return ConstantesBD.codigoNuncaValido;
			}
		}catch(SQLException e){
			logger.info("Error en el ingreso del Detalles de la Solicitud de Autorizacion !!!!!!!!!");
			logger.error(e);
			return 0;
		}
		return 0;	
	}
	
	//*********************************************************************************************
	
	/**
	 * Insertar Detalle Autorizacion Admision Estancias
	 * @param Connection con
	 * @param DtoDetAutorizacion dtoDetAutorizacion
	 */
	public static int insertarDetalleAutorizacionAE(Connection con, DtoDetAutorizacion dtoDetAutorizacion) 
	{
		try
		{
			PreparedStatementDecorator infoBasica = null;							
			PreparedStatementDecorator insertarDetalleAutorizacion =  new PreparedStatementDecorator(con.prepareStatement(strInsertarDetalleAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_det_autorizaciones");
			
			insertarDetalleAutorizacion.setInt(1,codigo);
			insertarDetalleAutorizacion.setInt(2,Utilidades.convertirAEntero(dtoDetAutorizacion.getAutorizacion()));
			
			insertarDetalleAutorizacion.setNull(3,Types.INTEGER);			
			insertarDetalleAutorizacion.setNull(4,Types.NUMERIC);				
			insertarDetalleAutorizacion.setNull(5,Types.NUMERIC);
			
			if(dtoDetAutorizacion.getServicioArticulo().getCodigo()>0)			
				insertarDetalleAutorizacion.setInt(6,dtoDetAutorizacion.getServicioArticulo().getCodigo()); // 6 servicio
			else
				insertarDetalleAutorizacion.setNull(6,Types.INTEGER);
									
			insertarDetalleAutorizacion.setNull(7,Types.INTEGER);
			
			if(dtoDetAutorizacion.getCantidad()>0)
				insertarDetalleAutorizacion.setInt(8,dtoDetAutorizacion.getCantidad());
			else
				insertarDetalleAutorizacion.setNull(8,Types.INTEGER);
			
			if(!dtoDetAutorizacion.getEstadoSolDetAuto().equals(""))
				insertarDetalleAutorizacion.setString(9,dtoDetAutorizacion.getEstadoSolDetAuto());
			else
				insertarDetalleAutorizacion.setNull(9,Types.VARCHAR);
			
			insertarDetalleAutorizacion.setString(10,dtoDetAutorizacion.getActivo());
			
			if(!dtoDetAutorizacion.getJustificacionSolicitud().equals(""))
				insertarDetalleAutorizacion.setString(11,dtoDetAutorizacion.getJustificacionSolicitud());
			else
				insertarDetalleAutorizacion.setNull(11,Types.VARCHAR);
							
			insertarDetalleAutorizacion.setString(12,dtoDetAutorizacion.getUsuarioModifica().getLoginUsuario());
			insertarDetalleAutorizacion.setNull(13,Types.INTEGER);			
			insertarDetalleAutorizacion.setNull(14,Types.INTEGER);
			
			if(insertarDetalleAutorizacion.executeUpdate()>0){
				insertarDetalleAutorizacion.close();
				return codigo;
			}
			
		}catch(SQLException e){
			logger.info("Error en el ingreso del Detalles de la Solicitud de Autorizacion AE !!!!!!!!!");
			logger.error(e);
			return 0;
		}
		return 0;	
	}
	
	//*********************************************************************************************
	
	
	/**
	 * Insertar Envio Autorizacion
	 */
	public static int insertarEnvioAutorizacion(Connection con, DtoEnvioAutorizacion dtoEnvAutorizacion) 
	{
		try{
			logger.info("------------------------------------------------------------------");
			logger.info("path_archivo descarga: "+dtoEnvAutorizacion.getUrlArchivoIncoXmlDes());
			logger.info("------------------------------------------------------------------");
			PreparedStatementDecorator insertarEnvioAutorizacion =  new PreparedStatementDecorator(con.prepareStatement(strInsertarEnvioAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo_pk = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_envio_autorizaciones");
			int codigoEntidad = ConstantesBD.codigoNuncaValido; 
			insertarEnvioAutorizacion.setInt(1,codigo_pk);
			insertarEnvioAutorizacion.setInt(2,Utilidades.convertirAEntero(dtoEnvAutorizacion.getDetAutorizacion()));
			
			if(dtoEnvAutorizacion.getEntidadEnvio().getValue().split(ConstantesBD.separadorSplit).length > 1)
				codigoEntidad = Utilidades.convertirAEntero(dtoEnvAutorizacion.getEntidadEnvio().getValue().split(ConstantesBD.separadorSplit)[0]);
			else
				codigoEntidad = Utilidades.convertirAEntero(dtoEnvAutorizacion.getEntidadEnvio().getValue());				

			if(codigoEntidad>0)
			{
				if(dtoEnvAutorizacion.isEsEmpresa())
				{
					insertarEnvioAutorizacion.setInt(3,codigoEntidad);
					insertarEnvioAutorizacion.setNull(4,Types.INTEGER);					
				}
				else
				{
					insertarEnvioAutorizacion.setNull(3,Types.INTEGER);
					insertarEnvioAutorizacion.setInt(4,codigoEntidad);
				}
			}
			else
			{
				insertarEnvioAutorizacion.setNull(3,Types.INTEGER);
				insertarEnvioAutorizacion.setNull(4,Types.INTEGER);
			}
			
			insertarEnvioAutorizacion.setString(5,dtoEnvAutorizacion.getMedioEnvio());
			insertarEnvioAutorizacion.setString(6,dtoEnvAutorizacion.getFechaModifica());
			insertarEnvioAutorizacion.setString(7,dtoEnvAutorizacion.getHoraModifica());
			insertarEnvioAutorizacion.setString(8,dtoEnvAutorizacion.getUsuarioModifica().getLoginUsuario());
			insertarEnvioAutorizacion.setString(9,dtoEnvAutorizacion.getUrlArchivoIncoXmlDes());
			
			if(insertarEnvioAutorizacion.executeUpdate()>0){
				insertarEnvioAutorizacion.close();
				return codigo_pk;
			}
		}catch(SQLException e){
			logger.info("Error en el ingreso del Envios de la Solicitud de Autorizacion !!!!!!!!!");
			logger.error(e);
			return 0;
		}
		return 0;	
	}
	
	//*********************************************************************************************
	
	/**
	 * Insertar Archivos Adjuntos a las Solicitudes de Autorizacioness
	 */
	public static int insertarDiagnosticoAutorizacion(Connection con, DtoDiagAutorizacion dtoDiagAutorizacion) 
	{
		try{
			PreparedStatementDecorator insertarDiagAutorizacion =  new PreparedStatementDecorator(con.prepareStatement(strInsertarDiagAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_diag_autorizaciones");
			insertarDiagAutorizacion.setInt(1,codigo);
			insertarDiagAutorizacion.setInt(2,Utilidades.convertirAEntero(dtoDiagAutorizacion.getAutorizacion()));
			insertarDiagAutorizacion.setString(3,dtoDiagAutorizacion.getDiagnostico().getAcronimo());
			insertarDiagAutorizacion.setInt(4,dtoDiagAutorizacion.getDiagnostico().getTipoCIE());
			insertarDiagAutorizacion.setString(5,dtoDiagAutorizacion.getDiagnostico().isPrincipal()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			insertarDiagAutorizacion.setString(6,dtoDiagAutorizacion.getFechaModifica());
			insertarDiagAutorizacion.setString(7,dtoDiagAutorizacion.getHoraModifica());
			insertarDiagAutorizacion.setString(8,dtoDiagAutorizacion.getUsuarioModifica().getLoginUsuario());
			if(insertarDiagAutorizacion.executeUpdate()>0){
				insertarDiagAutorizacion.close();
				return codigo;
			}
		}catch(SQLException e){
			logger.info("Error en el ingreso del Diagnostico Principal o Relacionado de la Solicitud de Autorizacion !!!!!!!!!");
			logger.error(e);
			return 0;
		}
		return 0;	
	}
	
	//*********************************************************************************************
	
	/**
	 * Insertar Archivo Adjunto de la Autorizacion
	 */
	public static int insertarAdjuntoAutorizacion(Connection con, DtoAdjAutorizacion dtoAdjAutorizacion) 
	{
		try{
			PreparedStatementDecorator insertarAdjgAutorizacion =  new PreparedStatementDecorator(con.prepareStatement(strInsertarAdjuntoAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo_pk = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_adj_autorizaciones");
			
			insertarAdjgAutorizacion.setInt(1,codigo_pk);
			insertarAdjgAutorizacion.setInt(2,Utilidades.convertirAEntero(dtoAdjAutorizacion.getAutorizacion()));
			insertarAdjgAutorizacion.setString(3,dtoAdjAutorizacion.getNombreArchivo());
			insertarAdjgAutorizacion.setString(4,dtoAdjAutorizacion.getNombreOriginal());
			
			insertarAdjgAutorizacion.setString(5,dtoAdjAutorizacion.getFechaModifica());
			insertarAdjgAutorizacion.setString(6,dtoAdjAutorizacion.getHoraModifica());
			insertarAdjgAutorizacion.setString(7,dtoAdjAutorizacion.getUsuarioModifica().getLoginUsuario());
			
			if(insertarAdjgAutorizacion.executeUpdate()>0){
				insertarAdjgAutorizacion.close();
				return codigo_pk;
			}
		}catch(SQLException e){
			logger.info("Error en el ingreso del Archivo Adjunto a la Autorizacion !!!!!!!!!");
			logger.error(e);
			return 0;
		}
		return 0;	
	}
	
	//*********************************************************************************************
	
	/**
	 * Insertar Respuesta de Autorizacion
	 */
	public static int insertarRespuestaAutorizacion(Connection con, DtoRespAutorizacion dtoRespAutorizacion) 
	{
		try{
			PreparedStatementDecorator insertarRespAutorizacion =  new PreparedStatementDecorator(con.prepareStatement(strInsertarRespuestaAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			insertarRespAutorizacion.setInt(1,Utilidades.convertirAEntero(dtoRespAutorizacion.getDetAutorizacion()));
			
			if(!dtoRespAutorizacion.getVigencia().equals(""))
				insertarRespAutorizacion.setInt(2,Utilidades.convertirAEntero(dtoRespAutorizacion.getVigencia()));
			else
				insertarRespAutorizacion.setNull(2,Types.INTEGER);
			
			if(dtoRespAutorizacion.getTipoVigencia().getCodigo()>0)
				insertarRespAutorizacion.setInt(3,dtoRespAutorizacion.getTipoVigencia().getCodigo());
			else
				insertarRespAutorizacion.setNull(3,Types.INTEGER);
				
			insertarRespAutorizacion.setString(4,dtoRespAutorizacion.getNumeroAutorizacion());
			insertarRespAutorizacion.setString(5,dtoRespAutorizacion.getPersonaAutoriza());
			
			if(!dtoRespAutorizacion.getValorCobertura().equals(""))
				insertarRespAutorizacion.setDouble(6,Double.parseDouble(dtoRespAutorizacion.getValorCobertura()));
			else
				insertarRespAutorizacion.setNull(6, Types.NUMERIC);
			
			if(!dtoRespAutorizacion.getTipoCobertura().equals(""))
				insertarRespAutorizacion.setString(7,dtoRespAutorizacion.getTipoCobertura());
			else
				insertarRespAutorizacion.setNull(7, Types.VARCHAR);
			
			if(!dtoRespAutorizacion.getValorPagoPaciente().equals(""))
				insertarRespAutorizacion.setDouble(8,Double.parseDouble(dtoRespAutorizacion.getValorPagoPaciente()));
			else
				insertarRespAutorizacion.setNull(8, Types.NUMERIC);
			
			if(!dtoRespAutorizacion.getTipoPagoPaciente().equals(""))
				insertarRespAutorizacion.setString(9,dtoRespAutorizacion.getTipoPagoPaciente());
			else
				insertarRespAutorizacion.setNull(9, Types.VARCHAR);
			
			if(!dtoRespAutorizacion.getPersonaRecibe().equals(""))
				insertarRespAutorizacion.setString(10,dtoRespAutorizacion.getPersonaRecibe());
			else
				insertarRespAutorizacion.setNull(10, Types.VARCHAR);
			
			if(Utilidades.convertirAEntero(dtoRespAutorizacion.getCargoPersRecibe())>0)
				insertarRespAutorizacion.setInt(11, Utilidades.convertirAEntero(dtoRespAutorizacion.getCargoPersRecibe()));
			else
				insertarRespAutorizacion.setNull(11, Types.INTEGER);
			
			insertarRespAutorizacion.setString(12, dtoRespAutorizacion.getPersonaRegistro());
			
			if(Utilidades.convertirAEntero(dtoRespAutorizacion.getCodCargoPersRegistro())>0)
				insertarRespAutorizacion.setInt(13, Utilidades.convertirAEntero(dtoRespAutorizacion.getCodCargoPersRegistro()));
			else
				insertarRespAutorizacion.setNull(13, Types.INTEGER);			
			
			insertarRespAutorizacion.setDate(14, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoRespAutorizacion.getFechaAutorizacion())));
			insertarRespAutorizacion.setString(15,dtoRespAutorizacion.getHoraAutorizacion());
			insertarRespAutorizacion.setInt(16, Utilidades.convertirAEntero(dtoRespAutorizacion.getCantidadSolicitada()));
			insertarRespAutorizacion.setInt(17, Utilidades.convertirAEntero(dtoRespAutorizacion.getCantidadAutorizada()));
			
			if(!dtoRespAutorizacion.getFechaInicialAutorizada().equals(""))
				insertarRespAutorizacion.setDate(18, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoRespAutorizacion.getFechaInicialAutorizada())));
			else
				insertarRespAutorizacion.setNull(18,Types.DATE);
			
			if(!dtoRespAutorizacion.getFechaFinalAutorizada().equals(""))
				insertarRespAutorizacion.setDate(19, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtoRespAutorizacion.getFechaFinalAutorizada())));
			else
				insertarRespAutorizacion.setNull(19,Types.DATE);
			
			if(!dtoRespAutorizacion.getObservacion().equals(""))
				insertarRespAutorizacion.setString(20,dtoRespAutorizacion.getObservacion());
			else
				insertarRespAutorizacion.setNull(20, Types.VARCHAR);
			
			if(insertarRespAutorizacion.executeUpdate()>0){
				insertarRespAutorizacion.close();
				return Utilidades.convertirAEntero(dtoRespAutorizacion.getDetAutorizacion());
			}
		}catch(SQLException e){
			logger.info("Error en el ingreso de la Respuesta de la Solicitud de la Autorizacion !!!!!!!!!");
			logger.error(e);
			return 0;
		}
		return 0;	
	}
	
	//*********************************************************************************************
	
	/**
	 * Consultar los detalles de una autorizacion basados en el ingreso
	 */
	public static DtoAutorizacion cargarAutorizacion(Connection con, HashMap parametros) 
	{
		DtoAutorizacion dtoAutorizacion = new DtoAutorizacion();
		
		try{
			//***********SE CARGA PRIMERO EL DETALLE DE LA AUTORIZACION****************************++
			ArrayList<DtoDetAutorizacion> dtoDetAuto = cargarDetallesAutorizacion(con,parametros);
			DtoDetAutorizacion detAutorizacion = new DtoDetAutorizacion();
			if(dtoDetAuto.size()==1)
				detAutorizacion = dtoDetAuto.get(0); 
			//***************************************************************************************				
			logger.info("..:Cargar Autorizacion >> codigo pk detalle "+detAutorizacion.getCodigoPK()+" codigo pk encabezado >> "+detAutorizacion.getAutorizacion());
			if(!detAutorizacion.getCodigoPK().equals(""))
			{
				String cadena = cargarAutorizacionStr +" AND a.codigo = "+detAutorizacion.getAutorizacion();
				
				//************SE CARGA EL ENCABEZADO DE LA AUTORIZACIÓN****************************************
				PreparedStatementDecorator Autorizacion = new PreparedStatementDecorator( new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)));
				
				ResultSetDecorator rs = new ResultSetDecorator(Autorizacion.executeQuery());
				if(rs.next())
				{
					dtoAutorizacion = new DtoAutorizacion();
					dtoAutorizacion.setCodigoPK(rs.getString("codigo"));
					dtoAutorizacion.setConsecutivo(rs.getString("consecutivo"));
					dtoAutorizacion.setAnioConsecutivo(rs.getString("anio_consecutivo"));
					dtoAutorizacion.setIdIngreso(rs.getString("id_ingreso"));
					dtoAutorizacion.setIdSubCuenta(rs.getString("id_sub_cuenta"));
					dtoAutorizacion.setCodigoTipoServicioSolicitado(rs.getInt("cod_tipo_ser_sol"));
					dtoAutorizacion.setNombreTipoServicioSolicitado(rs.getString("nom_tipo_ser_sol"));
					dtoAutorizacion.setObservaciones(rs.getString("observaciones"));			
					dtoAutorizacion.setCodigoTipoCobertura(rs.getInt("cod_tipo_cobertura"));
					dtoAutorizacion.setNombreTipoCobertura(rs.getString("nom_tipo_cobertura"));
					dtoAutorizacion.setCodigoOrigenAtencion(rs.getInt("cod_origen_atencion"));
					dtoAutorizacion.setNombreOrigenAtencion(rs.getString("nom_origen_atencion"));
					dtoAutorizacion.setFechaSolicitud(rs.getString("fecha_solicitud"));
					dtoAutorizacion.setHoraSolicitud(rs.getString("hora_solicitud"));
					if(!rs.getString("usuario_solicitud").toString().equals(""))
						dtoAutorizacion.getUsuarioSolicitud().cargarUsuarioBasico(con, rs.getString("usuario_solicitud"));
					dtoAutorizacion.setTipoTramite(rs.getString("tipo_tramite"));
					dtoAutorizacion.setCodigoConvenio(rs.getInt("codigo_convenio"));
					dtoAutorizacion.setNombreConvenio(rs.getString("nombre_convenio"));
					dtoAutorizacion.setPrioridadAtencion(rs.getString("prioridad_atencion"));
					dtoAutorizacion.setIdCuenta(rs.getString("id_cuenta"));
					dtoAutorizacion.getCama().setCodigo(rs.getInt("codigo_cama"));
					dtoAutorizacion.getCama().setNombre(rs.getString("nombre_cama"));
					dtoAutorizacion.setFechaModifica(rs.getString("fecha_modifica"));
					dtoAutorizacion.setHoraModifica(rs.getString("hora_modifica"));
					dtoAutorizacion.getUsuarioSolicitud().cargarUsuarioBasico(con, rs.getString("usuario_modifica"));
					dtoAutorizacion.getPersonaSolicita().setCodigo(rs.getInt("persona_solicita"));
					dtoAutorizacion.getPersonaSolicita().setNombre(rs.getString("nom_persona_solicita"));
					dtoAutorizacion.setTipo(rs.getString("tipo_autorizacion"));
										
					//Para este caso en que solo se devuelve un detalle la solicitud toma el estado de la respuesta
					dtoAutorizacion.setEstado(detAutorizacion.getEstadoSolDetAuto());
				}else
					logger.info("La consulta Solicitada no Arrogo ningun Resultado !!!!!!\n La Consulta: "+cargarAutorizacionStr);
				
				//almacena la información
				dtoAutorizacion.getDetalle().add(detAutorizacion);
				
				if(!dtoAutorizacion.getCodigoPK().equals("") && dtoAutorizacion!=null)
				{
					HashMap parametro = new HashMap<String,Object>();
					parametro.put("autorizacion",detAutorizacion.getCodigoPK());
					//**************SE CARGAN LOS ENVÍOS DE AUTORIZACION**********************************
					dtoAutorizacion.getDetalle().get(0).setEnvios(cargarEnvioAutorizacion(con,parametro));
					//************************************************************************************
					//logger.info("valor del detalle envios >> "+dtoAutorizacion.getDetalle().get(0).getEnvios().size());
					
					//*************SE CARGAN LOS ADJUNTOS DE LA AUTORIZACION********************************+
					parametro.put("autorizacion",dtoAutorizacion.getCodigoPK());
					dtoAutorizacion.setAdjuntos(caragarAdjAutorizacion(con,parametro));
					//****************************************************************************************
					
					//**************SE CARGAN LOS DIAGNÓSTICOAS DE LA AUTORIZACION***************************
					dtoAutorizacion.setDiagnosticos(cargarDiagAutorizacion(con, parametro));
					//***************************************************************************************+
				}		
				
				rs.close();
				Autorizacion.close();
			}
		}catch(SQLException e){
			logger.info("Error Consultando la Autorizacion con sus Detales de Autorizacion!!!!!!\n La Consulta: "+cargarAutorizacionStr);
			logger.error(e);			
			return null;
		}		
				
		return dtoAutorizacion;
	}
	
//*********************************************************************************************
	
	/**
	 * Consultar listado de solicitudes enviadas
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static ArrayList<DtoAutorizacion> cargarListadoAutorizaciones(Connection con, HashMap parametros) 
	{
		ArrayList<DtoAutorizacion> array = new ArrayList<DtoAutorizacion>();
		HashMap aux = new HashMap();
		String cadena = cargarListadoAutorizacionStr;
		
		try
		{
			//***************************************************************************************
			if(parametros.containsKey("cuenta") && 
					!parametros.get("cuenta").toString().equals(""))
				cadena += " AND a.cuenta = "+parametros.get("cuenta").toString();
			
			if(parametros.containsKey("tipo") && 
					!parametros.get("tipo").toString().equals(""))
				cadena += " AND a.tipo IN ("+parametros.get("tipo").toString()+") ";
			
			if(parametros.containsKey("subCuenta") && 
					!parametros.get("subCuenta").toString().equals(""))
				cadena += " AND a.sub_cuenta = "+parametros.get("subCuenta").toString();
			
			cadena += " ORDER BY a.codigo DESC ";
				
			//logger.info("valor de la cadena >> "+cadena);
			//************SE CARGA EL ENCABEZADO DE LA AUTORIZACIÓN****************************************
			PreparedStatementDecorator Autorizacion = new PreparedStatementDecorator( new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)));
			ResultSetDecorator rs = new ResultSetDecorator(Autorizacion.executeQuery());
			
			while(rs.next())
			{
				DtoAutorizacion dtoAutorizacion = new DtoAutorizacion();
				dtoAutorizacion.setCodigoPK(rs.getString("codigo"));
				dtoAutorizacion.setFechaSolicitud(rs.getString("fecha_solicitud"));
				dtoAutorizacion.setHoraSolicitud(rs.getString("hora_solicitud"));
				dtoAutorizacion.setIdCuenta(rs.getString("id_cuenta"));
				dtoAutorizacion.setNombreConvenio(rs.getString("nombre_convenio"));
				dtoAutorizacion.setEstado(rs.getString("estado"));
				dtoAutorizacion.setColorEstado(Autorizaciones.getColorEstado(rs.getString("estado")));
				dtoAutorizacion.setTipo(rs.getString("tipo_autorizacion"));
				dtoAutorizacion.setTipoTramite(rs.getString("tipo_tramite"));
				
				aux.put("codigoCuenta",rs.getString("id_cuenta"));
				aux.put("codigoSubcuenta",rs.getString("id_sub_cuenta"));
				aux.put("codigoIdIngreso", rs.getString("id_ingreso"));
				aux.put("tipoAuto", rs.getString("tipo_autorizacion"));
				
				ArrayList<DtoCuentaAutorizacion> cuentas = cargarListadoCuentas(con, aux);
				
				if(cuentas.size()>0)
				{
					cuentas.get(0).setIndViaIngCuenta(
							Autorizaciones.getIndicadorCuenta(
									cuentas.get(0).getCodigoViaIngreso(),
									cuentas.get(0).getCodigoTipoPaciente(),
									cuentas.get(0).isEsAsocioPendiente(),
									cuentas.get(0).isFueAsociada()));
					
					dtoAutorizacion.setCuentaAuto(cuentas.get(0));
				}
				
				array.add(dtoAutorizacion);
			}
				
			rs.close();
			Autorizacion.close();
			
		}catch(SQLException e){
			logger.info("Error Consultando la Autorizacion con sus Detales de Autorizacion!!!!!!\n La Consulta: "+cadena);
			logger.error(e);			
			return null;
		}		
				
		return array;
	}
	
	//*********************************************************************************************
	

	/**
	 * Consultar la autorizacion con informacion del encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static DtoAutorizacion cargarAutorizacionXEncabezado(Connection con, HashMap parametros) 
	{
		DtoAutorizacion dtoAutorizacion = new DtoAutorizacion();
		
		String cadena = cargarAutorizacionStr;
		
		if(parametros.containsKey("codigoPkAuto") && 
				Utilidades.convertirAEntero(parametros.get("codigoPkAuto").toString())>0)
			cadena += " AND a.codigo = "+parametros.get("codigoPkAuto").toString();
		
		if(parametros.containsKey("codigoCuenta") &&
				Utilidades.convertirAEntero(parametros.get("codigoCuenta").toString())>0)				
			cadena += " AND a.cuenta = "+parametros.get("codigoCuenta").toString();
		
		if(parametros.containsKey("subCuenta") && 
				Utilidades.convertirAEntero(parametros.get("subCuenta").toString())>0)				
			cadena += " AND a.sub_cuenta = "+parametros.get("subCuenta").toString();
		
		if(parametros.containsKey("tipoAuto") && 
				!parametros.get("tipoAuto").toString().equals(""))
			cadena += " AND a.tipo = '"+parametros.get("tipoAuto").toString()+"' ";
		
		if(parametros.containsKey("ingreso") && 
				Utilidades.convertirAEntero(parametros.get("ingreso").toString())>0)
			cadena += " AND a.ingreso = "+parametros.get("ingreso").toString();
		
		if(parametros.containsKey("activo"))
			if(parametros.get("activo").toString().equals(ConstantesBD.acronimoSi))
				cadena += " AND d.activo = '"+parametros.get("activo").toString()+"' ";			
				
		try
		{							
			//************SE CARGA EL ENCABEZADO DE LA AUTORIZACIÓN****************************************
			PreparedStatementDecorator Autorizacion = new PreparedStatementDecorator( new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)));
			ResultSetDecorator rs = new ResultSetDecorator(Autorizacion.executeQuery());
			
			if(rs.next())
			{
				logger.info("..:Cargar Autorizacion >> codigo pk encabezado >> "+rs.getString("codigo"));
				
				dtoAutorizacion = new DtoAutorizacion();
				dtoAutorizacion.setCodigoPK(rs.getString("codigo"));
				dtoAutorizacion.setConsecutivo(rs.getString("consecutivo"));
				dtoAutorizacion.setAnioConsecutivo(rs.getString("anio_consecutivo"));
				dtoAutorizacion.setIdIngreso(rs.getString("id_ingreso"));
				dtoAutorizacion.setIdSubCuenta(rs.getString("id_sub_cuenta"));
				dtoAutorizacion.setCodigoTipoServicioSolicitado(rs.getInt("cod_tipo_ser_sol"));
				dtoAutorizacion.setNombreTipoServicioSolicitado(rs.getString("nom_tipo_ser_sol"));
				dtoAutorizacion.setObservaciones(rs.getString("observaciones"));			
				dtoAutorizacion.setCodigoTipoCobertura(rs.getInt("cod_tipo_cobertura"));
				dtoAutorizacion.setNombreTipoCobertura(rs.getString("nom_tipo_cobertura"));
				dtoAutorizacion.setCodigoOrigenAtencion(rs.getInt("cod_origen_atencion"));
				dtoAutorizacion.setNombreOrigenAtencion(rs.getString("nom_origen_atencion"));
				dtoAutorizacion.setFechaSolicitud(rs.getString("fecha_solicitud"));
				dtoAutorizacion.setHoraSolicitud(rs.getString("hora_solicitud"));
				if(!rs.getString("usuario_solicitud").toString().equals(""))
					dtoAutorizacion.getUsuarioSolicitud().cargarUsuarioBasico(con, rs.getString("usuario_solicitud"));
				dtoAutorizacion.setTipoTramite(rs.getString("tipo_tramite"));
				dtoAutorizacion.setCodigoConvenio(rs.getInt("codigo_convenio"));
				dtoAutorizacion.setNombreConvenio(rs.getString("nombre_convenio"));
				dtoAutorizacion.setPrioridadAtencion(rs.getString("prioridad_atencion"));
				dtoAutorizacion.setIdCuenta(rs.getString("id_cuenta"));
				dtoAutorizacion.getCama().setCodigo(rs.getInt("codigo_cama"));
				dtoAutorizacion.getCama().setNombre(rs.getString("nombre_cama"));
				dtoAutorizacion.setFechaModifica(rs.getString("fecha_modifica"));
				dtoAutorizacion.setHoraModifica(rs.getString("hora_modifica"));
				dtoAutorizacion.getUsuarioSolicitud().cargarUsuarioBasico(con, rs.getString("usuario_modifica"));
				dtoAutorizacion.getPersonaSolicita().setCodigo(rs.getInt("persona_solicita"));
				dtoAutorizacion.getPersonaSolicita().setNombre(rs.getString("nom_persona_solicita"));
				dtoAutorizacion.setTipo(rs.getString("tipo_autorizacion"));
																		
				//***********SE CARGA PRIMERO EL DETALLE DE LA AUTORIZACION****************************++
				parametros.put("autorizacion",dtoAutorizacion.getCodigoPK());
				ArrayList<DtoDetAutorizacion> dtoDetAuto = cargarDetallesAutorizacion(con,parametros);
				DtoDetAutorizacion detAutorizacion = new DtoDetAutorizacion();
				
				//Para este caso en que solo se devuelve un detalle la solicitud toma el estado de la respuesta
				if(dtoDetAuto.size()==1)
				{
					detAutorizacion = dtoDetAuto.get(0);
					dtoAutorizacion.setEstado(detAutorizacion.getEstadoSolDetAuto());
				}
				
				//almacena la información
				dtoAutorizacion.setDetalle(dtoDetAuto);
				//***************************************************************************************				
				logger.info("..:Cargar Autorizacion >> codigo pk detalle "+detAutorizacion.getCodigoPK()+" codigo pk encabezado >> "+detAutorizacion.getAutorizacion());
								
				HashMap parametro = new HashMap<String,Object>();
				parametro.put("autorizacion",detAutorizacion.getCodigoPK());
				//**************SE CARGAN LOS ENVÍOS DE AUTORIZACION**********************************
				dtoAutorizacion.getDetalle().get(0).setEnvios(cargarEnvioAutorizacion(con,parametro));
				//************************************************************************************
								
				//*************SE CARGAN LOS ADJUNTOS DE LA AUTORIZACION********************************+
				parametro.put("autorizacion",dtoAutorizacion.getCodigoPK());
				dtoAutorizacion.setAdjuntos(caragarAdjAutorizacion(con,parametro));
				//****************************************************************************************
				
				//**************SE CARGAN LOS DIAGNÓSTICOAS DE LA AUTORIZACION***************************
				dtoAutorizacion.setDiagnosticos(cargarDiagAutorizacion(con, parametro));
				//***************************************************************************************+
							
			}
			else
				logger.info("La consulta Solicitada no Arrogo ningun Resultado !!!!!!\n La Consulta: "+cadena);								
								
			rs.close();
			Autorizacion.close();
			
		}catch(SQLException e){
			logger.info("Error Consultando la Autorizacion con sus Detales de Autorizacion!!!!!!\n La Consulta: "+cadena);
			logger.error(e);			
			return null;
		}		
				
		return dtoAutorizacion;
	}
	
	//*********************************************************************************************
	
	/**
	 * Consultar los detalles de una autorizacion
	 */
	public static ArrayList<DtoDetAutorizacion> cargarDetallesAutorizacion(Connection con, HashMap parametros) 
	{
		ArrayList<DtoDetAutorizacion> array = new ArrayList<DtoDetAutorizacion>();
		try{
			//**************SE TOMAN LOS PARÁMETROS**************************************++
			String cadena = strConsultarDetalleAutorizaciones;
			
			if(parametros.containsKey("codigoDetAutorizacion"))
				if(Utilidades.convertirAEntero(parametros.get("codigoDetAutorizacion").toString())>0)
					cadena += " AND da.codigo = "+parametros.get("codigoDetAutorizacion").toString();
			
			if(parametros.containsKey("codDetCarAmb"))
			{
				if(Utilidades.convertirAEntero(parametros.get("codDetCarAmb").toString())>0)
				{
					if(parametros.get("tipoOrden").toString().equals("2"))
						cadena += " AND da.det_cargo = "+parametros.get("codDetCarAmb").toString();
					else
						cadena += " AND da.orden_ambulatoria = "+parametros.get("codDetCarAmb").toString();
				}
			}
			
			if(parametros.containsKey("activo"))
				if(parametros.get("activo").toString().equals(ConstantesBD.acronimoSi))
					cadena += " AND da.activo = '"+parametros.get("activo").toString()+"' ";
			
			if(parametros.containsKey("autorizacion"))
				if(!parametros.get("autorizacion").toString().equals(""))
					cadena += " AND da.autorizacion = "+parametros.get("autorizacion").toString()+" ";
									
			int codigoInstitucion = Utilidades.convertirAEntero(parametros.get("codigoInstitucion").toString());
			logger.info("..:cargarDetallesAutorizacion  >> "+parametros);
			logger.info(".::Cadena :"+cadena);
			//******************************************************************************
			int codigoTarifario = Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion),true);			
			PreparedStatementDecorator DetAutorizaciones = new PreparedStatementDecorator( new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)));
			DetAutorizaciones.setInt(1,codigoTarifario);
			DetAutorizaciones.setInt(2,codigoTarifario);			
			
			ResultSetDecorator rs = new ResultSetDecorator(DetAutorizaciones.executeQuery());
			
			while(rs.next())
			{
				DtoDetAutorizacion dtoDetAutorizacion = new DtoDetAutorizacion();
				dtoDetAutorizacion.setCodigoPK(rs.getString("codigo"));
				dtoDetAutorizacion.setAutorizacion(rs.getString("autorizacion"));
				dtoDetAutorizacion.setNumeroSolicitud(rs.getString("numero_solicitud"));
				dtoDetAutorizacion.setOrdenAmbulatoria(rs.getString("orden_ambulatoria"));
				dtoDetAutorizacion.setDetCargo(rs.getString("det_cargo"));
				//----------- Se verifica que tipo detalle autorizacion que se carga
				dtoDetAutorizacion.getServicioCx().setCodigo(rs.getInt("codigo_servicio_cx"));
				if(dtoDetAutorizacion.getServicioCx().getCodigo()>0)
				{ 
					dtoDetAutorizacion.setEsServicio(1);// servicio_cx con el asocio
					dtoDetAutorizacion.getServicioCx().setNombre(rs.getString("nombre_serviciocx"));
					dtoDetAutorizacion.setCodigoCUPS(rs.getString("codigo_cups_cx"));
					dtoDetAutorizacion.getTipoAsocio().setCodigo(rs.getInt("tipo_asocio"));
					dtoDetAutorizacion.getServicioArticulo().setCodigo(rs.getInt("codigo_servicio"));
					if(dtoDetAutorizacion.getServicioArticulo().getCodigo()>0)
						dtoDetAutorizacion.getServicioArticulo().setNombre(rs.getString("nombre_servicio"));
					if(dtoDetAutorizacion.getTipoAsocio().getCodigo()>0)
						dtoDetAutorizacion.getTipoAsocio().setNombre(rs.getString("nombre_tipo_asocio"));
				}
				else
				{	
					dtoDetAutorizacion.getServicioArticulo().setCodigo(rs.getInt("codigo_servicio"));
					if(dtoDetAutorizacion.getServicioArticulo().getCodigo()>0){
						dtoDetAutorizacion.setEsServicio(2); // servicio de interconsulta - procedimientos 
						dtoDetAutorizacion.getServicioArticulo().setNombre(rs.getString("nombre_servicio"));
						dtoDetAutorizacion.setCodigoCUPS(rs.getString("codigo_cups"));
					}else{
						dtoDetAutorizacion.setEsServicio(3); // articulo
						dtoDetAutorizacion.getServicioArticulo().setCodigo(rs.getInt("codigo_articulo"));
						dtoDetAutorizacion.getServicioArticulo().setNombre(rs.getString("nombre_articulo"));
						dtoDetAutorizacion.setCodigoCUPS(rs.getString("codigo_cups_art"));
					}	
				}
				dtoDetAutorizacion.setCantidad(rs.getInt("cantidad"));
				dtoDetAutorizacion.setJustificacionSolicitud(rs.getString("justificacion_solicitud"));
				dtoDetAutorizacion.setFechaModifica(rs.getString("fecha_modifica"));
				dtoDetAutorizacion.setHoraModifica(rs.getString("hora_modifica"));
				dtoDetAutorizacion.getUsuarioModifica().cargarUsuarioBasico(con, rs.getString("usuario_modifica"));
				dtoDetAutorizacion.setEstadoSolDetAuto(rs.getString("estado"));
				dtoDetAutorizacion.setTipoServicio(rs.getString("tipo_servicio").toString());
				
				//Carga la respuesta de la autorizacion
				HashMap filtros = new HashMap();
				filtros.put("codigoDetalle",dtoDetAutorizacion.getCodigoPK());
				dtoDetAutorizacion.setRespuestaDto(cargarRespuestaAutorizacion(con,filtros));
				
				array.add(dtoDetAutorizacion);
			}
			
			rs.close();
			DetAutorizaciones.close();
		}catch(SQLException e){
			logger.info("Error Consultando los Detalles de la Solicitud de Autorizacion !!!!!!\n La Consulta: "+strConsultarDetalleAutorizaciones);
			logger.error(e);			
			return null;
		}
		return array;
	}
	
	//*********************************************************************************************
	
	/**
	 * Consulta informacion basica del detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap datosBasicosSolicitud(Connection con,HashMap parametros)
	{
		String cadena = "";
		HashMap resultado = new HashMap();
		resultado.put("numRegistros",0);
		
		try
		{
			if(Utilidades.convertirAEntero(parametros.get("codDetCarAmb").toString())>0)
			{
				if(parametros.get("tipoOrden").toString().equals(Autorizaciones.codInternoAutoSolicSerMed))
				{
					cadena = strCargarInfoBasicaDetAutoXCargo;					
					
					PreparedStatementDecorator DetAutorizaciones = new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					DetAutorizaciones.setInt(1,Utilidades.convertirAEntero(parametros.get("codDetCarAmb").toString()));
					
					ResultSetDecorator rs = new ResultSetDecorator(DetAutorizaciones.executeQuery());
					return UtilidadBD.cargarValueObject(rs,true,true);								
				}
				else if(parametros.get("tipoOrden").toString().equals(Autorizaciones.codInternoAutoSolicAmbula))
				{
					logger.info("-->strCargarInfoBasicaDetAutoXAmbula:\n"+strCargarInfoBasicaDetAutoXAmbula+"\ncodDetCarAmb: "+Utilidades.convertirAEntero(parametros.get("codDetCarAmb").toString()));
					PreparedStatementDecorator DetAutorizaciones = new PreparedStatementDecorator(con.prepareStatement(strCargarInfoBasicaDetAutoXAmbula,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					DetAutorizaciones.setInt(1,Utilidades.convertirAEntero(parametros.get("codDetCarAmb").toString()));
					
					ResultSetDecorator rs = new ResultSetDecorator(DetAutorizaciones.executeQuery());
					return UtilidadBD.cargarValueObject(rs,true,true);
				}
			}
		}
		catch (Exception e) {
			logger.info("error en datosBasicosServicioArticulo >> "+e);
			return resultado;
		}
		
		return resultado;
		
	}
	
	//*********************************************************************************************
	
	/**
	 * Consultar los Envios de una autorizacion
	 */
	public static ArrayList<DtoEnvioAutorizacion> cargarEnvioAutorizacion(Connection con, HashMap parametros) 
	{
		ArrayList<DtoEnvioAutorizacion> EnvAuto = new ArrayList<DtoEnvioAutorizacion>();
		
		try
		{
			PreparedStatementDecorator EnvAutorizaciones =  new PreparedStatementDecorator(con.prepareStatement(strConsultarEnvioAutorizaciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			EnvAutorizaciones.setInt(1,Utilidades.convertirAEntero(parametros.get("autorizacion").toString()));
			ResultSetDecorator rs = new ResultSetDecorator(EnvAutorizaciones.executeQuery());
			
			//logger.info("valor de la sql >> "+strConsultarEnvioAutorizaciones+" >> "+parametros.get("autorizacion").toString());
			
			while(rs.next())
			{	
				DtoEnvioAutorizacion dtoEnvAutorizacion = new DtoEnvioAutorizacion();
				dtoEnvAutorizacion.setCodigoPK(rs.getString("codigo_pk"));
				dtoEnvAutorizacion.setDetAutorizacion(rs.getString("det_autorizacion"));
				
				if(rs.getInt("cod_entidad_envio") > 0)
				{
					dtoEnvAutorizacion.setEsEmpresa(true);
					dtoEnvAutorizacion.getEntidadEnvio().setValue("");
					dtoEnvAutorizacion.getEntidadEnvio().setCodigo(rs.getInt("cod_entidad_envio"));
					dtoEnvAutorizacion.getEntidadEnvio().setDescripcionInd(rs.getString("nom_entidad_envio"));
				}
				else
				{
					dtoEnvAutorizacion.setEsEmpresa(false);
					dtoEnvAutorizacion.getEntidadEnvio().setValue("");
					dtoEnvAutorizacion.getEntidadEnvio().setCodigo(rs.getInt("cod_convenio_envio"));
					dtoEnvAutorizacion.getEntidadEnvio().setDescripcionInd(rs.getString("nom_convenio_envio"));					
				}			
				
				dtoEnvAutorizacion.setMedioEnvio(rs.getString("medio_envio"));
				dtoEnvAutorizacion.setFechaModifica(rs.getString("fecha_modifica"));
				dtoEnvAutorizacion.setHoraModifica(rs.getString("hora_modifica"));
				dtoEnvAutorizacion.getUsuarioModifica().cargarUsuarioBasico(con, rs.getString("usuario_modifica"));
				dtoEnvAutorizacion.setUrlArchivoIncoXmlDes(rs.getString("patharchivo"));
				EnvAuto.add(dtoEnvAutorizacion);				
			}
			
			rs.close();
			EnvAutorizaciones.close();
		}catch(SQLException e){
			logger.info("Error Consultando los Envios de la Solicitud de Autorizacion !!!!!!\n La Consulta: "+strConsultarEnvioAutorizaciones);
			logger.error(e);			
			return null;
		}
		return EnvAuto;
	}
	
	/**
	 * Consultar los diagnosticos de una solicitd de autorizacion
	 */
	public static ArrayList<DtoDiagAutorizacion> cargarDiagAutorizacion(Connection con, HashMap parametros) 
	{
		ArrayList<DtoDiagAutorizacion> array = new ArrayList<DtoDiagAutorizacion>();
		try{
			PreparedStatementDecorator DiagAutorizaciones =  new PreparedStatementDecorator(con.prepareStatement(strConsultarDiagnosticoAutorizaciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			DiagAutorizaciones.setInt(1,Utilidades.convertirAEntero(parametros.get("autorizacion").toString()));
			ResultSetDecorator rs = new ResultSetDecorator(DiagAutorizaciones.executeQuery());
			//logger.info("valor de la sql >> "+strConsultarDiagnosticoAutorizaciones+" "+parametros.get("autorizacion").toString());
			
			while(rs.next())
			{
				DtoDiagAutorizacion dtoDiagAutorizacion = new DtoDiagAutorizacion();
				dtoDiagAutorizacion.setCodigoPK(rs.getString("codigo"));
				dtoDiagAutorizacion.setAutorizacion(rs.getString("autorizacion"));
				dtoDiagAutorizacion.getDiagnostico().setAcronimo(rs.getString("acronimo"));
				dtoDiagAutorizacion.getDiagnostico().setTipoCIE(rs.getInt("tipo_cie"));
				dtoDiagAutorizacion.getDiagnostico().setNombre(rs.getString("nombre"));
				dtoDiagAutorizacion.getDiagnostico().setPrincipal(UtilidadTexto.getBoolean(rs.getString("principal")));
				dtoDiagAutorizacion.setFechaModifica(rs.getString("fecha_modifica"));
				dtoDiagAutorizacion.setHoraModifica(rs.getString("hora_modifica"));
				dtoDiagAutorizacion.getUsuarioModifica().cargarUsuarioBasico(con,rs.getString("usuario_modifica"));
				
				array.add(dtoDiagAutorizacion);
			}
			
			rs.close();
			DiagAutorizaciones.close();
		}catch(SQLException e){
			logger.info("Error Consultando los Diagnosticos de la Solicitud de Autorizacion !!!!!!\n La Consulta: "+strConsultarDiagnosticoAutorizaciones);
			logger.error(e);			
			return null;
		}
		return array;
	}
	
	
	/**
	 * Consulta la informacion de la respuesta
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static DtoRespAutorizacion cargarRespuestaAutorizacion(Connection con,HashMap parametros)
	{
		DtoRespAutorizacion dto = new DtoRespAutorizacion();
		String cadena = strConsultarRespuestasAutorizacion;
		
		if(parametros.containsKey("codigoDetalle") && 
			!parametros.get("codigoDetalle").equals(""))		
			cadena += " AND det_autorizacion = "+parametros.get("codigoDetalle").toString();			
		
		try
		{
			PreparedStatementDecorator p =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(p.executeQuery());
			
			if(rs.next())
			{
				dto.setDetAutorizacion(rs.getString("det_autorizacion"));
				dto.setVigencia(rs.getString("vigencia"));
				dto.setTipoVigencia(new InfoDatosInt(rs.getInt("tipo_vigencia"),rs.getString("tipo_dia")));
				dto.setNombreTipoVigencia(rs.getString("nombre_tipo_vigencia"));
				
				dto.setNumeroAutorizacion(rs.getString("numero_autorizacion"));
				dto.setPersonaAutoriza(rs.getString("persona_autoriza"));
				dto.setValorCobertura(rs.getString("valor_cobertura"));
				dto.setTipoCobertura(rs.getString("tipo_cobertura"));
				dto.setValorPagoPaciente(rs.getString("valor_pago_paciente"));
				dto.setTipoPagoPaciente(rs.getString("tipo_pago_paciente"));
				dto.setPersonaRecibe(rs.getString("persona_recibe"));
				dto.setCargoPersRecibe(rs.getString("cargo_pers_recibe"));
				dto.setNombreCargoPersRecibe(rs.getString("nombre_cargo_pers_recibe"));
				dto.setPersonaRegistro(rs.getString("persona_registro"));
				dto.setFechaRegistro(rs.getString("fecha_registro"));
				dto.setHoraRegistro(rs.getString("hora_registro"));
				dto.setFechaAutorizacion(rs.getString("fecha_autorizacion"));
				dto.setHoraAutorizacion(rs.getString("hora_autorizacion"));
				dto.setFechaAnulacion(rs.getString("fecha_anulacion"));	
				dto.setHoraAnulacion(rs.getString("hora_anulacion"));
				
				UsuarioBasico usuario = new UsuarioBasico();
				if(rs.getString("usuario_anulacion").equals(""))
					dto.setUsuarioAnulacion(new UsuarioBasico());
				else
				{
					usuario.cargarUsuarioBasico(rs.getString("usuario_anulacion"));
					dto.setUsuarioAnulacion(usuario);
				}
				
				dto.setMotivoAnulacion(rs.getString("motivo_anulacion"));
				dto.setCantidadSolicitada(rs.getString("cantidad_solicitada"));
				dto.setCantidadAutorizada(rs.getString("cantidad_autorizada"));
				dto.setFechaInicialAutorizada(rs.getString("fecha_ini_autorizada"));
				dto.setFechaFinalAutorizada(rs.getString("fecha_fin_autorizada"));
				dto.setNombrePersonaRegistro(rs.getString("nombre_perso_registro"));
				dto.setCargoPerRegistra(rs.getString("cargo_pers_registro"));				 
				dto.setObservacion(rs.getString("observaciones_autorizacion"));
				dto.setNombreCargoPersRecibe(rs.getString("nom_cargo_per_rec"));
				dto.setNombreCargoPersRegistro(rs.getString("nom_cargo_per_reg"));
			}
			
			logger.info("..:Buscando Respuesta >> parametros "+parametros+" >> codigo pk >> "+dto.getDetAutorizacion());
		}
		catch(SQLException e)
		{
			logger.info("Error Consultando los Archivos Respuesta Autorizacion !!!!!!\n La Consulta: "+cadena);
			logger.error(e);			
			return null;
		}
		
		return dto;		
	}
	
	//*******************************************************************************************************
	
	/**
	 * Consultar los Archivos Adjuntos de una Solicitd de Autorizacion
	 */
	public static ArrayList<DtoAdjAutorizacion> caragarAdjAutorizacion(Connection con, HashMap parametros) 
	{
		ArrayList<DtoAdjAutorizacion> AdjAuto = new ArrayList<DtoAdjAutorizacion>();
		
		try
		{
			PreparedStatementDecorator DiagAutorizaciones =  new PreparedStatementDecorator(con.prepareStatement(strConsultarAdjuntoAutorizaciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			DiagAutorizaciones.setInt(1,Utilidades.convertirAEntero(parametros.get("autorizacion").toString()));
			ResultSetDecorator rs = new ResultSetDecorator(DiagAutorizaciones.executeQuery());
			while(rs.next())
			{	
				DtoAdjAutorizacion dtoAdjAutorizacion = new DtoAdjAutorizacion();
				dtoAdjAutorizacion.setCodigoPK(rs.getString("codigo_pk"));
				dtoAdjAutorizacion.setAutorizacion(rs.getString("autorizacion"));
				dtoAdjAutorizacion.setNombreArchivo(rs.getString("nombre_archivo"));
				dtoAdjAutorizacion.setNombreOriginal(rs.getString("nombre_original"));
				dtoAdjAutorizacion.setFechaModifica(rs.getString("fecha_modifica"));
				dtoAdjAutorizacion.setHoraModifica(rs.getString("hora_modifica"));
				dtoAdjAutorizacion.getUsuarioModifica().cargarUsuarioBasico(con,rs.getString("usuario_modifica"));
				
				AdjAuto.add(dtoAdjAutorizacion);				
			}			
			
			rs.close();
			DiagAutorizaciones.close();
		}catch(SQLException e){
			logger.info("Error Consultando los Archivos Adjuntos de la Solicitud de Autorizacion !!!!!!\n La Consulta: "+strConsultarAdjuntoAutorizaciones);
			logger.error(e);			
			return null;
		}
		return AdjAuto;
	}
	
	/**
	 * Listar las Persona que hacen la Solicitd de una Servicio/Articulo
	 */
	public static ArrayList<HashMap<String,Object>> getListadoPersonaSolicita(Connection con, HashMap parametros) 
	{
		
		ArrayList<HashMap<String,Object>> arrayPerSol = new ArrayList<HashMap<String,Object>>();
		if(parametros.get("indorden").equals("1"))
			strListarPerSolicita += "WHERE numero_solicitud IN ("+parametros.get("orden").toString()+") "+
									"AND s.codigo_medico IS NOT NULL";
		else
			if(parametros.get("indorden").equals("2"))
				strListarPerSolicita += "INNER JOIN det_cargos det ON ( " +
										"det.codigo_detalle_cargo IN ("+parametros.get("orden").toString()+")) "+
										"WHERE s.numero_solicitud = det.solicitud "+ 
										"AND s.codigo_medico IS NOT NULL";
		try{
			PreparedStatementDecorator ListadoPerSol =  new PreparedStatementDecorator(con.prepareStatement(strListarPerSolicita,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(ListadoPerSol.executeQuery());
			
			do{
				HashMap<String,Object> persol = new HashMap<String,Object>();
				persol.put("codigo",rs.getObject("codigo_medico"));
				persol.put("nombre",rs.getObject("nombre_persona"));
				arrayPerSol.add(persol);
			}while(rs.next());
			
			rs.close();
			ListadoPerSol.close();
		}catch(SQLException e){
			logger.info("Error Consultando las Personas que hacen la Solicitud de un Servicio/Articulo !!!!!!\n La Consulta: "+strListarPerSolicita);
			logger.error(e);			
			return null;
		}
		return arrayPerSol;
	}
	
	/**
	 * Modificar Tipo de Servicio Solicitado de la solicitd de Autorizacion
	 */
	public static int updateTipoSerSolAutorizacion(Connection con, HashMap parametros) 
	{
		String strUpdateTipoSerSolAutorizacion = "";
		int resultado = 0;
		try{
			strUpdateTipoSerSolAutorizacion="UPDATE autorizaciones as auto" +
					" SET auto.tipo_servicio_solicitado = "+parametros.get("tipo_solicitud").toString()+
					" ,auto.fecha_modifica = "+Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))+
					" ,auto.hora_modifica = "+UtilidadFecha.getHoraActual()+
					" ,auto.usuario_modifica = "+parametros.get("usuario_modifica").toString()+
					" WHERE auto.codigo = "+parametros.get("codigo").toString();
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultado= st.executeUpdate(strUpdateTipoSerSolAutorizacion);
			if(resultado>0){
				st.close();
				return resultado;
			}
		}catch(SQLException e){
			logger.info("Error en la Modificacion del Tipo de Servicio Solicitado !!!!!!!!!\n Query: "+strUpdateTipoSerSolAutorizacion);
			logger.error(e);
			return 0;
		}
		return 0;
	}
	
	/**
	 * Modifica Observacion de la Solicitd de Autorizacion
	 */
	public static int updateObservacionAutorizacion(Connection con, HashMap parametros) 
	{
		String strUpdateObservacionAutorizacion = "";
		int resultado = 0;
		try{
			strUpdateObservacionAutorizacion="UPDATE autorizaciones " +
					" SET observaciones = '"+parametros.get("observaciones").toString()+"' "+
					" ,fecha_modifica = '"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"' "+
					" ,hora_modifica = '"+UtilidadFecha.getHoraActual()+"' "+
					" ,usuario_modifica = '"+parametros.get("usuario_modifica").toString()+"' "+
					" WHERE codigo = "+parametros.get("codigo").toString();
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			resultado= st.executeUpdate(strUpdateObservacionAutorizacion);
			if(resultado>0){
				st.close();
				return resultado;
			}
		}catch(SQLException e){
			logger.info("Error en la Modificacion de la Observacion de la Solicitud de Autorizacion !!!!!!!!!\n Query: "+strUpdateObservacionAutorizacion);
			logger.error(e);
			return 0;
		}
		return 0;
	}
	
	/**
	 * Modificar Autorizacion del Detalle de Autorizacion
	 */
	public static int updateAutorizacion(Connection con, HashMap parametros) 
	{
		String strUpdateAutorizacion = "";
		int resultado = 0;
		try{
			strUpdateAutorizacion="UPDATE det_autorizaciones as det" +
					" SET det.autorizacion = "+parametros.get("autorizacion").toString()+
					" ,det.fecha_modifica = "+Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))+
					" ,det.hora_modifica = "+UtilidadFecha.getHoraActual()+
					" ,det.usuario_modifica = "+parametros.get("usuario_modifica").toString()+
					" WHERE det.codigo = "+parametros.get("codigo_det").toString();
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultado= st.executeUpdate(strUpdateAutorizacion);
			if( resultado>0){
				st.close();
				return resultado;
			}
		}catch(SQLException e){
			logger.info("Error en la Modificacion de la Solicitud de Atorizacion del Detalle de la Atorizacion !!!!!!!!!\n Query: "+strUpdateAutorizacion);
			logger.error(e);
			return 0;
		}
		return 0;	
	}
	
	/**
	 * Modificar El Diagnostico Principal de la Solicitud de Autorizacion
	 */
	public static int updateDiagPPAutorizacion(Connection con, HashMap parametros) 
	{
		String strUpdateDiagPPAutorizacion = "";
		int resultado = 0;
		try{
			strUpdateDiagPPAutorizacion="UPDATE diag_autorizaciones as diag" +
					" SET adj.acronimo = "+parametros.get("acronimo").toString()+
					" ,diag.tipo_cie = "+parametros.get("tipo_cie").toString()+
					" ,diag.principal = "+parametros.get("principal").toString()+
					" ,diag.fecha_modifica = "+Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))+
					" ,diag.hora_modifica = "+UtilidadFecha.getHoraActual()+
					" ,diag.usuario_modifica = "+parametros.get("usuario_modifica").toString()+
					" WHERE diag.codigo = "+parametros.get("codigo").toString();
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultado= st.executeUpdate(strUpdateDiagPPAutorizacion);
			if(resultado>0){
				st.close();
				return resultado;
			}
		}catch(SQLException e){
			logger.info("Error en la Modificacion del Diagnostico Principal de la Solicitud de Autorizacion !!!!!!!!!\n Query: "+strUpdateDiagPPAutorizacion);
			logger.error(e);
			return 0;
		}
		return 0;
	}
	
	/**
	 * Eliminacion del diagnostico Relacionado de la Solicitud de Autorizacion 
	 */
	public static int deleteDiagRAutorizacion(Connection con, HashMap parametros) 
	{
		int resultado = 0;
		try{
			PreparedStatementDecorator deleteDiagRAutorizacion =  new PreparedStatementDecorator(con.prepareStatement(strDeleteDiagRAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			deleteDiagRAutorizacion.setInt(1,Utilidades.convertirAEntero(parametros.get("codigo").toString()));
			resultado = deleteDiagRAutorizacion.executeUpdate(); 
			if(resultado>0){
				deleteDiagRAutorizacion.close();
				return resultado;
			}
			con.close();
		}catch(SQLException e){
			logger.info("Error en la Eliminacion del Diagnostico Relacionado de la Solicitud de Autorizacion !!!!!!!!!\n Query: "+strDeleteDiagRAutorizacion);
			logger.error(e);
			return 0;
		}
		return 0;	
	}
	
	/**
	 * Eliminacion de Archivos Adjuntos a la Solicitud de Autorizacion
	 */
	public static int deleteArchivoAdjAutorizacion(Connection con, HashMap parametros)
	{		
		int resultado = 0;
		try{
			PreparedStatementDecorator deleteArchivoAdjAutorizacion =  new PreparedStatementDecorator(con.prepareStatement(strDeleteArchivoAdjAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			deleteArchivoAdjAutorizacion.setInt(1,Utilidades.convertirAEntero(parametros.get("codigo_pk").toString()));
			resultado = deleteArchivoAdjAutorizacion.executeUpdate(); 
			if(resultado>0){
				deleteArchivoAdjAutorizacion.close();
				return resultado;
			}
		}catch(SQLException e){
			logger.info("Error en la Eliminacion del Archivo Adjunto a la Solicitud de Autorizacion !!!!!!!!!\n Query: "+strDeleteArchivoAdjAutorizacion);
			logger.error(e);
			return 0;
		}
		return 0;
	}
	
	/**
	 * Metodo encargado de ingresar el log cuando se realiza una modificacion
	 * de la autorizacion 
	 * @param connection
	 * @param datos
	 * ----------------------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------------------
	 *  Solicitud --> Requerido pero excluyente con Solicitud orden_ambulatoria
	 *  orden_ambulatoria --> Requerido pero excluyente con Solicitud
	 *  servicioArticulo --> Requerido
	 *  autorizacionAnt --> Requerido
	 *  autorizacion --> Requerido
	 *  subCuenta --> 
	 *  institucion --> Requerido
	 *  usuarioModifica --> Requerido
	 *  asocio --> Opcional
	 *  @return int
	 */
	public static int ingresarLog(Connection connection,HashMap datos)
	{

		logger.info("\n entro a ingresarLog datos -->"+datos);
		logger.info("\n cadena -->"+ingresarLog);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(ingresarLog, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(connection, "manejopaciente.seq_log_actualiz_autorizac");
			//consecutivo
			ps.setInt(1, consecutivo);
			
			//Solicitud (Servicio/Articlo) o Ambulatoria
			if (Utilidades.convertirAEntero(datos.get("Solicitud")+"")>=0){
				ps.setNull(12, Types.INTEGER);
				ps.setInt(2, Utilidades.convertirAEntero(datos.get("Solicitud")+""));
			}else{
				ps.setNull(2, Types.INTEGER);
				ps.setInt(12, Utilidades.convertirAEntero(datos.get("orden_ambulatoria")+""));
			}
			//servicio_articulo
			 if (UtilidadCadena.noEsVacio(datos.get("servicioArticulo")+""))
				 ps.setString(3, datos.get("servicioArticulo")+"");
			 else
				 ps.setNull(3, Types.VARCHAR);
			 
			//autorizacion_ant
			 if (UtilidadCadena.noEsVacio(datos.get("autorizacionAnt")+""))
				 ps.setString(4, datos.get("autorizacionAnt")+"");
			 else
				 ps.setNull(4, Types.VARCHAR);
			 
			//autorizacion
			ps.setString(5, datos.get("autorizacion")+"");
			
			//sub_cuenta 
			if (Utilidades.convertirAEntero(datos.get("subCuenta")+"")>=0)
				ps.setInt(6, Utilidades.convertirAEntero(datos.get("subCuenta")+""));
			else
				ps.setNull(6, Types.INTEGER);
			
			//institucion
			ps.setInt(7, Utilidades.convertirAEntero(datos.get("institucion")+""));
			
			//fecha_modifica
			ps.setDate(8, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			
			//hora_modifica
			ps.setString(9, UtilidadFecha.getHoraActual());
			
			//usuario_modifica
			ps.setString(10, datos.get("usuarioModifica")+"");
			
			//asocio
			if (UtilidadCadena.noEsVacio(datos.get("asocio")+""))
				ps.setString(11, datos.get("asocio")+"");
			else
				ps.setNull(11, Types.VARCHAR);
			
			if(ps.executeUpdate()>0){
				ps.close();
				return consecutivo;
			}
		} 
		catch (SQLException e){
			logger.info("\n problema insertando el log en la tabla  log_actualiz_autorizaciones "+e);
		}
		return 0;
	}
	
	//*********************************************************************************************
	
	/**
	 * Metodo encargado de actualizar el numero de autorizacion 
	 * de un servicio o articulo en la tabla det_cargos
	 * @author Jhony Alexander Duque.
	 * @param connection
	 * @param datos
	 * --------------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------------
	 * -- numeroAutorizacion --> Requerido
	 * -- codigoDetalleCargo --> Requerido
	 * @return true/false
	 */
	public static int actualizarAutorizacionServicioArticulo (Connection connection, HashMap datos)
	{		
		int resultado = 0;		
	
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(actualizarAutorizacionServicioArticulo, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			//numero autorizacion
			if(!datos.get("numeroAutorizacion").equals(""))				
				ps.setString(1, datos.get("numeroAutorizacion")+"");
			else
				ps.setNull(1,Types.VARCHAR);
			
			//codigo_detalle_cargo
			ps.setDouble(2, Utilidades.convertirAEntero(datos.get("codigoDetalleCargo")+""));
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return 1;
			}
			
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.info("\n problema actualizando el numero de autorizacion de un servicio o articulo en la tabla det_cargos "+e+" "+datos);
		}
		return resultado;
	}

	//*********************************************************************************************
		
	
	/**
	 * Modificar El Estado del Detalle de la Autorizacion
	 * @param Connection con
	 * @param HashMap parametros 
	 */
	public static int updateAnulacionDetAutorizacion(Connection con, HashMap parametros) 
	{			
		try
		{
			logger.info("parametros >> "+parametros);
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strActualizarMotivoAnulacion, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			ps.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaAnulacion")+"")));			
			ps.setString(2,parametros.get("horaAnulacion")+"");
			ps.setString(3,parametros.get("usuarioAnulacion")+"");
			ps.setString(4,parametros.get("motivoAnulacion")+"");
			ps.setInt(5,Utilidades.convertirAEntero(parametros.get("codigoPkDetalle")+""));
			
			if(ps.executeUpdate()>0)
			{
				parametros.put("usuario_modifica",parametros.get("usuarioAnulacion").toString());
				parametros.put("codigo",parametros.get("codigoPkDetalle").toString());				
				
				if(updateEstadoDetAutorizacion(con, parametros) > 0)
					return 1;
			}				
			
			ps.close();	
		}
		catch(SQLException e){
			logger.info("Error en updateAnulacionDetAutorizacion: "+strActualizarMotivoAnulacion+" "+parametros);
			logger.error(e);
			return 0;
		}
		
		return 0;
	}
	
	//*********************************************************************************************
	
	/**
	 * Modificar la cobertura en salud de la subcuenta
	 * @param Connection con
	 * @param HashMap parametros 
	 */
	public static int updateCoberturaSaludSubCuenta(Connection con, HashMap parametros) 
	{			
		try
		{						
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarCoberturaSalud, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			if(parametros.containsKey("tipoCobertura") && 
					!parametros.get("tipoCobertura").toString().equals(""))
				ps.setInt(1,Utilidades.convertirAEntero(parametros.get("tipoCobertura").toString()));
			else
				ps.setNull(1,Types.INTEGER);
					
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("subCuenta").toString()));
			
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return 1;
			}
			
			ps.close();
				
		}
		catch(SQLException e){
			logger.info("Error en updateCoberturaSaludSubCuenta: "+actualizarCoberturaSalud+" "+parametros);
			logger.error(e);
			return 0;
		}
		
		return 0;
	}
	
	//*********************************************************************************************
	
	/**
	 * Modificar El Estado del Detalle de la Autorizacion
	 */
	public static int updateEstadoDetAutorizacion(Connection con, HashMap parametros) 
	{
		String strUpdateEstadoDetAutorizacion = "";
		int resultado = 0;
		try{
			strUpdateEstadoDetAutorizacion="UPDATE det_autorizaciones " +
					" SET estado = '"+parametros.get("estado").toString()+"' "+
					" ,fecha_modifica = '"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"' "+
					" ,hora_modifica = '"+UtilidadFecha.getHoraActual()+"' "+
					" ,usuario_modifica = '"+parametros.get("usuario_modifica").toString()+"' "+
					" WHERE codigo = "+parametros.get("codigo").toString();
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultado= st.executeUpdate(strUpdateEstadoDetAutorizacion);
			if(resultado>0){
				st.close();
				return resultado;
			}
		}catch(SQLException e){
			logger.info("Error en la Modificacion del Estado del Detalle de la Autorizacion !!!!!!!!!\n Query: "+strUpdateEstadoDetAutorizacion);
			logger.error(e);
			return 0;
		}
		return 0;
	}
	
	//*********************************************************************************************
	
	
	/**
	 * Modificar el Atributo activo del Detalle de la Autorizacion
	 */
	public static int updateActivoDetAutorizacion(Connection con, HashMap parametros) 
	{
		String strUpdateActivoDetAutorizacion = "";
		int resultado = 0;
		try{
			strUpdateActivoDetAutorizacion="UPDATE det_autorizaciones " +
					" SET activo = '"+parametros.get("activo").toString()+"' "+
					" ,fecha_modifica = '"+Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))+"' "+
					" ,hora_modifica = '"+UtilidadFecha.getHoraActual()+"' "+
					" ,usuario_modifica = '"+parametros.get("usuario_modifica").toString()+"' "+
					" WHERE codigo = "+parametros.get("codigo").toString();
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultado= st.executeUpdate(strUpdateActivoDetAutorizacion);
			if(resultado>0){
				st.close();
				return 1;
			}
		}catch(SQLException e){
			logger.info("Error en la Modificacion del Atributo Activo del Detalle de la Autorizacion !!!!!!!!!\n Query: "+strUpdateActivoDetAutorizacion);
			logger.error(e);
			return 0;
		}
		return 0;
	}
	
	//*********************************************************************************************
	
	/**
	 * Método para obtener el origen de atencion verificando
	 * la informacion de la cuenta del paciente
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public static InfoDatosInt obtenerOrigenAtencionXCuenta(Connection con,HashMap parametros)
	{				
		String idCuenta = parametros.get("idCuenta").toString();
		int viaIngreso = Utilidades.convertirAEntero(parametros.get("viaIngreso").toString());
		String tipoEvento = parametros.get("tipoEvento").toString();
		
		InfoDatosInt origenAtencion = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		try
		{
			switch(viaIngreso)
			{
				case ConstantesBD.codigoViaIngresoAmbulatorios:
					//Si hay tipo evento se cnosulta su respectiva causa externa
					if(UtilidadCadena.noEsVacio(tipoEvento))
					{
						PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(obtenerCausaExternaXEventoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setString(1,tipoEvento);
						ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
						if(rs.next())
						{
							origenAtencion.setCodigo(rs.getInt("codigo"));
							origenAtencion.setNombre(rs.getString("nombre"));
						}
					}
				break;
				case ConstantesBD.codigoViaIngresoHospitalizacion:
				case ConstantesBD.codigoViaIngresoUrgencias:
					PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(obtenerCausaExternaXCuentaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(idCuenta));
					if(viaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
					{
						pst.setInt(2,ConstantesBD.codigoTipoSolicitudInicialUrgencias);
					}
					else
					{
						pst.setInt(2,ConstantesBD.codigoTipoSolicitudInicialHospitalizacion);
					}
					ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						origenAtencion.setCodigo(rs.getInt("codigo"));
						origenAtencion.setNombre(rs.getString("nombre"));
					}
				break;
				case ConstantesBD.codigoViaIngresoConsultaExterna:
					pst =  new PreparedStatementDecorator(con.prepareStatement(obtenerCausaExternaXCuentaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(idCuenta));
					pst.setInt(2,ConstantesBD.codigoTipoSolicitudCita);
					
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						origenAtencion.setCodigo(rs.getInt("codigo"));
						origenAtencion.setNombre(rs.getString("nombre"));
					}
					
					//Si aun no se ha encontrado causa externa pero hay tipo evento en la cuenta, entonces se prosigue
					//a consultar por el tipo evento
					if(origenAtencion.getCodigo()<=0&&UtilidadCadena.noEsVacio(tipoEvento))
					{
						pst =  new PreparedStatementDecorator(con.prepareStatement(obtenerCausaExternaXEventoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setString(1,tipoEvento);
						rs = new ResultSetDecorator(pst.executeQuery());
						if(rs.next())
						{
							origenAtencion.setCodigo(rs.getInt("codigo"));
							origenAtencion.setNombre(rs.getString("nombre"));
						}
					}
				break;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerOrigenAtencionXCuenta: "+e);
		}
		return origenAtencion;
	}
	
	//*********************************************************************************************
	
	
	/**
	 * Actualizar Autorizacion Detalle de Autorizacion y detalle cargos
	 */
	public static int actualizarAutorizacionyDetalle (Connection con, HashMap parametros)
	{
	 
		HashMap datosmodificables=parametros;
		String cadenaActualizacionAutorizacion= actualizarAutorizacionSbCuentaConvenioyCuenta;
		String cadenaAcutualizacionDetalle = actualizarDetAutorizacionSolicitudyCargo;
		String cadenaConsultaDetAutorizaciones = strConsultarDetalleAutorizaciones2;
		String cadenaConsultaEnvioAutorizaciones = strConsultarEnvioAutorizaciones;
		String cadenaActulizardetCargo=actualizarAutorizacionServicioArticulo;
		String numRespuestas="SELECT * FROM resp_autorizaciones WHERE det_autorizacion = ?";
		
		int ordenAmbulatoria=Utilidades.convertirAEntero(datosmodificables.get("codordenambulatoria").toString());
		logger.info("Esta en el Sql Autorizacion >> orden Ambulatoria >> "+datosmodificables.get("codordenambulatoria").toString());
		
		if(datosmodificables.get("articulo").toString().equals(""))
		{
			cadenaConsultaDetAutorizaciones=cadenaConsultaDetAutorizaciones.replace("WHERE 1=1", "WHERE da.orden_ambulatoria = "+ordenAmbulatoria+" ");
		}
		else
		{
			cadenaConsultaDetAutorizaciones=cadenaConsultaDetAutorizaciones.replace("WHERE 1=1", " WHERE da.orden_ambulatoria = "+ordenAmbulatoria+" AND da.articulo = "+datosmodificables.get("articulo").toString()+" ");
		}
		
		try{
		   	PreparedStatementDecorator consultaDetAutorizacion= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetAutorizaciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		   	ResultSetDecorator detalleAutorizacion=new ResultSetDecorator(consultaDetAutorizacion.executeQuery());
		   	
		   	while (detalleAutorizacion.next())
		   	{   
		   	    try{
				     PreparedStatementDecorator ps2= new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				     ps2.setInt(1,Utilidades.convertirAEntero(datosmodificables.get("subcuenta").toString()));
				     ps2.setInt(2, Utilidades.convertirAEntero(datosmodificables.get("convenio").toString()));
				     ps2.setInt(3, Utilidades.convertirAEntero(datosmodificables.get("cuenta").toString()));
				     ps2.setInt(4, Utilidades.convertirAEntero(detalleAutorizacion.getString("autorizacion")));
			   	
			         if(ps2.executeUpdate()>0)
			         {
			        	 try{
						   		PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadenaAcutualizacionDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							   	ps.setInt(1, Utilidades.convertirAEntero(datosmodificables.get("numsolicitud").toString()));
							   	ps.setDouble(2, Utilidades.convertirADouble(datosmodificables.get("detcargo").toString()));
							   	ps.setInt(3,ordenAmbulatoria );
							   	
							   	 if(ps.executeUpdate()>0)
							   	    {
								   		try{
									   		PreparedStatementDecorator existeRespuestas= new PreparedStatementDecorator(con.prepareStatement(numRespuestas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
									   		existeRespuestas.setInt(1, Utilidades.convertirAEntero(detalleAutorizacion.getString("codigo")));
										   	ResultSetDecorator respuesta=new ResultSetDecorator(existeRespuestas.executeQuery());
										   	   
										   	   if(respuesta.next())
										   	   {
											   		try{
												   		PreparedStatementDecorator actualizardetCargos= new PreparedStatementDecorator(con.prepareStatement(cadenaActulizardetCargo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
												   		actualizardetCargos.setInt(1, Utilidades.convertirAEntero(detalleAutorizacion.getString("autorizacion")));
												   		actualizardetCargos.setDouble(2, Utilidades.convertirADouble(datosmodificables.get("detcargo").toString()));
													   	actualizardetCargos.executeUpdate();
											   		}
										   		    catch(SQLException e){
														logger.info("Error en la actualizacion detalle cargos del metodo acutualizarAutorizacionyDetalle  >>> Cadena: "+cadenaActulizardetCargo);
														logger.error(e);
														return 0;
													}
										   		
										   	   }
										   	
								   		    }
								   		    catch(SQLException e){
												logger.info("Error en la busqueda Detalle Cargo (busqueda respuestas autorizaciones  del metodo acutualizarAutorizacionyDetalle  >>> Cadena: "+numRespuestas);
												logger.error(e);
												return 0;
											}
							   	    }
						   		}
						   		catch(SQLException e){
									logger.info("Error en la actualizacion Detalle Autorizacion del metodo acutualizarAutorizacionyDetalle  >>> Cadena: "+cadenaAcutualizacionDetalle);
									logger.error(e);
									return 0;
								}
			        	 
			        	 
			            }
			        	 
			        	 
		   	   	    }catch(SQLException e){
		   	   		 logger.info("Error en actualizar Autorizacion del metodo acutualizarAutorizacionyDetalle >>> Cadena "+cadenaActualizacionAutorizacion);
		   	   		 logger.error(e);
		   	   		return 0;
		   	       }
		   		
		   	   
		   	}
		   	
		}catch(SQLException e){
			logger.info("Error en Consulta Detalle Autorizacion del metodo acutualizarAutorizacionyDetalle  !!!!!!!!!" + cadenaConsultaDetAutorizaciones);
			logger.error(e);
			return 0;
		}
		
		return 1;
	}
	
	//*********************************************************************************************
	
	/**
	 * Método para obtener la persona que hace la solicitud 
	 * @param con
	 * @param HashMap parametros
	 * @return
	 */
	public static HashMap<String, Object> obtenerPersonaSolicitad(Connection con,HashMap parametros)
	{				
		HashMap<String, Object> personaSolicita = new HashMap<String, Object>();
		personaSolicita.put("codigo_persona_sol", ConstantesBD.codigoNuncaValido);
		String tipoOrdenInd = parametros.get("tipoOrdenInd").toString();
		if(tipoOrdenInd.equals(Autorizaciones.codInternoAutoSolicSerMed)) // Servicio/Medicamento
		{
			String consulta = obtenerPersonaSolicitaStr;
			consulta += " INNER JOIN solicitudes s ON (";
			if(Utilidades.convertirAEntero(parametros.get("tipo_solicitud").toString())==ConstantesBD.codigoTipoSolicitudEstancia){
				// tipo se solicitud estancia
				consulta += " p.codigo IN (SELECT ah.codigo_medico from admisiones_hospi WHERE ah.cuenta = s.cuenta) AND "; 
			}else{
				if(Utilidades.convertirAEntero(parametros.get("tipo_solicitud").toString())==ConstantesBD.codigoTipoSolicitudCargosDirectosServicios
						|| Utilidades.convertirAEntero(parametros.get("tipo_solicitud").toString())==ConstantesBD.codigoTipoSolicitudInicialUrgencias
						|| Utilidades.convertirAEntero(parametros.get("tipo_solicitud").toString())==ConstantesBD.codigoTipoSolicitudInicialHospitalizacion
						|| Utilidades.convertirAEntero(parametros.get("tipo_solicitud").toString())==ConstantesBD.codigoTipoSolicitudCita){
					// tipo de solicitud cargo directo de servicio, valor inicial de urgencias y hospitalizacion y consulta
					consulta += " s.codigo_medico_responde = p.codigo AND ";
				}else{
					if(Utilidades.convertirAEntero(parametros.get("tipo_solicitud").toString())==ConstantesBD.codigoTipoSolicitudCirugia
							||Utilidades.convertirAEntero(parametros.get("tipo_solicitud").toString())==ConstantesBD.codigoTipoSolicitudEvolucion
							||Utilidades.convertirAEntero(parametros.get("tipo_solicitud").toString())==ConstantesBD.codigoTipoSolicitudProcedimiento
							||Utilidades.convertirAEntero(parametros.get("tipo_solicitud").toString())==ConstantesBD.codigoTipoSolicitudMedicamentos
							||Utilidades.convertirAEntero(parametros.get("tipo_solicitud").toString())==ConstantesBD.codigoTipoSolicitudInterconsulta){
						consulta += " s.codigo_medico = p.codigo AND ";
					}else{
						if(Utilidades.convertirAEntero(parametros.get("tipo_solicitud").toString())==ConstantesBD.codigoTipoSolicitudPaquetes){
							//tipo de solicitud de orden ambulatoria
							String cadenaConsultaAmbulatoria = "SELECT "+
								"numero_solicitud AS solicitud , " +
								"gettiposolicitud(numero_solicitud) AS tipo_solicitud " +
								"FROM detalle_paquetizacion  dp " +
								"INNER JOIN paquetizacion p ON( " +
								"p.numero_solicitud_paquete = ? " +
								"AND dp.codigo_paquetizacion = p.codigo ) "+
								"WHERE dp.principal = '"+ConstantesBD.acronimoSi+"' ";
							HashMap parametros2 = new HashMap(); 
							try{
								PreparedStatementDecorator pstOrdAmb = new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaAmbulatoria, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
								pstOrdAmb.setInt(1,Utilidades.convertirAEntero(parametros.get("numero_solicitud").toString()));
								ResultSetDecorator rs = new ResultSetDecorator(pstOrdAmb.executeQuery());
								if(rs.next()){
									parametros2.put("numero_solicitud", rs.getString("solicitud"));
									parametros2.put("tipo_solicitud", rs.getString("tipo_solicitud"));
									return obtenerPersonaSolicitad(con,parametros2);
							}
							logger.info("la Cosulta >>>>>>>>>>>>>>>>>>>>>> "+consulta);
							}catch(SQLException e){
								logger.info("EROR en la Consulta >>>>>>>>>>>>>> "+cadenaConsultaAmbulatoria);
								return parametros2;
							}
							
						}
					}
					
					
				}
			}
			consulta += " s.numero_solicitud = ? AND s.tipo = ? )";
			try{
				PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Utilidades.convertirAEntero(parametros.get("numero_solicitud").toString()));
				pst.setInt(1,Utilidades.convertirAEntero(parametros.get("tipo_solicitud").toString()));
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
					personaSolicita.put("codigo_persona_sol", rs.getString("codigo"));
				
				pst.close();
				rs.close();
				logger.info("la Cosulta >>>>>>>>>>>>>>>>>>>>>> "+consulta);
			}catch(SQLException e){
				logger.info(" ERROR en la Consulta >>>>>>>>>>>>>>>>>>>>>>> "+consulta);
				personaSolicita.put("codigo_persona_sol", ConstantesBD.codigoNuncaValido);
			}
		}else{
			if(tipoOrdenInd.equals(Autorizaciones.codInternoAutoSolicAmbula)) // ambulatorio
			{
				try{
					PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(obtenerPersonaSolicitaAmbulatorioStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(parametros.get("numero_solicitud").toString()));
					ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
					
					if(rs.next())
					{
						if(rs.getInt("numero_solicitud")>0 && !rs.getString("tipo_solicitud").equals(""))
						{
							HashMap parametros2 = new HashMap(); 
							parametros2.put("numero_solicitud", rs.getString("numero_solicitud"));
							parametros2.put("tipo_solicitud", rs.getString("tipo_solicitud"));
							parametros2.put("tipoOrdenInd",Autorizaciones.codInternoAutoSolicSerMed);
							
							return obtenerPersonaSolicitad(con,parametros2);
						}
						else if(rs.getInt("codigo")>0)
						{
							personaSolicita.put("codigo_persona_sol", rs.getString("codigo"));
						}
					}
					
					pst.close();
					rs.close();
					
					logger.info("la Cosulta >>>>>>>>>>>>>>>>>>>>>> "+obtenerPersonaSolicitaAmbulatorioStr+" >> "+parametros.get("numero_solicitud").toString());
				}catch(SQLException e){
					logger.info(" ERROR en la Consulta >>>>>>>>>>>>>>>>>>>>>>> "+obtenerPersonaSolicitaAmbulatorioStr);
					personaSolicita.put("codigo_persona_sol", ConstantesBD.codigoNuncaValido);
				}
			}
		}
		return personaSolicita;
	}
	
	//*********************************************************************************************
	
	/**
	 * Insertar Archivo Adjunto de la Respusta de Autorizacion
	 */
	public static int insertarAdjuntoRespAutorizacion(Connection con, DtoAdjAutorizacion dtoAdjAutorizacion) 
	{
		try{
			PreparedStatementDecorator insertarAdjRespAutorizacion =  new PreparedStatementDecorator(con.prepareStatement(strInsertarAdjuntoRespAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo_pk = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_adj_resp_autori");
			insertarAdjRespAutorizacion.setInt(1,codigo_pk);
			insertarAdjRespAutorizacion.setInt(2,Utilidades.convertirAEntero(dtoAdjAutorizacion.getAutorizacion()));
			insertarAdjRespAutorizacion.setString(3,dtoAdjAutorizacion.getNombreArchivo());
			insertarAdjRespAutorizacion.setString(4,dtoAdjAutorizacion.getNombreOriginal());
			insertarAdjRespAutorizacion.setString(5,dtoAdjAutorizacion.getUsuarioModifica().getLoginUsuario());
			
			if(insertarAdjRespAutorizacion.executeUpdate()>0){
				insertarAdjRespAutorizacion.close();
				return codigo_pk;
			}
		}catch(SQLException e){
			logger.info("Error en el ingreso del Archivo Adjunto de la Respuesta de Autorizacion !!!!!!!!!");
			logger.error(e);
			return 0;
		}
		return 0;	
	}
	
	//*********************************************************************************************
	
	/**
	 * Modificar el Atributo Cantidad del Detalle de la Autorizacion
	 */
	public static int updateCantidadAutoDetAutorizacion(Connection con, HashMap parametros) 
	{
		String strUpdateCantidadDetAutorizacion = "";
		int resultado = 0;
		try{
			strUpdateCantidadDetAutorizacion="UPDATE det_autorizaciones " +
					" SET cantidad_autorizacion = "+parametros.get("cantidad_autorizacion").toString()+
					" ,fecha_modifica = '"+Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))+"' "+
					" ,hora_modifica = '"+UtilidadFecha.getHoraActual()+"' "+
					" ,usuario_modifica = '"+parametros.get("usuario_modifica").toString()+"' "+
					" WHERE codigo = "+parametros.get("codigo").toString();
			logger.info("consulta update >>>> "+strUpdateCantidadDetAutorizacion);
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultado= st.executeUpdate(strUpdateCantidadDetAutorizacion);
			if(resultado>0){ 
				st.close();
				return resultado;
			}
		}catch(SQLException e){
			logger.info("Error en la Modificacion del Atributo Cantidad del Detalle de la Autorizacion !!!!!!!!!\n Query: "+strUpdateCantidadDetAutorizacion);
			logger.error(e);
			return 0;
		}
		return 0;
	}
	
	//*********************************************************************************************
	
	/**
	 * Modificar el Atributo Cantidad Autorizada de la Respuesta de la Autorizacion
	 */
	public static int updateCantidadAutoRespAutorizacion(Connection con, HashMap parametros) 
	{
		String strUpdateCantidadAutoRespAutorizacion = "";
		int resultado = 0;
		try{
			strUpdateCantidadAutoRespAutorizacion="UPDATE resp_autorizaciones as resp " +
					" SET resp.cantidad_autorizada = "+parametros.get("cantidad_autorizada").toString()+
					" WHERE resp.det_autorizacion = "+parametros.get("det_autorizacion").toString();
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultado= st.executeUpdate(strUpdateCantidadAutoRespAutorizacion);
			if(resultado>0){
				st.close();
				return resultado;
			}
		}catch(SQLException e){
			logger.info("Error en la Modificacion del Atributo Cantidad Autorizada de la Respuesta de la Autorizacion !!!!!!!!!\n Query: "+strUpdateCantidadAutoRespAutorizacion);
			logger.error(e);
			return 0;
		}
		return 0;
	}
	
	//*********************************************************************************************
	
	/**
	 * Insertar Detalle Autorizacion Estancia
	 */
	public static int insertarDetAutorizacionEstancia(Connection con, DtoDetAutorizacionEst dtoDetAutorizacionEst) 
	{
		try
		{
			PreparedStatementDecorator insertarDetAutorizacionEst =  new PreparedStatementDecorator(con.prepareStatement(strInsertarDetalleAutorizacionEstancia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_det_autori_est");
			
			insertarDetAutorizacionEst.setInt(1,codigo);
			insertarDetAutorizacionEst.setInt(2,Utilidades.convertirAEntero(dtoDetAutorizacionEst.getDetAutorizacion() ));
			insertarDetAutorizacionEst.setInt(3,Utilidades.convertirAEntero(dtoDetAutorizacionEst.getNumeroSolicitud() ));
			insertarDetAutorizacionEst.setInt(4,Utilidades.convertirAEntero(dtoDetAutorizacionEst.getDetCargo()));
			insertarDetAutorizacionEst.setString(5,dtoDetAutorizacionEst.getActivo());
			 
			if(insertarDetAutorizacionEst.executeUpdate()>0){
				insertarDetAutorizacionEst.close();
				return codigo;
			}
			else			
				return ConstantesBD.codigoNuncaValido;
			
		}catch(SQLException e){
			logger.info("Error en el ingreso del Detalles de la Solicitud de Autorizacion Estancia !!!!!!!!!");
			logger.error(e);
			return 0;
		}
	}
	
	
	//***********************************************************************************
	
	
	/**
	 * consulta la informacion del convenio
	 * @param HashMap parametros
	 */
	public static HashMap consultarInfoConvenio(Connection con, HashMap parametros) 
	{		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(obtenerFormatoSolicitud,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigoSubCuenta").toString()));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());			
			HashMap resultado = UtilidadBD.cargarValueObject(rs,false,false);
			ps.close();
			rs.close();
			return resultado;
			
		}catch(SQLException e){
			logger.info("Error consultarInfoConvenio !!!!!!!!!\n Query: "+obtenerFormatoSolicitud+" "+e);			
			return new HashMap();
		}	
	}
	
	
	/**
	 * Busqueda de Estancias y Validacion
	 */
	public static ArrayList<DtoDetAutorizacionEst> cargarInfoBasicaDetAutorizacionEstancia(Connection con, HashMap parametros) 
	{
		ArrayList<DtoDetAutorizacionEst> infoBasicaDetAutoEst = new ArrayList<DtoDetAutorizacionEst>();
		String consulta = "";
		
		if(parametros.get("tipo_consulta").toString().equals(ConstantesIntegridadDominio.acronimoManual))
			consulta = strBusquedaEstyVal;
		else
			if(parametros.get("tipo_consulta").toString().equals(ConstantesIntegridadDominio.acronimoAutomatica))
				consulta = strBusquedaEstyVal2;
		
		logger.info("La Consulta >>>>>> "+consulta+" "+parametros);
		
		try
		{
			PreparedStatementDecorator cargarInfoBasicaDetAutorizacionEst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if(parametros.get("tipo_consulta").toString().equals(ConstantesIntegridadDominio.acronimoManual))
			{
				cargarInfoBasicaDetAutorizacionEst.setInt(1, Utilidades.convertirAEntero(parametros.get("servicio").toString()));
				cargarInfoBasicaDetAutorizacionEst.setInt(2, Utilidades.convertirAEntero(parametros.get("sub_cuenta").toString()));
				cargarInfoBasicaDetAutorizacionEst.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametros.get("fecha_ini").toString())));
				cargarInfoBasicaDetAutorizacionEst.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametros.get("fecha_fin").toString())));
			}
			else if(parametros.get("tipo_consulta").toString().equals(ConstantesIntegridadDominio.acronimoAutomatica))
			{
				cargarInfoBasicaDetAutorizacionEst.setInt(1, Utilidades.convertirAEntero(parametros.get("sub_cuenta").toString()));
			}
			
			ResultSetDecorator rs = new ResultSetDecorator(cargarInfoBasicaDetAutorizacionEst.executeQuery());
			
			while(rs.next())
			{
				DtoDetAutorizacionEst dto = new DtoDetAutorizacionEst();
				dto.setNumeroSolicitud(rs.getString("solicitud"));
				dto.setDetCargo(rs.getString("det_cargo"));
				dto.setConsecutivoSolicitud(rs.getString("consecutivo_sol"));
				dto.setDetAutorizacion(rs.getString("codigo_det_auto").equals("")?"":rs.getString("codigo_det_auto"));
				dto.setCodigo(rs.getString("codigo_det_auto_est").equals("")?"":rs.getString("codigo_det_auto_est"));
				
				if(!parametros.get("tipo_consulta").toString().equals(ConstantesIntegridadDominio.acronimoAutomatica))
				{
					if(rs.getString("estado_auto_preg").equals(ConstantesBD.acronimoNo)&&rs.getString("estado_auto_est_preg").equals(ConstantesBD.acronimoNo))
						dto.setPreguntar(ConstantesBD.acronimoNo);
					else
						dto.setPreguntar(ConstantesBD.acronimoSi);
				}
				else dto.setPreguntar(ConstantesBD.acronimoNo);
				infoBasicaDetAutoEst.add(dto);
			}
			
			cargarInfoBasicaDetAutorizacionEst.close();
			rs.close();
			
		}catch(SQLException e){
			logger.info("Error al Cargar la Informacion Basica de Detalles de la Solicitud de Autorizacion Estancia Por el Proceso "+parametros.get("tipo_consulta").toString()+"!!!!!!!!! Consulta >>>>>>>>>>>>> "+consulta);
			logger.error(e);
		}
		return infoBasicaDetAutoEst;
	}
	
	/**
	 * Modificar el Atributo Activo de det_autorizacion_estancia
	 */
	public static int updateActivoDetAutorizacionEst(Connection con, HashMap parametros) 
	{
		String strUpdateActivoDetAutorizacionEst = "";
		int resultado = 0;
		try{
			strUpdateActivoDetAutorizacionEst ="UPDATE det_autorizaciones_estancia " +
					" SET activo = '"+parametros.get("activo").toString()+"'"+
					" WHERE codigo = "+parametros.get("cod_det_auto_est").toString();
			PreparedStatementDecorator st = new PreparedStatementDecorator(con.prepareStatement(strUpdateActivoDetAutorizacionEst,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet ));
			resultado= st.executeUpdate(strUpdateActivoDetAutorizacionEst);
			if(resultado>0){
				st.close();
				return resultado;
			}
		}catch(SQLException e){
			logger.info("Error en la Modificacion del Atributo Activo de det_Autorizacion_estancia !!!!!!!!!\n Query: "+strUpdateActivoDetAutorizacionEst);
			logger.error(e);
			return 0;
		}
		return 0;
	}
	
	/**
	 * Consulta la Cantidad que falta por Autorizar
	 * @param HashMap parametros
	 * @return int
	 */
	public static int obtenerCantidadAutorizadaDetAuto(Connection con, HashMap parametros) 
	{		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strObtenerCantidadAutorizadaDetAuto,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("cod_det_auto").toString()));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());			
			if(rs.next())
				return rs.getInt("cantidad");
		}catch(SQLException e){
			logger.info("Error obtenerCantidadAutorizadaDetAuto !!!!!!!!!\n Query: "+strObtenerCantidadAutorizadaDetAuto+"\n"+e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	//************************************************************************************************
	
	/**
	 * Método para asociar una cuenta a autorizaciones sin cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int asociarCuentaAAutorizacionesSinCuenta(Connection con,HashMap<String,Object> campos)
	{
		try
		{
			//************SE TOMAN PARÁMETROS****************************************+
			int codigoIngreso = Utilidades.convertirAEntero(campos.get("codigoIngreso").toString());
			int codigoCuenta = Utilidades.convertirAEntero(campos.get("codigoCuenta").toString());
			//************************************************************************
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(asociarCuentaAAutorizacionesSinCuentaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoCuenta);
			pst.setInt(2,codigoIngreso);
			pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en asociarCuentaAAutorizacionesSinCuenta: "+e);
			return 0;
		}
		
		return 1;
	}
	
	
	/**
	 * Consulta Informe Tecnico de Solicitd de Autorizacion
	 */
	public static DtoAutorizacion caragarInformeTecnico(Connection con, HashMap parametros) 
	{
		DtoAutorizacion dto = new DtoAutorizacion();
		try
		{
			PreparedStatementDecorator Autorizaciones =  new PreparedStatementDecorator(con.prepareStatement(strConsultaInformeTecnico,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			Autorizaciones.setInt(1,Utilidades.convertirAEntero(parametros.get("autorizacion").toString()));
			logger.info("consulta >>>>>>>>> "+strConsultaInformeTecnico);
			logger.info("codigo autorizacion >>>>>>>> "+parametros.get("autorizacion").toString());
			ResultSetDecorator rs = new ResultSetDecorator(Autorizaciones.executeQuery());
			if(rs.next())
			{	
				dto.setCodigoPK(rs.getString("cod_auto"));
				dto.setConsecutivo(rs.getString("consecutivo"));
				dto.setAnioConsecutivo(rs.getString("anioconsecutivo"));
				dto.setFechaSolicitud(rs.getString("fecha_sol"));
				dto.setHoraSolicitud(rs.getString("hora_sol"));
				dto.setConvenioInformeTec(new InfoDatosString(rs.getString("cod_min_salud_convenio"),rs.getString("nombre_convenio")));
				dto.setNombreCobertura(rs.getString("nombre_cobertura"));
				dto.setNombreAtencio(rs.getString("nombre_atencion"));
				dto.setTipoServicioSolicitado(new InfoDatosInt(rs.getInt("tipo_ser_sol"),rs.getString("nombre_tipo_ser_sol")));
				dto.setPrioridadAtencion(rs.getString("prioridad"));
				dto.setViaIngreso(new InfoDatosInt(rs.getInt("via_ingreso"),rs.getString("nombre_via_ingreso")));
				dto.setTipoPaciente(rs.getString("tip_paciente"));
				dto.setServicioHospitalizacion(rs.getString("servicio"));
				dto.setCamaHospitalizacion(rs.getString("cama"));
				dto.setNombreUsuSol(rs.getString("nombre_usua_sol"));
				dto.setTelefonoUsuSol(rs.getString("telfono_usua_sol"));
				dto.setTelefonoCelUsuSol(rs.getString("telfono_celular_usua_sol"));
				dto.setNombreCargoUsuSol(rs.getString("nombre_cargo"));
				
				if(!dto.getCodigoPK().equals("")){ 
					HashMap datos = new HashMap(); 
					datos.put("autorizacion", dto.getCodigoPK());
					datos.put("codigoInstitucion", parametros.get("codigoInstitucion").toString());
					// cargar los diagnosticos relacionados
					dto.setDiagnosticos(cargarDiagAutorizacion(con, datos));
					// cargar los detalles de autorizacion
					dto.setDetalle(cargarDetallesAutorizacion(con, datos));
				}
			}
			rs.close();
			Autorizaciones.close();
		}catch(SQLException e){
			logger.info("Error Consultando Informe Tecnico de la Solicitud de Autorizacion !!!!!!\n La Consulta: "+strConsultaInformeTecnico);
			logger.error(e);			
			return null;
		}
		return dto;
	}
	
	//******************************************************************************************
	
	/**
	 * Captura la fecha de admision de la cuenta 
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap getFechaAdmisionCuenta(Connection con,HashMap parametros)
	{
		String cadena = "";
		HashMap resultado = new HashMap();
		resultado.put("fecha","");
		resultado.put("hora","");
		
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(strConsultarCuentasPacXIngresosXCuenta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(parametros.get("codigoSubcuenta").toString()));
			pst.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoSubcuenta").toString()));			
			pst.setInt(3,Utilidades.convertirAEntero(parametros.get("codigoIdIngreso").toString()));			
			pst.setInt(4,Utilidades.convertirAEntero(parametros.get("codigoCuenta").toString()));		
			
			ResultSetDecorator rs0 = new ResultSetDecorator(pst.executeQuery());
			
			if(rs0.next())
			{
				if(rs0.getInt("codigo_inicial_asocio") > 0 && 
						rs0.getInt("codigo_final_asocio")  < 0)
					parametros.put("via","asocios");
				else
					parametros.put("via",rs0.getString("via_ingreso"));

				if(parametros.get("via").equals(ConstantesBD.codigoViaIngresoAmbulatorios+"") 
						|| parametros.get("via").equals(ConstantesBD.codigoViaIngresoConsultaExterna+""))
					cadena = strFechaConsultaExtAmb;
				
				if(parametros.get("via").equals(ConstantesBD.codigoViaIngresoUrgencias+""))
					cadena = strFechaUrgencias;
				
				if(parametros.get("via").equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
					cadena = strFechaHospitalizacion;
				
				if(parametros.get("via").equals("asocios"))
					cadena = strFechaAsocio;		
				
				
				pst = new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Utilidades.convertirAEntero(parametros.get("codigoCuenta").toString()));			
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
				{
					resultado.put("fecha",rs.getString("fecha_admision"));
					resultado.put("hora",rs.getString("hora_admision"));
				}		
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en getFechaAdmisionCuenta: "+e+" "+cadena);
			return new HashMap();
		}
		
		logger.info("resultado A>> "+resultado+" >> "+parametros);
		return resultado;
	}
	
	//******************************************************************************************
		
	/**
	 * Consulta listado de cuentas
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public static ArrayList<DtoCuentaAutorizacion> cargarListadoCuentas(Connection con, HashMap parametros) 
	{
		String cadena = strConsultarCuentasPacXIngresos;
		
		if(parametros.containsKey("codigoCuenta") 
				&& !parametros.get("codigoCuenta").toString().equals(""))
		{
			if(parametros.containsKey("tipoAuto") 
					&& !parametros.get("tipoAuto").toString().equals(""))
			{
				cadena = cadena.replace(ConstantesBD.separadorSplit+"1"," AND cu.id = "+parametros.get("codigoCuenta"));
				cadena = cadena.replace(ConstantesBD.separadorSplit+"2"," AND cu.id = "+parametros.get("codigoCuenta")+" AND a.tipo = '"+parametros.get("tipoAuto").toString()+"' ");
			}
			else
			{
				cadena = cadena.replace(ConstantesBD.separadorSplit+"1"," AND cu.id = "+parametros.get("codigoCuenta"));
				cadena = cadena.replace(ConstantesBD.separadorSplit+"2"," AND cu.id = "+parametros.get("codigoCuenta"));
			}
		}
		else
		{
			cadena = cadena.replace(ConstantesBD.separadorSplit+"1"," ");
			cadena = cadena.replace(ConstantesBD.separadorSplit+"2"," ");
		}
			
//		logger.info("valor de la consulta >> "+parametros+" >> "+cadena);
		ArrayList<DtoCuentaAutorizacion> list = new ArrayList<DtoCuentaAutorizacion>();		
		
		try
		{
			PreparedStatementDecorator Autorizaciones =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			Autorizaciones.setInt(1,Utilidades.convertirAEntero(parametros.get("codigoSubcuenta").toString()));
			Autorizaciones.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoSubcuenta").toString()));			
			Autorizaciones.setInt(3,Utilidades.convertirAEntero(parametros.get("codigoIdIngreso").toString()));
			Autorizaciones.setInt(4,Utilidades.convertirAEntero(parametros.get("codigoSubcuenta").toString()));
			
			Autorizaciones.setInt(5,Utilidades.convertirAEntero(parametros.get("codigoSubcuenta").toString()));
			Autorizaciones.setInt(6,Utilidades.convertirAEntero(parametros.get("codigoSubcuenta").toString()));
			Autorizaciones.setInt(7,Utilidades.convertirAEntero(parametros.get("codigoSubcuenta").toString()));
			Autorizaciones.setInt(8,Utilidades.convertirAEntero(parametros.get("codigoIdIngreso").toString()));
								
			ResultSetDecorator rs = new ResultSetDecorator(Autorizaciones.executeQuery());
			
			while(rs.next())
			{				
				DtoCuentaAutorizacion dto = new DtoCuentaAutorizacion();
				dto.setIdCuenta(rs.getString("codigo_cuenta"));
				dto.setCodigoTipoPaciente(rs.getString("tipo_paciente"));
				dto.setIdIngreso(rs.getString("id_ingreso"));
				dto.setCodigoViaIngreso(rs.getString("via_ingreso"));
				dto.setEstadoCuenta(rs.getString("estado_cuenta"));
				dto.setCodigoAutorizacion(rs.getString("codigo_auto"));
				dto.setTipoAutorizacion(rs.getString("tipo_auto"));
				dto.setEstadoAutorizacion(rs.getString("estado_auto"));
				dto.setCodigoDetAutorizacion(rs.getString("codigo_det_auto"));
				dto.setCodigoConvenio(rs.getString("convenio"));
				dto.setNombreConvenio(rs.getString("nombre_convenio"));				
								
				if(rs.getInt("sub_cuenta") > 0)
					dto.setCodigoSubCuenta(rs.getString("sub_cuenta"));
				else
					dto.setCodigoSubCuenta(parametros.get("codigoSubcuenta").toString());
				
				dto.setEsBD(true);

				if(rs.getInt("codigo_inicial_asocio") > 0 &&					
					rs.getInt("codigo_final_asocio")  < 0)
				{
					dto.setEsAsocioPendiente(true);				
				}
				
				if(rs.getInt("codigo_inicial_asocio") > 0 &&					
					rs.getInt("codigo_final_asocio")  > 0)
				{
					dto.setFueAsociada(true);
					
					if(rs.getInt("codigo_final_asocio") == rs.getInt("codigo_cuenta"))
					{
						dto.setViaIngCuentaAsocio(rs.getString("via_ing_cini"));												
					}
				}			
				
				list.add(dto);
			}
			
			rs.close();
			Autorizaciones.close();
			
		}catch(SQLException e){
			logger.info("Error caragarListadoCuentas !!!!!!\n La Consulta: "+cadena);
			logger.error(e);			
			return null;
		}
		return list;
	}	
}