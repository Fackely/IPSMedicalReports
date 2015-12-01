package com.servinte.axioma.servicio.interfaz.capitacion;

import com.servinte.axioma.orm.InconsistenciaPersona;

public interface IInconsistenciaPersonaServicio {

	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de InconsistenciaPersona
	 * 
	 * @param InconsistenciaPersona 
	 * @return boolean
	 * @author Camilo G�mez
	 *
	 */
	public boolean guardarInconsistenciaPersona(InconsistenciaPersona inconsistenciaPersona);
	
}
