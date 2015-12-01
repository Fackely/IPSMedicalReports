package com.princetonsa.action.glosas;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;

import com.princetonsa.actionform.glosas.ConceptosEspecificosForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.glosas.ConceptosEspecificos;

/**
 * Anexo 685
 * Fecha: Septiembre de 2008
 * @author Ing. Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public class ConceptosEspecificosAction extends Action
{
	/**
	 * Para manjar los logger de la clase ConceptosEspecificosAction
	 */
	Logger logger = Logger.getLogger(ConceptosEspecificosAction.class);
	
	/*----------------------------------------------------------------------------------
	 * 						METODO EXECUTE DEL ACTION
	 ----------------------------------------------------------------------------------*/
	public ActionForward execute (ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response) throws Exception
									{
		Connection connection = null;
		try{
			if (form instanceof ConceptosEspecificosForm)
			{

				connection = UtilidadBD.abrirConexion();

				//se verifica si la conexion esta nula
				if (connection == null)
				{
					// de ser asi se envia a una pagina de error. 
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//obtenemos el valor de la forma.
				ConceptosEspecificosForm forma = (ConceptosEspecificosForm) form;

				//obtenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				ConceptosEspecificos mundo= new ConceptosEspecificos(); 

				logger.info("\n\n***************************************************************************");
				logger.info(" 	 EL ESTADO DE ConceptosEspecificosForm ES ====>> "+forma.getEstado());
				logger.info("\n***************************************************************************");

				/*-------------------------------------------------------------------
				 * 		ESTADOS PARA EL CONCEPTOS ESPECIFICOS GLOSAS
			 -------------------------------------------------------------------*/

				/*----------------------------------------------
				 * ESTADO =================>>>>>  NULL
			 ---------------------------------------------*/

				if (estado == null)
				{
					//inicializamos los valores de los atributos del pager.
					forma.resetpager();
					//se saca un log con el siguiente texto.
					logger.warn("Estado no Valido dentro del Flujo de Conceptos Específicos (null)");
					//se asigana un error a mostar por ser un estado invalido
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					//se cierra la conexion con la BD
					UtilidadBD.cerrarConexion(connection);

					//se retorna el error al forward paginaError.
					return mapping.findForward("paginaError");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  EMPEZAR
			 ---------------------------------------------*/
				else if (estado.equals("empezar"))
				{ 
					forma.resetMensaje();
					forma.setRedireccion(false);
					mundo.empezar(connection, forma, usuario);
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("principal");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  NUEVO
			 ---------------------------------------------*/
				else if (estado.equals("nuevo"))
				{
					logger.info("===> Entré a estado nuevo");
					forma.resetMensaje();
					mundo.nuevo(forma);
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("principal");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  GUARDAR
			 ---------------------------------------------*/
				else if (estado.equals("guardar"))
				{
					ActionErrors errores = new ActionErrors();
					errores = mundo.guardar(connection, forma, usuario);
					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
						forma.setEstado("nuevo");
						forma.setRedireccion(true);
						UtilidadBD.cerrarConexion(connection);
						return mapping.findForward("principal");
					}
					else
					{
						UtilidadBD.cerrarConexion(connection);
						return mapping.findForward("principal");
					}
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  CARGAR
			 ---------------------------------------------*/
				else if (estado.equals("cargar"))
				{
					forma.resetMensaje();
					mundo.cargar(forma);
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("principal");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  ELIMINAR
			 ---------------------------------------------*/
				else if (estado.equals("eliminar"))
				{ 
					mundo.eliminar(connection, forma, usuario);
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("principal");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  ORDENAR
			 ---------------------------------------------*/
				else if (estado.equals("ordenar"))
				{
					mundo.ordenar(forma);
					UtilidadBD.closeConnection(connection);
					return mapping.findForward("principal");	
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  REDIRECCION
			 ---------------------------------------------*/
				else if (estado.equals("redireccion"))
				{
					forma.resetMensaje();
					UtilidadBD.closeConnection(connection);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
		return null;
									}
}