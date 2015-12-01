package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;

import com.princetonsa.dto.manejoPaciente.DtoAdjAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDiagAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoEnvioAutorizacion;
import com.princetonsa.mundo.manejoPaciente.ListadoIngresos;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * Clase utilizada para el manejo de los listados de ingresos de un paciente
 * @author Jhony Alexander Duque A.
 * @modificated Felipe Pérez Granda
 *
 */


public class SqlBaseListadoIngresosDao {
	
	
	private static Logger logger = Logger.getLogger(SqlBaseListadoIngresosDao.class);

	
	/***********************************************************************************************************************************
	 * NUEVO DESARROLLO
	 ***********************************************************************************************************************************/

	
	private  final static String consultarListadoIngresos = "SELECT " +
																	" i.id As id_ingreso0, " +
																	" i.consecutivo As consecutivo_ingreso1, " +
																	" i.centro_atencion As centro_atencion2," +
																	" getnomcentroatencion(i.centro_atencion) As nombre_centro_atencion3, " +
																	" i.estado As estado_ingreso4, " +
																	" to_char(i.fecha_ingreso,'DD/MM/YYYY') || ' ' || i.hora_ingreso As fecha_apertura_ingreso5, " +
																	" c.via_ingreso As via_ingreso6, " +
																	" getnombreviaingreso(c.via_ingreso) As nombre_via_ingreso7, " +
																	" coalesce(to_char(i.fecha_egreso,'DD/MM/YYYY'),'')||' '||coalesce(substr(i.hora_egreso,0,6), '') As fecha_cierre_ingreso8, " +
																	" c.id As numero_cuenta9, " +
																	" c.estado_cuenta AS estado_cuenta10, " +
																	" getnombreestadocuenta(c.estado_cuenta) As nombre_estado_cuenta11 " +
															" FROM ingresos i " +
															" INNER JOIN cuentas c ON (c.id_ingreso=i.id) " +
															" WHERE i.codigo_paciente=?" +
															" AND i.institucion=? " +
															" AND i.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoCerrado+"','"+ConstantesIntegridadDominio.acronimoEstadoAbierto+"') " +
															" ORDER BY consecutivo_ingreso1 DESC ";
	
	
	private final static String consultarConveniosXIngreso=" SELECT " +
																" sc.convenio As codigo, " +
																" getnombreconvenio(sc.convenio) As nombre," +
																" sc.contrato As contrato," +
																" coalesce(sc.nro_autorizacion,'') As autorizacion, " +
																" sc.sub_cuenta As subcuenta " +
														" FROM sub_cuentas sc " +
														" WHERE sc.ingreso=? ";
	
	
	private final static String consultaServiciosArticulos=" SELECT " +
																	" dc.solicitud As solicitud0, " +
																	" s.consecutivo_ordenes_medicas As consecutivo_ordenes1, " +
																	" s.tipo As tipo_solicitud2, " +
																	" getnomtiposolicitud(s.tipo) As nombre_tipo_solicitud3, " +
																	" s.estado_historia_clinica As estado_historia_clinica4, " +
																	" getestadosolhis(s.estado_historia_clinica) As nombre_estado_hist_clinica5, " +
																	" s.centro_costo_solicitante As centro_costo_solicita6, " +
																	" getnomcentrocosto(s.centro_costo_solicitante) As nombre_centro_costo_solicita7, " +
																	" s.centro_costo_solicitado As centro_costo_ejecuta8, " +
																	" getnomcentrocosto(s.centro_costo_solicitado) As nombre_centro_costo_ejecuta9, " +
																	" sc.porcentaje_autorizado As porcentaje_autorizado10, " +
																	" sc.monto_autorizado As monto_autorizado11, " +
																	" coalesce(dc.valor_unitario_cargado,0) As valor_unitario12, " +
																	" coalesce(dc.valor_total_cargado,0) As valor_total13, " +
																	" dc.estado As estado_cargo14, " +
																	" getestadosolfac(dc.estado) As nombre_estado_cargo15, " +
																	" coalesce(to_char(s.fecha_solicitud,'DD/MM/YYYY'),' ')||' '||coalesce(substr(s.hora_solicitud,0,6),' ') As fecha_solicitud16, " +
																	" case when dc.servicio is null then getdescripcionarticulo(dc.articulo) else getobtenercodigocupsserv (dc.servicio,"+ConstantesBD.codigoTarifarioCups+") ||' - '||getnombreservicio(dc.servicio,"+ConstantesBD.codigoTarifarioCups+") end As serv_articulo17, " +
																	" sum(dc.cantidad_cargada) as cantidad18, " +
																	" coalesce(dc.requiere_autorizacion,'') As requiere_autorizacion19, " +
																	//" CASE WHEN dc.nro_autorizacion IS NULL AND dc.servicio IS NOT NULL THEN getAutorizacionSolicitud(dc.solicitud,gettiposervicio(dc.servicio),dc.servicio,s.tipo) ELSE coalesce(dc.nro_autorizacion,'') END As numero_autorizacion20, " +
																	" dc.codigo_detalle_cargo As codigo_detalle_cargo21, " +
																	" getnombretipoasocio(dc.tipo_asocio) as nombre_tipo_asocio24, " +
																	" getnombreservicio(dc.servicio_cx,0) As servicio_cx25, " +
																	" CASE WHEN s.tipo="+ConstantesBD.codigoTipoSolicitudCirugia+" AND dc.articulo is not null THEN '"+ConstantesBD.acronimoSi+"' ELSE '"+ConstantesBD.acronimoNo+"' END As es_material_especial26, " +
																	" coalesce(a.codigo,"+ConstantesBD.codigoNuncaValido+") as cod_autorizacion27, " +
																	" coalesce(da.codigo,"+ConstantesBD.codigoNuncaValido+") as cod_det_autorizacion28, " +
																	" coalesce(da.estado,'') as estado_autorizacion29, " +
																	" coalesce(a.tipo_tramite,'') as tipo_tramite30," +
																	" s.urgente as urgente31, " +
																	" coalesce(ra.vigencia,0) as vigencia, " +
																	" coalesce(to_char(ra.fecha_autorizacion,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_autorizacion36, "+
																	" coalesce(ra.hora_autorizacion,'') as hora_autorizacion37, "+
																	" coalesce(tv.tipo,'') as tipo_vigencia," +
																	" sc.sub_cuenta as id_sub_cuenta34, " +
																	" dc.convenio as codigo_convenio35," +
																	" coalesce(s.codigo_medico,"+ConstantesBD.codigoNuncaValido+") AS medico_sol38," +
																	" CASE WHEN s.codigo_medico IS NULL THEN '' ELSE getnombrepersona(s.codigo_medico) END AS nombre_medico_sol39," +
																	" CASE WHEN ra.fecha_fin_autorizada IS NULL THEN '' ELSE to_char(ra.fecha_fin_autorizada,'dd/mm/yyyy') END AS fecha_fin_autoriza," +
																	" CASE WHEN dc.servicio IS NULL THEN '' ELSE gettiposervicio(dc.servicio) END AS tipo_servicio41 " +
															" FROM det_cargos dc " +
															" INNER JOIN solicitudes s ON (s.numero_solicitud=dc.solicitud)" +
															" INNER JOIN sub_cuentas sc ON (sc.sub_cuenta=dc.sub_cuenta) " +
															" LEFT OUTER JOIN det_autorizaciones da ON(da.det_cargo = dc.codigo_detalle_cargo and da.activo = '"+ConstantesBD.acronimoSi+"') " +
															" LEFT OUTER JOIN autorizaciones a ON(a.codigo = da.autorizacion ) " +
															" LEFT OUTER JOIN resp_autorizaciones ra ON(ra.det_autorizacion = da.codigo) " +
															" LEFT OUTER JOIN tipos_vigencia tv ON(tv.codigo = ra.tipo_vigencia) " ;
	
