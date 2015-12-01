/*
 * @(#)ListarSolicitudesAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.action.ordenesmedicas.interconsultas;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Random;
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
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.EncabezadoLista;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.ordenesmedicas.interconsultas.ListarSolicitudesForm;
import com.princetonsa.actionform.ordenesmedicas.interconsultas.SolicitarForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.JustificacionDinamica;
import com.princetonsa.mundo.Medico;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.solicitudes.Procedimiento;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;
import com.princetonsa.mundo.solicitudes.Solicitudes;
import com.princetonsa.mundo.solicitudes.ValidacionesSolicitud;
import com.princetonsa.pdf.RespuestaProcedimientoPdf;
import com.princetonsa.pdf.ResumenGuardarProcedimientoPdf;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IAurorizacionesEntSubCapitacionServicio;


/**
 * @author rcancino
 * @version 1.0, Feb 11, 2004
 *
 */
public class ListarSolicitudesInterconsultasAction extends Action {
	/**
	 * log del sistema
	 */
	private Logger logger = Logger.getLogger(ListarSolicitudesInterconsultasAction.class);
	/**
	 * Manejo re resumen de atenciones
	 */
	private String encabezadoResumen;
	/**
	 * estado del Form
	 */
	private String estado;
	/**
	 * codigo de la persona cargada en el resumen
	 */
	private int codigoPersona;

	/**
	 * tipo de servicio que se esta prestando
	 */
	private String tipoServicio;
	/**
	 * estado historia clinica de la solicitud en el resumen general
	 */
	private String estadoHistoriaClinicaResumen;

	/**
	 * indica si existe un paciente cargado
	 */
	private boolean pacienteCargado;
	/**
     * indica si se esta listando para epicrisis
	 */
	private boolean epicrisis;

	private UsuarioBasico medico;

	private PersonaBasica personaCargada;
	/**
	 * indica si el usuario solicito que la solicitud vaya a epicrisis
	 */	
	private boolean vaEpicrisis;
	/**
	 * indica el tipo de cambio que se desea realizar sobre la solicitud
	 **/
	private String tipoModificacion;
	
	
	
