package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.FarmaciaCentroCostoDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseFarmaciaCentroCostoDao;
import com.princetonsa.mundo.inventarios.FarmaciaCentroCosto;

/**
 * 
 * @author axioma
 *
 */
public class PostgresqlFarmaciaCentroCostoDao implements FarmaciaCentroCostoDao 
{
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean insertar(Connection con, FarmaciaCentroCosto farmaciacentrocosto)
	{
		return SqlBaseFarmaciaCentroCostoDao.insertar(con, farmaciacentrocosto);
	}
	

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminar(Connection con, int codigo, int farmacia)
	{
		return SqlBaseFarmaciaCentroCostoDao.eliminar(con, codigo, farmacia);
	}
	
	/** 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultar(Connection con, FarmaciaCentroCosto farmaciacentrocosto) 
	{
		return SqlBaseFarmaciaCentroCostoDao.consultar(con, farmaciacentrocosto);	
	}
	
	/**
	 * 
	 * @param con
	 * @param farmaciacentrocosto
	 * @return
	 */
	public boolean insertartrans(Connection con, FarmaciaCentroCosto farmaciacentrocosto)
	{
		return SqlBaseFarmaciaCentroCostoDao.insertartrans(con, farmaciacentrocosto);	
	}

	/**
	 * 
	 * @param con
	 * @param farmaciacentrocosto
	 * @return
	 */
	public boolean insertardet(Connection con, FarmaciaCentroCosto farmaciacentrocosto)
	{
		return SqlBaseFarmaciaCentroCostoDao.insertardet(con, farmaciacentrocosto);
	}
}

