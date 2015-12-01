/*
* @(#)CancelarCitaAction.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*/
package com.princetonsa.action.agenda;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import util.UtilidadTexto;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.actionform.agenda.CancelarCitaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.Cita;

/**
* Action, controla todas las opciones dentro de la cancelación de la cita, incluyendo los
* posibles casos de error, los casos de flujo.
*
* @version 1.0, Abr 07, 2004
* @author <a href="mailto:edgar@PrincetonSA.com">Edgar Prieto</a>
*
* @see org.apache.struts.action.ActionForm#validate(ActionMapping, HttpServletRequest)
*/
public class CancelarCitaAction extends Action
{
	/** Objeto que permite el manejo de archivos de registro */
	private transient Logger logger = Logger.getLogger(CancelarCitaAction.class);

	/** Definición de constantes del estado del flujo */
	private static final int ii_ESTADO_INVALIDO		= -1;
	private static final int ii_CANCELAR			= 1;
	private static final int ii_LISTAR				= 2;
	private static final int ii_PREPARAR_LISTADO	= 3;
	private static final int ii_CAMBIAR_CENTRO_ATENCION =4;

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

	/**
	*  Cancela las citas macadas con el estado Cancelada por el Paciente o Canceladas por la
	* institución
	 * @param usuarioActual 
	*/
	private ActionForward cancelar(
		ActionMapping mapping,
		CancelarCitaForm citasForm,
		HttpServletRequest request, UsuarioBasico usuarioActual
	)throws Exception
	{
		ActionForward validar;
		Connection con;

		validar =
			validarPaciente(
				mapping,
				request,
				(PersonaBasica)request.getSession().getAttribute("pacienteActivo")
			);

		if(validar != null)
			return validar;

		if( (con = abrirConexion(false) ) == null)
			return error(mapping, request, "errors.problemasBd", true);

		try
		{
			ActionErrors	errores;
			Cita cita;
			int codigoCita;
			int estadoCita;
			boolean cupoLibre;
			Integer aux;

			citasForm.setEstado("listar");

			/* Cancelar las citas solicitadas */
			errores = new ActionErrors();
			
			int elementosEliminados=0;

			for(
				int li_i = 0, li_tam = citasForm.getNumeroItemsSeleccionados();
				li_i < li_tam;
				li_i++
			)
			{
				aux = (Integer)citasForm.getItemSeleccionado("codigoEstadoCita_" + li_i);
				estadoCita = aux.intValue();

				if(
					estadoCita == ConstantesBD.codigoEstadoCitaCanceladaPaciente ||
					estadoCita == ConstantesBD.codigoEstadoCitaCanceladaInstitucion
				)
				{
					aux			= (Integer)citasForm.getItemSeleccionado("codigoCita_" + li_i);
					codigoCita	= aux.intValue();

					/* Iniciar la cita */
					cita = new Cita();

					/* Establecer los datos básicos de la cita */
					cita.setCodigo(codigoCita);
					cita.setCodigoEstadoCita(estadoCita);

					aux = (Integer)citasForm.getItemSeleccionado("codigoAgenda_" + li_i);
					cita.setCodigoAgenda(aux.intValue() );

					cita.setMotivoCancelacion(
						(String)citasForm.getItemSeleccionado("motivoCancelacion_" + li_i)
					);
					
					cita.setCodigoMotivoCancelacion(citasForm.getItemSeleccionado("codigoMotivoCancelacion_"+li_i)+"");
					
					cita.setCodigoUsuario(usuarioActual.getLoginUsuario());
					
					//Se toma el cupo libre
					cupoLibre = UtilidadTexto.getBoolean(citasForm.getItemSeleccionado("cupoLibre_" + li_i).toString());

					if(cita.cancelar(con,cupoLibre, usuarioActual.getLoginUsuario()))
					{
						generarLog(citasForm,li_i,usuarioActual);
						elementosEliminados++;
					}
					else
						errores.add("",
							new ActionMessage(
								"errors.noSeGraboInformacion",
								"DE LA CANCELACIÓN DE LA CITA DE "+citasForm.getItemSeleccionado("nombreUnidadConsulta_"+li_i)+
								" PARA LA FECHA-HORA "+citasForm.getItemSeleccionado("fecha_"+li_i)+" - "+
								UtilidadFecha.convertirHoraACincoCaracteres(citasForm.getItemSeleccionado("horaInicio_"+li_i).toString())));
				}
			}

			citasForm.setCitasCanceladas(elementosEliminados);
			citasForm.setEstado("resumen");
			
			/* Cerrar la conexion a base de datos */
			cerrarConexion(con);

			if(!errores.isEmpty() )
			{
				saveErrors(request, errores);
				return mapping.findForward("cancelarCita");
			}

			return listarCitas(mapping, citasForm, request, usuarioActual);
		}
		catch(SQLException lse_e)
		{
			/* Cerrar la conexion a base de datos */
			cerrarConexion(con);

			if(logger.isDebugEnabled() )
				logger.debug("No se realizar el proceso de cancelación de citas");

			lse_e.printStackTrace();
			return error(mapping, request, "errors.problemasBd", true);
		}
	}

