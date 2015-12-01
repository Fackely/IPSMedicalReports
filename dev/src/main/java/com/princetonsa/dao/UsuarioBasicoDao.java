/*
 * @(#)UsuarioBasicoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>UsuarioBasico</code>.
 *
 * @version 1.0, Oct 1, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public interface UsuarioBasicoDao {

	/**
	 * Cargar el nombre de la instituci�n a la cual pertenece el usuario
	 * @param codigoInstitucion
	 * @return Nombre de la instituci�n
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarNombreInstitucion(Connection con, String codigoInstitucion) throws SQLException;
	
	/**
	* Recupera, desde una fuente de datos, los datos basicos de un usuario.
	* @param ac_con				Conexi�n abierta con una fuente de datos
	* @param ai_codigoPersona	Codigo del usuario que se desea cargar
	* @return un <code>ResultSet</code> con los datos del usuario y una conexion abierta
	*/
	public ResultSetDecorator cargarUsuarioBasico(Connection ac_con, int ai_codigoPersona)
	throws SQLException;

	/**
	 * Recupera, desde una fuente de datos, los datos basicos de un usuario.
	 * @param con una conexion abierta con una fuente de datos
	 * @param login el login del usuario que se desea cargar
	 * @return un <code>ResultSet</code> con los datos del usuario y una conexion abierta
	 */
	public ResultSetDecorator cargarUsuarioBasico (Connection con, String login) throws SQLException;

	/**
	 * Recupera, desde una fuente de datos, los datos basicos de un usuario.
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoId tipo de identificaci�n del usuario
	 * @param numeroId n�mero de identificaci�n del usuario
	 * @return un <code>ResultSet</code> con los datos del usuario y una conexion abierta
	 */
	public ResultSetDecorator cargarUsuarioBasico (Connection con, String tipoId, String numeroId) throws SQLException;

	/**
	 * Cambia el password de un usuario. Internamente, abre y cierra una coexi�n con la fuente de datos
	 * @param login el login del usuario del cual se desea cambiar el password
	 * @param newPassword el nuevo password del usuario
	 * @return el nuevo password del usuario, <i>hashed</i> con el algoritmo MD5
	 */
	public String cambiarPassword (final String login, final String newPassword,String usuActual) throws SQLException;

	/**
	 * Cambia el password de un usuario Internamente, abre y cierra una coexi�n con la fuente de datos. Este m�todo es llamado �nicamente
	 * desde la funcionalidad "administrador - cambiar contrase�a", ya que cambia el password de "cualquiera" que tenga el login dado.
	 * @param login el login del usuario del cual se desea cambiar el password
	 * @param newPassword el nuevo password del usuario
	 * @param change par�metro dummy, para que este m�todo tenga una <i>signature</i> diferente al anterior.
	 * @return <b>true</b> si se pudo cambiar el password, <b>false</b> si no.
	 */
	public boolean cambiarPassword (final String login, final String newPassword, final boolean change,String usuActual) throws SQLException;

	/**
	 * M�todo que b�sca el n�mero de registro m�dico de este usuario
	 * (Devuelve "-" si el usuario no es m�dico)
	 *
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoTipoIdentificacionUsuario C�digo del tipo de identificaci�n
	 * del usuario al que queremos buscarle su numero de registro como m�dico
	 * @param numeroIdentificacionPacienteUsuario N�mero de identificaci�n del
	 * usuario al que queremos buscarle su numero de registro como m�dico
	 * @return String con "-" si el usuario no es m�dico o el n�mero de registro
	 * si es m�dico
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarDatosMedico (Connection con, int codigoPersona) throws SQLException;

	/**
	 * M�todo que revisa si un usuario es paciente y solo tiene como rol
	 * paciente
	 *
	 * @param con una conexion abierta con una fuente de datos
	 * @param login login del usuario para el que se desea saber si es
	 * paciente con �nico rol paciente
	 * @return
	 * @throws SQLException
	 */
	public boolean esUsuarioSoloPaciente (Connection con, String login) throws SQLException;
	/**
	 * adici�n sebasti�n
	 * M�todo que lista todos los usuarios que hacen parte de un mismo 
	 * profesional o usuarios del sistema
	 * @param con
	 * @param tipoId
	 * @param numeroId
	 * @return
	 */
	public Collection cargarUsuariosMismoProfesional(Connection con, String tipoId, String numeroId,String activo);
	
	/**
	 * M�todo implementado para cargar los centros de costo de un usuario
	 * @param con
	 * @param login
	 * @return
	 */
	public HashMap cargarCentrosCostoUsuario(Connection con,String login);
	
	/**
	 * @param con
	 * @param loginUsuario
	 * @param codigoFuncionalidad
	 * @return si tiene una funcionalidad dada
	 */
	public  Boolean consultarTieneRol(Connection con,String loginUsuario, Integer codigoFuncionalidad);
}