/*
 * @(#)RolFuncionalidad.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

/**
 * Clase utilitaria para encapsular un nombre de rol con el path releativo al
 * archivo de una funcionalidad propia de dicho rol.
 *
 * @version 1.0, Ene 16, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class RolFuncionalidad implements java.io.Serializable {

	/**
	 * Almacena el nombre de un rol del usuario.
	 */
	private String nombreRol = "";

	/**
	 * Almacena el path relativo de una funcionalidad de la aplicación
	 */
	private String archivoFuncionalidad = "";

	/**
	 * Crea un nuevo objeto <code>RolFuncionalidad</code>.
	 * @param nombreRol nombre del rol del usuario
	 * @param archivoFuncionalidad path del archivo con la funcionalidad
	 * asignada al rol
	 */
	public RolFuncionalidad (String nombreRol, String archivoFuncionalidad) {
		this.nombreRol = nombreRol;
		this.archivoFuncionalidad = archivoFuncionalidad;
	}

	/**
	 * Retorna el path de la funcionalidad.
	 * @return el path de la funcionalidad
	 */
	public String getArchivoFuncionalidad() {
		return archivoFuncionalidad;
	}

	/**
	 * Retorna el nombre del rol.
	 * @return el nombre del rol
	 */
	public String getNombreRol() {
		return nombreRol;
	}

}