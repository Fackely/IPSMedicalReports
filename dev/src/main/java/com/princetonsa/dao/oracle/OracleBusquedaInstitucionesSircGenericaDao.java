package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;
import com.princetonsa.dao.BusquedaInstitucionesSircGenericaDao;
import com.princetonsa.dao.sqlbase.SqlBaseBusquedaInstitucionesSircDao;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
public class OracleBusquedaInstitucionesSircGenericaDao implements BusquedaInstitucionesSircGenericaDao 
{
	/**
	 * Consulta de instituciones SIRC
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarInstitucioneSirc(Connection con, HashMap parametros)
	{
		return SqlBaseBusquedaInstitucionesSircDao.consultarInstitucioneSirc(con, parametros);				
	}	
}