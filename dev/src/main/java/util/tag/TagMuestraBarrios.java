/*
 * @(#)TagMuestraBarrios.java
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
 * y seleccionar el barrio, dados los codigos de departamento
 * y ciudad.
 *
 * @version 1.0, Sep 28, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class TagMuestraBarrios extends TagSupport
{

	/**
	 * Código de la ciudad en donde está ubicado el barrio.
	 */
	private String codCiudad;

	/**
	 * Código del departamento en donde está ubicado el barrio.
	 */
	private String codDepartamento;
	
	/**
	 * <code>nombre</code> parámetro de búsqueda del nombre de barrio
	 */
	private String nombre;

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;

	
	
	public void clean(){
		this.nombre="";
		this.codCiudad="";
		this.codDepartamento="";
	}
	/**
	 * Este es el metodo que hay que sobreescribir en cualquier
	 * clase que extienda <code>TagSupport</code>(Custom Tags de
	 * JSP). Este metodo recibe a través del <code>jsp:param</code>
	 * el codigo de la ciudad y el codigo del departamento, con
	 * estos busco los codigos y nombres de los barrios existentes
	 * en estas ciudades
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
			int contador=0;
			rs=tagDao.consultaTagMuestraBarrios (con, codDepartamento, codCiudad, nombre);
			while (rs.next())
			{
				s = "<option value=\"" + rs.getString("codigoBarrio")  +"-" + rs.getString("barrio") +  "\">" + Encoder.encode(rs.getString("barrio")) + "</option>";
				pageContext.getOut().print(s);
				contador++;
			}
			s="</select><input type=\"hidden\" id=\"contador\" value=\""+contador+"\">";
			pageContext.getOut().print(s);
		}
	catch (java.io.IOException e)
		{
		throw new JspTagException("TagMuestraBarrios: "+e.getMessage());
		}
	catch (java.sql.SQLException e)
		{
		throw new JspTagException("TagMuestraBarrios: "+e.getMessage());
		}
	catch (Exception e)
		{
		throw new JspTagException("TagMuestraBarrios: "+e.getMessage());
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
	 * Metodo "Get" que retorna el codigo de la ciudad
	 * @return el codigo de la ciudad
	 */
	public String getCodCiudad ()
	{
		return this.codCiudad;
	}

	/**
	 * Metodo "Get" que retorna el codigo del departamento
	 * @return el codigo del departamento
	 */
	public String getCodDepartamento ()
	{
		return this.codDepartamento;
	}

	/**
	 * Metodo "Set" que asigna el codigo del departamento
	 * @param codCiudad codigo de la ciudad
	 */
	public void setCodCiudad (String codCiudad)
	{
		this.codCiudad=codCiudad;
	}

	/**
	 * Metodo "Set" que asigna el codigo del departamento
	 * @param codDepartamento codigo del departamento
	 */
	public void setCodDepartamento (String codDepartamento)
	{
		this.codDepartamento=codDepartamento;
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
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}