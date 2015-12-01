package com.servinte.axioma.dao.impl.ordenes;

import com.servinte.axioma.dao.interfaz.ordenes.IRegistroEntregaEntSubDAO;
import com.servinte.axioma.dao.interfaz.ordenes.ISolDespachoSinAutoDAO;
import com.servinte.axioma.orm.SolDespachoSinAuto;
import com.servinte.axioma.orm.delegate.ordenes.SolDespachoSinAutoDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link IRegistroEntregaEntSubDAO}.
 * @author Cristhian Murillo
 */
public class SolDespachoSinAutoHibernateDAO implements ISolDespachoSinAutoDAO 
{
	
	private SolDespachoSinAutoDelegate solDespachoSinAutoDelegate;
	
	/**
	 * Constructor
	 */
	public SolDespachoSinAutoHibernateDAO(){
		solDespachoSinAutoDelegate = new SolDespachoSinAutoDelegate();
	}

	
	
	@Override
	public void attachDirtySolDespachoSinAuto(SolDespachoSinAuto instance) {
		solDespachoSinAutoDelegate.attachDirtySolDespachoSinAuto(instance);
	}





}
