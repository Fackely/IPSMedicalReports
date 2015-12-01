package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jaramillo Henao
 * Fecha: Agosto de 2008
 */

public interface ConsultaSaldosCierresInventariosDao
{

	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public HashMap consultarSaldosCierresInventarios(Connection con, HashMap criterios);

	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public String consultarCondicionesSaldosCierresInventarios(Connection con, HashMap criterios);

}