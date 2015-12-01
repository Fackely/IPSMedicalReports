package com.princetonsa.action.manejoPaciente;

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
import util.Utilidades;

import com.princetonsa.actionform.manejoPaciente.AsignacionCamaCuidadoEspecialAPisoForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.AsignacionCamaCuidadoEspecialAPiso;


/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
 
public class AsignacionCamaCuidadoEspecialAPisoAction extends Action
{
	/**
	 * Para manejar los logger de la clase AsignacionCamaCuidadoEspecialAction
	 */
	Logger logger = Logger.getLogger(AsignacionCamaCuidadoEspecialAPisoAction.class);
	

	
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
		if (form instanceof AsignacionCamaCuidadoEspecialAPisoForm) 
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
			AsignacionCamaCuidadoEspecialAPisoForm forma = (AsignacionCamaCuidadoEspecialAPisoForm) form;
			
			//optenemos el estado que contiene la forma.
			String estado = forma.getEstado();
			
			// se instancia el mmundo
			AsignacionCamaCuidadoEspecialAPiso mundo = new AsignacionCamaCuidadoEspecialAPiso();
			
			 ActionErrors errores = new ActionErrors();
			logger.info("\n\n***************************************************************************");
			logger.info(" 	 EL ESTADO DE AsignacionCamaCuidadoEspecialForm ES ====>> "+forma.getEstado());
			logger.info("\n***************************************************************************");
			
			
			/*-------------------------------------------------------------------
			 * 					ESTADOS PARA EL CENSO DE CAMAS
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
					forma.setListadoPacientes(mundo.consultarPaciente(connection));
					if (Utilidades.convertirAEntero(forma.getListadoPacientes("numRegistros")+"")==1)
					{
						forma.setIndex("0");
						mundo.cargarDetalle(connection, forma, usuario, paciente, request);
						UtilidadBD.cerrarConexion(connection);
						return mapping.findForward("principal");
						
						
					}
					else 
					{
							UtilidadBD.cerrarConexion(connection);
							return mapping.findForward("listado");
					}
				}
				else
					/*----------------------------------------------
					 * ESTADO =================>>>>>  CARGARDETALLE
					 ---------------------------------------------*/
					
					if (estado.equals("cargarDetalle"))
					{	
						
						
						mundo.cargarDetalle(connection, forma, usuario, paciente, request);
						UtilidadBD.cerrarConexion(connection);
						return mapping.findForward("principal");
					}
					else
						/*----------------------------------------------
						 * ESTADO =================>>>>>  GUARDAR
						 ---------------------------------------------*/
						
						if (estado.equals("guardar"))
						{	
							errores=mundo.guardar(connection, forma, paciente, usuario, usuario, request);
							if (!errores.isEmpty())
								saveErrors(request, errores);
							
							UtilidadBD.cerrarConexion(connection);
							return mapping.findForward("principal");
						}
						else /*----------------------------
							 * 	ESTADO ==> REDIRECCION
							 ----------------------------*/
							if (estado.equals("redireccion"))
						{
							UtilidadBD.closeConnection(connection);
							response.sendRedirect(forma.getLinkSiguiente());
							return null;
						}
							else
								/*------------------------------
								 * 		ESTADO ==> ORDENAR
								 -------------------------------*/
								if (estado.equals("ordenar"))
							{
								mundo.accionOrdenarMapa(forma);
								UtilidadBD.closeConnection(connection);
								return mapping.findForward("listado");
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