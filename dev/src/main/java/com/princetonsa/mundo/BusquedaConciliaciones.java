package com.princetonsa.mundo;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.BusquedaConciliacionesDao;
import com.princetonsa.dao.BusquedaRespuestaGlosasDao;
import com.princetonsa.dao.DaoFactory;


public class BusquedaConciliaciones
{
	/**
	 * Variable para el manejo de Logs
	 */
	private Logger logger = Logger.getLogger(BusquedaConciliaciones.class);
	
	/**
	 * Retorno del get del Dao Factory
	 * @return
	 */
	private static BusquedaConciliacionesDao getBusquedaConciliacionesDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBusquedaConciliacionesDao(); 
	}

	public HashMap consultarConciliaciones(HashMap<String, Object> mapa) {
		return getBusquedaConciliacionesDao().consultarConciliaciones(mapa);
	}
}