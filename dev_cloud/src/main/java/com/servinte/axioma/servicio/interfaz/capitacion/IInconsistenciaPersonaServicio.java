package com.servinte.axioma.servicio.interfaz.capitacion;

import com.servinte.axioma.orm.InconsistenciaPersona;

public interface IInconsistenciaPersonaServicio {

	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de InconsistenciaPersona
	 * 
	 * @param InconsistenciaPersona 
	 * @return boolean
	 * @author Camilo Gómez
	 *
	 */
	public boolean guardarInconsistenciaPersona(InconsistenciaPersona inconsistenciaPersona);
	
}
