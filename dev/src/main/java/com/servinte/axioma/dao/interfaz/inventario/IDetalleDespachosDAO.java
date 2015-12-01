package com.servinte.axioma.dao.interfaz.inventario;

import com.servinte.axioma.orm.DetalleDespachos;
import com.servinte.axioma.orm.DetalleDespachosId;


/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de IDetalleDespachosDAO
 * 
 * @author Cristhian Murillo
 */
public interface IDetalleDespachosDAO 
{
	
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return DetalleDespachos
	 */
	public DetalleDespachos obtenerDetalleDespachosPorId(DetalleDespachosId id);

	
	
	/**
	 * Implementacion del attachDirty
	 * @param instance
	 */
	public void attachDirty(DetalleDespachos instance);
	
	
	
	/**
	 * Guarda la isntancia en la base de datos
	 * @param transientInstance
	 */
	public void persist(DetalleDespachos transientInstance);
		
	
}
