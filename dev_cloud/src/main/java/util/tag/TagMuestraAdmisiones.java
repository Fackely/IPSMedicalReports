/*
 * @(#)TagMuestraAdmisiones.java
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
 * @version 1.0, Mar 27, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */

public class TagMuestraAdmisiones extends BodyTagSupport 
{

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;

	/**
	 * El código de la institución a la cual pertenece esta persona.
	 */
	private String codigoInstitucion = "";

	/**
	 * <code>ResultSet</code> con los resultados de la consulta de Admision
	 * Hospitalaria
	 */
	private ResultSetDecorator rs = null;

	/**
	 * Código del tipo de identificación del paciente que desea buscar
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	private String tipoIdBusqueda;
	
	/**
	 * Número de identificación del paciente que desea buscar
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	private String numeroIdBusqueda;

	/**
	 * En este Buffer vamos guardando los resultados a "imprimir" en el JSP.
	 */
	private StringBuffer output = null;

	/**
	 * Con esta variable se dice que tipo de admisiones vamos a mostrar
	 * urgencias u hospitalarias
	 */
	private String modo="";
	
	/**
	 * Si el tipo de persona es diferente de nulo y no es vacío, evaluamos el
	 * cuerpo de este tag. En caso contrario, no lo evaluamos.
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspTagException 
	{
		if ( tipoIdBusqueda!=null&&numeroIdBusqueda!=null &&modo!=null&&!tipoIdBusqueda.equals("")&&!numeroIdBusqueda.equals("") &&!modo.equals("")) 
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
				throw new JspTagException("Error Escribiendo El Tag Muestra Admisiones  : "+e.getMessage());
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

			throw new JspTagException("Error Recorriendo El Tag Muestra Admisiones : "+e.getMessage());
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

				throw new JspTagException("Error Escribiendo El TagMuestraAdmisiones: "+e.getMessage());
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
		codigoInstitucion = "";
		tipoIdBusqueda="";
		numeroIdBusqueda="";
		modo="";
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
			this.rs = (DaoFactory.getDaoFactory((String) pageContext.getServletContext().getInitParameter("TIPOBD"))).getTagDao().consultaTagMuestraAdmisiones(con, numeroIdBusqueda, tipoIdBusqueda, modo, codigoInstitucion);
		}
		catch (SQLException sqle) 
		{
			throw new JspTagException("Error Obteniendo Los Datos De La Base De Datos En El Tag Muestra Admisiones Hospitalizacion : "+sqle.getMessage());
		}

	}

	/**
	 * Este método pone en pageContext los valores de las <i>scripting
	 * variables</i>, para cada iteración del tag.
	 */
	private void recorrerResultSet() throws JspTagException 
	{

		String fechaIngreso="", fechaEgreso="", horaIngreso="", horaEgreso="", viaIngreso="", estado="", codigo="", codigoCuenta="";
		
		try 
		{
			if (modo.equals("urgencias"))
			{
				fechaIngreso=rs.getString("fechaAdmision");
				fechaEgreso=rs.getString("fechaEgreso");
				horaIngreso=rs.getString("horaAdmision");
				horaEgreso=rs.getString("horaEgreso");
				codigoCuenta=rs.getString("codigoCuenta");
				
				viaIngreso="Urgencias";
				int estadoCuenta=rs.getInt("estadoCuenta");
				if (estadoCuenta==0)
				{
					estado="Abierta";
				}
				else
				{
					estado="Cerrada";
				}
				
				codigo=rs.getString("codigo") + "-" + rs.getString("anio");
			}
			else if (modo.equals("hospitalarias"))
			{
				fechaIngreso=rs.getString("fechaAdmision");
				fechaEgreso=rs.getString("fechaEgreso");
				horaIngreso=rs.getString("horaAdmision");
				horaEgreso=rs.getString("horaEgreso");
				codigoCuenta=rs.getString("codigoCuenta");
				
				viaIngreso="Hospitalizacion";
				estado=rs.getString("estadoAdmision");
				codigo=rs.getString("codigo");
			}
			else if (modo.equals("general"))
			{
				//Primero los datos comunes tanto a admision hospitalaria como de urgencias
				fechaIngreso=rs.getString("fechaAdmision");
				fechaEgreso=rs.getString("fechaEgreso");
				horaIngreso=rs.getString("horaAdmision");
				horaEgreso=rs.getString("horaEgreso");
				codigoCuenta=rs.getString("codigoCuenta");

				int estadoCuenta=rs.getInt("estadoCuenta");
				//Se que es una admision de hospitalaizacion si el estado de la cuenta es -28
				if (estadoCuenta==-28)
				{
					//Admision Hospitalaria
					viaIngreso="Hospitalizacion";
					estado=rs.getString("estadoAdmision");
					codigo=rs.getString("codigo");
				}
				else
				{
					//admision Urgencias
					viaIngreso="Urgencias";
					if (estadoCuenta==0)
					{
						estado="Abierta";
					}
					else
					{
						estado="Cerrada";
					}
				
					codigo=rs.getString("codigo") + "-" + rs.getString("anio");
				}

			}
			else
			{
				throw new JspTagException("Tipo de admision invalido");
			}
		}
		catch (SQLException sqle) 
		{
			throw new JspTagException("Error Poniendo Los Datos En PageContext : " + sqle.getMessage());
		}

	pageContext.setAttribute("fechaIngreso",  fechaIngreso);

	if (fechaEgreso==null)
	{
		pageContext.setAttribute("fechaEgreso",  " No ha terminado esta Admision");
	}
	else
	{
		pageContext.setAttribute("fechaEgreso",  fechaEgreso);
	}

	pageContext.setAttribute("horaIngreso",  horaIngreso);

	if (horaEgreso==null)
	{
		pageContext.setAttribute("horaEgreso",  "");
	}
	else
	{
		pageContext.setAttribute("horaEgreso",  horaEgreso);
	}

	pageContext.setAttribute("viaIngreso",  viaIngreso);
	pageContext.setAttribute("estado",  estado);
	pageContext.setAttribute("codigo",  codigo);
	pageContext.setAttribute("codigoCuenta",  codigoCuenta);

	}

