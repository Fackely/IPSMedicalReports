package com.princetonsa.dao.oracle.ordenesmedicas.procedimientos;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.ordenesmedicas.procedimientos.InterpretarProcedimientoDao;
import com.princetonsa.dao.sqlbase.ordenesmedicas.procedimientos.SqlBaseInterpretarProcedimientoDao;

public class OracleInterpretarProcedimientoDao implements
		InterpretarProcedimientoDao
{

	
	/**
	 * 
	 */
	public HashMap listadoSolicitudesProcedimientosInterpretar(Connection con, HashMap vo)
	{
		return SqlBaseInterpretarProcedimientoDao.listadoSolicitudesProcedimientosInterpretar(con,vo);
	}

	/**
	 * 
	 */
	public boolean interpretarSolicitud(Connection con, String numeroSolicitud, String interpretacion, int codigoPersona)
	{
		return SqlBaseInterpretarProcedimientoDao.interpretarSolicitud(con,numeroSolicitud,interpretacion,codigoPersona);
	}

}
