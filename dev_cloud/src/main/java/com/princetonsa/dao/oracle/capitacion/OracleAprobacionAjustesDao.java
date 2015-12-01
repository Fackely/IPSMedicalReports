/*
 * Creado el Jun 30, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.oracle.capitacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.capitacion.AprobacionAjustesDao;
import com.princetonsa.dao.sqlbase.capitacion.SqlBaseAprobacionAjustesDao;

public class OracleAprobacionAjustesDao implements AprobacionAjustesDao {

	/**
	 * Metodo Para Cargar Informacion Parametrizada.
	 * @param con
	 * @param mapaParam
	 * @return
	 */
	public HashMap cargarInformacion(Connection con, HashMap mapaParam)
	{
		return SqlBaseAprobacionAjustesDao.cargarInformacion(con, mapaParam);
	}

	/**
	 * Metodo para Aprobar el Ajuste Seleccionado.
	 * @param con
	 * @param mapaParam
	 * @return
	 */
	public int aprobarAjuste(Connection con, HashMap mapaParam)
	{
		return SqlBaseAprobacionAjustesDao.aprobarAjuste(con, mapaParam);
	}
	

}
