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
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.ValoresPorDefecto;
import com.princetonsa.actionform.salasCirugia.AsociosXRangoTiempoForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.salasCirugia.AsociosXRangoTiempo;


/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */

public class AsociosXRangoTiempoAction extends Action
{
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	Logger logger = Logger.getLogger(AsociosXRangoTiempoAction.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Connection connection = null;
		try{
			if (form instanceof AsociosXRangoTiempoForm)
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
				AsociosXRangoTiempoForm forma = (AsociosXRangoTiempoForm) form;

				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();

				ActionErrors errores = new ActionErrors();

				logger.info("\n\n***************************************************************************");
				logger.info(" 	           EL ESTADO DE AsociosXRangoTiempoForm ES ====>> "+forma.getEstado());
				logger.info("\n***************************************************************************");

				/*-------------------------------------------------------------------
				 * 					ESTADOS PARA ASOCIOS POR RANGO DE TIEMPO
			 -------------------------------------------------------------------*/

				/*----------------------------------
				 * 			ESTADO ==> NULL
			 ---------------------------------*/
				if (estado == null)
				{
					//se formatean los valores de la forma
					forma.reset();
					forma.resetPager();

					logger.warn("Estado no Valido dentro del Flujo de Asocios X Rango de Tiempo (null)");
					//se asigana un error a mostar por ser un estado invalido
					request.setAttribute("CodigoDescripcionError","errors.estadoInvalido");
					//se cierra la conexion con la BD
					UtilidadBD.cerrarConexion(connection);
					//se retorna el error al forward paginaError.
					return mapping.findForward("paginaError");
				}
				else
					/*------------------------------
					 * 		ESTADO ==> INICIAL
				 -------------------------------*/
					if (estado.equals("inicial"))
					{
						forma.reset();
						forma.resetPager();
						forma.resetBusqueda();
						AsociosXRangoTiempo.init(connection, usuario.getCodigoInstitucionInt(),forma);
						return mapping.findForward("principal");
					}
					else
						/*------------------------------
						 * 		ESTADO ==> BUSCAR
					 -------------------------------*/
						if (estado.equals("buscar"))
						{
							return AsociosXRangoTiempo.buscar(connection, forma, mapping,usuario);
						}
						else
							/*------------------------------
							 * 		ESTADO ==> BUSCARCONVENIO	
						 -------------------------------*/
							if (estado.equals("buscarconv"))
							{

								return AsociosXRangoTiempo.obtenerFechasAsocios(connection, mapping, forma);

							}
							else
								/*------------------------------
								 * 		ESTADO ==> ORDENAR
						 -------------------------------*/
								if (estado.equals("ordenar"))
								{
									AsociosXRangoTiempo.accionOrdenarMapa(forma);
									UtilidadBD.closeConnection(connection);
									return mapping.findForward("frame");
								}
								else
									/*------------------------------
									 * 		ESTADO ==> ORDENAR
							 -------------------------------*/
									if (estado.equals("ordenarvig"))
									{
										AsociosXRangoTiempo.accionOrdenarMapaVig(forma);
										UtilidadBD.closeConnection(connection);
										return mapping.findForward("vigencias");
									}
									else 
										/*------------------------------
										 * 		ESTADO ==> NUEVO
							 -------------------------------*/
										if (estado.equals("nuevo"))
										{

											AsociosXRangoTiempo.accionNuevo(forma, usuario,connection);
											UtilidadBD.closeConnection(connection);
											return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()), Integer.parseInt(forma.getAsociosXRangoTiempo("numRegistros").toString()), response, request, "frameAsociosXRangoTiempo.jsp", true);

										}
										else 
											/*------------------------------
											 * 		ESTADO ==> NUEVAVIGENCIA
							 -------------------------------*/
											if (estado.equals("nuevavigencia"))
											{

												AsociosXRangoTiempo.accionNuevaVigencias(forma, usuario, connection);
												UtilidadBD.closeConnection(connection);
												return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()), Integer.parseInt(forma.getFechas("numRegistros").toString()), response, request, "frameVigenciasConvenio.jsp", true);

											}		
											else
												/*------------------------------
												 * 		ESTADO ==> ELIMINAR
									 -------------------------------*/
												if (estado.equals("eliminar"))
												{
													return AsociosXRangoTiempo.accionEliminarCampo(forma, request, response, usuario.getCodigoInstitucionInt());
												}

												else
													/*------------------------------
													 * 		ESTADO ==> ELIMINARVIGENCIA
										 -------------------------------*/
													if (estado.equals("eliminarvig"))
													{
														return AsociosXRangoTiempo.accionEliminarCampoVig(forma, request, response, usuario.getCodigoInstitucionInt());
													}
													else
														/*----------------------------
														 * 	ESTADO ==> GUARDAR
										 ----------------------------*/
														if(estado.equals("guardar"))
														{
															return AsociosXRangoTiempo.accionGuardarRegistros(connection, forma, mapping, usuario);
														}
														else
															/*----------------------------
															 * 	ESTADO ==> GUARDAR
											 ----------------------------*/
															if(estado.equals("guardarvigencias"))
															{
																errores = this.accionValidar(forma, errores);

																if(!errores.isEmpty())
																{
																	forma.setEstado("error");
																	saveErrors(request,errores);	
																	UtilidadBD.closeConnection(connection);
																	return mapping.findForward("vigencias");
																}

																return AsociosXRangoTiempo.accionGuardarRegistrosCabeza(connection, forma, mapping, usuario);
															}

															else

																/*----------------------------
																 * 	ESTADO ==> DETALLE VIGENCIAS
												 ----------------------------*/
																if (estado.equals("detallevigencias"))
																{

																	return AsociosXRangoTiempo.buscar(connection, forma, mapping,usuario);
																}
																else

																	/*----------------------------
																	 * 	ESTADO ==> DETALLE
													 ----------------------------*/
																	if (estado.equals("detalle"))
																	{

																		AsociosXRangoTiempo.cargarDetalle(connection, forma);
																		return mapping.findForward("detalle");
																	}
																	else
																		/*----------------------------
																		 * 	ESTADO ==> GUARDARDETALLE
														 ----------------------------*/
																		if (estado.equals("guardardetalle"))
																		{
																			AsociosXRangoTiempo.guardarDetalle(forma);
																			forma.setEstado("cerrar");
																			return mapping.findForward("detalle");
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
																			else /*----------------------------
																			 * 	ESTADO ==> BUSQUEDAAVANZADA
															 ----------------------------*/
																				if (estado.equals("busquedaavanzada"))
																				{
																					forma.reset();
																					AsociosXRangoTiempo.resetBusquedaAvanzada(forma);
																					AsociosXRangoTiempo.cargarValoresSelects(connection, forma);
																					UtilidadBD.closeConnection(connection);
																					return mapping.findForward("busquedaavanzada");
																				}
																				else /*----------------------------
																				 * 	ESTADO ==> BUSQUEDAAVANCED
																 ----------------------------*/
																					if (estado.equals("buscaradvanced"))
																					{

																						return AsociosXRangoTiempo.busquedaAvanzada(connection, forma, usuario, mapping);

																					}
																					else
																						/*------------------------------
																						 * 		ESTADO ==> ORDENARCONSULTA
																	 -------------------------------*/
																						if (estado.equals("ordenarconsulta"))
																						{
																							AsociosXRangoTiempo.accionOrdenarMapa(forma);
																							UtilidadBD.closeConnection(connection);
																							return mapping.findForward("busquedaavanzada");
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
	
	
	public ActionErrors accionValidar(AsociosXRangoTiempoForm forma,ActionErrors errores)
{
	
		if (forma.getEstado().equals("guardarvigencias"))
		{
			logger.info(":::::::Entro a guardarvigencias en el validate ");
			for (int i= 0; i < Integer.parseInt(forma.getFechas("numRegistros")+"");i++)
			{
			
				//se valida que la fecha inicial no este vacia	
				if ( forma.getFechas().containsKey("fecIniAsoc_"+i) && forma.getFechas("fecIniAsoc_"+i).equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial del registro "+(i+1)));
				//se valida que la fecha final no este vacia.
				if ( forma.getFechas().containsKey("fecFinAsoc_"+i) && forma.getFechas("fecFinAsoc_"+i).equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final del registro "+(i+1)));
			//se valida que la fecha final sea mayor o igual a la fecha del sistema
				if (!(forma.getFechasClone().containsKey("fecFinAsoc_"+i)))
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), forma.getFechas("fecFinAsoc_"+i)+""))
						errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Final "+forma.getFechas("fecFinAsoc_"+i), "Actual "+UtilidadFecha.getFechaActual()));
				
				if ( forma.getFechas().containsKey("fecFinAsoc_"+i) && forma.getFechas().containsKey("fecIniAsoc_"+i) &&
						!forma.getFechas("fecIniAsoc_"+i).equals("") && !forma.getFechas("fecFinAsoc_"+i).equals("") &&
						!UtilidadFecha.esFechaMenorIgualQueOtraReferencia((forma.getFechas("fecIniAsoc_"+i)+""), (forma.getFechas("fecFinAsoc_"+i)+"")))
					errores.add("descripcion",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia"," Final "+forma.getFechas("fecFinAsoc_"+i)+" del registro "+(i+1), "Inicial "+forma.getFechas("fecIniAsoc_"+i)));
					
					
				for (int j= i; j < Integer.parseInt(forma.getFechas("numRegistros")+"");j++)
				{
					if (j>i && !(forma.getFechas("fecIniAsoc_"+i)+"").equals("") && !(forma.getFechas("fecFinAsoc_"+i)+"").equals("") && !(forma.getFechas("fecIniAsoc_"+j)+"").equals("") && !(forma.getFechas("fecFinAsoc_"+j)+"").equals(""))
					{
						// se pregunta por el cruze de fechas
						logger.info("\n \n el valor de las fechas es fecha incial "+ forma.getFechas("fecIniAsoc_"+j)+" fecha final "+forma.getFechas("fecFinAsoc_"+i));
						if (UtilidadFecha.existeTraslapeEntreFechas((forma.getFechas("fecIniAsoc_"+i)+""), (forma.getFechas("fecFinAsoc_"+i)+""), (forma.getFechas("fecIniAsoc_"+j)+""), (forma.getFechas("fecFinAsoc_"+j)+"")))
						{
							errores.add("descripcion",new ActionMessage("error.rangoFechasInvalido",(forma.getFechas("fecIniAsoc_"+j)+""),
									(forma.getFechas("fecFinAsoc_"+j)+"")+" del registro "+(j+1),(forma.getFechas("fecIniAsoc_"+i)+""),
									(forma.getFechas("fecFinAsoc_"+i)+"")+" del registro "+(i+1)));
							
							j= Integer.parseInt(forma.getFechas("numRegistros")+"");
						}
					}
			
				}
			}
			
			
		}
		
		
		return errores;
		
}
	
	
	
}