/*
 * @(#)FuncionalidadMenu.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

/**
 * Clase utilitaria para encapsular un nombre de funcionalidad con el path
 * releativo a su archivo.
 *
 * @version 1.0, Ene 16, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class FuncionalidadMenu implements java.io.Serializable {

	/**
	 * Almacena el nombre de una funcionalidad a la cual el usuario tiene
	 * acceso.
	 */
	private String nombreFuncionalidad = "";

	/**
	 * Almacena el path relativo de una funcionalidad de la aplicación
	 */
	private String archivoFuncionalidad = "";

	/**
	 * Almacena el codigo de la funcionalidad
	 */
	private String codigoFuncionalidad="";

	/**
	 * Crea un nuevo objeto <code>FuncionalidadMenu</code>.
	 * @param nombreFuncionalidad nombre del rol del usuario
	 * @param archivoFuncionalidad path del archivo con la funcionalidad
	 * asignada al usuario
	 * @param codigo de la funcionalidad
	 */
	public FuncionalidadMenu (String nombre, String archivo, String codigo) {
		this.nombreFuncionalidad = nombre;
		this.archivoFuncionalidad = archivo;
		this.codigoFuncionalidad = codigo;
	}

	/**
	 * Retorna el path de la funcionalidad.
	 * @return el path de la funcionalidad
	 */
	public String getArchivoFuncionalidad() {
		return archivoFuncionalidad;
	}

	/**
	 * Retorna el nombre del la funcionalidad.
	 * @return el nombre del rol
	 */
	public String getNombreFuncionalidad() {
		return nombreFuncionalidad;
	}
	
	/**
	 * Retorna el codigo de la funcionalidad.
	 * @return el codigo de la funcionalidad
	 */
	public String getCodigoFuncionalidad() {
		return codigoFuncionalidad;
	}

}