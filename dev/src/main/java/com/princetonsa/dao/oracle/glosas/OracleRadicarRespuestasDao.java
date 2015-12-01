package com.princetonsa.dao.oracle.glosas;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.glosas.RadicarRespuestasDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseRadicarRespuestasDao;

public class OracleRadicarRespuestasDao implements RadicarRespuestasDao
{
	/**
	 * Metodo que acualiza la Respuesta
	 */
	public boolean guardar(Connection con, HashMap criterios)
	{
		return SqlBaseRadicarRespuestasDao.guardar(con, criterios);
	}
}