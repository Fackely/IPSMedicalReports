package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.ConsultaDevolucionPedidosDao;

public class ConsultaDevolucionPedidos
{
	private static Logger logger = Logger.getLogger(SustitutosNoPos.class);
	
	private static ConsultaDevolucionPedidosDao getConsultaDevolucionPedidosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultaDevolucionPedidosDao();		
	}
	
	/**
	 * Metodo de consulta de los centros de costo relacionados con el centro de atencion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */	
	public static HashMap<String, Object> consultaCentroCosto (Connection con, int centroAtencion)
	{
		return getConsultaDevolucionPedidosDao().consultaCentroCosto(con, centroAtencion);
	}
	
	/**
	 * Metodo de consulta de los estados Devolucion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */	
	public static HashMap<String, Object> consultaEstadosDevolucion (Connection con)
	{
		return getConsultaDevolucionPedidosDao().consultaEstadosDevolucion(con);
	}
	
	/**
	 * Metodo de consulta de los Motivos Devolucion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */	
	public static HashMap<String, Object> consultaMotivosDevolucion (Connection con)
	{
		return getConsultaDevolucionPedidosDao().consultaMotivosDevolucion(con);
	}
	
	/**
	 * Metodo de consulta de las Devoluciones
	 * @param con
	 * @param centroAtencion
	 * @return
	 */	
	public static HashMap<String, Object> consultaDevoluciones (Connection con, String numeroDevolucion, String centroAtencion, String almacen, String centroCosto, String fechaIni, String fechaFin, String estadoDevolucion, String check, String motivoDevolucion, String usuarioDevuelve, String usuarioRecibe)
	{
		return getConsultaDevolucionPedidosDao().consultaDevoluciones(con, numeroDevolucion, centroAtencion, almacen, centroCosto, fechaIni, fechaFin, estadoDevolucion, check, motivoDevolucion, usuarioDevuelve, usuarioRecibe);
	}
	
	/**
	 * Metodo de consulta de los Detalles de las Devoluciones
	 * @param con
	 * @param centroAtencion
	 * @return
	 */	
	public static HashMap<String, Object> consultaDetalleDevoluciones (Connection con, String codigoDevolucion)
	{
		return getConsultaDevolucionPedidosDao().consultaDetalleDevoluciones(con, codigoDevolucion);
	}
	
}