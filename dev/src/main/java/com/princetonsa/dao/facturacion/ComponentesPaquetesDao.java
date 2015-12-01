/*
 * Creado May 7, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * ComponentesPaquetesDao
 * com.princetonsa.dao.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosString;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 7, 2007
 */
public interface ComponentesPaquetesDao
{

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public abstract Vector<InfoDatosString> obtenerListadoPaquetes(Connection con, int institucion);

	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public abstract HashMap consultarAgrupacionArticulosPaquete(Connection con, String codigoPaquete, int institucion);

	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public abstract HashMap consultarArticulosPaquete(Connection con, String codigoPaquete, int institucion);

	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public abstract HashMap consultarAgrupacionServiciosPaquete(Connection con, String codigoPaquete, int institucion);

	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public abstract HashMap consultarServiciosPaquete(Connection con, String codigoPaquete, int institucion);

	/**
	 * 
	 * @param con
	 * @param codigoAgrupacion
	 * @return
	 */
	public abstract boolean eliminarAgrupacionArticulos(Connection con, String codigoAgrupacion);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract HashMap consultarAgrupacionArticulosPaqueteLLave(Connection con, String codigo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarAgrupacionArticulos(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarAgrupacionArticulos(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param codigoComponente
	 * @return
	 */
	public abstract boolean eliminarArticulos(Connection con, String codigoComponente);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract HashMap consultarArticulosPaqueteLLave(Connection con, String codigo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarArticulos(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarArticulos(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param codigoAgrupacion
	 * @return
	 */
	public abstract boolean eliminarAgrupacionServicios(Connection con, String codigoAgrupacion);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract HashMap consultarAgrupacionServiciosPaqueteLLave(Connection con, String codigo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarAgrupacionServicios(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarAgrupacionServicios(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param codigoComponente
	 * @return
	 */
	public abstract boolean eliminarServicios(Connection con, String codigoComponente);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract HashMap consultarServiciosPaqueteLLave(Connection con, String codigo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarServicios(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarServicios(Connection con, HashMap vo);

}
