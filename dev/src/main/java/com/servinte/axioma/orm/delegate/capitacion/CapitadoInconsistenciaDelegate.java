package com.servinte.axioma.orm.delegate.capitacion;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.CapitadoInconsistencia;
import com.servinte.axioma.orm.CapitadoInconsistenciaHome;
import com.servinte.axioma.orm.Servicios;

public class CapitadoInconsistenciaDelegate extends CapitadoInconsistenciaHome{

	
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de CapitadoInconsistencia
	 * 
	 * @param CapitadoInconsistencia 
	 * @return boolean
	 * @author Camilo Gómez
	 *
	 */
	public boolean guardarCapitadoInconsistencia(CapitadoInconsistencia capitadoInconsistencia){
		boolean save = true;					
		try{
			super.attachDirty(capitadoInconsistencia);
		} catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el registro de " +
					"capitado Inconsistencia: ",e);
		}				
		return save;				
	}
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return CapitadoInconsistencia
	 */
	public CapitadoInconsistencia obtenerCapitadoInconsistenciaPorId(Long id) {
		return super.findById(id);
	}
}
