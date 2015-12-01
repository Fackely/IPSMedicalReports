package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.manejoPaciente.EstadisticasServicios;
import com.princetonsa.mundo.manejoPaciente.EventosAdversos;

public interface EstadisticasServiciosDao{
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public String crearWhereServiciosRealizados(Connection con, EstadisticasServicios mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public String crearWhereServiciosRealizadosXConvenio(Connection con, EstadisticasServicios mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public String crearWhereServiciosRealizadosXEspecialidad(Connection con, EstadisticasServicios mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public String crearCampoEgresosPorMes(Connection con, EstadisticasServicios mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public boolean registrarGeneracionReporte(Connection con, EstadisticasServicios mundo);
	
	
}