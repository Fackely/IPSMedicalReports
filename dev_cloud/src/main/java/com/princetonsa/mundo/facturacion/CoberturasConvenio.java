/*
 * Creado May 17, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * CoberturasConvenio
 * com.princetonsa.mundo.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.CoberturasConvenioDao;
import com.princetonsa.dto.facturacion.DtoProExeCobConvXCont;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 17, 2007
 */
public class CoberturasConvenio
{
	
	/**
	 * 
	 */
	private CoberturasConvenioDao objetoDao;

	/**
	 * 
	 */
	public CoberturasConvenio()
	{
		this.init(System.getProperty("TIPOBD"));
	}

	private void init(String tipoBD)
	{
		if(objetoDao==null)
		{
			objetoDao=DaoFactory.getDaoFactory(tipoBD).getCoberturasConvenioDao();
		}
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param contrato
	 * @return
	 */
	public HashMap obtenerListadoCoberturas(Connection con, int institucion, int contrato)
	{
		return objetoDao.obtenerListadoCoberturas(con,institucion,contrato);
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param contrato
	 * @return
	 */
	public HashMap obtenerListadoExcepciones(Connection con, int institucion, int contrato)
	{
		return objetoDao.obtenerListadoExcepciones(con,institucion,contrato);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminareExepciones(Connection con, String codigo)
	{
		return objetoDao.eliminareExepciones(con,codigo);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarExcepcionLLave(Connection con, String codigo)
	{
		return objetoDao.consultarExcepcionLLave(con,codigo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarExcepcion(Connection con, HashMap vo)
	{
		return objetoDao.modificarExcepcion(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarExcepcion(Connection con, HashMap vo)
	{
		return objetoDao.insertarExcepcion(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param contrato
	 * @param institucion
	 * @return
	 */
	public boolean eliminarCobertura(Connection con, String codigo, int contrato, int institucion)
	{
		return objetoDao.eliminarCobertura(con,codigo,contrato,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarCobertura(Connection con, HashMap vo)
	{
		return objetoDao.insertarCobertura(con,vo);
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

	/**
	 * 
	 * @param con
	 * @param codigoCobertura
	 * @param contrato
	 * @param institucion
	 * @return
	 */
	public HashMap consultarCoberturaLLave(Connection con, String codigoCobertura, int contrato, int institucion) 
	{
		return objetoDao.consultarCoberturaLLave(con,codigoCobertura,contrato,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarCobertura(Connection con, HashMap vo) 
	{
		return objetoDao.modificarCobertura(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public ArrayList obtenerConvenios(Connection con) 
	{
		return objetoDao.obtenerConvenios(con);
	}
	
	/**
	 * 
	 */
	public boolean insertarProgExcepcionConvenio(Connection con, DtoProExeCobConvXCont dto)
	{
		return objetoDao.insertarProgExcepcionConvenio(con,dto);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoProExeCobConvXCont> consultaProgExcepcionConvenio(Connection con, DtoProExeCobConvXCont dto)
	{
		return objetoDao.consultaProgExcepcionConvenio(con,dto);
	}
	
	/**
	 * 
	 */
	public boolean eliminarProgExcepcionConvenio(Connection con, double excepcion)
	{
		return objetoDao.eliminarProgExcepcionConvenio(con,excepcion);
	}


}
