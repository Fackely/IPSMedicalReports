/*
 * Ene 17, 2007
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author Sebastián Gómez R.
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>BusquedaDiagnosticosGenerica</code>.
 */
public interface BusquedaDiagnosticosGenericaDao 
{
	/**
	 * Método implementado para consultar la fecha de una cita 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String consultaFechaCita (Connection con, String numeroSolicitud);
	
	/**
	 * Método que consulta el CIE vigente según fecha
	 * @param con
	 * @param fecha
	 * @return
	 */
	public int consultaCieVigente (Connection con, String fecha);
	
	/**
	 * Método que realiza la busqueda de diagnosticos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultaDiagnosticos(Connection con, HashMap campos);
}
