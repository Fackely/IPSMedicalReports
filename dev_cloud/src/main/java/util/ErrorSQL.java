/*
 * @(#)ErrorSQL.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.sql.SQLException;
import java.sql.SQLWarning;

/**
 * Esta clase recorre todas las excepciones <code>SQLException</code> generadas
 * desde la base de datos y muestra una version reducida o una version detallada
 * del error.
 *
 * @version 1.0, Oct 3, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class ErrorSQL {

	/**
	 * Excepcion o cadena de excepciones SQL.
	 */
	private SQLException sqle = null;

	/**
	 * Cadena constante con el tag HTML de separacion de lineas.
	 */
	private static final String nl = "<BR/>";

	/**
	 * Inicializa esta clase con un <code>Throwable</code> generado por el driver de BD.
	 * @param un <code>Throwable</code> que inicializa el estado interno de esta clase
	 */
	public void init (Throwable throwable) throws Throwable {

		
		if (throwable instanceof SQLException) {
			this.sqle = (SQLException) throwable;
		}

		else {
			throw new Throwable("Se Encontró un Error que NO es de la Base de Datos" + nl, throwable);
		}

	}

	/**
	 * Imprime un mensaje sencillo sobre el error proveniente de la BD.
	 * @return el mensaje de la o las excepciones.
	 */
	public String getMessage() {

		StringBuffer sb = new StringBuffer();
		do {
			sb.append(sqle.getMessage());
			sb.append(nl);
		}	while (sqle.getNextException() != null);
		return sb.toString();

	}

	/**
	 * Imprime un mensaje detallado sobre el error proveniente de la BD
	 * @return el mensaje de error, su codigo, estado de SQL, y volcado de pila
	 * por cada una de la o las excepciones recibidas.
	 */
	public String getDetailedMessage() {

		if (sqle instanceof SQLWarning) {
			return getWarning();
		}

		StringBuffer sb = new StringBuffer();
		StackTraceElement [] ste = null;

		do {

			sb.append("Mensaje de Error : ");
			sb.append(sqle.getMessage());
			sb.append(nl);
			sb.append("Código de Error de la Base de Datos : ");
			sb.append(sqle.getErrorCode());
			sb.append(nl);
			sb.append("Estado de SQL : ");
			sb.append(sqle.getSQLState());
			sb.append(nl);
			sb.append("Volcado de pila del error : ");
			sb.append(nl); sb.append(nl);

			ste = sqle.getStackTrace();
			for (int i = 0; i < ste.length; i++) {
				sb.append(ste[i].toString());
				sb.append(nl);
			}
			sb.append(nl);
			sb.append(" ************* ");

		}	while (sqle.getNextException() != null);

		return sb.toString();

	}

	/**
	 * Imprime un mensaje detallado sobre el <i>warning</i> proveniente de la BD
	 * @return el mensaje de <i>warning</i>, su codigo, estado de SQL, y volcado de pila
	 * por cada uno de el o los <i>warning</i> recibidos.
	 */
	private String getWarning () {

		StringBuffer sb = new StringBuffer();
		StackTraceElement [] ste = null;
		SQLWarning sqlw = (SQLWarning) sqle;

		do {

			sb.append("Mensaje de Error : ");
			sb.append(sqlw.getMessage());
			sb.append(nl);
			sb.append("Código de Error de la Base de Datos : ");
			sb.append(sqlw.getErrorCode());
			sb.append(nl);
			sb.append("Estado de SQL : ");
			sb.append(sqlw.getSQLState());
			sb.append(nl);
			sb.append("Volcado de pila del error : ");
			sb.append(nl);

			ste = sqlw.getStackTrace();
			for (int i = 0; i < ste.length; i++) {
				sb.append(ste[i].toString());
				sb.append(nl); sb.append(nl);
			}
			sb.append(nl);
			sb.append(" ************* ");

		}	while (sqlw.getNextWarning() != null);

		return sb.toString();

	}

}