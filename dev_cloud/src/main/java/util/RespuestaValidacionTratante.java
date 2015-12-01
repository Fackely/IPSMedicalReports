/*
 * @(#)RespuestaValidacionTratante.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package util;

/**
 * Esta clase permite manejar el resultado de las validacion que 
 * se hace cuando se necesita saber si el médico tiene permiso y si
 * es el tratante
 * Es usada principalmente por <code>UtilidadValidacion</code> 
 *
 * @version 1.0, May 30, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */
public class RespuestaValidacionTratante extends RespuestaValidacion 
{
	/**
	 * Boolean que dice si el médico es tratante o no
	 */
	public boolean esTratante=false;
	
	/**
	 * Constructor de RespuestaValidacionTratante
	 * 
	 * @param textoRespuesta Texto con la razón por la cual 
	 * puede seguir o no
	 * @param puedoSeguir boolean que define si se
	 * puede seguir o no
	 * @param esTratante boolean que define si el médico
	 * es tratante o no
	 */
	public RespuestaValidacionTratante(String textoRespuesta, boolean puedoSeguir, boolean esTratante)
	{
		super(textoRespuesta, puedoSeguir);
		this.esTratante=esTratante;
	}
	
}
