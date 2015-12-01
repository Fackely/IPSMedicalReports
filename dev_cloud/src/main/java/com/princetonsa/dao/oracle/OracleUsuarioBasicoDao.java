/*
 * @(#)OracleUsuarioBasicoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.oracle;

import java.io.Serializable;
import java.sql.Connection;

import com.princetonsa.decorator.ResultSetDecorator;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.UsuarioBasicoDao;
import com.princetonsa.dao.sqlbase.SqlBaseUsuarioBasicoDao;

/**
 * Esta clase implementa el contrato estipulado en <code>UsuarioBasicoDao</code>, proporcionando los servicios
 * de acceso a una base de datos Oracle requeridos por la clase <code>UsuarioBasico</code>
 *
 * @version 1.0, Oct 1, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class OracleUsuarioBasicoDao implements UsuarioBasicoDao, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Cargar el nombre de la institución a la cual pertenece el usuario
	 * @param codigoInstitucion
	 * @return Nombre de la institución
	 * @throws SQLException
	 */
	public ResultSetDecorator cargarNombreInstitucion(Connection con, String codigoInstitucion) throws SQLException
	{
		return SqlBaseUsuarioBasicoDao.cargarNombreInstitucion(con, codigoInstitucion);
	}

	/**
	* Recupera, desde una fuente de datos, los datos basicos de un usuario.
	* @param ac_con				Conexión abierta con una fuente de datos
	* @param ai_codigoPersona	Codigo del usuario que se desea cargar
	* @return un <code>ResultSet</code> con los datos del usuario y una conexion abierta
	*/
	public ResultSetDecorator cargarUsuarioBasico(Connection ac_con, int ai_codigoPersona)throws SQLException
	{
		return SqlBaseUsuarioBasicoDao.cargarUsuarioBasico(ac_con, ai_codigoPersona);
	}

	/**
	 * Recupera, desde una base de datos Oracle, los datos basicos de un usuario.
	 * @param con una conexion abierta con una base de datos Oracle
	 * @param login el login del usuario que se desea cargar
	 * @return un <code>ResultSet</code> con los datos del usuario
	 */
	public ResultSetDecorator cargarUsuarioBasico (Connection con, String login) throws SQLException 
	{
		return SqlBaseUsuarioBasicoDao.cargarUsuarioBasico (con, login) ;
	}

	/**
	 * Recupera, desde una base de datos Oracle, los datos basicos de un usuario.
	 * @param con una conexion abierta con una base de datos Oracle
	 * @param tipoId tipo de identificación del usuario
	 * @param numeroId número de identificación del usuario
	 * @return un <code>ResultSet</code> con los datos del usuario
	 */
	public ResultSetDecorator cargarUsuarioBasico (Connection con, String tipoId, String numeroId) throws SQLException 
	{
		return SqlBaseUsuarioBasicoDao.cargarUsuarioBasico (con, tipoId, numeroId) ;
	}

	/**
	 * Cambia el password de un usuario.
	 * @param login login el login del usuario del cual se desea cambiar el password
	 * @param newPassword el nuevo password del usuario
	 * @return el nuevo password del usuario, <i>hashed</i> con el algoritmo MD5
	 */
	public String cambiarPassword (final String login, final String newPassword,String usuActual) throws SQLException 
	{
		String resp="";
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		Connection con = myFactory.getConnection();
		
		resp=SqlBaseUsuarioBasicoDao.cambiarPassword (con, login, newPassword,usuActual) ;

		if (con != null && !con.isClosed()) 
		{
			UtilidadBD.closeConnection(con);
			con = null;
		}

		return resp;
	}

	/**
	 * Cambia el password de un usuario. Este método es llamado únicamente
	 * desde la funcionalidad "administrador - cambiar contraseña", ya que cambia el password de "cualquiera" que tenga el login dado.
	 * @param login login el login del usuario del cual se desea cambiar el password
	 * @param newPassword el nuevo password del usuario
	 * @param change parámetro dummy, para que este método tenga una <i>signature</i> diferente al anterior.
	 * @return <b>true</b> si se pudo cambiar el password, <b>false</b> si no.
	 */
	public boolean cambiarPassword (final String login, final String newPassword, final boolean change,String usuActual) throws SQLException 
	{
		boolean resp=false;

		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		Connection con = myFactory.getConnection();

		resp=SqlBaseUsuarioBasicoDao.cambiarPassword (con, login, newPassword, change,usuActual) ;
		if (con != null && !con.isClosed()) 
		{
			UtilidadBD.closeConnection(con);
			con = null;
		}
		return resp;
	}

	/**
	 * Implementación en Oracle de la cargada de Número de Registro Médico
	 * @see com.princetonsa.dao.UsuarioBasicoDao#cargarDatosMedico(Connection, int)
	 */
	public ResultSetDecorator cargarDatosMedico (Connection con, int codigoPersona) throws SQLException
	{
		return SqlBaseUsuarioBasicoDao.cargarDatosMedico (con, codigoPersona) ;
	}

	/**
	 * Implementación del método que permite saber si un usuario es paciente
	 * y solo tiene el rol paciente en una BD Oracle
	 *
	 * @see com.princetonsa.dao.TagDao#consultaTagValoresPorDefecto (Connection , String ) throws SQLException
	 */
	public boolean esUsuarioSoloPaciente (Connection con, String login) throws SQLException
	{
		return SqlBaseUsuarioBasicoDao.esUsuarioSoloPaciente (con, login) ;
	}

	/**
	 * adición sebastián
	 * Método que lista todos los usuarios que hacen parte de un mismo 
	 * profesional o usuarios del sistema
	 * @param con
	 * @param tipoId
	 * @param numeroId
	 * @return
	 */
	public Collection cargarUsuariosMismoProfesional(Connection con, String tipoId, String numeroId,String activo) 
	{
		return SqlBaseUsuarioBasicoDao.cargarUsuariosMismoProfesional(con,tipoId,numeroId,activo);
	}
	
	/**
	 * Método implementado para cargar los centros de costo de un usuario
	 * @param con
	 * @param login
	 * @return
	 */
	public HashMap cargarCentrosCostoUsuario(Connection con,String login)
	{
		return SqlBaseUsuarioBasicoDao.cargarCentrosCostoUsuario(con,login);
	}
	
	
	/**
	 * @see com.princetonsa.dao.UsuarioBasicoDao#consultarTieneRol(java.sql.Connection, java.lang.String, java.lang.Integer)
	 */
	public  Boolean consultarTieneRol(Connection con,String loginUsuario, Integer codigoFuncionalidad){
		return SqlBaseUsuarioBasicoDao.consultarTieneRol(con, loginUsuario, codigoFuncionalidad);
	}
}