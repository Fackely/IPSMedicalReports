/*
 * @(#)PacientesUrgenciasSalaEsperaForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform;

import java.util.HashMap;
import org.apache.struts.validator.ValidatorForm;

/**
 * Form que contiene todos los datos específicos la consulta de pacientes de via
 * de ingreso de Urgencias con conducta a seguir Sala de Espera
 * @version 1,0. Julio 24, 2006
 * @author cperalta
 */
public class PacientesUrgenciasSalaEsperaForm extends ValidatorForm 
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
	
	/**
 	 * Este campo contiene el pageUrl para controlar el pager,
 	 *  y conservar los valores del hashMap mediante un submit de
 	 * JavaScript. (Integra pager -Valor Captura)
 	 */
     private String linkSiguiente;
     
     /**
     * Flag que indica que tipo de ordenamiento se va a hacer 
     */
    private Integer orderBy;
	
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
	 	this.linkSiguiente="";
	 	this.orderBy=new Integer(0);
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
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}
	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente= linkSiguiente;
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
	 * @return the orderBy
	 */
	public Integer getOrderBy() {
		return orderBy;
	}

	/**
	 * @param orderBy the orderBy to set
	 */
	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}
	
}