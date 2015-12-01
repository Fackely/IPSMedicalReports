package com.servinte.axioma.common;

import java.io.Serializable;

public class ErrorMessage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8217403944404457082L;

	/** Codigo de error. */
	private String errorKey;
	
	/** Atributo que representa Los param�tros del mensaje de error cuando se requiera.
	 * Esta variable no se deja Generica para recibir cualquier tipo de dato para evitar
	 * un error de seguridad y evitar inyecci�n de c�digo Malicioso */
	private String[] paramsMsg;
	
	/**
	 * Constructor encargado de inicializar el objeto con el c�digo de error 
	 * @param errorCode
	 */
	public ErrorMessage(String errorCode) {
		this.errorKey = errorCode;
	}
	
	/**
	 * Constructor encargado de inicializar el objeto con el c�digo de error 
	 * y la lista de par�metros requeridos por el mensaje (el par�metro varargs String...)
	 * permite pasar los valores de la siguiente manera:
	 * MessageError(1001, "parametro1", "parametro2", "parametro3") �
	 * como un arreglo de String[]
	 * MessageError(1001, new String[] {"parametro1", "parametro2", "parametro3"})
	 * @param errorCode
	 * @param paramsMsg
	 */
	public ErrorMessage(String errorCode, String... paramsMsg) {
		this.errorKey = errorCode;
		this.paramsMsg = paramsMsg;
	}

	/**
	 * @return the errorCode
	 */
	public String getErrorKey() {
		return errorKey;
	}

	/**
	 * @param errorCode the errorCode to set
	 */
	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}

	/**
	 * @return the paramsMsg
	 */
	public String[] getParamsMsg() {
		return paramsMsg;
	}

	/**
	 * @param paramsMsg the paramsMsg to set
	 */
	public void setParamsMsg(String[] paramsMsg) {
		this.paramsMsg = paramsMsg;
	}

}
