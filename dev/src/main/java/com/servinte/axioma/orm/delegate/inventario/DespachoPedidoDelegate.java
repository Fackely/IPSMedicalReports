/**
 * 
 */
package com.servinte.axioma.orm.delegate.inventario;

import com.servinte.axioma.orm.DespachoPedido;
import com.servinte.axioma.orm.DespachoPedidoHome;

/**
 * @author Cristhian Murillo
 *
 */
public class DespachoPedidoDelegate extends  DespachoPedidoHome 
{
	
	
	@Override 
	public void attachDirty( DespachoPedido instance) {
		super.attachDirty(instance);
	}
	
	
	@Override
	public void persist( DespachoPedido transientInstance) {
		super.persist(transientInstance);
	}
	
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return  DespachoPedido
	 */
	public DespachoPedido obtenerDetalleDespachosPorId(int id) {
		// FIXME verificar si este método funciona ya que el .java fue modificado. Cristhian Murillo
		return super.findById(id);
	}
	
}