	/**
	 * Método que realiza la consulta de los servicios ambulatorios
	 */
	private final static String consultaServiciosAmbulatorios = "SELECT "+ 
		"oa.codigo As solicitud0, "+ 
		"coalesce(oa.consecutivo_orden,'') As consecutivo_ordenes1, "+ 
		"oa.tipo_orden As tipo_solicitud2, "+ 
		"toa.descripcion As nombre_tipo_solicitud3, "+ 
		"oa.estado As estado_historia_clinica4, "+ 
		"eoa.descripcion As nomestadohiscli5, "+ 
		"coalesce(oa.centro_costo_solicita,"+ConstantesBD.codigoNuncaValido+") As centro_costo_solicita6, "+ 
		"coalesce(getnomcentrocosto(oa.centro_costo_solicita),'') As nombre_centro_costo_solicita7, "+ 
		ConstantesBD.codigoNuncaValido+" As centro_costo_ejecuta8, "+ 
		"'' As nombre_centro_costo_ejecuta9, "+ 
		"0 As porcentaje_autorizado10, "+ 
		"0 As monto_autorizado11, "+ 
		"0 As valor_unitario12, "+ 
		"0 As valor_total13, "+ 
		ConstantesBD.codigoNuncaValido+" As estado_cargo14, "+ 
		"'' As nombre_estado_cargo15, "+ 
		"coalesce(to_char(oa.fecha,'"+ConstantesBD.formatoFechaAp+"'),'')||' '||coalesce(substr(oa.hora,0,6),'') As fecha_solicitud16, "+ 
		"getobtenercodigocupsserv (doas.servicio,"+ConstantesBD.codigoTarifarioCups+") ||' - '||getnombreservicio(doas.servicio,"+ConstantesBD.codigoTarifarioCups+") as serv_articulo17, "+
		"doas.cantidad as cantidad18, "+ 
		"'"+ConstantesBD.acronimoNo+"' As requiere_autorizacion19, "+ 
		ConstantesBD.codigoNuncaValido+" As codigo_detalle_cargo21, "+ 
		"'' as nombre_tipo_asocio24, "+ 
		"'' As servicio_cx25, "+ 
		"'"+ConstantesBD.acronimoNo+"' As es_material_especial26, "+ 
		"coalesce(a.codigo,"+ConstantesBD.codigoNuncaValido+") as cod_autorizacion27, "+ 
		"coalesce(da.codigo,"+ConstantesBD.codigoNuncaValido+") as cod_det_autorizacion28, "+ 
		"coalesce(da.estado,'') as estado_autorizacion29, "+ 
		"coalesce(a.tipo_tramite,'') as tipo_tramite30," +
		"oa.urgente as urgente31, "+
		"coalesce(ra.vigencia,0) as vigencia, " +
		"coalesce(to_char(ra.fecha_autorizacion,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_autorizacion36, "+
		"coalesce(ra.hora_autorizacion,'') as hora_autorizacion37, "+
		"coalesce(tv.tipo,'') as tipo_vigencia, " +
		"-1 as id_sub_cuenta34, " +
		"coalesce(a.convenio,"+ConstantesBD.codigoNuncaValido+") as codigo_convenio35," +
		"CASE WHEN oa.usuario_solicita IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE u.codigo_persona END AS medico_sol38, " +		
		"CASE WHEN oa.usuario_solicita IS NULL THEN '' ELSE getnombreusuario(oa.usuario_solicita) END AS nombre_medico_sol39, " +
		"CASE WHEN ra.fecha_fin_autorizada IS NULL THEN '' ELSE to_char(ra.fecha_fin_autorizada,'dd/mm/yyyy') END AS fecha_fin_autoriza, " +
		"CASE WHEN doas.servicio IS NULL THEN '' ELSE gettiposervicio(doas.servicio) END AS tipo_servicio41 " +
		"FROM ordenes_ambulatorias oa "+ 
		"INNER JOIN tipos_ordenes_ambulatorias toa ON(toa.codigo = oa.tipo_orden) "+
		"INNER JOIN estados_ord_ambulatorias eoa ON(eoa.codigo = oa.estado) "+
		"INNER JOIN det_orden_amb_servicio doas ON(doas.codigo_orden=oa.codigo) "+
		"LEFT OUTER JOIN det_autorizaciones da ON(da.orden_ambulatoria = oa.codigo and da.activo = '"+ConstantesBD.acronimoSi+"') "+ 
		"LEFT OUTER JOIN autorizaciones a ON(a.codigo = da.autorizacion) "+
		"LEFT OUTER JOIN resp_autorizaciones ra ON(ra.det_autorizacion = da.codigo) " +
		"LEFT OUTER JOIN tipos_vigencia tv ON(tv.codigo = ra.tipo_vigencia) " +
		"LEFT OUTER JOIN usuarios u ON (u.login = oa.usuario_solicita) " ;
	
	private static final String consultaArticulosAmbulatorios = "SELECT "+ 
		"oa.codigo As solicitud0, "+ 
		"coalesce(oa.consecutivo_orden,'') As consecutivo_ordenes1, "+ 
		"oa.tipo_orden As tipo_solicitud2, "+ 
		"toa.descripcion As nombre_tipo_solicitud3, "+ 
		"oa.estado As estado_historia_clinica4, "+ 
		"eoa.descripcion As nomestadohiscli5, "+ 
		"coalesce(oa.centro_costo_solicita,"+ConstantesBD.codigoNuncaValido+") As centro_costo_solicita6, "+ 
		"coalesce(getnomcentrocosto(oa.centro_costo_solicita),'') As nombre_centro_costo_solicita7, "+ 
		ConstantesBD.codigoNuncaValido+" As centro_costo_ejecuta8, "+ 
		"'' As nombre_centro_costo_ejecuta9, "+ 
		"0 As porcentaje_autorizado10, "+ 
		"0 As monto_autorizado11, "+ 
		"0 As valor_unitario12, "+ 
		"0 As valor_total13, "+ 
		ConstantesBD.codigoNuncaValido+" As estado_cargo14, "+ 
		"'' As nombre_estado_cargo15, "+ 
		"coalesce(to_char(oa.fecha,'"+ConstantesBD.formatoFechaAp+"'),'')||' '||coalesce(substr(oa.hora,0,6),'') As fecha_solicitud16, "+ 
		"getdescripcionarticulo(doaa.articulo) As serv_articulo17, "+
		"doaa.cantidad as cantidad18, "+ 
		"'"+ConstantesBD.acronimoNo+"' As requiere_autorizacion19, "+ 
		ConstantesBD.codigoNuncaValido+" As codigo_detalle_cargo21, "+ 
		"'' as nombre_tipo_asocio24, "+ 
		"'' As servicio_cx25, "+ 
		"'"+ConstantesBD.acronimoNo+"' As es_material_especial26, "+ 
		"coalesce(a.codigo,"+ConstantesBD.codigoNuncaValido+") as cod_autorizacion27, "+ 
		"coalesce(da.codigo,"+ConstantesBD.codigoNuncaValido+") as cod_det_autorizacion28, "+ 
		"coalesce(da.estado,'') as estado_autorizacion29, "+ 
		"coalesce(a.tipo_tramite,'') as tipo_tramite30," +
		"oa.urgente as urgente31,  "+
		"coalesce(ra.vigencia,0) as vigencia, " +
		"coalesce(to_char(ra.fecha_autorizacion,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_autorizacion36, "+
		"coalesce(ra.hora_autorizacion,'') as hora_autorizacion37, "+
		"coalesce(tv.tipo,'') as tipo_vigencia, " +
		"-1 as id_sub_cuenta34, " +
		"coalesce(a.convenio,-1) as codigo_convenio35, " +
		"CASE WHEN oa.usuario_solicita IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE u.codigo_persona END AS medico_sol38, " +		
		"CASE WHEN oa.usuario_solicita IS NULL THEN '' ELSE getnombreusuario(oa.usuario_solicita) END AS nombre_medico_sol39, " +
		"CASE WHEN ra.fecha_fin_autorizada IS NULL THEN '' ELSE to_char(ra.fecha_fin_autorizada,'dd/mm/yyyy') END AS fecha_fin_autoriza, " +
		"'' AS tipo_servicio41 " +
		"FROM ordenes_ambulatorias oa "+ 
		"INNER JOIN tipos_ordenes_ambulatorias toa ON(toa.codigo = oa.tipo_orden) "+
		"INNER JOIN estados_ord_ambulatorias eoa ON(eoa.codigo = oa.estado) "+
		"INNER JOIN det_orden_amb_articulo doaa ON(doaa.codigo_orden=oa.codigo) "+
		"LEFT OUTER JOIN det_autorizaciones da ON(da.orden_ambulatoria = oa.codigo and da.activo = '"+ConstantesBD.acronimoSi+"') "+ 
		"LEFT OUTER JOIN autorizaciones a ON(a.codigo = da.autorizacion ) "+
		"LEFT OUTER JOIN resp_autorizaciones ra ON(ra.det_autorizacion = a.codigo) " +
		"LEFT OUTER JOIN tipos_vigencia tv ON(tv.codigo = ra.tipo_vigencia) "+ 
		"LEFT OUTER JOIN usuarios u ON (u.login = oa.usuario_solicita) " ;
	
	/**
	 * Encargado de insertar en los logs en la tabla 
	 * log_actualiz_autorizaciones
	 */
	private final static String ingresarLog=" INSERT  INTO log_actualiz_autorizaciones (consecutivo,solicitud,servicio_articulo," +
																							"autorizacion_ant,autorizacion,sub_cuenta," +
																							"institucion,fecha_modifica,hora_modifica," +
																							"usuario_modifica,asocio) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	
	

	
	
	 
	/**
	 * Parte principal de la cadena consulta para el reporte
	 * Warning: Hay que cerrar el paréntesis del from 
	 */
	private final static String consultaPrincipalReporte = 
		"SELECT " +
			"t.codigo, " +
			"t.convenio, " +
			"t.paciente, " +
			"t.tiponoid, " +
			"t.ingreso, " +
			"t.viaingreso, " +
			"t.fechaingreso, " +
			"t.estadoingreso, " +
			"t.nrocuenta, " +
			"t.orden, " +
			"t.articuloservicio, " +
			"t.asocio, " +
			"t.cantidad, " +
			"t.numconv " +
		"FROM (";
	
	/**
	 * Cadena que completa el from con un paréntesis
	 */
	private final static String cerrarFrom = 
		") t ORDER BY paciente";
	
	/**
	 * Cadena que completa el from con un paréntesis
	 */
	private final static String cerrarFromReporte = 
		") t ORDER BY convenio, paciente";
	
	/**
	 * Primera parte de la Consulta para el reporte encargada de consultar los servicios 14, 6 y 9 (Solicitudes de 1 solo servicio)
	 * Warning: hay que cerrar el paréntesis al finalizar los filtros
	 */
	private final static String consultaUnSoloServicioReporte =
		"(" +
			"SELECT DISTINCT " +
				"cu.codigo_paciente AS codigo, " +
				"getnombreconvenio(detc.convenio) AS convenio, " +
				"getnombrepersona(cu.codigo_paciente) AS paciente, " +
				"getidpaciente(cu.codigo_paciente) AS tipoNoId, " +
				"getconsecutivoingreso(cu.id_ingreso) AS ingreso, " +
				"getnombreviaingreso(cu.via_ingreso) AS viaIngreso, " +
				"getfechaingreso(cu.id,cu.via_ingreso) AS fechaIngreso, " +
				"getestadoingreso(cu.id_ingreso) AS estadoIngreso, " +
				"cu.id AS nroCuenta, " +
				"getconsecutivosolicitud(detc.solicitud) AS orden, " +
				"getnombreservicio(detc.servicio, 0) AS articuloServicio, " +
				"getnombretipoasocio(detc.tipo_asocio) AS asocio, " +
				"detc.cantidad_cargada AS cantidad, " +
				"detc.convenio AS numConv " +
			"FROM cuentas cu " +
			"INNER JOIN solicitudes sol ON (sol.cuenta = cu.id ) " +
			"INNER JOIN centros_costo cc ON (cc.codigo=cu.area) " +
			"INNER JOIN ingresos i ON (i.id=cu.id_ingreso) " +
			"INNER JOIN det_cargos detc ON (detc.solicitud=sol.numero_solicitud) " +
			"INNER JOIN sub_cuentas subc ON (i.id=subc.ingreso) " +
			"INNER JOIN contratos con ON (con.codigo=subc.contrato) " +
			"WHERE " +
				"sol.tipo NOT IN (14,6,9) AND detc.cargo_padre IS NULL AND detc.cantidad_cargada > 0 ";
	
