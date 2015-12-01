package com.servinte.axioma.servicio.interfaz.capitacion;

import com.servinte.axioma.orm.InconsistenSubirPaciente;

public interface IInconsistenSubirPacienteServicio {

	
	/**
	 * 
	 * @param inconsistenSubirPaciente
	 * @return
	 * @author Camilo Gómez
	 */
	public boolean guardarInconsistenciaSubirPaciente(InconsistenSubirPaciente inconsistenSubirPaciente);
}
