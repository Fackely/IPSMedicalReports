package com.princetonsa.mundo;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.BusquedaInstitucionesSircGenericaDao;
import com.princetonsa.dao.DaoFactory;

public class BusquedaInstitucionesSircGenerica
{
	
	/**
	 * 
	 * */
	public static BusquedaInstitucionesSircGenericaDao busquedaInstitucionesSircGenericaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBusquedaInstitucionesSircGenericaDao();		
	}
	
	/**
	 * Consulta de instituciones SIRC
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultarInstitucioneSirc(Connection con, HashMap parametros)
	{		
		return busquedaInstitucionesSircGenericaDao().consultarInstitucioneSirc(con, parametros);		
	}	
}