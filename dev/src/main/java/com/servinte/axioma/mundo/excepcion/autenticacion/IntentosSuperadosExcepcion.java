/**
 * 
 */
package com.servinte.axioma.mundo.excepcion.autenticacion;

import com.servinte.axioma.mundo.excepcion.MundoRuntimeExcepcion;

/**
 * Excepci&oacute;n que debe ser arrojada cuando el n&uacute;mero de
 * intentos para autenticaci&oacute;n es superado.
 * 
 * @author Fernando Ocampo
 *
 */
public class IntentosSuperadosExcepcion extends
		MundoRuntimeExcepcion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7820825081481217568L;

	public IntentosSuperadosExcepcion() {
		super();
	}

	public IntentosSuperadosExcepcion(String message,
			Throwable cause) {
		super(message, cause);
	}

	public IntentosSuperadosExcepcion(String message) {
		super(message);
	}

	public IntentosSuperadosExcepcion(Throwable cause) {
		super(cause);
	}
}
