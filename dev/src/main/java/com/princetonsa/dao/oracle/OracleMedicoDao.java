/*
 * @(#)OracleMedicoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import util.Answer;

import com.princetonsa.dao.MedicoDao;
import com.princetonsa.dao.sqlbase.SqlBaseMedicoDao;
import com.princetonsa.mundo.Especialidades;

/**
 * Esta clase implementa el contrato estipulado en <code>MedicoDao</code>, proporcionando los servicios
 * de acceso a una base de datos Oracle requeridos por la clase <code>Medico</code>
 *
 * @version 1.0, Sep 22, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class OracleMedicoDao implements MedicoDao {

	/**
	 * Inserta un medico en una base de datos Oracle. Usa una conexion abierta, y la cierra
	 * despues de ejecutarse. Si no esta abierta, la crea.
	 * @param con una conexion abierta con una base de datos Oracle
	 * @param especialidades arreglo con los codigos de las especialidades del medico
	 * @param codigoOcupacionMedica codigo de la ocupaci�n m�dica del profesional de la salud
	 * @param diaVinculacion dia de vinculacion del medico
	 * @param mesVinculacion mes de vinculacion del medico
	 * @param anioVinculacion a�o de vinculacion del medico
	 * @param codigoTipoVinculacion codigo con el tipo de vinculacion del medico
	 * @param numeroIdentificacion numero de identificacion del medico
	 * @param codigoTipoIdentificacion codigo con el tipo de identificacion del medico
	 * @param codigoDeptoId
	 * @param codigoCiudadId
	 * @param codigoDepartamentoIdentificacion codigo del departamento donde fue expedido el numero de identificacion del medico
	 * @param codigoCiudadIdentificacion codigo de la ciudad donde fue expedido el numero de identificacion del medico
	 * @param codigoTipoPersona codigo del tipo de persona (Natural o Juridica)
	 * @param diaNacimiento dia de nacimiento del medico
	 * @param mesNacimiento mes de nacimiento del medico
	 * @param anioNacimiento a�o de nacimiento del medico
	 * @param codigoEstadoCivil codigo del estado civil del medico
	 * @param codigoSexo codigo del sexo del medico
	 * @param primerNombrePersona primer nombre del medico
	 * @param segundoNombrePersona segundo nombre del medico
	 * @param primerApellidoPersona primer apellido del medico
	 * @param segundoApellidoPersona segundo apellido del medico
	 * @param direccion direccion de la residencia del medico
	 * @param codigoDepartamento codigo del departamento donde reside el medico
	 * @param codigoCiudad codigo de la ciudad donde reside el medico
	 * @param codigoBarrio codigo del barrio de residencia del medico
	 * @param telefono telefono del medico
	 * @param email correo electronico del medico
	 * @param numeroRegistro numero de registro del medico
	 * @param codigoDeptoRegistro
	 * @param codigoCiudadRegistro
	 * @return numero de filas insertadas
	 */
	public int insertarMedico (Connection con, Especialidades especialidades, String codigoCategoria, String diaVinculacion, String mesVinculacion, String anioVinculacion, String codigoTipoVinculacion,  String numeroIdentificacion, String codigoTipoIdentificacion, String codigoDeptoId, String codigoCiudadId, String codigoPaisId, String codigoDepartamentoIdentificacion, String codigoCiudadIdentificacion, String codigoPaisIdentificacion, String codigoTipoPersona, String diaNacimiento, String mesNacimiento, String anioNacimiento, String codigoEstadoCivil, String codigoSexo, String primerNombrePersona, String segundoNombrePersona, String primerApellidoPersona, String segundoApellidoPersona, String direccion, String codigoDepartamento, String codigoCiudad, String codigoPais, String codigoBarrio, String telefono, String email, String numeroRegistro,String codigoDeptoRegistro,String codigoCiudadRegistro, String codigoPaisRegistro, String firmaDigital, int convencion, String tipoLiquidacion, String telefonoFijo, String telefonoCelular) throws SQLException 
	{
	    return SqlBaseMedicoDao.insertarMedico (con, especialidades, codigoCategoria, diaVinculacion, mesVinculacion, anioVinculacion, codigoTipoVinculacion, numeroIdentificacion, codigoTipoIdentificacion, codigoDeptoId, codigoCiudadId, codigoPaisId, codigoDepartamentoIdentificacion, codigoCiudadIdentificacion, codigoPaisIdentificacion, codigoTipoPersona, diaNacimiento, mesNacimiento, anioNacimiento, codigoEstadoCivil, codigoSexo, primerNombrePersona, segundoNombrePersona, primerApellidoPersona, segundoApellidoPersona, direccion, codigoDepartamento, codigoCiudad,codigoPais, codigoBarrio, telefono, email, numeroRegistro,codigoDeptoRegistro,codigoCiudadRegistro,codigoPaisRegistro,firmaDigital, convencion, tipoLiquidacion,telefonoFijo,telefonoCelular) ;
	}

	/**
	 * Dados el numero y tipo de identificacion, retorna los datos de un medico. Reutiliza una
	 * conexion abierta.
	 * @param con una conexion abierta con una base de datos Oracle
	 * @param tipoId codigo de la identificacion del medico
	 * @param numeroId numero de identificacion del medico
	 * @return un objeto <code>Answer</code> con una conexion abierta y los datos del medico
	 */
	public Answer cargarMedico (Connection con, int codigoMedico) throws SQLException {

		return SqlBaseMedicoDao.cargarMedico(con, codigoMedico);
	}

	/**
	 * Dados el numero y tipo de identificacion, cambia los datos de un medico. Usa una conexion abierta,
	 * si no esta abierta, la crea.
	 * @param con una conexion abierta con una base de datos Oracle
	 * @param tipoId codigo de la identificacion del medico
	 * @param numeroId numero de identificacion del medico
	 * @param especialidades arreglo con los codigos de las especialidades del medico
	 * @param codigoCategoria codigo con la categoria del medico
	 * @param diaVinculacion dia de vinculacion del medico
	 * @param mesVinculacion mes de vinculacion del medico
	 * @param anioVinculacion a�o de vinculacion del medico
	 * @param codigoTipoVinculacion codigo con el tipo de vinculacion del medico
	 * @param codigoDepartamentoIdentificacion codigo del departamento donde fue expedido el numero de identificacion del medico
	 * @param codigoCiudadIdentificacion codigo de la ciudad donde fue expedido el numero de identificacion del medico
	 * @param codigoTipoPersona codigo del tipo de persona (Natural o Juridica)
	 * @param diaNacimiento dia de nacimiento del medico
	 * @param mesNacimiento mes de nacimiento del medico
	 * @param anioNacimiento a�o de nacimiento del medico
	 * @param codigoEstadoCivil codigo del estado civil del medico
	 * @param codigoSexo codigo del sexo del medico
	 * @param primerNombrePersona primer nombre del medico
	 * @param segundoNombrePersona segundo nombre del medico
	 * @param primerApellidoPersona primer apellido del medico
	 * @param segundoApellidoPersona segundo apellido del medico
	 * @param direccion direccion de la residencia del medico
	 * @param codigoDepartamento codigo del departamento donde reside el medico
	 * @param codigoCiudad codigo de la ciudad donde reside el medico
	 * @param codigoBarrio codigo del barrio de residencia del medico
	 * @param telefono telefono del medico
	 * @param email correo electronico del medico
	 * @param numeroRegistro numero de registro del medico
	 * @param codigoDeptoRegistro
	 * @param codigoCiudadREgistro
	 * @return el numero de filas modificadas (0 si no se pudo modificar)
	 */
	public int modificarMedico (Connection con, int codigoMedico, Especialidades  especialidades, String codigoCategoria, String diaVinculacion, String mesVinculacion, String anioVinculacion, String codigoTipoVinculacion,  String numeroRegistro, String codigoDeptoRegistro, String codigoCiudadRegistro, String codigoPaisRegistro,String firmaDigital, int convencion, String tipoLiquidacion) throws SQLException 
	{
		return SqlBaseMedicoDao.modificarMedico(con, codigoMedico, especialidades, codigoCategoria, diaVinculacion, mesVinculacion, anioVinculacion, codigoTipoVinculacion,  numeroRegistro,codigoDeptoRegistro,codigoCiudadRegistro, codigoPaisRegistro,firmaDigital, convencion, tipoLiquidacion);
	}

	/**
	 * Dados el n�mero y tipo de identificaci�n de un m�dico, lo desactiva
	 * @param con una conexion abierta con la BD Oracle
	 * @param tipoId codigo de la identificacion del medico
	 * @param numeroId numero de identificacion del medico
	 * @param codigoInstitucion c�digo de la instituci�n de la que se va a
	 * desactivar el m�dico
	 * @return int 1 si pudo desactivar el m�dico, 0 si no pudo desactivarlo
	 */
	public int desactivarMedico (Connection con, int codigoMedico, String codigoInstitucion) throws SQLException
	{
	    return SqlBaseMedicoDao.desactivarMedico (con, codigoMedico, codigoInstitucion);
	}

	/**
	 * Dados el n�mero y tipo de identificaci�n de un m�dico, lo activa
	 * @param con una conexion abierta con la BD Oracle
	 * @param tipoId codigo de la identificacion del medico
	 * @param numeroId numero de identificacion del medico
	 * @param codigoInstitucion c�digo de la instituci�n de la que se va a
	 * desactivar el m�dico
	 * @return int 1 si pudo activar el m�dico, 0 si no pudo activarlo
	 */
	public int activarMedico (Connection con, int codigoMedico, String codigoInstitucion) throws SQLException
	{
	    return SqlBaseMedicoDao.activarMedico (con, codigoMedico, codigoInstitucion) ;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
	public String obtenerFirmaDigitalMedico(Connection con, int codigoMedico)
	{
		return SqlBaseMedicoDao.obtenerFirmaDigitalMedico(con, codigoMedico);
	}

	
	/**
	 * 
	 * @param codigoMedico
	 * @return
	 */
	public String obtenerTipoLiquidacionPool(int codigoMedico)
	{
		return SqlBaseMedicoDao.obtenerTipoLiquidacionPool( codigoMedico);
	}
}