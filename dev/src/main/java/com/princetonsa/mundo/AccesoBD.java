/*
 * @(#)AccesoBD.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;

import java.sql.Connection;
import java.sql.SQLException;

import util.TipoNumeroId;

/**
 * Esta interfaz define las operaciones est�ndar que debe implementar un objeto del mundo para
 * acceder a una fuente de datos, a saber : insertar, cargar y modificar.
 *
 * @version 1.0, Mar 1, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public interface AccesoBD {

	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores v�lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicializaci�n fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD);

	/**
	 * Inserta los datos del objeto en una fuente de datos, reutilizando una conexi�n existente,
	 * con la informaci�n presente en los atributos del objeto.
	 * @param con una conexion abierta con una fuente de datos
	 * @return 0 si la inserci�n fall�, > 0 si tuvo �xito.
	 */
	public int insertar(Connection con) throws SQLException;

	/**
	 * Carga un objeto desde una fuente de datos, buscando por
	 * el id suministrado. Establece los atributos del objeto con
	 * los datos cargados.
	 * @param con una conexion abierta con una fuente de datos
	 * @param id pareja tipo/numero, correspondiente a la PK en una tabla necesaria para
	 * identificar de manera �nica el objeto que se desea cargar. Si la PK s�lo depende de
	 * un atributo, por convenci�n �ste se denomina 'tipo'.
	 */
	public void cargar(Connection con, TipoNumeroId id) throws SQLException;

	/**
	 * Modifica un objeto almacenado en una fuente de datos, reutilizando una conexion existente,
	 * con los datos presentes en los atributos del objeto.
	 * @param con una conexion abierta con una fuente de datos
	 * @param id pareja tipo/numero, correspondiente a la PK en una tabla necesaria para
	 * identificar de manera �nica el objeto que se desea modificar. Si la PK s�lo depende de
	 * un atributo, por convenci�n �ste se denomina 'tipo'.
	 * @return 0 si la modificaci�n fall�, > 0 si tuvo �xito
	 */
	public int modificar(Connection con, TipoNumeroId id) throws SQLException;

}