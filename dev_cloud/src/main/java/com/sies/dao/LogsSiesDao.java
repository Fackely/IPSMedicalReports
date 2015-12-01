/**
 * 
 */
package com.sies.dao;

import java.sql.Connection;

/**
 * @author Juan David Ram�rez L�pez
 * Creado el 18/03/2008
 */
public interface LogsSiesDao
{
	/**
	 * Generaci�n de logs para eliminaci�n de cuadro de turnos
	 * @param con Conexi�n con la BD
	 * @param usuario Usuario que elimin� el cuadro de turnos
	 * @param observacion Observaci�n de la eliminaci�n
	 */
	public abstract void logEliminarCuadroTurnos(Connection con, String usuario, String observacion);
}
