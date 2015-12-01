package com.princetonsa.action.odontologia;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

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
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;
import util.consultaExterna.UtilidadesConsultaExterna;
import util.facturacion.InfoResponsableCobertura;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.odontologia.InfoTarifaServicioPresupuesto;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.actionform.odontologia.AgendaOdontologicaForm;
import com.princetonsa.dto.comun.DtoResultado;
import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.princetonsa.dto.manejoPaciente.DtoInformacionBasicaIngresoPaciente;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.dto.odontologia.DtoBusquedaAgendaRango;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.princetonsa.dto.odontologia.DtoSuperficiesPorPrograma;
import com.princetonsa.dto.odontologia.DtoUnidadAgendaServCitaOdonto;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosOdon;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.manejoPaciente.Consultorios;
import com.princetonsa.mundo.odontologia.AgendaOdontologica;
import com.princetonsa.mundo.odontologia.CitaOdontologica;
import com.princetonsa.mundo.odontologia.IngresoPacienteOdontologia;
import com.princetonsa.mundo.odontologia.PlanTratamiento;
import com.princetonsa.mundo.odontologia.PresupuestoOdontologico;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.pdf.CitasOdontologicasPdf;
import com.princetonsa.sort.odontologia.SortBeneficiarios;
import com.princetonsa.sort.odontologia.SortCitasOdontologicas;
import com.princetonsa.sort.odontologia.SortServiciosOdontologicos;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.generadorReporte.odontologia.citaOdontologica.ImpresionCitaOdontologica;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.BonosConvIngPac;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.CitasAsociadasAProgramada;
import com.servinte.axioma.orm.CitasOdontologicasHome;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;
import com.servinte.axioma.orm.ConveniosIngresoPacienteHome;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.Personas;
import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.orm.UnidadesConsulta;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.fabrica.administracion.EspecialidadServicioFabrica;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.agendaOdontologica.AgendaOdontologicaFabricaServicio;
import com.servinte.axioma.servicio.fabrica.odontologia.unidadagendaserviciotipocitaodonto.UnidadAgendaServTipoCitaOdonFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.ICentroAtencionServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IEspecialidadServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IPersonasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IContratoServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IIngresosServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ISubCuentasServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica.IAgendaOdontologicaServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.unidadagendaserviciotipocitaodonto.IUnidadAgendaServTipoCitaOdontoServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.unidadagendaserviciotipocitaodonto.IUnidadesConsultaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITiposIdentificacionServicio;


@SuppressWarnings({ "deprecation","unchecked", "static-access" })
public class AgendaOdontologicaAction extends Action
{
	IAgendaOdontologicaServicio agendaOdontologicaServicio = AgendaOdontologicaFabricaServicio.crearAgendaOdontologicaServicio();
	IContratoServicio contratoServicio = FacturacionServicioFabrica.crearContratoServicio();
	ISubCuentasServicio subCuentasServicio = ManejoPacienteServicioFabrica.crearSubCuentasServicio();
	
	
	@Override
	public ActionForward execute(	ActionMapping mapping, 
									ActionForm form,
									HttpServletRequest request, 
									HttpServletResponse response)throws Exception 
									{
		Connection con=null;
		try{
			if (form instanceof AgendaOdontologicaForm)
			{
				UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
				PersonaBasica persona = (PersonaBasica) request.getSession().getAttribute("pacienteActivo");
				InstitucionBasica institucionBasica=(InstitucionBasica)request.getSession().getAttribute("institucionBasica");

				AgendaOdontologicaForm forma = (AgendaOdontologicaForm) form;
				AgendaOdontologica mundo = new AgendaOdontologica();
				CitaOdontologica mundoCita = new CitaOdontologica();
				String estado=forma.getEstado();

				Log4JManager.info("estado-->" + estado);

				if (estado==null) {

					forma.reset();
					Log4JManager.error("Estado no valido dentro del flujo de Generación de Agenda Odontólogica (null) ");
					request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}

				//*******************************ESTADOS ANTIGUOS****************************************************
				/**
				 * estado que determinan el flujo de agenda odontológica
				 */
				else if(estado.equals("empezar")){

					con=UtilidadBD.abrirConexion();
					forma.reset();
					forma.setNumDiasAntFechaActual(ValoresPorDefecto.getNumDiasAntFActualAgendaOd(usuario.getCodigoInstitucionInt()));
					forma.setMultiploMinGenCitas(ValoresPorDefecto.getMultiploMinGeneracionCita(usuario.getCodigoInstitucionInt()));
					forma.setValidaPresupuestoContratado(ValoresPorDefecto.getValidaPresupuestoOdoContratado(usuario.getCodigoInstitucionInt()));
					forma.setUtilProgOdonPlanTra(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()));
					//forma.setUtilProgOdonPlanTra(ConstantesBD.acronimoSi);
					forma.setMinutosEsperaAsgCitOdoCad(ValoresPorDefecto.getMinutosEsperaAsignarCitasOdoCaducadas(usuario.getCodigoInstitucionInt()));
					forma.setCentroAtencion(usuario.getCodigoCentroAtencion());
					forma.setCodigoActividadAutorizacion(ConstantesBD.codigoNuncaValido);
					cargarDatosIniciales(con, forma, usuario);
					UtilidadBD.closeConnection(con);		


					return mapping.findForward("principal");


				}else if(estado.equals("ingresarPacienteOdontologia")){

					return mapping.findForward("ingresarPacienteOdontologia");

				}else if(estado.equals("imprimirCitaOdo")){
					return accionImprimirCitaOdontologica(mapping, usuario, request, forma, persona);
				}

				//Estado que inicia el flujo de empezar paciente
				else if (estado.equals("empezarPaciente"))
				{
					return accionEmpezarPaciente(forma,usuario,mapping);
				}
				else if(estado.equals("validarPaciente")||estado.equals("buscar"))
				{
					forma.resetCancelacionCita();
					determinarValidacionIdentificacion(forma, request, usuario.getCodigoInstitucionInt());
					return accionValidarPaciente(forma,usuario,mapping,persona,request,mundo);
				}
				else if (estado.equals("consultarTodasCitas"))
				{
					return accionConsultarTodasCitas(forma,usuario,mapping,mundo);
				}
				else if (estado.equals("iniciarActividad"))
				{
					//forma.setCodigoActividad(ConstantesIntegridadDominio.acronimoTipoFuncionalidadReservar);
					//verificarCondicionesParaReciboCaja(usuario, forma, persona);

					ActionForward forward = accionIniciarActividad(forma, usuario, mapping, request, persona, mundoCita, response);

					/*
					 * Se valida si el usuario en sesion tiene permisos para generar recibos de caja.
					 */
					verificarCondicionesParaReciboCaja(usuario, forma, persona);

					return forward;
				}
				//Estado que se usa para recargar las citas del record de citas en el momento que se termine un proceso en una actividad
				else if (estado.equals("recargarCitas"))
				{
					return accionRecargarCitas(forma,usuario,mapping,mundo,request);
				}
				//Estado para preparar la página de los criterios de búsqueda
				else if (estado.equals("criteriosBusqueda"))
				{
					return accionCriteriosBusqueda(forma,usuario,mapping, persona, request);
				}
				//*************************ESTADOS PARA LA SECCIÓN DE CRITERIOS DE BÚSQUEDA**********************
				else if (estado.equals("recargarPais"))
				{
					con=UtilidadBD.abrirConexion();
					accionCargarPaises(con, forma, usuario, mapping);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("criteriosBusqueda");
				}
				else if (estado.equals("recargarCiudad"))
				{
					con=UtilidadBD.abrirConexion();
					accionCargarCiudades(con, forma, mapping, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("criteriosBusqueda");
				}
				else if (estado.equals("recargarCentroAtencion"))
				{
					con=UtilidadBD.abrirConexion();
					accionCargarCentrosAtencion(con, forma, usuario, mapping);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("criteriosBusqueda");

				}else if (estado.equals("validarBusqueda") || estado.equals("realizarBusqueda")||estado.equals("realizarBusquedaAtras")||estado.equals("realizarBusquedaAdelante"))
				{
					return accionValidarBusqueda(forma,usuario,mapping,request);
				}
				//*****************************************************************************************************************
				//*******************ESTADOS PARA LA SECCION DE AGENDA ODONTOLOGICA**************************************************
				//Estado que se invoca al seleccionar un rango de horas de la agenda
				else if (estado.equals("procesarAgenda"))
				{
					forma.resetDatosCita();
					forma.setCuposExtra(ConstantesBD.acronimoNo);
					//forma.setPermitirCupoExtra(false);

					return accionProcesarAgenda(forma,mapping,usuario,mundo,request,persona, false, response);

				}
				//*******************************************************************************************************************
				//***********************ESTADOS PARA LA ACTIVIDAD DE CONFIRMACIï¿½N DE CITAS************************************
				//Estado usado para mostrar los servicios/artículos incluidos de un servicio de una cita que se estï¿½ confirmando
				else if (estado.equals("mostrarAsociosServicio"))
				{
					return mapping.findForward("asociosServicio");
				}
				//Recarga de una parte del popUp de confirmación de citas al momento de cambiar el tipo de confirmacion
				else if (estado.equals("retazoConfirmacionCita"))
				{
					return mapping.findForward("retazoConfirmacionCita");
				}
				else if (estado.equals("confirmarCita"))
				{
					return accionConfirmacionCita(forma,usuario,mapping,request);
				}
				//*******************ESTADOS PARA LA ACTIVIDAD DE RESERVAR CITAS******************************************
				else if(estado.equals("iniciarReservar"))
				{
					return iniciarProcesoReserva(mapping, request, response, usuario, persona, forma, mundo);
				}
				else if(estado.equals("listarConTomaSer"))
				{
					return mapping.findForward("condicionTomaSer");
				}
				else if(estado.equals("validarReserva"))
				{
					return accionValidarReserva(forma, usuario, mapping, request, mundo, mundoCita, persona);
				}
				else if (estado.equals("guardarReserva"))
				{
					return accionGuardarReserva(forma,usuario,mapping,request,mundo,mundoCita,persona);
				}
				//*********************************************************************************************************
				//********************************************************************************************************

				//******************ESTADOS ANDRES****************************************************************************

				else if(estado.equals("busquedaXRango"))
				{
					con = UtilidadBD.abrirConexion();
					return iniciarParametrosBusquedaXRango(con, mapping, forma, usuario);

				}
				else if (estado.equals("recargarPaisRango"))
				{
					con=UtilidadBD.abrirConexion();

					pasarValoresBusquedaXRangoAForma(forma);

					accionCargarPaises(con, forma, usuario, mapping);

					pasarValoresFormaABusquedaXRango(forma);

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principalXRango");
				}
				else if (estado.equals("recargarCiudadRango"))
				{
					con=UtilidadBD.abrirConexion();

					pasarValoresBusquedaXRangoAForma(forma);

					accionCargarCiudades(con, forma, mapping, usuario);

					pasarValoresFormaABusquedaXRango(forma);

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principalXRango");
				}
				else if (estado.equals("recargarCentroAtencionRango"))
				{
					con=UtilidadBD.abrirConexion();

					pasarValoresBusquedaXRangoAForma(forma);

					accionCargarCentrosAtencion(con, forma, usuario, mapping);

					pasarValoresFormaABusquedaXRango(forma);

					UtilidadBD.closeConnection(con);
					return mapping.findForward("principalXRango");
				}

				else if (estado.equals("realizarBusquedaRango"))
				{
					con=UtilidadBD.abrirConexion();
					if(request.getParameter("origen")!=null && request.getParameter("origen").equals("AgendaOdontologicaXRango"))
					{
						forma.getDtoBusquedaAgendaRango().setIndicativoConf(forma.getIndicativoConf());
					}
					return accionRealizarBusquedaXRango(con,forma,usuario,mapping,request,mundo);
				}

				else if(estado.equals("abrirAgendXReservaRango"))
				{
					con=UtilidadBD.abrirConexion();
					return accionBuscarAgendaOdon(mapping, con, forma, mundo,usuario);
				}

				else if(estado.equals("ordenarCitasPac"))
				{
					con=UtilidadBD.abrirConexion();
					return accionOrdenarCitasPac(mapping, con, forma);
				}

				else if(estado.equals("redireccionCita"))
				{
					forma.setEstado("realizarBusquedaRango");
					if(!forma.getPaginaLinkSiguienteCita().equals("")){

						response.sendRedirect(forma.getPaginaLinkSiguienteCita());
					}else{					
						return mapping.findForward("principalXRango");					
					}
				}
				//*********************************************************************************************

				//*****************************DUVIAN ********************************************************

				else if (estado.equals("guardarCancelacion"))
				{
					return accionGuardarCancelacion(forma,usuario,mapping,request,mundo,mundoCita,persona);
				}
				//************************************************************************************************************
				else if(estado.equals("iniciarAsignar"))
				{

					return iniciarProcesoAsignacion(mapping, request, response,
							usuario, persona, forma, mundo);

				}
				else if(estado.equals("imprimirFecha"))
				{
					return mapping.findForward("cargarFecha");
				}
				else if(estado.equals("validarAsignacion"))
				{
					accionValidarAsignacion(forma, usuario, mapping, request, mundo, mundoCita, persona);

					//if(forma.isMostrarTarifas()){
					if(forma.isPasoValidacionesGuardar()){
						return accionGuardarAsignacion(forma, usuario, mapping, request, mundo, mundoCita, persona);
					}
					else{
						return mapping.findForward("asignacionCita");
					}
				}
				else if(estado.equals("guardarAsignacion"))
				{
					return accionGuardarAsignacion(forma, usuario, mapping, request, mundo, mundoCita, persona);

				}
				//			else if(estado.equals("confirmarCupoExtra"))
				//			{
				//				forma.setConfirmacionCupoExtra(true);
				//				return mapping.findForward("criteriosBusqueda");
				//				
				//			}menuCitaOdontologica
				//			else if(estado.equals("permitirCupoExtra"))
				//			{
				//				//forma.setConfirmacionCupoExtra(false);
				//				//forma.setPermitirCupoExtra(true);
				//				forma.setCuposExtra(ConstantesBD.acronimoSi);
				//				
				//				if(forma.getCodigoActividad().equals("")){
				//					
				//					return mapping.findForward("menuCitaOdontologica");
				//				
				//				}else{
				//					
				//					if(forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoTipoFuncionalidadAsignar)){
				//						
				//						forma.setEstado("iniciarAsignar");
				//						
				//						//return iniciarProcesoAsignacion(mapping, request, response,
				//								//usuario, persona, forma, mundo);
				//						
				//					}else if(forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoTipoFuncionalidadReservar)
				//							|| forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoReprogramar)){
				//						
				//						forma.setEstado("iniciarReservar");
				//						
				//						//return iniciarProcesoReserva(mapping, request, response, usuario, persona, forma, mundo);
				//					}
				//					
				//					
				//					return mapping.findForward("cerrarPopUpConfirmacionCupoExtra");
				//					
				//					//ActionForward forward = accionProcesarAgenda(forma, mapping, usuario, mundo, request, persona, true, response);
				//					
				//					/*
				//					 * Se valida si el usuario en sesion tiene permisos para generar recibos de caja.
				//					 */
				//					//verificarCondicionesParaReciboCaja(usuario, forma, persona);
				//					
				//					//return forward;
				//				}
				//				
				//			}
				else if(estado.equals("postularUnidadAgenda")){

					con=UtilidadPersistencia.getPersistencia().obtenerConexion();
					forma.setCodCuentaPaciente(persona.getCodigoCuenta());

					accionCargarCentrosAtencion(con, forma, usuario, mapping);

					return mapping.findForward("detalleListadoUnidadesProfesionales");

				}else if("determinarValidacionCampo".equals(estado)){

					determinarValidacionIdentificacion(forma, request, usuario.getCodigoInstitucionInt());

					return mapping.findForward("numIdentificacionPaciente");
				}

				else if(estado.equals("mostrarValoresServicios"))	
				{
					cargarValoresSaldosAbonos(forma, usuario, persona, request);
					return mapping.findForward("asignacionCita");


				} else if(estado.equals("mostrarContratosConvenio"))	
				{
					accionMostrarContratosConvenio(forma);
					cargarValoresSaldosAbonos(forma, usuario, persona, request);
					return mapping.findForward("asignacionCita");	


				} else if(estado.equals("refrescarAsignacion"))
				{	
					accionMostrarContratosConvenio(forma);
					cargarValoresSaldosAbonos(forma, usuario, persona, request);

					if (Utilidades.isEmpty(forma.getListaConveniosPaciente())) {

						forma.setConvenioSeleccionado(true);
					}

					return mapping.findForward("asignacionCita");


				} else if(estado.equals("refrescarReserva")){	

					if(Utilidades.isEmpty(forma.getListaConveniosPaciente())){

						forma.setConvenioSeleccionado(true);
					}

					cargarValoresSaldosAbonos(forma, usuario, persona, request);

					return mapping.findForward("reservacionCita");

				}else if ("recargarSeccionFechas".equals(estado)){

					return mapping.findForward("seccionBusquedaRangoFechas");
				}
				else if(estado.equals("cargarTiposCancelacion")){

					/*
					 *Nota. Cuidado con las modificaciones.
				 se dañan la funciondalidades 
					 */

					accionCargarTiposCancelacion(forma); // lo arregla edgar

					return mapping.findForward("cancelacionCita");

				}else if(estado.equals("cargarProfesionalesSalud")){

					con=UtilidadBD.abrirConexion();

					String unidadAgenda = (String) request.getParameter("unidadAgenda");

					accionCargarProfesionalesSalud(con, forma, usuario, unidadAgenda);

					return mapping.findForward("cargarProfesionalesSalud");

				}else if(estado.equals("cargarOtrosServicios")){

					return mapping.findForward("cargarOtrosServicios");

				}else if(estado.equals("cargarSeccionServicios")){

					return mapping.findForward("cargarSeccionServicios");

				}else if(estado.equals("confirmarOtrosServicios")){

					actualizarServiciosDesdeOtros(forma, usuario, "confirmarOtrosServicios");

					// Actualiza la tarifa de los servicios cuando se confirman Otros Servicios
					cargarValoresSaldosAbonos(forma, usuario, persona, request); 


					return mapping.findForward("cerrarPopup");

				}else if(estado.equals("imprimirCitaOdontologicaPOS")){


					/*
					 * -Al dar clic sobre este botón se debe validar el parámetro General del módulo de Consulta Externa 'Formato Impresión Asignación Citas Odontológicas' 
					A. Si el parámetro 'Formato Impresión Asignación Citas Odontológicas' esta definido como Estandar  el sistema debe hacer llamado al formato de la Asignación de Cita definido en el Anexo FORMATO IMPRESIÓN ESTANDAR ASIGNACIÓN CITA-1133
					-Se maneja hoja tamaño Carta 
					-Se maneja orientación Vertical
					B. Si el parámetro 'Formato Impresión Asignación Citas Odontológicas' esta definido como Sonria  el sistema debe hacer llamado al formato de Impresión de  Cita definido en el Anexo FORMATO POS IMPRESIÓN CITA ODONTOLOGICA-1100
					-Se maneja formato POS 
					-Se maneja orientación Vertical
					 */

					imprimirCitaOdontologicaPOS(usuario, forma, persona, institucionBasica, response);

					return mapping.findForward("cargarFormatoImpresion");


				}

				else if (estado.equals("activarTipoCancelacion"))
				{
					if (!forma.getDtoBusquedaAgendaRango().getEstadoCita().trim().equals(ConstantesIntegridadDominio.acronimoAreprogramar) &&
							!forma.getDtoBusquedaAgendaRango().getEstadoCita().trim().equals(ConstantesIntegridadDominio.acronimoEstadoCancelado)) {
						forma.getDtoBusquedaAgendaRango().setTipoCancelacion("");
					}
					return mapping.findForward("principalXRango");
				}

				else if (estado.equals("consultarHistoricoEstadosCita"))
				{
					return accionConsultarHistoricoEstadosCita(forma,usuario,mapping,mundo);
				}

				else {

					forma.reset();

					Log4JManager.warning("Estado no valido dentro del flujo de generación de Agenda Odontologica -> "+estado );

					request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
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
	 * Método que centraliza todo lo necesario para iniciar el proceso de Reserva
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @param usuario
	 * @param persona
	 * @param forma
	 * @param mundo
	 * @return
	 */
	private ActionForward iniciarProcesoReserva(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response,
			UsuarioBasico usuario, PersonaBasica persona,
			AgendaOdontologicaForm forma, AgendaOdontologica mundo) throws IPSException {
		
		forma.setReprogramar(ConstantesBD.acronimoNo);
		forma.setCodCuentaPaciente(persona.getCodigoCuenta());
		
		forma.setCodigoActividad(ConstantesIntegridadDominio.acronimoTipoFuncionalidadReservar);
		
		ActionForward forward = accionIniciarReservar(forma, usuario, mapping, persona, response, request, mundo);
		
		/*
		 * Se valida si el usuario en sesion tiene permisos para generar recibos de caja.
		 */
		verificarCondicionesParaReciboCaja(usuario, forma, persona);
		
		return forward;
	}

	
	/**
	 * Método que centraliza todo lo necesario para iniciar el proceso de Asignación
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @param usuario
	 * @param persona
	 * @param forma
	 * @param mundo
	 * @return
	 */
	private ActionForward iniciarProcesoAsignacion(ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response,
			UsuarioBasico usuario, PersonaBasica persona,
			AgendaOdontologicaForm forma, AgendaOdontologica mundo) throws IPSException {
		
		
		forma.setMostrarValorTotalCita(true);
		
		accionProcesarAgenda(forma,mapping,usuario,mundo,request,persona, false, response);
		
		forma.setCodigoActividad(ConstantesIntegridadDominio.acronimoTipoFuncionalidadAsignar);
		
		cargarValoresSaldosAbonos(forma, usuario, persona, request);
		
		ActionForward forward = accionIniciarAsignar(forma,usuario,mapping,request,response,persona);
		
		/*
		 * Se valida si el usuario en sesion tiene permisos para generar recibos de caja.
		 */
		verificarCondicionesParaReciboCaja(usuario, forma, persona);
		
		return forward;
	}
	
	
	/**
	 * Método para validar los abonos del paciente contra la tarifa del servicio
	 * @param forma
	 * @throws SQLException
	 */
	private void accionCargarTiposCancelacion(AgendaOdontologicaForm forma)	throws SQLException {
		
		Connection con=HibernateUtil.obtenerConexion();
		
		if(forma.getTipoCancelacion().equals(ConstantesBD.acronimoSi)){
			forma.setListMotivoCancelacion(
									UtilidadesConsultaExterna.obtenerMotivosCancelacion(
									con,	
									ConstantesBD.acronimoSi, 
									ConstantesBD.codigoEstadoCitaCanceladaPaciente+""));
		}
		else if(forma.getTipoCancelacion().equals(ConstantesBD.acronimoNo)){
			forma.setListMotivoCancelacion(
					UtilidadesConsultaExterna.obtenerMotivosCancelacion(
									con,	
									ConstantesBD.acronimoSi, 
									ConstantesBD.codigoEstadoCitaCanceladaInstitucion+""));
												
		}
		
		UtilidadBD.cerrarConexion(con);
	}


	/**
	 * Método para validar los abonos del paciente contra la tarifa del servicio
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param mundo
	 * @param mundoCita
	 * @param persona
	 * @return
	 * @author Juanda
	 */
	private ActionForward accionGuardarAsignacion(AgendaOdontologicaForm forma,
			UsuarioBasico usuario, ActionMapping mapping,
			HttpServletRequest request, AgendaOdontologica mundo,
			CitaOdontologica mundoCita, PersonaBasica persona) throws IPSException {

		ActionErrors errores = new ActionErrors();
		errores = validarAbonosYAnticipos(forma, errores);

		forma.setProcesoExito(ConstantesBD.acronimoNo);
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			forma.setMostrarTarifas(false);
			forma.setPasoValidacionesGuardar(false);
			return mapping.findForward("asignacionCita");

		}
		
		Connection con=UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		String estadoCita="";
		DtoCitaOdontologica cita;
		String horaFinalCita="";
		
		if(forma.getCitasOdonPac()!=null && forma.getPosicionCita()>=0 )
		{
			cita=forma.getCitasOdonPac().get(forma.getPosicionCita());
			estadoCita=cita.getEstado();
			if(!cita.getEstado().equals(ConstantesIntegridadDominio.acronimoProgramado))
			{
				forma.setHoraInicio(cita.getHoraInicio());
				horaFinalCita=cita.getHoraFinal();	
				forma.setCodigoAge(cita.getAgenda());
				forma.setTipoCita(cita.getTipo());
				forma.setFecha(UtilidadFecha.conversionFormatoFechaAAp(forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon().getFecha()));				
			}
			else
			{				
				cita.setTipo(forma.getTipoCita());
				cita.setAgendaOdon(forma.getAgendaSel());
				cita.setAgenda(forma.getAgendaSel().getCodigoPk());
			}
			forma.setCitaAgendaSel(forma.getCitasOdonPac().get(forma.getPosicionCita()));
			forma.setFechaDesplazamiento(forma.getFecha());
		}
		else
		{
			cita=new DtoCitaOdontologica();	
			cita.setTipo(forma.getTipoCita());
			cita.setAgenda(forma.getCodigoAge());
			cita.setCodigoPaciente(persona.getCodigoPersona());
			cita.setAgendaOdon(forma.getAgendaSel());
		}
		cita.setHoraInicio(forma.getHoraInicio());
		cita.setHoraFinal(forma.getHoraFinal());
		
		if(forma.getTotalDuracionServicios()>forma.getDuracionCita())
		{
			cita.setDuracion(forma.getTotalDuracionServicios());
		}
		else
		{
			cita.setDuracion(forma.getDuracionCita());
		}
		cita.setEstado(ConstantesIntegridadDominio.acronimoAsignado);
		cita.setUsuarioModifica(usuario.getLoginUsuario());
		cita.setPorConfirmar(null); // Se debe guardar en null, ya que se los 2 estados que maneja se ingresan solamente en la atención
		cita.setCodigoCentroCosto(forma.getCodCentroCostoXuniAgen());
		
		if(forma.getPosicionCita()>=0 && !forma.getCodigoActividad().equals(""))
		{
			Log4JManager.info("\n\nes flujo modificar! "+forma.getCodigoActividad());
			Log4JManager.info("tipo cita "+cita.getTipo());
						
			if(forma.getCodigoActividad().equals(ConstantesBD.acronimoCambiarServicioCitaOdon))
			{				
				ArrayList<DtoServicioCitaOdontologica> serviciosViejosCitaOdonAbono= new ArrayList<DtoServicioCitaOdontologica>();
				serviciosViejosCitaOdonAbono=CitaOdontologica.consultarServiciosCitaOdontologica(con, forma.getCitasOdonPac().get(forma.getPosicionCita()).getCodigoPk(),usuario.getCodigoInstitucionInt(), persona.getCodigoPersona()); 
				forma.setCitaAgendaSel(forma.getCitasOdonPac().get(forma.getPosicionCita()));

				Connection conAbonos=UtilidadBD.abrirConexion();
				double valorAbonos=CitaOdontologica.obtenerValorCargadoCita(con, serviciosViejosCitaOdonAbono);
				UtilidadBD.closeConnection(conAbonos);

				//elimina cargos
				for(DtoServicioCitaOdontologica elem: serviciosViejosCitaOdonAbono)
				{
					if(elem.getFacturado().equals(ConstantesBD.acronimoNo))
					{
						if(!Solicitud.cambiarEstadosSolicitudStatico(con,
								elem.getNumeroSolicitud(), 
								ConstantesBD.codigoEstadoFAnulada, 
								ConstantesBD.codigoEstadoHCAnulada).isTrue())
						{
							Log4JManager.info("se aborta la transaccion por errores en la anulacion de solicitudes");
							UtilidadBD.abortarTransaccion(con);
							saveErrors(request, errores);
							
							break;
						}
						elem.setEliminarSer(ConstantesBD.acronimoSi);
					}
				}

				Log4JManager.info("\n\n va a Devolver Reserva de Abonos ");
				CitaOdontologica.devolverReservaAbonosCita(con, valorAbonos, usuario, forma.getPaciente().getCodigo(), forma.getCitasOdonPac().get(forma.getPosicionCita()).getCodigoPk(), persona.getCodigoIngreso()); 
				Log4JManager.info("\n\n Sale de  Devolver Reserva de Abonos ");			
				if(!errores.isEmpty())
				{
					Log4JManager.info("se aborta la transaccion por errores en la eliminacion abonos");
					UtilidadBD.abortarTransaccion(con);
					saveErrors(request, errores);
				}
			}
						
			int codigoCita=forma.getCitasOdonPac().get(forma.getPosicionCita()).getCodigoPk();
			
			
			ArrayList<DtoServicioOdontologico> listaServicios=forma.getServiciosOdon();
			ArrayList<DtoServicioCitaOdontologica> listaServiciosParaReservaAbonos=new ArrayList<DtoServicioCitaOdontologica>();
			ArrayList<DtoServicioCitaOdontologica> listaServiciosParaModificarSolicitud=new ArrayList<DtoServicioCitaOdontologica>();
						
			int tam=0;
			for(DtoServicioOdontologico elem: forma.getServiciosOdon())
			{
				if(elem.getAsociarSerCita().equals(ConstantesBD.acronimoSi))
				{
					tam++;
				}
			}			
			
			int insertar=0;
			if(estadoCita.equals(ConstantesIntegridadDominio.acronimoProgramado))
			{
					cita.setServiciosCitaOdon(new ArrayList<DtoServicioCitaOdontologica>());
					if(!guardarAsignacionCitaNueva(forma,con,cita, usuario, mapping, request, errores, persona))
					{
						return mapping.findForward("paginaError");
					}

					/*
					Modificado por tarea 153853
					
					if(forma.getDtoCitaProgramada().getServiciosCitaOdon().size() > tam || !forma.getDtoCitaProgramada().getFechaProgramacion().equals(UtilidadFecha.conversionFormatoFechaABD(forma.getFechaDesplazamiento())))
					{
						cita.setServiciosCitaOdon(new ArrayList<DtoServicioCitaOdontologica>());
						if(!guardarAsignacionCitaNueva(forma,con,cita, usuario, mapping, request, errores, persona))
						{
							return mapping.findForward("paginaError");
						}
					}
					else
					{
						insertar=1;
					}
					*/
			}
			else
			{
				insertar=1;
			}

			if(insertar == 1)
			{
				
				// valida si se debe crear el ingreso del paciente. Este camino 
				// solo sucede cuando se esta asignando una cita que ha sido reservada.
				DtoResultado resultado = validarCrearIngreso(forma, persona, usuario, null);
				
				try 
				{
					// Se carga nuevamente el paciente para tener los datos antes de guardar
					persona.cargar(con,persona.getCodigoPersona());
					persona.cargarPaciente(con, persona.getCodigoPersona(),usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
					this.setObservable(persona);
					
				} catch (SQLException e1) {
					Log4JManager.info("No se puedo recargar el paciente");
					e1.printStackTrace();
				}
				
				
				for(DtoServicioOdontologico servicio:listaServicios)
				{
					if(servicio.getAsociarSerCita().equals(ConstantesBD.acronimoSi))
					{
						DtoServicioCitaOdontologica servicioCita=new DtoServicioCitaOdontologica();
						
						servicioCita.setCitaOdontologica(codigoCita);
						servicioCita.setUnidadAgenda(forma.getAgendaSel().getUnidadAgenda());
						servicioCita.setServicio(servicio.getCodigoServicio());
						servicioCita.setCodigoPrograma(servicio.getCodigoPrograma());
						servicioCita.setDuracion(servicio.getMinutosduracionNuevos());
						servicioCita.setProgramaHallazgoPieza(servicio.getProgramaHallazgoPieza());
						servicioCita.setCodigoPresuOdoProgSer(servicio.getCodigoPresuOdoProgSer());
						servicioCita.setGarantiaServicio(servicio.getGarantiaServicio());
						servicioCita.setCodigoTipoServicio(servicio.getCodigoTipoServicio());
						servicioCita.setNombreServicio(servicio.getDescripcionServicio());
						servicioCita.setInfoTarifa(servicio.getInfoTarifa());
						
						if(servicio.getInfoTarifa() != null) {
							servicioCita.setValorTarifa(servicio.getInfoTarifa().getValorTarifaTotalConDctos());
						}
						
						servicioCita.setIndicativo(servicio.getIndicativo());
						Log4JManager.info("\n\n\n\n\n  ***************  servicioCita  ---> "+servicio.getInfoTarifa().getEstadoFacturacion());
						
						servicioCita.setTipoCita(forma.getCitasOdonPac().get(forma.getPosicionCita()).getTipo());
						servicioCita.setEstadoCita(forma.getCitasOdonPac().get(forma.getPosicionCita()).getEstado());
						servicioCita.setCodigoAgenda(forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgenda());
						
						//----------------------------------- viene reservada
						int contrato = ConstantesBD.codigoNuncaValido;
						if(servicio.getResponsableServicio() == null){	// esto aplica para cuando es diferente de tratamiento
							contrato = Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoContrato());
						}else{ contrato=servicio.getResponsableServicio().getContrato(); }
						//----------------------------------
						
						servicioCita.setInfoResponsableCobertura(new InfoResponsableCobertura());
						servicioCita.getInfoResponsableCobertura().setDtoSubCuenta(new DtoSubCuentas());
						servicioCita.getInfoResponsableCobertura().getDtoSubCuenta().setContrato(contrato);
						
						String subcuenta = "";
						if(servicio.getResponsableServicio() == null){
							subcuenta = resultado.getPk2();
						}else{ subcuenta = servicio.getResponsableServicio().getSubCuenta(); }
						servicioCita.getInfoResponsableCobertura().getDtoSubCuenta().setSubCuenta(subcuenta);
						
						if(servicioCita.getEstadoCita().equals(ConstantesIntegridadDominio.acronimoProgramado))
							servicioCita.setFechaCita(forma.getFechaDesplazamiento());
						else
							servicioCita.setFechaCita(forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon().getFecha());
						servicioCita.setHoraInicio(cita.getHoraInicio());
						servicioCita.setHoraFinal(cita.getHoraFinal());
						servicioCita.setActivo(ConstantesBD.acronimoSi);
						servicioCita.setUsuarioModifica(usuario.getLoginUsuario());
						
						boolean pacientePagaAtencion=Contrato.pacientePagaAtencion(contrato);
										
						if(pacientePagaAtencion)
						{
							servicio.setAplicaAnticipo(ConstantesBD.acronimoNo);
							servicio.setAplicaAbono(ConstantesBD.acronimoSi);
							servicioCita.setAplicaAnticipo(ConstantesBD.acronimoNo);
							servicioCita.setAplicaAbono(ConstantesBD.acronimoSi);
						}
						else
						{
							servicio.setAplicaAnticipo(ConstantesBD.acronimoSi);
							servicio.setAplicaAbono(ConstantesBD.acronimoNo);
							servicioCita.setAplicaAnticipo(ConstantesBD.acronimoSi);
							servicioCita.setAplicaAbono(ConstantesBD.acronimoNo);
						}
						forma.getCitaAgendaSel().getServiciosCitaOdon().add(servicioCita);
						int numeroSolicitud=Utilidades.convertirAEntero(cita.getSolicitud());
						boolean garantia=servicio.getInfoTarifa()==null; /* Si llega null es porque es Garantï¿½a*/
						
						
						if(numeroSolicitud<=0)
						{
							if(cita.getTipo().equals(ConstantesIntegridadDominio.acronimoRemisionInterconsulta))
							{
							   numeroSolicitud=UtilidadOdontologia.consultarNumeroSolicitudServicio(servicioCita.getServicio(),forma.getCodCuentaPaciente());
							}else
							{							
							  //numeroSolicitud=CitaOdontologica.generarSolicitud(con, servicioCita, persona, usuario, forma.getCodCentroCostoXuniAgen(), forma.getAgendaSel().getEspecialidadUniAgen()/*@TODO Revisar las especialidades por mï¿½dico*/, usuario.getCodigoPersona(), ConstantesBD.codigoOcupacionMedicaNinguna, errores, garantia).intValue();
								numeroSolicitud = 0;
							}
						}
						Log4JManager.info("\n\nnumero de solicitud: "+numeroSolicitud);
						
						servicioCita.setNumeroSolicitud(numeroSolicitud);
						listaServiciosParaModificarSolicitud.add(servicioCita);
						
						if(pacientePagaAtencion)				
						{
							Log4JManager.info("se adiciona el servicio "+contrato);
							listaServiciosParaReservaAbonos.add(servicioCita);
						}
							
						/*
						 * Este lo debo utilizar cunado estoy con plan de tratamiento
									double porcentaje=PresupuestoOdontologico.obtenerPorcentajeDctoOdonContratadoPresupuesto(codigoPresupuesto);
						 */	
					}
				}
							
				if(!horaFinalCita.equals(""))
				{
						if(horaFinalCita.compareTo(forma.getHoraFinal())<0)
						{
							if(AgendaOdontologica.modificarCuposAgendaOdontologica(con, forma.getCodigoAge(), horaFinalCita, cita.getHoraFinal(), usuario.getLoginUsuario(), true, usuario.getCodigoInstitucionInt())<=0)
							{
								UtilidadBD.abortarTransaccion(con);
								try {
									UtilidadBD.cerrarConexion(con);
								} catch (SQLException e) {
									Log4JManager.info("Error cerrando la conexión", e);
								}
								errores.add("Error validacion anticipo", new ActionMessage("errors.problemasBd"));
								return mapping.findForward("paginaError");
							}
						}
						else if(horaFinalCita.compareTo(forma.getHoraFinal())>0)
						{
							if(AgendaOdontologica.modificarCuposAgendaOdontologica(con, forma.getCodigoAge(), cita.getHoraFinal(), horaFinalCita, usuario.getLoginUsuario(), false, usuario.getCodigoInstitucionInt())<=0)
							{
								UtilidadBD.abortarTransaccion(con);
								try {
									UtilidadBD.cerrarConexion(con);
								} catch (SQLException e) {
									Log4JManager.info("Error cerrando la conexión", e);
								}
								errores.add("Error validacion anticipo", new ActionMessage("errors.problemasBd"));
								return mapping.findForward("paginaError");
							}
						}
				 }
					else
					{				
						if(AgendaOdontologica.modificarCuposAgendaOdontologica(con, forma.getCodigoAge(), cita.getHoraInicio(), cita.getHoraFinal(), usuario.getLoginUsuario(), false, usuario.getCodigoInstitucionInt())<=0)
						{
							UtilidadBD.abortarTransaccion(con);
							try {
								UtilidadBD.cerrarConexion(con);
							} catch (SQLException e) {
								Log4JManager.info("Error cerrando la conexión", e);
							}
							errores.add("Error validacion anticipo", new ActionMessage("errors.problemasBd"));
							return mapping.findForward("paginaError");
						}
						
					}
				//cambia el estado de la cita a asignada
				DtoCitaOdontologica dto=cita;
				dto=forma.getCitasOdonPac().get(forma.getPosicionCita());
				dto.setEstado(ConstantesIntegridadDominio.acronimoAsignado);
				dto.setServiciosCitaOdon(listaServiciosParaModificarSolicitud);
						
				if(!CitaOdontologica.actualizarCitaOdontologicaYServicios(con, dto, false))
				{
					UtilidadBD.abortarTransaccion(con);
					try {
						UtilidadBD.cerrarConexion(con);
					} catch (SQLException e) {
						Log4JManager.info("Error cerrando la conexión", e);
					}
					errores.add("Error validacion anticipo", new ActionMessage("errors.problemasBd"));
					return mapping.findForward("paginaError");
				}
				forma.setCitaCreada(dto);
				
				Log4JManager.info("aqui reserva los abonos "+listaServiciosParaReservaAbonos.size());
				reservarAbonos(con, listaServiciosParaReservaAbonos, usuario, persona, codigoCita);
			}
		}
		else{
			
			if(!guardarAsignacionCitaNueva(forma,con,cita, usuario, mapping, request, errores, persona))
				return mapping.findForward("paginaError");
		}
		
		forma.setCitaCreada(cita);
		
		Log4JManager.info("forma.getDuracionCita() "+forma.getDuracionCita());
		
		UtilidadBD.finalizarTransaccion(con);
		
		boolean actualizarInfo = true;
		
		try {
			
			UtilidadBD.cerrarConexion(con);
			
		} catch (SQLException e) {
			
			Log4JManager.info("Error cerrando la conexión", e);
			actualizarInfo = false;
		}
		
		
		// Anexo 1131: Asociar el convenio seleccionado a los datos básicos del paciente y actualizar los bonos si tiene
		// Si la cita es de tratamiento no hay que actualizar los datos básicos porqué se supone que ya tiene los convenios asociados
		llamarActualizarInformacionIngresoPciente(forma, persona, usuario, null, actualizarInfo);
		//-----------------------------------------
		forma.setCodigoCitaGuardada(forma.getCitaCreada().getCodigoPk()); // Se guarda la cita insertada para poder asociarla a la programada.
		//asociarCitasProgramadas(forma); //En este caso no se asocia por la ruta de donde se viene
		//----------------------------------------------------------------------------------------------------------------
		
		
		forma.setProcesoExito(ConstantesBD.acronimoSi);
		Log4JManager.info("duracion cita "+forma.getDuracionCita());
		
		forma.setAbonosDisponiblesPaciente(Utilidades.obtenerAbonosDisponiblesPaciente(persona.getCodigoPersona(),persona.getCodigoIngreso(), usuario.getCodigoInstitucionInt()));
		
		return mapping.findForward("asignacionCita");
	}

	
	
	/**
	 * @param forma
	 * @param con
	 * @param cita
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param errores
	 * @param persona
	 * @return boolean
	 */
	public boolean guardarAsignacionCitaNueva(AgendaOdontologicaForm forma, Connection con, DtoCitaOdontologica cita, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, ActionErrors errores, PersonaBasica persona)
	{
		
		//----- Anexo 1131:  ---------------------------------------------------
		IIngresosServicio ingresosServicio = ManejoPacienteServicioFabrica.crearIngresosServicio();
		DtoInformacionBasicaIngresoPaciente dtoInfoBasica = ingresosServicio.construirDtoInformacionBasicaIngresoPaciente(forma, usuario);
		DtoResultado resultado = validarCrearIngreso(forma, persona, usuario, dtoInfoBasica);
		//----------------------------------------------------------------------
		
		try 
		{
			// Se carga nuevamente el paciente para tener los datos antes de guardar
			persona.cargar(con,persona.getCodigoPersona());
			persona.cargarPaciente(con, persona.getCodigoPersona(),usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
			this.setObservable(persona);
			
		} catch (SQLException e1) {
			Log4JManager.info("No se puedo recargar el paciente");
			e1.printStackTrace();
		}
		
		
		boolean retornar=true;
		Log4JManager.info("--- forma.getDuracionCita(): "+forma.getDuracionCita());
		Log4JManager.info("--- TIPO CITA: "+cita.getTipo()+" >> "+forma.getTipoCita());
	
		if(forma.getPosicionCita()>=0 && !forma.getCodigoActividad().equals(""))
		{
			for(DtoServicioCitaOdontologica elem: cita.getServiciosCitaOdon())
			{
				elem.setCodigoAgenda(forma.getCodigoAge());
				
				// Se guarda el valor de la cita.
				// guardandotarifas
				if(elem.getInfoTarifa()!=null)
				{
					if(elem.getInfoTarifa().getValorTarifaTotalConDctos() != null){
						elem.setValorTarifa(elem.getInfoTarifa().getValorTarifaTotalConDctos());
					}
				}
				
			}
		}
		
		forma.getCodCuentaPaciente();
		int codigoCita=CitaOdontologica.insertarCitaOdontologica(con, cita);
		if(codigoCita>0)
		{
			cita.setCodigoPk(codigoCita);
			ArrayList<DtoServicioOdontologico> listaServicios=forma.getServiciosOdon();
			ArrayList<DtoServicioCitaOdontologica> listaServiciosParaReservaAbonos=new ArrayList<DtoServicioCitaOdontologica>();
			for(DtoServicioOdontologico servicio:listaServicios)
			{
				if(servicio.getAsociarSerCita().equals(ConstantesBD.acronimoSi)){
					
					DtoServicioCitaOdontologica servicioCita=new DtoServicioCitaOdontologica();
					servicioCita.setCitaOdontologica(codigoCita);
					servicioCita.setTipoCita(forma.getTipoCita());
					servicioCita.setServicio(servicio.getCodigoServicio());
					servicioCita.setDuracion(servicio.getMinutosduracionNuevos());
					servicioCita.setProgramaHallazgoPieza(servicio.getProgramaHallazgoPieza());
					servicioCita.setCodigoPrograma(servicio.getCodigoPrograma());
					servicioCita.setCodigoProgramaHallazgoPieza(servicio.getProgramaHallazgoPieza().getCodigoPk());

					servicioCita.setActivo(ConstantesBD.acronimoSi);
					servicioCita.setUsuarioModifica(usuario.getLoginUsuario());
					servicioCita.setEstadoCita(ConstantesIntegridadDominio.acronimoAsignado);
					servicioCita.setCodigoAgenda(forma.getCodigoAge());
					servicioCita.setFechaCita(forma.getAgendaSel().getFecha());
					servicioCita.setHoraInicio(forma.getHoraInicio());
					servicioCita.setHoraFinal(forma.getHoraFinal());
					servicioCita.setUnidadAgenda(forma.getAgendaSel().getUnidadAgenda());
					servicioCita.setCodigoPresuOdoProgSer(servicio.getCodigoPresuOdoProgSer());
					servicioCita.setGarantiaServicio(servicio.getGarantiaServicio());
					servicioCita.setCodigoTipoServicio(servicio.getCodigoTipoServicio());
					servicioCita.setNombreServicio(servicio.getDescripcionServicio());
					servicioCita.setNuevoServicio(ConstantesBD.acronimoSi);
					servicioCita.setInfoTarifa(servicio.getInfoTarifa()); 
					
					if(servicio.getInfoTarifa() != null) {
						servicioCita.setValorTarifa(servicio.getInfoTarifa().getValorTarifaTotalConDctos());
					}
					
					
					//NOTA: Anexo 1131: Revisar si esto no afecta. Anteriormente solo se hacia el else
					int contrato = ConstantesBD.codigoNuncaValido;
					if(servicio.getResponsableServicio() == null){	// esto aplica para cuando es diferente de tratamiento
						contrato = Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoContrato());
					}else{ contrato=servicio.getResponsableServicio().getContrato(); }
					
					servicioCita.setInfoResponsableCobertura(new InfoResponsableCobertura());
					servicioCita.getInfoResponsableCobertura().setDtoSubCuenta(new DtoSubCuentas());
					servicioCita.getInfoResponsableCobertura().getDtoSubCuenta().setContrato(contrato);
					
					/* Si el paciente no tiene ingreso se crea uno nuevo (insertando en: ingreso, cuenta, subcuenta).
					 En este caso el DtoResultado tiene el codigo de la subcuenta creada. */
					String subcuenta = "";
					if(servicio.getResponsableServicio() == null){
						subcuenta = resultado.getPk2();
					}else{ subcuenta = servicio.getResponsableServicio().getSubCuenta(); }
					servicioCita.getInfoResponsableCobertura().getDtoSubCuenta().setSubCuenta(subcuenta);
					
					boolean pacientePagaAtencion=Contrato.pacientePagaAtencion(contrato);
					if(pacientePagaAtencion)
					{
						servicio.setAplicaAnticipo(ConstantesBD.acronimoNo);
						servicio.setAplicaAbono(ConstantesBD.acronimoSi);
						servicioCita.setAplicaAnticipo(ConstantesBD.acronimoNo);
						servicioCita.setAplicaAbono(ConstantesBD.acronimoSi);
					}
					else
					{
						servicio.setAplicaAnticipo(ConstantesBD.acronimoSi);
						servicio.setAplicaAbono(ConstantesBD.acronimoNo);
						servicioCita.setAplicaAnticipo(ConstantesBD.acronimoSi);
						servicioCita.setAplicaAbono(ConstantesBD.acronimoNo);
					}
					forma.getCitaAgendaSel().getServiciosCitaOdon().add(servicioCita);
					cita.getServiciosCitaOdon().add(servicioCita);
					
					boolean garantia=UtilidadTexto.getBoolean(servicio.getGarantiaServicio());
					
					Log4JManager.info("\n\n ---GARANTIA---> "+garantia);
					
					int numeroSolicitud=0;
					
					if(cita.getTipo().equals(ConstantesIntegridadDominio.acronimoRemisionInterconsulta))
					{					  	
					   numeroSolicitud=UtilidadOdontologia.consultarNumeroSolicitudServicio(servicioCita.getServicio(), forma.getCodCuentaPaciente());
					}else
					{
						//numeroSolicitud=CitaOdontologica.generarSolicitud(con, servicioCita, persona, usuario, forma.getCodCentroCostoXuniAgen(), forma.getAgendaSel().getEspecialidadUniAgen()/*@TODO Revisar las especialidades por mï¿½dico*/, usuario.getCodigoPersona(), ConstantesBD.codigoOcupacionMedicaNinguna, errores, garantia).intValue();
						numeroSolicitud = 0;
					}
					
					// Log4JManager.info("genero la solicitud");
					Log4JManager.info("\n\nnumero solicitud: "+numeroSolicitud);
					if(numeroSolicitud>0)
					{	
						servicioCita.setNumeroSolicitud(numeroSolicitud);
						// Log4JManager.info("numero de solicitud "+numeroSolicitud);						
						int codigoServicioCitaOdo=CitaOdontologica.insertarServiciosCitaOdontologica(con, servicioCita);
						//cita.setCodigoPk(codigoServicioCitaOdo);

						/*
						 * Este lo debo utilizar cunado estoy con plan de tratamiento
								double porcentaje=PresupuestoOdontologico.obtenerPorcentajeDctoOdonContratadoPresupuesto(codigoPresupuesto);
						*/
					}
					else{
						UtilidadBD.abortarTransaccion(con);
						try {
							UtilidadBD.cerrarConexion(con);
						} catch (SQLException e) {
							Log4JManager.error("Error cerrando la conexión", e);
						}
						errores.add("Error validacion anticipo", new ActionMessage("errors.problemasBd"));
						retornar=false;
					}
					if(pacientePagaAtencion)
					{
						listaServiciosParaReservaAbonos.add(servicioCita);
					}
				}
			}
			reservarAbonos(con, listaServiciosParaReservaAbonos, usuario, persona, codigoCita);
		}
		else{
			UtilidadBD.abortarTransaccion(con);
			try {
				UtilidadBD.cerrarConexion(con);
			} catch (SQLException e) {
				Log4JManager.error("Error cerrando la conexión", e);
			}
			errores.add("Error validacion anticipo", new ActionMessage("errors.problemasBd"));
			retornar=false;
		}
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
		}
		
		Log4JManager.info("hora inicio "+forma.getHoraInicio()+" hora final "+forma.getHoraFinal());
		Log4JManager.info("forma.getDuracionCita() "+forma.getDuracionCita());
		
		if(AgendaOdontologica.modificarCuposAgendaOdontologica(con, forma.getCodigoAge(), forma.getHoraInicio(), cita.getHoraFinal(), usuario.getLoginUsuario(), true, usuario.getCodigoInstitucionInt())<=0)
		{
			Log4JManager.info("forma.getDuracionCita() "+forma.getDuracionCita());
			UtilidadBD.abortarTransaccion(con);
			try {
				UtilidadBD.cerrarConexion(con);
			} catch (SQLException e) {
				Log4JManager.error("Error cerrando la conexión", e);
			}
			errores.add("Error validacion anticipo", new ActionMessage("errors.problemasBd"));
			retornar=false;
		}
		else
		{
			UtilidadBD.finalizarTransaccion(con);
			try {
				UtilidadBD.cerrarConexion(con);
			} catch (SQLException e) {
				Log4JManager.error("Error cerrando la conexión", e);
			}
		}
		
		
		// Anexo 1131: Asociar el convenio seleccionado a los datos básicos del paciente y actualizar los bonos si tiene
		// Si la cita es de tratamiento no hay que actualizar los datos básicos porqué se supone que ya tiene los convenios asociados
		llamarActualizarInformacionIngresoPciente(forma, persona, usuario, dtoInfoBasica, retornar);
		//-----------------------------------
		forma.setCodigoCitaGuardada(codigoCita); // Se guarda la cita insertada para poder asociarla a la programada.
		asociarCitasProgramadas(forma, usuario, persona);
		//----------------------------------------------------------------------------------------------------------------
		
		return retornar;
	}
	
	
	/**
	 * Juan David
	 * Método para reservar los abonos o los anticipos del paciente (Valor total de todos los servicios)
	 * @param con
	 * @param listaServicios
	 * @param usuario actualizarInformacionIngresoPciente
	 * @param persona 
	 * @param codigoCita Este cï¿½digo de cita se pondrï¿½ como nï¿½mero de documento
	 * @return
	 */
	public boolean reservarAbonos(Connection con, ArrayList<DtoServicioCitaOdontologica> listaServicios, UsuarioBasico usuario, PersonaBasica persona, int codigoCita)
	{
		double valorAbonos=0;
		Log4JManager.info("empieza");
		for(DtoServicioCitaOdontologica servicio:listaServicios)
		{
			Log4JManager.info("servicio "+servicio.getCodigoPk());
			if(!UtilidadTexto.getBoolean(servicio.getGarantiaServicio()))
			{
				Log4JManager.info("adiciona "+servicio.getInfoTarifa().getValorTarifaTotalConDctos().doubleValue());
				valorAbonos+=servicio.getInfoTarifa().getValorTarifaTotalConDctos().doubleValue();//
			}
		}
		if(valorAbonos>0)
		{
			//return AbonosYDescuentos.insertarMovimientoAbonos(con, persona.getCodigoPersona(), codigoCita, ConstantesBD.tipoMovimientoAbonoSalidaReservaAbono, valorAbonos, usuario.getCodigoInstitucionInt(), persona.getCodigoIngreso())>0;
			return false;
		}
		return false;
	}

	/**
	 * M&eacute;todo para validar los abonos o anticipos disponibles de los pacientes
	 * para la reserva.
	 * @param forma
	 * @param errores
	 * @return ActionErrors Errores generados durante el proceso
	 * @author Juan David
	 */
	private ActionErrors validarAbonosYAnticipos(AgendaOdontologicaForm forma, ActionErrors errores) {
		
		double valorTotalServiciosAbono = forma.getValorTotalServiciosAbono();
		if (valorTotalServiciosAbono > 0) {
			
			if(forma.getAbonosDisponiblesPaciente() < valorTotalServiciosAbono)
			{
				errores.add("Error validacion abono", new ActionMessage("errors.notEspecific", "El abono disponible "+
						forma.getAbonosDisponiblesPaciente()+" no cubre valor total del servicio: "+UtilidadTexto.formatearValores(valorTotalServiciosAbono)));
			}
			
		}
		return errores;
	}


	/**
	 * Ingreso nueva cita desde el record de citas o desde la asignación poniendo un nuevo cupo
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param mundo
	 * @param mundoCita
	 * @return página según el flujo
	 */
	private ActionForward accionIniciarAsignar(AgendaOdontologicaForm forma, UsuarioBasico usuario, ActionMapping mapping,
					HttpServletRequest request, HttpServletResponse response, PersonaBasica paciente) throws IPSException
	{

		forma.setMostrarTarifaServicios(true);

		llenarInformacionAsignacioncita(forma, usuario, mapping, request, response, paciente);
		
		forma.setProcesoExito(ConstantesBD.acronimoNo);
		
		if(paciente==null || paciente.getCodigoPersona()<=0)
		{
			ActionErrors errores=new ActionErrors();
			errores.add("fechaDesplazamiento",new ActionMessage("errors.paciente.noCargadoPac"));
			saveErrors(request, errores);
			return mapping.findForward("principalXRango");
		}
		
				
		if(paciente.getCodigoIngreso()>0)
		{
			return flujoContinuarConCita(forma, usuario, mapping, request, paciente);
		
		}else{
			
			UtilidadTransaccion.getTransaccion().begin();
			AgendaOdontologica agendaOdontologicaMundo = new AgendaOdontologica();
			
			// validar si tiene convenios activos
			if(!Utilidades.isEmpty	
				((ArrayList)agendaOdontologicaServicio.obtenerConveniosIngresoPacientePorEstado(paciente.getCodigoPersona(), ConstantesBD.acronimoSiChar)))
			{
				return flujoContinuarConCita(forma, usuario, mapping, request, paciente);
			}
			else
			{
				// validar convenios por defecto
				ArrayList<HashMap<String, Object>> arrayConveniosParametrizadosPorDefecto = ValoresPorDefecto.getConveniosAMostrarPresupuestoOdo();
				
				if(arrayConveniosParametrizadosPorDefecto.size() <= 0)
				{
					UtilidadTransaccion.getTransaccion().commit();
					return llamarRutaCreacionCuenta(paciente, response, request, mapping);
				}		
				else{
					// convenios tipo tarj cliente
					if(agendaOdontologicaMundo.existeConvenioActivoTipoTarjetaClienteNo(arrayConveniosParametrizadosPorDefecto))
					{
						UtilidadTransaccion.getTransaccion().commit();
						return flujoContinuarConCita(forma, usuario, mapping, request, paciente);
					}
					else
					{
						UtilidadTransaccion.getTransaccion().commit();
						return llamarRutaCreacionCuenta(paciente, response, request, mapping);
					}
				}
			}
		}
		
	}


	

	/**
	 * Continua con el flujo de cita
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @return ActionForward
	 */
	private ActionForward flujoContinuarConCita(AgendaOdontologicaForm forma, UsuarioBasico usuario, ActionMapping mapping,
			HttpServletRequest request, PersonaBasica paciente) throws IPSException
	{
		Log4JManager.info(" Codigo Cuenta >>> "+paciente.getCodigoCuenta());
		forma.setCodCuentaPaciente(paciente.getCodigoCuenta());
		return accionNuevaCita(forma, usuario, mapping, request, paciente);
	}
	
	
	
	
	/**
	 * Método para guardar la cancelacón de la cita odontológica
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param mundo
	 * @param mundoCita
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionGuardarCancelacion(
			AgendaOdontologicaForm forma, UsuarioBasico usuario,
			ActionMapping mapping, HttpServletRequest request,
			AgendaOdontologica mundo, CitaOdontologica mundoCita,PersonaBasica paciente) throws SQLException, IPSException {
		
		Connection con = UtilidadBD.abrirConexion();
		ResultadoBoolean resultado = new ResultadoBoolean(Boolean.TRUE);
		
		forma.setTipoCitaCrear(ConstantesBD.acronimoCancelarCitaOdon);
		
		ActionErrors errores = validacionCitaOdontologica(forma, paciente);
		
		if (!errores.isEmpty())
		{
			forma.setProcesoExito(ConstantesBD.acronimoNo);
			saveErrors(request, errores);
		
		}else
		{
			//proceso de cancelación duvian;
			
			//llena la cita con la que voy a trabajar
			/*UtilidadBD.iniciarTransaccion(con); 
			DtoCitaOdontologica cita=forma.getCitasOdonPac().get(forma.getPosicionCita());
			// revisa si la cita esta asignada
			if(cita.getEstado().equals(ConstantesIntegridadDominio.acronimoAsignado))
			{
				Log4JManager.info("llega cuando la cita esta asignada");
				
				for(DtoServicioCitaOdontologica elem: cita.getServiciosCitaOdon())
				{
					Log4JManager.info("numero solicitud "+elem.getNumeroSolicitud());
					//Log4JManager.info.("orden servicio >>"+ )
					if(elem.getFacturado().equals(ConstantesBD.acronimoNo))
					{
						if(!Solicitud.cambiarEstadosSolicitudStatico(con,
								elem.getNumeroSolicitud(), 
								ConstantesBD.codigoEstadoFAnulada, 
								ConstantesBD.codigoEstadoHCAnulada).isTrue())
						{
							Log4JManager.info("se aborta la transaccion por errores en la anulacion de solicitudes");
							UtilidadBD.abortarTransaccion(con);
							saveErrors(request, errores);
							
							break;
						}
						elem.setEliminarSer(ConstantesBD.acronimoSi);
					}
				}
				
				double valorAbonos=CitaOdontologica.obtenerValorCargadoCita(con, cita.getServiciosCitaOdon());
				
				CitaOdontologica.devolverReservaAbonosCita(con, valorAbonos, usuario, forma.getPaciente().getCodigo(), cita.getCodigoPk());
				
				if(errores.isEmpty())
				{
					UtilidadBD.finalizarTransaccion(con);
				}else{
					Log4JManager.info("se aborta la transaccion por errores en la eliminacion abonos");
					UtilidadBD.abortarTransaccion(con);
					saveErrors(request, errores);
					
				}
			}
			else // si no esta asignada debe estar reservada
			{
				for(DtoServicioCitaOdontologica elem: cita.getServiciosCitaOdon())
				{
					elem.setEliminarSer(ConstantesBD.acronimoSi);
					Log4JManager.info("reservada");
				}
			}
			

			boolean eliminarCupo=false;
			// verifica el motivo de la cita 
			if(forma.getTipoCancelacion().equals(ConstantesBD.acronimoNo))//institucion
			{
				cita.setEstado(ConstantesIntegridadDominio.acronimoAreprogramar);
			}
			else
			{      
			   Log4JManager.info("*****************pregunta si  "+forma.getTipoCancelacion());
	           Log4JManager.info("***************es igual a  "+ConstantesBD.acronimoSi);
					
				if(forma.getTipoCancelacion().equals(ConstantesBD.acronimoSi))//paciente
				{
					cita.setEstado(ConstantesIntegridadDominio.acronimoEstadoCancelado);
					eliminarCupo=true;
				}
				Log4JManager.info("*****************el estado llega   "+cita.getEstado());
				cita.setMotivoCancelacion(Utilidades.convertirAEntero(forma.getCodMotivoCancelacion()));
			}
			cita.setUsuarioModifica(usuario.getLoginUsuario());
			if(CitaOdontologica.cambiarSerOdontologicos(con, cita))
			{	
				// liberar cupo de la unidad de agenda odontologica cuando es motivo de cancelacion es por paciente
				if(eliminarCupo)
				{
				
					// aumenta el cupo de la cita 
					if(AgendaOdontologica.modificarCuposAgendaOdontologica(con,cita.getAgenda(), cita.getHoraInicio(), 
					forma.getHoraFinal(),usuario.getLoginUsuario(), false, usuario.getCodigoInstitucionInt())>0)
					{
						UtilidadBD.finalizarTransaccion(con);
					}
					else
					{
						UtilidadBD.abortarTransaccion(con);
						saveErrors(request, errores);
					}
				}
				
				if(forma.getListaProximasCitasCancelar().size()>0)
				 {
					return cancelarProximasCitas(con,forma,usuario, mapping, request);				  
				 }	
							
				forma.setProcesoExito(ConstantesBD.acronimoSi);
				UtilidadBD.finalizarTransaccion(con);
			}
			else
			{
				UtilidadBD.abortarTransaccion(con);
			}*/
			
			
			
			/*
			 * 1. si la cita es diferente a reprogramar.
			 */
			
			if(!forma.getCitaAgendaSel().getEstado().equals(ConstantesIntegridadDominio.acronimoAreprogramar) )
			{	
				if( forma.getCancelandoCitaOdoEstadoReprogramar().equals(ConstantesBD.acronimoSi) )
				{
					/*
					 *Reprogramar 
					 */
					//accionReprogramarCitaOdo(forma, con);
					resultado = accionCancelarCita(forma, usuario, paciente, con, ConstantesIntegridadDominio.acronimoAreprogramar);
				}
				else if( forma.getCancelandoCitaOdoEstadoReprogramar().equals(ConstantesBD.acronimoNo) ){
					
					if(forma.getReprogramaCita()!=null){
						
						if(forma.getReprogramaCita().equals(ConstantesBD.acronimoSi)){
							
							/*
							 *Reprogramar 
							 */
							
							resultado = accionCancelarCita(forma, usuario, paciente, con, ConstantesIntegridadDominio.acronimoAreprogramar);
							
						}
						else if(forma.getReprogramaCita().equals(ConstantesBD.acronimoNo)){
							
							/*
							 * Cancelar la cita.
							 */
							
							resultado = accionCancelarCita(forma, usuario, paciente, con,ConstantesIntegridadDominio.acronimoCancelado );
						}
					}
				}
			}
			else
			{
				resultado = accionCancelarCita(forma, usuario, paciente, con , ConstantesIntegridadDominio.acronimoCancelado);
			}
			
			
			
			forma.setProcesoExito(UtilidadTexto.convertirSN(resultado.isTrue()+""));
			if(!resultado.isTrue())
			{
				Log4JManager.error("ERROR -->"+resultado.getDescripcion());
			}
		}
		UtilidadBD.cerrarConexion(con);
		
		return mapping.findForward("cancelacionCita");
	}
	
	/**
	* Método para hacer que el paciente
	* pueda ser visto por todos los usuario en la aplicacion
	* @param paciente
	*/
	private void setObservable(PersonaBasica paciente)
	{
		ObservableBD observable = (ObservableBD)getServlet().getServletContext().getAttribute("observable");
		if (observable != null) {
			synchronized (observable) {
				observable.setChanged();
				observable.notifyObservers(new Integer(paciente.getCodigoPersona()));
			}
		}
	}
	



	/**
	 * Metodo para cancelar la cita odontologica
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param con
	 * @return
	 */
	private ResultadoBoolean accionCancelarCita(AgendaOdontologicaForm forma,
			UsuarioBasico usuario, PersonaBasica paciente, Connection con , String estadoCita) throws IPSException {
		ResultadoBoolean resultado;
		
		int valorMotivoCancelacion= ConstantesBD.codigoNuncaValido;
		try {
		
			String[] list= forma.getCodMotivoCancelacion().split(ConstantesBD.separadorSplit);
			Log4JManager.info(list[0]);
			valorMotivoCancelacion=Utilidades.convertirAEntero(list[0]);
		} catch (Exception e) {
			
			Log4JManager.info(e.getMessage());
		}
		
		
		
		resultado= CitaOdontologica.cancelarCita(	con, 
									forma.getCitasOdonPac().get(forma.getPosicionCita()), 
									usuario, 
									forma.getPaciente().getCodigo(), 
									forma.getTipoCancelacion(), 
									valorMotivoCancelacion, 
									true, paciente.getCodigoIngreso(),
									 estadoCita);
		return resultado;
	}


	
	/**
	 * Metodo para reprogramar la cita odontologica.
	 * @param forma
	 * @param con
	 */

	@SuppressWarnings("unused")
	private void accionReprogramarCitaOdo(AgendaOdontologicaForm forma, Connection con) {
		DtoCitaOdontologica dtoCita = forma.getCitaAgendaSel();//
		dtoCita.setEstado(ConstantesIntegridadDominio.acronimoAreprogramar);
	
		CitaOdontologica.actualizarCitaOdontologica(con, dtoCita);
	}
	
	/**
	 * Mï¿½todo que realiza el guardado de la reserva
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param persona 
	 * @param mundoCita 
	 * @param mundo 
	 * @return
	 */
	private ActionForward accionGuardarReserva(AgendaOdontologicaForm forma,
			UsuarioBasico usuario, ActionMapping mapping,
			HttpServletRequest request, AgendaOdontologica mundo, CitaOdontologica mundoCita, PersonaBasica persona) 
	{
		//************VALIDACIONES INICIALES ********************************
		if(!forma.getCambiarServicio().equals(ConstantesBD.acronimoSi))
		{
			forma.setTipoCitaCrear(ConstantesIntegridadDominio.acronimoReservado);
		}
		forma.setFechaDesplazamiento(forma.getFecha());
		ActionErrors errores = validacionCitaOdontologica(forma, persona);
		//************************************************************************
		
		Log4JManager.info("duracion cita: "+forma.getDuracionCita());
		Log4JManager.info("hora inicio: "+forma.getHoraInicio());
		Log4JManager.info("hora final: "+forma.getHoraFinal());
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			forma.setProcesoExito(ConstantesBD.acronimoNo);
			forma.setMostrarTarifas(false);
			forma.setPasoValidacionesGuardar(false);
		}
		else
		{
			Log4JManager.info("POS CITA >> "+forma.getPosicionCita() +"   codActividad >> "+forma.getCodigoActividad());
			if(forma.getPosicionCita()>=0 && !forma.getCodigoActividad().equals(""))
				{
					Log4JManager.info("ENTRAAAAAAAAAA POR ESTE FLUJO   EXISTE CITAAA ....");
					Log4JManager.info("Hora Inicial CITA "+forma.getCitasOdonPac().get(forma.getPosicionCita()).getHoraInicio());
					
					// SI es por el flujo de  Cambiar Servicio
				  if(forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoCambiarServicio))
					{
					  Log4JManager.info("\n\n Cambiar Servicio");
						forma.setHoraFinal(UtilidadFecha.incrementarMinutosAHora(forma.getHoraInicio(), forma.getTotalDuracionServicios()));						
						Connection con = UtilidadBD.abrirConexion();
												
						if(!modificarCitaXReserva(con, usuario, forma, mundo, persona))
						{
							errores.add("modificarReserva",new ActionMessage("errors.notEspecific", "No se pudo realizar la Reserva. "));
							saveErrors(request, errores);
						}
					}// SI es por el Flujo de Reserva				     
					else
					 {
							int tam=0;
							for(DtoServicioOdontologico elem: forma.getServiciosOdon())
							{
								if(elem.getAsociarSerCita().equals(ConstantesBD.acronimoSi))
								{
									tam++;
								}
							}
							int insertar=0;
							if(forma.getCitasOdonPac().get(forma.getPosicionCita()).getEstado().equals(ConstantesIntegridadDominio.acronimoProgramado))
							{
								//////////ARMANDO
								Connection con = UtilidadBD.abrirConexion();
								//Se vuelve a calcular la hora final
								forma.setHoraFinal(UtilidadFecha.incrementarMinutosAHora(forma.getHoraInicio(), forma.getTotalDuracionServicios()));
								
								int codigoPkCita = insertarReservaCita(con, usuario, forma, mundo, mundoCita, persona);
								forma.setCodigoCitaGuardada(codigoPkCita); // Se guarda la cita insertada para poder asociarla a la programada.
								
								if(codigoPkCita==ConstantesBD.codigoNuncaValido)
								{
									errores.add("guardarReserva",new ActionMessage("errors.notEspecific", "No se pudo realizar la Reserva. "));
									saveErrors(request, errores);
								}
								
								/*
								 Modificado por Tarea 153853
								 
								 if(forma.getDtoCitaProgramada().getServiciosCitaOdon().size() > tam || !forma.getDtoCitaProgramada().getFechaProgramacion().equals(UtilidadFecha.conversionFormatoFechaABD(forma.getFechaDesplazamiento())))
								{
									Connection con = UtilidadBD.abrirConexion();
									//Se vuelve a calcular la hora final
									forma.setHoraFinal(UtilidadFecha.incrementarMinutosAHora(forma.getHoraInicio(), forma.getTotalDuracionServicios()));
									
									int codigoPkCita = insertarReservaCita(con, usuario, forma, mundo, mundoCita, persona);
										
									if(codigoPkCita==ConstantesBD.codigoNuncaValido)
									{
										errores.add("guardarReserva",new ActionMessage("errors.notEspecific", "No se pudo realizar la Reserva. "));
										saveErrors(request, errores);
									}
								}
								else
									insertar=1;*/
							 }
							  else
							   {
								insertar=1;
							   }    
							
							if(insertar == 1)
							{
								 Log4JManager.info("\n\n Reservar ");
									 for(int i=0;i<forma.getAgendaOdon().size();i++)
									 {
										Log4JManager.info("codigo agenda arreglo["+i+"]: "+forma.getAgendaOdon().get(i).getCodigoPk());
										if(forma.getAgendaOdon().get(i).getCodigoPk()==forma.getCodigoAge())
										{
											
											forma.setPosAgendaSel(i);
											forma.setAgendaSel(forma.getAgendaOdon().get(i));
				                            forma.setHoraFinal(UtilidadFecha.incrementarMinutosAHora(forma.getHoraInicio(), forma.getTotalDuracionServicios()));
											
											Connection con = UtilidadBD.abrirConexion();
																	
											if(!modificarCitaXReserva(con, usuario, forma, mundo, persona))
											{
												errores.add("modificarReserva",new ActionMessage("errors.notEspecific", "No se pudo realizar la Reserva. "));
												saveErrors(request, errores);
											}
								         }
							          }
						     }
				     }
				}else
				{				
					//Se vuelve a calcular la hora final
					forma.setHoraFinal(UtilidadFecha.incrementarMinutosAHora(forma.getHoraInicio(), forma.getTotalDuracionServicios()));
					
					Connection con = UtilidadBD.abrirConexion();
					int codigoPkCita = insertarReservaCita(con, usuario, forma, mundo, mundoCita, persona);
					forma.setCodigoCitaGuardada(codigoPkCita); // Se guarda la cita insertada para poder asociarla a la programada.
					
					if(codigoPkCita==ConstantesBD.codigoNuncaValido)
					{
						errores.add("guardarReserva",new ActionMessage("errors.notEspecific", "No se pudo realizar la Reserva. "));
						saveErrors(request, errores);
					}
					
					
				}
		}
		
		if(!errores.isEmpty()){
			forma.setMostrarTarifas(false);
			forma.setPasoValidacionesGuardar(false);
		}else{
			//------------------------------------------------------
			asociarCitasProgramadas(forma, usuario, persona);
			//------------------------------------------------------
		}
	
		
		return mapping.findForward("reservacionCita");
	}


	/**
	 * M&eacute;todo para iniciar el proceso de iniciar reserva de citas
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @return
	 */
	private ActionForward accionNuevaCita(AgendaOdontologicaForm forma,UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request, PersonaBasica paciente) throws IPSException 
	{
		forma.setMostrarTarifas(false);
		forma.setPasoValidacionesGuardar(false);
		forma.setHabilitarCalcTarifa(ConstantesBD.acronimoNo);
		
		cargarServiciosAsignacionYReservaCita(forma, usuario, paciente.getCodigoIngreso());
		
		cargarValoresSaldosAbonos(forma, usuario, paciente, request);
		
		validacionesMostrarConvenio(forma, paciente, usuario);
		
		return mapping.findForward("asignacionCita");
	}


	/**
	 * M&eacute;todo centralizado en donde se cargan los servicios de la cita, ya sea cuando se va a asignar
	 * una nueva cita o cuando se va a reservar.
	 * 
	 * @param forma
	 * @param usuario
	 * @return c&oacute;digo de la unidad de agenda.
	 */
	private void cargarServiciosAsignacionYReservaCita(AgendaOdontologicaForm forma, UsuarioBasico usuario, int codigoIngreso){
		
		int unidadAgenda=0;
		int codigoCita=0;
		int codigoMedico = ConstantesBD.codigoNuncaValido;
		boolean buscarPlanTratamiento=true;
		
		forma.setOtrosServiciosOdon(new ArrayList<DtoServicioOdontologico>());
		forma.setServiciosOdon(new ArrayList<DtoServicioOdontologico>());
		
		// Se necesita el código de la Cita para Busqueda de Servicios y verificar si se encuentrar asignados a otra cita 
		if(forma.getPosicionCita()>=0 && forma.getCitasOdonPac()!=null && forma.getCitasOdonPac().size()>=forma.getPosicionCita()){
			
			codigoCita=forma.getCitasOdonPac().get(forma.getPosicionCita()).getCodigoPk();
			//forma.getCitaAgendaSel().setCodigoPk(codCita);

		}else{
			
			codigoCita=ConstantesBD.codigoNuncaValido;
		}
	
		if(forma.getAgendaSel().getCodigoPk()<=0){
	
			forma.setCitaAgendaSel(forma.getCitasOdonPac().get(forma.getPosicionCita()));
			forma.setTipoCita(forma.getCitaAgendaSel().getTipo());
			forma.setHoraInicio(forma.getCitaAgendaSel().getHoraInicio());
			forma.setHoraFinal(forma.getCitaAgendaSel().getHoraFinal());
			forma.setFecha(forma.getCitaAgendaSel().getFechaProgramacion());
			forma.setFechaDesplazamiento(forma.getFecha());

			if(codigoCita!=ConstantesBD.codigoNuncaValido){
				
				forma.getCitaAgendaSel().setCodigoPk(codigoCita);
			}

			codigoMedico = forma.getCitaAgendaSel().getAgendaOdon().getCodigoMedico();
			unidadAgenda= forma.getCitaAgendaSel().getAgendaOdon().getUnidadAgenda();
		
			//if(!forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoTipoFuncionalidadReservar)){
				
				if(forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoPrioritaria) || 
						forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial) ||
						forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoControlCitaOdon) ||
						forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoAuditoria) ||
						forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoRevaloracion)){
					
					buscarPlanTratamiento=false;
				}
			//}
		}else{
			
			codigoMedico = forma.getAgendaSel().getCodigoMedico();
			unidadAgenda=forma.getAgendaSel().getUnidadAgenda();
		
			//if(!forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoTipoFuncionalidadReservar)){
				
				if(forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoPrioritaria) || 
						forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial) ||
						forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoControlCitaOdon) || // Se agrego Auditoria...Tarea 150439
						forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoAuditoria)) {
						
					buscarPlanTratamiento=false;
				}
			//}
		}
			
		forma.setCasoCita(obtenerServiciosXTipoCita(forma, usuario, codigoIngreso, unidadAgenda, codigoCita, codigoMedico, buscarPlanTratamiento));
		
		ArrayList<DtoServicioOdontologico> listaServicios = depurarListadoServicios(forma, usuario);
		
		forma.setOtrosServiciosOdon(listaServicios) ;
		
		if(forma.getOtrosServiciosOdon() !=null && forma.getOtrosServiciosOdon().size()>0)
		{
			if(forma.getPosicionCita()>=0 && listaServicios.size()>0)
			{
				listaServicios = actualizarDuracionServicios(forma, listaServicios);
			}
			
			if(listaServicios.size()>0){
			
				forma.setCasoBusServicio(listaServicios.get(0).getCasoBusServicio());
				forma.setCodigoPlanTratamiento(listaServicios.get(0).getCodigoPlanTratamiento());
			}
			
			//verificar si la visualizacion de los servicios es por programa o servicios
			if(forma.getUtilProgOdonPlanTra().equals(ConstantesBD.acronimoSi))
			{
				ArrayList<DtoServicioOdontologico> servicioOdoOrden = new ArrayList<DtoServicioOdontologico>();
				String seccion = "";
				String codigoProg = "";
				forma.setSeccion(listaServicios);
				forma.setProgramas(listaServicios);
				Iterator<String> iterPrograma = forma.getProgramas().iterator();
				while(iterPrograma.hasNext())
				{
					codigoProg = iterPrograma.next();
					Iterator<String> iterSeccion = forma.getSeccion().iterator();
					while(iterSeccion.hasNext())
					{
						seccion = iterSeccion.next() ;
						for(DtoServicioOdontologico servicio: listaServicios)
						{
							if(servicio.getSeccionPlanTrata().equals(seccion) && servicio.getCodigoPrograma()==Utilidades.convertirAEntero(codigoProg)){
								
								servicioOdoOrden.add(servicio);
							}
						
							if(forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoRemisionInterconsulta)){
					
								if(servicio.getEstaVinculadoSerCita().equals(ConstantesBD.acronimoNo)){
									
									forma.setHabilitarCalcTarifa(ConstantesBD.acronimoSi);
								}
								
							}else if(servicio.getHabilitarSeleccion().equals(ConstantesBD.acronimoSi)){
								
								forma.setHabilitarCalcTarifa(ConstantesBD.acronimoSi);
							}
						}
					}
				}
				
				//forma.setServiciosOdon(servicioOdoOrden);
				forma.setOtrosServiciosOdon (servicioOdoOrden);
				
			}else{
				
				// se ordena por pieza los servicios a mostrar
				SortServiciosOdontologicos sort= new SortServiciosOdontologicos();
				sort.setPatronOrdenar(forma.getPatronOrdenarSerOdo());
				forma.setOtrosServiciosOdon(listaServicios);
				Collections.sort(forma.getOtrosServiciosOdon(), sort);
			}
		
			Connection con = UtilidadBD.abrirConexion();
			
			if(forma.getPosAgendaSel() >= 0 && forma.getAgendaOdon().get(forma.getPosAgendaSel())!=null)
			{
				Log4JManager.info("codigo unidad agenda: "+forma.getAgendaOdon().get(forma.getPosAgendaSel()).getUnidadAgenda());
				unidadAgenda=forma.getAgendaOdon().get(forma.getPosAgendaSel()).getUnidadAgenda();
			}
			
			// Se cargan los centros de costo de la unidad de agenda seleccionada
			forma.setListCentrosCostoXUniAgen(obtenerCentrosCostoXUniAgenda(con, unidadAgenda+"",forma.getCentroAtencion()));
			//Si solo hay un centro de costo x unidad de agenda se asigna automáticamente
			if(forma.getNumListCentrosCostoXUniAgen()==1)
			{
				forma.setCodCentroCostoXuniAgen(Integer.parseInt(forma.getListCentrosCostoXUniAgen().get(0).get("codigo").toString()));
			}
			
			UtilidadBD.closeConnection(con);
		}

		
		/*
		 * Se valida si el usuario en sesion tiene permisos para Modificar la duración de los servicios
		 */
		forma.setPermiteModificarDuracionServicio(false);
	
		if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), ConstantesBD.codigoFuncionalidadModificarDuracionServicio)){
			
			forma.setPermiteModificarDuracionServicio(true);
		}
		
		/*
		 * Este caso implica que los servicios que estan asociados a la cita creada anteriormente
		 * sean exactamente los mismos que se van a postular para la nueva cita.
		 */
		if (forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoRemisionInterconsulta) || "citaRecordNoProgramada".equals(forma.getCasoCita()) ){

			forma.setServiciosOdon(forma.getOtrosServiciosOdon());
			forma.setInhabilitaOtrosServicios(true);
				
		}else if(forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTratamiento)){
			
			if("citaSinServiciosProg".equals(forma.getCasoCita())){
				
				forma.setServiciosOdon(forma.getOtrosServiciosOdon());
				forma.setInhabilitaOtrosServicios(true);
				
			}else{
				
				actualizarServiciosDesdeOtros(forma, usuario, "confirmarOtrosServicios");
			}
		
		}else if(forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoControlCitaOdon)){
			
			if("citaSinServiciosProg".equals(forma.getCasoCita())){
				
				actualizarServiciosDesdeOtros(forma, usuario, "inicializarListadoServiciosCita");
				
			}else{
				
				actualizarServiciosDesdeOtros(forma, usuario, "confirmarOtrosServicios");
			}
		
		}else if(!forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoRemisionInterconsulta) && !forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTratamiento)){
		
			actualizarServiciosDesdeOtros(forma, usuario, "inicializarListadoServiciosCita");
		}
	}

	/**
	 * Método para controlar el flujo al seleccionar un cupo de la agenda
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param mundo 
	 * @param request 
	 * @param persona 
	 * @param valideCupos
	 * @return ActionForward con la página de respuesta
	 */
	private ActionForward accionProcesarAgenda(AgendaOdontologicaForm forma,
			ActionMapping mapping, UsuarioBasico usuario, AgendaOdontologica mundo, 
			HttpServletRequest request, PersonaBasica persona, boolean valideCupos, HttpServletResponse response) throws IPSException 
	{
		ActionErrors errores=new ActionErrors();

		for(int i=0;i<forma.getAgendaOdon().size();i++)
		{
			DtoAgendaOdontologica dtoAgenda=forma.getAgendaOdon().get(i);
			if(dtoAgenda.getCodigoPk()==forma.getCodigoAge())
			{
				if(dtoAgenda.getCodigoMedico()==persona.getCodigoPersona())
				{
					errores.add("paciente mismo medico atencion", new ActionMessage("errors.notEspecific", "El paciente es el mismo profesional de la salud"));
					saveErrors(request, errores);
					forma.setAsignar(ConstantesBD.acronimoNo);
					forma.setReserva(ConstantesBD.acronimoNo);
					return mapping.findForward("menuCitaOdontologica");
				}
				forma.setPosAgendaSel(i);
				forma.setAgendaSel(dtoAgenda);
			}
		}

//		Connection con = UtilidadBD.abrirConexion();
//		//Se identifican los permisos del usuario
//		
//		UtilidadBD.closeConnection(con);

		//Se calcula la duración
		forma.setDuracionCita(UtilidadFecha.numeroMinutosEntreFechas(forma.getFecha(), forma.getHoraInicio(), forma.getFecha(), forma.getHoraFinal()));
		
		int n=forma.getAgendaOdon().size();
		if(!valideCupos)
		{
			for(int i=0;i<n; i++)		
			{
				DtoAgendaOdontologica dtoAgenda=forma.getAgendaOdon().get(i);
				int m=dtoAgenda.getCitasOdontologicas().size();
				boolean agregueError=false;
				cicloCitas:for(int j=0;j<m;j++)
				{
					if(forma.getCodigoAge()==dtoAgenda.getCodigoPk())
					{
						DtoCitaOdontologica cita=dtoAgenda.getCitasOdontologicas().get(j);
						Log4JManager.info(cita.getHoraInicio());
						Log4JManager.info(cita.getHoraFinal());
						if(	forma.getHoraInicio().compareTo(cita.getHoraInicio())>=0 
								&& 
								forma.getHoraInicio().compareTo(cita.getHoraFinal())<0
							)
								
						{
							if(	cita.getEstado().equals(ConstantesIntegridadDominio.acronimoReservado) || 
								cita.getEstado().equals(ConstantesIntegridadDominio.acronimoAsignado))
							{
								if(cita.getCodigoPaciente() == persona.getCodigoPersona())
								{
									if(cita.getEstado().equals(ConstantesIntegridadDominio.acronimoReservado))
									{
										agregueError=true;
										errores.add("horacita",new ActionMessage("errors.notEspecific", "Ya existe una cita reservada para ese paciente en el horario seleccionado."));
									}
									else if(cita.getEstado().equals(ConstantesIntegridadDominio.acronimoAsignado))
									{
										agregueError=true;
										errores.add("horacita",new ActionMessage("errors.notEspecific", "Ya existe una cita asignada para ese paciente en el horario seleccionado."));
									}
								}
								else
								{
									Utilidades.imprimirMapa(dtoAgenda.getCuposEspacioTiempo());
									Integer cupos=dtoAgenda.getCuposEspacioTiempo().get(cita.getHoraInicio());
									if(cupos!=null && cupos<=0)
									{
										agregueError=true;
										errores.add("horacita",new ActionMessage("errors.notEspecific", "No hay cupos disponibles en el horario seleccionado."));
									}
									else
									{
										forma.setCuposExtra(ConstantesBD.acronimoSi);
										return mapping.findForward("menuCitaOdontologica");
									}
								}
							}
						}
						if(agregueError)
						{
							break cicloCitas;
						}
					}
				}
			}
		}
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			return mapping.findForward("menuCitaOdontologica");
		}
		
		/*
		 * Se analiza segï¿½n ciertos parï¿½metros de donde fue invocada el proceso de agendamiento para saber 
		 * que mostrar.
		 * Si posicionCita y codigoActividad estï¿½n lllenos quiere decir que se estï¿½ procesando una cita delr ecord de citas
		 * Si posicionCita = -1 y codigoActividad vacï¿½o lo que se desea hacer es crear una nueva cita
		 */
		Log4JManager.info("POS CITA >> "+forma.getPosicionCita() +"   codActividad >> "+forma.getCodigoActividad());
		if(forma.getPosicionCita()>=0 && !forma.getCodigoActividad().equals(""))
		{
			Log4JManager.info("ENTRAAAAAAAAAA POR ESTE FLUJO   EXISTE CITAAA ....");
			Log4JManager.info("Hora Inicial CITA "+forma.getCitasOdonPac().get(forma.getPosicionCita()).getHoraInicio());
			
			for(int i=0;i<forma.getAgendaOdon().size();i++)
			{
				DtoAgendaOdontologica dtoAgenda=forma.getAgendaOdon().get(i);
				Log4JManager.info("codigo agenda arreglo["+i+"]: "+dtoAgenda.getCodigoPk());
				if(dtoAgenda.getCodigoPk()==forma.getCodigoAge())
				{
					
					forma.setPosAgendaSel(i);
					forma.setAgendaSel(dtoAgenda);					
				}
			}
			
			Log4JManager.info("codigo de la Cita: "+forma.getCitasOdonPac().get(forma.getPosicionCita()).getCodigoPk());
			
			Log4JManager.info("codigo de la agenda: "+forma.getCodigoAge());
			Log4JManager.info("posicion de la agenda: "+forma.getPosAgendaSel());
			Log4JManager.info("unidad de agenda a validar: "+forma.getAgendaSel().getUnidadAgenda());
			Log4JManager.info("centro atencion a validar: "+forma.getAgendaSel().getCentroAtencion());
			Log4JManager.info("login usuario: "+usuario.getLoginUsuario());
			Log4JManager.info("actividad asignar: "+ConstantesBD.codigoActividadAutorizadaAsignarCitas);
			Log4JManager.info("actividad reservar: "+ConstantesBD.codigoActividadAutorizadaReservarCitas);
			
			Log4JManager.info("Codigo Agenda Anterior "+forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgenda()+"  ï¿½  "+forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon().getCodigoPk());
			Log4JManager.info("Codigo Agenda Nueva "+forma.getAgendaSel().getCodigoPk());	
			Log4JManager.info("CUPOS Agenda Anterior "+forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon().getCupos());
			Log4JManager.info("CUPOS Agenda Nueva "+forma.getAgendaSel().getCupos());
			
			Log4JManager.info("Codigo Unidad Agenda Anterior "+forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon().getUnidadAgenda());
			Log4JManager.info("Codigo Unidad Agenda Nueva"+forma.getAgendaSel().getUnidadAgenda());
			
			
			if(forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoReprogramar))
			{
				Log4JManager.info("REPROGRAMAR CITA  ..."); 
				forma.setReprogramar(ConstantesBD.acronimoSi);	
				forma.setFechaReprogramacion(forma.getFecha());
				
				//accionProcesarAgenda(forma,mapping,usuario,mundo,request,persona, false, response);
				return accionIniciarReservar(forma, usuario, mapping, persona, response, request, mundo);
				
			}
			
			String estadoCita = forma.getCitasOdonPac().get(forma.getPosicionCita()).getEstado();
			
			if(estadoCita.equals(ConstantesIntegridadDominio.acronimoProgramado))
			{
				Log4JManager.info("CITA PROGRAMADA  ..."); 
				
				if(forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoTipoFuncionalidadReservar))
					return accionIniciarReservar(forma, usuario, mapping, persona, response, request, mundo);
				else if(forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoTipoFuncionalidadAsignar))
					return accionIniciarAsignar(forma, usuario, mapping, request, response, persona);				
			}
			
			if(forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoTipoFuncionalidadReservar))
			{
				forma.setReprogramar(ConstantesBD.acronimoNo);
				if(forma.getTipoFlujoPaciente().equals(ConstantesBD.acronimoNo))
				{
					return accionIniciarReservar(forma, usuario, mapping, persona, response, request, new AgendaOdontologica());
				}
			}
			
			if(forma.getCodigoActividad().equals(ConstantesBD.codigoActividadAutorizadaAsignarCitas) || forma.getCodigoActividad().equals(ConstantesBD.codigoActividadAutorizadaReservarCitas) )
			{
				return mapping.findForward("menuCitaOdontologica");
			}	
		}
		//NUEVA CITA
		else
		{		
			//Se ubica la agenda odontologica seleccionada
			for(int i=0;i<forma.getAgendaOdon().size();i++)
			{
				Log4JManager.info("codigo agenda arreglo["+i+"]: "+forma.getAgendaOdon().get(i).getCodigoPk());
				if(forma.getAgendaOdon().get(i).getCodigoPk()==forma.getCodigoAge())
				{
					forma.setPosAgendaSel(i);
					forma.setAgendaSel(forma.getAgendaOdon().get(i));
				}
			}
			
			//Log4JManager.info("codigo de la Cita: "+forma.getCitasOdonPac().get(forma.getPosicionCita()).getCodigoPk());
		    Log4JManager.info("Codigo Cuenta > "+persona.getCodigoCuenta());				
			Log4JManager.info("codigo de la agenda: "+forma.getCodigoAge());
			Log4JManager.info("posicion de la agenda: "+forma.getPosAgendaSel());
			Log4JManager.info("unidad de agenda a validar: "+forma.getAgendaSel().getUnidadAgenda());
			Log4JManager.info("centro atencion a validar: "+forma.getAgendaSel().getCentroAtencion());
			Log4JManager.info("login usuario: "+usuario.getLoginUsuario());
			Log4JManager.info("actividad asignar: "+ConstantesBD.codigoActividadAutorizadaAsignarCitas);
			Log4JManager.info("actividad reservar: "+ConstantesBD.codigoActividadAutorizadaReservarCitas);
			
			if(!UtilidadTexto.getBoolean(forma.getAsignar())&&!UtilidadTexto.getBoolean(forma.getReserva()))
			{
				//forma.setAttribute("ocultarEncabezadoErrores", "true");
				//request.setAttribute("descripcionError", "No tiene permisos para flujo y asignar citas. Por favor verifique");
				request.setAttribute("ocultarEncabezadoErrores", "true");
				request.setAttribute("descripcionError", "No tiene permisos para reservar y asignar citas. Por favor verifique");
				request.setAttribute("utilizaParent", true);
				return mapping.findForward("paginaError");
			}
			else if(UtilidadTexto.getBoolean(forma.getAsignar()) || UtilidadTexto.getBoolean(forma.getReserva()))
			{
				//return mapping.findForward("menuCitaOdontologica");
			}
		}
		return null;
	}

	
	/**
	 * Método implementado para realizar la busqueda de la agenda odontologica
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param mundo 
	 * @return
	 */
	private ActionForward accionRealizarBusqueda(AgendaOdontologicaForm forma,
			UsuarioBasico usuario, ActionMapping mapping,
			HttpServletRequest request) 
	{
		String estado=forma.getEstado();
		if(estado.equals("realizarBusquedaAtras"))
		{
			forma.setFecha(UtilidadFecha.incrementarDiasAFecha(forma.getFecha(), -1, false));
		}
		else if(estado.equals("realizarBusquedaAdelante"))
		{
			forma.setFecha(UtilidadFecha.incrementarDiasAFecha(forma.getFecha(), 1, false));
		}
		
		forma.setMostrarDesplazarFecha(true);
		Connection con = UtilidadBD.abrirConexion();
		
		if(!forma.getUnidadAgenda().equals("Seleccione")){
			
			// se consulta las agendas odontologicas segun el parametro de seleccion
			forma.setAgendaOdon(AgendaOdontologica.cosultarAgendaOdontologica(con, 
						forma.getCentroAtencion()+"",
						forma.getUnidadAgenda(),
						forma.getProfesionalSalud(), // Como no es requerido, se puede enviar el valor Seleccione, esto se válida en la consulta
						forma.getFecha(),
						forma.getTipoCita()));
			
			// se carga el listado de conceviones de colores de las unidades de agendas
			forma.setConvencionColores(AgendaOdontologica.convencionColorUnidaAgenda(con, forma.getUnidadAgenda(),forma.getFecha()));
			
			forma.setFechaDesplazamiento(forma.getFecha());
			// se carga los consultorios para el manejo de la interfaz de la agenda odontologica
			String[] horaini = {""};
			String[] horafin = {""};
			forma.setConsultoriosAgenOdon(AgendaOdontologica.consultoriosAgendaOdontologica(forma.getAgendaOdon(),
					horaini, horafin, forma.getFecha()));
			forma.setHoraIniAgenOdon(horaini[0]);
			forma.setHoraFinAgenOdon(horafin[0]);
			
			// lista con los intervalos de horas ya definidos
			/*
			forma.setIntervaloHora(intevaloEntreHoraIniFin(forma.getHoraIniAgenOdon(), 
					forma.getHoraFinAgenOdon(),
					Utilidades.convertirAEntero(forma.getMultiploMinGenCitas())));
			*/
			
		}
		
		// se generan los string xml
		if(forma.getAgendaOdon()!=null && forma.getAgendaOdon().size()>0)
		{
			forma.setXmlAgenda(generacionXML(forma, usuario, con));
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("agendaOdo");
		}
		else
		{
			/*
			 * En este caso el elemento flash muestra el error, con el 
			 * fin de que permita la navegaciï¿½n en ves de mostrar el
			 * mensaje de error y quite las felchitas
			 */
			forma.setXmlAgenda("");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("agendaOdo");
		}
	}

	/**
	 * Método que abre la ventana de los criterios de busqueda
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionCriteriosBusqueda(AgendaOdontologicaForm forma,
			UsuarioBasico usuario, ActionMapping mapping, PersonaBasica persona, HttpServletRequest request) 
	{
		
		//forma.setConfirmacionCupoExtra(false);

		String recargarCriteriosBusqueda = forma.getRecargarCriteriosBusqueda();
		
		if (recargarCriteriosBusqueda==null || recargarCriteriosBusqueda.equals(ConstantesBD.acronimoNo)
				|| recargarCriteriosBusqueda.equals("")){
			
			if(forma.getPosicionCita() >= 0 && forma.getCitasOdonPac().get(forma.getPosicionCita()).getEstado().equals(ConstantesIntegridadDominio.acronimoProgramado))
			{
				forma.setDtoCitaProgramada(forma.getCitasOdonPac().get(forma.getPosicionCita()));
			}
			
			ActionErrors errores=new ActionErrors();
			Connection con = UtilidadBD.abrirConexion();
			String fechaSistema=UtilidadFecha.getFechaActual();
			
			if(forma.getPosicionCita() < 0 /*|| !forma.getReprogramar().equals(ConstantesBD.acronimoSi)*/)
			{
				forma.setReprogramar(ConstantesBD.acronimoNo);	
				forma.resetCriteriosBusqueda();
			}
			if(forma.getCiudad().equals(""))
			{
				errores.add("", new ActionMessage("errors.required","La ciudad"));
				
			}
			if(forma.getCentroAtencion()<=0)
			{
				errores.add("", new ActionMessage("errors.required","El centro de atención"));
			}
			if(forma.getTipoCita().equals(""))
			{
				errores.add("", new ActionMessage("errors.required","El tipo de cita"));
			}
			if(forma.getUnidadAgenda().equals(""))
			{
				errores.add("", new ActionMessage("errors.required","La unidad de agenda"));
			}
			if(forma.getFecha().trim().equals(""))
			{
				errores.add("", new ActionMessage("errors.required","La fecha"));
			}
			else if(!UtilidadFecha.validarFecha(forma.getFecha()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido",""));
			}
			else if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getFecha(), fechaSistema))
			{
				errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual",""," ("+fechaSistema+")"));
			}
			
			forma.setPaises(UtilidadesConsultaExterna.obtenerPaisesPermitidosXUsuarioXUnidadAgendaXCentroAtencion(con, usuario.getLoginUsuario(),ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica));

			if(forma.getCitaConfirmacion() !=null  && !"".equals(forma.getCitaConfirmacion().getEstado()) 
					&& !(ConstantesIntegridadDominio.acronimoProgramado).equals(forma.getCitaConfirmacion().getEstado())){
				
				forma.setPais(forma.getCitaConfirmacion().getCodpais());
				forma.setCiudad(forma.getCitaConfirmacion().getCodciudad());
				forma.setUnidadAgenda(forma.getCitaConfirmacion().getAgendaOdon().getUnidadAgenda()+"");
				forma.setProfesionalSalud(forma.getCitaConfirmacion().getAgendaOdon().getCodigoMedico()+"");
				
				accionCargarPaises(con,forma,usuario,mapping);
				
				if(!forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoReprogramar))
				{
					forma.setInhabilitaPais(true);
					forma.setInhabilitaCiudad(true);
					forma.setInhabilitaCentroAtencion(true);
					forma.setInhabilitaUnidadAgenda(true);
				}
				
			}else{
				
				accionCargarPaises(con,forma,usuario,mapping);
			}

			//*********Validaciones del tipo de cita**************************
			
			if(forma.getTiposCita().size() <=0 )
			{
				forma.setTipoCita("");
				ArrayList<String> estados = new ArrayList<String>();
				ArrayList<BigDecimal> codigosPlanT = new ArrayList<BigDecimal>();
				
				//TIPO CITA AUDITORIA (debe tener al menos un plan de tratamiento sin importar el estado)
				
				
				//codigosPlanT =  PlanTratamiento.obtenerCodigoPlanTratamiento(forma.getPaciente().getCodigo(), null, ConstantesBD.acronimoNo);
				if(obtenerCodigoPlanTratamientoPorEstados(forma, null) > 0) //codigosPlanT.size()>0
				{
					forma.getTiposCita().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoAuditoria,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoAuditoria)+""));
				}
				
				if(ValoresPorDefecto.getLasCitasDeControlSePermitenAsignarA(usuario.getCodigoInstitucionInt()).equals(ConstantesIntegridadDominio.acronimoSoloPacienteConPlanTratamientoTerminado))
				{
					if(UtilidadOdontologia.pacienteConPlanTratamientoEnEstado(con,ConstantesIntegridadDominio.acronimoTerminado,persona.getCodigoPersona()))
					{
						forma.getTiposCita().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoControlCitaOdon,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoControlCitaOdon)+""));
					}
				}
				else
				{
					//TIPO CITA CONTROL (Cita de tipo Control: Se permite selección de tipo cita Control a cualquier tipo de paciente.)
					forma.getTiposCita().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoControlCitaOdon,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoControlCitaOdon)+""));
				}
				
				if(UtilidadOdontologia.validarTipoCitaInterconsulta(persona.getCodigoCuenta(), persona.getCodigoCuentaAsocio()))
				{
					Log4JManager.info("\n>>>>>>>>>>>>> TIPO INTERCONSULTAAAAAAAAAAAAAA >>> ");
					forma.getTiposCita().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoRemisionInterconsulta,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoRemisionInterconsulta)+""));
				}
				
				//TIPO CITA PRIORITARIA (aplica siempre)
				forma.getTiposCita().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoPrioritaria,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoPrioritaria)+""));
				
				//TIPO CITA REVALORACION (debe tener plan de tratamiento inactivo)
				estados = new ArrayList<String>();
				estados.add(ConstantesIntegridadDominio.acronimoInactivo);
				//codigosPlanT =  PlanTratamiento.obtenerCodigoPlanTratamiento(forma.getPaciente().getCodigo(), estados, ConstantesBD.acronimoNo);
				
				if(obtenerCodigoPlanTratamientoPorEstados(forma, estados) > 0)//codigosPlanT.size()>0
				{
					forma.getTiposCita().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoRevaloracion,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoRevaloracion)+""));
				}
				
				//TIPO CITA TRATAMIENTO (debe tener plan de tratamiento vigente)
				estados = new ArrayList<String>();
				if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValidaPresupuestoOdoContratado(usuario.getCodigoInstitucionInt())))
				{
					estados = new ArrayList<String>();
					estados.add(ConstantesIntegridadDominio.acronimoContratadoContratado);
					DtoPresupuestoOdontologico dtoPresupuestoOdontologico=new DtoPresupuestoOdontologico();
					dtoPresupuestoOdontologico.setCodigoPaciente(new InfoDatosInt(forma.getPaciente().getCodigo()));
					if(PresupuestoOdontologico.cargarPresupuesto(dtoPresupuestoOdontologico, estados).size()>0)
					{
						forma.getTiposCita().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoTratamiento,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTratamiento)+""));
					}
				}
				else
				{
					estados.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
					estados.add(ConstantesIntegridadDominio.acronimoEstadoEnProceso);
					//codigosPlanT =  PlanTratamiento.obtenerCodigoPlanTratamiento(forma.getPaciente().getCodigo(), estados, ConstantesBD.acronimoNo);
					if(obtenerCodigoPlanTratamientoPorEstados(forma, estados) > 0)//codigosPlanT.size()>0
					{
						forma.getTiposCita().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoTratamiento,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTratamiento)+""));
					}
				}
				estados = new ArrayList<String>();
				estados.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
				
				//codigosPlanT =  PlanTratamiento.obtenerCodigoPlanTratamiento(forma.getPaciente().getCodigo(), estados, ConstantesBD.acronimoNo);
				if(obtenerCodigoPlanTratamientoPorEstados(forma, estados) == 0)//codigosPlanT.size()==0 Solamente se puede tener un plan de tratamiento contratado
				{
					estados = new ArrayList<String>();
					estados.add(ConstantesIntegridadDominio.acronimoTerminado);
					estados.add(ConstantesIntegridadDominio.acronimoEstadoCancelado);
					estados.add(ConstantesIntegridadDominio.acronimoInactivo);
		
					//codigosPlanT =  PlanTratamiento.obtenerCodigoPlanTratamiento(forma.getPaciente().getCodigo(), estados, ConstantesBD.acronimoNo);
					if(obtenerCodigoPlanTratamientoPorEstados(forma, estados) > 0) //codigosPlanT.size()>0
					{
						forma.getTiposCita().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial)+""));				
					}
					else
					{
						estados = new ArrayList<String>();
						estados.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
						codigosPlanT =  PlanTratamiento.obtenerCodigoPlanTratamiento(forma.getPaciente().getCodigo(), estados, ConstantesBD.acronimoNo);
						if(codigosPlanT.size()==1)// Solamente se puede tener un plan de tratamiento contratado
						{
							if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValidaPresupuestoOdoContratado(usuario.getCodigoInstitucionInt())))
							{
								//TIPO CITA VALORACION INICIAL
								estados = new ArrayList<String>();
								estados.add(ConstantesIntegridadDominio.acronimoContratadoContratado);
								DtoPresupuestoOdontologico dtoPresupuestoOdontologico=new DtoPresupuestoOdontologico();
								dtoPresupuestoOdontologico.setPlanTratamiento(codigosPlanT.get(0));
								if(PresupuestoOdontologico.cargarPresupuesto(dtoPresupuestoOdontologico, estados).size()<=0)
								{
									forma.getTiposCita().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial)+""));
								}
							}
							else
							{
								forma.getTiposCita().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial)+""));
							}
						}
						else
						{
							estados = new ArrayList<String>();
							estados.add(ConstantesIntegridadDominio.acronimoInactivo);
							estados.add(ConstantesIntegridadDominio.acronimoTerminado);
							estados.add(ConstantesIntegridadDominio.acronimoCancelar);
							//codigosPlanT =  PlanTratamiento.obtenerCodigoPlanTratamiento(forma.getPaciente().getCodigo(), estados, ConstantesBD.acronimoNo);
							if(obtenerCodigoPlanTratamientoPorEstados(forma, estados)>0)
							{
								forma.getTiposCita().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial)+""));
							}
							else
							{
								//codigosPlanT =  PlanTratamiento.obtenerCodigoPlanTratamiento(forma.getPaciente().getCodigo(), null, ConstantesBD.acronimoNo);
								if(obtenerCodigoPlanTratamientoPorEstados(forma, null)==0)
								{
									forma.getTiposCita().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial)+""));
								}
							}
						}
					}
				}
				else
				{
					estados = new ArrayList<String>();
					estados.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
					codigosPlanT =  PlanTratamiento.obtenerCodigoPlanTratamiento(forma.getPaciente().getCodigo(), estados, ConstantesBD.acronimoNo);
					if(codigosPlanT.size()==1)// Solamente se puede tener un plan de tratamiento contratado
					{
						if(UtilidadTexto.getBoolean(ValoresPorDefecto.getValidaPresupuestoOdoContratado(usuario.getCodigoInstitucionInt())))
						{
							//TIPO CITA VALORACION INICIAL
							estados = new ArrayList<String>();
							estados.add(ConstantesIntegridadDominio.acronimoContratadoContratado);
							DtoPresupuestoOdontologico dtoPresupuestoOdontologico=new DtoPresupuestoOdontologico();
							dtoPresupuestoOdontologico.setPlanTratamiento(codigosPlanT.get(0));
							if(PresupuestoOdontologico.cargarPresupuesto(dtoPresupuestoOdontologico, estados).size()<=0)
							{
								forma.getTiposCita().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial)+""));
							}
						}
						else
						{
							forma.getTiposCita().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial)+""));
						}
					}
					else
					{
						estados = new ArrayList<String>();
						estados.add(ConstantesIntegridadDominio.acronimoInactivo);
						estados.add(ConstantesIntegridadDominio.acronimoTerminado);
						estados.add(ConstantesIntegridadDominio.acronimoCancelar);
						//codigosPlanT =  PlanTratamiento.obtenerCodigoPlanTratamiento(forma.getPaciente().getCodigo(), estados, ConstantesBD.acronimoNo);
						if(obtenerCodigoPlanTratamientoPorEstados(forma, estados)>0)
						{
							forma.getTiposCita().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial)+""));
						}
						else
						{
							//codigosPlanT =  PlanTratamiento.obtenerCodigoPlanTratamiento(forma.getPaciente().getCodigo(), null, ConstantesBD.acronimoNo);
							if(obtenerCodigoPlanTratamientoPorEstados(forma, null)==0)
							{
								forma.getTiposCita().add(new InfoDatosString(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial)+""));
							}
						}
					}
				}

				if(forma.getCitaConfirmacion() !=null  && !"".equals(forma.getCitaConfirmacion().getEstado())){
					
					forma.setTipoCita(forma.getCitaConfirmacion().getTipo());
					forma.setInhabilitaTipoCita(true);
				}
			}
			
			//*****************************************************************
			//forma.setFecha(UtilidadFecha.getFechaActual());
			// modificado por cristhian
			//Se toma el estado de la cita
			//if(forma.getCitasOdonPac().get(forma.getPosicionCita()).getEstado() != null) 

			String fechaSistemaPostulada = UtilidadFecha.getFechaActual();
			
			if(forma.getCitasOdonPac() != null && forma.getCitasOdonPac().size()>0)
			{
				if(forma.getPosicionCita() != ConstantesBD.codigoNuncaValido && forma.getCitasOdonPac().get(forma.getPosicionCita()) != null){
					
					String estadoCita = forma.getCitaConfirmacion().getEstado();
				
					if(estadoCita.equals(ConstantesIntegridadDominio.acronimoProgramado)){
						
						String fechaProgramacion = UtilidadFecha.conversionFormatoFechaAAp(forma.getCitasOdonPac().get(forma.getPosicionCita()).getFechaProgramacion()) ;
											
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaProgramacion, UtilidadFecha.getFechaActual())){
							
							fechaSistemaPostulada = fechaProgramacion;
						}
					}
				}
			}
			forma.setFecha(fechaSistemaPostulada);
			
			// modificado por cristhian
			
			UtilidadBD.closeConnection(con);
			
		}
		
		return mapping.findForward("criteriosBusqueda");
	}



	/**
	 * Mï¿½todo implementado para cargar los datos por cambio de pais
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping
	 */
	private void accionCargarPaises(Connection con,
			AgendaOdontologicaForm forma, UsuarioBasico usuario,
			ActionMapping mapping) 
	{
		forma.setCentroAtencion(0);
		forma.setCiudad("");
		forma.setCiudades(new ArrayList<HashMap<String,Object>>());
		forma.setListCentroAtencion(new ArrayList<HashMap<String,Object>>());
		forma.setListUnidadesAgendaXUsuario(new ArrayList<HashMap>());
		forma.setListProfesionalesActivos(new ArrayList<HashMap<String,Object>>());
		
		if(forma.getCitaConfirmacion() !=null  &&  !"".equals(forma.getCitaConfirmacion().getEstado())  && !(ConstantesIntegridadDominio.acronimoProgramado).equals(forma.getCitaConfirmacion().getEstado())){
			
			if(forma.getPais()==""){
				
				forma.setPais(forma.getCitaConfirmacion().getCodpais());
			}
		
			Log4JManager.info("************************************************************************************ " + forma.getCitaConfirmacion().getCodpais() + " se seleccionó una cita " + forma.getCitaConfirmacion().getEstado());

		}else{
			
			Log4JManager.info("************************************************************************************ no hay cita " );	
		}
		
		if(forma.getNumPaises()==1)
		{
			forma.setCiudades(UtilidadesConsultaExterna.obtenerCiudadesPermitidasXUsuarioXUnidadAgendaXCentroAtencion(
					con,
					usuario.getLoginUsuario(), 
					forma.getPaises().get(0).get("codigo").toString(), 
					ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica)
				);
			accionCargarCiudades(con, forma, mapping, usuario);
		}
		else
		{
			if(forma.getPais()!=null &&  !forma.getPais().equals(""))
			{
				forma.setCiudad("");
				forma.setCiudades(UtilidadesConsultaExterna.obtenerCiudadesPermitidasXUsuarioXUnidadAgendaXCentroAtencion(
						con,
						usuario.getLoginUsuario(), 
						forma.getPais(), 
						ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica)
					);
				accionCargarCiudades(con,forma,mapping,usuario);
			}
			else
			{
				forma.setCiudades(new ArrayList<HashMap<String,Object>>());
				forma.setListCentroAtencion(new ArrayList<HashMap<String,Object>>());
				forma.setListUnidadesAgendaXUsuario(new ArrayList<HashMap>());
				forma.setListProfesionalesActivos(new ArrayList<HashMap<String,Object>>());
			}
			
			
		}
		
	}

	/**
	 * Método implementado para validar los parï¿½metros de la secciï¿½n criterios de bï¿½squeda
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionValidarBusqueda(AgendaOdontologicaForm forma,
			UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		
		ActionErrors errores = new ActionErrors();
		
		String fechaSistema = UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(),Utilidades.convertirAEntero(ValoresPorDefecto.getNumDiasAntFActualAgendaOd(usuario.getCodigoInstitucionInt()),true)*-1, false);
		
		Log4JManager.info("---------------------------------------------- Entre a validar la fecha en el action -----------------------------------");
		
		
		//**************VALIDACIÓN DE CAMPOS REQUERIDOS**********************************+
		if(forma.getPais().equals(""))
		{
			errores.add("", new ActionMessage("errors.required","El país"));
		}
		if(forma.getCiudad().equals(""))
		{
			errores.add("", new ActionMessage("errors.required","La ciudad"));
			
		}
		if(forma.getCentroAtencion()<=0)
		{
			errores.add("", new ActionMessage("errors.required","El centro de atención"));
		}
		if(forma.getTipoCita().equals(""))
		{
			errores.add("", new ActionMessage("errors.required","El tipo de cita"));
		}
		if(forma.getUnidadAgenda().equals(""))
		{
			errores.add("", new ActionMessage("errors.required","La unidad de agenda"));
		}
		if(forma.getFecha().trim().equals(""))
		{
			errores.add("", new ActionMessage("errors.required","La fecha"));
		}
		else if(!UtilidadFecha.validarFecha(forma.getFecha()))
		{
			errores.add("", new ActionMessage("errors.formatoFechaInvalido",""));
			forma.setFecha(UtilidadFecha.getFechaActual());
			
		}else if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFecha(), fechaSistema))
		{
			errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual","La Fecha del rango de búsqueda"," ("+fechaSistema+")"));
			forma.setFecha(UtilidadFecha.getFechaActual());
		}
		
		//***********************************************************************************
		
		if(!errores.isEmpty()){
			
			saveErrors(request, errores);
			forma.setEstado("criteriosBusqueda");
			return mapping.findForward("erroresAJAX");
		}
		
		forma.setRecargarCriteriosBusqueda("");
		
		return accionRealizarBusqueda(forma, usuario, mapping, request);
	}

	/**
	 * Método implementado para cargar las ciudades
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 */
	private void accionCargarCiudades(Connection con,
			AgendaOdontologicaForm forma, ActionMapping mapping,
			UsuarioBasico usuario) 
	{
		
		
		if(forma.getNumCiudades()==1)
		{
			forma.setListCentroAtencion(UtilidadesConsultaExterna.obtenerCentrosAtencionPermitidosUsuarioXCiudad(con, 
				forma.getCiudades().get(0).get("codigoPais").toString(), 
				forma.getCiudades().get(0).get("codigoCiudad").toString(), 
				forma.getCiudades().get(0).get("codigoDepto").toString(), 
				usuario.getLoginUsuario(), 
				ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica));
			
			accionCargarCentrosAtencion(con, forma, usuario, mapping);
		}
		else
		{
			if(!forma.getCiudad().equals(""))
			{
				forma.setCentroAtencion(ConstantesBD.codigoNuncaValido);
				
				String[] datosCiudad = forma.getCiudad().split(ConstantesBD.separadorSplit);
				
				forma.setListCentroAtencion(UtilidadesConsultaExterna.obtenerCentrosAtencionPermitidosUsuarioXCiudad(con, 
						forma.getPais(), 
						datosCiudad[1], 
						datosCiudad[0], 
						usuario.getLoginUsuario(), 
						ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica));
				
				accionCargarCentrosAtencion(con,forma,usuario,mapping);
			}
			else
			{
				forma.setListCentroAtencion(new ArrayList<HashMap<String,Object>>());
				forma.setListUnidadesAgendaXUsuario(new ArrayList<HashMap>());
				forma.setListProfesionalesActivos(new ArrayList<HashMap<String,Object>>());
			}
		}
	}



	/**
	 * M&eacute;todo implementado para cargar los centros de atencion
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping
	 */
	private void accionCargarCentrosAtencion(Connection con, AgendaOdontologicaForm forma, UsuarioBasico usuario, ActionMapping mapping) 
	{
		
		String codigosServicio = obtenerCodigosServicios(forma , usuario.getCodigoInstitucionInt());

		if(forma.getCitaConfirmacion()!=null && !forma.getCitaConfirmacion().getEstado().equals("")
				&& forma.getCitaConfirmacion().getCodigoCentroAtencion() > 0){
			
			if(forma.getCentroAtencion() <= 0)
			{
				forma.setCentroAtencion(forma.getCitaConfirmacion().getCodigoCentroAtencion());
			}
		}
		
		if(forma.getNumCentrosAtencion()==1)
		{
	
			forma.setListUnidadesAgendaXUsuario(UtilidadesConsultaExterna.unidadesAgendaXUsuarioArray(con,usuario.getLoginUsuario(),Integer.parseInt(forma.getListCentroAtencion().get(0).get("codigo").toString()),ConstantesBD.codigoNuncaValido,ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica, codigosServicio));

			postularUnidadAgenda(forma);
		
			accionCargarProfesionalesSalud(con, forma, usuario, forma.getUnidadAgenda());
		}
		else if(forma.getCentroAtencion()>0)
		{
		
			forma.setListUnidadesAgendaXUsuario(UtilidadesConsultaExterna.unidadesAgendaXUsuarioArray(con,usuario.getLoginUsuario(),forma.getCentroAtencion(),ConstantesBD.codigoNuncaValido,ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica, codigosServicio));
			
			postularUnidadAgenda(forma);
			
			accionCargarProfesionalesSalud(con, forma, usuario, forma.getUnidadAgenda());
		}
		else
		{
			forma.setListUnidadesAgendaXUsuario(new ArrayList<HashMap>());
			forma.setListProfesionalesActivos(new ArrayList<HashMap<String,Object>>());
		}

	}

	/**
	 * M&eacute;todo que sirve para obtener los c&oacute;digos de los servicios
	 * asociados, dependiendo del tipo de cita seleccionado
	 * 
	 * 
	 * @param forma
	 * @param i 
	 * @return
	 */
	private String obtenerCodigosServicios(AgendaOdontologicaForm forma, int  codigoInstitucionInt) {
		
		
		Log4JManager.info("------------- " + forma.getCodCuentaPaciente());
		
		String codigosServicio = "";
		
		if(ConstantesIntegridadDominio.acronimoRemisionInterconsulta.equals(forma.getTipoCita())){
			
			ArrayList<DtoServicioOdontologico> servicios = UtilidadOdontologia.obtenerServiciosInterconsulta(forma.getCodCuentaPaciente(), codigoInstitucionInt, ConstantesBD.codigoNuncaValido);
			
			for (DtoServicioOdontologico servicio : servicios) {
				
				if(!"".equals(codigosServicio)){
					
					codigosServicio+= " , ";
				}
				
				codigosServicio += servicio.getCodigoServicio();
			}
		}else if(forma.getCitaConfirmacion()!=null){
			
			for (DtoServicioCitaOdontologica servicio : forma.getCitaConfirmacion().getServiciosCitaOdon()) {
				
				if(!"".equals(codigosServicio)){
					
					codigosServicio+= " , ";
				}
				
				codigosServicio += servicio.getServicio();
			}
		}
		
		return codigosServicio;
	}
	
	
	
	/**
	 * M&eacute;todo que se encarga de cargar los profesionales de la salud
	 * que cumplan con los criterios de b&uacute;squeda
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param mapping
	 */
	private void accionCargarProfesionalesSalud(Connection con,	AgendaOdontologicaForm forma, UsuarioBasico usuario, String unidadAgenda) 
	{
		if(!UtilidadTexto.isEmpty(unidadAgenda)){
			
			forma.setUnidadAgenda(unidadAgenda);

			int codigoEspecialidad = obtenerEspecialidadXUnidadAgenda(forma.getUnidadAgenda());
			
			forma.setProfesionalSalud("");
			ArrayList<HashMap<String, Object>> centrosCosto = new ArrayList<HashMap<String,Object>>();
			
			if(forma.getNumCentrosAtencion()==1)
			{
				centrosCosto =  UtilidadesManejoPaciente.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), "", false, Integer.parseInt(forma.getListCentroAtencion().get(0).get("codigo").toString()));
				//forma.setListProfesionalesActivos(UtilidadesAdministracion.obtenerProfesionalesAgendaOdontologica(con, usuario.getCodigoInstitucionInt(), codigoEspecialidad, centrosCosto));//(con, usuario.getCodigoInstitucionInt(), codigoEspecialidad   /*ConstantesBD.codigoNuncaValido*/, false, false, centrosCosto));
		
			}else if(forma.getCentroAtencion()>0){

				centrosCosto =  UtilidadesManejoPaciente.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), "", false, forma.getCentroAtencion());
			}
			
			forma.setListProfesionalesActivos(UtilidadesAdministracion.obtenerProfesionalesAgendaOdontologica (con, usuario.getCodigoInstitucionInt(), codigoEspecialidad, centrosCosto)) ;//(con, usuario.getCodigoInstitucionInt(), codigoEspecialidad, false, false, centrosCosto));
			
			if(forma.getListProfesionalesActivos()==null)
			{
				forma.setListProfesionalesActivos(new ArrayList<HashMap<String,Object>>());
			}
		}
	}
//
//	/**
//	 * M&eacute;todo que se encarga de devolver un arreglo con
//	 * las unidades de agenda disponibles para ese usuario, asociado a ese centro
//	 * de atenci&oacute;n y que tenga los servicios de la cita.
//	 * 
//	 * 
//	 * @param unidadesAgendaXUsuario
//	 * @return
//	 */
//	private ArrayList<HashMap> obtenerListadoUnidadesAgendaXUsuarioXServicio(HashMap unidadesAgendaXUsuario) {
//		
//		ArrayList<HashMap> listUnidadesAgendaXUsuario =  new ArrayList<HashMap>();
//		
//		if(unidadesAgendaXUsuario.size()>0)
//		{
//			Log4JManager.info("se va a insertar agendas en el listado ---------------------------------------------------**************************###########################################");
//			
////			for (HashMap unidadAgendaXUsuari : unidadesAgendaXUsuario) {
////				
////				listUnidadesAgendaXUsuario.add(unidadAgendaXUsuari);
////			}
//		}
//		return listUnidadesAgendaXUsuario;
//	}


	/**
	 * Mï¿½todo empleado para recargar las citas en el caso de que se haya hecho una selección
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param mundo
	 * @return
	 */
	private ActionForward accionRecargarCitas(AgendaOdontologicaForm forma,
			UsuarioBasico usuario, ActionMapping mapping, AgendaOdontologica mundo,HttpServletRequest request) 
	{
		Connection con = UtilidadBD.abrirConexion();
		if(forma.getTipoFlujoPaciente().equals(ConstantesBD.acronimoSi))
		{	
//		 forma.setCitasOdonPac(mundo.consultarCitasPaciente(con, usuario.getLoginUsuario(), forma.getPaciente().getCodigo(),UtilidadFecha.getFechaActual(con),UtilidadFecha.getHoraActual(con),Utilidades.convertirAEntero(ValoresPorDefecto.getMinutosEsperaAsignarCitasOdoCaducadas(usuario.getCodigoInstitucionInt()))));
		 UtilidadBD.closeConnection(con);
		 return mapping.findForward("principal");		
		}
		else
		{
			forma.setEstado("realizarBusquedaRango");
			return accionRealizarBusquedaXRango(con, forma, usuario, mapping, request,mundo);
		}
		
	}



	/**
	 * Mï¿½todo implementado para realizar el proceso de confirmaciï¿½n de cita
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param mundo
	 * @param request
	 * @return
	 */
	private ActionForward accionConfirmacionCita(AgendaOdontologicaForm forma,
			UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		Log4JManager.info("Se validan las actualizaviones---------------------------------------------------------------");
		Log4JManager.info("forma.getObservacionesConfirmacion() "+forma.getObservacionesConfirmacion().length());
		
		//**********Se realizan validaciones***********************
		if(forma.getTipoConfirmacion().equals(""))
		{
			errores.add("", new ActionMessage("errors.required","El indicativo"));
		}
		else if(forma.getTipoConfirmacion().equals(ConstantesBD.acronimoSi))
		{/*
			if(forma.getObservacionesConfirmacion().trim().equals(""))
			{
				errores.add("", new ActionMessage("errors.required","El campo observaciones"));
			}
			else if(forma.getObservacionesConfirmacion().length()>128)
			{
				errores.add("", new ActionMessage("errors.maxlength","El campo observaciones","128"));
			}*/
		}
		else if(forma.getTipoConfirmacion().equals(ConstantesBD.acronimoNo))
		{
			if(forma.getCitasOdonPac().get(forma.getPosicionCita()).getMotivoNoConfirmacion().getCodigo()<=0)
			{
				errores.add("", new ActionMessage("errors.required","El motivo"));
			}
			
			if(forma.getObservacionesConfirmacion().trim().equals(""))
			{
				errores.add("", new ActionMessage("errors.required","El campo observaciones"));
			}
			else if(forma.getObservacionesConfirmacion().length()>128)
			{
				errores.add("", new ActionMessage("errors.maxlength","El campo observaciones","128"));
			}
		}
		//*********************************************************
		
		
		if(errores.isEmpty())
		{
			Connection con = UtilidadBD.abrirConexion();
			
			UtilidadBD.iniciarTransaccion(con);
			
			forma.getCitasOdonPac().get(forma.getPosicionCita()).setConfirmacion(forma.getTipoConfirmacion());
			forma.getCitasOdonPac().get(forma.getPosicionCita()).setObservacionesConfirmacion(forma.getObservacionesConfirmacion());
			forma.getCitasOdonPac().get(forma.getPosicionCita()).getUsuarioConfirmacion().setLoginUsuario(usuario.getLoginUsuario());
			forma.getCitasOdonPac().get(forma.getPosicionCita()).setFechaConfirmacion(UtilidadFecha.getFechaActual(con));
			forma.getCitasOdonPac().get(forma.getPosicionCita()).setHoraConfirmacion(UtilidadFecha.getHoraActual(con));
			
			
			
			/*if(CitaOdontologica.insertLogCitaOdontologica(con, forma.getCitasOdonPac().get(forma.getPosicionCita()))>0)
			{*/
				
				
				if(!CitaOdontologica.actualizarCitaOdontologica(con, forma.getCitasOdonPac().get(forma.getPosicionCita())))
				{
					errores.add("", new ActionMessage("errors.problemasGenericos","actualizando la información de la cita"));
				}
				
			/*}
			else
			{
				errores.add("", new ActionMessage("errors.problemasGenericos","registrando el histï¿½rico de la cita"));
			}*/
			
			if(errores.isEmpty())
			{
				UtilidadBD.finalizarTransaccion(con);
			}
			else
			{
				UtilidadBD.abortarTransaccion(con);
				saveErrors(request, errores);
				forma.setEstado("iniciarActividad");
			}
			UtilidadBD.closeConnection(con);
			
		}
		else
		{
			saveErrors(request, errores);
			forma.setEstado("iniciarActividad");
		}
		
		
		return mapping.findForward("confirmacionCita");
	}

	
	
	/**
	 * Método implementado para que el controlador elija cual actividad desea ejecutar segun el estado de la cita
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param mundo
	 * @return
	 */
	private ActionForward accionIniciarActividad(AgendaOdontologicaForm forma,
			UsuarioBasico usuario, ActionMapping mapping,
			HttpServletRequest request, PersonaBasica persona,
			CitaOdontologica mundoCita, HttpServletResponse response) throws IPSException 
	{
		
		forma.setNombreArchivoImpresionCita("");
		
		//Se toma el estado de la cita
		String estadoCita = forma.getCitasOdonPac().get(forma.getPosicionCita()).getEstado();
		
		
		// Informacion del record de citas utilizada para hacer las asociaciones d ela programada
		forma.setEstadoCitaRecord(estadoCita);
		forma.setCodigoCitaRecord(forma.getCitasOdonPac().get(forma.getPosicionCita()).getCodigoPk());
		//-------------------------------------------------------------------------------------------
		
		
		// Settear informacion dtoConfirmacion
		forma.setCitaConfirmacion(forma.getCitasOdonPac().get(forma.getPosicionCita()));
		
		// Cargar Informacion Paciente 
		Paciente paciente= new Paciente();
		Connection con2= UtilidadBD.abrirConexion();
	
		try {
			paciente.cargarPaciente(con2, forma.getCitaConfirmacion().getCodigoPaciente());
			
			if(paciente.getCodigoPersona() != persona.getCodigoPersona()){
				
				persona.cargar(con2, paciente.getCodigoPersona());
				//request.getSession().setAttribute("pacienteActivo", persona);
			}
			
		} catch (SQLException e) { Log4JManager.info("No se pudo cargar paciente"); }
			
		UtilidadBD.cerrarObjetosPersistencia(null, null, con2);
		
		// Settera informcion paciente 
		forma.getCitaConfirmacion().setNombrePaciente(paciente.getNombres());
		forma.getCitaConfirmacion().setTipoIdentificacionPac(paciente.getCodigoTipoIdentificacion());
		forma.getCitaConfirmacion().setNumeroIdentificacionPac(paciente.getNumeroIdentificacion());
		forma.getCitaConfirmacion().setNombreCompletoPaciente(paciente.getNombreCompleto());
		//estadoCita=ConstantesIntegridadDominio.acronimoAsignado;
		
		//quita el mensaje de exito al ingresar 
		forma.setProcesoExito(ConstantesBD.acronimoNo);
		forma.setReprogramar(ConstantesBD.acronimoNo);
		forma.setAsignar(ConstantesBD.acronimoNo);
		forma.setReserva(ConstantesBD.acronimoNo);
		forma.setCambiarServicio(ConstantesBD.acronimoNo);
		forma.setHabilitarCalcTarifa(ConstantesBD.acronimoNo);
		forma.setMostrarTarifas(false);
		forma.setPasoValidacionesGuardar(false);
		
		Log4JManager.info("actividad: "+forma.getCodigoActividad());
		Log4JManager.info("estado cita: "+estadoCita);
		
		ActionErrors errores = new ActionErrors();
		Connection con = UtilidadBD.abrirConexion();
		
		//Se Carga Paciente en sesion
		if(forma.getTipoFlujoPaciente().equals(ConstantesBD.acronimoNo))
	    {
	        if(forma.getPaciente().getCodigo()<=0)
	    	{
				forma.setPaciente(IngresoPacienteOdontologia.consultarPersonaPaciente(con, 
				forma.getCitasOdonPac().get(forma.getPosicionCita()).getNumeroIdentificacionPac(), 
				forma.getCitasOdonPac().get(forma.getPosicionCita()).getTipoIdentificacionPac(), ""));
				
				forma.setNumeroIdentificacionPac(forma.getPaciente().getNumeroId());
				forma.setTipoIdentificacionPac(forma.getPaciente().getTipoId());
				try 
				{
					persona.setCodigoPersona(UtilidadValidacion.getCodigoPersona(con, forma.getCitasOdonPac().get(forma.getPosicionCita()).getTipoIdentificacionPac(), forma.getCitasOdonPac().get(forma.getPosicionCita()).getNumeroIdentificacionPac()));
					forma.getPaciente().setCodigo(persona.getCodigoPersona());
					UtilidadesManejoPaciente.cargarPaciente(con, usuario, persona, request);
					ObservableBD observable = (ObservableBD) getServlet().getServletContext().getAttribute("observable");
					if (observable != null) 
					{
						persona.setObservable(observable);
						// Si ya lo habï¿½amos aï¿½adido, la siguiente lï¿½nea no hace nada
						observable.addObserver(persona);
					}
					Log4JManager.info("No ha cargado Paciente1 "+ persona.getCodigoPersona());
					
					if(persona==null || persona.getCodigoPersona()<=0)
					{	
						Log4JManager.info("No ha cargado Paciente 2 "+ persona.getCodigoPersona());
						errores.add("fechaDesplazamiento",new ActionMessage("errors.paciente.noCargadoPac"));
						saveErrors(request, errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principalXRango");
					}
					Log4JManager.info("\n\n Codigo Cuenta >>> "+persona.getCodigoCuenta());
					forma.setCodCuentaPaciente(persona.getCodigoCuenta());
				} 
				catch (SQLException e) 
				{
					Log4JManager.error("Error validando si existe el paciente: ",e);
				}
				
				//forma.getCitasOdonPac().get(forma.getPosicionCita()).getNumeroIdentificacionPac();
				//forma.getCitasOdonPac().get(forma.getPosicionCita()).getTipoIdentificacionPac();
	    	}
	    }	
		
		forma.setCodCuentaPaciente(persona.getCodigoCuenta());
		
		if(estadoCita.equals(ConstantesIntegridadDominio.acronimoProgramado))
		{			
			if(forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoTipoFuncionalidadReservar)
			 || forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoTipoFuncionalidadAsignar)){
				
				return accionCriteriosBusqueda(forma,usuario,mapping, persona, request);
			}
			
		}else if(estadoCita.equals(ConstantesIntegridadDominio.acronimoReservado)){
			
			if(forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoReprogramar))
			{
				forma.setReprogramar(ConstantesBD.acronimoSi);						
				forma.resetTiposCita();
				forma.setCitaAgendaSel(forma.getCitasOdonPac().get(forma.getPosicionCita()));
				forma.setTipoCita(forma.getCitaAgendaSel().getTipo());
				forma.setHoraInicio(forma.getCitaAgendaSel().getHoraInicio());
				forma.setHoraFinal(forma.getCitaAgendaSel().getHoraFinal());
				Log4JManager.info("\n\n ************************ fecha >>"+forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon().getFecha());
				forma.setFecha(UtilidadFecha.conversionFormatoFechaAAp(forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon().getFecha()));
				forma.setFechaDesplazamiento(forma.getFecha());
				return accionCriteriosBusqueda(forma,usuario,mapping, persona, request);
				
			}
			else if(forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoCancelar))
			{
				forma.resetCancelacion();
				return accionIniciarActividadCancelar(forma,mapping,usuario, persona);							
			}
			else if(forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoConfirmar))
			{
				Log4JManager.info("paso por aqui para confirmar");
			    return accionIniciarActividadConfirma (forma,mapping,usuario,paciente, persona, request);	 // Estaba este atributo -- persona	   			    	 
			}
			else if(forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoTipoFuncionalidadAsignar))
			{
				forma.setMostrarTarifas(true);
				// OJO no se abren criterios de busqueda se va directo a asignar 
				DtoCitaOdontologica cita= new DtoCitaOdontologica();
			    cita=forma.getCitasOdonPac().get(forma.getPosicionCita());
			    forma.setAgendaSel(cita.getAgendaOdon());    
			    // Se agrega el nombre de la especialidad de la agenda odontologica
			    forma.getAgendaSel().setNombreEspecialidadUniAgen(cita.getNomEspeUconsulta());
			    
				forma.setHoraInicio(cita.getHoraInicio());
				forma.setHoraFinal(cita.getHoraFinal());
				forma.setFecha(UtilidadFecha.conversionFormatoFechaAAp(cita.getAgendaOdon().getFecha()));
				forma.setTipoCita(cita.getTipo());				
				forma.setCentroAtencion(cita.getAgendaOdon().getCentroAtencion());
				
				return accionIniciarAsignar(forma,usuario,mapping,request,response,persona);
			}
			else if(forma.getCodigoActividad().equals(ConstantesBD.acronimoCambiarServicioCitaOdon))
			{
				DtoCitaOdontologica cita= new DtoCitaOdontologica();
				cita=forma.getCitasOdonPac().get(forma.getPosicionCita());
				forma.setCentroAtencion(cita.getAgendaOdon().getCentroAtencion());
				return accionCambiarServicioOdontologico(forma, mapping, usuario, mundoCita, request, response, persona);
			}
			
		}else if(estadoCita.equals(ConstantesIntegridadDominio.acronimoAsignado)){
			
			if(forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoCancelar))
			{
				forma.resetCancelacion();
				return accionIniciarActividadCancelar(forma,mapping,usuario, persona);
			}
			else if(forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoCambiarServicio))
			{
				forma.setCambiarServicio(ConstantesBD.acronimoSi);	
				DtoCitaOdontologica cita= new DtoCitaOdontologica();
				cita=forma.getCitasOdonPac().get(forma.getPosicionCita());
				forma.setCentroAtencion(cita.getAgendaOdon().getCentroAtencion());
				return accionCambiarServicioOdontologico(forma, mapping, usuario, mundoCita, request, response, persona);
			}
			
		}else if(forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoReprogramar)){
				
				forma.setReprogramar(ConstantesBD.acronimoSi);
				DtoCitaOdontologica cita= new DtoCitaOdontologica();
			    cita=forma.getCitasOdonPac().get(forma.getPosicionCita());
				forma.setTipoCita(cita.getTipo());				

				return accionCriteriosBusqueda(forma,usuario,mapping, persona, request);
				//return mapping.findForward("criteriosBusqueda");
				
			}else if(forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoCancelar)){
				
				/*
				 * Se elimina la validación que si la cita se va a Reprogramar solo
				 * debe listar la opcion 'Por Paciente'
				 */
				forma.setPorPaciente(ConstantesBD.acronimoNo);
				forma.resetCancelacion();
				forma.setEstadoCita(estadoCita);
				forma.setCancelar(ConstantesBD.acronimoSi);
				return accionIniciarActividadCancelar(forma,mapping,usuario, persona);
			}
		
		return null;
	}

	
	
	
	
	/**
	 * Metodo para actividad cambiar estado
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCambiarServicioOdontologico(AgendaOdontologicaForm forma, ActionMapping mapping, UsuarioBasico usuario, CitaOdontologica mundoCita , HttpServletRequest request, HttpServletResponse response,  PersonaBasica paciente) throws IPSException
	{
		Connection con = UtilidadBD.abrirConexion();
		forma.setCitaSeleccionada(forma.getCitasOdonPac().get(forma.getPosicionCita()).getCodigoPk());
		Log4JManager.info("\n\ncita seleccionada:: "+forma.getCitaSeleccionada()+"\n\ncodi pk: "+forma.getCitasOdonPac().get(forma.getPosicionCita()).getCodigoPk());

		
		forma.setTipoCitaCrear(ConstantesBD.acronimoCambiarServicioCitaOdon);
		forma.setCitaAgendaSel(forma.getCitasOdonPac().get(forma.getPosicionCita()));
		forma.setTipoCita(forma.getCitaAgendaSel().getTipo());
		forma.setHoraInicio(forma.getCitaAgendaSel().getHoraInicio());
		forma.setHoraFinal(forma.getCitaAgendaSel().getHoraFinal());
		Log4JManager.info("\n\n ************************ fecha >>"+forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon().getFecha());
		forma.setFecha(UtilidadFecha.conversionFormatoFechaAAp(forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon().getFecha()));
		forma.setFechaDesplazamiento(forma.getFecha());
		Log4JManager.info("Codigo Agenda "+forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgenda());
		forma.setCodigoAge(forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgenda());
		forma.setAgendaSel(forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon());
		
		ArrayList<DtoServicioCitaOdontologica> listaServCita= new ArrayList<DtoServicioCitaOdontologica>();
		listaServCita= CitaOdontologica.consultarServiciosCitaOdontologica(con, forma.getCitasOdonPac().get(forma.getPosicionCita()).getCodigoPk(),usuario.getCodigoInstitucionInt(), paciente.getCodigoPersona());
			
		
		for(DtoServicioCitaOdontologica elem: listaServCita)
		{			
			DtoServicioOdontologico dto= new DtoServicioOdontologico();
			Log4JManager.info("\n\ncod: "+elem.getCodigoPk()+" desc: "+elem.getNombreServicio());
			dto.setCodigoServicio(elem.getCodigoPk());
			dto.setDescripcionServicio(elem.getNombreServicio());
			dto.setAsociarSerCita(ConstantesBD.acronimoSi);
						
			forma.getServiciosOdon().add(dto);
		}
		UtilidadBD.closeConnection(con);
		
		String estadoCita = forma.getCitasOdonPac().get(forma.getPosicionCita()).getEstado();
		
		if(estadoCita.equals(ConstantesIntegridadDominio.acronimoReservado))
		{			
			return accionIniciarReservar(forma, usuario, mapping, paciente, response, request, new AgendaOdontologica());
		}
		else 
		{
			return accionIniciarAsignar(forma,usuario,mapping,request,response, paciente);
		}
	}
	
	/**
	 * Mï¿½todo para iniciar la actividad de confirmacion
	 * @param forma
	 * @param mapping
	 * @param usuario 
	 * @param persona 
	 * @param request 
	 * @return
	 */
	private ActionForward accionIniciarActividadConfirma(
			AgendaOdontologicaForm forma, ActionMapping mapping, UsuarioBasico usuario, Paciente paciente, PersonaBasica persona, HttpServletRequest request) throws IPSException 
	{
		forma.setTipoConfirmacion("");
		Connection con = UtilidadBD.abrirConexion();
		forma.setMotivosNoConfirmacion(Utilidades.consultarMotivosCita(con, usuario.getCodigoInstitucionInt(), ConstantesIntegridadDominio.acronimoMotivoNoconfirma));
		forma.setTipoConfirmacion("");
		forma.setSaldoActualPaciente(0);
		forma.setAbonosDisponiblesPaciente(0);
		forma.setSaldoTotalServcios("");
		
		
		//Saldo Actual---------------------
		//forma.setSaldoActualPaciente(AbonosYDescuentos.consultarAbonosDisponibles(con, forma.getPaciente().getCodigo(), ingreso));
		
		Integer codigoIngreso = null;
		
		if(paciente.getCodigoIngreso()>ConstantesBD.codigoNuncaValido){
			
			codigoIngreso = new Integer(paciente.getCodigoIngreso());
		}
		
		forma.setSaldoActualPaciente      (Utilidades.obtenerAbonosDisponiblesPaciente(paciente.getCodigoPersona(),codigoIngreso, usuario.getCodigoInstitucionInt()));
		forma.setAbonosDisponiblesPaciente(Utilidades.obtenerAbonosDisponiblesPaciente(paciente.getCodigoPersona(),codigoIngreso, usuario.getCodigoInstitucionInt()));
		

		//--------------------------------
		
		//cargarServiciosAsignacionYReservaCita(forma, usuario, paciente.getCodigoIngreso());
		
		//cargarValoresSaldosAbonos(forma, usuario, paciente);
		
		double tmpSaldo=ConstantesBD.codigoNuncaValidoDouble;
		
//		if(forma.getCitaConfirmacion() !=null)
//		{
//			for(DtoServicioOdontologico servicio: forma.getServiciosOdon())
//			{
//				if(servicio.getInfoTarifa()!=null  && servicio.getInfoTarifa().getValorTarifaTotalConDctos()!=null)
//				{
//					tmpSaldo+= servicio.getInfoTarifa().getValorTarifaTotalConDctos().doubleValue();
//				}
//			}
//		}
		
		DtoCitaOdontologica cita = new DtoCitaOdontologica();		
		cita = forma.getCitaConfirmacion();
		
		if(cita !=null)
		{
			
			if (!cita.getEstadoPlanTratamiento().trim().equals(ConstantesIntegridadDominio.acronimoEstadoActivo) &&
				!forma.getCitaConfirmacion().isMigrado()) {
				
				for(DtoServicioCitaOdontologica ser: cita.getServiciosCitaOdon())
				{
					if(ser.getValorTarifa()!=null)
					{
						tmpSaldo+= ser.getValorTarifa().doubleValue();
					}
				}
				
				forma.setSaldoTotalServcios(tmpSaldo+"");
				
			}else{
				
				asignarValorTarifaCitasMigradas(con, forma, mapping, usuario, paciente, persona, request);
				
			}
		}
		
		UtilidadBD.closeConnection(con);
		Log4JManager.info("paso por aqui para confirmar segunda vez");
		return mapping.findForward("confirmacionCita");
	}
	
	/**
	 * Método que calcula y asigna el valor de la cita para las citas migradas.
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param persona
	 * @param request
	 */
	private void asignarValorTarifaCitasMigradas(Connection con, AgendaOdontologicaForm forma,
			ActionMapping mapping, UsuarioBasico usuario, Paciente paciente,
			PersonaBasica persona, HttpServletRequest request) throws IPSException {
		
		ArrayList<DtoServicioCitaOdontologica> listaServiciosCita= new ArrayList<DtoServicioCitaOdontologica>();
		ActionErrors errores = new ActionErrors();
		ArrayList<DtoServicioOdontologico> servicios = new ArrayList<DtoServicioOdontologico>();
		boolean citaMigrada = forma.getCitaConfirmacion().isMigrado();
		
		listaServiciosCita= CitaOdontologica.consultarServiciosCitaOdontologica(con, forma.getCitaConfirmacion().getCodigoPk(),usuario.getCodigoInstitucionInt(), paciente.getCodigoPersona());
		
		for (DtoServicioCitaOdontologica registro : listaServiciosCita) {
			DtoServicioOdontologico  dto = new DtoServicioOdontologico ();
			
			dto.setCodigoServicio(registro.getServicio());
			dto.setCodigoCita(registro.getCitaOdontologica());
			dto.setCodigoPlanTratamiento(registro.getPlanTratamiento());
			dto.setAsociarSerCita(ConstantesBD.acronimoSi);
			dto.setGarantiaServicio(registro.getGarantiaServicio());
			dto.setCodigoPresuOdoProgSer(registro.getCodigoPresuOdoProgSer());
			dto.setDescripcionServicio(registro.getNombreServicio());
			dto.setInfoTarifa(registro.getInfoTarifa());
			
			servicios.add(dto);
			
		}
		
		forma.setServiciosOdon(servicios);
		if (servicios != null && servicios.size() > 0) {
			forma.setCodigoPlanTratamiento(servicios.get(0).getCodigoPlanTratamiento());
		}
		
		

		calcularTarifaActualOPresupuestoContratado(forma, usuario, request, persona, errores, citaMigrada);
		
		
	}

	/**
	 * Mï¿½todo para consultar todas las 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param mundo
	 * @return
	 */
	private ActionForward accionConsultarTodasCitas(AgendaOdontologicaForm forma, UsuarioBasico usuario, ActionMapping mapping, AgendaOdontologica mundo) 
	{
		Connection con = UtilidadBD.abrirConexion();
		
		forma.setTodasCitas(mundo.consultarCitasPaciente(con, usuario, forma.getPaciente().getCodigo(),usuario.getCodigoInstitucionInt(), "", "", 0, ConstantesBD.codigoNuncaValido, 0));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("citasPaciente");
	}

	/**
	 * Método para realizar la validación del paciente
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param persona
	 * @param request
	 * @param mundo 
	 * @return
	 */
	private ActionForward accionValidarPaciente(AgendaOdontologicaForm forma,UsuarioBasico usuario, ActionMapping mapping, PersonaBasica persona, HttpServletRequest request, AgendaOdontologica mundo) 
	{
		String existePaciente = (String) request.getParameter("existePaciente");
		String origen = (String) request.getParameter("origen");
		forma.setNombreArchivoImpresionCita("");
		
		if(!UtilidadTexto.getBoolean(existePaciente) && "ingresoPacienteOdon".equals(origen)){
			
			forma.setTipoIdentificacionPac("");
			forma.setNumeroIdentificacionPac("");
			
			forma.setEstado("empezar");
			
			return mapping.findForward("principal");
			
		}else if("criteriosBusqueda".equals(origen)){

			forma.resetCriteriosBusqueda();
		}
			
		//return mapping.findForward("principal");
		
		Connection con = UtilidadBD.abrirConexion();
		
		try 
		{
			forma.setExistePaciente(UtilidadValidacion.existePaciente(con, forma.getTipoIdentificacionPac(), forma.getNumeroIdentificacionPac()));
			
			if(forma.isExistePaciente())
			{
				persona.setCodigoPersona(UtilidadValidacion.getCodigoPersona(con, forma.getTipoIdentificacionPac(), forma.getNumeroIdentificacionPac()));
				forma.getPaciente().setCodigo(persona.getCodigoPersona());
				UtilidadesManejoPaciente.cargarPaciente(con, usuario, persona, request);
				
				forma.setTieneIngresoPaciente(UtilidadesManejoPaciente.tienePacienteIngresoAbierto(con, new BigDecimal(persona.getCodigoPersona())));
				
				ArrayList<String> estados = new ArrayList<String>();
				estados.add(ConstantesIntegridadDominio.acronimoInactivo);
				
				if(obtenerCodigoPlanTratamientoPorEstados(forma, estados) > 0){
					
					forma.setTienePlanTratamientoInactivo(true);
					
				}else{
					
					forma.setTienePlanTratamientoInactivo(false);
				}
					
				
				int codigoInstitucion = usuario.getCodigoInstitucionInt();
				//getMinutosCaducaCitasAsignadasReprogramadas
			    forma.setCitasOdonPac(AgendaOdontologica.consultarCitasPaciente(con, usuario, forma.getPaciente().getCodigo(), codigoInstitucion,
			    		UtilidadFecha.getFechaActual(con),UtilidadFecha.getHoraActual(con),Utilidades.convertirAEntero(ValoresPorDefecto.getMinutosCaducaCitasAsignadasReprogramadas(codigoInstitucion)),
			    		persona.getCodigoIngreso(), Utilidades.convertirAEntero(ValoresPorDefecto.getMinutosCaducaCitasReservadas(codigoInstitucion))));
			    
			    int contSerCitaProg;
			    for(DtoCitaOdontologica citaOdon: forma.getCitasOdonPac())
			    {			    	
			    	contSerCitaProg=0;
			      for(DtoServicioCitaOdontologica serv :citaOdon.getServiciosCitaOdon())
			      {
			    	  if(serv.getAsignadoAOtraCita()){
			    		  contSerCitaProg++;
			    	  }
			      }
			      
			      if(contSerCitaProg>0 && contSerCitaProg == citaOdon.getServiciosCitaOdon().size()){
			    	  
			    	  citaOdon.setTodosServiciosAsigCitaProgramada(ConstantesBD.acronimoSi);
			      }
			   }

			    //Tarea Xplanner 2008 161356 
			    //no mostrar las citas programadas y que no tienen actividad.
			    ArrayList<DtoCitaOdontologica> temporal=new ArrayList<DtoCitaOdontologica> ();
			    for(DtoCitaOdontologica cita: forma.getCitasOdonPac())
			    {
			    	if((cita.getEstado().equals(ConstantesIntegridadDominio.acronimoProgramado) && 
			    		cita.getTodosServiciosAsigCitaProgramada().equals(ConstantesBD.acronimoNo))
			    		
			    			|| !cita.getEstado().equals(ConstantesIntegridadDominio.acronimoProgramado))
			    	{
			    		temporal.add(cita);
			    	}
			    }
			    forma.setCitasOdonPac(temporal);
			    
			    //Se debe validar que existen citas para el paciente para poder habilitar o no el link.
			    accionConsultarTodasCitas(forma, usuario, mapping, mundo);
			}
			
		}catch (SQLException e) 
		{
			Log4JManager.error("Error validando si existe el paciente: ",e);
		}
		
		UtilidadBD.closeConnection(con);
		if(forma.getTipoFlujoPaciente().equals(ConstantesBD.acronimoSi))
			return mapping.findForward("principal");
		else
			return mapping.findForward("principalXRango");

	}

	/**
	 * accion para empezar el flujo de agenda odontol&oacute;gica por paciente
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezarPaciente(AgendaOdontologicaForm forma,UsuarioBasico usuario, ActionMapping mapping) 
	{
		forma.reset();
		forma.setTipoFlujoPaciente(ConstantesBD.acronimoSi);		
		forma.setNumDiasAntFechaActual(ValoresPorDefecto.getNumDiasAntFActualAgendaOd(usuario.getCodigoInstitucionInt()));
		forma.setMultiploMinGenCitas(ValoresPorDefecto.getMultiploMinGeneracionCita(usuario.getCodigoInstitucionInt()));
		forma.setValidaPresupuestoContratado(ValoresPorDefecto.getValidaPresupuestoOdoContratado(usuario.getCodigoInstitucionInt()));
		forma.setUtilProgOdonPlanTra(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()));
		forma.setMinutosEsperaAsgCitOdoCad(ValoresPorDefecto.getMinutosEsperaAsignarCitasOdoCaducadas(usuario.getCodigoInstitucionInt()));
		forma.setListTipIdent(Utilidades.obtenerTiposIdentificacion("ingresoPaciente",usuario.getCodigoInstitucionInt()));
		return mapping.findForward("principal");
	}
	
	
	//********************************Mï¿½TODOS ANTIGUOS*************************************************************

	/**
	 * accion seleccionar cita paciente
	 * @param mapping
	 * @param con
	 * @param forma
	 * @param mundo
	 * @return
	 */
	@SuppressWarnings("unused")
	private ActionForward accionSeleccionarCitaPac(ActionMapping mapping,
			Connection con, AgendaOdontologicaForm forma,
			AgendaOdontologica mundo, UsuarioBasico usuario) {
		forma.setCentroAtencion(forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon().getCentroAtencion());
		forma.setUnidadAgenda(forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon().getUnidadAgenda()+"");
		forma.setProfesionalSalud(forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon().getCodigoMedico()+"");
		forma.setFechaFinal(forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon().getFecha());
		forma.setFechaInicial(forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon().getFecha());
		
		// se consulta las agendas odontologicas segun el parametro de seleccion
		forma.setAgendaOdon(AgendaOdontologica.cosultarAgendaOdontologica(con, 
					forma.getCentroAtencion()+"", 
					forma.getUnidadAgenda(), 
					forma.getProfesionalSalud(),
					forma.getFechaInicial(),
					forma.getFechaFinal()));
		
		// se carga el listado de conceviones de colores de las unidades de agendas
		forma.setConvencionColores(mundo.convencionColorUnidaAgenda(con, forma.getUnidadAgenda(),forma.getFechaDesplazamiento().equals("")?forma.getFechaInicial():forma.getFechaDesplazamiento()));
		
		
		forma.setFechaDesplazamiento(forma.getFechaInicial());
		// se carga los consultorios para el manejo de la interfaz de la agenda odontologica
		String[] horaini = {""};
		String[] horafin = {""};
		forma.setConsultoriosAgenOdon(AgendaOdontologica.consultoriosAgendaOdontologica(forma.getAgendaOdon(),
				horaini, horafin, forma.getFechaDesplazamiento()));
		forma.setHoraIniAgenOdon(horaini[0]);
		forma.setHoraFinAgenOdon(horafin[0]);
		
		// lista con los intervalos de horas ya definidos
		forma.setIntervaloHora(intevaloEntreHoraIniFin(forma.getHoraIniAgenOdon(), 
				forma.getHoraFinAgenOdon(),
				Utilidades
				.convertirAEntero(forma.getMultiploMinGenCitas())));
		
		forma.setXmlAgenda(generacionXML(forma, usuario, con));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * General el XML que será enviado al componente Flash
	 * @param forma Formulario
	 * @param usuario Usuario que genera la agenda
	 * @param con Conexión con la BD
	 * @return
	 */
	private String generacionXML(AgendaOdontologicaForm forma, UsuarioBasico usuario, Connection con)
	{
		String XMLAgenda="<XML>"+
		generacionXMLParametros(forma,usuario)+
		generacionXMLConsultorios(forma, usuario, con)+
		generacionXMLAgendas(forma)+
		"</XML>";
		
		return XMLAgenda;
	}






	/**
	 * accion buscar Agenda odontologica
	 * @param mapping
	 * @param con
	 * @param forma
	 * @param mundo
	 * @return
	 */
	private ActionForward accionBuscarAgendaOdon(ActionMapping mapping,
			Connection con, AgendaOdontologicaForm forma,
			AgendaOdontologica mundo, UsuarioBasico usuario) {
		String centroAtencion = "";
		if(forma.getCentroAtencion()==ConstantesBD.codigoNuncaValido)
			centroAtencion = forma.getListCentroAtencion().get(forma.getListCentroAtencion().size()-1).get("todos").toString();
		else
			centroAtencion = forma.getCentroAtencion()+"";
		
/*		if(forma.getUnidadAgenda().equals(ConstantesBD.codigoNuncaValido+""))
		{
			forma.setUnidadAgenda(forma.getListUnidadesAgendaXUsuario().get(forma.getListUnidadesAgendaXUsuario().size()-1).get("todos").toString());
		}
*/
		
		// se consulta las agendas odontologicas segun el parametro de seleccion
		forma.setAgendaOdon(AgendaOdontologica.cosultarAgendaOdontologica(con, 
					centroAtencion, 
					forma.getUnidadAgenda(), 
					forma.getProfesionalSalud(),
					forma.getFechaInicial(),
					forma.getFechaFinal()));
		
		// se carga el listado de conceviones de colores de las unidades de agendas
		forma.setConvencionColores(AgendaOdontologica.convencionColorUnidaAgenda(con, forma.getUnidadAgenda(),forma.getFechaDesplazamiento().equals("")?forma.getFechaInicial():forma.getFechaDesplazamiento()));
		
		
		forma.setFechaDesplazamiento(forma.getFechaInicial());
		// se carga los consultorios para el manejo de la interfaz de la agenda odontologica
		String[] horaini = {""};
		String[] horafin = {""};
		forma.setConsultoriosAgenOdon(AgendaOdontologica.consultoriosAgendaOdontologica(forma.getAgendaOdon(),
				horaini, horafin, forma.getFechaDesplazamiento()));
		forma.setHoraIniAgenOdon(horaini[0]);
		forma.setHoraFinAgenOdon(horafin[0]);
		
		// lista con los intervalos de horas ya definidos
		forma.setIntervaloHora(intevaloEntreHoraIniFin(forma.getHoraIniAgenOdon(), 
				forma.getHoraFinAgenOdon(),
				Utilidades.convertirAEntero(forma.getMultiploMinGenCitas())));
		
		// se generan los string xml
		if(forma.getAgendaOdon().size()>0)
		{
			forma.setXmlAgenda(generacionXML(forma, usuario, con));
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * insercion de la reserva de una cita
	 * @param con
	 * @param usuario
	 * @param forma
	 * @param mundo
	 * @param mundoCita
	 */
	private int insertarReservaCita(Connection con, UsuarioBasico usuario,
			AgendaOdontologicaForm forma, AgendaOdontologica mundo,
			CitaOdontologica mundoCita, PersonaBasica persona) 
	{
		int codigoPKCita=ConstantesBD.codigoNuncaValido;
		DtoCitaOdontologica dto = new DtoCitaOdontologica();
		dto.setEstado(ConstantesIntegridadDominio.acronimoReservado);
		dto.setTipo(forma.getTipoCita());
		dto.setAgenda(forma.getAgendaSel().getCodigoPk());
		dto.setDuracion(forma.getDuracionCita());
		dto.setHoraInicio(forma.getHoraInicio());
		dto.setHoraFinal(forma.getHoraFinal());
		dto.setFechaReserva(UtilidadFecha.getFechaActual());
		dto.setHoraReserva(UtilidadFecha.getHoraActual());
		dto.setUsuarioReserva(usuario.getLoginUsuario());
		dto.setPorConfirmar(ConstantesBD.acronimoNo);
		dto.setCodigoPaciente(persona.getCodigoPersona());// 2420 
		dto.setUsuarioModifica(usuario.getLoginUsuario());
		dto.setCodigoCentroCosto(forma.getCodCentroCostoXuniAgen());		
		for(DtoServicioOdontologico elem: forma.getServiciosOdon())
		{
			if(elem.getAsociarSerCita().equals(ConstantesBD.acronimoSi))
			{
				DtoServicioCitaOdontologica dtoSer = new DtoServicioCitaOdontologica();
				dtoSer.setServicio(elem.getCodigoServicio());
				dtoSer.setCodigoPrograma(elem.getCodigoPrograma());
				dtoSer.setDuracion(elem.getMinutosduracionNuevos());
				dtoSer.setProgramaHallazgoPieza(elem.getProgramaHallazgoPieza());

				dtoSer.setActivo(ConstantesBD.acronimoSi);
				dtoSer.setUsuarioModifica(usuario.getLoginUsuario());
				dtoSer.setEstadoCita(ConstantesIntegridadDominio.acronimoReservado);
				dtoSer.setCodigoAgenda(forma.getAgendaSel().getCodigoPk());
				dtoSer.setFechaCita(forma.getAgendaSel().getFecha());
				dtoSer.setHoraInicio(forma.getHoraInicio());
				dtoSer.setHoraFinal(forma.getHoraFinal());
				dtoSer.setUnidadAgenda(forma.getAgendaSel().getUnidadAgenda());
				dtoSer.setAplicaAbono(ConstantesBD.acronimoNo);
				dtoSer.setAplicaAnticipo(ConstantesBD.acronimoNo);
				
				///tarea 159880, en esta no se esta guardando el valor de la tarifa al momento de reservar, me queda la duda para la valoracion en 0. Solo si tiene tarifa
				// guardandotarifas
				if(elem.getInfoTarifa()!=null)
				{
					if(elem.getInfoTarifa().getValorTarifaTotalConDctos() != null){
						dtoSer.setValorTarifa(elem.getInfoTarifa().getValorTarifaTotalConDctos());
					}
				}
				
				dto.getServiciosCitaOdon().add(dtoSer);
			}
		}
		UtilidadBD.iniciarTransaccion(con);
		if((codigoPKCita=CitaOdontologica.insertarCitaOdontologica(con, dto))>0){
			Log4JManager.info("se realizo la reserva exitosamente enviar datos de confirmacion al swf");
			// disminuir el cupo de la agenda
			if(AgendaOdontologica.modificarCuposAgendaOdontologica(con, 
					forma.getAgendaOdon().get(forma.getPosAgendaSel()).getCodigoPk(), 
					forma.getHoraInicio(), 
					forma.getHoraFinal(),
					usuario.getLoginUsuario(), true, usuario.getCodigoInstitucionInt())>0)
			{
				//se adiciona la cita recien creada a la agenda corespondiente
				dto.setCodigoPk(codigoPKCita);
				dto.setNombrePaciente(forma.getPaciente().getPrimerApellido()+" "+forma.getPaciente().getPrimerNombre());
				dto.setTipoIdentificacionPac(forma.getPaciente().getTipoId());
				dto.setNumeroIdentificacionPac(forma.getPaciente().getNumeroId());
				forma.getAgendaOdon().get(forma.getPosAgendaSel()).getCitasOdontologicas().add(dto);
				forma.getAgendaOdon().get(forma.getPosAgendaSel()).setCupos(forma.getAgendaOdon().get(forma.getPosAgendaSel()).getCupos()-1);
				//forma.setXmlCitaAgenda(generacionXMLAgendas(forma, false, forma.getAgendaSel().getCodigoPk()));
				//Log4JManager.info("citaAgnda: "+forma.getXmlCitaAgenda());
				dto.setNombreProfesional(forma.getAgendaSel().getNombreMedico());
				dto.setNomEspeUconsulta(forma.getAgendaSel().getNombreEspecialidadUniAgen());
				
				forma.setCitaCreada(dto);
				forma.setProcesoExito(ConstantesBD.acronimoSi);
				UtilidadBD.finalizarTransaccion(con);
			}else
				UtilidadBD.abortarTransaccion(con);
		}else{
			Log4JManager.info("NO se realizo la reserva [NO SE ENVIA NINGUN DATO DE CONFIRMACION AL SWF]");
			codigoPKCita = ConstantesBD.codigoNuncaValido;
			UtilidadBD.abortarTransaccion(con);
		}
		UtilidadBD.closeConnection(con);
		return codigoPKCita;
	}

	
	
	/**
	 * validacion de los campos de la cita
	 * @param forma
	 * @return
	 */
	private ActionErrors validacionCitaOdontologica(AgendaOdontologicaForm forma, PersonaBasica persona) {
		ActionErrors errores = new ActionErrors();
		
		Log4JManager.info("Multiplo Minutos Generacion Citas >>"+forma.getMultiploMinGenCitas());
		Log4JManager.info("tipoCITA crear "+forma.getTipoCitaCrear());
		
		int n=forma.getAgendaOdon().size();
		String  horaInicioAux= forma.getHoraInicio();
        String horaFinalAux= forma.getHoraFinal();
        int duracionCitaAux=forma.getDuracionCita();
        int duracionTotalServAux= forma.getTotalDuracionServicios();
        
		if(!forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoCambiarServicio))
		{
			for(int i=0;i<n; i++)		
			{
				int m=forma.getAgendaOdon().get(i).getCitasOdontologicas().size();
				for(int j=0;j<m;j++)
				{				
					if(forma.getAgendaOdon().get(i).getCitasOdontologicas().get(j).getCodigoPaciente() == forma.getPaciente().getCodigo() 
							&& forma.getAgendaOdon().get(i).getCitasOdontologicas().get(j).getHoraInicio().equals(forma.getHoraInicio()) 
							&& forma.getAgendaOdon().get(i).getCitasOdontologicas().get(j).getEstado().equals(forma.getTipoCitaCrear()))
					{
						if(forma.getAgendaOdon().get(i).getCitasOdontologicas().get(j).getEstado().equals(ConstantesIntegridadDominio.acronimoReservado))
							errores.add("horacita",new ActionMessage("errors.notEspecific", "Ya existe una cita reservada para ese paciente en el horario seleccionado."));
						else if(forma.getAgendaOdon().get(i).getCitasOdontologicas().get(j).getEstado().equals(ConstantesIntegridadDominio.acronimoAsignado))
							errores.add("horacita",new ActionMessage("errors.notEspecific", "Ya existe una cita asignada para ese paciente en el horario seleccionado."));
					}
				}
			}
		}
		if(forma.getCodigoActividad().equals(ConstantesBD.acronimoCambiarServicioCitaOdon) || forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoTipoFuncionalidadAsignar))
		{
			//forma.setFecha(UtilidadFecha.conversionFormatoFechaAAp(forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon().getFecha()));
			//forma.setFechaDesplazamiento(forma.getFecha());
			forma.setDuracionCita(UtilidadFecha.numeroMinutosEntreFechas(forma.getFecha(), forma.getHoraInicio(), forma.getFecha(), forma.getHoraFinal()));
		}	
		
		if(!forma.getTipoCitaCrear().equals(ConstantesBD.acronimoCancelarCitaOdon)
			&& !forma.getTipoCitaCrear().equals(ConstantesIntegridadDominio.acronimoReprogramado) 
			&& !forma.getTipoCitaCrear().equals(ConstantesBD.acronimoCambiarServicioCitaOdon))
		{
			
			//validar los cupos de la agenda
			if(forma.getPosAgendaSel()>0 && forma.getAgendaOdon().get(forma.getPosAgendaSel())!=null  )
			{
			  if(forma.getAgendaOdon().get(forma.getPosAgendaSel()).getCupos()<=0)
				 errores.add("horafinal",new ActionMessage("errors.notEspecific", "No existe cupos en la Agenda "+forma.getAgendaOdon().get(forma.getPosAgendaSel()).getDescripcionUniAgen()+" para la realización de la Reserva. "));
			}
			//validar los tiempos 
			// fecha
			
			if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaDesplazamiento()),UtilidadFecha.getFechaActual()))
			{
				errores.add("fechacita",new ActionMessage("errors.fechaAnteriorIgualActual", forma.getFechaDesplazamiento(),"Actual "+UtilidadFecha.getFechaActual()));
			}
			// hora
			if(UtilidadFecha.esFechaMenorQueOtraReferencia(
					UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaDesplazamiento()),UtilidadFecha.getFechaActual())
				&& forma.getHoraInicio().compareTo(forma.getAgendaSel().getHoraInicio())<0)
			{
				errores.add("HoraInicio",new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "Inicial",forma.getAgendaSel().getHoraInicio()));
			}
			Log4JManager.info("\n\nhora fin agen sel:: "+forma.getAgendaSel().getHoraFin()+" hora fin forma: "+forma.getHoraFinal());
			
			if(forma.getAgendaSel().getHoraFin().compareTo(forma.getHoraFinal())<0){
				//errores.add("HoraFinal",new ActionMessage("errors.horaPosteriorAOtraDeReferencia", "Final",forma.getAgendaSel().getHoraFin()));
				errores.add("horafinal",new ActionMessage("errors.notEspecific", "La sumatoria de la duración de cada servicio supera el espacio reservado por la agenda. "));
			}
		}
		else if(forma.getTipoCitaCrear().equals(ConstantesBD.acronimoCancelarCitaOdon))
		{
			if (forma.getTipoCancelacion().equals(""))
			{
				errores.add("radiocancelacion",new ActionMessage("errors.required"," El Tipo Cancelación"));
			}
			
			if(forma.getCodMotivoCancelacion().equals("") && forma.getTipoCancelacion().equals(ConstantesBD.acronimoSi))
			{
				errores.add("motcancelacion",new ActionMessage("errors.required", "El Campo Motivo Cancelación por Paciente"));
			}
			
			if(forma.getCancelandoCitaOdoEstadoReprogramar().equals(ConstantesBD.acronimoNo) ){
				
				if(!forma.getEstadoCita().equals(ConstantesIntegridadDominio.acronimoAreprogramar))
				{
					if( UtilidadTexto.isEmpty(forma.getReprogramaCita()))
					{
						errores.add("cancelado por",new ActionMessage("errors.required", "El campo Cancelado por es requerido"));
					}
				}
			}
		}
		else if(forma.getTipoCitaCrear().equals(ConstantesIntegridadDominio.acronimoReprogramado))
		{
			if(!forma.getFechaReprogramacion().equals(""))
			{
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaReprogramacion()), UtilidadFecha.getFechaActual()))
					errores.add("fechareprogramacion",new ActionMessage("errors.fechaAnteriorIgualActual", "de Reprogramación","Actual "+UtilidadFecha.getFechaActual()));
			}
			else
				errores.add("fechareprog",new ActionMessage("errors.required", "La Fecha de Reprogramacion de la Cita "));
		}
		
		
		// validar hora inicio dependiendo del tipo de cita a crear
		// garantizar de que la cita que se vaya a generar no se en un tiempo ya caducado segun el tipo de cita a generar
		if(forma.getTipoCitaCrear().equals(ConstantesIntegridadDominio.acronimoReservado)
			|| forma.getTipoCitaCrear().equals(ConstantesBD.acronimoCambiarServicioCitaOdon))
		{
            Log4JManager.info("\n\n Hora Inicio >> "+forma.getHoraInicio()+" fecha Desplazamiento >> "+forma.getFechaDesplazamiento());			
			if(!UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaDesplazamiento())))
				if(forma.getHoraInicio().compareTo(UtilidadFecha.getHoraActual())<0)
					errores.add("horaInicio",new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "Inicial","del Sistema. "));

			/*!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaDesplazamiento())) && 
			if(!UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaDesplazamiento()))
					|| ( !UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaDesplazamiento())) && forma.getHoraInicio().compareTo(UtilidadFecha.getHoraActual())<0) )
				errores.add("horaInicio",new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "Inicial","del Sistema. "));
			*/
		}else{
			if(forma.getTipoCitaCrear().equals(ConstantesIntegridadDominio.acronimoAsignado)
				|| forma.getTipoCitaCrear().equals(ConstantesBD.acronimoCambiarServicioCitaOdon))
			{				
				
				Log4JManager.info("\n\nUtilidadFecha.getHoraActual(): "+UtilidadFecha.getHoraActual()+" reserva + param: "+UtilidadFecha.incrementarMinutosAHora(forma.getHoraInicio(),
						Utilidades.convertirAEntero(forma.getMinutosEsperaAsgCitOdoCad())));
				
				String horaActual=UtilidadFecha.getHoraActual();
				String horaValidar=UtilidadFecha.incrementarMinutosAHora(horaActual, -Utilidades.convertirAEntero(forma.getMinutosEsperaAsgCitOdoCad()));
				/*
				 * Existe la posibilidad de que al restarle minutos pase al día anterior
				 * Ej: 00:10 - 60 Min = 23:10, solamente si la hora actual es mayo a la hora
				 * a validar indica que se está trabajando el mismo día
				 */
				if(horaActual.compareTo(horaValidar)>=0)
				{
					if(UtilidadFecha.getFechaActual().equals(forma.getFecha()))
					{
						if(UtilidadFecha.esHoraMenorQueOtraReferencia(forma.getHoraInicio(), horaValidar))
						{
							errores.add("horaInicio",new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "Inicial","del Sistema más los minutos de espera para asignar citas odontológicas caducadas"));
						}
					}
				}
				
				/*
						
				if(!UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaDesplazamiento())))
					if(!UtilidadFecha.esHoraMenorIgualQueOtraReferencia(forma.getHoraInicio(),(UtilidadFecha.incrementarMinutosAHora(UtilidadFecha.getHoraActual(),
								Utilidades.convertirAEntero(forma.getMinutosEsperaAsgCitOdoCad()))))) 
					errores.add("horaInicio",new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "Inicial","del Sistema mï¿½s los minutos de espera para asignar citas odontolï¿½gicas caducadas. "));
				
				if(!UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaDesplazamiento()))
						|| (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaDesplazamiento())) 
							 && forma.getHoraInicio().compareTo(UtilidadFecha.incrementarMinutosAHora(UtilidadFecha.getHoraActual(),
								Utilidades.convertirAEntero(forma.getMinutosEsperaAsgCitOdoCad())))<0) )*/
			}
		}
		
		
		// validar centro de costo por unidad de agenda 
		// para las asignaciones de las citas odontologicas
		if(forma.getTipoCitaCrear().equals(ConstantesIntegridadDominio.acronimoAsignado))
		{
			if(forma.getCodCentroCostoXuniAgen()==ConstantesBD.codigoNuncaValido)
				errores.add("centroxuni",new ActionMessage("errors.required", "El Campo Centro de Costo "));
		}
		
		// --------- Validaciones adicionales agregadas en el Anexo 1131
		if(forma.getTipoCitaCrear().equals(ConstantesIntegridadDominio.acronimoAsignado))
		{	
			if(UtilidadTexto.isEmpty(forma.getCodigoArea())){
				errores.add("area",new ActionMessage("errors.required", "El Campo Area"));
			}
			if(forma.isMostrarSeccionconvenio())
			{
				if(UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio())){
					errores.add("convenio",new ActionMessage("errors.required", "El Campo Convenio"));
				}
				if(UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getCodigoContrato())){
					errores.add("contrato",new ActionMessage("errors.required", "El Campo Contrato"));
				}
				else
				{
					// Si se mostro la sección de convenio se debe tomar el contrato seleccionado
					boolean pacientePagaAtencion = Contrato.pacientePagaAtencion(Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoContrato()));
					if(pacientePagaAtencion)
					{
						boolean validaAbonoParaAtencionOdont = Contrato.pacienteValidaBonoAtenOdo(Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoContrato()));
						if(validaAbonoParaAtencionOdont)
						{
							if(forma.getValorTotalServicios() > forma.getAbonosDisponiblesPaciente())
							{
								errores.add("Error validacion abono", new ActionMessage("errors.notEspecific", "Los abonos disponibles "+
										forma.getAbonosDisponiblesPaciente()+" no cubre valor total del servicio: "+forma.getValorTotalServicios()));
							}
						}
					}
				}
			}
		}
		// ------------------------
		
		
		
		// validar el tipo de cita
		/*		
		if(forma.getCasoBusServicio().equals(ConstantesBD.acronimoSinPlanTratamiento))
		{
			if(!forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoPrioritaria)
					&& !forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial))
				errores.add("tipocita",new ActionMessage("errors.required", "El Tipo de Cita "));
		}else{
			if(forma.getCasoBusServicio().equals(ConstantesBD.acronimoConPlanTrataActivoEnProceso))
			{
				if(forma.getTipoCita().equals("")||forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTratamiento)){
					forma.setTipoCita(ConstantesIntegridadDominio.acronimoTratamiento);
				}else{
					if(!forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoPrioritaria))
						errores.add("tipocita",new ActionMessage("errors.notEspecific", "El Tipo de Cita No corresponde a las opciones postuladas. "));
				}
			}else{
				if(forma.getCasoBusServicio().equals(ConstantesBD.acronimoConPlanTratamientoTerminado))
				{
					if(!forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoPrioritaria)
							&& !forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTipoAtencionValoracionInicial)
							&& !forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoControlCitaOdon))
						errores.add("tipocita",new ActionMessage("errors.required", "El Tipo de Cita "));
				}else{
					if(forma.getCasoBusServicio().equals(ConstantesBD.acronimoConPlanTratamientoInactivo))
					{
						if(!forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoPrioritaria)
								&& !forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoRevaloracion))
							errores.add("tipocita",new ActionMessage("errors.required", "El Tipo de Cita "));
					}
				}
			}
		}*/
		
		if(forma.getTipoCitaCrear().equals(ConstantesIntegridadDominio.acronimoReservado)
			|| forma.getTipoCitaCrear().equals(ConstantesIntegridadDominio.acronimoAsignado))
		{
			// validar que seleccione un servicio
			if(forma.getServiciosOdon().size()>0)
			{
				
				int totalMinutos = 0;
				boolean seleccionoServicio = false;
				//boolean igualDiente=false;
				boolean existeHabiladoServ=false;
				//int ultimoSeleccionado=0;
				//int codigoDienteNuevo= 0, codigoDienteViejo= 0;
				
				for(int i=0;i<forma.getServiciosOdon().size();i++)
				{
					DtoServicioOdontologico dtoServicioOdontologico=forma.getServiciosOdon().get(i);
					Log4JManager.info("asociar: "+dtoServicioOdontologico.getAsociarSerCita());
					Log4JManager.info("Pieza Dental: "+dtoServicioOdontologico.getCodigoPiezaDental());
					Log4JManager.info("Descripcion: "+dtoServicioOdontologico.getDescripcionServicio());
//					codigoDienteNuevo=dtoServicioOdontologico.getCodigoPiezaDental();
//					if(i>0)
//					{
//						codigoDienteViejo=forma.getServiciosOdon().get(i-1).getCodigoPiezaDental();
//					}
					//igualDiente=false;
					
//					if(codigoDienteViejo>0 && codigoDienteNuevo==codigoDienteViejo)
//					{
//						igualDiente=true;
//					}
					
					if(dtoServicioOdontologico.getHabilitarSeleccion().equals(ConstantesBD.acronimoSi))
					{
						existeHabiladoServ=true;
					}
					if(dtoServicioOdontologico.getAsociarSerCita().equals(ConstantesBD.acronimoSi))
					{
						Log4JManager.info("Descripcion Servicio ASOCIADO >>>>>>>> "+dtoServicioOdontologico.getDescripcionServicio());
						seleccionoServicio = true;
						if(dtoServicioOdontologico.getMinutosduracionNuevos()<=0)
						{
							errores.add("servicio",new ActionMessage("errors.notEspecific", "Se requiere la duración (en minutos) del Servicio "+forma.getServiciosOdon().get(i).getDescripcionServicio()));
						}
						else
						{
							// Tarea XPLanner 2008 : 163210
							//if(dtoServicioOdontologico.getMinutosduracionNuevos()<dtoServicioOdontologico.getMinutosduracion())
							int multiploMinGenCitas=Integer.parseInt(forma.getMultiploMinGenCitas());
							
							if(dtoServicioOdontologico.getMinutosduracionNuevos()<multiploMinGenCitas)
							{
								errores.add("servicio minutos duracion menor",new ActionMessage("errors.notEspecific", "La duración (en minutos) del Servicio "+forma.getServiciosOdon().get(i).getDescripcionServicio()+" debe ser mayor o igual a "+multiploMinGenCitas));
							
							}else if (dtoServicioOdontologico.getMinutosduracionNuevos()%Integer.parseInt(forma.getMultiploMinGenCitas())!=0){
								
								errores.add("servicio minutos duracion multiplo  parametro",new ActionMessage("error.cita.duracionNoMultiploParametro", forma.getServiciosOdon().get(i).getDescripcionServicio(), multiploMinGenCitas));
							}
							
							totalMinutos += dtoServicioOdontologico.getMinutosduracionNuevos();
						}
						
						//---- 1131
						if(forma.isMostrarTarifaServicios())
						{
							if(dtoServicioOdontologico.getInfoTarifa() == null)
							{
								String mensaje = "Programa "+ forma.getServiciosOdon().get(i).getDescripcionServicio()+" no tiene Tarifa";
								errores.add("Programa sin tarifa",new ActionMessage("errors.notEspecific",mensaje));
							}
						}
						//----
						
						
						// Juan David - se quita la validación de órden de los servicios debido a que se va a reestructurar totalmente
						/*if(forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento) && ultimoSeleccionado+1<i && igualDiente&&forma.getServiciosOdon().get(i).getOrdenServicio()>1)//revisar esta validacion, creo que esta mal. (ARMANDO. el orden no se debe evaluar con respeto al ultimo seleccionado y la posicion en el array.)
						{							
							errores.add("hay un servicio anterior que no se ha realizado",new ActionMessage("errors.notEspecific", "No se puede asignar el servicio ("+dtoServicioOdontologico.getCodigoServicio()+": "+dtoServicioOdontologico.getDescripcionServicio()+"), puesto que hay servicios de menor orden no se han realizado."));
							return errores;
						}
						*/
						//ultimoSeleccionado=i;
					}
//					else
//					{
//						if(UtilidadTexto.getBoolean(dtoServicioOdontologico.getEstaVinculadoSerCita()))
//						{
//							ultimoSeleccionado=i;
//						}
//					}
				}
				if(!seleccionoServicio && existeHabiladoServ)
				{
					errores.add("servicio",new ActionMessage("errors.notEspecific", "Se requiere la selección de al menos un Servicio."));
				}
				else
				{
					if(existeHabiladoServ){	
						// Validaciï¿½n duración de la cita
						if(totalMinutos<=0)
						{
							errores.add("duracioncita",new ActionMessage("errors.notEspecific", "La sumatoria de la duración de los servicios debe ser mayor a cero (0)."));
						}
						Log4JManager.info("duracion "+totalMinutos);
						boolean asignarCupoAdicional=false;
						// Valido que la duración de la cita sea mï¿½ltiplo del parametro general Mï¿½ltiplo Munitos generaciï¿½n cita 
						if(totalMinutos%Integer.parseInt(forma.getMultiploMinGenCitas())!=0)
						{
							errores.add("", new ActionMessage("errors.notEspecific","La sumatoria total de duración de los servicios  de la cita ("+totalMinutos+") no es múltiplo de "+forma.getMultiploMinGenCitas()));
						}
						else
						{
							if(!UtilidadTexto.getBoolean(forma.getCuposExtra()))
							{
								// Valido la duración de la cita
								Log4JManager.info("duracionCita "+forma.getDuracionCita());
								Log4JManager.info("totalMinutos "+totalMinutos);
								if(totalMinutos>forma.getDuracionCita())
								{
									String fechaValidacion=forma.getFecha();
									String[] fechaHoraValidacion=UtilidadFecha.incrementarMinutosAFechaHora(fechaValidacion, forma.getHoraInicio(), totalMinutos, false);
									Log4JManager.info("fechaHoraValidacion[0] "+fechaHoraValidacion[0]);
									if(!fechaHoraValidacion[0].equals(forma.getFecha()))
									{
										errores.add("", new ActionMessage("errors.notEspecific","La duración del servicio supera el tiempo de la agenda seleccionada"));
									}
									else
									{
										DtoAgendaOdontologica dtoValidacion=new DtoAgendaOdontologica();
										dtoValidacion.setCodigoPk(forma.getCodigoAge());
										Log4JManager.info("forma.getPosicionCita() "+forma.getPosicionCita());
										if(forma.getPosicionCita()>=0 && (forma.getCodigoActividad().equals(ConstantesBD.acronimoCambiarServicioCitaOdon) ||
												forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoTipoFuncionalidadAsignar)))
										{
											Log4JManager.info("Si asigna");
											dtoValidacion.setHoraInicio(forma.getHoraFinal());
										}
										else
										{
											dtoValidacion.setHoraInicio(forma.getHoraInicio());
										}
										Log4JManager.info(dtoValidacion.getHoraInicio());
										Log4JManager.info("fechaHoraValidacion[1] "+fechaHoraValidacion[1]);
										dtoValidacion.setHoraFin(fechaHoraValidacion[1]);
										asignarCupoAdicional=CitaOdontologica.validarCuposSiguientesDisponibles(dtoValidacion);
										
										if(!asignarCupoAdicional)
										{
											errores.add("", new ActionMessage("errors.notEspecific","La agenda seleccionada se cruza con un espacio de tiempo ya ocupado"));
										
										}else{
											forma.setHoraFinal(fechaHoraValidacion[1]);
										}
									}								
								}
							}
							else if(totalMinutos<forma.getDuracionCita())
							{
								if(!UtilidadTexto.getBoolean(forma.getCuposExtra()))
								{
									forma.setHoraFinal(UtilidadFecha.incrementarMinutosAHora(forma.getHoraInicio(), totalMinutos));
								}
							}
						}
					}
				}
				if(errores.isEmpty())
				{
					if(!UtilidadTexto.getBoolean(forma.getCuposExtra()))
					{
						forma.setTotalDuracionServicios(totalMinutos);
						forma.setDuracionCita(totalMinutos);
					}
					else
					{
						forma.setTotalDuracionServicios(UtilidadFecha.numeroMinutosEntreFechas(forma.getFecha(), forma.getHoraInicio(), forma.getFecha(), forma.getHoraFinal()));
						forma.setDuracionCita(forma.getTotalDuracionServicios());
					}
				}else
				{
					 forma.setHoraInicio(horaInicioAux);
					 forma.setHoraFinal(horaFinalAux);
					 forma.setDuracionCita(duracionCitaAux);
					 forma.setTotalDuracionServicios(duracionTotalServAux);
				}
			  
				
			}else{
				errores.add("servicio",new ActionMessage("errors.notEspecific", "No existen servicios asociados."));
			}
		}
		return errores;
	}

	/**
	 * se carga el paciente segun el numero y el tipo de identificacion  
	 * @param mapping
	 * @param con
	 * @param forma
	 * @return
	 */
	@SuppressWarnings("unused")
	private ActionForward cargarPaciente(ActionMapping mapping, Connection con,
			AgendaOdontologicaForm forma, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors(); 
		forma.setPaciente(IngresoPacienteOdontologia.consultarPersonaPaciente(con, 
				forma.getNumeroIdentificacionPac(), 
				forma.getTipoIdentificacionPac(), ""));
		//forma.setNumeroIdentificacionPac(forma.getPaciente().getNumeroId());
		//forma.setTipoIdentificacionPac(forma.getPaciente().getTipoId());
		
		if(forma.getPaciente().getCodigo()<=0)
		{
			forma.resetPacBas();
			errores.add("fechaDesplazamiento",new ActionMessage("errors.paciente.noCargadoPac"));
			saveErrors(request, errores);
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/*
	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param forma
	 * @param mundo
	 * @return
	 *
	private ActionForward desplazamientoFecha(Connection con, ActionMapping mapping,
			HttpServletRequest request, AgendaOdontologicaForm forma,
			AgendaOdontologica mundo , HttpServletResponse response, UsuarioBasico usuario) {
		ActionErrors errores = new ActionErrors();
		String[] horaini = {""};
		String[] horafin = {""};
		String resultado = "";
		if(forma.getAgendaOdon().size()>0)
		{
			if(forma.getSentidoDesplazamiento().equals(ConstantesIntegridadDominio.acronimoUbicacionDerecha))
			{
				if(forma.getDesplazamientoFecha()!=ConstantesBD.codigoNuncaValido)
					forma.setFechaDesplazamiento(UtilidadFecha.incrementarDiasAFecha(
							UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaDesplazamiento()), 
							forma.getDesplazamientoFecha(), false));
				else
					forma.setFechaDesplazamiento(forma.getFechaFinal());
				
			}else{
				if(forma.getSentidoDesplazamiento().equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda))
				{
					if(forma.getDesplazamientoFecha()!=ConstantesBD.codigoNuncaValido)
						forma.setFechaDesplazamiento(UtilidadFecha.incrementarDiasAFecha(
								UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaDesplazamiento()), 
								ConstantesBD.codigoNuncaValido, false));
					else
						forma.setFechaDesplazamiento(forma.getFechaInicial());
				}
			}
			
			
			if(!UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getFechaDesplazamiento(), forma.getFechaInicial())
					&& UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaDesplazamiento(), forma.getFechaFinal()))
			{
				
				if(forma.getUnidadAgenda().equals(ConstantesBD.codigoNuncaValido+""))
					forma.setUnidadAgenda(forma.getListUnidadesAgendaXUsuario().get(forma.getListUnidadesAgendaXUsuario().size()-1).get("todos").toString());
				
				forma.setConvencionColores(mundo.convencionColorUnidaAgenda(con, 
						forma.getUnidadAgenda(),
						forma.getFechaDesplazamiento().equals("")?forma.getFechaInicial():forma.getFechaDesplazamiento()));
				
				forma.setConsultoriosAgenOdon(mundo.consultoriosAgendaOdontologica(forma.getAgendaOdon(),
						horaini, horafin, forma.getFechaDesplazamiento()));
				forma.setHoraIniAgenOdon(horaini[0]);
				forma.setHoraFinAgenOdon(horafin[0]);
				// se generan los string xml
				forma.setXmlAgenda(generacionXML(forma, false, usuario));
			}else{
				errores.add("fechaDesplazamiento",new ActionMessage("errors.notEspecific", "La Fecha de Desplazamiento estï¿½ fuera de los rango de bï¿½squeda selecionados."));
				saveErrors(request, errores);
			}
		}else{
			errores.add("fechaDesplazamiento",new ActionMessage("errors.notEspecific", "Se debe realizar una bï¿½squeda de Agendas Odontolï¿½gicas."));
			saveErrors(request, errores);
		}
		UtilidadBD.closeConnection(con);
		if(errores.isEmpty())
		{
			resultado = 
			"<respuesta>" +
			"<infoid>" +
				"<ind-reemplazo>"+ConstantesBD.acronimoSi+"</ind-reemplazo>"+
				"<sufijo>ajaxBusquedaTipoHidden</sufijo>" +
				"<id-hidden>fechaDesplazamiento</id-hidden>" +		
				"<contenido-hidden>"+forma.getFechaDesplazamiento()+"</contenido-hidden>"+
				"<sufijo>ajaxBusquedaTipoInnerHtml</sufijo>" +
				"<id-div>xmlParametros</id-div>" +		
				"<contenido>"+forma.getXmlParametros()+"</contenido>"+
				"<id-div>xmlConsultorios</id-div>" +		
				"<contenido>"+forma.getXmlConsultorios()+"</contenido>"+
				"<id-div>xmlAgendas</id-div>" +		
				"<contenido>"+forma.getXmlAgendas()+"</contenido>"+
				"<comand>"+forma.getFunctionPintarAgen()+"</comand>"+
			"</infoid>"+
			"</respuesta>";
			try
			{
				response.setContentType("text/xml");
				response.setHeader("Cache-Control", "no-cache");
		        response.getWriter().write(resultado);
			}
			catch(IOException e)
			{
				Log4JManager.error("Error al enviar respuesta AJAX en accionEstadoCalcularFechaIncre: "+e);
			}
			return null;	
		}
		return mapping.findForward("principal");
	}*/

	/**
	 * 
	 * @param forma
	 */
	private ArrayList<String> intevaloEntreHoraIniFin(String horaInicial, String horaFinal, int intervalo) 
	{
		ArrayList<String> array = new ArrayList<String>();
		String[] intevaloHoras = horaInicial.split(":");
		while(horaInicial.compareTo(horaFinal)<0)
		{
			array.add(intevaloHoras[0]+":"+intevaloHoras[1]);
			if((Utilidades.convertirAEntero(intevaloHoras[1])+intervalo)>=60)
			{
				intevaloHoras[0] = (Utilidades.convertirAEntero(intevaloHoras[0])+1)<10?"0"+(Utilidades.convertirAEntero(intevaloHoras[0])+1):(Utilidades.convertirAEntero(intevaloHoras[0])+1)+"";
				if((Utilidades.convertirAEntero(intevaloHoras[1])+intervalo)==60)
					intevaloHoras[1] = "00";
				else
					intevaloHoras[1] = ((Utilidades.convertirAEntero(intevaloHoras[1])+intervalo)-60)<10?"0"+((Utilidades.convertirAEntero(intevaloHoras[1])+intervalo)-60)+"":((Utilidades.convertirAEntero(intevaloHoras[1])+intervalo)-60)+"";
				
			}else{
				intevaloHoras[1] = ""+(Utilidades.convertirAEntero(intevaloHoras[1])+intervalo);
			}
			horaInicial = intevaloHoras[0]+":"+intevaloHoras[1];
		}
		array.add(horaInicial);
		return array;
	}
	
	/**
	 * se cargan los ArrayList iniciales para la generacion de la agenda
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void cargarDatosIniciales(Connection con,
			AgendaOdontologicaForm forma, UsuarioBasico usuario) {
		
		
		// se cargan los centros de atencion
		forma.setListCentroAtencion(
				UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(
						con, 
						usuario.getLoginUsuario(), 
						forma.getCodigoActividadAutorizacion(),
						ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica)
				);
		
		// se cargan las unidades de agenda por usuario
		forma.setListUnidadesAgendaXUsuario(
				UtilidadesConsultaExterna.unidadesAgendaXUsuarioArray(
						con, 
						usuario.getLoginUsuario(), 
						ConstantesBD.codigoNuncaValido, 
						forma.getCodigoActividadAutorizacion(),
						ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica, "")
					);
		
		// se carga los profesionales de la salud activos
//		forma.setListProfesionalesActivos(iyuiyui
//				UtilidadesAdministracion.obtenerProfesionales(
//						con, 
//						usuario.getCodigoInstitucionInt(), 
//						ConstantesBD.codigoNuncaValido, 
//						false, true,ConstantesBD.codigoNuncaValido)
//					);
		
		// se cargan los tipos de identificacion
		forma.setListTipIdent(Utilidades.obtenerTiposIdentificacion(con,
				"ingresoPoliza", 
				usuario.getCodigoInstitucionInt()));
		
	}
	
	/**
	 * metodo que retona un string xml de parametros
	 * @param forma
	 * @param usuario 
	 * @return
	 */
	public String generacionXMLParametros(AgendaOdontologicaForm forma, UsuarioBasico usuario)
	{
		StringBuffer xml = new StringBuffer();
		String etiquetaAbrir = "<", etiquetaCerrar = ">";
		String[] horaInicio = forma.getHoraIniAgenOdon().split(":");
		String[] horaFinal = forma.getHoraFinAgenOdon().split(":");
		
		String horaActual="00:00";
		String horaPosibleAsignacion="00:00";
		String fechaActual=UtilidadFecha.getFechaActual();
		
		String[] fechaHora=UtilidadFecha.incrementarMinutosAFechaHora(fechaActual, horaActual,-Utilidades.convertirAEntero(forma.getMinutosEsperaAsgCitOdoCad()), false);
		if(forma.getFechaDesplazamiento().equals(fechaActual))
		{
			horaActual=UtilidadFecha.getHoraActual();
			if(fechaHora[0].equals(fechaActual) || fechaHora[0].equals(forma.getFechaDesplazamiento()))
			{
				horaPosibleAsignacion=fechaHora[1];
			}
		}
		else if(fechaHora[0].equals(forma.getFechaDesplazamiento()))
		{
			horaPosibleAsignacion=fechaHora[1];
		}
		else if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getFechaDesplazamiento(), fechaActual) && UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getFechaDesplazamiento(), fechaHora[0]))
		{
			horaPosibleAsignacion="23:59";
		}
		
		xml.append(etiquetaAbrir+"agenda"+etiquetaCerrar);
		xml.append(etiquetaAbrir+"intervalo"+etiquetaCerrar+ValoresPorDefecto.getMultiploMinGeneracionCita(usuario.getCodigoInstitucionInt())+etiquetaAbrir+"/intervalo"+etiquetaCerrar);
		xml.append(etiquetaAbrir+"horaInicio"+etiquetaCerrar+Integer.valueOf(horaInicio[0])+etiquetaAbrir+"/horaInicio"+etiquetaCerrar);
		xml.append(etiquetaAbrir+"minInicio"+etiquetaCerrar+Integer.valueOf(horaInicio[1])+etiquetaAbrir+"/minInicio"+etiquetaCerrar);
		xml.append(etiquetaAbrir+"horaFin"+etiquetaCerrar+Integer.valueOf(horaFinal[0])+etiquetaAbrir+"/horaFin"+etiquetaCerrar);
		xml.append(etiquetaAbrir+"minFin"+etiquetaCerrar+Integer.valueOf(horaFinal[1])+etiquetaAbrir+"/minFin"+etiquetaCerrar);
		xml.append(etiquetaAbrir+"horaActual"+etiquetaCerrar+horaActual+etiquetaAbrir+"/horaActual"+etiquetaCerrar);
		xml.append(etiquetaAbrir+"horaPosibleAsignacion"+etiquetaCerrar+horaPosibleAsignacion+etiquetaAbrir+"/horaPosibleAsignacion"+etiquetaCerrar);
		
		xml.append(etiquetaAbrir+"actividad"+etiquetaCerrar+forma.getCodigoActividad()+etiquetaAbrir+"/actividad"+etiquetaCerrar);
		
		xml.append(etiquetaAbrir+"/agenda"+etiquetaCerrar);
		
		Log4JManager.info("Xml parametros: "+xml);
		
		return xml.toString();
	}
	
	/**
	 * metodo que retorna un string xml de consultorios
	 * @param forma Formulario
	 * @param usuario Usuario que genera la búsqueda
	 * @param con Conexión con la BD
	 * @return String con el XML de cada una de las agendas
	 */
	public String generacionXMLConsultorios(AgendaOdontologicaForm forma, UsuarioBasico usuario, Connection con)
	{
		StringBuffer xml = new StringBuffer();
		String etiquetaAbrir = "<", etiquetaCerrar = ">", horaInicio = "", horaFinal = "";
		/**xml.append(etiquetaAbrir+"Consultorios"+etiquetaCerrar);
		for(InfoDatosInt elem: forma.getConsultoriosAgenOdon())
		{
			xml.append(etiquetaAbrir+"Consultorio"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"nombre"+etiquetaCerrar+elem.getNombre()+etiquetaAbrir+"/nombre"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"codigo_consultorio"+etiquetaCerrar+elem.getCodigo()+etiquetaAbrir+"/codigo_consultorio"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"/Consultorio"+etiquetaCerrar);
		}
		xml.append(etiquetaAbrir+"/Consultorios"+etiquetaCerrar);**/
		
		xml.append(etiquetaAbrir+"consultorios"+etiquetaCerrar);
		
		for(InfoDatosInt consultorio:forma.getConsultoriosAgenOdon())
		{
	//		Log4JManager.info("Primer For XML Consultorios");
			xml.append(etiquetaAbrir+"consultorio"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"numero"+etiquetaCerrar+consultorio.getNombre()+etiquetaAbrir+"/numero"+etiquetaCerrar);
			
			for(DtoAgendaOdontologica dto:forma.getAgendaOdon())
			{
		//		Log4JManager.info("Segundo For XML Consultorios");
				
				if(dto.getConsultorio()==consultorio.getCodigo())
				{
					xml.append(etiquetaAbrir+"generadas");
					if(UtilidadesConsultaExterna.esActividadAurtorizada(con, dto.getUnidadAgenda(), ConstantesBD.codigoActividadAutorizadaAsignarCitas, usuario.getLoginUsuario(), forma.getCentroAtencion(), true))
					{
						xml.append(" permiteAsignar='true'");
					}
					else
					{
						xml.append(" permiteAsignar='false'");
					}
					if(UtilidadesConsultaExterna.esActividadAurtorizada(con, dto.getUnidadAgenda(), ConstantesBD.codigoActividadAutorizadaReservarCitas, usuario.getLoginUsuario(), forma.getCentroAtencion(), true))
					{
						xml.append(" permiteReservar='true'");
					}
					else
					{
						xml.append(" permiteReservar='false'");
					}
					if(UtilidadesConsultaExterna.esActividadAurtorizada(con, dto.getUnidadAgenda(), ConstantesBD.codigoActividadAutorizadaCuposExtra, usuario.getLoginUsuario(), forma.getCentroAtencion(), true))
					{
						xml.append(" permiteCupoExtra='true'");
					}
					else
					{
						xml.append(" permiteCupoExtra='false'");
					}
					xml.append(etiquetaCerrar);
					xml.append(etiquetaAbrir+"codigo_pk"+etiquetaCerrar+dto.getCodigoPk()+etiquetaAbrir+"/codigo_pk"+etiquetaCerrar);
					xml.append(etiquetaAbrir+"color"+etiquetaCerrar+dto.getColorUniAgen()+etiquetaAbrir+"/color"+etiquetaCerrar);
					
					horaInicio = dto.getHoraInicio();
					horaFinal = dto.getHoraFin();
					xml.append(etiquetaAbrir+"cupos"+etiquetaCerrar);

					String fechaTmp[] = {forma.getFecha(), horaInicio};
/*					Log4JManager.info("fechaTmp[0] "+fechaTmp[0]);
					Log4JManager.info("fechaTmp[1] "+fechaTmp[1]);
					Log4JManager.info("horaFinal "+horaFinal);
	*/				
					while ( (fechaTmp[0].equals(forma.getFecha())) && (UtilidadFecha.esHoraMenorQueOtraReferencia(fechaTmp[1], horaFinal)) )
					{
/*						Log4JManager.info("fechatmp [0]" + fechaTmp[0] + " fechatmp[1]" + fechaTmp[1] + " ");
						Log4JManager.info("Primer While XML Consultorios");
						Log4JManager.info("Hora inicio: " + horaInicio + " MultiploMinGenCitas: " + forma.getMultiploMinGenCitas()+ " Hora Final: "+horaFinal);
						Log4JManager.info("Fecha: " + forma.getFecha() + " Fecha Inicial: " + forma.getFechaInicial()+ " Fecha Final: "+forma.getFechaFinal());
	*/					
						xml.append(etiquetaAbrir+"cupo"+etiquetaCerrar);
						xml.append(etiquetaAbrir+"hora"+etiquetaCerrar+fechaTmp[1]+etiquetaAbrir+"/hora"+etiquetaCerrar);
						Integer cantidadCuposDisponibles=dto.getCuposEspacioTiempo().get(fechaTmp[1]);
						xml.append(etiquetaAbrir+"cuposExtra"+etiquetaCerrar+cantidadCuposDisponibles+etiquetaAbrir+"/cuposExtra"+etiquetaCerrar);
						xml.append(etiquetaAbrir+"profesional"+etiquetaCerrar+dto.getNombreMedico()+etiquetaAbrir+"/profesional"+etiquetaCerrar);
						int duracion=traerDuracionCita(dto.getCitasOdontologicas(), fechaTmp[1]);
						if(duracion>0)
						{
							xml.append(etiquetaAbrir+"duracion"+etiquetaCerrar+duracion+etiquetaAbrir+"/duracion"+etiquetaCerrar);
						}
						xml.append(etiquetaAbrir+"especialidad"+etiquetaCerrar+dto.getNombreEspecialidadUniAgen()+etiquetaAbrir+"/especialidad"+etiquetaCerrar);
						xml.append(etiquetaAbrir+"/cupo"+etiquetaCerrar);
						
						//horaInicio = UtilidadFecha.incrementarMinutosAHora(horaInicio, Utilidades.convertirAEntero(forma.getMultiploMinGenCitas()));
						fechaTmp = UtilidadFecha.incrementarMinutosAFechaHora(forma.getFecha(), fechaTmp[1], Utilidades.convertirAEntero(forma.getMultiploMinGenCitas()), false);
					}
					xml.append(etiquetaAbrir+"/cupos"+etiquetaCerrar);
					
					xml.append(etiquetaAbrir+"citas"+etiquetaCerrar);
					
					//Se recorren las citas que contengan la misma hora inicio de la agenda
					for(DtoCitaOdontologica cita:dto.getCitasOdontologicas())
					{
		//				Log4JManager.info("Tercer For XML Consultorios");
						
						xml.append(etiquetaAbrir+"cita"+etiquetaCerrar);
						xml.append(etiquetaAbrir+"estado"+etiquetaCerrar+ValoresPorDefecto.getIntegridadDominio(cita.getEstado())+etiquetaAbrir+"/estado"+etiquetaCerrar);
						xml.append(etiquetaAbrir+"tipo"+etiquetaCerrar+ValoresPorDefecto.getIntegridadDominio(cita.getTipo())+etiquetaAbrir+"/tipo"+etiquetaCerrar);
						xml.append(etiquetaAbrir+"plan"+etiquetaCerrar+ValoresPorDefecto.getIntegridadDominio(cita.getEstadoPlanTratamiento())+etiquetaAbrir+"/plan"+etiquetaCerrar);
						xml.append(etiquetaAbrir+"duracion"+etiquetaCerrar+cita.getDuracion()+etiquetaAbrir+"/duracion"+etiquetaCerrar);
						xml.append(etiquetaAbrir+"paciente"+etiquetaCerrar);
						xml.append(etiquetaAbrir+"nombre"+etiquetaCerrar+cita.getNombrePaciente()+etiquetaAbrir+"/nombre"+etiquetaCerrar);
						xml.append(etiquetaAbrir+"tipoID"+etiquetaCerrar+cita.getTipoIdentificacionPac()+etiquetaAbrir+"/tipoID"+etiquetaCerrar);
						xml.append(etiquetaAbrir+"numeroID"+etiquetaCerrar+cita.getNumeroIdentificacionPac()+etiquetaAbrir+"/numeroID"+etiquetaCerrar);
						xml.append(etiquetaAbrir+"/paciente"+etiquetaCerrar);
						xml.append(etiquetaAbrir+"especialidad"+etiquetaCerrar+dto.getNombreEspecialidadUniAgen()+etiquetaAbrir+"/especialidad"+etiquetaCerrar);
						xml.append(etiquetaAbrir+"hora_inicio"+etiquetaCerrar+cita.getHoraInicio()+etiquetaAbrir+"/hora_inicio"+etiquetaCerrar);
						xml.append(etiquetaAbrir+"hora_fin"+etiquetaCerrar+cita.getHoraFinal()+etiquetaAbrir+"/hora_fin"+etiquetaCerrar);
						
						xml.append(etiquetaAbrir+"servicios"+etiquetaCerrar);
						
						//Se almacenan los servicios de la cita
						for(DtoServicioCitaOdontologica servicio:cita.getServiciosCitaOdon())
						{
			//				Log4JManager.info("Cuarto For XML Consultorios");
							
							xml.append(etiquetaAbrir+"servicio"+etiquetaCerrar);
							xml.append(etiquetaAbrir+"nombre"+etiquetaCerrar+servicio.getNombreServicio()+etiquetaAbrir+"/nombre"+etiquetaCerrar);
							
							//Se almacenan las condiciones del servicio
							if(servicio.getCondicionesServicio().size()>0)
							{
								xml.append(etiquetaAbrir+"condiciones"+etiquetaCerrar);
								for(InfoDatosInt condicion:servicio.getCondicionesServicio())
								{
									xml.append(etiquetaAbrir+"condicion"+etiquetaCerrar+condicion.getNombre()+etiquetaAbrir+"/condicion"+etiquetaCerrar);
								}
								xml.append(etiquetaAbrir+"/condiciones"+etiquetaCerrar);
							}							
							xml.append(etiquetaAbrir+"/servicio"+etiquetaCerrar);
						}
						xml.append(etiquetaAbrir+"/servicios"+etiquetaCerrar);
						
						xml.append(etiquetaAbrir+"/cita"+etiquetaCerrar);
					}
					
					xml.append(etiquetaAbrir+"/citas"+etiquetaCerrar);
					
					xml.append(etiquetaAbrir+"/generadas"+etiquetaCerrar);
				}
			}
			
			xml.append(etiquetaAbrir+"/consultorio"+etiquetaCerrar);
		}
		
		xml.append(etiquetaAbrir+"/consultorios"+etiquetaCerrar);
		
		
		return xml.toString();
	}

	private int traerDuracionCita(ArrayList<DtoCitaOdontologica> citasOdontologicas, String horaCita)
	{
		for(DtoCitaOdontologica cita:citasOdontologicas)
		{
			if(cita.getHoraInicio().equals(horaCita))
			{
				return cita.getDuracion();
			}
		}
		return 0;
	}

	/**
	 * metodo que retorna un string xml de agendas
	 * @param forma
	 * @return
	 */
	public String generacionXMLAgendas(AgendaOdontologicaForm forma)
	{
		StringBuffer xml = new StringBuffer();
		String etiquetaAbrir = "<", etiquetaCerrar = ">";
		
		SortCitasOdontologicas sort= new SortCitasOdontologicas();
		sort.setPatronOrdenar("horainicio");
		ArrayList<InfoDatosInt> convenciones = new ArrayList<InfoDatosInt>();
		
		for(DtoAgendaOdontologica dto:forma.getAgendaOdon())
		{
			boolean existe = false;
			
			for(InfoDatosInt convencion:convenciones)
			{
				if(dto.getUnidadAgenda()==convencion.getCodigo())
				{
					existe = true;
				}
			}
			
			if(!existe)
			{
				InfoDatosInt convencion = new InfoDatosInt(dto.getUnidadAgenda(),dto.getDescripcionUniAgen(),dto.getColorUniAgen());
				convenciones.add(convencion);
			}
		}
		
		xml.append(etiquetaAbrir+"convenciones"+etiquetaCerrar);
		for(InfoDatosInt convencion:convenciones)
		{
			xml.append(etiquetaAbrir+"convencion"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"nombre"+etiquetaCerrar+convencion.getNombre()+etiquetaAbrir+"/nombre"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"color"+etiquetaCerrar+convencion.getDescripcion()+etiquetaAbrir+"/color"+etiquetaCerrar);
			xml.append(etiquetaAbrir+"/convencion"+etiquetaCerrar);
		}
		xml.append(etiquetaAbrir+"/convenciones"+etiquetaCerrar);
		
		return xml.toString();
	}
	
	/**
	 * metodo que devuelve la posicion del consultorio
	 * @param codigoConsultorio
	 * @param forma
	 * @return
	 */
	public int getPosicionConsultorio(int codigoConsultorio, AgendaOdontologicaForm forma)
	{
		int pos = 1;
		for(InfoDatosInt elem: forma.getConsultoriosAgenOdon())
		{
			if(elem.getCodigo()==codigoConsultorio)
				return pos;
			pos++;
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * metodo que me devuelve un html de convenciones de colores
	 * @param forma
	 * @return
	 */
	public ActionForward generarConvencionColores(Connection con, 
			AgendaOdontologica mundo, 
			AgendaOdontologicaForm forma,
			HttpServletResponse response)
	{
		StringBuffer html = new StringBuffer();
		String resultado = "";
		String bgColor = "";
		if(forma.getUnidadAgenda().equals(ConstantesBD.codigoNuncaValido+""))
			forma.setUnidadAgenda(forma.getListUnidadesAgendaXUsuario().get(forma.getListUnidadesAgendaXUsuario().size()-1).get("todos").toString());
		
		forma.setConvencionColores(AgendaOdontologica.convencionColorUnidaAgenda(con, 
				forma.getUnidadAgenda(),
				forma.getFechaDesplazamiento().equals("")?forma.getFechaInicial():forma.getFechaDesplazamiento()));
		UtilidadBD.closeConnection(con);
		html.append("<table width=\"100%\" border=\"0\" cellspacing=\"1\" cellpadding=\"2\">");
		if(forma.getConvencionColores().size()>0)
		{
			int cont = 0;
			for(int j=0;j<forma.getConvencionColores().size();j++)
			{
				InfoDatosString elem = (InfoDatosString) forma.getConvencionColores().get(j);
				if(cont==0 || cont==4){
					cont = 0;
					html.append("<tr bgcolor=\"#FFFFFF\">");
				}
				bgColor = elem.getDescripcion().equals("")?"#000000":elem.getDescripcion();
				html.append("<td bgcolor=\""+bgColor+"\" width=\"2%\"></td>");
				html.append("<td width=\"20%\">"+elem.getNombre()+"</td>");
				cont++;
				if(cont==0 || cont==4){
					html.append("</tr>");
				}
			}
			if(cont<4)
			{
				html.append("<td bgcolor=\"#FFFFFF\" width=\"2%\"></td>");
				html.append("<td  width=\"20%\">Sin Horario de Atenci&oacute;n</td>");
			}else{
				html.append("<tr bgcolor=\"#FFFFFF\">");
				html.append("<td bgcolor=\"#FFFFFF\" width=\"2%\"></td>");
				html.append("<td  width=\"20%\">Sin Horario de Atenci&oacute;n</td>");
				html.append("</tr>");
			}
		}else{
			html.append("<tr bgcolor=\"FFFFFF\">");
			html.append("<td>No se Encontraron Convenciones de Colores de las Unidades de Agendas Odontologicas</td>");
			html.append("</tr>");
		}
		html.append("</table>");
		resultado = html.toString();
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write(resultado);
		}
		catch(IOException e)
		{
			Log4JManager.error("Error al enviar respuesta AJAX en accionEstadoCalcularFechaIncre: "+e);
		}
		return null;
	}
	
	/**
	 * metodo de ordenamiento de las citas odontologicas de un paciente especifico
	 * @param mapping
	 * @param con
	 * @param forma
	 * @return
	 */
	private ActionForward accionOrdenarCitasPac(ActionMapping mapping, Connection con, AgendaOdontologicaForm forma) 
	{
		SortCitasOdontologicas sort= new SortCitasOdontologicas();
		sort.setPatronOrdenar(forma.getPatronOrdenarCitas());
		Collections.sort(forma.getCitasOdonPac(), sort);
		forma.setEstado("realizarBusquedaRango");
		return mapping.findForward("principalXRango");
	}
	
	/**
	 * metodo de ordenamiento de los beneficiarios de un paciente especifico
	 * @param mapping
	 * @param con
	 * @param forma
	 * @return
	 */
	@SuppressWarnings("unused")
	private ActionForward accionOrdenarBenPac(ActionMapping mapping, Connection con, AgendaOdontologicaForm forma) 
	{
		SortBeneficiarios sort= new SortBeneficiarios();
		sort.setPatronOrdenar(forma.getPatronOrdenarBen());
		Collections.sort(forma.getPaciente().getBeneficiariosPac(), sort);
		return mapping.findForward("beneficiarios");
	}
	
	/**
	 * obtener centros costos x unidad de agenda
	 * @param con
	 * @param codigoUnidadAgenda
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerCentrosCostoXUniAgenda(Connection con, String codigoUnidadAgenda, int centroAtencion)
	{
		return UtilidadesConsultaExterna.consultarCentrosCostoXUnidadAgenda(con, codigoUnidadAgenda, centroAtencion);
	}

	/**
	 * metodo de impresion cita odontologica
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param forma
	 * @param paciente
	 * @return
	 */
	private ActionForward accionImprimirCitaOdontologica(ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request, AgendaOdontologicaForm forma, PersonaBasica paciente) 
	{
		Log4JManager.info("Unidad >>"+forma.getCitaCreada().getAgendaOdon().getDescripcionUniAgen() +" Medico>>"+forma.getCitaCreada().getAgendaOdon().getNombreMedico()); 
		forma.getCitaCreada().getAgendaOdon().setDescripcionUniAgen(forma.getAgendaSel().getDescripcionUniAgen());
		forma.getCitaCreada().getAgendaOdon().setNombreMedico(forma.getAgendaSel().getNombreMedico());
		forma.getCitaCreada().getAgendaOdon().setDescripcionConsultorio(forma.getAgendaSel().getDescripcionConsultorio());
		forma.getCitaCreada().getAgendaOdon().setFecha(forma.getAgendaSel().getFecha());
		forma.getCitaCreada().setHoraInicio(forma.getAgendaSel().getHoraInicio());
		forma.getCitaCreada().setDuracion(forma.getCitaAgendaSel().getDuracion());
		forma.getCitaCreada().setCodigoPk(forma.getCitaAgendaSel().getCodigoPk());
		forma.getCitaCreada().setEstado(forma.getCitaAgendaSel().getEstado());
		request.setAttribute("nombreArchivo", CitasOdontologicasPdf.pdfCitaOdontologica(usuario, request, 
	    			forma.getCitaCreada().getAgendaOdon(),
	    			forma.getCitaCreada(),
	    			paciente));
    	request.setAttribute("nombreVentana", "Citas Odontológica");    	
    	return mapping.findForward("abrirPdf");
	}
	

	
	//********************************************************** INICIO ANDRES ORTIZ **********************************************

	/**
	 * Metodo para empezar la busqueda se cargan los parametros de busqueda 
	 */
	private ActionForward iniciarParametrosBusquedaXRango(Connection con,ActionMapping mapping,AgendaOdontologicaForm forma, UsuarioBasico usuario)	
	{		     
		forma.reset();
		forma.setDtoBusquedaAgendaRango(new DtoBusquedaAgendaRango());
		forma.setTipoFlujoPaciente(ConstantesBD.acronimoNo);
		forma.setNumDiasAntFechaActual(ValoresPorDefecto.getNumDiasAntFActualAgendaOd(usuario.getCodigoInstitucionInt()));
		forma.setMultiploMinGenCitas(ValoresPorDefecto.getMultiploMinGeneracionCita(usuario.getCodigoInstitucionInt()));
		forma.setValidaPresupuestoContratado(ValoresPorDefecto.getValidaPresupuestoOdoContratado(usuario.getCodigoInstitucionInt()));
		forma.setUtilProgOdonPlanTra(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()));
		forma.setMinutosEsperaAsgCitOdoCad(ValoresPorDefecto.getMinutosEsperaAsignarCitasOdoCaducadas(usuario.getCodigoInstitucionInt()));
		
		
		forma.setPaises(UtilidadesConsultaExterna.obtenerPaisesPermitidosXUsuarioXUnidadAgendaXCentroAtencion(con, usuario.getLoginUsuario(),ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica));		
		accionCargarPaises(con,forma,usuario,mapping);
		
		try {
			UtilidadBD.cerrarConexion(con);
		} catch (SQLException e) {
			Log4JManager.error("Error cerrando la conexión",e);
		}
		
		pasarValoresFormaABusquedaXRango(forma);
		
		return mapping.findForward("principalXRango");
	}
	
	
	/**
	 * Metodo para realizar la reserva de una cita Odontologica existente
	 * @param con
	 * @param usuario
	 * @param forma
	 * @param mundo
	 * @param mundoCita
	 * @param persona
	 * @return
	 */
	private boolean modificarCitaXReserva(Connection con, UsuarioBasico usuario,AgendaOdontologicaForm forma, AgendaOdontologica mundo, PersonaBasica persona) {
		
		String estadoCita=forma.getCitasOdonPac().get(forma.getPosicionCita()).getEstado();
		
		HashMap parametros= new HashMap();
		parametros.put("codigoCita", forma.getCitasOdonPac().get(forma.getPosicionCita()).getCodigoPk());
		parametros.put("fecha",forma.getFecha());
		parametros.put("horaInicioCita", forma.getCitasOdonPac().get(forma.getPosicionCita()).getHoraInicio());	
		parametros.put("horaFinalCita", forma.getCitasOdonPac().get(forma.getPosicionCita()).getHoraFinal());
		parametros.put("horaInicio",forma.getHoraInicio());
		parametros.put("horaFinal", forma.getHoraFinal());
		parametros.put("codAgendaAnterior",forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgenda());
		parametros.put("codAgendaActual",forma.getAgendaSel().getCodigoPk());
		parametros.put("codUnidadAgendaAnterior",forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon().getUnidadAgenda());
		parametros.put("codUnidadAgendaActual",forma.getAgendaSel().getUnidadAgenda());
		parametros.put("usuario", usuario.getLoginUsuario());
		parametros.put("codInstitucion", usuario.getCodigoInstitucionInt());
		parametros.put("estadoCita", estadoCita);
	    
		int codigoPKCita=forma.getCitasOdonPac().get(forma.getPosicionCita()).getCodigoPk();				
	
		DtoCitaOdontologica dto = new DtoCitaOdontologica();
		dto.setCodigoPk(codigoPKCita);
		dto.setEstado(ConstantesIntegridadDominio.acronimoReservado);
		dto.setTipo(forma.getTipoCita());		
		dto.setAgenda(forma.getAgendaSel().getCodigoPk());
		dto.setDuracion(forma.getDuracionCita());
		dto.setHoraInicio(forma.getHoraInicio());
		dto.setHoraFinal(forma.getHoraFinal());
		dto.setFechaReserva(UtilidadFecha.getFechaActual());
		dto.setHoraReserva(UtilidadFecha.getHoraActual());
		dto.setUsuarioReserva(usuario.getLoginUsuario());
		dto.setPorConfirmar(ConstantesBD.acronimoNo);
		dto.setCodigoPaciente(persona.getCodigoPersona());// 2420 
		//dto.setConvenio(Utilidades.convertirAEntero(forma.getConvenioPac()));// persona.getCodigoConvenio() - ConstantesBD.codigoNuncaValido 
		dto.setUsuarioModifica(usuario.getLoginUsuario());
		
		for(DtoServicioOdontologico elem: forma.getServiciosOdon())
		{
			if(elem.getAsociarSerCita().equals(ConstantesBD.acronimoSi))
			{			
				
				DtoServicioCitaOdontologica dtoSer = new DtoServicioCitaOdontologica();
				dtoSer.setServicio(elem.getCodigoServicio());
				dtoSer.setCodigoPrograma(elem.getCodigoPrograma());
				dtoSer.setDuracion(elem.getMinutosduracionNuevos());
				dtoSer.setProgramaHallazgoPieza(elem.getProgramaHallazgoPieza());

				dtoSer.setActivo(ConstantesBD.acronimoSi);
				dtoSer.setUsuarioModifica(usuario.getLoginUsuario());
				dtoSer.setEstadoCita(ConstantesIntegridadDominio.acronimoReservado);
				dtoSer.setCodigoAgenda(forma.getAgendaSel().getCodigoPk());
				dtoSer.setFechaCita(forma.getAgendaSel().getFecha());
				dtoSer.setHoraInicio(forma.getHoraInicio());
				dtoSer.setHoraFinal(forma.getHoraFinal());
				dtoSer.setUnidadAgenda(forma.getAgendaSel().getUnidadAgenda());
				dtoSer.setAplicaAbono(ConstantesBD.acronimoNo);
				dtoSer.setAplicaAnticipo(ConstantesBD.acronimoNo);
				dto.getServiciosCitaOdon().add(dtoSer);
			}
		}
		UtilidadBD.iniciarTransaccion(con);
		if(mundo.modificarCitaOdontologica(con, dto, parametros)>0){
			 forma.setProcesoExito(ConstantesBD.acronimoSi);
			 UtilidadBD.finalizarTransaccion(con);
			 UtilidadBD.closeConnection(con);
			 return true;
		   }else
	    	{
			   UtilidadBD.abortarTransaccion(con);
			   UtilidadBD.closeConnection(con);
			  return false;
		    }
	}
	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @return
	 */
   private ActionErrors accionValidarBusquedaXRango(AgendaOdontologicaForm forma, UsuarioBasico usuario) 
	{
		ActionErrors errores = new ActionErrors();
		
		String fechaSistema = UtilidadFecha.getFechaActual();
		fechaSistema = UtilidadFecha.incrementarDiasAFecha(fechaSistema,Utilidades.convertirAEntero(ValoresPorDefecto.getNumDiasAntFActualAgendaOd(usuario.getCodigoInstitucionInt()),true)*-1, false);
	
		if(forma.getDtoBusquedaAgendaRango().getPais().equals(""))
		{
			errores.add("", new ActionMessage("errors.required","El país"));
		}
		if(forma.getDtoBusquedaAgendaRango().getCiudad().equals(""))
		{
			errores.add("", new ActionMessage("errors.required","La ciudad"));
		}
		if(forma.getDtoBusquedaAgendaRango().getUnidadAgenda().equals(""))
		{
			errores.add("", new ActionMessage("errors.required","La unidad de agenda"));
		}
		if(forma.getDtoBusquedaAgendaRango().getFechaInicial().trim().equals(""))
		{
			errores.add("", new ActionMessage("errors.required","La fechaInicial"));
		}
		else 
		{
			if(!UtilidadFecha.validarFecha(forma.getDtoBusquedaAgendaRango().getFechaInicial()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido","Fecha Inicial"));
			}
			else
			{ 
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getDtoBusquedaAgendaRango().getFechaInicial(), fechaSistema))
				{
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual",""," ("+fechaSistema+")"));
				}
			}	
		}    
		if(forma.getDtoBusquedaAgendaRango().getFechaFinal().trim().equals(""))
		{
			errores.add("", new ActionMessage("errors.required","La fechaFinal"));
		}
		else 
		{
			if(!UtilidadFecha.validarFecha(forma.getDtoBusquedaAgendaRango().getFechaInicial()))		  
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido","Fecha Final"));
			}else
			{
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getDtoBusquedaAgendaRango().getFechaFinal(), fechaSistema))
				{
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual",""," ("+fechaSistema+")"));
				}
			}
		}
		return errores;
	}
   
   
 
   /**
    * Metodo para realizar la busqueda de las citas Odontologicas asociadas a unos parametros de busqueda ( Por Rango )
    * @param forma
    * @param usuario
    * @param mapping
    * @param request
    * @param mundo
    * @return
    */
	private ActionForward accionRealizarBusquedaXRango(Connection con,AgendaOdontologicaForm forma, UsuarioBasico usuario,ActionMapping mapping, HttpServletRequest request,
			AgendaOdontologica mundo) {
		
		ActionErrors errores = new ActionErrors();
		HashMap parametrosBusqueda= new HashMap();
		errores = accionValidarBusquedaXRango(forma, usuario);
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			
		}
		else
		{
			parametrosBusqueda.put("codPais", forma.getDtoBusquedaAgendaRango().getPais());
			parametrosBusqueda.put("codCiudad", forma.getDtoBusquedaAgendaRango().getCiudad().split(ConstantesBD.separadorSplit)[1]);
			parametrosBusqueda.put("codCentroAtencion", forma.getDtoBusquedaAgendaRango().getCentroAtencion());
			parametrosBusqueda.put("tipoCita", forma.getDtoBusquedaAgendaRango().getTipoCita());
			parametrosBusqueda.put("unidadAgenda", forma.getDtoBusquedaAgendaRango().getUnidadAgenda());
			parametrosBusqueda.put("fechaInicial", forma.getDtoBusquedaAgendaRango().getFechaInicial());			
			parametrosBusqueda.put("fechaFinal", forma.getDtoBusquedaAgendaRango().getFechaFinal());			
			parametrosBusqueda.put("profesionalSalud", forma.getDtoBusquedaAgendaRango().getProfesionalSalud());
			parametrosBusqueda.put("estadoCita", forma.getDtoBusquedaAgendaRango().getEstadoCita());
			parametrosBusqueda.put("indicativoConfirmacion", forma.getDtoBusquedaAgendaRango().getIndicativoConf());
		    parametrosBusqueda.put("usuario", usuario.getLoginUsuario());
		    parametrosBusqueda.put("institucion", usuario.getCodigoInstitucionInt()+"");
			parametrosBusqueda.put("tipoCancelacion", forma.getDtoBusquedaAgendaRango().getTipoCancelacion());
			
		    Log4JManager.info("Fecha Inicial "+forma.getDtoBusquedaAgendaRango().getFechaInicial());
		    Log4JManager.info("Fecha Final "+forma.getDtoBusquedaAgendaRango().getFechaFinal());
		    
			forma.setCitasOdonPac(AgendaOdontologica.consultarCitasXRango(con, parametrosBusqueda, usuario.getCodigoCentroAtencion()));								
	       	 
		 
			UtilidadBD.closeConnection(con);
		 
    	}
        return mapping.findForward("principalXRango");  
		   
	}
	

	/**
	 * M&eacute;todo para iniciar el proceso de iniciar reserva de citas
	 * @param forma Formulario
	 * @param usuario Usuario
	 * @param mapping Mapeo de JSP's
	 * @param paciente Paciente en sesión
	 * @param response 
	 * @param request 
	 * @param mundo 
	 * @return de reserva ó pagina de error según validaciones
	 */
	private ActionForward accionIniciarReservar(AgendaOdontologicaForm forma,UsuarioBasico usuario, ActionMapping mapping, PersonaBasica paciente, HttpServletResponse response, HttpServletRequest request, AgendaOdontologica mundo) throws IPSException 
	{
		forma.setProcesoExito(ConstantesBD.acronimoNo);
		forma.setMostrarTarifas(false);
		forma.setPasoValidacionesGuardar(false);
		forma.setTipoCitaCrear(ConstantesIntegridadDominio.acronimoReservado);
		
		for(int i=0;i<forma.getAgendaOdon().size();i++)
		{
			Log4JManager.info("codigo agenda arreglo["+i+"]: "+forma.getAgendaOdon().get(i).getCodigoPk());
			if(forma.getAgendaOdon().get(i).getCodigoPk()==forma.getCodigoAge())
			{
				forma.setPosAgendaSel(i);
				forma.setAgendaSel(forma.getAgendaOdon().get(i));
			}
		}
		
		UtilidadTransaccion.getTransaccion().begin();
		forma.setListaAreas((ArrayList)agendaOdontologicaServicio.obtenerCentrosCostoPorCentroAtencion(forma.getAgendaSel().getCentroAtencion(), ConstantesBD.codigoTipoAreaDirecto));

		//FIXME aca toca aclarar cuando no se encuentran areas, debe sacar mensaje de error.
		
		cargarServiciosAsignacionYReservaCita(forma, usuario, paciente.getCodigoIngreso());
		
		UtilidadTransaccion.getTransaccion().commit();

		//----------------------------------------------------
		if(forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTratamiento))
		{
			if(forma.getValidaPresupuestoContratado().equals(ConstantesBD.acronimoNo))
			{
				// no mostrar valor tarifas
				forma.setHabilitarCalcTarifa(ConstantesBD.acronimoNo);
				forma.setMostrarTarifaServicios(false);
			}
			else{
				//mostrar valor tarifas
				cargarValoresSaldosAbonos(forma, usuario, paciente, request);
				forma.setMostrarTarifaServicios(true);
			}
		}
		else
		{
			// no mostrar valor tarifas
			forma.setHabilitarCalcTarifa(ConstantesBD.acronimoNo);
			forma.setMostrarTarifaServicios(false);
		}
		//----------------------------------------------------
		
		
		//----------------------------------------------------
		// Valida si se debe mostrar la sección de valor total de cita
		if(!forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento)){
			forma.setMostrarValorTotalCita(false);
		}
		else{
			if(forma.getValidaPresupuestoContratado().equals(ConstantesBD.acronimoNo))	{
				forma.setMostrarValorTotalCita(false);
			}
			else {
				forma.setMostrarValorTotalCita(true);
			}
		}
		//----------------------------------------------------
		
		
		actualizarTotales(usuario, forma, paciente, request); 
		
		
		return mapping.findForward("reservacionCita");
		
	}

	
	/**
	 * Mï¿½todo que realiza el guardado de la reserva
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param persona 
	 * @param mundoCita 
	 * @param mundo 
	 * @return
	 * @author Juanda
	 */
	private ActionForward accionValidarAsignacion(AgendaOdontologicaForm forma,
			UsuarioBasico usuario, ActionMapping mapping,
			HttpServletRequest request, AgendaOdontologica mundo, CitaOdontologica mundoCita, PersonaBasica persona) throws IPSException 
	{
		//************VALIDACIONES INICIALES ********************************
		forma.setTipoCitaCrear(ConstantesIntegridadDominio.acronimoAsignado);
		forma.setMostrarTarifas(false);
		forma.setPasoValidacionesGuardar(false);
		forma.setFechaDesplazamiento(forma.getFecha());

		ActionErrors errores = validacionCitaOdontologica(forma, persona);
		//************************************************************************
		
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			forma.setProcesoExito(ConstantesBD.acronimoNo);
		}
		else
		{
			calcularTarifaActualOPresupuestoContratado(forma, usuario, request,	persona, errores, false);
		}
		
		return mapping.findForward("asignacionCita");
	}

	
	
	/**
	 * Metodo para obtener la tarifa nueva o clacular la tarifa del presupuesto contratado
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param persona
	 * @param errores
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see      
	 */
	private void calcularTarifaActualOPresupuestoContratado(	AgendaOdontologicaForm forma, UsuarioBasico usuario,
																HttpServletRequest request, PersonaBasica persona,
																ActionErrors errores, boolean citaMigrada) throws IPSException 
	{
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoSubCuentas> listaResponsables=UtilidadesHistoriaClinica.obtenerResponsablesIngreso(
							con,
							persona.getCodigoIngreso(),
							true, // Traer todos los responsables (Facturados y no facturasdos)
							new String[0], // Exluir responsables
							false, // Solamente PYP
							"", // Sub cuenta
							persona.getCodigoUltimaViaIngreso());
		
		forma.setResponsablesCuenta(listaResponsables);
		
		/*
		 * Este arraylist se envia a la vista para mostrar las tarifas de los sevicios
		 */
		ArrayList<DtoServicioOdontologico> listaServicios=forma.getServiciosOdon();
		double valorTotalServiciosAbono=0;
		double valorTotalServicios=0;
		for(int i=0;i<listaServicios.size();i++)
		{
			if(forma.getServiciosOdon().size()>0)
			{
				DtoServicioOdontologico servicio=listaServicios.get(i);
				if(UtilidadTexto.getBoolean(servicio.getGarantiaServicio()))
				{
					servicio.setInfoTarifa(null);
				}else
				if(UtilidadTexto.getBoolean(servicio.getAsociarSerCita()))
				{
					InfoTarifaServicioPresupuesto tarifa=new InfoTarifaServicioPresupuesto();
					InfoResponsableCobertura infoResponsableCobertura=new InfoResponsableCobertura();
					for(int w=0; w<listaResponsables.size(); w++)
					{
						DtoSubCuentas subCuenta= listaResponsables.get(w);
						Log4JManager.info("\n\n Codigo Presupuesto Prog >> "+ servicio.getCodigoPresuOdoProgSer());
						if(servicio.getCodigoPresuOdoProgSer()>0 )
						{
							Log4JManager.info("OBTENER INFO presupuesto >> ");
							CargosOdon.obtenerInfoPresupuestoContratadoProgSer(con,servicio.getCodigoPresuOdoProgSer(), infoResponsableCobertura, tarifa, UtilidadTexto.getBoolean(forma.getUtilProgOdonPlanTra()), servicio.getCodigoServicio(), new BigDecimal(forma.getCodigoPlanTratamiento()));
				
							if(!tarifa.getError().equals(""))
							{
								tarifa.setError(tarifa.getError()+" para el servicio "+servicio.getDescripcionServicio());
							}
							
						}
						else
						{
							Log4JManager.info("Obtener info Tarifa Unitaria Por Servicio "); 
							infoResponsableCobertura = Cobertura.validacionCoberturaServicio(con, persona.getCodigoIngreso()+"", persona.getCodigoUltimaViaIngreso(), persona.getCodigoTipoPaciente(), servicio.getCodigoServicio(), Integer.parseInt(usuario.getCodigoInstitucion()), false, subCuenta.getSubCuenta());
							int convenio=infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo();
							tarifa=CargosOdon.obtenerTarifaUnitariaXServicio(servicio.getCodigoServicio(), convenio, subCuenta.getContrato(), "", usuario.getCodigoInstitucionInt(), new BigDecimal(persona.getCodigoCuenta()), false /*en este punto no existe programa, por esa razon la cobertura se hace a nivel del servicio*/, usuario.getCodigoCentroCosto());
						}
						
						if(tarifa.getError().equals(""))
						{

							// Como no hay error encontro cobertura y tarifa
							// Se verifica que no se tenga una tarifa cargada
							if(servicio.getInfoTarifa() == null){
								servicio.setInfoTarifa(tarifa);
							}
							
														
							servicio.setResponsableServicio(infoResponsableCobertura.getDtoSubCuenta());
							Log4JManager.info("codigo contrato "+infoResponsableCobertura.getDtoSubCuenta().getContrato());
							servicio.setPacientePagaAtencion(Contrato.pacientePagaAtencion(infoResponsableCobertura.getDtoSubCuenta().getContrato()));
							if(servicio.getPacientePagaAtencion())
							{
								valorTotalServiciosAbono+=tarifa.getValorTarifaTotalConDctos().doubleValue();
							}
							valorTotalServicios+=tarifa.getValorTarifaTotalConDctos().doubleValue();
							// no debo validar nada más
							w=listaResponsables.size();
							break;
						}
					}
					if(tarifa==null || !tarifa.getError().equals(""))
					{
						errores.add("error tarifa", new ActionMessage("errors.notEspecific", tarifa.getError()));
					}else if (citaMigrada) {
						
						for (DtoCitaOdontologica citas : forma.getCitasOdonPac()) {
							
							if (citas.getCodigoPk() == servicio.getCodigoCita()) {
								citas.getServiciosCitaOdon().get(i).setValorTarifa(tarifa.getValorTarifaUnitaria());
							}
						}	
						
						String valorTotal = valorTotalServicios + "";
						forma.setSaldoTotalServcios(valorTotal);
					}
				}
			}
		}
		
		if(errores.isEmpty())
		{
			/*
			 * Ya no se muestran las tarifas. Estas son calculadas en la presentacion
			*/
			/*
				forma.setValorTotalServicios(valorTotalServicios);
				forma.setAbonosDisponiblesPaciente(Utilidades.obtenerAbonosDisponiblesPaciente(persona.getCodigoPersona(),persona.getCodigoIngreso(), usuario.getCodigoInstitucionInt()));
			*/
			forma.setValorTotalServiciosAbono(valorTotalServiciosAbono);
			if (valorTotalServiciosAbono > 0) {
				forma.setValorPorAbonar(valorTotalServiciosAbono - forma.getAbonosDisponiblesPaciente());
			}else{
				forma.setValorPorAbonar(valorTotalServiciosAbono);
			}
			
			forma.setValorTotalServicios(valorTotalServicios);
			forma.setMostrarTarifas(true);
			forma.setPasoValidacionesGuardar(true);
		}
		else
		{
			forma.setMostrarTarifas(false);
			forma.setPasoValidacionesGuardar(false);
			saveErrors(request, errores);
		}
		
		UtilidadBD.closeConnection(con);
	}
	
	
	
	/**
	 * Metodo intermedio para realizar la reserva de una cita
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param persona 
	 * @param mundoCita 
	 * @param mundo 
	 * @return
	 * @author Gio
	 */
	private ActionForward accionValidarReserva(AgendaOdontologicaForm forma,
			UsuarioBasico usuario, ActionMapping mapping,
			HttpServletRequest request, AgendaOdontologica mundo, CitaOdontologica mundoCita, PersonaBasica persona) throws IPSException 
	{
		ArrayList<String> estados = new ArrayList<String>(); 
		estados.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
		estados.add(ConstantesIntegridadDominio.acronimoEstadoEnProceso);
		estados.add(ConstantesIntegridadDominio.acronimoContratado);
		
		//************VALIDACIONES INICIALES ********************************
		if(!forma.getCambiarServicio().equals(ConstantesBD.acronimoSi))
		{
			forma.setTipoCitaCrear(ConstantesIntegridadDominio.acronimoReservado);
		}
		forma.setFechaDesplazamiento(forma.getFecha());
		ActionErrors errores = validacionCitaOdontologica(forma, persona);
		//************************************************************************
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			forma.setProcesoExito(ConstantesBD.acronimoNo);
			forma.setMostrarTarifas(false);
			forma.setPasoValidacionesGuardar(false);
			
		} else {
			
			if(PlanTratamiento.obtenerCodigoPlanTratamiento(persona.getCodigoPersona(), estados, ConstantesBD.acronimoNo).size()>0)
			{
				if(forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTipoCitaOdonValoracionInicial))
				{
					calcularTarifaActualOPresupuestoContratado(forma, usuario, request,	persona, errores, false);
					forma.setMostrarTarifas(false);
					forma.setPasoValidacionesGuardar(false);
					errores.add("tiene_tratamiento",new ActionMessage("errors.notEspecific", "El paciente ya tiene un plan de tratamiento."));
					saveErrors(request, errores);
				}
				else{
					return accionGuardarReserva(forma,usuario,mapping,request,mundo,mundoCita,persona);
				}
			} 
			else 
			{
				return accionGuardarReserva(forma,usuario,mapping,request,mundo,mundoCita,persona);
			}
		}
		
		return mapping.findForward("reservacionCita");
	}
	
	//***************************************************************************************************************
		
	//************************** DUVIAN *********************************************


	
	/**
	 * Método accionIniciarActividadCancelar
	 * 
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * 
	 * @return ActionForward
	 */
	private ActionForward accionIniciarActividadCancelar(AgendaOdontologicaForm forma, ActionMapping mapping,UsuarioBasico usuario, PersonaBasica paciente ) {


		Connection con;
		con = UtilidadBD.abrirConexion();
		
		forma.setCancelandoCitaOdoEstadoReprogramar(ValoresPorDefecto.getAlCancelarCitasOdontoDejarAutoEstadoReprogramar(usuario.getCodigoInstitucionInt()))  ;
		
		//DtoCitaOdontologica cita=forma.getCitasOdonPac().get(forma.getPosicionCita());
		forma.setCitaAgendaSel(forma.getCitasOdonPac().get(forma.getPosicionCita()));
		
		
		Log4JManager.info("-->"+forma.getCitaAgendaSel().getAgendaOdon().getNombreEspecialidadUniAgen()); 

		IEspecialidadServicio especialidadServicio=EspecialidadServicioFabrica.crearEspecialidadServicio();
		

		Consultorios consultorioMundo= new Consultorios();
		
		HashMap mapaConsultorio =consultorioMundo.consultarConsultorio(con, forma.getCitaAgendaSel().getAgendaOdon().getConsultorio());
		Utilidades.imprimirMapa(mapaConsultorio);
		try{
			if(mapaConsultorio.get("descripcion_0")!=null)
			{
				forma.getCitaAgendaSel().getAgendaOdon().setNombreConsultorio(mapaConsultorio.get("descripcion_0").toString());
			}
			
		}
		catch (Exception e) {
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
		
		
		
		/*
		 *Abrir transaccion  
		 */
		UtilidadTransaccion.getTransaccion().begin();
		/*
		 *Cargamos le nombre completo del paciente 
		 */
		Personas persona = accionCargarPersona(forma);
		//String acronimo=persona.getTiposIdentificacion().getAcronimo();
		String tmpNombreCompleto = accionTransformarNombreCompletoPaciente(persona);
		
		forma.getCitaAgendaSel().setNombreCompletoPaciente(tmpNombreCompleto);
		/*
		 * Cargar la Especialidad 
		 */
		Especialidades especialidad=  especialidadServicio.buscarPorId(forma.getCitaAgendaSel().getAgendaOdon().getEspecialidadUniAgen());
		if(especialidad!=null && especialidad.getNombre()!=null)
		{
			forma.getCitaAgendaSel().getAgendaOdon().setNombreEspecialidadUniAgen(especialidad.getNombre());
		}
		
		
		/*
		 * Cargar el nombre del centro atencion
		 */
		ICentroAtencionServicio servioCentroAtencion=AdministracionFabricaServicio.crearCentroAtencionServicio();
		com.servinte.axioma.orm.CentroAtencion  centroAtencionEntidad=  servioCentroAtencion.buscarPorCodigoPK(forma.getCitaAgendaSel().getCodigoCentroAtencion());
		

		if(centroAtencionEntidad!=null &&   centroAtencionEntidad.getDescripcion()!=null )
		{
			forma.getCitaAgendaSel().setAyudanteNombreCentroAtencion(centroAtencionEntidad.getDescripcion());
		}
		
		/*
		 * Cerrar transaccion
		 */
		UtilidadTransaccion.getTransaccion().commit();
		
		forma.setTipoCita(forma.getCitaAgendaSel().getTipo());
		forma.setHoraInicio(forma.getCitaAgendaSel().getHoraInicio());
		forma.setHoraFinal(forma.getCitaAgendaSel().getHoraFinal());
		
		
		forma.setTipoCita(forma.getCitaAgendaSel().getTipo());
		forma.setHoraInicio(forma.getCitaAgendaSel().getHoraInicio());
		forma.setHoraFinal(forma.getCitaAgendaSel().getHoraFinal());
		forma.setFecha(UtilidadFecha.conversionFormatoFechaAAp(forma.getCitasOdonPac().get(forma.getPosicionCita()).getAgendaOdon().getFecha()));
		forma.setFechaDesplazamiento(forma.getFecha());
		
		 
		 /*
		  * Nota...........................
		  */
		 //ValoresPorDefecto
		 //Log4JManager.info(forma.getCitaAgendaSel().getAgendaOdon().getEspecialidadMedico().getNombre())  ;
		
		// Log4JManager.info(" nombre paciente _-->Z" + forma.getCitaAgendaSel().getAgendaOdon().getFecha() .getHoraInicio());
		 
		 
			
	   
		/*forma.setServiciosOdon(UtilidadOdontologia.obtenerServicios( 
			usuario.getCodigoInstitucionInt(),
			forma.getCitaAgendaSel().getAgendaOdon().getUnidadAgenda(), 
			forma.getCitaAgendaSel().getAgendaOdon().getCodigoMedico(),
			forma.getPaciente().getCodigo()2420 - 289582, 
			"'"+ConstantesBD.codigoServicioCargosConsultaExterna+"'", 
			true,
			forma.getValidaPresupuestoContratado(),
			forma.getCambiarSerOdo(),
			forma.getCasoBusServicio()));
		
		if(forma.getServiciosOdon().size()>0)
		{
		   forma.setCodigoPlanTratamiento(forma.getServiciosOdon().get(0).getCodigoPlanTratamiento());
		   
		   Log4JManager.info("Codigo Plan de Tratamiento >>"+forma.getCodigoPlanTratamiento());
		   Log4JManager.info("Codigo Programa >>"+forma.getServiciosOdon().get(0).getCodigoPrograma());
		   Log4JManager.info("Codigo Servicio >>"+forma.getServiciosOdon().get(0).getCodigoServicio());
		   Log4JManager.info("Orden Servicio >>"+forma.getServiciosOdon().get(0).getOrdenServicio());
		}  
		//verificar si la visualizacion de los servicios es por programa o servicios
		if(forma.getUtilProgOdonPlanTra().equals(ConstantesBD.acronimoSi))
		{
			ArrayList<DtoServicioOdontologico> servicioOdoOrden = new ArrayList<DtoServicioOdontologico>();
			String seccion = "";
			String codigoProg = "";
			forma.setSeccion(forma.getServiciosOdon());
			forma.setProgramas(forma.getServiciosOdon());
			Iterator<String> iterPrograma = forma.getProgramas().iterator();
			while(iterPrograma.hasNext())
			{
				codigoProg = iterPrograma.next();
				Iterator<String> iterSeccion = forma.getSeccion().iterator();
				while(iterSeccion.hasNext())
				{
					seccion = iterSeccion.next() ;
					
					for(DtoServicioOdontologico elem: forma.getServiciosOdon())
					{						
						if(elem.getSeccionPlanTrata().equals(seccion) && elem.getCodigoPrograma()==Utilidades.convertirAEntero(codigoProg))
							servicioOdoOrden.add(elem);
									   				  
					}
				}
			}
			forma.setServiciosOdon(servicioOdoOrden);*/
			
			ArrayList<DtoServicioCitaOdontologica> listaServiciosCita= new ArrayList<DtoServicioCitaOdontologica>();
			listaServiciosCita= CitaOdontologica.consultarServiciosCitaOdontologica(con, forma.getCitasOdonPac().get(forma.getPosicionCita()).getCodigoPk(),usuario.getCodigoInstitucionInt(), paciente.getCodigoPersona());
			
			Log4JManager.info("Consulta las citas proximas");
			for(DtoServicioCitaOdontologica citasOrd: listaServiciosCita)
			{			
				ArrayList<DtoCitaOdontologica> citasTem = new ArrayList<DtoCitaOdontologica>();
				citasTem=CitaOdontologica.consultarProxCitasPacienteXCancelacion(forma.getPaciente().getCodigo(), citasOrd.getProgramaHallazgoPieza().getPrograma(), citasOrd.getOrdenServicio(), usuario.getCodigoInstitucionInt(), forma.getFecha(), forma.getHoraFinal(), usuario.getLoginUsuario());
				for(DtoCitaOdontologica citasProx:citasTem)
				{
					if(!existeCitaProxima(citasProx.getCodigoPk(),forma.getListaProximasCitasCancelar()))
					{
						forma.getListaProximasCitasCancelar().add(citasProx);
					}
				}
			}
		/*}
		else
		{
			// se ordena por pieza los servicios a mostrar
			SortServiciosOdontologicos sort= new SortServiciosOdontologicos();
			sort.setPatronOrdenar(forma.getPatronOrdenarSerOdo());
			Collections.sort(forma.getServiciosOdon(), sort);
		}*/
		
		String tipoCancelacion = ConstantesBD.codigoEstadoCitaCanceladaPaciente+"";	
		forma.setListMotivoCancelacion(UtilidadesConsultaExterna.obtenerMotivosCancelacion(con, ConstantesBD.acronimoSi,tipoCancelacion	));
		UtilidadBD.closeConnection(con);
		
		

		
		return mapping.findForward("cancelacionCita");  

	}


	private String accionTransformarNombreCompletoPaciente(Personas persona) {
		String tmpNombreCompleto=persona.getPrimerApellido()+" "+persona.getSegundoApellido()+" "+persona.getPrimerNombre()+ " "+persona.getSegundoNombre();
		return tmpNombreCompleto;
	}


	private Personas accionCargarPersona(AgendaOdontologicaForm forma) {
		IPersonasServicio servicioPersonas =com.servinte.axioma.servicio.fabrica.administracion.AdministracionFabricaServicio.crearPersonaServicio();
		Personas persona= servicioPersonas.buscarPersonasPorId(forma.getCitaAgendaSel().getCodigoPaciente());
		return persona;
	}
	

	/**
	 * Metodo para  validar si exite cita en el arreglo de proximas citas a cancelar
	 * @param codigo
	 * @param citasProximas
	 * @return
	 */
	private boolean existeCitaProxima(int codigo, ArrayList<DtoCitaOdontologica> citasProximas)
	{
		boolean existe=false;
		for(DtoCitaOdontologica elem: citasProximas)
		{
			if(elem.getCodigoPk()==codigo)
			{
				existe = true;
			}
		}
		
		return existe;
	}
	
	/**
	 * M&eacute;todo que se encarga de postular la Unidad de agenda parametrizada para un tipo de cita
	 * espec&iacute;fico. Si el usuario en sesi&oacute;n no tiene permisos para la unidad de agenda seleccionada
	 * en la parametrizaci&oacute;n o si no existe un registro para ese tipo de cita, se debe postular 
	 * autom&aacute;ticamente la opci&oacute;n 'Todos los Autorizados'.
	 * 
	 * Solo postula unicamente cuando se esta ingresando una nueva cita.
	 * 
	 * @param forma
	 */
	private void postularUnidadAgenda(AgendaOdontologicaForm forma) {
		

		if(forma.getCitaConfirmacion() !=null  && "".equals(forma.getCitaConfirmacion().getEstado())){
			
			for(HashMap<String, String> mapa:forma.getListUnidadesAgendaXUsuario())
			{
				if(mapa.get("todos")!=null)
				{
					forma.setUnidadAgenda(mapa.get("todos"));
				}
			}
			
			if(forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoAuditoria) ||
			   forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoControlCitaOdon) ||
			   forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoRevaloracion) ||
			   forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTipoCitaOdonValoracionInicial) ||
			   forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoPrioritaria)){
				
				IUnidadAgendaServTipoCitaOdontoServicio unidadAgendaServCitaOdontoServicio =  UnidadAgendaServTipoCitaOdonFabricaServicio.crearUnidadAgendaServCitaOdontoServicio();
				
				String unidadAPostular = "";

				DtoUnidadAgendaServCitaOdonto unidadAgendaServCitaOdonto = unidadAgendaServCitaOdontoServicio.consultarParametricaPorTipoCita(forma.getTipoCita());
				
				if(unidadAgendaServCitaOdonto!=null){
					
					unidadAPostular = unidadAgendaServCitaOdonto.getCodigoUnidadAgenda()+"";
				}
				
				/*
				 * Este método se encarga de buscar entre las unidades de agenda disponibles 
				 * del listado, la Unidad de Agenda seleccionada en la parametrización
				 * para el tipo de cita específico
				 */
				
				if(!UtilidadTexto.isEmpty(unidadAPostular)){
					
					for(HashMap<String, Object> mapa:forma.getListUnidadesAgendaXUsuario()){
						
						if(mapa.get("codigo")!=null && mapa.get("codigo").toString().equals(unidadAPostular)){
							
							forma.setUnidadAgenda(unidadAPostular);
							
							break;
						}
					}
				}
			}
		}
		
		if(forma.getListUnidadesAgendaXUsuario() !=null && forma.getListUnidadesAgendaXUsuario().size() == 2){
		
			for(HashMap<String, Object> mapa:forma.getListUnidadesAgendaXUsuario())
			{
				if(mapa.get("codigo")!=null)
				{
					forma.setUnidadAgenda(mapa.get("codigo").toString());
				}
			}
		}
	}
	
	
	/**
	 * Llena la informacion a mostrar al cargar la asignación de la cita
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @param response
	 * @param paciente
	 * 
	 * @author Cristhian Murillo
	 */
	private void llenarInformacionAsignacioncita(AgendaOdontologicaForm forma,
			UsuarioBasico usuario, ActionMapping mapping,
			HttpServletRequest request, HttpServletResponse response,
			PersonaBasica paciente) {
		
		UtilidadTransaccion.getTransaccion().begin();
		
		/* Este metodo carga todos los convenios a seleccionar que esten activos y asociados al paciente sin validaciones
		forma.setListaConveniosPaciente((ArrayList)agendaOdontologicaServicio.obtenerDtoConveniosIngresoPacientePorEstado
				(paciente.getCodigoPersona(), ConstantesBD.acronimoSiChar));
		*/
		
		forma.setListaAreas((ArrayList)agendaOdontologicaServicio.obtenerCentrosCostoPorCentroAtencion(forma.getAgendaSel().getCentroAtencion(), ConstantesBD.codigoTipoAreaDirecto));
		
		/* Este método muestra la lista de convenios a seleccionar dependiendo de validaciones */
		validacionesMostrarConvenio(forma, paciente, usuario);
		
		UtilidadTransaccion.getTransaccion().commit();
		
		// Se agrega el valor de todos los servicios cargados
		//cargarValoresServiciosCargados(forma);
		
	}
	
	
	
	/**
	 * Verifica si existe un convenio seleccionado
	 * @author Cristhian Murillo
	 * @param forma
	 */
	private void verificarConvenioSeleccionado(AgendaOdontologicaForm forma)
	{ 
		if(!UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio()))
		{
			forma.setConvenioSeleccionado(true);
			habilitarSeccionesConvenioSeleccionado(forma, 
					Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio()));
		}
		else
		{
			forma.setConvenioSeleccionado(false);
			forma.setConvenioManejaBono(false);
			forma.setConvenioManejaBonoRequerido(false);
		}
	}
	
	
	/**
	 * 
	 * Método que se encarga de determinar como se debe validar el campo
	 * de ingreso de número de identificación del paciente
	 * 
	 * @param forma
	 * @param request
	 * @param codigoInstitucion
	 */
	private void determinarValidacionIdentificacion(AgendaOdontologicaForm forma, HttpServletRequest request, int codigoInstitucion) {
		
		UtilidadTransaccion.getTransaccion().begin();
		
		ITiposIdentificacionServicio tiposIdentificacionServicio = AdministracionFabricaServicio.crearTiposIdentificacionServicio();

		String numDigCaptNumIdPac = ValoresPorDefecto.getNumDigCaptNumIdPac(codigoInstitucion);
		
		if(UtilidadTexto.isNumber(numDigCaptNumIdPac)){
			
			forma.setNumDigCaptNumIdPac(Integer.parseInt(numDigCaptNumIdPac));
			
		}else{
			
			forma.setNumDigCaptNumIdPac(20);
		}
		
		if(!"".equals(forma.getTipoIdentificacionPac())){
			
			TiposIdentificacion tipoIdentificacion = tiposIdentificacionServicio.obtenerTipoIdentificacionPorAcronimo(forma.getTipoIdentificacionPac());
			
			if(tipoIdentificacion!=null){
				
				if(tipoIdentificacion.getSoloNumeros().equals(ConstantesBD.acronimoSi.charAt(0))){
					
					request.setAttribute("validacionCampo", "soloNumero");

				}else{
					
					request.setAttribute("validacionCampo", "alfanumerico");
				}
			}
		}
		
		UtilidadTransaccion.getTransaccion().commit();
	}
	
	
	
	/** 
	 * Anexo 1131
	 * Actualiza la lista de contratos segun el convenio seleccionado
	 * 
	 * @author Cristhian Murillo
	 * 
	 * @param forma
	 */
	private void accionMostrarContratosConvenio(AgendaOdontologicaForm forma) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		if(!UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio()))
		{
			int convenioSeleccionado = Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio());
			forma.setListaContratoConvenio(contratoServicio.listarContratosVigentesPorConvenio(convenioSeleccionado));
			forma.setConvenioSeleccionado(true);
			
		}
		else{
			forma.setListaContratoConvenio(new ArrayList<Contratos>());
			forma.setConvenioSeleccionado(false);
		}
		
		UtilidadTransaccion.getTransaccion().commit();
		
		verificarConvenioSeleccionado(forma);
		
	}
	
	
	
	
	/**
	 * Llama el formulario a mostrar para la creacion del ingreso
	 * @param paciente
	 * @param response
	 * @param request
	 * @param mapping
	 * @author Cristhian Murillo
	 */
	private ActionForward llamarRutaCreacionCuenta( PersonaBasica paciente, HttpServletResponse response, HttpServletRequest request, ActionMapping mapping)
	{
		String ruta=request.getContextPath()+"/ingresarPacienteOdontologia/ingresoPacienteOdontologia.do?" +
			"estado=empezar&" +	"tipoIdenPac="+paciente.getCodigoTipoIdentificacionPersona()+ "&numeroIdenPac="+paciente.getNumeroIdentificacionPersona()+
			"&existePaciente=true"+	"&tipoFuncionalidad="+ConstantesIntegridadDominio.acronimoTipoFuncionalidadAsignar+	
			"&urlRetorno="+request.getContextPath()+"/agendaOdontologica/agendaOdoXPaciente.do?estado=iniciarAsignar&vieneDeReserva=false";
		/*
		String ruta=request.getContextPath()+"/aperturaCuentaPacienteOdontologico/aperturaCuentaPacienteOdontologico.do?" +
			"estado=empezar" +"&urlRetorno="+request.getContextPath()+"/agendaOdontologica/agendaOdoXPaciente.do?estado=iniciarAsignar&vieneDeReserva=false";
		*/	
		
		try {
			response.sendRedirect(ruta);
			return null;
		} 
		catch (IOException e) 
		{
			Log4JManager.error("Error haciendo la redirección ",e);
			ActionErrors errores=new ActionErrors();
			errores.add("fechaDesplazamiento",new ActionMessage("errors.notEspecific", "Error redireccionando a la paertura de la cuenta"));
			saveErrors(request, errores);
			return mapping.findForward("paginaError");
		}
	}
	

	/**
	 * Llama las validaciones apra determinar si el campo convenio se debe
	 * mostrar en la asignacion de cita.
	 * 
	 * @author Cristhian Murillo
	 * 
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * 
	 */
	private void validacionesMostrarConvenio(AgendaOdontologicaForm forma,
			PersonaBasica paciente, UsuarioBasico usuario) {
		AgendaOdontologica mundoAgendaOdontologica = new AgendaOdontologica();
		forma.setMostrarSeccionconvenio(mundoAgendaOdontologica
				.validacionesMostrarConvenioAsignacioncita(paciente, forma
						.getTipoCita(), usuario));

		if (!forma.isMostrarSeccionconvenio()) {
			forma.setConvenioSeleccionado(true); // Muestra los datos de la cita
			forma.setListaConveniosPaciente(new ArrayList<DtoSeccionConvenioPaciente>());
		} else {
			// Carga los convenios a msotrar según las validaciones
			//FIXME Verificar transacción en este nivel, cita de revaloración
			UtilidadTransaccion.getTransaccion().begin();
			ArrayList<DtoSeccionConvenioPaciente> listaConveniosMostrarAsignacionCita = new ArrayList<DtoSeccionConvenioPaciente>();
			listaConveniosMostrarAsignacionCita = (ArrayList) agendaOdontologicaServicio
					.obtenerDtoConveniosIngresoPacienteValidacionesAsignacioncita(paciente
							.getCodigoPersona());

			if (Utilidades.isEmpty(listaConveniosMostrarAsignacionCita)) {
				forma.setListaConveniosPaciente(new ArrayList<DtoSeccionConvenioPaciente>());
			} else {
				forma.setListaConveniosPaciente(listaConveniosMostrarAsignacionCita);
				// -------------------
				if (forma.getListaConveniosPaciente().size() == 1) {
					forma.getDtoSeccionConvenioPaciente().setCodigoConvenio(forma.getListaConveniosPaciente().get(0).getCodigoConvenioInt()+ "");
					accionMostrarContratosConvenio(forma);
					UtilidadTransaccion.getTransaccion().begin(); // Se abre la transaccion porque en el metodo anterior fue cerrada
				}
				// ------------------
			}

			// Se cargan en la lista de conveios que han sido parametrizados por defecto
			ArrayList<DtoSeccionConvenioPaciente> listaConveniosParametrizadosPorDefecto = new ArrayList<DtoSeccionConvenioPaciente>();
			listaConveniosParametrizadosPorDefecto = mundoAgendaOdontologica.obtenerConveniosParametrizadosPorDefecto();

			/*
			 * se cargan TODOS los convenios asociados al paciente (Activos/Inactivos) para comparar con los 
			 * parametrizados por defecto y agregar los qué NO estén asociados al paciente.
			 */
			ArrayList<DtoSeccionConvenioPaciente> listaTodosConveniosPac = new ArrayList<DtoSeccionConvenioPaciente>();
			listaTodosConveniosPac.addAll(((ArrayList) agendaOdontologicaServicio.obtenerDtoConveniosIngresoPacientePorEstado(paciente.getCodigoPersona(),ConstantesBD.acronimoSiChar)));
			listaTodosConveniosPac.addAll(((ArrayList) agendaOdontologicaServicio.obtenerDtoConveniosIngresoPacientePorEstado(paciente.getCodigoPersona(),ConstantesBD.acronimoNoChar)));

			// Al agregar los convenios por defecto de último se pierde el orden qué se trae al consultar
			// Toca organizar los convenios alfabeticamente por el nombre

			// Se igualan los codigos de convenio que son enteros con los que son String
			for (DtoSeccionConvenioPaciente dtoSeccionConvenioPacienteDefecto : listaConveniosParametrizadosPorDefecto) {
				boolean existeConvenioEnPaciente = false;
				dtoSeccionConvenioPacienteDefecto.setCodigoConvenioInt(Integer.parseInt(dtoSeccionConvenioPacienteDefecto.getCodigoConvenio()));

				// Los convenios por defecto son comparados contra todos loos convenios asociados al paciente
				for (DtoSeccionConvenioPaciente dtoSeccionConvenioPacientePaciente : listaTodosConveniosPac) {
					// Se igualan los codigos de convenio que son String con los que son enteros
					dtoSeccionConvenioPacientePaciente.setCodigoConvenio(dtoSeccionConvenioPacientePaciente.getCodigoConvenioInt()+ "");

					dtoSeccionConvenioPacientePaciente.getBonosConvIngPac();

					if (dtoSeccionConvenioPacienteDefecto.getCodigoConvenio().equals(dtoSeccionConvenioPacientePaciente.getCodigoConvenio())) {
						existeConvenioEnPaciente = true;
					}
				}
				if (!existeConvenioEnPaciente) {
					forma.getListaConveniosPaciente().add(dtoSeccionConvenioPacienteDefecto);
				}
			}


			//-------------------
			if (forma.getListaConveniosPaciente().size() == 1) {
				forma.getDtoSeccionConvenioPaciente().setCodigoConvenio(forma.getListaConveniosPaciente().get(0).getCodigoConvenioInt()+ "");
				accionMostrarContratosConvenio(forma);
			}
			// ------------------
			
		}

	}
		
		
		
	/** 
	 * Anexo 1131
	 * Habilita los parametros requeridos para el convenio enviado
	 * 
	 * @author Cristhian Murillo
	 * 
	 * @param forma
	 * @param codConvenio
	 */
	private void habilitarSeccionesConvenioSeleccionado(AgendaOdontologicaForm forma, int codConvenio)
	{
		Convenios convenios  = new Convenios();
		
		UtilidadTransaccion.getTransaccion().begin();
		
		
		convenios = agendaOdontologicaServicio.findByIdConvenio(codConvenio);
		
		if(convenios.getManejaBonos() != null)
		{
			forma.setConvenioManejaBono(UtilidadTexto.getBoolean(convenios.getManejaBonos()));
			
			if(convenios.getReqBonoIngPac() != null)
			{
				forma.setConvenioManejaBonoRequerido(UtilidadTexto.getBoolean(convenios.getReqBonoIngPac()));
			} 
			else
			{ 
				forma.setConvenioManejaBonoRequerido(false);
			}
		}
		else
		{ 
			forma.setConvenioManejaBono(false); 
			forma.setConvenioManejaBonoRequerido(false);
		}
		
		// Se verifica para cargar los bonos
		if(forma.isConvenioManejaBono()){
			
			// Se busca cual es la llave primaria del convenio seleccionado en caso de que este este asociado al paciente
			long codigoConvenioIngreso = ConstantesBD.codigoNuncaValidoLong;
			for (DtoSeccionConvenioPaciente convenio : forma.getListaConveniosPaciente()) {
				if(convenio.getCodigoConvenioInt() == codConvenio){
					codigoConvenioIngreso = convenio.getCodigoConveniosIngresoPaciente();
				}
			}
			
			// Se buscan los bonos que se asociaron al convenio seleccionado, en caso de tenerlos
			if(codigoConvenioIngreso != ConstantesBD.codigoNuncaValidoLong)
			{
				ConveniosIngresoPaciente conveniosIngresoPaciente = new ConveniosIngresoPacienteHome().findById(codigoConvenioIngreso);
				ArrayList<BonosConvIngPac> listaBonosCargados = new ArrayList<BonosConvIngPac>(conveniosIngresoPaciente.getBonosConvIngPacs());
				
				// Se filtran y se toman solo los bonos en estado no utilizado
				ArrayList<BonosConvIngPac> listaBonosCargadosNoUtilizados = new ArrayList<BonosConvIngPac>();
				for (BonosConvIngPac bonosConvIngPac : listaBonosCargados) {
					if(bonosConvIngPac.getUtilizado() == ConstantesBD.acronimoNoChar){
						listaBonosCargadosNoUtilizados.add(bonosConvIngPac);
					}
				}
				forma.getDtoSeccionConvenioPaciente().setListaBonosConvIngPac(listaBonosCargadosNoUtilizados);
			}
		}
		
		UtilidadTransaccion.getTransaccion().commit();
	}
	
	
	
	
	/**
	 * M&eacute;todo que se encarga de devolver el c&oacute;digo de la especialidad
	 * asociada a la unidad de agenda
	 * 
	 * @param codigo
	 * @return
	 */
	private int obtenerEspecialidadXUnidadAgenda (String codigo){
		
		
		if(UtilidadTexto.isNumber(codigo)){
		
			int codigoUnidadAgenda = Integer.parseInt(codigo);
			
			IUnidadesConsultaServicio unidadesConsultaServicio = UnidadAgendaServTipoCitaOdonFabricaServicio.crearUnidadConsultaServicio();
			
			UnidadesConsulta unidadConsulta = unidadesConsultaServicio.buscarUnidadConsultaId(codigoUnidadAgenda);
			
			if(unidadConsulta!=null){
				
				return unidadConsulta.getEspecialidades().getCodigo();
			}
		}
		
		return ConstantesBD.codigoNuncaValido;
	
	}
	
	/**
	 * M&eacute;todo que se encarga de actualizar el listado de servicios que van a listarse en la p&aacute;gina 
	 * principal cuando se asigna o reserva una cita. El listado otros servicios contiene todos los servicios
	 * que pueden asociarse a la cita, mostrando o no dependiendo de los que se encuentren en el otro listado.
	 * El listado de servicios contiene solo los que van a asociarse a dicha cita.
	 */
	private void actualizarServiciosDesdeOtros (AgendaOdontologicaForm forma, UsuarioBasico usuario, String estado){
		
		forma.setInhabilitaOtrosServicios(false);
		
		ArrayList<DtoServicioOdontologico> listadoServicios = new ArrayList<DtoServicioOdontologico>();
		
		if(estado.equals("inicializarListadoServiciosCita")){
			
			IUnidadAgendaServTipoCitaOdontoServicio unidadAgendaServCitaOdontoServicio =  UnidadAgendaServTipoCitaOdonFabricaServicio.crearUnidadAgendaServCitaOdontoServicio();

			DtoUnidadAgendaServCitaOdonto unidadAgendaServCitaOdonto = unidadAgendaServCitaOdontoServicio.consultarParametricaPorTipoCita(forma.getTipoCita());
			
			listadoServicios = inicializarListadoServiciosCita(forma, unidadAgendaServCitaOdonto, usuario.getCodigoInstitucionInt());
			
			forma.setServiciosOdon(listadoServicios);

		}else if (estado.equals("confirmarOtrosServicios")){
			
			for (DtoServicioOdontologico servicio : forma.getOtrosServiciosOdon()){
				
				if(servicio.getAsociarSerCita().equals(ConstantesBD.acronimoSi) && servicio.isMuestraEnPantalla() == true){
				
					servicio.setMuestraEnPantalla(false);
					listadoServicios.add(servicio);
				}
			}
		
			forma.getServiciosOdon().addAll(listadoServicios);
		}
		
		/*
		 * Si los dos listados de servicios contienen la misma información, entonces
		 * el link 'Otros Servicios' no se habilita.
		 */

		if(listadoServicios.size() == forma.getOtrosServiciosOdon().size()){
			
			forma.setInhabilitaOtrosServicios(true);
			
		}else if(forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento)){
			
			if(!Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), ConstantesBD.codigoFuncionalidadLinkOtrosServicios)
				|| !Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), ConstantesBD.codigoFuncionalidadSeleccionarServicios)){
					
				forma.setInhabilitaOtrosServicios(true);
			}
		}
		
		
		validarExistenRegistroServicios (forma);

	}
	
	/**
	 * M&eacute;todo que se encarga de determinar si existen servicios disponibles
	 * del listado de Otros servicios que a&uacute;n no esten asociados a la asignaci&oacute;n 
	 * o a la reserva.
	 * 
	 * @param forma 
	 * @param otrosServiciosOdon
	 */
	private boolean validarExistenRegistroServicios(AgendaOdontologicaForm forma) {
		
		forma.setExistenRegistroServicios(false);
		
		for (DtoServicioOdontologico servicio : forma.getOtrosServiciosOdon()) {
			
			if(servicio.isMuestraEnPantalla()){
				
				forma.setExistenRegistroServicios(true);
				break;
			}
		}
		
		return forma.isExistenRegistroServicios();
	}


	/**
	 * M&eacute;todo que inicializa el listado de servicios que estan asociados
	 * a la cita y que deben aparecer inicialmente.
	 * 
	 * Si existe registro en la par&aacute;metrica, Unidad de Agenda - Servicio por Tipo de 
	 * Cita Odontol&oacute;gica, debe postular este servicio, y los otros, deben quedar
	 * disponibles en el link de otros servicios.
	 * 
	 * @param forma
	 * @param unidadAgendaServCitaOdonto
	 * @param codigoInstitucion
	 * @return
	 */
	private ArrayList<DtoServicioOdontologico> inicializarListadoServiciosCita (AgendaOdontologicaForm forma, DtoUnidadAgendaServCitaOdonto unidadAgendaServCitaOdonto, int codigoInstitucion){
		
		boolean registrado = false;
		
		ArrayList<DtoServicioOdontologico> listadoServicios = new ArrayList<DtoServicioOdontologico>();

		int codigoServicioPostular = ConstantesBD.codigoNuncaValido;
	
		if(unidadAgendaServCitaOdonto!=null){
			
			codigoServicioPostular = unidadAgendaServCitaOdonto.getCodigoServicio();
		}
		
		if(codigoServicioPostular != ConstantesBD.codigoNuncaValido){
			
			for (DtoServicioOdontologico servicio : forma.getOtrosServiciosOdon()){
				
				if(servicio.getCodigoServicio() == codigoServicioPostular){
					
					listadoServicios.add(servicio);
					servicio.setMuestraEnPantalla(false);
					servicio.setAsociarSerCita(ConstantesBD.acronimoSi);
					registrado = true;
					break;
				}
			}
		}
		
		// Esto puede servir mas adelante.
//		if(!registrado && unidadAgendaServCitaOdonto!=null){
//			
//			String tipoServicio = "'"+unidadAgendaServCitaOdonto.getTipoServicio()+"'";
//			
//			HashMap<String, Object> parametros = agruparParametrosConsultaServicios(forma, codigoInstitucion, unidadAgendaServCitaOdonto.getCodigoUnidadAgenda(), 
//					ConstantesBD.codigoNuncaValido, tipoServicio, ConstantesBD.codigoNuncaValido, false, codigoServicioPostular+"", "", "", "");
//		 	
//			/*forma.setServiciosOdon */ 
//			ArrayList<DtoServicioOdontologico> servicioApostular = UtilidadOdontologia.obtenerServicios(parametros);
//			
//			if (servicioApostular!=null && servicioApostular.size()>0){
//				
//				listadoServicios.addAll(servicioApostular);
//				
//				DtoServicioOdontologico servicio = servicioApostular.get(0);
//				servicio.setMuestraEnPantalla(false);
//				servicio.setAsociarSerCita(ConstantesBD.acronimoSi);
//				forma.getOtrosServiciosOdon().add(servicio);
//			}
//			
//		}else 
//			
		if(unidadAgendaServCitaOdonto == null || !registrado){
		
			/*
			 * No existe registro en la paramétrica para este tipo de cita
			 */
			listadoServicios = forma.getOtrosServiciosOdon();
		}
	
			
		return listadoServicios;
	}
	
	
	/**
	 * M&eacute;todo que se encarga de agrupar los par&aacute;metros necesarios
	 * para realizar la consulta de los servicios
	 * 
	 * @param forma
	 * @param codigoInstitucion
	 * @param unidadConsulta
	 * @param codigoMedico
	 * @param tipoServicio
	 * @param codigoCitaNoVinculado
	 * @param buscarPlanTratamiento
	 * @param codigosServicios
	 * @param codigosCitasVinculado
	 * @param estadosCitasVinculado
	 * @param isServicioVinculadoCita 
	 * @return
	 */
	private HashMap<String, Object> agruparParametrosConsultaServicios (AgendaOdontologicaForm forma, int codigoInstitucion, int unidadConsulta, 
			int codigoMedico, String tipoServicio, int codigoCitaNoVinculado, boolean buscarPlanTratamiento, String codigosServicios,
			String codigosCitasVinculado, String estadosCitasVinculado, String isServicioVinculadoCita, String codigosServiciosNoIncluir){
		
		
		HashMap<String, Object> parametros = new HashMap<String, Object>();
		
		parametros.put("codigoInstitucion", codigoInstitucion);
		parametros.put("unidadConsulta", unidadConsulta);
		parametros.put("codigoMedico", codigoMedico);
		parametros.put("codigoPaciente",forma.getPaciente().getCodigo());
		parametros.put("tipoServicio",tipoServicio);
		parametros.put("activos", true);
		parametros.put("validaPresCont",forma.getValidaPresupuestoContratado());
		parametros.put("cambiarSerOdo",forma.getCambiarSerOdo());
		parametros.put("casoBusSer",forma.getCasoBusServicio());
		parametros.put("codigoCitaNoVinculado",codigoCitaNoVinculado);
		parametros.put("buscarPlanTratamiento",buscarPlanTratamiento);
		parametros.put("codigosServicios",codigosServicios);
		parametros.put("codigosCitasVinculado", codigosCitasVinculado);
		parametros.put("estadosCitasVinculado", estadosCitasVinculado);
		parametros.put("isServicioVinculadoCita", isServicioVinculadoCita);
		parametros.put("codigosServiciosNoIncluir", codigosServiciosNoIncluir);
		parametros.put("ordenarPorPHP", false);
		
		return parametros;

	}
	
	
	/**
	 * Validaciones necesarias para asignar el valor de los servicios cargados. Anexo 1131
	 * Asigna los valores de los servicios cargados
	 * @author Cristhian Murillo
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request 
	 */
	private void cargarValoresSaldosAbonos(AgendaOdontologicaForm forma, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) throws IPSException
	{

		int institucion 			= usuario.getCodigoInstitucionInt();
		int centroCosto				= usuario.getCodigoCentroCosto();
		BigDecimal codigoCuenta		= new BigDecimal(paciente.getCodigoCuenta());
		
		int contratoOpresupuesto 	= ConstantesBD.codigoNuncaValido;
		int convenio 				= ConstantesBD.codigoNuncaValido;
		int servicio				= ConstantesBD.codigoNuncaValido;
		
		
		if(!forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTratamiento))
		{
			// calcular por convenio-contrato
			if(!UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio())){
				convenio = Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio());
				if(forma.getListaContratoConvenio().size() == 1){
					forma.getDtoSeccionConvenioPaciente().setCodigoContrato(forma.getListaContratoConvenio().get(0).getCodigo()+"");
				}
			}
			if(!UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getCodigoContrato())){
				contratoOpresupuesto = Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoContrato());
			}
			
		}
		else // tratamiento
		{
			if(forma.getValidaPresupuestoContratado().equals(ConstantesBD.acronimoNo))
			{
				// calcular por convenio-contrato
				if(!UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio())){
					convenio = Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoConvenio());
				}
				if(!UtilidadTexto.isEmpty(forma.getDtoSeccionConvenioPaciente().getCodigoContrato())){
					contratoOpresupuesto = Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoContrato());
				}
			}
			else
			{
				// Esta condición se ejecuta dentro del ciclo siguiente al preguntar si valida presupuesto = SI
			}
		}
		
		for (DtoServicioOdontologico dtoServicioOdontologico : forma.getServiciosOdon()) 
		{
			servicio = dtoServicioOdontologico.getCodigoServicio();
			
			
			if(forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTratamiento))
			{

				if(forma.getValidaPresupuestoContratado().equals(ConstantesBD.acronimoSi))
				{
					Connection con= UtilidadPersistencia.getPersistencia().obtenerConexion();
					ArrayList<DtoSubCuentas> listaResponsables=UtilidadesHistoriaClinica.obtenerResponsablesIngreso(
										con,
										paciente.getCodigoIngreso(),
										true, // Traer todos los responsables (Facturados y no facturasdos)
										new String[0], // Exluir responsables
										false, // Solamente PYP
										"", // Sub cuenta
										paciente.getCodigoUltimaViaIngreso());
					
					forma.setResponsablesCuenta(listaResponsables);
					
					
					InfoTarifaServicioPresupuesto tarifa=new InfoTarifaServicioPresupuesto();
					InfoResponsableCobertura infoResponsableCobertura=new InfoResponsableCobertura();
					
					for(int w=0; w<listaResponsables.size(); w++)
					{
						DtoSubCuentas subCuenta= listaResponsables.get(w);
						Log4JManager.info("\n\n Codigo Presupuesto Prog >> "+ dtoServicioOdontologico.getCodigoPresuOdoProgSer());
						
						if(dtoServicioOdontologico.getCodigoPresuOdoProgSer()>0)
						{
							Log4JManager.info("OBTENER INFO presupuesto >> ");
							CargosOdon.obtenerInfoPresupuestoContratadoProgSer(con,dtoServicioOdontologico.getCodigoPresuOdoProgSer(), infoResponsableCobertura, tarifa, UtilidadTexto.getBoolean(forma.getUtilProgOdonPlanTra()), dtoServicioOdontologico.getCodigoServicio(), new BigDecimal(forma.getCodigoPlanTratamiento()));
				
							if(!tarifa.getError().equals(""))
							{
								tarifa.setError(tarifa.getError()+" para el servicio "+dtoServicioOdontologico.getDescripcionServicio());
							}
						}
						else
						{
							Log4JManager.info("Obtener info Tarifa Unitaria Por Servicio ");
							infoResponsableCobertura = Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), dtoServicioOdontologico.getCodigoServicio(), Integer.parseInt(usuario.getCodigoInstitucion()), false, subCuenta.getSubCuenta());
							convenio=infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo();
							tarifa=CargosOdon.obtenerTarifaUnitariaXServicio(dtoServicioOdontologico.getCodigoServicio(), convenio, subCuenta.getContrato(), "", usuario.getCodigoInstitucionInt(), new BigDecimal(paciente.getCodigoCuenta()), false /*en este punto no existe programa, por esa razon la cobertura se hace a nivel del servicio*/, usuario.getCodigoCentroCosto());
						}
						
						if(tarifa.getError().equals(""))
						{
							// Como no hay error encontro cobertura y tarifa
							dtoServicioOdontologico.setInfoTarifa(tarifa);
							dtoServicioOdontologico.setResponsableServicio(infoResponsableCobertura.getDtoSubCuenta());
							dtoServicioOdontologico.setPacientePagaAtencion(Contrato.pacientePagaAtencion(infoResponsableCobertura.getDtoSubCuenta().getContrato()));
							
							w=listaResponsables.size();
							break;
						}
					}
					
					try { 
						
						UtilidadBD.cerrarConexion(con); 
						
					} catch (SQLException e) { 
						
						e.printStackTrace(); 
					}
				}
				else // parametro en no
				{
					InfoTarifaServicioPresupuesto infoTarifaServicioPresupuesto;
					infoTarifaServicioPresupuesto = new InfoTarifaServicioPresupuesto();
					infoTarifaServicioPresupuesto = CargosOdon.obtenerTarifaUnitariaXServicio(
							servicio, convenio, contratoOpresupuesto, "", institucion, codigoCuenta, false, centroCosto );
					
					dtoServicioOdontologico.setInfoTarifa(infoTarifaServicioPresupuesto);
				}
			}
			else // diferente tratamiento
			{
				InfoTarifaServicioPresupuesto infoTarifaServicioPresupuesto;
				infoTarifaServicioPresupuesto = new InfoTarifaServicioPresupuesto();
				infoTarifaServicioPresupuesto = CargosOdon.obtenerTarifaUnitariaXServicio(
						servicio, convenio, contratoOpresupuesto, "", institucion, codigoCuenta, false, centroCosto );
				
				dtoServicioOdontologico.setInfoTarifa(infoTarifaServicioPresupuesto);
			}
			
			
			
			
		}// iterando serv
		
		// Cargar abono disponible del paciente
		forma.setAbonosDisponiblesPaciente(Utilidades.obtenerAbonosDisponiblesPaciente(paciente.getCodigoPersona(),paciente.getCodigoIngreso(), usuario.getCodigoInstitucionInt()));
		
		
		for (DtoServicioOdontologico servCargado : forma.getServiciosOdon()) 
		{
			try {
				Log4JManager.info("tarifa cargada: "+servCargado.getInfoTarifa().getValorTarifaTotalConDctosFormateado());
			} catch (Exception e) {
				Log4JManager.info("Las tarifas están null");
			}
			
		}
		
		
		actualizarTotales(usuario, forma, paciente, request);
	}
	
	
	
	/**
	 * M&eacute;todo que se encarga de depurar el listado de los servicios
	 * teniendo en cuenta los posibles casos seg&uacute;n los tipos de cita
	 * 
	 * @param forma
	 * @param usuario
	 * @return ArrayList<DtoServicioOdontologico>
	 */
	private ArrayList<DtoServicioOdontologico> depurarListadoServicios(AgendaOdontologicaForm forma, UsuarioBasico usuario) {
		
		boolean incluir;
		boolean permiteSeleccionarServicio = false;
		boolean indicativoProg = false;
		
		boolean serviciosProgramadosNoDisponible = false;
		
		ArrayList<DtoServicioOdontologico> listaServicios = new ArrayList<DtoServicioOdontologico>()  ; //forma.getOtrosServiciosOdon();
		
		HashMap<Integer, String> codigosServicios = new HashMap<Integer, String>();
		
		/*
		 * Se debe tener en cuenta si esta ingresando a través de una cita del record de citas en estado
		 * programado para poder marcar los respectivos servicios con el indicativo 'PROG'
		 */
		if(forma.getCitaConfirmacion() !=null  && !"".equals(forma.getCitaConfirmacion().getEstado()) &&
				(ConstantesIntegridadDominio.acronimoProgramado).equals(forma.getCitaConfirmacion().getEstado())){
			
			indicativoProg = true;
			
		}
		
		/*
		 * Rol que indica si se puede o no cambiar la selección de un servicio. Solo aplica para la cita tipo Tratamiento
		 */
		if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), ConstantesBD.codigoFuncionalidadSeleccionarServicios)){
			
			permiteSeleccionarServicio = true;		
		}
		
		/*
		 * Si la cita es de tipo tratamiento se debe tener en cuenta, si existen registros,
		 * los servicios que se encuentran asociados a citas en estado programado.
		 * Si estan habilitados deben preseleccionarse, agregar el indicativo 'PROG' y adicionarse
		 * al listado general (Otros Servicios)
		 */
		if (forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTratamiento)){
			
			if(forma.getServiciosOdon()!=null && forma.getServiciosOdon().size()>0){
				
				for (DtoServicioOdontologico servicio : forma.getServiciosOdon()){
					
					incluir = false;
				
					if(servicio.getHabilitarSeleccion().equals(ConstantesBD.acronimoSi)){ /* Unidad de Agenda */ 
						
						incluir = true;
						
					}else if(UtilidadOdontologia.profesionalTieneAsosiadoServicioAdd(servicio.getCodigoServicio(),usuario.getCodigoPersona(),usuario.getCodigoInstitucionInt())){
						
						servicio.setHabilitarSeleccion(ConstantesBD.acronimoSi);
						incluir= true;
					}
					
					if(incluir){
						
						if(!permiteSeleccionarServicio){
							
							servicio.setHabilitarSeleccion(ConstantesBD.acronimoNo);
						}
						/*
						 * En el caso de la cita de tipo Tratamiento no se tiene en cuenta la variable indicativoProg para saber
						 * si se debe o no marcar los servicios con el indicativo, ya que aplica para el caso de una cita nueva
						 * o una cita del record de citas en estado programado
						 */
						servicio.setIndicativo(ConstantesIntegridadDominio.acronimoProgramado);
						servicio.setAsociarSerCita(ConstantesBD.acronimoSi);
						listaServicios.add(servicio);
						codigosServicios.put(servicio.getCodigoServicio(), ConstantesIntegridadDominio.acronimoProgramado);
					
					}else if(!serviciosProgramadosNoDisponible){
						
						/*
						 * Cuando ingresa en esta condición se entiende que existen Servicios
						 * Programados pero que no estan disponibles para su selección por
						 * no encontrarse asociados a la Unidad de Agenda o al Profesional de la Salud.
						 *  
						 *  Debe comportarse igual que citas sin servicios programados.
						 */
						serviciosProgramadosNoDisponible  = true;
						forma.setCasoCita("citaSinServiciosProg");
					}
				}
				
				/*
				 * Este listado de servicios debe limpiarse ya que los servicios contenidos en el ya hacen parte del listado
				 * Otros servicios que es el que debe contenerlos a todos. El listado Otros Servicios es utilizado  para 
				 * actualizar los servicios que se van a relacionar a la cita.
				 */
				forma.setServiciosOdon(new ArrayList<DtoServicioOdontologico>());
			}
		}
		
		/*
		 * Se recorre el listado de servicios retornado desde la consulta y se eliminan los servicios
		 * que estan vinculados a otras citas y los que no estan disponibles para su selección
		 * ya sea porque no estan asociados a la Unidad de Agenda o al profesional de la salud.
		 * 
		 * Esto varia dependiendo del tipo de cita.
		 */
		for(DtoServicioOdontologico servicioCita: forma.getOtrosServiciosOdon()){
			
			incluir = false;
			
			/*
			 * Para los tipos de cita Interconsulta, no se habilitan los
			 * servicios y se asocian automaticamente a la cita.
			 * Sucede lo mismo con las citas seleccionadas del record de citas con estado diferente a programada.
			 * Si es una cita de tipo interconsulta seleccionada a través del record de citas, se deben marcar
			 * los servicios como programado.
			 */
			

			if(forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoRemisionInterconsulta) || "citaRecordNoProgramada".equals(forma.getCasoCita())){
			
				servicioCita.setHabilitarSeleccion(ConstantesBD.acronimoNo);
				servicioCita.setAsociarSerCita(ConstantesBD.acronimoSi);
				listaServicios.add(servicioCita);
				
				// Solo aplica para la cita tipo interconsulta en este caso.
				if(indicativoProg){
					
					servicioCita.setIndicativo(ConstantesIntegridadDominio.acronimoProgramado);
				}

			}else{
				
				if (forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTratamiento)){
				
					if(!codigosServicios.containsKey(servicioCita.getCodigoServicio())){
						
						incluir = true;
					}
				}else{
					
					/*
					 * Aplica para los otros tipos de cita
					 */
					incluir = true;
				}
				
				if(incluir && servicioCita.getEstaVinculadoSerCita().equals(ConstantesBD.acronimoNo)){
					
					
					if (forma.getDtoCitaProgramada().getServiciosCitaOdon() != null && forma.getDtoCitaProgramada().getServiciosCitaOdon().size() > 0) {
						
						for(DtoServicioCitaOdontologica servicioCitaProgramada:forma.getDtoCitaProgramada().getServiciosCitaOdon())
						{
							if(servicioCitaProgramada.getServicio()==servicioCita.getCodigoServicio()
									&&
								servicioCitaProgramada.getProgramaHallazgoPieza().getCodigoPk()==servicioCita.getProgramaHallazgoPieza().getCodigoPk()
							){
								if(servicioCita.getHabilitarSeleccion().equals(ConstantesBD.acronimoSi)){ /* Unidad de Agenda */ 
									
									listaServicios.add(servicioCita);
									
								}else if(UtilidadOdontologia.profesionalTieneAsosiadoServicioAdd(servicioCita.getCodigoServicio(),usuario.getCodigoPersona(),usuario.getCodigoInstitucionInt())){
									
									servicioCita.setHabilitarSeleccion(ConstantesBD.acronimoSi);
									listaServicios.add(servicioCita);
								}
								
							}
						}
					}else{
						
						if(servicioCita.getHabilitarSeleccion().equals(ConstantesBD.acronimoSi)){ /* Unidad de Agenda */ 
							
							listaServicios.add(servicioCita);
							
						}else if(UtilidadOdontologia.profesionalTieneAsosiadoServicioAdd(servicioCita.getCodigoServicio(),usuario.getCodigoPersona(),usuario.getCodigoInstitucionInt())){
							
							servicioCita.setHabilitarSeleccion(ConstantesBD.acronimoSi);
							listaServicios.add(servicioCita);
						}
					}
					
				}
			}
		}
		
		return listaServicios;
	}
	
	/**
	 * Actualiza la informacion de convenios y bonos del paciente de la cita
	 * @param forma
	 * @param dtoInfoBasica
	 * @return boolean
	 * 
	 * @author Cristhian Murillo
	 */
	private boolean actualizarInformacionIngresoPciente(AgendaOdontologicaForm forma,  PersonaBasica persona, DtoInformacionBasicaIngresoPaciente dtoInfoBasica)
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		int contratoConvenio = Integer.parseInt(forma.getDtoSeccionConvenioPaciente().getCodigoContrato());
		int paciente = persona.getCodigoPersona();
		
		ConveniosIngresoPaciente conveniosIngresoPaciente;
		conveniosIngresoPaciente = new ConveniosIngresoPaciente();
		
		ConveniosIngresoPacienteHome cipH = new ConveniosIngresoPacienteHome();
		
		conveniosIngresoPaciente = agendaOdontologicaServicio.obtenerConvenioIngresoPacientePorContrato(paciente, contratoConvenio);
		boolean exito = true;
		
		if(conveniosIngresoPaciente == null){
			// guardar un nuevo convenio para el ingreso del paciente
			conveniosIngresoPaciente = new ConveniosIngresoPaciente();
			conveniosIngresoPaciente.setActivo(dtoInfoBasica.getAcronimoSi().charAt(0));
			conveniosIngresoPaciente.setContratos(dtoInfoBasica.getContrato());
			conveniosIngresoPaciente.setUsuarios(dtoInfoBasica.getUsuario());
			conveniosIngresoPaciente.setPacientes(dtoInfoBasica.getPaciente());
			conveniosIngresoPaciente.setHoraModifica(dtoInfoBasica.getHoraActual());
			conveniosIngresoPaciente.setFechaModifica(dtoInfoBasica.getFechaActual());
			conveniosIngresoPaciente.setActivo(dtoInfoBasica.getAcronimoSi().charAt(0));
			conveniosIngresoPaciente.setPorDefecto(dtoInfoBasica.getAcronimoSi().charAt(0));
			
			try {
				cipH.persist(conveniosIngresoPaciente);
			} catch (Exception e) {
				exito = false;
			}
		}
		else{
			// mirar si tiene bonos y marcarlos como utilizado
			if(conveniosIngresoPaciente.getBonosConvIngPacs() != null){
				ArrayList<BonosConvIngPac> listaBonosConvenioPaciente = new ArrayList<BonosConvIngPac>(conveniosIngresoPaciente.getBonosConvIngPacs());
				for (BonosConvIngPac bonosConvIngPac : listaBonosConvenioPaciente) {
					bonosConvIngPac.setUtilizado(ConstantesBD.acronimoSiChar);
				}
				
				try {
					cipH.merge(conveniosIngresoPaciente);
				} catch (Exception e) {
					exito = false;
				}
			}
		}
			
		if (exito) {
			UtilidadTransaccion.getTransaccion().commit();
		}else{
			UtilidadTransaccion.getTransaccion().rollback();
		}
		
		return exito;
	}
	

	/**
	 * M&eacute;todo que se encarga de realizar la consulta pertinente de los servicios
	 * seg&uacute;n el tipo de cita que se esta ingresando
	 * 
	 * @param forma
	 * @param usuario
	 * @param codigoIngreso
	 * @param unidadAgenda
	 * @param codigoCita
	 * @param codigoMedico
	 * @param buscarPlanTratamiento
	 */
	private String obtenerServiciosXTipoCita(AgendaOdontologicaForm forma, UsuarioBasico usuario, int codigoIngreso, int unidadAgenda, int codigoCita, int codigoMedico, boolean buscarPlanTratamiento) {
		
		String tipoServicio = "";
		String casoCita = "";
		
		/*
		 * Se evalua si se ha seleccionado una cita del record de citas y si su estado es diferente a programado.
		 * Si se cumple la validación se deben postular los servicios de la cita registrada
		 * 
		 */
		if(forma.getCitaConfirmacion() !=null  && !"".equals(forma.getCitaConfirmacion().getEstado()) &&
				!(ConstantesIntegridadDominio.acronimoProgramado).equals(forma.getCitaConfirmacion().getEstado())){
		
			
			if(forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoRemisionInterconsulta)){
				
				forma.setOtrosServiciosOdon(UtilidadOdontologia.obtenerServiciosInterconsulta(forma.getCodCuentaPaciente(),usuario.getCodigoInstitucionInt(), codigoCita));
			 	
		 		forma.setInhabilitaOtrosServicios(true);
		 		
			}else{
				
				String codigosServicios = "";
				/*
				 *Cuando se seleccionan citas del record de citas en estado diferente a programado
				 * listar los servicios asociados a estas citas
				 */
				for (DtoServicioCitaOdontologica servicio : forma.getCitaConfirmacion().getServiciosCitaOdon()) {
					
					if(!codigosServicios.isEmpty()){
						
						codigosServicios += " , ";		
					}
					
					codigosServicios += " " + servicio.getServicio() + " ";
				}
				
				HashMap<String, Object> parametros = agruparParametrosConsultaServicios(forma, usuario.getCodigoInstitucionInt(), unidadAgenda, 
			 			codigoMedico, tipoServicio, codigoCita, buscarPlanTratamiento, codigosServicios, "", "", "", "");
			 	
				
				ArrayList<DtoServicioOdontologico> serviciosConsultados = UtilidadOdontologia.obtenerServicios(parametros);
			
				if(serviciosConsultados.size()>0){
					
					for (DtoServicioCitaOdontologica servicioCita : forma.getCitaConfirmacion().getServiciosCitaOdon()) {
						
						for (DtoServicioOdontologico servicioConsultado : serviciosConsultados) {
							
							if(servicioCita.getServicio() == servicioConsultado.getCodigoServicio()){
								
								if(forma.getCitaConfirmacion().getTipo().equals(ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento)){
									
									if(servicioCita.getProgramaHallazgoPieza().getCodigoPk() == servicioConsultado.getProgramaHallazgoPieza().getCodigoPk()){
										
										forma.getOtrosServiciosOdon().add(servicioConsultado);
									}
									
								}else{
									forma.getOtrosServiciosOdon().add(servicioConsultado);
								}
							}
						}
					}
				}
			}

			casoCita = "citaRecordNoProgramada";
			
		}else {
			
			casoCita = "citaCambioEnServicios";
			
			/*
			 * Cuando ingresa en esta parte de la condicion, quiere decir que se esta trabajando con una cita
			 * totalmente nueva, o una cita en estado programada.
			 */
			if(forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoRemisionInterconsulta))
		 	{
		 		forma.setOtrosServiciosOdon(UtilidadOdontologia.obtenerServiciosInterconsulta(forma.getCodCuentaPaciente(),usuario.getCodigoInstitucionInt(), codigoCita));
		 	
		 		forma.setInhabilitaOtrosServicios(true);
		 		
		 	}else if(forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento)
		 			|| forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoControlCitaOdon)){
		 			
		 		String tipoCita = "";
		 		
		 		if(forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTipoAtencionTratamiento)){
		 			
		 			tipoServicio="'"+ConstantesBD.codigoServicioProcedimiento+"', '"+ConstantesBD.codigoServicioCargosConsultaExterna+"'";
		 			tipoCita = ConstantesIntegridadDominio.acronimoTratamiento;
		 			
		 		}else{
		 			
		 			tipoServicio="'"+ConstantesBD.codigoServicioProcedimiento+"'";
		 			tipoCita = ConstantesIntegridadDominio.acronimoControlCitaOdon;
		 		}
		 		
		
		 		forma.setServiciosOdon(obtenerServiciosXEstadoCitaXTipoCita(forma, usuario, codigoIngreso, unidadAgenda, codigoCita, 
		 				codigoMedico, buscarPlanTratamiento, tipoServicio, tipoCita));
				
		 		/*
				 * Se verifica que existan registros de postulación de servicios por cita programada
				 */
		 		
		 		String codigosServiciosNoIncluir = "";
		 		
				if(forma.getServiciosOdon().size() <= 0){
					
					casoCita = "citaSinServiciosProg";
					
				}else{
					
					/*
					 * Se obtienen los códigos de los servicios asociados a citas programadas que deben 
					 * postularse para que no se incluyan y se evite su selección.
					 */
					for (DtoServicioOdontologico servicio : forma.getServiciosOdon()) {
						
						if(!codigosServiciosNoIncluir.isEmpty()){
							
							codigosServiciosNoIncluir += " , ";
						}
						
						codigosServiciosNoIncluir += " " + servicio.getCodigoServicio() + " ";
					}
				}
				
				/*
				 * Se deben listar tambien, independientemente de si existen o no citas programadas
				 * los servicios asociados al plan de tratamiento y teniendo en cuenta el parámetro
				 * general 'Valida Presupuesto Contratado'
				 */
				
				//String estadosCitasVinculado = " '"+ConstantesIntegridadDominio.acronimoProgramado+"' , '"+ConstantesIntegridadDominio.acronimoReservado+"' , '"+ConstantesIntegridadDominio.acronimoAsignado+"' , '"+ConstantesIntegridadDominio.acronimoReprogramado+"' ";
				
			 	HashMap<String, Object> parametros = agruparParametrosConsultaServicios (forma, usuario.getCodigoInstitucionInt(), unidadAgenda, 
			 			codigoMedico, tipoServicio, codigoCita, buscarPlanTratamiento, "", "",  ""/*estadosCitasVinculado*/, ConstantesBD.acronimoNo, codigosServiciosNoIncluir);
			 
			 	forma.setOtrosServiciosOdon(UtilidadOdontologia.obtenerServicios(parametros));
				
		 	}else {
		 		
				tipoServicio="'"+ConstantesBD.codigoServicioCargosConsultaExterna+"'";
				
			 	
			 	HashMap<String, Object> parametros = agruparParametrosConsultaServicios(forma, usuario.getCodigoInstitucionInt(), unidadAgenda, 
			 			codigoMedico, tipoServicio, codigoCita, buscarPlanTratamiento, "", "", "", "", "");
			 	
			 	forma.setOtrosServiciosOdon(UtilidadOdontologia.obtenerServicios(parametros));
		 	}
		}
		
		return casoCita;
	}


	/**
	 * Metodo que obtiene los servicios asociados a un cita ya registrada y dependiendo del servicio
	 * lo asocia o no al proceso de asignación o al de reserva
	 * 
	 * @param forma
	 * @param usuario
	 * @param codigoIngreso
	 * @param unidadAgenda
	 * @param codigoCita
	 * @param codigoMedico
	 * @param buscarPlanTratamiento
	 * @param tipoServicio
	 * @param tipoCita
	 * @return
	 */
	private ArrayList<DtoServicioOdontologico> obtenerServiciosXEstadoCitaXTipoCita(
			
			AgendaOdontologicaForm forma, UsuarioBasico usuario,
			int codigoIngreso, int unidadAgenda, int codigoCita,
			int codigoMedico, boolean buscarPlanTratamiento,
			String tipoServicio, String tipoCita) {
		
		ArrayList<DtoServicioOdontologico> serviciosOdontologicos = new ArrayList<DtoServicioOdontologico>();
		
		ArrayList<DtoServicioOdontologico> serviciosProg = UtilidadOdontologia.obtenerServiciosXEstadoCitaXTipoCita
		(forma.getPaciente().getCodigo(), codigoIngreso, ConstantesIntegridadDominio.acronimoProgramado, tipoCita, codigoCita);

		String codigosServicios = "";
		
		if(serviciosProg.size()>0){
			
			/*
			 * Cuando existen citas en estado programada de tipo Tratamiento o tipo Control, se deben
			 * listar los servicios asociados a estas citas
			 */
			for (DtoServicioOdontologico servicio : serviciosProg) {
				
				if(!codigosServicios.isEmpty()){
					
					codigosServicios += " , ";
				}
				
				codigosServicios += " " + servicio.getCodigoServicio() + " ";
			}
			
			HashMap<String, Object> parametros = agruparParametrosConsultaServicios(forma, usuario.getCodigoInstitucionInt(), unidadAgenda, 
		 			codigoMedico, tipoServicio, codigoCita, buscarPlanTratamiento, codigosServicios, "", "", ConstantesBD.acronimoNo, "");
		 	
			ArrayList<DtoServicioOdontologico> serviciosConsultados = UtilidadOdontologia.obtenerServicios(parametros);
			
			/*
			 * Contiene los servicios asociados a las citas programadas del paciente.
			 */
			
			if(serviciosConsultados.size()>0){
				
				for (DtoServicioOdontologico servicio : serviciosProg) {
					
					for (DtoServicioOdontologico servicioConsultado : serviciosConsultados) {
						
						if(servicio.getCodigoServicio() == servicioConsultado.getCodigoServicio() 
								&& servicio.getProgramaHallazgoPieza().getCodigoPk() == servicioConsultado.getProgramaHallazgoPieza().getCodigoPk()){
						
							serviciosOdontologicos.add(servicioConsultado);
						}
					}
				}
			}
		}
		
		return serviciosOdontologicos;
	}
	
	

	/**
	 * Valida y crea si se debe crear un ingreso para el paciente
	 * Este método se encarga de
	 * @author Cristhian Murillo
	 */
	private DtoResultado validarCrearIngreso(AgendaOdontologicaForm forma,  PersonaBasica persona, UsuarioBasico usuario, DtoInformacionBasicaIngresoPaciente dtoInfoBasica_)
	{
		DtoResultado resultado = new DtoResultado();
		
		DtoInformacionBasicaIngresoPaciente dtoInfoBasica;
		if(dtoInfoBasica_ == null){
			IIngresosServicio ingresosServicio = ManejoPacienteServicioFabrica.crearIngresosServicio();
			dtoInfoBasica = ingresosServicio.construirDtoInformacionBasicaIngresoPaciente(forma, usuario);
		}
		else{
			dtoInfoBasica = dtoInfoBasica_;
		}
		
		if(persona.getCodigoIngreso() <=0)
		{
			// Se crea un ingreso (subcuenta, cuenta, ingreso)
			
			UtilidadTransaccion.getTransaccion().begin();
			
			AgendaOdontologica agendaOdontologicaMundo = new AgendaOdontologica();
			
			resultado = new DtoResultado();
			resultado = agendaOdontologicaMundo.crearIngresoPaciente(persona, dtoInfoBasica);
			
			if(!resultado.isExitoso())
			{
				// No se pudo crear el ingreso
				UtilidadTransaccion.getTransaccion().rollback();
			}
			else{
				// Ingreso creado
				UtilidadTransaccion.getTransaccion().commit();
			}
		}
		else
		{
			if(forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTratamiento))
			{
				// Se actualiza el ingreso si la cita es de tratamiento (sub_cuentas)
				
				UtilidadTransaccion.getTransaccion().begin();
				
				AgendaOdontologica agendaOdontologicaMundo = new AgendaOdontologica();
				
				resultado = new DtoResultado();
				ArrayList<DtoServicioOdontologico> listaServicios=forma.getServiciosOdon();
				resultado = agendaOdontologicaMundo.actualizarIngresoPaciente(persona, dtoInfoBasica, listaServicios, usuario);
				
				if(!resultado.isExitoso())
				{
					// No se pudo actualizar el ingreso
					UtilidadTransaccion.getTransaccion().rollback();
				}
				else{
					// Ingreso actualizar
					UtilidadTransaccion.getTransaccion().commit();
				}
			}
		}
		
		return resultado;
	}
	
	
	
	/**
	 * Actualiza la informacion del ingreso del paciente
	 * @param forma
	 * @param persona
	 * @param usuario
	 * @param dtoInfoBasica
	 * @param retornar - Indica que se debe hacer la actualizacion de los datos del paciente
	 * 
	 * @author Cristhian Murillo
	 */
	private void llamarActualizarInformacionIngresoPciente(AgendaOdontologicaForm forma,  PersonaBasica persona, UsuarioBasico usuario, DtoInformacionBasicaIngresoPaciente dtoInfoBasica, boolean retornar)
	{
		if(!forma.getTipoCita().equals(ConstantesIntegridadDominio.acronimoTratamiento))
		{
			if (retornar) {
				if(!actualizarInformacionIngresoPciente(forma, persona, dtoInfoBasica)){
					Log4JManager.error("Se crea la cita. Pero no se pudo actualizar la información básica del paciente");
				}
			}
		}
	}
	

	/**
	 * Método que se encarga de actualizar la duración de los servicios asociados a la cita
	 * dependiendo si se esta registrando una cita completamente nueva o una relacionada en el record
	 * de citas del paciente.
	 * 
	 * @param forma
	 * @param listaServicios
	 * @return
	 */
	private ArrayList<DtoServicioOdontologico> actualizarDuracionServicios(AgendaOdontologicaForm forma,	ArrayList<DtoServicioOdontologico> listaServicios) {
		
		DtoCitaOdontologica cita = forma.getCitasOdonPac().get(forma.getPosicionCita());
		
		for(DtoServicioOdontologico servicio: listaServicios)
		{
			for(DtoServicioCitaOdontologica servicioCita: cita.getServiciosCitaOdon())
			{
				if(servicio.getCodigoServicio()==servicioCita.getServicio())
				{	    
					// Si no es -1 entonces es cita de tratamiento
					if(servicio.getCodigoPiezaDental()!=ConstantesBD.codigoNuncaValido)
					{
						//VALIDACION DE LA SUPERFICIE.
						if(servicio.getCodigoPiezaDental() == servicioCita.getPiezaDental() && servicio.getSeccionPlanTrata().equals(servicioCita.getSeccionHallazgo()))
						{
							for(DtoSuperficiesPorPrograma superficieServicios:servicio.getSuperficies())
							{
								for(DtoSuperficiesPorPrograma superficiesCita:servicioCita.getSuperficies())
								{
									if(superficieServicios.getSuperficieDental()==superficiesCita.getSuperficieDental() && servicioCita.getDuracion()>0)
									{
										servicio.setMinutosduracionNuevos(servicioCita.getDuracion());
										break;
									}
								}
							}
						}
					}else{
						
						servicio.setMinutosduracionNuevos(servicioCita.getDuracion());
						break;
					}
				}
			}
		}
		
		return listaServicios;
	}
	
	
	/**
	 * Validaciones para habilitar el link de recibos de caja

	 * @author Jorge Armando Agudelo Quintero
	 * @param usuario {@link UsuarioBasico} en session
	 * @param forma {@link AgendaOdontologicaForm} Formulario
	 * @param paciente 
	 * @since 30 Agosto 2010
	 */
	private void verificarCondicionesParaReciboCaja (UsuarioBasico usuario, AgendaOdontologicaForm forma, PersonaBasica paciente){
		
		forma.setInhabilitaLinkRecibosCaja(true);

		/*
		 * Validar el rol del usuario
		 */
		if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), ConstantesBD.codigoFuncionalidadRecibosCaja))
		{
			/*
			 * Validar si el usuario tiene caja de tipo recaudo en estado 'activo'
			 */
			ICajasServicio cajasServicio=TesoreriaFabricaServicio.crearCajasServicio();
			ArrayList<Cajas> listaCajas = cajasServicio.listarCajasPorCajeroActivasXInstitucionXCentroAtencion(usuario, ConstantesBD.codigoTipoCajaRecaudado);
			
			if(listaCajas.size()>0)
			{
				/*
				 * Se valida si el usuario tiene turno de caja abierto
				 */
				for(Cajas caja: listaCajas )
				{
					IMovimientosCajaServicio movimientosCajaServicio= TesoreriaFabricaServicio.crearMovimientosCajaServicio();
					
					if(movimientosCajaServicio.validarTurnoCajaAbierta(usuario, caja.getConsecutivo())){

						forma.setInhabilitaLinkRecibosCaja (false);	
						
						String url = "../recibosCaja/recibosCaja.do?estado=generarReciboAgendaOdon&idPaciente="+
						paciente.getNumeroIdentificacionPersona()+"&tipoIdentificacion="+
						paciente.getCodigoTipoIdentificacionPersona()+"&consecutivoCaja="+caja.getConsecutivo()+
						"&flujoAgendaOdontologica=true&actividadAgenda="+forma.getCodigoActividad();
						
						forma.setUrlRecibosCaja (url);
						
						break;
					}
				}
			}
		}
	}

	/**
	 * Este m&eacute;todo se encarga de generar la impresión de la cita odontológica en formato
	 * POS
	 * 
	 * @param usuario
	 * @param forma
	 * @param paciente
	 * @param institucionBasica
	 * @param response 
	 */
	private void imprimirCitaOdontologicaPOS (UsuarioBasico usuario, AgendaOdontologicaForm forma, PersonaBasica paciente, InstitucionBasica institucionBasica, HttpServletResponse response) {
		
		DtoCitaOdontologica citaOdontologica = forma.getCitaCreada();
		
		citaOdontologica.setFechaRegistra(forma.getAgendaSel().getFecha());
		citaOdontologica.setUsuarioRegistra(usuario);
		citaOdontologica.setNombreCompletoPaciente(paciente.getNombrePersona());
		citaOdontologica.setTipoIdentificacionPac(paciente.getCodigoTipoIdentificacionPersona());
		citaOdontologica.setNumeroIdentificacionPac(paciente.getNumeroIdentificacionPersona());
		citaOdontologica.setCodigoPaciente(paciente.getCodigoPersona());

		ImpresionCitaOdontologica impresionCitaOdontologica = 	new ImpresionCitaOdontologica(institucionBasica, forma.getServiciosOdon(), citaOdontologica);
		
		JasperPrint reporte = impresionCitaOdontologica.generarReporte();
		
		String nombreArchivoImpresionCita =  impresionCitaOdontologica.exportarReportePDF(reporte, "FormatoImpresionCita");
		
		forma.setNombreArchivoImpresionCita(nombreArchivoImpresionCita);
	}
	 

	/**
	 * Refresca los valores de toda la pagina de asignaciíon
	 * @param usuario
	 * @param forma
	 * @param persona
	 * @param request 
	 */
	private void actualizarTotales(UsuarioBasico usuario, AgendaOdontologicaForm forma, PersonaBasica persona, HttpServletRequest request) throws IPSException 
	{
		double valorPorAbonar = 0;
		
		forma.setAbonosDisponiblesPaciente(Utilidades.obtenerAbonosDisponiblesPaciente(persona.getCodigoPersona(),persona.getCodigoIngreso(), usuario.getCodigoInstitucionInt()));
		
		if(forma.getTipoCitaCrear().equals(ConstantesIntegridadDominio.acronimoAsignado) || forma.getCodigoActividad().equals(ConstantesIntegridadDominio.acronimoTipoFuncionalidadAsignar))
		{
			forma.setMostrarValorTotalCita(true);
		}
		if(forma.isMostrarValorTotalCita())
		{
			calcularTarifaActualOPresupuestoContratado(forma, usuario, request, persona, new ActionErrors(), false);
		}
	}
	
	
	
	/**
	 * Verifica y asocia (en el caso de aplicar) la cita que se guarda a una programada
	 * @param forma
	 * @param usuario
	 * @param persona
	 * @author Cristhian Murillo
	 */
	private void asociarCitasProgramadas(AgendaOdontologicaForm forma, UsuarioBasico usuario, PersonaBasica persona)
	{
		if(forma.getCodigoCitaGuardada() > 0)
		{
			if( (forma.getCitaConfirmacion() !=null) && (ConstantesIntegridadDominio.acronimoProgramado.equals(forma.getEstadoCitaRecord())) )
			{
				Log4JManager.info("cita ya guardada a asociar: "+forma.getCodigoCitaGuardada());
				Log4JManager.info("cita programada a asociar : "+forma.getCodigoCitaRecord());
				
				CitasAsociadasAProgramada citasAsociadasAProgramada = new CitasAsociadasAProgramada();

				UtilidadTransaccion.getTransaccion().begin();
				citasAsociadasAProgramada.setCitasOdontologicasByCitaAsociada(new CitasOdontologicasHome().findById(forma.getCodigoCitaGuardada()));
				citasAsociadasAProgramada.setCitasOdontologicasByCitaProgramada(new CitasOdontologicasHome().findById(forma.getCodigoCitaRecord()));
				UtilidadTransaccion.getTransaccion().commit();
				
				UtilidadTransaccion.getTransaccion().begin();
				boolean guardarAsocio = agendaOdontologicaServicio.guardarCitaAsociadasProgramada(citasAsociadasAProgramada); 
				UtilidadTransaccion.getTransaccion().commit();
				
				if(!guardarAsocio)
				{
					Log4JManager.info("No se pudo asociar la cita a la programada");
				}
				else{
					Log4JManager.info("Se asocio la cita programada con el consecutivo: "+citasAsociadasAProgramada.getCodigoPk());
				}
			}
			else
			{
				if(forma.getTipoCitaCrear().equals(ConstantesIntegridadDominio.acronimoAsignado))
				{
					
					Log4JManager.info("cita ya guardada a asociar: "+forma.getCodigoCitaGuardada());
					
					// Se consultan los servicios de cita programadas para el paciente
					ArrayList<DtoServicioOdontologico> serviciosProg = UtilidadOdontologia.obtenerServiciosXEstadoCitaXTipoCita
						(forma.getPaciente().getCodigo(), persona.getCodigoIngreso(), ConstantesIntegridadDominio.acronimoProgramado, forma.getTipoCita(), ConstantesBD.codigoNuncaValido);
					

					UtilidadTransaccion.getTransaccion().begin();
					
					boolean guardarAsocio = true;
					
					// Se comparan los servicios seleccionados en la asignación de cita con los servicios programados
					for (DtoServicioOdontologico servicioDeEstaCita : forma.getServiciosOdon()) 
					{
						if(servicioDeEstaCita.getAsociarSerCita().equals(ConstantesBD.acronimoSi))
						{
							for (DtoServicioOdontologico servicioProg : serviciosProg) 
							{
								if(servicioProg.getCodigoServicio() == servicioDeEstaCita.getCodigoServicio())
								{
									CitasAsociadasAProgramada citasAsociadasAProgramada;
									citasAsociadasAProgramada = new CitasAsociadasAProgramada();
									
									citasAsociadasAProgramada.setCitasOdontologicasByCitaAsociada(new CitasOdontologicasHome().findById(forma.getCodigoCitaGuardada()));
									citasAsociadasAProgramada.setCitasOdontologicasByCitaProgramada(new CitasOdontologicasHome().findById(servicioProg.getCodigoCita()));
									
									guardarAsocio = agendaOdontologicaServicio.guardarCitaAsociadasProgramada(citasAsociadasAProgramada); 
									
									if(!guardarAsocio)
									{
										break;
									}
								}
							}
						}
					}
					
					if(guardarAsocio){
						
						UtilidadTransaccion.getTransaccion().commit();
						Log4JManager.info("Se asociaron las citas correctamente");
					
					}else{
						
						UtilidadTransaccion.getTransaccion().rollback();
						Log4JManager.info("No se pudo asociar la cita a la programada");
					}
				}
			}
		}
	}
	
	/**
	 * Método que obtiene el código del plan de tratamiento 
	 * del paciente según un conjunto de estado.
	 * 
	 * @param forma
	 * @return 
	 */
	private int obtenerCodigoPlanTratamientoPorEstados (AgendaOdontologicaForm forma, ArrayList<String> estados){
		
		//ArrayList<String> estados = new ArrayList<String>();
		//estados.add(ConstantesIntegridadDominio.acronimoInactivo);
		ArrayList<BigDecimal> codigosPlanT = PlanTratamiento.obtenerCodigoPlanTratamiento(forma.getPaciente().getCodigo(), estados, ConstantesBD.acronimoNo);
		
		if(codigosPlanT!=null)
		{
			return codigosPlanT.size();
			
		}else{
			
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	/**
	 * Método que se encarga de pasar los valores del objeto {@link DtoBusquedaAgendaRango}
	 * a la forma.
	 * 
	 * @param forma
	 */
	private void pasarValoresBusquedaXRangoAForma (AgendaOdontologicaForm forma){
		
		forma.setCitaConfirmacion(new DtoCitaOdontologica());
		
		DtoBusquedaAgendaRango  busquedaAgendaRango = forma.getDtoBusquedaAgendaRango();
		
		forma.setPais(busquedaAgendaRango.getPais());
		forma.setCiudad(busquedaAgendaRango.getCiudad());
		forma.setCentroAtencion(busquedaAgendaRango.getCentroAtencion());
		forma.setUnidadAgenda(busquedaAgendaRango.getUnidadAgenda());
		forma.setProfesionalSalud(busquedaAgendaRango.getProfesionalSalud());
		forma.setFechaInicial(busquedaAgendaRango.getFechaInicial());
		forma.setFechaFinal(busquedaAgendaRango.getFechaFinal());
		forma.setTipoCita(busquedaAgendaRango.getTipoCita());
		forma.setEstadoCita(busquedaAgendaRango.getEstadoCita());
		busquedaAgendaRango.setIndicativoConf(forma.getIndicativoConf());
	}
	

	/**
	 * Método que se encarga de pasar los valores de la forma {@link AgendaOdontologicaForm}
	 * al objeto {@link DtoBusquedaAgendaRango}
	 * 
	 * @param forma
	 */
	private void pasarValoresFormaABusquedaXRango(AgendaOdontologicaForm forma) {

		DtoBusquedaAgendaRango  busquedaAgendaRango = new DtoBusquedaAgendaRango();
		
		busquedaAgendaRango.setPais(forma.getPais());
		busquedaAgendaRango.setCiudad(forma.getCiudad());
		busquedaAgendaRango.setCentroAtencion(forma.getCentroAtencion());
		busquedaAgendaRango.setUnidadAgenda(forma.getUnidadAgenda());
		busquedaAgendaRango.setProfesionalSalud(forma.getProfesionalSalud());
		busquedaAgendaRango.setFechaInicial(forma.getFechaInicial());
		busquedaAgendaRango.setFechaFinal(forma.getFechaFinal());
		busquedaAgendaRango.setTipoCita(forma.getTipoCita());
		busquedaAgendaRango.setEstadoCita(forma.getEstadoCita());
		busquedaAgendaRango.setIndicativoConf(forma.getIndicativoConf());
		
		forma.setDtoBusquedaAgendaRango(busquedaAgendaRango);
	}
	
	/**
	 * Este método se encarga de 
	 *
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param mundo
	 * @return
	 *
	 * @autor Yennifer Guerrero
	 */
	private ActionForward accionConsultarHistoricoEstadosCita(
			AgendaOdontologicaForm forma, UsuarioBasico usuario,
			ActionMapping mapping, AgendaOdontologica mundo) {
		
		Connection con = UtilidadBD.abrirConexion();
		
		int indiceCitaSeleccionada = forma.getIndiceCitaSeleccionada();
		int codigoCita = forma.getTodasCitas().get(indiceCitaSeleccionada).getCodigoPk();
		
		forma.setCitaSeleccionadaHistorico(forma.getTodasCitas().get(indiceCitaSeleccionada));
		
		forma.setHistoricoCita(mundo.obtenerHistoricoEstadosCita(con,codigoCita));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("historicoEstadosCita");
	}
	
}
