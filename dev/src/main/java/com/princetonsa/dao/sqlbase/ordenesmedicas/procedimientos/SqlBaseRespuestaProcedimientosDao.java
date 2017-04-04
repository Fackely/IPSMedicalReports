package com.princetonsa.dao.sqlbase.ordenesmedicas.procedimientos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.ordenes.DtoProcedimiento;
import com.princetonsa.dto.ordenes.DtoRespuestaProcedimientos;

/**
 * @author Jorge Armando Osorio Velasquez
 * @author Wilson Rios
 *
 */
public class SqlBaseRespuestaProcedimientosDao
{
	
	/**
	 * 
	 */
	public static Logger logger=Logger.getLogger(SqlBaseRespuestaProcedimientosDao.class);
	
	
	


//	" rs.descripcion as procedimiento," +
	
	/**
	 * Caden encargada de consultar los procedimientos
	 */
	private static String strSelectConsultaProcedimientos_1 = " SELECT " +
																		" s.numero_solicitud as solicitud," +
																		" s.centro_costo_solicitante as ccsolicitante, " +
																		" to_char(sc.fecha_cita,'dd/mm/yyyy') as fechasolicitud, " +
																		" sc.hora_inicio_cita as horasolicitud," +
																		" s.consecutivo_ordenes_medicas as orden," +
																		" sp.codigo_servicio_solicitado as servicio, " +
																		" getcodigoservicio(sp.codigo_servicio_solicitado, ?) ||' ' ||getnombreservicio(sp.codigo_servicio_solicitado, ?) as procedimiento, " +
																		" getnomcentroatencion(cccuenta.centro_atencion) as centroatencion," +
																		" c.via_ingreso as codigoviaingreso, " +
																		" c.codigo_paciente as codigopaciente, " +
																		" getnombrepersona(c.codigo_paciente) as nombrepaciente, " +
																		" s.cuenta as cuenta, " +
																		" getnombreviaingreso(c.via_ingreso) as viaingreso," +
																		" getnombrepersona(s.codigo_medico) as solicitante," +
																		" s.estado_historia_clinica as codigoestadohc, " +
																		" s.pyp as pyp, " +
																		" s.tipo as tiposolicitud," +
																		" se.requiere_diagnostico as requierediagnostico," +
																		" getnombreespecialidad(s.especialidad_solicitante) as especialidad, " +
																		" getestadosolhis(s.estado_historia_clinica) as estadohc," +
																		" "+ConstantesBD.codigoNuncaValido+" as codigopeticion," +
																		" getcamacuenta(c.id,c.via_ingreso) as cama, " +
																		" case when s.urgente="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'U' else '' end as urgente," +
																		" '' As indqx, " +
																		" '' As peticion, " +
																		"  coalesce (sp.portatil_asociado,-1) As portatil," +
																		" getSolicitudTieneIncluidos(s.numero_solicitud) as cantidadincluidos "+
																	" FROM solicitudes s " +
																		" inner join sol_procedimientos sp ON(sp.numero_solicitud=s.numero_solicitud) " +
																		" inner join cuentas c on(c.id=s.cuenta and c.estado_cuenta <> '"+ConstantesBD.codigoEstadoCuentaCerrada+"') " +
																		" inner join centros_costo cccuenta on(c.area=cccuenta.codigo) " +
																		" inner join servicios se on(se.codigo=sp.codigo_servicio_solicitado) " +
																		" INNER JOIN ingresos i on(i.id=c.id_ingreso)" +
																		" LEFT join servicios_cita sc on (sc.numero_solicitud=s.numero_solicitud) " ;


	private static String strWhereConsultaProcedimientos_1 = " WHERE tiene_cita='"+ConstantesBD.acronimoSi+"' and " +
																			" (c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaActiva+
																			" OR c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturadaParcial+
																			" OR c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaAsociada+" " +
																			" OR c.estado_cuenta="+ConstantesBD.codigoEstadoFacturacionFacturada+" " +
																			" AND getcuentafinal(c.id) IS NULL) ";
	
																			/*
																			 * Comentado x Tarea 123993
																			 *
																			" AND ( " +
																			"		i.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' " +
																			"		OR (i.estado = '"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"' " +
																					"AND (c.estado_cuenta="+ConstantesBD.codigoEstadoFacturacionFacturada+" OR  " +
																					"     c.estado_cuenta="+ConstantesBD.ccodigoEstadoCuentaFacturadaParcial+"))) " ; */

	/**
	 * Caden encargada de consultar los procedimientos
	 */
	private static String strSelectConsultaProcedimientos_2 = " SELECT " +
																		" s.numero_solicitud as solicitud," +
																		" s.centro_costo_solicitante as ccsolicitante, " +
																		" to_char(s.fecha_solicitud,'dd/mm/yyyy') as fechasolicitud, " +
																		" substr(s.hora_solicitud,0,6) as horasolicitud," +
																		" s.consecutivo_ordenes_medicas as orden," +
																		" sp.codigo_servicio_solicitado as servicio, " +
																		" getcodigoservicio(sp.codigo_servicio_solicitado, ?) ||' ' ||getnombreservicio(sp.codigo_servicio_solicitado, ?) as procedimiento, " +
																		" getnomcentroatencion(cccuenta.centro_atencion) as centroatencion," +
																		" c.via_ingreso as codigoviaingreso, " +
																		" c.codigo_paciente as codigopaciente, " +
																		" getnombrepersona(c.codigo_paciente) as nombrepaciente, " +
																		" s.cuenta as cuenta, " +
																		" getnombreviaingreso(c.via_ingreso) as viaingreso," +
																		" getnombrepersona(s.codigo_medico) as solicitante," +
																		" s.estado_historia_clinica as codigoestadohc, " +
																		" s.pyp as pyp, " +
																		" s.tipo as tiposolicitud," +
																		" se.requiere_diagnostico as requierediagnostico," +
																		" getnombreespecialidad(s.especialidad_solicitante) as especialidad, " +
																		" getestadosolhis(s.estado_historia_clinica) as estadohc," +
																		" "+ConstantesBD.codigoNuncaValido+" as codigopeticion," +
																		" getcamacuenta(c.id,c.via_ingreso) as cama, " +
																		" case when s.urgente="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'U' else '' end as urgente," +
																		" '' As indqx, " +
																		" '' As peticion, " +
																		"  coalesce (sp.portatil_asociado,-1) As portatil," +
																		" getSolicitudTieneIncluidos(s.numero_solicitud) as cantidadincluidos "+
																	" FROM solicitudes s " +
																		" inner join sol_procedimientos sp ON(sp.numero_solicitud=s.numero_solicitud) " +
																		" inner join cuentas c on(c.id=s.cuenta and c.estado_cuenta <> '"+ConstantesBD.codigoEstadoCuentaCerrada+"') " +
																		" inner join centros_costo cccuenta on(c.area=cccuenta.codigo) " +
																		" inner join servicios se on(se.codigo=sp.codigo_servicio_solicitado) " +
																		" INNER JOIN ingresos i on(i.id=c.id_ingreso)" +
																		" left outer join servicios_cita sc on (sc.numero_solicitud=s.numero_solicitud) " ;  

	private static String strWhereConsultaProcedimientos_2 =	" WHERE " +
																			" tiene_cita='"+ConstantesBD.acronimoNo+"' and " +
																			" (c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaActiva+
																			" OR c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturadaParcial+
																			" OR c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaAsociada+" " +
																			" OR c.estado_cuenta="+ConstantesBD.codigoEstadoFacturacionFacturada+" " +
																			" AND getcuentafinal(c.id) IS NULL) ";
																			/*
																			 * Comentado x Tarea 123993
																			 *
																			" AND ( " +
																			"		i.estado='"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"' " +
																			"		OR (i.estado = '"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"' " +
																					"AND (c.estado_cuenta="+ConstantesBD.codigoEstadoFacturacionFacturada+" OR  " +
																					"     c.estado_cuenta="+ConstantesBD.codigoEstadoCuentaFacturadaParcial+"))) " ; */

	
	/**
	 * Cadena encargada de consultar No Cruentos
	 */												
	private static String strSelectConsultaNoCruentos=" SELECT " +
															" s.numero_solicitud as solicitud," +
															" s.centro_costo_solicitante as ccsolicitante, " +
															" case when tiene_cita='"+ConstantesBD.acronimoSi+"' then to_char(sc.fecha_cita,'"+ConstantesBD.formatoFechaAp+"') else to_char(s.fecha_solicitud,'dd/mm/yyyy') end as fechasolicitud, " +
															" case when tiene_cita='"+ConstantesBD.acronimoSi+"' then sc.hora_inicio_cita else substr(s.hora_solicitud,0,6) end as horasolicitud," +
															" s.consecutivo_ordenes_medicas as orden," +
															" scxs.servicio as servicio," +
															" getcodigoservicio(scxs.servicio, ?) ||' ' ||getnombreservicio(scxs.servicio, ?) as procedimiento," +
															" getnomcentroatencion(cccuenta.centro_atencion) as centroatencion," +
															" c.via_ingreso as codigoviaingreso, " +
															" c.codigo_paciente as codigopaciente, " +
															" getnombrepersona(c.codigo_paciente) as nombrepaciente, " +
															" s.cuenta as cuenta, " +
															" getnombreviaingreso(c.via_ingreso) as viaingreso," +
															" getnombrepersona(s.codigo_medico) as solicitante," +
															" s.estado_historia_clinica as codigoestadohc, " +
															" s.pyp as pyp, " +
															" s.tipo as tiposolicitud," +
															" se.requiere_diagnostico as requierediagnostico," +
															" getnombreespecialidad(s.especialidad_solicitante) as especialidad, " +
															" getestadosolhis(s.estado_historia_clinica) as estadohc," +
															" pq.codigo as codigopeticion," +
															" getcamacuenta(c.id,c.via_ingreso) as cama, " +
															" case when s.urgente="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'U' else '' end as urgente, " +
															" scx.ind_qx As indqx, " +
															" pq.codigo||'' As peticion, " +
															" "+ConstantesBD.codigoNuncaValido+" As portatil, " +
															" getSolicitudTieneIncluidos(s.numero_solicitud) as cantidadincluidos "+																	
													" FROM solicitudes s " +
															" inner join sol_cirugia_por_servicio scxs on (scxs.numero_solicitud=s.numero_solicitud AND scxs.consecutivo=1) " +
															" inner join cuentas c on(c.id=s.cuenta and c.estado_cuenta <> '"+ConstantesBD.codigoEstadoCuentaCerrada+"') " +
															" inner join centros_costo cccuenta on(c.area=cccuenta.codigo) " +
															" inner join servicios se on(se.codigo=scxs.servicio) " +
															" inner JOIN solicitudes_cirugia scx on(s.numero_solicitud=scx.numero_solicitud) " +
															" INNER JOIN peticion_qx pq on (scx.codigo_peticion=pq.codigo) " +  
															" INNER JOIN ingresos i on(i.id=c.id_ingreso)" +
															" left outer join servicios_cita sc on (sc.numero_solicitud=s.numero_solicitud) ";
 private static String strWhereConsultaNoCruentos=	" WHERE " +
															 	" (scx.ind_qx='"+ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento+"')";


/**
* se consultan las peticiones sin solicitud.
*/
private static String strConsultaNoCruentosPeticion = " SELECT " +
																"  "+ConstantesBD.codigoNuncaValido+" As solicitud," +
																"  "+ConstantesBD.codigoNuncaValido+" As ccsolicitante, " +
																" '' As fechasolicitud, " +
																" '' As horasolicitud," +
																"  "+ConstantesBD.codigoNuncaValido+" as orden," +
																" ps.servicio as servicio," +
																" getcodigoservicio(ps.servicio, ?) ||' ' ||getnombreservicio(ps.servicio, ?) as procedimiento," +
																" '' as centroatencion," +
																" "+ConstantesBD.codigoNuncaValido+" as codigoviaingreso, " +
																" pq.paciente as codigopaciente, " +
																" getnombrepersona(pq.paciente) as nombrepaciente, " +
																"  "+ConstantesBD.codigoNuncaValido+" as cuenta, " +
																" '' as viaingreso," +
																" getnombrepersona(pq.solicitante) as solicitante," +
																"  "+ConstantesBD.codigoNuncaValido+" as codigoestadohc, " +
																"  "+ValoresPorDefecto.getValorFalseParaConsultas()+" As pyp, " +
																"  "+ConstantesBD.codigoNuncaValido+" as tiposolicitud," +
																" se.requiere_diagnostico as requierediagnostico," +
																" '' as especialidad, " +
																" '' as estadohc," +
																" pq.codigo as codigopeticion," +
																" '' as cama, " +
																" '' As urgente, " +
																" '' As indqx, " +
																" pq.codigo||'' As peticion, " +
																" "+ConstantesBD.codigoNuncaValido+" As portatil," +
																" 0 as cantidadincluidos "+	
																" FROM peticion_qx pq " +
																" INNER JOIN peticiones_servicio ps ON (ps.peticion_qx=pq.codigo)"+
																" inner join servicios se on(se.codigo=ps.servicio) " +
																" INNER JOIN centro_costo_grupo_ser ccgs on (ccgs.grupo_servicio=se.grupo_servicio)" +
																" WHERE " +
																	" (pq.estado_peticion="+ConstantesBD.codigoEstadoPeticionPendiente+
																" OR pq.estado_peticion="+ConstantesBD.codigoEstadoPeticionProgramada+
																" OR pq.estado_peticion="+ConstantesBD.codigoEstadoPeticionAtendida+
																" OR pq.estado_peticion="+ConstantesBD.codigoEstadoPeticionReprogramada+" or pq.estado_peticion is null) " +
																//valida si es cuenta válida 
																" AND (getcuentavalidaxpaci(pq.paciente)>0) "+		
																//se valida que todos los servicios asociados a la peticion sean no cruentos
																" AND getcantnumserrq(pq.codigo)=0 " +
																//se valida que la peticion no tenga solicitud
																" AND gettienesolpeteticion (pq.codigo)=0 ";
																


	
	/**
	 * 
	 */
	private static String finaizarRespuestaSolProcedimientoStr="UPDATE sol_procedimientos SET finalizada_respuesta= ? WHERE numero_solicitud= ? ";
	
