package com.servinte.axioma.mundo.interfaz.capitacion;

import com.servinte.axioma.orm.InconsistenciasCampos;

public interface IInconsistenciasCamposMundo {

	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de InconsistenciasCampos
	 * 
	 * @param InconsistenciasCampos 
	 * @return boolean
	 * @author Camilo G�mez
	 *
	 */
	public boolean guardarInconsistenciasCampos(InconsistenciasCampos inconsistenciasCampos);
	
}
