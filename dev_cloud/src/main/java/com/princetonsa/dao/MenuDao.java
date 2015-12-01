/*
 * @(#)MenuDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para <code>MenuFilter</code>.
 *
 * @version 1.0, Sep 20, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public interface MenuDao {

	/**
	 * Dado un login, obtiene los roles asociados a su respectivo usuario. Este metodo recibe una conexion abierta con una
	 * fuente de datos, y la mantiene abierta despues de ejecutarse.
	 * @param con una conexion abierta con una fuente de datos
	 * @param principal la representacion como cadena de texto de un objeto <code>java.security.Principal</code>
	 * @return un objeto <code>ResultSet</code> con los roles del usuario
	 */
	public ArrayList<HashMap<String, String>> obtenerRoles (Connection con, String principal) throws SQLException;
	
	/**
	 * Método para cargar los menús con sus respectivas funcionalidades
	 * para poder asignarlos al HashTable del MenuFilter
	 * @param con
	 * @return
	 */
	public HashMap obtenerMenus ();

}