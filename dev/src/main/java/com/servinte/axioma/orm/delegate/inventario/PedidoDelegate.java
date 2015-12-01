/**
 * 
 */
package com.servinte.axioma.orm.delegate.inventario;

import com.servinte.axioma.orm.Pedido;
import com.servinte.axioma.orm.PedidoHome;

/**
 * @author Cristhian Murillo
 *
 */
public class PedidoDelegate extends PedidoHome 
{
	
	/**
	 * Implementacion del findById
	 * @param id
	 * @return Pedido
	 */
	public Pedido obtenerPedidoPorId(int id) {
		return super.findById(id);
	}

	
	@Override 
	public void attachDirty(Pedido instance) {
		super.attachDirty(instance);
	}
}
