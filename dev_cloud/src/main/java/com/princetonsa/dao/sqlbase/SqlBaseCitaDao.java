/*
 * @(#)SqlBaseCitaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoValidacionTipoCobroPaciente;
import com.princetonsa.mundo.agenda.Cita;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.IValidacionTipoCobroPacienteServicio;

/**
 * Esta clase implementa la funcionalidad comï¿½n a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estï¿½ndar. Mï¿½todos particulares a Cita
 *
 *	@version 1.0, Apr 6, 2004
 */
public class SqlBaseCitaDao
{

	/**
	* Manejador de logs
	*/
	private static Logger logger = Logger.getLogger(SqlBaseCitaDao.class);

	/**
	* Cadena constante con el <i>Statement</i> necesario para actualizar
	* el estado de una cita en una base de datos Genï¿½rica
	*/
	private static String actualizarEstadoCitaStr = "UPDATE cita " +
																			"SET estado_cita = ?, motivo_noatencion = ? , fecha_modifica=CURRENT_DATE, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD() +", usuario_modifica_estado=? " +
																			"WHERE codigo= ? ";
	
	/**
	 * Cadena que  consulta las solicitudes de la cita con sus estados mï¿½dicos
	 * para saber si a una cita se le puede psar a estado cumplida
	 */
	private static String consultarEstadosSolicitudesCitaStr = "SELECT "+ 
		"coalesce(sc.numero_solicitud,"+ConstantesBD.codigoNuncaValido+") AS numero_solicitud, "+
		"coalesce(s.estado_historia_clinica,"+ConstantesBD.codigoNuncaValido+") AS estado_historia_clinica," +
		"sc.estado As estado_servicio "+ 
		"FROM servicios_cita sc "+ 
		"LEFT OUTER JOIN solicitudes s ON(s.numero_solicitud=sc.numero_solicitud) "+ 
		"WHERE "+ 
		"sc.codigo_cita = ?";
	
	/**
	 * Cadena que actualiza el estado de la cita a partir del codigo de la cita
	 */
	private static String actualizarEstadoCitaXCodigoStr = "UPDATE cita SET estado_cita = ?, fecha_modifica=CURRENT_DATE, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD() +", usuario_modifica_estado = ? WHERE codigo = ?";

	/**
	 *cadena para la insercion de una autorizacion de citas si es el caso 
	 */
	private static String is_autorizacion_cita = "INSERT INTO autorizaciones_cita " +
			"("+
				"consecutivo, " +
				"cita, " +
				"motivo_autorizacion, " +
				"usuario_autoriza, " +
				"fecha_modifica, " +
				"hora_modifica, " +
				"usuario_modifica, " + 
				"estado_cita " +
			") "+
			"VALUES (?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
	/**
	* Cadena constante con el <i>Statement</i> necesario para insertar una cita asignada en una base
	* de datos Genï¿½rica
	*/
	private static final String is_asignar =
		"INSERT INTO cita"	+
		"("							+
			"codigo,"				+
			"codigo_paciente,"		+
			"estado_cita,"			+
			"estado_liquidacion,"	+
			"codigo_agenda,"		+
			"unidad_consulta,"		+
			"usuario, " +
			"fecha_gen, " +
			"hora_gen, " +
			"fecha_solicitada, " +
			"telefono," +
			"prioritaria," +
			"fecha_modifica," +
			"hora_modifica, " +
			"usuario_modifica_estado " +
		")"					+
		"VALUES"			+
		"(?,?," + ConstantesBD.codigoEstadoCitaAsignada + ",'" + ConstantesBD.codigoEstadoLiquidacionSinLiquidar  + "',?,?,?, CURRENT_DATE, ?,?,?,?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?)";

	/**
	* Cadena constante con el <i>Statement</i> necesario para insertar una cita reservada en una
	* base de datos Genï¿½rica
	*/
	private static final String is_reservar =
		"INSERT INTO cita"			+
		"("							+
			"codigo,"				+
			"codigo_paciente,"		+
			"estado_cita,"			+
			"estado_liquidacion,"	+
			"codigo_agenda,"		+
			"unidad_consulta,"		+
			"usuario, " +
			"fecha_gen, " +
			"hora_gen, " +
			"fecha_solicitada," +
			"prioritaria," +
			"fecha_modifica," +
			"hora_modifica, " +
			"usuario_modifica_estado" +
		")"							+
		"VALUES"					+
		"(?,?," + ConstantesBD.codigoEstadoCitaReservada + ",'" + ConstantesBD.codigoEstadoLiquidacionSinLiquidar  + "',?,?,?, CURRENT_DATE,?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?)";

	/**
	* Cadena constante con el <i>Statement</i> necesario para disminuir los cupos diponibles en una
	* agenda en una base de datos Genï¿½rica
	*/
	private static final String is_disminuirCuposAgenda =
		"UPDATE "	+ "agenda "	+
		"SET "		+ "cupos"	+ "=cupos-1 "	+
		"WHERE "	+ "codigo"	+ "=?";
	
	private static final String insertarServicioCitaStr = "INSERT INTO servicios_cita "+ 
		"(codigo,codigo_cita,servicio,estado,centro_costo,numero_solicitud,especialidad,fecha_creacion,hora_creacion,observaciones,usuario_creacion,fecha_cita,hora_inicio_cita,hora_fin_cita,estado_cita,codigo_agenda,control_post_operatorio_cx) "+ 
		"values "; 

	/**
	* Cadena constante con la sentencia necesario para asignar un nï¿½mero de solicitud a una cita, en
	* una base de datos Genï¿½rica
	*/
	private static final String is_asignarSolicitud =
		"UPDATE servicios_cita SET numero_solicitud=?, usuario_modificacion = ?, fecha_modificacion = CURRENT_DATE, hora_modificacion = substr(?,0,6) "	+
		"WHERE codigo_cita=? AND servicio = ? AND numero_solicitud IS NULL";

	/**
	 * Cadena que actualiza el estado de liquidacion de una cita
	 */
	private static final String is_asignarSolicitud_2 = "UPDATE cita SET estado_liquidacion='"+ConstantesBD.codigoEstadoLiquidacionLiquidada+"' WHERE codigo = ?";

	/**
	* Cadena constante con el <i>Statement</i> necesario para aumentar los cupos diponibles en una
	* agenda en una base de datos Genï¿½rica
	*/
	private static final String is_aumentarCuposAgenda =
		"UPDATE "	+ "agenda "	+
		"SET "		+ "cupos"	+ "=cupos+1 "	+
		"WHERE "	+ "codigo"	+ "=?";

	/**
	* Cadena constante con el <i>Statement</i> necesario para modificar los datos de una cita en una
	* fuente de datos Genï¿½rica
	*/
	private static final String is_cancelar =
		"UPDATE "	+	"cita "					+
		"SET "		+	"codigo_agenda"			+ "=?,"	+
						"estado_cita"			+ "=?,"	+
						"motivo_cancelacion"	+ "=?," +
						"observaciones_cancelacion" +"=?, " +
						"usuario_cancela=?," +
						"fecha_cancela=?," +
						"hora_cancela=? "+
		"WHERE "	+	"codigo"				+ "=? " +
		"AND estado_cita NOT IN("+ConstantesBD.codigoEstadoCitaCanceladaInstitucion+", "+ConstantesBD.codigoEstadoCitaCanceladaPaciente+")";
	

	

	/**
	* Cadena constante con el <i>Statement</i> necesario para aumentar los cupos diponibles en una
	* agenda en una base de datos Genï¿½rica
	*/
	private static final String is_aumentarCuposAgendas =
		"UPDATE "	+ "agenda "	+
		"SET "		+ "cupos"	+ "=cupos+1 "	+
		"WHERE "	+ "codigo IN (SELECT codigo_agenda FROM cita WHERE codigo IN(?) AND estado_cita<>"+ConstantesBD.codigoEstadoCitaAReprogramar+")";

	/**
	* Cadena constante con loa sentencia necesario para reprograr una cita  en una fuente de datos
	* Genï¿½rica
	*/
	private static final String is_reprogramar =
		"UPDATE "	+	"cita "				+
		"SET "		+	"codigo_agenda=?,"	+
						"unidad_consulta=?,"	+
						"estado_cita="	+ ConstantesBD.codigoEstadoCitaReprogramada + ", " +
						"fecha_modifica = CURRENT_DATE, " +
						"hora_modifica = " + ValoresPorDefecto.getSentenciaHoraActualBD() + ", " +
						"usuario_modifica_estado = ?"+
		"WHERE "	+	"codigo=?";
	
	/**
	 * Cadena que elimina los servicios de una cita
	 */
	private static final String eliminarServiciosCitaStr = "DELETE FROM servicios_cita WHERE codigo_cita = ?";
	

	/**
	* Cadena constante con la sentencia necesario para eliminar la programación de un conjunto de
	* citas en una fuente de datos Genérica
	*/
	private static final String is_eliminarProgramacion =
		"UPDATE " +
			"cita " +
		"SET " +
			"estado_cita = " + ConstantesBD.codigoEstadoCitaAReprogramar + " " +
		"WHERE codigo IN(?)";

	/**
	* Cadena constante con el <i>Statement</i> necesario para aumentar los cupos diponibles en una
	* agenda en una base de datos Genï¿½rica
	*/
	private static final String is_disminuirCuposAgendas =
		"UPDATE "	+ "agenda "	+
		"SET "		+ "cupos"	+ "=cupos-1 "	+
		"WHERE "	+ "codigo IN (SELECT codigo_agenda FROM cita WHERE codigo IN(?))";
	
	/**
	 * Cadena que modifica el servicio de la cita
	 */
	private static final String modificarServicioCitaStr = "UPDATE servicios_cita SET " +
		"centro_costo = ?, " +
		"usuario_modificacion = ?, " +
		"fecha_modificacion = CURRENT_DATE, " +
		"hora_modificacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
		"observaciones = ?," +
		"control_post_operatorio_cx=? " +
		"WHERE codigo_cita = ? AND servicio = ?";

	/**
	* Cadena constante con el <i>Statement</i> necesario listar
	* una cita con toda la informaciï¿½n
	*/
	public static final String listarCitaImpresionStr=
						"SELECT " +
						" c.fecha_gen || '' AS fechaCita,"+  
						" c.hora_gen || '' AS horaCita,"  +
						" c.codigo AS codigoCita,"  +
						" c.usuario,"  +
						" uc.descripcion AS unidadConsulta,"+  
						" coalesce(p.primer_nombre,'') || ' ' || coalesce(p.segundo_nombre,'') || ' ' || coalesce(p.primer_apellido,'') || ' ' || coalesce(p.segundo_apellido,'') AS nombrePaciente,"+  
						" p.numero_identificacion AS numId,"  +
						" ti.acronimo AS tipoId,"  +
						" p.fecha_nacimiento || '' AS fechaNacimiento,"+  
						" s.nombre AS sexo,"  +
						" p.direccion AS direccion,"+

                        "coalesce(p.telefono_fijo||'',' ') as telefono,"+
                        "coalesce(p.telefono_celular||'',' ') as celular,"+
                        "coalesce(p.telefono||'',' ') as otrostelefonos,"+
							
						//" c.telefono AS telefono,"+ 
						" a.centro_atencion AS cod_centro_atencion,"+ 
						" getnomcentroatencion(a.centro_atencion) AS nombre_centro_atencion,"+
						" case when a.codigo_medico is null then 1 else 0 end as citacancelada,"+
						" administracion.getnombremedico(a.codigo_medico) as nombremedico,"+
						" CASE WHEN a.fecha is NULL then  '" + ConstantesBD.textoCitaAgendaCancelada +"' ELSE a.fecha||'' END as fechaAgenda,"+ 
						" CASE WHEN a.hora_inicio is NULL then  '" + ConstantesBD.textoCitaAgendaCancelada +"' ELSE a.hora_inicio||'' END as horaAgenda,"+
						" CASE WHEN a.consultorio is NULL then  '" + ConstantesBD.textoCitaAgendaCancelada +"' ELSE con.descripcion END as consultorio" +
					" FROM cita c" +
					" INNER JOIN agenda a on(a.codigo=c.codigo_agenda)"+
					" INNER JOIN unidades_consulta uc ON (c.unidad_consulta=uc.codigo)"+ 
					" INNER JOIN personas p ON (c.codigo_paciente=p.codigo)" +
					" INNER JOIN tipos_identificacion ti ON(p.tipo_identificacion = ti.acronimo)"+ 
					" INNER JOIN sexo s ON(s.codigo=p.sexo)" +
					" INNER JOIN consultorios con ON(a.consultorio = con.codigo)"+ 
					" WHERE s.nombre is not null";
	
	/**
	 * Cadena que lista los servicios de la cita
	 */
	public static final String listarServiciosCitaImpresionStr = "SELECT "+ 
		"'(' || getcodigoespecialidad(sc.servicio) || '-' || sc.servicio || ')' AS codigo_servicio, "+
		"getnombreservicio(sc.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_servicio "+
		"FROM servicios_cita sc "+ 
		"WHERE sc.codigo_cita = ? AND sc.estado = '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' " +
		" AND sc.numero_solicitud is not null "+ //se aï¿½adio para solucion 2564 de xplanner 2008
		" ORDER BY nombre_servicio";

	private static String asignarCitaCanceladaIni="INSERT INTO cita (codigo, codigo_paciente, estado_cita, estado_liquidacion, codigo_agenda, unidad_consulta, fecha_gen, hora_gen, usuario, prioritaria) VALUES (";
	private static String asignarCitaCanceladaFin=",?," + ConstantesBD.codigoEstadoCitaAReprogramar + ", ?,?,?,?,?,?, '"+ConstantesBD.acronimoNo+"')";
	
	/**
	 * Cadena que consulta el ultimo consecutivo de la cita
	 */
	private static final String obtenerUltimoCodigoCitaPacienteStr = "SELECT max(codigo) as codigo FROM cita WHERe codigo_paciente = ?";
	
	/**
	 * Cadena que actualiza el codigo de la cita de los servicios
	 */
	private static final String actualizarCodigoCitaEnServiciosStr = "UPDATE servicios_cita SET codigo_cita = ? WHERE codigo_cita = ?";

	private static String fueCanceladaInsStr="SELECT * from cita where codigo_agenda = ? and estado_cita="+ConstantesBD.codigoEstadoCitaCanceladaInstitucion;
	
	private static String actualizarObservacionCita="UPDATE servicios_cita SET observaciones =?, usuario_modificacion = ?, fecha_modificacion = CURRENT_DATE, hora_modificacion = substr(?,0,6) WHERE codigo_cita=? AND servicio = ?";
	
	private static String consultarCentrosCostoXUnidadDeConsultaStr=" SELECT cc.codigo as codigocentrocosto, " +
																	" cc.nombre as descripcioncentrocosto " +
																	" FROM centros_costo cc  " +
																	" INNER JOIN cen_costo_x_un_consulta ccx ON(cc.codigo=ccx.centro_costo) " +
																	" INNER JOIN agenda a ON(ccx.unidad_consulta=a.unidad_consulta) " +
																	" WHERE a.codigo = ? " +
																	" AND cc.centro_atencion = ?" +
																	" AND cc.institucion = ? ";
	/**
	 * Cadena que consulta los campos adicionales de la reserva
	 */
	private static final String consultaCamposAdicionalesReservaStr = "SELECT "+ 
		"ci.convenio AS codigo_convenio, "+ 
		"co.nombre As nombre_convenio, "+
		"co.tipo_regimen AS codigo_tipo_regimen, "+
		"getnomtiporegimen(co.tipo_regimen) AS nombre_tipo_regimen, "+
		"ci.contrato AS codigo_contrato, "+ 
		"ci.estrato_social AS codigo_estrato_social, "+
		"getnombreestrato(ci.estrato_social) AS nombre_estrato_social, "+
		"ci.tipo_afiliado AS codigo_tipo_afiliado, "+
		"getnombretipoafiliado(ci.tipo_afiliado) AS nombre_tipo_afiliado," +
		"ci.naturaleza_paciente as naturaleza_paciente "+
		"FROM cita ci "+ 
		"INNER JOIN convenios co ON(co.codigo=ci.convenio) "+
		"WHERE ci.codigo = ?";
	
