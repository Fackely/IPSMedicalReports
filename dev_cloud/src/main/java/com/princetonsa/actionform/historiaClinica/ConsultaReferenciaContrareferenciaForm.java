/*
 * @(#)ConsultaReferenciaContrareferenciaForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.historiaClinica.InstitucionesSirc;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * Form que contiene todos los datos especificos para generar 
 * 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. May 25, 2007
 * @author Wilson Rios wrios@princetonsa.com
 */
public class ConsultaReferenciaContrareferenciaForm extends ValidatorForm 
{
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * indica si la busqueda es x paciente o por periodo
	 */
	private boolean esBusquedaXpaciente;
	
	/**
	 * codigo del paciente
	 */
	private String codigoPaciente;
	
	/**
	 * tipo referencia
	 */
	private String tipoReferenciaInterna;
	
	/**
	 * tipo contrareferencia
	 */
	private String tipoContrareferencia;
	
	
	/**
	 * rango inicial del nro referencia
	 */
	private String rangoNroReferenciaInicial;
	
	/**
	 * rango final del nro referencia
	 */
	private String rangoNroReferenciaFinal;
	
	/**
	 * fecha finalizacion incial
	 */
	private String fechaFinalizacionInicial;
	
	/**
	 * fecha finalizacion final
	 */
	private String fechaFinalizacionFinal;
	
	/**
	 * codigo de la institucion Origen
	 */
	private String codigoInstitucionOrigen;
	
	/**
	 * codigo de la institcuion
	 */
	private int codigoInstitucion;
	
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
	 * mapa listado
	 */
	private HashMap mapaListado;
	
	/**
	 * tag para mostrar las instituciones
	 */
	private HashMap institucionesSircTagMap;
	
	/**
     * resetea los atributos del form
     *
     */
    public void reset()
    {
    	this.esBusquedaXpaciente=true;
		this.codigoPaciente="";
		this.tipoReferenciaInterna=ConstantesBD.acronimoNo;
		this.tipoContrareferencia=ConstantesBD.acronimoNo;
		this.rangoNroReferenciaInicial="";
		this.rangoNroReferenciaFinal="";
		this.fechaFinalizacionInicial="";
		this.fechaFinalizacionFinal="";
		this.codigoInstitucionOrigen="";
		this.codigoInstitucion=ConstantesBD.codigoNuncaValido;
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.linkVolver="";
    	this.mapaListado= new HashMap();
    	this.mapaListado.put("numRegistros", "0");
    	this.institucionesSircTagMap= new HashMap();
    	this.institucionesSircTagMap.put("numRegistros", "0");
    }
    
