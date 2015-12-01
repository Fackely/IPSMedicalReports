/*
 * Creado el May 12, 2006
 * por Julian Montoya
 */
package com.princetonsa.actionform.interfaz;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class CuentaUnidadFuncionalForm extends ValidatorForm {

	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private transient Logger logger = Logger.getLogger(CuentaUnidadFuncionalForm.class);

	/**
	 * Estado para el manejo del flujo de la funcionalidad
	 */
	private String estado;

	/**
	 * Para almacenar el centro de costo seleccionado.
	 */
	private String centroCosto;

	/**
	 * Mapa para el manejo de las consultas
	 */
	private HashMap mapa;
	
	/**
	 * Variable para almacenar el nombre del grupo seleccionado para mostrarlo al consultar los Tipos de ese Grupo Especifico
	 */
	private String nombreUnidadFuncional;
	
	/**
	 * Varible para paginar los tipos de servicios..... 
	 */
	private int maxPageItems;
	
	/**
	 * Varible para paginar los tipos de servicios..... 
	 */
	private String  linkSiguiente;
	

	/**
	 * Mapa para el manejo de las consultas
	 */
	private HashMap mapaCuenta;
	
	
	/**
	 * Método para limpiar la clase
	 */
	public void reset()
	{
		this.centroCosto = "";
		this.nombreUnidadFuncional = "";
		this.linkSiguiente = "";
		
		this.mapa = new HashMap();
		this.mapaCuenta = new HashMap();
	}
	
	/**
	 * Metodo para validar la informacion digitada por el usuario.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		/*if(estado.equals("guardar"))
		{
			
		}*/
		
		return errores;
	}

	/**
	 * @return Retorna estado.
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param Asigna estado.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return Retorna mapa.
	 */
	public HashMap getMapa() {
		return mapa;
	}

	/**
	 * @param Asigna mapa.
	 */
	public void setMapa(HashMap mapa) {
		this.mapa = mapa;
	}

	/**
	 * @return Retorna mapa.
	 */
	public Object getMapa(String key)
	{
		return mapa.get(key);
	}

	/**
	 * @param Asigna mapa.
	 */
	public void setMapa(String key, Object obj) 
	{
		this.mapa.put(key, obj);
	}

	/**
	 * @return Retorna centroCosto.
	 */
	public String getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param Asigna centroCosto.
	 */
	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @return Retorna linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param Asigna linkSiguiente.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return Retorna maxPageItems.
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param Asigna maxPageItems.
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return Retorna nombreUnidadFuncional.
	 */
	public String getNombreUnidadFuncional() {
		return nombreUnidadFuncional;
	}

	/**
	 * @param Asigna nombreUnidadFuncional.
	 */
	public void setNombreUnidadFuncional(String nombreUnidadFuncional) {
		this.nombreUnidadFuncional = nombreUnidadFuncional;
	}

	public HashMap getMapaCuenta() {
		return mapaCuenta;
	}

	public void setMapaCuenta(HashMap mapaCuenta) {
		this.mapaCuenta = mapaCuenta;
	}

	/**
	 * @return Retorna mapa.
	 */
	public Object getMapaCuenta(String key)
	{
		return mapaCuenta.get(key);
	}

	/**
	 * @param Asigna mapa.
	 */
	public void setMapaCuenta(String key, Object obj) 
	{
		this.mapaCuenta.put(key, obj);
	}

	
}
