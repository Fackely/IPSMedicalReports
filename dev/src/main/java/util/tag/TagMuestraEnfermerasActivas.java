/*
 * @(#)TagMuestraEnfermerasActiva.java
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
import org.apache.log4j.Logger;

import util.ConstantesBD;

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

public class TagMuestraEnfermerasActivas extends BodyTagSupport
{
	private Logger logger = Logger.getLogger(TagMuestraEnfermerasActivas.class);

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con=null;
	
	private String restriccion;
	
	private String codigoInstitucion;
	
   
	private String separador;
	
    /**
     * @return Returns the separador.
     */
    public String getSeparador() {
        return separador;
    }
    /**
     * @param separador The separador to set.
     */
    public void setSeparador(String separador) {
        this.separador = separador;
    }
    /**
     * @return Returns the con.
     */
    public Connection getCon() {
        return con;
    }
    /**
     * @param con The con to set.
     */
    public void setCon(Connection con) {
        this.con = con;
    }
    

    
    
    
	/**
	 * Este es el metodo que hay que sobreescribir en cualquier
	 * clase que extienda <code>TagSupport</code> (Custom Tags de
	 * JSP).
	 */
	public int doStartTag() throws JspTagException {
			funcionalidad();
			return EVAL_BODY_BUFFERED;
	}

	public int doEndTag() throws JspTagException {

		BodyContent bodyContent = getBodyContent();

		if (bodyContent != null) {

			try {
				bodyContent.writeOut(bodyContent.getEnclosingWriter());
			}	catch (Exception e) {
					e.printStackTrace();
					throw new JspTagException("Error escribiendo Tag Muestra Enfermeras Activas : " + e.getMessage()+e.toString());
			}
		}

		clean();
		return EVAL_PAGE;
	}

	private void clean()
	{
	}

	private void funcionalidad() throws JspTagException
	{

	  try
	    {
	        TagDao tagDao;

			ServletContext sc=pageContext.getServletContext();
			String tipoBD=(String)sc.getInitParameter("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tagDao = myFactory.getTagDao();
	        
			Vector resultados=new Vector();
			ResultSetDecorator rs;
			
			String res="";
			separador=ConstantesBD.separadorTags;
			   
			rs=tagDao.consultaTagMuestraEnfermerasActivas(con, restriccion, codigoInstitucion);
			
			while (rs.next())
			{
			    res=rs.getString("nombres") + this.separador +rs.getString("codigo_medico");
			    resultados.add(res);
			}
	        
	        pageContext.setAttribute("resultados" ,  resultados);

		}
		catch (Exception e)
		{
			logger.warn(e.getMessage());
			e.printStackTrace();
		throw new JspTagException("TagMuestraEnfermerasActivas: "+e.getMessage()+e.toString());
		}

	}
    /**
     * @return Returns the restriccion.
     */
    public String getRestriccion() {
        return restriccion;
    }
    /**
     * @param restriccion The restriccion to set.
     */
    public void setRestriccion(String restriccion) {
        this.restriccion = restriccion;
    }
    /**
     * @return Returns the codigoInstitucion.
     */
    public String getCodigoInstitucion() {
        return codigoInstitucion;
    }
    /**
     * @param codigoInstitucion The codigoInstitucion to set.
     */
    public void setCodigoInstitucion(String codigoInstitucion) {
        this.codigoInstitucion = codigoInstitucion;
    }
}