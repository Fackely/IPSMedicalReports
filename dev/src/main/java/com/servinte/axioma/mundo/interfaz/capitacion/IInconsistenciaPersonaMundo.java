package com.servinte.axioma.mundo.interfaz.capitacion;

import com.servinte.axioma.orm.InconsistenciaPersona;

public interface IInconsistenciaPersonaMundo {

	
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
