package com.servinte.axioma.dao.impl.ordenes;

import com.servinte.axioma.dao.interfaz.ordenes.IRegistroEntregaEntSubDAO;
import com.servinte.axioma.dao.interfaz.ordenes.ISolicitudesPosponerDAO;
import com.servinte.axioma.orm.SolicitudesPosponer;
import com.servinte.axioma.orm.delegate.ordenes.SolicitudesPosponerDelegate;


/**
 * Implementaci&oacute;n de la interfaz {@link IRegistroEntregaEntSubDAO}.
 * @author Cristhian Murillo
 */
public class SolicitudesPosponerHibernateDAO implements ISolicitudesPosponerDAO 
{
	
	private SolicitudesPosponerDelegate solicitudesPosponerDelegate;
	
	
	/**
	 * Constructor
	 */
	public SolicitudesPosponerHibernateDAO()
	{
		solicitudesPosponerDelegate = new SolicitudesPosponerDelegate();
	}


	
	@Override
	public void guardarSolicitudesPosponer(SolicitudesPosponer transientInstance) 
	{
		solicitudesPosponerDelegate.guardarSolicitudesPosponer(transientInstance);
	}



}
