package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.ConsultaCostoArticulosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseConsultaCostoArticulosDao;

/**
 * @author Mauricio Jaramillo H.
 * Fecha: Agosto de 2008
 */

public class PostgresqlConsultaCostoArticulosDao implements ConsultaCostoArticulosDao
{

	/**
	 * 
	 */
	public String consultarCondicionesCostoArticulos(Connection con, HashMap criterios) 
	{
		return SqlBaseConsultaCostoArticulosDao.consultarCondicionesCostoArticulos(con, criterios);
	}

	/**
	 * 
	 */
	public HashMap consultarCostoArticulos(Connection con, HashMap criterios) 
	{
		return SqlBaseConsultaCostoArticulosDao.consultarCostoArticulos(con, criterios);
	}
	
}