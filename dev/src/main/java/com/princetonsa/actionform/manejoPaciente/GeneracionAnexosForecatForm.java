/*
 * 30 de Abril, 2007
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
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 *	Generacion Anexos Forecat 
 */
public class GeneracionAnexosForecatForm extends ValidatorForm 
{
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * fecha inicial
	 */
	private String fechaInicial;
	/**
	 * fecha final
	 */
	private String fechaFinal;
	/**
	 * tipo de manual
	 */
	private String tipoManual;
	/**
	 * fecha de remisión
	 */
	private String fechaRemision;
	/**
	 * Número de remisión
	 */
	private String numeroRemision;
	/**
	 * mapa Tipos de manuales 
	 */
	private HashMap tiposManuales = new HashMap();
	
	/**
	 * Mapa para almacenar los resultados de la generacion
	 */
	private HashMap resultados = new HashMap();
	
	/**
	 * Variable para almacenar el path de la generacion de archivos
	 */
	private String pathGeneracion;
	
	/**
	 * Variable que indica si hubo inconsistencias
	 */
	private boolean huboInconsistencias;
	
	/**
	 * Mapa que guarda el contenido de un archivo
	 */
	private HashMap contenidoArchivo = new HashMap();
	
	/**
	 * Acrónimo del archivo que se desea cargar
	 */
	private String archivo ;
	
	/**
	 * Variable que indica si estamos en la generacion
	 */
	private boolean generacion;
	
	
	/**
	 * Variable referente a la ejecucion de generacion de rips para la interfaz ax_rips en caso de ser true
	 */
	private boolean esAxRips;
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.tipoManual = "";
		this.fechaRemision = "";
		this.numeroRemision = "";
		this.tiposManuales = new HashMap();
		this.resultados = new HashMap();
		this.pathGeneracion = "";
		this.huboInconsistencias = false;
		this.contenidoArchivo = new HashMap();
		this.archivo = "";
		this.generacion = false;
		this.esAxRips = false;
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
		ActionErrors errores = new ActionErrors();
		
