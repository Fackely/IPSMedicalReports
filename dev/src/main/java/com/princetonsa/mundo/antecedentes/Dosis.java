/*
 * @(#)Dosis.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.mundo.antecedentes;

/**
 * Esta clase 
 *
 * @version 1.0, Abr 7, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>, <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */
public class Dosis {
	
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
    private int numeroDosis; 
    
    /**
     * 
     */
	private boolean refuerzo;
	
	/**
	 * 
	 */
    private String fecha; 

	/**
	 * Returns the esRefuerzo.
	 * @return boolean
	 */
	public boolean isRefuerzo() {
		return refuerzo;
	}

	/**
	 * Returns the fecha.
	 * @return String
	 */
	public String getFecha() {
		return fecha;
	}

	/**
	 * Returns the numeroDosis.
	 * @return int
	 */
	public int getNumeroDosis() {
		return numeroDosis;
	}

	/**
	 * Sets the esRefuerzo.
	 * @param esRefuerzo The esRefuerzo to set
	 */
	public void setRefuerzo(boolean esRefuerzo) {
		this.refuerzo = esRefuerzo;
	}

	/**
	 * Sets the fecha.
	 * @param fecha The fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	/**
	 * Sets the numeroDosis.
	 * @param numeroDosis The numeroDosis to set
	 */
	public void setNumeroDosis(int numeroDosis) {
		this.numeroDosis = numeroDosis;
	}
	public Dosis() {
		clean();
	}
	public Dosis(int numeroDosis, String fecha,  boolean refuerzo) {		
		this.fecha = fecha;
		this.numeroDosis = numeroDosis;
		this.refuerzo = refuerzo;
		this.estaEnBD = ' ';
	}
	public void clean() {
		this.fecha = "";
		this.numeroDosis = -1;
		this.refuerzo = false;
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

} // end Dosis



