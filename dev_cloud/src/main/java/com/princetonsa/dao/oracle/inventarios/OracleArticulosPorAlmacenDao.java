package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.ArticulosPorAlmacenDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseArtPorAlmacenDao;
import com.princetonsa.mundo.inventarios.ArticulosPorAlmacen;
import com.princetonsa.mundo.inventarios.Secciones;

/**
 * @author garias@princetonsa.com
 */
public class OracleArticulosPorAlmacenDao implements ArticulosPorAlmacenDao {
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public HashMap consultar(Connection con, ArticulosPorAlmacen a)
	{
		return SqlBaseArtPorAlmacenDao.consultar(con, a);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public HashMap consultarUbicaciones(Connection con, ArticulosPorAlmacen a)
	{
		return SqlBaseArtPorAlmacenDao.consultarUbicaciones(con, a);
	}
	
	/**
	 * 
	 * @param con
	 * @param a
	 * @return
	 */
	public boolean guardarNuevo(Connection con, ArticulosPorAlmacen a)
	{
		return SqlBaseArtPorAlmacenDao.guardarNuevo(con, a);
	}
	
	/**
	 * 
	 * @param con
	 * @param a
	 * @return
	 */
	public boolean eliminarRegistroDet(Connection con, ArticulosPorAlmacen a)
	{
		return SqlBaseArtPorAlmacenDao.eliminarRegistroDet(con, a);		
	}
	
	/**
	 * 
	 * @param con
	 * @param a
	 * @return
	 */
	public boolean modificarRegistroDet(Connection con, ArticulosPorAlmacen a)
	{
		return SqlBaseArtPorAlmacenDao.modificarRegistroDet(con, a);		
	}
	
}