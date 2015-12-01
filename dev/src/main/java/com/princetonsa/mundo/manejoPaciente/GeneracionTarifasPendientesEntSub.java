package com.princetonsa.mundo.manejoPaciente;
import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.GeneracionTarifasPendientesEntSubDao;


@SuppressWarnings("unchecked")
public class GeneracionTarifasPendientesEntSub
{
	/**
	 * Para manejo de Logs
	 */
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(GeneracionTarifasPendientesEntSub.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static GeneracionTarifasPendientesEntSubDao generacionTarifasPendientesEntSubDao;
	
	/**
	 * Codigo de la institucion
	 */
	@SuppressWarnings("unused")
	private int institucion;
	
	
	@SuppressWarnings("unused")
	private void reset() 
	{
		this.institucion = ConstantesBD.codigoNuncaValido;
	}
	
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			generacionTarifasPendientesEntSubDao = myFactory.getGeneracionTarifasPendientesEntSubDao();
			wasInited = (generacionTarifasPendientesEntSubDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @return
	 */
	private static GeneracionTarifasPendientesEntSubDao getGeneracionTarifasPendientesEntSubDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getGeneracionTarifasPendientesEntSubDao();
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static HashMap buscarAutorizaciones(Connection con, HashMap filtros)
	{
		return getGeneracionTarifasPendientesEntSubDao().buscarAutorizaciones(con, filtros);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static HashMap buscarServiciosAutorizacion(Connection con, String autorizacion)
	{
		return getGeneracionTarifasPendientesEntSubDao().buscarServiciosAutorizacion(con, autorizacion);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static HashMap obtenerErroresServicioAut(Connection con, String autorizacion)
	{
		return getGeneracionTarifasPendientesEntSubDao().obtenerErroresServicioAut(con, autorizacion);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static HashMap buscarArticulosPedidos(Connection con, HashMap filtros)
	{
		return getGeneracionTarifasPendientesEntSubDao().buscarArticulosPedidos(con, filtros);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static HashMap buscarArticulosSolicitudes(Connection con, HashMap filtros)
	{
		return getGeneracionTarifasPendientesEntSubDao().buscarArticulosSolicitudes(con, filtros);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static HashMap obtenerErroresPedidos(Connection con, String codigo)
	{
		return getGeneracionTarifasPendientesEntSubDao().obtenerErroresPedidos(con, codigo);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static HashMap obtenerErroresSolicitudes(Connection con, String codigo)
	{
		return getGeneracionTarifasPendientesEntSubDao().obtenerErroresSolicitudes(con, codigo);
	}
	
	public static HashMap obtenerServiciosTarifados(Connection con, HashMap servicios)
	{
		return getGeneracionTarifasPendientesEntSubDao().obtenerServiciosTarifados(con, servicios);
	}
	
	public static HashMap obtenerPedidosTarifados(Connection con, HashMap pedidos)
	{
		return getGeneracionTarifasPendientesEntSubDao().obtenerPedidosTarifados(con,pedidos);
	}
	
	public static HashMap obtenerSolicitudesTarifados(Connection con, HashMap solicitudes)
	{
		return getGeneracionTarifasPendientesEntSubDao().obtenerSolicitudesTarifados(con,solicitudes); 
	}
	
}