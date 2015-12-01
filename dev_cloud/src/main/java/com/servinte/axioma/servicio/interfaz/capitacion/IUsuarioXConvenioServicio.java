package com.servinte.axioma.servicio.interfaz.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.orm.UsuarioXConvenio;

/**
 * Esta clase se encarga de UsuarioXConvenio
 * @author Cristhian Murillo
 */
public interface IUsuarioXConvenioServicio 
{

	/**
	 * Lista todos
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<UsuarioXConvenio> listarTodos();
	
	
	/**
	 * attachDirty
	 * @param instance
	 */
	public void attachDirty(UsuarioXConvenio instance);


}
