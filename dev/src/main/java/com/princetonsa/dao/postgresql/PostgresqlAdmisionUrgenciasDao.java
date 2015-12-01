/*
 * @(#)PostgresqlAdmisionUrgenciasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import com.princetonsa.dao.AdmisionUrgenciasDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.SqlBaseAdmisionUrgenciasDao;

/**
 * Esta clase implementa el contrato estipulado en <code>AdmisionUrgenciasDao</code>, proporcionando los servicios
 * de acceso a una base de datos PostgreSQL requeridos por la clase <code>AdmisionUrgencias</code>
 *
 * @version 1.0, Mar 6, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>, <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class PostgresqlAdmisionUrgenciasDao implements AdmisionUrgenciasDao 
{
	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar una admisión de urgencias en la tabla respectiva.
	 */
	// "numero_autorizacion, " +
	private static final String insertarAdmisionUrgenciasStr = "INSERT INTO admisiones_urgencias (" +
			"codigo," +
			"consecutivo, "+
			"anio," +
			"origen_admision_urgencias, " +
			"fecha_admision, " +
			"hora_admision, " +
			"codigo_medico, " +
			"causa_externa, " +
			"fecha_ingreso_observacion, " +
			"hora_ingreso_observacion, " +
			"cama_observacion, " +
			"login_usuario, " +
			"cuenta, " +
			"consecutivo_triage, " +
			"consecutivo_triage_fecha, " +
			"fecha_egreso_observacion, " +
			"hora_egreso_observacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para 
	 * consultar los datos basicos de una admision de urgencias 
	 * dado el numero de cuenta . Es específico para postgres
	 * porque el cast 24:mi no funciona en Oracle
	 */
	private static final String consultarBasicoAdmisionStr = "" +
	"SELECT codigo as idAdmision, to_char(fecha_admision,'YYYY-MM-DD') as fechaAdmision, to_char(hora_admision,'24:mi') as horaAdmision, origen_admision_urgencias as origenAdmision " +
	"FROM admisiones_urgencias " +
	"WHERE cuenta = ?";
	
	private static final String consultaUltimaCama = 
			"SELECT * FROM "+
			"( "+
			  "SELECT ' P.' "+
			    "|| SUBSTR(pis.nombre,0,16) "+
			    "|| ' H.' "+
			    "|| SUBSTR(hab.nombre,0,16) "+
			    "|| ' C.' "+
			    "|| c.numero_cama AS cama, "+
			    "c.descripcion, "+
			    "c.codigo, "+
			    "au.fecha_egreso_observacion, "+
			    "au.hora_egreso_observacion "+
			  "FROM admisiones_urgencias au "+
			  "INNER JOIN camas1 c "+
			  "ON(c.codigo = au.cama_observacion) "+
			  "INNER JOIN habitaciones hab "+
			  "ON(hab.codigo = c.habitacion) "+
			  "INNER JOIN pisos pis "+
			  "ON(pis.codigo                    = hab.piso) "+
			  "WHERE au.codigo                  = ? "+
			"ORDER BY to_date((to_char(au.fecha_admision, 'DD/MM/YYYY') || ' ' || au.hora_admision) , 'DD/MM/YYYY HH24:MI') DESC "+
			") AS subconsulta "+
			"LIMIT 1 ";
	
	
	/**
	 * Este método implementa insertarAdmisionUrgencias para PostgreSQL.
	 * Manejando la transaccion dependiendo del estado que le llega en el
	 * parametro
	 * @see com.princetonsa.dao.AdmisionUrgenciasDao#insertarAdmisionUrgencias
	 * (Connection, int, String, String, String, String, String, int, String,
	 * String, int, String, int, String)
	 */
	public int insertarAdmisionUrgencias(Connection con, int codigoOrigen, String fecha, String hora, int codigoMedico, /*String numeroAutorizacion, */int codigoCausaExterna, String fechaObservacion, String horaObservacion, int codigoCama, String loginUsuario, int idCuenta, String estado, String consecutivoTriage, String consecutivoFechatriage,String fechaEgresoObservacion,String horaEgresoObservacion,int institucion) throws SQLException {
	    return SqlBaseAdmisionUrgenciasDao.insertarAdmisionUrgencias( con,  codigoOrigen,  fecha,  hora,  codigoMedico,  /*numeroAutorizacion, */codigoCausaExterna,  fechaObservacion,  horaObservacion,  codigoCama,  loginUsuario,  idCuenta,  estado,  insertarAdmisionUrgenciasStr, consecutivoTriage, consecutivoFechatriage,fechaEgresoObservacion,horaEgresoObservacion,institucion) ;
	}
	
	/**
	 * Este método implementa modificarAdmisionUrgencias para PostgreSQL.
	 * @see com.princetonsa.dao.AdmisionUrgenciasDao#modificarAdmisionUrgencias
	 * (Connection con,  int codigoOrigen, String fecha, String hora, String
	 * numeroIdentificacionMedico, String codigoTipoIdentificacionMedico, String
	 * numeroAutorizacion, int codigoCausaExterna, String fechaObservacion,
	 * String horaObservacion, int codigoCama, String loginUsuario)
	 */
	public  int modificarAdmisionUrgencias (Connection con, int codigoAdmisionUrgencias, int anio, int codigoOrigen, String fecha, String hora, int codigoMedico, /*String numeroAutorizacion, */int codigoCausaExterna, String fechaObservacion, String horaObservacion, int codigoCama, String fechaEgresoObservacion, String horaEgresoObservacion, String loginUsuario, String estado) throws SQLException {
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		//se le pasa por parámetros el objeto de tipo DaoFactory
		return SqlBaseAdmisionUrgenciasDao.modificarAdmisionUrgencias(con,codigoAdmisionUrgencias,anio,codigoOrigen,fecha,hora,codigoMedico,/*numeroAutorizacion,*/codigoCausaExterna,fechaObservacion,horaObservacion,codigoCama,fechaEgresoObservacion,horaEgresoObservacion,loginUsuario,estado,myFactory) ;
	}



	/**
	 * Este método implementa modificarAdmisionUrgencias para PostgreSQL.
	 * @see com.princetonsa.dao.AdmisionUrgenciasDao#modificarAdmisionUrgencias
	 * (Connection con,  int codigoOrigen, String fecha, String hora, String
	 * numeroIdentificacionMedico, String codigoTipoIdentificacionMedico, String
	 * numeroAutorizacion, int codigoCausaExterna, String fechaObservacion,
	 * String horaObservacion, int codigoCama, String loginUsuario)
	 */
	public int modificarAdmisionUrgencias (Connection con, int codigoAdmisionUrgencias, int anio, int codigoOrigen, String fecha, String hora, int codigoMedico, /*String numeroAutorizacion, */int codigoCausaExterna, String fechaObservacion, String horaObservacion, int codigoCama, String fechaEgresoObservacion, String horaEgresoObservacion, String loginUsuario) throws SQLException {
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				//se le pasa por parámetros el objeto de tipo DaoFactory
				return SqlBaseAdmisionUrgenciasDao.modificarAdmisionUrgencias(con, codigoAdmisionUrgencias, anio, codigoOrigen, fecha, hora,  codigoMedico, /*numeroAutorizacion, */codigoCausaExterna,  fechaObservacion,  horaObservacion,  codigoCama, fechaEgresoObservacion,  horaEgresoObservacion, loginUsuario, myFactory);
	}
	
	/**
	 * Este método implementa cargarAdmisionUrgencias para PostgreSQL.
	 * @see com.princetonsa.dao.AdmisionUrgenciasDao#cargarAdmisionUrgencias
	 * (Connection con, int codigoAdmisionUrgencias)
	 */
	public ResultSetDecorator cargarAdmisionUrgencias(Connection con, int codigoAdmisionUrgencias, int anio) throws SQLException
	{
		try
		{
			if (con == null || con.isClosed())
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
		}
		catch (SQLException e)
		{
			//Por alguna razón no salió bien
			return null;
		}
		return SqlBaseAdmisionUrgenciasDao.cargarAdmisionUrgencias(con,codigoAdmisionUrgencias,anio);
	}
	
	/**
	 * Este método implementa cargarAdmisionUrgencias para Genérica o Hsqldb.
	 * @see com.princetonsa.dao.AdmisionUrgenciasDao#cargarAdmisionUrgencias
	 * (Connection con, int codigoAdmisionUrgencias)
	 */
	public ResultSetDecorator cargarAdmisionUrgencias(Connection con, int codigoCuenta)
	{
		return SqlBaseAdmisionUrgenciasDao.cargarAdmisionUrgencias(con,codigoCuenta);
	}
	
	/**
	 * Si encuentra un egreso asociado a esta admision-cuenta con medico no nulo, significa que no se puede modificar los
	 * datos de ingreso a observacion. De lo contrario si.
	 * Nota Raul: esta funcionalidad queda por defecto en true, ya que la existencia del egreso se esta validando directamente en
	 * desicionTipoAdmision.jsp y para cualquier caso desvia a una pagina de error, ya que estos datos de ingreso se pueden modificar
	 * en los casos restantes.
	 */
	public boolean isModificacionCamaHabilitada(Connection con, int codigoAdmisionUrgencias, int codigoCuenta) throws SQLException
	{
		return SqlBaseAdmisionUrgenciasDao.isModificacionCamaHabilitada();
	}

	public boolean mostrarDatosCama(Connection con, int codigoAdmisionUrgencias) throws SQLException 
	{
		return SqlBaseAdmisionUrgenciasDao.mostrarDatosCama(con,codigoAdmisionUrgencias);
  	}
	
	/**
	 * Implementación del borrado de los datos de cama de observacion y de la liberacion de la cama,
	 * dejandola en estado 2 de desinfeccion
	 *
	 * @see com.princetonsa.dao.ValoracionUrgenciasDao#borrarDatosObservacion(Connection, int)
	 */
	public int borrarDatosObservacion(Connection con, int numeroCuenta, int institucion) throws SQLException
	{
		return SqlBaseAdmisionUrgenciasDao.borrarDatosObservacion(con,numeroCuenta, institucion);
	}
	
	public boolean estaAsignadaCamaObservacion(Connection con, int numeroCuenta) 
	{
		return SqlBaseAdmisionUrgenciasDao.estaAsignadaCamaObservacion(con,numeroCuenta);
	}
	
	/**
	 * Implementación de la consulta de datos basicos de una admision urgencias
	 * @see com.princetonsa.dao.AdmisionUrgenciasDao#getBasicoAdmision (con, int ) throws SQLException
	 * @version Sept. 11 de 2003
	 */
	public ResultSetDecorator getBasicoAdmision(Connection con, int numeroCuenta) throws SQLException
	{
	    return SqlBaseAdmisionUrgenciasDao.getBasicoAdmision(con, numeroCuenta, consultarBasicoAdmisionStr) ;
	}

	/**
	 * Para consultar la cama asociada a esta admision.
	 * Metodo usado para cargar resumen de los datos del paciente
	 */
	public String[] getCama(Connection con, int codigoAdmision) throws SQLException
	{
		return SqlBaseAdmisionUrgenciasDao.getCama(con,codigoAdmision);
	}
	public String[] getUltimaCama(Connection con, int codigoAdmision) throws SQLException
	{
		return SqlBaseAdmisionUrgenciasDao.getUltimaCama(con,consultaUltimaCama,codigoAdmision);
	}
	
	public  int pasarCamaADesinfeccionTransaccional (Connection con, int numeroAdmision, int anioAdmision, String estado, int institucion) throws SQLException
	{
	    return SqlBaseAdmisionUrgenciasDao.pasarCamaADesinfeccionTransaccional ( con,  numeroAdmision,  anioAdmision,  estado, institucion);
	}

	public  int asignarFechaObservacionTransaccional (Connection con, int numeroAdmision, int anioAdmision, String fechaObservacion, String horaObservacion, String estado) throws SQLException
	{
	    return SqlBaseAdmisionUrgenciasDao.asignarFechaObservacionTransaccional (con,  numeroAdmision,  anioAdmision,  fechaObservacion,  horaObservacion,  estado); 
	}

	/**
	* Método que implementa el efecto de una reversión de egreso en una
	* admisión de urgencias en una BD postgreSQL 
	*
	* @see com.princetonsa.dao.AdmisionUrgenciasDao#reversarEgresoYAdmisionTransaccional(Connection , int , int , String) throws SQLException
	*/
	public int reversarEgresoYAdmisionTransaccional(Connection con, int idCuenta, int codigoCama, String estado) throws SQLException
	{
	    return SqlBaseAdmisionUrgenciasDao.reversarEgresoYAdmisionTransaccional(con, idCuenta, codigoCama, estado); 
	}
	
	/**
	 * Método para actualizar la fecha/hora ingreso a observacion de la admision de urgencias
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean actualizarFechaHoraIngresoObservacion(Connection con,HashMap campos)
	{
		return SqlBaseAdmisionUrgenciasDao.actualizarFechaHoraIngresoObservacion(con, campos);
	}
	/**
	 * Consulta la cuenta de una admision que tuvo asignada una cama en particular
	 * 
	 * @param con conexion 
	 * @param codigoCama
	 * @return Codigo de la cuenta
	 */
	public String obtenerCuentaUltimaAdmisionXCama(Connection con,int codigoCama) {
		return SqlBaseAdmisionUrgenciasDao.obtenerCuentaUltimaAdmisionXCama(con, codigoCama);
	}	
}