	/**
	 * Cadena que elimina los diagnósticos de un procedimiento
	 */
	private static final String eliminarDiagnosticosStr = " DELETE FROM diag_procedimientos WHERE codigo_respuesta = ? ";
	
	/**
	 * Cadena que inserta un diagnóstico para la respuesta del procedimiento
	 */
	private static final String insertarDiagnosticoStr = 	"INSERT " +
															"INTO diag_procedimientos " +
															"(codigo_respuesta,acronimo,tipo_cie,principal,complicacion,numero) " +
															"VALUES (?,?,?,?,?,?) ";
	
	/**
	 * Sentencia SQL para insertar 
	 */
	private static final String insertarRespuestaProcedimientoStr="INSERT INTO res_sol_proc (" +
																						   " codigo, " +
																						   " numero_solicitud," +
																						   " fecha_grabacion," +
																						   " hora_grabacion," +
																						   " fecha_ejecucion," +
																						   " resultados," +
																						   " tipo_recargo," +
																						   " observaciones," +
																						   " comentario_historia_clinica, " +
																						   " hora_ejecucion," +
																						   " codigo_medico_responde," +
																						   " usuario_registra_respuesta," +
																						   " observacion_capitacion" +
																						   " )" +
																						   " VALUES (?, ?, CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Sentencia para actualizar 
	 * */
	private static final String StrActualizarRespuestaProcedimientoSerCx = "UPDATE res_sol_proc SET codigo_cx_serv = ? WHERE codigo = ?  ";
		
	/**
	 * 
	 */
	private static final String datosHistoriaClinicaMedicoSolicitanteStr= "SELECT coalesce(getnombrepersona(s.codigo_medico),'') AS medicoSolicitante FROM solicitudes s WHERE s.numero_solicitud=?";
	
	/**
	 * 
	 */
	private static final String datosHistoriaCuenta= "SELECT s.cuenta AS cuenta FROM solicitudes s WHERE numero_solicitud=?";
	
	/**
	 * 
	 */
	private static final String cargarResultadosAnterioresCxStr="SELECT COALESCE(resultados,' ') as resultados FROM res_sol_proc WHERE numero_solicitud=? AND codigo_cx_serv = ?  order by fecha_grabacion, hora_grabacion";
	
	/**
	 * 
	 */
	private static final String cargarResultadosAnterioresStr="SELECT COALESCE(resultados,' ') as resultados FROM res_sol_proc WHERE numero_solicitud=?  order by fecha_grabacion, hora_grabacion";
	
	/**
	 * 
	 */
	private static final String cargarRespuestaBasicaStr= " SELECT to_char(rsp.fecha_ejecucion, '"+ConstantesBD.formatoFechaAp+"') as fechaEjecucion, substr(rsp.hora_ejecucion, 0, 6) as horaEjecucion, rsp.tipo_recargo AS codigoTipoRecargo, getnombretiporecargo(rsp.tipo_recargo) AS nombreTipoRecargo, rsp.comentario_historia_clinica AS comentarioHistoriaClinica, coalesce(rsp.resultados,'') as resultado, rsp.observaciones AS observaciones FROM res_sol_proc rsp WHERE codigo=?";
	
	
	/**
	 * Cadena que consulta los diagnósticos de un procedimiento
	 */
	private static final String cargarDiagnosticosStr = "SELECT "+ 
		"acronimo AS acronimo, "+ 
		"getnombrediagnostico(acronimo,tipo_cie) As nombre, "+ 
		"tipo_cie AS tipo_cie, "+ 
		"principal AS principal, "+ 
		"complicacion AS complicacion, "+ 
		"numero AS numero "+ 
		"FROM diag_procedimientos "+ 
		"where codigo_respuesta = ? ORDER BY numero ";
	
	/**
	 * Consulta los articulos incluidos dentro de una solicitud de procedimientos
	 * */
	private static final String strConsultarArticulosIncluidosSolProc = 
			"SELECT " +
			"solicitud_ppal," +
			"getconsecutivosolicitud(solicitud_ppal) AS orden_ppal, " +
			"articulo," +
			"art.codigo_interfaz," +
			"getdescarticulo(articulo) AS descripcion_articulo," +
			"servicio_ppal," +
			"getnombreservicio(servicio_ppal,"+ConstantesBD.codigoTarifarioCups+") AS descripcion_servicio, " +
			"getcodigoespecialidad(servicio_ppal) AS especialidad_servicio_ppal, " +
			"cantidad," +
			"cantidad_maxima," +
			"farmacia," +
			"getnomcentrocosto(farmacia) AS descripcion_farmacia, " +
			"es_servicio_incluido," +
			"solicitud_incluida," +
			"getespos(articulo) AS es_pos," +
			"CASE WHEN solicitud_incluida IS NULL THEN 0 ELSE gettienejustificacionnopos(articulo,solicitud_incluida) END AS tienejus " +			
			"FROM " +
			"art_inclu_sol_proc " +			
			"INNER JOIN articulo art ON (art.codigo = articulo) " +
			"WHERE solicitud_ppal = ? " +
			"ORDER BY descripcion_articulo ASC ";	
	
	/**
	 * Consulta los servicios incluidos en una solicitud de procedimientos
	 * */
	private static final String strConsultarServiciosIncluidosSolProc = "" +
			"SELECT " +
			"solicitud_ppal," +
			"getconsecutivosolicitud(solicitud_ppal) AS orden_ppal, " +			
			"servicio_ppal," +			
			"servicio_incluido," +
			"getnombreservicio(servicio_incluido,"+ConstantesBD.codigoTarifarioCups+") AS descripcion_servicio_incl, " +
			"getnombreservicio(servicio_ppal,"+ConstantesBD.codigoTarifarioCups+") AS descripcion_servicio_ppal, " +
			"getcodigoespecialidad(servicio_ppal) AS especialidad_servicio_ppal, " +
			"getcodigoespecialidad(servicio_incluido) AS especialidad_servicio_incl, " +
			"centro_costo_ejecuta, " +
			"getnomcentrocosto(centro_costo_ejecuta) AS descripcion_cc_ejecuta, " +
			"count(servicio_incluido) AS cantidad, " +
			"getcodigoservicio(servicio_ppal,?) as codigo_propietario_ppal, " +
			"getcodigoservicio(servicio_incluido,?) as codigo_propietario_incluido " +
			"FROM " +
			"serv_inclu_sol_proc " +			
			"WHERE solicitud_ppal = ? " +
			"GROUP BY solicitud_ppal,servicio_ppal,servicio_incluido,centro_costo_ejecuta " +
			"ORDER BY descripcion_servicio_incl ASC ";
	
	//**************************************************************************************************************
	//Metodo Para Cargar el Dto de Procedimientos*******************************************************************
	
	/**
	 * Carga el Dto de Procedimientos
	 * */
	/*
	private static final String strConsultaDtoProcedimiento = "SELECT " +
															  "sol.numero_solicitud," +
															  "sol.centro_costo_solicitado," +
															  "CASE WHEN sol.tipo = "+ConstantesBD.codigoTipoSolicitudCirugia+" THEN coalesce(solc.servicio,"+ConstantesBD.codigoNuncaValido+") ELSE coalesce(solp.codigo_servicio_solicitado,"+ConstantesBD.codigoNuncaValido+") END AS codigo_servicio_solicitado," +															  
															  "TO_CHAR(sol.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') as fecha_solicitud," +
															  "sol.hora_solicitud," +
															  "sol.cuenta," +
															  "getviaingresosolicitud(sol.numero_solicitud) AS via_ingreso," +
															  "coalesce(e.res_proc,"+ConstantesBD.codigoNuncaValido+") AS egreso_res_proc," +
															  "sol.tipo," +
															  "CASE WHEN sol.tipo = "+ConstantesBD.codigoTipoSolicitudCirugia+" THEN coalesce(getgruposervicio(coalesce(solc.servicio,"+ConstantesBD.codigoNuncaValido+")),"+ConstantesBD.codigoNuncaValido+") ELSE coalesce(getgruposervicio(coalesce(solp.codigo_servicio_solicitado,"+ConstantesBD.codigoNuncaValido+")),"+ConstantesBD.codigoNuncaValido+") END AS gruposervicio, " +
															  "CASE WHEN sol.tipo = "+ConstantesBD.codigoTipoSolicitudCirugia+" THEN getcodigoservicio(solc.servicio,?) ||' '||getnombreservicio(solc.servicio,?) ELSE getcodigoservicio(solp.codigo_servicio_solicitado, ?) ||' '||getnombreservicio(solp.codigo_servicio_solicitado,?) END AS nombre_servicio_solicitado, "+															  
															  "conv.codigo as codigoConvenio, " +
															  "conv.nombre as nombreConvenio, " +
															  "conv.tipo_regimen as codigoTipoRegimen,"+
															  "coalesce(getnomtiporegimen(conv.tipo_regimen),'') AS nombreTipoRegimen "+															  
															  "FROM solicitudes sol " +
															  "LEFT OUTER JOIN sol_procedimientos solp ON (solp.numero_solicitud = sol.numero_solicitud) " +
															  "LEFT OUTER JOIN sol_cirugia_por_servicio solc ON (solc.numero_solicitud = sol.numero_solicitud) " +
															  "LEFT OUTER JOIN egresos e ON (e.cuenta = sol.cuenta) " +
															  "LEFT OUTER JOIN sub_cuentas subc ON (subc.ingreso=getingresosolicitud(sol.numero_solicitud) AND subc.nro_prioridad = 1) " +
															  "LEFT OUTER JOIN convenios conv ON (conv.codigo=subc.convenio) " +
															  "WHERE sol.numero_solicitud = ? ";*/
	
