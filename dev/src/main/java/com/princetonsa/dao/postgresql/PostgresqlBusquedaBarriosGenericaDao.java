package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.BusquedaBarriosGenericaDao;
import com.princetonsa.dao.sqlbase.SqlBaseBusquedaBarriosGenericaDao;

/**
 * 
 * @author Sebasti�n G�mez R.
 * Implementaci�n postgres de las funciones de acceso a la fuente de datos
 * para busqueda de barrios generica
 *
 */
public class PostgresqlBusquedaBarriosGenericaDao implements
		BusquedaBarriosGenericaDao {

	/**
	 * M�todo que realiza la consulta de barrios
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consulta(Connection con,HashMap campos)
	{
		return SqlBaseBusquedaBarriosGenericaDao.consulta(con, campos);
	}

}
