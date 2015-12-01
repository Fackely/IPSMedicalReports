/*
 * Junio 26, 2009
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author Sebastián Gómez R.
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>BusquedaDeudoresGenerica</code>.
 */
public interface BusquedaDeudoresGenericaDao 
{
	/**
	 * Método que realiza la busqueda de deudores
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> busqueda(Connection con,HashMap<String, Object> campos);
}
