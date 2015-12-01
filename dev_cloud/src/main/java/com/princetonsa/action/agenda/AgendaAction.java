/*
* @(#)AgendaAction.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.action.agenda;


import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.actionform.agenda.AgendaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.Agenda;

/**
* Action, controla todas las opciones dentro de una agenda de consulta, incluyendo los posibles
* casos de error, y los casos de flujo.
*
* @version 1.0, Sep 17, 2003
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*
* @see org.apache.struts.action.ActionForm#validate(ActionMapping, HttpServletRequest)
*/
public class AgendaAction extends Action
{
	/** Objeto que permite el manejo de archivos de registro */
	private transient Logger logger = Logger.getLogger(AgendaAction.class);

	/** Definición de constantes del estado del flujo */
	private static final int ii_ESTADO_INVALIDO			= -1;
	private static final int ii_CANCELAR_AGENDA			= 1;
	private static final int ii_DETALLE_AGENDA			= 2;
	private static final int ii_ELIMINAR_AGENDA			= 3;
	private static final int ii_GENERAR_AGENDA			= 4;
	private static final int ii_LISTAR_AGENDA			= 5;
	private static final int ii_NUEVA_AGENDA			= 6;
	private static final int ii_PREPARAR_LISTADO_AGENDA	= 7;
	private static final int ii_CAMBIAR_CENTRO_ATENCION	= 8;
	private static final int ii_IMPRESION_AGENDA	= 9;
	private static final int ii_GENERAR_AGENDA_CONSULTORIO = 10;
	
	/**
	* Abre una conexión a una fuente de datos
	* ab_iniciarTransaccion indicador de si la conexión debe o no iniciar una transacción
	* @throws SQLException
	*/
	private Connection abrirConexion(boolean ab_iniciarTransaccion)
	{
		Connection	lc_con;
		DaoFactory	ldf_df;

		/* Obtener una conexión a la fuente de datos */
		try
		{
			ldf_df	= DaoFactory.getDaoFactory(System.getProperty("TIPOBD") );
			lc_con	= ldf_df.getConnection();

			/* Iniciar una transacción si es necesario */
			if(ab_iniciarTransaccion)
			{
				if(!ldf_df.beginTransaction(lc_con) )
					lc_con = null;
			}
		}
		catch(SQLException lse_e)
		{
			if(logger.isDebugEnabled() )
				logger.debug("No se pudo obtener conexión a la fuente de datos");

			lc_con = null;
		}

		return lc_con;
	}

