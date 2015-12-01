package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.manejoPaciente.CalidadAtencion;
import com.princetonsa.mundo.manejoPaciente.EventosAdversos;

public interface CalidadAtencionDao{
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public String crearWhereSatisfaccionGeneral(Connection con, CalidadAtencion mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public String crearWhereMotCalificacionCalidadAtencion(Connection con, CalidadAtencion mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public boolean registrarGeneracionReporte(Connection con, CalidadAtencion mundo);
	
	
}