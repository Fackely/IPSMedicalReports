package com.servinte.axioma.servicio.interfaz.tesoreria;

import java.util.ArrayList;

import com.servinte.axioma.orm.TiposIdentificacion;

/**
 * Interfaz donde se define el comportamiento del DAO
 * 
 * @author Cristhian Murillo
 *
 */

public interface ITiposIdentificacionServicio {
	

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
	 * Lista todos los tipos de indentificación del sistema
	 */
	public ArrayList<TiposIdentificacion> listarTodos();
	
	
	
}
