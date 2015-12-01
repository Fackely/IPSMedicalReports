/*
 * @(#)TagMuestraOpcionesExFisicosStruts.java
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
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * Muestra todas las selecciones dentro de cada examen fisico que las tenga 
 *
 * @version 1.0, Mar 10, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero </a>
 */

public class TagMuestraOpcionesExFisicosStruts  extends BodyTagSupport
{
	
	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con=null;

	/**
	 * Tipo de valoración : General/Urgencias/Hospitalización....
	 */
	private String codigoEspecialidad = "";
	
	private String codigoEspecialidad2 = "";
	
	private String codigoEspecialidad3 = "";
	
	/**
	 * Al trabajar con Tags siempre tenemos que definir al menos este método.
	 * Siempre se ejecuta antes que doEndTag. Para los Tags hechos anteriormente
	 * se manejaba toda la funcionalidad en este método, ahora lo limitamos
	 * únicamente a validar que los datos que llegan ni sean nulos, ni sean
	 * vacios
	 * @see javax.servlet.jsp.tagext.Tag#doStartTag()
	 */
	public int doStartTag() throws JspTagException 
	{
		if( codigoEspecialidad != null && !codigoEspecialidad.equals("") ) 
		{
			funcionalidad();
			return EVAL_BODY_BUFFERED;
		}
		else 
		{
			return SKIP_BODY;
		}
	}

	/**
	 * Este método escribe en la página el valor y nombre por defecto pedidos
	 * según el parámetro, usando la presentación presente en la página. También
	 * limpia el estado interno de los atributos de este tag.
	 * @see javax.servlet.jsp.tagext.Tag#doEndTag()
	 */
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
				throw new JspTagException("Error escribiendo TagMuestraOpcionesExFisicosStruts : " + e.getMessage());
			}

		}

		clean();
		return EVAL_PAGE;
	}

	/**
	 * Como Tomcat a partir de su versión 4.1.12 ? maneja un pool de Tags,
	 * tenemos que asegurarnos de limpiar los datos que recibimos, si no cuando
	 * Tomcat reutiliza este Tag nos vamos a encontrar con datos viejos /
	 * inconsistentes
	 */

	private void clean() 
	{
		this.codigoEspecialidad = "";
		this.codigoEspecialidad2 = "";
		this.codigoEspecialidad3 = "";
	}

	public void funcionalidad() throws JspTagException 
	{
		try
		{
			TagDao tagDao;
			ServletContext sc = pageContext.getServletContext();
			String tipoBD = (String)sc.getInitParameter("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tagDao = myFactory.getTagDao();
	
			ResultSetDecorator rs = null;
			
			rs=tagDao.consultaTagMuestraOpcionesExFisicosStruts (con, this.codigoEspecialidad, this.codigoEspecialidad2, this.codigoEspecialidad3);
			Vector resultados=new Vector(), opSubCategoria = new Vector();
			String nombreViejo = "";

			boolean loEncontre=false;	
			
			int tmpInt=0;
			while( rs.next() )
			{	tmpInt++;	
				loEncontre=true;
				String nombre = rs.getString("nombre");
									
				String row = rs.getString("codigoExam");
				row += "-"+rs.getString("nombreExam");
				row += "-"+rs.getString("codigo");
				row += "-"+rs.getString("nombre");
				row += "-"+rs.getString("verdadero");
				row += "-"+rs.getString("falso");
					
				if( !nombre.equals(nombreViejo) )
				{
					if( opSubCategoria.size() != 0 )
						resultados.addElement(opSubCategoria);					
	
					opSubCategoria = new Vector();
					opSubCategoria.addElement(row);
				}
				else
				{
					opSubCategoria.addElement(row);
				}
					
				nombreViejo = nombre;
			}
			if(loEncontre)
			{
				resultados.addElement(opSubCategoria);
			}
			
			pageContext.setAttribute("resultados" ,  resultados);			
		}
		catch (Exception e)
		{
			throw new JspTagException("TagMuestraOpcionesExFisicosStruts: "+e.getMessage());
		}	
	}
		

	/**
	 * Metodo "Set" que recibe una conexion para permitir manejar todos los tags
	 * de una misma pagina con la misma conexion
	 * @param con conexion
	 */
	public void setCon (Connection con)	
	{
		this.con=con;
	}
	
	/**
	 * Metodo "Get" que retorna la conexion usada por este tag
	 * @return conexion usada por el tag
	 */
	public Connection getCon ()	
	{
		return con;
	}

	/**
	 * Returns the tipoValoracion.
	 * @return String
	 */
	public String getCodigoEspecialidad() 
	{
		return codigoEspecialidad;
	}

	/**
	 * Sets the tipoValoracion.
	 * @param tipoValoracion The tipoValoracion to set
	 */
	public void setCodigoEspecialidad(String codigoEspecialidad) 
	{
		this.codigoEspecialidad = codigoEspecialidad;
	}

	/**
	 * Returns the codigoEspecialidad2.
	 * @return String
	 */
	public String getCodigoEspecialidad2() 
	{
		return codigoEspecialidad2;
	}

	/**
	 * Sets the codigoEspecialidad2.
	 * @param codigoEspecialidad2 The codigoEspecialidad2 to set
	 */
	public void setCodigoEspecialidad2(String codigoEspecialidad2) 
	{
		this.codigoEspecialidad2 = codigoEspecialidad2;
	}

	/**
	 * Returns the codigoEspecialidad3.
	 * @return String
	 */
	public String getCodigoEspecialidad3() 
	{
		return codigoEspecialidad3;
	}

	/**
	 * Sets the codigoEspecialidad3.
	 * @param codigoEspecialidad3 The codigoEspecialidad3 to set
	 */
	public void setCodigoEspecialidad3(String codigoEspecialidad3) 
	{
		this.codigoEspecialidad3 = codigoEspecialidad3;
	}

}