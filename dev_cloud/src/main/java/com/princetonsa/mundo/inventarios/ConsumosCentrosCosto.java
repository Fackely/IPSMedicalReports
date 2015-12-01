package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.ConsumosCentrosCostoDao;


public class ConsumosCentrosCosto
{
	private static Logger logger = Logger.getLogger(SustitutosNoPos.class);
	
	private static ConsumosCentrosCostoDao getConsumosCentrosCostoDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsumosCentrosCostoDao();		
	}
	
	/**
	 * Metodo de consulta de los centros de costo relacionados con el centro de atencion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */	
	public static HashMap<String, Object> consultaCentroCosto (Connection con, int centroAtencion)
	{
		return getConsumosCentrosCostoDao().consultaCentroCosto(con, centroAtencion);
	}
	
	/**
	 * Metodo de consulta de las clases
	 * @param con
	 * @param centroAtencion
	 * @return
	 */	
	public static HashMap<String, Object> consultaClases (Connection con)
	{
		return getConsumosCentrosCostoDao().consultaClases(con);
	}
	
	/**
	 * Metodo de consulta de los Pedidos
	 * @return
	 */	
	public static HashMap<String, Object> consultaPedidos (Connection con, String fechaInicial, String fechaFinal, int centroAtencion, String almacen, String centroCosto, String clase)
	{
		return getConsumosCentrosCostoDao().consultaPedidos(con, fechaInicial, fechaFinal, centroAtencion, almacen, centroCosto, clase);
	}
}