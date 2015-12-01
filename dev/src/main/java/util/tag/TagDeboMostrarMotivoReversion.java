/*
 * @(#)TagDeboMostrarMotivoReversion.java
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

import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * Este tag se encarga de buscar la información correspondiente a la reversion
 * de un egreso para una evolucion particular. Devuelve dos variables si 
 * encuentra algun resultado y una de estas (motivoReversionEgresoBoolean)
 * indica si el contenido corresponde a la evolucion especificada 
 * (motivoReversionEgreso)
 * 
 *	@version 1.0, Aug 29, 2003
 */
public class TagDeboMostrarMotivoReversion  extends BodyTagSupport 
{


	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;

	/**
	 * Número de la evolución a la que se esta averiguando si mostro o no
	 * motivo de reversion de egreso
	 */
	private int numeroEvolucion=0;

	/**
	 * <code>ResultSet</code> con los resultados de la consultas a la
	 * fuente de datos
	 */
	private ResultSetDecorator rs = null;

	/**
	 * En este Buffer vamos guardando los resultados a "imprimir" en el JSP.
	 */
	private StringBuffer output = null;
	
	/**
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspTagException 
	{
		if (numeroEvolucion!=0) 
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
					//Si no hay nada, no hay problema, no retornamos las variables
					return SKIP_BODY;
				}
			}
			catch (SQLException e) 
			{
				throw new JspTagException("Error Escribiendo El Tag DeboMostrarMotivoReversion  : "+e.getMessage());
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
				throw new JspTagException("Error I/O Fatal Limpiando El Cuerpo Del Tag");
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

			throw new JspTagException("Error Recorriendo El Tag DeboMostrarMotivoReversion : "+e.getMessage());
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

				throw new JspTagException("Error Escribiendo El Tag DeboMostrarMotivoReversion: "+e.getMessage());
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
		this.numeroEvolucion=0;
		output = null;
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
		    TagDao tagDao=(DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))).getTagDao();
			this.rs = tagDao.consultaTagDeboMostrarDatosMotivoReversion(con, numeroEvolucion);
		}
		catch (SQLException sqle) 
		{
			throw new JspTagException("Error Obteniendo Los Datos De La Base De Datos En El Tag DeboMostrarMotivoReversion : "+sqle.getMessage());
		}

	}

	/**
	 * Este método pone en pageContext los valores de las <i>scripting
	 * variables</i>, para cada iteración del tag.
	 */
	private void recorrerResultSet() throws JspTagException 
	{

		String motivoReversion="", deboMostrarMotivo="", fechaReversion="", horaReversion="", creadorReversionEgreso="";
		
		try 
		{
			motivoReversion=rs.getString("motivoReversion");
			deboMostrarMotivo=rs.getString("deboMostrarMotivo");
			fechaReversion=UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaReversion"));
			horaReversion=UtilidadFecha.convertirHoraACincoCaracteres(rs.getString("horaReversion"));
			creadorReversionEgreso=rs.getString("nombreCompleto");
		}
		catch (SQLException sqle) 
		{
			throw new JspTagException("Error Poniendo Los Datos En PageContext : " + sqle.getMessage());
		}



		if (deboMostrarMotivo==null||deboMostrarMotivo.equals("f"))
		{
			deboMostrarMotivo="false";
		}

		pageContext.setAttribute("motivoReversionEgreso",  motivoReversion);
		pageContext.setAttribute("motivoReversionEgresoBoolean", deboMostrarMotivo );
		pageContext.setAttribute("fechaReversionEgreso", fechaReversion);
		pageContext.setAttribute("horaReversionEgreso", horaReversion);
		pageContext.setAttribute("creadorReversionEgreso", creadorReversionEgreso);
	}

	/**
	 * Returns the con.
	 * @return Connection
	 */
	public Connection getCon() {
		return con;
	}

	/**
	 * Sets the con.
	 * @param con The con to set
	 */
	public void setCon(Connection con) {
		this.con = con;
	}

	/**
	 * @return
	 */
	public int getNumeroEvolucion() {
		return numeroEvolucion;
	}

	/**
	 * @param i
	 */
	public void setNumeroEvolucion(int i) {
		numeroEvolucion = i;
	}

}
