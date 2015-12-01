/*
 * ErrorsList.java 
 * Autor			:  mdiaz
 * Creado el	:  29-nov-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package util;

import java.util.ArrayList;

/**
 * descripcion de esta clase
 *
 * @version 1.0, 29-nov-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public class ErrorsList {
	/**
	 * ArrayList que contendra los errores generados en el action
	 */
	private ArrayList errors;

	/**
	 * String que contendra el nombre del tipo de errores, puede ser 'critical' o 'normal'
	 */
	private String errorType;
	
	
	public ErrorsList(){
		this.errors = new ArrayList();
		this.errorType = "normal";
	}
	
	
	public void reset(){
		if(errors == null)
		  this.errors = new ArrayList();
		
		this.errors.clear();
		this.errorType = "normal";
	}		
	
	public int getNumErrors(){
		return errors.size(); 
	}
	
	public ArrayList getErrors() {
		return errors;
	}

	public void setErrors(ArrayList errors) {
		this.errors = errors;
	}
	
	
	public void addError(String errorStr){
	  if(errors == null)
	  	errors = new ArrayList();
	  
	  errors.add(errorStr);
	}
	
	public void clearErrors(){
		this.reset();
	}
	
	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	
}