	/**
	 * Carga el Dto de Procedimientos
	 * */
	private static final String strConsultaDtoProcedimiento ="SELECT * FROM ( " +
															  "SELECT " +
															  "sol.numero_solicitud," +
															  "sol.centro_costo_solicitado," +
															  "coalesce(solp.codigo_servicio_solicitado,"+ConstantesBD.codigoNuncaValido+") AS codigo_servicio_solicitado," +															  
															  "TO_CHAR(sol.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') as fecha_solicitud," +
															  "sol.hora_solicitud," +
															  "sol.cuenta," +
															  "getviaingresosolicitud(sol.numero_solicitud) AS via_ingreso," +
															  "coalesce(e.res_proc,"+ConstantesBD.codigoNuncaValido+") AS egreso_res_proc," +
															  "sol.tipo," +
															  "coalesce(getgruposervicio(coalesce(solp.codigo_servicio_solicitado,"+ConstantesBD.codigoNuncaValido+")),"+ConstantesBD.codigoNuncaValido+") AS gruposervicio, " +
															  "FACTURACION.getObtenerCodigoServHist(solp.codigo_servicio_solicitado, ?, TO_CHAR(ING.FECHA_INGRESO, 'DD/MM/YYYY')) ||' '||FACTURACION.getNombreServicioHistorico(solp.codigo_servicio_solicitado, ?, TO_CHAR(ING.FECHA_INGRESO, 'DD/MM/YYYY')) AS nombre_servicio_solicitado, "+															  
															  "conv.codigo as codigoConvenio, " +
															  "conv.nombre as nombreConvenio, " +
															  "conv.tipo_regimen as codigoTipoRegimen,"+
															  "coalesce(getnomtiporegimen(conv.tipo_regimen),'') AS nombreTipoRegimen," +
															  "coalesce(sol.interpretacion,' ') as interpretacion "+																    
															  "FROM solicitudes sol " +
															  "INNER JOIN sol_procedimientos solp ON (solp.numero_solicitud = sol.numero_solicitud) " +															  
															  "LEFT OUTER JOIN egresos e ON (e.cuenta = sol.cuenta) " +
															  "INNER JOIN cuentas cue ON (cue.id = sol.cuenta) " +
															  "INNER JOIN MANEJOPACIENTE.INGRESOS ING ON (ING.ID = CUE.ID_INGRESO) "+
															  "INNER JOIN sub_cuentas subc ON (subc.ingreso=cue.id_ingreso AND subc.nro_prioridad = 1) " +
															  "INNER JOIN convenios conv ON (conv.codigo=subc.convenio) " +
															  "WHERE sol.numero_solicitud = ? " +
															  "UNION " +															  
															  "SELECT " +
															  "sol.numero_solicitud," +
															  "sol.centro_costo_solicitado," +
															  "coalesce(solc.servicio,"+ConstantesBD.codigoNuncaValido+") AS codigo_servicio_solicitado," +															  
															  "TO_CHAR(sol.fecha_solicitud,'"+ConstantesBD.formatoFechaAp+"') as fecha_solicitud," +
															  "sol.hora_solicitud," +
															  "sol.cuenta," +
															  "getviaingresosolicitud(sol.numero_solicitud) AS via_ingreso," +
															  "coalesce(e.res_proc,"+ConstantesBD.codigoNuncaValido+") AS egreso_res_proc," +
															  "sol.tipo," +
															  "coalesce(getgruposervicio(coalesce(solc.servicio,"+ConstantesBD.codigoNuncaValido+")),"+ConstantesBD.codigoNuncaValido+")  AS gruposervicio, " +
															  "getcodigoservicio(solc.servicio,?) ||' '||getnombreservicio(solc.servicio,?) AS nombre_servicio_solicitado, "+															  
															  "conv.codigo as codigoConvenio, " +
															  "conv.nombre as nombreConvenio, " +
															  "conv.tipo_regimen as codigoTipoRegimen,"+
															  "coalesce(getnomtiporegimen(conv.tipo_regimen),'') AS nombreTipoRegimen," +
															  "coalesce(sol.interpretacion,' ') as interpretacion "+																   
															  "FROM solicitudes sol " +															  
															  "INNER JOIN sol_cirugia_por_servicio solc ON (solc.numero_solicitud = sol.numero_solicitud) " +
															  "LEFT OUTER JOIN egresos e ON (e.cuenta = sol.cuenta) " +
															  "INNER JOIN cuentas cue ON (cue.id = sol.cuenta) " +
															  "INNER JOIN sub_cuentas subc ON (subc.ingreso = cue.id_ingreso AND subc.nro_prioridad = 1) " +
															  "INNER JOIN convenios conv ON (conv.codigo=subc.convenio) " +
															  "WHERE sol.numero_solicitud = ? " +
															  ") s " ;
	
	
	/**
	 * Carga las respuesta de procedimientos 
	 * */
	private static final String strConsultaDtoRespuestaProce = "SELECT " +
															   "res.codigo," +
															   "coalesce(res.nro_resp_anteriores,"+ConstantesBD.codigoNuncaValido+") AS nro_resp_anteriores," +
															   "coalesce(e.diagnostico_muerte,'') AS diagnostico_muerte," +
															   "e.diagnostico_muerte_cie," +
															   "getnombrediagnostico(e.diagnostico_muerte,e.diagnostico_muerte_cie) AS nombre_diag," +
															   "TO_CHAR(p.fecha_muerte,'"+ConstantesBD.formatoFechaAp+"') AS fecha_muerte," +
															   "p.hora_muerte," +
															   "p.certificado_defuncion, " +
															   "getnombreespecialidad(s.especialidad_solicitada) AS especialidadsolicitada,  " +
															   "s.interpretacion," +
															   "coalesce(sp.finalidad,-1) as finalidad," +
															   "coalesce(fs.nombre,'') as nomfinalidad " +
															   "FROM res_sol_proc res " +
															   "INNER JOIN solicitudes s ON (s.numero_solicitud = ?) " +
															   "inner join sol_procedimientos sp on (sp.numero_solicitud=s.numero_solicitud) " +
															   "INNER JOIN cuentas c ON (c.id = s.cuenta ) " +
															   "LEFT OUTER JOIN egresos e ON (e.res_proc = res.codigo) " +
															   "INNER JOIN pacientes p ON (p.codigo_paciente = c.codigo_paciente ) " +
															   "LEFT OUTER JOIN finalidades_servicio fs on (fs.codigo=sp.finalidad) " +
															   "WHERE res.numero_solicitud = ? ";
	
	/**
	 * Carga los otros comentarios para la respuesta de procedimiento
	 * */
	private static final String strConsultaOtrosComentarios = "SELECT " +
															  "o.res_sol_proc AS res_sol_proc," +
															  "o.codigo_medico AS codigo_medico," +
															  "o.descripcion AS descripcion," +
															  "administracion.getnombremedico(o.codigo_medico) AS nombre_medico " +
															  "FROM otros_comen_res_sol_proc o " +
															  "WHERE " +
															  "o.res_sol_proc = ?  " +
															  "ORDER BY fecha_modifica,hora_modifica DESC ";
	
	/**
	 * 
	 * */
	private static final String strInsertarOtrosComentarios = "INSERT INTO " +
															  "otros_comen_res_sol_proc(res_sol_proc,codigo_medico,descripcion,usuario_modifica,fecha_modifica,hora_modifica) " +
															  "VALUES (?,?,?,?,?,?) ";
	
	/**
	 * 
	 * */
	private static final String strActualizarNoRespuestaAnte = "UPDATE res_sol_proc SET nro_resp_anteriores = ? WHERE codigo = ? ";
	
	/**
	 * 
	 * */
	private static final String strActualizarConsecutivoOrden = "UPDATE " +
																"centro_costo_grupo_ser " +
																"SET " +
																"consecutivo = ? " +
																"WHERE centro_atencion = ? AND " +
																"grupo_servicio = ? AND " +
																"centro_costo = ? ";
	
	/**
	 * 
	 * */
	private static final String strExisteEgresoCuenta = "SELECT COUNT(cuenta) FROM egresos WHERE cuenta = ? ";
	
	/**
	 * 
	 * */
	private static final String strInsertarEgresoResProc = "INSERT INTO " +
														   "egresos" +
														   "(" +
														   "cuenta," +
														   "diagnostico_muerte," +
														   "diagnostico_muerte_cie," +
														   "diagnostico_principal," +
														   "diagnostico_principal_cie," +
														   "diagnostico_relacionado1," +
														   "diagnostico_relacionado1_cie," +
														   "diagnostico_relacionado2," +
														   "diagnostico_relacionado2_cie," +
														   "diagnostico_relacionado3," +
														   "diagnostico_relacionado3_cie," +
														   "es_automatico," +
														   "res_proc) " +
														   "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	
	/**
	 * 
	 * */
	private static final String strActualizarMuertePaciente = "UPDATE " +
															  "pacientes " +
															  "SET " +
															  "fecha_muerte = ?, " +
															  "hora_muerte = ?, " +
															  "certificado_defuncion = ? " +
															  "WHERE codigo_paciente  = ? ";
	
	/**
	 * Consulta el numero de respuestas de procedimiento para el mismo servicio y centro de costos solicitado 
	 * 
	private static final String strConsultaNumRespuestasAnteriores = "SELECT COUNT(sol.numero_solicitud) " +
																	 "FROM solicitudes  sol " +
																	 "INNER JOIN sol_procedimientos solp  ON (solp.numero_solicitud = sol.numero_solicitud AND solp.codigo_servicio_solicitado = ? ) " +
																	 "INNER JOIN cuentas c ON (c.id = sol.cuenta ) " +																	 
																	 "WHERE sol.centro_costo_solicitado = ? " +																	 
																	 "AND sol.estado_historia_clinica = ? " +
																	 "AND c.codigo_paciente = ? ";
	*/		
	
	/**
	 * Consulta el numero de respuestas de procedimiento para el mismo servicio y centro de costos solicitado 
	 */ 
	private static final String strConsultaNumRespuestasAnteriores = "SELECT COUNT(sol.numero_solicitud) " +
																	 "FROM solicitudes  sol " +
																	 "INNER JOIN sol_procedimientos solp  ON (solp.numero_solicitud = sol.numero_solicitud ) " +
																	 "INNER JOIN servicios ser ON (ser.codigo = solp.codigo_servicio_solicitado AND ser.grupo_servicio = ? ) " +																	 																	 
																	 "WHERE sol.centro_costo_solicitado = ? " +																	 
																	 "AND sol.estado_historia_clinica = ? ";
	
	
	/**
	 * Consulta el numero de respuestas de procedimiento para el mismo servicio y centro de costos solicitado y que el tipo de solicitud sea cirugia 
	 * 
	private static final String strConsultaNumRespuestasAnterioresCirugia = "SELECT COUNT(sol.numero_solicitud) " +
																			"FROM solicitudes  sol " +
																			"INNER JOIN sol_cirugia_por_servicio solc  ON (solc.numero_solicitud = sol.numero_solicitud AND solc.servicio = ? ) " +
																			"INNER JOIN res_sol_proc res ON (res.numero_solicitud = sol.numero_solicitud ) "+
																			"INNER JOIN cuentas c ON (c.id = sol.cuenta) " +																	 
																			"WHERE sol.centro_costo_solicitado = ? " +
																			"AND sol.estado_historia_clinica != "+ConstantesBD.codigoEstadoHCAnulada +" "+ 
																			"AND c.codigo_paciente = ? ";
																			
	*/
	
	/**
	 * Consulta el numero de respuestas de procedimiento para el mismo servicio y centro de costos solicitado y que el tipo de solicitud sea cirugia 
	 **/ 
	private static final String strConsultaNumRespuestasAnterioresCirugia = "SELECT COUNT(sol.numero_solicitud) " +
																			"FROM solicitudes  sol " +
																			"INNER JOIN sol_cirugia_por_servicio solc  ON (solc.numero_solicitud = sol.numero_solicitud ) " +
																			"INNER JOIN servicios ser ON (ser.codigo = solc.servicio AND ser.grupo_servicio = ? ) " +	
																			"INNER JOIN res_sol_proc res ON (res.numero_solicitud = sol.numero_solicitud) "+																																				 
																			"WHERE sol.centro_costo_solicitado = ?  " +
																			"AND sol.estado_historia_clinica != "+ConstantesBD.codigoEstadoHCAnulada +" ";
	
