/*
 * @(#)CuentasProcesoFacturacionForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2006. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2
 *
 */
package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * Form que contiene todos los datos específicos para generar 
 * las cuentas en proceso de facturacion
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Julio 6, 2006
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class CuentasProcesoFacturacionForm extends ValidatorForm 
{
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Mapa 
	 */
	private HashMap mapa;

	/**
	 * vod cuenta
	 */
	private String codigoCuenta;
	
	/**
	 * estado cuenta
	 */
	private String nombreEstadoCuenta;
	
	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
	
	/**
	 * 
	 */
	private String idSesion;
	
	/**
	 * 
	 * resetea los atributos del objeto
	 *
	 */
	public void reset()
	{
		this.mapa= new HashMap();
		this.patronOrdenar = "";
	 	this.ultimoPatron = "";
	 	this.codigoCuenta="";
	 	this.nombreEstadoCuenta="";
	 	this.idSesion="";
	}
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		return errores;
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}	
	
	/**
	 * Set del mapa 
	 * @param key
	 * @param value
	 */
	public void setMapa(String key, Object value){
		mapa.put(key, value);
	}
	/**
	 * Get del mapa  
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapa(String key){
		return mapa.get(key);
	}

	/**
	 * @return Returns the mapa.
	 */
	public HashMap getMapa() {
		return mapa;
	}

	/**
	 * @param mapa The mapa to set.
	 */
	public void setMapa(HashMap mapa) {
		this.mapa = mapa;
	}

	/**
	 * @return Returns the patronOrdenar.
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar The patronOrdenar to set.
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return Returns the ultimoPatron.
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron The ultimoPatron to set.
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return Returns the codigoCuenta.
	 */
	public String getCodigoCuenta() {
		return codigoCuenta;
	}

	/**
	 * @param codigoCuenta The codigoCuenta to set.
	 */
	public void setCodigoCuenta(String codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}

	/**
	 * @return Returns the nombreEstadoCuenta.
	 */
	public String getNombreEstadoCuenta() {
		return nombreEstadoCuenta;
	}

	/**
	 * @param nombreEstadoCuenta The nombreEstadoCuenta to set.
	 */
	public void setNombreEstadoCuenta(String nombreEstadoCuenta) {
		this.nombreEstadoCuenta = nombreEstadoCuenta;
	}

	/**
	 * @return the idSesion
	 */
	public String getIdSesion() {
		return idSesion;
	}

	/**
	 * @param idSesion the idSesion to set
	 */
	public void setIdSesion(String idSesion) {
		this.idSesion = idSesion;
	}
	
	
}
