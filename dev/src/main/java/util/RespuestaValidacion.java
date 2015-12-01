/*
 * @(#)RespuestaValidacion.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.io.Serializable;

/**
 * Esta clase permite manejar los resultados de las validaciones hechas
 * antes de hacer cambios en la base de datos que puedan llevar a errores
 * sql, del tipo ingreso de una nueva fila con llave primaria repetida.
 * Es usada principalmente por <code>UtilidadValidacion</code>
 *
 * @version 1.0, Sep 30, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class RespuestaValidacion implements Serializable
{
	/**
	 * Explica la razón de falla de la validacion o un mensaje.
	 */
	public String textoRespuesta="";

	/**
	 * Indica si se puede o no efectuar la modificacion requerida sobre la BD
	 */
	public boolean puedoSeguir=false;
	
	/**
	 * En este campo se va a almacenar un nuevo valor de la respuesta
	 */
	public String nuevoValor="";

	/**
	 * Unico constructor de la clase RespuestaValidacion.
	 * @param textoRespuesta Un <code>String</code> donde se explica porque la validacion fallo o en caso contrario un mensaje genérico
	 * @param puedoSeguir Un <code>Boolean</code> donde se dice si el cambio en la base de datos es permitido. Si no lo es, en el campo textoRespuesta se encuentra la explicación.
	 */
	public RespuestaValidacion (String textoRespuesta, boolean puedoSeguir)
	{
		this.textoRespuesta=textoRespuesta;
		this.puedoSeguir=puedoSeguir;
		this.nuevoValor="";
	}

	/**
	 * Unico constructor de la clase RespuestaValidacion.
	 * @param textoRespuesta Un <code>String</code> donde se explica porque la validacion fallo o en caso contrario un mensaje genérico
	 * @param puedoSeguir Un <code>Boolean</code> donde se dice si el cambio en la base de datos es permitido. Si no lo es, en el campo textoRespuesta se encuentra la explicación.
	 * @param numevoValor: Un nuevo valor de respuesta
	 */
	public RespuestaValidacion (String textoRespuesta, boolean puedoSeguir, String nuevoValor)
	{
		this.textoRespuesta=textoRespuesta;
		this.puedoSeguir=puedoSeguir;
		this.nuevoValor=nuevoValor;
	}

}