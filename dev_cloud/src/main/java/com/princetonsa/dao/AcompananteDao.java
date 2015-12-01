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
	 * Este metodo inserta dos cosas, un acompañante y una pareja
	 * acompañante/paciente en las tablas respectivas. Internamente, debe
	 * verificar los casos en los que debe o no debe insertar los datos de
	 * acompañante, actualizarlos, sólo insertarlos en la tabla de acompañantes-
	 * pacientes, etc.
	 * @param con una conexión abierta con una fuente de datos
	 * @param numeroIdentificacionPaciente número de identificación del paciente
	 * @param codigoTipoIdentificacionPaciente código de la identificación del
	 * paciente
	 * @param numeroIdentificacionAcompanante número de identificación del
	 * acompañante
	 * @param codigoTipoIdentificacionAcompanante código de la identificación
	 * del acompañante
	 * @param nombreAcompanante nombre del acompañante
	 * @param apellidoAcompanante nombre del acompañante
	 * @param direccionAcompanante dirección del acompañante
	 * @param telefonoAcompanante teléfono del acompañante
	 * @param relacionAcompanantePaciente relación del acompañante con el
	 * paciente (amigo/a, novio/a, etc.)
	 * @return 0 si no se pudo efectuar ninguna inserción; 1 si se insertó un
	 * acompañante en la tabla de acompañantes y/o una pareja en la tabla de
	 * pacientes-acompañantes Y el acompañante tenía un documento de
	 * identificación; o el número de identificación asignado por el sistema si
	 * se insertó un acompañante en la tabla de acompañantes y/o una pareja en
	 * la tabla de pacientes-acompañantes Y el acompañante era un Menor Sin
	 * Identificación (MS) o un Adulto Sin Identificación (AS).
	 */
	public int insertarAcompanante(Connection con, int codigoPaciente, String numeroIdentificacionAcompanante, String codigoTipoIdentificacionAcompanante, String nombreAcompanante, String apellidoAcompanante, String direccionAcompanante, String telefonoAcompanante, String relacionAcompanantePaciente) throws SQLException;

	/**
	 * Este metodo carga un acompañante desde la fuente de datos, dado su tipo y
	 * número de identificación.
	 * @param con una conexión abierta con una fuente de datos
	 * @param codigoTipoIdentificacionAcompanante código de la identificación
	 * del acompañante
	 * @param numeroIdentificacionAcompanante número de identificación del
	 * acompañante
	 * @return <code>ResultSet</code> con los datos del acompañante
	 */
	public ResultSetDecorator cargarAcompanante(Connection con, String codigoTipoIdentificacionAcompanante, String numeroIdentificacionAcompanante) throws SQLException;

	public ResultSetDecorator cargarAcompananteDadoPaciente (Connection con, int codigoPaciente) throws SQLException;
	/**
	 * Dice si ya existe un acompañante en la base de datos con los mismos datos
	 * de acompañante que se pasan como parámetro.
	 * @param con una conexión abierta con una fuente de datos
	 * @param codigoTipoIdentificacionAcompanante código de la identificación
	 * del acompañante
	 * @param numeroIdentificacionAcompanante número de identificación del
	 * acompañante
	 * @return <b>true</b> si ya existe el acompañante, <b>false</b> si no
	 */
	public boolean existeAcompanante (Connection con, String codigoTipoIdentificacionAcompanante, String numeroIdentificacionAcompanante) throws SQLException;

	/**
	 * Dice si ya existe una pareja acompañante-paciente en la base de datos con
	 * los  mismos códigos y tipos de identificación de acompañante y de
	 * paciente que se pasan como parámetro.
	 * @param con una conexión abierta con una fuente de datos
	 * @param codigoTipoIdentificacionAcompanante código de la identificación
	 * del acompañante
	 * @param numeroIdentificacionAcompanante número de identificación del
	 * acompañante
	 * @param codigoTipoIdentificacionPaciente código de la identificación del
	 * paciente
	 * @param numeroIdentificacionPaciente número de identificación del paciente
	 * @return <b>true</b> si ya existe esta pareja acompañante-paciente en la
	 * base de datos, <b>false</b> si no
	 */
	public boolean existeAcompanantePaciente (Connection con, String codigoTipoIdentificacionAcompanante, String numeroIdentificacionAcompanante, int codigoPaciente) throws SQLException;

	/**
	 * 
	 * @param con una conexión abierta con una fuente de datos
	 * @param codigoTipoIdentificacionAcompanante código de la identificación
	 * del acompañante
	 * @param numeroIdentificacionAcompanante número de identificación del
	 * acompañante
	 * @param codigoTipoIdentificacionPaciente código de la identificación del
	 * paciente
	 * @param numeroIdentificacionPaciente número de identificación del paciente
	 * @return encapsula el resultado de la consulta: dice si se puede o no
	 * insertar el acompañante, y los motivos.
	 */
	public RespuestaValidacion validacionAcompanante(Connection con, String codigoTipoIdentificacionAcompanante, String numeroIdentificacionAcompanante, int codigoPaciente) throws SQLException;
	public ResultSetDecorator getAcompanantesPaciente(Connection con, int codigoPaciente) throws SQLException;
}