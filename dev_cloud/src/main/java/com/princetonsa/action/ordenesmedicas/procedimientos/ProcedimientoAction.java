/*
 * @(#)ProcedimientoAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.action.ordenesmedicas.procedimientos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.laboratorios.InterfazLaboratorios;
import util.laboratorios.UtilidadLaboratorios;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.ordenesmedicas.procedimientos.ProcedimientoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Cama;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.ordenesmedicas.procedimientos.RespuestaProcedimientos;
import com.princetonsa.mundo.ordenesmedicas.solicitudes.Procedimiento;
import com.princetonsa.mundo.solicitudes.DocumentoAdjunto;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;
import com.princetonsa.mundo.solicitudes.ValidacionesSolicitud;
import com.princetonsa.pdf.RespuestaProcedimientoPdf;
import com.princetonsa.pdf.ResumenGuardarProcedimientoPdf;

/**
 * Action, controla todas las opciones dentro de el módulo de procedimientos
 * (responder, modificar, interpretar), incluyendo los posibles casos de error.
 * Y los casos de flujo.
 * @version 1.0, Febrero 25 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
 * HttpServletRequest)
 */
public class ProcedimientoAction extends Action
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(ProcedimientoAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */	
	public ActionForward execute(	ActionMapping mapping, 
														ActionForm form, 
														HttpServletRequest request,
														HttpServletResponse response) throws Exception
														{

		Connection con = null;
		try {
			if( form instanceof ProcedimientoForm )
			{
				ProcedimientoForm procedimientoForm = (ProcedimientoForm)form;
				String estado = procedimientoForm.getEstado();

				int tmp=procedimientoForm.getInd();
				int numSol=procedimientoForm.getNumeroSolicitud();
				logger.info("El numero de solictud el action=>"+numSol);

				String tipoBD;
				try
				{
					tipoBD = System.getProperty("TIPOBD");
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					con = myFactory.getConnection();
				}
				catch(Exception e)
				{
					e.printStackTrace();
					logger.error("No se pudo acceder a la base de datos "+e.getMessage());

					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				HttpSession session=request.getSession();			
				UsuarioBasico medico = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				if(numSol>0)
				{
					int codigoPac= UtilidadesHistoriaClinica.obtenerCodigoPacienteSolicitud(con, ""+numSol);
					paciente.setCodigoPersona(codigoPac);
					paciente.cargar(con,codigoPac);
					paciente.cargarPaciente(con, codigoPac, medico.getCodigoInstitucion(),medico.getCodigoCentroAtencion()+"");
				}

				if( medico == null )
				{
					procedimientoForm.reset();
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe el usuario", "errors.usuario.noCargado", true);
				}
				else
					if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
					{
						procedimientoForm.reset();
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
					}
					else	
						if( estado == null )
						{
							procedimientoForm.reset();
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "La accion específicada no esta definida", "errors.estadoInvalido", true);
						}

				logger.info("El estado en el ProcedimientoAction entendible->"+estado);

				/*else*/ if( estado.equals("cancelar") )
				{
					UtilidadBD.cerrarConexion(con);
					procedimientoForm.reset();
					return null;
				}

				else
					if( !UtilidadValidacion.esProfesionalSalud(medico) && 
							!Utilidades.esCentroCostoRespuestaProcTercero(con,medico.getCodigoCentroCosto()).equals(ConstantesBD.acronimoSi))
					{					
						UtilidadBD.closeConnection(con);
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Profesional de la salud no válido "+medico.getLoginUsuario(), "errors.usuario.noAutorizado", true);				
					}
				/**
				 * Validar concurrencia
				 * Si ya está en proceso de distribucion, no debe dejar entrar
				 **/
					else if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), medico.getLoginUsuario()) )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
					}
				/**
				 * Validar concurrencia
				 * Si ya está en proceso de facturación, no debe dejar entrar
				 **/
					else if(!estado.equals("imprimir")&&UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
					}
					else
						if( procedimientoForm.getNumeroSolicitud() < 1 )
						{
							procedimientoForm.reset();
							ArrayList atributosError = new ArrayList();
							atributosError.add("El número de solicitud");
							request.setAttribute("atributosError", atributosError);

							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Número de solicitud inválido "+procedimientoForm.getNumeroSolicitud(), "errors.invalid", true);
						}
						else
						{
							ValidacionesSolicitud validaciones = new ValidacionesSolicitud(con, procedimientoForm.getNumeroSolicitud(), medico, paciente);

							ResultadoBoolean permisosResponder = validaciones.puedoResponder();					
							ResultadoBoolean permisosModificar = validaciones.puedoModificarSolicitudRespondida();


							//********ESTADOS CUANDO ES CENTRO DE COSTO EXTERNO********************************************************************

							SolicitudProcedimiento solicitudProcedimiento = new SolicitudProcedimiento();
							boolean resCargar=solicitudProcedimiento.cargarSolicitudProcedimiento(con, procedimientoForm.getNumeroSolicitud());
							// ESTADO EMPEZAR CUANDO ES SOLICITUD MÚLTIPLE --------------------------------------------------------------------------

							logger.warn("\n\nEl estado en Procedimientos es------->"+estado+"\n");

							if(solicitudProcedimiento.getMultiple() && resCargar && estado.equals("empezar"))
							{
								procedimientoForm.reset();

								// En esta etapa inicial de responder se postulan todos los datos que se debe
								procedimientoForm.setFechaEjecucion(UtilidadFecha.getFechaActual());
								procedimientoForm.setHoraEjecucion(UtilidadFecha.getHoraActual());
								procedimientoForm.setCodigoTipoRecargo(ConstantesBD.codigoTipoRecargoSinRecargo);
								Solicitud solicitud = new Solicitud();
								solicitud.cargar(con, procedimientoForm.getNumeroSolicitud());
								procedimientoForm.setFechaSolicitud(UtilidadFecha.conversionFormatoFechaAAp(solicitud.getFechaSolicitud()));
								procedimientoForm.setHoraSolicitud(solicitud.getHoraSolicitud());
								//solicitud.cargarNumeroAutorizacion(con, procedimientoForm.getNumeroSolicitud());
								//procedimientoForm.setNumeroAutorizacion(solicitud.getNumeroAutorizacion());
								procedimientoForm.setMultiple(true);
								UtilidadBD.cerrarConexion(con);
								return mapping.findForward("paginaPrincipal");
							}
							// ESTADO MODIFICAR -----------------------------------------------------------------------------------------------------
							if( permisosModificar.isTrue() && estado.equals("modificar") )
							{
								Procedimiento procedimiento = new Procedimiento();					
								procedimientoForm.reset();
								procedimiento.cargar(con, procedimientoForm.getNumeroSolicitud());
								this.cargarForm(con,procedimientoForm, procedimiento, true,medico,request);
								UtilidadBD.cerrarConexion(con);
								return mapping.findForward("paginaModificar");
							}
							// ESTADO EMPEZAR (cuando hay permisos para responder)-------------------------------------------------------------------
							if( permisosResponder.isTrue() && estado.equals("empezar") )
							{
								procedimientoForm.reset();

								// En esta etapa inicial de responder se postulan todos los datos que se debe
								procedimientoForm.setFechaEjecucion(UtilidadFecha.getFechaActual());
								procedimientoForm.setHoraEjecucion(UtilidadFecha.getHoraActual());
								procedimientoForm.setCodigoTipoRecargo(ConstantesBD.codigoTipoRecargoSinRecargo);
								Solicitud solicitud = new Solicitud();
								solicitud.cargar(con, procedimientoForm.getNumeroSolicitud());
								procedimientoForm.setFechaSolicitud(UtilidadFecha.conversionFormatoFechaAAp(solicitud.getFechaSolicitud()));
								procedimientoForm.setHoraSolicitud(solicitud.getHoraSolicitud());
								//solicitud.cargarNumeroAutorizacion(con, procedimientoForm.getNumeroSolicitud());
								//procedimientoForm.setNumeroAutorizacion(solicitud.getNumeroAutorizacion());

								//Se carga servicio y descripcion del procedimiento
								SolicitudProcedimiento solProcedimiento = new SolicitudProcedimiento();
								solProcedimiento.cargarSolicitudProcedimiento(con,procedimientoForm.getNumeroSolicitud());
								procedimientoForm.setNombreServicio("asdgahsgdasd" +solProcedimiento.getCodigoServicioSolicitado()+"-"+solProcedimiento.getEspecialidadSolicitada().getCodigo()+" "+solProcedimiento.getNombreServicioSolicitado());
								UtilidadBD.cerrarConexion(con);
								return mapping.findForward("paginaPrincipal");
							}
							// ESTADO IMPRIMIR -----------------------------------------------------------------------------------------------------
							else if(estado.equals("imprimir") )
							{				
								logger.info("imprimir formato");
								cargarRespuestaConFormatoParam(con, procedimientoForm,paciente, medico, request);
								UtilidadBD.closeConnection(con);
								return mapping.findForward("imprimirFormato");		
								//return this.accionImprimir(con, procedimientoForm.getNumeroSolicitud(), mapping, medico, paciente, request);
							}
							// ESTADO IMPRIMIR CON EL FORMATO PARAMETRIZABLE--------------------------------------------------------------------------
							else if(estado.equals("imprimirFormato") )
							{				
								cargarRespuestaConFormatoParam(con, procedimientoForm,paciente, medico, request);
								UtilidadBD.closeConnection(con);
								return mapping.findForward("imprimirFormato");						
							}
							else if (estado.equals("recargarPlantilla"))
							{
								UtilidadBD.closeConnection(con);
								return mapping.findForward("seccionesValoresParametrizables");
							}
							// ESTADO FINALIZAR ----------------------------------------------------------------------------------------------------------------------
							else if( (permisosResponder.isTrue() && estado.equals("finalizar")) || (procedimientoForm.getMultiple() && estado.equals("finalizar")))
							{
								SolicitudProcedimiento nuevaSol=new SolicitudProcedimiento();
								nuevaSol.cargarSolicitudProcedimiento(con, procedimientoForm.getNumeroSolicitud());
								if(nuevaSol.getMultiple() && nuevaSol.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCRespondida)
								{
									nuevaSol.setMultiple(false);
									int nuevaSolicitud=nuevaSol.insertarTransaccional(con, ConstantesBD.continuarTransaccion, nuevaSol.getNumeroDocumento(), paciente.getCodigoCuenta(), false,ConstantesBD.codigoNuncaValido+"");
									//logger.info("nuevaSolicitud "+nuevaSolicitud);
									procedimientoForm.setNumeroSolicitud(nuevaSolicitud);

									//****************LLAMADO A INTERFAZ DE LABORATORIOS***************************
									HashMap infoInterfaz = new HashMap();
									//Se llenan datos necesarios para interfaz laboratorios
									infoInterfaz.put("numeroDocumento",nuevaSol.getNumeroDocumento()+"");
									infoInterfaz.put("fechaSolicitud",nuevaSol.getFechaSolicitud());
									infoInterfaz.put("horaSolicitud",nuevaSol.getHoraSolicitud());
									infoInterfaz.put("codigoMedico",nuevaSol.getCodigoMedicoSolicitante()+"");
									infoInterfaz.put("nombreMedico",UtilidadValidacion.obtenerNombrePersona(con,nuevaSol.getCodigoMedicoSolicitante()));
									infoInterfaz.put("institucion",medico.getCodigoInstitucion());
									infoInterfaz.put("observaciones",nuevaSol.getComentario());
									Cama cama = new Cama();
									cama.cargarCama(con,paciente.getCodigoCama()+"");
									infoInterfaz.put("numeroCama",cama.getDescripcionCama());
									if(ValoresPorDefecto.getCliente().equals(ConstantesBD.clienteSHAIO))
									{
										//infoInterfaz.put("comentarioDiagnostico", UtilidadesHistoriaClinica.obtenerDescripcionDxIngresoPaciente(con, paciente.getCodigoCuenta(), paciente.getCodigoUltimaViaIngreso()));
										infoInterfaz.put("horaSistema",UtilidadFecha.getHoraSegundosActual(con));
										infoInterfaz.put("nitEmpresa",UtilidadesFacturacion.obtenerNitEmpresaConvenio(con, paciente.getCodigoConvenio()));
										infoInterfaz.put("nroCarnet",UtilidadesManejoPaciente.obtenerNroCarnetIngresoPaciente(con, paciente.getCodigoIngreso()));
										infoInterfaz.put("codigoEspecialidadSolicitante",nuevaSol.getEspecialidadSolicitante().getCodigo());
										//infoInterfaz.put("ciePrevio",Utilidades.consultarDiagnosticosPaciente(con, paciente.getCodigoCuenta()+"", paciente.getCodigoUltimaViaIngreso()));
										//infoInterfaz.put("habitacionCama",cama.getCodigoDescriptivoHabitacion());

									}
									infoInterfaz.put("numeroSolicitud_0",nuevaSol.getNumeroSolicitud()+"");

									infoInterfaz.put("estado_0",ConstantesBD.codigoEstadoHCSolicitada+"");
									infoInterfaz.put("centroCosto_0",nuevaSol.getCentroCostoSolicitado().getCodigo()+"");
									infoInterfaz.put("urgente_0",nuevaSol.getUrgente()+"");
									if(ValoresPorDefecto.getCliente().equals(ConstantesBD.clienteSHAIO))
									{
										infoInterfaz.put("codigoLaboratorio_0",UtilidadLaboratorios.obtenerCodigoLaboratorioServicio(con, nuevaSol.getCodigoServicioSolicitado()));
										HashMap informacionServ = Utilidades.consultarInformacionServicio(con, nuevaSol.getCodigoServicioSolicitado()+"", medico.getCodigoInstitucionInt());
										infoInterfaz.put("codigoCUPS_0",informacionServ.get("codigopropietario")+"");
										infoInterfaz.put("nombreServicio_0",informacionServ.get("nombre")+"");
									}
									else
									{
										infoInterfaz.put("codigoCUPS_0",Utilidades.obtenerCodigoPropietarioServicio(con,nuevaSol.getCodigoServicioSolicitado()+"",ConstantesBD.codigoTarifarioCups));
									}
									infoInterfaz.put("numRegistros","1");
									InterfazLaboratorios.generarRegistroArchivo(infoInterfaz,paciente,ValoresPorDefecto.getCliente(),new ActionErrors());
									//**************************************************************************
								}

								Procedimiento procedimiento = new Procedimiento();
								this.cargarObjeto(procedimientoForm, procedimiento, false, medico);

								ResultadoBoolean resultado = procedimiento.insertarModificar(con, procedimientoForm.getNumeroSolicitud(),medico,paciente);


								if(Utilidades.esSolicitudPYP(con,procedimientoForm.getNumeroSolicitud())&&!procedimientoForm.isVieneDePyp())
								{
									logger.info("\n\n\n\n\nACTUALIZANDO SOLICITUDES PYP\n\n\n\n\n\n");
									String codigoActividad=Utilidades.obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(con,procedimientoForm.getNumeroSolicitud());
									Utilidades.actualizarEstadoActividadProgramaPypPaciente(con,codigoActividad,ConstantesBD.codigoEstadoProgramaPYPEjecutado,medico.getLoginUsuario(),"");
									//Utilidades.actualizarAcumuladoPYP(con,procedimientoForm.getNumeroSolicitud()+"",medico.getCodigoCentroAtencion()+"");
								}
								else
									procedimientoForm.setVieneDePyp(true);


								if( resultado.isTrue() ) // Inserción exitosa
								{
									String paginaSiguiente=request.getParameter("paginaSiguiente");	
									if( paginaSiguiente != null && !paginaSiguiente.equals("") ) // Salió por click en otro lado
									{
										procedimientoForm.reset();
										UtilidadBD.cerrarConexion(con);
										response.sendRedirect(paginaSiguiente);
										return null;
									} 
									else	// Salida al resumen
									{
										procedimientoForm.reset();
										procedimiento.cargar(con, procedimientoForm.getNumeroSolicitud());
										this.cargarForm(con,procedimientoForm, procedimiento, false,medico,request);
										//Se carga servicio y descripcion del procedimiento
										SolicitudProcedimiento solProcedimiento = new SolicitudProcedimiento();
										solProcedimiento.cargarSolicitudProcedimiento(con,procedimientoForm.getNumeroSolicitud());
										procedimientoForm.setNombreServicio("11111"+solProcedimiento.getCodigoServicioSolicitado()+"-"+solProcedimiento.getEspecialidadSolicitada().getCodigo()+" "+solProcedimiento.getNombreServicioSolicitado());

										UtilidadBD.cerrarConexion(con);
										return mapping.findForward("paginaResumen");
									}														
								}
								else // Inserción no existosa
								{

									procedimientoForm.reset();
									ArrayList atributosError = new ArrayList();
									atributosError.add("procedimiento");							
									request.setAttribute("atributosError", atributosError);
									return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No se pudo insertar/modificar la respuesta del procedimiento"+resultado.getDescripcion(), "error.ordenmedica.noGrabada", true);
								}
							}
							// ESTADO EMPEZAR (cuando hay permisos para modificar) -------------------------------------------------------------------------------
							else
								if( permisosModificar.isTrue() && estado.equals("empezar") )
								{
									Procedimiento procedimiento = new Procedimiento();					
									procedimientoForm.reset();
									procedimiento.cargar(con, procedimientoForm.getNumeroSolicitud());
									this.cargarForm(con,procedimientoForm, procedimiento, true,medico,request);
									UtilidadBD.cerrarConexion(con);
									return mapping.findForward("paginaModificar");
								}
							// ESTADO FINALIZARMODIFICAR (cuando hay permisos para modificar) -----------------------------------------------------------------------------
								else if( permisosModificar.isTrue() && estado.equals("finalizarModificar") )
								{
									Procedimiento procedimiento = new Procedimiento();
									this.cargarObjeto(procedimientoForm, procedimiento, true, medico);

									ResultadoBoolean resultado = procedimiento.insertarModificar(con, procedimientoForm.getNumeroSolicitud(),medico,paciente);

									if( resultado.isTrue() ) // Modificación exitosa
									{
										String paginaSiguiente=request.getParameter("paginaSiguiente");	
										if( paginaSiguiente != null && !paginaSiguiente.equals("") ) // Salió por click en otro lado
										{
											procedimientoForm.reset();
											UtilidadBD.cerrarConexion(con);
											response.sendRedirect(paginaSiguiente);
											return null;
										} 
										else	// Salida al resumen
										{
											procedimientoForm.reset();
											procedimiento.cargar(con, procedimientoForm.getNumeroSolicitud());
											this.cargarForm(con,procedimientoForm, procedimiento, false,medico,request);

											UtilidadBD.cerrarConexion(con);
											return mapping.findForward("paginaResumen");
										}														
									}		
									else // Modificación no existosa
									{
										procedimientoForm.reset();
										ArrayList atributosError = new ArrayList();
										atributosError.add("procedimiento");							
										request.setAttribute("atributosError", atributosError);

										return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No se pudo insertar/modificar la respuesta del procedimiento"+resultado.getDescripcion(), "error.ordenmedica.noGrabada", true);
									}
								}


							//ESTADO RESUMEN ---
								else if(  estado.equals("resumen")  ) {
									procedimientoForm.reset();
									RespuestaProcedimientos respuestaProcedimiento = new RespuestaProcedimientos();
									respuestaProcedimiento.cargarRespuestaProcedimientos(procedimientoForm.getNumeroSolicitud()+"", true, procedimientoForm.getCodigoRespuesta(),"");

									this.cargarFormRespuesta(con,procedimientoForm, respuestaProcedimiento, false,paciente,medico,request);

									///Se carga servicio y descripcion del procedimiento
									SolicitudProcedimiento solProcedimiento = new SolicitudProcedimiento();
									solProcedimiento.setInstitucion(medico.getCodigoInstitucion());	
									solProcedimiento.cargarSolicitudProcedimiento(con,procedimientoForm.getNumeroSolicitud());
									procedimientoForm.setNombreServicio(solProcedimiento.getCodigoServicioSolicitado()+"-"+solProcedimiento.getEspecialidadSolicitada().getCodigo()+" "+solProcedimiento.getNombreServicioSolicitado());

									if(procedimientoForm.isConsultarIterpretacion())
										procedimientoForm.getProcedimientoDto().setMostrarInterpretacionEnResumen(!UtilidadTexto.isEmpty(procedimientoForm.getProcedimientoDto().getInterpretacion().trim()));
									else 
										procedimientoForm.getProcedimientoDto().setMostrarInterpretacionEnResumen(false);

									UtilidadBD.cerrarConexion(con);
									logger.info("El estado resumen en ProcedimientoAction");
									return mapping.findForward("paginaResumen");
								}

								else
									if( !permisosResponder.isTrue() && !permisosModificar.isTrue() )
									{
										UtilidadBD.cerrarConexion(con);
										request.setAttribute("codigoDescripcionError", permisosResponder.getDescripcion());
										request.setAttribute("codigoDescripcionError", permisosModificar.getDescripcion());
										return mapping.findForward("paginaError");									
									}

									else
									{
										UtilidadBD.cerrarConexion(con);						
										request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");				
										return mapping.findForward("paginaError");														
									}

							//**********FIN IF DE TIPO DE CENTRO COSTO*******************************************************************
						}
			}
			else
			{
				logger.error("El form no es compatible con el form de Procedimiento");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}	
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
	
	/**
	 * @param procedimientoForm
	 * @param procedimiento
	 * @param esModificar
	 * @param medico
	 */
	private void cargarObjeto(ProcedimientoForm procedimientoForm, Procedimiento procedimiento, boolean esModificar, UsuarioBasico medico)
	{
		if( !esModificar )
		{
			procedimiento.setCentroCostoSolicitado(medico.getCodigoCentroCosto());
			procedimiento.setFechaEjecucion(procedimientoForm.getFechaEjecucion());
			procedimiento.setResultados(UtilidadCadena.cargarObservaciones(procedimientoForm.getResultados(), "", medico));
			procedimiento.setObservaciones(UtilidadCadena.cargarObservaciones(procedimientoForm.getObservaciones(), "", medico));
			procedimiento.setTipoRecargo(new InfoDatosInt(procedimientoForm.getCodigoTipoRecargo()));
			procedimiento.setComentarioHistoriaClinica(UtilidadCadena.cargarObservaciones(procedimientoForm.getComentariosHistoriaClinica(), "", medico));
		}
		else
		{
			procedimiento.setResultados(UtilidadCadena.cargarObservaciones(procedimientoForm.getResultados(), procedimientoForm.getResultadosAnteriores(), medico));
			procedimiento.setObservaciones(UtilidadCadena.cargarObservaciones(procedimientoForm.getObservaciones(), procedimientoForm.getObservacionesAnteriores(), medico));
			procedimiento.setComentarioHistoriaClinica(UtilidadCadena.cargarObservaciones(procedimientoForm.getComentariosHistoriaClinica(), procedimientoForm.getComentariosHistoriaClinicaAnteriores(), medico));			
		}
		
		//Se cargan los diagnósticos
		procedimiento.setDiagnosticos(procedimientoForm.getDiagnosticos());
		procedimiento.setNumDiagnosticos(procedimientoForm.getNumDiagnosticos());
		
		//procedimiento.setNumeroAutorizacion(procedimientoForm.getNumeroAutorizacion());
		
		// Documentos adjuntos
		int numAdjuntos = procedimientoForm.getNumDocumentosAdjuntos();
		
		for( int i=0; i<numAdjuntos; i++ )
		{
			String nombre = (String)procedimientoForm.getDocumentoAdjuntoGenerado(""+i);
			String checkbox = (String)procedimientoForm.getDocumentoAdjuntoGenerado("checkbox_"+i);
			
			if( nombre != null && checkbox != null && !checkbox.equals("off") )
			{
				String[] nombres = nombre.split("@");
				
				if( nombres.length == 2 )
				{
					String codigoStr = (String)procedimientoForm.getDocumentoAdjuntoGenerado("codigo_"+i);
					int codigo = 0;
					
					if( UtilidadCadena.noEsVacio(codigoStr) )
						codigo = Integer.parseInt(codigoStr);
						
					DocumentoAdjunto documento = new DocumentoAdjunto(nombres[1], nombres[0], false, codigo, "");					
					procedimiento.getDocumentosAdjuntos().addDocumentoAdjunto(documento);
				}
			}		
		}
	}
	
	/**
	 * @param procedimientoForm
	 * @param procedimiento
	 * @param esModificar
	 */
	private void cargarFormRespuesta(
			Connection con,
			ProcedimientoForm procedimientoForm,
			RespuestaProcedimientos procedimiento,
			boolean esModificar,
			PersonaBasica paciente,
			UsuarioBasico usuario, 
			HttpServletRequest request)
	{
		if( esModificar )
		{
			procedimientoForm.setResultadosAnteriores(procedimiento.getResultados().replaceAll("<br>", "\n"));
			procedimientoForm.setObservacionesAnteriores(procedimiento.getObservaciones().replaceAll("<br>", "\n"));
			procedimientoForm.setComentariosHistoriaClinicaAnteriores(procedimiento.getComentariosHistoriaClinica().replaceAll("<br>", "\n"));
		}
		else
		{
			procedimientoForm.setResultados(procedimiento.getResultados().replaceAll("\n", "<br>"));
			procedimientoForm.setObservaciones(procedimiento.getObservaciones().replaceAll("\n", "<br>"));
			
			if(procedimiento.getComentariosHistoriaClinica() != null)
				procedimientoForm.setComentariosHistoriaClinica(procedimiento.getComentariosHistoriaClinica().replaceAll("\n", "<br>"));
			else
				procedimientoForm.setComentariosHistoriaClinica("");
		}
		
		//procedimientoForm.setNumeroAutorizacion(procedimiento.get);
		procedimientoForm.setNombreTipoRecargo(procedimiento.getNombreTipoRecargo());
		procedimientoForm.setCodigoTipoRecargo(procedimiento.getCodigoTipoRecargo());
		procedimientoForm.setFechaEjecucion(procedimiento.getFechaEjecucion());
		
		//se asignan los diagnósticos
		procedimientoForm.setDiagnosticos(procedimiento.getDiagnosticos());
		procedimientoForm.setNumDiagnosticos(procedimiento.getNumDiagnosticos());
		
		int numAdjuntos = procedimiento.getDocumentosAdjuntos().getNumDocumentosAdjuntos();
		procedimientoForm.setNumDocumentosAdjuntos(numAdjuntos);
		
		for( int i=0; i<numAdjuntos; i++ )
		{
			DocumentoAdjunto documento = procedimiento.getDocumentosAdjuntos().getDocumentoAdjunto(i);
			
			procedimientoForm.setDocumentoAdjuntoGenerado("original_"+i, documento.getNombreOriginal());
			procedimientoForm.setDocumentoAdjuntoGenerado("generado_"+i, documento.getNombreGenerado());
			procedimientoForm.setDocumentoAdjuntoGenerado("codigo_"+i, documento.getCodigoArchivo()+"");
			procedimientoForm.setDocumentoAdjuntoGenerado(""+i, documento.getNombreGenerado()+"@"+documento.getNombreOriginal());
		}
		
		if (!(request.getParameter("indicadorDummy")+"").equals("") && !(request.getParameter("indicadorDummy")+"").equals("null"))		
			procedimientoForm.setIndicadorDummy(request.getParameter("indicadorDummy").toString());
		else
			procedimientoForm.setIndicadorDummy(ConstantesBD.acronimoNo);
		
		//Carga la informacion de la Plantilla Relacioanada		
		procedimientoForm.setPlantillaDto(Plantillas.cargarPlantillaXRespuestaProcedimiento(
				con,
				Plantillas.consultarBasicaPlantillasResProc(con,procedimiento.getCodigoRespuesta()+"").getId()+"", 
				usuario.getCodigoInstitucion(),
				procedimiento.getCodigoRespuesta()+""));		
		
		accionCargarDtoProcedimiento(con,request,procedimientoForm,paciente,usuario);
	}
	
	
	/**
	 * Carga el Dto de Procedimiento
	 * @param Connection con
	 * @param ProcedimientoForm forma
	 * @param PersonaBasica paciente
	 * */
	private void accionCargarDtoProcedimiento(
			Connection con,
			HttpServletRequest request,
			ProcedimientoForm forma, 
			PersonaBasica paciente, 
			UsuarioBasico usuario)
	{
		logger.info("VALOR NUMERO SOLICITUD DTO PROCEDIMIENTO >> "+forma.getNumeroSolicitud()+" NUMERO DE RESPUESTA >> "+forma.getCodigoRespuesta());
		forma.setProcedimientoDto(RespuestaProcedimientos.cargarDtoProcedimiento(
				con,
				paciente.getCodigoPersona()+"",
				forma.getNumeroSolicitud()+"",
				forma.getCodigoRespuesta(),
				usuario.getCodigoInstitucionInt(),
				usuario.getCodigoCentroAtencion())
				);
		
		//Valida si se debe mostrar o no la sección de Muerto
		if(forma.getProcedimientoDto().getCodigoViaIngreso() == ConstantesBD.codigoViaIngresoAmbulatorios || 
				forma.getProcedimientoDto().getCodigoViaIngreso() == ConstantesBD.codigoViaIngresoConsultaExterna)
			forma.getProcedimientoDto().getRespuestaProceEspecificoDto().setMostrarSeccionMuerto(ConstantesBD.acronimoSi);
		else
			forma.getProcedimientoDto().getRespuestaProceEspecificoDto().setMostrarSeccionMuerto(ConstantesBD.acronimoNo);
	}
	
	
	/**
	 * @param procedimientoForm
	 * @param procedimiento
	 * @param esModificar
	 */
	private void cargarForm(Connection con,ProcedimientoForm procedimientoForm, Procedimiento procedimiento, boolean esModificar,UsuarioBasico usuario, HttpServletRequest request)
	{
		if( esModificar )
		{
			procedimientoForm.setResultadosAnteriores(procedimiento.getResultados().replaceAll("<br>", "\n"));
			procedimientoForm.setObservacionesAnteriores(procedimiento.getObservaciones().replaceAll("<br>", "\n"));
			procedimientoForm.setComentariosHistoriaClinicaAnteriores(procedimiento.getComentarioHistoriaClinica().replaceAll("<br>", "\n"));
		}
		else
		{
			procedimientoForm.setResultados(procedimiento.getResultados().replaceAll("\n", "<br>"));
			procedimientoForm.setObservaciones(procedimiento.getObservaciones().replaceAll("\n", "<br>"));
			
			if(procedimiento.getComentarioHistoriaClinica() != null)
				procedimientoForm.setComentariosHistoriaClinica(procedimiento.getComentarioHistoriaClinica().replaceAll("\n", "<br>"));
			else
				procedimientoForm.setComentariosHistoriaClinica("");
		}
		
		//procedimientoForm.setNumeroAutorizacion(procedimiento.getNumeroAutorizacion());
		procedimientoForm.setNombreTipoRecargo(procedimiento.getTipoRecargo().getNombre());
		procedimientoForm.setCodigoTipoRecargo(procedimiento.getTipoRecargo().getCodigo());
		procedimientoForm.setFechaEjecucion(procedimiento.getFechaEjecucion());
		
		//se asignan los diagnósticos
		procedimientoForm.setDiagnosticos(procedimiento.getDiagnosticos());
		procedimientoForm.setNumDiagnosticos(procedimiento.getNumDiagnosticos());
		
		int numAdjuntos = procedimiento.getDocumentosAdjuntos().getNumDocumentosAdjuntos();
		procedimientoForm.setNumDocumentosAdjuntos(numAdjuntos);
		
		for( int i=0; i<numAdjuntos; i++ )
		{
			DocumentoAdjunto documento = procedimiento.getDocumentosAdjuntos().getDocumentoAdjunto(i);
			
			procedimientoForm.setDocumentoAdjuntoGenerado("original_"+i, documento.getNombreOriginal());
			procedimientoForm.setDocumentoAdjuntoGenerado("generado_"+i, documento.getNombreGenerado());
			procedimientoForm.setDocumentoAdjuntoGenerado("codigo_"+i, documento.getCodigoArchivo()+"");
			procedimientoForm.setDocumentoAdjuntoGenerado(""+i, documento.getNombreGenerado()+"@"+documento.getNombreOriginal());
		}
		
		if (!(request.getParameter("indicadorDummy")+"").equals("") && !(request.getParameter("indicadorDummy")+"").equals("null"))		
			procedimientoForm.setIndicadorDummy(request.getParameter("indicadorDummy").toString());
		else
			procedimientoForm.setIndicadorDummy(ConstantesBD.acronimoNo);
		
		//Carga la informacion de la Plantilla Relacioanada		
		procedimientoForm.setPlantillaDto(Plantillas.cargarPlantillaXRespuestaProcedimiento(
				con,
				Plantillas.consultarBasicaPlantillasResProc(con,procedimiento.getCodigoPkResSolProc()+"").getId()+"", 
				usuario.getCodigoInstitucion(),
				procedimiento.getCodigoPkResSolProc()+""));		
	}
	
	/**
	 * Acción para la impresión de lo relacionado con ordenes médicas
	 * @param con
	 * @param numeroSolicitud
	 * @param mapping
	 * @param medico
	 * @param pacienteActivo
	 * @param request
	 * @return Action Forward
	 * @throws SQLException
	 */
	private ActionForward accionImprimir(Connection con, int numeroSolicitud, ActionMapping mapping,
										UsuarioBasico medico,PersonaBasica pacienteActivo,HttpServletRequest request, InstitucionBasica institucionActual)throws SQLException 
	{
			String nombreArchivo;
			Random r=new Random();
			nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
			SolicitudProcedimiento solicitud=new SolicitudProcedimiento();

			Procedimiento procedimiento=new Procedimiento();
			solicitud.cargarSolicitudProcedimiento(con, numeroSolicitud);
			
			if(solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCSolicitada)
			{
				ResumenGuardarProcedimientoPdf.pdfImprimirResumen(con, ValoresPorDefecto.getFilePath() +nombreArchivo, solicitud, medico, pacienteActivo,true, institucionActual);
			}
			else if(solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCRespondida || solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCInterpretada)
			{
				procedimiento.cargar(con, numeroSolicitud);
				procedimiento.setNumeroSolicitud(numeroSolicitud);
				procedimiento.getDocumentosAdjuntos().cargarDocumentosAdjuntos(con,numeroSolicitud, true, "");
				RespuestaProcedimientoPdf.pdfImprimirRespuesta(con, ValoresPorDefecto.getFilePath() +nombreArchivo, solicitud, medico, pacienteActivo, procedimiento);
			}
			request.setAttribute("nombreArchivo", nombreArchivo);
			request.setAttribute("nombreVentana", "Procedimientos");
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("abrirPdf");
	}
	
	/**
	 * Accion de imprimir un procedimiento externo
	 * @param con
	 * @param numeroSolicitud
	 * @param mapping
	 * @param usu
	 * @param pacienteActivo
	 * @param request
	 * @return
	 * @throws SQLException
	 */
		private ActionForward accionImprimirExterno(Connection con, int numeroSolicitud, ActionMapping mapping,UsuarioBasico usu,PersonaBasica pacienteActivo, HttpServletRequest request, Procedimiento procedimiento)throws SQLException 
		{
			SolicitudProcedimiento solicitud=new SolicitudProcedimiento();
			
			solicitud.cargarSolicitudProcedimiento(con, numeroSolicitud);
			solicitud.setNumeroSolicitud(numeroSolicitud);
		
			solicitud.getImpresion();
			String nombreArchivo;
			Random r=new Random();
			nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
			
			logger.info("Impresion de respuesta");
			RespuestaProcedimientoPdf.pdfImprimirRespuesta(con, ValoresPorDefecto.getFilePath() +nombreArchivo, solicitud, usu, pacienteActivo, procedimiento);
			request.setAttribute("nombreArchivo", nombreArchivo);
			request.setAttribute("nombreVentana", "Procedimientos Externos");
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("abrirPdf");
		
		}
		
		
	//****************************************************************************************************************
	//****************************************************************************************************************

	/**
	 * Carga el procedimiento con el formato de parametrizable
	 * @param Connection con
	 * @param ProcedimientoForm forma
	 * @param UsuarioBasico medico
	 * @param HttpServletRequest request
	 * */
	private void cargarRespuestaConFormatoParam(
			Connection con, 
			ProcedimientoForm forma,
			PersonaBasica paciente,
			UsuarioBasico medico,
			HttpServletRequest request)
	{
		logger.info("Entro Cargar Formato Pra codigoRespuesta: "+forma.getCodigoRespuesta());
							
		forma.reset();
		RespuestaProcedimientos respuestaProcedimientos = new RespuestaProcedimientos();
		respuestaProcedimientos.cargarRespuestaProcedimientos(forma.getNumeroSolicitud()+"", true, forma.getCodigoRespuesta(),"");
		
		this.cargarFormRespuesta(con,forma, respuestaProcedimientos, false,paciente,medico,request);
		
		try
		{
			//Se carga servicio y descripcion del procedimiento
			SolicitudProcedimiento solProcedimiento = new SolicitudProcedimiento();
			solProcedimiento.setInstitucion(medico.getCodigoInstitucion());
			solProcedimiento.cargarSolicitudProcedimiento(con,forma.getNumeroSolicitud());
			forma.setNombreServicio(solProcedimiento.getNombreServicioSolicitado());
			
			//Se cargan los datos del médico que responde
			forma.setCodigoMedicoResponde(UtilidadesOrdenesMedicas.obtenerCodigoMedicoRespondeSolicitud(con, forma.getNumeroSolicitud()));
			UsuarioBasico medicoResponde = new UsuarioBasico();
			medicoResponde.cargarUsuarioBasico(con, forma.getCodigoMedicoResponde());
			forma.setDatosMedicoResponde(medicoResponde.getInformacionGeneralPersonalSalud());
		}
		catch (SQLException e) 
		{
			
			logger.error("Error en cargarRespuestaConFormatoParam: "+e);
		}
	}	
}