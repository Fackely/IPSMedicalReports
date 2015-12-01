/*
* @(#)SolicitudProcedimientoDao.java
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

import com.princetonsa.dto.ordenes.DtoProcedimiento;


/**
* Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta
* el servicio de acceso a datos para el objeto <code>SolicitudProcedimiento</code>
*
* @version 1.0, Mar 10, 2004
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*/
public interface SolicitudProcedimientoDao
{
	/**
	* Carga los datos de una solicitud de procedimientos desde una fuente de datos
	* @param ac_con				Conexión a la fuente de datos
	* @param ai_numeroSolicitud	Número de la solicitud de procedimiento a cargar
	*/
	public HashMap cargar(Connection ac_con, int ai_numeroSolicitud,String tarifario)throws SQLException;

	/**
	* Inserta una solicitud de procedimiento en una fuente de datos
	* @param ac_con				Conexión a la fuente de datos
	 * @param as_estado			Estado de la transacción
	 * @param ai_numeroSolicitud	Número de la solicitud de procedimiento a asignar. Este número de
	*							existir en la tabla de solicitudes
	 * @param ai_codigoServicio	Código del servicio de la solicitud de procedimientos
	 * @param as_nombreOtros		Nombre del servicio (si el código de servicio no está parametrizado en
	*							la aplicación) de la solicitud de procedimientos
	 * @param as_comentario		Comentario de la solicitud del procedimiento
	 * @param multiple			Solicitud Múltiple
	 * @param frecuencia @todo
	 * @param tipoFrecuencia @todo
	 * @param finalidad
	 * @param finalizadaRespuesta 
	 * @param respuestaMultiple 
	 * @param idCuenta @todo
	 * @param finalizar @todo
	 * @return Número de solicitudes insertadas correctamente
	* @throws java.sql.SQLException si se presentó un error de base de datos
	*/
	public int insertarTransaccional(
		Connection	ac_con,
		String		as_estado,
		int			ai_numeroSolicitud,
		int			ai_codigoServicio,
		String		as_nombreOtros,
		String		as_comentario,
		int numeroDocumento, boolean multiple, float frecuencia, int tipoFrecuencia, 
		int finalidad, boolean respuestaMultiple, boolean finalizadaRespuesta, int idCuenta, boolean finalizar,String portatil
	)throws SQLException;

	/**
	* Modifica los datos de una solicitud de procedimiento en una fuente de datos. Solo es posible
	* modificar el comentario de la solicitud
	* @param ac_con				Conexión a la fuente de datos
	* @param ai_numeroSolicitud	Número de la solicitud de procedimiento a modificar
	* @param as_comentario		Texto a adicionar en la sección de comentarios de la solicitud de
	*							procedimientos
	* @return El número de solicitudes de procedimientos modificadas
	*/
	public int modificar(
		Connection	ac_con,
		int			ai_numeroSolicitud,
		String		as_comentario
	)throws SQLException;

	/**
	 * Funcion para finalizar Solicitud de procedimientos
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 * @throws SQLException
	 */
	public int finalizarSolicitudMultiple(
			Connection	con,
			int			numeroSolicitud 
		)throws SQLException;

	
	/**
	* Modifica el número de autorización de una solicitud
	* @param ac_con					Conexión a la fuente de datos
	* @param ai_numeroSolicitud		Número de la solicitud a modificar
	* @param as_numeroAutorizacion	Nuevo número de autorización de la solicitud
	* @return El número de solicitudes de modificadas
	*/
	/*public int modificarNumeroAutorizacion(
		Connection	ac_con,
		int			ai_numeroSolicitud,
		String		as_numeroAutorizacion
	)throws SQLException;
	*/
	
	/**
	 * Mñétodo para consultar el numero de documento siguiente
	 * para insertar en la solicitud de procedimientos
	 * @param con
	 * @return numero de documento
	 */
	public int numeroDocumentoSiguiente(Connection con);
	
	/**
	 * Metodo para cargar los procedimientos con su codgio, descripcion, especialidad, 
	 * cantidad y si es pos segun el numero de Documento que reciba
	 * @param con
	 * @param numeroDocumento
	 * @return col Collection para recorrerla en la impresion
	 */
	public  Collection solicitudesXDocumento(Connection con, int numeroDocumento);
	
	
	/**
	 * Método que me carga la descripcion de una solictud de servicio externo
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String consultarExterno(Connection con, int numeroSolicitud);

	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @param fechaSolicitud
	 * @param horaSolicitud
	 * @return
	 */
	public Collection solicitudesXCuentaFechaHora(Connection con, int codigoCuenta, String fechaSolicitud, String horaSolicitud, int institucion);
	
	/**
	 * Metodo encargado de verificar si el servicio es un portatil
	 * @param con
	 * @param numSol
	 * @param codServ
	 * @return true/fasle
	 */
	public boolean esPortatil(Connection con,String numSol,String codServ);
	
	/**
	 * Metodo que devuelve el codigo del portatil asociado a la solicitud de proc, si no existe entonces -1
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int obtenerPortatilSolicitud(Connection con, int numeroSolicitud);
	
	/**
	 * Metodo que devuelve el codigo del portatil asociado a un servicio, si no existe entonces -1
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	public int obtenerPortatilServicio(Connection con, int codServicio);
	
	/**
	 * Metodo encargado de Anular o Aprobar un portatil
	 * @param con
	 * @param datos
	 * Jhony alexander Duque A.
	 * --------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------
	 * -- portatil --> Opcional
	 * -- motAnuPort --> Opcional
	 * -- numeroSolicitud --> Requerido
	 * @return
	 */
	public boolean anularAprobarPortatil(Connection con,HashMap datos);

	/**
	 * @param con
	 * @param numerosSolicitudes
	 * @return
	 */
	public String obtenerWhereFormatoMediaCarta(Connection con, HashMap numerosSolicitudes);

	public HashMap valAnulacion(Connection ac_con, int numeroSolicitud);
	
	public String[] obtenerConsultaSolicitudProcedimientosReporte(Connection con, HashMap numerosSolicitudes);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public abstract boolean finalizarSolicitudMultiple(Connection con,String numeroSolicitud); 
	
	/**
	 * M&eacute;todo encargado de consultar los servicios
	 * asociados a la solicitud de procedimientos
	 * @param Connection con, String numeroDocumento
	 * @return ArrayList<DtoServicios>
	 * @author Diana Carolina G
	 */
	public abstract DtoProcedimiento buscarServiciosSolicitudProcedimientos(Connection con,
			int numeroSolicitud, int codigoTarifario);
	/**
	 * M&eacute;todo encargado de consultar los servicios
	 * asociados a la solicitud de procedimientos de tipo no cruento
	 * @param Connection con, String numeroDocumento
	 * @return ArrayList<DtoServicios>
	 * @author Diana Carolina G
	 */
	public abstract DtoProcedimiento buscarServiciosSolicitudProcedimientosC(Connection con,
			int numeroSolicitud, int codigoTarifario);
	
	/**
	 * @param conn
	 * @param codigoMedico
	 * @return obtiene los datos del medico que anula  
	 * @throws SQLException
	 */
	public  String consultarDatosMedicoAnulacion(Connection conn,Integer codigoMedico) throws SQLException;
	
	
}