/*
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 11, 2007
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosString;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.DetalleCoberturaDao;
import com.princetonsa.dto.facturacion.DtoCoberturaProgramas;
import com.princetonsa.dto.facturacion.DtoCoberturaServicios;


/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 11, 2007
 */
public class DetalleCobertura
{
	/**
	 * 
	 */
	DetalleCoberturaDao objetoDao;

	/**
	 * 
	 */
	public DetalleCobertura()
	{
		this.init(System.getProperty("TIPOBD"));
	}

	private void init(String tipoBD)
	{
		if(objetoDao==null)
		{
			objetoDao=DaoFactory.getDaoFactory(tipoBD).getDetalleCoberturaDao();
		}
	}

	/**
	 * Metodo que obtiene el detalle de coberturas
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap obtenerListadoCoberturas(Connection con, int institucion)
	{
		return objetoDao.obtenerListadoCoberturas(con,institucion);
	}

	/**
	 * 
	 * @param con
	 * @param codigoDetCobertura
	 * @return
	 */
	public boolean eliminarDetalleCobertura(Connection con, String codigoDetCobertura)
	{
		return objetoDao.eliminarDetalleCobertura(con,codigoDetCobertura);
	}

	/**
	 * 
	 * @param con
	 * @param codigoDetallecobertura
	 * @return
	 */
	public HashMap consultarDetalleCoberturaLLave(Connection con, String codigoDetallecobertura)
	{
		return objetoDao.consultarDetalleCoberturaLLave(con,codigoDetallecobertura);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarDetalleCobertura(Connection con, HashMap vo)
	{
		return objetoDao.modificarDetalleCobertura(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarDetalleCobertura(Connection con, HashMap vo)
	{
		return objetoDao.insertarDetalleCobertura(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public Vector<InfoDatosString> obtenerListadoCoberturasInstitucion(Connection con, int institucion)
	{
		return objetoDao.obtenerListadoCoberturasInstitucion(con,institucion);
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
	public HashMap consultarAgrupacionArticulosLLave(Connection con, String codigo)
	{
		return objetoDao.consultarAgrupacionArticulosLLave(con,codigo);
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
	public HashMap consultarArticulosLLave(Connection con, String codigo)
	{
		return objetoDao.consultarArticulosLLave(con,codigo);
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
	public HashMap consultarAgrupacionServiciosLLave(Connection con, String codigo)
	{
		return objetoDao.consultarAgrupacionServiciosLLave(con,codigo);
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
	public HashMap consultarServiciosLLave(Connection con, String codigo)
	{
		return objetoDao.consultarServiciosLLave(con,codigo);
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
	 */
	public boolean insertarCoberturaProgramas(Connection con, DtoCoberturaProgramas dto)
	{
		return objetoDao.insertarCoberturaProgramas(con,dto);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoCoberturaProgramas> consultarCoberturaProgramas (Connection con, DtoCoberturaProgramas dto)
	{
		return objetoDao.consultarCoberturaProgramas(con,dto);
	}

	/**
	 * 
	 */
	public boolean eliminarCoberturaProgramas(Connection con, double codigoCoberturaAEliminar)
	{
		return objetoDao.eliminarCoberturaProgramas(con, codigoCoberturaAEliminar);
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoCoberturaServicios> consultarCoberturaServicios(Connection con, DtoCoberturaServicios dto)
	{
		return objetoDao.consultarCoberturaServicios(con,dto);
	}
	
	/**
	 * 
	 */
	public boolean insertarCoberturaServicios(Connection con, DtoCoberturaServicios dto)
	{
		return objetoDao.insertarCoberturaServicios(con,dto);
	}
	
	/**
	 * 
	 */
	public boolean eliminarCoberturaServicios(Connection con, double codigoCoberturaAEliminar)
	{
		return objetoDao.eliminarCoberturaServicios(con, codigoCoberturaAEliminar);
	}
}
