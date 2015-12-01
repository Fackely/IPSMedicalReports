/*
* @(#)CitaAction.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.action.agenda;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ConstantesValoresPorDefecto;
import util.ElementoApResource;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.RespuestaValidacion;
import util.ResultadoBoolean;
import util.TipoNumeroId;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;
import util.consultaExterna.UtilidadesConsultaExterna;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.agenda.CitaForm;
import com.princetonsa.dto.agenda.DTOAdministrarSolicitudesAutorizar;
import com.princetonsa.dto.facturacion.DTOMontosCobro;
import com.princetonsa.dto.manejoPaciente.DtoAdjAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;
import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoRespAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.Agenda;
import com.princetonsa.mundo.agenda.Cita;
import com.princetonsa.mundo.capitacion.SubirPaciente;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.facturacion.CondicionesXServicios;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;
import com.princetonsa.mundo.manejoPaciente.Autorizaciones;
import com.princetonsa.mundo.ordenesmedicas.OrdenesAmbulatorias;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.parametrizacion.RegistroUnidadesConsulta;
import com.princetonsa.mundo.salasCirugia.Peticion;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.bl.ordenes.facade.OrdenesFacade;
import com.servinte.axioma.common.ErrorMessage;
import com.servinte.axioma.dto.capitacion.DtoAutorizacionCapitacionOrdenAmbulatoria;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.facturacion.ConvenioDto;
import com.servinte.axioma.dto.facturacion.MontoCobroDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.DatosPacienteAutorizacionDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroCostoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IAutorizacionCapitacionOrdenesAmbulatoriasMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IMontosCobroMundo;
import com.servinte.axioma.orm.Convenios;

/**
* Action, controla todas las opciones dentro de una cita, incluyendo los posibles casos de error,
* los casos de flujo.
*
* @version 1.0, Mar 24, 2004
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*
* @see org.apache.struts.action.ActionForm#validate(ActionMapping, HttpServletRequest)
*/
@SuppressWarnings("unchecked")
public class CitaAction extends Action
{
	/** Objeto que permite el manejo de archivos de registro */
	private transient Logger logger = Logger.getLogger(CitaAction.class);

	/** Definiciï¿½n de constantes del estado del flujo */
	private static final int ii_ESTADO_INVALIDO				= -1;
	private static final int ii_ASIGNAR						= 1;
	private static final int ii_CANCELAR_ASIGNACION			= 2;
	private static final int ii_CANCELAR_RESERVA			= 3;
	private static final int ii_CONFIRMAR_ASIGNACION		= 4;
	private static final int ii_CONFIRMAR_RESERVA			= 5;
	private static final int ii_ELIMINAR_AGENDA_ASIGNACION	= 6;
	private static final int ii_ELIMINAR_AGENDA_RESERVA		= 7;
	private static final int ii_INCLUIR_AGENDA_ASIGNACION	= 8;
	private static final int ii_INCLUIR_AGENDA_RESERVA		= 9;
	private static final int ii_LISTADO_ASIGNAR				= 10;
	private static final int ii_LISTADO_RESERVAR			= 11;
	private static final int ii_PREPARAR_LISTADO_ASIGNAR	= 12;
	private static final int ii_PREPARAR_LISTADO_RESERVAR	= 13;
	private static final int ii_RESERVAR					= 14;
	private static final int ii_VALIDAR_LISTADO_ASIGNAR		= 15;
	private static final int ii_VALIDAR_LISTADO_RESERVAR	= 16;
	private static final int ii_RESUMEN_IMPRESION_CITA = 17;
	private static final int ii_CAMBIAR_CENTRO_ATENCION_ASIGNAR	= 18;
	private static final int ii_CAMBIAR_CONVENIO		= 19;
	private static final int ii_VERIFICAR_USUARIO_CAPITADO		= 20;
	private static final int ii_REGRESAR_RESERVA		= 21; //solo aplica cuando viene del ingreso del paciente
	private static final int ii_ADICIONAR_SERVICIO = 22;
	private static final int ii_ELIMINAR_SERVICIO = 23;
	private static final int ii_CONSULTAR_CONDICIONES_SERVICIO = 24;
	private static final int ii_INICIAR_BUSQUEDA_AVANZADA = 25;
	private static final int ii_RESULTADO_BUSQUEDA_AVANZADA = 26;
	private static final int ii_VERIFICAR_FINALIDAD = 27;
	private static final int ii_GUARDAR_CIERRE_INGRESO = 28;
	private static final int ii_GUARDAR_INGRESO = 29;
	private static final int ii_CAMBIAR_CENTRO_ATENCION_RESERVAR= 30;
	private static final int ii_REGISTRO_RESPUESTA_SOL_AUTO= 31; // registro respuesta de solicitud de utorizacion  
	private static final int ii_GUARDAR_RESPUESTA_SOL_AUTO = 32;  // guardar respuesta autorizacion en el dto de repuestas
	private static final int ii_ADJUNTAR_ARCHIVO_RESPUESTA_SOL_AUTO = 33;  // adjuntar archivos a la respuesta de autorizacion
	private static final int ii_GUARDAR_AUTORIZACION_CITA = 34; // guada el motivo de la autorizacion de la cita
	private static final int ii_LISTAR_CITAS_INCUMPLIDAS= 35 ; //lista las citas incumplidas
	private static final int ii_VERIFICAR_CITAS_INCUMPLIDAS = 36; // verificar si el paciente posee citas incumplidas
	
	
	/** Controla el flujo principal de la asignación y la reserva de citas */
	@SuppressWarnings("unused")
	public ActionForward execute(ActionMapping mapping, ActionForm aaf_form, HttpServletRequest	request, HttpServletResponse ahsr_response)throws Exception
	{

		Connection con=null;
		try {
		/* Validar que el tipo de formulario recibido sea el esperado */
		if(aaf_form instanceof CitaForm)
		{
			CitaForm		citaForm;
			int				estado;
			String			estadoStr;

			
			/* Obtener el estado actual */
			citaForm = (CitaForm)aaf_form;
			estadoStr = citaForm.getEstado();
			Cita cita = new Cita();
				con = UtilidadBD.abrirConexion();
			HttpSession session = request.getSession();
			PersonaBasica paciente =(PersonaBasica) session.getAttribute("pacienteActivo");
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			//InstitucionBasica institucionBasica=usuario.getInstitucion();
			//InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			logger.warn(" [CitaAction] estado-> "+estadoStr);
			
			
			if(estadoStr.equals("prepararListadoAsignar"))
			{
				citaForm.setOcultarEncabezado("N");
				citaForm.setOrdenAmbulatoria("");
				citaForm.setIndicativoOrdenAmbulatoria(false);
				citaForm.setSolPYP(false);
				citaForm.setServicioAmbulatorioPostular("");
				citaForm.setCentroCostoAmbulatorioSel("");
			}
			
			/* Validar el estado actual del flujo */
			if(estadoStr == null)
				estado = ii_ESTADO_INVALIDO;
			else if(estadoStr.equals("asignar") )
				estado = ii_ASIGNAR;
		// Cambio anexo 652 control post operatorio
			else if(estadoStr.equals("guardarCerrarIngreso") )
				estado=ii_GUARDAR_CIERRE_INGRESO;
			else if(estadoStr.equals("guardarIngreso") )
				estado=ii_GUARDAR_INGRESO;
		// fin cambio anexo 652
			else if(estadoStr.equals("verificarFinalidad"))
				estado = ii_VERIFICAR_FINALIDAD;
			else if(estadoStr.equals("cancelarAsignacion") )
				estado = ii_CANCELAR_ASIGNACION;
			else if(estadoStr.equals("cancelarReserva") )
				estado = ii_CANCELAR_RESERVA;
			else if(estadoStr.equals("confirmarAsignacion") )
				estado = ii_CONFIRMAR_ASIGNACION;
			else if(estadoStr.equals("confirmarReserva") )
				estado = ii_CONFIRMAR_RESERVA;
			else if(estadoStr.equals("eliminarAgendaAsignacion") )
				estado = ii_ELIMINAR_AGENDA_ASIGNACION;
			else if(estadoStr.equals("eliminarAgendaReserva") )
				estado = ii_ELIMINAR_AGENDA_RESERVA;
			else if(estadoStr.equals("incluirAgendaAsignacion") )
				estado = ii_INCLUIR_AGENDA_ASIGNACION;
			else if(estadoStr.equals("incluirAgendaReserva") )
				estado = ii_INCLUIR_AGENDA_RESERVA;
			else if(estadoStr.equals("listadoAsignar") )
				estado = ii_LISTADO_ASIGNAR;
			else if(estadoStr.equals("listadoReservar") )
				estado = ii_LISTADO_RESERVAR;
			else if(estadoStr.equals("prepararListadoAsignar") || estadoStr.equals("prepararListadoAsignarOA"))
				estado = ii_PREPARAR_LISTADO_ASIGNAR;
			else if(estadoStr.equals("prepararListadoReservar") )
				estado = ii_PREPARAR_LISTADO_RESERVAR;
			else if(estadoStr.equals("reservar") )
				estado = ii_RESERVAR;
			else if(estadoStr.equals("validarListadoAsignar") || estadoStr.equals("validarListadoAsignarOA") )
				estado = ii_VALIDAR_LISTADO_ASIGNAR;
			else if(estadoStr.equals("validarListadoReservar") )
				estado = ii_VALIDAR_LISTADO_RESERVAR;
			else if(estadoStr.equals("resumenImpresionCita"))
				estado = ii_RESUMEN_IMPRESION_CITA;
			else if(estadoStr.equals("cambiarCentroAtencionAsignar"))
				estado = ii_CAMBIAR_CENTRO_ATENCION_ASIGNAR;
			else if(estadoStr.equals("cambiarCentroAtencionReservar"))
				estado = ii_CAMBIAR_CENTRO_ATENCION_RESERVAR;
			else if(estadoStr.equals("cambiarConvenio"))
				estado = ii_CAMBIAR_CONVENIO;
			else if(estadoStr.equals("verificarUsuarioCapitado"))
				estado = ii_VERIFICAR_USUARIO_CAPITADO;
			else if(estadoStr.equals("regresarReserva"))
				estado = ii_REGRESAR_RESERVA;
			else if (estadoStr.equals("adicionarServicio")  || estadoStr.equals("adicionarServicioReservar"))
				estado = ii_ADICIONAR_SERVICIO;
			else if (estadoStr.equals("adicionarServicioAsignar"))
			{
				estado = ii_ADICIONAR_SERVICIO;
				return accionAdicionarServicio(con, mapping, citaForm, usuario,paciente,true);
			}
			else if (estadoStr.equals("eliminarServicio"))
				estado = ii_ELIMINAR_SERVICIO;
			else if (estadoStr.equals("consultarCondicionesServicio"))
				estado = ii_CONSULTAR_CONDICIONES_SERVICIO;
			else if (estadoStr.equals("iniciarBusquedaAvanzada")) //solo aplica para el flujo de reserva
				estado = ii_INICIAR_BUSQUEDA_AVANZADA;
			else if (estadoStr.equals("resultadoBusquedaAvanzada")) //solo aplica para el flujo de reserva
				estado = ii_RESULTADO_BUSQUEDA_AVANZADA;
			else if (estadoStr.equals("validarAutoIngEvento") || estadoStr.equals("reservarAutoIng"))			
			{			
				return reservar(mapping, citaForm, request, con, usuario,true);				
			}
			else if(estadoStr.equals("validarAutoIngEventoAsig"))
			{
				return asignar(mapping, citaForm, request, con,true);
			}
			else if(estadoStr.equals("generarReporte"))
			{				
				accionGenerarReportePdf(con,citaForm,usuario,request);	 
				UtilidadBD.closeConnection(con);
				return mapping.findForward("imprimirCitaPdf");				 
			}else if(estadoStr.equals("regRespSolAuto"))
				estado = ii_REGISTRO_RESPUESTA_SOL_AUTO;
			else if(estadoStr.equals("guardarRespAuto"))
				estado = ii_GUARDAR_RESPUESTA_SOL_AUTO;
			else if(estadoStr.equals("iniciarAdjuntarArchivos"))
				estado = ii_ADJUNTAR_ARCHIVO_RESPUESTA_SOL_AUTO;
			else if(estadoStr.equals("guardarAutorizacionCita"))
				estado = ii_GUARDAR_AUTORIZACION_CITA;
			else if(estadoStr.equals("listarCitasIncumplidas"))
				estado = ii_LISTAR_CITAS_INCUMPLIDAS;
			else if(estadoStr.equals("verificarEstCitasPac"))
				estado = ii_VERIFICAR_CITAS_INCUMPLIDAS;
			else if (estadoStr.equals("redireccion"))
			{
				UtilidadBD.closeConnection(con);
				ahsr_response.sendRedirect(citaForm.getLinkSiguiente());
				return null;
			}
			else
				estado = ii_ESTADO_INVALIDO;

			switch(estado)
			{
				case ii_ESTADO_INVALIDO:
					/* El estado actual es intederminado y por lo tanto no vï¿½lido */
					if(logger.isDebugEnabled() )
						logger.debug("Estado inválido en el flujo de agenda de consultas");

					cerrarConexion(con);
					return error(mapping, request, "errors.estadoInvalido", true);

				case ii_ASIGNAR:
					/*
						Asignar una o varias citas para asignación. Generar solicitud para las citas
						creadas en el proceso de reserva de citas
					*/
					/* Reservar una o varias citas */
					
					return asignar(mapping, citaForm, request, con,false);
				case ii_GUARDAR_CIERRE_INGRESO:
					/*
						guardar el ingreso con indicativo de control post operatorio y cambiar el estado de ingreso en 'cerrado'
					*/
					logger.info("[-> Guardar Cierre Ingreso <-]");
					return 	cerrarGuardarIngreso(mapping, citaForm, request, con, paciente, usuario);
				case ii_GUARDAR_INGRESO:
					/*
						guardar automaticamenteel ingreso con indicativo de control post operatorio
					*/
					logger.info("[-> Guardar Ingreso <-]");
					return 	guardarIngreso(mapping, citaForm, request, con, paciente, usuario);
				case ii_VERIFICAR_FINALIDAD:
					/*
					 Estado creado por requerimiento de la Tarea 38989 y Tarea 38990 la cual sustenta
					 que se debe cargan las finalidades de los servicios que son de procedimientos
					 */
					return verificarFinalidad(mapping, citaForm, request, con, usuario.getCodigoInstitucionInt());
					
				case ii_CANCELAR_ASIGNACION:
				case ii_CANCELAR_RESERVA:
					/* Cancela el proceso de reserva / asignaciï¿½n de citas */
					if(estado == ii_CANCELAR_ASIGNACION)
						citaForm.setEstado("validarListadoAsignar");
					else
						citaForm.setEstado("validarListadoReservar");
					this.cerrarConexion(con);
					return mapping.findForward("cita");

				case ii_CONFIRMAR_ASIGNACION:
				case ii_CONFIRMAR_RESERVA:
					/*
						Confirma la reserva / asignaciï¿½n de una cita cuando el paciente ya tiene registrada
						citas para la misma fecha
					*/
					citaForm.setItemSeleccionado("validarCitaFecha_" + citaForm.getIndiceItemSeleccionado(),new Boolean(false));

					if(estado == ii_CONFIRMAR_ASIGNACION)
						return asignar(mapping, citaForm, request, con,true);
					else
						return reservar(mapping, citaForm, request, con, usuario,true);

				case ii_ELIMINAR_AGENDA_ASIGNACION:
				case ii_ELIMINAR_AGENDA_RESERVA:
					/* Eliminar un item de agenda de la lista de citas a reservar / asignar */
					return eliminarAgenda(mapping, citaForm, estado, con);

				case ii_INCLUIR_AGENDA_ASIGNACION:
				case ii_INCLUIR_AGENDA_RESERVA:
					
					/* Incluir un item de agenda en la lista de citas a reservar / asignar */
					return incluirAgenda(mapping, citaForm, request, estado, con, cita);

				case ii_LISTADO_ASIGNAR:
					/* Listar la agenda disponible para el paciente */
					int actividada = ConstantesBD.codigoActividadAutorizadaAsignarCitas;
					return listarAgenda(mapping, citaForm, request,con, false, actividada);
				case ii_LISTADO_RESERVAR:
					/* Listar la agenda disponible para el paciente */
					int actividadr = ConstantesBD.codigoActividadAutorizadaReservarCitas;
					return listarAgenda(mapping, citaForm, request,con, true, actividadr);

				case ii_PREPARAR_LISTADO_ASIGNAR:
					
				case ii_PREPARAR_LISTADO_RESERVAR:
					
					// Si aplica validar las agendas por servicio
					String servicioA="";
					logger.info("isFiltrarAgendasXServicio "+citaForm.isFiltrarAgendasXServicio());
					if(citaForm.isFiltrarAgendasXServicio()){
						citaForm.setServicioAmbulatorioPostular(request.getParameter("servicioAmbulatorioPostular")+"");
						servicioA = citaForm.getServicioAmbulatorioPostular();
					}
					//Se elimina esta validación porque según Análisis y Calidad
					//definen que se debe hacer así   16/11/2011
//					if(citaForm.isIndicativoOrdenAmbulatoria())
//					{
//						citaForm.setCentroCostoAmbulatorioSel(request.getParameter("centroCostoAmbulatorioSel")+""); 
//						
//						if(UtilidadTexto.isEmpty(citaForm.getCentroCostoAmbulatorioSel()))
//						{
//							ActionErrors errores = new ActionErrors();
//							errores.add("",new ActionMessage("errors.notEspecific","No se encontró un centro de costo asociado. Por favor verifique."));
//							saveErrors(request, errores);
//							return mapping.findForward("validarCentroCosto"); 
//						}
//						else if(citaForm.getCentroCostoAmbulatorioSel().equals(ConstantesBD.codigoNuncaValido+""))
//						{
//							ActionErrors errores = new ActionErrors();
//							errores.add("",new ActionMessage("errors.notEspecific","No se encontró un centro de costo asociado. Por favor verifique."));
//							saveErrors(request, errores);
//							return mapping.findForward("validarCentroCosto"); 
//						}
//						else{
//							inicializarSelectCentroAtencionUnidadAgenda(citaForm, con, usuario, request, servicioA);
//						}
//					}else{
						// Obtener centros de atenciï¿½n validos para el usuario
						if (estado==ii_PREPARAR_LISTADO_ASIGNAR){
							citaForm.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaAsignarCitas));
							citaForm.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuarioXServicio(con, usuario.getLoginUsuario(), usuario.getCodigoCentroAtencion(), ConstantesBD.codigoActividadAutorizadaAsignarCitas,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral, servicioA));
						}
						if (estado==ii_PREPARAR_LISTADO_RESERVAR){
							citaForm.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaReservarCitas));
							citaForm.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuarioXServicio(con, usuario.getLoginUsuario(), usuario.getCodigoCentroAtencion(), ConstantesBD.codigoActividadAutorizadaReservarCitas,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral, servicioA));
						}
//					}
					
					//Si es reserva se verifica el parametro que indica si es requerido
					//ingresar la informaciï¿½n de la cuenta
					if(estado==ii_PREPARAR_LISTADO_RESERVAR || estado==ii_PREPARAR_LISTADO_ASIGNAR)
						{
						
							//Se resetean las variables 
							citaForm.setDatosCuentaReserva(ConstantesBD.acronimoNo);
							citaForm.setVerificadoUsuarioCapitado(false);
							citaForm.setCodigoUsuarioCapitado(ConstantesBD.codigoNuncaValido);
							
							/*Se obtiene el parametro de valores por defecto, para saber
							 * si de debe capturar los datos de la cuenta en la reserva
							 */
							String datosCuentaReserva=ConstantesBD.acronimoNo;
							
							//Si es true es requerida la informaciï¿½n de cuenta en la reserva de la cita.
							if(UtilidadTexto.getBoolean(ValoresPorDefecto.getDatosCuentaRequeridoReservaCitas(usuario.getCodigoInstitucionInt())))
								{
									datosCuentaReserva=ConstantesBD.acronimoSi;
								}
							
						 	citaForm.setDatosCuentaReserva(datosCuentaReserva);
						}
					
					/**
					 * Validar concurrencia
					 * Si ya estï¿½ en proceso de facturaciï¿½n, no debe dejar entrar
					 **/
					if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "" /*idSesion*/) )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
					}
					else
					{
						/* Prepara el listado de la agenda disponible para reserva / asignar citas */
						return iniciarCita(mapping, citaForm, request, estado, con, cita);
					}

				case ii_RESERVAR:
					/* Reservar una o varias citas */
					return reservar(mapping, citaForm, request, con, usuario,false);

				case ii_VALIDAR_LISTADO_ASIGNAR:
				case ii_VALIDAR_LISTADO_RESERVAR:
					/*
						Prepara el listado de la agenda disponible para reserva / asignaciï¿½n de
						citas
					*/
					// Obtener centros de atenciï¿½n validos para el usuario
					
					// Si aplica validar las agendas por servicio
					String servicio=""; 
					if(citaForm.isFiltrarAgendasXServicio())
						servicio = citaForm.getServicioAmbulatorioPostular();
					
					if (estado==ii_VALIDAR_LISTADO_ASIGNAR){
						citaForm.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaAsignarCitas));
						citaForm.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, usuario.getLoginUsuario(), usuario.getCodigoCentroAtencion(), ConstantesBD.codigoActividadAutorizadaAsignarCitas,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));
					}
					if (estado==ii_VALIDAR_LISTADO_RESERVAR){
						if (citaForm.getConvenio() != -1)
						{ citaForm.setListando(true); 
									}
						citaForm.setEstadoant("1");
						citaForm.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaReservarCitas));
						citaForm.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuarioXServicio(con, usuario.getLoginUsuario(), usuario.getCodigoCentroAtencion(), ConstantesBD.codigoActividadAutorizadaReservarCitas,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral, servicio));
					}
					return validarListado(mapping, citaForm, request, estado, con);
				
				case ii_RESUMEN_IMPRESION_CITA:
					this.cerrarConexion(con);
					return this.resumenImpresionCita(mapping, request);
					
				case ii_CAMBIAR_CENTRO_ATENCION_ASIGNAR:
					citaForm.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, usuario.getLoginUsuario(), citaForm.getCentroAtencion(), ConstantesBD.codigoActividadAutorizadaAsignarCitas,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));
					this.cerrarConexion(con);
					return mapping.findForward("cita");
				
				case ii_CAMBIAR_CENTRO_ATENCION_RESERVAR:
					String serv="";
					if(citaForm.isFiltrarAgendasXServicio())
						serv=citaForm.getServicioAmbulatorioPostular();
						
					citaForm.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuarioXServicio(con, usuario.getLoginUsuario(), citaForm.getCentroAtencion(), ConstantesBD.codigoActividadAutorizadaReservarCitas, ConstantesIntegridadDominio.acronimoTipoAtencionGeneral, serv));
					this.cerrarConexion(con);
					return mapping.findForward("cita");		
						
				case ii_CAMBIAR_CONVENIO:
					return accionCambiarConvenio(con,citaForm,ahsr_response);
					
				case ii_VERIFICAR_USUARIO_CAPITADO:
					return accionVerificarUsuarioCapitado(con, mapping, citaForm,ahsr_response);
					
				case ii_REGRESAR_RESERVA:
					citaForm.setTipoIdentificacionPaciente("");
					citaForm.setNumeroIdentificacionPaciente("");
					cerrarConexion(con);
					return mapping.findForward("cita");
					
				case ii_ADICIONAR_SERVICIO:
					return accionAdicionarServicio(con, mapping, citaForm, usuario,paciente,false);
					
				case ii_ELIMINAR_SERVICIO: //Para filtros Ajax
					return accionEliminarServicio(con,citaForm,ahsr_response);
					
				case ii_CONSULTAR_CONDICIONES_SERVICIO:
					return accionConsultarCondicionesServicio(con,citaForm,mapping,usuario);
				
				//**********ESTADOS USADOS PARTA LA BUSQUEDA AVANZADA DE PACIENTES EN LA RESERVA DE CITAS*******************
				case ii_INICIAR_BUSQUEDA_AVANZADA:
					return accionIniciarBusquedaAvanzada(con,citaForm,mapping);
				case ii_RESULTADO_BUSQUEDA_AVANZADA:
					cerrarConexion(con);
					return mapping.findForward("cita");
				case ii_REGISTRO_RESPUESTA_SOL_AUTO:
					// cargar registro respuesta
					UtilidadBD.finalizarTransaccion(con);
					if(citaForm.getDtoRespAuto().isEstadoIngresoResp())
						citaForm.getDtoRespAuto().setEstadoIngresoResp(false);
					cargarDatosForResAuto(con,citaForm,usuario,session,request);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("regRespSolAuto");
				case ii_GUARDAR_RESPUESTA_SOL_AUTO:
					// guarda la respuesta de autorizacion
					UtilidadBD.closeConnection(con);
					return guardarRespAutorizacion(citaForm, mapping, request);
				case ii_ADJUNTAR_ARCHIVO_RESPUESTA_SOL_AUTO:
					// adjnta archivos a la respuesta de autorizacion
					return accionIniciarAdjuntarArchivos(con, citaForm, mapping, usuario);
				case ii_GUARDAR_AUTORIZACION_CITA:
					// guardar el motivo de la autorizacion de la cita y mostrar los servicios
					return guardarMotivoAutorizacion(con,citaForm,mapping,request);
				case ii_LISTAR_CITAS_INCUMPLIDAS:
					// lista las citas incumplidas del paciente
					return mapping.findForward("listarCitasIncumplidas");
				case ii_VERIFICAR_CITAS_INCUMPLIDAS:
					// verificar el estado de las citas del paciente
					return verificarEstCitasPac(con,citaForm,mapping,cita,request);
			}
		}

		/* El formulario recibido no corresponde al formulario de reserva / asignaciï¿½n de citas */
		if(logger.isDebugEnabled() )
			logger.debug(
				"El formulario actual no corresponde con el formulario esperado por reserva " +
				" / asignación de citas"
			);

		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return error(mapping, request, "errors.accesoInvalido", true);
	}
	
	
	
	/**
	* Abre una conexiï¿½n a una fuente de datos
	* ab_iniciarTransaccion indicador de si la conexiï¿½n debe o no iniciar una transacciï¿½n
	* @throws SQLException
	*/