	/* Eliminar items de agenda de consulta */
	private ActionForward cancelarAgenda(
		ActionMapping		mapping,
		AgendaForm			form,
		HttpServletRequest	request, 
		UsuarioBasico usuario
	)throws Exception
	{
		Agenda		agenda;
		Connection	con;

		if( (con = abrirConexion(false) ) == null)
			return error(mapping, request, "errors.problemasBd", true);

		try
		{
			/* Copiar la información de el formulario a la agenda */
			agenda = new Agenda();
			
			PropertyUtils.copyProperties(agenda, form);
			
			// Capturar los centros de atencion si se ha seleccionado la opcion todos
			if (form.getCentroAtencion()==ConstantesBD.codigoNuncaValido){
				agenda.setCentrosAtencion(form.getCentrosAtencionAutorizados("todos").toString());
			}
			
			// Capturar las unidades de agenda si se ha seleccionado la opcion todos
			if (form.getCodigoUnidadConsulta()==ConstantesBD.codigoNuncaValido){
				agenda.setUnidadesAgenda(form.getUnidadesAgendaAutorizadas("todos").toString());
			}
			
			
			/* Cancelar la agenda de consultas */
			form.setItemsAEliminar(agenda.listarCitasOcupadasAEliminar(con));
			
			form.setCitasNoAtendidas(agenda.listarCitasNoAtendidasAEliminar(con));
			
			form.setExisteValoresUnidadConsulta(UtilidadValidacion.existeValoresCancelarAgenda(con, form.getCodigoUnidadConsulta(), UtilidadFecha.conversionFormatoFechaABD(form.getFechaInicio()),  UtilidadFecha.conversionFormatoFechaABD(form.getFechaFin()),  form.getHoraInicio(),  form.getHoraFin(),  form.getCodigoConsultorio(),  form.getCodigoDiaSemana(),  form.getCodigoMedico(), form.getCentroAtencion(), agenda.getCentrosAtencion(), agenda.getUnidadesAgenda() ));
			
			HashMap respuesta = agenda.cancelarAgenda(con);
			form.setCancelados("" + respuesta.get("cancelados"));
			request.setAttribute("cancelados", "" + respuesta.get("cancelados") );
			
			
			/*Se genera el log de la cancelacion de la agenda*/
			this.generarLogCancelacionAgenda(respuesta,usuario);
			
			/* Cerrar la conexion a base de datos */
			cerrarConexion(con);
			return mapping.findForward("confirmacionAgenda");				
		}
		catch(Exception e)
		{
			/* Cerrar la conexion a base de datos */
			cerrarConexion(con);
			/* Error cancelar la agenda de consultas */
			if(logger.isDebugEnabled() )
				logger.debug("No se pudo cancelar la agenda de consultas");

			e.printStackTrace();
			return error(mapping, request, "errors.problemasBd", true);
		}
	}

	/**
	 * Método que realiza la cancelación de la agenda
	 * @param respuesta
	 * @param usuario 
	 */
	private void generarLogCancelacionAgenda(HashMap respuesta, UsuarioBasico usuario) 
	{
		int numRegistros = Integer.parseInt(respuesta.get("numRegistros").toString());
		String log = "";
		if(numRegistros>0)
		{
			 log="\n            ====INFORMACION ELIMINADA DE CANCELACIÓN AGENDA===== " ;
			 for(int i=0;i<numRegistros;i++)
			 {
				 log+="\n\n\n   código agenda ["+respuesta.get("codigoAgenda_"+i)+"]"+
				 	"\n   fecha generación ["+respuesta.get("fechaGeneracion_"+i)+"]"+
				 	"\n   hora generación ["+respuesta.get("horaGeneracion_"+i)+"]"+
				 	"\n   usuario ["+respuesta.get("usuario_"+i)+"]"+
				 	"\n   unidad consulta ["+respuesta.get("unidadConsulta_"+i)+"]"+
				 	"\n   consultorio ["+respuesta.get("consultorio_"+i)+"]"+
				 	"\n   día ["+respuesta.get("dia_"+i)+"]"+
				 	"\n   fecha ["+respuesta.get("fecha_"+i)+"]"+
				 	"\n   hora inicio ["+respuesta.get("horaInicio_"+i)+"]"+
				 	"\n   hora fin ["+respuesta.get("horaFin_"+i)+"]"+
				 	"\n   médico ["+respuesta.get("medico_"+i)+"]"+
				 	"\n   tiempo sesion ["+respuesta.get("tiempoSesion_"+i)+"]"+
				 	"\n   cupos ["+respuesta.get("cupos_"+i)+"]"+
				 	"\n   activo ["+respuesta.get("activo_"+i)+"]"+
				 	"\n   centro de atención ["+respuesta.get("centroAtencion_"+i)+"]";
			 }
			 log+="\n========================================================\n\n\n " ;
			LogsAxioma.enviarLog(ConstantesBD.logCancelacionAgendaCodigo, log, ConstantesBD.tipoRegistroLogEliminacion,usuario.getLoginUsuario());
		}		
	}