	/**
	 * Consulta el consecutivo de respuesta
	 * */
	private static final String strConsultaConsecutivoRespuesta = "SELECT coalesce(consecutivo,"+ConstantesBD.codigoNuncaValido+") " +
																  "FROM centro_costo_grupo_ser " +
																  "WHERE " +
																  "centro_atencion = ? AND " +
																  "grupo_servicio = ?  AND " +
																  "centro_costo = ? ";
																			
																			

	
	//**************************************************************************************************************
	//**************************************************************************************************************
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap listadoSolicitudesProcedimientosResponder(Connection con, HashMap vo)
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		boolean permitirIntepretarMultiple = UtilidadTexto.getBoolean(vo.get("permitirInterpretarMultiple").toString());
		boolean mostrarNoCruentosPet = true;
		String complementoConsulta="";
		String complementoConsulta2="";
		String complementoWhere = "";
		String cadena3 = "";
		PreparedStatementDecorator ps1 = null;
		ResultSetDecorator rs1 = null;
		PreparedStatementDecorator ps2 = null;
		ResultSetDecorator rs2 = null;
		PreparedStatementDecorator ps3 = null;
		ResultSetDecorator rs3 = null;
		
		if(!UtilidadTexto.isEmpty(vo.get("pisoFiltro")+"")||!UtilidadTexto.isEmpty(vo.get("habitacionFiltro")+"")||!UtilidadTexto.isEmpty(vo.get("camaFiltro")+""))
		{
			complementoConsulta=" left outer join his_camas_cuentas hcc on(hcc.cuenta=s.cuenta) ";
		}
		
		if(vo.containsKey("entidadSubcontratada") && 
				vo.get("entidadSubcontratada").toString().equals(ConstantesBD.acronimoSi))
		{			
			// PermitirAutorizarDiferenteDeSolicitudes
			complementoConsulta2 = " LEFT JOIN auto_entsub_solicitudes aess ON (aess.numero_solicitud = s.numero_solicitud) ";
			complementoConsulta2 += " LEFT JOIN autorizaciones_entidades_sub aes ON (aess.autorizacion_ent_sub = aes.consecutivo AND  aes.estado = '"+ConstantesIntegridadDominio.acronimoAutorizado+"' AND aes.tipo = '"+ConstantesIntegridadDominio.acronimoExterna+"' AND aes.entidad_autorizada_sub = "+vo.get("codigoEntidadSub").toString()+" ) ";
			//complementoConsulta2 += " LEFT JOIN auto_entsub_solicitudes aess ON (aes.numero_solicitud = s.numero_solicitud AND aes.estado = '"+ConstantesIntegridadDominio.acronimoAutorizado+"' AND aes.tipo = '"+ConstantesIntegridadDominio.acronimoExterna+"' AND aes.entidad_autorizada_sub = "+vo.get("codigoEntidadSub").toString()+" ) "; aca
			
			complementoWhere = " AND s.estado_historia_clinica IN ("+ConstantesBD.codigoEstadoHCSolicitada+","+ConstantesBD.codigoEstadoHCTomaDeMuestra+","+ConstantesBD.codigoEstadoHCEnProceso+") ";
			mostrarNoCruentosPet = false;
		}
		else
		{
			if(vo.containsKey("tipCCEjecuta") && 
					vo.get("tipCCEjecuta").toString().equals(ConstantesIntegridadDominio.acronimoAmbos)) 
			{
				// PermitirAutorizarDiferenteDeSolicitudes
				//complementoConsulta2 = "LEFT JOIN autorizaciones_entidades_sub aes ON (aes.numero_solicitud = s.numero_solicitud AND aes.estado = '"+ConstantesIntegridadDominio.acronimoAutorizado+"' AND aes.tipo = '"+ConstantesIntegridadDominio.acronimoInterna+"' )  ";  aca
				complementoConsulta2 = "LEFT JOIN autorizaciones_entidades_sub aes ON (aes.estado = '"+ConstantesIntegridadDominio.acronimoAutorizado+"' AND aes.tipo = '"+ConstantesIntegridadDominio.acronimoInterna+"' )  ";  
				complementoConsulta2 += " LEFT JOIN auto_entsub_solicitudes aess ON (aess.numero_solicitud = s.numero_solicitud AND aess.autorizacion_ent_sub = aes.consecutivo )  ";
				    
				complementoWhere = "";
				mostrarNoCruentosPet = false;
			}
		}
		
		String cadena1_1 = strSelectConsultaProcedimientos_1+complementoConsulta+complementoConsulta2+strWhereConsultaProcedimientos_1+complementoWhere;
		String cadena1_2 = strSelectConsultaProcedimientos_2+complementoConsulta+complementoConsulta2+strWhereConsultaProcedimientos_2+complementoWhere;
		String cadena2   = strSelectConsultaNoCruentos+complementoConsulta+complementoConsulta2+strWhereConsultaNoCruentos+complementoWhere;
		
		if(mostrarNoCruentosPet)
			cadena3   = strConsultaNoCruentosPeticion;
		
		
		//cadena1.replace("?",ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(Utilidades.convertirAEntero(vo.get("institucion").toString())));
		
		if(vo.containsKey("cCosto") && Utilidades.convertirAEntero(vo.get("cCosto")+"")>0  &&  vo.containsKey("cAtencion") && Utilidades.convertirAEntero(vo.get("cAtencion")+"")>0)
		{
			if(mostrarNoCruentosPet)
				cadena3 +=" AND getvalidacionccservicio (pq.codigo,"+Utilidades.convertirAEntero(vo.get("cAtencion")+"")+","+Utilidades.convertirAEntero(vo.get("cCosto")+"")+")>0 ";
			
		}
		
