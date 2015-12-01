package com.mercury.dao.postgresql.odontologia;

import java.sql.Connection;
import java.util.HashMap;

import com.mercury.dao.odontologia.CartaDentalDao;
import com.mercury.dao.sqlbase.odontologia.SqlBaseCartaDentalDao;
import com.mercury.dto.odontologia.DtoCartaDental;

public class PostgresqlCartaDentalDao implements CartaDentalDao
{
	/**
	 * Inserta la informacion de la carta dental
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int insertarCartaDental(Connection con,HashMap parametros)
	{
		return SqlBaseCartaDentalDao.insertarCartaDental(con, parametros);
	}
	
	/**
	 * Inserta la informacion del diagnostico de la carta dental
	 *  @param Connection con
	 *  @param HashMap parametros  
	 * */
	public int insertarDiagnosticoCartaDental(Connection con, HashMap parametros)
	{
		return SqlBaseCartaDentalDao.insertarDiagnosticoCartaDental(con, parametros);
	}
	
	/**
	 * Inserta la informacion del tratamiento de la carta dental
	 *  @param Connection con
	 *  @param HashMap parametros  
	 * */
	public int insertarTratamientoCartaDental(Connection con, HashMap parametros)
	{
		return SqlBaseCartaDentalDao.insertarTratamientoCartaDental(con, parametros);
	}
	
	/**
	 * Consulta la informacion de la carta dental
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public DtoCartaDental cargarCartaDental(Connection con, HashMap parametros)
	{
		return SqlBaseCartaDentalDao.cargarCartaDental(con, parametros);
	}
	
	/**
	 * Elimina la informarcion de diagnosticos de la carta dental 
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean eliminarDiagnosticoCartaDental(Connection con, HashMap parametros)
	{
		return SqlBaseCartaDentalDao.eliminarDiagnosticoCartaDental(con, parametros);
	}
	
	/**
	 * Elimina la informarcion de tratamientos de la carta dental 
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean modificarActivoTratamientosCartaDental(Connection con, HashMap parametros)
	{
		return SqlBaseCartaDentalDao.modificarActivoTratamientosCartaDental(con, parametros);
	}
	
	/**
	 * Actualiza la informacion del diente en los tratamientos y los diagnosticos
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarDienteDiagTrataCartaDental(Connection con, HashMap parametros)
	{
		return SqlBaseCartaDentalDao.actualizarDienteDiagTrataCartaDental(con, parametros);
	}
	
	/**
	 * Actualiza la informacion de la superficie de un diagnostico de la carta dental
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarSuperficieDiagTrataCartaDental(Connection con, HashMap parametros)
	{
		return SqlBaseCartaDentalDao.actualizarSuperficieDiagTrataCartaDental(con, parametros);
	}
}