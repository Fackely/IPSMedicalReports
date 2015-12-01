/*
 * @(#)TagMuestraTipoAfiliado.java
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

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import util.Encoder;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * Esta clase maneja la funcionalidad del tag que permite 
 * mostrar todos los tipos de afiliado permitidos en un
 * determinado tipo de Régimen
 *
 * @version 1.0, Dec 9, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class TagMuestraTipoAfiliado extends TagSupport{

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;
	
	/**
	 * Código del tipo de régimen
	 */
	private String codigoTipoRegimen=null;

	/**
	 * Este es el metodo que hay que sobreescribir en cualquier
	 * clase que extienda <code>TagSupport</code>(Custom Tags de
	 * JSP). Este metodo recibe a través del <code>jsp:param</code>
	 * el codigo del tipo de Règimen y solo los tipos de afiliado
	 * permitidos para este Régimen (No es información de la BD,
	 * es local)
	 * Si solo hay un estrato para este tipo de regimen, imprime un
	 * hidden.
	 * @return la constante SKIP_BODY
	 */
	public int doStartTag() throws JspException
	{
	try
		{
			TagDao tagDao;

			ServletContext sc=pageContext.getServletContext();
			String tipoBD=(String)sc.getInitParameter("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tagDao = myFactory.getTagDao();

			ResultSetDecorator rs = null;

			//Primero preguntamos por los casos en los que no se muestra
			//nada

			//En caso que la persona pertenezca al regimen Subsidiado o
			//Vinculado, automáticamente se pone como beneficiario
			
			//Aunque estos códigos están cableados, si en algún momento
			//estos se cambian, el tag sigue funcionando, obviamente sin
			//las preferencias especificadas (muestra todas las opciones)
			
			if (codigoTipoRegimen.equals("S")||codigoTipoRegimen.equals("V"))
			{
				pageContext.getOut().print("<INPUT TYPE=\"HIDDEN\" NAME=\"tipoAfiliado\" VALUE=\"B-Beneficiario\">");
				return SKIP_BODY;
			}
			//Si el tipo de regimen es particular u otros, automáticamente
			//se pone como cotizante
			else if (codigoTipoRegimen.equals("P")||codigoTipoRegimen.equals("O"))
			{
				pageContext.getOut().print("<INPUT TYPE=\"HIDDEN\" NAME=\"tipoAfiliado\" VALUE=\"C-Cotizante\">");
				return SKIP_BODY;
			}

			rs=tagDao.consultaTagMuestraTipoAfiliado (con) ;

		pageContext.getOut().print("<tr><td>Tipo de Afiliado</td><td><select name=\"tipoAfiliado\"><option value=\"-1\">Seleccione la afiliación</option>");

		//Con este while vamos a guardar en el vector todos los estratos disponibles
		while (rs.next())
		{
			pageContext.getOut().print("<option value=\"" + rs.getString("acronimo") + "-"+ rs.getString("nombre") + "\">" + Encoder.encode(rs.getString("nombre")) + "</option>");
		}

		pageContext.getOut().print("</select></td></tr>");
	}
	catch (java.io.IOException e)
		{
		throw new JspTagException("TagMuestraTipoAfiliado: "+e.getMessage());
		}
	catch (java.sql.SQLException e)
		{
		throw new JspTagException("TagMuestraTipoAfiliado: "+e.getMessage());
		}
	catch (Exception e)
		{
		throw new JspTagException("TagMuestraTipoAfiliado: "+e.getMessage());
		}

	return SKIP_BODY;
	}

	/**
	 * Metodo necesario al extender la clase <code>TagSupport</code>
	 * en este caso no se usa para nada más
	 * @return la constante EVAL_PAGE
	 */
	public int doEndTag() {
		return EVAL_PAGE;
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
	 * Retorna el codigo del estrato.
	 * @return String con el codigo del estrato
	 */
	public String getCodigoTipoRegimen() {
		return codigoTipoRegimen;
	}

	/**
	 * Establece el codigo del estrato.
	 * @param codigoTipoRegimen El codigo del estrato a establecer
	 */
	public void setCodigoTipoRegimen(String codigoEstrato) {
		this.codigoTipoRegimen = codigoEstrato;
	}

}