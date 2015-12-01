/*
 * @(#)PacientesTriageUrgenciasForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform.triage;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * Form que contiene todos los datos específicos para generar 
 * paciente triage urgencias
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Junio 02, 2006
 * @author wrios
 */
public class PacientesTriageUrgenciasForm extends ValidatorForm 
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
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
	
	/**
	 * Máximo numero de registros por p+agina
	 */
	private int maxPageItems;
	
	/**
	 * Fecha Inicial para realizar el filtro
	 * de la busqueda avanzada planteada en
	 * la tarea 753
	 */
	private String fechaInicial;
	
	/**
	 * Fecha Inicial para realizar el filtro
	 * de la busqueda avanzada planteada en
	 * la tarea 753
	 */
	private String fechaFinal;
	
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
		ActionErrors errores = new ActionErrors();
		errores = super.validate(mapping, request);
		
		if(this.estado.equals("busquedaAvanzada"))
		{
			if(this.fechaInicial.trim().equals("") || this.fechaInicial.trim().equals("null"))
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Inicial "));
			if(this.fechaFinal.trim().equals("") || this.fechaFinal.trim().equals("null"))
				errores.add("codigo", new ActionMessage("errors.required","La Fecha Final "));
			if(!UtilidadTexto.isEmpty(this.fechaInicial) && !UtilidadTexto.isEmpty(this.fechaFinal))
			{
				boolean centinelaErrorFechas = false;
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaInicial))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Inicial "+this.fechaInicial));
					centinelaErrorFechas=true;
				}
				if(!UtilidadFecha.esFechaValidaSegunAp(this.fechaFinal))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.fechaFinal));
					centinelaErrorFechas=true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, this.fechaFinal))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Final "+this.fechaFinal));
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal, UtilidadFecha.getFechaActual().toString()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+this.fechaFinal, "Actual "+UtilidadFecha.getFechaActual()));
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, UtilidadFecha.getFechaActual().toString()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+this.fechaInicial, "Actual "+UtilidadFecha.getFechaActual()));
					if(UtilidadFecha.numeroMesesEntreFechasExacta(this.getFechaInicial(), this.getFechaFinal()) >= 3)
						errores.add("", new ActionMessage("error.facturacion.maximoRangoFechas", "para consultar Pacientes Triage Urgencias", "3", "90"));
				}
			}
		}
		
		return errores;
	}
	
	/**
	 * Resetea  los valores de  la forma
	 *
	 */
	public void reset()
	{
		this.mapa= new HashMap();
		this.patronOrdenar = "";
	 	this.ultimoPatron = "";
	 	this.maxPageItems = 0;
	 	this.fechaInicial = UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), -1, false);
	 	this.fechaFinal = UtilidadFecha.getFechaActual();
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
	 * @return Returns the maxPageItems.
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems The maxPageItems to set.
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	
}