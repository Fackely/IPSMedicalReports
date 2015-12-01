/*
 * Mayo 31, 2008
 */
package com.princetonsa.action.consultaExterna;

import java.io.IOException;
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
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.consultaExterna.UtilidadesConsultaExterna;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;

import com.princetonsa.actionform.consultaExterna.ValoracionConsultaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.comun.DtoDatosGenericos;
import com.princetonsa.dto.historiaClinica.DtoFechaInicioFechaFin;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.historiaClinica.Valoraciones;
import com.princetonsa.mundo.ordenesmedicas.RegistroIncapacidades;
import com.servinte.axioma.bl.historiaClinica.facade.HistoriaClinicaFacade;
import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoPacienteDto;
import com.servinte.axioma.dto.historiaClinica.DatosAlmacenarCurvaCrecimientoDto;
import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.fabrica.historiaClinica.HistoriaClinicaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IAntecedentesPacienteMundo;
import com.sysmedica.util.UtilidadFichas;

/**
 * @author Sebastiï¿½n Gï¿½mez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * Valoracion Consulta
 */
public class ValoracionConsultaAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ValoracionConsultaAction.class);
	
	/** Conserva la fecha y la hora inicial de ejecución de la funcionalidad para saber por fechas que registros se guardaron **/
	private DtoFechaInicioFechaFin validacionantecedentesMostrar = new DtoFechaInicioFechaFin();
	
	
	/**
	 * Mï¿½todo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response ) throws Exception
	{
		
		Connection con=null;
		try {

			if (response==null); //Para evitar que salga el warning

			if(form instanceof ValoracionConsultaForm)
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
				ValoracionConsultaForm valoracionForm =(ValoracionConsultaForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");

				String estado=valoracionForm.getEstado(); 
				logger.warn("estado ValoracionConsultaAction-->"+estado);

				if(estado == null)
				{
					valoracionForm.reset();	
					logger.warn("Estado no valido dentro del flujo de VALORACION CONSULTA (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con, valoracionForm, mapping, paciente,usuario,request);
				}
				else if (estado.equals("insertar"))
				{
					return accionInsertar(con,valoracionForm, mapping, paciente, usuario, request);
				}
				else if (estado.equals("modificar"))
				{
					return accionModificar(con,valoracionForm,mapping, paciente, usuario, request);
				}
				else if (estado.equals("resumen")||estado.equals("imprimir"))
				{
					return accionResumen(con,valoracionForm, mapping, usuario, paciente, request);
				}
				//Estado que es llamado desde la hoja obstï¿½trica para recargar la seccion Historia Menstrual (si estï¿½ como componente)
				else if (estado.equals("recargarHistoricoMenstrual"))
				{
					return accionRecargarHistoricoMenstrual(con, valoracionForm,paciente, mapping);
				}
				///Estado para recargar la plantilla cuando se abre popUp
				else if (estado.equals("recargarPlantilla"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seccionesValoresParametrizables");
				}
				else if (estado.equals("volverFlujoEntidades"))
				{
					UtilidadBD.closeConnection(con);
					return accionVolverFlujoEntidades(valoracionForm, response, mapping, request);
				}
				else if (estado.equals("imprimirIncapacidad"))
				{
					request = accionImprimirIncapacidad(con, request, usuario, paciente.getCodigoIngreso());
					return mapping.findForward("resumenConsulta");
				}
				else
				{
					valoracionForm.reset();
					logger.warn("Estado no valido dentro del flujo de ValoracionConsultaAction (null) ");
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

	private HttpServletRequest accionImprimirIncapacidad(Connection con,
			HttpServletRequest request, UsuarioBasico usuario, int codigoIngreso) {
		RegistroIncapacidades mundoIncapacidades = new RegistroIncapacidades();
		return mundoIncapacidades.imprimirIncapacidad(con, request, usuario, codigoIngreso);
	}


	private ActionForward accionVolverFlujoEntidades(ValoracionConsultaForm forma, HttpServletResponse response, ActionMapping mapping,HttpServletRequest request) {
		
		
		try
		{
			String path = "../responderConsultasEntSubcontratadas/responderConsultasEntSubcontratadas.do?estado=buscarSolicitudes";
			forma.reset();
			response.sendRedirect(path);					
		}
		catch(IOException e)
		{
			logger.error("Error al direccionar : "+e);
			ActionErrors errores = new ActionErrors();
			errores.add("",new ActionMessage("error.errorEnBlanco","Error al tratar de abrir listado Solicitudes . Por favor reportar el error al admisnitrador del sistema"));
			saveErrors(request, errores);
			return mapping.findForward("paginaErroresActionErrors");
		}
		
		return null;
		
		
	}



	/**
	 * Mï¿½todo empleado para recargar el histï¿½rico menstrual como peticiï¿½n desde la hoja obstï¿½trica
	 * @param con
	 * @param valoracionForm
	 * @param paciente 
	 * @param mapping
	 * @return
	 */
	private ActionForward accionRecargarHistoricoMenstrual(Connection con, ValoracionConsultaForm valoracionForm, PersonaBasica paciente, ActionMapping mapping) 
	{
		//Esta validaciï¿½n solo se hace cuando se tiene un componente de ginecologï¿½a
		if(valoracionForm.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia))
		{
			//Se recarga el histï¿½rico menstrual dependiendo de la valoraciï¿½n que se estï¿½ manejando
			valoracionForm.getValoracion().setHistoriaMenstrual(UtilidadesHistoriaClinica.cargarHistoriaMenstrual(con, paciente.getCodigoPersona()+"", ""));
		}
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ingresarConsulta");
	}

	/**
	 * Mï¿½todo que realiza la modificaciï¿½n de las observaciones de la valoraciï¿½n
	 * @param con
	 * @param valoracionForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionModificar(Connection con, ValoracionConsultaForm valoracionForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) 
	{
		UtilidadBD.iniciarTransaccion(con);
		ResultadoBoolean resultado = Valoraciones.insertarObservaciones(con, valoracionForm.getValoracion());
		
		if(!resultado.isTrue())
		{
			UtilidadBD.abortarTransaccion(con);
			ActionErrors errores = new ActionErrors();
			errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("modificarConsulta");
		}
		
		UtilidadBD.finalizarTransaccion(con);
		//Se carga el resumen de la valoraciï¿½n
		this.cargarResumenConsulta(con,valoracionForm,usuario,paciente);
		UtilidadBD.closeConnection(con);
		
		valoracionForm.setEsResumenModificacion(true); //se activa variable para indicar que es resumen de modificacion
		return mapping.findForward("resumenConsulta");
	}

	/**
	 * Mï¿½todo que llama el resumen de la valoraciï¿½n
	 * @param con
	 * @param valoracionForm
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 */
	private ActionForward accionResumen(Connection con, ValoracionConsultaForm valoracionForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		//**********************RESUMEN VALORACION CONSULTA	*****************************************************
		//Dependiendo del estado se hacen cosas diferentes
		/*
		* Antes MT6313
		* Tipo Modificacion: Segun incidencia MT6713
		* Autor: Jesús Darío Ríos
		* usuario: jesrioro
		* Fecha: 20/03/2013
		* Descripcion: 	Se  valida  si  viene  de  consulta externa / atender citas.
		* 				Si  no  proviene de  ese flujo, se  hace  el  flujo normal.
		*/
		boolean vieneDeConsultaExterna= valoracionForm.getVieneDeConsultaExterna();
		
			if(valoracionForm.getEstado().equals("resumen"))
			{
				//MT6313  si  no  viene  de consulta externa/Atender citas,  se  deben  mostrar los encabezados
				valoracionForm.setOcultarEncabezado(false);
				if(valoracionForm.getFuncionalidad()!=null && !valoracionForm.getFuncionalidad().equals("submitPorClickEnCurvaCrecimiento"))
				{
					ActionErrors errores = this.validacionesIniciales(con, valoracionForm, usuario,paciente, request);
					
					//Se verifica que todo haya salido bien al cargar el resumen
					if(!errores.isEmpty())
					{
						saveErrors(request, errores);
						UtilidadBD.closeConnection(con);
						if(valoracionForm.isOcultarEncabezado())
							return mapping.findForward("paginaErroresActionErrorsSinCabezote");
						else
							return mapping.findForward("paginaErroresActionErrors");
					}
				}
				//Se carga el resumen de la valoraciï¿½n
				this.cargarResumenConsulta(con,valoracionForm,usuario,paciente);
				UtilidadBD.closeConnection(con);
				if(vieneDeConsultaExterna){
				valoracionForm.setOcultarEncabezado(true);
				}
				return mapping.findForward("resumenConsulta");
			}
			else
			{
				valoracionForm.setVieneDeHistoriaAtenciones(UtilidadTexto.getBoolean(request.getParameter("vieneDeHistoriaAtenciones")));
				if(valoracionForm.isVieneDeHistoriaAtenciones())
				{
					//Cuando viene de historia de atenciones se debe cargar la informacion
					this.validacionesIniciales(con, valoracionForm, usuario, paciente, request);
					this.cargarResumenConsulta(con,valoracionForm,usuario,paciente);
				}
				else
				{
					/**
					 *Solución Alberto Ovalle 
					 *tm 6751 
					 *se corrige ErrorAl ingresar a la funcionalidad historia clinica - 
					 *historia de atenciones - sección consulta externa - valoración,
					 * cuando se consulta el detalle y se imprime la pantalla queda en blanco
					 */
					//Se cargan los datos de la institucion
				    //valoracionForm.getInstitucion().cargar(con, usuario.getCodigoInstitucionInt());
					ParametrizacionInstitucion parametrizacionInstitucion = new ParametrizacionInstitucion();
					parametrizacionInstitucion.cargar(con, usuario.getCodigoInstitucionInt());
					valoracionForm.setInstitucion(parametrizacionInstitucion);
					this.CargarAntecedentesPaciente(paciente,usuario.getCodigoInstitucionInt(),valoracionForm);
					//valoracionForm.setNombreCentroAtencion(usuario.getCentroAtencion());
				}
				
				UtilidadBD.closeConnection(con);
				return mapping.findForward("imprimirConsulta");
			}
		
		
	}

	
	/**
	 * Mï¿½todo implementado para insertar una valoraciï¿½n de consulta
	 * @param con
	 * @param valoracionForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @return
	 * @throws IPSException 
	 * @throws NumberFormatException 
	 */
	private ActionForward accionInsertar(Connection con, ValoracionConsultaForm valoracionForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) throws NumberFormatException, IPSException 
	{
		Valoraciones mundoValoracion = new Valoraciones();
		//******************SE CARGA INFORMACION AL MUNDO***************************************************
		mundoValoracion.setDiagnosticosRelacionados(valoracionForm.getDiagnosticosRelacionados());
		mundoValoracion.setValoracion(valoracionForm.getValoracion());
		mundoValoracion.setPlantilla(valoracionForm.getPlantilla());
		//mundoValoracion.setNumeroAutorizacion("");//Anexo 753, se quito el campo
		mundoValoracion.setCodigoCita(valoracionForm.getCodigoCita());
		mundoValoracion.setPyp(valoracionForm.isPyp());
		mundoValoracion.setUnificarPyp(valoracionForm.isUnificarPyp());
		mundoValoracion.setVieneDePyp(valoracionForm.isVieneDePyp());
		mundoValoracion.setCodEspecialidadProfesionalResponde(valoracionForm.getCodigoEspecialidad()+"");
		//****************************************************************************
		
		UtilidadBD.iniciarTransaccion(con);
		
		//Cambio por tarea 143743 -- Miro si existe medico para la agenda Si medico es 0 es que la agenda no tieneel medico
		int medicoAgenda=UtilidadesConsultaExterna.obtenerCodigoMedicoAgendaXCita(con, Utilidades.convertirAEntero(mundoValoracion.getCodigoCita()));
		int solicitud=UtilidadesConsultaExterna.obtenerSolicitudXCita(con,Utilidades.convertirAEntero(mundoValoracion.getCodigoCita()));
		
		logger.info("MEDICO/CITA/SOLICITUD-------->"+medicoAgenda+"/"+mundoValoracion.getCodigoCita()+"/"+solicitud);
		
		
				
		//********************SE INSERTA VALORACION*************************************************
		valoracionForm.setMensajes(mundoValoracion.insertarConsulta(con, paciente, request, usuario));
		//********************************************************************************************
		
		if(mundoValoracion.getErrores().isEmpty())
		{
			//Se cargan los antecedentes
			CargarTodosAntecedentesPaciente(paciente, usuario.getCodigoInstitucionInt(), valoracionForm);
			
			UtilidadBD.finalizarTransaccion(con);
			
			//******************SE CARGA EL RESUMEN DE LA CONSULTA****************************************
			//Se consulta el resumen de la consulta
			this.cargarResumenConsulta(con,valoracionForm,usuario,paciente);
			//******************************************************************************************
			
			//Se recarga el paciente en sesiï¿½n
			UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
				
			valoracionForm.setEsResumenInsercion(true); //se activa variable para indicar que es resumen de inserciï¿½n
			
			//Agrego validacion por tarea 143743-- Si el medico de la agenda antes de ser atendida la cita no estaba asignado, entonces actualizo el pool de la solicitud
			if(medicoAgenda==0)
			{
				//Traigo el medico que responde la solicitud
				int medicoResponde=UtilidadesConsultaExterna.obtenerCodigoMedicoRespondeSolicitud(con,solicitud);
				logger.info("MEDICO RESPONDE--->"+medicoResponde);
				
				//Traigo los pooles para ese medico segun la fecha de interpretacion (fecha actual)
				@SuppressWarnings("rawtypes")
				ArrayList poolesMedico=Utilidades.obtenerPoolesMedico(con, UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()), medicoResponde);
				logger.info("EL TAMANIO DE LOS POOLES------>"+poolesMedico.size());
				int ultimoPoolAEscoger=poolesMedico.size();
				int pool=Utilidades.convertirAEntero(poolesMedico.get(ultimoPoolAEscoger-1)+"");
				logger.info("EL POOL--->"+pool);
				
				//Actualizo en la solicitud el pool del medico responde
				boolean actualizoPool=Valoraciones.actualizarPoolSolicitudXAgenda(con,solicitud,pool);
				
				logger.info("ACTUALIZO POOL CUANDO EL MEDICO DE LA AGENDA NO ESTA ASIGNADO CON EL POOL DEL MEDICO RESPONDE------->"+actualizoPool);
			}
			
			UtilidadBD.closeConnection(con);
			
			
			//Almacenan las Curvas de Crecimiento modificadas del paciente
			
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
						dtoDatosAlmacenarCurvaCrecimiento.setNumeroSolicitud(Integer.parseInt(valoracionForm.getValoracion().getNumeroSolicitud()));
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
			
			if(valoracionForm.getValoracion().getNumeroSolicitud() != null && !valoracionForm.getValoracion().getNumeroSolicitud().equals(""))
				idValoracion = Integer.valueOf(valoracionForm.getValoracion().getNumeroSolicitud());
			
			if(idValoracion != -1)
			{
				List<HistoricoImagenPlantillaDto> dtohipTemp = new ArrayList<HistoricoImagenPlantillaDto>();
				try {
					dtohipTemp = new HistoriaClinicaFacade().valoracionesPorId(idValoracion);
				} catch (IPSException e) {
					Log4JManager.error(e);
				}
				valoracionForm.setDtoHistoricoImagenPlantilla(dtohipTemp);
				
				valoracionForm.setMostrarDetalles(false);
				if(valoracionForm.getIndiceCurvaSeleccionada()!=null){
					valoracionForm.setCurvaSeleccionada(valoracionForm.getDtoHistoricoImagenPlantilla().get(valoracionForm.getIndiceCurvaSeleccionada()));
					
					Calendar c = Calendar.getInstance();
					c.setTime(valoracionForm.getCurvaSeleccionada().getFecha());
			
					valoracionForm.setEdadCalculada(paciente.calcularEdadEnFechaDetallada(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR)));
					valoracionForm.setMostrarDetalles(true);
					valoracionForm.setIndiceCurvaSeleccionada(null);
				}
			}
			
			return mapping.findForward("resumenConsulta");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			saveErrors(request, mundoValoracion.getErrores());
			UtilidadBD.closeConnection(con);
			return mapping.findForward("ingresarConsulta");
		}
		
	}

	

	/**
	 * Mï¿½todo implementado para consultar el resumen de la consulta
	 * @param con
	 * @param valoracionForm
	 * @param usuario
	 * @param paciente
	 */
	private void cargarResumenConsulta(Connection con, ValoracionConsultaForm valoracionForm, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		//SE carga de nuevo la plantilla con los valores a travï¿½s del codigoPK de la plantilla ingresada
		valoracionForm.setPlantilla(Plantillas.cargarPlantillaXSolicitud(
				con, 
				usuario.getCodigoInstitucionInt(), 
				ConstantesCamposParametrizables.funcParametrizableValoracionConsulta,
				ConstantesBD.codigoNuncaValido, //centro de costo 
				ConstantesBD.codigoNuncaValido, // codigo sexo 
				ConstantesBD.codigoNuncaValido, // especialidad 
				Plantillas.obtenerCodigoPlantillaXIngreso(con, ConstantesBD.codigoNuncaValido, paciente.getCodigoPersona(), Integer.parseInt(valoracionForm.getNumeroSolicitud())),
				true, 
				paciente.getCodigoPersona(), 
				ConstantesBD.codigoNuncaValido, //codigo inngreso no aplica
				Integer.parseInt(valoracionForm.getNumeroSolicitud()),
				ConstantesBD.codigoNuncaValido,
				ConstantesBD.codigoNuncaValido,
				false
				));
		
		Valoraciones mundoValoracion = new Valoraciones();
		mundoValoracion.setPlantilla(valoracionForm.getPlantilla());
		//SE carga de nuevo la valoracion
		mundoValoracion.cargarConsulta(con, paciente,valoracionForm.getNumeroSolicitud(), valoracionForm.getCodigoCita());
		
		//Se cargan los datos a la forma
		valoracionForm.setValoracion(mundoValoracion.getValoracion());		
		valoracionForm.setFechaConsulta(mundoValoracion.getFechaConsulta());
		valoracionForm.setHoraConsulta(mundoValoracion.getHoraConsulta());
		valoracionForm.setDiagnosticosRelacionados(mundoValoracion.getDiagnosticosRelacionados());
		
		
		//Curvas de crecimiento
		int idValoracion = -1;
		
		if(valoracionForm.getValoracion().getNumeroSolicitud() != null && !valoracionForm.getValoracion().getNumeroSolicitud().equals(""))
			idValoracion = Integer.valueOf(valoracionForm.getValoracion().getNumeroSolicitud());
			
		if(idValoracion != -1)
		{
			List<HistoricoImagenPlantillaDto> dtohipTemp = new ArrayList<HistoricoImagenPlantillaDto>();
			try {
				dtohipTemp = new HistoriaClinicaFacade().valoracionesPorId(idValoracion);
			} catch (IPSException e) {
				Log4JManager.error(e);
			}
			valoracionForm.setDtoHistoricoImagenPlantilla(dtohipTemp);
			
			valoracionForm.setMostrarDetalles(false);
			if(valoracionForm.getIndiceCurvaSeleccionada()!=null){
				valoracionForm.setCurvaSeleccionada(valoracionForm.getDtoHistoricoImagenPlantilla().get(valoracionForm.getIndiceCurvaSeleccionada()));
				
				Calendar c = Calendar.getInstance();
				c.setTime(valoracionForm.getCurvaSeleccionada().getFecha());
		
				valoracionForm.setEdadCalculada(paciente.calcularEdadEnFechaDetallada(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR)));
				valoracionForm.setMostrarDetalles(true);
				valoracionForm.setIndiceCurvaSeleccionada(null);
			}
		}
		//Fin curvas de crecimiento
	}

	/**
	 * Mï¿½todo implementado para iniciar el flujo de la valoraciï¿½n de consulta
	 * @param con
	 * @param valoracionForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionForward accionEmpezar(Connection con, ValoracionConsultaForm valoracionForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) 
	{
		
		this.validacionantecedentesMostrar.iniciarDatos();
		
		//*****************VALIDACIONES INICIALES**********************************************************
		ActionErrors errores = validacionesIniciales(con,valoracionForm,usuario,paciente,request);
		//**************************************************************************************************
	
		//Si no hay errores se prosigue con el flujo
		if(errores.isEmpty())
		{
		
			if(Utilidades.convertirAEntero(valoracionForm.getEstadoHistoriaClinica().getCodigo())==ConstantesBD.codigoEstadoHCSolicitada)
			{
				//***********************************EMPEZAR INSERTAR VALORACIï¿½N CONSULTA************************************
				return accionEmpezarInsertar(con,valoracionForm,mapping,paciente,usuario,request); 
				//***********************************************************************************************************
				
			}
			else if(Utilidades.convertirAEntero(valoracionForm.getEstadoHistoriaClinica().getCodigo())==ConstantesBD.codigoEstadoHCInterpretada)
			{
				//*****************************EMPEZAR MODIFICAR VALORACIï¿½N CONSULTA******************************************
				return accionEmpezarModificar(con,valoracionForm,mapping,paciente,usuario,request);
				//*************************************************************************************************************
			}
		}
		
		saveErrors(request, errores);
		UtilidadBD.closeConnection(con);
		if(valoracionForm.isOcultarEncabezado())
			return mapping.findForward("paginaErroresActionErrorsSinCabezote");
		else
			return mapping.findForward("paginaErroresActionErrors");
	}

	/**
	 * Mï¿½todo que inicia la pï¿½gina de modificaciï¿½n de la valoraciï¿½n
	 * @param con
	 * @param valoracionForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezarModificar(Connection con, ValoracionConsultaForm valoracionForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) 
	{
		//Se carga la informaciï¿½n de la valoraciï¿½n
		this.cargarResumenConsulta(con, valoracionForm, usuario, paciente);

		//Se verifica que se hayan encontrado secciones fijas
		if(valoracionForm.getPlantilla().getSeccionesFijas().size()==0)
		{
			ActionErrors errores = new ActionErrors();
			errores.add("",new ActionMessage("errors.noExiste2","parametrizaciï¿½n de secciones fijas para la Valoraciï¿½n de Urgencias. Por favor verifique"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaErroresActionErrors");
			
		}
		
		//**********SE ALISTAN LAS NUEVAS OBSERVACIONES*****************************************************
		//Lo que se hace aquï¿½ es permitir capturar mas informaciï¿½n a los campos de la secciï¿½n observaciones
		//Solo sirve cuando el usuario es un profesional de la salud
		if(UtilidadValidacion.esProfesionalSalud(usuario))
		{
			valoracionForm.setPuedoModificar(true);
			Valoraciones mundoValoracion = new Valoraciones();
			mundoValoracion.prepararNuevasObservaciones(usuario, valoracionForm.getValoracion());
		}
		else
			valoracionForm.setPuedoModificar(false);
		//********************************************************************************************
		
		
		//Curvas de crecimiento
		int idValoracion = -1;
		
		if(valoracionForm.getValoracion().getNumeroSolicitud() != null && !valoracionForm.getValoracion().getNumeroSolicitud().equals(""))
			idValoracion = Integer.valueOf(valoracionForm.getValoracion().getNumeroSolicitud());
		
		if(idValoracion != -1)
		{
			List<HistoricoImagenPlantillaDto> dtohipTemp = new ArrayList<HistoricoImagenPlantillaDto>();
			try {
				dtohipTemp = new HistoriaClinicaFacade().valoracionesPorId(idValoracion);
			} catch (IPSException e) {
				Log4JManager.error(e);
			}
			valoracionForm.setDtoHistoricoImagenPlantilla(dtohipTemp);
			
			valoracionForm.setMostrarDetalles(false);
			if(valoracionForm.getIndiceCurvaSeleccionada()!=null){
				valoracionForm.setCurvaSeleccionada(valoracionForm.getDtoHistoricoImagenPlantilla().get(valoracionForm.getIndiceCurvaSeleccionada()));
				
				Calendar c = Calendar.getInstance();
				c.setTime(valoracionForm.getCurvaSeleccionada().getFecha());
		
				valoracionForm.setEdadCalculada(paciente.calcularEdadEnFechaDetallada(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR)));
				valoracionForm.setMostrarDetalles(true);
				valoracionForm.setIndiceCurvaSeleccionada(null);
			}
		}
		//Fin curvas de crecimiento
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("modificarConsulta");
	}

	/**
	 * Mï¿½todo implementado para empezar a insertar la valoraciï¿½n de consulta
	 * @param con
	 * @param valoracionForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezarInsertar(Connection con, ValoracionConsultaForm valoracionForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, HttpServletRequest request) 
	{
		
		Valoraciones mundoValoraciones = new Valoraciones();
		
		///Se eliminan las fichas de epidemiologï¿½a inactivas del paciente
		UtilidadFichas.eliminarFichasInactivas(con, usuario.getLoginUsuario(), paciente.getCodigoPersona());
		
		//Se elimina lo temporal que haya del registro de incapacidades
		RegistroIncapacidades registro = new RegistroIncapacidades();
		String idIngreso = UtilidadesOrdenesMedicas.obtenerIngresoXNumeroSolicitud(con, valoracionForm.getNumeroSolicitud());
		if(registro.eliminarIncapacidadesInactivasXRegistro(con, Integer.parseInt(idIngreso)).equals(ConstantesBD.acronimoSi))
		{
			logger.info("se han eliminmado satisfactoriamente los registros de incapacidades con campo activo N");
		}
			
		if(registro.consultarIncapacidadesCambios(con, Integer.parseInt(idIngreso)).equals(ConstantesBD.acronimoSi))
		{
			logger.info("se han reversado satisfactoriamente los registros de incapacidades con campo activo C");
		}
		
		
		
		//Se elimina el mapa que venga del flujo de pyp
		 request.getSession().removeAttribute("mapaPYPConsultaExterna");
		
		 //Se cargan las plantillas
		 valoracionForm.setPlantilla(Plantillas.cargarPlantillaXSolicitud(
					con, 
					usuario.getCodigoInstitucionInt(), 
					ConstantesCamposParametrizables.funcParametrizableValoracionConsulta, 
					ConstantesBD.codigoNuncaValido, //centro costo 
					ConstantesBD.codigoNuncaValido, //sexo 
					valoracionForm.getCodigoEspecialidad(),
					ConstantesBD.codigoNuncaValido, //codigo plantilla PK
					false, //no se consulta informaciï¿½n 
					paciente.getCodigoPersona(), 
					ConstantesBD.codigoNuncaValido, //ingreso 
					Integer.parseInt(valoracionForm.getNumeroSolicitud()),
					paciente.getCodigoSexo(),
					UtilidadFecha.numeroDiasEntreFechas(paciente.getFechaNacimiento(), UtilidadFecha.getFechaActual(con)),
					true
					));
		 
		 try {
			 valoracionForm.setCheckVisibilidadEnlaceOrdenesAmbulatorias(Plantillas.consultarVisibilidadPlantillaFijaSinOrdenConsultaExterna(con, ConstantesBD.codigoNuncaValido, valoracionForm.getCodigoEspecialidad()));
		} catch (SQLException e) {
			logger.error("error consultando estado check "+e.getMessage());
		}
		 
		 //Se verifica que se hayan encontrado secciones fijas
		if(valoracionForm.getPlantilla().getSeccionesFijas().size()==0)
			mundoValoraciones.getErrores().add("",new ActionMessage("errors.noExiste2","parametrizaciï¿½n de secciones fijas para la Valoraciï¿½n de Consulta. Por favor verifique"));
		else
			valoracionForm.getPlantilla().cargarEdadesPaciente(paciente);
		 
		logger.info("\n\n\n\n\n--------->"+valoracionForm.getCodigoCita()+"\n\n\n\n\n");
		 //Se preaparan los datos de la valoraciï¿½n
		mundoValoraciones.setPlantilla(valoracionForm.getPlantilla());
		mundoValoraciones.precargarBaseConsulta(
			con, 
			paciente, 
			usuario, 
			valoracionForm.getNumeroSolicitud(), 
			valoracionForm.isVieneDePyp(), 
			valoracionForm.getFinalidad(), 
			valoracionForm.getCodigoCita());
		
		//********************SE CARGAN LOS DATOS A LA FORMA***********************************
		valoracionForm.setValoracion(mundoValoraciones.getValoracion());
		//Se cargan arreglos
		valoracionForm.setCausasExternas(mundoValoraciones.getCausasExternas());
		valoracionForm.setFinalidades(mundoValoraciones.getFinalidades());
		valoracionForm.setTiposDiagnostico(mundoValoraciones.getTiposDiagnostico());
		
		//Se verifica componentes
		if(valoracionForm.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteGinecologia))
		{
			valoracionForm.setRangosEdadMenarquia(mundoValoraciones.getRangosEdadMenarquia());
			valoracionForm.setRangosEdadMenopausia(mundoValoraciones.getRangosEdadMenopausia());
			valoracionForm.setConceptosMenstruacion(mundoValoraciones.getConceptosMenstruacion());
		}
		//Se cargan advertencias
		valoracionForm.setAdvertencias(mundoValoraciones.getAdvertencias());
		//Se cargan indicadores de flujo pyp
		valoracionForm.setPyp(mundoValoraciones.isPyp());
		valoracionForm.setUnificarPyp(mundoValoraciones.isUnificarPyp());
		//Se cargan datos de la solicitud/cita		
		valoracionForm.setFechaConsulta(mundoValoraciones.getFechaConsulta());
		valoracionForm.setHoraConsulta(mundoValoraciones.getHoraConsulta());
		//**********************************************************************************
		
		
		//*********SE VERIFICA SI SE DEBE MOSTRRA BOTON "OTROS SERVICIOS"*********************
		//Se verifica si se debe mostrar el link de otros servicios de la cita
		if(
			!valoracionForm.isVieneDePyp()&&
			UtilidadValidacion.esValidoPacienteCargado(con, paciente).puedoSeguir&&
			UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getAcronimo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto)&&
			UtilidadesConsultaExterna.deboMostrarOtrosServiciosCita(con, valoracionForm.getCodigoCita()+""))
		{
			valoracionForm.setDeboMostrarOtrosServiciosCita(true);
		    valoracionForm.setDeboMostrarVolverlistadoCitas(true);
	    }
		else
		{
			
			valoracionForm.setDeboMostrarOtrosServiciosCita(false);
		}
		logger.info("\n\n\n\n\n--------->"+valoracionForm.getCodigoCita()+"\n\n\n\n\n");
		if(	!valoracionForm.isVienedeEntidadesSubcontratadas()&&
			UtilidadValidacion.esValidoPacienteCargado(con, paciente).puedoSeguir&&
			UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getAcronimo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto)&&
			UtilidadesConsultaExterna.deboMostrarOtrosServiciosCita(con, valoracionForm.getCodigoCita()+""))	
			{
				valoracionForm.setDeboMostrarOtrosServiciosCita(true);
			    valoracionForm.setDeboMostrarVolverlistadoCitas(true);
		    }
			else
			{				
				valoracionForm.setDeboMostrarVolverlistadoCitas(false);				
				valoracionForm.setDeboMostrarOtrosServiciosCita(false);
			}
		
		
		//************************************************************************************
		
		UtilidadBD.closeConnection(con);
		
		//Se hubo errores 
		if(!mundoValoraciones.getErrores().isEmpty())
		{
			saveErrors(request, mundoValoraciones.getErrores());
			if(valoracionForm.isOcultarEncabezado())
				return mapping.findForward("paginaErroresActionErrorsSinCabezote");
			else
				return mapping.findForward("paginaErroresActionErrors");
		}
		
		return mapping.findForward("ingresarConsulta");
	}

	/**
	 * Mï¿½todo implementado para realizar las validaciones iniciales
	 * @param con
	 * @param valoracionForm
	 * @param usuario
	 * @param paciente 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionErrors validacionesIniciales(Connection con, ValoracionConsultaForm valoracionForm, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		try{
		
		//Se realiza reset de la forma
		valoracionForm.reset(
			request.getParameter("numeroSolicitud"), 
			request.getParameter("codigoCita"),
			UtilidadTexto.getBoolean(request.getParameter("ocultarEncabezado")), //indicativo de ocultar encabezado
			UtilidadTexto.getBoolean(request.getParameter("vieneDePyp")), //indicativo de viene de pyp
			//datos de la finalidad (Aplica solo para pyp)
			new InfoDatosString(
				request.getParameter("codigoFinalidadConsulta")==null?"":request.getParameter("codigoFinalidadConsulta"),
				request.getParameter("nombreFinalidadConsulta")==null?"":request.getParameter("nombreFinalidadConsulta")
			),
			//Datos de la especialidad
			new InfoDatosInt(Utilidades.convertirAEntero(request.getParameter("codigoEspecialidad"))),
			UtilidadTexto.getBoolean(request.getParameter("vieneDeHistoriaAtenciones")), //indicativo de viene de historia atenciones
			UtilidadTexto.getBoolean(request.getParameter("vienedeEntidadesSubcontratadas")),
			request.getParameter("codigoPacienteSolicitudEntSub"));
		
		//Se verifica que el usuario no se atienda a ï¿½l mismo
		ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
		if(respuesta.isTrue())
			errores.add("", new ActionMessage(respuesta.getDescripcion()));
		
		//Se verifica el estado de historia clï¿½nica de la solicitud
		valoracionForm.setEstadoHistoriaClinica(Utilidades.obtenerEstadoHC(con, valoracionForm.getNumeroSolicitud()));
		//Se consulta el nombre de la especialidad del Profesional que responde
		if(valoracionForm.getCodigoEspecialidad()>0)
			valoracionForm.setNombreEspecialidad(Utilidades.getNombreEspecialidad(con, valoracionForm.getCodigoEspecialidad()));
		
		//Se verifica que haya llegado solicitud
		if(valoracionForm.getNumeroSolicitud().equals(""))
			errores.add("", new ActionMessage("errors.problemasGenericos","obteniendo el número de la solicitud a atender"));
		//Se verifica que la solicitud tenga el estado de historia clï¿½nica correcto
		else if(!valoracionForm.getEstadoHistoriaClinica().getCodigo().equals(ConstantesBD.codigoEstadoHCSolicitada+"")&&!valoracionForm.getEstadoHistoriaClinica().getCodigo().equals(ConstantesBD.codigoEstadoHCInterpretada+""))
			errores.add("", new ActionMessage("errors.invalid","el estado médico de la solicitud ("+valoracionForm.getEstadoHistoriaClinica().getNombre()+") "));
		
		//Se verifica que haya llegado el codigo de la cita si no es del flujo de pyp ni del de historia de atenciones, ni de entidades Subcontratadas
		if(!valoracionForm.isVieneDePyp()&&!valoracionForm.isVieneDeHistoriaAtenciones()&&!valoracionForm.isVienedeEntidadesSubcontratadas() && valoracionForm.getCodigoCita().equals(""))
			errores.add("", new ActionMessage("errors.required","El codigo de la cita"));
		
		//Se verifica que se haya ingreso la especialidad del servicio 
		if(valoracionForm.getCodigoEspecialidad()<0)
			errores.add("", new ActionMessage("errors.required","La especialidad del servicio"));
		
		//Se verifica que haya usuario cargado en sesion
		if(usuario==null||usuario.getCodigoPersona()<=0)
			errores.add("", new ActionMessage("errors.usuario.noCargado"));
		//Se verifica que el usuario sea profesional de la salud cuando la solicitud aï¿½n estï¿½ en estado solicitada
		else if(!UtilidadValidacion.esProfesionalSalud(usuario)&&valoracionForm.getEstadoHistoriaClinica().getCodigo().equals(ConstantesBD.codigoEstadoHCSolicitada+""))
			errores.add("", new ActionMessage("errors.noProfesionalSalud"));
		
		
		if(valoracionForm.isVienedeEntidadesSubcontratadas())
		{	
			if(!valoracionForm.getCodigoPacienteSolicitudEntSub().equals("") )
			 {
				paciente.setCodigoPersona(Utilidades.convertirAEntero(valoracionForm.getCodigoPacienteSolicitudEntSub()));
				UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
			 }else
			 {
				 if(paciente==null||paciente.getCodigoPersona()<=0)
					{
						errores.add("", new ActionMessage("errors.paciente.noCargado"));
					}
			 }
			
		}else
		  {
			//Se verifica que el paciente estï¿½ cargado en sesiï¿½n
			if(paciente==null||paciente.getCodigoPersona()<=0)
			{
				errores.add("", new ActionMessage("errors.paciente.noCargado"));
			}
			
		  }
		}catch (Exception e) {
			errores.add("", new ActionMessage("errors.paciente.noCargado"));
		}
		return errores;
	}
	
	
	
	

	/**
	 * Carga todos los antecedentes del paciente
	 * @param paciente
	 * @param codInstitucion
	 * @param valoracionForm
	 *
	 * @autor Cristhian Murillo
	 */
	private void CargarTodosAntecedentesPaciente(PersonaBasica paciente, int codInstitucion,  ValoracionConsultaForm valoracionForm)
	{
		IAntecedentesPacienteMundo antecedentesPacienteMundo = HistoriaClinicaFabricaMundo.crearAntecedentesPacienteMundo();
		ArrayList<DtoDatosGenericos> listaTodosAntecedentes = new ArrayList<DtoDatosGenericos>();
		valoracionForm.setListaAntecedentes(new ArrayList<DtoDatosGenericos>());
		this.validacionantecedentesMostrar.finalizarDatos();
		//this.validacionantecedentesMostrar.setFechaInicio(UtilidadFecha.conversionFormatoFechaStringDate("10/05/2011"));
		
		try {
			//llamar los métodos
			listaTodosAntecedentes.addAll(antecedentesPacienteMundo.obtenerListaTodosAntecedentesPaciente(paciente.getCodigoPersona(), codInstitucion));
			//valoracionForm.setListaAntecedentes(listaAntecedentes);
			
		} catch (SQLException e) {
			Log4JManager.error("Error obteniendo los antecedentes del paciente con codigo: "+paciente.getCodigoPersona(), e);
		}
		
		
		//Se filtran por fechas para mostrar;
		valoracionForm.setListaAntecedentes(new ArrayList<DtoDatosGenericos>());
		//listaAntecedentesMostrar
		
		for (DtoDatosGenericos dtoDatosGenericos : listaTodosAntecedentes) 
		{
			if(!Utilidades.isEmpty(dtoDatosGenericos.getListaDatos()))
			{
				for (DtoDatosGenericos dtoDatosGenericosDet : dtoDatosGenericos.getListaDatos()) 
				{
					// Comparamos: Fecha y hora igual o mayor 
					if(dtoDatosGenericosDet.getFecha() != null && !UtilidadTexto.isEmpty(dtoDatosGenericosDet.getHora()))
					{
						if( dtoDatosGenericosDet.getFecha().equals(this.validacionantecedentesMostrar.getFechaInicio()) 
								|| dtoDatosGenericosDet.getFecha().after(this.validacionantecedentesMostrar.getFechaInicio()))
						{
							if(dtoDatosGenericosDet.getHora().compareTo(this.validacionantecedentesMostrar.getHoraInicio()) >= 0)
							{
								dtoDatosGenericosDet.setFlag(true);
								dtoDatosGenericos.setFlag(true);
							}
						}
					}
				}
			}
		}
		
		valoracionForm.setListaAntecedentes(listaTodosAntecedentes);
		
	}
	/**
	 * Alberto Ovalle
	 * Solucion mt6751
	 * @param paciente
	 * @param codInstitucion
	 * @param valoracionForm
	 */
	public void CargarAntecedentesPaciente(PersonaBasica paciente, int codInstitucion,  ValoracionConsultaForm valoracionForm){
		ArrayList<DtoDatosGenericos> listaTodosAntecedentes = new ArrayList<DtoDatosGenericos>();
		IAntecedentesPacienteMundo antecedentesPacienteMundo = HistoriaClinicaFabricaMundo.crearAntecedentesPacienteMundo();
		try {
			//llamar los métodos
			listaTodosAntecedentes.addAll(antecedentesPacienteMundo.obtenerListaTodosAntecedentesPaciente(paciente.getCodigoPersona(), codInstitucion));
			valoracionForm.setListaAntecedentes(listaTodosAntecedentes);
		} catch (SQLException e) {
			Log4JManager.error("Error obteniendo los antecedentes del paciente con codigo en el metodo CargarAntecedentesPaciente : "+paciente.getCodigoPersona(), e);
		}
		
		
	}
	
	
	
}