	/**
	 * Cadena que consulta el monto de cobro de la reserva
	 */
	private static final String consultaMontoCobroReservaStr = "SELECT " +
		"codigo " +
		"FROM montos_cobro " +
		"WHERE convenio = ? AND via_ingreso = "+ConstantesBD.codigoViaIngresoConsultaExterna+" AND tipo_afiliado = ? AND estrato_social = ? AND vigencia_inicial <= CURRENT_DATE ORDER BY vigencia_inicial DESC";
	
	/**
	 * Cadena que consulta el listado de citas
	 */
	private static final String is_listar = "SELECT "+
		"DISTINCT(ci.codigo) AS codigo, "+
		"coalesce(a.codigo,-1) AS codigoAgenda, "+
		"ci.estado_cita AS codigoEstadoCita, "+
		"ec.nombre AS nombreEstadoCita, "+
		"ci.estado_liquidacion AS codigoEstadoLiquidacion, "+
		"el.nombre AS nombreEstadoLiquidacion, "+
		"ci.unidad_consulta AS codigoUnidadConsulta, "+
		//"getsexopaciente(ci.codigo_paciente) AS codigoSexoPaciente, "+
		"a.centro_atencion as codigocentroatencion,  "+
		"getnomcentroatencion(a.centro_atencion) as nombrecentroatencion, "+
		"uc.descripcion AS nombreUnidadConsulta, "+
		"getnombrepersona(p.codigo) as nombremedico, "+ 
		"coalesce(co.descripcion,'') AS nombreConsultorio, "+
		"coalesce(p.primer_nombre,'') AS primerNombreMedico, "+
		"coalesce(p.segundo_nombre,'') AS segundoNombreMedico, "+
		"coalesce(p.primer_apellido,'') AS primerApellidoMedico, "+
		"coalesce(p.segundo_apellido,'') AS segundoApellidoMedico, "+
		"coalesce(TO_CHAR(a.fecha,'DD/MM/YYYY'),'') AS fecha, "+
		"coalesce(a.hora_inicio||'','') AS horaInicio, "+
		"coalesce(a.hora_fin||'','') AS horaFin, "+
		"coalesce(m.ocupacion_medica,-1) AS ocupacionMedica, " +
		"TO_CHAR(ci.fecha_solicitada,'DD/MM/YYYY') AS fechaSolicitada," +
		"ci.convenio as convenio," +
		"a.consultorio, " +
		"p.codigo AS codigomedico," +
		"coalesce(oarc.orden||'', '') AS ordenamb," +
		"coalesce(doas.servicio||'', '') AS servordenamb "+
		"FROM cita ci "+ 
		"LEFT OUTER JOIN(agenda a "+ 
			"INNER JOIN consultorios co ON(co.codigo=a.consultorio) "+ 
			"LEFT OUTER JOIN medicos m on(m.codigo_medico=a.codigo_medico) "+ 
			"LEFT OUTER JOIN personas p ON(p.codigo=m.codigo_medico) "+ 
		") ON(a.codigo=ci.codigo_agenda) "+ 
		"INNER JOIN estados_cita ec ON(ec.codigo=ci.estado_cita) "+ 
		"INNER JOIN estados_liquidacion el ON(el.acronimo=ci.estado_liquidacion) "+ 
		"INNER JOIN unidades_consulta uc ON(uc.codigo=ci.unidad_consulta) "+ 
		"LEFT OUTER JOIN servicios_cita sc ON(sc.codigo_cita=ci.codigo) "+ 
		"LEFT OUTER JOIN solicitudes so ON(so.numero_solicitud=sc.numero_solicitud) " +
		"LEFT OUTER JOIN ordenes.ordenes_amb_reservas_citas oarc ON (oarc.codigo_cita=ci.codigo) " +
		"LEFT OUTER JOIN det_orden_amb_servicio doas ON (oarc.orden=doas.codigo_orden)";
	
	/**
	 * Cadena que consulta los servicios de la cita
	 */
	private static final String consultarServiciosCitaStr = "SELECT "+ 
		"sc.codigo_cita AS codigo_cita, "+ 
		"sc.servicio AS codigo_servicio, "+
		"coalesce(sc.especialidad||'','') AS codigo_especialidad, "+
		"coalesce(sc.numero_solicitud||'','') AS numero_solicitud, " +
		"CASE WHEN sc.numero_solicitud IS NULL THEN '' ELSE getconsecutivosolicitud(sc.numero_solicitud) || '' END AS consecutivo_orden, "+ 
		"'(' || s.especialidad || '-' || sc.servicio || ') ' || getnombreservicio(sc.servicio,"+ConstantesBD.codigoTarifarioCups+") AS nombre_servicio, "+
		"sc.centro_costo AS codigo_centro_costo, " +
		"getnomcentrocosto(sc.centro_costo) AS nombre_centro_costo, "+
		"sc.estado AS estado_servicio, "+
		"coalesce(sc.observaciones,'') AS observaciones, "+
		"coalesce(s.sexo,0) AS codigo_sexo, "+
		"CASE WHEN s.espos = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS es_pos, " +
		"tieneServicioCondiciones(sc.servicio,?) AS tiene_condiciones, " +
		" sc.codigo As codigo," +
		"c.convenio as convenio " +
		"FROM servicios_cita sc "+ 
		"INNER JOIN servicios s ON(s.codigo=sc.servicio) " +
		"INNER JOIN cita c ON(sc.codigo_cita=c.codigo) "+ 
		"WHERE "+ 
		"sc.codigo_cita = ? ORDER BY  nombre_servicio";
	
	/**
	 * Cadena que anula el servicio de una cita
	 */
	private static final String anularServicioCitaStr = "UPDATE servicios_cita SET " +
		"control_post_operatorio_cx=null, estado = '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"',fecha_anulacion = CURRENT_DATE, hora_anulacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+",usuario_anulacion = ? " +
		"WHERE codigo_cita = ? AND servicio = ?";
	
	private static final String strEliminarServicioCita = "DELETE FROM servicios_cita  WHERE codigo=?";
	
	/**
	 * Cadena que anula la solicitud de una cita
	 */
	private static final String anularSolicitudCitaStr = "UPDATE solicitudes SET estado_historia_clinica = "+ConstantesBD.codigoEstadoHCAnulada+" WHERe numero_solicitud = ?";
	
	/**
	 * Cadena que anula el cargo de una cita
	 */
	private static final String anularCargoCitaStr = "UPDATE det_cargos set estado = "+ConstantesBD.codigoEstadoFAnulada+", usuario_modifica = ?, fecha_modifica = CURRENT_DATE, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" WHERE solicitud = ?";
	
	
	/**
	 * cadena que consulta el numero de peticion
	 */
	private static final String strConsultPeticion = "SELECT codigo_peticion FROM solicitudes_cirugia WHERE  numero_solicitud=?";
	
	
	/**
	 * Cadena para insertar informacion dentro del log al reprogramar una cita
	 * */
	private static final String strLogReprogramacion = "INSERT INTO log_reprogramacion_citas (cita_reprogramada,fecha_reprogramacion,hora_reprogramacion,usuario_reprogramo,centro_atencion_anter,centro_atencion_nuevo,fecha_cita_anter,fecha_cita_nuevo,hora_cita_anter,hora_cita_nuevo,profesional_anter,profesional_nuevo,consultorio_anter,consultorio_nuevo,unidad_agenda_anter,unidad_agenda_nuevo) " +
			"VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// adicionado por tarea 3437 xplanner 2008
	/**
	 * Encargado de consultar si una cita tiene el servicio indicado
	 */
	private static final String strConsultaServicioXCita = " SELECT count (*) FROM servicios_cita where codigo_cita=? AND servicio=?";
	
	/**
	 * Cadena que actualiza la prioridad de una cita
	 */
	private static final String strActualizarPrioridadCita = "UPDATE cita SET prioritaria = ? WHERE codigo = ?";
	
	/**
	 * Cadena que consulta el numero de autorizacion de un paciente
	 */
	private static final String strConsultaNumeroAutorizacion = "SELECT " +
																	"numero_verificacion " +
																"FROM verificaciones_derechos " +
																"WHERE ingreso = ? AND convenio = ?";
	
