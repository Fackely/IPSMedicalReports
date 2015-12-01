/*
 * @(#)TagMuestraPosiblesInterconsultas.java
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

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * Esta clase maneja laa funcionalidad del tag que permite
 * seleccionar los centros de costo a los cuales se les puede
 * solicitar una interConsulta.
 * Todos menos:
 * 	-Hospitalización
 * 	-Urgencias
 * 	-Consulta Externa
 * 	-Todos aquellos en los que el paciente, en la cuenta abierta
 * tenga solicitudes sin contestar. 
 *
 * @version 1.0, May 23, 2003
 */

/**
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TagMuestraPosiblesInterconsultas  extends BodyTagSupport
{
	
	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con=null;

	/**
	 * Código de la institución.
	 */
	private String codigoInstitucion="";

	/**
	 * Código de la cuenta a la que se le quiere hacer una solicitud
	 */
	private int codigoCuenta=0;	
	
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
		//El único parámetro que no revisamos es el login pues no siempre es necesario
		if (codigoInstitucion!= null && !codigoInstitucion.trim().equals("")&&codigoCuenta>0) 
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
					throw new JspTagException("Error escribiendo TagMuestraPosiblesInterconsultas : " + e.getMessage());
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
		codigoInstitucion = "";
		codigoCuenta=0;
	}

	public void funcionalidad() throws JspTagException 
	{
		try
		{
			TagDao tagDao;

			String tipoBD=(String)System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tagDao = myFactory.getTagDao();
	
			ResultSetDecorator rs = null;
	
			rs=tagDao.consultaTagMuestraPosiblesInterconsultas (con, codigoCuenta);

			Vector resultados=new Vector (10,5);
			while (rs.next())
			{
				String arregloResultados[]=new String[2];
				arregloResultados[0]=rs.getString("codigo");
				arregloResultados[1]=rs.getString("nombre");
				resultados.add(arregloResultados);
			}
			pageContext.setAttribute("resultados" ,  resultados);
		}
		catch (java.sql.SQLException e)
			{
			throw new JspTagException("TagMuestraPosiblesInterconsultas: "+e.getMessage());
			}
		catch (Exception e)
			{
			throw new JspTagException("TagMuestraPosiblesInterconsultas: "+e.getMessage());
			}
		
	}
		

	/**
	 * Metodo "Set" que recibe una conexion
	 * para permitir manejar todos los tags
	 * de una misma pagina con la misma
	 * conexion
	 * @param con conexion
	 */
	public void setCon (Connection con)	{
		this.con=con;
	}
	
	/**
	 * Metodo "Get" que retorna la conexion
	 * usada por este tag
	 * @return conexion usada por el tag
	 */
	public Connection getCon ()	{
		return con;
	}

	/**
	 * Establece el codigo de la institucion.
	 * @param codigoInstitucion El codigo de la institucion a establecer
	 */
	public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * @param i
	 */
	public void setCodigoCuenta(int i) {
		codigoCuenta = i;
	}

}