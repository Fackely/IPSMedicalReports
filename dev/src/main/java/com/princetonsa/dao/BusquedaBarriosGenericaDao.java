/*
 * Jun 15, 2007
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Sebastián Gómez R.
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>BusquedaBarriosGenerica</code>.
 */
public interface BusquedaBarriosGenericaDao 
{
	/**
	 * Método que realiza la consulta de barrios
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consulta(Connection con,HashMap campos);
	
}
