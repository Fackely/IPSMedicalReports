/*
 * Junio 26, 2009
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.BusquedaDeudoresGenericaDao;
import com.princetonsa.dao.sqlbase.SqlBaseBusquedaDeudoresGenericaDao;

/**
 * 
 * @author Sebasti�n G�mez R.
 * Implementaci�n oracle de las funciones de acceso a la fuente de datos
 * para busqueda de deudores generica
 *
 */
public class OracleBusquedaDeudoresGenericaDao implements
		BusquedaDeudoresGenericaDao 
{

	/**
	 * M�todo que realiza la busqueda de deudores
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> busqueda(Connection con,HashMap<String, Object> campos)
	{
		return SqlBaseBusquedaDeudoresGenericaDao.busqueda(con, campos);
	}
}
