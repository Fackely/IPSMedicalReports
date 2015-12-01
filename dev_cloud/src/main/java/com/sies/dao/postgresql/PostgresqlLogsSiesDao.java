/**
 * 
 */
package com.sies.dao.postgresql;

import java.sql.Connection;

import com.sies.dao.LogsSiesDao;
import com.sies.dao.sqlbase.SqlBaseLogsSiesDao;

/**
 * @author Juan David Ramírez López
 * Creado el 18/03/2008
 */
public class PostgresqlLogsSiesDao implements LogsSiesDao
{
	@Override
	public void logEliminarCuadroTurnos(Connection con, String usuario, String observacion)
	{
		SqlBaseLogsSiesDao.eliminarCuadroTurnos(con, usuario, observacion);
	}

}
