/*
 * @(#)UsuarioDao.java
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
import java.util.HashMap;

import util.Answer;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>Usuario</code>.
 *
 * @version 1.0, Sep 22, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public interface UsuarioDao 
{

	/**
	 * Inserta un usuario en una fuente de datos. Usa una conexion abierta.
	 * @param con una conexion abierta con una fuente de datos
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
	 * @param codigocargo codigo del cargo que tiene el  usuario
	 * @param codigoInstitucion código de la institución en la que se va a insertar el usuario
	 * @param codigoTipoPersona 
	 * @param codigoLocalidad 
	 * @param indicativoInterfaz 
	 * @param fechaActual3 
	 * @param fechaUltima 
	 * @param horaCreacion 
	 * @param fechaCreacion 
	 * @param usuarioCreacion 
	 * @param j 
	 * @param i 
	 * @param codigoCentroCosto Mapa donde se almacenan los centros de costo del usuario
	 * @return numero de usuarios insertados
	 */
	public int insertarUsuario (Connection con, String loginUsuario, String passwordUsuario, String [] rolesUsuario, String numeroIdentificacion, String codigoTipoIdentificacion, String codigoDeptoId, String codigoCiudadId, String codigoPaisId, String codigoDepartamentoIdentificacion, String codigoCiudadIdentificacion, String codigoPaisIdentificacion, String codigoTipoPersona, String diaNacimiento, String mesNacimiento, String anioNacimiento, String codigoEstadoCivil, String codigoSexo, String primerNombrePersona, String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String direccion, String codigoDepartamento, String codigoCiudad, String codigoPais, String codigoBarrio, String telefono, String email, String codigoCargo, String codigoInstitucion, HashMap centrosCosto,String codigoLocalidad, String indicativoInterfaz, String contratoInterfaz, int diasInactivarUsuario,int diasCaducidadPWD, String usuarioCreacion, String fechaCreacion, String horaCreacion, String fechaUltimaActivacionUsuario, String fechaUltimaActivacionPWD) ;	

	/**
	 * Dado un login, busca en la fuente de datos el usuario correspondiente y recupera sus datos
	 * @param con una conexion abierta con una fuente de datos
	 * @param login login del usuario que se desea cargar
	 * @return Un objeto <code>Answer</code> con la conexion abierta y un <code>ResultSet</code> con los datos del usuario
	 */
	public Answer cargarUsuario (Connection con, String login) throws SQLException;

	/**
	 * Dado el login de un usuario, modifica sus datos. Usa una conexion abierta.
	 * Notese que no se cambia ni el login ni el password del usuario en el sistema.
	 * @param con una conexion abierta con una fuente de datos
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
	 * @param activarUsuario 
	 * @param j 
	 * @param i 
	 * @param código del centro de costo del usuario
	 * @return numero de usuarios modificados
	 */
	public int modificarUsuario (Connection con, String [] rolesUsuario, String login, String codCargo, int codigoUsuario, String numeroIdentificacion, String codigoTipoIdentificacion, String codigoPaisIdentificacion, String codigoDepartamentoIdentificacion, String codigoCiudadIdentificacion, String codigoTipoPersona, String diaNacimiento, String mesNacimiento, String anioNacimiento, String codigoEstadoCivil, String codigoSexo, String primerNombrePersona, String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String direccion, String codigoPais, String codigoDepartamento, String codigoCiudad, String codigoBarrio, String telefono, String email, HashMap centrosCosto, String contratoInterfaz, String activarUsuario, int diasInactivarUsuario,int diasCaducidadPWD,String usuarioModifica) throws SQLException;

	/**
	 * Lista los roles disponibles en el sistema.
	 * @param con una conexion abierta con la fuente de datos
	 * @return el HTML que muestra los roles disponibles del sistema con
	 * excepcion del de superAdministrador (no debe aparecerle al usuario)
	 */
	public String listarRoles (Connection con) throws SQLException;

	/**
	 * Lista los roles disponibles en el sistema, con los roles a los que pertenece el usuario elegidos (<i>checked</i>)
	 * @param con una conexion abierta con la fuente de datos
	 * @param rolesUsuario roles a los cuales pertenece el usuario
	 * @return el HTML que muestra los roles disponibles del sistema
	 */
	public String listarRolesElegidos (Connection con, String [] rolesUsuario) throws SQLException;

	/**
	 * Método que permite desactivar un usuario dado su login
	 * @param con una conexion abierta con la fuente de datos
	 * @param login del usuario que desea ser eliminado
	 * @param codigoInstitucion código de la institución en la cual se va a desactivar al usuario
     * @return número de usuarios desactivados
	 */
	public  int desactivarUsuario (Connection con, String login, String codigoInstitucion,String usuActual) throws SQLException  ;

	/**
	 * Método que permite desactivar un usuario dada su identificación
	 * @param con una conexion abierta con la fuente de datos
	 * @param tipoId  Tipo de identificacion del usuario que se desea desactivar
	 * @param numeroId  Numero de identificacion del usuario que se desea desactivar
	 * @param codigoInstitucion código de la institución en la cual se va a desactivar al usuario
	 * @return número de usuarios desactivados
	 */
	public  int desactivarUsuario (Connection con, String tipoId, String numeroId, String codigoInstitucion,String usuActual) throws SQLException;

	/**
	 * Método que permite activar un usuario dado su login
	 * @param con una conexion abierta con la fuente de datos
	 * @param login del usuario que desea ser activado
	 * @param codigoInstitucion código de la institución en la cual se va a activar al usuario
	 * @return Número de usuarios activados
	 */
	public int activarUsuario (Connection con, String login, String codigoInstitucion,String usuActual) throws SQLException;

	/**
	 * Método que permite activar un usuario dada su identificación
	 * @param con una conexion abierta con la fuente de datos
	 * @param tipoId  Tipo de identificacion del usuario que se desea activar
	 * @param numeroId  Numero de identificacion del usuario que se desea activar
	 * @param codigoInstitucion código de la institución en la cual se va a activar al usuario
	 * @return Número de usuarios activados
	 */
	public  int activarUsuario (Connection con, String tipoId, String numeroId, String codigoInstitucion,String usuActual) throws SQLException;

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
	public String buscarLogin(Connection con, String tipoId, String numeroId,String activo) throws SQLException ;

	/**
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
	public String buscarLogin(Connection con, int codigoMedico) throws SQLException;
	
	
	/**
	 * Método implementado para cargar los centros de costo del usuario
	 * @param con
	 * @param login
	 * @return
	 */
	public HashMap cargarCentrosCostoUsuario(Connection con,String login);
	

}