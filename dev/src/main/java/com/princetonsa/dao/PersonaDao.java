/*
 * @(#)PersonaDao.java
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

import util.RespuestaInsercionPersona;

/**
 * Esta clase encapsula el funcionamiento comun en cuanto a BD de las clases
 * que extienden persona
 *
 * @version 1.0, Sep 21, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public interface PersonaDao {

	/**
	 * Dada una conexion abierta con una fuente de datos y los datos basicos de
	 * una persona, hace el insert fisico de la persona, (si no existe una fuente
	 * de datos, NO la crea para no tocar la transaccionalidad del resto de
	 * inserciones / modificaciones que la usen (medico, persona o usuario)
	 * @param con una conexion abierta con una fuente de datos
	 * @param numeroIdentificacion numero de identificacion de la persona
	 * @param codigoTipoIdentificacion codigo del tipo de identificacion de la persona
	 * @param codigoDeptoId
	 * @param codigoCiudadId
	 * @param codigoDepartamentoIdentificacion c�digo del departamento de expedici�n de la identificaci�n de la persona
	 * @param codigoCiudadIdentificacion c�digo de la ciudad de expedici�n de la identificaci�n de la persona
	 * @param codigoTipoPersona c�digo del tipo de persona
	 * @param diaNacimiento d�a de naciemiento de la persona
	 * @param mesNacimiento mes de naciemiento de la persona
	 * @param anioNacimiento a�o de naciemiento de la persona
	 * @param codigoEstadoCivil c�digo del estado civil de la persona
	 * @param codigoSexo c�digo del sexo de la persona
	 * @param primerNombrePersona primer nombre de la persona
	 * @param segundoNombrePersona segundo nombre de la persona
	 * @param primerApellidoPersona primer apellido de la persona
	 * @param segundoApellidoPersona segundo apellido de la persona
	 * @param direccion direcci�n de residencia de la persona
	 * @param codigoDepartamento c�digo del departamento de residencia de la persona
	 * @param codigoCiudad c�digo del departamento de residencia de la persona
	 * @param codigoBarrio c�digo del barrio de residencia de la persona
	 * @param telefono tel�fono de contacto de la persona
	 * @param email correo electr�nico de la persona
	 * @return un objeto <code>RespuestaInsercionPersona</code> con la informacion de la insercion, y los (posibles) mensajes de error
	*/
	public RespuestaInsercionPersona insertarPersona (Connection con, String numeroIdentificacion, String codigoTipoIdentificacion,String codigoDeptoId, String codigoCiudadId, String codigoPaisId, String codigoDepartamentoIdentificacion, String codigoCiudadIdentificacion, String codigoPaisIdentificacion, String codigoTipoPersona, String diaNacimiento, String mesNacimiento, String anioNacimiento, String codigoEstadoCivil, String codigoSexo, String primerNombrePersona, String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String direccion, String codigoDepartamento, String codigoCiudad, String codigoPais, String codigoBarrio,String codigoLocalidad, String telefono, String email, String telefonoCelular, String tipoPersona,int codigoInstitucion, String telefonoFijo) throws SQLException ;
	
	/**
	 * Dada una conexion abierta con una fuente de datos y los datos basicos de
	 * una persona, hace el insert fisico de la persona, (si no existe una fuente
	 * de datos, NO la crea para no tocar la transaccionalidad del resto de
	 * inserciones / modificaciones que la usen (medico, persona o usuario)
	 * @param con una conexion abierta con una fuente de datos
	 * @param numeroIdentificacion numero de identificacion de la persona
	 * @param codigoTipoIdentificacion codigo del tipo de identificacion de la persona
	 * @param codigoDeptoId
	 * @param codigoCiudadId
	 * @param codigoDepartamentoIdentificacion c�digo del departamento de expedici�n de la identificaci�n de la persona
	 * @param codigoCiudadIdentificacion c�digo de la ciudad de expedici�n de la identificaci�n de la persona
	 * @param codigoTipoPersona c�digo del tipo de persona
	 * @param fechaNacimiento fecha de naciemiento de la persona
	 * @param codigoEstadoCivil c�digo del estado civil de la persona
	 * @param codigoSexo c�digo del sexo de la persona
	 * @param primerNombrePersona primer nombre de la persona
	 * @param segundoNombrePersona segundo nombre de la persona
	 * @param primerApellidoPersona primer apellido de la persona
	 * @param segundoApellidoPersona segundo apellido de la persona
	 * @param direccion direcci�n de residencia de la persona
	 * @param codigoDepartamento c�digo del departamento de residencia de la persona
	 * @param codigoCiudad c�digo del departamento de residencia de la persona
	 * @param codigoBarrio c�digo del barrio de residencia de la persona
	 * @param telefono tel�fono de contacto de la persona
	 * @param email correo electr�nico de la persona
	 * @return un objeto <code>RespuestaInsercionPersona</code> con la informacion de la insercion, y los (posibles) mensajes de error
	*/
	public RespuestaInsercionPersona insertarPersona (Connection con, String numeroIdentificacion, String codigoTipoIdentificacion, String codigoDeptoId, String codigoCiudadId, String codigoPaisId, String codigoDepartamentoIdentificacion, String codigoCiudadIdentificacion, String codigoPaisIdentificacion, String codigoTipoPersona, String fechaNacimiento, String codigoEstadoCivil, String codigoSexo, String primerNombrePersona, String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String direccion, String codigoDepartamento, String codigoCiudad, String codigoPais, String codigoBarrio, String codigoLocalidad, String telefono, String email, String telefonoCelular, String tipoPersona,int codigoInstitucion, String telefonoFijo) throws SQLException ;
	

	/**
	* Modifica una persona en una base de datos PostgreSQL, ejecutando solamente una parte de la
	* transacci�n SQL
	* @param ac_con									Conexion abierta con una base de datos
	*												PostgreSQL
	* @param as_tipoId								Tipo de identificaci�n de la persona (c�dula,
	*												tarjeta de identidad, etc.)
	* @param as_numeroId							N�mero de la identificaci�n de la persona
	* @param codigoDeptoId							codigo del departamento de la identificacion
	* @param codigoCiudadId							codigo de la ciudad de la identificacion
	* @param ai_codigoPersona						Identificador interno de la persona
	* @param as_codigoDepartamentoIdentificacion	C�digo del departamento de expedici�n de la
	*												identificaci�n de la persona
	* @param as_codigoCiudadIdentificacion			C�digo de la ciudad de expedici�n de la
	*												identificaci�n de la persona
	* @param as_codigoTipoPersona					C�digo del tipo de persona
	* @param as_diaNacimiento						D�a de naciemiento de la persona
	* @param as_mesNacimiento						Mes de naciemiento de la persona
	* @param as_anioNacimiento						A�o de naciemiento de la persona
	* @param as_codigoEstadoCivil					C�digo del estado civil de la persona
	* @param as_codigoSexo							C�digo del sexo de la persona
	* @param as_primerNombrePersona					Primer nombre de la persona
	* @param as_segundoNombrePersona				Segundo nombre de la persona
	* @param as_primerApellidoPersona				Primer apellido de la persona
	* @param as_segundoApellidoPersona				Segundo apellido de la persona
	* @param as_direccion							Direcci�n de residencia de la persona
	* @param as_codigoDepartamento					C�digo del departamento de residencia de la
	*												persona
	* @param as_codigoCiudad						C�digo del departamento de residencia de la
	*												persona
	* @param as_codigoBarrio						C�digo del barrio de residencia de la persona
	* @param as_telefono							Tel�fono de contacto de la persona
	* @param as_email								Correo electr�nico de la persona
	* @param as_estado								Estado de la transacci�n que se quiere realizar
	* @return	1	Si la modificaci�n de la persona fue exitoso
	*			0	Error de base de datos
	*			-1	Si la combinaci�n tipo/n�mero de documento ya existe
	*			-2	Si la fecha de nacimiento es posterior a la actual
	*/
	public int modificarPersonaTransaccional(
		Connection	ac_con,
		String		as_tipoId,
		String		as_numeroId,
		String 		codigoDeptoId,
		String 		codigoCiudadId,
		String		codigoPaisId,
		int			ai_codigoPersona,
		String		as_codigoDepartamentoIdentificacion,
		String		as_codigoCiudadIdentificacion,
		String 		codigoPaisIdentificacion,
		String		as_codigoTipoPersona,
		String		as_diaNacimiento,
		String		as_mesNacimiento,
		String		as_anioNacimiento,
		String		as_codigoEstadoCivil,
		String		as_codigoSexo,
		String		as_primerNombrePersona,
		String		as_segundoNombrePersona,
		String		as_primerApellidoPersona,
		String		as_segundoApellidoPersona,
		String		as_direccion,
		String		as_codigoDepartamento,
		String		as_codigoCiudad,
		String		codigoPais,
		String		as_codigoBarrio,
		String 		codigoLocalidad,
		String		as_telefono,
		String		as_email, 
		String 		as_telefonoCelular,
		String		as_estado,
		String 		as_tipoPersona,
		int 		codigoInstitucion,
		String 		as_telefonoFijo
	)throws SQLException;
	
	/**
	 * Adici�n de Sebasti�n
	 * M�todo para obtener el c�digo de una persona que ya est� en el sistema
	 * a trav�s de su n�mero y tipo de identificaci�n
	 * @param con
	 * @param numeroId
	 * @param codigoTipoId
	 * @return
	 */
	public int obtenerCodigoPersona(Connection con,String numeroId, String codigoTipoId);
	
	/**
	 * M�todo para obtener apellidos nombres de una persona
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public String obtenerApellidosNombresPersona(Connection con,int codigoPersona);
	
	
	/**
	 * M�todo implementado para cargar los datos de la persona
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public HashMap<String, Object> cargarPersona(Connection con,int codigoPersona);
	
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return firma del medico
	 */
	public String obtenerFirmaDigitalMedico(Connection con,Integer numeroSolicitud);
}