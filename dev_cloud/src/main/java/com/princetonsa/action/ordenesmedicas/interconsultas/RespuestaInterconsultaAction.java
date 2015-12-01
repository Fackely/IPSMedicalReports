/*
 * Junio 3, 2008
 */
package com.princetonsa.action.ordenesmedicas.interconsultas;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import util.ConstantesCamposParametrizables;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;

import com.princetonsa.actionform.ordenesmedicas.interconsultas.RespuestaInterconsultaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.SignoVital;
import com.princetonsa.mundo.historiaClinica.Epicrisis1;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.historiaClinica.Valoraciones;
import com.servinte.axioma.bl.historiaClinica.facade.HistoriaClinicaFacade;
import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoPacienteDto;
import com.servinte.axioma.dto.historiaClinica.DatosAlmacenarCurvaCrecimientoDto;
import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IAurorizacionesEntSubCapitacionServicio;
import com.sysmedica.util.UtilidadFichas;

/**
 * @author Sebastián Gómez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Respuesta Interconsulta
 */
public class RespuestaInterconsultaAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(RespuestaInterconsultaAction.class);
	
	
	
	/**
	 * Servicios necesarios para la implementación de la funcionalidad
	 */
	IAurorizacionesEntSubCapitacionServicio aurorizacionesEntSubCapitacionServicio = ManejoPacienteServicioFabrica.crearAurorizacionesEntSubCapitacionServicio(); 
	IConvenioServicio convenioServicio = FacturacionServicioFabrica.crearConvenioServicio();
	
	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.manejoPaciente.AutorizacionesIngresoEstanciaForm");
	
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute (	ActionMapping mapping,
									ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response ) throws Exception
									{
		Connection con=null;
		try {
			if (response==null); //Para evitar que salga el warning
			if(form instanceof RespuestaInterconsultaForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//OBJETOS A USAR
				RespuestaInterconsultaForm respuestaForm =(RespuestaInterconsultaForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");


				String estado=respuestaForm.getEstado(); 
				logger.warn("estado RespuestaInterconsultaAction-->"+estado);

				if(estado == null)
				{
					respuestaForm.reset();	
					logger.warn("Estado no valido dentro del flujo de RESPUESTA INTERCONSULTA (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con, respuestaForm, mapping, paciente,usuario,request);
				}
				else if (estado.equals("insertar"))
				{
					return accionInsertar(con, respuestaForm, mapping, usuario, paciente, request);
				}
				else if (estado.equals("resumen")||estado.equals("imprimir"))
				{
					return accionResumen(con,respuestaForm, mapping, usuario, paciente, request);
				}
				//Estado que es llamado desde la hoja obstétrica para recargar la seccion Historia Menstrual (si está como componente)
				else if (estado.equals("recargarHistoricoMenstrual"))
				{
					return accionRecargarHistoricoMenstrual(con, respuestaForm, paciente, mapping);
				}
				//Estado para recargar la plantilla cuando se abre popUp
				else if (estado.equals("recargarPlantilla"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seccionesValoresParametrizables");
				}
				else
				{
					respuestaForm.reset();
					logger.warn("Estado no valido dentro del flujo de RespuestaInterconsultaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
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
	 * Método empleado para recargar el histórico menstrual como petición desde la hoja obstétrica
	 * @param con
	 * @param respuestaForm
	 * @param paciente 
	 * @param mapping
	 * @return
	 */
	private ActionForward accionRecargarHistoricoMenstrual(Connection con, RespuestaInterconsultaForm respuestaForm, PersonaBasica paciente, ActionMapping mapping) 
	{
		//Esta validación solo se hace cuando se tiene un componente de ginecología
		if(respuestaForm.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia))
		{
			//Se recarga el histórico menstrual dependiendo de la valoración que se esté manejando
			respuestaForm.getValoracion().setHistoriaMenstrual(UtilidadesHistoriaClinica.cargarHistoriaMenstrual(con, paciente.getCodigoPersona()+"", ""));
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ingresarInterconsulta");
	}

	/**
	 * Método implementado para cargar el resumen de la interconsulta
	 * @param con
	 * @param respuestaForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	private ActionForward accionResumen(Connection con, RespuestaInterconsultaForm respuestaForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		//**********************RESUMEN RESPUESTA INTERCONSULTA	*****************************************************
		//Dependiendo del estado se hacen cosas diferentes
		if(respuestaForm.getEstado().equals("resumen"))
		{
			ActionErrors errores = this.validacionesIniciales(con, respuestaForm, usuario,paciente, request);
			
			//Se verifica que todo haya salido bien al cargar el resumen
			if(!errores.isEmpty())
			{
				saveErrors(request, errores);
				UtilidadBD.closeConnection(con);
				if(respuestaForm.isOcultarEncabezado())
					return mapping.findForward("paginaErroresActionErrorsSinCabezote");
				else
					return mapping.findForward("paginaErroresActionErrors");
			}
			
			//Se carga el resumen de la valoración
			this.cargarResumenInterconsulta(con,respuestaForm,usuario,paciente);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("resumenInterconsulta");
		}
		else
		{
			respuestaForm.setOcultarInfoPaciente(false);
			respuestaForm.setVieneDeHistoriaAtenciones(UtilidadTexto.getBoolean(request.getParameter("vieneDeHistoriaAtenciones")));
			if(respuestaForm.isVieneDeHistoriaAtenciones())
			{
				//Si viene de historia de atenciones se debe cargar la informacion
				this.validacionesIniciales(con, respuestaForm, usuario, paciente, request);
				this.cargarResumenInterconsulta(con,respuestaForm,usuario,paciente);
				respuestaForm.setOcultarInfoPaciente(true);
			}
			else
			{
				///Se cargan los datos de la institucion
				respuestaForm.getInstitucion().cargar(con, usuario.getCodigoInstitucionInt());
				respuestaForm.setNombreCentroAtencion(usuario.getCentroAtencion());
			}
			UtilidadBD.closeConnection(con);
			return mapping.findForward("imprimirInterconsulta");
		}
	}

	/**
	 * Método implementado para insertar una respuesta de interconsulta
	 * @param con
	 * @param respuestaForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	private ActionForward accionInsertar(Connection con, RespuestaInterconsultaForm respuestaForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) throws IPSException 
	{
		//****************VALIDACION CAMPOS*********************************************************************
		ActionErrors errores = validacionIngresar(con,respuestaForm,paciente);
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("ingresarInterconsulta");
		}
		
		// se debe suprimir de valoraciones el atribo numeroAutorizacion
		Valoraciones mundoValoracion = new Valoraciones();
		//******************SE CARGA INFORMACION AL MUNDO***************************************************
		mundoValoracion.setDiagnosticosRelacionados(respuestaForm.getDiagnosticosRelacionados());
		mundoValoracion.setValoracion(respuestaForm.getValoracion());
		mundoValoracion.setPlantilla(respuestaForm.getPlantilla());
		//mundoValoracion.setNumeroAutorizacion("");
		mundoValoracion.setCodigoManejo(respuestaForm.getCodigoManejo());
		mundoValoracion.setAceptaCambioTratante(respuestaForm.getAceptaCambioTratante());
		mundoValoracion.setCodEspecialidadProfesionalResponde(respuestaForm.getEspecialidad().getCodigo()+"");
		//****************************************************************************
		
		//Mt 6339 Responder Interconsultas -parametrización Curvas de Crecimiento
		
		HistoriaClinicaFacade historiaClinicaFacade = new HistoriaClinicaFacade();
		
		HttpSession session=request.getSession();
		@SuppressWarnings("unchecked")
		List<CurvaCrecimientoPacienteDto> curvasCrecimientoPaciente = (List<CurvaCrecimientoPacienteDto>)session.getAttribute("curvasCrecimientoPaciente");
		
		//Verifica que se halla guardado en el componente de Curvas de Crecimiento
		if(curvasCrecimientoPaciente != null){
			//Recorre la Lista de Curvas de Crecimiento del paciente 
			for(CurvaCrecimientoPacienteDto curvaCrecimiento : curvasCrecimientoPaciente){
				//Verifica que la Curva de Crecimiento halla sido modificada
				if(curvaCrecimiento.isGraficaDiligenciada()){
					
					DatosAlmacenarCurvaCrecimientoDto dtoDatosAlmacenarCurvaCrecimiento = new DatosAlmacenarCurvaCrecimientoDto();
					//dtoDatosAlmacenarCurvaCrecimiento.setNumeroSolicitud(Integer.parseInt(respuestaForm.getValoracionUrgencias().getNumeroSolicitud()));
					dtoDatosAlmacenarCurvaCrecimiento.setCodigoCurvaParametrizada(curvaCrecimiento.getDtoCurvaCrecimientoParametrizab().getId());
					dtoDatosAlmacenarCurvaCrecimiento.setImagenBase64(curvaCrecimiento.getImagenBase64());
					dtoDatosAlmacenarCurvaCrecimiento.setCodigoImagenParametrizada(curvaCrecimiento.getDtoCurvaCrecimientoParametrizab().getDtoImagenesParametrizadas().getId());
					dtoDatosAlmacenarCurvaCrecimiento.setCoordenadasPuntos(curvaCrecimiento.getCoordenadasCurvaCrecimiento());
					dtoDatosAlmacenarCurvaCrecimiento.setEsValoracion(true);
					
					try{
						//Se almacena la Curva de Crecimiento asociada a la Valoracion de Urgencias
						historiaClinicaFacade.guardarCurvaCrecimiento(dtoDatosAlmacenarCurvaCrecimiento);
					}
					catch(IPSException ipse){
						Log4JManager.error(ipse);
					}	
				}
			}
			
			//Se elimina la variable en session de curvas de crecimiento
			session.removeAttribute("curvasCrecimientoPaciente");
		}
					
		//Curvas de crecimiento
		int idValoracion = -1;
		
		if(respuestaForm.getNumeroSolicitud() != null && !respuestaForm.getNumeroSolicitud().equals(""))
			idValoracion = Integer.valueOf(respuestaForm.getNumeroSolicitud());
		idValoracion = Integer.valueOf(respuestaForm.getNumeroSolicitud());
		if(respuestaForm.getNumeroSolicitud() != null && !respuestaForm.getNumeroSolicitud().equals(""))
			idValoracion = Integer.valueOf(respuestaForm.getNumeroSolicitud());
		
		if(idValoracion != -1)
		{
			List<HistoricoImagenPlantillaDto> dtohipTemp = new ArrayList<HistoricoImagenPlantillaDto>();
			try {
				dtohipTemp = new HistoriaClinicaFacade().valoracionesPorId(idValoracion);
			} catch (IPSException e) {
				Log4JManager.error(e);
			}
			respuestaForm.setDtoHistoricoImagenPlantilla(dtohipTemp);
			
			respuestaForm.setMostrarDetalles(false);
			if(respuestaForm.getIndiceCurvaSeleccionada()!=null){
				respuestaForm.setCurvaSeleccionada(respuestaForm.getDtoHistoricoImagenPlantilla().get(respuestaForm.getIndiceCurvaSeleccionada()));
				
				Calendar c = Calendar.getInstance();
				c.setTime(respuestaForm.getCurvaSeleccionada().getFecha());
		
				respuestaForm.setEdadCalculada(paciente.calcularEdadEnFechaDetallada(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR)));
				respuestaForm.setMostrarDetalles(true);
				respuestaForm.setIndiceCurvaSeleccionada(null);
			}
		}
		//Fin curvas crecimiento
		
	
		
		
		
		
		

		// Anexo 37 - Cambio 1.50 - Cristhian Murillo
		boolean sinAutorizacionEntidadsubcontratada = false;
		ArrayList<String> listaAdvertencias = new ArrayList<String>();
		
		String numerosolicitud = "";
		if(!UtilidadTexto.isEmpty(mundoValoracion.getNumeroSolicitud())){
			numerosolicitud = mundoValoracion.getNumeroSolicitud();
		}else{
			numerosolicitud = mundoValoracion.getValoracion().getNumeroSolicitud();
		}
		
		listaAdvertencias = 	validacionCapitacion(numerosolicitud, usuario);
	
		if(!Utilidades.isEmpty(listaAdvertencias))
		{
			sinAutorizacionEntidadsubcontratada = true;
		}
		String observacionCapitacion = null;
		if(sinAutorizacionEntidadsubcontratada){
			observacionCapitacion = ConstantesIntegridadDominio.acronimoRegistroSinAutorizacionIngresoEstancia;
		}
		else{
			observacionCapitacion = ConstantesIntegridadDominio.acronimoRegistroConAutorizacionIngresoEstancia;
		}
		//------------------------------------
		
		
		UtilidadBD.iniciarTransaccion(con);
		//********************SE INSERTA VALORACION*************************************************
		mundoValoracion.insertarInterconsulta(con, paciente, request, observacionCapitacion); 
		//********************************************************************************************
		
		if(mundoValoracion.getErrores().isEmpty())
		{
			//insertamos la info automatica de epicrsis
			Epicrisis1.insertarInfoAutomaticaJusServiciosEpicrisis(con, mundoValoracion.getNumeroSolicitud(), usuario, paciente, ConstantesBD.codigoTipoEvolucionEpicrisisInterconsulta, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()), UtilidadFecha.getHoraActual());
			
			UtilidadBD.finalizarTransaccion(con);
			
			//Se asignan las advertencias resultado del proceso
			respuestaForm.setAdvertencias(mundoValoracion.getAdvertencias());
			
			//******************SE CARGA EL RESUMEN DE LA INTERCONSULTA****************************************
			//Se consulta el resumen de la consulta
			this.cargarResumenInterconsulta(con,respuestaForm,usuario,paciente);
			//******************************************************************************************
			
			//Se recarga el paciente en sesión
			UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
				
			UtilidadBD.closeConnection(con);
			return mapping.findForward("resumenInterconsulta");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			saveErrors(request, mundoValoracion.getErrores());
			UtilidadBD.closeConnection(con);
			return mapping.findForward("ingresarInterconsulta");
		}
	}

	/**
	 * Método para cargar el resumen de interconsulta 
	 * @param con
	 * @param respuestaForm
	 * @param usuario
	 * @param paciente
	 */
	private void cargarResumenInterconsulta(Connection con, RespuestaInterconsultaForm respuestaForm, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		//SE carga de nuevo la plantilla con los valores a través del codigoPK de la plantilla ingresada
		respuestaForm.setPlantilla(Plantillas.cargarPlantillaXSolicitud(
				con, 
				usuario.getCodigoInstitucionInt(), 
				ConstantesCamposParametrizables.funcParametrizableValoracionInterconsulta,
				ConstantesBD.codigoNuncaValido, //centro de costo 
				ConstantesBD.codigoNuncaValido, // codigo sexo 
				ConstantesBD.codigoNuncaValido, // especialidad 
				Plantillas.obtenerCodigoPlantillaXIngreso(con, ConstantesBD.codigoNuncaValido, paciente.getCodigoPersona(), Integer.parseInt(respuestaForm.getNumeroSolicitud())),
				true, 
				paciente.getCodigoPersona(), 
				ConstantesBD.codigoNuncaValido, //codigo inngreso no aplica
				Integer.parseInt(respuestaForm.getNumeroSolicitud()),
				ConstantesBD.codigoNuncaValido,
				ConstantesBD.codigoNuncaValido,
				false));
		
		//SE carga de nuevo la valoracion
		Valoraciones mundoValoracion = new Valoraciones();
		mundoValoracion.setPlantilla(respuestaForm.getPlantilla());
		
		mundoValoracion.cargarInterconsulta(con, paciente, respuestaForm.getNumeroSolicitud());
		
		//Se cargan los datos a la forma
		respuestaForm.setValoracion(mundoValoracion.getValoracion());
		respuestaForm.getAdvertencias().addAll(mundoValoracion.getAdvertencias());		
		
		respuestaForm.setDiagnosticosRelacionados(mundoValoracion.getDiagnosticosRelacionados());
		
	}

	/**
	 * Método que realiza las validaciones la ingresar la respuesta de interconsulta
	 * @param con
	 * @param respuestaForm
	 * @param paciente
	 * @return
	 */
	private ActionErrors validacionIngresar(Connection con, RespuestaInterconsultaForm respuestaForm, PersonaBasica paciente) 
	{
		ActionErrors errores = new ActionErrors();
		
		String[] fechaHora = UtilidadesOrdenesMedicas.obtenerFechaHoraSolicitud(con, respuestaForm.getNumeroSolicitud());
		String fechaSolicitud = fechaHora[0];
		String horaSolicitud = fechaHora[1];
		String fechaSistema = UtilidadFecha.getFechaActual(con);
		String horaSistema = UtilidadFecha.getHoraActual(con);
		
		boolean fechaValida = false, horaValida = false;
		
		//Fecha de la consulta
		if(respuestaForm.getValoracion().getFechaValoracion().equals(""))
			errores.add("", new ActionMessage("errors.required","La fecha de la consulta"));
		else if(!UtilidadFecha.validarFecha(respuestaForm.getValoracion().getFechaValoracion()))
			errores.add("", new ActionMessage("errors.formatoFechaInvalido","de la consulta"));
		else
			fechaValida = true;
		
		//Hora de la consulta
		if(respuestaForm.getValoracion().getHoraValoracion().equals(""))
			errores.add("", new ActionMessage("errors.required","La hora de la consulta"));
		else if(!UtilidadFecha.validacionHora(respuestaForm.getValoracion().getHoraValoracion()).puedoSeguir)
			errores.add("", new ActionMessage("errors.formatoHoraInvalido","de la consulta"));
		else
			horaValida = true;
		
		if(fechaValida&&horaValida)
		{
			if(!UtilidadFecha.compararFechas(fechaSistema, horaSistema, respuestaForm.getValoracion().getFechaValoracion(), respuestaForm.getValoracion().getHoraValoracion()).isTrue())
				errores.add("", new ActionMessage("errors.fechaHoraPosteriorIgualActual","de la consulta","del sistema: "+fechaSistema+" - "+horaSistema));
			
			if(!UtilidadFecha.compararFechas(respuestaForm.getValoracion().getFechaValoracion(), respuestaForm.getValoracion().getHoraValoracion(), fechaSolicitud, horaSolicitud).isTrue())
				errores.add("", new ActionMessage("errors.fechaHoraAnteriorIgualActual","de la consulta","de la solicitud: "+fechaSolicitud+" - "+horaSolicitud));
		}
		
		//Se verifica PrimeraVez/Control
		if(respuestaForm.getValoracion().getValoracionConsulta().getPrimeraVez()==null)
			errores.add("",new ActionMessage("errors.required","El campo Primera Vez/Control"));
		
		///Causa externa
		if(respuestaForm.getValoracion().getCodigoCausaExterna()==0)
			errores.add("", new ActionMessage("errors.required","La causa externa"));
		
		//Finalidad de la consulta
		if(respuestaForm.getValoracion().getValoracionConsulta().getCodigoFinalidadConsulta().equals(""))
			errores.add("", new ActionMessage("errors.required","La finalidad de la consulta"));
		
		//Diagnóstico Principal
		if(respuestaForm.getValoracion().getDiagnosticos().get(0).getAcronimo().equals(""))
			errores.add("", new ActionMessage("errors.required","El diagnóstico principal"));
		
		//Tipo Diagnóstico principal
		if(respuestaForm.getValoracion().getValoracionConsulta().getCodigoTipoDiagnostico()==ConstantesBD.codigoNuncaValido)
			errores.add("", new ActionMessage("errors.required","El tipo de diagnóstico principal"));
		
		
		//FEcha proximo control
		if(!respuestaForm.getValoracion().getValoracionConsulta().getFechaProximoControl().equals("")&&
			!UtilidadFecha.validarFecha(respuestaForm.getValoracion().getValoracionConsulta().getFechaProximoControl()))
			errores.add("", new ActionMessage("errors.formatoFechaInvalido","de la fecha de control"));
		
		//Numero de días incapacidad
		if(!respuestaForm.getValoracion().getValoracionConsulta().getNumeroDiasIncapacidad().equals("")&&
			Utilidades.convertirAEntero(respuestaForm.getValoracion().getValoracionConsulta().getNumeroDiasIncapacidad())==ConstantesBD.codigoNuncaValido)
			errores.add("",new ActionMessage("errors.integer","El número de días de incapacidad"));
		
		
		
		
		//Se realizan las validaciones de los campos de la plantilla
		errores = Plantillas.validacionCamposPlantilla(respuestaForm.getPlantilla(), errores);
		
		//************VALIDACIONES COMPONENTES***********************************
		//Se realizan las validaciones de los campos de la historia mentrual
		if(respuestaForm.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia))
			errores = respuestaForm.getValoracion().getHistoriaMenstrual().validate(errores);
		//Se realizan las validaciones de los campos de signos vitales
		if(respuestaForm.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteSignosVitales))
			errores = SignoVital.validate(errores, respuestaForm.getValoracion().getSignosVitales());
		//Se realizan las validaciones de los campos de oftalmología
		if(respuestaForm.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteOftalmologia))
			errores = respuestaForm.getValoracion().getOftalmologia().validate(errores);
		//************************************************************************
		
		return errores;
	}

	/**
	 * Método implementado para iniciar el flujo de la respuesta de interconsulta
	 * @param con
	 * @param respuestaForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, RespuestaInterconsultaForm respuestaForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//*****************VALIDACIONES INICIALES**********************************************************
		ActionErrors errores = validacionesIniciales(con,respuestaForm,usuario,paciente,request);
		//**************************************************************************************************
	
		//Si no hay errores se prosigue con el flujo
		if(errores.isEmpty())
		{
		
			Valoraciones mundoValoraciones = new Valoraciones();
			
			///Se eliminan las fichas de epidemiología inactivas del paciente
			UtilidadFichas.eliminarFichasInactivas(con, usuario.getLoginUsuario(), paciente.getCodigoPersona());
			
			 //Se cargan las plantillas
			 respuestaForm.setPlantilla(Plantillas.cargarPlantillaXSolicitud(
					con, 
					usuario.getCodigoInstitucionInt(), 
					ConstantesCamposParametrizables.funcParametrizableValoracionInterconsulta, 
					ConstantesBD.codigoNuncaValido, //centro costo 
					ConstantesBD.codigoNuncaValido, //sexo 
					respuestaForm.getCodigoEspecialidad(),
					ConstantesBD.codigoNuncaValido, //codigo plantilla PK
					false, //no se consulta información 
					paciente.getCodigoPersona(), 
					ConstantesBD.codigoNuncaValido, //ingreso 
					Integer.parseInt(respuestaForm.getNumeroSolicitud()),
					paciente.getCodigoSexo(),
					UtilidadFecha.numeroDiasEntreFechas(paciente.getFechaNacimiento(), UtilidadFecha.getFechaActual(con)),
					true));
			 
			 //Se verifica que se hayan encontrado secciones fijas
			if(respuestaForm.getPlantilla().getSeccionesFijas().size()==0)
				mundoValoraciones.getErrores().add("",new ActionMessage("errors.noExiste2","parametrización de secciones fijas para la Valoración de Interconsulta. Por favor verifique"));
			else
				respuestaForm.getPlantilla().cargarEdadesPaciente(paciente);
			 
			 //Se preaparan los datos de la valoración
			mundoValoraciones.setPlantilla(respuestaForm.getPlantilla());
			mundoValoraciones.precargarBaseInterconsulta(con, paciente, usuario, respuestaForm.getNumeroSolicitud());
			mundoValoraciones.setCodEspecialidadProfesionalResponde(respuestaForm.getCodigoEspecialidad()+"");
			
			//********************SE CARGAN LOS DATOS A LA FORMA***********************************
			respuestaForm.setValoracion(mundoValoraciones.getValoracion());
			respuestaForm.setDiagnosticosRelacionados(mundoValoraciones.getDiagnosticosRelacionados());
			respuestaForm.setDiagnosticosSeleccionados(mundoValoraciones.getDiagnosticosSeleccionados());
			//Se cargan arreglos
			respuestaForm.setCausasExternas(mundoValoraciones.getCausasExternas());
			respuestaForm.setAdvertencias(mundoValoraciones.getAdvertencias());
			respuestaForm.setFinalidades(mundoValoraciones.getFinalidades());
			respuestaForm.setTiposDiagnostico(mundoValoraciones.getTiposDiagnostico());
			//Si hay componente de ginecologia
			if(respuestaForm.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia))
			{
				respuestaForm.setRangosEdadMenarquia(mundoValoraciones.getRangosEdadMenarquia());
				respuestaForm.setRangosEdadMenopausia(mundoValoraciones.getRangosEdadMenopausia());
				respuestaForm.setConceptosMenstruacion(mundoValoraciones.getConceptosMenstruacion());
			}
			
			//Se cargan datos de la solicitud/cita			
			respuestaForm.setCodigoManejo(mundoValoraciones.getCodigoManejo());
			respuestaForm.setTiposRecargo(mundoValoraciones.getTiposRecargo());
			//Validacion de aceptar cambio tratante
			respuestaForm.setDeboMostrarAceptaCambioTratante(mundoValoraciones.isDeboMostrarAceptaCambioTratante());
			//**********************************************************************************
			
			
			
			UtilidadBD.closeConnection(con);
			
			//Se hubo errores 
			if(!mundoValoraciones.getErrores().isEmpty())
			{
				saveErrors(request, mundoValoraciones.getErrores());
				if(respuestaForm.isOcultarEncabezado())
					return mapping.findForward("paginaErroresActionErrorsSinCabezote");
				else
					return mapping.findForward("paginaErroresActionErrors");
			}
			
			return mapping.findForward("ingresarInterconsulta");
		}
		
		saveErrors(request, errores);
		UtilidadBD.closeConnection(con);
		if(respuestaForm.isOcultarEncabezado())
			return mapping.findForward("paginaErroresActionErrorsSinCabezote");
		else
			return mapping.findForward("paginaErroresActionErrors");
		
	}

	/**
	 * Se realizan validaciones iniciales
	 * @param con
	 * @param respuestaForm
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionErrors validacionesIniciales(Connection con, RespuestaInterconsultaForm respuestaForm, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		
		//Se realiza reset de la forma
		respuestaForm.reset(
			request.getParameter("numeroSolicitud"),
			UtilidadTexto.getBoolean(request.getParameter("ocultarEncabezado")), //indicativo de ocultar encabezado
			UtilidadTexto.getBoolean(request.getParameter("resumenCompacto")), //indicativo para saber si la respuesta se muestra compacta (IFRAME)
			UtilidadTexto.getBoolean(request.getParameter("vieneDeHistoriaAtenciones")) //indicativo para saber si viene de historia de atenciones
			);
		//Se verifica el estado de historia clínica de la solicitud
		try{
			respuestaForm.setEstadoHistoriaClinica(Utilidades.obtenerEstadoHC(con, respuestaForm.getNumeroSolicitud()));
		}catch (Exception e) {
			errores.add("Archivo Sin Datos", new ActionMessage("error.errorEnBlanco", "El archivo no tiene registros. PROCESO CANCELADO."));
		       saveErrors(request, errores);
		}
		respuestaForm.setEspecialidad(UtilidadesOrdenesMedicas.obtenerEspecialidadSolicitadaInterconsulta(con, respuestaForm.getNumeroSolicitud()));
		
		
		//Validación de autoatencion
		ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
		if(respuesta.isTrue())
			errores.add("", new ActionMessage(respuesta.getDescripcion()));
		
		//Se verifica que haya llegado solicitud
		if(respuestaForm.getNumeroSolicitud().equals(""))
			errores.add("", new ActionMessage("errors.problemasGenericos","obteniendo el número de la solicitud a atender (método GET)"));
		
		
		
		//Se verifica la especialidad
		if(respuestaForm.getCodigoEspecialidad()<0)
			errores.add("", new ActionMessage("errors.problemasGenericos",", no se pudo encontrar la especialidad solicitada de la interconsulta"));
		
		//Se verifica que haya usuario cargado en sesion
		if(usuario==null||usuario.getCodigoPersona()<=0)
			errores.add("", new ActionMessage("errors.usuario.noCargado"));
		
		//Valuidaciones que solo aplican para el estado empezar
		if(respuestaForm.getEstado().equals("empezar"))
		{
			//Se verifica que la solicitud tenga el estado de historia clínica correcto
			if(!respuestaForm.getEstadoHistoriaClinica().getCodigo().equals(ConstantesBD.codigoEstadoHCSolicitada+""))
				errores.add("", new ActionMessage("errors.invalid","el estado médico de la solicitud ("+respuestaForm.getEstadoHistoriaClinica().getNombre()+") "));
			
			//Se verifica que el usuario sea profesional de la salud cuando la solicitud aún esté en estado solicitada
			if(!UtilidadValidacion.esProfesionalSalud(usuario))
				errores.add("", new ActionMessage("errors.noProfesionalSalud"));
			
			//Se verifica que el paciente esté cargado en sesión
			if(paciente==null||paciente.getCodigoPersona()<=0)
				errores.add("", new ActionMessage("errors.paciente.noCargado"));
		}	
		
		return errores;
	}
	
	
	
	
	/**
	 * Realiza las validaciones de capitación referentes al cambio número 1.52 del Anexo 37
	 * @param forma
	 * @param usuario
	 * @return ArrayList<String>
	 * @author Cristhian Murillo
	 */
	private ArrayList<String> validacionCapitacion(String numeroSolicitud, UsuarioBasico usuario)
	{
		ArrayList<String> listaAdvertencias = new ArrayList<String>();
		
		UtilidadTransaccion.getTransaccion().begin();
		//String numeroSolicitud = forma.getNumeroSolicitud()+"";
		
		boolean tieneConvenioCapitado 			= false;
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
		
		UtilidadTransaccion.getTransaccion().commit();
		
		return listaAdvertencias;
	}
}
