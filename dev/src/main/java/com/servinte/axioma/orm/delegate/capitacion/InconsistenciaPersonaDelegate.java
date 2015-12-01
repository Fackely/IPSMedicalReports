package com.servinte.axioma.orm.delegate.capitacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.InconsistenciaPersona;
import com.servinte.axioma.orm.InconsistenciaPersonaHome;

public class InconsistenciaPersonaDelegate extends InconsistenciaPersonaHome{

	
	
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
	public boolean guardarInconsistenciaPersona(InconsistenciaPersona inconsistenciaPersona){
		boolean save = true;					
		try{
			super.attachDirty(inconsistenciaPersona);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro de " +
					"Inconsistencias persona: ",e);
		}				
		return save;				
	}
}
