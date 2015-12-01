package com.princetonsa.dao.cargos;

import java.sql.Connection;
import java.util.HashMap;

public interface CargosAutomaticosPresupuestoDao 
{
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public abstract HashMap cargarServiciosAutomaticos(Connection con, int codigoPaciente);
	
	/**
	 * 
	 * @param con
	 * @param servicios
	 * @return
	 */
	public abstract HashMap obtenerCentrosCosto(Connection con, String servicios);

	public abstract HashMap cargarTarifas(Connection con, int numeroSolicitud, int servicio);
	
	/**
	 * Metodo que valida si el paciente tiene asiciado un presupuesto
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public abstract boolean pacienteTienePresupuesto(Connection con,int codigoPaciente);

}
