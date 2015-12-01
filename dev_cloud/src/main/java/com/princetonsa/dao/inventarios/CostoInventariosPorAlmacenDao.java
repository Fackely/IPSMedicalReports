package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public interface CostoInventariosPorAlmacenDao
{

	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public abstract HashMap consultarCostoInventarioPorAlmacen(Connection con, HashMap criterios);

}