    /**
     * 
     */
    public void inicializarTagsMap(int codigoInstitucion)
    {
    	this.setInstitucionesSircTagMap(InstitucionesSirc.cargarInstituciones(codigoInstitucion));
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
        
        if(this.estado.equals("resultadoBusquedaXPeriodo"))
        {
        	if(this.getTipoReferenciaInterna().equals(ConstantesBD.acronimoNo) && this.getTipoContrareferencia().equals(ConstantesBD.acronimoNo))
        	{
        		errores.add("", new ActionMessage("errors.required","El Tipo"));
        	}
        	boolean existeError=false;
    		if(!UtilidadTexto.isEmpty(this.getRangoNroReferenciaInicial()) || !UtilidadTexto.isEmpty(this.getRangoNroReferenciaFinal()))
        	{
    			if(UtilidadTexto.isEmpty(this.getRangoNroReferenciaInicial()))
        		{
        			errores.add("", new ActionMessage("errors.required","Si ingresa información en el Nro. Referencia Final entonces el Nro. de Referencia Inicial"));
        			existeError=true;
        		}
        		if(UtilidadTexto.isEmpty(this.getRangoNroReferenciaFinal()))
        		{
        			errores.add("", new ActionMessage("errors.required","Si ingresa información en el Nro. Referencia Inicial entonces el Nro. de Referencia Final"));
        			existeError=true;
        		}
        		if(!existeError)
        		{
        			try
        			{
	        			double nroInicial= Double.parseDouble(this.getRangoNroReferenciaInicial());
	        			double nroFinal= Double.parseDouble(this.getRangoNroReferenciaFinal());
	        			
	        			if(nroFinal<nroInicial)
	        			{
	        				errores.add("", new ActionMessage("errors.MayorIgualQue", "El Nro. Referencia Final", "el Nro. Referencia Inicial"));
	        			}
	        		}
        			catch (Exception e) 
        			{
        				errores.add("", new ActionMessage("errors.integer","Tanto el Nro. Referencia Inicial como el Nro. de Referencia Final"));
					}
        		}
        	}
    		existeError=false;
    		if(!UtilidadTexto.isEmpty(this.getFechaFinalizacionInicial()) || !UtilidadTexto.isEmpty(this.getFechaFinalizacionFinal()))
        	{
    			if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaFinalizacionInicial()))
        		{
        			errores.add("", new ActionMessage("errors.formatoFechaInvalido","Inicial"));
        			existeError=true;
        		}
    			if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaFinalizacionFinal()))
        		{
        			errores.add("", new ActionMessage("errors.formatoFechaInvalido","Final"));
        			existeError=true;
        		}
        		if(!existeError)
        		{
        			if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaFinalizacionInicial(), this.getFechaFinalizacionFinal()))
        			{
        				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","Inicial","Final"));
        			}
        			if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaFinalizacionFinal(), UtilidadFecha.getFechaActual()))
        			{
        				errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual","Final","Actual"));
        			}
        		}
        	}
        }
        if(!errores.isEmpty())
        {
        	if(!this.esBusquedaXpaciente)
        		estado= "continuarErroresXPeriodo";
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
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * @return the codigoInstitucionOrigen
	 */
	public String getCodigoInstitucionOrigen() {
		return codigoInstitucionOrigen;
	}

	/**
	 * @param codigoInstitucionOrigen the codigoInstitucionOrigen to set
	 */
	public void setCodigoInstitucionOrigen(String codigoInstitucionOrigen) {
		this.codigoInstitucionOrigen = codigoInstitucionOrigen;
	}

	/**
	 * @return the codigoPaciente
	 */
	public String getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the esBusquedaXpaciente
	 */
	public boolean getEsBusquedaXpaciente() {
		return esBusquedaXpaciente;
	}

	/**
	 * @param esBusquedaXpaciente the esBusquedaXpaciente to set
	 */
	public void setEsBusquedaXpaciente(boolean esBusquedaXpaciente) {
		this.esBusquedaXpaciente = esBusquedaXpaciente;
	}

	/**
	 * @return the fechaFinalizacionFinal
	 */
	public String getFechaFinalizacionFinal() {
		return fechaFinalizacionFinal;
	}

	/**
	 * @param fechaFinalizacionFinal the fechaFinalizacionFinal to set
	 */
	public void setFechaFinalizacionFinal(String fechaFinalizacionFinal) {
		this.fechaFinalizacionFinal = fechaFinalizacionFinal;
	}

	/**
	 * @return the fechaFinalizacionInicial
	 */
	public String getFechaFinalizacionInicial() {
		return fechaFinalizacionInicial;
	}

	/**
	 * @param fechaFinalizacionInicial the fechaFinalizacionInicial to set
	 */
	public void setFechaFinalizacionInicial(String fechaFinalizacionInicial) {
		this.fechaFinalizacionInicial = fechaFinalizacionInicial;
	}

	/**
	 * @return the rangoNroReferenciaFinal
	 */
	public String getRangoNroReferenciaFinal() {
		return rangoNroReferenciaFinal;
	}

	/**
	 * @param rangoNroReferenciaFinal the rangoNroReferenciaFinal to set
	 */
	public void setRangoNroReferenciaFinal(String rangoNroReferenciaFinal) {
		this.rangoNroReferenciaFinal = rangoNroReferenciaFinal;
	}

	/**
	 * @return the rangoNroReferenciaInicial
	 */
	public String getRangoNroReferenciaInicial() {
		return rangoNroReferenciaInicial;
	}

	/**
	 * @param rangoNroReferenciaInicial the rangoNroReferenciaInicial to set
	 */
	public void setRangoNroReferenciaInicial(String rangoNroReferenciaInicial) {
		this.rangoNroReferenciaInicial = rangoNroReferenciaInicial;
	}

	/**
	 * @return the tipoContrareferencia
	 */
	public String getTipoContrareferencia() {
		return tipoContrareferencia;
	}

	/**
	 * @param tipoContrareferencia the tipoContrareferencia to set
	 */
	public void setTipoContrareferencia(String tipoContrareferencia) {
		this.tipoContrareferencia = tipoContrareferencia;
	}

	/**
	 * @return the tipoReferenciaInterna
	 */
	public String getTipoReferenciaInterna() {
		return tipoReferenciaInterna;
	}

	/**
	 * @param tipoReferenciaInterna the tipoReferenciaInterna to set
	 */
	public void setTipoReferenciaInterna(String tipoReferenciaInterna) {
		this.tipoReferenciaInterna = tipoReferenciaInterna;
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
	 * @return the mapaListado
	 */
	public HashMap getMapaListado() {
		return mapaListado;
	}

	/**
	 * @param mapaListado the mapaListado to set
	 */
	public void setMapaListado(HashMap mapaListado) {
		this.mapaListado = mapaListado;
	}
	
	/**
	 * @return the mapaListado
	 */
	public Object getMapaListado(Object key) {
		return mapaListado.get(key);
	}

	/**
	 * @param mapaListado the mapaListado to set
	 */
	public void setMapaListado(Object key, Object value) {
		this.mapaListado.put(key, value);
	}

	/**
	 * @return the institucionesSircTagMap
	 */
	public HashMap getInstitucionesSircTagMap() {
		return institucionesSircTagMap;
	}

	/**
	 * @param institucionesSircTagMap the institucionesSircTagMap to set
	 */
	public void setInstitucionesSircTagMap(HashMap institucionesSircTagMap) {
		this.institucionesSircTagMap = institucionesSircTagMap;
	}
	
	/**
	 * @return the institucionesSircTagMap
	 */
	public Object getInstitucionesSircTagMap(Object key) {
		return institucionesSircTagMap.get(key);
	}

	
}