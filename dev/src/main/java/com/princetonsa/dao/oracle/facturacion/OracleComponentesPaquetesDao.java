/*
 * Creado May 7, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * OracleComponentesPaquetesDao
 * com.princetonsa.dao.oracle.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosString;

import com.princetonsa.dao.facturacion.ComponentesPaquetesDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseComponentesPaquetesDao;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 7, 2007
 */
public class OracleComponentesPaquetesDao implements ComponentesPaquetesDao
{

	/**
	 * 
	 */
	public Vector<InfoDatosString> obtenerListadoPaquetes(Connection con, int institucion)
	{
		return SqlBaseComponentesPaquetesDao.obtenerListadoPaquetes(con,institucion);
	}

	/**
	 * 
	 */
	public HashMap consultarAgrupacionArticulosPaquete(Connection con, String codigoPaquete, int institucion)
	{
		return SqlBaseComponentesPaquetesDao.consultarAgrupacionArticulosPaquete(con,codigoPaquete,institucion);
	}


	/**
	 * 
	 */
	public HashMap consultarAgrupacionServiciosPaquete(Connection con, String codigoPaquete, int institucion)
	{
		return SqlBaseComponentesPaquetesDao.consultarAgrupacionServiciosPaquete(con,codigoPaquete,institucion);
	}

	/**
	 * 
	 */
	public HashMap consultarArticulosPaquete(Connection con, String codigoPaquete, int institucion)
	{
		return SqlBaseComponentesPaquetesDao.consultarArticulosPaquete(con,codigoPaquete,institucion);
	}

	/**
	 * 
	 */
	public HashMap consultarServiciosPaquete(Connection con, String codigoPaquete, int institucion)
	{
		return SqlBaseComponentesPaquetesDao.consultarServiciosPaquete(con,codigoPaquete,institucion);
	}

	/**
	 * 
	 */
	public boolean eliminarAgrupacionArticulos(Connection con, String codigoAgrupacion)
	{
		return SqlBaseComponentesPaquetesDao.eliminarAgrupacionArticulos(con,codigoAgrupacion);
	}
	

	/**
	 * 
	 */
	public HashMap consultarAgrupacionArticulosPaqueteLLave(Connection con, String codigo)
	{
		return SqlBaseComponentesPaquetesDao.consultarAgrupacionArticulosPaqueteLLave(con,codigo);
	}

	/**
	 * 
	 */
	public boolean modificarAgrupacionArticulos(Connection con, HashMap vo)
	{
		return SqlBaseComponentesPaquetesDao.modificarAgrupacionArticulos(con,vo);
	}

	/**
	 * 
	 */
	public boolean insertarAgrupacionArticulos(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO paq_agrupacion_articulos (codigo_paq_agrup_articulo,codigo_paquete,institucion,clase,grupo,subgrupo,naturaleza,cantidad,usuario_modifica,fecha_modifica,hora_modifica,grupo_especial_articulo) values (seq_paq_agrup_art.nextval,?,?,?,?,?,?,?,?,?,?,?)";
		return SqlBaseComponentesPaquetesDao.insertarAgrupacionArticulos(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean eliminarArticulos(Connection con, String codigoComponente)
	{
		return SqlBaseComponentesPaquetesDao.eliminarArticulos(con,codigoComponente);
	}

	/**
	 * 
	 */
	public HashMap consultarArticulosPaqueteLLave(Connection con, String codigo)
	{
		return SqlBaseComponentesPaquetesDao.consultarArticulosPaqueteLLave(con,codigo);
	}

	/**
	 * 
	 */
	public boolean modificarArticulos(Connection con, HashMap vo)
	{
		return SqlBaseComponentesPaquetesDao.modificarArticulos(con,vo);
	}

	/**
	 * 
	 */
	public boolean insertarArticulos(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO paq_comp_articulos (codigo_paq_articulo,codigo_paquete,institucion,codigo_articulo,cantidad,usuario_modifica,fecha_modifica,hora_modifica) values(seq_paq_comp_art.nextval,?,?,?,?,?,?,?)";
		return SqlBaseComponentesPaquetesDao.insertarArticulos(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean eliminarAgrupacionServicios(Connection con, String codigoAgrupacion)
	{
		return SqlBaseComponentesPaquetesDao.eliminarAgrupacionServicios(con,codigoAgrupacion);
	}

	/**
	 * 
	 */
	public HashMap consultarAgrupacionServiciosPaqueteLLave(Connection con, String codigo)
	{
		return SqlBaseComponentesPaquetesDao.consultarAgrupacionServiciosPaqueteLLave(con,codigo);
	}

	/**
	 * 
	 */
	public boolean insertarAgrupacionServicios(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO paq_agrupacion_servicios (codigo_paq_agrup_servicio,codigo_paquete,institucion,grupo_servicio,tipo_servicio,especialidad,cantidad,usuario_modifica,fecha_modifica,hora_modifica) values(seq_paq_agrupacion_serv.nextval,?,?,?,?,?,?,?,?,?)";
		return SqlBaseComponentesPaquetesDao.insertarAgrupacionServicios(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean modificarAgrupacionServicios(Connection con, HashMap vo)
	{
		return SqlBaseComponentesPaquetesDao.modificarAgrupacionServicios(con,vo);
	}
	

	/**
	 * 
	 */
	public boolean eliminarServicios(Connection con, String codigoComponente)
	{
		return SqlBaseComponentesPaquetesDao.eliminarServicios(con,codigoComponente);
	}

	/**
	 * 
	 */
	public HashMap consultarServiciosPaqueteLLave(Connection con, String codigo)
	{
		return SqlBaseComponentesPaquetesDao.consultarServiciosPaqueteLLave(con,codigo);
	}

	/**
	 * 
	 */
	public boolean insertarServicios(Connection con, HashMap vo)
	{
		String cadena="INSERT INTO paq_comp_servicios (codigo_paq_servicio,codigo_paquete,institucion,codigo_servicio,principal,cantidad,usuario_modifica,fecha_modifica,hora_modifica) values(seq_paq_comp_serv.nextval,?,?,?,?,?,?,?,?)";
		return SqlBaseComponentesPaquetesDao.insertarServicios(con,vo,cadena);
	}

	/**
	 * 
	 */
	public boolean modificarServicios(Connection con, HashMap vo)
	{
		return SqlBaseComponentesPaquetesDao.modificarServicios(con,vo);
	}
}
