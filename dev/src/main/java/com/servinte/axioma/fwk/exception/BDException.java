package com.servinte.axioma.fwk.exception;


/**
 * Class BDException. que encapsula el seguimiento de las
 * excepciones de en la capa de integracion conla BD
 * 
 * @author ricruico
 * @since 21/06/2012
 */
public class BDException extends IPSException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -731893211708406305L;
	
	/**
	 * Crea una nueva instancia de ONEExcepcion.
	 */
	public BDException() {
		super();
	}

	/**
	 * Crea una nueva instancia de ONEExcepcion con la excepción lazada.
	 *
	 * @param cause Objeto throwable encapsulado
	 */
	public BDException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Crea una nueva instancia de IPSExcepcion con el código de error.
	 *
	 * @param errorCode Código de error de la aplicación
	 */
	public BDException(Long errorCode) {
		super(errorCode);
	}
	
	/**
	 * Crea una nueva instancia de IPSExcepcion con el código de error y
	 * los parámetros del mensaje de error.
	 *
	 * @param errorCode Código de error de la aplicación
	 * @param paramsMsj Parámetros del mensaje de error
	 */
	public BDException(Long errorCode, String[] paramsMsj) {
		super(errorCode, paramsMsj);
	}
	
	/**
	 * Crea una nueva instancia de ONEExcepcion con el código de error 
	 * y la excepción lazada.
	 *
	 * @param errorCode Codigo de error de la aplicación
	 * @param cause Objeto throwable encapsulado
	 */
	public BDException(Long errorCode, Throwable cause) {
		super(errorCode,cause);
	}
	
	/**
	 * Crea una nueva instancia de ONEExcepcion con el código de error, 
	 * los parámetros del mensaje de error y la excepción lazada.
	 *
	 * @param errorCode Codigo de error de la aplicación
	 * @param paramsMsj Parámetros del mensaje de error
	 * @param cause Objeto throwable encapsulado
	 */
	public BDException(Long errorCode, String[] paramsMsj, Throwable cause) {
		super(errorCode,paramsMsj,cause);
	}

}
