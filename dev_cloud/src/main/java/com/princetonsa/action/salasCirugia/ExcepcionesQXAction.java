/*
 * @(#)ExcepcionesQXAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.salasCirugia;

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
import com.princetonsa.actionform.salasCirugia.ExcepcionesQXForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.salasCirugia.ExcepcionesQX;

/**
 *   Action, controla todas las opciones dentro de las excepciones qx 
 *   incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, Oct 12, 2005
 * @author wrios
 * @author Jhony Alexander Duque A.
 * 
 */
public class ExcepcionesQXAction extends Action
{
    /**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ExcepcionesQXAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
													{
		Connection connection=null;
		try{
			connection = UtilidadBD.abrirConexion();

			if(form instanceof ExcepcionesQXForm)
			{
				//			se verifica si la conexion esta nula
				if (connection == null)
				{
					// de ser asi se envia a una pagina de error. 
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}

				//se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

				//optenemos el valor de la forma.
				ExcepcionesQXForm forma = (ExcepcionesQXForm) form;

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				// mundo 
				ExcepcionesQX mundo = new ExcepcionesQX();

				ActionErrors errores = new ActionErrors();

				logger.info("\n\n***************************************************************************");
				logger.info(" 	           EL ESTADO DE ExcepcionesQXForm ES ====>> "+forma.getEstado());
				logger.info("\n***************************************************************************");

				/*-------------------------------------------------------------------
				 * 					ESTADOS PARA EXCEPCIONESQX
			 -------------------------------------------------------------------*/

				if(estado == null)
				{
					forma.reset();	
					logger.warn("Estado no valido dentro del flujo de la EXCECIONES QX (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(connection);
					return mapping.findForward("paginaError");
				}
				else/*------------------------------
				 * 		ESTADO ==> EMPEZAR
				 -------------------------------*/
					if (estado.equals("empezar"))
					{
						return ExcepcionesQX.accionEmpezar(connection, usuario.getCodigoInstitucionInt(), forma, mapping);
					}
					else/*------------------------------
					 * 		ESTADO ==> BUSCARVIG
					 -------------------------------*/
						if (estado.equals("buscarvig"))
						{
							return ExcepcionesQX.buscarVigencias(connection, forma, usuario.getCodigoInstitucionInt(), mapping);
						}
						else /*------------------------------
						 * 		ESTADO ==> NUEVAVIG
						 -------------------------------*/
							if(estado.equals("nuevavig"))
							{
								return ExcepcionesQX.accionNuevaVigencias(forma, usuario, connection, response, request);
							}
							else/*------------------------------
							 * 		ESTADO ==> GUARDAR
							 -------------------------------*/
								if(estado.equals("guardarvig"))
								{
									errores = ExcepcionesQX.accionValidar(forma, errores);

									if(!errores.isEmpty())
									{
										forma.setEstado("buscarvig");
										saveErrors(request,errores);	
										UtilidadBD.closeConnection(connection);
										return mapping.findForward("framevig");
									}

									logger.info("\n el hashmap vigencias "+forma.getVigencias());
									return ExcepcionesQX.accionGuardarRegistrosVig(connection, forma, mapping, usuario);
								}
								else/*------------------------------
								 * 		ESTADO ==> ELIMINARVIG
								 -------------------------------*/ 
									if(estado.equals("eliminarvig"))
									{
										UtilidadBD.closeConnection(connection);
										return ExcepcionesQX.accionEliminarCampoVig(forma, request, response, usuario.getCodigoInstitucionInt(), mapping);
									}
									else /*------------------------------
									 * 		ESTADO ==> CENTROCOSTO
									 -------------------------------*/
										if(estado.equals("centrocosto"))
										{
											return ExcepcionesQX.cargarencabezado(connection, forma, mapping);
										}
										else /*------------------------------
										 * 		ESTADO ==> RECARGAR SELECT TIPO PACIENTE
										 -------------------------------*/
											if(estado.equals("recargar") || estado.equals("recargarBusqueda"))
											{
												return ExcepcionesQX.cargarTipoPaciente(connection, forma, mapping);
											}

											else/*------------------------------
											 * 		ESTADO ==> BUSCARDET
										 -------------------------------*/
												if(estado.equals("buscardet"))
												{

													return ExcepcionesQX.buscarCentroCosto(connection, forma, mapping);
												}
												else/*------------------------------
												 * 		ESTADO ==> NUEVODET
											 -------------------------------*/
													if(estado.equals("nuevodet"))
													{
														return ExcepcionesQX.accionNueva(forma, usuario, connection, response, request);
													}
													else/*------------------------------
													 * 		ESTADO ==> ELIMINAR
												  -------------------------------*/
														if(estado.equals("eliminar"))
														{
															UtilidadBD.closeConnection(connection);
															return ExcepcionesQX.accionEliminarCampo(forma, request, response, usuario.getCodigoInstitucionInt(), mapping);
														}
														else/*------------------------------
														 * 		ESTADO ==> ELIMINAR
													  -------------------------------*/
															if(estado.equals("guardar"))
															{
																return mundo.accionGuardarRegistros(connection, forma, mapping, usuario);
															}
															else/*------------------------------
															 * 		ESTADO ==> ORDENAR
														 -------------------------------*/
																if (estado.equals("ordenarvig"))
																{
																	ExcepcionesQX.accionOrdenarMapaVig(forma);
																	UtilidadBD.closeConnection(connection);
																	return mapping.findForward("framevig");
																}
																else/*------------------------------
																 * 		ESTADO ==> ORDENAR
															 -------------------------------*/
																	if (estado.equals("ordenar"))
																	{
																		ExcepcionesQX.accionOrdenarMapa(forma);
																		UtilidadBD.closeConnection(connection);
																		return mapping.findForward("frame");
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
																		else/*----------------------------
																		 * 	ESTADO ==> BUSQUEDA AVANZADA
																	 ----------------------------*/
																			if (estado.equals("busquedaavanzada"))
																			{

																				return ExcepcionesQX.busqueda(connection, forma, mapping);
																			}
																			else/*----------------------------
																			 * 	ESTADO ==> BUSCAR
																		 ----------------------------*/
																				if (estado.equals("buscar"))
																				{

																					return ExcepcionesQX.busquedaAvanzada(connection, forma, mapping);
																				}
																				else
																				{
																					forma.reset();
																					logger.warn("Estado no valido dentro del flujo de ExcepcionesQX (null) ");
																					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
																					UtilidadBD.cerrarConexion(connection);
																					return mapping.findForward("paginaError");
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