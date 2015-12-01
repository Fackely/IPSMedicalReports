package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class CostoInventariosPorAlmacenForm extends ValidatorForm
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
     * HashMap con la informacion del mapa
     */
    private HashMap costoTotalInventario;
    
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
    	this.costoTotalInventario = new HashMap();
    	this.costoTotalInventario.put("numRegistros", "0");
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
	 * @param key
	 * @return
	 */	
	public Object getCentroAtencion(String key) 
	{
		return centroAtencion.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCentroAtencion(String key, Object value) 
	{
		this.centroAtencion.put(key, value);
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
	 * @return the costoTotalInventario
	 */
	public HashMap getCostoTotalInventario() {
		return costoTotalInventario;
	}

	/**
	 * @param costoTotalInventario the costoTotalInventario to set
	 */
	public void setCostoTotalInventario(HashMap costoTotalInventario) {
		this.costoTotalInventario = costoTotalInventario;
	}

	/**
	 * @param key
	 * @return
	 */	
	public Object getCostoTotalInventario(String key) 
	{
		return costoTotalInventario.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCostoTotalInventario(String key, Object value) 
	{
		this.costoTotalInventario.put(key, value);
	}
	
}