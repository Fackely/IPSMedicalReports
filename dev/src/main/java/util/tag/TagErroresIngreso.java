/*
 * @(#)TagMuestraConvenios.java
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

import util.MD5Hash;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.TagDao;

/**
 * Esta clase maneja los mensajes de error que muestra la aplicación cuando el
 * usuario NO puede ingresar. Por el momento solo aplica cuando el usuario esta
 * inactivo (No se muestran otras razones por seguridad. Ej: El usuario existe
 * pero el password esta mal, los dos están mal)
 *
 * @version 1.0, Feb 24, 2003
 * @author <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s
 * &oacute;pez P.</a>
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 * P.</a>,
 * @author <a href="mailto:Diego@PrincetonSA.com">Diego Ram&iacute;rez </a>
 */

public class TagErroresIngreso extends TagSupport{

	/**
	 * Objeto que representa una conexión con una base de datos.
	 */
	private Connection con = null;
	
	/**
	 * Código del tipo de régimen.
	 */
	private String login="";

	/**
	 * Tipo de Mensaje a mostrar
	 */
	private String tipoMensaje="";
	
	/**
	 * Password a utilizar
	 */
	private String password="";

	/**
	 * Este es el metodo que hay que sobreescribir en cualquier
	 * clase que extienda <code>TagSupport</code>(Custom Tags de
	 * JSP). Este metodo recibe a través del <code>jsp:param</code>
	 * el codigo del tipo de Règimen y solo muestra los estratos
	 * aceptables para este tipo de regimen
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

			int numResultados;

			if (tipoMensaje.equals("usuarioInactivo"))
			{
				ResultSetDecorator rs = null;

				rs=tagDao.consultaTagErroresIngreso(con, login, MD5Hash.hashPassword(password));
	
				//Con este while vamos a guardar en el vector todos los estratos disponibles
				if (rs.next())
				{
					numResultados=Integer.parseInt(rs.getString("numResultados"));
					if (numResultados>0)
					{
						pageContext.getOut().print("Este usuario ha sido desactivado, por favor comuniquese con su administrador para volver a tener acceso");
					}
				}
	
			}
		
		}
	catch (java.io.IOException e)
		{
		throw new JspTagException("TagErroresIngreso: "+e.getMessage());
		}
	catch (java.sql.SQLException e)
		{
		throw new JspTagException("TagErroresIngreso: "+e.getMessage());
		}
	catch (Exception e)
		{
		throw new JspTagException("TagErroresIngreso: "+e.getMessage());
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
	public String getLogin() {
		return login;
	}

	/**
	 * Establece el codigo del estrato.
	 * @param login El codigo del estrato a establecer
	 */
	public void setLogin(String codigoEstrato) {
		this.login = codigoEstrato;
	}

	/**
	 * Returns the tipoMensaje.
	 * @return String
	 */
	public String getTipoMensaje() {
		return tipoMensaje;
	}

	/**
	 * Sets the tipoMensaje.
	 * @param tipoMensaje The tipoMensaje to set
	 */
	public void setTipoMensaje(String tipoMensaje) {
		this.tipoMensaje = tipoMensaje;
	}

	/**
	 * Returns the password.
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Sets the password.
	 * @param password The password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}