	/**
	 * Segunda parte de la Consulta para el reporte encargada de consultar los artículos (Cargos directos, artículos y medicamentos)
	 * También se consultan los materiales especiales
	 * Warning: hay que cerrar el paréntesis al finalizar los filtros
	 * Solución de la tarea 51850:
	 * para solicitudes de cirugias, para el caso de los asocios colocar el nombre del servicio de la cirugia no el nombre del servicio del
	 * asocio. (se consulto oscar cely)
	 * adicionar campo asocio
	 * mostrar los materiales especiales (mensaje azul)
	 * para el caso de cirugias y materiales especiales(ver distribucion de la cuenta) 
	 */
	private final static String consultaCargosDirectosArticulosYMedicamentosReporte = 
		"(" +
			"SELECT DISTINCT " +
				"cu.codigo_paciente AS codigo, " +
				"getnombreconvenio(detc.convenio) AS convenio, " +
				"getnombrepersona(cu.codigo_paciente) AS paciente, " +
				"getidpaciente(cu.codigo_paciente) AS tipoNoId, " +
				"getconsecutivoingreso(cu.id_ingreso) AS ingreso, " +
				"getnombreviaingreso(cu.via_ingreso) AS viaIngreso, " +
				"getfechaingreso(cu.id,cu.via_ingreso) AS fechaIngreso, " +
				"getestadoingreso(cu.id_ingreso) AS estadoIngreso, " +
				"cu.id AS nroCuenta, " +
				"getconsecutivosolicitud(detc.solicitud) AS orden, " +
				"CASE WHEN sol.tipo = 14 AND detc.articulo IS NOT NULL " +
					"THEN  ' Materiales Especiales ' || getdescarticulo(detc.articulo) " +
					"ELSE getdescarticulo(detc.articulo) END AS articuloServicio, " +
				"getnombretipoasocio(detc.tipo_asocio) AS asocio, " +
				"detc.cantidad_cargada AS cantidad, " +
				"detc.convenio AS numConv " +
			"FROM cuentas cu " +
			"INNER JOIN solicitudes sol ON (sol.cuenta = cu.id ) " +
			"INNER JOIN centros_costo cc ON (cc.codigo=cu.area) " +
			"INNER JOIN ingresos i ON (i.id=cu.id_ingreso) " +
			"INNER JOIN det_cargos detc ON (detc.solicitud=sol.numero_solicitud) " +
			"INNER JOIN sub_cuentas subc ON (i.id=subc.ingreso) " +
			"INNER JOIN contratos con ON (con.codigo=subc.contrato) " +
			"WHERE " +
				"( " +
					"sol.tipo IN (6,9) " +
					"OR (sol.tipo = 14 AND detc.articulo IS NOT NULL)" +
				") " +
				"AND detc.cargo_padre IS NULL AND detc.cantidad_cargada > 0 ";
	
	/**
	 * Tercera parte de la consulta para el reporte encargada de consultar los servicios 14 (Cirugías)
	 * Warning: hay que cerrar el paréntesis al finalizar los filtros
	 * Solución Tarea: 51850:
	 * segun mail jhony duque 25 nov 2800 14:10
	 * De acuerdo a lo hablado con margarita:
	 * El listado de las socitudes de cirugia, se debe mostrar registro por asocio, ya que los asocios de una cirugia pueden tener estado de 
	 * cargo diferentes, y manejan una autorizacion de cargo independiente, en el listado, debe mostrar el nombre de la cirugia, y el nombre del 
	 * asocio. 
	 */
	private final static String consultaCirugiasReporte =
		"( " +
			"SELECT DISTINCT " +
				"cu.codigo_paciente AS codigo, " +
				"getnombreconvenio(detc.convenio) AS convenio, " +
				"getnombrepersona(cu.codigo_paciente) AS paciente, " +
				"getidpaciente(cu.codigo_paciente) AS tipoNoId, " +
				"getconsecutivoingreso(cu.id_ingreso) AS ingreso, " +
				"getnombreviaingreso(cu.via_ingreso) AS viaIngreso, " +
				"getfechaingreso(cu.id,cu.via_ingreso) AS fechaIngreso, " +
				"getestadoingreso(cu.id_ingreso) AS estadoIngreso, " +
				"cu.id AS nroCuenta, " +
				"getconsecutivosolicitud(detc.solicitud) AS orden, " +
				"getnombreservicio(detc.servicio_cx, 0) " +
					"|| ' - ' || " +
					"CASE WHEN detc.det_cx_honorarios IS NULL THEN '' ELSE ' : ' " +
					"|| getnombrepersona(dch.medico) end AS articuloServicio, " +
				"getnombretipoasocio(detc.tipo_asocio) AS asocio, " +
				"1 AS cantidad, " +
				"detc.convenio AS numConv " +
			"FROM cuentas cu " +
			"INNER JOIN solicitudes sol ON (sol.cuenta = cu.id ) " +
			"INNER JOIN centros_costo cc ON (cc.codigo=cu.area) " +
			"INNER JOIN ingresos i ON (i.id=cu.id_ingreso) " +
			"INNER JOIN det_cargos detc ON (detc.solicitud=sol.numero_solicitud) " +
			"INNER JOIN sub_cuentas subc ON (i.id=subc.ingreso) " +
			"INNER JOIN contratos con ON (con.codigo=subc.contrato) " +
			"LEFT OUTER JOIN det_cx_honorarios dch on (dch.codigo = detc.det_cx_honorarios) " +
			"WHERE " +
			"sol.tipo = 14 AND detc.articulo IS NULL AND detc.cargo_padre IS NULL AND detc.cantidad_cargada > 0 ";
	
	/**
	 * Parte principal de la cadena consulta para el reporte
	 * Warning: Hay que cerrar el paréntesis del from 
	 */
	private final static String consultaPrincipalVista = 
		"SELECT " +
			"t.codigo, " +
			"t.paciente, " +
			"t.tipoNoId, " +
			"t.ingreso, " +
			"t.viaIngreso, " +
			"t.fechaIngreso, " +
			"t.estadoIngreso, " +
			"t.nroCuenta " +
		"FROM (";
	
	/**
	 * Primera parte de la Consulta para la vista encargada de consultar los servicios 14, 6 y 9 (Solicitudes de 1 solo servicio)
	 * Warning: hay que cerrar el paréntesis al finalizar los filtros
	 */
	private final static String consultaUnSoloServicioVista =
		"( " +
			"SELECT DISTINCT " +
				"cu.codigo_paciente AS codigo, " +
				"getnombrepersona(cu.codigo_paciente) AS paciente, " +
				"getidpaciente(cu.codigo_paciente) AS tipoNoId, " +
				"getconsecutivoingreso(cu.id_ingreso) AS ingreso, " +
				"getnombreviaingreso(cu.via_ingreso) AS viaIngreso, " +
				"getfechaingreso(cu.id,cu.via_ingreso) AS fechaIngreso, " +
				"getestadoingreso(cu.id_ingreso) AS estadoIngreso, " +
				"cu.id AS nroCuenta " +
			"FROM cuentas cu " +
			"INNER JOIN solicitudes sol ON (sol.cuenta = cu.id ) " +
			"INNER JOIN centros_costo cc ON (cc.codigo=cu.area) " +
			"INNER JOIN ingresos i ON (i.id=cu.id_ingreso) " +
			"INNER JOIN det_cargos detc ON (detc.solicitud=sol.numero_solicitud) " +
			"INNER JOIN sub_cuentas subc ON (i.id=subc.ingreso) " +
			"INNER JOIN contratos con ON (con.codigo=subc.contrato) " +
			"WHERE " +
				"sol.tipo NOT IN (14,6,9) AND detc.cargo_padre IS NULL ";
			
	/**
	 * Segunda parte de la Consulta para la vista encargada de consultar los artículos (Cargos directos, artículos y medicamentos)
	 * Warning: hay que cerrar el paréntesis al finalizar los filtros
	 */
	private final static String consultaCargosDirectosArticulosYMedicamentosVista = 
		"(" +
			"SELECT DISTINCT " +
				"cu.codigo_paciente AS codigo, " +
				"getnombrepersona(cu.codigo_paciente) AS paciente, " +
				"getidpaciente(cu.codigo_paciente) AS tipoNoId, " +
				"getconsecutivoingreso(cu.id_ingreso) AS ingreso, " +
				"getnombreviaingreso(cu.via_ingreso) AS viaIngreso, " +
				"getfechaingreso(cu.id,cu.via_ingreso) AS fechaIngreso, " +
				"getestadoingreso(cu.id_ingreso) AS estadoIngreso, " +
				"cu.id AS nroCuenta " +
			"FROM cuentas cu " +
			"INNER JOIN solicitudes sol ON (sol.cuenta = cu.id ) " +
			"INNER JOIN centros_costo cc ON (cc.codigo=cu.area) " +
			"INNER JOIN ingresos i ON (i.id=cu.id_ingreso) " +
			"INNER JOIN det_cargos detc ON (detc.solicitud=sol.numero_solicitud) " +
			"INNER JOIN sub_cuentas subc ON (i.id=subc.ingreso) " +
			"INNER JOIN contratos con ON (con.codigo=subc.contrato) " +
			"WHERE " +
				"(sol.tipo in (6,9) OR (sol.tipo = 14 and detc.articulo IS NOT NULL) ) AND detc.cargo_padre IS NULL ";
	
