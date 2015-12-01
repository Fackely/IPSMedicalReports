package com.servinte.axioma.dao.impl.inventario;

import com.servinte.axioma.dao.interfaz.inventario.IDetalleDespachosDAO;
import com.servinte.axioma.orm.DetalleDespachos;
import com.servinte.axioma.orm.DetalleDespachosId;
import com.servinte.axioma.orm.delegate.inventario.DetalleDespachosDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link IDetalleDespachosDAO}.
 * @author Cristhian Murillo
 */
public class DetalleDespachosHibernateDAO implements IDetalleDespachosDAO 
{
	
	private DetalleDespachosDelegate detalleDespachosDelegate;

	
	/**
	 * Constructor
	 */
	public DetalleDespachosHibernateDAO(){
		detalleDespachosDelegate = new DetalleDespachosDelegate();
	}


	@Override
	public void attachDirty(DetalleDespachos instance) {
		detalleDespachosDelegate.attachDirty(instance);
	}


	@Override
	public DetalleDespachos obtenerDetalleDespachosPorId(DetalleDespachosId id) {
		return detalleDespachosDelegate.obtenerDetalleDespachosPorId(id);
	}


	@Override
	public void persist(DetalleDespachos transientInstance) {
		detalleDespachosDelegate.persist(transientInstance);
	}

	

}
