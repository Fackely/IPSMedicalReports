package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jaramillo
 * Fecha: Junio de 2007
 */

public interface CostoInventarioPorFacturarDao
{

	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public abstract HashMap consultarCostoInventarioPorFacturar(Connection con, HashMap criterios);

}