	/**
	 * Tercera parte de la consulta para la vista encargada de consultar los servicios 14 (Cirugías)
	 * Warning: hay que cerrar el paréntesis al finalizar los filtros
	 */
	private final static String consultaCirugiasVista =
		"(" +
			"SELECT DISTINCT " +
				"cu.codigo_paciente AS codigo, " +
				"getnombrepersona(cu.codigo_paciente) AS paciente, " +
				"getidpaciente(cu.codigo_paciente) AS tipoNoId, " +
				"getconsecutivoingreso(cu.id_ingreso) AS ingreso, " +
				"getnombreviaingreso(cu.via_ingreso) AS viaIngreso, " +
				"getfechaingreso(cu.id,cu.via_ingreso) AS fechaIngreso, " +
				"getestadoingreso(cu.id_ingreso) AS estadoIngreso, " +
				"cu.id AS nroCuenta " +
			"FROM cuentas cu " +
			"INNER JOIN solicitudes sol ON (sol.cuenta = cu.id ) " +
			"INNER JOIN centros_costo cc ON (cc.codigo=cu.area) " +
			"INNER JOIN ingresos i ON (i.id=cu.id_ingreso) " +
			"INNER JOIN det_cargos detc ON (detc.solicitud=sol.numero_solicitud) " +
			"INNER JOIN sub_cuentas subc ON (i.id=subc.ingreso) " +
			"INNER JOIN contratos con ON (con.codigo=subc.contrato) " +
			"WHERE " +
				"sol.tipo = 14 AND detc.articulo IS NULL AND detc.cargo_padre IS NULL ";
	
	/**
	 * Encargado de actualizar el numero de autorizacion de la admision en la tabla sub_cuentas
	 */
	private final static String actualizarAutorizacionAdmision =" UPDATE sub_cuentas SET nro_autorizacion=? WHERE sub_cuenta=?";
	
	/**
	 * Encargado de actualizar el numero de autorizacion de un servicio o articulo de la tabla det_cargos
	 */
	private final static String actualizarAutorizacionServicioArticulo = 
		" UPDATE det_cargos SET nro_autorizacion=? WHERE codigo_detalle_cargo=? ";
	
	
	
	/*********************************************************************************************************************************************
	 * 
	 ********************************************************************************************************************************************/

	/**
	 * Método para cargar ek detalle de una autorizacion
	 */
	private final static String cargarDetAutorizacionesStr = "SELECT "+ 
		"da.codigo as codigo, "+
		"da.autorizacion as autorizacion, "+
		"coalesce(da.numero_solicitud,"+ConstantesBD.codigoNuncaValido+") as numero_solicitud, "+
		"coalesce(da.orden_ambulatoria,"+ConstantesBD.codigoNuncaValido+") as orden_ambulatoria, "+
		"coalesce(da.det_cargo,"+ConstantesBD.codigoNuncaValido+") as det_cargo, "+
		"coalesce(da.servicio,"+ConstantesBD.codigoNuncaValido+") as codigo_servicio, "+
		"coalesce(getnombreservicio(da.servicio,?),'') as nombre_servicio, "+
		"coalesce(da.articulo,"+ConstantesBD.codigoNuncaValido+") as codigo_articulo, "+
		"coalesce(getdescripcionarticulo(da.articulo),'') as nombre_articulo, "+
		"coalesce(da.cantidad,0) as cantidad, "+
		"coalesce(da.justificacion_solicitud,'') as justificacion_solicitud, "+
		"to_char(da.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') as fecha_modifica, "+
		"da.hora_modifica as hora_modifica, "+
		"da.usuario_modifica as usuario_modifica "+ 
		"FROM det_autorizaciones da "+ 
		"WHERE da.codigo = ?";
	
	/**
	 * Método para cargar la autorizacion
	 */
	private final static String cargarAutorizacionStr = "SELECT "+ 
		"a.codigo as codigo, "+
		"a.consecutivo as consecutivo, "+
		"a.anio_consecutivo as anio_consecutivo, "+
		"a.ingreso as id_ingreso, "+
		"a.sub_cuenta as id_sub_cuenta, "+
		"a.tipo_servicio_solicitado as cod_tipo_ser_sol, "+
		"coalesce(tss.nombre,'') as nom_tipo_ser_sol, "+
		"coalesce(a.observaciones,'') as observaciones, "+
		"coalesce(a.estado,'') as estado, "+
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
		"a.activo as activo, "+
		"coalesce(a.prioridad_atencion,'') as prioridad_atencion, "+
		"a.cuenta as id_cuenta, "+
		"coalesce(a.cama,"+ConstantesBD.codigoNuncaValido+") as codigo_cama, "+
		"coalesce(getdescripcioncamaphc(a.cama),'') as nombre_cama, "+
		"to_char(a.fecha_modifica,'') as fecha_modifica, "+
		"a.hora_modifica, "+
		"a.usuario_modifica "+ 
		"FROM autorizaciones a "+ 
		"LEFT OUTER JOIN tipos_ser_sol tss ON(tss.codigo = a.tipo_servicio_solicitado) "+ 
		"LEFT OUTER JOIN coberturas_salud cs ON(cs.codigo = a.tipo_cobertura) "+ 
		"LEFT OUTER JOIN causas_externas ce ON(ce.codigo = a.origen_atencion) "+ 
		"WHERE "+ 
		"a.codigo = ?";
	
	/**
	 * Cadena que carga los envios de autorizacion de una autorizacion
	 */
	private final static String cargarEnvioAutorizacionStr = "SELECT "+ 
		"ea.codigo_pk as codigo_pk, "+
		"ea.autorizacion as autorizacion, "+
		"coalesce(ea.entidad_envio,"+ConstantesBD.codigoNuncaValido+") as cod_entidad_envio, "+
		"coalesce(e.razon_social,'') as nom_entidad_envio, "+
		"coalesce(ea.convenio_envio,"+ConstantesBD.codigoNuncaValido+") as cod_convenio_envio, "+
		"coalesce(getnombreconvenio(ea.convenio_envio),'') as nom_convenio_envio, "+
		"ea.medio_envio as medio_envio, "+
		"to_char(ea.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') as fecha_modifica, "+
		"ea.hora_modifica as hora_modifica, "+
		"ea.usuario_modifica as usuario_modifica "+ 
		"FROM envio_autorizaciones ea "+ 
		"LEFT OUTER JOIN empresas e ON(e.codigo = ea.entidad_envio) "+ 
		"WHERE "+ 
		"ea.autorizacion = ? "+ 
		"ORDER BY ea.codigo_pk desc";
	
	/**
	 * Cadena que carga los adjuntos de la autorizacion
	 */
	private final static String cargarAdjuntosAutorizacionStr = "SELECT "+ 
		"aa.codigo_pk as codigo_pk, "+
		"aa.autorizacion as autorizacion, "+
		"aa.nombre_archivo as nombre_archivo, "+
		"aa.nombre_original as nombre_original, "+
		"to_char(aa.fecha_modifica,'"+ConstantesBD.formatoFechaAp+"') as fecha_modifica, "+
		"aa.hora_modifica as hora_modifica, "+
		"aa.usuario_modifica as usuario_modifica "+ 
		"FROM adj_autorizaciones aa "+  
		"WHERE "+ 
		"aa.autorizacion = ? ";
	
	/**
	 * Cadena para cargar los diagnósticos de la autorización
	 */
	private final static String cargarDiagnosticosAutorizacionStr = "SELECT "+ 
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
	
																			
	/***********************************************************************************************************************************
	 * NUEVO DESARROLLO
	 ***********************************************************************************************************************************/																		
	
	/**
	 * Metodo encargado de ingresar el log cuando se realiza una modificacion
	 * de la autorizacion 
	 * @param connection
	 * @param datos
	 * ----------------------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------------------
	 *  Solicitud --> Requerido
	 *  servicioArticulo --> Requerido
	 *  autorizacionAnt --> Requerido
	 *  autorizacion --> Requerido
	 *  subCuenta --> 
	 *  institucion --> Requerido
	 *  usuarioModifica --> Requerido
	 *  asocio --> Opcional
	 *  @return boolean
	 */
	public static boolean ingresarLog(Connection connection,HashMap datos)
	{

		logger.info("\n entro a ingresarLog datos -->"+datos);
		
		String cadena = ingresarLog;
		
		logger.info("\n cadena -->"+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		
			
			//consecutivo

			ps.setInt(1, UtilidadBD.obtenerSiguienteValorSecuencia(connection, "manejopaciente.seq_log_actualiz_autorizac"));

			//solicitud
			if (Utilidades.convertirAEntero(datos.get("Solicitud")+"")>=0)
				ps.setInt(2, Utilidades.convertirAEntero(datos.get("Solicitud")+""));
			else
				ps.setNull(2, Types.INTEGER);
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
			
			if(ps.executeUpdate()>0)
				return true;
			else
				return false;
		} 
		catch (SQLException e) 
		{
			logger.info("\n problema insertando el log en la tabla  log_actualiz_autorizaciones "+e);
		}
		
		return false;
	}
	
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
	public static boolean actualizarAutorizacionServicioArticulo (Connection connection, HashMap datos)
	{
		logger.info("\n entro a actualizarAutorizacionServicioArticulo datos -->"+datos);
		
		String cadena = actualizarAutorizacionServicioArticulo;
		boolean inserto=false;
		logger.info("\n cadena -->"+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 *  UPDATE det_cargos SET nro_autorizacion=? WHERE codigo_detalle_cargo=? 
			 */
			
			//numero autorizacion
			ps.setString(1, datos.get("numeroAutorizacion")+"");
			//codigo_detalle_cargo
			ps.setDouble(2, Utilidades.convertirADouble(datos.get("codigoDetalleCargo")+""));
			
			if(ps.executeUpdate()>0)
					inserto= true;
			
			if (inserto)
				inserto=ingresarLog(connection, datos);
		} 
		catch (SQLException e) 
		{
			logger.info("\n problema actualizando el numero de autorizacion de un servicio o articulo en la tabla det_cargos "+e);
		}
		
		return inserto;
	}
	
	
	/**
	 * Metodo encargado de actualizar el numero de autorizacion 
	 * de la admision
	 * @author Jhony Alexander Duque.
	 * @param connection
	 * @param datos
	 * --------------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------------
	 * -- numeroAutorizacion --> Requerido
	 * -- subCuenta --> Requerido
	 * @return true/false
	 */
	public static boolean actualizarAutorizacionAdmision (Connection connection, HashMap datos)
	{
		logger.info("\n entro a actualizarAutorizacionAdmision datos -->"+datos);
		
		String cadena = actualizarAutorizacionAdmision;
		boolean inserto=false;
		logger.info("\n cadena -->"+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			/**
			 *  UPDATE sub_cuentas SET nro_autorizacion=? WHERE sub_cuenta=?
			 */
			
			//numero autorizacion
			ps.setString(1, datos.get("numeroAutorizacion")+"");
			//sub_cuenta
			ps.setLong(2, Utilidades.convertirALong(datos.get("subCuenta")+""));
			
			if(ps.executeUpdate()>0)
				inserto= true;
			
			if (inserto)
				inserto=ingresarLog(connection, datos);
			
		} 
		catch (SQLException e) 
		{
			logger.info("\n problema actualizando el numero de autorizacion de la admision en sub_cuentas "+e);
		}
		
		return inserto;
	}
	
	
	
