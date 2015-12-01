package com.princetonsa.dao.glosas;

import java.sql.Connection;
import java.util.HashMap;

public interface RadicarRespuestasDao
{
	/**
	 * Metodo que actualiza la Respuesta
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean guardar(Connection con, HashMap criterios);
}