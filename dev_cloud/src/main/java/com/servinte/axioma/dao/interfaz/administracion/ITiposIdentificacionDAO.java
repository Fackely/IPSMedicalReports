package com.servinte.axioma.dao.interfaz.administracion;

import java.util.ArrayList;

import com.servinte.axioma.orm.TiposIdentificacion;

/**
 * Interfaz donde se define el comportamiento del DAO
 * 
 * @author Cristhian Murillo
 *
 */

public interface ITiposIdentificacionDAO {
	

	/**
	 * Lista por tipo de tipos de identificacion
	 */
	public ArrayList<TiposIdentificacion> listarTiposIdentificacionPorTipo(String[] listaIntegridadDominio);
	
	
	/**
	 * M&eacute;todo que retorna un objeto {@link TiposIdentificacion} que corresponde
	 * con el acr&oacute;nimo pasado como par&aacute;metro;
	 * 
	 * @param acronimo
	 * @return
	 */
	public TiposIdentificacion obtenerTipoIdentificacionPorAcronimo (String acronimo);
	
	
	
	/**
	 * Lista todos
	 */
	public ArrayList<TiposIdentificacion> listarTodos();
}