	/**
	 * Metodo encargado de consultar los datos de servicios
	 * o articulos con el numero de autorizacion.
	 * @author Jhony Alexander Duque A.
	 * @param con 
	 * @param criterios
	 * ---------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------------
	 * -- subCuenta --> Requerido
	 * -- solicitud --> Opcional
	 * -- articulo --> Opcional
	 * -- servicio --> Opcional
	 * -- fechaSolicitud --> Opcional
	 * -- centroCostoSolicita --> Opcional
	 * -- centroCostoEjecuta --> Opcional
	 * --numeroAutorizacion --> Opcional
	 * @return Mapa
	 * --------------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * --------------------------------------
	 * solicitud0_,consecutivoOrdenes1_,tipoSolicitud2_,
	 * nombreTipoSolicitud3_,estadoHistoriaClinica4_,
	 * nombreEstadoHistoriaClinica5_,centroCostoSolicita6_,
	 * nombreCentroCostoSolicita7_,centroCostoEjecuta8_,
	 * nombreCentroCostoEjecuta9_,porcentajeAutorizado10_,
	 * montoAutorizado11_,valorUnitario12_,valorTotal13_,
	 * estadoCargo14_,nombreEstadoCargo15_,fechaSolicitud16_,
	 * servArticulo17_,cantidad18_,requiereAutorizacion19_,
	 * numeroAutorizacion20_
	 */
	public static HashMap cargarServiciosArticulos (Connection con, HashMap criterios)
	{
		boolean ordenesAmbulatorias = UtilidadTexto.getBoolean(criterios.get("ordenesAmbulatorias").toString());
		HashMap resultados = new HashMap();
		
		if(ordenesAmbulatorias)
		{
			resultados = cargarServiciosArticulosAmbulatorioa(con,criterios);
		}
		else
		{
			resultados = cargarServiciosArticulosSolicitudes(con, criterios);
		}
		
		
		resultados = verificarVigencia(con,ordenesAmbulatorias,resultados);
		
				
		return resultados;
	}
	
	/**
	 * Método que verifica la vigencia de cada registro de solicitud autorizacion
	 * @param con
	 * @param ordenesAmbulatorias
	 * @param resultados
	 * @return
	 */
	private static HashMap verificarVigencia(Connection con,boolean ordenesAmbulatorias, HashMap resultados) 
	{
		///**************VALIDACION DE LA VIGENCIA*************************************
		for(int i=0;i<Integer.parseInt(resultados.get("numRegistros").toString());i++)
		{
			String esVigente = ConstantesBD.acronimoNo;
			String fechaVigencia = ConstantesBD.acronimoSi;
			int vigencia = Integer.parseInt(resultados.get("vigencia_"+i).toString());
			int estadoHistoriaClinica = Integer.parseInt(resultados.get(ListadoIngresos.indicesServiciosArticulos[4]+i).toString());
			int tipoSolicitud = Integer.parseInt(resultados.get(ListadoIngresos.indicesServiciosArticulos[2]+i).toString());
			String tipoVigencia = resultados.get("tipoVigencia_"+i).toString();
			String fechaAutorizacion = resultados.get("fechaAutorizacion36_"+i).toString();
			String horaAutorizacion = resultados.get("horaAutorizacion37_"+i).toString();
			//Si noa hay fecha/hora autorizacion quiere decir que la autorización no ha sido respondida
			if(fechaAutorizacion!=null&&!fechaAutorizacion.equals("")&&horaAutorizacion!=null&&!horaAutorizacion.equals(""))
			{
				if(vigencia>0)
				{
					String[] vectorFecha = {"","00:00"};
					if(tipoVigencia.equals(ConstantesIntegridadDominio.acronimoHoras2))
					{
						 vectorFecha = UtilidadFecha.incrementarMinutosAFechaHora(fechaAutorizacion, horaAutorizacion, vigencia*60, false);
					}
					else
					{
						vectorFecha[0] = UtilidadFecha.incrementarDiasAFecha(fechaAutorizacion, vigencia, false);
					}
					
					fechaVigencia = vectorFecha[0] + " " + vectorFecha[1];
					if(UtilidadFecha.compararFechas(vectorFecha[0], vectorFecha[1], UtilidadFecha.getFechaActual(con), UtilidadFecha.getHoraActual(con)).isTrue())
						esVigente = ConstantesBD.acronimoSi;
					else
						esVigente = ConstantesBD.acronimoNo;
					
					if(ordenesAmbulatorias)
					{
						
					}
					else
					{
						//Si la solicitud es un servicio y ya estaba respondida se acaba su vigencia
						if((estadoHistoriaClinica == ConstantesBD.codigoEstadoHCRespondida||
							estadoHistoriaClinica == ConstantesBD.codigoEstadoHCInterpretada)
							&& tipoSolicitud != ConstantesBD.codigoTipoSolicitudMedicamentos && tipoSolicitud != ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos)
						{
							esVigente = ConstantesBD.acronimoNo;
						}
						//Si la solicitud de medicamentos ya está administrada entonces ya no hay vigencia 
						else if(estadoHistoriaClinica == ConstantesBD.codigoEstadoHCAdministrada
								&& tipoSolicitud == ConstantesBD.codigoTipoSolicitudMedicamentos )
						{
							esVigente = ConstantesBD.acronimoNo;
						}
					}
				}
				else
					esVigente = ConstantesBD.acronimoSi;
			}
			
			resultados.put(ListadoIngresos.indicesServiciosArticulos[32]+i, esVigente);
			resultados.put(ListadoIngresos.indicesServiciosArticulos[33]+i, fechaVigencia);
		}
		//*****************************************************************************
		return resultados;
	}

	/**
	 * Método implementado para cargar los servicios / artículos de las ordenes ambulatorias
	 * de un ingreso
	 * @param con
	 * @param criterios
	 * @return
	 */
	private static HashMap cargarServiciosArticulosAmbulatorioa(Connection con,HashMap criterios) 
	{
		HashMap resultados = new HashMap();
		
		//*****************SE TOMAN LOS PARÁMETROS**************************************+
		String idIngreso = criterios.get("idIngreso").toString();
		//******************************************************************************
		
		String consulta = "SELECT * FROM (" + consultaServiciosAmbulatorios ;
		String where = "WHERE oa.ingreso = "+idIngreso+" and oa.estado in ("+ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente+","+ConstantesBD.codigoEstadoOrdenAmbulatoriaRespondida+") ";
		
		/*
		//filtro servicio
		if (UtilidadCadena.noEsVacio(criterios.get("servicio")+""))
			where+=" AND doas.servicio="+criterios.get("servicio");
		//filtro fecha de la solicitud
		if (UtilidadCadena.noEsVacio(criterios.get("fechaSolicitud")+""))
			where+=" AND oa.fecha='"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaSolicitud")+"")+"'";
		//filtro centro costo solicita
		if (UtilidadCadena.noEsVacio(criterios.get("centroCostoSolicita")+""))
			where+=" AND oa.centro_costo_solicita="+criterios.get("centroCostoSolicita");*/
		
		///Filtro estado autorizacion
		if (UtilidadCadena.noEsVacio(criterios.get("estadoAutorizacion")+""))
		{
			where += " AND a.estado = '"+criterios.get("estadoAutorizacion")+"' ";
		}
		
		consulta += where + " UNION " + consultaArticulosAmbulatorios;
		
		where = "WHERE oa.ingreso = "+idIngreso+" and oa.estado in ("+ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente+","+ConstantesBD.codigoEstadoOrdenAmbulatoriaRespondida+") ";
		
		/*
		//filtro articulo		
		if (UtilidadCadena.noEsVacio(criterios.get("articulo")+""))
			where+=" AND doaa.articulo="+criterios.get("articulo");
		//filtro fecha de la solicitud
		if (UtilidadCadena.noEsVacio(criterios.get("fechaSolicitud")+""))
			where+=" AND oa.fecha='"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaSolicitud")+"")+"'";
		//filtro centro costo solicita
		if (UtilidadCadena.noEsVacio(criterios.get("centroCostoSolicita")+""))
			where+=" AND oa.centro_costo_solicita="+criterios.get("centroCostoSolicita");*/
		
		///Filtro estado autorizacion
		if (UtilidadCadena.noEsVacio(criterios.get("estadoAutorizacion")+""))
		{
			where += " AND a.estado = '"+criterios.get("estadoAutorizacion")+"' ";
		}
		
		consulta += where+" ) t ";
		
		String order=" ORDER BY consecutivo_ordenes1";
		consulta += order;
		
		try
		{
			logger.info("valor de la consulta "+consulta);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
		}
		catch (SQLException e)
		{
			logger.error(" Error en consulta de servicios articulos det_cargos "+e);
		}
		
		
		return resultados;
	}

