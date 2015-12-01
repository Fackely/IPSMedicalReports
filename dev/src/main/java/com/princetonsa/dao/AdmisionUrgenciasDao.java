/*
 * @(#)AdmisionUrgenciasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>AdmisionUrgencias/code>.
 *
 * @version 1.0, Mar 6, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>, <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public interface AdmisionUrgenciasDao {

	
	/**
	 * Este método inserta una nueva admisión de urgencias en una fuente de datos,
	 * recibiendo un parametro "estado" que define la transaccionalidad
	 * 
	 * @param con una conexión abierta con una fuente de datos
	 * @param codigoOrigen código del origen de admisión de esta urgencia
	 * @param fecha fecha en que se hizo esta admisión (en la forma 'año-mes-día')
	 * @param hora hora en que se hizo esta admisión
	 * @param codigoMedico código que identifica al médico en el sistema
	 * @param numeroAutorizacion número de autorización de esta urgencia
	 * @param codigoCausaExterna código de la causa externa que originó esta admisión
	 * @param fechaObservacion fecha de entrada a observación
	 * @param horaObservacion hora de entrada a observación
	 * @param codigoCama código de la cama de observación
	 * @param loginUsuario login del usuario que hace esta admisión
	 * @param idCuenta número de la cuenta asociada a esta admisión
	 * @param estado estado de la transacción "empezar", "continuar" o "finalizar" 
	 * @return 0 si no se pudo realizar la inserción; el código de la admisión si sí se pudo insertar.
	 */
	public int insertarAdmisionUrgencias(Connection con, int codigoOrigen, String fecha, String hora, int codigoMedico, /*String numeroAutorizacion, */int codigoCausaExterna, String fechaObservacion, String horaObservacion, int codigoCama, String loginUsuario, int idCuenta, String estado, String consecutivoTriage, String consecutivoFechaTriage,String fechaEgresoObservacion,String horaEgresoObservacion,int institucion) throws SQLException;
		
	/**
	 * Método que permite cargar una admisión de urgencias
	 * 
	 * @param con una conexión abierta con una fuente de datos
	 * @param codigoAdmisionUrgencias Código con el que quedo la admisión a
	 * cargar
	 * @param anio anio de la admision (hace parte de la PK)
	 * @return ResultSetDecorator con la información de la admisión urgencias
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarAdmisionUrgencias(Connection con, int codigoAdmisionUrgencias, int anio) throws SQLException;
	
	/**
	 * Este método implementa cargarAdmisionUrgencias para Genérica o Hsqldb.
	 * @see com.princetonsa.dao.AdmisionUrgenciasDao#cargarAdmisionUrgencias
	 * (Connection con, int codigoAdmisionUrgencias)
	 */
	public ResultSetDecorator cargarAdmisionUrgencias(Connection con, int codigoCuenta);

	/**
	 * Método que permite modificar una admisión de urgencias
	 * 
	 * @param con una conexión abierta con una fuente de datos
	 * @param codigo código de la admision
 	 * @param codigoOrigen código del origen de la admision
	 * @param fecha fecha en que se hizo esta admisión (en la forma 'año-mes-día')
	 * @param hora hora en que se hizo esta admisión
	 * @param codigoMedico código que identifica al médico en el sistema
	 * @param numeroAutorizacion número de autorización de esta urgencia
	 * @param codigoCausaExterna código de la causa externa que originó esta admisión
	 * @param fechaObservacion fecha de entrada a observación
	 * @param horaObservacion hora de entrada a observación
	 * @param codigoCama código de la cama de observación
	 * @param fechaEgresoObservacion fecha de salida de observación
	 * @param horaEgresoObservacion hora de salida de observación 
	 * @param loginUsuario login del usuario que hace esta admisión
	 * @return int 0 si salio mal o si no el número de admisiones
	 * modificadas
	 * @throws SQLException
	 */
	public int modificarAdmisionUrgencias (Connection con, int codigoAdmisionUrgencias, int anio, int codigoOrigen, String fecha, String hora, int codigoMedico, /*String numeroAutorizacion, */int codigoCausaExterna, String fechaObservacion, String horaObservacion, int codigoCama, String fechaEgresoObservacion, String horaEgresoObservacion, String loginUsuario) throws SQLException;
	
	/**
	 * Método que permite modificar una admisión de urgencias
	 * recibiendo un parametro "estado" que define la transaccionalidad
	 * 
	 * @param con una conexión abierta con una fuente de datos
	 * @param codigo código de la admision
	 * @param codigoOrigen código del origen de la admision
	 * @param fecha fecha en que se hizo esta admisión (en la forma 'año-mes-día')
	 * @param hora hora en que se hizo esta admisión
	 * @param codigoMedico código que identifica al médico en el sistema
	 * @param numeroAutorizacion número de autorización de esta urgencia
	 * @param codigoCausaExterna código de la causa externa que originó esta admisión
	 * @param fechaObservacion fecha de entrada a observación
	 * @param horaObservacion hora de entrada a observación
	 * @param codigoCama código de la cama de observación
	 * @param fechaEgresoObservacion fecha de salida de observación
	 * @param horaEgresoObservacion hora de salida de observación 
	 * @param loginUsuario login del usuario que hace esta admisión
	 * @param estado estado de la transacción "empezar", "continuar" o "finalizar" 
	 * @return int 0 si salio mal o si no el número de admisiones
	 * modificadas
	 * @throws SQLException
	 */
	public int modificarAdmisionUrgencias (Connection con, int codigoAdmisionUrgencias, int anio, int codigoOrigen, String fecha, String hora, int codigoMedico, /*String numeroAutorizacion, */int codigoCausaExterna, String fechaObservacion, String horaObservacion, int codigoCama, String fechaEgresoObservacion, String horaEgresoObservacion, String loginUsuario, String estado) throws SQLException;	
	public boolean isModificacionCamaHabilitada(Connection con, int codigoAdmisionUrgencias, int codigoCuenta) throws SQLException;
	public boolean mostrarDatosCama(Connection con, int codigoAdmisionUrgencias) throws SQLException;	
	/**
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param numeroCuenta Código de la cuenta asociada al numero de la solicitud con la que se pidió la valoración
	 * @return 0 si no se pudo borrar correctamente, 1 de lo contrario
	 * @throws SQLException
	 */	
	public int borrarDatosObservacion(Connection con, int numeroCuenta, int institucion) throws SQLException;
	
	/**
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param numeroCuenta Código de la cuenta asociada al numero de la solicitud con la que se pidió la valoración
	 * @return true si la admision de urgencias identificada con el numero de cuenta dado tiene datos de observación,
	 * false de lo contrario.
	 * @throws SQLException
	 */
	public boolean estaAsignadaCamaObservacion(Connection con, int numeroCuenta) ;
	
	/**
	 * Método que permite pasar una cama a estado de desinfección en
	 * modo transaccional
	 * 
	 * @param con una conexión abierta con una fuente de datos
	 * @param numeroAdmision Código con el que quedo la admisión
	 * a a la cual se le quiere liberar cama
	 * @param anioAdmision Año en el que se creo la admisión
	 * a a la cual se le quiere liberar cama
	 * @param estado estado de la transaccion (empezar, continuar,
	 * finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int pasarCamaADesinfeccionTransaccional (Connection con, int numeroAdmision, int anioAdmision, String estado, int institucion) throws SQLException;

	/**
	 * Método que permite asignar la fecha / hora actual a la de observación
	 * de una admisión de urgencias
	 * 
	 * @param con una conexión abierta con una fuente de datos
	 * @param numeroAdmision Código con el que quedo la admisión
	 * a a la cual se quiere asignar fecha / hora de observación
	 * @param anioAdmision Año en el que se creo la admisión
	 * a a la cual se quiere asignar fecha / hora de observación
	 * @param estado estado de la transaccion (empezar, continuar,
	 * finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int asignarFechaObservacionTransaccional(Connection con, int numeroAdmision, int anioAdmision, String fechaObservacion, String horaObservacion, String estado) throws SQLException;

	/**
	* Método que permite hacer una reversión de egreso para una admisión de
	* urgencias. Para esto se debe:
	*
	* 1. Pasar la cama seleccionada a estado de ocupada.
	* 2. Cambiar el estado de la admisión a urgencias
	* 3. Actualizar la cama en la admisión
	*
	* Todo esto debe ser en modo transaccional
	*
	* @param con Una conexión abierta con una fuente de datos
	* @param idCuenta Código de la cuenta a la que pertenece esta admisión
	* @param codigoCama Código de la cama que va a ocupar el paciente
	* @param estado Estado de la transacción (empezar, continuar, finalizar)
	* @return
	* @throws SQLException
	*/
	public int reversarEgresoYAdmisionTransaccional(Connection con, int idCuenta, int codigoCama, String estado) throws SQLException;
	public ResultSetDecorator getBasicoAdmision(Connection con, int numeroCuenta) throws SQLException;
	public String[] getCama(Connection con, int codigoAdmision) throws SQLException;
	public String[] getUltimaCama(Connection con, int codigoAdmision) throws SQLException;
	
	/**
	 * Método para actualizar la fecha/hora ingreso a observacion de la admision de urgencias
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean actualizarFechaHoraIngresoObservacion(Connection con,HashMap campos);
	/**
	 * Consulta la cuenta de una admision que tuvo asignada una cama en particular
	 * 
	 * @param con conexion 
	 * @param codigoCama
	 * @return Codigo de la cuenta
	 */
	public String obtenerCuentaUltimaAdmisionXCama(Connection con,
			int codigoCama);
	
}