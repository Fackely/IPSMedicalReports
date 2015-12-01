/**
 * 
 */
package com.sies.mundo;

import java.sql.Connection;

import com.sies.dao.LogsSiesDao;
import com.sies.dao.SiEsFactory;


/**
 * @author Juan David Ram�rez L�pez
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
	 * Generaci�n de logs para eliminaci�n de cuadro de turnos
	 * @param con Conexi�n con la BD
	 * @param usuario Usuario que elimin� el cuadro de turnos
	 * @param observacion Observaci�n de la eliminaci�n
	 */
	public static void logEliminarCuadroTurnos(Connection con, String usuario, String observacion)
	{
		logsSiesDao.logEliminarCuadroTurnos(con, usuario, observacion);
	}
}
