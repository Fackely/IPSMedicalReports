package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jaramillo
 */

public interface ArticulosConsumidosPacientesDao 
{
	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public abstract HashMap<String, Object> consultarCondicionesArticulosConsumidos(Connection con, HashMap<String, Object> criterios);

	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public abstract HashMap consultarArticulosConsumidos(Connection con, HashMap<String, Object> criterios);
	
}