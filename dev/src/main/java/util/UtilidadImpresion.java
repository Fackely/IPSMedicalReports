/*
 * Creado en 13/12/2006
 *
 * Princeton S.A.
 */
package util;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;

/**
 * @author Wilson Rios
 *
 * Princeton S.A.
 */
public class UtilidadImpresion
{
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static HashMap obtenerEncabezadoPaciente(Connection con, String idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadImpresionDao().obtenerEncabezadoPaciente(con, idCuenta);
	}
}