	/**
	 * Método implementado para generar el log de eliminacion de la cancelación de citas
	 * @param citasForm
	 * @param i
	 * @param usuarioActual
	 */
	private void generarLog(CancelarCitaForm citasForm, int i, UsuarioBasico usuarioActual) 
	{
		String  log="\n            ====INFORMACION DE CITA CANCELADA===== " +
	    "\n*  Código [" +citasForm.getItemSeleccionado("codigoCita_"+i)+"] "+
	    "\n*  Unidad Agenda [" +citasForm.getItemSeleccionado("nombreUnidadConsulta_"+i)+"] "+
	    "\n*  Centro Atención [" +citasForm.getItemSeleccionado("nombreCentroAtencion_"+i)+"] "+
	    "\n*  Fecha-Hora [" +citasForm.getItemSeleccionado("fecha_"+i)+"-"+UtilidadFecha.convertirHoraACincoCaracteres(citasForm.getItemSeleccionado("horaInicio_"+i).toString())+"] "+
	    "\n*  Consultorio [" +citasForm.getItemSeleccionado("nombreConsultorio_"+i)+"] "+
	    "\n*  Profesional Salud [" +citasForm.getItemSeleccionado("nombreMedico_"+i)+"] "+
	    "\n*  Estado Cita [" +citasForm.getItemSeleccionado("nombreEstadoCita_"+i)+"] "+
	    "\n*  Motivo Cancelación [" +citasForm.getItemSeleccionado("motivoCancelacion_"+i)+"] "+
		"\n\n------------------------- SERVICIOS DE LA CITA ---------------------------- ";
		
		//**************SERVICIOS DE LA CITA*********************************
		for(int j=0;j<Integer.parseInt(citasForm.getItemSeleccionado("numServicios_"+i).toString());j++)
		{
			log += "\n\n*  Servicio ["+citasForm.getItemSeleccionado("nombreServicio_"+i+"_"+j)+"]";
			log += "\n*  Nro Orden ["+citasForm.getItemSeleccionado("consecutivoOrden_"+i+"_"+j)+"]";
		}
	    
		log+="\n========================================================\n\n\n " ;
		LogsAxioma.enviarLog(ConstantesBD.logCancelacionCitasCodigo, log, ConstantesBD.tipoRegistroLogEliminacion,usuarioActual.getLoginUsuario());
		
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
		ahsr_request.setAttribute(
			lb_parametrizado ? "codigoDescripcionError" : "descripcionError",
			ls_error
		);

		/* Obtener la página de errores */
		return aam_mapping.findForward("paginaError");
	}

