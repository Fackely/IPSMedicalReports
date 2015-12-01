/*
 * @(#)TagMuestraPersonasBasicasStruts.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util.tag;

import java.io.IOException;
import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import util.Encoder;

import com.princetonsa.dao.DaoFactory;

/**
 * Esta clase implementa un tag que permite, dada la tabla y los campos de
 * consulta, recuperar los datos básicos de una persona . Define las
 * <i>scripting variables</i> : <b>numeroIdentificacionPersona</b>,
 * <b>codigoTipoIdentificacionPersona</b>, <b>primerNombrePersona</b>,
 * <b>segundoNombrePersona</b>, <b>primerApellidoPersona</b>,
 * <b>segundoApellidoPersona</b> y <b>nombreCompletoPersona</b>, en donde
 * <b>nombreCompletoPersona</b> es la concatenación de los nombres y apellidos
 * de la persona. Se apoya en la clase
 * <code>TEIMuestraPersonasBasicasStruts</code>. Recibe como parámetro una
 * conexión abierta con la base de datos y un 'tipoPersona' que indica de dónde
 * se desean consultar los datos de la persona. los únicos valores válidos para
 * tipoPersona son : paciente, medico, usuario, responsable y acompanante.
 *
 * @version 1.0, Mar 11, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar L&oacute;</a>
 */

public class TagMuestraPersonasBasicasStruts extends BodyTagSupport {

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;

	/**
	 * Tipo de la persona que se desea consultar.
	 */
	private String tipoPersona = "";

	/**
	 * El código de la institución a la cual pertenece esta persona.
	 */
	private String codigoInstitucion = "";

	/**
	 * En este Buffer vamos guardando los resultados a "imprimir" en el JSP.
	 */
	private StringBuffer output = null;

	/**
	 * <code>ResultSet</code> con los resultados de la consulta
	 */
	private ResultSetDecorator rs = null;

	/**
	 * Metodo "Get" que retorna la conexion
	 * usada por este tag
	 * @return conexión usada por el tag
	 */
	public Connection getCon() {
		return con;
	}

	/**
	 * Retorna el tipo de persona que se desea consultar.
	 * @return el tipo de persona que se desea consultar
	 */
	public String getTipoPersona() {
		return tipoPersona;
	}

	/**
	 * Retorna el código de la institución.
	 * @return el código de la institución
	 */
	public String getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * Método "Set" que recibe una conexion abierta, para permitir manejar todos
	 * los tags de una misma página con la misma conexión
	 * @param con conexión abierta con una BD.
	 */
	public void setCon(Connection con) {
		this.con = con;
	}

	/**
	 * Establece el tipo de persona.
	 * @param tipoPersona el tipo de persona a establecer
	 */
	public void setTipoPersona(String tipoPersona) {
		if (tipoPersona != null) {
			this.tipoPersona = tipoPersona.trim().toLowerCase();
		}
		else {
			this.tipoPersona = "";
		}
	}

	/**
	 * Establece el código de la institución.
	 * @param codigoInstitucion el código de la institucióncódigo de la
	 * institución a establecer
	 */
	public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * Si el tipo de persona es diferente de nulo y no es vacío, evaluamos el
	 * cuerpo de este tag. En caso contrario, no lo evaluamos.
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspTagException {

		if (!tipoPersona.equals("")) {
			getData();
			try {
				if (rs.next()) {
					output = new StringBuffer();
					putData();
					return EVAL_BODY_BUFFERED;
				}
				else {
					return SKIP_BODY;
				}
			}
			catch (SQLException e) {
				throw new JspTagException("Error Escribiendo El Tag Muestra Personas Basicas Struts : "+e.getMessage());
			}
		}
		else {
			return SKIP_BODY;
		}

	}

	/**
	 * Procesa el cuerpo del tag.
	 * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
	 */
	public int doAfterBody() throws JspTagException {

		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null) {
			output.append(bodyContent.getString());
			try {
				bodyContent.clear();
			}
			catch (IOException ex) {
				throw new JspTagException("Error I/O Fatal Limpiando El Cuerpo Del Tag");
			}
		}

