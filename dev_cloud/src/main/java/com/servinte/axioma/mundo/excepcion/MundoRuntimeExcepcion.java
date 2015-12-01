/**
 * 
 */
package com.servinte.axioma.mundo.excepcion;

/**
 * Excepci&oacute;n <code>unchecked</code> que representa un error al 
 * autenticar un usuario.
 * 
 * @author Fernando Ocampo
 *
 */
public class MundoRuntimeExcepcion extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5963339510478627266L;

	public MundoRuntimeExcepcion() {
		super();
	}

	public MundoRuntimeExcepcion(String message, Throwable cause) {
		super(message, cause);
	}

	public MundoRuntimeExcepcion(String message) {
		super(message);
	}

	public MundoRuntimeExcepcion(Throwable cause) {
		super(cause);
	}
}
