package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Giovanny Arias
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * JustificacionNoPosServ
 */
public interface JustificacionNoPosServDao 
{
	/**
	 * Método que consulta las justificaciones de servicios No POS que se encuentran pendiente por determinada cuenta
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public HashMap consultarSolicitudesJustificaciones(Connection con, int cuenta);
	
	/**
	 * Método que consulta las justificaciones de servicios No POS que se encuentran pendiente por determinada cuenta
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