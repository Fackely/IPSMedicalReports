/*
 * Creado en 13/12/2006
 *
 * Princeton S.A.
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Wilson Rios
 *
 * Princeton S.A.
 */
public interface UtilidadImpresionDao
{
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public HashMap obtenerEncabezadoPaciente(Connection con, String idCuenta);
}
