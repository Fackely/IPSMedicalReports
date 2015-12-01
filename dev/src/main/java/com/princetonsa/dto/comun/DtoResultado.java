package com.princetonsa.dto.comun;

import java.io.Serializable;

import org.apache.struts.action.ActionErrors;


/**
 * Dto Usado para retornar el resultado de un query, proceso o validación, en donde se puede indicar
 * si el proceso fue exitoso o no. Se puede retornar una lista de erroes y tambien asignar 
 * un valor y/o al resultado del proceso.
 * 
 * @author Cristhian Murillo
 */
public class DtoResultado implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	/** Valor principal que se quiere mostrar en el resultado, puede ser
	 * una llave primaria o cualquier valor que  se quiera mostarr en el resultado */
	private String pk;
	
	/** Valor secundario que se quiere mostrar en el resultado, puede ser
	 * una llave primaria o cualquier valor que  se quiera mostarr en el resultado */
	private String pk2;
	
	/** En el caso de que el valor a mostrar pueda ser de un tipo u otro (de datos o campos) 
	 * es modificada para perfilar la presentación a lo que se desea esta variable*/
	private String tipoPk;
	
	/** * Indica si el proceso efectuado fue exitoso o no*/
	private boolean exitoso;
	
	/** * Se puede retornar una lista de errores para mostrar en presentacion*/
	private ActionErrors errores;
	
	/** *Variable para almacenar cualquier objeto para retornar con el resultado */
	private Object obj;
	
	
	
	/** Constructor de la calse*/
	public DtoResultado() 
	{
		this.pk			= "";
		this.pk2		= "";
		this.exitoso	= false;
		this.errores 	= new ActionErrors();
		this.obj		= new Object();
	}

	
	public String getPk() {
		return pk;
	}


	public void setPk(String pk) {
		this.pk = pk;
	}


	public boolean isExitoso() {
		return exitoso;
	}


	public void setExitoso(boolean exitoso) {
		this.exitoso = exitoso;
	}


	public ActionErrors getErrores() {
		return errores;
	}


	public void setErrores(ActionErrors errores) {
		this.errores = errores;
	}


	public String getTipoPk() {
		return tipoPk;
	}


	public void setTipoPk(String tipoPk) {
		this.tipoPk = tipoPk;
	}


	public String getPk2() {
		return pk2;
	}


	public void setPk2(String pk2) {
		this.pk2 = pk2;
	}


	public Object getObj() {
		return obj;
	}


	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
