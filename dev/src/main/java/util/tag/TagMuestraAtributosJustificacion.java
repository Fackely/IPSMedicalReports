package util.tag;
import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;
/*
 * Creado el 29/04/2005
 * Juan David Ramírez López
 */

/**
 * @author Juan David Ramírez
 * 
 * CopyRight Princeton S.A.
 * 29/04/2005
 */
public class TagMuestraAtributosJustificacion extends BodyTagSupport
{
	/**
	 * Conexión con la BD
	 */
	private Connection con;
	/**
	 * Booleano que sirve para filtrar los servicios de los articulos
	 * true --> muestra articulos
	 * false --> muestra servicios
	 */
	private boolean mostrarAtributosArticulo;
	/**
	 * Institucion para hacer el filtrado
	 */
	private int institucion;
	/**
	 * @return Retorna con.
	 */
	public Connection getCon()
	{
		return con;
	}
	/**
	 * @param con Asigna con.
	 */
	public void setCon(Connection con)
	{
		this.con = con;
	}
	public int doStartTag() throws JspTagException
	{
		funcionalidad();
		return EVAL_BODY_BUFFERED;
	}

	/**
	 * Funcionalida del tag
	 */
	private void funcionalidad()
	{
		TagDao tagDao;

		ServletContext sc=pageContext.getServletContext();
		String tipoBD=(String)sc.getInitParameter("TIPOBD");

		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		tagDao = myFactory.getTagDao();
		
		Collection col=tagDao.consultaTagMuestraAtributos(con,mostrarAtributosArticulo, institucion);
		Iterator iterador=col.iterator();
		Vector resultados=new Vector();
		while(iterador.hasNext())
		{
			HashMap bean=(HashMap)iterador.next();
			Vector fila=new Vector();
			fila.add(bean.get("codigo"));
			fila.add(bean.get("nombre"));
			fila.add(bean.get("esrequerido"));
			fila.add(bean.get("validarautorizacion"));
			resultados.add(fila);
		}
		pageContext.setAttribute("resultados" ,  resultados);

	}
	
	public int doEndTag() throws JspTagException
	{
		BodyContent bodyContent = getBodyContent();
		if (bodyContent != null)
		{
			try
			{
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}
			catch (Exception e)
			{
					e.printStackTrace();
					throw new JspTagException("Error escribiendo Tag Muestra Atributos Justificacion Struts : " + e);
			}
		}
		clean();
		return EVAL_PAGE;
	}

	public void clean()
	{
		mostrarAtributosArticulo=false;
		institucion=0;
	}
	/**
	 * @return Retorna mostrarAtributosArticulo.
	 */
	public boolean isMostrarAtributosArticulo()
	{
		return mostrarAtributosArticulo;
	}
	/**
	 * @param mostrarAtributosArticulo Asigna mostrarAtributosArticulo.
	 */
	public void setMostrarAtributosArticulo(boolean mostrarAtributosArticulo)
	{
		this.mostrarAtributosArticulo = mostrarAtributosArticulo;
	}
	/**
	 * @return Retorna institucion.
	 */
	public int getInstitucion()
	{
		return institucion;
	}
	/**
	 * @param institucion Asigna institucion.
	 */
	public void setInstitucion(int institucion)
	{
		this.institucion = institucion;
	}
}
