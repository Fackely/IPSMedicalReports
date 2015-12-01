package com.princetonsa.dao.sqlbase.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;
import util.odontologia.InfoServicios;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosPlanT;
import com.princetonsa.dto.odontologia.DtoServArtIncCitaOdo;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.PlanTratamiento;
import com.princetonsa.sort.odontologia.SortGenerico;

/**
 * 
 * @author V�ctor Hugo G�mez L.
 *
 */

public class SqlBaseCitaOdontologicaDao 
{
	/**
	 * cadena que consulta las citas Odontol�gicas a partir de una agenda odontol�gica
	 */
	private static final String strConsultaCitasOdontologicasXAgenda = "SELECT " + 
			"co.codigo_pk AS codigo, " +
			"co.estado AS estado_cita, " +
			"co.tipo AS tipo_cita, " +
			"co.agenda AS cod_agenda, " +
			"co.duracion AS duracion, " +
			"co.hora_inicio AS hora_ini, " +
			"co.hora_fin AS hora_fin, " +
			"CASE WHEN co.fecha_reserva IS NOT NULL  THEN to_char(co.fecha_reserva,'"+ConstantesBD.formatoFechaBD+"') ELSE ' ' END AS fecha_res, " +
			"coalesce(co.hora_reserva,' ') AS hora_res, " +
			"coalesce(co.usuario_reserva,' ') AS usu_res, " +
			"coalesce(co.motivo_cancelacion,"+ConstantesBD.codigoNuncaValido+") AS mot_cancelacion, " +
			"CASE WHEN co.motivo_cancelacion IS NOT NULL THEN mcc.descripcion ELSE ' ' END AS desp_mot_cancelacion,  " +
			"co.por_confirmar AS confimar, " +
			"co.usuario_modifica AS usu_mod, " +
			"co.codigo_paciente AS cod_paciente, " +
			"getnombrepersona2(per.codigo) as nombre_paciente, " +
			"per.numero_identificacion AS num_iden_pac, " +
			"per.tipo_identificacion AS tipo_iden_pc, " +
			"coalesce(co.motivo_no_atencion,' ') AS mot_no_cancelacion, " +
			"coalesce(co.confirmacion,'') as confirmacion, " +
			"coalesce(co.observaciones_confirmacion,'') as observaciones_confirmacion, " +
			"coalesce(co.motivo_no_confirmacion,"+ConstantesBD.codigoNuncaValido+") as motivo_no_confirmacion, " +
			"coalesce(motcita.descripcion,'') as desc_motivo_no_confirmacion, " +
			"co.usuario_confirmacion as usuario_confirmacion, " +
			"getnombreusuario(usuario_confirmacion) as nombreusuarioconfirma, "+
			"to_char(co.fecha_confirmacion,'"+ConstantesBD.formatoFechaBD+"') as fecha_confirmacion, " +
			"co.hora_confirmacion as hora_confirmacion, " +
			"coalesce(co.centro_costo,"+ConstantesBD.codigoNuncaValido+") as centro_costo, " +
			"coalesce(to_char(co.fecha_programacion,'"+ConstantesBD.formatoFechaBD+"'),' ') as fecha_prog " +
			"FROM odontologia.citas_odontologicas co " +
			"INNER JOIN administracion.personas per ON (per.codigo = co.codigo_paciente) " +
			"LEFT OUTER JOIN consultaexterna.motivos_cancelacion_cita mcc ON (mcc.codigo = co.motivo_cancelacion) "+
			"LEFT OUTER JOIN " +
			"odontologia.motivos_cita motcita ON (motcita.codigo_pk = co.motivo_no_confirmacion) "+
			"WHERE co.agenda = ? " +
			"AND co.estado IN ('"+ConstantesIntegridadDominio.acronimoAsignado+"','"+ConstantesIntegridadDominio.acronimoReservado+"','"+ConstantesIntegridadDominio.acronimoAtendida+"') ";
	
	/**
	 * cadena que consulta las citas odontologicas a partir del codigo de un paciente
	 */
	private static final String strConsultaCitasOdontologicaXPaciente =
			"SELECT " +
				"co.codigo_pk as codigo_cita_odo, " +
				"co.estado as estado_cita_odo, " +
				"co.tipo as tipo_cita_odo, " +
				"co.agenda as cod_agenda, " +
				"co.duracion as duracion_cita, " +
				"coalesce(co.hora_inicio,' ') as hora_inicio, " +
				"coalesce(co.hora_fin,' ') as hora_fin, " +
				"coalesce(to_char(co.fecha_reserva,'yyyy-mm-dd'),' ') as fecha_reser, " +
				"coalesce(co.hora_reserva,' ') as hora_reser, " +
				"coalesce(co.usuario_reserva,' ') as usuario_reser, " +
				"coalesce(co.motivo_cancelacion,"+ConstantesBD.codigoNuncaValido+") as motivo_can_cita, " +
				"CASE WHEN co.motivo_cancelacion IS NOT NULL THEN mcc.descripcion ELSE ' ' END AS desp_mot_cancelacion,  " +
				"co.por_confirmar as por_confirmar, " +
				"co.usuario_modifica AS usu_mod, " +
				"co.codigo_paciente as cod_paciente, " +
				"caten.pais AS codpais, " +
				"getdescripcionpais(caten.pais) AS nombrepais, " +
				"caten.ciudad AS codciudad, " +
				"getnombreciudad(caten.pais,caten.departamento ,caten.ciudad) AS nombreciudad, " +
				"coalesce(co.motivo_no_atencion,' ') as motivo_no_aten, " +
				"coalesce(co.confirmacion,'') as confirmacion, " +
				"coalesce(co.observaciones_confirmacion,'') as observaciones_confirmacion, " +
				"coalesce(co.motivo_no_confirmacion,"+ConstantesBD.codigoNuncaValido+") as motivo_no_confirmacion, " +
				"coalesce(motcita.descripcion,'') as desc_motivo_no_confirmacion, " +
				"co.usuario_confirmacion as usuario_confirmacion, " +
				"getnombreusuario(usuario_confirmacion) as nombreusuarioconfirma, "+
				"to_char(co.fecha_confirmacion,'"+ConstantesBD.formatoFechaBD+"') as fecha_confirmacion, " +
				"co.hora_confirmacion as hora_confirmacion, " +
				"CASE WHEN ao.unidad_agenda IN (SELECT DISTINCT unidad_agenda FROM consultaexterna.unid_agenda_usu_caten WHERE usuario_autorizado= ?) THEN 'S' ELSE 'N' END AS permiso_usuario, " +
				"coalesce(co.centro_costo,"+ConstantesBD.codigoNuncaValido+") as centro_costo, " +
				"coalesce(to_char(co.fecha_programacion,'"+ConstantesBD.formatoFechaBD+"'),' ') as fecha_prog," +
				"ao.centro_atencion as codigo_centro_atencion," +
				"administracion.getnomcentroatencion(COALESCE(ao.centro_atencion,caccos.consecutivo)) as nombre_centro_atencion," +
				"co.migrado as migrado, " +
				"espe.nombre as nom_espe_uconsul, " +	// nombre de especialidad de la unidad de consulta
				"coalesce(ao.fecha,co.fecha_programacion) AS fecha_orden " +
			"FROM " +
				"odontologia.citas_odontologicas co " +
			"LEFT OUTER JOIN " +
				"odontologia.agenda_odontologica ao ON ( ao.codigo_pk = co.agenda) " +
			"LEFT OUTER JOIN " +
				"consultaexterna.motivos_cancelacion_cita mcc ON (mcc.codigo = co.motivo_cancelacion) "+
			"LEFT OUTER JOIN " +
				"odontologia.motivos_cita motcita ON (motcita.codigo_pk = co.motivo_no_confirmacion) "+
			"LEFT OUTER JOIN " +
				"administracion.centro_atencion caten ON (caten.consecutivo = ao.centro_atencion) " +
			"LEFT OUTER JOIN " +
				"administracion.centros_costo ccos ON (co.centro_costo = ccos.codigo) " +
				
			"LEFT OUTER JOIN consultaexterna.unidades_consulta uconsul ON (uconsul.codigo = ao.unidad_agenda) "+ 
			"LEFT OUTER JOIN administracion.especialidades espe ON (espe.codigo = uconsul.especialidad) "+ 	
				
			"LEFT OUTER JOIN " +
				"administracion.centro_atencion caccos ON (caccos.consecutivo = ccos.centro_atencion) ";
	
	/**
	 * sentencia sql que ingresa un registro en la cita odontologica
	 */
	private static final String strInsertCitaOdontologica = "INSERT INTO odontologia.citas_odontologicas (" +
			"codigo_pk, " +//1
			"estado, " +//2
			"tipo, " +//3
			"agenda, " +//4
			"duracion, " +//5
			"hora_inicio, " +//6
			"hora_fin, " +//7
			"fecha_reserva, " +//8
			"hora_reserva, " +//9
			"usuario_reserva, " +//10
			"motivo_cancelacion, " +//11
			"por_confirmar, " +//12
			"usuario_modifica, " +//13
			"fecha_modifica, " +
			"hora_modifica, " +
			"codigo_paciente, " +//14
			"motivo_no_atencion, " +//15
			"centro_costo, " +//16
			"fecha_programacion ," + //17
			"evo_genera_prox_cita, " +
			"indicativo_cambio_estado, " +
			"tipo_cancelacion" +//18 
			") VALUES (" +
			"?,?," +//2
			"?,?," +//4
			"?,?," +//6
			"?,?," +//8
			"?,?," +//10
			"?,?," +//12
			"?," +//13
			"CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
			"?,?," +//15
			"?,? , " + //17
			"?, " +//18
			"?, " +//19
			"?" +//20
			
			") ";
	
	/**
	 * sentencia sql que ingresa un o varios servicio de cita
	 */
	private static final String strInsertarServiciosCita = "INSERT INTO odontologia.servicios_cita_odontologica ( " +
			"codigo_pk, " +			//1
			"cita_odontologica, " +//2
			"servicio, " +			//3
			"duracion, " +			//4
			"programa_hallazgo_pieza, " +//5
			"numero_solicitud, " +//6
			"activo, " +	//7
			"usuario_modifica, " +//8
			"fecha_modifica, " +//9
			"hora_modifica, " +//10
			"valor_tarifa, " +//11
			"estado_cita, " +//12
			"codigo_agenda, " +//13
			"fecha_cita, " +//14
			"hora_inicio, " +//15
			"hora_fin, " +//16
			"unidad_agenda, " +//17
			"aplica_abono, " +//18
			"aplica_anticipo," +//19
			"contrato) " +//20
			"VALUES (" +
					"?,?," +//2
					"?,?," +//4
					"?,?," +//6
					"?,?," +//8
					"CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +//10
					"?,?," +//12
					"?,?," +//14
					"?,?," +//16
					"?,?," +//18
					"?,?) ";//20
	
	
	/**
	 * sentencia sql que actualiza los servicios de una cita especifica
	 */
	private static final String strActualizarServicioCitaOdon = " UPDATE odontologia.servicios_cita_odontologica SET " +
			"numero_solicitud = ?, " +
			"valor_tarifa = ?, " +
			"aplica_abono = ?, " +
			"aplica_anticipo = ?, " +
			"usuario_modifica = ?, " +
			"fecha_modifica= CURRENT_DATE, " +
			"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
			"WHERE " +
				"codigo_pk = ?";

	/**
	 * sentencia sql que actualiza los servicios de una cita especifica
	 */
	private static final String strActualizarServicioCitaOdonSinPk = " UPDATE odontologia.servicios_cita_odontologica SET " +
			"numero_solicitud = ?, " +
			"valor_tarifa = ?, " +
			"aplica_abono = ?, " +
			"aplica_anticipo = ?, " +
			"usuario_modifica = ?, " +
			"fecha_modifica= CURRENT_DATE, " +
			"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
			"WHERE " +
				"cita_odontologica = ? " +
				"AND " +
				"servicio = ?";

	
	/**
	 * Sentencia Sql para Inactivar todos los servicios Odontologicos asociados a un cita
	 */
	private static final String strInactivarServiciosCitaOdont ="UPDATE odontologia.servicios_cita_odontologica SET " +
	"activo = '"+ConstantesBD.acronimoNo+"' , " +
	"usuario_modifica = ?, " +
	"fecha_modifica = CURRENT_DATE, " +
	"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
	"WHERE cita_odontologica  = ? AND activo = '"+ConstantesBD.acronimoSi+"'";		
	
	
	/*
	 * 
	 */
	private static final String strUpdateServicioExistenteCitaOdonto="UPDATE odontologia.servicios_cita_odontologica SET " +
		"activo='"+ConstantesBD.acronimoSi+"', " +		
		"duracion = ?, " +
		"usuario_modifica = ?," +
		"estado_cita = ?, " +
		"codigo_agenda = ?, " +
		"fecha_cita = ?, " +
		"hora_inicio = ?, " +
		"hora_fin = ?, " +
		"unidad_agenda = ?, " +
		"fecha_modifica = CURRENT_DATE, " +
		"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
		"WHERE servicio = ? AND cita_odontologica = ? "; 
	
	
	
	/**
	 * sentencia sql consultar la cita
	 */
	private static final String strConsultaCitaOdon = "SELECT " + 
			"co.codigo_pk as cod_cita, " +
			"co.estado as est_cita, " +
			"co.tipo as tipo_cita, " +
			"co.agenda as agenda, " +
			"co.duracion as duracion_cita, " +
			"co.hora_inicio as hora_ini_cita, " +
			"co.hora_fin as hora_fin_cita, " +
			"case when co.fecha_reserva is null then ' ' else to_char(co.fecha_reserva,'yyyy-mm-dd') end as fecha_res_cit, " +
			"coalesce(co.hora_reserva,' ') as hora_res_cit, " +
			"coalesce(co.usuario_reserva,' ') as usu_res_cit, " +
			"coalesce(co.motivo_cancelacion,"+ConstantesBD.codigoNuncaValido+") as mot_can_cit, " +
			"co.por_confirmar as por_confir_cit, " +
			"co.codigo_paciente as cod_paciente_cit, " +
			"coalesce(co.motivo_no_atencion,' ') as mot_no_aten, " +
			"coalesce(co.centro_costo,"+ConstantesBD.codigoNuncaValido+") as centro_costo, " +
			"coalesce(to_char(co.fecha_programacion,'"+ConstantesBD.formatoFechaBD+"'),' ') as fecha_prog, " +
			"co.fecha_confirmacion as fecha_confirmacion, " +
			"co.hora_confirmacion as hora_confirmacion, " +
			"co.confirmacion as confirmacion, " +
			"co.observaciones_confirmacion as observaciones_confirmacion, " +
			"co.motivo_no_confirmacion as motivo_no_confirmacion " +
			"FROM odontologia.citas_odontologicas co " +
			"WHERE co.codigo_pk = ? ";
	
	/**
	 * sentencia sql consulta los servicios asociados a una cita especifica
	 */
	private static final String strConsultaServiciosCitaOdo = "SELECT " + 
			"sco.codigo_pk as cod_ser_cit_odo, " +
			"sco.cita_odontologica as cod_cit_odo, " +
			"sco.servicio as cod_servicio, " +
			"sco.duracion as duracion_ser, " +
			"coalesce(sco.programa_hallazgo_pieza,"+ConstantesBD.codigoNuncaValido+") as cod_php, " +
			"coalesce(sco.numero_solicitud,"+ConstantesBD.codigoNuncaValido+") as solicitud, " +
			"sco.activo as activo, " +
			"coalesce(sco.valor_tarifa,0) as  valor_tarifa, " +
			"sco.estado_cita as est_cita, " +
			"sco.codigo_agenda as cod_agen_odo, " +
			"to_char(sco.fecha_cita,'yyyy-mm-dd') as fecha_cit, " +
			"sco.hora_inicio as hora_ini_cit, " +
			"sco.hora_fin as hora_fin_cit, " +
			"coalesce(sco.unidad_agenda,"+ConstantesBD.codigoNuncaValido+") as cod_uni_consulta, " + 
			"sco.aplica_abono as aplica_abono, " +
			"sco.aplica_anticipo as aplica_anticipo, " +
			"CASE WHEN sco.numero_solicitud IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE essolserviciofacturado(sco.numero_solicitud, sco.servicio) END AS facturado, " +
			"ser.tipo_servicio as codigo_tipo_ser, " +
			"'('|| ser.especialidad||'-'||ser.codigo|| ')'||' '||getnombreservicio(ser.codigo,?) AS descripcion_servicio " +
			"FROM odontologia.servicios_cita_odontologica sco " +
			"INNER JOIN facturacion.servicios ser ON (ser.codigo = sco.servicio)" + 
			"WHERE sco.cita_odontologica = ? " +
			"AND sco.activo = '"+ConstantesBD.acronimoSi+"' ";
	
	/**
	 * 
	 */
	private static final String strConsultaServiciosArticulosIncluidos = "SELECT " +
			"saip.codigo_pk as codigo_pk, " +
			"saip.servicio_cita_odo as servicio_cita_odo, " +
			"coalesce(saip.servicio,"+ConstantesBD.codigoNuncaValido+") as codigo_servicio, " +
			"coalesce(facturacion.getnombreservicio(saip.servicio,"+ConstantesBD.codigoTarifarioCups+"),'') as nombre_servicio, " +
			"coalesce(saip.articulo,"+ConstantesBD.codigoNuncaValido+") as codigo_articulo, " +
			"coalesce(inventarios.getdescripcionarticulo(saip.articulo),'') as nombre_articulo, " +
			"saip.cantidad as cantidad, " +
			"saip.solicitud as solicitud " +
			"from odontologia.serv_art_inc_cita_odo saip WHERE saip.servicio_cita_odo = ? order by nombre_servicio,nombre_articulo";
	
	/**
	 * sentencia sql para insertar el log de cita odontologica
	 */
	private static final String strInsertLogCitaOdontologica = "INSERT INTO odontologia.log_citas_odontologicas (" +
			"codigo_pk," +
			"cita_odontologica," +
			"estado," +
			"agenda," +
			"hora_inicio," +
			"hora_fin," +
			"motivo_cancelacion," +
			"por_confirmar," +
			"usuario_modifica," +
			"fecha_modifica," +
			"hora_modifica," +
			"motivo_no_atencion, " +
			"fecha_programacion," +
			"confirmacion," +
			"observaciones_confirmacion, " +
			"motivo_no_confirmacion, " +
			"usuario_confirmacion, " +
			"fecha_confirmacion, " +
			"hora_confirmacion, " +
			"codigo_paciente " +
			")VALUES(?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?,?,?,?,?,?)";
	
	/**
	 * sentencia sql para insertar el log de servicios cita odontologica
	 */
	private static final String strInsertLogServiciosCitaOdontologica = "INSERT INTO odontologia.log_servicios_cita_odo (" +
			"codigo_pk," +
			"servicio_cita_odontologica," +
			"servicio," +
			"duracion," +
			"programa_hallazgo_pieza," +
			"numero_solicitud," +
			"activo," +
			"usuario_modifica," +
			"fecha_modifica," +
			"hora_modifica," +
			"valor_tarifa," +
			"estado_cita," +
			"codigo_agenda," +
			"fecha_cita," +
			"hora_inicio," +
			"hora_fin," +
			"unidad_agenda," +
			"aplica_abono," +
			"aplica_anticipo" +
			")VALUES (?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?,?,?,?,?,?)";
	
	/**
	 * sentencia sql para la inactivacion de los servicios de una cita odontologica
	 */
	private static final String strInactivarServicioCitaOdontologica = "UPDATE odontologia.servicios_cita_odontologica SET " +
			"activo = '"+ConstantesBD.acronimoNo+"' , " +
			"usuario_modifica = ?, " +
			"fecha_modifica = CURRENT_DATE, " +
			"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
			"WHERE codigo_pk = ? ";
	
