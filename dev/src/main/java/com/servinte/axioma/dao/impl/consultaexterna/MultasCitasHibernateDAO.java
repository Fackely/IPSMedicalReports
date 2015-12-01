/*
 * Mayo 18, 2010
 */
package com.servinte.axioma.dao.impl.consultaexterna;

import com.servinte.axioma.dao.interfaz.consultaexterna.IMultasCitasDAO;
import com.servinte.axioma.orm.MultasCitas;
import com.servinte.axioma.orm.delegate.consultaexterna.MultasCitasDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IMultasCitasDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see MultasCitasDelegate
 */

public class MultasCitasHibernateDAO implements IMultasCitasDAO {
	
	
	private MultasCitasDelegate multasCitasDelegate;

	public MultasCitasHibernateDAO() {
		
		multasCitasDelegate = new MultasCitasDelegate();
	}
	

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.consultaexterna.IMultasCitasMundo#obtenerMultaCitaPorCodigo(long)
	 */
	@Override
	public MultasCitas obtenerMultaCitaPorCodigo(long codigoPk) {
		
		return multasCitasDelegate.obtenerMultaCitaPorCodigo(codigoPk);
	}
}
