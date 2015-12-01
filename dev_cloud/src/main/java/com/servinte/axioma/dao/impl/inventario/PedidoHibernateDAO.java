package com.servinte.axioma.dao.impl.inventario;

import com.servinte.axioma.dao.interfaz.inventario.IDespachoDAO;
import com.servinte.axioma.dao.interfaz.inventario.IPedidoDAO;
import com.servinte.axioma.orm.Pedido;
import com.servinte.axioma.orm.delegate.inventario.PedidoDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link IDespachoDAO}.
 * @author Cristhian Murillo
 */
public class PedidoHibernateDAO implements IPedidoDAO 
{
	
	private PedidoDelegate pedidoDelegate;

	
	/**
	 * Constructor
	 */
	public PedidoHibernateDAO(){
		pedidoDelegate = new PedidoDelegate();
	}


	@Override
	public void attachDirty(Pedido instance) {
		pedidoDelegate.attachDirty(instance);
	}


	@Override
	public Pedido obtenerPedidoPorId(int id) {
		return pedidoDelegate.obtenerPedidoPorId(id);
	}


}
