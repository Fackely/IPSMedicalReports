/**
 * 
 */
package com.servinte.axioma.orm.delegate.inventario;

import com.servinte.axioma.orm.DetalleDespachos;
import com.servinte.axioma.orm.DetalleDespachosHome;
import com.servinte.axioma.orm.DetalleDespachosId;

/**
 * @author Cristhian Murillo
 *
 */
public class DetalleDespachosDelegate extends DetalleDespachosHome 
{
	
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return DetalleDespachos
	 */
	public DetalleDespachos obtenerDetalleDespachosPorId(DetalleDespachosId id) {
		return super.findById(id);
	}

	
	@Override 
	public void attachDirty(DetalleDespachos instance) {
		super.attachDirty(instance);
	}
	
	
	@Override
	public void persist(DetalleDespachos transientInstance) {
		super.persist(transientInstance);
	}
}
