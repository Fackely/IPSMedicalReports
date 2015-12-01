package com.princetonsa.mundo.ordenesmedicas.procedimientos;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ordenesmedicas.procedimientos.InterpretarProcedimientoDao;

public class InterpretarProcedimiento
{

	/**
	 * 
	 */
	InterpretarProcedimientoDao objetoDao;

	public InterpretarProcedimiento()
	{
		this.reset();
		this.init(System.getProperty("TIPOBD"));
	}

	private void reset()
	{
		
		
	}

	private void init(String tipoBD)
	{
		if (objetoDao == null) 
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			objetoDao = myFactory.getInterpretarProcedimientoDao();
		}
	}

	public HashMap generarListadoSolicitudesProcedimientosResponder(Connection con, HashMap vo)
	{
		return objetoDao.listadoSolicitudesProcedimientosInterpretar(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param interpretacion
	 * @param codigoPersona
	 */
	public boolean interpretarSolicitud(Connection con, String numeroSolicitud, String interpretacion, int codigoPersona)
	{
		return objetoDao.interpretarSolicitud(con,numeroSolicitud,interpretacion,codigoPersona);
	}
}
