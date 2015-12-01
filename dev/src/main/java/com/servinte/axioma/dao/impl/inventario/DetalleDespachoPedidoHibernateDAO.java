package com.servinte.axioma.dao.impl.inventario;

import com.servinte.axioma.dao.interfaz.inventario.IDetalleDespachoPedidoDAO;
import com.servinte.axioma.orm.DetalleDespachoPedido;
import com.servinte.axioma.orm.DetalleDespachoPedidoId;
import com.servinte.axioma.orm.delegate.inventario.DetalleDespachoPedidoDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link IDetalleDespachoPedidoDAO}.
 * @author Cristhian Murillo
 */
public class DetalleDespachoPedidoHibernateDAO implements IDetalleDespachoPedidoDAO 
{
	
	private DetalleDespachoPedidoDelegate detalleDespachoPedidoDelegate;
	
	/**
	 * Constructor
	 */
	public DetalleDespachoPedidoHibernateDAO(){
		detalleDespachoPedidoDelegate = new DetalleDespachoPedidoDelegate();
	}

	
	@Override
	public void attachDirty(DetalleDespachoPedido instance) {
		detalleDespachoPedidoDelegate.attachDirty(instance);
	}

	@Override
	public DetalleDespachoPedido obtenerDetalleDespachosPorId(DetalleDespachoPedidoId id) {
		return detalleDespachoPedidoDelegate.obtenerDetalleDespachosPorId(id);
	}

	@Override
	public void persist(DetalleDespachoPedido transientInstance) {
		detalleDespachoPedidoDelegate.persist(transientInstance);
	}




}