		if(this.estado.equals("generar"))
		{
			this.generacion = false;
			boolean fechaValida = false;
			//********VALIDAR FECHA INICIAL***************************
			if(!this.fechaInicial.equals(""))
			{
				if(!UtilidadFecha.validarFecha(this.fechaInicial))
					errores.add("fecha inicial nos es valida",new ActionMessage("errors.formatoFechaInvalido","inicial"));
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, UtilidadFecha.getFechaActual()))
					errores.add("fecha inicial mayor a fecha actual",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","actual"));
				else
					fechaValida = true;
					
			}
			else
				errores.add("fecha inicial es requeridad",new ActionMessage("errors.required","La fecha inicial"));
			//*******VALIDAR FECHA FINAL*****************************************************
			if(!this.fechaFinal.equals(""))
			{
				if(!UtilidadFecha.validarFecha(this.fechaFinal))
					errores.add("fecha final nos es valida",new ActionMessage("errors.formatoFechaInvalido","final"));
				else
				{
					if(fechaValida&&!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaInicial, this.fechaFinal))
						errores.add("fecha inicial mayor a fecha final",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","final"));
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaFinal, UtilidadFecha.getFechaActual()))
						errores.add("fecha final mayor a fecha actual",new ActionMessage("errors.fechaPosteriorIgualActual","final","actual"));
				}
					
			}
			else
				errores.add("fecha inicial es requerida",new ActionMessage("errors.required","La fecha inicial"));
			//******VALIDAR TIPO DE MANUAL**********************************************+
			if(this.tipoManual.equals(""))
				errores.add("tipo de manual es requerido",new ActionMessage("errors.required","El tipo de manual"));
			//*****FECHA DE REMISION***************************************************
			if(!this.fechaRemision.equals(""))
			{
				if(!UtilidadFecha.validarFecha(this.fechaRemision))
					errores.add("fecha REMISION nos es valida",new ActionMessage("errors.formatoFechaInvalido","de remisión"));
			}
			else
				errores.add("fecha remision es requerida",new ActionMessage("errors.required","La fecha de remisión"));
			//******NÚMERO DE REMISION**********************************************+
			if(this.numeroRemision.equals(""))
				errores.add("número de remisión es requerido",new ActionMessage("errors.required","El número de remisión"));
			
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

	/**
	 * @return the fechaRemision
	 */
	public String getFechaRemision() {
		return fechaRemision;
	}

	/**
	 * @param fechaRemision the fechaRemision to set
	 */
	public void setFechaRemision(String fechaRemision) {
		this.fechaRemision = fechaRemision;
	}

	/**
	 * @return the numeroRemision
	 */
	public String getNumeroRemision() {
		return numeroRemision;
	}

	/**
	 * @param numeroRemision the numeroRemision to set
	 */
	public void setNumeroRemision(String numeroRemision) {
		this.numeroRemision = numeroRemision;
	}

	/**
	 * @return the tipoManual
	 */
	public String getTipoManual() {
		return tipoManual;
	}

	/**
	 * @param tipoManual the tipoManual to set
	 */
	public void setTipoManual(String tipoManual) {
		this.tipoManual = tipoManual;
	}

	/**
	 * @return the tiposManuales
	 */
	public HashMap getTiposManuales() {
		return tiposManuales;
	}

	/**
	 * @param tiposManuales the tiposManuales to set
	 */
	public void setTiposManuales(HashMap tiposManuales) {
		this.tiposManuales = tiposManuales;
	}
	
	/**
	 * @return retorna elemento del mapa tiposManuales
	 */
	public Object getTiposManuales(String key) {
		return tiposManuales.get(key);
	}

	/**
	 * @param Asigna elemento al mapa tiposManuales 
	 */
	public void setTiposManuales(String key,Object obj) {
		this.tiposManuales.put(key,obj);
	}

	/**
	 * @return the resultados
	 */
	public HashMap getResultados() {
		return resultados;
	}

	/**
	 * @param resultados the resultados to set
	 */
	public void setResultados(HashMap resultados) {
		this.resultados = resultados;
	}
	
	/**
	 * @return retorna elemento del mapa resultados
	 */
	public Object getResultados(String key) {
		return resultados.get(key);
	}

	/**
	 * @param Asigna elemento al mapa resultados 
	 */
	public void setResultados(String key, Object obj) {
		this.resultados.put(key,obj);
	}

	/**
	 * @return the huboInconsistencias
	 */
	public boolean isHuboInconsistencias() {
		return huboInconsistencias;
	}

	/**
	 * @param huboInconsistencias the huboInconsistencias to set
	 */
	public void setHuboInconsistencias(boolean huboInconsistencias) {
		this.huboInconsistencias = huboInconsistencias;
	}

	/**
	 * @return the pathGeneracion
	 */
	public String getPathGeneracion() {
		return pathGeneracion;
	}

	/**
	 * @param pathGeneracion the pathGeneracion to set
	 */
	public void setPathGeneracion(String pathGeneracion) {
		this.pathGeneracion = pathGeneracion;
	}

	/**
	 * @return the contenidoArchivo
	 */
	public HashMap getContenidoArchivo() {
		return contenidoArchivo;
	}

	/**
	 * @param contenidoArchivo the contenidoArchivo to set
	 */
	public void setContenidoArchivo(HashMap contenidoArchivo) {
		this.contenidoArchivo = contenidoArchivo;
	}
	
	/**
	 * @return retorna elemento del mapa contenidoArchivo
	 */
	public Object getContenidoArchivo(String key) {
		return contenidoArchivo.get(key);
	}

	/**
	 * @param Asigna elemento al mapa contenidoArchivo 
	 */
	public void setContenidoArchivo(String key,Object obj) {
		this.contenidoArchivo.put(key,obj);
	}

	/**
	 * @return the archivo
	 */
	public String getArchivo() {
		return archivo;
	}

	/**
	 * @param archivo the archivo to set
	 */
	public void setArchivo(String archivo) {
		this.archivo = archivo;
	}

	/**
	 * @return the generacion
	 */
	public boolean isGeneracion() {
		return generacion;
	}

	/**
	 * @param generacion the generacion to set
	 */
	public void setGeneracion(boolean generacion) {
		this.generacion = generacion;
	}

	/**
	 * @return the esAxRips
	 */
	public boolean isEsAxRips() {
		return esAxRips;
	}

	/**
	 * @param esAxRips the esAxRips to set
	 */
	public void setEsAxRips(boolean esAxRips) {
		this.esAxRips = esAxRips;
	}
}
