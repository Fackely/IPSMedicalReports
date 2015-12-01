package com.servinte.axioma.orm.delegate.administracion;

import java.util.ArrayList;

import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.InstitucionesHome;

/**
 * 
 * @author axioma
 */
public class InstitucionDelegate extends InstitucionesHome {
	
	
	/**
	 * Lista todas las Instituciones
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<Instituciones> listarInstituciones (){
		return (ArrayList<Instituciones>) sessionFactory.getCurrentSession()
			.createCriteria(Instituciones.class)
			.list();
	}
	
	
	
	@Override
	public Instituciones findById(int id) {
		return super.findById(id);
	}

}
