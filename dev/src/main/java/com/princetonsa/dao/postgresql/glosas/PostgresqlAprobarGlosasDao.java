package com.princetonsa.dao.postgresql.glosas;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.glosas.AprobarGlosasDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseAprobarGlosasDao;

public class PostgresqlAprobarGlosasDao implements AprobarGlosasDao
{
	/**
	 * Metodo para consultar los Convenios
	 */
	public HashMap consultarConvenios(Connection con)
	{
		return SqlBaseAprobarGlosasDao.consultarConvenios(con);
	}
	
	/**
	 * Metodo para validar detalle y valor de Glosa
	 */
	public boolean validarAprobarGlosa(Connection con, String codGlosa, String valor)
	{
		return SqlBaseAprobarGlosasDao.validarAprobarGlosa(con, codGlosa, valor);
	}
	
	/**
	 * Metodo que actualiza la Glosa
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean guardar(Connection con, HashMap criterios)
	{
		return SqlBaseAprobarGlosasDao.guardar(con, criterios);
	}
}