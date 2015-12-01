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
 * consulta, recuperar los datos b�sicos de una persona . Define las
 * <i>scripting variables</i> : <b>numeroIdentificacionPersona</b>,
 * <b>codigoTipoIdentificacionPersona</b>, <b>primerNombrePersona</b>,
 * <b>segundoNombrePersona</b>, <b>primerApellidoPersona</b>,
 * <b>segundoApellidoPersona</b> y <b>nombreCompletoPersona</b>, en donde
 * <b>nombreCompletoPersona</b> es la concatenaci�n de los nombres y apellidos
 * de la persona. Se apoya en la clase
 * <code>TEIMuestraPersonasBasicasStruts</code>. Recibe como par�metro una
 * conexi�n abierta con la base de datos y un 'tipoPersona' que indica de d�nde
 * se desean consultar los datos de la persona. los �nicos valores v�lidos para
 * tipoPersona son : paciente, medico, usuario, responsable y acompanante.
 *
 * @version 1.0, Mar 11, 2003
 * @author 	<a href="mailto:Sandra@PrincetonSA.com">Sandra Moya</a>
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar L&oacute;</a>
 */

public class TagMuestraPersonasBasicasStruts extends BodyTagSupport {

	/**
	 * Objeto que representa una conexi�n con una base de datos.
	 */
	private Connection con = null;

	/**
	 * Tipo de la persona que se desea consultar.
	 */
	private String tipoPersona = "";

	/**
	 * El c�digo de la instituci�n a la cual pertenece esta persona.
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
	 * @return conexi�n usada por el tag
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
	 * Retorna el c�digo de la instituci�n.
	 * @return el c�digo de la instituci�n
	 */
	public String getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * M�todo "Set" que recibe una conexion abierta, para permitir manejar todos
	 * los tags de una misma p�gina con la misma conexi�n
	 * @param con conexi�n abierta con una BD.
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
	 * Establece el c�digo de la instituci�n.
	 * @param codigoInstitucion el c�digo de la instituci�nc�digo de la
	 * instituci�n a establecer
	 */
	public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * Si el tipo de persona es diferente de nulo y no es vac�o, evaluamos el
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

			/* Si llegamos aqu�, es porque acabamos de recorrer todos los resultados */
			else {
				return SKIP_BODY;
			}
		}
		catch (SQLException e) {
			throw new JspTagException("Error Recorriendo El Tag Muestra Personas Basicas Struts : "+e.getMessage());
		}

	}

	/**
	 * Este m�todo escribe en la p�gina las <i>scripting variables</i>
	 * solicitadas seg�n el tipoPersona, usando la presentaci�n presente en la
	 * p�gina. Tambi�n limpia el estado interno de los atributos de este tag.
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
	 * Este m�todo deja en valores vac�os los atributos de este Tag. Debe
	 * llamarse en doEndTag justo antes del return de dicho m�todo; esta es una
	 * precauci�n que se debe tener en caso que el container donde corra esta
	 * aplicaci�n haga pooling de tag handlers y no "limpie" los atributos
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
	 * Este m�todo se conecta con la Base de Datos y trae los resultados de la
	 * consulta solicitada. Los guarda en una <code>Collection</code> de
	 * resultados.
	 */
	private void getData() throws JspTagException {

		/* Consultas para traer de la BD los datos b�sicos de personas solicitados. Algunas dependen de la instituci�n */


		try {
			this.rs = (DaoFactory.getDaoFactory((String) pageContext.getServletContext().getInitParameter("TIPOBD"))).getTagDao().consultaTagMuestraPersonasBasicasStruts (con, tipoPersona, codigoInstitucion) ;
		}
		catch (SQLException sqle) {
			throw new JspTagException("Error Obteniendo Los Datos De La Base De Datos En El Tag Muestra Personas Basicas Struts : "+sqle.getMessage());
		}

	}

	/**
	 * Este m�todo pone en pageContext los valores de las <i>scripting
	 * variables</i>, para cada iteraci�n del tag.
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