package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

public interface UbicacionGeograficaDao {
	
//////////////////////////////INICIO INSERTAR//////////////////////////////////
	
	/**
	 * Insertar pais
	 */
	public abstract boolean insertarPais(Connection con, HashMap vo);
	
	/**
	 * Insertar departamento
	 */
	public abstract boolean insertarDepartamento(Connection con, HashMap vo);
	
	/**
	 * Insertar ciudad
	 */
	public abstract boolean insertarCiudad(Connection con, HashMap vo);
	
	/**
	 * Insertar localidad
	 */
	public abstract boolean insertarLocalidad(Connection con, HashMap vo);
	
	/**
	 * Insertar barrio
	 */
	public abstract boolean insertarBarrio(Connection con, HashMap vo);
	
//////////////////////////////FIN INSERTAR//////////////////////////////////
	
	
	
	
	
//////////////////////////////INICIO CONSULTAR//////////////////////////////////
	
	/**
	 * consultar pais 
	 */
	public abstract HashMap consultarPais(Connection con, HashMap vo);
	
	/**
	 * consultar departamento
	 */
	public abstract HashMap consultarDepartamento(Connection con, HashMap vo);
	
	/**
	 * consultar ciudad 
	 */
	public abstract HashMap consultarCiudad(Connection con, HashMap vo);
	
	/**
	 * consultar localidad
	 */
	public abstract HashMap consultarLocalidad(Connection con, HashMap vo);
	
	/**
	 * consultar barrio 
	 */
	public abstract HashMap consultarBarrio(Connection con, HashMap vo);
	
//////////////////////////////FIN CONSULTAR//////////////////////////////////
	
	
	
	
	
//////////////////////////////INICIO MODIFICAR////////////////////////////////////
	
	/**
	 * Modificar pais
	 * */
	public abstract boolean modificarPais(Connection con,HashMap vo);
	
	/**
	 * Modificar departamento
	 * */
	public abstract boolean modificarDepartamento(Connection con,HashMap vo);
	
	/**
	 * Modificar ciudad
	 * */
	public abstract boolean modificarCiudad(Connection con,HashMap vo);
	
	/**
	 * Modificar localidad
	 * */
	public abstract boolean modificarLocalidad(Connection con,HashMap vo);
	
	/**
	 * Modificar barrio
	 * */
	public abstract boolean modificarBarrio(Connection con,HashMap vo);
	
//////////////////////////////FIN MODIFICAR////////////////////////////////////
	
	
	
	
	
//////////////////////////////INICIO ELIMINACION////////////////////////////////////
	
	/**
	 * Eliminar pais
	 * */
	public abstract boolean eliminarPais(Connection con, String codigo);
	
	/**
	 * Eliminar departamento
	 * */
	public abstract boolean eliminarDepartamento(Connection con, String codigo_departamento,String codigo_pais);
	
	/**
	 * Eliminar ciudad
	 * */
	public abstract boolean eliminarCiudad(Connection con, String codigo_ciudad, String codigo_departamento, String codigo_pais);
	
	/**
	 * Eliminar localidad
	 * */
	public abstract boolean eliminarLocalidad(Connection con, String codigo_localidad, String codigo_ciudad, String codigo_departamento, String codigo_pais);
	
	/**
	 * Eliminar barrio
	 * */
	public abstract boolean eliminarBarrio(Connection con, int codigo);
	
//////////////////////////////FIN ELIMINACION////////////////////////////////////

}
