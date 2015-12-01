package com.princetonsa.dao.ordenesmedicas.procedimientos;

import java.sql.Connection;
import java.util.HashMap;

public interface InterpretarProcedimientoDao
{

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap listadoSolicitudesProcedimientosInterpretar(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param interpretacion
	 * @param codigoPersona
	 * @return
	 */
	public abstract boolean interpretarSolicitud(Connection con, String numeroSolicitud, String interpretacion, int codigoPersona);

}
