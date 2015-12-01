package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.HashMap;
import com.princetonsa.dao.BusquedaInstitucionesSircGenericaDao;
import com.princetonsa.dao.sqlbase.SqlBaseBusquedaInstitucionesSircDao;

public class PostgresqlBusquedaInstitucionesSircGenericaDao implements BusquedaInstitucionesSircGenericaDao 
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