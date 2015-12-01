package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.inventarios.Secciones;

/**
 * Interfaz de secciones x almacen
 * @author axioma
 *
 */
public interface SeccionesDao 
{
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean insertar(Connection con, Secciones secciones);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean modificar(Connection con, Secciones secciones);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminar(Connection con, int codigo);
	
	/** 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultar(Connection con, Secciones seccion) ;
	
	/**
	 * 
	 * @param con
	 * @param codigoSeccion
	 * @return
	 */
	public HashMap consultarSubsecciones(Connection con, int codigoseccion);
	
	
	/**
	 * 
	 * @param con
	 * @param subseccion
	 * @return
	 */
	public HashMap cargarSubsecciones(Connection con, Secciones subseccion);
	
	/**
	 * 
	 * @param con
	 * @param codigoSeccion
	 * @return
	 */
	public boolean insertarSubsecciones(Connection con, Secciones secciones);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean modificarSubseccion(Connection con, Secciones secciones);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarSubseccion(Connection con, Secciones secciones);
	
	
}