	/** Controla el flujo principal de la cancelación de citas */
	public ActionForward execute(
		ActionMapping		aam_mapping,
		ActionForm			aaf_form,
		HttpServletRequest	ahsr_request,
		HttpServletResponse	ahsr_response
	)throws Exception
	{
		/* Validar que el tipo de formulario recibido sea el esperado */
		if(aaf_form instanceof CancelarCitaForm)
		{
			CancelarCitaForm	lccf_form;
			int					li_estado;
			String				ls_estado;

			/* Obtener el estado actual */
			lccf_form = (CancelarCitaForm)aaf_form;
			ls_estado = lccf_form.getEstado();
			logger.warn("[CancelarCitaForm] Estado=>"+ls_estado);
			UsuarioBasico usuarioActual = (UsuarioBasico)ahsr_request.getSession().getAttribute("usuarioBasico");
			
			//Primera Condición: El usuario debe existir
			if( usuarioActual  == null )
			{
				ActionErrors errores = new ActionErrors();
				errores.add("No existe el usuario", new ActionMessage("errors.usuario.noCargado"));
				saveErrors(ahsr_request,errores);
				
			    return aam_mapping.findForward("paginaErroresActionErrors");
			}
			
			/* Validar el estado actual del flujo */
			if(ls_estado == null)
				li_estado = ii_ESTADO_INVALIDO;
			else if(ls_estado.equals("listar") )
				li_estado = ii_LISTAR;
			else if(ls_estado.equals("prepararListado") )
				li_estado = ii_PREPARAR_LISTADO;
			else if(ls_estado.equals("cancelar") )
				li_estado = ii_CANCELAR;
			else if(ls_estado.equals("cambiarCentroAtencion") )
				li_estado = ii_CAMBIAR_CENTRO_ATENCION;
			else
				li_estado = ii_ESTADO_INVALIDO;

			switch(li_estado)
			{
				case ii_ESTADO_INVALIDO:
					/* El estado actual es intederminado y por lo tanto no válido */
					if(logger.isDebugEnabled() )
						logger.debug("Estado inválido en el flujo de agenda de consultas");

					return error(aam_mapping, ahsr_request, "errors.estadoInvalido", true);

				case ii_LISTAR:
					/* Listar las citas del paciente en estado válido para cancelación */
					return listarCitas(aam_mapping, lccf_form, ahsr_request, usuarioActual);

				case ii_PREPARAR_LISTADO:
					/* Prepara el listado de las cistas disponibles para cancelación */
					return iniciar(aam_mapping, lccf_form, ahsr_request,usuarioActual);

				case ii_CANCELAR:
					/* Cancela las citas con estados de cancelación validos */
					return cancelar(aam_mapping, lccf_form, ahsr_request, usuarioActual);
					
				case ii_CAMBIAR_CENTRO_ATENCION:
					return cambiarCentroAtencion(aam_mapping, lccf_form, ahsr_request, usuarioActual);
			}
		}

		/* El formulario recibido no corresponde a un formulario de agenda de consultas */
		if(logger.isDebugEnabled() )
			logger.debug(
				"El formulario actual no corresponde con el formulario esperado por " +
				"cancelación de citas"
			);

		return error(aam_mapping, ahsr_request, "errors.accesoInvalido", true);
	}

