package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.SeccionesDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseSeccionesDao;
import com.princetonsa.mundo.inventarios.Secciones;

/**
 * 
 * @author axioma
 *
 */
public class OracleSeccionesDao implements SeccionesDao 
{
	/**
	 * 
	 * @param con
	 * @param secciones
	 */
	public boolean insertar(Connection con, Secciones secciones)
	{
		return SqlBaseSeccionesDao.insertar(con, secciones);
	}
	
	/**
	 * @param con
	 * @param secciones
	 */
	public boolean modificar(Connection con, Secciones secciones)
	{
		return SqlBaseSeccionesDao.modificar(con, secciones);
	}
	
	/**
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminar(Connection con, int codigo)
	{
		return SqlBaseSeccionesDao.eliminar(con, codigo);
	}
	
	/** 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultar(Connection con, Secciones seccion) 
	{
		return SqlBaseSeccionesDao.consultar(con, seccion);	
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoSeccion
	 * @return
	 */
	public HashMap consultarSubsecciones(Connection con, int codigoSeccion)
	{
		return SqlBaseSeccionesDao.consultarSubsecciones(con, codigoSeccion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoSeccion
	 * @return
	 */
	public boolean insertarSubsecciones(Connection con, Secciones secciones)
	{
		return SqlBaseSeccionesDao.insertarSubsecciones(con, secciones);
	}

	/**
	 * 
	 */
	public HashMap cargarSubsecciones(Connection con, Secciones subseccion) {
		return SqlBaseSeccionesDao.cargarSubsecciones(con, subseccion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean modificarSubseccion(Connection con, Secciones subseccion)
	{
		return SqlBaseSeccionesDao.modificarSubseccion(con, subseccion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarSubseccion(Connection con, Secciones secciones)
	{
		return SqlBaseSeccionesDao.eliminarSubseccion(con, secciones);
	}
}