package com.princetonsa.action.cartera;

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

import com.princetonsa.actionform.cartera.RecaudoCarteraEventoForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.RecaudoCarteraEvento;





/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
 
public class RecaudoCarteraEventoAction extends Action 
{
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
		Logger logger = Logger.getLogger(RecaudoCarteraEventoAction.class);
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
		
		
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form, 
									HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		
		Connection connection = null;
		try{
		if (form instanceof RecaudoCarteraEventoForm) 
		 {
						
			
			//se habre la conexion a la BD
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
			
			//se obtiene el paciente cargado en sesion.
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			
			//optenemos el valor de la forma.
			RecaudoCarteraEventoForm forma = (RecaudoCarteraEventoForm) form;
			
			//optenemos el estado que contiene la forma.
			String estado = forma.getEstado();
			
			ActionErrors errores = new ActionErrors();
			
			logger.info("\n\n***************************************************************************");
			logger.info(" 	     EL ESTADO DE RecaudoCarteraEventoForm ES ====>> "+forma.getEstado());
			logger.info("\n***************************************************************************");
			
		 
			/*-------------------------------------------------------------------
			 * 	ESTADOS PARA LA GENERACION MODIFICACION AJUSTES FACTURAS VARIAS
			 -------------------------------------------------------------------*/
			
			
			/*----------------------------------------------
			 * ESTADO =================>>>>>  NULL
			 ---------------------------------------------*/
			if (estado == null)
			{
				//resetiamos la forma.
				forma.reset();
			
				//se saca un log con el siguiente texto.
				logger.warn("Estado no Valido dentro del Flujo de Recaudo Cartera Evento (null)");
				//se asigana un error a mostar por ser un estado invalido
				request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
				//se cierra la conexion con la BD
				UtilidadBD.cerrarConexion(connection);
				
				//se retorna el error al forward paginaError.
				return mapping.findForward("paginaError");
			}
			/*----------------------------------------------
			 * ESTADO =================>>>>>  INICIAL
			 ---------------------------------------------*/
			else
				if (estado.equals("inicial"))
				{ 
					forma.reset();
					RecaudoCarteraEvento.inicializar(connection, forma, usuario);
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("principal");
				}
				/*----------------------------------------------
				 * ESTADO =================>>>>>  CARGARCONVENIOS
				 ---------------------------------------------*/
				else
					if (estado.equals("cargarConvenios"))
					{ 
						RecaudoCarteraEvento.filtrarConvenio(connection, forma, usuario);
						UtilidadBD.cerrarConexion(connection);
						return mapping.findForward("principal");
					}
					/*----------------------------------------------
					 * ESTADO =================>>>>> BUSCAR
					 ---------------------------------------------*/
					else
						if (estado.equals("buscar"))
						{ 
							RecaudoCarteraEvento.buscarReporte(connection, forma,usuario,request);
							UtilidadBD.cerrarConexion(connection);
							return mapping.findForward("principal");
						}
						/*----------------------------------------------
						 * ESTADO =================>>>>> CANCELAR PROCESO
						 ---------------------------------------------*/
						else
							if(estado.equals("cancelarProceso"))
							{
								UtilidadBD.closeConnection(connection);
							    return mapping.findForward("principal");
							}
				 
		 
		 }
		
		
		
		
		
		
		return super.execute(mapping, form, request, response);
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(connection);
		}
		return null;
	}
	
	
	
}