	private ActionForward cambiarCentroAtencion(ActionMapping aam_mapping, CancelarCitaForm lccf_form, HttpServletRequest ahsr_request, UsuarioBasico usuarioActual) throws Exception {
		Connection	con;
		if( (con = abrirConexion(false) ) == null)
			return error(aam_mapping, ahsr_request, "errors.problemasBd", true);
		lccf_form.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, usuarioActual.getLoginUsuario(), Integer.parseInt(lccf_form.getCentroAtencion()), ConstantesBD.codigoNuncaValido,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));
		cerrarConexion(con);
		return aam_mapping.findForward("cancelarCita");
	}

	/** Inicia la forma de cancelación de citas y establece su estado de flujo
	 * @param usuarioActual */
	private ActionForward iniciar(	
		ActionMapping		aam_mapping,
		CancelarCitaForm	accf_form,
		HttpServletRequest	ahsr_request, 
		UsuarioBasico usuarioActual
	)throws Exception
	{
		ActionForward	laf_validar;
		int				li_paciente;
		PersonaBasica	lpb_paciente;
		
		Connection	con;
		if( (con = abrirConexion(false) ) == null)
			return error(aam_mapping, ahsr_request, "errors.problemasBd", true);

		/* Iniciar el flujo de cancelación de citas */
		accf_form.reset();

		/* Obtener el paciente actual sesión */
		lpb_paciente	= (PersonaBasica)ahsr_request.getSession().getAttribute("pacienteActivo");

		/* Validar que sobre el paciente se puedan hacer cancelaciones de cita */
		laf_validar = validarPaciente(aam_mapping, ahsr_request, lpb_paciente);

		if(laf_validar != null)
			return laf_validar;

		if( (li_paciente = lpb_paciente.getCodigoPersona() ) > 0)
		{
			/* Obtener el código del paciente */
			accf_form.setCodigoPaciente(li_paciente);

			/* Obtener la cuenta del paciente */
			accf_form.setCuentaPaciente(lpb_paciente.getCodigoCuenta() );
		}
		
		accf_form.setCentroAtencion(usuarioActual.getCodigoCentroAtencion()+"");
		
		// Obtener centros de atención validos para el usuario
		accf_form.setCentrosAtencionAutorizados(UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(con, usuarioActual.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaCancelarCitas));
		accf_form.setUnidadesAgendaAutorizadas(UtilidadesConsultaExterna.unidadesAgendaXUsuario(con, usuarioActual.getLoginUsuario(), Integer.parseInt(accf_form.getCentroAtencion()), ConstantesBD.codigoActividadAutorizadaCancelarCitas,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral));

		cerrarConexion(con);
		return aam_mapping.findForward("cancelarCita");
	}

	/**
	* Carga en la lista de citas a modificar todas las citas del paciente en estado válido para
	* cancelar
	 * @param usuarioActual 
	*/
	private ActionForward listarCitas(
		ActionMapping		aam_mapping,
		CancelarCitaForm	accf_form,
		HttpServletRequest	ahsr_request, UsuarioBasico usuarioActual
	)throws SQLException
	{
		Connection lc_con;

		if( (lc_con = abrirConexion(false) ) == null)
			return error(aam_mapping, ahsr_request, "errors.problemasBd", true);

		try
		{
			String centrosAtencion="";
			String unidadesAgenda="";
			
			//Capturar los centros de atencion si se ha seleccionado la opcion todos
			if (Integer.parseInt(accf_form.getCentroAtencion().toString())==ConstantesBD.codigoNuncaValido){
				centrosAtencion = UtilidadesConsultaExterna.centrosAtencionValidosXUsuario(lc_con, usuarioActual.getLoginUsuario(), ConstantesBD.codigoActividadAutorizadaCancelarCitas).get("todos").toString();
			}
			
			// Capturar las unidades de agenda si se ha seleccionado la opcion todos
			if (accf_form.getCodigoUnidadConsulta()==ConstantesBD.codigoNuncaValido){
				unidadesAgenda = UtilidadesConsultaExterna.unidadesAgendaXUsuario(lc_con, usuarioActual.getLoginUsuario(), Integer.parseInt(accf_form.getCentroAtencion()), ConstantesBD.codigoActividadAutorizadaCancelarCitas,ConstantesIntegridadDominio.acronimoTipoAtencionGeneral).get("todos").toString();
			}
			
			/* Obtener la lista de citas reservadas para el paciente */
			Collection listado=Cita.listar(
								lc_con,
								Cita.BUSCAR_PARA_CANCELACION,
								accf_form.getCodigoPaciente(),
								accf_form.getCodigoUnidadConsulta(),
								accf_form.getCodigoConsultorio(),
								accf_form.getCodigoMedico(),
								"",
								accf_form.getFechaInicio(),
								accf_form.getFechaFin(),
								accf_form.getHoraInicio(),
								accf_form.getHoraFin(),
								accf_form.getCodigoEstadoCita(),
								accf_form.getEstadoLiquidacionCita(),
								accf_form.getCuentaPaciente(),
								accf_form.getCentroAtencion(),
								centrosAtencion,
								unidadesAgenda
								
							);

			
			accf_form.setItemsSeleccionados(lc_con,listado, usuarioActual);
			UtilidadBD.closeConnection(lc_con);
			return aam_mapping.findForward("cancelarCita");

		}
		catch(SQLException lse_e)
		{
			/* Cerrar la conexion a base de datos */
			cerrarConexion(lc_con);

			if(logger.isDebugEnabled() )
				logger.debug("No se pudo generar el listado de citas a cancelar");

			lse_e.printStackTrace();
			return error(aam_mapping, ahsr_request, "errors.problemasBd", true);
		}
	}

	/** Validar que el paciente cumpla todas las condiciones para la cancelación de citas */
	private ActionForward validarPaciente(
		ActionMapping		aam_mapping,
		HttpServletRequest	ahsr_request,
		PersonaBasica		apb_paciente
	)
	{
		if(apb_paciente.getCodigoPersona() < 0)
		{
			/* El paciente no esta cargado en sesión */
			if(logger.isDebugEnabled() )
				logger.debug("El paciente no está cargado en sesión");

			return error(aam_mapping, ahsr_request, "errors.paciente.noCargado", true);
		}

		return null;
	}
}