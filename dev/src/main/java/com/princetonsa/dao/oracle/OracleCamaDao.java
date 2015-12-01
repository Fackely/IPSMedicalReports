/*
 * @(#)CamaDao.java
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
import com.princetonsa.dao.CamaDao;
import com.princetonsa.dao.sqlbase.SqlBaseCamaDao;

/**
 * Esta clase implementa el contrato estipulado en <code>CamaDao</code>, y presta los servicios de acceso a
 * una base de datos Oracle requeridos por la clase <code>Cama</code>.
 * 
 * @version 1.0, Oct 30, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class OracleCamaDao implements CamaDao{//SIN PROBAR FUNC. SECUENCIA

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar una cama en la base de datos Oracle.
	 */
	private static final String insertarCamaStr="INSERT INTO camas VALUES (seq_camas.nextval, ?, ?, ?, ?)";


	/**
	 * Inserta una cama en una base de datos Oracle, reutilizando una conexion existente.
	 * @param con una conexion abierta con una base de datos Oracle
	 * @param numeroCama número asignado a la cama
	 * @param estado estado de la cama
	 * @param codigoCentroCosto código del centro de costo al cual pertenece la cama
	 * @param codigoTipoUsuario código del tipo de usuario que va a usar la cama
	 * @return número de camas insertadas
	 */
	public int insertarCama (Connection con, String numeroCama, int estado, String codigoCentroCosto, String codigoTipoUsuario) throws SQLException
	{
	    return SqlBaseCamaDao.insertarCama (con, numeroCama, estado, codigoCentroCosto, codigoTipoUsuario, insertarCamaStr);
	}

	/**
	 * Dada la identificacion de una cama, carga los datos correspondientes desde la base de datos Oracle.
	 * @param con una conexion abierta con una base de datos Oracle
	 * @param codigoCama el código de la cama que se desea cargar
	 * @return un <code>Answer</code> con los datos pedidos y una conexión abierta con la base de datos Oracle
	 */
	public Answer cargarCama (Connection con, String codigoCama) throws SQLException
	{
		return SqlBaseCamaDao.cargarCama(con, codigoCama);
	}

	/**
	 * Modifica una cama en una base de datos Oracle, reutilizando una conexion existente.
	 * @param con una conexion abierta con una base de datos Oracle
	 * @param codigoCama el código de la cama que desea modificar
	 * @param numeroCama número asignado a la cama
	 * @param estado estado de la cama
	 * @param codigoCentroCosto código del centro de costo al cual pertenece la cama
	 * @param codigoTipoUsuario código del tipo de usuario que va a usar la cama
	 * @return numero de camas modificadas
	 */
	public int modificarCama (Connection con, String codigoCama, String numeroCama, int estado, String codigoCentroCosto, String codigoTipoUsuario) throws SQLException
	{
		return SqlBaseCamaDao.modificarCama(con, codigoCama, numeroCama, estado, codigoCentroCosto, codigoTipoUsuario);
	}
	
	/**
	 * Cambia el estado de la cama en la base de datos.
	 * @param	estado, nuevo estado de la cama
	 * @param	codigoCama, código de la cama que desea modificar 
 	 */
	public int cambiarEstadoCama(Connection con, String codigoCama, int estado) throws SQLException
	{
		return SqlBaseCamaDao.cambiarEstadoCama(con, codigoCama, estado);
	}
	
	public String[] getFechaHoraUltimoUsoCama(Connection con, int codCama) throws SQLException
	{
		return SqlBaseCamaDao.getFechaHoraUltimoUsoCama(con, codCama);
	}
}