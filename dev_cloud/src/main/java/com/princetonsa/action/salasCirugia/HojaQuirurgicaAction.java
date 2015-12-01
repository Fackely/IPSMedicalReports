/*
 * 
 * Jhony Alexander Duque A.
 */
package com.princetonsa.action.salasCirugia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;
import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.salasCirugia.HojaQuirurgicaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;
import com.princetonsa.mundo.ordenesmedicas.procedimientos.RespuestaProcedimientos;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;
import com.servinte.axioma.bl.manejoPaciente.impl.ProcesoGeneracionAutorizacionMundo;
import com.servinte.axioma.bl.manejoPaciente.interfaz.IProcesoGeneracionAutorizacionMundo;
import com.servinte.axioma.bl.ordenes.facade.OrdenesFacade;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionPorOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;

/**
 * Manejo de la Hoja Quirúrgica
 * @author Jhony Alexander Duque A.
 * 
 * CopyRight Princeton S.A.
 * 
 */
public class HojaQuirurgicaAction extends Action
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(HojaQuirurgicaAction.class);
	
	String [] indicesServicios = HojaQuirurgica.indicesServicios;
	String [] indicesProfesionales = HojaQuirurgica.indicesProfesionales;
	String [] indicesSolicitud = HojaQuirurgica.indicesSolicitud;
	String [] indicesDiagPostOpera = HojaQuirurgica.indicesDiagPostopera;
	String [] indicesParamGenerales = HojaQuirurgica.indicesParamGenerales;
	String [] indicesSecInfoQx = HojaQuirurgica.indicesSecInfoQx;
	String [] indicesFechasCx = HojaQuirurgica.indicesFechasCx;
	String [] indicesProfInfoQx = HojaQuirurgica.indicesProfInfoQx;
	String [] indicesOtrosProfesionales = HojaQuirurgica.indicesOtrosProfesionales;
	String [] indicesSalidaSala = HojaQuirurgica.indicesSalidaSala;
	
	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.manejoPaciente.AutorizacionesIngresoEstanciaForm");
	
	
	/**
	 * Metodo para el control del flujo de la funcionalidad
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
			if(form instanceof HojaQuirurgicaForm)
			{
				HojaQuirurgicaForm forma=(HojaQuirurgicaForm)form;
				String estado=forma.getEstado();
				ActionErrors errores = new ActionErrors();
				logger.info("\n\n***************************************************************************");
				logger.info(" 	           EL ESTADO DE HojaQuirurgicaForm ES ====>> "+forma.getEstado());
				logger.info(" 	          funcionalidad ES ====>> "+forma.getFuncionalidad());
				logger.info(" 	          esdummy ES ====>> "+forma.isEsDummy());
				logger.info(" 	          paciente ES ====>> "+forma.getPaciente().getCodigoPersona());
				logger.info(" 	          peticion ES ====>> "+forma.getPeticion());
				logger.info("\n***************************************************************************");

				PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				//Se toma el valor de validacion capitacion desde responder cirugia para saber si es de capitacion y si tiene orden
				boolean validacionCapitacion=forma.isValidacionCapitacion();

				//se reciben los datos traidos por request
				HojaQuirurgica.obtenerDatosRequest(forma, request);

				if(usuario==null)
				{
					return ComunAction.accionSalirCasoError(mapping, request, null, logger, "No existe el usuario", "errors.usuario.noCargado", true);
				}

				logger.info(" 	          getCodigoPaciente ====>> "+forma.getCodigoPaciente());
				logger.info(" 	          funcionalidad ES ====>> "+forma.getFuncionalidad());
				logger.info(" 	          forma.getPaciente().getCodigoPersona() ====>> "+forma.getPaciente().getCodigoPersona());
				logger.info(" 	          paciente.getCodigoPersona() ES ====>> "+paciente.getCodigoPersona());


				//Carga la informacion del paciente pasados por parametros
				forma.setCodigoPaciente(request.getParameter("codigoPaciente")!=null?request.getParameter("codigoPaciente"):"");
				//TODO Permite obtener el usuario que está logeado al momento de realizar la petición de una cirugía, para que posteriormente
				//aparezca parametrizado en la seccion Profesional en la opción servicios.
				forma.setCodigoUsuario(String.valueOf(usuario.getCodigoPersona()));
				forma.setCodigoParamAsocioCirujano(ValoresPorDefecto.getAsocioCirujano(usuario.getCodigoInstitucionInt()));
				
				if(paciente==null || paciente.getTipoIdentificacionPersona().equals("") && !forma.getFuncionalidad().equals("ListProce"))
				{
					//Evalua si se carga el paciente a partir del codigo del Paciente 

					if(!forma.getCodigoPaciente().equals(""))
					{
						logger.info(" 	          entre 1 ====>> ");
						paciente.setCodigoPersona(Utilidades.convertirAEntero(forma.getCodigoPaciente()));
						con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
						UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
						forma.setPaciente(paciente);
						UtilidadBD.closeConnection(con);
					}

					if(paciente==null || paciente.getTipoIdentificacionPersona().equals(""))
						return ComunAction.accionSalirCasoError(mapping, request, null, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);

				}//si el paciente que viene por parametro es diferente al que esta cargado se carga el que viene por parametro 
				else 
					if( UtilidadCadena.noEsVacio(forma.getCodigoPaciente()) && !forma.getCodigoPaciente().equals(paciente.getCodigoPersona()) && !forma.getFuncionalidad().equals("ListProce"))
					{
						logger.info(" 	          entre 2 ====>> ");
						logger.info("Se carga el Paciente pasado por parametro. actual >> "+paciente.getCodigoPersona()+" >> nuevo "+forma.getCodigoPaciente());
						paciente.setCodigoPersona(Utilidades.convertirAEntero(forma.getCodigoPaciente()));
						con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
						UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
						forma.setPaciente(paciente);
						UtilidadBD.closeConnection(con);
					}
					else
						if (forma.getFuncionalidad().equals("ListProce") && (!UtilidadCadena.noEsVacio(forma.getPaciente().getCodigoPersona()+"") || (forma.getPaciente().getCodigoPersona()+"").equals(ConstantesBD.codigoNuncaValido+"") ))
						{
							logger.info(" 	          entre 3 ====>> ");
							Connection connection = UtilidadBD.abrirConexion();
							paciente.cargar(connection, Utilidades.convertirAEntero(forma.getCodigoPaciente()));
							int [] solicitud = UtilidadValidacion.estaPeticionAsociada(connection, Utilidades.convertirAEntero(forma.getPeticion()));
							logger.info("\n numero_sol -->"+solicitud[1]);
							String ingreso =UtilidadesHistoriaClinica.obtenerIdIngresoSolicitud(connection, (solicitud[1])+"");
							logger.info("\n ingreso -->"+ingreso);
							HashMap tmp=UtilidadesManejoPaciente.obtenerDatosIngreso(connection, ingreso, usuario.getCodigoInstitucion());
							paciente.cargarPacienteXingreso(connection, ingreso, usuario.getCodigoInstitucion(), tmp.get("centroAtencion")+"");
							logger.info("\n consecutivo ingreso -->"+paciente.getConsecutivoIngreso());
							logger.info("\n ingreso -->"+paciente.getCodigoIngreso());
							logger.info("\n fecha ingreso -->"+paciente.getFechaIngreso());
							forma.setPaciente(paciente);
							logger.info("\n ingreso -->"+paciente.getCodigoIngreso());
							logger.info("\n fecha ingreso utilidad-->"+UtilidadesManejoPaciente.obtenerFechaAperturaIngreso(connection, ingreso));
							forma.getPaciente().setFechaIngreso(UtilidadesManejoPaciente.obtenerFechaAperturaIngreso(connection, ingreso));
							UtilidadBD.closeConnection(connection);
						}


				//Validar que el usuario no se autoatienda
				ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
				if(respuesta.isTrue())
					return ComunAction.accionSalirCasoError(mapping, request, null, logger, "El paciente no puede ser autoatendido", respuesta.getDescripcion(), true);



				con=UtilidadBD.abrirConexion();			

				/**
				 * Cambio por responder cirugia ANEXO-395
				 */
				if (!forma.getFuncionalidad().equals("ListProce"))
				{
					if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.ingresoEstadoDiferenteAbierto", "errors.ingresoEstadoDiferenteAbierto", true);
					}
					if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.cuentaNoValida", "errors.paciente.cuentaNoValida", true);
					}
				}

				if (!UtilidadValidacion.esOcupacionQueRealizaCx(con, usuario.getCodigoOcupacionMedica(), usuario.getCodigoInstitucionInt()))
				{
					ArrayList<String> mensajes = new ArrayList<String>();
					mensajes.add("Profesional debe tener ocupación realiza cirugías y especialidad anestesiología, opción solo de consulta.");
					forma.setMensajes(mensajes);

				}

				forma.setPaciente(paciente);



				logger.info(" 	          paciente ES ====>> "+forma.getPaciente().getCodigoPersona());
				logger.info(" 	          peticion ES ====>> "+forma.getPeticion());
				/**********************************************************************************************************************/
				/*-------------------------------------------------------------------------------------------------------------------
				 * ESTADOS PARA LA NUEVA HOJA QUIRURGICA
				 *------------------------------------------------------------------------------------------------------------------*/
				/**********************************************************************************************************************/
				/*-----------------------------------
				 *    ESTADO ==> LISTADOPETICIONESDummy
	 	 -----------------------------------*/
				/*----------------------------------------------
				 * ESTADO =================>>>>>  CARGARHQX
				 ---------------------------------------------*/
				if (estado.equals("cargarHQxDummy"))
				{
					//Se valida si el parametro enviado desde ResponderCirugiasAction es true o false para asignar descripcion a guardar en bd
					if (request.getParameter("validacionCapitacion")!=null&&request.getParameter("validacionCapitacion").equals("true"))
					{
						forma.setValidacionCapitacion(true);
					}
					HojaQuirurgica.obtenerDatosRequest(forma, request);
					HojaQuirurgica.cargarSecGeneral(con, forma,usuario,paciente,forma.isEsDummy(),request,true);
					forma.setValidacionesMap("esModificableHoja",HojaAnestesia.validacionEsModificableHojaXMedico(con, usuario,false)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
					forma.setEsModificable(UtilidadTexto.getBoolean(forma.getValidacionesMap("esModificableHoja")+""));		

					//LLamada desde la respuesta de procedimientos
					if(forma.getFuncionalidad().equals("RespProceServ"))
					{
						HojaQuirurgica.validarDatosRequeridos(con, forma, usuario, false);
						HojaQuirurgica.cargarSecServicios(con, forma,usuario,paciente,true);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("secServicios");
					}


					// Anexo 179 - Cambio 1.50
					String tieneAutoIEstr = request.getParameter("tieneAutoIE");
					if(!UtilidadTexto.isEmpty(tieneAutoIEstr))
					{
						boolean tieneAutoIE = UtilidadTexto.getBoolean(tieneAutoIEstr);

						if(tieneAutoIE)
						{
							forma.setSinAutorizacionEntidadsubcontratada(false);
							forma.setAdvertencia(null);
						}
						else
						{
							forma.setSinAutorizacionEntidadsubcontratada(true);
							forma.setAdvertencia(new ArrayList<String>());

							String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.ordenSinAutorizacionIE");
							forma.getAdvertencia().add(mensajeConcreto);
						}
					}



					UtilidadBD.closeConnection(con); 
					return mapping.findForward("principal");			
				}
				else
					/*-----------------------------------
					 *    ESTADO ==> LISTADOPETICIONES	
			 	 -----------------------------------*/
					if(estado.equals("listadoPeticiones"))
					{
						HojaAnestesia mundo = new HojaAnestesia();

						return metodoEstadoEmpezar(con, forma, usuario,mundo, paciente, request, mapping);
					}
					else/*-----------------------------------
					 *    ESTADO ==> CREACIONORDEN	
				 	 -----------------------------------*/
						if (estado.equals("crearOrden"))
						{
							errores=HojaQuirurgica.crearSolicitud(con, forma, usuario, paciente);
							if(!errores.isEmpty())
							{

								saveErrors(request,errores);	
								forma.reset_hqx();
								forma.setListadoPeticionesMap(HojaQuirurgica.consultarPeticiones(con, usuario.getCodigoInstitucionInt(), paciente.getCodigoPersona(), paciente.getCodigoIngreso()));
								UtilidadBD.closeConnection(con);
								return mapping.findForward("listadoPeticiones");
							}	
							//se cargan los datos que se muestran en la seccion General
							HojaQuirurgica.cargarSecGeneral(con, forma,usuario,paciente,forma.isEsDummy(),request,true);
							
							/*
							 * Se realizan los cambios de la MT 6020, en este momento no se encuentran los cambios por análisis
							 * de esta manera solamente se desarrolla lo siguiente:
							 * Cuando se tiene una petición y esta se encuentra asociada a una autorización de capitación el 
							 * sistema debe asociar la autorización de capita de la petición a la nueva solicitud.
							 */
							int codPeticion = Integer.parseInt(forma.getPeticion()); 
							Map<String, Object> servicio = new HashMap<String, Object>();
							servicio = (Map<String, Object>)forma.getListadoPeticionesMap().get("servicios9_"+0);
							int codServicio = Integer.parseInt(servicio.get("codigoServicio0_"+0).toString()); 
							this.asociarAutorizacionSolicitud(codPeticion, forma.getCodSolicitud(), codServicio);
							
							UtilidadBD.closeConnection(con);
							return mapping.findForward("principal");
						}
						else /*----------------------------------------------
						 * ESTADO =================>>>>>  REDIRECCION
						 ---------------------------------------------*/
							if (estado.equals("redireccionListado"))
							{
								UtilidadBD.closeConnection(con);
								response.sendRedirect(forma.getLinkSiguiente());
								return null;
							}
							else/*----------------------------------------------
							 * ESTADO =================>>>>>  ORDENAR
							 ---------------------------------------------*/
								if (estado.equals("ordenarListado"))//estado encargado de ordenar el HashMap del listado de peticiones.
								{
									HojaQuirurgica.accionOrdenarMapa(forma);				 
									UtilidadBD.closeConnection(con);
									return mapping.findForward("listadoPeticiones");	

								}
								else/*----------------------------------------------
								 * ESTADO =================>>>>>  CARGARSECGENERAL
								 ---------------------------------------------*/
									if (estado.equals("cargarSecGeneral"))
									{
										HojaQuirurgica.cargarSecGeneral(con, forma,usuario,paciente,forma.isEsDummy(),request,true );		 
										UtilidadBD.closeConnection(con);
										return mapping.findForward("principal");	

									}
									else/*----------------------------------------------
									 * ESTADO =================>>>>> GUARDARSECGENERAL
									 ---------------------------------------------*/
										if (estado.equals("guardarSecGeneral"))
										{
											
											HojaQuirurgica.guardarSecGeneral(con, forma, mapping, usuario,paciente,forma.isEsDummy(),request,validacionCapitacion);
											UtilidadBD.closeConnection(con);
											return mapping.findForward("principal");	

										}
										else/*----------------------------------------------
										 * ESTADO =================>>>>> CARGARSECSERVICIOS
										 ---------------------------------------------*/
											if(estado.equals("volverSecServicios") || estado.equals("volverFromResumenSecServicios"))
											{
												//Actualiza el indicador de tiene Respuesta en Si para el Servicio Visto si se Guardo
												if(estado.equals("volverFromResumenSecServicios"))
												{												
													if(forma.getServicios().containsKey(indicesServicios[36]+forma.getIndexServicio()))
														forma.setServicios(indicesServicios[36]+forma.getIndexServicio(),ConstantesBD.acronimoSi);
												}

												UtilidadBD.closeConnection(con);
												return mapping.findForward("secServicios");
											}
				if (estado.equals("cargarSecServicios"))
				{
					HojaQuirurgica.validarDatosRequeridos(con, forma, usuario, false);
					HojaQuirurgica.cargarSecServicios(con, forma,usuario,paciente,true);
					forma.setAdvertencia(new ArrayList<String>());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("secServicios");
				}
				else/*----------------------------------------------
				 * ESTADO =================>>>>> CARGARSECSERVICIOS
											 ---------------------------------------------*/
					if (estado.equals("abrirProcedimientoServ"))
					{
						return HojaQuirurgica.accionAbrirProcedimientoServ(
								con,
								forma,
								usuario,
								mapping,
								response,
								request,
								paciente);										
					}

					else/*----------------------------------------------
					 * ESTADO =================>>>>> FORMATOSERVICIOS
											 ---------------------------------------------*/
						if (estado.equals("formatoServicios"))
						{

							HojaQuirurgica.formatearDatosServicios(con, forma, usuario);
							HojaQuirurgica.secServiciosConvertirFormatAP(con, forma, usuario);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("secServicios");	

						}
						else/*----------------------------------------------
						 * ESTADO =================>>>>> GUARDARSECSERVICIOS
												 ---------------------------------------------*/
							if (estado.equals("guardarSecServicios"))
							{
								errores = validarSecServicios(con, forma, usuario, request, errores);

								if(!errores.isEmpty())
								{
									saveErrors(request,errores);	
									UtilidadBD.closeConnection(con);
									return mapping.findForward("secServicios");	
								}
								//HojaQuirurgica.convertirFormatoABDDiagnosticosPostOpe(forma.getServicios());

								HojaQuirurgica.guardarSecServicios(con,request, forma, usuario,paciente, validacionCapitacion);
								request.getSession().removeAttribute("MAPAJUS");
								UtilidadBD.closeConnection(con);
								return mapping.findForward("secServicios");	

							}
							else/*----------------------------------------------
							 * ESTADO =================>>>>> ADICIONARHONORARIOS
													 ---------------------------------------------*/
								if (estado.equals("adicionarHonorarios"))
								{
									HojaQuirurgica.adicionarProfesionales(con, forma, usuario);
									UtilidadBD.closeConnection(con);
									return mapping.findForward("secServicios");	

								}
								else/*----------------------------------------------
								 * ESTADO =================>>>>> FILTRARPROFESIONALES
														 ---------------------------------------------*/
									if (estado.equals("filtrarProfesional"))
									{
										HojaQuirurgica.filtarProfesionalesXEspecialidad(con, forma, usuario);
										UtilidadBD.closeConnection(con);
										return mapping.findForward("secServicios");	

									}
									else/*----------------------------------------------
									 * ESTADO =================>>>>> FILTRARESPECIALIDAD
															 ---------------------------------------------*/
										if (estado.equals("filtrarEspecialidad"))
										{
											HojaQuirurgica.filtarEspecialidadXProfesional(con, forma);
											UtilidadBD.closeConnection(con);
											return mapping.findForward("secServicios");	

										}
										else/*----------------------------------------------
										 * ESTADO =================>>>>> ELIMINARPROFECIONAL
																 ---------------------------------------------*/
											if (estado.equals("eliminarProfecional"))
											{
												HojaQuirurgica.eliminarProfecional(con,forma, Integer.parseInt(forma.getIndexServicio()), Integer.parseInt(forma.getIndexprofesional()),usuario);
												UtilidadBD.closeConnection(con);
												return mapping.findForward("secServicios");	

											}
											else/*----------------------------------------------
											 * ESTADO =================>>>>> FILTRARESPINTERVIENE
																	 ---------------------------------------------*/
												if (estado.equals("filtrarEspInterviene"))
												{
													HojaQuirurgica.limpiarDependenciasEspeInter(con, forma, usuario);
													UtilidadBD.closeConnection(con);
													return mapping.findForward("secServicios");	

												}
												else/*----------------------------------------------
												 * ESTADO =================>>>>> ELIMINARSERVICIO
																		 ---------------------------------------------*/
													if (estado.equals("eliminarServicio"))
													{
														HojaQuirurgica.eliminarServicio(forma);
														UtilidadBD.closeConnection(con);
														return mapping.findForward("secServicios");	

													}
													else if(estado.equals("eliminarServicioConInfoParto")){
														HojaQuirurgica.eliminarServicio(forma);
														HojaQuirurgica.guardarServicios(con, request, forma, usuario, paciente);
														
														HojaQuirurgica.validarDatosRequeridos(con, forma, usuario, false);
														HojaQuirurgica.cargarSecServicios(con, forma,usuario,paciente,true);
														forma.setAdvertencia(new ArrayList<String>());
														
														UtilidadBD.closeConnection(con);
														return mapping.findForward("secServicios");
													}
													else/*----------------------------------------------
													 * ESTADO =================>>>>> CARGARSECINFOQX
																			 ---------------------------------------------*/
														if (estado.equals("cargarSecInfoQx"))
														{


															HojaQuirurgica.cargarSecInfoQx(con, forma,usuario,paciente,forma.isEsDummy(),true,response);
															HojaQuirurgica.validarDatosRequeridos(con,forma, usuario,true);
															logger.info("numero de mensajes añadidos: "+forma.getSizeMensajes());
															UtilidadBD.closeConnection(con);
															return mapping.findForward("secInfoQx");	

														}
														else/*----------------------------------------------
														 * ESTADO =================>>>>> FILTRARSALAS
																				 ---------------------------------------------*/
															if (estado.equals("filtrarSalas"))
															{
																HojaQuirurgica.accionFiltrarSalas(con,forma,usuario,response);
																UtilidadBD.closeConnection(con);
																return null; 
															}
				/////anexo 777
															else if(estado.equals("filtrarMedicoSala"))
															{
																return HojaQuirurgica.accionFiltrarMedicoSala(con, forma, response);
															}
															else /*----------------------------------------------
															 * ESTADO =================>>>>> ADCIONARPROFINFOQX
																					 ---------------------------------------------*/
																if (estado.equals("adicionarProfInfoQx"))
																{
																	HojaQuirurgica.adicionarProfesionalInfoQx(forma);
																	UtilidadBD.closeConnection(con);
																	return mapping.findForward("secInfoQx");
																}
																else /*----------------------------------------------
																 * ESTADO =================>>>>> CARGARSECPATOLOGIA
																						 ---------------------------------------------*/
																	if (estado.equals("cargarSecPatologia"))
																	{
																		HojaQuirurgica.cargarSecPatologia(con, forma,usuario,paciente,true);
																		UtilidadBD.closeConnection(con);
																		return mapping.findForward("secPatologia");
																	}
																	else /*----------------------------------------------
																	 * ESTADO =================>>>>> CARGARSECOTROSPROF
																							 ---------------------------------------------*/
																		if (estado.equals("cargarSecOtrosProf"))
																		{
																			HojaQuirurgica.cargarSecOtrosProfesionales(con, forma, usuario,paciente,true);
																			UtilidadBD.closeConnection(con);
																			return mapping.findForward("secOtrosProf");
																		}
																		else /*----------------------------------------------
																		 * ESTADO =================>>>>> CARGARSECNOTASACLA
																								 ---------------------------------------------*/
																			if (estado.equals("cargarSecNotasAcla"))
																			{
																				HojaQuirurgica.cargarSecNotasAcla(con, forma,usuario,paciente,true);
																				UtilidadBD.closeConnection(con);
																				return mapping.findForward("secNotasAcla");
																			}
																			else /*----------------------------------------------
																			 * ESTADO =================>>>>> CARGARSECOBSGENERALES
																									 ---------------------------------------------*/
																				if (estado.equals("cargarSecObsGenerales"))
																				{

																					HojaQuirurgica.cargarSecObsGenerales(con, forma,usuario,paciente,true);
																					UtilidadBD.closeConnection(con);
																					return mapping.findForward("secObsGenerales");
																				}
																				else /*----------------------------------------------
																				 * ESTADO =================>>>>> GUARDARNOTASACLA
																										 ---------------------------------------------*/
																					if (estado.equals("guardarNotasAcla"))
																					{

																						HojaQuirurgica.guardarSecNotasAcla(con, forma, usuario,paciente,validacionCapitacion);
																						UtilidadBD.closeConnection(con);
																						return mapping.findForward("secNotasAcla");
																					}
																					else /*----------------------------------------------
																					 * ESTADO =================>>>>> GUARDARPATOLOGIA
																											 ---------------------------------------------*/
																						if (estado.equals("guardarPatologia"))
																						{

																							HojaQuirurgica.guardarSecPatologia(con, forma, usuario,paciente, validacionCapitacion);
																							UtilidadBD.closeConnection(con);
																							return mapping.findForward("secPatologia");
																						}

																						else /*----------------------------------------------
																						 * ESTADO =================>>>>> GUARDAROBSERVACIONESGENERALES
																												 ---------------------------------------------*/
																							if (estado.equals("guardarObsGen"))
																							{

																								HojaQuirurgica.guardarSecObsGen(con, forma, usuario,paciente, validacionCapitacion);
																								UtilidadBD.closeConnection(con);
																								return mapping.findForward("secObsGenerales");
																							}
																							else /*----------------------------------------------
																							 * ESTADO =================>>>>> GUARDARINFOQX
																													 ---------------------------------------------*/
																								if (estado.equals("guardarInfoQx"))
																								{
																									errores=validarInfoQx(con, forma, usuario, request, errores,paciente);
																									if(!errores.isEmpty())
																									{
																										saveErrors(request,errores);	
																										UtilidadBD.closeConnection(con);
																										return mapping.findForward("secInfoQx");	
																									}
																									//FIXME
																									ArrayList<String> listaMensajes=HojaQuirurgica.guardarSecInfoQx(con, forma, usuario,paciente,forma.isEsDummy(),response, validacionCapitacion);
																									UtilidadBD.closeConnection(con);
																									forma.setListaMensajes(listaMensajes);
																									return mapping.findForward("secInfoQx");
																								}
																								else 
																									/*----------------------------------------------
																								 * ESTADO =================>>>>> reversarInfoQx
																														 ---------------------------------------------*/
																									if(estado.equals("reversarInfoQx")){
																										forma.setParamGenerSecServicos(indicesParamGenerales[7], HojaQuirurgica.reversarInfoQx(con,forma,usuario));
																										return mapping.findForward("secInfoQx");
																									}else											
																									
																									/*----------------------------------------------
																								 * ESTADO =================>>>>> eliminarProfInfoQx
																														 ---------------------------------------------*/
																									if (estado.equals("eliminarProfInfoQx"))
																									{

																										HojaQuirurgica.eliminarCampoProfInfoQx(forma);
																										UtilidadBD.closeConnection(con);
																										return mapping.findForward("secInfoQx");
																									}
																									else /*----------------------------------------------
																									 * ESTADO =================>>>>> FILTRAROTROSPROFESIONALES
																															 ---------------------------------------------*/
																										if (estado.equals("filtrarOtrosProfesionales"))
																										{

																											HojaQuirurgica.filtrarEspecialidadXOtrosProfesionales(con, forma);
																											UtilidadBD.closeConnection(con);
																											return mapping.findForward("secOtrosProf");
																										}
																										else /*----------------------------------------------
																										 * ESTADO =================>>>>> FILTRAROTROSPROFESIONALES
																																 ---------------------------------------------*/
																											if (estado.equals("adicionarOtroProf"))
																											{

																												HojaQuirurgica.addOtrosProfesionales(forma);
																												UtilidadBD.closeConnection(con);
																												return mapping.findForward("secOtrosProf");
																											}
																											else /*----------------------------------------------
																											 * ESTADO =================>>>>> ELIMINAROTROSPROF
																																	 ---------------------------------------------*/
																												if (estado.equals("eliminarOtrosProf"))
																												{

																													HojaQuirurgica.eliminarOtrosProfesionales(forma);
																													UtilidadBD.closeConnection(con);
																													return mapping.findForward("secOtrosProf");
																												}
																												else /*----------------------------------------------
																												 * ESTADO =================>>>>> GUARDARSECOTROSPROF
																																		 ---------------------------------------------*/
																													if (estado.equals("guardarSecOtrosProf"))
																													{
																														errores=validarOtrosprofesionales(con, forma, usuario, request, errores);
																														if(!errores.isEmpty())
																														{
																															saveErrors(request,errores);	
																															UtilidadBD.closeConnection(con);
																															return mapping.findForward("secOtrosProf");	
																														}
																														HojaQuirurgica.guardarSecOtrosProf(con, forma, usuario,paciente, validacionCapitacion);
																														UtilidadBD.closeConnection(con);
																														return mapping.findForward("secOtrosProf");
																													}
																													else /*----------------------------------------------
																													 * ESTADO =================>>>>> ADICIONARJUS
																																			 ---------------------------------------------*/
																														if (estado.equals("adicionarJus"))
																														{
																															logger.info("____________mapa para subir "+forma.getJustificacionesServicios());
																															request.getSession().setAttribute("MAPAJUS", forma.getJustificacionesServicios());
																															UtilidadBD.cerrarConexion(con);
																															return mapping.findForward("secServicios");
																														}
																														else /*----------------------------------------------
																														 * ESTADO =================>>>>> FILTRARSALIDAPACIENTE
																																				 ---------------------------------------------*/
																															if (estado.equals("filtrarSalidaPaciente"))
																															{
																																forma.setSalidaPaciente(HojaQuirurgica.cargarSelectSalidaPaciente(con, usuario, UtilidadTexto.getBoolean(forma.getSalPac(indicesSalidaSala[8])+"")));
																																return mapping.findForward("secInfoQx");
																															}
																															else /*----------------------------------------------
																															 * ESTADO =================>>>>> IMPRIMIRHQX
																																					 ---------------------------------------------*/
																																if (estado.equals("imprimirHqx"))
																																{
																																	return HojaQuirurgica.imprimirHqx(con, forma, request, paciente, usuario, mapping,response);
																																	//return mapping.findForward("imprimir");
																																}
																																else /*----------------------------------------------
																																	 * ESTADO =================>>>>> cargarValoresPostulacionDiagnosticos
																																							 ---------------------------------------------*/
																																		if (estado.equals("cargarValoresPostulacionDiagnosticos"))
																																		{
																																			HojaQuirurgica.cargarValoresPostulados(forma);
																																			return mapping.findForward("secServicios");
																																		}
				/*********************************************************************************************************************************************************************************************************************/
				/*-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
				 * ESTADOS PARA LA NUEVA HOJA QUIRURGICA
				 *-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
				/************************************************************************************************************************************************************************************************************************/
																																else
																																	return ComunAction.accionSalirCasoError(mapping, request, null, logger, "La accion especÃ­ficada no esta definida", "errors.estadoInvalido", true);


			}
			else
			{
				return ComunAction.accionSalirCasoError(mapping, request, null, logger, "Forma Inválida", "errors.formaTipoInvalido", true);
			}
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(), e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}

	
	


