package com.princetonsa.action.facturacion;


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

import com.princetonsa.actionform.facturacion.TrasladoSolicitudesPorTransplantesForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.TrasladoSolicitudesPorTransplantes;



/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */
 
public class TrasladoSolicitudesPorTransplantesAction extends Action
{
	/**
	 * Para manjar los logger de la clase TransladoSolicitudesPorTransplantesAction
	 */
	Logger logger = Logger.getLogger(TrasladoSolicitudesPorTransplantesAction.class);
	

	
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
			if (form instanceof TrasladoSolicitudesPorTransplantesForm) 
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
				TrasladoSolicitudesPorTransplantesForm forma = (TrasladoSolicitudesPorTransplantesForm) form;

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				// se instancia el mundo			
				TrasladoSolicitudesPorTransplantes mundo = new TrasladoSolicitudesPorTransplantes();


				//indices
				String [] indicesListado = mundo.indicesListado;

				logger.info("\n\n***************************************************************************");
				logger.info("EL ESTADO DE TransladoSolicitudesPorTransplantesForm ES ====>> "+forma.getEstado());
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
						forma.reset(true);
						return mundo.empezar(connection, paciente, request, mapping, forma,usuario);
					}
					else
						if (estado.equals("empezar2"))
						{
							forma.reset(false);
							return mundo.empezar(connection, paciente, request, mapping, forma,usuario);
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
									return mapping.findForward("listado");	

								}
								else/*----------------------------------------------
								 * ESTADO =================>>>>>  ORDENARRECEPTORES
								 ---------------------------------------------*/
									if (estado.equals("ordenarReceptores"))
									{
										mundo.accionOrdenarMapaReceptores(forma);
										UtilidadBD.closeConnection(connection);
										return mapping.findForward("listadoReceptores");	

									}
									else/*----------------------------------------------
									 * ESTADO =================>>>>>  checkTodos
									 ---------------------------------------------*/
										if (estado.equals("checkTodos"))
										{
											mundo.checkTodos(forma);
											UtilidadBD.closeConnection(connection);
											return mapping.findForward("listado");	

										}
										else/*----------------------------------------------
										 * ESTADO =================>>>>>  verificaChecks
										 ---------------------------------------------*/
											if (estado.equals("verificaChecks"))
											{
												mundo.verificarChecks(forma);
												UtilidadBD.closeConnection(connection);
												return mapping.findForward("listado");	

											}
											else/*----------------------------------------------
											 * ESTADO =================>>>>>  BUSQUEDAAVANZADA
										 ---------------------------------------------*/
												if (estado.equals("busquedaAvanzada"))
												{
													mundo.busquedaAvanzadaServiciosArticulos(connection, forma, paciente);
													UtilidadBD.closeConnection(connection);
													return mapping.findForward("listado");	

												}
												else/*----------------------------------------------
												 * ESTADO =================>>>>>  CARGARRECEPTORES
											 ---------------------------------------------*/
													if (estado.equals("cargarReceptores"))
													{
														forma.setListadoIngresosReceptores(mundo.consultarPacienteReceptores(connection, usuario.getCodigoCentroAtencion()+""));
														UtilidadBD.closeConnection(connection);
														logger.info("PASO POR QAUI!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
														return mapping.findForward("listado");	

													}
													else/*----------------------------------------------
													 * ESTADO =================>>>>>  TRASLADARSOLICITUDES
												 ---------------------------------------------*/
														if (estado.equals("trasladarSolicitudes"))
														{
															forma.setOperacionTrue(mundo.guardar(connection, forma, request, mapping, usuario, paciente));
															return mapping.findForward("listadoReceptores");
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