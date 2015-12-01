/**
 * 
 */
package com.sies.dao;

import java.sql.Connection;

/**
 * @author Juan David Ramírez López
 * Creado el 18/03/2008
 */
public interface LogsSiesDao
{
	/**
	 * Generación de logs para eliminación de cuadro de turnos
	 * @param con Conexión con la BD
	 * @param usuario Usuario que eliminó el cuadro de turnos
	 * @param observacion Observación de la eliminación
	 */
	public abstract void logEliminarCuadroTurnos(Connection con, String usuario, String observacion);
}
