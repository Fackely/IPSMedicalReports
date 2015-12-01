package com.servinte.axioma.dao.impl.ordenes;

import com.servinte.axioma.dao.interfaz.ordenes.IAutorizacionArticuloDespachoDAO;
import com.servinte.axioma.orm.AutorizacionArticuloDespacho;
import com.servinte.axioma.orm.delegate.ordenes.AutorizacionArticuloDespachoDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link IAutorizacionArticuloDespachoDAO}.
 * @author Cristhian Murillo
 */
public class AutorizacionArticuloDespachoHibernateDAO implements IAutorizacionArticuloDespachoDAO 
{
	
	private AutorizacionArticuloDespachoDelegate autorizacionArticuloDespachoDelegate;
	

	
	/**
	 * Constructor
	 */
	public AutorizacionArticuloDespachoHibernateDAO(){
		autorizacionArticuloDespachoDelegate = new AutorizacionArticuloDespachoDelegate();
	}



	@Override
	public void attachDirty(AutorizacionArticuloDespacho instance) {
		autorizacionArticuloDespachoDelegate.attachDirty(instance);
	}



}
