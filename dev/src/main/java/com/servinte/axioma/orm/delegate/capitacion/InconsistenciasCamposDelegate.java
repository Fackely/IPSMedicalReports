package com.servinte.axioma.orm.delegate.capitacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.InconsistenciasCampos;
import com.servinte.axioma.orm.InconsistenciasCamposHome;

public class InconsistenciasCamposDelegate extends InconsistenciasCamposHome{

	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de InconsistenciasCampos
	 * 
	 * @param InconsistenciasCampos 
	 * @return boolean
	 * @author Camilo Gómez
	 *
	 */
	public boolean guardarInconsistenciasCampos(InconsistenciasCampos inconsistenciasCampos){
		boolean save = true;					
		try{
			super.attachDirty(inconsistenciasCampos);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro de " +
					"Inconsistencias campos: ",e);
		}				
		return save;				
	}
}
