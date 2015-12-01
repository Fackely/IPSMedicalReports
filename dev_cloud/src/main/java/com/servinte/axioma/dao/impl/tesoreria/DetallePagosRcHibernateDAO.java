package com.servinte.axioma.dao.impl.tesoreria;

import com.servinte.axioma.dao.interfaz.tesoreria.IDetallePagosRcDAO;
import com.servinte.axioma.orm.DetallePagosRc;
import com.servinte.axioma.orm.delegate.tesoreria.DetallePagosRcDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IDetallePagosRcDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see DetallePagosRcDelegate.
 */


public class DetallePagosRcHibernateDAO implements IDetallePagosRcDAO{

	private DetallePagosRcDelegate detallePagosRcDelegate;

	public DetallePagosRcHibernateDAO() {
		detallePagosRcDelegate  = new DetallePagosRcDelegate();
	}

	
	@Override
	public DetallePagosRc findById(int codigoDetalle) {
		
		return detallePagosRcDelegate.findById(codigoDetalle);
	}

	@Override
	public void attachDirty(DetallePagosRc detallePagos) {
		detallePagosRcDelegate.attachDirty(detallePagos);
	}


}
