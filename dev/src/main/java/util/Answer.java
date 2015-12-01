/*
 * @(#)Answer.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Clase utilitaria, encapsula una conexion y un ResultSetDecorator en un objeto.
 *
 * @version 1.1, Sep 18, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class Answer {

	/**
	 * Un objeto <code>ResultSet</code> producto de una consulta.
	 */
	private ResultSetDecorator resultSet   = null;

	/**
	 * Un objeto <code>Connection</code> que referencia una conexion <i>abierta</i> con una base de datos
	 */
	private Connection connection = null;

	/**
	 * Crea un nuevo objeto <code>Answer</code>
	 * @param resultSet un result set producto de una consulta
	 * @param connection una conexion abierta con una base de datos
	 */
	public Answer (ResultSetDecorator resultSet, Connection connection) {
		this.resultSet  = resultSet;
		this.connection = connection;
	}

	/**
	 * Retorna la conexión.
	 * @return una conexion <i>abierta</i> a una base de datos
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Returna el result set.
	 * @return un <code>ResultSet</code> producto de una consulta
	 */
	public ResultSetDecorator getResultSet() {
		return resultSet;
	}

}