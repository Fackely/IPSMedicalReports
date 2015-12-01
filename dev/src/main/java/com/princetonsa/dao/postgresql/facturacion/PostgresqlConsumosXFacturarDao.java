package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.facturacion.ConsumosXFacturarDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseConsumosXFacturarDao;
import com.princetonsa.dto.interfaz.DtoInterfazConsumosXFacturar;

/**
 * 
 * @author wilson
 *
 */
public class PostgresqlConsumosXFacturarDao implements ConsumosXFacturarDao 
{
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public ArrayList<DtoInterfazConsumosXFacturar> obtenerConsumosXFacturar(Connection con, int institucion)
	{
		return SqlBaseConsumosXFacturarDao.obtenerConsumosXFacturar(con, institucion);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoInterfazConsumosXFacturar> obtenerConsumosXFacturarReproceso(Connection con, int institucion)
	{
		return SqlBaseConsumosXFacturarDao.obtenerConsumosXFacturarReproceso(con, institucion);
	}
}
