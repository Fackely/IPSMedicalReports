package com.servinte.axioma.dao.impl.administracion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.administracion.ITiposIdentificacionDAO;
import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.orm.delegate.administracion.TiposIdentificacionDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link ITiposIdentificacionDAO}.
 * 
 * @author Cristhian Murillo
 * @see TiposIdentificacionDelegate.
 */

public class TiposIdentificacionHibernateDAO implements ITiposIdentificacionDAO{

	
	private TiposIdentificacionDelegate delegate = new TiposIdentificacionDelegate();

	
	
	@Override
	public ArrayList<TiposIdentificacion> listarTiposIdentificacionPorTipo(String[] listaIntegridadDominio) 
	{
		return delegate.listarTiposIdentificacionPorTipo(listaIntegridadDominio);
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.administracion.ITiposIdentificacionDAO#obtenerTipoIdentificacionPorAcronimo(java.lang.String)
	 */
	@Override
	public TiposIdentificacion obtenerTipoIdentificacionPorAcronimo(String acronimo) {
	
		return delegate.obtenerTipoIdentificacionPorAcronimo(acronimo);
	}
	
	
	/**
	 * Lista todos
	 */
	public ArrayList<TiposIdentificacion> listarTodos(){
		return delegate.listarTodos();
	}

}
