package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.ParametrizacionPlantillasDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseParametrizacionPlantillasDao;

public class OracleParametrizacionPlantillasDao implements
		ParametrizacionPlantillasDao 
		
{

	/**
	 * 
	 */
	public HashMap consultarEspecialidades(Connection con) 
	{
		return SqlBaseParametrizacionPlantillasDao.consultarEspecialidades(con);
	}

	/**
	 * 
	 */
	public HashMap consultarPlantillas(Connection con, String plantilla, String centroCosto, String sexo) 
	{
		return SqlBaseParametrizacionPlantillasDao.consultarPlantillas(con, plantilla, centroCosto, sexo);
	}
	
	/**
	 * 
	 */
	public HashMap consultaSeccionesFijas(Connection con, String plantillaBase, String codigoInstitucion) 
	{
		return SqlBaseParametrizacionPlantillasDao.consultaSeccionesFijas(con, plantillaBase, codigoInstitucion);
	}

	
	
}
