/*
* @(#)ReprogramarCitaAction.java
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
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
import util.ConstantesValoresPorDefecto;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.consultaExterna.UtilidadesConsultaExterna;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.agenda.ReprogramarCitaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.Agenda;
import com.princetonsa.mundo.agenda.Cita;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.facturacion.CondicionesXServicios;
import com.princetonsa.mundo.parametrizacion.RegistroUnidadesConsulta;

/**
* Action, controla todas las opciones dentro de la reprogramación de la cita, incluyendo los
* posibles casos de error, los casos de flujo.
*
* @version 1.0, Abr 05, 2004
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*
* @see org.apache.struts.action.ActionForm#validate(ActionMapping, HttpServletRequest)
*/
public class ReprogramarCitaAction extends Action
{
	/** Objeto que permite el manejo de archivos de registro */
	private Logger logger=Logger.getLogger(ReprogramarCitaAction.class);

	/** Definición de constantes del estado del flujo */
	private static final int ii_ESTADO_INVALIDO				= -1;
	private static final int ii_A_REPROGRAMAR_CITA			= 1;
	private static final int ii_BUSCAR_AGENDA				= 2;
	private static final int ii_CANCELAR_REPROGRAMACION		= 3;
	private static final int ii_CONFIRMAR_REPROGRAMACION	= 4;
	private static final int ii_LISTAR_AGENDA				= 5;
	private static final int ii_LISTAR						= 6;
	private static final int ii_PREPARAR_LISTADO			= 7;
	private static final int ii_PREPARAR_REPROGRAMAR_CITA	= 8;
	private static final int ii_REPROGRAMAR					= 9;
	private static final int ii_REPROGRAMAR_CITA			= 10;
	private static final int ii_RESTAURAR_CITA				= 11;
	private static final int ii_CAMBIAR_CENTRO_ATENCION = 12;
	private static final int ii_CAMBIAR_CENTRO_ATENCION_BUSQUEDA = 13;
	private static final int ii_CONSULTA_CONDICIONES_SERVICIO = 14;
	private static final int ii_ADICIONAR_SERVICIO = 15;
	private static final int ii_ELIMINAR_SERVICIO = 16;
	private static final int ii_LISTAR_CITAS_INCUMPLIDAS = 17; //listado de citas incumplida
	private static final int ii_REQUIERE_AUTORIZACION = 18 ; // en caso de que requiera autorizacion pregunta
	private static final int ii_AUTORIZACION_CITA = 19; // autorizacion de cita
	private static final int ii_GUARDAR_AUTORIZACION_CITA = 20; // guardar autorizacion de cita


	/**
	* Abre una conexión a una fuente de datos
	* ab_iniciarTransaccion indicador de si la conexión debe o no iniciar una transacción
	* @throws SQLException
	*/
	private Connection abrirConexion(boolean iniciarTransaccion)
	{
		Connection	con;
		DaoFactory	daoFactory;

		/* Obtener una conexión a la fuente de datos */
		try
		{
			daoFactory	= DaoFactory.getDaoFactory(System.getProperty("TIPOBD") );
			con	= daoFactory.getConnection();

			/* Iniciar una transacción si es necesario */
			if(iniciarTransaccion)
			{
				if(!daoFactory.beginTransaction(con) )
					con = null;
			}
		}
		catch(SQLException e)
		{
			if(logger.isDebugEnabled() )
				logger.debug("No se pudo obtener conexión a la fuente de datos");

			con = null;
		}

		return con;
	}

	private ActionForward buscarAgenda(
		ActionMapping		aam_mapping,
		ReprogramarCitaForm	arcf_form,
		HttpServletRequest	ahsr_request,
		Connection lc_con, UsuarioBasico usuario
	)throws Exception
	{
		
		/* Obtener datos del item seleccionado */
		String puedoModificarServicios = arcf_form.getItemSeleccionado("puedoModificarServicios_" + arcf_form.getIndiceItemSeleccionado()).toString(); 
		String unidadConsulta=(String)arcf_form.getItemSeleccionado("nombreUnidadAgenda_" + arcf_form.getIndiceItemSeleccionado());
		
		// Se asignan las variables correspondientes si la cita esta relacionada con una orden ambulatoria
		if(arcf_form.getItemsSeleccionados().containsKey("ordenAmb_"+arcf_form.getIndiceItemSeleccionado()) && !arcf_form.getItemSeleccionado("ordenAmb_"+arcf_form.getIndiceItemSeleccionado()).toString().equals("")){
			arcf_form.setOrdenAmbulatoria(arcf_form.getItemSeleccionado("ordenAmb_"+arcf_form.getIndiceItemSeleccionado())+"");
			arcf_form.setServicioOrdenAmbulatoria(arcf_form.getItemSeleccionado("servOrdenAmb_"+arcf_form.getIndiceItemSeleccionado())+"");
		} else {
			arcf_form.setOrdenAmbulatoria("");
			arcf_form.setServicioOrdenAmbulatoria("");
		}
		
		arcf_form.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuarioXServicio(lc_con, usuario.getLoginUsuario(), usuario.getCodigoCentroAtencion(), ConstantesBD.codigoActividadAutorizadaReprogramarCitas, "", arcf_form.getServicioOrdenAmbulatoria()));
			
		arcf_form.setEstado("listarAgenda");
		
		ahsr_request.setAttribute("puedoModificarServicios", puedoModificarServicios);
		ahsr_request.setAttribute("nombreUnidadAgenda",unidadConsulta);

