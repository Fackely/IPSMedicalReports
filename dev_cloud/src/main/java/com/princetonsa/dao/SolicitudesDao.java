/*
* @(#)PostgresqlSolicitudesDao.java
*
* Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.servinte.axioma.fwk.exception.BDException;

/**
* Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta
* el servicio de acceso a datos para el objeto <code>Solicitudes</code>.
*
* @version 1.1, Feb 12, 2004
* @author <a href="mailto:edgar@princetonsa.com">Edgar Prieto</a>
* @author <a href="mailto:raul@princetonsa.com">Raúl Cancino</a>
*/
public interface SolicitudesDao
{
	/**
	* Obtiene un listado de solicitudes de acuerdo a los parámatros especificados en el objeto
	* Solicitudes
	 * @param centroCostoSolicitanteFiltro 
	 * @param fechaFinalFiltro 
	 * @param fechaInicialFiltro 
	 * @param tipoOrdenFiltro 
	 * @param estadoHCFiltro 
	*
	* @return <code>Collection<code> con las solicitudes que coinciden con los parámetros especificados.
	*/
	public Collection listarSolicitudes(
		Connection	ac_con,
		int			ai_codigoCentroCostoSolicitado,
		int			ai_codigoCentroCostoSolicitante,
		int			ai_codigoCentroCostoTratante,
		int			ai_codigoMedico,
		int			ai_codigoOcupacionSolicitada,
		int			ai_codigoPaciente,
		int			ai_codigoTipoSolicitud,
		int			ai_estadoCuenta,
		int			ai_estadoHistoriaClinica,
		int[]		aia_codigoEspecialidadSolicitada,
		int			codigoCuentaAsociada,
		int			codigoCentroCostoIntentaAcceso,
		int codigoCuenta,
		int isntitucion,
		boolean resumenAtenciones,
		int codigoCentroAtencionCuenta, String fechaInicialFiltro, String fechaFinalFiltro, String centroCostoSolicitanteFiltro, String estadoHCFiltro, String tipoOrdenFiltro,
		String areaFiltro,String centroCostoSolicitadoFiltro,String pisoFiltro,String habitacionFiltro,String camaFiltro, boolean requierePortatilFiltro, String codigoMedicoEnSesion
	)throws SQLException;
	
	/**
	 * adición de Sebastián
	 * Método que obtiene la lista de solicitudes de acuerdo a los parámetros
	 * especificados en RevisionCuenta del módulo facturación
	 * @param con
	 * @param idCuenta
	 * @param contrato
	 * @param opcion: si es true se refiere a una cuenta, si es false se refiere a una subcuenta
	 * @return
	 */
	public HashMap listarSolicitudes(Connection con,int idCuenta,int contrato,boolean opcion);
	/**
	 * adición de Sebastián
	 * Método que obtiene los campos de la solicitud que es de servicios
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public Collection listarSolicitudesServicios(Connection con,int solicitud);
	/**
	 * adición de Sebastián
	 * Método que obtiene los campos de la solicitud que es de medicamentos
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public Collection listarSolicitudesMedicamentos(Connection con,int solicitud);
	/**
	 * adición de Sebastián
	 * Método que inserta un pool de medico por solicitud en el caso de que el médico pertenezca
	 * a varios pooles
	 * @param con
	 * @param solicitud
	 * @param pool
	 * @return
	 */
	public int insertarPoolMedicoXSolicitud(Connection con,int solicitud,int pool);
	
	/**
	 * Adición Sebastián
	 * Método usado en la revisión de la cuenta para la búsqueda
	 * avanzada de solicitudes
	 * @param con
	 * @param orden
	 * @param tipo
	 * @param fecha
	 * @param estado
	 * @param contrato
	 * @param opcion
	 * @return
	 */
	public HashMap busquedaRevisionSolicitudes(
			Connection con,int idCuenta,int orden,int tipo,String fecha,
			int estado,int contrato,boolean opcion);
	
	/**
	 * Método implementado para insertar el Log de modificacion de tarifas
	 * en la funcionalidad de Revisión de la Cuenta
	 * @param con
	 * @param cuenta
	 * @param solicitud
	 * @param servicio
	 * @param articulo
	 * @param tipoCargo
	 * @param tarifaInicial
	 * @param tarifaFinal
	 * @param usuario
	 * @param institucion
	 * @param estado
	 * @return
	 */
	public int insertarLogRevisionCuenta(Connection con,int cuenta,int solicitud,int servicio,int articulo,
			int tipoCargo,double tarifaInicial,double tarifaFinal,String usuario,int institucion,String estado);

	/**
	 * Mètodo que consulta los procedimientos e interconsultas asociados al paciente, para
	 * mostrarlos en la ventana al ubicarse el usuario sobre la flecha de detalle en el listado de 
	 * solicitudes  de interconsultas y procedimientos
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public Collection consultarInterconsultasProcedimientos(Connection con, int codigoCuenta);

	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @param codigoContrato
	 */
	public HashMap obtenerSolicitudesServicioIndicadorCapitado(Connection con, String idCuenta, int codigoContrato);

	/**
	 * 
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public int obtenerViaIngresoSolicitud(Connection con, int solicitud) throws BDException;
	
	
	/**
	 * 
	 * @param numeroSolicitud
	 * @param estado
	 * @param con
	 * @return
	 */
	public  boolean  modificarEstadosOrdenesSolicitud(int numeroSolicitud, int estado, Connection con);
}
