package com.princetonsa.dao.interfaz;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public interface CuentasConveniosDao
{
	/**
	 * Metodo que inserta la información de una cuenta de un régimen especificado
	 * @param con
	 * @param codTipoCuenta
	 * @param acronimoTipoRegimen
	 * @param codInstitucion
	 * @param valor
	 * @param rubro 
	 * @throws SQLException
	 */
	public void insertarCuentaRegimen(Connection con, int codTipoCuenta, String acronimoTipoRegimen, int codInstitucion, String valor, String rubro, String capitacion) throws SQLException;

	/**
	 * Actualiza la información de una cuenta de un régimen especificado
	 * @param con
	 * @param codTipoCuenta
	 * @param acronimoTipoRegimen
	 * @param codInstitucion
	 * @param valor
	 * @param rubro 
	 * @throws SQLException
	 */
	public void actualizarCuentaRegimen(Connection con, int codTipoCuenta, String acronimoTipoRegimen, int codInstitucion, String valor, String rubro, String capitacion) throws SQLException;

	/**
	 * Consulta si existe una cuenta dada para un régimen especificado
	 * @param con
	 * @param codTipoCuenta
	 * @param acronimoTipoRegimen
	 * @param codInstitucion
	 * @return
	 * @throws SQLException
	 */
	public boolean existeCuentaRegimen(Connection con, int codTipoCuenta, String acronimoTipoRegimen, int codInstitucion) throws SQLException;
	
	/**
	 * Método que consulta las cuentas pertenecientes a un tipo de regimen y de la institucion indicada
	 * @param acronimoTipoRegimen
	 * @param codInstitucion
	 * @return
	 */
	public HashMap consultarCuentasRegimen(Connection con, String acronimoTipoRegimen, int codInstitucion) throws SQLException;
	
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
	public void insertarCuentaConvenio(Connection con, int codTipoCuenta, int codConvenio, int codInstitucion, String valor, String rubro, String capitacion) throws SQLException;

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
	public void actualizarCuentaConvenio(Connection con, int codTipoCuenta, int codConvenio, int codInstitucion, String valor, String rubro, String capitacion) throws SQLException;

	/**
	 * Consulta si existe una cuenta dada para un convenio especificado
	 * @param con
	 * @param codTipoCuenta
	 * @param codConvenio
	 * @param codInstitucion
	 * @return
	 * @throws SQLException
	 */
	public boolean existeCuentaConvenio(Connection con, int codTipoCuenta, int codConvenio, int codInstitucion) throws SQLException;

	/**
	 * Método que consulta las cuentas pertenecientes a un tipo de convenio y de la institucion indicada
	 * @param con
	 * @param codConvenio
	 * @param codInstitucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarCuentasConvenio(Connection con, int codConvenio, int codInstitucion) throws SQLException;
}
