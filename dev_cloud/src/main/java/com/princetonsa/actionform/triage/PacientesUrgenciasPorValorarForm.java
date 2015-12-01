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
import org.apache.struts.validator.ValidatorForm;

/**
 * Form que contiene todos los datos específicos para generar 
 * paciente de urgencias por valorar
 * @version 1,0. Junio 03, 2006
 * @author cperalta
 */
public class PacientesUrgenciasPorValorarForm extends ValidatorForm 
{
	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Mapa 
	 */
	private HashMap mapaPacientes;

	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
	
	/**
	 * Posicion del Mapa
	 */
	private int posicionMapa;
	
	//Parámetros para el pager 
	private int maxPageItems;
	private String linkSiguiente;
	private int offset;
	
	/**
	 * Resetea  los valores de  la forma
	 *
	 */
	public void reset()
	{
		this.mapaPacientes = new HashMap();
		this.patronOrdenar = "";
	 	this.ultimoPatron = "";
	 	this.posicionMapa = 0;
	 	this.maxPageItems = 0;
	 	this.linkSiguiente = "";
	 	this.offset = 0;
	}
	
	/**
	 * Set del mapaPacientes 
	 * @param key
	 * @param value
	 */
	public void setMapaPacientes(String key, Object value)
	{
		mapaPacientes.put(key, value);
	}
	
	
	/**
	 * @return Returns the posicionMapa.
	 */
	public int getPosicionMapa()
	{
		return posicionMapa;
	}

	/**
	 * @param posicionMapa The posicionMapa to set.
	 */
	public void setPosicionMapa(int posicionMapa)
	{
		this.posicionMapa=posicionMapa;
	}

	/**
	 * Get del mapaPacientes  
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getMapaPacientes(String key)
	{
		return mapaPacientes.get(key);
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
	 * @return Returns the mapaPacientes.
	 */
	public HashMap getMapaPacientes() 
	{
		return mapaPacientes;
	}

	/**
	 * @param mapaPacientes The mapaPacientes to set.
	 */
	public void setMapaPacientes(HashMap mapaPacientes) 
	{
		this.mapaPacientes = mapaPacientes;
	}

	/**
	 * @return Returns the patronOrdenar.
	 */
	public String getPatronOrdenar() 
	{
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar The patronOrdenar to set.
	 */
	public void setPatronOrdenar(String patronOrdenar) 
	{
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return Returns the ultimoPatron.
	 */
	public String getUltimoPatron() 
	{
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron The ultimoPatron to set.
	 */
	public void setUltimoPatron(String ultimoPatron) 
	{
		this.ultimoPatron = ultimoPatron;
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
	
}