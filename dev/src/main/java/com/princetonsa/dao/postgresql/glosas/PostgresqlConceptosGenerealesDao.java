package com.princetonsa.dao.postgresql.glosas;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.glosas.ConceptosGeneralesDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseConceptosGeneralesDao;

/**
 * @author Mauricio Jaramillo
 * Fecha: Septiembre de 2008
 */

public class PostgresqlConceptosGenerealesDao implements ConceptosGeneralesDao
{

	/**
	 * 
	 */
	public HashMap consultarConceptosGenerales(Connection con, int codigoInstitucionInt)
	{
		return SqlBaseConceptosGeneralesDao.consultarConceptosGenerales(con, codigoInstitucionInt);
	}
	
	/**
	 * 
	 */
	public boolean insertarConceptoGeneral(Connection con, HashMap criterios)
	{
		return SqlBaseConceptosGeneralesDao.insertarConceptoGeneral(con, criterios);
	}

	/**
	 * 
	 */
	public boolean eliminarConceptoGeneral(Connection con, int criterios)
	{
		return SqlBaseConceptosGeneralesDao.eliminarConceptoGeneral(con, criterios);
	}
	
	/**
	 * 
	 */
	public boolean modificarConceptoGeneral(Connection con, HashMap criterios)
	{
		return SqlBaseConceptosGeneralesDao.modificarConceptoGeneral(con, criterios);
	}
	
}