		/* Continuamos procesando mientras queden resultados por mostrar */
		try {
			if (rs.next()) {
				putData();
				return EVAL_BODY_AGAIN;
			}

			/* Si llegamos aquí, es porque acabamos de recorrer todos los resultados */
			else {
				return SKIP_BODY;
			}
		}
		catch (SQLException e) {
			throw new JspTagException("Error Recorriendo El Tag Muestra Personas Basicas Struts : "+e.getMessage());
		}

	}

	/**
	 * Este método escribe en la página las <i>scripting variables</i>
	 * solicitadas según el tipoPersona, usando la presentación presente en la
	 * página. También limpia el estado interno de los atributos de este tag.
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspTagException {

		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null && output != null && output.length() > 0) {

			try {
				bodyContent.getEnclosingWriter().write(output.toString());
				clean();
			}
			catch (Exception e) {
				throw new JspTagException("Error Escribiendo El Tag Muestra Personas Basicas Struts : "+e.getMessage());
			}

		}

		return EVAL_PAGE;

	}

	/**
	 * Este método deja en valores vacíos los atributos de este Tag. Debe
	 * llamarse en doEndTag justo antes del return de dicho método; esta es una
	 * precaución que se debe tener en caso que el container donde corra esta
	 * aplicación haga pooling de tag handlers y no "limpie" los atributos
	 * deinstancia de los tags al momento de reutilizarlos.
	 */
	private void clean() throws SQLException {
		tipoPersona = "";
		codigoInstitucion = "";
		output = null;
		rs.close();
		rs = null;
	}

	/**
	 * Este método se conecta con la Base de Datos y trae los resultados de la
	 * consulta solicitada. Los guarda en una <code>Collection</code> de
	 * resultados.
	 */
	private void getData() throws JspTagException {

		/* Consultas para traer de la BD los datos básicos de personas solicitados. Algunas dependen de la institución */


		try {
			this.rs = (DaoFactory.getDaoFactory((String) pageContext.getServletContext().getInitParameter("TIPOBD"))).getTagDao().consultaTagMuestraPersonasBasicasStruts (con, tipoPersona, codigoInstitucion) ;
		}
		catch (SQLException sqle) {
			throw new JspTagException("Error Obteniendo Los Datos De La Base De Datos En El Tag Muestra Personas Basicas Struts : "+sqle.getMessage());
		}

	}

	/**
	 * Este método pone en pageContext los valores de las <i>scripting
	 * variables</i>, para cada iteración del tag.
	 */
	private void putData() throws JspTagException {

		String numeroIdentificacionPersona = "", codigoTipoIdentificacionPersona = "", primerNombrePersona = "", segundoNombrePersona = "", primerApellidoPersona = "", segundoApellidoPersona = "", nombreCompletoPersona = "", loginUsuario = "";

		try {

			numeroIdentificacionPersona = rs.getString("numeroIdentificacionPersona");
			codigoTipoIdentificacionPersona = rs.getString("codigoTipoIdentificacionPersona");

			if (tipoPersona.equals("paciente") || tipoPersona.equals("medico") || tipoPersona.equals("usuario")) {
				primerNombrePersona = Encoder.encode(rs.getString("primerNombrePersona"));
				segundoNombrePersona = Encoder.encode(rs.getString("segundoNombrePersona"));
				primerApellidoPersona = Encoder.encode(rs.getString("primerApellidoPersona"));
				segundoApellidoPersona = Encoder.encode(rs.getString("segundoApellidoPersona"));
				nombreCompletoPersona = primerNombrePersona + " " + segundoNombrePersona + " " + primerApellidoPersona + " " + segundoApellidoPersona;
			}

			else if (tipoPersona.equals("acompanante") || tipoPersona.equals("responsable")) {
				nombreCompletoPersona = Encoder.encode(rs.getString("nombreCompletoPersona"));
			}

			if (tipoPersona.equals("usuario")) {
				loginUsuario = rs.getString("loginUsuario");
			}

		}
		catch (SQLException sqle) {
			throw new JspTagException("Error Poniendo Los Datos En PageContext : " + sqle.getMessage());
		}

		pageContext.setAttribute("numeroIdentificacionPersona", numeroIdentificacionPersona);
		pageContext.setAttribute("codigoTipoIdentificacionPersona", codigoTipoIdentificacionPersona);
		pageContext.setAttribute("primerNombrePersona", primerNombrePersona);
		pageContext.setAttribute("segundoNombrePersona", segundoNombrePersona);
		pageContext.setAttribute("primerApellidoPersona", primerApellidoPersona);
		pageContext.setAttribute("segundoApellidoPersona", segundoApellidoPersona);
		pageContext.setAttribute("nombreCompletoPersona", nombreCompletoPersona);
		pageContext.setAttribute("loginUsuario", loginUsuario);

	}

}