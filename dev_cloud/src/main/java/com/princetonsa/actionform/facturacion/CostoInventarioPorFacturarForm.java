package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.UtilidadFecha;
import util.Utilidades;

/**
 * @author Mauricio Jaramillo
 * Fecha: Junio de 2007
 */

public class CostoInventarioPorFacturarForm extends ValidatorForm
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
     * Carga los datos del select de Convenios
     */
    private HashMap centroCosto;
    
    /**
     * Variable que maneja el convenio seleccionado
     */
    private String centroCostoSeleccionado;
    
    /**
     * Mes para el corte
     */
    private String mesCorte;
    
    /**
     * Año para el corte
     */
    private String anoCorte;
    
    /**
     * ArrayList que consulta todos los tipos de inventario existente en axioma
     */
    private ArrayList tiposInventario;
    
    /**
     * Tipo de Inventario Seleccionado
     */
    private String tipoInventario;
    
    /**
     * HashMap con los datos consultados para ejecutar el reporte
     */
    private HashMap costoInventarioPorFacturar;
    
    /**
     * Metodo que inicializa todas las variables.
     */
    public void reset()
    {
    	this.centroAtencion = new HashMap();
    	this.centroAtencion.put("numRegistros", "0");
    	this.codigoCentroAtencion = "";
    	this.centroCosto = new HashMap();
    	this.centroCosto.put("numRegistros", "0");
    	this.centroCostoSeleccionado = "";
    	this.mesCorte = "";
    	this.anoCorte = "";
    	this.tiposInventario = new ArrayList();
    	this.tipoInventario = "";
    	this.costoInventarioPorFacturar = new HashMap();
    	this.costoInventarioPorFacturar.put("numRegistros", "0");
    }
    
    /**
     * Validate
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
    	ActionErrors errores = new ActionErrors();
    	if(this.estado.equals("imprimir"))
    	{
    		if(this.mesCorte.trim().equals("") || this.mesCorte.trim().equals("null"))
				errores.add("mesCorte", new ActionMessage("errors.required","El Mes de Corte "));
    		if(this.anoCorte.trim().equals("") || this.anoCorte.trim().equals("null"))
				errores.add("anoCorte", new ActionMessage("errors.required","El Año de Corte "));
    		if(this.anoCorte.length() != 4)
    			errores.add("", new ActionMessage("errors.formatoAnoInvalido", "de Corte "+this.anoCorte));
    		if(!this.mesCorte.isEmpty() && !this.anoCorte.isEmpty())
    		{
    			if(Utilidades.convertirAEntero(this.mesCorte) < 10 && Utilidades.convertirAEntero(this.mesCorte) > 0)
    				this.setMesCorte("0"+Utilidades.convertirAEntero(this.mesCorte));
    			if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia("01/"+this.mesCorte+"/"+this.anoCorte, UtilidadFecha.getFechaActual()))
        			errores.add("fechaCorte",new ActionMessage("errors.fechaPosteriorIgualActual", "Corte", "Actual"));
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
	 * @return the centroCosto
	 */
	public HashMap getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(HashMap centroCosto) {
		this.centroCosto = centroCosto;
	}
	
	/**
	 * @param key
	 * @return
	 */	
	public Object getCentroCosto(String key) 
	{
		return centroCosto.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCentroCosto(String key, Object value) 
	{
		this.centroCosto.put(key, value);
	}
	
	/**
	 * @return the centroCostoSeleccionado
	 */
	public String getCentroCostoSeleccionado() {
		return centroCostoSeleccionado;
	}

	/**
	 * @param centroCostoSeleccionado the centroCostoSeleccionado to set
	 */
	public void setCentroCostoSeleccionado(String centroCostoSeleccionado) {
		this.centroCostoSeleccionado = centroCostoSeleccionado;
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
	 * @return the costoInventarioPorFacturar
	 */
	public HashMap getCostoInventarioPorFacturar() {
		return costoInventarioPorFacturar;
	}

	/**
	 * @param costoInventarioPorFacturar the costoInventarioPorFacturar to set
	 */
	public void setCostoInventarioPorFacturar(HashMap costoInventarioPorFacturar) {
		this.costoInventarioPorFacturar = costoInventarioPorFacturar;
	}
	
	/**
	 * @param key
	 * @return
	 */	
	public Object getCostoInventarioPorFacturar(String key) 
	{
		return costoInventarioPorFacturar.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCostoInventarioPorFacturar(String key, Object value) 
	{
		this.costoInventarioPorFacturar.put(key, value);
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
	 * @return the anoCorte
	 */
	public String getAnoCorte() {
		return anoCorte;
	}

	/**
	 * @param anoCorte the anoCorte to set
	 */
	public void setAnoCorte(String anoCorte) {
		this.anoCorte = anoCorte;
	}

	/**
	 * @return the mesCorte
	 */
	public String getMesCorte() {
		return mesCorte;
	}

	/**
	 * @param mesCorte the mesCorte to set
	 */
	public void setMesCorte(String mesCorte) {
		this.mesCorte = mesCorte;
	}
	
}