/**
 * 
 */
package com.sies.dao.oracle;

import java.sql.Connection;

import com.sies.dao.LogsSiesDao;
import com.sies.dao.sqlbase.SqlBaseLogsSiesDao;

/**
 * @author Juan David Ram�rez L�pez
 * Creado el 18/03/2008
 */
public class OracleLogsSiesDao implements LogsSiesDao
{
	@Override
	public void logEliminarCuadroTurnos(Connection con, String usuario, String observacion)
	{
		SqlBaseLogsSiesDao.eliminarCuadroTurnos(con, usuario, observacion);
	}

}
