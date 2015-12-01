/*
 * @(#)ListaFuncRoles.java
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
 * Esta clase sirve como contenedora de objetos <code>FuncRoles</code>.
 *
 * @version 1.0, Dec 8, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class ListaFuncRoles extends Vector {

	/**
	 * Crea una nueva instancia de la clase <code>ListaFuncRoles</code>.
	 */
	public ListaFuncRoles() {
		super();
	}

	/**
	 * Crea una nueva instancia de la clase <code>ListaFuncRoles</code>, y
	 * la inicializa con un conjunto de funcionalidades.
	 * @param funcionalidades conjunto de funcionalidades
	 */
	public ListaFuncRoles(Set funcionalidades) {
		super();
		addFuncionalidades(funcionalidades);
	}

	/**
	 * Retorna esta lista de obketos <code>FuncRoles</code> como un conjunto de
	 * objetos <code>Funcionalidad</code>.
	 * @return conjunto de objetos <code>Funcionalidad</code>
	 */
	public synchronized Set expandListaFuncRoles() {

		Funcionalidades funcs = new Funcionalidades();
		Set fs = funcs.getFuncionalidades();
		FuncRoles fr = null;

		for (int i = 0; i < elementCount; i++) {

			// elementData es la estructura de datos interna de Vector
			fr = (FuncRoles) elementData[i];
			fs.addAll(fr.expandFuncionalidades());

		}

		return fs;

	}

	/**
	 * Añade un conjunto de <code>funcionalidad</code>es a este objeto.
	 * @param funcionalidades conjunto de funcionalidades a añadir
	 */
	public synchronized void addFuncionalidades(Set funcionalidades) {

		Funcionalidad f = null;
		Iterator i = funcionalidades.iterator();

		while (i.hasNext()) {

			f = (Funcionalidad) i.next();
			addFuncionalidad(f);

		}

	}

	/**
	 * Añade una <code>Funcionalidad</code> a esta lista de
	 * <code>FuncRoles</code>, cuidando de que, si ya existe una funcionalidad
	 * con ese nombre, sólo se agrega el nuevo nombre del rol, a la
	 * funcionalidad ya existente.
	 * @param f la funcionalidad que se desea añadir
	 */
	public synchronized void addFuncionalidad(Funcionalidad f) {

		boolean found = false;
		FuncRoles fr = null;
		String funcName = f.getFuncName();

		for (int i = 0; i < elementCount; i++) {

			fr = (FuncRoles) elementData[i];
			if (funcName.equals(fr.getFuncName())) {
				found = true;
				break;
			}

		}

		if (!found) {
			super.add(new FuncRoles(f));
		}
		else {
			fr.annexFuncionalidad(f);
		}

	}

	/**
	 * Añade un nuevo objeto <code>FuncRoles</code> a esta lista.
	 * @param fr el objeto <code>FuncRoles</code> que se desea añadir
	 */
	public synchronized void addFuncionalidad(FuncRoles fr) {
		super.add(fr);
	}

}