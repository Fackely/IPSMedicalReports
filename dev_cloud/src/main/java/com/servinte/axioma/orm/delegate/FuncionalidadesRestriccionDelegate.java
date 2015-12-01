package com.servinte.axioma.orm.delegate;

import java.util.List;

import com.servinte.axioma.orm.FuncionalidadesRestriccion;
import com.servinte.axioma.orm.FuncionalidadesRestriccionHome;

public class FuncionalidadesRestriccionDelegate extends FuncionalidadesRestriccionHome {
	
	public List <FuncionalidadesRestriccion> listarTodas()
	{
		
		
		return sessionFactory.getCurrentSession().createCriteria(FuncionalidadesRestriccion.class).list(); 
		
		
	}

}
