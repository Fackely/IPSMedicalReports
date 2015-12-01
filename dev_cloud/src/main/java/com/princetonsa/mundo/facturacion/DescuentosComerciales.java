/*
 * Creado May 22, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * DescuentosComerciales
 * com.princetonsa.mundo.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.DescuentosComercialesDao;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturacion.DtoProgDescComercialConvenioContrato;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 22, 2007
 */
public class DescuentosComerciales
{

	private DescuentosComercialesDao objetoDao;
	
	/**
	 * 
	 */
	public DescuentosComerciales()
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
			objetoDao=DaoFactory.getDaoFactory(tipoBD).getDescuentosComercialesDao();
		}
	}

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param contrato
	 * @return
	 */
	public HashMap obtenerListadoViasDescuento(Connection con, int institucion, int contrato,boolean vigentes,boolean todas)
	{
		return objetoDao.obtenerListadoViasDescuento(con,institucion,contrato,vigentes,todas);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarViaDescuento(Connection con, String codigo)
	{
		return objetoDao.eliminarViaDescuento(con,codigo);
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarViaDescuentoLLave(Connection con, String codigo)
	{
		return objetoDao.consultarViaDescuentoLLave(con,codigo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarViaDescuento(Connection con, HashMap vo)
	{
		return objetoDao.modificarViaDescuento(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarViaDescuento(Connection con, HashMap vo)
	{
		return objetoDao.insertarViaDescuento(con,vo);
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
	 * @param dto
	 * @return
	 */
	
	public static  ArrayList<DtoProgDescComercialConvenioContrato> cargarProgDescComercialContrato(	DtoProgDescComercialConvenioContrato dto) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDescuentosComercialesDao().cargarProgDescComercialContrato(dto);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean eliminarProgDescComercialContrato(Connection con, DtoProgDescComercialConvenioContrato dto) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDescuentosComercialesDao().eliminarProgDescComercialContrato(con, dto);

	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean insertProgDescComercialConvenioContrato(Connection con,
			DtoProgDescComercialConvenioContrato dto) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDescuentosComercialesDao().insertProgDescComercialConvenioContrato(con, dto);

	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean modificarProgDescComercialConvenioContrato(Connection con, DtoProgDescComercialConvenioContrato dto) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDescuentosComercialesDao().modificarProgDescComercialConvenioContrato(con , dto);
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static int validarTipoAtencionOdontologica(DtoConvenio dto){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDescuentosComercialesDao().validarTipoAtencionOdontologica(dto);
	}

	
	
	
}
