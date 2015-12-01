/*
 * Oct 08 2005
 *
 */
package util.tag;

import java.sql.Connection;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import java.util.HashMap;

import util.Encoder;

import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Sebastián Gómez R
 *
 * Esta clase maneja la funcionalidad del tag que permite
 * seleccionar los usuarios inactivos de una persona
 */
public class TagMuestraUsuariosInactivos extends TagSupport 
{
	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con=null;
	
	/**
	 * Código de la persona
	 */
	private String codigo="";
	
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
			UsuarioBasico usuario=new UsuarioBasico();
			String s="";
			Collection usuarios=usuario.cargarUsuariosMismoProfesional(con,Integer.parseInt(codigo),"false");
			
			Iterator iterador=usuarios.iterator();
			while(iterador.hasNext())
			{
				HashMap usuarioBD=(HashMap)iterador.next();
				s="<option value=\""+usuarioBD.get("login")+"\">"+Encoder.encode(usuarioBD.get("login")+"")+"</option>";
				pageContext.getOut().print(s);
			}
			
			
			
		}
		catch (java.io.IOException e)
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
	 * @return Returns the codigo.
	 */
	public String getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
}
