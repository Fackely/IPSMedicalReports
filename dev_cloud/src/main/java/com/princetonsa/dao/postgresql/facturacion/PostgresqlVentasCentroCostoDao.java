package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.VentasCentroCostoDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseVentasCentroCostoDao;

public class PostgresqlVentasCentroCostoDao implements VentasCentroCostoDao 
{

	
	/**
	 * 
	 */
	public String cambiarConsulta(Connection con, String fechaInicial, String fechaFinal, String centroCosto) 
	{
		return SqlBaseVentasCentroCostoDao.cambiarConsulta(con, fechaInicial, fechaFinal, centroCosto);
	}

	
	/**
	 * 
	 */
	public HashMap consultarVentasCentroCosto(Connection con, HashMap vo) 
	{
		return SqlBaseVentasCentroCostoDao.consultarVentasCentroCosto(con, vo);
	}
	
	
}
