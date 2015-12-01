package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.List;

import com.servinte.axioma.orm.InconsistenSubirPaciente;

public interface IInconsistenSubirPacienteMundo {

	/**
	 * 
	 * @param inconsistenSubirPaciente
	 * @return
	 * @author Camilo Gómez
	 */
	public boolean guardarInconsistenciaSubirPaciente(InconsistenSubirPaciente inconsistenSubirPaciente);
	
	/**
	 * Lista las inconsistencias asociadas al log de subir pacientes
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoLogSubirPaciente
	 * @return List<InconsistenSubirPaciente>
	*/
	public List<InconsistenSubirPaciente> buscarInconsistenciasPorLog(long codigoLogSubirPaciente);
}
