package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.CostoInventarioPorFacturarDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseCostoInventarioPorFacturarDao;

/**
 * @author Mauricio Jaramillo
 * Fecha: Junio de 2007
 */

public class OracleCostoInventarioPorFacturarDao implements CostoInventarioPorFacturarDao
{

	/**
	 * 
	 */
	public HashMap consultarCostoInventarioPorFacturar(Connection con, HashMap criterios)
    {
        return SqlBaseCostoInventarioPorFacturarDao.consultarCostoInventarioPorFacturar(con, criterios);
    }
	
}
