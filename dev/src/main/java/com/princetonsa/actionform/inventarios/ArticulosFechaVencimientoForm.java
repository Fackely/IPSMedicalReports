/*
 * Marzo 28, del 2007
 */
package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

/**
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 *Consulta de articulos por fecha de vencimiento
 */
public class ArticulosFechaVencimientoForm extends ValidatorForm 
{
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Objeto donde se almacena el listado de articulos x fecha vencimiento
	 */
	private HashMap listado = new HashMap();
	
	/**
	 * Número de registros del mapa listado
	 */
	private int numListado;
	
	/**
	 * Campo de filtro del listado
	 */
	private String fecha;
	
	/**
	 * Campo que contiene la fecha del sistema
	 */
	private String fechaActual;
	
	//******CAMPOS PARA LA BUSQUEDA AVANZADA**************
	private boolean busquedaAvanzada; //permite saber si la busqueda venía de busqueda avanzada
	private String codigoArticulo;
	private String descripcionArticulo;
	
	//*****CAMPOS PARA PAGINADOR************************+
	private int maxPageItems;
	private String linkSiguiente;
	private int offset;
	private String indice;
	private String ultimoIndice;
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.listado = new HashMap();
		this.numListado = 0;
		this.fecha = "";
		this.fechaActual = "";
		
		//campos de la busqueda avanzada
		this.busquedaAvanzada = false;
		this.codigoArticulo = "";
		this.descripcionArticulo = "";
		
		//campos para paginador
		this.maxPageItems = 0;
		this.linkSiguiente = "";
		this.offset = 0;
		this.indice = "";
		this.ultimoIndice = "";
		
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
			if(this.fecha.equals(""))
			{
				 errores.add("La Fecha", new ActionMessage("errors.required", "La Fecha de Vencimiento Corte"));
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
	 * @return the listado
	 */
	public HashMap getListado() {
		return listado;
	}

	/**
	 * @param listado the listado to set
	 */
	public void setListado(HashMap listado) {
		this.listado = listado;
	}
	
	/**
	 * @return elemento del mapa listado
	 */
	public Object getListado(String key) {
		return listado.get(key);
	}

	/**
	 * @param Asigna elemento al mapa listado
	 */
	public void setListado(String key,Object obj) {
		this.listado.put(key,obj);
	}

	/**
	 * @return the numListado
	 */
	public int getNumListado() {
		return numListado;
	}

	/**
	 * @param numListado the numListado to set
	 */
	public void setNumListado(int numListado) {
		this.numListado = numListado;
	}

	/**
	 * @return the busquedaAvanzada
	 */
	public boolean isBusquedaAvanzada() {
		return busquedaAvanzada;
	}

	/**
	 * @param busquedaAvanzada the busquedaAvanzada to set
	 */
	public void setBusquedaAvanzada(boolean busquedaAvanzada) {
		this.busquedaAvanzada = busquedaAvanzada;
	}

	/**
	 * @return the codigoArticulo
	 */
	public String getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * @param codigoArticulo the codigoArticulo to set
	 */
	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * @return the descripcionArticulo
	 */
	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}

	/**
	 * @param descripcionArticulo the descripcionArticulo to set
	 */
	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}

	/**
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * @return the indice
	 */
	public String getIndice() {
		return indice;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}

	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}

	/**
	 * @return the ultimoIndice
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}

	/**
	 * @param ultimoIndice the ultimoIndice to set
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}

	/**
	 * @return the fechaActual
	 */
	public String getFechaActual() {
		return fechaActual;
	}

	/**
	 * @param fechaActual the fechaActual to set
	 */
	public void setFechaActual(String fechaActual) {
		this.fechaActual = fechaActual;
	}

}