	/**
	 * Método implementado para cargar los servicios artículos de las solicitudes de un responsable
	 * del ingreso seleccionado
	 * @param con
	 * @param criterios
	 * @return
	 */
	private static HashMap cargarServiciosArticulosSolicitudes(Connection con,HashMap criterios)
	{
		HashMap resultados = new HashMap();
		resultados.put("numRegistros","0");
		logger.info("\n entre a cargarServiciosArticulos criterios-->"+criterios);
		String cadena = consultaServiciosArticulos;
		
		String where=" WHERE dc.sub_cuenta=? and dc.cargo_padre is null " +
								" AND s.estado_historia_clinica not in ("+ConstantesBD.codigoEstadoHCAnulada+")" ;
								
		
		//filtro numero solicitud
		if (UtilidadCadena.noEsVacio(criterios.get("solicitud")+""))
			where+=" AND s.consecutivo_ordenes_medicas='"+criterios.get("solicitud")+"'";
		//filtro articulo		
		if (UtilidadCadena.noEsVacio(criterios.get("articulo")+""))
			where+=" AND dc.articulo="+criterios.get("articulo");
		//filtro servicio
		if (UtilidadCadena.noEsVacio(criterios.get("servicio")+""))
			where+=" AND dc.servicio="+criterios.get("servicio");
		//filtro fecha de la solicitud
		if (UtilidadCadena.noEsVacio(criterios.get("fechaSolicitud")+""))
			where+=" AND s.fecha_solicitud='"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaSolicitud")+"")+"'";
		//filtro centro costo solicita
		if (UtilidadCadena.noEsVacio(criterios.get("centroCostoSolicita")+""))
			where+=" AND s.centro_costo_solicitante="+criterios.get("centroCostoSolicita");
		//filtro centro costo ejecuta
		if (UtilidadCadena.noEsVacio(criterios.get("centroCostoEjecuta")+""))
			where+=" AND s.centro_costo_solicitado="+criterios.get("centroCostoEjecuta");
		
		where +=" AND (SELECT COUNT(dae.codigo) FROM det_autorizaciones_estancia dae WHERE dae.numero_solicitud = s.numero_solicitud AND s.tipo = "+ConstantesBD.codigoTipoSolicitudEstancia+" AND dae.activo = '"+ConstantesBD.acronimoSi+"') = 0 " ;
		
		//Filtro estado autorizacion
		if (UtilidadCadena.noEsVacio(criterios.get("estadoAutorizacion")+""))
		{
			where += " AND da.estado = '"+criterios.get("estadoAutorizacion")+"' ";
		}
		
		
		String group=" GROUP BY dc.solicitud,s.consecutivo_ordenes_medicas,s.tipo, s.estado_historia_clinica," +
								" s.centro_costo_solicitante, s.centro_costo_solicitado,sc.porcentaje_autorizado," +
								" sc.monto_autorizado, dc.valor_unitario_cargado, dc.valor_total_cargado, " +
								" dc.estado, s.fecha_solicitud,s.hora_solicitud ,dc.servicio, dc.articulo, " +
								" dc.requiere_autorizacion,dc.nro_autorizacion,s.numero_autorizacion,dc.codigo_detalle_cargo," +
								" dc.tipo_asocio,"+
						        " dc.servicio_cx,"+
						        " s.tipo,"+
						        " dc.articulo,"+
								" a.codigo, da.codigo, da.estado,a.tipo_tramite,s.urgente, ra.vigencia, " +
								" ra.fecha_autorizacion,ra.hora_autorizacion,tv.tipo,sc.sub_cuenta,dc.convenio,s.codigo_medico,ra.fecha_fin_autorizada  ";
		
		
		String order=" ORDER BY s.consecutivo_ordenes_medicas,servicio_cx25,serv_articulo17,nombre_tipo_asocio24 DESC";
		
		
		cadena+=where+group+order;
		
		logger.info("\n cadena -->"+cadena);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//filtro sub_cuenta
			ps.setLong(1, Utilidades.convertirALong(criterios.get("subCuenta")+""));
			
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
		}
		catch (SQLException e)
		{
			logger.error(" Error en consulta de servicios articulos det_cargos "+e);
		}
		return resultados;
	}
	
	
	
	
	
	
	
	
	/**
	 * Metodo encargado de consultar el listado de ingresos (cerrados, abiertos)
	 * y cuentas (no importa el estado) de un paciente. 
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param criterios
	 * ---------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------
	 * -- paciente --> Requerido
	 * -- institucion --> Requerido
	 * @return Mapa
	 * -----------------------------
	 * KEY'S DEL MAPA QUE RETORNA
	 * -----------------------------
	 * idIngreso0_,consecutivoIngreso1_,centroAtencion2_,
	 * nombreCentroAtencion3_,estadoIngreso4_,
	 * fechaAperturaIngreso5_,viaIngreso6_,nombreViaIngreso7_,
	 * fechaCierreIngreso8_,numeroCuenta9_,estadoCuenta10_,
	 * nombreEstadoCuenta11_
	 */
	public static HashMap cargarListadoIngresos (Connection con, HashMap criterios)
	{
		logger.info("\n entre a cargarListadoIngresos criterios-->"+criterios);
		String cadena = consultarListadoIngresos;
		
			
		logger.info("\n cadena -->"+cadena);
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//codigo del paciente
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get("paciente")+""));
			// codigo institucion 
			ps.setInt(2, Utilidades.convertirAEntero(criterios.get("institucion")+""));
			// Cargar los resultados de la consulta en un hashMap
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			ps.close();
			return mapaRetorno;
			
		}
		catch (SQLException e)
		{
			logger.error(" Error en consulta de ingresos del paciente "+e);
		}
				
		return null;
	}
	
	
	/**
	 * Metodo encargado de consultar los convenios de un ingreso en 
	 * la tabla sub_cuentas
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * --ingreso --> Requerido
	 * @return ArrayList<HashMap>
	 * ----------------------------------------------
	 * KEY'S DE LOS MAPAS DEL ARRAYLIST DE RESULTADO
	 * ----------------------------------------------
	 * -- codigo
	 * -- nombre
	 * -- contrato
	 * -- autorizacion
	 */
	public static ArrayList<HashMap<String, Object>> ObtenerConvenios (Connection connection,HashMap criterios)
	{
		logger.info("\n entre a ObtenerConvenios -->"+criterios);
		
		ArrayList convenios= new ArrayList ();
		String cadena=consultarConveniosXIngreso;
		
		logger.info("\n cadena -->"+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//institucion
			ps.setInt(1, Utilidades.convertirAEntero(criterios.get("ingreso")+""));
		
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			while (rs.next())
			{
				HashMap mapa = new HashMap ();
				mapa.put("codigo", rs.getObject("codigo"));
				mapa.put("nombre", rs.getObject("nombre"));
				mapa.put("contrato", rs.getObject("contrato"));
				mapa.put("autorizacion", rs.getObject("autorizacion"));
				mapa.put("subCuenta", rs.getObject("subcuenta"));
				
				convenios.add(mapa);
			}
			
		}
		catch (SQLException e) 
		{
			logger.warn("\n problema consultando los convenios de la tabla sub_cuentas "+e);
		}
		
		return convenios;
	}
	
	/********************************************************************************************************************************************
	 * 
	 ********************************************************************************************************************************************/
	
	/**
	 * Metodo que consulta las Tipos de Transacciones
	 * @param con
	 * @return mapa
	 */
	public static HashMap consultarViaIngreso(Connection con, int codInstitucion) 
	{
		logger.info("===> Entré a consultarViaIngreso");
		PreparedStatementDecorator pst=null;
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String cadena = "SELECT " +
					"codigo AS codigo, " +
					"nombre AS nombre " +
					"FROM vias_ingreso "+
					"ORDER BY nombre "; 
			logger.info("===> La instituciín es: "+codInstitucion);
			logger.info("===>Consulta Vias de Ingreso: "+cadena);
			pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarViaIngreso: "+e);
		}
		return mapa;
	}
	
	/**
	 * Método encargado de consultar todos los convenios existentes en el sistema
	 * @param con
	 * @return Mapa
	 */
	public static HashMap obtenerTodosLosConvenios(Connection con) 
	{
		logger.info("===> Entré a consultarTodosLosConvenios");
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String cadena = "SELECT " +
					"codigo AS codigo, " +
					"nombre AS nombre " +
					"FROM convenios " +
					"ORDER BY nombre "; 
			logger.info("===>Consulta Todos Los Convenios: "+cadena);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarTodosLosConvenios: "+e);
		}
		return mapa;
	}
	
	/**
	 * Método encargado de consultar todos estados de solicitud
	 * @param con
	 * @param codInstitucion
	 * @return Mapa
	 */
	public static HashMap obtenerEstadoSolicitud(Connection con) 
	{
		logger.info("===> Entré a obtenerEstadoSolicitud");
		PreparedStatementDecorator pst=null;
		
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String cadena = "SELECT " +
					"codigo AS codigo, " +
					"nombre AS nombre " +
					"FROM estados_sol_his_cli "+
					"ORDER BY nombre "; 
			logger.info("===>Consulta Todos Los Estados De Solicitud: "+cadena);
			pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarTodosLosConvenios: "+e);
		}
		return mapa;
	}
	
	/**
	 * Método encargado de consultar todos los pisos basados en una institución
	 * @param con
	 * @param codInstitucion
	 * @return Mapa
	 */
	public static HashMap obtenerPisosXInstitucion(Connection con, int codInstitucion, String centroDeAtencion) 
	{
		logger.info("===> Entré a obtenerPisosXInstitucion");
		logger.info("===> codigoInstitucion: "+codInstitucion);
		logger.info("===> centroAtencion: "+centroDeAtencion);
		
		PreparedStatementDecorator pst=null;
		
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String cadena = "SELECT " +
					"codigo AS codigo, " +
					"nombre AS nombre " +
					"FROM pisos " +
					"WHERE " +
					"institucion ="+codInstitucion+" " +
					"AND centro_atencion="+Integer.parseInt(centroDeAtencion)+" "+
					"ORDER BY nombre "; 
			logger.info("===>Consulta Todos Los Convenios: "+cadena);
			pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarTodosLosConvenios: "+e);
		}
		return mapa;
	}
	
	/**
	 * Método en cargado de realizar la consulta para la Actualizacion Autorizacion Medicas X Rangos
	 * @param connection
	 * @param criterios
	 * @return cargarValueObject / NULL
	 */
	
	public static HashMap consultaXRangos (Connection connection, HashMap criterios)
	{
		logger.info("===> Entre a consultaXRangos, Los Criterios Son: "+criterios);
		String
			cadena = "",
			cadenaPrincipal = consultaPrincipalReporte,
			cadenaTodosServicios = consultaUnSoloServicioReporte,
			cadenaArticulos = consultaCargosDirectosArticulosYMedicamentosReporte,
			cadenaCirugia = consultaCirugiasReporte,
			cerrarElFrom = cerrarFromReporte;
		cadenaTodosServicios+=obtenerWhere(criterios);
		cadenaArticulos+=obtenerWhere(criterios);
		cadenaCirugia+=obtenerWhere(criterios);
		
		/*
		 * Unión de todas las cadenas
		 */
		cadena = cadenaPrincipal + cadenaTodosServicios + "UNION" + cadenaArticulos + "UNION" + cadenaCirugia + cerrarElFrom;
		logger.info("===>  Consulta La Cadena X Rangos: "+cadena);
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			ps.close();
			return mapaRetorno;
					
		}
		catch (SQLException e)
		{
			logger.error("===> Problema consultando datos Actualización Autorizacion Medicas X Rangos"+e);
		}
		return null;
	}
	
	/**
	 * Método en cargado de realizar la consulta para la Actualizacion Autorizacion Medicas X Rangos
	 * @param connection
	 * @param criterios
	 * @return cargarValueObject / NULL
	 */
	
	public static HashMap consultaXRangosParaVista (Connection connection, HashMap criterios)
	{
		logger.info("===> Entre a consultaXRangosParaVista, Los criterios son: "+criterios);
		String
			cadena = "",
			cadenaPrincipal = consultaPrincipalVista,
			cadenaTodosServicios = consultaUnSoloServicioVista,
			cadenaArticulos = consultaCargosDirectosArticulosYMedicamentosVista,
			cadenaCirugia = consultaCirugiasVista,
			cerrarElFrom = cerrarFrom;
		cadenaTodosServicios+=obtenerWhere(criterios);
		cadenaArticulos+=obtenerWhere(criterios);
		cadenaCirugia+=obtenerWhere(criterios);
		
		/*
		 * Unión de todas las cadenas
		 */
		cadena = cadenaPrincipal + cadenaTodosServicios + "UNION" + cadenaArticulos + "UNION" + cadenaCirugia + cerrarElFrom;
		logger.info("===> Cadena X Rangos Para Vista: "+cadena);
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			ps.close();
			return mapaRetorno;
					
		}
		catch (SQLException e)
		{
			logger.error("===> Problema consultando datos Actualización Autorizacion Medicas X Rangos en la vista "+e);
		}
		return null;
	}
	
	
	/**
	 * Método encargado de generar una cadena where
	 * @param HashMap criterios
	 * @return where
	 */
	public static String obtenerWhere (HashMap criterios)
	{
		Vector<String> estadosCuenta= new Vector<String>();
		
		/*
		 * Los campos requeridos son:
		 * 1. Centro de Atención
		 * 2. Indicativo Orden
		 */
				
		String where="";
		
		/*
		 * Validación del convenio
		 */
		if(!criterios.get("convenio").equals((ConstantesBD.codigoNuncaValido)+""))
		{
			where+= "AND cc.centro_atencion="+criterios.get("centroAtencion"+"")+" "+
			"AND detc.convenio="+criterios.get("convenio");
		}
		else
		{
			where+= "AND cc.centro_atencion="+criterios.get("centroAtencion"+"");
		}
		
		/*
		 * Validación de las fechas de ingreso
		 */
		if(!(criterios.get("fechaIngresoInicial")+"").equals("") &&
			!(criterios.get("fechaIngresoFinal")+"").equals(""))
		{
			where+=" AND i.fecha_ingreso BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaIngresoInicial")+"")+"' AND " +
	    	"'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaIngresoFinal")+"")+"'";
		}

		/*
		 * Validación de las fechas de solicitud
		 */
		if(!(criterios.get("fechaSolicitudInicial")+"").equals("") &&
			!(criterios.get("fechaSolicitudFinal")+"").equals(""))
		{
			where+=" AND sol.fecha_solicitud BETWEEN '"+
			UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaSolicitudInicial")+"")+"' AND " +
	    	"'"+UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaSolicitudFinal")+"")+"'";
		}
		
		/*
		 * Validación del estado de Solicitud
		 */
		if(!(criterios.get("estadoSolicitud").equals((ConstantesBD.codigoNuncaValido)+"")))
		{
			where+=" AND sol.estado_historia_clinica='"+(criterios.get("estadoSolicitud")+"")+"'";
		}
		
		/*
		 * Validación del estado de Ingreso
		 */
		if(!(criterios.get("estadoIngreso").equals((ConstantesBD.codigoNuncaValido)+"")))
		{
			where+=" AND i.estado='"+(criterios.get("estadoIngreso")+"")+"'";
		}
		
		/*
		 * Validación de las vías de ingreso
		 */
		if(criterios.get("viaIngreso")!=null)
		{
			if(!criterios.get("viaIngreso").equals(-1))
			{
				where+=" AND cu.via_ingreso IN("+criterios.get("viaIngreso")+") ";
			}
		}
		/*
		 * Validación de:
		 * Estado de la cuenta Activa
		 * Estado de la cuenta Asociada
		 * Estado de la cuenta Facturada Parcial
		 * Estado de la cuenta Facturada
		 * En la tabla de estados_cuenta, existen diferentes tipos de codigo
		 * Allí se muestran los nombres 
		 */
		if((criterios.get("estadoCuentaActiva")+"").equals("S"))
			estadosCuenta.add("0");
		
		if((criterios.get("estadoCuentaAsociada")+"").equals("S"))
			estadosCuenta.add("3");

		if((criterios.get("estadoCuentaFacturadaParcial")+"").equals("S"))
			estadosCuenta.add("6");
		

		if((criterios.get("estadoCuentaFacturada")+"").equals("S"))
			estadosCuenta.add("1");

		if(estadosCuenta.size()!= 0)
		{
			where+=" AND cu.estado_cuenta IN("+UtilidadTexto.convertirVectorACodigosSeparadosXComas(estadosCuenta, false)+") ";
		}
		
		/*
		 * Validacion del piso
		 */
		if(!criterios.get("piso").equals((ConstantesBD.codigoNuncaValido)+""))
		{
			where+=" AND getcodigopisocuenta(cu.id, cu.via_ingreso)="+criterios.get("piso");
		}
		
		/*
		 * Validación de indicativo orden
		 */
		if(criterios.get("indicativoOrden").equals("PXA"))
		{
			where+=" AND ((detc.cubierto='S' " +
					"AND detc.requiere_autorizacion='S' " +
					"AND detc.nro_autorizacion IS NULL) " +
					"OR " +
					"(con.req_auto_no_cobertura='S' " +
					"AND detc.cubierto='N' " +
					"AND detc.nro_autorizacion IS NULL))";
		}
		where+=")";
		return where;
	}
	
	/**
	 * Método encargado de obtener la consulta para generar el reporte
	 * @param connection
	 * @param criterios
	 * @return cadena
	 */
	
	public static String obtenerConsulta (Connection connection, HashMap criterios)
	{
		/*
		logger.info("===> Entre a obtenerConsulta Los criterios son: "+criterios);
		
		String cadena = consultaXRangos;
		cadena+=obtenerWhere(criterios)+" ";
		cadena+=" ORDER BY paciente ";
		*/
		
		logger.info("===> Entre a obtenerConsulta, Los criterios son: "+criterios);
		//String cadena = consultaXRangos;
		String
			cadena = "",
			cadenaPrincipal = consultaPrincipalReporte,
			cadenaTodosServicios = consultaUnSoloServicioReporte,
			cadenaArticulos = consultaCargosDirectosArticulosYMedicamentosReporte,
			cadenaCirugia = consultaCirugiasReporte,
			cerrarElFrom = cerrarFrom;
		cadenaTodosServicios+=obtenerWhere(criterios);
		cadenaArticulos+=obtenerWhere(criterios);
		cadenaCirugia+=obtenerWhere(criterios);
		
		//cadena+=obtenerWhere(criterios)+" ";
		//cadena+=" ORDER BY paciente ";
		/*
		 * Unión de todas las cadenas
		 */
		cadena = cadenaPrincipal + cadenaTodosServicios + "UNION" + cadenaArticulos + "UNION" + cadenaCirugia + cerrarElFrom;
		
		logger.info("===>  Cadena X Rangos: "+cadena);
		
		return cadena;
	}
	
	/**
	 * Método encargado de obtener los centros de atención
	 * @param Coneection con
	 * @param int codInstitucion
	 * @return HashMap con los resultados de la obtención de los centros de atención
	 */
	public static HashMap obtenerCentrosDeAtencion (Connection con, int codInstitucion)
	{
		logger.info("===> Entré a obtenerCentrosDeAtencion");
		logger.info("===> codigoInstitucion: "+codInstitucion);
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String cadena = "SELECT " +
					"consecutivo AS consecutivo, " +
					"descripcion AS descripcion " +
					"FROM centro_atencion " +
					"WHERE cod_institucion="+codInstitucion+" " +
					"AND activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" "+
					"order by descripcion"; 
			logger.info("===>Consultar Centros De Atención: "+cadena);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerCentrosDeAtencion: "+e);
		}
		return mapa;
	}
	
	
	/**
	 * Método para cargar la autorizacion de una orden ambulatoria o solicitud
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static DtoAutorizacion cargarAutorizacion(Connection con,HashMap parametros)
	{
		DtoAutorizacion autorizacion = new DtoAutorizacion();
		try
		{
			
			//***********SE CARGA PRIMERO EL DETALLE DE LA AUTORIZACION****************************++
			DtoDetAutorizacion detAutorizacion = cargarDetalleAutorizacion(con,parametros);
			//***************************************************************************************
			
			if(!detAutorizacion.getCodigoPK().equals(""))
			{
				//************SE CARGA EL ENCABEZADO DE LA AUTORIZACIÓN****************************************
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarAutorizacionStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Integer.parseInt(detAutorizacion.getAutorizacion()));
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
				{
					autorizacion.setCodigoPK(rs.getString("codigo"));
					autorizacion.setConsecutivo(rs.getString("consecutivo"));
					autorizacion.setAnioConsecutivo(rs.getString("anio_consecutivo"));
					autorizacion.setIdIngreso(rs.getString("id_ingreso"));
					autorizacion.setIdSubCuenta(rs.getString("id_sub_cuenta"));
					autorizacion.setCodigoTipoServicioSolicitado(rs.getInt("cod_tipo_ser_sol"));
					autorizacion.setNombreTipoServicioSolicitado(rs.getString("nom_tipo_ser_sol"));
					autorizacion.setObservaciones(rs.getString("observaciones"));
					autorizacion.setEstado(rs.getString("estado"));
					autorizacion.setCodigoTipoCobertura(rs.getInt("cod_tipo_cobertura"));
					autorizacion.setNombreTipoCobertura(rs.getString("nom_tipo_cobertura"));
					autorizacion.setCodigoOrigenAtencion(rs.getInt("cod_origen_atencion"));
					autorizacion.setNombreOrigenAtencion(rs.getString("nom_origen_atencion"));
					autorizacion.setFechaSolicitud(rs.getString("fecha_solicitud"));
					autorizacion.setHoraSolicitud(rs.getString("hora_solicitud"));
					if(!rs.getString("usuario_solicitud").toString().equals(""))
						autorizacion.getUsuarioSolicitud().cargarUsuarioBasico(con, rs.getString("usuario_solicitud"));
					autorizacion.setTipoTramite(rs.getString("tipo_tramite"));
					autorizacion.setCodigoConvenio(rs.getInt("codigo_convenio"));
					autorizacion.setNombreConvenio(rs.getString("nombre_convenio"));
					autorizacion.setActivo(rs.getString("activo"));
					autorizacion.setPrioridadAtencion(rs.getString("prioridad_atencion"));
					autorizacion.setIdCuenta(rs.getString("id_cuenta"));
					autorizacion.getCama().setCodigo(rs.getInt("codigo_cama"));
					autorizacion.getCama().setNombre(rs.getString("nombre_cama"));
					autorizacion.setFechaModifica(rs.getString("fecha_modifica"));
					autorizacion.setHoraModifica(rs.getString("hora_modifica"));
					autorizacion.getUsuarioModifica().cargarUsuarioBasico(con, rs.getString("usuario_modifica"));
					
					autorizacion.getDetalle().add(detAutorizacion);
					
				}
				//**********************************************************************************************
				
				if(!autorizacion.getCodigoPK().equals(""))
				{
					//**************SE CARGAN LOS ENVÍOS DE AUTORIZACION**********************************
					autorizacion = cargarEnviosAutorizacion(con,autorizacion);
					//************************************************************************************
					
					//*************SE CARGAN LOS ADJUNTOS DE LA AUTORIZACION********************************+
					autorizacion = cargarAdjuntosAutorizacion(con,autorizacion);
					//****************************************************************************************
					
					//**************SE CARGAN LOS DIAGNÓSTICOAS DE LA AUTORIZACION***************************
					autorizacion = cargarDiagnosticosAutorizacion(con,autorizacion);
					//***************************************************************************************+
				}
				
			}
			
			
		}
		catch(SQLException e)
		{
			logger.info("Error en cargarAutorizacion: "+e);
		}
		return autorizacion;
	}

	/**
	 * Método para cargar los diagnósticos de la autorizacion
	 * @param con
	 * @param autorizacion
	 * @return
	 */
	private static DtoAutorizacion cargarDiagnosticosAutorizacion(Connection con, DtoAutorizacion autorizacion) {
		
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarDiagnosticosAutorizacionStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Integer.parseInt(autorizacion.getCodigoPK()));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoDiagAutorizacion diagnostico = new DtoDiagAutorizacion();
				diagnostico.setCodigoPK(rs.getString("codigo"));
				diagnostico.setAutorizacion(rs.getString("autorizacion"));
				
				diagnostico.getDiagnostico().setAcronimo(rs.getString("acronimo"));
				diagnostico.getDiagnostico().setTipoCIE(rs.getInt("tipo_cie"));
				diagnostico.getDiagnostico().setNombre(rs.getString("nombre"));
				diagnostico.getDiagnostico().setPrincipal(UtilidadTexto.getBoolean(rs.getString("principal")));
				
				
				diagnostico.setFechaModifica(rs.getString("fecha_modifica"));
				diagnostico.setHoraModifica(rs.getString("hora_modifica"));
				diagnostico.getUsuarioModifica().cargarUsuarioBasico(con, rs.getString("usuario_modifica"));
				autorizacion.getDiagnosticos().add(diagnostico);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarDiagnosticosAutorizacion: "+e);
		}
		return autorizacion;
	}

	/**
	 * Método implementado para cargar los adjuntos de la autorizacion
	 * @param con
	 * @param autorizacion
	 * @return
	 */
	private static DtoAutorizacion cargarAdjuntosAutorizacion(Connection con,DtoAutorizacion autorizacion) {
		
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarAdjuntosAutorizacionStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Integer.parseInt(autorizacion.getCodigoPK()));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoAdjAutorizacion adjAutorizacion = new DtoAdjAutorizacion();
				adjAutorizacion.setCodigoPK(rs.getString("codigo_pk"));
				adjAutorizacion.setAutorizacion(rs.getString("autorizacion"));
				adjAutorizacion.setNombreArchivo(rs.getString("nombre_archivo"));
				adjAutorizacion.setNombreOriginal(rs.getString("nombre_original"));
				adjAutorizacion.setFechaModifica(rs.getString("fecha_modifica"));
				adjAutorizacion.setHoraModifica(rs.getString("hora_modifica"));
				adjAutorizacion.getUsuarioModifica().cargarUsuarioBasico(con, rs.getString("usuario_modifica"));
				autorizacion.getAdjuntos().add(adjAutorizacion);
				
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarAdjuntosAutorizacion: "+e);
		}
		return autorizacion;
	}

	/**
	 * Método que carga los envíos de autorizacion
	 * @param con
	 * @param autorizacion
	 * @return
	 */
	private static DtoAutorizacion cargarEnviosAutorizacion(Connection con,DtoAutorizacion autorizacion) {
		try
		{
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarEnvioAutorizacionStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Integer.parseInt(autorizacion.getCodigoPK()));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				DtoEnvioAutorizacion envioAutorizacion = new DtoEnvioAutorizacion();
				envioAutorizacion.setCodigoPK(rs.getString("codigo_pk"));
				//envioAutorizacion.setAutorizacion(rs.getString("autorizacion"));
				if(rs.getInt("cod_entidad_envio")>0)
				{
					envioAutorizacion.setEsEmpresa(true);
					envioAutorizacion.getEntidadEnvio().setCodigo(rs.getInt("cod_entidad_envio"));
					envioAutorizacion.getEntidadEnvio().setNombre(rs.getString("nom_entidad_envio"));
				}
				else
				{
					envioAutorizacion.setEsEmpresa(false);
					envioAutorizacion.getEntidadEnvio().setCodigo(rs.getInt("cod_convenio_envio"));
					envioAutorizacion.getEntidadEnvio().setNombre(rs.getString("nom_convenio_envio"));
				}
				//Se asigna el value para el option del select
				envioAutorizacion.getEntidadEnvio().setValue(envioAutorizacion.getEntidadEnvio().getCodigo()+ConstantesBD.separadorSplit+(envioAutorizacion.isEsEmpresa()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo));
				envioAutorizacion.setMedioEnvio(rs.getString("medio_envio"));
				envioAutorizacion.setFechaModifica(rs.getString("fecha_modifica"));
				envioAutorizacion.setHoraModifica(rs.getString("hora_modifica"));
				envioAutorizacion.getUsuarioModifica().cargarUsuarioBasico(con, rs.getString("usuario_modifica"));
				//autorizacion.getEnvios().add(envioAutorizacion);
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarEnviosAutorizacion: "+e);
		}
		
		return autorizacion;
	}

	/**
	 * Método para cargar el detalle de la autorización
	 * @param con
	 * @param parametros
	 * @return
	 */
	private static DtoDetAutorizacion cargarDetalleAutorizacion(Connection con,HashMap parametros) {
		DtoDetAutorizacion detAutorizacion = new DtoDetAutorizacion();
		try
		{
			//**************SE TOMAN LOS PARÁMETROS**************************************++
			String codigoDetAutorizacion = parametros.get("codigoDetAutorizacion").toString();
			int codigoInstitucion = Integer.parseInt(parametros.get("codigoInstitucion").toString());
			//******************************************************************************
			
			int codigoTarifario = Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion),true);
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarDetAutorizacionesStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoTarifario);
			pst.setInt(2,Integer.parseInt(codigoDetAutorizacion));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				detAutorizacion.setCodigoPK(rs.getString("codigo"));
				detAutorizacion.setAutorizacion(rs.getString("autorizacion"));
				detAutorizacion.setNumeroSolicitud(rs.getString("numero_solicitud"));
				detAutorizacion.setOrdenAmbulatoria(rs.getString("orden_ambulatoria"));
				detAutorizacion.setDetCargo(rs.getString("det_cargo"));
				detAutorizacion.getServicioArticulo().setCodigo(rs.getInt("codigo_servicio"));
				if(detAutorizacion.getServicioArticulo().getCodigo()>0)
				{
					//detAutorizacion.setEsServicio(true);
					detAutorizacion.getServicioArticulo().setNombre(rs.getString("nombre_servicio"));
				}
				else
				{
					//detAutorizacion.setEsServicio(false);
					detAutorizacion.getServicioArticulo().setCodigo(rs.getInt("codigo_articulo"));
					detAutorizacion.getServicioArticulo().setNombre(rs.getString("nombre_articulo"));
				}
				detAutorizacion.setCantidad(rs.getInt("cantidad"));
				detAutorizacion.setJustificacionSolicitud(rs.getString("justificacion_solicitud"));
				detAutorizacion.setFechaModifica(rs.getString("fecha_modifica"));
				detAutorizacion.setHoraModifica(rs.getString("hora_modifica"));
				detAutorizacion.getUsuarioModifica().cargarUsuarioBasico(con, rs.getString("usuario_modifica"));
			}
			
		}
		catch(SQLException e)
		{
			logger.info("Error en cargarDetalleAutorizacion: "+e);
		}
		
		return detAutorizacion;
	}
	
	
	/**
	 * Método para obtener el origen de atencion verificando
	 * la informacion de la cuenta del paciente
	 * @param con
	 * @param idCuenta
	 * @param viaIngreso
	 * @param tipoEvento
	 * @return
	 */
	public static InfoDatosInt obtenerOrigenAtencionXCuenta(Connection con,String idCuenta,int viaIngreso,String tipoEvento)
	{
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
					pst.setInt(1,Integer.parseInt(idCuenta));
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
					pst.setInt(1,Integer.parseInt(idCuenta));
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
}