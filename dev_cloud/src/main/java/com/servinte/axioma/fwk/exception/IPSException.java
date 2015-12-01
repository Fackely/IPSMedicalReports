package com.servinte.axioma.fwk.exception;

/**
 * Class IPSMException. que encapsula el seguimiento de las
 * excepciones de negocio del sistema
 * 
 * @author ricruico
 * @since 21/06/2012
 */
public class IPSException extends Exception{

	
	
	/**
	 * Serial Version de Serialización 
	 */
	private static final long serialVersionUID = -6532779604699669656L;

	/** Codigo de error. */
	private Long errorCode;
	
	/** Atributo que representa Los paramétros del mensaje de error cuando se requiera.
	 * Esta variable no se deja Generica para recibir cualquier tipo de dato para evitar
	 * un error de seguridad y evitar inyección de código Malicioso */
	private String[] paramsMsg;

	
	/**
	 * Crea una nueva instancia de IPSExcepcion.
	 */
	public IPSException() {
		super();
	}
	/**
	 * Crea una nueva instancia de IPSExcepcion con el codigo de error 
	 * y la excepción lazada.
	 *
	 * @param errorCode Codigo de error de la aplicacion
	 * @param cause Objeto throwable encapsulado
	 */
	public IPSException(long errorCode, Throwable cause) {
		super(cause);
		this.errorCode=errorCode;
	}
	
	

	/**
	 * Crea una nueva instancia de IPSExcepcion con el codigo de error, los parametros del
	 * mensaje de error y la excepción lazada.
	 *
	 * @param errorCode Codigo de error de la aplicacion
	 * @param paramsMsg Parametros necesarios para el mensaje de error de la aplicacion
	 * @param cause Objeto throwable encapsulado
	 */
	public IPSException(long errorCode, String[] paramsMsg, Throwable cause) {
		super(cause);
		this.errorCode=errorCode;
		this.paramsMsg=paramsMsg;
	}
	
	/**
	 * Crea una nueva instancia de IPSExcepcion con la excepción lazada.
	 *
	 * @param cause Objeto throwable encapsulado
	 */
	public IPSException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Crea una nueva instancia de IPSExcepcion con la excepción lazada y
	 * los parámetros del mensaje de error.
	 *
	 * @param cause Objeto throwable encapsulado
	 * @param paramsMsg Parametros necesarios para el mensaje de error de la aplicacion
	 */
	public IPSException(Throwable cause,  String[] paramsMsg) {
		super(cause);
		this.paramsMsg=paramsMsg;
	}

	/**
	 * Crea una nueva instancia de IPSExcepcion con el codigo de error.
	 *
	 * @param errorCode Código de error de la aplicación
	 */
	public IPSException(Long errorCode) {
		super();
		this.errorCode=errorCode;
	}
	
	/**
	 * Crea una nueva instancia de IPSExcepcion con el codigo de error
	 * y los parametros del mensaje de error.
	 *
	 * @param errorCode Código de error de la aplicación
	 * @param paramsMsg Parametros necesarios para el mensaje de error de la aplicacion
	 */
	public IPSException(Long errorCode, String[] paramsMsg) {
		super();
		this.errorCode=errorCode;
		this.paramsMsg=paramsMsg;
	}
	
	
	/**
	 * @return Codigo de Error 
	 */
	public Long getErrorCode() {
		return errorCode;
	}

	/**
	 * @return arreglo de Strings con parametros de mensajes
	 */
	public String[] getParamsMsg() {
		return paramsMsg;
	}

}
