/**
 * 
 */
package com.servinte.axioma.orm.delegate.inventario;

import com.servinte.axioma.orm.DetallePedidos;
import com.servinte.axioma.orm.DetallePedidosHome;
import com.servinte.axioma.orm.DetallePedidosId;

/**
 * @author Cristhian Murillo
 *
 */
public class DetallePedidosDelegate extends DetallePedidosHome 
{
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return  DetallePedidos
	 */
	public  DetallePedidos obtenerDetalleDespachosPorId(DetallePedidosId id) {
		return super.findById(id);
	}

	
	@Override 
	public void attachDirty( DetallePedidos instance) {
		super.attachDirty(instance);
	}
	
	
	@Override
	public void persist( DetallePedidos transientInstance) {
		super.persist(transientInstance);
	}
}
