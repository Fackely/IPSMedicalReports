package com.servinte.axioma.dao.impl.inventario;

import com.servinte.axioma.dao.interfaz.inventario.IDespachoDAO;
import com.servinte.axioma.orm.Despacho;
import com.servinte.axioma.orm.delegate.inventario.DespachoDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link IDespachoDAO}.
 * @author Cristhian Murillo
 */
public class DespachoHibernateDAO implements IDespachoDAO 
{
	
	private DespachoDelegate despachoDelegate;

	
	/**
	 * Constructor
	 */
	public DespachoHibernateDAO(){
		despachoDelegate = new DespachoDelegate();
	}


	@Override
	public Despacho obtenerDespachoPorId(int id) {
		return despachoDelegate.obtenerDespachoPorId(id);
	}


	@Override
	public void attachDirty(Despacho instance) {
		despachoDelegate.attachDirty(instance);
	}
	
	


	

}
