/*
 * @(#)PostgresqlCostoViaIngresoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import com.princetonsa.dao.CentrosCostoViaIngresoDao;
import com.princetonsa.dao.sqlbase.SqlBaseCentrosCostoViaIngresoDao;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 15 /May/ 2006
 */
public class PostgresqlCentrosCostoViaIngresoDao implements CentrosCostoViaIngresoDao 
{
	/**
	 * Cadena con el statement necesario para insertar un centro de costo x via de ingreso
	 */
	private final static String insertarCentroCostoViaIngresoStr = " INSERT INTO centro_costo_via_ingreso " +
																  " (codigo, " +
																  " centro_costo, " +
																  " via_ingreso, " +
																  " tipo_paciente, " +
																  " institucion ) " +
																  " VALUES (nextval('seq_centro_costo_via_ingreso'), ?, ?, ?, ?)" ;
	
	
	
	/**
	 * Método para consultar los centros de cotso por via de ingreso
	 * segun la insitucion de los centros de costo
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarCentrosCostoViaIngreso(Connection con, int institucion, int centroAtencion) 
	{
		return SqlBaseCentrosCostoViaIngresoDao.consultarCentrosCostoViaIngreso(con, institucion, centroAtencion);
	}
	
	
	/**
	 * Método para eliminar un cento ed costo x via de ingreso
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public int eliminarCentroCostoViaIngreso(Connection con, int codigo) throws SQLException
	{
		return SqlBaseCentrosCostoViaIngresoDao.eliminarCentroCostoViaIngreso(con, codigo);
	}
	
	
	/**
	 * Método para modificar un centro de costo x via de ingreso
	 * @param con
	 * @param codigoCentroCosto
	 * @param codigoViaIngreso
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public int modificarCentroCostoViaIngreso(Connection con, int codigoCentroCosto, int codigoViaIngreso, int codigo, int insitucion, String tipopaciente) throws SQLException
	{
		return SqlBaseCentrosCostoViaIngresoDao.modificarCentroCostoViaIngreso(con, codigoCentroCosto, codigoViaIngreso, codigo, insitucion, tipopaciente);
	}
	
	
	/**
	 * Método para insertar un nuevo centro de costo.
	 * Si existe lo que hace es modificarlo de lo contrario lo Inserta
	 * @param con
	 * @param codigo
	 * @param codigoCentroCosto
	 * @param codigoViaIngreso
	 * @param insertarCentroCostoViaIngresoStr
	 * @return
	 * @throws SQLException
	 */
	public int insertarCentrosCostoViaIngreso(Connection con, int codigo, int codigoCentroCosto, int codigoViaIngreso, int institucion, String tipopaciente) throws SQLException
	{
		return SqlBaseCentrosCostoViaIngresoDao.insertarCentrosCostoViaIngreso(con, codigo, codigoCentroCosto, codigoViaIngreso, institucion, insertarCentroCostoViaIngresoStr, tipopaciente);
	}
}