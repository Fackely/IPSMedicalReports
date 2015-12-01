package com.servinte.axioma.dao.impl.administracion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.administracion.IRegionesCoberturaDAO;
import com.servinte.axioma.orm.RegionesCobertura;
import com.servinte.axioma.orm.delegate.administracion.RegionesCoberturaDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IRegionesCoberturaDAO}.
 * 
 * @author Cristhian Murillo
 * @see RegionesCoberturaDelegate.
 */

public class RegionesCoberturaHibernateDAO implements IRegionesCoberturaDAO{

	
	private RegionesCoberturaDelegate delegate = new RegionesCoberturaDelegate();

	@Override
	public ArrayList<RegionesCobertura> listarRegionesCoberturaActivas() {
		return delegate.listarRegionesCoberturaActivas();
	}
	
}
