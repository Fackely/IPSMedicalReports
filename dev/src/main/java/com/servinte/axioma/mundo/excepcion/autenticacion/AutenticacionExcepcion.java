/**
 * 
 */
package com.servinte.axioma.mundo.excepcion.autenticacion;

import com.servinte.axioma.mundo.excepcion.MundoRuntimeExcepcion;

/**
 * Excepci&oacute;n que representa un error al 
 * autenticar un usuario y la cual es obligatoria capturar
 * para quien invoque la funcionalidad que la arroja.
 * 
 * @author Fernando Ocampo
 * @see com.servinte.axioma.mundo.interfaz.autenticacion.IAutenticacionMundo
 */
public class AutenticacionExcepcion extends MundoRuntimeExcepcion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7122015110482555083L;

	public AutenticacionExcepcion() {
		super();
	}

	public AutenticacionExcepcion(String message, Throwable cause) {
		super(message, cause);
	}

	public AutenticacionExcepcion(String message) {
		super(message);
	}

	public AutenticacionExcepcion(Throwable cause) {
		super(cause);
	}
}
