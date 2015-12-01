package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.salasCirugia.AsocioSalaCirugiaDao;


public class AsocioSalaCirugia
{
	
	/**
	 * Instancia el Dao
	 * */
	public static AsocioSalaCirugiaDao getAsocioSalaCirugiaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAsocioSalaCirugiaDao();
	}	
	
	
	/**
	 * Consulta la informacion de los asocios de Cirugia
	 * @param Connection con
	 * @param int institucion
	 * */
	public static HashMap consultaAsocioSalaCirugia(Connection con, int institucion)
	{
		return getAsocioSalaCirugiaDao().consultaAsocioSalaCirugia(con,institucion);
	}
	
	
	/**
	 * Consulta la informacion de los asocios de Cirugia por medio del acronimo
	 * @param Connection con
	 * @param int institucion 
	 * @param int codigoAsocio
	 * */
	public static HashMap consultaAsocioSalaCirugia(Connection con, int institucion,String codigoAsocio) 
	{	
		return getAsocioSalaCirugiaDao().consultaAsocioSalaCirugia(con, institucion, codigoAsocio);
	}
	
	/**
	 * Actualiza la informacion de los asocios de Cirugia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean actualizarAsocioSalaCirugia(Connection con, HashMap parametros)
	{
		return getAsocioSalaCirugiaDao().actualizarAsocioSalaCirugia(con, parametros);
	}
	
	
	/**
	 * Inserta informacion de los asocios de Cirugia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean insertarAsocioSalaCirugia(Connection con, HashMap parametros)
	{
		return getAsocioSalaCirugiaDao().insertarAsocioSalaCirugia(con, parametros);
	}

	
	/**
	 * Elimina informacion de los asocios de Cirugia
	 * @param Connection con
	 * @param int institucion
	 * @param String codigoasocio
	 * */
	public static boolean eliminaAsocioSalaCirugia(Connection con, int institucion, String codigoasocio)
	{
		return getAsocioSalaCirugiaDao().eliminaAsocioSalaCirugia(con,institucion,codigoasocio);
	}
	
	
	/**
	 * Consulta de Tipos de Servicio
	 * @param Connection con
	 * */
	public static ArrayList<HashMap<String,Object>> consultaTiposServicios(Connection con)
	{
		return getAsocioSalaCirugiaDao().consultaTiposServicios(con);
	}		
}