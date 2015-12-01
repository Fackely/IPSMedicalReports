/**
 * 
 */
package com.servinte.axioma.servicio.excepcion;

/**
 * Excepci&oacute;n base para las excepciones lanzadas desde la capa de servicios.
 * 
 * @author Fernando Ocampo
 *
 */
public class ServicioException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8039100751473447303L;

	public ServicioException() {
		super();
	}

	public ServicioException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServicioException(String message) {
		super(message);
	}

	public ServicioException(Throwable cause) {
		super(cause);
	}
}
