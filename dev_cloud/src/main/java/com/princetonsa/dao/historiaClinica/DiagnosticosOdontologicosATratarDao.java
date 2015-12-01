package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;





public interface DiagnosticosOdontologicosATratarDao
{

	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public abstract HashMap consultarDiagnosticosATratar(int institucion);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract HashMap consultarDiagnosticosATratarEspecifico(Connection con, int codigo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificar(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertar(Connection con, HashMap vo);
	
}