	/**
	 * Servicios necesarios para la implementación de la funcionalidad
	 */
	
	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.manejoPaciente.AutorizacionesIngresoEstanciaForm");
	
	
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response)
													throws Exception 
													{
		Connection con = null;
		try {
			if (form instanceof ListarSolicitudesForm) 
			{
				if (response == null); //Para evitar que salga el warning
				if (logger.isDebugEnabled()) 
					logger.debug("Entro al Action de Listar Solicitudes");

				ListarSolicitudesForm listarSolicitudesForm =(ListarSolicitudesForm) form;
				SolicitarForm solicitarForm=new SolicitarForm();
				encabezadoResumen=listarSolicitudesForm.getEncabezadoResumen();
				this.estado = listarSolicitudesForm.getEstado();

				logger.warn("[ListarSolicitudesInterconsultasAction] - estado: *"+estado+"*");
				this.tipoServicio = listarSolicitudesForm.getTipoServicio();
				this.epicrisis=Boolean.valueOf(listarSolicitudesForm.isEpicrisis()).booleanValue();


				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());

				try 
				{
					String tipoBD = System.getProperty("TIPOBD");
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					con = myFactory.getConnection();
				} 
				catch (Exception e) 
				{
					//No se cierra conexión porque si llega aca ocurrió un
					//error al abrirla
					logger.error("Problemas abriendo la conexión en ListarSolicitudesAction", e);
					request.setAttribute("codigoDescripcionError",	"errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				//Lo primero que vamos a hacer es validar que se
				//cumplan las condiciones.
				HttpSession session = request.getSession();
				this.medico =(UsuarioBasico) session.getAttribute("usuarioBasico");
				this.personaCargada =(PersonaBasica) session.getAttribute("pacienteActivo");
				InstitucionBasica institucionActual = (InstitucionBasica)session.getAttribute("institucionBasica");

				//Primera Condición: El usuario debe existir
				//la validación de si es médico o no solo se hace en insertar
				if (this.medico == null) 
				{
					if (logger.isDebugEnabled()) 
						logger.debug("No existe el usuario");
					if (con != null && !con.isClosed())
						UtilidadBD.closeConnection(con);
					request.setAttribute("codigoDescripcionError",	"errors.usuario.noCargado");
					return mapping.findForward("paginaError");
				} 
				else if (this.estado == null || this.estado.equals("")) 
				{
					listarSolicitudesForm.reset();
					if (logger.isDebugEnabled()) 
						logger.debug("La accion específicada no esta definida ");
					if (con != null && !con.isClosed()) 
						UtilidadBD.closeConnection(con);
					request.setAttribute("codigoDescripcionError",	"errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
				else if( !UtilidadValidacion.esProfesionalSalud(medico) )
				{
					if(UtilidadValidacion.estaMedicoInactivo(con,medico.getCodigoPersona(),medico.getCodigoInstitucionInt()))
						request.setAttribute("codigoDescripcionError", "errors.profesionalSaludInactivo");
					else
						request.setAttribute("codigoDescripcionError", "errors.usuario.noAutorizado");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				if(this.personaCargada==null || personaCargada.getTipoIdentificacionPersona().equals(""))
				{
					this.pacienteCargado=false;
				}
				else
				{
					this.pacienteCargado=true;
					this.codigoPersona=this.personaCargada.getCodigoPersona();
				}
				//hacer validaciones de cuenta abierta y cerrada

				if (estado.equals("empezar")) 
				{
					return this.accionEmpezar(mapping,con,listarSolicitudesForm, request);
				} 
				else if (estado.equals("interconsultas")) 
				{
					return this.accionGeneralSolicitudes(mapping,request,	con,listarSolicitudesForm,false);
				} 
				else if(estado.equals("resumen"))
				{
					return this.accionResumenSolicitudes(mapping, request, con, listarSolicitudesForm, usuario);
				}
				else if(estado.equals("imprimir"))
				{
					return this.accionImprimir(con,listarSolicitudesForm.getNumeroSolicitud(), mapping, medico, personaCargada,request, institucionActual);
				}
				else if(estado.equals("imprimirExterno"))
				{
					return this.accionImprimirExterno(con, listarSolicitudesForm.getNumeroSolicitud(), mapping, medico, personaCargada, request, solicitarForm, listarSolicitudesForm);
				}
				else if(estado.equals("imprimirExternoRespondido"))
				{
					return this.accionImprimir(con,listarSolicitudesForm.getNumeroSolicitud(), mapping, medico, personaCargada,request, institucionActual);	
				}
				else if(estado.equals("ordenar"))
				{
					return this.accionOrdenar(mapping,con,listarSolicitudesForm);
				}
				else if(estado.equals("guardar"))
				{
					return this.accionGuardarCambiosSolicitud(mapping,request,response,	con,	listarSolicitudesForm);
				}
				else if(estado.equals("verEstadoAutorizacion"))
				{
					return this.accionEstadosAutorizacion(listarSolicitudesForm, con,mapping);
				}
				else if(estado.equals("guardarJustificacionDinamica"))
				{
					return this.accionGuardarJustificacionDinamica(listarSolicitudesForm, con);
				}
				else 
				{
					//Todavía no existe conexión, por eso no se cierra
					request.setAttribute("codigoDescripcionError",	"errors.formaTipoInvalido");
					return mapping.findForward("paginaError");
				}
			} 
			else 
			{
				//Todavía no existe conexión, por eso no se cierra
				request.setAttribute("codigoDescripcionError",	"errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}
		} catch (Exception e) {
			Log4JManager.error(e);
			request.setAttribute("codigoDescripcionError",	"errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
	
	

	/**
	 * Metodo que almacena la justificacion dinamica de los servicios NOPOS
	 * @param listarSolicitudesForm
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardarJustificacionDinamica(		ListarSolicitudesForm listarSolicitudesForm,
			 																					Connection con) throws SQLException
	{
		logger.info("Llega al action a insertar!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    logger.info("Mapa----------->"+listarSolicitudesForm.getJustificacionDinamicaMap());
	    JustificacionDinamica justificacion= new JustificacionDinamica();
	    
	    for(int i=0; i< Integer.parseInt(listarSolicitudesForm.getJustificacionDinamicaMap("numeroJustificaciones_")+""); i++)
	    {
	        if(Utilidades.esSolicitudJustificada(	con,
	                											Integer.parseInt(listarSolicitudesForm.getJustificacionDinamicaMap("numeroSolicitud_")+""), 
	                											Integer.parseInt(listarSolicitudesForm.getJustificacionDinamicaMap("codigoServicio_")+""),  
	                											medico.getCodigoInstitucionInt(), false))
	        {
	            //con,numeroSolicitud,parametro,atributo,descripcion,esArticulo
	            justificacion.setNumeroSolicitud(Integer.parseInt(listarSolicitudesForm.getJustificacionDinamicaMap("numeroSolicitud_")+""));
	            justificacion.setParametro(Integer.parseInt(listarSolicitudesForm.getJustificacionDinamicaMap("codigoServicio_")+""));
	            justificacion.setAtributo(Integer.parseInt(listarSolicitudesForm.getJustificacionDinamicaMap("codigoAtributo_"+i)+""));
	            justificacion.setDescripcion(listarSolicitudesForm.getJustificacionDinamicaMap("descripcionJustificacion_"+i)+"");
	            
	            int inse=justificacion.modificarAtributoJustificacion(con);
	            if(inse<0)
	            {
	                logger.warn("No modifico la justificacion dinamica");
	            }
	        }
	        else
	        {
	            justificacion.setNumeroSolicitud(Integer.parseInt(listarSolicitudesForm.getJustificacionDinamicaMap("numeroSolicitud_")+""));
	            justificacion.setParametro(Integer.parseInt(listarSolicitudesForm.getJustificacionDinamicaMap("codigoServicio_")+""));
	            justificacion.setAtributo(Integer.parseInt(listarSolicitudesForm.getJustificacionDinamicaMap("codigoAtributo_"+i)+""));
	            justificacion.setDescripcion(listarSolicitudesForm.getJustificacionDinamicaMap("descripcionJustificacion_"+i)+"");
	            
	            int inse=justificacion.insertarAtributoJustificacion(con);
	            if(inse<0)
	            {
	                logger.warn("No inserto la justificacion dinamica");
	            }
	        }
	    }
		UtilidadBD.cerrarConexion(con);							
		return null;
	}
	

	/**
	 * @param mapping
	 * @param request
	 * @param con
	 * @param listarSolicitudesForm
	 * @param usuario
	 * 
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionForward accionResumenSolicitudes(ActionMapping mapping, HttpServletRequest request, Connection con, ListarSolicitudesForm listarSolicitudesForm, UsuarioBasico usuario) throws SQLException 
	{
		//cargar paciente, aun si hay un paciente cargado
		//no podemos asegurar que es el mismo involucrado en la solicitud
		this.codigoPersona=listarSolicitudesForm.getCodigoPaciente();
		
		PersonaBasica pb=new PersonaBasica();
		pb.cargar(con,this.codigoPersona);
		pb.cargarPaciente2(con, this.codigoPersona,medico.getCodigoInstitucion(),medico.getCodigoCentroAtencion()+"");
		
		// Código necesario para registrar este paciente como Observer
		Observable observable = (Observable) this.servlet.getServletContext().getAttribute("observable");
		if (observable != null) 
		{
			pb.setObservable(observable);
			// Si ya lo habíamos añadido, la siguiente línea no hace nada
			observable.addObserver(pb);
		}

		//subir paciente a sesion
		request.getSession().setAttribute("pacienteActivo",pb);
		this.personaCargada=(PersonaBasica) request.getSession().getAttribute("pacienteActivo");
				
		//leer estado historia clinica y cuadrar permisos
		this.estadoHistoriaClinicaResumen=listarSolicitudesForm.getEstadoHistoriaClinicaResumen();

		//cargar core validaciones con la solicitud
		ValidacionesSolicitud vs=new ValidacionesSolicitud(con,listarSolicitudesForm.getNumeroSolicitud(), this.medico,this.personaCargada); 
		
		//Valida si es el medico que hizo la solicitud.
		/*if(!vs.puedoModificarSolicitudInterpretada().isTrue())
		{
			logger.info("linea 310 en if de !vs.puedoModificarSolicitudInterpretada().isTrue()");
			request.setAttribute("codigoDescripcionError",	"error.validacionessolicitud.interpretar.medicoNoSolicitante");
			return mapping.findForward("paginaError");
		}*/
		

		if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, personaCargada.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.ingresoEstadoDiferenteAbierto", "errors.ingresoEstadoDiferenteAbierto", true);
		}
		if(UtilidadValidacion.esCuentaValida(con, personaCargada.getCodigoCuenta())<1)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.cuentaNoValida", "errors.paciente.cuentaNoValida", true);
		}
		
		
			Solicitud s=new Solicitud();
			s.cargar(con,listarSolicitudesForm.getNumeroSolicitud()); 
			
			logger.info("s.getCentroCostoSolicitado().getCodigo()        "+ s.getCentroCostoSolicitado().getCodigo());
			logger.info(" s.getCentroCostoSolicitante().getCodigo())      " + s.getCentroCostoSolicitante().getCodigo());
			
			//-Cargar EL codigo
		    listarSolicitudesForm.setCentroCostoSolicitante(s.getCentroCostoSolicitante().getCodigo());
		   
			if(s.getTipoSolicitud().getCodigo()==ConstantesBD.codigoTipoSolicitudProcedimiento)
			{
				//logger.info("Voy bien");
				SolicitudProcedimiento solProcedimiento=new SolicitudProcedimiento();
				solProcedimiento.cargarSolicitudProcedimiento(con, listarSolicitudesForm.getNumeroSolicitud());
				listarSolicitudesForm.setMultiple(solProcedimiento.getMultiple());
				//logger.info("multiple "+listarSolicitudesForm.getMultiple());
			}
			//indicar epicrisis
			listarSolicitudesForm.setVaEpicrisis(s.isVaAEpicrisis());
			listarSolicitudesForm.setCodigoCentroCosto(s.getCentroCostoSolicitado().getCodigo());
			//logger.info("PUEDO RESPONDER"+vs.puedoResponder().isTrue());
			if(this.estadoHistoriaClinicaResumen.equals(String.valueOf(ConstantesBD.codigoEstadoHCSolicitada)))
			{
				
				if(vs.puedoResponder().isTrue())
					listarSolicitudesForm.setResponder(true);
				
				if(vs.puedoModificarSolicitudSolicitada().isTrue())
					listarSolicitudesForm.setModificar(true);
			}
			if(this.estadoHistoriaClinicaResumen.equals(String.valueOf(ConstantesBD.codigoEstadoHCRespondida)))
			{
				logger.info("EN EL ACTION"+vs.puedoInterpretar().isTrue());
				if(vs.puedoInterpretar().isTrue())
					listarSolicitudesForm.setInterpretar(true);
				
				if(vs.puedoModificarSolicitudRespondida().isTrue())
					listarSolicitudesForm.setModificar(true);
			}
			if(this.estadoHistoriaClinicaResumen.equals(String.valueOf(ConstantesBD.codigoEstadoHCInterpretada)))
			{
				/**
				 * Cambio temporal para el caso en que en la valoración se cambia directamente
				 * el estado de la solicitud a 'Interpretada' sin pasar por 'Responder' oid=[6758].
				 * Ya que al cambiar de este modo el estado de la solicitud la interpretacion queda nula
				 * y genera un error en la JSP del resumen
				 */
				
				if(s.getInterpretacion()==null)
					listarSolicitudesForm.setUltimaInterpretacion("");
				else
					listarSolicitudesForm.setUltimaInterpretacion(s.getInterpretacion());
				
				if(vs.puedoModificarSolicitudInterpretada().isTrue())
					listarSolicitudesForm.setModificar(true);
				
			}
			
			if (con != null && !con.isClosed()){
				UtilidadBD.closeConnection(con);
			}
			
			
			
			// Anexo 37 - Cambio 1.50
			listarSolicitudesForm.setSinAutorizacionEntidadsubcontratada(false);
			ArrayList<String> listaAdvertencias = new ArrayList<String>();
			listaAdvertencias = validacionCapitacion(listarSolicitudesForm, usuario);
		
			if(!Utilidades.isEmpty(listaAdvertencias))
			{
				listarSolicitudesForm.setSinAutorizacionEntidadsubcontratada(true);
			}
			
			listarSolicitudesForm.setListaAdvertencias(listaAdvertencias);
			//------------------------------------
			
			return mapping.findForward("resumen");
								
	}

	
	
	
	
	/**
	 * 
	 * @param mapping
	 * @param request
	 * @param con
	 * @param listarSolicitudesForm
	 * @param esResumenAtenciones
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionGeneralSolicitudes(	ActionMapping mapping,
													HttpServletRequest request,
													Connection con,
													ListarSolicitudesForm listarSolicitudesForm,
													boolean esResumenAtenciones)
													throws Exception 
	{
		
		
		logger.info("LISTANDO LAS SOLICITUDES");
		Vector e = new Vector();
		Collection listado;
		Solicitudes s = new Solicitudes();
				
		if(!listarSolicitudesForm.getFiltro().trim().equals(""))
		{
			if(listarSolicitudesForm.getFiltro().trim().equalsIgnoreCase("paciente"))
			{
				if(!pacienteCargado)
				{
					UtilidadBD.closeConnection(con);
					request.setAttribute("codigoDescripcionError",	"errors.paciente.noCargado");
					return mapping.findForward("paginaError");
				}
				//Validar que el usuario no se autoatienda
				ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(medico, personaCargada);
				if(respuesta.isTrue())
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no puede ser autoatendido", respuesta.getDescripcion(), true);
				/*
				 * Tarea 276354
				 */
				if(personaCargada.getCodigoCuenta()==0)
				{
					return ComunAction.accionSalirCasoError(mapping,request,con,logger,"No hay cuenta activa","errors.paciente.cuentaNoAbierta",true);
				}
				else if(personaCargada.getCodigoCuenta()>0)
				{
					s.setCodigoCuenta(personaCargada.getCodigoCuenta());
					if(personaCargada.getExisteAsocio())
					{
						s.setCodigoCuentaasociada(this.personaCargada.getCodigoCuentaAsocio());
					}
				}
			}
			else if(listarSolicitudesForm.getFiltro().trim().equalsIgnoreCase("rango"))
			{
				listarSolicitudesForm.setFiltro("filtroRangoEjecutado");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("filtroSolicitudes");
			}
		}
		else
		{
			UtilidadBD.closeConnection(con);
			return mapping.findForward("solicitarFiltro");
		}
		
		if(listarSolicitudesForm.getFiltro().trim().equalsIgnoreCase("filtroRangoEjecutado"))
		{
			s.setFechaInicialFiltro(listarSolicitudesForm.getFechaInicialFiltro());
			s.setFechaFinalFiltro(listarSolicitudesForm.getFechaFinalFiltro());
			s.setCentroCostoSolicitanteFiltro(listarSolicitudesForm.getCentroCostosSolicitanteFiltro());
			s.setAreaFiltro(listarSolicitudesForm.getAreaFiltro());
			s.setPisoFiltro(listarSolicitudesForm.getPisoFiltro());
			s.setHabitacionFiltro(listarSolicitudesForm.getHabitacionFiltro());
			s.setCamaFiltro(listarSolicitudesForm.getCamaFiltro());
		}
		
		if(listarSolicitudesForm.getIndicador()==null)
		{
			listarSolicitudesForm.setIndicador("0");
		}
		if(listarSolicitudesForm.getIndicador().equals("1")){
			listarSolicitudesForm.reset();
			listarSolicitudesForm.setIndicador("1");
		}
		else{
			listarSolicitudesForm.reset();
			listarSolicitudesForm.setIndicador("0");
		}
		
	    //cargar medico
		Medico m = new Medico();
		m.init(System.getProperty("TIPOBD"));
		m.cargarMedico(con, this.medico.getCodigoPersona());
		
		//preparar objeto para consulta
		//medico.setOcupacionMedica()
		if (this.tipoServicio.equals("responder")) 
		{
			s.setEstadoHistoriaClinica(ConstantesBD.codigoEstadoHCSolicitada);
			s.setCodigoTipoSolicitud(ConstantesBD.codigoTipoSolicitudInterconsulta);
			s.setCodigosEspecialidadSolicitada(m.getEspecialidades());
			
			s.setCodigoMedicoEnSesion(m.getCodigoPersona()+"");
			
			s.setCodigoCentroCostoSolicitado(medico.getCodigoCentroCosto());
			s.setCodigoCentroCostoTratante(medico.getCodigoCentroCosto());
			s.setCodigoOcupacionSolicitada(medico.getCodigoOcupacionMedica());
			s.setCodigoCentroAtencionCuenta(medico.getCodigoCentroAtencion());
		} 
		else if (this.tipoServicio.equals("interpretar")) 
		{
				
			s.setCodigoCentroCostoSolicitante(medico.getCodigoCentroCosto());
			s.setEstadoHistoriaClinica(ConstantesBD.codigoEstadoHCRespondida);
			s.setCodigoTipoSolicitud(ConstantesBD.codigoTipoSolicitudInterconsulta);
		} 
		else if (this.tipoServicio.equals("ordenMedicaInter"))
		{
			s.setCodigoPaciente(this.personaCargada.getCodigoPersona());
			if(this.personaCargada.getExisteAsocio())
			{
				s.setCodigoCuentaasociada(this.personaCargada.getCodigoCuentaAsocio());
			}
			else
			{
				s.setCodigoCuentaasociada(-1);
			}
			if(this.personaCargada.getCodigoCuenta()==0 && personaCargada.getExisteAsocio())
			{
			    listarSolicitudesForm.setMostrarMensaje(true);
			    //el usuario no es del mismo centro de costo
			    //ya que el paciente no tiene cuenta abierta
			    listarSolicitudesForm.setMedicoMismoCentroCosto(false);
			}
			if(this.personaCargada.getCodigoCuenta()!=0 && personaCargada.getExisteAsocio())
			{
			    if(medico.getCodigoCentroCosto()==ConstantesBD.codigoCentroCostoUrgencias)
			    {
			        //ya que si existe asocio el centro de costo es urgencias.
			        listarSolicitudesForm.setMedicoMismoCentroCosto(false);
			    }
			}
			s.setCodigoTipoSolicitud(ConstantesBD.codigoTipoSolicitudInterconsulta);
			this.tipoServicio = "modificar"; 
		}
		listarSolicitudesForm.setListarPorMedico(this.estado.equals("medico")?true:false);
		s.setCodigoCentroCostoIntentaAcceso(medico.getCodigoCentroCosto());
		//realizar consulta
		
		logger.info("EJECUTAR CONSULTA");
		s.listarSolicitudes(con, medico.getCodigoInstitucionInt());
		
		//crear listado
		listado = s.getListadoSolicitudes();
		//crear enlaces del listado
		
		listado=validacionListado(con, listado, this.tipoServicio);

		
		//añadirlo al form
		listarSolicitudesForm.setListaSolicitudes(new ArrayList(listado));
			
		//añadir encabezado
		e = setInfoEncabezado(e);
		listarSolicitudesForm.setEncabezado(e);
		listarSolicitudesForm.setTipoServicio(this.tipoServicio);
		listarSolicitudesForm.setEpicrisis(this.epicrisis);
		
		//subir ultimo url busqueda a sesion
		request.getSession().setAttribute("ultimoListadoSolicitudes","../solicitudes/listarSolicitudesInterconsultas.do?estado="+this.estado+"&tipoServicio="+this.tipoServicio+"&recuperarListado=true");
		
		//-Se consultan los procedimientos e interconsultas asignadas al paciente por la solicitud correspondiente
		//--para poder mostrarlos en la ventana del listado de solicitudes procedimientos e interconsultas
		listarSolicitudesForm.setInterconsultasProcedimientosSolicitud(s.consultarInterconsultasProcedimientos(con, this.personaCargada.getCodigoCuenta()));
	
		if (con != null && !con.isClosed()) 
		{
			UtilidadBD.closeConnection(con);
		}

		return mapping.findForward("listado");
	}

	/**
	 * @param listado de solicitudes resultado de la busqueda
	 * @return lista validada con las reglas de solicitudes y modificada para mostrar con el tag display:*
	 */
	@SuppressWarnings("unchecked")
	private Collection validacionListado(Connection con, Collection listado, String tipoServicio) throws SQLException 
	{
		//logger.info("Listado");
		//añadir a listado definitivo
		ArrayList listadoTemporal= new ArrayList(listado);
		Collection c=new ArrayList();
		//	recorrer el listado
		logger.info("TAMANIO DEL LISTADO -->"+listadoTemporal.size());
		for(int i=0;i<listadoTemporal.size();i++)
		{
			//validar condiciones
			Solicitud temp = (Solicitud)listadoTemporal.get(i);
			SolicitarForm sf=new SolicitarForm();
			sf.reset();
			sf.setFechaSolicitud(temp.getFechaSolicitud()+" "+temp.getHoraSolicitud().substring(0,5));
			//la fecha de respuesta se debe sacar de la valoracion
			if(temp.getHoraRespuesta()!=null && temp.getHoraRespuesta().length()>5)
			{
				sf.setFechaRespuesta(temp.getFechaRespuesta()+" "+temp.getHoraRespuesta().substring(0,5));
			}
			else
			{
				sf.setFechaRespuesta(temp.getFechaRespuesta());
			}
			sf.setNombreCentroCostoSolicitado(temp.getCentroCostoSolicitado().getNombre());
			
			sf.setNumeroSolicitud(temp.getNumeroSolicitud());
			sf.setConsecutivoOrdenesMedicas(temp.getConsecutivoOrdenesMedicas());
			sf.setNombreTipoSolicitud(temp.getTipoSolicitud().getNombre());
            sf.setCodigoTipoSolicitudInt(temp.getTipoSolicitud().getCodigo());
			sf.setEstado(temp.getEstadoHistoriaClinica().getNombre());
			sf.setLabelUrgente(temp.isUrgente()?"<font color=\"#FF0000\">Urgente</font>":"");
			sf.setNombrePaciente(temp.getNombrePaciente());
			sf.setNombreMedico(temp.getNombreMedico());
			sf.setNombreMedicoResponde(temp.getNombreMedicoResponde());
			sf.setCodigoCentroAtencionCuentaSol(temp.getCodigoCentroAtencionCuentaSol());
			sf.setNombreCentroAtencionCuentaSol(temp.getNombreCentroAtencionCuentaSol());
			sf.setCama(temp.getCama());
			///***********************************////////////////////////
			
			/*
			if(tipoServicio.equals("interpretar"))
			{
				Valoracion valoracion=new Valoracion();
				valoracion.setNumeroSolicitud(temp.getNumeroSolicitud());
				valoracion.cargar(con);
				sf.setNombreMedico(valoracion.getMedicoResponsable().getNombreUsuario());
			}
			else
			{
				sf.setNombreMedico(temp.getNombreMedico()+" "+temp.getApellidoMedico());
			}
			*/
			sf.setNombreEspecialidadSolicitante(temp.getEspecialidadSolicitante().getNombre());
			sf.setCheckEpicrisis("<input type=\"checkbox\" name=\"valores(vaEpicrisis_"+i+")\"  id=\"vaEpicrisis_"+i+"\"  "+(temp.isVaAEpicrisis()?"checked ":"")+"/>"+"<input type=\"hidden\" name=\"numeroSolicitud_"+i+"\" value=\""+temp.getNumeroSolicitud()+"\" />");
			if(temp.getEspecialidadSolicitada() != null)
				sf.setNombreEspecialidadSolicitada(temp.getEspecialidadSolicitada().getNombre());
			else
				sf.setNombreEspecialidadSolicitada("");
			
			if(encabezadoResumen!=null && !encabezadoResumen.equals(""))
			{
				String[] resultado=encabezadoResumen.split("@");
				if(resultado[0].equals("false"))
				{
					//A las solicitudes de procedimientos se les quitó el boton de imprimir común
					//que está en el encabezado superior e inferior
					if(temp.getTipoSolicitud().getCodigo()!=ConstantesBD.codigoTipoSolicitudProcedimiento)
						sf.setEnlace("listarSolicitudesInterconsultas.do?estado=resumen&tipoServicio="+this.tipoServicio+"&numeroSolicitud="+temp.getNumeroSolicitud()+"&codigoPaciente="+temp.getCodigoPaciente()+"&estadoHistoriaClinicaResumen="+temp.getEstadoHistoriaClinica().getCodigo()+"&codigoTipoSolicitud="+temp.getTipoSolicitud().getCodigo()+"&encabezadoResumen=false&imprimirPagina=preparar");
					else
						sf.setEnlace("listarSolicitudesInterconsultas.do?estado=resumen&tipoServicio="+this.tipoServicio+"&numeroSolicitud="+temp.getNumeroSolicitud()+"&codigoPaciente="+temp.getCodigoPaciente()+"&estadoHistoriaClinicaResumen="+temp.getEstadoHistoriaClinica().getCodigo()+"&codigoTipoSolicitud="+temp.getTipoSolicitud().getCodigo()+"&encabezadoResumen=false");
				}
				else
				{
					sf.setEnlace("listarSolicitudesInterconsultas.do?estado=resumen&tipoServicio="+this.tipoServicio+"&numeroSolicitud="+temp.getNumeroSolicitud()+"&codigoPaciente="+temp.getCodigoPaciente()+"&estadoHistoriaClinicaResumen="+temp.getEstadoHistoriaClinica().getCodigo()+"&codigoTipoSolicitud="+temp.getTipoSolicitud().getCodigo());
				}
			}
			else
			{
				sf.setEnlace("listarSolicitudesInterconsultas.do?estado=resumen&tipoServicio="+this.tipoServicio+"&numeroSolicitud="+temp.getNumeroSolicitud()+"&codigoPaciente="+temp.getCodigoPaciente()+"&estadoHistoriaClinicaResumen="+temp.getEstadoHistoriaClinica().getCodigo()+"&codigoTipoSolicitud="+temp.getTipoSolicitud().getCodigo());
			}
			c.add(sf);
		}
		return c;
	}

	/**
	 * @param e
	 * @return vector con la estructura de encabezado de la lista dadas las condiciones de cada accion de solicitudes
	 */
	@SuppressWarnings("unchecked")
	private Vector setInfoEncabezado(Vector e) 
	{
		//inicializar cada elemento
		if (this.tipoServicio.equals("responder")) 
		{
			e.add(new EncabezadoLista("labelUrgente", "Urgente", true, true, false));
			e.add(new EncabezadoLista("fechaSolicitud","Fecha Solicitud",true,true,false));
			e.add(new EncabezadoLista("consecutivoOrdenesMedicas","Orden Nº",true,true,false));
			e.add(new EncabezadoLista("nombreCentroAtencionCuentaSol","Centro de Atencion del Paciente",true,true,false));
			e.add(new EncabezadoLista("nombrePaciente", "Paciente", true, true, false));
			e.add(new EncabezadoLista("nombreMedico", "Solicitante", true, true, false));
			e.add(new EncabezadoLista("nombreEspecialidadSolicitante","Especialidad Solicitante",true,true,false));
			e.add(new EncabezadoLista("nombreEspecialidadSolicitada","Especialidad Solicitada",true,true,false));
			e.add(new EncabezadoLista("estadoAutorizacion","Estado Aut",true,true,false));
			//e.add(new EncabezadoLista("enlace", "Detalle", true, true, false));
			e.add(new EncabezadoLista("cama", "Cama", true, true, false));
		}
		else if (this.tipoServicio.equals("interpretar")) 
		{
			e.add(new EncabezadoLista("fechaSolicitud","Fecha Orden",true,true,false));
			e.add(new EncabezadoLista("consecutivoOrdenesMedicas","Orden Nº",true,true,false));
			e.add(new EncabezadoLista("nombreCentroAtencionCuentaSol","Centro de Atencion del Paciente",true,true,false));
			e.add(new EncabezadoLista("nombrePaciente","Nombre Paciente",true,true,false));
			e.add(new EncabezadoLista("nombreMedicoResponde","Medico Responde",true,true,false));
			e.add(new EncabezadoLista("nombreEspecialidadSolicitada","Especialidad Respuesta",true,true,false));
			e.add(new EncabezadoLista("fechaRespuesta","Fecha Respuesta",true,true,false));
			//e.add(new EncabezadoLista("enlace", "Detalle", true, true, false));
			e.add(new EncabezadoLista("cama", "Cama", true, true, false));
		} 
		else if (this.tipoServicio.equals("modificar")) 
		{
			if(!this.epicrisis)
			{
				e.add(new EncabezadoLista("fechaSolicitud","Fecha Orden",	true,true,false));
				//****************************************
				e.add(new EncabezadoLista("nombreCentroCostoSolicitado","Centro Costo Solicitado",	true,true,false));
				e.add(new EncabezadoLista("nombreMedico","Solicitante",	true,true,false));
				//****************************************
				if(this.estado.equals("medico"))
					e.add(new EncabezadoLista("nombrePaciente", "Paciente", true, true, false));
				e.add(new EncabezadoLista("nombreTipoSolicitud", "Servicio", true, true, false));
				e.add(new EncabezadoLista("nombreEspecialidadSolicitada","Especialidad",true,true,false));
				e.add(new EncabezadoLista("consecutivoOrdenesMedicas","Orden Nº",true,true,false));
				e.add(new EncabezadoLista("estado", "Estado Medico", true, true, false));
				//e.add(new EncabezadoLista("enlace", "Detalle", true, true, false));
				e.add(new EncabezadoLista("cama", "Cama", true, true, false));

			}
			else
			{
				//cambiamos las columnas para la epicrisis
				e.add(new EncabezadoLista("fechaSolicitud","Fecha Solicitud",	false,true,false));
				if(this.estado.equals("medico"))
					e.add(new EncabezadoLista("nombrePaciente", "Paciente", false, true, false));
				e.add(new EncabezadoLista("nombreEspecialidadSolicitada","Especialidad",true,true,false));
				e.add(new EncabezadoLista("consecutivoOrdenesMedicas","Orden Nº",false,true,false));
				//e.add(new EncabezadoLista("enlace", "Detalle", true, true, false));
				e.add(new EncabezadoLista("cama", "Cama", true, true, false));

				e.add(new EncabezadoLista("checkEpicrisis", "Enviar a Epicrisis",false,true,false));
			}
		}
		return e;
	}

	/**
	 * Acciones a realizar en el estado empezar
	 * @param mapping
	 * @param con
	 * @param listarSolicitudesForm
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionEmpezar( 	ActionMapping mapping,
																Connection con,
																ListarSolicitudesForm listarSolicitudesForm,
																HttpServletRequest request)
																throws Exception 
	{
		//Se limpian los datos anteriores
		listarSolicitudesForm.setFiltro("");
		listarSolicitudesForm.setCentroCostosSolicitanteFiltro("");
		listarSolicitudesForm.setFechaInicialFiltro(UtilidadFecha.getFechaActual());
		listarSolicitudesForm.setFechaFinalFiltro(UtilidadFecha.getFechaActual());
		listarSolicitudesForm.reset();
		//	indicador que me ayuda a saber que se va a listar interocnsultas
		//y procedimientos del módulo Ordenes Médicas
		listarSolicitudesForm.setIndicador("0");
		this.vaEpicrisis=true;
		if (con != null && !con.isClosed()) 
			UtilidadBD.closeConnection(con);
		
		// en caso de que sea interpretacion solo se debe accesar los 
		// profesional de la salud Médicos (MEDICOS)
		if (this.tipoServicio.equals("interpretar")) 
		{
			String mensaje = UtilidadValidacion.esMedico(this.medico); 
		    if(!mensaje.equals(""))
		    {
		        //s.listarSolicitudes(con);
		        ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage(mensaje));
				logger.warn("opcion solo valida para profesional de la salud MEDICO");
				saveErrors(request, errores);	
				this.estado="empezar";
				this.closeConnection(con);									
				return mapping.findForward("empezar");
		    }
		}
		
		return mapping.findForward("empezar");

	}
	
	/**
	 * Guardar cambios de la solicitud
	 * @param mapping
	 * @param request
	 * @param response
	 * @param con
	 * @param listarSolicitudesForm
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	private ActionForward accionGuardarCambiosSolicitud(	ActionMapping mapping, 
	        																			HttpServletRequest request, 
	        																			HttpServletResponse response, 
	        																			Connection con, 
	        																			ListarSolicitudesForm listarSolicitudesForm)
																						throws SQLException, IOException 
	{
		int numeroSolicitud=listarSolicitudesForm.getNumeroSolicitud();
		this.tipoModificacion=listarSolicitudesForm.getTipoModificacion();

		Solicitud s=new Solicitud();
		s.setNumeroSolicitud(listarSolicitudesForm.getNumeroSolicitud());
		//guardar epicrisis si existe
		if(this.epicrisis)
		{
			//viene de un listado general usado en la epicrisis
			//HashMap mapa=listarSolicitudesForm.();
			int numResultados=listarSolicitudesForm.getNumeroResultados();
			
			for(int i=0;i<numResultados;i++)
			{
				//cargar solicitud
				s.cargar(con,Integer.parseInt(request.getParameter("numeroSolicitud_"+i) ));
				if(request.getParameter("valores(vaEpicrisis_"+i+")")==null){
					//se quito el check del campo,no va a epicrisis
					s.modificarEpicrisis(con,false);
				}else if(request.getParameter("valores(vaEpicrisis_"+i+")").equals("on")){
					//va a epicrisis
					s.modificarEpicrisis(con,true);
				}
			}
			if (con != null && !con.isClosed()) 
				UtilidadBD.closeConnection(con);
			//response.sendRedirect((String)request.getSession().getAttribute("ultimoListadoSolicitudes"));
			return mapping.findForward("respuestaEpicrisis");		  	
		}
		else
		{
			//viene del resumen general
		  if(request.getParameter("vaEpicrisis")==null)
		  {
			  this.vaEpicrisis=false;
		  }
		  else
		  {
		  	this.vaEpicrisis=true;
		  }
		}
		
		s.modificarEpicrisis(con,this.vaEpicrisis);
		   //desviar a pagina correspondiente
		
		logger.info("this.tipoModificacion "+this.tipoModificacion);
		
		if(this.tipoModificacion.equals("modificarSolicitud"))
		{
		    response.sendRedirect("guardarInterconsulta.do?estado=modificar&numeroSolicitud="+numeroSolicitud);
		}
		else if(this.tipoModificacion.equals("responderMultiple"))
		{
		    response.sendRedirect("../procedimiento/procedimiento.do?estado=empezar&numeroSolicitud="+numeroSolicitud+"&ind="+listarSolicitudesForm.getInd());		  	
		}
		else if(this.tipoModificacion.equals("modificarProcedimiento"))
		{
			response.sendRedirect("procedimientos.do?estado=modificar&numeroSolicitud="+numeroSolicitud);
		}
		else if(this.tipoModificacion.equals("responderInterconsulta"))
		{
		    response.sendRedirect("../respuestaInterconsulta/respuesta.do?estado=empezar&numeroSolicitud="+numeroSolicitud);
		}
		else if(this.tipoModificacion.equals("responderProcedimiento"))
		{
		    response.sendRedirect("../procedimiento/procedimiento.do?estado=empezar&numeroSolicitud="+numeroSolicitud+"&ind="+listarSolicitudesForm.getInd());		  	
		}
		else if(this.tipoModificacion.equals("modificarRespuestaProcedimiento"))
		{
		    response.sendRedirect("../procedimiento/procedimiento.do?estado=modificar&numeroSolicitud="+numeroSolicitud);		  	
	  	}
		else if(this.tipoModificacion.equals("modificarInterpretacion"))
		{
			if(listarSolicitudesForm.getInterpretacion().equals("")==false)
			{
				String interpretacion=UtilidadCadena.cargarObservaciones(listarSolicitudesForm.getInterpretacion(),"",this.medico);
				s.modificarInterpretacionSolicitud(con,interpretacion);
			}
			if (con != null && !con.isClosed()) 
				UtilidadBD.closeConnection(con);
			response.sendRedirect((String)request.getSession().getAttribute("ultimoListadoSolicitudes")+"&origen=interpretar");		  	
		}
		else if(this.tipoModificacion.equals("anulada"))
		{
			UtilidadBD.closeConnection(con);	
			response.sendRedirect((String)request.getSession().getAttribute("ultimoListadoSolicitudes"));		  	
		} 
		else
		{
			request.setAttribute("codigoDescripcionError","errors.formaTipoInvalido");
			UtilidadBD.cerrarConexion(con);
			return mapping.findForward("paginaError");
		}
		if (con != null && !con.isClosed()) 
			UtilidadBD.closeConnection(con);
		return null;
	}
	
	/**
	 * Accion ordenar
	 * @param mapping
	 * @param con
	 * @param listarSolicitudesForm
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private ActionForward accionOrdenar(ActionMapping mapping, Connection con, ListarSolicitudesForm listarSolicitudesForm)throws Exception	
    {
		listarSolicitudesForm.setListaSolicitudes(Listado.ordenarColumna(new ArrayList(listarSolicitudesForm.getListaSolicitudes()),listarSolicitudesForm.getUltimaPropiedad(),listarSolicitudesForm.getColumna()));
		listarSolicitudesForm.setUltimaPropiedad(listarSolicitudesForm.getColumna());
		
		closeConnection(con);
		return  mapping.findForward("listado");
	}
	
	/**
	 * Cierra la conexión en el caso que esté abierta
	 * @param con
	 * @throws SQLException
	 */
	private void closeConnection(Connection con) throws SQLException
	{
		if( con != null && !con.isClosed() )
			UtilidadBD.closeConnection(con);
	}
	
	/**
	 * Accion para la impresion de la respuesta a la solicitud de un procedimiento
	 * @param con
	 * @param numeroSolicitud
	 * @param mapping
	 * @param medico
	 * @param pacienteActivo (localmente es personaCargada) 
	 * @param request
	 * @return Action Forward
	 * @throws SQLException
	 */
	private ActionForward accionImprimir(Connection con, int numeroSolicitud, ActionMapping mapping,UsuarioBasico medico,PersonaBasica pacienteActivo,HttpServletRequest request, InstitucionBasica institucionActual)throws SQLException 
	{
			logger.info("Accion de imprimir");
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
			else if(solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCRespondida || solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCInterpretada || solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCAnulada)
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
	private ActionForward accionImprimirExterno(Connection con, int numeroSolicitud, ActionMapping mapping,UsuarioBasico usu,PersonaBasica pacienteActivo, HttpServletRequest request, SolicitarForm solicitarForm,ListarSolicitudesForm listarSolicitudesForm )throws SQLException 
	{
		SolicitudProcedimiento solicitud=new SolicitudProcedimiento();
		Procedimiento procedimiento=new Procedimiento();
		
		solicitud.cargarSolicitudProcedimiento(con, numeroSolicitud);
		solicitud.getImpresion();
		String nombreArchivo;
		Random r=new Random();
		nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
		
		if(solicitud.getEstadoHistoriaClinica().getCodigo()==ConstantesBD.codigoEstadoHCRespondida)
		{
			logger.info("Impresion de respuesta");
			RespuestaProcedimientoPdf.pdfImprimirRespuesta(con, ValoresPorDefecto.getFilePath() +nombreArchivo, solicitud, medico, pacienteActivo, procedimiento);
		}
		else
		{
			logger.info("Impresion de externo 2");
			ResumenGuardarProcedimientoPdf.pdfImprimirExterno2(con, ValoresPorDefecto.getFilePath() +nombreArchivo, solicitud,usu, pacienteActivo, solicitarForm,listarSolicitudesForm);
		}
		request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Procedimientos Externos");
		UtilidadBD.cerrarConexion(con);
        return mapping.findForward("abrirPdf");
	
	}
	
	
	private ActionForward accionEstadosAutorizacion(ListarSolicitudesForm listarSolicitudesForm, Connection con,ActionMapping mapping) {
		
		logger.info("El numero de solicitud es: "+ listarSolicitudesForm.getNumeroSolicitud());
		listarSolicitudesForm.setEstadoAuto(UtilidadesOrdenesMedicas.obtenerConvenioEstadoSolicitud(listarSolicitudesForm.getNumeroSolicitud()));
		
		return mapping.findForward("estadosAutorizacion");
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Realiza las validaciones de capitación referentes al cambio número 1.52 del Anexo 37
	 * @param forma
	 * @param usuario
	 * @return ArrayList<String>
	 * @author Cristhian Murillo
	 */
	private ArrayList<String> validacionCapitacion(ListarSolicitudesForm forma, UsuarioBasico usuario)
	{
		
		ArrayList<String> listaAdvertencias = new ArrayList<String>();
		try{
			HibernateUtil.beginTransaction();
			IAurorizacionesEntSubCapitacionServicio aurorizacionesEntSubCapitacionServicio = ManejoPacienteServicioFabrica.crearAurorizacionesEntSubCapitacionServicio(); 
			IConvenioServicio convenioServicio = FacturacionServicioFabrica.crearConvenioServicio();	
			String numeroSolicitud = forma.getNumeroSolicitud()+"";
			
			boolean tieneConvenioCapitado 			= true;
			boolean servicioAutorizado 				= false;
						
			DtoSolicitud dtoSolicitud = new DtoSolicitud(); 
			ArrayList<DtoSolicitud> listasolicitudes = new ArrayList<DtoSolicitud>();
			dtoSolicitud.setNumeroSolicitud(Integer.parseInt(numeroSolicitud));
			dtoSolicitud.setInstitucion(usuario.getCodigoInstitucionInt());
			
			/* Si se le envía el número de la solicitud, se supone que solo debe retornar una. Por eso se toma  listasolicitudes.get(0) */
			listasolicitudes.addAll(aurorizacionesEntSubCapitacionServicio.obtenerSolicitudesSubcuenta(dtoSolicitud));
			
			
			if(!Utilidades.isEmpty(listasolicitudes))
			{
				dtoSolicitud = new DtoSolicitud(); dtoSolicitud = listasolicitudes.get(0);
				
				/* Se obtiene el convenio de la solicitud para validar si es capitado */
				Convenios convenios = new Convenios();
				convenios = convenioServicio.findById(dtoSolicitud.getCodigoConvenio());
				
				
				/* Se valida que el convenio sea capitado al igual que sus contratod y que a su ves los contratos esten vigentes */
				if(convenios.getCapitacionSubcontratada() != null){
					if(convenios.getCapitacionSubcontratada().equals(ConstantesBD.acronimoSiChar))
					{
						tieneConvenioCapitado =  true;
					}
					else {
						/* El convenio no maneja capitación */
						tieneConvenioCapitado =  false;
					}
				}
				/* -------------------------------------------------------------------------------------------------------------------- */
				
				
				if(tieneConvenioCapitado)
				{
					/* Se toma la solicitud y se cargan todas las autorizaciones de entidad subcontratada asociadas */
					DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacion = new DtoAutorizacionEntSubcontratadasCapitacion();
					dtoAutorizacionEntSubcontratadasCapitacion.setNumeroOrden(dtoSolicitud.getNumeroSolicitud());
					ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> listaAutorizacionesEntSubPorSolicitud = 
						aurorizacionesEntSubCapitacionServicio.obtenerAutorizacionesPorEntSubPorNumeroSolicitud(dtoAutorizacionEntSubcontratadasCapitacion);
				
					ArrayList<AutorizacionesEntSubServi> listaTodosServiciosAutorizados = new ArrayList<AutorizacionesEntSubServi>();
					
					
					/* Busco las autorizaciones que estén Autorizadas y cargo todos los servicios de estas */
					for (DtoAutorizacionEntSubcontratadasCapitacion autorizacionPorSolicitud : listaAutorizacionesEntSubPorSolicitud) 
					{
						if(autorizacionPorSolicitud.getEstado().equals(ConstantesIntegridadDominio.acronimoAutorizado))
						{
							// Se carga la lista de Lista Servicios por Autorización de entidad Subcontratada
					    	DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacionServi = new  DtoAutorizacionEntSubcontratadasCapitacion();
					    	dtoAutorizacionEntSubcontratadasCapitacionServi.setAutorizacion(autorizacionPorSolicitud.getAutorizacion());
					    	listaTodosServiciosAutorizados.addAll(aurorizacionesEntSubCapitacionServicio.listarAutorizacionesEntSubServiPorAutoEntSub(dtoAutorizacionEntSubcontratadasCapitacionServi));
						}
					}
					//----------------------------------------------------------------------------------------------------------------------------
					
					
					/*  Comparo los servicios a responder contra los autorizados a ver si concuerdan */
					for (DtoServiciosAutorizaciones dtoServiciosAutorizaciones : dtoSolicitud.getListaServicios()) 
					{
						// Cortar el ciclo para mejorar rendimiento
						if(servicioAutorizado){ break; }
						
						for (AutorizacionesEntSubServi autorizacionesEntSubServi : listaTodosServiciosAutorizados) {
							Log4JManager.info(dtoServiciosAutorizaciones.getCodigoServicio()+ "=" + autorizacionesEntSubServi.getServicios().getCodigo());
							if(dtoServiciosAutorizaciones.getCodigoServicio() == autorizacionesEntSubServi.getServicios().getCodigo()){
								servicioAutorizado 	= true;
								break;
							}
						}
					}
					
					
					if(servicioAutorizado){
						/* Si SI tiene asociada una Autorización de Capitación Subcontratada, 
						 * se debe continuar con el flujo actual de la funcionalidad. */
						listaAdvertencias = new ArrayList<String>();
					}
					else{
						/* Si NO tiene asociada una Autorización de Capitación Subcontratada, 
						 * se debe mostrar el siguiente mensaje informativo, permitiendo informarle al  
						 * usuario que la orden que responde no tiene asociada una autorización de capitación subcontratada: */
						String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.ordenSinAutorizacionIE");
						listaAdvertencias.add(mensajeConcreto);
					}
				}
				else
				{
					/* Si NO maneja Capitación Subcontratada, se debe continuar con el 
					 * flujo actual de la funcionalidad. */
					listaAdvertencias = new ArrayList<String>();
				}
			}
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR validacionCapitacion", e);
			HibernateUtil.abortTransaction();
		}
		return listaAdvertencias;
	}	
	
	
}
