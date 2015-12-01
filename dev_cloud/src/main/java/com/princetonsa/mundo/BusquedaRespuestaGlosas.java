package com.princetonsa.mundo;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.BusquedaRespuestaGlosasDao;
import com.princetonsa.dao.DaoFactory;

public class BusquedaRespuestaGlosas
{
	/**
	 * Variable para el manejo de Logs
	 */
	private Logger logger = Logger.getLogger(BusquedaRespuestaGlosas.class);
	
	/**
	 * Retorno del get del Dao Factory
	 * @return
	 */
	private static BusquedaRespuestaGlosasDao getBusquedaRespuestaGlosasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBusquedaRespuestaGlosasDao(); 
	}
	
	/**
	 * Metodo que consulta todas las respuestas glosas segun parametros del mapa criterios
	 * @param con
	 * @param criterios
	 * @return
	 */
	public HashMap consultarRespuestas(Connection con, HashMap criterios)
	{
		return getBusquedaRespuestaGlosasDao().consultarRespuestas(con, criterios);
	}
}