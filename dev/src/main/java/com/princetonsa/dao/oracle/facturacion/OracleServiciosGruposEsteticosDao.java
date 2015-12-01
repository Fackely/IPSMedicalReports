package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.ServiciosGruposEsteticosDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseServiciosGruposEsteticosDao;




public class OracleServiciosGruposEsteticosDao implements
		ServiciosGruposEsteticosDao 
		
{

	/**
	 * @param con
	 * @param institucion
	 */
	
	public HashMap consultarServiciosEsteticosExistentes(Connection con, HashMap vo) 
	{
		return SqlBaseServiciosGruposEsteticosDao.consultarServiciosEsteticosExistentes(con,vo);
	}

	
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 */
	
	public boolean insertar(Connection con, HashMap vo)
	{
		return SqlBaseServiciosGruposEsteticosDao.insertar(con, vo);
	}
	
	

	/**
	 * 
	 */
	
	public boolean modificar(Connection con, HashMap vo)
	{
		return SqlBaseServiciosGruposEsteticosDao.modificar(con, vo);
	}
	
	
	
	/**
	 * 
	 */
	
	public boolean eliminarRegistro(Connection con, int institucion, String codigo)
	{
		return SqlBaseServiciosGruposEsteticosDao.eliminarRegistro(con,institucion,codigo);
	}
	
	
	
	/////////////////////////////////////////////////////////////////////////Grupos
	
	public HashMap consultarGruposEsteticosExistentes(Connection con, HashMap vo)
	{
		return SqlBaseServiciosGruposEsteticosDao.consultarGruposEsteticosExistentes(con, vo);
	}
	
	
	public boolean insertarServicio(Connection con, HashMap vo)
	{
		return SqlBaseServiciosGruposEsteticosDao.insertarServicio(con, vo);
	}
	
	
	/*public boolean modificarServicio(Connection con, HashMap vo)
	{
		return SqlBaseServiciosGruposEsteticosDao.modificarServicio(con, vo);
	}*/
	
	
	public boolean eliminarServicio(Connection con, int institucion, int servicio)
	{
		return SqlBaseServiciosGruposEsteticosDao.eliminarServicio(con, institucion, servicio);
	}
	
	

	
	
}
