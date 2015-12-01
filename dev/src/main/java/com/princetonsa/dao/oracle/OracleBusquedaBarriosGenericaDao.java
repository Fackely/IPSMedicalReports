/*
 * Jun 15, 2007
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.BusquedaBarriosGenericaDao;
import com.princetonsa.dao.sqlbase.SqlBaseBusquedaBarriosGenericaDao;

/**
 * 
 * @author Sebastián Gómez R.
 * Implementación oracle de las funciones de acceso a la fuente de datos
 * para busqueda de barrios generica
 *
 */
public class OracleBusquedaBarriosGenericaDao implements
		BusquedaBarriosGenericaDao {

	/**
	 * Método que realiza la consulta de barrios
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consulta(Connection con,HashMap campos)
	{
		return SqlBaseBusquedaBarriosGenericaDao.consulta(con, campos);
	}

}
