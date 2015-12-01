package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.FacturadosPorConvenioDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseFacturadosPorConvenioDao;

/**
 * @author Mauricio Jllo
 * Fecha Junio de 2008
 */

public class PostgresqlFacturadosPorConvenioDao implements FacturadosPorConvenioDao
{

	/**
	 * 
	 */
	public HashMap consultarFacturadosPorConvenio(Connection con, HashMap criterios, boolean porSerArt)
	{
		return SqlBaseFacturadosPorConvenioDao.consultarFacturadosPorConvenio(con, criterios, porSerArt);
	}
	
	/**
	 * 
	 */
	public void insertarLog(Connection con, HashMap criterios)
	{
		SqlBaseFacturadosPorConvenioDao.insertarLog(con, criterios);
	}
	
}
