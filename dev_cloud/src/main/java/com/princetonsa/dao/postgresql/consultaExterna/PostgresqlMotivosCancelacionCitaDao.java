package com.princetonsa.dao.postgresql.consultaExterna;
 
import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.consultaExterna.MotivosCancelacionCitaDao;
import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseMotivosCancelacionCitaDao;

public class PostgresqlMotivosCancelacionCitaDao implements
		MotivosCancelacionCitaDao 
		
		
{

	
	/**
	 * 
	 */
	public HashMap consultarMotivoEspecifico(Connection con, int codigoMotivo) 
	{
		return SqlBaseMotivosCancelacionCitaDao.consultarMotivoEspecifico(con, codigoMotivo);
	}

	/**
	 * 
	 */
	public HashMap consultarMotivosExistentes(Connection con,HashMap parametros) 
	{
		return SqlBaseMotivosCancelacionCitaDao.consultarMotivosExistentes(con,parametros);
	}

	/**
	 * 
	 */
	public boolean eliminarRegistro(Connection con, int codigoMotivo) 
	{
		return SqlBaseMotivosCancelacionCitaDao.eliminarRegistro(con, codigoMotivo);
	}

	/**
	 * 
	 */
	public boolean insertar(Connection con, HashMap vo) 
	{
		return SqlBaseMotivosCancelacionCitaDao.insertar(con, vo);
	}

	/**
	 * 
	 */
	public boolean modificar(Connection con, HashMap vo) 
	{
		return SqlBaseMotivosCancelacionCitaDao.modificar(con, vo);
	}
	
	
}
