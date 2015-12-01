/*
 * @(#)TagMostrarLinkEnviarEvolucion.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util.tag;

import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import util.UtilidadBD;
import util.UtilidadValidacion;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * El objetivo de esta clase es mostrar o no (dependiendo de unas reglas del
 * negocio especificadas en UtilidadValidacion) el link que permite ir a 
 * Enviar Evoluciones a Epicrisis
 *   
 *	@version 1.0, Aug 27, 2003
 */
public class TagMostrarLinkEnviarEvolucion extends TagSupport
{
	/**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(TagMostrarLinkEnviarEvolucion.class);

	/**
	 * Este es el metodo que hay que sobreescribir en cualquier
	 * clase que extienda <code>TagSupport</code>(Custom Tags de
	 * JSP). Este metodo recibe a través del <code>jsp:param</code>
	 * el codigo del tipo de Régimen y solo muestra los estratos
	 * aceptables para este tipo de regimen
	 * Si solo hay un estrato para este tipo de regimen, imprime un
	 * hidden.
	 * @return la constante SKIP_BODY
	 */
	String nombreContexto=null;
	
	
	public int doStartTag() throws JspException
	{
	 try
		{
			HttpSession session=this.pageContext.getSession();
			ServletRequest request=pageContext.getRequest();
			ServletContext contexto=pageContext.getServletContext();
			
			nombreContexto=contexto.getServletContextName();
					
			PersonaBasica paciente=(PersonaBasica)session.getAttribute("pacienteActivo");
			UsuarioBasico usuario=(UsuarioBasico)session.getAttribute("usuarioBasico");

			if (usuario==null)
			{
				request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
				pageContext.forward(nombreContexto+"common/mostrarErroresStruts.jsp");
			}

			if (paciente==null)
			{
				pageContext.forward(nombreContexto+"common/mostrarErroresStruts.jsp");
				request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			}

			Connection con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();

			if (UtilidadValidacion.deboMostrarEnviarEvolucionesEpicrisis(con, paciente, usuario))
			{
				pageContext.getOut().print("| <a href=\"javascript:cambiarEstadoSalidaPrevisible('evolucion.do?estado=historicoModificar')\"> Enviar Evoluciones anteriores a Epicrisis </a>");
			}

            UtilidadBD.closeConnection(con);
;
		}
		catch (java.io.IOException e)
		{
			logger.warn("Error de entrada y salida en TagMostrarLinkEnviarEvolucion " + e.toString());
			throw new JspTagException("TagMostrarLinkEnviarEvolucion: "+e.getMessage());
		}
		catch (java.sql.SQLException e)
		{
			logger.warn("Error de BD en TagMostrarLinkEnviarEvolucion " + e.toString());
			throw new JspTagException("TagMostrarLinkEnviarEvolucion: "+e.getMessage());
		}
		catch (Exception e)
		{
			logger.warn("Excepción (No BD ni I/O) en TagMostrarLinkEnviarEvolucion " + e.toString());
			throw new JspTagException("TagMostrarLinkEnviarEvolucion: "+e.getMessage());
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




}
