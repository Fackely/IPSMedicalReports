package com.servinte.axioma.mundo.interfaz.administracion;

import java.util.ArrayList;

import com.servinte.axioma.orm.TiposIdentificacion;

/**
 * Define la l&oacute;gica de negocio relacionada con los tipos de identificacion del sistema
 * 
 * @author Cristhian Murillo
 * @see com.servinte.axioma.mundo.impl.tesoreria.TiposIdentificacionMundo
 */

public interface ITiposIdentificacionMundo {
	
	
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