		if(vo.containsKey("paciente")&&Utilidades.convertirAEntero(vo.get("paciente")+"")>0)
		{
			//logger.info("codigo paciente "+vo.get("paciente"));
			cadena1_1+=" and c.codigo_paciente="+vo.get("paciente")+" ";
			cadena1_2+=" and c.codigo_paciente="+vo.get("paciente")+" ";
			cadena2+=" and c.codigo_paciente="+vo.get("paciente")+" ";
			
			if(mostrarNoCruentosPet)
				cadena3+=" and pq.paciente="+vo.get("paciente")+" ";				
		
		}
		if(!(vo.get("fechaInicialFiltro")+"").equals("")&&!(vo.get("fechaFinalFiltro")+"").equals(""))
		{
			 //añadido
			cadena1_1+=" AND (to_char(sc.fecha_cita, '"+ConstantesBD.formatoFechaBD+"') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialFiltro")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalFiltro")+"")+"' )";
			cadena1_2+=" AND (to_char(s.fecha_solicitud, '"+ConstantesBD.formatoFechaBD+"') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialFiltro")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalFiltro")+"")+"' )";
			cadena2+=" AND ((tiene_cita = '"+ConstantesBD.acronimoNo+"' and to_char(s.fecha_solicitud, '"+ConstantesBD.formatoFechaBD+"') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialFiltro")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalFiltro")+"")+"' ) OR (tiene_cita = '"+ConstantesBD.acronimoSi+"' and to_char(sc.fecha_cita, '"+ConstantesBD.formatoFechaBD+"') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialFiltro")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalFiltro")+"")+"' ))";
			
			if(mostrarNoCruentosPet)
				cadena3+=" and to_char(pq.fecha_peticion, '"+ConstantesBD.formatoFechaBD+"') between '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaInicialFiltro")+"")+"' and '"+UtilidadFecha.conversionFormatoFechaABD(vo.get("fechaFinalFiltro")+"")+"'";
		}
		if(!(vo.get("centroCostroSolicitanteFiltro")+"").equals(""))
		{
			cadena1_1+=" AND s.centro_costo_solicitante ='"+(vo.get("centroCostroSolicitanteFiltro")+"")+"'";
			cadena1_2+=" AND s.centro_costo_solicitante ='"+(vo.get("centroCostroSolicitanteFiltro")+"")+"'";
			cadena2+=" AND s.centro_costo_solicitante ='"+(vo.get("centroCostroSolicitanteFiltro")+"")+"'";
		}
		if(!(vo.get("areaFiltro")+"").equals(""))
		{
			cadena1_1+=" AND c.area = '"+(vo.get("areaFiltro")+"")+"'";
			cadena1_2+=" AND c.area = '"+(vo.get("areaFiltro")+"")+"'";
			cadena2+=" AND c.area = '"+(vo.get("areaFiltro")+"")+"'";
		}
		
		
		if(!(vo.get("pisoFiltro")+"").equals(""))
		{
			
			cadena1_1+=" AND hcc.codigopkpiso = '"+(vo.get("pisoFiltro")+"")+"'";
			cadena1_2+=" AND hcc.codigopkpiso = '"+(vo.get("pisoFiltro")+"")+"'";
			cadena2+=" AND hcc.codigopkpiso = '"+(vo.get("pisoFiltro")+"")+"'";
		}
		if(!(vo.get("habitacionFiltro")+"").equals(""))
		{
			
			cadena1_1+=" AND hcc.codigopkhabitacion = '"+(vo.get("habitacionFiltro")+"")+"'";
			cadena1_2+=" AND hcc.codigopkhabitacion = '"+(vo.get("habitacionFiltro")+"")+"'";
			cadena2+=" AND hcc.codigopkhabitacion = '"+(vo.get("habitacionFiltro")+"")+"'";
		}
		if(!(vo.get("camaFiltro")+"").equals(""))
		{
			
			cadena1_1+=" AND hcc.codigocama = '"+(vo.get("camaFiltro")+"")+"'";
			cadena1_2+=" AND hcc.codigocama = '"+(vo.get("camaFiltro")+"")+"'";
			cadena2+=" AND hcc.codigocama = '"+(vo.get("camaFiltro")+"")+"'";
		}

		//solo se deben mostrar las solicitudes con fecha de la cita menor igual a la fecha del sistema. docuemtno 482 pag 20 --> esto solo aplica para solicitudes tipo_cita.

		cadena1_1+=" and(sc.fecha_cita<=current_date and sc.estado_cita not in("+ConstantesBD.codigoEstadoCitaAtendida+","+ConstantesBD.codigoEstadoCitaNoCumplida+","+ConstantesBD.codigoEstadoCitaNoAtencion+")) ";
		cadena2+=" and(tiene_cita = '"+ConstantesBD.acronimoNo+"' or (tiene_cita = '"+ConstantesBD.acronimoSi+"' and sc.fecha_cita<=current_date and sc.estado_cita not in("+ConstantesBD.codigoEstadoCitaAtendida+","+ConstantesBD.codigoEstadoCitaNoCumplida+","+ConstantesBD.codigoEstadoCitaNoAtencion+"))) ";
		
		cadena1_1+=" and s.centro_costo_solicitado in (SELECT centro_costo FROM centros_costo_usuario WHERE usuario='"+vo.get("usuario")+"') ";
		cadena1_2+=" and s.centro_costo_solicitado in (SELECT centro_costo FROM centros_costo_usuario WHERE usuario='"+vo.get("usuario")+"') ";

		cadena2+=" and s.centro_costo_solicitado in (SELECT centro_costo FROM centros_costo_usuario WHERE usuario='"+vo.get("usuario")+"') ";
		
		
		/*cadena1_1+=" and s.centro_costo_solicitado = "+vo.get("centroCostoSolicitado");
		cadena1_2+=" and s.centro_costo_solicitado ="+vo.get("centroCostoSolicitado");

		cadena2+=" and s.centro_costo_solicitado ="+vo.get("centroCostoSolicitado");*/

		//no es necasario validar el centro de antecion, ya que el centro_cosoto_solicitado, el codigo es unico, y este es el que se relaciona al centro de antecion.
		//la validacion de centro de costo tratante solo se tiene en cuenta para interpretar
		/*
		//tarea oid=280623, para responder procedimientos solo se debe validar, que el centro de costo solicitado sea el de 
		cadena+=" AND s.ocupacion_solicitada IN('"+ConstantesBD.codigoOcupacionMedicaTodos+"','"+vo.get("ocupacionSolicitada")+"') ";
		*/
		if(!permitirIntepretarMultiple)
		{
			cadena1_1+=" AND (s.estado_historia_clinica in('"+ConstantesBD.codigoEstadoHCSolicitada+"','"+ConstantesBD.codigoEstadoHCTomaDeMuestra+"','"+ConstantesBD.codigoEstadoHCEnProceso+"') OR (s.estado_historia_clinica = '"+ConstantesBD.codigoEstadoHCRespondida+"' AND sp.respuesta_multiple='"+ConstantesBD.acronimoSi+"' AND sp.finalizada_respuesta='"+ConstantesBD.acronimoNo+"' ) )";
			cadena1_2+=" AND (s.estado_historia_clinica in('"+ConstantesBD.codigoEstadoHCSolicitada+"','"+ConstantesBD.codigoEstadoHCTomaDeMuestra+"','"+ConstantesBD.codigoEstadoHCEnProceso+"') OR (s.estado_historia_clinica = '"+ConstantesBD.codigoEstadoHCRespondida+"' AND sp.respuesta_multiple='"+ConstantesBD.acronimoSi+"' AND sp.finalizada_respuesta='"+ConstantesBD.acronimoNo+"' ) )";
		}
		else
		{
			cadena1_1+=" AND (s.estado_historia_clinica in('"+ConstantesBD.codigoEstadoHCSolicitada+"','"+ConstantesBD.codigoEstadoHCTomaDeMuestra+"','"+ConstantesBD.codigoEstadoHCEnProceso+"') OR (s.estado_historia_clinica in ('"+ConstantesBD.codigoEstadoHCRespondida+"','"+ConstantesBD.codigoEstadoHCInterpretada+"') AND sp.respuesta_multiple='"+ConstantesBD.acronimoSi+"' AND sp.finalizada_respuesta='"+ConstantesBD.acronimoNo+"' ) )";
			cadena1_2+=" AND (s.estado_historia_clinica in('"+ConstantesBD.codigoEstadoHCSolicitada+"','"+ConstantesBD.codigoEstadoHCTomaDeMuestra+"','"+ConstantesBD.codigoEstadoHCEnProceso+"') OR (s.estado_historia_clinica in ('"+ConstantesBD.codigoEstadoHCRespondida+"','"+ConstantesBD.codigoEstadoHCInterpretada+"') AND sp.respuesta_multiple='"+ConstantesBD.acronimoSi+"' AND sp.finalizada_respuesta='"+ConstantesBD.acronimoNo+"' ) )";
		}
		
		cadena2+=" AND (s.estado_historia_clinica in('"+ConstantesBD.codigoEstadoHCSolicitada+"','"+ConstantesBD.codigoEstadoHCRespondida+"') )";

		/*
		//tarea oid=280623, para responder procedimientos solo se debe validar, que el centro de costo solicitado sea el de 

		String cadenaEspecialidades="'"+ConstantesBD.codigoEspecialidadMedicaTodos+"'";
		Especialidades esp=(Especialidades)vo.get("especialidadSolicitada");
		Iterator iter=esp.getListadoEspecialidades().iterator();
		while(iter.hasNext())
		{
			Especialidad espe=(Especialidad)iter.next();
			if(espe.isActivaSistema())
			{
				cadenaEspecialidades+=",'"+espe.getCodigoEspecialidad()+"'";
			}
		}
		
		cadena+=" AND se.especialidad IN("+cadenaEspecialidades+") ";
		*/
		
		/*
		String cadena = "SELECT solicitud, ccsolicitante, fechasolicitud,horasolicitud,orden, servicio,procedimiento, centroatencion," +
								" codigoviaingreso, codigopaciente, nombrepaciente, cuenta, viaingreso, solicitante, codigoestadohc, " +
								" pyp, tiposolicitud, requierediagnostico, especialidad, estadohc, codigopeticion, cama, urgente, indqx, " +
								" peticion, portatil,cantidadincluidos " +
						" FROM ( " +cadena1+
						" UNION " +cadena2+
						" UNION " +cadena3+
							   " ) s ";
							   */
		
		
		
		int numRegiMapaOriginal = 0;
		try {
			try
			{
				//logger.info("consulta procedimientos=> "+cadena);
				String consultaTempo=cadena1_1+" UNION "+cadena1_2+" ORDER BY fechasolicitud,horasolicitud ";
				logger.info("consulta 1 -->"+consultaTempo);
				ps1 = new PreparedStatementDecorator(con.prepareStatement(consultaTempo));
				ps1.setString(1, ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(Utilidades.convertirAEntero(vo.get("institucion").toString())));
				ps1.setString(2, ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(Utilidades.convertirAEntero(vo.get("institucion").toString())));
				ps1.setString(3, ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(Utilidades.convertirAEntero(vo.get("institucion").toString())));
				ps1.setString(4, ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(Utilidades.convertirAEntero(vo.get("institucion").toString())));
				rs1 = new ResultSetDecorator(ps1.executeQuery());
				mapa=UtilidadBD.cargarValueObject(rs1);
				for(int i=0;i<Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
				{
					String tempo=Utilidades.consultarDiagnosticosPaciente(con, mapa.get("cuenta_"+i)+"", Utilidades.convertirAEntero(mapa.get("codigoviaingreso_"+i)+""));
					if(!tempo.trim().equals(""))
					{
						tempo=tempo.split(ConstantesBD.separadorSplit)[0];
					}
					mapa.put("diagnostico_"+i, tempo);
					
					if (mapa.containsKey("indqx_"+i) && !(mapa.get("indqx_"+i)+"").equals("") && !(mapa.get("orden_"+i)+"").equals(""))
					{
						mapa.put("NecHAnes_"+i, util.UtilidadValidacion.solCxNecesitaHojaAntesia(con, Utilidades.convertirAEntero(mapa.get("orden_"+i)+"")));
					}
					
					//Llaves para los procedimientos y articulos incluidos
					mapa.put("inclart_"+mapa.get("solicitud_"+i),"");
					mapa.put("inclserv_"+mapa.get("solicitud_"+i),"");
				}
				
				numRegiMapaOriginal=Utilidades.convertirAEntero(mapa.get("numRegistros")+"");
			} catch(Exception e)
			{
				logger.error("ERROR CONSULTA 1: ", e);
				throw e;
			}
			finally
			{
				UtilidadBD.cerrarObjetosPersistencia(ps1, rs1, null);
			}
			HashMap mapa1 =new HashMap();
			try {
				
				///CIRUGIAS
				logger.info("consulta 2 -->"+cadena2);
				ps2 = new PreparedStatementDecorator(con.prepareStatement(cadena2+" ORDER BY fechasolicitud,horasolicitud "));
				ps2.setString(1, ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(Utilidades.convertirAEntero(vo.get("institucion").toString())));
				ps2.setString(2, ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(Utilidades.convertirAEntero(vo.get("institucion").toString())));
				rs2 = new ResultSetDecorator(ps2.executeQuery());
				mapa1=UtilidadBD.cargarValueObject(rs2);
				for(int i=0;i<Utilidades.convertirAEntero(mapa1.get("numRegistros")+"");i++)
				{
					mapa.put("solicitud_"+numRegiMapaOriginal, mapa1.get("solicitud_"+i)+"");
					mapa.put("ccsolicitante_"+numRegiMapaOriginal, mapa1.get("ccsolicitante_"+i)+"");
					mapa.put("fechasolicitud_"+numRegiMapaOriginal, mapa1.get("fechasolicitud_"+i)+"");
					mapa.put("horasolicitud_"+numRegiMapaOriginal, mapa1.get("horasolicitud_"+i)+"");
					mapa.put("orden_"+numRegiMapaOriginal, mapa1.get("orden_"+i)+"");
					mapa.put("servicio_"+numRegiMapaOriginal, mapa1.get("servicio_"+i)+"");
					mapa.put("procedimiento_"+numRegiMapaOriginal, mapa1.get("procedimiento_"+i)+"");
					mapa.put("centroatencion_"+numRegiMapaOriginal, mapa1.get("centroatencion_"+i)+"");
					mapa.put("codigoviaingreso_"+numRegiMapaOriginal, mapa1.get("codigoviaingreso_"+i)+"");
					mapa.put("codigopaciente_"+numRegiMapaOriginal, mapa1.get("codigopaciente_"+i)+"");
					mapa.put("nombrepaciente_"+numRegiMapaOriginal, mapa1.get("nombrepaciente_"+i)+"");
					mapa.put("cuenta_"+numRegiMapaOriginal, mapa1.get("cuenta_"+i)+"");
					mapa.put("viaingreso_"+numRegiMapaOriginal, mapa1.get("viaingreso_"+i)+"");
					mapa.put("solicitante_"+numRegiMapaOriginal, mapa1.get("solicitante_"+i)+"");
					mapa.put("codigoestadohc_"+numRegiMapaOriginal, mapa1.get("codigoestadohc_"+i)+"");
					mapa.put("pyp_"+numRegiMapaOriginal, mapa1.get("pyp_"+i)+"");
					mapa.put("tiposolicitud_"+numRegiMapaOriginal, mapa1.get("tiposolicitud_"+i)+"");
					mapa.put("requierediagnostico_"+numRegiMapaOriginal, mapa1.get("requierediagnostico_"+i)+"");
					mapa.put("especialidad_"+numRegiMapaOriginal, mapa1.get("especialidad_"+i)+"");
					mapa.put("estadohc_"+numRegiMapaOriginal, mapa1.get("estadohc_"+i)+"");
					mapa.put("codigopeticion_"+numRegiMapaOriginal, mapa1.get("codigopeticion_"+i)+"");
					mapa.put("cama_"+numRegiMapaOriginal, mapa1.get("cama_"+i)+"");
					mapa.put("urgente_"+numRegiMapaOriginal, mapa1.get("urgente_"+i)+"");
					mapa.put("indqx_"+numRegiMapaOriginal, mapa1.get("indqx_"+i)+"");
					mapa.put("peticion_"+numRegiMapaOriginal, mapa1.get("peticion_"+i)+"");
					mapa.put("portatil_"+numRegiMapaOriginal, mapa1.get("portatil_"+i)+"");
					mapa.put("cantidadincluidos_"+numRegiMapaOriginal, mapa1.get("cantidadincluidos_"+i)+"");
					String tempo=Utilidades.consultarDiagnosticosPaciente(con, mapa1.get("cuenta_"+i)+"", Utilidades.convertirAEntero(mapa1.get("codigoviaingreso_"+i)+""));
					if(!tempo.trim().equals(""))
					{
						tempo=tempo.split(ConstantesBD.separadorSplit)[0];
					}
					mapa.put("diagnostico_"+numRegiMapaOriginal, tempo);
					
					if (mapa1.containsKey("indqx_"+i) && !(mapa1.get("indqx_"+i)+"").equals("") && !(mapa1.get("orden_"+i)+"").equals(""))
					{
						mapa.put("NecHAnes_"+numRegiMapaOriginal, util.UtilidadValidacion.solCxNecesitaHojaAntesia(con, Utilidades.convertirAEntero(mapa1.get("orden_"+i)+"")));
					}
					
					//Llaves para los procedimientos y articulos incluidos
					mapa.put("inclart_"+mapa1.get("solicitud_"+i),"");
					mapa.put("inclserv_"+mapa1.get("solicitud_"+i),"");
					numRegiMapaOriginal++;
				}
				mapa.put("numRegistros", numRegiMapaOriginal+"");
				}catch (Exception  e )
				{
					logger.error("ERROR CONSULTA 2: ", e);
					throw e;
				}
				finally
				{
					UtilidadBD.cerrarObjetosPersistencia(ps2, rs2, null);	
				}
				
			try {
				
				if(mostrarNoCruentosPet)
				{
					////PETICIONES
					logger.info("consulta 3 -->"+cadena3);
					ps3 = new PreparedStatementDecorator(con.prepareStatement(cadena3+" ORDER BY fechasolicitud,horasolicitud "));
					ps3.setString(1, ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(Utilidades.convertirAEntero(vo.get("institucion").toString())));
					ps3.setString(2, ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(Utilidades.convertirAEntero(vo.get("institucion").toString())));
					rs3 = new ResultSetDecorator(ps3.executeQuery());
					mapa1=UtilidadBD.cargarValueObject(rs3);
					for(int i=0;i<Utilidades.convertirAEntero(mapa1.get("numRegistros")+"");i++)
					{
						mapa.put("solicitud_"+numRegiMapaOriginal, mapa1.get("solicitud_"+i)+"");
						mapa.put("ccsolicitante_"+numRegiMapaOriginal, mapa1.get("ccsolicitante_"+i)+"");
						mapa.put("fechasolicitud_"+numRegiMapaOriginal, mapa1.get("fechasolicitud_"+i)+"");
						mapa.put("horasolicitud_"+numRegiMapaOriginal, mapa1.get("horasolicitud_"+i)+"");
						mapa.put("orden_"+numRegiMapaOriginal, mapa1.get("orden_"+i)+"");
						mapa.put("servicio_"+numRegiMapaOriginal, mapa1.get("servicio_"+i)+"");
						mapa.put("procedimiento_"+numRegiMapaOriginal, mapa1.get("procedimiento_"+i)+"");
						mapa.put("centroatencion_"+numRegiMapaOriginal, mapa1.get("centroatencion_"+i)+"");
						mapa.put("codigoviaingreso_"+numRegiMapaOriginal, mapa1.get("codigoviaingreso_"+i)+"");
						mapa.put("codigopaciente_"+numRegiMapaOriginal, mapa1.get("codigopaciente_"+i)+"");
						mapa.put("nombrepaciente_"+numRegiMapaOriginal, mapa1.get("nombrepaciente_"+i)+"");
						mapa.put("cuenta_"+numRegiMapaOriginal, mapa1.get("cuenta_"+i)+"");
						mapa.put("viaingreso_"+numRegiMapaOriginal, mapa1.get("viaingreso_"+i)+"");
						mapa.put("solicitante_"+numRegiMapaOriginal, mapa1.get("solicitante_"+i)+"");
						mapa.put("codigoestadohc_"+numRegiMapaOriginal, mapa1.get("codigoestadohc_"+i)+"");
						mapa.put("pyp_"+numRegiMapaOriginal, mapa1.get("pyp_"+i)+"");
						mapa.put("tiposolicitud_"+numRegiMapaOriginal, mapa1.get("tiposolicitud_"+i)+"");
						mapa.put("requierediagnostico_"+numRegiMapaOriginal, mapa1.get("requierediagnostico_"+i)+"");
						mapa.put("especialidad_"+numRegiMapaOriginal, mapa1.get("especialidad_"+i)+"");
						mapa.put("estadohc_"+numRegiMapaOriginal, mapa1.get("estadohc_"+i)+"");
						mapa.put("codigopeticion_"+numRegiMapaOriginal, mapa1.get("codigopeticion_"+i)+"");
						mapa.put("cama_"+numRegiMapaOriginal, mapa1.get("cama_"+i)+"");
						mapa.put("urgente_"+numRegiMapaOriginal, mapa1.get("urgente_"+i)+"");
						mapa.put("indqx_"+numRegiMapaOriginal, mapa1.get("indqx_"+i)+"");
						mapa.put("peticion_"+numRegiMapaOriginal, mapa1.get("peticion_"+i)+"");
						mapa.put("portatil_"+numRegiMapaOriginal, mapa1.get("portatil_"+i)+"");
						mapa.put("cantidadincluidos_"+numRegiMapaOriginal, mapa1.get("cantidadincluidos_"+i)+"");
						String tempo=Utilidades.consultarDiagnosticosPaciente(con, mapa1.get("cuenta_"+i)+"", Utilidades.convertirAEntero(mapa1.get("codigoviaingreso_"+i)+""));
						if(!tempo.trim().equals(""))
						{
							tempo=tempo.split(ConstantesBD.separadorSplit)[0];
						}
						mapa.put("diagnostico_"+numRegiMapaOriginal, tempo);
						
						if (mapa1.containsKey("indqx_"+i) && !(mapa1.get("indqx_"+i)+"").equals("") && !(mapa1.get("orden_"+i)+"").equals(""))
						{
							mapa.put("NecHAnes_"+numRegiMapaOriginal, util.UtilidadValidacion.solCxNecesitaHojaAntesia(con, Utilidades.convertirAEntero(mapa1.get("orden_"+i)+"")));
						}
						
						//Llaves para los procedimientos y articulos incluidos
						mapa.put("inclart_"+mapa1.get("solicitud_"+i),"");
						mapa.put("inclserv_"+mapa1.get("solicitud_"+i),"");
						numRegiMapaOriginal++;
					}
					mapa.put("numRegistros", numRegiMapaOriginal+"");
				}
					
			} catch (SQLException e) {
				logger.error("ERROR CONSULTA 3: ", e);
				throw e;
				
			} finally {
	//			
	//			
				UtilidadBD.cerrarObjetosPersistencia(ps3, rs3, null);
			}
		}catch (Exception e) {
			logger.error("ERROR: ", e);
		}
		return mapa;
	}
	
	/**
	 * @param finalidad 
	 * 
	 */
	public static String insertarRespuestaProcedimiento	(	Connection con,
																		String numeroSolicitud,
																		String fechaEjecucion,
																		String resultados,
																		String observaciones,
																		int tipoRecargo,
																		String comentarioHistoriaClinica,
																		String horaEjecucion,
																		int codigoMedicoResponde,
																		String loginUsuarioRegistraRes, 
																		int finalidad,
																		String observacionCapitacion
																		)
	{
		String nuevoValorSecuencia="";
		try
		{
			nuevoValorSecuencia=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_res_sol_proc")+"";
			PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(insertarRespuestaProcedimientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO res_sol_proc (" +
									   " codigo, " +
									   " numero_solicitud," +
									   " fecha_grabacion," +
									   " hora_grabacion," +
									   " fecha_ejecucion," +
									   " resultados," +
									   " tipo_recargo," +
									   " observaciones," +
									   " comentario_historia_clinica, " +
									   " hora_ejecucion )" +
									   " VALUES (?, ?, CURRENT_DATE,ValoresXDefecto,?,?,?,?,?,?)
			 */
			
			insertarStatement.setDouble(1, Utilidades.convertirADouble(nuevoValorSecuencia));
			insertarStatement.setInt(2, Utilidades.convertirAEntero(numeroSolicitud));
			insertarStatement.setDate(3,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaEjecucion)));
			
			if(!resultados.equals(""))			
				insertarStatement.setString(4,resultados);
			else
				insertarStatement.setNull(4,Types.VARCHAR);
				
			insertarStatement.setInt(5,tipoRecargo);
			insertarStatement.setString(6,observaciones);
			insertarStatement.setString(7,comentarioHistoriaClinica);
			insertarStatement.setString(8, horaEjecucion);
			
			insertarStatement.setInt(9,codigoMedicoResponde);
			insertarStatement.setString(10,loginUsuarioRegistraRes);
			if(!UtilidadTexto.isEmpty(observacionCapitacion))
				insertarStatement.setString(11,observacionCapitacion);
			else
				insertarStatement.setNull(11,Types.VARCHAR);
				
			
			if(insertarStatement.executeUpdate()>0)
			{
				if(finalidad>0)
				{
					String consTempo="update sol_procedimientos set finalidad=? where numero_solicitud=?";
					PreparedStatementDecorator psUS=new PreparedStatementDecorator(con, consTempo);
					psUS.setInt(1, finalidad);
					psUS.setInt(2, Utilidades.convertirAEntero(numeroSolicitud));
					psUS.executeUpdate();
				}
				return nuevoValorSecuencia;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error en la insercion de los datos del procedimiento Genérica: "+e);
			return "";
		}
		return "";
	}
	
	
	/**
	 * Actualiza el campo de Servicio Respuesta Procedimiento
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean actualizarCodigoCxServicioRespProc (Connection con, HashMap parametros)
	{
		logger.info("valor de la actualizacion >> "+parametros);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(StrActualizarRespuestaProcedimientoSerCx,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if(!parametros.get("codigoCxServicio").toString().equals(""))
				ps.setInt(1,Utilidades.convertirAEntero(parametros.get("codigoCxServicio").toString()));
			else
				ps.setNull(1,Types.INTEGER);
			
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("codigoPk").toString()));
			
			if(ps.executeUpdate() > 0)
				return true;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
			logger.info("error en actualizarCodigoCxServicioRespProc >> "+parametros);
		}
		
		return false;		
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean finaizarRespuestaSolProcedimiento (	Connection con,
																String numeroSolicitud,
																String acronimofinalizar
															)
	{
		try
		{
			PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(finaizarRespuestaSolProcedimientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE sol_procedimientos SET finalizada_respuesta= ? WHERE numero_solicitud= ? 
			 */
			
			insertarStatement.setString(1, acronimofinalizar);
			insertarStatement.setInt(2, Utilidades.convertirAEntero(numeroSolicitud));
			if(insertarStatement.executeUpdate()>0)
			{
				return true;
			}
		}
		catch (SQLException e)
		{
			logger.error("Error en finaizarRespuestaSolProcedimiento : "+e);
			return false;
		}
		return false;
	}
	
	/**
	 * Metodo que verifica si la solicitud es de cita, en caso de ser cita validar si se pude cambiar el estado a cumplida o no
	 * @param con
	 * @param numeroSolicitud
	 */
	public static String validacionCita(Connection con, int numeroSolicitud) 
	{
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(" SELECT codigo_cita from solicitudes s inner join servicios_cita sc ON(sc.numero_solicitud=s.numero_solicitud) where s.numero_solicitud = "+numeroSolicitud,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			//si la solicitud tiene cita, verficar si los servicios_cita asociado estan todas interpretadas o anuladas
			if(rs.next())
			{
				int codigoCita=rs.getInt(1);
				ps= new PreparedStatementDecorator(con.prepareStatement("SELECT count(1) from solicitudes where estado_historia_clinica not in ("+ConstantesBD.codigoEstadoHCAnulada+","+ConstantesBD.codigoEstadoHCRespondida+","+ConstantesBD.codigoEstadoHCInterpretada+") and numero_solicitud in(select numero_solicitud from servicios_cita where codigo_cita="+codigoCita+")",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSetDecorator rs1=new ResultSetDecorator(ps.executeQuery());
				//si no tiene solicitudes en estados diferentes a anuladas,interpretadas,respondidas entonces retorna el codigoCita..
				if(rs1.next()&&rs1.getInt(1)==0)
				{
					return codigoCita+"";
				}
			}
		}
		catch (SQLException e)
		{
			logger.error("Error en finaizarRespuestaSolProcedimiento : "+e);
			return "";
		}
		return "";
	}

	/**
	 * 
	 * @param con
	 * @param 
	 * @return
	 */
	public static boolean eliminarDiagnosticos (Connection con, String codigoRespuesta)
	{
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(eliminarDiagnosticosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(codigoRespuesta));
			pst.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error en la insercion de diagnóstico del procedimiento : "+e);
			return false;
		}
		return true;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param 
	 * @param acronimo
	 * @param codigoCie
	 * @param principal
	 * @param complicacion
	 * @param numero
	 * @param estado
	 * @return
	 */
	public static boolean insertarDiagnostico (Connection con,String codigoRespuesta,String acronimo,int codigoCie,boolean principal,boolean complicacion,int numero,String estado)
	{
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(insertarDiagnosticoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT " +
					"INTO diag_procedimientos " +
					"(codigo_respuesta,acronimo,tipo_cie,principal,complicacion,numero) " +
					"VALUES (?,?,?,?,?,?)
			 */
			
			pst.setInt(1,Utilidades.convertirAEntero(codigoRespuesta));
			pst.setString(2,acronimo);
			pst.setInt(3,codigoCie);
			pst.setBoolean(4, principal);				
			pst.setBoolean(5, complicacion);
			pst.setInt(6, numero);
			pst.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("Error en la insercion de diagnóstico del procedimiento : "+e);
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap datosHistoriaClinica (Connection con, String numeroSolicitud)
	{
		HashMap mapa= new HashMap();
		int cuenta=0;
		mapa.put("medicoSolicitante","");
		mapa.put("diagnosticoPresuntivo", "");
		
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(datosHistoriaClinicaMedicoSolicitanteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				mapa.put("medicoSolicitante",rs.getString("medicoSolicitante"));
			
			
			

			PreparedStatementDecorator pst1 = new PreparedStatementDecorator(con.prepareStatement(datosHistoriaCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst1.setInt(1,Utilidades.convertirAEntero(numeroSolicitud));
			ResultSetDecorator rs1=new ResultSetDecorator(pst1.executeQuery());
			if(rs1.next())
				 cuenta= rs1.getInt("cuenta");
			
			
			DtoDiagnostico dtoDiagnostico = Utilidades.getDiagnosticoPacienteCuenta(cuenta);


			if(dtoDiagnostico!=null)
			{
				String diagnostico = dtoDiagnostico.getAcronimoDiagnostico();						
						mapa.put("diagnosticoPresuntivo",diagnostico);
			}
			
			
			//@todo hacer lo del diagnostico
		}
		catch (SQLException e)
		{
			logger.error("Error en la insercion de diagnóstico del procedimiento : "+e);
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static String cargarResultadosAnteriores (Connection con, HashMap parametros)
	{
		String resultadosAnteriores="";
		try
		{
			if(parametros.containsKey("codigoCxServ") && !parametros.get("codigoCxServ").toString().equals(""))
			{
				PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cargarResultadosAnterioresCxStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, Utilidades.convertirAEntero(parametros.get("numeroSolicitud").toString()));
				ps.setInt(2, Utilidades.convertirAEntero(parametros.get("codigoCxServ").toString()));
				logger.info("valor de parametrso >> "+parametros+" >> "+cargarResultadosAnterioresCxStr);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				while(rs.next())
					resultadosAnteriores+=rs.getString("resultados");
			}
			else
			{
				PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cargarResultadosAnterioresStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, Utilidades.convertirAEntero(parametros.get("numeroSolicitud").toString()));
				logger.info("valor de parametrso >> "+parametros+" >> "+cargarResultadosAnterioresStr);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				while(rs.next())
					resultadosAnteriores+=rs.getString("resultados");
			}
			
		}
		catch(SQLException e)
		{
			logger.warn("Error " +e.toString());
			e.printStackTrace();
		}
		return resultadosAnteriores;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoRespuesta
	 * @return
	 */
	public static ResultSetDecorator cargarRespuestaBasica (Connection con, String codigoRespuesta)
	{
		ResultSetDecorator rs=null;
		//logger.info("valor de cargar respuesta >> "+cargarRespuestaBasicaStr+" >> "+codigoRespuesta);
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cargarRespuestaBasicaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Utilidades.convertirADouble(codigoRespuesta));
			rs=new ResultSetDecorator(ps.executeQuery());
		}
		catch(SQLException e)
		{
			logger.warn("Error " +e.toString());
		}
		return rs;
	}

	
	/**
	 * Método implementado para cargar los diagnósticos de un respuesta
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap cargarDiagnosticos (Connection con,String codigoRespuesta)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarDiagnosticosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(codigoRespuesta));
			logger.info("-->"+cargarDiagnosticosStr+"-----"+codigoRespuesta);
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarDiagnosticos : "+e);
			HashMap mapa = new HashMap();
			mapa.put("numRegistros","0");
			return mapa;
		}
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean servicioRequiereInterpretacion(Connection con, int numeroSolicitud) 
	{
		String cadena="SELECT coalesce(s.requiere_interpretacion,'N') as requiereinterpretacion from sol_procedimientos sp inner join servicios s on (sp.codigo_servicio_solicitado=s.codigo) where numero_solicitud = "+numeroSolicitud;
		try
		{
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return UtilidadTexto.getBoolean(rs.getString(1));
		}
		catch(SQLException e)
		{
			logger.warn("Error " +e.toString());
		}
		return false;
	}
	
	/**
	 * Consulta la informacion de los articulos incluidos dentro de una solicitud
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public static HashMap cargarArticulosIncluidosSolicitud(Connection con, HashMap parametros)
	{	
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strConsultarArticulosIncluidosSolProc,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(parametros.get("numeroSolicitud").toString()));			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
	        pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en Articulos Incluidos : "+e+" >> "+strConsultarArticulosIncluidosSolProc+" >> "+parametros);
			HashMap mapa = new HashMap();
			mapa.put("numRegistros","0");
			return mapa;
		}		
	}	
	
	
	/**
	 * Consulta la informacion de los servicios incluidos dentro de una solicitud
	 * @param Connection con 
	 * @param HashMap parametros
	 * */
	public static HashMap cargarServiciosIncluidosSolicitud(Connection con, HashMap parametros)
	{	
		logger.info("entro al sqlbase");
		
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strConsultarServiciosIncluidosSolProc,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("\n\n##################################################################################################");
			logger.info("cargarServiciosIncluidosSolicitud->"+strConsultarServiciosIncluidosSolProc+" tar->"+parametros.get("tarifario").toString()+" sol->"+parametros.get("numeroSolicitud"));
			logger.info("\n\n##################################################################################################");
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("tarifario").toString()));
			pst.setInt(2, Utilidades.convertirAEntero(parametros.get("tarifario").toString()));
			pst.setInt(3,Utilidades.convertirAEntero(parametros.get("numeroSolicitud").toString()));			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,true);
			pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en Servicios Incluidos : "+e+" >> "+strConsultarServiciosIncluidosSolProc+" >> "+parametros);
			HashMap mapa = new HashMap();
			mapa.put("numRegistros","0");
			return mapa;
		}		
	}
	
	/**
	 * Método para eliminar las respuestas de procedimientos de una solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean eliminarRespuestaProcedimientos(Connection con,HashMap campos)
	{
		boolean exito = true;
		try
		{
			int codigoResProc = ConstantesBD.codigoNuncaValido, codigoPlantilla = ConstantesBD.codigoNuncaValido;
			int consecutivoComponente = ConstantesBD.codigoNuncaValido, consecutivoEscala = ConstantesBD.codigoNuncaValido;
			int consecutivoCampoPlantilla = ConstantesBD.codigoNuncaValido;
			
			//********SE TOMAN LOS PARÁMETROS*************************************
			int numeroSolicitud = Utilidades.convertirAEntero(campos.get("numeroSolicitud").toString());
			int codigoCirugia = Utilidades.convertirAEntero(campos.get("codigoCirugia").toString());
			//*********************************************************************
			
			//Se consultan las respuestas
			String consulta = "SELECT codigo FROM res_sol_proc WHERE ";
			if(codigoCirugia>0)
				consulta += " codigo_cx_serv = ? ";
			else
				consulta += " numero_solicitud = ? ";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			if(codigoCirugia>0)
				pst.setInt(1,codigoCirugia);
			else
				pst.setInt(2,numeroSolicitud);
			
			ResultSetDecorator rs0 = new ResultSetDecorator(pst.executeQuery());
			
			//Iteración de las respuestas asociadas a la solicitud
			while(rs0.next())
			{
				codigoResProc = rs0.getInt("codigo");
				
				//1) SE CONSULTA EL CONSECUTIVO DE LA PLANTILLA DE LA RESPUESTA
				consulta = "SELECT codigo_pk as codigo FROM plantillas_res_proc WHERE res_sol_proc = ?";
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,codigoResProc);
				
				ResultSetDecorator rs1 = new ResultSetDecorator(pst.executeQuery());
				
				if(rs1.next())
				{
					codigoPlantilla = rs1.getInt("codigo");
					//*******ELIMINACION DE TODO LO QUE SEA COMPONENTE********************+
					//A) Se consultan los procedimientos que se hayan registrado en la plantilla
					consulta = "SELECT componente_res_proc as consecutivo_componente FROM plantillas_comp_res_proc WHERE plantilla_res_proc = ?";
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,codigoPlantilla);
					ResultSetDecorator rs2 = new ResultSetDecorator(pst.executeQuery());
					
					while(rs2.next())
					{
						consecutivoComponente = rs2.getInt("consecutivo_componente");
						//B) Se elminan todos los valores almacenados del componente
						consulta = "DELETE FROM valores_comp_res_proc WHERE componente_res_proc = ?";
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setInt(1,consecutivoComponente);
						pst.executeUpdate();
						
						//C) Se elimina el regitro que enlaza la plantilla con el registro de componente
						consulta = "DELETE FROM plantillas_comp_res_proc WHERE componente_res_proc = ? and plantilla_res_proc = ?";
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setInt(1,consecutivoComponente);
						pst.setInt(2,codigoPlantilla);
						pst.executeUpdate();
						
						//D) Se elimina el registro de componente por respuesta
						consulta = "DELETE FROM componentes_res_proc WHERE codigo_pk = ?";
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setInt(1,consecutivoComponente);
						pst.executeUpdate();
						
					}
					//***********************************************************************
					
					//********* ELIMINACION DE TODO LO QUE SEA ESCALA************************
					//A) Se buscan las escalas que se hayan registrado desde esa plantilla
					consulta = "SELECT escalas_ingreso as consecutivo_escala FROM plantillas_escala_res_proc WHERE plantilla_res_proc = ?";
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,codigoPlantilla);
					rs2 = new ResultSetDecorator(pst.executeQuery());
					
					while(rs2.next())
					{
						consecutivoEscala = rs2.getInt("consecutivo_escala");
						//B) Se elimina el registro de la escala con la plantilla
						consulta = "DELETE FROM plantillas_escala_res_proc WHERE plantilla_res_proc = ? and escalas_ingreso = ?";
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setInt(1,codigoPlantilla);
						pst.setInt(2,consecutivoEscala);
						pst.executeUpdate();
						
						//C) Se eliminan los valores de la escala
						consulta = "DELETE FROM escalas_campos_ingresos WHERE escala_ingreso = ?";
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setInt(1,consecutivoEscala);
						pst.executeUpdate();
						
						//D) Se eliminan adjuntos de la escala
						consulta = "DELETE FROM escalas_adjuntos_ingreso WHERE escala_ingreso = ?";
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setInt(1,consecutivoEscala);
						pst.executeUpdate();
						
						//E) Se elimina la escala encontrada
						consulta = "DELETE FROM escalas_ingresos WHERE codigo_pk = ?";
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setInt(1,consecutivoEscala);
						pst.executeUpdate();
						
					}
					//************************************************************************
					
					//********************ELIMINACION DE TODO LO 	QUE SEA CAMPOS*****************
					//A) Se buscan todos los campos que se hayan registrado
					consulta = "SELECT codigo_pk as consecutivo_campo FROM plantillas_proc_campos WHERE plantilla_res_proc = ?";
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,codigoPlantilla);
					rs2 = new ResultSetDecorator(pst.executeQuery());
					
					while(rs2.next())
					{
						consecutivoCampoPlantilla = rs2.getInt("consecutivo_campo");
						//B) Se eliminan los valores del campo registrado
						consulta = "DELETE FROM valores_plan_proc_camp WHERE plantilla_proc_campos = ?";
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setInt(1,consecutivoCampoPlantilla);
						pst.executeUpdate();
						
						//C) Se elimina el registro del campo
						consulta = "DELETE FROM plantillas_proc_campos WHERE codigo_pk = ?";
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setInt(1,consecutivoCampoPlantilla);
						pst.executeUpdate();
					}
					//******************************************************************************
				}
				
				//2) ********POR ULTIMO ELIMINAR EL REGISTRO DE RES SOL PROC******************************
				consulta = "DELETE FROM res_sol_proc WHERE codigo = ?";
				pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,codigoResProc);
				pst.executeUpdate();
				//**************************************************************************************
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarRespuestaProcedimientos:"+e);
			exito = false;
		}
		return exito;
	}
	
	//*************************************************************************************************************************
	
	//*************************************************************************************************************************
	
	/**
	 * Cargar Dto Procedimiento
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static DtoProcedimiento cargarDtoProcedimiento(Connection con,HashMap parametros)
	{
		DtoProcedimiento proc = new DtoProcedimiento();
		DtoRespuestaProcedimientos res;
		ArrayList<DtoRespuestaProcedimientos> array = new ArrayList<DtoRespuestaProcedimientos>();
		ArrayList<InfoDatosString> arrayS = new ArrayList<InfoDatosString>();
		InfoDatosString info;
		
		//logger.info("valor de la hora cargar sqlpro 1 >>"+UtilidadFecha.getHoraSegundosActual() );
		try
		{
			logger.info("valor de la consulta >> "+strConsultaDtoProcedimiento+" >> "+parametros.get("numeroSolicitud").toString()+" >> "+parametros.get("tarifario").toString());
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strConsultaDtoProcedimiento,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("tarifario").toString()));
			pst.setInt(2, Utilidades.convertirAEntero(parametros.get("tarifario").toString()));			
			pst.setInt(3, Utilidades.convertirAEntero(parametros.get("numeroSolicitud").toString()));			
			pst.setInt(4, Utilidades.convertirAEntero(parametros.get("tarifario").toString()));
			pst.setInt(5, Utilidades.convertirAEntero(parametros.get("tarifario").toString()));			
			pst.setInt(6, Utilidades.convertirAEntero(parametros.get("numeroSolicitud").toString()));
						
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			//logger.info("\n\n\n\n\n\nvalor de la hora cargar sqlpro 2 >>"+UtilidadFecha.getHoraSegundosActual() );
			
			
			if(rs.next())				
			{
				proc.setNumeroSolicitud(Utilidades.convertirAEntero(rs.getString(1)));
				proc.setCodigoCentroCostoSolicitado(Utilidades.convertirAEntero(rs.getString(2)));
				proc.setCodigoServicioSolicitado(Utilidades.convertirAEntero(rs.getString(3)));				
				proc.setFechaSolicitud(rs.getString(4));
				proc.setHoraSolicitud(rs.getString(5));
				proc.setCodigoCuenta(Utilidades.convertirAEntero(rs.getString(6)));				
				proc.setCodigoViaIngreso(Utilidades.convertirAEntero(rs.getString(7)));
				proc.setCodigoPkResProcMuerto(Utilidades.convertirAEntero(rs.getString(8)));
				proc.setCodigoTipoSolicitud(Utilidades.convertirAEntero(rs.getString(9)));
				proc.setCodigoGrupoServicio(Utilidades.convertirAEntero(rs.getString(10)));
				proc.setNombreServicioSolicitado(rs.getString(11));
				proc.setCodigoConvenio(rs.getInt(12));
				proc.setNombreConvenio(rs.getString(13));
				proc.setCodigoTipoRegimen(rs.getString(14));
				proc.setNombreTipoRegimen(rs.getString(15));
				proc.setInterpretacion(rs.getString("interpretacion"));

				ResultSetDecorator rs1;
				//********************************************************************************************************							
				//logger.info("valor del sql >> "+rs.getString(3)+" >> "+rs.getString(2)+" "+ConstantesBD.codigoEstadoHCRespondida+" "+parametros.get("numeroSolicitud").toString());
				HashMap parametrosTempo = new HashMap();
				parametrosTempo.put("codigoCentroAtencion",parametros.get("codigoCentroAtencion"));
				parametrosTempo.put("codigoGrupoServicio",proc.getCodigoGrupoServicio());
				parametrosTempo.put("codigoCentroCostos",rs.getString(2));
				proc.setNumeroResSolProcAnteriores(getNumeroRespuestasAnteriores(con, parametrosTempo));				
				//*************************************************************************************************************
				
				ResultSetDecorator rs2;			
				String cadena = strConsultaDtoRespuestaProce;
				
				if(parametros.containsKey("codigoPkRespProc") 
						&& !parametros.get("codigoPkRespProc").toString().equals(""))				
					cadena += " AND res.codigo = "+parametros.get("codigoPkRespProc").toString();				
				
				//Consulta la respuesta a la solicitud de proccedimientos				
				pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,proc.getNumeroSolicitud());
				pst.setInt(2,proc.getNumeroSolicitud());
				
				rs1 = new ResultSetDecorator(pst.executeQuery());				
				
				while(rs1.next())
				{
					res = new DtoRespuestaProcedimientos();
					res.setCodigoPkRespuestaProce(Utilidades.convertirAEntero(rs1.getString(1)));
					res.setNumeroResSolProcHistorico(Utilidades.convertirAEntero(rs1.getString(2)));
					res.setNombreEspecialidadProfResponde(rs1.getString(9).toString());
					res.setCodigoFinalidadServicio(rs1.getInt("finalidad"));
					res.setNombreFinalidadServicio(rs1.getString("nomfinalidad"));
					if(rs1.getString(3).toString().equals(""))
						res.setEsMuerto(ConstantesBD.acronimoNo);
					else
					{
						res.setEsMuerto(ConstantesBD.acronimoSi);
						res.setDiagnosticoMuerteCadenaCompleta(rs1.getString(3).toString()+ConstantesBD.separadorSplit+rs1.getString(4).toString()+ConstantesBD.separadorSplit+rs1.getString(5).toString());
						res.setCodigoDiagnosticoMuerte(rs1.getString(3).toString());
						res.setCodigoTipoCieDiagnosticoMuerte(rs1.getString(4).toString());
						res.setNombreDiagnosticoMuerte(rs1.getString(5).toString());
						res.setFechaMuerte(rs1.getString(6).toString());
						res.setHoraMuerte(rs1.getString(7).toString());		
						res.setCertificadoDefuncion(rs1.getString(8).toString());
						
					}
					res.setInterpretacion(rs1.getString(10));		
					
					pst =  new PreparedStatementDecorator(con.prepareStatement(strConsultaOtrosComentarios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,res.getCodigoPkRespuestaProce());
					
					rs2 = new ResultSetDecorator(pst.executeQuery());
					arrayS = new ArrayList<InfoDatosString>();
					
					//logger.info("\n\n\n\n\n\nvalor de la hora cargar sqlpro 4 >>"+UtilidadFecha.getHoraSegundosActual() );
					
					while(rs2.next())
					{
						info = new InfoDatosString();
						info.setCodigo(rs2.getString(2));
						info.setDescripcion(rs2.getString(3));
						info.setNombre(rs2.getString(4));
						info.setIndicativo(ConstantesBD.acronimoSi);
						info.setActivo(true);
						arrayS.add(info);
					}
					
					res.setOtrosComentariosArray(arrayS);
					array.add(res);
				}
				
				proc.setRespuestaProceArray(array);
			}	
			else
			{
				logger.info("valor de la consulta (SIN RESULTADOS) >> "+strConsultaDtoProcedimiento+" >> "+parametros.get("numeroSolicitud").toString());
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarDtoProcedimiento : "+e+" >> "+parametros);
			new DtoProcedimiento();			
		}
		
		return proc;
	}
	
	//*************************************************************************************************************************
	
	/**
	 * consulta el numero de respuestas anteriores
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static int getNumeroRespuestasAnteriores(Connection con, HashMap parametros)
	{
		int cantidad = ConstantesBD.codigoNuncaValido;
		int codigoCentroAtencion = Utilidades.convertirAEntero(parametros.get("codigoCentroAtencion").toString());
		int codigoGrupoServicio = Utilidades.convertirAEntero(parametros.get("codigoGrupoServicio").toString());		
		int codigoCentroCostos = Utilidades.convertirAEntero(parametros.get("codigoCentroCostos").toString());				

		try
		{
			logger.info("*******************************************************************************************");
			logger.info("valor del centro de atencion >> "+codigoCentroAtencion+" valor del grupo del servicio >> "+codigoGrupoServicio+" >> valor del centro de costo >> "+codigoCentroCostos);
			logger.info("sql >> "+strConsultaConsecutivoRespuesta+" >> "+codigoCentroAtencion+" "+codigoGrupoServicio+" "+codigoCentroCostos);		
			logger.info("*******************************************************************************************");

			ResultSetDecorator rs1;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strConsultaConsecutivoRespuesta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoCentroAtencion);
			pst.setInt(2,codigoGrupoServicio);
			pst.setInt(3,codigoCentroCostos);

			rs1 = new ResultSetDecorator(pst.executeQuery());

			if(rs1.next())
			{
				cantidad = Utilidades.convertirAEntero(rs1.getString(1));
				if(cantidad >= 0)
					cantidad = cantidad +1;
			}									
		}
		catch (Exception e){
			e.printStackTrace();
		}	
	
		return cantidad;
	}
	
	//*************************************************************************************************************************
	
	/**
	 * Guarda Otros Comentarios
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean guardarOtrosComentarios(Connection con, HashMap parametros)
	{
		boolean respuesta = true;
				
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strInsertarOtrosComentarios,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,parametros.get("res_sol_proc").toString());
			pst.setString(2,parametros.get("codigo_medico").toString());
			
			if(!parametros.get("descripcion").toString().trim().equals(""))
				pst.setString(3,parametros.get("descripcion").toString());
			else
				pst.setNull(3,Types.VARCHAR);
				
			pst.setString(4,parametros.get("usuario_modifica").toString());
			pst.setString(5,parametros.get("fecha_modifica").toString());
			pst.setString(6,parametros.get("hora_modifica").toString());
			
			if(pst.executeUpdate()>0)
				return true;
			else
				return false;
			
		}
		catch (SQLException e) {
			logger.info("Error en guardarOtrosComentarios "+e+" >> "+parametros);
		}	
		
		return respuesta;		
	}
	
	//*************************************************************************************************************************
	
	
	/**
	 * Actualiza el numero de respuestas anteriores
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean actualizarNoRespuestasAnteriores(Connection con, HashMap parametros)
	{
		boolean respuesta = true;
		int codigoCentroAtencion = Utilidades.convertirAEntero(parametros.get("codigoCentroAtencion").toString());
		int codigoGrupoServicio = Utilidades.convertirAEntero(parametros.get("codigoGrupoServicio").toString());		
		int codigoCentroCostos = Utilidades.convertirAEntero(parametros.get("codigoCentroCostos").toString());
				
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strActualizarNoRespuestaAnte,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if(Utilidades.convertirAEntero(parametros.get("numero").toString()) > 0)
				pst.setInt(1,Utilidades.convertirAEntero(parametros.get("numero").toString()));
			else
				pst.setNull(1,Types.INTEGER);
			
			pst.setString(2,parametros.get("res_sol_proc").toString());
									
			if(pst.executeUpdate()>0)
			{
				if(Utilidades.convertirAEntero(parametros.get("numero").toString()) > 0)
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(strActualizarConsecutivoOrden,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(parametros.get("numero").toString()));
					pst.setInt(2,codigoCentroAtencion);
					pst.setInt(3,codigoGrupoServicio);
					pst.setInt(4,codigoCentroCostos);
					
					if(pst.executeUpdate()>0)			
						return true;
					else
						return false;
				}
				else
					return true;
			}
			else
				return false;
			
		}
		catch (SQLException e) {
			logger.info("Error en actualizarNoRespuestasAnteriores "+e+" >> "+parametros);
		}	
		
		return respuesta;		
	}
	
	//*************************************************************************************************************************
	
	/**
	 * Guarda Muerte del Paciente desde Respuesta de Procedimientos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean guardarMuertePacienteRespProc(Connection con, HashMap parametros)
	{
		boolean respuesta = true;
				
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strExisteEgresoCuenta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(parametros.get("cuenta").toString()));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				if(rs.getInt(1) == 0)
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(strInsertarEgresoResProc,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Utilidades.convertirAEntero(parametros.get("cuenta").toString()));
					pst.setString(2,parametros.get("diagnostico_muerte").toString());
					pst.setInt(3,Utilidades.convertirAEntero(parametros.get("diagnostico_muerte_cie").toString()));
					pst.setString(4,parametros.get("diagnostico_muerte").toString());
					pst.setInt(5,Utilidades.convertirAEntero(parametros.get("diagnostico_muerte_cie").toString()));
					
					pst.setString(6,"1");
					pst.setInt(7,0);
					pst.setString(8,"1");
					pst.setInt(9,0);
					pst.setString(10,"1");
					pst.setInt(11,0);
					
					pst.setBoolean(12,false);
					pst.setInt(13,Utilidades.convertirAEntero(parametros.get("codigoPkRespProc").toString()));
					
					if(pst.executeUpdate()>0)
					{
						pst =  new PreparedStatementDecorator(con.prepareStatement(strActualizarMuertePaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						pst.setDate(1,Date.valueOf(parametros.get("fechaMuerte").toString()));
						pst.setString(2,parametros.get("horaMuerte").toString());
						pst.setString(3,parametros.get("certificado").toString());
						pst.setString(4,parametros.get("codigoPaciente").toString());				
						
						if(pst.executeUpdate()>0)
							return true;
						else
							return false;
					}
					else
						return false;					
				}				
			}	
		}
		catch (SQLException e) {
			logger.info("Error en guardarMuertePacienteRespProc "+e+" >> "+parametros);
		}	
		
		return respuesta;		
	}
	
	//*************************************************************************************************************************
	

	/**
	 * 
	 * @param con
	 * @param codigoResputa
	 * @param observacionesRes
	 * @return
	 */
	public static boolean actualizarObservacionesRespuesta(Connection con,String codigoResputa, String observacionesRes) 
	{
		String cadena="UPDATE res_sol_proc SET observaciones = ? WHERE codigo = ?  ";
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, observacionesRes);
			ps.setInt(2,Utilidades.convertirAEntero(codigoResputa));
			
			if(ps.executeUpdate() > 0)
				return true;
			
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;		
	}
}