/**
 * 
 */
package com.servinte.axioma.orm.delegate.inventario;

import com.servinte.axioma.orm.DetalleDespachoPedido;
import com.servinte.axioma.orm.DetalleDespachoPedidoHome;
import com.servinte.axioma.orm.DetalleDespachoPedidoId;

/**
 * @author Cristhian Murillo
 *
 */
public class DetalleDespachoPedidoDelegate extends DetalleDespachoPedidoHome 
{
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return  DetalleDespachoPedido
	 */
	public  DetalleDespachoPedido obtenerDetalleDespachosPorId(DetalleDespachoPedidoId id) {
		return super.findById(id);
	}

	
	@Override 
	public void attachDirty( DetalleDespachoPedido instance) {
		super.attachDirty(instance);
	}
	
	
	@Override
	public void persist( DetalleDespachoPedido transientInstance) {
		super.persist(transientInstance);
	}
}