	/**
	 * sentencia sql para el log
	 */
	private static final String strConsultaCitaOdontologica = "SELECT " +
			"codigo_pk," +
			"estado," +
			"agenda," +
			"hora_inicio," +
			"hora_fin," +
			"coalesce(motivo_cancelacion,"+ConstantesBD.codigoNuncaValido+") as mot_cancelacion ," +
			"por_confirmar," +
			"coalesce(motivo_no_atencion,' ') as mot_no_aten, " +
			"coalesce(centro_costo,"+ConstantesBD.codigoNuncaValido+") as centro_costo, " +
			"coalesce(to_char(fecha_programacion,'"+ConstantesBD.formatoFechaBD+"'),' ') as fecha_prog, " +
			"coalesce(confirmacion,'') as confirmacion, " +
			"coalesce(observaciones_confirmacion,'') as observaciones_confirmacion, " +
			"coalesce(motivo_no_confirmacion,"+ConstantesBD.codigoNuncaValido+") as motivo_no_confirmacion, " +
			"usuario_confirmacion as usuario_confirmacion, " +
			"to_char(fecha_confirmacion,'"+ConstantesBD.formatoFechaBD+"') as fecha_confirmacion, " +
			"coalesce(hora_confirmacion,'') as hora_confirmacion  " +
			"FROM odontologia.citas_odontologicas " +
			"WHERE codigo_pk = ? ";
	
	/**
	 * sentencia sql para log servicios cita
	 */
	private static final String strConsultaServiciosCitaOdoLog = "SELECT " +
			"codigo_pk, " +
			"cita_odontologica, " +
			"servicio, " +
			"duracion, " +
			"coalesce(programa_hallazgo_pieza,"+ConstantesBD.codigoNuncaValido+") as php, " +
			"coalesce(numero_solicitud,"+ConstantesBD.codigoNuncaValido+") as solicitud, " +
			"activo, " +
			"coalesce(valor_tarifa,0) as valor_tarifa, " +
			"estado_cita, " +
			"codigo_agenda, " +
			"fecha_cita, " +
			"hora_inicio, " +
			"hora_fin, " +
			"coalesce(unidad_agenda,"+ConstantesBD.codigoNuncaValido+") as unid_agenda, " +
			"aplica_abono, " +
			"aplica_anticipo  " +
			"FROM odontologia.servicios_cita_odontologica " +
			"WHERE codigo_pk = ? ";
	
	/**
	 * sentencia de actualizacion de cita odontologica
	 */
	private static final String strUpdateCitaOdontologica = "UPDATE odontologia.citas_odontologicas SET " +
			"estado = ?, " +
			"tipo = ?, " +
			"agenda = ?, " +
			"duracion = ?, " +
			"hora_inicio = ?, " +
			"hora_fin = ?, " +
			"fecha_reserva = ?, " +
			"hora_reserva = ?, " +
			"usuario_reserva = ?, " +
			"motivo_cancelacion = ?, " +
			"por_confirmar = ?, " +
			"usuario_modifica = ?, " +
			"fecha_modifica= CURRENT_DATE, " +
			"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
			"codigo_paciente = ?, " +
			"motivo_no_atencion = ?, " +
			"centro_costo = ?, " +
			"fecha_programacion = ?," +
			"usuario_registra = ?," +
			"fecha_registra = ?," +
			"hora_registra = ?, " +
			"confirmacion = ?," +
			"observaciones_confirmacion = ?, " +
			"motivo_no_confirmacion = ?, " +
			"usuario_confirmacion = ?, " +
			"fecha_confirmacion = ?, " +
			"hora_confirmacion = ? " +
			"WHERE codigo_pk = ? ";

	/**
	 * sentencia de actualizacion de cita odontologica
	 */
	private static final String strUpdateCitaOdontologicaCambioServicio = "UPDATE odontologia.citas_odontologicas SET " +
			"estado = ?, " +
			"tipo = ?, " +
			"agenda = ?, " +
			"duracion = ?, " +
			"hora_inicio = ?, " +
			"hora_fin = ?, " +
			"fecha_reserva = ?, " +
			"hora_reserva = ?, " +
			"usuario_reserva = ?, " +
			"motivo_cancelacion = ?, " +
			"por_confirmar = ?, " +
			"usuario_modifica = ?, " +
			"codigo_paciente = ?, " +
			"motivo_no_atencion = ?, " +
			"centro_costo = ?, " +
			"fecha_programacion = ?," +
			"usuario_registra = ?," +
			"fecha_registra = ?," +
			"hora_registra = ?, " +
			"confirmacion = ?," +
			"observaciones_confirmacion = ?, " +
			"motivo_no_confirmacion = ?, " +
			"usuario_confirmacion = ?, " +
			"fecha_confirmacion = ?, " +
			"hora_confirmacion = ? " +
			"WHERE codigo_pk = ? ";

	/**
	 * Cadena Sql utilizada para guardar los datos de Reprogramacion de una cita odontologica
	 */
	private static final String strUpdateCitaOdontoXReprogramacion= "UPDATE odontologia.citas_odontologicas SET " +
			"estado = '"+ConstantesIntegridadDominio.acronimoReservado+"', "+
	        "duracion = ?, " +
	        "hora_inicio = ?, " +
	        "hora_fin = ?, " +
			"fecha_programacion = ?, " +
			"agenda = ?, " +
			"usuario_modifica = ?, " +
			"tipo = ?, " +
			"fecha_modifica= CURRENT_DATE, " +
			"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
			"WHERE codigo_pk = ? ";
	
	
	/**
	 * Cadena Sql utilizada para guardar los datos de Reserva de una cita odontologica
	 */
	@SuppressWarnings("unused")
	private static final String strUpdateCitaOdontoXReserva= "UPDATE odontologia.citas_odontologicas SET " +
			"estado = '"+ConstantesIntegridadDominio.acronimoReservado+"', "+
	        "duracion = ?, " +
	        "hora_inicio = ?, " +
	        "hora_fin = ?, " +
			"agenda = ?, " +			
			"tipo = ?, " +
			"fecha_reserva = ?, " +
			"hora_reserva = ?, " +
			"usuario_reserva = ?, " +
			"fecha_modifica= CURRENT_DATE, " +
			"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" ," +
			"usuario_modifica = ? " +
			"WHERE codigo_pk = ? ";
	
	
	/**
	 * sentencia sql que verifica si almenos hay un servicios facturado para una 
	 * cita odontolo
	 */
	private static final String strConsultaServicioFacturado = "SELECT " +
			"CASE WHEN numero_solicitud IS NOT NULL THEN facturacion.essolserviciofacturado(numero_solicitud,servicio) ELSE '"+ConstantesBD.acronimoNo+"' END AS facturado " +
			"FROM odontologia.servicios_cita_odontologica " +
			"WHERE cita_odontologica = ? ";
	
	/**
	 * sentencia sql que obtiene informaci�n b�sica del centro de atencion
	 */
	private static final String strConsultaCentroCosto= "select " +
			"descripcion," +
			"coalesce(direccion,' ') as direccion, " +
			"coalesce(telefono,' ') as telefono  " +
			"from administracion.centro_atencion " +
			"where consecutivo = ? " +
			"and activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
	
	
	/**
	 * Sentencia Sql para consultar los servicios para las citas proximas
	 */
	private static final String consultarProxCitasPacienteXCancelacion="SELECT DISTINCT " +
			"co.codigo_pk AS codcita, " +
			"ser.codigo AS codservicio, " +
			"'('|| ser.especialidad||'-'||ser.codigo|| ')'||' '||getnombreservicio(ser.codigo,?) AS descripcion_servicio " + //1
			"FROM odontologia.servicios_cita_odontologica sco " +
			"INNER JOIN facturacion.servicios ser ON (ser.codigo = sco.servicio) " +
			"INNER JOIN odontologia.citas_odontologicas co ON (co.codigo_pk=sco.cita_odontologica) " +
			"INNER JOIN odontologia.agenda_odontologica ao ON (co.agenda=ao.codigo_pk) " +
			"LEFT OUTER JOIN odontologia.programas_hallazgo_pieza php ON (sco.programa_hallazgo_pieza=php.codigo_pk) " +
			"LEFT OUTER JOIN odontologia.superficies_x_programa sxp ON (php.codigo_pk=sxp.prog_hallazgo_pieza) " +
			"LEFT OUTER JOIN odontologia.det_plan_tratamiento dpt ON (sxp.det_plan_trata=dpt.codigo_pk) " +
			"LEFT OUTER JOIN odontologia.programas_servicios_plan_t prser ON (dpt.codigo_pk=prser.det_plan_tratamiento and sco.servicio=prser.servicio) " +
			"LEFT OUTER JOIN odontologia.programas prog ON ( prog.codigo = prser.programa AND prog.institucion = ? AND prog.activo = '"+ConstantesBD.acronimoSi+"') "+ //2
			" WHERE prog.codigo= ? " + //3
			"    AND co.codigo_paciente = ?  " + // 4
			"    AND prser.orden_servicio > ? " + // 5
			"    AND ( ao.fecha > ? OR ( ao.fecha = ? AND  co.hora_inicio >= ? )) " + // 6 - 7 - 8
			"    AND co.estado IN ('"+ConstantesIntegridadDominio.acronimoAsignado+"','"+ConstantesIntegridadDominio.acronimoReservado+"') ";
	

	private static final String strUpdateProgramaHallazgoPiezaServicioCitaOdonto="UPDATE odontologia.servicios_cita_odontologica SET " +
			"programa_hallazgo_pieza = ?, " +
			"fecha_modifica = CURRENT_DATE, " +
			"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
			"WHERE codigo_pk = ?";
	
	
	/**
	 * sentencia sql para el log
	 */
	private static final String strConsultaHistoricoEstadosCita = "SELECT " +
			"cita_odontologica, " +
			"estado, " +
			"fecha_modifica, " +
			"hora_modifica, " +
			"getnombreusuario(usuario_modifica) as usuarioModifica " +
			"from log_citas_odontologicas where cita_odontologica = ? " +
			"or cita_odontologica = " +
			"(select cita_asociada from citas_asociadas_a_programada where cita_programada = ?) " +
			"order by fecha_modifica desc, hora_modifica desc ";
	
