/*
 * @(#)OracleAdminMedicamentosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.enfermeria.DtoAdministracionMedicamentosBasico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import util.ConstantesBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.AdminMedicamentosDao;
import com.princetonsa.dao.sqlbase.SqlBaseAdminMedicamentosDao;

/**
 * Implementación Oracle de las funciones de acceso a la fuente de datos
 * para un la admin de medicmentos
 *
 * @version 1.0, Sept. 16 / 2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan López</a>
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class OracleAdminMedicamentosDao implements AdminMedicamentosDao 
{
	/**
	 *  Información de la creación de la solicitud
	 */
	private final static String encabezadoSolicitudMedicamentosStr = 	"SELECT " +
																	    "TO_CHAR(sol.fecha_solicitud,'yyyy-mm-dd') AS fechaSolicitud, " +
																	    "sol.hora_solicitud horaSolicitud, " + // No poner to_char
																	    "sol.numero_solicitud AS numeroSolicitud, " +
																	    "sol.consecutivo_ordenes_medicas AS orden, " +
																	    "per.primer_apellido ||' '|| " +
																	    "per.segundo_apellido ||' '|| " +
																	    "per.primer_nombre ||' '|| " +
																	    "per.segundo_nombre AS medicoSolicitante, " +
																	    //"sol.numero_autorizacion AS numeroAutorizacion, " +
																	    "sol.estado_historia_clinica as codigoestadomedico, " +
																	    "est.nombre AS estadoMedico, " +
																	    "sm.observaciones_generales AS observacionesGenerales, " +
																	    "sol.centro_costo_solicitado AS farmaciaDespacho "+
																	    "FROM solicitudes sol INNER JOIN cuentas cue ON (sol.cuenta=cue.id) " +
																	    "INNER JOIN personas per ON (sol.codigo_medico = per.codigo) " +
																	    "INNER JOIN estados_sol_his_cli est ON (est.codigo=sol.estado_historia_clinica) " +
																	    "INNER JOIN solicitudes_medicamentos sm ON (sm.numero_solicitud = sol.numero_solicitud) " +
																	    "WHERE sol.numero_solicitud = ? ";


	/**
	 * Sección SELECT para el listado de las solicitudes de medicamentos por área
	 */
	private final static String listadoSolMedXAreaSELECT_Str = 		"SELECT " +
																	"distinct "+
																	"CASE WHEN sol.urgente= "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN  'urgente' ELSE 'NO' END  AS identificadorPrioridad, "+
																	"TO_CHAR(sol.fecha_solicitud,'yyyy-mm-dd') AS fechaSolicitud, "+
																	"sol.hora_solicitud AS horaSolicitud, "+ // No poner to_char
																	"sol.numero_solicitud AS numeroSolicitud, " +
																	"sol.consecutivo_ordenes_medicas AS orden, "+
																	"eshc.nombre AS estadoMedico, " +
																	"getnombrepersona(cue.codigo_paciente) AS nombresPaciente, " +
																	"cue.codigo_paciente AS codigoPaciente, "+ 
																	"getnombrepersona(sol.codigo_medico) AS medicoSolicitante, " +
																	"getnomcentrocosto(sol.centro_costo_solicitante) AS centroCostoSolicitante, " +
																	"getnomcentrocosto(sol.centro_costo_solicitado) AS farmaciaDespacho, "+
																	"cue.id AS codigoCuenta, " +
																	"CASE WHEN sol.pyp IS NULL THEN '' ELSE (CASE WHEN sol.pyp ="+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'Si' ELSE '' END) END AS esPyP, " +
																	"coalesce(getcamacuenta(cue.id, cue.via_ingreso), '') as infocama, " +
																	"via.nombre as viaIngreso " +
																	"FROM solicitudes sol   "+
																	"INNER JOIN solicitudes_medicamentos sm ON (sm.numero_solicitud=sol.numero_solicitud) "+
																	"INNER JOIN cuentas cue ON (sol.cuenta=cue.id) "+
																	"INNER JOIN estados_sol_his_cli eshc ON (sol.estado_historia_clinica=eshc.codigo) " +
																	"LEFT OUTER  JOIN despacho des ON (sol.numero_solicitud= des.numero_solicitud AND des.es_directo<>"+ValoresPorDefecto.getValorTrueParaConsultas()+") " +
																	"INNER JOIN vias_ingreso via ON (via.codigo=cue.via_ingreso) ";

	/**
	 * Secciñon WHERE para el listado de las solicitudes de medicamentos por área
	 */
	private final static String listadoSolMedXAreaWHERE_Str = 	" WHERE   "+
																"(cue.estado_cuenta in ( "+ConstantesBD.codigoEstadoCuentaActiva+", "+ConstantesBD.codigoEstadoCuentaFacturadaParcial+") or (cue.estado_cuenta="+ConstantesBD.codigoEstadoCuentaAsociada+" AND getcuentafinal(cue.id) IS NULL)) " +
																"AND sol.tipo="+ConstantesBD.codigoTipoSolicitudMedicamentos+"  " +
																"AND sol.centro_costo_solicitado <>"+ConstantesBD.codigoCentroCostoExternos+" "+
																"AND (sol.estado_historia_clinica= "+ConstantesBD.codigoEstadoHCSolicitada+" OR sol.estado_historia_clinica="+ConstantesBD.codigoEstadoHCDespachada+")  "+
																"AND sm.orden_dieta IS NULL ";

	/**
	 *  Listado de las solicitudes  de medicamentos por área
	 */
	private final static String listadoSolMedXAreaStr= listadoSolMedXAreaSELECT_Str + listadoSolMedXAreaWHERE_Str;


	/**
	 * Carga el Listado de las solicitudes de medicamentos para un paciente determinado
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigoPaciente, int, codigo del paciente
	 * @return ResulSet list
	 */
	@SuppressWarnings("rawtypes")
	public Collection listadoSolMedXPaciente(Connection con, int codigoPaciente,int institucion)
	{
		return SqlBaseAdminMedicamentosDao.listadoSolMedXPaciente(con,codigoPaciente,institucion);
	}
	
	/**
	 * Carga el Listado de las solicitudes de medicamentos para un AREA determinada
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param centroCosto, int, centroCosto
	 * @return ResulSet list
	 */
	@SuppressWarnings("rawtypes")
	public Collection listadoSolMedXArea(Connection con, HashMap<String, Object> criteriosBusquedaMap)
	{
		return SqlBaseAdminMedicamentosDao.listadoSolMedXArea(con, criteriosBusquedaMap, listadoSolMedXAreaStr);
	}
	
	/**
	 * Carga la Información de la creación de la solicitud 
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroSolicitud, int, numero de la solicitud 
	 * @return ResulSet list
	 */
	public  ResultSetDecorator encabezadoSolicitudMedicamentos(Connection con, int numeroSolicitud)
	{
		return SqlBaseAdminMedicamentosDao.encabezadoSolicitudMedicamentos(con,numeroSolicitud, encabezadoSolicitudMedicamentosStr);
	}
	
	/**
	 * Carga el Listado de las solicitudes de medicamentos para un numeroSolicitud determinado
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroSolicitud
	 * @param codigoInstitucion Código de la institución del ususario
	 * @return ResulSet list
	 */
	@SuppressWarnings("rawtypes")
	public HashMap listadoMedicamentos(Connection con, int numeroSolicitud, int codigoInstitucion, boolean mostrarFinalizados)
	{
		return SqlBaseAdminMedicamentosDao.listadoMedicamentos(con, numeroSolicitud, codigoInstitucion, mostrarFinalizados);
	}
	
	/**
	 * Metodo que obtiene todos los registros de insumos.
	 * 
	 * @param con Connection conexion con la fuente de datos.
	 * @param numeroSolicitud,listadoInsumos
	 *                  codigo del n&uacute;mero de la solicitud.
	 * @return Collection.
	 */
	@SuppressWarnings("rawtypes")
	public HashMap listadoInsumos(Connection con, int numeroSolicitud, boolean mostrarFinalizados)
	{
		return SqlBaseAdminMedicamentosDao.listadoInsumos(con, numeroSolicitud, mostrarFinalizados);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @return
	 */
	public int obtenerNumeroDosisAdministradas(Connection con, int numeroSolicitud, int codigoArticulo)
	{
		return SqlBaseAdminMedicamentosDao.obtenerNumeroDosisAdministradas(con, numeroSolicitud, codigoArticulo);
	}
	
	
	/**
	 * Metodo empleado para insertar los datos de una Administraci&oacute;n de
	 * Medicamentos.
	 * @param con, Connection con la fuente de datos.
	 * @param numeroSolicitud, codigo del n&uacute;mero de Solicitud.
	 * @param centroCosto, Centro de costo.
	 * @param usuario, Usuario
	 * @return int 
	 */
	public int insertarAdmin(Connection con,int numeroSolicitud, int centroCosto, String usuario)
	{
	    return SqlBaseAdminMedicamentosDao.insertarAdmin(con,numeroSolicitud,centroCosto,usuario);
	}
	
	/**
	 * Metodo para insertar los datos detalle administraci&oacute;n
	 * en la tabla de <code>detalle_admin</code>.
	 * @param con Connection conexion con la fuente de datos.
	 * @param articulo, codigo del articulo.
	 * @param administracion, codigo de la administraci&oacute;n al que pertenece el detalle.
	 * @param fecha, fecha 
	 * @param hora, hora
	 * @param cantidad, consumo realizado por parte de la enfermeria.
	 * @param observaciones, observaciones.
	 * @param esTraidoUsuario, boolean (true) traido por el usuario, false de lo contrario.
	 * @return int 1 efectivo, 0 de lo contrario.
	 */
	public int insertarDetalleAdmin(Connection con,
			int articulo, int artppal, int administracion, 
			String fecha, String hora, int cantidad, 
			String observaciones, String esTraidoUsuario,
			String lote, String fechaVencimiento, String unidadesDosisSaldos, 
			String adelantoXNecesidad, String nadaViaOral, String usuarioRechazo)
	{
		String validacionConcurrencia="" +
		"SELECT " +
			"getTotalAdminNoTraidoUser(?, " +
				"(" +
					"SELECT numero_solicitud FROM admin_medicamentos am WHERE am.codigo=?" +
				")" +
			") AS cantidad from dual ";
		
		String numeroDespachos="SELECT " +
		"getdespacho(?, " +
			"(SELECT numero_solicitud FROM admin_medicamentos am WHERE am.codigo=?)) AS cantidad from dual ";
		
	    return SqlBaseAdminMedicamentosDao.insertarDetalleAdmin(con,articulo,artppal,administracion,fecha,hora,cantidad,observaciones,esTraidoUsuario, lote, fechaVencimiento,unidadesDosisSaldos, adelantoXNecesidad, nadaViaOral, usuarioRechazo, validacionConcurrencia, numeroDespachos);
	}
	
	/**
	 * 
	 * @param con
	 * @param acumuladoAdminDosisMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean insertarActualizarAcumuladoAdminDosis(Connection con, HashMap acumuladoAdminDosisMap)
	{
		return SqlBaseAdminMedicamentosDao.insertarActualizarAcumuladoAdminDosis(con, acumuladoAdminDosisMap);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param loginUsuario
	 * @return
	 */
	public boolean insertarFinalizacionAdminArticulo(Connection con, int numeroSolicitud, int articulo, String loginUsuario)
	{
		return SqlBaseAdminMedicamentosDao.insertarFinalizacionAdminArticulo(con, numeroSolicitud, articulo, loginUsuario);
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
	public  ResultSetDecorator resumenAdmiMedicamentos(Connection con, int numeroSolicitud, boolean parte1)
	{
		return SqlBaseAdminMedicamentosDao.resumenAdmiMedicamentos(con, numeroSolicitud, parte1);
	}
	
	/**
	 * Método para conocer la fecha y la hora de la última administración 
	 * @param con Conección con la BD
	 * @param numeroSolicitud Solicitud de la cual se quiere saber la última administración
	 * @return fecha y hora de la última administración
	 */
	public String fechaUtlimaAdministracion(Connection con, int numeroSolicitud)
	{
		return SqlBaseAdminMedicamentosDao.fechaUtlimaAdministracion(con, numeroSolicitud);
	}
	
	/**
	 * Método para consultar los medicamentos despachados para las
	 * diferentes solicitudes que pueda tener un paciente
	 * @param con
	 * @param codigoCuenta
	 * @param nroSolicitudes
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Collection consultaMedicamentosDespachadosPaciente(Connection con, int codigoCuenta, Vector nroSolicitudes)
	{
		return SqlBaseAdminMedicamentosDao.consultaMedicamentosDespachadosPaciente (con, codigoCuenta, nroSolicitudes);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public ArrayList<DtoAdministracionMedicamentosBasico> cargarResumenAdministracion(Connection con, int numeroSolicitud)
	{
		return SqlBaseAdminMedicamentosDao.cargarResumenAdministracion(con,numeroSolicitud);
	}
	
	/**
	 * Método que consulta el color del triage
	 * @param con
	 * @param cuentaAdmisionUrgencias
	 * @return
	 */
	public String consultarColorTriage(Connection con,int cuentaAdmisionUrgencias){
		return SqlBaseAdminMedicamentosDao.consultarColorTriage(con,cuentaAdmisionUrgencias);
	}

}