	/**
	 * Returns the codigoInstitucion.
	 * @return String
	 */
	public String getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * Returns the con.
	 * @return Connection
	 */
	public Connection getCon() {
		return con;
	}

	/**
	 * Returns the numeroIdBusqueda.
	 * @return String
	 */
	public String getNumeroIdBusqueda() {
		return numeroIdBusqueda;
	}

	/**
	 * Returns the tipoIdBusqueda.
	 * @return String
	 */
	public String getTipoIdBusqueda() {
		return tipoIdBusqueda;
	}

	/**
	 * Sets the codigoInstitucion.
	 * @param codigoInstitucion The codigoInstitucion to set
	 */
	public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * Sets the con.
	 * @param con The con to set
	 */
	public void setCon(Connection con) {
		this.con = con;
	}

	/**
	 * Sets the numeroIdBusqueda.
	 * @param numeroIdBusqueda The numeroIdBusqueda to set
	 */
	public void setNumeroIdBusqueda(String numeroIdBusqueda) {
		this.numeroIdBusqueda = numeroIdBusqueda;
	}

	/**
	 * Sets the tipoIdBusqueda.
	 * @param tipoIdBusqueda The tipoIdBusqueda to set
	 */
	public void setTipoIdBusqueda(String tipoIdBusqueda) {
		this.tipoIdBusqueda = tipoIdBusqueda;
	}

	/**
	 * Returns the modo.
	 * @return String
	 */
	public String getModo() {
		return modo;
	}

	/**
	 * Sets the modo.
	 * @param modo The modo to set
	 */
	public void setModo(String modo) {
		this.modo = modo;
	}

}