/*
 * @(#)AcompananteDao.java
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

import util.RespuestaValidacion;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>Acompanante</code>.
 *
 * @version 1.0, Nov 5, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public interface AcompananteDao {

	/**
	 * Este metodo inserta dos cosas, un acompa�ante y una pareja
	 * acompa�ante/paciente en las tablas respectivas. Internamente, debe
	 * verificar los casos en los que debe o no debe insertar los datos de
	 * acompa�ante, actualizarlos, s�lo insertarlos en la tabla de acompa�antes-
	 * pacientes, etc.
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param numeroIdentificacionPaciente n�mero de identificaci�n del paciente
	 * @param codigoTipoIdentificacionPaciente c�digo de la identificaci�n del
	 * paciente
	 * @param numeroIdentificacionAcompanante n�mero de identificaci�n del
	 * acompa�ante
	 * @param codigoTipoIdentificacionAcompanante c�digo de la identificaci�n
	 * del acompa�ante
	 * @param nombreAcompanante nombre del acompa�ante
	 * @param apellidoAcompanante nombre del acompa�ante
	 * @param direccionAcompanante direcci�n del acompa�ante
	 * @param telefonoAcompanante tel�fono del acompa�ante
	 * @param relacionAcompanantePaciente relaci�n del acompa�ante con el
	 * paciente (amigo/a, novio/a, etc.)
	 * @return 0 si no se pudo efectuar ninguna inserci�n; 1 si se insert� un
	 * acompa�ante en la tabla de acompa�antes y/o una pareja en la tabla de
	 * pacientes-acompa�antes Y el acompa�ante ten�a un documento de
	 * identificaci�n; o el n�mero de identificaci�n asignado por el sistema si
	 * se insert� un acompa�ante en la tabla de acompa�antes y/o una pareja en
	 * la tabla de pacientes-acompa�antes Y el acompa�ante era un Menor Sin
	 * Identificaci�n (MS) o un Adulto Sin Identificaci�n (AS).
	 */
	public int insertarAcompanante(Connection con, int codigoPaciente, String numeroIdentificacionAcompanante, String codigoTipoIdentificacionAcompanante, String nombreAcompanante, String apellidoAcompanante, String direccionAcompanante, String telefonoAcompanante, String relacionAcompanantePaciente) throws SQLException;

	/**
	 * Este metodo carga un acompa�ante desde la fuente de datos, dado su tipo y
	 * n�mero de identificaci�n.
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param codigoTipoIdentificacionAcompanante c�digo de la identificaci�n
	 * del acompa�ante
	 * @param numeroIdentificacionAcompanante n�mero de identificaci�n del
	 * acompa�ante
	 * @return <code>ResultSet</code> con los datos del acompa�ante
	 */
	public ResultSetDecorator cargarAcompanante(Connection con, String codigoTipoIdentificacionAcompanante, String numeroIdentificacionAcompanante) throws SQLException;

	public ResultSetDecorator cargarAcompananteDadoPaciente (Connection con, int codigoPaciente) throws SQLException;
	/**
	 * Dice si ya existe un acompa�ante en la base de datos con los mismos datos
	 * de acompa�ante que se pasan como par�metro.
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param codigoTipoIdentificacionAcompanante c�digo de la identificaci�n
	 * del acompa�ante
	 * @param numeroIdentificacionAcompanante n�mero de identificaci�n del
	 * acompa�ante
	 * @return <b>true</b> si ya existe el acompa�ante, <b>false</b> si no
	 */
	public boolean existeAcompanante (Connection con, String codigoTipoIdentificacionAcompanante, String numeroIdentificacionAcompanante) throws SQLException;

	/**
	 * Dice si ya existe una pareja acompa�ante-paciente en la base de datos con
	 * los  mismos c�digos y tipos de identificaci�n de acompa�ante y de
	 * paciente que se pasan como par�metro.
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param codigoTipoIdentificacionAcompanante c�digo de la identificaci�n
	 * del acompa�ante
	 * @param numeroIdentificacionAcompanante n�mero de identificaci�n del
	 * acompa�ante
	 * @param codigoTipoIdentificacionPaciente c�digo de la identificaci�n del
	 * paciente
	 * @param numeroIdentificacionPaciente n�mero de identificaci�n del paciente
	 * @return <b>true</b> si ya existe esta pareja acompa�ante-paciente en la
	 * base de datos, <b>false</b> si no
	 */
	public boolean existeAcompanantePaciente (Connection con, String codigoTipoIdentificacionAcompanante, String numeroIdentificacionAcompanante, int codigoPaciente) throws SQLException;

	/**
	 * 
	 * @param con una conexi�n abierta con una fuente de datos
	 * @param codigoTipoIdentificacionAcompanante c�digo de la identificaci�n
	 * del acompa�ante
	 * @param numeroIdentificacionAcompanante n�mero de identificaci�n del
	 * acompa�ante
	 * @param codigoTipoIdentificacionPaciente c�digo de la identificaci�n del
	 * paciente
	 * @param numeroIdentificacionPaciente n�mero de identificaci�n del paciente
	 * @return encapsula el resultado de la consulta: dice si se puede o no
	 * insertar el acompa�ante, y los motivos.
	 */
	public RespuestaValidacion validacionAcompanante(Connection con, String codigoTipoIdentificacionAcompanante, String numeroIdentificacionAcompanante, int codigoPaciente) throws SQLException;
	public ResultSetDecorator getAcompanantesPaciente(Connection con, int codigoPaciente) throws SQLException;
}