	/**
	* Cerrar una conexión con una fuente de datos
	* @param con Conexión con la fuente de datos a cerrar
	* @throws SQLException
	*/
	private void cerrarConexion(Connection con)throws SQLException
	{
		if(con != null && !con.isClosed() )
            UtilidadBD.closeConnection(con);
;
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
		ahsr_request.setAttribute(
			lb_parametrizado ? "codigoDescripcionError" : "descripcionError",
			ls_error
		);

		/* Obtener la página de errores */
		return aam_mapping.findForward("paginaError");
	}

	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(
		ActionMapping		aam_mapping,
		ActionForm			aaf_form,
		HttpServletRequest	ahsr_request,
		HttpServletResponse	ahsr_response
	)throws Exception
	{
		/* Validar que el tipo de formulario recibido sea el esperado */
		if(aaf_form instanceof AgendaForm)
		{
			AgendaForm		laf_form;
			int				li_estado;
			String			ls_estado;
 
			/* Obtener el estado actual */
			laf_form = (AgendaForm)aaf_form;
			ls_estado = laf_form.getEstado();
			
			UsuarioBasico usuario=(UsuarioBasico)ahsr_request.getSession().getAttribute("usuarioBasico");
			
			/* Validar el estado actual del flujo */
			if(ls_estado == null)
				li_estado = ii_ESTADO_INVALIDO;
			else if(ls_estado.equals("cancelarAgenda") )
				li_estado = ii_CANCELAR_AGENDA;
			else if(ls_estado.equals("detalleAgenda") )
				li_estado = ii_DETALLE_AGENDA;
			else if(ls_estado.equals("eliminarAgenda") )
				li_estado = ii_ELIMINAR_AGENDA;
			else if(ls_estado.equals("generarAgenda") )
				li_estado = ii_GENERAR_AGENDA;
			else if(ls_estado.equals("generarAgendaConsul") || ls_estado.equals("horarioConsultorio"))
				li_estado = ii_GENERAR_AGENDA_CONSULTORIO;
			else if(ls_estado.equals("listarAgenda") )
				li_estado = ii_LISTAR_AGENDA;
			else if(ls_estado.equals("nuevaAgenda") )
				li_estado = ii_NUEVA_AGENDA;
			else if(ls_estado.equals("prepararListadoAgenda") )
				li_estado = ii_PREPARAR_LISTADO_AGENDA;
			else if(ls_estado.equals("cambiarCentroAtencion") )
				li_estado = ii_CAMBIAR_CENTRO_ATENCION;
			else if(ls_estado.equals("imprimirAgenda") )
				li_estado = ii_IMPRESION_AGENDA;
			else
				li_estado = ii_ESTADO_INVALIDO;
			
			logger.warn("\n\nESTADO (AgendaAction) > "+li_estado);
			
		

			switch(li_estado)
			{
				case ii_ESTADO_INVALIDO:
					
					/* El estado actual es intederminado y por lo tanto no válido */
					if(logger.isDebugEnabled() )
						logger.debug("Estado inválido en el flujo de agenda de consultas");

					return error(aam_mapping, ahsr_request, "errors.estadoInvalido", true);

				case ii_CANCELAR_AGENDA:
					
					/* Cancela ítems de agenda de consulta existentes */

					return cancelarAgenda(aam_mapping, laf_form, ahsr_request,usuario);

				case ii_DETALLE_AGENDA:
					/* Obtener el detalle de un item de agenda de consulta para consulta */

					return detalleAgenda(aam_mapping, laf_form, ahsr_request);

				case ii_ELIMINAR_AGENDA:
				
					/* Ingresar la información para la cancelación de agendas ya exisentes */
					return iniciarAgenda(aam_mapping, ahsr_request, laf_form, "cancelarAgenda", li_estado, usuario);

				case ii_GENERAR_AGENDA:
					/* Genera una agenda de consulta de acuerdo a los parámetros recibidos */
					
					return generarAgenda(aam_mapping, laf_form, ahsr_request,null);
					
				case ii_GENERAR_AGENDA_CONSULTORIO:
					/* Genera la agenda a  partir de los horarios que no poseen Consultorios*/
					
					if(ls_estado.equals("generarAgendaConsul"))					
						return generarAgenda(aam_mapping, laf_form, ahsr_request,laf_form.getHoraAtencionConMap());					
					else if(ls_estado.equals("horarioConsultorio"))
						return aam_mapping.findForward("horarioConsultorio");

				case ii_LISTAR_AGENDA:
					
					/* Listar ítems de agenda de consulta de acuerdo a los parámetros recibidos */
					return listarAgenda(aam_mapping, laf_form, ahsr_request);

				case ii_NUEVA_AGENDA:
					
					/* Ingresar la información de una nueva agenda de consultas */
					return iniciarAgenda(aam_mapping, ahsr_request, laf_form, "generarAgenda", li_estado, usuario);

				case ii_PREPARAR_LISTADO_AGENDA:
					
					/* Ingresar la información listar la agenda de consultas */
					return iniciarAgenda(aam_mapping, ahsr_request, laf_form, "listarAgenda", li_estado, usuario);
					
				case ii_CAMBIAR_CENTRO_ATENCION:
					
					/* Ingresar la información de una nueva agenda de consultas */
					return iniciarAgenda(aam_mapping, ahsr_request, laf_form, laf_form.getEstadoAnterior(), li_estado, usuario);
				case ii_IMPRESION_AGENDA:
					
					/* Ejecutar la impresion de la agenda*/
					laf_form.setEstado("cancelarAgenda");
					return aam_mapping.findForward("impresionAgenda");
					
			}
		}
		/* El formulario recibido no corresponde a un formulario de agenda de consultas */
		if(logger.isDebugEnabled() )
			logger.debug(
				"El formulario actual no corresponde con el formulario esperado por agenda " +
				"de consultas"
			);

		return error(aam_mapping, ahsr_request, "errors.accesoInvalido", true);
	}

