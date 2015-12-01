package com.servinte.axioma.orm.delegate.administracion;

import java.util.ArrayList;

import org.hibernate.criterion.Order;

import com.servinte.axioma.orm.RegionesCobertura;
import com.servinte.axioma.orm.RegionesCoberturaHome;

/**
 * 
 * @author Cristhian Murillo
 */
public class RegionesCoberturaDelegate extends RegionesCoberturaHome {
	

	/**
	 * Lista todas las Regiones de Cobertura
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<RegionesCobertura> listarRegionesCoberturaActivas(){
		return (ArrayList<RegionesCobertura>) sessionFactory.getCurrentSession()
			.createCriteria(RegionesCobertura.class)
			.addOrder(Order.asc("descripcion"))
			// FIXME filtrar las que sean activas ?
			.list();
	}
	
	
}
