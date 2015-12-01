/*
 * @(#)TagValoresPorDefecto.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util.tag;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import util.Encoder;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * Esta clase implementa un tag que permite, dado el nombre de un parámetro,
 * recuperar de la Base de Datos el valor por defecto respectivo. Define
 * las <i>scripting variables</i> <b>valor</b> y <b>nombre</b>, y se apoya en la
 * clase <code>TEIValoresPorDefecto</code>.
 *
 * @version 1.0, Jun 05, 2003
 */

public class TagValoresPorDefecto extends BodyTagSupport {

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con;

	/**
	 * Código del parámetro al cual se le va a pedir el valor por defecto.
	 */
	private String parametro;

	/**
	 * Metodo "Get" que retorna la conexion
	 * usada por este tag
	 * @return conexión usada por el tag
	 */
	public Connection getCon ()	{
		return con;
	}

	/**
	 * Retorna el parámetro especificado
	 * @return string con el parámetro especificado
	 */
	public String getParametro() {
		return parametro;
	}

	/**
	 * Método "Set" que recibe una conexion para permitir manejar todos los tags
	 * de una misma pagina con la misma conexión
	 * @param con conexion
	 */
	public void setCon (Connection con)	{
		this.con = con;
	}

	/**
	 * Establece el parámetro.
	 * @param parametro el nombre del parámetro a establecer
	 */
	public void setParametro(String parametro) {
		if (parametro != null) {
			this.parametro = parametro;
		}
		else {
			this.parametro = "";
		}
	}

	/**
	 * Si el nombre del parámetro es diferente de nulo y no es vacío, evaluamos
	 * el cuerpo de este tag. En caso contrario, no lo evaluamos.
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspTagException {
		if (parametro != null && !parametro.trim().equals("")) {
			getData();
			return EVAL_BODY_BUFFERED;
		}
		else {
			return SKIP_BODY;
		}
	}

	/**
	 * Este método escribe en la página el valor y nombre por defecto pedidos
	 * según el parámetro, usando la presentación presente en la página. También
	 * limpia el estado interno de los atributos de este tag.
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspTagException {

		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null) {

			try {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}	catch (Exception e) {
					throw new JspTagException("Error Escribiendo El Tag Valores Por Defecto : " + e.getMessage());
			}

		}

		clean();
		return EVAL_PAGE;
	}

	/**
	 * Este método deja en valores vacíos, mas no nulos, los atributos de este
	 * Tag. Debe llamarse en doEndTag justo antes del return de dicho método;
	 * esta es una precaución que se debe tener en caso que el container donde
	 * corra esta aplicación haga pooling de tag handlers y no "limpie" los
	 * valores de los tags al momento de reutilizarlos.
	 */
	private void clean() {
		this.parametro = "";
	}

	/**
	 * Este método obtiene de la BD los datos de "valor" y "nombre" asociados al
	 * "parametro" pasado a este tag, y los pone en el contexto de la página
	 * como <i>scripting variables</i>.
	 */
	private void getData() throws JspTagException {

		String valor = "", nombre = "";
		TagDao tagDao = (DaoFactory.getDaoFactory((String) pageContext.getServletContext().getInitParameter("TIPOBD"))).getTagDao();
		try {

			ResultSetDecorator rs=tagDao.consultaTagValoresPorDefecto(con, parametro);

			if (rs.next())	{
				valor  = rs.getString("valor");
				nombre = Encoder.encode(rs.getString("nombre"));
			}
			else {
				throw new SQLException("Seleccione Un Parámetro Válido");
			}

		}	catch (SQLException sqle) {
				throw new JspTagException("Error Obteniendo Los Datos De La Base De Datos En El Tag Valores Por Defecto : " + sqle.getMessage());
		}

		pageContext.setAttribute("valor" ,  valor);
		pageContext.setAttribute("nombre", nombre);

	}

}