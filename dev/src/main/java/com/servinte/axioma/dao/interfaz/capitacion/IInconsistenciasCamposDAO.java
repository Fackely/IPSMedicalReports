package com.servinte.axioma.dao.interfaz.capitacion;

import com.servinte.axioma.orm.InconsistenciasCampos;

public interface IInconsistenciasCamposDAO {

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
