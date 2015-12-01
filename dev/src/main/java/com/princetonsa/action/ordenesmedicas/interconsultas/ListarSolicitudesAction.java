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

import util.ConstantesBD;

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
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.EncabezadoLista;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.ordenesmedicas.interconsultas.ListarSolicitudesForm;
import com.princetonsa.actionform.ordenesmedicas.interconsultas.SolicitarForm;
import com.princetonsa.dao.DaoFactory;
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


/**
 * @author rcancino
 * @version 1.0, Feb 11, 2004
 *
 */
public class ListarSolicitudesAction extends Action {
	/**
	 * log del sistema
	 */
	private Logger logger = Logger.getLogger(ListarSolicitudesAction.class);
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
	 *  */

	private String tipoModificacion;
	
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

				logger.info("\n\n Estado ListarSolicitudesAction => "+estado);
				this.tipoServicio = listarSolicitudesForm.getTipoServicio();
				this.epicrisis=Boolean.valueOf(listarSolicitudesForm.isEpicrisis()).booleanValue();

				try 
				{
					String tipoBD = System.getProperty("TIPOBD");
					DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
					con = myFactory.getConnection();
				}
				catch (Exception e) 
				{
					e.printStackTrace();
					//No se cierra conexión porque si llega aca ocurrió un
					//error al abrirla
					logger.error("Problemas abriendo la conexión en ListarSolicitudesAction");
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
					//Validación de Parametrización de Centros de Costos marcado como Registro de Respuesta Procedimiento X Terceros
					if(Utilidades.esCentroCostoRespuestaProcTercero(con,medico.getCodigoCentroCosto()).equals(ConstantesBD.acronimoSi))
					{
						logger.info("\n\nUsuario no valido, pero posee centro de Costos parametrizado como Registro de Respuesta Procedimiento X Terceros");										
					}
					else
					{
						if(UtilidadValidacion.estaMedicoInactivo(con,medico.getCodigoPersona(),medico.getCodigoInstitucionInt()))
							request.setAttribute("codigoDescripcionError", "errors.profesionalSaludInactivo");
						else
							request.setAttribute("codigoDescripcionError", "errors.usuario.noAutorizado");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaError");
					}
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
				if (estado.equals("empezar")) {
					logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
					logger.info("<< Estado: " + estado);
					return this.accionEmpezar(mapping,con,listarSolicitudesForm, request);
				} 

				else if (estado.equals("medico")||estado.equals("interconsultas")|| estado.equals("procedimientos"))
				{
					return this.accionGeneralSolicitudes(mapping,request, con,listarSolicitudesForm,false);
				} 

				//*******************************************************************************
				else if (estado.equals("paciente")) {
					boolean esResumenAtenciones=false;
					String esResumenAtencionesStr=request.getParameter("resumenAtenciones");

					if(esResumenAtencionesStr!=null) {
						//indicador que me ayuda a saber si el listado va a ser de resumen de atenciones
						listarSolicitudesForm.setIndicador("1");
						esResumenAtenciones=esResumenAtencionesStr.equals("true");
					}

					if(pacienteCargado)	{
						if(!this.epicrisis)	{

							//si no se trata de la epicrisis, debemos restringir que la cuenta del paciente este abierta
							//TAMBIEN VALIDAR EL ASOCIO
							if(this.personaCargada.getCodigoCuenta()==0 && !esResumenAtenciones && !(personaCargada.getExisteAsocio()&&this.tipoServicio.equals("modificar"))) {
								request.setAttribute("codigoDescripcionError",	"errors.paciente.cuentaNoAbierta");
								UtilidadBD.cerrarConexion(con);
								return mapping.findForward("paginaError");
							}

							else {
								//se trata del listado de epicrisis que no tiene en cuenta que la cuenta este abierta
							}

							if(this.personaCargada.getCodigoCuenta()==0 && personaCargada.getExisteAsocio() &&this.tipoServicio.equals("modificar")) {
								listarSolicitudesForm.setMostrarMensaje(true);
							}
						}
						return this.accionGeneralSolicitudes(mapping, request, con,listarSolicitudesForm, esResumenAtenciones);
					}

					else {
						UtilidadBD.closeConnection(con);
						request.setAttribute("codigoDescripcionError",	"errors.paciente.noCargado");
						return mapping.findForward("paginaError");
					}
				}
				//*******************************************************

				else if(estado.equals("resumen")) {
					return this.accionResumenSolicitudes(mapping,	request,	con,	listarSolicitudesForm);
				}
				else if(estado.equals("imprimir"))
				{
					return this.accionImprimir(con,listarSolicitudesForm.getNumeroSolicitud(), mapping, medico, personaCargada,request,institucionActual);
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
				else	if(estado.equals("guardar"))
				{
					return this.accionGuardarCambiosSolicitud(mapping,request,response,	con,	listarSolicitudesForm);
				}
				else if(estado.equals("guardarJustificacionDinamica"))
				{
					return this.accionGuardarJustificacionDinamica(listarSolicitudesForm, con);
				}
				else if(estado.equals("volverFiltroSolicitudes"))
				{
					listarSolicitudesForm.setFiltro("filtroRangoEjecutado");
					listarSolicitudesForm.setEstado("medico");
					return mapping.findForward("filtroSolicitudes"); 
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
			return null;
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
		//logger.info("Llega al action a insertar!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
	    //logger.info("Mapa----------->"+listarSolicitudesForm.getJustificacionDinamicaMap());
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
	

	/** pagina de resumen que se llama
	 * @param mapping
	 * @param request
	 * @param con
	 * @param listarSolicitudesForm
	 * @return */
	private ActionForward accionResumenSolicitudes(ActionMapping mapping, HttpServletRequest request, Connection con, ListarSolicitudesForm listarSolicitudesForm) throws SQLException {
		logger.info("\n entre a accionResumenSolicitudes ");
		//cargar paciente, aun si hay un paciente cargado
		//no podemos asegurar que es el mismo involucrado en la solicitud
		this.codigoPersona = listarSolicitudesForm.getCodigoPaciente();
		
		PersonaBasica pb=new PersonaBasica();
		pb.cargar(con,this.codigoPersona);
		pb.cargarPaciente2(con, this.codigoPersona,medico.getCodigoInstitucion(),medico.getCodigoCentroAtencion()+"");
		
		// Código necesario para registrar este paciente como Observer
		Observable observable = (Observable) this.servlet.getServletContext().getAttribute("observable");

		if (observable != null) {
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
		/*if(!vs.puedoModificarSolicitudInterpretada().isTrue()) {
			logger.info("linea 310 en if de !vs.puedoModificarSolicitudInterpretada().isTrue()");
			request.setAttribute("codigoDescripcionError",	"error.validacionessolicitud.interpretar.medicoNoSolicitante");
			return mapping.findForward("paginaError");
		}*/
		
		Solicitud s=new Solicitud();
		s.cargar(con,listarSolicitudesForm.getNumeroSolicitud());
			
		//logger.info("s.getCentroCostoSolicitado().getCodigo()        "+ s.getCentroCostoSolicitado().getCodigo());
		//logger.info(" s.getCentroCostoSolicitante().getCodigo())      " + s.getCentroCostoSolicitante().getCodigo());
			
		//-Cargar EL codigo
		listarSolicitudesForm.setCentroCostoSolicitante(s.getCentroCostoSolicitante().getCodigo()); 
			
		if(s.getTipoSolicitud().getCodigo()==ConstantesBD.codigoTipoSolicitudProcedimiento) {
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

		if(this.estadoHistoriaClinicaResumen.equals(String.valueOf(ConstantesBD.codigoEstadoHCSolicitada))) {
				
			if(vs.puedoResponder().isTrue())
				listarSolicitudesForm.setResponder(true);
				
			if(vs.puedoModificarSolicitudSolicitada().isTrue())
				listarSolicitudesForm.setModificar(true);
		}

		if(this.estadoHistoriaClinicaResumen.equals(String.valueOf(ConstantesBD.codigoEstadoHCRespondida))) {
			//logger.info("EN EL ACTION"+vs.puedoInterpretar().isTrue());
			if(vs.puedoInterpretar().isTrue())
				listarSolicitudesForm.setInterpretar(true);
				
				if(vs.puedoModificarSolicitudRespondida().isTrue())
					listarSolicitudesForm.setModificar(true);
		}

		if(this.estadoHistoriaClinicaResumen.equals(String.valueOf(ConstantesBD.codigoEstadoHCInterpretada))) {
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
			
		if(s.getTipoSolicitud().getCodigo()==ConstantesBD.codigoTipoSolicitudProcedimiento)	{
			//con el nuevo manejo de procedimientos se definió que no se pueden responder ni interpretar desde la consulta/modificacion, es decir desde el resumen.
			listarSolicitudesForm.setResponder(false);
			listarSolicitudesForm.setInterpretar(false);
			if(this.estadoHistoriaClinicaResumen.equals(String.valueOf(ConstantesBD.codigoEstadoHCSolicitada)))	{
				if(vs.puedoModificarSolicitudSolicitada().isTrue())
					listarSolicitudesForm.setModificar(true);
			}

			if(this.estadoHistoriaClinicaResumen.equals(String.valueOf(ConstantesBD.codigoEstadoHCTomaDeMuestra))) {
				//if(vs.puedoModificarSolicitudSolicitada().isTrue())
				listarSolicitudesForm.setModificar(true);
			}
			if(this.estadoHistoriaClinicaResumen.equals(String.valueOf(ConstantesBD.codigoEstadoHCRespondida)))	{
				listarSolicitudesForm.setInterpretar(false);
				listarSolicitudesForm.setModificar(false);
				listarSolicitudesForm.setResponder(false);
			}
		}
			
		UtilidadBD.closeConnection(con);
		
		logger.info("\n sali!!!!!!! a accionResumenSolicitudes ");
		return mapping.findForward("resumen");		
	}


	/**
	 * Generar Solicitudes en: ordenes > consultar/modificar > paciente
	 * @param mapping
	 * @param request
	 * @param con
	 * @param listarSolicitudesForm 
	 * @param esResumenAtenciones
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private ActionForward accionGeneralSolicitudes(	
			ActionMapping mapping,
			HttpServletRequest request,
			Connection con,
			ListarSolicitudesForm listarSolicitudesForm,
			boolean esResumenAtenciones) throws Exception 
	{
		
		//logger.info("LISTANDO LAS SOLICITUDES");
		Vector e = new Vector();
		Collection listado;
		Solicitudes s = new Solicitudes();		
				
		if (this.estado.equals("interconsultas")||this.estado.equals("procedimientos")) {
			logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			logger.info("<<accion GeneralSolicitudes, estado: " + this.estado);
			
			if(!listarSolicitudesForm.getFiltro().trim().equals("")) {
				if(listarSolicitudesForm.getFiltro().trim().equalsIgnoreCase("paciente")) {
					if(!pacienteCargado) {
						UtilidadBD.closeConnection(con);
						request.setAttribute("codigoDescripcionError",	"errors.paciente.noCargado");
						return mapping.findForward("paginaError");
					}
					else {
						s.setCodigoCuenta(personaCargada.getCodigoCuenta());
						if(personaCargada.getExisteAsocio()) {
							s.setCodigoCuentaasociada(this.personaCargada.getCodigoCuentaAsocio());
						}
					}
				}
				else if(listarSolicitudesForm.getFiltro().trim().equalsIgnoreCase("rango")) {
					listarSolicitudesForm.setFiltro("filtroRangoEjecutado");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("filtroSolicitudes");
				}
			}
			else {
				UtilidadBD.closeConnection(con);
				return mapping.findForward("solicitarFiltro");
			}
		}
		
		
		if(estado.equals("medico"))	
		{
			if(listarSolicitudesForm.getFiltro().trim().equalsIgnoreCase("filtroRangoEjecutado")) 
			{
				s.setFechaInicialFiltro(listarSolicitudesForm.getFechaInicialFiltro());
				s.setFechaFinalFiltro(listarSolicitudesForm.getFechaFinalFiltro());
				s.setCentroCostoSolicitanteFiltro(listarSolicitudesForm.getCentroCostosSolicitanteFiltro());
				s.setEstadoHCFiltro(listarSolicitudesForm.getEstadoHCFiltro());
				s.setTipoOrdenFiltro(listarSolicitudesForm.getTipoOrdenFiltro());
				s.setAreaFiltro(listarSolicitudesForm.getAreaFiltro());
				s.setCentroCostoSolicitadoFiltro(listarSolicitudesForm.getCentroCostoSolicitadoFiltro());
				s.setPisoFiltro(listarSolicitudesForm.getPisoFiltro());
				s.setHabitacionFiltro(listarSolicitudesForm.getHabitacionFiltro());
				s.setCamaFiltro(listarSolicitudesForm.getCamaFiltro());
				///////////////////////////////////////////////////////////////////////////////////
				//portatil
				s.setRequierePortatilFiltro(listarSolicitudesForm.isRequierePortatilFiltro());
				///////////////////////////////////////////////////////////////////////////////////
								
				//Si viene de un llamado no actualiza la cadena
				if(!this.tipoServicio.equals("modificarExterno"))	
				{
					//Cadena separada por separadorSplit usada para pasarla por parametro al llamado de otra funcionalidad y 
					//mantener la busqueda del listado
					listarSolicitudesForm.setCadenaFiltroBusqueda(
									s.getFechaInicialFiltro()
									+ConstantesBD.separadorSplit+
									s.getFechaFinalFiltro()
									+ConstantesBD.separadorSplit+
									s.getCentroCostoSolicitanteFiltro()
									+ConstantesBD.separadorSplit+
									s.getEstadoHCFiltro()
									+ConstantesBD.separadorSplit+
									s.getTipoOrdenFiltro()
									+ConstantesBD.separadorSplit+
									s.getAreaFiltro()
									+ConstantesBD.separadorSplit+
									s.getCentroCostoSolicitadoFiltro()
									+ConstantesBD.separadorSplit+
									s.getPisoFiltro()
									+ConstantesBD.separadorSplit+
									s.getHabitacionFiltro()
									+ConstantesBD.separadorSplit+
									s.getCamaFiltro()
									+ConstantesBD.separadorSplit+
									s.isRequierePortatilFiltro());			
					
					logger.info("\n\n\nvalor de la cadena ARMADA de busqueda >> "+listarSolicitudesForm.getCadenaFiltroBusqueda());
				}
			}
			else 
			{
				listarSolicitudesForm.setFiltro("filtroRangoEjecutado");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("filtroSolicitudes");
			}
		}
		
		if(listarSolicitudesForm.getIndicador()==null) {
			listarSolicitudesForm.setIndicador("0");
		}
		if(listarSolicitudesForm.getIndicador().equals("1")) {
			listarSolicitudesForm.reset();
			listarSolicitudesForm.setIndicador("1");
		}
		else {
			listarSolicitudesForm.reset();
			listarSolicitudesForm.setIndicador("0");
		}
		
	    //cargar medico
		Medico m = new Medico();
		m.init(System.getProperty("TIPOBD"));
		m.cargarMedico(con, this.medico.getCodigoPersona());
		
		//preparar objeto para consulta
		//medico.setOcupacionMedica()
		//logger.info("cc medico -->"+medico.getCodigoCentroCosto());
		//logger.info("om medico -->"+medico.getCodigoOcupacionMedica());
		//logger.info("tipo ser-->"+this.tipoServicio);
		if (this.tipoServicio.equals("responder")) {
			
			s.setEstadoHistoriaClinica(ConstantesBD.codigoEstadoHCSolicitada);
			if (this.estado.equals("interconsultas")) {
			   s.setCodigoTipoSolicitud(ConstantesBD.codigoTipoSolicitudInterconsulta);
			} 
			else if (this.estado.equals("procedimientos")) {
				s.setCodigoTipoSolicitud(ConstantesBD.codigoTipoSolicitudProcedimiento);
			}
			//responde solo las que pertenecen a su Especialidad, Ocupacion y CC
			s.setCodigosEspecialidadSolicitada(m.getEspecialidades());
			//logger.info("cc medico -->"+medico.getCodigoCentroCosto());
			//logger.info("om medico -->"+medico.getCodigoOcupacionMedica());
			
			s.setCodigoCentroCostoSolicitado(medico.getCodigoCentroCosto());
			s.setCodigoCentroCostoTratante(medico.getCodigoCentroCosto());
			s.setCodigoOcupacionSolicitada(medico.getCodigoOcupacionMedica());
		} 

		else if (this.tipoServicio.equals("interpretar")) {
			s.setCodigoCentroCostoSolicitante(medico.getCodigoCentroCosto());
			s.setEstadoHistoriaClinica(ConstantesBD.codigoEstadoHCRespondida);

			if (this.estado.equals("interconsultas")) {
				//logger.info("colocando tipo de solicitud interconsulta");
				s.setCodigoTipoSolicitud(ConstantesBD.codigoTipoSolicitudInterconsulta);
			} 
			else if (this.estado.equals("procedimientos")) {
				//logger.info("colocando tipo de solicitud procedimientos");
				s.setCodigoTipoSolicitud(ConstantesBD.codigoTipoSolicitudProcedimiento);
			}
		} 

		else if (this.tipoServicio.equals("ordenMedicaInter")) {
			s.setCodigoPaciente(this.personaCargada.getCodigoPersona());
			if(this.personaCargada.getExisteAsocio()) {
				s.setCodigoCuentaasociada(this.personaCargada.getCodigoCuentaAsocio());
			}
			else {
				s.setCodigoCuentaasociada(-1);
			}
			if(this.personaCargada.getCodigoCuenta()==0 && personaCargada.getExisteAsocio()) {
			    listarSolicitudesForm.setMostrarMensaje(true);
			    //el usuario no es del mismo centro de costo
			    //ya que el paciente no tiene cuenta abierta
			    listarSolicitudesForm.setMedicoMismoCentroCosto(false);
			}

			if(this.personaCargada.getCodigoCuenta()!=0 && personaCargada.getExisteAsocio()) {
			    if(medico.getCodigoCentroCosto()==ConstantesBD.codigoCentroCostoUrgencias) {
			        //ya que si existe asocio el centro de costo es urgencias.
			        listarSolicitudesForm.setMedicoMismoCentroCosto(false);
			    }
			}
			s.setCodigoTipoSolicitud(ConstantesBD.codigoTipoSolicitudInterconsulta);
			this.tipoServicio = "modificar"; 
		}

		else if (this.tipoServicio.equals("ordenMedicaProc")) {
			s.setCodigoPaciente(this.personaCargada.getCodigoPersona());
			if(this.personaCargada.getExisteAsocio()) {
				s.setCodigoCuentaasociada(this.personaCargada.getCodigoCuentaAsocio());
			}
			else {
				s.setCodigoCuentaasociada(-1);
			}

			if(this.personaCargada.getCodigoCuenta()==0 && personaCargada.getExisteAsocio()) {
			    listarSolicitudesForm.setMostrarMensaje(true);
			    //el usuario no es del mismo centro de costo
			    //ya que el paciente no tiene cuenta abierta
			    listarSolicitudesForm.setMedicoMismoCentroCosto(false);
			}

			if(this.personaCargada.getCodigoCuenta()!=0 && personaCargada.getExisteAsocio()) {
			    if(medico.getCodigoCentroCosto()==ConstantesBD.codigoCentroCostoUrgencias) {
			        //ya que si existe asocio el centro de costo es urgencias.
			        listarSolicitudesForm.setMedicoMismoCentroCosto(false);
			    }
			}
			s.setCodigoTipoSolicitud(ConstantesBD.codigoTipoSolicitudProcedimiento);
			this.tipoServicio = "modificar"; 
		}
		
		else if (this.tipoServicio.equals("ordenMedicaCirugia")) {
			s.setCodigoPaciente(this.personaCargada.getCodigoPersona());
			if(this.personaCargada.getExisteAsocio()) {
				s.setCodigoCuentaasociada(this.personaCargada.getCodigoCuentaAsocio());
			}
			else {
				s.setCodigoCuentaasociada(-1);
			}

			if(this.personaCargada.getCodigoCuenta()==0 && personaCargada.getExisteAsocio()) {
			    listarSolicitudesForm.setMostrarMensaje(true);
			    //el usuario no es del mismo centro de costo
			    //ya que el paciente no tiene cuenta abierta
			    listarSolicitudesForm.setMedicoMismoCentroCosto(false);
			}

			if(this.personaCargada.getCodigoCuenta()!=0 && personaCargada.getExisteAsocio()) {
			    if(medico.getCodigoCentroCosto()==ConstantesBD.codigoCentroCostoUrgencias) {
			        //ya que si existe asocio el centro de costo es urgencias.
			        listarSolicitudesForm.setMedicoMismoCentroCosto(false);
			    }
			}
			s.setCodigoTipoSolicitud(ConstantesBD.codigoTipoSolicitudCirugia);
			this.tipoServicio = "modificar"; 
		}
		
		else if (this.tipoServicio.equals("modificar") || 
				this.tipoServicio.equals("modificarExterno"))	
		{
			if(this.estado.equals("medico")
					&& this.tipoServicio.equals("modificarExterno"))
			{
				//Al realizar el llamado al listado desde otra funcionalidad se ha pasado los parametros de la busqueda
				if(!listarSolicitudesForm.getCadenaFiltroBusqueda().trim().equals(""))
				{
					//Actualiza los campos de los filtros para la busqueda
					logger.info("\n\n\nvalor de la cadena de busqueda >> "+listarSolicitudesForm.getCadenaFiltroBusqueda());
					s.actualizarCadenaFiltroBusqueda(listarSolicitudesForm.getCadenaFiltroBusqueda());
				}
				
				this.tipoServicio ="modificar";
				listarSolicitudesForm.setTipoServicio("modificar");
				
				/*
				 * TAREA id=46456 EN XPLANNER 2008
				 * s.setCodigoMedico(medico.getCodigoPersona());
				 * si es por rangos, no debe validar nada por defecto. 
				 * s.setCodigoCentroCostoSolicitante(medico.getCodigoCentroCosto());
				 */
			}
			else if(this.estado.equals("paciente"))	{
				if(pacienteCargado)	{
					s.setCodigoCuenta(personaCargada.getCodigoCuenta());
					if(personaCargada.getExisteAsocio()) {
						s.setCodigoCuentaasociada(this.personaCargada.getCodigoCuentaAsocio());
					}
				}
				
				s.setCodigoPaciente(this.personaCargada.getCodigoPersona());
				s.setResumenAtenciones(esResumenAtenciones);
				
				if(this.personaCargada.getExisteAsocio()) {
					s.setCodigoCuentaasociada(this.personaCargada.getCodigoCuentaAsocio());
				}
				else {
					s.setCodigoCuentaasociada(-1);
				}

				if(this.personaCargada.getCodigoCuenta()==0 && personaCargada.getExisteAsocio()) {
				    listarSolicitudesForm.setMostrarMensaje(true);
				    //el usuario no es del mismo centro de costo
				    //ya que el paciente no tiene cuenta abierta
				    listarSolicitudesForm.setMedicoMismoCentroCosto(false);
				}

				if(this.personaCargada.getCodigoCuenta()!=0 && personaCargada.getExisteAsocio()) {
				    if(medico.getCodigoCentroCosto()==ConstantesBD.codigoCentroCostoUrgencias) {
				        //ya que si existe asocio el centro de costo es urgencias.
				        listarSolicitudesForm.setMedicoMismoCentroCosto(false);
				    }
				}
				
				if(!this.epicrisis && !esResumenAtenciones)	{
					s.setEstadoCuenta(ConstantesBD.codigoEstadoCuentaActiva);
				}

				if(encabezadoResumen!=null && !encabezadoResumen.equals("")) {
					String[] resultado=encabezadoResumen.split("@");
					if(resultado.length>=2&&resultado[1].equals("inter")) {
						s.setCodigoTipoSolicitud(ConstantesBD.codigoTipoSolicitudInterconsulta);
					}
					if(resultado.length>=2&&resultado[1].equals("proc")) {
						s.setCodigoTipoSolicitud(ConstantesBD.codigoTipoSolicitudProcedimiento);
					}
					if(resultado.length>=3&&resultado[2]!=null && !resultado[2].equals(""))	{
						s.setCodigoCuenta(Integer.parseInt(resultado[2]));
					}
					else {
						s.setCodigoCuenta(0);
					}
				}
			}
			s.setCodigoCentroAtencionCuenta(medico.getCodigoCentroAtencion());
			//logger.info("VALIDAR EL LISTADO POR EN CENTRO DE ATENCION "+s.getCodigoCentroAtencionCuenta());
		}

		if(listarSolicitudesForm.getRestriccion()!=null) 
		{    
            if(listarSolicitudesForm.getRestriccion().equals("solicitudesCx"))
            {
                s.setCodigoTipoSolicitud(ConstantesBD.codigoTipoSolicitudCirugia);
                listarSolicitudesForm.setRestriccion("");
            }
        }
		
		listarSolicitudesForm.setListarPorMedico(this.estado.equals("medico")?true:false);
		s.setCodigoCentroCostoIntentaAcceso(medico.getCodigoCentroCosto());
		//realizar consulta
		s.listarSolicitudes(con, medico.getCodigoInstitucionInt());
		
		//crear listado
		listado = s.getListadoSolicitudes();
		//crear enlaces del listado
		
		listado=validacionListado(con, listado, this.tipoServicio);

		
		//añadirlo al form
		listarSolicitudesForm.setListaSolicitudes(new ArrayList(listado));
		
		/**
		 * MT 6825 Version Cambio 1.1.4
		 * @author leoquico
		 * @fecha 17/04/2013
		 */
		
		listarSolicitudesForm.setListaSolicitudes(Listado.ordenarColumna(new ArrayList(listarSolicitudesForm.getListaSolicitudes()),listarSolicitudesForm.getUltimaPropiedad(),"fechaSolicitudDate"));
		listarSolicitudesForm.setListaSolicitudes(Listado.ordenarColumna(new ArrayList(listarSolicitudesForm.getListaSolicitudes()),"fechaSolicitudDate","fechaSolicitudDate"));

		//añadir encabezado
		e = setInfoEncabezado(e);
		listarSolicitudesForm.setEncabezado(e);
		listarSolicitudesForm.setTipoServicio(this.tipoServicio);
		listarSolicitudesForm.setEpicrisis(this.epicrisis);
		
		//subir ultimo url busqueda a sesion
		request.getSession().setAttribute("ultimoListadoSolicitudes","../solicitudes/listarSolicitudes.do?estado="+this.estado+"&tipoServicio="+this.tipoServicio+"&recuperarListado=true");
		
		//-Se consultan los procedimientos e interconsultas asignadas al paciente por la solicitud correspondiente
		//--para poder mostrarlos en la ventana del listado de solicitudes procedimientos e interconsultas
		listarSolicitudesForm.setInterconsultasProcedimientosSolicitud(s.consultarInterconsultasProcedimientos(con, this.personaCargada.getCodigoCuenta()));
			

		listarSolicitudesForm.setEstado(this.estado);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listado");
	}

	/**
	 * @param listado de solicitudes resultado de la busqueda
	 * @return lista validada con las reglas de solicitudes y modificada para mostrar con el tag display:*
	 */
	private Collection validacionListado(Connection con, Collection listado, String tipoServicio) throws SQLException 
	{
		//logger.info("Listado");
		//añadir a listado definitivo
		ArrayList listadoTemporal= new ArrayList(listado);
		Collection c=new ArrayList();
		//	recorrer el listado
		//logger.info("TAMANIO DEL LISTADO -->"+listadoTemporal.size());
		for(int i=0;i<listadoTemporal.size();i++)
		{
			//validar condiciones
			Solicitud temp = (Solicitud)listadoTemporal.get(i);
			SolicitarForm sf=new SolicitarForm();
			sf.reset();

			logger.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++");
			logger.info("Fecha: " + temp.getFechaSolicitud() + " Long: " + temp.getFechaSolicitud().length());
			logger.info("Hora: " + temp.getHoraSolicitud() + " Long: " + temp.getHoraSolicitud().length());
			
			//sf.setFechaSolicitud(temp.getFechaSolicitud()+" "+temp.getHoraSolicitud().substring(0,5));
			sf.setFechaSolicitudDate(temp.getFechaSolicitudDate());
			sf.setFechaSolicitud(temp.getFechaSolicitud()+" "+temp.getHoraSolicitud());
			
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
			sf.setIncluyeServiciosArticulos(temp.getIncluyeServiciosArticulos());
			sf.setCodigoEstadoHistoriaClinica(temp.getEstadoHistoriaClinica().getCodigo());
			
			/////////////////////////////////////////////////////////////////////////////
			//portatil
			sf.setPortatil(temp.getPortatil());
			/////////////////////////////////////////////////////////////////////////////
			
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
						sf.setEnlace("listarSolicitudes.do?estado=resumen&tipoServicio="+this.tipoServicio+"&numeroSolicitud="+temp.getNumeroSolicitud()+"&codigoPaciente="+temp.getCodigoPaciente()+"&estadoHistoriaClinicaResumen="+temp.getEstadoHistoriaClinica().getCodigo()+"&codigoTipoSolicitud="+temp.getTipoSolicitud().getCodigo()+"&encabezadoResumen=false&imprimirPagina=preparar");
					else
						sf.setEnlace("listarSolicitudes.do?estado=resumen&tipoServicio="+this.tipoServicio+"&numeroSolicitud="+temp.getNumeroSolicitud()+"&codigoPaciente="+temp.getCodigoPaciente()+"&estadoHistoriaClinicaResumen="+temp.getEstadoHistoriaClinica().getCodigo()+"&codigoTipoSolicitud="+temp.getTipoSolicitud().getCodigo()+"&encabezadoResumen=false");
				}
				else
				{
					sf.setEnlace("listarSolicitudes.do?estado=resumen&tipoServicio="+this.tipoServicio+"&numeroSolicitud="+temp.getNumeroSolicitud()+"&codigoPaciente="+temp.getCodigoPaciente()+"&estadoHistoriaClinicaResumen="+temp.getEstadoHistoriaClinica().getCodigo()+"&codigoTipoSolicitud="+temp.getTipoSolicitud().getCodigo());
				}
			}
			else
			{
				sf.setEnlace("listarSolicitudes.do?estado=resumen&tipoServicio="+this.tipoServicio+"&numeroSolicitud="+temp.getNumeroSolicitud()+"&codigoPaciente="+temp.getCodigoPaciente()+"&estadoHistoriaClinicaResumen="+temp.getEstadoHistoriaClinica().getCodigo()+"&codigoTipoSolicitud="+temp.getTipoSolicitud().getCodigo());
			}
			c.add(sf);
		}
		return c;
	}

	/**
	 * @param e
	 * @return vector con la estructura de encabezado de la lista dadas las condiciones de cada accion de solicitudes
	 */
	private Vector setInfoEncabezado(Vector e) 
	{
		logger.info("\n\n \n ENTRO A  ENCABEZADO ACTION............ >>>>>>>>>>>>>>>>> "+this.tipoServicio );
		//inicializar cada elemento
		if (this.tipoServicio.equals("responder")) 
		{
			logger.info("Entró al estado RESPONDER del Action.. >> "+this.tipoServicio );
			e.add(new EncabezadoLista("labelUrgente", "Urg.", true, true, false));
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
			//e.add(new EncabezadoLista("enlace", "Detalle", true, true, true));
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
				e.add(new EncabezadoLista("incluyeServiciosArticulos", "Incl.", true, true, false));
			}
			else
			{
				//cambiamos las columnas para la epicrisis
				e.add(new EncabezadoLista("fechaSolicitud","Fecha Solicitud",	false,true,false));
				if(this.estado.equals("medico"))
					e.add(new EncabezadoLista("nombrePaciente", "Paciente", false, true, false));
				e.add(new EncabezadoLista("nombreEspecialidadSolicitada","Especialidad",true,true,false));
				e.add(new EncabezadoLista("consecutivoOrdenesMedicas","Orden Nº",false,true,false));
				//e.add(new EncabezadoLista("enlace", "Detalle", false, true, false));
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
	private ActionForward accionEmpezar(ActionMapping mapping, Connection con, ListarSolicitudesForm listarSolicitudesForm, HttpServletRequest request) throws Exception {

		//Se limpian los datos anteriores
		listarSolicitudesForm.setFiltro("");
		listarSolicitudesForm.setCentroCostosSolicitanteFiltro("");
		String fechaActual=UtilidadFecha.getFechaActual();
		listarSolicitudesForm.setFechaInicialFiltro(UtilidadFecha.incrementarDiasAFecha(fechaActual, -1, false));
		listarSolicitudesForm.setFechaFinalFiltro(fechaActual);
		listarSolicitudesForm.reset();

		//	indicador que me ayuda a saber que se va a listar interconsultas
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
		UtilidadBD.closeConnection(con);
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
		
		//logger.info("this.tipoModificacion "+this.tipoModificacion);
		
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
		    response.sendRedirect("../procedimiento/procedimiento.do?estado=empezar&numeroSolicitud="+numeroSolicitud+"&ind="+listarSolicitudesForm.getInd()+"&vieneDePyp="+listarSolicitudesForm.isVieneDePyp());
		    listarSolicitudesForm.setVieneDePyp(false);
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
	private ActionForward accionOrdenar(	ActionMapping mapping,
	        												Connection con,
	        												ListarSolicitudesForm listarSolicitudesForm)throws Exception	
    {
		
	        //*******************Mt 6791*************************	
		    if(listarSolicitudesForm.getColumna().equals("fechaSolicitud")){
		    	
		    	listarSolicitudesForm.setColumna("fechaSolicitudDate");
		    }
		 		    
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
			//logger.info("Accion de imprimir");
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
			//logger.info("Impresion de respuesta");
			RespuestaProcedimientoPdf.pdfImprimirRespuesta(con, ValoresPorDefecto.getFilePath() +nombreArchivo, solicitud, medico, pacienteActivo, procedimiento);
		}
		else
		{
			//logger.info("Impresion de externo 2");
			ResumenGuardarProcedimientoPdf.pdfImprimirExterno2(con, ValoresPorDefecto.getFilePath() +nombreArchivo, solicitud,usu, pacienteActivo, solicitarForm,listarSolicitudesForm);
		}
		request.setAttribute("nombreArchivo", nombreArchivo);
		request.setAttribute("nombreVentana", "Procedimientos Externos");
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("abrirPdf");
	
	}
}
