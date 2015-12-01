package com.servinte.axioma.orm.delegate.inventario;

import java.util.ArrayList;

import org.hibernate.Criteria;

import com.servinte.axioma.orm.NaturalezaArticulo;
import com.servinte.axioma.orm.NaturalezaArticuloHome;

@SuppressWarnings("unchecked")
public class NaturalezaArticuloDelegate extends NaturalezaArticuloHome{
	
	/**
	 * 	
	 * Este M�todo se encarga de consultar las naturalezas de art�culos
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
