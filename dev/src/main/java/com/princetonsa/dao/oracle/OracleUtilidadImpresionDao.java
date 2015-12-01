/*
 * Creado en 13/12/2006
 *
 * Princeton S.A.
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.UtilidadImpresionDao;
import com.princetonsa.dao.sqlbase.SqlBaseUtilidadImpresionDao;

/**
 * @author Wilson Rios
 *
 * Princeton S.A.
 */
public class OracleUtilidadImpresionDao implements UtilidadImpresionDao
{
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public HashMap obtenerEncabezadoPaciente(Connection con, String idCuenta)
	{
		return SqlBaseUtilidadImpresionDao.obtenerEncabezadoPaciente(con, idCuenta);
	}
}
