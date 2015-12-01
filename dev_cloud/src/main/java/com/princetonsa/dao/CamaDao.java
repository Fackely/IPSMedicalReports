/*
 * @(#)CamaDao.java
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

import util.Answer;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>Cama</code>.
 *
 * @version 1.0, Oct 30, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public interface CamaDao {

	/**
	 * Inserta una cama en una fuente de datos, reutilizando una conexion existente.
	 * @param con una conexion abierta con una fuente de datos
	 * @param numeroCama número asignado a la cama
	 * @param estado estado de la cama
	 * @param codigoCentroCosto código del centro de costo al cual pertenece la cama
	 * @param codigoTipoUsuario código del tipo de usuario que va a usar la cama
	 * @return número de camas insertadas
	 */
	public int insertarCama (Connection con, String numeroCama, int estado, String codigoCentroCosto, String codigoTipoUsuario) throws SQLException;

	/**
	 * Dada la identificacion de una cama, carga los datos correspondientes desde la fuente de datos.
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoCama el código de la cama que se desea cargar
	 * @return un <code>Answer</code> con los datos pedidos y una conexión abierta con la fuente de datos
	 */
	public Answer cargarCama (Connection con, String codigoCama) throws SQLException;

	/**
	 * Modifica una cama en una fuente de datos, reutilizando una conexion existente.
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoCama el código de la cama que desea modificar
	 * @param numeroCama número asignado a la cama
	 * @param estado estado de la cama
	 * @param codigoCentroCosto código del centro de costo al cual pertenece la cama
	 * @param codigoTipoUsuario código del tipo de usuario que va a usar la cama
	 * @return numero de camas modificadas
	 */
	public int modificarCama (Connection con, String codigoCama, String numeroCama, int estado, String codigoCentroCosto, String codigoTipoUsuario) throws SQLException;
	
	/**
	 * Cambia el estado de la cama en la base de datos.
	 * @param	estado, nuevo estado de la cama
	 * @param	codigoCama, código de la cama que desea modificar
	 * @return	numero de camas modificadas
	 */
	public int cambiarEstadoCama(Connection con, String codigoCama, int estado) throws SQLException;
	
	
	public String[] getFechaHoraUltimoUsoCama(Connection con, int codCama) throws SQLException;
}