package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosString;

import com.princetonsa.dao.facturacion.InclusionesExclusionesDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseInclusionesExclusionesDao;

public class OracleInclusionesExclusionesDao implements
		InclusionesExclusionesDao {

	public HashMap consultarIncluExcluExistentes(Connection con, HashMap vo) 
	{
		return SqlBaseInclusionesExclusionesDao.consultarIncluExcluExistentes(con,vo);
	}

	
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 */
	
	public boolean insertar(Connection con, HashMap vo)
	{
		return SqlBaseInclusionesExclusionesDao.insertar(con, vo);
	}
	
	

	/**
	 * 
	 */
	
	public boolean modificar(Connection con, HashMap vo)
	{
		return SqlBaseInclusionesExclusionesDao.modificar(con, vo);
	}
	
	
	
	/**
	 * 
	 */
	
	public boolean eliminarRegistro(Connection con, int institucion, String codigo)
	{
		return SqlBaseInclusionesExclusionesDao.eliminarRegistro(con,institucion,codigo);
	}

	/**
	 * 
	 */
	public Vector<InfoDatosString> consultarIncluExcluExistentes(Connection con, int institucion)
	{
		return SqlBaseInclusionesExclusionesDao.consultarIncluExcluExistentes(con,institucion);
	}
}
