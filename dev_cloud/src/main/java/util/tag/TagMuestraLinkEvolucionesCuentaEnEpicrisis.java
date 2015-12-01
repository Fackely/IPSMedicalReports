/*
 * @(#)TagMuestraLinkEvolucionesCuentaEnEpicrisis.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
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

import com.princetonsa.dao.DaoFactory;

/**
 * 
 *	@version 1.0, Oct 10, 2003
 */
public class TagMuestraLinkEvolucionesCuentaEnEpicrisis extends BodyTagSupport
{

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;

	/**
	 * <code>ResultSet</code> con los resultados de la consulta de Admision
	 * Hospitalaria
	 */
	private ResultSetDecorator rs = null;

	/**
	 * En este Buffer vamos guardando los resultados a "imprimir" en el JSP.
	 */
	private StringBuffer output = null;

	/**
	 * Código de la epicrisis buscada
	 */
	private int idEpicrisis=0;
	
	/**
	 * El listado puede pertenecer tanto a una epicrisis de urgencias
	 * como a una de hospitalización por defecto sera de hospitalizacion 
	 */
	private boolean mostrarHospitalizacion=true;

	/**
	 * Si el tipo de persona es diferente de nulo y no es vacío, evaluamos el
	 * cuerpo de este tag. En caso contrario, no lo evaluamos.
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspTagException 
	{
		//No se valida que los codigos sean 0, pues el paciente
		//puede tener la cuenta cerrada
		if (idEpicrisis>0) 
		{
			abrirResultSet();
			try 
			{
				if (rs.next()) 
				{
					output = new StringBuffer();
					recorrerResultSet();
					return EVAL_BODY_BUFFERED;
				}
				else 
				{
					return SKIP_BODY;
				}
			}
			catch (SQLException e) 
			{
				throw new JspTagException("Error Escribiendo El TagMuestraLinkEvolucionesCuentaEnEpicrisis  : "+e.getMessage());
			}
		}
		else 
		{
			return SKIP_BODY;
		}

	}

	/**
	 * Procesa el cuerpo del tag.
	 * @see javax.servlet.jsp.tagext.IterationTag#doAfterBody()
	 */
	public int doAfterBody() throws JspTagException 
	{

		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null) 
		{

			output.append(bodyContent.getString());
			try 
			{
				bodyContent.clear();
			}
			catch (IOException ex) 
			{
				throw new JspTagException("Error I/O Fatal Limpiando El Cuerpo Del TagMuestraLinkEvolucionesCuentaEnEpicrisis");
			}
		}

		try {
			if (rs.next()) 
			{

				recorrerResultSet();
				return EVAL_BODY_AGAIN;
			}

			else 
			{

				return SKIP_BODY;
			}
		}
		catch (SQLException e) 
		{

			throw new JspTagException("Error Recorriendo El TagMuestraLinkEvolucionesCuentaEnEpicrisis : "+e.getMessage());
		}

	}

	/**
	 * Este método escribe en la página las <i>scripting variables</i>
	 * solicitadas por la página. También limpia el estado interno de los
	 * atributos de este tag.
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspTagException 
	{

		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null && output!=null && output.length() > 0) 
		{

			try 
			{
				bodyContent.getEnclosingWriter().write(output.toString());
				clean();
			}
			catch (Exception e) 
			{

				throw new JspTagException("Error Escribiendo El TagMuestraLinkEvolucionesCuentaEnEpicrisis: "+e.getMessage());
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
	private void clean() throws SQLException 
	{
		idEpicrisis=0;
		output = null;
		this.mostrarHospitalizacion=true;
		if (rs!=null)
		{
			rs.close();
			rs = null;
		}

	}

	/**
	 * Este método se conecta con la Base de Datos y trae los resultados de la
	 * consulta solicitada. Los guarda en una <code>Collection</code> de
	 * resultados.
	 */
	private void abrirResultSet() throws JspTagException 
	{
		try 
		{
			this.rs=(DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))).getTagDao().consultaTagMuestraLinkEvolucionesCuentaEnEpicrisis(con, idEpicrisis, mostrarHospitalizacion);
		}
		catch (SQLException sqle) 
		{
			throw new JspTagException("Error Obteniendo Los Datos De La Base De Datos En El TagMuestraLinkEvolucionesCuentaEnEpicrisis : "+sqle.getMessage());
		}

	}

	/**
	 * Este método pone en pageContext los valores de las <i>scripting
	 * variables</i>, para cada iteración del tag.
	 */
	private void recorrerResultSet() throws JspTagException 
	{
		try 
		{
			pageContext.setAttribute("idCuenta", rs.getString("idCuenta"));
		}
		catch (SQLException sqle) 
		{
			throw new JspTagException("Error Poniendo Los Datos del  TagMuestraLinkEvolucionesCuentaEnEpicrisis En PageContext : " + sqle.getMessage());
		}
	}
	/**
	 * @return
	 */
	public Connection getCon() {
		return con;
	}

	/**
	 * @return
	 */
	public int getIdEpicrisis() {
		return idEpicrisis;
	}

	/**
	 * @return
	 */
	public boolean isMostrarHospitalizacion() {
		return mostrarHospitalizacion;
	}

	/**
	 * @param connection
	 */
	public void setCon(Connection connection) {
		con = connection;
	}

	/**
	 * @param i
	 */
	public void setIdEpicrisis(int i) {
		idEpicrisis = i;
	}

	/**
	 * @param b
	 */
	public void setMostrarHospitalizacion(boolean b) {
		mostrarHospitalizacion = b;
	}

}
