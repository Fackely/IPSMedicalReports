/*
 * @(#)FosygaForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.actionform.manejoPaciente;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;

/**
 * Form que contiene todos los datos especificos para generar 
 * la informacion de fosyga
 * 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Apr 28, 2007
 * @author Wilson Rios wrios@princetonsa.com
 */
public class FosygaForm extends ValidatorForm  
{
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * mapa que contiene los criterios de busqueda
	 */
	private HashMap criteriosBusquedaMap;
	
	/**
	 * mapa con el listado de adminisiones
	 */
	private HashMap listadoMap;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	/**
	 * 
	 */
	private String linkVolver;
	
	/**
	 * Variables Boolean que indica si se esta llamando desde el Consultar/Imprimir ó
	 * desde el Modificar Amparos y Reclamaciones
	 */
	private boolean porConsultarImprimir;
	
	/**
     * resetea los atributos del form
     *
     */
    public void reset()
    {
    	this.criteriosBusquedaMap= new HashMap();
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.listadoMap= new HashMap();
    	this.listadoMap.put("numRegistros", "0");
    	this.linkVolver="";
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
        errores=super.validate(mapping,request);
        
        if(this.getEstado().equals("busquedaAvanzadaXPeriodo"))
        {
        	//la fecha inicial es requerida
        	if(!UtilidadFecha.validarFecha(this.getCriteriosBusquedaMap("fechaInicial").toString()))
			{
				errores.add("Fecha inicial", new ActionMessage("errors.formatoFechaInvalido", " Inicial"));							
			}
			else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechaInicial").toString(), UtilidadFecha.getFechaActual()))
			{
			    errores.add("Fecha inicial", new ActionMessage("errors.fechaPosteriorIgualActual","Inicial "+this.getCriteriosBusquedaMap("fechaInicial"), "Actual "+UtilidadFecha.getFechaActual()));
			}
		    if(!UtilidadFecha.validarFecha(this.getCriteriosBusquedaMap("fechaFinal").toString()))
			{
				errores.add("Fecha final", new ActionMessage("errors.formatoFechaInvalido", "Final"));							
			}
			else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechaFinal").toString(), UtilidadFecha.getFechaActual()))
			{
			    errores.add("Fecha Final", new ActionMessage("errors.fechaPosteriorIgualActual","Final "+this.getCriteriosBusquedaMap("fechaFinal"), "Actual "+UtilidadFecha.getFechaActual()));
			}
			if(errores.isEmpty())
		    {
		        if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getCriteriosBusquedaMap("fechaInicial").toString(), this.getCriteriosBusquedaMap("fechaFinal").toString()))
		        {
		            errores.add("Fecha ini vs Fecha fin", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.getCriteriosBusquedaMap("fechaInicial"), "Final "+this.getCriteriosBusquedaMap("fechaFinal")));
		        }
		    }
			if(errores.isEmpty()&&UtilidadFecha.numeroMesesEntreFechas(this.getCriteriosBusquedaMap("fechaInicial").toString(), this.getCriteriosBusquedaMap("fechaFinal").toString(),true)>3)
				errores.add("", new ActionMessage("errors.rangoMayorTresMeses",""));
			
			if(!errores.isEmpty())
				this.estado="mostrarErroresXPeriodo";
        }
        
       
        
        return errores;
    }

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the criteriosBusqueda
	 */
	public HashMap getCriteriosBusquedaMap() {
		return criteriosBusquedaMap;
	}

	/**
	 * @param criteriosBusqueda the criteriosBusqueda to set
	 */
	public void setCriteriosBusquedaMap(HashMap criteriosBusquedaMap) {
		this.criteriosBusquedaMap = criteriosBusquedaMap;
	}
    
	/**
	 * @return the criteriosBusqueda
	 */
	public Object getCriteriosBusquedaMap(Object key) {
		return criteriosBusquedaMap.get(key);
	}

	/**
	 * @param criteriosBusqueda the criteriosBusqueda to set
	 */
	public void setCriteriosBusquedaMap(Object key, Object value) {
		this.criteriosBusquedaMap.put(key, value);
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return the listadoMap
	 */
	public HashMap getListadoMap() {
		return listadoMap;
	}

	/**
	 * @param listadoMap the listadoMap to set
	 */
	public void setListadoMap(HashMap listadoMap) {
		this.listadoMap = listadoMap;
	}
	
	/**
	 * @return the listadoMap
	 */
	public Object getListadoMap(Object key) {
		return listadoMap.get(key);
	}

	/**
	 * @param listadoMap the listadoMap to set
	 */
	public void setListadoMap(Object key, Object value) {
		this.listadoMap.put(key, value);
	}

	/**
	 * @return the linkVolver
	 */
	public String getLinkVolver() {
		return linkVolver;
	}

	/**
	 * @param linkVolver the linkVolver to set
	 */
	public void setLinkVolver(String linkVolver) {
		this.linkVolver = linkVolver;
	}

	/**
	 * @return the porConsultarImprimir
	 */
	public boolean isPorConsultarImprimir() {
		return porConsultarImprimir;
	}

	/**
	 * @param porConsultarImprimir the porConsultarImprimir to set
	 */
	public void setPorConsultarImprimir(boolean porConsultarImprimir) {
		this.porConsultarImprimir = porConsultarImprimir;
	}
}