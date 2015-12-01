package com.princetonsa.mundo.glosas;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.RadicarRespuestasDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseRadicarRespuestasDao;

public class RadicarRespuestas
{
	Logger logger = Logger.getLogger(RadicarRespuestas.class);
	private static RadicarRespuestasDao getRadicarRespuestasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRadicarRespuestasDao();
	}
	
	/**
	 * Metodo que acualiza la Respuesta
	 */
	public boolean guardar(Connection con, String codrespuesta, String fecha, String numResp, String observ, String usuario)
	{
		HashMap<String, Object> criterios = new HashMap<String, Object>();
		criterios.put("codrespuesta", codrespuesta);
		criterios.put("fecha", fecha);
		criterios.put("numero", numResp);
		criterios.put("observ", observ);
		criterios.put("usuario", usuario);
		
		return getRadicarRespuestasDao().guardar(con, criterios);
	}
}