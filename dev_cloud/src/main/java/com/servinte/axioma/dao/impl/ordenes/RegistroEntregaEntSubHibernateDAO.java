package com.servinte.axioma.dao.impl.ordenes;

import com.servinte.axioma.dao.interfaz.ordenes.IRegistroEntregaEntSubDAO;
import com.servinte.axioma.orm.RegistroEntregaEntSub;
import com.servinte.axioma.orm.delegate.ordenes.RegistroEntregaEntSubDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link IRegistroEntregaEntSubDAO}.
 * @author Cristhian Murillo
 */
public class RegistroEntregaEntSubHibernateDAO implements IRegistroEntregaEntSubDAO 
{
	
	private RegistroEntregaEntSubDelegate registroEntregaEntSubDelegate;
	
	
	/**
	 * Constructor
	 */
	public RegistroEntregaEntSubHibernateDAO(){
		registroEntregaEntSubDelegate = new RegistroEntregaEntSubDelegate();
	}


	@Override
	public void attachDirty(RegistroEntregaEntSub instance) {
		registroEntregaEntSubDelegate.attachDirty(instance);
	}


}