		arcf_form.setMostrarSoloDisponibles(ConstantesBD.acronimoSi);
		UtilidadBD.cerrarConexion(lc_con);
		return aam_mapping.findForward("buscarAgenda");
	}

	/**
	* Cerrar una conexión con una fuente de datos
	* @param ac_con Conexión con la fuente de datos a cerrar
	* @throws SQLException
	*/
	private void cerrarConexion(Connection ac_con)throws SQLException
	{
		if(ac_con != null && !ac_con.isClosed() )
            UtilidadBD.closeConnection(ac_con);
	}

	/** Retorna la página de error, con un mensaje para el usuario */
	private ActionForward error(
		ActionMapping		aam_mapping,
		HttpServletRequest	ahsr_request,
		String				ls_error,
		boolean				lb_parametrizado
	)
	{
		/* Determinar que como se debe visualizar el error */
		ahsr_request.setAttribute(lb_parametrizado ? "codigoDescripcionError" : "descripcionError",ls_error);

		/* Obtener la página de errores */
		return aam_mapping.findForward("paginaError");
	}

	/** Controla el flujo principal de la reprogramación de citas */
	public ActionForward execute(
		ActionMapping		mapping,
		ActionForm			aaf_form,
		HttpServletRequest	request,
		HttpServletResponse	ahsr_response
	)throws Exception
	{
		Connection con=null;

		try {

		/* Validar que el tipo de formulario recibido sea el esperado */
		if(aaf_form instanceof ReprogramarCitaForm)
		{
			ReprogramarCitaForm	form;
			int					li_estado;
			String				estado;

			/* Obtener el estado actual */
			form = (ReprogramarCitaForm)aaf_form;
			estado = form.getEstado();
				con = abrirConexion(true);
			HttpSession session = request.getSession();
			UsuarioBasico medico =(UsuarioBasico) session.getAttribute("usuarioBasico");
			PersonaBasica paciente =(PersonaBasica) session.getAttribute("pacienteActivo");
			logger.warn("[ReprogramarCitaAction] estado: "+estado);
			/* Validar el estado actual del flujo */
			if(estado == null)
				li_estado = ii_ESTADO_INVALIDO;
			else if(estado.equals("aReprogramarCita") )
				li_estado = ii_A_REPROGRAMAR_CITA;
			else if(estado.equals("buscarAgenda") )
				li_estado = ii_BUSCAR_AGENDA;
			else if(estado.equals("cancelarReprogramacion") )
				li_estado = ii_CANCELAR_REPROGRAMACION;
			else if(estado.equals("confirmarReprogramacion") )
				li_estado = ii_CONFIRMAR_REPROGRAMACION;
			else if(estado.equals("listarAgenda") )
				li_estado = ii_LISTAR_AGENDA;
			else if(estado.equals("listar") )
				li_estado = ii_LISTAR;
			else if(estado.equals("prepararListado") )
				li_estado = ii_PREPARAR_LISTADO;
			else if(estado.equals("prepararReprogramarCita") )
				li_estado = ii_PREPARAR_REPROGRAMAR_CITA;
			else if(estado.equals("reprogramar") )
				li_estado = ii_REPROGRAMAR;
			else if(estado.equals("reprogramarCita") )
				li_estado = ii_REPROGRAMAR_CITA;
			else if(estado.equals("restaurarCita") )
				li_estado = ii_RESTAURAR_CITA;
			else if(estado.equals("cambiarCentroAtencion"))
				li_estado = ii_CAMBIAR_CENTRO_ATENCION;
			else if(estado.equals("cambiarCentroAtencionBusqueda"))
				li_estado = ii_CAMBIAR_CENTRO_ATENCION_BUSQUEDA;
			else if(estado.equals("consultarCondicionesServicio"))
				li_estado = ii_CONSULTA_CONDICIONES_SERVICIO;
			else if(estado.equals("adicionarServicio"))
				li_estado = ii_ADICIONAR_SERVICIO;
			else if(estado.equals("eliminarServicio"))
				li_estado = ii_ELIMINAR_SERVICIO;
			else if(estado.equals("listarCitasIncumplidas"))
				li_estado = ii_LISTAR_CITAS_INCUMPLIDAS;
			else if(estado.equals("requiereAutorizacion"))
				li_estado = ii_REQUIERE_AUTORIZACION;
			else if(estado.equals("autorizacionCita"))
				li_estado = ii_AUTORIZACION_CITA;
			else if(estado.equals("guardarAutorizacionCita"))
				li_estado = ii_GUARDAR_AUTORIZACION_CITA;
			else if (estado.equals("redireccion"))
			{
				UtilidadBD.closeConnection(con);
				ahsr_response.sendRedirect(form.getLinkSiguiente());
				return null;
			}
			else
				li_estado = ii_ESTADO_INVALIDO;

			switch(li_estado)
			{
				case ii_ESTADO_INVALIDO:
					/* El estado actual es intederminado y por lo tanto no válido */
					if(logger.isDebugEnabled() )
						logger.debug("Estado inválido en el flujo de agenda de consultas");

					return error(mapping, request, "errors.estadoInvalido", true);

				case ii_A_REPROGRAMAR_CITA:
					/**
					 * Validar concurrencia
					 * Si ya está en proceso de distribucion, no debe dejar entrar
					 **/
					if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), medico.getLoginUsuario()) )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
					}
					/**
					 * Validar concurrencia
					 * Si ya está en proceso de facturación, no debe dejar entrar
					 **/
					if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
					{
						return ComunAction.accionSalirCasoError(mapping,request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
					}
					
					
					/* Elimina la información de la agenda asociada a la cita */
					form.aReprogramarCita();
					//form.restaurarCita();
					form.setEstado("listar");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("reprogramarCita");

				case ii_BUSCAR_AGENDA:
					return buscarAgenda(mapping, form, request, con, medico);

				case ii_CANCELAR_REPROGRAMACION:
					/* Cancela el proceso de reprogramación de citas */
					form.setEstado("listar");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("reprogramarCita");

				case ii_CONFIRMAR_REPROGRAMACION:
					/*
					 * 
						Confirma la reprogramación de una cita cuando el paciente ya tiene
						registrada citas para la misma fecha
					*/
					form.setItemSeleccionado("validarCitaFecha_" + form.getIndiceItemSeleccionado(),new Boolean(false));
					return reprogramar(mapping, form, request, con);

				case ii_LISTAR:
					/* Listar las citas del paciente en estado válido para reprogramación */
						return listarCitas(mapping, form, request, con, medico);

				case ii_LISTAR_AGENDA:
					/* Listar la agenda disponible para el paciente */
					return listarAgenda(mapping, form, request, con);

				case ii_PREPARAR_LISTADO:
					/**
					 * Validar concurrencia
					 * Si ya está en proceso de facturación, no debe dejar entrar
					 **/
					if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), medico.getLoginUsuario()) )
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
					}
					else
					{
						/* Prepara el listado de las citas disponibles para reprogramación */
						return iniciar(mapping, form, request, con);
					}

				case ii_PREPARAR_REPROGRAMAR_CITA:
					UtilidadBD.cerrarConexion(con);
					/* Preara el formulario para abrir el listado de agenda disponible */
					
					
					return mapping.findForward("reprogramarCita");

				case ii_REPROGRAMAR:
					/* Reprograma las citas con estados de reprogramación validos */
					return reprogramar(mapping, form, request, con);

				case ii_REPROGRAMAR_CITA:
					/*
						Reemplaza la información de la agenda asociada a la cita  por la de otra
						agenda
					*/					
					form.reprogramarCita(con,medico);					
					//form.setEstado("listar");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("reprogramarCita");

				case ii_RESTAURAR_CITA:
					/* Elimina la información de la agenda asociada a la cita */
					form.restaurarCita();
					form.setEstado("listar");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("reprogramarCita");
				case ii_CAMBIAR_CENTRO_ATENCION:
					logger.info("--------------1");
					if(form.getCentroAtencion()!=ConstantesBD.codigoNuncaValido){
						logger.info("--------------2");
						form.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, medico.getLoginUsuario(), form.getCentroAtencion(), ConstantesBD.codigoActividadAutorizadaReprogramarCitas,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));
					}
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("reprogramarCita");
				case ii_CAMBIAR_CENTRO_ATENCION_BUSQUEDA:
					
					String	ls_estadoLiquidacion;
					/* Obtener datos del item seleccionado */
					ls_estadoLiquidacion =(String)form.getItemSeleccionado("codigoEstadoLiquidacion_" + form.getIndiceItemSeleccionado());
					//String unidadConsulta=(String)form.getItemSeleccionado("nombreUnidadConsulta_" + form.getIndiceItemSeleccionado());
					form.setEstado("listarAgenda");
					request.setAttribute("codigoEstadoLiquidacion", ls_estadoLiquidacion);
					//request.setAttribute("nombreUnidadConsulta",unidadConsulta);
					
					if(form.getCentroAtencion()!=ConstantesBD.codigoNuncaValido){
						form.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, medico.getLoginUsuario(), form.getCentroAtencion(), ConstantesBD.codigoActividadAutorizadaReprogramarCitas,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));
					}

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("buscarAgenda");
				case ii_CONSULTA_CONDICIONES_SERVICIO:
					return accionConsultaCondicionesServicio(con,form,mapping,medico);
				case ii_ADICIONAR_SERVICIO:
					return accionAdicionarServicio(con,form,mapping,medico,paciente);
				case ii_ELIMINAR_SERVICIO:
					return accionEliminarServicio(con,form,ahsr_response);
				case ii_LISTAR_CITAS_INCUMPLIDAS:
					// listar las citas incumplidas
					return mapping.findForward("listarCitasIncumplidas");
				case ii_REQUIERE_AUTORIZACION:
					// verificar si se requiere autorizacion de la cita
					return verificarAutoUsurio(con, form,medico,request,mapping);
				case ii_AUTORIZACION_CITA:
					// despliege del formulario de autorizacion de cita
					form.setMostrarFormAuto(ConstantesBD.acronimoNo);
					return mapping.findForward("autorizacionCita");
				case ii_GUARDAR_AUTORIZACION_CITA:
					// guardar la autorizacion de la ciata
					return guardarMotivoAutorizacion(con, form, mapping,request);
			}
		}

		/* El formulario recibido no corresponde a un formulario de agenda de consultas */
		if(logger.isDebugEnabled() )
			logger.debug("El formulario actual no corresponde con el formulario esperado por " +
				"reprogramacion de citas"
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
	 * Motodo que elimina un servicio de la cita
	 * @param con
	 * @param form
	 * @param response
	 * @return
	 */
	private ActionForward accionEliminarServicio(Connection con, ReprogramarCitaForm form, HttpServletResponse response) 
	{
		int i = form.getPosCita();
		int j = form.getPosServicio();
		
		//se elimina el servicio del mapa
		if(form.getItemSeleccionado("estadoServicio_"+i+"_"+j)!=null)
			form.setItemSeleccionado("estadoServicio_"+i+"_"+j, ConstantesIntegridadDominio.acronimoEstadoAnulado);
		else
			form.getItemsSeleccionados().remove("codigoServicio_"+i+"_"+j);
			
		
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
	        response.getWriter().write("<respuesta>");
	        response.getWriter().write("<pos-cita>"+form.getPosCita()+"</pos-cita>");
	        response.getWriter().write("<pos-servicio>"+form.getPosServicio()+"</pos-servicio>");
	        response.getWriter().write("</respuesta>");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionEliminarServicio: "+e);
		}
		return null;
	}

	/**
	 * Motodo que realiza la busqueda de los servgicios de una nunidad de consulta para aoadirlo a una cita
	 * @param con
	 * @param form
	 * @param mapping
	 * @param medico 
	 * @return
	 */
	private ActionForward accionAdicionarServicio(Connection con, ReprogramarCitaForm form, ActionMapping mapping, UsuarioBasico medico,PersonaBasica paciente) 
	{
		if(paciente.getCodigoPersona() > 0)
		{
			//Se consultan los servicios de la unidad de consulta seleccionada	
			form.setMapaServicios(RegistroUnidadesConsulta.obtenerServiciosUnidadConsulta(
				con, 
				form.getCodigoUnidadAgendaTemp(), 
				//VERIFICACION DEL FLUJO DE ORDENES AMBULATORIAS
				"",
				medico.getCodigoInstitucionInt(),
				true,
				paciente.getCodigoPersona()));
		}
		else
		{
			//Se consultan los servicios de la unidad de consulta seleccionada	
			form.setMapaServicios(RegistroUnidadesConsulta.obtenerServiciosUnidadConsulta(
				con, 
				form.getCodigoUnidadAgendaTemp(), 
				//VERIFICACION DEL FLUJO DE ORDENES AMBULATORIAS
				"",
				medico.getCodigoInstitucionInt(),
				false,
				ConstantesBD.codigoNuncaValido));
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaServicios");
	}

	/**
	 * Motodo implementado para consultar las condiciones de la toma del servicio
	 * @param con
	 * @param form
	 * @param mapping
	 * @param medico 
	 * @return
	 */
	private ActionForward accionConsultaCondicionesServicio(Connection con, ReprogramarCitaForm form, ActionMapping mapping, UsuarioBasico medico) 
	{
		//se toma el servicio al cual se le desea consultar las condiciones para la toma
		int codigoServicio = form.getCodigoServicio();
		
		//Se consultan las condiciones para la toma del servicio
		form.setCondicionesToma(CondicionesXServicios.obtenerCondicionesTomaXServicio(con, codigoServicio, medico.getCodigoInstitucionInt()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("condicionesServicio");
	}

	/** Inicia la forma de reprogramacion de citas y establece su estado de flujo*/
	private ActionForward iniciar(
		ActionMapping		aam_mapping,
		ReprogramarCitaForm	arcf_form,
		HttpServletRequest	ahsr_request,
		Connection lc_con
	)throws Exception
	{
		ActionForward	laf_validar;
		int				li_paciente;
		PersonaBasica	lpb_paciente;
		
		UsuarioBasico usuario=(UsuarioBasico)ahsr_request.getSession().getAttribute("usuarioBasico");

		/* Iniciar el flujo de reprogramacio de citas */
		arcf_form.reset();
		
		//Obtener centros de atencin validos para el usuario
		arcf_form.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(lc_con, usuario.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaReprogramarCitas));
		arcf_form.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(lc_con, usuario.getLoginUsuario(), usuario.getCodigoCentroAtencion(), ConstantesBD.codigoActividadAutorizadaReprogramarCitas,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));
		
		/* Obtener el paciente actual sesin */
		lpb_paciente	= (PersonaBasica)ahsr_request.getSession().getAttribute("pacienteActivo");
		/* Validar que sobre el paciente se puedan hacer reprogramaciones de cita */
		laf_validar = validarPaciente(aam_mapping, ahsr_request, lpb_paciente,usuario);
		
		//-------------------------------------------
		// verificar si el paciente posee una cita inc
		String [] parametrosModulo = {
				ConstantesValoresPorDefecto.nombreInstitucionManejaMultasPorIncumplimiento,
				ConstantesValoresPorDefecto.nombreBloqueaCitasReservaAsignReprogPorIncump,
				ConstantesValoresPorDefecto.nombreFechaInicioControlMultasIncumplimientoCitas
				};
		
		if(validarEstadoCitasPaciente(lc_con, lpb_paciente, usuario,ConstantesBD.codigoModuloConsultaExterna, parametrosModulo, arcf_form, ahsr_request))
			arcf_form.setCitasIncumpl(ConstantesBD.acronimoSi);
		else
			arcf_form.setCitasIncumpl(ConstantesBD.acronimoNo);
		//-------------------------------------------
		
		
		//-----Se asigna el centro de atencin del usuario por defecto cuando empieza ---------//
		arcf_form.setCentroAtencion(usuario.getCodigoCentroAtencion());

		if(laf_validar != null)
		{
			cerrarConexion(lc_con);
			return laf_validar;
		}
		
		if( (li_paciente = lpb_paciente.getCodigoPersona() ) > 0)
		{
			/* Obtener el codigo del paciente */
			arcf_form.setCodigoPaciente(li_paciente);

			/* Obtener la cuenta del paciente */
			arcf_form.setCuentaPaciente(lpb_paciente.getCodigoCuenta() );

			/* Establecer el codigo del sexo del paciente */
			arcf_form.setCodigoSexoPaciente(lpb_paciente.getCodigoSexo() );
		}
		cerrarConexion(lc_con);
		return aam_mapping.findForward("reprogramarCita");
	}

	/** Lista la agenda de consulta disponible para reprogramacion de citas */
	private ActionForward listarAgenda(
		ActionMapping		mapping,
		ReprogramarCitaForm	form,
		HttpServletRequest	request,
		Connection con
	)throws Exception
	{
		ActionErrors		errores;
		Date				ld_fechaInicio;
		Date				ld_fechaFin;
		SimpleDateFormat	lsdf_sdfFecha;

		errores = new ActionErrors();

		UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

		/* Iniciar variables. El formato de fecha que se espera es dd/MM/yyyy */
		lsdf_sdfFecha	= new SimpleDateFormat("dd/MM/yyyy");
		ld_fechaInicio	= null;
		ld_fechaFin		= null;;

		logger.info("\nPASA POR AK! ------------\n");
		logger.info("centro atencion --------- "+form.getCentroAtencion());
		logger.info("unidad de agenda  --------- "+form.getCodigoUnidadConsulta());
		
		/* Exije una interpretacion estricta del formato de fecha esperado */
		lsdf_sdfFecha.setLenient(false);
		
		// Capturar los centros de atencion si se ha seleccionado la opcion todos
		if (form.getCentroAtencion()==ConstantesBD.codigoNuncaValido){
			form.setCentrosAtencion(form.getCentrosAtencionAutorizados("todos").toString());
		}
		
		// Capturar las unidades de agenda si se ha seleccionado la opcion todos
		if (form.getCodigoUnidadConsulta()==ConstantesBD.codigoNuncaValido){
			form.setUnidadesAgenda(form.getUnidadesAgendaAutorizadas("todos").toString());
		}

		if(!form.getFechaInicioAgenda().equals("") )
		{
			try
			{
				Calendar lc_cal;

				/* Obtener la fecha de inicio */
				ld_fechaInicio = lsdf_sdfFecha.parse(form.getFechaInicioAgenda() );

				lc_cal = new GregorianCalendar();
				lc_cal.set(Calendar.HOUR_OF_DAY, 0);
				lc_cal.set(Calendar.MILLISECOND, 0);
				lc_cal.set(Calendar.MINUTE, 0);
				lc_cal.set(Calendar.SECOND, 0);

				/*
					Verificar que el listado de agenda solo traiga otems cuya fecha sea mayor o
					igual a la fecha actual
				*/
				if(lc_cal.getTime().after(ld_fechaInicio) )
					errores.add(
						"fechaInicioAgenda",
						new ActionMessage(
							"errors.fechaAnteriorAOtraDeReferencia", "Inicial", "actual"
						)
					);
			}
			catch(ParseException lpe_fechaInicio)
			{
				errores.add(
					"fechaInicioAgenda", new ActionMessage("errors.formatoFechaInvalido", "Inicial")
				);
				ld_fechaInicio = null;
			}
		}
		else
		{
			errores.add(
				"fechaInicioAgenda", new ActionMessage("errors.required", "Fecha Inicial")
			);
		}

		if(!form.getFechaFinAgenda().equals("") )
		{
			try
			{
				/* Obtener la fecha de finalizacion */
				ld_fechaFin = lsdf_sdfFecha.parse(form.getFechaFinAgenda() );
			}
			catch(ParseException lpe_fechaFin)
			{
				errores.add(
					"fechaFinAgenda", new ActionMessage("errors.formatoFechaInvalido", "Final")
				);
				ld_fechaFin = null;
			}
		}
		else
		{
			errores.add( "fechaFinAgenda", new ActionMessage("errors.required", "Fecha Final") );
		}

		if(ld_fechaFin != null && ld_fechaInicio != null && ld_fechaFin.before(ld_fechaInicio) )
		{
			/* La fecha de inicio debe ser anterior a la fecha de finalizacion */
			errores.add(
				"fechaFinAgenda",
				new ActionMessage(
					"errors.fechaAnteriorAOtraDeReferencia", "Fecha Final", "Fecha Inicial"
				)
			);
		} 

		/* Validar esto so y solo si no se han encontrado errores */
		if(!form.getHoraInicioAgenda().equals("") || !form.getHoraFinAgenda().equals("") )
		{
			Date				ld_horaInicio;
			Date				ld_horaFin;
			SimpleDateFormat	lsdf_sdfHora;

			/* Iniciar variables. El formato de hora que se espera es HH:mm en 24 horas */
			lsdf_sdfHora	= new SimpleDateFormat("H:mm");
			ld_horaInicio	= null;
			ld_horaFin		= null;

			/* Exije una interpretacion estricta del formato de hora esperado */
			lsdf_sdfHora.setLenient(false);

			if(!form.getHoraInicioAgenda().equals("") )
			{
				try
				{
					/* Obtener la hora de inicio */
					ld_horaInicio = lsdf_sdfHora.parse(form.getHoraInicioAgenda() );
				}
				catch(ParseException lpe_horaInicio)
				{
					errores.add(
						"horaInicioAgenda",
						new ActionMessage("errors.formatoHoraInvalido", "de inicio")
					);
					ld_horaInicio = null;
				}
			}

			if(!form.getHoraFinAgenda().equals("") )
			{
				try
				{
					/* Obtener la hora de finalizacion */
					ld_horaFin = lsdf_sdfHora.parse(form.getHoraFinAgenda() );
				}
				catch(ParseException lpe_horaFin)
				{
					errores.add(
						"horaFinAgenda",
						new ActionMessage("errors.formatoHoraInvalido", "de finalizacion")
					);
					ld_horaFin = null;
				}
			}

			/* La hora de inicio debe ser anterior a la hora de finalizacion */
			if(ld_horaFin != null && ld_horaInicio != null && ld_horaFin.before(ld_horaInicio) )
			errores.add(
					"horaFinAgenda",
					new ActionMessage(
						"errors.fechaAnteriorAOtraDeReferencia", "final", "inicial"
					)
				);
		}
		
		//----------Se verifica que la fecha final no sea mayor 3 meses a la fecha actual ----------//
		if(ld_fechaFin!=null)
		{
			int nroMeses = UtilidadFecha.numeroMesesEntreFechas(UtilidadFecha.getFechaActual(), form.getFechaFinAgenda(),true);

			if (nroMeses > 3)
			{
				errores.add("rango agenda mayor", new ActionMessage("error.agenda.rangoMayorTresMeses", "REPROGRAMAR CITAS"));
			}
		}
		
		if(!errores.isEmpty() )
		{
			if( (con = abrirConexion(false) ) == null)
				return error(mapping, request, "errors.problemasBd", true);
			saveErrors(request, errores);
			return buscarAgenda(mapping, form, request, con,usuario);
		}
		else
			errores = null;

		try
		{
			Agenda	agenda;
			int		li_seleccionado;
			Integer	li_unidadConsulta;
			String	puedoModificarServicios;

			/* Obtener datos del item seleccionado */
			li_seleccionado = form.getIndiceItemSeleccionado();

			puedoModificarServicios =
				(String)form.getItemSeleccionado("puedoModificarServicios_" + li_seleccionado);
			li_unidadConsulta =
				(Integer)form.getItemSeleccionado("codigoUnidadAgenda_" + li_seleccionado);

			/* Configurar la bosqueda de items de agenda */
			agenda = new Agenda();

			agenda.setCentrosAtencion(form.getCentrosAtencion());
			agenda.setUnidadesAgenda(form.getUnidadesAgenda());
			agenda.setCodigoConsultorio(form.getCodigoConsultorioAgenda() );
			agenda.setCodigoDiaSemana(form.getCodigoDiaSemanaAgenda() );
			agenda.setCodigoMedico(form.getCodigoMedicoAgenda() );
			agenda.setFechaInicio(form.getFechaInicioAgenda() );
			agenda.setFechaFin(form.getFechaFinAgenda() );
			agenda.setHoraInicio(form.getHoraInicioAgenda() );
			agenda.setHoraFin(form.getHoraFinAgenda() );
			agenda.setCentroAtencion(form.getCentroAtencion());

			if(!UtilidadTexto.getBoolean(puedoModificarServicios) )
				agenda.setCodigoUnidadConsulta(li_unidadConsulta.intValue() );
			else
				agenda.setCodigoUnidadConsulta(form.getCodigoUnidadConsultaAgenda() );

			/* Obtener los otems de agenda de consulta a listar */
			form.setItems(
				agenda.listarAgendaDisponible(
					con, form.getCodigoPaciente(), form.getCodigoSexoPaciente(), usuario.getCodigoInstitucionInt(), false,UtilidadTexto.getBoolean(form.getMostrarSoloDisponibles()))
			);

			/* Cerrar la conexion a base de datos */
			cerrarConexion(con);
			return mapping.findForward("listarAgenda");
		}
		catch(SQLException e)
		{
			/* Cerrar la conexion a base de datos */
			cerrarConexion(con);

			if(logger.isDebugEnabled() )
				logger.debug("No se pudo generar el listado de agenda de consulta");

			e.printStackTrace();
			return error(mapping, request, "errors.problemasBd", true);
		}
	}

	/**
	* Carga en la lista de citas a modificar todas las citas del paciente en estado volido para
	* reprogramacion
	 * @param medico 
	*/
	private ActionForward listarCitas(
		ActionMapping		aam_mapping,
		ReprogramarCitaForm	arcf_form,
		HttpServletRequest	ahsr_request,
		Connection lc_con, UsuarioBasico medico
	)throws SQLException
	{
		try
		{
			// Capturar los centros de atencion si se ha seleccionado la opcion todos
			if (arcf_form.getCentroAtencion()==ConstantesBD.codigoNuncaValido){
				arcf_form.setCentrosAtencion(arcf_form.getCentrosAtencionAutorizados("todos").toString());
			}
			
			// Capturar las unidades de agenda si se ha seleccionado la opcion todos
			if (arcf_form.getCodigoUnidadConsulta()==ConstantesBD.codigoNuncaValido){
				arcf_form.setUnidadesAgenda(arcf_form.getUnidadesAgendaAutorizadas("todos").toString());
			}
						
			/* Obtener la lista de citas reservadas para el paciente */
			Collection c=Cita.listar(
			lc_con,
			Cita.BUSCAR_PARA_REPROGRAMACION,
			arcf_form.getCodigoPaciente(),
			arcf_form.getCodigoUnidadConsulta(),
			arcf_form.getCodigoConsultorio(),
			arcf_form.getCodigoMedico(),
			arcf_form.getFechaSolicitada(),
			arcf_form.getFechaInicio(),
			arcf_form.getFechaFin(),
			arcf_form.getHoraInicio(),
			arcf_form.getHoraFin(),
			arcf_form.getCodigoEstadoCita(),
			arcf_form.getEstadoLiquidacionCita(),
			arcf_form.getCuentaPaciente(),
			arcf_form.getCentroAtencion()+"",
			arcf_form.getCentrosAtencion(),
			arcf_form.getUnidadesAgenda()
			);
			if(c!=null){
				logger.debug("# citas a reprogramar "+c.size());
			}else{
				logger.debug("No ha citas para reprogramar");
			}
			
			arcf_form.setItemsSeleccionados(lc_con,c,medico);
			
			/* Cerrar la conexion a base de datos */
			UtilidadBD.cerrarConexion(lc_con);

			return aam_mapping.findForward("reprogramarCita");
		}
		catch(SQLException lse_e)
		{
			/* Cerrar la conexion a base de datos */
			cerrarConexion(lc_con);

			if(logger.isDebugEnabled() )
				logger.debug("No se pudo generar el listado de citas a reprogramar");

			lse_e.printStackTrace();
			return error(aam_mapping, ahsr_request, "errors.problemasBd", true);
		}
	}

	/** Reprograma las citas marcadas con el estado A Reprogramar o Reprogramada 
	 * @param formaCita */
	private ActionForward reprogramar(
		ActionMapping		mapping,
		ReprogramarCitaForm	forma,
		HttpServletRequest	request,
		Connection		con
	)throws Exception
	{
		logger.info("\n\n\n\n reprogramar********************** "+forma.getItemsSeleccionados());
		
		ActionForward	validar;
		HashMap logReprogramacionMap = new HashMap();
		int numCitasReprogramadas=0;
		
		PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
		UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		
		//Informacion para la generacion del Log
		logReprogramacionMap.put("usuario_reprogramo",usuario.getLoginUsuario());
		logReprogramacionMap.put("fecha_reprogramacion",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		logReprogramacionMap.put("hora_reprogramacion",UtilidadFecha.getHoraActual());
		
		//****************************************************************************************************
		//*************************VALIDACIONES INICIALES*****************************************************
		//****************************************************************************************************
		validar =
			validarPaciente(
				mapping,
				request,
				paciente,
				usuario
			);
		
		if(validar != null)
		{
			cerrarConexion(con);
			return validar;
		}
		
		
		/**
		 * Validar concurrencia
		 * Si ya esto en proceso de distribucion, no debe dejar entrar
		 **/
		if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), usuario.getLoginUsuario()) )
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
		}
		//****************************************************************************************************
		//****************************************************************************************************
		//****************************************************************************************************
		try
		{
			ActionErrors	errores;
			boolean lb_cambio;
			boolean puedoModificarServicios;
			boolean lb_validarCitaFecha;
			Boolean lb_aux;
			Collection coleccionCitas;
			Cita cita;
			int li_codigoCita;
			int li_estadoCita;
			Integer li_aux;

			forma.setEstado("listar");

			/* Reprogramar las citas solicitadas */
			errores = new ActionErrors();
			
			coleccionCitas = new ArrayList();
			
			
			//****************************************************************************************************
			//***************ITERACION DE LAS CITAS****************************************************************
			//****************************************************************************************************
			logger.info("numero de items  >>>>> "+forma.getNumeroItemsSeleccionados());
			for(int i = 0, li_tam = forma.getNumeroItemsSeleccionados();i < li_tam;i++)
			{
				//oCambio estado de la cita?
				lb_aux		= (Boolean)forma.getItemSeleccionado("cambioEstadoCita_" + i);
				lb_cambio	= lb_aux.booleanValue();
				
				//oPuedo modificar servicios?
				puedoModificarServicios = UtilidadTexto.getBoolean(forma.getItemSeleccionado("puedoModificarServicios_" + i).toString());
				
				logger.info("cambio estado de cita ["+i+"]? "+lb_cambio);
				logger.info("puedo modificar servicios ["+i+"]? "+puedoModificarServicios);
				
				//Solo se toman las citas que se hayan modificado o que hayan sido reprogramadas
				if(lb_cambio || puedoModificarServicios)
				{
					//Validacion de la fecha
					lb_aux = (Boolean)forma.getItemSeleccionado("validarCitaFecha_" + i);
					lb_validarCitaFecha = lb_aux.booleanValue();
	
					//Codigo de la cita
					li_aux			= (Integer)forma.getItemSeleccionado("codigoCita_" + i);
					li_codigoCita	= li_aux.intValue();				
	
					//Se toma el codigo de la cita
					try
					{
						li_aux = (Integer)forma.getItemSeleccionado("codigoEstadoCita_" + i);
						li_estadoCita = li_aux.intValue();
					}
					catch (Exception e)
					{
						li_estadoCita=Integer.parseInt((String)forma.getItemSeleccionado("codigoEstadoCita_" + i));
					}
	
					/* Iniciar la cita */
					cita = new Cita();
					
					//*********************SE CARGA AL MUNDO INFORMACION BoSICA DE LA CITA*************************
					/* Establecer los datos bosicos de la cita */
					cita.setCodigo(li_codigoCita);
					
					//Informacion para la generacion del Log
					logReprogramacionMap.put("cita_reprogramada_"+numCitasReprogramadas,li_codigoCita);
					logReprogramacionMap.put("centro_atencion_anter_"+numCitasReprogramadas,forma.getItemSeleccionado("codigoCentroAtencionOriginal_"+i));
					logReprogramacionMap.put("centro_atencion_nuevo_"+numCitasReprogramadas,forma.getItemSeleccionado("codigoCentroAtencion_"+i));
					logReprogramacionMap.put("fecha_cita_anter_"+numCitasReprogramadas,forma.getItemSeleccionado("fechaOriginal_"+i));
					logReprogramacionMap.put("fecha_cita_nuevo_"+numCitasReprogramadas,forma.getItemSeleccionado("fecha_"+i));
					logReprogramacionMap.put("hora_cita_anter_"+numCitasReprogramadas,forma.getItemSeleccionado("horaInicioOriginal_"+i));
					logReprogramacionMap.put("hora_cita_nuevo_"+numCitasReprogramadas,forma.getItemSeleccionado("horaInicio_"+i));					
					
					logReprogramacionMap.put("profesional_anter_"+numCitasReprogramadas,forma.getItemSeleccionado("codigomedicoOriginal_"+i));
					logReprogramacionMap.put("profesional_nuevo_"+numCitasReprogramadas,forma.getItemSeleccionado("codigomedico_"+i));
					
					logReprogramacionMap.put("consultorio_anter_"+numCitasReprogramadas,forma.getItemSeleccionado("consultorioOriginal_"+i));
					logReprogramacionMap.put("consultorio_nuevo_"+numCitasReprogramadas,forma.getItemSeleccionado("consultorio_"+i));						
					
					logReprogramacionMap.put("unidad_agenda_anter_"+numCitasReprogramadas,forma.getItemSeleccionado("codigoUnidadAgendaOriginal_"+i));
					logReprogramacionMap.put("unidad_agenda_nuevo_"+numCitasReprogramadas,forma.getItemSeleccionado("codigoUnidadAgenda_"+i));		
										
					request.getSession().setAttribute("codigoCita_"+numCitasReprogramadas, "" + li_codigoCita);
					numCitasReprogramadas++;
					
					cita.setCodigoEstadoCita(li_estadoCita);
					cita.setCodigoPaciente(forma.getCodigoPaciente() );
					cita.setCambioEstadoCita(lb_cambio);
					cita.setTieneSolicitudes(!puedoModificarServicios);
					cita.setCodigoUsuario(usuario.getLoginUsuario());
					
					//**********************************************************
					// autorizacion de reprogramacion de citas
					// se verifica si el paciente posse citas incumplidad
					logger.info("citas incumplidad >>>>>> "+forma.getCitasIncumpl());
					logger.info("valor del contador >>>>>> "+i);
					
					if(UtilidadTexto.getBoolean(forma.getCitasIncumpl()))
					{
						
						if((!forma.getItemSeleccionado("motivoAuto_"+i).toString().equals("")) && forma.getItemSeleccionado("motivoAuto_"+i)!=null)
							cita.setMotivoAutorizacionCita(forma.getItemSeleccionado("motivoAuto_"+i).toString());
						else
							cita.setMotivoAutorizacionCita("");
						
						logger.info("motivo >>>>>>> "+cita.getMotivoAutorizacionCita());
						
						if((!forma.getItemSeleccionado("usuarioAuto_"+i).toString().equals(""))&& forma.getItemSeleccionado("usuarioAuto_"+i)!=null)
							cita.setUsuarioAutoriza(forma.getItemSeleccionado("usuarioAuto_"+i).toString());
						else
							cita.setUsuarioAutoriza("");
						
						logger.info("usuario >>>>>>> "+cita.getUsuarioAutoriza());
						
						if(forma.getItemSeleccionado("requiereAuto_"+i).toString().equals("") || forma.getItemSeleccionado("requiereAuto_"+i)==null){
							cita.setRequiereAuto(ConstantesBD.acronimoNo);
							logger.info("requiere auto >>>>> "+cita.getRequiereAuto());
						}else{
							cita.setRequiereAuto(forma.getItemSeleccionado("requiereAuto_"+i).toString());
							logger.info("requiere auto >>>>> "+cita.getRequiereAuto());
						}
					}/*else{
						cita.setMotivoAutorizacionCita("");
						cita.setUsuarioAutoriza("");
						cita.setRequiereAuto(ConstantesBD.acronimoNo);
					}*/
						
					//**********************************************************
					
					//SE verifica si se cambio la unidad de agenda
					if(forma.getItemSeleccionado("codigoUnidadAgenda_"+i).toString().equals(forma.getItemSeleccionado("codigoUnidadAgendaOriginal_"+i).toString()))
						cita.setCambioUnidadAgenda(false);
					else
						cita.setCambioUnidadAgenda(true);
	
					li_aux = (Integer)forma.getItemSeleccionado("codigoAgenda_" + i);
					cita.setCodigoAgenda(li_aux.intValue() );
	
					li_aux = (Integer)forma.getItemSeleccionado("codigoUnidadAgenda_" + i);
					cita.setCodigoUnidadConsulta(li_aux.intValue() );
					//*****************************************************************************************************
					
					forma.setMensaje(new ResultadoBoolean(false));
					
					HashMap mapa= new HashMap();
					
					//*************************SE CARGAN LOS SERVICIOS DE LA CITA AL MUNDO**********************************
					int contador = 0;
					//Se cargan los servicios de la cita
					for(int j=0;j<Integer.parseInt(forma.getItemSeleccionado("numServicios_"+i).toString());j++)
					{
						
						
//						*******************************	CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] *************************************// 					
						
						//verificar valor definido en el campo cantidad maxima de citas control post operatorio para el convenio seleccionado
						
						//validacion de convenio 
						
						if (!UtilidadTexto.isEmpty(forma.getIm_citas().get("convenio_"+i+"_"+j)+""))
						{
							//validacion de convenio - si es de reserva (convenio reserva) - si no el de prioridad uno
							forma.setConvenio(Integer.parseInt(forma.getItemSeleccionado("convenio_"+i+"_"+j).toString()));
						}
						else
						{
							forma.setConvenio(paciente.getCodigoConvenio());
						}
						
						int cantCPO=UtilidadesConsultaExterna.consultarCantidadMaximaCitasControlPostOperatorioConvenio(con, forma.getConvenio());
						
						logger.info("\n\n\n\n [Convenio] "+forma.getConvenio()+" [CantidadMaximaCitasControlPostOperatorioConvenio]  "+cantCPO);
						logger.info("\n\n\n\n [Paciente] "+forma.getCodigoPaciente());
						if (cantCPO>0)
						{
							
							
							int diasCPO=UtilidadesConsultaExterna.consultarDiasControlPostOperatorioConvenio(con, forma.getConvenio());	
							logger.info("\n\n\n\n [Convenio] "+forma.getConvenio()+" [DiasControlPostOperatorioConvenio]  "+cantCPO);
							
							
							mapa=UtilidadesConsultaExterna.validarControlPostOperatorio
													(con, 
													forma.getCodigoPaciente(), 
													forma.getItemSeleccionado("fecha_"+i).toString(), 
													cantCPO, 
													diasCPO, 
													Utilidades.convertirAEntero(forma.getItemSeleccionado("codigoEspecialidad_"+i+"_"+j)+"")
													);
		
							if(mapa.containsKey("codigopo_0"))
							{
								logger.info("SE AGREGÓ MENSAJE DE VALIDACION!!");
								forma.getIm_citas().put("solicitudpo_"+i, mapa.get("solicitud_"+0));
								forma.getIm_citas().put("codigopo_"+i, mapa.get("codigopo_"+0));
								forma.setMensaje(new ResultadoBoolean(true," Paciente Control Post Operatorio "));
							}
							else
							{
								forma.getIm_citas().put("solicitudpo_"+i, "null");
								forma.getIm_citas().put("codigopo_"+i, "null");
							}
						
							if(forma.getItemSeleccionado("codigoServicio_"+i+"_"+j)!=null)
							{
								cita.setMapaServicios("codigoServicio_"+contador, forma.getItemSeleccionado("codigoServicio_"+i+"_"+j));
								cita.setMapaServicios("codigoEspecialidad_"+contador, forma.getItemSeleccionado("codigoEspecialidad_"+i+"_"+j));
								cita.setMapaServicios("codigoCentroCosto_"+contador, forma.getItemSeleccionado("codigoCentroCosto_"+i+"_"+j));
								cita.setMapaServicios("codigoCentroCostoOriginal_"+contador, forma.getItemSeleccionado("codigoCentroCostoOriginal_"+i+"_"+j));
								cita.setMapaServicios("observaciones_"+contador, forma.getItemSeleccionado("observaciones_"+i+"_"+j));
								cita.setMapaServicios("observacionesOriginal_"+contador, forma.getItemSeleccionado("observacionesOriginal_"+i+"_"+j));
								cita.setMapaServicios("numeroSolicitud_"+contador, forma.getItemSeleccionado("numeroSolicitud_"+i+"_"+j));
								cita.setMapaServicios("estadoServicio_"+contador, forma.getItemSeleccionado("estadoServicio_"+i+"_"+j));
								cita.setMapaServicios("fechaCita_"+contador, forma.getItemSeleccionado("fecha_"+i));
								cita.setMapaServicios("horaInicioCita_"+contador, forma.getItemSeleccionado("horaInicio_"+i));
								cita.setMapaServicios("horaFinCita_"+contador, forma.getItemSeleccionado("horaFin_"+i));
								cita.setMapaServicios("codigoEstadoCita_"+contador, forma.getItemSeleccionado("codigoEstadoCita_"+i));
								cita.setMapaServicios("codigoAgenda_"+contador, forma.getItemSeleccionado("codigoAgenda_"+i));
								
//							*******************************	CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] *************************************//
								
								cita.setMapaServicios("solicitudpo_"+contador, forma.getItemSeleccionado("solicitudpo_" + i));
								cita.setMapaServicios("codigopo_"+contador, forma.getItemSeleccionado("codigopo_" + i));
								
//							*******************************	FIN CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] *********************************//
								logger.info("numeroSolicitud -- "+forma.getItemSeleccionado("numeroSolicitud_"+i+"_"+j));
								logger.info("Servicio -- "+forma.getItemSeleccionado("codigoServicio_"+i+"_"+j));
								
								//Se modifico por la Tarea 59745, con la intencion de poder mandar el codigo del convenio
								if(UtilidadJustificacionPendienteArtServ.validarNOPOS(con, Utilidades.convertirAEntero(forma.getItemSeleccionado("numeroSolicitud_"+i+"_"+j)+""), Utilidades.convertirAEntero(forma.getItemSeleccionado("codigoServicio_"+i+"_"+j)+""), false, false, Utilidades.convertirAEntero(forma.getItemSeleccionado("convenio_"+i+"_"+j)+"")))
								{
									double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, Utilidades.convertirAEntero(forma.getItemSeleccionado("codigoServicio_"+i+"_"+j)+""), Utilidades.convertirAEntero(forma.getItemSeleccionado("numeroSolicitud_"+i+"_"+j)+""), Utilidades.convertirAEntero(forma.getItemSeleccionado("convenio_"+i+"_"+j)+""), false);
									if(UtilidadJustificacionPendienteArtServ.insertarJusNP(con, Utilidades.convertirAEntero(forma.getItemSeleccionado("numeroSolicitud_"+i+"_"+j)+""), Utilidades.convertirAEntero(forma.getItemSeleccionado("codigoServicio_"+i+"_"+j)+""), 1, usuario.getLoginUsuario(), false, false, Utilidades.convertirAEntero(subcuenta+""),""))
									{
										forma.setJustificacionNoPosMap("mensaje_0", "SERVICIO ["+forma.getItemSeleccionado("codigoServicio_"+i+"_"+j)+"] DE NATURALEZA NO POS REQUIERE DE JUSTIFICACIÓN NO POS.");
									}
								}
							 }
							contador ++;
						}
						else{
//	******************************* FIN CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] **********************************//
						//Se excluyen los servicios eliminados
							if(forma.getItemSeleccionado("codigoServicio_"+i+"_"+j)!=null)
							{
								cita.setMapaServicios("codigoServicio_"+contador, forma.getItemSeleccionado("codigoServicio_"+i+"_"+j));
								cita.setMapaServicios("codigoEspecialidad_"+contador, forma.getItemSeleccionado("codigoEspecialidad_"+i+"_"+j));
								cita.setMapaServicios("codigoCentroCosto_"+contador, forma.getItemSeleccionado("codigoCentroCosto_"+i+"_"+j));
								cita.setMapaServicios("codigoCentroCostoOriginal_"+contador, forma.getItemSeleccionado("codigoCentroCostoOriginal_"+i+"_"+j));
								cita.setMapaServicios("observaciones_"+contador, forma.getItemSeleccionado("observaciones_"+i+"_"+j));
								cita.setMapaServicios("observacionesOriginal_"+contador, forma.getItemSeleccionado("observacionesOriginal_"+i+"_"+j));
								cita.setMapaServicios("numeroSolicitud_"+contador, forma.getItemSeleccionado("numeroSolicitud_"+i+"_"+j));
								cita.setMapaServicios("estadoServicio_"+contador, forma.getItemSeleccionado("estadoServicio_"+i+"_"+j));
								cita.setMapaServicios("fechaCita_"+contador, forma.getItemSeleccionado("fecha_"+i));
								cita.setMapaServicios("horaInicioCita_"+contador, forma.getItemSeleccionado("horaInicio_"+i));
								cita.setMapaServicios("horaFinCita_"+contador, forma.getItemSeleccionado("horaFin_"+i));
								cita.setMapaServicios("codigoEstadoCita_"+contador, forma.getItemSeleccionado("codigoEstadoCita_"+i));
								cita.setMapaServicios("codigoAgenda_"+contador, forma.getItemSeleccionado("codigoAgenda_"+i));
								
//							*******************************	CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] *************************************//
								
								cita.setMapaServicios("solicitudpo_"+contador, null);
								cita.setMapaServicios("codigopo_"+contador, null);
								
//							*******************************	FIN CAMBIO ANEXO 652 [CAMBIOS EN FUNCIONALIDADES POR CONTROL POST OPERATORIO] *********************************//
								
								//Se modifico por la Tarea 59745, con la intencion de poder mandar el codigo del convenio
								if(UtilidadJustificacionPendienteArtServ.validarNOPOS(con, Utilidades.convertirAEntero(forma.getItemSeleccionado("numeroSolicitud_"+i+"_"+j)+""), Utilidades.convertirAEntero(forma.getItemSeleccionado("codigoServicio_"+i+"_"+j)+""), false, false, Utilidades.convertirAEntero(forma.getItemSeleccionado("convenio_"+i+"_"+j)+"")))
								{
									double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, Utilidades.convertirAEntero(forma.getItemSeleccionado("codigoServicio_"+i+"_"+j)+""), Utilidades.convertirAEntero(forma.getItemSeleccionado("numeroSolicitud_"+i+"_"+j)+""), Utilidades.convertirAEntero(forma.getItemSeleccionado("convenio_"+i+"_"+j)+""), false); 
									if(UtilidadJustificacionPendienteArtServ.insertarJusNP(con, Utilidades.convertirAEntero(forma.getItemSeleccionado("numeroSolicitud_"+i+"_"+j)+""), Utilidades.convertirAEntero(forma.getItemSeleccionado("codigoServicio_"+i+"_"+j)+""), 1, usuario.getLoginUsuario(), false, false, Utilidades.convertirAEntero(subcuenta+""),""))
									{
										forma.setJustificacionNoPosMap("mensaje_0", "SERVICIO ["+forma.getItemSeleccionado("codigoServicio_"+i+"_"+j)+"] DE NATURALEZA NO POS REQUIERE DE JUSTIFICACIoN NO POS.");
									} 
								}
							 }
							contador ++;
						}
					}
					cita.setMapaServicios("numRegistros", contador+"");
					//****************************************************************************
					
					logger.info("\n\n*********MAPA SERVICIOS DE LA CITA ********************************");
					Utilidades.imprimirMapa(cita.getMapaServicios());
					logger.info("\n\n*********FIN MAPA SERVICIOS DE LA CITA ********************************");
					//******************VALIDACIONES ESPECIALES PARA REPROGRAMADAS********************************
					//Si la cita es reprogramada se realiza la verificacion de la fecha
					//Se verifica que si hay mas citas para el doa se confirme
					if(li_estadoCita == ConstantesBD.codigoEstadoCitaReprogramada)
					{
						
						forma.setIndiceItemSeleccionado(i);
	
						
						if(lb_validarCitaFecha)
						{
							if(cita.validarReservaCitaFecha(con) )
							{
								/* Cerrar la conexion a base de datos */
								cerrarConexion(con);
	
								/* Confirmar la reprogramacion de la cita */
								return mapping.findForward("confirmarCita");
							}
						}
					}
					//*******************************************************************************
					
					if(errores.isEmpty() )
						coleccionCitas.add(cita);
				}
			}

			
			//******************REPROGRAMACION DE CITAS********************************************************
			if(errores.isEmpty()/* && !hayErroresCargo*/)
			{
				try
				{
					logger.info("coleccion >>>>>>>>>>> "+coleccionCitas);
					if(!Cita.reprogramarCitas(con, coleccionCitas, usuario.getLoginUsuario()))
					{
						errores.add("Error al realizar la reprogramaacion de citas", new ActionMessage("error.cita.reprogramacion"));
					}
				}
				catch(Exception le_e)
				{
					errores.add("codigoSexoPaciente", new ActionMessage("error.cita.reprogramacion"));
					logger.error("Error al ir a la reprogramacion de citas: "+le_e);
				}
			}
			//***********************************************************************************************

			/* Cerrar la conexion a base de datos */
			
			
			logger.info("FINALIZACION PROCESO REPROGRAMACION");
			
			if(!errores.isEmpty() )
			{
				logger.info("¡¡¡HUBO ERRORES!!");					
				saveErrors(request, errores);
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("reprogramarCita");
			}
			
			request.setAttribute("numeroCitas", "" + numCitasReprogramadas); 
			if (numCitasReprogramadas>0)
			{
				//Utilidades.imprimirMapa(logReprogramacionMap);
				for(int i = 0; i < numCitasReprogramadas; i++)
				{
					if(logReprogramacionMap.containsKey("fecha_cita_anter_"+i) &&  
						 !logReprogramacionMap.get("fecha_cita_anter_"+i).toString().equals("") && 
						 	!logReprogramacionMap.get("fecha_cita_anter_"+i).toString().isEmpty())
					{
						if(!Cita.guardarLogReprogramacionCita(
								con, 
								logReprogramacionMap.get("cita_reprogramada_"+i).toString(),
								logReprogramacionMap.get("fecha_reprogramacion").toString(),
								logReprogramacionMap.get("hora_reprogramacion").toString(),												
								logReprogramacionMap.get("usuario_reprogramo").toString(),
								logReprogramacionMap.get("centro_atencion_anter_"+i).toString(),
								logReprogramacionMap.get("centro_atencion_nuevo_"+i).toString(),
								UtilidadFecha.conversionFormatoFechaABD(logReprogramacionMap.get("fecha_cita_anter_"+i).toString()), 
								UtilidadFecha.conversionFormatoFechaABD(logReprogramacionMap.get("fecha_cita_nuevo_"+i).toString()),
								logReprogramacionMap.get("hora_cita_anter_"+i).toString(),
								logReprogramacionMap.get("hora_cita_nuevo_"+i).toString(), 
								logReprogramacionMap.get("profesional_anter_"+i).toString(),
								logReprogramacionMap.get("profesional_nuevo_"+i).toString(),
								logReprogramacionMap.get("consultorio_anter_"+i).toString(),
								logReprogramacionMap.get("consultorio_nuevo_"+i).toString(), 
								logReprogramacionMap.get("unidad_agenda_anter_"+i).toString(), 
								logReprogramacionMap.get("unidad_agenda_nuevo_"+i).toString()))
						{
							logger.info("oooHUBO ERRORES en Insertar el Log!!, en la posicion >> "+i);
							Utilidades.imprimirMapa(logReprogramacionMap);
							saveErrors(request, errores);
							UtilidadBD.cerrarConexion(con);
							return mapping.findForward("reprogramarCita");
						}
					}
				}
				UtilidadBD.closeConnection(con);
				logger.info("HAY MENSJAE?: "+forma.getMensaje().isTrue()+": "+forma.getMensaje().getDescripcion());
				//Se modifico para poder imprimir en el reporte la informacion del paciente
				return this.resumenImpresionVariasCitas(mapping, request, "reprogramar", forma.getCodigoPaciente());
			}
			else
			{
				return listarCitas(mapping, forma, request, con, usuario);
			}
		}
		catch(SQLException e)
		{
			/* Cerrar la conexion a base de datos */
			cerrarConexion(con);

			if(logger.isDebugEnabled() )
				logger.debug("No se realizar el proceso de reprogramacion de citas");

			e.printStackTrace();
			return error(mapping, request, "errors.problemasBd", true);
		}
	}

	/** Validar que el paciente cumpla todas las condiciones para la reprogramacion de citas 
	 * @param usuario */
	private ActionForward validarPaciente(
		ActionMapping		mapping,
		HttpServletRequest	request,
		PersonaBasica		paciente, UsuarioBasico usuario
	)
	{
		if(paciente.getCodigoPersona() < 0)
		{
			/* El paciente no esta cargado en sesion */
			if(logger.isDebugEnabled() )
				logger.debug("El paciente no está cargado en sesión");

			return error(mapping, request, "errors.paciente.noCargado", true);
		}
		//Validar que el usuario no se autoatienda
		ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
		if(respuesta.isTrue())
			return error(mapping, request,  respuesta.getDescripcion(), true);
		return null;
	}


	/**
	 * Motodo que se encarga de realizar el resumen de la impresion
	 * 
	 * @param mapping Mapping con los sitios a los que puede 
	 * acceder struts
	 * @param request Objeto request
	 * @param codigoPaciente 
	 * @param formaCita 
	 * @return
	 * @throws Exception
	 */
	private ActionForward resumenImpresionVariasCitas (ActionMapping mapping, HttpServletRequest request, String tipo, int codigoPaciente) throws Exception
	{
		int i=0, numElementos=0, codigosCita[];
		String codigos = ConstantesBD.codigoNuncaValido+"";
		try
		{
			numElementos=Integer.parseInt((String)request.getAttribute("numeroCitas"));
			codigosCita = new int[numElementos];
			for (i=0;i<numElementos;i++)
			{
				codigosCita[i]=Integer.parseInt((String)request.getSession().getAttribute("codigoCita_" + i));
				request.getSession().removeAttribute("codigoCita_" + i);
				codigos += ","+codigosCita[i];
			}
			Cita citaACargar = new Cita();
			HashMap resultados = citaACargar.cargarImpresionCodigos(codigosCita);
			request.setAttribute("tipo", tipo);
			request.setAttribute("citasAImprimir", resultados);
			request.setAttribute("codigosCitasImprimir", codigos);
			request.setAttribute("codigoPaciente", codigoPaciente+"");
			request.setAttribute("nombreForm", "ReprogramarCitaForm");
			
			return mapping.findForward("resumenImpresionesCita2");
		}
		catch (Exception e)
		{
			return this.error(mapping, request, "Error durante visualizacion cita(s)", false);
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param paciente
	 * @param cod_modulo
	 * @param parametrosModulo
	 * @param fechaIniciControlMultas
	 */
	public boolean validarEstadoCitasPaciente(Connection con, PersonaBasica paciente, UsuarioBasico usuario,
			int cod_modulo, String[] parametrosModulo, ReprogramarCitaForm	forma, HttpServletRequest request){
		try
		{
			String manMultaIncCita = ConstantesBD.acronimoNo;
			String fechaIniControl = "";
			String bloqCitasRevAsgiReprog = ConstantesBD.acronimoNo;
			
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			ArrayList<HashMap<String, Object>> citasIncumplidas = new ArrayList<HashMap<String, Object>>();
			ArrayList<HashMap<String, Object>> array1 = new ArrayList<HashMap<String, Object>>();
			array1 = UtilidadesConsultaExterna.institucionManejaMultasIncumCitas(con,institucion.getCodigo(),cod_modulo,parametrosModulo);
			if(array1.size()>0)
			{
				// se verifica los parametros generales de la institucion para saber si se maneja 
				// multas por incumpimiento de citas, si se bloque la reserva, asignacion y reprogramacion de las citas
				// y la fecha inicial de control de multas
				//--------------------------------------
				for(HashMap elemento:array1)
				{
					if(elemento.get("parametro").toString().equals(ConstantesValoresPorDefecto.nombreInstitucionManejaMultasPorIncumplimiento))
						manMultaIncCita = elemento.get("valor").toString();
					else if(elemento.get("parametro").toString().equals(ConstantesValoresPorDefecto.nombreBloqueaCitasReservaAsignReprogPorIncump))
						bloqCitasRevAsgiReprog = elemento.get("valor").toString();
					else if(elemento.get("parametro").toString().equals(ConstantesValoresPorDefecto.nombreFechaInicioControlMultasIncumplimientoCitas))
						fechaIniControl = elemento.get("valor").toString();
				}
				//--------------------------------------
				
				if(!manMultaIncCita.equals(ConstantesBD.acronimoNo))
				{
					// si la institucion maneja multas por incumplimiento de citas
					if(!fechaIniControl.equals(""))
					{
						// se carga las citas del paciente con estado no asistio y que la fecha de generacion de la cita
						// se mayor o igual a la fecha inicial de control de citas de la institucion
						ArrayList<HashMap<String, Object>> array2 = new ArrayList<HashMap<String, Object>>();
						array2 = UtilidadesConsultaExterna.estadoCitasPaciente(con, paciente.getCodigoPersona(), fechaIniControl,true);
						if(array2.size()>0)
						{
							if(!bloqCitasRevAsgiReprog.equals(ConstantesBD.acronimoNo))
							{
								// si el parametro de bloque de reservas, asignacion de citas y de reprogramancion se cumple
								for(HashMap elementocita:array2)
								{
									logger.info("valor de la solicitud"+elementocita.get("SolicitudSerCita").toString());
									if(!elementocita.get("SolicitudSerCita").toString().equals(ConstantesBD.codigoNuncaValido+""))
									{
										logger.info("hay numero de solicitud");
										if(!elementocita.get("convenioManejaMultIncCita").equals(ConstantesBD.acronimoNo))
										{
											//en el caso de que el convenio maneje multas por incumplimiento de citas
											// se adiciona al arraylist la cita incumplida 
											HashMap<String, Object> citaincumplida = new HashMap<String, Object>();
											citaincumplida.put("centroAtencion", elementocita.get("centroAtencion"));
											citaincumplida.put("nombreAgenda", elementocita.get("nombreAgenda"));
											citaincumplida.put("fechaCita", elementocita.get("fechaCita"));
											citaincumplida.put("horaInicioCita", elementocita.get("horaInicioCita"));
											citaincumplida.put("horaFinalCita", elementocita.get("horaFinalCita"));
											citaincumplida.put("nombreProfesional", elementocita.get("nombreProfesional"));
											citaincumplida.put("codConvenio", elementocita.get("codConvenio"));
											citaincumplida.put("convenioManejaMultIncCita", elementocita.get("convenioManejaMultIncCita"));
											citasIncumplidas.add(citaincumplida);
										}
									}else
									{
										logger.info("NO hay numero de solicitud");
										HashMap<String, Object> citaincumplida = new HashMap<String, Object>();
										citaincumplida.put("centroAtencion", elementocita.get("centroAtencion"));
										citaincumplida.put("nombreAgenda", elementocita.get("nombreAgenda"));
										citaincumplida.put("fechaCita", elementocita.get("fechaCita"));
										citaincumplida.put("horaInicioCita", elementocita.get("horaInicioCita"));
										citaincumplida.put("horaFinalCita", elementocita.get("horaFinalCita"));
										citaincumplida.put("nombreProfesional", elementocita.get("nombreProfesional"));
										citaincumplida.put("codConvenio", elementocita.get("codConvenio"));
										citaincumplida.put("convenioManejaMultIncCita", elementocita.get("convenioManejaMultIncCita"));
										citasIncumplidas.add(citaincumplida);
									}
								}
								
								if(citasIncumplidas.size()>0)
								{
									// se viculan todas las citas incumplidad listas para mostrar
									// y que pasaron los filtros
									forma.setCitasIncumplidas(citasIncumplidas);
									//forma.setEstadoValidarCitasPaciente(true);
									//forma.setRealizadaVerificacion(true);
									return true;
								}
							}
						}
					}
				}
			}
		//logger.info("SE PERMITE RESERVAR, ASIGNAR Y/O REPROGRAMAR CITAS ASI HAYA CITAS INCUMPLIDAS\nse desarolla normalmente el proceso de asignacin , reserva y reprogramacion de citas");
		//logger.info("NO SE ENCONTRARON CITAS EN ESTADO NO ASISTIO CON O SIN MULTA\nse desarolla normalmente el proceso de asignacin , reserva y reprogramacion de citas");
		//logger.info("no existe la fecha inicial de control de multas por incumplimiento de citas");
		//logger.info("INSTITUCION NO MANEJA MULTAS!!!!!\nse desarolla normalmente el proceso de asignacin , reserva y reprogramacion de citas");
		//logger.info("no hay parametrizacion de la institucion en multas de incumplimientio de citas");
		forma.setRealizadaVerificacion(true);
		forma.setEstadoValidarCitasPaciente(false);
		return false;
		}catch(Exception e){
			logger.info("Ha ocurrido un error el la verificacion del estado de citas del paciente !!!!!! ");
			forma.setRealizadaVerificacion(false);
			forma.setEstadoValidarCitasPaciente(false);
			return false;
		}
	}
	
	/**
	 * Metodo para vericar los permisos del usuario que esta logiadoen 
	 * cuanto asignacion de citas incumplidas por el paciente  
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	public ActionForward verificarAutoUsurio(Connection con, 
			ReprogramarCitaForm forma, 
			UsuarioBasico usuario, 
			HttpServletRequest request, 
			ActionMapping mapping)
	{
		// verificacion si el usuario que esta logiado tiene los permi sos para autrizar la reprogramacion de la
		// cita del paciente para la agenda, activida y centro costo
		forma.setCargarAgenda(ConstantesBD.acronimoNo);
		
		logger.info("posicion >>>>>>>>> "+forma.getPosCita());
		
		if(!forma.getItemSeleccionado("motivoAuto_"+ forma.getPosCita()).toString().equals("") &&
			!forma.getItemSeleccionado("usuarioAuto_"+ forma.getPosCita()).toString().equals(""))
		{
			forma.setMostrarFormAuto(ConstantesBD.acronimoNo);
			forma.setEstado("prepararReprogramarCita");
		}else{
			forma.setMostrarFormAuto(ConstantesBD.acronimoSi);
			if(UtilidadTexto.getBoolean(forma.getCitasIncumpl()))
			{
				//***********************************************
				// validar si el usuario esta autorizado 
				logger.info("agenda >>>>>>>> "+forma.getItemSeleccionado("codigoUnidadAgenda_"+forma.getPosCita()).toString());
				logger.info("centro de costo >>>>>>>  "+forma.getItemSeleccionado("codigoCentroAtencion_"+forma.getPosCita()).toString());
				logger.info("usuario >>>>>> "+usuario.getLoginUsuario());
				if(UtilidadesConsultaExterna.esActividadAurtorizada(con, 
						Utilidades.convertirAEntero(forma.getItemSeleccionado("codigoUnidadAgenda_"+forma.getPosCita()).toString()), 
						ConstantesBD.codigoActividadAutorizadaAutorizarCitasPacientesConMulta, 
						usuario.getLoginUsuario(), 
						Utilidades.convertirAEntero(forma.getItemSeleccionado("codigoCentroAtencion_"+forma.getPosCita()).toString()), 
						true)){
					forma.setItemSeleccionado("motivoAuto_"+ forma.getPosCita(),forma.getMotivoAutorizacionCita());
					forma.setItemSeleccionado("usuarioAuto_"+ forma.getPosCita(),forma.getUsuarioAutoriza());
					forma.setItemSeleccionado("requiereAuto_"+ forma.getPosCita(),ConstantesBD.acronimoSi);
					forma.setRequiereAuto(ConstantesBD.acronimoSi);
					forma.setPreguntarAutorizacion(ConstantesBD.acronimoSi);
					forma.setVerificarEstCitaPac(ConstantesBD.acronimoSi);
					logger.info("requiere autorizacion >>>> "+forma.getVerificarEstCitaPac());
				}else{
					logger.info("el usario no posee los permisos");
					forma.setItemSeleccionado("motivoAuto_"+ forma.getPosCita(),"");
					forma.setItemSeleccionado("usuarioAuto_"+ forma.getPosCita(),"");
					forma.setItemSeleccionado("requiereAuto_"+ forma.getPosCita(),ConstantesBD.acronimoNo);
					forma.setRequiereAuto(ConstantesBD.acronimoNo);
					forma.setPreguntarAutorizacion(ConstantesBD.acronimoNo);
					forma.setVerificarEstCitaPac(ConstantesBD.acronimoNo);
				}
				//***********************************************
				forma.setItemSeleccionado("cargarAgenda_"+forma.getPosCita(),ConstantesBD.acronimoNo);
			}else{
				logger.info("no hay citas incumplidas ");
				forma.setMostrarFormAuto(ConstantesBD.acronimoNo);
				forma.setEstado("prepararReprogramarCita");
			}
		}
		logger.info("se dispone a redirigirlo al lugar  <<<<<<<<< reprogrmar cita >>>>>>>>>>>");
		return mapping.findForward("reprogramarCita");
		
	}
	
	/**
	 * Motodo implementado para guardar la utorizacion de la repro gramacion de la cita
	 * @param connection
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward guardarMotivoAutorizacion(
			Connection connection,
			ReprogramarCitaForm citaForm, 
			ActionMapping mapping,
			HttpServletRequest request) 
	{
		//guardar el motivo de la consulta
		
		logger.info("posicion en el guardado >>>>>>>>> "+citaForm.getPosCita());
		
		
		ActionErrors errors = new ActionErrors(); 
		if(citaForm.getUsuarioAutoriza().equals(""))
			errors.add("descripcion",new ActionMessage("errors.required","El Usuario que Autoriza "));
		if(citaForm.getMotivoAutorizacionCita().equals(""))
			errors.add("descripcion",new ActionMessage("errors.required","El Motivo de la Autorización "));
		
		if(!errors.isEmpty()){
			citaForm.setEstado("");
			saveErrors(request, errors);
			return mapping.findForward("autorizacionCita");
		}else{
			citaForm.setItemSeleccionado("motivoAuto_"+citaForm.getPosCita(),citaForm.getMotivoAutorizacionCita());
			citaForm.setItemSeleccionado("usuarioAuto_"+citaForm.getPosCita(),citaForm.getUsuarioAutoriza());
			citaForm.setItemSeleccionado("requiereAuto_"+citaForm.getPosCita(),ConstantesBD.acronimoSi);
			//citaForm.setItemSeleccionado("cargarAgenda_"+citaForm.getPosCita(),ConstantesBD.acronimoSi);
			//prepararReprogramarCita
			citaForm.setEstado("guardarAutorizacionCita");
			return mapping.findForward("autorizacionCita");
		}
	}
}