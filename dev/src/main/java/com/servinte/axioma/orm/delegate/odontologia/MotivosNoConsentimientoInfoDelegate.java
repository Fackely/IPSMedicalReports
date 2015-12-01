/**
 * 
 */
package com.servinte.axioma.orm.delegate.odontologia;

import java.util.ArrayList;

import org.hibernate.Criteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;

import util.ConstantesBD;

import com.servinte.axioma.orm.MotivosNoConsentimientoInfo;
import com.servinte.axioma.orm.MotivosNoConsentimientoInfoHome;

/**
 * @author armando
 *
 */
public class MotivosNoConsentimientoInfoDelegate extends MotivosNoConsentimientoInfoHome
{

	public ArrayList<MotivosNoConsentimientoInfo> consultarTodos(boolean todos) 
	{
		Criteria criteria=sessionFactory.getCurrentSession().createCriteria(MotivosNoConsentimientoInfo.class);
		if(!todos)
			criteria.add(Expression.eq("activo", ConstantesBD.acronimoSi));
		criteria.addOrder(Order.asc("descripcion"));
		return (ArrayList<MotivosNoConsentimientoInfo>)criteria.list();
	}

}
