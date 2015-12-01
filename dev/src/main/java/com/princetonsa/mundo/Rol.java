/*
 * @(#)Rol.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;

/**
 * Esta clase encapsula los atributos y la funcionalidad de un rol de la
 * aplicación, así como debe aparecer en web.xml
 *
 * @version 1.0, Dec 5, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class Rol {

	/**
	 * Descripción del rol.
	 */
	private String descripcion;

	/**
	 * Nombre del rol.
	 */
	private String nombre;

	/**
	 * Crea un rol nuevo, si no tiene un comentario, le asigna "" por defecto.
	 * @param descripcion descripción del rol
	 * @param nombre nombre del rol
	 */
	public Rol (String descripcion, String nombre) {
		if (descripcion != null && !descripcion.equals("")) {
			this.descripcion = util.UtilidadTexto.removeAccents(descripcion).trim();
		}
		else {
			this.descripcion = "";
		}
		this.nombre = nombre.trim();
	}

	/**
	 * Obtiene la descripción del rol.
	 * @return la descripción del rol
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * Obtiene el nombre del rol.
	 * @return el nombre del rol
	 */
	public String getNombre() {
		return nombre;
	}

}