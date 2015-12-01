package com.princetonsa.action.agenda;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.CsvFile;
import util.InfoDatosInt;
import util.RespuestaValidacion;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.consultaExterna.UtilidadesConsultaExterna;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.agenda.CitaForm;
import com.princetonsa.actionform.agenda.CitasForm;
import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.dto.facturacion.DtoBusquedaMontosCobro;
import com.princetonsa.dto.facturacion.DtoValidacionTipoCobroPaciente;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.dto.odontologia.DtoProgramasServiciosPlanT;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.Medico;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.Cita;
import com.princetonsa.mundo.agenda.Citas;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.facturacion.CondicionesXServicios;
import com.princetonsa.mundo.inventarios.FormatoJustServNopos;
import com.princetonsa.mundo.odontologia.CitaOdontologica;
import com.princetonsa.mundo.odontologia.PlanTratamiento;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.salasCirugia.Peticion;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;
import com.princetonsa.mundo.solicitudes.ValidacionesSolicitud;
import com.princetonsa.pdf.ConsultaCitasPdf;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.IBusquedaMontosCobroServicio;

/**
 * Clase para el manejo del flujo para listados/conjuntos de citas 
 *
 * @version 1.0, Marzo 26 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Raul@PrincetonSA.com">Raï¿½l Cancino</a>
 */
public class CitasAction extends Action 
{
	
	
	private boolean esMedico=false; 
	
	/**serviciosCita
	 * log del sistema
	 */
	private Logger logger = Logger.getLogger(CitasAction.class);
	
	/**
	 * estado del Form
	 */
	private String estado;
	
	/**
	 * codigo de la persona cargada en el resumen
	 */
	//private int codigoPersona;

	/**
	 * indica si existe un paciente cargado
	 */
	private boolean pacienteCargado;
	
	private UsuarioBasico medico;

	private PersonaBasica personaCargada;
	
