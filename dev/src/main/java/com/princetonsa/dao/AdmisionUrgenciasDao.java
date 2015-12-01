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
	 * Este m�todo inserta una nueva admisi�n de urgencias en una fuente de datos,
	 * recibiendo un parametro "estado" que define la transaccionalidad
	 * 
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param codigoOrigen c�digo del origen de admisi�n de esta urgencia
	 * @param fecha fecha en que se hizo esta admisi�n (en la forma 'a�o-mes-d�a')
	 * @param hora hora en que se hizo esta admisi�n
	 * @param codigoMedico c�digo que identifica al m�dico en el sistema
	 * @param numeroAutorizacion n�mero de autorizaci�n de esta urgencia
	 * @param codigoCausaExterna c�digo de la causa externa que origin� esta admisi�n
	 * @param fechaObservacion fecha de entrada a observaci�n
	 * @param horaObservacion hora de entrada a observaci�n
	 * @param codigoCama c�digo de la cama de observaci�n
	 * @param loginUsuario login del usuario que hace esta admisi�n
	 * @param idCuenta n�mero de la cuenta asociada a esta admisi�n
	 * @param estado estado de la transacci�n "empezar", "continuar" o "finalizar" 
	 * @return 0 si no se pudo realizar la inserci�n; el c�digo de la admisi�n si s� se pudo insertar.
	 */
	public int insertarAdmisionUrgencias(Connection con, int codigoOrigen, String fecha, String hora, int codigoMedico, /*String numeroAutorizacion, */int codigoCausaExterna, String fechaObservacion, String horaObservacion, int codigoCama, String loginUsuario, int idCuenta, String estado, String consecutivoTriage, String consecutivoFechaTriage,String fechaEgresoObservacion,String horaEgresoObservacion,int institucion) throws SQLException;
		
	/**
	 * M�todo que permite cargar una admisi�n de urgencias
	 * 
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param codigoAdmisionUrgencias C�digo con el que quedo la admisi�n a
	 * cargar
	 * @param anio anio de la admision (hace parte de la PK)
	 * @return ResultSetDecorator con la informaci�n de la admisi�n urgencias
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarAdmisionUrgencias(Connection con, int codigoAdmisionUrgencias, int anio) throws SQLException;
	
	/**
	 * Este m�todo implementa cargarAdmisionUrgencias para Gen�rica o Hsqldb.
	 * @see com.princetonsa.dao.AdmisionUrgenciasDao#cargarAdmisionUrgencias
	 * (Connection con, int codigoAdmisionUrgencias)
	 */
	public ResultSetDecorator cargarAdmisionUrgencias(Connection con, int codigoCuenta);

	/**
	 * M�todo que permite modificar una admisi�n de urgencias
	 * 
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param codigo c�digo de la admision
 	 * @param codigoOrigen c�digo del origen de la admision
	 * @param fecha fecha en que se hizo esta admisi�n (en la forma 'a�o-mes-d�a')
	 * @param hora hora en que se hizo esta admisi�n
	 * @param codigoMedico c�digo que identifica al m�dico en el sistema
	 * @param numeroAutorizacion n�mero de autorizaci�n de esta urgencia
	 * @param codigoCausaExterna c�digo de la causa externa que origin� esta admisi�n
	 * @param fechaObservacion fecha de entrada a observaci�n
	 * @param horaObservacion hora de entrada a observaci�n
	 * @param codigoCama c�digo de la cama de observaci�n
	 * @param fechaEgresoObservacion fecha de salida de observaci�n
	 * @param horaEgresoObservacion hora de salida de observaci�n 
	 * @param loginUsuario login del usuario que hace esta admisi�n
	 * @return int 0 si salio mal o si no el n�mero de admisiones
	 * modificadas
	 * @throws SQLException
	 */
	public int modificarAdmisionUrgencias (Connection con, int codigoAdmisionUrgencias, int anio, int codigoOrigen, String fecha, String hora, int codigoMedico, /*String numeroAutorizacion, */int codigoCausaExterna, String fechaObservacion, String horaObservacion, int codigoCama, String fechaEgresoObservacion, String horaEgresoObservacion, String loginUsuario) throws SQLException;
	
	/**
	 * M�todo que permite modificar una admisi�n de urgencias
	 * recibiendo un parametro "estado" que define la transaccionalidad
	 * 
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param codigo c�digo de la admision
	 * @param codigoOrigen c�digo del origen de la admision
	 * @param fecha fecha en que se hizo esta admisi�n (en la forma 'a�o-mes-d�a')
	 * @param hora hora en que se hizo esta admisi�n
	 * @param codigoMedico c�digo que identifica al m�dico en el sistema
	 * @param numeroAutorizacion n�mero de autorizaci�n de esta urgencia
	 * @param codigoCausaExterna c�digo de la causa externa que origin� esta admisi�n
	 * @param fechaObservacion fecha de entrada a observaci�n
	 * @param horaObservacion hora de entrada a observaci�n
	 * @param codigoCama c�digo de la cama de observaci�n
	 * @param fechaEgresoObservacion fecha de salida de observaci�n
	 * @param horaEgresoObservacion hora de salida de observaci�n 
	 * @param loginUsuario login del usuario que hace esta admisi�n
	 * @param estado estado de la transacci�n "empezar", "continuar" o "finalizar" 
	 * @return int 0 si salio mal o si no el n�mero de admisiones
	 * modificadas
	 * @throws SQLException
	 */
	public int modificarAdmisionUrgencias (Connection con, int codigoAdmisionUrgencias, int anio, int codigoOrigen, String fecha, String hora, int codigoMedico, /*String numeroAutorizacion, */int codigoCausaExterna, String fechaObservacion, String horaObservacion, int codigoCama, String fechaEgresoObservacion, String horaEgresoObservacion, String loginUsuario, String estado) throws SQLException;	
	public boolean isModificacionCamaHabilitada(Connection con, int codigoAdmisionUrgencias, int codigoCuenta) throws SQLException;
	public boolean mostrarDatosCama(Connection con, int codigoAdmisionUrgencias) throws SQLException;	
	/**
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param numeroCuenta C�digo de la cuenta asociada al numero de la solicitud con la que se pidi� la valoraci�n
	 * @return 0 si no se pudo borrar correctamente, 1 de lo contrario
	 * @throws SQLException
	 */	
	public int borrarDatosObservacion(Connection con, int numeroCuenta, int institucion) throws SQLException;
	
	/**
	 * 
	 * @param con Una conexion abierta con una fuente de datos
	 * @param numeroCuenta C�digo de la cuenta asociada al numero de la solicitud con la que se pidi� la valoraci�n
	 * @return true si la admision de urgencias identificada con el numero de cuenta dado tiene datos de observaci�n,
	 * false de lo contrario.
	 * @throws SQLException
	 */
	public boolean estaAsignadaCamaObservacion(Connection con, int numeroCuenta) ;
	
	/**
	 * M�todo que permite pasar una cama a estado de desinfecci�n en
	 * modo transaccional
	 * 
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param numeroAdmision C�digo con el que quedo la admisi�n
	 * a a la cual se le quiere liberar cama
	 * @param anioAdmision A�o en el que se creo la admisi�n
	 * a a la cual se le quiere liberar cama
	 * @param estado estado de la transaccion (empezar, continuar,
	 * finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int pasarCamaADesinfeccionTransaccional (Connection con, int numeroAdmision, int anioAdmision, String estado, int institucion) throws SQLException;

	/**
	 * M�todo que permite asignar la fecha / hora actual a la de observaci�n
	 * de una admisi�n de urgencias
	 * 
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param numeroAdmision C�digo con el que quedo la admisi�n
	 * a a la cual se quiere asignar fecha / hora de observaci�n
	 * @param anioAdmision A�o en el que se creo la admisi�n
	 * a a la cual se quiere asignar fecha / hora de observaci�n
	 * @param estado estado de la transaccion (empezar, continuar,
	 * finalizar)
	 * @return
	 * @throws SQLException
	 */
	public int asignarFechaObservacionTransaccional(Connection con, int numeroAdmision, int anioAdmision, String fechaObservacion, String horaObservacion, String estado) throws SQLException;

	/**
	* M�todo que permite hacer una reversi�n de egreso para una admisi�n de
	* urgencias. Para esto se debe:
	*
	* 1. Pasar la cama seleccionada a estado de ocupada.
	* 2. Cambiar el estado de la admisi�n a urgencias
	* 3. Actualizar la cama en la admisi�n
	*
	* Todo esto debe ser en modo transaccional
	*
	* @param con Una conexi�n abierta con una fuente de datos
	* @param idCuenta C�digo de la cuenta a la que pertenece esta admisi�n
	* @param codigoCama C�digo de la cama que va a ocupar el paciente
	* @param estado Estado de la transacci�n (empezar, continuar, finalizar)
	* @return
	* @throws SQLException
	*/
	public int reversarEgresoYAdmisionTransaccional(Connection con, int idCuenta, int codigoCama, String estado) throws SQLException;
	public ResultSetDecorator getBasicoAdmision(Connection con, int numeroCuenta) throws SQLException;
	public String[] getCama(Connection con, int codigoAdmision) throws SQLException;
	public String[] getUltimaCama(Connection con, int codigoAdmision) throws SQLException;
	
	/**
	 * M�todo para actualizar la fecha/hora ingreso a observacion de la admision de urgencias
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