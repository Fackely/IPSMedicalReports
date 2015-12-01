package com.servinte.axioma.dao.impl.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.historiaClinica.ICausasExternasDAO;
import com.servinte.axioma.orm.CausasExternas;
import com.servinte.axioma.orm.delegate.historiaClinica.CausasExternasDelegate;

public class CausasExternasHibernateDAO implements ICausasExternasDAO{
	
	
	CausasExternasDelegate delegate;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Fabi�n Becerra
	 */
	public CausasExternasHibernateDAO(){
		delegate = new CausasExternasDelegate();		
	}
	
	
	/**
	 * Este M�todo se encarga de consultar las causas externas
	 * registradas en el sistema
	 * 
	 * @return ArrayList<CausasExternas>
	 * 
	 * @author, Fabi�n Becerra
	 *
	 */
	public ArrayList<CausasExternas> consultarCausasExternas(){
		return delegate.consultarCausasExternas();
	}

}
