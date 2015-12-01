package com.princetonsa.action.manejoPaciente;


import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;

import com.princetonsa.actionform.manejoPaciente.ConsultarIngresosPorTransplantesForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.ConsultarIngresosPorTransplantes;




/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
 
public class ConsultarIngresosPorTransplantesAction extends Action
{
	/**
	 * Para manjar los logger de la clase ConsultarIngresosPorTransplantesAction
	 */
	Logger logger = Logger.getLogger(ConsultarIngresosPorTransplantesAction.class);
	

	
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
		if (form instanceof ConsultarIngresosPorTransplantesForm) 
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
			
			//se obtiene el paciente cargado en sesion.
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			
			//optenemos el valor de la forma.
			ConsultarIngresosPorTransplantesForm forma = (ConsultarIngresosPorTransplantesForm) form;
			
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			//optenemos el estado que contiene la forma.
			String estado = forma.getEstado();
						
			// se instancia el mundo			
			ConsultarIngresosPorTransplantes mundo = new ConsultarIngresosPorTransplantes(); 
			

			//indices
		
			
			logger.info("\n\n***************************************************************************");
			logger.info("EL ESTADO DE TrasladoSolicitudesPorTransplantesForm ES ====>> "+forma.getEstado());
			logger.info("\n***************************************************************************");
			
			
			/*-------------------------------------------------------------------
			 * 	ESTADOS PARA EL TRANSLADO DE SOLICITUDES POR TRANSPLANTES
			 -------------------------------------------------------------------*/
			
			
			/*----------------------------------------------
			 * ESTADO =================>>>>>  NULL
			 ---------------------------------------------*/
			
			if (estado == null)
			{
			
				//se saca un log con el siguiente texto.
				logger.warn("Estado no Valido dentro del Flujo de Censo de Camas (null)");
				//se asigana un error a mostar por ser un estado invalido
				request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
				//se cierra la conexion con la BD
				UtilidadBD.cerrarConexion(connection);
				
				//se retorna el error al forward paginaError.
				return mapping.findForward("paginaError");
			}
			else
				/*----------------------------------------------
				 * ESTADO =================>>>>>  EMPEZAR
				 ---------------------------------------------*/
				if (estado.equals("empezar"))
				{
					forma.reset();
					mundo.empezar(connection, usuario, forma);
					return mapping.findForward("principal");
				}
				else
					/*----------------------------------------------
					 * ESTADO =================>>>>>  BUSCAR
					 ---------------------------------------------*/
					
					if (estado.equals("buscar"))
					{
						forma.setListadoPaciente(mundo.buscar(connection, forma, usuario));
						return mapping.findForward("principal");
					}
					else /*----------------------------------------------
						 * ESTADO =================>>>>>  REDIRECCION
						 ---------------------------------------------*/
						if (estado.equals("redireccion"))
						{
							UtilidadBD.closeConnection(connection);
							response.sendRedirect(forma.getLinkSiguiente());
							return null;
						}
						else/*----------------------------------------------
							 * ESTADO =================>>>>>  ORDENAR
							 ---------------------------------------------*/
							if (estado.equals("ordenar"))
							{
								mundo.accionOrdenarMapa(forma);
								UtilidadBD.closeConnection(connection);
								return mapping.findForward("principal");	
						
							}
							else/*----------------------------------------------
								 * ESTADO =================>>>>>  CARGARDETALLE
								 ---------------------------------------------*/
								if (estado.equals("cargarDetalle"))
								{
									mundo.cargarDetalle(connection, forma, usuario);
									UtilidadBD.closeConnection(connection);
									return mapping.findForward("detalle");	
							
								}
								else/*----------------------------------------------
									 * ESTADO =================>>>>>  IMPRIMIR
									 ---------------------------------------------*/
									if (estado.equals("imprimir"))
									{
										mundo.accionImprimir(connection, forma, usuario, institucion, request);
										UtilidadBD.closeConnection(connection);
										return mapping.findForward("principal");	
								
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