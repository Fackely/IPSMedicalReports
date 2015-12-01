package com.servinte.axioma.dao.impl.historiaClinica;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.historiaClinica.ICausasExternasDAO;
import com.servinte.axioma.orm.CausasExternas;
import com.servinte.axioma.orm.delegate.historiaClinica.CausasExternasDelegate;

public class CausasExternasHibernateDAO implements ICausasExternasDAO{
	
	
	CausasExternasDelegate delegate;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabián Becerra
	 */
	public CausasExternasHibernateDAO(){
		delegate = new CausasExternasDelegate();		
	}
	
	
	/**
	 * Este Método se encarga de consultar las causas externas
	 * registradas en el sistema
	 * 
	 * @return ArrayList<CausasExternas>
	 * 
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<CausasExternas> consultarCausasExternas(){
		return delegate.consultarCausasExternas();
	}

}
