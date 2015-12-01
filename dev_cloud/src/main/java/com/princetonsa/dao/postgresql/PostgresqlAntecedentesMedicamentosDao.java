package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import util.ResultadoBoolean;

import com.princetonsa.dao.AntecedentesMedicamentosDao;
import com.princetonsa.dao.sqlbase.SqlBaseAntecedentesMedicamentosDao;

/**
 * Implementación de la interfaz para el acceso a la base de datos de los
 * antecedentes a medicamentos para postgres.
 *
 * @version 1.0, Agosto 26, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class PostgresqlAntecedentesMedicamentosDao implements AntecedentesMedicamentosDao
{
	public ResultadoBoolean existenAntecedentes(Connection con, int codigoPaciente)
	{
			return SqlBaseAntecedentesMedicamentosDao.existenAntecedentes(con,codigoPaciente);
	}

	public ResultadoBoolean insertarAntecedenteGeneral(Connection con, int codigoPaciente)
	{
			return SqlBaseAntecedentesMedicamentosDao.insertarAntecedenteGeneral(con,codigoPaciente);
	}
	
	public ResultadoBoolean existenAntecedentesMedicamentos( Connection con, int codigoPaciente)
	{
			return SqlBaseAntecedentesMedicamentosDao.existenAntecedentesMedicamentos(con,codigoPaciente);
	}
	
	/**
	 * @see com.princetonsa.dao.AntecedentesMedicamentosDao#existenAntecedentesMedicamentos(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public ResultSetDecorator consultarAntecedentesMedicamentos( Connection con, int codigoPaciente)
	{
			return SqlBaseAntecedentesMedicamentosDao.consultarAntecedentesMedicamentos(con,codigoPaciente);
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMedicamentosDao#insertar(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */	
	public ResultadoBoolean insertar(Connection con, int codigoPaciente, String observaciones)
	{
			return SqlBaseAntecedentesMedicamentosDao.insertar(con,codigoPaciente,observaciones);
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMedicamentosDao#insertarTransaccional(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean insertarTransaccional(Connection con, int codigoPaciente, String observaciones, String estado) throws SQLException
	{
	    return SqlBaseAntecedentesMedicamentosDao.insertarTransaccional(con, codigoPaciente, observaciones, estado) ;
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMedicamentosDao#modificar(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean modificar(Connection con, int codigoPaciente, String observaciones)
	{
		return SqlBaseAntecedentesMedicamentosDao.modificar(con,codigoPaciente,observaciones);
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMedicamentosDao#modificarTransaccional(java.sql.Connection, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public ResultadoBoolean modificarTransaccional(Connection con, int codigoPaciente, String observaciones, String estado) throws SQLException
	{
	    return SqlBaseAntecedentesMedicamentosDao.modificarTransaccional(con, codigoPaciente, observaciones, estado); 
	}

	/**
	 * @see com.princetonsa.dao.AntecedentesMedicamentosDao#existenAntecedentesMedicamentos(java.sql.Connection, java.lang.String, java.lang.String)
	 */
	public ResultSetDecorator consultarMedicamentos( Connection con, int codigoPaciente)
	{
		return SqlBaseAntecedentesMedicamentosDao.consultarMedicamentos(con,codigoPaciente);	
	}
	
	public HashMap<String, Object> consultaFormaConc (Connection con, int codigoArticulo)
	{
		return SqlBaseAntecedentesMedicamentosDao.consultaFormaConc(con,codigoArticulo);
	}

}