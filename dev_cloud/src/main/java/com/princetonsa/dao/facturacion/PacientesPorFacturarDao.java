package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jaramillo
 * Fecha: Agosto de 2008
 */

public interface PacientesPorFacturarDao
{

	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public abstract HashMap consultarConsumosPorFacturar(Connection con, HashMap criterios);

}
