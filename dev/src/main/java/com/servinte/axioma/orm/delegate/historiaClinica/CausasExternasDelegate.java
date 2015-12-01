package com.servinte.axioma.orm.delegate.historiaClinica;

import java.util.ArrayList;

import org.hibernate.criterion.Order;

import com.servinte.axioma.orm.CausasExternas;
import com.servinte.axioma.orm.CausasExternasHome;

public class CausasExternasDelegate extends CausasExternasHome{
	
	/**
	 * Este Método se encarga de consultar las causas externas
	 * registradas en el sistema
	 * 
	 * @return ArrayList<CausasExternas>
	 * 
	 * @author, Fabián Becerra
	 *
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<CausasExternas> consultarCausasExternas(){
		
		ArrayList<CausasExternas> lista =  (ArrayList<CausasExternas>)sessionFactory.getCurrentSession()
		.createCriteria(CausasExternas.class, "causaExterna")
		.addOrder(Order.asc("causaExterna.nombre"))
		.list();
	
		return lista;	
		
	}

}
