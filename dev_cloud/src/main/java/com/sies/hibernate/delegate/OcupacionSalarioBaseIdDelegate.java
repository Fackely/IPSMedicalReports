package com.sies.hibernate.delegate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Expression;

import com.servinte.axioma.orm.OcupacionSalarioBaseHome;
import com.servinte.axioma.orm.OcupacionSalarioBaseId;

public class OcupacionSalarioBaseIdDelegate extends OcupacionSalarioBaseHome {
	@SuppressWarnings("unchecked")
	public List<OcupacionSalarioBaseId> listarOcupacionSalarioBase() {
		return sessionFactory.getCurrentSession().createCriteria(OcupacionSalarioBaseId.class).list();
	}
	
	@SuppressWarnings("unchecked")
	public List<OcupacionSalarioBaseId> listarOcupacionDefinida(int codigoOcupacion)
	{
		List<OcupacionSalarioBaseId> retorno=null;
		try
		{
			retorno = sessionFactory.getCurrentSession().createCriteria(OcupacionSalarioBaseId.class).add(Expression.eq("ocupacion", codigoOcupacion)).list();
		}
		catch (HibernateException e){
			e.printStackTrace();
		}
		return retorno;
	}
}