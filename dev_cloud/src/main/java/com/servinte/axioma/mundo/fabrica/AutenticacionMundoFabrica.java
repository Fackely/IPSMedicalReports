/**
 * 
 */
package com.servinte.axioma.mundo.fabrica;

import com.servinte.axioma.mundo.impl.autenticacion.AutenticacionMundo;
import com.servinte.axioma.mundo.interfaz.autenticacion.IAutenticacionMundo;

/**
 * Fabrica para la creaci&oacute;n de objetos del mundo
 * que contienen l&oacute;gica referente a 
 * autenticaci&oacute;n.
 * 
 * @author Fernando Ocampo
 * @see com.servinte.axioma.mundo.interfaz.autenticacion.IAutenticacionMundo
 */
public abstract class AutenticacionMundoFabrica {

	private AutenticacionMundoFabrica() {
	}

	public static IAutenticacionMundo crearAutenticacionMundo() {
		return new AutenticacionMundo();
	}
}
