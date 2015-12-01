package com.servinte.axioma.dao.interfaz.capitacion;

import com.servinte.axioma.orm.InconsistenciaPersona;

public interface IInconsistenciaPersonaDAO {

	
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
