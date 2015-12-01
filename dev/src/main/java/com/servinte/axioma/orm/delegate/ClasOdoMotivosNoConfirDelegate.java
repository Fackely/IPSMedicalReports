package com.servinte.axioma.orm.delegate;

import java.util.List;

import org.hibernate.criterion.Expression;

import com.servinte.axioma.orm.ClasOdoMotivosNoConfir;
import com.servinte.axioma.orm.ClasOdoMotivosNoConfirHome;
import com.servinte.axioma.orm.ClasificaPacientesOdo;

public class ClasOdoMotivosNoConfirDelegate extends ClasOdoMotivosNoConfirHome{

	@SuppressWarnings("unchecked")
	public List<ClasOdoMotivosNoConfir> listarTodos(ClasificaPacientesOdo clasificaPacientesOdo)
	{
		return (List<ClasOdoMotivosNoConfir>)sessionFactory.
				getCurrentSession().
				createCriteria(ClasOdoMotivosNoConfir.class).
				createCriteria("ClasOdoSecIndConf").
				add(Expression.eq("clasificaPacientesOdo", clasificaPacientesOdo)).
				list();
	}
}
