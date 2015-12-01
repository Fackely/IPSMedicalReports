/**
 * 
 */
package com.servinte.axioma.mundo.excepcion;

/**
 * Excepci&oacute;n qué representa errores genericos del Mundo.
 * 
 * @author Fernando Ocampo
 *
 */
public class MundoExcepcion extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8999968372641966007L;

	public MundoExcepcion() {
		super();
	}

	public MundoExcepcion(String message, Throwable cause) {
		super(message, cause);
	}

	public MundoExcepcion(String message) {
		super(message);
	}

	public MundoExcepcion(Throwable cause) {
		super(cause);
	}
}
