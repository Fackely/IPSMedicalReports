package com.servinte.axioma.dao.impl.inventario;

import com.servinte.axioma.dao.interfaz.inventario.IDespachoPedidoDAO;
import com.servinte.axioma.dao.interfaz.inventario.IDetalleDespachosDAO;
import com.servinte.axioma.orm.DespachoPedido;
import com.servinte.axioma.orm.delegate.inventario.DespachoPedidoDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link IDetalleDespachosDAO}.
 * @author Cristhian Murillo
 */
public class DespachoPedidoHibernateDAO implements IDespachoPedidoDAO 
{
	
	private DespachoPedidoDelegate despachoPedidoDelegate;
	
	
	/**
	 * Constructor
	 */
	public DespachoPedidoHibernateDAO(){
		despachoPedidoDelegate = new DespachoPedidoDelegate();
	}


	@Override
	public void attachDirty(DespachoPedido instance) {
		despachoPedidoDelegate.attachDirty(instance);
	}


	@Override
	public void persist(DespachoPedido transientInstance) {
		despachoPedidoDelegate.persist(transientInstance);
	}


}
