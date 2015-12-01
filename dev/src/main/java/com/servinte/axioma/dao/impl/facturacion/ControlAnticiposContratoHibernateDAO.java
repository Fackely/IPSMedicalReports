
package com.servinte.axioma.dao.impl.facturacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.facturacion.IControlAnticiposContratoDAO;
import com.servinte.axioma.orm.ControlAnticiposContrato;
import com.servinte.axioma.orm.delegate.facturacion.ControlAnticiposContratoDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link IControlAnticiposContratoDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see ControlAnticiposContratoDelegate
 */


public class ControlAnticiposContratoHibernateDAO implements IControlAnticiposContratoDAO {

	
	private ControlAnticiposContratoDelegate controlAnticiposContratoDelegate;
	
	public ControlAnticiposContratoHibernateDAO() {
		
		controlAnticiposContratoDelegate = new ControlAnticiposContratoDelegate();
	}

	

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.facturacion.IControlAnticiposContratoMundo#determinarContratoRequiereAnticipo(int)
	 */
	@Override
	public ArrayList<ControlAnticiposContrato> determinarContratoRequiereAnticipo(int contrato) {
		
		return controlAnticiposContratoDelegate.determinarContratoRequiereAnticipo(contrato);
	}

}
