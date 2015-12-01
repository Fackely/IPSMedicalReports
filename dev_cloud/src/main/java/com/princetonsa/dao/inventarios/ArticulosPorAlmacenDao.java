package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.inventarios.SqlBaseArtPorAlmacenDao;
import com.princetonsa.mundo.inventarios.ArticulosPorAlmacen;
import com.princetonsa.mundo.inventarios.Secciones;

/**
 * Interfaz de articulos por almacen
 * @author garias@princetonsa.com
 */
public interface ArticulosPorAlmacenDao {
	
	/** 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultar(Connection con, ArticulosPorAlmacen a);
	
	/** 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarUbicaciones(Connection con, ArticulosPorAlmacen a);
	
	/**
	 * 
	 * @param con
	 * @param a
	 * @return
	 */
	public boolean guardarNuevo(Connection con, ArticulosPorAlmacen a);
	
	/**
	 * 
	 * @param con
	 * @param a
	 * @return
	 */
	public boolean eliminarRegistroDet(Connection con, ArticulosPorAlmacen a);
	
	/**
	 * 
	 * @param con
	 * @param a
	 * @return
	 */
	public boolean modificarRegistroDet(Connection con, ArticulosPorAlmacen a);	
}