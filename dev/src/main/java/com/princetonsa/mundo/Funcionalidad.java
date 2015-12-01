/*
 * @(#)Funcionalidad.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;

/**
 * Esta clase encapsula los atributos y operaciones para manipular parejas de
 * roles-funcionalidades. A diferencia de <code>FuncRoles</code>, esta clase
 * s�lo almacena un rol por cada funcionalidad. Es usada en la clase
 * <code>Funcionalidades</code> para guardar el estado de
 * <code>RolesFuncsBean</code>, el <i>worker bean</i> de RolFunc.jsp.
 *
 * @version 1.0, Dec 5, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class Funcionalidad {

	/**
	 * Nombre de la funcionalidad.
	 */
	private String funcName;

	/**
	 * Patr�n de URL que identifica la localizaci�n de esta funcionalidad dentro
	 * del contexto de la aplicaci�n.
	 */
	private String urlPattern;

	/**
	 * Nombre del rol que puede acceder esta funcionalidad.
	 */
	private String roleName;

	/**
	 * Indica si esta funcionalidad debe o no ser accedida usando SSL.
	 */
	private boolean isSSL;

	/**
	 * Crea un nuevo objeto <code>Funcionalidad</code>.
	 * @param funcName nombre de la funcionalidad
	 * @param urlPattern patr�n de acceso para esta funcionalidad
	 * @param roleName nombre del rol  
	 * @param isSSL indica si se debe o no usar SSL para acceder esta
	 * funcionalidad
	 */
	public Funcionalidad (String funcName, String urlPattern, String roleName, boolean isSSL) {
		this.funcName = funcName.trim();
		this.urlPattern = urlPattern.trim();
		this.roleName = roleName.trim();
		this.isSSL = isSSL;
	}

	/**
	 * Retorna el nombre de esta funcionalidad.
	 * @return el nombre de esta funcionalidad
	 */
	public String getFuncName() {
		return funcName;
	}

	/**
	 * Retorna el patr�n de URL asignado a esta funcionalidad.
	 * @return el patr�n de URL asignado a esta funcionalidad
	 */
	public String getUrlPattern() {
		return urlPattern;
	}

	/**
	 * Retorna el c�digo de la funcionalidad.
	 * @return el c�digo de la funcionalidad
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * Retorna si el acceso a la funcionalidad debe o no estar cifrada usando SSL.
	 * @return <b>true</b> si se debe usar SSL, <b>false</b> si no
	 */
	public boolean isSSL() {
		return isSSL;
	}

}