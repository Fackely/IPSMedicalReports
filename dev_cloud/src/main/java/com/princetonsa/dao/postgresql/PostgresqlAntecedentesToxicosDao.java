/*
 * @(#)PostgresqlAntecedentesToxicosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.ResultadoBoolean;

import com.princetonsa.dao.AntecedentesToxicosDao;
import com.princetonsa.dao.sqlbase.SqlBaseAntecedentesToxicosDao;


/**
 * Implemetacion para postgres de la interfaz para acceder a la fuente de datos,
 * la parte de Antecedentes Tóxicos
 *
 * @version 2.0, Noviembre 28, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class PostgresqlAntecedentesToxicosDao implements AntecedentesToxicosDao
{
	/**
	 * @see com.princetonsa.dao.AntecedentesToxicosDao#existenAntecedentes(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean existenAntecedentes(Connection con, int codigoPaciente)
	{
		return SqlBaseAntecedentesToxicosDao.existenAntecedentes(con,codigoPaciente);
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesToxicosDao#insertarAntecedenteGeneral(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean insertarAntecedenteGeneral(Connection con, int codigoPaciente)
	{
		return SqlBaseAntecedentesToxicosDao.insertarAntecedenteGeneral(con,codigoPaciente);
	}
	
	/**
	 * @see com.princetonsa.dao.AntecedentesToxicosDao#existenAntecedentesTo
	 * xicos(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean existenAntecedentesToxicos( Connection con, int codigoPaciente )
	{
		return SqlBaseAntecedentesToxicosDao.existenAntecedentesToxicos(con,codigoPaciente);
	}
	
	/**
	 * @see com.princetonsa.dao.
	 * AntecedentesToxicosDao#existenAntecedentesToxicos (java.sql.Connection,
	 * java.lang.String, java.lang.String)
	 */
	public ResultSetDecorator consultarAntecedentesToxicos( Connection con, int codigoPaciente)
	{
		return SqlBaseAntecedentesToxicosDao.consultarAntecedentesToxicos(con,codigoPaciente);
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesToxicosDao#insertar(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean insertar(Connection con, int codigoPaciente, String observaciones)
	{
		return SqlBaseAntecedentesToxicosDao.insertar(con,codigoPaciente,observaciones);
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesToxicosDao#insertarTransaccional(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean insertarTransaccional(Connection con, int codigoPaciente, String observaciones, String estado) throws SQLException
	{
	    return SqlBaseAntecedentesToxicosDao.insertarTransaccional(con, codigoPaciente, observaciones, estado) ;
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesToxicosDao#modificar(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean modificar(Connection con, int codigoPaciente, String observaciones)
	{
		return SqlBaseAntecedentesToxicosDao.modificar(con,codigoPaciente,observaciones);
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesToxicosDao#modificarTransaccional(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean modificarTransaccional(Connection con, int codigoPaciente, String observaciones, String estado) throws SQLException
	{
	    return SqlBaseAntecedentesToxicosDao.modificarTransaccional(con, codigoPaciente, observaciones, estado) ;
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesToxicosDao#cargarToxicosPredefinidos
	 * (java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public ResultSetDecorator cargarToxicosPredefinidos(Connection con, int codigoPaciente)
	{
		return SqlBaseAntecedentesToxicosDao.cargarToxicosPredefinidos(con,codigoPaciente);
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesToxicosDao#cargarToxicosOtros(java.
	 * sql. Connection, java.lang.String, java.lang.String)
	 */
	public ResultSetDecorator cargarToxicosOtros(Connection con, int codigoPaciente)
	{
		return SqlBaseAntecedentesToxicosDao.cargarToxicosOtros(con,codigoPaciente);
	}
}
