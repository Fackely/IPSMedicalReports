/*
 * @(#)Especialidad.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;

/**
 * Esta clase encapsula los atributos y la funcionalidad de una especialidad de un m�dico.
 *
 * @version 1.0, Oct 26, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class Especialidad {

	/**
	 * Nombre de una especialidad de un m�dico
	 */
	private String especialidad = "";

	/**
	 * C�digo de una especialidad de un m�dico
	 */
	private String codigoEspecialidad = "";

	/**
	 * Define si esta especialidad est� activa en el sistema
	 */
	private boolean activaSistema=false;

	/**
	 * Constructora de objetos <code>Especialidad</code>
	 * @param codigoEspecialidad codigo de una especialidad
	 * @param especialidad nombre de la especialidad
	 */
	public Especialidad (String codigoEspecialidad, String especialidad, boolean activaSistema) {
		this.codigoEspecialidad = codigoEspecialidad;
		this.especialidad = especialidad;
		this.activaSistema = activaSistema;
	}

	/**
	 * Retorna la especialidad de un m�dico.
	 * @return la especialidad de un m�dico
	 */
	public String getEspecialidad() {
		return this.especialidad;
	}

	/**
	 * Retorna el c�digo de la especialidad de un m�dico.
	 * @return el c�digo de la especialidad de un m�dico
	 */
	public String getCodigoEspecialidad() {
		return this.codigoEspecialidad;
	}

	/**
	 * Retorna el hecho de si esta especialidad esta activa en el
	 * sistema o no 
	 * @return el c�digo de la especialidad de un m�dico
	 */
	public boolean getActivaSistema() {
		return activaSistema ;
	}
	
	/**
	 * Retorna el hecho de si esta especialidad esta activa en el
	 * sistema o no 
	 * @return el c�digo de la especialidad de un m�dico
	 */
	public boolean isActivaSistema()
	{
		return activaSistema;
	}	

}