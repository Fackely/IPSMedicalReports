package com.princetonsa.dao.manejoPaciente;

import com.princetonsa.mundo.manejoPaciente.MotivoCierreAperturaIngresos;
import java.sql.Connection;
import java.util.HashMap;


public interface MotivoCierreAperturaIngresosDao{
	
	/**
	 * 
	 * @param con
	 * @param motivoCierreAperturaIngresos
	 * @return
	 */
	public boolean insertarMotivoCierreAperturaIngresos(Connection con, MotivoCierreAperturaIngresos motivoCierreAperturaIngresos);

	
	/**
	 * 
	 * @param con
	 * @param motivoCierreAperturaIngresos
	 * @return
	 */
	public boolean modificarMotivoCierreAperturaIngresos(Connection con, MotivoCierreAperturaIngresos motivoCierreAperturaIngresos);
	
	
	/**
	 * 
	 * @param con
	 * @param motivoCierreAperturaIngresos
	 * @return
	 */
	public boolean eliminarMotivoCierreAperturaIngresos(Connection con, String motivoCierreAperturaIngresos);
	
	
	/**
	 * 
	 * @param con
	 * @param motivoCierreAperturaIngresos
	 * @return
	 */
	public HashMap consultarMotivoCierreAperturaIngresos(Connection con, MotivoCierreAperturaIngresos motivoCierreAperturaIngresos);
	
}