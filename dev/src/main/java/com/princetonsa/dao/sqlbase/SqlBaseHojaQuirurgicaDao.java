/*
 * Jhony Alexander Duque A.
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.salas.UtilidadesSalas;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseUtilidadesManejoPacienteDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.salasCirugia.DtoProfesionalesCirugia;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;
import com.servinte.axioma.dto.salascirugia.CargoSolicitudDto;
import com.servinte.axioma.dto.salascirugia.DescripcionOperatoriaDto;
import com.servinte.axioma.dto.salascirugia.DestinoPacienteDto;
import com.servinte.axioma.dto.salascirugia.EspecialidadDto;
import com.servinte.axioma.dto.salascirugia.EstadoSolicitudDto;
import com.servinte.axioma.dto.salascirugia.InformacionActoQxDto;
import com.servinte.axioma.dto.salascirugia.InformeQxDto;
import com.servinte.axioma.dto.salascirugia.InformeQxEspecialidadDto;
import com.servinte.axioma.dto.salascirugia.IngresoSalidaPacienteDto;
import com.servinte.axioma.dto.salascirugia.NotaAclaratoriaDto;
import com.servinte.axioma.dto.salascirugia.PatologiaDto;
import com.servinte.axioma.dto.salascirugia.PeticionQxDto;
import com.servinte.axioma.dto.salascirugia.ProfesionalHQxDto;
import com.servinte.axioma.dto.salascirugia.ProgramacionPeticionQxDto;
import com.servinte.axioma.dto.salascirugia.SalaCirugiaDto;
import com.servinte.axioma.dto.salascirugia.ServicioHQxDto;
import com.servinte.axioma.dto.salascirugia.SolicitudCirugiaDto;
import com.servinte.axioma.dto.salascirugia.TipoAnestesiaDto;
import com.servinte.axioma.dto.salascirugia.TipoHeridaDto;
import com.servinte.axioma.dto.salascirugia.TipoProfesionalDto;
import com.servinte.axioma.dto.salascirugia.TipoSalaDto;
import com.servinte.axioma.dto.salascirugia.TipoViaDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;

/**
 * @author Jhony Alexander Duque A.
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad de Hoja Quirurgica
 *
 */
@SuppressWarnings("unchecked")
public class SqlBaseHojaQuirurgicaDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseHojaQuirurgicaDao.class);
	
	
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * ATRIBUTOS DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	/**
	 * String encargado de eliminar los diagnosticos postOperatorios
	 * del servicio en sol_cirugia_por_servicio
	 */
	private static final String eliminarDiagnosticosStr="DELETE FROM diag_post_opera_sol_cx WHERE cod_sol_cx_servicio=?";
	/**
	 * Sentencia para ingresar los diagnosticos de cada servicio
	 */
	private static final String ingresarDiagnosticoPostoperatorio="INSERT INTO diag_post_opera_sol_cx(codigo, cod_sol_cx_servicio, diagnostico, tipo_cie, principal, complicacion,fecha_modifica, hora_modifica, usuario_modifica) VALUES(?,?,?,?,?,?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";

	
	/**
	 * Sentencia para cargar los diagnósticos de los servicios
	 */
	private static final String cargarDiagnosticosServiciosStr="SELECT " +
			"ds.codigo AS codigo, " +
			"ds.cod_sol_cx_servicio AS servicio, " +
			"ds.diagnostico AS diagnostico, " +
			"ds.tipo_cie AS tipo_cie, " +
			"ds.principal AS principal, " +
			"ds.complicacion AS complicacion, " +
			"d.nombre AS nombre_diagnostico " +
			"FROM diagnosticos d " +
			"INNER JOIN diag_post_opera_sol_cx ds ON(d.acronimo=ds.diagnostico AND ds.tipo_cie=d.tipo_cie) " +
			"WHERE ds.cod_sol_cx_servicio=?";
	
	private static final String cargarDiagnosticosPreoperatoriosRelacionadosStr="SELECT dp.codigo AS codigo, dp.diagnostico AS diagnostico, dp.tipo_cie AS tipo_cie, d.nombre AS nombre_diagnostico, dp.principal AS principal FROM diagnosticos d INNER JOIN diag_preoperatorio_cx dp ON(d.acronimo=dp.diagnostico AND dp.tipo_cie=d.tipo_cie) WHERE dp.numero_solicitud=?"; 
	
	
	/**
	 * String de consulta de las peticiones de cirugia; con las siguientes validadciones:
	 * --Consulta el listado de peticiones que no tengan solicitud asociada, de ser asi
	 * 	 se debe validar que la solicitud corresponda a la cuenta cargada y que se encuentren
	 *   en estado diferente de ATENDIDO o CANCELADA
	 */
	private static final String strConsultaPeticiones =" SELECT" +
																" cuenta0, paciente1, codigo_peticion2, fecha_cirugia3, consecutivo_ordenes4," +
																"estado_medico5, numero_solicitud6, solicitante7, especialidad8,es_asociado10," +
																"codigo_solicitante11 " +
														"FROM (	SELECT  " +
																		"null as cuenta0, " +
																		"p.paciente as paciente1, " +
																		"p.codigo as codigo_peticion2, " +
																		"coalesce(to_char(p.fecha_cirugia,'DD/MM/YYYY'),'') as fecha_cirugia3, " +
																		"'' as consecutivo_ordenes4, " +
																		"'' as estado_medico5, " +
																		"'' as numero_solicitud6, " +
																		"administracion.getnombremedico(p.solicitante) as solicitante7, " +
																		"''  as especialidad8, " +
																		"'"+ConstantesBD.acronimoNo+"' AS es_asociado10," +
																		"p.solicitante AS codigo_solicitante11 " +
																" FROM peticion_qx  p " +
																" INNER JOIN peticiones_servicio ps on(p.codigo=ps.peticion_qx)" +
																" INNER JOIN servicios se on(ps.servicio=se.codigo)" +
																" WHERE p.codigo not in(select solcir.codigo_peticion from solicitudes_cirugia solcir where solcir.codigo_peticion is not null)" +
																" AND (se.tipo_servicio='"+ConstantesBD.codigoServicioQuirurgico+"' OR se.tipo_servicio='"+ConstantesBD.codigoServicioPartosCesarea+"' OR se.tipo_servicio='"+ConstantesBD.codigoServicioNoCruentos+"') " +
																" AND p.estado_peticion!="+ConstantesBD.codigoEstadoPeticionAtendida+
																" AND p.estado_peticion!="+ConstantesBD.codigoEstadoPeticionAnulada+
																" AND p.paciente=?" +
																" AND p.institucion=?"+
														"UNION " +
																"SELECT " +
																		"c.id as cuenta0, " +
																		"p.paciente as paciente1, " +
																		"p.codigo as codigo_peticion2, " +
																		"coalesce(to_char(p.fecha_cirugia,'DD/MM/YYYY'),'') as   fecha_cirugia3, " +
																		"s.consecutivo_ordenes_medicas||'' as consecutivo_ordenes4, " +
																		"getestadosolhis(s.estado_historia_clinica) as estado_medico5, " +
																		"sc.numero_solicitud||'' as numero_solicitud6, " +
																		"administracion.getnombremedico(s.codigo_medico) as solicitante7, " +
																		"CASE WHEN s.especialidad_solicitante IS NULL THEN '' ELSE getnombreespecialidad(s.especialidad_solicitante) END as especialidad8, " +
																		"'"+ConstantesBD.acronimoSi+"' AS es_asociado10, " +
																		"s.codigo_medico AS codigo_solicitante11 " +
																" FROM peticion_qx p " +
																" INNER JOIN solicitudes_cirugia sc ON(sc.codigo_peticion=p.codigo) " +
																" INNER JOIN solicitudes s ON(sc.numero_solicitud=s.numero_solicitud) " +
																" INNER JOIN cuentas c on (c.id=s.cuenta) " +
																" WHERE (sc.ind_qx='"+ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia+"' " +
																" OR sc.ind_qx='"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"' )" +
																" AND estado_peticion!="+ConstantesBD.codigoEstadoPeticionAtendida+
																" AND estado_peticion!="+ConstantesBD.codigoEstadoPeticionAnulada+
																" AND c.id_ingreso=? and c.estado_cuenta in ("+ConstantesBD.codigoEstadoCuentaActiva+","+ConstantesBD.codigoEstadoCuentaAsociada+","+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") "+
																
														") tabla ";		
													
									
	/**
	 * String para consultar los servicios de la petición
	 */
	private static final String strConsultarServiciosPeticion = " SELECT " +
																		"ps.servicio AS codigo_servicio0, " +
																		"getcodigopropservicio2(ps.servicio, "+ConstantesBD.codigoTarifarioCups+") AS cod_cups1, "+
																		"(s.codigo||'-'||s.especialidad||' '||getnombreservicio(ps.servicio,"+ConstantesBD.codigoTarifarioCups+")) AS servicio2, " +
																		"getnombreespecialidad(s.especialidad) as nom_especialidad3, "+
																		"ps.numero_servicio AS consecutivo4, " +
																		"s.tipo_servicio AS tipo_servicio5  "+
																" FROM peticiones_servicio ps "+
																" INNER JOIN servicios s ON(ps.servicio=s.codigo) "+
																" WHERE ps.peticion_qx=? " +
																" ORDER BY ps.numero_servicio ASC";
	
	/**
	 * String para consultar los servicios de la solicitud
	 */
	private static final  String strConsultarServiciosSolicitud ="SELECT " +
																		" scxs.servicio AS codigo_servicio0," +
																		" getcodigopropservicio2(scxs.servicio, "+ConstantesBD.codigoTarifarioCups+") AS cod_cups1, "+
																		" (s.especialidad||'-'||s.codigo||' '||getnombreservicio(scxs.servicio,"+ConstantesBD.codigoTarifarioCups+")) AS servicio2, " +
																		" getnombreespecialidad(s.especialidad) as nom_especialidad3, " +
																		" scxs.consecutivo AS consecutivo4, "+
																		" s.tipo_servicio AS tipo_servicio5, " +
																		" CASE WHEN s.espos="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN  s.especialidad||'-'||s.codigo||' '||getnombreservicio(scxs.servicio,"+ConstantesBD.codigoTarifarioCups+") || ' - POS' ELSE s.especialidad||'-'||s.codigo||' '||getnombreservicio(scxs.servicio,"+ConstantesBD.codigoTarifarioCups+") || ' - NO POS' END AS des_servicio6, " +
																		" scxs.observaciones AS observaciones7, " +
																		" '"+ConstantesBD.acronimoSi+"' AS esta_bd8," +
																		" scxs.codigo AS codigo9, " +
																		" scxs.finalidad AS finalidad13, " +
																		" s.naturaleza_servicio AS naturaleza14, " +
																		" scxs.via_cx AS via_cx16," +
																		" scxs.ind_bilateral AS ind_bilateral17," +
																		" scxs.ind_via_acceso AS ind_via_acceso18," +
																		" scxs.especialidad AS especialidad_inter19," +
																		" '"+ConstantesBD.acronimoNada+"' AS operacion25," +
																		" s.requiere_diagnostico AS requiere_serv27, " +
																		" scxs.numero_solicitud AS num_sol29," +
																		" s.requiere_interpretacion As req_inter30, " +
																		" CASE WHEN s.espos = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'POS' ELSE 'NOPOS' END AS espos34, " +
																		" s.especialidad As especialidad_servicio37 " + 
																  " FROM sol_cirugia_por_servicio scxs " +
																  " INNER JOIN servicios s ON (scxs.servicio=s.codigo) " +
																  " WHERE scxs.numero_solicitud=? " +
																  " ORDER BY scxs.consecutivo ASC";
	
	/**
	 * String utilizado para la actualizacion de LA SUBCUENTA cuando cambia el servicio
	 * de prioridad uno en la solicitud.
	 */
	private static final String strCambiaSubCuentaSolicitudesQx = "UPDATE solicitudes_cirugia SET sub_cuenta=? WHERE numero_solicitud=?";
	
	
	
	/**
	 * String cadena de consulta de informacion de la solicitud para 
	 * mostrarla en el encabezado de la hoja Qx y la hoja de anestecia 
	 */
	private static final String strConsultaDatosSolicitud="SELECT " +
															"s.centro_costo_solicitado AS cc_solicitado0, " +
															"coalesce(s.numero_autorizacion,'') AS num_autorizacion1, " +
															"(to_char(s.fecha_solicitud,'DD/MM/YYYY'))  AS fecha_solicitud2, " +
															"substr(s.hora_solicitud,0,6) AS hora_solicitud3, " +
															"s.especialidad_solicitante AS especialidad4, " +
															" coalesce(CASE WHEN s.urgente="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END,'') AS urgente5, " +
															"getnomcentrocosto(s.centro_costo_solicitado) AS nom_cc_solicitado6, " +
															"getnombreespecialidad(s.especialidad_solicitante) AS nom_especialidad7, " +
															"s.numero_solicitud as numero_solicitud8, " +
															"c.tipo_paciente AS tipo_paciente9, " +
															"getnombretipopaciente(c.tipo_paciente) AS nom_tipo_paciente10, " +
															"s.codigo_medico AS codigo_solicitante11, " +
															"administracion.getnombremedico(s.codigo_medico) as solicitante12, " +
															"coalesce(CASE WHEN p.requiere_uci="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END,'') AS requiere_uci13," +
															"sc.ind_qx AS tipo_cargo14, " +
															"s.estado_historia_clinica AS estado15, " +
															"s.consecutivo_ordenes_medicas AS orden16," +
															" getrequiereinterpretacion(s.numero_solicitud) As req_inter17," +
															"p.codigo AS codigo_peticion18," +
															"sc.ha_registrada_desde AS registrada_desde19," +
															"p.ingreso AS ingreso20 " +
														"FROM peticion_qx p " +
														"INNER JOIN solicitudes_cirugia sc ON(sc.codigo_peticion=p.codigo) " +
														"INNER JOIN solicitudes s ON(sc.numero_solicitud=s.numero_solicitud) " +
														"INNER JOIN cuentas c on (c.id=s.cuenta) " +
														"WHERE 1 = 1 ";
															
	/**
	 * metodo encargado de consultar los diagnosticos
	 * preoperatorios
	 */
	private static final String strConsultaDiagnosticos = "SELECT " +
																"dpcx.codigo AS codigo0, " +
																"dpcx.numero_solicitud AS num_solicitud1, " +
																"dpcx.diagnostico AS diagnostico2, " +
																"dpcx.tipo_cie AS tipo_cie3, " +
																"coalesce(CASE WHEN dpcx.principal="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END,'') AS principal4," +
																"getnombrediagnostico(dpcx.diagnostico,dpcx.tipo_cie) AS nom_diagnostico5," +
																"'"+ConstantesBD.acronimoSi+"' AS esta_bd6 "  +
															"FROM diag_preoperatorio_cx dpcx " +
															"WHERE dpcx.numero_solicitud=? ";
	
	/**
	 * Sentencia para ingresar el diagnóstico preoperatorio
	 */
	private static final String stringresarDiagnosticoPreoperatorio="INSERT INTO diag_preoperatorio_cx (codigo, numero_solicitud, diagnostico, tipo_cie, principal) VALUES(?,?,?,?,?)";
	
	private static final String strEliminarDiagnosticoPreoperatorio = "DELETE FROM diag_preoperatorio_cx where codigo=?";
	
	private static final String strModificarDiagnosticoPreoperatorio = "UPDATE diag_preoperatorio_cx SET diagnostico=?, tipo_cie=?, principal=? WHERE codigo=?";
	
	
	/**
	 * String que inserta la informacion basica de la hoja quirurgica
	 */
	private static final String strIngresarHojaQxBasica = "INSERT INTO hoja_quirurgica (numero_solicitud, finalizada, fecha_grabacion, hora_grabacion, datos_medico, medico_finaliza, anestesiologo, descripcion) VALUES (?,?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?)";
	
	/**
	 * String que actualiza el numero de autirizacion de la solicitud
	 */
	private static final String strActualizarAutorizacion = "UPDATE solicitudes SET numero_autorizacion=? WHERE numero_solicitud=? ";
	
	/**
	 * String que mira si existe o no la hoja Qx
	 */
	private static final String strConsultaExistenciaHojaQx = "SELECT count(*) as cantidad FROM hoja_quirurgica hq WHERE hq.numero_solicitud=?";
	
	
	
	
	/**
	 * Cadena usada para insertar un descripcion qx
	 */
	private static final String strInsertarDescripcionQx = "INSERT INTO descripciones_qx_hq " +
		"(descripcion,cod_sol_cx_serv,fecha_grabacion,hora_grabacion,usuario_grabacion,codigo) " +
		"VALUES (?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
	
	/**
	 * Cadena que modifica un profesional de cirugía
	 */
	private static final String strModificarProfesionalCirugia = "UPDATE profesionales_cirugia SET "+ 
																				"tipo_asocio = ?, "+ 
																				"especialidad = ?, "+
																				"codigo_profesional = ?, "+
																				"cobrable = ?, "+
																				"pool = ?, "+
																				"tipo_especialista = ?, " +
																				"fecha_modifica=CURRENT_DATE, " +
																				"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
																				"usuario_modifica = ? "+ 
																				"WHERE consecutivo = ?";
																			
	
	/**
	 * string encargado de listar todos los profesionales
	 * de que intervienen en un servicio de la solicitud
	 */
	private static final String strConsultarProfesionalesCx = "SELECT " +
																	" pc.consecutivo AS consecutivo0, " +
																	" pc.tipo_asocio AS tipo_asocio1, " +
																	" pc.especialidad AS especialidad2, " +
																	" pc.codigo_profesional AS codigo_profecional3, " +
																	" pc.cobrable AS cobrable4, " +
																	" pc.pool AS pool5, " +
																	" pc.tipo_especialista AS tipo_especialista6, " +
																	"'"+ConstantesBD.acronimoSi+"' AS esta_bd7," +
																	" administracion.getnombremedico(pc.codigo_profesional) AS nombre_profesional8," +
																	" getnombreasocio(pc.tipo_asocio) AS nom_tipo_asocio9," +
																	"'"+ConstantesBD.acronimoNo+"' AS eliminado13," +
																	"coalesce(getnombreespecialidad(pc.especialidad),'') as nombre_especialidad15," +
																	"coalesce(getdescripcionpool(pc.pool),'') as nombre_pool16 " +
																"FROM profesionales_cirugia pc " +
																"WHERE pc.cod_sol_cx_serv=?";
	
	private static final String strConsultProfCirujanoCx = " SELECT " +
																" pc.consecutivo AS consecutivo1," +
																" pc.especialidad AS especialidad2, " +
																" pc.codigo_profesional AS codigo_profecional3 " +
															" FROM profesionales_cirugia pc " +
															" WHERE pc.cod_sol_cx_serv=? AND tipo_asocio=?";
	
	
	
	/**
	 * cadena para consultar los diagnosticos postoperatorios
	 * del los servicios de sol_Cirugia_por_servicio
	 */
	private static final String strCosultarDiagPostOpera = " SELECT " +
																	" dposc.codigo AS codigo0, " +
																	" dposc.diagnostico AS diagnostico1, " +
																	" dposc.tipo_cie AS tipo_cie2, " +
																	" coalesce(CASE WHEN dposc.principal="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END,'') AS principal3, " +
																	" coalesce(CASE WHEN dposc.complicacion="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END,'') AS complicacion4, " +
																	" getnombrediagnostico(dposc.diagnostico,dposc.tipo_cie) AS nom_diagnostico5," +
																	"'"+ConstantesBD.acronimoSi+"' AS esta_bd6, " +
																	" dposc.cod_sol_cx_servicio AS cod_sol_cx_serv8 "  +
																" FROM diag_post_opera_sol_cx dposc" +
																" WHERE dposc.cod_sol_cx_servicio=?";
	
	
	/**
	 * Cadena que consulta las descripciones qx de una cirugía
	 */
	private static final String strConsultaDescripcionesQx = "SELECT "+ 
																	"codigo, "+
																	"descripcion, "+
																	"cod_sol_cx_serv AS consecutivo_servicio, "+
																	"to_char(fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"') AS fecha_grabacion, "+
																	"hora_grabacion, "+
																	"usuario_grabacion "+ 
																"FROM descripciones_qx_hq "+ 
																"WHERE cod_sol_cx_serv = ? "+ 
																"ORDER BY fecha_grabacion, hora_grabacion";
	
	/**
	 * string de consulta de las finalidades del servicio
	 */
	private static final String strConsultarFinalidadServicio = " SELECT " +
																" fsn.finalidad AS codigo, " +
																" getnomfinalidadservicio(fsn.finalidad) AS finalidad " +
															" FROM fin_serv_naturaleza fsn " +
															" WHERE fsn.naturaleza=? AND fsn.institucion=? ORDER BY finalidad";
	
	/**
	 *String de consulta que verifica si existe o no la hoja de anestecia 
	 */
	private static final String strVerificaExistenciaHojaAnestesia = "SELECT count (*) as contador from hoja_anestesia where numero_solicitud=?";
	
	/**
	 * String encargado de verificar si la hoja Qx esta finalizada o no.
	 */
	private static final String strVerificaSiHojaQxFinalizada = "SELECT CASE WHEN finalizada="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS finalizada FROM hoja_quirurgica where numero_solicitud=?";
	
	/**
	 * Sección SELECT para verificar si la sala esta ocupada
	 */
	private static final String estaSalaOcupada_Str = ""+
														"SELECT " +
														"sc.fecha_ingreso_sala AS fechaIngresoSala, " +
														"sc.hora_ingreso_sala AS horaIngresoSala, " +
														"sc.fecha_salida_sala AS fechaSalidaSala, " +
														"sc.hora_salida_sala AS horaSalidaSala,"+
														"count(1) AS resultado "+ 
														"FROM solicitudes s " +
														"INNER JOIN solicitudes_cirugia sc ON(sc.numero_solicitud=s.numero_solicitud) " +
														"INNER JOIN hoja_quirurgica hq ON(hq.numero_solicitud = sc.numero_solicitud) "+
														"WHERE " +
														"s.estado_historia_clinica="+ConstantesBD.codigoEstadoHCInterpretada+" AND " +
														" esSolicitudCargadaoInactiva(s.numero_solicitud) ='"+ConstantesBD.acronimoSi+"' AND " +
														"hq.sala=? AND "+ 
														"s.numero_solicitud != ? ";
	/**
	 * String encargado de ingresar los datos de un servico
	 */
	private static final String strInsertarServiciosQx = "INSERT INTO sol_cirugia_por_servicio (codigo,numero_solicitud,servicio,consecutivo," +
														 		  "institucion,finalidad, via_cx,ind_bilateral,ind_via_acceso,especialidad, " +
														 		  "cubierto, contrato_convenio) " +
														 "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * String encargado de eliminar los datos de la tabla sol_cirugia_por_servicio.
	 */
	private static final String strEliminarServicioQx = "DELETE FROM sol_cirugia_por_servicio WHERE codigo=?";
	
	/**
	 * Metodo encargado de actualizar los datos de los servicios.
	 */
	private static final String strActualizarServicioQx = "UPDATE sol_cirugia_por_servicio SET consecutivo=?, finalidad=?,via_cx=?," +
																							"ind_bilateral=?,ind_via_acceso=?,especialidad=? " +
																					"WHERE codigo=?";

	/**
	 * consulta el nombre del asocio.
	 */
	private static final String strNombreAsocio ="SELECT getnombreasocio(?)";
	
	/**
	 * Elimina una descripcion Qx
	 */
	private static final String strEliminarDescQx = "DELETE FROM descripciones_qx_hq WHERE cod_sol_cx_serv=?";
	
	/**
	 * String para la actualizacion de la informacion de los diagnosticos postoperatorio
	 */
	private static final String strModificarDiagPostOpera = " UPDATE diag_post_opera_sol_cx SET diagnostico=?, tipo_cie=?, fecha_modifica=CURRENT_DATE," +
																							" hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
																							" usuario_modifica=? WHERE codigo=?"; 
	
	
	private static final String strEliminarProfesionalesCx = " DELETE FROM profesionales_cirugia where cod_sol_cx_serv=? ";
	
	/**
	 * Actualiza la informacion de las especialidades que intervienen
	 * */
	public static final String strActualizarEspecIntervienen ="UPDATE " +
															" esp_intervienen_sol_cx SET " +
															" usuario_modifica = ?, " +
															" fecha_modifica=CURRENT_DATE," +
															" hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +				 			
															" asignada = '"+ConstantesBD.acronimoNo+"' " +			
															"WHERE numero_solicitud = ? " ;

								
	/**
	 * Encargado de actualizar los profecionales que intervienen
	 * en N
	 */
	public static final String strActualizarProfIntervienen ="UPDATE " +
															" cirujanos_esp_int_solcx SET " +
															" usuario_modifica = ?, " +
															" fecha_modifica=CURRENT_DATE," +
															" hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +			 			
															" asignada = '"+ConstantesBD.acronimoNo+"' " +			
															"WHERE numero_solicitud = ? " ;
	
	/**
	 * String encargado de consultar la informacion de
	 * la peticion 
	 */
	public static final String strConsultaDatosPeticion = "SELECT " +
															" pqx.codigo As codigo0," +
															" (to_char(pqx.fecha_cirugia,'DD/MM/YYYY')) As fecha_cirugia1," +
															" (to_char(pqx.FECHA_PETICION,'DD/MM/YYYY')) As fechapeticion," +
															" pqx.hora_PETICION as horapeticion,"+															
															" pqx.duracion As duracion2," +
															" CASE WHEN pqx.requiere_uci ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END As requiere_uci3," +
															" administracion.getnombremedico(pqx.solicitante) As solicitante4, " +
															" pqx.solicitante As cod_solicitante6," +
															" pqx.estado_peticion As estado_peticion7 " +
													" FROM peticion_qx pqx " +
													" WHERE pqx.codigo=?";
	
	/**
	 * 
	 */
	private static  final String strConsultasFechasCx = " SELECT " +
																" (to_char(sc.fecha_inicial_cx,'DD/MM/YYYY')) As fecha_inicial0," +
																" sc.hora_inicial_cx As hora_inicial1," +
																" (to_char(sc.fecha_final_cx,'DD/MM/YYYY')) As fecha_final2," +
																" sc.hora_final_cx As hora_final3," +
																" (to_char(pq.fecha_peticion,'DD/MM/YYYY')) As fecha_peticion8, " +
																" substr(pq.hora_peticion,0,6) AS hora_peticion9, " +
																" sc.duracion_final_cx As duracion_final_cx10 " +
														" FROM solicitudes_cirugia sc " +
														" INNER JOIN peticion_qx pq ON (pq.codigo=sc.codigo_peticion)" +
														" WHERE numero_solicitud=? ";
	
	
	private static final String strConsultaInfoQx ="SELECT " +
														   " hq.tipo_herida As tipo_herida0," +
														
														   " CASE WHEN hq.politrauma ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END As politrauma2," +
														   " hq.sala As sala3," +
														   " CASE WHEN hq.finalizada ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END As finalizada4," +
														   " (to_char(hq.fecha_finaliza,'DD/MM/YYYY')) As fecha_finaliza5," +
														   " hq.hora_finaliza As hora_finaliza6," +
														   " CASE WHEN hq.participo_anestesiologo is null THEN '' "+ 
														   " else "+
														   " CASE WHEN hq.participo_anestesiologo ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END END As part_anes7," +
														   " hq.tipo_anestesia As tipo_anes8," +
														   " hq.tipo_sala As tipo_sala9," +
														   " anestesiologo as anestesiologo," +
														   " (to_char(fecha_ini_anestesia,'DD/MM/YYYY')) as fechainianestesia," +
														   " hora_ini_anestesia as horainianestesia," +
														   " (to_char(fecha_fin_anestesia,'DD/MM/YYYY')) as fechafinanestesia, " +
														   " hora_fin_anestesia as horafinanestesia "+ 
													" FROM hoja_quirurgica hq " +
													" WHERE numero_solicitud=?";
	
	
	

	
	
	private static final String strConsultaProfInfoQx =" SELECT " +
																" piqx.consecutivo As consecutivo0," +
																" piqx.numero_solicitud As numero_solicitud1," +
																" piqx.tipo_asocio As tipo_asocio2," +
																" piqx.codigo_profesional As codigo_profesional3, " +
																"'"+ConstantesBD.acronimoSi+"' AS esta_bd5  " +
														" FROM profesionales_info_qx piqx" +
														" WHERE numero_solicitud = ?";
														
	private static final String strConsultaCamposTextoHQx = " SELECT " +
																	" cthqx.descripcion," +
																	" to_char(cthqx.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"') AS fecha_grabacion, "+
																	" cthqx.hora_grabacion, "+
																	" cthqx.usuario_grabacion "+ 
															" FROM campos_texto_hoja_qx cthqx " +
															" WHERE numero_solicitud=? and tipo=? " +
															"ORDER BY fecha_grabacion, hora_grabacion";

	
	private static final String strInsertarCamposTextHQx ="INSERT INTO campos_texto_hoja_qx (numero_solicitud,descripcion,tipo,usuario_grabacion) VALUES (?,?,?,?)";
	
	/**
	 * Encargado de actualizar los datos de la peticion
	 */
	private static final String strActualizarPeticion = "UPDATE peticion_qx SET " +
																				" requiere_uci=?," +
																				" solicitante =?" +
																		" WHERE codigo=?";
	
	private static final String strActualizarFechasCx = "UPDATE solicitudes_cirugia SET " +
																					" fecha_inicial_cx=?," +
																					" hora_inicial_cx=?," +
																					" fecha_final_cx=?," +
																					" hora_final_cx=?," +
																					"duracion_final_cx=? " +
																			" WHERE numero_solicitud=?"; 
						
	
	private static final String strActualizarInfoQx = "UPDATE hoja_quirurgica SET " +
																				" politrauma=?, " +
																				" tipo_sala=?, " +
																				" sala=?, " +
																				" tipo_herida=?, " +
																				" participo_anestesiologo=?, " +
																				" tipo_anestesia=?, " +
																				" fecha_grabacion=CURRENT_DATE," +
																				" observacion_capitacion=?, " +
																				" hora_grabacion="+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
																				" anestesiologo=?," +
																				" fecha_ini_anestesia=?," +
																				" hora_ini_anestesia=?," +
																				" fecha_fin_anestesia=?," +
																				" hora_fin_anestesia=? " +			 		
																			" WHERE numero_solicitud =? ";	
	
	
	
	/**
	 * se encarga de mirar la sala y el tipo si la  peticion esta en estado
	 * programada o reprogramada
	 */
	private static final String strSalaProgramada = "SELECT " +
															" pgs.sala As sala0, " +
															" s.tipo_sala As tipo_sala1 " +
														" FROM programacion_salas_qx pgs " +
														" INNER JOIN  salas s ON (s.consecutivo=pgs.sala) " +
														" WHERE peticion=?";
	
	/**
	 * string encargado de insertar los profesionales
	 * de la informacion quirurgica
	 */
	private static final String strInsertarProfInfoQx = "INSERT INTO profesionales_info_qx (numero_solicitud,tipo_asocio,codigo_profesional,cobrable,usuario_modifica,fecha_modifica,hora_modifica,historia_clinica,consecutivo) " +
																		" VALUES (?,?,?,?,?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
	
	/**
	 * String encagado de actualizar los profesionales
	 * de la informacion quirurgica
	 */
	private static final String strActualizarProfInfoQx ="UPDATE profesionales_info_qx SET" +
																							" tipo_asocio=?, " +
																							" codigo_profesional=?, " +
																							" usuario_modifica=?," +
																							" fecha_modifica = CURRENT_DATE, " +
																							" hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
																						" WHERE consecutivo=?";
	/**
	 * String encargado de eliminar los profesionales
	 * de la informacion quierurgica
	 */
	private static final String strEliminarProfInfoQx = "DELETE FROM profesionales_info_qx WHERE consecutivo=?";
	
	
	/**
	 * Strig encargado de cambiar el estado de la peticion
	 */
	private static final String strCambiarEstadoPeticion = "UPDATE peticion_qx SET " +
																					" estado_peticion=? " +
																				" WHERE codigo=?";
	/**
	 * String encargado de cambiar el estado de la solicitud
	 */
	private static final String strCambiarEstadoSolicitud = "UPDATE solicitudes SET " +
																					" fecha_grabacion=CURRENT_DATE," +
																					" hora_grabacion="+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +			 
																					" estado_historia_clinica=? " ;

	private static final String strConsultarSalidaPaciente = " SELECT " +
																	" (to_char(sc.fecha_ingreso_sala,'DD/MM/YYYY')) As fecha_ingreso_sala0," +
																	" sc.hora_ingreso_sala As hora_ingreso_sala1," +
																	" (to_char(sc.fecha_salida_sala,'DD/MM/YYYY')) As fecha_salida_sala2," +
																	" sc.hora_salida_sala As hora_salida_sala3," +
																	" ssp.salida_paciente_cc As salida_paciente4," +
																	" sc.salida_sala_paciente As sali_paci_select5 " +
																	
																" FROM solicitudes_cirugia sc " +
																" INNER JOIN salidas_sala_paciente ssp ON (ssp.consecutivo=sc.salida_sala_paciente)" +
																" WHERE sc.numero_solicitud=? ";
	
	private static final String strConsultaFallece = "SELECT " +
	
													" (to_char(fecha_fallece,'DD/MM/YYYY')) As fecha_fallece6," +
													" hora_fallece As hora_fallece7," +
													" CASE WHEN (fecha_fallece IS NOT NULL AND hora_fallece IS NOT NULL AND diag_fallece IS NOT NULL AND tipo_cie_fallece IS NOT NULL) THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END As fallece8," +
													" CASE WHEN (diag_fallece IS NULL OR tipo_cie_fallece IS NULL) THEN '' ELSE diag_fallece || '"+ConstantesBD.separadorSplit+"' || tipo_cie_fallece || '"+ConstantesBD.separadorSplit+"' || getnombrediagnostico(diag_fallece,tipo_cie_fallece) END AS diag_fallece9 "+
												" FROM solicitudes_cirugia " +
												" WHERE numero_solicitud=? ";
////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	/****************************************
	 * Adicionado por la tarea 3379
	 *****************************************/
	private static final String strActualizarFallece =" UPDATE  solicitudes_cirugia SET " +
																						" fecha_fallece=?, " +
																						" hora_fallece=?," +
																						" diag_fallece = ?," +
																						" tipo_cie_fallece = ? " +
																					" WHERE numero_solicitud=?";
	/****************************************
	 * fin Adicion por la tarea 3379
	 *****************************************/
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	private static final String strActualizarSalidaPaciente = " UPDATE  solicitudes_cirugia SET " +
																								" fecha_ingreso_sala=?, " +
																								" hora_ingreso_sala=?, " +
																								" fecha_salida_sala=?, " +
																								" hora_salida_sala=?, " +
																								" salida_sala_paciente=?, ";
															
	
	/**
	 * Sentencia para ingresar otros profesionales
	 */
	private static final String strIngresarOtrosProfesionales="INSERT INTO otros_prof_hoja_qx(hoja_quirurgica, codigo_medico, tipo_participante, especialidad) VALUES(?, ?, ?, ?)";
	
	
	private static final String strEliminarOtrosProfesionales="DELETE FROM otros_prof_hoja_qx WHERE hoja_quirurgica=? AND codigo_medico=?";
												
	private static final String strActualizarOtrosProfesionales="UPDATE otros_prof_hoja_qx SET " +
																								" tipo_participante=?," +
																								" codigo_medico=?, " +
																								" especialidad=? "+
																						" WHERE hoja_quirurgica=? AND " +
																								" codigo_medico=?";
																						
	private static final String strConsultarOtrosProfesionales="SELECT " +
																	" codigo_medico As codigo_medico0," +
																	" codigo_medico As codigo_medico_old7," +
																	" tipo_participante As tipo_participante1," +
																	" especialidad As especialidad2," +
																	" getnombreespecialidad(especialidad) As nom_especialidad3, " +
																	" '"+ConstantesBD.acronimoSi+"' AS esta_bd6  " +
																" FROM otros_prof_hoja_qx " +
																" WHERE hoja_quirurgica=?";
	/**
	 * String encargado de obtener los tipos de profesionales 
	 */
	private static final String strObtenerTiposProfesional="SELECT " +
											" tpi.codigo As codigo, " +
											" tp.nombre As nombre " +
										" FROM tipos_participantes_inst_qx tpi" +
										" INNER JOIN tipos_participantes_qx tp ON(tpi.tipo_profesional=tp.codigo) " +
										" WHERE  tpi.institucion=? AND tpi.activo='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' ORDER BY tp.nombre ";
	
	
	
	private static final String strCambiarEstadoHQx ="UPDATE hoja_quirurgica SET " +
																				" finalizada=?," +
																				" fecha_finaliza=?," +
																				" hora_finaliza=?," +
																				" fecha_grabacion=CURRENT_DATE," +
																				" hora_grabacion="+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
																				" medico_finaliza=? " +
																			"WHERE numero_solicitud=? ";
																			
	
	/**
	 * String de indices de la consulta de peticiones
	 */
	private static final String [] indicesPeticiones = HojaQuirurgica.indicesPeticiones;
	
	/**
	 * String de indices de los servicios por cada peticion
	 */
	private static final String [] indicesServicios = HojaQuirurgica.indicesServicios;
	
	/**
	 * String de indices de los datos que devuelve la consulta de la solicitud
	 */
	private static final String [] indicesSolicitudes = HojaQuirurgica.indicesSolicitud;
	
	/**
	 * String de indices de los datos que devuelve la consulta de diagnosticos preoperatorios
	 */
	private static final String [] indicesDiagnosticos = HojaQuirurgica.indicesDiagnosticos;
	
	/**
	 * String de indices de los datos que devuelve la consulta de profecionales
	 */
	private static final String [] indicesProfesionales = HojaQuirurgica.indicesProfesionales;
	
	/**
	 * String con los indices de los datos que devuelve los diagnosticos posoperatorios
	 */
	private static final String [] indicesDiagPostOpera = HojaQuirurgica.indicesDiagPostopera;
	
	private static final String [] indicesOtrosProfesionales = HojaQuirurgica.indicesOtrosProfesionales;
		
	/******************************************************************************************************************************************/
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * FIN DE ATRIBUTOS DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	
	
	/******************************************************************************************************************************************/
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * METODOS DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	////////////////////////////////////////////////////////////////////////////////////////////////
	/************************************
	 * Metodo adicionado por tarea 3379
	 *************************************/
	
	/**
	 * Metodo encargado de actualizar los datos de 
	 * fecha y hora fallece en la tabla solicitudes_cirugia
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param datos
	 * ------------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------------
	 * -- fechaFallece --> Requerido
	 * -- horaFallece --> Requerido
	 * -- numeroSolicitud --> Requerido
	 * @return true/false
	 */
	public static boolean ActualizarFallece (Connection connection, HashMap datos)
	{
		logger.info("\n entre a ActualizarFallece "+datos);		
	
		String cadena = strActualizarFallece;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//fecha_fallece
			if (UtilidadCadena.noEsVacio(datos.get("fechaFallece")+""))
				ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(datos.get("fechaFallece")+"")));
			else
				ps.setNull(1, Types.DATE);
			
			//hora_fallece
			if (UtilidadCadena.noEsVacio(datos.get("horaFallece")+""))
				ps.setString(2, datos.get("horaFallece")+"");
			else
				ps.setNull(2, Types.VARCHAR );
			
			//diagnostico acronimo
			if (UtilidadCadena.noEsVacio(datos.get("diagAcronimo")+""))
				ps.setString(3, datos.get("diagAcronimo")+"");
			else
				ps.setNull(3, Types.VARCHAR );
			
			//tipo cie diagnostico
			if (UtilidadCadena.noEsVacio(datos.get("diagTipoCie")+""))
				ps.setInt(4,Utilidades.convertirAEntero(datos.get("diagTipoCie")+""));
			else
				ps.setNull(4, Types.INTEGER);
			
			//numero_solicitud
			ps.setInt(5,Utilidades.convertirAEntero(datos.get("numeroSolicitud")+""));
			
			if (ps.executeUpdate()>0)
				return true;
						
		}
		catch (SQLException e) 
		{
			logger.info("\n problema actualizando la fecha y hora fallece en solicitudes_cirugia "+e);
		}
		return false;
	}
	
	/****************************************
	 * Fin metodo adicionado por tarea 3379
	 *****************************************/
	/////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Metodo encargado de consultar los servicios
	 * de la peticion
	 */
	public static HashMap consultarServiciosPeticion(Connection connection, String peticion)
	{
		logger.info("\n ENTRE A CONSULTAR ServiciosPeticion "+peticion);
		
		HashMap mapa = new HashMap ();
		String cadena = strConsultarServiciosPeticion;
		logger.info("\n consulta -->"+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//numero Solicitud 
			ps.setInt(1,Utilidades.convertirAEntero(peticion));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			
						
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultando ServiciosPeticion "+e);
		}
		return mapa;
	}
	
	
	
	/**
	 * Metodo encargado de cambiar de estado la Hoja
	 * Qx
	 * @param connection
	 * @param datos
	 * ------------------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------------------
	 * -- finalizada (true/false) --> Requerido
	 * -- medicoFin --> Requerido
	 * -- numSol --> Requerido
	 * @return (true/false)
	 */
	public static boolean cambiarEstadoHqx (Connection connection,HashMap datos)
	{
		logger.info("\n *** cambiar estado Hqx -->"+datos);
		String cadena=strCambiarEstadoHQx;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//finalizada
			ps.setBoolean(1,UtilidadTexto.getBoolean(datos.get("finalizada")+""));
			//se valida si se va afinalizar la hqx
			if (UtilidadTexto.getBoolean(datos.get("finalizada")+""))
			{
			//fecha finalizacion
				ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			//hora fin
				ps.setString(3,UtilidadFecha.getHoraActual());
			}
			else
				{
					//	fecha finalizacion
					ps.setNull(2, Types.DATE);
					//hora fin
					ps.setNull(3,Types.VARCHAR);
				}
			//medico finaliza
			ps.setInt(4,Utilidades.convertirAEntero(datos.get("medicoFin")+""));
			//numro solicitud
			ps.setInt(5,Utilidades.convertirAEntero(datos.get("numSol")+""));
						
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (Exception e) 
		{
			logger.error("\n problema cambiando el estado de la Hqx "+e);
		}
		
		return false;
	}
	
	
	
	/**
	 * Metodo encargado de consultar los tipos de profesionales
	 * @param connection
	 * @param institucion
	 * @return  ArrayList<HashMap>
	 * -------------------------------
	 * KEY'S DEL HASHMAP
	 * --------------------------------
	 * --codigo
	 * --nombre
	 */
	public static ArrayList< HashMap<String, Object>>  obtenerTiposProfesional (Connection connection,String institucion)
	{
		logger.info("\n ENTRO A CONSULTAR LOS TIPOS DE PROFESIONAL -->"+institucion);
		ArrayList result = new ArrayList ();
		String cadena=strObtenerTiposProfesional;
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet , ConstantesBD.concurrencyResultSet ));
			//codigo institucion
			ps.setInt(1,Utilidades.convertirAEntero(institucion));
		

			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next())
			{
				HashMap datos = new HashMap ();
				datos.put("codigo", rs.getObject("codigo"));
				datos.put("nombre", rs.getObject("nombre"));
				result.add(datos);
			}
			
			
		}
		catch (SQLException e)
		{
			logger.error("\n problema consultando tipos de profesionales "+e);
		}
		
		
		return result;
	}
	
	
	/**
	 * Metodo encargado de ingresar otros profesionales en la tabla
	 * otros_prof_hoja_qx.
	 * @param connection
	 * @param datos
	 * --------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------
	 * -- numSol
	 * -- codMedico
	 * -- tipoParticipante
	 * -- especialidad
	 * @return False/True
	 */
	public static boolean IngresarOtrosProfesionales (Connection connection,HashMap datos)
	{
		logger.info("\n ENTRO A INGRESAR OTROS PROFESIONALES -->"+datos);
		
		String cadena = strIngresarOtrosProfesionales;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//hoja_quirurgica
			ps.setInt(1,Utilidades.convertirAEntero(datos.get("numSol")+""));
			//codigo_medico
			ps.setInt(2,Utilidades.convertirAEntero( datos.get("codMedico")+""));
			//tipo_participante
			ps.setInt(3,Utilidades.convertirAEntero( datos.get("tipoParticipante")+""));
			//especialidad
			ps.setInt(4,Utilidades.convertirAEntero( datos.get("especialidad")+""));
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (Exception e) 
		{
			logger.info("problema ingresando otros profesionales "+e);
		}
		
		return false;
	}
	
	
	/**
	 * Metodo encargado de actualizar otros profesionales de la 
	 * tabla otros_prof_hoja_qx
	 * @param connection
	 * @param datos
	 * ----------------------------
	 * KEY'S DEL MAPA DATOS
	 * ----------------------------
	 * -- tipoParticipante
	 * -- numSol
	 * -- codMedico
	 * -- especialidad
	 * -- codMedicoOld
	 */
	public static boolean actualizarOtrosProfesionales (Connection connection,HashMap datos)
	{
		logger.info("\n ENTRO A ACTUALIZAR OTROS PROFESIONALES -->"+datos);
		
		String cadena = strActualizarOtrosProfesionales;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//tipo_participante
			ps.setInt(1,Utilidades.convertirAEntero(datos.get("tipoParticipante")+""));
			//codigo_medico
			ps.setInt(2,Utilidades.convertirAEntero(datos.get("codMedico")+""));
			//especialidad			
			ps.setInt(3,Utilidades.convertirAEntero(datos.get("especialidad")+""));
			//hoja_quirurgica
			ps.setInt(4,Utilidades.convertirAEntero(datos.get("numSol")+""));
			//codigo_medico
			ps.setInt(5,Utilidades.convertirAEntero(datos.get("codMedicoOld")+""));
			
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (Exception e) 
		{
			logger.info("problema actualizando otros profesionales "+e);
		}
		
		return false;
	}
	
	
	
	/**
	 * Metodo encargado de eliminar otros profesionales de la
	 * tabla otros_prof_hoja_qx
	 * @param connection
	 * @param numSol
	 * @param codMedico
	 * @return True/False
	 * 
	 */
	public static boolean eliminarOtrosProfesionales (Connection connection,String numSol,String codMedico)
	{
		logger.info("\n ENTRO A ELIMINAR OTROS PROFESIONALES solicitud -->"+numSol+"  codMedico-->"+codMedico);
		
		String cadena = strEliminarOtrosProfesionales;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//numero solicitud
			ps.setInt(1, Utilidades.convertirAEntero(numSol));
			//codigo medico
			ps.setInt(2, Utilidades.convertirAEntero(codMedico));
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (Exception e) 
		{
			logger.info("problema eliminando otros profesionales : "+e);
		}
		
		return false;
	}
	
	
	
	/**
	 * Metodo encargado de  consultar otros profesionales
	 * de la tabla otros_prof_hoja_qx
	 * @param connection
	 * @param numSol
	 * @return mapa
	 * ----------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ----------------------------
	 * -- codigoMedico0
	 * -- tipoParticipante1
	 * -- especialidad2
	 * -- nomEspecialidad3
	 */
	public static HashMap consultarOtrosProfesionales (Connection connection, String numSol)
	{
		logger.info("\n ENTRE A CONSULTAR OTROS PROFESIONALES "+numSol);
		
		HashMap mapa = new HashMap ();
		String cadena = strConsultarOtrosProfesionales;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//numero Solicitud 
			ps.setInt(1, Utilidades.convertirAEntero(numSol));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			//se ingresan las especialidades a cada profesional
			for (int i=0;i<Integer.parseInt(mapa.get("numRegistros")+"");i++)
			{
				mapa.put(indicesOtrosProfesionales[4]+i, Utilidades.obtenerEspecialidadesEnArray(connection, Integer.parseInt(mapa.get(indicesOtrosProfesionales[0]+i)+""), ConstantesBD.codigoNuncaValido));
			}
			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultando otros profesionales "+e);
		}
		return mapa;
	}
	
	
	
//******************************************************************************************************************************************************	
	
	
	/**
	 * Metodo encargado de Actualizar la salida del paciente
	 * en la tabla solicitudes_cirugia		
	 * @param connection
	 * @param datos
	 * -------------------------------
	 * KEY'S DEL MAPA DATOS
	 * -------------------------------										
	 *	-- fechaIngSala
	 *	-- horaIngSala
	 *	-- fechaSalSala
	 *	-- horaSalSala
	 *	-- salPac
	 *  -- desSelec S-->desseleccion ---- N--> Seleccion [Muy importante]
	 *  -- usuario
	 *  -- numSol
	 */
	public static boolean actualizarSalidaPaciente (Connection connection,HashMap datos)
	{
		logger.info("\n entre a actualizarSalidaPaciente  datos-->  "+datos);
		
		String cadena=strActualizarSalidaPaciente,where="  WHERE numero_solicitud=?";
		
		
		if ((datos.get("desSelect")+"").equals(ConstantesBD.acronimoSi))
		{
			cadena +=" fecha_deseleccion_salida=CURRENT_DATE, " +
					" hora_deseleccion_salida="+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
					" usuario_deseleccion_salida='"+datos.get("usuario")+"'" ;
		}
		else
		{
			cadena +=" fecha_grabacion_salida=CURRENT_DATE, " +
			" hora_grabacion_salida="+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
			" usuario_grabacion_salida='"+datos.get("usuario")+"'" ;
		}
		
		cadena+=where;
		
		logger.info("\n cadena de actualizacion de la salida del paciente "+cadena);		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//fecha_ingreso_sala
			if (!(datos.get("fechaIngSala")+"").equals("") && !(datos.get("fechaIngSala")+"").equals("-1"))
				ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(datos.get("fechaIngSala")+"")));
			else
				ps.setNull(1, Types.DATE);
			
			//hora_ingreso_sala
			if (!(datos.get("horaIngSala")+"").equals("") && !(datos.get("horaIngSala")+"").equals("-1"))
				ps.setString(2, datos.get("horaIngSala")+"");
			else
				ps.setNull(2, Types.VARCHAR);
			
			//fecha_salida_sala
			if (!(datos.get("fechaSalSala")+"").equals("") && !(datos.get("fechaSalSala")+"").equals("-1"))
				ps.setDate(3,  Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(datos.get("fechaSalSala")+"")));
			else
				ps.setNull(3, Types.DATE);
			
			//hora_salida_sala
			if (!(datos.get("horaSalSala")+"").equals("") && !(datos.get("horaSalSala")+"").equals("-1"))
				ps.setString(4, datos.get("horaSalSala")+"");
			else
				ps.setNull(4, Types.VARCHAR);
			
			//salida_paciente
			if (!(datos.get("salPac")+"").equals("") && !(datos.get("salPac")+"").equals("-1") && !(datos.get("salPac")+"").equals("null"))
				ps.setString(5, datos.get("salPac")+"");
			else
				ps.setNull(5, Types.VARCHAR);
			
			ps.setInt(6,Utilidades.convertirAEntero(datos.get("numSol")+""));
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema actualizando la salida del paciente "+e);
		}
		
		return false;
	}
	
	
	
	/**
	 * Consultar datos salida del paciente
	 * @param connection
	 * @param numSol
	 * @return mapa
	 * --------------------------------
	 * KEY'S DEL MAPA QUE DEVUELVE
	 * --------------------------------
	 * -- fechaIngresoSala0
	 * -- horaIngresoSala1
	 * -- fechaSalidaSala2
	 * -- horaSalidaSala3
	 * -- salidaPaciente4
	 * -- saliPaciSelect5
	 */
	public static HashMap consultarSalidaPaciente (Connection connection, String numSol)
	{
		HashMap mapa = new HashMap ();
		String cadena = strConsultarSalidaPaciente;
		try 
		{
			logger.info("\n cadena -->"+cadena);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//numero Solicitud 
			ps.setInt(1,Utilidades.convertirAEntero(numSol));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,true); 
			///////////////////////////////////////////////////////////
			//se moidifca por la tara 3379
			/////////////////////////////////////////////////////////
			cadena=strConsultaFallece;
			logger.info("\n cadena -->"+cadena);
			ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//numero Solicitud 
			ps.setInt(1,Utilidades.convertirAEntero(numSol));
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if (rs.next())
			{
				mapa.put(HojaQuirurgica.indicesSalidaSala[6], rs.getObject(1));
				mapa.put(HojaQuirurgica.indicesSalidaSala[7], rs.getObject(2));
				mapa.put(HojaQuirurgica.indicesSalidaSala[8], rs.getObject(3));
				mapa.put(HojaQuirurgica.indicesSalidaSala[9], rs.getObject(4));
			}
			else
			{
				mapa.put(HojaQuirurgica.indicesSalidaSala[6], "");
				mapa.put(HojaQuirurgica.indicesSalidaSala[7], "");
				mapa.put(HojaQuirurgica.indicesSalidaSala[8], ConstantesBD.acronimoNo);
				mapa.put(HojaQuirurgica.indicesSalidaSala[9], "");
			}
				
			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultando la salida del paciente "+e);
		}
		return mapa;
	}
	
	
	
	
	/**
	 * Metodo encargado de cambiar el estado de la solicitud
	 * @param connection
	 * @param HashMap parametros
	 * @return true /false
	 */
	@Deprecated
	public static boolean cambiarEstadoSolicitud (Connection connection,HashMap parametros)
	{
		logger.info("\n ENTRO A CAMBIAR ESTADO SOLICITUD estado nuevo--> "+parametros.get("estado")+"  solicitud--> "+parametros.get("numSol"));
		
		PreparedStatementDecorator ps = null;
		
		String cadena = strCambiarEstadoSolicitud;
		
		if(parametros.containsKey("codigoMedicoInterpretacion") && !parametros.get("codigoMedicoInterpretacion").toString().equals(""))
			cadena += " ,codigo_medico_interpretacion = "+parametros.get("codigoMedicoInterpretacion")+" " +
					  " ,fecha_interpretacion = CURRENT_DATE " +
					  " ,hora_interpretacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" ";
		
		
		cadena+=" WHERE numero_solicitud=?";
		
		try 
		{
			ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//estado
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("estado")+""));
			//numero Solicitud
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("numSol")+""));
			
			if (ps.executeUpdate()>0)
			{
				UtilidadBD.finalizarTransaccion(connection);
				return true;
			}

		}catch (SQLException e) {
			logger.error("ERROR -- SQLException cambiarEstadoSolicitud: "+e);

		}catch (Exception ex){
			logger.error("ERROR -- Exception cambiarEstadoSolicitud: "+ ex);
			
		}finally{
			try{
				if(ps != null){
					ps.close();
		}
		}
			catch (SQLException se) {
				logger.error("Error close PreparedStatement ", se);
		}
		}
		
		return false;
	}
	
	
	/**
	 * Metodo encargado de cambiar el estado
	 * de la peticion.
	 * @param connection
	 * @param estado
	 * @param peticion
	 * @return true/false
	 */
	@Deprecated
	public static boolean cambiarEstadoPeticion (Connection connection,String estado,String peticion)
	{
		logger.info("\n ENTRO A CAMBIAR ESTADO PETICION estado nuevo--> "+estado+"  peticion--> "+peticion);
		
		String cadena = strCambiarEstadoPeticion;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//estado
			ps.setInt(1,Utilidades.convertirAEntero(estado));
			//codigo
			ps.setInt(2,Utilidades.convertirAEntero(peticion));
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (Exception e) 
		{
			logger.info("problema cambiando estado de la peticion : "+e);
		}
		
		return false;
	}
	
	
	/**
	 * Metodo encargado de eliminar un profesional
	 * de l ainformacion Qx
	 */
	public static boolean eliminarProfInfoQx (Connection connection,String consec)
	{
		logger.info("\n ENTRO A ELIMINAR PROFESIONALES QX -->"+consec);
		
		String cadena = strEliminarProfInfoQx;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//Consecutivo
			ps.setInt(1,Utilidades.convertirAEntero(consec));
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (Exception e) 
		{
			logger.info("problema eliminando los profesionales info qx : "+e);
		}
		
		return false;
	}
	
	
	/**
 	 * Metodo encargado de actualizar los profesionales
 	 * de la informacion quirurgica
 	 * @param connection
 	 * @param datos
 	 * -------------------------
 	 * KEY'S DEL MAPA DATOS
 	 * -------------------------
 	 * -- tipoAsoc
 	 * -- prof
 	 * -- consec
 	 */
	public static boolean actualizarProfInfoQx (Connection connection,HashMap datos)
	{
		logger.info("\n ENTRO A ACTUALIZANDO PROFESIONALES QX -->"+datos);
		
		String cadena = strActualizarProfInfoQx;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//tipo Asocio
			ps.setInt(1,Utilidades.convertirAEntero(datos.get("tipoAsoc")+""));
			//coodigo profesional
			ps.setInt(2, Utilidades.convertirAEntero(datos.get("prof")+""));
			//usuario
			ps.setString(3, datos.get("usuario")+"");
			//Consecutivo
			ps.setInt(4, Utilidades.convertirAEntero(datos.get("consec")+""));
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (Exception e) 
		{
			logger.info("problema actualizando los profesionales info qx : "+e);
		}
		
		return false;
	}
	
	/**
	 * Metodo encargado de insertar los profesionales
	 * de la informacion quirurgica
	 * @param connection
	 * @param datos
	 * -----------------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------------
	 * -- numSol
	 * -- tipoAsoc
	 * -- prof
	 */
	public static boolean insertarProfInfoQx (Connection connection,HashMap datos)
	{
		logger.info("\n ENTRO A INSERTANDO PROFESIONALES QX -->"+datos);
		
		String cadena = strInsertarProfInfoQx;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//consecutivo
			int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_profesionales_info_qx");
			
			//numero Solicitud
			ps.setInt(1,Utilidades.convertirAEntero(datos.get("numSol")+""));
			//tipo Asocio
			ps.setInt(2,Utilidades.convertirAEntero(datos.get("tipoAsoc")+""));
			//codigo profesional
			ps.setInt(3, Utilidades.convertirAEntero(datos.get("prof")+""));
			//cobrable
			if(!UtilidadTexto.isEmpty(datos.get("cobrable")+""))
				ps.setString(4, datos.get("cobrable")+"");
			else
				ps.setString(4, ConstantesBD.acronimoSi);
			//usuario modifica
			ps.setString(5, datos.get("usuario")+"");
			if(!UtilidadTexto.isEmpty(datos.get("historiaClinica")+""))
				ps.setString(6,datos.get("historiaClinica")+"");
			else
				ps.setString(6,ConstantesBD.acronimoSi);
			
			//7)consecutivo
			ps.setInt(7, consecutivo);
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (Exception e) 
		{
			logger.info("problema ingresando los profesionales info qx : "+e);
		}
		
		return false;
	}
	
//*****************************************************************************************************************************************	
	
	/**
	 * Metodo encargado de consultar los 
	 * la sala y el tipo de la peticion en
	 * estado programada o reprogramada.
	 * @param connection
	 * @param peticion
	 * @return mapa
	 * ------------------------------
	 * KEY'S DEL MAPA QUE RETORNA
	 * ------------------------------
	 * -- sala0
	 * -- tipoSala1
	 */
	public static HashMap consultarSalaProgramada (Connection connection, String peticion)
	{
		HashMap mapa = new HashMap ();
		String cadena = strSalaProgramada;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//peticon
			ps.setInt(1,Utilidades.convertirAEntero(peticion));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,true); 
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultando la sala programada "+e);
		}
		return mapa;
	}
	
	
	
	/**
	 * Metodo encargado de consultar si la
	 * solicitud esta finalizada totalmente
	 * @param numSol
	 * @return S/N
	 */
	public static String essolicitudtotalpendiente (Connection connection,String numSol,String cadena)
	{
		
		try 
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//numero solicitud
			ps.setInt(1,Utilidades.convertirAEntero(numSol));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				return rs.getString(1);
			
		}
		catch (Exception e) 
		{
		 logger.info("\n problema consultando si la solicitud esta finalizada ");
			
		}
		return "";
	}
	
	
	/**
	 * Metodo encargado de actualizar los datos
	 * de la hoja quirurgica 
	 * @param connection
	 * @param datos
	 * ------------------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------------------
	 * -- poli
	 * -- tipoSala
	 * -- sala
	 * -- tipoHerida
	 * -- partAnest
	 * -- tipoAnest
	 * -- numSol
	 */
	public static boolean actualizarInfoQx (Connection connection,HashMap datos)
	{
		logger.info("\n ENTRO A ACTUALIZAR INFORMACION QX -->"+datos);
		
		String cadena = strActualizarInfoQx;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//politrauma
			
			ps.setBoolean(1, UtilidadTexto.getBoolean(datos.get("poli")+""));
				
			//tipo_sala
			if (Utilidades.convertirAEntero(datos.get("tipoSala")+"")>0){
				ps.setInt(2, Utilidades.convertirAEntero(datos.get("tipoSala")+""));
			}else{
				ps.setNull(2, Types.INTEGER);
			}
		
			//sala
			if (UtilidadCadena.noEsVacio(datos.get("sala")+"") && !(datos.get("sala")+"").equals("-1")){
				ps.setInt(3,Utilidades.convertirAEntero(datos.get("sala")+""));
			}else{
				ps.setNull(3, Types.INTEGER);
			}
		
			//tipo_herida
			if(UtilidadCadena.noEsVacio(datos.get("tipoHerida")+"")){
				ps.setString(4, datos.get("tipoHerida")+"");
			}else{
				ps.setNull(4,Types.VARCHAR);
			}
			
			//participo_anestesiologo
			ps.setBoolean(5, UtilidadTexto.getBoolean(datos.get("partAnest")+""));
			
			//tipo_anestesia
			if (!(datos.get("tipoAnest")+"").equals("") && !(datos.get("tipoAnest")+"").equals("-1")){
				ps.setInt(6,Utilidades.convertirAEntero(datos.get("tipoAnest")+""));
			}else{
				ps.setNull(6, Types.INTEGER);
			}
			
			
			// Anexo 179. Cambio 1.50
			if(!UtilidadTexto.isEmpty(datos.get("observacionCapitacion")+"")){
				ps.setString(7, datos.get("observacionCapitacion")+"");
			}
			else{
				ps.setNull(7, Types.CHAR);
			}
			
			// Anexo 179. Cambio 1.50
			if(!UtilidadTexto.isEmpty(datos.get("anestesiologo")+"") && !(datos.get("anestesiologo")+"").equals("-1")){
				ps.setString(8, datos.get("anestesiologo")+"");
			}
			else{
				ps.setObject(8, null);
			}
			if(!UtilidadTexto.isEmpty(datos.get("fechaInicioAnestesia")+"")){
				ps.setDate(9,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(datos.get("fechaInicioAnestesia")+"")));
				
			}
			else{
				ps.setObject(9, null);
			}
			if(!UtilidadTexto.isEmpty(datos.get("horaInicioAnestesia")+"")){
				ps.setString(10, datos.get("horaInicioAnestesia")+"");
			}
			else{
				ps.setObject(10, null);
			}
			if(!UtilidadTexto.isEmpty(datos.get("fechaFinAnestesia")+"")){
				ps.setDate(11,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(datos.get("fechaFinAnestesia")+"")));
			}
			else{
				ps.setObject(11, null);
			}
			if(!UtilidadTexto.isEmpty(datos.get("horaFinAnestesia")+"")){
				ps.setString(12, datos.get("horaFinAnestesia")+"");
			}
			else{
				ps.setObject(12, null);
			}
			
			
			//numero solicitud
			ps.setInt(13, Utilidades.convertirAEntero(datos.get("numSol")+""));
			
			if (ps.executeUpdate()>0){
				return true;
			}
			
		}
		catch (Exception e) 
		{
			logger.info("problema Actualizando los datos hoja quirurgica : "+e);
		}
		
		return false;
	}
	
	
	/**
	 * Metodo encargado de actualizar los datos de la fecha inicial,
	 * hora inicial, fecha final, hora final y duracion final de
	 * la cirugia.
	 * @param connection
	 * @param datos
	 * ------------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------------
	 * -- fechaIni
	 * -- horaIni
	 * -- fechaFin
	 * -- horaFin
	 * -- duracionFin
	 * -- numSol
	 */
	public static boolean actualizarFechas (Connection connection,HashMap datos)
	{
		logger.info("\n ENTRO A ACTUALIZAR FECHAS -->"+datos);
		
		String cadena = strActualizarFechasCx;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//fecha inicial
			if (!(datos.get("fechaIni")+"").equals("") && !(datos.get("fechaIni")+"").equals("null"))
				ps.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(datos.get("fechaIni")+"")));
			else
				ps.setNull(1, Types.DATE);
			
			//hora inicial
			if (!(datos.get("horaIni")+"").equals("") && !(datos.get("horaIni")+"").equals("null"))
				ps.setString(2, datos.get("horaIni")+"");
			else
				ps.setNull(2, Types.VARCHAR);
			
			//fecha final
			if (!(datos.get("fechaFin")+"").equals("") && !(datos.get("fechaFin")+"").equals("null"))
				ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(datos.get("fechaFin")+"")));
			else
				ps.setNull(3, Types.DATE);
			
			//hora final
			if (!(datos.get("horaFin")+"").equals("") && !(datos.get("horaFin")+"").equals("null"))
				ps.setString(4, datos.get("horaFin")+"");
			else
				ps.setNull(4, Types.VARCHAR);
			
			//duracion final
			if (!(datos.get("duracionFin")+"").equals("") && !(datos.get("duracionFin")+"").equals("null")) 
				ps.setString(5, datos.get("duracionFin")+"");
			else
				ps.setNull(5, Types.VARCHAR);
			
			//numero solicitud
			ps.setInt(6, Utilidades.convertirAEntero(datos.get("numSol")+""));
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (Exception e) 
		{
			logger.info("problema Actualizando las fechas de la cirugia : "+e);
		}
		
		return false;
	}
	
	
	
	
	/**
	 * Metodo encargado de actualizar la peticion
	 * @param connection
	 * @param datos
	 * --------------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------------
	 * -- requiereUci
	 * -- solicitante
	 * -- codigo
	 */
	public static boolean actualizarPeticion (Connection connection,HashMap datos)
	{
		logger.info("\n ENTRO A ACTUALIZAR PETICION -->"+datos);
		
		String cadena = strActualizarPeticion;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//requiere uci
			ps.setBoolean(1,UtilidadTexto.getBoolean(datos.get("requiereUci")+""));
			//solicitante
			ps.setInt(2, Utilidades.convertirAEntero(datos.get("solicitante")+""));
			//codigo
			ps.setInt(3, Utilidades.convertirAEntero(datos.get("codigo")+""));
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (Exception e) 
		{
			logger.info("problema Actualizando los datos de la peticion: "+e);
		}
		
		return false;
	}
	
	/**
	 * Metodo encargado de insertar los datos
	 * en la tabla campos_texto_hoja_qx
	 * @param connection
	 * @param datos
	 * -------------------------------
	 * KEY'S DEL MAPA DATOS
	 * -------------------------------
	 *  -- numSol --> Requerido
	 *  -- descripcion --> Requerido
	 *  -- tipo --> Requerido
	 *  -- usuario --> Requerido
	 *  @return false/true
	 */
	public static boolean insertarCamposTextHQx (Connection connection,HashMap datos)
	{
		logger.info("\n ENTRO A INSETTAR CAMPOS TEXTO HQX -->"+datos+" longitud descripcion -->"+(datos.get("descripcion")+"").length());
		
		String cadena = strInsertarCamposTextHQx;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//numero solicitud
			ps.setInt(1,Utilidades.convertirAEntero(datos.get("numSol")+""));
			//descripcion
			ps.setString(2, datos.get("descripcion")+"");
			//tipo
			ps.setString(3, datos.get("tipo")+"");
			//usuario
			ps.setString(4, datos.get("usuario")+"");
			
			if (ps.executeUpdate()>0)
				return true;
			
			
		}
		catch (SQLException e) 
		{
			logger.info("\n Problema en insertarCamposTextHQx "+e);
		}
		
		return false;
	}
	
	
	
	
	
	 /**
	  * Metodo encargado de consultar los campos de texto
	  * de la hoja Qx
	  */
    public static String consultarCamposTextoHQx (Connection con,String numSol,String tipo)
    {
    	String result="";
    	String cadena=strConsultaCamposTextoHQx;
    	logger.info("\n ******************** entre a consultar los campos de texto de la hqx **************");
    	try
    	{
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
    		//numero solicitud
    		pst.setInt(1,Utilidades.convertirAEntero(numSol));
    		//tipo
    		pst.setString(2,tipo);
    		
    		ResultSetDecorator resultado=new ResultSetDecorator(pst.executeQuery());
			while(resultado.next())
			{
				String desc =  resultado.getString(1);
				String fecha = resultado.getString(2);
				String hora = resultado.getString(3);
				String usuario = resultado.getString(4);
				
				UsuarioBasico usu = new UsuarioBasico();
				usu.cargarUsuarioBasico(con, usuario);
				result += "Fecha:"+fecha+"  -----  Hora:"+hora+"\n"+desc+"\n"+"Usuario:"+usu.getInformacionGeneralPersonalSalud()+"\n\n";
			}
    		
    	}
    	catch(SQLException e)
    	{
    		logger.error("Error en consultarCamposTextoHQx: "+e);
    	}
    
    	return  result;
    }
	
	
	
	
	/**
	 * Metodo encargado de consultar los profesionales
	 * de la informacion Qx.
	 * @param connection
	 * @param numSol
	 * @return mapa
	 * ----------------------------------
	 * KEY'S DEL MAPA QUE DEVUELVE
	 * ----------------------------------
	 * -- consecutivo0
	 * -- numeroSolicitud1
	 * -- tipoAsocio2
	 * -- codigoProfesional3
	 * 
	 */
	public static HashMap consultarProfInfQx (Connection connection,String numSol)
	{
		logger.info("\n ************ ENTRO A COSNULTAR PROFESIONALES INFO QX ******** "+numSol); 
		HashMap mapa = new HashMap ();
		String cadena = strConsultaProfInfoQx;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//numero solicitud
			ps.setInt(1,Utilidades.convertirAEntero(numSol));
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
		}
		catch (SQLException  e) 
		{
			logger.info("\n problema consultando la informacion de profesionales info Qx "+e);
		}
		
		
		return mapa;
	}
	
	
	
	/**
	 * Metodo encargado de consultar la informacion de la hoja quierugica
	 * @param connection
	 * @param numSol
	 * 
	 * @return mapa
	 * -----------------------------
	 * KEY'S DEL MAPA QUE DEVUELVE
	 * -----------------------------
	 * -- tipoHerida0
	 * -- duracionFinalCx1
	 * -- politrauma2
	 * -- sala3
	 * -- finalizada4
	 * -- fechaFinaliza5
	 * -- horaFinaliza6
	 * -- partAnes7
	 * -- tipoAnes8
	 * -- tipoSala9
	 */
	public static HashMap consultarDatosHQX (Connection connection,String numSol)
	{
		logger.info("\n ************ ENTRO A COSNULTAR INFORMACION DE LA HQX ******** "+numSol); 
		HashMap mapa = new HashMap ();
		String cadena = strConsultaInfoQx;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//numero solicitud
			ps.setInt(1,Utilidades.convertirAEntero(numSol));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), false, true);
		}
		catch (SQLException  e) 
		{
			logger.info("\n problema consultando la informacion Qx "+e);
		}
		
		
		return mapa;
	}
	
	
	
	
	/**
	 * Metodo encargado de consultar
	 * Las fechas de inicio y finalizacion de la
	 * Cirugia
	 */
	public static HashMap consultarDatosFechas (Connection connection, String solicitud)
	{
		logger.info("\n************* ENTRO A CONSULTAR FECHAS Cx para la solicitud --> "+solicitud);
		HashMap mapa = new HashMap ();
		String cadena = strConsultasFechasCx;
		
		try
		{
		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		 //solicitud
		 ps.setInt(1, Utilidades.convertirAEntero(solicitud));
		 mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), false, true);
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultando las fechss Cx de la Solicitud "+e);
		}
		
		return mapa;
	}
	
	
	
	
	/**
	 * Metodo encargado de consutar los datos de la peticion
	 * @return mapa
	 * ---------------------------------
	 * KEY'S DEL MAPA QUE DEVUELVE
	 * ---------------------------------
	 * -- codigo0
	 * -- fechaCirugia1 
	 * -- duracion2 
	 * -- requiereUci3 
	 * -- solicitante4 
	 */
	public static HashMap consultarDatosPeticion (Connection connection, String peticion)
	{
		logger.info("\n************* ENTRO A CONSULTAR LOS DATOS DE LA PETICION para la peticion --> "+peticion);
		HashMap mapa = new HashMap ();
		String cadena = strConsultaDatosPeticion;
		
		try
		{
		 PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		 //peticion
		 ps.setInt(1,Utilidades.convertirAEntero(peticion));
		 mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), false, true);
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultando los datos de la peticion "+e);
		}
		
		return mapa;
	}
	
	
	/**
	 * Metodo encargado de Actualizar
	 * las profesional en N
	 */
	private static boolean actualizarEnNoProfInter (Connection connection, String numSol,String usuario)
	{
		logger.info("\n ENTRO A ACTUALIZAR EN N PROF INTER ");
		String cadena = strActualizarProfIntervienen;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//usuario modifica
			ps.setString(1, usuario);
			//numero de la solicitud
			ps.setInt(2,Utilidades.convertirAEntero(numSol));
			
			if (ps.executeUpdate()>0)
				return true;
		} 
		catch (Exception e) 
		{
			logger.info("\n problema actualizando a N los prof asignados "+e);	
		}
		
		return false;
	}
	
	
	
	/**
	 * Metodo encargado de consultar 
	 * todos los profecionales de la cirugia
	 */
	private static HashMap consultProfCirugiaCx (Connection connection, String numSol,int codinstitu )
	{
		logger.info("\n ENTRO A CONSULTAR PROF CX numero Solicitud-->"+numSol+" codigo institucion -->"+codinstitu);
		String cadena = strConsultProfCirujanoCx;
		HashMap mapa = new HashMap();
		
		logger.info("\n cadena --> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//cod_sol_cx_serv
			ps.setInt(1,Utilidades.convertirAEntero(numSol));
			//tipo_asocio
			ps.setInt(2, Utilidades.convertirAEntero(ValoresPorDefecto.getAsocioCirujano(codinstitu)));
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
		}
		catch (SQLException e)
		{
		 logger.info(" problema consultando los profesionales del servicio "+e);
		}
		
		return mapa;
	}
	
	
	
	/**
	 * Metodo encargado de Actualizar
	 * las especialidades en N
	 */
	private static boolean actualizarEnNoEspInter (Connection connection, String numSol,String usuario)
	{logger.info("\n ENTRO A ACTUALIZAR EN N ESP INTER ");
		
		String cadena = strActualizarEspecIntervienen;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//usuario modifica
			ps.setString(1, usuario);
			//numero de la solicitud
			ps.setInt(2,Utilidades.convertirAEntero(numSol));
			
			if (ps.executeUpdate()>0)
				return true;
		} 
		catch (Exception e) 
		{
			logger.info("\n problema actualizando a N las esp asignados "+e);	
		}
		
		return false;
	}
	
	
	
	
	/**
	 * Metodo encargado de actualiazar los datos de las
	 * tablas esp_intervienen_solcx y cirujanos_esp_int_solcx
	 * dependiendo de las especialidades y cirujanos del servicio.
	 * @param connection
	 * @param datos
	 * ---------------
	 * KEY'S DATOS
	 * ---------------
	 * -- solicitud --> Requerido
	 * @param usuario
	 * @return
	 */
	public static boolean ActualizarEspecialidadesAndCirujanos (Connection connection,HashMap datos,UsuarioBasico usuario)
	{
		
		
		logger.info("\n ********ENTRO A ACTUALIZAR ESPECIALIDADES Y CIRUJANOS ***** ==> "+datos);
		HashMap servicios = new HashMap();
		String cadena = strConsultarServiciosSolicitud;
		boolean operacionTrue= true;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//solicitud
			ps.setInt(1,Utilidades.convertirAEntero(datos.get("solicitud")+""));
			
			servicios=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
				
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultando los servicios "+e);
		}
		logger.info("\n ******** 1 ***************"+operacionTrue);
			HojaAnestesia mundoAnestesia = new HojaAnestesia();
		
			ArrayList 	especialidades = new ArrayList ();
		  	//se existen registros de especialidades en la tabla esp_intervienen_sol_cx
			especialidades=UtilidadesSalas.obtenerEspecialidadesIntervienen(connection, datos.get("solicitud")+"", "");
			//se actualizan todas las especialidades en N
			
				actualizarEnNoEspInter(connection, datos.get("solicitud")+"",usuario.getLoginUsuario());
			
				actualizarEnNoProfInter(connection, datos.get("solicitud")+"", usuario.getLoginUsuario());
			
			//ahora se iteran las especialidades de la solicitud
			int numReg = Integer.parseInt(servicios.get("numRegistros")+"");
			for (int k=0;k<numReg;k++)
			{
				//se existen registros de especialidades en la tabla esp_intervienen_sol_cx
				especialidades=UtilidadesSalas.obtenerEspecialidadesIntervienen(connection, datos.get("solicitud")+"", "");
				
				logger.info("\n ******** 2 ***************"+operacionTrue);
				//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
				//----------------------------se consultan los cirujanos de cada servicio -------------------
				HashMap cirujanosQx = new HashMap ();
				cirujanosQx=consultProfCirugiaCx(connection,servicios.get(indicesServicios[9]+k)+"",usuario.getCodigoInstitucionInt());
				
				logger.info("\n *************** mapa cirujanos "+cirujanosQx);	
				boolean inseresp=true;
				
				for (int i=0;i<especialidades.size();i++)
				{
					
					HashMap elemento = (HashMap)especialidades.get(i);
				
					logger.info("\n ******** 3 ***************"+operacionTrue);
					//se pregunta si el codigo que la especialidad ya esta en la tabla esp_intervienen_solcx
					logger.info("comparacion --> "+servicios.get(indicesServicios[19]+k)+" == "+elemento.get("codigo"));
					if((servicios.get(indicesServicios[19]+k)+"").equals(elemento.get("codigo")+""))
					{
						inseresp=false;
						logger.info("\n ******** 4 ***************"+operacionTrue);
						//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
						//------------- SE ACTUALIZA LA ESPECIALIDAD A ASIGNADA S --------------------------
						HashMap parametros = new HashMap ();
						parametros.put("asignada", ConstantesBD.acronimoSi); 
						parametros.put("numeroSolicitud", datos.get("solicitud"));
						parametros.put("especialidad", servicios.get(indicesServicios[19]+k));
						//se actualiza cada especialidad con asigana S
						if(operacionTrue)
						{
							logger.info("\n ******** 5 ***************"+operacionTrue);
							operacionTrue=mundoAnestesia.actualizarEspecialidadesIntervienen(connection, parametros);
						}
						//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
						//---------------- AHORA SE CONSULTAN LOS CIRUJANOS DE ESA ESPECIALIDAD ------------------
						HashMap cirujanosAnes = new HashMap ();
						cirujanosAnes=mundoAnestesia.consultarCirujanosIntervienen(connection, datos.get("solicitud")+"", elemento.get("codigo")+"");
						
						//Se actualizan en N Todos los Cirujanos de la tabla cirujanos_esp_int_solcx
										
						for (int c=0;c<Integer.parseInt(cirujanosQx.get("numRegistros")+"");c++)
						{	
							logger.info("\n ******** sebas ***************");
							boolean ban=false;
							for (int j=0;j<Integer.parseInt(cirujanosAnes.get("numRegistros")+"");j++)
							{
								logger.info("comparacion cirujanos --> "+cirujanosQx.get("especialidad2_"+c)+" == "+cirujanosAnes.get("especialidad2_"+j));
								if ((cirujanosQx.get("codigoProfecional3_"+c)+"").equals(cirujanosAnes.get("profesional4_"+j)+""))
								{
									logger.info("\n ******** 6 ***************"+operacionTrue);
									//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
									//----------------- SE ACTUALIZAN LOS CIRUJANOS EN S -----------------------
									HashMap param = new HashMap ();
									param.put("usuarioModifica", usuario.getLoginUsuario());
									param.put("fechaModifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
									param.put("horaModifica", UtilidadFecha.getHoraActual());
									param.put("asignada6_", ConstantesBD.acronimoSi);
									param.put("consecutivo0_", cirujanosAnes.get("consecutivo0_"+j));
									
									//aqui esta el error 
									
									if (operacionTrue)
									{
										logger.info("\n ******** 7 ***************"+operacionTrue);
										operacionTrue=mundoAnestesia.actualizarCirujanosIntervienen(connection, param);
										ban=true;
									}
								}
								
							}
						
								if(!ban)
								{
									logger.info("\n ******** 8 ***************"+operacionTrue);
									//----------------------------- SE INGRESAN LOS CIRUJANOS SI NO EXISTEN --------------------------
									if (operacionTrue)
											operacionTrue=mundoAnestesia.insertarCirujanosIntervienen(connection, datos.get("solicitud")+"", servicios.get(indicesServicios[19]+k)+"", cirujanosQx.get("codigoProfecional3_"+c)+"", ConstantesBD.acronimoSi, usuario.getLoginUsuario());
								}
							
						}
						
						
					}
												
				}
				
				if(inseresp)
				{
					logger.info("\n ******** 9 ***************"+operacionTrue);
					//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
					//-------------------SE INGRESAN LAS ESPECIALIDADES SI NO EXISTEN ---------------------
					if (operacionTrue)
					operacionTrue=mundoAnestesia.insertarEspecialidadesIntervienen(connection, datos.get("solicitud")+"", servicios.get(indicesServicios[19]+k)+"", ConstantesBD.acronimoSi, usuario.getLoginUsuario());
					//------------------------- SE INGRESA LOS CIRUJANOS DE ESA ESCIALIDAD  -----------------------------------------
					
					for (int c=0;c<Integer.parseInt(cirujanosQx.get("numRegistros")+"");c++)
					{
						logger.info("\n ******** 10 ***************"+operacionTrue);
						if (operacionTrue)
							operacionTrue=mundoAnestesia.insertarCirujanosIntervienen(connection, datos.get("solicitud")+"", servicios.get(indicesServicios[19]+k)+"", cirujanosQx.get("codigoProfecional3_"+c)+"", ConstantesBD.acronimoSi, usuario.getLoginUsuario());
					}
					
					
						
				}
				
										
				
				
				
					
			}
			
			if (operacionTrue)			
				return true;
			else
				return false;
	}
	
	
	
	/**
	 * Metodo encargado de eliminar los profecionales
	 * de la cirugia
	 * @param connection
	 * @param codSolCxXServ
	 * @param consecutivo
	 */
	public static boolean eliminarProfesionalesCx (Connection connection,String codSolCxXServ,String consecutivo)
	{
		logger.info("\n ENTRO A ELIMINAR PROFESIONALES SQL "+codSolCxXServ);
		String cadena = strEliminarProfesionalesCx;
	
		if (!consecutivo.equals("") && !consecutivo.equals(ConstantesBD.codigoNuncaValido+""))
			cadena +=" AND consecutivo="+consecutivo;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//codigo sol_cirugia_por_servicio
			ps.setInt(1,Utilidades.convertirAEntero(codSolCxXServ));
			
			if (ps.executeUpdate()>0)
				return true;
			
		} 
		catch (Exception e)
		{
			logger.info("\n problema eliminando los profecionales de la cirugia "+e);	
		}
		
		
		return false;
	}
	
	
	
	
	/**
	 * Metodo encargado de m odificar los diagnosticos 
	 * PostOperatorios.
	 * @param connection
	 * @param datos
	 * --------------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------------	
	 * -- diagnosticos --> Requerido
	 * -- tipoCie --> Requerido
	 * -- usuarioModifica --> Requerido
	 * -- codigo --> Requerido 
	 */
	public static boolean modificarDiagPostOpera (Connection connection, HashMap datos)
	{
		logger.info("\n ENTRE A MODIFICAR DIAGNOSTICOS POSTOPERATORIOS "+datos);
		String cadena= strModificarDiagPostOpera;
		
		try 
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//diagnostico
			ps.setString(1, datos.get("diagnostico")+"");
			//tipoCie
			ps.setInt(2, Utilidades.convertirAEntero(datos.get("tipoCie")+""));
			//usuario_modifica
			ps.setString(3, datos.get("usuarioModifica")+"");
			//codigo
			ps.setInt(4,Utilidades.convertirAEntero(datos.get("codigo")+""));
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			logger.info("\n problema actualizando los datos de los diagnosticosPostOperatorios "+e);
		}
		
		return false;
	}
	
	
	
	/**
	 * Metodo encargado de eliminar los diagnosticos
	 * postoperatorios de la tabla diag_post_opera_sol_cx
	 * @param connection
	 * @param codSolCxXServ
	 */
	public static boolean eliminarDiagPostOpe (Connection connection,String codSolCxXServ,String codigo)
	{
		logger.info("\n ENTRO A ELIMINAR DIAGNOSTICOS POSTOPERATORIOS SQL "+codSolCxXServ);
		String cadena = eliminarDiagnosticosStr;
		
		if (!codigo.equals("") && !codigo.equals(ConstantesBD.codigoNuncaValido+""))
			cadena +=" AND codigo="+codigo;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//codigo sol_cirugia_por_servicio
			ps.setInt(1,Utilidades.convertirAEntero(codSolCxXServ));
			
			if (ps.executeUpdate()>0)
				return true;
			
		} 
		catch (Exception e)
		{
			logger.info("\n problema eliminando los datos en la tabla diag_post_opera_sol_cx "+e);	
		}
		
		
		return false;
	}
	
	/**
	 * Metodo encargado de eliminar la descripcion Qx
	 * de la tabla sol_cirugia_por_servicio
	 * @param connection
	 * @param codSolCxXServ
	 */
	public static boolean eliminarDecQx (Connection connection,String codSolCxXServ)
	{
		logger.info("\n ENTRO A ELIMINAR DESCRIPCION SQL "+codSolCxXServ);
		String cadena = strEliminarDescQx;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//codigo sol_cirugia_por_servicio
			ps.setInt(1,Utilidades.convertirAEntero(codSolCxXServ));
			
			if (ps.executeUpdate()>0)
				return true;
			
		} 
		catch (Exception e)
		{
			logger.info("\n problema eliminando los datos en la tabla descripciones_qx_hq "+e);	
		}
		
		
		return false;
	}
	
	
	
	
	/**
	 * Actualizar los datos del servicio.
	 * @param connection
	 * @param datos
	 * -----------------------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------------------
	 * -- consecutivo --> Requerido
	 * -- indBilateral --> Requerido
	 * -- indViaAcceso --> Requerido
	 * -- especialidad --> Requerido
	 * -- codigo --> Requerido
	 * -- finalidad --> Opcional
	 * -- viaCx --> Opcional
	 */
	public static boolean ActualizarServicioQx (Connection connection,HashMap datos)
	{
		logger.info("\n ENTRO A MODIFICAR SERVICIO SQL "+datos);
		String cadena = strActualizarServicioQx;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//consecutivo
			ps.setInt(1, Utilidades.convertirAEntero(datos.get("consecutivo")+""));

			//finalidad
			if (datos.containsKey("finalidad") && !(datos.get("finalidad")+"").equals("") && !(datos.get("finalidad")+"").equals("-1"))
				ps.setInt(2,Utilidades.convertirAEntero(datos.get("finalidad")+""));
			else
				ps.setNull(2, Types.INTEGER);
			
			//via_cx
			if (datos.containsKey("viaCx") && !(datos.get("viaCx")+"").equals("") && !(datos.get("viaCx")+"").equals("-1"))
				ps.setString(3, datos.get("viaCx")+"");
			else
				ps.setNull(3, Types.VARCHAR);
			
			//indBialateral
			ps.setString(4, datos.get("indBilateral")+"");
			//indViaAcceso
			ps.setString(5, datos.get("indViaAcceso")+"");
			//especialidad
			ps.setInt(6,Utilidades.convertirAEntero(datos.get("especialidad")+""));
			//codigo
			ps.setInt(7,Utilidades.convertirAEntero(datos.get("codigo")+""));
			
			
			if (ps.executeUpdate()>0)
				return true;
			
		} 
		catch (Exception e)
		{
			logger.info("\n problema Modificando los datos en la tabla sol_cirugia_por_servicio "+e);	
		}
		
		
		return false;
	}
	
	
	
	
	
	
	
	
	/**
	 * Metodo encargado de eliminar un servicio
	 * de la tabla sol_cirugia_por_servicio
	 * @param connection
	 * @param codSolCxXServ
	 */
	public static boolean eliminarServicioQx (Connection connection,String codSolCxXServ)
	{
		logger.info("\n ENTRO A ELIMINAR SERVICIO SQL "+codSolCxXServ);
		String cadena = strEliminarServicioQx;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//codigo sol_cirugia_por_servicio
			ps.setInt(1,Utilidades.convertirAEntero(codSolCxXServ));
			
			if (ps.executeUpdate()>0)
				return true;
			
		} 
		catch (Exception e)
		{
			logger.info("\n problema eliminando los datos en la tabla sol_cirugia_por_servicio "+e);	
		}
		
		
		return false;
	}
	
	public static Integer codigoServicio = null;
	
	/**
	 * Metodo encargado de insertar los datos
	 * de la seccion servicios en la tabla 
	 * sol_cirugia_por_servicio
	 * @param connection
	 * @param datos
	 * --------------------------
	 * KEY'S DEL MAPA DATOS
	 * -------------------------
	 * -- numSolicitud --> Requerido
	 * -- servicio --> Requerido
	 * -- consecutivo --> Requerido
	 * -- institucion --> Requerido
	 * -- indBilateral --> Requerido
	 * -- indViaAcceso --> Requerido
	 * -- especialidad --> Requerido
	 * -- finalidad --> Opcional
	 * -- viaCx --> Opcional
	 * @return (true/false)
	 */
	public static boolean insertarServiciosQx (Connection connection,HashMap datos)
	{
		logger.info("\n ENTRO A INSERTAR SERVICIO SQL "+datos);
		String cadena=strInsertarServiciosQx; 
		PreparedStatementDecorator ps = null;
				
		try {
			ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			int codigoServicio=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).incrementarValorSecuencia(connection, "seq_sol_cx_ser");
			SqlBaseHojaQuirurgicaDao.codigoServicio = codigoServicio;
			//--------------%%%% Datos requeridos %%%%%%------------
			//codigo
			ps.setInt(1, codigoServicio);
			//numero_solicitud
			ps.setInt(2, Utilidades.convertirAEntero(datos.get("numSolicitud")+""));
			//servicio
			ps.setInt(3, Utilidades.convertirAEntero(datos.get("servicio")+""));
			//consecutivo
			ps.setInt(4, Utilidades.convertirAEntero(datos.get("consecutivo")+""));
			//institucion
			ps.setInt(5, Utilidades.convertirAEntero(datos.get("institucion")+""));
			//indBialateral
			ps.setString(8, datos.get("indBilateral")+"");
			//indViaAcceso
			ps.setString(9, datos.get("indViaAcceso")+"");
			//especialidad
			ps.setInt(10, Utilidades.convertirAEntero(datos.get("especialidad")+""));
			//--------------%%%% Fin Datos requeridos %%%%%%------------
			
			//finalidad
			if (datos.containsKey("finalidad") && !(datos.get("finalidad")+"").equals("") && !(datos.get("finalidad")+"").equals("-1"))
				ps.setInt(6,Utilidades.convertirAEntero(datos.get("finalidad")+""));
			else
				ps.setNull(6, Types.INTEGER);
			
			//via_cx
			if (datos.containsKey("viaCx") && !(datos.get("viaCx")+"").equals("") && !(datos.get("viaCx")+"").equals("-1"))
				ps.setString(7, datos.get("viaCx")+"");
			else
				ps.setNull(7, Types.VARCHAR);
			
			//cubierto
			if(datos.containsKey("cubierto") && !(datos.get("cubierto").toString()).equals("")) {
				ps.setString(11, datos.get("cubierto").toString());
			} else {
				ps.setNull(11, Types.VARCHAR);
			}
			
			//contrato/convenio
			if(datos.containsKey("contratoConvenio") ) {
				ps.setInt(12, Integer.parseInt(datos.get("contratoConvenio").toString()));
			} else {
				ps.setNull(12, Types.VARCHAR);
			}
			
			if (ps.executeUpdate()>0)
				return true;
			
		}catch (SQLException se) {
			Log4JManager.error(se);
			
		}catch (Exception e) {
			Log4JManager.error(e);
			
		}finally {
			try{
				if(ps != null){
					ps.close();
				}
		}
			catch (SQLException se) {
				logger.error("Error close PreparedStatement ", se);
			}
		}

		return false;
	}
	
	
	
	
	/**
	 * Metodo encargado de verificar
	 * si la hoja Qx esta finalizada o no.
	 * @param connection
	 * @param solicitud
	 * @return (S/N)
	 */
	public static String estaFinalizadaHojaQx (Connection connection,String solicitud)
	{
			logger.info("ENTRO A CONSULTARFINALIZADOHOJAQX "+solicitud);
		String cadena = strVerificaSiHojaQxFinalizada;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//numero de la solicitud
			ps.setInt(1, Utilidades.convertirAEntero(solicitud));
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			return rs.getString(1);
		}
		catch (Exception e) {
		}
		
		
		return "";
	}
	
	
	
	
	/**
	 * mETODO ENCARGADO DE CONSULTAR EL NOMBRE DEL ASOCIO
	 */
	public static String getNombreAsocio (Connection connection,String codigo)
	{
		logger.info("\n getNombreAsocio -->"+codigo);
		String cadena = strNombreAsocio;
		
		try 
		{
			int tipoBD = DaoFactory.getConstanteTipoBD(System.getProperty("TIPOBD"));
			if(tipoBD==DaoFactory.ORACLE)
			{
				cadena=cadena+" from dual ";
			}
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//codigo asocio
			ps.setInt(1,Utilidades.convertirAEntero(codigo));
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			return rs.getString(1);
			
				
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultando el nombre del asocio "+e);
		}
		
		
		return "";
	}
	
	
	
	
	/**
	 * Metodo encargado de identificar si existe hoja
	 * de anestecia para una solicitud.
	 */
	public static String existeHojaAnestesia (Connection connection,String solicitud)
	{
		String cadena = strVerificaExistenciaHojaAnestesia;
		int result=0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps =  connection.prepareStatement(cadena);
			//numero solicitud
			ps.setInt(1,Utilidades.convertirAEntero(solicitud));
			
			rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				result=rs.getInt("contador");
			
			if(result>0)
				return ConstantesBD.acronimoSi;
			else
				return ConstantesBD.acronimoNo;
			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultando la existencia de la hoja de anestesia "+e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		
		
		return ConstantesBD.acronimoNo;
	}
	
	
	/**
	 * Metodo encargado de devolver un arrayList
	 * con la informacion de finalidad por servicio
	 * @param connection 
	 * @param criterios
	 * ----------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ----------------------------
	 * -- naturaleza
	 * -- institucion
	 * @return Arraylist<HashMap<>>
	 * ---------------------------------------
	 * KEY'S DEL HASHMAP DENTRO DEL ARRAYLIST
	 * ---------------------------------------
	 * -- codigo
	 * -- finalidad
	 * 
	 */
	public static ArrayList< HashMap<String, Object>>  consultarFinalidadServicio (Connection connection,HashMap criterios)
	{
		logger.info("\n ********ENTRO A CONSULTAR GIAGNOSTICOS FINALIDAD DEL SERVICIO  ***** ==> "+criterios);
		ArrayList result = new ArrayList ();
		String cadena=strConsultarFinalidadServicio;
		logger.info("\n cosnulta -->"+cadena);
		PreparedStatementDecorator ps =null;
		ResultSetDecorator rs  =null;
		try
		{
			ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet , ConstantesBD.concurrencyResultSet ));
			//naturaleza
			ps.setString(1, criterios.get("naturaleza")+"");
			//institucion
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("institucion")+""));
			
			rs = new ResultSetDecorator(ps.executeQuery());
			
			while (rs.next())
			{
				HashMap datos = new HashMap ();
				datos.put("codigo", rs.getObject("codigo"));
				datos.put("finalidad", rs.getObject("finalidad"));
				result.add(datos);
			}
			
			
		}
		catch (SQLException e)
		{
			logger.error("\n problema consultando las finalidades del servicio "+e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		
		logger.info("\n al salir es --> "+result);
		return result;
	}
	
	
	
	/**
	 * Metodo encargado de consultar
	 * los diagnosticos postoperatorios
	 * @param connection
	 * @param criterios
	 * ----------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ----------------------------------
	 * -- codigoSolCXSer
	 * 
	 * @return HashMap
	 * ----------------------------------
	 * KEY'S DEL HASHMAP QUE DEVUELVE
	 * ----------------------------------
	 * 
	 */
	public static HashMap consultarDiagnosticosPostOpera (Connection connection,HashMap criterios)
	{
		logger.info("\n ********ENTRO A CONSULTAR GIAGNOSTICOS POSTOPERATORIOS ***** ==> "+criterios);
		HashMap mapa = new HashMap ();
		String cadena= strCosultarDiagPostOpera;
		logger.info("\n consulta --> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//cod_sol_cx_servicio
			ps.setInt(1,Utilidades.convertirAEntero(criterios.get("codigoSolCXSer")+""));
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
		}
		catch (SQLException  e) 
		{
			logger.error("\n problema consultando los diagnosticos post operatorios del servicio "+e);
		}
		
		mapa.put("INDICES_MAPA",indicesDiagPostOpera );
		return mapa;
	}
	
	
	
	
	
	
	/**
	 * Metodo encargado de consultar los profesionales
	 * de de los servicios de cada cirugia en la tabla
	 * profesionales_cirugia
	 * @param connection
	 * @param criterios
	 * ---------------------------
	 * KEY'S DEL MAPA CRITERIOS	
	 * ---------------------------
	 * -- codigoSolCXSer
	 * @return Hashmap
	 * --------------------------------
	 * KEY'S DE LOS MAPAS QUE RETORNA
	 * --------------------------------
	 * -- consecutivo0_
	 * -- tipoAsocio1_
	 * -- especialidad2_
	 * -- codigoProfecional3_
	 * -- cobrable4_
	 * -- pool5_
	 * -- tipoEspecialista6_
	 * -- estaBd7_
	 */
	public static HashMap consultarProfecionalesCx (Connection connection,HashMap criterios)
	{
		//logger.info("\n ********ENTRO A CONSULTAR PROFECIONALES ***** ==> "+criterios);
		HashMap mapa = new HashMap ();
		String cadena= strConsultarProfesionalesCx;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//cod_sol_cx_servicio
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get("codigoSolCXSer")+""));
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
		}
		catch (SQLException  e) 
		{
			logger.error("\n problema consultando los profecionales del servicio "+e);
		}
		
		mapa.put("INDICES_MAPA",indicesProfesionales );
		return mapa;
	}
	
		
	
	
	/**
	 * Metodo encargado de consultar los servicios
	 * de una solicitud
	 * @param connection
	 * @param datos
	 * ---------------------
	 * KEY'S DEL MAPA DATOS
	 * ---------------------
	 * -- solicitud --> Requerido
	 * -- institucion --> Requerido
	 * -- codigoMedico --> Opcional
	 * -- funcionalidad --> Opcional
	 */
	public static HashMap consultarServicios (Connection connection,HashMap datos)
	{
		logger.info("\n ********ENTRO A CONSULTAR SERVICIOS ***** ==> "+datos);
		HashMap mapa = new HashMap ();
		String cadena = strConsultarServiciosSolicitud;
		
		try 
		{
			logger.info("consultarServicios-->"+cadena+"    -->"+datos.get("solicitud"));
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//solicitud
			ps.setInt(1,Utilidades.convertirAEntero(datos.get("solicitud")+""));
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			int numRegistros=Utilidades.convertirAEntero(mapa.get("numRegistros")+"");
			if (mapa.containsKey("numRegistros") && numRegistros>0)
			{
				//se le ingresa el total de servicios
				//totSev26
				mapa.put(indicesServicios[26], numRegistros);
				
				//se pregunta si existe hoja de anestesia para la solicitud
				mapa.put(indicesServicios[20], existeHojaAnestesia(connection, datos.get("solicitud")+""));		
				
				/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++****************************************
				 * *******************************************************************************************************************
				 * Aqui se hace la validacion para cargar la especialidad que interviene
				 * *******************************************************************************************************************
				 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++************************************/
				ArrayList<HashMap<String, Object>> especialidades = new ArrayList<HashMap<String,Object>>();
						
				//si existe la hoja de anestesia entonces entra al if
				if ((mapa.get(indicesServicios[20])+"").equals(ConstantesBD.acronimoSi))
				{						
					//se existen registros de especialidades en la tabla esp_intervienen_sol_cx
					especialidades=UtilidadesSalas.obtenerEspecialidadesIntervienen(connection, datos.get("solicitud")+"", "");
					//se pregunta si esta busqueda arrojo resultados
					if (especialidades.size()>0)
						mapa.put(indicesServicios[21], especialidades);
					else
						mapa.put(indicesServicios[21], Utilidades.obtenerEspecialidadesEnArray(connection, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido));
				}
				else
					mapa.put(indicesServicios[21], Utilidades.obtenerEspecialidadesEnArray(connection, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido));
						
				
				/*******************************************************************************
				 * Cambio por anexo 728
				 * se evalua si la especialidad que tiene
				 * el servicio la tiene el medico, de ser asi
				 * se postula esta especialidad.
				 * Este cambio solo apliaca cuando la hoja quirurgica
				 * se llama desde la Respuesta de Procedimientos
				 */
					if ((datos.get("funcionalidad")+"").equals("RespProce"))
					{
						HashMap especialidadesMedico=Utilidades.obtenerEspecialidadesMedico(connection, datos.get("codigoMedico")+"");
						int numReg = Utilidades.convertirAEntero(especialidadesMedico.get("numRegistros")+"");
						for (int j=0;j<numRegistros;j++)
						{
							//logger.info("\n \n entre a verificar la  especialidad ---"+mapa.get(indicesServicios[37]+j));
							if (numReg>1)
							{
								for (int i=0;i<numReg;i++)
								{
									//logger.info("\n \n especialidad medico -->"+especialidadesMedico.get("codigo_"+i)+" ---especialidad servicio  -->"+mapa.get(indicesServicios[37]+j));
									if ((especialidadesMedico.get("codigo_"+i)+"").equals(mapa.get(indicesServicios[37]+j)+""))
									{
										mapa.put(indicesServicios[19]+j, especialidadesMedico.get("codigo_"+i));
										//logger.info("\n\n LA ESPECIALIDAD A POSTULAR ES --> "+especialidadesMedico.get("codigo_"+i));
									}
								}
							}
							else
								if (numReg==1)
									mapa.put(indicesServicios[19]+j, especialidadesMedico.get("codigo_0"));
						}
					}
				/*
				 * 
				 *******************************************************************************/
				
					/**************************************************************************************************
					 * Cambio por anexo 728 
					 * solo aplica cuando se llama la hoja quirurgica desde la Respuesta
					 * de procedimientos
					 */
					if ((datos.get("funcionalidad")+"").equals("RespProce"))
					//se postula el tipo de honorario cirujano
						for (int j=0;j<numRegistros;j++)
							mapa.put(indicesServicios[10]+j+"_"+indicesProfesionales[12]+"0",ValoresPorDefecto.getAsocioCirujano(Utilidades.convertirAEntero(datos.get("institucion")+"")));
					/*
					 * 
					 *************************************************************************************************/
					
					
				/*********************************************************************************************************************		
				//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		
				**********************************************************************************************************************/					
				
						
				for (int i=0;i<Integer.parseInt(mapa.get("numRegistros")+"");i++)
				{
					HashMap criterios = new HashMap ();
					criterios.put("codigoSolCXSer", mapa.get(indicesServicios[9]+i));
					//se consultan los profecionales para ese codigo de sol_cirugia_por_servicio
					Listado.copyMapIndexWithNewIndex(consultarProfecionalesCx(connection, criterios), indicesProfesionales, mapa, indicesServicios[10]+i);
				
				
					//se consultan los diagnosticos postoperatorios para ese codigo de sol_cirugia_por_servicio
					
					HojaQuirurgica.convertirFormatoAAPDiagnosticosPostOpe(consultarDiagnosticosPostOpera(connection, criterios),mapa,i);
					//logger.info("\n diagnosticos --> "+Listado.obtenerMapaInterno(mapa, indicesServicios[11]+i+"_"));
					//se organizan los criterios de busqueda para la finalidad
						criterios.put("naturaleza", mapa.get(indicesServicios[14]+i));
						criterios.put("institucion", datos.get("institucion"));
					//se consulta y almacena en el hashmap
						//finalidad
					mapa.put(indicesServicios[15]+i,consultarFinalidadServicio(connection, criterios));
					
					//se obtiene la descripcionQx en el historico
					mapa.put(indicesServicios[23]+i, consultarDescripcionesQx(connection, mapa.get(indicesServicios[9]+i)+""));
					
					//descripcion Qx para ingresar
					mapa.put(indicesServicios[24]+i, "");
					
				}
			}
			else
			{
				//se pregunta si existe hoja de anestesia para la solicitud
				mapa.put(indicesServicios[20], existeHojaAnestesia(connection, datos.get("solicitud")+""));
				
				/**++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++****************************************
				 * *******************************************************************************************************************
				 * Aqui se hace la validacion para cargar la especialidad que interviene
				 * *******************************************************************************************************************
				 +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++************************************/
				ArrayList<HashMap<String, Object>> especialidades = new ArrayList<HashMap<String,Object>>();
						
				//si existe la hoja de anestesia entonces entra al if
				if ((mapa.get(indicesServicios[20])+"").equals(ConstantesBD.acronimoSi))
				{						
					//se existen registros de especialidades en la tabla esp_intervienen_sol_cx
					especialidades=UtilidadesSalas.obtenerEspecialidadesIntervienen(connection, datos.get("solicitud")+"", "");
					//se pregunta si esta busqueda arrojo resultados
					if (especialidades.size()>0)
						mapa.put(indicesServicios[21], especialidades);
					else
						mapa.put(indicesServicios[21], Utilidades.obtenerEspecialidadesEnArray(connection, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido));
				}
				else
					mapa.put(indicesServicios[21], Utilidades.obtenerEspecialidadesEnArray(connection, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido));
						
				/*********************************************************************************************************************		
				//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		
				**********************************************************************************************************************/
				
				
			}
			
		}
		catch (SQLException e) 
		{
			logger.error("\n problema consultando servicios de la solicitud "+e);
		}
		
		mapa.put("INDICES_MAPA", indicesServicios);
		return mapa;
	}
	
	
	
	
	
	/**
	 * Metodo encargado de eliminar 
	 * un diagnostico preoperatorio
	 * @param connection
	 * @param datos
	 * ------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------
	 * -- codigo --> Requerido
	 */
	public static boolean eliminarDiagnostioPreoperatorio (Connection connection, HashMap datos)
	{
		String cadena=strEliminarDiagnosticoPreoperatorio;
		logger.info("\n ********ENTRO A ELIMINAR DIAGNOSTICO PREOPERATORIO ***** ==> "+datos);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//codigo
			ps.setInt(1,Utilidades.convertirAEntero(datos.get("codigo")+""));
			if (ps.executeUpdate()>0)
				return true;
		}
		catch (Exception e)
		{
			logger.error("\n problema eliminando el registro de diagnosticos preoperatorios "+e);
		}
		
		return false;
	}
	
	/**
	 * Metodo encargado de modificar los diagnosticos
	 * preoperatorios 
	 * @param connection
	 * @param datos
	 * -----------------------
	 * KEY'S DE MAPA DATOS
	 * -----------------------
	 * -- diagnostico --> Requerido
	 * -- tipoCie --> Requerido
	 * -- principal --> Requerido
	 * -- codigo --> Requerido
	 * @return
	 */
	public static boolean modificarDiagnosticoPreoperatirio (Connection connection, HashMap datos)
	{
		String cadena = strModificarDiagnosticoPreoperatorio;
		
		logger.info("\n ********ENTRO A MODIFICAR DIGANOSTICO PREOPERATORIO ***** ==> "+datos);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			// diagnostico
			ps.setString(1, datos.get("diagnostico")+"");
			//tipo cie
			ps.setInt(2,Utilidades.convertirAEntero(datos.get("tipoCie")+""));
			//principal
			ps.setBoolean(3,UtilidadTexto.getBoolean(datos.get("principal")+""));
			//codigo
			ps.setInt(4, Utilidades.convertirAEntero(datos.get("codigo")+""));
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			logger.error("\n problema modificando el registro de diagnosticos preoperatorios "+e);
		}
		
		//diagnostico=?, tipo_cie=?, principal=? WHERE codigo=?";
		
		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Metodo encargado de verificar si existe
	 * la hoja quirurgica.
	 * @param connection
	 * @param solicitud
	 */
	public static boolean existeHojaQx (Connection connection, String solicitud)
	{
		logger.info("\n ********ENTRO A CONSULTAR EXISTENCIA  ***** ==> "+solicitud);
		String cadena = strConsultaExistenciaHojaQx;
		int result=0;
		PreparedStatement ps =null;
		ResultSet rs= null;
		try 
		{
			ps =  connection.prepareStatement(cadena);
			//solicitud
			ps.setInt(1, Utilidades.convertirAEntero(solicitud));
			logger.info("--->"+cadena+"--->"+Utilidades.convertirAEntero(solicitud));
			
			rs = new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				result=rs.getInt("cantidad");
			
			if(result>0)
				return true;
			else
				return false;
			
		}
		catch (SQLException e) 
		{
			logger.error("\n problema verificando existencia de la hoja quirurgica "+e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		
		return false;
	}
	
	
	
	
	/**
	 * Metodo encargado de actualizar el 
	 * numero de autorizacion de la solicitud
	 * @param connection
	 * @param datos
	 * -------------------------
	 * KEY'S DEL MAPA DATOS
	 * -------------------------
	 * -- autorizacion --> Requerido
	 * -- solicitud --> Requerido
	 */
	public static boolean actualizarAutorizacion (Connection connection, HashMap datos)
	{
		logger.info("\n ********ENTRO A ACTUALIZAR AUTORIZACION ***** ==> "+datos);
		String cadena = strActualizarAutorizacion;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//numero autorizacion
			ps.setString(1, datos.get("autorizacion")+"");
			//solicitud
			ps.setInt(2,Utilidades.convertirAEntero(datos.get("solicitud")+""));
			
			if (ps.executeUpdate()>0)
				return  true;
			
		}
		catch (SQLException e)
		{
		 logger.info("\n problema actualizando el numero de la actualizacion "+e);
		}
		return false;
	}
	
	
	
	
	/**
	 * Metod encargado de insertar los datos basicos 
	 * en la hoja quirurgica
	 * @param connection
	 * @param datos
	 * -----------------------------------------
	 * 			KEY'S DEL MAPA DATOS
	 * -----------------------------------------
	 * -- numSolicitud --> Requerido
	 * -- finalizada --> Requerio
	 * -- datosMedico --> Requerido
	 * -- medicoFinaliza --> No Requerido
	 * 
	 */
	public static boolean insertarHojaQxBasica (Connection connection,HashMap datos)
	{
		
		String cadena = strIngresarHojaQxBasica;
		logger.info("\n ********ENTRO A INSERTAR HOJA QX BASICA ***** ==> "+datos);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet , ConstantesBD.concurrencyResultSet ));	
			//numero solicitud
			ps.setInt(1, Utilidades.convertirAEntero(datos.get("numSolicitud")+""));
			
			//finalizada
			ps.setBoolean(2,UtilidadTexto.getBoolean(datos.get("finalizada")+""));
			
			//datos medico
			if(datos.get("datosMedico").toString().length() > 254)
				ps.setString(3, datos.get("datosMedico").toString().substring(0,253));
			else				
				ps.setString(3, datos.get("datosMedico")+"");
			
			//medico finaliza
			if(datos.get("medicoFinaliza")!=null&&!datos.get("medicoFinaliza").toString().equals(""))
				ps.setInt(4, Utilidades.convertirAEntero(datos.get("medicoFinaliza")+""));
			else
				ps.setNull(4,Types.INTEGER);
			
			/**MT 3911 -Diana Ruiz */			
			//Anestesiologo
			if (datos.get("anestesiologo")!=null&&!datos.get("anestesiologo").toString().equals(""))
				ps.setInt(5, Utilidades.convertirAEntero(datos.get("anestesiologo")+""));
			else
				ps.setNull(5,Types.INTEGER);			
			//validacion capitacion
			ps.setString(6,datos.get("validacionCapitacion")+"");
				
			if (ps.executeUpdate()>0)
				return true;

		}
		catch (Exception e) 
		{
			logger.info("\n problema insertando los daos en la tabla hoja_quirurgica "+e);
		}
		
		
		return false;
	}
	
	
	
	
	
	/**
	 * Metodo encargado de consultar los diagnosticos
	 * de una solicitud.
	 * @param connection
	 * @param criterios
	 * --------------------------------
	 * 	  KEY'S DEL MAPA CRITERIOS 
	 * --------------------------------
	 * -- solicitud --> Requerido
	 * 
	 * @return mapa
	 * -- codigo0_
	 * -- numSolicitud1_
	 * -- diagnostico2_
	 * -- tipoCie3_
	 * -- principal4_
	 * -- nomDiagnostico5_
	 * -- estaBd6_
	 */
	public static HashMap consultaDiagnosticos (Connection connection, HashMap criterios)
	{
		logger.info("\n ********ENTRO A CONSULTAR DIAGNOSTICOS ***** ==> "+criterios);
		HashMap mapa = new HashMap ();
		String cadena = strConsultaDiagnosticos;
		
		try 
		{
			logger.info("\n CADENA CONSULTA ==> "+cadena);
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//solicitud
			ps.setInt(1,Utilidades.convertirAEntero(criterios.get("solicitud")+""));
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
		}
		catch (SQLException e) 
		{
			logger.info("\n problema consultando los diagnosticos "+e);
		}
		mapa.put("INDICES_MAPA", indicesDiagnosticos);
		
		return mapa;
	}
	
	
	
	
	
	
	
	/**
	 * Metodo encargado de consultar los datos de la
	 * solicitud.
	 * @param connection
	 * @param criterios
	 * -----------------------------------
	 * 		KEY'S DEL MAPA CRITERIOS
	 * -----------------------------------
	 * -- Peticion  --> Opcional(requerido solicitud)
	 * -- Solicitud --> Opcional(requerido peticion)
	 * @return mapa
	 * -----------------------------------
	 * KEY'S DEL MAPA QUE RETORNA
	 * -----------------------------------
	 * -- ccSolicitado0
	 * -- numAutorizacion1
	 * -- fechaSolicitud2
	 * -- horaSolicitud3
	 * -- especialidad4
	 * -- urgente5
	 * -- nomCcSolicitado6
	 * -- nomEspecialidad7
	 * -- numeroSolicitud8
	 * -- tipoPaciente9
	 * -- nomTipoPaciente10
	 * -- codigoSolicitante11
	 * -- solicitante12
	 * -- requiereUci13
	 *	
	 */
	public static HashMap consultaSolicitud (Connection connection,HashMap criterios)
	{
		logger.info("\n entro a consultaSolicitud -->"+criterios);
		HashMap mapa = new HashMap();
		String cadena = strConsultaDatosSolicitud;
				
		if(criterios.containsKey("peticion") && 
				!(criterios.get("peticion")+"").equals("") && !(criterios.get("peticion")+"").equals("null"))
			cadena+=" AND p.codigo = "+criterios.get("peticion").toString();
		
		if(criterios.containsKey("solicitud") && 
				!criterios.get("solicitud").equals(""))
			cadena+=" AND s.numero_solicitud = "+criterios.get("solicitud").toString();
			
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);			
		}
		catch (SQLException e) 
		{
			logger.error("\n problema consultando los datos de la solicitud "+cadena);
		}
		
		mapa.put("INDICES_MAPA", indicesSolicitudes);
		return mapa;
		
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * Metodo encargado de actualizar la subcuenta
	 * de la tabla solicitudes_cirugia cuando se 
	 * cambian los el servicio de consecutivo 1
	 * en la solicitud de cirugia.
	 */
	public static boolean actualizarSubCuenta (Connection connection,double subCuenta,String solicitud)
	{
		logger.info("\n ****** ENTRO A ACTUALIZAR SUBCUENTA ******* solicitud-->"+solicitud+"  subCuenta -->"+subCuenta);
		String cadena = strCambiaSubCuentaSolicitudesQx;
		try 
		{
			logger.info("\n la cadena es --> "+cadena);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			ps.setLong(1, (long)subCuenta);
			//solicitud
			ps.setInt(2, Utilidades.convertirAEntero(solicitud));
			
			if (ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			logger.error("\n problema actualizando la subCuenta de la tabla solicitudes_cirugia "+e);
		}
			
		return false;
	}
	
	
	
	/**
	 * Metodo encargado Consulta el listado de peticiones
	 * que no tengan solicitud asociada, de ser asi
	 * se debe validar que la solicitud corresponda
	 * a la cuenta cargada y que se encuentren
	 * en estado diferente de ATENDIDO o CANSELADA,
	 * ademas consulta todos los servicios de cada
	 * peticion.
	 * @param connection
	 * @param criterios
	 * -----------------------------------------------
	 * 	KEY'S DEL MAPA CRITERIOS
	 * -----------------------------------------------
	 * --paciente --> Requerido
	 * --institucion --> Requerido
	 * --cuenta --> Requerido
	 * @return mapa
	 * ------------------------------------------------
	 * 			KEY'S DEL MAPA DE RESULTADO
	 * ------------------------------------------------
	 * -- cuenta0_
	 * -- paciente1_
	 * -- codigoPeticion2_
	 * -- fechaCirugia3_
	 * -- consecutivoOrdenes4_
	 * -- estadoMedico5_
	 * -- numeroSolicitud6_
	 * -- solicitante7_
	 * -- especialidad8_
	 * -- servicios9_ --> Este es un HashMap por tanto tiene otras llaves
	 * -- esAsociado10_
	 * ----------------------------------------------------------------------------------
	 * KEY'S DEL HASHMAP SERVICIOS9_ QUE LLEVA EL LISTADO DE SERVICIOS DE CADA PETICION
	 * ----------------------------------------------------------------------------------
	 * -- codigoServicio0_
	 * -- codCups1_
	 * -- servicio2_
	 * -- especialidad3_
	 */
	@Deprecated
	public static HashMap consultarPeticiones (Connection connection, HashMap criterios)
	{
		logger.info("\n estoy en consultarPeticiones SQL "+criterios);
		HashMap mapa = new HashMap ();
		String cadena;
		String cadena2= strConsultarServiciosPeticion;
		String cadena3= strConsultarServiciosSolicitud;
		@SuppressWarnings("unused")
		String serviciosCodigo = "";
		
		//Evalua si se filtra por el codigo de la petición
		if(criterios.containsKey("codigoPeticion") && 
				!criterios.get("codigoPeticion").toString().equals(""))
		{
			 cadena = strConsultaPeticiones+" WHERE codigo_peticion2 ="+criterios.get("codigoPeticion")+" ORDER BY fecha_cirugia3 ASC ";
		}
		else 
		{
			cadena = strConsultaPeticiones+" ORDER BY fecha_cirugia3 ASC ";
		}
	
		try
		{
			
			logger.info("\n consulta ==> "+cadena);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//paciente
			ps.setInt(1,Utilidades.convertirAEntero(criterios.get("paciente")+""));
			//institucion
			ps.setInt(2,Utilidades.convertirAEntero(criterios.get("institucion")+""));
			//ingreso
			ps.setInt(3,Utilidades.convertirAEntero(criterios.get("ingreso")+""));
			
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			//se consulta si existen peticiones, de ser asi se buscan los servicios
			if (Integer.parseInt(mapa.get("numRegistros")+"")>0)
			{
				for (int i=0;i<Integer.parseInt(mapa.get("numRegistros")+"");i++)
				{
					HashMap servicios = new HashMap ();
					
					if (mapa.get((indicesPeticiones[10]+i)+"").equals(ConstantesBD.acronimoNo))
					{
						try 
						{
							logger.info("\n consulta 2 ==> "+cadena2);
							logger.info("\n codigo peticion  ==> "+mapa.get(indicesPeticiones[2]+i));
							PreparedStatementDecorator pss =  new PreparedStatementDecorator(connection.prepareStatement(cadena2, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
							//codigo de la peticion
							pss.setInt(1, Utilidades.convertirAEntero(mapa.get(indicesPeticiones[2]+i)+""));
							
							servicios=UtilidadBD.cargarValueObject(new ResultSetDecorator(pss.executeQuery()),true,true);
							
						//logger.info("\n el valor de los servicios es "+servicios);
						} catch (SQLException e) {
							logger.error("\n problema consultando los servicios de las peticiones "+e);
						}
					}
					else
					{
						try 
						{
							
							logger.info("\n consulta 3 ==> "+cadena3);
							logger.info("\n codigo solicitud  ==> "+mapa.get(indicesPeticiones[6]+i));
							PreparedStatementDecorator pss =  new PreparedStatementDecorator(connection.prepareStatement(cadena3, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
							//codigo de la solicitud
							pss.setInt(1, Utilidades.convertirAEntero(mapa.get(indicesPeticiones[6]+i)+""));
							
							servicios=UtilidadBD.cargarValueObject(new ResultSetDecorator(pss.executeQuery()),true,true);
							
						} catch (SQLException e) {
							logger.error("\n problema consultando los servicios de las peticiones "+e);
						}
					}
					
					
					//se agrega los indices de servicios
					servicios.put("INDICES_MAPA", indicesServicios);
					//se agregan los servicios al HashMap General
					mapa.put("servicios9_"+i, servicios);
				}
			}
			
		}
		catch (SQLException e) 
		{
			logger.error("\n problema consultando las peticiones de cirugias "+e);
			e.printStackTrace();
		}
		
		mapa.put("INDICES_MAPA", indicesPeticiones);
		logger.info("\n\n el valor al final  es "+mapa);
		return mapa;
		
	}
	
	/**
	 * Metodo encargado de insertar los datos
	 * de diagnosticos preoperativos.
	 * @param connection
	 * @param datos
	 * -------------------------------
	 * 		KEY'S DEL MAPA DATOS
	 * -------------------------------
	 * -- solicitud
	 * -- diagnostico
	 * -- tipoCie
	 * -- principal
	 * @return
	 */
	public static boolean insertarDiacnosticosPreoperatorios (Connection connection, HashMap datos)
	{
		logger.info("\n ******** ENTRO A INSERTAR DIAG PREO ---> "+datos);
		String cadena = stringresarDiagnosticoPreoperatorio;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//codigo
			ps.setInt(1, DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).incrementarValorSecuencia(connection, "seq_dia_pre_op_solcx"));
			//numero solicitud
			ps.setInt(2, Utilidades.convertirAEntero(datos.get("solicitud")+""));
			//diagnostico
			ps.setString(3, datos.get("diagnostico")+"");
			//tipo cie
			ps.setInt(4, Utilidades.convertirAEntero(datos.get("tipoCie")+""));
			//principal
			ps.setBoolean(5, UtilidadTexto.getBoolean(datos.get("principal")+""));
			
			if (ps.executeUpdate()>0)
				return true;
			
			
		} 
		catch (SQLException e)
		{
			logger.info("\n problema insertando datos en la tabla diag_preoperatorio_cx "+e);
		}

		return false;
	}
	
	
	/**
	 * Método para cargar los diagnósticos de un servicio
	 * @param con
	 * @param numeroServicio
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap cargarDiagnosticosPorServicio(Connection con, int numeroServicio, int numeroSolicitud)
	{
		try
		{
			PreparedStatementDecorator statement;
			if(numeroSolicitud!=0)
			{
				statement= new PreparedStatementDecorator(con.prepareStatement(cargarDiagnosticosPreoperatoriosRelacionadosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				//numero solicitud
				statement.setInt(1, numeroSolicitud);
				
				HashMap mapaTemporal=UtilidadBD.cargarValueObject(new ResultSetDecorator(statement.executeQuery()), true, true);
				if(mapaTemporal==null)
				{
					logger.error("Error cargando los diagnosticos preoperatorios");
					return null;
				}
				if(mapaTemporal.size()>1)
				{
					return mapaTemporal;
				}
				
				String validarEvoluciones="SELECT max(codigo) FROM evoluciones WHERE valoracion=?";
				statement= new PreparedStatementDecorator(con.prepareStatement(validarEvoluciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				statement.setInt(1, numeroSolicitud);
				ResultSetDecorator resultado=new ResultSetDecorator(statement.executeQuery());
				if(resultado.next())
				{
					int codigoEvolucion=resultado.getInt(1);
					if(codigoEvolucion!=0)
					{
						String cargarDesdeEvoluciones="SELECT 0 AS codigo, e.acronimo_diagnostico AS diagnostico, e.tipo_cie_diagnostico AS tipo_cie, d.nombre AS nombre_diagnostico, e.principal AS principal FROM diagnosticos d INNER JOIN evol_diagnosticos e ON(e.acronimo_diagnostico=d.acronimo AND e.tipo_cie_diagnostico=d.tipo_cie) WHERE e.evolucion=?";
						statement= new PreparedStatementDecorator(con.prepareStatement(cargarDesdeEvoluciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						statement.setInt(1, codigoEvolucion);
						mapaTemporal=UtilidadBD.cargarValueObject(new ResultSetDecorator(statement.executeQuery()), true, true);
						if(mapaTemporal==null)
						{
							logger.error("Error cargando los diagnosticos preoperatorios");
							return null;
						}
						return mapaTemporal;
					}
				}

				String validarValoraciones="SELECT 0 AS codigo, v.acronimo_diagnostico AS diagnostico, v.tipo_cie_diagnostico AS tipo_cie, d.nombre AS nombre_diagnostico, v.principal as principal FROM diagnosticos d INNER JOIN val_diagnosticos v ON(v.acronimo_diagnostico=d.acronimo AND v.tipo_cie_diagnostico=d.tipo_cie) WHERE v.valoracion=? AND v.definitivo="+ValoresPorDefecto.getValorTrueParaConsultas();
				statement= new PreparedStatementDecorator(con.prepareStatement(validarValoraciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				statement.setInt(1, numeroSolicitud);
				mapaTemporal=UtilidadBD.cargarValueObject(new ResultSetDecorator(statement.executeQuery()), true, true);
				if(mapaTemporal==null)
				{
					logger.error("Error cargando los diagnosticos preoperatorios");
					return null;
				}
				return mapaTemporal;

				
			}
			else if(numeroServicio!=0)
			{
				statement= new PreparedStatementDecorator(con.prepareStatement(cargarDiagnosticosServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				statement.setInt(1, numeroServicio);
				HashMap mapaTemporal=UtilidadBD.cargarValueObject(new ResultSetDecorator(statement.executeQuery()), true, true);
				if(mapaTemporal==null)
				{
					logger.error("Error cargando los diagnosticos postoperatorios");
					return null;
				}
				return mapaTemporal;
			}
			logger.error("No se especificó valor de solicitud o servicio");
			return null;
		}
		catch (SQLException e)
		{
			logger.error("Error cargando los diagnósticos de la hoja quirurgica : "+e);
			return null;
		}
	}
	
	
	/**
     * Método implementado para insertar un profesional de la cirugía
     * @param con
     * @param profesional
     * @return
     */
    public static int insertarProfesionalCirugia(Connection con,DtoProfesionalesCirugia profesional,String usuario)
    {
    	logger.info("\n ENTRO A INSERTAR PROFESIONALES CX "+usuario);
    	logger.info("el dto tipo especialista --> "+profesional.getTipoEspecialista());
    	logger.info("el dto getCodSolCxServ --> "+profesional.getCodSolCxServ());
    	logger.info("el dto getCodigoAsocio --> "+profesional.getCodigoAsocio());
    	logger.info("el dto getCodigoEspecialidad --> "+profesional.getCodigoEspecialidad());
    	logger.info("el dto getCodigoProfesional --> "+profesional.getCodigoProfesional());
    	logger.info("el dto cobrable --> "+profesional.getCobrable());
    	
    	
    	int codigoPoolPostular=profesional.getCodigoPool();
    	
    	int resultado = 0;
    	try
    	{
    		
    		if(codigoPoolPostular<=0) 
        		codigoPoolPostular=obtenerCodigoPoolProfesionalPostular(con,profesional.getCodigoProfesional());
        	
    		
    		String consulta = "INSERT INTO profesionales_cirugia " +
    			"(consecutivo,cod_sol_cx_serv,tipo_asocio,especialidad,codigo_profesional,cobrable,pool,tipo_especialista," +
    			" usuario_modifica,fecha_modifica, hora_modifica, historia_clinica) values " +
    			"(?,?,?,?,?,?,?,?,?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?)";
    		int secuenciaProfesionales = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_profesionales_cirugia");
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
    		pst.setInt(1,secuenciaProfesionales);
    		logger.info("\n antesssssssssssss 1\n");
    		pst.setInt(2,Integer.parseInt(profesional.getCodSolCxServ()));
    		logger.info("\n antesssssssssssss 2\n");
    		pst.setInt(3,profesional.getCodigoAsocio());
    		pst.setInt(4,profesional.getCodigoEspecialidad());
    		pst.setInt(5,profesional.getCodigoProfesional());
    		pst.setString(6, profesional.getCobrable());
    		
    		if(codigoPoolPostular>0)
    			pst.setInt(7,codigoPoolPostular);
    		else
    		{
    			pst.setNull(7,Types.INTEGER);
    		}
    		
    		if(!profesional.getTipoEspecialista().equals(""))
    			pst.setString(8,profesional.getTipoEspecialista());
    		else
    			pst.setNull(8,Types.VARCHAR);
    		//usuario
    		pst.setString(9, usuario);
    		if(!profesional.getHistoriaClinica().equals(""))
    			pst.setString(10,profesional.getHistoriaClinica());
    		else
    			pst.setString(10,ConstantesBD.acronimoSi);
    		
    		resultado = pst.executeUpdate();
    		
    		if(resultado>0)
    			resultado = secuenciaProfesionales;
    	
    		logger.info("\n paseeeeeeeeeeeeeeeeeeee");
    	}
    	catch(SQLException e)
    	{
    		logger.error("Error en insertarProfesionalCirugia: "+e);
    	}
    	return resultado;
    }
    
    
    /**
     * @throws SQLException 
     * 
     */
    private static int obtenerCodigoPoolProfesionalPostular(Connection con,int codigoProfesional) throws SQLException  
    {
    	int resultado=ConstantesBD.codigoNuncaValido;
    	String consulta="SELECT count(1) from participaciones_pooles where medico="+codigoProfesional+" and (fecha_retiro > current_date or fecha_retiro is null)";
    	PreparedStatementDecorator ps=new PreparedStatementDecorator(con,consulta);
    	ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
    	if(rs.next())
    	{
    		if(rs.getInt(1)==1)
    		{
    			String consultaInterna="SELECT pool from participaciones_pooles where medico="+codigoProfesional+" and (fecha_retiro > current_date or fecha_retiro is null)";
    			PreparedStatementDecorator psInterna=new PreparedStatementDecorator(con,consultaInterna);
    	    	ResultSetDecorator rsInterna=new ResultSetDecorator(psInterna.executeQuery());
    	    	if(rsInterna.next())
    	    		resultado=rsInterna.getInt(1);
    	    	rsInterna.close();
    	    	psInterna.close();
    		}
    	}
    	rs.close();
    	ps.close();
		return resultado;
	}

	public static String consultarDescripcionesQx(Connection con,String consecutivoSolCx)
    {
    	String result="";
    	logger.info("\n ******************** entre a consultar descripsiones **************");
    	try
    	{
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strConsultaDescripcionesQx, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
    		//cod_sol_cx_serv
    		pst.setInt(1,Utilidades.convertirAEntero(consecutivoSolCx));
    		
    		ResultSetDecorator resultado=new ResultSetDecorator(pst.executeQuery());
			while(resultado.next())
			{
				String desc =  resultado.getString(2);
				String fecha = resultado.getString(4);
				String hora = resultado.getString(5);
				String usuario = resultado.getString(6);
				
				UsuarioBasico usu = new UsuarioBasico();
				usu.cargarUsuarioBasico(con, usuario);
				result += "Fecha:"+fecha+"  -----  Hora:"+hora+"\n"+desc+"\n"+"Usuario:"+usu.getInformacionGeneralPersonalSalud()+"\n\n";
			}
    		
    	}
    	catch(SQLException e)
    	{
    		logger.error("Error en consultarDescripcionesQx: "+e);
    	}
    	//logger.info("\n al salir es --> "+result);
    	return  result;
    }
    
    /**
     * Método que consulta las descripciones Qx de una cirugÃ­a
     * @param con
     * @param consecutivoSolCx
     * @return
     * ------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------
	 * codigo_, descripcion_, consecutivoServicio_, fechaGrabacion, horaGrabacion_, usuarioGrabacion_
     */
    public static HashMap consultarDescripcionesQx2(Connection con,String consecutivoSolCx)
    {
    	HashMap resultados = new HashMap();
    	try
    	{
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strConsultaDescripcionesQx, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
    		//cod_sol_cx_serv
    		pst.setInt(1,Utilidades.convertirAEntero(consecutivoSolCx));
    		
    		resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
    	}
    	catch(SQLException e)
    	{
    		logger.error("Error en consultarDescripcionesQx: "+e);
    	}
    	return resultados;
    }
    
    /**
     * Método que realiza la inserción de un diagnóstico postoperatorio
     * @param con
     * @param campos
     * @return
     */
    public static int insertarDiagnosticoPostOperatorio(Connection con,HashMap campos)
    {
    	logger.info("\n ENTRO A INSERTAR DIAGNOSTICOS POSTOPERATORIOS "+campos);
    	int resultado = 0;
    	try
    	{
    		String consulta = ingresarDiagnosticoPostoperatorio;
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
    		int secuenciaDiagPostOpera = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_dia_pos_op_solcx");
    		pst.setInt(1,secuenciaDiagPostOpera);
    		//cod_sol_cx_servicio
    		pst.setInt(2,Utilidades.convertirAEntero(campos.get("consecutivoSolCx")+""));
    		//diagnostico
    		pst.setString(3,campos.get("acronimoDiagnostico")+"");
    		//tipo cie
    		pst.setInt(4,Utilidades.convertirAEntero(campos.get("codigoTipoCie")+""));
    		pst.setBoolean(5,UtilidadTexto.getBoolean(campos.get("principal").toString()));
    		pst.setBoolean(6,UtilidadTexto.getBoolean(campos.get("complicacion").toString()));
    		pst.setString(7, campos.get("usuario")+"");
    		
    		resultado = pst.executeUpdate();
    		
    		if(resultado>0)
    			resultado = secuenciaDiagPostOpera;
    	}
    	catch(SQLException e)
    	{
    		logger.error("Error en insertarDiagnosticoPostOperatorio:"+e);
    	}
    	return resultado;
    }
    
    /**
     * Método para Insertar la descripcion qx de un servicio 
     * @param con
     * @param campos
     * @return
     */
    public static int insertarDescripcionQx(Connection con,HashMap campos)
    {
    	logger.info("\n ENTRO A INSERTAR DESCRIPCION QX SQL "+campos);
    	int resultado = 0;
    	try
    	{
    		String consulta = strInsertarDescripcionQx;
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
    		//codigo
    		int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_descripciones_qx_hq");
    		//descripcion
    		pst.setString(1,campos.get("descripcion")+"");
    		//consecutivoSolCx
    		pst.setInt(2,Utilidades.convertirAEntero(campos.get("consecutivoSolCx")+""));
    		pst.setString(3,campos.get("usuario")+"");
    		//codigo
    		pst.setInt(4,codigo);
    		
    		resultado = pst.executeUpdate();
    		
    		if(resultado>0)
    			return resultado;
    	}
    	catch(SQLException e)
    	{
    		logger.error("Error en insertarDescripcionQx: "+e);
    	}
    	
    	return resultado;
    }
    
    /**
     * Método que realiza la modificacion de un profesional de una cirugia
     * @param con
     * @param campos
     * @return
     */
    public static int modificarProfesionalCirugia(Connection con,DtoProfesionalesCirugia profesional,String usuario)
    {
    	int resultados = 0;
    	try
    	{
    		int codigoPool=profesional.getCodigoPool();
    		if(codigoPool<=0)
    			codigoPool=obtenerCodigoPoolProfesionalPostular(con, profesional.getCodigoProfesional());
    		
    		PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strModificarProfesionalCirugia, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
    		pst.setInt(1,profesional.getCodigoAsocio());
    		pst.setInt(2,profesional.getCodigoEspecialidad());
    		pst.setInt(3,profesional.getCodigoProfesional());
    		pst.setString(4,profesional.getCobrable());
    		if(codigoPool>0)
    			pst.setInt(5,codigoPool);
    		else
    			pst.setNull(5,Types.INTEGER);
    		if(!profesional.getTipoEspecialista().equals(""))
    			pst.setString(6,profesional.getTipoEspecialista());
    		else
    			pst.setNull(6,Types.VARCHAR);
    		
    		pst.setString(7,usuario);
    		
    		pst.setInt(8,Integer.parseInt(profesional.getCodigo()));
    		
    		resultados = pst.executeUpdate();
    	}
    	catch(SQLException e)
    	{
    		logger.error("Error en modificarProfesionalCirugia: "+e);
    	}
    	
    	return resultados;
    }
	
	
    /**
	 * Método usado para modificar la hoja quirúrgica
	 * @param con
	 * @param numeroSolicitud
	 * @param duracion
	 * @param sala
	 * @param tipoSala
	 * @param politrauma
	 * @param tipoHerida
	 * @param finalizada
	 * @param usuarioFinaliza
	 * @param fechaFinaliza
	 * @param horaFinaliza
	 * @param datosMedico
	 * @param estado
	 * @return
	 */
	public static int modificarTransaccional(
			Connection con,int numeroSolicitud,
			String duracion,int sala,int tipoSala,String politrauma,
			int tipoHerida,String finalizada,String usuarioFinaliza,
			String fechaFinaliza,String horaFinaliza,String datosMedico,String estado)
	{
		
		try
		{
			//se inicia transacción
			if(estado.equals(ConstantesBD.inicioTransaccion))
				UtilidadBD.iniciarTransaccion(con);
			
			String consulta = "UPDATE hoja_quirurgica SET ";
			boolean primero = true;
			
			
			
			//se verifica duracion final Cx
			if(!duracion.equals(""))
			{
				if(!primero)
					consulta += " , ";
				consulta += " duracion_final_cx = '"+duracion+"' ";
				primero = false;
			}
			
			//se verifica sala
			if(sala>0)
			{
				if(!primero)
					consulta += " , ";
				consulta += " sala = "+sala;
				primero = false;
			}
			
			//se verifica tipo sala
			if(tipoSala>0)
			{
				if(!primero)
					consulta += " , ";
				consulta += " tipo_sala = "+tipoSala;
				primero = false;
			}
			
			//se verifica politrauma
			if(!politrauma.equals(""))
			{
				if(!primero)
					consulta += " , ";
				if(UtilidadTexto.getBoolean(politrauma))
					consulta += " politrauma = "+ValoresPorDefecto.getValorTrueParaConsultas();
				else
					consulta += " politrauma = "+ValoresPorDefecto.getValorFalseParaConsultas();
				primero = false;
			}
			
			//se verifica tipo de herida
			if(tipoHerida>0)
			{
				if(!primero)
					consulta += " , ";
				consulta += " tipo_herida = "+tipoHerida;
				primero = false;
			}
			
			
			
			//se verifica finalizada
			if(!finalizada.equals(""))
			{
				if(!primero)
					consulta += " , ";
				if(UtilidadTexto.getBoolean(finalizada))
					consulta += " finalizada = "+ValoresPorDefecto.getValorTrueParaConsultas();
				else
					consulta += " finalizada = "+ValoresPorDefecto.getValorFalseParaConsultas();
				primero = false;
			}
			
			//se verifica usuario_finaliza
			if(!usuarioFinaliza.equals(""))
			{
				if(!primero)
					consulta += " , ";
				consulta += " medico_finaliza = '"+usuarioFinaliza+"' ";
				primero = false;
			}
			
			//se verifica fecha finalización
			if(!fechaFinaliza.equals(""))
			{
				if(!primero)
					consulta += " , ";
				consulta += " fecha_finaliza = '"+UtilidadFecha.conversionFormatoFechaABD(fechaFinaliza)+"' ";
				primero = false;
			}
			
			//se verifica hora finalización
			if(!horaFinaliza.equals(""))
			{
				if(!primero)
					consulta += " , ";
				consulta += " hora_finaliza = '"+horaFinaliza+"' ";
				primero = false;
			}
			
			//se verifica datos médico
			if(!datosMedico.equals(""))
			{
				if(!primero)
					consulta += " , ";
				consulta += " datos_medico = '"+datosMedico+"' ";
				primero = false;
			}
			
			consulta += " WHERE numero_solicitud = "+numeroSolicitud;
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			int resp = st.executeUpdate(consulta);
			
			//se finaliza transacción
			if(estado.equals(ConstantesBD.finTransaccion))
				UtilidadBD.finalizarTransaccion(con);
			
			if(resp<=0)
				UtilidadBD.abortarTransaccion(con);
			
			return resp;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en modificar de SqlBaseHojaQuirurgicaDao: "+e);
			UtilidadBD.abortarTransaccion(con);
			return -1;
		}
	}
    
	
	/**
	 * Método implementado para actualizar los datos de un profesional de acto quirurgico
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int actualizarProfesionalActoQx(Connection con,HashMap campos)
	{
		int resultado = 0;
		try
		{
			String consulta = "UPDATE profesionales_info_qx SET " +
				"fecha_modifica = current_date, " +
				"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
				"usuario_modifica = '"+campos.get("loginUsuario")+"' " ;
			
			if(Utilidades.convertirAEntero(campos.get("asocio").toString(), true)>0)
				consulta += " ,tipo_asocio = "+campos.get("asocio");
			
			if(Utilidades.convertirAEntero(campos.get("codigoProfesional").toString(), true)>0)
				consulta += " ,codigo_profesional = "+campos.get("codigoProfesional");
			
			if(!campos.get("cobrable").toString().trim().equals(""))
				consulta += " ,cobrable = '"+campos.get("cobrable")+"'";
				
			consulta += " WHERE consecutivo = "+campos.get("consecutivo");
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultado = st.executeUpdate(consulta);
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarProfesionalActoQx: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método que verifica si una orden de cirugia posee una sala que esta ocupada
	 * por otras ordenes
	 * @param con
	 * @param numeroSolicitud
	 * @param sala
	 * @param fechaInicial
	 * @param horaInicial
	 * @param fechaFinal
	 * @param horaFinal
	 * @return
	 */
	public static HashMap estaSalaOcupada(Connection con,int numeroSolicitud,int sala,
			String fechaInicial,String horaInicial,String fechaFinal,String horaFinal)
	{		
		HashMap result = new HashMap ();
		int cont=0;
		result.put("estaSalaOcupada", false);
		result.put("numRegistros", cont);
		try
		{
			//en caso de no encontrarse la sala se consulta a partir de la informacion de la solicitud			
			if(sala <= 0)
			{
				String consulta = "SELECT sala FROM hoja_quirurgica WHERE numero_solicitud = "+numeroSolicitud+" AND sala IS NOT NULL "+ValoresPorDefecto.getValorLimit1()+" 1";
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
					sala = Utilidades.convertirAEntero(rs.getString(1));
				
				if(sala <= 0)
					return result;
			}
			
			int resp=0;
			
			//conversion de fechas
			String FHI = UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"_"+horaInicial;
			String FHF = UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"_"+horaFinal;
			String consulta = estaSalaOcupada_Str +	" AND " +
						"(" +			
								"((sc.fecha_ingreso_sala || '_' || sc.hora_ingreso_sala) >= '"+FHI+"' AND (sc.fecha_ingreso_sala || '_' || sc.hora_ingreso_sala) <= '"+FHF+"') " +
							"OR " +		
								"((sc.fecha_salida_sala || '_' || sc.hora_salida_sala) >= '"+FHI+"' AND (sc.fecha_salida_sala || '_' || sc.hora_salida_sala) <= '"+FHF+"') " +
							"OR " +		
								"('"+FHI+"' >= (sc.fecha_ingreso_sala || '_' || sc.hora_ingreso_sala) AND '"+FHI+"' <= (sc.fecha_salida_sala || '_' || sc.hora_salida_sala)) " +
							"OR " +
								"('"+FHF+"' >= (sc.fecha_ingreso_sala || '_' || sc.hora_ingreso_sala) AND '"+FHF+"' <= (sc.fecha_salida_sala || '_' || sc.hora_salida_sala)) " +
						")  GROUP BY sc.fecha_ingreso_sala,sc.hora_ingreso_sala,sc.fecha_salida_sala,sc.hora_salida_sala " +
						"	ORDER BY sc.fecha_ingreso_sala,sc.hora_ingreso_sala" ;
					
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,sala);
			pst.setInt(2,numeroSolicitud);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			logger.info("\nvalor sql estaSalaOcupada >> "+consulta+" >> "+sala+" "+numeroSolicitud);
			
			while(rs.next())
			{
				resp=rs.getInt("resultado");
			
				if(resp>0)
				{
					
					result.put("estaSalaOcupada", true);
	
					//--------------------------------------------------------
					//Modificacion para organizar el mensaje
					result.put("fechaIngresoSala_"+cont, rs.getString("fechaIngresoSala"));
					result.put("horaIngresoSala_"+cont, rs.getString("horaIngresoSala")+"");
					result.put("fechaSalidaSala_"+cont, rs.getString("fechaSalidaSala")+"");
					result.put("horaSalidaSala_"+cont, rs.getString("horaSalidaSala")+"");
					//-------------------------------------------------------
				}
				
				
				cont++;
				result.put("numRegistros", cont);
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en estaSalaOcupada de SqlBaseHojaQuirurgica: "+e);
			return result;
		}
		
		return result;
	}
	
	/*************************************
	 *   INICIO MT 6497 Usabilidad HQx   *
	 *************************************/
	
	/**
	 * Consulta de peticiones o solicitudes de cirugia de un paciente
	 * <br><br>
	 * Cuando el parametro <b>codigoPeticion</b> sea null, consultara todas las peticiones, en caso contrario consultara la peticion especifica
	 * 
	 * @param connection
	 * @param codigoTarifario
	 * @param codigoIngreso
	 * @param codigoPaciente
	 * @param codigoPeticion 
	 * @param numeroSolicitud
	 * @param valorTrue en oracle se utiliza la cadena '0' o '1' pero en postgresql utiliza true o false
	 * @return lista de peticiones/solicitudes
	 * @throws BDException
	 * @author jeilones
	 * @created 24/06/2013
	 */
	public static List<PeticionQxDto> consultarPeticiones (Connection connection, String codigoTarifario,int codigoIngreso, int codigoPaciente, Integer codigoPeticion, Integer numeroSolicitud, String valorTrue) throws BDException
	{
		
		List<PeticionQxDto> listaPeticiones=new ArrayList<PeticionQxDto>(0);
		
		StringBuffer consulta = new StringBuffer(" SELECT")
				.append(" cuenta, paciente, codigo_peticion, fecha_cirugia, consecutivo_ordenes, " )
				.append("estado_solicitud, numero_solicitud, codigo_estado_solicitud, cc_solicitado, solicitante, especialidad, codigo_especialidad,es_solicitud, " )
				.append("codigo_solicitante, urgente, hqx_finalizada " )
			.append("FROM ( ");
		
		boolean soloConsultaPeticiones=false;
		boolean soloConsultaSolicitudes=false;
		boolean ambasConsultas=false;
		
		if(codigoPeticion==null&&numeroSolicitud==null){
			ambasConsultas=true;
		}else{
			if(codigoPeticion!=null&&numeroSolicitud==null){
				soloConsultaPeticiones=true;
			}else{
				if(codigoPeticion==null&&numeroSolicitud!=null){
					soloConsultaSolicitudes=true;
				}else{
					ambasConsultas=true;
				}
				
			}
		}
		
		if(soloConsultaPeticiones||ambasConsultas){
			consulta.append(" SELECT  " )
						.append("null as cuenta, " )
						.append("p.paciente as paciente, " )
						.append("p.codigo as codigo_peticion, " )
						.append("p.fecha_cirugia as fecha_cirugia, " )
						.append("null as consecutivo_ordenes, " )
						.append("null as estado_solicitud, " )
						.append("-1 as codigo_estado_solicitud, " )
						.append("-1 as numero_solicitud, " )
						.append("administracion.getnombremedico(p.solicitante) as solicitante, " )
						.append("null  as especialidad, " )
						.append("-1  as codigo_especialidad, " )
						.append("-1  as cc_solicitado, " )
						.append("'").append(ConstantesBD.acronimoNo).append("' AS es_solicitud," )
						.append("p.solicitante AS codigo_solicitante, " )
						.append(" CASE WHEN p.urgente="+valorTrue+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END AS urgente, " )
						.append("null as hqx_finalizada ")
				.append(" FROM peticion_qx  p " )
				.append(" INNER JOIN peticiones_servicio ps on(p.codigo=ps.peticion_qx)" )
				.append(" INNER JOIN servicios se on(ps.servicio=se.codigo)" )
				.append(" WHERE p.codigo not in (select solcir.codigo_peticion from solicitudes_cirugia solcir where solcir.codigo_peticion is not null and solcir.codigo_peticion=p.codigo) " )
				.append(" AND (se.tipo_servicio='").append(ConstantesBD.codigoServicioQuirurgico)
				.append("' OR se.tipo_servicio='").append(ConstantesBD.codigoServicioPartosCesarea).append("' OR se.tipo_servicio='").append(ConstantesBD.codigoServicioNoCruentos).append("') " )
				.append(" AND p.estado_peticion!=").append(ConstantesBD.codigoEstadoPeticionAnulada)
				.append(" AND p.paciente=? " );
		if(codigoPeticion!=null){
			consulta.append(" AND p.codigo = ")
				.append(codigoPeticion.intValue())
				.append(" ");
		}
		}
		if(soloConsultaSolicitudes||ambasConsultas){
			if(ambasConsultas){
				consulta.append(" UNION " );
			}
			consulta.append("SELECT " )
				.append("c.id as cuenta, " )
				.append("p.paciente as paciente, " )
				.append("p.codigo as codigo_peticion, " )
				.append("p.fecha_cirugia as   fecha_cirugia, " )
				.append("s.consecutivo_ordenes_medicas as consecutivo_ordenes, " )
				.append("getestadosolhis(s.estado_historia_clinica) as estado_solicitud, " )
				.append("s.estado_historia_clinica as codigo_estado_solicitud, " )
				.append("sc.numero_solicitud as numero_solicitud, " )
				.append("administracion.getnombremedico(s.codigo_medico) as solicitante, " )
				.append("CASE WHEN s.especialidad_solicitante IS NULL THEN '' ELSE getnombreespecialidad(s.especialidad_solicitante) END as especialidad, " )
				.append("s.especialidad_solicitante  as codigo_especialidad, " )
				.append("s.centro_costo_solicitado  as cc_solicitado, " )
				.append("'").append(ConstantesBD.acronimoSi).append("' AS es_solicitud, " )
				.append("s.codigo_medico AS codigo_solicitante, " )
				.append(" coalesce(CASE WHEN s.urgente="+valorTrue+" THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END,'') AS urgente, ")
				.append("hqx.finalizada as hqx_finalizada ")
				.append(" FROM peticion_qx p " )
				.append(" INNER JOIN solicitudes_cirugia sc ON(sc.codigo_peticion=p.codigo) " )
				.append(" INNER JOIN solicitudes s ON(sc.numero_solicitud=s.numero_solicitud) " )
				.append(" INNER JOIN cuentas c on (c.id=s.cuenta) " )
				.append(" LEFT JOIN hoja_quirurgica hqx on hqx.numero_solicitud = s.numero_solicitud " )
				.append(" WHERE (sc.ind_qx='").append(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia).append("' " )
				.append(" OR sc.ind_qx='").append(ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento).append("' ) " )
				.append(" AND estado_peticion!=").append(ConstantesBD.codigoEstadoPeticionAnulada)
				.append(" AND c.id_ingreso=? ");
			if(codigoPeticion==null&&numeroSolicitud==null){
				consulta.append(" and c.estado_cuenta in (").append(ConstantesBD.codigoEstadoCuentaActiva).append(",").append(ConstantesBD.codigoEstadoCuentaAsociada).append(",").append(ConstantesBD.codigoEstadoCuentaFacturadaParcial).append(") ");
			}else{
				if(codigoPeticion!=null){
					consulta.append(" AND p.codigo = ")
						.append(codigoPeticion.intValue())
						.append(" ");
				}
			}
			if(numeroSolicitud!=null){
				consulta.append(" AND s.numero_solicitud = ")
				.append(numeroSolicitud.intValue())
				.append(" ");
			}
		}
		consulta.append(") tabla ")
			.append(" ORDER BY codigo_peticion DESC ");
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{

			String consultaString=consulta.toString();
			
			ps =  connection.prepareStatement(consultaString);
			int posicionParametro=1;
			if(ambasConsultas||soloConsultaPeticiones){
				ps.setInt(posicionParametro,codigoPaciente);
				posicionParametro++;
			}
			if(ambasConsultas||soloConsultaSolicitudes){
				ps.setInt(posicionParametro,codigoIngreso);
			}
			
			rs=ps.executeQuery();
			
			while(rs.next()){
				
				PeticionQxDto peticion=new PeticionQxDto();
				peticion.setCodigoCuenta(rs.getInt("cuenta"));
				peticion.setCodigoPaciente(rs.getInt("paciente"));
				peticion.setCodigoPeticion(rs.getInt("codigo_peticion"));
				peticion.setFechaCirugia(rs.getDate("fecha_cirugia"));
				
				ProfesionalHQxDto medicoSolicitante=new ProfesionalHQxDto();
				medicoSolicitante.setIdMedico(rs.getInt("codigo_solicitante"));
				medicoSolicitante.setNombreCompleto(rs.getString("solicitante"));
				
				peticion.setMedicoSolicitante(medicoSolicitante);
				
				if(rs.getObject("hqx_finalizada")!=null){
					peticion.setFinalizada(rs.getBoolean("hqx_finalizada"));
				}
				
				peticion.setEsSolicitud(UtilidadTexto.getBoolean(rs.getString("es_solicitud")));
				peticion.setEsUrgente(UtilidadTexto.getBoolean(rs.getString("urgente")));
				
				if(peticion.isEsSolicitud()){
					EstadoSolicitudDto estadoSolicitud=new EstadoSolicitudDto();
					estadoSolicitud.setCodigo(rs.getInt("codigo_estado_solicitud"));
					estadoSolicitud.setNombre(rs.getString("estado_solicitud"));
					
					SolicitudCirugiaDto solicitudCirugia=new SolicitudCirugiaDto();
					solicitudCirugia.setConsecutivoSolicitud(rs.getInt("consecutivo_ordenes"));
					solicitudCirugia.setEstadoSolicitud(estadoSolicitud);
					solicitudCirugia.setNumeroSolicitud(rs.getInt("numero_solicitud"));
					solicitudCirugia.setEsUrgente(UtilidadTexto.getBoolean(rs.getString("urgente")));
					solicitudCirugia.setCodigoCentroCostoSolicitado(rs.getInt("cc_solicitado"));
					
					EspecialidadDto especilialidadSolicita=new EspecialidadDto();
					especilialidadSolicita.setCodigo(rs.getInt("codigo_especialidad"));
					especilialidadSolicita.setNombre(rs.getString("especialidad"));
					solicitudCirugia.setEspecialidadSolicita(especilialidadSolicita);
					
					peticion.setSolicitudCirugia(solicitudCirugia);
				}
				
				/*No depende de la especialidad, se envia null*/
				List<ServicioHQxDto>servicios=consultarServiciosPeticion(connection,peticion,null,codigoTarifario,true,false);
				peticion.setServiciosHQx(servicios);
				
				listaPeticiones.add(peticion);
			}
			
		}
		catch (SQLException e) 
		{
			logger.error("\n problema consultando las peticiones de cirugias "+e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		
		return listaPeticiones;
	}
	
	/**
	 * Consulta los servicios de la peticion/solicitud de cirugia.
	 * <br/><br/>
	 * Para el nombre del servicio se consulta el codigo manual estandar para busqueda de servicios, si este no esta definido
	 * se consulta el nombre del servicio definido para el codigo tarifario CUPS.
	 * <br/><br/>
	 * Si el parametro <b>vienePeticion</b> se encuentra en <b>true</b>, consultara todos los servicios que esten asociados a la peticion/solicitud que hagan sido agregados
	 * unicamente al momento de la creacion de la solicitud/peticion, en caso contrario, consultara todos los servicios, incluso los que hayan sido registrados en la Hoja Qx. 
	 * 
	 * @param connection
	 * @param peticion
	 * @param especialidadDto
	 * @param codigoTarifario
	 * @param vienePeticion
	 * @param consultarProfesionales
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @param consultarProfesionales 
	 * @created 25/06/2013
	 */
	public static List<ServicioHQxDto> consultarServiciosPeticion(Connection connection,
			PeticionQxDto peticion,EspecialidadDto especialidadDto, String codigoTarifario, boolean vienePeticion, boolean consultarProfesionales) throws BDException {
		
		List<ServicioHQxDto>servicios=new ArrayList<ServicioHQxDto>(0);
		PreparedStatement ps = null;
		ResultSet rs = null;
		try{
			
			StringBuffer consulta=new StringBuffer("SELECT ") 
					.append(" scxs.servicio AS codigo_servicio,") 
					.append(" FACTURACION.getObtenerCodigoServHist(scxs.SERVICIO, "+codigoTarifario+", TO_CHAR(ING.FECHA_INGRESO, 'DD/MM/YYYY')) AS cod_referencia_servicio, ")
					.append(" FACTURACION.getNombreServicioHistorico(scxs.SERVICIO, "+codigoTarifario+", TO_CHAR(ING.FECHA_INGRESO, 'DD/MM/YYYY')) AS nombre_referencia_servicio, " )
					.append(" getnombreespecialidad(s.especialidad) as nom_especialidad_servicio, " )
					.append(" s.especialidad As especialidad_servicio, "  )
					.append(" scxs.consecutivo AS numero_servicio, ")
					.append(" s.tipo_servicio AS tipo_servicio, " )
					.append(" scxs.observaciones AS observaciones_sol_sir, ") 
					.append(" scxs.codigo AS codigo_sol_cir, " )
					.append(" scxs.finalidad AS finalidad, " )
					.append(" s.naturaleza_servicio AS naturaleza_servicio, ") 
					.append(" scxs.via_cx AS via_cx," )
					.append(" scxs.ind_bilateral AS ind_bilateral,") 
					.append(" scxs.ind_via_acceso AS ind_via_acceso,") 
					.append(" scxs.especialidad AS especialidad_interviene,") 
					.append(" s.requiere_diagnostico AS serv_requiere_diag, " )
					.append(" scxs.numero_solicitud AS numero_solicitud," )
					.append(" s.requiere_interpretacion As serv_req_interpretacion, " )
					.append(" s.espos AS es_pos," )
					.append(" scxs.viene_peticion, " )
					.append(" scxs.auto_ent_sub, ")
					.append(" count(jus.codigo) as cantidad_justificacion ")
				.append(" FROM sol_cirugia_por_servicio scxs " )
				.append(" INNER JOIN servicios s ON (scxs.servicio=s.codigo) " )
				.append(" INNER JOIN ORDENES.solicitudes  				SOL ON (scxs.NUMERO_SOLICITUD = SOL.NUMERO_SOLICITUD) " )
				.append(" INNER JOIN MANEJOPACIENTE.cuentas             CUE ON (CUE.id = SOL.cuenta) " )
				.append(" INNER JOIN MANEJOPACIENTE.INGRESOS          	ING ON (ING.ID = CUE.ID_INGRESO) " )
				.append(" LEFT JOIN inventarios.justificacion_serv_sol jus ON (jus.solicitud=scxs.numero_solicitud AND jus.servicio =s.codigo) ");
				
			if(especialidadDto!=null){
				consulta.append("INNER JOIN SALASCIRUGIA.informe_qx_por_especialidad iqxe " )
						.append("on iqxe.codigo=scxs.cod_informe_especialidad ");
			}
				
			consulta.append(" WHERE scxs.numero_solicitud=? " );
			
			if(vienePeticion){
				consulta.append(" AND scxs.viene_peticion = ? " );
			}
			
			if(especialidadDto!=null){
				consulta.append(" AND scxs.especialidad = ? " );
			}
			
			consulta.append("group by scxs.servicio , ")
					.append("FACTURACION.getObtenerCodigoServHist(scxs.SERVICIO, "+codigoTarifario+", TO_CHAR(ING.FECHA_INGRESO, 'DD/MM/YYYY')), ")
					.append("FACTURACION.getNombreServicioHistorico(scxs.SERVICIO, "+codigoTarifario+", TO_CHAR(ING.FECHA_INGRESO, 'DD/MM/YYYY')), ")
					.append("getnombreespecialidad(s.especialidad), ")
					.append("s.especialidad, ")
					.append("scxs.consecutivo, ")
					.append("s.tipo_servicio, ")
					.append("scxs.observaciones, ")
					.append("scxs.codigo, ")
					.append("scxs.finalidad, ")
					.append("s.naturaleza_servicio, ")
				  	.append("scxs.via_cx, ")
				  	.append("scxs.ind_bilateral, ")
				  	.append("scxs.ind_via_acceso, ")
				  	.append("scxs.especialidad, ")
				  	.append("s.requiere_diagnostico, ")
				  	.append("scxs.numero_solicitud, ")
				  	.append("s.requiere_interpretacion, ")
				  	.append("s.espos, ")
				  	.append("scxs.viene_peticion, ")
				  	.append("scxs.auto_ent_sub")
				 .append(" ORDER BY scxs.consecutivo ASC " );
			
			String cadena2= consulta.toString();
			
			consulta=new StringBuffer(" SELECT ") 
					.append("ps.servicio AS codigo_servicio, ") 
					.append("FACTURACION.getObtenerCodigoServHist(scxs.SERVICIO, "+codigoTarifario+", TO_CHAR(ING.FECHA_INGRESO, 'DD/MM/YYYY')) AS cod_referencia_servicio, ")
					.append("FACTURACION.getNombreServicioHistorico(scs.SERVICIO, "+codigoTarifario+", TO_CHAR(ING.FECHA_INGRESO, 'DD/MM/YYYY')) AS nombre_referencia_servicio, " )
					.append("getnombreespecialidad(s.especialidad) as nom_especialidad_servicio, ")
					.append("s.especialidad As especialidad_servicio, ")
					.append("ps.numero_servicio AS numero_servicio, " )
					.append("s.tipo_servicio AS tipo_servicio,  ")
					.append("s.espos AS es_pos " )
				.append(" FROM peticiones_servicio ps ")
				.append(" INNER JOIN servicios s ON(ps.servicio=s.codigo) ")
				.append(" WHERE ps.peticion_qx=? " );
			
			consulta.append(" ORDER BY ps.numero_servicio ASC " );
			
			String cadena3= consulta.toString();
			
			if(peticion.isEsSolicitud()){
				ps=connection.prepareStatement(cadena2);
				int posNumeroSolicitud=1;
				int posVienePeticion=2;
				int posEspecialidad=2;
				ps.setInt(posNumeroSolicitud, peticion.getSolicitudCirugia().getNumeroSolicitud());
				if(vienePeticion){
					ps.setBoolean(posVienePeticion, vienePeticion);
					posEspecialidad=3;
				}
				if(especialidadDto!=null){
					ps.setInt(posEspecialidad, especialidadDto.getCodigo());
				}
			}else{
				ps=connection.prepareStatement(cadena3);
				ps.setInt(1, peticion.getCodigoPeticion());
			}
			rs=ps.executeQuery();
			
			while(rs.next()){
				ServicioHQxDto servicioHQx=new ServicioHQxDto();
				servicioHQx.setCodigo(rs.getInt("codigo_servicio"));
				servicioHQx.setCodigoReferenciaServicio(rs.getString("cod_referencia_servicio"));
				servicioHQx.setDescripcionReferenciaServicio(rs.getString("nombre_referencia_servicio"));
				
				EspecialidadDto especialidad=new EspecialidadDto();
				especialidad.setCodigo(rs.getInt("especialidad_servicio"));
				especialidad.setNombre(rs.getString("nom_especialidad_servicio"));
				servicioHQx.setEspecialidad(especialidad);
				servicioHQx.setNumeroServicio(rs.getInt("numero_servicio"));
				servicioHQx.setTipoServicio(rs.getString("tipo_servicio"));

				servicioHQx.setEsPos(UtilidadTexto.getBoolean(rs.getString("es_pos")));
				
				if(peticion.isEsSolicitud()){
					servicioHQx.setCodigoServicioXCirugia(rs.getInt("codigo_sol_cir"));
					servicioHQx.setObservacionesServicioXCirugia(rs.getString("observaciones_sol_sir"));
					servicioHQx.setFinalidad(rs.getInt("finalidad"));
					servicioHQx.setNaturalezaServicio(rs.getString("naturaleza_servicio"));
					if(rs.getObject("viene_peticion")!=null){
						servicioHQx.setVieneDePeticion(rs.getBoolean("viene_peticion"));
					}else{
						/**
						 * Significa que el registro no tiene guardado el valor por defecto el cual
						 * deberia ser true, que indica que si fue registrado al momento de la creacion de la solicitud
						 * */
						servicioHQx.setVieneDePeticion(true);
					}
					
					servicioHQx.setNumeroAutorizacion(rs.getInt("auto_ent_sub"));

					MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.salasCirugia.HojaQuirurgicaForm");
					
					TipoViaDto tipoVia=new TipoViaDto();
					tipoVia.setCodigo(ConstantesBD.codigoNuncaValido);
					if(rs.getString("via_cx")!=null){
						
						tipoVia.setAcronimo(rs.getString("via_cx"));
						
						if(tipoVia.getAcronimo().equals(fuenteMensaje.getMessage("hojaQuirurgicaForm.tipoVia.acronimoIgual"))){
							tipoVia.setCodigo(ConstantesBD.codigoIgualCx);
							tipoVia.setNombre(fuenteMensaje.getMessage("hojaQuirurgicaForm.tipoVia.igual"));
						}
						if(tipoVia.getAcronimo().equals(fuenteMensaje.getMessage("hojaQuirurgicaForm.tipoVia.acronimoDiferente"))){
							tipoVia.setCodigo(ConstantesBD.codigoDiferenteCx);
							tipoVia.setNombre(fuenteMensaje.getMessage("hojaQuirurgicaForm.tipoVia.diferente"));
						}
					}
					
					servicioHQx.setTipoVia(tipoVia);
					
					servicioHQx.setBilateral(UtilidadTexto.getBoolean(rs.getString("ind_bilateral")));
					servicioHQx.setViaAcceso(UtilidadTexto.getBoolean(rs.getString("ind_via_acceso")));
					
					especialidad=new EspecialidadDto();
					especialidad.setCodigo(rs.getInt("especialidad_interviene"));
					servicioHQx.setEspecialidad(especialidad);
					servicioHQx.setRequiereDiagnostico(UtilidadTexto.getBoolean(rs.getString("serv_requiere_diag")));
					servicioHQx.setRequiereInterpretacion(UtilidadTexto.getBoolean(rs.getString("serv_req_interpretacion")));
				
					if(rs.getLong("cantidad_justificacion")>0){
						servicioHQx.setHaSidoJustificado(true);
					}else{
						servicioHQx.setHaSidoJustificado(false);
					}
				}
				
				if(consultarProfesionales){
					List<ProfesionalHQxDto>profesionales=consultarProfesionalesXServicio(connection, servicioHQx);
					servicioHQx.setProfesionalesXServicio(profesionales);
				}
				
				servicios.add(servicioHQx);
			}
		} catch (SQLException e) {
			logger.error("\n problema consultando los servicios de una peticion/solicitud de cirugias "+e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return servicios;
	}

	/**
	 * Consulta las especialidades que intervienen en una cirugia
	 * 
	 * @param connection
	 * @param codigoSolicitudCx
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 19/06/2013
	 */
	public static List<EspecialidadDto> consultarEspecialidadesInformeQx (Connection connection, int codigoSolicitudCx) throws BDException{
		List<EspecialidadDto> listaEspecialidades=new ArrayList<EspecialidadDto>(0);
		StringBuffer consulta=new StringBuffer("SELECT ESP.CODIGO,ESP.NOMBRE,IQXE.CONFIRMADO FROM SALASCIRUGIA.SOLICITUDES_CIRUGIA SC ") 
			.append("INNER JOIN SALASCIRUGIA.INFORME_QX_POR_ESPECIALIDAD IQXE ON IQXE.CODIGO_SOLICITUDES_CIRUGIA = SC.NUMERO_SOLICITUD ")
			.append("INNER JOIN ADMINISTRACION.ESPECIALIDADES ESP ON ESP.CODIGO=IQXE.ESPECIALIDAD ")
			.append("WHERE SC.NUMERO_SOLICITUD = ? ");
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try
		{

			String consultaString=consulta.toString();
			
			ps =  connection.prepareStatement(consultaString);
			ps.setInt(1,codigoSolicitudCx);
			
			rs=ps.executeQuery();
			
			while(rs.next()){
				EspecialidadDto especialidad=new EspecialidadDto();
				especialidad.setCodigo(rs.getInt("CODIGO"));
				especialidad.setNombre(rs.getString("NOMBRE"));
				especialidad.setEsConfirmado(rs.getBoolean("CONFIRMADO"));
				
				listaEspecialidades.add(especialidad);
			}
			
		}
		catch (SQLException e) 
		{
			logger.error("\n problema consultando las especialidades que intervienen en cirugias "+e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		
		return listaEspecialidades;
	}

	/**
	 * Consulta la informacion del acto quirurgica
	 *  
	 * @param connection
	 * @param numeroSolicitud
	 * @return InformacionActoQxDto
	 * @throws BDException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	public static InformacionActoQxDto consultarInformacionActoQx (Connection connection,int numeroSolicitud) throws BDException
	{
		StringBuffer consulta=new StringBuffer("SELECT " )
				.append(" CASE WHEN hq.politrauma =").append(ValoresPorDefecto.getValorTrueParaConsultas())
		   		.append(" THEN '"+ConstantesBD.acronimoSi)
		   		.append("' ELSE '"+ConstantesBD.acronimoNo)
	   		.append("' END As politrauma," )
	   		.append(" CASE WHEN hq.finalizada =").append(ValoresPorDefecto.getValorTrueParaConsultas())
		   		.append(" THEN '").append(ConstantesBD.acronimoSi)
		   		.append("' ELSE '").append(ConstantesBD.acronimoNo+"' " )
				.append("END AS finalizada," )
	   		.append(" hq.fecha_finaliza AS fecha_finaliza," )
	   		.append(" hq.hora_finaliza AS hora_finaliza," )
	   		.append(" CASE WHEN hq.participo_anestesiologo =").append(ValoresPorDefecto.getValorTrueParaConsultas())
		   		.append(" THEN '").append(ConstantesBD.acronimoSi)
		   		.append("' ELSE '").append(ConstantesBD.acronimoNo)
	   		.append("' END AS part_anes," )
	   		.append(" ta.codigo AS codigo_tipo_anes, " )
	   		.append(" ta.acronimo AS acronimo_tipo_anes, " )
	   		.append(" ta.descripcion AS nombre_tipo_anes, " )
	   		.append(" med.codigo_medico AS anestesiologo, " )
	   		.append(" per.primer_nombre AS anes_primer_nombre, " )
	   		.append(" per.segundo_nombre AS anes_segundo_nombre, " )
	   		.append(" per.primer_apellido AS anes_primer_apellido, " )
	   		.append(" per.segundo_apellido AS anes_segundo_apellido ")
			.append(" FROM salascirugia.hoja_quirurgica hq " )
			.append(" LEFT JOIN medicos med on med.codigo_medico=hq.anestesiologo " )
			.append(" LEFT JOIN personas per on per.codigo=med.codigo_medico " )
			.append(" LEFT JOIN salascirugia.tipos_anestesia ta on ta.codigo = hq.tipo_anestesia " )
			.append(" WHERE hq.numero_solicitud=? ");

		PreparedStatement ps = null;
		ResultSet rs = null;
		InformacionActoQxDto informacionActoQxDto=new InformacionActoQxDto();
		try 
		{
			String cadena = consulta.toString();
			ps =  connection.prepareStatement(cadena);
			ps.setInt(1,numeroSolicitud);
			rs=ps.executeQuery();
			
			if(rs.next()){
				if(rs.getObject("politrauma")!=null){
					informacionActoQxDto.setPolitrauma(UtilidadTexto.getBoolean(rs.getString("politrauma")));
				}
				
				if(rs.getObject("part_anes")!=null){
					informacionActoQxDto.setParticipaAnestesiologo(UtilidadTexto.getBoolean(rs.getString("part_anes")));
				}
				if(informacionActoQxDto.getParticipaAnestesiologo()!=null&&informacionActoQxDto.getParticipaAnestesiologo().booleanValue()){
					
					ProfesionalHQxDto anestesiologo=new ProfesionalHQxDto();
					anestesiologo.setIdMedico(rs.getInt("anestesiologo"));
					anestesiologo.setPrimerNombre(rs.getString("anes_primer_nombre"));
					anestesiologo.setSegundoNombre(rs.getString("anes_segundo_nombre"));
					anestesiologo.setPrimerApellido(rs.getString("anes_primer_apellido"));
					anestesiologo.setSegundoApellido(rs.getString("anes_segundo_apellido"));
				
					informacionActoQxDto.setAnestesiologo(anestesiologo);
				}
				
				informacionActoQxDto.setFinalizadaHQx(UtilidadTexto.getBoolean(rs.getString("finalizada")));
				informacionActoQxDto.setFechaFinalizaHQx(rs.getDate("fecha_finaliza"));
				informacionActoQxDto.setHoraFinalizaHQx(rs.getString("hora_finaliza"));
				
				TipoAnestesiaDto tipoAnestesia=new TipoAnestesiaDto();
				tipoAnestesia.setCodigo(rs.getInt("codigo_tipo_anes"));
				tipoAnestesia.setAcronimo(rs.getString("acronimo_tipo_anes"));
				tipoAnestesia.setDescripcion(rs.getString("nombre_tipo_anes"));
				
				informacionActoQxDto.setTipoAnestesia(tipoAnestesia);
				
			}
		}
		catch (SQLException  e) 
		{
			logger.info("\n problema consultando la informacion de acto Qx "+e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
		
		
		return informacionActoQxDto;
	}

	/**
	 * Consulta el diagnostico principal preoperatorio registrado en la HQx
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return Diagnostico principal
	 * @throws BDException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	public static DtoDiagnostico consultarDiagnosticoPrincipalPreoperatorio(Connection connection,int numeroSolicitud) throws BDException{
		StringBuffer consulta=new StringBuffer("SELECT " )
				.append("dpcx.codigo AS codigo, " )
				.append("diag.acronimo AS acronimo_diag, " )
				.append("diag.tipo_cie AS tipo_cie_diag, " )
				.append("getnombrediagnostico(diag.acronimo,diag.tipo_cie) AS nom_diagnostico " )
			.append("FROM diag_preoperatorio_cx dpcx " )
			.append("INNER JOIN manejopaciente.diagnosticos diag on (diag.acronimo=dpcx.diagnostico AND diag.tipo_cie=dpcx.tipo_cie) ")
			.append("WHERE dpcx.numero_solicitud=? ")
			.append(" and dpcx.principal = ").append(ValoresPorDefecto.getValorTrueParaConsultas());
		
		DtoDiagnostico diagnostico=null;
		String cadena=consulta.toString();
		PreparedStatement ps=null;
		ResultSet rs= null;
		try {
			ps = connection.prepareStatement(cadena);
			ps.setInt(1, numeroSolicitud);
			rs = ps.executeQuery();
			
			if(rs.next()){
				diagnostico=new DtoDiagnostico();
				diagnostico.setCodigoDxPreoperatorio(rs.getInt("codigo"));
				diagnostico.setAcronimoDiagnostico(rs.getString("acronimo_diag"));
				diagnostico.setTipoCieDiagnosticoInt(rs.getInt("tipo_cie_diag"));
				diagnostico.setNombreDiagnostico(rs.getString("nom_diagnostico"));
				diagnostico.organizarNombreCompletoDiagnostico();
			}
		} catch (SQLException e) {
			logger.info("\n problema consultando el diagnostico principal preoperatorio "+e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return diagnostico;
		
	}
	
	/**
	 * Consulta los diagnosticos relacionados preoperatorios registrados en la HQx
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return Diagnosticos relacionados
	 * @throws BDException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	public static List<DtoDiagnostico> consultarDiagnosticosRelacionadosPreoperatorio(Connection connection,int numeroSolicitud) throws BDException{
		StringBuffer consulta=new StringBuffer("SELECT " )
				.append("dpcx.codigo AS codigo, " )
				.append("diag.acronimo AS acronimo_diag, " )
				.append("diag.tipo_cie AS tipo_cie_diag, " )
				.append("getnombrediagnostico(diag.acronimo,diag.tipo_cie) AS nom_diagnostico " )
			.append("FROM diag_preoperatorio_cx dpcx " )
			.append("INNER JOIN manejopaciente.diagnosticos diag on (diag.acronimo=dpcx.diagnostico AND diag.tipo_cie=dpcx.tipo_cie) ")
			.append("WHERE dpcx.numero_solicitud=? ")
			.append(" and dpcx.principal = ").append(ValoresPorDefecto.getValorFalseParaConsultas());
		
		List<DtoDiagnostico> diagnosticos=new ArrayList<DtoDiagnostico>(0);
		String cadena=consulta.toString();
		PreparedStatement ps=null;
		ResultSet rs= null;
		try {
			ps = connection.prepareStatement(cadena);
			ps.setInt(1, numeroSolicitud);
			rs = ps.executeQuery();
			
			while(rs.next()){
				DtoDiagnostico diagnostico=new DtoDiagnostico();
				diagnostico.setCodigoDxPreoperatorio(rs.getInt("codigo"));
				diagnostico.setAcronimoDiagnostico(rs.getString("acronimo_diag"));
				diagnostico.setTipoCieDiagnosticoInt(rs.getInt("tipo_cie_diag"));
				diagnostico.setNombreDiagnostico(rs.getString("nom_diagnostico"));
				diagnostico.organizarNombreCompletoDiagnostico();
				
				diagnosticos.add(diagnostico);
			}
		} catch (SQLException e) {
			logger.info("\n problema consultando los diagnosticos relacionados preoperatorio "+e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return diagnosticos;
		
	}

	
	/**
	 * Consulta los profesionales de una especialidad (codigoEspecialidad) especifica y segun el indicativo profesionalActivo lo indique, ordenados ascendemente por sus apellidos y nombres,
	 * si profesionalActivo es null entonces consultara tanto los profesionales activos como los inactivos
	 * 
	 * @param connection
	 * @param codigoInstitucion
	 * @param profesionalActivo
	 * @param codigoEspecialidad
	 * @return lista de profesionales
	 * @throws BDException
	 * @author jeilones
	 * @created 10/07/2013
	 */
	public static List<ProfesionalHQxDto> consultarProfesionales(Connection connection, int codigoInstitucion, Boolean profesionalActivo,String codigoEspecialidad) throws BDException{
		StringBuffer consulta=new StringBuffer("select med.codigo_medico,per.primer_nombre,per.segundo_nombre,per.primer_apellido,per.segundo_apellido ");
		if(codigoEspecialidad!=null){
			consulta.append(", esp.codigo as codigo_especialidad,esp.nombre as nombre_especialidad ");
		}
		consulta.append("from personas per ")
			.append("inner join medicos med on med.codigo_medico=per.codigo ")
			.append("inner join medicos_instituciones med_ins on med_ins.codigo_medico=med.codigo_medico ");
		if(codigoEspecialidad!=null){
			consulta.append("inner join especialidades_medicos em on em.codigo_medico=med.codigo_medico ")
					.append("inner join especialidades esp on esp.codigo=em.codigo_especialidad ");
		}
		
		consulta.append("where med_ins.codigo_institucion = ? ");
		
		if(codigoEspecialidad!=null){
			consulta.append("and esp.codigo = ? ");
		}
		
		if(profesionalActivo!=null){
			consulta.append("and not exists (select med_ina.codigo_medico from administracion.medicos_inactivos med_ina where med_ina.codigo_medico = med.codigo_medico) ");
		}
			
		consulta.append("order by per.primer_apellido,per.segundo_apellido,per.primer_nombre,per.segundo_nombre,per.primer_apellido ");
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String cadena=consulta.toString();
		
		List<ProfesionalHQxDto>profesionales=new ArrayList<ProfesionalHQxDto>(0);
		try {
			
			ps=connection.prepareStatement(cadena);
			
			ps.setInt(1, codigoInstitucion);
			
			if(codigoEspecialidad!=null){
				ps.setInt(2, Integer.parseInt(codigoEspecialidad));
			}
			
			rs=ps.executeQuery();
			
			while(rs.next()){
				ProfesionalHQxDto profesionalHQxDto=new ProfesionalHQxDto();
				profesionalHQxDto.setIdMedico(rs.getInt("codigo_medico"));
				
				profesionalHQxDto.setPrimerNombre(rs.getString("primer_nombre"));
				profesionalHQxDto.setSegundoNombre(rs.getString("segundo_nombre"));
				profesionalHQxDto.setPrimerApellido(rs.getString("primer_apellido"));
				profesionalHQxDto.setSegundoApellido(rs.getString("segundo_apellido"));
				
				profesionalHQxDto.setNombreCompleto
				(
					(UtilidadCadena.noEsVacio(profesionalHQxDto.getPrimerApellido())	? profesionalHQxDto.getPrimerApellido()		: "")+ " " +
					(UtilidadCadena.noEsVacio(profesionalHQxDto.getSegundoApellido())	? profesionalHQxDto.getSegundoApellido()	: "")+ " " +
					(UtilidadCadena.noEsVacio(profesionalHQxDto.getPrimerNombre())		? profesionalHQxDto.getPrimerNombre() 		: "")+ " " +
					(UtilidadCadena.noEsVacio(profesionalHQxDto.getSegundoNombre())		? profesionalHQxDto.getSegundoNombre() 		: "")
				);
				
				if(codigoEspecialidad!=null){
					EspecialidadDto especialidadDto=new EspecialidadDto();
					especialidadDto.setCodigo(rs.getInt("codigo_especialidad"));
					especialidadDto.setNombre(rs.getString("nombre_especialidad"));
					profesionalHQxDto.setEspecialidad(especialidadDto);
				}
				profesionales.add(profesionalHQxDto);
			}
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return profesionales;
	}
	
	/**
	 * Consulta la informacion del informe qx por especialidad, las descripciones operatorias registradas,
	 * patologias, complicaciones, hallazgos y materiales especiales registrados.
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @param codigoEspecialidad
	 * @return informe qx por especialidad
	 * @throws BDException
	 * @author jeilones
	 * @created 26/06/2013
	 */
	public static InformeQxEspecialidadDto consultarDescripcionOperatoriaXEspecialidad(Connection connection, int numeroSolicitud, int codigoEspecialidad) throws BDException{
		//informe_qx_por_especialidad
		InformeQxEspecialidadDto informeQxEspecialidadDto=new InformeQxEspecialidadDto();
		StringBuffer consulta=new StringBuffer("select  iqxe.codigo,iqxe.confirmado,iqxe.tipo_herida, ")
			.append("iqxe.codigo_solicitudes_cirugia,iqxe.complicaciones,iqxe.hallazgos, ")
			.append("iqxe.materiales_especiales ")
			.append("from salascirugia.solicitudes_cirugia sc ")
			.append("inner join salascirugia.informe_qx_por_especialidad iqxe on iqxe.codigo_solicitudes_cirugia=sc.numero_solicitud ")
			.append("inner join administracion.especialidades esp on (esp.codigo = iqxe.especialidad and esp.codigo=iqxe.especialidad) ")
			.append("where sc.numero_solicitud = ? ")
			.append("and iqxe.especialidad = ? ");
		
		PreparedStatement ps=null;
		ResultSet rs=null;
		
		PreparedStatement ps1=null;
		ResultSet rs1=null;
		
		PreparedStatement ps2=null;
		ResultSet rs2=null;
		
		String cadena=consulta.toString();
		try {
			ps=connection.prepareStatement(cadena);
			ps.setInt(1, numeroSolicitud);
			ps.setInt(2, codigoEspecialidad);
			
			rs=ps.executeQuery();
			if(rs.next()){
				informeQxEspecialidadDto.setCodigo(rs.getInt("codigo"));
				informeQxEspecialidadDto.setConfirmada(rs.getBoolean("confirmado"));
				
				TipoHeridaDto tipoHeridaDto=new TipoHeridaDto();
				
				if(rs.getString("tipo_herida")!=null){
					tipoHeridaDto.setAcronimo(rs.getString("tipo_herida"));
					if(tipoHeridaDto.getAcronimo().equals(ConstantesIntegridadDominio.acronimoTipoHeridaLimpiaContaminada)){
						tipoHeridaDto.setNombre(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoHeridaLimpiaContaminada).toString());
					}else if(tipoHeridaDto.getAcronimo().equals(ConstantesIntegridadDominio.acronimoTipoHeridaContaminada)){
						tipoHeridaDto.setNombre(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoHeridaContaminada).toString());
					}else if(tipoHeridaDto.getAcronimo().equals(ConstantesIntegridadDominio.acronimoTipoHeridaLimpia)){
						tipoHeridaDto.setNombre(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoHeridaLimpia).toString());
					}else if(tipoHeridaDto.getAcronimo().equals(ConstantesIntegridadDominio.acronimoTipoHeridaSucia)){
						tipoHeridaDto.setNombre(ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoHeridaSucia).toString());
					}
				}
				informeQxEspecialidadDto.setTipoHerida(tipoHeridaDto);
				
				informeQxEspecialidadDto.setCodigoSolicitudesCirugia(rs.getInt("codigo_solicitudes_cirugia"));
				informeQxEspecialidadDto.setComplicaciones(rs.getString("complicaciones"));
				informeQxEspecialidadDto.setHallazgos(rs.getString("hallazgos"));
				informeQxEspecialidadDto.setObservacionesMaterialesEspeciales(rs.getString("materiales_especiales"));
				if(UtilidadTexto.isEmpty(informeQxEspecialidadDto.getObservacionesMaterialesEspeciales())){
					informeQxEspecialidadDto.setUsaMaterialesEspeciales(false);
				}else{
					informeQxEspecialidadDto.setUsaMaterialesEspeciales(true);
				}
				
				/*Se consulta las descipciones operatorias del informe qx por especialidad*/
				consulta=new StringBuffer("select dqx.codigo,dqx.descripcion, ")
					.append("dqx.fecha_grabacion,dqx.hora_grabacion,dqx.usuario_grabacion ")
					.append("from salascirugia.informe_qx_por_especialidad iqxe ")
					.append("inner join salascirugia.descripciones_qx_hq dqx on dqx.cod_informe_especialidad = iqxe.codigo ")
					.append("where iqxe.codigo= ? ")
					.append("order by dqx.fecha_grabacion desc, dqx.hora_grabacion desc ");
				cadena=consulta.toString();
				ps1=connection.prepareStatement(cadena);
				ps1.setInt(1, informeQxEspecialidadDto.getCodigo());
				
				rs1=ps1.executeQuery();
				
				List<DescripcionOperatoriaDto>descripcionesOperatorias=new ArrayList<DescripcionOperatoriaDto>(0);
				
				while(rs1.next()){
					DescripcionOperatoriaDto descripcionOperatoriaDto=new DescripcionOperatoriaDto();
					descripcionOperatoriaDto.setCodigo(rs1.getInt("codigo"));
					descripcionOperatoriaDto.setDescripcion(rs1.getString("descripcion"));
					descripcionOperatoriaDto.setFechaGrabacion(rs1.getDate("fecha_grabacion"));
					descripcionOperatoriaDto.setHoraGrabacion(rs1.getString("hora_grabacion"));
					descripcionOperatoriaDto.setUsuarioGrabacion(rs1.getString("usuario_grabacion"));
					
					descripcionesOperatorias.add(descripcionOperatoriaDto);
				}
				
				if(descripcionesOperatorias.size()>0){
					informeQxEspecialidadDto.setUltimaDescripcionOperatoria(descripcionesOperatorias.get(0));
				}else{
					informeQxEspecialidadDto.setUltimaDescripcionOperatoria(new DescripcionOperatoriaDto());
				}
				informeQxEspecialidadDto.setDescripcionesOperatorias(descripcionesOperatorias);
				
				consulta=new StringBuffer("select pat.codigo,pat.descripcion, ")
					.append("pat.fecha_grabacion,pat.hora_grabacion, ")
					.append("pat.usuario_grabacion ")
					.append("from salascirugia.informe_qx_por_especialidad iqxe ")
					.append("inner join salascirugia.patologias pat on pat.cod_informe_especialidad = iqxe.codigo ")
					.append("where iqxe.codigo= ? ")
					.append("order by pat.fecha_grabacion desc,pat.hora_grabacion desc ");
				cadena=consulta.toString();
				
				ps2=connection.prepareStatement(cadena);
				ps2.setInt(1, informeQxEspecialidadDto.getCodigo());
				
				rs2=ps2.executeQuery();
				
				List<PatologiaDto>patologias=new ArrayList<PatologiaDto>(0);
				
				while(rs2.next()){
					PatologiaDto patologiaDto=new PatologiaDto();
					patologiaDto.setCodigo(rs2.getInt("codigo"));
					patologiaDto.setDescripcion(rs2.getString("descripcion"));
					patologiaDto.setFechaGrabacion(rs2.getDate("fecha_grabacion"));
					patologiaDto.setHoraGrabacion(rs2.getString("hora_grabacion"));
					patologiaDto.setUsuarioGrabacion(rs2.getString("usuario_grabacion"));
					
					patologias.add(patologiaDto);
				}
				
				if(patologias.size()>0){
					informeQxEspecialidadDto.setUltimaPatologia(patologias.get(0));
				}else{
					informeQxEspecialidadDto.setUltimaPatologia(new PatologiaDto());
				}
				informeQxEspecialidadDto.setPatologias(patologias);
			}
				
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			UtilidadBD.cerrarObjetosPersistencia(ps1, rs1, null);
			UtilidadBD.cerrarObjetosPersistencia(ps2, rs2, null);
		}
		
		return informeQxEspecialidadDto;
	}

	/**
	 * Consulta el diagnostico principal postoperatorio registrado en el informe Qx, si el parametro codigoInformeQx es null, consulta el primer diagnostico
	 * principal postoperatorio registrado en el informe qx 
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return Diagnostico principal
	 * @throws BDException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	public static DtoDiagnostico consultarDiagnosticoPrincipalPostoperatorio(Connection connection, Integer codigoInformeQx) throws BDException{
		StringBuffer consulta=new StringBuffer("select diag_post.codigo AS codigo_dx_post, dx.acronimo AS acronimo_diag, dx.tipo_cie AS tipo_cie_diag, getnombrediagnostico(dx.acronimo,dx.tipo_cie) AS nom_diagnostico " )
				.append("from salascirugia.solicitudes_cirugia sc ")
				.append("inner join salascirugia.informe_qx_por_especialidad iqxe on iqxe.codigo_solicitudes_cirugia=sc.numero_solicitud ")
				.append("inner join salascirugia.diag_post_opera_sol_cx diag_post on diag_post.cod_informe_especialidad = iqxe.codigo ")
				.append("inner join manejopaciente.diagnosticos dx on (dx.acronimo=diag_post.diagnostico and dx.tipo_cie = diag_post.tipo_cie) ")
				.append("WHERE diag_post.principal = ").append(ValoresPorDefecto.getValorTrueParaConsultas()).append(" ");
				
		if(codigoInformeQx!=null){
			consulta.append("AND iqxe.codigo= ? ");
		}
				
		consulta.append("order by diag_post.cod_informe_especialidad asc,diag_post.fecha_modifica asc, diag_post.hora_modifica asc "); 
		
		DtoDiagnostico diagnostico=null;
		String cadena=consulta.toString();
		PreparedStatement ps=null;
		ResultSet rs= null;
		try {
			ps = connection.prepareStatement(cadena);
			if(codigoInformeQx!=null){
				ps.setInt(1, codigoInformeQx);
			}
			ps.setMaxRows(1);
			
			rs = ps.executeQuery();
			
			if(rs.next()){
				diagnostico=new DtoDiagnostico();
				diagnostico.setCodigoDxPostOperatorio(rs.getInt("codigo_dx_post"));
				diagnostico.setAcronimoDiagnostico(rs.getString("acronimo_diag"));
				diagnostico.setTipoCieDiagnosticoInt(rs.getInt("tipo_cie_diag"));
				diagnostico.setNombreDiagnostico(rs.getString("nom_diagnostico"));
				diagnostico.organizarNombreCompletoDiagnostico();
			}
		} catch (SQLException e) {
			logger.info("\n problema consultando el diagnostico principal postoperatorio "+e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return diagnostico;
		
	}

	/**
	 * Consulta los diagnosticos relacionados postoperatorios registrados en el informe Qx, si el parametro codigoInformeQx es null, consulta los primeros diagnosticos
	 * relacionados postoperatorios registrado en el informe qx 
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return Diagnosticos relacionados
	 * @throws BDException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	public static List<DtoDiagnostico> consultarDiagnosticosRelacionadosPostoperatorio(Connection connection,int numeroSolicitud,Integer codigoInformeQx) throws BDException{
		StringBuffer consulta=new StringBuffer("select diag_post.codigo AS codigo_dx_post, iqxe.codigo AS codigo, dx.acronimo AS acronimo_diag, dx.tipo_cie AS tipo_cie_diag, getnombrediagnostico(dx.acronimo,dx.tipo_cie) AS nom_diagnostico ")
				.append("from salascirugia.solicitudes_cirugia sc ")
				.append("inner join salascirugia.informe_qx_por_especialidad iqxe on iqxe.codigo_solicitudes_cirugia=sc.numero_solicitud ")
				.append("inner join salascirugia.diag_post_opera_sol_cx diag_post on diag_post.cod_informe_especialidad = iqxe.codigo ")
				.append("inner join manejopaciente.diagnosticos dx on (dx.acronimo=diag_post.diagnostico and dx.tipo_cie = diag_post.tipo_cie) ")
				.append("where diag_post.principal = ").append(ValoresPorDefecto.getValorFalseParaConsultas()).append(" ")
				.append("and diag_post.complicacion = ").append(ValoresPorDefecto.getValorFalseParaConsultas()).append(" ")
				.append("and sc.numero_solicitud =  ? ");
		
		if(codigoInformeQx!=null){
			consulta.append("and iqxe.codigo= ? ");
		}
		consulta.append("order by diag_post.cod_informe_especialidad asc,diag_post.fecha_modifica asc, diag_post.hora_modifica asc ");
		
		List<DtoDiagnostico> diagnosticos=new ArrayList<DtoDiagnostico>(0);
		String cadena=consulta.toString();
		PreparedStatement ps=null;
		ResultSet rs= null;
		try {
			ps = connection.prepareStatement(cadena);
			ps.setInt(1, numeroSolicitud);
			if(codigoInformeQx!=null){
				ps.setInt(2, codigoInformeQx);
			}
			rs = ps.executeQuery();
			
			Integer codigoInformerQxTemp=null;
			while(rs.next()){
				if(codigoInformeQx==null){
					if(codigoInformerQxTemp==null){
						codigoInformerQxTemp=rs.getInt("codigo");
					}
					
					if(codigoInformerQxTemp.compareTo(rs.getInt("codigo"))!=0){
						/*Solo se toma en cuenta el primer informe qx por especialidad registrado*/
						break;
					}
				}
				
				DtoDiagnostico diagnostico=new DtoDiagnostico();
				diagnostico.setCodigoDxPostOperatorio(rs.getInt("codigo_dx_post"));
				diagnostico.setAcronimoDiagnostico(rs.getString("acronimo_diag"));
				diagnostico.setTipoCieDiagnosticoInt(rs.getInt("tipo_cie_diag"));
				diagnostico.setNombreDiagnostico(rs.getString("nom_diagnostico"));
				diagnostico.organizarNombreCompletoDiagnostico();
				diagnosticos.add(diagnostico);
			}
		} catch (SQLException e) {
			logger.info("\n problema consultando los diagnosticos relacionados postoperatorios "+e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return diagnosticos;
		
	}
	
	/**
	 * Consulta los diagnosticos de complicacion postoperatorios registrados en el informe Qx, si el parametro codigoInformeQx es null, consulta los primeros diagnosticos
	 * relacionados postoperatorios registrado en el informe qx 
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return Diagnostico de complicacion
	 * @throws BDException
	 * @author jeilones
	 * @created 20/06/2013
	 */
	public static DtoDiagnostico consultarDiagnosticoComplicacionPostoperatorio(Connection connection,Integer codigoInformeQx) throws BDException{
		StringBuffer consulta=new StringBuffer("select diag_post.codigo AS codigo_dx_post, iqxe.codigo AS codigo, dx.acronimo AS acronimo_diag, dx.tipo_cie AS tipo_cie_diag, getnombrediagnostico(dx.acronimo,dx.tipo_cie) AS nom_diagnostico ")
				.append("from salascirugia.solicitudes_cirugia sc ")
				.append("inner join salascirugia.informe_qx_por_especialidad iqxe on iqxe.codigo_solicitudes_cirugia=sc.numero_solicitud ")
				.append("inner join salascirugia.diag_post_opera_sol_cx diag_post on diag_post.cod_informe_especialidad = iqxe.codigo ")
				.append("inner join manejopaciente.diagnosticos dx on (dx.acronimo=diag_post.diagnostico and dx.tipo_cie = diag_post.tipo_cie) ")
				.append("where diag_post.principal = ").append(ValoresPorDefecto.getValorFalseParaConsultas()).append(" ")
				.append("and diag_post.complicacion = ").append(ValoresPorDefecto.getValorTrueParaConsultas()).append(" ");
		if(codigoInformeQx!=null){
			consulta.append("and iqxe.codigo= ? ");
		}
		consulta.append("order by diag_post.cod_informe_especialidad asc,diag_post.fecha_modifica asc, diag_post.hora_modifica asc ");
		
		DtoDiagnostico diagnostico=null;
		String cadena=consulta.toString();
		PreparedStatement ps=null;
		ResultSet rs= null;
		try {
			ps = connection.prepareStatement(cadena);
			if(codigoInformeQx!=null){
				ps.setInt(1, codigoInformeQx);
			}
			rs = ps.executeQuery();
			
			if(rs.next()){
				
				diagnostico=new DtoDiagnostico();
				diagnostico.setCodigoDxPostOperatorio(rs.getInt("codigo_dx_post"));
				diagnostico.setAcronimoDiagnostico(rs.getString("acronimo_diag"));
				diagnostico.setTipoCieDiagnosticoInt(rs.getInt("tipo_cie_diag"));
				diagnostico.setNombreDiagnostico(rs.getString("nom_diagnostico"));
				diagnostico.organizarNombreCompletoDiagnostico();
				
			}
		} catch (SQLException e) {
			logger.info("\n problema consultando los diagnosticos de complicacion postoperatorio "+e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return diagnostico;
		
	}

	/**
	 * Permite persistir la informacion de la seccion informacion del acto Qx (Diagnosticos y participacion de anestesiologia)
	 * 
	 * @param connection
	 * @param actualizacion
	 * @param peticionQxDto
	 * @param informacionActoQxDto
	 * @param dxRelacionadosEliminar
	 * @throws BDException
	 * @author jeilones
	 * @param usuarioBasico 
	 * @created 27/06/2013
	 */
	public static void guardarInformacionActoQuirurgico(Connection connection,boolean actualizacion,PeticionQxDto peticionQxDto,InformacionActoQxDto informacionActoQxDto, List<DtoDiagnostico> dxRelacionadosEliminar, UsuarioBasico usuarioBasico)throws BDException{
		StringBuffer insert=null;
		if(actualizacion){
			insert=new StringBuffer("update salascirugia.hoja_quirurgica set ") 
			.append("politrauma = ?, participo_anestesiologo = ?, tipo_anestesia = ?, anestesiologo = ? , finalizada = ? , fecha_grabacion = ? , hora_grabacion = ?, datos_medico = ? ")
			.append("where numero_solicitud = ?");
		}else{
			insert=new StringBuffer("insert into salascirugia.hoja_quirurgica ") 
				.append("(numero_solicitud, politrauma, participo_anestesiologo, tipo_anestesia, anestesiologo, finalizada, fecha_grabacion, hora_grabacion, datos_medico) ")
				.append("values (?,?,?,?,?,?,?,?,?)");
		}
		PreparedStatement ps=null;
	
		String cadena=insert.toString();
		try {
			ps=connection.prepareStatement(cadena);
			
			int posNumeroSolicitud=1;
			int posPolitrauma=2;
			int posParticipaAnestesiologo=3;
			int posTipoAnestesia=4;
			int posAnestesiologo=5;
			int posFinalizada=6;
			int posFechaGrabacion=7;
			int posHoraGrabacion=8;
			int posDatosMedico=9;
			
			if(actualizacion){
				posPolitrauma=1;
				posParticipaAnestesiologo=2;
				posTipoAnestesia=3;
				posAnestesiologo=4;
				posFinalizada=5;
				posFechaGrabacion=6;
				posHoraGrabacion=7;
				posDatosMedico=8;
				posNumeroSolicitud=9;
				
			}
			
			ps.setInt(posNumeroSolicitud, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
			if(informacionActoQxDto.getPolitrauma()!=null){
				ps.setBoolean(posPolitrauma, informacionActoQxDto.getPolitrauma());
			}else{
				ps.setNull(posPolitrauma, Types.BOOLEAN);
			}
			ps.setBoolean(posParticipaAnestesiologo, informacionActoQxDto.getParticipaAnestesiologo());
			if(informacionActoQxDto.getTipoAnestesia()!=null&&informacionActoQxDto.getTipoAnestesia().getCodigo()>0){
				ps.setInt(posTipoAnestesia, informacionActoQxDto.getTipoAnestesia().getCodigo());
			}else{
				ps.setNull(posTipoAnestesia, Types.INTEGER);
			}
			if(informacionActoQxDto.getAnestesiologo()!=null&&informacionActoQxDto.getAnestesiologo().getIdMedico()>0){
				ps.setInt(posAnestesiologo, informacionActoQxDto.getAnestesiologo().getIdMedico());
			}else{
				ps.setNull(posAnestesiologo, Types.INTEGER);
			}
			if(informacionActoQxDto.getFinalizadaHQx()!=null){
				ps.setBoolean(posFinalizada, informacionActoQxDto.getFinalizadaHQx());
			}else{
				ps.setBoolean(posFinalizada, false);
			}
			
			/***/
			/*posFechaGrabacion=6;
			posHoraGrabacion=7;
			posDatosMedico=8;*/
			ps.setDate(posFechaGrabacion,new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(connection).getTime()));
			ps.setString(posHoraGrabacion, UtilidadFecha.getHoraActual(connection));
			ps.setString(posDatosMedico, usuarioBasico.getInformacionGeneralPersonalSalud());
			/***/
			
			int resultado=ps.executeUpdate();
			if(resultado!=1){
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
			}
			
			guardarDiagnosticosPreoperatorio(connection, peticionQxDto, informacionActoQxDto,dxRelacionadosEliminar);
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
		}
	}
	
	/**
	 * Persiste la informacion de los dx preoperatorio de la informacion del acto Qx
	 * 
	 * @param connection
	 * @param peticionQxDto
	 * @param informacionActoQxDto
	 * @param dxRelacionadosEliminar
	 * @throws BDException
	 * @author jeilones
	 * @created 2/07/2013
	 */
	private static void guardarDiagnosticosPreoperatorio(Connection connection,PeticionQxDto peticionQxDto,
			InformacionActoQxDto informacionActoQxDto,List<DtoDiagnostico> dxRelacionadosEliminar)throws BDException
	{
		StringBuffer insert=new StringBuffer("insert into salascirugia.diag_preoperatorio_cx ") 
				.append("(codigo, diagnostico, tipo_cie, numero_solicitud, principal) ")
				.append("values(?,?,?,?,?)");
		
		StringBuffer update=new StringBuffer("update salascirugia.diag_preoperatorio_cx set ") 
			.append("diagnostico=?, tipo_cie=?, numero_solicitud=?, principal=? ")
			.append("where codigo= ? ");
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		PreparedStatement ps2=null;
	
		try {
			String cadena=insert.toString();
			
			ps=connection.prepareStatement(cadena);
			
			int posSecuencia=1;
			int posDx=2;
			int posTipoCIE=3;
			int posNumeroSolicitud=4;
			int posPrincipal=5;
			
			int secuencia=-1;
			
			int resultado=0;
			
			if(informacionActoQxDto.getDiagnosticoPrincipal()!=null){
				
				DtoDiagnostico dxPrincipalActual=consultarDiagnosticoPrincipalPreoperatorio(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
				if(dxPrincipalActual==null){
					secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(connection, "salascirugia.seq_dia_pre_op_solcx");
					
					posSecuencia=1;
					posDx=2;
					posTipoCIE=3;
					posNumeroSolicitud=4;
					posPrincipal=5;
					
					ps.setInt(posSecuencia, secuencia);
					ps.setString(posDx, informacionActoQxDto.getDiagnosticoPrincipal().getAcronimoDiagnostico());
					ps.setInt(posTipoCIE, informacionActoQxDto.getDiagnosticoPrincipal().getTipoCieDiagnosticoInt());
					ps.setInt(posNumeroSolicitud, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
					ps.setBoolean(posPrincipal, true);
					
					resultado=ps.executeUpdate();
					
					if(resultado!=1){
						throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
					}
				}else{
					cadena=update.toString();
					ps1=connection.prepareStatement(cadena);
					
					secuencia=dxPrincipalActual.getCodigoDxPreoperatorio();
					
					posDx=1;
					posTipoCIE=2;
					posNumeroSolicitud=3;
					posPrincipal=4;
					posSecuencia=5;
					
					ps1.setString(posDx, informacionActoQxDto.getDiagnosticoPrincipal().getAcronimoDiagnostico());
					ps1.setInt(posTipoCIE, informacionActoQxDto.getDiagnosticoPrincipal().getTipoCieDiagnosticoInt());
					ps1.setInt(posNumeroSolicitud, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
					ps1.setBoolean(posPrincipal, true);
					ps1.setInt(posSecuencia, secuencia);
					
					resultado=ps1.executeUpdate();
					
					if(resultado!=1){
						throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
					}
				}
			}
			
			if(!informacionActoQxDto.getDiagnosticosRelacionados().isEmpty()){
				
				List<DtoDiagnostico>dxRelacionadosActuales=consultarDiagnosticosRelacionadosPreoperatorio(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
				
				for(DtoDiagnostico diagnostico:informacionActoQxDto.getDiagnosticosRelacionados()){
					boolean existe=false;
					for(DtoDiagnostico dxActual:dxRelacionadosActuales){
						if(dxActual.getAcronimoDiagnostico().equals(diagnostico.getAcronimoDiagnostico())
								&&dxActual.getTipoCieDiagnosticoInt().intValue()==diagnostico.getTipoCieDiagnosticoInt().intValue()){
							existe=true;
							break;
						}
					}
					
					if(!existe&&diagnostico.isCheckDiagRelacionado()){
						secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(connection, "salascirugia.seq_dia_pre_op_solcx");
						
						posSecuencia=1;
						posDx=2;
						posTipoCIE=3;
						posNumeroSolicitud=4;
						posPrincipal=5;
						
						ps.setInt(posSecuencia, secuencia);
						ps.setString(posDx, diagnostico.getAcronimoDiagnostico());
						ps.setInt(posTipoCIE, diagnostico.getTipoCieDiagnosticoInt());
						ps.setInt(posNumeroSolicitud, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
						ps.setBoolean(posPrincipal, false);
						
						resultado=ps.executeUpdate();
						
						if(resultado!=1){
							throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
						}
					}
				}
				
				StringBuffer delete=new StringBuffer("delete from salascirugia.diag_preoperatorio_cx ") 
					.append("where numero_solicitud = ? AND principal = ? AND diagnostico = ? AND tipo_cie = ? ");
				cadena = delete.toString();
				ps2=connection.prepareStatement(cadena);
				ps2.setInt(1, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
				ps2.setBoolean(2, false);
				
				for(DtoDiagnostico diagnostico:dxRelacionadosEliminar){
					ps2.setString(3, diagnostico.getAcronimoDiagnostico());
					ps2.setInt(4, diagnostico.getTipoCieDiagnosticoInt());
					
					resultado=ps2.executeUpdate();
					
				}
			}else{
				StringBuffer delete=new StringBuffer("delete from salascirugia.diag_preoperatorio_cx ") 
					.append("where numero_solicitud = ? AND principal = ? ");
				
				cadena = delete.toString();
				ps2=connection.prepareStatement(cadena);
				ps2.setInt(1, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
				ps2.setBoolean(2, false);
				
				resultado=ps2.executeUpdate();
			}
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
			UtilidadBD.cerrarObjetosPersistencia(ps1, null, null);
			UtilidadBD.cerrarObjetosPersistencia(ps2, null, null);
		}
	}

	/**
	 * Consulta si se ha guardado un informe qx por especialidad de una solicitud de cirugia 
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @param codigoEspecialidad
	 * @return existe
	 * @throws BDException
	 * @author jeilones
	 * @created 2/07/2013
	 */
	public static boolean existeInformeQxEspecialidad(Connection connection,int numeroSolicitud, Integer codigoInformeQx, Integer codigoEspecialidad) throws BDException{
		boolean existe=false;
		StringBuffer consulta=new StringBuffer("select count(1) AS total from ")
			.append("salascirugia.solicitudes_cirugia sc ") 
			.append("inner join salascirugia.informe_qx_por_especialidad iqxe on iqxe.codigo_solicitudes_cirugia=sc.numero_solicitud ")
			.append("where sc.numero_solicitud = ? ");
			
		if(codigoEspecialidad!=null){
			consulta.append("and iqxe.especialidad = ? ");
		}else{
			if(codigoInformeQx!=null){
				consulta.append("and iqxe.codigo = ? ");
			}
		}
		
		PreparedStatement ps= null;
		ResultSet rs = null;
		try {
			String cadena=consulta.toString();
			
			ps=connection.prepareStatement(cadena);
			ps.setInt(1, numeroSolicitud);
			if(codigoEspecialidad!=null){
				ps.setInt(2, codigoEspecialidad);
			}else{
				if(codigoInformeQx!=null){
					ps.setInt(2, codigoInformeQx);
				}
			}
			
			rs=ps.executeQuery();
			
			if(rs.next()){
				Integer total=rs.getInt("total");
				if(total>0){
					existe=true;
				}
			}
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		
		return existe;
	}
	
	/**
	 * Permite persistir la informacion de la seccion descripcion operatoria del Informe Qx (Servicios, Diagnosticos y Patologias)
	 * 
	 * @param connection
	 * @param existeIQxE
	 * @param peticionQxDto
	 * @param informeQxEspecialidadDto
	 * @param usuarioModifica
	 * @param dxRelacionadosEliminar
	 * @param serviciosEliminar 
	 * @param codigoTarifario
	 * @throws BDException
	 * @author jeilones
	 * @created 2/07/2013
	 */
	public static void guardarDescripcionOperatoria(Connection connection,boolean existeIQxE, PeticionQxDto peticionQxDto, InformeQxEspecialidadDto informeQxEspecialidadDto, UsuarioBasico usuarioModifica, List<DtoDiagnostico> dxRelacionadosEliminar, List<ServicioHQxDto> serviciosEliminar, String codigoTarifario) throws BDException {

		StringBuffer insert=new StringBuffer("insert into salascirugia.informe_qx_por_especialidad ")
			.append("(codigo,codigo_solicitudes_cirugia,complicaciones,confirmado,especialidad,hallazgos,materiales_especiales,tipo_herida) ") 
			.append("values (?,?,?,?,?,?,?,?) ");
		StringBuffer update=new StringBuffer("update salascirugia.informe_qx_por_especialidad ") 
			.append("set codigo_solicitudes_cirugia = ? ,complicaciones = ? , ")
			.append("confirmado = ? ,especialidad = ? ,hallazgos = ? , ")
			.append("materiales_especiales = ? ,tipo_herida = ?  ")
			.append("where codigo = ? ");
		
		StringBuffer insertDescripcion=new StringBuffer("insert into salascirugia.descripciones_qx_hq ")
			.append("(codigo,cod_informe_especialidad,descripcion,fecha_grabacion,hora_grabacion,usuario_grabacion) ")
			.append("values (?,?,?,?,?,?)");
		StringBuffer insertPatologia=new StringBuffer("insert into salascirugia.patologias ")
			.append("(codigo,cod_informe_especialidad,descripcion,fecha_grabacion,hora_grabacion,usuario_grabacion) ")
			.append("values (?,?,?,?,?,?)");
		
		PreparedStatement ps= null;
		PreparedStatement ps1= null;
		PreparedStatement ps2= null;
		try {
			String cadena = null;
		
			if(existeIQxE){
				cadena=update.toString();
			}else{
				cadena=insert.toString();
			}
			
			ps=connection.prepareStatement(cadena);
			
			int posCodigo=1;
			int posNumeroSolicitud=2;
			int posComplicaciones=3;
			int posConfirmado=4;
			int posEspecialidad=5;
			int posHallazgos=6;
			int posMaterialesEspeciales=7;
			int posTipoHerida=8;
			
			int secuenciaInfXEsp=0;
			
			if(existeIQxE){
				
				secuenciaInfXEsp=informeQxEspecialidadDto.getCodigo();
				
				posNumeroSolicitud=1;
				posComplicaciones=2;
				posConfirmado=3;
				posEspecialidad=4;
				posHallazgos=5;
				posMaterialesEspeciales=6;
				posTipoHerida=7;
				posCodigo=8;
			}else{
				secuenciaInfXEsp=UtilidadBD.obtenerSiguienteValorSecuencia(connection, "SALASCIRUGIA.SEQ_INFO_QX_ESP");
				informeQxEspecialidadDto.setCodigo(secuenciaInfXEsp);
			}
			
			ps.setInt(posCodigo, secuenciaInfXEsp);
			ps.setInt(posNumeroSolicitud, informeQxEspecialidadDto.getCodigoSolicitudesCirugia());
			if(informeQxEspecialidadDto.getComplicaciones()!=null&&!informeQxEspecialidadDto.getComplicaciones().isEmpty()){
				ps.setString(posComplicaciones, informeQxEspecialidadDto.getComplicaciones());
			}else{
				ps.setNull(posComplicaciones, Types.VARCHAR);
			}
			ps.setBoolean(posConfirmado, informeQxEspecialidadDto.isConfirmada());
			ps.setInt(posEspecialidad, informeQxEspecialidadDto.getEspecialidad().getCodigo());
			
			if(informeQxEspecialidadDto.getHallazgos()!=null&&!informeQxEspecialidadDto.getHallazgos().isEmpty()){
				ps.setString(posHallazgos, informeQxEspecialidadDto.getHallazgos());
			}else{
				ps.setNull(posHallazgos, Types.VARCHAR);
			}
			
			if(informeQxEspecialidadDto.getObservacionesMaterialesEspeciales()!=null&&!informeQxEspecialidadDto.getObservacionesMaterialesEspeciales().isEmpty()){
				ps.setString(posMaterialesEspeciales, informeQxEspecialidadDto.getObservacionesMaterialesEspeciales());
			}else{
				ps.setNull(posMaterialesEspeciales, Types.VARCHAR);
			}
			
			if(informeQxEspecialidadDto.getTipoHerida()!=null){
				ps.setString(posTipoHerida, informeQxEspecialidadDto.getTipoHerida().getAcronimo());
			}else{
				ps.setNull(posTipoHerida, Types.VARCHAR);
			}
			
			int resultado=ps.executeUpdate();
			
			if(resultado<=0){
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
			}
			
			java.sql.Date fechaActual=new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(connection).getTime());
			String horaActual=UtilidadFecha.getHoraActual(connection);
			
			if(informeQxEspecialidadDto.getUltimaDescripcionOperatoria()!=null){
				cadena=insertDescripcion.toString();
				ps1=connection.prepareStatement(cadena);
				int secuenciaDes=UtilidadBD.obtenerSiguienteValorSecuencia(connection,"SALASCIRUGIA.SEQ_DESCRIPCIONES_QX_HQ");
				int posSecuenciaDes=1;
				int posCodInformeEspecialidad=2;
				int posDescripcion=3;
				int posFechaGrabacionDes=4;
				int posHoraGrabacionDes=5;
				int posUsuarioModDes=6;
				
				ps1.setInt(posSecuenciaDes, secuenciaDes);
				ps1.setInt(posCodInformeEspecialidad, secuenciaInfXEsp);
				ps1.setString(posDescripcion, informeQxEspecialidadDto.getUltimaDescripcionOperatoria().getDescripcion());
				ps1.setDate(posFechaGrabacionDes, fechaActual);
				ps1.setString(posHoraGrabacionDes, horaActual);
				ps1.setString(posUsuarioModDes, usuarioModifica.getLoginUsuario());
				
				resultado=ps1.executeUpdate();
				
				if(resultado<=0){
					throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
				}
			}
			
			if(informeQxEspecialidadDto.getUltimaPatologia()!=null){
				cadena=insertPatologia.toString();
				ps2=connection.prepareStatement(cadena);
				int secuenciaPat=UtilidadBD.obtenerSiguienteValorSecuencia(connection,"SALASCIRUGIA.SEQ_PATOLOGIAS");
				int posSecuenciaPat=1;
				int posCodInformeEspecialidad=2;
				int posDescripcion=3;
				int posFechaGrabacionPat=4;
				int posHoraGrabacionPat=5;
				int posUsuarioModPat=6;
				
				ps2.setInt(posSecuenciaPat, secuenciaPat);
				ps2.setInt(posCodInformeEspecialidad, secuenciaInfXEsp);
				ps2.setString(posDescripcion, informeQxEspecialidadDto.getUltimaPatologia().getDescripcion());
				ps2.setDate(posFechaGrabacionPat, fechaActual);
				ps2.setString(posHoraGrabacionPat, horaActual);
				ps2.setString(posUsuarioModPat, usuarioModifica.getLoginUsuario());
				
				resultado=ps2.executeUpdate();
				
				if(resultado<=0){
					throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
				}
			}
				
			
			guardarDiagnosticosPostoperatorio(connection, peticionQxDto,informeQxEspecialidadDto, dxRelacionadosEliminar, usuarioModifica);
			
			guardarServiciosInformeQx(connection, informeQxEspecialidadDto, usuarioModifica, codigoTarifario, peticionQxDto,serviciosEliminar);
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
		}
	}
	
	/**
	 * Persiste los servicios que se realizan en la cirugia
	 * 
	 * @param connection
	 * @param informeQxEspecialidadDto
	 * @param usuarioModifica
	 * @param codigoTarifario
	 * @param peticion
	 * @param serviciosEliminar
	 * @throws BDException
	 * @author jeilones
	 * @created 3/07/2013
	 */
	private static void guardarServiciosInformeQx(Connection connection,InformeQxEspecialidadDto informeQxEspecialidadDto,UsuarioBasico usuarioModifica, String codigoTarifario, PeticionQxDto peticion,List<ServicioHQxDto>serviciosEliminar)throws BDException{
		StringBuffer insert=new StringBuffer("insert into salascirugia.sol_cirugia_por_servicio ")
			.append("(codigo,cod_informe_especialidad,especialidad,servicio,consecutivo,finalidad,ind_bilateral,ind_via_acceso,institucion,via_cx, ")
			.append("numero_solicitud, viene_peticion ) ")
			.append("values(?,?,?,?,?,?,?,?,?,?,?,?)");
		
		/*
		 * "INSERT INTO sol_cirugia_por_servicio (codigo,numero_solicitud,servicio,consecutivo," +
														 		  "institucion,finalidad, via_cx,ind_bilateral,ind_via_acceso,especialidad, " +
														 		  "cubierto, contrato_convenio) " +
														 "VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		 * */
		StringBuffer update=new StringBuffer("update salascirugia.sol_cirugia_por_servicio ")
			.append("set cod_informe_especialidad = ?,especialidad = ?,servicio = ?,consecutivo = ?,finalidad = ?,ind_bilateral = ?,ind_via_acceso = ?,institucion = ?,")
			.append("via_cx = ?,numero_solicitud = ? ")
			.append("where codigo = ? ");
		
		StringBuffer delete=new StringBuffer("delete from salascirugia.sol_cirugia_por_servicio ")
		.append("where codigo = ? ");
		
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		PreparedStatement ps2 = null;
		
		try {
			String cadena=insert.toString();
			ps=connection.prepareStatement(cadena);
			
			int posSecuencia=1;
			int posCodInformeEspecialidad=2;
			int posEspecialidad=3;
			int posServicio=4;
			int posConsecutivo=5;
			int posFinalidad=6;
			int posBilateral=7;
			int posViaAcceso=8;
			int posInstitucion=9;
			int posViaCx=10;
			int posNumeroSolicitud=11;
			int posVienePeticion=12;
			
			//int posTipoCirugia=2;
			//int posEpicrisis=1;
			//int posLiquidarServicio=2;
			
			
			if(!serviciosEliminar.isEmpty()){
				cadena=delete.toString();
				ps2=connection.prepareStatement(cadena);
				for(ServicioHQxDto servicio:serviciosEliminar){
					if(servicio.getCodigoServicioXCirugia()>0){
						
						eliminarInformacionServicios(connection,peticion,servicio);
						
						ps2.setInt(1, servicio.getCodigoServicioXCirugia());
						int resultado=ps2.executeUpdate();
						if(resultado<=0){
							throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
						}
					}
				}
				
			}
			
			cadena=update.toString();
			ps1=connection.prepareStatement(cadena);
			
			List<ServicioHQxDto>serviciosActuales=consultarServiciosPeticion(connection, peticion, null, codigoTarifario, false,false);
			
			for(ServicioHQxDto servicio:informeQxEspecialidadDto.getServicios()){
				boolean existe=false;
				for(ServicioHQxDto servicioActual:serviciosActuales){
					if(servicioActual.getCodigo()==servicio.getCodigo()&&servicioActual.getCodigoServicioXCirugia()==servicio.getCodigoServicioXCirugia()){
						existe=true;
						break;
					}
				}
				if(existe){
					posCodInformeEspecialidad=1;
					posEspecialidad=2;
					posServicio=3;
					posConsecutivo=4;
					posFinalidad=5;
					posBilateral=6;
					posViaAcceso=7;
					posInstitucion=8;
					posViaCx=9;
					posNumeroSolicitud=10;
					
					posSecuencia=11;
					
					ps1.setInt(posCodInformeEspecialidad, informeQxEspecialidadDto.getCodigo());
					ps1.setInt(posEspecialidad, informeQxEspecialidadDto.getEspecialidad().getCodigo());
					ps1.setInt(posServicio, servicio.getCodigo());
					ps1.setInt(posConsecutivo, servicio.getNumeroServicio());
					ps1.setInt(posFinalidad, servicio.getFinalidad());
					
					if(servicio.isBilateral()){
						ps1.setString(posBilateral, ConstantesBD.acronimoSi);
					}else{
						ps1.setString(posBilateral, ConstantesBD.acronimoNo);
					}
					
					if(servicio.isViaAcceso()){
						ps1.setString(posViaAcceso,ConstantesBD.acronimoSi);
					}else{
						ps1.setString(posViaAcceso,ConstantesBD.acronimoNo);
					}
					
					ps1.setInt(posInstitucion, usuarioModifica.getCodigoInstitucionInt());
					ps1.setString(posViaCx, servicio.getTipoVia().getAcronimo());
					ps1.setInt(posNumeroSolicitud, peticion.getSolicitudCirugia().getNumeroSolicitud());

					ps1.setInt(posSecuencia, servicio.getCodigoServicioXCirugia());
					
					int resultado=ps1.executeUpdate();
					if(resultado<=0){
						throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
					}
					
				}
				else{
					posSecuencia=1;
					posCodInformeEspecialidad=2;
					posEspecialidad=3;
					posServicio=4;
					posConsecutivo=5;
					posFinalidad=6;
					posBilateral=7;
					posViaAcceso=8;
					posInstitucion=9;
					posViaCx=10;
					posNumeroSolicitud=11;
					posVienePeticion=12;
					
					int secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(connection,"salascirugia.seq_sol_cx_ser");
					servicio.setCodigoServicioXCirugia(secuencia);
					
					ps.setInt(posSecuencia, secuencia);
					ps.setInt(posCodInformeEspecialidad, informeQxEspecialidadDto.getCodigo());
					ps.setInt(posEspecialidad, informeQxEspecialidadDto.getEspecialidad().getCodigo());
					ps.setInt(posServicio, servicio.getCodigo());
					ps.setInt(posConsecutivo, servicio.getNumeroServicio());
					ps.setInt(posFinalidad, servicio.getFinalidad());
					
					if(servicio.isBilateral()){
						ps.setString(posBilateral, ConstantesBD.acronimoSi);
					}else{
						ps.setString(posBilateral, ConstantesBD.acronimoNo);
					}
					
					if(servicio.isViaAcceso()){
						ps.setString(posViaAcceso,ConstantesBD.acronimoSi);
					}else{
						ps.setString(posViaAcceso,ConstantesBD.acronimoNo);
					}
					
					ps.setInt(posInstitucion, usuarioModifica.getCodigoInstitucionInt());
					ps.setString(posViaCx, servicio.getTipoVia().getAcronimo());
					ps.setInt(posNumeroSolicitud, peticion.getSolicitudCirugia().getNumeroSolicitud());
					/**
					 * Es registrado desde la Hoja Qx por tanto es false
					 * */
					ps.setBoolean(posVienePeticion, false);
					
					int resultado=ps.executeUpdate();
					if(resultado<=0){
						throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
					}
				}
			}
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
			UtilidadBD.cerrarObjetosPersistencia(ps1, null, null);
		}
	}
	
	
	/**
	 * Elimina la informacion relacionada a los servicios como los profesionales (si es servicio de parto, actualiza las tablas informacion_parto e info_parto_hijos al nuevo servicio de parto)
	 * 
	 * @param connection
	 * @param peticion
	 * @param servicio
	 * @throws BDException
	 * @author jeilones
	 * @created 4/07/2013
	 */
	private static void eliminarInformacionServicios(Connection connection, PeticionQxDto peticion, ServicioHQxDto servicio) throws BDException{
		
		/* 
		 * se deben eliminar (puede quedar un historico), 
		 * PROFESIONALES_CIRUGIA
		 * INFO_PARTO_HIJOS (aqui se debe actualizar el nuevo servicio de parto, no se puede eliminar)
		 * 
		 * DIAG_POST_OPERA_SOL_CX (se supone que no se deben eliminar, al hacer la migracion estos datos no son necesarios por el servicio)
		 * DESCRIPCIONES_QX_HQ (ahora estan a nivel de la especialidad y no del servicio, al hacer la migracion estos datos no son necesarios por el servicio)
		 * 
		 * DET_CX_HONORARIOS (al parecer solo se llena cuando se hace una liquidacion, al reversar la liquidacion se eliminan)
		 * DET_ASOCIO_CX_SALAS_MAT (no debe eliminarlo, segun parece siempre debe existir un servicio que lo relacione, al reversar la liquidacion se eliminan)
		 * 
		 * */
		StringBuffer deleteProfesionales=new StringBuffer("DELETE FROM salascirugia.profesionales_cirugia where cod_sol_cx_serv=? ");
		
		StringBuffer selectJustificacionesNOPOSServ=new StringBuffer("SELECT FROM inventarios.JUSTIFICACION_SERV_SOL where solicitud=? and servicio=? ");
		StringBuffer deleteJustificacionesNOPOSServ=new StringBuffer("DELETE FROM inventarios.JUSTIFICACION_SERV_SOL where codigo = ? ");
		StringBuffer deleteJustificacionesNOPOSResp=new StringBuffer("DELETE FROM inventarios.JUSTIFICACION_SERV_RESP where JUSTIFICACION_SERV_SOL=? ");
		StringBuffer deleteJustificacionesNOPOSParam=new StringBuffer("DELETE FROM inventarios.JUSTIFICACION_SERV_PARAM where JUSTIFICACION_SERV_SOL=? ");
		StringBuffer deleteJustificacionesNOPOSFijo=new StringBuffer("DELETE FROM inventarios.JUSTIFICACION_SERV_FIJO where JUSTIFICACION_SERV_SOL=? ");
		StringBuffer deleteJustificacionesNOPOSDx=new StringBuffer("DELETE FROM inventarios.JUSTIFICACION_SERV_DX where JUSTIFICACION_SERV_SOL=? ");
		
		StringBuffer updateInfoParto=new StringBuffer("UPTADE TABLE historiaclinica.informacion_parto set cirugia = ? " )
				.append("where consecutivo = ? ");
		
		StringBuffer updateInfoPartoHijos=new StringBuffer("UPTADE TABLE historiaclinica.info_parto_hijos set cirugia = ? " )
				.append("where cirugia=? ");
		
		PreparedStatement ps= null;
		PreparedStatement ps1= null;
		PreparedStatement ps2= null;
		
		PreparedStatement ps3= null;
		PreparedStatement ps4= null;
		PreparedStatement ps5= null;
		PreparedStatement ps6= null;
		PreparedStatement ps7= null;
		PreparedStatement ps8= null;
		try {
			
			String cadena=deleteProfesionales.toString();
			
			ps=connection.prepareStatement(cadena);
			ps.setInt(1, servicio.getCodigoServicioXCirugia());
			
			ps.executeUpdate();
				
			int codigoInfoParto=SqlBaseUtilidadesManejoPacienteDao.obtenerConsecutivoPartoXcodigoSolCxServ(connection, servicio.getCodigoServicioXCirugia()+"");
			
			if(servicio.getNuevoServicioParto()!=null&&codigoInfoParto>0){
				cadena=updateInfoParto.toString();
				ps1=connection.prepareStatement(cadena);
				ps1.setInt(1, servicio.getNuevoServicioParto().getCodigoServicioXCirugia());
				ps1.setInt(2, codigoInfoParto);
				
				ps1.executeUpdate();
				
				cadena=updateInfoPartoHijos.toString();
				ps2=connection.prepareStatement(cadena);
				//SolXServ nuevo
				ps2.setInt(1, servicio.getNuevoServicioParto().getCodigoServicioXCirugia());
				//SolXServ actual
				ps2.setInt(2, servicio.getCodigoServicioXCirugia());
				
				ps2.executeUpdate();
			}
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
			UtilidadBD.cerrarObjetosPersistencia(ps1, null, null);
			UtilidadBD.cerrarObjetosPersistencia(ps2, null, null);
		}
	}
	
	/**
	 * Persiste la informacion de los dx preoperatorio de la informacion del acto Qx
	 * 
	 * @param connection
	 * @param peticionQxDto
	 * @param informacionActoQxDto
	 * @param dxRelacionadosEliminar
	 * @throws BDException
	 * @author jeilones
	 * @param peticionQxDto 
	 * @created 2/07/2013
	 */
	private static void guardarDiagnosticosPostoperatorio(Connection connection,PeticionQxDto peticionQxDto, InformeQxEspecialidadDto informeQxEspecialidadDto,List<DtoDiagnostico> dxRelacionadosEliminar,UsuarioBasico usuarioModifica)throws BDException{
		/*COD_SOL_CX_SERVICIO*/
		StringBuffer insert=new StringBuffer("insert into salascirugia.diag_post_opera_sol_cx ") 
				.append("(codigo, diagnostico, tipo_cie, principal, complicacion, fecha_modifica, hora_modifica, usuario_modifica, cod_informe_especialidad) ")
				.append("values(?,?,?,?,?,?,?,?,?) ");
		
		StringBuffer update=new StringBuffer("update salascirugia.diag_post_opera_sol_cx set ") 
			.append("diagnostico=?, tipo_cie=?, principal=?, complicacion=?, fecha_modifica=?, hora_modifica=?, usuario_modifica=?, cod_informe_especialidad=? ")
			.append("where codigo= ? ");
		PreparedStatement ps=null;
		PreparedStatement ps1=null;
		PreparedStatement ps2=null;
		PreparedStatement ps3=null;
		PreparedStatement ps4=null;
		PreparedStatement ps5=null;
		
		try {
			String cadena=insert.toString();
			
			int posSecuencia=1;
			int posDx=2;
			int posTipoCIE=3;
			int posPrincipal=4;
			int posComplicacion = 5;
			int posFechaModifica=6;
			int posHoraModifica=7;
			int posUsuarioModifica=8;
			int posInformeXEspecialiad=9;
			
			
			int secuencia=-1;
			
			int resultado=0;
			
			java.sql.Date fechaActual=new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(connection).getTime());
			String horaActual=UtilidadFecha.getHoraActual(connection);
			
			if(informeQxEspecialidadDto.getDiagnosticoPostOperatorioPrincipal()!=null){
				/**
				 * Diagnostico principal
				 * */
				DtoDiagnostico dxPrincipalActual=consultarDiagnosticoPrincipalPostoperatorio(connection, informeQxEspecialidadDto.getCodigo());
				
				if(dxPrincipalActual==null){
					
					ps=connection.prepareStatement(cadena);
					
					secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(connection, "salascirugia.seq_dia_pos_op_solcx");
					
					posSecuencia=1;
					posDx=2;
					posTipoCIE=3;
					posPrincipal=4;
					posComplicacion=5;
					posFechaModifica=6;
					posHoraModifica=7;
					posUsuarioModifica=8;
					posInformeXEspecialiad=9;
					
					ps.setInt(posSecuencia, secuencia);
					ps.setString(posDx, informeQxEspecialidadDto.getDiagnosticoPostOperatorioPrincipal().getAcronimoDiagnostico());
					ps.setInt(posTipoCIE, informeQxEspecialidadDto.getDiagnosticoPostOperatorioPrincipal().getTipoCieDiagnosticoInt());
					//ps.setInt(posNumeroSolicitud, informeQxEspecialidadDto.getCodigoSolicitudesCirugia());
					ps.setBoolean(posPrincipal, true);
					ps.setBoolean(posComplicacion, false);
					ps.setDate(posFechaModifica, fechaActual);
					ps.setString(posHoraModifica, horaActual);
					ps.setString(posUsuarioModifica, usuarioModifica.getLoginUsuario());
					ps.setInt(posInformeXEspecialiad, informeQxEspecialidadDto.getCodigo());
					
					resultado=ps.executeUpdate();
					
					if(resultado!=1){
						throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
					}
				}else{
					cadena=update.toString();
					ps1=connection.prepareStatement(cadena);
					
					secuencia=dxPrincipalActual.getCodigoDxPostOperatorio();
					
					posDx=1;
					posTipoCIE=2;
					posPrincipal=3;
					posComplicacion=4;
					posFechaModifica=5;
					posHoraModifica=6;
					posUsuarioModifica=7;
					posInformeXEspecialiad=8;
					posSecuencia=9;
					
					
					ps1.setString(posDx, informeQxEspecialidadDto.getDiagnosticoPostOperatorioPrincipal().getAcronimoDiagnostico());
					ps1.setInt(posTipoCIE, informeQxEspecialidadDto.getDiagnosticoPostOperatorioPrincipal().getTipoCieDiagnosticoInt());
					//ps1.setInt(posNumeroSolicitud, informeQxEspecialidadDto.getCodigoSolicitudesCirugia());
					ps1.setBoolean(posPrincipal, true);
					ps1.setBoolean(posComplicacion, false);
					ps1.setDate(posFechaModifica, fechaActual);
					ps1.setString(posHoraModifica, horaActual);
					ps1.setString(posUsuarioModifica, usuarioModifica.getLoginUsuario());
					ps1.setInt(posInformeXEspecialiad, informeQxEspecialidadDto.getCodigo());
					
					ps1.setInt(posSecuencia, secuencia);
					
					resultado=ps1.executeUpdate();
					
					if(resultado!=1){
						throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
					}
				}
			}
			
			if(informeQxEspecialidadDto.getDiagnosticoPostOperatorioComplicacion()!=null){
				/**
				 * Diagnostico de complicacion
				 * */
				DtoDiagnostico dxComplicacionActual=consultarDiagnosticoComplicacionPostoperatorio(connection, informeQxEspecialidadDto.getCodigo());
				if(dxComplicacionActual==null){
					cadena=insert.toString();
					ps2=connection.prepareStatement(cadena);
					
					secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(connection, "salascirugia.seq_dia_pos_op_solcx");
					
					posSecuencia=1;
					posDx=2;
					posTipoCIE=3;
					posPrincipal=4;
					posComplicacion=5;
					posFechaModifica=6;
					posHoraModifica=7;
					posUsuarioModifica=8;
					posInformeXEspecialiad=9;
					
					ps2.setInt(posSecuencia, secuencia);
					ps2.setString(posDx, informeQxEspecialidadDto.getDiagnosticoPostOperatorioComplicacion().getAcronimoDiagnostico());
					ps2.setInt(posTipoCIE, informeQxEspecialidadDto.getDiagnosticoPostOperatorioComplicacion().getTipoCieDiagnosticoInt());
					//ps.setInt(posNumeroSolicitud, informeQxEspecialidadDto.getCodigoSolicitudesCirugia());
					ps2.setBoolean(posPrincipal, false);
					ps2.setBoolean(posComplicacion, true);
					ps2.setDate(posFechaModifica, fechaActual);
					ps2.setString(posHoraModifica, horaActual);
					ps2.setString(posUsuarioModifica, usuarioModifica.getLoginUsuario());
					ps2.setInt(posInformeXEspecialiad, informeQxEspecialidadDto.getCodigo());
					
					resultado=ps2.executeUpdate();
					
					if(resultado!=1){
						throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
					}
				}else{
					cadena=update.toString();
					ps3=connection.prepareStatement(cadena);
					
					secuencia=dxComplicacionActual.getCodigoDxPostOperatorio();
					
					posDx=1;
					posTipoCIE=2;
					posPrincipal=3;
					posComplicacion=4;
					posFechaModifica=5;
					posHoraModifica=6;
					posUsuarioModifica=7;
					posInformeXEspecialiad=8;
					posSecuencia=9;
					
					
					ps3.setString(posDx, informeQxEspecialidadDto.getDiagnosticoPostOperatorioComplicacion().getAcronimoDiagnostico());
					ps3.setInt(posTipoCIE, informeQxEspecialidadDto.getDiagnosticoPostOperatorioComplicacion().getTipoCieDiagnosticoInt());
					//ps1.setInt(posNumeroSolicitud, informeQxEspecialidadDto.getCodigoSolicitudesCirugia());
					ps3.setBoolean(posPrincipal, false);
					ps3.setBoolean(posComplicacion, true);
					ps3.setDate(posFechaModifica, fechaActual);
					ps3.setString(posHoraModifica, horaActual);
					ps3.setString(posUsuarioModifica, usuarioModifica.getLoginUsuario());
					ps3.setInt(posInformeXEspecialiad, informeQxEspecialidadDto.getCodigo());
					
					ps3.setInt(posSecuencia, secuencia);
					
					resultado=ps3.executeUpdate();
					
					if(resultado!=1){
						throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
					}
				}
			}
			
			if(!informeQxEspecialidadDto.getDiagnosticosPostOperatorioRelacionados().isEmpty()){
				
				List<DtoDiagnostico>dxRelacionadosActuales=consultarDiagnosticosRelacionadosPostoperatorio(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud(),informeQxEspecialidadDto.getCodigo());
				
				for(DtoDiagnostico diagnostico:informeQxEspecialidadDto.getDiagnosticosPostOperatorioRelacionados()){
					boolean existe=false;
					for(DtoDiagnostico dxActual:dxRelacionadosActuales){
						if(dxActual.getAcronimoDiagnostico().equals(diagnostico.getAcronimoDiagnostico())
								&&dxActual.getTipoCieDiagnosticoInt().intValue()==diagnostico.getTipoCieDiagnosticoInt().intValue()){
							existe=true;
							break;
						}
					}
					
					if(!existe&&diagnostico.isCheckDiagRelacionado()){
						secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(connection, "salascirugia.seq_dia_pos_op_solcx");
						
						cadena=insert.toString();
						ps4=connection.prepareStatement(cadena);
						
						posSecuencia=1;
						posDx=2;
						posTipoCIE=3;
						posPrincipal=4;
						posComplicacion=5;
						posFechaModifica=6;
						posHoraModifica=7;
						posUsuarioModifica=8;
						posInformeXEspecialiad=9;
						
						ps4.setInt(posSecuencia, secuencia);
						ps4.setString(posDx, diagnostico.getAcronimoDiagnostico());
						ps4.setInt(posTipoCIE, diagnostico.getTipoCieDiagnosticoInt());
						//ps.setInt(posNumeroSolicitud, informeQxEspecialidadDto.getCodigoSolicitudesCirugia());
						ps4.setBoolean(posPrincipal, false);
						ps4.setBoolean(posComplicacion, false);
						ps4.setDate(posFechaModifica, fechaActual);
						ps4.setString(posHoraModifica, horaActual);
						ps4.setString(posUsuarioModifica, usuarioModifica.getLoginUsuario());
						ps4.setInt(posInformeXEspecialiad, informeQxEspecialidadDto.getCodigo());
						
						resultado=ps4.executeUpdate();
						
						if(resultado!=1){
							throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
						}
					}
				}
				
				StringBuffer delete=new StringBuffer("delete from salascirugia.diag_post_opera_sol_cx ") 
					.append("where cod_informe_especialidad = ? AND principal = ? AND complicacion = ? AND diagnostico = ? AND tipo_cie = ? ");
				cadena = delete.toString();
				ps5=connection.prepareStatement(cadena);
				ps5.setInt(1, informeQxEspecialidadDto.getCodigo());
				ps5.setBoolean(2, false);
				ps5.setBoolean(3, false);
				
				for(DtoDiagnostico diagnostico:dxRelacionadosEliminar){
					ps5.setString(4, diagnostico.getAcronimoDiagnostico());
					ps5.setInt(5, diagnostico.getTipoCieDiagnosticoInt());
					
					resultado=ps5.executeUpdate();
					
				}
			}else{
				StringBuffer delete=new StringBuffer("delete from salascirugia.diag_post_opera_sol_cx ") 
					.append("where cod_informe_especialidad = ? AND principal = ? AND complicacion = ? ");
				
				cadena = delete.toString();
				ps4=connection.prepareStatement(cadena);
				ps4.setInt(1, informeQxEspecialidadDto.getCodigo());
				ps4.setBoolean(2, false);
				ps4.setBoolean(3, false);
				
				resultado=ps4.executeUpdate();
			}
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
			UtilidadBD.cerrarObjetosPersistencia(ps1, null, null);
			UtilidadBD.cerrarObjetosPersistencia(ps2, null, null);
			UtilidadBD.cerrarObjetosPersistencia(ps3, null, null);
			UtilidadBD.cerrarObjetosPersistencia(ps4, null, null);
			UtilidadBD.cerrarObjetosPersistencia(ps5, null, null);
		}
	}

	/**
	 * Consulta los profesionales relacionados a un servicio de una cirugia
	 * 
	 * @param connection
	 * @param servicioHQxDto
	 * @return lista de profesionales
	 * @throws BDException
	 * @author jeilones
	 * @created 8/07/2013
	 */
	private static List<ProfesionalHQxDto> consultarProfesionalesXServicio(Connection connection,ServicioHQxDto servicioHQxDto) throws BDException{
		List<ProfesionalHQxDto> profesionalesXServicio=new ArrayList<ProfesionalHQxDto>(0);
		StringBuffer consulta=new StringBuffer("select pc.consecutivo,pc.cobrable,ta.codigo as codigo_asocio,ta.nombre_asocio, ")
			.append("per.codigo AS codigo_medico,per.primer_nombre,per.segundo_nombre,per.primer_apellido, per.segundo_apellido, ")
			.append("esp.codigo AS codigo_especialidad,esp.nombre AS nombre_especialidad ")
			.append("from salascirugia.sol_cirugia_por_servicio scxs ")
			.append("inner join salascirugia.profesionales_cirugia pc on pc.cod_sol_cx_serv=scxs.codigo ")
			.append("inner join administracion.personas per on per.codigo=pc.codigo_profesional ")
			.append("inner join administracion.especialidades esp on esp.codigo=pc.especialidad ")
			.append("inner join salascirugia.tipos_asocio ta on ta.codigo=pc.tipo_asocio ")
			.append("where scxs.codigo = ? ");
		PreparedStatement ps = null;
		ResultSet rs = null;
		String cadena=consulta.toString();
		try {
			ps=connection.prepareStatement(cadena);
			ps.setInt(1, servicioHQxDto.getCodigoServicioXCirugia());
			rs=ps.executeQuery();
			
			while(rs.next()){
				ProfesionalHQxDto profesionalHQxDto=new ProfesionalHQxDto();
				
				profesionalHQxDto.setIdProfesionalXServicio(rs.getInt("consecutivo"));
				profesionalHQxDto.setIdMedico(rs.getInt("codigo_medico"));
				profesionalHQxDto.setPrimerNombre(rs.getString("primer_nombre"));
				profesionalHQxDto.setSegundoNombre(rs.getString("segundo_nombre"));
				profesionalHQxDto.setPrimerApellido(rs.getString("primer_apellido"));
				profesionalHQxDto.setSegundoApellido(rs.getString("segundo_apellido"));
				
				profesionalHQxDto.setNombreCompleto
				(
					(UtilidadCadena.noEsVacio(profesionalHQxDto.getPrimerApellido())	? profesionalHQxDto.getPrimerApellido()		: "")+ " " +
					(UtilidadCadena.noEsVacio(profesionalHQxDto.getSegundoApellido())	? profesionalHQxDto.getSegundoApellido()	: "")+ " " +
					(UtilidadCadena.noEsVacio(profesionalHQxDto.getPrimerNombre())		? profesionalHQxDto.getPrimerNombre() 		: "")+ " " +
					(UtilidadCadena.noEsVacio(profesionalHQxDto.getSegundoNombre())		? profesionalHQxDto.getSegundoNombre() 		: "")
				);
				
				profesionalHQxDto.setCobrable(UtilidadTexto.getBoolean(rs.getString("cobrable")));
				
				TipoProfesionalDto tipoProfesionalDto=new TipoProfesionalDto();
				tipoProfesionalDto.setCodigo(rs.getInt("codigo_asocio"));
				tipoProfesionalDto.setNombreAsocio(rs.getString("nombre_asocio"));
				profesionalHQxDto.setTipoProfesional(tipoProfesionalDto);
				
				EspecialidadDto especialidadDto=new EspecialidadDto();
				especialidadDto.setCodigo(rs.getInt("codigo_especialidad"));
				especialidadDto.setNombre(rs.getString("nombre_especialidad"));
				profesionalHQxDto.setEspecialidad(especialidadDto);
				
				profesionalesXServicio.add(profesionalHQxDto);
			}
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return profesionalesXServicio;
	}
	
	/**
	 * Consulta los profesionales relacionados a una especialidad que intervino en una cirugia
	 * 
	 * @param connection
	 * @param servicioHQxDto
	 * @return lista de profesionales
	 * @throws BDException
	 * @author jeilones
	 * @created 8/07/2013
	 */
	public static List<ProfesionalHQxDto> consultarProfesionalesXEspecialidad(Connection connection,int numeroSolicitud, int codigoEspecialidad) throws BDException{
		List<ProfesionalHQxDto>profesionalesXEspecialidad=new ArrayList<ProfesionalHQxDto>(0);
		StringBuffer consulta=new StringBuffer("select ")
			.append("prof_esp.codigo AS consecutivo,prof_esp.cobrable,ta.codigo as codigo_asocio,ta.nombre_asocio, ")
			.append("per.codigo as codigo_medico,per.primer_nombre,per.segundo_nombre,per.primer_apellido, per.segundo_apellido, ")
			.append("esp.codigo AS codigo_especialidad,esp.nombre AS nombre_especialidad ")
			.append("from salascirugia.informe_qx_por_especialidad iqxe ")
			.append("inner join salascirugia.solicitudes_cirugia sc on sc.numero_solicitud=iqxe.codigo_solicitudes_cirugia ")
			.append("inner join salascirugia.prof_cx_por_especialidad prof_esp on prof_esp.cod_informe_especialidad = iqxe.codigo ")
			.append("inner join administracion.personas per on per.codigo=prof_esp.codigo_profesional ")
			.append("inner join administracion.especialidades esp on esp.codigo=prof_esp.especialidad ")
			.append("inner join salascirugia.tipos_asocio ta on ta.codigo=prof_esp.tipo_asocio ")
			.append("where sc.numero_solicitud= ? ")
			.append("and iqxe.especialidad = ? ");
		
		String cadena=consulta.toString();
		PreparedStatement ps = null;
		ResultSet rs= null;
		try {
			ps=connection.prepareStatement(cadena);
			ps.setInt(1, numeroSolicitud);
			ps.setInt(2, codigoEspecialidad);
			
			rs=ps.executeQuery();
			
			while(rs.next()){
				ProfesionalHQxDto profesionalHQxDto=new ProfesionalHQxDto();
				
				profesionalHQxDto.setIdProfesionalXEspecialidad(rs.getInt("consecutivo"));
				profesionalHQxDto.setIdMedico(rs.getInt("codigo_medico"));
				profesionalHQxDto.setPrimerNombre(rs.getString("primer_nombre"));
				profesionalHQxDto.setSegundoNombre(rs.getString("segundo_nombre"));
				profesionalHQxDto.setPrimerApellido(rs.getString("primer_apellido"));
				profesionalHQxDto.setSegundoApellido(rs.getString("segundo_apellido"));
				
				profesionalHQxDto.setNombreCompleto
				(
					(UtilidadCadena.noEsVacio(profesionalHQxDto.getPrimerApellido())	? profesionalHQxDto.getPrimerApellido()		: "")+ " " +
					(UtilidadCadena.noEsVacio(profesionalHQxDto.getSegundoApellido())	? profesionalHQxDto.getSegundoApellido()	: "")+ " " +
					(UtilidadCadena.noEsVacio(profesionalHQxDto.getPrimerNombre())		? profesionalHQxDto.getPrimerNombre() 		: "")+ " " +
					(UtilidadCadena.noEsVacio(profesionalHQxDto.getSegundoNombre())		? profesionalHQxDto.getSegundoNombre() 		: "")
				);
				
				profesionalHQxDto.setCobrable(UtilidadTexto.getBoolean(rs.getString("cobrable")));
				
				TipoProfesionalDto tipoProfesionalDto=new TipoProfesionalDto();
				tipoProfesionalDto.setCodigo(rs.getInt("codigo_asocio"));
				tipoProfesionalDto.setNombreAsocio(rs.getString("nombre_asocio"));
				profesionalHQxDto.setTipoProfesional(tipoProfesionalDto);
				
				EspecialidadDto especialidadDto=new EspecialidadDto();
				especialidadDto.setCodigo(rs.getInt("codigo_especialidad"));
				especialidadDto.setNombre(rs.getString("nombre_especialidad"));
				profesionalHQxDto.setEspecialidad(especialidadDto);
				
				profesionalesXEspecialidad.add(profesionalHQxDto);
			}
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return profesionalesXEspecialidad;
	}
	
	/**
	 * Consulta otros profesionales relacionados que participan en una cirugia
	 * 
	 * @param connection
	 * @param numeroSolicitud
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 9/07/2013
	 */
	public static List<ProfesionalHQxDto> consultarOtrosProfesionalesInfoQx(Connection connection,int numeroSolicitud) throws BDException{
		List<ProfesionalHQxDto>profesionalesXEspecialidad=new ArrayList<ProfesionalHQxDto>(0);
		StringBuffer consulta=new StringBuffer("select piqx.consecutivo   as consecutivo, ")  
			.append("ta.codigo as codigo_asocio,ta.nombre_asocio, ")
			.append("per.codigo as codigo_medico, ")
			.append("per.primer_nombre , ")
			.append("per.segundo_nombre, ")
			.append("per.primer_apellido, ")
			.append("per.segundo_apellido ")
			.append("from salascirugia.profesionales_info_qx piqx ")  
			.append("inner join administracion.personas per on per.codigo=piqx.codigo_profesional ")
			.append("inner join salascirugia.tipos_asocio ta on ta.codigo=piqx.tipo_asocio ")
			.append("where piqx.numero_solicitud = ? ");
		
		String cadena=consulta.toString();
		PreparedStatement ps = null;
		ResultSet rs= null;
		try {
			ps=connection.prepareStatement(cadena);
			ps.setInt(1, numeroSolicitud);
			
			rs=ps.executeQuery();
			
			while(rs.next()){
				ProfesionalHQxDto profesionalHQxDto=new ProfesionalHQxDto();
				profesionalHQxDto.setEsOtroProfesionalEspecialidad(true);
				
				profesionalHQxDto.setIdProfesionalInfoQx(rs.getInt("consecutivo"));
				profesionalHQxDto.setIdMedico(rs.getInt("codigo_medico"));
				profesionalHQxDto.setPrimerNombre(rs.getString("primer_nombre"));
				profesionalHQxDto.setSegundoNombre(rs.getString("segundo_nombre"));
				profesionalHQxDto.setPrimerApellido(rs.getString("primer_apellido"));
				profesionalHQxDto.setSegundoApellido(rs.getString("segundo_apellido"));
				
				profesionalHQxDto.setNombreCompleto
				(
					(UtilidadCadena.noEsVacio(profesionalHQxDto.getPrimerApellido())	? profesionalHQxDto.getPrimerApellido()		: "")+ " " +
					(UtilidadCadena.noEsVacio(profesionalHQxDto.getSegundoApellido())	? profesionalHQxDto.getSegundoApellido()	: "")+ " " +
					(UtilidadCadena.noEsVacio(profesionalHQxDto.getPrimerNombre())		? profesionalHQxDto.getPrimerNombre() 		: "")+ " " +
					(UtilidadCadena.noEsVacio(profesionalHQxDto.getSegundoNombre())		? profesionalHQxDto.getSegundoNombre() 		: "")
				);
				
				TipoProfesionalDto tipoProfesionalDto=new TipoProfesionalDto();
				tipoProfesionalDto.setCodigo(rs.getInt("codigo_asocio"));
				tipoProfesionalDto.setNombreAsocio(rs.getString("nombre_asocio"));
				profesionalHQxDto.setTipoProfesional(tipoProfesionalDto);
				
				profesionalesXEspecialidad.add(profesionalHQxDto);
			}
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return profesionalesXEspecialidad;
	}
	

	/**
	 * Consulta los tipos de asocio que se pueden usar para relacionar un profesional a una cirugia
	 * 
	 * @param connection
	 * @param codigoInstitucion
	 * @return tipos de profesional (tipos de asocio)
	 * @throws BDException
	 * @author jeilones
	 * @created 10/07/2013
	 */
	public static List<TipoProfesionalDto> consultarTiposProfesionales(Connection connection,int codigoInstitucion) throws BDException{
		List<TipoProfesionalDto>tiposProfesionales=new ArrayList<TipoProfesionalDto>(0);
		StringBuffer consulta=new StringBuffer("SELECT " )
			.append("ta.codigo , " )
			.append("ta.nombre_asocio " )
			.append(" FROM salascirugia.tipos_asocio ta" )
			.append(" WHERE  ta.institucion=? AND ta.tipos_servicio = ? AND ta.participa_cir = ? ")
			/** 
			 * MT 7947, se pide que no se pueda consultar los profesionales que sean tipo honorario anestesia.
			 * 
			 * No es la solucion mas conveniente ya que la tabla es parametrica y la descripcion puede tener cualquier valor
			 */
			.append(" AND UPPER(ta.nombre_asocio) not like ? ")
			.append("ORDER BY ta.nombre_asocio asc ");
		PreparedStatement ps= null;
		String cadena=consulta.toString();
		try {
			ps=connection.prepareStatement(cadena);
			
			ps.setInt(1, codigoInstitucion);
			ps.setString(2, ConstantesBD.codigoServicioHonorariosCirugia+"");
			ps.setString(3, ConstantesBD.acronimoSi);
			ps.setString(4, "%"+ConstantesBD.nombreHonorarioServicioAnestesiaCirugia.toUpperCase()+"%");
			
			ResultSet rs=ps.executeQuery();
			
			while(rs.next()){
				TipoProfesionalDto tipoProfesionalDto=new TipoProfesionalDto();
				tipoProfesionalDto.setCodigo(rs.getInt("codigo"));
				tipoProfesionalDto.setNombreAsocio(rs.getString("nombre_asocio"));
				
				tiposProfesionales.add(tipoProfesionalDto);
			}
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
		return tiposProfesionales;
	}
	
	/**
	 * Guarda los profesionales que participan por servicio, por especialidad y otros profesionales relacionados al acto Qx
	 * 
	 * @param connection
	 * @param peticionQxDto
	 * @param informeQxDto
	 * @param usuarioBasico
	 * @throws BDException
	 * @author jeilones
	 * @created 12/07/2013
	 */
	public static void guardarProfesionalesInformeQx(Connection connection,PeticionQxDto peticionQxDto,InformeQxDto informeQxDto, UsuarioBasico usuarioBasico) throws BDException{
		StringBuffer insert=new StringBuffer("insert into salascirugia.profesionales_info_qx ")
			.append("(consecutivo,numero_solicitud,tipo_asocio,codigo_profesional, ")
			.append("fecha_modifica,hora_modifica,usuario_modifica,cobrable) ")
			.append("values(?,?,?,?, ")
			.append("?,?,?,?)");
		
		StringBuffer update=new StringBuffer("update salascirugia.profesionales_info_qx ")
			.append("set numero_solicitud = ?,tipo_asocio = ?,codigo_profesional = ?, ")
			.append("fecha_modifica = ?,hora_modifica = ?,usuario_modifica = ?,cobrable = ? ")
			.append("where consecutivo = ?");
		
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		try {
			
			eliminarProfesionalesInformeQx(connection, informeQxDto);
			
			String cadena=insert.toString();
			ps=connection.prepareStatement(cadena);
			
			cadena=update.toString();
			ps1=connection.prepareStatement(cadena);
			
			int posSecuencia=1;
			int posNumeroSolicitud=2;
			int posTipoAsocio=3;
			int posCodigoMedico=4;
			int posFechaModifica=5;
			int posHoraModifica=6;
			int posUsuarioModicia=7;
			int posCobrable=8;
			
			int secuencia=0;
			int resultado=0;
			
			java.sql.Date fechaActual=new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(connection).getTime());
			String horaActual=UtilidadFecha.getHoraActual(connection);
			
			List<ProfesionalHQxDto>otrosProfesionalesActuales=consultarOtrosProfesionalesInfoQx(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
			
			for (ProfesionalHQxDto profesionalHQxDto : informeQxDto.getOtrosProfesionales()) {
				boolean existe=false;
				for (ProfesionalHQxDto profesionalActual : otrosProfesionalesActuales) {
					if(profesionalActual.getTipoProfesional().getCodigo()==profesionalHQxDto.getTipoProfesional().getCodigo()
							&&profesionalActual.getIdMedico()==profesionalHQxDto.getIdMedico()){
						profesionalHQxDto.setIdProfesionalInfoQx(profesionalActual.getIdProfesionalInfoQx());
						existe=true;
						break;
					}
				}
				
				if(!existe){
					posSecuencia=1;
					posNumeroSolicitud=2;
					posTipoAsocio=3;
					posCodigoMedico=4;
					posFechaModifica=5;
					posHoraModifica=6;
					posUsuarioModicia=7;
					posCobrable=8;
					
					secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(connection, "salascirugia.seq_profesionales_info_qx");
					profesionalHQxDto.setIdProfesionalInfoQx(secuencia);
					
					ps.setInt(posSecuencia, secuencia);
					ps.setInt(posNumeroSolicitud, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
					ps.setInt(posTipoAsocio, profesionalHQxDto.getTipoProfesional().getCodigo());
					ps.setInt(posCodigoMedico, profesionalHQxDto.getIdMedico());
					ps.setDate(posFechaModifica, fechaActual);
					ps.setString(posHoraModifica, horaActual);
					ps.setString(posUsuarioModicia, usuarioBasico.getLoginUsuario());
					
					if(profesionalHQxDto.isCobrable()){
						ps.setString(posCobrable, ConstantesBD.acronimoSi);
					}else{
						ps.setString(posCobrable, ConstantesBD.acronimoNo);
					}
					
					resultado=ps.executeUpdate();
					
				}else{
					posNumeroSolicitud=1;
					posTipoAsocio=2;
					posCodigoMedico=3;
					posFechaModifica=4;
					posHoraModifica=5;
					posUsuarioModicia=6;
					posCobrable=7;
					posSecuencia=8;
					
					ps1.setInt(posSecuencia, profesionalHQxDto.getIdProfesionalInfoQx());
					ps1.setInt(posNumeroSolicitud, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud());
					ps1.setInt(posTipoAsocio, profesionalHQxDto.getTipoProfesional().getCodigo());
					ps1.setInt(posCodigoMedico, profesionalHQxDto.getIdMedico());
					ps1.setDate(posFechaModifica, fechaActual);
					ps1.setString(posHoraModifica, horaActual);
					ps1.setString(posUsuarioModicia, usuarioBasico.getLoginUsuario());
					
					if(profesionalHQxDto.isCobrable()){
						ps1.setString(posCobrable, ConstantesBD.acronimoSi);
					}else{
						ps1.setString(posCobrable, ConstantesBD.acronimoNo);
					}
					
					resultado=ps1.executeUpdate();
				}
				
				if(resultado!=1){
					throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
				}
			}
			
			guardarProfesionalesXEspecialidad(connection, peticionQxDto, informeQxDto.getInformeQxEspecialidad(), usuarioBasico);
						
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
			UtilidadBD.cerrarObjetosPersistencia(ps1, null, null);
		}
		
	}
	
	/**
	 * Elimina los otros profesionales relacionados al acto Qx
	 * 
	 * @param connection
	 * @param informeQxDto
	 * @throws BDException
	 * @author jeilones
	 * @created 12/07/2013
	 */
	private static void eliminarProfesionalesInformeQx(Connection connection,InformeQxDto informeQxDto) throws BDException{
		StringBuffer deleteProfesionales=new StringBuffer("DELETE FROM salascirugia.profesionales_info_qx where consecutivo = ? ");
		PreparedStatement ps= null;
		try {
			String cadena=deleteProfesionales.toString();
			ps=connection.prepareStatement(cadena);
			
			for(ProfesionalHQxDto profesionalHQxDto:informeQxDto.getOtrosProfesionalesAEliminarse()){
				ps.setInt(1, profesionalHQxDto.getIdProfesionalInfoQx());
				
				ps.executeUpdate();
			}
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
		}
	}
	
	/**
	 * Guarda los profesionales por especialidad que participan en el acto Qx
	 * 
	 * @param connection
	 * @param peticionQxDto
	 * @param informeQxEspecialidadDto
	 * @param usuarioBasico
	 * @throws BDException
	 * @author jeilones
	 * @created 12/07/2013
	 */
	private static void guardarProfesionalesXEspecialidad(Connection connection,PeticionQxDto peticionQxDto, InformeQxEspecialidadDto informeQxEspecialidadDto, UsuarioBasico usuarioBasico) throws BDException{
		StringBuffer insert=new StringBuffer("insert into salascirugia.prof_cx_por_especialidad ")
			.append("(codigo,especialidad,codigo_profesional,cod_informe_especialidad, ")
			.append("usuario_modifica,fecha_modifica,hora_modifica,tipo_asocio,cobrable ) ")
			.append("values (?,?,?,?, ")
			.append("?,?,?,?,?)");
		
		StringBuffer update=new StringBuffer("update salascirugia.prof_cx_por_especialidad ")
			.append("set especialidad = ?,codigo_profesional = ?,cod_informe_especialidad = ?, ")
			.append("usuario_modifica = ?,fecha_modifica = ?,hora_modifica = ?,tipo_asocio = ?,cobrable = ? ")
			.append("where codigo = ? ");
		
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		try {
			
			eliminarProfesionalesXEspecialidad(connection, informeQxEspecialidadDto);
			
			String cadena=insert.toString();
			ps= connection.prepareStatement(cadena);
			
			cadena=update.toString();
			ps1= connection.prepareStatement(cadena);
			
			int posSecuencia=1;
			int posEspecialidad=2;
			int posCodigoMedico=3;
			int posCodigoInformeQxXEspecialidad=4;
			int posUsuarioModifica=5;
			int posFechaModifica=6;
			int posHoraModifica=7;
			int posTipoAsocio=8;
			int posCobrable=9;
			
			java.sql.Date fechaActual=new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(connection).getTime());
			String horaActual=UtilidadFecha.getHoraActual(connection);
			
			List<ProfesionalHQxDto>profesionalesActuales=consultarProfesionalesXEspecialidad(connection, peticionQxDto.getSolicitudCirugia().getNumeroSolicitud(), informeQxEspecialidadDto.getEspecialidad().getCodigo());
			
			int secuencia=0;
			int resultado=0;
			
			for(ProfesionalHQxDto profesionalHQxDto:informeQxEspecialidadDto.getProfesionales()){
				boolean existe=false;
				for (ProfesionalHQxDto profesionalActual : profesionalesActuales) {
					if(profesionalActual.getTipoProfesional().getCodigo()==profesionalHQxDto.getTipoProfesional().getCodigo()
							&&profesionalActual.getIdMedico()==profesionalHQxDto.getIdMedico()){
						
						profesionalHQxDto.setIdProfesionalXEspecialidad(profesionalActual.getIdProfesionalXEspecialidad());
						
						existe=true;
						break;
					}
				}
				if(!existe){
					posSecuencia=1;
					posEspecialidad=2;
					posCodigoMedico=3;
					posCodigoInformeQxXEspecialidad=4;
					posUsuarioModifica=5;
					posFechaModifica=6;
					posHoraModifica=7;
					posTipoAsocio=8;
					posCobrable=9;
					
					secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(connection, "salascirugia.seq_prof_cx_por_esp");
					profesionalHQxDto.setIdProfesionalXEspecialidad(secuencia);
					
					ps.setInt(posSecuencia, secuencia);
					ps.setInt(posEspecialidad, profesionalHQxDto.getEspecialidad().getCodigo());
					ps.setInt(posCodigoMedico, profesionalHQxDto.getIdMedico());
					ps.setInt(posCodigoInformeQxXEspecialidad, informeQxEspecialidadDto.getCodigo());
					ps.setString(posUsuarioModifica, usuarioBasico.getLoginUsuario());
					ps.setDate(posFechaModifica, fechaActual);
					ps.setString(posHoraModifica, horaActual);
					ps.setInt(posTipoAsocio, profesionalHQxDto.getTipoProfesional().getCodigo());
					
					ps.setBoolean(posCobrable, profesionalHQxDto.isCobrable());
					
					resultado=ps.executeUpdate();
					
				}else{
					
					posEspecialidad=1;
					posCodigoMedico=2;
					posCodigoInformeQxXEspecialidad=3;
					posUsuarioModifica=4;
					posFechaModifica=5;
					posHoraModifica=6;
					posTipoAsocio=7;
					posCobrable=8;
					posSecuencia=9;
					
					ps1.setInt(posSecuencia, profesionalHQxDto.getIdProfesionalXEspecialidad());
					ps1.setInt(posEspecialidad, profesionalHQxDto.getEspecialidad().getCodigo());
					ps1.setInt(posCodigoMedico, profesionalHQxDto.getIdMedico());
					ps1.setInt(posCodigoInformeQxXEspecialidad, informeQxEspecialidadDto.getCodigo());
					ps1.setString(posUsuarioModifica, usuarioBasico.getLoginUsuario());
					ps1.setDate(posFechaModifica, fechaActual);
					ps1.setString(posHoraModifica, horaActual);
					ps1.setInt(posTipoAsocio, profesionalHQxDto.getTipoProfesional().getCodigo());
					
					ps1.setBoolean(posCobrable, profesionalHQxDto.isCobrable());
					
					resultado=ps1.executeUpdate();
				}
				
				if(resultado!=1){
					throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
				}
			}
			
			for (ServicioHQxDto servicioHQxDto : informeQxEspecialidadDto.getServicios()) {
				guardarProfesionalesXServicio(connection, servicioHQxDto, usuarioBasico, informeQxEspecialidadDto.getEspecialidad());
			} 
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
			UtilidadBD.cerrarObjetosPersistencia(ps1, null, null);
		}
	}
	
	/**
	 * Elimina los profesionales por especialidad que participan en el acto Qx
	 * 
	 * @param connection
	 * @param informeQxEspecialidadDto
	 * @throws BDException
	 * @author jeilones
	 * @created 12/07/2013
	 */
	private static void eliminarProfesionalesXEspecialidad(Connection connection,InformeQxEspecialidadDto informeQxEspecialidadDto) throws BDException{
		StringBuffer deleteProfesionales=new StringBuffer("DELETE FROM salascirugia.prof_cx_por_especialidad where codigo = ? ");
		PreparedStatement ps= null;
		try {
			String cadena=deleteProfesionales.toString();
			ps=connection.prepareStatement(cadena);
			
			for(ProfesionalHQxDto profesionalHQxDto:informeQxEspecialidadDto.getProfesionalesAEliminarse()){
				ps.setInt(1, profesionalHQxDto.getIdProfesionalXEspecialidad());
				
				ps.executeUpdate();
			}
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
		}
	}
	
	/**
	 * Guarda los profesionales por servicio que participan en el acto Qx
	 * 
	 * @param connection
	 * @param servicioHQxDto
	 * @param usuarioBasico
	 * @param especialidadDto
	 * @throws BDException
	 * @author jeilones
	 * @created 12/07/2013
	 */
	private static void guardarProfesionalesXServicio(Connection connection,ServicioHQxDto servicioHQxDto,UsuarioBasico usuarioBasico,EspecialidadDto especialidadDto) throws BDException{
		StringBuffer insert=new StringBuffer("insert into salascirugia.profesionales_cirugia ")
			.append("(consecutivo,cod_sol_cx_serv,tipo_asocio,especialidad,codigo_profesional, ")
			.append("cobrable,fecha_modifica,hora_modifica,usuario_modifica,pool) ")
			.append("values(?,?,?,?,?, ")
			.append("?,?,?,?,?) ");
		
		StringBuffer update=new StringBuffer("update salascirugia.profesionales_cirugia ")
			.append("set cod_sol_cx_serv = ?,tipo_asocio = ? ,especialidad = ?, codigo_profesional = ?, ")
			.append("cobrable = ?, fecha_modifica = ?, hora_modifica = ?, usuario_modifica = ? ")
			.append("where consecutivo = ? ");
		
		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		try {
			
			eliminarProfesionalesXServicio(connection, servicioHQxDto);
			
			String cadena=insert.toString();
			ps=connection.prepareStatement(cadena);
			
			cadena=update.toString();
			ps1=connection.prepareStatement(cadena);
			
			java.sql.Date fechaActual=new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(connection).getTime());
			String horaActual=UtilidadFecha.getHoraActual(connection);
			
			int posSecuencia=1;
			int posSolCirXServicio=2;
			int posTipoAsocio=3;
			int posEspecialidad=4;
			int posCodigoMedico=5;
			int posCobrable=6;
			int posFechaModifica=7;
			int posHoraModifica=8;
			int posUsuarioModifica=9;
			int posPool=10;
			
			int secuencia=0;
			
			List<ProfesionalHQxDto>profesionalesActuales=consultarProfesionalesXServicio(connection, servicioHQxDto);
			
			int resultado = 0;
			
			for(ProfesionalHQxDto profesionalHQxDto:servicioHQxDto.getProfesionalesXServicio()){
				boolean existe=false;
				for (ProfesionalHQxDto profesionalActual : profesionalesActuales) {
					if(profesionalActual.getTipoProfesional().getCodigo()==profesionalHQxDto.getTipoProfesional().getCodigo()
							&&profesionalActual.getIdMedico()==profesionalHQxDto.getIdMedico()){
						profesionalHQxDto.setIdProfesionalXServicio(profesionalActual.getIdProfesionalXServicio());
						existe=true;
						break;
					}
				}
				if(!existe){
					
					posSecuencia=1;
					posSolCirXServicio=2;
					posTipoAsocio=3;
					posEspecialidad=4;
					posCodigoMedico=5;
					posCobrable=6;
					posFechaModifica=7;
					posHoraModifica=8;
					posUsuarioModifica=9;
					posPool=10;
					
					secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(connection, "salascirugia.seq_profesionales_cirugia");
					profesionalHQxDto.setIdProfesionalXServicio(secuencia);
					
					ps.setInt(posSecuencia, secuencia);
					ps.setInt(posSolCirXServicio, servicioHQxDto.getCodigoServicioXCirugia());
					ps.setInt(posTipoAsocio, profesionalHQxDto.getTipoProfesional().getCodigo());
					ps.setInt(posEspecialidad, especialidadDto.getCodigo());
					ps.setInt(posCodigoMedico, profesionalHQxDto.getIdMedico());
					
					if(profesionalHQxDto.isCobrable()){
						ps.setString(posCobrable, ConstantesBD.acronimoSi);
					}else{
						ps.setString(posCobrable, ConstantesBD.acronimoNo);
					}
					
					ps.setDate(posFechaModifica, fechaActual);
					ps.setString(posHoraModifica, horaActual);
					ps.setString(posUsuarioModifica, usuarioBasico.getLoginUsuario());	
					ps.setNull(posPool, Types.INTEGER);
					
					resultado=ps.executeUpdate();
				}else{
					
					cadena=update.toString();
					ps1=connection.prepareStatement(cadena);
					
					posSolCirXServicio=1;
					posTipoAsocio=2;
					posEspecialidad=3;
					posCodigoMedico=4;
					posCobrable=5;
					posFechaModifica=6;
					posHoraModifica=7;
					posUsuarioModifica=8;
					//posPool=9;
					posSecuencia=9;
					
					secuencia=profesionalHQxDto.getIdProfesionalXServicio();
					
					ps1.setInt(posSecuencia, secuencia);
					ps1.setInt(posSolCirXServicio, servicioHQxDto.getCodigoServicioXCirugia());
					ps1.setInt(posTipoAsocio, profesionalHQxDto.getTipoProfesional().getCodigo());
					ps1.setInt(posEspecialidad, especialidadDto.getCodigo());
					ps1.setInt(posCodigoMedico, profesionalHQxDto.getIdMedico());
					
					if(profesionalHQxDto.isCobrable()){
						ps1.setString(posCobrable, ConstantesBD.acronimoSi);
					}else{
						ps1.setString(posCobrable, ConstantesBD.acronimoNo);
					}
					
					ps1.setDate(posFechaModifica, fechaActual);
					ps1.setString(posHoraModifica, horaActual);
					ps1.setString(posUsuarioModifica, usuarioBasico.getLoginUsuario());	
					//ps1.setNull(posPool, Types.INTEGER);
					
					resultado=ps1.executeUpdate();
				}
				
				
				if(resultado!=1){
					throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
				}
			}
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
			UtilidadBD.cerrarObjetosPersistencia(ps1, null, null);
		}
		
	}
	
	/**
	 * Elimina los profesionales por servicio que participan en el acto Qx
	 * 
	 * @param connection
	 * @param servicioHQxDto
	 * @throws BDException
	 * @author jeilones
	 * @created 12/07/2013
	 */
	private static void eliminarProfesionalesXServicio(Connection connection,ServicioHQxDto servicioHQxDto) throws BDException{
		StringBuffer deleteProfesionales=new StringBuffer("DELETE FROM salascirugia.profesionales_cirugia where consecutivo = ? ");
		PreparedStatement ps= null;
		try {
			String cadena=deleteProfesionales.toString();
			ps=connection.prepareStatement(cadena);
			
			for(ProfesionalHQxDto profesionalHQxDto:servicioHQxDto.getProfesionalesAEliminarse()){
				ps.setInt(1, profesionalHQxDto.getIdProfesionalXServicio());
				
				ps.executeUpdate();
			}
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
		}
	}
	
	/**
	 * Metodo que Consulta seccion de ingreso / salida paciente de la hoja quirurgica dado un numero de Solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return IngresoSalidaPacienteDto dto
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	public static IngresoSalidaPacienteDto consultarIngresoSalidaPaciente(Connection con,int numeroSolicitud) throws BDException {
		
		IngresoSalidaPacienteDto dto = new IngresoSalidaPacienteDto();
		StringBuffer consulta=null;
		boolean existeHojaAnestesia=existeHojaAnestesia(con,numeroSolicitud);
		
		if(existeHojaAnestesia){
		
			consulta=new StringBuffer(" WITH hoja_anestesia_temp as (SELECT numero_solicitud, fecha_inicia_anestesia,hora_inicia_anestesia,fecha_finaliza_anestesia,hora_finaliza_anestesia ")
															.append(" FROM hoja_anestesia ") 
															.append(" WHERE numero_solicitud=? ) ")
																		
										.append(" SELECT hq.numero_solicitud, ts.codigo as cod_tipo_sala, ts.descripcion as desc_tipo_sala, ts.institucion as institucion_tipo_sala, ")
										.append(" s.consecutivo as consecutivo_sala, s.codigo as cod_sala, s.descripcion as desc_sala, s.institucion as institucion_sala, s.centro_atencion as centro_atencion_sala,")
											
										.append(" CASE WHEN hat.fecha_inicia_anestesia IS NOT NULL ")
										.append(" THEN hat.fecha_inicia_anestesia ")
										.append(" ELSE sc.fecha_ingreso_sala ")
										.append(" END as fecha_ingreso_sala, ")
										
										.append(" CASE WHEN hat.hora_inicia_anestesia IS NOT NULL ")
										.append(" THEN hat.hora_inicia_anestesia ")
										.append(" ELSE sc.hora_ingreso_sala ")
										.append(" END as hora_ingreso_sala, ")
										
										.append(" CASE WHEN hat.fecha_finaliza_anestesia IS NOT NULL ")
										.append(" THEN hat.fecha_finaliza_anestesia ")
										.append(" ELSE sc.fecha_salida_sala ")
										.append(" END as fecha_salida_sala, ")
										
										.append(" CASE WHEN hat.hora_finaliza_anestesia IS NOT NULL ")
										.append(" THEN hat.hora_finaliza_anestesia ")
										.append(" ELSE sc.hora_salida_sala ")
										.append(" END as hora_salida_sala, ")
										
										.append(" hq.fecha_ini_anestesia as fecha_ini_anestesia, hq.hora_ini_anestesia as hora_ini_anestesia, hq.fecha_fin_anestesia as fecha_fin_anestesia, hq.hora_fin_anestesia as hora_fin_anestesia, ")
										.append(" sc.fecha_inicial_cx as fecha_inicial_cx, sc.hora_inicial_cx as hora_inicial_cx, sc.fecha_final_cx as fecha_final_cx, sc.hora_final_cx as hora_final_cx, ")
										.append(" sc.duracion_final_cx as duracion_final_cx, ")
										.append(" hq.finalizada as finalizada, ")
										.append(" hq.ha_sido_reversada as ha_sido_reversada ")
										.append(" FROM hoja_anestesia_temp hat, salascirugia.hoja_quirurgica hq ")
										.append(" INNER JOIN salascirugia.solicitudes_cirugia sc on(sc.numero_solicitud=hq.numero_solicitud) ")
										.append(" LEFT JOIN salascirugia.salas s on(hq.sala=s.consecutivo) ")
										.append(" LEFT JOIN salascirugia.tipos_salas ts ON (ts.codigo = hq.tipo_sala)")
										.append(" WHERE hq.numero_solicitud=? ");
		}else{
			
			consulta=new StringBuffer(" SELECT hq.numero_solicitud as numero_solicitud, ts.codigo as cod_tipo_sala, ts.descripcion as desc_tipo_sala, ts.institucion as institucion_tipo_sala, ")
									.append(" s.consecutivo as consecutivo_sala, s.codigo as cod_sala, s.descripcion as desc_sala, s.institucion as institucion_sala, s.centro_atencion as centro_atencion_sala, ")
								
									.append(" sc.fecha_ingreso_sala as fecha_ingreso_sala, sc.hora_ingreso_sala as hora_ingreso_sala, sc.fecha_salida_sala as fecha_salida_sala, sc.hora_salida_sala as hora_salida_sala, ")
									.append(" hq.fecha_ini_anestesia as fecha_ini_anestesia, hq.hora_ini_anestesia as hora_ini_anestesia, hq.fecha_fin_anestesia as fecha_fin_anestesia, hq.hora_fin_anestesia as hora_fin_anestesia, ")
									.append(" sc.fecha_inicial_cx as fecha_inicial_cx, sc.hora_inicial_cx as hora_inicial_cx, sc.fecha_final_cx as fecha_final_cx, sc.hora_final_cx as hora_final_cx, ")
									.append(" sc.duracion_final_cx as duracion_final_cx, ")
									.append(" hq.finalizada as finalizada, ")
									.append(" hq.ha_sido_reversada as ha_sido_reversada ")
								.append(" FROM salascirugia.hoja_quirurgica hq ")
								.append(" INNER JOIN salascirugia.solicitudes_cirugia sc on(sc.numero_solicitud=hq.numero_solicitud) ")
								.append(" LEFT JOIN salascirugia.salas s on(hq.sala=s.consecutivo) ")
								.append(" LEFT JOIN salascirugia.tipos_salas ts ON (ts.codigo = hq.tipo_sala)")
								.append(" WHERE hq.numero_solicitud=? ");	
		}
		
		StringBuffer consultaDestino=new StringBuffer(" SELECT ssp.numero_solicitud as numero_solicitud, sp.codigo as codigo_destino, sp.descripcion as descripcion_destino ")
											.append(" FROM salascirugia.salidas_sala_paciente ssp ")
											.append(" JOIN salascirugia.salidas_pac_inst_cc sai on (ssp.salida_paciente_cc=sai.salida_pac) ")
											.append(" JOIN salascirugia.salida_paciente sp on (sai.salida_pac=sp.codigo) ")
											.append(" WHERE ssp.numero_solicitud=? ");		

		PreparedStatement ps= null;
		ResultSet rs = null;
		
		PreparedStatement psDestino= null;
		ResultSet rsDestino = null;
		try {
			String cadena=consulta.toString();
			
			ps=con.prepareStatement(cadena);
			ps.setInt(1, numeroSolicitud);
			if(existeHojaAnestesia){
				ps.setInt(2, numeroSolicitud);
			}
			
			rs=ps.executeQuery();
			
			if(rs.next()){
				dto.setTipoSala(new TipoSalaDto(rs.getInt("cod_tipo_sala"), rs.getString("desc_tipo_sala"), rs.getInt("institucion_tipo_sala")));
				dto.setSalaCirugia(new SalaCirugiaDto(rs.getInt("consecutivo_sala"), rs.getInt("cod_sala"), rs.getString("desc_sala"),
														rs.getInt("institucion_sala"),rs.getInt("centro_atencion_sala")));
				dto.setFechaIngresoSala(rs.getDate("fecha_ingreso_sala"));
				dto.setHoraIngresoSala(rs.getString("hora_ingreso_sala"));
				dto.setFechaSalidaSala(rs.getDate("fecha_salida_sala"));
				dto.setHoraSalidaSala(rs.getString("hora_salida_sala"));
				dto.setFechaInicioAnestesiaSala(rs.getDate("fecha_ini_anestesia"));
				dto.setHoraInicioAnestesiaSala(rs.getString("hora_ini_anestesia"));
				dto.setFechaFinAnestesiaSala(rs.getDate("fecha_fin_anestesia"));
				dto.setHoraFinAnestesiaSala(rs.getString("hora_fin_anestesia"));
				dto.setFechaInicioActoQxSala(rs.getDate("fecha_inicial_cx"));
				dto.setHoraInicioActoQxSala(rs.getString("hora_inicial_cx"));
				dto.setFechaFinActoQxSala(rs.getDate("fecha_final_cx"));
				dto.setHoraFinActoQxSala(rs.getString("hora_final_cx"));
				dto.setDuracionFinalCirugia(rs.getString("duracion_final_cx"));
				dto.setFinalizaHojaQuirurgica(rs.getBoolean("finalizada"));
				dto.setHaSidoReversada(rs.getBoolean("ha_sido_reversada"));
			}
			
			String cadenaDestino=consultaDestino.toString();
			
			psDestino=con.prepareStatement(cadenaDestino);
			psDestino.setInt(1, numeroSolicitud);
			rsDestino=psDestino.executeQuery();
			
			if(rsDestino.next()){
				dto.setDestinoPaciente(new DestinoPacienteDto(rsDestino.getInt("codigo_destino"), rsDestino.getString("descripcion_destino")));
			}
			dto.setNumeroSolicitud(numeroSolicitud);

		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			UtilidadBD.cerrarObjetosPersistencia(psDestino, rsDestino, null);
		}

		return dto;
	}
	
	/**
	 * Metodo para consultar si existe una Hoja de anestesia relacionada con una solicitud de cirugia
	 * @param con
	 * @param numeroSolicitud
	 * @return boolean existe
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	public static boolean existeHojaAnestesia(Connection con,int numeroSolicitud) throws BDException{
		boolean existe=false;

		StringBuffer consulta=new StringBuffer(" SELECT count(1) AS total ")
										.append(" FROM salascirugia.hoja_anestesia ha ") 
										.append(" WHERE ha.numero_solicitud = ? ");
		
		PreparedStatement ps= null;
		ResultSet rs = null;
		String cadena=consulta.toString();
		try {
			ps=con.prepareStatement(cadena);
			ps.setInt(1, numeroSolicitud);
			rs=ps.executeQuery();
			
			if(rs.next()&&rs.getInt("total")>0){
				existe=true;
			}
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return existe;
	}

	/**
	 * Metodo que consulta los tipos de sala para una institucion dada
	 * @param con
	 * @param institucion
	 * @return List<TipoSalaDto> lista de TipoSala
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	public static List<TipoSalaDto> consultarTiposSala(Connection con, int institucion) throws BDException {
		
		StringBuffer consulta=new StringBuffer(" SELECT codigo, descripcion, institucion ")
										.append(" FROM salascirugia.tipos_salas ")
										.append(" WHERE institucion=? and es_quirurgica = "+ValoresPorDefecto.getValorTrueParaConsultas());
		 
		List<TipoSalaDto> listaTipos = new ArrayList<TipoSalaDto>();
		
		PreparedStatement ps= null;
		ResultSet rs = null;
		String cadena=consulta.toString();
		try {
			ps=con.prepareStatement(cadena);
			ps.setInt(1, institucion);
			rs=ps.executeQuery();
			
			while(rs.next()){
				TipoSalaDto dto= new TipoSalaDto(rs.getInt("codigo"), rs.getString("descripcion"), rs.getInt("institucion"));
				listaTipos.add(dto);
			}
			
		}catch(SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		
		return listaTipos;
	}

	/**
	 * Metodo que consulta las salas de cirugia de un tipo de sala dado
	 * @param con
	 * @param tipoSalaDto
	 * @return List<SalaCirgugiaDto> lista salas
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	public static List<SalaCirugiaDto> consultarSalasCirugia(Connection con, TipoSalaDto tipoSalaDto) throws BDException {
		
		StringBuffer consulta=new StringBuffer(" SELECT consecutivo, codigo, descripcion, institucion, centro_atencion ")
										.append(" FROM salascirugia.salas ")
										.append(" WHERE institucion=? ")
										.append(" AND tipo_sala=? ");
	
			List<SalaCirugiaDto> listaSalas = new ArrayList<SalaCirugiaDto>();
			
			PreparedStatement ps= null;
			ResultSet rs = null;
			String cadena=consulta.toString();
			try {
				ps=con.prepareStatement(cadena);
				ps.setInt(1, tipoSalaDto.getInstitucion());
				ps.setInt(2, tipoSalaDto.getCodigoTipoSala());
				rs=ps.executeQuery();
			
				while(rs.next()){
					SalaCirugiaDto dto= new SalaCirugiaDto(rs.getInt("consecutivo"), rs.getInt("codigo"), rs.getString("descripcion"),
											rs.getInt("institucion"), rs.getInt("centro_atencion"));
					listaSalas.add(dto);
				}
			}catch(SQLException e) {
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
			}finally{
				UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
			}
			return listaSalas;
	}

	
	/**
	 * Metodo que consulta los destinosPaciente parametrizados para una institucion dada
	 * @param con
	 * @param institucion
	 * @return List<DestinoPacienteDto> lista de destinos
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	public static List<DestinoPacienteDto> consultarDestinosPaciente(Connection con, int institucion)throws BDException{
		
		StringBuffer consulta=new StringBuffer(" SELECT sp.codigo as codigo, sp.descripcion as descripcion, sai.institucion, sai.activo, sai.fallece ")
										.append(" FROM salascirugia.salida_paciente sp ")
										.append(" JOIN salascirugia.salidas_pac_inst_cc sai on (sai.salida_pac=sp.codigo) ")
										.append(" WHERE sai.institucion=? ");

		List<DestinoPacienteDto> listaDestinos = new ArrayList<DestinoPacienteDto>();
		
		PreparedStatement ps= null;
		ResultSet rs = null;
		String cadena=consulta.toString();
		try {
			ps=con.prepareStatement(cadena);
			ps.setInt(1, institucion);
			rs=ps.executeQuery();
		
			while(rs.next()){
				DestinoPacienteDto dto = new DestinoPacienteDto(rs.getInt("codigo"), rs.getString("descripcion"));
				listaDestinos.add(dto);
			}
		}catch(SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return listaDestinos;
	}

	/**
	 * Metodo que registra el indicativo ha_sido_reversada de la hoja quirurgica
	 * @param con
	 * @param numeroSolicitud
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	public static void reversarHojaQx(Connection con, int numeroSolicitud) throws BDException{
		
		StringBuffer update=new StringBuffer("UPDATE salascirugia.hoja_quirurgica   " )
													.append(" SET ha_sido_reversada = ")
													.append(ValoresPorDefecto.getValorTrueParaConsultas()).append(", ")
													.append(" finalizada = ")
													.append(ValoresPorDefecto.getValorFalseParaConsultas()).append(" ")
													.append(" WHERE numero_solicitud=? ");

		PreparedStatement ps= null;
		int resultado =ConstantesBD.codigoNuncaValido;
		try {
			String cadena=update.toString();
			ps=con.prepareStatement(cadena);
			ps.setInt(1, numeroSolicitud);
			
			resultado=ps.executeUpdate();
			
			if(resultado!=1){
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
			}
			
		}catch(SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
		}
	}


	/**
	 * Metodo que Persiste la informacion relacionada con la seccion "IngresoSalidaPaciente" de la Hoja quirurgica,
	 * si la hqx ya existe y no es necesario crearla se actualiza, 
	 * de lo contrario se hace la creacion de un nuevo registro de hqx.
	 * @param con
	 * @param ingresoSalidaPacienteDto
	 * @param actualizacion
	 * @param usuarioModifica
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 11/07/2013
	 */
	public static void guardarIngresoSalidaPaciente(Connection con, IngresoSalidaPacienteDto ingresoSalidaPacienteDto, UsuarioBasico usuarioModifica) throws BDException{
		
		StringBuffer guardaHqx=null;
		StringBuffer guardarDestino=null;
		StringBuffer guardaSolCx=null;
		
		boolean actualizacion=existeHojaQx(con, ingresoSalidaPacienteDto.getNumeroSolicitud());
		boolean tieneSalidaGuardada=tieneSalidaGuardada(con, ingresoSalidaPacienteDto.getNumeroSolicitud());
		boolean finalizada=hojaQxFinalizada(con, ingresoSalidaPacienteDto.getNumeroSolicitud());
		
		if(actualizacion){
			guardaHqx=new StringBuffer(" UPDATE salascirugia.hoja_quirurgica  " )
							.append(" SET fecha_ini_anestesia=?, hora_ini_anestesia=?, fecha_fin_anestesia=?, hora_fin_anestesia=?, ")
							.append(" sala=?, tipo_sala=?, finalizada=?, datos_medico=?, fecha_grabacion=?, hora_grabacion=? ");
							if(ingresoSalidaPacienteDto.isFinalizaHojaQuirurgica()&&!finalizada){
								guardaHqx.append(" , fecha_finaliza=?, hora_finaliza=? ");
							}
							guardaHqx.append(" WHERE numero_solicitud=? ");	
		}else{
			guardaHqx=new StringBuffer("INSERT INTO salascirugia.hoja_quirurgica  " )
							.append(" (numero_solicitud, fecha_ini_anestesia, hora_ini_anestesia, fecha_fin_anestesia, hora_fin_anestesia, sala, tipo_sala, finalizada,  datos_medico, fecha_grabacion, hora_grabacion ");
							if(ingresoSalidaPacienteDto.isFinalizaHojaQuirurgica()&&!finalizada){
								guardaHqx.append(", fecha_finaliza, hora_finaliza ");
							}
							guardaHqx.append(" ) ");
							guardaHqx.append(" VALUES (?,?,?,?,?,?,?,?,?,?,? ");
							if(ingresoSalidaPacienteDto.isFinalizaHojaQuirurgica()&&!finalizada){
								guardaHqx.append(" ,?,? ");
							}
							guardaHqx.append(" ) ");
							
		}
		if(tieneSalidaGuardada){
			guardarDestino= new StringBuffer(" UPDATE salascirugia.salidas_sala_paciente  ")
									.append(" SET salida_paciente_cc=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? ")
									.append(" WHERE numero_solicitud=? ");
		}else{
			guardarDestino=new StringBuffer(" INSERT INTO salascirugia.salidas_sala_paciente ")
									.append(" (consecutivo, numero_solicitud, salida_paciente_cc, usuario_modifica, fecha_modifica, hora_modifica) ")
									.append(" values(?,?,?,?,?,?) ");			
		}
		guardaSolCx=new StringBuffer(" UPDATE salascirugia.solicitudes_cirugia ")
							.append(" SET fecha_ingreso_sala=?, hora_ingreso_sala=?, fecha_salida_sala=?, hora_salida_sala=?, ")
							.append(" fecha_inicial_cx=?, hora_inicial_cx=?, fecha_final_cx=?, hora_final_cx=?, duracion_final_cx=? ")
							.append(" WHERE numero_solicitud=? ");
		
		
		PreparedStatement psGuardaHqx= null;
		PreparedStatement psGuardarDestino= null;
		PreparedStatement psGuardaSolCx= null;
		int resultado =ConstantesBD.codigoNuncaValido;
		try {
			if(actualizacion){
				String cadenaGuardaHqx=guardaHqx.toString();
				psGuardaHqx=con.prepareStatement(cadenaGuardaHqx);
				
				if(ingresoSalidaPacienteDto.getFechaInicioAnestesiaSala()!=null){
				psGuardaHqx.setDate(1, new java.sql.Date(ingresoSalidaPacienteDto.getFechaInicioAnestesiaSala().getTime()));
				}else{
					psGuardaHqx.setDate(1, new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(con).getTime()));
				}
				psGuardaHqx.setString(2, ingresoSalidaPacienteDto.getHoraInicioAnestesiaSala());
				if(ingresoSalidaPacienteDto.getFechaFinAnestesiaSala()!=null){
				psGuardaHqx.setDate(3, new java.sql.Date(ingresoSalidaPacienteDto.getFechaFinAnestesiaSala().getTime()));
				}else{
					psGuardaHqx.setDate(3, new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(con).getTime()));
				}
				
				psGuardaHqx.setString(4, ingresoSalidaPacienteDto.getHoraFinAnestesiaSala());
				psGuardaHqx.setInt(5, ingresoSalidaPacienteDto.getSalaCirugia().getConsecutivoSala());
				psGuardaHqx.setInt(6, ingresoSalidaPacienteDto.getTipoSala().getCodigoTipoSala());
				
				if(ingresoSalidaPacienteDto.isFinalizaHojaQuirurgica()){
					psGuardaHqx.setBoolean(7, ingresoSalidaPacienteDto.isFinalizaHojaQuirurgica());
				}else{
					psGuardaHqx.setBoolean(7, finalizada);
				}
				
				psGuardaHqx.setString(8, usuarioModifica.getInformacionGeneralPersonalSalud());
				psGuardaHqx.setDate(9, new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(con).getTime()));
				psGuardaHqx.setString(10, UtilidadFecha.getHoraActual(con));
				
				if(ingresoSalidaPacienteDto.isFinalizaHojaQuirurgica()&&!finalizada){
					psGuardaHqx.setDate(11, new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(con).getTime()));
					psGuardaHqx.setString(12, UtilidadFecha.getHoraActual(con));
					psGuardaHqx.setInt(13, ingresoSalidaPacienteDto.getNumeroSolicitud());
				}else{
					psGuardaHqx.setInt(11, ingresoSalidaPacienteDto.getNumeroSolicitud());
				}

				resultado=psGuardaHqx.executeUpdate();
				if(resultado!=1){
					throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
				}
	
			}else{
				String cadenaGuardaHqx=guardaHqx.toString();
				psGuardaHqx=con.prepareStatement(cadenaGuardaHqx);
				
				psGuardaHqx.setInt(1, ingresoSalidaPacienteDto.getNumeroSolicitud());
				if(ingresoSalidaPacienteDto.getFechaInicioAnestesiaSala()!=null){
				psGuardaHqx.setDate(2, new java.sql.Date(ingresoSalidaPacienteDto.getFechaInicioAnestesiaSala().getTime()));
				}else{
					psGuardaHqx.setDate(2, new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(con).getTime()));
				}			
				psGuardaHqx.setString(3, ingresoSalidaPacienteDto.getHoraInicioAnestesiaSala());
				if(ingresoSalidaPacienteDto.getFechaFinAnestesiaSala()!=null){
				psGuardaHqx.setDate(4, new java.sql.Date(ingresoSalidaPacienteDto.getFechaFinAnestesiaSala().getTime()));
				}else{
					psGuardaHqx.setDate(4, new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(con).getTime()));
				}
				psGuardaHqx.setString(5, ingresoSalidaPacienteDto.getHoraFinAnestesiaSala());
				psGuardaHqx.setInt(6, ingresoSalidaPacienteDto.getSalaCirugia().getConsecutivoSala());
				psGuardaHqx.setInt(7, ingresoSalidaPacienteDto.getTipoSala().getCodigoTipoSala());
				psGuardaHqx.setBoolean(8, ingresoSalidaPacienteDto.isFinalizaHojaQuirurgica());
				
				psGuardaHqx.setString(9, usuarioModifica.getInformacionGeneralPersonalSalud());
				psGuardaHqx.setDate(10, new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(con).getTime()));
				psGuardaHqx.setString(11, UtilidadFecha.getHoraActual(con));
				
				if(ingresoSalidaPacienteDto.isFinalizaHojaQuirurgica()&&!finalizada){
					psGuardaHqx.setDate(12, new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(con).getTime()));
					psGuardaHqx.setString(13, UtilidadFecha.getHoraActual(con));
				}
			
				resultado=psGuardaHqx.executeUpdate();
				if(resultado!=1){
					throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
				}
			}
			if(tieneSalidaGuardada && ingresoSalidaPacienteDto.getDestinoPaciente()!=null){
				String cadenaGuardarDestino=guardarDestino.toString();
				psGuardarDestino=con.prepareStatement(cadenaGuardarDestino);
				
				psGuardarDestino.setInt(1, ingresoSalidaPacienteDto.getDestinoPaciente().getCodigoDestino());
				psGuardarDestino.setString(2, usuarioModifica.getLoginUsuario());
				psGuardarDestino.setDate(3, new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(con).getTime()));
				psGuardarDestino.setString(4, UtilidadFecha.getHoraActual(con));
				psGuardarDestino.setInt(5, ingresoSalidaPacienteDto.getNumeroSolicitud());
				
				resultado=psGuardarDestino.executeUpdate();
				if(resultado!=1){
					throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
				}
				
			}
			//solo se guarda el destino si este viene diligenciado
			else if(ingresoSalidaPacienteDto.getDestinoPaciente()!=null){
				String cadenaGuardarDestino=guardarDestino.toString();
				psGuardarDestino=con.prepareStatement(cadenaGuardarDestino);
				
				int secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "salascirugia.seq_salidas_sala_paciente");
				
				psGuardarDestino.setInt(1, secuencia);
				psGuardarDestino.setInt(2, ingresoSalidaPacienteDto.getNumeroSolicitud());
				psGuardarDestino.setInt(3, ingresoSalidaPacienteDto.getDestinoPaciente().getCodigoDestino());
				psGuardarDestino.setString(4, usuarioModifica.getLoginUsuario());
				psGuardarDestino.setDate(5, new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(con).getTime()));
				psGuardarDestino.setString(6, UtilidadFecha.getHoraActual(con));			

				resultado=psGuardarDestino.executeUpdate();
				if(resultado!=1){
					throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
				}
			}

			String cadenaGuardarSolCx=guardaSolCx.toString();
			psGuardaSolCx=con.prepareStatement(cadenaGuardarSolCx);
			
			psGuardaSolCx.setDate(1, new java.sql.Date(ingresoSalidaPacienteDto.getFechaIngresoSala().getTime()));
			psGuardaSolCx.setString(2, ingresoSalidaPacienteDto.getHoraIngresoSala());
			psGuardaSolCx.setDate(3, new java.sql.Date(ingresoSalidaPacienteDto.getFechaSalidaSala().getTime()));
			psGuardaSolCx.setString(4, ingresoSalidaPacienteDto.getHoraSalidaSala());
			psGuardaSolCx.setDate(5, new java.sql.Date(ingresoSalidaPacienteDto.getFechaInicioActoQxSala().getTime()));
			psGuardaSolCx.setString(6, ingresoSalidaPacienteDto.getHoraInicioActoQxSala());
			psGuardaSolCx.setDate(7, new java.sql.Date(ingresoSalidaPacienteDto.getFechaFinActoQxSala().getTime()));
			psGuardaSolCx.setString(8, ingresoSalidaPacienteDto.getHoraFinActoQxSala());
			psGuardaSolCx.setString(9, ingresoSalidaPacienteDto.getDuracionFinalCirugia());
			psGuardaSolCx.setInt(10, ingresoSalidaPacienteDto.getNumeroSolicitud());
			
			resultado=psGuardaSolCx.executeUpdate();
			if(resultado!=1){
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
			}
			
		}catch(SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}catch(Exception e){
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}
		finally{
			UtilidadBD.cerrarObjetosPersistencia(psGuardaHqx, null, null);
			UtilidadBD.cerrarObjetosPersistencia(psGuardarDestino, null, null);
			UtilidadBD.cerrarObjetosPersistencia(psGuardaSolCx, null, null);
		}
	}
	
	/**
	 * Metodo para consultar si existe una Hoja de anestesia relacionada con una solicitud de cirugia
	 * @param con
	 * @param numeroSolicitud
	 * @return boolean existe
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 10/07/2013
	 */
	public static boolean existeHojaQx(Connection con,int numeroSolicitud) throws BDException{
		boolean existe=false;

		StringBuffer consulta=new StringBuffer(" SELECT count(1) as total ")
										.append(" FROM salascirugia.hoja_quirurgica hq ") 
										.append(" WHERE hq.numero_solicitud = ? ");

		PreparedStatement ps= null;
		ResultSet rs = null;
		String cadena=consulta.toString();
		try {
			ps=con.prepareStatement(cadena);
			ps.setInt(1, numeroSolicitud);
			rs=ps.executeQuery();
			
			if(rs.next()&&rs.getInt("total")>0){
				existe=true;
			}
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return existe;
	}

	/**
	 * Metodo que consulta si la hoja quirurgica esta registrada como finalizada. 
	 * @param con
	 * @param numeroSolicitud
	 * @return boolean finalizada
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 17/07/2013
	 */
	public static boolean hojaQxFinalizada(Connection con,int numeroSolicitud) throws BDException{
		boolean finalizada=false;

		StringBuffer consulta=new StringBuffer(" SELECT hq.finalizada as finalizada")
										.append(" FROM salascirugia.hoja_quirurgica hq ") 
										.append(" WHERE hq.numero_solicitud = ? ");

		PreparedStatement ps= null;
		ResultSet rs = null;
		String cadena=consulta.toString();
		try {
			ps=con.prepareStatement(cadena);
			ps.setInt(1, numeroSolicitud);
			rs=ps.executeQuery();
			
			if(rs.next()&&rs.getBoolean("finalizada")){
				finalizada=true;
			}
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return finalizada;
	}
	
	/**
	 * Metodo que consulta si hay una salida asociada a la solicitud dada
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 17/07/2013
	 */
	public static boolean tieneSalidaGuardada(Connection con,int numeroSolicitud) throws BDException{
		boolean existe=false;

		StringBuffer consulta=new StringBuffer(" SELECT count(1) as total ")
										.append(" FROM salascirugia.salidas_sala_paciente hq ") 
										.append(" WHERE hq.numero_solicitud = ? ");

		PreparedStatement ps= null;
		ResultSet rs = null;
		String cadena=consulta.toString();
		try {
			ps=con.prepareStatement(cadena);
			ps.setInt(1, numeroSolicitud);
			rs=ps.executeQuery();
			
			if(rs.next()&&rs.getInt("total")>0){
				existe=true;
			}
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return existe;
	}

	/**
	 * Metodo que consulta las notas aclaratorias relacionadas con un Informe Qx por Especialidad.
	 * @param con
	 * @param codigoInformeQxEspecialidad
	 * @return List<NotaAclaratoriaDto> 
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 11/07/2013
	 */
	public static List<NotaAclaratoriaDto> consultarNotasAclaratorias(Connection con, int codigoInformeQxEspecialidad, boolean esAscendente) throws BDException {
		
		String ordenamiento="DESC";
		if(esAscendente){
			ordenamiento="ASC";
		}
		
		StringBuffer consulta=new StringBuffer(" SELECT codigo, descripcion, fecha_grabacion, hora_grabacion, usuario_grabacion, ")
										.append(" administracion.GETESPECIALIDADESMEDICO(usuario_grabacion,', ') as especialidadesMedico, ")
										.append(" administracion.getnombreusuario(usuario_grabacion) as nombreUsuario, ")
										.append(" administracion.getdatosmedico(usuario_grabacion) as datosMedico ")
										.append(" FROM salascirugia.notas_aclaratorias ")
										.append(" WHERE cod_informe_especialidad=? ")
										.append(" ORDER BY fecha_grabacion "+ordenamiento+", hora_grabacion "+ordenamiento+"");
		
		
		List<NotaAclaratoriaDto> listaNotasAclaratorias = new ArrayList<NotaAclaratoriaDto>();
		
		PreparedStatement ps= null;
		ResultSet rs = null;
		String cadena=consulta.toString();
		try {
			ps=con.prepareStatement(cadena);
			ps.setInt(1, codigoInformeQxEspecialidad);
			rs=ps.executeQuery();
		
			while(rs.next()){
				NotaAclaratoriaDto dto = new NotaAclaratoriaDto(rs.getString("descripcion"), 
																rs.getInt("codigo"), 
																rs.getDate("fecha_grabacion"), 
																rs.getString("hora_grabacion"), 
																rs.getString("usuario_grabacion"),
																codigoInformeQxEspecialidad,
																rs.getString("especialidadesMedico"),
																rs.getString("nombreUsuario"),
																rs.getString("datosMedico"));
				listaNotasAclaratorias.add(dto);
			}
		}catch(SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return listaNotasAclaratorias;
	}

	/**
	 * Metodo que persiste la informacion de una nota aclaratoria, el objeto "notaAclaratoriaDto" debe tener descripcion y 
	 * codigoInformeQxEspecialidad seteados.
	 * @param con
	 * @param notaAclaratoriaDto
	 * @param usuarioModifica
	 * @throws BDException
	 * @author Oscar Pulido
	 * @created 12/07/2013
	 */
	public static void guardarNotaAclaratotia(Connection con, NotaAclaratoriaDto notaAclaratoriaDto, UsuarioBasico usuarioModifica) throws BDException {
		
		StringBuffer insert=new StringBuffer(" INSERT INTO salascirugia.notas_aclaratorias " )
									.append(" (codigo, descripcion, fecha_grabacion, hora_grabacion, cod_informe_especialidad, usuario_grabacion) ")
									.append(" VALUES (?,?,?,?,?,?) ");

		int secuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "SALASCIRUGIA.SEQ_NOTAS_ACLARATORIAS");

		PreparedStatement ps= null;
		int resultado =ConstantesBD.codigoNuncaValido;
		try {
			String cadena=insert.toString();
			ps=con.prepareStatement(cadena);
			ps.setInt(1, secuencia);
			ps.setString(2, notaAclaratoriaDto.getDescripcion());
			ps.setDate(3, new java.sql.Date(UtilidadFecha.getFechaActualTipoBD(con).getTime()));
			ps.setString(4, UtilidadFecha.getHoraActual(con));
			ps.setInt(5, notaAclaratoriaDto.getCodigoInformeQxEspecialidad());	
			ps.setString(6, usuarioModifica.getLoginUsuario());

			resultado=ps.executeUpdate();
			
			if(resultado!=1){
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
			}
		
		}catch(SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
		}
		
	}

	/**
	 * Consulta el cirujano que participa en la peticion de la cirugia y la especialidad que se asigna
	 * 
	 * @param connection
	 * @param codigoPeticion
	 * @return cirujano y especialidad
	 * @throws BDException
	 * @author jeilones
	 * @created 16/07/2013
	 */
	public static ProfesionalHQxDto consultarCirujanoPeticionCx(Connection connection,int codigoPeticion) throws BDException{
		StringBuffer consulta=new StringBuffer("select per.codigo as id_medico,per.primer_nombre,per.segundo_nombre,per.primer_apellido,per.segundo_apellido, ")
			.append("esp.codigo as codigo_especialidad,esp.nombre as nombre_especialidad  from ") 
			.append("salascirugia.peticion_qx pet ")
			.append("inner join salascirugia.prof_partici_peticion_qx cir on cir.peticion_qx=pet.codigo ")
			.append("inner join administracion.personas per on cir.codigo_medico=per.codigo ")
			.append("inner join administracion.especialidades esp on esp.codigo=cir.especialidad ")
			.append("where pet.codigo = ? ")
			.append("and cir.tipo_participante = ? ");
		PreparedStatement ps = null;
		ResultSet rs = null;
		String cadena=consulta.toString();
		
		ProfesionalHQxDto profesionalHQxDto = null;
		try {
			ps=connection.prepareStatement(cadena);
			ps.setInt(1, codigoPeticion);
			ps.setInt(2, ConstantesBD.codigoTipoParticipanteCirujano);
			
			rs=ps.executeQuery();
			
			if(rs.next()){
				profesionalHQxDto=new ProfesionalHQxDto();
				
				profesionalHQxDto.setIdMedico(rs.getInt("id_medico"));
				profesionalHQxDto.setPrimerNombre(rs.getString("primer_nombre"));
				profesionalHQxDto.setSegundoNombre(rs.getString("segundo_nombre"));
				profesionalHQxDto.setPrimerApellido(rs.getString("primer_apellido"));
				profesionalHQxDto.setSegundoApellido(rs.getString("segundo_apellido"));
				
				EspecialidadDto especialidadDto=new EspecialidadDto();
				especialidadDto.setCodigo(rs.getInt("codigo_especialidad"));
				especialidadDto.setNombre(rs.getString("nombre_especialidad"));
				profesionalHQxDto.setEspecialidad(especialidadDto);
			}
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		
		return profesionalHQxDto;
	}
	
	/**
	 * Metodo que consulta los estados de facturacion y liquidacion de los cargos asociados a una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws BDException
	 * @autor Oscar Pulido
	 * @created 22/07/2013
	 */
	public static List<CargoSolicitudDto> consultarEstadoCargosSolicitud(Connection con, int numeroSolicitud) throws BDException {
			
		List<CargoSolicitudDto> listaCargos = new ArrayList<CargoSolicitudDto>();

		StringBuffer consulta=new StringBuffer(" SELECT dc.facturado AS estado_facturacion, esSolCxCargada(292) AS estado_liquidacion ")
									.append(" FROM facturacion.det_cargos dc ")
									.append(" WHERE solicitud=? ");
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String cadena=consulta.toString();
		
		try {
			ps=con.prepareStatement(cadena);
			ps.setInt(1, numeroSolicitud);
			
			rs=ps.executeQuery();
			
			if(rs.next()){
				CargoSolicitudDto dto = new CargoSolicitudDto();
				if(rs.getString("estado_facturacion").equals(ConstantesBD.acronimoSi)){
					dto.setEstadoFacturacion(true);
				}else{
					dto.setEstadoFacturacion(true);
				}
				if(rs.getString("estado_liquidacion").equals(ConstantesBD.acronimoNo)){
					dto.setEstadoLiquidacion(true);
				}else{
					dto.setEstadoLiquidacion(false);
				}
			listaCargos.add(dto);
			}
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		
		return listaCargos;
	}
	
	/**
	 * Metodo que consulta la programacion de cirugia, si existe, para una peticion dada
	 * @param con
	 * @param codigoPeticion
	 * @return
	 * @throws BDException
	 * @autor Oscar Pulido
	 * @created 23/07/2013
	 */
	public static ProgramacionPeticionQxDto consultarProgramacionPeticionQx(Connection con, int codigoPeticion)  throws BDException {
			
		ProgramacionPeticionQxDto programacionPeticionQxDto= new ProgramacionPeticionQxDto();

		StringBuffer consulta=new StringBuffer(" SELECT fecha_cirugia, fecha_programacion, hora_programacion, hora_inicio, hora_fin, sala, estado_sala ")
									.append(" FROM programacion_salas_qx ")
									.append(" WHERE peticion=? ");

		PreparedStatement ps = null;
		ResultSet rs = null;
		String cadena=consulta.toString();
		
		try {
			ps=con.prepareStatement(cadena);
			ps.setInt(1, codigoPeticion);
			
			rs=ps.executeQuery();
			
			if(rs.next()){
				programacionPeticionQxDto.setCodigoPeticion(codigoPeticion);
				programacionPeticionQxDto.setFechaCirugia(rs.getDate("fecha_cirugia"));
				programacionPeticionQxDto.setFechaProgramacion(rs.getDate("fecha_programacion"));
				programacionPeticionQxDto.setHoraPrgramacion(rs.getString("hora_programacion"));
				programacionPeticionQxDto.setHoraInicio(rs.getString("hora_inicio"));
				programacionPeticionQxDto.setHoraFin(rs.getString("hora_fin"));
				programacionPeticionQxDto.setSala(rs.getInt("sala"));
				programacionPeticionQxDto.setEstadoSala(rs.getInt("estado_sala"));
			}
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		
		return programacionPeticionQxDto;
	}
	
	/**
	 * Cambia el estado de una solicitud, si se va a cambiar a estado Interpretada, el parametro medicoInterpreta es obligatorio
	 * 
	 * @param connection
	 * @param codigoSolicitud
	 * @param estado
	 * @param medicoInterpreta
	 * @throws BDException
	 * @author jeilones
	 * @created 29/07/2013
	 */
	public static void cambiarEstadoSolicitud(Connection  connection,int codigoSolicitud, int estado,UsuarioBasico medicoInterpreta) throws BDException{
		StringBuffer update=new StringBuffer("UPDATE ordenes.solicitudes SET fecha_grabacion= ? , " )
				.append("hora_grabacion= ? , ") 			 
				.append("estado_historia_clinica= ? ");
		
		if(estado==ConstantesBD.codigoEstadoHCInterpretada&&medicoInterpreta==null){
			throw new BDException(CODIGO_ERROR_NEGOCIO.NO_HAY_MEDICO_INTERPRETA);
		}
		
		if(estado==ConstantesBD.codigoEstadoHCInterpretada){
			update.append(",codigo_medico_interpretacion = ? , ")  
				   .append("fecha_interpretacion = ? , ")
				   .append("hora_interpretacion = ? ");
		}
		update.append("WHERE numero_solicitud= ? ");
		
		PreparedStatement ps= null;
		
		try {
			String cadena=update.toString();
			
			ps=connection.prepareStatement(cadena);
			java.sql.Date fechaActual=new Date(UtilidadFecha.getFechaActualTipoBD(connection).getTime());
			String horaActual=UtilidadFecha.getHoraActual(connection);
			
			int posFechaGrabacion=1;
			int posHoraGrabacion=2;
			int posEstado=3;
			int posMedicoInterpreta=4;
			int posFechaInterpretacion=5;
			int posHoraInterpretacion=6;
			int posNumeroSolicitud=7;
			
			
			ps.setDate(posFechaGrabacion, fechaActual);
			ps.setString(posHoraGrabacion, horaActual);
			ps.setInt(posEstado, estado);
			
			if(estado==ConstantesBD.codigoEstadoHCInterpretada){
				ps.setInt(posMedicoInterpreta, medicoInterpreta.getCodigoPersona());
				ps.setDate(posFechaInterpretacion, fechaActual);
				ps.setString(posHoraInterpretacion, horaActual);
			}else{
				posNumeroSolicitud=4;
			}
			ps.setInt(posNumeroSolicitud, codigoSolicitud);
			
			int resultado=ps.executeUpdate();
			
			if(resultado<1){
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
			}
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
		}
	}
	
	/**
	 * Cambia el estado de una peticion de cirugia
	 * 
	 * @param connection
	 * @param codigoPeticion
	 * @param estado
	 * @throws BDException
	 * @author jeilones
	 * @created 29/07/2013
	 */
	public static void cambiarEstadoPeticion(Connection  connection,int codigoPeticion, int estado) throws BDException{
		StringBuffer update=new StringBuffer("UPDATE salascirugia.peticion_qx SET " )
			.append(" estado_peticion= ? " )
			.append(" WHERE codigo= ? ");
		
		PreparedStatement ps= null;
		
		try {
			String cadena=update.toString();
			
			ps=connection.prepareStatement(cadena);
			java.sql.Date fechaActual=new Date(UtilidadFecha.getFechaActualTipoBD(connection).getTime());
			String horaActual=UtilidadFecha.getHoraActual(connection);
			
			int posEstado=1;
			int posPeticion=2;
			
			ps.setInt(posEstado, estado);
			ps.setInt(posPeticion, codigoPeticion);
			
			
			int resultado=ps.executeUpdate();
			
			if(resultado<1){
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS);
			}
			
		} catch (SQLException e) {
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS,e);
		}finally{
			UtilidadBD.cerrarObjetosPersistencia(ps, null, null);
		}
	}
	
	public static String getNombreDiagnostico(Connection con, String acronimoDiagnostico, Integer tipoCieDiagnosticoInt) throws BDException
	{
		PreparedStatement ps = null;
		ResultSet rs = null;
		String nombreDiagnostico = null;
		try
		{
			ps = con.prepareStatement("select manejopaciente.getnombrediagnostico(?, ?) as nombreDiagnostico from dual");
			ps.setString(1, acronimoDiagnostico);
			ps.setInt(2, tipoCieDiagnosticoInt);
			rs = ps.executeQuery();
			if(rs.next())
			{
				nombreDiagnostico = rs.getString("nombreDiagnostico");
			}
		}
		catch (SQLException e) 
		{
			logger.error("\n problema el nombre del diagnostico "+e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		finally
		{
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return nombreDiagnostico;
	}
	/*************************************
	 *    FIN MT 6497 Usabilidad HQx     *
	 *************************************/
	
	/******************************************************************************************************************************************/
	/*--------------------------------------------------------------------------------------------------------------------------------------
	 * FIN DE METODOS DE LA NUEVA HOJA QUIRURGICA
	 --------------------------------------------------------------------------------------------------------------------------------------*/
	/******************************************************************************************************************************************/
	
	
	
}
