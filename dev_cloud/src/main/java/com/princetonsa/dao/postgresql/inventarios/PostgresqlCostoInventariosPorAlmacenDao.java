package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.CostoInventariosPorAlmacenDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseCostoInventariosPorAlmacenDao;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class PostgresqlCostoInventariosPorAlmacenDao implements CostoInventariosPorAlmacenDao
{

	/**
	 * 
	 */
	public HashMap consultarCostoInventarioPorAlmacen(Connection con, HashMap criterios)
	{
		return SqlBaseCostoInventariosPorAlmacenDao.consultarCostoInventarioPorAlmacen(con, criterios);
	}
	
}
