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

import util.RespuestaValidacion;
import util.UtilidadBD;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.ConsultaPreingresosForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.ConsultaPreingresos;


/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
 
public class ConsultaPreingresosAction extends Action
{
	/**
	 * Para manjar los logger de la clase ConsultaPreingresosAction
	 */
	Logger logger = Logger.getLogger(AperturaIngresosAction.class);
	

	
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
		if (form instanceof ConsultaPreingresosForm) 
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
			ConsultaPreingresosForm forma = (ConsultaPreingresosForm) form;
			
			//optenemos el estado que contiene la forma.
			String estado = forma.getEstado();
			
			//se instancia el mundo
			ConsultaPreingresos mundo = new  ConsultaPreingresos();
			
			//manejo de indieces
			String [] indicesListado = mundo.indicesListadoPreingresos;
			String [] indicesCriterios = mundo.indicesCriterios;
			
			logger.info("\n\n***************************************************************************");
			logger.info(" 	           EL ESTADO DE ConsultaPreingresosForm ES ====>> "+forma.getEstado());
			logger.info("\n***************************************************************************");
			
			
			/*-------------------------------------------------------------------
			 * 					ESTADOS PARA APERTURA INGRESOS
			 -------------------------------------------------------------------*/
			
			
			 
			/*----------------------------------------------
			 * ESTADO =================>>>>>  NULL
			 ---------------------------------------------*/
			if (estado == null)
			{
				
				//se saca un log con el siguiente texto.
				logger.warn("Estado no Valido dentro del Flujo de apertura ingresos (null)");
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
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("principal");
				}
				else
					/*----------------------------------------------
					 * ESTADO =================>>>>>  PACIENTE
					 ---------------------------------------------*/
					
					if (estado.equals("paciente"))
					{					
						/**
						  *  Validacion Cargado Paciente
						  */
						if(paciente.getCodigoPersona()<1)
						{		    
							RespuestaValidacion resp = new RespuestaValidacion("errors.paciente.noCargado",false);
							if(!resp.puedoSeguir)
							{
								return ComunAction.accionSalirCasoError(mapping, request, connection, logger, resp.textoRespuesta, resp.textoRespuesta, true);
							}
						}
						
						forma.setListado(mundo.cargarListadoPreingresos(connection, paciente, usuario));
						UtilidadBD.cerrarConexion(connection);
						return mapping.findForward("listado");
					}
					else
						/*----------------------------------------------
						 * ESTADO =================>>>>>  PERIODO
						 ---------------------------------------------*/
						
						if (estado.equals("periodo"))
						{					
							mundo.cargarCriterios(connection, usuario, forma);
							UtilidadBD.cerrarConexion(connection);
							return mapping.findForward("listadoRangos");
						}
						else
							/*----------------------------------------------
							 * ESTADO =================>>>>>  CARGARUSUARIOS
							 ---------------------------------------------*/
							
							if (estado.equals("cargarUsuarios"))
							{					
								forma.setUsuarios(mundo.cargarUsuarios(connection, forma.getCriterios(indicesCriterios[3])+"", usuario.getCodigoInstitucion()));
								UtilidadBD.cerrarConexion(connection);
								return mapping.findForward("listadoRangos");
							}
							else
								/*----------------------------------------------
								 * ESTADO =================>>>>>  CARGARINGRESO
								 ---------------------------------------------*/
								
								if (estado.equals("cargarIngreso"))
								{					
									forma.setDetalle(mundo.cargarDetalle(connection, forma.getListado(indicesListado[2]+forma.getIndex())+"", usuario.getCodigoInstitucion())); 
									UtilidadBD.cerrarConexion(connection);
									return mapping.findForward("detalle");
								}
								else
									/*----------------------------------------------
									 * ESTADO =================>>>>>  BUSCAR
									 ---------------------------------------------*/
									
									if (estado.equals("buscar"))
									{					
										forma.setListado(mundo.cargarListadoPreingresosRangos(connection, usuario, forma)); 
										UtilidadBD.cerrarConexion(connection);
										return mapping.findForward("listadoRangos");
									}
									else
										/*----------------------------------------------
										 * ESTADO =================>>>>>  CARGARIngresoRango
										 ---------------------------------------------*/
										
										if (estado.equals("cargarIngresoRango"))
										{					
											forma.setDetalle(mundo.cargarDetalle(connection, forma.getListado(indicesListado[2]+forma.getIndex())+"", usuario.getCodigoInstitucion())); 
											UtilidadBD.cerrarConexion(connection);
											return mapping.findForward("detalle");
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