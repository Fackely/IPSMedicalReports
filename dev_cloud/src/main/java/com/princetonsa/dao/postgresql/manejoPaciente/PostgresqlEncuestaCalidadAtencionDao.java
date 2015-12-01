package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.EncuestaCalidadAtencionDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseEncuestaCalidadAtencionDao;
import com.princetonsa.mundo.manejoPaciente.EncuestaCalidadAtencion;

/**
 * 
 * @author axioma
 *
 */
public class PostgresqlEncuestaCalidadAtencionDao implements EncuestaCalidadAtencionDao 
{
	
	/** 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultar(Connection con, EncuestaCalidadAtencion encuesta) 
	{
		return SqlBaseEncuestaCalidadAtencionDao.consultar(con, encuesta);	
	}
	
	public HashMap consultarEncuestas(Connection con, int ingreso, String area) 
	{
		return SqlBaseEncuestaCalidadAtencionDao.consultarEncuestas(con, ingreso,area);
	}
	public int guardarEncuestas(Connection con, HashMap encuesta) 
	{
		return SqlBaseEncuestaCalidadAtencionDao.guardarEncuestas(con, encuesta);
	}
	
	public int modificarEncuestas(Connection con, HashMap encuesta) 
	{
		return SqlBaseEncuestaCalidadAtencionDao.modificarEncuestas(con, encuesta);
	}


}
