package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jaramillo H.
 * Fecha: Agosto de 2008
 */

public interface ConsultaCostoArticulosDao
{

	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public String consultarCondicionesCostoArticulos(Connection con, HashMap criterios);

	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public HashMap consultarCostoArticulos(Connection con, HashMap criterios);
	
}