/**
 * 
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ExcepcionesTarifas1Dao;

/**
 * @author axioma
 *
 */
public class ExcepcionesTarifas 
{
	ExcepcionesTarifas1Dao objetoDao;

	/**
	 * 
	 */
	public ExcepcionesTarifas() 
	{
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 * @param property
	 */
	private void init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			objetoDao=DaoFactory.getDaoFactory(tipoBD).getExcepcionesTarifas1Dao();
		}
	}


	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param contrato 
	 * @return
	 */
	public HashMap obtenerExcepciones(Connection con, int institucion, int contrato,boolean vigentes,boolean todas)
	{
		return objetoDao.obtenerExcepciones(con,institucion,contrato,vigentes,todas);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarExepciones(Connection con, String codigo)
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
	public int insertarAgrupacionServicios(Connection con, HashMap vo)
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
	public int insertarArticulos(Connection con, HashMap vo)
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
	public int insertarAgrupacionArticulos(Connection con, HashMap vo)
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
	public int insertarServicios(Connection con, HashMap vo)
	{
		return objetoDao.insertarServicios(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param i
	 * @param codigoSeccion
	 * @return
	 */
	public HashMap<String, Object> consultarPorcentaje(Connection con, String codigosPKSeccion,int codigoSeccion) 
	{
		HashMap<String, Object> mapa=new HashMap<String, Object>();
		if(!codigosPKSeccion.trim().equals(ConstantesBD.codigoNuncaValido+""))
		{
			mapa=objetoDao.consultarPorcentaje(con,codigosPKSeccion,codigoSeccion);
		}
		return mapa;
		
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param codigoSeccion
	 * @return
	 */
	public boolean modificarPorcentaje(Connection con, HashMap vo, int codigoSeccion) 
	{
		return objetoDao.modificarPorcentaje(con,vo,codigoSeccion);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @param codigoSeccion
	 * @return
	 */
	public boolean insertarPorcentaje(Connection con, HashMap vo, int codigoSeccion) 
	{
		return objetoDao.insertarPorcentaje(con,vo,codigoSeccion);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean eliminarPorcentajes(Connection con, String codigo,int codigoSeccion) 
	{
		return objetoDao.eliminarPorcentajes(con,codigo,codigoSeccion);
	}

	public HashMap consultarPorcentajeLLave(Connection con, int codigo,int codigoSeccion, int posRegistroExcepcion) 
	{
		return objetoDao.consultarPorcentajeLLave(con,codigo,codigoSeccion,posRegistroExcepcion);
	}
	

}
