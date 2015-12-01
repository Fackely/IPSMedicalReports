package com.princetonsa.dao.postgresql.interfaz;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.princetonsa.dao.interfaz.CuentasConveniosDao;
import com.princetonsa.dao.sqlbase.interfaz.SqlBaseCuentasConveniosDao;

public class PostgresqlCuentasConveniosDao implements CuentasConveniosDao
{
	/**
	 * Metodo que inserta la información de una cuenta de un régimen especificado
	 * @param con
	 * @param codTipoCuenta
	 * @param acronimoTipoRegimen
	 * @param codInstitucion
	 * @param valor
	 * @throws SQLException
	 */
	public void insertarCuentaRegimen(Connection con, int codTipoCuenta, String acronimoTipoRegimen, int codInstitucion, String valor, String rubro,String capitacion) throws SQLException
	{
		SqlBaseCuentasConveniosDao.insertarCuentaRegimen(con, codTipoCuenta, acronimoTipoRegimen, codInstitucion, valor, rubro, capitacion);
	}

	/**
	 * Actualiza la información de una cuenta de un régimen especificado
	 * @param con
	 * @param codTipoCuenta
	 * @param acronimoTipoRegimen
	 * @param codInstitucion
	 * @param valor
	 * @throws SQLException
	 */
	public void actualizarCuentaRegimen(Connection con, int codTipoCuenta, String acronimoTipoRegimen, int codInstitucion, String valor, String rubro, String capitacion) throws SQLException
	{
		SqlBaseCuentasConveniosDao.actualizarCuentaRegimen(con, codTipoCuenta, acronimoTipoRegimen, codInstitucion, valor, rubro, capitacion);
	}

	/**
	 * Consulta si existe una cuenta dada para un régimen especificado
	 * @param con
	 * @param codTipoCuenta
	 * @param acronimoTipoRegimen
	 * @param codInstitucion
	 * @return
	 * @throws SQLException
	 */
	public boolean existeCuentaRegimen(Connection con, int codTipoCuenta, String acronimoTipoRegimen, int codInstitucion) throws SQLException
	{
		return SqlBaseCuentasConveniosDao.existeCuentaRegimen(con, codTipoCuenta, acronimoTipoRegimen, codInstitucion);
	}
	
	/**
	 * Método que consulta las cuentas pertenecientes a un tipo de regimen y de la institucion indicada
	 * @param acronimoTipoRegimen
	 * @param codInstitucion
	 * @return
	 */
	public HashMap consultarCuentasRegimen(Connection con, String acronimoTipoRegimen, int codInstitucion) throws SQLException
	{
		return SqlBaseCuentasConveniosDao.consultarCuentasRegimen(con, acronimoTipoRegimen, codInstitucion);
	}
	
	/**
	 * Metodo que inserta la información de una cuenta de un convenio especificado
	 * @param con
	 * @param codTipoCuenta
	 * @param codConvenio
	 * @param codInstitucion
	 * @param valor
	 * @param rubro
	 * @throws SQLException
	 */
	public void insertarCuentaConvenio(Connection con, int codTipoCuenta, int codConvenio, int codInstitucion, String valor, String rubro, String capitacion) throws SQLException
	{
		SqlBaseCuentasConveniosDao.insertarCuentaConvenio(con, codTipoCuenta, codConvenio, codInstitucion, valor, rubro, capitacion);
	}

	/**
	 * Actualizar la información de una cuenta convenio especificado 
	 * @param con
	 * @param codTipoCuenta
	 * @param codConvenio
	 * @param codInstitucion
	 * @param valor
	 * @param rubro
	 * @throws SQLException
	 */
	public void actualizarCuentaConvenio(Connection con, int codTipoCuenta, int codConvenio, int codInstitucion, String valor, String rubro, String capitacion) throws SQLException
	{
		SqlBaseCuentasConveniosDao.actualizarCuentaConvenio(con, codTipoCuenta, codConvenio, codInstitucion, valor, rubro, capitacion);
	}

	/**
	 * Consulta si existe una cuenta dada para un convenio especificado
	 * @param con
	 * @param codTipoCuenta
	 * @param codConvenio
	 * @param codInstitucion
	 * @return
	 * @throws SQLException
	 */
	public boolean existeCuentaConvenio(Connection con, int codTipoCuenta, int codConvenio, int codInstitucion) throws SQLException
	{
		return SqlBaseCuentasConveniosDao.existeCuentaConvenio(con, codTipoCuenta, codConvenio, codInstitucion);
	}

	/**
	 * Método que consulta las cuentas pertenecientes a un tipo de convenio y de la institucion indicada
	 * @param con
	 * @param codConvenio
	 * @param codInstitucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarCuentasConvenio(Connection con, int codConvenio, int codInstitucion) throws SQLException
	{
		return SqlBaseCuentasConveniosDao.consultarCuentasConvenio(con, codConvenio, codInstitucion);
	}
}