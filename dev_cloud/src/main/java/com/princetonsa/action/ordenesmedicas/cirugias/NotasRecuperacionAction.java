
/*
 * Creado   24/11/2005
 *
 *	Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.action.ordenesmedicas.cirugias;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.ordenesmedicas.cirugias.NotasRecuperacionForm;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.cirugias.NotasRecuperacion;
import com.princetonsa.mundo.ordenesmedicas.cirugias.ResponderCirugias;

/**
* Clase para manejar el workflow de  
 * las notas de recuperación 
 *
 * @version 1.0, 24/11/2005
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class NotasRecuperacionAction extends Action 
{
    /**
     * manejador de los logs de la clase
     */
    private Logger logger=Logger.getLogger(NotasRecuperacionAction.class);
    
    
    /** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.manejoPaciente.AutorizacionesIngresoEstanciaForm");
	
	
	
    /**
	 * Método execute del action
	 */
	@SuppressWarnings({ "deprecation", "unchecked" })
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		Connection con = null;		    

		try {
			if(form instanceof NotasRecuperacionForm)
			{
				/**Intentamos abrir una conexion con la fuente de datos**/ 
				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				NotasRecuperacionForm formNotasRecuperacion=(NotasRecuperacionForm)form;
				NotasRecuperacion mundo=new NotasRecuperacion();

				HttpSession sesion = request.getSession();			
				PersonaBasica paciente= (PersonaBasica)sesion.getAttribute("pacienteActivo");
				UsuarioBasico usuario= (UsuarioBasico)sesion.getAttribute("usuarioBasico");
				String estado=formNotasRecuperacion.getEstado();
				logger.warn("[NotasRecuperacionAction] estado=>"+formNotasRecuperacion.getEstado());

				Cuenta cuenta=new Cuenta();

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de NotasRecuperacionAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				//Mt 6368 Historico Registro Notas
				if(estado.equals("historicoNota"))
				{
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("historicoRegistro");
				}
				
				else if(estado.equals("submitFrame"))
				{
					UtilidadBD.closeConnection(con);
					return null;
				}			
				else if(estado.equals("empezar") )
				{		


					// Anexo 179 - Cambio 1.50
					String tieneAutoIEstr = request.getParameter("tieneAutoIE");
					if(!UtilidadTexto.isEmpty(tieneAutoIEstr))
					{
						boolean tieneAutoIE = UtilidadTexto.getBoolean(tieneAutoIEstr);

						if(tieneAutoIE)
						{
							formNotasRecuperacion.setSinAutorizacionEntidadsubcontratada(false);
						}
						else
						{
							formNotasRecuperacion.setSinAutorizacionEntidadsubcontratada(true);

							String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.ordenSinAutorizacionIE");

							if(Utilidades.isEmpty(formNotasRecuperacion.getListaAdvertencias())){
								formNotasRecuperacion.setListaAdvertencias(new ArrayList<String>());
							}
							formNotasRecuperacion.getListaAdvertencias().add(mensajeConcreto);
						}
					}

					return metodoEmpezar(con, request, mapping, paciente, usuario, formNotasRecuperacion, mundo);
				}
				else if(estado.equals("guardarNota"))
				{
					cuenta.cargarCuenta(con,(paciente.getCodigoCuenta()+""));
					/**Para guardar la nota el paciente debe tener la cuenta abierta**/
					if(!cuenta.getCodigoEstadoCuenta().trim().equals(""))
					{
						int estadoCuenta=Integer.parseInt(cuenta.getCodigoEstadoCuenta());
						/** Si la cuenta esta abierta el estado debe ser Activa(0) **/
						if(estadoCuenta!=0)
						{
							return ComunAction.accionSalirCasoError(mapping,request,con,logger, "cuenta no activa","errors.paciente.cuentaNoActiva", true);
						}
						else
						{
							@SuppressWarnings("unused")
							ActionForward forward=this.accionGuardarNota(mapping, request, formNotasRecuperacion, con, mundo, usuario, paciente);
							sesion.setAttribute("esResumen", "0");
							return mapping.findForward("paginaNotasRecuperacion");
						}
					}
					else
					{
						return ComunAction.accionSalirCasoError(mapping,request,con,logger, "cuenta no activa","errors.paciente.cuentaNoActiva", true);
					}
				}
				else if(estado.equals("guardarNotaEnfermera"))
				{
					cuenta.cargarCuenta(con,(paciente.getCodigoCuenta()+""));
					/**Para guardar la nota el paciente debe tener la cuenta abierta**/
					if(!cuenta.getCodigoEstadoCuenta().trim().equals(""))
					{
						int estadoCuenta=Integer.parseInt(cuenta.getCodigoEstadoCuenta());
						/**Si la cuenta esta abierta el estado debe ser Activa(0)**/
						if(estadoCuenta!=0)
						{
							return ComunAction.accionSalirCasoError(mapping,request,con,logger, "cuenta no activa","errors.paciente.cuentaNoActiva", true);
						}
						else
						{
							@SuppressWarnings("unused")
							ActionForward forward=this.accionGuardarNota(mapping,request,formNotasRecuperacion,con,mundo, usuario, paciente);
							sesion.setAttribute("esResumen", "1");
							return mapping.findForward("paginaNotasRecuperacion");
						}
					}
					else
					{
						return ComunAction.accionSalirCasoError(mapping,request,con,logger, "cuenta no activa","errors.paciente.cuentaNoActiva", true);
					}
				}
				//***********************************************************************
				//Anexo 550.*************************************************************
				else if(estado.equals("mostrarOpciones"))
				{
					UtilidadBD.closeConnection(con); 
					return mapping.findForward("paginaOpciones");
				}
				else if(estado.equals("empezarPorPaciente"))
				{				
					formNotasRecuperacion.setNumeroSolicitud(
							Integer.parseInt(formNotasRecuperacion.getMapaPeticionesPaciente("numeroSolicitud_"+formNotasRecuperacion.getPosicionMapa())+""));

					formNotasRecuperacion.setNumeroPeticion(
							Integer.parseInt(formNotasRecuperacion.getMapaPeticionesPaciente("codigoPeticion_"+formNotasRecuperacion.getPosicionMapa())+""));

					formNotasRecuperacion.setEsResumen(0);				

					return metodoEmpezar(con, request, mapping, paciente, usuario, formNotasRecuperacion, mundo);				

				}
				else if(estado.equals("empezarPorMedico"))
				{
					formNotasRecuperacion.setNumeroSolicitud(
							Integer.parseInt(formNotasRecuperacion.getMapaPeticionesMedico("numeroSolicitud_"+formNotasRecuperacion.getPosicionMapa())+""));

					formNotasRecuperacion.setNumeroPeticion(
							Integer.parseInt(formNotasRecuperacion.getMapaPeticionesMedico("codigoPeticion_"+formNotasRecuperacion.getPosicionMapa())+""));

					formNotasRecuperacion.setEsResumen(0);	

					/**para cargar el paciente que corresponda a la orden**/
					paciente.setCodigoPersona((Integer.parseInt(formNotasRecuperacion.getMapaPeticionesMedico("codigoPaciente_"+formNotasRecuperacion.getPosicionMapa())+"")));
					paciente.cargar(con,(Integer.parseInt(formNotasRecuperacion.getMapaPeticionesMedico("codigoPaciente_"+formNotasRecuperacion.getPosicionMapa())+"")));
					paciente.cargarPaciente(con, (Integer.parseInt(formNotasRecuperacion.getMapaPeticionesMedico("codigoPaciente_"+formNotasRecuperacion.getPosicionMapa())+"")),usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");	    		

					return metodoEmpezar(con, request, mapping, paciente, usuario, formNotasRecuperacion, mundo);			
				}
				else if(estado.equals("peticionesPaciente"))
				{
					/**Validamos que haya un paciente cargado en session**/
					if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con,logger, "paciente null o sin id","errors.paciente.noCargado", true);
					}				

					/**
					 * Cambio por responder cirugia ANEXO-395
					 */
					if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.ingresoEstadoDiferente", "errors.ingresoEstadoDiferenteAbierto", true);
					}
					if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.cuentaNoValida", "errors.paciente.cuentaNoValida", true);
					}
					//Validar que el usuario no se autoatienda
					ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
					if(respuesta.isTrue())
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no puede ser autoatendido", respuesta.getDescripcion(), true);


					formNotasRecuperacion.reset();
					ResponderCirugias responder = new ResponderCirugias();
					formNotasRecuperacion.setMapaPeticionesPaciente(
							responder.cargarPeticionesPacienteConPeticion(con, 
									paciente.getCodigoPersona(), 
									Integer.parseInt(paciente.getTipoInstitucion()), 
									paciente.getCodigoCuenta(),
									ConstantesBD.codigoNuncaValido));

					int cantidadRegistros=Integer.parseInt(formNotasRecuperacion.getMapaPeticionesPaciente("numRegistros")+"");
					if(cantidadRegistros>0)
					{
						for(int i=0; i<cantidadRegistros; i++)
						{
							if(Utilidades.tieneSolicitudPeticionQxStr(con, Integer.parseInt(formNotasRecuperacion.getMapaPeticionesPaciente("codigoPeticion_"+i)+"")))
							{
								formNotasRecuperacion.setMapaPeticionesPaciente("tieneOrden_"+i, "SI");
							}
							else
							{
								formNotasRecuperacion.setMapaPeticionesPaciente("tieneOrden_"+i, "NO");
							}
						}
					}
					if(cantidadRegistros<=0)
					{
						UtilidadBD.closeConnection(con);
						ArrayList atributosError = new ArrayList();
						atributosError.add(" Paciente sin Peticiones");
						request.setAttribute("codigoDescripcionError", "error.salasCirugia.pacienteSinPeticiones");				
						request.setAttribute("atributosError", atributosError);
						return mapping.findForward("paginaError");	
					}

					UtilidadBD.closeConnection(con);
					return mapping.findForward("peticionesPaciente");					
				}
				else if(estado.equals("peticionesMedico"))	    
				{
					formNotasRecuperacion.reset();
					ResponderCirugias responder = new ResponderCirugias();
					formNotasRecuperacion.setMapaPeticionesMedico(responder.cargarPeticionesMedico(con,usuario.getLoginUsuario(),usuario.getCodigoInstitucionInt(),usuario.getCodigoCentroCosto(), usuario.getCodigoCentroAtencion()));
					int cantidadRegistros=Integer.parseInt(formNotasRecuperacion.getMapaPeticionesMedico("numRegistros")+"");

					if(cantidadRegistros>0)
					{
						for(int i=0; i<cantidadRegistros; i++)
						{
							if(Utilidades.tieneSolicitudPeticionQxStr(con, Integer.parseInt(formNotasRecuperacion.getMapaPeticionesMedico("codigoPeticion_"+i)+"")))
							{
								formNotasRecuperacion.setMapaPeticionesMedico("tieneOrden_"+i, "SI");
							}
							else
							{
								formNotasRecuperacion.setMapaPeticionesMedico("tieneOrden_"+i, "NO");
							}
						}
					}
					if(cantidadRegistros<=0)
					{
						UtilidadBD.closeConnection(con);
						ArrayList atributosError = new ArrayList();
						atributosError.add(" Medico sin Peticiones");
						request.setAttribute("codigoDescripcionError", "error.salasCirugia.medicoSinPeticiones");				
						request.setAttribute("atributosError", atributosError);
						return mapping.findForward("paginaError");	
					}

					UtilidadBD.closeConnection(con);
					return mapping.findForward("peticionesMedico");		    	
				}
				else if(estado.equals("ordenarColumnaPaciente"))
				{
					return this.accionOrdenarColumnaPaciente(mapping, con, formNotasRecuperacion);
				}
				else if(estado.equals("ordenarColumnaMedico"))
				{
					return this.accionOrdenarColumnaMedico(mapping, con, formNotasRecuperacion);
				}

				//***********************************************************************
				//***********************************************************************    
			}
			else
			{
				logger.error("El form no es compatible con el form de NotasRecuperacionForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
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
	 * Accion para empezar el flujo de notas de recuperacion
	 * @param mapping
	 * @param request
	 * @param formaNotasRecuperacion 
	 * @param con
	 * @param mundo
	 * @param paciente
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, NotasRecuperacionForm formaNotasRecuperacion, Connection con, NotasRecuperacion mundo, UsuarioBasico enfermera, PersonaBasica paciente) throws Exception
	{
		formaNotasRecuperacion.reset();
		/**Ponemos la fecha y hora del sistema por defecto en el momento de empezar**/
		formaNotasRecuperacion.setFechaRecuperacion(UtilidadFecha.getFechaActual());
		formaNotasRecuperacion.setHoraRecuperacion(UtilidadFecha.getHoraActual());
		/**Tomo el número de solicitud que se paso por request desde la funcionalidad de Responder Cirugias**/
		int numeroSolicitud=formaNotasRecuperacion.getNumeroSolicitud();
		
		/**Pongo los datos que ya hallan sido ingresados previamente**/
		formaNotasRecuperacion.setNotasRecuperacionParam(mundo.consultarTiposNotasRecuperacion(con, Integer.parseInt(enfermera.getCodigoInstitucion()),numeroSolicitud));
		/**Tomo las fechas en las que se hallan ingresado notas de recuperación para la organización en la vista**/
		formaNotasRecuperacion.setFechasNotasRecuperacion(mundo.consultarFechaRecuperacion(con, numeroSolicitud));
		/**Cargo las Observaciones y Medicamentos que se hallan ingresado en las notas de recuperación básicas**/
		formaNotasRecuperacion.setObservacionesGrales(mundo.consultarObservacionesGenerales(con, numeroSolicitud));
		formaNotasRecuperacion.setMedicamentosGrales(mundo.consultarMedicamentosGenerales(con, numeroSolicitud));
		formaNotasRecuperacion.resetMapaNuevo();
	
		/**Datos necesarios para el validate de esta forma**/
		formaNotasRecuperacion.setCodigoCuenta(paciente.getCodigoCuenta());
		formaNotasRecuperacion.setViaIngreso(paciente.getCodigoUltimaViaIngreso());
		Cuenta cue=new Cuenta();
		cue.cargarCuenta(con, formaNotasRecuperacion.getCodigoCuenta()+"");
		
		
		formaNotasRecuperacion.setFechaAperturaCuenta(cue.getDiaApertura()+"/"+cue.getMesApertura()+"/"+cue.getAnioApertura());
		formaNotasRecuperacion.setHoraApertura(cue.getHoraApertura());
		if(formaNotasRecuperacion.getViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			formaNotasRecuperacion.setFechaAdmision(Utilidades.obtenerFechaAdmisionHosp(con, paciente.getCodigoCuenta()));
			formaNotasRecuperacion.setHoraAdmision(Utilidades.obtenerHoraAdmisionHosp(con, paciente.getCodigoCuenta()));
		}
		if(formaNotasRecuperacion.getViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		{
			formaNotasRecuperacion.setFechaAdmision(Utilidades.obtenerFechaAdmisionUrg(con, paciente.getCodigoCuenta()));
			formaNotasRecuperacion.setHoraAdmision(Utilidades.obtenerHoraAdmisionUrg(con, paciente.getCodigoCuenta()));;
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaNotasRecuperacion");
	}
	
	//***************************************************************************************
	
	/**
	 * Accion para guardar la nota que ingresa la enfermera
	 * @param mapping
	 * @param request
	 * @param formaNotasRecuperacion
	 * @param con
	 * @param mundo
	 * @param enfermera
	 * @param paciente
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionGuardarNota(ActionMapping mapping, HttpServletRequest request,  NotasRecuperacionForm formaNotasRecuperacion, Connection con, NotasRecuperacion mundo, UsuarioBasico enfermera, PersonaBasica paciente) throws Exception
	{
		int indicadorError=0;
		int numeroSolicitud=formaNotasRecuperacion.getNumeroSolicitud();
		String observaciones=formaNotasRecuperacion.getObservacionesGrales();
		String medicamentos=formaNotasRecuperacion.getMedicamentosGrales();
		UtilidadBD.iniciarTransaccion(con);
		
		/**Agrego a las Obsaervaciones los datos del medico y la fecha y hora del sistema**/
		if(!formaNotasRecuperacion.getObservacionesGralesNueva().trim().equals("") && formaNotasRecuperacion.getObservacionesGralesNueva()!=null)
		{
			observaciones=UtilidadTexto.agregarTextoAObservacion(formaNotasRecuperacion.getObservacionesGrales(),formaNotasRecuperacion.getObservacionesGralesNueva(),enfermera, false);
		}
		/**Agrego a los Medicamentos los datos del medico y la fecha y hora del sistema**/
		if(!formaNotasRecuperacion.getMedicamentosNuevos().trim().equals("") && formaNotasRecuperacion.getMedicamentosNuevos()!=null)
		{
			medicamentos=UtilidadTexto.agregarTextoAObservacion(formaNotasRecuperacion.getMedicamentosGrales(),formaNotasRecuperacion.getMedicamentosNuevos(), enfermera, false);
		}
		int basica=mundo.insertarNotaRecuperacion(con, numeroSolicitud, medicamentos,observaciones);
		int numeroRegistros=Integer.parseInt(formaNotasRecuperacion.getNotasRecuperacionParam("numRegistros")+"");
		
		int relacion=1;
		/**Recorrer el mapa de los nuevo registros insertados y se va insertando cada detalle**/
		int cont=0;
		for(int j=0; j<numeroRegistros; j++)
		{
			/**Inserto cada detalle de la nota**/
			if(!(formaNotasRecuperacion.getHistoricoNuevasNotas("nuevasNotas_"+j)+"").trim().equals(""))
			{
				cont++;
			}
		}
		if(cont>0)
		{
			/**Para que todas las notas que se inserten queden con la misma hora**/
			String horaActual=UtilidadFecha.getHoraSegundosActual();
			
			for(int i=0; i<numeroRegistros;i++)
			{
				int insercion=mundo.insertarDetalleNotaRecuperacion(con, numeroSolicitud, formaNotasRecuperacion.getFechaRecuperacion(), formaNotasRecuperacion.getHoraRecuperacion(),relacion, formaNotasRecuperacion.getHistoricoNuevasNotas("nuevasNotas_"+i)+"", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()), horaActual, enfermera.getCodigoPersona(),enfermera.getCodigoInstitucionInt());
				relacion++;
				if(insercion<=0)
				{
					indicadorError=1;
					UtilidadBD.abortarTransaccion(con);
					return ComunAction.accionSalirCasoError(mapping,request,con,logger, "no se logro guardar la nota","error.salasCirugia.errorGuardandoNotaRecuperacion", true);
				}
			}
		}
		/**Si no se presento error en ni en la insercion de la nota basica(basica)
		 * ni en la nota detallada (inidicadorError)**/
		if(basica>0 && indicadorError==0)
		{
			
			UtilidadBD.finalizarTransaccion(con);
			return this.accionEmpezar(mapping, formaNotasRecuperacion, con, mundo, enfermera,paciente);
			/**formaNotasRecuperacion.resetBasico();
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaNotasRecuperacion");**/
		}
		else
		{
			return ComunAction.accionSalirCasoError(mapping,request,con,logger, "no se logro guardar la nota","error.salasCirugia.errorGuardandoNotaRecuperacion", true);
		}
	}
	
	
	//***************************************************************************************
	
	/**
	 * Metodo para empezar la funcionalidad 
	 * 
	 * @param Connection con
	 * @param HttpServletRequest request
	 * @param ActionMapping mapping
	 * @param PersonaBasica paciente
	 * @param UsuarioBasico usuario
	 * @param NotasRecuperacionForm formNotasRecuperacion
	 * @param NotasRecuperacion mundo
	 * */
	@SuppressWarnings("deprecation")
	public ActionForward metodoEmpezar(Connection con, HttpServletRequest request, ActionMapping mapping, PersonaBasica paciente,
			UsuarioBasico usuario, NotasRecuperacionForm formNotasRecuperacion, NotasRecuperacion mundo) {
		
		/**Validamos que haya un paciente cargado en session**/
		if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
		{
			return ComunAction.accionSalirCasoError(mapping, request, con,logger, "paciente null o sin id","errors.paciente.noCargado", true);
		}				

		/**
		 * Cambio por responder cirugia ANEXO-395
		 */
		if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
		{
			UtilidadBD.closeConnection(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.ingresoEstadoDiferente", "errors.ingresoEstadoDiferenteAbierto", true);
		}
		if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
		{
			UtilidadBD.closeConnection(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.cuentaNoValida", "errors.paciente.cuentaNoValida", true);
		}
		
		if(formNotasRecuperacion.getEsResumen()==0)
		{
			/**Validamos que la ocupacion medica sea enfermera**/
			String mensaje = UtilidadValidacion.esEnfermera(usuario);
			if(!mensaje.equals(""))
			{
				//se verifica si se ha definido la ocupación enfermera o auxiliar de enfermería
				//en parámetros generales
				if(mensaje.equals("errors.noOcupacionEnfermera"))
				{
					UtilidadBD.closeConnection(con);
					return ComunAction.accionSalirCasoError(mapping, request, con,logger, "ocupacion enfermera no definida",mensaje, true);
				}
				else
				{
					UtilidadBD.closeConnection(con);
					return ComunAction.accionSalirCasoError(mapping, request, con,logger, "ocupacion no valida","error.salasCirugia.ocupacionNoValida", true);
				}
			}
			else
			{
				try
				{
					return this.accionEmpezar(mapping,formNotasRecuperacion,con,mundo, usuario, paciente);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		else
		{
			/**Si es RESUMEN solo validamos que sea profesional de la salud**/
			if(!UtilidadValidacion.esProfesionalSalud(usuario))
			{
				UtilidadBD.closeConnection(con);
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.noProfesionalSalud", "errors.noProfesionalSalud", true);
			}
			else
			{
				try
				{
					return this.accionEmpezar(mapping,formNotasRecuperacion,con,mundo, usuario, paciente);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaNotasRecuperacion");
	}
	
	//***************************************************************************************
	
	/**
	 * Acción que permite el ordenamiento por cualquiera de las columnas
	 * en el listado de peticiones de una paciente
	 * @param con
	 * @param responderCirugiasForm
	 * @param response
	 * @return
	 */
    private ActionForward accionOrdenarColumnaPaciente(ActionMapping mapping, Connection con, NotasRecuperacionForm forma) 
    {
        String[] indices={"codigoPeticion_", "fechaCirugia_", "consecutivoOrdenes_","estadoMedico_", "numeroSolicitud_", "solicitante_", "especialidad_", "detPeticion_", "tieneOrden_"};
        
        int tmp=Integer.parseInt(forma.getMapaPeticionesPaciente("numRegistros")+"");
        forma.setMapaPeticionesPaciente(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaPeticionesPaciente(),Integer.parseInt(forma.getMapaPeticionesPaciente("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapaPeticionesPaciente("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);

        return mapping.findForward("peticionesPaciente");
    }
    
    //***************************************************************************************
    
    /**
     * Acción que permite el ordenamiento por cualquiera de las columnas
	 * en el listado de peticiones por el flujo de medico
     * @param con
     * @param responderCirugiasForm
     * @param response
     * @return
     */
    private ActionForward accionOrdenarColumnaMedico(ActionMapping mapping, Connection con, NotasRecuperacionForm forma) 
    {
        String[] indices={"codigoPeticion_", "fechaCirugia_", "consecutivoOrdenes_","estadoMedico_", "numeroSolicitud_", "tipoId_", "paciente_", "codigoPaciente_", "detPeticion_", "tieneOrden_"};
        
        int tmp=Integer.parseInt(forma.getMapaPeticionesMedico("numRegistros")+"");
        forma.setMapaPeticionesMedico(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaPeticionesMedico(),Integer.parseInt(forma.getMapaPeticionesMedico("numRegistros")+"")));
        forma.setUltimoPatron(forma.getPatronOrdenar());
        forma.setMapaPeticionesMedico("numRegistros",tmp+"");
        UtilidadBD.closeConnection(con);

        return mapping.findForward("peticionesMedico");
    }
}