package com.princetonsa.dao.postgresql.glosas;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.glosas.RadicarRespuestasDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseRadicarRespuestasDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseRegistrarModificarGlosasDao;

public class PostgresqlRadicarRespuestasDao implements RadicarRespuestasDao
{
	/**
	 * Metodo que acualiza la Respuesta
	 */
	public boolean guardar(Connection con, HashMap criterios)
	{
		return SqlBaseRadicarRespuestasDao.guardar(con, criterios);
	}
}