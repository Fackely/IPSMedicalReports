package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.PaquetesDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBasePaquetesDao;

public class OraclePaquetesDao implements PaquetesDao 
{
	
	
	/**
	 * @param con
	 * @param institucion
	 */
	
	public HashMap consultarPaquetesExistentes(Connection con, HashMap vo) 
	{
		return SqlBasePaquetesDao.consultarPaquetesExistentes(con,vo);
	}

	
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 */
	
	public boolean insertar(Connection con, HashMap vo)
	{
		return SqlBasePaquetesDao.insertar(con, vo);
	}
	
	

	/**
	 * 
	 */
	
	public boolean modificar(Connection con, HashMap vo)
	{
		return SqlBasePaquetesDao.modificar(con, vo);
	}
	
	
	
	/**
	 * 
	 */
	
	public boolean eliminarRegistro(Connection con, int institucion, String codigoPaquete)
	{
		return SqlBasePaquetesDao.eliminarRegistro(con,institucion,codigoPaquete);
	}
	

	

}
