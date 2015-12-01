package com.princetonsa.mundo;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.BusquedaGlosasDao;
import com.princetonsa.dao.DaoFactory;
import java.sql.Connection;


public class BusquedaGlosas 
{
	/*
	 * Variable para el manejo de loggs
	 */
	private Logger logger=  Logger.getLogger(BusquedaGlosas.class);
	
	/**
	 * Retornamos el get del DaoFactory
	 * @return
	 */
	private static BusquedaGlosasDao getBusquedaGlosasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBusquedaGlosasDao();
	}
	
	/**
	 * Metodo que consulta las glosas segun parametros
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static HashMap<String, Object> consultarGlosas(Connection con, HashMap criterios)
	{
		return getBusquedaGlosasDao().consultarGlosas(con,criterios);
	}
}