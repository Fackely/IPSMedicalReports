/*
 * Creado en Jun 1, 2005
 */
package com.princetonsa.action.ordenesmedicas;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

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
import util.historiaClinica.UtilidadesJustificacionNoPos;
import util.interfaces.ConstantesBDInterfaz;
import util.interfaces.UtilidadBDInterfaz;
import util.inventarios.UtilidadInventarios;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.ordenesmedicas.OrdenMedicaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.comun.DtoResultado;
import com.princetonsa.dto.historiaClinica.DtoParamJusNoPos;
import com.princetonsa.dto.interfaz.DtoInterfazNutricion;
import com.princetonsa.dto.manejoPaciente.DtoResultadoLaboratorio;
import com.princetonsa.dto.ordenesmedicas.DtoHemodialisis;
import com.princetonsa.dto.ordenesmedicas.DtoPrescripcionDialisis;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.enfermeria.ProgramacionCuidadoEnfer;
import com.princetonsa.mundo.enfermeria.RegistroEnfermeria;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.princetonsa.mundo.ordenesmedicas.OrdenMedica;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Andrés Mauricio Ruiz Vélez Princeton S.A. (Parquesoft-Manizales)
 */
public class OrdenMedicaAction extends Action {

	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(OrdenMedicaAction.class);

	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm,
	 *      HttpServletRequest, HttpServletResponse)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		Connection con = null;

		try {
			if (response == null)
				; // Para evitar que salga el warning

			if (form instanceof OrdenMedicaForm) {
				try {
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))
					.getConnection();
				} catch (SQLException e) {
					logger.warn("No se pudo abrir la conexión" + e.toString());
				}

				OrdenMedicaForm ordenMedicaForm = (OrdenMedicaForm) form;
				HttpSession session = request.getSession();
				//MT2300
				ordenMedicaForm.setProcesoExitoso(false);
				String estado = ordenMedicaForm.getEstado();
				logger.info("En OrdenesMedicasAction el Estado es  [" + estado
						+ "] \n\n");

				if (estado == null) {
					ordenMedicaForm.reset();
					logger
					.warn("Estado no valido dentro del flujo de registro orden mÃ©dica (null) ");
					request.setAttribute("codigoDescripcionError",
					"errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}

				PersonaBasica paciente = (PersonaBasica) session
				.getAttribute("pacienteActivo");
				UsuarioBasico medico = (UsuarioBasico) session
				.getAttribute("usuarioBasico");
				UtilidadValidacion.esProfesionalSalud(medico);
				ordenMedicaForm.setAncla("");


				// logger.info("medico->"+medico.getCentroCosto());
				// logger.info("cuenta paciente->"+paciente.getCodigoCuenta());
				if (estado.equals("empezar")) {
					if (medico == null) {
						return ComunAction.accionSalirCasoError(mapping, request,
								con, logger, "No existe el usuario",
								"errors.usuario.noCargado", true);
					} else
						// Valida que el paciente esté cargado
						if (paciente == null
								|| paciente.getTipoIdentificacionPersona().equals("")) {
							return ComunAction.accionSalirCasoError(mapping, request,
									con, logger, "paciente null o sin  id",
									"errors.paciente.noCargado", true);
						}

					// Validación de autoatencion
					ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(medico, paciente);
					if (respuesta.isTrue())
						return ComunAction.accionSalirCasoError(mapping, request,
								con, logger,
								"El paciente no puede ser autoatendido", respuesta
								.getDescripcion(), true);

					// Valida que el paciente tiene la cuenta abierta

					if (!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con,
							paciente.getCodigoIngreso()).getCodigo().trim().equals(
									ConstantesIntegridadDominio.acronimoEstadoAbierto)) {
						return ComunAction.accionSalirCasoError(mapping, request,
								con, logger,
								"errors.ingresoEstadoDiferenteAbierto",
								"errors.ingresoEstadoDiferenteAbierto", true);
					}
					/**
					 * Se verifica que el ingreso no sea realizado atraves de
					 * entidades subcontratadas
					 */
					if (paciente.esIngresoEntidadSubcontratada()) {
						request.setAttribute("codigoDescripcionError",
						"error.ingresoEntidadSubContratada");
						return mapping.findForward("paginaError");
					}

					if (paciente.getCodigoCuenta() == 0) {
						return ComunAction.accionSalirCasoError(mapping, request,
								con, logger, "Paciente sin cuenta Abierta",
								"errors.paciente.cuentaNoAbierta", true);
					} else if (!UtilidadValidacion.esProfesionalSalud(medico)) {
						return ComunAction
						.accionSalirCasoError(
								mapping,
								request,
								con,
								logger,
								"Usuario NO personal de la salud tratÃ³ de ingresar evoluciÃ³n. Como usuario que no es profesional de la salud usted no esta autorizado para llenar una evoluciÃ³n",
								"errors.usuario.noAutorizado", true);
					} else {

						/*
						 * Valida que la orden médica la realiza el médico/grupo
						 * tratante y el médico grupo/adjunto para pacientes de
						 * urgencias y hospitalizacion
						 */
						// logger.info("VIA DE INGRESO->"+paciente.
						// getCodigoUltimaViaIngreso()+"\n");
						if (paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoHospitalizacion
								|| paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoUrgencias) {
							String mensaje = UtilidadValidacion.esMedicoTratante(
									con, medico, paciente);
							if (!mensaje.equals("")
									&& !UtilidadValidacion.esAdjuntoCuenta(con,
											paciente.getCodigoCuenta(), medico
											.getLoginUsuario())) {
								// se verifica si no está definido la ocupación
								// médica General y Especialista en Parámetros
								// Generales
								if (mensaje.equals("errors.noOcupacionMedica"))
									return ComunAction
									.accionSalirCasoError(
											mapping,
											request,
											con,
											logger,
											"No está definida la ocupación médica",
											mensaje, true);
								else
									return ComunAction
									.accionSalirCasoError(
											mapping,
											request,
											con,
											logger,
											"Médico no es tratante ni Adjunto",
											"error.validacionessolicitud.medicoNoTratanteNiAdjunto",
											true);
							}
						}
					}

					if (UtilidadValidacion.esCuentaValida(con, paciente
							.getCodigoCuenta()) < 1) {
						return ComunAction.accionSalirCasoError(mapping, request,
								con, logger, "errors.paciente.cuentaNoValida",
								"errors.paciente.cuentaNoValida", true);
					}

					// Cuando la via de ingreso es de urgencias u hospitalización
					if (paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoUrgencias
							|| paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoHospitalizacion) {
						// Valida que tenga valoración cuando el paciente es por
						// vía de ingreso hospitalización
						if (!UtilidadValidacion.tieneValoraciones(con, paciente
								.getCodigoCuenta())
								&& paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoHospitalizacion) {
							return ComunAction.accionSalirCasoError(mapping,
									request, con, logger,
									"Paciente sin valoracion inicial",
									"errors.noHayValoracionInicial", true);
						}

						logger.info("\n existe asocio -->"+paciente.getExisteAsocio());
						// logger.info("destino egreso evolucion->"+Utilidades.
						// obtenerDestinoSalidaEgresoEvolucion(con,
						// paciente.getCodigoCuenta())+"\n\n");
						/*
						 * logger.info("fecha orden salida->"+Utilidades.obtenerFechaOrdenSalida
						 * (con,
						 * paciente.getCodigoCuenta(),medico.getCodigoInstitucionInt
						 * ())+"\n\n");logger.info("fecha reversion>"+Utilidades.
						 * obtenerFechaReversionEgreso(con,
						 * paciente.getCodigoCuenta())+"\n\n");
						 */
						// Validar que no tenga orden de salida en la evolución
						// cuando el paciente es por vía de ingreso
						// hospitalización
						if (Utilidades.obtenerDestinoSalidaEgresoEvolucion(con,
								paciente.getCodigoCuenta()) != -1
								&& paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoHospitalizacion && !paciente.getExisteAsocio()) {
							/*
							 * if(!Utilidades.obtenerFechaOrdenSalida(con,
							 * paciente.getCodigoCuenta
							 * (),medico.getCodigoInstitucionInt()).equals("")) {
							 * if((Utilidades.obtenerFechaReversionEgreso(con,
							 * paciente.getCodigoCuenta()))!=null &&
							 * (Utilidades.obtenerFechaReversionEgreso(con,
							 * paciente.getCodigoCuenta())).equals("")) {
							 */
							// logger.info("11111111111\n");
							return ComunAction.accionSalirCasoError(mapping,
									request, con, logger,
									"Paciente con orden salida en la evoluci�n",
									"errors.noDebeTenerSalidaEvolucion", true);
							/*
							 * } }
							 */
						}
						// logger.info(
						// "entro a via ingreso urgencias u hospitalización \n\n"
						// +UtilidadValidacion.estaEnCamaObservacion(con,
						// paciente.getCodigoCuenta()));
						// Valida que tenga observacion el paciente de urgencias y
						// tenga la valoracion inicial
						if (UtilidadValidacion.estaEnCamaObservacion(con, paciente
								.getCodigoCuenta())
								&& paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoUrgencias) {
							// logger.info(
							// "cuando tiene observacion el paciente de urgencias\n\n"
							// );
							// Valida que no tenga orden de salida en la evolución

							// logger.info("destino egreso evolucion->"+Utilidades.
							// obtenerDestinoSalidaEgresoEvolucion(con,
							// paciente.getCodigoCuenta())+"\n\n");
							/*
							 * logger.info("fecha orden salida->"+Utilidades.obtenerFechaOrdenSalida
							 * (con,
							 * paciente.getCodigoCuenta(),medico.getCodigoInstitucionInt
							 * ())+"\n\n");
							 * logger.info("fecha reversion>"+Utilidades
							 * .obtenerFechaReversionEgreso(con,
							 * paciente.getCodigoCuenta())+"\n\n");
							 */
							if (Utilidades.obtenerDestinoSalidaEgresoEvolucion(con,
									paciente.getCodigoCuenta()) != -1 && !paciente.getExisteAsocio()) {
								/*
								 * if(!Utilidades.obtenerFechaOrdenSalida(con,
								 * paciente
								 * .getCodigoCuenta(),medico.getCodigoInstitucionInt
								 * ()).equals("")) {
								 * if((Utilidades.obtenerFechaReversionEgreso(con,
								 * paciente.getCodigoCuenta()))!=null &&
								 * (Utilidades.obtenerFechaReversionEgreso(con,
								 * paciente.getCodigoCuenta())).equals("")) {
								 */
								// logger.info("2222222\n\n"+Utilidades.
								// obtenerFechaReversionEgreso(con,
								// paciente.getCodigoCuenta()));
								return ComunAction
								.accionSalirCasoError(
										mapping,
										request,
										con,
										logger,
										"Paciente con orden salida en la evolución",
										"errors.noDebeTenerSalidaEvolucion",
										true);
								/*
								 * } }
								 */
							}
						}// if
						else {
							// logger.info("cuando no tiene observacion\n\n");
							// Cuando el paciente de urgencias no tiene observacion
							if (paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoUrgencias) {
								// logger.info("cuando es de urgencias \n\n");
								// Se valida que tenga la valoración inicial
								if (!UtilidadValidacion.tieneValoraciones(con,
										paciente.getCodigoCuenta())) {
									return ComunAction.accionSalirCasoError(
											mapping, request, con, logger,
											"Paciente sin valoracion inicial",
											"errors.noHayValoracionInicial", true);
								}
								// Valida que no tenga generado el egreso
								// Nueva valición tarea 44656 permitir ordenes
								// estado cuenta asociada
								if (UtilidadValidacion.tieneEgreso(con, paciente
										.getCodigoCuenta())
										&& UtilidadesHistoriaClinica
										.obtenerEstadoCuenta(con,
												paciente.getCodigoCuenta())
												.getCodigo() != ConstantesBD.codigoEstadoCuentaAsociada) {
									return ComunAction.accionSalirCasoError(
											mapping, request, con, logger,
											"Paciente con egreso",
											"errors.noDebeTenerEgreso", true);
								}
							}// if
						}// else
					}// if cuando es de urgencias u hospitalizacion

					ordenMedicaForm.setMensajeError(null);
					ordenMedicaForm.setParametroMensaje(null);
					if (ValoresPorDefecto.getCodigoTransSoliPacientes(
							medico.getCodigoInstitucionInt(), true).trim().equals(
							"")) {
						
						//Ajuste por MT 6191
						/*ordenMedicaForm
						.setMensajeError("error.inventarios.sinDefinirTipoTransaccion");
						ordenMedicaForm.setParametroMensaje("SOLICITUDES PACIENTE");*/
		            
						return ComunAction.accionSalirCasoError(mapping,
								request, con, logger,
								"SOLICITUDES PACIENTE",
								"error.inventarios.sinDefinirTipoTransaccion.solicitudPaciente", true);					
						
						
					} else {
						// validacion de las transacciones validas por centro de
						// consto
						Vector restricciones = new Vector();
						restricciones.add(0, ValoresPorDefecto
								.getCodigoTransSoliPacientes(medico
										.getCodigoInstitucionInt(), true));
						HashMap transaccionesValidasCCMap = UtilidadInventarios
						.transaccionesValidasCentroCosto(medico
								.getCodigoInstitucionInt(), paciente
								.getCodigoArea(), restricciones, true);
						if (Integer.parseInt(transaccionesValidasCCMap.get(
						"numRegistros").toString()) <= 0) {
							ordenMedicaForm
							.setMensajeError("error.inventarios.transaccionNoValidaCentroCosto");
							ordenMedicaForm.setParametroMensaje(paciente.getArea());
						}
					}

				}// if estado=empezar

				// Captura la informacion de los almacenes
				ordenMedicaForm.setListadoAlmacenesMap(OrdenMedica
						.consultarListadoAlmacenes(con, paciente, medico));

				logger.info("-------------------------------------");
				logger.info("Valor del Estado  >> " + ordenMedicaForm.getEstado());
				logger.info("-------------------------------------");

				if (estado.equals("empezar")) {
					
					return this.accionEmpezar(ordenMedicaForm, mapping, con,
							medico, paciente);
				}
				if(estado.equals("adicionarCampo"))
				{
					if(!ordenMedicaForm.getEtiquetaCampoOtro().isEmpty())
					{
						DtoResultadoLaboratorio dtoResultado=new DtoResultadoLaboratorio();
						dtoResultado.setEtiquietaCampo(ordenMedicaForm.getEtiquetaCampoOtro());
						dtoResultado.setCentroCosto(paciente.getCodigoArea());
						dtoResultado.setCampoOtro(ConstantesBD.acronimoSi);
						ordenMedicaForm.getResultadoLaboratorios().add(dtoResultado);
						ordenMedicaForm.setAncla("adicionarCampo");
					}
					ordenMedicaForm.setEtiquetaCampoOtro("");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				if(estado.equals("cambiarPosicionPagerSecionVE"))
				{
					ordenMedicaForm.setAncla("pagerSecionResultadosVE");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}

				if(estado.equals("cambiarPosicionPagerSecionResulLab"))
				{
					ordenMedicaForm.setAncla("pagerSecionResultados");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}

				if (estado.equals("modificarMezcla")) {
					return this.accionCargarMezclaParaModificacion(ordenMedicaForm,
							mapping, medico, con);
				}
				if (estado.equals("nuevoArticuloMezcla")) {
					return this.accionNuevoArticuloMezcla(ordenMedicaForm, mapping,
							con);
				}
				if (estado.equals("guardarModificacionMezcla")) {
					ActionErrors error = new ActionErrors();
					return this.accionGuardarModificacionMezcla(ordenMedicaForm,
							error, request, mapping, con, medico, paciente);
				}
				if (estado.equals("anularOdenMezcla")) {
					return this.accionAnularMezcla(ordenMedicaForm, mapping, con);
				}
				if (estado.equals("cambiarMezcla")) {
					return this.accionCambiarMezcla(ordenMedicaForm, mapping, con,
							medico, paciente, request);
				} else if (estado.equals("EliminarArticuloMezcla")) {
					metodoEliminarArticuloMezcla(ordenMedicaForm);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");

				}
				if (estado.equals("listadoHistoCuidadosEsp")) {
					OrdenMedica mundoOrdenMedica = new OrdenMedica();
					ordenMedicaForm.setListadoHistoCuidadosEsp(mundoOrdenMedica
							.consultarFechasHistoCuidadosEspe(con, paciente
									.getCodigoCuenta(), paciente
									.getCodigoCuentaAsocio()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoHistoCuidadosEsp");
				}
				if (estado.equals("mostrarDetalleHistoCuidados")) {
					ordenMedicaForm
					.setListadoCuidadosEmfermeriaPopUp(new ArrayList());
					ordenMedicaForm
					.setListadoCuidadosEnfHistoPopUp(new ArrayList());
					OrdenMedica mundoOrdenMedica = new OrdenMedica();
					ordenMedicaForm
					.setListadoCuidadosEmfermeriaPopUp(mundoOrdenMedica
							.consultarTipos(con, medico
									.getCodigoInstitucionInt(), paciente
									.getCodigoArea(), 3, -1, paciente
									.getCodigoCuenta(), paciente
									.getCodigoCuentaAsocio(), -1, -1));
					ordenMedicaForm
					.setListadoCuidadosEnfHistoPopUp(mundoOrdenMedica
							.consultarCuidadosEnfHisto(con, paciente
									.getCodigoCuenta(), paciente
									.getCodigoCuentaAsocio(), medico
									.getCodigoInstitucionInt(), paciente
									.getCodigoArea(), ordenMedicaForm
									.getFechaCuidado(), ordenMedicaForm
									.getFechaCuidado()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleHistoCuidadosEsp");

				}
				if (estado.equals("salir")) {
					return this.accionSalir(ordenMedicaForm, mapping, con, paciente.getCodigoCuenta(), medico.getLoginUsuario(),
							UtilidadTexto.agregarTextoAObservacion(null, null,
									medico, false), session, request);
				} else if (estado.equals("detalleMezclaAnteriores")) {
					return this.accionDetalleMezclaAnteriores(ordenMedicaForm,
							mapping, con, paciente);
				} else if (estado.equals("volverMezclasFinalizadas")) {
					request.getSession().setAttribute("pacienteActivo", paciente);

					response
					.sendRedirect("anterioresMezclaParenteral.jsp?param=mezclasFinalizadas");

					UtilidadBD.closeConnection(con);
					return null;
				}
				// Estado usado para retornar a la funcionalidad de ordenes, cuando
				// es llamada desde la referencia
				else if (estado.equals("volverOrdenes")) {
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				// **********ESTADOS FILTROS
				// AJAX********************************************************
				else if (estado.equals("filtroFiltrosDializados")) {
					return accionFiltroFiltrosDializado(con, ordenMedicaForm,
							response);
				}
				//******************************************************************
				// ********************

				else
					if (estado.equals("suspender"))
					{
						return suspenderMezcla(con, mapping, ordenMedicaForm, medico, paciente);
					}



				UtilidadBD.closeConnection(con);
			}
			return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}

	/**
	 * Método que realiza el filtro de los filstros dializados en la
	 * prescripcion diálisis cuando se selecciona un tipo membrana
	 * 
	 * @param con
	 * @param ordenMedicaForm
	 * @param response
	 * @return
	 */
	private ActionForward accionFiltroFiltrosDializado(Connection con,
			OrdenMedicaForm ordenMedicaForm, HttpServletResponse response) {
		// /Se consultan los filtros
		ordenMedicaForm.getDialisis().setFiltros(
				OrdenMedica.cargarArregloPrescripcionDialisis(con,
						DtoPrescripcionDialisis.tipoConsultaFiltro,
						ordenMedicaForm.getCodigoTipoMembrana()));

		String resultado = "<respuesta>" + "<infoid>"
				+ "<sufijo>ajaxBusquedaTipoSelect</sufijo>"
				+ "<id-select>codigoFiltroDializado</id-select>"
				+ "<id-arreglo>filtro</id-arreglo>" + // nombre de la etiqueta
														// de cada elemento
				"</infoid>";

		for (HashMap elemento : ordenMedicaForm.getDialisis().getFiltros()) {
			resultado += "<filtro>";
			resultado += "<codigo>" + elemento.get("codigo") + "</codigo>";
			resultado += "<descripcion>" + elemento.get("nombre")
					+ "</descripcion>";
			resultado += "</filtro>";
		}

		resultado += "</respuesta>";

		UtilidadBD.closeConnection(con);
		// **********SE GENERA RESPUESTA PARA AJAX EN
		// XML**********************************************
		try {
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().write(resultado);
		} catch (IOException e) {
			logger
					.error("Error al enviar respuesta AJAX en accionFiltroFiltrosDializado: "
							+ e);
		}
		return null;
	}

	/**
	 * 
	 * @param ordenMedicaForm
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionAnularMezcla(OrdenMedicaForm ordenMedicaForm,
			ActionMapping mapping, Connection con) {
		OrdenMedica mundoOrdenMedica = new OrdenMedica();
		mundoOrdenMedica.accionAnularMezcla(con, ordenMedicaForm
				.getMezclaModificar());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("dummyModificarMezcla");
	}

	/**
	 * @param ordenMedicaForm
	 * @param mapping
	 * @param con
	 * @param paciente
	 * @param medico
	 * @return
	 */
	private ActionForward accionGuardarModificacionMezcla(
			OrdenMedicaForm ordenMedicaForm, ActionErrors errores,
			HttpServletRequest request, ActionMapping mapping, Connection con,
			UsuarioBasico medico, PersonaBasica paciente) throws IPSException {
		errores = OrdenMedica.validacionModificacionMezcla(con, ordenMedicaForm
				.getMezclaModificar(), ordenMedicaForm.getJustificacionMap(),
				paciente, medico, errores, ordenMedicaForm
						.isModificacionRegistroEnfermeria());

		if (!errores.isEmpty()) {
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("dummyModificarMezcla");
		}

		OrdenMedica mundoOrdenMedica = new OrdenMedica();
		mundoOrdenMedica.guardarModificacionMezcla(con,
				Utilidades.convertirAEntero(ordenMedicaForm
						.getNumeroSolicitudModificar()), ordenMedicaForm
						.getMezclaModificar(), ordenMedicaForm
						.getJustificacionMap(), ordenMedicaForm
						.getMedicamentosNoPosMap(), ordenMedicaForm
						.getMedicamentosPosMap(), ordenMedicaForm
						.getSustitutosNoPosMap(), ordenMedicaForm
						.getDiagnosticosDefinitivos(), medico, paciente,
				ordenMedicaForm.isModificacionRegistroEnfermeria());

		// Inserta la informacion de las observaciones
		if (ordenMedicaForm.isModificacionRegistroEnfermeria()
				&& !ordenMedicaForm.getObservacionesMezclasMap(
						"descripcionNueva").toString().equals("")) {
			String temporal = "";
			temporal = UtilidadTexto.agregarTextoAObservacion(ordenMedicaForm
					.getObservacionesMezclasMap("descripcionDietaEnfermera_0")
					.toString(), ordenMedicaForm.getObservacionesMezclasMap(
					"descripcionNueva").toString(), medico, true);

			if (temporal.length() > 4000)
				temporal = ordenMedicaForm.getObservacionesMezclasMap(
						"descripcionDietaEnfermera_0").toString();

			mundoOrdenMedica.actualizarObservacionesMezcla(con, temporal,
					ordenMedicaForm.getObservacionesMezclasMap("codigo_0")
							.toString());
		}

		UtilidadBD.closeConnection(con);
		return mapping.findForward("dummyModificarMezcla");
	}

	/**
	 * 
	 * @param ordenMedicaForm
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionNuevoArticuloMezcla(
			OrdenMedicaForm ordenMedicaForm, ActionMapping mapping,
			Connection con) {
		int pos = Integer.parseInt(ordenMedicaForm.getMezclaModificar().get(
				"numRegistros")
				+ "");
		ordenMedicaForm.setMezclaModificar("articulo_" + pos, ordenMedicaForm
				.getCodigoArticuloMezcla());
		ordenMedicaForm.setMezclaModificar("nombrearticulo_" + pos,
				ordenMedicaForm.getNombreArticuloMezcla());
		ordenMedicaForm.setMezclaModificar("volumen_" + pos, "");
		ordenMedicaForm.setMezclaModificar("tiporegistro_" + pos, "MEM");
		ordenMedicaForm.setMezclaModificar("espos_" + pos, ordenMedicaForm
				.isEsPosArticuloMezcla());
		ordenMedicaForm.setMezclaModificar("codigojustificacion_" + pos,
				ConstantesBD.codigoNuncaValido + "");

		// Dependiendo del flujo los articulos incluidos pueden ser o no Insumos
		// o Medicamentos
		if (ordenMedicaForm.isModificacionRegistroEnfermeria())
			ordenMedicaForm.setMezclaModificar("esmedicamento_" + pos,
					ConstantesBD.acronimoNo);
		else
			ordenMedicaForm.setMezclaModificar("esmedicamento_" + pos,
					ConstantesBD.acronimoSi);

		ordenMedicaForm.setMezclaModificar("numRegistros", (pos + 1));
		ordenMedicaForm.setEstado("nuevoArticuloMezcla");

		UtilidadBD.closeConnection(con);
		return mapping.findForward("cargarMezclaModificar");
	}

	/**
	 * 
	 * @param ordenMedicaForm
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionCargarMezclaParaModificacion(
			OrdenMedicaForm ordenMedicaForm, ActionMapping mapping,
			UsuarioBasico usuario, Connection con) {
		OrdenMedica mundoOrdenMedica = new OrdenMedica();
		ordenMedicaForm.setMezclaModificar(mundoOrdenMedica
				.consultarMezclaModificar(con, ordenMedicaForm
						.getNumeroOrdenModificar(), ordenMedicaForm
						.getNumeroSolicitudModificar()));
		ordenMedicaForm.setMezclaModificar("numeroOrden", ordenMedicaForm
				.getNumeroOrdenModificar());
		ordenMedicaForm.setMezclaModificar("numeroSolicitud", ordenMedicaForm
				.getNumeroSolicitudModificar());
		ordenMedicaForm.setMezclaModificar("numeroMezcla", ordenMedicaForm
				.getMezcla());

		// Inicializa los valores para la justificacion
		ordenMedicaForm.setJustificacionHistoricoMap(new HashMap());
		ordenMedicaForm.setHiddens("");

		// Inicializa la transacción valida para la nueva solicitud de insumos
		ordenMedicaForm.setTipoTransaccionPedido(ValoresPorDefecto
				.getCodigoTransSoliPacientes(usuario.getCodigoInstitucionInt(),
						true));

		// Carga las observaciones de la mezcla
		if (ordenMedicaForm.isModificacionRegistroEnfermeria())
			ordenMedicaForm.setObservacionesMezclasMap(mundoOrdenMedica
					.obtenerObservacionesMezcla(con, ordenMedicaForm
							.getNumeroSolicitudModificar()));
		else {
			ordenMedicaForm.setObservacionesMezclasMap(new HashMap());
			ordenMedicaForm.setObservacionesMezclasMap("numRegistros", "0");
		}

		ordenMedicaForm.setObservacionesMezclasMap("descripcionNueva", "");
		if (ordenMedicaForm.getObservacionesMezclasMap("numRegistros").equals(
				"0")) {
			ordenMedicaForm.setObservacionesMezclasMap("descripcionDietaPar_0",
					"");
			ordenMedicaForm.setObservacionesMezclasMap(
					"descripcionDietaEnfermera_0", "");
		}

		UtilidadBD.closeConnection(con);
		return mapping.findForward("cargarMezclaModificar");
	}

	/**
	 * @param ordenMedicaForm
	 * @param mapping
	 * @param con
	 * @param medico
	 * @param paciente
	 * @return
	 */
	private ActionForward accionCambiarMezcla(OrdenMedicaForm ordenMedicaForm,
			ActionMapping mapping, Connection con, UsuarioBasico medico,
			PersonaBasica paciente, HttpServletRequest request)
			throws SQLException {
		// inicializa los datos para la mezcla
		ordenMedicaForm.setListadoHistoCuidadosEsp(new HashMap());
		ordenMedicaForm.setListadoNutParentHisto(new LinkedList());
		OrdenMedica.elimiarTodosArticulosNuevos(ordenMedicaForm
				.getMapaCompleto(), ordenMedicaForm.getNumeroElementos(), "");
		ordenMedicaForm.setNumeroElementos(0);

		OrdenMedica mundoOrdenMedica = new OrdenMedica();
		ordenMedicaForm.setListadoNutParentHisto(mundoOrdenMedica.consultarNutricionParentHisto(con, paciente.getCodigoCuenta(),
						paciente.getCodigoCuentaAsocio(), ordenMedicaForm.getMezcla()));

		/*
		 * Voy a utilizar el campo cuenta asocio para enviar la mezcla y no
		 * tener que cambiar mucho!
		 */
		ordenMedicaForm.setListadoNutParent(mundoOrdenMedica.consultarTipos(
				con, medico.getCodigoInstitucionInt(),
				paciente.getCodigoArea(), 2, ordenMedicaForm.getMezcla(),
				paciente.getCodigoCuenta(), paciente.getCodigoCuentaAsocio(),
				-1, -1));
		ordenMedicaForm.setVelocidadInfusion("");
		ordenMedicaForm.setDosificacion("");
		ordenMedicaForm.setEstado("empezar");
		ordenMedicaForm.setNutricionParenteral(false);

		// Saca mensaje sobre farmacia
		if (Utilidades.convertirAEntero(ordenMedicaForm
				.getListadoAlmacenesMap().get("numRegistros").toString()) <= 0) {
			ActionErrors errores = new ActionErrors();
			errores.add("descripcion", new ActionMessage("errors.notEspecific",
					"CENTRO DE COSTOS " + paciente.getArea()
							+ " SIN ALMACEN ASIGNADO. "));
			saveErrors(request, errores);
		}

		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Elimina un articulo de la mezcla
	 * 
	 * @param ordenMedicaForm
	 *            forma
	 * */
	public void metodoEliminarArticuloMezcla(OrdenMedicaForm forma) {
		forma.setMapaCompleto(OrdenMedica.elimiarTodosArticulosNuevos(forma
				.getMapaCompleto(), forma.getNumeroElementos(), forma
				.getIndicadorUtilitario()));
		forma.setNumeroElementos(forma.getNumeroElementos() - 1);
		forma.setEstado("empezar");
	}

	/**
	 * Este método especifica las acciones a realizar en el estado empezar.
	 * 
	 * @param ordenMedicaForm
	 *            OrdenMedicaForm para pre-llenar datos si es necesario
	 * @param request
	 *            HttpServletRequest para obtener los datos
	 * @param mapping
	 *            Mapping para manejar la navegación
	 * @param con
	 *            Conexión con la fuente de datos
	 * @return ActionForward a la página principal "ordenMedica.jsp"
	 * @throws SQLException
	 */

	private ActionForward accionEmpezar(OrdenMedicaForm ordenMedicaForm,
			ActionMapping mapping, Connection con, UsuarioBasico medico,
			PersonaBasica paciente) throws SQLException {
		OrdenMedica mundoOrdenMedica = new OrdenMedica();
		// ----Consutar Listado de tipos de nutricion Parenteral
		// System.out.print("\n\n El centro de costo  " +
		// paciente.getCodigoArea());
		// System.out.print("\n\n El Codigo Institucion " +
		// medico.getCodigoInstitucionInt());

		// ----Limpiamos lo que venga del form
		ordenMedicaForm.reset();
		ordenMedicaForm.resetSecciones();

		// Se verifica si se debe abrir la referencia
		ordenMedicaForm.setDeboAbrirReferencia(UtilidadTexto
				.getBoolean(ValoresPorDefecto
						.getLlamadoAutomaticoReferencia(medico
								.getCodigoInstitucionInt())));

		ordenMedicaForm.setListadoTiposMonitoreo(mundoOrdenMedica
				.consultarTipos(con, medico.getCodigoInstitucionInt(), paciente
						.getCodigoArea(), 5, paciente.getCodigoPersona(),
						paciente.getCodigoCuenta(), paciente
								.getCodigoCuentaAsocio(), -1, -1));
		// ---Se quitan los tipos de monitoreo repetidos ----//
		ordenMedicaForm.setListadoTiposMonitoreo(Utilidades
				.coleccionSinRegistrosRepetidos(ordenMedicaForm
						.getListadoTiposMonitoreo(), "codigo"));

		// ---Consultar los tipos de soporte respiratorio (equipo - Elemento)
		ordenMedicaForm.setListadoSoporteRes(mundoOrdenMedica.consultarTipos(
				con, medico.getCodigoInstitucionInt(),
				paciente.getCodigoArea(), 4, -1, paciente.getCodigoCuenta(),
				paciente.getCodigoCuentaAsocio(), -1, -1));

		// ---Consutar Listado de tipos de nutricion Oral
		//logger.info("Antes de cargar tipos");
		ordenMedicaForm.setListadoNutOral(mundoOrdenMedica.consultarTipos(con,
				medico.getCodigoInstitucionInt(), paciente.getCodigoArea(), 1,
				-1, paciente.getCodigoCuenta(), paciente
						.getCodigoCuentaAsocio(), -1, -1));
/*		logger.info("Despu�s de cargar tipos "+medico.getCodigoInstitucionInt()+" "+paciente.getCodigoArea());
		logger.info("Tama�o de la colecci�n "+ordenMedicaForm.getListadoNutOral().size());*/
		// ---Se quitan los tipos de nutricion oral repetidos ----//
		ordenMedicaForm.setListadoNutOral(Utilidades
				.coleccionSinRegistrosRepetidos(ordenMedicaForm
						.getListadoNutOral(), "codigo_tipo_oral"));

		// ordenMedicaForm.setListadoNutParent(
		// mundoOrdenMedica.consultarTipos(con,
		// medico.getCodigoInstitucionInt(), paciente.getCodigoArea(), 2, -1,
		// -1, -1) );

		ordenMedicaForm.setListadoOtrosNutOral(mundoOrdenMedica
				.consultarOtrosNutricionOral(con, paciente.getCodigoCuenta(),
						paciente.getCodigoCuentaAsocio()));

		// --Consultar los tipos de cuidados de Emfermeria
		ordenMedicaForm.setListadoCuidadosEmfermeria(new ArrayList());
		ordenMedicaForm.setListadoCuidadosEmfermeria(mundoOrdenMedica
				.consultarTipos(con, medico.getCodigoInstitucionInt(), paciente
						.getCodigoArea(), 3, -1, paciente.getCodigoCuenta(),
						paciente.getCodigoCuentaAsocio(), -1, -1));

		// ---Consultar los tipos de monitoreo historicos a salir en el popup de
		// ver anteriores
		ordenMedicaForm.setMonitoreosHisto(mundoOrdenMedica
				.consultarMonitoreosHisto(con, paciente.getCodigoCuenta(),
						paciente.getCodigoCuentaAsocio()));

		// ---Consultar los tipos de soporte respiratorio (equipo - Elemento)
		// historicos
		ordenMedicaForm.setSoporteRespiraHisto(mundoOrdenMedica
				.consultarSoporteRespiraHisto(con, paciente.getCodigoCuenta(),
						paciente.getCodigoCuentaAsocio()));

		// ---Consultar Historicos de detalle de nutricion Oral
		ordenMedicaForm.setListadoNutOralHisto(mundoOrdenMedica
				.consultarNutricionOralHisto(con, paciente.getCodigoCuenta(),
						paciente.getCodigoCuentaAsocio()));

		// -Consultar Historicos de detalle de nutricion parenteral
		// ordenMedicaForm.setListadoNutParentHisto(
		// mundoOrdenMedica.consultarNutricionParentHisto
		// (con,paciente.getCodigoCuenta(), paciente.getCodigoCuentaAsocio(),
		// ordenMedicaForm.getMezcla()));
		ordenMedicaForm.setListadoNutParentHisto(new ArrayList());
		ordenMedicaForm.setListadoNutParent(new ArrayList());

		// -Consultar los historicos de cuidados de enfermeria
		ordenMedicaForm.setFechaCuidado(UtilidadFecha
				.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		ordenMedicaForm.setListadoCuidadosEnfHisto(mundoOrdenMedica
				.consultarCuidadosEnfHisto(con, paciente.getCodigoCuenta(),
						paciente.getCodigoCuentaAsocio(), medico
								.getCodigoInstitucionInt(), paciente
								.getCodigoArea(), ordenMedicaForm
								.getFechaCuidado(), ordenMedicaForm
								.getFechaCuidado()));
		// ordenMedicaForm.setListadoCuidadosEnfHisto( new ArrayList());
		// -Consultar las mezclas finalizadas que se mostrarán en ver
		// anteriores
		ordenMedicaForm.setMezclasFinalizadasAnteriores(mundoOrdenMedica.consultarMezclasParenteral(con, paciente.getCodigoCuenta(),paciente.getCodigoCuentaAsocio(), 1));

		ordenMedicaForm.setListadoMezclasSinFinalizar(mundoOrdenMedica
				.consultarTipos(con, medico.getCodigoInstitucionInt(), 0, 6, 0,
						paciente.getCodigoCuenta(), paciente
								.getCodigoCuentaAsocio(), 0, 0));

		
		
		//cargar los resultados laboratorios.
		ordenMedicaForm.setResultadoLaboratorios(mundoOrdenMedica.cargarResultadoLaboratorios(con, paciente.getCodigoIngreso(), ConstantesBD.codigoNuncaValido, paciente.getCodigoArea(), true,false));
		ordenMedicaForm.setResultadoLaboratoriosHistoricos(mundoOrdenMedica.cargarResultadoLaboratorios(con, paciente.getCodigoIngreso(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, false,true));
		
		
		//cargar los resultados laboratorios.
		ordenMedicaForm.setValoracionEnfermeria(RegistroEnfermeria.cargarValoracionEnfermeria(con, paciente.getCodigoIngreso(), ConstantesBD.codigoNuncaValido, paciente.getCodigoArea(), true,false));
		ordenMedicaForm.setValoracionEnfermeriaHistoricos(RegistroEnfermeria.cargarValoracionEnfermeria(con, paciente.getCodigoIngreso(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, false,true));


		
		// --Consultar otros tipos de cuidados de enfermeria
		// ordenMedicaForm.setListadoOtrosCuidadosEnfer(mundoOrdenMedica.
		// consultarOtrosCuidadosEnfer(con,paciente.getCodigoCuenta(),
		// paciente.getCodigoCuentaAsocio()));

		// logger.info("("+dateFormatter.format(fecha)+
		// ")           CODIGO CUENTA DE ASOCIO->"
		// +paciente.getCodigoCuentaAsocio()+"\n\n");

		// -Consultar La Orden Medica Si La Tiene para cargar la información en
		// el formulario

		if (mundoOrdenMedica.cargarOrdenMedica(con, paciente.getCodigoCuenta(),
				false)) {
			// ---------Si el paciente tiene cuenta de asocio se carga la
			// descripción soporte, dieta y las observaciones
			// ----------------//
			if (paciente.getCodigoCuentaAsocio() != 0)
				mundoOrdenMedica.cargarDatosUrgencias(con, paciente
						.getCodigoCuentaAsocio());

			// -Se carga el ultimo tipo de monitoreo (si lo hay)
			mundoOrdenMedica.cargarTipoMonitoreo(con, paciente
					.getCodigoCuenta());

			// Se carga la información del soporte respiratorio
			boolean existeSoporte = mundoOrdenMedica.cargarSoporteRespiratorio(
					con, paciente.getCodigoCuenta(), false, medico
							.getCodigoInstitucionInt(), paciente
							.getCodigoArea());

			// logger.info("("+dateFormatter.format(fecha)+
			// ")           EXISTE SOPORTE->"+existeSoporte+"\n\n");
			if (!existeSoporte)
				ordenMedicaForm.setNoHaySoporte(true);

			// Se carga la información de la dieta
			mundoOrdenMedica.cargarDieta(con, paciente.getCodigoCuenta(),
					false, medico.getCodigoInstitucionInt(), paciente
							.getCodigoArea(), ordenMedicaForm);

			// -Se carga la información de la orden de Hoja Neurológica
			mundoOrdenMedica.cargarOrdenHojaNeurologica(con);

			llenarForm(ordenMedicaForm, mundoOrdenMedica, 1);
		}
		// Sino tiene orden médica con el número de cuenta
		else {
			// Se verfica que tenga cuenta de asocio
			if (paciente.getCodigoCuentaAsocio() != 0) {
				// Se carga la orden medica con la cuenta de asocio
				if (mundoOrdenMedica.cargarOrdenMedica(con, paciente
						.getCodigoCuentaAsocio(), true)) {
					// -Se carga el ultimo tipo de monitoreo (si lo hay) con la
					// cuenta de asocio
					mundoOrdenMedica.cargarTipoMonitoreo(con, paciente
							.getCodigoCuentaAsocio());

					// Se carga la información del soporte respiratorio con la
					// cuenta de asocio
					boolean existeSoporte = mundoOrdenMedica
							.cargarSoporteRespiratorio(con, paciente
									.getCodigoCuentaAsocio(), true, medico
									.getCodigoInstitucionInt(), paciente
									.getCodigoArea());

					// logger.info("("+dateFormatter.format(fecha)+
					// ")           EXISTE SOPORTE->"+existeSoporte+"\n\n");
					if (!existeSoporte)
						ordenMedicaForm.setNoHaySoporte(true);

					// Se carga la información de la dieta con la cuenta de
					// asocio
					mundoOrdenMedica.cargarDieta(con, paciente
							.getCodigoCuentaAsocio(), true, medico
							.getCodigoInstitucionInt(), paciente
							.getCodigoArea(), ordenMedicaForm);

					// -Se carga la información de la orden de Hoja
					// Neurológica
					mundoOrdenMedica.cargarOrdenHojaNeurologica(con);

					llenarForm(ordenMedicaForm, mundoOrdenMedica, 1);
				}
			}// if cuenta no es de asocio
			else {
				ordenMedicaForm.setNoHaySoporte(true);
			}
		}

		//logger.info("ordenMedicaForm.getMezcla() "+ordenMedicaForm.getMezcla());
		if (ordenMedicaForm.getMezcla() != 0) {
			ordenMedicaForm.setListadoNutParentHisto(mundoOrdenMedica
					.consultarNutricionParentHisto(con, paciente
							.getCodigoCuenta(), paciente
							.getCodigoCuentaAsocio(), mundoOrdenMedica
							.getMezcla()));
			/*
			 * Voy a utilizar el campo cuenta asocio para enviar la mezcla y no
			 * tener que cambiar mucho!
			 */
			ordenMedicaForm.setListadoNutParent(mundoOrdenMedica
					.consultarTipos(con, medico.getCodigoInstitucionInt(),
							paciente.getCodigoArea(), 2, mundoOrdenMedica
									.getMezcla(), paciente.getCodigoCuenta(),
							paciente.getCodigoCuentaAsocio(), -1, -1));
		}

		// ***********VALIDACION PARA LA SECCION DE PRESCRIPCION
		// DIALISIS******************
		ordenMedicaForm
				.setDeboAbrirPrescripcionDialisis(UtilidadesOrdenesMedicas
						.deboAbrirPrescripcionDialisis(con, paciente
								.getCodigoArea(), medico
								.getCodigoInstitucionInt()));
		// Si debo abrir la prescripcion de la diálisis cargo la informació
		// necesaria
		if (ordenMedicaForm.isDeboAbrirPrescripcionDialisis()) {
			ordenMedicaForm.setHistoricoDialisis(mundoOrdenMedica
					.getHistoricoPrescripcionDialisis(con, paciente
							.getCodigoPersona()));
			/**
			 * if(ordenMedicaForm.getHistoricoDialisis().size()>0) {
			 * DtoPrescripcionDialisis dialisisUltima = new
			 * DtoPrescripcionDialisis(); dialisisUltima =
			 * ordenMedicaForm.getHistoricoDialisis().get(0);
			 * ordenMedicaForm.setDialisis(dialisisUltima); }
			 **/

			if (ordenMedicaForm.getDialisis().getHemodialisis().size() == 0)
				ordenMedicaForm.getDialisis().getHemodialisis().add(
						new DtoHemodialisis()); // se agrega una hemodialisis
												// por defecto
			// ordenMedicaForm.getDialisis().setFiltros(OrdenMedica.
			// cargarArregloPrescripcionDialisis(con,
			// DtoPrescripcionDialisis.tipoConsultaFiltro));
			ordenMedicaForm
					.getDialisis()
					.setFlujosBomba(
							OrdenMedica
									.cargarArregloPrescripcionDialisis(
											con,
											DtoPrescripcionDialisis.tipoConsultaFlujoBomba,
											""));
			ordenMedicaForm.getDialisis().setFlujosDializado(
					OrdenMedica.cargarArregloPrescripcionDialisis(con,
							DtoPrescripcionDialisis.tipoConsultaFlujoDializado,
							""));
			ordenMedicaForm.getDialisis().setAccesosVasculares(
					OrdenMedica.cargarArregloPrescripcionDialisis(con,
							DtoPrescripcionDialisis.tipoConsultaAccesoVascular,
							""));
			ordenMedicaForm.getDialisis().setRecambios(
					OrdenMedica.cargarArregloPrescripcionDialisis(con,
							DtoPrescripcionDialisis.tipoConsultaRecambio, ""));
			ordenMedicaForm.getDialisis().setVolumenes(
					OrdenMedica.cargarArregloPrescripcionDialisis(con,
							DtoPrescripcionDialisis.tipoConsultaVolumen, ""));
			ordenMedicaForm.getDialisis().setTiposMembrana(
					OrdenMedica.cargarArregloPrescripcionDialisis(con,
							DtoPrescripcionDialisis.tipoConsultaTiposMembrana,
							""));

		}
		//********************************************************************************		
		//Consulta la informacion de lasfrecuencias para los cuidados de enfermeria 
	 	if(!ordenMedicaForm.getListadoCuidadosEmfermeria().isEmpty())
	 	{
	 		ordenMedicaForm.setArrayFrecuenciasCuidadoEnfer(ProgramacionCuidadoEnfer.consultarFrecuenciaCuidado(
	 				con,
	 				paciente.getCodigoIngreso()+"",
	 				ConstantesBD.codigoNuncaValido,
	 				true));
	 		
	 		ordenMedicaForm.setArrayTipoFrecuencias(ProgramacionCuidadoEnfer.consultarTipoFrecuenciaInst(con,medico.getCodigoInstitucionInt()));
	 		ordenMedicaForm.setMapa(RegistroEnfermeria.cargarFrecPeriodoCuidadosEnfer(ordenMedicaForm.getMapa(), ordenMedicaForm.getArrayFrecuenciasCuidadoEnfer()));
	 	}

		ordenMedicaForm.setEstado("empezar");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * @param ordenMedicaForm
	 * @param mundoOrdenMedica
	 */
	private void llenarForm(OrdenMedicaForm ordenMedicaForm,
			OrdenMedica mundoOrdenMedica, int tipo) {
		//logger.info("-----------ENTRO A LLANER FORM-----------------------\n")
		// ;
		// -----------------------------ENCABEZADO DE LbbA ORDEN
		// MEDICA----------------------------------------------------//
		/*
		ordenMedicaForm.setFechaOrden(UtilidadFecha
				.conversionFormatoFechaAAp(mundoOrdenMedica.getFechaOrden()));
		ordenMedicaForm.setHoraOrden(UtilidadFecha.convertirHoraACincoCaracteres(mundoOrdenMedica.getHoraOrden()));
		*/
		  ordenMedicaForm.setFechaOrden(UtilidadFecha.conversionFormatoFechaAAp(
		  UtilidadFecha.getFechaActual()));
		  ordenMedicaForm.setHoraOrden(UtilidadFecha.getHoraActual());

		 ordenMedicaForm.setFechaOrden(UtilidadFecha
				.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual()));
		ordenMedicaForm.setDescripcionSoporteRespiratorio(mundoOrdenMedica
				.getDescripcionSoporteRespiratorio());
		ordenMedicaForm.setDescripcionDieta(mundoOrdenMedica
				.getDescripcionDieta());
		ordenMedicaForm.setDescripcionDietaParenteral(mundoOrdenMedica
				.getDescripcionDietaParenteral());
		ordenMedicaForm.setObservacionesGenerales(mundoOrdenMedica
				.getObservacionesGenerales());
		ordenMedicaForm.setDescripcionSoporteUrgencias(mundoOrdenMedica
				.getDescripcionSoporteUrgencias());
		ordenMedicaForm.setDescripcionDietaUrgencias(mundoOrdenMedica
				.getDescripcionDietaUrgencias());
		ordenMedicaForm.setObservacionesGralesUrgencias(mundoOrdenMedica
				.getObservacionesGralesUrgencias());

		// ---------------------------CAMPOS TEMPORALES PARA SABER SI SE
		// MODIFICO ALGO DE LA ORDEN MEDICA---------------------------//
		ordenMedicaForm.setFechaTempOrden(UtilidadFecha
				.conversionFormatoFechaAAp(mundoOrdenMedica.getFechaOrden()));
		ordenMedicaForm.setHoraTempOrden(mundoOrdenMedica.getHoraOrden());

		// ----------------------------SECCION SOPORTE
		// RESPIRATORIO-------------------------------------------------//
		ordenMedicaForm.setCantidadSoporteRespiratorio(mundoOrdenMedica
				.getCantidadSoporteRespiratorio());
		ordenMedicaForm.setEquipoElemento(mundoOrdenMedica.getEquipoElemento());
		ordenMedicaForm.setOxigenoTerapia(mundoOrdenMedica.getOxigenoTerapia());
		ordenMedicaForm.setCodigoEncabezadoSoporteRespira(mundoOrdenMedica.getCodigoEncabezadoSoporteRespira());
		ordenMedicaForm.setDescripcionIndivSoporteRespira(mundoOrdenMedica.getDescripcionIndivSoporteRespira());
		// ---------------------------CAMPOS TEMPORALES PARA SABER SI SE
		// MODIFICO ALGO EN EL SOPORTE---------------------------//
		ordenMedicaForm.setTempCantidadSoporte(mundoOrdenMedica
				.getCantidadSoporteRespiratorio());
		ordenMedicaForm.setTempEquipoElemento(mundoOrdenMedica
				.getEquipoElemento());
		ordenMedicaForm.setTempOxigenoTerapia(mundoOrdenMedica
				.getOxigenoTerapia());

		// ---------------------------------------------------SECCION
		// DIETA-------------------------------------------------------------//
		ordenMedicaForm.setNutricionOral(mundoOrdenMedica.getNutricionOral());
		// ordenMedicaForm.setNutricionParenteral(mundoOrdenMedica.
		// getNutricionParenteral());
		ordenMedicaForm.setFinalizarDieta(mundoOrdenMedica.isFinalizarDieta());
		ordenMedicaForm.setFinalizarDietaEnfermeria(mundoOrdenMedica
				.isFinalizarDietaEnfermeria());
		ordenMedicaForm.setOtroNutORal(mundoOrdenMedica.getOtroNutORal());
		/*
		 * ordenMedicaForm.setMezcla(mundoOrdenMedica.getMezcla());
		 * ordenMedicaForm.setMezclaTempo(mundoOrdenMedica.getMezcla());
		 */
		// ---------------------------CAMPOS TEMPORALES PARA SABER SI SE
		// MODIFICO ALGO EN LA DIETA---------------------------//
		ordenMedicaForm.setTempNutricionOral(mundoOrdenMedica
				.getNutricionOral());
		ordenMedicaForm.setTempNutricionParenteral(mundoOrdenMedica
				.getNutricionParenteral());
		ordenMedicaForm.setTempFinalizarDieta(mundoOrdenMedica
				.isFinalizarDieta());
		ordenMedicaForm.setTempfinalizarDietaEnfermeria(mundoOrdenMedica
				.isFinalizarDietaEnfermeria());
		ordenMedicaForm.setTempOtroNutOral(mundoOrdenMedica.getOtroNutORal());

		// ---------------------------CAMPO DEL ULTIMO MONITOREO EN (EVOLUCIONES
		// Ó ORDEN MEDICA)-----------------------------//
		ordenMedicaForm.setTipoMonitoreo(mundoOrdenMedica.getTipoMonitoreo());
		ordenMedicaForm.setTipoMonitoreoTemp(mundoOrdenMedica
				.getTipoMonitoreo());

		// ---------------------------NUTRICIONES ORALES NO PARAMETRIZADAS EN
		// HOSPITALIZACIÓN----------------------------------//
		// logger.info("setOtrasNutricionesOrales en llenar form->"+
		// mundoOrdenMedica.getOtrasNutricionesOrales()+"\n");

		ordenMedicaForm.setOtrasNutricionesOrales(mundoOrdenMedica
				.getOtrasNutricionesOrales());

		// -------------------------------------------------LLENAR EL
		// MAPA--------
		// ----------------------------------------------------------------//
		ordenMedicaForm.setMapaCompleto(mundoOrdenMedica.getMapaCompleto());

		// ------------------------------------------------LLENAR EL MAPA
		// TEMPORAL PARA LA
		// VALIDACIÓN----------------------------------------//
		Vector codigos = new Vector();
		Vector codigosOtros = new Vector();

		if (tipo == 1) {
			codigos = mundoOrdenMedica.getCodigosNutOral();
			codigosOtros = mundoOrdenMedica.getCodigosOtroNutOral();
		} else {
			codigos = (Vector) ordenMedicaForm.getMapa("codigosNutOral");
			codigosOtros = (Vector) ordenMedicaForm
					.getMapa("codigosOtroNutOral");
		}

		// -------------SE CARGA LA INFORMACIÓN DE LOS TIPOS DE NUTRICION ORAL
		// EN EL MAPA TEMPORAL DEL FORM-----------//
		if (codigos != null) {
			for (int i = 0; i < codigos.size(); i++) {
				int tipoNutOral = Integer.parseInt(codigos.elementAt(i) + "");

				String tipoNutricion = (String) mundoOrdenMedica
						.getMapa("tipoNut_" + tipoNutOral);

				if (tipoNutricion != null) {
					// logger.info("tipoNutricion en llenar form (if)->"+
					// tipoNutricion
					// +" en mapa "+ordenMedicaForm.getMapa("tipoNut_"
					// +tipoNutOral)+"\n");
					ordenMedicaForm.setTempMapa("tipoNut_" + tipoNutOral,
							tipoNutricion);
				} else {
					// logger.info("tipoNutricion en llenar form (else)->"+
					// tipoNutricion+"\n");
					ordenMedicaForm.setTempMapa("tipoNut_" + tipoNutOral, "");
				}
			}
		}

		// -------------SE CARGA LA INFORMACIÓN DE LOS OTROS TIPOS DE NUTRICION
		// ORAL EN EL MAPA TEMPORAL DEL FORM-----------//
		if (codigosOtros != null) {
			for (int i = 0; i < codigosOtros.size(); i++) {
				int tipoNutOralOtro = Integer.parseInt(codigosOtros
						.elementAt(i)
						+ "");

				String tipoNutricionOtro = (String) mundoOrdenMedica
						.getMapa("tipoNutOtro_" + tipoNutOralOtro);

				if (tipoNutricionOtro != null) {
					// *logger.info("tipoNutricionOtro en llenar form (if)->"+
					// tipoNutricionOtro+"\n");
					ordenMedicaForm.setTempMapa("tipoNutOtro_"
							+ tipoNutOralOtro, tipoNutricionOtro);
				} else {
					// logger.info("tipoNutricionOtro en llenar form (else)->"+
					// tipoNutricionOtro+"\n");
					ordenMedicaForm.setTempMapa("tipoNutOtro_"
							+ tipoNutOralOtro, "");
				}
			}
		}

		// ------------Se resetean los radio de la sección cuidados de
		// enfermería---------------//
		if (tipo == 2) {
			Vector codigosCuidadoEnf = (Vector) ordenMedicaForm
					.getMapa("codigosCuidadoEnf");

			if (codigosCuidadoEnf != null) {
				for (int i = 0; i < codigosCuidadoEnf.size(); i++) {
					// ------El codigo del tipo de cuidado de enfermeria
					int tipoCuidado = Integer.parseInt(codigosCuidadoEnf
							.elementAt(i)
							+ "");

					// ------Extraer
					ordenMedicaForm.setMapa("presenta_" + tipoCuidado, null);
				}
			}// if codigosCuidadoEnf != null

			// Se resetea el presenta de otro cuidado de enfermería
			ordenMedicaForm.setMapa("presentaOtro", null);

		}

		// ------Orden de la Hoja Neurológica -------------//
		ordenMedicaForm.setPresentaHojaNeuro(mundoOrdenMedica
				.getPresentaHojaNeuro());
		ordenMedicaForm.setObservacionesHojaNeuro(mundoOrdenMedica
				.getObservacionesHojaNeuro());
		ordenMedicaForm.setFinalizadaHojaNeuro(mundoOrdenMedica
				.getFinalizadaHojaNeuro());
	}

	
	/***********************************************************************************************************************
	 *Metodo encargado de suspender una mezcla o cada una de las solicitudes 
	 * @throws SQLException 
	 */
	
	private ActionForward  suspenderMezcla (Connection con, ActionMapping mapping, OrdenMedicaForm forma,UsuarioBasico usuario,PersonaBasica paciente) throws SQLException
	{
		OrdenMedica mundo = new OrdenMedica();
		if (UtilidadCadena.noEsVacio(forma.getSuspender()))
		{
			Iterator iterador = forma.getListadoNutParentHisto().iterator();
			String tmp="";
			while (iterador.hasNext()) 
			{
				
				HashMap elemento = (HashMap) iterador.next();
				if(!tmp.equals(elemento.get("coddieta")+""))
				{ 
					logger.info(" !!!!!!!!!!!!!!!!!!!   codigo -> "+elemento.get("coddieta"));
					if (forma.getSuspender().equals("T") || UtilidadCadena.indexOf(forma.getSuspender().split(","), elemento.get("coddieta")+"")>=0 )
						mundo.suspenderMezcla(con, ConstantesBD.acronimoSi, Utilidades.convertirAEntero(elemento.get("coddieta")+""),usuario.getLoginUsuario());
					 tmp=elemento.get("coddieta")+"";
				}
			}
		}
		

		forma.setEstado("empezar");
		return accionEmpezar(forma, mapping, con, usuario, paciente); 
	}
	
	
	/*
	 * 
	 ***********************************************************************************************************************/
	
	/**
	 * Este método especifica las acciones a realizar en el estado salir. Se
	 * copian las propiedades del objeto ordenMedicaen el objeto mundo
	 * 
	 * @param ordenMedicaForm
	 *            OrdenMedicaForm
	 * @param request
	 *            HttpServletRequest para obtener datos de la session
	 * @param mapping
	 *            Mapping para manejar la navegación
	 * @param con
	 *            Conexión con la fuente de datos
	 * @param request
	 * 
	 * @return ActionForward "ordenMedica.do?estado=resumen"
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionSalir(OrdenMedicaForm ordenMedicaForm,
			ActionMapping mapping, Connection con, int cuenta, String login,
			String datosMedico, HttpSession session, HttpServletRequest request)
			throws SQLException, IPSException {
		
		
		
		
		if(ordenMedicaForm.getObservacionesGeneralesNueva()!=null && !ordenMedicaForm.getObservacionesGeneralesNueva().equals("")){

			if(ordenMedicaForm.getObservacionesGeneralesNueva().length()>1000){
				ordenMedicaForm.setObservacionesGeneralesNueva(ordenMedicaForm.getObservacionesGeneralesNueva().replace("@",""));
				ActionErrors errores = new ActionErrors();
				errores.add("descripcion", new ActionMessage("errors.notEspecific",
				"La observaci�n general no puede ser mayor a 1.000 caracteres "));
				saveErrors(request, errores);
				//MT2300
				ordenMedicaForm.setProcesoExitoso(false);	
				ordenMedicaForm.setEstado("empezar");

				return mapping.findForward("principal");
			}
		}
		
		
		int codigoOrden = 0, codigoEncabezado = 0;
		int numeroSolicitud = 0;
		int operacionRealizada = 0;
		boolean codigoEncabezadoUsado = false;
		ArrayList<Integer> listaErroresRegistroAlertas = new ArrayList<Integer>();
		//se limpian las alertas antiguas 
		ordenMedicaForm.resetInterfaz();
		
		OrdenMedica mundoOrdenMedica = new OrdenMedica();
		UsuarioBasico usuario = (UsuarioBasico) session
				.getAttribute("usuarioBasico");
		PersonaBasica paciente = (PersonaBasica) session
				.getAttribute("pacienteActivo");
		
		boolean esHojaQuirurgicaModificada = verificarInsertaronDatosHojaNeurologica(ordenMedicaForm, mundoOrdenMedica);
		
		llenarMundo(ordenMedicaForm, mundoOrdenMedica, usuario);
		operacionRealizada = verificarInsertaronDatosDieta(ordenMedicaForm);
		logger.info("Valor de la Operacion Realiza dentro de Ordenes Medicas >> "
						+ operacionRealizada
						+ " >> 1.nutricion oral, 2. mezcla, 3.oral y mezlca. 4.finalizar dieta. 6.Solo Observaciones ");

		if (operacionRealizada == 2
				&& Utilidades.convertirAEntero(ordenMedicaForm
						.getListadoAlmacenesMap().get("numRegistros")
						.toString()) <= 0) {
			ActionErrors errores = new ActionErrors();
			errores.add("descripcion", new ActionMessage("errors.notEspecific",
					"CENTRO DE COSTOS " + paciente.getArea()
							+ " SIN ALMACEN ASIGNADO. "));
			saveErrors(request, errores);
			//MT2300
			ordenMedicaForm.setProcesoExitoso(false);	
			ordenMedicaForm.setEstado("empezar");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}

		// ******INICIA TRANSACCION*********//
		boolean enTransaccion = UtilidadBD.iniciarTransaccion(con);
		// *********************************//

		// Verifica en ordenes medicas si ya existe una para la cuenta
		DtoResultado respuesta= mundoOrdenMedica.insertarOrdenMedica(con, cuenta, false,ordenMedicaForm.isExisteNuevoRegObservacionesGral());
		if(respuesta != null){
			codigoOrden=Integer.valueOf(respuesta.getPk());
		}
		enTransaccion = codigoOrden > 0;

		if (enTransaccion) {
			/**
             * Se inserta el registro de alerta para registro de enfermer�a MT-3438
             */
			if (ordenMedicaForm.isExisteNuevoRegObservacionesGral()) {
				listaErroresRegistroAlertas = mundoOrdenMedica.registrarAlertaOrdenMedica(con, 
						ConstantesBD.seccionObservacionesGenerales, 
						cuenta, login);
			}
			
			// ---------------------------------------------Hoja Neurologica
			//******************************************************************
			// ******************************
			mundoOrdenMedica.insertarEncabezadoOrdenHojaNeurologica(con,
					codigoOrden, login, datosMedico, false);
			/**
             * Se inserta el registro de alerta para registro de enfermer�a MT-3438
             */
			if (esHojaQuirurgicaModificada) {
				listaErroresRegistroAlertas = mundoOrdenMedica.registrarAlertaOrdenMedica(con, 
						ConstantesBD.seccionHojaNeurologica, 
						cuenta, login);
			}
			
			//******************************************************************
			// ******************************

			// -----Insertar encabezado historico en Encabezado Histo Orden
			// Medica
			codigoEncabezado = mundoOrdenMedica.insertarEncabezadoOrdenMedica(
					con, codigoOrden, login, datosMedico, false);
			enTransaccion = codigoEncabezado > 0;

			if (enTransaccion) {
				
				//Si se inserto correctamente el encabezado Histo Orden M�dica 
				//se actualiza la observaci�n que se guardo con el fin de asociarla al
				//encabezado; esta implementaci�n se realiza de esta manera por la forma en que 
				//esta implementada toda la funcionalidad  11/10/2012
				
				//Se valida que se haya generado observacion de la orden m�dica
				if(respuesta != null && respuesta.getPk2() != null && codigoEncabezado > 0){
					int codigoPkDetalleObservacion=Integer.valueOf(respuesta.getPk2());
					mundoOrdenMedica.asociarDetalleObservacionEncabezado(con, codigoPkDetalleObservacion, codigoEncabezado);
				}
				// ---------------------------------Soporte Respiratorio Y Tipo
				// de Monitoreo
				//**************************************************************
				// *********************************
				// Devuelve true si insertaron o cambiaron datos en el soporte
				// respiratorio
				boolean resp = verificarInsertaronDatosSoporte(ordenMedicaForm);
				logger.info("valor respuesta 1: "+resp);
				if (enTransaccion && resp) {
					enTransaccion = mundoOrdenMedica
							.insertarOrdenSoporteRespiratorio(con,
									codigoEncabezado, false) > 0;
					ordenMedicaForm.setNoHaySoporte(false);
					
					/**
		             * Se inserta el registro de alerta para registro de enfermer�a MT-3438
		             */
					listaErroresRegistroAlertas = mundoOrdenMedica.registrarAlertaOrdenMedica(con, 
							ConstantesBD.seccionSoporteRespiratorio, 
							cuenta, login);
					
				} else if(enTransaccion && !resp && !ordenMedicaForm.getDescripcionSoporteNueva().isEmpty()) {
					enTransaccion = mundoOrdenMedica.actualizarDescripcionSoporteRespiratorio(con);
				}


				//insertar respuestalaboratorio.
				enTransaccion = mundoOrdenMedica.insertaResultadosLaboratorios(con, codigoEncabezado,ordenMedicaForm.getResultadoLaboratorios()) > 0;

				
				// -----Insertar Tipos Monitoreo
				if (enTransaccion
						&& (ordenMedicaForm.getTipoMonitoreo() != 0)
						&& (ordenMedicaForm.getTipoMonitoreo() != ordenMedicaForm
								.getTipoMonitoreoTemp())) {
					enTransaccion = mundoOrdenMedica
							.insertarOrdenTipoMonitoreo(con, codigoEncabezado,
									false) > 0;
				}
				//**************************************************************
				// ************************************

				// --------------------------------------------Cuidado
				// Enfermeria
				//*************************************************************************************************
				logger.info("valor transaccion 1 :"+enTransaccion);
				if(enTransaccion
						&& RegistroEnfermeria.insertarModificarCuidadosEnfermeria(
						con, 
						paciente.getCodigoIngreso(),
						ordenMedicaForm.getMapa(),
						ordenMedicaForm.getArrayFrecuenciasCuidadoEnfer(), 
						usuario.getLoginUsuario()))
				{
					logger.info("entra parte 1");
					if(enTransaccion&&verificarInsertaronDatosCuidadosEnf(ordenMedicaForm)){
						logger.info("entra parte 1.1");
						enTransaccion=mundoOrdenMedica.insertarDetalleOrdenCuidadoEnf(
								con,
								codigoEncabezado,
								false,
								paciente.getCodigoIngreso(),
								usuario.getLoginUsuario())>0;
					}
				}
				else
					enTransaccion = false;

				// ********INSERCION DATOS SECCION PRESCRIPCION
				// DIALISIS*********************
				
				//logger.info("Si llega aqui enTransaccion"+enTransaccion+"--------------*************-------------***********------------************");
				if (enTransaccion
						&& ordenMedicaForm.isDeboAbrirPrescripcionDialisis()
						&& ordenMedicaForm.getDialisis().ingresoInformacion()) {
					logger.info("entra parte 2");
					ordenMedicaForm.getDialisis().setCodigoHistoEnca(
							codigoEncabezado + "");
					ordenMedicaForm.getDialisis().setProfesional(usuario);
					if (mundoOrdenMedica.insertarPrescripcionDialisis(con,
							ordenMedicaForm.getDialisis()) <= 0)
						enTransaccion = false;
				}
				//**************************************************************
				// ************
				
				//logger.info("\n ······························ paso por qui 1 ....................................... ");
				
				// ---------------------------------------------------Dieta
				//**************************************************************
				// ****************************************
				logger.info("valor transaccion 2 (la que prepara la dieta) :"+enTransaccion);
				logger.info("valor operacion realizada: "+operacionRealizada);
				if (enTransaccion
						&& (operacionRealizada == 1 || operacionRealizada == 3 || operacionRealizada == 4)) {
					logger.info("\n ······························ paso por qui 2 ....................................... ");
					// -----Inserta Informacion en la tabla orden dieta
					enTransaccion = mundoOrdenMedica.insertarOrdenDieta(con,
							codigoEncabezado, usuario.getCodigoInstitucion(),
							false) > 0;
					logger.info("valor transaccion 3 :"+enTransaccion);
					if (enTransaccion
							&& mundoOrdenMedica.getNutricionParenteral()) {
						//logger.info("\n ······························ paso por qui 3 ....................................... ");
						Collection listadoNutParent = ordenMedicaForm
								.getListadoNutParentHisto();
						Vector codHistoricosParent = new Vector();
						Iterator iterador = listadoNutParent.iterator();
						String tm = "";

						while (iterador.hasNext()) {
							HashMap fila = (HashMap) iterador.next();
							String codigoDieta = fila.get("coddieta") + "";
							if (!tm.equals(codigoDieta)) {
								codHistoricosParent.add(Integer
										.parseInt(codigoDieta));
								tm = fila.get("coddieta") + "";
							}
						}
						int numHistoFinalizados = mundoOrdenMedica
								.finalizarParenteral(con, codHistoricosParent);
						logger
								.info("\n dieta-----------------------------------------------"
										+ numHistoFinalizados);
					}

					// -----Inserta tipos de nutricion (Oral y Parenteral)
					if (enTransaccion && operacionRealizada != 4) {
						//logger.info("\n ······························ paso por qui 4 ....................................... ");
						enTransaccion = mundoOrdenMedica.insertarNutricionOral(
								con, codigoEncabezado, operacionRealizada,
								usuario, paciente, ordenMedicaForm, false) > 0;
						if (enTransaccion) {
							//logger.info("\n ······························ paso por qui 5 ....................................... ");
							codigoEncabezadoUsado = true;

							//**************************************************
							// ***********************************
							// Inserta en la Nutricion Oral
							ActionForward forward = interfazNutricionOral(
									ordenMedicaForm, con, numeroSolicitud,
									codigoEncabezado, paciente, usuario,
									mundoOrdenMedica, mapping, request);
							if (forward != null)
								return forward;
						}
						//******************************************************
						// *********************************
					}
					
					/**
		             * Se inserta el registro de alerta para registro de enfermer�a MT-3438
		             */
					listaErroresRegistroAlertas = mundoOrdenMedica.registrarAlertaOrdenMedica(con, 
							ConstantesBD.seccionDieta, 
							cuenta, login);
				}
			}
		}
		//logger.info("\n ······························ paso por qui 6 ....................................... ");
		// -------------------------------------------------Mezclas
		//**********************************************************************
		// *****************************
		
			if (enTransaccion && ordenMedicaForm.getFarmacia() > 0
					&& (operacionRealizada == 2 || operacionRealizada == 3)) {
				logger
						.info("\n ······························ paso por qui 7 ....................................... ");
				// Evalua si debe generar otro registro en la tabla
				// encabezado_histo_orden_m, se genera en el caso de
				// que se realice una orden medica con mezclas y dieta y que la
				// dieta ya se hubiera registrado en los procesos anteriores
				if (operacionRealizada == 3 && codigoEncabezadoUsado) {
					logger
							.info("Operacion 3. se genera otro encabezado para la mezcla");
					codigoEncabezado = mundoOrdenMedica
							.insertarEncabezadoOrdenMedica(con, codigoOrden, login,
									datosMedico, false);
					enTransaccion = codigoEncabezado > 0;
					
					/**
		             * Se inserta el registro de alerta para registro de enfermer�a MT-3438
		             */
					listaErroresRegistroAlertas = mundoOrdenMedica.registrarAlertaOrdenMedica(con, 
							ConstantesBD.seccionDieta, 
							cuenta, login);
				}
	
				numeroSolicitud = this.insertarSolicitudBasica(con,
						ordenMedicaForm, paciente, usuario);
				
				if (numeroSolicitud > 0 && enTransaccion) {
					logger
							.info("\n ······························ paso por qui 8 ....................................... ");
					// Genera la justificacion NO POS de los articulos de la mezcla
					generarJustificacionNoPosMezclas(ordenMedicaForm, con,
							numeroSolicitud, paciente, usuario, request);
					if (mundoOrdenMedica.insertarOrdenMezcla(con, codigoEncabezado,
							numeroSolicitud, usuario.getCodigoInstitucion(), false) > 0) {
						logger
								.info("\n ······························ paso por qui 12 ....................................... ");
						enTransaccion = mundoOrdenMedica.insertarNutricionParental(
								con, codigoEncabezado, operacionRealizada, usuario,
								paciente, ordenMedicaForm, false) > 0;
						
						/**
			             * Se inserta el registro de alerta para registro de enfermer�a MT-3438
			             */
						listaErroresRegistroAlertas = mundoOrdenMedica.registrarAlertaOrdenMedica(con, 
								ConstantesBD.seccionMezclas, 
								cuenta, login);		
					} else
						enTransaccion = false;
				} else {
	
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
					session.setAttribute("ocultarEncabezadoOrden", null);
					//MT2300
					ordenMedicaForm.setProcesoExitoso(false);	
					return ComunAction.accionSalirCasoError(mapping, request, con,
							logger, "error en el guardar la solicitud básica",
							"error.ordenmedica.transaccion", true);
				}
			} else if (enTransaccion
					&& ordenMedicaForm.getFarmacia() <= 0
					&& (operacionRealizada == 2 || operacionRealizada == 3)
					&& Utilidades.convertirAEntero(ordenMedicaForm
							.getListadoAlmacenesMap().get("numRegistros")
							.toString()) <= 0) {
				if (enTransaccion)
					UtilidadBD.finalizarTransaccion(con);
				else
					UtilidadBD.abortarTransaccion(con);
	
				ActionErrors errores = new ActionErrors();
				errores.add("descripcion", new ActionMessage("errors.notEspecific",
						"CENTRO DE COSTOS " + paciente.getArea()
								+ " SIN ALMACEN ASIGNADO. "));
				saveErrors(request, errores);
				//MT2300
				ordenMedicaForm.setProcesoExitoso(false);	
				ordenMedicaForm.setEstado("empezar");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
		
		//**********************************************************************
		// ********************************

		if (enTransaccion)
			UtilidadBD.finalizarTransaccion(con);
		else
			UtilidadBD.abortarTransaccion(con);
		//MT2300
		ordenMedicaForm.setProcesoExitoso(true);	
		ordenMedicaForm.setEstado("empezar");
		return accionEmpezar(ordenMedicaForm, mapping, con, usuario, paciente);
	}

	//**************************************************************************
	// ***************************************

	/**
	 * Método que muestra los articulos que componen la mezcla con su
	 * respectiva información, de acuerdo a la mezcla finalizada en ver
	 * anteriores seleccionada
	 * 
	 * @param ordenMedicaForm
	 * @param mapping
	 * @param con
	 * @param paciente
	 * @return
	 */
	private ActionForward accionDetalleMezclaAnteriores(
			OrdenMedicaForm ordenMedicaForm, ActionMapping mapping,
			Connection con, PersonaBasica paciente) throws SQLException {
		OrdenMedica mundoOrdenMedica = new OrdenMedica();

		// logger.info("codigo mezcla->"+ordenMedicaForm.getMezclaAnterior());
		// logger.info("codigo encabezado minimo->"+ordenMedicaForm.
		// getCodEncabezadoMinAnterior());
		// logger.info("codigo encabezado mezcla finalizada->"+ordenMedicaForm.
		// getCodEncabezadoAnterior());

		ordenMedicaForm.setDetalleArticulosMezclaAnteriores(mundoOrdenMedica
				.consultarDetalleMezclaAnteriores(con, paciente
						.getCodigoCuenta(), paciente.getCodigoCuentaAsocio(),
						ordenMedicaForm.getMezclaAnterior(), ordenMedicaForm
								.getCodEncabezadoMinAnterior(), ordenMedicaForm
								.getCodEncabezadoAnterior()));

		// logger.info("\n Nro reg consulta -->"+ordenMedicaForm.
		// getDetalleArticulosMezclaAnteriores().size()+"\n");
		ordenMedicaForm.setListadoNutParentAnterior(mundoOrdenMedica
				.consultarTipos(con, -1, -1, 2, ordenMedicaForm
						.getMezclaAnterior(), paciente.getCodigoCuenta(),
						paciente.getCodigoCuentaAsocio(), ordenMedicaForm
								.getCodEncabezadoMinAnterior(), ordenMedicaForm
								.getCodEncabezadoAnterior()));
		// ordenMedicaForm.setEstado("");

		UtilidadBD.closeConnection(con);
		return mapping.findForward("mezclasAnteriores");
	}

	/**
	 * Método que llena el mundo, con los datos del form
	 * 
	 * @param ordenMedicaForm
	 * @param mundoOrdenMedica
	 */
	private void llenarMundo(OrdenMedicaForm ordenMedicaForm,
			OrdenMedica mundoOrdenMedica, UsuarioBasico usuario) {
		// ------------------------------------------ENCABEZADO ORDEN
		// MEDICA------
		// --------------------------------------------------------//
		mundoOrdenMedica.setFechaOrden(UtilidadFecha
				.conversionFormatoFechaABD(ordenMedicaForm.getFechaOrden()));
		mundoOrdenMedica.setHoraOrden(ordenMedicaForm.getHoraOrden());

		if (!ordenMedicaForm.getObservacionesGeneralesNueva().trim().equals("")) {
			String observacionesGeneralesNuevas = ordenMedicaForm
					.getObservacionesGeneralesNueva();
			observacionesGeneralesNuevas = ConstantesBD.separadorSplit+"Fecha Orden: "
					+ ordenMedicaForm.getFechaOrden() + " - "
					+ ordenMedicaForm.getHoraOrden() + "\n"
					+ConstantesBD.separadorSplit+ observacionesGeneralesNuevas+ConstantesBD.separadorSplit;
			ordenMedicaForm.setObservacionesGenerales(UtilidadTexto
					.agregarTextoAObservacionConSeparadorSplit(ordenMedicaForm
							.getObservacionesGenerales(),
							observacionesGeneralesNuevas, usuario, false));
			mundoOrdenMedica.setObservacionesGenerales(ordenMedicaForm
					.getObservacionesGenerales());
			
			ordenMedicaForm.setExisteNuevoRegObservacionesGral(true);
		} else {
			mundoOrdenMedica.setObservacionesGenerales(ordenMedicaForm
					.getObservacionesGenerales());
		}

		// -----------------------------------SECCION TIPO
		// MONITOREO----------------------------------------------------//
		mundoOrdenMedica.setTipoMonitoreo(ordenMedicaForm.getTipoMonitoreo());

		// ---------------------------------SECCION SOPORTE
		// RESPIRATORIO-------------------------------------------//
		// logger.info("EQUIPO ELEMENTO LLENAR MUNDO->"+ordenMedicaForm.
		// getEquipoElemento()+"\n");
		// logger.info("CANTIDAD SOPORTE LLENAR MUNDO->"+ordenMedicaForm.
		// getCantidadSoporteRespiratorio()+"\n");
		// logger.info("OXIGENO TERAPIA LLENAR MUNDO->"+ordenMedicaForm.
		// getOxigenoTerapia()+"\n");
		// logger.info("FINALIZAR SOPORTE LLENAR MUNDO->"+ordenMedicaForm.
		// isFinalizarSoporte()+"\n");
		if (!ordenMedicaForm.getDescripcionSoporteNueva().trim().equals("")) {
			ordenMedicaForm.setDescripcionSoporteRespiratorio(UtilidadTexto
					.agregarTextoAObservacion(ordenMedicaForm
							.getDescripcionSoporteRespiratorio(),
							ordenMedicaForm.getDescripcionSoporteNueva(),
							usuario, false));
			mundoOrdenMedica.setDescripcionSoporteRespiratorio(ordenMedicaForm
					.getDescripcionSoporteRespiratorio());
			
			if(verificarInsertaronDatosSoporte(ordenMedicaForm)){
				ordenMedicaForm.setDescripcionIndivSoporteRespira(UtilidadTexto.agregarTextoAObservacion(
					"", ordenMedicaForm.getDescripcionSoporteNueva(), 
					usuario, false));
		} else {
				ordenMedicaForm.setDescripcionIndivSoporteRespira(UtilidadTexto.agregarTextoAObservacion(
					ordenMedicaForm.getDescripcionIndivSoporteRespira(), ordenMedicaForm.getDescripcionSoporteNueva(), 
					usuario, false));
			}
			
			mundoOrdenMedica.setDescripcionIndivSoporteRespira(ordenMedicaForm.getDescripcionIndivSoporteRespira());
			mundoOrdenMedica.setCodigoEncabezadoSoporteRespira(ordenMedicaForm.getCodigoEncabezadoSoporteRespira());
		} else {
			mundoOrdenMedica.setDescripcionSoporteRespiratorio(ordenMedicaForm
					.getDescripcionSoporteRespiratorio());
		}
		
		mundoOrdenMedica.setEquipoElemento(ordenMedicaForm.getEquipoElemento());
		mundoOrdenMedica.setCantidadSoporteRespiratorio(ordenMedicaForm
				.getCantidadSoporteRespiratorio());
		mundoOrdenMedica.setOxigenoTerapia(ordenMedicaForm.getOxigenoTerapia());
		mundoOrdenMedica.setFinalizarSoporte(ordenMedicaForm
				.isFinalizarSoporte());
		
		// ------------------------------------------------SECCION
		// DIETA------------------------------------------------------//
		if (!ordenMedicaForm.getDescripcionDietaNueva().trim().equals("")) {
			ordenMedicaForm.setDescripcionDieta(UtilidadTexto
					.agregarTextoAObservacion(ordenMedicaForm
							.getDescripcionDieta(), ordenMedicaForm
							.getDescripcionDietaNueva(), usuario, false));
			mundoOrdenMedica.setDescripcionDieta(ordenMedicaForm
					.getDescripcionDieta());
		} else {
			mundoOrdenMedica.setDescripcionDieta(ordenMedicaForm
					.getDescripcionDieta());
		}

		if (!ordenMedicaForm.getDescripcionDietaParenteralNueva().trim()
				.equals("")) {
			ordenMedicaForm.setDescripcionDietaParenteral(UtilidadTexto
					.agregarTextoAObservacion(ordenMedicaForm
							.getDescripcionDietaParenteral(), ordenMedicaForm
							.getDescripcionDietaParenteralNueva(), usuario,
							false));
			mundoOrdenMedica.setDescripcionDietaParenteral(ordenMedicaForm
					.getDescripcionDietaParenteral());
		} else {
			mundoOrdenMedica.setDescripcionDietaParenteral(ordenMedicaForm
					.getDescripcionDietaParenteral());
		}
		
		mundoOrdenMedica.setNutricionOral(ordenMedicaForm.isNutricionOral());
		mundoOrdenMedica.setNutricionParenteral(ordenMedicaForm
				.isNutricionParenteral());
		mundoOrdenMedica.setVolumenTotal(ordenMedicaForm.getVolumenTotal());
		mundoOrdenMedica.setUnidadVolumenTotal(ordenMedicaForm
				.getUnidadVolumenTotal());
		mundoOrdenMedica.setVelocidadInfusion(ordenMedicaForm
				.getVelocidadInfusion());
		mundoOrdenMedica.setDosificacion(ordenMedicaForm.getDosificacion());
		mundoOrdenMedica.setFarmacia(ordenMedicaForm.getFarmacia());
		mundoOrdenMedica.setOtroNutORal(ordenMedicaForm.getOtroNutORal());
		mundoOrdenMedica.setFinalizarDieta(ordenMedicaForm.isFinalizarDieta());
		mundoOrdenMedica.setFinalizarDietaEnfermeria(ordenMedicaForm
				.isFinalizarDietaEnfermeria());
		mundoOrdenMedica.setMezcla(ordenMedicaForm.getMezcla());

		// --------------------------------------SECCION CUIDADOS DE
		// ENFERMERIA------------------------------------//
		mundoOrdenMedica.setOtroCuidadoEnf(ordenMedicaForm.getOtroCuidadoEnf());

		// -Pasarle el hashmap al mundo con los datos de orden medica
		mundoOrdenMedica.setMapaCompleto(ordenMedicaForm.getMapaCompleto());

		// logger.info("setOtrasNutricionesOrales en llenar mundo->"+
		// ordenMedicaForm.getOtrasNutricionesOrales()+"\n");
		mundoOrdenMedica.setOtrasNutricionesOrales(ordenMedicaForm
				.getOtrasNutricionesOrales());

		// ---------------------------------------------Orden Hoja Neurológica
		// --------------------------------------------------------//
		mundoOrdenMedica.setPresentaHojaNeuro(ordenMedicaForm.getPresentaHojaNeuro());

		if (!ordenMedicaForm.getObservacionesHojaNeuroNueva().trim().equals("")) {
			ordenMedicaForm.setObservacionesHojaNeuro(UtilidadTexto
					.agregarTextoAObservacion(ordenMedicaForm
							.getObservacionesHojaNeuro(), ordenMedicaForm
							.getObservacionesHojaNeuroNueva(), usuario, false));
			mundoOrdenMedica.setObservacionesHojaNeuro(ordenMedicaForm
					.getObservacionesHojaNeuro());
		} else {
			mundoOrdenMedica.setObservacionesHojaNeuro(ordenMedicaForm
					.getObservacionesHojaNeuro());
		}

		mundoOrdenMedica.setFinalizadaHojaNeuro(ordenMedicaForm
				.getFinalizadaHojaNeuro());

	}

	/**
	 * Método que verifica si insertaron o modificaron datos en la sección de
	 * soporte respiratorio
	 * 
	 * @param ordenMedicaForm
	 * @return true si ingresaron datos nuevos sino retorna false
	 */

	public boolean verificarInsertaronDatosSoporte(
			OrdenMedicaForm ordenMedicaForm) {
		/*
		 * logger.info("OXIGENO TERAPIA->"+ordenMedicaForm.getOxigenoTerapia() +
		 * " ES IGUAL A  "+ ordenMedicaForm.getTempOxigenoTerapia()+"\n");
		 * logger
		 * .info("CANTIDAD SOPORTE->"+ordenMedicaForm.getCantidadSoporteRespiratorio
		 * () + " ES IGUAL A  "+ ordenMedicaForm.getTempCantidadSoporte()+"\n");
		 * logger.info("EQUIPO ELEMENTO->"+ordenMedicaForm.getEquipoElemento() +
		 * " ES IGUAL A  "+ ordenMedicaForm.getTempEquipoElemento()+"\n");
		 */

		if (!ordenMedicaForm.getOxigenoTerapia().equals(
				ordenMedicaForm.getTempOxigenoTerapia())
				|| ordenMedicaForm.getCantidadSoporteRespiratorio() != ordenMedicaForm
						.getTempCantidadSoporte()
				|| ordenMedicaForm.getEquipoElemento() != ordenMedicaForm
						.getTempEquipoElemento()
				|| (ordenMedicaForm.isNoHaySoporte() == true &&	!ordenMedicaForm.getDescripcionSoporteNueva().isEmpty()))
			return true;
		else
			return false;
	}
	
	/**
	 * M�todo que verifica si insertaron o modificaron datos en la secci�n de
	 * Hoja Neurol�gica
	 * 
	 * @param ordenMedicaForm
	 * @return true si ingresaron datos nuevos
	 */

	public boolean verificarInsertaronDatosHojaNeurologica(
			OrdenMedicaForm ordenMedicaForm, OrdenMedica ordenMedicaMundo) {
		
		if (!ordenMedicaForm.getPresentaHojaNeuro().equals(ordenMedicaForm.getPresentaHojaNeuro())
				|| !UtilidadTexto.isEmpty(ordenMedicaForm.getObservacionesHojaNeuroNueva())
				|| ordenMedicaForm.getFinalizadaHojaNeuro() != ordenMedicaForm.getFinalizadaHojaNeuro())
			return true;
		else
			return false;
	}
	

	/**
	 * Método que verifica si insertaron o modificaron datos en la sección
	 * dieta
	 * 
	 * @param ordenMedicaForm
	 * @return entero dependiendo de lo que cambió 0=>no cambió nada, 1
	 *         cambió nutrición oral, 2 cambió Mezcla parenteral, 3 cambió
	 *         oral y parenteral, 4 cuando finalizar dieta sólo inserta orden
	 *         dieta
	 */

	public int verificarInsertaronDatosDieta(OrdenMedicaForm ordenMedicaForm) {
		Vector codigos = (Vector) ordenMedicaForm.getMapa("codigosNutOral");
		Vector codigosOtros = (Vector) ordenMedicaForm.getMapa("codigosOtroNutOral");

		logger.info("VIA ORAL->"+ordenMedicaForm.isNutricionOral() +" ES IGUAL A  "+ ordenMedicaForm.isTempNutricionOral()+"\n");
		logger.info("VIA PARENTERAL->"+ordenMedicaForm.isNutricionParenteral()+ " ES IGUAL A  "+ ordenMedicaForm.isTempNutricionParenteral()+"\n");
		logger.info("FINALIZAR DIETA->"+ordenMedicaForm.isFinalizarDieta() +" ES IGUAL A  "+ ordenMedicaForm.isTempFinalizarDieta()+"\n");
		logger.info("OTRO NUT ORAL->"+ordenMedicaForm.getOtroNutORal() +" ES IGUAL A  "+ ordenMedicaForm.getTempOtroNutOral()+"\n");
		logger.info("VOLUMEN TOTAL->"+ordenMedicaForm.getVolumenTotal()+"\n");

		int cambio = 0;
		boolean nutOral = false;

		// Se le quita la validacion: ordenMedicaForm.isNutricionOral() !=
		// ordenMedicaForm.isTempNutricionOral() ||
		if (!ordenMedicaForm.getOtroNutORal().equals(ordenMedicaForm.getTempOtroNutOral())) {
			logger.info("PASO POR AQUI A!!!!");
			cambio = 1;
			nutOral = true;
		}

		if (!nutOral && ordenMedicaForm.isNutricionOral()) {
			// ------------------Tipos de nutrición oral
			// parametrizados------------------------------------//
			if (codigos != null) {
				for (int i = 0; i < codigos.size(); i++) {
					logger.info("codigos: "+codigos.get(i).toString());
					int tipoNutOral = Integer.parseInt(codigos.elementAt(i)+ "");
					logger.info("tipo Nut Oral: "+tipoNutOral);
					String tipoNutricion = (String) ordenMedicaForm.getMapa("tipoNut_" + tipoNutOral);
					logger.info("tipo nutricion oral: "+tipoNutricion);
					
					if (tipoNutricion.equals("") || tipoNutricion.equals("0") || tipoNutricion == null)
						tipoNutricion = "";

					String tipoNutricionTemp = (String) ordenMedicaForm.getTempMapa("tipoNut_" + tipoNutOral);
					logger.info("tipo nutricion oral tmp: "+tipoNutricionTemp);
					
					if (tipoNutricionTemp != null) {
						if (tipoNutricionTemp.equals("")|| tipoNutricionTemp.equals("0"))
							tipoNutricionTemp = "";
					} else
						tipoNutricionTemp = "";

					 logger.info("tipoNutricion->"+tipoNutricion+"  ES IGUAL A tipoNutricionTemp->"+tipoNutricionTemp+"\n");
					if (tipoNutricion != null && tipoNutricionTemp != null && !tipoNutricion.equals(tipoNutricionTemp)) {
						cambio = 1;
						logger.info("PASO POR AQUI B!!!!");
						nutOral = true;
						break;
					}
				}// for
			}// if codigos != null

			// ------------------Otros tipos de nutrición oral
			// ingresados------------------------------------//
			if (codigosOtros != null) {
				for (int i = 0; i < codigosOtros.size(); i++) {
					logger.info("codigos otros: "+codigosOtros.get(i).toString());
					int tipoNutOralOtro = Integer.parseInt(codigosOtros.elementAt(i)+ "");
					String tipoNutricionOtro = (String) ordenMedicaForm.getMapa("tipoNutOtro_" + tipoNutOralOtro);
					logger.info("tipo nutriocn otro: "+tipoNutricionOtro);
					
					if (tipoNutricionOtro.equals("")|| tipoNutricionOtro.equals("0")|| tipoNutricionOtro == null)
						tipoNutricionOtro = "";

					String tipoNutricionOtroTemp = (String) ordenMedicaForm.getTempMapa("tipoNutOtro_" + tipoNutOralOtro);
					logger.info("tipo nutriocn otro Tmp: "+tipoNutricionOtroTemp);
					
					if (tipoNutricionOtroTemp != null) {
						if (tipoNutricionOtroTemp.equals("")|| tipoNutricionOtroTemp.equals("0"))
							tipoNutricionOtroTemp = "";
					} else
						tipoNutricionOtroTemp = "";

					 logger.info("tipoNutricionOtro->"+tipoNutricionOtro+"  ES IGUAL A tipoNutricionOtroTemp->"+tipoNutricionOtroTemp+"\n");
					 
					if (tipoNutricionOtro != null&& tipoNutricionOtroTemp != null && !tipoNutricionOtro.equals(tipoNutricionOtroTemp)) {
						cambio = 1;
						logger.info("PASO POR AQUI C!!!!");
						break;
					}
				} // for
			}// if codigosotros != null

		}// if !nutOral y nutricionOral=true

		if (!ordenMedicaForm.getDosificacion().toString().equals("")|| ordenMedicaForm.isNutricionParenteral()) {
			cambio += 2;
		}
		// PREGUNTO POR LOS CUALQUIERA DE LOS CHECK BOX DE SUPENDER Y SUPENDIDO
		// POR ENFERMERIA
		if (ordenMedicaForm.isFinalizarDieta() != ordenMedicaForm.isTempFinalizarDieta()
				|| ordenMedicaForm.isFinalizarDietaEnfermeria() != ordenMedicaForm.isFinalizarDietaEnfermeria() 
				&& cambio == 0)
			cambio = 4;
		// VERIFICA POR EL CAMPO DE OBSERVACIONES GENERALES
		if (cambio <= 0 && !ordenMedicaForm.getObservacionesGeneralesNueva().trim().equals(""))
			cambio = 6;

		logger.info("variable cambio: "+cambio);
		return cambio;
	}

	/**
	 * Método que verifica si insertaron o modificaron datos en la orden
	 * médica
	 * 
	 * @param ordenMedicaForm
	 * @return true si ingresaron datos nuevos sino retorna false
	 */

	public boolean verificarInsertaronDatosOrden(OrdenMedicaForm ordenMedicaForm) {
		// logger.info("FECHA ORDEN->"+ordenMedicaForm.getFechaOrden()+
		// " == FECHA TEMP->"+ordenMedicaForm.getFechaTempOrden());
		// logger.info("HORA ORDEN->"+ordenMedicaForm.getHoraOrden()+
		// " == HORA TEMP->"+ordenMedicaForm.getHoraTempOrden());
		// logger.info("DESCRIPCION SOPORTE->"+ordenMedicaForm.
		// getDescripcionSoporteNueva());
		// logger.info("DESCRIPCION DIETA->"+ordenMedicaForm.
		// getDescripcionDietaNueva());
		// logger.info("OBSERVACIONES GRALES->"+ordenMedicaForm.
		// getObservacionesGeneralesNueva()+"\n");

		if (!ordenMedicaForm.getFechaOrden().equals(
				ordenMedicaForm.getFechaTempOrden())
				|| !ordenMedicaForm.getHoraOrden().equals(
						ordenMedicaForm.getHoraTempOrden())
				|| !ordenMedicaForm.getDescripcionSoporteNueva().equals("")
				|| !ordenMedicaForm.getDescripcionDietaNueva().equals("")
				|| !ordenMedicaForm.getObservacionesGeneralesNueva().equals(""))
			return true;
		else {
			//logger.info("VERIFICAR SOPORTE->"+verificarInsertaronDatosSoporte(
			// ordenMedicaForm));
			// logger.info("VERIFICAR DIETE->"+verificarInsertaronDatosDieta(
			// ordenMedicaForm));
			// logger.info("VERIFICAR CUIDADO->"+
			// verificarInsertaronDatosCuidadosEnf(ordenMedicaForm));

			if ((ordenMedicaForm.getTipoMonitoreo() != 0)
					&& (ordenMedicaForm.getTipoMonitoreo() != ordenMedicaForm
							.getTipoMonitoreoTemp())
					|| verificarInsertaronDatosSoporte(ordenMedicaForm)
					|| verificarInsertaronDatosDieta(ordenMedicaForm) != 0
					|| verificarInsertaronDatosCuidadosEnf(ordenMedicaForm))
				return true;
			else
				return false;
		}// else

	}

	/**
	 * Método que verifica si insertaron datos en la sección de cuidados de
	 * enfermería
	 * 
	 * @param ordenMedicaForm
	 * @return true si ingresaron datos nuevos sino retorna false
	 */

	public boolean verificarInsertaronDatosCuidadosEnf(
			OrdenMedicaForm ordenMedicaForm) {
		Vector codigosCuidadoEnf = (Vector) ordenMedicaForm
				.getMapa("codigosCuidadoEnf");

		if (codigosCuidadoEnf != null) {
			for (int i = 0; i < codigosCuidadoEnf.size(); i++) {
				// ------El codigo del tipo de cuidado de enfermeria
				int tipoCuidado = Integer.parseInt(codigosCuidadoEnf
						.elementAt(i)
						+ "");

				// ------Extraer
				String presenta = (String) ordenMedicaForm.getMapa("presenta_"
						+ tipoCuidado);
				String descripcion = (String) ordenMedicaForm
						.getMapa("cuidadosEnfDes_" + tipoCuidado);

				// -Verificar que se haya seleccionado el tipo de cuidado
				if (UtilidadCadena.noEsVacio(presenta)
						|| UtilidadCadena.noEsVacio(descripcion)) {
					logger.info("\n tipoCuidado -->" + tipoCuidado
							+ "\n presenta " + presenta);
					return true;
				}
			}
		}// if codigosCuidadoEnf != null

		// --------Verificar Otro Detalle cuidados enfermeria
		String presenta = (String) ordenMedicaForm.getMapa("presentaOtro");
		if (UtilidadCadena.noEsVacio(presenta)
				&& UtilidadCadena
						.noEsVacio(ordenMedicaForm.getOtroCuidadoEnf())) {
			logger.info("\n presenta " + presenta + "   otro cuidado ->"
					+ ordenMedicaForm.getOtroCuidadoEnf());
			return true;
		}
				
		return false;

	}

	/**
	 * Método en que se cierra la conexión (Buen manejo recursos), usado ante
	 * todo al momento de hacer un forward
	 * 
	 * @param con
	 *            Conexión con la fuente de datos
	 * @throws SQLException
	 */
	public void cerrarConexion(Connection con) throws SQLException {
		if (con != null && !con.isClosed()) {
			UtilidadBD.closeConnection(con);
		}
	}

	/**
	 * Método para hacer un ingreso de la solicitud básica en la base de datos
	 * 
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @return
	 */
	private int insertarSolicitudBasica(Connection con, OrdenMedicaForm forma,
			PersonaBasica paciente, UsuarioBasico usuario) {
		Solicitud objectSolicitud = new Solicitud();
		int numeroSolicitudInsertado = 0;
		try {
			objectSolicitud.clean();
			objectSolicitud.setFechaSolicitud(forma.getFechaOrden());
			objectSolicitud.setHoraSolicitud(forma.getHoraOrden());
			objectSolicitud.setTipoSolicitud(new InfoDatosInt(
					ConstantesBD.codigoTipoSolicitudMedicamentos));
			//objectSolicitud.setNumeroAutorizacion("");
			objectSolicitud.setEspecialidadSolicitante(new InfoDatosInt(-1));
			objectSolicitud.setOcupacionSolicitado(new InfoDatosInt(
					ConstantesBD.codigoOcupacionMedicaNinguna));

			objectSolicitud.setCentroCostoSolicitante(new InfoDatosInt(paciente
					.getCodigoArea()));
			objectSolicitud.setCodigoMedicoSolicitante(usuario
					.getCodigoPersona());

			objectSolicitud.setCentroCostoSolicitado(new InfoDatosInt(forma
					.getFarmacia()));
			objectSolicitud.setCodigoCuenta(paciente.getCodigoCuenta());

			objectSolicitud.setVaAEpicrisis(true);
			objectSolicitud.setUrgente(false);
			objectSolicitud.setEstadoHistoriaClinica(new InfoDatosInt(
					ConstantesBD.codigoEstadoHCSolicitada));
			try {
				numeroSolicitudInsertado = objectSolicitud
						.insertarSolicitudGeneralTransaccional(con,
								ConstantesBD.continuarTransaccion);
			} catch (SQLException sqle) {
				logger
						.warn("Error en la transaccion del insert en la solicitud básica "
								+ sqle);
				return 0;
			}
		} catch (Exception e) {
			logger.error("Error general en la inserción de la solicitud base "
					+ e);
			return 0;
		}
		return numeroSolicitudInsertado;
	}

	/**
	 * Generar justificacion de los articulo de la mezcla
	 * @param ordenMedicaForm
	 * @param con
	 * @param numeroSolicitud
	 * @param paciente
	 * @param usuario
	 * @param request
	 */
	private void generarJustificacionNoPosMezclas(OrdenMedicaForm ordenMedicaForm, Connection con, int numeroSolicitud, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) {
		
		// ------FORMATO JUSTIFICACION NO POS
		FormatoJustArtNopos fjan = new FormatoJustArtNopos();

		// Insercion para articulos predeterminados para mezclas
		Iterator iterador = ordenMedicaForm.getListadoNutParent().iterator();
		while (iterador.hasNext()) {
			HashMap elemento = (HashMap) iterador.next();

			if (elemento.get("espos").toString().equals("" + ConstantesBD.acronimoFalseCorto)) {
				if (UtilidadesFacturacion.requiereJustificacioArt(con, paciente.getCodigoConvenio(), Integer.parseInt(elemento.get("codigo")+ ""))) {
					if (!UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario,true))
					{
						logger
								.info("\n***********************************************************************\n"
										+ "*[No se genera justificacion segun validacion profesional de la salud]*\n"
										+ "***********************************************************************\n");
						//UtilidadJustificacionPendienteArtServ.insertarJusNP(con
						// , numeroSolicitud,
						// Integer.parseInt(elemento.get("codigo")+""),
						// usuario.getLoginUsuario(), true);
					} 
					else if(UtilidadInventarios.esMedicamento(con, Utilidades.convertirAEntero(elemento.get("codigo")+""), usuario.getCodigoInstitucionInt())){
						if (Utilidades.convertirAEntero(ordenMedicaForm.getMapa("tipoParent_" + elemento.get("codigo")).toString()) > 0
										&& ordenMedicaForm.getJustificacionMap().get(elemento.get("codigo") + "_pendiente").equals("1")) {
							// frecuencia estaba en -1 int
	
							
								fjan.insertarJustificacion(con, numeroSolicitud,
										ConstantesBD.codigoNuncaValido, ordenMedicaForm
												.getJustificacionMap(), ordenMedicaForm
												.getMedicamentosNoPosMap(),
										ordenMedicaForm.getMedicamentosPosMap(),
										ordenMedicaForm.getSustitutosNoPosMap(),
										ordenMedicaForm.getDiagnosticosDefinitivos(),
										Integer.parseInt(elemento.get("codigo") + ""),
										usuario.getCodigoInstitucionInt(), "",
										ConstantesBD.continuarTransaccion, Integer
												.parseInt(elemento.get("codigo") + ""),
										"", ordenMedicaForm.getMapa("tipoParent_"
												+ elemento.get("codigo"))
												+ "", "", -1, "", "", Utilidades
												.convertirADouble(ordenMedicaForm
														.getMapa("tipoParent_"
																+ elemento
																		.get("codigo"))
														+ ""), "-1", usuario
												.getLoginUsuario());
							}
						} else {
							// Guardar Justificacion No Pos de Articulo diferente a medicamento
							ArrayList<DtoParamJusNoPos> justificacion = new ArrayList<DtoParamJusNoPos>();
							justificacion = (ArrayList<DtoParamJusNoPos>)request.getSession().getAttribute("JUSTIFICACION");
							for(int jus=0; jus<justificacion.size(); jus++){
				    			if(justificacion.get(jus).getCodigoArticulo().equals(elemento.get("codigo")+"")){
				    				justificacion.get(jus).setSolicitud(numeroSolicitud+"");
				    				UtilidadesJustificacionNoPos.guardarJustificacion(con, justificacion.get(jus), ConstantesIntegridadDominio.acronimoInsumo, usuario);
				    			}
				    		}
						}
					}
				}
			}
		
		// Insercion para nuevos articulos
		for (int i = 0; i < ordenMedicaForm.getNumeroElementos(); i++) {
			if (ordenMedicaForm.getMapa("espos_" + i).toString()
					.equals("false")) {
				if (UtilidadesFacturacion.requiereJustificacioArt(con, paciente
						.getCodigoConvenio(), Integer.parseInt(ordenMedicaForm
						.getMapa("art_" + i)
						+ ""))) {
					if (!UtilidadesHistoriaClinica
							.validarEspecialidadProfesionalSalud(con, usuario,
									true))// segun validacion medico
											// especialista queda pendiente=0
					{
						logger
								.info("\n***********************************************************************\n"
										+ "*[No se genera justificacion segun validacion profesional de la salud]*\n"
										+ "***********************************************************************\n");
						//UtilidadJustificacionPendienteArtServ.insertarJusNP(con
						// , numeroSolicitud,
						// Integer.parseInt(ordenMedicaForm.getMapa
						// ("art_"+i)+""), usuario.getLoginUsuario(), true);
					} else if(UtilidadInventarios.esMedicamento(con, Utilidades.convertirAEntero(ordenMedicaForm.getMapa("art_" + i)+""), usuario.getCodigoInstitucionInt())){
						if (Utilidades.convertirAEntero(ordenMedicaForm.getMapa("tipoParent_"+ ordenMedicaForm.getMapa("art_" + i).toString()).toString()) > 0
							&& ordenMedicaForm.getJustificacionMap().get(ordenMedicaForm.getMapa("art_" + i)+ "_pendiente").equals("1")) {
						// frecuencia estaba en -1 int
							fjan.insertarJustificacion(con, numeroSolicitud,
									ConstantesBD.codigoNuncaValido, ordenMedicaForm
											.getJustificacionMap(), ordenMedicaForm
											.getMedicamentosNoPosMap(),
									ordenMedicaForm.getMedicamentosPosMap(),
									ordenMedicaForm.getSustitutosNoPosMap(),
									ordenMedicaForm.getDiagnosticosDefinitivos(),
									Integer.parseInt(ordenMedicaForm.getMapa("art_"
											+ i)
											+ ""), usuario
											.getCodigoInstitucionInt(), "",
									ConstantesBD.continuarTransaccion, Integer
											.parseInt(ordenMedicaForm.getMapa("art_" + i)
													+ ""), "", ordenMedicaForm
											.getMapa("tipoParent_"
													+ ordenMedicaForm
															.getMapa("art_" + i))
											+ "", "", -1, "", "", Utilidades
											.convertirADouble(ordenMedicaForm
													.getMapa("tipoParent_"
															+ ordenMedicaForm
																	.getMapa("art_"
																			+ i))
													+ ""), "-1", usuario
											.getLoginUsuario());
						}
					} else {
						// Guardar Justificacion No Pos de Articulo diferente a medicamento
						ArrayList<DtoParamJusNoPos> justificacion = new ArrayList<DtoParamJusNoPos>();
						justificacion = (ArrayList<DtoParamJusNoPos>)request.getSession().getAttribute("JUSTIFICACION");
						for(int jus=0; jus<justificacion.size(); jus++){
			    			if(justificacion.get(jus).getCodigoArticulo().equals(ordenMedicaForm.getMapa("art_"+i)+"")){
			    				justificacion.get(jus).setSolicitud(numeroSolicitud+"");
			    				UtilidadesJustificacionNoPos.guardarJustificacion(con, justificacion.get(jus), ConstantesIntegridadDominio.acronimoInsumo, usuario);
			    			}
			    		}
					}	
				}
			}
		}
		
		request.removeAttribute("JUSTIFICACION");
	}

	/**
	 * General la interfaz de Nutricion Oral
	 * 
	 * @param OrdenMedicaForm
	 *            ordenMedicaForm
	 * @param Connection
	 *            con
	 * @param int numeroSolicitud
	 * @param PersonaBasica
	 *            paciente
	 * @param UsuarioBasico
	 *            usuario
	 * @param OrdenMedica
	 *            mundoOrdenMedica
	 * @param ActionMapping
	 *            mapping
	 * @param HttpServletRequest
	 *            request
	 * */
	private ActionForward interfazNutricionOral(
			OrdenMedicaForm ordenMedicaForm, Connection con,
			int numeroSolicitud, int codigoEncabezadoOrdenDieta,
			PersonaBasica paciente, UsuarioBasico usuario,
			OrdenMedica mundoOrdenMedica, ActionMapping mapping,
			HttpServletRequest request) {
		//-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
		// *
		// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*
		// --------------------------------------------------------- I N T E R F
		// A Z N U T R I C I O N
		// -------------------------------------------------------
		// CAPTURO EL PARAMETRO GENERAL DE INTERFAZ NUTRICION
		ordenMedicaForm.setInterfazNutricion(mundoOrdenMedica
				.consultarParametroInterfazNutricion(con));
		logger.info("\n\n\n\n PARAMETRO GENERAL INTERFAZ NUTRICION ->"
				+ ordenMedicaForm.getInterfazNutricion() + "<-");
		// VALIDO EL PARAMETRO GENERAL INTERFAZ NUTRICION PARA ENVIAR
		// INFORMACION AL SISTEMA EXTERNO
		if (ordenMedicaForm.getInterfazNutricion().equals(
				ConstantesBD.acronimoSi)) {
			// Se instancia el DTO
			DtoInterfazNutricion dto = new DtoInterfazNutricion();
			// Se resetea el HashMap
			ordenMedicaForm.setNutricionOralMap(new HashMap());
			ordenMedicaForm.setNutricionOralMap(mundoOrdenMedica
					.tiposNutricionOralActivo(con, paciente.getCodigoCuenta()));
			int registrosNutricionOral = Utilidades
					.convertirAEntero(ordenMedicaForm.getNutricionOralMap()
							.get("numRegistros").toString());
			logger.info("MAPA DE TIPOS NUTRICION ORAL ->"
					+ ordenMedicaForm.getNutricionOralMap());
			// Cargo el DTO
			dto.setIngreso(paciente.getConsecutivoIngreso() + "");
			dto.setNumhis(paciente.getHistoriaClinica());
			dto.setCoddie(codigoEncabezadoOrdenDieta + ""); // este codigo si es
															// el nuevo campo de
															// la tabla
															// tipo_nutricion_oral
			// String descripcionDieta =
			// mundoOrdenMedica.consultarDescripcionDieta(con,
			// paciente.getCodigoCuenta());
			String descripcionDieta = ordenMedicaForm
					.getDescripcionDietaNueva();
			String descrip1 = "";
			String descrip2 = "";
			int size = descripcionDieta.length();
			if (size > 220) {
				descrip1 = descripcionDieta.substring(0, 220);
				descrip2 = descripcionDieta.substring(221, size);
			} else {
				descrip1 = descripcionDieta.substring(0, size);
			}
			logger.info("\n\n\n DESCRIPCION 1 ->" + descrip1);
			logger.info("\n\n \n DESCRIPCION 2 ->" + descrip2);

			dto.setDescr1(descrip1);
			dto.setDescr2(descrip2);
			dto.setRegusu(usuario.getLoginUsuario());
			dto.setEstreg(ConstantesBDInterfaz.codigoEstadoPacienteNoProcesado
					+ "");
			// CONSULTAR LA FECHA DE LA DIETA
			String fechaDie = mundoOrdenMedica.consultarFechaDieta(con,
					codigoEncabezadoOrdenDieta);
			String fechasplit[] = fechaDie.split("-");
			fechaDie = fechasplit[0] + fechasplit[1] + fechasplit[2];
			dto.setFecdie(fechaDie);
			// CONSULTAR LA HORA DE LA DIETA
			String horaDie = mundoOrdenMedica.consultarHoraDieta(con,
					codigoEncabezadoOrdenDieta);
			String horasplit[] = horaDie.split(":");
			horaDie = horasplit[0] + horasplit[1] + horasplit[2];
			dto.setHordie(horaDie);
			// CONSULTAR LA FECHA DE GRABACION
			String FechaEnv = mundoOrdenMedica.consultarFechaGrabacion(con,
					codigoEncabezadoOrdenDieta);
			fechasplit = FechaEnv.split("-");
			FechaEnv = fechasplit[0] + fechasplit[1] + fechasplit[2];
			dto.setFecenv(FechaEnv);
			// CONSULTAR LA HORA DE GRABACION
			String horaEnv = mundoOrdenMedica.consultarHoraGrabacion(con,
					codigoEncabezadoOrdenDieta);
			horaEnv = horaEnv.substring(0, 8);
			horasplit = horaEnv.split(":");
			horaEnv = horasplit[0] + horasplit[1] + horasplit[2];
			dto.setHorenv(horaEnv);
			// Cambios Adicion campo ax_nutri idvia. Agosto 25 2008 aesilva
			dto.setViaing(paciente.getCodigoUltimaViaIngreso() + "");
			
			dto.setTipopac(paciente.getCodigoTipoPaciente());
			
			if (ordenMedicaForm.isFinalizarDieta() == true) {
				// CUANDO FINALIZAR DIETA ES TRUE SIGNIFICA QUE ESTA SUSPENDIDA
				dto.setEstdie(ConstantesBDInterfaz.estadoDietaSuspendido + "");
			} else {
				dto.setEstdie(ConstantesBDInterfaz.estadoDietaActivo + "");
			}

			ResultadoBoolean inserto;

			
			// VALIDO VIA DE INGRESO HOSPITALIZACION
			// Se retira la restriccion de validar que exista cama, &&
			// paciente.getCodigoCama()>0 Enero 27 de 2009
			if (paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoHospitalizacion) {
				logger
						.info("----------------> PACIENTE POR VIA INGRESO HOSPITALIZACION <-------------");
				logger
						.info("----------------------> SE ENVIA INFORMACION A SHAIO <-------------------");

				dto
						.setIdvia(ConstantesBDInterfaz.interfazNutricionHospitalizacion);
				dto.setSecama(mundoOrdenMedica.consultarPisoCama(con, paciente
						.getCodigoCama()));
				dto.setNucama(mundoOrdenMedica.consultarNumeroCama(con,
						paciente.getCodigoCama()));
				dto.setPacvip(" ");

				// Recorro todos los tipos de Dieta para enviar registro por
				// cada uno
				for (int x = 0; x < registrosNutricionOral; x++) {
					dto.setCoddie(ordenMedicaForm.getNutricionOralMap().get(
							"codigointerfaz_" + x)
							+ "");
					UtilidadBDInterfaz interfaz = new UtilidadBDInterfaz();
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					//Modificado por anexo 779
					inserto = interfaz.insertarInterfazNutricion(dto, paciente.getTipoInstitucion(),false); // SE INSERTA EN AS400 SHAIO

					if (!inserto.isTrue()) {
						ArrayList alertas= new ArrayList();
						alertas.add(inserto.getDescripcion());
						ordenMedicaForm.setMensajes(alertas);
					}
					//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
				}
			} else // VALIDO VIA INGRESO URGENCIAS EN OBSERVACION
			// Se retira la restriccion de validar que exista cama, &&
			// paciente.getCodigoCama()>0 Enero 27 de 2009
			if (paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoUrgencias) {
				logger
						.info("-----------> PACIENTE POR VIA INGRESO URGENCIAS CON OBSERVACION <--------");
				logger
						.info("----------------------> SE ENVIA INFORMACION A SHAIO <-------------------");

				dto
						.setIdvia(ConstantesBDInterfaz.interfazNutricionUrgenciasObserva);
				dto.setSecama(mundoOrdenMedica.consultarPisoCama(con, paciente
						.getCodigoCama()));
				dto.setNucama(mundoOrdenMedica.consultarNumeroCama(con,
						paciente.getCodigoCama()));

				String vip = mundoOrdenMedica.consultarConvenioVip(con,
						paciente.getCodigoConvenio());

				if (vip.equals(ConstantesBD.acronimoSi))
					dto.setPacvip(ConstantesBDInterfaz.vipConvenioSi);
				if (vip.equals(ConstantesBD.acronimoNo))
					dto.setPacvip(ConstantesBDInterfaz.vipConvenioNo);

				// Recorro todos los tipos de Dieta para enviar registro por
				// cada uno
				for (int x = 0; x < registrosNutricionOral; x++) {
					dto.setCoddie(ordenMedicaForm.getNutricionOralMap().get(
							"codigointerfaz_" + x)
							+ "");
					UtilidadBDInterfaz interfaz = new UtilidadBDInterfaz();

								
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					//Modificado por anexo 779
					inserto = interfaz.insertarInterfazNutricion(dto, paciente.getTipoInstitucion(),false); // SE INSERTA EN AS400 SHAIO

					if (!inserto.isTrue()) {
						ArrayList alertas= new ArrayList();
						alertas.add(inserto.getDescripcion());
						ordenMedicaForm.setMensajes(alertas);
					}
					//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					
				}
			} else {
				logger
						.info("--------------------------------------------------------------------------------");
				logger
						.info("--EL PACIENTE NO SE ENCUENTRA EN LA VIA DE INGRESO REQUERIDA PARA LA INTERFAZ---");
				logger
						.info("--------------------------------------------------------------------------------");
			}
		}
		//-*-*-*-*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
		// *
		// -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
		// *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*

		return null;
	}
}