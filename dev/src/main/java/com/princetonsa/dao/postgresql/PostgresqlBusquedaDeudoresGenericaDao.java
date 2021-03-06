/*
 * Junio 26, 2009
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.BusquedaDeudoresGenericaDao;
import com.princetonsa.dao.sqlbase.SqlBaseBusquedaDeudoresGenericaDao;

/**
 * 
 * @author Sebastián Gómez R.
 * Implementación postgres de las funciones de acceso a la fuente de datos
 * para busqueda de deudores generica
 *
 */
public class PostgresqlBusquedaDeudoresGenericaDao implements
		BusquedaDeudoresGenericaDao 
{
	/**
	 * Método que realiza la busqueda de deudores
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> busqueda(Connection con,HashMap<String, Object> campos)
	{
		return SqlBaseBusquedaDeudoresGenericaDao.busqueda(con, campos);
	}
}
