package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

public interface BusquedaRespuestaGlosasDao
{
	/**
	 * Metodo que consulta todas las respuestas glosas segun parametros del mapa criterios
	 * @param con
	 * @param criterios
	 * @return
	 */
	public HashMap consultarRespuestas(Connection con, HashMap criterios);
}