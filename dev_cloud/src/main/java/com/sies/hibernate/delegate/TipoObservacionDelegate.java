package com.sies.hibernate.delegate;

import java.util.List;

import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import com.servinte.axioma.orm.TipoObservacion;
import com.servinte.axioma.orm.TipoObservacionHome;

/**
 * 
 * @author axioma
 *
 */
public class TipoObservacionDelegate extends TipoObservacionHome {
	@SuppressWarnings("unchecked")
	public List<TipoObservacion> listar()
	{
		return sessionFactory.getCurrentSession().createCriteria(TipoObservacion.class).add(Expression.eq("activo", true)).addOrder(Order.asc("descripcion")).list();
	}
}
