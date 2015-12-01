/*
 * @(#)FuncRoles.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;

import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 * Esta clase encapsula los atributos y operaciones para manipular parejas de
 * roles-funcionalidades. A diferencia de <code>Funcionalidad</code>, esta clase
 * almacena  todos los roles que acceden una misma funcionalidad. Es usada en la
 * clase <code>ListaFuncRoles</code> para guardar la información sobre roles y
 * control de acceso a funcionalidades en el formato que debe usarse en web.xml.
 *
 * @version 1.0, Dec 8, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class FuncRoles {

	/**
	 * Nombre de la funcionalidad.
	 */
	private String funcName;

	/**
	 * Patrón de URL que identifica la localización de esta funcionalidad dentro
	 * del contexto de la aplicación.
	 */
	private String urlPattern;

	/**
	 * Lista de los roles que pueden acceder esta funcionalidad.
	 */
	private Vector roleNames;

	/**
	 * Indica si esta funcionalidad debe o no ser accedida usando SSL.
	 */
	private boolean isSSL;

	/**
	 * Crea un nuevo objeto <code>FuncRoles</code> a partir de un objeto
	 * <code>Funcionalidad</code>, e inicializa la lista de roles con un único
	 * valor.
	 * @param func un objeto <code>Funcionalidad</code> ya instanciadao
	 */
	public FuncRoles(Funcionalidad func) {

		this.funcName = func.getFuncName();
		this.urlPattern = func.getUrlPattern();
		this.isSSL = func.isSSL();
		this.roleNames = new Vector();
		roleNames.add(func.getRoleName());

	}

	/**
	 * Crea un nuevo objeto <code>FuncRoles</code>.
	 * @param funcName nombre de la funcionalidad 
	 * @param urlPattern patrón de acceso al URL de esta funcionalidad
	 * @param roleNames lista de los roles que pueden acceder a esta
	 * funcionalidad
	 * @param isSSL indica si se debe o no acceder a esta funcionalidad usando
	 * SSL
	 */
	public FuncRoles(String funcName, String urlPattern, Vector roleNames, boolean isSSL) {

		this.funcName = funcName;
		this.urlPattern = urlPattern;
		this.roleNames = roleNames;
		this.isSSL = isSSL;

	}

	/**
	 * Returna el nombre de esta funcionalidad.
	 * @return el nombre de esta funcionalidad
	 */
	public String getFuncName() {
		return funcName;
	}

	/**
	 * Dice si la funcionalidad debe o no accederse usando SSL.
	 * @return <b>true</b> si se debe usar SSL, <b>false</b> si no
	 */
	public boolean isSSL() {
		return isSSL;
	}

	/**
	 * Retorna el <code>Vector</code> con los nombres de los roles que usan esta funcionalidad.
	 * @return el <code>Vector</code> con los nombres de los roles que usan esta funcionalidad
	 */
	public Vector getRoleNames() {
		return roleNames;
	}

	/**
	 * Retorna el patrón de URL asociado a esta funcionalidad
	 * @return el patrón de URL asociado a esta funcionalidad
	 */
	public String getUrlPattern() {
		return urlPattern;
	}

	/**
	 * Dado un objeto <code>Funcionalidad</code> que represente la misma
	 * funcionalidad que ESTE objeto <code>FuncRoles</code>, le anexa un nuevo
	 * nombre de rol a este objeto.
	 * @param func el objeto cuyo nombre de rol se desea anexar
	 */
	public synchronized void annexFuncionalidad(Funcionalidad func) {

		if (func.getFuncName().equals(this.getFuncName())) {
			/* Si hay por lo menos UN rol que deba usar SSL para acceder a una
			   funcionalidad, todos  los roles deben accederla usando SSL   */
			if (!this.isSSL && func.isSSL()) {
				this.isSSL = func.isSSL();
			}
			if (!roleNames.contains(func.getRoleName())) {
				roleNames.add(func.getRoleName());
			}
		}

	}

	/**
	 * Este método retorna el contenido de este objeto <code>FuncRoles</code>
	 * como un conjunto de <code>Funcionalidad</code>es.
	 * @return conjunto de <code>Funcionalidad</code>es de este objeto
	 */
	public synchronized Set expandFuncionalidades() {

		String roleName = "";
		Iterator i = this.roleNames.iterator();
		Funcionalidades funcs = new Funcionalidades();

		while (i.hasNext()) {

			roleName = (String) i.next();
			funcs.addFuncionalidad(new Funcionalidad(this.funcName, this.urlPattern, roleName, this.isSSL));

		}

		return funcs.getFuncionalidades();

	}

}