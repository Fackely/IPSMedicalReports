/*
 * @(#)Roles.java
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

/**
 * Clase contenedora de objetos <code>Rol</code>.
 *
 * @version 1.0, Dec 5, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class Roles {

	/**
	 * Conjunto ordenado y sincronizado de roles.
	 */
	private Set<Rol> roles;

	/**
	 * Crea un nuevo objeto <code>Roles</code>.
	 */
	public Roles () {
		roles = Collections.synchronizedSortedSet(new TreeSet(new RolComparator()));
	}

	/**
	 * Retorna los roles.
	 * @return un conjunto con los roles
	 */
	public Set<Rol> getRoles() {
		return roles;
	}

	/**
	 * Añade un nuevo rol al conjunto de roles.
	 * @param rol el rol que se desea añadir
	 */
	public synchronized void addRol (Rol rol) {
		roles.add(rol);
	}

	/**
	 * Borra un rol del conjunto de roles.
	 * @param roleName nombre del rol que se desea eliminar
	 * @return <b>true</b> si se pudo borrar el rol, <b>false</b> si no
	 */
	public synchronized boolean delRol (String roleName) {

		Iterator<Rol> i = roles.iterator();
		boolean deleted = false;
		Rol rol;

		roleName = roleName.trim();

		while (i.hasNext()) {

			rol = i.next();

			if (rol.getNombre().trim().equals(roleName)) {
				deleted = roles.remove(rol);
				break;
			}

		}

		return deleted;

	}

	/**
	 * Esta clase implementa la <i>interface</i> <code>Comparator</code> y sirve
	 * para efectuar comparaciones entre objetos <code>Rol</code>
	 * 
	 * @version 1.0, Dec 5, 2002
	 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar L&oacute;pez</a>
	 * @author 	<a href="mailto:Camilo@PrincetonSA.com">Camilo Camacho</a>
	 */
	private class RolComparator implements Comparator {

		/**
		 * Compara un objeto de los posibles pertenecientes al conjunto de
		 * roles, con otro. Se trata simplemente de una comparación
		 * de los <code>Strings</code> el nombre de los roles
		 * @param o1 primer objeto a ser comparado
		 * @param o2 segundo objeto a ser comparado
		 * @return -1 si o1&lt;o2, 1 si o1&gt;o2, 0 si o1=o2 , se usa el orden
		 * lexicográfico por tratarse de comparaciones de cadenas de texto.
		 */
		public int compare(Object o1, Object o2) {
			return (((Rol) o1).getNombre()).compareTo(((Rol) o2).getNombre());
		}

	}

}