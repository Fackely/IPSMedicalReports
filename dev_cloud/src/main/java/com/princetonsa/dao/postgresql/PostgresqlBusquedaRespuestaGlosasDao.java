package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.BusquedaRespuestaGlosasDao;
import com.princetonsa.dao.sqlbase.SqlBaseBusquedaRespuestaGlosasDao;

public class PostgresqlBusquedaRespuestaGlosasDao implements BusquedaRespuestaGlosasDao
{
	/**
	 * Metodo que consulta todas las respuestas glosas segun parametros del mapa cirterios
	 * @param con
	 * @param criterios
	 * @return
	 */
	public HashMap consultarRespuestas(Connection con, HashMap criterios)
	{
		return SqlBaseBusquedaRespuestaGlosasDao.consultarRespuestas(con, criterios);
	}
}