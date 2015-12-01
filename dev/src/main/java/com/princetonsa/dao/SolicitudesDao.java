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
* @author <a href="mailto:raul@princetonsa.com">Ra�l Cancino</a>
*/
public interface SolicitudesDao
{
	/**
	* Obtiene un listado de solicitudes de acuerdo a los par�matros especificados en el objeto
	* Solicitudes
	 * @param centroCostoSolicitanteFiltro 
	 * @param fechaFinalFiltro 
	 * @param fechaInicialFiltro 
	 * @param tipoOrdenFiltro 
	 * @param estadoHCFiltro 
	*
	* @return <code>Collection<code> con las solicitudes que coinciden con los par�metros especificados.
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
	 * adici�n de Sebasti�n
	 * M�todo que obtiene la lista de solicitudes de acuerdo a los par�metros
	 * especificados en RevisionCuenta del m�dulo facturaci�n
	 * @param con
	 * @param idCuenta
	 * @param contrato
	 * @param opcion: si es true se refiere a una cuenta, si es false se refiere a una subcuenta
	 * @return
	 */
	public HashMap listarSolicitudes(Connection con,int idCuenta,int contrato,boolean opcion);
	/**
	 * adici�n de Sebasti�n
	 * M�todo que obtiene los campos de la solicitud que es de servicios
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public Collection listarSolicitudesServicios(Connection con,int solicitud);
	/**
	 * adici�n de Sebasti�n
	 * M�todo que obtiene los campos de la solicitud que es de medicamentos
	 * @param con
	 * @param solicitud
	 * @return
	 */
	public Collection listarSolicitudesMedicamentos(Connection con,int solicitud);
	/**
	 * adici�n de Sebasti�n
	 * M�todo que inserta un pool de medico por solicitud en el caso de que el m�dico pertenezca
	 * a varios pooles
	 * @param con
	 * @param solicitud
	 * @param pool
	 * @return
	 */
	public int insertarPoolMedicoXSolicitud(Connection con,int solicitud,int pool);
	
	/**
	 * Adici�n Sebasti�n
	 * M�todo usado en la revisi�n de la cuenta para la b�squeda
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
	 * M�todo implementado para insertar el Log de modificacion de tarifas
	 * en la funcionalidad de Revisi�n de la Cuenta
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
	 * M�todo que consulta los procedimientos e interconsultas asociados al paciente, para
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
