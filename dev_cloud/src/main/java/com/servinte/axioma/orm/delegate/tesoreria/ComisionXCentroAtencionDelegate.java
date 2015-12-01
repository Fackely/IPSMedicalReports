/*
 * Noviembre 19, 2010
 */
package com.servinte.axioma.orm.delegate.tesoreria;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.ComisionXCentroAtencion;
import com.servinte.axioma.orm.ComisionXCentroAtencionHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
@SuppressWarnings("unchecked")
public class ComisionXCentroAtencionDelegate extends ComisionXCentroAtencionHome{
	
	

	/**
	 * Lista los registros parametrizados por TarjetaFinancieraComision
	 * @param tarjetaFinancieraComision
	 * @return  ArrayList<ComisionXCentroAtencion>
	 */
	public ArrayList<ComisionXCentroAtencion> listarPorTarjetaFinancieraComision(long tarjetaFinancieraComision)
	{
		Criteria criteria = sessionFactory.getCurrentSession().createCriteria(ComisionXCentroAtencion.class, "comisionXCentroAtencion");
		
		criteria.createAlias("comisionXCentroAtencion.tarjetaFinancieraComision"	, "tarjetaFinancieraComision");
		criteria.createAlias("comisionXCentroAtencion.centroAtencion"				, "centroAtencion");
		
		criteria.add(Restrictions.eq("tarjetaFinancieraComision.codigoPk", tarjetaFinancieraComision));

		criteria.addOrder( Order.asc("centroAtencion.descripcion") );
		
		ArrayList<ComisionXCentroAtencion> listaConsulta = (ArrayList<ComisionXCentroAtencion>)criteria.list();
		
		
		for (ComisionXCentroAtencion comisionXCentroAtencion : listaConsulta) {
			comisionXCentroAtencion.getCentroAtencion().getDescripcion();
		}
		
		return listaConsulta;
	}
	
	
}
