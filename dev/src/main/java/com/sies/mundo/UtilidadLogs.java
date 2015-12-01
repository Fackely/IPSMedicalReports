/**
 * 
 */
package com.sies.mundo;

import java.sql.Connection;

import com.sies.dao.LogsSiesDao;
import com.sies.dao.SiEsFactory;


/**
 * @author Juan David Ramírez López
 * Creado el 18/03/2008
 */
public class UtilidadLogs
{
	static LogsSiesDao logsSiesDao;
	static {
		if(logsSiesDao==null)
		{
			logsSiesDao=SiEsFactory.getDaoFactory().getLogsSiesDao();
		}
	}
	
	/**
	 * Generación de logs para eliminación de cuadro de turnos
	 * @param con Conexión con la BD
	 * @param usuario Usuario que eliminó el cuadro de turnos
	 * @param observacion Observación de la eliminación
	 */
	public static void logEliminarCuadroTurnos(Connection con, String usuario, String observacion)
	{
		logsSiesDao.logEliminarCuadroTurnos(con, usuario, observacion);
	}
}
