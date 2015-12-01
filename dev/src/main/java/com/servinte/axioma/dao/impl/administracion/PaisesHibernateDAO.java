package com.servinte.axioma.dao.impl.administracion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.administracion.IPaisesDAO;
import com.servinte.axioma.dto.administracion.DtoPaises;
import com.servinte.axioma.orm.Paises;
import com.servinte.axioma.orm.delegate.administracion.PaisesDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IPaisesDAO}.
 * 
 * @author Cristhian Murillo
 * @see PaisesDelegate.
 */

public class PaisesHibernateDAO implements IPaisesDAO{

	
	private PaisesDelegate delegate = new PaisesDelegate();
	
	
	@Override
	public ArrayList<Paises> listarPaises() {
		return delegate.listarPaises();
	}


	@Override
	public ArrayList<DtoPaises> listarPaisesDto() {
		return delegate.listarPaisesDto();
	}

	
	
}
