package com.servinte.axioma.mundo.excepcion.autenticacion;

import com.servinte.axioma.mundo.excepcion.MundoRuntimeExcepcion;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class VerificacionIngresoUnicosException extends MundoRuntimeExcepcion{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	

	/**
	 * 
	 */
	public VerificacionIngresoUnicosException() {
		super();
	}

	/**
	 * 
	 * @param message
	 * @param cause
	 */
	public VerificacionIngresoUnicosException(String message,
			Throwable cause) {
		super(message, cause);
	}

	
	/**
	 * 
	 * @param message
	 */
	public VerificacionIngresoUnicosException(String message) {
		super(message);
	}
	

	/**
	 * 
	 * @param cause
	 */
	public VerificacionIngresoUnicosException(Throwable cause) {
		super(cause);
	}
	
	

}
