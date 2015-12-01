package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public interface CostoVentasPorInventarioDao
{

	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public abstract HashMap consultarCostoVentasPorInventario(Connection con, HashMap criterios);
	
}
