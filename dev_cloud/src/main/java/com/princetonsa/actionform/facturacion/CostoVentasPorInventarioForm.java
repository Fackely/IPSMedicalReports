package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class CostoVentasPorInventarioForm extends ValidatorForm
{

	private String estado;
	
	 /**
     * HashMap de los Centros de Atencion 
     */
    private HashMap centroAtencion;
    
    /**
     * Codigo del Centro de Atencion seleccionado para realizar el filtro
     */
    private String codigoCentroAtencion;
	
    /**
     * Variable que maneja los centros de costo por Almacen
     */
    private HashMap centroCostoAlmacen;
    
    /**
     * Variable que guarda la seleccion del centro de costo por almacen
     */
    private String centroCostoSeleccionadoAlmacen;
    
    /**
     * Variable que maneja los centros de costo solicitante
     */
    private HashMap centroCostoSolicitante;
    
    /**
     * Variable que guarda la seleccion del centro de costo solicitante
     */
    private String centroCostoSeleccionadoSolicitante;
    
    /**
     * ArrayList que consulta todos los tipos de inventario existente en axioma
     */
    private ArrayList tiposInventario;
    
    /**
     * Tipo de Inventario Seleccionado
     */
    private String tipoInventario;
    
    /**
     * Fecha Inicial
     */
    private String fechaInicial;
    
    /**
     * Fecha Final
     */
    private String fechaFinal;
    
    /**
     * HashMap con los datos consultados para ejecutar el reporte
     */
    private HashMap costoVentasPorInventario;
    
    /**
     * Metodo que inicializa todas las variables.
     */
    public void reset()
    {
    	this.centroAtencion = new HashMap();
    	this.centroAtencion.put("numRegistros", "0");
    	this.codigoCentroAtencion = "";
    	this.centroCostoAlmacen = new HashMap();
    	this.centroCostoAlmacen.put("numRegistros", "0");
    	this.centroCostoSeleccionadoAlmacen = "";
    	this.centroCostoSolicitante = new HashMap();
    	this.centroCostoSolicitante.put("numRegistros", "0");
    	this.centroCostoSeleccionadoSolicitante = "";
    	this.tiposInventario = new ArrayList();
    	this.tipoInventario = "";
    	this.fechaInicial = "";
    	this.fechaFinal = "";
    	this.costoVentasPorInventario = new HashMap();
    	this.costoVentasPorInventario.put("numRegistros", "0");
    	this.fechaInicial = "";
    	this.fechaFinal = "";
    }
    
    /**
     * Validate
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
    	ActionErrors errores = new ActionErrors();
    	if(this.estado.equals("imprimir"))
    	{
    		if(this.fechaInicial.trim().equals("") || this.fechaInicial.trim().equals("null"))
				errores.add("fechaInicial", new ActionMessage("errors.required","La Fecha Inicial "));
    		if(this.fechaFinal.trim().equals("") || this.fechaFinal.trim().equals("null"))
				errores.add("fechaFinal", new ActionMessage("errors.required","La Fecha Final "));
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
				}
			}
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
	 * @return the centroAtencion
	 */
	public HashMap getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(HashMap centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the codigoCentroAtencion
	 */
	public String getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	/**
	 * @param codigoCentroAtencion the codigoCentroAtencion to set
	 */
	public void setCodigoCentroAtencion(String codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	/**
	 * @return the centroCostoAlmacen
	 */
	public HashMap getCentroCostoAlmacen() {
		return centroCostoAlmacen;
	}

	/**
	 * @param centroCostoAlmacen the centroCostoAlmacen to set
	 */
	public void setCentroCostoAlmacen(HashMap centroCostoAlmacen) {
		this.centroCostoAlmacen = centroCostoAlmacen;
	}
	
	/**
	 * @param key
	 * @return
	 */	
	public Object getCentroCostoAlmacen(String key) 
	{
		return centroCostoAlmacen.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCentroCostoAlmacen(String key, Object value) 
	{
		this.centroCostoAlmacen.put(key, value);
	}
	
	/**
	 * @return the centroCostoSeleccionadoAlamcen
	 */
	public String getCentroCostoSeleccionadoAlmacen() {
		return centroCostoSeleccionadoAlmacen;
	}

	/**
	 * @param centroCostoSeleccionadoAlmacen the centroCostoSeleccionadoAlmacen to set
	 */
	public void setCentroCostoSeleccionadoAlmacen(String centroCostoSeleccionadoAlmacen) {
		this.centroCostoSeleccionadoAlmacen = centroCostoSeleccionadoAlmacen;
	}

	/**
	 * @return the centroCostoSolicitante
	 */
	public HashMap getCentroCostoSolicitante() {
		return centroCostoSolicitante;
	}

	/**
	 * @param centroCostoSolicitante the centroCostoSolicitante to set
	 */
	public void setCentroCostoSolicitante(HashMap centroCostoSolicitante) {
		this.centroCostoSolicitante = centroCostoSolicitante;
	}
	
	/**
	 * @param key
	 * @return
	 */	
	public Object getCentroCostoSolicitante(String key) 
	{
		return centroCostoSolicitante.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCentroCostoSolicitante(String key, Object value) 
	{
		this.centroCostoSolicitante.put(key, value);
	}
	
	/**
	 * @return the centroCostoSeleccionadoSolicitante
	 */
	public String getCentroCostoSeleccionadoSolicitante() {
		return centroCostoSeleccionadoSolicitante;
	}

	/**
	 * @param centroCostoSeleccionadoSolicitante the centroCostoSeleccionadoSolicitante to set
	 */
	public void setCentroCostoSeleccionadoSolicitante(String centroCostoSeleccionadoSolicitante) {
		this.centroCostoSeleccionadoSolicitante = centroCostoSeleccionadoSolicitante;
	}
	
	/**
	 * @return the tipoInventario
	 */
	public String getTipoInventario() {
		return tipoInventario;
	}

	/**
	 * @param tipoInventario the tipoInventario to set
	 */
	public void setTipoInventario(String tipoInventario) {
		this.tipoInventario = tipoInventario;
	}

	/**
	 * @return the tiposInventario
	 */
	public ArrayList getTiposInventario() {
		return tiposInventario;
	}

	/**
	 * @param tiposInventario the tiposInventario to set
	 */
	public void setTiposInventario(ArrayList tiposInventario) {
		this.tiposInventario = tiposInventario;
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
	 * @return the costoVentasPorInventario
	 */
	public HashMap getCostoVentasPorInventario() {
		return costoVentasPorInventario;
	}

	/**
	 * @param costoVentasPorInventario the costoVentasPorInventario to set
	 */
	public void setCostoVentasPorInventario(HashMap costoVentasPorInventario) {
		this.costoVentasPorInventario = costoVentasPorInventario;
	}
	
	/**
	 * @param key
	 * @return
	 */	
	public Object getCostoVentasPorInventario(String key) 
	{
		return costoVentasPorInventario.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCostoVentasPorInventario(String key, Object value) 
	{
		this.costoVentasPorInventario.put(key, value);
	}
   
}