	/** Obtener el detalle de un item de agenda de consulta para consulta */
	private ActionForward detalleAgenda(
		ActionMapping		aam_mapping,
		AgendaForm			aaf_form,
		HttpServletRequest	ahsr_request
	)throws Exception
	{
		Connection lc_con;

		if( (lc_con = abrirConexion(false) ) == null)
			return error(aam_mapping, ahsr_request, "errors.problemasBd", true);

		try
		{
			/* Obtener los datos del ítem de agenda de consulta especificado */
			aaf_form.setItem(Agenda.detalleAgenda(lc_con, aaf_form.getCodigoAgenda() ) );

			/* Cerrar la conexion a base de datos */
			cerrarConexion(lc_con);

			return aam_mapping.findForward("detalleAgenda");
		}
		catch(Exception le_e)
		{
			/* Cerrar la conexion a base de datos */
			cerrarConexion(lc_con);

			/* Error al obtener datos del ítem de agenda de consulta desde la fuente de datos */
			if(logger.isDebugEnabled() )
				logger.debug("No se pudo obtener el ítem de agenda de consulta");

			le_e.printStackTrace();

			return error(aam_mapping, ahsr_request, "errors.problemasBd", true);
		}
	}
	
	
	
	
	/** Genera una agenda de consulta de acuerdo a los parámetros recibidos */
	private ActionForward generarAgenda(ActionMapping aam_mapping, AgendaForm aaf_form,
		HttpServletRequest	ahsr_request, HashMap horarioConsultorio )throws Exception
	{
		Agenda		la_agenda;
		Connection	lc_con;
		
		/* MT 1233 */
		aaf_form.setCodigoDiaSemana(convertirDiaSemanaAgendaToBD(aaf_form.getCodigoDiaSemana(), aaf_form));
		
		if( (lc_con = abrirConexion(false) ) == null)
			return error(aam_mapping, ahsr_request, "errors.problemasBd", true);

		/* Copiar la información de el formulario a la agenda */
		la_agenda = new Agenda();
		PropertyUtils.copyProperties(la_agenda,aaf_form);

		//	Capturar los centros de atencion si se ha seleccionado la opcion todos
		if (aaf_form.getCentroAtencion()==ConstantesBD.codigoNuncaValido)
			la_agenda.setCentrosAtencion(aaf_form.getCentrosAtencionAutorizados("todos").toString());
		
		// Capturar las unidades de agenda si se ha seleccionado la opcion todos
		if (aaf_form.getCodigoUnidadConsulta()==ConstantesBD.codigoNuncaValido)
			la_agenda.setUnidadesAgenda(aaf_form.getUnidadesAgendaAutorizadas("todos").toString());
		
		
		try
		{	
			//Verifica que la informacion para el mapa de horarios de atencion sin consultorio no se encuentre vacia
			if(horarioConsultorio != null)
			{
				ActionErrors errores = new ActionErrors();
				errores = validarHorarioConsultorio(errores,horarioConsultorio);
				
				if(!errores.isEmpty())
				{
					saveErrors(ahsr_request, errores);
					return aam_mapping.findForward("horarioConsultorio");					
				}
			}
			
			//Captura los Horarios de Atencion que no poseen numero de consultorio
			aaf_form.setHoraAtencionConMap(la_agenda.getHorariosAtencionSinConsultorios(lc_con));
			
			
			//***********Genera la Agenda******************************	
			int cod=la_agenda.generarAgenda(lc_con,horarioConsultorio);
			
			
			/* Genera una agenda de consultas */
			//ahsr_request.setAttribute("generados", "" + la_agenda.generarAgenda(lc_con));
			ahsr_request.setAttribute("generados", "" +cod);
			aaf_form.setParcial(la_agenda.getParcial());
			
			
			/*Si cod==-2 es porque hay un error, ya que no se puede generar la agenda porque existen una(s) fechas 
			/en excepciones de agenda*/
			if (cod==-2)
			{
				
				String tempFechas=la_agenda.getParcial();
				
				ActionErrors lae_errores;
				lae_errores = new ActionErrors();
				
				lae_errores.add("codigoMedico", new ActionMessage("error.agenda.horariosExcepcion", tempFechas));
				saveErrors(ahsr_request, lae_errores);
				
				UtilidadBD.closeConnection(lc_con);
				
				logger.info("EL ESTADO ES===> "+aaf_form.getEstado()+", num registros=> "+aaf_form.getHoraAtencionConMap("numRegistros"));
				
				if(aaf_form.getEstado().equals("generarAgendaConsul")){
					return aam_mapping.findForward("horarioConsultorio");
				}
				else
				{
					//Se verifica si hay horarios sin consultorio para saber a cual forward enviar
					if(Integer.parseInt(aaf_form.getHoraAtencionConMap("numRegistros").toString())>0)
						return aam_mapping.findForward("horarioConsultorio");
					else
						return aam_mapping.findForward("agenda");
				}
			}
			else if(cod == -4)
			{
				
				ActionErrors lae_errores;

				/* Cerrar la conexion a base de datos */
				cerrarConexion(lc_con);

				/* Error generar la agenda de consultas */
				if(logger.isDebugEnabled() )
					logger.debug("No se pudo generar la agenda de consultas");

				lae_errores = new ActionErrors();				
				
				String[] mensaje = la_agenda.getParcial().split(ConstantesBD.separadorSplit);
				for(int i=0;i<mensaje.length;i++)
					lae_errores.add("codigoMedico", new ActionMessage("error.agenda.horarioTraslapado",mensaje[i]));
				saveErrors(ahsr_request, lae_errores);
				
				if(aaf_form.getEstado().equals("generarAgendaConsul"))
					return aam_mapping.findForward("horarioConsultorio");
				else
				{
					//Se verifica si hay horarios sin consultorio para saber a cual forward enviar
					if(Integer.parseInt(aaf_form.getHoraAtencionConMap("numRegistros").toString())>0)
					{
						return aam_mapping.findForward("horarioConsultorio");
					}
					else
						return aam_mapping.findForward("agenda");
				}
				
			}
			else
			{
				/* Cerrar la conexion a base de datos */
				cerrarConexion(lc_con);
				if(aaf_form.getEstado().equals("generarAgendaConsul"))
				{
					aaf_form.setEstadoHorarioConsul("generarAgendaConsul");
					aaf_form.setEstado("generarAgenda");
					return aam_mapping.findForward("confirmacionAgenda");
				}
				else
				{
					aaf_form.setEstadoHorarioConsul("");
					aaf_form.setEstado("generarAgenda");
					//Se verifica si hay horarios sin consultorio para saber a cual forward enviar
					if(Integer.parseInt(aaf_form.getHoraAtencionConMap("numRegistros").toString())>0)
					{
						ActionErrors errores = new ActionErrors();
						errores.add("",new ActionMessage("error.agenda.mensajeConfirmacion","generados: "+cod));
						saveErrors(ahsr_request, errores);
						
						return aam_mapping.findForward("horarioConsultorio");
					}
					else
						return aam_mapping.findForward("confirmacionAgenda");
				}
			}
		}
		catch(Exception le_e)
		{
			ActionErrors lae_errores;

			/* Cerrar la conexion a base de datos */
			cerrarConexion(lc_con);

			/* Error generar la agenda de consultas */
			if(logger.isDebugEnabled() )
				logger.debug("No se pudo generar la agenda de consultas");

			lae_errores = new ActionErrors();
			
			logger.debug("\nError capturado --->"+le_e.getMessage()+"\n");
			lae_errores.add("codigoMedico", new ActionMessage(le_e.getMessage() ) );
			saveErrors(ahsr_request, lae_errores);
			
			if(aaf_form.getEstado().equals("generarAgendaConsul"))
				return aam_mapping.findForward("horarioConsultorio");
			else
			{
				//Se verifica si hay horarios sin consultorio para saber a cual forward enviar
				if(Integer.parseInt(aaf_form.getHoraAtencionConMap("numRegistros").toString())>0)
				{
					return aam_mapping.findForward("horarioConsultorio");
				}
				else
					return aam_mapping.findForward("agenda");
			}
		}
	}
	
	
	

