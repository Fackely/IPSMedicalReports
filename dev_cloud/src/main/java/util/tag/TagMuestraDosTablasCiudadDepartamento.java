/*
 * @(#)TagMuestraDosTablasCiudadDepartamento.java
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
 * Esta clase maneja la funcionalidad del tag que permite ver
 * y seleccionar la ciudad y el departamento en una forma html,
 * en un mismo select.
 *
 * @version 1.0, Sep 28, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class TagMuestraDosTablasCiudadDepartamento extends TagSupport
{
	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con=null;
	
	/**
	 * Restriccion para mostrar unicamente la ciudad deseada
	 */
	private String restriccion="";
	
	/**
	 * Restriccion para mostrar todas excepto la ciudad de la restricción
	 */
	private String restriccionDiferente="false";
	/**
	 * Este es el metodo que hay que sobreescribir en cualquier
	 * clase que extienda <code>TagSupport</code>(Custom Tags de
	 * JSP). En este caso el metodo imprime todas las posibles
	 * combinaciones de ciudades departamento, separando el codigo
	 * de la ciudad, el codigo del departamento y el nombre
	 * combinado de ciudad/departamento a traves de guiones "-"
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

		String s;

		rs  = tagDao.consultaTagMuestraDosTablasCiudadDepartamento (con);

		while (rs.next())
		{
			if(restriccion==null||restriccion.equals(""))
			{
				s = "<option value=\"" + rs.getString("codigo_departamento") + "-" + rs.getString("codigo_ciudad") + "-" + rs.getString("ciudad") + " (" +  rs.getString("departamento") + ")" + "\">" + Encoder.encode(rs.getString("ciudad")) + " (" +  Encoder.encode(rs.getString("departamento")) + ")</option>";
				pageContext.getOut().print(s);
			}
			else if(restriccionDiferente.equals("true"))
			{
				if(!restriccion.equals(rs.getString("codigo_departamento") + "-" + rs.getString("codigo_ciudad") + "-" + rs.getString("ciudad")))
				{
					s = "<option value=\"" + rs.getString("codigo_departamento") + "-" + rs.getString("codigo_ciudad") + "-" + rs.getString("ciudad") + " (" +  rs.getString("departamento") + ")" + "\">" + Encoder.encode(rs.getString("ciudad")) + " (" +  Encoder.encode(rs.getString("departamento")) + ")</option>";
					pageContext.getOut().print(s);
				}
			}
			else
			{
				if(restriccion.equals(rs.getString("codigo_departamento") + "-" + rs.getString("codigo_ciudad") + "-" + rs.getString("ciudad")))
				{
					s = "<option value=\"" + rs.getString("codigo_departamento") + "-" + rs.getString("codigo_ciudad") + "-" + rs.getString("ciudad") + " (" +  rs.getString("departamento") + ")" + "\">" + Encoder.encode(rs.getString("ciudad")) + " (" +  Encoder.encode(rs.getString("departamento")) + ")</option>";					
					pageContext.getOut().print(s);
				}
			}
		}
		}
	catch (java.io.IOException e)
		{
		throw new JspTagException("TagMuestraDosTablasCiudadDepartamento: "+e.getMessage());
		}
	catch (java.sql.SQLException e)
		{
		throw new JspTagException("TagMuestraDosTablasCiudadDepartamento: "+e.getMessage());
		}
	catch (Exception e)
		{
		throw new JspTagException("TagMuestraDosTablasCiudadDepartamento: "+e.getMessage());
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
	 * @return Retorna restriccion.
	 */
	public String getRestriccion()
	{
		return restriccion;
	}
	/**
	 * @param restriccion Asigna restriccion.
	 */
	public void setRestriccion(String restriccion)
	{
		this.restriccion = restriccion;
	}
	/**
	 * @return Retorna restriccionDiferente.
	 */
	public String getRestriccionDiferente()
	{
		return restriccionDiferente;
	}
	/**
	 * @param restriccionDiferente Asigna restriccionDiferente.
	 */
	public void setRestriccionDiferente(String restriccionDiferente)
	{
		this.restriccionDiferente = restriccionDiferente;
	}
}