/**********************************************************************************************************************/
/*-------------------------------------------------------------------------------------------------------------------
 * METODOS PARA LA NUEVA HOJA QUIRURGICA
 *------------------------------------------------------------------------------------------------------------------*/
/*********************************************************************************************************************
	
	/**
	 * Operciones del estado Empezar  
	 * @param Connection con
	 * @param HojaAnestesiaForm forma
	 * @param UsuarioBasico usuario
	 * @param HojaAnestesia mundo
	 * @param PersonaBasica paciente
	 * @return ActionErrors
	 * */
	@SuppressWarnings("unchecked")
	private ActionForward metodoEstadoEmpezar(
			Connection con,
			HojaQuirurgicaForm forma,
			UsuarioBasico usuario,
			HojaAnestesia mundo,
			PersonaBasica paciente,
			HttpServletRequest request,
			ActionMapping mapping)
	{
		//Incializa los atributos de la forma 
		forma.reset_hqx();
		 
		//los valores enviados por request
		//---------------------------------------------------------------
		HojaQuirurgica.obtenerDatosRequest(forma, request);
		//---------------------------------------------------------------
		logger.info("\n ********************** estoy listando las peticiones --> "+forma.getPeticion());
		 //Evalua el ingreso a la funcionalidad 
		 if(mundo.validacionRolFuncionalidad(con,usuario,671,"Hoja Quirúrgica"))
		 {
			 //Evalua requerimientos para el paciente y medico
			 if(!mundo.validacionIngresoFuncional(con,paciente,usuario))					 
				 saveErrors(request,mundo.getErrores());				
			 else
			 {
				 //Evalua si el usuario puede modificar o no la hoja quirurgica			 
				 forma.setValidacionesMap("esModificableHoja",mundo.validacionEsModificableHojaXMedico(con, usuario,false)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				 forma.setEsModificable(UtilidadTexto.getBoolean(forma.getValidacionesMap("esModificableHoja")+""));
				 logger.info("\n ********************** 1 *************** esDummy--> "+forma.isEsDummy() +" es modificable -->"+forma.isEsModificable());
				 if(!forma.isEsDummy())
				 {
					 logger.info("\n ********************** 2 ***************");
					 //Almacena la informacion del listado de peticiones por paciente
					 forma.setListadoPeticionesMap(HojaQuirurgica.consultarPeticiones(con,usuario.getCodigoInstitucionInt(),paciente.getCodigoPersona(),paciente.getCodigoIngreso()));
				 }
				 else
				 {
					 logger.info("\n ********************** 3 ***************");
					 if (forma.getFuncionalidad().equals("RespProce") || forma.getFuncionalidad().equals("RespCx"))
					 {
						 logger.info("\n ********************** 4 ***************");
						 
						 HashMap pet=HojaQuirurgica.consultarDatosPeticion(con, forma.getPeticion());
						 logger.info("los datos de la peticion "+pet);
						 HashMap dato = new HashMap ();
						 dato.put("numRegistros", 0);
						 HashMap peticiones = new  HashMap ();
						
						 peticiones.put("codigoPeticion2_0", forma.getPeticion());
						 dato=HojaQuirurgica.consultarServiciosPeticion(con, forma.getPeticion());
						 peticiones.put("servicios9_0", dato);
						 peticiones.put("esAsociado10_0", ConstantesBD.acronimoNo);
						 peticiones.put("fechaCirugia3_0", pet.get("fechaCirugia1"));
						 peticiones.put("solicitante7_0", pet.get("solicitante4"));
						 peticiones.put("codigoSolicitante11_0", pet.get("codSolicitante6"));
						 
						 forma.setIndexPeticion("0");
						 peticiones.put("numRegistros", "1");
						
						 
						 forma.setListadoPeticionesMap(peticiones);
						 logger.info("los datos de la peticion en la forma "+forma.getListadoPeticionesMap());
						 HojaQuirurgica.cargarselects(con, forma, usuario, paciente);
						 HojaQuirurgica.initSolicitud(forma);
						 
						UtilidadBD.closeConnection(con);	 
						return mapping.findForward("listadoPeticiones");	 	
					 }
				 }
				 //Si solo existe un registro y la peticion es valida, ingresa directamente a la Hoja de Quirurgica
				 if(forma.getListadoPeticionesMap("numRegistros").toString().equals("1"))
				 {
					 logger.info("\n ********************** 5 ***************");
					 //coloca el indice del servicio
					 forma.setIndexPeticion("0");
					 if(forma.getListadoPeticionesMap("esAsociado10_0").toString().equals(ConstantesBD.acronimoSi))
					 {
						 HojaQuirurgica.cargarSecGeneral(con, forma,usuario,paciente,forma.isEsDummy(),request,true);
						 return mapping.findForward("principal");
					 }
					
				 }
				//si las peticiones son muchas se cargan los valores de centro de costo
				 //y especialidades
				 HojaQuirurgica.cargarselects(con, forma, usuario, paciente);
				 HojaQuirurgica.initSolicitud(forma);
			 }					 
		 
			 String[] datosSolicitud = UtilidadValidacion.existeSolicitudValoracionCuenta(con, paciente);
		
		 if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		 {
			
			 //Valoraciones.valoracionDao().cargarDiagnosticosValoracion(con, datosSolicitud[0]);
			 HojaQuirurgica.consultarDiagnosticosValoracionPPal ( con,datosSolicitud[0]);
			 
		 }
		 else if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
		 {
			
			 //Valoraciones.valoracionDao().cargarHospitalizacion(con, datosSolicitud[0]);
			 HojaQuirurgica.consultarDiagnosticosHospitalizacion(con, datosSolicitud[0]);
		 }
		 
		 }
		 else	
			 saveErrors(request,mundo.getErrores());
		 
		 UtilidadBD.closeConnection(con);	 
		 
		 return mapping.findForward("listadoPeticiones");	 			
	}
	
	
	
	/**
	 * Metodo encargado de validar la seccion de servicios
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param errores
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionErrors validarSecServicios(Connection connection, HojaQuirurgicaForm forma,	UsuarioBasico usuario,HttpServletRequest request,ActionErrors errores)
	{
		logger.info("\n%%%%%%%%%%%%ENTRE A VALIDAR SERVICIOS %%%%%%%%%%%%%%%%%%% mapa servicios --> ");//forma.getServicios());
		forma.setAdvertencia(new ArrayList<String>());
		
		if (forma.getEstado().equals("guardarSecServicios"))
		{
			//cantidad de servicios1
			int numReg = Integer.parseInt(forma.getServicios("numRegistros")+"");
			String asocioCirugia = ValoresPorDefecto.getAsocioCirujano(usuario.getCodigoInstitucionInt());
			
			if(Utilidades.convertirAEntero(asocioCirugia)<=0)
			{
				errores.add("descripcion",new ActionMessage("errors.required","El Valor X Defecto del Asocio de Cirujano"));
			}
			else
			{	
				int totServDisp=0;
				boolean existeServicioCx=false;
				
				for(int i=0;i<numReg;i++)
				{
					if (!(forma.getServicios(indicesServicios[25]+i)+"").equals(ConstantesBD.acronimoEliminar))
					{
						logger.info("el tipo de servicio es --> "+forma.getServicios(indicesServicios[5]+i));
						if ((forma.getServicios(indicesServicios[5]+i)+"").equals(ConstantesBD.codigoServicioQuirurgico+"") || (forma.getServicios(indicesServicios[5]+i)+"").equals(ConstantesBD.codigoServicioPartosCesarea+""))
							existeServicioCx=true;
						
						totServDisp++;
						//1)epscialidad que interviene
						
						if (!forma.getServicios(indicesServicios[25]+i).equals(ConstantesBD.acronimoEliminar))
						{
							if ((forma.getServicios(indicesServicios[19]+i)+"").equals("") || (forma.getServicios(indicesServicios[19]+i)+"").equals("-1"))
								forma.getAdvertencia().add("Falta La Especialidad que Interviene del servicio "+totServDisp);
								//errores.add("descripcion",new ActionMessage("errors.required","La Especialidad que Interviene del servicio "+totServDisp));		
					
						//2) via
						if (numReg>1)
							if ((forma.getServicios(indicesServicios[16]+i)+"").equals("") || (forma.getServicios(indicesServicios[16]+i)+"").equals("-1") || (forma.getServicios(indicesServicios[16]+i)+"").equals("null"))
								forma.getAdvertencia().add("Falta La Vía del servicio "+totServDisp);
								//errores.add("descripcion",new ActionMessage("errors.required","La VÃ­a del servicio "+totServDisp));
					
					
						//3) profesional cirujano
						//se deben recorrer los profesionales
						HashMap profesionales = new HashMap ();
						profesionales=Listado.obtenerMapaInterno(forma.getServicios(), indicesServicios[10]+i+"_");
						int numProf = Integer.parseInt(profesionales.get("numRegistros")+"");
						
						//logger.info("\n\n ****************************************profesionales --> "+profesionales);
						boolean ban=false;
						if (numProf>0)
						{
							//Esta variable es para el control de lo profesionales
							boolean ban2=true;
							for (int p=0;p<numProf;p++)
							{
								
								// se verifica los profesionales que no se halla eliminado
								if (!(profesionales.get(indicesProfesionales[13]+p)+"").equals(ConstantesBD.acronimoSi))
								{
									//se valida que el codigo del profesional venga
									if(!UtilidadCadena.noEsVacio(profesionales.get(indicesProfesionales[3]+p)+"") || (profesionales.get(indicesProfesionales[3]+p)+"").equals(ConstantesBD.codigoNuncaValido+""))
										forma.getAdvertencia().add("Falta La Vía del servicio "+totServDisp);
										//errores.add("descripcion",new ActionMessage("errors.required","Seleccionar el profesional "));
									
									//se valida que la especial del profesional venga
									if(!UtilidadCadena.noEsVacio(profesionales.get(indicesProfesionales[2]+p)+"") || (profesionales.get(indicesProfesionales[2]+p)+"").equals(ConstantesBD.codigoNuncaValido+""))
										forma.getAdvertencia().add("Falta Seleccionar la especialidad del Profesional");
										//errores.add("descripcion",new ActionMessage("errors.required","Seleccionar la especialidad del Profesional "));
								
									//se verifica que exista el asocio de cirujano
									if ((profesionales.get(indicesProfesionales[1]+p)+"").equals(asocioCirugia))
										ban=true;
									
									/*********************************************************************************************************
									 * Modificado por tarea 50915 puesta por German Aguilera
									 * Se solicita que los profesionales no pueden ir repetidos.
									 */
										for (int j=0;j<numProf;j++)
											if (j!=p && ban2 && !(profesionales.get(indicesProfesionales[13]+j)+"").equals(ConstantesBD.acronimoSi))
												if ((profesionales.get(indicesProfesionales[3]+p)+"").equals((profesionales.get(indicesProfesionales[3]+j)+"")))
												{
													errores.add("descripcion",new ActionMessage("prompt.generico"," No se pueden ingresar Profesionales Repetidos"));
													ban2=false;
												}
									/*
									 * 
									 **********************************************************************************************************/
								}
							}
							if (!ban)
								forma.getAdvertencia().add("Falta El Tipo de Honorario "+Utilidades.obtenerNombreTipoAsocio(connection,Utilidades.convertirAEntero(asocioCirugia))[0]+" del servicio "+totServDisp);
								//errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Honorario "+Utilidades.obtenerNombreTipoAsocio(connection,Utilidades.convertirAEntero(asocioCirugia))[0]+" del servicio "+totServDisp));
						}
						else
						{
							String[] cadenaAdv=Utilidades.obtenerNombreTipoAsocio(connection,Utilidades.convertirAEntero(asocioCirugia));
							logger.info("---"+cadenaAdv+"-->"+cadenaAdv.length);
							forma.getAdvertencia().add("Falta El Tipo de Honorario "+"-------------------"+" del servicio "+totServDisp);
							//errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Honorario "+Utilidades.obtenerNombreTipoAsocio(connection,Utilidades.convertirAEntero(asocioCirugia))[0]+" del servicio "+totServDisp));
						}
						
						//4)diagnosticos postoperatorios
						
							HashMap diagnosticos = new HashMap ();
							diagnosticos=Listado.obtenerMapaInterno(forma.getServicios(), indicesServicios[11]+i+"_");
							
							//logger.info("diagnosticos --> "+diagnosticos);
							//se pregunta si la solicitud es de tipo No Cruento
							if ((forma.getSolicitudMap(indicesSolicitud[14]+"_0")+"").equals(ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento))
							{
								//se valida si el diagnostico principal es requerido para No Cruento
								if ((forma.getServicios(indicesServicios[27]+i)+"").equals(ConstantesBD.acronimoSi))
									if (diagnosticos.get(indicesDiagPostOpera[3]).equals("") || diagnosticos.get(indicesDiagPostOpera[3]).equals("-1") || diagnosticos.get(indicesDiagPostOpera[3]).equals("null"))
										forma.getAdvertencia().add("Falta El Diagnóstico Principal del servicio "+totServDisp);
										//errores.add("descripcion",new ActionMessage("errors.required","El DiagnÃ³stico Principal del servicio "+totServDisp));
							
								//5)validar si es requerida la descripcion por especialidad de Cx
								if ((forma.getParamGenerSecServicos(indicesParamGenerales[9])+"").equals(ConstantesBD.acronimoSi))
									if ((forma.getServicios(indicesServicios[23]+i)+"").equals("") && (forma.getServicios(indicesServicios[24]+i)+"").equals(""))
										forma.getAdvertencia().add("Falta La descripción Quirúrgica del servicio "+totServDisp);
										//errores.add("descripcion",new ActionMessage("errors.required","La descripciÃ³n QuirÃºrgica del servicio "+totServDisp));					
								
							}
							else
							{
								//5)validar si es requerida la descripcion por especialidad de Cx
								if ((forma.getParamGenerSecServicos(indicesParamGenerales[8])+"").equals(ConstantesBD.acronimoSi))
									if ((forma.getServicios(indicesServicios[23]+i)+"").equals("") && (forma.getServicios(indicesServicios[24]+i)+"").equals(""))
										forma.getAdvertencia().add("Falta La descripción Quirúrgica del servicio "+totServDisp);
										//errores.add("descripcion",new ActionMessage("errors.required","La descripciÃ³n QuirÃºrgica del servicio "+totServDisp));		
								
								//se valida el diagnostico principal pues es requerido para Cirugias
								if (diagnosticos.get(indicesDiagPostOpera[3]).equals("") || diagnosticos.get(indicesDiagPostOpera[3]).equals("-1") || diagnosticos.get(indicesDiagPostOpera[3]).equals("null"))
									forma.getAdvertencia().add("Falta El Diagnóstico Principal del servicio "+totServDisp);
									//errores.add("descripcion",new ActionMessage("errors.required","El DiagnÃ³stico Principal del servicio "+totServDisp));
							}
							
						//6)SE VERIFICA SI SE HA INGRESADO JUSTIFICACIÒN NO POS PARA EL SERVICIO EN CASO DE QUE LO REQUIERA
							if (forma.getServicios(indicesServicios[31]+i) != null){
					            if (forma.getServicios(indicesServicios[31]+i).equals("true")){
					                if(!forma.getJustificacionesServicios().containsKey(i+"_servicio"))
					                	forma.getAdvertencia().add("Falta Justificación No POS para el servicio "+totServDisp+" codigo "+forma.getServicios(indicesServicios[6]+i));
					                	//errores.add("No se ha diligenciado el formato de justificaciÃ³n No POS", new ActionMessage("errors.required","JustificaciÃ³n No POS para el servicio "+totServDisp+" codigo "+forma.getServicios(indicesServicios[6]+i)));
					            }
							}
						}
					}
				}
			
				if (numReg<1)
					errores.add("descripcion",new ActionMessage("errors.required","Por lo menos un Servicio "));
				else
					if ((forma.getSolicitudMap(indicesSolicitud[14]+"_0")+"").equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia))	
					{
						logger.info("entre a validar  existe cx --> "+existeServicioCx);
						if (!existeServicioCx)
							errores.add("descripcion",new ActionMessage("errors.required","Por lo menos un Servicio Quirurgico o Partos/Cesáreas "));
					}
			}
		}
		
		
		return errores;
	}
	
	
	
	/**
	 * Metodo encargado de validar la secion de informacion quirurgica
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param errores
	 * @param paciente
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ActionErrors validarInfoQx (Connection connection, HojaQuirurgicaForm forma,	UsuarioBasico usuario,HttpServletRequest request,ActionErrors errores,PersonaBasica paciente)
	{
		logger.info("\n%%%%%%%%%%%%ENTRE A VALIDAR INFORMACION QX %%%%%%%%%%%%%%%%%%% ");
		if (forma.getEstado().equals("guardarInfoQx") )
		{
			logger.info("\n la salida de la sala es -->"+UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[4])+"") +" --- "+forma.getSalPac(indicesSalidaSala[4]));
			if ((UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[4])+"") || UtilidadTexto.getBoolean(forma.getSecInfoQx(indicesSecInfoQx[7])+"")))
			{
				//aqui va
				
				/*****************************************************************************************************
					Validar fechas de inico y final  de la cirugia		
				*****************************************************************************************************/
				String hora=UtilidadFecha.getHoraActual();
				String fecha=UtilidadFecha.getFechaActual();
				
			
				@SuppressWarnings("unused")
				boolean validSala=true;
				
				
				// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%% se valida la fecha/hora inicial %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
				boolean valid=true;
				if ((forma.getSecInfoQx(indicesSecInfoQx[7])+"").equals(ConstantesBD.acronimoNo))
				{
					
					if ((forma.getFechasCx(indicesFechasCx[0])+"").equals(""))
					{
						errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial "));	
						valid=false;
					}
					else
						if(!UtilidadFecha.validarFecha(forma.getFechasCx(indicesFechasCx[0])+""))
						{
							errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido",forma.getFechasCx(indicesFechasCx[0])+""));
							valid=false;
						}
					
					if ((forma.getFechasCx(indicesFechasCx[1])+"").equals(""))
					{
						errores.add("descripcion",new ActionMessage("errors.required","La Hora Inicial "));
						valid=false;
					}
					else				
						if (!UtilidadFecha.validacionHora(forma.getFechasCx(indicesFechasCx[1])+"").puedoSeguir)
						{
							errores.add("descripcion",new ActionMessage("errors.formatoHoraInvalido",forma.getFechasCx(indicesFechasCx[1])+""));
							valid=false;
						}
				
					
					if (valid)
						if (!UtilidadFecha.compararFechas(fecha, hora,forma.getFechasCx(indicesFechasCx[0])+"",forma.getFechasCx(indicesFechasCx[1])+"").isTrue())
						{
							errores.add("descripcion",new ActionMessage("errors.fechaHoraPosteriorIgualActual","inicial de la cirugia: "+ forma.getFechasCx(indicesFechasCx[0])+" / "+forma.getFechasCx(indicesFechasCx[1]),"del sistema: "+fecha+" / "+hora));
							validSala=false;	
						}
						else
							if (!UtilidadFecha.compararFechas(forma.getFechasCx(indicesFechasCx[0])+"" ,forma.getFechasCx(indicesFechasCx[1])+"",forma.getFechasCx(indicesFechasCx[8])+"", forma.getFechasCx(indicesFechasCx[9])+"").isTrue())
							{
									errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorIgualActual","inicial de la cirugia: "+forma.getFechasCx(indicesFechasCx[0])+"  "+forma.getFechasCx(indicesFechasCx[1]),"de la petición: "+forma.getFechasCx(indicesFechasCx[8])+""+ forma.getFechasCx(indicesFechasCx[9])+""));
									validSala=false;
							}
					
					logger.info("\n***************************** fechas ***************** "+forma.getFechasCx());
					//	%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% se valida la fecha/hora final %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
					
					
					@SuppressWarnings("unused")
					boolean valid2=true;
					
					
					//modificado por anexo 728
					if((!forma.getFuncionalidad().equals("RespProce")) || (forma.getParamGenerSecServicos(indicesParamGenerales[18])+"").equals(ConstantesBD.acronimoNo))
					{
						//se valida la que la fecha final esta vacia
						if (!UtilidadCadena.noEsVacio(forma.getFechasCx(indicesFechasCx[2])+""))
						{
							errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final "));	
							valid2=false;
						}
						else//se valida el formato de la fecha final
							if(!UtilidadFecha.validarFecha(forma.getFechasCx(indicesFechasCx[2])+""))
							{
								errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido",forma.getFechasCx(indicesFechasCx[2])+""));
								valid2=false;
							}
						//se valida la hora final
						if (!UtilidadCadena.noEsVacio(forma.getFechasCx(indicesFechasCx[3])+""))
						{
							errores.add("descripcion",new ActionMessage("errors.required","La Hora Final "));
							valid2=false;
						}
						else				
							if (!UtilidadFecha.validacionHora(forma.getFechasCx(indicesFechasCx[3])+"").puedoSeguir)
							{
								errores.add("descripcion",new ActionMessage("errors.formatoHoraInvalido",forma.getFechasCx(indicesFechasCx[3])+""));
								valid2=false;
							}
						
						
					}
					else
					{
						//modificado por docuemento 728 
						if (!UtilidadCadena.noEsVacio(forma.getFechasCx(indicesFechasCx[2])+""))
							forma.setFechasCx(indicesFechasCx[2],fecha);
						if (!UtilidadCadena.noEsVacio(forma.getFechasCx(indicesFechasCx[3])+""))
							forma.setFechasCx(indicesFechasCx[3],hora);
						
						String dura=UtilidadFecha.calcularDuracionEntreFechas(forma.getFechasCx(indicesFechasCx[0])+"", forma.getFechasCx(indicesFechasCx[1])+"", forma.getFechasCx(indicesFechasCx[2])+"", forma.getFechasCx(indicesFechasCx[3])+"");
						logger.info("\n duracion de la cirugia --> "+dura);
						if (UtilidadCadena.noEsVacio(dura))
							forma.setFechasCx(indicesFechasCx[10],dura);
						else
							forma.setFechasCx(indicesFechasCx[10],"00:00");
							
					}
					
						if (valid)
							if (!UtilidadFecha.compararFechas(fecha, hora,forma.getFechasCx(indicesFechasCx[2])+"",forma.getFechasCx(indicesFechasCx[3])+"").isTrue())
							{
								errores.add("descripcion",new ActionMessage("errors.fechaHoraPosteriorIgualActual","Final de la cirugia: "+ forma.getFechasCx(indicesFechasCx[2])+" / "+forma.getFechasCx(indicesFechasCx[3]),"del sistema: "+fecha+" / "+hora));
								validSala=false;
							}
							else
								if (!UtilidadFecha.compararFechas(forma.getFechasCx(indicesFechasCx[2])+"" ,forma.getFechasCx(indicesFechasCx[3])+"",forma.getFechasCx(indicesFechasCx[0])+"", forma.getFechasCx(indicesFechasCx[1])+"").isTrue())
								{
										errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorIgualActual","Final de la cirugia: "+forma.getFechasCx(indicesFechasCx[2])+"  "+forma.getFechasCx(indicesFechasCx[3])," Inicial de la cirugia: "+forma.getFechasCx(indicesFechasCx[0])+" / "+ forma.getFechasCx(indicesFechasCx[1])+""));
										validSala=false;
								}					
						
						//logger.info("entre a comprobar la fecha inicial fecha incial-->"+forma.getFechasCx(indicesFechasCx[0])+" Hora inicial --> "+forma.getFechasCx(indicesFechasCx[1])+" facha ingreso -->"+forma.getSalPac(indicesSalidaSala[0])+" Hora ingreso -->"+forma.getSalPac(indicesSalidaSala[1])); 
						//se valida que la fecha y hora del ingreso de la sala sea menor que la la fecha y hora inicial de la cirugia
						if (UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[0])+"") && UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[1])+"") && 
							UtilidadCadena.noEsVacio(forma.getFechasCx(indicesFechasCx[0])+"") && UtilidadCadena.noEsVacio(forma.getFechasCx(indicesFechasCx[1])+""))
						{
							//logger.info("entre a comprobar la fecha inicial fecha incial-->"+forma.getFechasCx(indicesFechasCx[0])+" Hora inicial --> "+forma.getFechasCx(indicesFechasCx[1])+" facha ingreso -->"+forma.getSalPac(indicesSalidaSala[0])+" Hora ingreso -->"+forma.getSalPac(indicesSalidaSala[1])+" el resultado es "+UtilidadFecha.compararFechas(forma.getFechasCx(indicesFechasCx[0])+"" ,forma.getFechasCx(indicesFechasCx[1])+"",forma.getSalPac(indicesSalidaSala[0])+"", forma.getSalPac(indicesSalidaSala[1])+"").isTrue());
							if (!UtilidadFecha.compararFechas(forma.getFechasCx(indicesFechasCx[0])+"" ,forma.getFechasCx(indicesFechasCx[1])+"",forma.getSalPac(indicesSalidaSala[0])+"", forma.getSalPac(indicesSalidaSala[1])+"").isTrue())
							{
									errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorIgualActual","inicial de la cirugia: "+forma.getFechasCx(indicesFechasCx[0])+" - "+forma.getFechasCx(indicesFechasCx[1]),"del ingreso a la sala: "+forma.getSalPac(indicesSalidaSala[0])+" - "+ forma.getSalPac(indicesSalidaSala[1])+""));
								
							}
						}
						
						
						//se valida que la fecha y hora de la salida de la sala sea menor que la la fecha y hora final de la cirugia
						if (UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[2])+"") && UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[3])+"") &&
							UtilidadCadena.noEsVacio(forma.getFechasCx(indicesFechasCx[2])+"") && UtilidadCadena.noEsVacio(forma.getFechasCx(indicesFechasCx[3])+""))
						{
							if (!UtilidadFecha.compararFechas(forma.getSalPac(indicesSalidaSala[2])+"", forma.getSalPac(indicesSalidaSala[3])+"",forma.getFechasCx(indicesFechasCx[2])+"" ,forma.getFechasCx(indicesFechasCx[3])+"").isTrue())
							{
									errores.add("descripcion",new ActionMessage("errors.fechaHoraPosteriorIgualActual","Final de la cirugia: "+forma.getFechasCx(indicesFechasCx[2])+"  "+forma.getFechasCx(indicesFechasCx[3]),"de la salida de la sala: "+forma.getSalPac(indicesSalidaSala[2])+" - "+ forma.getSalPac(indicesSalidaSala[3])+""));
							}
						}
				
				}   
				if((forma.getParamGenerSecServicos(indicesParamGenerales[11])+"").equals(ConstantesBD.acronimoNo)&&UtilidadTexto.getBoolean(forma.getSecInfoQx(indicesSecInfoQx[7])+""))
				{
					if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaHojaAnestesia(usuario.getCodigoInstitucionInt())))
					{
						if (forma.getAnestesiologo()<=0)
						{
							errores.add("descripcion",new ActionMessage("errors.required","Anestesiologo "));
						}
						if(UtilidadTexto.isEmpty(forma.getFechaInicioAnestesia()))
						{
							errores.add("descripcion",new ActionMessage("errors.required","Fecha Inicio Anestesia "));
						}
						if(UtilidadTexto.isEmpty(forma.getHoraInicioAnestesia()))
						{
							errores.add("descripcion",new ActionMessage("errors.required","Hora Inicio Anestesia "));
						}
						if(!UtilidadTexto.isEmpty(forma.getFechaInicioAnestesia()) && !UtilidadTexto.isEmpty(forma.getHoraInicioAnestesia()))
						{
							
							//Validar que la fecha/hora ingresada sea mayor igual a la fecha/hora de ingreso a Sala
							if (!UtilidadFecha.compararFechas(forma.getFechaInicioAnestesia()+"", forma.getHoraInicioAnestesia()+"",forma.getSalPac(indicesSalidaSala[0])+"" ,forma.getSalPac(indicesSalidaSala[1])+"").isTrue())
							{
								errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorOtraReferencia","inicial Anestesia: "+forma.getFechaInicioAnestesia()+" - "+forma.getHoraInicioAnestesia(),"del ingreso a la sala: "+(UtilidadTexto.isEmpty(forma.getSalPac(indicesSalidaSala[0])+"")?"":forma.getSalPac(indicesSalidaSala[0])+"")+" - "+ (UtilidadTexto.isEmpty(forma.getSalPac(indicesSalidaSala[1])+"")?"":forma.getSalPac(indicesSalidaSala[1]))+""));
							}
							
							 HashMap pet=HojaQuirurgica.consultarDatosPeticion( forma.getPeticion());
							
							// y mayor igual a la fecha/hora de la Petición.
							if (!UtilidadFecha.compararFechas(forma.getFechaInicioAnestesia()+"", forma.getHoraInicioAnestesia()+"",pet.get("fechapeticion")+"" ,pet.get("horapeticion")+"").isTrue())
							{
									errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorOtraReferencia","inicial Anestesia: "+forma.getFechaInicioAnestesia()+" - "+forma.getHoraInicioAnestesia(),"Peticion: "+(UtilidadTexto.isEmpty(pet.get("fechapeticion")+"")?"":pet.get("fechapeticion")+"")+" - "+ (UtilidadTexto.isEmpty(pet.get("horapeticion")+"")?"":pet.get("horapeticion")+"")+""));
							}
							
						}
						
						if(UtilidadTexto.isEmpty(forma.getFechaFinAnestesia()))
						{
							errores.add("descripcion",new ActionMessage("errors.required","Fecha Fin Anestesia "));
						}
						if(UtilidadTexto.isEmpty(forma.getHoraFinAnestesia()))
						{
							errores.add("descripcion",new ActionMessage("errors.required","Hora Fin Anestesia "));
						}
						if(!UtilidadTexto.isEmpty(forma.getFechaFinAnestesia()) && !UtilidadTexto.isEmpty(forma.getHoraFinAnestesia()))
						{
							//o	Validar que la fecha/hora ingresada sea menor igual a la fecha/hora de salida de la Sala
							if (!UtilidadFecha.compararFechasMenorOIgual(forma.getFechaFinAnestesia()+"", forma.getHoraFinAnestesia()+"",forma.getSalPac(indicesSalidaSala[2])+"" ,forma.getSalPac(indicesSalidaSala[3])+"").isTrue())
							{
								errores.add("descripcion",new ActionMessage("errors.fechaHoraPosteriorIgualActual","Fin Anestesia: "+forma.getFechaFinAnestesia()+" - "+forma.getHoraFinAnestesia(),"de salida de la sala: "+(UtilidadTexto.isEmpty(forma.getSalPac(indicesSalidaSala[2])+"")?"":forma.getSalPac(indicesSalidaSala[2]))+" - "+ (UtilidadTexto.isEmpty(forma.getSalPac(indicesSalidaSala[3])+"")?"":forma.getSalPac(indicesSalidaSala[3]))+""));
							}
							
							// y menor igual a la fecha/hora del sistema.
							if (!UtilidadFecha.compararFechasMenorOIgual(forma.getFechaFinAnestesia()+"", forma.getHoraFinAnestesia()+"",UtilidadFecha.getFechaActual() ,UtilidadFecha.getHoraActual()+"").isTrue())
							{
									errores.add("descripcion",new ActionMessage("errors.fechaHoraPosteriorIgualActual","Fin Anestesia: "+forma.getFechaFinAnestesia()+" - "+forma.getHoraFinAnestesia()," del Sistema"));
							}
							
						}
						
						if (UtilidadTexto.isEmpty(forma.getSalPac(indicesSalidaSala[4])+""))
						{
							errores.add("descripcion",new ActionMessage("errors.required","Salida de la sala del paciente "));
						}
					}
				}
			
				
				//se valida que el tipo de sala no este vacio
				if ((forma.getSecInfoQx(indicesSecInfoQx[9])+"").equals("") && !(forma.getSecInfoQx(indicesSecInfoQx[9])+"").equals("-1"))
				{
					errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Sala "));
				}
				
				//modificado por anexo 728
				if(!forma.getFuncionalidad().equals("RespProce"))
				{
					//se valida que el tipo de herida
					if ((forma.getSecInfoQx(indicesSecInfoQx[0])+"").equals("") && !(forma.getSecInfoQx(indicesSecInfoQx[0])+"").equals(ConstantesBD.codigoNuncaValido+""))
					{
						errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Herida "));
					}
				}
				
				
				/******************************************************************************************************/
	
			
		/*************************************************************************************************************************
		 * Validacion de la informacion quirurgica.  seccion informacion quirurgica
		 */
				/*
				logger.info("\n\n\n*****************PASO POR AQUI VALIDACION INFORMACION 	QUIRURGICA*************************************************\n\n\n");
				logger.info("funcionalidad: "+forma.getFuncionalidad());
				logger.info(indicesParamGenerales[9]+": "+forma.getParamGenerSecServicos(indicesParamGenerales[9]));
				logger.info("indicativo cargo: "+forma.getSolicitudMap(indicesSolicitud[14]+"_0"));
				logger.info(indicesSecInfoQx[12]+": "+forma.getSecInfoQx(indicesSecInfoQx[12]));
				logger.info(indicesSecInfoQx[10]+": "+forma.getSecInfoQx(indicesSecInfoQx[10]));
				logger.info("\n\n\n*****************FIN PASO POR AQUI VALIDACION INFORMACION 	QUIRURGICA*************************************************\n\n\n");
				*/
			//se pregunta que no venga de la funcionalidad de responder procedimientos.
			if (!forma.getFuncionalidad().equals("RespProce"))
			{
				//si es una cirugia de para un no cruento
				if ((forma.getSolicitudMap(indicesSolicitud[14]+"_0")+"").equals(ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento))
				{
					//se pregunta si el indicativo de:
					//hacer requerida en hoja quirurgica la descripcion por especialidad para No Cruentos
					//esta en N, de ser asi, se hace requerido el ingreso de la informacion quirurqica
					if (!UtilidadTexto.getBoolean(forma.getParamGenerSecServicos(indicesParamGenerales[9])+""))
						if (!UtilidadCadena.noEsVacio(forma.getSecInfoQx(indicesSecInfoQx[12])+"") && !UtilidadCadena.noEsVacio(forma.getSecInfoQx(indicesSecInfoQx[10])+""))
							errores.add("descripcion",new ActionMessage("errors.required","La información quirúrgica "));
				}
				else
					if ((forma.getSolicitudMap(indicesSolicitud[14]+"_0")+"").equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia))
					{
						//se pregunta si el indicativo de:
						//hacer requerida en hoja quirurgica la descripcion por especialidad para Cirugias
						//esta en N, de ser asi, se hace requerido el ingreso de la informacion quirurqica
						if (!UtilidadTexto.getBoolean(forma.getParamGenerSecServicos(indicesParamGenerales[8])+""))
							if (!UtilidadCadena.noEsVacio(forma.getSecInfoQx(indicesSecInfoQx[12])+"") && !UtilidadCadena.noEsVacio(forma.getSecInfoQx(indicesSecInfoQx[10])+""))
								errores.add("descripcion",new ActionMessage("errors.required","La información quirúrgica "));
					}
			}
			else
				if ((forma.getSolicitudMap(indicesSolicitud[14]+"_0")+"").equals(ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia))
				{
					//se pregunta si el indicativo de:
					//hacer requerida en hoja quirurgica la descripcion por especialidad para Cirugias
					//esta en N, de ser asi, se hace requerido el ingreso de la informacion quirurqica
					if (!UtilidadTexto.getBoolean(forma.getParamGenerSecServicos(indicesParamGenerales[8])+""))
						if (!UtilidadCadena.noEsVacio(forma.getSecInfoQx(indicesSecInfoQx[12])+""))
							errores.add("descripcion",new ActionMessage("errors.required","La información quirúrgica "));
				}
				
		
		/*
		 * Fin de la validacion de la informacion quirurgica 
		 **************************************************************************************************************************/
		
				
	//*********************************** FIN VALIDACION *************************************************
	
		
		//*******************************************************************************************************************************
		//****************************************************VALIDACION DE LA SALIDA DEL PACIENTE **************************************
		//se valida que que no participo anestesiologo
		
		if ((forma.getSecInfoQx(indicesSecInfoQx[7])+"").equals(ConstantesBD.acronimoNo))
		{
			boolean tmp=true;
			//1) se valida que el tipo de anestesia no este vacio
			if ((forma.getSecInfoQx(indicesSecInfoQx[8])+"").equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","El tipo de Anestesia"));
			
			//si se selecciono fallece
			if ((forma.getSalPac(indicesSalidaSala[8])+"").equals(ConstantesBD.acronimoSi))
			{
				//se valida que no venga vacia la fecha fallece
				if (!UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[6])+""))
				{
					tmp=false;
					errores.add("descripcion",new ActionMessage("errors.required","La Fecha Fallece "));
				}
				else//se valida que venga en el formato de fecha que debe de ser DD/MM/YYYY
					if(!UtilidadFecha.validarFecha(forma.getSalPac(indicesSalidaSala[6])+""))
					{
						errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido",forma.getSalPac(indicesSalidaSala[6])+" fallece "));
						tmp=false;
					}
				
				//se vaida que venga la hora fallece
				if (!UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[7])+""))
				{
					tmp=false;
					errores.add("descripcion",new ActionMessage("errors.required","La Hora Fallece "));
				}
				else//se valida que la hora fallece venga en el formato que es HH:MM
					if (!UtilidadFecha.validacionHora(forma.getSalPac(indicesSalidaSala[7])+"").puedoSeguir)
					{
						errores.add("descripcion",new ActionMessage("errors.formatoHoraInvalido",forma.getSalPac(indicesSalidaSala[7])+" fallece "));
						tmp=false;
					}
				//se valida que el diagnostico no venga vacio
				if (!UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[9])+""))
					errores.add("descripcion",new ActionMessage("errors.required","El Diagnóstico Muerte "));
					
				
				if (tmp)
				{
					//se valida la fecha y hora fallece sea menos o igual a la fecha del sistema
					if (!UtilidadFecha.compararFechas(fecha, hora,forma.getSalPac(indicesSalidaSala[6])+"",forma.getSalPac(indicesSalidaSala[7])+"").isTrue())
						errores.add("descripcion",new ActionMessage("errors.fechaHoraPosteriorIgualActual"," fallece: "+ forma.getSalPac(indicesSalidaSala[6])+" / "+forma.getSalPac(indicesSalidaSala[7]) ,"del sistema: "+fecha+" / "+hora));
						
					//se valida que la fecha y hora fallece sean mayor que la fecha y hora de ingreso a la sala
					if (UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[6])+"") && UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[7])+"") && 
							UtilidadCadena.noEsVacio(forma.getFechasCx(indicesFechasCx[0])+"") && UtilidadCadena.noEsVacio(forma.getFechasCx(indicesFechasCx[1])+""))
						{
							if (!UtilidadFecha.compararFechas(forma.getSalPac(indicesSalidaSala[6])+"", forma.getSalPac(indicesSalidaSala[7])+"",forma.getFechasCx(indicesFechasCx[0])+"" ,forma.getFechasCx(indicesFechasCx[1])+"").isTrue())
									errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorIgualActual"," Fallece "+forma.getSalPac(indicesSalidaSala[6])+" - "+ forma.getSalPac(indicesSalidaSala[7])," Inicial de la cirugia: "+forma.getFechasCx(indicesFechasCx[0])+" - "+forma.getFechasCx(indicesFechasCx[1])));
						}
					
					//se valida que la fecha y hora fallece sea menor que la la fecha y hora final de la cirugia
					if (UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[6])+"") && UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[7])+"") &&
						UtilidadCadena.noEsVacio(forma.getFechasCx(indicesFechasCx[2])+"") && UtilidadCadena.noEsVacio(forma.getFechasCx(indicesFechasCx[3])+""))
					{
						if (!UtilidadFecha.compararFechas(forma.getFechasCx(indicesFechasCx[2])+"" ,forma.getFechasCx(indicesFechasCx[3])+"",forma.getSalPac(indicesSalidaSala[6])+"", forma.getSalPac(indicesSalidaSala[7])+"").isTrue())
								errores.add("descripcion",new ActionMessage("errors.fechaHoraPosteriorIgualActual"," Fallece "+forma.getSalPac(indicesSalidaSala[6])+" - "+ forma.getSalPac(indicesSalidaSala[7])," Final de la cirugia: "+forma.getFechasCx(indicesFechasCx[2])+" - "+forma.getFechasCx(indicesFechasCx[3])));
					}
			
				}
				
			}
			
			
			if (UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[4])+""))
			{
			
			boolean ban=true;
			/*
			 * 	//modificado por tarea 2909
			//se evalua si ya fueron llenada las demas secciones
			if (forma.getMensajes().size()>0)
				errores.add("descripcion",new ActionMessage("errors.noIngresado","Los campos de las secciones requeridas "));	
			*/
			//1) se valida que la fecha de ingreso a la sala es requerido
			if (!UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[0])+""))
			{
				errores.add("descripcion",new ActionMessage("errors.required","La fecha de Ingreso a la sala "));
				ban=false;
			}
			else
				if(!UtilidadFecha.validarFecha(forma.getSalPac(indicesSalidaSala[0])+""))
				{
					errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido",forma.getSalPac(indicesSalidaSala[0])+" de la salida de la sala del paciente"));
					ban=false;
				}
				
				
			//2)se valida que hora de ingreso de la sala es requerido
			if (!UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[1])+""))
			{
				errores.add("descripcion",new ActionMessage("errors.required","La Hora de Ingreso a la sala "));
				ban=false;
			}
			else
				if (!UtilidadFecha.validacionHora(forma.getSalPac(indicesSalidaSala[1])+"").puedoSeguir)
				{
					errores.add("descripcion",new ActionMessage("errors.formatoHoraInvalido",forma.getSalPac(indicesSalidaSala[1])+" de la salida de la sala del paciente"));
					ban=false;
				}
		
			if (!forma.getFuncionalidad().equals("RespProce") || (forma.getParamGenerSecServicos(indicesParamGenerales[18])+"").equals(ConstantesBD.acronimoNo))
			{
				//3)se valida la Fecha  de salida de la sala
				if (!UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[2])+""))
				{
					errores.add("descripcion",new ActionMessage("errors.required","La fecha de Salida de la sala "));
					ban=false;
				}
				else
					if(!UtilidadFecha.validarFecha(forma.getSalPac(indicesSalidaSala[2])+""))
					{
						errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido",forma.getSalPac(indicesSalidaSala[2])+" de la salida de la sala del paciente"));
						ban=false;
					}
			}
			else
				if (!UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[2])+""))
					forma.setSalPac(indicesSalidaSala[2],UtilidadFecha.getFechaActual());
				
			if ((!forma.getFuncionalidad().equals("RespProce")) || (forma.getParamGenerSecServicos(indicesParamGenerales[18])+"").equals(ConstantesBD.acronimoNo))
			{
				//4)se valida que hora de salida de la sala es requerido
				if (!UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[3])+""))
				{
					errores.add("descripcion",new ActionMessage("errors.required","La Hora de Salida de la sala "));
					ban=false;
				}
				else
					if (!UtilidadFecha.validacionHora(forma.getSalPac(indicesSalidaSala[3])+"").puedoSeguir)
					{
						errores.add("descripcion",new ActionMessage("errors.formatoHoraInvalido",forma.getSalPac(indicesSalidaSala[3])+" de la salida de la sala del paciente"));
						ban=false;
					}
			}
			else
				if (!UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[3])+""))
					forma.setSalPac(indicesSalidaSala[3],UtilidadFecha.getHoraActual());
			
			
			//se valida que la fecha y hora de ingreso a la sea menor o igual a la fecha del sistema y mayor o igual 
			//a la fecha de registro de ingreso del paciente.
			//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
			//+++++++++++++++++++ SE VALIDA LA FECHA Y HORA INICIAL ++++++++++++++++++++++++++++++++++++++++++++++
			
						String fechaIngresoPac=paciente.getFechaIngreso();
						String horaIngresoPac = UtilidadFecha.convertirHoraACincoCaracteres(UtilidadesManejoPaciente.getHoraIngreso(connection, paciente.getCodigoCuenta(),paciente.getCodigoUltimaViaIngreso()));
				
						//logger.info("\n **************** el vlaor de las fechas --> "+fechaIngresoPac+" hora --> "+horaIngresoPac);
					//aqui se valida la fecha y hora de ingreso a a la sala
				if (ban)
					if (!UtilidadFecha.compararFechas(fecha, hora,forma.getSalPac(indicesSalidaSala[0])+"",forma.getSalPac(indicesSalidaSala[1])+"").isTrue())
					{
						errores.add("descripcion",new ActionMessage("errors.fechaHoraPosteriorIgualActual","del ingreso a la sala: "+ forma.getSalPac(indicesSalidaSala[0])+" / "+forma.getSalPac(indicesSalidaSala[1]) ,"del sistema: "+fecha+" / "+hora));
						
					}
					else
						if (!UtilidadFecha.compararFechas(forma.getSalPac(indicesSalidaSala[0])+"" ,forma.getSalPac(indicesSalidaSala[1])+"",fechaIngresoPac, horaIngresoPac).isTrue())
						{
							logger.info("\n **************** entre a comparar --> "+fechaIngresoPac+" hora --> "+horaIngresoPac);	
							errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorIgualActual","del ingreso a la sala: "+forma.getSalPac(indicesSalidaSala[0])+"  "+forma.getSalPac(indicesSalidaSala[1]),"del ingreso del paciente: "+fechaIngresoPac+" / "+ horaIngresoPac));
								
						}
			
			//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			//+++++++++++++++++++ SE VALIDA LA FECHA Y HORA SALIDA DE LA SALA  ++++++++++++++++++++++++++++++++++++++++++++++
				
				if (ban)
					if (!UtilidadFecha.compararFechas(fecha, hora,forma.getSalPac(indicesSalidaSala[2])+"",forma.getSalPac(indicesSalidaSala[3])+"").isTrue())
					{
						errores.add("descripcion",new ActionMessage("errors.fechaHoraPosteriorIgualActual","de la salida de la sala: "+ forma.getSalPac(indicesSalidaSala[2])+" / "+forma.getSalPac(indicesSalidaSala[3]) ,"del sistema: "+fecha+" / "+hora));
						
					}
					else
						if (!UtilidadFecha.compararFechas(forma.getSalPac(indicesSalidaSala[2])+"" ,forma.getSalPac(indicesSalidaSala[3])+"",forma.getSalPac(indicesSalidaSala[0])+"", forma.getSalPac(indicesSalidaSala[1])+"").isTrue())
						{
								errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorIgualActual","de la salida de la sala: "+forma.getSalPac(indicesSalidaSala[2])+"  "+forma.getSalPac(indicesSalidaSala[3]),"del ingreso a la sala: "+forma.getSalPac(indicesSalidaSala[0])+" / "+ forma.getSalPac(indicesSalidaSala[1])));
								
						}
			
			//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
								
				//se valida que la sala no este ocupada
				if(UtilidadFecha.validarFecha(forma.getSalPac(indicesSalidaSala[0])+"") 
						&& UtilidadFecha.validarFecha(forma.getSalPac(indicesSalidaSala[2])+"") 
							&& UtilidadFecha.validacionHora(forma.getSalPac(indicesSalidaSala[1])+"").puedoSeguir 
								&& UtilidadFecha.validacionHora(forma.getSalPac(indicesSalidaSala[3])+"").puedoSeguir
								&& Utilidades.convertirAEntero(forma.getSecInfoQx(indicesSecInfoQx[3])+"")>0)
				{
					//******VALIDACION DE LA OCUPACIÓN DE LA SALA*************************************
					HashMap datosSala=HojaQuirurgica.estaSalaOcupada(
							connection, 
							Utilidades.convertirAEntero(forma.getSolicitudMap(indicesSolicitud[8]+"_0")+""),
							Utilidades.convertirAEntero(forma.getSecInfoQx(indicesSecInfoQx[3])+"") ,
							forma.getSalPac(indicesSalidaSala[0])+"",
							forma.getSalPac(indicesSalidaSala[1])+"",
							forma.getSalPac(indicesSalidaSala[2])+"",								
							forma.getSalPac(indicesSalidaSala[3])+"");
					
					if(UtilidadTexto.getBoolean(datosSala.get("estaSalaOcupada")+""))
					{
						int numReg=Utilidades.convertirAEntero(datosSala.get("numRegistros")+"");
						for (int i=0;i<numReg;i++)
							errores.add("descripcion",new ActionMessage("error.salasCirugia.salaOcupada", "entre el rango de fecha/hora Sala: "+UtilidadFecha.conversionFormatoFechaAAp(datosSala.get("fechaIngresoSala_"+i)+"") +" "+datosSala.get("horaIngresoSala_"+i)+" - "+UtilidadFecha.conversionFormatoFechaAAp(datosSala.get("fechaSalidaSala_"+i)+"") +" "+datosSala.get("horaSalidaSala_"+i), ""));
					}
					//********************************************************************************							
				}
				
				
			}
			
			//Valida la informacion de los articulos incluidos
			if(UtilidadCadena.noEsVacio(forma.getSalPac(indicesSalidaSala[4])+""))
				errores = RespuestaProcedimientos.valicacionesArticulosIncluidosProc(errores,forma.getArrayArticuloIncluidoDto(),forma.getJustificacionMap());		
		}
		
		//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		//++++++++++++++++++++++++++++++++++++VALIDACION DE LAS SECCIONES REQUERIDAS +++++++++++++++++++++++++++++++++++++++++++++++
		//modificado por tarea 2909
		
		//se evalua si ya fueron llenada las demas secciones
		logger.info("TAMANO DE LOS MENSAJES ANTES DE GUARDAR: "+forma.getMensajes().size());
		if (forma.getMensajes().size()>0)
			for (int i=0; i<forma.getMensajes().size();i++)
				errores.add("descripcion",new ActionMessage("prompt.generico",forma.getMensajes().get(i)));	
		
		//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		
		//********************************************FIN VALIDACION SALIDA DEL PACIENTE ************************************************
		//********************************************************************************************************************************
			
			}
			
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//*********************************SE VALIDA QUE LOS PROFESIONALES NO ESTEN REPETIDOS ********************************
				int numProf = Integer.parseInt(forma.getProfesionales("numRegistros")+"");
				boolean ban2=true;
				for (int i=0 ; i<numProf;i++)
				{
					if (((forma.getProfesionales(indicesProfInfoQx[6]+i)+"").equals(ConstantesBD.acronimoInsertar) && (forma.getProfesionales(indicesProfInfoQx[5]+i)+"").equals(ConstantesBD.acronimoNo)) ||
						((forma.getProfesionales(indicesProfInfoQx[6]+i)+"").equals(ConstantesBD.acronimoModificar) && (forma.getProfesionales(indicesProfInfoQx[5]+i)+"").equals(ConstantesBD.acronimoSi)))
					{
					
						if ((forma.getProfesionales(indicesProfInfoQx[2]+i)+"").equals("") || (forma.getProfesionales(indicesProfInfoQx[2]+i)+"").equals("-1"))
							errores.add("descripcion",new ActionMessage("errors.required","El Tipo de asocio del registro "+(i+1)));
						
						if ((forma.getProfesionales(indicesProfInfoQx[3]+i)+"").equals("") || (forma.getProfesionales(indicesProfInfoQx[3]+i)+"").equals("-1"))
								errores.add("descripcion",new ActionMessage("errors.required","El Profesional del registro "+(i+1)));
						
					}
					
					if(!(forma.getProfesionales(indicesProfInfoQx[6]+i)+"").equals(ConstantesBD.acronimoEliminar))
					{
						for (int j=(i+1);j<numProf;j++)
						{
							logger.info("\n profesionales --> "+forma.getProfesionales(indicesProfInfoQx[2]+i)+""+"%%%%%%%%%5"+forma.getProfesionales(indicesProfInfoQx[2]+j)+"");
							if ((forma.getProfesionales(indicesProfInfoQx[2]+i)+"").equals((forma.getProfesionales(indicesProfInfoQx[2]+j)+"")))
									errores.add("descripcion",new ActionMessage("error.noRegistroMismaInformacion","Asocio "));
						}
						
						
						
						/*********************************************************************************************************
						 * Modificado por tarea 50915 puesta por German Aguilera
						 * Se solicita que los profesionales no pueden ir repetidos.
						 */
							for (int j=0;j<numProf;j++)
								if (j!=i && ban2 && !(forma.getProfesionales(indicesProfInfoQx[6]+j)+"").equals(ConstantesBD.acronimoEliminar))
									if ((forma.getProfesionales(indicesProfInfoQx[3]+i)+"").equals((forma.getProfesionales(indicesProfInfoQx[3]+j)+"")))
									{
										errores.add("descripcion",new ActionMessage("prompt.generico"," No se pueden ingresar Profesionales Repetidos"));
										ban2=false;
									}
						/*
						 * 
						 **********************************************************************************************************/
						
					}
				}
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			
		}		
		
		return errores;
	}

	
	
	/**
	 * Metodo encargado de validad la seccion de otros profesionales
	 * otros profesionales.
	 * @param connection
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param errores
	 * @return
	 */
	private ActionErrors validarOtrosprofesionales (Connection connection, HojaQuirurgicaForm forma,	UsuarioBasico usuario,HttpServletRequest request,ActionErrors errores)
	{
		logger.info("\n%%%%%%%%%%%%ENTRE A VALIDAR OTROS PROFESIONALES %%%%%%%%%%%%%%%%%%%");
		if (forma.getEstado().equals("guardarSecOtrosProf"))
		{
			int numRegVerdad = Integer.parseInt(forma.getOtrosProf(indicesOtrosProfesionales[8])+"");
			int numReg = Integer.parseInt(forma.getOtrosProf("numRegistros")+"");
			
			if (!forma.getFuncionalidad().equals("Citas"))
				if (numRegVerdad<1)
					errores.add("descripcion",new ActionMessage("errors.minimoCampos2","un Profesional","la sección Otros Profesionales"));
			
			for (int i=0; i<numReg;i++ )
			{
				//1)se valida que los datos a ser ingresados o modificados a la BD
				//esten todos llenos
				if (!(forma.getOtrosProf(indicesOtrosProfesionales[5]+i)+"").equals(ConstantesBD.acronimoEliminar) && 
					!(forma.getOtrosProf(indicesOtrosProfesionales[5]+i)+"").equals(ConstantesBD.acronimoNada))
				{
					//medico 0
					if (forma.getOtrosProf(indicesOtrosProfesionales[0]+i).equals("") || forma.getOtrosProf(indicesOtrosProfesionales[0]+i).equals("-1"))
						errores.add("descripcion",new ActionMessage("errors.required","El Profesional"));
					//tipo profesional1
					if (forma.getOtrosProf(indicesOtrosProfesionales[1]+i).equals("") || forma.getOtrosProf(indicesOtrosProfesionales[1]+i).equals("-1"))
						errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Profesional "));
					// especialidad 2
					if (forma.getOtrosProf(indicesOtrosProfesionales[2]+i).equals("") || forma.getOtrosProf(indicesOtrosProfesionales[2]+i).equals("-1"))
						errores.add("descripcion",new ActionMessage("errors.required","La Especialidad "));
					
				
					//se validan que no se repitan profesionales
					
					for (int j=(i+1);j<numReg;j++)
					{
						if (!(forma.getOtrosProf(indicesOtrosProfesionales[5]+j)+"").equals(ConstantesBD.acronimoEliminar) && 
								!(forma.getOtrosProf(indicesOtrosProfesionales[5]+j)+"").equals(ConstantesBD.acronimoNada))
							{
								if ((forma.getOtrosProf(indicesOtrosProfesionales[0]+i)+"").equals((forma.getOtrosProf(indicesOtrosProfesionales[0]+j)+"")))
									errores.add("descripcion",new ActionMessage("error.noRegistroMismaInformacion","Profesional "));
					
							}
					}
				
				}
				
				
				
				
				
			}
				
		}
		
		return errores;
	}
	
	
