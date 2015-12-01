package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.ResultadoBoolean;

import com.princetonsa.dao.AntecedentesMorbidosDao;
import com.princetonsa.dao.sqlbase.SqlBaseAntecedentesMorbidosDao;

/**
 * Implemetacion para postgres de la interfaz para acceder a la fuente de datos,
 * la parte de Antecedentes Mórbidos
 *
 * @version 1.0, Agosto 15, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class PostgresqlAntecedentesMorbidosDao implements AntecedentesMorbidosDao
{
	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#existenAntecedentes(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean existenAntecedentes(Connection con, int codigoPaciente)
	{
			return SqlBaseAntecedentesMorbidosDao.existenAntecedentes(con,codigoPaciente);
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#insertarAntecedenteGeneral(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean insertarAntecedenteGeneral(Connection con, int codigoPaciente)
	{
		return SqlBaseAntecedentesMorbidosDao.insertarAntecedenteGeneral(con,codigoPaciente);
	}
	
	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#existenAntecedentesMorbidos(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean existenAntecedentesMorbidos( Connection con, int codigoPaciente)
	{
		return SqlBaseAntecedentesMorbidosDao.existenAntecedentesMorbidos(con,codigoPaciente);
	}
	
	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#existenAntecedentesMorbidos(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public ResultSetDecorator consultarAntecedentesMorbidos( Connection con, int codigoPaciente)
	{
		return SqlBaseAntecedentesMorbidosDao.consultarAntecedentesMorbidos(con,codigoPaciente);
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#insertar(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean insertar(Connection con, int codigoPaciente, String observaciones)
	{
		return SqlBaseAntecedentesMorbidosDao.insertar(con,codigoPaciente,observaciones);
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#insertarTransaccional(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean insertarTransaccional(Connection con, int codigoPaciente, String observaciones, String estado) throws SQLException
	{
	    return SqlBaseAntecedentesMorbidosDao.insertarTransaccional(con, codigoPaciente, observaciones, estado) ;
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#modificar(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean modificar(Connection con, int codigoPaciente, String observaciones)
	{
			return SqlBaseAntecedentesMorbidosDao.modificar(con,codigoPaciente,observaciones);
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#modificarTransaccional(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean modificarTransaccional(Connection con,	int codigoPaciente, String observaciones, String estado) throws SQLException
	{
	    return SqlBaseAntecedentesMorbidosDao.modificarTransaccional(con,	codigoPaciente, observaciones, estado) ;
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#cargarMorbidosMedicosPredefinidos(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public ResultSetDecorator cargarMorbidosMedicosPredefinidos(Connection con, int codigoPaciente)
	{
		return SqlBaseAntecedentesMorbidosDao.cargarMorbidosMedicosPredefinidos(con,codigoPaciente);
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#cargarMorbidosMedicosOtros(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public ResultSetDecorator cargarMorbidosMedicosOtros(Connection con, int codigoPaciente)
	{
		return SqlBaseAntecedentesMorbidosDao.cargarMorbidosMedicosOtros(con,codigoPaciente);
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMorbidosDao#cargarMorbidosQuirurgicos(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public ResultSetDecorator cargarMorbidosQuirurgicos(Connection con, int codigoPaciente)
	{
		return SqlBaseAntecedentesMorbidosDao.cargarMorbidosQuirurgicos(con,codigoPaciente);
	}
}
