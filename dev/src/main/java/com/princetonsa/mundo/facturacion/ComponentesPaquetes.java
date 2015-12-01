/*
 * Creado May 7, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * ComponentesPaquetes
 * com.princetonsa.mundo.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.InfoDatosString;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ComponentesPaquetesDao;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 7, 2007
 */
public class ComponentesPaquetes
{
	/**
	 * 
	 */
	Logger logger=Logger.getLogger(ComponentesPaquetes.class);
	
	ComponentesPaquetesDao objetoDao;

	/**
	 * 
	 *
	 */
	public ComponentesPaquetes()
	{
		this.reset();
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 * @param property
	 */
	private boolean init(String tipoBD)
	{
		if(objetoDao==null)
		{
			objetoDao=DaoFactory.getDaoFactory(tipoBD).getComponentesPaquetesDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
	}

	/**
	 * 
	 *
	 */
	private void reset()
	{
		
		
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public Vector<InfoDatosString> obtenerListadoPaquetes(Connection con, int institucion)
	{
		return objetoDao.obtenerListadoPaquetes(con,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public HashMap consultarAgrupacionArticulosPaquete(Connection con, String codigoPaquete, int institucion)
	{
		return objetoDao.consultarAgrupacionArticulosPaquete(con,codigoPaquete,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public HashMap consultarArticulosPaquete(Connection con, String codigoPaquete, int institucion)
	{
		return objetoDao.consultarArticulosPaquete(con,codigoPaquete,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public HashMap consultarAgrupacionServiciosPaquete(Connection con, String codigoPaquete, int institucion)
	{
		return objetoDao.consultarAgrupacionServiciosPaquete(con,codigoPaquete,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPaquete
	 * @param institucion
	 * @return
	 */
	public HashMap consultarServiciosPaquete(Connection con, String codigoPaquete, int institucion)
	{
		return objetoDao.consultarServiciosPaquete(con,codigoPaquete,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoAgrupacion
	 * @return
	 */
	public boolean eliminarAgrupacionArticulos(Connection con, String codigoAgrupacion)
	{
		return objetoDao.eliminarAgrupacionArticulos(con,codigoAgrupacion);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarAgrupacionArticulosPaqueteLLave(Connection con, String codigo)
	{
		return objetoDao.consultarAgrupacionArticulosPaqueteLLave(con,codigo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarAgrupacionArticulos(Connection con, HashMap vo)
	{
		return objetoDao.modificarAgrupacionArticulos(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarAgrupacionArticulos(Connection con, HashMap vo)
	{
		return objetoDao.insertarAgrupacionArticulos(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param codigoComponente
	 * @return
	 */
	public boolean eliminarArticulos(Connection con, String codigoComponente)
	{
		return objetoDao.eliminarArticulos(con,codigoComponente);
	}

	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public HashMap consultarArticulosPaqueteLLave(Connection con, String codigo)
	{
		return objetoDao.consultarArticulosPaqueteLLave(con,codigo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarArticulos(Connection con, HashMap vo)
	{
		return objetoDao.modificarArticulos(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarArticulos(Connection con, HashMap vo)
	{
		return objetoDao.insertarArticulos(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param codigoAgrupacion
	 * @return
	 */
	public boolean eliminarAgrupacionServicios(Connection con, String codigoAgrupacion)
	{
		return objetoDao.eliminarAgrupacionServicios(con,codigoAgrupacion);
	}

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarAgrupacionServiciosPaqueteLLave(Connection con, String codigo)
	{
		return objetoDao.consultarAgrupacionServiciosPaqueteLLave(con,codigo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarAgrupacionServicios(Connection con, HashMap vo)
	{
		return objetoDao.modificarAgrupacionServicios(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarAgrupacionServicios(Connection con, HashMap vo)
	{
		return objetoDao.insertarAgrupacionServicios(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param codigoComponente
	 * @return
	 */
	public boolean eliminarServicios(Connection con, String codigoComponente)
	{
		return objetoDao.eliminarServicios(con,codigoComponente);
	}

	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public HashMap consultarServiciosPaqueteLLave(Connection con, String codigo)
	{
		return objetoDao.consultarServiciosPaqueteLLave(con,codigo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarServicios(Connection con, HashMap vo)
	{
		return objetoDao.modificarServicios(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarServicios(Connection con, HashMap vo)
	{
		return objetoDao.insertarServicios(con,vo);
	}

}
