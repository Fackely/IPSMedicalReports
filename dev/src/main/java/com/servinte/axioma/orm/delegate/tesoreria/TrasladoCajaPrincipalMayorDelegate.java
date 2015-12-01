/*
 * Septiembre 01, 2011
 */
package com.servinte.axioma.orm.delegate.tesoreria;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.TrasladoCajaPrincipalMayor;
import com.servinte.axioma.orm.TrasladoCajaPrincipalMayorHome;


/**
 * @author Cristhian Murillo
 *
 * Clase que contiene logica del negocio sobre el modelo 
 */
public class TrasladoCajaPrincipalMayorDelegate extends TrasladoCajaPrincipalMayorHome
{

	/**
	 *  Guarda la entidad en la base de datos.
	 *  @param transientInstance
	 *  @author Cristhian Murillo
	 *  
	 *  @return boolean
	 */
	public boolean persistTrasladoCajaPrincipalMayor(TrasladoCajaPrincipalMayor transientInstance) 
	{
		boolean save = false;
		try{
			super.persist(transientInstance);
			save = true;
		}
		catch (Exception e) {
			save = false;
			Log4JManager.error("No se pudo guardar el Traslado a Caja Mayor: ",e);
		}
		
		return save;
	}

}
