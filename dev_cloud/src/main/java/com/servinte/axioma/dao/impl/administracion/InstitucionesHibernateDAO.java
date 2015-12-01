package com.servinte.axioma.dao.impl.administracion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.administracion.IInstitucionesDAO;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.delegate.administracion.InstitucionDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IInstitucionesDAO}.
 * 
 * @author Cristhian Murillo
 * @see InstitucionDelegate.
 */

public class InstitucionesHibernateDAO implements IInstitucionesDAO{

	
	private InstitucionDelegate delegate = new InstitucionDelegate();

	@Override
	public Instituciones findById(int id) {
		return delegate.findById(id);
	}

	@Override
	public ArrayList<Instituciones> listarInstituciones() {
		return delegate.listarInstituciones();
	}
	
	
}
