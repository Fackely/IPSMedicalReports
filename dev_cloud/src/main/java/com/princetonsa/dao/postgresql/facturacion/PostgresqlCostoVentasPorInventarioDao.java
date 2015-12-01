package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.CostoVentasPorInventarioDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseCostoVentasPorInventarioDao;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class PostgresqlCostoVentasPorInventarioDao implements CostoVentasPorInventarioDao
{

	/**
	 * 
	 */
	public HashMap consultarCostoVentasPorInventario(Connection con, HashMap criterios)
    {
        return SqlBaseCostoVentasPorInventarioDao.consultarCostoVentasPorInventario(con, criterios);
    }
	
}
