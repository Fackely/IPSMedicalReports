package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.manejoPaciente.EventosAdversos;

public interface EventosAdversosDao{
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public HashMap consultar(Connection con, EventosAdversos mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public boolean ingresar(Connection con, EventosAdversos mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public boolean modificar(Connection con, EventosAdversos mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public boolean inactivar(Connection con, EventosAdversos mundo);
}