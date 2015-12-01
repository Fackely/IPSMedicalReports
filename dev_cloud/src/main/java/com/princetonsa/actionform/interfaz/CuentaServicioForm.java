/*
 * Creado el Apr 18, 2006
 * por Julian Montoya
 */
package com.princetonsa.actionform.interfaz;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;


public class CuentaServicioForm extends ValidatorForm {

	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private transient Logger logger = Logger.getLogger(CuentaServicioForm.class);

	/**
	 * Estado para el manejo del flujo de la funcionalidad
	 */
	private String estado;

	
	
	/**
	 * Variable para manejar que tipo de Grupo se esta listando ( Grupos de Servicios  - Tipos de servicios - Servicios )
	 */
	private String tipoListado;
	
	
	/**
	 * Para almacenar el centro de costo seleccionado.
	 */
	private String centroCosto;

	/**
	 * Para almacenar el nombre del centro de costo seleccionado.
	 */
	private String nombreCentroCosto;
	
	/**
	 * Mapa para el manejo de las consultas
	 */
	private HashMap mapa;

	
	/**
	 * Variable para almacenar el nombre del grupo seleccionado para mostrarlo al consultar los Tipos de ese Grupo Especifico
	 */
	private String nombreGrupoServicio;
	
	
	/**
	 * Variable para almacenar el nombre del grupo seleccionado para mostrarlo al consultar las especialidades.
	 */
	private String 	nombreTipoServicio;
	/**
	 * Variable para almacenar el nombre del grupo seleccionado para mostrarlo al consultar las especialidades.
	 */
	private String 	nombreEspecialidad;
	
	/**
	 * Varible para paginar los tipos de servicios..... 
	 */
	private int maxPageItems;
	
	/**
	 * Varible para paginar los tipos de servicios..... 
	 */
	private String  linkSiguiente;
	
	/**
	 * Método para limpiar la clase
	 */
	public void reset()
	{
		this.centroCosto = "";
		this.tipoListado = "";
		this.nombreCentroCosto = "";
		this.nombreGrupoServicio = "";
		this.nombreTipoServicio= "";
		this.nombreEspecialidad = "";
		this.linkSiguiente = "";
		
		this.mapa = new HashMap();
	}
	
	/**
	 * Metodo para validar la informacion digitada por el usuario.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(estado.equals("guardar"))
		{
			
		}
		
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
	 * @return Retorna tipoListado.
	 */
	public String getTipoListado() {
		return tipoListado;
	}

	/**
	 * @param Asigna tipoListado.
	 */
	public void setTipoListado(String tipoListado) {
		this.tipoListado = tipoListado;
	}

	/**
	 * @return Retorna nombreCentroCosto.
	 */
	public String getNombreCentroCosto() {
		return nombreCentroCosto;
	}

	/**
	 * @param Asigna nombreCentroCosto.
	 */
	public void setNombreCentroCosto(String nombreCentroCosto) {
		this.nombreCentroCosto = nombreCentroCosto;
	}

	/**
	 * @return Retorna nombreGrupoServicio.
	 */
	public String getNombreGrupoServicio() {
		return nombreGrupoServicio;
	}

	/**
	 * @param Asigna nombreGrupoServicio.
	 */
	public void setNombreGrupoServicio(String nombreGrupoServicio) {
		this.nombreGrupoServicio = nombreGrupoServicio;
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
	 * @return Retorna nombreEspecialidad.
	 */
	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}

	/**
	 * @param Asigna nombreEspecialidad.
	 */
	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}

	/**
	 * @return Retorna nombreTipoServicio.
	 */
	public String getNombreTipoServicio() {
		return nombreTipoServicio;
	}

	/**
	 * @param Asigna nombreTipoServicio.
	 */
	public void setNombreTipoServicio(String nombreTipoServicio) {
		this.nombreTipoServicio = nombreTipoServicio;
	}



}
