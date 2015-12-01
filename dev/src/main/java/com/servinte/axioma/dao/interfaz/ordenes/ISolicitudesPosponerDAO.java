package com.servinte.axioma.dao.interfaz.ordenes;

import com.servinte.axioma.orm.SolicitudesPosponer;


/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de ISolicitudesPosponerDAO 
 * 
 * @author Cristhian Murillo 
 */
public interface ISolicitudesPosponerDAO 
{

	/**
	 * Guarta la instancia de la entidad en la base de datos
	 * @param transientInstance
	 */
	public void guardarSolicitudesPosponer(SolicitudesPosponer transientInstance);
	
	
}
