/**
 * 
 */
package com.servinte.axioma.orm.delegate.ordenes;

import com.servinte.axioma.orm.SolicitudesPosponer;
import com.servinte.axioma.orm.SolicitudesPosponerHome;

/**
 * @author Cristhian Murillo
 */
public class SolicitudesPosponerDelegate extends SolicitudesPosponerHome 
{
	

	/**
	 * Guarta la instancia de la entidad en la base de datos
	 * @param transientInstance
	 */
	public void guardarSolicitudesPosponer(SolicitudesPosponer transientInstance) {
		super.persist(transientInstance);
	}
	
}