	/** Inicia el la forma de agenda de consultas y establece su estado 
	 * @param ahsr_request */
	private ActionForward iniciarAgenda(
		ActionMapping	aam_mapping,
		HttpServletRequest ahsr_request, 
		AgendaForm		aaf_form,
		String			as_estado,
		int				ai_estado,
		UsuarioBasico usuario
	)throws Exception
	{
		

		Connection	con;
		if( (con = abrirConexion(false) ) == null)
			return error(aam_mapping, ahsr_request, "errors.problemasBd", true);
		
		if(ai_estado != ii_PREPARAR_LISTADO_AGENDA && ai_estado != ii_CAMBIAR_CENTRO_ATENCION){
			aaf_form.reset();
			// Obtener centros de atención validos para el usuario
			//UnidadAgendaUsuarioCentro mundo = new UnidadAgendaUsuarioCentro();
			//aaf_form.setUnidadAgendaMap(mundo.consultaUAP(con, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoNuncaValido, usuario.getLoginUsuario()));
			if(ai_estado == ii_NUEVA_AGENDA )
				aaf_form.setCodigoActividadAutorizacion(ConstantesBD.codigoActividadAutorizadaGenerarAgenda);
			else if(ai_estado==ii_ELIMINAR_AGENDA)
				aaf_form.setCodigoActividadAutorizacion(ConstantesBD.codigoActividadAutorizadaCancelarAgenda);
			
			aaf_form.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuario.getLoginUsuario(), aaf_form.getCodigoActividadAutorizacion()));
			aaf_form.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, usuario.getLoginUsuario(), ConstantesBD.codigoNuncaValido, aaf_form.getCodigoActividadAutorizacion(), ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));
			
		}
		aaf_form.setEstado(as_estado);
		
		if(as_estado.equals("generarAgenda") || as_estado.equals("cancelarAgenda"))
		{
			aaf_form.setEstadoInicial(as_estado);
		}	
		//-----Se asigna el centro de atención del usuario por defecto cuando empieza ---------//
		/*if(ai_estado != ii_CAMBIAR_CENTRO_ATENCION){
			aaf_form.setCentroAtencion(usuario.getCodigoCentroAtencion());
			aaf_form.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, usuario.getLoginUsuario(), aaf_form.getCentroAtencion(), ConstantesBD.codigoNuncaValido));
		}	*/
		
		//-----Se asignan las unidades de agenda del usuario por defecto cuando empieza ---------//
		if(ai_estado == ii_CAMBIAR_CENTRO_ATENCION){
			aaf_form.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, usuario.getLoginUsuario(), aaf_form.getCentroAtencion(), aaf_form.getCodigoActividadAutorizacion(), ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));
		}
		
		//----Se valida que el usuario tenga permisos para entrar a la agenda--------------------------
		if((ai_estado == ii_NUEVA_AGENDA || ai_estado==ii_ELIMINAR_AGENDA) && Integer.parseInt(aaf_form.getCentrosAtencionAutorizados("numRegistros")+"")<=0)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("", new ActionMessage("errors.notEspecific","Usuario sin ningun centro de atención autorizado para ingresar a "+(ai_estado == ii_NUEVA_AGENDA?"generación de agenda":"cancelación de agenda")));
			saveErrors(ahsr_request, errores);
			UtilidadBD.closeConnection(con);
			return aam_mapping.findForward("paginaErroresActionErrors");
		}
		
		//inicia el mapa de Horarios de Atencion sin Consultorios
		aaf_form.setHoraAtencionConMap("numRegistros","0");
		
		cerrarConexion(con);
		return aam_mapping.findForward("agenda");
	}

	/** Lista la agenda de consulta de acuerdo a los parámetros recibidos */
	private ActionForward listarAgenda(
		ActionMapping		mapping,
		AgendaForm			form,
		HttpServletRequest	request
	)throws Exception
	{
		Agenda		agenda;
		Connection	con;
		UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");

		if( (con = abrirConexion(false) ) == null)
			return error(mapping, request, "errors.problemasBd", true);

		try
		{
			/* Copiar la información del formulario a la agenda */
			agenda = new Agenda();
			PropertyUtils.copyProperties(agenda, form);

			agenda.setCentrosAtencion(form.getCentrosAtencionAutorizados().get("todos").toString());
			agenda.setUnidadesAgenda(form.getUnidadesAgendaAutorizadas().get("todos").toString());
			
			/* Obtener los ítems de agenda de consulta a listar */
			form.setItems(agenda.listarAgenda(con, usuario.getCodigoInstitucionInt()) );

			/* Cerrar la conexion a base de datos */
			cerrarConexion(con);

			return mapping.findForward("listarAgenda");
		}
		catch(SQLException lse_e)
		{
			if(logger.isDebugEnabled() )
				logger.debug("No se pudo generar el listado de agenda de consulta");

			lse_e.printStackTrace();
			return error(mapping, request, "errors.problemasBd", true);
		}
	}	
	
	
	
	/**
	 * Valida la información de los Horarios de Atención sin consultorios  	 
	 * @param ActionErrors errores
	 * @param HashMap horarioConsultorio
	 * */
	private ActionErrors validarHorarioConsultorio(ActionErrors errores,HashMap horarioConsultorio)
	{
		int numRegistros = Integer.parseInt(horarioConsultorio.get("numRegistros").toString());
		long horaA = 0;
		long horaB = 0;
		long horaAf = 0;		
		SimpleDateFormat lsdf_horas;
		
		
		/* Exije una interpretación estricta del formato de hora esperado */
		lsdf_horas	= new SimpleDateFormat("H:mm");
		lsdf_horas.setLenient(false);
		
		for(int i = 0; i <numRegistros; i++)
		{
			for(int j = 0; j < numRegistros; j++)
			{
				if( i!= j &&
						!horarioConsultorio.get("consultorio_"+i).equals(ConstantesBD.codigoNuncaValido+"") && 
							!horarioConsultorio.get("consultorio_"+j).equals(ConstantesBD.codigoNuncaValido+""))
				{
					if(horarioConsultorio.get("dia_"+i).equals(horarioConsultorio.get("dia_"+j)))						
					{											
						//Valida el Cruce de Horarios
						if(horarioConsultorio.get("consultorio_"+i).equals(horarioConsultorio.get("consultorio_"+j))) 
						{								
							try
							{
								horaA = lsdf_horas.parse(horarioConsultorio.get("hora_inicio_"+i).toString()).getTime();							
								horaB = lsdf_horas.parse(horarioConsultorio.get("hora_inicio_"+j).toString()).getTime();							
								
								horaAf = lsdf_horas.parse(horarioConsultorio.get("hora_fin_"+i).toString()).getTime();
																
								if(horaA > horaB)
								{					
									horaB = lsdf_horas.parse(horarioConsultorio.get("hora_inicio_"+i).toString()).getTime();									
									
									horaA = lsdf_horas.parse(horarioConsultorio.get("hora_inicio_"+j).toString()).getTime();
									horaAf = lsdf_horas.parse(horarioConsultorio.get("hora_fin_"+j).toString()).getTime();
								}					
								
								
								if(horaB > horaA && horaB < horaAf)								
									errores.add("descripcion",new ActionMessage("errors.notEspecific","Existe Cruce de Horarios para el Registro Numero "+(i+1)+". Y Registro Numero "+(j+1)+"."));									
																	
							}
							catch (ParseException e) {
								e.printStackTrace();
							}
						}
						
						
						if(horarioConsultorio.get("hora_inicio_"+i).equals(horarioConsultorio.get("hora_inicio_"+j)) && 
								horarioConsultorio.get("hora_fin_"+i).equals(horarioConsultorio.get("hora_fin_"+j)))
						{
						
							if(horarioConsultorio.get("codigo_medico_"+i).equals(horarioConsultorio.get("codigo_medico_"+j)))
								errores.add("descripcion",new ActionMessage("errors.notEspecific","Profesional de la Salud, Horario y Día Repetido en el Registro Numero "+(i+1)+". Y Registro Numero "+(j+1)+"."));
							if(horarioConsultorio.get("consultorio_"+i).equals(horarioConsultorio.get("consultorio_"+j))) 
								errores.add("descripcion",new ActionMessage("errors.notEspecific","Consultorio,Horario y Día Repetido en el Registro Numero "+(i+1)+". Y Registro Numero "+(j+1)+"."));
						}
					}
				}	
			}
			
			if(!errores.isEmpty())
				return errores;
		}		
		
		return errores;
	}
	
	
	
	/**
	 * Convierte el día de la semana domingo->7 a uno valido para la BD: domingo->0
	 * MT 1233 
	 * @param diaSemana
	 * @param aaf_form
	 * 
	 * @return diaSemana
	 *
	 * @autor Cristhian Murillo
	 */
	private int convertirDiaSemanaAgendaToBD(int diaSemana, AgendaForm aaf_form)
	{
		if(diaSemana == 7){
			aaf_form.setDiaDomingo(true);
			return 0;
		}
		else{
			aaf_form.setDiaDomingo(false );
			return diaSemana;
		}
	}
	
	
}