package com.princetonsa.dao.oracle;

import java.util.HashMap;

import com.princetonsa.dao.BusquedaGlosasDao;
import com.princetonsa.dao.sqlbase.SqlBaseBusquedaGlosasDao;
import java.sql.Connection;


public class OracleBusquedaGlosasDao implements BusquedaGlosasDao
{
	/**
	 * Metodo que consulta las glosas segun parametros
	 */
	public HashMap<String, Object> consultarGlosas(Connection con, HashMap criterios)
	{
		return SqlBaseBusquedaGlosasDao.consultarGlosas(con, criterios);
	}
}