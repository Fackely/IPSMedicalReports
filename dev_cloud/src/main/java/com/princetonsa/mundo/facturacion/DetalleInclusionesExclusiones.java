/*
 * Creado May 22, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * DetalleInclusionesExclusiones
 * com.princetonsa.mundo.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.DetalleInclusionesExclusionesDao;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 22, 2007
 */
public class DetalleInclusionesExclusiones
{
	
	DetalleInclusionesExclusionesDao objetoDao;

	/**
	 * 
	 */
	public DetalleInclusionesExclusiones()
	{
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 * @param tipoBD
	 */
	private void init(String tipoBD)
	{
		if(objetoDao==null)
		{
			objetoDao=DaoFactory.getDaoFactory(tipoBD).getDetalleInclusionesExclusionesDao();
			
		}
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap consultarInclusionesExclusiones(Connection con, int institucion)
	{
		return objetoDao.consultarInclusionesExclusiones(con,institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarInclusionesExclusiones(Connection con, String codigo)
	{
		return objetoDao.eliminarInclusionesExclusiones(con,codigo);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarInclusionExclusionLLave(Connection con, String codigo)
	{
		return objetoDao.consultarInclusionExclusionLLave(con,codigo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarInclusionExclusion(Connection con, HashMap vo)
	{
		return objetoDao.modificarInclusionExclusion(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarInclusionExclusion(Connection con, HashMap vo)
	{
		return objetoDao.insertarInclusionExclusion(con,vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion 
	 * @return
	 */
	public HashMap consultarAgrupacionArticulos(Connection con, String codigo)
	{
		return objetoDao.consultarAgrupacionArticulos(con,codigo);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion 
	 * @return
	 */
	public HashMap consultarArticulos(Connection con, String codigo)
	{
		return objetoDao.consultarArticulos(con,codigo);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion 
	 * @return
	 */
	public HashMap consultarAgrupacionServicios(Connection con, String codigo)
	{
		return objetoDao.consultarAgrupacionServicios(con,codigo);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion 
	 * @return
	 */
	public HashMap consultarServicios(Connection con, String codigo)
	{
		return objetoDao.consultarServicios(con,codigo);
	}


	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarAgrupacionArticulosLLave(Connection con, String codigo)
	{
		return objetoDao.consultarAgrupacionArticulosLLave(con,codigo);
	}


	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public HashMap consultarArticulosLLave(Connection con, String codigo)
	{
		return objetoDao.consultarArticulosLLave(con,codigo);
	}

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarAgrupacionServiciosLLave(Connection con, String codigo)
	{
		return objetoDao.consultarAgrupacionServiciosLLave(con,codigo);
	}


	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public HashMap consultarServiciosLLave(Connection con, String codigo)
	{
		return objetoDao.consultarServiciosLLave(con,codigo);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarAgrupacionServicios(Connection con, String codigo)
	{
		return objetoDao.eliminarAgrupacionServicios(con,codigo);
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
	 * @param codigo
	 * @return
	 */
	public boolean eliminarArticulos(Connection con, String codigo)
	{
		return objetoDao.eliminarArticulos(con,codigo);
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
	 * @param string
	 * @return
	 */
	public boolean eliminarAgrupacionArticulos(Connection con, String codigo)
	{
		return objetoDao.eliminarAgrupacionArticulos(con,codigo);
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
	 * @param string
	 * @return
	 */
	public boolean eliminarServicios(Connection con, String codigo)
	{
		return objetoDao.eliminarServicios(con,codigo);
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
