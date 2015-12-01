package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
public interface BusquedaInstitucionesSircGenericaDao
{
	/**
	 * Consulta de instituciones SIRC
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarInstitucioneSirc(Connection con, HashMap parametros);	
}