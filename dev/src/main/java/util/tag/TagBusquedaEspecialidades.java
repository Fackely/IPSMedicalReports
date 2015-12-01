/*
 * @(#)TagBusquedaEspecialidades.java
 * 
 * Created on 07-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package util.tag;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

import util.InfoDatosInt;
import util.ResultadoCollectionDB;
import util.UtilidadCadena;

/**
 * Esta clase implementa un tag que permite, dado el nombre , parte del nombre,
 * código o parte del código recuperar de la Base de Datos las especialidades que cumplan 
 * con el criterio dado. Define las <i>scripting variables</i> <b>codigos</b> y 
 * <b>nombres</b>, y se apoya en la clase <code>TEIBusquedaEspecialidades</code>. 
 * 
 * @version 1.0, 07-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class TagBusquedaEspecialidades extends BodyTagSupport
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(TagBusquedaEspecialidades.class);	

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con;

	/**
	 * Criterio de busqueda de la especialidad
	 */
	private String criterioBusqueda;
	
	/**
	 * Tipo de busqueda a realizar : "texto" / "codigo"
	 */
	private String tipoBusqueda;
	
	
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
		if( UtilidadCadena.noEsVacio(this.criterioBusqueda)  &&  UtilidadCadena.noEsVacio(this.tipoBusqueda) ) 
		{
			boolean buscarTexto = false;
			
			if( this.tipoBusqueda.equalsIgnoreCase("texto") )
				buscarTexto = true;
			
			this.ejecutarBusqueda(buscarTexto);			
			
			return EVAL_BODY_BUFFERED;
		}
		else 
		{
			return SKIP_BODY;
		}
	}
	
	private void ejecutarBusqueda(boolean buscarTexto) throws JspTagException 
	{
		TagDao tagDao;

		ServletContext sc = pageContext.getServletContext();
		String tipoBD = (String)sc.getInitParameter("TIPOBD");
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		tagDao = myFactory.getTagDao();
		
		ResultadoCollectionDB resultadoBusqueda = tagDao.consultaTagBusquedaEspecialidades(con, criterioBusqueda, buscarTexto);
		
		if( resultadoBusqueda != null && criterioBusqueda.length()>2)
		{
			if( !resultadoBusqueda.isTrue() )
			{
				logger.warn("El resultado de la busqueda retornó false "+resultadoBusqueda.getDescripcion());
				pageContext.setAttribute("especialidades" , new ArrayList());
			}
			else
			{
				LinkedList listadoEspecialidades = (LinkedList)resultadoBusqueda.getFilasRespuesta();
				int tam = 0;
				
				ArrayList especialidadesList = new ArrayList();		
				
				if( listadoEspecialidades != null && (tam = listadoEspecialidades.size()) > 0 )
				{		
					for( int i=0; i < tam; i++ )
					{
						HashMap especialidad = (HashMap)(listadoEspecialidades).get(i);	
						InfoDatosInt tempEspecialidad = new InfoDatosInt();			
						
						tempEspecialidad.setCodigo(((Integer)especialidad.get("codigoespecialidad")).intValue());
						tempEspecialidad.setNombre((String)especialidad.get("nombreespecialidad"));	
						especialidadesList.add(tempEspecialidad);
					}
				}			
				pageContext.setAttribute("especialidades" , especialidadesList);
			}
		}
		else
		{
			pageContext.setAttribute("especialidades" , new ArrayList());
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
				throw new JspTagException("Error escribiendo TagBusquedaProcedimientosClinicos : " + e.getMessage());
			}
		}
		clean();
		return EVAL_PAGE;
	}

	private void clean()
	{
		this.criterioBusqueda = "";
		this.tipoBusqueda = "";
	}


	
	/**
	 * Retorna el objeto que representa una conexión con una base de datos.
	 * @return
	 */
	public Connection getCon()
	{
		return con;
	}

	/**
	 * Asigna el objeto que representa una conexión con una base de datos.
	 * @param connection
	 */
	public void setCon(Connection connection)
	{
		con = connection;
	}

	/**
	 * Retorna el criterio de busqueda de la especialidad
	 * @return
	 */
	public String getCriterioBusqueda()
	{
		return criterioBusqueda;
	}

	/**
	 * Asigna el criterio de busqueda de la especialidad
	 * @param criterioBusqueda
	 */
	public void setCriterioBusqueda(String criterioBusqueda)
	{
		this.criterioBusqueda = criterioBusqueda;
	}

	/**
	 * Retorna el tipo de busqueda a realizar : "texto" / "codigo"
	 * @return
	 */
	public String getTipoBusqueda()
	{
		return tipoBusqueda;
	}

	/**
	 * Asigna el tipo de busqueda a realizar : "texto" / "codigo"
	 * @param tipoBusqueda
	 */
	public void setTipoBusqueda(String tipoBusqueda)
	{
		this.tipoBusqueda = tipoBusqueda;
	}

}
