package com.servinte.axioma.servicio.interfaz.capitacion;

import com.servinte.axioma.orm.CapitadoInconsistencia;

public interface ICapitadoInconsistenciaServicio {

	/**
	 * 
	 * Este M?todo se encarga de insertar en la base de datos
	 * un registro de CapitadoInconsistencia
	 * 
	 * @param CapitadoInconsistencia 
	 * @return boolean
	 * @author Camilo G?mez
	 *
	 */
	public boolean guardarCapitadoInconsistencia(CapitadoInconsistencia capitadoInconsistencia);
	
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return CapitadoInconsistencia
	 */
	public CapitadoInconsistencia obtenerCapitadoInconsistenciaPorId(Long id) ;
}
