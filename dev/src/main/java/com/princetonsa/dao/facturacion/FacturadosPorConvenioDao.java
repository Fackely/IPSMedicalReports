package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jllo
 * Fecha Junio de 2008
 */

public interface FacturadosPorConvenioDao
{

	/**
	 * @param con
	 * @param criterios
	 * @param porSerArt
	 * @return
	 */
	public abstract HashMap consultarFacturadosPorConvenio(Connection con, HashMap criterios, boolean porSerArt);

	/**
	 * @param con
	 * @param criterios
	 */
	public abstract void insertarLog(Connection con, HashMap criterios);

}