	/**
	 * Metodo encargado de verificar si un servicio indicado
	 * se encuentra en la tabla servicios_cita.
	 * @param codigoCita
	 * @param codigoServicio
	 * @param connection
	 */
	public static String existeServicioEnCita (Connection connection, String codigoCita,String codigoServicio)
	{
	
		logger.info("\n entre a existeServicioEnCita cita --> "+codigoCita+"  Servicio --> "+codigoServicio);
		String cadena = strConsultaServicioXCita;
		try 
		{
			logger.info("\n cadena --> "+cadena);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet ));
			//codigo cita
			ps.setObject(1, codigoCita);
			//codigo servicio
			ps.setObject(2, codigoServicio);
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if (rs.next())
				if (rs.getInt(1)>0)
				return ConstantesBD.acronimoSi;
			else 
				return ConstantesBD.acronimoNo;
				
		} 
		catch (Exception e) 
		{
			logger.info("\n problema consultando servicio x cita "+e);
		}
		
		return ConstantesBD.acronimoNo;
	}
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	/**
	 * Metodo encargado de consultar la peticion
	 * dependiendo del numero de solicitud
	 * @param connection
	 * @param numSol
	 * @return
	 */
	public static String consultPetXNumSol (Connection connection, String numSol)
	{
		String cadena= strConsultPeticion;
		PreparedStatement pst=null;
		ResultSet rs=null;
		String resultado="";
		try 
		{
			pst = connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			pst.setObject(1, numSol);
			rs = pst.executeQuery();
			if(rs.next())
				resultado= rs.getString(1);
			
		}
		catch(Exception e){
			logger.error("############## ERROR consultPetXNumSol", e);
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
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return resultado;
	}
	
	/**
	 * M?todo para implementar el manejo de Commit en
	 * una BD Gen?rica
	 * @param con
	 * @param estado
	 * @return
	 */
	private static ResultadoBoolean setCommit(Connection con, boolean estado)
	{
		try
		{
			con.setAutoCommit(estado);
		}
		catch(SQLException e)
		{
			logger.error("Problemas poniendo el commit en "+estado+", Cita "+e);
			return new ResultadoBoolean(false, "Problemas poniendo el commit en "+estado+", Cita "+e);
		}

		return new ResultadoBoolean(true);
	}

	/**
	 * Mï¿½todo que implementa el inicio de una transacciï¿½n en
	 * una BD Genï¿½rica
	 *
	 * @param con Conexiï¿½n con la fuente de datos
	 * @return
	 */
	private static ResultadoBoolean empezarTransaccion(Connection con)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			if (myFactory.beginTransaction(con))
			{
				return new ResultadoBoolean(true);
			}
			else
			{
				return new ResultadoBoolean(false, "La transacciï¿½n no pudo ser iniciada");
			}
		}
		catch (SQLException e)
		{
			return new ResultadoBoolean(false, "La transacciï¿½n no pudo ser iniciada");
		}
	}

	/**
	 * Mï¿½todo que implementa la finalizaciï¿½n de una transacciï¿½n en una
	 * BD Genï¿½rica
	 *
	 * @param con Conexiï¿½n con la fuente de datos
	 * @return
	 */
	private static ResultadoBoolean terminarTransaccion(Connection con)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			myFactory.endTransaction(con);
			return new ResultadoBoolean(true);
		}
		catch (SQLException e)
		{
			return new ResultadoBoolean(false, "La transacciï¿½n no pudo ser terminada");
		}
	}

	/**
	 * Mï¿½todo que implementa la cancelaciï¿½n de una transacciï¿½n en una
	 * BD Genï¿½rica
	 *
	 * @param con Conexiï¿½n con la fuente de datos
	 * @return
	 */
	private static ResultadoBoolean abortarTransaccion(Connection con)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			myFactory.abortTransaction(con);
			return new ResultadoBoolean(false, "Error al tratar de hacer la actualizaciï¿½n del estado de la cita");
		}
		catch (SQLException e)
		{
			return new ResultadoBoolean(false, "La transacciï¿½n no pudo ser abortada");
		}
	}

	/**
	 * Metodo encargado de eliminar un servicio de una cita.
	 * @param connection
	 * @param codigo
	 * @return
	 */
	public static boolean eliminarServicioCita (Connection connection, String codigo)
	{
		logger.info("\n entre a eliminarServicioCita codigo servicio cita --> "+codigo);
		String cadena = strEliminarServicioCita;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//codigo pk de servicios cita
			ps.setObject(1, codigo);
	
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema eliminando el servicio de la cita "+e);
			
		} 
		
		
		
		return false;
	}
	
	
	/**
	 * Al cancelar una cita por instituciï¿½n, se debe generar una cita similar, con estado a reprogramar
	 * @param ac_con
	 * @param codigoPaciente
	 * @param estadoLiquidaciï¿½n
	 * @param numeroSolicitud
	 * @param codigoAgenda
	 * @param unidadConsulta
	 * @param fechaGeneracion
	 * @param horaGeneracion
	 * @param usuario
	 * @return
	 */
	public static int asignarCitaCanncelada(
						Connection con,
						int codigoPaciente,
						String estadoLiquidacion,
						int codigoCita,
						int codigoAgenda,
						int unidadConsulta,
						String fechaGeneracion,
						String horaGeneracion,
						String usuario,
						String cadenaSQL)
	{
		if(!validarDisponibilidadAgenda(con, codigoAgenda))
		{
			return -5;// Retorno -5 para poder identificar el error, caso concurrencia
		}
		
		try 
		{
			int resp0 = 0, resp1 = 0;
			
			PreparedStatementDecorator asignarCita= new PreparedStatementDecorator(con.prepareStatement(asignarCitaCanceladaIni+cadenaSQL+asignarCitaCanceladaFin,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			asignarCita.setInt(1,codigoPaciente);
			asignarCita.setString(2,estadoLiquidacion);
			asignarCita.setInt(3,codigoAgenda);
			asignarCita.setInt(4,unidadConsulta);
			asignarCita.setString(5,fechaGeneracion);
			asignarCita.setString(6,horaGeneracion);
			asignarCita.setString(7,usuario);
								
			resp0 = asignarCita.executeUpdate();
			
			if(resp0>0)
			{
				//************SE TOMA EL CODIGO DE LA CITA RECIEN INSERTADA*******************************************************************
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(obtenerUltimoCodigoCitaPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,codigoPaciente);
		
				
				int codigoCitaActual = 0;
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
					codigoCitaActual = rs.getInt("codigo");
				
				//***********************************************************************************************************
				//**********SE PASAN LOS SERVICIOS A LA NUEVA CITA**********************************************************
				pst =  new PreparedStatementDecorator(con.prepareStatement(actualizarCodigoCitaEnServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,codigoCitaActual);
				pst.setInt(2,codigoCita);
				resp1 = pst.executeUpdate();
				
				//************************************************************************************************************
				
			}
			
			if(resp0>0&&resp1>0)
				return 1;
			else
				return 0;
		}
		catch (SQLException e)
		{
			logger.error("Error asignando la cita "+e);
			return 0;
		}
	}




	/**
	* * Actualiza el estado a la cita con numero de solicitud dado
	*/
	public static ResultadoBoolean actualizarEstadoCitaTransaccional(Connection con, int codigoCita, int codEstadoCita,String motivoNoAtencion, String estado, String usuarioModifica)
	{
		ResultadoBoolean resp=new ResultadoBoolean(false);

		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
			resp = setCommit(con, false);
			if(!resp.isTrue())
			{
				resp.setDescripcion("Error insertarTransaccional Genï¿½rica en setCommit(con, false)");
				return resp;
			}

			resp = empezarTransaccion(con);
			if( resp.isTrue() == false )
			{
				resp.setDescripcion("Error insertarTransaccional Genï¿½rica empezando la transacciï¿½n");
				return resp;
			}
		}
		PreparedStatement ps = null;
		
		try
		{
			if (con == null || con.isClosed())
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexiï¿½n, sino que mandarï¿½ una excepction
				throw new SQLException ("Error SQL: Conexiï¿½n cerrada");
			}
			else
			{
				boolean puedoActualizar = true;
				
				
				//Si lo que se desea es pasar el estado de la cita a cumplida, se debe verificar que todas sus solicitudes ya hayan sido 
				//respondidas o interpretadas
				if(codEstadoCita==ConstantesBD.codigoEstadoCitaAtendida)
				{
					
					HashMap estadosSolicitud = consultarEstadosSolicitudesCita(con, codigoCita+"");
					
					logger.info("paso por aqui "+estadosSolicitud);
					
					for(int i=0;i<Integer.parseInt(estadosSolicitud.get("numRegistros").toString());i++)
					{
						int numSolicitud = Integer.parseInt(estadosSolicitud.get("numeroSolicitud_"+i).toString());
						int estadoMedico = Integer.parseInt(estadosSolicitud.get("estadoHistoriaClinica_"+i).toString());
						String estadoServicio = estadosSolicitud.get("estadoServicio_"+i).toString();
						//Si la cita todavï¿½a tiene servicios sin solicitud asociada o tiene solicitud pero solicitada
						//entonces no se puede actualizar todavï¿½a el estado de la cita a cumplida
						if(estadoServicio.equals(ConstantesIntegridadDominio.acronimoEstadoActivo)&&
							(numSolicitud == ConstantesBD.codigoNuncaValido || estadoMedico == ConstantesBD.codigoNuncaValido || estadoMedico == ConstantesBD.codigoEstadoHCSolicitada))
							puedoActualizar = false;
					}
				}
				logger.info("puedo actualizar la cita? "+puedoActualizar);
				
				if(puedoActualizar)
				{
					ps= con.prepareStatement(actualizarEstadoCitaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
					ps.setInt(1,codEstadoCita);
					if(!motivoNoAtencion.equals(""))
						ps.setString(2,motivoNoAtencion);
					else
						ps.setNull(2,Types.VARCHAR);
					ps.setString(3,usuarioModifica);
					ps.setInt(4,codigoCita);
					int resultadoUpdate = ps.executeUpdate();
	
					if( resultadoUpdate == 0 )
						return abortarTransaccion(con);
				}
			}

		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
			resp = setCommit(con, true);
			if( resp.isTrue() == false )
			{
				resp.setDescripcion("Error insertarTransaccional Genï¿½rica en setCommit(con, false) _ "+resp.getDescripcion());
				return resp;
			}
			resp = terminarTransaccion(con);
			if( resp.isTrue() == false )
			{
				resp.setDescripcion("Error insertarTransaccional Genï¿½rica empezando la transacciï¿½n _ "+resp.getDescripcion());
				return resp;
			}
		}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la actualizacion del estado: Genï¿½ricaCitaDao");
			abortarTransaccion(con);
			resp=new ResultadoBoolean(false," Error en la actualizacion del estado: Genï¿½ricaCitaDao"+e);
			return resp;
		}
		finally{
			try{
				if(ps != null){
					ps.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return resp =new ResultadoBoolean(true);
	}
	
	
	/**
	* * Actualiza el estado a la cita con codigo de cita dado
	*/
	public static ResultadoBoolean actualizarEstadoCitaXCodigoTransaccional(Connection con, String codigoCita, int codEstadoCita, String estado, String usuarioModifica)
	{
		ResultadoBoolean resp=new ResultadoBoolean(false);
		PreparedStatementDecorator ps=null;

		if( estado != null && estado.equals(ConstantesBD.inicioTransaccion) )
		{
			resp = setCommit(con, false);
			if(!resp.isTrue())
			{
				resp.setDescripcion("Error insertarTransaccional Genï¿½rica en setCommit(con, false)");
				return resp;
			}

			resp = empezarTransaccion(con);
			if( resp.isTrue() == false )
			{
				resp.setDescripcion("Error insertarTransaccional Genï¿½rica empezando la transacciï¿½n");
				return resp;
			}
		}
		try
		{
			if (con == null || con.isClosed())
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexiï¿½n, sino que mandarï¿½ una excepction
				throw new SQLException ("Error SQL: Conexiï¿½n cerrada");
			}
			else
			{
				ps= new PreparedStatementDecorator(con.prepareStatement(actualizarEstadoCitaXCodigoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1,codEstadoCita);
				ps.setString(2,usuarioModifica);
				ps.setString(3,codigoCita);
				int resultadoUpdate = ps.executeUpdate();

				if( resultadoUpdate == 0 )
					return abortarTransaccion(con);
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la actualizacion del estado: Genï¿½ricaCitaDao");
			abortarTransaccion(con);
			resp=new ResultadoBoolean(false," Error en la actualizacion del estado: Genï¿½ricaCitaDao"+e);
			return resp;
		}
		finally
		{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
		}

		if( estado != null && estado.equals(ConstantesBD.finTransaccion) )
		{
			resp = setCommit(con, true);
			if( resp.isTrue() == false )
			{
				resp.setDescripcion("Error insertarTransaccional Genï¿½rica en setCommit(con, false) _ "+resp.getDescripcion());
				return resp;
			}
			resp = terminarTransaccion(con);
			if( resp.isTrue() == false )
			{
				resp.setDescripcion("Error insertarTransaccional Genï¿½rica empezando la transacciï¿½n _ "+resp.getDescripcion());
				return resp;
			}
		}
		return resp =new ResultadoBoolean(true);
	}

	/**
	* Asigna una cita en una fuente de datos, reutilizando una conexion existente.
	* @param ac_con				Conexiï¿½n abierta con una fuente de datos
	 * @param ai_paciente		Cï¿½digo del paciente para el cual se asignarï¿½ la cita
	 * @param ai_agenda			Cï¿½digo del ï¿½tem de agenda de consulta que se asignarï¿½ como cita
	 * @param ai_unidadConsulta	Cï¿½digo de la unidad de la consulta que se le asignara a esta cita
	 * @param as_usuario			Cï¿½digo del usuario que asigna la cita
	 * @param ai_codigoSolicitud	Cï¿½digo de la solicitud generada para esta cita
	 * @param observacion Observacion de la cita
	 * @param prioridad
	* @return Cï¿½digo asignado a la cita. Si es menor que 0 es un cï¿½digo invï¿½lido
	*/
	public static int asignarCita(Connection ac_con, int ai_paciente, int ai_agenda, int ai_unidadConsulta, String as_usuario, String is_validarReserva, String is_aumentarSeqCita, HashMap mapaServicios,String estado,String secuenciaServiciosCita, String fechaSolicitada, String telefono,String prioridad,
			String motivoAutorizacionCita, String usuarioAutoriza, String citasIncumpl) throws Exception
	{
		if(!validarDisponibilidadAgenda(ac_con, ai_agenda))
		{
			return -5;// Retorno -5 para poder identificar el error, caso concurrencia
		}

		
		boolean		lb_continuar;
		int			li_codigo;
		DaoFactory	ldf_df;

		/* Obtener una instancia del objeto principal de acceso a fuente de datos */
		ldf_df = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		/* Abrir una conexï¿½on a la base de datos si es necesario */
		if(ac_con == null || ac_con.isClosed() )
			ac_con = ldf_df.getConnection();

		/* Iniciar la transacciï¿½n */
		if(estado.equals(ConstantesBD.inicioTransaccion))
			lb_continuar	= ldf_df.beginTransaction(ac_con);
		else
			lb_continuar = true;
		
		li_codigo		= -1;

		/* Validar si la asignaciï¿½n de la cita es posible */
		if(lb_continuar)
			lb_continuar = validarReservaCita(ac_con, ai_paciente, ai_agenda, is_validarReserva) ;

		/* Obtener el siguiente consecutivo a asignar */
		if(lb_continuar)
		{
			PreparedStatementDecorator	lps_ps;
			ResultSetDecorator			lrs_rs;

			/* Preparar consulta */
			lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_aumentarSeqCita,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			

			/* Ejecutar la consulta */
			lrs_rs = new ResultSetDecorator(lps_ps.executeQuery());

			/* Verificar si se obtuvo resultado de la consulta */
			if(lrs_rs.next() )
				li_codigo = lrs_rs.getInt(1);

			/* Verificar si el cï¿½digo obtenido es vï¿½lido */
			if(li_codigo < 0)
				li_codigo = -1;

			/* Liberar recursos de base de datos */
			lps_ps.close();

			lb_continuar = (li_codigo > -1);
		}

		/* Preparar la inserciï¿½n de la cita */
		if(lb_continuar)
		{
			PreparedStatementDecorator lps_ps;

			lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_asignar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/* Establecer atributos numï¿½ricos */
			lps_ps.setInt(1, li_codigo);
			lps_ps.setInt(2, ai_paciente);
			lps_ps.setInt(3, ai_agenda);
			lps_ps.setInt(4, ai_unidadConsulta);

			/* Establecer atributos de cadena */
			lps_ps.setString(5, as_usuario);
			lps_ps.setString(6, UtilidadFecha.getHoraActual());
			if(!fechaSolicitada.equals(""))
			{
			
				lps_ps.setString(7, UtilidadFecha.conversionFormatoFechaABD(fechaSolicitada));
			}
			else{
			
				lps_ps.setNull(7,Types.DATE);
			}
			lps_ps.setString(8, telefono);
			lps_ps.setString(9, prioridad);
			lps_ps.setString(10, as_usuario);

			/* Ejecutar la inserciï¿½n de la cita en la fuente de datos */
			
			lb_continuar = (lps_ps.executeUpdate() == 1);

			/* Liberar recursos de base de datos */
			lps_ps.close();
		}

		/* Preparar la inserciï¿½n de la cita */
		if(lb_continuar)
		{
			PreparedStatementDecorator lps_ps;

			lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_disminuirCuposAgenda,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			

			/* Establecer atributos numï¿½ricos */
			lps_ps.setInt(1, ai_agenda);

			/* Ejecutar la inserciï¿½n de la cita en la fuente de datos */
			lb_continuar = (lps_ps.executeUpdate() == 1);

			/* Liberar recursos de base de datos */
			lps_ps.close();
		}
		
		//Se inserta el detalle de la cita (LOS SERVICIOS)
		if(lb_continuar)
		{
			PreparedStatementDecorator lps_ps;
			boolean exito = true;
			
			Utilidades.imprimirMapa(mapaServicios);
			
			for(int i=0;i<Integer.parseInt(mapaServicios.get("numRegistros").toString());i++)
			{
				
				String consulta = insertarServicioCitaStr + " ("+secuenciaServiciosCita+",?,?,'"+ConstantesIntegridadDominio.acronimoEstadoActivo+"',?,?,?,CURRENT_DATE,substr('"+UtilidadFecha.getHoraActual()+"',0,6),?,?,?,?,?,?,?,?)";
			
				lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				lps_ps.setInt(1, li_codigo);
				lps_ps.setObject(2,mapaServicios.get("codigoServicio_"+i));
				lps_ps.setObject(3,mapaServicios.get("codigoCentroCosto_"+i));
				lps_ps.setNull(4,Types.INTEGER);
				if(mapaServicios.get("codigoEspecialidad_"+i)!=null&&!mapaServicios.get("codigoEspecialidad_"+i).toString().equals(""))
					lps_ps.setObject(5,mapaServicios.get("codigoEspecialidad_"+i));
				else
					lps_ps.setNull(5,Types.INTEGER);
				if(mapaServicios.get("observaciones_"+i)!=null&&!mapaServicios.get("observaciones_"+i).toString().equals(""))
					lps_ps.setObject(6,mapaServicios.get("observaciones_"+i));
				else
					lps_ps.setNull(6,Types.VARCHAR);
				lps_ps.setString(7,as_usuario);
				lps_ps.setObject(8,UtilidadFecha.conversionFormatoFechaABD(mapaServicios.get("fechaCita_"+i).toString()));
				lps_ps.setObject(9,mapaServicios.get("horaInicioCita_"+i));
				lps_ps.setObject(10,mapaServicios.get("horaFinCita_"+i));
				lps_ps.setObject(11,mapaServicios.get("codigoEstadoCita_"+i));
				lps_ps.setObject(12,mapaServicios.get("codigoAgenda_"+i));
				
				if(UtilidadTexto.isEmpty(mapaServicios.get("codigopo_"+i)+""))
				{
					lps_ps.setNull(13, Types.INTEGER);
				}
				else
				{
					lps_ps.setInt(13, Integer.parseInt(mapaServicios.get("codigopo_"+i).toString()));
				}
				
				
				
				if(lps_ps.executeUpdate()<=0)
					exito = false;
			}
			
			lb_continuar = exito;
		}
		
		// insertar la autorizacion de cita si es el caso de ser requerida
		// para los pacientes con citas incumplidad 
		if(lb_continuar)
		{
			// si es requerida l autorizacion 
			if(citasIncumpl.equals(ConstantesBD.acronimoSi))
			{
				// insertar autorizacion se cita
				logger.info("se insertara autorizacion de cita");
				int  consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(ac_con,"seq_autori_cita");
				PreparedStatementDecorator inserAutorizacionCita ;
				inserAutorizacionCita =  new PreparedStatementDecorator(ac_con.prepareStatement(is_autorizacion_cita,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				/* Establecer atributos */
				inserAutorizacionCita.setInt(1, consecutivo);
				inserAutorizacionCita.setInt(2, li_codigo);
				inserAutorizacionCita.setString(3, motivoAutorizacionCita);
				inserAutorizacionCita.setString(4, usuarioAutoriza);
				inserAutorizacionCita.setString(5, as_usuario);
				inserAutorizacionCita.setInt(6, ConstantesBD.codigoEstadoCitaAsignada);
				
				/* Ejecutar la inserciï¿½n de la cita en la fuente de datos */
				lb_continuar = (inserAutorizacionCita.executeUpdate()==1); 
				
				logger.info("la  i nsercion de autorizacion se inserto correctamente >>>>>>>>> "+lb_continuar);
				
				/* Liberar recursos de base de datos */
				inserAutorizacionCita.close();
			}
		}
		
		
		if(!lb_continuar)
		{
			li_codigo = -1;
			ldf_df.abortTransaction(ac_con);
		}
		else if(estado.equals(ConstantesBD.finTransaccion))
		{
			ldf_df.endTransaction(ac_con);
		}

		return li_codigo;
	}

	/**
	* Reserva una cita en una fuente de datos, reutilizando una conexion existente.
	* @param con				Conexiï¿½n abierta con una fuente de datos
	 * @param paciente		Cï¿½digo del paciente para el cual se reservarï¿½ la cita
	 * @param agenda			Cï¿½digo del ï¿½tem de agenda de consulta que se reservarï¿½ como cita
	 * @param ai_unidadConsulta	Cï¿½digo de la unidad de consulta que se reservarï¿½ a esta cita
	 * @param as_usuario			Cï¿½digo del usuario que reserva la cita
	 * @param prioridad 
	 * @param observacion @todo
	* @return Cï¿½digo asignado a la cita. Si es menor que 0 es un cï¿½digo invï¿½lido
	*/
	public static int reservarCita(Connection con, int paciente, int agenda, int ai_unidadConsulta, String as_usuario, String is_validarReserva, String is_aumentarSeqCita, HashMap mapaServicios,String secuenciaServiciosCita, String fecha_solicitada, String prioridad, 
			String motivoAutorizacionCita, String usuarioAutoriza, String requiereAuto, String verificarEstCitaPac) throws Exception
	{
		if(!validarDisponibilidadAgenda(con, agenda))
		{
			return -5;// Retorno -5 para poder identificar el error, caso concurrencia
		}
		
		boolean		lb_continuar;
		boolean 	lb_continuar_aux;
		int			li_codigo;
		DaoFactory	ldf_df;

		/* Obtener una instancia del objeto principal de acceso a fuente de datos */
		ldf_df = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		/* Verificar el estado de la conexiï¿½n y abrir una nueva si es necesario */
		if(con == null || con.isClosed() )
			con = ldf_df.getConnection();

		lb_continuar	= true;
		lb_continuar_aux= true;
		li_codigo		= -1;
		
		/* Validar si la reserva de la cita es posible */
		if(lb_continuar)
			lb_continuar = validarReservaCita(con, paciente, agenda, is_validarReserva);
		
		logger.info("motivo >>>> "+motivoAutorizacionCita);
		logger.info("usuario >>>>"+usuarioAutoriza);
		logger.info("reAuto >>>"+requiereAuto);
		logger.info("verifica >>>"+verificarEstCitaPac);
		
		if(verificarEstCitaPac.equals(ConstantesBD.acronimoSi))
		{
			if(requiereAuto.equals(ConstantesBD.acronimoSi))
				lb_continuar_aux= true;
			else
				lb_continuar_aux= false;
		}else
			lb_continuar_aux= true;
		
		
		logger.info("bandera >>>"+lb_continuar_aux);
		
		if(lb_continuar_aux)
		{
			/* Obtener el siguiente consecutivo a reservar */
			if(lb_continuar)
			{
				PreparedStatementDecorator	lps_ps;
				ResultSetDecorator			lrs_rs;
				/* Preparar consulta */
				lps_ps = new PreparedStatementDecorator(con.prepareStatement(is_aumentarSeqCita,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				/* Ejecutar la consulta */
				lrs_rs = new ResultSetDecorator(lps_ps.executeQuery());
				/* Verificar si se obtuvo resultado de la consulta */
				if(lrs_rs.next() )
					li_codigo = lrs_rs.getInt(1);
	
				/* Verificar si el cï¿½digo obtenido es vï¿½lido */
				if(li_codigo < 0)
					li_codigo = -1;
	
				/* Liberar recursos de base de datos */
				lps_ps.close();
	
				lb_continuar = (li_codigo > -1);
			}
			
			/* Preparar la inserciï¿½n de la cita */
			if(lb_continuar)
			{
				
				PreparedStatementDecorator lps_ps;
	
				lps_ps = new PreparedStatementDecorator(con.prepareStatement(is_reservar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				/* Establecer atributos numï¿½ricos */
				lps_ps.setInt(1, li_codigo);
				lps_ps.setInt(2, paciente);
				lps_ps.setInt(3, agenda);
				lps_ps.setInt(4, ai_unidadConsulta);
	
				/* Establecer atributos de cadena */
				lps_ps.setString(5, as_usuario);

				//logger.info("UtilidadFecha.getHoraActual() -->"+UtilidadFecha.getHoraActual()+"<--");
				lps_ps.setString(6,UtilidadFecha.getHoraActual());

				if(!fecha_solicitada.equals(""))
				{
					lps_ps.setString(7, UtilidadFecha.conversionFormatoFechaABD(fecha_solicitada));
				}
				else
				{
					lps_ps.setNull(7,Types.DATE);
				}
				
				lps_ps.setString(8,prioridad);
				
				lps_ps.setString(9, as_usuario);
				
				/* Ejecutar la inserciï¿½n de la cita en la fuente de datos */
				lb_continuar = (lps_ps.executeUpdate() == 1);
				
				/* Liberar recursos de base de datos */
				lps_ps.close();
			}
			
	
			/* Preparar la inserciï¿½n de la cita */
			if(lb_continuar)
			{
				PreparedStatementDecorator lps_ps;
	
				lps_ps = new PreparedStatementDecorator(con.prepareStatement(is_disminuirCuposAgenda,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				
				/* Establecer atributos numï¿½ricos */
				lps_ps.setInt(1, agenda);
	
				/* Ejecutar la inserciï¿½n de la cita en la fuente de datos */
				lb_continuar = (lps_ps.executeUpdate() == 1);
	
				/* Liberar recursos de base de datos */
				lps_ps.close();
			}
			
			
			//Se inserta el detalle de la cita (LOS SERVICIOS)
			if(lb_continuar)
			{
				PreparedStatementDecorator lps_ps;
				boolean exito = true;
				Utilidades.imprimirMapa(mapaServicios);
				
				for(int i=0;i<Integer.parseInt(mapaServicios.get("numRegistros").toString());i++)
				{
					String consulta = insertarServicioCitaStr + " ("+secuenciaServiciosCita+",?,?,'"+ConstantesIntegridadDominio.acronimoEstadoActivo+"',?,?,?,CURRENT_DATE,'"+UtilidadFecha.getHoraActual()+"',?,?,?,?,?,?,?,?)";
					lps_ps = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					lps_ps.setInt(1, li_codigo);
					lps_ps.setObject(2,mapaServicios.get("codigoServicio_"+i));
					if(Utilidades.convertirAEntero(mapaServicios.get("codigoCentroCosto_"+i).toString())>0)
					{
						
						lps_ps.setObject(3,mapaServicios.get("codigoCentroCosto_"+i));
					}
					else
					{
					
						lps_ps.setNull(3,Types.INTEGER);
					}
				
					lps_ps.setNull(4,Types.INTEGER);
					if(mapaServicios.get("codigoEspecialidad_"+i)!=null&&!mapaServicios.get("codigoEspecialidad_"+i).toString().equals(""))
					{
					
						lps_ps.setObject(5,mapaServicios.get("codigoEspecialidad_"+i));
					}
					else
					{
					
						lps_ps.setNull(5,Types.INTEGER);
					}
					if(mapaServicios.get("observaciones_"+i)!=null&&!mapaServicios.get("observaciones_"+i).toString().equals(""))
					{
					
						lps_ps.setObject(6,mapaServicios.get("observaciones_"+i));
					}
					else
					{
					
						lps_ps.setNull(6,Types.VARCHAR);
					}
					lps_ps.setString(7,as_usuario);
					lps_ps.setObject(8,UtilidadFecha.conversionFormatoFechaABD(mapaServicios.get("fechaCita_"+i).toString()));
					lps_ps.setObject(9,mapaServicios.get("horaInicioCita_"+i));
					lps_ps.setObject(10,mapaServicios.get("horaFinCita_"+i));
					lps_ps.setObject(11,mapaServicios.get("codigoEstadoCita_"+i));
					lps_ps.setObject(12,mapaServicios.get("codigoAgenda_"+i));
					
					if(UtilidadTexto.isEmpty(mapaServicios.get("codigopo_"+i)+""))
					{
					
						lps_ps.setNull(13, Types.INTEGER);
					}
					else
					{
					
						lps_ps.setInt(13, Integer.parseInt(mapaServicios.get("codigopo_"+i).toString()));
					}
					
					
					
					if(lps_ps.executeUpdate()<=0)
						exito = false;
				}
				
				lb_continuar = exito;
			}
			
	
			// insertar la autorizacion de la reserva 
			// insertar la autorizacion de cita si es el caso de ser requerida
			// para los pacientes con citas incumplidad 
			if(lb_continuar)
			{
				if(UtilidadTexto.getBoolean(requiereAuto))
				{
					// insertar autorizacion se cita
					logger.info("se insertara autorizacion de cita");
					int  consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_autori_cita");
					PreparedStatementDecorator inserAutorizacionCita ;
					inserAutorizacionCita =  new PreparedStatementDecorator(con.prepareStatement(is_autorizacion_cita,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					/* Establecer atributos */
					inserAutorizacionCita.setInt(1, consecutivo);
					inserAutorizacionCita.setInt(2, li_codigo);
					inserAutorizacionCita.setString(3, motivoAutorizacionCita);
					inserAutorizacionCita.setString(4, usuarioAutoriza);
					inserAutorizacionCita.setString(5, as_usuario);
					inserAutorizacionCita.setInt(6, ConstantesBD.codigoEstadoCitaReservada);
					
					/* Ejecutar la inserciï¿½n de la cita en la fuente de datos */
					lb_continuar = (inserAutorizacionCita.executeUpdate()==1); 
					
					logger.info("la  insercion de autorizacion se inserto correctamente >>>>>>>>> "+lb_continuar);
					
					/* Liberar recursos de base de datos */
					inserAutorizacionCita.close();
				}
			}
		}else
			lb_continuar = false;
		
		if(!lb_continuar)
		{
			li_codigo = -1;
		}
		
		return li_codigo;
		
	}

	private static boolean validarDisponibilidadAgenda(Connection con, int agenda) {
		String sentencia="SELECT codigo FROM consultaexterna.agenda WHERE codigo=? AND cupos>0";
		try {
			logger.info(sentencia);
			logger.info(agenda);
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			psd.setInt(1, agenda);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			boolean resultado=false;
			if(rsd.next())
			{
				resultado=true;
			}
			return resultado;
		} catch (SQLException e) {
			logger.info("Error verificando concurrecnia asignaciï¿½n/reserva citas", e);
			return false;
		}
	}

	/**
	* Valida si el paciente tiene o no citas reservada para la fecha de un ï¿½tem de agenda
	* especificado
	* @param ac_con				Conexiï¿½n abierta con una fuente de datos
	* @param ai_paciente		Cï¿½digo del paciente para el cual se verifica el nï¿½mero de cita
	*							asignadas
	* @param ai_agenda			Cï¿½digo del ï¿½tem de agenda de consulta paa el cual se verifica el
	*							nï¿½mero de cita asignadas
	* @return true si el paciente tiene citas reservadas para la fecha de ï¿½tem de agenda de consulta
	* especificado. false de lo contrario
	*/
	public static boolean validarReservaCitaFecha( Connection	con, int paciente, int agenda, String validarCitasFecha) throws Exception
	{
		long				cantidadCitas;
		ResultSetDecorator			rs;
		PreparedStatementDecorator	ps;

		/* Preparar la consulta */
		logger.info(validarCitasFecha+" "+paciente+" "+agenda);
		ps = new PreparedStatementDecorator(con.prepareStatement(validarCitasFecha,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		/* Establecer atributos numï¿½ricos de la consulta */
		ps.setInt(1, paciente);
		ps.setInt(2, agenda);

		/* Obtener el nï¿½mero de conflictos de citas del paciente */
		rs = new ResultSetDecorator(ps.executeQuery());

		if(rs.next() )
			cantidadCitas = rs.getLong(1);
		else
			cantidadCitas = 0;

		rs.close();
		ps.close();
		logger.info("hay fecha igual "+(cantidadCitas > 0)+" cant "+cantidadCitas);

		return cantidadCitas > 0;
	}
	
	/**
	* Valida si el paciente tiene o no citas reservada para la fecha y hora de un ï¿½tem de agenda
	* especificado
	* @param con				Conexiï¿½n abierta con una fuente de datos
	* @param paciente		Cï¿½digo del paciente para el cual se verifica el nï¿½mero de cita
	*							asignadas
	* @param agenda			Cï¿½digo del ï¿½tem de agenda de consulta paa el cual se verifica el
	*							nï¿½mero de cita asignadas
	* @return true si el paciente tiene citas reservadas para la fecha y hora de ï¿½tem de agenda de consulta
	* especificado. false de lo contrario
	*/
	public static boolean validarReservaCitaFechaHora( Connection	con, int paciente, int agenda, String validarCitasFechaHora) throws Exception
	{
		long				cantidadCitas;
		ResultSetDecorator			rs;
		PreparedStatementDecorator	ps;

		/* Preparar la consulta */
		logger.info("\n\n\n\n*******************************************************");
		logger.info("Consulta: "+validarCitasFechaHora);
		logger.info("paciente: "+paciente+" agenda: "+agenda);
		logger.info("\n\n\n\n*******************************************************");
		ps = new PreparedStatementDecorator(con.prepareStatement(validarCitasFechaHora,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		/* Establecer atributos numï¿½ricos de la consulta */
		ps.setInt(1, paciente);
		ps.setInt(2, agenda);

		/* Obtener el nï¿½mero de conflictos de citas del paciente */
		rs = new ResultSetDecorator(ps.executeQuery());

		if(rs.next() )
			cantidadCitas = rs.getLong(1);
		else
			cantidadCitas = 0;

		rs.close();
		ps.close();
		return cantidadCitas > 0;
	}

	/**
	* Asignar el nï¿½mero de solicitud a una cita reservada
	* @param ac_con				Conexiï¿½n abierta con una fuente de datos
	* @param ai_codigo			Cï¿½digo ï¿½nico de la cita a modificar
	* @param ai_numeroSolicitud	Nï¿½mero de solicitud a asignar
	* @return El nï¿½mero de citas modificadas
	*/
	@SuppressWarnings("rawtypes")
	public static int asignarSolicitud(
		Connection	ac_con,
		int			ai_codigo,
		int			ai_numeroSolicitud,
		int 		codigoServicio,
		String loginUsuario,
		String 		estado
	)throws Exception
	{
		PreparedStatement	pst=null;
		PreparedStatement	pst2=null;
		int					li_resp=0;
		try{
		DaoFactory			ldf_df;
		/* Obtener una instancia del objeto principal de acceso a fuente de datos */
		ldf_df = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		/* Preparar la modificaciï¿½n */
			pst = ac_con.prepareStatement(is_asignarSolicitud,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);

		/* Establecer atributos numï¿½ricos de la consulta */
			pst.setInt(1, ai_numeroSolicitud);
			pst.setString(2,loginUsuario);
			pst.setString(3, UtilidadFecha.getHoraActual());
			pst.setInt(4, ai_codigo);
			pst.setInt(5, codigoServicio);
		
		
			li_resp = pst.executeUpdate();
		logger.info("SE ASIGNï¿½ SOLICITUD A LA CITA? "+li_resp);
		if(li_resp>0)
		{
			//Antes de cambiar el estado de liquidacion, se verifica que todos los servicios de la cita ya tengan solicitud
			HashMap estadosSolicitudes = consultarEstadosSolicitudesCita(ac_con, ai_codigo+"");
			
			int numServiciosPendientes = 0; //nï¿½mero de servicios pendientes por solicitud
			
			for(int i=0;i<Integer.parseInt(estadosSolicitudes.get("numRegistros").toString());i++)
				if(Integer.parseInt(estadosSolicitudes.get("numeroSolicitud_"+i).toString())==ConstantesBD.codigoNuncaValido&&
						estadosSolicitudes.get("estadoServicio_"+i).toString().equals(ConstantesIntegridadDominio.acronimoEstadoActivo))
					numServiciosPendientes ++;
			
			//Si no hay servicios pendientes ya se puede actualizar el estado de la liquidacion de la cita
			if(numServiciosPendientes==0)
			{
				logger.info("VOY A ACTUALIZAR EL ESTADO DE LA LIQUDIACION!!!!");
					pst2 = ac_con.prepareStatement(is_asignarSolicitud_2, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
					pst2.setInt(1, ai_codigo);
				
					li_resp = pst2.executeUpdate();
				logger.info("SE ASIGNï¿½ SOLICITUD EL ESTADO DE LIQUIDACION A LA CITA? "+li_resp);
			}
		}

		/* Asignar el nï¿½mero de solicitud a la cita */
		if( li_resp == 1)
		{
			/* El resultado de generaciï¿½n de solicitud fue exitoso */
			if(estado.equals(ConstantesBD.finTransaccion))
				ldf_df.endTransaction(ac_con);
		}
		else
		{
			li_resp = -1;
			logger.info("ABORTï¿½ TRANSACCION!!!!");
			/* Se presentaron errores en la generaciï¿½n de la solicitud */
			ldf_df.abortTransaction(ac_con);
		}
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerCondicionesTomaXServicio", e);
		}
		finally{
			try{
				if(pst2 != null){
					pst2.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return li_resp;
	}

	/**
	* Cancela una cita en una fuente de datos, reutilizando una conexion existente.
	* @param ac_con					Conexiï¿½n abierta con una fuente de datos
	* @param ai_codigo				Cï¿½digo de la cita a modificar
	* @param ai_agenda				Cï¿½digo del ï¿½tem de agenda de consulta que se actualizarï¿½ en la
	*								lista
	* @param ai_estadoCita			Nuevo estado de la cita
	* @param as_motivoCancelacion	Motivo de cancelaciï¿½n de la cita
	* @return Indicador de exito de la operaciï¿½n de modificaciï¿½n de la cita
	*/
	public static boolean cancelarCita(
		Connection	con,
		int			ai_codigo,
		int			ai_agenda,
		int			ai_estadoCita,
		String		as_motivoCancelacion,
		boolean cupoLibre,
		String usuario,
		String codigoMotivoCancelacion,
		String loginUsuario
	)throws Exception
	{
		boolean		lb_continuar;
		DaoFactory	ldf_df;

		/* Obtener una instancia del objeto principal de acceso a fuente de datos */
		ldf_df = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		/* Verificar el estado de la conexiï¿½n y abrir una nueva si es necesario */
		if(con == null || con.isClosed() )
			con = ldf_df.getConnection();

		lb_continuar	= true;

		
		/* Determinar si se debe actualizar el cupo en la agenda anterior */
		if(lb_continuar && ai_agenda > -1	&& (ai_estadoCita==ConstantesBD.codigoEstadoCitaCanceladaPaciente || (ai_estadoCita==ConstantesBD.codigoEstadoCitaCanceladaInstitucion && cupoLibre)))
		{
			PreparedStatementDecorator lps_ps;
			lps_ps =  new PreparedStatementDecorator(con.prepareStatement(is_aumentarCuposAgenda,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			/* Establecer atributos numï¿½ricos */
			lps_ps.setInt(1, ai_agenda);
			/* Ejecutar el aumento de cupos de la agenda */
			lb_continuar = (lps_ps.executeUpdate() == 1);
			/* Liberar recursos de base de datos */
			lps_ps.close();
		}

		/* Actualizar la informaciï¿½n de la cita */
		if(lb_continuar)
		{
			PreparedStatementDecorator lps_ps;

			lps_ps =  new PreparedStatementDecorator(con.prepareStatement(is_cancelar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			/* Establecer atributos numï¿½ricos */
			lps_ps.setInt(1, ai_agenda);
			lps_ps.setInt(2, ai_estadoCita);
			lps_ps.setInt(8, ai_codigo);
			/* Asignar atributos de texto */
			lps_ps.setString(4, as_motivoCancelacion);
			lps_ps.setString(3, codigoMotivoCancelacion);
			
			//Agrego por tarea 
			lps_ps.setString(5, loginUsuario);
			lps_ps.setString(6, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			lps_ps.setString(7, UtilidadFecha.getHoraActual());
			
			/* Ejecutar el aumento de cupos de la agenda */
			lb_continuar = (lps_ps.executeUpdate() == 1);
			/* Liberar recursos de base de datos */
			lps_ps.close();
		}
		
		//********ANULACION DE LOS SERVICIOS DE LA CITA*******************************************
		//Solo se anulan si no hay que dejar el cupo libre de la cancelacion
		if(lb_continuar&&!cupoLibre)
		{
			//Se consultan los servicios de la cita
			HashMap campos = new HashMap();
			campos.put("codigoCita", ai_codigo);
			campos.put("usuario", usuario);
			campos.put("institucion", "0");
			HashMap resultados = consultarServiciosCita(con, campos);
			
			for(int j=0;j<Integer.parseInt(resultados.get("numRegistros").toString());j++)
			{
				//Se anula cada uno de los servicios
				campos.put("codigoServicio",resultados.get("codigoServicio_"+j));
				campos.put("numeroSolicitud",resultados.get("numeroSolicitud_"+j));
				campos.put("validarCitaCumplida",ConstantesBD.acronimoNo);
				if(anularServicioCita(con, campos)<=0)
					lb_continuar = false;
						
			}
		}
		//*****************************************************************************************

		
		return lb_continuar;
	}

	/**
	* Valida si el paciente tiene o no conflicto de citas con el ï¿½tem de agenda especificado
	* @param ac_con				Conexiï¿½n abierta con una fuente de datos
	* @param ai_paciente		Cï¿½digo del paciente para el cual se reserva la cita
	* @param ai_agenda			Cï¿½digo del ï¿½tem de agenda de consulta que se reservarï¿½ como cita
	* @return true si el paciente puede reservar una cita en la agenda espececificada, false de lo
	* contrario
	*/
	private static boolean validarReservaCita(
		Connection	ac_con,
		int			ai_paciente,
		int			ai_agenda,
		String is_validarReserva
	)throws SQLException
	{
		long				ll_conflictos;
		ResultSetDecorator			lrs_rs;
		PreparedStatementDecorator	lps_ps;

		/* Preparar la consulta */
		lps_ps = new PreparedStatementDecorator(ac_con.prepareStatement(is_validarReserva,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		/* Establecer atributos numï¿½ricos de la consulta */
		lps_ps.setInt(1, ai_paciente);
		lps_ps.setInt(2, ai_agenda);

		/* Obtener el nï¿½mero de conflictos de citas del paciente */
		lrs_rs = new ResultSetDecorator(lps_ps.executeQuery());

		if(lrs_rs.next() )
			ll_conflictos = lrs_rs.getLong(1);
		else
			ll_conflictos = 0;

		return ll_conflictos < 1;
	}

	/**
	 * Modifica la programaciï¿½n de varias cita en una fuente de datos, reutilizando una conexiï¿½n
	 * existente
	 * @param con		Conexiï¿½n abierta con una fuente de datos
	 * @param listadoCitas	Conjunto de citas a reprogramar
	 * @param secuenciaServiciosCita 
	 * @param usuario
	 * @return Indicador de ï¿½xito de la reprogramaciï¿½n de las citas
	*/
	public static boolean reprogramarCitas(
		Connection	con,
		Collection<Cita>	listadoCitas,
		String is_validarReserva,
		String secuenciaServiciosCita, String usuario
	)throws Exception
	{
		boolean		continuarTransaccion;

		/* Iniciar una nueva transacción */
		continuarTransaccion = (listadoCitas != null && !listadoCitas.isEmpty() );

		if(continuarTransaccion)
		{
			continuarTransaccion	= UtilidadBD.iniciarTransaccion(con);
		}
		
		/* Eliminar la programación de las citas  y aumentar el cupo de sus agendas */
		if(continuarTransaccion)
		{
			Iterator<Cita>		iterador;
			StringBuffer	lsb_citas;

			iterador		= listadoCitas.iterator();
			lsb_citas	= new StringBuffer();

			/* Obtener los cï¿½digos de las citas a eliminar la programaciï¿½n */
			while(iterador.hasNext() )
			{
				Cita cita = (Cita)iterador.next();
				
				//Pero solo se toman aquellas a las cuales se les haya cambiado el estadp
				if(cita.isCambioEstadoCita())
				{
					if(iterador.hasNext())
					{
						lsb_citas.append(cita.getCodigo() + ",");
					}
					else
					{
						lsb_citas.append(cita.getCodigo());
					}
				}
			}

			/* Determinar si obtuvieron cï¿½digos */
			if(lsb_citas.length() > 0)
			{
				PreparedStatementDecorator	lps_cupos;
				String				ls_cupos;

				/* Preparar la actualizaciï¿½n de cupos */
				ls_cupos	= is_aumentarCuposAgendas.replaceFirst("\\?", lsb_citas.toString() );
				lps_cupos	= new PreparedStatementDecorator(con, ls_cupos);

				/* Ejecutar la actualizaciï¿½n de cupos de la agenda */
				try
				{
					lps_cupos.executeUpdate();
				}
				catch(SQLException lse_se)
				{
					continuarTransaccion = false;
				}

				if(continuarTransaccion)
				{
					/* Preparar le eliminaciï¿½n de la programaciï¿½n */
					PreparedStatementDecorator	lps_citas;
					String				ls_citas;

					ls_citas	= is_eliminarProgramacion.replaceFirst("\\?", lsb_citas.toString() );
					lps_citas	= new PreparedStatementDecorator(con, ls_citas);

					try
					{
						lps_citas.executeUpdate();
					}
					catch(SQLException lse_se)
					{
						continuarTransaccion = false;
					}

					lps_citas.close();
				}

				lps_cupos.close();
			}
		}
		
		logger.info("2) SE AUMENTï¿½ EL CUPO Y SE ELIMINï¿½ LA REPROGRAMACION :"+continuarTransaccion);

		/* Reprogamar las citas con estado 'A Reprogramar' y disminuir el cupo en la agenda */
		if(continuarTransaccion)
		{			
			Iterator<Cita>			li_i;
			Cita				lc_cita;
			StringBuffer		lsb_citas;
			PreparedStatementDecorator psdReprogramar = new PreparedStatementDecorator(con, is_reprogramar);
			PreparedStatementDecorator psdEliminarServicios = null, psdInsertarAutorizacion = null;
			li_i		= listadoCitas.iterator();
			lsb_citas	= new StringBuffer();
			/* Reprogramar la cita */
			
			/* Obtener los cï¿½digos de las citas a reprogramar */
			while(li_i.hasNext() && continuarTransaccion)
			{
				lc_cita = (Cita)li_i.next();

				if(lc_cita.getCodigoEstadoCita() == ConstantesBD.codigoEstadoCitaReprogramada && lc_cita.isCambioEstadoCita())
				{
					/* Validar que la cita no tenga conflictos con las citas ya programadas */
					continuarTransaccion =
						validarReservaCita(
							con, lc_cita.getCodigoPaciente(), lc_cita.getCodigoAgenda(), is_validarReserva
						);

					/* Reprogramar la cita */
					if(continuarTransaccion)
					{
						/*
							Incluir la cita en la lista de citas cuya agenda debe disminuir su cupo
						*/
						lsb_citas.append(lc_cita.getCodigo() + ",");

						if(!validarDisponibilidadAgenda(con, lc_cita.getCodigoAgenda())){
							psdReprogramar.close();
							return false;
						}
						
						psdReprogramar.setInt(1, lc_cita.getCodigoAgenda() );
						psdReprogramar.setInt(2, lc_cita.getCodigoUnidadConsulta() );
						psdReprogramar.setString(3, usuario);
						psdReprogramar.setInt(4, lc_cita.getCodigo() );
						
						continuarTransaccion = psdReprogramar.executeUpdate() == 1;
						
						//---------------------------------
						// insercion de autorizacion de reprogramacion de citas
						if(UtilidadTexto.getBoolean(lc_cita.getRequiereAuto()))
						{
							int  consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_autori_cita");
							psdInsertarAutorizacion = new PreparedStatementDecorator(con.prepareStatement(is_autorizacion_cita,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							psdInsertarAutorizacion.setInt(1, consecutivo);
							psdInsertarAutorizacion.setInt(2, lc_cita.getCodigo());
							psdInsertarAutorizacion.setString(3, lc_cita.getMotivoAutorizacionCita());
							psdInsertarAutorizacion.setString(4, lc_cita.getUsuarioAutoriza());
							psdInsertarAutorizacion.setString(5, lc_cita.getCodigoUsuario());
							psdInsertarAutorizacion.setInt(6, ConstantesBD.codigoEstadoCitaReprogramada);
							
							continuarTransaccion = (psdInsertarAutorizacion.executeUpdate() == 1);
							
							logger.info("3.2) REALICï¿½ LA AUTORIZACION DE REPROGRAMACION DE CITA :"+continuarTransaccion);
						}
						
						//---------------------------------
						
						//Si la cita no tiene solicitudes y se cambiï¿½ su unidad de agenda, entonces se eliminan sus servicios
						if(continuarTransaccion&&!lc_cita.isTieneSolicitudes()&&lc_cita.isCambioUnidadAgenda())
						{
							psdEliminarServicios		= new PreparedStatementDecorator(con.prepareStatement(eliminarServiciosCitaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
							psdEliminarServicios.setInt(1,lc_cita.getCodigo());
							continuarTransaccion = psdEliminarServicios.executeUpdate() > 0;
							
						}			
					}
				}
			}

			UtilidadBD.cerrarObjetosPersistencia(psdReprogramar, null, null);

			/* Determinar si obtuvieron cï¿½digos */
			if(continuarTransaccion && lsb_citas.length() > 0)
			{
				PreparedStatementDecorator	lps_cupos;
				String				ls_cupos;

				lsb_citas.append("-1");

				/* Preparar la actualizaciï¿½n de cupos */
				ls_cupos	= is_disminuirCuposAgendas.replaceFirst("\\?", lsb_citas.toString() );
				lps_cupos	= new PreparedStatementDecorator(con.prepareStatement(ls_cupos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

				/* Ejecutar la actualizaciï¿½n de cupos de la agenda */
				continuarTransaccion = lps_cupos.executeUpdate() > 0;
				lps_cupos.close();
			}
		}
		
		logger.info("4) SE DISMINUYERON LOS CUPOS DE LA AGENDA :"+continuarTransaccion);
		
		logger.info("\n\n\n\n\n [mapa servicios sqlbase]");
		
		
		/* Modificacion de los servicios de las citas*/
		if(continuarTransaccion)
		{
			
			Iterator			li_i;
			Cita				lc_cita;
			
			li_i		= listadoCitas.iterator();
			
			/* Obtener los cï¿½digos de las citas a reprogramar */
			while(li_i.hasNext() && continuarTransaccion)
			{
				lc_cita = (Cita)li_i.next();
				//Solo se modifican los servicios de las citas que no tenï¿½an solicitudes cargadas
				
				if(!lc_cita.isTieneSolicitudes())
				{
					int numServicios = Integer.parseInt(lc_cita.getMapaServicios("numRegistros").toString());
					//Se iteran los servicios de la cita
					for(int i=0;i<numServicios;i++)
					{
						if(continuarTransaccion)
						{
							int codigoServicio = Integer.parseInt(lc_cita.getMapaServicios("codigoServicio_"+i).toString());
							String codigoEspecialidad = lc_cita.getMapaServicios("codigoEspecialidad_"+i).toString();
							String observaciones = lc_cita.getMapaServicios("observaciones_"+i).toString();
							//Se toman las observaciones originales
							String observacionesOriginal = lc_cita.getMapaServicios("observacionesOriginal_"+i)==null?"":lc_cita.getMapaServicios("observacionesOriginal_"+i).toString();
							//Se toma el estado del servicio 
							String estadoServicio = lc_cita.getMapaServicios("estadoServicio_"+i)==null?"":lc_cita.getMapaServicios("estadoServicio_"+i).toString();
							//Se toma el centro de costo
							int codigoCentroCosto = Integer.parseInt(lc_cita.getMapaServicios("codigoCentroCosto_"+i).toString());
							//Se toma el centro de costo original
							int codigoCentroCostoOriginal = (lc_cita.getMapaServicios("codigoCentroCostoOriginal_"+i)==null||lc_cita.getMapaServicios("codigoCentroCostoOriginal_"+i).toString().equals(""))?0:Integer.parseInt(lc_cita.getMapaServicios("codigoCentroCostoOriginal_"+i).toString());
							
							//Se toman datos adicionales de la cita
							String fechaCita = lc_cita.getMapaServicios("fechaCita_"+i).toString();
							String horaInicioCita = lc_cita.getMapaServicios("horaInicioCita_"+i).toString();
							String horaFinCita = lc_cita.getMapaServicios("horaFinCita_"+i).toString();
							String codigoEstadoCita = lc_cita.getMapaServicios("codigoEstadoCita_"+i).toString();
							String codigoAgenda = lc_cita.getMapaServicios("codigoAgenda_"+i).toString();
							
							logger.info("==>"+codigoAgenda);
							String codigopo= lc_cita.getMapaServicios("codigopo_"+i)==null?"":lc_cita.getMapaServicios("codigopo_"+i).toString();
							logger.info("==>"+codigopo);
							
							HashMap campos = new HashMap();
							
							logger.info("ESTADO DEL SERVICIO=> *"+estadoServicio+"*");
							logger.info("COIDGO CENTRO COSTO=> *"+codigoCentroCosto+"*");
							logger.info("COIDGO CENTRO COSTO ORIGINAL=> *"+codigoCentroCostoOriginal+"*");
							logger.info("OBSERVACIONES=> *"+observaciones+"*");
							logger.info("OBSERVACIONES ORIGINAL=> *"+observacionesOriginal+"*");
							
							//Se verifica si el servicio se debe insertar, modificar o anular
							//1) INSERTAR : si no hay estado del servicio quiere decir que es un registro nuevo ***************************
							if(estadoServicio.equals(""))
							{
								
								campos.put("secuencia",secuenciaServiciosCita);
								campos.put("codigoCita",lc_cita.getCodigo()+"");
								campos.put("codigoServicio",codigoServicio+"");
								campos.put("codigoCentroCosto",codigoCentroCosto+"");
								campos.put("codigoEspecialidad",codigoEspecialidad);
								campos.put("observaciones",observaciones);
								campos.put("usuario",lc_cita.getCodigoUsuario());
								campos.put("fechaCita",fechaCita);
								campos.put("horaInicioCita",horaInicioCita);
								campos.put("horaFinCita",horaFinCita);
								campos.put("codigoEstadoCita",codigoEstadoCita);
								campos.put("codigoAgenda",codigoAgenda);
								campos.put("codigopo",codigopo);
								
								continuarTransaccion = insertarServicioCita(con, campos) == 1;
								logger.info("5.1) SE aï¿½adiï¿½ nuevo servicio :"+continuarTransaccion);
							}
							//2) ANULAR : si el estado es anulado quiere decir que se debe anular ****************************************
							else if (estadoServicio.equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
							{
								campos.put("codigoCita",lc_cita.getCodigo()+"");
								campos.put("codigoServicio",codigoServicio+"");
								campos.put("usuario",lc_cita.getCodigoUsuario());
								campos.put("numeroSolicitud",""); //es cita que no tiene solicitud
								campos.put("validarCitaCumplida", ConstantesBD.acronimoNo);
								
								continuarTransaccion = anularServicioCita(con, campos) == 1;
								logger.info("5.2) SE anulï¿½ servicio :"+continuarTransaccion);
							}
							//3) MODIFICAR: si se modificï¿½ el centro de costo o las observaciones del servicio
							else if (codigoCentroCosto != codigoCentroCostoOriginal || !observaciones.equals(observacionesOriginal))
							{
								campos.put("codigoCita",lc_cita.getCodigo()+"");
								campos.put("codigoServicio",codigoServicio+"");
								campos.put("usuario",lc_cita.getCodigoUsuario());
								campos.put("codigoCentroCosto",codigoCentroCosto+"");
								campos.put("observaciones",observaciones);
								campos.put("codigopo",codigopo);
								
								continuarTransaccion = modificarServicioCita(con, campos) == 1;
								logger.info("5.3) SE modificï¿½ servicio :"+continuarTransaccion);
							}
						}
						
					}
				}
				else
				{
	//*********************hace la modificarcion a la tabla servicios_cita para colocar el indicativo 
	//******************** de post operatoriio a los servicios que correspondan
					logger.info("SE modificï¿½ servicio [indicativo post operatorio] :"+continuarTransaccion);
					int numServicios = Integer.parseInt(lc_cita.getMapaServicios("numRegistros").toString());
					logger.info("numero de servicio >>>> "+numServicios);
					logger.info("SE modificï¿½ servicio [numServicios] :"+numServicios);
					for(int i=0;i<numServicios;i++)
					{
						if(continuarTransaccion)
						{
							int codigoServicio = Integer.parseInt(lc_cita.getMapaServicios("codigoServicio_"+i).toString());
							String codigopo = null;
							if(lc_cita.getMapaServicios("codigopo_"+i)!=null){
								if(!lc_cita.getMapaServicios("codigopo_"+i).equals("null"))
									codigopo = lc_cita.getMapaServicios("codigopo_"+i).toString();
								else
									codigopo = "";
							}else
								codigopo = "";
							
							logger.info("codigoop >>>>>>>> "+codigopo);
							
							HashMap campos = new HashMap();
							//Se modifica el indicativo post operatorio
								campos.put("codigoServicio",codigoServicio+"");
								campos.put("usuario",lc_cita.getCodigoUsuario());
								campos.put("codigopo",codigopo);
								campos.put("codigoCita",lc_cita.getCodigo()+"");
								
								Utilidades.imprimirMapa(campos);
								
								continuarTransaccion = modificarIndicativoServicioCita(con, campos) == 1;
								logger.info("SE modificï¿½ servicio [indicativo post operatorio] :"+continuarTransaccion);
						}
					}				
				}			
			}
			
		}

		/* Terminar la transacciï¿½n */
		if(continuarTransaccion){
		
			UtilidadBD.finalizarTransaccion(con);
		}
		else{
			UtilidadBD.abortarTransaccion(con);
		}
		
		logger.info("valor de la bandera de incerrcion >>>>>> "+continuarTransaccion);
		return continuarTransaccion;
	}
	
	/**
	 * Mï¿½todo que realiza la modificacion del servicio de la cita
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int modificarServicioCita(Connection con,HashMap campos)
	{
		try
		{
			logger.info("mapa campos: "+campos);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarServicioCitaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setObject(1,campos.get("codigoCentroCosto"));
			pst.setObject(2,campos.get("usuario"));
			pst.setObject(3,campos.get("observaciones"));
			if(Utilidades.convertirAEntero(campos.get("codigopo")+"")>0)
				pst.setObject(4,campos.get("codigopo"));
			else
				pst.setNull(4,Types.INTEGER);
			pst.setObject(5,campos.get("codigoCita"));
			pst.setObject(6,campos.get("codigoServicio"));
			
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en modificarServicioCita: "+e);
			return 0;
		}
	}
	
	/**
	 * Mï¿½todo que realiza la modificacion del indicativo postoperatorio en servicio de la cita
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int modificarIndicativoServicioCita(Connection con,HashMap campos)
	{
	String sentencia= "UPDATE servicios_cita SET " +
											" control_post_operatorio_cx=? " +
											" WHERE codigo_cita = ? AND servicio = ? ";
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(sentencia, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			if(UtilidadTexto.isEmpty(campos.get("codigopo")+""))
			{
				pst.setNull(1, Types.INTEGER);
			}
			else
			{
				pst.setInt(1, Integer.parseInt(campos.get("codigopo").toString()));
			}
			pst.setObject(2,campos.get("codigoCita"));
			pst.setObject(3,campos.get("codigoServicio"));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en modificarServicioCita: "+e);
			return 0;
		}
	}
	
	
	

	/**
	* Obtiene los datos de una cita, reutilizando una conexiï¿½n existente
	* @param ac_con		Conexiï¿½n abierta con una fuente de datos
	* @param ai_codigo	Cï¿½digo ï¿½nico de la cita a consultar
	* @return Datos de la cita solicitada
	*/
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static HashMap detalleCita(Connection ac_con, int ai_codigo, String is_detalle)throws Exception
	{
		HashMap			ldb_db=null;
		List				ll_l;
		PreparedStatement lps_ps=null;
		ResultSet rs=null;
		try{
		/* Verificar el estado de la conexiï¿½n y abrir una nueva si es necesario */
		if(ac_con == null || ac_con.isClosed() )
			ac_con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();

		/* Preparar la consulta sobre la cita */
			lps_ps = ac_con.prepareStatement(is_detalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);

		/* Establecer los atributos de la bï¿½squeda */
		lps_ps.setInt(1, ai_codigo);

			rs=lps_ps.executeQuery();
			
			Collection coleccion=new ArrayList();
			if(rs!=null && rs.getMetaData()!=null){
				ResultSetMetaData ars_rsm=rs.getMetaData();
				while(rs.next())
				{
					int numColumnas=ars_rsm==null?0:Utilidades.convertirAEntero(ars_rsm.getColumnCount()+"");
					HashMap mapa=new HashMap();
					for(int i=1;i<=ars_rsm.getColumnCount();i++)
					{
						mapa.put((ars_rsm.getColumnLabel(i)).toLowerCase()+"",rs.getObject(ars_rsm.getColumnLabel(i))==null||rs.getObject(ars_rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(ars_rsm.getColumnLabel(i)));
					}
					coleccion.add(mapa);
				}
			}
		/* Obtener el conjunto soluciï¿½n de la bï¿½squeda */
			ll_l = (List)coleccion;
		/* El conjunto soluciï¿½n solo puede tener un elemento */
		if(ll_l.size() == 1)
			ldb_db = (HashMap) ll_l.get(0);
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerCondicionesTomaXServicio", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(lps_ps != null){
					lps_ps.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return ldb_db;
	}

	/**
	 * Mï¿½todo que implementa la bï¿½squeda de citas(Con toda la informaciï¿½n
	 * necesaria para imprimirlas) para una BD genï¿½rica
	 * 
	* @see com.princetonsa.dao.CitaDao#listarCitas(String , boolean , String , boolean , String , boolean , String , boolean , int , boolean )
	 */
	public static Collection listarCitas(String fechaInicio, boolean buscarFechaInicio, String fechaFin, boolean buscarFechaFin, String horaInicio, boolean buscarHoraInicio, String horaFin, boolean buscarHoraFin, int codigoCita[], boolean buscarCodigo) 
	{
		String nuevoListarCitas=listarCitaImpresionStr;
		if (buscarFechaInicio)
		{
			nuevoListarCitas = nuevoListarCitas + " and (a.fecha || ''>= '" +fechaInicio + "' or a.fecha IS NULL)";
		}
		if (buscarFechaFin)
		{
			nuevoListarCitas = nuevoListarCitas + " and (a.fecha || ''<= '" +fechaFin + "' or a.fecha IS NULL)";
		}
		
		if (buscarHoraInicio)
		{
			nuevoListarCitas = nuevoListarCitas + " and (a.hora_inicio || ''>='" + horaInicio +"' or a.hora_inicio IS NULL)"; 
		}
		if (buscarHoraFin)
		{
			nuevoListarCitas = nuevoListarCitas + " and (a.hora_inicio || ''<='" + horaFin +"' or a.hora_inicio IS NULL)"; 
		}
		if (buscarCodigo&&codigoCita.length>0)
		{
			nuevoListarCitas = nuevoListarCitas + " and ( ";
			for (int i=0;i<codigoCita.length;i++)
			{
				if (i!=0)
				{
					nuevoListarCitas = nuevoListarCitas + " or ";
				}
				nuevoListarCitas = nuevoListarCitas + " c.codigo=" + codigoCita[i];
			}
			nuevoListarCitas = nuevoListarCitas + " )";
		}
		

		try
		{
			Connection con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			//Ahora le doy los criterios de ordenamiento
			nuevoListarCitas = nuevoListarCitas + " order by a.fecha || '' ASC, a.hora_inicio || '' ASC";
			logger.info("--->"+nuevoListarCitas);
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(nuevoListarCitas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator resultado =new ResultSetDecorator(pst.executeQuery());
			Collection aRetornar=UtilidadBD.resultSet2Collection(resultado);
			if (con!=null&&!con.isClosed())
			{
				UtilidadBD.closeConnection(con);
			}
			return  aRetornar;
		}
		catch(SQLException e)
		{
			logger.error("Error listando las citas: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo que consulta las citas y sus servicios para la impresion
	 * @param codigoCita
	 * @return
	 */
	public static HashMap listarCitas(int[] codigoCita)
	{
		try
		{
			Connection con = UtilidadBD.abrirConexion();
			
			String consulta = listarCitaImpresionStr + " AND c.codigo IN ";
			
			String codigosStr = "";
			for(int i=0;i<codigoCita.length;i++)
			{
				if(!codigosStr.equals(""))
					codigosStr += ",";
				codigosStr += codigoCita[i];
			}
			
			consulta += "("+codigosStr+")";
			
			//Se consultan las citas
			Statement st = con.createStatement(ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			HashMap mapaCitas = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), true, true);
			
			//Se consultan los servicios de las citas
			for(int i=0;i<Integer.parseInt(mapaCitas.get("numRegistros").toString());i++)
			{
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(listarServiciosCitaImpresionStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setObject(1,mapaCitas.get("codigocita_"+i));
				
				mapaCitas.put("mapaServicios_"+i,UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true));
			}
			
			UtilidadBD.closeConnection(con);
			return mapaCitas;
		}
		catch(SQLException e)
		{
			logger.error("Error al listar las citas para el resumen de la impresion: "+e);
			return null;
		}
	}
	
	/**
	 * Mï¿½todo para verificar si la cita fue cancelada anteriormente
	 * @param con
	 * @param codigoAgenda
	 * @return
	 */
	public static boolean fueCanceladaIns(Connection con, int codigoAgenda)
	{
	    try
        {
            PreparedStatementDecorator cancelada= new PreparedStatementDecorator(con.prepareStatement(fueCanceladaInsStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            cancelada.setInt(1, codigoAgenda);
            return new ResultSetDecorator(cancelada.executeQuery()).next();
        }
        catch (SQLException e)
        {
            logger.error("Error verificando la cancelaciï¿½n de la cita "+e);
            return false;
        }
	}
	
	/**
	 * Mï¿½todo para actualizar las observaciones de la cita
	 * @param con
	 * @param observacion
	 * @return numero de actualizaciones en BD
	 */
	public static int actualizarObservacion(Connection con, int codigoCita, String observacion, int codigoServicio,String loginUsuario)
	{
		try
		{
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(actualizarObservacionCita,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			stm.setString(1, observacion);
			stm.setString(2, loginUsuario);
			stm.setString(3, UtilidadFecha.getHoraActual());
			stm.setInt(4, codigoCita);
			stm.setInt(5, codigoServicio);
			return stm.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("error actualizando la observacion de la cita : "+e);
			return -1;
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoAgenda
	 * @param centroAtencion
	 * @param institucion
	 * @param unidadConsulta
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarCentrosCostoXUnidadDeConsulta(Connection con, int codigoAgenda, int centroAtencion, int institucion, int unidadConsulta) throws SQLException
	{
		try
		{
			StringBuffer consultaStr=new StringBuffer();
			
			consultaStr.append(" SELECT cc.codigo as codigocentrocosto, " +
								" cc.nombre as descripcioncentrocosto " +
								" FROM centros_costo cc  " +
								" INNER JOIN cen_costo_x_un_consulta ccx ON(cc.codigo=ccx.centro_costo) " +
								" INNER JOIN agenda a ON(ccx.unidad_consulta=a.unidad_consulta) " +
								" WHERE a.codigo = ? " +
								" AND cc.institucion = ? " +
								" AND a.unidad_consulta = ? " +
								" AND cc.tipo_entidad_ejecuta = '"+ConstantesIntegridadDominio.acronimoInterna+"' ");
			
			if(centroAtencion!=ConstantesBD.codigoNuncaValido)
			{
				consultaStr.append(" AND cc.centro_atencion = ?");
			}
			
			logger.info("UNIDAD DE CONSULTA=> "+unidadConsulta);
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1 , codigoAgenda);
			ps.setInt(2 , institucion);
			ps.setInt(3 , unidadConsulta);
			
			if(centroAtencion!=ConstantesBD.codigoNuncaValido)
				ps.setInt(4 , centroAtencion);
			
			logger.info("consulta centors de costa X unidad de consulta >>>>>>>>>>>>>> "+consultaStr.toString());
			logger.info("codigo agenda >>> "+codigoAgenda);
			logger.info("institucion >>> "+institucion);
			logger.info("codigo unidad consulta >>> "+unidadConsulta);
			logger.info("centro de atencion >>>>> "+centroAtencion);
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en consultarCentrosCostoXUnidadDeConsulta : [SqlBaseCitaDao] "+e.toString() );
		}
		return null;
	}
	
	/**
	 * M?todo implementado para consultar los campos adicionales de la reserva de cita
	 * para la posterior creaci?n de la cuenta
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap consultaCamposAdicionalesReserva(Connection con,String codigoCita)
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		HashMap mapa=new HashMap();
		try
		{
			pst =  con.prepareStatement(consultaCamposAdicionalesReservaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setObject(1,codigoCita);
			rs=pst.executeQuery();
			int cont=0;
			mapa.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					int index=alias.indexOf("_");
					while(index>0)
					{
						index=alias.indexOf("_");
						try{
							String caracter=alias.charAt(index+1)+"";
							{
								alias=alias.replaceFirst("_"+caracter, caracter.toUpperCase());
							}
						}
						catch(IndexOutOfBoundsException e)
						{
							break;
						}
					}
					mapa.put(alias, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
			if(Integer.parseInt(mapa.get("numRegistros").toString())>0)
			{
				IValidacionTipoCobroPacienteServicio servicioValidacion=FacturacionServicioFabrica.crearValidacionTipoCobroPacienteServicio();
				DtoValidacionTipoCobroPaciente validacion=servicioValidacion.validarTipoCobroPacienteServicioConvenioContrato(Integer.parseInt(mapa.get("codigoContrato")+""));
				mapa.put("validacionTipoCobroPaciente", validacion);
			}
			else
			{
				mapa.put("llaveError", "error.cita.reservaSinDatosCuenta");
			}
			
			
		}
		catch(Exception e){
			logger.error("############## ERROR consultaCamposAdicionalesReserva", e);
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
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return mapa;
	}
	
	
	/**
	 * Metodo que actualiza en la cita la informaciï¿½n de la cuenta para el caso
	 * de la reserva
	 * @param con
	 * @param codigoCita
	 * @param convenio
	 * @param contrato
	 * @param estratoSocial
	 * @param tipoAfiliado
	 * @param naturalezaPaciente 
	 * @param otrosTelefonos 
	 * @param celular 
	 * @param centroCosto 
	 * @param codigoServicio
	 */
	public static int actualizarInfoCuentaCita(Connection con, int codigoCita, int convenio, int contrato, int estratoSocial, String tipoAfiliado,int naturalezaPaciente, String telefono, String origenTelefono, int codigoPaciente, String celular, String otrosTelefonos)
	{
		StringBuffer consultaStr = new StringBuffer();
		boolean primero=false;
		
		consultaStr.append(" UPDATE cita " +
					  	   "SET ");
		
		if(convenio!=ConstantesBD.codigoNuncaValido)
		{
				consultaStr.append(" convenio ="+convenio);
				primero=true;
		}
		
		if(contrato!=ConstantesBD.codigoNuncaValido)
			{
			if(primero)
				{
				consultaStr.append(", contrato ="+contrato);	
				}
			else
				{
				consultaStr.append(" contrato ="+contrato);
				primero=true;
				}
			}
		
		if(estratoSocial!=ConstantesBD.codigoNuncaValido)
			{
			if(primero)
				{
				consultaStr.append(", estrato_social ="+estratoSocial);	
				}
			else
				{
				consultaStr.append(" estrato_social ="+estratoSocial);
				primero=true;
				}
			}
		
		if(UtilidadCadena.noEsVacio(tipoAfiliado) && !tipoAfiliado.equals(ConstantesBD.codigoNuncaValido+""))
			{
			if(primero)
				{
				consultaStr.append(", tipo_afiliado ='"+tipoAfiliado+"'");	
				}
			else
				{
				consultaStr.append(" tipo_afiliado ='"+tipoAfiliado+"'");
				primero=true;
				}
			}
		
		if(!UtilidadTexto.isEmpty(telefono))
		{
			if(primero)
			    {
				 consultaStr.append(", telefono ='"+telefono+"'");
			   }	
			else
			   {
				consultaStr.append(" telefono ='"+telefono+"'");
				primero=true;
			    }
		}
		
		if(naturalezaPaciente!=ConstantesBD.codigoNuncaValido)
		{
			if(primero)
			{
				consultaStr.append(", naturaleza_paciente ="+naturalezaPaciente);
			}	
			else
			{
				consultaStr.append(" naturaleza_paciente ="+naturalezaPaciente);
				primero=true;
			}
		}
		
		
		
		consultaStr.append(" WHERE codigo = "+codigoCita);
		
		logger.info("ACTUALIZACION INFORMACION CUENTA: "+consultaStr);
		try
		{
			int resp = ConstantesBD.codigoNuncaValido;
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            //-Ejecutar la sentencia de actualizacion
			resp = ps.executeUpdate();
			
			if(resp>0&&!telefono.equals("") )
			{
				consultaStr = new StringBuffer();
				
				consultaStr.append("UPDATE personas SET telefono_fijo=?,telefono_celular=?,telefono=? WHERE codigo =? ");
				 
				ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr.toString(), ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				if(Utilidades.convertirADouble(telefono)>0)
					ps.setBigDecimal(1,new BigDecimal(telefono));
				else
					ps.setObject(1, null);
				if(Utilidades.convertirADouble(celular)>0)
					ps.setBigDecimal(2,new BigDecimal(celular));
				else
					ps.setObject(2, null);
				if(!UtilidadTexto.isEmpty(otrosTelefonos))
					ps.setString(3,otrosTelefonos);
				else
					ps.setObject(3, null);
			   	ps.setInt(4, codigoPaciente);
				resp = ps.executeUpdate();
				
			}
			
			return resp;
		} 
		catch (SQLException e)
		{
			logger.error("Error Actualizando la informacion de la cuenta en la cita: "+e.toString());
			return -1;
		}
	}
	
	/**
	* Lista las citas para un paciente, reutilizando una conexiï¿½n existente 
	* en una BD Postgresql
	* 
	* @param con					Conexiï¿½n abierta con una fuente de datos
	* @param ai_modo				Indicador de modo de bï¿½squeda de las citas
	* @param ai_paciente			Cï¿½digo del paciente
	* @param ai_unidadConsulta		Cï¿½digo de la unidad de consulta a la cual debe estar asociada la
	*								cita
	* @param ai_consultorio			Cï¿½digo del consultoria en la cual se llevara a cabo la cita
	* @param ai_medico				Cï¿½digo del mï¿½dico
	* @param as_fechaInicio			Fecha inicial del rango de fechas de las citas
	* @param as_fechaFin			Fecha final del rango de fechas de las citas
	* @param as_horaInicio			Hora inicial del rango de horas de las citas
	* @param as_horaFin				Hora final del rango de fechas de las citas
	* @param ai_estadoCita			Estado de la cita
	* @param as_estadoLiquidacion	Estado de liquidaciï¿½n de la cita
	* @param ai_cuenta				Codigo de la cuenta del paciente
	*/
	public static Collection listar(
		Connection con,
		int			ai_modo,
		int			ai_paciente,
		int			ai_unidadConsulta,
		int			ai_consultorio,
		int			ai_medico,
		String		as_fechaSolicitada,
		String		as_fechaInicio,
		String		as_fechaFin,
		String		as_horaInicio,
		String		as_horaFin,
		int			ai_estadoCita,
		String		as_estadoLiquidacion,
		int			ai_cuenta,
		String centroAtencion,
		String centrosAtencion,
		String unidadesAgenda
	)
	{
		boolean			cerrarConexion;
		Collection		coleccion=null;
		StringBuffer	consultaStr;

		cerrarConexion = false;
		
		String fechaActual=UtilidadFecha.getFechaActual();
		String fechaActualBD=UtilidadFecha.conversionFormatoFechaABD(fechaActual);
		try
		{
			/* Verificar el estado de la conexiï¿½n y abrir una nueva si es necesario */
		    cerrarConexion = (con == null || con.isClosed() );
			if( cerrarConexion )
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
			}

			/* Preparar la consulta sobre las citas del paciente */
			consultaStr = new StringBuffer();

			/* Validar el paciente */
			if(ai_paciente > 0)
			{
				consultaStr.append(" AND ci.codigo_paciente=" + ai_paciente);
			}

			/* validar la fecha solicitada por el paciente */
			if(!as_fechaSolicitada.equals("") )
			{
				consultaStr.append(" AND (ci.fecha_solicitada IS NULL OR to_char(ci.fecha_solicitada,'yyyy-mm-dd')='" +UtilidadFecha.conversionFormatoFechaABD(as_fechaSolicitada) + "')");
			} 
			
			/* Validar la fecha de inicio de la cita */
			if(!as_fechaInicio.equals("") )
			{
				consultaStr.append(" AND(a.fecha IS NULL OR to_char(a.fecha,'yyyy-mm-dd')>='" +UtilidadFecha.conversionFormatoFechaABD(as_fechaInicio) + "')");
			}

			/* Validar la fecha de finalizaciï¿½n de la cita */
			if(!as_fechaFin.equals("") )
			{
				consultaStr.append(" AND(a.fecha IS NULL OR to_char(a.fecha,'yyyy-mm-dd')<='" +UtilidadFecha.conversionFormatoFechaABD(as_fechaFin) + "')");
			}

			/* Validar la hora de inicio */
			if(!as_horaInicio.equals("") )
			{
				consultaStr.append(" AND(a.hora_inicio IS NULL OR a.hora_inicio>='" + as_horaInicio + "')");
			}
			
			/* Validar la hora de finalizaciï¿½n */
			if(!as_horaFin.equals("") )
			{
				consultaStr.append(" AND(a.hora_fin IS NULL OR a.hora_fin<='" + as_horaFin + "')");
			}

			/* Validar el consultorio del horario de atenciï¿½n */
			if(ai_consultorio > -1)
			{
				consultaStr.append(" AND(a.consultorio IS NULL OR a.consultorio=" + ai_consultorio + ")");
			}
			
			if(Integer.parseInt(centroAtencion)>0)
				consultaStr.append(" AND a.centro_atencion="+centroAtencion);
			else
			{
				//222if(centrosAtencion)
				consultaStr.append(" AND a.centro_atencion IN ("+centrosAtencion+") ");
			}	
			/* Validar el cï¿½digo del mï¿½dico del horario de atenciï¿½n */
			if(ai_medico == -1 )
			{
				/*
				 * este filtro no debe existir
				 */
				//consultaStr.append(" AND (a.codigo_medico IS NULL OR a.codigo_medico=" + ai_medico + ")");
			}
			if(ai_medico > 0 )
			{
				consultaStr.append(" AND a.codigo_medico=" + ai_medico + "");
			}
			if(ai_medico ==-2)
			{
				consultaStr.append(" AND a.codigo_medico IS NULL"); 
			}

			/* Validar la unidad de consulta */
			if(ai_unidadConsulta > 0)
				consultaStr.append(" AND ci.unidad_consulta=" + ai_unidadConsulta);
			else
				consultaStr.append(" AND ci.unidad_consulta IN (" + unidadesAgenda +") ");

			/* Validar el estado de la cita */
			if(ai_estadoCita > -1)
			{
				consultaStr.append(" AND ci.estado_cita=" + ai_estadoCita);
			}

			/* Incluir solo las citas con estado vï¿½lido para reprogramaciï¿½n */
			if(ai_modo == Cita.BUSCAR_PARA_CANCELACION)
			{
				consultaStr.append(" AND ci.estado_cita IN(");
				consultaStr.append(ConstantesBD.codigoEstadoCitaAsignada + ",");
				consultaStr.append(ConstantesBD.codigoEstadoCitaReservada + ",");
				consultaStr.append(ConstantesBD.codigoEstadoCitaReprogramada + ")");
				consultaStr.append("AND(a.fecha IS NULL OR((to_char(a.fecha,'yyyy-mm-dd')='" + fechaActualBD + "' AND a.hora_inicio>='"+UtilidadFecha.getHoraActual()+"')OR to_char(a.fecha,'yyyy-mm-dd')>'" + fechaActualBD + "'))");
				consultaStr.append(" AND puedoCancelarCita(ci.codigo)='"+ConstantesBD.acronimoSi+"' ");
			}
			else if(ai_modo == Cita.BUSCAR_PARA_LIQUIDACION)
			{
				consultaStr.append(" AND ci.estado_cita IN(");
				consultaStr.append(ConstantesBD.codigoEstadoCitaAsignada + ",");
				consultaStr.append(ConstantesBD.codigoEstadoCitaReservada + ",");
				consultaStr.append(ConstantesBD.codigoEstadoCitaReprogramada + ")");
				consultaStr.append("AND(a.fecha IS NULL OR to_char(a.fecha,'yyyy-mm-dd')>='"+fechaActualBD+"')");
			}
			else if(ai_modo == Cita.BUSCAR_PARA_REPROGRAMACION)
			{
				consultaStr.append(" AND ci.estado_cita IN(");
				consultaStr.append(ConstantesBD.codigoEstadoCitaAsignada + ",");
				consultaStr.append(ConstantesBD.codigoEstadoCitaReservada + ",");
				consultaStr.append(ConstantesBD.codigoEstadoCitaReprogramada + ",");
				consultaStr.append(ConstantesBD.codigoEstadoCitaAReprogramar + ")");
			}

			/* Validar el estado de liquidaciï¿½n de la cita */
			if(!as_estadoLiquidacion.equals("") )
			{
				consultaStr.append(" AND ci.estado_liquidacion='" + as_estadoLiquidacion + "'");
			}

			/* Validar la cuenta del paciente */
			if(ai_cuenta > 0)
			{
				consultaStr.append(" AND (so.cuenta IS NULL OR so.cuenta=" + ai_cuenta + ")");
			}
			
			//Para REPROGRAMNACIONES deben ser citas mayores a la fecha/hora actual
			if(ai_modo==Cita.BUSCAR_PARA_REPROGRAMACION)
			{
//xplanner 2 oid 16373		lsb_consulta.append(" AND (a.fecha >= CURRENT_DATE AND a.hora_inicio > "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+") ");
				//consultaStr.append(" AND ((a.fecha >= CURRENT_DATE AND a.hora_inicio > "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+") OR ci.estado_cita="+ConstantesBD.codigoEstadoCitaAReprogramar+") ");
				consultaStr.append(" AND (((a.fecha || '-' || a.hora_inicio) >= ('"+UtilidadFecha.conversionFormatoFechaABD(fechaActual)+"' || '-' || '"+UtilidadFecha.getHoraActual()+"')) OR ci.estado_cita="+ConstantesBD.codigoEstadoCitaAReprogramar+") ");
			}
			
			
			/* Eliminar el primer " AND " de la cunsulta si existe*/
			if(consultaStr.length() > 0)
			{
				consultaStr.replace(1, 4, "WHERE");
			}
			else{
				return coleccion;
			}

			consultaStr.append(" ORDER BY fecha,horaInicio");
			/* Obtener el conjunto soluciï¿½n de la bï¿½squeda */
			
			logger.info("CONSULTA DE LAS CITAS=> "+is_listar + consultaStr.toString());
			PreparedStatementDecorator stm= new PreparedStatementDecorator(con.prepareStatement(is_listar + consultaStr.toString(),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			coleccion=UtilidadBD.resultSet2Collection(new ResultSetDecorator(stm.executeQuery()));

			if(coleccion.size() < 1)
			{
				coleccion = null;
			}

			if(cerrarConexion)
			{
				UtilidadBD.closeConnection(con);
			}
		}
		catch(Exception e)
		{
			logger.error("Error al listar las citas=> "+e);
			if(cerrarConexion)
			{
				UtilidadBD.closeConnection(con);
			}
			coleccion = null;
		}

		return coleccion;
	}
	
	/**
	 * Mï¿½todo que consulta los servicios de una cita
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarServiciosCita(Connection con,HashMap campos)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarServiciosCitaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setObject(1,campos.get("institucion"));
			pst.setObject(2,campos.get("codigoCita"));
			
			logger.info("CONSULTAA PPPPP>>>>>>>>>>>>>>"+consultarServiciosCitaStr);
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
	        pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarServiciosCita: "+e);
			return null;
		}
	}
	
	
	/**
	 * Mï¿½todo que anula el servicio de una cita
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int anularServicioCita(Connection con,HashMap campos)
	{
		PreparedStatement pst=null;
		PreparedStatement pst2=null;
		PreparedStatement pst3=null;
		PreparedStatement pst4=null;
		PreparedStatement pst5=null;
		try
		{
			int resp0 = 0, resp1 = 0;
			
			//Anulacion del servicio de la cita
			pst =  con.prepareStatement(anularServicioCitaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			pst.setObject(1,campos.get("usuario"));
			pst.setObject(2,campos.get("codigoCita"));
			pst.setObject(3,campos.get("codigoServicio"));
			
			resp0 = pst.executeUpdate();
			
			if(!campos.get("numeroSolicitud").toString().equals(""))
			{
				pst2 = con.prepareStatement(anularSolicitudCitaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
				pst2.setObject(1,campos.get("numeroSolicitud"));
				
				resp1 = pst2.executeUpdate();
				
				if(resp1>0)
				{
					pst3 = con.prepareStatement(anularCargoCitaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
					pst3.setObject(1,campos.get("usuario"));
					pst3.setObject(2,campos.get("numeroSolicitud"));
					
					pst3.executeUpdate();
				}
				
				//*********REVERSION DE LA JUSTIFICACION DE SERVICIOS*************************
				if(resp1>0)
					if(!UtilidadJustificacionPendienteArtServ.eliminarJusNoposPendiente(con, Integer.parseInt(campos.get("numeroSolicitud").toString()), Integer.parseInt(campos.get("codigoServicio").toString()), false))
						resp1 = 0;
				//******************************************************************************
			}
			else
				resp1 = 1;
			
			
			if(resp0>0&&resp1>0)
			{
				//*************ACTUALIZACION DEL ESTADO DE LIQUIDACION DE LA CITA********************************
				int resp = 1;
				
				
				if(UtilidadTexto.getBoolean(campos.get("validarCitaCumplida").toString()))
				{
					//Se verifica si la cita ya tiene todos sus servicios con solicitud
					HashMap serviciosCita = consultarServiciosCita(con, campos);
					
					int numServiciosActivos = 0; //nï¿½mero de servicios activos
					int numServiciosSinSolicitud = 0; //nï¿½mero de servicios activo sin solicitud
					int numServiciosAtendidos = 0; //nï¿½mero de servicios activos con solicitud, pero ya fueron atendidos
					
					for(int i=0;i<Integer.parseInt(serviciosCita.get("numRegistros").toString());i++)
					{
						if(serviciosCita.get("estadoServicio_"+i).toString().equals(ConstantesIntegridadDominio.acronimoEstadoActivo))
						{
							numServiciosActivos ++;
							if(serviciosCita.get("numeroSolicitud_"+i).toString().equals(""))
								numServiciosSinSolicitud ++;
							else
							{
								//se consulta el estado de historia clinica de la solicitud
								int estadoMedico = Integer.parseInt(Utilidades.obtenerEstadoHistoriaClinicaSolicitud(con, serviciosCita.get("numeroSolicitud_"+i).toString()));
								if(estadoMedico == ConstantesBD.codigoEstadoHCInterpretada )
									numServiciosAtendidos++;
							}
						}
					}
					
					logger.info("valores para la comparacion >> numServiciosAtendidos: "+numServiciosAtendidos+" >> numServiciosActivos : "+numServiciosActivos+" >> numServiciosSinSolicitud: "+numServiciosSinSolicitud);
					
					//Se verifica si la cita todavï¿½a tiene servicios activos
					//y no hay servicios sin solicitud, entonces se pasa el estado de liquidacion de la cita a LIQUIDADA
					if(numServiciosActivos>0&&numServiciosSinSolicitud==0)
					{
						pst4 = con.prepareStatement(is_asignarSolicitud_2, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
						pst4.setObject(1,campos.get("codigoCita"));
						resp = pst4.executeUpdate();
						
						
					}
										
					//1) Si hay servicios activos y todos tienen solicitudes ya atendidas					
					if(resp>0&&numServiciosAtendidos==numServiciosActivos&&numServiciosSinSolicitud==0&&numServiciosActivos>0)
					{
						pst5= con.prepareStatement(actualizarEstadoCitaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
						pst5.setInt(1,ConstantesBD.codigoEstadoCitaAtendida);
						pst5.setNull(2,Types.VARCHAR);
						pst5.setObject(3,campos.get("codigoCita"));
						
						resp = pst5.executeUpdate();
					}
				}
				return resp;
				//*********************************************************************************************************
			}
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerCondicionesTomaXServicio", e);
		}
		finally{
			try{
				if(pst5 != null){
					pst5.close();
				}
				if(pst4 != null){
					pst4.close();
				}
				if(pst3 != null){
					pst3.close();
				}
				if(pst2 != null){
					pst2.close();
				}
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
			return 0;
		}
	
	/**
	 * Mï¿½todo que consulta los estados las solicitudes de una cita
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultarEstadosSolicitudesCita(Connection con,String codigoCita)
	{
		HashMap mapa=new HashMap();
		PreparedStatement ps=null;
		ResultSet rs=null;
		try
		{
			ps =  con.prepareStatement(consultarEstadosSolicitudesCitaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			ps.setObject(1,codigoCita);
			
			rs=ps.executeQuery();
			
			int cont=0;
			mapa.put("numRegistros","0");
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					int index=alias.indexOf("_");
					while(index>0)
					{
						index=alias.indexOf("_");
						try{
							String caracter=alias.charAt(index+1)+"";
							{
								alias=alias.replaceFirst("_"+caracter, caracter.toUpperCase());
							}
						}
						catch(IndexOutOfBoundsException e)
						{
							break;
						}
					}
					mapa.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			mapa.put("numRegistros", cont+"");
			
		}
		catch(Exception e){
			logger.error("############## ERROR consultarEstadosSolicitudesCita", e);
		}
		finally{
			try{
				if(rs != null){
					rs.close();
				}
				if(ps != null){
					ps.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
		}
		}
		return mapa;
	}
	
	/**
	 * Mï¿½todo que realiza la inserciï¿½n del servicio a la cita
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static int insertarServicioCita(Connection con ,HashMap campos)
	{
		PreparedStatement pst=null;
		int resultado=0;
		try
		{
			String consulta = insertarServicioCitaStr + " ("+campos.get("secuencia")+",?,?,'"+ConstantesIntegridadDominio.acronimoEstadoActivo+"',?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?,?,?,?,?)";
			pst  =  con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			pst.setObject(1, campos.get("codigoCita"));
			pst.setObject(2,campos.get("codigoServicio"));
			pst.setObject(3,campos.get("codigoCentroCosto"));
			pst.setNull(4,Types.INTEGER);
			if(campos.get("codigoEspecialidad")!=null&&!campos.get("codigoEspecialidad").toString().equals(""))
				pst.setObject(5,campos.get("codigoEspecialidad"));
			else
				pst.setNull(5,Types.INTEGER);
			if(campos.get("observaciones")!=null&&!campos.get("observaciones").toString().equals(""))
				pst.setObject(6,campos.get("observaciones"));
			else
				pst.setNull(6,Types.VARCHAR);
			pst.setObject(7,campos.get("usuario"));
			pst.setObject(8,UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaCita").toString()));
			pst.setObject(9,UtilidadFecha.convertirHoraACincoCaracteres(campos.get("horaInicioCita").toString()));
			pst.setObject(10,UtilidadFecha.convertirHoraACincoCaracteres(campos.get("horaFinCita").toString()));
			pst.setObject(11,campos.get("codigoEstadoCita"));
			pst.setObject(12,campos.get("codigoAgenda"));
			
			if(UtilidadTexto.isEmpty(campos.get("codigopo")+""))
			{
				pst.setNull(13, Types.INTEGER);
			}
			else
			{
				pst.setInt(13, Integer.parseInt(campos.get("codigopo").toString()));
			}
			
			resultado= pst.executeUpdate();
		}
		catch(Exception e){
			logger.error("############## ERROR insertarServicioCita", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
		}
		}
		return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param codigoEstadoCita
	 * @return
	 */
	public static String obtenerDescripcionEstadoCita(Connection con, int codigoEstadoCita) 
	{
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta = "select nombre from estados_cita where codigo=?";
			pst  = con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet);
			pst.setInt(1, codigoEstadoCita);
			rs=pst.executeQuery();
			if(rs.next())
			{
				return rs.getString(1);
			}
		}
		catch(Exception e){
			logger.error("############## ERROR obtenerCondicionesTomaXServicio", e);
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
			catch (SQLException se) {
				logger.error("###########  Error close ResultSet - PreparedStatement", se);
			}
		}
		return "";
	}

	/**
	 * 
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public static String obtenerEstadoCita(Connection con, int codigoCita) 
	{
		try
		{
			String consulta = "SELECT estado_cita from cita where codigo=?";
			PreparedStatementDecorator pst  =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoCita);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getString(1);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerEstadoCita: "+e);
		}
		return ConstantesBD.codigoNuncaValido+"";
	}
	
	//**********************************************************************************************************************************	
	
	/**
	 * Inserta la informacion del log de Reprogramacion de citas
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public static boolean guardarLogReprogramacionCita(Connection con,HashMap parametros)
	{
		try
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strLogReprogramacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("cita_reprogramada").toString()));
			ps.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametros.get("fecha_reprogramacion").toString())));
			ps.setString(3,parametros.get("hora_reprogramacion").toString());
			ps.setString(4,parametros.get("usuario_reprogramo").toString());
			ps.setInt(5,Utilidades.convertirAEntero(parametros.get("centro_atencion_anter").toString()));
			ps.setInt(6,Utilidades.convertirAEntero(parametros.get("centro_atencion_nuevo").toString()));
			ps.setDate(7,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametros.get("fecha_cita_anter").toString())));
			ps.setDate(8,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametros.get("fecha_cita_nuevo").toString())));
			ps.setString(9,parametros.get("hora_cita_anter").toString());
			ps.setString(10,parametros.get("hora_cita_nuevo").toString());
			
			if(!(parametros.get("profesional_anter")+"").equals("") 
					&& Utilidades.convertirAEntero(parametros.get("profesional_anter").toString()) > 0)
			{				
				ps.setInt(11,Utilidades.convertirAEntero(parametros.get("profesional_anter").toString()));
			}
			else			
				ps.setNull(11,Types.INTEGER);
			
			if(!(parametros.get("profesional_nuevo")+"").equals("") 
					&& Utilidades.convertirAEntero(parametros.get("profesional_nuevo").toString())>=0)
				ps.setInt(12,Utilidades.convertirAEntero(parametros.get("profesional_nuevo").toString()));
			else
				ps.setNull(12,Types.INTEGER);
			
			if(!(parametros.get("consultorio_anter")+"").equals("") 
					&& Utilidades.convertirAEntero(parametros.get("consultorio_anter").toString())>=0)
				ps.setInt(13,Utilidades.convertirAEntero(parametros.get("consultorio_anter").toString()));
			else
				ps.setNull(13,Types.INTEGER);
			
			if(!(parametros.get("consultorio_nuevo")+"").equals("")
					&& Utilidades.convertirAEntero(parametros.get("consultorio_nuevo").toString())>=0)
				ps.setInt(14,Utilidades.convertirAEntero(parametros.get("consultorio_nuevo").toString()));
			else
				ps.setNull(14,Types.INTEGER);	
			
			ps.setInt(15,Utilidades.convertirAEntero(parametros.get("unidad_agenda_anter").toString()));
			//si la unidad de agenda nueva, no se ha asigando, quiere decer que esta en "a reprogramar" y por el momento la unidad de agenda nueva es la misma que la anterior.
			if(Utilidades.convertirAEntero(parametros.get("unidad_agenda_nuevo").toString())>0)
				ps.setInt(16,Utilidades.convertirAEntero(parametros.get("unidad_agenda_nuevo").toString()));
			else
				ps.setInt(16,Utilidades.convertirAEntero(parametros.get("unidad_agenda_anter").toString()));
			if(ps.executeUpdate() > 0)
				return true;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	//**********************************************************************************************************************************
	/**
	 * Mï¿½todo implementado para actualizar la prioridad de una cita
	 */
	public static boolean actualizarPrioridadCita(Connection con,HashMap campos)
	{
		boolean exito = false;
		try
		{
			//**************SE TOMAN LOS PARï¿½METROS*********************************
			int codigoCita = Integer.parseInt(campos.get("codigoCita").toString());
			String prioritaria = campos.get("prioritaria").toString().equals("")?ConstantesBD.acronimoNo:campos.get("prioritaria").toString();
			//**********************************************************************
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strActualizarPrioridadCita, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,prioritaria);
			pst.setInt(2,codigoCita);
			
			
			if(pst.executeUpdate()>0)
				exito = true;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarPrioridadCita: "+e);
		}
		return exito;
	}
	//************************************************************************************************************************************
	
	/**
	 * Mï¿½todo para actualizar las observaciones de la cita
	 * @param Connection con
	 * @param HashMap parametros
	 * @return
	 */
	public static HashMap getReportePdfBaseCita(Connection con,HashMap parametros)
	{
		String citas = "select "+
					   "c.codigo AS codigo, " +
					   "administracion.getnombremedico(a.codigo_medico) AS nombremedico, "+
					   "getnomcentroatencion(a.centro_atencion) AS centroatencion, "+
					   "getnombreunidadconsulta(a.unidad_consulta) AS nombreunidadconsulta, "+
					   "to_char(a.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha,"+
					   "a.hora_inicio||'' AS hora ,"+
					   "getnombreconsultorio(a.consultorio) AS nombreconsultorio "+ 
					   "from consultaexterna.cita c "+
					   "inner join consultaexterna.agenda a ON (a.codigo = c.codigo_agenda) "+
					   "where c.codigo IN ("+parametros.get("codigoCitas").toString()+") "; 
		
		String encabezado = 
						"select  "+
						"getnombrepersona(p.codigo) AS nombrepaciente, "+ 
						"p.tipo_identificacion || ' No. ' || p.numero_identificacion AS numeroidentificacion, "+
						"to_char(p.fecha_nacimiento,'"+ConstantesBD.formatoFechaAp+"') AS fechanacimiento, "+
						"'"+parametros.get("edad").toString()+"' AS edad, "+
						"getdescripcionsexo(p.sexo) AS sexo, "+
						"p.direccion AS direccion,  "+
						"coalesce(p.telefono_fijo||'',' ')||' '||coalesce(p.telefono_celular||'',' ')||' '||coalesce(p.telefono||'',' ') AS telefono,  "+
						"to_char(c.fecha_gen,'"+ConstantesBD.formatoFechaAp+"') || ' ' || c.hora_gen  AS fechageneracion, "+
						"getnombreusuario2(c.usuario) AS usuario  "+
						"from administracion.personas p   "+
						"inner join consultaexterna.cita c ON (c.codigo_paciente = "+parametros.get("codigoPaciente").toString()+") "+ 
						"where p.codigo = "+parametros.get("codigoPaciente").toString()+" " +
						"AND c.codigo IN ("+parametros.get("codigoCitas").toString()+") "+ 
						""+ValoresPorDefecto.getValorLimit1()+" 1 ";					
		
		HashMap respuesta = new HashMap();
		
		respuesta.put("sql0",citas);
		respuesta.put("sql1",encabezado);
		
		return respuesta;
	}
	
	//************************************************************************************************************************************
	
	/**
	 * Metodo consulta si existe Autorizacion del Paciente
	 * @param con
	 * @param codigoIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean consultarAutorizacionPaciente(Connection con, int codigoIngreso, int codigoConvenio){
		
		boolean resultado = false;
		
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		
		try{
			ps= new PreparedStatementDecorator(con.prepareStatement(strConsultaNumeroAutorizacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoIngreso);
			ps.setInt(2, codigoConvenio);
			
			rs =new ResultSetDecorator(ps.executeQuery());
			
			logger.info("consultarAutorizacionPaciente: "+strConsultaNumeroAutorizacion+" --"+codigoIngreso+" "+codigoConvenio);
			
			if(rs.next()){
				resultado = rs.getString("numero_verificacion")==null || rs.getString("numero_verificacion").equals("") ? false : true;
			}
				
		}catch(SQLException e){
			logger.info("ERROR -- SQLException consultarAutorizacionPaciente: ");
			
		}catch(Exception ex){
			logger.info("ERROR -- Exception consultarAutorizacionPaciente: ");
			
		}finally{
			try{
				if(ps != null){
					ps.close();
				}
				if(rs != null){
					rs.close();
				}
			}
			catch(SQLException e)
			{
				logger.error("Error Cerrando PreparedStatement - ResultSet : "+e);
			}
		}
		
		return resultado;
	}

}