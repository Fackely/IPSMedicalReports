package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.manejoPaciente.EstadisticasIngresos;
import com.princetonsa.mundo.manejoPaciente.EstadisticasServicios;
import com.princetonsa.mundo.manejoPaciente.EventosAdversos;

public interface EstadisticasIngresosDao{
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public String crearWhereIngresosXConvenio(Connection con, EstadisticasIngresos mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public String crearWhereReingresos(Connection con, EstadisticasIngresos mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public String crearWhereTotalReingresosXConvenio(Connection con, EstadisticasIngresos mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public String crearConsultaAtencionXRangoEdad(Connection con, EstadisticasIngresos mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public HashMap consultarAtencionXEmpresaYConvenio(Connection con, EstadisticasIngresos mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public int consultarEgresosMes(Connection con, String anioMes, HashMap filtro);
	
	
}