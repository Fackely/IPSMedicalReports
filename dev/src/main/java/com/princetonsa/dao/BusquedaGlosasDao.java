package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;


public interface BusquedaGlosasDao
{
	/**
	 * Metodo que consulta las glosas segun parametros
	 * @param con
	 * @param criterios
	 * @return
	 */
	public HashMap<String, Object> consultarGlosas(Connection con, HashMap criterios);
}