package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.historiaClinica.ValoracionPacientesCuidadosEspeciales;

public interface ValoracionPacientesCuidadosEspecialesDao{
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public HashMap consultar(Connection con, ValoracionPacientesCuidadosEspeciales mundo);
	
	/**
	 * 
	 * @param con
	 * @param mundo
	 * @return
	 */
	public HashMap consultarTiposMonitoreo(Connection con, ValoracionPacientesCuidadosEspeciales mundo);
	
}