/*
 * @(#)TagMuestraAsocioEvolucionValoracion2.java
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
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Tag que dado el código de la valoración  muestra ( define valirables) 
 * los datos necesarios para mostar esa valoración en una evolucion 
 * particular. Como maneja los mismos datos que al seleccionar la
 * valoración al ingreso de la evolución, utiliza el mismo TEI de
 * TagMuestraAsocioEvolucionValoracion (TEIMuestraAsocioEvolucionValoracion)
 * 
 *	@version 1.0, Oct 21, 2003
 */
public class TagMuestraAsocioEvolucionValoracion2 extends BodyTagSupport
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

	private int numeroSolicitud=0;

	/**
	 * Si el tipo de persona es diferente de nulo y no es vacío, evaluamos el
	 * cuerpo de este tag. En caso contrario, no lo evaluamos.
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspTagException 
	{

		if (numeroSolicitud>0) 
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
				throw new JspTagException("Error Escribiendo El Tag Muestra Ingresos Epicrisis  : "+e.getMessage());
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
				throw new JspTagException("Error I/O Fatal Limpiando El Cuerpo Del TagMuestraAsocioEvolucionValoracion2");
			}
		}

		try 
		{
			
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

			throw new JspTagException("Error Recorriendo El TagMuestraAsocioEvolucionValoracion2 : "+e.getMessage());
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

				throw new JspTagException("Error Escribiendo El TagMuestraAsocioEvolucionValoracion2: "+e.getMessage());
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
		output = null;
		numeroSolicitud=0;
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
			this.rs=(DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))).getTagDao().consultaTagMuestraAsocioEvolucionValoracion2(con, numeroSolicitud);
		}
		catch (SQLException sqle) 
		{
			throw new JspTagException("Error Obteniendo Los Datos De La Base De Datos En El TagMuestraAsocioEvolucionValoracion2 : "+sqle.getMessage());
		}

	}

	/**
	 * Este método pone en pageContext los valores de las <i>scripting
	 * variables</i>, para cada iteración del tag.
	 */
	private void recorrerResultSet() throws JspTagException 
	{
		String fechaValoracion, tipoValoracion, especialidad ;
		UsuarioBasico medicoResultadoValoracion;

		try 
		{

			//Revisamos si este numero nos sirve, de acuerdo a los 
			//permisos
			
			//Ahora si preguntamos el resto

			fechaValoracion=rs.getString("fechaValoracion");
			tipoValoracion=rs.getString("tipoValoracion");

			pageContext.setAttribute("fechaValoracion",  UtilidadFecha.conversionFormatoFechaAAp(fechaValoracion));
			pageContext.setAttribute("tipoValoracion",  tipoValoracion);
			pageContext.setAttribute("numeroSolicitud",  numeroSolicitud+"");
				
			//Solo si el tipo de valoracion es interconsulta, tomamos
			//los otros datos de BD
			
			if (tipoValoracion.equals("interconsulta"))
			{
				medicoResultadoValoracion=new UsuarioBasico();
				medicoResultadoValoracion.cargarUsuarioBasico(con, rs.getString("tipoIdMedico"), rs.getString("numIdMedico"));
				pageContext.setAttribute("medicoResultadoValoracion", medicoResultadoValoracion);
				especialidad=rs.getString("especialidad");
			}
			else
			{
				especialidad="vacio";
			}
				
			pageContext.setAttribute("especialidad",  especialidad);
			pageContext.setAttribute("numeroSolicitud",  numeroSolicitud + "");
			pageContext.setAttribute("entradaValida",  "true");
		}
		catch (SQLException sqle) 
		{
			throw new JspTagException("Error Poniendo Los Datos del  TagMuestraAsocioEvolucionValoracion2 En PageContext : " + sqle.getMessage());
		}

	
	}

	/**
	 * @return
	 */
	public Connection getCon() {
		return con;
	}


	/**
	 * @param connection
	 */
	public void setCon(Connection connection) {
		con = connection;
	}


	/**
	 * @return
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param i
	 */
	public void setNumeroSolicitud(int i) {
		numeroSolicitud = i;
	}

}