	/**
	 * 
	 * @param codigoPaciente
	 * @param codigoPrograma
	 * @param ordenServicio
	 * @param codInstitucion
	 * @param fechaCita
	 * @param horaFinCita
	 * @return
	 */
	public static ArrayList<DtoCitaOdontologica> consultarProxCitasPacienteXCancelacion(int codigoPaciente, double codigoPrograma, int ordenServicio, int codInstitucion, String fechaCita, String horaFinCita, String loginUsuario)
	{
		String consulta =consultarProxCitasPacienteXCancelacion ;

		ArrayList<DtoCitaOdontologica> arrayProxCitas= new ArrayList<DtoCitaOdontologica>();
		int codigoTarifario = Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codInstitucion));

		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setInt(1, codigoTarifario);
			ps.setInt(2, codInstitucion);
			ps.setDouble(3, codigoPrograma);
			ps.setInt(4, codigoPaciente);
			ps.setInt(5, ordenServicio);
			ps.setDate(6,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaCita)));
			ps.setDate(7,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaCita)));
			ps.setString(8, horaFinCita);


			Log4JManager.info("\n\nla consulta Servicio Proximas Citas: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{			 
				arrayProxCitas.add(consultarCitaOdontologicaXCodigo(codigoPaciente, rs.getInt("codcita"),codInstitucion, loginUsuario));
			}
			rs.close();
			ps.close();
			UtilidadBD.cerrarConexion(con);
		}catch (Exception e) {
			Log4JManager.info("Error Consulta Servicios Proximas Citas: "+consultarProxCitasPacienteXCancelacion, e);

		}

		return arrayProxCitas;
	}
	
	
	/**
	 * Metodo que retorna  un true si la cita esta confirmada.
	 * @author Edgar Carvajal Ruiz
	 * @return
	 */
	public static boolean esCitaConfirmada(int codigoCita ){
		
		boolean retorna=Boolean.FALSE;
		
		String consulta="select confirmacion as confirmacion  FROM  odontologia.citas_odontologicas co  where codigo_pk="+codigoCita;
		
		try{
			
			Connection con=UtilidadBD.abrirConexion();
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
		
			
			Log4JManager.info("   "+ps+"\n\n\n");
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());			
			if(rs.next())
			{
				if( rs.getString("confirmacion").equals(ConstantesBD.acronimoSi))
				{
					retorna=Boolean.TRUE;
				}
			}
			
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (Exception e) {
			Log4JManager.info(e);
			
		}
	
		
		return retorna;
	}
	
	
	
	/**
	 * Se obtiene un listado de las citas odontol�gicas x una agenda espec�fica
	 * @param con
	 * @param HashMap parametros
	 * @return ArrayList<DtoCitaOdontologica>
	 */
	public static ArrayList<DtoCitaOdontologica> consultarCitaOdontologicaXAgenda(Connection con,HashMap parametros)
	{
		ArrayList<DtoCitaOdontologica> array = new ArrayList<DtoCitaOdontologica>();
		try{		
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strConsultaCitasOdontologicasXAgenda);
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get("codigo_agenda").toString()));
			Log4JManager.info("Consulta consultarCitaOdontologicaXAgenda: \n\n\n\n"+ps+"\n\n\n");
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());			
			while(rs.next())
			{
				DtoCitaOdontologica dto = new DtoCitaOdontologica();
				dto.setCodigoPk(rs.getInt("codigo"));
				dto.setEstado(rs.getString("estado_cita"));
				dto.setTipo(rs.getString("tipo_cita"));
				dto.setAgenda(rs.getInt("cod_agenda"));
				dto.setDuracion(rs.getInt("duracion"));
				dto.setHoraInicio(rs.getString("hora_ini"));
				dto.setHoraFinal(rs.getString("hora_fin"));
				dto.setFechaReserva(rs.getString("fecha_res").trim());
				dto.setHoraReserva(rs.getString("hora_res").trim());
				dto.setUsuarioReserva(rs.getString("usu_res").equals(" ")?"":rs.getString("usu_res"));
				dto.setMotivoCancelacion(rs.getInt("mot_cancelacion"));
				dto.setDescripcionMotCancelacion(rs.getString("desp_mot_cancelacion").equals(" ")?"":rs.getString("desp_mot_cancelacion"));
				dto.setPorConfirmar(rs.getString("confimar"));
				dto.setUsuarioModifica(rs.getString("usu_mod"));
				dto.setCodigoPaciente(rs.getInt("cod_paciente"));
				dto.setNombrePaciente(rs.getString("nombre_paciente"));
				dto.setNumeroIdentificacionPac(rs.getString("num_iden_pac"));
				dto.setTipoIdentificacionPac(rs.getString("tipo_iden_pc"));
				dto.setMotivoNoAtencion(rs.getString("mot_no_cancelacion"));
				dto.setCodigoCentroCosto(rs.getInt("centro_costo"));
				dto.setFechaProgramacion(rs.getString("fecha_prog").equals(" ")?"":rs.getString("fecha_prog"));
				
				dto.setConfirmacion(rs.getString("confirmacion"));
				dto.setObservacionesConfirmacion(rs.getString("observaciones_confirmacion"));
				//dto.getMotivoNoConfirmacion().setCodigo(rs.getInt("motivo_no_confirmacion"));
				dto.setMotivoNoConfirmacion(new InfoDatosInt(rs.getInt("motivo_no_confirmacion"), rs.getString("desc_motivo_no_confirmacion")));
				dto.getUsuarioConfirmacion().setLoginUsuario(rs.getString("usuario_confirmacion"));
				dto.getUsuarioConfirmacion().setNombreUsuario(rs.getString("nombreusuarioconfirma"));
				dto.setFechaConfirmacion(rs.getString("fecha_confirmacion"));
				dto.setHoraConfirmacion(rs.getString("hora_confirmacion"));
				
				if(dto.getCodigoPk()>0){
					Log4JManager.info("entre por mi primer servicio");
					dto.setServiciosCitaOdon(consultarServiciosCitaOdontologica(con, dto.getCodigoPk(),Utilidades.convertirAEntero(parametros.get("institucion")+""), false, dto.getCodigoPaciente()));
					
					for(DtoServicioCitaOdontologica servicio:dto.getServiciosCitaOdon())
					{
						if(servicio.getCodigoProgramaHallazgoPieza()>0)
						{
							dto.setEstadoPlanTratamiento(obtenerEstadoPlanTratamiento(con, servicio.getCodigoProgramaHallazgoPieza()));
							break;
						}
					}
					
				}
				array.add(dto);
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			Log4JManager.error("Error Consulta: "+strConsultaCitasOdontologicasXAgenda,e);
		}
		return array;
	}
	
	
	/**
	 * M�todo para obtener el estado del plan de tratamiento a partir de un detalle del servicio.
	 * @param con
	 * @param programasServiciosPlanT
	 * @return
	 */
	private static String obtenerEstadoPlanTratamiento(Connection con,int programaHallazgoPieza)
	{
		String estado = "";
		try
		{
			String consulta = "SELECT " +
				"pt.estado as estado " +
				"from odontologia.programas_hallazgo_pieza php " +
				"inner join odontologia.plan_tratamiento pt on (pt.codigo_pk = php.plan_tratamiento) " +
				"WHERE php.codigo_pk = ?";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1,programaHallazgoPieza);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				estado = rs.getString("estado");
			}
			rs.close();
			pst.close();
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error en obtenerEstadoPlanTratamiento: ",e);
		}
		return estado;
	}
	
	
	/**
	 * Se obtiene un listado de los servicios de cada cita odontologica
	 * @param con
	 * @param institucion 
	 * @param incluirInactivos 
	 * @param HashMap parametros
	 * @return ArrayList<DtoCitaOdontologica>
	 */
	public static ArrayList<DtoServicioCitaOdontologica> consultarServiciosCitaOdontologica(Connection con,int codigoCita, int institucion, boolean incluirInactivos, int codigoPaciente)
	{
		ArrayList<DtoServicioCitaOdontologica> array = new ArrayList<DtoServicioCitaOdontologica>();
		try{
			String codigoTarifario=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion);
			if(UtilidadTexto.isEmpty(codigoTarifario))
			{
				codigoTarifario="0";
			}
			//Log4JManager.info("Consulta strConsultaServiciosCitaOdontologica: "+strConsultaServiciosCitaOdontologica);
			String sentenciaSQL = "SELECT distinct " +
								"dpt.plan_tratamiento as planTratamiento, " +
								"php.programa AS programa, " +
								"co.tipo AS tipo, sco.codigo_pk as codigo, sco.cita_odontologica as cita_odo, sco.servicio as servicio, " +
								"getcodigopropservicio2(sco.servicio,1) as codigopropietario," +
								"facturacion.getNombreServicio(sco.servicio,1) as nombrepropietario,pspt.orden_servicio AS ordenservicio, " +
								"facturacion.getnombreservicio(sco.servicio,0) as nombre_servicio, sco.duracion as duracion, " +
								"coalesce(sco.numero_solicitud,-1) as num_solicitud, sco.activo as activo, sco.usuario_modifica as usu_mod, " +
								"coalesce(sco.valor_tarifa,0.0) as val_tarifa, sco.estado_cita as est_cita, sco.codigo_agenda as cod_agenda, " +
								"CASE WHEN sco.fecha_cita IS NOT NULL THEN to_char(sco.fecha_cita,'YYYY-MM-DD') ELSE ' ' END AS fecha_cita, " +
								"sco.hora_inicio as hora_ini, sco.hora_fin as hora_fin, coalesce(sco.unidad_agenda,-1) as cod_uni_agen, " +
								"sco.contrato AS contrato, sco.aplica_abono as aplica_abono, sco.aplica_anticipo as aplica_anticipo, " +
								"CASE WHEN sco.numero_solicitud IS NULL THEN 'N' ELSE essolserviciofacturado(sco.numero_solicitud, " +
								"sco.servicio) END AS facturado, sco.programa_hallazgo_pieza as codigoproghallpieza," +
								"php.pieza_dental as pieza_dental,php.seccion as seccion " +
					"FROM odontologia.servicios_cita_odontologica sco " +
					"INNER JOIN odontologia.citas_odontologicas co ON(co.codigo_pk=sco.cita_odontologica AND sco.activo='S') " +
					"LEFT OUTER JOIN odontologia.programas_hallazgo_pieza php on  (sco.programa_hallazgo_pieza=php.codigo_pk )" +
					"LEFT OUTER JOIN odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
					"LEFT OUTER JOIN odontologia.det_plan_tratamiento dpt ON(sxp.det_plan_trata=dpt.codigo_pk) " +
					"LEFT OUTER JOIN odontologia.programas_servicios_plan_t pspt ON (dpt.codigo_pk=pspt.det_plan_tratamiento and pspt.servicio=sco.servicio) " +
					"WHERE sco.cita_odontologica = ? " ;
			
			//ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(2)
			if(!incluirInactivos)
			{
				sentenciaSQL+="AND sco.activo = '"+ConstantesBD.acronimoSi+"'";
			}
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,sentenciaSQL);
			ps.setInt(1, codigoCita);
			Log4JManager.info(ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoServicioCitaOdontologica dto = new DtoServicioCitaOdontologica();
				dto.setCodigoPk(rs.getInt("codigo"));
				dto.setCitaOdontologica(rs.getInt("cita_odo"));
				dto.setServicio(rs.getInt("servicio"));
				dto.setCodigoPropietarioServicio(rs.getString("codigopropietario"));
				dto.setDescripcionPropietario(rs.getString("nombrepropietario"));
				dto.setNombreServicio(rs.getString("nombre_servicio"));
				dto.setDuracion(rs.getInt("duracion"));
				dto.setCodigoProgramaHallazgoPieza(rs.getInt("codigoproghallpieza"));
				dto.setNumeroSolicitud(rs.getInt("num_solicitud"));
				dto.setActivo(rs.getString("activo"));
				dto.setUsuarioModifica(rs.getString("usu_mod"));
				dto.setValorTarifa(rs.getBigDecimal("val_tarifa"));
				dto.setEstadoCita(rs.getString("est_cita"));
				dto.setCodigoAgenda(rs.getInt("cod_agenda"));
				dto.setFechaCita(rs.getString("fecha_cita"));
				dto.setHoraInicio(rs.getString("hora_ini"));
				dto.setHoraFinal(rs.getString("hora_fin"));
				dto.setUnidadAgenda(rs.getInt("cod_uni_agen"));
				dto.setTipoCita(rs.getString("tipo"));				
				dto.setAplicaAbono(rs.getString("aplica_abono"));
				dto.setAplicaAnticipo(rs.getString("aplica_anticipo"));
				dto.setFacturado(rs.getString("facturado"));
				dto.getProgramaHallazgoPieza().setCodigoPk(rs.getInt("codigoproghallpieza"));
				dto.getProgramaHallazgoPieza().setPrograma(rs.getInt("programa"));
				dto.setCodigoPrograma(rs.getInt("programa"));
				dto.setOrdenServicio(rs.getInt("ordenservicio"));
				
				dto.setServiciosArticulosIncluidos(obtenerServiciosArticulosIncluidos(con, dto.getCodigoPk()));
				dto.setCondicionesServicio(obtenerCondicionesServicio(con, dto.getServicio()));
				dto.setInfoResponsableCobertura(new InfoResponsableCobertura());
				dto.getInfoResponsableCobertura().setDtoSubCuenta(new DtoSubCuentas());
				dto.getInfoResponsableCobertura().getDtoSubCuenta().setContrato(rs.getInt("contrato"));
				dto.setPiezaDental(rs.getInt("pieza_dental"));
				dto.setSuperficies(SqlBaseUtilidadOdontologia.cargarSuperficiesXPrograma(con,rs.getInt("codigoproghallpieza")));
				dto.setSeccionHallazgo(rs.getString("seccion"));
				//dto.setCodigoPresuOdoProgSer(rs.getInt("presuOdoProgServ"));
				dto.setPlanTratamiento(rs.getInt("planTratamiento"));
				//Log4JManager.info("NUM SOLICITUD >>"+dto.getNumeroSolicitud());
				array.add(dto);
				
				dto.setAsignadoAOtraCita(PlanTratamiento.estaServicioAsociadoAOtraCita(con, dto.getCitaOdontologica(), rs.getInt("codigoproghallpieza"),dto.getServicio(), codigoPaciente));
				
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			Log4JManager.error("Error Consulta:",e);;
		}
		return array;
	}
	
	/**
	 * M�todo para obtener las condiciones del servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	private static ArrayList<InfoDatosInt> obtenerCondicionesServicio(Connection con,int codigoServicio)
	{
		ArrayList<InfoDatosInt> condiciones = new ArrayList<InfoDatosInt>();
		try
		{
			String consulta = "SELECT " +
				"ec.codigo_examenct as codigo, " +
				"ec.descrip_examenct as condicion " +
				"from facturacion.condi_serv_servicios css " +
				"inner join facturacion.condi_serv_condiciones csc ON(csc.consecutivo = css.consecutivo) " +
				"inner join facturacion.examen_conditoma ec ON(ec.codigo_examenct = csc.condicion) " +
				"WHERE css.servicio = ?";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1,codigoServicio);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				InfoDatosInt elemento = new InfoDatosInt();
				elemento.setCodigo(rs.getInt("codigo"));
				elemento.setNombre(rs.getString("condicion"));
				
				condiciones.add(elemento);
			}
			rs.close();
			pst.close();
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error en obtenerCondicionesServicio: ",e);
		}
		return condiciones;
	}
	
	
	/**
	 * Metodo para consultar la cita Odontologica asociada a un paciente y codigo especifico
	 * @param codigoPaciente
	 * @param codigoCita
	 * @param institucion 
	 * @param usuario
	 * @return
	 */
	private static DtoCitaOdontologica consultarCitaOdontologicaXCodigo(int codigoPaciente, int codigoCita, int institucion, String usuario)
	{
		String consulta = strConsultaCitasOdontologicaXPaciente;
	
		consulta+= "WHERE co.codigo_paciente = ? AND co.codigo_pk = ? ";
		
		DtoCitaOdontologica cita = new DtoCitaOdontologica();
	
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setString(1, usuario);
			ps.setInt(2, codigoPaciente);
			ps.setInt(3, codigoCita);
			
			Log4JManager.info("consulta de cita X Codigo: "+ps);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{			
				cita.setCodigoPk(rs.getInt("codigo_cita_odo"));
				cita.setEstado(rs.getString("estado_cita_odo"));
				cita.setTipo(rs.getString("tipo_cita_odo"));
				cita.setAgenda(rs.getInt("cod_agenda"));
				cita.setDuracion(rs.getInt("duracion_cita"));
				cita.setHoraInicio(rs.getString("hora_inicio"));
				cita.setHoraFinal(rs.getString("hora_fin"));
				cita.setFechaReserva(rs.getString("fecha_reser").trim());
				cita.setHoraReserva(rs.getString("hora_reser").trim());
				cita.setUsuarioReserva(rs.getString("usuario_reser").equals(" ")?"":rs.getString("usuario_reser"));
				cita.setMotivoCancelacion(rs.getInt("motivo_can_cita"));
				cita.setDescripcionMotCancelacion(rs.getString("desp_mot_cancelacion").equals(" ")?"":rs.getString("desp_mot_cancelacion"));
				cita.setPorConfirmar(rs.getString("por_confirmar"));
				cita.setUsuarioModifica(rs.getString("usu_mod"));
				cita.setCodigoPaciente(rs.getInt("cod_paciente"));
				cita.setMotivoNoAtencion(rs.getString("motivo_no_aten"));
				cita.setPermisoUsuario(rs.getString("permiso_usuario"));
				cita.setCodigoCentroCosto(rs.getInt("centro_costo"));
				cita.setFechaProgramacion(rs.getString("fecha_prog").equals(" ")?"":rs.getString("fecha_prog"));
				
				cita.setMigrado(UtilidadTexto.getBoolean(rs.getString("migrado")));
				
				cita.setConfirmacion(rs.getString("confirmacion"));
				cita.setObservacionesConfirmacion(rs.getString("observaciones_confirmacion"));
				cita.getMotivoNoConfirmacion().setCodigo(rs.getInt("motivo_no_confirmacion"));
				cita.getUsuarioConfirmacion().setLoginUsuario(rs.getString("usuario_confirmacion"));
				cita.setFechaConfirmacion(rs.getString("fecha_confirmacion"));
				cita.setHoraConfirmacion(rs.getString("hora_confirmacion"));
				
				
				if(cita.getCodigoPk()>0){				
					
					cita.setServiciosCitaOdon(consultarServiciosCitaOdontologica(con, cita.getCodigoPk(),institucion, true, codigoPaciente));
					Log4JManager.info("numero de servicios encontrados: "+cita.getServiciosCitaOdon().size());
					cita.setAgendaOdon(SqlBaseAgendaOdontologicaDao.consultaAgendaOdontologica(con, cita.getAgenda(), true));
				}
				if(cita.getAgendaOdon().getCodigoPk()<=0)
				{
					String consultarProfesionalCitaProgramada=
							"SELECT " +
								"getNombrePersona(m.codigo_medico) AS nombremedico " +
							"FROM " +
								"odontologia.citas_odontologicas co " +
							"INNER JOIN " +
								"administracion.usuarios u " +
									"ON(u.login=co.usuario_modifica) " +
							"INNER JOIN " +
								"administracion.medicos m " +
									"ON(m.codigo_medico=u.codigo_persona) " +
							"WHERE " +
								"co.codigo_pk=?";
					
					PreparedStatementDecorator psdProfesional=new PreparedStatementDecorator(con, consultarProfesionalCitaProgramada);
					psdProfesional.setInt(1, cita.getCodigoPk());
					ResultSetDecorator rsdProfesional=new ResultSetDecorator(psdProfesional.executeQuery());
					if(rsdProfesional.next())
					{
						cita.getAgendaOdon().setNombreMedico(rsdProfesional.getString("nombremedico"));
					}
				}

			}
			rs.close();
			ps.close();
			UtilidadBD.cerrarConexion(con);
			
		}catch(SQLException e)
		{
			Log4JManager.error("Error en Cita Odontologica: ",e);
		}
		
		return cita;
	}
	
	/**
	 * se obtiene un listado de las citas odontologicas x un paciente especifico
	 * @param con
	 * @param HashMap parametros
	 * @return ArrayList<DtoCitaOdontologica>
	 */
	public static ArrayList<DtoCitaOdontologica> consultarCitaOdontologicaXPaciente(Connection con, HashMap parametros)
	{
		ArrayList<DtoCitaOdontologica> array = new ArrayList<DtoCitaOdontologica>();
		try{
			//Log4JManager.info("Consulta Citas Odontologicas: "+ );
			//********************************SE TOMAN LOS PAR�METROS*********************************
			String fechaFiltroAsignacion = parametros.get("fechaFiltro").toString();
			String horaFiltroAsignacion = parametros.get("horaFiltro").toString();
			String fechaFiltroReserva = parametros.get("fechaFiltroReserva").toString();
			String horaFiltroReserva = parametros.get("horaFiltroReserva").toString();
			//****************************************************************************************
			String consulta = strConsultaCitasOdontologicaXPaciente;
			
			int codigoIngreso = ConstantesBD.codigoNuncaValido;
				
			if(parametros.get("codigo_ingreso")!=null){
				
				codigoIngreso =  (Integer) parametros.get("codigo_ingreso");
			}

			int posicionParametro = 1;
			

			if(codigoIngreso>0){
				
				consulta+=" LEFT OUTER JOIN " +
								"manejopaciente.ingresos ingreso ON (ingreso.codigo_paciente = co.codigo_paciente) "+
							"WHERE co.codigo_paciente = ? AND ingreso.id = ? ";
			}else{
				
				consulta+=" WHERE co.codigo_paciente = ? ";
			}
			
			if(!fechaFiltroAsignacion.equals("") && !horaFiltroAsignacion.equals("") && !fechaFiltroReserva.equals("") && !horaFiltroReserva.equals("") )
			{
				consulta += " and (" +
										"( (ao.fecha > ? or (ao.fecha = ? and  (co.hora_inicio >= ?))) AND co.estado NOT IN ('"+ConstantesIntegridadDominio.acronimoProgramado+"', '"+ConstantesIntegridadDominio.acronimoNoAsistio+"', '"+ConstantesIntegridadDominio.acronimoNoAtencion+"'))" +
									
									"OR " +
										"(co.estado = '"+ConstantesIntegridadDominio.acronimoAsignado+"') " +
									"OR " +
										"(co.estado = '"+ConstantesIntegridadDominio.acronimoAreprogramar+"') " +
									
									"OR (co.estado = '"+ConstantesIntegridadDominio.acronimoReservado+"' AND (ao.fecha > ? OR (ao.fecha = ? AND  (co.hora_inicio >= ?)))) " +
				
									"AND co.codigo_pk IN (SELECT codigo_cita FROM odontologia.solicitud_cambio_servicio where estado='"+ConstantesIntegridadDominio.acronimoEstadoSolicitado+"'"    /*+"AND co.fecha_programacion>=CURRENT_DATE"*/+")" +
										
									"OR " +
										"(co.estado IN ('"+ConstantesIntegridadDominio.acronimoProgramado+"', '"+ConstantesIntegridadDominio.acronimoNoAsistio+"', '"+ConstantesIntegridadDominio.acronimoNoAtencion+"') "/*+"AND co.fecha_programacion>=CURRENT_DATE"*/+")" +
								") ";
				
				consulta += " ORDER BY ao.fecha ,co.hora_inicio";
				
			}else{
				
				consulta += "ORDER BY fecha_orden,co.hora_inicio";
			}
			
			UsuarioBasico usuario=(UsuarioBasico)parametros.get("usuario");
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setString(posicionParametro++, usuario.getLoginUsuario());
			ps.setInt(posicionParametro++, Utilidades.convertirAEntero(parametros.get("codigo_paciente").toString()));
			
			if(codigoIngreso > 0){
				
				ps.setInt(posicionParametro++, codigoIngreso);
			}
			
			if(!fechaFiltroAsignacion.equals("")&&!horaFiltroAsignacion.equals("") && !fechaFiltroReserva.equals("")&&!horaFiltroReserva.equals(""))
			{
				ps.setDate(posicionParametro++,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFiltroAsignacion)));
				ps.setDate(posicionParametro++,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFiltroAsignacion)));
				ps.setString(posicionParametro++,horaFiltroAsignacion);
				ps.setDate(posicionParametro++,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFiltroReserva)));
				ps.setDate(posicionParametro++,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFiltroReserva)));
				ps.setString(posicionParametro++,horaFiltroReserva);
			}
			
			Log4JManager.info("consulta de citas Paciente: "+ps);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoCitaOdontologica dto = new DtoCitaOdontologica();
				dto.setCodigoPk(rs.getInt("codigo_cita_odo"));
				dto.setEstado(rs.getString("estado_cita_odo"));
				dto.setTipo(rs.getString("tipo_cita_odo"));
				dto.setAgenda(rs.getInt("cod_agenda"));
				dto.setDuracion(rs.getInt("duracion_cita"));
				dto.setHoraInicio(rs.getString("hora_inicio"));
				dto.setHoraFinal(rs.getString("hora_fin"));
				dto.setFechaReserva(rs.getString("fecha_reser").trim());
				dto.setHoraReserva(rs.getString("hora_reser").trim());
				dto.setUsuarioReserva(rs.getString("usuario_reser").equals(" ")?"":rs.getString("usuario_reser"));
				dto.setMotivoCancelacion(rs.getInt("motivo_can_cita"));
				dto.setDescripcionMotCancelacion(rs.getString("desp_mot_cancelacion").equals(" ")?"":rs.getString("desp_mot_cancelacion"));
				dto.setPorConfirmar(rs.getString("por_confirmar"));
				dto.setUsuarioModifica(rs.getString("usu_mod"));
				dto.setCodigoPaciente(rs.getInt("cod_paciente"));
				dto.setMotivoNoAtencion(rs.getString("motivo_no_aten"));
				dto.setPermisoUsuario(rs.getString("permiso_usuario"));
				dto.setCodigoCentroCosto(rs.getInt("centro_costo"));
				dto.setFechaProgramacion(rs.getString("fecha_prog").equals(" ")?"":rs.getString("fecha_prog"));
				dto.setCodigoCentroAtencion(rs.getInt("codigo_centro_atencion"));
				dto.setCodciudad(rs.getString("codciudad"));
				dto.setCodpais(rs.getString("codpais"));
				dto.setMigrado(UtilidadTexto.getBoolean(rs.getString("migrado")));
				
				dto.setConfirmacion(rs.getString("confirmacion"));
				dto.setObservacionesConfirmacion(rs.getString("observaciones_confirmacion"));
				dto.getMotivoNoConfirmacion().setCodigo(rs.getInt("motivo_no_confirmacion"));
				dto.setMotivoNoConfirmacion(new InfoDatosInt(rs.getInt("motivo_no_confirmacion"), rs.getString("desc_motivo_no_confirmacion")));
				dto.getUsuarioConfirmacion().setLoginUsuario(rs.getString("usuario_confirmacion"));
				dto.getUsuarioConfirmacion().setNombreUsuario(rs.getString("nombreusuarioconfirma"));
				dto.setFechaConfirmacion(rs.getString("fecha_confirmacion"));
				dto.setHoraConfirmacion(rs.getString("hora_confirmacion"));
				
				dto.setNomEspeUconsulta(rs.getString("nom_espe_uconsul"));
				
				Log4JManager.info("Codigo de la cita, voy a buscar los servicios "+dto.getCodigoPk());
				
				if(dto.getCodigoPk()>0){				
					
					dto.setServiciosCitaOdon(consultarServiciosCitaOdontologica(con, dto.getCodigoPk(),Utilidades.convertirAEntero(parametros.get("institucion")+""), true, Utilidades.convertirAEntero(parametros.get("codigo_paciente").toString())));
					Log4JManager.info("numero de servicios encontrados: "+dto.getServiciosCitaOdon().size());
					dto.setAgendaOdon(SqlBaseAgendaOdontologicaDao.consultaAgendaOdontologica(con, dto.getAgenda(), true));
				}
				
				dto.getAgendaOdon().setDescripcionCentAten(rs.getString("nombre_centro_atencion"));
				
				if(dto.getAgendaOdon().getCodigoPk()<=0)
				{
					String consultarProfesionalCitaProgramada=
							"SELECT " +
								"getNombrePersona(m.codigo_medico) AS nombremedico " +
							"FROM " +
								"odontologia.citas_odontologicas co " +
							"INNER JOIN " +
								"administracion.usuarios u " +
									"ON(u.login=co.usuario_modifica) " +
							"INNER JOIN " +
								"administracion.medicos m " +
									"ON(m.codigo_medico=u.codigo_persona) " +
							"WHERE " +
								"co.codigo_pk=?";
					PreparedStatementDecorator psdProfesional=new PreparedStatementDecorator(con, consultarProfesionalCitaProgramada);
					psdProfesional.setInt(1, dto.getCodigoPk());
					Log4JManager.info("**********Consulta Profesional : "+psdProfesional);
					ResultSetDecorator rsdProfesional=new ResultSetDecorator(psdProfesional.executeQuery());
					if(rsdProfesional.next()) {
						dto.getAgendaOdon().setNombreMedico(rsdProfesional.getString("nombremedico"));
					} else {
						dto.getAgendaOdon().setNombreMedico("NO ASIGNADO");
					}
				}

				if(UtilidadTexto.isEmpty(dto.getAgendaOdon().getNombreMedico())) {
					dto.getAgendaOdon().setNombreMedico("NO ASIGNADO");
				}

				//cargar la informacion de la ultima solicitud de cambio de servicio en estado solicitada.
				String consultaSolicitudCambioServicio="SELECT codigo_pk as codigo,estado as estado from odontologia.solicitud_cambio_servicio where codigo_cita=? and estado='"+ConstantesIntegridadDominio.acronimoEstadoSolicitado+"' order by codigo_pk desc";
				PreparedStatementDecorator psSCS =  new PreparedStatementDecorator(con,consultaSolicitudCambioServicio);
				psSCS.setInt(1, dto.getCodigoPk());
				ResultSetDecorator rsSCS = new ResultSetDecorator(psSCS.executeQuery());
				
				if(rsSCS.next())
				{
					dto.setCodigoSolicitudCambioServicio(rsSCS.getInt("codigo"));
					dto.setEstadoSolicitudCambioServicio(rsSCS.getString("estado"));
				
				}else{
					
					dto.setCodigoSolicitudCambioServicio(ConstantesBD.codigoNuncaValido);
					dto.setEstadoSolicitudCambioServicio("");
				}
				
				//Se cargan las actividades que podr�a aplicar la cita
				if(!fechaFiltroAsignacion.equals("")&&!horaFiltroAsignacion.equals("") && !fechaFiltroReserva.equals("")&&!horaFiltroReserva.equals(""))
				{
					int centroAtencion=dto.getAgendaOdon().getCentroAtencion();
					if(dto.getEstado().equals(ConstantesIntegridadDominio.acronimoProgramado))
					{
						centroAtencion=usuario.getCodigoCentroAtencion();
					}
					Log4JManager.info("Cargar Actividades unidad >> "+ dto.getAgendaOdon().getUnidadAgenda()+" usuario>> "+ usuario.getLoginUsuario()+" centroAt >> "+ usuario.getCodigoCentroAtencion()+" cita >>"+ dto.getCodigoPk() );
					dto.cargarArregloActividades(con, dto.getAgendaOdon().getUnidadAgenda(), usuario.getLoginUsuario(), centroAtencion, dto.getCodigoPk(),dto.getCodigoSolicitudCambioServicio(),dto.getEstadoSolicitudCambioServicio());
				}
				
				array.add(dto);
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			Log4JManager.error("Error Consulta: "+strConsultaCitasOdontologicasXAgenda, e);
		}
		return array;
	}
	
	
	

	
	
	
	
	
	/**
	 * Se obtiene un listado de las citas odontol�gicas seg�n par�metros de B�squeda ( Rango ) 
	 * @param con
	 * @param parametros
	 * @param centroAtencionUsuario centro de atenci�n del usuario
	 * @param ultimoPlanTratamiento Sentnecia para buscar el �ltimo plan de tratamiento del paciente
	 * @return
	 */
	public static ArrayList<DtoCitaOdontologica> consultarCitaOdontologicaXRango(Connection con,HashMap<String, Object> parametros, int centroAtencionUsuario, String ultimoPlanTratamiento)
	{
		ArrayList<DtoCitaOdontologica> array = new ArrayList<DtoCitaOdontologica>(); 
		String strConsultaCitasOdontologicaXRango =
			"SELECT " +
				"co.codigo_pk as codigo_cita_odo, " +
				"co.estado as estado_cita_odo, " +
				"co.tipo as tipo_cita_odo, " +
				"getintegridaddominio(co.tipo) AS nombre_tipo_cita_odo, " +
				"co.agenda as cod_agenda, " +
				"co.duracion as duracion_cita, " +
				"coalesce(co.hora_inicio,' ') as hora_inicio, " +
				"coalesce(co.hora_fin,' ') as hora_fin, " +
				"coalesce(to_char(co.fecha_reserva,'yyyy-mm-dd'),' ') as fecha_reser, " +
				"coalesce(co.hora_reserva,' ') as hora_reser, " +
				"coalesce(co.usuario_reserva,' ') as usuario_reser, " +			
				"coalesce(co.motivo_cancelacion,"+ConstantesBD.codigoNuncaValido+") as motivo_can_cita, " +
				"CASE WHEN co.motivo_cancelacion IS NOT NULL THEN mcc.descripcion ELSE ' ' END AS desp_mot_cancelacion,  " +
				"co.por_confirmar as por_confirmar, " +
				"co.usuario_modifica AS usu_mod, " +
				"co.codigo_paciente as cod_paciente, " +
				"administracion.getnombrepersona(co.codigo_paciente) AS nombrespaciente,  " +
				"historiaclinica.getnumeroytipoid(co.codigo_paciente) AS numidpaciente, " +
				"caten.pais AS codpais, " +
				"getdescripcionpais(caten.pais) AS nombrepais, " +
				"caten.ciudad AS codciudad, " +
				"getnombreciudad(caten.pais,caten.departamento ,caten.ciudad) AS nombreciudad, " +
				"coalesce(co.motivo_no_atencion,' ') as motivo_no_aten, " +
				"coalesce(co.confirmacion,'') as confirmacion, " +
				"coalesce(co.observaciones_confirmacion,'') as observaciones_confirmacion, " +
				"coalesce(co.motivo_no_confirmacion,"+ConstantesBD.codigoNuncaValido+") as motivo_no_confirmacion, " +
				"coalesce(motcita.descripcion,'') as desc_motivo_no_confirmacion, " +
				"co.usuario_confirmacion as usuario_confirmacion, " +
				"getnombreusuario(usuario_confirmacion) as nombreusuarioconfirma, "+
				"to_char(co.fecha_confirmacion,'"+ConstantesBD.formatoFechaBD+"') as fecha_confirmacion, " +
				"co.hora_confirmacion as hora_confirmacion, " +
				"CASE WHEN ao.unidad_agenda IN (SELECT DISTINCT unidad_agenda FROM consultaexterna.unid_agenda_usu_caten WHERE usuario_autorizado= ?) THEN 'S' ELSE 'N' END AS permiso_usuario, " +
				"coalesce(co.centro_costo,"+ConstantesBD.codigoNuncaValido+") as centro_costo, " +
				"coalesce(to_char(co.fecha_programacion,'"+ConstantesBD.formatoFechaBD+"'),' ') as fecha_prog," +
				"ao.centro_atencion as codigo_centro_atencion," +
				"administracion.getnomcentroatencion(COALESCE(ao.centro_atencion,caccos.consecutivo)) as nombre_centro_atencion," +
				"co.migrado as migrado, " +
				"coalesce(ao.fecha,co.fecha_programacion) AS fecha_orden, " +
				"("+ultimoPlanTratamiento+") AS estado_plan_t ," +
				"co.tipo_cancelacion AS tipo_can " +
			"FROM " +
				"odontologia.citas_odontologicas co " +
			"LEFT OUTER JOIN " +
				"odontologia.agenda_odontologica ao ON ( ao.codigo_pk = co.agenda) " +
			"LEFT OUTER JOIN " +
				"consultaexterna.motivos_cancelacion_cita mcc ON (mcc.codigo = co.motivo_cancelacion) " +
			"LEFT OUTER JOIN " +
				"odontologia.motivos_cita motcita ON (motcita.codigo_pk = co.motivo_no_confirmacion) "+
			"LEFT OUTER JOIN " +
				"administracion.centro_atencion caten ON (caten.consecutivo = ao.centro_atencion) "+
			"LEFT OUTER JOIN " + 
				"administracion.centros_costo ccos ON (co.centro_costo = ccos.codigo) " + 
			"LEFT OUTER JOIN " + 
				"administracion.centro_atencion caccos ON (caccos.consecutivo = ccos.centro_atencion) " +
			"WHERE 1=1 ";

		String consulta = strConsultaCitasOdontologicaXRango;
		String fechaInicio, fechaFinal;
		
		try{
			
			//********************************SE TOMAN LOS PAR�METROS*********************************
			
			if(parametros.get("codPais")!=null && !parametros.get("codPais").equals(""))
			{
				consulta+= " AND " +
						"(" +
								"caten.pais = '" + parametros.get("codPais")+"' " +
							"OR " +
								"caccos.pais = '" + parametros.get("codPais")+"' " +
						")";
			}
			 
			if(parametros.get("codCiudad")!=null && !parametros.get("codCiudad").equals(""))
			{
				consulta+= " AND " +
							"(" +
									"caten.ciudad = '" + parametros.get("codCiudad")+"' " +
								"OR " +
									"caccos.ciudad = '" + parametros.get("codCiudad")+"' " +
							")";
			}
			
			if(parametros.get("codCentroAtencion")!=null && !parametros.get("codCentroAtencion").equals(""))
			{
				consulta+= " AND " +
							"(" +
									"ao.centro_atencion  = '" + parametros.get("codCentroAtencion")+"' " +
								"OR " +
									"caccos.consecutivo  = '" + parametros.get("codCentroAtencion")+"' " +
							")";
			}
			 
			if(parametros.get("unidadAgenda")!=null && !parametros.get("unidadAgenda").equals(""))
			{
				consulta+= " AND " +
							"(" +
									"ao.unidad_agenda IN (" + parametros.get("unidadAgenda")+") " +
								"OR " +
									"ao.codigo_pk IS NULL" +
							")";
			}
			 
			if(parametros.get("profesionalSalud")!=null && !parametros.get("profesionalSalud").equals(""))
			{
				consulta+= " AND ao.codigo_medico = '" + parametros.get("profesionalSalud")+"' ";
			}
			 
			if(parametros.get("tipoCita")!=null && !parametros.get("tipoCita").equals(""))
			{
				consulta+= " AND  co.tipo = '" + parametros.get("tipoCita")+"' ";
			}
			 
			if(parametros.get("estadoCita")!=null && !parametros.get("estadoCita").equals(""))
			{
				consulta+= " AND  co.estado = '" + parametros.get("estadoCita")+"' ";
			}
			
			if(parametros.get("indicativoConfirmacion")!=null)
			{
				String[] indicativoConf=(String[]) parametros.get("indicativoConfirmacion");
				
				String cadena="";
				
				boolean existeNull=false;
				int w=0;
				for(String valorStr: indicativoConf)
				{
					if(!UtilidadTexto.isEmpty(valorStr))
					{
						if(w>0 && w<(indicativoConf.length))
						{
							cadena+=",";
						}
						cadena+= "'"+valorStr+"'";
					}
					else
					{
						existeNull=true;
					}
					w++;
				}

				
				if(indicativoConf.length>0)
				{
					consulta+= " AND (" ;
					if(!UtilidadTexto.isEmpty(cadena))
					{
						consulta+="co.confirmacion IN("+cadena+")";
						if(existeNull)
						{
							consulta+=" OR ";
						}
					}
					
					if(existeNull)
					{
						consulta+= "co.confirmacion IS NULL ";
					}
					consulta+=") ";
				}
			} 
			
			if(parametros.get("fechaInicial") != null && !parametros.get("fechaInicial").toString().equals("") && 
					parametros.get("fechaFinal") != null && !parametros.get("fechaFinal").toString().equals(""))
			{	
				fechaInicio=UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaInicial")+"");
				fechaFinal=UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaFinal")+"");
				
				consulta += " AND " +
						"(" +
								"TO_CHAR(ao.fecha,'yyyy-mm-dd') BETWEEN '"+fechaInicio+"' AND '"+fechaFinal+"' " +
							"OR " +
								"TO_CHAR(co.fecha_programacion,'yyyy-mm-dd') BETWEEN '"+fechaInicio+"' AND '"+fechaFinal+"' " +
						")";       
			}
			
			if (!UtilidadTexto.isEmpty(parametros.get("tipoCancelacion").toString())) {
				
				if (!parametros.get("tipoCancelacion").toString().trim().equals(ConstantesIntegridadDominio.acronimoAmbos)) {
					consulta += " AND " + "co.tipo_cancelacion = '" + parametros.get("tipoCancelacion").toString() +"' ";
				}else{
					consulta += " AND ( " + "co.tipo_cancelacion = '" + ConstantesIntegridadDominio.acronimoPaciente + "' "+
					"OR co.tipo_cancelacion = '"+ConstantesIntegridadDominio.acronimoInstitucion+"')";
				}
			}
			
			consulta+= " AND co.codigo_pk  NOT IN  " +
					"(SELECT caap.cita_programada FROM odontologia.citas_asociadas_a_programada caap)";
			consulta += " ORDER BY fecha_orden, hora_inicio";
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setString(1, parametros.get("usuario").toString());
			
			
			Log4JManager.info("consulta de citas por Rango: "+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoCitaOdontologica dto = new DtoCitaOdontologica();
				dto.setCodigoPk(rs.getInt("codigo_cita_odo"));
				dto.setEstado(rs.getString("estado_cita_odo"));
				dto.setTipo(rs.getString("tipo_cita_odo"));
				dto.setNombreTipo(rs.getString("nombre_tipo_cita_odo"));
				dto.setAgenda(rs.getInt("cod_agenda"));
				dto.setDuracion(rs.getInt("duracion_cita"));
				dto.setHoraInicio(rs.getString("hora_inicio"));
				dto.setHoraFinal(rs.getString("hora_fin"));
				dto.setFechaReserva(rs.getString("fecha_reser").trim());
				dto.setHoraReserva(rs.getString("hora_reser").trim());
				dto.setUsuarioReserva(rs.getString("usuario_reser").equals(" ")?"":rs.getString("usuario_reser"));
				dto.setMotivoCancelacion(rs.getInt("motivo_can_cita"));
				dto.setDescripcionMotCancelacion(rs.getString("desp_mot_cancelacion").equals(" ")?"":rs.getString("desp_mot_cancelacion"));
				dto.setPorConfirmar(rs.getString("por_confirmar"));
				dto.setUsuarioModifica(rs.getString("usu_mod"));
				dto.setCodigoPaciente(rs.getInt("cod_paciente"));
				dto.setMotivoNoAtencion(rs.getString("motivo_no_aten"));
				dto.setPermisoUsuario(rs.getString("permiso_usuario"));
				dto.setCodigoCentroCosto(rs.getInt("centro_costo"));
				dto.setFechaProgramacion(rs.getString("fecha_prog").equals(" ")?"":rs.getString("fecha_prog"));
				
				dto.setEstadoPlanTratamiento(rs.getString("estado_plan_t"));
				
				dto.setMigrado(UtilidadTexto.getBoolean(rs.getString("migrado")));
				
				dto.setConfirmacion(rs.getString("confirmacion"));
				dto.setObservacionesConfirmacion(rs.getString("observaciones_confirmacion"));
				dto.getMotivoNoConfirmacion().setCodigo(rs.getInt("motivo_no_confirmacion"));
				dto.setMotivoNoConfirmacion(new InfoDatosInt(rs.getInt("motivo_no_confirmacion"), rs.getString("desc_motivo_no_confirmacion")));
				dto.getUsuarioConfirmacion().setLoginUsuario(rs.getString("usuario_confirmacion"));
				dto.getUsuarioConfirmacion().setNombreUsuario(rs.getString("nombreusuarioconfirma"));
				dto.setFechaConfirmacion(rs.getString("fecha_confirmacion"));
				dto.setHoraConfirmacion(rs.getString("hora_confirmacion"));
				
						
				dto.setCodigoPaciente(rs.getInt("cod_paciente"));
				dto.setNombrePaciente(rs.getString("nombrespaciente"));
				dto.setTipoyNumIdentificacionPac(rs.getString("numidpaciente"));
				dto.setTipoIdentificacionPac(rs.getString("numidpaciente").split(" ")[0]);
				dto.setNumeroIdentificacionPac(rs.getString("numidpaciente").split(" ")[1]);
				dto.setCodpais(rs.getString("codpais"));
				dto.setCodciudad(rs.getString("codciudad"));
				dto.setDescripcionpais(rs.getString("nombrepais"));			
				dto.setDescripcionCiudad(rs.getString("nombreciudad"));
				dto.setCodigoCentroAtencion(rs.getInt("codigo_centro_atencion"));
				
				
				if(dto.getCodigoPk()>0){
					dto.setServiciosCitaOdon(consultarServiciosCitaOdontologica(con, dto.getCodigoPk(),Utilidades.convertirAEntero(parametros.get("institucion")+""), true, dto.getCodigoPaciente()));
					Log4JManager.info("numero de servicios encontrados: "+dto.getServiciosCitaOdon().size());
					dto.setAgendaOdon(SqlBaseAgendaOdontologicaDao.consultaAgendaOdontologica(con, dto.getAgenda(), true));
				}
								
				dto.setTipoCancelacion(rs.getString("tipo_can"));
				
				dto.getAgendaOdon().setDescripcionCentAten(rs.getString("nombre_centro_atencion"));
				if(dto.getAgendaOdon().getCodigoPk()<=0)
				{
					String consultarProfesionalCitaProgramada=
							"SELECT " +
								"getNombrePersona(m.codigo_medico) AS nombremedico " +
							"FROM " +
								"odontologia.citas_odontologicas co " +
							"INNER JOIN " +
								"administracion.usuarios u " +
									"ON(u.login=co.usuario_modifica) " +
							"INNER JOIN " +
								"administracion.medicos m " +
									"ON(m.codigo_medico=u.codigo_persona) " +
							"WHERE " +
								"co.codigo_pk=?";
					PreparedStatementDecorator psdProfesional=new PreparedStatementDecorator(con, consultarProfesionalCitaProgramada);
					psdProfesional.setInt(1, dto.getCodigoPk());
					ResultSetDecorator rsdProfesional=new ResultSetDecorator(psdProfesional.executeQuery());
					if(rsdProfesional.next())
					{
						dto.getAgendaOdon().setNombreMedico(rsdProfesional.getString("nombremedico"));
					}
					rsdProfesional.close();
					psdProfesional.close();
				}
				
				//cargar la informaci�n de la �ltima solicitud de cambio de servicio en estado solicitada.
				String consultaSolicitudCambioServicio="SELECT codigo_pk as codigo,estado as estado from odontologia.solicitud_cambio_servicio where codigo_cita=? and estado='"+ConstantesIntegridadDominio.acronimoEstadoSolicitado+"' order by codigo_pk desc";
				PreparedStatementDecorator psSCS =  new PreparedStatementDecorator(con,consultaSolicitudCambioServicio);
				psSCS.setInt(1, dto.getCodigoPk());
				ResultSetDecorator rsSCS = new ResultSetDecorator(psSCS.executeQuery());
				if(rsSCS.next())
				{
					dto.setCodigoSolicitudCambioServicio(rsSCS.getInt("codigo"));
					dto.setEstadoSolicitudCambioServicio(rsSCS.getString("estado"));
				}
				else
				{
					dto.setCodigoSolicitudCambioServicio(ConstantesBD.codigoNuncaValido);
					dto.setEstadoSolicitudCambioServicio("");
				}
				psSCS.close();
				rsSCS.close();
				
				int centroAtencion=dto.getAgendaOdon().getCentroAtencion();
				if(dto.getEstado().equals(ConstantesIntegridadDominio.acronimoProgramado))
				{
					centroAtencion=centroAtencionUsuario;
				}

				dto.cargarArregloActividades(con, dto.getAgendaOdon().getUnidadAgenda(), parametros.get("usuario").toString(), centroAtencion, dto.getCodigoPk(),dto.getCodigoSolicitudCambioServicio(),dto.getEstadoSolicitudCambioServicio());
				
				// Cargar tel�fonos persona
				String sentenciaTelefonos=
					"SELECT " +
						"p.telefono_celular AS telefono_celular, " +
						"p.telefono_fijo AS telefono_fijo " +
					"FROM " +
						"administracion.personas p " +
					"WHERE " +
						"p.codigo=?";
				PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentenciaTelefonos);
				psd.setInt(1, dto.getCodigoPaciente());
				ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
				if(rsd.next())
				{
					String telefonos=rsd.getString("telefono_fijo");
					if(!UtilidadTexto.isEmpty(telefonos))
					{
						telefonos+=" - "+rsd.getString("telefono_celular");
					}
					else
					{
						telefonos=rsd.getString("telefono_celular");
					}
					dto.setTelefonoPaciente(telefonos);
				}
				rsd.close();
				psd.close();
				array.add(dto);
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			Log4JManager.error("Error Consulta citas", e);			
		}
		return array;
	}
	
	
	/**
	 * M�todo que inserta una cita odontol�gica
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int insertarCitaOdontologica(Connection con, DtoCitaOdontologica dto)
	{
		try
		{
			int codigoPK = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_citas_odontologicas");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strInsertCitaOdontologica);
			ps.setInt(1, codigoPK);
			ps.setString(2, dto.getEstado());
		
			if(!UtilidadTexto.isEmpty(dto.getTipo()))
			{
				ps.setString(3, dto.getTipo());
			}
			else
			{
				ps.setNull(3, Types.VARCHAR );
			}
			
			
			if(dto.getAgenda()>ConstantesBD.codigoNuncaValido)
			{
				ps.setInt(4, dto.getAgenda());
				
			}
			else {
				ps.setNull(4, Types.INTEGER);
			}
			
			
			ps.setInt(5, dto.getDuracion());
			ps.setString(6, dto.getHoraInicio());
			ps.setString(7, dto.getHoraFinal());
			
			if(!dto.getFechaReserva().equals(""))
				ps.setString(8, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaReserva()));
			else
				ps.setNull(8, Types.VARCHAR);
			
			if(!dto.getHoraReserva().equals(""))
				ps.setString(9, dto.getHoraReserva());
			else
				ps.setNull(9, Types.VARCHAR);
				
			if(!dto.getUsuarioReserva().equals(""))
				ps.setString(10, dto.getUsuarioReserva());
			else
				ps.setNull(10, Types.VARCHAR);
			
			if(dto.getMotivoCancelacion()>0)
				ps.setInt(11, dto.getMotivoCancelacion());
			else
				ps.setNull(11, Types.INTEGER);
			
			if(dto.getPorConfirmar()==null || dto.getPorConfirmar().trim().equals(""))
			{
				ps.setNull(12, Types.VARCHAR);
			}
			else
			{
				ps.setString(12, dto.getPorConfirmar());
			}
			ps.setString(13, dto.getUsuarioModifica());
			ps.setInt(14, dto.getCodigoPaciente());
			
			if(!dto.getMotivoNoAtencion().equals(""))
				ps.setString(15, dto.getMotivoNoAtencion());
			else
				ps.setNull(15, Types.VARCHAR);
			
			if(dto.getCodigoCentroCosto()>0)
				ps.setInt(16, dto.getCodigoCentroCosto());
			else
				ps.setNull(16, Types.INTEGER);
			
			if(!dto.getFechaProgramacion().equals(""))
				ps.setString(17, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaProgramacion()));
			else
				ps.setNull(17, Types.VARCHAR);
			
			
			if(dto.getCodigoEvolucion()>0)
			{
				ps.setInt(18, dto.getCodigoEvolucion());
			}
			else
			{
				ps.setNull(18, Types.NUMERIC);
			}

			if(!UtilidadTexto.isEmpty(dto.getIndicativoCambioEstado()))
			{
				ps.setString(19, dto.getIndicativoCambioEstado());
			}
			else
			{
				ps.setNull(19, Types.VARCHAR);
			}

			if(!UtilidadTexto.isEmpty(dto.getTipoCancelacion()))
			{
				ps.setString(20, dto.getTipoCancelacion());
			}
			else
			{
				ps.setNull(20, Types.VARCHAR);
			}

			
			
			Log4JManager.info("Consulta: "+ps);
			
			if(ps.executeUpdate()>0)
			{
				Log4JManager.info("codigo pk a retornar: "+codigoPK);
				
				for(DtoServicioCitaOdontologica elem: dto.getServiciosCitaOdon())
				{
					elem.setCitaOdontologica(codigoPK);
					if(insertarServiciosCitaOdontologica(con, elem)<=0)
						return ConstantesBD.codigoNuncaValido;
				}
				ps.close();
				return codigoPK;
			}
			ps.close();
		}catch (Exception e) {
			Log4JManager.info("ERROR Consulta: "+strInsertCitaOdontologica, e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	
	
	
	/**
	 * insert servicios citas odontologicas
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int insertarServiciosCitaOdontologica(Connection con, DtoServicioCitaOdontologica dto)
	{
		try
		{
			int codigoPK = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_servicios_cita_odo");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strInsertarServiciosCita);
			ps.setInt(1, codigoPK);
			ps.setInt(2, dto.getCitaOdontologica());
			ps.setInt(3, dto.getServicio());
			ps.setInt(4, dto.getDuracion());
			
			if(dto.getCodigoProgramaHallazgoPieza()>0)
				ps.setInt(5, dto.getCodigoProgramaHallazgoPieza());
			else
			{
				if(dto.getProgramaHallazgoPieza().getCodigoPk()>0)
				{
					ps.setInt(5, dto.getProgramaHallazgoPieza().getCodigoPk());
				}
				else
				{
					ps.setNull(5, Types.INTEGER);
				}
			}
			
			if(dto.getNumeroSolicitud()>0)
				ps.setInt(6, dto.getNumeroSolicitud());
			else
				ps.setNull(6, Types.INTEGER);
				
			ps.setString(7, dto.getActivo());
			ps.setString(8, dto.getUsuarioModifica());
			
			if(dto.getValorTarifa()!=null && dto.getValorTarifa().doubleValue()>=0)
				ps.setDouble(9, dto.getValorTarifa().doubleValue());
			else
				ps.setNull(9, Types.DOUBLE);
			
			ps.setString(10, dto.getEstadoCita());
			
			if(dto.getCodigoAgenda()>ConstantesBD.codigoNuncaValido)
			{
				ps.setInt(11, dto.getCodigoAgenda());
			}	
			else
			{
				ps.setNull(11, Types.INTEGER) ;
			}
			
			ps.setString(12, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaCita()));
			ps.setString(13, dto.getHoraInicio());
			ps.setString(14, dto.getHoraFinal());
			
			if(dto.getUnidadAgenda()>0)
				ps.setInt(15, dto.getUnidadAgenda());
			else
				ps.setNull(15, Types.INTEGER);
			
			if(UtilidadTexto.isEmpty(dto.getAplicaAbono()))
			{
				ps.setNull(16, Types.CHAR);
			}
			else
			{
				ps.setString(16, dto.getAplicaAbono());
			}
			if(UtilidadTexto.isEmpty(dto.getAplicaAnticipo()))
			{
				ps.setNull(17, Types.CHAR);
			}
			else
			{
				ps.setString(17, dto.getAplicaAnticipo());
			}
			
			int contrato=dto.getInfoResponsableCobertura().getDtoSubCuenta().getContrato();
			if(contrato<=0)
			{
				ps.setNull(18, Types.INTEGER);
			}
			else
			{
				ps.setInt(18, contrato);
			}
			
			Log4JManager.info("Consulta: "+ps);
			
			if(ps.executeUpdate()>0){
				Log4JManager.info("codigo pk a retornar: "+codigoPK);
				ps.close();
				dto.setCodigoPk(codigoPK);
				return codigoPK;
			}
			ps.close();
		}catch (Exception e) {
			Log4JManager.error("ERROR Consulta: "+strInsertarServiciosCita, e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * M&eacute;todo para Insertar los servicios a una Pr&oacute;xima Cita
	 * 
	 * @param con
	 * @param arrayServicios
	 * @param fechaCita
	 * @param codigoPaciente
	 * @param loginUsuario
	 * @param tipoCita
	 * @param codigoEvolucion
	 * @param centroCosto
	 * @param totalDuracionCita
	 * @return
	 */
	public static int insertarProximaCitaOdontologia(Connection con,ArrayList<InfoServicios> arrayServicios,String fechaCita, int codigoPaciente, 
			String loginUsuario, String tipoCita, int codigoEvolucion, int centroCosto, int totalDuracionCita)
	{
		try
		{
			int codigoPK = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_citas_odontologicas");
			Log4JManager.info("Consulta: "+strInsertCitaOdontologica);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strInsertCitaOdontologica);
			
			ps.setInt(1, codigoPK);
			ps.setString(2, ConstantesIntegridadDominio.acronimoProgramado);
			if(tipoCita.isEmpty())
			{
				throw new RuntimeException("El tipo de cita es requerido");
			}
			else
			{
				ps.setString(3, tipoCita);
			}
			ps.setNull(4, Types.INTEGER);
			
			if(totalDuracionCita > 0){
				
				ps.setInt(5, totalDuracionCita);
				
			}else{
				
				ps.setNull(5, Types.INTEGER);
			}
			
			ps.setNull(6, Types.VARCHAR);
			ps.setNull(7, Types.VARCHAR);				
	    	ps.setNull(8, Types.DATE);			
		    ps.setNull(9, Types.VARCHAR);
			ps.setNull(10, Types.VARCHAR);			
			ps.setNull(11, Types.INTEGER);
			//ps.setNull(12, Types.INTEGER);			
			ps.setString(12, ConstantesBD.acronimoSi);
			ps.setString(13, loginUsuario);
			ps.setInt(14, codigoPaciente);
			ps.setNull(15, Types.VARCHAR);
			ps.setInt(16, centroCosto);
			ps.setDate(17, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaCita)));
			
			if(codigoEvolucion>0)
			{
				ps.setInt(18, codigoEvolucion);
			}
			else
			{
				ps.setNull(18, Types.NUMERIC);
			}
			
			// Indicativo Cambio Estado
			ps.setString(19, ConstantesIntegridadDominio.acronimoManual);

			// Tipo Cancelaci�n;
			ps.setNull(20, Types.VARCHAR);
	
			SortGenerico comparador=new SortGenerico("AyudanteCodigoProgramaHallazgoPieza", true);
			// Se ordena para estar seguros del cambio de servicio o de pieza
			Collections.sort(arrayServicios, comparador);
			
			Log4JManager.info("INSERTAR CITA ODONTOLOGICA SQL---->: \n"+ps);
			
			if(ps.executeUpdate()>0)
			{
				ArrayList<Integer> programaHallazgoPiezaAsignados=new ArrayList<Integer>();
				ArrayList<Integer> serviciosAsignados=new ArrayList<Integer>();
				Integer codigoProgramaServicioAnterior=null;
				Log4JManager.info("codigo pk a retornar: "+codigoPK);
				for(InfoServicios elem: arrayServicios)
				{					
					if(!elem.getEliminarSeleccionProxCita().equals(ConstantesBD.acronimoSi) && elem.getMsjErrorServicioCita().getNombre().equals(""))
					{		
						int codigoProgHallPieza=ConstantesBD.codigoNuncaValido;
						if(elem.getProgramaAsociado()!=null&&elem.getProgramaAsociado().getProgHallazgoPieza()!=null&&elem.getProgramaAsociado().getProgHallazgoPieza().getCodigoPk()>0)
						{
							codigoProgHallPieza=elem.getProgramaAsociado().getProgHallazgoPieza().getCodigoPk();
						}
						else
						{
							codigoProgHallPieza=UtilidadOdontologia.obtenerCodigoProgramaHallazgoPiezaProgramaSerPT(con, elem.getCodigoPkProgServ().intValue());
						}
						if(codigoProgramaServicioAnterior==null || codigoProgHallPieza!=codigoProgramaServicioAnterior)
						{
							programaHallazgoPiezaAsignados=new ArrayList<Integer>();
							serviciosAsignados=new ArrayList<Integer>();
						}
						//CUANDO SE PROGRAMA LA CITA, SE ENVIAN TODOS LOS SERVICIOS. POR ESTA RAZON SE DEBE EVALUAR SI EL CODIGO DE PROGRAMA_HALLAZGO_PIEZA YA FUE INSERTADO.
						if(
								!programaHallazgoPiezaAsignados.contains(codigoProgHallPieza)
								||
								// Si contiene el programa hallazgo pieza, pero el servicio no se ha ingresado entonces se inserta el servicio
								(programaHallazgoPiezaAsignados.contains(codigoProgHallPieza) && !serviciosAsignados.contains(elem.getServicio().getCodigo())))
						{
							long codigoRegistroServicio = insertarServiciosProximaCitaOdontologica(con, elem,codigoPK,fechaCita,loginUsuario,codigoProgHallPieza);
							
						    if(codigoRegistroServicio<=0)
						    {
						    	ps.close();
						    	return ConstantesBD.codigoNuncaValido;
						    }
						    
						    elem.setCodigoPk(codigoRegistroServicio);
						    
						    programaHallazgoPiezaAsignados.add(codigoProgHallPieza);
						    serviciosAsignados.add(elem.getServicio().getCodigo());
						    codigoProgramaServicioAnterior=codigoProgHallPieza;
						}
					}
				}
				ps.close();
				return codigoPK;
			}
			ps.close();
		}catch (Exception e) {
			Log4JManager.error("ERROR Consulta: "+strInsertCitaOdontologica, e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * Metodo para actualizar una Cita Odontologica por Reprogramaci�n 
	 * @param con
	 * @return
	 */
	public static boolean actualizarCitaOdontologicaXReprogramacion(Connection con, int codigoCita, int codAgenda, String fechaCita ,String horaInicio, String horaFin, int duracion, String usuarioModifica, String tipoCita, ArrayList<DtoServicioCitaOdontologica> arrayServicios)
	{		
		
		try
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strUpdateCitaOdontoXReprogramacion);
			ps.setInt(1, duracion);
			ps.setString(2, horaInicio);
			ps.setString(3, horaFin);
			ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(fechaCita));
			ps.setInt(5, codAgenda);
			ps.setString(6, usuarioModifica);
			ps.setString(7, tipoCita);
			ps.setInt(8, codigoCita);
		
           Log4JManager.info("Cadena Sql Reprogramar Cita: "+ps);
			
			if(ps.executeUpdate()>0){
				Log4JManager.info("codigo pk cita Actualizada: "+codigoCita);
				ps.close();
				
				  if(actualizarServiciosCitaOdont(con, codigoCita, usuarioModifica, arrayServicios)>0)
				   {
					 Log4JManager.info("Actualiza Correctamente Servicios Cita X Reprogramacion");
					  return true;	 
				   }
				
			}
			ps.close();
		
		}catch (Exception e) {
			Log4JManager.error("ERROR Consulta: "+strInsertCitaOdontologica, e);
		}
		
	  return false;	
	}
	
	
	/**
	 * Metodo para actualizar un Cita Odontologica por Reserva
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarCitaOdontologicaXReserva(Connection con, DtoCitaOdontologica dto)
	{
	   
		if(actualizarCitaOdontologica(con, dto))
		 {
			 if(actualizarServiciosCitaOdont(con, dto.getCodigoPk(),dto.getUsuarioModifica(), dto.getServiciosCitaOdon())>0)
			   {
				 Log4JManager.info("Actualiza Correctamente Servicios Cita X Reserva");
				  return true;	 
			   }
		 }
		return false;
	}

	/**
	 * Metodo para actualizar los servicios de un Cita Odontologica (Elimina los servicios antiguos e inserta los servicios Nuevos)
	 * @param con
	 * @param codigoCita
	 * @param arrayServicios
	 * @return
	 */
	private static int actualizarServiciosCitaOdont(Connection con, int codigoCita, String usuario,ArrayList<DtoServicioCitaOdontologica> arrayServicios) {
		
		int resp=1;		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strInactivarServiciosCitaOdont);
			ps.setString(1, usuario);
			ps.setInt(2, codigoCita);			    
			Log4JManager.info("Inactivacion Servicios Cita >> "+ps);
			ps.executeUpdate();
			ps.close();
			
			Log4JManager.info("\n\n******Inactiva Servicios");
			if(arrayServicios.size()>0)
			{	
				for(DtoServicioCitaOdontologica elem: arrayServicios)
				{
					elem.setCitaOdontologica(codigoCita);
					int codigoPHP=elem.getProgramaHallazgoPieza().getCodigoPk()>0?elem.getProgramaHallazgoPieza().getCodigoPk():elem.getCodigoProgramaHallazgoPieza();
					if(!existeServicio(elem.getServicio(), codigoCita,codigoPHP))
					{
							Log4JManager.info("\n\n******** NO EXISTE servicio Entra a insertar Servicio");
							if(insertarServiciosCitaOdontologica(con, elem)<=0)
							{
								return 0;
							}
					}else
					{
						Log4JManager.info("\n\n ******* Existe Servicio Entra a Actualizar Servicio");
						if(actualizarServicioExistCitaOdonto(con, elem)<=0)
						{							    	
							return 0;
						}
						if(elem.getNumeroSolicitud()>0)
						{
							if(!actualizarServicioCitaOdontologica(con, elem))
							{
								return 0;
							}
						}
					}
				}
			}
			return resp;
		}catch (Exception e) {
			Log4JManager.error("ERROR Insertando Servicios Cita: "+strInsertarServiciosCita, e);
			resp=0;
		}
		 
		return resp;
	}

	
	/**
	 * Metodo para actulizar servicios que estaban asociados a la cita
	 * @param con
	 * @param elem
	 * @return
	 */
	private static int actualizarServicioExistCitaOdonto(Connection con,DtoServicioCitaOdontologica dto) 
	{
		int resp=0;		
		try
		{
			String consulta=strUpdateServicioExistenteCitaOdonto;
			int codigoPHP=dto.getProgramaHallazgoPieza().getCodigoPk()>0?dto.getProgramaHallazgoPieza().getCodigoPk():dto.getCodigoProgramaHallazgoPieza();
			if(codigoPHP>0)
				consulta=consulta+" and programa_hallazgo_pieza="+codigoPHP;
			else
				consulta=consulta+" and programa_hallazgo_pieza is null";
			
		    PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
		    ps.setInt(1, dto.getDuracion());
			ps.setString(2, dto.getUsuarioModifica());
			ps.setString(3, dto.getEstadoCita());
			ps.setInt(4, dto.getCodigoAgenda());
			ps.setString(5, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaCita()));
			ps.setString(6, dto.getHoraInicio());
			ps.setString(7, dto.getHoraFinal());
			if(dto.getUnidadAgenda()>0)
				ps.setInt(8, dto.getUnidadAgenda());
			else
				ps.setNull(8, Types.INTEGER);
			ps.setInt(9, dto.getServicio());
			ps.setInt(10, dto.getCitaOdontologica());
			
			Log4JManager.info("Cadena Actualizacion Servicio"+ps);
			 if(ps.executeUpdate()>0)
		     {
				 resp=1;
		     }
			 ps.close();
		     	
		}
	    catch (Exception e) {
		  Log4JManager.error("ERROR Actualizando Servicios Cita: "+strUpdateServicioExistenteCitaOdonto, e);
		  resp=0;
	     }
	    
		return resp;
	}


	/**
	 * Metodo para consultar si un servicio ya se encuentra asociado a una cita
	 * @param codigoServicio
	 * @param codigoCita
	 * @return
	 */
	private static boolean existeServicio(int codigoServicio,int codigoCita,int codigoPHP)
	{
		String consulta = "SELECT servicio FROM odontologia.servicios_cita_odontologica WHERE servicio = ? AND cita_odontologica = ? " ;
		if(codigoPHP>0)
			consulta=consulta+" and programa_hallazgo_pieza ="+codigoPHP;
		else
			consulta=consulta+" and programa_hallazgo_pieza is null";
		boolean existe= false;

		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			ps.setInt(1, codigoServicio);
			ps.setInt(2, codigoCita);
			Log4JManager.info("Consulta Existe Servicio: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				existe= true;
			}
			UtilidadBD.closeConnection(con);
		}
		catch (Exception e) 
		{
			Log4JManager.error("ERROR Consultado Servicios Cita: "+consulta, e);
		}
		return existe;
	}

	/**
	 * Metodo para insertar un servicio a una Proxima Cita
	 * @param con
	 * @param servicio
	 * @param codigoCitaOdont
	 * @param fechaCita
	 * @param loginUsuario
	 * @param codigoProgHallPieza 
	 * @return
	 */
	public static int insertarServiciosProximaCitaOdontologica(Connection con, InfoServicios servicio, int codigoCitaOdont,String fechaCita, String loginUsuario, int codigoProgHallPieza)
	{
		try
		{
			int codigoPK = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_servicios_cita_odo");
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strInsertarServiciosCita);
			ps.setInt(1, codigoPK);
			ps.setInt(2, codigoCitaOdont);
			ps.setInt(3,servicio.getServicio().getCodigo());
			if(servicio.getDuracionCita()<=0)
			{
				ps.setNull(4,Types.INTEGER);
			}
			else
			{
				ps.setInt(4, servicio.getDuracionCita());
			}
			
		
			if(codigoProgHallPieza > 0)
			   ps.setInt(5, codigoProgHallPieza);
	         else
				ps.setNull(5, Types.NUMERIC);
			
			ps.setNull(6, Types.INTEGER);
				
			ps.setString(7,ConstantesBD.acronimoSi);
			ps.setString(8, loginUsuario);
			ps.setNull(9, Types.DOUBLE);
			ps.setString(10, ConstantesIntegridadDominio.acronimoProgramado);
			ps.setNull(11, Types.NUMERIC);
			ps.setString(12, UtilidadFecha.conversionFormatoFechaABD(fechaCita));
			ps.setNull(13, Types.VARCHAR);
			ps.setNull(14, Types.VARCHAR);
				
			ps.setNull(15, Types.INTEGER);
			
			ps.setString(16, ConstantesBD.acronimoNo);
			ps.setString(17, ConstantesBD.acronimoNo);
		
			
			if(servicio.getContrato()>0)
			{	
				ps.setInt(18, servicio.getContrato());
			}
			else
			{
				ps.setNull(18, Types.INTEGER);
			}
			
			
			Log4JManager.info(" INSERTAR SERVICIO DE LA CITA :  \n\n\n"+ps.toString());
			
			if(ps.executeUpdate()>0){
				Log4JManager.info("codigo pk a retornar: "+codigoPK);
				ps.close();
				
				return codigoPK;
			}
			ps.close();
		}catch (Exception e) {
			Log4JManager.error("ERROR Insertando Servicios Proxima Cita: "+strInsertarServiciosCita, e);
		}
		return ConstantesBD.codigoNuncaValido;
		
	}
	
	
	/**
	 * actualizar servicios citas odontologicas
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarServicioCitaOdontologica(Connection con, DtoServicioCitaOdontologica dto)
	{
		try
		{
			String consulta="";
			if(dto.getCodigoPk()>0)
			{
				consulta=strActualizarServicioCitaOdon;
			}
			else
			{
				consulta=strActualizarServicioCitaOdonSinPk;
				int codigoPHP=dto.getProgramaHallazgoPieza().getCodigoPk()>0?dto.getProgramaHallazgoPieza().getCodigoPk():dto.getCodigoProgramaHallazgoPieza();
				if(codigoPHP>0)
					consulta=consulta+" and programa_hallazgo_pieza="+codigoPHP;
				else
					consulta=consulta+" and programa_hallazgo_pieza is null";
			}
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			if(dto.getNumeroSolicitud()>0)
				ps.setInt(1, dto.getNumeroSolicitud());
			else
				ps.setNull(1, Types.INTEGER);
			
			if(dto.getValorTarifa()!=null && dto.getValorTarifa().doubleValue()>=0)
				ps.setDouble(2, dto.getValorTarifa().doubleValue());
			else
				ps.setNull(2, Types.DOUBLE);
			
			ps.setString(3, dto.getAplicaAbono());
			ps.setString(4, dto.getAplicaAnticipo());
			ps.setString(5, dto.getUsuarioModifica());

			if(dto.getCodigoPk()>0)
			{
				ps.setInt(6, dto.getCodigoPk());
			}
			else
			{
				ps.setInt(6, dto.getCitaOdontologica());
				ps.setInt(7, dto.getServicio());
			}
			
			Log4JManager.info(ps);
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
		}catch (Exception e) {
			Log4JManager.error("Error Consulta: "+strActualizarServicioCitaOdon, e);
		}
		return false;
	}
	
	/**
	 * Consulta una cita espec�fica
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public static DtoCitaOdontologica obtenerCitaOdontologica(Connection con, int codigoCita, int codInstitucion)
	{
		DtoCitaOdontologica dto = new DtoCitaOdontologica(); 
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strConsultaCitaOdon);
			ps.setInt(1, codigoCita);
			Log4JManager.info("Consulta: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				dto.setCodigoPk(rs.getInt("cod_cita"));
				dto.setEstado(rs.getString("est_cita"));
				dto.setTipo(rs.getString("tipo_cita"));
				dto.setAgenda(rs.getInt("agenda"));
				dto.setDuracion(rs.getInt("duracion_cita"));
				dto.setHoraInicio(rs.getString("hora_ini_cita"));
				dto.setHoraFinal(rs.getString("hora_fin_cita"));
				dto.setFechaReserva(rs.getString("fecha_res_cit").equals(" ")?"":rs.getString("fecha_res_cit"));
				dto.setHoraReserva(rs.getString("hora_res_cit").equals(" ")?"":rs.getString("hora_res_cit"));
				dto.setUsuarioReserva(rs.getString("usu_res_cit").equals(" ")?"":rs.getString("usu_res_cit"));
				dto.setMotivoCancelacion(rs.getInt("mot_can_cit"));
				dto.setPorConfirmar(rs.getString("por_confir_cit"));
				dto.setCodigoPaciente(rs.getInt("cod_paciente_cit"));
				dto.setMotivoNoAtencion(rs.getString("mot_no_aten"));
				dto.setCodigoCentroCosto(rs.getInt("centro_costo"));
				dto.setFechaProgramacion(rs.getString("fecha_prog"));
				dto.setConfirmacion(rs.getString("confirmacion"));
				dto.setFechaConfirmacion(rs.getString("fecha_confirmacion"));
				dto.setHoraConfirmacion(rs.getString("hora_confirmacion"));
				dto.setObservacionesConfirmacion(rs.getString("observaciones_confirmacion"));
				dto.setMotivoNoConfirmacion(new InfoDatosInt(rs.getInt("motivo_no_confirmacion")));

				
				if(dto.getCodigoPk()>0)
				{
					// se carga los servicios de la cita odontologica
					dto.setServiciosCitaOdon(obtenerServiciosCitaOdon(con, dto.getCodigoPk(), codInstitucion));
					if(dto.getAgenda()>0)
					{
						// se cargan los datos de la agenda odontologica asociada a la cita odontologica
						dto.setAgendaOdon(SqlBaseAgendaOdontologicaDao.consultaAgendaOdontologica(con, dto.getAgenda(), false));
					}
				}
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			Log4JManager.error("Error Consulta: "+strConsultaCitaOdon, e);
		}
		return dto;
	}
	
	/**
	 * Consulta los servicios asociados a una cita espec�fica
	 * @param con Conexi�n con la BD
	 * @param codigoCita C�digo de la cita
	 * @return Lista de servicios de la cita odontol�gica
	 */
	public static ArrayList<DtoServicioCitaOdontologica> obtenerServiciosCitaOdon(Connection con, int codigoCita, int codInstitucion)
	{
		ArrayList<DtoServicioCitaOdontologica> array = new ArrayList<DtoServicioCitaOdontologica>(); 
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strConsultaServiciosCitaOdo);
			ps.setInt(1, Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codInstitucion)));
			ps.setInt(2, codigoCita);
			Log4JManager.info("Consulta: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoServicioCitaOdontologica dto = new DtoServicioCitaOdontologica();
				dto.setCodigoPk(rs.getInt("cod_ser_cit_odo"));				 
				dto.setCitaOdontologica(rs.getInt("cod_cit_odo"));
				dto.setServicio(rs.getInt("cod_servicio"));
				dto.setDuracion(rs.getInt("duracion_ser"));
				dto.setCodigoProgramaHallazgoPieza(rs.getInt("cod_php"));
				dto.setNumeroSolicitud(rs.getInt("solicitud"));
				dto.setActivo(rs.getString("activo"));
				dto.setValorTarifa(rs.getBigDecimal("valor_tarifa"));
				dto.setEstadoCita(rs.getString("est_cita"));
				dto.setCodigoAgenda(rs.getInt("cod_agen_odo"));
				dto.setFechaCita(rs.getString("fecha_cit"));
				dto.setHoraInicio(rs.getString("hora_ini_cit"));
				dto.setHoraFinal(rs.getString("hora_fin_cit"));
				dto.setUnidadAgenda(rs.getInt("cod_uni_consulta"));
				dto.setAplicaAbono(rs.getString("aplica_abono"));
				dto.setAplicaAnticipo(rs.getString("aplica_anticipo"));
				dto.setFacturado(rs.getString("facturado"));
				dto.setNombreServicio(rs.getString("descripcion_servicio"));
				
				dto.setServiciosArticulosIncluidos(obtenerServiciosArticulosIncluidos(con, dto.getCodigoPk()));
				
				array.add(dto);
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			Log4JManager.error("Error Consulta: "+strConsultaServiciosCitaOdo, e);
		}
		return array;
	}
	
	/**
	 * M�todo para obtener los servicios / art�culos inclu�dos de un servicio de cita odontol�gica
	 * @param con
	 * @param codigoServCita
	 * @return
	 */
	private static ArrayList<DtoServArtIncCitaOdo> obtenerServiciosArticulosIncluidos(Connection con,int codigoServCita)
	{
		ArrayList<DtoServArtIncCitaOdo> serviciosArticulosIncluidos = new ArrayList<DtoServArtIncCitaOdo>();
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,strConsultaServiciosArticulosIncluidos);
			pst.setInt(1,codigoServCita);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoServArtIncCitaOdo servicioArticulo = new DtoServArtIncCitaOdo();
				servicioArticulo.setCodigo_pK(rs.getBigDecimal("codigo_pk"));
				servicioArticulo.setServicioCitaOdo(rs.getBigDecimal("servicio_cita_odo"));
				servicioArticulo.getServicio().setCodigo(rs.getInt("codigo_servicio"));
				servicioArticulo.getServicio().setNombre(rs.getString("nombre_servicio"));
				servicioArticulo.getArticulo().setCodigo(rs.getInt("codigo_articulo"));
				servicioArticulo.getArticulo().setNombre(rs.getString("nombre_articulo"));
				servicioArticulo.setCantidad(rs.getInt("cantidad"));
				servicioArticulo.getSolicitud().setCodigo(rs.getInt("solicitud"));
				
				serviciosArticulosIncluidos.add(servicioArticulo);
			}
			rs.close();
			pst.close();
			
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error en obtenerServiciosArticulosIncluidos: ",e);
		}
		return serviciosArticulosIncluidos;
	}
	
	/**
	 * Insert de log de citas odontol�gicas
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int insertLogCitaOdontologica(Connection con, DtoCitaOdontologica dto)
	{
		try
		{
			DtoCitaOdontologica dtolog = new DtoCitaOdontologica();
			dtolog = obtenercitaOdoLog(con, dto.getCodigoPk());
			if(dtolog.getCodigoPk()>0)
			{
				int codigoPK = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_log_citas_odo");
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strInsertLogCitaOdontologica);
				ps.setInt(1, codigoPK);
				ps.setInt(2, dto.getCodigoPk());
				ps.setString(3, dtolog.getEstado());
				ps.setInt(4, dtolog.getAgenda());
				ps.setString(5, dtolog.getHoraInicio());
				ps.setString(6, dtolog.getHoraFinal());
				
				if(dtolog.getMotivoCancelacion()>0)
					ps.setInt(7, dtolog.getMotivoCancelacion());
				else
					ps.setNull(7, Types.INTEGER);
				
				
				
				if(dtolog.getPorConfirmar()==null || dtolog.getPorConfirmar().trim().equals(""))
				{
					ps.setNull(8, Types.VARCHAR);	
				}
				else
				{
					ps.setString(8, dtolog.getPorConfirmar());
				}

				
				ps.setString(9, dto.getUsuarioModifica());
				 
				if(!dtolog.getMotivoNoAtencion().equals(""))
					ps.setString(10, dtolog.getMotivoNoAtencion());
				else
					ps.setNull(10, Types.VARCHAR);
				
				if(!dtolog.getFechaProgramacion().equals(""))
					ps.setString(11, dtolog.getFechaProgramacion());
				else
					ps.setNull(11, Types.VARCHAR);
				
				
				if(!dtolog.getConfirmacion().equals(""))
				{
					ps.setString(12,dtolog.getConfirmacion());
				}
				else
				{
					ps.setNull(12,Types.VARCHAR);
				}
				if(!dtolog.getObservacionesConfirmacion().trim().equals(""))
				{
					ps.setString(13,dtolog.getObservacionesConfirmacion());
				}
				else
				{
					ps.setNull(13,Types.VARCHAR);
				}
				if(dtolog.getMotivoNoConfirmacion().getCodigo()>0)
				{
					ps.setInt(14,dtolog.getMotivoNoConfirmacion().getCodigo());
				}
				else
				{
					ps.setNull(14,Types.INTEGER);
				}
				
				if(!dtolog.getUsuarioConfirmacion().getLoginUsuario().equals(""))
				{
					ps.setString(15,dtolog.getUsuarioConfirmacion().getLoginUsuario());
				}
				else
				{
					ps.setNull(15,Types.VARCHAR);
				}
				
				if(!dtolog.getFechaConfirmacion().equals(""))
				{
					ps.setDate(16,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dtolog.getFechaConfirmacion())));
				}
				else
				{
					ps.setNull(16,Types.DATE);
				}
				
				if(!dtolog.getHoraConfirmacion().equals(""))
				{
					ps.setString(17,dtolog.getHoraConfirmacion());
				}
				else
				{
					ps.setNull(17,Types.VARCHAR);
				}
				
				ps.setInt(18,dto.getCodigoPaciente());
				Log4JManager.info("Consulta: "+ps);
				
				if(ps.executeUpdate()>0)
				{
					Log4JManager.info("codigo pk a retornar: "+codigoPK);
					ps.close();
					return codigoPK;
				}
				ps.close();
			}else
				return ConstantesBD.codigoNuncaValido;
		}catch (Exception e) {
			Log4JManager.error("ERROR Consulta: "+strInsertCitaOdontologica, e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Inserta el log servicios cita odontol�gica
	 * @param con Conexi�n con la BD
	 * @param dto DTO con los datos del servicio de la cita
	 * @return n�mero mayor que 0 en caso de una inserci�n correcta
	 */
	public static int insertLogServiciosCitaOdon (Connection con, DtoServicioCitaOdontologica dto)
	{
		try
		{
			DtoServicioCitaOdontologica dtoLog = new DtoServicioCitaOdontologica();
			dtoLog = obtenerServiciosCitaOdoLog(con, dto.getCodigoPk());
			if(dtoLog.getCodigoPk()>0)
			{
				int codigoPK = UtilidadBD.obtenerSiguienteValorSecuencia(con, "odontologia.seq_log_serv_cita_odo");
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strInsertLogServiciosCitaOdontologica);
				ps.setInt(1, codigoPK);
				ps.setInt(2, dto.getCodigoPk());
				ps.setInt(3, dtoLog.getServicio());
				ps.setInt(4, dtoLog.getDuracion());
				
				if(dtoLog.getCodigoProgramaHallazgoPieza()>0)
					ps.setInt(5, dtoLog.getCodigoProgramaHallazgoPieza());
				else
					ps.setNull(5, Types.INTEGER);
				 
				if(dtoLog.getNumeroSolicitud()>0)
					ps.setInt(6, dtoLog.getNumeroSolicitud());
				else
					ps.setNull(6, Types.INTEGER);
				 
				ps.setString(7, dtoLog.getActivo());
				ps.setString(8, dto.getUsuarioModifica());
				
				if(dtoLog.getValorTarifa()!=null && dtoLog.getValorTarifa().doubleValue()>=0)
					ps.setDouble(9, dtoLog.getValorTarifa().doubleValue());
				else
					ps.setNull(9, Types.DOUBLE);
				
				ps.setString(10, dtoLog.getEstadoCita());
				ps.setInt(11, dtoLog.getCodigoAgenda());Log4JManager.info("dtoLog.getFechaCita()"+dtoLog.getFechaCita());
				ps.setString(12, UtilidadFecha.conversionFormatoFechaABD(dtoLog.getFechaCita().substring(0,10)));
				ps.setString(13, dtoLog.getHoraInicio());
				ps.setString(14, dtoLog.getHoraFinal());
				
				if(dtoLog.getUnidadAgenda()>0)
					ps.setInt(15, dtoLog.getUnidadAgenda());
				else
					ps.setNull(15, Types.INTEGER);
				
				if(UtilidadTexto.isEmpty(dtoLog.getAplicaAbono()))
					ps.setObject(16, null);
				else
					ps.setString(16, dtoLog.getAplicaAbono());
				if(UtilidadTexto.isEmpty(dtoLog.getAplicaAnticipo()))
					ps.setObject(17, null);
				else
					ps.setString(17, dtoLog.getAplicaAnticipo());
				 
				Log4JManager.info("Consulta: "+ps);
				
				if(ps.executeUpdate()>0)
				{
					Log4JManager.info("codigo pk a retornar: "+codigoPK);
					ps.close();
					return codigoPK;
				}
				ps.close();
			}else{
				return ConstantesBD.codigoNuncaValido;
			}
		}catch (Exception e) {
			Log4JManager.error("ERROR Consulta: "+strInsertLogServiciosCitaOdontologica, e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * M�todo que inactiva el servicio de una cita odontol�gica
	 * @param con
	 * @param usuario
	 * @param codigoPk
	 * @return
	 */
	public static boolean inactivarServicioCitaOdontologica(Connection con, String usuario, int codigoPk)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strInactivarServicioCitaOdontologica);
			ps.setString(1, usuario);
			ps.setInt(2, codigoPk);
			Log4JManager.info("Consulta: "+ps);
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			ps.close();
		}catch (Exception e) {
			Log4JManager.error("Error Consulta: "+strInactivarServicioCitaOdontologica, e);
		}
		return false;
	}
	
	/**
	 * M�todo que consulta cita para el log
	 * @param con
	 * @param codigoPk
	 * @return
	 */
	public static DtoCitaOdontologica obtenercitaOdoLog(Connection con, int codigoPk)
	{
		DtoCitaOdontologica dto = new DtoCitaOdontologica(); 
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strConsultaCitaOdontologica);
			ps.setInt(1, codigoPk);
			//Log4JManager.info("Consulta: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				dto.setCodigoPk(rs.getInt("codigo_pk"));				 
				dto.setEstado(rs.getString("estado"));
				dto.setAgenda(rs.getInt("agenda"));
				dto.setHoraInicio(rs.getString("hora_inicio"));
				dto.setHoraFinal(rs.getString("hora_fin"));
				dto.setMotivoCancelacion(rs.getInt("mot_cancelacion"));
				dto.setPorConfirmar(rs.getString("por_confirmar"));
				dto.setMotivoNoAtencion(rs.getString("mot_no_aten").equals(" ")?"":rs.getString("mot_no_aten"));
				dto.setCodigoCentroCosto(rs.getInt("centro_costo"));
				dto.setFechaProgramacion(rs.getString("fecha_prog").equals(" ")?"":rs.getString("fecha_prog"));
				dto.setConfirmacion(rs.getString("confirmacion"));
				dto.setObservacionesConfirmacion(rs.getString("observaciones_confirmacion"));
				dto.getMotivoNoConfirmacion().setCodigo(rs.getInt("motivo_no_confirmacion"));
				dto.getUsuarioConfirmacion().setLoginUsuario(rs.getString("usuario_confirmacion"));
				dto.setFechaConfirmacion(rs.getString("fecha_confirmacion"));
				dto.setHoraConfirmacion(rs.getString("hora_confirmacion"));
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			Log4JManager.error("Error Consulta: "+strConsultaCitaOdontologica, e);
		}
		return dto;
	}
	
	/**
	 * M�todo que consulta servicios cita odon para log	
	 * @param con
	 * @param codigoPk
	 * @return
	 */
	public static DtoServicioCitaOdontologica obtenerServiciosCitaOdoLog(Connection con, int codigoPk)
	{
		DtoServicioCitaOdontologica dto = new DtoServicioCitaOdontologica(); 
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strConsultaServiciosCitaOdoLog);
			ps.setInt(1, codigoPk);
			Log4JManager.info("Consulta: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				dto.setCodigoPk(rs.getInt("codigo_pk"));				 
				dto.setCitaOdontologica(rs.getInt("cita_odontologica"));
				dto.setServicio(rs.getInt("servicio"));
				dto.setDuracion(rs.getInt("duracion"));
				dto.setCodigoProgramaHallazgoPieza(rs.getInt("php"));
				dto.setNumeroSolicitud(rs.getInt("solicitud"));
				dto.setActivo(rs.getString("activo"));
				dto.setValorTarifa(rs.getBigDecimal("valor_tarifa"));
				dto.setEstadoCita(rs.getString("estado_cita"));
				dto.setCodigoAgenda(rs.getInt("codigo_agenda"));
				dto.setFechaCita(rs.getString("fecha_cita"));
				dto.setHoraInicio(rs.getString("hora_inicio"));
				dto.setHoraFinal(rs.getString("hora_fin"));
				dto.setUnidadAgenda(rs.getInt("unid_agenda"));
				dto.setAplicaAbono(rs.getString("aplica_abono"));
				dto.setAplicaAnticipo(rs.getString("aplica_anticipo"));
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			Log4JManager.error("Error Consulta: "+strConsultaServiciosCitaOdoLog, e);
		}
		return dto;
	}
	
	/**
	 * Actualizar cita odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarCitaOdontologica(Connection con, DtoCitaOdontologica dto)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strUpdateCitaOdontologica);
			ps.setString(1, dto.getEstado());
			ps.setString(2, dto.getTipo());
			ps.setInt(3, dto.getAgenda());
			ps.setInt(4, dto.getDuracion());
			ps.setString(5, dto.getHoraInicio());
			ps.setString(6, dto.getHoraFinal());
			
			if(!dto.getFechaReserva().equals(""))
				ps.setString(7, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaReserva()));
			else
				ps.setNull(7, Types.VARCHAR);
			
			if(!dto.getHoraReserva().equals(""))
				ps.setString(8, dto.getHoraReserva());
			else
				ps.setNull(8, Types.VARCHAR);
			
			if(!dto.getUsuarioReserva().equals(""))
				ps.setString(9, dto.getUsuarioReserva());
			else
				ps.setNull(9, Types.VARCHAR);
			
			if(dto.getMotivoCancelacion()>0)
				ps.setInt(10, dto.getMotivoCancelacion());
			else
				ps.setNull(10, Types.INTEGER);
			
			if(dto.getPorConfirmar()==null || dto.getPorConfirmar().equals(""))
				ps.setNull(11, Types.VARCHAR);
			else
				ps.setString(11, dto.getPorConfirmar());
			ps.setString(12, dto.getUsuarioModifica());
			ps.setInt(13, dto.getCodigoPaciente());
			
			if(!dto.getMotivoNoAtencion().equals(""))
				ps.setString(14, dto.getMotivoNoAtencion());
			else
				ps.setNull(14, Types.VARCHAR);
			
			if(dto.getCodigoCentroCosto()>0)
				ps.setInt(15, dto.getCodigoCentroCosto());
			else
				ps.setNull(15, Types.INTEGER);
			
			if(!dto.getFechaProgramacion().equals(""))
				ps.setString(16, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaProgramacion()));
			else
				ps.setNull(16, Types.VARCHAR);
			
			if(!dto.getUsuarioRegistra().getLoginUsuario().equals(""))
			{
				ps.setString(17,dto.getUsuarioRegistra().getLoginUsuario());
				ps.setDate(18,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
				ps.setString(19,UtilidadFecha.getHoraActual(con));
			}
			else
			{
				ps.setNull(17,Types.VARCHAR);
				ps.setNull(18,Types.DATE);
				ps.setNull(19,Types.VARCHAR);
			}
			
			
			if(!dto.getConfirmacion().equals(""))
			{
				ps.setString(20,dto.getConfirmacion());
			}
			else
			{
				ps.setNull(20,Types.VARCHAR);
			}
			
			if(!dto.getObservacionesConfirmacion().trim().equals(""))
			{
				ps.setString(21,dto.getObservacionesConfirmacion());
			}
			else
			{
				ps.setNull(21,Types.VARCHAR);
			}
			if(dto.getMotivoNoConfirmacion().getCodigo()>0)
			{
				ps.setInt(22,dto.getMotivoNoConfirmacion().getCodigo());
			}
			else
			{
				ps.setNull(22,Types.INTEGER);
			}
			
			if(!dto.getUsuarioConfirmacion().getLoginUsuario().equals(""))
			{
				ps.setString(23,dto.getUsuarioConfirmacion().getLoginUsuario());
			}
			else
			{
				ps.setNull(23,Types.VARCHAR);
			}
			
			if(!dto.getFechaConfirmacion().trim().equals(""))
			{
				ps.setDate(24,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaConfirmacion())));
			}
			else
			{
				ps.setNull(24,Types.DATE);
			}
			
			if(!dto.getHoraConfirmacion().equals(""))
			{
				ps.setString(25,dto.getHoraConfirmacion());
			}
			else
			{
				ps.setNull(25,Types.VARCHAR);
			}
			
			ps.setInt(26, dto.getCodigoPk());
			
			Log4JManager.info("actualizar cita odo: "+ps);
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
		}catch (Exception e) {
			Log4JManager.error("Error Consulta: "+strUpdateCitaOdontologica, e);
		}
		return false;
	}

	/**
	 * Actualizar cita odontologica
	 * Cuando es cambio de servicio no se debe actualizar la fecha_modific y hora_modifica
	 * ya que se est� tomando como la hora de la asignaci�n 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarCitaOdontologicaCambioServicio(Connection con, DtoCitaOdontologica dto)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strUpdateCitaOdontologicaCambioServicio);
			ps.setString(1, dto.getEstado());
			ps.setString(2, dto.getTipo());
			ps.setInt(3, dto.getAgenda());
			ps.setInt(4, dto.getDuracion());
			ps.setString(5, dto.getHoraInicio());
			ps.setString(6, dto.getHoraFinal());
			
			if(!dto.getFechaReserva().equals(""))
				ps.setString(7, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaReserva()));
			else
				ps.setNull(7, Types.VARCHAR);
			
			if(!dto.getHoraReserva().equals(""))
				ps.setString(8, dto.getHoraReserva());
			else
				ps.setNull(8, Types.VARCHAR);
			
			if(!dto.getUsuarioReserva().equals(""))
				ps.setString(9, dto.getUsuarioReserva());
			else
				ps.setNull(9, Types.VARCHAR);
			
			if(dto.getMotivoCancelacion()>0)
				ps.setInt(10, dto.getMotivoCancelacion());
			else
				ps.setNull(10, Types.INTEGER);
			
			if(dto.getPorConfirmar()==null || dto.getPorConfirmar().equals(""))
				ps.setNull(11, Types.VARCHAR);
			else
				ps.setString(11, dto.getPorConfirmar());
			ps.setString(12, dto.getUsuarioModifica());
			ps.setInt(13, dto.getCodigoPaciente());
			
			if(!dto.getMotivoNoAtencion().equals(""))
				ps.setString(14, dto.getMotivoNoAtencion());
			else
				ps.setNull(14, Types.VARCHAR);
			
			if(dto.getCodigoCentroCosto()>0)
				ps.setInt(15, dto.getCodigoCentroCosto());
			else
				ps.setNull(15, Types.INTEGER);
			
			if(!dto.getFechaProgramacion().equals(""))
				ps.setString(16, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaProgramacion()));
			else
				ps.setNull(16, Types.VARCHAR);
			
			if(!dto.getUsuarioRegistra().getLoginUsuario().equals(""))
			{
				ps.setString(17,dto.getUsuarioRegistra().getLoginUsuario());
				ps.setDate(18,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con))));
				ps.setString(19,UtilidadFecha.getHoraActual(con));
			}
			else
			{
				ps.setNull(17,Types.VARCHAR);
				ps.setNull(18,Types.DATE);
				ps.setNull(19,Types.VARCHAR);
			}
			
			
			if(!dto.getConfirmacion().equals(""))
			{
				ps.setString(20,dto.getConfirmacion());
			}
			else
			{
				ps.setNull(20,Types.VARCHAR);
			}
			
			if(!dto.getObservacionesConfirmacion().trim().equals(""))
			{
				ps.setString(21,dto.getObservacionesConfirmacion());
			}
			else
			{
				ps.setNull(21,Types.VARCHAR);
			}
			if(dto.getMotivoNoConfirmacion().getCodigo()>0)
			{
				ps.setInt(22,dto.getMotivoNoConfirmacion().getCodigo());
			}
			else
			{
				ps.setNull(22,Types.INTEGER);
			}
			
			if(!dto.getUsuarioConfirmacion().getLoginUsuario().equals(""))
			{
				ps.setString(23,dto.getUsuarioConfirmacion().getLoginUsuario());
			}
			else
			{
				ps.setNull(23,Types.VARCHAR);
			}
			
			if(!dto.getFechaConfirmacion().trim().equals(""))
			{
				ps.setDate(24,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(dto.getFechaConfirmacion())));
			}
			else
			{
				ps.setNull(24,Types.DATE);
			}
			
			if(!dto.getHoraConfirmacion().equals(""))
			{
				ps.setString(25,dto.getHoraConfirmacion());
			}
			else
			{
				ps.setNull(25,Types.VARCHAR);
			}
			
			ps.setInt(26, dto.getCodigoPk());
			
			Log4JManager.info("actualizar cita odo: "+ps);
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
		}catch (Exception e) {
			Log4JManager.error("Error Consulta: "+strUpdateCitaOdontologica, e);
		}
		return false;
	}

	/**
	 * metodo que vverifica si al menos un servicios asociado a una cita esta facturado
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public static boolean almenosUnServicioFacturado(Connection con, int codigoCita)
	{
		boolean result = false;
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strConsultaServicioFacturado);
			ps.setInt(1, codigoCita);
			Log4JManager.info("Consulta: "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				if((result=UtilidadTexto.getBoolean(rs.getString("facturado")))==true)
				{
					break;
				}
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			Log4JManager.info("Error Consulta: "+strConsultaServiciosCitaOdoLog, e);
		}
		return result;
	}
	
	/**
	 * Metodo q retorna arraylist de citas segun parametros de busqueda funcionaluidad consulta externa/consultar citas
	 * @param connection
	 * @param codigoPaciente
	 * @param fechaHoraInicial
	 * @param fechaHorafinal
	 * @param centroAtencion
	 * @param unidadConsulta
	 * @param estadosCita
	 * @param consultorio
	 * @param tipoCita
	 * @param profesionalSalud
	 * @return
	 */
	public static ArrayList<DtoCitaOdontologica> consultaCitaOdontologicaConsExt (Connection connection,int codigoPaciente, String fechaHoraInicial, 
			String fechaHorafinal, String centroAtencion, int unidadConsulta, String[] estadosCita, int consultorio, String tipoCita, 
			int profesionalSalud, int tipoBD, int institucion){
		ArrayList<DtoCitaOdontologica> listCita = new ArrayList<DtoCitaOdontologica>();
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
		Log4JManager.info("\n\nfechaHoraInicial:: "+fechaHoraInicial+" fechaHorafinal:: "+fechaHorafinal);
		
		DtoCitaOdontologica dtoCita;
		DtoAgendaOdontologica dtoAgenda;
		String limit = ""; 
		if (tipoBD == DaoFactory.ORACLE)
			limit = " And rownum = 1 ";
		else if (tipoBD == DaoFactory.POSTGRESQL)
			limit = " limit 1 ";
		String consulta = " SELECT co.codigo_pk AS codigo," +
		"ao.centro_atencion                                            as codigo_centro_atencion," +
		"administracion.getnomcentroatencion(ao.centro_atencion)       AS nom_centro_atencion," +
		"consultaexterna.getnombreunidadconsulta(ao.unidad_agenda)     AS nom_unidad_agenda  ," +
		"administracion.getnombrepersona(co.codigo_paciente)           AS nom_paciente       ," +
		"facturacion.getidpaciente(co.codigo_paciente)                 AS id_paciente        ," +
		"ao.fecha                                                      AS fecha              ," +
		"co.hora_inicio                                                AS hora               ," +
		"administracion.getnombrepersona(ao.codigo_medico)             AS nom_medico         ," +
		"ao.consultorio                                                as cod_consultorio," +
		"consultaexterna.getnombreconsultorio(ao.consultorio)          AS nom_consultorio    ," +
		"administracion.getintegridaddominio(co.estado)                AS nom_estado_cita    ," +
		"consultaexterna.getregmedico(ao.codigo_medico)                AS registo_medico    ," +
		"co.usuario_confirma                                           AS usuario    ," +
		
		"(SELECT s.cuenta AS cuenta " +
		"FROM odontologia.servicios_cita_odontologica sco " +
		"INNER JOIN ordenes.solicitudes s " +
		"ON (sco.numero_solicitud = s.numero_solicitud) " +
		"WHERE sco.cita_odontologica = co.codigo_pk " +
		limit + ") AS no_cuenta," +
		
		"(SELECT coalesce(p.telefono, '') AS telefono " +
		"FROM administracion.personas p " +
		"WHERE p.codigo = co.codigo_paciente " +
		limit + ") AS telefono," +
		
		"(SELECT p.historia_clinica AS historia_clinica " +
		"FROM manejopaciente.pacientes p " +
		"WHERE p.codigo_paciente = co.codigo_paciente " +
		limit + ") AS historia_clinica," +
		
		"(SELECT mcc.descripcion AS descripcion " +
		"FROM consultaexterna.motivos_cancelacion_cita mcc " +
		"WHERE mcc.codigo = co.motivo_cancelacion " +
		limit + ") AS motivo_cancelacion " +
		
		"FROM odontologia.citas_odontologicas co " +
		"LEFT OUTER JOIN odontologia.agenda_odontologica ao " +
		"ON (co.agenda = ao.codigo_pk and  ao.activo = '"+ConstantesBD.acronimoSi+"'"+((Utilidades.convertirAEntero(centroAtencion) > 0)?"AND ao.centro_atencion = "+centroAtencion+" ":" ")+((( unidadConsulta != ConstantesBD.codigoNuncaValido)&&unidadConsulta>0)?"AND ao.unidad_agenda = "+unidadConsulta+" ":" ")+" ) " +
		"WHERE 1=1 ";
		
		if(fechaHoraInicial.contains(ConstantesBD.separadorSplit))
			fechaHoraInicial = fechaHoraInicial.split(ConstantesBD.separadorSplit)[0]+" "+fechaHoraInicial.split(ConstantesBD.separadorSplit)[1];
		
		if(fechaHorafinal.contains(ConstantesBD.separadorSplit))
			fechaHorafinal = fechaHorafinal.split(ConstantesBD.separadorSplit)[0]+" "+fechaHorafinal.split(ConstantesBD.separadorSplit)[1];

		//SE HACEN MODIFICACIONES EN LA CONSULTA PARA PERMITIR CARGAR LAS PROGRAMADAS.
		consulta += "AND (TO_CHAR(ao.fecha,'yyyy-mm-dd')||' '||co.hora_inicio " +
					" BETWEEN '"+fechaHoraInicial+"' AND '"+fechaHorafinal+"' or to_char(co.fecha_programacion,'yyyy-mm-dd') BETWEEN '"+fechaHoraInicial.split(ConstantesBD.separadorSplit)[0]+"' AND '"+fechaHorafinal.split(ConstantesBD.separadorSplit)[0]+"')";
		/*
		if(Utilidades.convertirAEntero(centroAtencion) > 0){
			consulta += "AND ao.centro_atencion = "+centroAtencion+" ";
		}
		if(( unidadConsulta != ConstantesBD.codigoNuncaValido)&&unidadConsulta>0){
			consulta += "AND ao.unidad_agenda = "+unidadConsulta+" ";
		}
		*/
		
		if(!UtilidadTexto.isEmpty(codigoPaciente) && codigoPaciente!=ConstantesBD.codigoNuncaValido){
			consulta += "AND co.codigo_paciente = "+codigoPaciente+" ";
		}
		
		
		if(estadosCita.length>0)
		{
			String estadosCitaTmp="";
			boolean todoEstado=false;
			Log4JManager.info("longitud de los estados cita desde sqlbase: "+estadosCita.length);
			for(int i=0;i<estadosCita.length;i++)
			{
				if(estadosCita[i]!=null && !estadosCita[i].equals(""))
				{
					if(!estadosCita[i].equals("-1"))
						estadosCitaTmp += (estadosCitaTmp.equals("")?"":",") + "'" + estadosCita[i] + "'";
					else if(estadosCita[i].equals("-1"))
						todoEstado=true;
					
				}
			}
			if(!todoEstado){
				if(estadosCitaTmp.length()>0){
					consulta+="AND co.estado IN ("+estadosCitaTmp+") ";
				}
			}
			if(consultorio!=-1){
				consulta +="AND ao.consultorio = "+consultorio+" ";
			}
			if(!tipoCita.equals(ConstantesBD.codigoNuncaValido+"")){
				consulta += "AND co.tipo = '"+tipoCita+"' ";
			}
			if(profesionalSalud != ConstantesBD.codigoNuncaValido){
				consulta += "AND ao.codigo_medico = "+profesionalSalud+" ";
			}
		}
		
		consulta += "ORDER BY fecha, hora ";
				
		
			pst = connection.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs = pst.executeQuery();
			while (rs.next()){
				dtoCita = new DtoCitaOdontologica();
				dtoAgenda = new DtoAgendaOdontologica();
				dtoCita.setCodigoPk(rs.getInt("codigo"));
				dtoAgenda.setDescripcionCentAten(rs.getString("nom_centro_atencion"));
				dtoAgenda.setDescripcionUniAgen(rs.getString("nom_unidad_agenda"));
				dtoCita.setNombrePaciente(rs.getString("nom_paciente"));
				dtoCita.setNumeroIdentificacionPac(rs.getString("id_paciente"));
				dtoAgenda.setFecha(rs.getString("fecha"));
				dtoCita.setHoraInicio(rs.getString("hora"));
				dtoAgenda.setNombreMedico(rs.getString("nom_medico"));
				dtoAgenda.setDescripcionConsultorio(rs.getString("nom_consultorio"));
				dtoCita.setEstado(rs.getString("nom_estado_cita"));
				dtoCita.setCuenta(rs.getString("no_cuenta"));
				dtoCita.setMotivoNoAtencion(rs.getString("motivo_cancelacion"));
				dtoCita.setServiciosCitaOdon(consultarServiciosCitaOdontologica(connection, dtoCita.getCodigoPk(), institucion, false, codigoPaciente));
				dtoAgenda.setConsultorio(rs.getInt("cod_consultorio"));
				dtoAgenda.setRegistroMedico(rs.getString("registo_medico"));
				dtoCita.setHistoriaClinica(rs.getString("historia_clinica"));
				dtoCita.setTelefonoPaciente(rs.getString("telefono"));
				dtoCita.getUsuarioConfirma().setNombreUsuario(rs.getString("usuario"));
				dtoAgenda.setCentroAtencion(rs.getInt("codigo_centro_atencion"));
				dtoCita.setAgendaOdon(dtoAgenda);
				listCita.add(dtoCita);
			}
		} catch (Exception e) {
			Log4JManager.error("ERROR consultaCitaOdontologicaConsExt",e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sqle) {
				Log4JManager.error("ERROR consultaCitaOdontologicaConsExt", sqle);
			}
		}
		return listCita;
	}
	
	/**
	 * M�todo que consulta el programa, servicio, garant�a y estado en el plan de tratamiento
	 * @param connection
	 * @param institucion
	 * @param cita
	 * @return
	 */
	public static ArrayList<DtoProgramasServiciosPlanT> consultarDetalleCitaConsExterna(Connection connection, int institucion, int cita){
		ArrayList<DtoProgramasServiciosPlanT> list = new ArrayList<DtoProgramasServiciosPlanT>();
		DtoProgramasServiciosPlanT dtoPlanT;
		
		String codigoTarifario=ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(institucion);
		if(UtilidadTexto.isEmpty(codigoTarifario))
		{
			codigoTarifario=ConstantesBD.codigoTarifarioCups+"";
		}
		String consulta = " SELECT " +
								"php.programa as codigo_programa, " +
								"p.nombre AS programa, " +
								"facturacion.getcodigopropservicio2(sco.servicio,"+codigoTarifario+")||' - '|| facturacion.getnombreservicio(sco.servicio,"+codigoTarifario+") AS servicio, " +
								"sco.servicio AS codigo_servicio, " +
								"pspt.garantia AS garantia, " +
								"pspt.estado_servicio AS estadoservicio " +
							"FROM odontologia.citas_odontologicas co " +
							"INNER JOIN odontologia.servicios_cita_odontologica sco ON (sco.cita_odontologica=co.codigo_pk) " +
							"LEFT OUTER JOIN odontologia.programas_hallazgo_pieza php on  (sco.programa_hallazgo_pieza=php.codigo_pk)" +
							"LEFT OUTER JOIN odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
							"LEFT OUTER JOIN odontologia.det_plan_tratamiento dpt ON(sxp.det_plan_trata=dpt.codigo_pk) " +
							"LEFT OUTER JOIN odontologia.programas_servicios_plan_t pspt ON(dpt.codigo_pk=pspt.det_plan_tratamiento amd pspt.servicio=sco.servicio) " +
							"LEFT OUTER JOIN odontologia.programas p ON (p.codigo = php.programa) " +
							"WHERE  co.codigo_pk="+cita;

		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(connection, consulta);
			Log4JManager.info("consultarDetalleCitaConsExterna-->"+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next()){
				dtoPlanT = new DtoProgramasServiciosPlanT();
				dtoPlanT.setPrograma(new InfoDatosDouble(rs.getDouble("codigo_programa"), rs.getString("programa")));
				dtoPlanT.setServicio(new InfoDatosInt(rs.getInt("codigo_servicio"), rs.getString("servicio")));
				dtoPlanT.setGarantia(rs.getString("garantia"));
				dtoPlanT.setEstadoServicio(rs.getString("estadoservicio"));
				list.add(dtoPlanT);
			}
			rs.close();
			ps.close();
		} catch (Exception e) {
			Log4JManager.error("error consultando el detalle de la cita odontologica",e);
		}
		return list;
	}
	
	/**
	 * metodo que obtiene los datos informativos del centro de atenci�n
	 * @param consecutivoCenAten
	 * @return
	 */
	public static InfoDatosString obtenerInfoCentroAtencion(int consecutivoCenAten)
	{
		InfoDatosString dt = new InfoDatosString();
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,strConsultaCentroCosto);
			ps.setInt(1,consecutivoCenAten);
			Log4JManager.info("Consulta: "+ps);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				dt.setId(rs.getString("descripcion")); // nombre del centro costo
				dt.setDescripcion(rs.getString("direccion").equals(" ")?"":rs.getString("direccion")); // direccion del centro costo
				dt.setIndicativo(rs.getString("telefono").equals(" ")?"":rs.getString("telefono")); // telefono
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}catch(SQLException e){
			Log4JManager.error("Error Consulta Obtener Centro Atencion: ",e);
		}
		return dt;
	}
	
	
	/**
	 * M�todo para cargar la informacion de la cita asociada a un servicio de un plan de tratamiento
	 * @param con
	 * @param codigoPkProgServ
	 * @param codigoInstitucion
	 * @return
	 */
	public static DtoCitaOdontologica obtenerCitaOdontologicaXProgServPlanT(Connection con,BigDecimal codigoPkProgServ,int codigoInstitucion)
	{
		DtoCitaOdontologica dtoCita = new DtoCitaOdontologica();
		try
		{
			String consulta = " SELECT DISTINCT sco.cita_odontologica as cita_odontologica " +
								" from odontologia.servicios_cita_odontologica sco" +
								" INNER JOIN odontologia.programas_hallazgo_pieza php on (php.codigo_pk=sco.programa_hallazgo_pieza) " +
								" INNER JOIN odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
								" INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk = sxp.det_plan_trata) " +
								" INNER JOIN odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento = dpt.codigo_pk AND pspt.servicio=sco.servicio AND pspt.activo = 'S' ) " +
								" where pspt.codigo_pk = ? and sco.activo = ?";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setLong(1,codigoPkProgServ.longValue());
			pst.setString(2,ConstantesBD.acronimoSi);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				dtoCita = obtenerCitaOdontologica(con,rs.getInt("cita_odontologica"), codigoInstitucion);
			
			}
			rs.close();
			pst.close();
			
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error en obtenerCitaOdontologicaXProgServPlanT: ",e);
		}
		return dtoCita;
	}


	public static boolean validarCuposSiguientesDisponibles(DtoAgendaOdontologica dto) {

		String sentencia=
				"SELECT " +
					"hora_fin AS hora_fin " +
				"FROM " +
					"odontologia.agenda_odontologica " +
				"WHERE " +
					"codigo_pk = ?";
		boolean resultado=false;
		try 
		{
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setInt(1, dto.getCodigoPk());
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			if(rsd.next())
			{
				Log4JManager.info("rsd.getString('hora_fin') "+rsd.getString("hora_fin"));
				Log4JManager.info("dto.getHoraFin() "+dto.getHoraFin());
				Log4JManager.info("validacion "+rsd.getString("hora_fin").compareTo(dto.getHoraFin()));
				resultado=rsd.getString("hora_fin").compareTo(dto.getHoraFin())>=0;
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd, rsd, con);
		} 
		catch (SQLException e) 
		{
			Log4JManager.error("Error verificando cupos disponibles hora fin agenda",e);
		}

		Log4JManager.info("resultado "+resultado);
		if(!resultado)
		{
			return resultado;
		}
		
		sentencia=
				"SELECT " +
					"count(1) AS cantidad " +
				"FROM " +
					"odontologia.citas_odontologicas " +
				"WHERE " +
						"agenda=? " +
					"AND " +
						"hora_inicio >= ? AND hora_inicio < ? " +
					"AND " +
						"estado!='"+ConstantesIntegridadDominio.acronimoEstadoCancelado+"'";
		
		try {
			
			Connection con=UtilidadBD.abrirConexion();
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setInt(1, dto.getCodigoPk());
			psd.setString(2, dto.getHoraInicio());
			psd.setString(3, dto.getHoraFin());
			Log4JManager.info(psd);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			
			if(rsd.next())
			{
				resultado=rsd.getInt("cantidad")==0;
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(psd, rsd, con);
			
		} catch (SQLException e) {
			
			Log4JManager.error("Error verificando cupos disponibles",e);
		}
		
		return resultado;
	}


	public static boolean asignarSolicitudServicioCitaOdo(Connection con, DtoServicioCitaOdontologica servicioCita) {
		String sentencia= "UPDATE " +
								"odontologia.servicios_cita_odontologica " +
							"SET " +
								"numero_solicitud=? " +
							"WHERE " +
								"cita_odontologica=? AND " +
								"servicio=?";	
			try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,sentencia);
			ps.setInt(1, servicioCita.getNumeroSolicitud());
			ps.setInt(2, servicioCita.getCitaOdontologica());
			ps.setInt(3, servicioCita.getServicio());
			Log4JManager.info("**********************Consulta actualizar el numero de solicitud: "+ps);
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
	
			ps.close();
		}catch (Exception e) {
			Log4JManager.error("Error Consulta actualizando la solicitud ",e);
		}
		return false;
	}


	/**
	 * M�todo que actualiza la cita odontol�gica con todos sus servicios
	 * @param con
	 * @param dto
	 * @param esCambioServicio Indica si se est� modificando por cambio de servicios
	 * @return
	 */
	public static boolean actualizarCitaOdontologicaYServicios(Connection con, DtoCitaOdontologica dto, boolean esCambioServicio) {
		boolean resultado=false;
		if(esCambioServicio)
		{
			resultado=actualizarCitaOdontologicaCambioServicio(con, dto);
		}
		else
		{
			resultado=actualizarCitaOdontologica(con, dto);
		}
		if(resultado)
		{
			if(actualizarServiciosCitaOdont(con, dto.getCodigoPk(),dto.getUsuarioModifica(), dto.getServiciosCitaOdon())>0)
			{
				return true;	 
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param estado
	 * @param loginUsuario
	 * @return
	 */
	public static boolean cambiarEstadoCita(Connection con, BigDecimal codigo,	String estado, String loginUsuario) 
	{
		boolean retorna= false;
		String consultaStr="UPDATE odontologia.citas_odontologicas set estado=?, usuario_modifica=?, fecha_modifica=CURRENT_DATE, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+" WHERE codigo_pk=? ";
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
			ps.setString(1, estado);
			ps.setString(2, loginUsuario);
			ps.setBigDecimal(3, codigo);
			Log4JManager.info("**********************Consulta actualizar estado cita : "+ps);
			if(ps.executeUpdate()>0)
			{
				ps.close();
				retorna= true;
			}
			ps.close();
			
		}
		catch (SQLException e) 
		{
			Log4JManager.info("Error Consulta actualizando la estado cita ",e);
		}
		return retorna;
	}
	
//	/**
//	 * 
//	 * Metodo para obtener el ultimo codigo pk de la cita de un programa servicio del plan tratamiento dado el codigo_pk de la tabla programas servicios plan t
//	 * @param codigoPkProgServ
//	 * @param fechaFormatoAppNOREQUERIDA
//	 * @return
//	 * @author   Wilson Rios
//	 * @version  1.0.0 
//	 * @param estado 
//	 * @see
//	 */
//	public static BigDecimal obtenerCodigoPkUltimaCitaProgServPlanT(BigDecimal codigoPkProgServPlanT, String fechaFormatoAppNOREQUERIDA, ArrayList<String> estado) 
//	{
//		BigDecimal retorna= BigDecimal.ZERO;
//		String consultaStr="select sco.codigo_pk " +
//							" from odontologia.servicios_cita_odontologica sco" +
//							" INNER JOIN odontologia.citas_odontologicas co on(co.codigo_pk=sco.cita_odontologica) " +
//							" INNER JOIN odontologia.programas_hallazgo_pieza php on (php.codigo_pk=sco.programa_hallazgo_pieza) " +
//							" INNER JOIN odontologia.superficies_x_programa sxp on (sxp.prog_hallazgo_pieza=php.codigo_pk) " +
//							" INNER JOIN odontologia.det_plan_tratamiento dpt ON (dpt.codigo_pk = sxp.det_plan_trata) " +
//							" INNER JOIN odontologia.programas_servicios_plan_t pspt ON(pspt.det_plan_tratamiento = dpt.codigo_pk and pspt.servicio=sco.servicio AND pspt.activo = 'S' ) " +
//							" where pspt.codigo_pk = ? AND sco.activo = '"+ConstantesBD.acronimoSi+"' ";
//		
//		if(UtilidadFecha.esFechaValidaSegunAp(fechaFormatoAppNOREQUERIDA))
//		{
//			consultaStr+=" and sco.fecha_cita= '"+UtilidadFecha.conversionFormatoFechaABD(fechaFormatoAppNOREQUERIDA)+"' ";
//		}
//		if(estado.size()>0)
//		{
//			String cadenaEstados="";
//			for(String varEstado:estado)
//			{
//				if(!UtilidadTexto.isEmpty(cadenaEstados))
//					cadenaEstados=cadenaEstados+",";
//				cadenaEstados=cadenaEstados+"'"+varEstado+"'";
//			}
//			consultaStr+=" and co.estado in ("+cadenaEstados+")";
//		}
//		try
//		{
//			Connection con= UtilidadBD.abrirConexion();
//			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
//			ps.setBigDecimal(1, codigoPkProgServPlanT);
//			
//			Log4JManager.info("LA CONSUTLA DE LA CITA-------->"+ps);
//			
//			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
//			
//			if(rs.next())
//			{
//				ps.close();
//				retorna= rs.getBigDecimal(1);
//			}
//			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
//			
//		}
//		catch (SQLException e) 
//		{
//			Log4JManager.info("Error Consulta obtenerCodigoPkUltimaCitaProgServPlanT ",e);
//		}
//		return retorna;
//	}
//	
	
	
	
	/**
	 * METODO QUE RETORNA EL CODIGO DE LA CITA 
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 * @return
	 */
	public static BigDecimal obtenerCodigoPkUltimaCitaProgServPlanTXEvolucion(DtoPlanTratamientoOdo dto) 
	{
		BigDecimal retorna= BigDecimal.ZERO;
		
		
		String consultaStr= " select  codigo_pk  as codigoPk from odontologia.citas_odontologicas where evo_genera_prox_cita=? ";
		
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
			ps.setBigDecimal(1, dto.getCodigoEvolucion());
			
			Log4JManager.info("LA CONSUTLA DE LA CITA-------->"+ps);
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				retorna= rs.getBigDecimal("codigoPk");
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch (SQLException e) 
		{
			Log4JManager.info("Error Consulta Sql Cargar consulta ",e);
		}
	
	
		return retorna;
	}
	
	
	
	
	/**
	 * METODO QUE CONSULTA LA CITA CON RESPECTO AL LA EVOLUCION
	 * NOTA SE ENVIA CON DTO PLAN DE TRATAMIENTO PARA LA ESCALABILIDAD DE LA CONSULTA 
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 * @return
	 */
	public static DtoCitaOdontologica cargarCitaXEvolucionPlanTratamiento(DtoPlanTratamientoOdo dto) 
	{
		
		DtoCitaOdontologica dtoCita= new DtoCitaOdontologica();
		
		//BigDecimal retorna= BigDecimal.ZERO;
		
		String consultaStr= " " +
				"" +
				"select " +
				" codigo_pk as codigoPk,  " +               
				" estado as estado ,   " +
				" tipo as tipo  , " + 
				" fecha_modifica as fecha_modifica ," +
				" fecha_registra  as fecha_registra " +
				" from odontologia.citas_odontologicas " +
				" where " +
				"" +
				" evo_genera_prox_cita=? "; 
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultaStr);
			ps.setBigDecimal(1, dto.getCodigoEvolucion());
			
			Log4JManager.info("LA CONSUTLA DE LA CITA-------->"+ps);
			
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				dtoCita.setCodigoPk(rs.getInt("codigoPk"));
				dtoCita.setEstado(rs.getString("estado"));
				dtoCita.setTipo(rs.getString("tipo"));
				dtoCita.setFechaRegistra(rs.getString("fecha_registra"));
				dtoCita.setFechaProgramacion(rs.getString("fecha_modifica")); // TODO CAMBIAR ESTA PARTE CARVAJAL
				
				
			}
			
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
			
		}
		catch (SQLException e) 
		{
			Log4JManager.info("Error Consulta Sql Cargar consulta ",e);
		}
	
		return dtoCita;
	}

	/**
	 * Verifica si las citas est�n siendo atendidas
	 * @param codigoPkCita codigo_pk (Llave primaria) de la cita
	 * @param con Conexi�n con la BD
	 * @return true en caso de que la cita est� siendo atendida, false de lo contrario
	 */
	public static boolean estaCitaEnAtencion(int codigoPkCita, Connection con) {
		try{
			String sentencia=
					"SELECT " +
						"codigo_pk " +
					"FROM " +
						"odontologia.valoraciones_odonto " +
					"WHERE " +
						"cita = ? " +
				"UNION " +
					"SELECT " +
						"codigo_pk " +
					"FROM " +
						"evoluciones_odo " +
					"WHERE " +
						"cita = ?";
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setInt(1, codigoPkCita);
			psd.setInt(2, codigoPkCita);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			boolean resultado=false;
			if(rsd.next())
			{
				resultado=true;
			}
			rsd.close();
			psd.close();
			return resultado;
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error verificando si la cita est� atendida", e);
			return false;
		}
	}
	
	
	/**
	 * 
	 * M�todo que actualiza el programa Hallazgo pieza de un servicio asociado 
	 * a una cita odontol�gica
	 * 
	 * @param con
	 * @param programaHallazgoPieza
	 * @param codigoPk ---> C�digo del registro en servicios_cita_odontologica
	 * @return
	 */
	public static boolean actualizarProgramaHallazgoPiezaServicioCita (Connection con, long programaHallazgoPieza, long codigoPk) 
	{
		boolean resultado = false;		
		try
		{
			String consulta=strUpdateProgramaHallazgoPiezaServicioCitaOdonto;
			
		    PreparedStatementDecorator ps =  new PreparedStatementDecorator(con , consulta);
		    
		    ps.setLong(1, programaHallazgoPieza);
			ps.setLong(2, codigoPk);
	
			Log4JManager.info("Cadena Actualizacion Servicio"+ps);
			
			 if(ps.executeUpdate()>0)
		     {
				 resultado = true;
		     }
			 ps.close();
		     	
		}catch (Exception e) {
			Log4JManager.error("ERROR Actualizando Servicios Cita: "+strUpdateProgramaHallazgoPiezaServicioCitaOdonto, e);
	     }
	    
		return resultado;
	}
	
	/**
	 * Este m�todo se encarga de consultar el hist�rico de estados
	 * de una cita odontol�gica. 
	 *
	 * @param con
	 * @param codigoPk
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	public static ArrayList<DtoCitaOdontologica> obtenerHistoricoEstadosCita(Connection con, int codigoPk){
		
		
		ArrayList<DtoCitaOdontologica> historicoCita = new ArrayList<DtoCitaOdontologica>();
		DtoCitaOdontologica dto = new DtoCitaOdontologica(); 
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strConsultaHistoricoEstadosCita);
			ps.setInt(1, codigoPk);
			ps.setInt(2, codigoPk);
			Log4JManager.info("Consulta: "+ps);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				dto = new DtoCitaOdontologica();
				dto.setCodigoPk(rs.getInt("cita_odontologica"));				 
				dto.setEstado(rs.getString("estado"));
				dto.setFechaModifica(rs.getString("fecha_modifica"));
				dto.setHoraModifica(rs.getString("hora_modifica"));
				dto.setUsuarioModifica(rs.getString("usuarioModifica"));
				
				historicoCita.add(dto);
			}
			rs.close();
			ps.close();
		}catch (Exception e) {
			Log4JManager.error("Error Consulta: "+strConsultaHistoricoEstadosCita, e);
		}
		return historicoCita;
	}
	
	/**
	 * Este m�todo se encarga de actualizar el tipo de cancelaci�n de la cita.
	 *
	 * @param tipoCancelacion
	 * @param codigoCita
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	public static boolean actualizarTipoCancelacionCita (Connection con, String tipoCancelacion, long codigoCita){
		
		boolean resultado = false;
		String consulta= "";
		try
		{
			consulta= "UPDATE odontologia.citas_odontologicas set " +
					"tipo_cancelacion = ? where codigo_pk = ?";
			
		    PreparedStatementDecorator ps =  new PreparedStatementDecorator(con , consulta);
		    
		    ps.setString(1, tipoCancelacion);
			ps.setLong(2, codigoCita);
	
			 if(ps.executeUpdate()>0)
		     {
				 resultado = true;
		     }
			 ps.close();
		     	
		}catch (Exception e) {
			Log4JManager.error("ERROR Actualizando Cita Odontol�gica: "+consulta, e);
	     }
	    
		return resultado;
	}
	
	public static List<Integer> consultarCitaAsociadaProgramada(Connection con, int codigoCitaProgramada){
		
		List<Integer> listadoCitasAsociadas = new ArrayList<Integer>();
		
		String consultaCitaAsociada = "SELECT cita_asociada AS citaAsociada from citas_asociadas_a_programada " +
				"where cita_programada  = ?";
		try{
			
			PreparedStatementDecorator psAsociada=new PreparedStatementDecorator(con, consultaCitaAsociada);
			psAsociada.setInt(1, codigoCitaProgramada);
			ResultSetDecorator rsAsociada=new ResultSetDecorator(psAsociada.executeQuery());
			while(rsAsociada.next())
			{
				listadoCitasAsociadas.add(rsAsociada.getInt("citaAsociada"));
			}
			
		}catch (Exception e) {
			Log4JManager.error("ERROR consultando cita asociada a programada: "+consultaCitaAsociada, e);
		}
		

		
		return listadoCitasAsociadas;
	}
	
}


