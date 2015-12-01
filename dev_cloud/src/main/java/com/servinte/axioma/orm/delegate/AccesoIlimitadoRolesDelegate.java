package com.servinte.axioma.orm.delegate;

import java.util.List;

import com.servinte.axioma.orm.AccesoIlimitadoRoles;
import com.servinte.axioma.orm.AccesoIlimitadoRolesHome;

public class AccesoIlimitadoRolesDelegate extends AccesoIlimitadoRolesHome{
	
	
	
	@SuppressWarnings("unchecked")
	public List<AccesoIlimitadoRoles> listar()
	{
		return (List<AccesoIlimitadoRoles>)sessionFactory.getCurrentSession().createCriteria(AccesoIlimitadoRoles.class).list();
	}
}
