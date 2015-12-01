/*
 * Julio 13, 2010
 */
package com.servinte.axioma.orm.delegate.administracion;

import java.util.ArrayList;

import org.hibernate.criterion.Restrictions;

import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.orm.TiposIdentificacionHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class TiposIdentificacionDelegate extends TiposIdentificacionHome{
	
	
	
	/**
	 * Lista todos
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TiposIdentificacion> listarTodos()
	{
		return (ArrayList<TiposIdentificacion>) sessionFactory.getCurrentSession()
			.createCriteria(TiposIdentificacion.class)
			.list();
	}
	
	
	
	
	/**
	 * Lista por tipo de tipos de identificacion
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<TiposIdentificacion> listarTiposIdentificacionPorTipo(String[] listaIntegridadDominio)
	{
		return (ArrayList<TiposIdentificacion>) sessionFactory.getCurrentSession()
			.createCriteria(TiposIdentificacion.class)
			.add(Restrictions.in("tipo", listaIntegridadDominio))
			.list();
	}

	/**
	 * M&eacute;todo que retorna un objeto {@link TiposIdentificacion} que corresponde
	 * con el acr&oacute;nimo pasado como par&aacute;metro;
	 * 
	 * @param acronimo
	 * @return
	 */
	public TiposIdentificacion obtenerTipoIdentificacionPorAcronimo (String acronimo){
		
		TiposIdentificacion tiposIdentificacion = super.findById(acronimo);
		
		return tiposIdentificacion;
	}
	
}
