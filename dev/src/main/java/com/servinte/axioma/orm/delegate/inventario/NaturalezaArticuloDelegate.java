package com.servinte.axioma.orm.delegate.inventario;

import java.util.ArrayList;

import org.hibernate.Criteria;

import com.servinte.axioma.orm.NaturalezaArticulo;
import com.servinte.axioma.orm.NaturalezaArticuloHome;

@SuppressWarnings("unchecked")
public class NaturalezaArticuloDelegate extends NaturalezaArticuloHome{
	
	/**
	 * 	
	 * Este Método se encarga de consultar las naturalezas de artículos
	 * en el sistema
	 * 
	 * @return ArrayList<NaturalezaArticulo>
	 * @author, Angela Maria Aguirre
	 *
	 */
	
	public ArrayList<NaturalezaArticulo> buscarNaturalezaArticulo(){
		 Criteria criteria = sessionFactory.getCurrentSession()
			.createCriteria(NaturalezaArticulo.class);
		 
		 return (ArrayList<NaturalezaArticulo>)criteria.list();
	} 

}
