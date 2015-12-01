package com.servinte.axioma.dao.impl.inventario;

import com.servinte.axioma.dao.interfaz.inventario.IDetalleDespachosDAO;
import com.servinte.axioma.dao.interfaz.inventario.IDetallePedidosDAO;
import com.servinte.axioma.orm.DetallePedidos;
import com.servinte.axioma.orm.DetallePedidosId;
import com.servinte.axioma.orm.delegate.inventario.DetallePedidosDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link IDetalleDespachosDAO}.
 * @author Cristhian Murillo
 */
public class DetallePedidosHibernateDAO implements IDetallePedidosDAO 
{
	
	private DetallePedidosDelegate detallePedidosDelegate;
	
	/**
	 * Constructor
	 */
	public DetallePedidosHibernateDAO(){
		detallePedidosDelegate = new DetallePedidosDelegate();
	}

	@Override
	public void attachDirty(DetallePedidos instance) {
		detallePedidosDelegate.attachDirty(instance);
	}

	@Override
	public DetallePedidos obtenerDetalleDespachosPorId(DetallePedidosId id) {
		return detallePedidosDelegate.obtenerDetalleDespachosPorId(id);
	}

	@Override
	public void persist(DetallePedidos transientInstance) {
		detallePedidosDelegate.persist(transientInstance);
	}



}
