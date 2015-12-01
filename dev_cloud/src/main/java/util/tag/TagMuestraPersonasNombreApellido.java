/*
 * @(#)TagMuestraPersonasNombreApellido.java
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
 * seleccionar cualquier persona (acompañanantes, pacientes,
 * usuarios y médicos) viendo sus nombres y apellidos,
 * en la busqueda para modificarlos o eliminarlos. 
 *
 * @version 1.0, Nov 27, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class TagMuestraPersonasNombreApellido extends TagSupport
{
	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con=null;

	/**
	 * String en donde viene con quien estoy trabajando
	 * (puede ser para modificar , buscar un médico, un
	 * usuario, un acompañante o un paciente).
	 */
	private String tipoPersonaBuscada=null;

	/**
	 * Código de la institución.
	 */
	private String codigoInstitucion="";
	
	/**
	 * Código del centro de costo
	 */
	private String codigoCentroCosto = "";

	/**
	 * 
	 */
	private String mostrarSoloActivos="";
	
	
	/**
	 * 
	 */
	private String mostrarSoloInactivos="";
	   
	   
	   
	/**
	 * Este es el metodo que hay que sobreescribir en cualquier
	 * clase que extienda <code>TagSupport</code>(Custom Tags de
	 * JSP). Este tag imprime en un campo select de una forma
	 * html todas los posibles pacientes. Dentro del value del
	 * select incluye todas los codigos necesarios.
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

		//ResultSetDecorator rs = null;

		String s = tagDao.consultaTagMuestraPersonasNombreApellido (con, tipoPersonaBuscada, codigoInstitucion,codigoCentroCosto,mostrarSoloActivos,mostrarSoloInactivos);
		pageContext.getOut().print(s);
		/*rs=tagDao.consultaTagMuestraPersonasNombreApellidoStruts (con, tipoPersonaBuscada, codigoInstitucion,codigoCentroCosto,mostrarSoloActivos,mostrarSoloInactivos);
		while (rs.next())
		{
			 if ( tipoPersonaBuscada.equals("MostrarUsuarios")|| tipoPersonaBuscada.equals("MostrarUsuariosActivacion")|| tipoPersonaBuscada.equals("MostrarUsuariosDesactivacion")||tipoPersonaBuscada.equals("MostrarUsuariosInactivos"))
				s="<option value=\"" + rs.getString("login")  +  "\">" + rs.getString("primer_apellido") + " " + Encoder.encode(rs.getString("primer_nombre") +  "(" + rs.getString("login")) + ")</option>";
			 else
				//s = "<option value=\"" + rs.getString("tipo_identificacion") + "-" + rs.getString("numero_identificacion")  +  "\">" + Encoder.encode(rs.getString("primer_apellido") + " " + rs.getString("segundo_apellido") + ", " + rs.getString("primer_nombre") + " " + rs.getString("segundo_nombre") ) + "</option>";
				s = "<option value=\"" + rs.getString("codigo") +  "\">" + Encoder.encode(rs.getString("primer_apellido") + " " + (rs.getString("segundo_apellido")==null ?"":rs.getString("segundo_apellido")) + ", " + rs.getString("primer_nombre") + " " + (rs.getString("segundo_nombre")==null ?"":rs.getString("segundo_nombre")) ) + "</option>";
		
			pageContext.getOut().print(s);
		}*/
		}
	catch (java.io.IOException e)
		{
		throw new JspTagException("TagMuestraPersonasNombreApellido: "+e.getMessage());
		}
	catch (java.sql.SQLException e)
		{
		throw new JspTagException("TagMuestraPersonasNombreApellido: "+e.getMessage());
		}
	catch (Exception e)
		{
		throw new JspTagException("TagMuestraPersonasNombreApellido: "+e.getMessage());
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
	 * Establece el codigo de la institucion.
	 * @param codigoInstitucion El codigo de la institucion a establecer
	 */
	public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * Establece el tipo de la persona buscada.
	 * @param tipoPersonaBuscada El tipo de persona buscada a establecer
	 */
	public void setTipoPersonaBuscada(String tipoPersonaBuscada) {
		this.tipoPersonaBuscada = tipoPersonaBuscada;
	}

	
	/**
	 * @return Returns the codigoCentroCosto.
	 */
	public String getCodigoCentroCosto() {
		return codigoCentroCosto;
	}

	/**
	 * @param codigoCentroCosto The codigoCentroCosto to set.
	 */
	public void setCodigoCentroCosto(String codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}

	public String getMostrarSoloActivos() {
		return mostrarSoloActivos;
	}

	public void setMostrarSoloActivos(String mostrarSoloActivos) {
		this.mostrarSoloActivos = mostrarSoloActivos;
	}

	public String getMostrarSoloInactivos() {
		return mostrarSoloInactivos;
	}

	public void setMostrarSoloInactivos(String mostrarSoloInactivos) {
		this.mostrarSoloInactivos = mostrarSoloInactivos;
	}

}