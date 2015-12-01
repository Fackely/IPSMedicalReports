package com.sies.hibernate.delegate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Expression;

import com.servinte.axioma.orm.OcupacionSalarioBase;
import com.servinte.axioma.orm.OcupacionSalarioBaseHome;

public class OcupacionSalarioBaseDelegate extends OcupacionSalarioBaseHome {
	@SuppressWarnings("unchecked")
	public List<OcupacionSalarioBase> listarOcupacionSalarioBase() {
		return sessionFactory.getCurrentSession().createCriteria(com.servinte.axioma.orm.OcupacionSalarioBase.class).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<OcupacionSalarioBase> listarOcupacionDefinida(int codigoOcupacion)
	{
		List<OcupacionSalarioBase> retorno=null;
		try
		{
			retorno = sessionFactory.getCurrentSession().createCriteria(OcupacionSalarioBase.class).add(Expression.eq("id.ocupacion", codigoOcupacion)).list();
		}
		catch (HibernateException e){
			e.printStackTrace();
		}
		return retorno;
	}
}
