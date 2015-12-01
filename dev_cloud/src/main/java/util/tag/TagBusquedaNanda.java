/*
 * @(#)TagMuestraOpcionesStruts.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util.tag;

import java.sql.Connection;
import java.util.Vector;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;
import org.apache.log4j.Logger;

/**
 * Esta clase es un Tag que permite separa un poco más la funcionalidad
 * de la presentación en una pagina JPS. Una de las operaciones más
 * frecuentes en los JSP's usados es la generacion dinamica de select's
 * donde el usuario ve la informacion y el select ve el codigo usado en
 * la Base de datos para almacenar este valor. La funcionalidad de este
 * tag es hacer todo este proceso, sin necesidad de conectar a la base
 * de datos o escribir consultas en SQL. La principal diferencia con el
 * TagMuestraOpciones es que trabaja con variables TEI que permiten definir
 * variables que son manejadas como tales en el JSP
 *
 * @version 1.0, Mar 7, 2003
 * @author <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s
 * &oacute;pez P.</a>
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 * P.</a>,
 * @author <a href="mailto:Diego@PrincetonSA.com">Diego Ram&iacute;rez </a>
 */

public class TagBusquedaNanda extends BodyTagSupport
{
	/**
	 * <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Manejador de logs
	 */
	private Logger logger = Logger.getLogger(TagBusquedaNanda.class);

	/**
	 * Criterio de búsqueda
	 */
	private String criterio;
	
	/**
	 * Conexión con la BD
	 */
	private Connection con;
	
	/**
	 * Indica si la busqueda se hace por codigo o no
	 */
	private boolean esPorCodigo;

	/**
	 * Institución del usuario 
	 */
	private int institucion;
	
	/**
	 * Filtrado de los codigos por institución
	 */
	private String codigosNoBuscados;

	/**
	 * Este es el metodo que hay que sobreescribir en cualquier
	 * clase que extienda <code>TagSupport</code> (Custom Tags de
	 * JSP).
	 */
	public int doStartTag()
	{
		return funcionalidad();
	}

	private int funcionalidad()
	{
		try
		{
			TagDao tagDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTagDao();
			Vector resultados=tagDao.buscarNanda(con, esPorCodigo, criterio, institucion, codigosNoBuscados);
			pageContext.setAttribute("resultados" ,  resultados);
		}
		catch (Exception e)
		{
			logger.error(e.getMessage());
			e.printStackTrace();
			return SKIP_BODY;
		}
		return EVAL_BODY_BUFFERED;
	}

	/**
	 * Finalización del Tag
	 */
	public int doEndTag() throws JspTagException {

		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null) {

			try {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}	catch (Exception e) {
					e.printStackTrace();
					throw new JspTagException("Error escribiendo Tag Muestra Opciones Struts : " + e.getMessage()+e.toString());
			}
		}

		clean();
		return EVAL_PAGE;
	}

	private void clean()
	{
	}

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

	/**
	 * @return Retorna criterio.
	 */
	public String getCriterio()
	{
		return criterio;
	}

	/**
	 * @param criterio Asigna criterio.
	 */
	public void setCriterio(String criterio)
	{
		this.criterio = criterio;
	}

	/**
	 * @return Retorna esPorCodigo.
	 */
	public boolean getEsPorCodigo()
	{
		return esPorCodigo;
	}

	/**
	 * @param esPorCodigo Asigna esPorCodigo.
	 */
	public void setEsPorCodigo(boolean esPorCodigo)
	{
		this.esPorCodigo = esPorCodigo;
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

	/**
	 * @return Retorna codigosNoBuscados.
	 */
	public String getCodigosNoBuscados()
	{
		return codigosNoBuscados;
	}

	/**
	 * @param codigosNoBuscados Asigna codigosNoBuscados.
	 */
	public void setCodigosNoBuscados(String noBuscarCodigos)
	{
		this.codigosNoBuscados = noBuscarCodigos;
	}

}