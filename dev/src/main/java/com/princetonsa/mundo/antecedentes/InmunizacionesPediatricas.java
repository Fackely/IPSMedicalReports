/*
 * @(#)InmunizacionesPediatricas.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.mundo.antecedentes;

import java.util.ArrayList;

/**
 * Esta clase 
 *
 * @version 1.0, Abr 7, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>, <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */
public class InmunizacionesPediatricas {
	
	/**
	 * Controla el tipo de accion que se debe efectuar en la BD.
	 * Tiene 3 posibles valores: 
	 * ' t' -> true 
	 * 'f' -> false 
	 * ' ' -> se desconoce
	 */	
	private char estaEnBD;
	
	/**
	 * 
	 */
    private String observaciones;
     
	/**
	 * 
	 */  
    private String nombreInmunizacion;
    
    /**
     * 
     */
    private int codigoInmunizacion;
    
	/**
	 * 
	 */
    private ArrayList dosis; 

	/**
	 * Returns the codigoInmunizacion.
	 * @return String
	 */
	public int getCodigoInmunizacion() {
		return codigoInmunizacion;
	}

	/**
	 * Returns the dosis.
	 * @return ArrayList
	 */
	public ArrayList getDosis() {
		return dosis;
	}

	/**
	 * Returns the nombreInmunizacion.
	 * @return String
	 */
	public String getNombreInmunizacion() {
		return nombreInmunizacion;
	}

	/**
	 * Returns the observaciones.
	 * @return String
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * Sets the codigoInmunizacion.
	 * @param codigoInmunizacion The codigoInmunizacion to set
	 */
	public void setCodigoInmunizacion(int codigoInmunizacion) {
		this.codigoInmunizacion = codigoInmunizacion;
	}

	/**
	 * Sets the dosis.
	 * @param dosis The dosis to set
	 */
	public void setDosis(ArrayList dosis) {
		this.dosis = dosis;
	}

	/**
	 * Sets the nombreInmunizacion.
	 * @param nombreInmunizacion The nombreInmunizacion to set
	 */
	public void setNombreInmunizacion(String nombreInmunizacion) {
		this.nombreInmunizacion = nombreInmunizacion;
	}

	/**
	 * Sets the observaciones.
	 * @param observaciones The observaciones to set
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public InmunizacionesPediatricas() {
		clean();
	}
	public InmunizacionesPediatricas(int codigoInmunizacion, String nombreInmunizacion, String observaciones,  ArrayList dosis) {		
		this.codigoInmunizacion = codigoInmunizacion;
		this.dosis = dosis;
		this.nombreInmunizacion = nombreInmunizacion;
		this.observaciones = observaciones;
		this.estaEnBD = ' ';		
	}
	public void clean() {
		this.codigoInmunizacion = -1;
		this.dosis = null;
		this.nombreInmunizacion = "";
		this.observaciones = "";
		this.estaEnBD = ' ';
	}
	/**
	 * Returns the estaEnBD.
	 * @return char
	 */
	public char getEstaEnBD() {
		return estaEnBD;
	}

	/**
	 * Sets the estaEnBD.
	 * @param estaEnBD The estaEnBD to set
	 */
	public void setEstaEnBD(char estaEnBD) {
		this.estaEnBD = estaEnBD;
	}

} // end InmunizacionesPediatricas



