package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Giovanny Arias
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * JustificacionNoPosServ
 */
public interface JustificacionNoPosServDao 
{
	/**
	 * M�todo que consulta las justificaciones de servicios No POS que se encuentran pendiente por determinada cuenta
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public HashMap consultarSolicitudesJustificaciones(Connection con, int cuenta);
	
	/**
	 * M�todo que consulta las justificaciones de servicios No POS que se encuentran pendiente por determinada cuenta
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public HashMap consultarSolicitudesJustificacionesDiligenciadas(Connection con, int cuenta);
	
	/**
	 * 
	 * @param con
	 * @param filtros
	 * @param codigosServicios
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public HashMap cargarInfoIngresoRango(Connection con, HashMap filtros, String codigosServicios, String fechaInicial, String fechaFinal);
	
	/**
	 * 
	 * @param con
	 * @param filtros
	 * @param codigosServicios
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public HashMap cargarInfoIngresoConsultarModificarRango(Connection con, HashMap filtros, String codigosServicios, String fechaInicial, String fechaFinal);

	/**
	 * 
	 * @param con
	 * @param filtros
	 * @param codigosServicios
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public HashMap cargarInfoIngresoConsultarModificarRangoCon(Connection con, HashMap filtros, String codigosServicios, String fechaInicial, String fechaFinal);
	
}