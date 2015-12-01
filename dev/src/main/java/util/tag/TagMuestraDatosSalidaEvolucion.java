/*
 * @(#)TagMuestraDatosSalidaEvolucion.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
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

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * Tag que muestra los datos de orden de salida, dado un código 
 * de evolución
 *
 * @version 1.0, Jun 05, 2003
 */

public class TagMuestraDatosSalidaEvolucion extends BodyTagSupport 
{

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;

	/**
	 * Código de la evolución a la que se desea buscar su egreso
	 */
	private int idEvolucion=0;

	/**
	 * Si el nombre del parámetro es diferente de nulo y no es vacío, evaluamos
	 * el cuerpo de este tag. En caso contrario, no lo evaluamos.
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspTagException {
		if (idEvolucion>0) 
		{
			getData();
			return EVAL_BODY_BUFFERED;
		}
		else {
			return SKIP_BODY;
		}
	}

	/**
	 * Este método escribe en la página los datos de la orden de salida para esta
	 * evolución si existe, si no uno de los atributos lo indica. También
	 * limpia el estado interno de los atributos de este tag.
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
	public int doEndTag() throws JspTagException {

		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null) {

			try {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}	catch (Exception e) {
					throw new JspTagException("Error Escribiendo El Tag Muestra Datos Salida Dado Evolucion: " + e.getMessage());
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
	private void clean() 
	{
		this.idEvolucion = 0;
	}

	/**
	 * Este método obtiene de la BD los datos de "valor" y "nombre" asociados al
	 * "parametro" pasado a este tag, y los pone en el contexto de la página
	 * como <i>scripting variables</i>.
	 */
	private void getData() throws JspTagException 
	{
		String estadoSalida = "", acronimoMuerte = "", diagnosticoMuerte="", tipoCIEMuerte="", destinoSalida="", tieneEgreso="";
		TagDao tagDao = (DaoFactory.getDaoFactory((String) pageContext.getServletContext().getInitParameter("TIPOBD"))).getTagDao();

		try {
			ResultSetDecorator rs=tagDao.consultaTagMuestaDatosSalidaEvolucion (con, idEvolucion);

			if (rs.next())	
			{
				if (rs.getBoolean("estadoSalida"))
				{
					estadoSalida  = "true";
				}
				else
				{
					estadoSalida  = "false";
				}
				
				acronimoMuerte=rs.getString("acronimoMuerte");
				diagnosticoMuerte=rs.getString("diagnosticoMuerte");
				tipoCIEMuerte=rs.getString("tipoCIEMuerte");
				if (rs.getInt("codigoDestinoSalida")==0)
				{
					//Seleccionó la opción otros
					destinoSalida=rs.getString("otroDestinoSalida");
				}
				else
				{
					destinoSalida=rs.getString("destinoSalida");
				}
				tieneEgreso="true";
			}
			else 
			{
				tieneEgreso="false";
			}

		}	
		catch (SQLException sqle) 
		{
				throw new JspTagException("Error Obteniendo Los Datos De La Base De Datos En El Tag Muestra Datos Salida Evolucion : " + sqle.getMessage());
		}

		pageContext.setAttribute("tieneEgresoTag" , tieneEgreso );
		pageContext.setAttribute("estadoSalidaTag" ,  estadoSalida);
		pageContext.setAttribute("acronimoMuerteTag" , acronimoMuerte );
		pageContext.setAttribute("tipoCIEMuerteTag" ,  tipoCIEMuerte);
		pageContext.setAttribute("diagnosticoMuerteTag" , diagnosticoMuerte );
		pageContext.setAttribute("destinoSalidaTag" ,  destinoSalida);

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
	public int getIdEvolucion() {
		return idEvolucion;
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
	public void setIdEvolucion(int i) {
		idEvolucion = i;
	}

}