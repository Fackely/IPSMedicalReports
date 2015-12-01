/*
 * @(#)SqlBaseAdminMedicamentosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.enfermeria.DtoAdministracionMedicamentosBasico;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * Implementación sql genérico de todas las funciones de acceso a la fuente de datos
 * para la admin. de medicamentos
 *
 * @version 1.0, Septiembre 16 / 2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan López</a>
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SqlBaseAdminMedicamentosDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseAdminMedicamentosDao.class);
	
	//PARTE A REUTIZAR ADMIN DE MEDICAMENTOS
	/**
	 *  Listado de las solicitudes  de medicamentos por paciente
	 */
	private final static String listadoSolMedXPacienteStr = "SELECT " +
															"distinct "+
															"CASE WHEN sol.urgente="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN  'urgente' ELSE 'NO' END  AS identificadorPrioridad, "+
															"TO_CHAR(sol.fecha_solicitud,'yyyy-mm-dd') AS fechaSolicitud, "+
															"sol.hora_solicitud||'' AS horaSolicitud, "+
															"sol.numero_solicitud AS numeroSolicitud, " +
															"sol.consecutivo_ordenes_medicas AS orden, "+
															"eshc.nombre AS estadoMedico, "+ 
															"getnombrepersona(cue.codigo_paciente) AS nombresPaciente, " +
															"cue.codigo_paciente AS codigoPaciente,   "+
															"getnombrepersona(sol.codigo_medico) AS medicoSolicitante, "+ 
															"getnomcentrocosto(sol.centro_costo_solicitante) AS centroCostoSolicitante, " +
															"getnomcentrocosto(sol.centro_costo_solicitado) AS farmaciaDespacho, "+
															"cue.id AS codigoCuenta, " +
															"CASE WHEN sol.pyp IS NULL THEN '' ELSE (CASE WHEN sol.pyp ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'Si' ELSE '' END) END AS esPyP, " +
															"coalesce(getcamacuenta(cue.id, cue.via_ingreso), '') as infocama, " +
															"via.nombre as viaIngreso " + 
															"FROM solicitudes sol   "+
															"INNER JOIN solicitudes_medicamentos sm ON (sm.numero_solicitud=sol.numero_solicitud) "+
															"INNER JOIN cuentas cue ON (sol.cuenta=cue.id) "+
															"INNER JOIN estados_sol_his_cli eshc ON (sol.estado_historia_clinica=eshc.codigo) "+
															"LEFT OUTER  JOIN despacho des ON (sol.numero_solicitud= des.numero_solicitud AND des.es_directo<> "+ValoresPorDefecto.getValorTrueParaConsultas()+") "+
															"INNER JOIN vias_ingreso via ON (via.codigo=cue.via_ingreso) "+
															"WHERE cue.codigo_paciente = ? " +
															"AND (cue.estado_cuenta in ( "+ConstantesBD.codigoEstadoCuentaActiva+", "+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") or (cue.estado_cuenta="+ConstantesBD.codigoEstadoCuentaAsociada+" AND getcuentafinal(cue.id) IS NULL)) " +
															"AND sol.tipo="+ConstantesBD.codigoTipoSolicitudMedicamentos+"  "+
															"AND sol.centro_costo_solicitado <>"+ConstantesBD.codigoCentroCostoExternos+" "+
															"AND (sol.estado_historia_clinica= "+ConstantesBD.codigoEstadoHCSolicitada+" OR sol.estado_historia_clinica="+ConstantesBD.codigoEstadoHCDespachada+")  "+
															"AND sm.orden_dieta IS NULL ";
	
	
	/**
	 * inserta el detalle de la administracion
	 */
	private final static String insertarDetalleStr=		"insert into " +
														"detalle_admin " +
														"(articulo,administracion,fecha,hora,cantidad,observaciones,traido_usuario, lote, fecha_vencimiento, art_principal, adelanto_x_necesidad, nada_via_oral, usuario_rechazo) " +
														"values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	
    /**
	 * Contiene la insercion en la tabla de <code>administracion_medicamentos</code>
	 */
	private final static String insertarAdminStr="insert into " +
																	"admin_medicamentos " +
																	"(codigo,numero_solicitud,centro_costo_admin,fecha_grabacion,hora_grabacion,usuario) " +
																	"values " +
																	"(?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?)";
    
	/**
	 * 
	 */
	private final static String existeAcumuladoAdminDosisStr="select articulo FROM acumulado_admin_dosis WHERE articulo=? and numero_solicitud=?";
	
	/**
	 * 
	 */
    private final static String insertarAcumuladoAdminDosisStr= "INSERT INTO acumulado_admin_dosis (articulo, numero_solicitud, nro_dosis_admin_far, nro_dosis_admin_pac, unidades_admin_far, unidades_admin_pac) VALUES (?,?,?,?,?,?) ";

    /**
     * 
     */
    private final static String updateAcumuladoAdminDosisStr="UPDATE acumulado_admin_dosis SET nro_dosis_admin_far=(nro_dosis_admin_far+?), nro_dosis_admin_pac=(nro_dosis_admin_pac+?), unidades_admin_far=(unidades_admin_far+?), unidades_admin_pac=(unidades_admin_pac+?) WHERE articulo=? AND numero_solicitud=?";
    
    /**
     * 
     */
    private final static String insertarFinalizacionAdminArticuloStr="INSERT INTO finalizar_admin_x_art(numero_solicitud,articulo,fecha_grabacion,hora_grabacion,usuario) values (?,?,CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
    
    /**
	 * Carga la info básica del artículo para el resumen de administración de medicamentos
	 */
	private final static String resumenAdminMedDescArticuloStr=
																				"SELECT * FROM ("+	
																				"(SELECT " +
																				"a.codigo AS codigo, " +
																				"a.descripcion AS descripcion, " +
																				"coalesce(a.concentracion, '') AS concentracion, " +
																				"CASE WHEN getNomFormaFarmaceutica(a.forma_farmaceutica) IS NULL THEN '' ELSE getNomFormaFarmaceutica(a.forma_farmaceutica) END AS formaFarmaceutica, " +
																				"CASE WHEN getNomUnidadMedida(a.unidad_medida) IS NULL THEN '' ELSE getNomUnidadMedida(a.unidad_medida) END  AS unidadMedida, " +
																				"CASE WHEN ds.dosis IS NULL THEN '' ELSE ds.dosis END as dosis, " +
																				" coalesce(getcantidadunidosisxunidad(ds.unidosis_articulo),0) as cantidadunidosis, " +
																				" coalesce(getunimedidaunidosisxunidad(ds.unidosis_articulo),'') as unidadmedidaunidosis, " +
																				" esarticulomultidosis(a.codigo) as esmultidosis, " +
																				"ds.frecuencia as frecuencia, " +
																				"CASE WHEN ds.tipo_frecuencia IS NULL THEN '' ELSE ds.tipo_frecuencia END as tipoFrecuencia, " +
																				"ds.cantidad as cantidad, " +
																				"CASE WHEN ds.via IS NULL THEN '' ELSE ds.via END as via, " +
																				"CASE WHEN ds.observaciones IS NULL THEN '' ELSE ds.observaciones END AS observaciones, " +
																				"na.es_pos AS es_pos, " +
																				"CASE WHEN getDespacho(a.codigo,ds.numero_solicitud) IS NULL THEN 0 ELSE getDespacho(a.codigo,ds.numero_solicitud) END AS despachoTotal, " +
																				"CASE WHEN gettotaldespachosaldos(a.codigo, ds.numero_solicitud) IS NULL THEN '0' ELSE gettotaldespachosaldos(a.codigo, ds.numero_solicitud) END as totaldespachosaldos, " +
																				"CASE WHEN getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") IS NULL THEN 0 ELSE getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") END AS totalAdministradoFarmacia, " +
																				"CASE WHEN getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorTrueParaConsultas() + ") IS NULL THEN 0 ELSE getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorTrueParaConsultas() + ") END AS totalAdministradoPaciente, " +
																				"CASE WHEN getTotalAdmin(a.codigo,ds.numero_solicitud) IS NULL THEN '0' ELSE getTotalAdmin(a.codigo,ds.numero_solicitud)||'' END AS totalAdministrado, " +
																				"'"+ConstantesBD.acronimoNo+"' AS adelanto_x_necesidad," +
																				"'"+ConstantesBD.acronimoNo+"' AS nada_via_oral," +
																				"'"+ConstantesBD.acronimoNo+"' AS usuario_rechazo " +
																				"FROM articulo a " +
																				" inner join detalle_solicitudes ds on (a.codigo=ds.articulo and ds.articulo_principal is null) " +
																				"inner join solicitudes_medicamentos sm on (ds.numero_solicitud=sm.numero_solicitud) " +
																				"inner join naturaleza_articulo na ON (a.naturaleza=na.acronimo and na.institucion=a.institucion) " +
																				"where " +
																				//XPLANNER 14500
																				//"va.naturaleza IN ('"+ConstantesBD.codigoNaturalezaArticuloMedicamentosNoPos+ "','"+ConstantesBD.codigoNaturalezaArticuloMedicamentosPos+"') AND"+
																				"ds.numero_solicitud = ?) " +
																				"UNION ALL " +
																				"(SELECT " +
																				"distinct(deta.articulo) AS codigo, " +
																				"a.descripcion AS descripcion, " +
																				"coalesce(a.concentracion, '') AS concentracion, " +
																				"CASE WHEN getNomFormaFarmaceutica(a.forma_farmaceutica) IS NULL THEN '' ELSE getNomFormaFarmaceutica(a.forma_farmaceutica) END AS formaFarmaceutica, " +
																				"CASE WHEN getNomUnidadMedida(a.unidad_medida) IS NULL THEN '' ELSE getNomUnidadMedida(a.unidad_medida) END  AS unidadMedida, " +
																				"CASE WHEN ds.dosis IS NULL THEN '' ELSE ds.dosis END  as dosis, " +
																				" coalesce(getcantidadunidosisxunidad(ds.unidosis_articulo),0) as cantidadunidosis, " +
																				" coalesce(getunimedidaunidosisxunidad(ds.unidosis_articulo),'') as unidadmedidaunidosis, " +
																				" esarticulomultidosis(a.codigo) as esmultidosis, " +
																				"ds.frecuencia as frecuencia, " +
																				"CASE WHEN ds.tipo_frecuencia IS NULL THEN '' ELSE ds.tipo_frecuencia END as tipoFrecuencia, " +
																				"ds.cantidad as cantidad, " +
																				"CASE WHEN ds.via IS NULL THEN '' ELSE ds.via END AS via, " +
																				"CASE WHEN ds.observaciones IS NULL THEN '' ELSE ds.observaciones END AS observaciones, " +
																				"na.es_pos AS es_pos, " +
																				"getDespacho(a.codigo,ds.numero_solicitud) AS despachoTotal, " +
																				"CASE WHEN gettotaldespachosaldos(a.codigo, ds.numero_solicitud) IS NULL THEN '0' ELSE gettotaldespachosaldos(a.codigo, ds.numero_solicitud) END as totaldespachosaldos, " +
																				"getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") AS totalAdministradoFarmacia, " +
																				"getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorTrueParaConsultas() + ") AS totalAdministradoPaciente, " +
																				"getTotalAdmin(a.codigo,ds.numero_solicitud)||'' AS totalAdministrado, " +
																				"deta.adelanto_x_necesidad," +
																				"deta.nada_via_oral," +
																				"deta.usuario_rechazo " +
																				"FROM detalle_admin deta " +
																				"INNER JOIN admin_medicamentos admin ON (admin.codigo=deta.administracion) " +
																				"INNER JOIN despacho desp ON (desp.numero_solicitud=admin.numero_solicitud) " +
																				"INNER JOIN detalle_despachos detd ON (detd.despacho=desp.orden AND detd.articulo=deta.articulo) " +
																				"INNER JOIN detalle_solicitudes ds ON (ds.articulo=detd.art_principal AND ds.numero_solicitud= admin.numero_solicitud) " +
																				"INNER JOIN articulo a ON (a.codigo=deta.articulo) " +
																				"inner join naturaleza_articulo na ON (a.naturaleza=na.acronimo and na.institucion=a.institucion) " +
																				"where admin.numero_solicitud=? and detd.articulo<>detd.art_principal) " +
										                                        "UNION ALL " +
										                                        "(SELECT distinct(deta.articulo) AS codigo, " +
										                                        "a.descripcion AS descripcion, " +
										                                        "coalesce(a.concentracion, '') AS concentracion, " +
										                                        "CASE WHEN getNomFormaFarmaceutica(a.forma_farmaceutica) IS NULL THEN '' ELSE getNomFormaFarmaceutica(a.forma_farmaceutica) END AS formaFarmaceutica, " +
										                                        "CASE WHEN getNomUnidadMedida(a.unidad_medida) IS NULL THEN '' ELSE getNomUnidadMedida(a.unidad_medida) END  AS unidadMedida, " +
										                                        "''  as dosis, " +
																				" 0 as cantidadunidosis, " +
																				" '' as unidadmedidaunidosis, " +
																				" 'N' as esmultidosis, " +
										                                        
										                                        "0 as frecuencia, " +
										                                        "'' as tipoFrecuencia, " +
										                                        "0 as cantidad, " +
										                                        "'' AS via, " +
										                                        "'' AS observaciones, " +
										                                        "na.es_pos AS es_pos, " +
										                                        "getDespacho(a.codigo,admin.numero_solicitud) AS despachoTotal, " +
										        								"CASE WHEN gettotaldespachosaldos(a.codigo, admin.numero_solicitud) IS NULL THEN '0' ELSE gettotaldespachosaldos(a.codigo, admin.numero_solicitud) END as totaldespachosaldos, " +
										                                        "getTotalAdminFarmacia(a.codigo,admin.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") AS totalAdministradoFarmacia, " +
										                                        "getTotalAdminFarmacia(a.codigo,admin.numero_solicitud,"+ValoresPorDefecto.getValorTrueParaConsultas()+") AS totalAdministradoPaciente, " +
										                                        "getTotalAdmin(a.codigo,admin.numero_solicitud)||'' AS totalAdministrado, " +
										                                        "deta.adelanto_x_necesidad," +
										        								"deta.nada_via_oral," +
										        								"deta.usuario_rechazo " +
										                                        "FROM detalle_admin deta " +
										                                        "INNER JOIN admin_medicamentos admin ON (admin.codigo=deta.administracion) " +
										                                        "INNER JOIN despacho desp ON (desp.numero_solicitud=admin.numero_solicitud) " +
										                                        "INNER JOIN detalle_despachos detd ON (detd.despacho=desp.orden AND detd.articulo=deta.articulo) " +
										                                        "INNER JOIN articulo a ON (a.codigo=deta.articulo) " +
										                                        "inner join naturaleza_articulo na ON (a.naturaleza=na.acronimo and na.institucion=a.institucion) " +
										                                        "where admin.numero_solicitud=? and na.es_medicamento='"+ConstantesBD.acronimoNo+"' and deta.articulo NOT IN (SELECT des.articulo from detalle_solicitudes des where des.numero_solicitud=admin.numero_solicitud))) " +
																				"TABLA " +
																				"WHERE  TABLA.codigo IN " +
																				"(SELECT deta.articulo FROM detalle_admin deta, admin_medicamentos admi where admi.codigo=deta.administracion AND admi.numero_solicitud=?)  " +
																				"ORDER BY TABLA.codigo " ;
    
	/**
	 * Carga info particular de cada articulo como su cantidad, fecha -hora admin.....
	 */
	private final static String resumenAdminMedDetalleStr= 	"SELECT " +
																					"deta.articulo AS articulo, " +
																					"deta.cantidad AS cantidad, " +
																					"to_char(deta.fecha,'dd/mm/yyyy') AS fechaAdministracion, " +
																					"deta.hora AS horaAdministracion, " +
																					"deta.observaciones AS observaciones, " +
																					"admi.usuario AS usuario, " +
																					"to_char(admi.fecha_grabacion,'dd/mm/yyyy') AS fechaGrabacion, " +
																					"admi.hora_grabacion AS horaGrabacion, " +
																					"CASE WHEN deta.traido_usuario="+ValoresPorDefecto.getValorTrueParaConsultas()+" then 'Si' ELSE 'No' END  AS traidoPaciente, " +
																					"deta.adelanto_x_necesidad," +
																					"deta.nada_via_oral," +
																					"deta.usuario_rechazo " +
																					"FROM " +
																					"detalle_admin deta, " +
																					"admin_medicamentos admi " +
																					"where " +
																					"admi.numero_solicitud=? " +
																					"AND " +
																					"admi.codigo=deta.administracion " +
																					"ORDER BY fechaAdministracion DESC, horaAdministracion DESC ";
	
	/**
	 * Busca la fecha y hora de la última administración
	 */
	private final static String fechaHoraUtlimaAdministracion="SELECT max(fecha_grabacion || '_' || hora_grabacion) AS fecha from admin_medicamentos where numero_solicitud=?";
	
	/**
	 * Consulta para obtener los medicamentos despachados a un paciente
	 */
	private final static String listadoMedicamentosDespachados =
															"SELECT articulos.*  FROM "+
																	"((SELECT ds.articulo as cod_articulo, ds.numero_solicitud AS numero_solicitud, getdescripcionarticulo(ds.articulo) AS articulo "+
																		" FROM detalle_solicitudes ds INNER JOIN articulo a ON(a.codigo=ds.articulo)) "+
																	" UNION "+
																	" (SELECT dd.articulo AS cod_articulo, d.numero_solicitud AS numerosolicitud, getdescripcionarticulo(dd.articulo) as articulo "+
																		 " FROM detalle_despachos dd "+
																		 	"INNER JOIN despacho d ON(dd.despacho=d.orden) "+
																		 	"INNER JOIN detalle_solicitudes ds ON(dd.art_principal=ds.articulo AND dd.articulo<>dd.art_principal AND ds.numero_solicitud=d.numero_solicitud) "+
																		")) articulos "+
															" INNER JOIN solicitudes sol ON(sol.numero_solicitud=articulos.numero_solicitud) "+
																" WHERE sol.cuenta=? ORDER BY articulos.articulo";
	
	/**
	 * Sección SELECT para el listado de las solicitudes de medicamentos por área
	 */
	private final static String listadoMedicamentosCentroCosto = 
			"SELECT articulos.codigoArticulo, articulos.numerosolicitud AS numero_solicitud , articulos.articulo  AS articulo  FROM "+
									"( (SELECT ds.articulo AS codigoArticulo, 	ds.numero_solicitud AS numerosolicitud, 	getdescripcionarticulo(ds.articulo) AS articulo "+
										" from detalle_solicitudes ds INNER JOIN articulo a ON(a.codigo=ds.articulo)) "+
									" UNION" +
									"(SELECT dd.articulo, 	d.numero_solicitud AS numerosolicitud, getdescripcionarticulo(dd.articulo) as articulo "+
										" from detalle_despachos dd "+
											" INNER JOIN despacho d ON(dd.despacho=d.orden) "+
											"INNER JOIN detalle_solicitudes ds ON(dd.art_principal=ds.articulo AND dd.articulo<>dd.art_principal AND ds.numero_solicitud=d.numero_solicitud) "+
									")	) articulos "+
										" WHERE articulos.numerosolicitud IN ( ";
	
    /**
	 * Carga el Listado de las solicitudes de medicamentos para un paciente determinado
     * @param institucion 
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigoPaciente, int, codigo del paciente
	 * @return ResulSet list
	 */
	@SuppressWarnings("rawtypes")
	public static Collection listadoSolMedXPaciente(Connection con, int codigoPaciente, int institucion)
	{
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		Collection resultado=null;
		try
		{
			String consulta=listadoSolMedXPacienteStr;
			if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso(institucion)))
			{
				consulta=consulta+" and (getExisteEgreso(cuenta)=" + ValoresPorDefecto.getValorFalseParaConsultas() + " or (getExisteEgreso(cuenta)=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and getExEgrCondHospEnPiso(cuenta,"+ConstantesBD.codigoDestinoSalidaHospitalizacion+" )=" + ValoresPorDefecto.getValorTrueParaConsultas() + ")  "+ " or (getExisteEgreso(cuenta)=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and getExEgrCondHospEnPiso(cuenta,"+ConstantesBD.codigoDestinoSalidaCirugiaAmbulatoria+" )=" + ValoresPorDefecto.getValorTrueParaConsultas() + ")  "+ " or (getExisteEgreso(cuenta)=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and getExEgrCondHospEnPiso(cuenta,"+ConstantesBD.codigoDestinoSalidaHospitalizar+" )=" + ValoresPorDefecto.getValorTrueParaConsultas() + ") "+ " or (getExisteEgreso(cuenta)=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and getExEgrCondHospEnPiso(cuenta,"+ConstantesBD.codigoDestinoSalidaTrasladoCuidadoEspecial+" )=" + ValoresPorDefecto.getValorTrueParaConsultas() + ")  )";
			}
			consulta=consulta+" ORDER BY fechaSolicitud ASC, horaSolicitud ASC" ;
			pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoPaciente);
			rs= new ResultSetDecorator(pst.executeQuery());
			resultado=UtilidadBD.resultSet2Collection(rs);
		}
		catch(Exception e){
			Log4JManager.error("ERROR listadoSolMedXPaciente", e);
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultado;
	}
	
	
	
	/**
	 * Carga el Listado de las solicitudes de medicamentos para un AREA determinada
	 * @param consulta
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigoPaciente, int, codigo del paciente
	 * @return ResulSet list
	 */
	@SuppressWarnings("rawtypes")
	public static Collection listadoSolMedXArea(Connection con, HashMap<String, Object> criteriosBusquedaMap, String consulta)
	{
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		Collection resultado=null;
		
		try
		{
			if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getPermitirOrdenarMedicamentosPacienteUrgenciasConEgreso(Utilidades.convertirAEntero(criteriosBusquedaMap.get("codigoInstitucion")+""))))
			{
				consulta+=" and (getExisteEgreso(sol.cuenta)=" + ValoresPorDefecto.getValorFalseParaConsultas() + " or (getExisteEgreso(sol.cuenta)=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and getExEgrCondHospEnPiso(sol.cuenta,"+ConstantesBD.codigoDestinoSalidaHospitalizacion+" )=" + ValoresPorDefecto.getValorTrueParaConsultas() + ")  "+ " or (getExisteEgreso(sol.cuenta)=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and getExEgrCondHospEnPiso(sol.cuenta,"+ConstantesBD.codigoDestinoSalidaCirugiaAmbulatoria+" )=" + ValoresPorDefecto.getValorTrueParaConsultas() + ")  "+ " or (getExisteEgreso(sol.cuenta)=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and getExEgrCondHospEnPiso(sol.cuenta,"+ConstantesBD.codigoDestinoSalidaHospitalizar+" )=" + ValoresPorDefecto.getValorTrueParaConsultas() + ") "+ " or (getExisteEgreso(sol.cuenta)=" + ValoresPorDefecto.getValorTrueParaConsultas() + " and getExEgrCondHospEnPiso(sol.cuenta,"+ConstantesBD.codigoDestinoSalidaTrasladoCuidadoEspecial+" )=" + ValoresPorDefecto.getValorTrueParaConsultas() + ")  )";
			}
			
			
			if(UtilidadFecha.esFechaValidaSegunAp(criteriosBusquedaMap.get("fechaInicialFiltro").toString())
				&& UtilidadFecha.esFechaValidaSegunAp(criteriosBusquedaMap.get("fechaFinalFiltro").toString()))
			{
				consulta+=" AND (to_char(sol.fecha_solicitud, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicialFiltro")+"")+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinalFiltro")+"")+"' )";
			}
			//codigoInstitucion
			if(Integer.parseInt(criteriosBusquedaMap.get("areaFiltro").toString())>0)
				consulta+=" AND cue.area="+criteriosBusquedaMap.get("areaFiltro")+" ";
			
			//filtrado por piso, habitacion cama
			if(Integer.parseInt(criteriosBusquedaMap.get("camaFiltro").toString())>0)
				consulta+=" AND  cue.id in (SELECT h.cuenta from his_camas_cuentas h where h.codigocama="+criteriosBusquedaMap.get("camaFiltro")+") ";
			else
			{
				if(Integer.parseInt(criteriosBusquedaMap.get("habitacionFiltro").toString())>0)
					consulta+=" AND  cue.id in (SELECT h.cuenta from his_camas_cuentas h where h.codigopkhabitacion="+criteriosBusquedaMap.get("habitacionFiltro")+") ";
				else
				{
					if(Integer.parseInt(criteriosBusquedaMap.get("pisoFiltro").toString())>0)
						consulta+=" AND  cue.id in (SELECT h.cuenta from his_camas_cuentas h where h.codigopkpiso="+criteriosBusquedaMap.get("pisoFiltro")+") ";
				}
			}
			//filtrado por Via de Ingreso
			if(Integer.parseInt(criteriosBusquedaMap.get("viaIngresoFiltro").toString())>0)
				consulta+=" AND via.codigo="+criteriosBusquedaMap.get("viaIngresoFiltro")+" ";
			
			consulta+=" ORDER BY fechaSolicitud ASC, horaSolicitud ASC ";
			
			logger.info(consulta);

			pst= new PreparedStatementDecorator(con.prepareStatement(consulta ,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs= new ResultSetDecorator(pst.executeQuery());
			resultado=UtilidadBD.resultSet2Collection(rs);
		}
		catch(Exception e){
			Log4JManager.error("ERROR listadoSolMedXArea", e);
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultado;
	}
	
	/**
	 * Carga la Información de la creación de la solicitud 
	 * @param encabezadoSolicitudMedicamentosStr
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroSolicitud, int, numero de la solicitud 
	 * @return ResulSet list
	 */
	public static ResultSetDecorator encabezadoSolicitudMedicamentos(Connection con, int numeroSolicitud, String encabezadoSolicitudMedicamentosStr)
	{
		try
		{
			logger.info(encabezadoSolicitudMedicamentosStr+" numero solicitud: "+numeroSolicitud);
			PreparedStatementDecorator infoStatement= new PreparedStatementDecorator(con.prepareStatement(encabezadoSolicitudMedicamentosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			infoStatement.setInt(1,numeroSolicitud);
			return new ResultSetDecorator(infoStatement.executeQuery());
		}  
		catch(SQLException e)
		{
			logger.warn(e+" Error en cargar la info de la solicitud de medicamentos: SqlBaseAdminMedicamentosDao "+e.toString());
			return null;
		}
	}
	
	
	/**
	 * Carga el Listado de las solicitudes de medicamentos para un numeroSolicitud determinado
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroSolicitud
	 * @param codigoInstitucion Código de la institución del ususario
	 * @return ResulSet list
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap listadoMedicamentos(Connection con, int numeroSolicitud, int codigoInstitucion, boolean mostrarFinalizados)
	{
		HashMap map= new HashMap();
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		try{
		
			map.put("numRegistros", "0");
			String listadoMedicamentosStr="( " +
												" SELECT " +
													" a.codigo AS codigo, " +
													" 0 AS artppal, " +
													" a.descripcion AS descripcion, " +
													" ordenes.getarticulotienesustituto(sm.numero_solicitud,a.codigo) as tienesustituto," +
													" getconcentracionarticulo(a.codigo) AS concentracion, " +
													" getformafarmaceuticaarticulo(a.codigo) AS formafarmaceutica, " +
													" getunidadmedidaarticulo(a.codigo) AS unidadmedida, " +
													" ds.dosis AS dosis, " +
													" CASE WHEN ds.unidosis_articulo IS NULL THEN '' ELSE getumxunidosisarticulo(ds.unidosis_articulo) END as unidosis, " +
													" coalesce(getcantidadunidosisxunidad(ds.unidosis_articulo),0) as cantidadunidosis, " +
													" coalesce(getunimedidaunidosisxunidad(ds.unidosis_articulo),'') as unidadmedidaunidosis, " +
													" esarticulomultidosis(a.codigo) as esmultidosis, " +
													" ds.frecuencia AS frecuencia, " +
													" ds.tipo_frecuencia as tipofrecuencia, " +
													" ds.cantidad as cantidad, " +
													" ds.dias_tratamiento as diastratamiento, " +
													" ds.via AS via, " +
													" na.es_pos AS espos, " +
													" CASE WHEN ds.nro_dosis_total IS NULL THEN 0 ELSE ds.nro_dosis_total END AS cantidadtotaldosisordenada, " +
													" CASE WHEN aad.nro_dosis_admin_far IS NULL THEN 0 ELSE aad.nro_dosis_admin_far END as numerodosisadminfarmacia, " +
													" CASE WHEN aad.nro_dosis_admin_pac IS NULL THEN 0 ELSE aad.nro_dosis_admin_pac END as numerodosisadminpaciente, " +
													" to_char(CURRENT_DATE, 'DD/MM/YYYY') AS fecha, " +
													" "+ValoresPorDefecto.getSentenciaHoraActualBD()+" AS hora, " +
													" '"+ConstantesBD.acronimoNo+"' AS administrar, " +
													" '"+ConstantesBD.acronimoNo+"' AS traidopaciente, " +
													" ds.observaciones AS observaciones, " +
													" (convertiranumero(getDespacho(a.codigo, ds.numero_solicitud)||'') - getCantidadDevolucion(a.codigo, ds.numero_solicitud)) as unidadesdespachadas, " +
													" CASE WHEN gettotaldespachosaldos(a.codigo, ds.numero_solicitud) IS NULL THEN '0' ELSE gettotaldespachosaldos(a.codigo, ds.numero_solicitud) END as totaldespachosaldos, " +
													" CASE WHEN getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorTrueParaConsultas() + ") IS NULL THEN 0 ELSE getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorTrueParaConsultas() + ") END AS unidadesconsumidasxpaciente, " +
													" CASE WHEN getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") IS NULL THEN 0 ELSE getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") END AS unidadesconsumidasxfarmacia, " +
													" 0 as unidadesconsumidas, "+
													" CASE WHEN getcantxunidosisarticulo(ds.unidosis_articulo)>0 THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' end as leerunidadesconsumidas, "+
													" getcantxunidosisarticulo(ds.unidosis_articulo) as  cantidadunidosisarticulo, "+
													" '' as lote, " +
													" '' as fechavencimientolote, " +
													" '' as existenciaxlote, " +
													" '"+ConstantesBD.acronimoNo+"' AS finalizararticulo, " +
													" 'vacio' AS codigosustitutoprincipal, " +
													" sa.motivo AS motivosuspension, " +
													" CASE WHEN sa.articulo IS NULL THEN " + ValoresPorDefecto.getValorFalseParaConsultas() + " ELSE " + ValoresPorDefecto.getValorTrueParaConsultas() + " END AS essuspendido, " +
													" a.multidosis as multidosis, " +
													" CASE WHEN aad.unidades_admin_far IS NULL THEN 0 ELSE aad.unidades_admin_far END as unidadesadminfarmacia, " +
													" CASE WHEN aad.unidades_admin_pac IS NULL THEN 0 ELSE aad.unidades_admin_pac END AS unidadesadminpaciente, " +
													//Se toma el valor del cargo (en el caso que se haya generado)
													//"CASE WHEN getvalortotalcargomedicamentos(sm.numero_solicitud,a.codigo) IS NULL " +
													//"THEN 0 ELSE (CASE WHEN getvalortotalcargomedicamentos(sm.numero_solicitud,a.codigo)*getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") IS NULL THEN 0 ELSE getvalortotalcargomedicamentos(sm.numero_solicitud,a.codigo)*getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") END) END AS valorcargo, ";
													" 0 AS valorcargo, ";
			 
			int[] codigosJustificacion=Utilidades.buscarCodigosJustificaciones(con, codigoInstitucion, false, true);
			for(int i=0; i<codigosJustificacion.length; i++)
			{
				listadoMedicamentosStr+=
					"getNopos("+codigosJustificacion[i]+",a.codigo, ds.numero_solicitud) AS just"+codigosJustificacion[i]+", " ;
			}
													
			listadoMedicamentosStr+=			" '' as observacionnueva," +
												"coalesce(getExistenSolArt(getingresosolicitud(ds.numero_solicitud),a.codigo,ds.dosis,ds.frecuencia || '',ds.via),0) AS existenordenesante " +
												"FROM " +
													"solicitudes_medicamentos sm " +
													"inner join detalle_solicitudes ds on (sm.numero_solicitud=ds.numero_solicitud and ds.articulo_principal is null) " +
													"inner join articulo a on (ds.articulo=a.codigo) " +
													"INNER JOIN naturaleza_articulo na ON(na.acronimo=a.naturaleza) " +												
													"LEFT OUTER JOIN acumulado_admin_dosis aad ON (aad.articulo=a.codigo and aad.numero_solicitud=sm.numero_solicitud) " +
													"LEFT OUTER JOIN suspension_articulo sa ON(sa.numero_solicitud=ds.numero_solicitud AND sa.articulo=ds.articulo) "+
												"WHERE " +
													"ds.numero_solicitud=? " +
													"and na.es_medicamento='"+ConstantesBD.acronimoSi+"' " ;
													if(!mostrarFinalizados)
													{
														listadoMedicamentosStr+=" and getessolarticulofinalizada(ds.numero_solicitud, a.codigo)='"+ConstantesBD.acronimoNo+"' " ;
													}
													
			listadoMedicamentosStr+=
											") " +
										"UNION ALL " +
										"(" +
											"SELECT " +
												" distinct (detd.articulo) AS codigo, " +
												" detd.art_principal AS artppal, " +
												" a.descripcion AS descripcion, " +
												" -1 as tienesustituto," +
												" getconcentracionarticulo(a.codigo) AS concentracion, " +
												" getformafarmaceuticaarticulo(a.codigo) AS formafarmaceutica, " +
												" getunidadmedidaarticulo(a.codigo) AS unidadmedida, " +
												" case WHEN detd.art_principal is not null then " +
												"	(to_number(ds.dosis,'999999.99999') * (SELECT eqi.cantidad FROM equivalentes_inventario eqi WHERE a.codigo = eqi.articulo_equivalente AND eqi.articulo_ppal=detd.art_principal))||'' " +
												" else " +
													" ds.dosis " +
												" end as dosis, " +
												//"(SELECT eqi.cantidad FROM equivalentes_inventario eqi WHERE a.codigo = eqi.articulo_equivalente AND eqi.articulo_ppal="+codigoArticulo+") AS cantidadeq, " +
												" CASE WHEN ds.unidosis_articulo IS NULL THEN '' ELSE getumxunidosisarticulo(ds.unidosis_articulo) END as unidosis, " +
												" coalesce(getcantidadunidosisxunidad(ds.unidosis_articulo),0) as cantidadunidosis, " +
												" coalesce(getunimedidaunidosisxunidad(ds.unidosis_articulo),'') as unidadmedidaunidosis, " +
												" esarticulomultidosis(a.codigo) as esmultidosis, " +
												" ds.frecuencia as frecuencia, " +
												" ds.tipo_frecuencia as tipofrecuencia, " +
												" ds.cantidad as cantidad, " +
												" ds.dias_tratamiento as diastratamiento, " +
												" ds.via as via, " +
												" na.es_pos AS espos, " +
												" CASE WHEN ds.nro_dosis_total IS NULL THEN 0 ELSE ds.nro_dosis_total END AS cantidadtotaldosisordenada, " +
												" CASE WHEN aad.nro_dosis_admin_far IS NULL THEN 0 ELSE aad.nro_dosis_admin_far END as numerodosisadminfarmacia, " +
												" CASE WHEN aad.nro_dosis_admin_pac IS NULL THEN 0 ELSE aad.nro_dosis_admin_pac END as numerodosisadminpaciente, " +
												" to_char(CURRENT_DATE, 'DD/MM/YYYY') AS fecha, " +
												" "+ValoresPorDefecto.getSentenciaHoraActualBD()+" AS hora, " +
												" '"+ConstantesBD.acronimoNo+"' AS administrar, " +
												" '"+ConstantesBD.acronimoNo+"' AS traidopaciente, " +
												" ds.observaciones AS observaciones, " +
												" (convertiranumero(getDespacho(a.codigo, ds.numero_solicitud)||'') - getCantidadDevolucion(a.codigo, ds.numero_solicitud)) as unidadesdespachadas, " +
												" CASE WHEN gettotaldespachosaldos(a.codigo, ds.numero_solicitud) IS NULL THEN '0' ELSE gettotaldespachosaldos(a.codigo, ds.numero_solicitud) END as totaldespachosaldos, " +
												" CASE WHEN getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorTrueParaConsultas() + ") IS NULL THEN 0 ELSE getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorTrueParaConsultas() + ") END AS unidadesconsumidasxpaciente, " +
												" CASE WHEN getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") IS NULL THEN 0 ELSE getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") end AS unidadesconsumidasxfarmacia, " +
												" 0 as unidadesconsumidas, " +
												" CASE WHEN getcantxunidosisarticulo(ds.unidosis_articulo)>0 THEN '"+ConstantesBD.acronimoNo+"' ELSE '"+ConstantesBD.acronimoSi+"' end as leerunidadesconsumidas, "+
												" getcantxunidosisarticulo(ds.unidosis_articulo) as  cantidadunidosisarticulo, "+
												" '' as lote, " +
												" '' as fechavencimientolote, " +
												" '' as existenciaxlote, " +
												" '"+ConstantesBD.acronimoNo+"' AS finalizararticulo, " +
												" detd.articulo ||'@'|| detd.art_principal AS codigosustitutoprincipal,  " +
												" sa.motivo AS motivosuspension, " +
												" CASE WHEN sa.articulo IS NULL THEN " + ValoresPorDefecto.getValorFalseParaConsultas() + " ELSE " + ValoresPorDefecto.getValorTrueParaConsultas() + " END AS essuspendido, "+
												" a.multidosis as multidosis, "+
												" CASE WHEN aad.unidades_admin_far IS NULL THEN 0 ELSE aad.unidades_admin_far END as unidadesadminfarmacia, " +
												" CASE WHEN aad.unidades_admin_pac IS NULL THEN 0 ELSE aad.unidades_admin_pac END AS unidadesadminpaciente, "+
												//Se toma el valor del cargo (en el caso que se haya generado)
												//"CASE WHEN getvalortotalcargomedicamentos(ds.numero_solicitud,detd.articulo) IS NULL " +
												//"THEN 0 ELSE (CASE WHEN getvalortotalcargomedicamentos(ds.numero_solicitud,detd.articulo)*getTotalAdminFarmacia(detd.articulo,ds.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") IS NULL THEN 0 ELSE getvalortotalcargomedicamentos(ds.numero_solicitud,detd.articulo)*getTotalAdminFarmacia(detd.articulo,ds.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") END) END AS valorcargo, ";
												" 0 AS valorcargo, ";
												
												
			for(int i=0; i<codigosJustificacion.length; i++)
			{
				listadoMedicamentosStr+=
					"getNopos("+codigosJustificacion[i]+",a.codigo, ds.numero_solicitud) AS just"+codigosJustificacion[i]+", " ;
			}
			
			listadoMedicamentosStr+=		" '' as observacionnueva," +
											"coalesce(getExistenSolArt(getingresosolicitud(ds.numero_solicitud),codigo,ds.dosis,ds.frecuencia || '',ds.via),0) AS existenordenesante " + 
											"FROM " +
												" despacho desp " +
												" INNER JOIN detalle_despachos detd on (detd.despacho=desp.orden) " +
												" INNER JOIN detalle_solicitudes ds ON (ds.articulo=detd.art_principal AND ds.numero_solicitud= desp.numero_solicitud) " +
												" INNER JOIN articulo a ON (a.codigo=detd.articulo ) " +
												" INNER JOIN naturaleza_articulo na ON(na.acronimo=a.naturaleza) " +											
												" LEFT OUTER JOIN acumulado_admin_dosis aad ON (aad.articulo=a.codigo and aad.numero_solicitud=desp.numero_solicitud) " +
												" LEFT OUTER JOIN suspension_articulo sa ON(sa.numero_solicitud=ds.numero_solicitud AND sa.articulo=a.codigo)  " +
											"WHERE " +
												" desp.numero_solicitud = ? "+
												" and na.es_medicamento='"+ConstantesBD.acronimoSi+"' " +
												" and detd.articulo NOT IN (select articulo from detalle_solicitudes where detalle_solicitudes.numero_solicitud=desp.numero_solicitud  and detalle_solicitudes.articulo_principal is null) ";
												if(!mostrarFinalizados)
												{
													listadoMedicamentosStr+=" and getessolarticulofinalizada(ds.numero_solicitud, a.codigo)='"+ConstantesBD.acronimoNo+"' " ;
												}
			listadoMedicamentosStr+=	") " ;
		
			pst= new PreparedStatementDecorator(con,listadoMedicamentosStr);
			pst.setInt(1,numeroSolicitud);
			pst.setInt(2,numeroSolicitud);
			rs=new ResultSetDecorator(pst.executeQuery());
			map=UtilidadBD.cargarValueObject(rs);
		}
		catch(Exception e){
			Log4JManager.error("ERROR listadoMedicamentos", e);
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return map;
	}
	
	
	/**
	 * Carga el listado de los insumos correspondientes al numero de solicitud,
	 * segun las restricciones en la consulta <code>listadoInsumosGeneralStr</code>.
	 * @param con Connection conexion con la fuente de datos.
	 * @param numeroSolicitud Codigo del numero de la solicitud.
	 * @return ResultSetDecorator list.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static HashMap listadoInsumos(Connection con, int numeroSolicitud, boolean mostrarFinalizados)
	{
		HashMap map= new HashMap();
		map.put("numRegistros", "0");
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		try
	    {
			/**
			 * LISTA LOS INSUMOS, EL PRIMER SELECT TRAE LOS INSUMOS QUE VIENEN DESDE LA SOLICITUD, 
			 * EL UNION TRAE LOS QUE SE SOLICITARON DESDE EL DESPACHO PERO NO TIENEN UN INSERT EN EL DETALLE DE LA SOLICITUD 
			 */
			String listadoInsumosGeneralStr= 	"SELECT " +
												"ds.articulo AS codigo, " +
												"a.descripcion, " +
												"getnomunidadmedida(a.unidad_medida) AS unidadmedida, " +
												"CASE WHEN getInfoArticulos(sol.numero_solicitud,a.codigo) IS NULL OR getInfoArticulos(sol.numero_solicitud,a.codigo)='' THEN '0' ELSE getInfoArticulos(sol.numero_solicitud,a.codigo)||'' END as cantidaddespacho, " +
												"CASE WHEN getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") IS NULL THEN 0 " +
													"ELSE getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") END AS consumofarmacia, "+
												"CASE WHEN getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorTrueParaConsultas() + ") IS NULL THEN 0 " +
													"ELSE getTotalAdminFarmacia(a.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorTrueParaConsultas() + ") END AS consumopaciente, "+
												//Se toma el valor del cargo (en el caso que se haya generado)
												//"CASE WHEN getvalortotalcargomedicamentos(sol.numero_solicitud,va.codigo) IS NULL THEN 0 " +
													//"ELSE ( CASE WHEN getvalortotalcargomedicamentos(sol.numero_solicitud,va.codigo)*getTotalAdminFarmacia(va.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() +") IS NULL THEN 0 ELSE getvalortotalcargomedicamentos(sol.numero_solicitud,va.codigo)*getTotalAdminFarmacia(va.codigo,ds.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() +") END) END AS valorcargo, " +
												" 0 AS valorcargo, "+	
												"'"+ConstantesBD.acronimoNo+"' as traidopacienteinsumo, " +
												"'' as loteinsumo,  " +
												"'' as fechavencimientoloteinsumo, " +
												"'' as existenciaxloteinsumo, " +
												"'0' as consumo," +
												"coalesce(getExistenSolInsumo(getingresosolicitud(sol.numero_solicitud),ds.articulo),0) AS existenordenesante, " +
												"coalesce(ds.cantidad, 0) as cantidadsolicitada " + 
											"from " +
												"solicitudes sol " +
												"inner join detalle_solicitudes ds on (sol.numero_solicitud=ds.numero_solicitud) " +
												"inner join articulo a ON(a.codigo=ds.articulo) " +
												"inner join naturaleza_articulo na ON (a.naturaleza=na.acronimo and na.institucion=a.institucion) " +
											"where " +
												"ds.numero_solicitud=? " +
												"and na.es_medicamento='"+ConstantesBD.acronimoNo+"' " +
												"AND (sol.estado_historia_clinica="+ConstantesBD.codigoEstadoHCSolicitada+" OR sol.estado_historia_clinica="+ConstantesBD.codigoEstadoHCDespachada+" OR sol.estado_historia_clinica="+ConstantesBD.codigoEstadoHCAdministrada+" OR sol.estado_historia_clinica="+ConstantesBD.codigoEstadoHCCargoDirecto+" ) ";
												if(!mostrarFinalizados)
												{
													listadoInsumosGeneralStr+=" and getessolarticulofinalizada(ds.numero_solicitud, ds.articulo)='"+ConstantesBD.acronimoNo+"' " ;
												}
						listadoInsumosGeneralStr+=
											"UNION " +
											"SELECT " +
												"a.codigo AS codigo, " +
												"a.descripcion, " +
												"getnomunidadmedida(a.unidad_medida) AS unidadmedida, " +
												"CASE WHEN getInfoArticulos(sol.numero_solicitud,a.codigo) IS NULL OR getInfoArticulos(sol.numero_solicitud,a.codigo)='' THEN '0' ELSE getInfoArticulos(sol.numero_solicitud,a.codigo)||'' END as cantidaddespacho, " +
												"CASE WHEN getTotalAdminFarmacia(a.codigo,sol.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") IS NULL THEN 0 " +
													"ELSE getTotalAdminFarmacia(a.codigo,sol.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() + ") END AS consumofarmacia, " +
												"CASE WHEN getTotalAdminFarmacia(a.codigo,sol.numero_solicitud," + ValoresPorDefecto.getValorTrueParaConsultas() + ") IS NULL THEN 0 " +
													"ELSE getTotalAdminFarmacia(a.codigo,sol.numero_solicitud," + ValoresPorDefecto.getValorTrueParaConsultas() + ") END AS consumopaciente, " +
												//"CASE WHEN getvalortotalcargomedicamentos(sol.numero_solicitud,va.codigo) IS NULL THEN 0 " +
												//	"ELSE ( CASE WHEN getvalortotalcargomedicamentos(sol.numero_solicitud,va.codigo)*getTotalAdminFarmacia(va.codigo,sol.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() +") IS NULL THEN 0 ELSE getvalortotalcargomedicamentos(sol.numero_solicitud,va.codigo)*getTotalAdminFarmacia(va.codigo,sol.numero_solicitud," + ValoresPorDefecto.getValorFalseParaConsultas() +") END) END AS valorcargo, " +
												" 0	AS valorcargo, "+	
												"'"+ConstantesBD.acronimoNo+"' as traidopacienteinsumo, "+
												"'' as loteinsumo,  " +
												"'' as fechavencimientoloteinsumo, " +
												"'' as existenciaxloteinsumo, " +
												"'0' as consumo," +
												"coalesce(getExistenSolInsumo(getingresosolicitud(sol.numero_solicitud),a.codigo),0) AS existenordenesante, " +
												"(select coalesce(ds.cantidad,0) from detalle_solicitudes ds where ds.numero_solicitud=sol.numero_solicitud and ds.articulo=a.codigo) as cantidadsolicitada " +
											"from "+ 
												"solicitudes sol "+
												"inner join despacho desp on (sol.numero_solicitud=desp.numero_solicitud) "+ 
												"inner join detalle_despachos dd on (desp.orden=dd.despacho) "+ 
												"inner join articulo a ON(a.codigo=dd.articulo) "+
												"inner join naturaleza_articulo na ON (a.naturaleza=na.acronimo and na.institucion=a.institucion) " +
											"where " +
												"sol.numero_solicitud=? " +
												"and na.es_medicamento='"+ConstantesBD.acronimoNo+"' " +
												"AND (sol.estado_historia_clinica="+ConstantesBD.codigoEstadoHCSolicitada+" OR sol.estado_historia_clinica="+ConstantesBD.codigoEstadoHCDespachada+" OR sol.estado_historia_clinica="+ConstantesBD.codigoEstadoHCAdministrada+" OR sol.estado_historia_clinica="+ConstantesBD.codigoEstadoHCCargoDirecto+" ) ";
												if(!mostrarFinalizados)
												{
													listadoInsumosGeneralStr+=" and getessolarticulofinalizada(sol.numero_solicitud, a.codigo)='"+ConstantesBD.acronimoNo+"' " ;
												}
		
	    
	    	logger.info("\n\n\n\n*****LISTADO INSUMOS-->"+listadoInsumosGeneralStr+"\n\n\n\n");
	    	pst= new PreparedStatementDecorator(con.prepareStatement(listadoInsumosGeneralStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    pst.setInt(1,numeroSolicitud);
		    pst.setInt(2,numeroSolicitud);
		    rs=new ResultSetDecorator(pst.executeQuery());
		    map=UtilidadBD.cargarValueObject(rs);
	    }
		catch(Exception e){
			Log4JManager.error("ERROR listadoInsumos", e);
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
			catch (SQLException sql){
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
	    return map;
	}
	
	/**
	 * Metodo empleado para insertar los datos de una Administraci&oacute;n de
	 * Medicamentos.
	 * @param con, Connection con la fuente de datos.
	 * @param numeroSolicitud, codigo del n&uacute;mero de Solicitud.
	 * @param centroCosto, Centro de costo.
	 * @param usuario, Usuario
	 * @return int 1 efectivo, 0 de lo contrario.
	 */
	public static int insertarAdmin(Connection con,int numeroSolicitud, int centroCosto, String usuario)
	{
		PreparedStatementDecorator pst=null;
		int result=ConstantesBD.codigoNuncaValido;
		try
		{
			int codigo= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_admin_medicamentos"); 
			pst=  new PreparedStatementDecorator(con.prepareStatement(insertarAdminStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigo);
			pst.setInt(2,numeroSolicitud);
			pst.setInt(3,centroCosto);
			pst.setString(4,usuario);
			if(pst.executeUpdate()>0){
				result=codigo;
			}
		}
		catch(Exception e){
			Log4JManager.error("ERROR insertarAdmin", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
	   return result; 
	}
	
	/**
	 * Metodo para insertar los datos detalle administraci&oacute;n
	 * en la tabla de <code>detalle_admin</code>.
	 * @param con Connection conexion con la fuente de datos.
	 * @param unidadesDosisSaldos 
	 * @param articulo, codigo del articulo.
	 * @param administracion, codigo de la administraci&oacute;n al que pertenece el detalle.
	 * @param fecha, fecha 
	 * @param hora, hora
	 * @param cantidad, consumo realizado por parte de la enfermeria.
	 * @param observaciones, observaciones.
	 * @param esTraidoUsuario, boolean (true) traido por el usuario, false de lo contrario.
	 * @return int 1 efectivo, 0 de lo contrario.
	 */
	public static int insertarDetalleAdmin(	Connection con,int articulo, int artppal, int administracion, 
	        								String fecha, String hora, int cantidad, 
	        								String observaciones, String esTraidoUsuario,
	        								String lote, String fechaVencimiento, String unidadesDosisSaldos,
	        								String adelantoXNecesidad, String nadaViaOral, String usuarioRechazo,
	        								String validacionConcurrencia, String numeroDespachos)
	{
	    int resp=0;
	    //se deben tener en cuenta las dosis.
	    PreparedStatementDecorator pst=null;
	    PreparedStatementDecorator pst2=null;
	    PreparedStatementDecorator pst3=null;
	    ResultSetDecorator rs=null;
	    ResultSetDecorator rs2=null;
	    try
		{
	    	pst=  new PreparedStatementDecorator(con.prepareStatement(validacionConcurrencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    	logger.info("\n\n validacionConcurrencia-->"+validacionConcurrencia+"  art->"+articulo+" adm->"+administracion);
	    	
			pst.setInt(1, articulo);
			pst.setInt(2, administracion);
			rs=new ResultSetDecorator(pst.executeQuery());
			int numElementosAdmin=0;
			if(rs.next())
			{
				numElementosAdmin=rs.getInt("cantidad");
			}
			pst2=  new PreparedStatementDecorator(con.prepareStatement(numeroDespachos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("\n\n numeroDespachos-->"+numeroDespachos+" art->"+articulo+ " adm->"+administracion);
			pst2.setInt(1, articulo);
			pst2.setInt(2, administracion);
			rs2=new ResultSetDecorator(pst2.executeQuery());
			int numElementosDespacho=0;
			if(rs2.next())
			{
				numElementosDespacho=rs2.getInt("cantidad")+Utilidades.convertirAEntero(unidadesDosisSaldos,true);
			}
			
			//toca verificar que no sea traido paciente
			int cantidadTemporal=0;
			if(esTraidoUsuario.equals(ConstantesBD.acronimoNo))
				cantidadTemporal=cantidad;
			
			if(numElementosDespacho<(numElementosAdmin+cantidadTemporal))
			{
				//-1111 es un id que indica que no se ha despachado
				return -1111;
			}
			pst3=  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("\n\ninsertarDetalleStr->"+insertarDetalleStr+" art->"+articulo+" adm->"+administracion+" fecha->"+UtilidadFecha.conversionFormatoFechaABD(fecha)+" hora->"+hora+" cantidad->"+cantidad+" obs->"+observaciones+" esTraidoUsuario->"+esTraidoUsuario+" artppal->"+artppal+" adelant->"+adelantoXNecesidad+" nadaviaoral->"+nadaViaOral+" usu->"+usuarioRechazo);
			
			pst3.setInt(1,articulo);
			pst3.setInt(2,administracion);
			pst3.setString(3,UtilidadFecha.conversionFormatoFechaABD(fecha));
			pst3.setString(4,hora);
			pst3.setInt(5,cantidad);
			pst3.setString(6,observaciones);
			
			if(esTraidoUsuario.equals(ConstantesBD.acronimoNo))
				pst3.setString(7,ValoresPorDefecto.getValorFalseParaConsultas());
			else
				pst3.setString(7,ValoresPorDefecto.getValorTrueParaConsultas());
			
			if(UtilidadTexto.isEmpty(lote))
				pst3.setObject(8, null);
			else
				pst3.setString(8, lote);
			if(UtilidadTexto.isEmpty(lote) || UtilidadTexto.isEmpty(fechaVencimiento) || !UtilidadFecha.esFechaValidaSegunAp(fechaVencimiento))
			{
				pst3.setObject(9, null);
			}	
			else
				pst3.setDate(9, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaVencimiento)));
			if(artppal > 0)
			{
				pst3.setInt(10, artppal);
			}
			else
				pst3.setObject(10, null);
			
			logger.info("valor usuario rechazo: "+usuarioRechazo);
			if(!adelantoXNecesidad.equals(""))
				pst3.setString(11,adelantoXNecesidad);
			else
				pst3.setString(11,ConstantesBD.acronimoNo);
			
			if(!nadaViaOral.equals(""))
				pst3.setString(12,nadaViaOral);
			else
				pst3.setString(12,ConstantesBD.acronimoNo);
			
			if(!nadaViaOral.equals(""))
				pst3.setString(13,usuarioRechazo);
			else
				pst3.setString(13,ConstantesBD.acronimoNo);
			
			resp=pst3.executeUpdate();
		}
	    catch(Exception e){
			Log4JManager.error("ERROR insertarDetalleAdmin", e);
		}
		finally{
			try{
				if(rs2 != null){
					rs2.close();
				}
				if(rs != null){
					rs.close();
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resp;
	}
	
	
	/**
	 * Cargar la ultima administraci&oacute; insertada   (table= admin_medicamentos))
	 * @param con Connection con la fuente de datos
	 * @return int ultimoCodigoSequence, 0 no efectivo.
	 */
	public static int obtenerNumeroDosisAdministradas(Connection con, int numeroSolicitud, int codigoArticulo)
	{	
		PreparedStatement pst=null;
		ResultSet rs=null;
		int result=0;
		try
		{
			String cadena="SELECT (nro_dosis_admin_far + nro_dosis_admin_pac) as numerodosis FROM acumulado_admin_dosis WHERE articulo="+codigoArticulo+" AND numero_solicitud="+numeroSolicitud;
			pst= con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			if(rs.next())
				result= rs.getInt("numerodosis");
		}
		catch(Exception e){
			Log4JManager.error("ERROR obtenerNumeroDosisAdministradas", e);
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return result;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param acumuladoAdminDosisMap
	 * @return
	 */
	private static boolean existeAcumuladoAdminDosis(Connection con, int articulo, int numeroSolicitud)
	{
		boolean resultado=false;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			pst= con.prepareStatement(existeAcumuladoAdminDosisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, articulo);
			pst.setInt(2, numeroSolicitud);
			rs=pst.executeQuery();
			if(rs.next())
				resultado=true;
		}
		catch(Exception e){
			Log4JManager.error("ERROR existeAcumuladoAdminDosis", e);
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultado;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param acumuladoAdminDosisMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean insertarActualizarAcumuladoAdminDosis(Connection con, HashMap acumuladoAdminDosisMap)
	{
		boolean resultado=true;
		try
		{
			for(int w=0; w<Integer.parseInt(acumuladoAdminDosisMap.get("numRegistros").toString()); w++)
			{
				//primero se evalua si no existe, en cuyo caso solamante se inserta
				if(!existeAcumuladoAdminDosis(con, Integer.parseInt(acumuladoAdminDosisMap.get("articulo_"+w).toString()), Integer.parseInt(acumuladoAdminDosisMap.get("numeroSolicitud_"+w).toString())))
				{
					PreparedStatement pst= new PreparedStatementDecorator(con.prepareStatement(insertarAcumuladoAdminDosisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1, Integer.parseInt(acumuladoAdminDosisMap.get("articulo_"+w).toString()));
					pst.setInt(2, Integer.parseInt(acumuladoAdminDosisMap.get("numeroSolicitud_"+w).toString()));
					pst.setInt(3, Integer.parseInt(acumuladoAdminDosisMap.get("incrementoNumeroDosisAdminFar_"+w).toString()));
					pst.setInt(4, Integer.parseInt(acumuladoAdminDosisMap.get("incrementoNumeroDosisAdminPac_"+w).toString()));
					pst.setInt(5, Integer.parseInt(acumuladoAdminDosisMap.get("incrementoUnidadesAdminFarmacia_"+w).toString()));
					pst.setInt(6, Integer.parseInt(acumuladoAdminDosisMap.get("incrementoUnidadesAdminPaciente_"+w).toString()));
					int r=pst.executeUpdate();
					pst.close();
					if(r<0){
						resultado=false;
						break;
					}
				}
				// de lo contrario se actualiza
				else
				{
					PreparedStatement pst= new PreparedStatementDecorator(con.prepareStatement(updateAcumuladoAdminDosisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1, Integer.parseInt(acumuladoAdminDosisMap.get("incrementoNumeroDosisAdminFar_"+w).toString()));
					pst.setInt(2, Integer.parseInt(acumuladoAdminDosisMap.get("incrementoNumeroDosisAdminPac_"+w).toString()));
					pst.setInt(3, Integer.parseInt(acumuladoAdminDosisMap.get("incrementoUnidadesAdminFarmacia_"+w).toString()));
					pst.setInt(4, Integer.parseInt(acumuladoAdminDosisMap.get("incrementoUnidadesAdminPaciente_"+w).toString()));
					pst.setInt(5, Integer.parseInt(acumuladoAdminDosisMap.get("articulo_"+w).toString()));
					pst.setInt(6, Integer.parseInt(acumuladoAdminDosisMap.get("numeroSolicitud_"+w).toString()));
					int r=pst.executeUpdate();
					pst.close();
					if(r<0){
						resultado=false;
						break;
					}
				}
			}	
		}
		catch(SQLException e)
		{
			logger.error("ERROR insertarActualizarAcumuladoAdminDosis",e);
		}
		return resultado;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param loginUsuario
	 * @return
	 */
	public static boolean insertarFinalizacionAdminArticulo(Connection con, int numeroSolicitud, int articulo, String loginUsuario)
	{
		PreparedStatement pst=null;
		boolean resultado=false;
		try
		{
			pst= con.prepareStatement(insertarFinalizacionAdminArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, numeroSolicitud);
			pst.setInt(2, articulo);
			pst.setString(3, loginUsuario);
			if(pst.executeUpdate()>0)
				resultado=true;
		}
		catch(Exception e){
			Log4JManager.error("ERROR insertarFinalizacionAdminArticulo", e);
		}
		finally{
			try{
				if(pst != null){
					pst.close();
				}
			}
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultado;
	}
	
	
	/**
	 * Carga el resumen de la administración de medicamentos para un número de solicitud
	 * específico, cuando parte1=boolean entonces carga la info básica (descripción) de los articulos, 
	 * de lo contrario carga la info de la admin de los articulos
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroSolicitud, int, numeroSolicitud
	 * @param parte1, boolean, indica el segmento a consultar
	 * @return ResulSet list
	 */
	public static ResultSetDecorator resumenAdmiMedicamentos(Connection con, int numeroSolicitud, boolean parte1)
	{
		try
		{
			if(parte1)
			{	
				PreparedStatementDecorator resumen1Statement= new PreparedStatementDecorator(con.prepareStatement(resumenAdminMedDescArticuloStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				resumen1Statement.setInt(1,numeroSolicitud);
				resumen1Statement.setInt(2,numeroSolicitud);
				resumen1Statement.setInt(3,numeroSolicitud);
                resumen1Statement.setInt(4,numeroSolicitud);
                logger.info("SQL / "+resumenAdminMedDescArticuloStr.replace("?", numeroSolicitud+""));
				return new ResultSetDecorator(resumen1Statement.executeQuery());
			}
			else
			{
				PreparedStatementDecorator resumen2Statement= new PreparedStatementDecorator(con.prepareStatement(resumenAdminMedDetalleStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				resumen2Statement.setInt(1,numeroSolicitud);
				logger.info("SQL / "+resumenAdminMedDetalleStr.replace("?", numeroSolicitud+""));
				return new ResultSetDecorator(resumen2Statement.executeQuery());
			}
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la consulta del resumen de admin: SqlBaseAdminMedicamentosDao "+e.toString());
			e.printStackTrace();
			return null;
		}
	}
	
	
	/**
	 * Método para conocer la fecha y la hora de la última administración 
	 * @param con Conección con la BD
	 * @param numeroSolicitud Solicitud de la cual se quiere saber la última administración
	 * @return fecha y hora de la última administración
	 */
	public static String fechaUtlimaAdministracion(Connection con, int numeroSolicitud)
	{
		String resultado=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			pst= con.prepareStatement(fechaHoraUtlimaAdministracion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.setInt(1, numeroSolicitud);
			rs=pst.executeQuery();
			if(rs.next())
			{
				resultado=rs.getString("fecha");
			}
		}
		catch(Exception e){
			Log4JManager.error("ERROR fechaUtlimaAdministracion", e);
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultado;
	}
	
	
	/**
	 * Método para consultar los medicamentos despachados para las
	 * diferentes solicitudes que pueda tener un paciente
	 * @param con
	 * @param codigoCuenta
	 * @param institucion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Collection consultaMedicamentosDespachadosPaciente(Connection con, int codigoCuenta, Vector solicitudes)
	{
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		Collection resultado=null;
		try
	    {
	    
	     
	    //----Si codigoCuenta es diferente de -1 entonces se consultan los medicamentos solicitados al paciente --//
	    if (codigoCuenta != -1)
		    {
		    pst= new PreparedStatementDecorator(con.prepareStatement(listadoMedicamentosDespachados,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    pst.setInt(1,codigoCuenta);
		    }
	    //----Si centroCosto es diferente de -1 entonces se consultan los medicamentos solicitados por centro de costo --//
	    else
		    {
	    	
	    		String consulta=listadoMedicamentosCentroCosto;
	    		if(solicitudes.size()==0)
	    		{
	    			consulta+="-1";
	    		}
	    		else
	    		{
		    		for(int i=0; i<solicitudes.size();i++)
		    		{
		    			if(i!=0)
		    			{
		    				consulta+=",";
		    			}
		    			consulta+=solicitudes.elementAt(i);
		    		}
	    			
	    		}
	    		consulta+=") ORDER BY articulos.articulo";
	    		pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    }//else
	    	rs=new ResultSetDecorator(pst.executeQuery());
	    	resultado= UtilidadBD.resultSet2Collection(rs);			
	    }
		catch(Exception e){
			Log4JManager.error("ERROR consultaMedicamentosDespachadosPaciente", e);
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
	    return resultado;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static ArrayList<DtoAdministracionMedicamentosBasico> cargarResumenAdministracion(Connection con, int numeroSolicitud) 
	{
		String consulta="(SELECT a.codigo            AS codigo, "+
							  "getdescarticulo(a.codigo) AS descarticulo, "+
							  "CASE "+
							    "WHEN getTotalAdminFarmacia(a.codigo,ds.numero_solicitud,"+ValoresPorDefecto.getValorTrueParaConsultas()+") IS NULL "+
							    "THEN 0 "+
							    "ELSE getTotalAdminFarmacia(a.codigo,ds.numero_solicitud,"+ValoresPorDefecto.getValorTrueParaConsultas()+") "+
							  "END AS unidadesconsumidasxpaciente, "+
							  "CASE "+
							    "WHEN getTotalAdminFarmacia(a.codigo,ds.numero_solicitud,"+ValoresPorDefecto.getValorFalseParaConsultas()+") IS NULL "+
							    "THEN 0 "+
							    "ELSE getTotalAdminFarmacia(a.codigo,ds.numero_solicitud,"+ValoresPorDefecto.getValorFalseParaConsultas()+") "+
							  "END                                                                AS unidadesconsumidasxfarmacia, "+
							  "enfermeria.getfechahoraultadmin(ds.numero_solicitud,a.codigo)      AS fechahoraultimaadmin, "+
							  "enfermeria.getfechorusiregisultadmin(ds.numero_solicitud,a.codigo) AS fechahorausureg, "+
							  "enfermeria.getobservultimaadmin(ds.numero_solicitud,a.codigo) AS obsultima, "+
							  "na.es_medicamento esmedicamento "+
							"FROM despacho desp "+
							"INNER JOIN detalle_despachos detd "+
							"ON (detd.despacho=desp.orden) "+
							"INNER JOIN detalle_solicitudes ds "+
							"ON (ds.articulo        =detd.art_principal "+
							"AND ds.numero_solicitud= desp.numero_solicitud) "+
							"INNER JOIN articulo a "+
							"ON (a.codigo=detd.articulo ) "+
							"INNER JOIN naturaleza_articulo na "+
							"ON(na.acronimo           =a.naturaleza) "+
							"WHERE ds.numero_solicitud=? "+
							") ";
		ArrayList<DtoAdministracionMedicamentosBasico> resultado=new ArrayList<DtoAdministracionMedicamentosBasico>();
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		try
		{
			pst=new PreparedStatementDecorator(con,consulta);
			pst.setInt(1, numeroSolicitud);
			//ps.setInt(2, numeroSolicitud);
			rs=new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoAdministracionMedicamentosBasico dto=new DtoAdministracionMedicamentosBasico();
				dto.setCodigoArticulo(rs.getInt("codigo"));
				dto.setDescripcionArticulo(rs.getString("descarticulo"));
				dto.setUnidadesConsumidasXPaciente(rs.getInt("unidadesconsumidasxpaciente"));
				dto.setUnidadesConsumidasXFarmacia(rs.getInt("unidadesconsumidasxfarmacia"));
				dto.setFechaHoraUltimaAdministracion(rs.getString("fechahoraultimaadmin"));
				dto.setFechaHoraUsuarioUltimoRegAdministracion(rs.getString("fechahorausureg"));
				dto.setObservacionesUltimaAdministracion(rs.getString("obsultima"));
				dto.setEsMedicamento(rs.getString("esmedicamento"));
				resultado.add(dto);
			}
		}
		catch(Exception e){
			Log4JManager.error("ERROR cargarResumenAdministracion", e);
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultado;
	}
	
	
	/**
	 * Método que consulta el color del triage
	 * @param con
	 * @param cuentaAdmisionUrgencias
	 * @return
	 */
	public static String consultarColorTriage(Connection con, int cuentaAdmisionUrgencias) 
	{
		String consulta="select distinct color.nombre as color " +
				"from cuentas cue " +
				"INNER JOIN pacientes pa ON (pa.codigo_paciente=cue.codigo_paciente) " +
				"INNER JOIN personas per ON (per.codigo = pa.codigo_paciente) " +
				"LEFT JOIN pacientes_triage pt ON (pt.codigo_paciente=pa.codigo_paciente) " +
				"LEFT JOIN triage tri ON ((tri.consecutivo=pt.consecutivo_triage AND tri.consecutivo_fecha =pt.consecutivo_fecha_triage) " +
					"OR (per.numero_identificacion=tri.numero_identificacion AND per.tipo_identificacion  =tri.tipo_identificacion)) " +
				"INNER join admisiones_urgencias au ON (au.codigo = tri.numero_admision AND au.anio= tri.anio_admision ) " +
				"LEFT JOIN categorias_triage ct ON (ct.consecutivo=tri.categoria_triage) " +
				"LEFT JOIN colores_triage color ON (color.codigo  =ct.color) " +
				"where " +
				"au.cuenta=? ";
		
		String resultado=new String();
		PreparedStatementDecorator pst=null;
		ResultSetDecorator rs=null;
		try
		{	logger.info("\n\nConsultaColorTriage:\n"+consulta+"-->"+cuentaAdmisionUrgencias);
			pst=new PreparedStatementDecorator(con,consulta);
			pst.setInt(1, cuentaAdmisionUrgencias);
			rs=new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				resultado=rs.getString("color");
			}
		}
		catch(Exception e){
			Log4JManager.error("ERROR consultarColorTriage", e);
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
			catch (SQLException sql) {
				Log4JManager.error("ERROR cerrando objetos persistentes", sql);
			}
		}
		return resultado;
	}
	
}
