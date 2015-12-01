package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.NaturalezaArticulosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseNaturalezaArticulosDao;

public class PostgresqlNaturalezaArticulosDao implements NaturalezaArticulosDao {
	
	
	/**
	 * Consultar Existentes
	 * */
	public HashMap consultarNaturalezaArticulosExistentes(Connection con, HashMap vo) 
	{
		return SqlBaseNaturalezaArticulosDao.consultarNaturalezaArticulosExistentes(con,vo);
	}

	/**
	 * Insertar
	 * */
	public boolean insertar(Connection con, HashMap vo)
	{
		return SqlBaseNaturalezaArticulosDao.insertar(con, vo);
	}
	
	/**
	 * Modificar
	 */
	
	public boolean modificar(Connection con, HashMap vo)
	{
		return SqlBaseNaturalezaArticulosDao.modificar(con, vo);
	}
	
	
	/**
	 * Eliminar
	 * */
	public boolean eliminarRegistro(Connection con, int institucion, String acronimo)
	{
		return SqlBaseNaturalezaArticulosDao.eliminarRegistro(con,institucion,acronimo);
	}
	
	/**
	 * 
	 * @param acronimoNaturaleza
	 * @return
	 */
	public boolean esMedicamento(String acronimoNaturaleza)
	{
		return SqlBaseNaturalezaArticulosDao.esMedicamento(acronimoNaturaleza);
	}
}