/*	private Connection abrirConexion(boolean ab_iniciarTransaccion)
	{
		Connection	con;
		DaoFactory	daoFactory;

		 Obtener una conexiï¿½n a la fuente de datos  
		try
		{
			daoFactory	= DaoFactory.getDaoFactory(System.getProperty("TIPOBD") );
			con	= daoFactory.getConnection();

			 Iniciar una transacciï¿½n si es necesario 
			if(ab_iniciarTransaccion)
			{
				if(!daoFactory.beginTransaction(con) )
					con = null;
			}
		}
		catch(SQLException e)
		{
			if(logger.isDebugEnabled() )
				logger.debug("No se pudo obtener conexiï¿½n a la fuente de datos");

			con = null;
		}

		return con;
	}
*/
	/**
	* Asigna para citas los items de agenda de consulta incluidos en la lista de asingaciï¿½n.
	* Genera solicitud para las citas reservadas
	*/
	@SuppressWarnings({ "deprecation", "rawtypes" })
	private ActionForward asignar(ActionMapping mapping,CitaForm citaForm,HttpServletRequest request,Connection con,boolean entroAutorizacionIngEven)throws Exception
	{		
	    ActionForward	actionForward;
		PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		/** *Dto que almacena el número de las solicitudes generadas y codigo de los servicios asociados **/
		/** @author Diana Carolina G **/
		DTOAdministrarSolicitudesAutorizar dtoAdministrarSolicitudesAutorizar 				  = new DTOAdministrarSolicitudesAutorizar();
		ArrayList<DTOAdministrarSolicitudesAutorizar> listaDtoAdministrarSolicitudesAutorizar = new ArrayList<DTOAdministrarSolicitudesAutorizar>();
		
		
		
		UsuarioBasico usuario=new UsuarioBasico();
		
		//logger.info("Inicio de la asignación de citas. Indicativo orden ambulatoria: "+citaForm.isIndicativoOrdenAmbulatoria());
		//logger.info("Inicio de la asignación de citas. orden ambulatoria: "+citaForm.getOrdenAmbulatoria());
		//se debe asignar en la solicitud el usuario original es decir el que genero la orden ambulatoria
		if(citaForm.isIndicativoOrdenAmbulatoria())
		{
			int institucion= ((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCodigoInstitucionInt();
			String infoSol=OrdenesAmbulatorias.obtenerInfoServicioProcOrdenAmbulatoria(con,Utilidades.obtenerCodigoOrdenAmbulatoria(con,citaForm.getOrdenAmbulatoria(),institucion), institucion);
			//logger.info("INFORMACION CAPTURADA DE La orden ambulatoria=>"+infoSol);
			//logger.info("CODIGO DE LA ORDEN AMBULATORIA CONSULTADO=> "+Utilidades.obtenerCodigoOrdenAmbulatoria(con,citaForm.getOrdenAmbulatoria(),((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCodigoInstitucionInt()));
			
			/** Obtengo Diagnostico de la orden ambulatoria generada, para ser guardado junto con la información de la 
			 *  solicitud - Cambio según Versión 1.50 - Anexo Asignación de citas 13**/
			
			String codigoOrdenAmbulatoria = Utilidades.obtenerCodigoOrdenAmbulatoria(con, citaForm.getOrdenAmbulatoria(), institucion);
			DtoDiagnostico dtoDiagnostico = OrdenesAmbulatorias.consultarDiagnosticoOrdenAmbulatoria(con, Integer.parseInt(codigoOrdenAmbulatoria));
			citaForm.setDtoDiagnostico(dtoDiagnostico);
			/*******************************************************************************************************/ 
			
			
			if(!infoSol.trim().equals(""))
			{
				//logger.info("\n me voy por orden ambularia innfosol --> "+infoSol);
				String[] vectorInfoSol=infoSol.split(ConstantesBD.separadorSplit);
				usuario.cargarUsuarioBasico(con,vectorInfoSol[3]);
				//////////////////////////////////////////////////////////////////////////////////
				//modificacion por tarea 3730
				usuario.setCodigoCentroAtencion(Utilidades.convertirAEntero(vectorInfoSol[2]));
				//////////////////////////////////////////////////////////////////////////////////////////////////////////
				//logger.info("\n el centro de atencion es --> "+usuario.getCodigoCentroAtencion()+" login -->"+vectorInfoSol[3]);
			}
			else
			{
				usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			}
		}
		else
		{
			usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		}
		
		//***************SE VERIFICA SI SE DEBE VALIDAR LA VIGENCIA EN LA COMPROBACIÓN DE DERECHOS CAPITADOS *******************************				
		if(ValoresPorDefecto.getValoresDefectoComprobacionDerechosCapitacionObligatoria(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi) 
				&& !entroAutorizacionIngEven)
		{
			citaForm.setAutorizacionIngEvento(UtilidadValidacion.esPacienteCapitadoVigente(con,UtilidadFecha.getFechaActual(),paciente.getNumeroIdentificacionPersona(),paciente.getCodigoTipoIdentificacionPersona()));
			
			//Valida que exista el paciente dentro de la vigencia capitada
			if(!citaForm.getAutorizacionIngEvento().isActivo())
			{
				logger.info("retormo ingreso codigo");
				citaForm.getAutorizacionIngEvento().setCodigo("");
				return mapping.findForward("autoIngEvento");
			}
		}
		//************************************************************************************************************************************
		
		
		actionForward =validarPacienteAsignacion(mapping,request,paciente,usuario);
		if(actionForward != null)
			return actionForward;

		/**
		 * Validar concurrencia
		 * Si ya estï¿½ en proceso de distribucion, no debe dejar entrar
		 **/
		if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), usuario.getLoginUsuario()) )
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
		}
		/**
		 * Validar concurrencia
		 * Si ya estï¿½ en proceso de facturaciï¿½n, no debe dejar entrar
		 **/
		if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "" /*idSesion*/) )
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
		}
		try
		{
			ActionErrors	errores;
			boolean			esExcepcion;
			String			esPos;
			Boolean			validarCitaFecha;
			Cita			cita = new Cita();
			int				codigoCita;
			int				estadoCita;
			int				ocupacionMedica;
			int				servicio;
			int				sexo;
			Integer			aux;
			String			estadoLiquidacion;
			//String			numeroAutorizacion;
			String			numeroSolicitud;
			String			observacion;
			citaForm.setEstado("validarListadoAsignar");
			/* Asignar las citas solicitadas */
			errores = new ActionErrors();
			
			
			int tamanio = citaForm.getNumeroItemsSeleccionados();
			boolean excento=false;
			//Utilidades.imprimirMapa((HashMap)citaForm.getIm_agenda());
			logger.info("tamanio de items seleccionados=> "+tamanio);
			//Se iteran las citas a asignar
			HibernateUtil.beginTransaction();
			for(int i = 0; i<tamanio; i++)
			{
				if(!UtilidadTexto.getBoolean(citaForm.getItemSeleccionado("citaProcesada_"+i)+""))
				{
					validarCitaFecha=(Boolean)citaForm.getItemSeleccionado("validarCitaFecha_" + i);
					logger.info("----------->"+citaForm.getItemSeleccionado("codigoCita_" + i) );
					codigoCita=Utilidades.convertirAEntero(citaForm.getItemSeleccionado("codigoCita_" + i) +"");
					Integer ocupaInt=Utilidades.convertirAEntero(citaForm.getItemSeleccionado("ocupacionMedica_" + i)+"");
					if(ocupaInt==null || ocupaInt<0)
					{
						ocupacionMedica=-1;
					}
					else
					{
						ocupacionMedica=ocupaInt;
					}
					aux=Integer.parseInt(citaForm.getItemSeleccionado("codigoEstadoCita_" + i).toString());
					estadoCita=aux.intValue();
					
					estadoLiquidacion=citaForm.getItemSeleccionado("codigoEstadoLiquidacion_" + i).toString();
					
					logger.info("ESTADO DE LIQUIDACION:"+estadoLiquidacion);	
					//Solo realiza el proceso con citas que se han definido como liquidadas
					if(estadoLiquidacion.equals(ConstantesBD.codigoEstadoLiquidacionLiquidada))
					{
						
						//*************SE CARGAN LOS SERVICIOS DE LA CITA AL MAPA SERVICIOS********************************
						HashMap mapaServicios = new HashMap();
						int numServicios = Integer.parseInt(citaForm.getItemSeleccionado("numServicios_"+i).toString());
						int cont = 0;
						int band=0;
					
						logger.info("NUMERO DE SERVICIOS=> "+numServicios);
	//					*******************************	CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] *************************************// 					
								
						//verificar valor definido en el campo cantidad maxima de citas control post operatorio para el convenio seleccionado
						citaForm.setMensaje(new ResultadoBoolean(false));
								
								HashMap mapa= new HashMap();
									// se realiza el procesos para todas las reservas.
									for(int j=0;j<numServicios;j++)
									{
										//***********CAMBIO INCLUIDO POR LA TAREA 59745*********************
										//mapaServicios.put("codigoConvenio_"+cont, citaForm.getItemSeleccionado("convenio_"+i+"_"+j));
										mapaServicios.put("codigoConvenio_"+cont, citaForm.getConvenio());
										
	
										logger.info("im_agenda---->"+citaForm.getIm_agenda("convenio_"+i+"_"+j));
										logger.info("itemSel---->"+citaForm.getItemSeleccionado("convenio_"+i+"_"+j));
										if (UtilidadTexto.isNumber(citaForm.getIm_agenda().get("convenio_"+i+"_"+j)+""))
										{
											//validacion de convenio - si es de reserva (convenio reserva) - si no el de prioridad uno
											citaForm.setConvenio(Integer.parseInt(citaForm.getIm_agenda("convenio_"+i+"_"+j).toString()));
										}
										else
										{
											citaForm.setConvenio(paciente.getCodigoConvenio());
										}
										
										
										int cantCPO=UtilidadesConsultaExterna.consultarCantidadMaximaCitasControlPostOperatorioConvenio(con, citaForm.getConvenio());
										
										logger.info("\n\n\n\n [Convenio] "+citaForm.getConvenio()+" [CantidadMaximaCitasControlPostOperatorioConvenio]  "+cantCPO);
										
										if (cantCPO>0)
										{
											int diasCPO=UtilidadesConsultaExterna.consultarDiasControlPostOperatorioConvenio(con, citaForm.getConvenio());	
											logger.info("\n\n\n\n [Convenio] "+citaForm.getConvenio()+" [DiasControlPostOperatorioConvenio]  "+cantCPO);
											
											logger.info("i - "+i);
											logger.info("j - "+j);
											
											logger.info("MAP - "+citaForm.getIm_agenda());
										
											if (citaForm.getIm_agenda().containsKey("codigoEspecialidad_"+i+"_"+j) 
													&& (!citaForm.getIm_agenda().get("codigoEspecialidad_"+i+"_"+j).toString().equals("") 
													   || !citaForm.getIm_agenda().get("codigoEspecialidad_"+i+"_"+j).toString().isEmpty()))
											{
												mapa=UtilidadesConsultaExterna.validarControlPostOperatorio
												(con, 
														citaForm.getCodigoPaciente(), 
														citaForm.getIm_agenda().get("fecha_"+i).toString(), 
												cantCPO, 
												diasCPO, 
												Utilidades.convertirAEntero(citaForm.getIm_agenda().get("codigoEspecialidad_"+i+"_"+j).toString())
												);
							
											}
											else
											{
												logger.info("\n\n [NO HACE VALIDACIONES CONTROL POST OPERATORIO] \n\n");
											}	
											
											
										
										if(mapa.containsKey("codigopo_0"))
										{
											citaForm.getIm_agenda().put("solicitudpo_"+i, mapa.get("solicitud_"+0));
											citaForm.getIm_agenda().put("codigopo_"+i, mapa.get("codigopo_"+0));
											citaForm.setMensaje(new ResultadoBoolean(true," Paciente Control Post Operatorio "));
											band++;
										}
									else
										{
										citaForm.getIm_agenda().put("solicitudpo_"+i, null);
										citaForm.getIm_agenda().put("codigopo_"+i, null);
										}
										
										//Se excluyen los servicios eliminados
										if(citaForm.getItemSeleccionado("codigoServicio_"+i+"_"+j)!=null)
										{
											sexo=Integer.parseInt(citaForm.getItemSeleccionado("codigoSexo_"+i+"_"+j).toString());
											servicio=Integer.parseInt(citaForm.getItemSeleccionado("codigoServicio_" + i +"_"+j).toString());
											//numeroAutorizacion =citaForm.getItemSeleccionado("numeroAutorizacion_"+i+"_"+j)==null?"":citaForm.getItemSeleccionado("numeroAutorizacion_"+i+"_"+j).toString();
											numeroSolicitud = citaForm.getItemSeleccionado("numeroSolicitud_"+i+"_"+j)==null?"":citaForm.getItemSeleccionado("numeroSolicitud_"+i+"_"+j).toString();
											//numeroAutorizacion	= numeroAutorizacion.trim();
											esPos=citaForm.getItemSeleccionado("esPos_"+i+"_"+j).toString();
											observacion=citaForm.getItemSeleccionado("observaciones_"+i+"_"+j).toString();
											
											mapaServicios.put("codigoSexo_"+cont, sexo);
											mapaServicios.put("codigoServicio_"+cont, servicio);
											mapaServicios.put("codigoTipoServicio_"+cont, Utilidades.obtenerTipoServicio(con, servicio+""));
											mapaServicios.put("nombreServicio_"+cont, citaForm.getItemSeleccionado("nombreServicio_" + i +"_"+j));
											mapaServicios.put("codigoEspecialidad_"+cont, citaForm.getItemSeleccionado("codigoEspecialidad_" + i +"_"+j));
											mapaServicios.put("codigoCentroCosto_"+cont, citaForm.getItemSeleccionado("codigoCentroCosto_" + i +"_"+j));
											
											//mapaServicios.put("dtoRespAuto_"+cont,citaForm.getItemSeleccionado("dtoRespAuto_"+ i +"_"+j));
											
											mapaServicios.put("numeroSolicitud_"+cont, numeroSolicitud);
											mapaServicios.put("codigoAgenda_"+cont, citaForm.getItemSeleccionado("codigoAgenda_" + i));
											mapaServicios.put("codigoEstadoCita_"+cont, citaForm.getItemSeleccionado("codigoEstadoCita_" + i));
											mapaServicios.put("fechaCita_"+cont, citaForm.getItemSeleccionado("fecha_" + i));
											mapaServicios.put("horaInicioCita_"+cont, UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString()));
											mapaServicios.put("horaFinCita_"+cont, UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaFin_" + i).toString()));
											mapaServicios.put("esPos_"+cont, esPos);
											mapaServicios.put("observaciones_"+cont, observacion);
				//							*******************************	CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] *************************************//
											
											mapaServicios.put("solicitudpo_"+cont, citaForm.getItemSeleccionado("solicitudpo_" + i));
											mapaServicios.put("codigopo_"+cont, citaForm.getItemSeleccionado("codigopo_" + i));
											
				//							*******************************	FIN CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] *********************************//
										
											//////////////////////////////////////////////////////////////////////////////
											//adicionado por tarea 3437 xplanner
											mapaServicios.put("codigoServicioCita_"+cont, citaForm.getItemSeleccionado("codigoServicioCita_" + i +"_"+j));
											/////////////////////////////////////////////////////////////////////////////
											
											//***********CAMBIO INCLUIDO POR LA TAREA 59745*********************
											
											/* Determinar si el servicio es o no una excepciï¿½n */
											esExcepcion=UtilidadValidacion.esExcepcionServicio(con, citaForm.getCuentaPaciente(), servicio);
											mapaServicios.put("esExcepcion_"+cont, esExcepcion);
											cont++;
										}
									}
							else{
								
	//		******************************* FIN CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] **********************************//
										
								if(citaForm.getItemSeleccionado("codigoServicio_"+i+"_"+j)!=null)
										{
								
											sexo=Integer.parseInt(citaForm.getItemSeleccionado("codigoSexo_"+i+"_"+j).toString());
											servicio=Integer.parseInt(citaForm.getItemSeleccionado("codigoServicio_" + i +"_"+j).toString());
											//numeroAutorizacion =citaForm.getItemSeleccionado("numeroAutorizacion_"+i+"_"+j)==null?"":citaForm.getItemSeleccionado("numeroAutorizacion_"+i+"_"+j).toString();
											logger.info("VALOR DEL NUMERO DE AUTORIZACION RECIBIDO("+i+","+j+")=>"+citaForm.getItemSeleccionado("numeroAutorizacion_"+i+"_"+j));
											numeroSolicitud = citaForm.getItemSeleccionado("numeroSolicitud_"+i+"_"+j)==null?"":citaForm.getItemSeleccionado("numeroSolicitud_"+i+"_"+j).toString();
											//numeroAutorizacion	= numeroAutorizacion.trim();
											esPos=citaForm.getItemSeleccionado("esPos_"+i+"_"+j).toString();
											observacion=citaForm.getItemSeleccionado("observaciones_"+i+"_"+j).toString();
											
											mapaServicios.put("codigoSexo_"+cont, sexo);
											mapaServicios.put("codigoServicio_"+cont, servicio);
											mapaServicios.put("codigoTipoServicio_"+cont, Utilidades.obtenerTipoServicio(con, servicio+""));
											mapaServicios.put("nombreServicio_"+cont, citaForm.getItemSeleccionado("nombreServicio_" + i +"_"+j));
											mapaServicios.put("codigoEspecialidad_"+cont, citaForm.getItemSeleccionado("codigoEspecialidad_" + i +"_"+j));
											mapaServicios.put("codigoCentroCosto_"+cont, citaForm.getItemSeleccionado("codigoCentroCosto_" + i +"_"+j));
											
											//mapaServicios.put("dtoRespAuto__"+cont,citaForm.getItemSeleccionado("dtoRespAuto_"+ i +"_"+j));
											
											mapaServicios.put("numeroSolicitud_"+cont, numeroSolicitud);
											mapaServicios.put("codigoAgenda_"+cont, citaForm.getItemSeleccionado("codigoAgenda_" + i));
											mapaServicios.put("codigoEstadoCita_"+cont, citaForm.getItemSeleccionado("codigoEstadoCita_" + i));
											mapaServicios.put("fechaCita_"+cont, citaForm.getItemSeleccionado("fecha_" + i));
											mapaServicios.put("horaInicioCita_"+cont, UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString()));
											mapaServicios.put("horaFinCita_"+cont, UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaFin_" + i).toString()));
											mapaServicios.put("esPos_"+cont, esPos);
											mapaServicios.put("observaciones_"+cont, observacion);
				//							*******************************	CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] *************************************//
											
											mapaServicios.put("solicitudpo_"+cont, null);
											mapaServicios.put("codigopo_"+cont, null);
											
				//							*******************************	FIN CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] *********************************//
										
											//////////////////////////////////////////////////////////////////////////////
											//adicionado por tarea 3437 xplanner
											mapaServicios.put("codigoServicioCita_"+cont, citaForm.getItemSeleccionado("codigoServicioCita_" + i +"_"+j));
											/////////////////////////////////////////////////////////////////////////////
											
											//***********CAMBIO INCLUIDO POR LA TAREA 59745*********************
											mapaServicios.put("codigoConvenio_"+cont, citaForm.getItemSeleccionado("convenio_"+i+"_"+j)+"");
											//***********CAMBIO INCLUIDO POR LA TAREA 59745*********************
											
											/* Determinar si el servicio es o no una excepciï¿½n */
											esExcepcion=UtilidadValidacion.esExcepcionServicio(con, citaForm.getCuentaPaciente(), servicio);
											mapaServicios.put("esExcepcion_"+cont, esExcepcion);
											cont++;
										}
									}
								}
								
						mapaServicios.put("numRegistros", cont);
						
						//****************************************************************************************
						
						if(band==numServicios)
						{
							if (UtilidadesFacturacion.puedoCerrarIngreso(con,paciente.getCodigoIngreso()))
							{	
							citaForm.setCierreIngresoPostOperatorio(true);
							}
						}
						else
						{
							citaForm.setCierreIngresoPostOperatorio(false);
						}
		
						//Si hay servicios para la cita entonces se continua 
						if(Integer.parseInt(mapaServicios.get("numRegistros").toString())>0)
						{
							cita = new Cita();
							cita.setCodigo(codigoCita);
							
							//*************SE REALIZAN VALIDACIONES SOBRE LOS SERVICIOS DE LA CITA******************
							for(int j=0;j<Integer.parseInt(mapaServicios.get("numRegistros").toString());j++)
							{
								//1) Actualizacion de las observaciones
								if(cita.actualizarObservacion(con, mapaServicios.get("observaciones_"+j).toString(), Integer.parseInt(mapaServicios.get("codigoServicio_"+j).toString()),usuario.getLoginUsuario())<0)
									errores.add("actualizacionObs_"+i+"_"+j, 
										new ActionMessage(
											"error.cita.noActualizaObservacion",
											mapaServicios.get("nombreServicio_"+j),
											citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i),
											citaForm.getItemSeleccionado("fecha_" + i),
											citaForm.getItemSeleccionado("horaInicio_" + i)
										)
									);
								
								//2) Se valida sexo del servicio: que corresponda al sexo del paciente
								sexo = Integer.parseInt(mapaServicios.get("codigoSexo_"+j).toString());
								/*
								if(sexo != ConstantesBD.codigoSexoTodos && sexo != citaForm.getCodigoSexoPaciente())
									errores.add("codigoSexoPaciente",
										new ActionMessage(
											"error.cita.sexoPaciente",
											mapaServicios.get("nombreServicio_"+j),
											citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i),
											citaForm.getItemSeleccionado("fecha_" + i)+"-"+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString()),
											"asignada")
									);
								*/
								//3) Validacion del nï¿½mero de autorizacion
								/*
								El servicio seleccionado es pos pero es una excepciï¿½n en el convenio de la
								cuenta del paciente, o no es pos y no es una exceciï¿½n en convenio de la
								cuenta del paciente, y entonces no estï¿½ cubierto por el convenio. Requiere
								entonces el numero de autorizaciï¿½n
								 */
								InfoResponsableCobertura infoResponsableCobertura= Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), Integer.parseInt(mapaServicios.get("codigoServicio_"+j).toString()), usuario.getCodigoInstitucionInt(), citaForm.isSolPYP(),"" /*subCuentaCoberturaOPCIONAL*/);
								
								//FIXME Se obtiene el convenio de cada servicio al que se le evalua la cobertura para generar autorizacion de Capitacion 
								mapaServicios.put("codConvenioServicio_"+j, infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo());
								citaForm.setInfoResponsableCobertura(infoResponsableCobertura);
								
								DtoRespAutorizacion dtoRespAuto = new DtoRespAutorizacion();
								dtoRespAuto = (DtoRespAutorizacion) citaForm.getItemSeleccionado("dtoRespAuto_"+i+"_"+j);
								
								if(infoResponsableCobertura.getInfoCobertura().getRequiereAutorizacion()&& (dtoRespAuto==null||!dtoRespAuto.isEstadoIngresoResp()))
								{
									if(!Cita.consultarAutorizacionPaciente(con, paciente.getCodigoIngreso(), paciente.getCodigoConvenio())){
										
									errores.add("descripcion",
										new ActionMessage(
											"error.cita.numeroAutorizacion",
											mapaServicios.get("nombreServicio_"+j),
											citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i),
											citaForm.getItemSeleccionado("fecha_" + i)+"-"+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString()
											)
										)
									);
									}
									/*
									if(estadoCita==ConstantesBD.codigoEstadoCitaReservada)
										citaForm.setItemSeleccionado("codigoEstadoLiquidacion_" + i,ConstantesBD.codigoEstadoLiquidacionSinLiquidar);
									*/
								}
								
								/*
								 * Validar si la unidad de agenda tiene parametrizada especialidad
								 */
								if(UtilidadTexto.isEmpty(mapaServicios.get("codigoEspecialidad_"+j)+""))
									errores.add("descripcion",
											new ActionMessage(
												"error.cita.unidadAgendaSinEspecialidad",
												citaForm.getItemSeleccionado("nombreUnidadConsulta_" + j))
									);
								
							}
							//******************************************************************************************
							
							
		
							//Si no hay errores se prosigue a generar solicitudes
							if(errores.isEmpty())
							{
								if(i>0)
								{
									UtilidadBD.iniciarTransaccion(con);
								}
								logger.info("\n\n\n\n********************PARTE DE LA GENERACION DE LA CITA**************************************\n\n\n\n");
								//**********INSERCION DE CITA CON SU DETALLE DE SERVICIOS**************************
								cita.setPrioridad(citaForm.getItemSeleccionado("prioritaria_" + i).toString());
								/*Solo aplica si la cita no ha sido programada*/
								if(estadoCita == ConstantesBD.codigoEstadoCitaAProgramar)
								{
									 /* Iniciar la cita */
									cita = new Cita();
			
									/* Establecer los datos bï¿½sicos de la cita */
									aux = Utilidades.convertirAEntero(citaForm.getItemSeleccionado("codigoAgenda_" + i)+"");
			
									cita.setCodigoAgenda(aux.intValue() );
									cita.setCodigoPaciente(citaForm.getCodigoPaciente() );
									cita.setPrioridad(citaForm.getItemSeleccionado("prioritaria_" + i).toString());
									
									//-------------------------------------------------
									// atributos para el ingreso de autorizacion de cita
									if(citaForm.getItemSeleccionado("requiereAuto_" + i)!=null)
										cita.setCitasIncumpl(citaForm.getItemSeleccionado("requiereAuto_" + i).toString());
									else
										cita.setCitasIncumpl("");
									if(citaForm.getItemSeleccionado("motivoAuto_" + i)!=null)
										cita.setMotivoAutorizacionCita(citaForm.getItemSeleccionado("motivoAuto_" + i).toString());
									else
										cita.setMotivoAutorizacionCita("");
									if(citaForm.getItemSeleccionado("usuarioAuto_" + i)!=null)
										cita.setUsuarioAutoriza(citaForm.getItemSeleccionado("usuarioAuto_" + i).toString());
									else
										cita.setUsuarioAutoriza("");
									//-------------------------------------------------
									
									aux =Utilidades.convertirAEntero(citaForm.getItemSeleccionado("codigoUnidadConsulta_" + i)+"");
			
									cita.setCodigoUnidadConsulta(aux.intValue() );
									cita.setCodigoUsuario(citaForm.getCodigoUsuario() );
									cita.setMapaServicios(mapaServicios);
									
									cita.setTelefono(citaForm.getTelefono());
									cita.setCelular(citaForm.getCelular());
									cita.setOtrosTelefonos(citaForm.getOtrosTelefonos());
									
									//---Se verifica si hay cita programada para la misma fecha y hora
									boolean hayCitaIgualFechaHora=cita.validarReservaCitaFechaHora(con);
									logger.info("\n\n\n\n**********************************************************");
									logger.info("hayCitaIgualFechaHora: "+hayCitaIgualFechaHora);
									logger.info("\n\n\n\n**********************************************************");
									//--Se muestra la confirmaciï¿½n si el paciente tiene citas en la misma fecha, pero no a la mismo fecha y hora
									if(validarCitaFecha.booleanValue() && cita.validarReservaCitaFecha(con) && !hayCitaIgualFechaHora)
									{
										/* Cerrar la conexion a base de datos */
										cerrarConexion(con);
			
										/* Indicar que item seleccionado se debe confirmar */
										citaForm.setIndiceItemSeleccionado(i);
			
										/* Confirmar la reserva de la cita */
										HibernateUtil.endTransaction();
										return mapping.findForward("confirmarCita");
									}
									
									//----------- Inicio Fecha Solicitada por el paciente -------------
									/*Date fechaSol = new Date();
									SimpleDateFormat	sdfFecha = new SimpleDateFormat();
									//Validaciones para la fecha solicitud si se ha ingresado por cada cita individualmente
									try
									{
										//Obtener la fecha solicitada por el paciente
										if(!citaForm.getItemSeleccionado("fecha_" + i).toString().equals(""))
											fechaSol = sdfFecha.parse(citaForm.getItemSeleccionado("fecha_" + i).toString());
									}
									catch(ParseException e)
									{
										errores.add("fechaSolicitada", new ActionMessage("errors.formatoFechaInvalido", "Fecha Solicitada de la Cita"));
										fechaSol = null;
									}
									try
									{
										//si la fecha es valida se continua validando
										if (fechaSol!=null)
											if (fechaSol.compareTo(sdfFecha.parse(UtilidadFecha.getFechaActual()))<0) 
												errores.add("fechaAsignarMenorActual", new ActionMessage("errors.fechaAnteriorIgualActual", "solicitada", "actual"));
									}	
									catch(ParseException lpe_fechaInicio)
									{
										System.out.print(lpe_fechaInicio);
									}*/
									//Se verifica la fecha solicitada por el paciente
									if (citaForm.getFechaSolicitada().equals("")){
										if (citaForm.getItemSeleccionado("fecha_" + i).toString().equals(""))
											citaForm.setFechaSolicitada(citaForm.getItemSeleccionado("fecha_" + i).toString());
										else
											citaForm.setFechaSolicitada(citaForm.getItemSeleccionado("fechaSolicitada_" + i).toString());
									}
									cita.setIs_fechaSolicitada(citaForm.getFechaSolicitada());
									//----------- Fin Fecha Solicitada por el paciente -----------------
									
									
									//SE INSERTA LA CITA
									cita.asignarCita(con,ConstantesBD.continuarTransaccion);
									
									//Se verifica si hay error
									if(cita.getCodigo()<=0)
									{
										if(cita.getCodigo()==-5)
										{
											errores.add("cita asignada concurrencia", new ActionMessage("errores.cita.citaYaAsignadaOReservada"));
										}
										else
										{
											citaForm.setItemSeleccionado("codigoEstadoLiquidacion_" + i,ConstantesBD.codigoEstadoLiquidacionSinLiquidar);
											errores.add("Error al asignar la cita",
												new ActionMessage(
													"errors.noSeGraboInformacion.mismaHora",
													"DE LA CITA DE "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" PARA LA FECHA/HORA "+
													citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString()))
												);
											
										}
									}

									
								}
								//La cita ya existe osea que se intenta cambiar el indicador
								else if(!cita.actualizarPrioridadCita(con))
								{
									cita.setCodigo(ConstantesBD.codigoNuncaValido);
									errores.add("",new ActionMessage("errors.problemasGenericos","actualizando la prioridad de la cita  de "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" para la fecha/hora "+
											citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString())));
								}
								else
								{
									/* Establecer los datos bï¿½sicos de la cita */
									aux = Utilidades.convertirAEntero(citaForm.getItemSeleccionado("codigoAgenda_" + i)+"");
			
									cita.setCodigoAgenda(aux.intValue() );
								}
								//*********************************************************************************
								
								
								if(cita.getCodigo()>0)
								{
									
									//Se iteran de nuevo los servicios de cada cita para generar solicitudes
									//************************GENERACION DE SOLICITUDES*********************************
									for(int j=0;j<Integer.parseInt(mapaServicios.get("numRegistros").toString());j++)
									{
										int tipoSolicitudCita=ConstantesBD.codigoNuncaValido;
										numeroSolicitud = mapaServicios.get("numeroSolicitud_"+j).toString();
										
										//Se desea liquidar y aun no existe la solicitud, pero ya existe cita
										if(
												((estadoCita == ConstantesBD.codigoEstadoCitaAsignada||estadoCita == ConstantesBD.codigoEstadoCitaReprogramada || estadoCita == ConstantesBD.codigoEstadoCitaReservada)&& 
												numeroSolicitud.equals("")	&&  
												estadoLiquidacion.equals(ConstantesBD.codigoEstadoLiquidacionLiquidada))
												||
												estadoCita == ConstantesBD.codigoEstadoCitaAProgramar)
										{
											
											
											////////////////////////////////////////////////////////////////////////////////////////
											/// adicionado por tarea 3437 xplanner 2008
											//verifico si la cita tiene estado reservado, luego si el servicio se encuentra en la tabla
											//servicios_cita; si no esta se inserta.
											if (estadoCita== ConstantesBD.codigoEstadoCitaReservada)
												if (Cita.existeServicioEnCita(con,cita.getCodigo()+"",mapaServicios.get("codigoServicio_"+j)+"").equals(ConstantesBD.acronimoNo))
														cita.insertarServicioCita(con, cita.getCodigo()+"", mapaServicios.get("codigoServicio_"+j)+"", mapaServicios.get("codigoCentroCosto_"+j)+"", mapaServicios.get("codigoEspecialidad_"+j)+"",  mapaServicios.get("observaciones_"+j)+"", usuario.getLoginUsuario(), mapaServicios.get("fechaCita_"+j)+"", mapaServicios.get("horaInicioCita_"+j)+"", mapaServicios.get("horaFinCita_"+j)+"", mapaServicios.get("codigoEstadoCita_"+j)+"", mapaServicios.get("codigoAgenda_"+j)+"");
											//////////////////////////////////////////////////////////////////////////////////////////
											
											//Se debe verificar si el cargo debe quedar excento. 
											Utilidades.imprimirMapa(mapaServicios);
											if ((mapaServicios.get("solicitudpo_"+j)+"").equals("null") || (mapaServicios.get("codigopo_"+j)+"").equals("null"))
												excento=false;
											else
												excento=true;
											
											/**
											 * victor gomez
											 */
											//***************************************
											DtoRespAutorizacion dtoRespAuto = new DtoRespAutorizacion();
											try
											{
												if(citaForm.getItemSeleccionado("dtoRespAuto_"+i+"_"+j)!=null)
												{
													dtoRespAuto = (DtoRespAutorizacion) citaForm.getItemSeleccionado("dtoRespAuto_"+i+"_"+j);
												}
											}
											catch(Exception e)
											{
												dtoRespAuto.setEstadoIngresoResp(false);
												logger.info("no exite una instancia de la respuesta asociada al servicio");
											}
											//***************************************
											
											//Cambio por tarea 146930 - Obtengo el codigo del medico asociado a la cita
											//Agrego el codigo del medico 
											cita.setCodigoMedicoXAgenda(UtilidadesConsultaExterna.obtenerCodigoMedicoAgendaXCita(con, cita.getCodigo()));
											logger.info("CODIGO DEL MEDICO DE LA AGENDA----->"+cita.getCodigoMedicoXAgenda());
											
											boolean validarEstadoCita=true;
											//SERVICIOS CONSULTA ---------------------------------------------------------
											boolean validarReservaOrdenAmb=(!citaForm.isIndicativoOrdenAmbulatoria());
											if(validarReservaOrdenAmb)
											{
												citaForm.setOrdenAmbulatoria(UtilidadesConsultaExterna.esReservaOrdenAmbulatoria(con,codigoCita));
												if(!UtilidadTexto.isEmpty(citaForm.getOrdenAmbulatoria()))
												{
													citaForm.setIndicativoOrdenAmbulatoria(true);
													citaForm.setSolPYP(Utilidades.esOrdenAmbulatoriaPYP(con,citaForm.getOrdenAmbulatoria(),usuario.getCodigoInstitucionInt()));
													citaForm.setServicioAmbulatorioPostular(mapaServicios.get("codigoServicio_"+j)+"");
													
													int institucion= ((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCodigoInstitucionInt();
													@SuppressWarnings("unused")
													String infoSol=OrdenesAmbulatorias.obtenerInfoServicioProcOrdenAmbulatoria(con,Utilidades.obtenerCodigoOrdenAmbulatoria(con,citaForm.getOrdenAmbulatoria(),institucion), institucion);
													//logger.info("INFORMACION CAPTURADA DE La orden ambulatoria=>"+infoSol);
													//logger.info("CODIGO DE LA ORDEN AMBULATORIA CONSULTADO=> "+Utilidades.obtenerCodigoOrdenAmbulatoria(con,citaForm.getOrdenAmbulatoria(),((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCodigoInstitucionInt()));
													/*
													if(!infoSol.trim().equals(""))
													{
														//logger.info("\n me voy por orden ambularia innfosol --> "+infoSol);
														String[] vectorInfoSol=infoSol.split(ConstantesBD.separadorSplit);
														usuario.cargarUsuarioBasico(con,vectorInfoSol[3]);
														//////////////////////////////////////////////////////////////////////////////////
														//modificacion por tarea 3730
														usuario.setCodigoCentroAtencion(Utilidades.convertirAEntero(vectorInfoSol[2]));
														//////////////////////////////////////////////////////////////////////////////////////////////////////////
														//logger.info("\n el centro de atencion es --> "+usuario.getCodigoCentroAtencion()+" login -->"+vectorInfoSol[3]);
													}
													else
													{
														usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
													}
													*/
													validarEstadoCita=false;
												}
												else
												{
													//usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
												}
											}
											else
											{
												//usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
											}
											
											
											
											if(mapaServicios.get("codigoTipoServicio_"+j).toString().equals(ConstantesBD.codigoServicioInterconsulta+"")){
												//SERVICIOS INTERCONSULTA --------------------------------------------------------
												errores = generacionSolicitudConsulta(con,citaForm,j,mapaServicios,errores,paciente,usuario,request,cita,ocupacionMedica,estadoCita,i,excento,dtoRespAuto,validarEstadoCita);
												tipoSolicitudCita = ConstantesBD.codigoTipoSolicitudCita;
											}else if(mapaServicios.get("codigoTipoServicio_"+j).toString().equals(ConstantesBD.codigoServicioProcedimiento+"")){
											//SERVICIOS PROCEDIMIENTOS --------------------------------------------------------
												errores = generacionSolicitudProcedimiento(con,citaForm,j,mapaServicios,errores,paciente,usuario,request,cita,ocupacionMedica,estadoCita,i,dtoRespAuto,validarEstadoCita);
												tipoSolicitudCita = ConstantesBD.codigoTipoSolicitudProcedimiento;
											}else if(mapaServicios.get("codigoTipoServicio_"+j).toString().equals(ConstantesBD.codigoServicioNoCruentos+"")){
											//SERVICIOS NO CRUENTOS --------------------------------------------------------
												errores = generacionSolicitudCirugia(con,citaForm,j,mapaServicios,errores,paciente,usuario,request,cita,ocupacionMedica,estadoCita,i,validarEstadoCita);
												tipoSolicitudCita = ConstantesBD.codigoTipoSolicitudCirugia;
											}
											
											if(validarReservaOrdenAmb)
											{
												citaForm.setOrdenAmbulatoria("");
												citaForm.setIndicativoOrdenAmbulatoria(false);
												citaForm.setSolPYP(false);
												citaForm.setServicioAmbulatorioPostular("");
												usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
											}
											
										}
										logger.info("\n\nNUMERO SOLICITUD>>>>>>>>"+citaForm.getNumeroSolicitudCita()+"\nSERVICIO>>>>>>>>>"+Integer.parseInt(mapaServicios.get("codigoServicio_"+j).toString())+"\nNUM SOL DEL MAPA>>>>>>>"+mapaServicios.get("numeroSolicitud_"+j).toString()+"\nï¿½errores vacios? "+errores.isEmpty()+"\n");
										logger.info("\n\n\n [INICIO VALIDACION JUSTIFICACION DE SERVICIOS NO POS] \n\n\n");
										//////////////////////////
										// Validacion Convenio y Servicio No Pos, para luego insertar Justificacion
										// Pendiente del Servicio No Pos, Carga Mapa de Mensaje de Advertencia de
										// Justificacion Pendiente de cada Servicio
										//////////////////////////
										
										//Se cambio este método por la Tarea 59745. La cual sustenta la adición del campo codigoConvenio
																			
										if(UtilidadJustificacionPendienteArtServ.validarNOPOS(
												con,
												Utilidades.convertirAEntero(mapaServicios.get("numeroSolicitud_"+j).toString()), 
												Utilidades.convertirAEntero(mapaServicios.get("codigoServicio_"+j).toString()), 
												false, 
												false, 
												Utilidades.convertirAEntero(mapaServicios.get("codigoConvenio_"+j).toString())))
										{
											double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, Utilidades.convertirAEntero(mapaServicios.get("codigoServicio_"+j).toString()), Utilidades.convertirAEntero(mapaServicios.get("numeroSolicitud_"+j).toString()), Utilidades.convertirAEntero(mapaServicios.get("codigoConvenio_"+j).toString()), false);
											
											//logger.info("NUMERO ENTRO 333333333");
											if(UtilidadJustificacionPendienteArtServ.insertarJusNP(con, Integer.parseInt(mapaServicios.get("numeroSolicitud_"+j).toString()), Integer.parseInt(mapaServicios.get("codigoServicio_"+j).toString()), 1, usuario.getLoginUsuario(), false, false, Utilidades.convertirAEntero(subcuenta+""),""))
											{
												//logger.info("NUMERO ENTRO 44444441");
												citaForm.setJustificacionNoPosMap("mensaje_0", "SERVICIO ["+mapaServicios.get("codigoServicio_"+j)+"] DE NATURALEZA NO POS REQUIERE DE JUSTIFICACIï¿½N NO POS.");
											}
										}
										///////////////////////////
										//logger.info("NUMERO ENTRO 555555555");
										
										/** Se almacenan los números de solicitudes generadas que se deben validar para generar la autorización de capitación**/
										/** @author Diana Carolina G **/
										if(citaForm.getNumeroSolicitudCita()>0){
											dtoAdministrarSolicitudesAutorizar=new DTOAdministrarSolicitudesAutorizar();
											dtoAdministrarSolicitudesAutorizar.setNumeroSolicitudCitaAutorizar(Utilidades.convertirAEntero(mapaServicios.get("numeroSolicitud_"+j)+""));
											dtoAdministrarSolicitudesAutorizar.setConsecutivoCitaAutorizar(citaForm.getConsecutivoCita()+"");
											dtoAdministrarSolicitudesAutorizar.setCodigoServicioCitaAutorizar(Integer.parseInt(mapaServicios.get("codigoServicio_"+j).toString()));
											dtoAdministrarSolicitudesAutorizar.setNombreServicioCitaAutorizar(mapaServicios.get("nombreServicio_"+j).toString());
											
											/*
											 * En la asignación de citas solo existe la prioridad de la cita no un indicativo de urgente
											 * por tanto cuando se autoriza el indicativo urgente siempre va en NO. Se revisa el DCU 13
											 * el cual indica que el campo urgente siempre se debe guardar como NO.
											 * MT 5952
											 */
											dtoAdministrarSolicitudesAutorizar.setUrgente(ConstantesBD.acronimoNoChar);
											//dtoAdministrarSolicitudesAutorizar.setUrgente(cita.getPrioridad().charAt(0));

											dtoAdministrarSolicitudesAutorizar.setCentroCostoSolicitado(cita.getCodigoCentroCostoSolicitado());
											dtoAdministrarSolicitudesAutorizar.setCodigoConvenioServicioAutorizar(mapaServicios.get("codConvenioServicio_"+j)+"");
											dtoAdministrarSolicitudesAutorizar.setTipoSolicitudCita(tipoSolicitudCita);
											listaDtoAdministrarSolicitudesAutorizar.add(dtoAdministrarSolicitudesAutorizar);
											
										}
										
									}
									//**********************************************************************************
								}
								
								if(errores.isEmpty())
								{
									citaForm.setItemSeleccionado("citaProcesada_"+i,ConstantesBD.acronimoSi);
									UtilidadBD.finalizarTransaccion(con);
									
									//Se captura la excepcion para no bloquear el flujo
									try{
										//FIXME VALIDACIONES PARA GENERACION DE AUTORIZACION O ASOCIACION CON LA/LAS NUEVAS SOLICITUDES
										cargarInfoVerificarGeneracionAutorizacion(con, citaForm, usuario, paciente,
												listaDtoAdministrarSolicitudesAutorizar, errores);
									}  catch (IPSException e) {
										citaForm.setErrorGeneracionAutorizacion(true);
										Log4JManager.error(e);
										ActionMessages mensajeError = new ActionMessages();
										mensajeError.add("", new ActionMessage(String.valueOf(e.getErrorCode())));
										saveErrors(request, mensajeError);
									}
								}
								else
									UtilidadBD.abortarTransaccion(con);
									
							} //Fin validacion errores empty
						} // Fin if num registros servicios
					} //Fin if cita liquidada
				} //Fin if cita procesada
			} //Fin for de las citas
			
			if(!errores.isEmpty())
									{
				//Se valida para que muestre el resumen de la cita, si es error de la autorizacion
				//debe continuar el flujo actual
				if(citaForm.isErrorGeneracionAutorizacion()){
				citaForm.setNumeroCitasTemporal(0);
				}
				saveErrors(request, errores);
			}
			else
			{
				//*******************************************************************************
             	//Inserta el log autorizacion en caso de que se hubiere pedido
             	if(ValoresPorDefecto.getValoresDefectoComprobacionDerechosCapitacionObligatoria(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi)
             			&& !citaForm.getAutorizacionIngEvento().getCodigo().equals("") 
             				&& !citaForm.getAutorizacionIngEvento().isActivo())
             	{
             		if(UtilidadesManejoPaciente.insertarLogAutorizacionIngresoEvento(
             				con,
             				citaForm.getAutorizacionIngEvento().getCodigo()+"",
             				paciente.getCodigoPersona()+"",
             				paciente.getNumeroIdentificacionPersona()+"",
             				paciente.getCodigoTipoIdentificacionPersona(),
             				usuario,
             				46+""))
             			logger.info("\n\nINSERTO LOG DE AUTORIZACION INGRESO EVENTO");
             		else
             			logger.info("\n\n NO INSERTO LOG DE AUTORIZACION INGRESO EVENTO");	                 			
             	}
             	//*******************************************************************************
			}
			
			/* Cerrar la conexion a base de datos */
			UtilidadBD.closeConnection(con);
			HibernateUtil.endTransaction();
			if (citaForm.getNumeroCitasTemporal()>0)
			{
				citaForm.setNumeroCitas(citaForm.getNumeroCitasTemporal());
				//request.setAttribute("numeroCitas", "" + citaForm.getNumeroCitasTemporal()); 
				//Antes de salir siempre las limpio
			  	citaForm.setNumeroCitasTemporal(0);
			  	citaForm.setCodigosCitas(new int[0]); //se limpian los codigos almacenados
			  	
				return this.resumenImpresionVariasCitas(mapping, request, "asignar",citaForm);
			}
			else
			{
				return mapping.findForward("cita");
			}
		}
		catch(SQLException lse_e)
		{
			/* Cerrar la conexion a base de datos */
			cerrarConexion(con);
			HibernateUtil.abortTransaction();
			if(logger.isDebugEnabled() )
				logger.debug("No se realizar el proceso de asinaciï¿½n de citas");

			logger.error("ERROR Asignar cita ",lse_e);
			return error(mapping, request, "errors.problemasBd", true);
		}
	}

	/**
	 * Mï¿½todo implementado para generar la solicitud de cirugia
	 * @param con
	 * @param citaForm
	 * @param pos
	 * @param mapaServicios
	 * @param errores
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @param cita
	 * @param ocupacionMedica
	 * @param estadoCita
	 * @param i
	 * @param validarEstadoCita 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ActionErrors generacionSolicitudCirugia(
													Connection con, 
													CitaForm citaForm, 
													int pos, 
													HashMap mapaServicios, 
													ActionErrors errores, 
													PersonaBasica paciente, 
													UsuarioBasico usuario, 
													HttpServletRequest request, 
													Cita cita, 
													int ocupacionMedica, 
													int estadoCita, 
													int i, boolean validarEstadoCita) throws IPSException 
	{
		SolicitudesCx mundoSolCx= new SolicitudesCx();
		
		//String numeroAutorizacion = mapaServicios.get("numeroAutorizacion_"+pos).toString();
		int codigoServicio = Integer.parseInt(mapaServicios.get("codigoServicio_"+pos).toString());
		int codigoCentroCosto = Integer.parseInt(mapaServicios.get("codigoCentroCosto_"+pos).toString());
		int codigoEspecialidad = Integer.parseInt(mapaServicios.get("codigoEspecialidad_"+pos).toString());
		
		String observaciones = mapaServicios.get("observaciones_"+pos).toString();
		int codigoPeticion = 0, numeroSolicitud=0, resp1=0;
		boolean resp0 = false;
		logger.info("\n\n\n\n********************PARTE DE LA GENERACION DE LA solicitud CONSULTA**************************************\n\n\n\n");
		
		//*************SE INSERTA PRIMERO LA PETICION QX ********************************************
		//Datos del encabezado de la peticion
        HashMap peticionEncabezadoMap= new HashMap();
        peticionEncabezadoMap.put("tipoPaciente",   UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(con, paciente.getCodigoCuenta()+"").getAcronimo());
        peticionEncabezadoMap.put("fechaPeticion", UtilidadFecha.getFechaActual(con));
        peticionEncabezadoMap.put("horaPeticion", UtilidadFecha.getHoraActual(con));
        peticionEncabezadoMap.put("duracion", "");
        peticionEncabezadoMap.put("solicitante", usuario.getCodigoPersona()+"");
        peticionEncabezadoMap.put("fechaEstimada", "");
        peticionEncabezadoMap.put("requiereUci", ConstantesBD.acronimoNo);
        peticionEncabezadoMap.put("programable",ConstantesBD.acronimoNo);
        
        //Datos del servicio
        HashMap serviciosPeticionMap=new HashMap();
        serviciosPeticionMap.put("numeroFilasMapaServicios", "1");
        serviciosPeticionMap.put("fueEliminadoServicio_0", "false");
        serviciosPeticionMap.put("codigoServicio_0", codigoServicio);
        serviciosPeticionMap.put("codigoEspecialidad_0", ""); //va vaï¿½o
        serviciosPeticionMap.put("codigoTipoCirugia_0", "-1"); //no se ingresa informacion
        serviciosPeticionMap.put("numeroServicio_0", "1");
        serviciosPeticionMap.put("observaciones_0", observaciones);
        
        //Datos de los artï¿½culos
        HashMap articulosPeticionMap= new HashMap();
        articulosPeticionMap.put("numeroMateriales", "0");
        articulosPeticionMap.put("numeroOtrosMateriales", "0");
        
        //Datos de los porfesionales
        HashMap profesionalesPeticionMap= new HashMap();
        profesionalesPeticionMap.put("numeroProfesionales", "0");
        
        Peticion mundoPeticion= new Peticion();
        int codigoPeticionYNumeroInserciones[]= mundoPeticion.insertar(
        	con, 
        	peticionEncabezadoMap, 
        	serviciosPeticionMap, 
        	profesionalesPeticionMap, 
        	articulosPeticionMap, 
        	paciente.getCodigoPersona(), 
        	ConstantesBD.codigoNuncaValido,
        	usuario, true, false);
        
        logger.info("resultado insercion peticion=> Nï¿½ inserciones: "+codigoPeticionYNumeroInserciones[0]+", peticion: "+codigoPeticionYNumeroInserciones[1]);
        
        //Se verifica nï¿½mero de inserciones
        if(codigoPeticionYNumeroInserciones[0]<1)
        	errores.add("",new ActionMessage("errors.noSeGraboInformacion",
        		"DE LA PETICION Qx PARA EL SERVICIO "+mapaServicios.get("nombreServicio_"+pos)+" "+
        		"DE LA CITA DE "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" PARA LA FECHA/HORA "+
				citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString())
        		));
        else
        	codigoPeticion = codigoPeticionYNumeroInserciones[1];
		//*******************************************************************************************
        
        //*******************SE INSERTA UNA SOLICITUD Bï¿½SICA*******************************************
        Solicitud objectSolicitud= new Solicitud();
        
        objectSolicitud.clean();
        objectSolicitud.setFechaSolicitud(UtilidadFecha.getFechaActual(con));
        objectSolicitud.setHoraSolicitud(UtilidadFecha.getHoraActual(con));
        objectSolicitud.setTipoSolicitud(new InfoDatosInt(ConstantesBD.codigoTipoSolicitudCirugia));
        //objectSolicitud.setNumeroAutorizacion(numeroAutorizacion);
        objectSolicitud.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaNinguna));
        objectSolicitud.setOcupacionSolicitado(new InfoDatosInt(ocupacionMedica));
        objectSolicitud.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea()));
        objectSolicitud.setCodigoMedicoSolicitante(ConstantesBD.codigoNuncaValido);
        
        objectSolicitud.setCentroCostoSolicitado(new InfoDatosInt(codigoCentroCosto));
        objectSolicitud.setCodigoCuenta(paciente.getCodigoCuenta());
        
        objectSolicitud.setCobrable(true);
        
        objectSolicitud.setVaAEpicrisis(false);
        objectSolicitud.setUrgente(false);
        objectSolicitud.setEstadoHistoriaClinica(new InfoDatosInt(ConstantesBD.codigoEstadoHCSolicitada));
        objectSolicitud.setSolPYP(citaForm.isSolPYP());
        objectSolicitud.setTieneCita(true);
        //Campo se actualiza despues de generado el cargo
        objectSolicitud.setLiquidarAsocio(ConstantesBD.acronimoSi);
        
		//Cambio por tarea 146930
        //Agrego el codigo del medico que responde 
        objectSolicitud.setCodigoMedicoResponde(cita.getCodigoMedicoXAgenda());
        
        objectSolicitud.setEspecialidadSolicitadaOrdAmbulatorias(codigoEspecialidad);
        
        try
        {
            numeroSolicitud=objectSolicitud.insertarSolicitudGeneralTransaccional(con, ConstantesBD.continuarTransaccion);
            
            citaForm.setNumeroSolicitudCita(numeroSolicitud);
			citaForm.setConsecutivoCita(objectSolicitud.getConsecutivoOrdenesMedicas());
        }
        catch(SQLException sqle)
        {
            logger.warn("Error al generar la solicitud basica de cirugï¿½as: "+sqle);
            errores.add("",new ActionMessage("errors.noSeGraboInformacion",
            		"DE LA SOLICITUD PARA EL SERVICIO "+mapaServicios.get("nombreServicio_"+pos)+" "+
            		"DE LA CITA DE "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" PARA LA FECHA/HORA "+
    				citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString())
            		));
            
        }
        
        ///Actualizacion del pool de la solicitud*******************************************************************
		int codigoMedico = UtilidadesConsultaExterna.obtenerCodigoMedicoAgenda(con, cita.getCodigoAgenda());
		if(codigoMedico>0)
		{
			Solicitud sol=new Solicitud();
			ArrayList array=Utilidades.obtenerPoolesMedico(con,UtilidadFecha.getFechaActual(con),codigoMedico);
			if(array.size()==1)
				sol.actualizarPoolSolicitud(con,numeroSolicitud,Integer.parseInt(array.get(0)+""));
		}
        
        //***********************************************************************************************
		//**CAMBIO POR JUS SERV NO POS
        logger.info("[injeccion de numero solucitud a mapa servicios indice ] i"+i+" pos"+pos+" numeroSolicitud"+numeroSolicitud);
        citaForm.getMapaServicios().put("numeroSolicitud_"+pos, numeroSolicitud);
        mapaServicios.put("numeroSolicitud_"+pos, numeroSolicitud);
        //*****************************
        
		//*******************SE CALCULA LA SUBCUENTA DE LA COBERTURA**************************************
        double subCuenta=ConstantesBD.codigoNuncaValido;
		InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();;
		infoResponsableCobertura=Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), codigoServicio, usuario.getCodigoInstitucionInt(),citaForm.isSolPYP(), "" /*subCuentaCoberturaOPCIONAL*/);
		subCuenta=infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble();
        //************************************************************************************************
	
		
		//*************SE INSERTA LA SOLICITUD DE CIRUGIA ************************************************
		//1) Se inserta encabezado de la cirugia
		try 
		{
			resp0 = mundoSolCx.insertarSolicitudCxGeneralTransaccional1(
					con, 
					numeroSolicitud+"", 
					codigoPeticion+"", 
					false, 
					ConstantesBD.continuarTransaccion, 
					subCuenta,
					ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento);
			
		} catch (SQLException e) 
		{
			resp0 = false;
			logger.error("Error al insertar solicitud general de cirugï¿½a: "+e);
		}
		if(!resp0)
			 errores.add("",new ActionMessage("errors.noSeGraboInformacion",
        		"DE LA SOLICITUD Qx PARA EL SERVICIO "+mapaServicios.get("nombreServicio_"+pos)+" "+
        		"DE LA CITA DE "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" PARA LA FECHA/HORA "+
				citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString())
        		));
		else
		{
			//2) Se inserta el detalle de la cirugia (El servicio)
			 
				resp1 = mundoSolCx.insertarSolicitudCxXServicioTransaccional(
					con, 
					numeroSolicitud+"", 
					codigoServicio+"", 
					ConstantesBD.codigoNuncaValido, //codigo tipo cirugia
					1,
					ConstantesBD.codigoNuncaValido, // esquema tarifario
					ConstantesBD.codigoNuncaValidoDouble, //grupo o uvr 
					usuario.getCodigoInstitucionInt(), 
					/*numeroAutorizacion,*/ 
					ConstantesBD.codigoNuncaValido, //finalidad 
					observaciones, 
					"", //via Cx 
					"",  // indicativo bilateral
					"",  //indicativo via de acceso
					ConstantesBD.codigoNuncaValido, // codigo especialidad
					"", //liquidar servicio 
					ConstantesBD.continuarTransaccion,
					"",
					null);
				
			
			
			if(resp1<=0)
				errores.add("",new ActionMessage("errors.noSeGraboInformacion",
		        		"DEL DETALLE DE LA SOLICITUD Qx PARA EL SERVICIO "+mapaServicios.get("nombreServicio_"+pos)+" "+
		        		"DE LA CITA DE "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" PARA LA FECHA/HORA "+
						citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString())
		        		));
		}
		
		//*******************************************************************************************************
		
		
		if(resp0 && resp1 > 0)
		{
			
			//**************SE ACTUALIZA EL NUMERO DE SOLICITUD EN LA CITA********************************************
			if(cita.asignarSolicitud(con, numeroSolicitud, codigoServicio, ConstantesBD.continuarTransaccion, usuario.getLoginUsuario())<=0)
				errores.add("",new ActionMessage("errors.noSeGraboInformacion",
						"AL ACTUALIZAR LA SOLICITUD DEL SERVICIO "+mapaServicios.get("nombreServicio_"+pos)+" "+
						"PARA LA CITA DE "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" PARA LA FECHA/HORA "+
						citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString())));
			//**************************************************************************************************************************
			//******************SE ACTUALIZA EL ESTADO DE LA CITA**********************************************************
			if (estadoCita == ConstantesBD.codigoEstadoCitaReservada)
				cita.actualizarEstadoCitaTransaccional(con, ConstantesBD.codigoEstadoCitaAsignada, cita.getCodigo(), ConstantesBD.continuarTransaccion, usuario.getLoginUsuario());
			//**************************************************************************************************************
			
			//Cambiar el indicador de liquidacion de asocios a No por haber generado el cargo sin asocios
			if(!objectSolicitud.cambiarLiquidacionAsociosSolicitud(con, numeroSolicitud, ConstantesBD.acronimoNo))
				errores.add("",new ActionMessage("errors.noSeGraboInformacion",
						"AL ACTUALIZAR EL CAMPO LIQUIDAR ASOCIOS DE LA SOLICITUD "+numeroSolicitud));
			else
				logger.info("ACTUALIZA EL VALOR DE LIQUIDAR ASOCIOS DE LA SOLICITUD A NO");
			
			//***************ACTUALIZACIï¿½N DE LA ORDEN AMBULATORIA (FLUJO ORDENES AMBULATORIAS)******************************
			//logger.info("INDICATIVO ORDEN AMBULATORIA AL GENERAR LA SOLICITUD=> "+citaForm.isIndicativoOrdenAmbulatoria());
			//logger.info("ESTADO DE LA CITA AL GENERAR LA SOLICITUD=> "+estadoCita+" = "+ConstantesBD.codigoEstadoCitaAProgramar);
			
			if(citaForm.isIndicativoOrdenAmbulatoria()&&(!validarEstadoCita||(validarEstadoCita&&estadoCita==ConstantesBD.codigoEstadoCitaAProgramar)))
			{
				String codigoOrden=Utilidades.obtenerCodigoOrdenAmbulatoria(con,citaForm.getOrdenAmbulatoria(),usuario.getCodigoInstitucionInt());
				HashMap vo=new HashMap();
				vo.put("estadoOrden",ConstantesBD.codigoEstadoOrdenAmbulatoriaSolicitada+"");
				vo.put("numeroSolicitud",numeroSolicitud+"");
				vo.put("numeroOrden",codigoOrden+"");
				vo.put("usuario",usuario.getLoginUsuario());
				//logger.info("Entrï¿½ por aquï¿½: "+vo);
				//esta consulta solo aplica para ordenes ambulatorias de tipo no cruentos, ya que en la generacion de la cita no se hace esta confimacion.
				if(OrdenesAmbulatorias.confirmarOrdenAmbulatoria(con,vo)<=0)
					errores.add("",new ActionMessage("errors.noSeGraboInformacion",
							"AL ACTUALIZAR EL ESTADO DE LA ORDEN AMBULATORIA "+citaForm.getOrdenAmbulatoria()+" "+
							"DE LA CITA DE "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" PARA LA FECHA/HORA "+
							citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString())));
				
				/**
				citaForm.setCodigoDetalleCargo(Utilidades.convertirAEntero(String.valueOf(cargos.getDtoDetalleCargo().getCodigoDetalleCargo())));	 
				citaForm.setCodigoConvenioCargo(infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo());
				citaForm.setCodigoSubCuentaCargo(Integer.parseInt(infoResponsableCobertura.getDtoSubCuenta().getSubCuenta()));
				logger.info("\n\n FLUJO CIRUGIA >> INFO DEL CARGO  CODIGO " +citaForm.getCodigoDetalleCargo()+"  SUbcuenta"+ citaForm.getCodigoSubCuentaCargo());
				if(Autorizaciones.actualizarAutorizacionyDetalle(con, codigoOrden, paciente.getCodigoCuenta()+"", citaForm.getCodigoConvenioCargo()+"", cita.getNumeroSolicitud()+"", citaForm.getCodigoDetalleCargo()+"", citaForm.getCodigoSubCuentaCargo()+"","")!=1)
				{
					errores.add("",new ActionMessage("errors.notEspecific","Problemas Actulizando los Datos de Autorizacion "));
				}**/
				
				
				
				
				/*
				 * Actualizaciï¿½n de PYP, si la orden ambulatoria es pyp se debe actualizar la solicitud
				*/
				if(citaForm.isSolPYP())
				{
					
					if(Utilidades.asignarSolicitudToActividadPYP(con, codigoOrden, numeroSolicitud+""))
					{
						String consecutivoActividad = Utilidades.obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(con, numeroSolicitud);
						if(!Utilidades.actualizarEstadoActividadProgramaPypPaciente(con, consecutivoActividad, ConstantesBD.codigoEstadoProgramaPYPSolicitado, usuario.getLoginUsuario(), ""))
							errores.add("",new ActionMessage("errors.noSeGraboInformacion",
									"AL ACTUALIZAR LA ACTIVIDAD PYP DE LA ORDEN AMBULATORIA "+citaForm.getOrdenAmbulatoria()+" "+
									"DE LA CITA DE "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" PARA LA FECHA/HORA "+
									citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString())));
							
					}
					else
						errores.add("",new ActionMessage("errors.noSeGraboInformacion",
								"AL CONSULTAR LA ACTIVIDAD PYP DE LA ORDEN AMBULATORIA "+citaForm.getOrdenAmbulatoria()+" "+
								"DE LA CITA DE "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" PARA LA FECHA/HORA "+
								citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString())));
				}
			}
			//****************************************************************************************************************
			
			//Se aï¿½ade la cita a la respuesta para el resumen
			request.getSession().setAttribute("codigoCita_"+citaForm.getNumeroCitasTemporal(), "" + cita.getCodigo());
			citaForm.setNumeroCitasTemporal(citaForm.getNumeroCitasTemporal()+1);
			
		}
		//*******************************************************************************************************
		
		return errores;
	}

	/**
	 * Mï¿½todo implementado para generar solicitud de procedimiento
	 * @param con
	 * @param citaForm
	 * @param pos
	 * @param mapaServicios
	 * @param errores
	 * @param paciente
	 * @param usuario
	 * @param request 
	 * @param cita
	 * @param ocupacionMedica
	 * @param estadoCita
	 * @param i
	 * @param validarEstadoCita 
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "rawtypes", "static-access" })
	private ActionErrors generacionSolicitudProcedimiento(Connection con, CitaForm citaForm, int pos, HashMap mapaServicios, ActionErrors errores, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request, Cita cita, int ocupacionMedica, int estadoCita, int i, DtoRespAutorizacion dto, boolean validarEstadoCita) 
	{
		SolicitudProcedimiento	solicitud= new SolicitudProcedimiento();
		
		//String numeroAutorizacion = mapaServicios.get("numeroAutorizacion_"+pos).toString();
		int codigoServicio = Integer.parseInt(mapaServicios.get("codigoServicio_"+pos).toString());
		int codigoCentroCosto = Integer.parseInt(mapaServicios.get("codigoCentroCosto_"+pos).toString());
		int codigoEspecialidad = Integer.parseInt(mapaServicios.get("codigoEspecialidad_"+pos).toString());
		
		try
		{
			//********************************GENERACION DE LA SOLICITUD DE PROCEDIMIENTOS*********************************************
			solicitud.setFechaSolicitud(UtilidadFecha.getFechaActual(con));
			solicitud.setHoraSolicitud(UtilidadFecha.getHoraActual(con));
			solicitud.setSolPYP(citaForm.isSolPYP());
			solicitud.setTipoSolicitud(new InfoDatosInt (ConstantesBD.codigoTipoSolicitudProcedimiento, ""));
			solicitud.setCobrable(true);
			//solicitud.setNumeroAutorizacion(numeroAutorizacion);
			solicitud.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaNinguna, ""));
			solicitud.setOcupacionSolicitado(new InfoDatosInt(ocupacionMedica, ""));
			solicitud.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea(), ""));
			solicitud.setCentroCostoSolicitado(new InfoDatosInt(codigoCentroCosto, ""));
			solicitud.setCodigoMedicoSolicitante(ConstantesBD.codigoNuncaValido);
			solicitud.setCodigoCentroAtencionCuentaSol(usuario.getCodigoCentroAtencion());
			solicitud.setCodigoCuenta(paciente.getCodigoCuenta());
			solicitud.setCodigoPaciente(paciente.getCodigoPersona());
			solicitud.setDatosMedico("");
			solicitud.setVaAEpicrisis(false);
			solicitud.setUrgente(false);
			solicitud.setComentario("");
			solicitud.setNombreOtros("");
			solicitud.setTieneCita(true);
			solicitud.setEspecialidadSolicitadaOrdAmbulatorias(codigoEspecialidad);
			
			int numeroDocumento=solicitud.numeroDocumentoSiguiente(con);

			/*Obtener el codigo de la finalidad*/
			ArrayList<HashMap<String, Object>> finalidades = Utilidades.obtenerFinalidadesServicio(con, codigoServicio, usuario.getCodigoInstitucionInt());
			//Si solo hay una finalidad se asigna automaticamente, de lo contrario se deja vacï¿½o
			if(finalidades.size()==1)
				solicitud.setFinalidad(Integer.parseInt(((HashMap)finalidades.get(0)).get("codigo").toString()));
			else
				solicitud.setFinalidad(0);
					
			/* Obtener el cï¿½digo del servicio de la solicitud */
			solicitud.setCodigoServicioSolicitado(codigoServicio);
			//por defecto en la creacion de la solicitud la finalizacion de la respuesta multiple es false;
			solicitud.setFinalizadaRespuesta(false);
			//el indicativo de si la solicitud es de respuesta multiple depende de la parametrizacion del servicio
			solicitud.setRespuestaMultiple(Utilidades.esServicioRespuestaMultiple(con,codigoServicio+""));
			/* Obtener dato soicitud mï¿½ltiple*/
			solicitud.setMultiple(false);

			InfoDatosInt centroCosto=new InfoDatosInt();
			centroCosto.setCodigo(codigoCentroCosto);
			solicitud.setCentroCostoSolicitado(centroCosto);
			solicitud.setFrecuencia(ConstantesBD.codigoNuncaValido);

			//Aca se genera la solicitud, para la tarea 146930 se adiciona el codigo del medico para guardarlo en datos_medico_responde
			logger.info("************MEDICO/CITA-------->"+citaForm.getCodigoMedico()+"/"+cita.getCodigo());
			solicitud.setCodigoMedicoResponde(cita.getCodigoMedicoXAgenda());
			
			/* Genera ls solicitud */
			int numeroSolicitud = solicitud.insertarTransaccional(con, ConstantesBD.continuarTransaccion, numeroDocumento, paciente.getCodigoCuenta(), false,ConstantesBD.codigoNuncaValido+"");
			mapaServicios.put("numeroSolicitud_"+pos, numeroSolicitud);
			solicitud.setNumeroSolicitud(numeroSolicitud);
			
			citaForm.setNumeroSolicitudCita(numeroSolicitud);
			citaForm.setConsecutivoCita(solicitud.getConsecutivoOrdenesMedicas());
			
			///Actualizacion del pool de la solicitud*******************************************************************
			int codigoMedico = UtilidadesConsultaExterna.obtenerCodigoMedicoAgenda(con, cita.getCodigoAgenda());
			if(codigoMedico>0)
			{
				Solicitud sol=new Solicitud();
				ArrayList array=Utilidades.obtenerPoolesMedico(con,UtilidadFecha.getFechaActual(con),codigoMedico);
				if(array.size()==1)
					sol.actualizarPoolSolicitud(con,numeroSolicitud,Integer.parseInt(array.get(0)+""));
			}
			
			//GENERACION DE LOS SERVICIOS/ARTICULOS INCLUIDOS
			if(!solicitud.registrarServiciosIncluidos(con, usuario, paciente))
			{
				errores.add("",new ActionMessage("errors.noSeGraboInformacion",
						"AL REGISTRAR LOS SERVICIOS INCLUIDOS DEL SERVICIO "+mapaServicios.get("nombreServicio_"+pos)+" "+
						"PARA LA CITA DE "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" PARA LA FECHA/HORA "+
						citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString())));
			}
						
			 //GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
		    Cargos cargos= new Cargos();
		    cargos.setPyp(citaForm.isSolPYP());
		    boolean dejarPendiente=true;
			if(UtilidadValidacion.esServicioViaIngresoCargoSolicitud(con, paciente.getCodigoUltimaViaIngreso()+"", ""+solicitud.getCodigoServicioSolicitado(), usuario.getCodigoInstitucion()))
			{
				dejarPendiente=false;
			}
			
			//logger.info("NUMERO DE AUTORIZACION AL GENERAR SOLICITUD PROCEDIMIENTOS: *"+numeroAutorizacion+"*");
		    
		    boolean inserto= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
				usuario, 
				paciente, 
				dejarPendiente/*dejarPendiente*/, 
				solicitud.getNumeroSolicitud(), 
				ConstantesBD.codigoTipoSolicitudProcedimiento /*codigoTipoSolicitudOPCIONAL*/, 
				paciente.getCodigoCuenta() /*codigoCuentaOPCIONAL*/, 
				solicitud.getCentroCostoSolicitado().getCodigo()/*codigoCentroCostoEjecutaOPCIONAL*/, 
				solicitud.getCodigoServicioSolicitado()/*codigoServicioOPCIONAL*/, 
				1 /*cantidadServicioOPCIONAL*/, 
				ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
				ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
				/*numeroAutorizacion,*/
				"" /*esPortatil*/,false,solicitud.getFechaSolicitud(),"");
						
		   /**
		     * Victor Gomez
		     */
		    //********************************************************************
		    logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nel codigo del detalle cargo: "+cargos.getDtoDetalleCargo().getCodigoDetalleCargo()+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		    //**********************************************************************************************
		    // se ingresa la autorizacion y la respuesta de la misma
		    logger.info("Estado Ingrso Respuesta >>>>>>> "+dto.isEstadoIngresoResp());
		    if(dto.isEstadoIngresoResp()){
			    DtoAutorizacion dtoAutorizacion = new DtoAutorizacion();
			    dtoAutorizacion.setIdIngreso(String.valueOf(paciente.getCodigoIngreso()));
			    dtoAutorizacion.setIdSubCuenta(String.valueOf(cargos.getDtoDetalleCargo().getCodigoSubcuenta()));
			    dtoAutorizacion.setConvenio(new InfoDatosInt(paciente.getCodigoConvenio(),""));
			    dtoAutorizacion.setIdCuenta(String.valueOf(paciente.getCodigoCuenta()));
			    dtoAutorizacion.setTipoTramite(ConstantesIntegridadDominio.acronimoExterna);
			    dtoAutorizacion.setUsuarioModifica(usuario);
			    dtoAutorizacion.setTipo(ConstantesIntegridadDominio.acronimoSolicitud);
			    
			    DtoDetAutorizacion dtoDetAutorizacion = new DtoDetAutorizacion();
			    dtoDetAutorizacion.setServicioArticulo(new InfoDatosInt(Utilidades.convertirAEntero(dto.getServicio().getCodigo()),dto.getServicio().getNombre()));
			    dtoDetAutorizacion.setEstadoSolDetAuto(dto.getEstadorespauto());
			    dtoDetAutorizacion.setNumeroSolicitud(String.valueOf(cargos.getDtoDetalleCargo().getCodigoSolicitudSubCuenta()));
			    dtoDetAutorizacion.setUsuarioModifica(usuario);
			    dtoDetAutorizacion.setActivo(ConstantesBD.acronimoSi);
			    dtoDetAutorizacion.setCodigoEvaluar(String.valueOf(cargos.getDtoDetalleCargo().getCodigoDetalleCargo()));
			    dtoDetAutorizacion.setTipoOrden(Autorizaciones.codInternoAutoSolicSerMed);
			    		    
			    //llena informacion de la respuesta
			    dto.setPersonaRegistro(usuario.getLoginUsuario());
			    dto.setCodCargoPersRegistro(usuario.getCodigoCargo()+"");
			    // se vincula la respuesta al detalle de autorizacion
			    dtoDetAutorizacion.setRespuestaDto(dto);
			    // se adiciona el detalle de autorizacion a al autorizacion
			    dtoAutorizacion.setDetalle(new ArrayList<DtoDetAutorizacion>());
			    dtoAutorizacion.getDetalle().add(dtoDetAutorizacion);
			    
			    Autorizaciones autorizaciones = new Autorizaciones(); 
			    InfoDatosInt codigo = autorizaciones.insertarAutorizacion(con, dtoAutorizacion, null, null, null, null); 
			    if(codigo.getCodigo()>0){
			    	if(!cargos.actualizarNumeroAutorizacion(con, dto.getNumeroAutorizacion(), cargos.getDtoDetalleCargo().getCodigoDetalleCargo()))
				    	logger.error("Error en la modificacion del campo nro_autorizacion de la tabla det_cargo");
			    }else
			    	logger.info("ERROR en la Insercion de Autorizacion ");
			    
		     }
		    //********************************************************************
		    
		    
		    
			if(!inserto)
			{
				errores.add("",new ActionMessage("errors.noSeGraboInformacion",
						"AL GENERAR LA SOLICITUD PARA EL SERVICIO "+mapaServicios.get("nombreServicio_"+pos)+" "+
						"DE LA CITA DE "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" PARA LA FECHA/HORA "+
						citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString())));
				
				citaForm.setItemSeleccionado("codigoEstadoLiquidacion_" + i,ConstantesBD.codigoEstadoLiquidacionSinLiquidar);
			}
			//***************************************************************************************************************************
			else
			{
				//**********************ASIGNACION DE LA SOLICITUD A LA CITA***************************************************************
				if(cita.asignarSolicitud(con, numeroSolicitud, codigoServicio, ConstantesBD.continuarTransaccion, usuario.getLoginUsuario())<=0)
					errores.add("",new ActionMessage("errors.noSeGraboInformacion",
							"AL ACTUALIZAR LA SOLICITUD DEL SERVICIO "+mapaServicios.get("nombreServicio_"+pos)+" "+
							"PARA LA CITA DE "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" PARA LA FECHA/HORA "+
							citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString())));
				//**************************************************************************************************************************
				//******************SE ACTUALIZA EL ESTADO DE LA CITA**********************************************************
				if (estadoCita == ConstantesBD.codigoEstadoCitaReservada)
					cita.actualizarEstadoCitaTransaccional(con, ConstantesBD.codigoEstadoCitaAsignada, cita.getCodigo(), ConstantesBD.continuarTransaccion, usuario.getLoginUsuario());
				//**************************************************************************************************************
				
				//Se aï¿½ade la cita a la respuesta para el resumen
				request.getSession().setAttribute("codigoCita_"+citaForm.getNumeroCitasTemporal(), "" + cita.getCodigo());
				citaForm.setNumeroCitasTemporal(citaForm.getNumeroCitasTemporal()+1);
			}			
			
		}
		catch(Exception e)
		{
			logger.error("Error al generar la solicitud de procedimientos de la cita: "+e);
			errores.add("",new ActionMessage("errors.noSeGraboInformacion",
					"AL GENERAR LA SOLICITUD PARA EL SERVICIO "+mapaServicios.get("nombreServicio_"+pos)+" "+
					"DE LA CITA DE "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" PARA LA FECHA/HORA "+
					citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString())));
		}

		return errores;
	}

	/**
	 * Mï¿½todo implementado para generar solicitudes de consulta
	 * @param con
	 * @param citaForm
	 * @param pos
	 * @param mapaServicios
	 * @param errores
	 * @param paciente
	 * @param usuario
	 * @param request 
	 * @param cita 
	 * @param ocupacionMedica 
	 * @param estadoCita 
	 * @param i 
	 * @param validarEstadoCita 
	 * @return
	 */
	@SuppressWarnings({ "deprecation", "rawtypes", "static-access" })
	private ActionErrors generacionSolicitudConsulta(Connection con, CitaForm citaForm, int pos, HashMap mapaServicios, ActionErrors errores, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request, Cita cita, int ocupacionMedica, int estadoCita, int i, boolean excento, DtoRespAutorizacion dto, boolean validarEstadoCita) 
	{
		
		int codigoServicio = Integer.parseInt(mapaServicios.get("codigoServicio_"+pos).toString());
		int codigoCentroCosto = Integer.parseInt(mapaServicios.get("codigoCentroCosto_"+pos).toString());
		int codigoEspecialidad = Integer.parseInt(mapaServicios.get("codigoEspecialidad_"+pos).toString());
		//String numeroAutorizacion = mapaServicios.get("numeroAutorizacion_"+pos).toString();
		
		logger.info("\n\n************************************************************************************\n" +
					"-----> [CARGO] DEJAR EXCENTO?"+excento+"\n" +
					"************************************************************************************");
		
		
		boolean lb_generada=false;
		
		Cargos cargos=new Cargos();
		
		try
		{
			
			cita.setCodigoAreaPaciente(paciente.getCodigoArea());
			cita.setCodigoCentroCostoSolicitado(codigoCentroCosto);
			cita.setCodigoEspecialidadSolicitante(codigoEspecialidad);
			/** * **/
			cita.setDtoDiagnostico(citaForm.getDtoDiagnostico());
			
			//en caso de que la solicitud sea de pyp
			cita.setSolPYP(citaForm.isSolPYP());
			
			//Cambio por tarea 146930 - Se agrega el codigo del medico
			//Se genera la solicitud			
			lb_generada =cita.generarSolicitud(
				con,
				ConstantesBD.continuarTransaccion,
				ocupacionMedica,
				citaForm.getCuentaPaciente(),
				codigoServicio,
				/*mapaServicios.get("numeroAutorizacion_"+pos).toString(),*/ 
				paciente.getCodigoArea(), 
				codigoCentroCosto,
				usuario.getLoginUsuario(),
				true);
			
			//Se actualiza el estado a asignada cuando el estado es reservada
			if (estadoCita == ConstantesBD.codigoEstadoCitaReservada)
				cita.actualizarEstadoCitaTransaccional(con, ConstantesBD.codigoEstadoCitaAsignada, cita.getCodigo(), ConstantesBD.continuarTransaccion, usuario.getLoginUsuario());
			
			citaForm.setNumeroSolicitudCita(cita.getNumeroSolicitud());
			citaForm.setConsecutivoCita(cita.getConsecutivoCita());
		}//try
		catch(Exception e)
		{
			logger.error("Error generando el cargo para la cita: "+e);
		}
		
		//Actualizacion del pool de la solicitud*******************************************************************
		logger.info("CODIGO DE LA AGENDA: "+cita.getCodigoAgenda());
		int codigoMedico = UtilidadesConsultaExterna.obtenerCodigoMedicoAgenda(con, cita.getCodigoAgenda());
		logger.info("CODIGO MEDICO ENCONTRADO DE LA AGENDA:"+codigoMedico);
		if(codigoMedico>0)
		{
			Solicitud sol=new Solicitud();
			ArrayList array=Utilidades.obtenerPoolesMedico(con,UtilidadFecha.getFechaActual(con),codigoMedico);
			logger.info("CUANTOS POOLES TIENE EL MEDICO: "+array.size()+" para el numero de oslicitud: "+cita.getNumeroSolicitud());
			if(array.size()==1)
				sol.actualizarPoolSolicitud(con,cita.getNumeroSolicitud(),Integer.parseInt(array.get(0)+""));
		}
		
		logger.info("\n\n\n\n********************PARTE DE LA GENERACION DEl cargo**************************************\n\n\n\n");
		if(lb_generada)
		{
			mapaServicios.put("numeroSolicitud_" + pos, cita.getNumeroSolicitud() + "");
			request.getSession().setAttribute("codigoCita_"+citaForm.getNumeroCitasTemporal(), "" + cita.getCodigo());
			citaForm.setNumeroCitasTemporal(citaForm.getNumeroCitasTemporal()+1);
			//cargo.generarCargoCita(con, cita.getNumeroSolicitud(), usuario.getLoginUsuario());
			//GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA
			
		    cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
    																			usuario, 
    																			paciente, 
    																			false/*dejarPendiente*/, 
    																			cita.getNumeroSolicitud(), 
    																			ConstantesBD.codigoTipoSolicitudCita /*codigoTipoSolicitudOPCIONAL*/, 
    																			citaForm.getCuentaPaciente(), 
    																			ConstantesBD.codigoCentroCostoConsultaExterna/*codigoCentroCostoEjecutaOPCIONAL*/, 
    																			codigoServicio/*codigoServicioOPCIONAL*/, 
    																			1/*cantidadServicioOPCIONAL*/, 
    																			ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
    																			ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
    																			/*numeroAutorizacion,*/
    																			""/*esPortatil*/,
    																			excento,citaForm.getFechaSolicitada(),""
    																		);
		    /**
		     * Victor Gomez
		     */
		    //********************************************************************
		    logger.info("\n\n\n\n\n\nel codigo del detalle cargo: "+cargos.getDtoDetalleCargo().getCodigoDetalleCargo()+"\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
		    //**********************************************************************************************
		    // se ingresa la autorizacion y la respuesta de la misma
		    logger.info("Estado Ingrso Respuesta >>>>>>> "+dto.isEstadoIngresoResp());
		    if(dto.isEstadoIngresoResp()){
			    DtoAutorizacion dtoAutorizacion = new DtoAutorizacion();
			    dtoAutorizacion.setIdIngreso(String.valueOf(paciente.getCodigoIngreso()));
			    dtoAutorizacion.setIdSubCuenta(String.valueOf(cargos.getDtoDetalleCargo().getCodigoSubcuenta()));
			    dtoAutorizacion.setConvenio(new InfoDatosInt(paciente.getCodigoConvenio(),""));
			    dtoAutorizacion.setIdCuenta(String.valueOf(paciente.getCodigoCuenta()));
			    dtoAutorizacion.setTipoTramite(ConstantesIntegridadDominio.acronimoExterna);
			    dtoAutorizacion.setUsuarioModifica(usuario);
			    dtoAutorizacion.setTipo(ConstantesIntegridadDominio.acronimoSolicitud);
			    
			    DtoDetAutorizacion dtoDetAutorizacion = new DtoDetAutorizacion();
			    dtoDetAutorizacion.setServicioArticulo(new InfoDatosInt(Utilidades.convertirAEntero(dto.getServicio().getCodigo()),dto.getServicio().getNombre()));
			    dtoDetAutorizacion.setEstadoSolDetAuto(dto.getEstadorespauto());
			    dtoDetAutorizacion.setNumeroSolicitud(String.valueOf(cargos.getDtoDetalleCargo().getCodigoSolicitudSubCuenta()));
			    dtoDetAutorizacion.setUsuarioModifica(usuario);
			    dtoDetAutorizacion.setCodigoEvaluar(String.valueOf(cargos.getDtoDetalleCargo().getCodigoDetalleCargo()));
			    dtoDetAutorizacion.setActivo(ConstantesBD.acronimoSi);
			    dtoDetAutorizacion.setTipoOrden(Autorizaciones.codInternoAutoSolicSerMed);
			    		    
			    //llena informacion de la respuesta
			    dto.setPersonaRegistro(usuario.getLoginUsuario());
			    dto.setCodCargoPersRegistro(usuario.getCodigoCargo()+"");
			    // se vincula la respuesta al detalle de autorizacion
			    dtoDetAutorizacion.setRespuestaDto(dto);
			    // se adiciona el detalle de autorizacion a al autorizacion
			    dtoAutorizacion.setDetalle(new ArrayList<DtoDetAutorizacion>());
			    dtoAutorizacion.getDetalle().add(dtoDetAutorizacion);
			    
			    Autorizaciones autorizaciones = new Autorizaciones(); 
			    InfoDatosInt codigo = autorizaciones.insertarAutorizacion(con, dtoAutorizacion, null, null, null, null); 
			    if(codigo.getCodigo()>0){
			    	if(!cargos.actualizarNumeroAutorizacion(con, dto.getNumeroAutorizacion(), cargos.getDtoDetalleCargo().getCodigoDetalleCargo()))
			    		errores.add("",new ActionMessage("errors.notEspecific","Problemas Actualizando Autorizacion en el detalle del Cargo"));				    	
			    }else
			    	errores.add("",new ActionMessage("errors.notEspecific","Problemas Insertando Datos de la Autorizacion "));
			    			    	
		     }
		    //********************************************************************
		    
		    //**********************************************************************************************
		    
		    
		    //logger.info("INDICATIVO ORDEN AMBULATORIA DESPUES DE GENERAR LA SOLICITUD=> "+citaForm.isIndicativoOrdenAmbulatoria());
		    //logger.info("ESTADO DE LA CITA AL GENERAR LA SOLICITUD=> "+estadoCita+" = "+ConstantesBD.codigoEstadoCitaAProgramar);
		    //logger.info("VALIDAR ESTADO CITA=> "+validarEstadoCita);
		    
		    if(citaForm.isIndicativoOrdenAmbulatoria()&&(!validarEstadoCita||(validarEstadoCita&&estadoCita==ConstantesBD.codigoEstadoCitaAProgramar)))
			{
		    	
				String codigoOrden=Utilidades.obtenerCodigoOrdenAmbulatoria(con,citaForm.getOrdenAmbulatoria(),usuario.getCodigoInstitucionInt());
				HashMap vo=new HashMap();
				vo.put("estadoOrden",ConstantesBD.codigoEstadoOrdenAmbulatoriaSolicitada+"");
				vo.put("numeroSolicitud",cita.getNumeroSolicitud()+"");
				vo.put("numeroOrden",codigoOrden+"");
				vo.put("usuario",usuario.getLoginUsuario());
				//logger.info("Entrï¿½ por aquï¿½: "+vo);
				//esta consulta solo aplica para ordenes de tipo consulta, ya que en la generacion de la cita no se hace esta confimacion.
				if(OrdenesAmbulatorias.confirmarOrdenAmbulatoria(con,vo)<=0)
					errores.add("",new ActionMessage("errors.noSeGraboInformacion",
							"AL ACTUALIZAR EL ESTADO DE LA ORDEN AMBULATORIA "+codigoOrden+" "+
							"DE LA CITA DE "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" PARA LA FECHA/HORA "+
							citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString())));
				
				logger.info("TERMINï¿½ DE CONFIRMAR ORDEN AMBULATORIA ï¿½ES PYP?: "+citaForm.isSolPYP());
				
				/**
				 * Actualiza las tablas de Autorizacion , detalle autorizacion , detalle cargos 
				 * Andres Ortiz
				 */
				citaForm.setCodigoDetalleCargo(Utilidades.convertirAEntero(String.valueOf(cargos.getDtoDetalleCargo().getCodigoDetalleCargo())));	 
				citaForm.setCodigoConvenioCargo(cargos.getDtoDetalleCargo().getCodigoConvenio());
				citaForm.setCodigoSubCuentaCargo(Utilidades.convertirAEntero(String.valueOf(cargos.getDtoDetalleCargo().getCodigoSubcuenta())));
				logger.info("\n\n FLUJO CONSULTA >> INFO DEL CARGO  CODIGO " +citaForm.getCodigoDetalleCargo()+"  SUbcuenta"+ citaForm.getCodigoSubCuentaCargo());
				if(Autorizaciones.actualizarAutorizacionyDetalle(con, codigoOrden, paciente.getCodigoCuenta()+"", citaForm.getCodigoConvenioCargo()+"", cita.getNumeroSolicitud()+"", citaForm.getCodigoDetalleCargo()+"", citaForm.getCodigoSubCuentaCargo()+"","")!=1)
				{
					errores.add("",new ActionMessage("errors.notEspecific","Problemas Actulizando los Datos de Autorizacion "));
				}
				
				
				
				/*
				 * Actualizaciï¿½n de PYP, si la orden ambulatoria es pyp se debe actualizar la solicitud
				*/
				if(citaForm.isSolPYP())
				{
					
					if(Utilidades.asignarSolicitudToActividadPYP(con, codigoOrden, cita.getNumeroSolicitud()+""))
					{
						String consecutivoActividad = Utilidades.obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(con, cita.getNumeroSolicitud());
						if(!Utilidades.actualizarEstadoActividadProgramaPypPaciente(con, consecutivoActividad, ConstantesBD.codigoEstadoProgramaPYPSolicitado, usuario.getLoginUsuario(), ""))
							errores.add("",new ActionMessage("errors.noSeGraboInformacion",
									"AL ACTUALIZAR LA ACTIVIDAD PYP DE LA ORDEN AMBULATORIA "+codigoOrden+" "+
									"DE LA CITA DE "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" PARA LA FECHA/HORA "+
									citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString())));
							
					}
					else
						errores.add("",new ActionMessage("errors.noSeGraboInformacion",
								"AL CONSULTAR LA ACTIVIDAD PYP DE LA ORDEN AMBULATORIA "+codigoOrden+" "+
								"DE LA CITA DE "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" PARA LA FECHA/HORA "+
								citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString())));
				}
			}
		   	
		}
		else
		{
			citaForm.setItemSeleccionado("codigoEstadoLiquidacion_" + i,ConstantesBD.codigoEstadoLiquidacionSinLiquidar);
			errores.add("",new ActionMessage("errors.noSeGraboInformacion",
					"AL GENERAR LA SOLICITUD PARA EL SERVICIO "+mapaServicios.get("nombreServicio_"+pos)+" "+
					"DE LA CITA DE "+citaForm.getItemSeleccionado("nombreUnidadConsulta_" + i)+" PARA LA FECHA/HORA "+
					citaForm.getItemSeleccionado("fecha_" + i)+" / "+UtilidadFecha.convertirHoraACincoCaracteres(citaForm.getItemSeleccionado("horaInicio_" + i).toString())));
		}
		
		
		return errores;
	}

	/**
	* Carga en la lista de asignaciï¿½n / reserva de citas las citas reservadas para este paciente
	*/
	@SuppressWarnings("rawtypes")
	private void cargarCitasReservadas(CitaForm acf_form, Connection ac_con, UsuarioBasico usuario,String centrosAtencion, String unidadesAgenda) throws NumberFormatException, SQLException
	{
		/* Obtener la lista de citas reservadas para el paciente */
		Collection coleccionReservados = Cita.listar(ac_con,Cita.BUSCAR_PARA_LIQUIDACION,acf_form.getCodigoPaciente(),-1,-1,0,"",UtilidadFecha.conversionFormatoFechaAAp(new Date() ),"","","",-1,ConstantesBD.codigoEstadoLiquidacionSinLiquidar,-1,ConstantesBD.codigoNuncaValido+"",centrosAtencion,unidadesAgenda);
		
		Iterator iterator;
		
		if(coleccionReservados != null)
		{
			HashMap	ldb_reservado;
			String		ls_estadoLiquidacion;

			iterator = coleccionReservados.iterator();

			
			for(int li_i = acf_form.getNumeroItemsSeleccionados(); iterator.hasNext(); li_i++)
			{
				ldb_reservado			= (HashMap)iterator.next();
				ls_estadoLiquidacion	= (String)ldb_reservado.get("codigoestadoliquidacion");
				
				
				/* Determinar si la cita fue reservada */
				if(ls_estadoLiquidacion.equals(ConstantesBD.codigoEstadoLiquidacionSinLiquidar) )
				{
					logger.info("\n\n\n\n entro");

					
					acf_form.setItemSeleccionado("codigoAgenda_" + li_i, ldb_reservado.get("codigoagenda")+"");
					acf_form.setItemSeleccionado("codigoCita_" + li_i, ldb_reservado.get("codigo")+"");
					acf_form.setItemSeleccionado("codigoEstadoCita_" + li_i,ldb_reservado.get("codigoestadocita")+"");
					acf_form.setItemSeleccionado("nombreEstadoCita_" + li_i,Utilidades.getNombreEstadoCita(ac_con,  Utilidades.convertirAEntero(ldb_reservado.get("codigoestadocita")+"")));
					acf_form.setItemSeleccionado("codigoEstadoLiquidacion_" + li_i, ls_estadoLiquidacion);
					acf_form.setItemSeleccionado("nombreEstadoLiquidacion_" + li_i, Utilidades.getNombreEstadoLiquidacion(ac_con, ls_estadoLiquidacion));
					acf_form.setItemSeleccionado("codigoUnidadConsulta_" + li_i,ldb_reservado.get("codigounidadconsulta")+"");
					acf_form.setItemSeleccionado("codigoCentroAtencion_" + li_i,ldb_reservado.get("codigocentroatencion")+"");
					acf_form.setItemSeleccionado("fecha_" + li_i,UtilidadFecha.conversionFormatoFechaAAp((String)ldb_reservado.get("fecha")));
					acf_form.setItemSeleccionado("horaFin_" + li_i, (String)ldb_reservado.get("horafin") );
					acf_form.setItemSeleccionado("horaInicio_" + li_i, (String)ldb_reservado.get("horainicio"));
					acf_form.setItemSeleccionado("nombreConsultorio_" + li_i, (String)ldb_reservado.get("nombreconsultorio"));
					acf_form.setItemSeleccionado("nombreMedico_" + li_i,(String)ldb_reservado.get("nombremedico"));
					acf_form.setItemSeleccionado("nombreUnidadConsulta_" + li_i,(String)ldb_reservado.get("nombreunidadconsulta"));
					acf_form.setItemSeleccionado("nombreCentroAtencion_" + li_i,(String)ldb_reservado.get("nombrecentroatencion"));
					acf_form.setItemSeleccionado("ocupacionMedica_" + li_i, ldb_reservado.get("ocupacionmedica")+"");
					acf_form.setItemSeleccionado("fechaSolicitada_" + li_i, UtilidadFecha.conversionFormatoFechaAAp((String)ldb_reservado.get("fechasolicitada")));
					acf_form.setItemSeleccionado("planControl_" + li_i, "");
					acf_form.setItemSeleccionado("tipoCita_" + li_i, "R");
					acf_form.setItemSeleccionado("validarCitaFecha_" + li_i, new Boolean(false) );
					acf_form.setItemSeleccionado("ordenAmb_" + li_i, ldb_reservado.get("ordenamb")+"");
					acf_form.setItemSeleccionado("servOrdenAmb_" + li_i, ldb_reservado.get("servordenamb")+"");
					//Se carga los centros de costo propios para cada agenda
					Cita cita = new Cita ();
					
					acf_form.setItemSeleccionado("mapaCentrosCosto_"+li_i, (HashMap)cita.consultarCentrosCostoXUnidadDeConsulta(ac_con, Utilidades.convertirAEntero(ldb_reservado.get("codigoagenda")+""), Utilidades.convertirAEntero( ldb_reservado.get("codigocentroatencion")+""), usuario.getCodigoInstitucionInt(), Utilidades.convertirAEntero(ldb_reservado.get("codigounidadconsulta")+"")));
					
					
					//Se consultan los servicios de la cita
					HashMap mapaServicios = Cita.consultarServiciosCita(ac_con, ldb_reservado.get("codigo").toString(), usuario.getCodigoInstitucionInt());
					
					acf_form.setItemSeleccionado("numServicios_" + li_i, mapaServicios.get("numRegistros"));
					
					for(int j=0;j<Integer.parseInt(mapaServicios.get("numRegistros").toString());j++)
					{
						if(mapaServicios.get("estadoServicio_"+j).toString().equals(ConstantesIntegridadDominio.acronimoEstadoActivo)&&
								mapaServicios.get("numeroSolicitud_"+j).toString().equals(""))
						{
							acf_form.setItemSeleccionado("codigoServicio_"+li_i+"_"+j, mapaServicios.get("codigoServicio_"+j));
							acf_form.setItemSeleccionado("codigoCentroCosto_"+li_i+"_"+j, mapaServicios.get("codigoCentroCosto_"+j));
							acf_form.setItemSeleccionado("observaciones_"+li_i+"_"+j, mapaServicios.get("observaciones_"+j));
							acf_form.setItemSeleccionado("nombreServicio_"+li_i+"_"+j, mapaServicios.get("nombreServicio_"+j));
							acf_form.setItemSeleccionado("codigoSexo_"+li_i+"_"+j, mapaServicios.get("codigoSexo_"+j));
							acf_form.setItemSeleccionado("esPos_"+li_i+"_"+j, mapaServicios.get("esPos_"+j));
							acf_form.setItemSeleccionado("codigoEspecialidad_"+li_i+"_"+j, mapaServicios.get("codigoEspecialidad_"+j));
							acf_form.setItemSeleccionado("tieneCondiciones_"+li_i+"_"+j, mapaServicios.get("tieneCondiciones_"+j));
							acf_form.setItemSeleccionado("numeroAutorizacion_"+li_i+"_"+j, "");
							acf_form.setItemSeleccionado("numeroSolicitud_"+li_i+"_"+j, "");
							//pk de servicios cita
							acf_form.setItemSeleccionado("codigoServicioCita_"+li_i+"_"+j,  mapaServicios.get("codigo_"+j));
							acf_form.setItemSeleccionado("convenio_"+li_i+"_"+j,  mapaServicios.get("convenio_"+j));
						}
					}
					
				}
				
			}
			
			acf_form.setItemSeleccionado("numRegistros", coleccionReservados.size());
		}
		
	}

	/** Carga un paciente en la sesiï¿½n */
	private void cargarPacienteSesion(
		HttpServletRequest	ahsr_request,
		CitaForm			acf_form,
		Connection			ac_con
	)
	{
		@SuppressWarnings("unused")
		ActionErrors	errores;
		HttpSession		lhs_sesion;
		Observable		lo_observable;
		PersonaBasica	lpb_paciente;
		String			ls_aux;
		String			ls_nidPaciente;
		String			ls_tidPaciente;

		/* Intentar obtener el cï¿½digo del paciente */
		ls_aux			= acf_form.getTipoIdentificacionPaciente();
		ls_nidPaciente	= acf_form.getNumeroIdentificacionPaciente();
		ls_tidPaciente	= ls_aux.split("-")[0];

		/* Obtener los datos de sesiï¿½n */
		lhs_sesion		= ahsr_request.getSession();
		lo_observable	= (Observable)lhs_sesion.getServletContext().getAttribute("observable");
		lpb_paciente	= (PersonaBasica)lhs_sesion.getAttribute("pacienteActivo");
		
		logger.info("TIPO IDENTIFICACIO PACIENTE FORM=> "+ls_tidPaciente+"-"+ls_nidPaciente);
		logger.info("TIPO IDENTIFICACIO PACIENTE SESSION=> "+lpb_paciente.getCodigoTipoIdentificacionPersona() + "-" +
				lpb_paciente.getNumeroIdentificacionPersona());
		
		
		try
		{
			/* Realizar esot solo si se ha modificado la identificaciï¿½n del paciente */
			errores = new ActionErrors();
			if(
				acf_form.getCodigoPaciente() < 0 ||
				!(
					lpb_paciente.getCodigoTipoIdentificacionPersona() + "-" +
					lpb_paciente.getNumeroIdentificacionPersona()
				).equals(ls_tidPaciente + "-" + ls_nidPaciente)
			)
			{
				/* Eliminar la informaciï¿½n del paciente en sesiï¿½n */
				lpb_paciente.clean();

				if(ls_tidPaciente.length() > 0 && ls_nidPaciente.length() > 0)
				{
					int li_codigoPaciente;

					li_codigoPaciente =
						UtilidadValidacion.buscarCodigoPaciente(
							ac_con, ls_tidPaciente, ls_nidPaciente
						);
					
					logger.info("resultado busqueda del codigo: "+li_codigoPaciente);
					
					//Se verifica si el paciente estï¿½ muerto
					if(li_codigoPaciente>0&&UtilidadValidacion.esPacienteMuerto(ac_con, li_codigoPaciente))
						li_codigoPaciente = 0;
					
					/* Establecer el cï¿½digo del paciente en la forma */
					acf_form.setCodigoPaciente(li_codigoPaciente);
					if(li_codigoPaciente > 0)
					{
						/* Cargar el paciente en sesiï¿½n */
						TipoNumeroId	ltni_id;
						UsuarioBasico	lub_usuario;

						ltni_id		= new TipoNumeroId(ls_tidPaciente, ls_nidPaciente);
						lub_usuario	= (UsuarioBasico)lhs_sesion.getAttribute("usuarioBasico");

						lpb_paciente.cargar(ac_con, ltni_id);
						lpb_paciente.cargarPaciente2(
							ac_con, ltni_id, lub_usuario.getCodigoInstitucion(),lub_usuario.getCodigoCentroAtencion()+""
						);
						
					}
				}
				
				/* Establecer el cï¿½digo del sexo del paciente */
				acf_form.setCodigoSexoPaciente(lpb_paciente.getCodigoSexo() );

				/* Registrar este paciente como Observador */
				if(lo_observable != null)
				{
					lpb_paciente.setObservable(lo_observable);
					lo_observable.addObserver(lpb_paciente);
				}
			}
		}
		catch(SQLException lsq_e)
		{
			if(logger.isDebugEnabled() )
				logger.debug("Error al cargar paciente en sesiï¿½n");
		}
	}

	/**
	* Cerrar una conexiï¿½n con una fuente de datos
	* @param ac_con Conexiï¿½n con la fuente de datos a cerrar
	* @throws SQLException
	*/
	private void cerrarConexion(Connection con)
	{
		/*try
		{
			UtilidadBD.finalizarTransaccion(con);
			if(con != null && !con.isClosed() )
	            UtilidadBD.closeConnection(con);
		}
		catch(SQLException e)
		{
			logger.error("Error al cerrar la conexion: "+e);
		}*/
		}

	/** Elimina un item de agenda de la lista para asignar / reservar cita 
	 *
	 */
	private ActionForward eliminarAgenda(ActionMapping aam_mapping,CitaForm acf_form,int ai_estado, Connection con)throws Exception
	{
		/* Establecer el estado */
		if(ai_estado == ii_ELIMINAR_AGENDA_ASIGNACION)
			acf_form.setEstado("validarListadoAsignar");
		else if(ai_estado == ii_ELIMINAR_AGENDA_RESERVA)
			acf_form.setEstado("validarListadoReservar");

		/* Eliminar el item seleccionado el ï¿½tem de agenda */
		acf_form.eliminarItemSeleccionado();

		//Eliminamos del mapa la cita seleccionada
		int index=Integer.parseInt(acf_form.getMapaCodCentroCosto("numRegistros").toString());
		acf_form.getMapaCodCentroCosto().remove("codigo_"+acf_form.getCodigoAgenda());
		acf_form.getMapaCodCentroCosto().remove("codigocentrocosto_"+acf_form.getCodigoAgenda());
		acf_form.getMapaCodCentroCosto().remove("nombrecentrocosto_"+acf_form.getCodigoAgenda());
		acf_form.getMapaCodCentroCosto().remove("codigoviaingreso_"+acf_form.getCodigoAgenda());
		acf_form.getMapaCodCentroCosto().remove("nombreviaingreso_"+acf_form.getCodigoAgenda());
		acf_form.setMapaCodCentroCosto("numRegistros",index-1+"");


		
		
		cerrarConexion(con);
		
		return aam_mapping.findForward("cita");
	}

	/** Retorna la pï¿½gina de error, con un mensaje para el usuario */
	private ActionForward error(ActionMapping aam_mapping,HttpServletRequest ahsr_request,String ls_error,boolean lb_parametrizado)
	{
		/* Determinar que como se debe visualizar el error */
		ahsr_request.setAttribute(lb_parametrizado ? "codigoDescripcionError" : "descripcionError",ls_error);

		/* Obtener la pï¿½gina de errores */
		return aam_mapping.findForward("paginaError");
	}	
	
	/**
	 * 
	 * @param mapping
	 * @param citaForm
	 * @param request
	 * @param con
	 * @param paciente
	 * @param usuario 
	 * @return
	 */
	private ActionForward cerrarGuardarIngreso(ActionMapping mapping, CitaForm citaForm, HttpServletRequest request, Connection con, PersonaBasica paciente, UsuarioBasico usuario)throws Exception {
		
		if (UtilidadesFacturacion.puedoCerrarIngreso(con,paciente.getCodigoIngreso()))
		{
			int codpaciente=ConstantesBD.codigoNuncaValido;
			IngresoGeneral.actualizarEstadoIngreso(con, paciente.getCodigoIngreso()+"", ConstantesIntegridadDominio.acronimoEstadoCerrado, usuario.getLoginUsuario());
			Cuenta cuenta = new Cuenta();
			int cerrarCuenta = cuenta.cambiarEstadoCuentaNoTransaccional(con, paciente.getCodigoCuenta(), ConstantesBD.codigoEstadoCuentaCerrada);
			logger.info("\n====>Cerrar Cuenta: "+cerrarCuenta);
			paciente.setCodigoPersona(codpaciente);
	        paciente.cargar(con, codpaciente);
	        paciente.cargarPaciente(con, codpaciente, usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
	
		}
		IngresoGeneral.actualizarControlPostOperatorioCx(con, paciente.getCodigoIngreso()+"", ConstantesBD.acronimoSi, usuario.getLoginUsuario());
		UtilidadBD.finalizarTransaccion(con);
		return this.resumenImpresionVariasCitas(mapping, request, "asignar",citaForm);
		//return mapping.findForward("cita");
	}

	/**
	 * 
	 * @param mapping
	 * @param citaForm
	 * @param request
	 * @param con
	 * @param paciente
	 * @param usuario 
	 * @return
	 */
	private ActionForward guardarIngreso(ActionMapping mapping, CitaForm citaForm, HttpServletRequest request, Connection con, PersonaBasica paciente, UsuarioBasico usuario)throws Exception {
		IngresoGeneral.actualizarControlPostOperatorioCx(con, paciente.getCodigoIngreso()+"", ConstantesBD.acronimoSi, usuario.getLoginUsuario());
		UtilidadBD.finalizarTransaccion(con);
		return this.resumenImpresionVariasCitas(mapping, request, "asignar",citaForm);
		//return mapping.findForward("cita");
	}
	
	
	/**
	 * Metodo implementado para consultar las finalidades de los servicios de procedimientos
	 * @param mapping
	 * @param citaForm
	 * @param request
	 * @param con
	 * @return
	 */
	private ActionForward verificarFinalidad(ActionMapping mapping, CitaForm citaForm, HttpServletRequest request, Connection con, int institucion)
	{
		ArrayList<HashMap<String, Object>> finalidades = new ArrayList<HashMap<String,Object>>();
		
		//for(int i=0; i<Utilidades.convertirAEntero(citaForm.getMapaServicios("numRegistros")+""); i++)
		//{
			
		logger.info("\nPOSICION SELECCIONADA: "+citaForm.getPosServicioUnidad());
		logger.info("\nTIPO SERVICIO SELECCIONADO UNIDAD DE AGENDA: "+citaForm.getMapaServicios("tipoServicio_"+citaForm.getPosServicioUnidad()+""));
		
			if(citaForm.getMapaServicios("tipoServicio_"+citaForm.getPosServicioUnidad()+"").equals(ConstantesBD.codigoServicioProcedimiento+"") || citaForm.getMapaServicios("tipoServicio_"+citaForm.getPosServicioUnidad()+"").equals(ConstantesBD.codigoServicioNoCruentos+""))
			{
				finalidades = Utilidades.obtenerFinalidadesServicio(con, Utilidades.convertirAEntero(citaForm.getMapaServicios("codigoServicio_"+citaForm.getPosServicioUnidad()).toString()+""), institucion);
				citaForm.setItemSeleccionado("finalidades_"+citaForm.getPosCita()+"_"+citaForm.getTempNumServicios(), finalidades);
			}
			else
			{
				citaForm.setItemSeleccionado("finalidades_"+citaForm.getPosCita()+"_"+citaForm.getTempNumServicios(), finalidades);
			}
		//}
		
		
		logger.info("\n\nMAPA ITEM SELEECCIONADOS: "+citaForm.getItemSeleccionado("finalidades_"+citaForm.getPosCita()+"_"+citaForm.getTempNumServicios()));
		
		
		// Evaluar cobertura para el servicio seleccionado
		
		
		return mapping.findForward("busquedaServicios");
	}


	/**
	 * Mï¿½todo implementado para iniciar la busqueda avanzada del paciente en la reserva de cita
	 * @param con
	 * @param citaForm
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ActionForward accionIniciarBusquedaAvanzada(Connection con, CitaForm citaForm, ActionMapping mapping) 
	{
		citaForm.setBusquedaAvanzada(new HashMap());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("iniciarBusquedaAvanzada");
	}

	/**
	 * Mï¿½todo que consulta las condiciones para la toma del servicio de la cita
	 * @param con
	 * @param citaForm
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionConsultarCondicionesServicio(Connection con, CitaForm citaForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		//se toma el servicio al cual se le desea consultar las condiciones para la toma
		int codigoServicio = Integer.parseInt(citaForm.getServicioTemporal());
		
		//Se consultan las condiciones para la toma del servicio
		citaForm.setCondicionesToma(CondicionesXServicios.obtenerCondicionesTomaXServicio(con, codigoServicio, usuario.getCodigoInstitucionInt()));
		
		cerrarConexion(con);
		return mapping.findForward("condicionesServicio");
	}

	/**
	 * Mï¿½todo implementado para realizar la eliminaciï¿½n de un servicio de la cita
	 * @param con
	 * @param citaForm
	 * @param response
	 * @return
	 */
	private ActionForward accionEliminarServicio(Connection con, CitaForm citaForm, HttpServletResponse response) 
	{
		logger.info("\n entre a accionEliminarServicio citas antes de eliminar--> "+citaForm.getItemsSeleccionados() +"pos1 --> "+citaForm.getPosCita()+" pos2 -->"+citaForm.getPosServicio());
		
		Cita.eliminarServicioCita(
				con, 
				citaForm.getItemSeleccionado("codigoServicioCita_"+citaForm.getPosCita()+"_"+citaForm.getPosServicio())+"");
		
		//se elimina el servicio del mapa
		citaForm.getItemsSeleccionados().remove("codigoServicio_"+citaForm.getPosCita()+"_"+citaForm.getPosServicio());
		
		cerrarConexion(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write("<respuesta>");
	        response.getWriter().write("<pos-cita>"+citaForm.getPosCita()+"</pos-cita>");
	        response.getWriter().write("<pos-servicio>"+citaForm.getPosServicio()+"</pos-servicio>");
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionEliminarServicio: "+e);
		}
		return null;
		
	}

	/**
	 * Mï¿½todo que realiza la busqueda de los servicios de la unidad de consulta de una cita
	 * @param con
	 * @param mapping
	 * @param citaForm
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionAdicionarServicio(
			Connection con, 
			ActionMapping mapping, 
			CitaForm citaForm, 
			UsuarioBasico usuario,
			PersonaBasica paciente,
			boolean filtrarServicios) 
	{
		
		// Cuando esta asociado a una orden ambulatoria se filtra el servicio
		if(citaForm.getItemsSeleccionados().containsKey("ordenAmb_"+citaForm.getPosCita())  && !citaForm.getItemSeleccionado("ordenAmb_"+citaForm.getPosCita()).toString().equals("")){
			citaForm.setIndicativoOrdenAmbulatoria(true);
			citaForm.setServicioAmbulatorioPostular(citaForm.getItemSeleccionado("servOrdenAmb_"+citaForm.getPosCita())+"");
		} 
		
		if(paciente.getCodigoPersona() > 0 
				&& filtrarServicios)
		{
			//Se consultan los servicios de la unidad de consulta seleccionada	
			citaForm.setMapaServicios(RegistroUnidadesConsulta.obtenerServiciosUnidadConsulta(
				con, 
				citaForm.getCodigoUnidadConsulta()+"", 
				//VERIFICACION DEL FLUJO DE ORDENES AMBULATORIAS
				citaForm.isIndicativoOrdenAmbulatoria()?citaForm.getServicioAmbulatorioPostular():"",
				usuario.getCodigoInstitucionInt(),
				true,
				paciente.getCodigoPersona()
				));
		}
		else
		{
			//Se consultan los servicios de la unidad de consulta seleccionada	
			citaForm.setMapaServicios(RegistroUnidadesConsulta.obtenerServiciosUnidadConsulta(
				con, 
				citaForm.getCodigoUnidadConsulta()+"", 
				//VERIFICACION DEL FLUJO DE ORDENES AMBULATORIAS
				citaForm.isIndicativoOrdenAmbulatoria()?citaForm.getServicioAmbulatorioPostular():"",
				usuario.getCodigoInstitucionInt(),
				false,
				ConstantesBD.codigoNuncaValido
				));
		}
		
		cerrarConexion(con);
		return mapping.findForward("busquedaServicios");
	}

	/** Inicia la forma de asignaciï¿½n y reserva de citas y establece su estado de flujo */
	private ActionForward iniciarCita(ActionMapping mapping,CitaForm forma,HttpServletRequest request,int estado,Connection con, Cita mundoCita)throws Exception
	{
		int				codigoPaciente;
		PersonaBasica	paciente;
		UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		/* Iniciar el flujo de reserva/ asignaciï¿½n de citas */
		forma.reset();
		forma.setMapaCodCentroCosto("numRegistros", 0);
		forma.setMapaCentrosCosto("numRegistros", 0);
		forma.setOcultarEncabezado(request.getParameter("ocultar"));
		forma.setMantenerPacienteCargado(UtilidadTexto.getBoolean(request.getParameter("mantenerPacienteCargado")));
		forma.setFiltrarAgendasXServicio(UtilidadTexto.getBoolean(request.getParameter("filtrarAgendasXServicio")));
		forma.setIndicativoOrdenAmbulatoria(UtilidadTexto.getBoolean(request.getParameter("indicativoOrdenAmbulatoria")));
		
		if(forma.isFiltrarAgendasXServicio())
			forma.setServicioAmbulatorioPostular(request.getParameter("servicioAmbulatorioPostular")+"");
		
		//-----Se asigna el centro de atenciï¿½n del usuario por defecto cuando empieza ---------//
		forma.setCentroAtencion(usuario.getCodigoCentroAtencion());

		
		/* Obtener el paciente actual sesiï¿½n */
		paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		codigoPaciente = paciente.getCodigoPersona();
		
		if(estado == ii_PREPARAR_LISTADO_ASIGNAR)
		{
			forma.setEstado("validarListadoAsignar");
			forma.setFlujo("asignacion");
		}
		else if(estado == ii_PREPARAR_LISTADO_RESERVAR)
		{
			forma.setEstado("validarListadoReservar");
			forma.setFlujo("reserva");
			
			if(!forma.isMantenerPacienteCargado())
				cargarPacienteSesion(request, forma, null);
			
			/*if(forma.isCargarInfoPacienteSesion()){
				forma.setNumeroIdentificacionPaciente(paciente.getNumeroIdentificacionPersona());
				forma.setTipoIdentificacionPaciente(paciente.getCodigoTipoIdentificacionPersona()+"-"+paciente.getTipoIdentificacionPersona(false)+"-"+ValoresPorDefecto.getValorFalseParaConsultas());
			}*/
		}
		
		if(estado == ii_PREPARAR_LISTADO_ASIGNAR)
		{
			ActionForward validar;

			validar = validarPacienteAsignacion(mapping, request, paciente,usuario);

			if(validar != null)
			{
				cerrarConexion(con);
				return validar;
			}
		}

		if( (codigoPaciente = paciente.getCodigoPersona() ) > 0)
		{
			/* Obtener el cï¿½digo del paciente */
			forma.setCodigoPaciente(codigoPaciente);
						
			if(estado == ii_PREPARAR_LISTADO_ASIGNAR)
			{
				forma.setCuentaPaciente(paciente.getCodigoCuenta() );
				
				//******************* Capturar todos los centros de atencion y unidades de agenda autorizados para el usuario
				// Capturar los centros de atencion si se ha seleccionado la opcion todos
				String centrosAtencion=UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario.getLoginUsuario(), ConstantesBD.codigoNuncaValido).get("todos").toString();
						
				// Capturar las unidades de agenda si se ha seleccionado la opcion todos
				String unidadesAgenda=UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, usuario.getLoginUsuario(), ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral).get("todos").toString();
				//**********************************************************************************************************

				/* El paciente cumple todas los condiciones para asignaciï¿½n de citas */
				cargarCitasReservadas(forma, con, usuario,centrosAtencion,unidadesAgenda);
			}

			/* Establecer el cï¿½digo del sexo del paciente */
			forma.setCodigoSexoPaciente(paciente.getCodigoSexo() );

			/* Obtener la identificaciï¿½n del paciente */
			forma.setNumeroIdentificacionPaciente(paciente.getNumeroIdentificacionPersona());
			forma.setTipoIdentificacionPaciente(paciente.getCodigoTipoIdentificacionPersona() + "-" +paciente.getTipoIdentificacionPersona(false)+"-"+ValoresPorDefecto.getValorFalseParaConsultas());
			
			
			if(forma.getNumeroItemsSeleccionados()>0)
			{
				forma.setMapaCentrosCosto(mundoCita.consultarCentrosCostoXUnidadDeConsulta(con, Integer.parseInt(forma.getIm_agenda("codigoAgenda_0")+""), Integer.parseInt(forma.getIm_agenda("codigoCentroAtencion_0")+""), usuario.getCodigoInstitucionInt(), Integer.parseInt(forma.getIm_agenda("codigoUnidadConsulta_0")+"")));
				int index=forma.getNumeroItemsSeleccionados();
				forma.setMapaCodCentroCosto("numRegistros", index+"");
			}
		
			//***********************************************
			// validar si el paciente posee citas incumplidad 
			String [] parametrosModulo = {
					ConstantesValoresPorDefecto.nombreInstitucionManejaMultasPorIncumplimiento,
					ConstantesValoresPorDefecto.nombreBloqueaCitasReservaAsignReprogPorIncump,
					ConstantesValoresPorDefecto.nombreFechaInicioControlMultasIncumplimientoCitas
					};
			if(mundoCita.validarEstadoCitasPaciente(con, institucion, paciente, usuario, ConstantesBD.codigoModuloConsultaExterna, parametrosModulo)){
				// Existen citas Incumplidas
				forma.setCitasIncumpl(ConstantesBD.acronimoSi);
				forma.setCitasIncumplidas(mundoCita.getCitasIncumplidas());
			}else
				forma.setCitasIncumpl(ConstantesBD.acronimoNo);
			//***********************************************
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("cita");
	}

	/** Incluye un item de agenda para asignar / reservar cita */
	@SuppressWarnings("deprecation")
	private ActionForward incluirAgenda(ActionMapping mapping, CitaForm forma, HttpServletRequest request,int estado,Connection lc_con, Cita mundoCita )throws Exception
	{
		ActionErrors errores ;
		
		/* Establecer el estado */
		if(estado == ii_INCLUIR_AGENDA_ASIGNACION)
			forma.setEstado("validarListadoAsignar");
		else if(estado == ii_INCLUIR_AGENDA_RESERVA)
			forma.setEstado("validarListadoReservar");

		PersonaBasica paciente =(PersonaBasica) request.getSession().getAttribute("pacienteActivo");
		UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		
		/* Adicionar el ï¿½tem de agenda */
		errores = forma.setItemSeleccionado(usuario);
		
		
		logger.info("\n\n  5  ii_INCLUIR_AGENDA_ASIGNACION \n"+errores);
		//Cargamos los centros de costo para un usuario
		
		
		//------------------------------------------------------------
		/**
		 * Victor Gomez
		 * Verificar los estados de la cita del paciente y segun ese estado
		 * preguntar si el usuario que esta logiado tiene los permisos 
		 * del caso para reservar o asignar citas con multas
		 */
		logger.info("Realizada Verificacion >>>>>>"+mundoCita.isRealizadaVerificacion());
		logger.info("Estado Vailidacion Paciente >>>>>>"+mundoCita.isEstadoValidarCitasPaciente());
		logger.info("Centro de Atencion >>>>>>"+forma.getCentroAtencion());
		logger.info("Codigo Agenda >>>>>>"+forma.getCodigoAgenda());
		logger.info("Cita codigo de unidad de Consulta >>>>>>> "+mundoCita.getUnidadConsulta().getCodigo());
		
		
		
		logger.info("Usuario >>>>>>"+usuario.getLoginUsuario());
		if(!mundoCita.isRealizadaVerificacion()){
			//verificar el estado de las citas
			String [] parametrosModulo = {
					ConstantesValoresPorDefecto.nombreInstitucionManejaMultasPorIncumplimiento,
					ConstantesValoresPorDefecto.nombreBloqueaCitasReservaAsignReprogPorIncump,
					ConstantesValoresPorDefecto.nombreFechaInicioControlMultasIncumplimientoCitas
					};
			if(mundoCita.validarEstadoCitasPaciente(lc_con, institucion, paciente, usuario, ConstantesBD.codigoModuloConsultaExterna, parametrosModulo)){
				// verificar si el  usuario que esta en sesiï¿½n esta autorizado para la reservar, asignar o reprogrmas
				// citas aunque el paciente posea multas por incumplimiento de citas
				logger.info("al verificar se encontraron citas  incumplidad");
				if(UtilidadesConsultaExterna.esActividadAurtorizada(lc_con, forma.getCodigoAgenda(), 
						ConstantesBD.codigoActividadAutorizadaAutorizarCitasPacientesConMulta, 
						usuario.getLoginUsuario(), 
						forma.getCentroAtencion(),false)){
					logger.info("el usuario esta autorizado para asignar citas asi el paciente posea citas incumplidas");
					// aqui se debe preguntar si desa autorizar la reserva o asignacion de la cita
					forma.setPreguntarAutorizacion(ConstantesBD.acronimoSi);
					forma.setPreguntarAutorizacionAux(ConstantesBD.acronimoSi);
				}else{
					// se cancela el proceso de asignar o reservar cita.
					logger.info("Se Cancela el Proceso de Asignar o Reservar Cita");
					forma.setPreguntarAutorizacionAux(ConstantesBD.acronimoNo);
				}
			}else{
				forma.setPreguntarAutorizacion(ConstantesBD.acronimoNo);
				forma.setPreguntarAutorizacionAux(ConstantesBD.acronimoSi);
			}
		}else{
			if(mundoCita.isEstadoValidarCitasPaciente()){
				if(UtilidadesConsultaExterna.esActividadAurtorizada(lc_con, forma.getCodigoAgenda(), 
						ConstantesBD.codigoActividadAutorizadaAutorizarCitasPacientesConMulta, 
						usuario.getLoginUsuario(), 
						forma.getCentroAtencion(),false)){
					logger.info("el usuario esta autorizado para asignar citas asi el paciente posea citas incumplidas");
					// aqui se debe preguntar si desa autorizar la reserva o asignacion de la cita
					forma.setVerificarEstCitasPac(ConstantesBD.acronimoSi);
					forma.setPreguntarAutorizacion(ConstantesBD.acronimoSi);
				}else{
					// se cancela el proceso de asignar o reservar cita.
					logger.info("Se Cancela el Proceso de Asignar o Reservar Cita");
				}
			}else{
				forma.setPreguntarAutorizacion(ConstantesBD.acronimoNo);
				forma.setPreguntarAutorizacionAux(ConstantesBD.acronimoSi);
			}
		}
			
		//------------------------------------------------------------
		
		
		if(estado == ii_INCLUIR_AGENDA_RESERVA){
			forma.setPreguntarAutorizacionAux(ConstantesBD.acronimoSi);
			logger.info("pregunta auxiliar >>>>>> "+forma.getPreguntarAutorizacionAux());
			logger.info("ii_INCLUIR_AGENDA_RESERVA");
		}
		
		
		forma.setMapaCentrosCosto(mundoCita.consultarCentrosCostoXUnidadDeConsulta(lc_con, forma.getCodigoAgenda(), forma.getCentroAtencion(), usuario.getCodigoInstitucionInt(), forma.getCodigoUnidadConsulta()));
		logger.info("\n\n  6  ii_INCLUIR_AGENDA_ASIGNACION \n");
		if(errores != null && !errores.isEmpty() )
		{
			logger.info("\n\n  7  ii_INCLUIR_AGENDA_ASIGNACION \n");
			saveErrors(request, errores);
		}
		else
		{
			
			int index=Integer.parseInt(forma.getMapaCodCentroCosto("numRegistros").toString());
			
			index=index+1;
			forma.setMapaCodCentroCosto("numRegistros", index+"");

		}
		this.cerrarConexion(lc_con);
		return mapping.findForward("cita");
	}

	/**
	 * Lista la agenda de consulta disponible para reserva / asiganciï¿½n de citas
	 * @param mapping
	 * @param form
	 * @param request
	 * @param con
	 * @param esReserva
	 * @return Forward al listado de citas
	 * @throws Exception
	 */
	private ActionForward listarAgenda(ActionMapping mapping, CitaForm form, HttpServletRequest request, Connection con, boolean esReserva, int actividad)throws Exception
	{
		try
		{
			Agenda	agenda = new Agenda();
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			/* Copiar la informaciï¿½n del formulario a la agenda */
			PropertyUtils.copyProperties(agenda, form);
			
			@SuppressWarnings("unused")
			String servicio="";
			if(form.isFiltrarAgendasXServicio()){
				servicio = form.getServicioAmbulatorioPostular();
			}
			
			form.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario.getLoginUsuario(), actividad));
			form.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, usuario.getLoginUsuario(), form.getCentroAtencion(), actividad,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));
			
			// Capturar los centros de atencion si se ha seleccionado la opcion todo
			if (form.getCentroAtencion()==ConstantesBD.codigoNuncaValido){
				agenda.setCentrosAtencion(form.getCentrosAtencionAutorizados().get("todos").toString());
			}
			// Capturar las unidades de agenda si se ha seleccionado la opcion todos
			if (form.getCodigoUnidadConsulta()==ConstantesBD.codigoNuncaValido){
				Utilidades.imprimirMapa(form.getUnidadesAgendaAutorizadas());
				agenda.setUnidadesAgenda(form.getUnidadesAgendaAutorizadas().get("todos").toString());
			}
			
			
			/* Obtener los ï¿½tems de agenda de consulta a listar */
			logger.info("VALOR DE DISPONBILES=> "+form.isSoloDisponibles());
			form.setItems(agenda.listarAgendaDisponible(con, form.getCodigoPaciente(), form.getCodigoSexoPaciente(), usuario.getCodigoInstitucionInt(), esReserva,form.isSoloDisponibles()));
			
			/* Cerrar la conexion a base de datos */
			cerrarConexion(con);

			return mapping.findForward("listarAgenda");
		}
		catch(SQLException lse_e)
		{
			/* Cerrar la conexion a base de datos */
			cerrarConexion(con);

			if(logger.isDebugEnabled() )
			{
				logger.debug("No se pudo generar el listado de agenda de consulta");
			}

			lse_e.printStackTrace();
			return error(mapping, request, "errors.problemasBd", true);
		}
	}
 
	/** Reserva para citas los items de agenda de consulta incluidos en la lista de reserva 
	 * @param usuario */
	@SuppressWarnings({ "deprecation", "rawtypes" })
	private ActionForward reservar(
		ActionMapping		mapping,
		CitaForm			forma,
		HttpServletRequest	request,
		Connection con, 
		UsuarioBasico usuario,
		boolean entroAutorizacionIngEven
	)throws Exception
	{
		int i = 0, tamanio=0 ,contc=0, contNoRes=0;
		boolean indicador = true;
		boolean indicadorCitas = false;
		
		try
		{
			//logger.info("CODIGO PACIENTE 0=> "+forma.getCodigoPaciente());
			/* Cargar al paciente en sesiï¿½n si este es vï¿½lido */
			if(!forma.isMantenerPacienteCargado())
				cargarPacienteSesion(request, forma, con);
			forma.getLisMensaje().clear();
			forma.setEstado("validarListadoReservar");
			//logger.info("CODIGO PACIENTE => "+forma.getCodigoPaciente());
			/* Reservar las citas solicitadas */
			if(forma.getCodigoPaciente() > 0)
			{
				//***************SE VERIFICA SI SE DEBE VALIDAR LA VIGENCIA EN LA COMPROBACIï¿½N DE DERECHOS CAPITADOS *******************************				
				if(ValoresPorDefecto.getValoresDefectoComprobacionDerechosCapitacionObligatoria(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi) 
						&& !entroAutorizacionIngEven)
				{
					forma.setAutorizacionIngEvento(UtilidadValidacion.esPacienteCapitadoVigente(con,UtilidadFecha.getFechaActual(),forma.getNumeroIdentificacionPaciente(),forma.getTipoIdentificacionPaciente().split("-")[0].trim()));
					
					//Valida que exista el paciente dentro de la vigencia capitada
					if(!forma.getAutorizacionIngEvento().isActivo())
					{
						logger.info("retormo ingreso codigo");
						forma.getAutorizacionIngEvento().setCodigo("");
						return mapping.findForward("autoIngEvento");
					}
				}
				//************************************************************************************************************************************
				
				ActionErrors	errores;
				Boolean			lb_validarCitaFecha;
				Cita			lc_cita;
				int				li_codigoCita;
				int				li_estadoCita;
				@SuppressWarnings("unused")
				int				li_sexo;
				int				diasRestriccionCitasIncumplidas = Utilidades.convertirAEntero(ValoresPorDefecto.getValoresDefectoDiasRestriccionCitasIncumplidas(usuario.getCodigoInstitucionInt()));
				Integer			li_aux;
				boolean 		validacionSexo;

				errores = new ActionErrors();
				
				String guardar = ConstantesBD.acronimoNo;
				//---------------------------------------------------
				// verificar que al menos una cita sea autorizada
				if(forma.getVerificarEstCitasPac().equals(ConstantesBD.acronimoSi)){
					for( contc = 0, tamanio = forma.getNumeroItemsSeleccionados();
							contc < tamanio;
							contc++
						)
						{
							if(forma.getItemSeleccionado("guardarAuto_" + contc).toString().equals(ConstantesBD.acronimoSi))
								indicadorCitas = true;
						}
					
					
					if(forma.getUsuarioAutoriza().equals("")){
						errores.add("descripcion", new ActionMessage("errors.required","El Usuario que Autoriza "));
						indicadorCitas = false;
					}
					if(forma.getUsuarioAutoriza().length()>125){
						errores.add("descripcion",new ActionMessage("errors.maxlength","Usuario que Autoriza ","125"));
						indicadorCitas = false;
					}
					if(UtilidadCadena.tieneCaracteresEspecialesGeneral(forma.getUsuarioAutoriza())){
						errores.add("descripcion",new ActionMessage("errors.caracteresInvalidos","El Campo Usuario que Autoriza "));
						indicadorCitas = false;
					}
					
					if(forma.getMotivoAutorizacionCita().equals("")){
						errores.add("descripcion", new ActionMessage("errors.required","El Motivo de la Autorizaciï¿½n "));
						indicadorCitas = false;
					}
					if(forma.getMotivoAutorizacionCita().length()>256){
						errores.add("descripcion",new ActionMessage("errors.maxlength","Motivo de la Autorizaciï¿½n ","256"));
						indicadorCitas = false;
					}
					if(UtilidadCadena.tieneCaracteresEspecialesGeneral(forma.getMotivoAutorizacionCita())){
						errores.add("descripcion",new ActionMessage("errors.caracteresInvalidos","El Campo Motivo de la Autorizaciï¿½n "));
						indicadorCitas = false;
					}
					
				}else
				{
					indicadorCitas = true;
				}
				
				logger.info("indicardor de citas >>>>> "+indicadorCitas);
				
				//---------------------------------------------------
				if(indicadorCitas)
				{
					//**********VALIDACIï¿½N DE LA INASSITENCIA********************************
					if(diasRestriccionCitasIncumplidas>0
							&& UtilidadesConsultaExterna.pacienteConInasistencia(con, forma.getCodigoPaciente(), diasRestriccionCitasIncumplidas))
					{
						errores.add("", new ActionMessage("error.cita.reservacion.inasistencia"));
					}
					else
					//************************************************************************
					{
						//logger.info("\n\n\n**************************************************************");
						//logger.info("Numero de item seleccionados: "+forma.getNumeroItemsSeleccionados());		
						//logger.info("\n\n\n**************************************************************");
						for(
							i = 0, tamanio = forma.getNumeroItemsSeleccionados();
							i < tamanio;
							i++
						)
						{
							
							if(forma.getItemSeleccionado("guardarAuto_" + i)!=null)
								if(forma.getItemSeleccionado("guardarAuto_" + i).toString().equals(ConstantesBD.acronimoSi))
									guardar = ConstantesBD.acronimoSi;
								else
									guardar = ConstantesBD.acronimoNo;
							else
								guardar = ConstantesBD.acronimoNo;
							
							
							if(
									//1) NO TIENE CITAS INCUMPLIDAS
									!UtilidadTexto.getBoolean(forma.getVerificarEstCitasPac()) 
									||
									//2) TIENE CITAS INCUMPLIDAS PERO ESTï¿½ AUTORIZADA
									(UtilidadTexto.getBoolean(forma.getVerificarEstCitasPac())&&guardar.equals(ConstantesBD.acronimoSi))
							)
							{
								validacionSexo = true;
								
								lb_validarCitaFecha =
									(Boolean)forma.getItemSeleccionado("validarCitaFecha_" + i);
			
								li_aux =
									Utilidades.convertirAEntero(forma.getItemSeleccionado("codigoEstadoCita_" + i)+"");
			
								li_estadoCita = li_aux.intValue();
								
								//***********SE REVISA QUE TODOS LOS SERVICIOS DE LA CITA APLIQUEN CON EL SEXO DEL PACIENTE **********************
								int numServicios = Integer.parseInt(forma.getItemSeleccionado("numServicios_"+i).toString());
								for(int j=0;j<numServicios;j++)
								{							
									//Se excluyen los servicios eliminados
									if(forma.getItemSeleccionado("codigoServicio_" + i + "_" + j)!=null)
									{							
										//se toma sexo del servicio
										li_sexo =Integer.parseInt(forma.getItemSeleccionado("codigoSexo_" + i + "_" + j)+"");
										/*
										if(li_sexo!=ConstantesBD.codigoSexoTodos&&
												li_sexo != forma.getCodigoSexoPaciente())
										{
											errores.add(
													"codigoSexoPaciente",
													new ActionMessage(
														"error.cita.sexoPaciente",
														forma.getItemSeleccionado("nombreServicio_" + i + "_" + j),
														forma.getItemSeleccionado("nombreUnidadConsulta_" + i),
														forma.getItemSeleccionado("fecha_" + i)+"-"+UtilidadFecha.convertirHoraACincoCaracteres(forma.getItemSeleccionado("horaInicio_" + i).toString()),
														"reservada"
													)
												);
											validacionSexo = false;										
										}	
										*/						
										// Se evalua la cobertura del servicio						
									}
								}
								//****************************************************************************************************************
								
								
								if(validacionSexo)
								{
									/* La cita no ha sido programada */
									if(li_estadoCita == ConstantesBD.codigoEstadoCitaAProgramar)
									{
										/* Iniciar la cita */
										lc_cita = new Cita();
										
										//*********SE CARGAN LOS DATOS DE LA CITA**************************						
										/* Establecer los datos bï¿½sicos de la cita */
										li_aux = Utilidades.convertirAEntero(forma.getItemSeleccionado("codigoAgenda_" + i)+"");
										lc_cita.setCodigoAgenda(li_aux.intValue() );
										lc_cita.setCodigoPaciente(forma.getCodigoPaciente() );
										li_aux = Utilidades.convertirAEntero(forma.getItemSeleccionado("codigoUnidadConsulta_" + i)+"");
										lc_cita.setCodigoUnidadConsulta(li_aux.intValue() );
										lc_cita.setCodigoUsuario(forma.getCodigoUsuario() );
										lc_cita.setPrioridad(forma.getItemSeleccionado("prioritaria_" + i).toString());
										
										//--------------------------------------------
										// atributos para el ingreso de autorizacion de cita si se require
										if(forma.getVerificarEstCitasPac().equals(ConstantesBD.acronimoSi))
										{
											//requiereAuto_
											lc_cita.setRequiereAuto(forma.getItemSeleccionado("guardarAuto_" + i).toString());
											lc_cita.setCitasIncumpl(forma.getCitasIncumpl());
											lc_cita.setMotivoAutorizacionCita(forma.getMotivoAutorizacionCita());
											lc_cita.setUsuarioAutoriza(forma.getUsuarioAutoriza());
											lc_cita.setVerificarEstCitaPac(forma.getVerificarEstCitasPac());
										}
										//--------------------------------------------
										
										
										if(forma.getFechaSolicitada().equals(""))
											lc_cita.setIs_fechaSolicitada(forma.getItemSeleccionado("fecha_" + i).toString());
										else
											lc_cita.setIs_fechaSolicitada(forma.getFechaSolicitada());
										
										//Se carga la informacion de los servicios
										numServicios = Integer.parseInt(forma.getItemSeleccionado("numServicios_"+i).toString());
										int contador = 0;
										
										
										
										
										
			//							*******************************	CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] *************************************// 					
										
										//verificar valor definido en el campo cantidad maxima de citas control post operatorio para el convenio seleccionado
										
										
										forma.setMensaje(new ResultadoBoolean(false));
										
										HashMap mapa= new HashMap();
										
										int cantCPO=UtilidadesConsultaExterna.consultarCantidadMaximaCitasControlPostOperatorioConvenio(con, forma.getConvenio());
										
										logger.info("\n\n\n\n [Convenio] "+forma.getConvenio()+" [CantidadMaximaCitasControlPostOperatorioConvenio]  "+cantCPO);
										
										if (cantCPO>0)
										{
											
											
											int diasCPO=UtilidadesConsultaExterna.consultarDiasControlPostOperatorioConvenio(con, forma.getConvenio());	
											logger.info("\n\n\n\n [Convenio] "+forma.getConvenio()+" [DiasControlPostOperatorioConvenio]  "+diasCPO);
											// se realiza el procesos para todas las reservas.
												
											Utilidades.imprimirMapa((HashMap) forma.getIm_agenda());
											
											for(int j=0;j<numServicios;j++)
											{
												
												if (forma.getIm_agenda().containsKey("codigoEspecialidad_"+i+"_"+j))
														logger.info("\n\n\n\n [Servicio] "+forma.getIm_agenda().get("codigoEspecialidad_"+i+"_"+j).toString());
												
											if (forma.getIm_agenda().containsKey("codigoEspecialidad_"+i+"_"+j) && 
													(!forma.getIm_agenda().get("codigoEspecialidad_"+i+"_"+j).toString().equals("") || 
														!forma.getIm_agenda().get("codigoEspecialidad_"+i+"_"+j).toString().isEmpty()))
											{
												
												logger.info("\n\n\n\n [Especialidad Unidad Agenda] -->"+Integer.parseInt(forma.getIm_agenda().get("codigoEspecialidad_"+i+"_"+j).toString())+"\n\n\n\n");
												mapa=UtilidadesConsultaExterna.validarControlPostOperatorio
																	(con, 
																	forma.getCodigoPaciente(), 
																	forma.getIm_agenda().get("fecha_"+i).toString(), 
																	cantCPO, 
																	diasCPO, 
																	Integer.parseInt(forma.getIm_agenda().get("codigoEspecialidad_"+i+"_"+j).toString())
																	);
											}
											else
											{
												logger.info("\n\n [NO HACE VALIDACIONES CONTROL POST OPERATORIO] \n\n");
											}
												if(mapa.containsKey("codigopo_0"))
												{
													logger.info("\n\n\n\n\n\n [Paciente Control Post Operatorio] \n\n\n\n ");
													forma.getIm_agenda().put("solicitudpo_"+i, mapa.get("solicitud_"+0));
													forma.getIm_agenda().put("codigopo_"+i, mapa.get("codigopo_"+0));
													forma.setMensaje(new ResultadoBoolean(true," Paciente Control Post Operatorio "));
												}
											else
												{
													forma.getIm_agenda().put("solicitudpo_"+i, "null");
													forma.getIm_agenda().put("codigopo_"+i, "null");
												}
												
												//Se excluyen los servicios eliminados
												if(forma.getItemSeleccionado("codigoServicio_"+i+"_"+j)!=null)
												{
													lc_cita.setMapaServicios("codigoServicio_"+contador, forma.getItemSeleccionado("codigoServicio_"+i+"_"+j));
													lc_cita.setMapaServicios("codigoEspecialidad_"+contador, forma.getItemSeleccionado("codigoEspecialidad_"+i+"_"+j));
													lc_cita.setMapaServicios("codigoCentroCosto_"+contador, forma.getItemSeleccionado("codigoCentroCosto_"+i+"_"+j));
													lc_cita.setMapaServicios("observaciones_"+contador, forma.getItemSeleccionado("observaciones_"+i+"_"+j));
													lc_cita.setMapaServicios("codigoAgenda_"+contador, forma.getItemSeleccionado("codigoAgenda_" + i));
													lc_cita.setMapaServicios("codigoEstadoCita_"+contador, forma.getItemSeleccionado("codigoEstadoCita_" + i));
													lc_cita.setMapaServicios("fechaCita_"+contador, forma.getItemSeleccionado("fecha_" + i));
			//			*******************************	CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] *************************************//
													
													lc_cita.setMapaServicios("solicitudpo_"+contador, forma.getIm_agenda("solicitudpo_"+i));
													lc_cita.setMapaServicios("codigopo_"+contador, forma.getIm_agenda("codigopo_"+i));
													
			//			*******************************	FIN CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] *********************************//
													lc_cita.setMapaServicios("horaInicioCita_"+contador, UtilidadFecha.convertirHoraACincoCaracteres(forma.getItemSeleccionado("horaInicio_" + i).toString()));
													lc_cita.setMapaServicios("horaFinCita_"+contador, UtilidadFecha.convertirHoraACincoCaracteres(forma.getItemSeleccionado("horaFin_" + i).toString()));
													contador ++;
												}
											}
			
			
										}
										else{
										
			//		******************************* FIN CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] **********************************//
			
											
												for(int j=0;j<numServicios;j++)
												{
													//Se excluyen los servicios eliminados
													if(forma.getItemSeleccionado("codigoServicio_"+i+"_"+j)!=null)
													{
														lc_cita.setMapaServicios("codigoServicio_"+contador, forma.getItemSeleccionado("codigoServicio_"+i+"_"+j));
														lc_cita.setMapaServicios("codigoEspecialidad_"+contador, forma.getItemSeleccionado("codigoEspecialidad_"+i+"_"+j));
														lc_cita.setMapaServicios("codigoCentroCosto_"+contador, forma.getItemSeleccionado("codigoCentroCosto_"+i+"_"+j));
														lc_cita.setMapaServicios("observaciones_"+contador, forma.getItemSeleccionado("observaciones_"+i+"_"+j));
														lc_cita.setMapaServicios("codigoAgenda_"+contador, forma.getItemSeleccionado("codigoAgenda_" + i));
														lc_cita.setMapaServicios("codigoEstadoCita_"+contador, forma.getItemSeleccionado("codigoEstadoCita_" + i));
														lc_cita.setMapaServicios("fechaCita_"+contador, forma.getItemSeleccionado("fecha_" + i));
			//				*******************************	CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] *************************************//
							
														lc_cita.setMapaServicios("solicitudpo", null);
														lc_cita.setMapaServicios("codigopo", null);
														
			//				*******************************	FIN CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] *********************************//
														lc_cita.setMapaServicios("horaInicioCita_"+contador, UtilidadFecha.convertirHoraACincoCaracteres(forma.getItemSeleccionado("horaInicio_" + i).toString()));
														lc_cita.setMapaServicios("horaFinCita_"+contador, UtilidadFecha.convertirHoraACincoCaracteres(forma.getItemSeleccionado("horaFin_" + i).toString()));
														contador ++;
													}
													
													
													
												}
										}
										
										lc_cita.setMapaServicios("numRegistros", contador);
										
										/*
										 * Validar si la unidad de agenda tiene parametrizada especialidad
										 */
										for(int g=0; g<Utilidades.convertirAEntero(lc_cita.getMapaServicios("numRegistros")+""); g++){
											if(UtilidadTexto.isEmpty(lc_cita.getMapaServicios("codigoEspecialidad_"+g)+""))
												errores.add("descripcion",
														new ActionMessage(
															"error.cita.unidadAgendaSinEspecialidad",
															forma.getIm_agenda().get("nombreUnidadConsulta_" + i))
											);
										}
										logger.info("EL MAPA DE LOS SERVICIOS=> "+lc_cita.getMapaServicios());
										
										/**String observacion=String.valueOf(forma.getItemSeleccionado("observaciones_" + i));
										lc_cita.setObservaciones(observacion);
										lc_cita.setCodigoCentroCostoSolicitado(Integer.parseInt(forma.getMapaCodCentroCosto("codigocentrocosto_"+i).toString()));
										lc_cita.setCodigoServicioCita(servicio);**/
										//*******************************************************************************
										
										
										boolean hayCitaIgualFechaHora=lc_cita.validarReservaCitaFechaHora(con);
										//logger.info("\n\n\n\n**********************************************************");
										//logger.info("hayCitaIgualFechaHora: "+hayCitaIgualFechaHora);
										//logger.info("\n\n\n\n**********************************************************");
										//--Se muestra la confirmaciï¿½n si el paciente tiene citas en la misma fecha, pero no a la mismo fecha y hora
										
											
										if(
											lb_validarCitaFecha.booleanValue() &&
											lc_cita.validarReservaCitaFecha(con) && (!hayCitaIgualFechaHora)
										)
										{
											/* Cerrar la conexion a base de datos */
											cerrarConexion(con);
			
											/* Indicar que item seleccionado se debe confirmar */
											forma.setIndiceItemSeleccionado(i);
			
											/* Confirmar la reserva de la cita */
											return mapping.findForward("confirmarCita");
										}
										
										if(!hayCitaIgualFechaHora)
										{
											try
										
											{
												lc_cita.setSolPYP(forma.isSolPYP());

												
												//--Se pasan los datos del form al mundo de cita de la parte de cuenta -----//
												llenarMundoInfoCuenta(lc_cita, forma);
												
												
												li_codigoCita = lc_cita.reservarCita(con);
												
												
												indicador = true;
												if(li_codigoCita>0)
												{
													//Se actualiza la cita con la informaciï¿½n de la cuenta si alguno de los parametros es diferente de vacï¿½o
												
													
													if(lc_cita.getConvenio()!=ConstantesBD.codigoNuncaValido || lc_cita.getContrato()!=ConstantesBD.codigoNuncaValido 
														|| lc_cita.getEstratoSocial()!=ConstantesBD.codigoNuncaValido || 
														(UtilidadCadena.noEsVacio(lc_cita.getTipoAfiliado())&& !lc_cita.getTipoAfiliado().equals(ConstantesBD.codigoNuncaValido+"")))
													{
														if(lc_cita.actualizarInfoCuentaCita(con, li_codigoCita)<=0)
														{
															li_codigoCita = 0;
															indicador = false;
														}
													}
												}
												else if(li_codigoCita==-5)
												{
													errores.add("cita asignada concurrencia", new ActionMessage("errores.cita.citaYaAsignadaOReservada"));
												}
											}
											catch(Exception le_e)
											{
												li_codigoCita = -1;
												logger.info("error reservando la cita. "+le_e);
											}
				
											/* Si la cita pudo ser programada, actualizar su estado */
											if(li_codigoCita > 0)
											{
												forma.setItemSeleccionado("codigoCita_" + i, new Integer(li_codigoCita));
												request.getSession().setAttribute("codigoCita_"+i, "" + li_codigoCita);
												forma.setItemSeleccionado("codigoEstadoCita_" + i,new Integer(ConstantesBD.codigoEstadoCitaAsignada));
												
												// *********** Cambio x reserva desde orden ambulatoria
												//logger.info("INDICATIVO ORDEN AMBULATORIA DESPUES DE GENERAR LA SOLICITUD=> "+citaForm.isIndicativoOrdenAmbulatoria());
											    //logger.info("ESTADO DE LA CITA AL GENERAR LA SOLICITUD=> "+estadoCita+" = "+ConstantesBD.codigoEstadoCitaAProgramar);
											    if(forma.isIndicativoOrdenAmbulatoria())
												{
													String codigoOrden=Utilidades.obtenerCodigoOrdenAmbulatoria(con,forma.getOrdenAmbulatoria(),usuario.getCodigoInstitucionInt());
													HashMap vo=new HashMap();
													vo.put("estadoOrden",ConstantesBD.codigoEstadoOrdenAmbulatoriaSolicitada+"");
													vo.put("codigoCita",li_codigoCita);
													vo.put("numeroOrden",codigoOrden+"");
													vo.put("usuario",usuario.getLoginUsuario());
													//logger.info("Entrï¿½ por aquï¿½: "+vo);
													//esta consulta solo aplica para ordenes de tipo consulta, ya que en la generacion de la cita no se hace esta confimacion.
													if(OrdenesAmbulatorias.confirmarReservaCitaEnOrdenAmbulatoria(con,vo)<=0)
														errores.add("",new ActionMessage("errors.noSeGraboInformacion",
																"AL ACTUALIZAR EL ESTADO DE LA ORDEN AMBULATORIA "+codigoOrden+" "+
																"EN LA RESERVA DE LA CITA0 "));
													logger.info("TERMINï¿½ DE CONFIRMAR ORDEN AMBULATORIA ï¿½ES PYP?: "+forma.isSolPYP());
												}
												// *********** FIN Cambio x reserva desde orden ambulatoria
											    
											}
											else
											{
												if(!indicador)
													errores.add("descripcion",new ActionMessage("errors.notEspecific","No fue posible actualizar la cita con la Informaciï¿½n de la Cuenta"));									
												else
												{
													if(forma.getItemSeleccionado("guardarAuto_" + i)!=null&&!forma.getItemSeleccionado("guardarAuto_" + i).toString().equals(ConstantesBD.acronimoNo)){
														errores.add("codigoMedico",new ActionMessage("error.cita.noReservaAsignacionCita",forma.getItemSeleccionado("nombreUnidadConsulta_" +	i),
																forma.getItemSeleccionado("fecha_" + i),
																forma.getItemSeleccionado("horaInicio_" + i),
																"reservada"
															)
														);
													}
												}
											}
										}else{
											//logger.info("entra ha esta parte ");
											ResultadoBoolean mensaje = new ResultadoBoolean(false);
											mensaje.setDescripcion("La cita programada en la unidad de consulta "+forma.getItemSeleccionado("nombreUnidadConsulta_"+i)+" para el dï¿½a "+forma.getItemSeleccionado("fecha_" + i)+" a las "+forma.getItemSeleccionado("horaInicio_" + i)+" no pudo ser Reservada, debido a que ya se tiene otra cita para la misma fecha y hora. [ak-02]");
											mensaje.setResultado(true);
											forma.getLisMensaje().add(mensaje);
											contNoRes++;
											if(forma.getNumeroItemsSeleccionados()==contNoRes)
											{
												for(int k=0; k<forma.getNumeroItemsSeleccionados(); k++)
												{
													errores.add("codigoMedico",new ActionMessage("error.cita.noReservaAsignacionCita",forma.getItemSeleccionado("nombreUnidadConsulta_"+k),
															forma.getItemSeleccionado("fecha_"+k),
															forma.getItemSeleccionado("horaInicio_"+k),
															"reservada"
														)
													);
												}
											}
										}
										logger.info("\n\nIndicador: "+indicador+"\n\n");
									}
								}
							}
						} // fin for citas
					}
					//Fin if- Validacion de la insasistencia del paciente
				}else{
					errores.add("descripcion", new ActionMessage("errors.notEspecific","Posiblemente no se ha Diligenciado el Formulario Correctamente o no se ha Seleccionado una Cita."));
				}

				if(!errores.isEmpty() )
				{
					saveErrors(request, errores);
					UtilidadBD.abortarTransaccion(con);
					UtilidadBD.closeConnection(con);
				}
				else
				{
					
					//*******************************************************************************
                 	//Inserta el log autorizacion en caso de que se hubiere pedido
                 	if(ValoresPorDefecto.getValoresDefectoComprobacionDerechosCapitacionObligatoria(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi)
                 			&& !forma.getAutorizacionIngEvento().getCodigo().equals("") 
                 				&& !forma.getAutorizacionIngEvento().isActivo())
                 	{
                 		if(UtilidadesManejoPaciente.insertarLogAutorizacionIngresoEvento(
                 				con,
                 				forma.getAutorizacionIngEvento().getCodigo()+"",
                 				forma.getCodigoPaciente()+"",
                 				forma.getNumeroIdentificacionPaciente()+"", 
                 				forma.getTipoIdentificacionPaciente().split("-")[0].trim(),
                 				usuario,
                 				47+""))
                 			logger.info("\n\nINSERTO LOG DE AUTORIZACION INGRESO EVENTO");
                 		else
                 			logger.info("\n\n NO INSERTO LOG DE AUTORIZACION INGRESO EVENTO");	                 			
                 	}
                 	//*******************************************************************************
					this.organizarCitasReserva(request,forma,tamanio);
					
					logger.info("\n\n\n\n****************************************************************");
					for(ResultadoBoolean elem: forma.getLisMensaje()){
						logger.info("Valor del Mensaje de Advertencia: "+elem.getDescripcion());
						logger.info("Valor Boolean del Mensaje: "+elem.isTrue());
					}
					logger.info("\n\n\n\n****************************************************************");
					
					
					cerrarConexion(con);
					return this.resumenImpresionVariasCitas(mapping, request, "reservar",forma);
					/*return
						iniciarCita(
							aam_mapping, acf_form, ahsr_request, ii_PREPARAR_LISTADO_RESERVAR);
							*/
				}
				
				cerrarConexion(con);
				return mapping.findForward("cita");
			}
			/* Forzar el ingreso del paciente en la instituciï¿½n */
			else
			{			
				//***************SE VERIFICA SI SE DEBE VALIDAR LA VIGENCIA EN LA COMPROBACIï¿½N DE DERECHOS CAPITADOS *******************************				
				if(ValoresPorDefecto.getValoresDefectoComprobacionDerechosCapitacionObligatoria(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi) 
						&& !entroAutorizacionIngEven)
				{
					forma.setAutorizacionIngEvento(UtilidadValidacion.esPacienteCapitadoVigente(con,UtilidadFecha.getFechaActual(),forma.getNumeroIdentificacionPaciente(),forma.getTipoIdentificacionPaciente().split("-")[0].trim()));
					
					//Valida que exista el paciente dentro de la vigencia capitada
					if(!forma.getAutorizacionIngEvento().isActivo())
					{
						logger.info("retormo ingreso codigo");
						forma.getAutorizacionIngEvento().setCodigo("");
						return mapping.findForward("autoIngEvento");
					}
				}
				//************************************************************************************************************************************
							
				
				/* Cerrar la conexion a base de datos */
				cerrarConexion(con);
				
				return
					new ActionForward(
						"/ingresarPacienteDummy/ingresarPacienteDummy.do"			+
							"?estado=decisionIngresoPacienteSistema"	+
							"&numeroIdentificacion="					+
							forma.getNumeroIdentificacionPaciente()	+
							"&tipoIdentificacion="						+
							forma.getTipoIdentificacionPaciente()	+
							"&ingresoDesdeReservaCita=true"				+
							"&ingresoDesdeReferencia=false"				+
							"&Submit=submit",
						true
					);
			}
		}
		catch(SQLException e)
		{
			/* Cerrar la conexion a base de datos */
			cerrarConexion(con);

			if(logger.isDebugEnabled() )
				logger.debug("No se realizar el proceso de reserva de citas");

			e.printStackTrace();
			return error(mapping, request, "errors.problemasBd", true);
		}
	}

	private void organizarCitasReserva(HttpServletRequest request,
			CitaForm forma, int tamanio) 
	{
		forma.setNumeroCitas(tamanio);
		forma.setCodigosCitas(new int[0]); //se limpian los codigos almacenados
		
		if(UtilidadTexto.getBoolean(forma.getVerificarEstCitasPac()))
		{
			ArrayList<Integer> citasReales = new ArrayList<Integer>();
			
			 for(int k=0;k<tamanio;k++)
			 {
				 if(request.getSession().getAttribute("codigoCita_" + k)!=null)
				 {
					 citasReales.add(Utilidades.convertirAEntero(request.getSession().getAttribute("codigoCita_" + k)+""));
				 }
			 }
			forma.setNumeroCitas(citasReales.size());
			forma.setCodigosCitas(new int[citasReales.size()]); //se limpian los codigos almacenados
			for(int k=0;k<citasReales.size();k++)
			{
				forma.getCodigosCitas()[k] = citasReales.get(k).intValue();
			}
		}
		
	}



	/** Validar las entradas del listado de agenda disponible para reserva */
	private ActionForward validarListado(
		ActionMapping		mapping,
		CitaForm			forma,
		HttpServletRequest	request,
		int					estado,
		Connection con

	)throws Exception
	{

		try
		{
			if(estado == ii_VALIDAR_LISTADO_ASIGNAR)
				forma.setEstado("listadoAsignar");
			else if(estado == ii_VALIDAR_LISTADO_RESERVAR)
			{
				/* Cargar al paciente en sesiï¿½n si este es vï¿½lido */
				if(!forma.isMantenerPacienteCargado())
					cargarPacienteSesion(request, forma, con);
				forma.setEstado("listadoReservar");
				forma.setEstadoAnterior("listadoReservar");
			}

			/* Cerrar la conexion a base de datos */
			cerrarConexion(con);
			return mapping.findForward("cita");
		}
		catch(Exception e)
		{
			/* Cerrar la conexion a base de datos */
			cerrarConexion(con);

			if(logger.isDebugEnabled() )
				logger.debug("No se pudo validar la agenda disponible para reserva de citas");

			e.printStackTrace();
			return error(mapping, request, "errors.problemasBd", true);
		}
	}

	/** Validar que el paciente cumpla todas las condiciones para la asignaciï¿½n de citas 
	 * @param usuario */
	private ActionForward validarPacienteAsignacion(ActionMapping mapping,HttpServletRequest request,PersonaBasica paciente, UsuarioBasico usuario)
	{
		Connection	con = UtilidadBD.abrirConexion();
		
		//Validacion del paciente
		RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
		int numeroDias = Utilidades.convertirAEntero(ValoresPorDefecto.getValoresDefectoDiasRestriccionCitasIncumplidas(usuario.getCodigoInstitucionInt())); 
		
		if(!resp.puedoSeguir)
		{
			/* El paciente no esta cargado en sesiï¿½n */
			if(logger.isDebugEnabled() )
				logger.debug("Error realizando las validaciones del paciente");
			
			UtilidadBD.closeConnection(con);
			return error(mapping, request, resp.textoRespuesta, true);
		}
		
		//Validar que el usuario no se autoatienda
		ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
		if(respuesta.isTrue())
			return error(mapping, request,  respuesta.getDescripcion(), true);
					
		
		//Valida que el paciente no posee el ingreso con entidades subcontratadas
		if(IngresoGeneral.esIngresoComoEntidadSubContratada(con,paciente.getCodigoIngreso()).toString().equals(ConstantesBD.acronimoSi))
		{
			return error(mapping, request,"Ingreso de Paciente en Entidades Subcontratadas. Entidad Subcontratada : "+EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con,paciente.getCodigoIngreso()+""),false);
		}
		//Se valida que el ingreso se encuentre abierto
		else if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getAcronimo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
		{
			UtilidadBD.closeConnection(con);
			return error(mapping, request, "errors.paciente.noIngreso", true);
		}
		else if(numeroDias>0&&UtilidadesConsultaExterna.pacienteConInasistencia(con, paciente.getCodigoPersona(), numeroDias))
		{
			UtilidadBD.closeConnection(con);
			return error(mapping, request, "error.cita.asignacion.inasistencia", true);
		}
		//Se valida que la cuenta se de Consulta Externa
		/*else if(paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoConsultaExterna)
		{
			UtilidadBD.closeConnection(con);
			/* La via de ingreso del paciente no es consulta externa *
			if(logger.isDebugEnabled() )
			{
				logger.debug("La vï¿½a de ingreso del paciente no es Consulta Externa");
			}

			return error(mapping, request, "error.cita.asignacion.viaIngreso", true);
			
		}*/
		UtilidadBD.closeConnection(con);
		return null;
		
	}

	/**
	 * Accion generar reporte para reserva de cita (pdf)
	 * @param Connection 
	 * @param CitaForm forma
	 * @param UsuarioBasico usuario
	 * @param HttpServletRequest request
	 * */
	@SuppressWarnings("rawtypes")
	private void accionGenerarReportePdf(Connection con, CitaForm forma, UsuarioBasico usuario,  HttpServletRequest request)
	{
		HashMap datosCita = new HashMap();
		Vector v = new Vector();
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		String nombreRptDesign = "";
		
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getImpresionMediaCarta(usuario.getCodigoInstitucionInt())))
			nombreRptDesign = "ResumenReservaCitaA4.rptdesign";
		else
			nombreRptDesign = "ResumenReservaCitaLetter.rptdesign";        
		
		//**************INICIO CAMBIO POR ERROR EN EL LLAMADO DEL REPORTE DESDE LA REPROGRAMACION******************
		@SuppressWarnings("unused")
		String codigos = "-1";
		logger.info("===>Cï¿½digo Citas por Request: "+request.getParameter("codigosCitas"));
		logger.info("===>Cï¿½digo Paciente: "+request.getParameter("codPaciente"));
		//Cuando viene por la Reprogramaciï¿½n sin pasar por la Reserva
		if(UtilidadCadena.noEsVacio(request.getParameter("codigosCitas")+""))
		{
			forma.setCodigosCitasReprogramacion(request.getParameter("codigosCitas")+"");
			//Se debe cargar el paciente que viene desde la Reprogramaciï¿½n
			if(UtilidadCadena.noEsVacio(request.getParameter("codPaciente")+""))
				forma.setCodigoPaciente(Utilidades.convertirAEntero(request.getParameter("codPaciente")+""));
		}
		//Cuando viene por la Reserva, Asignaciï¿½n y la Reprogramaciï¿½n pasando previamente por la Reprogramaciï¿½n
		//logger.info("===>Length Codigo Citas: "+forma.getCodigosCitas().length);
		/*for(int i = 0; i < forma.getCodigosCitas().length; i++)
		{
			logger.info("===>Codigo Cita: "+forma.getCodigosCitas()[i]);
			codigos += ","+forma.getCodigosCitas()[i];
		}*/
		
		// Envio de parametros a el reporte
        String fechaNacimiento = "";
        String edad="";
        try {
			PersonaBasica personaBasica = new PersonaBasica();
			personaBasica.cargar(con, forma.getCodigoPaciente());
			personaBasica.cargarPaciente(con, forma.getCodigoPaciente(), usuario.getCodigoInstitucionInt()+"", usuario.getCodigoCentroCosto()+"");
			
			String arrTemp[]=personaBasica.getFechaNacimiento().split("/");
			fechaNacimiento=util.UtilidadFecha.conversionFormatoFechaAAp ( fechaNacimiento);
			
			String frechaActual[]=util.UtilidadFecha.getFechaActual().split("/");
			
			edad=util.UtilidadFecha.calcularEdadDetallada(Integer.parseInt(arrTemp[2]), Integer.parseInt(arrTemp[1]), Integer.parseInt(arrTemp[0]), frechaActual[0], frechaActual[1], frechaActual[2]);
			
		} catch (Exception e) {
			Log4JManager.error("No se pudo cargar la persona ", e);
		}
		
		datosCita = Cita.getReportePdfBaseCita(con, forma.getCodigosCitasReprogramacion(), forma.getCodigoPaciente()+"",edad);
		//****************FIN CAMBIO POR ERROR EN EL LLAMADO DEL REPORTE DESDE LA REPROGRAMACION********************
		
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"consultaExterna/",nombreRptDesign);
         
        // Logo
        comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        v.add(ins.getRazonSocial());
        if(Utilidades.convertirAEntero(ins.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit()+" - "+ins.getDigitoVerificacion());
        else
        	v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit());
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0, 1, v);
        
        //Informaciï¿½n del reporte
        logger.info("===>Titulo: "+forma.getFlujo());
        if(UtilidadCadena.noEsVacio(forma.getFlujo()) && !forma.getFlujo().equals("null"))
        {
        	if(forma.getFlujo().equals("reserva"))
        	{
        		logger.info("===>Titulo 1: "+forma.getFlujo());
        		comp.insertLabelInGridPpalOfHeader(2,0,"RESERVAR CITAS");
        	}
	        else if(forma.getFlujo().equals("asignacion"))
	        {
	        	comp.insertLabelInGridPpalOfHeader(2,0,"ASIGNAR CITAS");
	        	logger.info("===>Titulo 2: "+forma.getFlujo());
	        }
        }
        else
        {
        	logger.info("===>Titulo 3: "+forma.getFlujo());
        	comp.insertLabelInGridPpalOfHeader(2,0,"REPROGRAMACIï¿½N DE CITAS");
        }
          
        //Como los parametros los estaban poniendo vacios mejor se coloco
        //el titulo en la fila dos con la intenciï¿½n de dejar un espacio
        //Parametros de Busqueda
        //comp.insertLabelInGridPpalOfHeader(2,0,"");
        
        //Usuario        
        comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
    	
        //Evalua el numero de dataset
        comp.obtenerComponentesDataSet("citas");
        comp.modificarQueryDataSet(datosCita.get("sql0").toString());
        comp.obtenerComponentesDataSet("encabezado");
        comp.modificarQueryDataSet(datosCita.get("sql1").toString());
        
        
        /**DCU 13->Cambio 2.1**********************************************/
        //llenando lista para mostrar los servicios en la impresion de la cita
        //Si se solicita impirmir los servicios faltaria agrgarle el codigo a cada servicio
        
        /*HashMap serviciosImprimir=new HashMap();
        for(int i=0;i<forma.getListaServiciosImpimirOrden().size();i++)
        	serviciosImprimir.put("descServicio_"+i,forma.getListaServiciosImpimirOrden().get(i));
        serviciosImprimir.put("numRegistros",forma.getListaServiciosImpimirOrden().size());
        
        String []columnas={"descServicio_"};        
        comp.insertarMapaEnReporte(columnas, serviciosImprimir, 3);*/
        /*****************************************************************/
        
        
        logger.info("valor de cita >> "+datosCita+" >> "+nombreRptDesign);
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);	
        comp.updateJDBCParameters(newPathReport);
      
        request.setAttribute("isOpenReport", "true");
    	request.setAttribute("newPathReport",newPathReport);  	
	}

	/**
	 * Mï¿½todo que se encarga de realizar el resumen de la impresiï¿½n
	 * 
	 * @param mapping Mapping con los sitios a los que puede 
	 * acceder struts
	 * @param request Objeto request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private ActionForward resumenImpresionVariasCitas ( ActionMapping mapping, HttpServletRequest	request, String tipo, CitaForm forma) throws Exception
	{
		int i=0, numElementos=0, codigosCita[];
		String codigos = ConstantesBD.codigoNuncaValido+"";
		try
		{
			logger.info("[resumenImpresionVariasCitas]");
			
			numElementos=forma.getNumeroCitas();
			
			logger.info("[resumen] numElementos=> "+numElementos);
			logger.info("[resumen] numero de codigosCitas=> "+forma.getCodigosCitas().length);
			
			codigosCita=forma.getCodigosCitas();
			
			logger.info("[Codigos Citas ]"+codigosCita);
			logger.info("[Codigos Citas Tamaï¿½o]"+forma.getCodigosCitas().length);
			
			if(forma.getCodigosCitas().length<=0)
			{
				codigosCita=new int[numElementos];
				for (i=0;i<numElementos;i++)
				{
					logger.info("[arreglo codigos citas "+i+"]"+codigosCita[i]);
					codigosCita[i]=Utilidades.convertirAEntero(request.getSession().getAttribute("codigoCita_" + i)+"");
					logger.info("[arreglo codigos citas despues "+i+"]"+codigosCita[i]);
					request.getSession().removeAttribute("codigoCita_" + i);
					codigos += ","+codigosCita[i]; 
				}
			}
			else
			{
				for(i=0;i<forma.getCodigosCitas().length;i++)
				{
					logger.info("[arreglo codigos citas ya venï¿½a lleno "+i+"]"+codigosCita[i]);
					codigos += ","+codigosCita[i];
				}
			}
						
			Cita citaACargar=new Cita();
			HashMap resultados=citaACargar.cargarImpresionCodigos(codigosCita);
			
			/**DCU 13->Cambio 2.1****************************************/
			//Se cambia solo en la presentacion la validacion de cambio en la descripcion
			if(!Utilidades.isEmpty(forma.getListaServiciosImpimirOrden()))
			{
				for(int k=0;k<Integer.parseInt(resultados.get("numRegistros").toString());k++)
				{
					HashMap mapaServicios = (HashMap)resultados.get("mapaServicios_"+k); 
					if(Integer.parseInt(mapaServicios.get("numRegistros").toString())>0)
					{
						for(int j=0;j<Integer.parseInt(mapaServicios.get("numRegistros").toString());j++)
						{						
							mapaServicios.put("nombreServicio_"+j, forma.getListaServiciosImpimirOrden().get(j));
						}
					}
				}
			}
			/***********************************************************/
			
			forma.setCodigosCitas(codigosCita);
			
			Utilidades.imprimirMapa(resultados);
			
			request.setAttribute("tipo", tipo);
			request.setAttribute("citasAImprimir", resultados);
			request.setAttribute("codigosCitasImprimir", codigos);
			
			logger.info("\n\n\n\n****************************************************************");
			for(ResultadoBoolean elem: forma.getLisMensaje()){
				logger.info("Valor del Mensaje de Advertencia: "+elem.getDescripcion());
				logger.info("Valor Boolean del Mensaje: "+elem.isTrue());
			}
			logger.info("\n\n\n\n****************************************************************");
			
			return mapping.findForward("resumenImpresionesCita2");
		}
		catch (Exception e)
		{
			logger.error(" [ Error ]",e);
			return this.error(mapping, request, "Error durante visualización cita(s)", false);
		}
	}
	
	/**
	 * Mï¿½todo que se encarga de realizar el resumen de la impresiï¿½n
	 * 
	 * @param mapping Mapping con los sitios a los que puede 
	 * acceder struts
	 * @param request Objeto request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private ActionForward resumenImpresionCita ( ActionMapping mapping, HttpServletRequest	request) throws Exception
	{
		int codigoCita=0;
		String codigoCitaStr=request.getParameter("codigoCita");
		if (codigoCitaStr==null)
		{
			codigoCitaStr=(String)request.getAttribute("codigoCita");
		}
		if (codigoCitaStr==null)
		{
			return this.error(mapping, request, "error.cita.noExiste", true);
		}
		try
		{
			codigoCita=Integer.parseInt(codigoCitaStr);
		}
		catch (Exception e)
		{
			return this.error(mapping, request, "error.cita.noExiste", true);
		}
		
		//En este punto numeroCitaLlega tiene el nï¿½mero de la cita
		//Ahora lo intentamos cargar, si carga lo enviamos al resumen,
		//si no a la pagina de error
		
		Cita citaACargar=new Cita();
		int arrTemp[]=new int[1];
		arrTemp[0]=codigoCita;
		HashMap resultado=citaACargar.cargarImpresionCodigo(arrTemp);

		if (resultado==null)
		{
			return this.error(mapping, request, "error.cita.noExiste", true);
		}
		else
		{
			request.setAttribute("encabezadoPaciente", "true");
			request.setAttribute("citaAImprimir", resultado);
			return mapping.findForward("resumenImpresionCita");
		}
	}

	/**
	 * Metodo que pasa la informacion del form al mundo de la parte
	 * de informacion de la cuenta en reserva de citas
	 * @param lc_cita
	 * @param forma
	 */
	private void llenarMundoInfoCuenta(Cita lc_cita, CitaForm forma) 
	{
		lc_cita.setConvenio(forma.getConvenio());
		lc_cita.setContrato(forma.getContrato());
		lc_cita.setEstratoSocial(forma.getEstratoSocial());
		lc_cita.setTipoAfiliado(forma.getTipoAfiliado());
		lc_cita.setNaturalezaPaciente(forma.getNaturalezaPaciente());
		lc_cita.setTelefono(forma.getTelefono());
		lc_cita.setCelular(forma.getCelular());
		lc_cita.setOtrosTelefonos(forma.getOtrosTelefonos());
		lc_cita.setOrigenTelefono(forma.getOrigenTelefono());
		
		
	}
	
	/**
	 * Mï¿½todo que realiza verifica si el tipo y numero de identificacion existe en usuarios
	 * capitados para cargar el convenio
	 * @param con
	 * @param mapping
	 * @param citaForm
	 * @param ahsr_response 
	 * @throws SQLException 
	 */
	@SuppressWarnings("rawtypes")
	private ActionForward accionVerificarUsuarioCapitado(Connection con, ActionMapping mapping, CitaForm citaForm, HttpServletResponse ahsr_response) throws SQLException 
	{
		String nombrePaciente="";
		logger.info("\n\n");
		logger.info("***************************************************");
		citaForm.setArrayListUtilitario_2(new ArrayList()); 
		int codigoUsuarioCapitado=0;
		HashMap mapaContratos = new HashMap();
		int numContratos = 0;		
		citaForm.setVerificadoUsuarioCapitado(true);
		HashMap mapaConvenios=new HashMap();
		boolean convenioManejaMontos=false;
		boolean setearCampos=false;
		
		if(!citaForm.getEstadoAnterior().equals("listadoReservar"))
		{
			setearCampos = true;
			citaForm.setConvenio(ConstantesBD.codigoNuncaValido);
			citaForm.setConvenioCapitado(ConstantesBD.codigoNuncaValido+"");
			citaForm.setNombreConvenioCapitado("");
			citaForm.setTipoRegimenConvenio("");
			citaForm.setEstratoSocial(ConstantesBD.codigoNuncaValido);
			citaForm.setTipoAfiliado("");
			citaForm.setNaturalezaPaciente(ConstantesBD.codigoNuncaValido);
			citaForm.setTelefono("");
			citaForm.setCelular("");
			citaForm.setOtrosTelefonos("");
			citaForm.setEsUsuarioCapitado(ConstantesBD.acronimoNo);
		}
				
		logger.info("tipoIdentificacionPaciente=> "+citaForm.getTipoIdentificacionPaciente()+" numeroIdentificacionPaciente=> "+citaForm.getNumeroIdentificacionPaciente());
		//verifica que el paciente sea un usuario capitado en la estructura de usuarios_capitados		
		codigoUsuarioCapitado=Utilidades.getCodigoUsuarioCapitado(con, citaForm.getTipoIdentificacionPaciente().split("-")[0], citaForm.getNumeroIdentificacionPaciente());
		
		//-----Si codigoUsuarioCapitado es != -1 significa que existe dentro de usuarios capitados ----// 
		if(codigoUsuarioCapitado!=ConstantesBD.codigoNuncaValido)
		{
			citaForm.setEsUsuarioCapitado(ConstantesBD.acronimoSi);
			logger.info("paciente dentro de usuarios capitados");
			
			//verifica que tenga una capita vigente tanto en conv_usuarios_capitados como en usuario_x_convenio
			InfoDatosString info = new InfoDatosString();
			info = UtilidadValidacion.esPacienteCapitadoVigente(
					con,
					UtilidadFecha.getFechaActual(),
					citaForm.getNumeroIdentificacionPaciente()+"",
					citaForm.getTipoIdentificacionPaciente().split("-")[0]); 
			
			if(info.getActivo())
			{
				//se evalua en conv_usuarios_capitados
				if(info.getDescripcion().equals("1"))
				{
					//Se consultan los contratos vigentes del convenio
					HashMap mapaUsuCapitados=Utilidades.obtenerInformacionUsuarioCapitado(con,citaForm.getTipoIdentificacionPaciente().split("-")[0], citaForm.getNumeroIdentificacionPaciente());
					//String pApellido=mapaUsuCapitados.get("primerApellido_0")+"";
					String pApellido=mapaUsuCapitados.get("primer_apellido_0")+"";
					//String sApellido=mapaUsuCapitados.get("segundoApellido_0")+"";
					String sApellido=mapaUsuCapitados.get("segundo_apellido_0")+"";
					//String pNombre=mapaUsuCapitados.get("primerNombre_0")+"";
					String pNombre=mapaUsuCapitados.get("primer_nombre_0")+"";
					//String SNombre=mapaUsuCapitados.get("segundoNombre_0")+"";
					String SNombre=mapaUsuCapitados.get("segundo_nombre_0")+"";
					if(!UtilidadTexto.isEmpty(pApellido)){
						nombrePaciente=pApellido+" "+(UtilidadTexto.isEmpty(sApellido)?"":sApellido+" ")+pNombre+(UtilidadTexto.isEmpty(SNombre)?"":SNombre);
					}

					//---Se consulta el convenio capitado para el usuario
					mapaContratos=Utilidades.convenioUsuarioCapitado(con, codigoUsuarioCapitado);
					numContratos = Integer.parseInt(mapaContratos.get("numRegistros").toString());
										
					if(numContratos > 0)
					{
						mapaConvenios.put("codigo_convenio_0",mapaContratos.get("codigo_convenio_0"));
						mapaConvenios.put("nombre_convenio_0",mapaContratos.get("nombre_convenio_0"));
						mapaConvenios.put("clasificacion_socio_economica_0", mapaContratos.get("clasificacion_socio_economica_0"));
						mapaConvenios.put("nombre_estrato_0", mapaContratos.get("nombre_estrato_0"));
						mapaConvenios.put("tipo_afiliado_0", mapaContratos.get("tipo_afiliado_0"));
						mapaConvenios.put("nombre_tipo_afiliado_0", mapaContratos.get("nombre_tipo_afiliado_0"));
						mapaConvenios.put("naturaleza_paciente_0", mapaContratos.get("naturaleza_paciente_0"));
						mapaConvenios.put("nombre_naturaleza_0", mapaContratos.get("nombre_naturaleza_0"));
						mapaConvenios.put("tipo_regimen_0",mapaContratos.get("codigo_tipo_regimen_0"));
						mapaConvenios.put("telefono_0","");
						mapaConvenios.put("celular_0","");
						mapaConvenios.put("otrostelefonos_0","");
						
						if(mapaContratos.get("maneja_montos_0").toString().equals(ConstantesBD.acronimoSi)){
							convenioManejaMontos = true;
						}
						
						//Solo puede listar contratos de un convenio
						HashMap temp = new HashMap();
						int cont = 0;
						
						for(int i=0; i<numContratos; i++)
						{
							if(mapaConvenios.get("codigo_convenio_0").toString().equals(mapaContratos.get("codigo_convenio_"+i).toString()))
							{
								temp.put("codigo_contrato_"+cont,mapaContratos.get("codigo_contrato_"+i));									
								temp.put("codigo_convenio_"+cont,mapaContratos.get("codigo_convenio_"+i));									
								temp.put("nombre_convenio_"+cont,mapaContratos.get("nombre_convenio_"+i));
								temp.put("codigo_tipo_regimen_"+cont,mapaContratos.get("codigo_tipo_regimen_"+i));
								temp.put("nombre_tipo_regimen_"+cont,mapaContratos.get("nombre_tipo_regimen_"+i));
								temp.put("es_pyp_"+cont,mapaContratos.get("es_pyp_"+i));
								temp.put("es_capitado_"+cont,mapaContratos.get("es_capitado_"+i));									
								temp.put("numero_contrato_"+cont,mapaContratos.get("numero_contrato_"+i));
								cont++;
							}
						}
						
						mapaContratos  = temp;
						mapaContratos.put("numRegistros",cont);
						numContratos = cont;
													
						logger.info("se encontraron contratos en usuario_x_convenio");
					}									
				}
				else if(info.getDescripcion().equals("2"))
				{
					HashMap mapaPaciente = Utilidades.obtenerDatosPersona(con, citaForm.getTipoIdentificacionPaciente().split("-")[0], citaForm.getNumeroIdentificacionPaciente());
					
					if(Integer.parseInt(mapaPaciente.get("numRegistros").toString())>0)
					{
						//String pApellido=mapaPaciente.get("primerApellido_0")+"";
						String pApellido=mapaPaciente.get("primer_apellido_0")+"";
						//String sApellido=mapaPaciente.get("segundoApellido_0")+"";
						String sApellido=mapaPaciente.get("segundo_apellido_0")+"";
						//String pNombre=mapaPaciente.get("primerNombre_0")+"";
						String pNombre=mapaPaciente.get("primer_nombre_0")+"";
						//String SNombre=mapaPaciente.get("segundoNombre_0")+"";
						String SNombre=mapaPaciente.get("segundo_nombre_0")+"";
						if(!UtilidadTexto.isEmpty(pApellido))
							nombrePaciente=pApellido+" "+(UtilidadTexto.isEmpty(sApellido)?"":sApellido+" ")+pNombre+(UtilidadTexto.isEmpty(SNombre)?"":SNombre);
						
						
						SubirPaciente subirPac = new SubirPaciente();
						mapaContratos = subirPac.consultarUsuarioXConvenio(con,mapaPaciente.get("codigo_0").toString());
						numContratos = Integer.parseInt(mapaContratos.get("numRegistros").toString());
											
						if(numContratos > 0)
						{
							mapaConvenios.put("codigo_convenio_0",mapaContratos.get("codigo_convenio_0"));
							mapaConvenios.put("nombre_convenio_0",mapaContratos.get("nombre_convenio_0"));
							mapaConvenios.put("clasificacion_socio_economica_0",mapaContratos.get("clasificacion_socio_economica_0"));
							mapaConvenios.put("nombre_estrato_0", mapaContratos.get("nombre_estrato_0"));
							mapaConvenios.put("tipo_afiliado_0", mapaContratos.get("tipo_afiliado_0"));
							mapaConvenios.put("nombre_tipo_afiliado_0", mapaContratos.get("nombre_tipo_afiliado_0"));
							mapaConvenios.put("naturaleza_paciente_0", mapaContratos.get("naturaleza_paciente_0"));
							mapaConvenios.put("nombre_naturaleza_0", mapaContratos.get("nombre_naturaleza_0"));
							mapaConvenios.put("tipo_regimen_0",mapaContratos.get("codigo_tipo_regimen_0"));
							mapaConvenios.put("telefono_0","");
							mapaConvenios.put("celular_0","");
							mapaConvenios.put("otrostelefonos_0","");
							
							if(mapaContratos.get("maneja_montos_0").toString().equals(ConstantesBD.acronimoSi)){
								convenioManejaMontos = true;
							}
							
							//Solo puede listar contratos de un convenio
							HashMap temp = new HashMap();
							int cont = 0;
							mapaConvenios.put("codigo_convenio_0",mapaContratos.get("codigo_convenio_0"));
							
							for(int i=0; i<numContratos; i++)
							{
								if(mapaConvenios.get("codigo_convenio_0").toString().equals(mapaContratos.get("codigo_convenio_"+i).toString()))
								{
									temp.put("codigo_paciente_"+cont,mapaContratos.get("codigo_paciente_"+i));
									temp.put("codigo_contrato_"+cont,mapaContratos.get("codigo_contrato_"+i));									
									temp.put("codigo_convenio_"+cont,mapaContratos.get("codigo_convenio_"+i));									
									temp.put("nombre_convenio_"+cont,mapaContratos.get("nombre_convenio_"+i));
									temp.put("codigo_tipo_regimen_"+cont,mapaContratos.get("codigo_tipo_regimen_"+i));
									temp.put("nombre_tipo_regimen_"+cont,mapaContratos.get("nombre_tipo_regimen_"+i));
									temp.put("es_pyp_"+cont,mapaContratos.get("es_pyp_"+i));
									temp.put("es_capitado_"+cont,mapaContratos.get("es_capitado_"+i));									
									temp.put("numero_contrato_"+cont,mapaContratos.get("numero_contrato_"+i));
									cont++;
								}
							}
							
							mapaContratos  = temp;
							mapaContratos.put("numRegistros",cont);
							numContratos = cont;
														
							logger.info("se encontraron contratos en usuario_x_convenio");
						}
					}
				}			
			}
			else
				logger.info("no se encontro contratos vigentes");
		}		
		else
		{
			logger.info("paciente NO esta dentro de usuarios capitados");
			
			HashMap mapaPaciente = Utilidades.obtenerDatosPersona(con, citaForm.getTipoIdentificacionPaciente().split("-")[0], citaForm.getNumeroIdentificacionPaciente());				
			if(Integer.parseInt(mapaPaciente.get("numRegistros").toString())>0)
			{
				//String pApellido=mapaPaciente.get("primerApellido_0")+"";
				String pApellido=mapaPaciente.get("primer_apellido_0")+"";
				//String sApellido=mapaPaciente.get("segundoApellido_0")+"";
				String sApellido=mapaPaciente.get("segundo_apellido_0")+"";
				//String pNombre=mapaPaciente.get("primerNombre_0")+"";
				String pNombre=mapaPaciente.get("primer_nombre_0")+"";
				//String SNombre=mapaPaciente.get("segundoNombre_0")+"";
				String SNombre=mapaPaciente.get("segundo_nombre_0")+"";
				if(!UtilidadTexto.isEmpty(pApellido))
					nombrePaciente=pApellido+" "+(UtilidadTexto.isEmpty(sApellido)?"":sApellido+" ")+pNombre+(UtilidadTexto.isEmpty(SNombre)?"":SNombre);
				
				SubirPaciente subirPac = new SubirPaciente();
				mapaContratos = subirPac.consultarUsuarioXConvenio(con,mapaPaciente.get("codigo_0").toString());
				numContratos = Integer.parseInt(mapaContratos.get("numRegistros").toString());
				
				if(numContratos > 0)
				{
					citaForm.setEsUsuarioCapitado(ConstantesBD.acronimoSi);
					mapaConvenios.put("codigo_convenio_0",mapaContratos.get("codigo_convenio_0"));
					mapaConvenios.put("nombre_convenio_0",mapaContratos.get("nombre_convenio_0"));
					mapaConvenios.put("clasificacion_socio_economica_0",mapaContratos.get("clasificacion_socio_economica_0"));
					mapaConvenios.put("nombre_estrato_0", mapaContratos.get("nombre_estrato_0"));
					mapaConvenios.put("tipo_afiliado_0", mapaContratos.get("tipo_afiliado_0"));
					mapaConvenios.put("nombre_tipo_afiliado_0", mapaContratos.get("nombre_tipo_afiliado_0"));
					mapaConvenios.put("naturaleza_paciente_0", mapaContratos.get("naturaleza_paciente_0"));
					mapaConvenios.put("nombre_naturaleza_0", mapaContratos.get("nombre_naturaleza_0"));
					mapaConvenios.put("tipo_regimen_0",mapaContratos.get("codigo_tipo_regimen_0"));
					mapaConvenios.put("telefono_0","");
					mapaConvenios.put("celular_0","");
					mapaConvenios.put("otrostelefonos_0","");
					
					if(mapaContratos.get("maneja_montos_0").toString().equals(ConstantesBD.acronimoSi)){
						convenioManejaMontos = true;
					}
					
					//Solo puede listar contratos de un convenio
					HashMap temp = new HashMap();
					int cont = 0;
					mapaConvenios.put("codigo_convenio_0",mapaContratos.get("codigo_convenio_0"));
					
					for(int i=0; i<numContratos; i++)
					{
						if(mapaConvenios.get("codigo_convenio_0").toString().equals(mapaContratos.get("codigo_convenio_"+i).toString()))
						{
							temp.put("codigo_paciente_"+cont,mapaContratos.get("codigo_paciente_"+i));
							temp.put("codigo_contrato_"+cont,mapaContratos.get("codigo_contrato_"+i));									
							temp.put("codigo_convenio_"+cont,mapaContratos.get("codigo_convenio_"+i));									
							temp.put("nombre_convenio_"+cont,mapaContratos.get("nombre_convenio_"+i));
							temp.put("codigo_tipo_regimen_"+cont,mapaContratos.get("codigo_tipo_regimen_"+i));
							temp.put("nombre_tipo_regimen_"+cont,mapaContratos.get("nombre_tipo_regimen_"+i));
							temp.put("es_pyp_"+cont,mapaContratos.get("es_pyp_"+i));
							temp.put("es_capitado_"+cont,mapaContratos.get("es_capitado_"+i));									
							temp.put("numero_contrato_"+cont,mapaContratos.get("numero_contrato_"+i));
							cont++;
						}
					}
					
					mapaContratos  = temp;
					mapaContratos.put("numRegistros",cont);
					numContratos = cont;
					
					logger.info("se encontraron contratos en usuario_x_convenio");
				}
			}
		}
		
		if(setearCampos)
		{
			if(numContratos > 0 )
			{
				citaForm.setConvenio(UtilidadCadena.vInt(mapaConvenios.get("codigo_convenio_0")+""));
				citaForm.setConvenioCapitado(mapaConvenios.get("codigo_convenio_0")+"");
				citaForm.setNombreConvenioCapitado(mapaConvenios.get("nombre_convenio_0")+"");
				citaForm.setTipoRegimenConvenio(mapaConvenios.get("tipo_regimen_0")+"");
				citaForm.setEstratoSocial(Utilidades.convertirAEntero(mapaConvenios.get("clasificacion_socio_economica_0").toString()));
				citaForm.setNombreClasificacionSocial(mapaConvenios.get("nombre_estrato_0").toString());
				citaForm.setTipoAfiliado(mapaConvenios.get("tipo_afiliado_0").toString());
				citaForm.setNombreTipoAfiliado(mapaConvenios.get("nombre_tipo_afiliado_0").toString());
				citaForm.setNaturalezaPaciente(Utilidades.convertirAEntero(mapaConvenios.get("naturaleza_paciente_0").toString()));
				citaForm.setNombreNaturaleza(mapaConvenios.get("nombre_naturaleza_0").toString());
				citaForm.setTelefono(mapaConvenios.get("telefono_0").toString());
				
				HashMap mapaPaciente = Utilidades.obtenerDatosPersona(con, citaForm.getTipoIdentificacionPaciente().split("-")[0], 
						citaForm.getNumeroIdentificacionPaciente());
				
				//Así se encuentren contratos de debe mostrar el teléfono del paciente Tarea Versalles ID: 180230
				//si es capitado, primero toma este telfono del cargue luego consulta si tiene otros telefonos asignados en el paciente.
				if(UtilidadTexto.isEmpty(citaForm.getTelefono()))
				{
					if(!UtilidadTexto.isEmpty(mapaPaciente.get("telefono_fijo_0")+"") && ( Utilidades.convertirAEntero(mapaPaciente.get("telefono_fijo_0")+"")>0) )
					{
						citaForm.setTelefono(mapaPaciente.get("telefono_fijo_0")+"");
						citaForm.setOrigenTelefono("telefono_fijo");
					}
				}
				if(!UtilidadTexto.isEmpty(mapaPaciente.get("telefono_celular_0")+"")  && ( Utilidades.convertirAEntero(mapaPaciente.get("telefono_celular_0")+"")>0) )
				{
						citaForm.setCelular(mapaPaciente.get("telefono_celular_0")+"");
				}
				if(!UtilidadTexto.isEmpty(mapaPaciente.get("telefono_0")+""))
				{
					citaForm.setOtrosTelefonos(mapaPaciente.get("telefono_0")+"");
				}		
			}
			else
			{
				HashMap mapaPaciente = Utilidades.obtenerDatosPersona(con, citaForm.getTipoIdentificacionPaciente().split("-")[0], citaForm.getNumeroIdentificacionPaciente());
				
				//String pApellido=mapaPaciente.get("primerApellido_0")+"";
				String pApellido=mapaPaciente.get("primer_apellido_0")+"";
				//String sApellido=mapaPaciente.get("segundoApellido_0")+"";
				String sApellido=mapaPaciente.get("segundo_apellido_0")+"";
				//String pNombre=mapaPaciente.get("primerNombre_0")+"";
				String pNombre=mapaPaciente.get("primer_nombre_0")+"";
				//String SNombre=mapaPaciente.get("segundoNombre_0")+"";
				String SNombre=mapaPaciente.get("segundo_nombre_0")+"";
				if(!UtilidadTexto.isEmpty(pApellido))
					nombrePaciente=pApellido+" "+(UtilidadTexto.isEmpty(sApellido)?"":sApellido+" ")+pNombre+(UtilidadTexto.isEmpty(SNombre)?"":SNombre);
				
				if(!UtilidadTexto.isEmpty(mapaPaciente.get("telefono_fijo_0")+"") && ( Utilidades.convertirAEntero(mapaPaciente.get("telefono_fijo_0")+"")>0) )
				{
					citaForm.setTelefono(mapaPaciente.get("telefono_fijo_0")+"");
					citaForm.setOrigenTelefono("telefono_fijo");
				}
				if(!UtilidadTexto.isEmpty(mapaPaciente.get("telefono_celular_0")+"")  && ( Utilidades.convertirAEntero(mapaPaciente.get("telefono_celular_0")+"")>0) )
				{
					citaForm.setCelular(mapaPaciente.get("telefono_celular_0")+"");
				}
				if(!UtilidadTexto.isEmpty(mapaPaciente.get("telefono_0")+""))
				{
					citaForm.setOtrosTelefonos(mapaPaciente.get("telefono_0")+"");
				}
				
			}
		}
		
		/**MT 2163- Validar cuando el convenio no maneja Montos Cobro,
		 * no es requerido la clasificacion S.E. ni el tipo de Afiliado */
		MessageResources mensajes=MessageResources.getMessageResources(
		"com.servinte.mensajes.agenda.CitaForm");
		String msgError = null;
		boolean deshabilitarCampos = false;
		if(!convenioManejaMontos)
		{//deshabilitar los campos clasificacion SE y tipoAfiliado
			deshabilitarCampos = true; 
		}else{
			IMontosCobroMundo montosCobroMundo=FacturacionFabricaMundo.crearMontosCobroMundo();
			DTOMontosCobro dtoMontosCobro=new DTOMontosCobro();
			dtoMontosCobro = montosCobroMundo.obtenerFechaMaximaMonto(citaForm.getConvenio());
			if(dtoMontosCobro == null)
			{	msgError= mensajes.getMessage("citaForm.noTieneMontosVigentes", citaForm.getNombreConvenioCapitado());
				deshabilitarCampos = true;
			}
		}
		
		
		logger.info("codigo paciente >> "+codigoUsuarioCapitado);
		//logger.info("Verificado usuario capitado=> "+citaForm.isVerificadoUsuarioCapitado());
		citaForm.setCodigoUsuarioCapitado(codigoUsuarioCapitado);		
		this.cerrarConexion(con);
		
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			ahsr_response.setContentType("text/xml");
			ahsr_response.setCharacterEncoding("iso-8859-1");
			ahsr_response.setHeader("Cache-Control", "no-cache");
			String respuesta = "<respuesta>"+
				"<estatrosocial>"+citaForm.getEstratoSocial()+"</estatrosocial>"+
				"<nombreestatrosocial>"+citaForm.getNombreClasificacionSocial()+"</nombreestatrosocial>"+
				"<tipoafiliado>"+citaForm.getTipoAfiliado()+"</tipoafiliado>"+
				"<nombretipoafiliado>"+citaForm.getNombreTipoAfiliado()+"</nombretipoafiliado>"+
				"<naturalezapaciente>"+citaForm.getNaturalezaPaciente()+"</naturalezapaciente>"+
				"<nombrenaturaleza>"+citaForm.getNombreNaturaleza()+"</nombrenaturaleza>"+
				"<convenio>"+citaForm.getConvenioCapitado()+"</convenio>"+
				"<telefono>"+citaForm.getTelefono()+"</telefono>"+
				"<celular>"+citaForm.getCelular()+"</celular>"+
				"<otrostelefonos>"+citaForm.getOtrosTelefonos()+"</otrostelefonos>"+
				
				"<nombre-paciente>"+(UtilidadTexto.isEmpty(nombrePaciente)?"El paciente con tipo ID "+citaForm.getTipoIdentificacionPaciente().split("-")[0]+" y numero de ID "+citaForm.getNumeroIdentificacionPaciente()+" no ha sido registrado en el sistema.":nombrePaciente)+"</nombre-paciente>"+
				"<nombre-convenio>"+(citaForm.getNombreConvenioCapitado().equals("")?"Seleccione":citaForm.getNombreConvenioCapitado())+"</nombre-convenio>"+
				"<tipo-regimen>"+(citaForm.getTipoRegimenConvenio().equals("")?ConstantesBD.codigoNuncaValido+"":citaForm.getTipoRegimenConvenio())+"</tipo-regimen>";
			//Se adiciona el resultado de contratos
			if(numContratos>0)
			{
				respuesta += "<contratos>";
				for(int i=0;i<numContratos;i++)
				{
					if(mapaContratos.containsKey("codigo_contrato_"+i))
						respuesta += "<contrato>"+mapaContratos.get("codigo_contrato_"+i)+ConstantesBD.separadorSplit+mapaContratos.get("numero_contrato_"+i)+"</contrato>";
					else
						respuesta += "<contrato>"+mapaContratos.get("codigo_"+i)+ConstantesBD.separadorSplit+mapaContratos.get("numero_contrato_"+i)+"</contrato>";
				}
				respuesta += "</contratos>";
			}
			else if(codigoUsuarioCapitado!=ConstantesBD.codigoNuncaValido)
			{
				respuesta += "<escapitado>"+ConstantesBD.acronimoSi+"</escapitado>";
				respuesta += "<contratos></contratos>";
				ElementoApResource elemento = new ElementoApResource("errors.notEspecific");
				elemento.agregarAtributo("El Paciente no tiene Capita Vigente.");
				citaForm.getArrayListUtilitario_2().add(elemento);				
			}
			else
			{
				respuesta += "<contratos></contratos>";
				respuesta += "<escapitado>"+ConstantesBD.acronimoNo+"</escapitado>";
			}
			//mensaje de error cuando montos cobro no estan vigentes
			if(msgError != null){
				respuesta += "<error>"+msgError+"</error>";
			}
			//deshabiliata campos clasificacion SE y Tipo Afiliado cuando convenio no maneja montos
			if(deshabilitarCampos){
				respuesta += "<deshabilitar>"+deshabilitarCampos+"</deshabilitar>";
			}
			
			respuesta += "</respuesta>";
			logger.info("***************************************************\n\n");
	        ahsr_response.getWriter().write(respuesta);
	        UtilidadBD.closeConnection(con);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionVerificarUsuarioCapitado: "+e);
		}
		
		return null;	
	}
	
	/**
	 * Mï¿½todo implementado para realizar el cambio de convenio al reservar la cita
	 * @param con
	 * @param citaForm
	 * @param ahsr_response
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ActionForward accionCambiarConvenio(Connection con, CitaForm citaForm, HttpServletResponse ahsr_response) throws IPSException 
	{
		Convenio convenio = new Convenio();
		logger.info("Convenio elegido=> "+citaForm.getConvenio());
		convenio.cargarResumen(con, citaForm.getConvenio());
		String tipoRegimen = convenio.getTipoRegimen();
		logger.info("Tipo regimen consultado=> "+tipoRegimen);
		//Se consultan los contratos vigentes del conveio
		citaForm.setEsUsuarioCapitado(ConstantesBD.acronimoNo);
		HashMap mapaContratos = new HashMap();
		int numContratos = 0;
		
		/**MT 2163- Validar cuando el convenio no maneja Montos Cobro,
		 * no es requerido la clasificacion S.E. ni el tipo de Afiliado*/
		MessageResources mensajes=MessageResources.getMessageResources(
		"com.servinte.mensajes.agenda.CitaForm");
		String msgError = null;
		boolean deshabilitarCampos = false;
		if(!convenio.isConvenioManejaMontoCobro())
		{//deshabilitar los campos clasificacion SE y tipoAfiliado
			deshabilitarCampos = true;
		}else{
			IMontosCobroMundo montosCobroMundo=FacturacionFabricaMundo.crearMontosCobroMundo();
			DTOMontosCobro dtoMontosCobro=new DTOMontosCobro();
			dtoMontosCobro = montosCobroMundo.obtenerFechaMaximaMonto(convenio.getCodigo());
			if(dtoMontosCobro == null){
				msgError= mensajes.getMessage("citaForm.noTieneMontosVigentes", convenio.getNombre());
				deshabilitarCampos = true;
			}	
		}
		
		if(convenio.getTipoContrato()== ConstantesBD.codigoTipoContratoCapitado){
			if(citaForm.getTipoIdentificacionPaciente() != null && !UtilidadTexto.isEmpty(citaForm.getTipoIdentificacionPaciente().split("-")[0]) 
					&& !UtilidadTexto.isEmpty(citaForm.getNumeroIdentificacionPaciente())){
				InfoDatosString info = UtilidadValidacion.esPacienteCapitadoVigente(
						con,
						UtilidadFecha.getFechaActual(),
						citaForm.getNumeroIdentificacionPaciente(),
						citaForm.getTipoIdentificacionPaciente().split("-")[0]);
				
				if(info.getActivo())
				{
					citaForm.setEsUsuarioCapitado(ConstantesBD.acronimoSi);
					//se evalua en conv_usuarios_capitados
					if(info.getDescripcion().equals("1"))
					{
						//---Se consultan los contratos vigentes para el usuario
						//verifica que el paciente sea un usuario capitado en la estructura de usuarios_capitados		
						int codigoUsuarioCapitado=Utilidades.getCodigoUsuarioCapitado(con, citaForm.getTipoIdentificacionPaciente().split("-")[0], citaForm.getNumeroIdentificacionPaciente());
						mapaContratos=Utilidades.convenioUsuarioCapitado(con, codigoUsuarioCapitado);									
					}
					else if(info.getDescripcion().equals("2"))
					{
						HashMap mapaPaciente = Utilidades.obtenerDatosPersona(con, citaForm.getTipoIdentificacionPaciente().split("-")[0], citaForm.getNumeroIdentificacionPaciente());
						SubirPaciente subirPac = new SubirPaciente();
						mapaContratos = subirPac.consultarUsuarioXConvenio(con,mapaPaciente.get("codigo_0").toString());
					}
				}
			}
			else{
				mapaContratos =	Convenio.consultarContratosVigentesConvenio(con,citaForm.getConvenio());
			}
		}
		else{
			mapaContratos =	Convenio.consultarContratosVigentesConvenio(con,citaForm.getConvenio());
		}
		numContratos = Utilidades.convertirAEntero((mapaContratos.get("numRegistros")+""));
				
		cerrarConexion(con);
		
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		if (citaForm.getEstadoant().equals("1"))
		{
			citaForm.setListando(true);
			citaForm.setEstadoant("2");
			
		}
		
		if (!citaForm.isListando())
		{ 
		try
		{
			ahsr_response.setContentType("text/xml");
			ahsr_response.setHeader("Cache-Control", "no-cache");
			String respuesta = "<respuesta>"+
				"<tipo-regimen>"+tipoRegimen+"</tipo-regimen>";
			//Se adiciona el resultado de contratos
			if(numContratos>0)
			{
				if(mapaContratos.get("tipo_regimen_0") != null){
					citaForm.setTipoRegimenConvenio(mapaContratos.get("tipo_regimen_0")+"");
				}
				if(mapaContratos.get("clasificacion_socio_economica_0") != null){
					citaForm.setEstratoSocial(Utilidades.convertirAEntero(mapaContratos.get("clasificacion_socio_economica_0").toString()));
				}
				respuesta += "<contratos>";
				for(int i=0;i<numContratos;i++)
				{ 
					if(mapaContratos.containsKey("codigo_contrato_"+i))
						respuesta += "<contrato>"+mapaContratos.get("codigo_contrato_"+i)+ConstantesBD.separadorSplit+mapaContratos.get("numero_contrato_"+i)+"</contrato>";
					else
						respuesta += "<contrato>"+mapaContratos.get("codigo_"+i)+ConstantesBD.separadorSplit+mapaContratos.get("numero_contrato_"+i)+"</contrato>";
				}
				respuesta += "</contratos>";
			}
			//mensaje de error cuando montos cobro no estan vigentes
			if(msgError != null){
				respuesta += "<error>"+msgError+"</error>";
			}
			//deshabiliata campos clasificacion SE y Tipo Afiliado cuando convenio no maneja montos
			if(deshabilitarCampos){
				respuesta += "<deshabilitar>"+deshabilitarCampos+"</deshabilitar>";
			}
			
			respuesta += "</respuesta>";
	        ahsr_response.getWriter().write(respuesta);
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionVerificarUsuarioCapitado: "+e);
		}
		}

		if (citaForm.isListando())
		{citaForm.setListando(false);
		ahsr_response.setContentType("text/xml");
		ahsr_response.setHeader("Cache-Control", "no-cache");
		String respuesta="";
		try {
			ahsr_response.getWriter().write(respuesta);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}

		return null;
	}
	
	
	@SuppressWarnings("deprecation")
	private void cargarDatosForResAuto(Connection con, CitaForm citaForm, UsuarioBasico usuario, HttpSession session, HttpServletRequest request)
	{
		logger.info("\n\n valor del mapa "+citaForm.getItemsSeleccionados());
		InstitucionBasica institucionBasica = (InstitucionBasica)session.getAttribute("institucionBasica");
		logger.info("el codigo de la institcion es >>>>>>>>>>>>>>>>>>>> "+institucionBasica.getCodigo());
		DtoRespAutorizacion dto = new DtoRespAutorizacion();
		
		if(citaForm.getItemSeleccionado("dtoRespAuto_"+citaForm.getPosCita()+"_"+citaForm.getPosServicio()) instanceof DtoRespAutorizacion){
			dto = (DtoRespAutorizacion) citaForm.getItemSeleccionado("dtoRespAuto_"+citaForm.getPosCita()+"_"+citaForm.getPosServicio());
			citaForm.setDtoRespAuto(dto);
			logger.info("esta reconociendo que hay una instancia del dto");
			logger.info("dto >>>>>>>>>>>>>>>>>>>>>>>>>> Estado :"+dto.getEstadorespauto());
			logger.info("dto Forma >>>>>>>>>>>>>>>>>>>>>>>>>> Estado :"+citaForm.getDtoRespAuto().getEstadorespauto());
			if(!citaForm.getDtoRespAuto().isEstadoIngresoResp()){			
				ActionErrors errors = new ActionErrors();  
				errors = Autorizaciones.evaluarAccionGuardarRespSolAutoAsigCita(citaForm.getDtoRespAuto());
				if(!errors.isEmpty()){
					citaForm.getDtoRespAuto().setEstadoIngresoResp(false);
					saveErrors(request, errors);
				}
			}
		}else{
			logger.info(citaForm.getItemSeleccionado("dtoRespAuto_"+citaForm.getPosCita()+"_"+citaForm.getPosServicio()));
			logger.info("No es una Instacia del DtoRespAutorizacion o no se ha instanciado algun objeto en esa posicion del mapa");
			citaForm.setDtoRespAuto(dto);
			citaForm.setItemSeleccionado("dtoRespAuto_"+citaForm.getPosCita()+"_"+citaForm.getPosServicio(),citaForm.getDtoRespAuto());
			
			citaForm.setArrayCargosPerRecAuto(UtilidadesAdministracion.obtenerCargosInstitucion(con, institucionBasica.getCodigo(), ConstantesBD.acronimoSi));
			citaForm.setArrayTiposVigencia(UtilidadesAdministracion.obtenerTiposVigencia(con, institucionBasica.getCodigo(), ConstantesBD.acronimoSi, ""));
			citaForm.getDtoRespAuto().getServicio().setCodigo(citaForm.getCodigoServicio());
			citaForm.getDtoRespAuto().getServicio().setNombre(Utilidades.obtenerTipoServicio(con,citaForm.getCodigoServicio()));
			citaForm.getDtoRespAuto().setFechaAutorizacion(dto.getFechaAutorizacion().equals("")?UtilidadFecha.getFechaActual():dto.getFechaAutorizacion());
			citaForm.getDtoRespAuto().setHoraAutorizacion(dto.getHoraAutorizacion().equals("")?UtilidadFecha.getHoraActual():dto.getHoraAutorizacion());
			citaForm.getDtoRespAuto().setPersonaRegistro(dto.getPersonaRegistro().equals("")?usuario.getNombreUsuario():dto.getPersonaRegistro());
			citaForm.getDtoRespAuto().setCargoPerRegistra(dto.getCargoPerRegistra().equals("")?usuario.getNombreCargo():dto.getCargoPerRegistra());
		}
		
	}
	
	
	/**
	 * Mï¿½todo que se encarga de realizar el resumen de la impresiï¿½n
	 * 
	 * @param mapping Mapping con los sitios a los que puede 
	 * acceder struts
	 * @param request Objeto request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("deprecation")
	private ActionForward guardarRespAutorizacion(CitaForm citaForm, ActionMapping mapping, HttpServletRequest	request) throws Exception
	{
		ActionErrors errors = new ActionErrors();  
		if(citaForm.getDtoRespAuto().getEstadorespauto().equals(ConstantesIntegridadDominio.acronimoAutorizado)
				&& citaForm.getDtoRespAuto().getServicio().getNombre().equals(ConstantesBD.codigoServicioCamaEstancias)){
			citaForm.getDtoRespAuto().setCantidadAutorizada("1");
			logger.info("la cantida esta llena "+citaForm.getDtoRespAuto().getCantidadAutorizada());
		}
		errors = Autorizaciones.evaluarAccionGuardarRespSolAutoAsigCita(citaForm.getDtoRespAuto());
		if(!errors.isEmpty()){
			citaForm.getDtoRespAuto().setEstadoIngresoResp(false);
			saveErrors(request, errors);
		}else
			citaForm.getDtoRespAuto().setEstadoIngresoResp(true);
		citaForm.setItemSeleccionado("dtoRespAuto_"+citaForm.getPosCita()+"_"+citaForm.getPosServicio(),citaForm.getDtoRespAuto());
		return mapping.findForward("regRespSolAuto");
	}
	
	/**
	 * Mï¿½todo implementado para iniciar la adicion de archivos adjuntos
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionIniciarAdjuntarArchivos(
			Connection connection,
			CitaForm citaForm, 
			ActionMapping mapping,
			UsuarioBasico usuario) 
	{
		//Si es un nuevo adjunto se adiciona a la estructurasz	
		DtoRespAutorizacion dtoRespAuto = new DtoRespAutorizacion();
		dtoRespAuto = (DtoRespAutorizacion) citaForm.getItemSeleccionado("dtoRespAuto_"+citaForm.getPosCita()+"_"+citaForm.getPosServicio());
				
		DtoAdjAutorizacion adjRespAutorizacion = new DtoAdjAutorizacion();
		adjRespAutorizacion.setUsuarioModifica(usuario);
		dtoRespAuto.getAdjuntos().add(adjRespAutorizacion);
		dtoRespAuto.setPosicionAdjuntos(dtoRespAuto.getAdjuntos().size()-1);	
		
		citaForm.setItemSeleccionado("dtoRespAuto_"+citaForm.getPosCita()+"_"+citaForm.getPosServicio(),dtoRespAuto);
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("seleccionarArchivo");
	}
	
	/**
	 * Mï¿½todo implementado para guardar el motivo de autorizacion de cada cita a signar
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionForward guardarMotivoAutorizacion(
			Connection connection,
			CitaForm citaForm, 
			ActionMapping mapping,
			HttpServletRequest request) 
	{
		//guardar el motivo de la consulta
		ActionErrors errors = new ActionErrors(); 
		
		if(citaForm.getUsuarioAutoriza().equals(""))
			errors.add("descripcion",new ActionMessage("errors.required","El Usuario que Autoriza "));
		if(citaForm.getUsuarioAutoriza().length()>125)
			errors.add("descripcion",new ActionMessage("errors.maxlength","Usuario que Autoriza ","125"));
		if(UtilidadCadena.tieneCaracteresEspecialesGeneral(citaForm.getUsuarioAutoriza()))
			errors.add("descripcion",new ActionMessage("errors.caracteresInvalidos","El Campo Usuario que Autoriza "));
		
		if(citaForm.getMotivoAutorizacionCita().equals(""))
			errors.add("descripcion",new ActionMessage("errors.required","El Motivo de la Autorizaciï¿½n "));
		if(citaForm.getMotivoAutorizacionCita().length()>256)
			errors.add("descripcion",new ActionMessage("errors.maxlength","Motivo de la Autorizaciï¿½n ","256"));
		if(UtilidadCadena.tieneCaracteresEspecialesGeneral(citaForm.getMotivoAutorizacionCita()))
			errors.add("descripcion",new ActionMessage("errors.caracteresInvalidos","El Campo Motivo de la Autorizaciï¿½n "));
		
		
		
		if(!errors.isEmpty()){
			UtilidadBD.closeConnection(connection);
			saveErrors(request, errors);
			return mapping.findForward("busquedaServicios");
		}else{
			citaForm.setItemSeleccionado("motivoAuto_"+citaForm.getPosCita(),citaForm.getMotivoAutorizacionCita());
			citaForm.setItemSeleccionado("usuarioAuto_"+citaForm.getPosCita(),citaForm.getUsuarioAutoriza());
			citaForm.setItemSeleccionado("requiereAuto_"+citaForm.getPosCita(),ConstantesBD.acronimoSi);
			citaForm.setPreguntarAutorizacion(ConstantesBD.acronimoNo);
			UtilidadBD.closeConnection(connection);
			return mapping.findForward("busquedaServicios");
		}
	}
	
	/**
	 * Mï¿½todo implementado para guardar el motivo de autorizacion de cada cita ha reservar
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @return
	 */
	@SuppressWarnings("unused")
	private ActionForward guardarMotivoAutorizacionReserva(
			Connection connection,
			CitaForm citaForm, 
			ActionMapping mapping) 
	{
		//guardar el motivo de la consulta
		citaForm.setItemSeleccionado("motivoAuto_"+citaForm.getPosCita(),citaForm.getMotivoAutorizacionCita());
		citaForm.setItemSeleccionado("usuarioAuto_"+citaForm.getPosCita(),citaForm.getUsuarioAutoriza());
		citaForm.setItemSeleccionado("requiereAuto_"+citaForm.getPosCita(),ConstantesBD.acronimoSi);
		citaForm.setPreguntarAutorizacion(ConstantesBD.acronimoNo);
		/*logger.info("posicion >>>>>"+citaForm.getPosCita());
		logger.info("motivo Autorizacion cita >>>>>>>> "+citaForm.getItemSeleccionado("motivoAuto_"+citaForm.getPosCita()));
		logger.info("usuario que Autoriza cita >>>>>>>> "+citaForm.getItemSeleccionado("usuarioAuto_"+citaForm.getPosCita()));
		logger.info("Requiere Autorizacion cita >>>>>>>> "+citaForm.getItemSeleccionado("requiereAuto_"+citaForm.getPosCita()));
		logger.info("preguntar autorizacion >>>>>>> "+citaForm.getPreguntarAutorizacion());
		*/
		return mapping.findForward("busquedaServicios");
	}
	
	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	private ActionForward verificarEstCitasPac(
			Connection connection,
			CitaForm forma, 
			ActionMapping mapping,
			Cita mundoCita,
			HttpServletRequest request)
	{
		boolean advertencia = false;
		ActionErrors errors = new ActionErrors();
		
		
		/**MT 2163- Validar cuando el convenio no maneja Montos Cobro, 
		 * no es requerido la clasificacion S.E. ni el tipo de Afiliado*/
		IConveniosMundo conveniosMundo=FacturacionFabricaMundo.crearcConveniosMundo();
		Convenios convenio=new Convenios();
		convenio=conveniosMundo.findById(forma.getConvenio());
		if(convenio.getManejaMontos().equals(ConstantesBD.acronimoSi))
		{
			//Se verifica que se haya seleccionado la clasificacion socioeconomico o estrato social
			if(forma.getEstratoSocial()==ConstantesBD.codigoNuncaValido)
			{
				errors.add(
						"estratoSocial",
						new ActionMessage("errors.required", "La Clasificación Socioeconómica")
					);
			}
			
			//Se verifica que se haya seleccionado el tipo de afiliado
			if(forma.getTipoAfiliado().equals(ConstantesBD.codigoNuncaValido+""))
			{
				errors.add(
						"tipoAfiliado",
						new ActionMessage("errors.required", "El Tipo de Afiliado")
					);
			}
			saveErrors(request, errors);
		}
		
		if(errors.isEmpty())
		{	
			// verifiacr si el paciente posee citas incumpladas
			//ActionMapping mapping, CitaForm forma, HttpServletRequest request,int estado,Connection lc_con, Cita mundoCita
			if(!forma.isMantenerPacienteCargado())
				cargarPacienteSesion(request, forma, connection);
			
			PersonaBasica paciente =(PersonaBasica) request.getSession().getAttribute("pacienteActivo");
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			if(paciente.getCodigoPersona()>0)
			{
				//logger.info("codigo institucion >>>>> "+institucion.getCodigo());
				//logger.info("codigo de paciente >>>>> "+paciente.getCodigoPersona());
				
				//verificar el estado de las citas
				String [] parametrosModulo = {
						ConstantesValoresPorDefecto.nombreInstitucionManejaMultasPorIncumplimiento,
						ConstantesValoresPorDefecto.nombreBloqueaCitasReservaAsignReprogPorIncump,
						ConstantesValoresPorDefecto.nombreFechaInicioControlMultasIncumplimientoCitas
						};
				if(mundoCita.validarEstadoCitasPaciente(connection, institucion, paciente, usuario, ConstantesBD.codigoModuloConsultaExterna, parametrosModulo))
				{
					// recorrer cada una el mapa que contiene las citas y 
					// verificar si el  usuario que esta en sesiï¿½n esta autorizado para la reservar, asignar o reprogrmas
					// citas aunque el paciente posea multas por incumplimiento de citas
					int numRegistros = forma.getNumeroItemsSeleccionados();
					for(int i=0; i<numRegistros;i++)
					{
						//setItemSeleccionado("codigoAgenda_" + li_i, getItemSeleccionado("codigoAgenda_" + (li_i + 1) ));
						//setItemSeleccionado("codigoCentroAtencion_" + li_i,getItemSeleccionado("codigoCentroAtencion_" + (li_i + 1) ));
						/*logger.info("---------------------------------");
						logger.info("al verificar se encontraron citas  incumplidad");
						logger.info("codigo centro de atencion >>>>>> "+forma.getItemSeleccionado("codigoCentroAtencion_"+ i).toString());
						logger.info("---------------------------------");
						logger.info("codigo de Agenda >>>>> "+forma.getItemSeleccionado("codigoAgenda_"+ i).toString());
						logger.info("motivo >>"+forma.getMotivoAutorizacionCita());
						logger.info("usuario >>"+forma.getUsuarioAutoriza());
						*/
						if(UtilidadesConsultaExterna.esActividadAurtorizada(connection, 
								Utilidades.convertirAEntero(forma.getItemSeleccionado("codigoAgenda_"+ i).toString()), 
								ConstantesBD.codigoActividadAutorizadaAutorizarCitasPacientesConMulta, 
								usuario.getLoginUsuario(), 
								Utilidades.convertirAEntero(forma.getItemSeleccionado("codigoCentroAtencion_"+ i).toString()),false)){
							//logger.info("el usuario esta autorizado para asignar citas asi el paciente posea citas incumplidas");
							// aqui se debe preguntar si desa autorizar la reserva o asignacion de la cita
							forma.setItemSeleccionado("motivoAuto_"+ i,forma.getMotivoAutorizacionCita());
							forma.setItemSeleccionado("usuarioAuto_"+ i,forma.getUsuarioAutoriza());
							forma.setItemSeleccionado("requiereAuto_"+ i,ConstantesBD.acronimoSi);
							forma.setItemSeleccionado("guardarAuto_"+ i,ConstantesBD.acronimoSi);
							forma.setVerificarEstCitasPac(ConstantesBD.acronimoSi);
							advertencia = true;
						}else{
							// se cancela el proceso de asignar o reservar cita.
							//logger.info("Se Cancela el Proceso de Asignar o Reservar Cita");
							forma.setItemSeleccionado("motivoAuto_"+ i,"");
							forma.setItemSeleccionado("usuarioAuto_"+ i,"");
							forma.setItemSeleccionado("requiereAuto_"+ i,ConstantesBD.acronimoNo);
							forma.setItemSeleccionado("guardarAuto_"+ i,ConstantesBD.acronimoNo);
							forma.setVerificarEstCitasPac(ConstantesBD.acronimoNo);
						}
					}
					forma.setPreguntarAutorizacion(ConstantesBD.acronimoNo);
					forma.setCitasIncumpl(ConstantesBD.acronimoSi);
					forma.setCitasIncumplidas(mundoCita.getCitasIncumplidas());
					forma.setVerificarEstCitasPac(ConstantesBD.acronimoSi);
				}else{
					//logger.info("no posee multas >>>>>>>>>>>");
					forma.setPreguntarAutorizacion(ConstantesBD.acronimoNo);
					forma.setCitasIncumpl(ConstantesBD.acronimoNo);
					forma.setVerificarEstCitasPac(ConstantesBD.acronimoNo);
					try { 
						//logger.info("esta entrando hacer la reserva de la cita");
						return reservar(mapping, forma, request, connection, usuario,false);
					} catch (Exception e) {
						//logger.info("Error en la reserva de la cita");
						e.printStackTrace();
					}
				}
			}else{
				try{
					return reservar(mapping, forma, request, connection, usuario,false);
				}catch (Exception e) {
					logger.info("Error en la reserva de la cita");
					e.printStackTrace();
				}
			}
			//logger.info("valor de citas  incumplidas >>>>>>>>> "+forma.getCitasIncumpl());
			if(!advertencia)
			{
				forma.setBloqueoUsuario(ConstantesBD.acronimoSi);
				ElementoApResource elemento = new ElementoApResource("errors.notEspecific");
				elemento.agregarAtributo("No se Puede Realizar la Reserva de la(s) Cita(s). Porque el Paciente tiene Citas Incumplidas y El Usuario No posee los Permisos para Autorizar");
				forma.getArrayListUtilitario_2().add(elemento);
			}else
				forma.setBloqueoUsuario(ConstantesBD.acronimoNo);
		}	
		
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("cita");
	}
	
	
	/**
	 * Método que se encarga de validar la descripción del servicio pendiente por autorizar si falló en :
	 * La generación de autorizacion (DCU-1106) mostrar SERVICIO + ACRONIMO de dias tramite del grupo de servicio
	 * En el no de consecutivos disponibles y niveles (DCU-1115), mostrar SERVICIO + DIAS de tramite del grupo de servicios
	 * RQF 02-0025 Autorizaciones Capitación Subcontratada
	 * 
	 * Cambio DCU 13 Version 2.1
	 *  
	 *  @author Camilo Gómez
	 */	
	private String validacionImpresionDescripcionServicio(int solicitud,int servicio,
			HttpServletRequest request,boolean procesoGeneracion)
	{		
		//RQF 02-0025 Autorizaciones Ordenes Ambulatorias
		//Paso la validacion de contrato=Capitado y capitacionSubcontratada='S' entonces 
		//aplica para autorizacion Capita Orden Ambulatoria
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		IAutorizacionCapitacionOrdenesAmbulatoriasMundo autorizacionCapitacionOrdenesAmbulatoriasMundo=CapitacionFabricaMundo.crearAutorizacionCapitacionOrdenesAmbulatoriasMundo();
		DtoAutorizacionCapitacionOrdenAmbulatoria dtoAutorizacionCapitacionOrdenAmbulatoria=new DtoAutorizacionCapitacionOrdenAmbulatoria();
			dtoAutorizacionCapitacionOrdenAmbulatoria.setCodigoServicioAutorizar(servicio);//solicitudGenerada.getCodigoServicioCitaAutorizar());
			dtoAutorizacionCapitacionOrdenAmbulatoria.setNumeroSolicitudAutorizar(solicitud);//solicitudGenerada.getNumeroSolicitudCitaAutorizar());
			if(procesoGeneracion)
				dtoAutorizacionCapitacionOrdenAmbulatoria.setProcesoGeneracionAutoriz(true);//indica si viene desde las validaciones de -Generacion Autorizacion- se debe agregar a la descripcion, el ACRONIMO
			else
				dtoAutorizacionCapitacionOrdenAmbulatoria.setProcesoGeneracionAutoriz(false);//indica si viene desde las validaciones de -Consecutivos y Nivel- se debe agregar a la descripcion, los DIAS
		
		autorizacionCapitacionOrdenesAmbulatoriasMundo.validarDescripcionServicio(dtoAutorizacionCapitacionOrdenAmbulatoria, ins);
		
		return dtoAutorizacionCapitacionOrdenAmbulatoria.getNombreServicioAutorizar();
	}
	
	
	/**
	 * Método que se encarga de inicalizar los select de Centro de Atención y Unidades de Agenda para Asignar Cita
	 * desde la funcionalidad de Ordenes Ambulatorias
	 *  
	 * @author Camilo Gómez
	 * @param citaForm
	 * @param con
	 * @param usuario
	 * @param request
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
	private void inicializarSelectCentroAtencionUnidadAgenda(CitaForm citaForm,Connection con,UsuarioBasico usuario,
			HttpServletRequest request,String servicioA)
	{
		ICentroCostoMundo centroCostoMundo=AdministracionFabricaMundo.crearCentroCostoMundo();
		DtoCentroCosto dtoCentroCosto=new DtoCentroCosto();
		int tamanoMapUnidades=ConstantesBD.codigoNuncaValido;
		ArrayList<DtoCentroCosto> listaCentroXUnidadAgen=new ArrayList<DtoCentroCosto>();			
		
		//consulta de centroAtencionXCentroCosto ->parametros (centroCosto)
		dtoCentroCosto.setCodigoCentroCosto(Utilidades.convertirAEntero(citaForm.getCentroCostoAmbulatorioSel()));
		dtoCentroCosto= centroCostoMundo.obtenerCentroAtencionXCentroCosto(dtoCentroCosto);
		
		//1. consulta de unidadesAgendaXCentroCosto ->parametros (centroAtencion-centroCosto)			
		listaCentroXUnidadAgen = centroCostoMundo.listaCentroCostoUnidadConsulta(dtoCentroCosto);
	
		if(dtoCentroCosto!=null)
		{
			//Se postula el centro de atencion de acuerdo al centro de costo seleccionado
			HashMap centroAtencionMap=new HashMap();
			centroAtencionMap.put("codigo_"+0, dtoCentroCosto.getCodigoCentroAtencion());
			centroAtencionMap.put("nombre_"+0, dtoCentroCosto.getNombreCentroAtencion());
			centroAtencionMap.put("numRegistros", 1);
			citaForm.setCentrosAtencionAutorizados(centroAtencionMap);
			
			//2. consulta de UnidadesAgendaXUsuarioXCentroAtencion ->parametros(usuario-centroAtencion-servicio)
			citaForm.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuarioXServicio(con, usuario.getLoginUsuario(), dtoCentroCosto.getCodigoCentroAtencion(), ConstantesBD.codigoActividadAutorizadaAsignarCitas,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral, servicioA));
			tamanoMapUnidades=Utilidades.convertirAEntero(citaForm.getUnidadesAgendaAutorizadas().get("numRegistros").toString());
		}
		
		
		HashMap listaUnidadesAgendaFinal=new HashMap();
		listaUnidadesAgendaFinal.put("numRegistros", ConstantesBD.codigoNuncaValido);
		
		//Se recorren las dos listan para comparar, y se da prioridad a la consulta 1. unidadesAgendaXCentroCosto
		for (DtoCentroCosto unidad : listaCentroXUnidadAgen) 
		{
			if(tamanoMapUnidades>0)
			{
				for(int i=0;i<tamanoMapUnidades;i++)
				{
					if(unidad.getCodigoUnidadConsulta()==Utilidades.convertirAEntero(citaForm.getUnidadesAgendaAutorizadas("codigo_"+i).toString()))
					{
						listaUnidadesAgendaFinal.put("codigo_"+i, unidad.getCodigoUnidadConsulta());
						listaUnidadesAgendaFinal.put("nombre_"+i, unidad.getDescripcionUnidadConsulta());
						listaUnidadesAgendaFinal.put("numRegistros", i);
					}
				}
			}
		}
		
		if(Utilidades.convertirAEntero(listaUnidadesAgendaFinal.get("numRegistros").toString())>=0)
		{
			int tamUnidadAgenda=Utilidades.convertirAEntero(listaUnidadesAgendaFinal.get("numRegistros").toString())+1;
			listaUnidadesAgendaFinal.put("numRegistros", tamUnidadAgenda);
		}			
		citaForm.setUnidadesAgendaAutorizadas(listaUnidadesAgendaFinal);
	}
	
	
	/**
	 * Método que se encarga de validar si se generar autorizacion para el servicio de la orden.
	 * DCU 13 - Asignación de Citas v2.3
	 * MT 4681
	 * 
	 * @author Camilo Gomez
	 * @param con
	 * @param citaForm
	 * @param usuario
	 * @param paciente
	 * @param listaDtoAdministrarSolicitudesAutorizar
	 * @param errores
	 * @throws IPSException
	 */
	private void cargarInfoVerificarGeneracionAutorizacion(Connection con,CitaForm citaForm, UsuarioBasico usuario, PersonaBasica paciente,
			List<DTOAdministrarSolicitudesAutorizar> listaDtoAdministrarSolicitudesAutorizar, ActionErrors errores)throws IPSException
	{
		DtoSubCuentas dtoSubCuenta = null;
		AutorizacionCapitacionDto autorizacionCapitacionDto = null;
		MontoCobroDto montoCobroAutorizacion				= null;
		DatosPacienteAutorizacionDto datosPacienteAutorizar = null;
		OrdenAutorizacionDto ordenAutorizacionDto 			= null;
		List<OrdenAutorizacionDto> listaOrdenesAutorizar	= null;
		List<ServicioAutorizacionOrdenDto> listaServiciosPorAutorizar = null;
		ContratoDto contratoDto = null;
		ConvenioDto convenioDto = null;
		ManejoPacienteFacade manejoPacienteFacade = null;
		OrdenesFacade ordenesFacade	= null;
		List<AutorizacionCapitacionDto> listaAutorizacionCapitacion = null;
		try{
			UtilidadBD.iniciarTransaccion(con);
			dtoSubCuenta = citaForm.getInfoResponsableCobertura().getDtoSubCuenta();
			listaOrdenesAutorizar 	= new ArrayList<OrdenAutorizacionDto>();
			ordenesFacade			= new OrdenesFacade();
						
			for(DTOAdministrarSolicitudesAutorizar solici: listaDtoAdministrarSolicitudesAutorizar){
				
				convenioDto = new ConvenioDto();
				convenioDto.setCodigo(Utilidades.convertirAEntero(solici.getCodigoConvenioServicioAutorizar()));
				convenioDto.setNombre(dtoSubCuenta.getConvenio().getNombre());
				if(dtoSubCuenta.getDatosConvenio().getManejaPreupuestoCapitacion()!=null &&
						dtoSubCuenta.getDatosConvenio().getManejaPreupuestoCapitacion().equals(ConstantesBD.acronimoSiChar+"")){
					convenioDto.setConvenioManejaPresupuesto(true);
				}else{
					convenioDto.setConvenioManejaPresupuesto(false);
				}
				contratoDto = new ContratoDto();
				contratoDto.setConvenio(convenioDto);
				contratoDto.setCodigo(dtoSubCuenta.getContrato());
				contratoDto.setNumero(dtoSubCuenta.getNumeroContrato());
				
				ordenAutorizacionDto = new OrdenAutorizacionDto();
				ordenAutorizacionDto.setCodigoOrden(Utilidades.convertirALong(solici.getNumeroSolicitudCitaAutorizar()+""));
				ordenAutorizacionDto.setConsecutivoOrden(solici.getConsecutivoCitaAutorizar());
				ordenAutorizacionDto.setContrato(contratoDto);
				ordenAutorizacionDto.setCodigoCentroCostoEjecuta(solici.getCentroCostoSolicitado());
				ordenAutorizacionDto.setEsPyp(citaForm.isSolPYP());
				ordenAutorizacionDto.setCodigoViaIngreso(paciente.getCodigoUltimaViaIngreso());
				
				if(citaForm.isIndicativoOrdenAmbulatoria()){
					ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenOrdenAmbulatoria);
					String codigoOA=Utilidades.obtenerCodigoOrdenAmbulatoria(con,citaForm.getOrdenAmbulatoria(),usuario.getCodigoInstitucionInt());
					ordenAutorizacionDto.setOtroCodigoOrden(Utilidades.convertirALong(codigoOA));
				}else{
					ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenOrdenMedica);
				}
				//se necesita identificar si el servicio es (interconsulta-procedimiento-cirugia) para la busqueda del servicio 
				ordenAutorizacionDto.setTipoOrden(solici.getTipoSolicitudCita());
				
				//Se consultan datos del servicio
				listaServiciosPorAutorizar	= null;
				listaServiciosPorAutorizar = ordenesFacade.obtenerServiciosPorAutorizar(Utilidades.convertirAEntero(ordenAutorizacionDto.getCodigoOrden()+""),
						ordenAutorizacionDto.getClaseOrden(), ordenAutorizacionDto.getTipoOrden());
				//listaServiciosPorAutorizar.get(0).setFinalidad(dtoSubCuenta.getSolicitudesSubcuenta().get(0).getFinalidadSolicitud());//en cita no lo encontre
				if(listaServiciosPorAutorizar != null && !listaServiciosPorAutorizar.isEmpty()){
					listaServiciosPorAutorizar.get(0).setAutorizar(true);
					if(solici.getUrgente()==ConstantesBD.acronimoSiChar){
						ordenAutorizacionDto.setEsUrgente(true);
						listaServiciosPorAutorizar.get(0).setUrgente(ConstantesBD.acronimoSiChar);
					}else{
						ordenAutorizacionDto.setEsUrgente(false);
						listaServiciosPorAutorizar.get(0).setUrgente(ConstantesBD.acronimoNoChar);
					}
					
					ordenAutorizacionDto.setServiciosPorAutorizar(listaServiciosPorAutorizar);
					listaOrdenesAutorizar.add(ordenAutorizacionDto);
				}
			}
			boolean manejaMonto = UtilidadTexto.isEmpty(dtoSubCuenta.getMontoCobro()) || dtoSubCuenta.getMontoCobro() == 0 ? false : true;
			
			datosPacienteAutorizar	= new DatosPacienteAutorizacionDto();
			datosPacienteAutorizar.setCodigoPaciente(dtoSubCuenta.getCodigoPaciente());
			datosPacienteAutorizar.setCodigoIngresoPaciente(dtoSubCuenta.getIngreso());
			datosPacienteAutorizar.setTipoPaciente(dtoSubCuenta.getTipoPaciente());
			datosPacienteAutorizar.setClasificacionSocieconomica(dtoSubCuenta.getClasificacionSocioEconomica());
			datosPacienteAutorizar.setTipoAfiliado(dtoSubCuenta.getTipoAfiliado());
			datosPacienteAutorizar.setNaturalezaPaciente(dtoSubCuenta.getNaturalezaPaciente());
			datosPacienteAutorizar.setCodConvenioCuenta(paciente.getCodigoConvenio());
			datosPacienteAutorizar.setCuentaAbierta(true);
			datosPacienteAutorizar.setCuentaManejaMontos(manejaMonto);
			datosPacienteAutorizar.setPorcentajeMontoCuenta(dtoSubCuenta.getPorcentajeMontoCobro());
			
			montoCobroAutorizacion	= new MontoCobroDto();
			montoCobroAutorizacion.setCantidadMonto(dtoSubCuenta.getCantidadMontoGeneral());
			montoCobroAutorizacion.setCodDetalleMonto(dtoSubCuenta.getMontoCobro());
			montoCobroAutorizacion.setPorcentajeMonto(dtoSubCuenta.getPorcentajeMontoGeneral());
			montoCobroAutorizacion.setTipoDetalleMonto(dtoSubCuenta.getTipoDetalleMonto());
			montoCobroAutorizacion.setTipoMonto(dtoSubCuenta.getTipoMonto());
			montoCobroAutorizacion.setValorMonto(dtoSubCuenta.getValorMontoGeneral());
			
			autorizacionCapitacionDto = new AutorizacionCapitacionDto();
			autorizacionCapitacionDto.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			autorizacionCapitacionDto.setLoginUsuario(usuario.getLoginUsuario());
			autorizacionCapitacionDto.setCodigoPersonaUsuario(usuario.getCodigoPersona());
			autorizacionCapitacionDto.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAutomatica);
			autorizacionCapitacionDto.setCentroAtencion(usuario.getCodigoCentroAtencion());
			autorizacionCapitacionDto.setDatosPacienteAutorizar(datosPacienteAutorizar);
			autorizacionCapitacionDto.setMontoCobroAutorizacion(montoCobroAutorizacion);
			autorizacionCapitacionDto.setOrdenesAutorizar(listaOrdenesAutorizar);
    		
			//Proceso que se encarga de verificar si se generara o asociara autorizacion para la orden.
			manejoPacienteFacade = new ManejoPacienteFacade();
			listaAutorizacionCapitacion = manejoPacienteFacade.verificarGenerarAutorizacionCapitacion(autorizacionCapitacionDto);
			
			
			if(listaAutorizacionCapitacion!=null && !listaAutorizacionCapitacion.isEmpty()){//Se adiciona mensaje para los servicio que no se autorizaron
				manejoPacienteFacade.obtenerMensajesError(listaAutorizacionCapitacion, errores);
			}
			
			UtilidadBD.finalizarTransaccion(con);
		}catch (IPSException ipsme) {
			UtilidadBD.abortarTransaccion(con);
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * Método que obtiene los mensajes de error despúes de ejecutar el proceso de autorización
	 * 
	 * @param dtoAutorizacion
	 * @param errores
	 */
	private void obtenerMensajesError(AutorizacionCapitacionDto dtoAutorizacion, ActionMessages errores, CitaForm forma) throws IPSException {
		
		try {
			forma.setErrorGeneracionAutorizacion(true);
			if(!dtoAutorizacion.isProcesoExitoso() && dtoAutorizacion.getMensajeErrorGeneral() != null){
				if(dtoAutorizacion.getMensajeErrorGeneral().getParamsMsg() != null
						&& dtoAutorizacion.getMensajeErrorGeneral().getParamsMsg().length > 0){
					errores.add("", new ActionMessage(dtoAutorizacion.getMensajeErrorGeneral().getErrorKey(), dtoAutorizacion.getMensajeErrorGeneral().getParamsMsg()));
				}
				else{
					errores.add("", new ActionMessage(dtoAutorizacion.getMensajeErrorGeneral().getErrorKey()));
				}
			}
				
			if(dtoAutorizacion.isVerificarDetalleError()){
				for(OrdenAutorizacionDto dtoOrden:dtoAutorizacion.getOrdenesAutorizar()){
					if(dtoOrden.getServiciosPorAutorizar() != null
							&& !dtoOrden.getServiciosPorAutorizar().isEmpty()){
						for(ServicioAutorizacionOrdenDto dtoServicio:dtoOrden.getServiciosPorAutorizar()){
							if(!dtoServicio.isAutorizar() || !dtoServicio.isAutorizado()){
								if(dtoServicio.getMensajeError().getParamsMsg() != null 
										&& dtoServicio.getMensajeError().getParamsMsg().length > 0){
									errores.add("", new ActionMessage(dtoServicio.getMensajeError().getErrorKey(),dtoServicio.getMensajeError().getParamsMsg()));
								}
								else{
									errores.add("", new ActionMessage(dtoServicio.getMensajeError().getErrorKey()));
								}
							}
						}
					}
					else if(dtoOrden.getMedicamentosInsumosPorAutorizar() != null
							&& !dtoOrden.getMedicamentosInsumosPorAutorizar().isEmpty()){
						for(MedicamentoInsumoAutorizacionOrdenDto dtoMedicamento:dtoOrden.getMedicamentosInsumosPorAutorizar()){
							if(!dtoMedicamento.isAutorizar() || !dtoMedicamento.isAutorizado()){
								if(dtoMedicamento.getMensajeError().getParamsMsg() != null
										&& dtoMedicamento.getMensajeError().getParamsMsg().length > 0){
									errores.add("", new ActionMessage(dtoMedicamento.getMensajeError().getErrorKey(), dtoMedicamento.getMensajeError().getParamsMsg()));
								}
								else{
									errores.add("", new ActionMessage(dtoMedicamento.getMensajeError().getErrorKey()));
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}
	
}