/**********************************************************************************************************************/
/*-------------------------------------------------------------------------------------------------------------------
 * FIN METODOS PARA LA NUEVA HOJA QUIRURGICA
 *------------------------------------------------------------------------------------------------------------------*/
/*********************************************************************************************************************/
	/**
	 * Metodo que permite verificar si la peticiÓn tiene asociada una autorizaciÃ³n de capita 
	 * asociandola a la nueva solicitud.
	 * 
	 * @param codPeticion
	 * @param codAutoEntSub
	 * @param codSolicitud
	 * @throws IPSException 
	 * @author DiaRuiPe
	 */
	private void asociarAutorizacionSolicitud(int codPeticion, int codSolicitud, int codServicio) throws IPSException{
		
		try{
			
			List<AutorizacionPorOrdenDto> listaAutorizacionesPorOrdenExistentes = null;
			OrdenesFacade ordenesFacade = new OrdenesFacade();
			List<OrdenAutorizacionDto> listaOrdenesAutorizar = null;
			OrdenAutorizacionDto ordenAutorizacionDto = null;
			List<ServicioAutorizacionOrdenDto> servicioAutorizar = null;
			ServicioAutorizacionOrdenDto servicio = null;
			
			IProcesoGeneracionAutorizacionMundo procesoGeneracionAutorizacionMundo = new ProcesoGeneracionAutorizacionMundo();
			
			
			//valida que existan ordenes con Autorizaciones de Capitacion Subcontratada en estado Autorizada	
			List<String> estados = new ArrayList<String>();
			estados.add(ConstantesIntegridadDominio.acronimoAutorizado);
			long peticion=codPeticion;
			long codOrden=codSolicitud;
			
			listaAutorizacionesPorOrdenExistentes = ordenesFacade.obtenerAutorizacionCapitacion(ConstantesBD.claseOrdenPeticion, ConstantesBD.codigoTipoOrdenAmbulatoriaServicios, peticion, estados);
			
			HibernateUtil.beginTransaction();
			//Verifico si la peticiÓn tiene autorizaciones asociadas 
			if(listaAutorizacionesPorOrdenExistentes != null 
					&& !listaAutorizacionesPorOrdenExistentes.isEmpty()){
				
				servicioAutorizar = new ArrayList<ServicioAutorizacionOrdenDto>();
				servicio = new ServicioAutorizacionOrdenDto();
				servicio.setAutorizado(true);
				servicio.setAutorizar(true);
				servicio.setCodigo(codServicio);
				servicioAutorizar.add(servicio);
				listaOrdenesAutorizar = new ArrayList<OrdenAutorizacionDto>();
				//Envio la información de la orden
				ordenAutorizacionDto = new OrdenAutorizacionDto();
				ordenAutorizacionDto.setMigrado(ConstantesBD.acronimoSiChar);
				ordenAutorizacionDto.setCodigoOrden(codOrden);
				ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenOrdenMedica);
				ordenAutorizacionDto.setTipoOrden(ConstantesBD.codigoTipoSolicitudCirugia);
				ordenAutorizacionDto.setServiciosPorAutorizar(servicioAutorizar);

				listaOrdenesAutorizar.add(ordenAutorizacionDto);
				//Envio la información de la autorización de entidad subcontratada
				AutorizacionesEntidadesSub autorizacionesEntidadesSub 	= null;
				autorizacionesEntidadesSub = new AutorizacionesEntidadesSub();
				autorizacionesEntidadesSub.setConsecutivo(listaAutorizacionesPorOrdenExistentes.get(0).getConsecutivo());
				procesoGeneracionAutorizacionMundo.asociarSolicitudAutorizacion(listaOrdenesAutorizar, autorizacionesEntidadesSub);
				procesoGeneracionAutorizacionMundo.asociarDetalleSolicitudesAutorizaciones(listaOrdenesAutorizar, autorizacionesEntidadesSub, false);
			}

			HibernateUtil.endTransaction();
			
			
		}catch (IPSException ipse) {			
			HibernateUtil.abortTransaction();
			Log4JManager.error(ipse.getMessage(),ipse);	
			throw ipse;			
		} 
		catch (Exception e){
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);			
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}
}

