/**
 * 
 */
package com.servinte.axioma.orm.delegate;

import java.util.List;

import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.DependenciasFunc;
import com.servinte.axioma.orm.DependenciasFuncHome;
import com.servinte.axioma.orm.Roles;

/**
 * @author armando
 *
 */
public class DependenciasFuncDelegate extends DependenciasFuncHome
{
	@SuppressWarnings("unchecked")
	public DependenciasFunc consultarFuncionalidadHija(int codigoFuncPadre)
	{
		return (DependenciasFunc)sessionFactory.getCurrentSession().createCriteria(DependenciasFunc.class).add(Restrictions.eq("id.funcionalidadPadre", codigoFuncPadre)).uniqueResult();
	}
}
