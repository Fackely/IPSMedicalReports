/**
 * 
 */
package com.servinte.axioma.dao.excepcion;

/**
 * Excepci&oacute;n qu&eacute;e representa un error
 * al ejecutar alguna operaci&oacute;n en la capa
 * de acceso a datos.
 * 
 * @author Fernando Ocampo
 *
 */
public class DAOExcepcion extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7809120308734055478L;

	public DAOExcepcion() {
		super();
	}

	public DAOExcepcion(String message, Throwable cause) {
		super(message, cause);
	}

	public DAOExcepcion(String message) {
		super(message);
	}

	public DAOExcepcion(Throwable cause) {
		super(cause);
	}
}
