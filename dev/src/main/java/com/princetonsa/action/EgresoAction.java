/*
 * @(#)EgresoAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.RespuestaValidacion;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.EgresoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Admision;
import com.princetonsa.mundo.Cama;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.TrasladoCamas;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.atencion.Egreso;
import com.princetonsa.mundo.historiaClinica.Valoraciones;

/**
 * Clase encargada del control de la funcionalidad "Egreso"
 * Modificado por Jhony Alexander Duque A.
 *	@version 1.0, Jul 17, 2003
 */
public class EgresoAction extends Action
{

	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 * de Egreso
	 */
	private Logger logger = Logger.getLogger(EgresoAction.class);
	
	//	objeto usado para grabar el diagnóstico de un semiegreso
	private Diagnostico diagnostico = new Diagnostico();
	
	String [] indicesFactura=Egreso.indicesFactura;
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
		throws Exception
		{

		Connection con=null; 
		try{
			if (form instanceof EgresoForm)
			{

				if (response==null); //Para evitar que salga el warning
				if( logger.isDebugEnabled() )
				{
					logger.debug("Entro al Action de Egreso");
				}

				EgresoForm egresoForm=(EgresoForm)form;
				String estado=egresoForm.getEstado();

				logger.warn("ESTADO EGRESO--->"+estado+" --- "+egresoForm.getDatosNuevos());
				logger.warn("ESTADO accion --->"+egresoForm.getAccionAFinalizar());


				try
				{
					String tipoBD = System.getProperty("TIPOBD");
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					con = myFactory.getConnection();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					//No se cierra conexión porque si llega aca ocurrió un
					//error al abrirla
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}


				//Lo primero que vamos a hacer es validar que se
				//cumplan las condiciones.

				HttpSession session=request.getSession();		
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				UsuarioBasico medico = (UsuarioBasico)session.getAttribute("usuarioBasico");
				if(paciente.getCodigoCuenta()!=0&&!egresoForm.isResumenEgresoAutomatico()&&!egresoForm.isResumenSemiEgreso())
				{
					egresoForm.setIdCuenta(paciente.getCodigoCuenta());
				}


				/**********************************************************************************************
				 * Modificado por anexo 747
				 */
				//			egresoForm.resetNuevosDatos();

				//Se toma el valor del parametro general Boleta salida depues de ingreso cerrado
				egresoForm.setParamBoletaSalidaDespuesdeIngresoCerrado(UtilidadTexto.getBoolean(ValoresPorDefecto.getLiberarCamaHospitalizacionDespuesFacturar(medico.getCodigoInstitucionInt())));

				//se consultan todos los ingresos
				HashMap ingresos = new HashMap ();
				ingresos=UtilidadesHistoriaClinica.consultarTodosIngresosXPaciente(con, paciente.getCodigoPersona());
				//logger.info("\n los ingresos son "+ingresos);


				//se ingresan los datos del ingreso a la forma
				egresoForm.setDatosIngresos(ingresos);

				int numReg=Utilidades.convertirAEntero(ingresos.get("numRegistros")+"");

				//se ingresa la cuenta
				egresoForm.setIdCuenta(Utilidades.convertirAEntero(ingresos.get("numCuenta_"+(numReg-1))+""));

				String estadoIngreso=  UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getAcronimo();
				
				if (estadoIngreso.equals(""))
				{
					estadoIngreso=  UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, Utilidades.convertirAEntero(ingresos.get("codigoIngreso_"+(numReg-1))+"")).getAcronimo();
             	}

				int destinoSalida=Utilidades.obtenerDestinoSalidaEgresoEvolucion(con,Utilidades.convertirAEntero(ingresos.get("numCuenta_"+(numReg-1))+""));
				//logger.info("\n ¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬¬> el estado del ingreso -->"+estadoIngreso+"    el estado del parametro ------> "+egresoForm.isParamBoletaSalidaDespuesdeIngresoCerrado());
				/**
				 * 
				 **********************************************************************************************/



				logger.info("HAY SOLICITUDES INCOMPLETAS CUENTA? "+UtilidadValidacion.haySolicitudesIncompletasEnCuenta(con, paciente.getCodigoIngreso(), medico.getCodigoInstitucionInt(), false,true).isTrue());


				/********************************************************************************************************************************
				 * 	VALIDACIONES GENERALES
				 ********************************************************************************************************************************/

				//Primera Condición: El usuario debe existir
				//la validación de si es médico o no solo se hace en insertar
				if( medico == null )
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe el usuario", "errors.usuario.noCargado", true);
				else
					if( paciente == null || paciente.getCodigoTipoIdentificacionPersona().equals("") )
						//Segunda Condición: Debe haber un paciente cargado
						
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true); 
	            else//se valida que la via de ingreso sea de urgencias u hospitalizacion
//					if (!(ingresos.get("codigoViaIngreso_"+(numReg-1))+"").equals(ConstantesBD.codigoViaIngresoUrgencias+"") && !(ingresos.get("codigoViaIngreso_"+(numReg-1))+"").equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
					
					if ((paciente.getCodigoUltimaViaIngreso() != ConstantesBD.codigoViaIngresoHospitalizacion) && (paciente.getCodigoUltimaViaIngreso() != ConstantesBD.codigoViaIngresoUrgencias) && paciente.getCodigoUltimaViaIngreso() != 0) 
					{	
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "la via de ingreso debe de ser hospitalizacion o urgencias", "errors.viaIngreso.HospitalizacionUrgencias", true);
					}
				if (!(ingresos.get("codigoViaIngreso_"+(numReg-1))+"").equals(ConstantesBD.codigoViaIngresoUrgencias+"") && !(ingresos.get("codigoViaIngreso_"+(numReg-1))+"").equals(ConstantesBD.codigoViaIngresoHospitalizacion+"") && paciente.getCodigoUltimaViaIngreso() == 0)
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "la via de ingreso debe de ser hospitalizacion o urgencias", "errors.viaIngreso.HospitalizacionUrgencias", true);
				}
				/********************************************************************************************************************************
				 *	FIN VALIDACIONES GENERALES
				 ********************************************************************************************************************************/


				/********************************************************************************************************
				 * Cambio realizado por anexo 747
				 * se valida el parametro boleta de salida ingreso cerrado para saber que se valida
				 */
				//***************************************************************************************************************************
						else   //----------------------------------------si el parametro esta en "SI"--------------------------------------------------	
							if (egresoForm.isParamBoletaSalidaDespuesdeIngresoCerrado())
							{
								logger.info("\n---------------------------------------------------- 1----------------------------------------");
								//si el parametro es "SI" se evalua si el ingreso esta cerrado 
							
								if (!estadoIngreso.equals(ConstantesIntegridadDominio.acronimoEstadoCerrado))
									return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El ingreso debe estar en estado Cerrado", "El ingreso debe estar en estado Cerrado.", false) ;
								else//se valida la conducta a seguir de la via de ingreso urgencias
									if((paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoUrgencias) || (ingresos.get("codigoViaIngreso_"+(numReg-1))+"").equals(ConstantesBD.codigoViaIngresoUrgencias+"") )
											{
										logger.info("\n---------------------------------------------------- 2----------------------------------------");
										int[] conductas = {
												ConstantesBD.codigoConductaSeguirSalaEspera,
												ConstantesBD.codigoConductaSeguirSalaCirugiaAmbulatoria,
												ConstantesBD.codigoConductaSeguirInterconsulta,
												ConstantesBD.codigoConductaSeguirSalaReanimacion,
										};
										if(UtilidadValidacion.validacionConductaASeguir(con, Utilidades.convertirAEntero(ingresos.get("numCuenta_"+(numReg-1))+""), conductas))
										{
											logger.info("\n---------------------------------------------------- 4 ----------------------------------------");
											ActionErrors errores = new ActionErrors();
											errores.add("Conducta a seguir equivocada",new ActionMessage("egreso.pacienteSinOrdenSalida"));
											saveErrors(request, errores);
											UtilidadBD.closeConnection(con);
											return mapping.findForward("paginaErroresActionErrors");
										}
									}

								if(!estado.equals("guardarDatosNuevos") && !estado.equals("imprimirBoletaSalida")) 
								{
									logger.info("\n---------------------------------------------------- 5----------------------------------------estao-->"+estado+"      estado forma-->"+egresoForm.getEstado()+ "----- cuenta -->"+egresoForm.getIdCuenta());
									if (UtilidadValidacion.existeEgresoCompleto (con, egresoForm.getIdCuenta()))
									{
										//logger.info("\n---------------------------------------------------- 6----------------------------------------");
										//Hacemos exactamente lo mismo que con el resumen
										//se consutlan los nuevos datos

										//return this.funcionalidadResumen(mapping, con, egresoForm, session,ingresos.get("codigoIngreso_"+(numReg-1))+"",request);
									}
								}
								//*********************************************************************************************************************

							}		
				//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
							else //--------------------------------------si el parametro esta en "NO"--------------------------------------------------
								if (!egresoForm.isParamBoletaSalidaDespuesdeIngresoCerrado())
								{
									//Se realiza la validacion del paciente
									RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente);

									//se valida que el paciene este cargado y que tenga cuenta abierta
									if (!resp.puedoSeguir&&!egresoForm.isResumenSemiEgreso()&&!egresoForm.isResumenEgresoAutomatico())
										//El paciente no tiene cuenta, saco error
										return ComunAction.accionSalirCasoError(mapping, request, con, logger, resp.textoRespuesta, resp.textoRespuesta, true) ;
									else
										//en caso de que el parametro sea "no" la funcionalidad sigue su flujo normal, preguntando si el ingreso esta habierto. 
										if (!estadoIngreso.equals(ConstantesIntegridadDominio.acronimoEstadoAbierto)
												&&!egresoForm.isResumenSemiEgreso()&&!egresoForm.isResumenEgresoAutomatico())
											//El paciente no tiene ingreso abierto
											return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noIngreso", "errors.paciente.noIngreso", true) ;
										else //se verifica si hay solicitudes incompletas
											if(UtilidadValidacion.haySolicitudesIncompletasEnCuenta(con, paciente.getCodigoIngreso(), medico.getCodigoInstitucionInt(), false,true).isTrue())
											{
												//se valida que el destino de salida en el egreso evolucion
												if(destinoSalida>0 && (destinoSalida!=ConstantesBD.codigoDestinoSalidaHospitalizacion&&destinoSalida!=ConstantesBD.codigoDestinoSalidaCirugiaAmbulatoria&&destinoSalida!=ConstantesBD.codigoDestinoSalidaTrasladoCuidadoEspecial))
												{
													if (con!=null&&!con.isClosed())
														UtilidadBD.closeConnection(con);
													boolean validarInterpretadas=UtilidadTexto.getBoolean(util.ValoresPorDefecto.getValidarEstadoSolicitudesInterpretadas(medico.getCodigoInstitucionInt()));
													if(validarInterpretadas)
														request.setAttribute("codigoDescripcionError", "egreso.estadosSolicitudes");
													else
														request.setAttribute("codigoDescripcionError", "egreso.estadosSolicitudesRespondida");
													return mapping.findForward("paginaError");
												}
												else
												{
													/*
													 * Conductas que se desean validar
													 */
													int[] conductas=
													{
															ConstantesBD.codigoConductaSeguirSalaEspera,
															ConstantesBD.codigoConductaSeguirSalaCirugiaAmbulatoria,
															ConstantesBD.codigoConductaSeguirInterconsulta,
															ConstantesBD.codigoConductaSeguirRemitirMayorComplejidad,
															ConstantesBD.codigoConductaSeguirSalidaSinObservacion,
															ConstantesBD.codigoConductaSeguirSalaReanimacion,
													};


													if(UtilidadValidacion.validacionConductaASeguir(con, egresoForm.getIdCuenta(), conductas))
													{
														//El paciente no tiene cuenta, saco error
														if( logger.isDebugEnabled() )
														{
															logger.debug("Los estados de la solicitud son invalidos para la conducta a seguir seleccionada");
														}
														if (con!=null&&!con.isClosed())
														{
															UtilidadBD.closeConnection(con);
														}
														boolean validarInterpretadas=UtilidadTexto.getBoolean(util.ValoresPorDefecto.getValidarEstadoSolicitudesInterpretadas(medico.getCodigoInstitucionInt()));
														if(validarInterpretadas)
														{
															request.setAttribute("codigoDescripcionError", "egreso.estadosSolicitudes");
														}
														else
														{
															request.setAttribute("codigoDescripcionError", "egreso.estadosSolicitudesRespondida");
														}
														return mapping.findForward("paginaError");
													}
												}

											}

									if (paciente.getCodigoAdmision()==0&&!egresoForm.isResumenSemiEgreso()&&!egresoForm.isResumenEgresoAutomatico())
									{
										//El paciente no tiene admisión, Reviso si
										//tiene egreso y si lo tiene, se muestra

										//Caso especial donde mostramos resumen 

										if( logger.isDebugEnabled() )
										{
											logger.debug("paciente sin admision");
										}
										if (con!=null&&!con.isClosed())
										{
											UtilidadBD.closeConnection(con);
										}
										request.setAttribute("codigoDescripcionError", "errors.paciente.noAdmision");
										return mapping.findForward("paginaError");
									}






									//Validacion de las conductas a seguir
									if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
									{
										int[] conductas = {
												ConstantesBD.codigoConductaSeguirSalaEspera,
												ConstantesBD.codigoConductaSeguirSalaCirugiaAmbulatoria,
												ConstantesBD.codigoConductaSeguirInterconsulta,
												ConstantesBD.codigoConductaSeguirSalaReanimacion,
										};
										if(UtilidadValidacion.validacionConductaASeguir(con, egresoForm.getIdCuenta(), conductas))
										{
											ActionErrors errores = new ActionErrors();
											errores.add("Conducta a seguir equivocada",new ActionMessage("egreso.pacienteSinOrdenSalida"));
											saveErrors(request, errores);
											UtilidadBD.closeConnection(con);
											return mapping.findForward("paginaErroresActionErrors");
										}
									}
















									//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

								}	

				/**
				 * 
				 **********************************************************************************************************************/














				logger.info("\n\n\n&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&& voy a eentrar a los  estadosssssssssssssssssssss &&&&&&&&&&&&&&&&&&& --> "+egresoForm.getEstado());


				//Llegados a este punto debemos irnos por cada uno de los estados
				//que tienen sus propias validaciones 

				if (estado==null||estado.equals(""))
				{
					egresoForm.reset();
					if( logger.isDebugEnabled() )
					{
						logger.debug("La accion específicada no esta definida ");
					}
					if (con!=null&&!con.isClosed())
					{
						UtilidadBD.closeConnection(con);
					}
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
				else
					if (estado.equals("empezar"))
					{
						egresoForm.reset();

						//se consutlan los nuevos datos
						HashMap datos = new HashMap ();
						datos.put(indicesFactura[5], ingresos.get("codigoIngreso_"+(numReg-1)));
						egresoForm.setDatosNuevos(Egreso.consultaDatosFactura(con, datos));
						logger.info("\n nuevos datos -->"+egresoForm.getDatosNuevos());
						if(paciente.getCodigoIngreso()==0)
						{
							egresoForm.setDatosIngresos("cuentas", UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, ingresos.get("codigoIngreso_"+(numReg-1))+""));
						}
						else
						{
						egresoForm.setDatosIngresos("cuentas", UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, String.valueOf(paciente.getCodigoIngreso())));
						}
						//---------------------------------------------
						//recibir datos request
						egresoForm.setResumenEgresoAutomatico(UtilidadTexto.getBoolean(request.getParameter("resumenEgresoAutomatico")+""));
						egresoForm.setResumenSemiEgreso(UtilidadTexto.getBoolean(request.getParameter("resumenSemiEgreso")+""));		
						//----------------------------------------------



						//Existe un caso en el que se desee ver el resumen de un Egreso Automático
						//invocado desde la funcionalidad de resumen de Atenciones
						if(egresoForm.isResumenEgresoAutomatico())
						{
							//se vuelve a cambiar el resumenEgresoAutomatico
							egresoForm.setResumenEgresoAutomatico(false);
							//se levanta el indicador de las vistas
							egresoForm.setIndicador1(true);
							//también se levanta el indicador del semiEgreso
							egresoForm.setIndicador(true);

							egresoForm.setSemiEgresoCompleto(false);

							egresoForm.setAccionAFinalizar("insertarSemiEgreso");

							//Se carga la fecha y hora del egreso
							Egreso egreso=new Egreso();

							egreso.cargarSemiEgreso(con,egresoForm.getIdCuenta());

							egresoForm.setFechaEgreso(egreso.getFechaEgreso());
							egresoForm.setHoraEgreso(egreso.getHoraEgreso());
							egresoForm.setNombreUsuario(egreso.getUsuarioGrabaEgreso().getNombreUsuario());
							egresoForm.setLoginUsuario(egreso.getUsuarioGrabaEgreso().getLoginUsuario());
							//************************************************

							int codValoracionACargar=UtilidadValidacion.getCodigoValoracionInicial(con, egresoForm.getIdCuenta() );
							Valoraciones mundoValoracion = new Valoraciones();
							mundoValoracion.setNumeroSolicitud(codValoracionACargar+"");
							mundoValoracion.cargarUrgencias(con, medico, paciente, paciente.getCodigoPersona(), false);
							//Se toma el diagnostico Principal de un SemiEgreso*********************
							diagnostico=(Diagnostico)mundoValoracion.getValoracionUrgencias().getDiagnosticos().get(0);
							//**********************************************************************
							request.setAttribute("conducta",mundoValoracion.getValoracionUrgencias().getNombreConductaValoracion());
							request.setAttribute("valoracion" , mundoValoracion.getValoracionUrgencias());
							request.setAttribute("numDiagnosticos", new Integer(mundoValoracion.getValoracionUrgencias().getDiagnosticos().size()));


							if (con!=null&&!con.isClosed())
							{
								UtilidadBD.closeConnection(con);
							}
							return mapping.findForward("jspSemiEgreso");

						}
						//Existe un caso en que se desee ver el resumen de un semiEgreso
						//invocado desde la fucnionalidad de resumen de Atenciones
						if(egresoForm.isResumenSemiEgreso()){
							//Ahora enviamos el id de la Cuenta a la cual se le desea consultar su semiEgreso
							/*boolean esSemiEgreso[]=UtilidadValidacion.tieneSemiEgreso(con,egresoForm.getIdCuenta());
						if (esSemiEgreso[0])
						{*/
							//se vuelve a cambiar el resumenSemiEgreso
							egresoForm.setResumenSemiEgreso(false);
							//se levanta el indicador
							egresoForm.setIndicador(true);
							//se baja el indicador de las vistas del egreso automatico
							egresoForm.setIndicador1(false);
							//Estamos en un semiEgreso
							//ahora definimos si está completo o no
							egresoForm.setSemiEgresoCompleto(false);

							egresoForm.setAccionAFinalizar("insertarSemiEgreso");

							//Se carga la fecha y hora del egreso
							Egreso egreso=new Egreso();						
							egreso.cargarSemiEgreso(con,egresoForm.getIdCuenta());
							egresoForm.setFechaEgreso(egreso.getFechaEgreso());
							egresoForm.setHoraEgreso(egreso.getHoraEgreso());
							egresoForm.setNombreUsuario(egreso.getUsuarioGrabaEgreso().getNombreUsuario());
							egresoForm.setLoginUsuario(egreso.getUsuarioGrabaEgreso().getLoginUsuario());
							//************************************************

							int codValoracionACargar=UtilidadValidacion.getCodigoValoracionInicial(con, egresoForm.getIdCuenta() );
							Valoraciones mundoValoracion = new Valoraciones();
							mundoValoracion.setNumeroSolicitud(codValoracionACargar+"");
							mundoValoracion.cargarUrgencias(con, medico, paciente, paciente.getCodigoPersona(), false);

							//Se toma el diagnostico Principal de un SemiEgreso*********************
							diagnostico=(Diagnostico)mundoValoracion.getValoracionUrgencias().getDiagnosticos().get(0);
							//**********************************************************************
							request.setAttribute("valoracion" , mundoValoracion.getValoracionUrgencias());
							request.setAttribute("numDiagnosticos", new Integer(mundoValoracion.getValoracionUrgencias().getDiagnosticos().size()));
							if (con!=null&&!con.isClosed())
							{
								UtilidadBD.closeConnection(con);
							}
							return mapping.findForward("jspSemiEgreso");
							//}

						}
						//Existe un caso particular en el cual no 
						//se deben mostrar datos y es cuando nos
						//encontramos en un semiegreso.
						//Este solo puede existir si estamos en
						//una admision de urgencias
						if (paciente.getAnioAdmision()>0)
						{
							//se bajan los indicadores
							egresoForm.setIndicador(false);
							egresoForm.setIndicador1(false);

							//Ahora revisamos si hay o no
							boolean esSemiEgreso[]=UtilidadValidacion.tieneSemiEgreso(con, egresoForm.getIdCuenta());
							if (esSemiEgreso[0])
							{
								//Se carga la fecha y hora del egreso
								Egreso egreso=new Egreso();
								egreso.cargarSemiEgreso(con,egresoForm.getIdCuenta());
								egresoForm.setFechaEgreso(egreso.getFechaEgreso());
								egresoForm.setHoraEgreso(egreso.getHoraEgreso());
								egresoForm.setNombreUsuario(egreso.getUsuarioGrabaEgreso().getNombreUsuario());
								egresoForm.setLoginUsuario(egreso.getUsuarioGrabaEgreso().getLoginUsuario());

								//Estamos en un semiEgreso
								//ahora definimos si está completo o no
								egresoForm.setSemiEgresoCompleto(esSemiEgreso[1]);

								egresoForm.setAccionAFinalizar("insertarSemiEgreso");

								int codValoracionACargar=UtilidadValidacion.getCodigoValoracionInicial(con, egresoForm.getIdCuenta() );
								Valoraciones mundoValoracion = new Valoraciones();
								mundoValoracion.setNumeroSolicitud(codValoracionACargar+"");
								mundoValoracion.cargarUrgencias(con, medico, paciente, paciente.getCodigoPersona(), false);

								//Se asigna el tipo de monitoreo de la valoracion si de pronto la conducta a seguir fue traslado cuidado especial
								egresoForm.setCodigoTipoMonitoreo(mundoValoracion.getValoracionUrgencias().getCodigoTipoMonitoreo());
								//Se toma el diagnostico Principal de un SemiEgreso*********************
								diagnostico=(Diagnostico)mundoValoracion.getValoracionUrgencias().getDiagnosticos().get(0);
								//**********************************************************************
								request.setAttribute("valoracion" , mundoValoracion.getValoracionUrgencias());
								request.setAttribute("numDiagnosticos", new Integer(mundoValoracion.getValoracionUrgencias().getDiagnosticos().size()));
								if (con!=null&&!con.isClosed())
								{
									UtilidadBD.closeConnection(con);
								}
								return mapping.findForward("jspSemiEgreso");
							}

							//Si no es un semiEgreso, no hacemos nada, continúa la
							//ejecución del action normalmente


						}


						//Limpiamos la forma al empezar
						//int idCuenta = egresoForm.getIdCuenta();

						int cocu=paciente.getCodigoCuenta();
						
						if (cocu!=0)
						{
							egresoForm.setIdCuenta(paciente.getCodigoCuenta());
						}
						else
						{
							egresoForm.setIdCuenta(Utilidades.convertirAEntero(ingresos.get("numCuenta_"+(numReg-1))+""));
						}
						//En este punto ya se han validado varias cosas, falta saber si 
						//tiene o no orden de salida SIN egreso.
						if (!UtilidadValidacion.existeEgresoCompleto (con, egresoForm.getIdCuenta()))
						{
							ActionForward posibleActionForward=this.validarComunPuedeDarEgreso(mapping, request, con, egresoForm.getIdCuenta());
								if (posibleActionForward!=null)
							{
								//Si no dio nulo es porque algún error se presento, 
								//luego lo debemos retornar
								return posibleActionForward;
							}
						}
						else
							return this.funcionalidadResumen(mapping, con, egresoForm, session,ingresos.get("codigoIngreso_"+(numReg-1))+"",request,true);
						//Si estamos aca es porque cumplimos todos
						//los requisitos para poder dar el egreso

						Egreso egreso=new Egreso();
						egreso.cargarEgresoGeneral(con, egresoForm.getIdCuenta());

						//Tambien vamos a llenar el form con los datos que deseamos
						//conservar (las fechas) para el momento de finalizar y el
						//número de autorizacion
						egresoForm.setFechaEgreso(egreso.getFechaEgreso());
						egresoForm.setHoraEgreso(egreso.getHoraEgreso());
						logger.info("FECHA EGRESO CONSULTADA: "+egresoForm.getFechaEgreso());

						egresoForm.setFechaGrabacionEgreso(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
						egresoForm.setHoraGrabacionEgreso(UtilidadFecha.getHoraActual());

						egresoForm.setNumeroAutorizacion(egreso.getNumeroAutorizacion());
						String fechaHoraEgreso[]=UtilidadValidacion.obtenerMaximaFechaYHoraEvolucion(con,egresoForm.getIdCuenta());
						egresoForm.setFechaSalidaDePiso(UtilidadFecha.conversionFormatoFechaAAp(fechaHoraEgreso[0]));
						egresoForm.setHoraSalidaDePiso(UtilidadFecha.convertirHoraACincoCaracteres(fechaHoraEgreso[1]));

						ActionForward validacionFechasObservacion=this.funcionalidadValidarFechasObservacion(con, mapping, egresoForm, request, paciente);

						if (validacionFechasObservacion!=null)
						{
							return validacionFechasObservacion;
						}
						session.setAttribute("egreso", egreso);

						if (con!=null&&!con.isClosed())
						{
							UtilidadBD.closeConnection(con);
						}

						//Antes de irnos le decimos que la accion a finalizar
						//es insertar
						egresoForm.setAccionAFinalizar("insertar");				
						return mapping.findForward("principal");
					}
					else 
						if (estado.equals("resumen"))
						{
							return this.funcionalidadResumen(mapping, con, egresoForm, session,ingresos.get("codigoIngreso_"+(numReg-1))+"",request,false);
						}
						else
							if( estado.equals("cancelar") )
							{
								return this.accionEstadoCancelar(request, response, con, egresoForm);
							}
							else 
								if (estado.equals("salir"))
								{
									//si el estado es salir y el paciente ya tiene el ingreso cerrado se carga con ingreso cerrado
									logger.info("\n el numero ingreso es "+paciente.getCodigoIngreso());
									if (UtilidadCadena.noEsVacio(paciente.getCodigoIngreso()+"") && !(paciente.getCodigoIngreso()>0))
										paciente.cargarPacienteXingreso(con, ingresos.get("codigoIngreso_"+(numReg-1))+"", medico.getCodigoInstitucion(), ingresos.get("centroAtencion_"+(numReg-1))+"");

									logger.info("\n paciente admision --> "+paciente.getCodigoAdmision() +" anioadmin "+paciente.getAnioAdmision());

									if (egresoForm.getAccionAFinalizar()==null||egresoForm.getAccionAFinalizar().equals(""))
									{
										request.setAttribute("descripcionError", "No se puede finalizar el proceso porque la acción a terminar no ha sido especificada. Por favor utilice los menúes");
										if (con!=null&&!con.isClosed())
										{
											UtilidadBD.closeConnection(con);
										}
										return mapping.findForward("paginaError");
									}
									else 
										if (egresoForm.getAccionAFinalizar().equals("insertarSemiEgreso"))
										{
											//POR AQUÍ ENTRAN PACIENTES DE URGENCIAS CON CONDUCTA A SEGUIR:
											//Sala de Espera - Sala de Cirugia - Interconsulta




											//Ahora revisamos si hay o no
											boolean esSemiEgreso[]=UtilidadValidacion.tieneSemiEgreso(con, egresoForm.getIdCuenta());
											if (esSemiEgreso[0])
											{
												//Si NO ha sido llenado aún
												if (!esSemiEgreso[1])
												{

													Egreso semiEgreso=new Egreso();
													semiEgreso.setNumeroCuenta(egresoForm.getIdCuenta());
													semiEgreso.setUsuarioGrabaEgreso(medico);
													semiEgreso.setDiagnosticoDefinitivoPrincipal(diagnostico);
													semiEgreso.setCodigoTipoMonitoreo(egresoForm.getCodigoTipoMonitoreo());
													try
													{
														if (semiEgreso.completarSemiEgreso(con)>0)
														{
															if (con!=null&&!con.isClosed())
															{
																UtilidadBD.closeConnection(con);
															}
															return mapping.findForward("semiEgresoExitoso");
														}
														else
														{
															return this.funcionalidadEnviarError(con, mapping, egresoForm, request, "No se pudo completar el semiEgreso", "errors.problemasBd", true);
														}
													}
													catch (SQLException e)
													{
														if (con!=null&&!con.isClosed())
														{
															UtilidadBD.closeConnection(con);
														}
														return this.funcionalidadEnviarError(con, mapping, egresoForm, request, "No se pudo completar el semiEgreso. Excepcion " + e.toString(), "errors.problemasBd", true);
													}

												}
												else 
												{
													//Si no es un semiEgreso, u otro usuario lo lleno previamente
													//o alguien se metio sin usar el menu
													return this.funcionalidadEnviarError(con, mapping, egresoForm, request, "Al tratar de finalizar la acción de guardar un semi-egreso, resulto ya estar respondido", "Algun usuario con privilegios similares ha llenado este egreso", false);
												}

											}
											else
											{
												//No esta en medio de un semiegreso, si esto ocurre
												//alguien intentó acceder por el menú a la aplicación 
												return this.funcionalidadEnviarError(con, mapping, egresoForm, request, "Usuario intento acceder a la funcionalidad de (salir) insertar semi-egreso sin usar los menus", "Por favor utilize los menués", false);
											}

										}
										else if (egresoForm.getAccionAFinalizar().equals("insertar"))
										{
											//Al terminar, justo antes de hacer los cambios también debemos
											//validar, de otro modo puede cambiar las cosas mientras se llenan
											//los datos

											//En este punto ya se han validado varias cosas, falta saber si 
											//tiene o no orden de salida SIN egreso.
											ActionForward posibleActionForward=this.validarComunPuedeDarEgreso (mapping, request, con, Utilidades.convertirAEntero(ingresos.get("numCuenta_"+(numReg-1))+""));
											if (posibleActionForward!=null)
											{
												//Si no dio nulo es porque algún error se presento, 
												//luego lo debemos retornar
												return posibleActionForward;
											}

											ActionForward validacionFechasObservacion=this.funcionalidadValidarFechasObservacion(con, mapping, egresoForm, request, paciente);

											if (validacionFechasObservacion!=null)
											{

												return validacionFechasObservacion;
											}

											//Si no se salio por allí, es porque no había ningún problema

											Egreso egreso=new Egreso();
											egreso.setNumeroCuenta(egresoForm.getIdCuenta());
											egreso.setFechaEgreso(egresoForm.getFechaEgreso());
											egreso.setFechaGrabacionEgreso(egresoForm.getFechaGrabacionEgreso());
											egreso.setHoraEgreso(egresoForm.getHoraEgreso());
											egreso.setHoraGrabacionEgreso(egresoForm.getHoraGrabacionEgreso());
											egreso.setUsuarioGrabaEgreso(medico);
											egreso.setNumeroAutorizacion(egresoForm.getNumeroAutorizacion());

											int entero1=egreso.modificarEgresoUsuarioFinalizar(con, ConstantesBD.inicioTransaccion);

											boolean actualizarNuevosDatos=egreso.guardarDatosNuevos(con, egresoForm.getDatosNuevos());
											int entero2=1, entero3=0;
											boolean actualizoTraslado= false;
											if (paciente.getAnioAdmision()<1)
											{

												//AdmisionHospitalaria admisionHospitalaria=new AdmisionHospitalaria();
												//admisionHospitalaria.init(System.getProperty("TIPOBD"));
												logger.info(" \n codigo de admision del paciene -->"+paciente.getCodigoAdmision());
												//entero2=admisionHospitalaria.actualizarPorEgresoTransaccional(con, paciente.getCodigoAdmision(), ConstantesBD.continuarTransaccion, medico.getCodigoInstitucionInt());

												////-----------Actualizar el estado a Finalizado de un Ingreso Cuidados Especiales------------////
												///----------------------------segun via ingreso y tipo paciente-----------------------------////
												if(paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoHospitalizacion && paciente.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado))
												{
													if(UtilidadesManejoPaciente.existeIngresoCuidadoEspecialActivo(con, paciente.getCodigoIngreso(), ""))
													{
														if(Utilidades.actualizarEstadoCuidadosEspeciales(con, paciente.getCodigoIngreso(),medico.getLoginUsuario(),egresoForm.getFechaEgreso(),egresoForm.getHoraEgreso()))
															entero3 = 1;
														else
															entero3 = 0;
													}
													else
														entero3 = 1;
												}
												else
													entero3=1;
												//---------------------------------------------------------------------------------------------//

												//Si es una admisión hospitalaria puede cambiar el estado y 
												//es necesario recargar el paciente. Por eso usamos el observer

												// Código necesario para notificar a todos los observadores que la cuentadel paciente en sesión pudo haber cambiado
												UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),getServlet().getServletContext());

												//Si la admision hospitalaria no es Hospital Día entonces se prosigue a liberar la cama
												if(!paciente.isHospitalDia())
												{
													int codigoCama=0;
													//se carga la ultima cama
													codigoCama = Utilidades.getUltimaCamaTraslado(con,egresoForm.getIdCuenta());
													//se cambia el estado de la cama

													logger.info("CODIGO DE LA CAMA=> "+codigoCama);
													if(codigoCama!=0)
													{
														////////////////////////////////////////////////////////////////////////////////////////////////////
														//cambio por anexo 658
														logger.info("\n getLiberarCamaHospitalizacionDespuesFacturar --> "+ValoresPorDefecto.getLiberarCamaHospitalizacionDespuesFacturar(medico.getCodigoInstitucionInt()));
														//segun lo hablado con margarita el 31 de marzo de 2011, la boleta de salida siempre debe liberar la cama
														//if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getLiberarCamaHospitalizacionDespuesFacturar(medico.getCodigoInstitucionInt())))
														{
															logger.info("\n entre a actualizar la cama 1");
															Cama cama = new Cama();
															cama.cambiarEstadoCama(con,codigoCama+"",Integer.parseInt(ValoresPorDefecto.getCodigoEstadoCama(medico.getCodigoInstitucionInt())+""));
															logger.info("\n entre a actualizar la cama 2");
															//ACTUALIZACION DE LA ESTANCIA DE LA CAMA
															TrasladoCamas objetoTraslado= new TrasladoCamas();
															actualizoTraslado=objetoTraslado.actualizarFechaHoraFinalizacion(con, egresoForm.getIdCuenta(), egresoForm.getFechaSalidaDePiso(), egresoForm.getHoraSalidaDePiso(), ConstantesBD.continuarTransaccion,"");
															//////////////////////////////////////
															logger.info("\n entre a actualizar la cama 3");
														}
														//else
														//actualizoTraslado=true;
														logger.info("\n entre a actualizar la cama 4");
														///////////////////////////////////////////////////////////////////////////////////////////////////////////////
													}
//													else 
//														if(paciente.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))
//															actualizoTraslado=true;
//													logger.info("\n entre a actualizar la cama 5");

												}
												else
													actualizoTraslado = true;

												logger.info("\n entre a actualizar la cama 6");
											}
											else
											{
												logger.info("\n entre a actualizar la cama 7");
												//AdmisionUrgencias admisionUrgencias=new AdmisionUrgencias();

												//admisionUrgencias.init(System.getProperty("TIPOBD"));
												//y actualizar la admision hospitalaria con fecha y hora de observación
												//de la evolucion - La fecha de la ultima evolucion la tomamos del egreso
												//entero2=admisionUrgencias.actualizarPorOrdenSalidaTransaccional(con, paciente.getCodigoAdmision(), paciente.getAnioAdmision(), egreso.getFechaEgreso(), egreso.getHoraEgreso(), ConstantesBD.continuarTransaccion);
												logger.info("entero2: "+entero2);
												//entero3=admisionUrgencias.actualizarPorEgresoTransaccional(con, paciente.getCodigoAdmision(), paciente.getAnioAdmision(), ConstantesBD.continuarTransaccion, medico.getCodigoInstitucionInt());
												actualizoTraslado=true;
												entero3 = 1;
											}

											logger.info("entero1: "+entero1);
											logger.info("entero2: "+entero2);
											logger.info("entero3: "+entero3);
											logger.info("actualizoTraslado: "+actualizoTraslado);

											if (entero1<1||entero2<1||entero3<1|| !actualizoTraslado && !actualizarNuevosDatos)
											{
												request.setAttribute("descripcionError", "Error en la ejecución del cambio en el egreso ");
												UtilidadBD.abortarTransaccion(con);
												if (con!=null&&!con.isClosed())
												{
													UtilidadBD.closeConnection(con);
												}
												return mapping.findForward("paginaError");
											}
											else
											{
												UtilidadBD.finalizarTransaccion(con);
												///*********Operación para deshabilitar la cama del paciente********************************************************

												//Quitar la cama en el encabezado de paciente
												PersonaBasica pacienteActivo = (PersonaBasica)session.getAttribute("pacienteActivo");
												pacienteActivo.setCama("");
												UtilidadesManejoPaciente.cargarPaciente(con, medico, pacienteActivo, request);
												//****************************************************************
											}

											String paginaSiguiente=request.getParameter("paginaSiguiente");
											if (paginaSiguiente==null||paginaSiguiente.equals(""))
											{
												if (con!=null&&!con.isClosed())
												{
													UtilidadBD.closeConnection(con);
												}
												egresoForm.setMensajeResumen("Egreso creado satisfactoriamente");
												egresoForm.setAcabaAparecerResumen(true);

												return mapping.findForward("resumen");
											}
											else
											{
												egresoForm.reset();
												if (con!=null&&!con.isClosed())
												{
													UtilidadBD.closeConnection(con);
												}
												response.sendRedirect(paginaSiguiente);
											}



										}
										else
										{
											return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion específicada no esta definida ", "errors.estadoInvalido", true);
										}
								}
								else//----------------------- Estado --> guardarDatosNuevos------------------------------------------------------
									if (estado.equals("guardarDatosNuevos"))
									{
										logger.info("\n entre a  guardarDatosNuevos   "+egresoForm.getDatosNuevos());
										egresoForm.setDatosNuevos(indicesFactura[12], ingresos.get("numCuenta_"+(numReg-1)));
										Egreso.guardarDatosNuevos(con, egresoForm.getDatosNuevos());
										egresoForm.resetNuevosDatos();
										this.funcionalidadResumen(mapping, con, egresoForm, session,ingresos.get("codigoIngreso_"+(numReg-1))+"",request,false);
										egresoForm.setEstado("guardarDatosNuevos");
										return mapping.findForward("datosNuevos");
									}
									else//----------------------- Estado --> imprimirBoletaSalida------------------------------------------------------
										if (estado.equals("imprimirBoletaSalida"))
										{

											return Egreso.imprimirBoletaSalida(con, medico, egresoForm,ingresos.get("codigoIngreso_"+(numReg-1))+"", request, mapping);

										}

			}
			else
			{
				//Todavía no existe conexión, por eso no se cierra
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}


	/**
	 * Este método se encarga de manejar la funcionalidad necesaria
	 * (Que es practicamente comun entre actions) para manejar el
	 * estado cancelar
	 * 
	 * @param mapping Mapa (control) de Struts
	 * @param request request de la pagina web
	 * @param response response de la pagina web
	 * @param con Conexión con la fuente de datos
	 * @param egresoForm Forma de Egreso
	 * @return
	 * @throws Exception
	 */
	public ActionForward accionEstadoCancelar(HttpServletRequest request, HttpServletResponse response, Connection con, EgresoForm egresoForm)throws Exception
	{
		if( logger.isDebugEnabled() )
		{
			logger.debug("La acción especificada es cancelar");
		}

		String paginaSiguiente=request.getParameter("paginaSiguiente");
		egresoForm.reset();
		if (con!=null&&!con.isClosed())
		{
            UtilidadBD.closeConnection(con);
		}
		response.sendRedirect(paginaSiguiente);

		return null;
	}


	/**
	 * Este método se encargar de encapsular la validación de poder dar o no egreso.
	 * Se creo como método porque es necesario llamarlo en dos partes del action, así
	 * que se encapsulo para que cualquier cambio se vea reflejado en los dos sitios
	 * (Y para que el Action sea más fácil de entender)
	 * 
	 * @param mapping
	 * @param request
	 * @param con
	 * @param paciente
	 * @return
	 * @throws SQLException
	 */
	public ActionForward validarComunPuedeDarEgreso (ActionMapping mapping, HttpServletRequest request, Connection con, int idCuenta) throws SQLException
	{
				
		RespuestaValidacion resp1=UtilidadValidacion.puedoFinalizarEgreso (con, idCuenta);
			
		if (!resp1.puedoSeguir)
		{
			if (con!=null&&!con.isClosed())
			{
                UtilidadBD.closeConnection(con);
			}
			
			request.setAttribute("descripcionError", resp1.textoRespuesta);
			return mapping.findForward("paginaError");
		}
		
		/**
		 * NOTA * ESTA VALIDACION SE QUITA POR [id=47364] de xplanner 3
		 * 
		if (paciente.getAnioAdmision()>0)
		{
			//Si la conducta a seguir de la valoración de urgencias es
			//cama de observación, debe tener cama asignada
			//Si no es cama de observacion retornamos nulo y ahorramos
			//una validación
			if (UtilidadValidacion.tieneValoracionConConductaCamaObservacion(con, idCuenta))
			{

				//Si es una admisión de urgencias, no puede hacer el egreso
				//a menos que tenga una cama de observación con sus
				//datos de ingreso completos (los datos de egreso de obs
				//se llenan en esta clase)
				if (!UtilidadValidacion.tieneCamaParaEgresoUrgencias(con, paciente.getCodigoAdmision(), paciente.getAnioAdmision()))
				{
					if( logger.isDebugEnabled() )
					{
						logger.debug("paciente admisión urgencias sin cama seleccionada");
					}
					if (con!=null&&!con.isClosed())
					{
	                    UtilidadBD.closeConnection(con);
					}
					request.setAttribute("descripcionError", "El paciente ha estado en cama de observación, sin embargo no se ha seleccionado cama. Por favor asignela a través de la funcionalidad asignación cama urgencias");
					return mapping.findForward("paginaError");
				}
				
			}
		}**/

		
		return null;
	}

	public ActionForward funcionalidadResumen (ActionMapping mapping, Connection con, EgresoForm egresoForm, HttpSession session,String ingreso,HttpServletRequest request,boolean inicio)throws Exception
	{
		//Limpiamos el form antes de empezar
		int idCuenta = egresoForm.getIdCuenta();
		
		egresoForm.reset();
		if(inicio)
		{
			egresoForm.setEstado("empezar");
		}
		else
		{
			egresoForm.setEstado("");
		}
		//---------------------------------------------
		//recibir datos request
		egresoForm.setResumenEgresoAutomatico(UtilidadTexto.getBoolean(request.getParameter("resumenEgresoAutomatico")+""));
		egresoForm.setResumenSemiEgreso(UtilidadTexto.getBoolean(request.getParameter("resumenSemiEgreso")+""));		
		//----------------------------------------------
		egresoForm.setIdCuenta(idCuenta);
		Egreso egreso=new Egreso();
				
		if (egresoForm.isAcabaAparecerResumen())
		{
			egresoForm.setAcabaAparecerResumen(false);
		}
		else
		{
			egresoForm.setMensajeResumen("El egreso ya existia, a continuación encuentra un resumen del mismo");
		}
		egreso.cargarEgresoGeneral(con, egresoForm.getIdCuenta());
		egresoForm.setDatosNuevos(egreso.consultarNuevosDatosEgreso(con, egresoForm.getIdCuenta()+""));
		
		HashMap datos = new HashMap ();
		datos.put(indicesFactura[5], ingreso);
		Listado.copyMap(Egreso.consultaDatosFactura(con, datos), egresoForm.getDatosNuevos(), indicesFactura);
		//egresoForm.setDatosNuevos();
		logger.info("\n nuevos datos -->"+egresoForm.getDatosNuevos());
		egresoForm.setDatosIngresos("cuentas", UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, ingreso));
		
		
		//Aunque no se necesita en sesión, como en ingreso SI
		//lo necesitamos en sesión, aquí también lo vamos a hacer
		session.setAttribute("egreso", egreso);
		if (con!=null&&!con.isClosed())
		{
            UtilidadBD.closeConnection(con);
		}
		
		logger.info("ESTADO FORM --> "+egresoForm.getEstado());
		logger.info("es resumen semi egreso --> "+egresoForm.isResumenSemiEgreso()+" isResumenEgresoAutomatico()"+egresoForm.isResumenEgresoAutomatico());
		if(egresoForm.isResumenSemiEgreso() || egresoForm.isResumenEgresoAutomatico())
		{
			return mapping.findForward("jspSemiEgreso");
		}
		else
			return mapping.findForward("resultadoEgreso");
	}


	/**
	 * Este método se encarga de manejar la funcionalidad correspondiente 
	 * al manejo de errores (Guardar mensaje error, cerrar conexión y mostrar
	 * mensaje error al usuario)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param mapping Mapping de las localizaciones en struts
	 * @param egresoForm Forma de esta funcionalidad
	 * @param request Objeto request
	 * @param textoLog Texto a guardar en el log
	 * @param textoUsuario Texto a mostrar al usuario
	 * @param esCodigoError, true si textoUsiario es un codigo/id de error,
	 * false de lo contrario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward funcionalidadEnviarError (Connection con, ActionMapping mapping, EgresoForm egresoForm, HttpServletRequest request, String textoLog, String textoUsuario, boolean esCodigoError) throws SQLException
	{
		egresoForm.reset();
		if( logger.isDebugEnabled() )
		{
			logger.debug(textoLog);
		}
		if (con!=null&&!con.isClosed())
		{
            UtilidadBD.closeConnection(con);
		}
		
		if( esCodigoError )
			request.setAttribute("codigoDescripcionError", textoUsuario);
		else
			request.setAttribute("descripcionError", textoUsuario);
		return mapping.findForward("paginaError");
	}

	private ActionForward funcionalidadValidarFechasObservacion (Connection con, ActionMapping mapping, EgresoForm egresoForm, HttpServletRequest request, PersonaBasica paciente) throws SQLException
	{
		//Validamos fechas, pero solo si estamos en admision de urgencias
		//y en cama de observacion (Lo sabemos si la cama no es vacia ni nula)
		if (paciente.getAnioAdmision()>0&& !Admision.getCama(
				con,
				paciente.getCodigoAdmision(),
				paciente.getCodigoUltimaViaIngreso(),
				paciente.getCodigoPersona() ).equals(""))
		{

			String fechaYHoraIngresoObservacion[]=UtilidadValidacion.obtenerFechayHoraObservacionAdmisionUrgencias(con, paciente.getCodigoAdmision(), paciente.getAnioAdmision());

				
			if (fechaYHoraIngresoObservacion[0]==null||fechaYHoraIngresoObservacion[1]==null||egresoForm.getFechaEgreso()==null||egresoForm.getHoraEgreso()==null)
			{
				return this.funcionalidadEnviarError(con, mapping, egresoForm, request, "Alguno de los campos (fecha y hora de observación- Datos llenados por el usuario en el egreso) es nulo. No se pudo validar que la fecha/hora de salida de observación fuese mayor a la de entrada a observacion", "Error en la grabacion del egreso, No se pudo validar que la fecha/hora de salida de observación fuese mayor a la de entrada a observacion", false);
			}
			else if (fechaYHoraIngresoObservacion[0].compareTo(egresoForm.getFechaEgreso())>0)
			{
				return this.funcionalidadEnviarError(con, mapping, egresoForm, request, "Fecha de ingreso a observacion mayor a fecha de egreso de observacion", "La fecha de ingreso a observación es mayor a la fecha de egreso de observación. Asigne un valor válido a la fecha de ingreso a observación (asignar cama de urgencias)", false);
			}
			else if (fechaYHoraIngresoObservacion[0].compareTo(egresoForm.getFechaEgreso())==0&&fechaYHoraIngresoObservacion[1].compareTo(egresoForm.getHoraEgreso())>0)
			{
				return this.funcionalidadEnviarError(con, mapping, egresoForm, request, "Fecha de ingreso a observacion mayor a fecha de egreso de observacion", "La fecha de ingreso a observación es mayor a la fecha de egreso de observación. Asigne un valor válido a la fecha de ingreso a observación (asignar cama de urgencias)", false);
			}
			else
			{
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	
	
	
	
	
	

}