	public ActionForward execute(	ActionMapping mapping,
														ActionForm form,
														HttpServletRequest request,
														HttpServletResponse response)throws Exception 
	{
		Connection con= null;
		try {

			if (form instanceof CitasForm)
			{
				if (logger.isDebugEnabled()) 
				{
					logger.debug("Entro al Action de Listar Citas");
				}
	
				CitasForm citasForm =(CitasForm) form;
				this.estado = citasForm.getEstado();
				logger.warn("[CitasAction] estado "+estado);
	
				citasForm.setMensaje(new ResultadoBoolean(false));
				
				con = UtilidadBD.abrirConexion();
				
				//Lo primero que vamos a hacer es validar que se
				//cumplan las condiciones.
				HttpSession session = request.getSession();
				this.medico =(UsuarioBasico) session.getAttribute("usuarioBasico");
				this.personaCargada =(PersonaBasica) session.getAttribute("pacienteActivo");
					
	
				citasForm.setInstitucion(medico.getCodigoInstitucionInt());
				//Primera Condiciï¿½n: El usuario debe existir
	
				if (this.medico == null) 
				{
					if (logger.isDebugEnabled()) 
						logger.debug("No existe el usuario");
					
					request.setAttribute("codigoDescripcionError",	"errors.usuario.noCargado");
					return mapping.findForward("paginaError");
				} 
				else if (this.estado == null || this.estado.equals("")) 
				{
					citasForm.reset();
					if (logger.isDebugEnabled()) 
						logger.debug("La accion especï¿½ficada no esta definida ");
					
					request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
				if( UtilidadValidacion.esProfesionalSalud(medico) )
				{
					this.esMedico=true;
				}
				
				if(this.personaCargada==null || personaCargada.getTipoIdentificacionPersona().equals(""))
				{
					this.pacienteCargado=false;
				}
				else
				{
					this.pacienteCargado=true;
					//this.codigoPersona=this.personaCargada.getCodigoPersona();
				}
				if (estado.equalsIgnoreCase("resumenatenciones"))
				{
					citasForm.setIndicador("1");
					return this.accionGeneralCitas(mapping,request,con,citasForm);
				}
				//**********************ESTADOS CONSULTA DE CITAS*************************************************************
				//hacer validaciones de cuenta abierta y cerrada
				if (estado.equals("paciente")) 
				{
					if(pacienteCargado==true)
					{
						UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
						citasForm.setCentroAtencion(usuario.getCodigoCentroAtencion()+"");
						citasForm.setBusquedaPaciente(true);
						request.getSession().setAttribute("ultimaBusquedaCitas","../consultarCita/listarCitas.do?estado="+this.estado);	
						return mapping.findForward("busqueda");
						//return this.accionGeneralCitas(mapping,request,con,citasForm);
					}
					else
					{
						request.setAttribute("codigoDescripcionError",	"errors.paciente.noCargado");
						return mapping.findForward("paginaError");
					}
				}
				else if(estado.equals("general"))
				{
					UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
					citasForm.setBusquedaGeneral(true);
					citasForm.setCentroAtencion(usuario.getCodigoCentroAtencion()+"");
					request.getSession().setAttribute("ultimaBusquedaCitas","../consultarCita/listarCitas.do?estado="+this.estado);	
					return mapping.findForward("busqueda");
				}
				else if (estado.equals("empezar")) 
				{
					citasForm.reset();
					citasForm.setIndicador("0");
					citasForm.setRecordCitas(UtilidadTexto.getBoolean(request.getParameter("recordCitas")));
					return mapping.findForward("empezar");
				}
				else if (estado.equals("empezarOdonto")) 
				{
					UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
					citasForm.reset();
					citasForm.setTipoAgendaSeleccionada(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica);
					citasForm.setIndicador("0");
					citasForm.setRecordCitas(UtilidadTexto.getBoolean(request.getParameter("recordCitas")));
					citasForm.setEstado("busquedaGeneral");
					citasForm.setBusquedaGeneral(true);
					citasForm.setCentroAtencion(usuario.getCodigoCentroAtencion()+"");
	//				 request.getSession().setAttribute("ultimaBusquedaCitas","../consultarCitasOdonto/consultarCitasOdonto.do?estado="+this.estado);	
	
					//Por Tarea 139850 - Se carga el medico que haya en sesion
					citasForm.setCodigoMedico(medico.getCodigoPersona());
					
					return mapping.findForward("empezar");
				}
				else if(estado.equals("busquedaGeneral") || estado.equals("busquedaPaciente"))
				{
					if(citasForm.getTipoAgendaSeleccionada().equals(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica))
						return this.accionOdontologiaCitas(mapping, request,con, citasForm,true,this.medico.getCodigoInstitucionInt());
					return this.accionGeneralCitas(mapping,request,con,citasForm);
				}
				else if (estado.equals("resumenat")) 
				{
					return this.accionGeneralResumen(request,response,con,citasForm);
				}
				else if (estado.equals("resumen")) 
				{
					return this.accionGeneralResumen(request,response,con,citasForm);
				}
				else if(estado.equals("ordenar"))
				{
					if(!UtilidadTexto.isEmpty(citasForm.getTipoAgendaSeleccionada()))
						if(citasForm.getTipoAgendaSeleccionada().equals(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica))
							return this.accionOrdenarOdonto(mapping,con,citasForm);
					return this.accionOrdenar(mapping,con,citasForm);
				}
				else if(estado.equals("imprimirPaciente"))
				{
					return this.accionImprimir(mapping, request,con, citasForm,medico,true);
				}
				else if(estado.equals("imprimirFecha"))
				{
					return this.accionImprimir(mapping, request,con, citasForm,medico,false);
				}
				else if(estado.equals("imprimirCvsPaciente"))
				{
					return this.accionImprimirCvs(mapping, request,con, citasForm,medico,true);
				}
				else if(estado.equals("imprimirCvsFecha"))
				{
					return this.accionImprimirCvs(mapping, request,con, citasForm,medico,false);
				}
				//Estado especial que se llama desde la reserva de citas
				else if(estado.equals("recordCitas"))
				{
					return this.accionRecordCitas(con, citasForm, mapping, request);
				}
				//*******************ESTADOS FUNCIONALIDAD ATENCION CITAS******************************
				else if( estado.equalsIgnoreCase("listarCitasPorMedico") )
				{
					return accionListarCitasPorMedico(con,mapping,request,citasForm);	
				}
				else if (estado.equals("consultarCondicionesServicio"))
				{
					return accionConsultarCondicionesServicio(con,mapping,citasForm);
				}
				else if( estado.equalsIgnoreCase("responderCita") )			
				{
					return accionReponderCita(con,citasForm,mapping,request,response, medico);
				}
				else if (estado.equals("cambiarEstadoCita"))
				{
					return accionCambiarEstadoCita(con,citasForm,mapping,request);
				}
				//------------ FLUJO DE OTROS SERVICIOS ---------------------------------------------
				else if (estado.equals("consultarOtrosServicios"))
				{
					return accionConsultarOtrosServicios(con,citasForm,mapping);
				}
				else if (estado.equals("seleccionOtroServicio"))
				{
					return accionSeleccionOtroServicio(con,citasForm,mapping,request);
				}
				//--------------FLUJO DE DETALLE DE LA CITA (SERVICIOS)--------------------------------
				else if(estado.equals("serviciosCita"))
				{
					if(citasForm.getTipoAgendaSeleccionada().equals(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica))
						return accionConsultarDetalleCitaOdonto(mapping, con,citasForm, medico);
					accionConsultarDetalleCita(con,citasForm,medico,request);
					return mapping.findForward("servicios");
				}
				else if(estado.equals("irListadoCitas"))
				{
					if(citasForm.getTipoAgendaSeleccionada().equals(ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica))
						return mapping.findForward("listadoOdonto");
					return mapping.findForward("listado");
				}
				else if(estado.equals("respuestaServicio"))
				{
					return respuestaServicio(con,citasForm,mapping,request,response);			
				}
				else if(estado.equals("mostrarDetalleCitasIncumplidas"))
				{
					return mapping.findForward("citasIncumplidas");		
				}
				else if (estado.equals("redireccion"))
				{
					response.sendRedirect(citasForm.getLinkSiguiente());
					return null;
				}
				
				//********************************************************************************************************
				else 
				{
					logger.warn("El estado especificado no es vï¿½lido "+estado);
					request.setAttribute( "codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
				
			} 
			else 
			{
				//Todavï¿½a no existe conexiï¿½n, por eso no se cierra
				logger.error("El form no es compatible con el form de Valoraciï¿½n");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}

		}catch (Exception e) {
			Log4JManager.error(e);
			request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
			return mapping.findForward("paginaError");
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}

	private ActionForward accionOrdenarOdonto(ActionMapping mapping,
			Connection con, CitasForm citasForm) {
		boolean ordenamiento= false;
		
		if(citasForm.getEsDescendente().equals(citasForm.getPatronOrdenar()+"descendente")){
			
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(citasForm.getPatronOrdenar(),ordenamiento);
		
		Collections.sort(citasForm.getListCitas(),sortG);
		return mapping.findForward("listadoOdonto");
	}

	/**
	 * Mï¿½todo utilizado para realizar la consulta del detalle de las citas odontologigas
	 * @param con
	 * @param citasForm
	 */
	private ActionForward accionConsultarDetalleCitaOdonto(ActionMapping mapping, Connection connection,
			CitasForm citasForm, UsuarioBasico usuario) {
		// se trae el numero de ingreso
		int aux = Utilidades.convertirAEntero(citasForm.getListCitas().get(Utilidades.convertirAEntero(citasForm.getIndexCita())).getCuenta());
		logger.info("cuenta:::::"+citasForm.getListCitas().get(Utilidades.convertirAEntero(citasForm.getIndexCita())).getCuenta());
		DtoCuentas dtoCuentas = UtilidadesManejoPaciente.consultarDatosCuenta(connection, new BigDecimal(aux));
		citasForm.getListCitas().get(Utilidades.convertirAEntero(citasForm.getIndexCita())).setIngreso(dtoCuentas.getIdIngreso());
		
		citasForm.setListDetalleCita(CitaOdontologica.consultarDetalleCitaConsExterna(connection, usuario.getCodigoInstitucionInt(), 
				citasForm.getListCitas().get(Utilidades.convertirAEntero(citasForm.getIndexCita())).getCodigoPk()));
		if(citasForm.getListDetalleCita().size()>0){
			for (DtoProgramasServiciosPlanT dto: citasForm.getListDetalleCita()){
				dto.setEstadoServicio(ValoresPorDefecto.getIntegridadDominio(dto.getEstadoServicio())+"");
			}
			citasForm.setEntidadManejaProgramas(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()));
			//se rrecorren los planes de tratamiento que tiene lacita y el ingreso
			int codigoPlanTratamiento = PlanTratamiento.obtenerUltimoCodigoPlanTratamientoXIngresoXCita(
					Utilidades.convertirAEntero(dtoCuentas.getIdIngreso()), new ArrayList<String>(), 
					citasForm.getListCitas().get(Utilidades.convertirAEntero(citasForm.getIndexCita())).getCodigoPk());
			if (codigoPlanTratamiento>0){
				DtoPlanTratamientoOdo parametros = new DtoPlanTratamientoOdo();
				parametros.setCodigoPk(new BigDecimal(codigoPlanTratamiento));
				parametros.setInstitucion(usuario.getCodigoInstitucionInt());
				citasForm.setDetCitasOdoPlanTrata(PlanTratamiento.consultarPlanTratamientoHistConf(parametros));
				//SECCION PLAN TRATAMIENTO INICIAL 
			}
			citasForm.setProgramaCita(citasForm.getListDetalleCita().get(0).getPrograma().getNombre());
		}
		UtilidadBD.closeConnection(connection);
		return mapping.findForward("serviciosOdonto");
	}

	/**
	 * Mï¿½todo utilizado para realizar la busqueda de citas odontologicas
	 * @param mapping
	 * @param request
	 * @param con
	 * @param citasForm
	 * @param medico2
	 * @param b
	 * @param i 
	 * @return
	 */
	private ActionForward accionOdontologiaCitas(ActionMapping mapping,
			HttpServletRequest request, Connection con, CitasForm citasForm,
			boolean b, int institucion) {
		logger.info("horasss*"+citasForm.getHoraInicio()+"*"+citasForm.getHoraFin()+"*");
		if(citasForm.getHoraInicio().equals(""))
			citasForm.setHoraInicio("00:00");
		if(citasForm.getHoraFin().equals(""))
			citasForm.setHoraFin("24:00");
		logger.info("horasss*"+citasForm.getHoraInicio()+"*"+citasForm.getHoraFin()+"*");
		if(this.estado.equals("busquedaPaciente"))
		{
			citasForm.setListCitas(CitaOdontologica.consultaCitaOdontologicaConsExt(con, this.personaCargada.getCodigoPersona(), 
					UtilidadFecha.conversionFormatoFechaABD(citasForm.getFechaInicio())+" "+citasForm.getHoraInicio(), UtilidadFecha.conversionFormatoFechaABD(citasForm.getFechaFin())+" "+citasForm.getHoraFin()  , 
					citasForm.getCentroAtencion(), citasForm.getCodigoUnidadConsulta(), citasForm.getEstadosCita(), 
					citasForm.getCodigoConsultorio(), citasForm.getCodigoTipoCita(), 
					citasForm.getCodigoMedico(),institucion));
			this.estado =  "serviciosCita";
		}
		else if(this.estado.equals("busquedaGeneral")){
			logger.info("entreee...");
			citasForm.setListCitas(CitaOdontologica.consultaCitaOdontologicaConsExt(con, ConstantesBD.codigoNuncaValido, UtilidadFecha.conversionFormatoFechaABD(citasForm.getFechaInicio())+ConstantesBD.separadorSplit+citasForm.getHoraInicio(), UtilidadFecha.conversionFormatoFechaABD(citasForm.getFechaFin())+ConstantesBD.separadorSplit+citasForm.getHoraFin(), 
					citasForm.getCentroAtencion(), citasForm.getCodigoUnidadConsulta(), citasForm.getEstadosCita(), citasForm.getCodigoConsultorio(), citasForm.getCodigoTipoCita(), 
					citasForm.getCodigoMedico(),institucion));
			this.estado =  "serviciosCita";
		}
		return mapping.findForward("listadoOdonto");
	}


	/**
	 * Mï¿½todo implementado para cargar el record de citas de un paciente
	 * @param con
	 * @param citasForm
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionRecordCitas(Connection con,
			CitasForm citasForm, ActionMapping mapping, HttpServletRequest request) 
	{
		
		Collection listado;
		Citas c = new Citas();
		//Se consulta el codigo del paciente
		citasForm.setCodigoPaciente(UtilidadValidacion.buscarCodigoPaciente(con, citasForm.getTipoIdentificacion(), citasForm.getNumeroIdentificacion()));
		
		//Si el paciente existe se intentan cargar sus citas
		if(citasForm.getCodigoPaciente()>0)
		{
			String fechaInicio = c.obtenerFechaPrimeraCitaPaciente(con, citasForm.getCodigoPaciente());
			//String fechaFin = UtilidadFecha.getFechaActual(con);
			String fechaFin = "";
			
			try 
			{
				c.cargarCitas(
					con, 
					citasForm.getCodigoPaciente(), 
					ConstantesBD.codigoNuncaValido, //Profesional de la salud Todos
					fechaInicio, // Fecha inicio
					fechaFin, //Fecha Fin
					"", //Hora inicio
					"", //Hora fin
					ConstantesBD.codigoNuncaValido, //Unidad de Agenda (Todos) 
					ConstantesBD.codigoNuncaValido+"", //Estado Liquidacion (Todos)
					ConstantesBD.codigoNuncaValido, // Consultorio (Todos)
					new String[0], // Estados Cita (Todos)
					ConstantesBD.codigoNuncaValido+"", // Centro Atencion (Todos)
					"",
					"fechaDescendente"); //Definiciï¿½n del tipo de ordenamiento
			} 
			catch (SQLException e) 
			{
				logger.error("Error al cargar las citas del pacietne: "+e);
			} //Control post-operatorio 
			this.estado =  "serviciosCita";
		}
		
		
		//crear listado
		listado = c.getCitas();
		//crear enlaces del listado
		try 
		{
			listado=validacionListado(listado);
		} 
		catch (SQLException e) 
		{
			logger.error("Error en la validacion edl listado: "+e);
		} catch (IllegalAccessException e) 
		{
			logger.error("Error en la validacion edl listado: "+e);
		} catch (InvocationTargetException e) 
		{
			logger.error("Error en la validacion edl listado: "+e);
		}
		//aï¿½adirlo al form
		citasForm.setListaCitas(new ArrayList(listado));
		
		//aï¿½adir encabezado
		//e = setInfoEncabezado(e);
		//listarCitasForm.setEncabezado(e);
		//subir ultimo url busqueda a sesion
		request.getSession().setAttribute("ultimoListadoSolicitudes","../consultarCita/listarCitas.do?estado="+this.estado);

		citasForm.setCheckCitasControlPO(ConstantesBD.acronimoNo);
		return mapping.findForward("listado");
	}


	/**
	 * Mï¿½todo que asigna el servicio al cual se le desea abrir la hoja de respuesta
	 * @param con
	 * @param citasForm
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionSeleccionOtroServicio(Connection con, CitasForm citasForm, ActionMapping mapping, HttpServletRequest request) throws IPSException 
	{
		UsuarioBasico usuario =(UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
		
		int i = citasForm.getPosCita();
		int j = citasForm.getPosServicio();
		
		//************VALIDACION DEL CENTRO DE COSTO************************+
		ActionErrors errores = new ActionErrors();
		if(citasForm.getOtrosServicios("codigoCentroCosto_"+j).toString().equals(""))
			errores.add("", new ActionMessage("errors.required","El centro de costo del servicio seleccionado"));
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			//Se reestablece el estado
			citasForm.setEstado("consultarOtrosServicios");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("otrosServicios");
		}
		//********************************************************************
		
		//Se carga el detalle de la cita
		HashMap citaTemp = null;
		try 
		{
			citaTemp = Cita.detalleCita(con, Integer.parseInt(citasForm.getCodigoCita()));
		} 
		catch (NumberFormatException e) 
		{
			logger.error("Erro al consultar el detalle de la cita: "+e);
		} 
		catch (Exception e) 
		{
			logger.error("Error al consultar el detalle de la cita: "+e);
		}
		
		String codigoEstadoLiquidacion = citaTemp.get("codigoestadoliquidacion").toString();
		String codigoEstadoCita = citaTemp.get("codigoestadocita").toString();
		
		citasForm.setMapaCitas("codigoCita_"+i, citasForm.getCodigoCita());
		citasForm.setMapaCitas("codigoPaciente_"+i, citaTemp.get("codigopaciente"));
		citasForm.setMapaCitas("codigoAgenda_"+i, citaTemp.get("codigoagenda"));
		citasForm.setMapaCitas("codigoEstadoCita_"+i, citaTemp.get("codigoestadocita"));
		citasForm.setMapaCitas("codigoEstadoLiquidacion_"+i, codigoEstadoLiquidacion);
		citasForm.setMapaCitas("nombreUnidadAgenda_"+i, citasForm.getNombreUnidadConsulta());
		citasForm.setMapaCitas("horaInicio_"+i, citaTemp.get("horainicio"));
		citasForm.setMapaCitas("numeroSolicitud_"+i+"_"+j, "0");
		citasForm.setMapaCitas("codigoServicio_"+i+"_"+j, citasForm.getOtrosServicios("codigoServicio_"+j));
		citasForm.setMapaCitas("nombreServicio_"+i+"_"+j, citasForm.getOtrosServicios("nombreServicio_"+j));
		citasForm.setMapaCitas("codigoCentroCosto_"+i+"_"+j, citasForm.getOtrosServicios("codigoCentroCosto_"+j));
		citasForm.setMapaCitas("codigoTipoServicio_"+i+"_"+j, citasForm.getOtrosServicios("codigoTipoServicio_"+j));
		citasForm.setMapaCitas("estadoServicio_"+i+"_"+j, ConstantesIntegridadDominio.acronimoEstadoActivo);
		citasForm.setMapaCitas("codigoEspecialidad_"+i+"_"+j, citasForm.getOtrosServicios("codigoEspecialidad_"+j));
		
		UtilidadBD.iniciarTransaccion(con);
		//***************SE INSERTA UN NUEVO SERVICIO A LA CITA*************************
		Cita cita = new Cita();
		int resp0 = cita.insertarServicioCita(
			con, 
			citasForm.getCodigoCita(), 
			citasForm.getOtrosServicios("codigoServicio_"+j).toString(), 
			citasForm.getOtrosServicios("codigoCentroCosto_"+j).toString(), 
			citasForm.getOtrosServicios("codigoEspecialidad_"+j).toString(), 
			"", 
			medico.getLoginUsuario(),
			citaTemp.get("fecha").toString(),
			citaTemp.get("horainicio").toString(),
			citaTemp.get("horafin").toString(),
			citaTemp.get("codigoestadocita").toString(),
			citaTemp.get("codigoagenda").toString()
		);
		
		if(resp0<=0)
			errores.add("",new ActionMessage("errors.notEspecific","Error al tratar de insertar el servicio en la cita"));
		//****************************************************************
		logger.info("EXITO AL INSERTAR EL SERVICO EN LA CITA? "+resp0);
		int resp1 = 1;
		//Si la cita ya estï¿½ liquidada entonces se prosigue a generarse su solicitud
		if(resp0>0)
		{
			if(codigoEstadoLiquidacion.equals(ConstantesBD.codigoEstadoLiquidacionLiquidada))
			{
				cargarDatosCitaForm(citasForm);
				String codigoTipoServicio = citasForm.getOtrosServicios("codigoTipoServicio_"+j).toString();
				//**********SEGUN TIPO DE SERVICIO SE GENERA SOLICITUD CARGO**********************************************
				//Servicios Consultas
				if(codigoTipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+""))
					errores = generarSolicitudConsulta(con,citasForm,errores,medico,personaCargada,personaCargada.getCodigoCuenta()+"");
				//Servicios Procedimientos
				else if(codigoTipoServicio.equals(ConstantesBD.codigoServicioProcedimiento+""))
					errores = generarSolicitudProcedimiento(con,citasForm,errores,medico,personaCargada,personaCargada.getCodigoCuenta()+"");
				//SErvicios No Cruents
				else if(codigoTipoServicio.equals(ConstantesBD.codigoServicioNoCruentos+""))
					errores = generarSolicitudCirugia(con,citasForm,errores,medico,personaCargada,personaCargada.getCodigoCuenta()+"");
				
				if(!errores.isEmpty())
					resp1 = 0;
				//*******************************************************************************************************
			}
			else
				resp1 = 1;
		}
		
		//************************SE ACTUALIZA EL ESTADO DE LA CITA SI ESTABA CUMPLIDA*************************************
		int resp2 = 1;
		if(resp1>0&&resp0>0&&codigoEstadoCita.equals(ConstantesBD.codigoEstadoCitaAtendida+""))
		{
			resp2 = cita.actualizarEstadoCitaTransaccional(con, ConstantesBD.codigoEstadoCitaAsignada, Integer.parseInt(citasForm.getCodigoCita()), ConstantesBD.continuarTransaccion, usuario.getLoginUsuario()).isTrue()?1:0;
			
			if(resp2<=0)
				errores.add("",new ActionMessage("errors.notEspecific","Problemas reestableciendo el estado de la cita a Asignada"));
		}
		//*********************************************************************************************
		
		if(resp0>0&&resp1>0&&resp2>0)
			UtilidadBD.finalizarTransaccion(con);
		else
		{
			saveErrors(request, errores);
			//Se reestablece el estado
			citasForm.setEstado("consultarOtrosServicios");
			UtilidadBD.abortarTransaccion(con);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("otrosServicios");
	}


	/**
	 * Mï¿½todo que realiza la consulta de los otros servicios de la cita
	 * @param con
	 * @param citasForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionConsultarOtrosServicios(Connection con, CitasForm citasForm, ActionMapping mapping) 
	{
		
		//Se consultan los otros servicios de la cita
		citasForm.setOtrosServicios(UtilidadesConsultaExterna.consultarOtrosServiciosCita(con, citasForm.getCodigoCita(),true));
		
		//Se toma la unidad de agenda
		citasForm.setCodigoUnidadConsulta(Integer.parseInt(citasForm.getOtrosServicios("codigoUnidadAgenda").toString()));
		citasForm.setNombreUnidadConsulta(citasForm.getOtrosServicios("nombreUnidadAgenda").toString());
		
		
		//Se consultan los centros de costo de la unidad de consulta
		citasForm.setCentrosCosto(UtilidadesConsultaExterna.consultarCentrosCostoXUnidadAgenda(con, citasForm.getCodigoUnidadConsulta()+"",Utilidades.convertirAEntero(citasForm.getCentroAtencion())));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("otrosServicios");
		
		
	}


	/**
	 * Mï¿½todo implementado para cambiar el estado de una cita
	 * @param con
	 * @param citasForm
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionCambiarEstadoCita(Connection con, CitasForm citasForm, ActionMapping mapping, HttpServletRequest request) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		int i = citasForm.getPosCita();
		citasForm.setCodigoCita(citasForm.getMapaCitas("codigoCita_"+i).toString());
		citasForm.setNombreUnidadConsulta(citasForm.getMapaCitas("nombreUnidadAgenda_"+i).toString());
		citasForm.setHoraInicio(citasForm.getMapaCitas("horaInicio_"+i).toString());
		
		int codigoEstadoCita=Utilidades.convertirAEntero(citasForm.getMapaCitas("codigoEstadoCita_"+i)+"");
		
		// El paciente no asistiï¿½ a la cita
		Cita cita = new Cita();
		ResultadoBoolean resultado = new ResultadoBoolean(false);
		String motivoNoAtencion=citasForm.getMapaCitas().containsKey("motivoNoAtencion_"+i)?citasForm.getMapaCitas("motivoNoAtencion_"+i)+"":"";
		resultado = cita.actualizarEstadoCitaTransaccional(con, codigoEstadoCita,motivoNoAtencion, Integer.parseInt(citasForm.getCodigoCita()), ConstantesBD.continuarTransaccion, medico.getLoginUsuario());
		
		if(resultado.isTrue())
		{
			//Se anulan los servicios de la cita que se encuentren en estado solicitado
			for(int j=0;j<Integer.parseInt(citasForm.getMapaCitas("numServicios_"+i).toString());j++)
			{
				boolean puedoAnular = true;
				
				//Si la solicitud tiene un estado diferente a Solicitada NO se puede anular
				if(Utilidades.convertirAEntero(citasForm.getMapaCitas("codigoEstadoHC_"+i+"_"+j).toString())!=ConstantesBD.codigoEstadoHCSolicitada&&
					Utilidades.convertirAEntero(citasForm.getMapaCitas("codigoEstadoHC_"+i+"_"+j).toString())!=ConstantesBD.codigoEstadoHCAnulada&&
					Utilidades.convertirAEntero(citasForm.getMapaCitas("codigoEstadoHC_"+i+"_"+j).toString())!=ConstantesBD.codigoNuncaValido)
					puedoAnular = false;
				
				if(puedoAnular&&Cita.anularServicioCita(
					con, 
					citasForm.getCodigoCita(), 
					Utilidades.convertirAEntero(citasForm.getMapaCitas("codigoServicio_"+i+"_"+j).toString()), 
					Utilidades.convertirAEntero(citasForm.getMapaCitas("numeroSolicitud_"+i+"_"+j).toString()), 
					this.medico.getLoginUsuario(), 
					this.medico.getCodigoInstitucionInt(),false)<=0)
					resultado.setResultado(false);
				
				
			}
		}
		

		if( !resultado.isTrue() )
		{
			logger.warn("PROBLEMAS ACTUALIZANDO EL ESTADO DE LA CITA "+resultado.getDescripcion());
			ActionErrors errores = new ActionErrors();
			errores.add("", new ActionMessage("errors.noSeGraboInformacion","DE LA ACTUALIZACION DEL ESTADO DE LA CITA DE "+citasForm.getNombreUnidadConsulta()+" A LAS "+citasForm.getHoraInicio()));
			saveErrors(request, errores);	
			UtilidadBD.abortarTransaccion(con);
		}
		else
		{
			//Se cambia el estado de la cita
			citasForm.setMapaCitas("nombreEstadoCita_"+i, Cita.obtenerDescripcionEstadoCita(con,codigoEstadoCita));
			//Ya no se podrï¿½ volver a cambiar el estado de la cita
			citasForm.setMapaCitas("noAsistio_"+i, ConstantesBD.acronimoNo);
			//Y tampoco se puede volver a responser ningun servicio
			for(int j=0;j<Integer.parseInt(citasForm.getMapaCitas("numServicios_"+i).toString());j++)
			{
				citasForm.setMapaCitas("puedoResponder_"+i+"_"+j, ConstantesBD.acronimoNo);
				
				//Se entiende de ante mano que los servicios que no tenï¿½a orden o estaban solicitados quedaron anulados
				if(Utilidades.convertirAEntero(citasForm.getMapaCitas("codigoEstadoHC_"+i+"_"+j).toString())==ConstantesBD.codigoEstadoHCSolicitada||
						Utilidades.convertirAEntero(citasForm.getMapaCitas("codigoEstadoHC_"+i+"_"+j).toString())==ConstantesBD.codigoNuncaValido)
					citasForm.setMapaCitas("estadoServicio_"+i+"_"+j, ConstantesIntegridadDominio.acronimoEstadoAnulado);
			}
			
			if(transaccion)
			{
				citasForm.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
				UtilidadBD.finalizarTransaccion(con);
				
				if(codigoEstadoCita==ConstantesBD.codigoEstadoCitaNoAtencion)
					citasForm.setMapaCitas("graboNoAtencion_"+i, ConstantesBD.acronimoSi);
				else if(codigoEstadoCita==ConstantesBD.codigoEstadoCitaNoCumplida)
					citasForm.setMapaCitas("graboNoAsistio_"+i, ConstantesBD.acronimoSi);
			}
			else
			{
				citasForm.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
				UtilidadBD.abortarTransaccion(con);
			}
		}
		
		
		citasForm.resetEstado();
		citasForm.setEstado("listarCitasPorMedico");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaListadoPorMedico");
	}


	/**
	 * Mï¿½todo que realiza el llamado a la respuesta de la cita
	 * @param con
	 * @param citasForm
	 * @param mapping
	 * @param request
	 * @param response
	 * @return
	 */
	private ActionForward accionReponderCita(Connection con, CitasForm citasForm, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, UsuarioBasico medico) throws IPSException 
	{
		
		HashMap validaciones=new  HashMap(); 
		//**********SE PASA LA INFORMACION DEL MAPA A LAS VARIABLES****************************
		cargarDatosCitaForm(citasForm);
		//***************************************************************************************
		//MT-3144 Se elimina esta validación 
		//Se agregan estas validaciones para prevenir el cruce de agendas entre los médicos
		/*if(!esEspecialidadMedico(citasForm.getCodigoEspecialidad(), medico.getEspecialidades())){
			return mapping.findForward("paginaListadoPorMedico");
		}
		else{*/
			if(citasForm.isTieneMedico()){
				if(citasForm.getCodigoMedico()!=medico.getCodigoPersona()){
					return mapping.findForward("paginaListadoPorMedico");
				}
			}
		//}
		
		//SI EL ESTADO PERMENECE ACTIVO SE PROSIGUE AL FLUJO DE RESPUESTA DE SOLICITUD
		if(citasForm.getEstadoServicio().equals(ConstantesIntegridadDominio.acronimoEstadoActivo))
		{
		
			if( (citasForm.getCodigoPaciente() != -1 && citasForm.getNumeroSolicitud()>0) ||
				((citasForm.getCodigoEstadoCita()==ConstantesBD.codigoEstadoCitaReservada||
				 citasForm.getCodigoEstadoCita()==ConstantesBD.codigoEstadoCitaReprogramada||
				 citasForm.getCodigoEstadoCita()==ConstantesBD.codigoEstadoCitaAsignada) && 
				citasForm.getEstadoLiquidacionCita().equals(ConstantesBD.codigoEstadoLiquidacionSinLiquidar) ))
			{
				
				
				
				//Se carga el paciente en sesiï¿½n *************************************************************
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				if(citasForm.getCuenta()>0)
				{
					paciente.setCodigoPersona(citasForm.getCodigoPaciente());
					UtilidadesManejoPaciente.cargarPacienteXIngreso(con, medico, paciente, request, Utilidades.obtenerCodigoIngresoDadaCuenta(con, citasForm.getCuenta()+""));
					
				}
				else
				{
					paciente.setCodigoPersona(citasForm.getCodigoPaciente());
					UtilidadesManejoPaciente.cargarPaciente(con, medico, paciente, request);
				}
				
				//*********************** SE VALIDAN LOS PARAMETROS GENERALES ************************
				    validaciones=UtilidadesConsultaExterna.validacionesBloqueoAtencionCitas(con, medico, citasForm.getCodigoPaciente());
				    ActionErrors errores1 = new ActionErrors(); 
				    errores1 =(ActionErrors)validaciones.get("errores");
				     
				    if(!errores1.isEmpty())
				       {
				    	 citasForm.setCitasIncumplidas((ArrayList<HashMap<String, Object>>)validaciones.get("citasincumplidas"));
					     citasForm.setEstado("listaCitasIncumplidas");
					     saveErrors(request, errores1);
				    	 return mapping.findForward("paginaListadoPorMedico");
				        }
				  
				//*************************************************************************************
				
				
				
				/**try
				{
					paciente.cargar(con, );
					paciente.cargarPaciente2(con, citasForm.getCodigoPaciente(), medico.getCodigoInstitucion(),medico.getCodigoCentroAtencion()+"");
				}
				catch(SQLException e)
				{
					logger.error("Error al tratar de realizar el cargue del paciente: "+e);
				}

				// Cï¿½digo necesario para registrar este paciente como Observer
				Observable observable = (Observable) getServlet().getServletContext().getAttribute("observable");
				if (observable != null) 
				{
					paciente.setObservable(observable);
					// Si ya lo habï¿½amos aï¿½adido, la siguiente lï¿½nea no hace nada
					observable.addObserver(paciente);
				}
				//Se sube a sesiï¿½n el paciente activo
				request.getSession().setAttribute("pacienteActivo", paciente);**/
				
				//********************PROCESO ADICIONAL PARA RESERVAS SIN LIQUIDAR******************************
				
				//Se verifica si tiene una justificacion pendiente de diligenciar
				/*if(citasForm.getJustificacionMap()!=null&&citasForm.getJustificacionMap().containsKey("justificar"))
					if(citasForm.getJustificacionMap("justificado").equals("false"))
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaListadoPorMedico");
					}*/
				
				//Se verifa Valores por defecto
				if(UtilidadTexto.getBoolean(ValoresPorDefecto.getCrearCuentaAtencionCitas(medico.getCodigoInstitucionInt()))&&
					citasForm.getNumeroSolicitud()<=0) //se verifica que no se haya generado numero de solicitud
				{
					ActionForward resultado = procesoCitaReservadaSinLiquidar(con,paciente,citasForm,mapping,request);
					//Si no es nulo quiere decir que hubo error
					
					logger.info(" \n \n>>>>Entro por validacion Valores por defecto..................>>>");
					
					if(resultado!=null)
					{
						return resultado;
					}
					
				}else{
					// Verificar si ya se ha ingresado la justificaciï¿½n
					if (!FormatoJustServNopos.existeJustificacion(con, citasForm.getNumeroSolicitud()+"", citasForm.getCodigoServicio()+"")){
						//*************** VALIDACION JUSTIFICACION NO POS
						ActionForward jus = new ActionForward();
						jus = validacionJustificacionNoPos(con, citasForm, medico, paciente, mapping);
						if (jus!=null)
							return jus;
						//*************** Se ingresa la justificaciï¿½n No Pos si es requerida
						ingresarJustificacionNoPos(con, citasForm, mapping, medico);
						
						//se elimina la justificacion pendiente
						UtilidadJustificacionPendienteArtServ.eliminarJusNoposPendiente(con, citasForm.getNumeroSolicitud(), citasForm.getCodigoServicio(), false);
					}	
				}
				
					
				
			
				if(citasForm.getNumeroSolicitud()>0)
				{
					//****************************************************************************
					//******SEGUN EL TIPO SE SERVICIO SE ENVï¿½A LA HOJA DE RESPUESTA ADECUADA********
					//******************************************************************************
					//1) SERVICIOS CONSULTA ************************************************************
					if(citasForm.getCodigoTipoServicio().equals(ConstantesBD.codigoServicioInterconsulta+""))
					{
						UtilidadBD.closeConnection(con);
						try
						{
							String path = "../valoracionConsulta/valoracion.do?estado=empezar" +
								"&numeroSolicitud="+citasForm.getNumeroSolicitud()+
								"&codigoCita="+citasForm.getCodigoCita()+
								"&codigoEspecialidad="+citasForm.getCodigoEspecialidad();
							
							response.sendRedirect(path);					
						}
						catch(IOException e)
						{
							logger.error("Error al direccion la hoja de respuesta de un servicio de consulta: "+e);
							ActionErrors errores = new ActionErrors();
							errores.add("",new ActionMessage("error.errorEnBlanco","Error al tratar de abrir la hoja de respuesta del servicio de consulta. Por favor reportar el error al admisnitrador del sistema"));
							saveErrors(request, errores);
							return mapping.findForward("paginaErroresActionErrors");
						}
					
					}
					//2) SERVICIOS PROCEDIMIENTOS ************************************************************
					else if(citasForm.getCodigoTipoServicio().equals(ConstantesBD.codigoServicioProcedimiento+""))
					{
					
						UtilidadBD.closeConnection(con);
						try
						{
							response.sendRedirect("../respuestaProcedimientosDummy/respuestaProcedimientos.do?estado=elegirPlantilla&numeroSolicitud="+citasForm.getNumeroSolicitud()+"&codigoPaciente="+citasForm.getCodigoPaciente()+"&codigoCita="+citasForm.getCodigoCita()+"&servicio="+citasForm.getCodigoServicio());
						}
						catch(IOException e)
						{
							logger.error("Error al direccion la hoja de respuesta de un servicio de procedimiento: "+e);
							ActionErrors errores = new ActionErrors();
							errores.add("",new ActionMessage("error.errorEnBlanco","Error al tratar de abrir la hoja de respuesta del servicio procedimiento. Por favor reportar el error al admisnitrador del sistema"));
							saveErrors(request, errores);
							return mapping.findForward("paginaErroresActionErrors");
						}
					}
					//3) SERVICIOS NO CRUENTOS ************************************************************
					else if(citasForm.getCodigoTipoServicio().equals(ConstantesBD.codigoServicioNoCruentos+""))
					{
						
						int numeroPeticion = Utilidades.getPeticionSolicitudCx(con, citasForm.getNumeroSolicitud());
						UtilidadBD.closeConnection(con);
						try
						{
							response.sendRedirect("../hojaQuirurgicaDummy/hojaQuirurgica.do?estado=cargarHQxDummy&peticion="+numeroPeticion+"&esDummy="+true+"&funcionalidad=AtencionCita&esModificable="+true+"&codigoCita="+citasForm.getCodigoCita());
						}
						catch(IOException e)
						{
							ActionErrors errores = new ActionErrors();
							errores.add("", new ActionMessage("error.errorEnBlanco","Esta opciï¿½n estï¿½ en construcciï¿½n"));
							saveErrors(request, errores);
							UtilidadBD.closeConnection(con);
							return mapping.findForward("paginaAdvertenciasActionMessages");
						}
						
						
					}
				}
				else
				{
					ActionErrors errores = new ActionErrors();
					logger.info("curea cuenta en atenciï¿½n de citas? "+ValoresPorDefecto.getCrearCuentaAtencionCitas(medico.getCodigoInstitucionInt()));
					if(UtilidadTexto.isEmpty(ValoresPorDefecto.getCrearCuentaAtencionCitas(medico.getCodigoInstitucionInt())))
						errores.add("", new ActionMessage("error.parametrosGenerales.faltaDefinirParametro","ï¿½Crear cuenta en atenciï¿½n de citas?"));
					else
						errores.add("", new ActionMessage("error.errorEnBlanco","No ha sido posible generar una solicitud para el servicio seleccionado, por tal motivo, no es posible realizar la atenciï¿½n. Por favor reportar el error al administrador del sistema"));
					saveErrors(request, errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaAdvertenciasActionMessages");
				}
				
				UtilidadBD.closeConnection(con);
				return null;
				
				
			}
			else
			{
				UtilidadBD.closeConnection(con);
				logger.warn("No llegaron los datos de codigo paciente, numero de solicitud requeridos ");
				request.setAttribute( "codigoDescripcionError", "errors.accesoInvalido");
				return mapping.findForward("paginaError");										
			}
		}
		//SI EL ESTADO DEL SERVICIO ES ANULADO, ENTONCES SE ANULA LA SOLICITUD (SI TIENE)
		else
		{
			UtilidadBD.iniciarTransaccion(con);
			logger.info("NUMERO DE LA SOLICITUD ANTES DE ANULAR=> "+citasForm.getNumeroSolicitud());
			int resp = Cita.anularServicioCita(con, citasForm.getCodigoCita(), citasForm.getCodigoServicio(), citasForm.getNumeroSolicitud(), medico.getLoginUsuario(), medico.getCodigoInstitucionInt(),true);
			
			if(resp>0)
			{
				//Si es exitoso ya no se puede responder el servicio
				citasForm.setMapaCitas("puedoResponder_"+citasForm.getPosCita()+"_"+citasForm.getPosServicio(), ConstantesBD.acronimoNo);
				UtilidadBD.finalizarTransaccion(con);
				
				//Se verifica el codigo del estado de la cita
				String codigoEstadoCita = Cita.obtenerEstadoCita(con, Integer.parseInt(citasForm.getCodigoCita()));
				if(Integer.parseInt(codigoEstadoCita)==ConstantesBD.codigoEstadoCitaAtendida)
				{
					citasForm.setMapaCitas("codigoEstadoCita_"+citasForm.getPosCita(), codigoEstadoCita);
					citasForm.setMapaCitas("graboNoAsistio_"+citasForm.getPosCita(), ConstantesBD.acronimoSi); //esto se hace para inhabilitar campo estado cita
					citasForm.setMapaCitas("nombreEstadoCita_"+citasForm.getPosCita(), Cita.obtenerDescripcionEstadoCita(con, Integer.parseInt(codigoEstadoCita)));
				}
				
				//Estado que refleja el ï¿½xito de la transaccion
				citasForm.setEstado("guardado");
			}
			else
			{
				//Si no es exitoso se reporta el error
				ActionErrors errores = new ActionErrors();
				errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA ANULACION DEL SERVICIO "+citasForm.getNombreServicio()+" EN LA CITA DE "+citasForm.getNombreUnidadConsulta()+" A LAS "+citasForm.getHoraInicio()));
				saveErrors(request, errores);
				UtilidadBD.abortarTransaccion(con);
			}
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaListadoPorMedico");
		}
	}


	/**
	 * Mï¿½todo que carga los datos de la cita seleccionada a los atributos de la forma
	 * @param citasForm
	 */
	private void cargarDatosCitaForm(CitasForm citasForm) 
	{
		int i = citasForm.getPosCita();
		int j = citasForm.getPosServicio();

		if(citasForm!=null && citasForm.getMapaCitas()!=null ){
			if(citasForm.getMapaCitas("codigoCita_"+i)!=null){
				citasForm.setCodigoCita(citasForm.getMapaCitas("codigoCita_"+i).toString());
			}
			
			if(citasForm.getMapaCitas("codigomedico_"+i)!=null){
				citasForm.setCodigoMedico(new Integer(citasForm.getMapaCitas("codigomedico_"+i).toString()));
			}
			
			if(citasForm.getMapaCitas("perteneceAlMedico_"+i)!=null){
				citasForm.setTieneMedico(UtilidadTexto.getBoolean(citasForm.getMapaCitas("perteneceAlMedico_"+i)));
			}
			
			if(citasForm.getMapaCitas("codigoPaciente_"+i)!=null){
				citasForm.setCodigoPaciente(Integer.parseInt(citasForm.getMapaCitas("codigoPaciente_"+i).toString()));
			}
			
			if(citasForm.getMapaCitas("codigoAgenda_"+i)!=null){
				citasForm.setCodigoAgenda(Integer.parseInt(citasForm.getMapaCitas("codigoAgenda_"+i).toString()));
			}
			
			if(citasForm.getMapaCitas("codigoEstadoCita_"+i)!=null){
				citasForm.setCodigoEstadoCita(Integer.parseInt(citasForm.getMapaCitas("codigoEstadoCita_"+i).toString()));
			}
			
			if(citasForm.getMapaCitas("codigoEstadoLiquidacion_"+i)!=null){
				citasForm.setEstadoLiquidacionCita(citasForm.getMapaCitas("codigoEstadoLiquidacion_"+i).toString());
			}
			
			if(citasForm.getMapaCitas("nombreUnidadAgenda_"+i)!=null){
				citasForm.setNombreUnidadConsulta(citasForm.getMapaCitas("nombreUnidadAgenda_"+i).toString());
			}
			
			if(citasForm.getMapaCitas("horaInicio_"+i)!=null){
				citasForm.setHoraInicio(citasForm.getMapaCitas("horaInicio_"+i).toString());
			}
			
			if(citasForm.getMapaCitas("numeroSolicitud_"+i+"_"+j)!=null){
				citasForm.setNumeroSolicitud(Integer.parseInt(citasForm.getMapaCitas("numeroSolicitud_"+i+"_"+j).toString()));
			}
			
			if(citasForm.getMapaCitas("codigoServicio_"+i+"_"+j)!=null){
				citasForm.setCodigoServicio(Integer.parseInt(citasForm.getMapaCitas("codigoServicio_"+i+"_"+j).toString()));
			}
			
			if(citasForm.getMapaCitas("nombreServicio_"+i+"_"+j)!=null){
				citasForm.setNombreServicio(citasForm.getMapaCitas("nombreServicio_"+i+"_"+j).toString());
			}
			
			if(citasForm.getMapaCitas("codigoCentroCosto_"+i+"_"+j)!=null){
				citasForm.setCodigoCentroCosto(Utilidades.convertirAEntero(citasForm.getMapaCitas("codigoCentroCosto_"+i+"_"+j).toString()));
			}
			
			if(citasForm.getMapaCitas("codigoTipoServicio_"+i+"_"+j)!=null){
				citasForm.setCodigoTipoServicio(citasForm.getMapaCitas("codigoTipoServicio_"+i+"_"+j).toString());
			}
			
			if(citasForm.getMapaCitas("estadoServicio_"+i+"_"+j)!=null){
				citasForm.setEstadoServicio(citasForm.getMapaCitas("estadoServicio_"+i+"_"+j).toString());
			}
			
			citasForm.setFormatoRespuesta(citasForm.getMapaCitas("tipoConsulta_"+i+"_"+j)==null?"":citasForm.getMapaCitas("tipoConsulta_"+i+"_"+j).toString());
			
			citasForm.setCodigoEspecialidad((citasForm.getMapaCitas("codigoEspecialidad_"+i+"_"+j)==null||citasForm.getMapaCitas("codigoEspecialidad_"+i+"_"+j).toString().equals(""))?ConstantesBD.codigoEspecialidadMedicaNinguna:Integer.parseInt(citasForm.getMapaCitas("codigoEspecialidad_"+i+"_"+j).toString()));
			
			if(citasForm.getMapaCitas("cuenta_"+i)!=null){
				citasForm.setCuenta(Utilidades.convertirAEntero(citasForm.getMapaCitas("cuenta_"+i).toString()));
			}
		}
	}


	/**
	 * Mï¿½todo que consulta las condiciones del servicio
	 * @param con
	 * @param mapping
	 * @param citasForm
	 * @return
	 */
	private ActionForward accionConsultarCondicionesServicio(Connection con, ActionMapping mapping, CitasForm citasForm) 
	{
		//se toma el servicio al cual se le desea consultar las condiciones para la toma
		int codigoServicio = citasForm.getCodigoServicio();
		
		//Se consultan las condiciones para la toma del servicio
		citasForm.setCondicionesToma(CondicionesXServicios.obtenerCondicionesTomaXServicio(con, codigoServicio, medico.getCodigoInstitucionInt()));
		
		return mapping.findForward("condicionesServicio");
	}


	/**
	 * Mï¿½todo que lista las citas por atender
	 * @param con
	 * @param mapping
	 * @param request
	 * @param citasForm
	 * @return
	 */
	private ActionForward accionListarCitasPorMedico(Connection con, ActionMapping mapping, HttpServletRequest request, CitasForm citasForm) 
	{
		/**
		 * Validar concurrencia
		 * Si ya estï¿½ en proceso de facturaciï¿½n, no debe dejar entrar
		 **/
		if(UtilidadValidacion.estaEnProcesofacturacion(con, personaCargada.getCodigoPersona(), "") )
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
		}
		
		//reseteo el mapa de justificacion No POS
		citasForm.setJustificacionesServicios(new HashMap());
		
		String fechaBusqueda = citasForm.getFechaBusqueda();
		
		if( !UtilidadCadena.noEsVacio(fechaBusqueda) || citasForm.isPrimeraVez() )
			fechaBusqueda = UtilidadFecha.getFechaActual();

		citasForm.setFechaBusqueda(fechaBusqueda);
		citasForm.setPrimeraVez(false);
		
		Citas citas = new Citas();
		ResultadoBoolean listadoCitas = citas.cargarCitasPorMedico(con, medico, fechaBusqueda);
		
		if( listadoCitas.isTrue() )
		{
			citasForm.setFechaConsulta(fechaBusqueda);
			cargarForm(con,citas, citasForm);
			
			request.getSession().setAttribute("ultimoListadoSolicitudes","../atenderCita/listadoCitas.do?estado="+this.estado);
			return mapping.findForward("paginaListadoPorMedico");
		}
		else
		{
			logger.warn("Problemas cargando el listado de las citas "+listadoCitas.getDescripcion());
			request.setAttribute( "codigoDescripcionError", "errors.problemasBd");
			return mapping.findForward("paginaError");					
		}
	}

	/**
	 * Mï¿½todo implementado para realizar el proceso de atender citas resevadas sin liquidar,
	 * incluye creaciï¿½n automï¿½tica de cuenta y solicitudes
	 * @param con
	 * @param paciente
	 * @param citasForm
	 * @param mapping 
	 * @param request
	 * @return
	 */
	private ActionForward procesoCitaReservadaSinLiquidar(Connection con, PersonaBasica paciente, CitasForm citasForm, ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		HashMap infoReserva = new HashMap();
		UsuarioBasico usuario =(UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
		
		try {

			//La cita debe ser reservada y sin liquidar
			if((citasForm.getCodigoEstadoCita()==ConstantesBD.codigoEstadoCitaReservada||
					citasForm.getCodigoEstadoCita()==ConstantesBD.codigoEstadoCitaReprogramada||
					citasForm.getCodigoEstadoCita()==ConstantesBD.codigoEstadoCitaAsignada)&&
					citasForm.getEstadoLiquidacionCita().equals(ConstantesBD.codigoEstadoLiquidacionSinLiquidar))
			{
				//Variables necesarias----------------------------------------------------------------------
				String idCuenta = "";

				//Se consulta la informaciï¿½n de la reserva------------------------------------------------------------------------
				infoReserva = Cita.consultaCamposAdicionalesReserva(con, citasForm.getCodigoCita());


				UtilidadBD.iniciarTransaccion(con);

				//CASO 1) CUENTA ABIERTA**************************************************************************
				logger.info("codigoCuenta paciente: "+paciente.getCodigoCuenta());
				logger.info("codigoIngreso paciente: "+paciente.getCodigoIngreso());
				if(paciente.getCodigoCuenta()>0&&UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
				{
					//Se verifica si la cuenta actual tiene la misma via de ingreso--------------------------------------------
					if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna)
					{
						if(Integer.parseInt(infoReserva.get("numRegistros").toString())>0)
						{

							idCuenta = paciente.getCodigoCuenta()+"";
							Cuenta cuenta = new Cuenta();
							cuenta.cargar(con,idCuenta);

							boolean existeConvenio = false;
							//Se verifica si algun convenio de la cuenta tiene la misma informacion de la reserva
							for(int i=0;i<cuenta.getCuenta().getConvenios().length;i++)
							{
								if(cuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo()==Integer.parseInt(infoReserva.get("codigoConvenio").toString())&&
										cuenta.getCuenta().getConvenios()[i].getContrato()==Integer.parseInt(infoReserva.get("codigoContrato").toString()))
									existeConvenio = true;
							}


							//Se verifica si el convenio principal de la cuenta cumple con la misma informaciï¿½n de la reserva--------------------------------------------
							if(!existeConvenio)
							{
								errores.add("Datos Cuenta Inconsistencia",new ActionMessage("error.cita.datosCuentaNoCorresponden"));
								saveErrors(request, errores);
								UtilidadBD.abortarTransaccion(con);
								return mapping.findForward("paginaAdvertenciasActionMessages");
							}
						}
						else
						{
							if(infoReserva.get("atributoError")==null)
								errores.add("Datos Cuenta Inconsistencia",new ActionMessage(infoReserva.get("llaveError").toString()));
							else
								errores.add("Datos Cuenta Inconsistencia",new ActionMessage(infoReserva.get("llaveError").toString(),infoReserva.get("atributoError").toString()));
							saveErrors(request, errores);

							return mapping.findForward("paginaAdvertenciasActionMessages");
						}

					}
					else
					{
						errores.add("No se creï¿½ cuenta",
								new ActionMessage(
										"errores.paciente.otraCuentaAbiertaVia",
										Utilidades.obtenerNombreViaIngreso(con, paciente.getCodigoUltimaViaIngreso()
										)
								)
						);
						saveErrors(request, errores);
						UtilidadBD.abortarTransaccion(con);
						return mapping.findForward("paginaAdvertenciasActionMessages");
					}

				}
				//CASO 2) CUENTA CERRADA ***************************************************************************
				else
				{
					//************SE VERIFICA SIS E DEBE ACTUALIZAR EL NUMERO DE HISTORIA CLï¿½NICA DEL PACIENTE*************************
					Paciente mundoPaciente = new Paciente();
					try 
					{
						mundoPaciente.cargarPaciente(con, paciente.getCodigoPersona());
						if(mundoPaciente.getNumeroHistoriaClinica()==null||mundoPaciente.getNumeroHistoriaClinica().equals(""))
						{
							//Se carga el paciente con los datos antiguos (para el log)
							Paciente pacienteAnterior = new Paciente();
							pacienteAnterior.cargarPaciente(con, mundoPaciente.getCodigoPersona());

							if(mundoPaciente.modificarPaciente(con, usuario, pacienteAnterior)<=0)
								errores.add("",new ActionMessage("errors.problemasGenericos","actualizando la informaciï¿½n del paciente"));
						}
					} 
					catch (SQLException e1) 
					{
						logger.error("Error cargando/actualizando paciente: "+e1);
						errores.add("", new ActionMessage("errors.problemasGenericos","cargando o actualizando la informaciï¿½n del paciente: "+e1));
					}


					//***************SE TOMA Y SE VALIDA EL CONSECUTIVO DE INGRESO**********************************************************
					String valorConsecutivoIngreso=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoIngresos, usuario.getCodigoInstitucionInt());
					String anioConsecutivoIngreso=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoIngresos, usuario.getCodigoInstitucionInt(),valorConsecutivoIngreso);
					if(!UtilidadCadena.noEsVacio(valorConsecutivoIngreso) || valorConsecutivoIngreso.equals("-1"))
						errores.add("Falta consecutivo disponible",new ActionMessage("error.paciente.faltaDefinirConsecutivo","el ingreso"));
					else
					{
						try
						{
							Integer.parseInt(valorConsecutivoIngreso);
						}
						catch(Exception e)
						{
							logger.error("Error en validacionConsecutivoDisponibleIngreso:  "+e);
							errores.add("Consecutivo no es entero", new ActionMessage("errors.integer","el consecutivo del ingreso"));
						}
					}

					//Si hay lagun error con el consecutivo de ingreso se debe manejar el error
					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
						UtilidadBD.abortarTransaccion(con);
						return mapping.findForward("paginaErroresActionErrors");
					}
					//***********************************************************************************************************************

					IngresoGeneral ingreso = new IngresoGeneral(
							usuario.getCodigoInstitucion(), 
							paciente,ConstantesIntegridadDominio.acronimoEstadoAbierto,
							usuario.getLoginUsuario(),
							valorConsecutivoIngreso,
							anioConsecutivoIngreso,
							usuario.getCodigoCentroAtencion(),
							"", //sin codigo paciente entidad subcontratada
							"","",""
					);
					RespuestaValidacion resp1;
					int idIngreso = 0;

					//Validaciones iniciales del ingreso y de la cuenta---------------------------------------------------------------

					resp1 = UtilidadValidacion.validacionIngresarIngreso(con, ingreso.getCodigoTipoIdentificacionPaciente(), ingreso.getNumeroIdentificacionPaciente(), ingreso.getInstitucion() );


					//----------------------------------------------------------------------------------------------------

					if(resp1.puedoSeguir)
					{

						if(Integer.parseInt(infoReserva.get("numRegistros").toString())>0)
						{

							//Se crea nuevo ingreso-------------------------------------------------------------------------------------------

							idIngreso = ingreso.insertarIngresoTransaccional(con, ConstantesBD.continuarTransaccion);
							UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoIngresos,usuario.getCodigoInstitucionInt(), valorConsecutivoIngreso, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);


							//Se inserta la cuenta -----------------------------------------------------------------------------------------
							DtoCuentas dtoCuenta = new DtoCuentas();
							//Se llenan los datos de la cuenta
							dtoCuenta.setCodigoEstado(ConstantesBD.codigoEstadoCuentaActiva);
							dtoCuenta.setCodigoViaIngreso(ConstantesBD.codigoViaIngresoConsultaExterna);
							dtoCuenta.setDesplazado(paciente.getCodigoGrupoPoblacional().equals(ConstantesIntegridadDominio.acronimoDesplazados)?true:false);
							dtoCuenta.setCodigoOrigenAdmision(ConstantesBD.codigoOrigenAdmisionHospitalariaEsConsultaExterna);
							Log4JManager.info("\n\n\n\n\n\n\n\n\n\n\n centro costo: "+citasForm.getCodigoCentroCosto()+"\n\n\n\n\n\n\n");
							dtoCuenta.setCodigoArea(citasForm.getCodigoCentroCosto());
							dtoCuenta.setCodigoTipoPaciente(ConstantesBD.tipoPacienteAmbulatorio);
							dtoCuenta.setCodigoPaciente(paciente.getCodigoPersona()+"");
							dtoCuenta.setLoginUsuario(usuario.getLoginUsuario());
							dtoCuenta.setIdIngreso(idIngreso+"");


							//Se llenan los datos del convenio principal
							DtoSubCuentas dtoSubCuenta = new DtoSubCuentas();
							dtoSubCuenta.setConvenio(new InfoDatosInt(Integer.parseInt(infoReserva.get("codigoConvenio").toString()),infoReserva.get("nombreConvenio").toString()));
							int natPaciente=Utilidades.convertirAEntero(infoReserva.get("naturalezaPaciente")+"");
							if(natPaciente<0)
								natPaciente=ConstantesBD.codigoNaturalezaPacientesNinguno;
							dtoSubCuenta.setNaturalezaPaciente(natPaciente);
							dtoSubCuenta.setTipoAfiliado(infoReserva.get("codigoTipoAfiliado").toString());
							dtoSubCuenta.setClasificacionSocioEconomica(Integer.parseInt(infoReserva.get("codigoEstratoSocial").toString()));
							dtoSubCuenta.setContrato(Integer.parseInt(infoReserva.get("codigoContrato").toString()));
							dtoSubCuenta.setIngreso(idIngreso);
							dtoSubCuenta.setLoginUsuario(usuario.getLoginUsuario());
							dtoSubCuenta.setCodigoPaciente(paciente.getCodigoPersona());
							dtoSubCuenta.setNroPrioridad(1);
							dtoSubCuenta.setFacturado(ConstantesBD.acronimoNo);
							DtoValidacionTipoCobroPaciente validacion=(DtoValidacionTipoCobroPaciente)infoReserva.get("validacionTipoCobroPaciente");
							Log4JManager.info("MANEJA MONTOS CONVENIO "+infoReserva.get("codigoConvenio")+"-->"+validacion.getManejaMontos());
							dtoSubCuenta.setTipoCobroPaciente(validacion.getTipoCobroPaciente());
							if(UtilidadTexto.getBoolean(validacion.getManejaMontos()))
							{
								IBusquedaMontosCobroServicio busquedaMontoServicio=FacturacionServicioFabrica.crearBusquedaMontosCobroServicio();
								DtoBusquedaMontosCobro montoCobro= busquedaMontoServicio.consultarMontosCobro(
										Integer.parseInt(infoReserva.get("codigoEstratoSocial").toString()), 
										Integer.parseInt(infoReserva.get("codigoConvenio").toString()), 
										UtilidadFecha.getFechaActual(con), 
										natPaciente, 
										infoReserva.get("codigoTipoAfiliado").toString(), 
										ConstantesBD.tipoPacienteAmbulatorio, 
										ConstantesBD.codigoViaIngresoConsultaExterna);
								if(montoCobro.getResultado().isTrue())
								{
									Iterator it=montoCobro.getMontosCobro().iterator();
									if(it.hasNext())
									{
										DTOResultadoBusquedaDetalleMontos dto=(DTOResultadoBusquedaDetalleMontos)it.next();
										dtoSubCuenta.setMontoCobro(dto.getDetalleCodigo());
										dtoSubCuenta.setTipoMontoCobro(dto.getTipoDetalleAcronimo());
									}
								}
								else
								{

									errores.add("error con el monto de cobro",new ActionMessage("error.errorEnBlanco",montoCobro.getResultado().getDescripcion()));
									saveErrors(request, errores);
									UtilidadBD.abortarTransaccion(con);
									return mapping.findForward("paginaErroresActionErrors");
								}
							}
							else
							{
								dtoSubCuenta.setMontoCobro(ConstantesBD.codigoNuncaValido);
								dtoSubCuenta.setPorcentajeMontoCobro(validacion.getPorcentajeMontoCobro());
							}


							Contrato mundoContrato = new Contrato();
							mundoContrato.cargar(con, dtoSubCuenta.getContrato()+"");

							//Se asigna el convenio a la cuenta
							dtoCuenta.setConvenios(new DtoSubCuentas[1]);
							dtoCuenta.getConvenios()[0] = dtoSubCuenta;

							Cuenta mundoCuenta = new Cuenta();
							mundoCuenta.setCuenta(dtoCuenta);
							ResultadoBoolean resp2 = mundoCuenta.guardar(con);


							//idCuenta = Integer.toString(cuenta.insertarCuentaTransaccional(con, ConstantesBD.continuarTransaccion));


							if(!resp2.isTrue())
							{
								errores.add("No se creá cuenta",new ActionMessage("errors.noSeGraboInformacion","DE LA CUENTA DEL PACIENTE"));
								saveErrors(request, errores);
								UtilidadBD.abortarTransaccion(con);
								return mapping.findForward("paginaErroresActionErrors");

							}

							idCuenta = resp2.getDescripcion();

							//Se carga de nuevo el paciente (para actualizar los datos de la cuenta)----------------------------------------------------
							try 
							{
								paciente.cargar(con, citasForm.getCodigoPaciente());
								paciente.cargarPaciente2(con, citasForm.getCodigoPaciente(), usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
							} 
							catch (SQLException e) 
							{
								logger.error("Error cargando los datos del paciente en procesoCitaReservadaSinLiquidar:"+e);
							}
						}
						else
						{
							if(infoReserva.get("atributoError")==null)
								errores.add("Datos Cuenta Inconsistencia",new ActionMessage(infoReserva.get("llaveError").toString()));
							else
								errores.add("Datos Cuenta Inconsistencia",new ActionMessage(infoReserva.get("llaveError").toString(),infoReserva.get("atributoError").toString()));
							UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoIngresos,usuario.getCodigoInstitucionInt(), valorConsecutivoIngreso, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
							saveErrors(request, errores);
							return mapping.findForward("paginaAdvertenciasActionMessages");
						}

					}
					else
					{
						request.setAttribute("descripcionError", resp1.textoRespuesta);
						UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoIngresos,usuario.getCodigoInstitucionInt(), valorConsecutivoIngreso, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
						UtilidadBD.abortarTransaccion(con);
						return mapping.findForward("paginaError");
					}
				}

				//**************** VALIDACION JUSTIFICACION NO POS
				ActionForward jus = new ActionForward();
				jus = validacionJustificacionNoPos(con, citasForm, usuario, paciente, mapping);
				if (jus!=null)
					return jus;


				//**********SEGUN TIPO DE SERVICIO SE GENERA SOLICITUD CARGO**********************************************
				//Servicios Consultas
				if(citasForm.getCodigoTipoServicio().equals(ConstantesBD.codigoServicioInterconsulta+""))
					errores = generarSolicitudConsulta(con,citasForm,errores,usuario,paciente,idCuenta);
				//Servicios Procedimientos
				else if(citasForm.getCodigoTipoServicio().equals(ConstantesBD.codigoServicioProcedimiento+""))
					errores = generarSolicitudProcedimiento(con,citasForm,errores,usuario,paciente,idCuenta);
				//SErvicios No Cruents
				else if(citasForm.getCodigoTipoServicio().equals(ConstantesBD.codigoServicioNoCruentos+""))
					errores = generarSolicitudCirugia(con,citasForm,errores,usuario,paciente,idCuenta);


				//*************** Se ingresa la justificaciï¿½n No Pos si es requerida
				ingresarJustificacionNoPos(con, citasForm, mapping, usuario);

				//-------------------------------------------------------------------------


				if(!errores.isEmpty())
				{
					saveErrors(request, errores);
					UtilidadBD.abortarTransaccion(con);
					return mapping.findForward("paginaErroresActionErrors");
				}

				UtilidadBD.finalizarTransaccion(con);

			}
		} catch(Exception e) {
			errores.add("procesoCitaReservadaSinLiquidar", new ActionMessage("errors.problemasGenericos", "Procesando Cita Reservada Sin Liquidar"));
			saveErrors(request, errores);
			UtilidadBD.abortarTransaccion(con);
			return mapping.findForward("paginaErroresActionErrors");
		}
		return null;
	}

	private void ingresarJustificacionNoPos(Connection con, CitasForm citasForm, ActionMapping mapping, UsuarioBasico usuario) {
		FormatoJustServNopos fjsn = new FormatoJustServNopos();
		if (citasForm.getJustificacionesServicios().containsKey("justificar")){
			if(citasForm.getJustificacionesServicios("justificar").equals("true")){
				logger.info("--------------- ENTRO A JUSTIFICAR -----------------");
				// Ingresamos Justificacion No Pos
				fjsn.ingresarJustificacion(
                		con,
                		usuario.getCodigoInstitucionInt(), 
                		usuario.getLoginUsuario(), 
                		citasForm.getJustificacionesServicios(), 
                		citasForm.getNumeroSolicitud(),
                		ConstantesBD.codigoNuncaValido,
                		0,
                		usuario.getCodigoPersona());
			}
		}
	}


	private ActionForward validacionJustificacionNoPos(Connection con, CitasForm citasForm, UsuarioBasico medico2, PersonaBasica paciente, ActionMapping mapping) throws IPSException {
		// ******************* JUSTIFICACION NO POS ************************************************************
		FormatoJustServNopos fjsn = new FormatoJustServNopos();
		Utilidades.imprimirMapa(citasForm.getJustificacionesServicios());
		
		// Evaluamos si el elemento es No POS
    	if (!UtilidadesFacturacion.esServicioPos(con, citasForm.getCodigoServicio())){
    		
    		// Miramos si no se ha realizado lo necesario para redirigir al formato de justificacion
    		if (!citasForm.getJustificacionesServicios().containsKey("justificar")){
    		
    			// **** Verificamos si el servicio requiere de justificacion No Pos
        			boolean valProfesionalSalud = UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, medico, false);
        			HashMap justificacionMap = new HashMap();
        			String justificar="false";
        			
            		//Evaluamos la cobertura del Servicio
            		InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
            		infoResponsableCobertura = Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), citasForm.getCodigoServicio(), Integer.parseInt(medico.getCodigoInstitucion()), false, "" /*subCuentaCoberturaOPCIONAL*/);
            		citasForm.setJustificacionesServicios("cobertura", infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo()+"");
            		citasForm.setJustificacionesServicios("subcuenta", infoResponsableCobertura.getDtoSubCuenta().getSubCuenta()+"");

            		//Evaluamos si el convenio que cubre el servicio requiere de justificaciï¿½n de servicios
            		if (UtilidadesFacturacion.requiereJustificacioServ(con, infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo())){
            			justificar="true";
            			
            			// Validacion 'Especialidad profesional de la salud'
            			if (!valProfesionalSalud){
            				justificar = "pendiente";
            			}
            		}
            		citasForm.setJustificacionesServicios("justificar", justificar);
                	citasForm.setJustificacionesServicios("justificado", "false");
                	citasForm.setJustificacionesServicios("servicio", citasForm.getCodigoServicio());
            	// ****
                	
            	//Si se debe justificar se retorna a la pï¿½gina
            	if (justificar.equals("true"))
            	{
            		logger.info("1 DEBO REALIZAR JUSTIFICACION !!! "+citasForm.getJustificacionesServicios());
            		UtilidadBD.finalizarTransaccion(con);
            		UtilidadBD.closeConnection(con);
            		return mapping.findForward("paginaListadoPorMedico");
            	}
    		} else {
    			if (!citasForm.getJustificacionesServicios().containsKey("0_servicio")){
    				logger.info("2 DEBO REALIZAR JUSTIFICACION !!!"+citasForm.getJustificacionesServicios());
            		UtilidadBD.closeConnection(con);
            		UtilidadBD.finalizarTransaccion(con);
            		return mapping.findForward("paginaListadoPorMedico");
    			}
    		}
    		
    	}
    	//*******************************************************************************
    	return null;
	}


	/**
	 * Mï¿½todo que realiza la generacion de la solicitud de cirugï¿½a
	 * @param con
	 * @param citasForm
	 * @param errores
	 * @param usuario
	 * @param paciente
	 * @param idCuenta
	 * @return
	 */
	private ActionErrors generarSolicitudCirugia(Connection con, CitasForm citasForm, ActionErrors errores, UsuarioBasico usuario, PersonaBasica paciente, String idCuenta) throws IPSException 
	{
		Cita cita = new Cita();
		cita.setCodigo(Integer.parseInt(citasForm.getCodigoCita()));
		SolicitudesCx mundoSolCx= new SolicitudesCx();
			
		//String numeroAutorizacion = "";
		int codigoServicio = citasForm.getCodigoServicio();
		int codigoCentroCosto = citasForm.getCodigoCentroCosto();
		String observaciones = "";
		int codigoPeticion = 0, numeroSolicitud=0, resp1 = 0;
		boolean resp0 = false;
		
		
		//*************SE INSERTA PRIMERO LA PETICION QX ********************************************
		//Datos del encabezado de la peticion
        HashMap peticionEncabezadoMap= new HashMap();
        peticionEncabezadoMap.put("tipoPaciente",   UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(con, idCuenta).getAcronimo());
        peticionEncabezadoMap.put("fechaPeticion", UtilidadFecha.getFechaActual(con));
        peticionEncabezadoMap.put("horaPeticion", UtilidadFecha.getHoraActual(con));
        peticionEncabezadoMap.put("duracion", "");
        peticionEncabezadoMap.put("solicitante", usuario.getCodigoPersona()+"");
        peticionEncabezadoMap.put("fechaEstimada", "");
        peticionEncabezadoMap.put("requiereUci", ConstantesBD.acronimoNo);
        peticionEncabezadoMap.put("programable", ConstantesBD.acronimoNo);
        
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
        
        //logger.info("resultado insercion peticion=> Nï¿½ inserciones: "+codigoPeticionYNumeroInserciones[0]+", peticion: "+codigoPeticionYNumeroInserciones[1]);
        
        //Se verifica nï¿½mero de inserciones
        if(codigoPeticionYNumeroInserciones[0]<1)
        	errores.add("",new ActionMessage("errors.noSeGraboInformacion",
        		"DE LA PETICION Qx PARA EL SERVICIO "+citasForm.getNombreServicio()));
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
	        objectSolicitud.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoNuncaValido));
	        objectSolicitud.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea()));
	       // objectSolicitud.setCodigoMedicoSolicitante(usuario.getCodigoPersona());
	        objectSolicitud.setCodigoMedicoSolicitante(ConstantesBD.codigoNuncaValido);
	        
	        objectSolicitud.setCentroCostoSolicitado(new InfoDatosInt(codigoCentroCosto));
	        objectSolicitud.setCodigoCuenta(Integer.parseInt(idCuenta));
	        
	        objectSolicitud.setCobrable(true);
	        
	        objectSolicitud.setVaAEpicrisis(false);
	        objectSolicitud.setUrgente(false);
	        objectSolicitud.setEstadoHistoriaClinica(new InfoDatosInt(ConstantesBD.codigoEstadoHCSolicitada));
	        objectSolicitud.setSolPYP(false);
	        objectSolicitud.setTieneCita(true);
	        //Campo se actualiza despues de generado el cargo
	        objectSolicitud.setLiquidarAsocio(ConstantesBD.acronimoSi);
	        
	        try
	        {
	            numeroSolicitud=objectSolicitud.insertarSolicitudGeneralTransaccional(con, ConstantesBD.continuarTransaccion);
	        }
	        catch(SQLException sqle)
	        {
	            logger.warn("Error al generar la solicitud basica de cirugï¿½as: "+sqle);
	            errores.add("",new ActionMessage("errors.noSeGraboInformacion",
	            		"DE LA SOLICITUD PARA EL SERVICIO "+citasForm.getNombreServicio()));
	            
	        }
	        cita.actualizarEstadoCitaTransaccional(con, ConstantesBD.codigoEstadoCitaAsignada, cita.getCodigo(), ConstantesBD.continuarTransaccion, medico.getLoginUsuario());
	        //***********************************************************************************************
			
	        citasForm.setNumeroSolicitud(numeroSolicitud);
	        citasForm.setMapaCitas("numeroSolicitud_"+citasForm.getPosCita()+"_"+citasForm.getPosServicio(), numeroSolicitud);
	        
			//*******************SE CALCULA LA SUBCUENTA DE LA COBERTURA**************************************
	        double subCuenta=ConstantesBD.codigoNuncaValido;
			InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();;
			infoResponsableCobertura=Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), codigoServicio, usuario.getCodigoInstitucionInt(),false, "" /*subCuentaCoberturaOPCIONAL*/);
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
	        		"DE LA SOLICITUD Qx PARA EL SERVICIO "+citasForm.getNombreServicio()));
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
			        		"DEL DETALLE DE LA SOLICITUD Qx PARA EL SERVICIO "+citasForm.getNombreServicio()
			        		));
			}
			
			//*******************************************************************************************************
			
			
			if(resp0 && resp1 > 0)
			{
				
				//**************SE ACTUALIZA EL NUMERO DE SOLICITUD EN LA CITA********************************************
				if(cita.asignarSolicitud(con, numeroSolicitud, codigoServicio, ConstantesBD.continuarTransaccion,usuario.getLoginUsuario())<=0)
				{
					errores.add("",new ActionMessage("errors.noSeGraboInformacion",
							"AL ACTUALIZAR LA SOLICITUD DEL SERVICIO "+citasForm.getNombreServicio()));
				}
				else
				{
					//Cambiar el indicador de liquidacion de asocios a No por haber generado el cargo sin asocios
					if(!objectSolicitud.cambiarLiquidacionAsociosSolicitud(con, numeroSolicitud, ConstantesBD.acronimoNo))
						errores.add("",new ActionMessage("errors.noSeGraboInformacion",
								"AL ACTUALIZAR EL CAMPO LIQUIDAR ASOCIOS DE LA SOLICITUD "+numeroSolicitud));						
				}
				//**************************************************************************************************************************
					
				
			}
			//*******************************************************************************************************
			
			return errores;
	}


	/**
	 * Mï¿½todo implementado para generar la solicitud de procedimiento
	 * @param con
	 * @param citasForm
	 * @param errores
	 * @param usuario
	 * @param paciente
	 * @param idCuenta
	 * @return
	 */
	private ActionErrors generarSolicitudProcedimiento(Connection con, CitasForm citasForm, ActionErrors errores, UsuarioBasico usuario, PersonaBasica paciente, String idCuenta) 
	{
		Cita cita = new Cita();
		cita.setCodigo(Integer.parseInt(citasForm.getCodigoCita()));
		SolicitudProcedimiento	solicitud= new SolicitudProcedimiento();
		
		//String numeroAutorizacion = "";
		int codigoServicio = citasForm.getCodigoServicio();
		int codigoCentroCosto = citasForm.getCodigoCentroCosto();
		
		try
		{
			//********************************GENERACION DE LA SOLICITUD DE PROCEDIMIENTOS*********************************************
			solicitud.setFechaSolicitud(UtilidadFecha.getFechaActual(con));
			solicitud.setHoraSolicitud(UtilidadFecha.getHoraActual(con));
			solicitud.setSolPYP(false);
			solicitud.setTipoSolicitud(new InfoDatosInt (ConstantesBD.codigoTipoSolicitudProcedimiento, ""));
			solicitud.setCobrable(true);
			//solicitud.setNumeroAutorizacion(numeroAutorizacion);
			solicitud.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaNinguna, ""));
			solicitud.setOcupacionSolicitado(new InfoDatosInt(usuario.getCodigoOcupacionMedica(), ""));
			solicitud.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea(), ""));
			solicitud.setCentroCostoSolicitado(new InfoDatosInt(codigoCentroCosto, ""));
			solicitud.setCodigoMedicoSolicitante(ConstantesBD.codigoNuncaValido);
			solicitud.setCodigoCentroAtencionCuentaSol(usuario.getCodigoCentroAtencion());
			solicitud.setCodigoCuenta(Integer.parseInt(idCuenta));
			solicitud.setCodigoPaciente(paciente.getCodigoPersona());
			solicitud.setDatosMedico("");
			solicitud.setVaAEpicrisis(false);
			solicitud.setUrgente(false);
			solicitud.setComentario("");
			solicitud.setNombreOtros("");
			solicitud.setTieneCita(true);
			
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

			/* Genera ls solicitud */
			int numeroSolicitud = solicitud.insertarTransaccional(con, ConstantesBD.continuarTransaccion, numeroDocumento, Integer.parseInt(idCuenta), false,ConstantesBD.codigoNuncaValido+"");
			solicitud.setNumeroSolicitud(numeroSolicitud);
			citasForm.setNumeroSolicitud(numeroSolicitud);
			citasForm.setMapaCitas("numeroSolicitud_"+citasForm.getPosCita()+"_"+citasForm.getPosServicio(), numeroSolicitud);
			
			
			//Actualizacion del pool de la solicitud*******************************************************************
			Solicitud sol=new Solicitud();
			ArrayList array=Utilidades.obtenerPoolesMedico(con,UtilidadFecha.getFechaActual(con),usuario.getCodigoPersona());
			if(array.size()==1)
				sol.actualizarPoolSolicitud(con,cita.getNumeroSolicitud(),Integer.parseInt(array.get(0)+""));
			
			//REGISTRAR SERVICIOS INCLUIDOS************
			if(!solicitud.registrarServiciosIncluidos(con, usuario, paciente))
			{
				errores.add("",new ActionMessage("errors.noSeGraboInformacion",
						"AL REGISTRAR LOS SERVICIOS INCLUIDOS DEL SERVICIO "+citasForm.getNombreServicio()));
			}
			//*******************************************
			
			cita.actualizarEstadoCitaTransaccional(con, ConstantesBD.codigoEstadoCitaAsignada, cita.getCodigo(), ConstantesBD.continuarTransaccion, medico.getLoginUsuario());
						
			 //GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
		    Cargos cargos= new Cargos();
		    cargos.setPyp(false);
		    boolean dejarPendiente=true;
			if(UtilidadValidacion.esServicioViaIngresoCargoSolicitud(con, paciente.getCodigoUltimaViaIngreso()+"", ""+solicitud.getCodigoServicioSolicitado(), usuario.getCodigoInstitucion()))
			{
				dejarPendiente=false;
			}
		    boolean inserto= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
				usuario, 
				paciente, 
				dejarPendiente/*dejarPendiente*/, 
				solicitud.getNumeroSolicitud(), 
				ConstantesBD.codigoTipoSolicitudProcedimiento /*codigoTipoSolicitudOPCIONAL*/, 
				Integer.parseInt(idCuenta)/*codigoCuentaOPCIONAL*/, 
				solicitud.getCentroCostoSolicitado().getCodigo()/*codigoCentroCostoEjecutaOPCIONAL*/, 
				solicitud.getCodigoServicioSolicitado()/*codigoServicioOPCIONAL*/, 
				1 /*cantidadServicioOPCIONAL*/, 
				ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
				ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
				/*numeroAutorizacion,*/
				"" /*esPortatil*/,
				false /*excento*/,solicitud.getFechaSolicitud(),"");
						
						
			if(!inserto)
			{
				errores.add("",new ActionMessage("errors.noSeGraboInformacion",
						"AL GENERAR LA SOLICITUD Y EL CARGO PARA EL SERVICIO "+citasForm.getNombreServicio()));
			}
			//***************************************************************************************************************************
			else
			{
				//**********************ASIGNACION DE LA SOLICITUD A LA CITA***************************************************************
				
				HashMap servicios = Cita.consultarServiciosCita(con, cita.getCodigo()+"", usuario.getCodigoInstitucionInt());
				logger.info("NUMERO DER EGISTROS ENCOTNRADO============> "+servicios.get("numRegistros")+" "+servicios);
				
				if(cita.asignarSolicitud(con, numeroSolicitud, codigoServicio, ConstantesBD.continuarTransaccion,usuario.getLoginUsuario())<=0)
					errores.add("",new ActionMessage("errors.noSeGraboInformacion",
							"AL ACTUALIZAR LA SOLICITUD DEL SERVICIO "+citasForm.getNombreServicio()+" "));
				//**************************************************************************************************************************
				
			}			
			
		}
		catch(Exception e)
		{
			logger.error("Error al generar la solicitud de procedimientos de la cita: "+e);
			errores.add("",new ActionMessage("errors.noSeGraboInformacion",
					"AL GENERAR LA SOLICITUD PARA EL SERVICIO "+citasForm.getNombreServicio()+" "));
		}

		return errores;
	}


	/**
	 * Generar la solicitud de consulta
	 * @param con
	 * @param citasForm
	 * @param errores
	 * @param idCuenta 
	 * @param paciente 
	 * @param usuario 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private ActionErrors generarSolicitudConsulta(Connection con, CitasForm citasForm, ActionErrors errores, UsuarioBasico usuario, PersonaBasica paciente, String idCuenta) 
	{
		//Se inserta solicitud de cita (generar cargo) -----------------------------------------------------------------------------------
		Cita cita = new Cita();
		cita.setCodigo(Integer.parseInt(citasForm.getCodigoCita()));
		
		boolean lb_generada=false;
		
		Cargos cargos=new Cargos();
		try
		{
			
			cita.setCodigoAreaPaciente(paciente.getCodigoArea());
			cita.setCodigoCentroCostoSolicitado(citasForm.getCodigoCentroCosto());
			cita.setCodigoEspecialidadSolicitante(citasForm.getCodigoEspecialidad());
			cita.setSolPYP(false);
			
			//usuario.getCodigoOcupacionMedica()
			lb_generada =cita.generarSolicitud(
				con,
				ConstantesBD.continuarTransaccion,
				usuario.getCodigoOcupacionMedica(),
				Integer.parseInt(idCuenta),
				citasForm.getCodigoServicio(),
				/*"",*/ 
				paciente.getCodigoArea(), 
				citasForm.getCodigoCentroCosto(),
				usuario.getLoginUsuario(),
				true);
			
			cita.actualizarEstadoCitaTransaccional(con, ConstantesBD.codigoEstadoCitaAsignada, cita.getCodigo(), ConstantesBD.continuarTransaccion, medico.getLoginUsuario());
			
			citasForm.setNumeroSolicitud(cita.getNumeroSolicitud());
			citasForm.setMapaCitas("numeroSolicitud_"+citasForm.getPosCita()+"_"+citasForm.getPosServicio(), cita.getNumeroSolicitud());
			
		}//try
		catch(Exception e)
		{
			logger.error("Error generando el cargo para la cita: "+e);
		}
		
		//Actualizacion del pool de la solicitud*******************************************************************
		Solicitud sol=new Solicitud();
		ArrayList array=Utilidades.obtenerPoolesMedico(con,UtilidadFecha.getFechaActual(con),usuario.getCodigoPersona());
		if(array.size()==1)
			sol.actualizarPoolSolicitud(con,cita.getNumeroSolicitud(),Integer.parseInt(array.get(0)+""));
		
		if(lb_generada )
		{
			try
			{
				//cargo.generarCargoCita(con, cita.getNumeroSolicitud(), usuario.getLoginUsuario());
				cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
																					usuario, 
																					paciente, 
																					false/*dejarPendiente*/, 
																					cita.getNumeroSolicitud(), 
																					ConstantesBD.codigoTipoSolicitudCita /*codigoTipoSolicitudOPCIONAL*/, 
																					Integer.parseInt(idCuenta), 
																					ConstantesBD.codigoCentroCostoConsultaExterna/*codigoCentroCostoEjecutaOPCIONAL*/, 
																					citasForm.getCodigoServicio()/*codigoServicioOPCIONAL*/, 
																					1/*cantidadServicioOPCIONAL*/, 
																					ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
																					ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
																					/*"", numeroAutorizacionOPCIONAL*/
																					"" /*esPortatil*/,
																					false /*excento*/,"",
																					"" /*subCuentaCoberturaOPCIONAL*/
																				);
			}
			catch(Exception e)
			{
				logger.error("**************************************************************************************************");
				logger.error("***********ERROR AL GENERAR EL CARGO EN ATENCION CITAS: "+e+"*********************************************");
				logger.error("**************************************************************************************************");
				errores.add("No se grabï¿½ informaciï¿½n de la solicitud y cargo cita",new ActionMessage("errors.ingresoDatos","la generaciï¿½n del cargo del servicio. Proceso Cancelado. Por favor intentar de nuevo, si el problema persiste reportarlo al adminsitrador del sistema"));
			}
		}
		else
			errores.add("No se grabï¿½ informaciï¿½n de la solicitud y cargo cita",new ActionMessage("errors.ingresoDatos","solicitud y cargo de la cita. Proceso Cancelado"));
		
		return errores;
	}


	private ActionForward accionOrdenar(ActionMapping mapping,Connection con,CitasForm listarCitasForm)throws Exception{
		
		
		listarCitasForm.setListaCitas(ordenarCitas(listarCitasForm.getListaCitas(),listarCitasForm.getUltimaPropiedad(),listarCitasForm.getColumna()));
		listarCitasForm.setUltimaPropiedad(listarCitasForm.getColumna());
		return  mapping.findForward("listado");
	}
	
	public ArrayList<CitaForm> ordenarCitas(ArrayList<CitaForm> lista, String ultimaPropiedad, String propiedad)
	{
		
		//MT 5936: Add Javier 
		boolean ordenamiento= true;
		if (ultimaPropiedad != null && ultimaPropiedad.equals(propiedad)){
			ordenamiento = false;
		}
		SortGenerico sortG=new SortGenerico(propiedad,ordenamiento);		
		Collections.sort(lista,sortG);
		return lista;
		//MT 5936: Fin Javier
		
	}

	
	
	
	private ActionForward accionGeneralResumen(HttpServletRequest request,
			HttpServletResponse response,
			Connection con,
			CitasForm listarCitasForm)throws Exception{
		
		
		
		PersonaBasica pb=new PersonaBasica();
		pb.cargar(con,listarCitasForm.getCodigoPaciente());
		pb.cargarPaciente2(con, listarCitasForm.getCodigoPaciente(),medico.getCodigoInstitucion(),medico.getCodigoCentroAtencion()+"");
		request.getSession().setAttribute("pacienteActivo",pb);
		
		this.personaCargada=(PersonaBasica) request.getSession().getAttribute("pacienteActivo");
		ValidacionesSolicitud vs=new ValidacionesSolicitud(con,listarCitasForm.getNumeroSolicitud(), this.medico,this.personaCargada);
		/**
		
		if(vs.isPediatrica())
		{
/*
 * La valoraciï¿½n de las citas no es modificable en ningun caso
			if(medico.getCodigoPersona()==listarCitasForm.getCodigoMedico() && !estado.equals("resumenat"))
			{ // es el mismo medico que respondio
				response.sendRedirect("../valoracionConsultaExterna/valoracionPediatricaInterconsulta.do?estado=empezar&tipoConsulta=cita&numeroSolicitud="+listarCitasForm.getNumeroSolicitud()+"&nombreUnidadConsulta="+listarCitasForm.getNombreUnidadConsulta().toUpperCase());
			}
			else
			{ // es otro medico*
			if(!estado.equals("resumenat"))
				response.sendRedirect("../valoracionConsultaExterna/valoracionPediatricaInterconsulta.do?estado=resumenModificar&tipoConsulta=cita&codigoCita=0&numeroSolicitud="+listarCitasForm.getNumeroSolicitud()+"&nombreUnidadConsulta="+listarCitasForm.getNombreUnidadConsulta().toUpperCase());
			else
				response.sendRedirect("../valoracionConsultaExterna/valoracionPediatricaInterconsulta.do?estado=resumenat&tipoConsulta=cita&codigoCita=0&numeroSolicitud="+listarCitasForm.getNumeroSolicitud()+"&nombreUnidadConsulta="+listarCitasForm.getNombreUnidadConsulta().toUpperCase());
//			}
		}
		else if(vs.isOdontologia())
        {
            response.sendRedirect("../valoracionConsultaExterna/valoracionInterconsulta.do?estado="+listarCitasForm.getEstado()+"&tipoConsulta=cita&codigoCita=0&numeroSolicitud="+listarCitasForm.getNumeroSolicitud()+"&nombreUnidadConsulta="+listarCitasForm.getNombreUnidadConsulta().toUpperCase()+"&extension="+ConstantesBD.codigoExtensionValoracionOdontologia);
        }
		else if(vs.getEsOftalmologica())
        {
            response.sendRedirect("../valoracionConsultaExterna/valoracionInterconsulta.do?estado="+listarCitasForm.getEstado()+"&tipoConsulta=cita&codigoCita=0&numeroSolicitud="+listarCitasForm.getNumeroSolicitud()+"&nombreUnidadConsulta="+listarCitasForm.getNombreUnidadConsulta().toUpperCase()+"&extension="+ConstantesBD.codigoExtensionValoracionOftalmologica);
        }
        else
		{
/*
 * La valoraciï¿½n de las citas no es modificable en ningun caso
 			if(medico.getCodigoPersona()==listarCitasForm.getCodigoMedico() && !estado.equals("resumenat"))
			{ // es el mismo medico que respondio			
				response.sendRedirect("../valoracionConsultaExterna/valoracionInterconsulta.do?estado=empezar&tipoConsulta=cita&numeroSolicitud="+listarCitasForm.getNumeroSolicitud()+"&nombreUnidadConsulta="+listarCitasForm.getNombreUnidadConsulta());
			}
			else
			{ // es otro medico
*
				if(vs.getCodigoTipoSolicitud()==ConstantesBD.codigoTipoSolicitudConsultaGinecoObstetrica)
				{
					response.sendRedirect("../valoracionConsultaExterna/valoracionInterconsulta.do?estado="+listarCitasForm.getEstado()+"&tipoConsulta=cita&codigoCita=0&numeroSolicitud="+listarCitasForm.getNumeroSolicitud()+"&nombreUnidadConsulta="+listarCitasForm.getNombreUnidadConsulta()+"&extension="+ConstantesBD.codigoExtensionValoracionGineco);
				}
                //else if(vs.getCodigoTipoSolicitud()==ConstantesBD.codigotipos)
				else
				{
					response.sendRedirect("../valoracionConsultaExterna/valoracionInterconsulta.do?estado="+listarCitasForm.getEstado()+"&tipoConsulta=cita&codigoCita=0&numeroSolicitud="+listarCitasForm.getNumeroSolicitud()+"&nombreUnidadConsulta="+listarCitasForm.getNombreUnidadConsulta()+"&extension="+ConstantesBD.codigoExtensionValoracionSinExtension);
				}
//			}
		} **/
		return null;
		
	}

	private ActionForward accionGeneralCitas(ActionMapping mapping,HttpServletRequest request,Connection con,CitasForm listarCitasForm)throws Exception 
	{
		Collection listado;
		Citas c = new Citas();
		//cargar medico
		Medico m = new Medico();
		m.init(System.getProperty("TIPOBD"));
		m.cargarMedico(con, this.medico.getCodigoPersona());
		
		
		//realizar consulta
		if(this.estado.equals("busquedaPaciente"))
		{
			c.cargarCitas(con, this.personaCargada.getCodigoPersona(), listarCitasForm.getCodigoMedico(),listarCitasForm.getFechaInicio(),listarCitasForm.getFechaFin(),listarCitasForm.getHoraInicio(),listarCitasForm.getHoraFin(),listarCitasForm.getCodigoUnidadConsulta(), listarCitasForm.getCodigoEstadoLiquidacion(),listarCitasForm.getCodigoConsultorio(),listarCitasForm.getEstadosCita(),listarCitasForm.getCentroAtencion(),listarCitasForm.getCheckCitasControlPO(),"");
			this.estado =  "serviciosCita";
		}
		else if(this.estado.equals("busquedaGeneral"))
		{
			
			//---Se verifica si entraron por busqueda por fecha, ya que este estado tambiï¿½n se llama
			//--en el link volver del resumen de la valoraciï¿½n por lo cuï¿½l no se debe la bï¿½squeda por el mï¿½dico
			if(!listarCitasForm.isYaEntroABusquedaXFecha())
			{
				listarCitasForm.setCodigoMedicoAnterior(listarCitasForm.getCodigoMedico());
				listarCitasForm.setYaEntroABusquedaXFecha(true);
			}
			c.cargarCitas(con, -1, listarCitasForm.getCodigoMedicoAnterior(),listarCitasForm.getFechaInicio(),
					listarCitasForm.getFechaFin(),listarCitasForm.getHoraInicio(),listarCitasForm.getHoraFin(),
					listarCitasForm.getCodigoUnidadConsulta(), listarCitasForm.getCodigoEstadoLiquidacion(),
					listarCitasForm.getCodigoConsultorio(),listarCitasForm.getEstadosCita(),listarCitasForm.getCentroAtencion(),
					listarCitasForm.getCheckCitasControlPO(),"");
			this.estado =  "serviciosCita";
		}
		else if(this.estado.equalsIgnoreCase("resumenatenciones"))
		{
			c.cargarCitas(con, personaCargada.getCodigoPersona(), listarCitasForm.getCuenta());
		}
		//crear listado
		listado = c.getCitas();
		//crear enlaces del listado
		
		listado=validacionListado(listado);
		//aï¿½adirlo al form
		listarCitasForm.setListaCitas(new ArrayList(listado));
		
		//aï¿½adir encabezado
		//e = setInfoEncabezado(e);
		//listarCitasForm.setEncabezado(e);
		//subir ultimo url busqueda a sesion
		request.getSession().setAttribute("ultimoListadoSolicitudes","../consultarCita/listarCitas.do?estado="+this.estado);

		listarCitasForm.setCheckCitasControlPO(ConstantesBD.acronimoNo);
		return mapping.findForward("listado");
	}
	
	/**
	 * @param listado de solicitudes resultado de la busqueda
	 * @return lista validada con las reglas de solicitudes y modificada para mostrar con el tag display:*
	 */
	private Collection validacionListado(Collection listado) throws SQLException, IllegalAccessException, InvocationTargetException 
	{
		//aï¿½adir a listado definitivo
		ArrayList listadoTemporal= new ArrayList(listado);
		Collection c=new ArrayList();
		
		//	recorrer el listado
		for(int i=0;i<listadoTemporal.size();i++)
		{
			
		//validar condiciones
			Cita temp = (Cita)listadoTemporal.get(i);
			CitaForm cf=new CitaForm();
			cf.reset();
			
			cf.setCodigoCita(temp.getCodigo());
			//unidad de consulta
			cf.setNombreUnidadConsulta(temp.getUnidadConsulta().getNombre().toLowerCase());
			//fecha y hora
			cf.setFechaInicio(UtilidadFecha.conversionFormatoFechaAAp(temp.getFecha()));
			cf.setNombreCompletoPaciente(temp.getNombreCompletoPaciente());
			cf.setNumeroIdentificacionPaciente(temp.getIdentificacionPaciente().getNumeroId());
			cf.setNombreCompletoMedico(temp.getNombreCompletoMedico());
			cf.setNombreEstadoCita(temp.getEstadoCita().getNombre());
			cf.setHoraInicio(UtilidadFecha.convertirHoraACincoCaracteres(temp.getHoraInicio()));
			cf.setEstadoLiquidacionCita(temp.getNombreEstadoLiquidacion());
			cf.setCuentaPaciente(temp.getNumeroCuenta());
			cf.setNumeroSolicitudCita(temp.getNumeroSolicitud());
			cf.setConsecutivoCita(temp.getConsecutivoCita());
			cf.setNombreConsultorio(temp.getNombreConsultorio().toLowerCase());
			cf.setNombreTipoDocumento(temp.getIdentificacionPaciente().getTipoId());			
			cf.setCodigoAgenda(temp.getCodigoAgenda());
			cf.setSolPYP(temp.isSolPYP());
			cf.setNombreCentroAtencion(temp.getNombreCentroAtencion());
			cf.setCentroAtencion(temp.getCodigoCentroAtencion());
			cf.setCodigoPaciente(temp.getCodigoPaciente());
			cf.setCodigoMedico(temp.getCodigoMedico());					
			cf.setNombreUsuario(temp.getNombreUsuario());
			cf.setFechaModifica(UtilidadFecha.conversionFormatoFechaAAp(temp.getFechaModifica()));
			cf.setHoraModifica(temp.getHoraModifica());
			//tipo de consulta
			
			//-- Datos de la cuenta
			cf.setNombreConvenio(temp.getNombreConvenio());
			cf.setNombreContrato(temp.getNombreContrato());
			cf.setNombreClasificacionSocial(temp.getNombreClasificacionSocial());
			cf.setNombreTipoAfiliado(temp.getNombreTipoAfiliado()); 			
			cf.setCodigoEstadoCita(temp.getCodigoEstadoCita());
			cf.setIndiPO(temp.getIndiPO());
			cf.setMotivoNoAtencion(temp.getMotivoNoAtencion());
			cf.setObservacion(temp.getObservaciones());
			cf.setNumeroHistoriaClinica(temp.getNumeroHistoriaClinica());
			
			cf.setCodigoConsultorioUsua(temp.getCodigoConsultorioUsua());			
			cf.setRegistroMedico(temp.getRegistroMedico());
			cf.setLoginUsuCita(temp.getLoginUsuCita());
			cf.setTelefono(temp.getTelefono());
			
			//add Javier Para ordenamiento por fechas			
			try {
				SimpleDateFormat formato = new SimpleDateFormat(ConstantesBD.formatoFechaApp);
				cf.setFechaInicioDate(formato.parse(cf.getFechaInicio()));
			} catch (Exception e) {
				logger.info("No se puede convertir la fecha => "+ cf.getFechaInicio());
			}					
			//Fin Javier
			
						
			if(temp.getEstadoCita().getCodigo()==ConstantesBD.codigoEstadoCitaAtendida && this.esMedico)
			{
				if(estado.equals("resumenAtenciones"))
				{
					cf.setEnlace("listarCitas.do?estado=resumenat&numeroSolicitud="+temp.getNumeroSolicitud()+"&nombreUnidadConsulta="+temp.getUnidadConsulta().getNombre()+"&codigoPaciente="+temp.getCodigoPaciente()+"&codigoEstadoCita="+temp.getEstadoCita().getCodigo()+"&codigoMedico="+temp.getCodigoMedico());
				}
				else
				{
					cf.setEnlace("listarCitas.do?estado=resumen&numeroSolicitud="+temp.getNumeroSolicitud()+"&nombreUnidadConsulta="+temp.getUnidadConsulta().getNombre()+"&codigoPaciente="+temp.getCodigoPaciente()+"&codigoEstadoCita="+temp.getEstadoCita().getCodigo()+"&codigoMedico="+temp.getCodigoMedico());
				}
			}else{
				cf.setEnlace("");
			}
			c.add(cf);
			
		}
		return c;
	}

	/**
	 * @param con 
	 * @param e
	 * @return vector con la estructura de encabezado de la lista dadas las condiciones de las citas
	 */
	/*private Vector setInfoEncabezado(Vector e) 
	{
		//inicializar cada elemento

		e.add(new EncabezadoLista("nombreUnidadConsulta", "Unidad Consulta", true, true, false));
		e.add(new EncabezadoLista("fechaInicio","Fecha",true,true,false));
		e.add(new EncabezadoLista("horaInicio","Hora",true,true,false));
		e.add(new EncabezadoLista("nombreTipoConsulta", "Tipo Consulta", true, true, false));
		e.add(new EncabezadoLista("enlace", "Detalle", true, true, false));

		return e;		
	}*/
	
	private void cargarForm(Connection con, Citas citas, CitasForm citasForm)
	{
		citasForm.setMapaCitas(new HashMap());//se limpia el mapa
		
		int numCitas = citas.getNumCitas();
		String fechaActual = UtilidadFecha.getFechaActual(con);
		String horaActual = UtilidadFecha.getHoraActual(con);
		//citasForm.setListaCitas(new ArrayList());
		
		citasForm.setMapaCitas("numRegistros", numCitas+"");
		
		for( int i = 0; i < numCitas; i++ )
		{
			
			Cita cita = citas.getCita(i);
			logger.info("pasando las citas en el ACtion, codigoCita=> "+cita.getCodigo()+", horaInicio=> "+cita.getHoraInicio());
			citasForm.setMapaCitas("codigoCita_"+i, cita.getCodigo());
			citasForm.setMapaCitas("codigomedico_"+i, cita.getCodigoMedico());
			citasForm.setMapaCitas("codigoEstadoLiquidacion_"+i, cita.getCodigoEstadoLiquidacion());
			citasForm.setMapaCitas("nombreEstadoLiquidacion_"+i, Utilidades.getNombreEstadoLiquidacion(con, cita.getCodigoEstadoLiquidacion()));
			citasForm.setMapaCitas("nombreUnidadAgenda_"+i, cita.getUnidadConsulta().getNombre());
			citasForm.setMapaCitas("horaInicio_"+i, UtilidadFecha.convertirHoraACincoCaracteres(cita.getHoraInicio()));
			citasForm.setMapaCitas("horaFin_"+i, UtilidadFecha.convertirHoraACincoCaracteres(cita.getHoraFin()));
			citasForm.setMapaCitas("numeroIdentificacionPaciente_"+i, cita.getIdentificacionPaciente().getNumeroId());
			citasForm.setMapaCitas("tipoIdentificacionPaciente_"+i, cita.getIdentificacionPaciente().getTipoId());
			citasForm.setMapaCitas("nombreCompletoPaciente_"+i, cita.getNombreCompletoPaciente());
			citasForm.setMapaCitas("nombreEstadoCita_"+i, cita.getEstadoCita().getNombre());
			citasForm.setMapaCitas("codigoEstadoCita_"+i, cita.getEstadoCita().getCodigo());
			citasForm.setMapaCitas("codigoPaciente_"+i, cita.getCodigoPaciente());
			citasForm.setMapaCitas("codigoSexoPaciente_"+i, cita.getSexoPaciente());
			citasForm.setMapaCitas("cuenta_"+i, cita.getCuenta());
			citasForm.setMapaCitas("codigoAgenda_"+i, cita.getCodigoAgenda());
			citasForm.setMapaCitas("perteneceAlMedico_"+i, cita.getPerteneceAlMedico());
			citasForm.setMapaCitas("solPyp_"+i, cita.isSolPYP());
			citasForm.setMapaCitas("prioritaria_"+i, cita.getPrioridad());
			
			citasForm.setMapaCitas("numServicios_"+i, cita.getMapaServicios("numRegistros"));
			
			
			//Validacion para seleccionar el estado de la cita No Asistiï¿½
			if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(citasForm.getFechaConsulta(), fechaActual))
			{
				//Si las fechas son iguales se validan las horas
				if(citasForm.getFechaConsulta().equals(fechaActual))
				{
					if(citasForm.getMapaCitas("horaFin_"+i).toString().compareTo(horaActual)>=0)
						citasForm.setMapaCitas("noAsistio_"+i, ConstantesBD.acronimoNo);
					else
						citasForm.setMapaCitas("noAsistio_"+i, ConstantesBD.acronimoSi);
				}
				else
					citasForm.setMapaCitas("noAsistio_"+i, ConstantesBD.acronimoSi);
					
			}
			else
				citasForm.setMapaCitas("noAsistio_"+i, ConstantesBD.acronimoNo);
			citasForm.setMapaCitas("noAtencion_"+i, ConstantesBD.acronimoSi);
			
			//Se consultan los estados de las solicitudes de la cita , con el fin de saber si a la cita
			//se le puede cambiar el estado a No Asistiï¿½ y No Atencion. Si hay alguna solicitud con estado diferente a Solicitado o Anulado 
			//entonces ya no se podrï¿½ pasar la cita a estado No Asistiï¿½ y no Atencion
			HashMap estadosSolicitudes = Cita.consultarEstadosSolicitudesCita(con, cita.getCodigo()+"");
			for(int j=0;j<Integer.parseInt(estadosSolicitudes.get("numRegistros").toString());j++)
			{
				int estadoSolicitud = Integer.parseInt(estadosSolicitudes.get("estadoHistoriaClinica_"+j).toString());
				if(
						estadoSolicitud != ConstantesBD.codigoEstadoHCSolicitada && 
						estadoSolicitud != ConstantesBD.codigoEstadoHCAnulada && 
						estadoSolicitud != ConstantesBD.codigoNuncaValido
					)
				{
					citasForm.setMapaCitas("noAsistio_"+i, ConstantesBD.acronimoNo);
					citasForm.setMapaCitas("noAtencion_"+i, ConstantesBD.acronimoNo);
				}
					
			}
			
			//Llaves que me permiten controlar si se grabaron los distintos estados de las citas
			citasForm.setMapaCitas("graboNoAtencion_"+i, ConstantesBD.acronimoNo);
			citasForm.setMapaCitas("graboNoAsistio_"+i, ConstantesBD.acronimoNo);
			
			///Se llena la informacion de los servicios
			boolean bandHelp = false ;
			for(int j=0;j<Integer.parseInt(cita.getMapaServicios("numRegistros").toString());j++)
			{
				citasForm.setMapaCitas("codigoServicio_"+i+"_"+j, cita.getMapaServicios("codigoServicio_"+j));
				citasForm.setMapaCitas("codigoCentroCosto_"+i+"_"+j, cita.getMapaServicios("codigoCentroCosto_"+j));
				citasForm.setMapaCitas("numeroSolicitud_"+i+"_"+j, cita.getMapaServicios("numeroSolicitud_"+j));
				//Se carga el codigo de estado medico de la solicitud si existe
				if(!citasForm.getMapaCitas("numeroSolicitud_"+i+"_"+j).toString().toString().equals(""))
					try{
						citasForm.setMapaCitas("codigoEstadoHC_"+i+"_"+j, Utilidades.obtenerEstadoHC(con, citasForm.getMapaCitas("numeroSolicitud_"+i+"_"+j).toString()).getCodigo().equals(ConstantesBD.codigoEstadoHCSolicitada+""));
					}catch (Exception e) {
						logger.error(e.getMessage());
					}
				else
					citasForm.setMapaCitas("codigoEstadoHC_"+i+"_"+j, "");
				
				citasForm.setMapaCitas("observaciones_"+i+"_"+j, cita.getMapaServicios("observaciones_"+j));
				citasForm.setMapaCitas("nombreServicio_"+i+"_"+j, cita.getMapaServicios("nombreServicio_"+j));
				citasForm.setMapaCitas("estadoServicio_"+i+"_"+j, cita.getMapaServicios("estadoServicio_"+j));
				citasForm.setMapaCitas("codigoTipoServicio_"+i+"_"+j, cita.getMapaServicios("codigoTipoServicio_"+j));
				citasForm.setMapaCitas("tieneCondiciones_"+i+"_"+j, cita.getMapaServicios("tieneCondiciones_"+j));
				citasForm.setMapaCitas("codigoEspecialidad_"+i+"_"+j, cita.getMapaServicios("codigoEspecialidad_"+j));
				citasForm.setMapaCitas("puedoResponder_"+i+"_"+j, ConstantesBD.acronimoSi);
				citasForm.setMapaCitas("mostrarsercita_"+i+"_"+j, cita.getMapaServicios("mostrarsercita_"+j));
				
				if(!bandHelp&&cita.getMapaServicios("mostrarsercita_"+j).toString().equals(ConstantesBD.acronimoSi)){
					citasForm.setMapaCitas("mostrarcita_"+i, ConstantesBD.acronimoSi);
					bandHelp=true;
				}else{
					if(!bandHelp)
						citasForm.setMapaCitas("mostrarcita_"+i, ConstantesBD.acronimoNo);
				}
			}
		}
		
	}
	
	/**
	 * Acciï¿½n para la impresion de la consulta de citas ya sea por paciente o por fecha
	 * @param mapping
	 * @param request
	 * @param con
	 * @param citasForm
	 * @param medico
	 * @param esPorPaciente
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionImprimir(ActionMapping mapping, HttpServletRequest request, Connection con, CitasForm citasForm, UsuarioBasico medico, boolean esPorPaciente)throws Exception
	{
		String nombreArchivo;
		Random r= new Random();	
		
		nombreArchivo= "/aBorrar" + r.nextInt() + ".pdf";
		logger.info("nombre archivo___::::"+nombreArchivo);
		logger.info("ubicacion archivo___::::"+ValoresPorDefecto.getFilePath()+ nombreArchivo);
		citasForm.getInfoCvsArchivo().setDescripcion("");
		if(esPorPaciente)
		{
			HttpSession session= request.getSession();
			PersonaBasica pacienteActivo=(PersonaBasica)session.getAttribute("pacienteActivo");
			ConsultaCitasPdf.pdfCitasPaciente(ValoresPorDefecto.getFilePath()+ nombreArchivo, citasForm, medico, pacienteActivo,request);
			citasForm.setEstado("imprimirPaciente");			
		}
		else
		{			
			ConsultaCitasPdf.pdfCitasFecha(ValoresPorDefecto.getFilePath()+ nombreArchivo, citasForm, medico, request);
			citasForm.setEstado("imprimirFecha");
		}
		request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Consulta de Citas");
		return mapping.findForward("abrirPdf");
	}
	
	/**
	 * Acciï¿½n para la impresion de la consulta de citas ya sea por paciente o por fecha
	 * @param mapping
	 * @param request
	 * @param con
	 * @param citasForm
	 * @param medico
	 * @param esPorPaciente
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionImprimirCvs(ActionMapping mapping, HttpServletRequest request, Connection con, CitasForm citasForm, UsuarioBasico medico, boolean esPorPaciente)throws Exception
	{
		String nombreArchivo = "";		
		Random r=new Random();
    	int numeroNombre = r.nextInt();
    	if(numeroNombre < 0)    	
    		numeroNombre = numeroNombre * (-1);	
    		
		
		if(esPorPaciente)
		{
			nombreArchivo = CsvFile.armarNombreArchivo("listadocitapac", medico) +"_"+ numeroNombre;			
			HttpSession session= request.getSession();
			PersonaBasica pacienteActivo=(PersonaBasica)session.getAttribute("pacienteActivo");
			citasForm.getInfoCvsArchivo().setDescripcion(ConsultaCitasPdf.CvsCitasPaciente(ValoresPorDefecto.getFilePath(), nombreArchivo, citasForm, medico, pacienteActivo, request));			
		}
		else
		{
			nombreArchivo = CsvFile.armarNombreArchivo("listadocitafechas", medico) +"_"+ numeroNombre;			
			HttpSession session= request.getSession();
			PersonaBasica pacienteActivo=(PersonaBasica)session.getAttribute("pacienteActivo");
			citasForm.getInfoCvsArchivo().setDescripcion(ConsultaCitasPdf.CvsCitasFecha(ValoresPorDefecto.getFilePath(), nombreArchivo, citasForm, medico, pacienteActivo, request));
		}
		
		UtilidadBD.cerrarConexion(con);	
		request.setAttribute("nombreArchivo",citasForm.getInfoCvsArchivo().getDescripcion());
		request.setAttribute("nombreVentana", "Consulta de Citas");
		return mapping.findForward("descargarFile");		
	}
	
	
	/**
	 * Consulta los servios de la cita 
	 * @param request 
	 * @param medico2 
	 * @param Connection con
	 * @param CitasForm citasForm
	 * */
	private void accionConsultarDetalleCita(Connection con,CitasForm form, UsuarioBasico usuario, HttpServletRequest request)
	{
		Citas citas = new Citas();
		CitaForm cita;
		HashMap parametros = new HashMap();	
		//Carga los parametros de la busqueda		
		cita = (CitaForm)form.getListaCitas().get(Utilidades.convertirAEntero(form.getIndexCita()));
				
		
		UtilidadSesion.notificarCambiosObserver(cita.getCodigoPaciente(), getServlet().getServletContext());
		
		/**para cargar el paciente que corresponda**/
		PersonaBasica paciente= (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		paciente.setCodigoPersona(cita.getCodigoPaciente());
		try {
			paciente.cargar(con,cita.getCodigoPaciente());
			paciente.cargarPaciente(con, cita.getCodigoPaciente(),usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ObservableBD observable = (ObservableBD)getServlet().getServletContext().getAttribute("observable");
		if (observable != null) {
			synchronized (observable) {
				observable.setChanged();
				observable.notifyObservers(new Integer(paciente.getCodigoPersona()));
			}
		}
		
		
		parametros.put("codigocita",cita.getCodigoCita());		
		form.setServiciosCita(citas.serviciosCita(con, parametros));
		
		form.setServiciosCita("listaCita",cita);		
	}
	
	
	/**
	 * Gestion la apertura de las Hojas de Respuesta de las Solicitudes
	 * @param Connection con
	 * @param CitasForm citasForm 
	 * @param ActionMapping mapping
	 * @param HttpServletRequest request
	 * */
	private ActionForward respuestaServicio(Connection con,
										 	CitasForm forma,
										 	ActionMapping mapping,
										 	HttpServletRequest request,
										 	HttpServletResponse response)
	{		
		try
		{
			
			CitaForm cita = (CitaForm)forma.getServiciosCita("listaCita");
			
			logger.info("codigo del paciente >> "+cita.getCodigoPaciente()+" >> "+cita.getCodigoMedico());
			
			//Se carga el paciente en sesiï¿½n
			this.personaCargada.setCodigoPersona(cita.getCodigoPaciente());
			UtilidadesManejoPaciente.cargarPaciente(con, this.medico, this.personaCargada, request);
			
			//Solicitudes Tipo Interconsulta		
			if(forma.getServiciosCita("codigotiposervicio_"+forma.getIndexServicio()).toString().trim().equals(ConstantesBD.codigoServicioInterconsulta+""))			
			{
				String path = "../valoracionConsulta/valoracion.do?estado=empezar" +
					"&numeroSolicitud="+forma.getServiciosCita("numerosolicitud_"+forma.getIndexServicio())+
					"&codigoCita="+forma.getServiciosCita("codigocita_"+forma.getIndexServicio())+
					"&codigoEspecialidad="+forma.getServiciosCita("codigoespecialidad_"+forma.getIndexServicio());
				response.sendRedirect(path);
			}
			else if(forma.getServiciosCita("codigotiposervicio_"+forma.getIndexServicio()).toString().trim().equals(ConstantesBD.codigoServicioNoCruentos+""))
			{
				String peticion=Cita.consultPetXNumSol(con, forma.getServiciosCita("numerosolicitud_"+forma.getIndexServicio())+"");
				try 
				{
					response.sendRedirect("../hojaQuirurgicaDummy/hojaQuirurgica.do?estado=cargarHQxDummy&peticion="+peticion+"&esDummy="+true+"&funcionalidad=consultaCitas&esModificable="+false);
				}
				catch (Exception e) 
				{
				  logger.info("\n problema cargando la hoja Quirurgica "+e);
				}				
				
				return mapping.findForward("paginaAdvertenciasActionMessages");				
			}
			else if(forma.getServiciosCita("codigotiposervicio_"+forma.getIndexServicio()).toString().trim().equals(ConstantesBD.codigoServicioProcedimiento+""))
			{				
				logger.info("entro procedimientos");
				response.sendRedirect("../respuestaProcedimientosDummy/respuestaProcedimientos.do?estado=resumenServicioConsultaExt&numeroSolicitud="+forma.getServiciosCita("numerosolicitud_"+forma.getIndexServicio()).toString()+"&codigoRespuesta="+forma.getServiciosCita("codigoresprocedimiento_"+forma.getIndexServicio()).toString()+"&codigoCita=");				
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
		return null;		
	}

	private boolean esEspecialidadMedico(int codigoEspCita, InfoDatosInt[] especialidadesMedico){
		boolean esEspecilidad=false;
		if(especialidadesMedico != null && especialidadesMedico.length>0){
			for(int i =0;i<especialidadesMedico.length;i++){
				if(especialidadesMedico[i].getCodigo()==codigoEspCita){
					return true;
				}
			}
		}
		return esEspecilidad;
	}
}