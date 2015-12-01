package com.servinte.axioma.servicio.impl.capitacion;

import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICapitadoInconsistenciaMundo;
import com.servinte.axioma.orm.CapitadoInconsistencia;
import com.servinte.axioma.servicio.interfaz.capitacion.ICapitadoInconsistenciaServicio;

public class CapitadoInconsistenciaServicio implements ICapitadoInconsistenciaServicio {

	ICapitadoInconsistenciaMundo mundo;
	
	/**
	 * metodo Constructor de la clase
	 * @author Camilo G�mez
	 */
	public CapitadoInconsistenciaServicio(){
		mundo =CapitacionFabricaMundo.crearCapitadoInconsistenciaMundo();
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de CapitadoInconsistencia
	 * 
	 * @param CapitadoInconsistencia 
	 * @return boolean
	 * @author Camilo G�mez
	 *
	 */
	public boolean guardarCapitadoInconsistencia(CapitadoInconsistencia capitadoInconsistencia){
		return mundo.guardarCapitadoInconsistencia(capitadoInconsistencia);
	}
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return CapitadoInconsistencia
	 */
	public CapitadoInconsistencia obtenerCapitadoInconsistenciaPorId(Long id) {
		return mundo.obtenerCapitadoInconsistenciaPorId(id);
	}
	
}
