/*
 * @(#)AsociarCxCAFacturasCapitadasForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2006. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2
 *
 */
package com.princetonsa.actionform.capitacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

/**
 * Form que contiene todos los datos específicos para generar 
 * AsociarCxCAFacturasCapitadas
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * +@version 1,0. Julio 4, 2006
 * @author wrios 
 */
public class AsociarCxCAFacturasCapitadasForm extends ValidatorForm  
{
	/**
	 * HashMap con los datos del listado, búsqueda avanzada (pager)
	 */
	private HashMap listadoMap;
	
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * citerios de busqueda
	 */
	private HashMap criteriosBusquedaMap;
	
	/**
	 * mensaje de insercion
	 */
	private String mensaje;
	
	/**
     * para la nevagación del pager, cuando se ingresa
     * un registro nuevo.
     */
    private String linkSiguiente;
	
    /**
     * check para colocar todos los seleccione en 'si' o en 'no'
     */
    private String checkSeleccionTodos;
    
    /**
     * almacena el indice por el cual 
     * se va ordenar el HashMap
     */
    private String patronOrdenar;
    
    /**
     * almacena el ultimo indice por el 
     * cual se ordeno el HashMap
     */
    private String ultimoPatron;
    
	/**
	 * 
	 * resetea los atributos del objeto
	 *
	 */
	public void reset()
	{
		this.listadoMap= new HashMap();
		this.criteriosBusquedaMap= new HashMap();
		this.linkSiguiente="";
		this.checkSeleccionTodos="si";
		this.patronOrdenar = "";
        this.ultimoPatron = "";
		resetInsertar();
	}
	
	/**
	 * reset los atributos pa insertar
	 *
	 */
	public void resetInsertar()
	{
		this.mensaje="";
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
		if(estado.equals("busquedaAvanzada"))
		{	
			errores=super.validate(mapping,request);
			
			if(this.getCriteriosBusquedaMap("fechaInicial").trim().equals(""))
			{
				errores.add("Campo Fecha Inicial vacio", new ActionMessage("errors.required","Si parametriza la fecha final entonces el campo Fecha Inicial"));
			}
			if(this.getCriteriosBusquedaMap("fechaFinal").trim().equals(""))
			{
				errores.add("Campo Fecha Final vacio", new ActionMessage("errors.required","Si parametriza la fecha inicial entonces el campo Fecha Final"));
			}
			if(errores.isEmpty())
			{	
				// primero se valida el formato y que los dos esten parametrizados
				if(!this.getCriteriosBusquedaMap("fechaInicial").trim().equals(""))
				{
					//se valida el formato de la fechaInicial
					if(!UtilidadFecha.validarFecha(this.getCriteriosBusquedaMap("fechaInicial").trim()))
					{
						errores.add("Fecha inicial", new ActionMessage("errors.formatoFechaInvalido", " Inicial"));
					}
				}
				if(!this.getCriteriosBusquedaMap("fechaFinal").trim().equals(""))
				{
					//se valida el formato de la fechaInicial
					if(!UtilidadFecha.validarFecha(this.getCriteriosBusquedaMap("fechaFinal").trim()))
					{
						errores.add("Fecha Final", new ActionMessage("errors.formatoFechaInvalido", " Final"));
					}
				}
				//si no existen errores entonces continuar con el resto de validaciones
				if(errores.isEmpty())
				{
					// la fecha inicial debe ser menor a la del sistema
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechaInicial").trim(), UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","Inicial "+this.getCriteriosBusquedaMap("fechaInicial"), "actual "+UtilidadFecha.getFechaActual()));
					}
					//la fecha final debe ser mayor igual a la inicial
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechaInicial").trim(), this.getCriteriosBusquedaMap("fechaFinal").trim()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","Inicial "+this.getCriteriosBusquedaMap("fechaInicial"), "final "+this.getCriteriosBusquedaMap("fechaFinal").trim()));
					}
					// la fecha final debe ser menor a la del sistema
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechaFinal").trim(), UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","Final "+this.getCriteriosBusquedaMap("fechaFinal"), "actual "+UtilidadFecha.getFechaActual()));
					}
				}
			}	
			if(!errores.isEmpty())
			{
				this.setEstado("continuarMostrarErrores");
			}
		}
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
	 * @return Returns the criteriosBusquedaMap.
	 */
	public HashMap getCriteriosBusquedaMap() {
		return criteriosBusquedaMap;
	}

	/**
	 * @param criteriosBusquedaMap The criteriosBusquedaMap to set.
	 */
	public void setCriteriosBusquedaMap(HashMap criteriosBusquedaMap) {
		this.criteriosBusquedaMap = criteriosBusquedaMap;
	}

	/**
	 * @return Returns the criteriosBusquedaMap.
	 */
	public String getCriteriosBusquedaMap(String key) {
		return criteriosBusquedaMap.get(key).toString();
	}

	/**
	 * @param listadoMap The listadoMap to set.
	 */
	public void setCriteriosBusquedaMap(String key, String valor) {
		this.criteriosBusquedaMap.put(key, valor) ;
	}
	
	/**
	 * @return Returns the mensaje.
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje The mensaje to set.
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return Returns the listadoMap.
	 */
	public HashMap getListadoMap() {
		return listadoMap;
	}

	/**
	 * @param listadoMap The listadoMap to set.
	 */
	public void setListadoMap(HashMap listadoMap) {
		this.listadoMap = listadoMap;
	}
	
	/**
	 * @return Returns the listadoMap.
	 */
	public String getListadoMap(String key) {
		return listadoMap.get(key).toString();
	}

	/**
	 * @param listadoMap The listadoMap to set.
	 */
	public void setListadoMap(String key, String valor) {
		this.listadoMap.put(key, valor) ;
	}

	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return Returns the checkSeleccionTodos.
	 */
	public String getCheckSeleccionTodos() {
		return checkSeleccionTodos;
	}

	/**
	 * @param checkSeleccionTodos The checkSeleccionTodos to set.
	 */
	public void setCheckSeleccionTodos(String checkSeleccionTodos) {
		this.checkSeleccionTodos = checkSeleccionTodos;
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
}
