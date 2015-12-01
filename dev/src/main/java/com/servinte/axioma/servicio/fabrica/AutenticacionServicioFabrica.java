/**
 * 
 */
package com.servinte.axioma.servicio.fabrica;

import com.servinte.axioma.servicio.autenticacion.IAutenticacionServicio;
import com.servinte.axioma.servicio.impl.autenticacion.AutenticacionServicio;

/**
 * Fabrica para objetos de la capa de servicios
 * relacionados con las funcionalidades de 
 * autenticaci&oacute;n.
 * 
 * @author Fernando Ocampo
 * @see com.servinte.axioma.servicio.autenticacion.IAutenticacionServicio
 */
public abstract class AutenticacionServicioFabrica {

	private AutenticacionServicioFabrica() {
	}

	public static IAutenticacionServicio crearAuntenticacionServicio() {
		return new AutenticacionServicio();
	}
}
