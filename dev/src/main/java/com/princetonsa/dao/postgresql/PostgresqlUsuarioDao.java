/*
 * @(#)PosgresqlUsuarioDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import util.Answer;

import com.princetonsa.dao.UsuarioDao;
import com.princetonsa.dao.sqlbase.SqlBaseUsuarioDao;

/**
 * Esta clase implementa el contrato estipulado en <code>UsuarioDao</code>, proporcionando los servicios
 * de acceso a una base de datos PostgreSQL requeridos por la clase <code>Usuario</code>
 *
 * @version 1.0, Sep 22, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class PostgresqlUsuarioDao implements UsuarioDao 
{

	/**
	 * Inserta un usuario en una base de datos PostgreSQL. Usa una conexion abierta. Si no existe, la crea.
	 * @param con una conexion abierta con una base de datos PostgreSQL
	 * @param loginUsuario login del usuario en el sistema
	 * @param passwordUsuario contraseña del usuario en el sistema
	 * @param rolesUsuario rol(es) del usuario en el sistema
	 * @param numeroIdentificacion numero de identificacion del usuario
	 * @param codigoTipoIdentificacion codigo del tipo de identificacion del usuario
	 * @param codigoDeptoId
	 * @param codigoCiudadId
	 * @param codigoDepartamentoIdentificacion codigo del departamento donde fue expedida la identificacion del usuario
	 * @param codigoCiudadIdentificacion codigo de la ciudad donde fue expedida la identificacion del usuario
	 * @param codigoTipoPersona tipo de persona (Natural o Juridica)
	 * @param diaNacimiento dia de nacimiento del usuario
	 * @param mesNacimiento mes de nacimiento del usuario
	 * @param anioNacimiento año de nacimiento del usuario
	 * @param codigoEstadoCivil estado civil del usuario
	 * @param codigoSexo sexo del usuario
	 * @param primerNombrePersona primer nombre del usuario
	 * @param segundoNombrePersona segundo nombre del usuario
	 * @param primerApellidoPersona primer apellido del usuario
	 * @param segundoApellidoPersona segundo apellido del usuario
	 * @param direccion direccion de la residencia del usuario
	 * @param codigoDepartamento departamento donde reside el usuario
	 * @param codigoCiudad codigo de la ciudad donde reside el usuario
	 * @param codigoBarrio codigo del barrio de residencia del usuario
	 * @param telefono telefono del usuario
	 * @param email correo electronico del usuario
	 * @param codigoInstitucion institucion a la que pertenece este usuario
	 * @return numero de filas insertadas en la BD
	 */
	public int insertarUsuario(Connection con, String loginUsuario, String passwordUsuario, String [] rolesUsuario, String numeroIdentificacion, String codigoTipoIdentificacion, String codigoDeptoId, String codigoCiudadId, String codigoPaisId, String codigoDepartamentoIdentificacion, String codigoCiudadIdentificacion, String codigoPaisIdentificacion, String codigoTipoPersona, String diaNacimiento, String mesNacimiento, String anioNacimiento, String codigoEstadoCivil, String codigoSexo, String primerNombrePersona, String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String direccion, String codigoDepartamento, String codigoCiudad, String codigoPais, String codigoBarrio, String telefono, String email, String codigoCargo, String codigoInstitucion, HashMap centrosCosto,String codigoLocalidad, String indicativoInterfaz, String contratoInterfaz, int diasInactivarUsuario,int diasCaducidadPWD, String usuarioCreacion, String fechaCreacion, String horaCreacion, String fechaUltimaActivacionUsuario, String fechaUltimaActivacionPWD)  
	{
		return SqlBaseUsuarioDao.insertarUsuario(con, loginUsuario, passwordUsuario, rolesUsuario, numeroIdentificacion, codigoTipoIdentificacion, codigoDeptoId, codigoCiudadId, codigoPaisId, codigoDepartamentoIdentificacion, codigoCiudadIdentificacion, codigoPaisIdentificacion, codigoTipoPersona, diaNacimiento, mesNacimiento, anioNacimiento, codigoEstadoCivil, codigoSexo, primerNombrePersona, segundoNombrePersona, primerApellidoPersona, segundoApellidoPersona, direccion, codigoDepartamento, codigoCiudad, codigoPais, codigoBarrio, telefono, email,codigoCargo, codigoInstitucion, centrosCosto,codigoLocalidad, indicativoInterfaz, contratoInterfaz,diasInactivarUsuario,diasCaducidadPWD,usuarioCreacion, fechaCreacion, horaCreacion, fechaUltimaActivacionUsuario, fechaUltimaActivacionPWD) ;
	}
	
	/**
	 * Dado un login, busca en la base de datos PostgreSQL el usuario correspondiente y recupera sus datos.
	 * @param con una conexion abierta con la BD PostgreSQL
	 * @param login login del usuario que se desea cargar
	 * @return Un objeto <code>Answer</code> con la conexion abierta y un <code>ResultSet</code> con los datos del usuario
	 */
	public Answer cargarUsuario(Connection con, String login) throws SQLException 
	{
		return SqlBaseUsuarioDao.cargarUsuario(con, login) ;
	}

	/**
	 * Dado el login de un usuario, modifica sus datos. Usa una conexion abierta, Si no existe, la crea.
	 * Notese que no se cambia ni el login ni el password del usuario en el sistema.
	 * @param con una conexion abierta con una base de datos PostgreSQL
	 * @param rolesUsuario rol(es) del usuario en el sistema
	 * @param login login del usuario en el sistema
	 * @param numeroIdentificacion numero de identificacion del usuario
	 * @param codigoTipoIdentificacion codigo del tipo de identificacion del usuario
	 * @param codigoDepartamentoIdentificacion codigo del departamento donde fue expedida la identificacion del usuario
	 * @param codigoCiudadIdentificacion codigo de la ciudad donde fue expedida la identificacion del usuario
	 * @param codigoTipoPersona tipo de persona (Natural o Juridica)
	 * @param diaNacimiento dia de nacimiento del usuario
	 * @param mesNacimiento mes de nacimiento del usuario
	 * @param anioNacimiento año de nacimiento del usuario
	 * @param codigoEstadoCivil estado civil del usuario
	 * @param codigoSexo sexo del usuario
	 * @param primerNombrePersona primer nombre del usuario
	 * @param segundoNombrePersona segundo nombre del usuario
	 * @param primerApellidoPersona primer apellido del usuario
	 * @param segundoApellidoPersona segundo apellido del usuario
	 * @param direccion direccion de la residencia del usuario
	 * @param codigoDepartamento departamento donde reside el usuario
	 * @param codigoCiudad codigo de la ciudad donde reside el usuario
	 * @param codigoBarrio codigo del barrio de residencia del usuario
	 * @param telefono telefono del usuario
	 * @param email correo electronico del usuario
	 * @return numero de filas modificadas en la BD
	 */
	public int modificarUsuario(Connection con,	String[] rolesUsuario, String login,String codCargo, int codigoPersona, String numeroIdentificacion, String codigoTipoIdentificacion, String codigoPaisIdentificacion, String codigoDepartamentoIdentificacion, String codigoCiudadIdentificacion, String codigoTipoPersona, String diaNacimiento, String mesNacimiento, String anioNacimiento, String codigoEstadoCivil, String codigoSexo, String primerNombrePersona, String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String direccion, String codigoPais, String codigoDepartamento, String codigoCiudad, String codigoBarrio, String telefono, String email, HashMap centrosCosto, String contratoInterfaz,String activo, int diasInactivarUsuario,int diasCaducidadPWD,String usuarioModifica) throws SQLException 
	{
		return SqlBaseUsuarioDao.modificarUsuario(con,	rolesUsuario, login, codCargo, centrosCosto, contratoInterfaz,activo,diasInactivarUsuario,diasCaducidadPWD,usuarioModifica) ;
	}

	/**
	 * Lista los roles disponibles en el sistema.
	 * @param con una conexion abierta con la base de datos PostgreSQL
	 * @return el código en HTML que muestra los roles disponibles del sistema
	 */
	public String listarRoles(Connection con) throws SQLException 
	{
		return SqlBaseUsuarioDao.listarRoles(con);
	}

	/**
	 * Lista los roles disponibles en el sistema, con los roles a los que pertenece el usuario elegidos (<i>checked</i>)
	 * @param con una conexion abierta con la base de datos PostgreSQL
	 * @param rolesUsuario roles a los cuales pertenece el usuario
	 * @return el código en HTML que muestra los roles disponibles del sistema
	 */
	public String listarRolesElegidos(Connection con, String[] rolesUsuario) throws SQLException 
	{
		return SqlBaseUsuarioDao.listarRolesElegidos(con, rolesUsuario);
	}

	/**
	 * Método auxiliar, que dados un número y un tipo de identificación retorna
	 * el login respectivo de ese usuario.
	 * @param con una conexión abierta con la BD
	 * @param tipoId tipo de identificación del usuario
	 * @param numeroId número de identificación del usuario
	 * @param activo:
	 * 'true' consultará login activo
	 * 'false' consultará login inactivo
	 * '' no se define si es login activo/inactivo
	 * @return cadena con el login del usuario o <b>null</b> si no existía un usuario con esos datos
	 */
	public String buscarLogin(Connection con, String tipoId, String numeroId,String activo) throws SQLException 
	{
		return SqlBaseUsuarioDao.buscarLogin(con, tipoId, numeroId,activo) ;
	}

	/**
	 * Método que permite desactivar un usuario dado su login
	 * @param con una conexion abierta con la base de datos PostgreSQL
	 * @param login del usuario que desea ser eliminado
	 * @param codigoInstitucion código de la institución de la cual se va a desactivar al usuario
	 * @return número de usuarios desactivados
	 */
	public int desactivarUsuario(Connection con, String login, String codigoInstitucion,String usuActual) throws SQLException 
	{
		return SqlBaseUsuarioDao.desactivarUsuario(con, login, codigoInstitucion,usuActual) ;
	}

	/**
	 * Método que permite desactivar un usuario dada su identificación
	 * @param con una conexion abierta con la fuente de datos
	 * @param tipoId  Tipo de identificacion del usuario que se desea desactivar
	 * @param numeroId  Numero de identificacion del usuario que se desea desactivar
	 * @param codigoInstitucion código de la institución de la cual se va a desactivar al usuario
	 * @return número de usuarios desactivados
	 */
	public int desactivarUsuario(Connection con, String tipoId, String numeroId, String codigoInstitucion,String usuActual) throws SQLException 
	{
		return SqlBaseUsuarioDao.desactivarUsuario(con, tipoId, numeroId, codigoInstitucion,usuActual) ;
	}

	/**
	 * Método que permite activar un usuario dado su login
	 * @param con una conexion abierta con la base de datos PostgreSQL
	 * @param login del usuario que desea ser activado
	 * @param codigoInstitucion código de la institución de la cual se va a desactivar al usuario
	 * @return número de usuarios activados
	 */
	public int activarUsuario(Connection con, String login, String codigoInstitucion,String usuActual) throws SQLException 
	{
		return SqlBaseUsuarioDao.activarUsuario(con, login, codigoInstitucion,usuActual) ;
	}

	/**
	 * Método que permite activar un usuario dada su identificación.
	 * @param con una conexion abierta con la base de datos PostgreSQL
	 * @param tipoId  Tipo de identificacion del usuario que se desea activar
	 * @param numeroId  Numero de identificacion del usuario que se desea activar
	 * @param codigoInstitucion código de la institución de la cual se va a desactivar al usuario
	 * @return número de usuarios activados
	*/
	public int activarUsuario(Connection con, String tipoId, String numeroId, String codigoInstitucion,String usuActual) throws SQLException 
	{
		return SqlBaseUsuarioDao.activarUsuario(con, tipoId, numeroId, codigoInstitucion,usuActual) ;
	}

	/**
	 * Implementación del método que permite buscar el login de un médico
	 * para una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.UsuarioDao#buscarLogin(java.sql.Connection, int)
	 */
	public String buscarLogin(Connection con, int codigoMedico) throws SQLException 
	{
		return SqlBaseUsuarioDao.buscarLogin(con, codigoMedico) ;
	}
	
	
	/**
	 * Método implementado para cargar los centros de costo del usuario
	 * @param con
	 * @param login
	 * @return
	 */
	public HashMap cargarCentrosCostoUsuario(Connection con,String login)
	{
		return SqlBaseUsuarioDao.cargarCentrosCostoUsuario(con,login);
	}

}