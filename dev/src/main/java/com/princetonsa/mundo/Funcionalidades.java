/*
 * @(#)Funcionalidades.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import util.UtilidadTexto;


/**
 * Esta clase sirve como contenedora de objetos <code>Funcionalidad</code>.
 *
 * @version 1.0, Dec 5, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class Funcionalidades {

	/**
	 * Conjunto sincronizado y ordenado de funcionalidades.
	 */
	private Set funcionalidades;

	/**
	 * Crea un nuevo objeto <code>Funcionalidades</code>
	 */
	public Funcionalidades() {
		funcionalidades = Collections.synchronizedSortedSet(new TreeSet(new FuncionalidadComparator()));
	}

	/**
	 * Retorna los roles.
	 * @return un conjunto con los roles
	 */
	public Set getFuncionalidades() {
		return funcionalidades;
	}

	/**
	 * Establece las funcionalidades.
	 * @param funcionalidades las funcionalidades a ser establecidas
	 */
	public void setFuncionalidades(Set funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

	/**
	 * Añade una nueva funcionalidad al conjunto de funcionalidades.
	 * @param func la funcionalidad a ser añadida
	 */
	public synchronized void addFuncionalidad(Funcionalidad func) {
		funcionalidades.add(func);
	}

	/**
	 * Elimina una funcionalidad del conjunto de funcionalidades.
	 * @param roleName nombre del rol al cual se asigno la funcionalidad que se
	 * desea eliminar
	 * @param funcName nombre de la funcionalidad que se desea eliminar
	 * @return <code>true</code> si se pudo borrar la funcionalidad,
	 * <code>false</code> si no
	 */
	public synchronized boolean delFuncionalidad(String roleName, String funcName) {

		Iterator i = funcionalidades.iterator();
		Funcionalidad func;
		boolean deleted = false;

		roleName = roleName.trim();
		funcName = funcName.trim();

		while (i.hasNext()) {
			func = (Funcionalidad) i.next();
			if (func.getRoleName().trim().equals(roleName) && func.getFuncName().trim().equals(funcName)) {
				deleted = funcionalidades.remove(func);
				break;
			}
		}

		return deleted;

	}

	/**
	 * Esta clase implementa la <i>interface</i> <code>Comparator</code> y sirve
	 * para efectuar comparaciones entre objetos <code>Funcionalidad</code>
	 * 
	 * @version 1.0, Dec 5, 2002
	 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar L&oacute;pez</a>
	 * @author 	<a href="mailto:Camilo@PrincetonSA.com">Camilo Camacho</a>
	 */
	private class FuncionalidadComparator implements Comparator {

		/**
		 * Compara un objeto de los posibles pertenecientes al conjunto de
		 * funcionalidades, con otro. Se trata simplemente de una comparación
		 * de los <code>Strings</code> con el <i>role-name</i> + <i>web-resource-name</i>
		 * de las funcionalidades.
		 * @param o1 primer objeto a ser comparado
		 * @param o2 segundo objeto a ser comparado
		 * @return -1 si o1&lt;o2, 1 si o1&gt;o2, 0 si o1=o2 , se usa el orden
		 * lexicográfico por tratarse de comparaciones de cadenas de texto.
		 */
		public int compare(Object o1, Object o2) {

			Funcionalidad f1 = (Funcionalidad) o1;
			Funcionalidad f2 = (Funcionalidad) o2;
			String [] resp = new String [0];
			
			resp = UtilidadTexto.separarNombresDeCodigos(f1.getFuncName(), 1);			
			String fn1 = resp[1];
			
			resp = UtilidadTexto.separarNombresDeCodigos(f2.getFuncName(), 1);			
			String fn2 = resp[1];
			return (f1.getRoleName() + fn1).compareTo(f2.getRoleName() + fn2);

		}

	}

}