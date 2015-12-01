package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
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
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.RespuestaValidacion;
import util.RespuestaValidacionTratante;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.historiaClinica.EvolucionesForm;
import com.princetonsa.dto.historiaClinica.DtoEvolucionComentarios;
import com.princetonsa.dto.historiaClinica.DtoValoracionHospitalizacion;
import com.princetonsa.dto.ordenesmedicas.DtoRegistroIncapacidades;
import com.princetonsa.mundo.AdmisionUrgencias;
import com.princetonsa.mundo.Camas1;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.TrasladoCamas;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.atencion.Egreso;
import com.princetonsa.mundo.atencion.Evolucion;
import com.princetonsa.mundo.atencion.HistoricoEvoluciones;
import com.princetonsa.mundo.atencion.SignoVital;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.enfermeria.RegistroEnfermeria;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;
import com.princetonsa.mundo.historiaClinica.Epicrisis1;
import com.princetonsa.mundo.historiaClinica.Evoluciones;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.historiaClinica.Valoraciones;
import com.princetonsa.mundo.ordenesmedicas.RegistroIncapacidades;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudEvolucion;
import com.servinte.axioma.bl.historiaClinica.facade.HistoriaClinicaFacade;
import com.servinte.axioma.dto.historiaClinica.CurvaCrecimientoPacienteDto;
import com.servinte.axioma.dto.historiaClinica.DatosAlmacenarCurvaCrecimientoDto;
import com.servinte.axioma.dto.historiaClinica.HistoricoImagenPlantillaDto;
import com.servinte.axioma.dto.manejoPaciente.InformacionCentroCostoValoracionDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.ICentroCostosServicio;

public class EvolucionesAction extends Action 
{

	
	/**
	 * 
	 */
	Logger logger =Logger.getLogger(EvolucionesAction.class);
	
	
	
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try {
			if (form instanceof EvolucionesForm) 
			{
				EvolucionesForm forma=(EvolucionesForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con= UtilidadBD.abrirConexion();

				HttpSession session=request.getSession();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				UsuarioBasico medico = (UsuarioBasico)session.getAttribute("usuarioBasico");

				Evoluciones evoluciones= new Evoluciones();

				boolean esUrgencias=false;

				if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(), ConstantesBD.permisoImprimirDetalleItemHC)){
					forma.setPermisoImpresionHCV(true); 
				}

				ActionErrors errores = new ActionErrors();

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de EvolucionAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					errores =ValidacionesEvolucion(con, forma, paciente, medico);
					
					//Se elimina la variable en session de curvas de crecimiento
					session.removeAttribute("curvasCrecimientoPaciente");

					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("errores");
					}

					this.cargarPlantillaEvolucion(con, forma, usuario, paciente);

					if(forma.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteSignosVitales))
						forma.getEvolucion().setSignosVitales(UtilidadesHistoriaClinica.cargarSignosVitales(con));

					this.accionesManejoWarning(con, forma, medico, paciente);
					this.cargarWarningsReferencia(con, forma, paciente);
					int codigoEvolucion= evoluciones.obtenerCodigoUltimaEvolucion(con, paciente.getCodigoCuenta());

					//*******************PROFESIONAL RESPONSABLE***********************************************************
					forma.getEvolucion().setProfesional(usuario);
					forma.getEvolucion().setFechaGrabacion(UtilidadFecha.getFechaActual());
					forma.getEvolucion().setHoraGrabacion(UtilidadFecha.getHoraActual());
					//******************************************************************************************************
					if(codigoEvolucion>0)
					{
						forma.setCodigoConductaSeguir(evoluciones.obtenerUltimaConducta(con, codigoEvolucion));
						String [] diagnosticoComplicacion= evoluciones.obtenerDiagnosticoComplicacion(con, codigoEvolucion);
						forma.getEvolucion().getDiagnosticoComplicacion1().setAcronimo(diagnosticoComplicacion[0]);
						forma.getEvolucion().getDiagnosticoComplicacion1().setTipoCIE(Utilidades.convertirAEntero(diagnosticoComplicacion[1]+""));
						forma.getEvolucion().getDiagnosticoComplicacion1().setNombre(diagnosticoComplicacion[2]);

						String [] diagnosticoPrincipal = evoluciones.obtenerDiagnosticoPrincipal(con, codigoEvolucion);

						forma.getEvolucion().getDiagnosticoPrincipal().setAcronimo(diagnosticoPrincipal[0]);
						forma.getEvolucion().getDiagnosticoPrincipal().setTipoCIE(Utilidades.convertirAEntero(diagnosticoPrincipal[1]+""));
						forma.getEvolucion().getDiagnosticoPrincipal().setNombre(diagnosticoPrincipal[2]);

						forma.getEvolucion().setDiagnosticos(evoluciones.consultaDiagnosticosRelacionados(con, codigoEvolucion));

						String diagnosticosSeleccionados = "'" + ConstantesBD.codigoNuncaValido + "'";
						for(int i=0;i<forma.getEvolucion().getDiagnosticos().size();i++)
						{
							diagnosticosSeleccionados +=  ",'"+forma.getEvolucion().getDiagnosticos().get(i).getAcronimo()+"'";
						}
						forma.setDiagnosticosSeleccionados(diagnosticosSeleccionados);




					}

					else
					{	
						this.cargarDiagnosticosValoracion(con, forma, usuario, paciente);
					}	
					this.asignarDiagnosticosRelacionados(forma);

					
					/**
					 * Cambios por MT 5568
					 */
					InformacionCentroCostoValoracionDto infoCentroCosto = new InformacionCentroCostoValoracionDto();
					
					if(paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoHospitalizacion 
							&& paciente.getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteHospitalizado) 
							&& UtilidadValidacion.tieneIngresoActivoCuidadoEspecial(con, paciente.getCodigoCuenta())){
						
						/*String [] tiposMonitoreo= evoluciones.consultarTipoMonitoreo(con, paciente.getCodigoIngreso());
						forma.setTipoMonitoreoActual(tiposMonitoreo[0]);
						forma.getEvolucion().setCentroCostoMonitoreo(Utilidades.convertirAEntero(tiposMonitoreo[1]));*/						
						infoCentroCosto = evoluciones.informacionCentroCostoDeIngresoActivoCuidadoEspecialXCuenta(con, paciente.getCodigoCuenta());
						if(infoCentroCosto != null){
							forma.setTipoMonitoreoActual(infoCentroCosto.getIdTipoMonitoreo() +"");
							forma.getEvolucion().setCentroCostoMonitoreo(infoCentroCosto.getIdCentroCosto());
						}
					}else{
						//Si el paciente no tiene un ingreso de cuidado especial, se muestra el area en la que se encuentra el paciente						
						infoCentroCosto.setIdCentroCosto(paciente.getCodigoArea());
						CentrosCosto centroCosto = obtenerCentroCosto(paciente.getCodigoArea());
						infoCentroCosto.setDescripcionCentroCosto(centroCosto.getNombre());					
					}
					forma.getEvolucion().setInfoCentroCostoValoracion(infoCentroCosto);
					/**
					 * Fin MT 5568
					 */
					
					
					if(UtilidadValidacion.pacienteFalleceCirugia(con, paciente.getCodigoCuenta()))
					{	
						String [] InfoFallecimiento= evoluciones.consultarInfoFallecimiento(con, paciente.getCodigoCuenta());

						forma.setCodigoConductaSeguir(ConstantesBD.codigoConductaASeguirSalidaEvolucion);
						forma.getEvolucion().setOrdenSalida(true);
						forma.setOrdenSalida("true");
						forma.getEvolucion().setMuerto("true");
						forma.getEvolucion().setFechaMuerte(UtilidadFecha.conversionFormatoFechaAAp(InfoFallecimiento[0]));
						forma.getEvolucion().setHoraMuerte(InfoFallecimiento[1]);
						forma.getEvolucion().getDiagnosticoMuerte().setAcronimo(InfoFallecimiento[2]);
						forma.getEvolucion().getDiagnosticoMuerte().setTipoCIE(Utilidades.convertirAEntero(InfoFallecimiento[3]));
						forma.getEvolucion().getDiagnosticoMuerte().setNombre(InfoFallecimiento[4]);

					}
					DtoEvolucionComentarios comentarios = new DtoEvolucionComentarios();
					comentarios.setProfesional(usuario);
					forma.getEvolucion().getComentarios().add(comentarios);
					forma.getPlantilla().cargarEdadesPaciente(paciente);

					//				********************ERLIMINAR INCAOPACIDADES NO GUARDADAS********************************
					RegistroIncapacidades mundoIncapacidades = new RegistroIncapacidades();

					if (mundoIncapacidades.eliminarIncapacidadesInactivasXRegistro(con, paciente.getCodigoIngreso()).equals(ConstantesBD.acronimoSi)){
						logger.info("se han eliminmado satisfactoriamente los registros de incapacidades con campo activo N");
					}

					if (mundoIncapacidades.consultarIncapacidadesCambios(con, paciente.getCodigoIngreso()).equals(ConstantesBD.acronimoSi)){
						logger.info("se han reversado satisfactoriamente los registros de incapacidades con campo activo C");
					}

					if(forma.getEvolucion().getProfesional().getEspecialidades1().length==1){
						forma.getEvolucion().setEspecialidadProfResponde(forma.getEvolucion().getProfesional().getEspecialidades1()[0].getCodigo()+"");
						forma.setSelectEspecialidad(forma.getEvolucion().getProfesional().getEspecialidades1()[0].getNombre());
					}


					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("histoDieta"))
				{
					//--Cargar la dieta de la Funcionalidad de Registro de enfermeria 
					cargarHistoricosDieta(con, medico, paciente, forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("historicoDieta");
				}
				else if (estado.equals("histoSignosVitales"))
				{
					accionCargarHistoricoSignosVitales(con, usuario, forma, medico.getCodigoInstitucionInt(), UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("signosVitalesEnfermeria");

				}
				else if (estado.equals("resumen")||estado.equals("imprimirHistoriaAtenciones"))
				{
					return accionResumen(con,forma,usuario,paciente,mapping,request);
				}
				else if(estado.equals("guardar"))
				{
					errores = validacionesGuardarEvolucion(con, forma, paciente, medico, usuario);

					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}

					errores= accionGuardarEvolucion(con, forma, paciente, medico,request);
					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}
					forma.resetMensaje();
					return accionResumen(con, forma, usuario, paciente, mapping,request);
				}
				else if (estado.equals("registrarReingreso"))
				{
					return accionRegistrarReingreso(con,forma,mapping,paciente,request);
				}
				else if (estado.equals("historicoMostrar") || estado.equals("historicoModificar"))
				{
					//Valida que el paciente no posee el ingreso con entidades subcontratadas
					if(IngresoGeneral.esIngresoComoEntidadSubContratada(con,paciente.getCodigoIngreso()).toString().equals(ConstantesBD.acronimoSi))
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Ingreso de Paciente en Entidades Subcontratadas", "Ingreso de Paciente en Entidades Subcontratadas. Entidad Subcontratada : "+EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con,paciente.getCodigoIngreso()+""), false);
					}

					HistoricoEvoluciones hEvoluciones = new HistoricoEvoluciones();

					//Si la cuenta no esta abierta debemos mostrar todas las evoluciones
					//del mismo
					if (paciente.getCodigoCuenta()==0)
					{
						//La cuenta no esta abierta debemos mostrar todas las evoluciones de este paciente
						hEvoluciones.cargar(con, paciente.getCodigoTipoIdentificacionPersona(), paciente.getNumeroIdentificacionPersona());
					}
					else if (request.getParameter("codigoCuenta")!=null)
					{
						int codigoCuenta=0;
						try
						{
							codigoCuenta=Integer.parseInt(request.getParameter("codigoCuenta"));
						}
						catch(NumberFormatException e)
						{
							codigoCuenta=paciente.getCodigoCuenta();
						}
						hEvoluciones.cargar(con, codigoCuenta);
					}
					else
					{
						//Si no existe asocio, cargamos el de la cuenta del
						//paciente actual
						if (!paciente.getExisteAsocio())
						{
							hEvoluciones.cargar(con, paciente.getCodigoCuenta());
						}
						//Si lo hay cargamos el de las dos cuentas
						else
						{
							hEvoluciones.cargarEvolucionesCuentaYAsocio(con, paciente.getCodigoCuenta(), paciente.getCodigoCuentaAsocio());
						}
					}

					if (hEvoluciones.size()==0) 
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe ninguna evolucion previa por consultar" , "error.evolucion.historicoNoHayEvolucion", true) ;
					}

					if (estado.equals("historicoMostrar")) 
					{
						request.setAttribute("estado", "resumen");
					}
					else if (estado.equals("historicoModificar")) 
					{
						request.setAttribute("estado", "modificar");
					}
					request.setAttribute("evoluciones", hEvoluciones);
					UtilidadBD.cerrarConexion(con);

					return mapping.findForward("historico");
				}
				else if (estado.equals("imprimir"))
				{
					return accionImprimir(con,forma,mapping,medico);
				}
				else if (estado.equals("cargarModEpicrisis"))
				{
					return accionCargarModificacionEpicrisis(con,forma,usuario,paciente,mapping,request);
				}
				else if (estado.equals("modificarEpicrisis"))
				{
					Epicrisis1.insertarInfoAutomaticaEvolucionEpicrisis(con, forma.getEvolucion(), medico, paciente, forma.getPlantilla());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("epicrisis");
				}

				else if (estado.equals("cargarModEvolucion"))
				{
					return accionCargarModificacionEvolucion(con,forma,usuario,paciente,mapping,request);
				}

				else if(estado.equals("modificarEvolucion"))
				{
					errores= accionModificarEvolucion(con, forma, paciente, medico,request);
					if(!errores.isEmpty())
					{
						saveErrors(request,errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("modificar");
					}
					forma.resetMensaje();
					return accionResumen(con, forma, usuario, paciente, mapping,request);

				}
				else if (estado.equals("recargarPlantilla"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seccionesValoresParametrizables");
				}
				else if (estado.equals("cambiarEspecdialidad")) 
				{
					for (InfoDatosInt evolucion : forma.getEvolucion().getProfesional().getEspecialidades1()) {
						if((evolucion.getCodigo()+"").equals(forma.getEvolucion().getEspecialidadProfResponde())){
							forma.setSelectEspecialidad(evolucion.getNombre());
						}
					}
					return null;
				}

				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de la EVOLUCION ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}		
			}
			else
			{
				logger.error("El form no es compatible con el form de EvolucionForm");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
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
	 * 
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param medico
	 * @param request
	 * @return
	 */
	private ActionErrors accionModificarEvolucion(Connection con, EvolucionesForm forma, PersonaBasica paciente, UsuarioBasico medico, HttpServletRequest request) 
	{
		
		ActionErrors errores  = new ActionErrors();
		
		boolean transaccion= UtilidadBD.iniciarTransaccion(con);
		
		for(int i=0;i<forma.getEvolucion().getComentarios().size();i++)
		 {	 
			 
			 if(!forma.getEvolucion().getComentarios().get(i).getValor().equals("")&&forma.getEvolucion().getComentarios().get(i).getConsecutivo().equals(""))
			 { 
				 transaccion= Evoluciones.insertarComentariosEvolucion(
						 					con,
						 					forma.getCodigoUltimaEvolucion(),
						 					forma.getEvolucion().getComentarios().get(i).getValor(),
						 					forma.getEvolucion().getComentarios().get(i).getProfesional().getLoginUsuario()
						 					);
			 }
		 }
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		
		return errores;
		
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionCargarModificacionEvolucion(Connection con, EvolucionesForm forma, UsuarioBasico usuario, PersonaBasica paciente, ActionMapping mapping, HttpServletRequest request) 
	{
		forma.setVieneDeHistorico(UtilidadTexto.getBoolean(request.getParameter("vieneDeHistorico")));
		forma.setVieneDeHistoriaAtenciones(UtilidadTexto.getBoolean(request.getParameter("vieneDeHistoriaAtenciones")));
		
		cargarPlantillaEvolucionResumen(con, forma, usuario, paciente);
		
		//Curvas de crecimiento
		List<HistoricoImagenPlantillaDto> dtohipTemp = new ArrayList<HistoricoImagenPlantillaDto>();
		try {
			dtohipTemp = new HistoriaClinicaFacade().evolucionesPorId(forma.getCodigoUltimaEvolucion());
		} catch (IPSException e) {
			Log4JManager.error(e);
		}
		forma.setDtoHistoricoImagenPlantilla(dtohipTemp);
		
		forma.setMostrarDetalles(false);
		if(forma.getIndiceCurvaSeleccionada()!=null){
			forma.setCurvaSeleccionada(forma.getDtoHistoricoImagenPlantilla().get(forma.getIndiceCurvaSeleccionada()));
			
			Calendar c = Calendar.getInstance();
			c.setTime(forma.getCurvaSeleccionada().getFecha());
	
			forma.setEdadCalculada(paciente.calcularEdadEnFechaDetallada(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR)));
			forma.setMostrarDetalles(true);
			forma.setIndiceCurvaSeleccionada(null);
		}
		//Fin curvas de crecimiento

		
		forma.setEvolucion(Evoluciones.cargarEvolucion(con, forma.getCodigoUltimaEvolucion()+""));
		
		for(int i=0;i<forma.getPlantilla().getSeccionesFijas().size();i++)
		{
			if(Evoluciones.enviadoEpicrisis(con, forma.getCodigoUltimaEvolucion()+""))
			{
				forma.getPlantilla().getSeccionesFijas().get(i).setEnviarEpicrisis(true);
			}
		}
		
		forma.setDiagnosticosRelacionados(new HashMap<String, Object>());
		asignarDiagnosticosRelacionados(forma);
		
		DtoEvolucionComentarios comentarios = new DtoEvolucionComentarios();
		comentarios.setProfesional(usuario);
		forma.getEvolucion().getComentarios().add(comentarios);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("modificar");
	}



	/**
	 * Mï¿½todo que cargar el resumen de una evoluciï¿½n
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionResumen(Connection con, EvolucionesForm forma, UsuarioBasico usuario, PersonaBasica paciente, ActionMapping mapping, HttpServletRequest request) 
	{
		
		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< RESUMEN ");
		
		forma.setOcultarInfoPaciente(true);
		forma.setVieneDeHistorico(UtilidadTexto.getBoolean(request.getParameter("vieneDeHistorico")));
		forma.setVieneDeHistoriaAtenciones(UtilidadTexto.getBoolean(request.getParameter("vieneDeHistoriaAtenciones")));
		
		cargarPlantillaEvolucionResumen(con, forma, usuario, paciente);
		forma.setEvolucion(Evoluciones.cargarEvolucion(con, forma.getCodigoUltimaEvolucion()+""));
			for(int i=0;i<forma.getPlantilla().getSeccionesFijas().size();i++)
			{
				if(Evoluciones.enviadoEpicrisis(con, forma.getCodigoUltimaEvolucion()+""))
				{
					forma.getPlantilla().getSeccionesFijas().get(i).setEnviarEpicrisis(true);
				}
			}

		forma.setDiagnosticosRelacionados(new HashMap<String, Object>());
		asignarDiagnosticosRelacionados(forma);
		
		DtoEvolucionComentarios comentarios = new DtoEvolucionComentarios();
		comentarios.setProfesional(usuario);
		forma.getEvolucion().getComentarios().add(comentarios);
		
		//Inicializa la informacion de signos vitales
		forma.setSignosVitalesInstitucionCcosto(new ArrayList());
		forma.setSignosVitalesFijosHisto(new ArrayList());
		forma.setSignosVitalesHistoTodos(new ArrayList());
		forma.setSignosVitalesParamHisto(new ArrayList());
		
		forma.setSignosVitales(new ArrayList<SignoVital>());
		forma.setMapaDietaHistorico(new HashMap());
		forma.setMapaDieta(new HashMap());
				
		//Curvas de crecimiento
		List<HistoricoImagenPlantillaDto> dtohipTemp = new ArrayList<HistoricoImagenPlantillaDto>();
		try {
			dtohipTemp = new HistoriaClinicaFacade().evolucionesPorId(forma.getCodigoUltimaEvolucion());
		} catch (IPSException e) {
			Log4JManager.error(e);
		}
		forma.setDtoHistoricoImagenPlantilla(dtohipTemp);
		
		forma.setMostrarDetalles(false);
		if(forma.getIndiceCurvaSeleccionada()!=null){
			forma.setCurvaSeleccionada(forma.getDtoHistoricoImagenPlantilla().get(forma.getIndiceCurvaSeleccionada()));
			
			Calendar c = Calendar.getInstance();
			c.setTime(forma.getCurvaSeleccionada().getFecha());
	
			forma.setEdadCalculada(paciente.calcularEdadEnFechaDetallada(c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH)+1, c.get(Calendar.YEAR)));
			forma.setMostrarDetalles(true);
			forma.setIndiceCurvaSeleccionada(null);
		}
		//Fin curvas de crecimiento
		
		UtilidadBD.closeConnection(con);
		if(forma.getEstado().equals("imprimirHistoriaAtenciones"))
			return mapping.findForward("imprimir");
		else
			return mapping.findForward("resumen");
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionCargarModificacionEpicrisis(Connection con, EvolucionesForm forma, UsuarioBasico usuario, PersonaBasica paciente, ActionMapping mapping, HttpServletRequest request) 
	{
		forma.setVieneDeHistorico(UtilidadTexto.getBoolean(request.getParameter("vieneDeHistorico")));
		forma.setVieneDeHistoriaAtenciones(UtilidadTexto.getBoolean(request.getParameter("vieneDeHistoriaAtenciones")));
		
		cargarPlantillaEvolucionResumen(con, forma, usuario, paciente);
		forma.setEvolucion(Evoluciones.cargarEvolucion(con, forma.getCodigoUltimaEvolucion()+""));
		
		for(int i=0;i<forma.getPlantilla().getSeccionesFijas().size();i++)
		{
			if(Evoluciones.enviadoEpicrisis(con, forma.getCodigoUltimaEvolucion()+""))
			{
				forma.getPlantilla().getSeccionesFijas().get(i).setEnviarEpicrisis(true);
			}
		}
		
		/*DtoEvolucionComentarios comentarios = new DtoEvolucionComentarios();
		comentarios.setProfesional(usuario);
		forma.getEvolucion().getComentarios().add(comentarios);*/
		
		forma.setDiagnosticosRelacionados(new HashMap<String, Object>());
		asignarDiagnosticosRelacionados(forma);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("epicrisis");
	}




	/**
	 * Mï¿½todo implementado para imprimir el resumen de la evolucion
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param medico 
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, EvolucionesForm forma, ActionMapping mapping, UsuarioBasico medico) 
	{
		forma.setOcultarInfoPaciente(false);
		//Se carga la institucion
		forma.getInstitucion().cargar(con, medico.getCodigoInstitucionInt());
		//Se asigna el nombre del centro de atencion
		forma.setNombreCentroAtencion(medico.getCentroAtencion());
		
		DtoEvolucionComentarios comentarios = new DtoEvolucionComentarios();
		comentarios.setProfesional(medico);
		forma.getEvolucion().getComentarios().add(comentarios);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("imprimir");
	}

	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param paciente
	 * @param request
	 * @return
	 */
	private ActionForward accionRegistrarReingreso(Connection con, EvolucionesForm forma, ActionMapping mapping, PersonaBasica paciente, HttpServletRequest request) 
	{
		
		//Si se aceptï¿½ el reingreso se intenta registrarlo
		if(forma.isAceptarReingreso())
			if(!Utilidades.marcarReingreso(con, paciente.getCodigoIngreso(), paciente.getCodigoPersona()))
			{
				ActionErrors errores = new ActionErrors();
				errores.add("", new ActionMessage("errors.noSeGraboInformacion","DEL REGISTRO DE REINGRESO"));
				saveErrors(request, errores);
			}
		
		//Ya no hay necesidad de validar el reingreso 
		forma.setIndI(0);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumen");
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param paciente
	 */
	private void cargarWarningsReferencia(Connection con, EvolucionesForm forma, PersonaBasica paciente) 
	{
		String mensajeReferenciaInterna= UtilidadesHistoriaClinica.getMensajeReferenciaParaValidacion(con, paciente.getCodigoIngreso(), ConstantesBD.codigoNuncaValido+"", ConstantesIntegridadDominio.acronimoInterna);
		String mensajeReferenciaExterna= UtilidadesHistoriaClinica.getMensajeReferenciaParaValidacion(con, paciente.getCodigoIngreso(), ConstantesBD.codigoNuncaValido+"", ConstantesIntegridadDominio.acronimoExterna);
		if(!UtilidadTexto.isEmpty(mensajeReferenciaInterna) && !UtilidadTexto.isEmpty(mensajeReferenciaExterna))
			forma.setWarningReferencias(mensajeReferenciaInterna+"<br>"+mensajeReferenciaExterna);
		else
		{
			if(!UtilidadTexto.isEmpty(mensajeReferenciaInterna))
				forma.setWarningReferencias(mensajeReferenciaInterna);
			if(!UtilidadTexto.isEmpty(mensajeReferenciaExterna))
				forma.setWarningReferencias(mensajeReferenciaExterna);
		}
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param paciente 
	 * @param medico 
	 * @param usuario 
	 * @return
	 * @throws SQLException 
	 * @throws IPSException 
	 */
	private ActionErrors validacionesGuardarEvolucion(Connection con, EvolucionesForm forma, PersonaBasica paciente, UsuarioBasico medico, UsuarioBasico usuario) throws SQLException, IPSException 
	{
		
		
		ActionErrors errores = new ActionErrors();
		
		if(forma.getEvolucion().getFechaEvolucion().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","La fecha de la evolucion "));
		}
		if(forma.getEvolucion().getHoraEvolucion().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","La hora de la evolucion "));
		}
		if((forma.getEvolucion().getCodigoConductaSeguir()+"").equals("")||forma.getEvolucion().getCodigoConductaSeguir()<=0)
		{
			errores.add("descripcion",new ActionMessage("errors.required","La conducta a seguir "));
		}
		if(forma.getEvolucion().getCodigoConductaSeguir()==ConstantesBD.codigoConductaASeguirRemitirEvolucion&&forma.getEvolucion().getTipoReferencia().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","El tipo referencia "));
		}
		/*
		 * javrammo
		 * MT 6577. La especialidad debe ser requerida
		 */
		if(UtilidadTexto.isEmpty(forma.getEvolucion().getEspecialidadProfResponde())){
			errores.add("", new ActionMessage("errors.required","La Especialidad del Profesional que Responde"));	
		}
		/*
		 *Fin MT  6577
		 */
		
		if((forma.getEvolucion().getCodigoConductaSeguir()==ConstantesBD.codigoConductaASeguirTrasladoCuidadoEspecial)&&forma.getEvolucion().getProcedQuirurgicosObst().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","El tipo de monitoreo "));
		}
		if((forma.getEvolucion().getCodigoConductaSeguir()==ConstantesBD.codigoConductaASeguirCambioTipoMonitoreo))
		{
			if(forma.getEvolucion().getProcedQuirurgicosObst().equals(""))
			{
				errores.add("descripcion",new ActionMessage("errors.required","El tipo de monitoreo "));
			}
			else if(forma.getTipoMonitoreoActual().equals(forma.getEvolucion().getProcedQuirurgicosObst()))
			{
				errores.add("descripcion",new ActionMessage("errors.notEspecific","Se ha seleccionado el mismo tipo de monitoreo, no se realizarï¿½n cambios. Favor verificar. "));
			}
		}
		if(forma.getEvolucion().getCodigoTipoDiagnosticoPrincipal()==0)
		{
			errores.add("descripcion",new ActionMessage("errors.required","El tipo de diagnostico principal "));
		}
		if(!UtilidadTexto.isEmpty(forma.getEvolucion().getFechaEvolucion()))
		{	
			
			String fechaAdmision= UtilidadFecha.conversionFormatoFechaAAp(UtilidadValidacion.getFechaAdmision(paciente.getCodigoCuenta()));
			String horaAdminsion= UtilidadFecha.convertirHoraACincoCaracteres(UtilidadValidacion.getHoraAdmision(paciente.getCodigoCuenta()));
			logger.info("FECHA ADMISION: "+fechaAdmision);
			logger.info("HORA ADMISION: "+horaAdminsion);
			boolean centinelaErrorFechas=false;
			if(!UtilidadFecha.esFechaValidaSegunAp(forma.getEvolucion().getFechaEvolucion()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "evolucion "+forma.getEvolucion().getFechaEvolucion()));
				centinelaErrorFechas=true;
			}
			if(!centinelaErrorFechas)
			{
				/*Crear variable tipo ResultadoBoolean para almacenar la respuesta de la validaciòn de fechas*/
				ResultadoBoolean FechaEvolMenoFechaActual = UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), forma.getEvolucion().getFechaEvolucion(), forma.getEvolucion().getHoraEvolucion());
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getEvolucion().getFechaEvolucion(), UtilidadFecha.getFechaActual()))
				{
					errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "evolucion "+forma.getEvolucion().getFechaEvolucion(), "Actual "+UtilidadFecha.getFechaActual()));
				}
				/*En caso de que FechaEvolMenoFechaActual devuelva false, se devuelve a la vista el 
				 * mensaje de error retornado en la misma variable*/
				else if(!FechaEvolMenoFechaActual.isTrue()){
					errores.add("descripcion",new ActionMessage("errors.notEspecific",FechaEvolMenoFechaActual.getDescripcion()));
				}
				else if(!UtilidadFecha.compararFechas(forma.getEvolucion().getFechaEvolucion(), forma.getEvolucion().getHoraEvolucion(), fechaAdmision, horaAdminsion).isTrue())
				{
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de evolucion "+ forma.getEvolucion().getFechaEvolucion()+ " "+forma.getEvolucion().getHoraEvolucion(), "de admisiï¿½n "+fechaAdmision+" "+horaAdminsion));
				}
			}	
		}
		if(forma.getEvolucion().isOrdenSalida()&&forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoNuncaValido)
		{
			errores.add("descripcion",new ActionMessage("errors.required","El destino de salida "));
		}
		if(forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaOtro&&forma.getEvolucion().getOtroDestinoSalida().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","Ingresar cual es el otro destino de salida "));
		}
		if(forma.getEvolucion().isOrdenSalida()&&forma.getEvolucion().getMuerto().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","El estado de salida "));
		}
		logger.info("valor por defect getObligarRegIncapaPacienteHospitalizado:::"+ValoresPorDefecto.getObligarRegIncapaPacienteHospitalizado(usuario.getCodigoInstitucionInt()));
		if(ValoresPorDefecto.getObligarRegIncapaPacienteHospitalizado(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi)){
			if(forma.getEvolucion().isOrdenSalida()&&forma.getEvolucion().getMuerto().equals("false"))
			{
				RegistroIncapacidades incapacidades = new RegistroIncapacidades();
				DtoRegistroIncapacidades dtoRegistroIncapacidades = incapacidades.consultarIncapacidadPorIngreso(con, paciente.getCodigoIngreso());
				if(dtoRegistroIncapacidades.getEstado().equals("NADA")){
					errores.add("",new ActionMessage("errors.required","Definir la incapacidad "));
				}
			}
		}
		if(forma.getEvolucion().getMuerto().equals("true")&&forma.getEvolucion().getFechaMuerte().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","La fecha de muerte del paciente "));
		}
		if(forma.getEvolucion().getMuerto().equals("true")&&forma.getEvolucion().getHoraMuerte().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","La hora de muerte del paciente "));
		}
		else
		{
			if(!UtilidadTexto.isEmpty(forma.getEvolucion().getFechaMuerte()))
			{
				
				String fechaAdmision= UtilidadFecha.conversionFormatoFechaAAp(UtilidadValidacion.getFechaAdmision(paciente.getCodigoCuenta()));
				String horaAdminsion= UtilidadFecha.convertirHoraACincoCaracteres(UtilidadValidacion.getHoraAdmision(paciente.getCodigoCuenta()));
				
				boolean centinelaErrorFechas=false;
				if(!UtilidadFecha.esFechaValidaSegunAp(forma.getEvolucion().getFechaMuerte()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "evolucion "+forma.getEvolucion().getFechaEvolucion()));
					centinelaErrorFechas=true;
				}
				if(!centinelaErrorFechas)
				{
					if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getEvolucion().getFechaMuerte(), UtilidadFecha.getFechaActual()))
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Muerte "+forma.getEvolucion().getFechaMuerte(), "Actual "+UtilidadFecha.getFechaActual()));
					}
					else if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), forma.getEvolucion().getFechaMuerte(), forma.getEvolucion().getHoraMuerte()).isTrue())
					{
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "de muerte "+ forma.getEvolucion().getFechaMuerte()+" "+forma.getEvolucion().getHoraMuerte(), " actual "+ UtilidadFecha.getFechaActual()+ " "+UtilidadFecha.getHoraActual()));
					}
					else if(!UtilidadFecha.compararFechas(forma.getEvolucion().getFechaMuerte(), forma.getEvolucion().getHoraMuerte(), fechaAdmision, horaAdminsion).isTrue())
					{
						errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de muerte "+ forma.getEvolucion().getFechaMuerte()+ " "+forma.getEvolucion().getHoraMuerte(), "de admisiï¿½n "+fechaAdmision+" "+horaAdminsion));
					}
				}	
			}
		}
		
		if(forma.getEvolucion().getMuerto().equals("true")&&forma.getEvolucion().getCertificadoDefuncion().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","Seleccionar algo en el campo certificado de defuncion "));
		}
		if(forma.getEvolucion().getMuerto().equals("true")&&forma.getEvolucion().getDiagnosticoMuerte().getAcronimo().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","El diagnostico de Muerte "));
		}
		
		if(forma.getEvolucion().getProfesional().getEspecialidades1().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","La especialidad del Profesional que responde "));
		}
		
		errores=Plantillas.validacionCamposPlantilla(forma.getPlantilla(), errores);
		
		if(errores.isEmpty())
		{	
			if (forma.getEsAdjunto()&&forma.getDeseaFinalizarAtencion()&&UtilidadValidacion.haySolicitudesIncompletasAdjunto(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), paciente.getCodigoCuentaAsocio(), medico.getCodigoInstitucionInt()).isTrue())
			{
				if (forma.getEvolucion().isOrdenSalida())
				{	
					if(ValoresPorDefecto.getValidarEstadoSolicitudesInterpretadas(medico.getCodigoInstitucionInt()).equals("true"))
					{
						errores.add("adjuntoConSolicitudesPendientes", new ActionMessage("error.evolucion.adjuntoConSolicitudesPendientes"));
					}
					else
					{
						errores.add("adjuntoConSolicitudesPendientes", new ActionMessage("error.evolucion.tratanteConSolicitudesPendientes"));
					}
				}	
			}
			
			if (UtilidadValidacion.esMedicoTratante(con, medico, paciente).equals("")&&forma.getOrdenSalida()!=null&&forma.getOrdenSalida().equals("true"))
			{
				//Si el destino a la salida es hospitalizaciï¿½n y estamos en 
				//admisiï¿½n de urgencias NO validamos que queden solicitudes
				//pendientes
				
				if (paciente.getAnioAdmision()>0&&forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaHospitalizacion)
				{
					//En este caso NO se debe validar si hay solicitudes incompletas
					logger.info("En este caso NO se valida si hay solicitudes incompletas");
				}
				else
				{

					/* 
					 * MT 5079 No debe validar que las solicitudes se encuentren respondidas, administradas y/o anuladas
					 * cuando un paciente que se admitio con via de ingreso Hospitalizacion y tipo de paciente Cirugia ambulatoria
					 * y si se le define conducta hospitalizar generar orden de egreso si y destino salida hospitalizar
					 * */
					if(!(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion
							 &&paciente.getCuentasPacienteArray(0).getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria+"")
							 &&forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaHospitalizar)){
						//Para cualquier otro caso si se valida
						ResultadoBoolean resp=UtilidadValidacion.haySolicitudesIncompletasEnCuenta(con, paciente.getCodigoIngreso(), medico.getCodigoInstitucionInt(), true,UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarAdministracionMedEgresoMedico(medico.getCodigoInstitucionInt())));
						if (resp.isTrue())
						{
							if(ValoresPorDefecto.getValidarEstadoSolicitudesInterpretadas(medico.getCodigoInstitucionInt()).equals("true"))
							{
								errores.add("tratanteConSolicitudesPendientes", new ActionMessage("error.evolucion.tratanteConSolicitudesPendientes"));
							}
							else
							{
								errores.add("tratanteConSolicitudesPendientes", new ActionMessage("error.evolucion.tratanteConSolicitudesPendientesRespondidas"));
							}
						}	
					}		
				}
			}
		}
		
		boolean esUrgencias;
		if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		{
			esUrgencias=true;
		}
		else
		{
			esUrgencias=false;
		}
		if (forma.getEvolucion().isOrdenSalida())
		{	
			
			if(forma.getEvolucion().getFechaEvolucion().equals(""))
				forma.getEvolucion().setFechaEvolucion(UtilidadFecha.getFechaActual());
			//Se debe revisar que la fecha dada por el usuario cumpla las condiciones
			String fechaEvolucionString[]=forma.getEvolucion().getFechaEvolucion().split("/");
			int diaEvolucion=Utilidades.convertirAEntero(fechaEvolucionString[0]);
			int mesEvolucion=Utilidades.convertirAEntero(fechaEvolucionString[1]);
			int anioEvolucion=Utilidades.convertirAEntero(fechaEvolucionString[2]);

			//La fecha llega en formato dd/mm/aaaa
			if(!paciente.isHospitalDia())
			{	
				RespuestaValidacion resp1=UtilidadValidacion.existenEvolucionesSuficientesOrdenSalida(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(),  diaEvolucion, mesEvolucion, anioEvolucion, esUrgencias, medico.getCodigoInstitucionInt());
				
				if (!resp1.puedoSeguir)
				{
					String warningSalida="";
					//En este caso aviso (NO se puede dar orden de salida)
				
					//Hay que buscar todas las fechas que faltan
				
					ArrayList fechasQueFaltan=UtilidadValidacion.obtenerListadoEvolucionesNoLlenadas (con, paciente.getCodigoCuenta(),  diaEvolucion, mesEvolucion, anioEvolucion, resp1.textoRespuesta, paciente.getCodigoCuentaAsocio());
				
					if (fechasQueFaltan!=null&&fechasQueFaltan.size()>0)
					{
						for (int i=0;i<fechasQueFaltan.size();i++)
						{
							if (i==0)
							{
								warningSalida=warningSalida+ " " + UtilidadFecha.conversionFormatoFechaAAp((String)fechasQueFaltan.get(i)); 
							}
							else
							{
								warningSalida=warningSalida+ ", " + UtilidadFecha.conversionFormatoFechaAAp((String)fechasQueFaltan.get(i)); 
							}
						}

						errores.add("evolInexistentes", new ActionMessage("error.evolucion.faltanevoluciones", warningSalida,"fdsa"));
						
					}

				}
				if(UtilidadValidacion.existeEvolucionConFechaSuperiorFormatoAp(con, paciente.getCodigoCuenta(), forma.getEvolucion().getFechaEvolucion(), forma.getEvolucion().getHoraEvolucion()) )
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Existe una evolucion con fecha superior a esta fecha de egreso."));
				}
				
				//validaciones de CLAP
				if(paciente.getCodigoSexo()==ConstantesBD.codigoSexoFemenino)
				{
					String cuentaAsociada="";
					if(paciente.getExisteAsocio())
						cuentaAsociada=paciente.getCodigoCuentaAsocio()+"";
					
					/**
					 * MT 7378 y MT 7399
					 * Se agrega la validación de destino de salida para mujeres con solicitudes de parto o cirugía.
					 * 
					 * @author jeilones
					 * @date 16/17/2013
					 * */
					Vector warningCLAPVector= UtilidadValidacion.puedoDarOrdenSalidaXClap(con, paciente.getCodigoCuenta()+"", paciente.getCodigoPersona()+"",cuentaAsociada,forma.getEvolucion().getCodigoDestinoSalida(),true);
					
					logger.info("ESTA MIRANDO EL TAMAï¿½O EN LOS ERRORES >>>>>>"+warningCLAPVector.size());
					
					if(warningCLAPVector.size()>0)
					{
						
						logger.info("ENTRO A VERIFICAR PARTO");
						
						String warningClapStr="";
						for(int w=0; w<warningCLAPVector.size(); w++)
						{
							
							logger.info("CUANTAS TIENE EN LA W >>>>"+w);
							
							if(w==0)
								warningClapStr+=warningCLAPVector.get(w);
							else
								warningClapStr+=", "+warningCLAPVector.get(w);
						}
						
						logger.info("POR QUE NO SE PUEDE DAR ORDEN DE SALIDA >>>>"+warningClapStr);
						
						//evolucionForm.setPuedeDarOrdenSalida("false");
						UtilidadBD.cerrarConexion(con);
						errores.add("", new ActionMessage("error.evolucion.noPuedeDarOrdenSalida", warningClapStr));
						forma.setWarningOrdenSalida("");
					}
				}
				
			}
		 
		}
		
		return errores;
	}


	/**
	 * 
	 * @param forma
	 */
	private void cargarDiagnosticosRelacionados(EvolucionesForm forma)
	{
		int numero = 1;
		
		forma.getEvolucion().setDiagnosticos(new ArrayList<Diagnostico>());
		
		for(int i=0;i<forma.getNumDiagRelacionados();i++)
		{	
			if(UtilidadTexto.getBoolean(forma.getDiagnosticosRelacionados("checkbox_"+i).toString()))
			{
				String[] vector = forma.getDiagnosticosRelacionados(i+"").toString().split(ConstantesBD.separadorSplit);
				Diagnostico diagnostico = new Diagnostico();
				diagnostico.setAcronimo(vector[0]);
				diagnostico.setTipoCIE(Integer.parseInt(vector[1]));
				diagnostico.setNombre(vector[2]);
				diagnostico.setNumero(numero);
				diagnostico.setActivo(true);
				diagnostico.setValorFicha("");
				
				//aunque se llenen todas las estructuras de todas las valoraciones
				//solo una se usarï¿½ y eso depende de donde se haya llamado el mï¿½todo
				forma.getEvolucion().getDiagnosticos().add(diagnostico);
				
				numero++;
			}
		}	
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param medico
	 * @param request 
	 * @return
	 * @throws SQLException 
	 */
	private ActionErrors accionGuardarEvolucion(Connection con, EvolucionesForm forma, PersonaBasica paciente, UsuarioBasico medico, HttpServletRequest request) throws SQLException 
	{
		
		ActionErrors errores  = new ActionErrors();
		
		Evoluciones evolucion= new Evoluciones();
		
		Egreso egreso= new Egreso();
		
		HttpSession session=request.getSession();
		
		boolean transaccion= UtilidadBD.iniciarTransaccion(con);
		
		try
		{
		//int numeroSolicitud=UtilidadValidacion.getCodigoValoracionInicial(con, paciente.getCodigoCuenta());
		int numeroSolicitud=Utilidades.convertirAEntero(forma.getEvolucion().getValoracionAsociada());
		int centroCostos=0;
		
		//MT 6566 -cambia solución de la 5887
		if(forma.getEvolucion().getCodigoConductaSeguir()==ConstantesBD.codigoConductaASeguirCambioTipoMonitoreo)
		{
			centroCostos=forma.getEvolucion().getCentroCostoMonitoreo();
		}
		else
		{
			centroCostos=paciente.getCodigoArea();
		}
		
		int consecutivoEvolucion=ConstantesBD.codigoNuncaValido;
		
		this.cargarDiagnosticosRelacionados(forma);
		
		/**
		 * MT 5568
		 * @author javrammo
		 * Si la conducta a seguir es Continuar en cuidado especial, se debe registrar el tipo de monitoreo que
		 * tiene actualmente el paciente
		 */
		if(forma.getEvolucion().getCodigoConductaSeguir()==ConstantesBD.codigoConductaASeguirContinuarCuidadoEspecial){
			forma.getEvolucion().setProcedQuirurgicosObst(forma.getEvolucion().getInfoCentroCostoValoracion().getIdTipoMonitoreo().toString());
		}
		/**
		 * Fin MT 5568
		 */
		
		
		
		 consecutivoEvolucion=Evoluciones.insertarEvolucionBase(
											con,
											numeroSolicitud,
											forma.getEvolucion().getDiagnosticoComplicacion1().getAcronimo(),
											forma.getEvolucion().getDiagnosticoComplicacion1().getTipoCIE()+"",
											forma.getEvolucion().getFechaEvolucion(),
											forma.getEvolucion().getHoraEvolucion(),
											forma.getEvolucion().getInformacionDadaPaciente(),
											forma.getEvolucion().getDescComplicacion(),
											forma.getEvolucion().getHallazgosImportantes(),
											Utilidades.convertirAEntero(forma.getEvolucion().getProcedQuirurgicosObst()),
											forma.getEvolucion().getPronostico(),
											medico.getCodigoPersona(),
											forma.getEvolucion().isOrdenSalida(),
											forma.getEvolucion().getCodigoTipoEvolucion(),
											ConstantesBD.codigoTipoRecargoSinRecargo,//Se envia quemadao por Tarea 43651.
											forma.getEvolucion().isCobrable(),
											forma.getEvolucion().getCodigoTipoDiagnosticoPrincipal(),
											//forma.getEvolucion().getDatosMedico(),
											medico.getInformacionGeneralPersonalSalud(),
											//MT 6566 -cambia solución de la 5887
											centroCostos,
											forma.getEvolucion().getCodigoConductaSeguir(),
											forma.getEvolucion().getTipoReferencia(),
											Utilidades.convertirAEntero(forma.getEvolucion().getDiasIncapacidad()),
											forma.getEvolucion().getObservacionesIncapacidad(),
											forma.getEvolucion().getEspecialidadProfResponde()
											);
		 
		 forma.getEvolucion().setDatosMedico(medico.getInformacionGeneralPersonalSalud());
		 
		 if(forma.getEvolucion().isCobrable())
		 {
			 
			 
			 	SolicitudEvolucion sol=new SolicitudEvolucion();
				llenarSolicitudEvolucion (sol, consecutivoEvolucion, forma.getEvolucion().getFechaEvolucion(), forma.getEvolucion().getHoraEvolucion(), medico, UtilidadValidacion.getCodigoCentroCostoTratanteMetodoLento(con, paciente.getCodigoCuenta(), medico.getCodigoInstitucionInt()), paciente);
			
				int codigoSolicitud=sol.insertarSolicitudEvolucionTransaccional(con, ConstantesBD.continuarTransaccion);
				sol.actualizarMedicoRespondeTransaccional(con, codigoSolicitud, medico, ConstantesBD.continuarTransaccion);
				ArrayList array=Utilidades.obtenerPoolesMedico(con,UtilidadFecha.getFechaActual(),medico.getCodigoPersona());
				if(array.size()==1)
				{
					sol.actualizarPoolSolicitud(con,codigoSolicitud,Integer.parseInt(array.get(0)+""));
				}
				//Debemos actualizar la solicitud para que 
				//ponga los datos del mï¿½dico que responde
				
				//si no genera cargo pendiente ni exitoso entonces debe abortar la transaccion
				//numeroSOLICITUD-> codigoSolicitud y codigoEvolcucion=codigo
				
				//GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
			    Cargos cargos= new Cargos();
			    transaccion= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
					    																			medico, 
					    																			paciente, 
					    																			false/*dejarPendiente*/, 
					    																			codigoSolicitud, 
					    																			ConstantesBD.codigoTipoSolicitudEvolucion /*codigoTipoSolicitudOPCIONAL*/, 
					    																			paciente.getCodigoCuenta() /*codigoCuentaOPCIONAL*/, 
					    																			ConstantesBD.codigoNuncaValido /*codigoCentroCostoEjecutaOPCIONAL*/, 
					    																			ConstantesBD.codigoNuncaValido /*codigoServicioOPCIONAL*/, 
					    																			1 /*cantidadServicioOPCIONAL*/, 
					    																			ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
					    																			consecutivoEvolucion /*codigoEvolucionOPCIONAL*/,
					    																			/*"" -- numeroAutorizacionOPCIONAL*/
					    																			""/*esPortatil*/,false,forma.getEvolucion().getFechaEvolucion(),
					    																			"" /*subCuentaCoberturaOPCIONAL*/);
						
				if(!transaccion)
				{
					logger.error("Error generando cargo ");
					UtilidadBD.abortarTransaccion(con);
				}
				
				
				ResultadoBoolean res=null;
				if(UtilidadValidacion.esMedicoTratante(con,medico,paciente).equals(""))
					res=sol.cambiarEstadosSolicitudTransaccional(con, codigoSolicitud, 0, ConstantesBD.codigoEstadoHCInterpretada, ConstantesBD.continuarTransaccion);
				else
				{
				    //@todo hacer las validaciones 
				    if(forma.getConvertirMedicoATratante().equals("s"))
				    {
				        Solicitud solicitudObject= new Solicitud();
				        solicitudObject.cargar(con, numeroSolicitud);
				        if(solicitudObject.getTipoSolicitud().getCodigo()==ConstantesBD.codigoTipoSolicitudInterconsulta)
				        {
				            res=solicitudObject.interpretarSolicitudInterconsulta(con, "-", medico);
				        }
				        else
				        {
				            res=sol.cambiarEstadosSolicitudTransaccional(con, codigoSolicitud, 0, ConstantesBD.codigoEstadoHCRespondida, ConstantesBD.continuarTransaccion);	
				        }
				    }
				    else 
				        res=sol.cambiarEstadosSolicitudTransaccional(con, codigoSolicitud, 0, ConstantesBD.codigoEstadoHCRespondida, ConstantesBD.continuarTransaccion);
				}
				if(!res.isTrue())
				{
					logger.error("Error cambiando los estados de la solicitud nï¿½mero "+res.getDescripcion());
				}
				
			 
		 }
		 
		 if(consecutivoEvolucion>0)
		 {
			 
			 /*transaccion= Evoluciones.insertarEvolucionesHospitalarias(
					 					con,
					 					consecutivoEvolucion,
					 					forma.getEvolucion().isDatosSubjetivos(),
					 					forma.getEvolucion().isHallazgosEpicrisis(),
					 					forma.getEvolucion().isAnalisis(),
					 					forma.getEvolucion().isDiagnosticosDefinitivos(),
					 					forma.getEvolucion().isBalanceLiquidosEpicrisis(),
					 					forma.getEvolucion().isPlanManejo()
					 					);*/
			 
			 ///****EPICRISIS***************************************************************************************************************////
			 forma.getEvolucion().setCodigoEvolucion(consecutivoEvolucion+"");
			 transaccion=Epicrisis1.insertarInfoAutomaticaEvolucionEpicrisis(con, forma.getEvolucion(), medico, paciente, forma.getPlantilla());
			 ///****FIN EPICRISIS***********************************************************************************************************////
			 
			 if(transaccion)
			 {
				 
				 for(int i=0;i<forma.getEvolucion().getComentarios().size();i++)
				 {	 
					 
					 if(!forma.getEvolucion().getComentarios().get(i).getValor().equals(""))
					 { 
						 transaccion= Evoluciones.insertarComentariosEvolucion(
								 					con,
								 					consecutivoEvolucion,
								 					forma.getEvolucion().getComentarios().get(i).getValor(),
								 					forma.getEvolucion().getComentarios().get(i).getProfesional().getLoginUsuario()
								 					);
					 }
				 }
				 logger.info(">>>>DP>>>>"+consecutivoEvolucion);
				 logger.info(">>>>DP>>>>"+forma.getEvolucion().getDiagnosticoPrincipal().getAcronimo());
				 logger.info(">>>>DP>>>>"+forma.getEvolucion().getDiagnosticoPrincipal().getTipoCIE());
				 
				 
				 
				 transaccion= Evoluciones.insertarDiagnosticosEvolucion(
						 					con, 
						 					consecutivoEvolucion, 
						 					forma.getEvolucion().getDiagnosticoPrincipal().getAcronimo(), 
						 					forma.getEvolucion().getDiagnosticoPrincipal().getTipoCIE()+"", 
						 					ConstantesBD.codigoNuncaValido, 
						 					true, 
						 					true);
				 
				 			
				 for(int i=0;i<forma.getEvolucion().getDiagnosticos().size();i++)
				 {
					 
					 
					 logger.info(">>>>DR>>>>"+consecutivoEvolucion);
					 logger.info(">>>DR>>>>>"+forma.getEvolucion().getDiagnosticos().get(i).getAcronimo());
					 logger.info(">>>>DR>>>>"+forma.getEvolucion().getDiagnosticos().get(i).getTipoCIE());
					 
					 transaccion= Evoluciones.insertarDiagnosticosEvolucion(con, 
							 					consecutivoEvolucion, 
							 					forma.getEvolucion().getDiagnosticos().get(i).getAcronimo(), 
							 					forma.getEvolucion().getDiagnosticos().get(i).getTipoCIE()+"", 
							 					i+1, 
							 					false, 
							 					false);
					 
				 }
				 
				 
				 if(transaccion)
				 {
					 
					 ResultadoBoolean resultado = new ResultadoBoolean(true,"");
					 
					 //SE revisa si se ingresaron signos vitales para activar plantilla 
					 if(forma.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteSignosVitales)&&SignoVital.ingresoInformacion(forma.getEvolucion().getSignosVitales()))
					 {
						 logger.info("SE MARCï¿½ COMPONENTE DE EVOLUCION========================================================");
						 forma.getPlantilla().marcarComponente(ConstantesCamposParametrizables.tipoComponenteSignosVitales);
					 }
					 logger.info("ï¿½Tiene informacion plantilla?"+forma.getPlantilla().tieneInformacion());
					 resultado= Plantillas.guardarDatosEvolucion(
							 					con, 
							 					forma.getPlantilla(), 
							 					paciente.getCodigoIngreso()+"", 
							 					consecutivoEvolucion+"", 
							 					forma.getEvolucion().getFechaEvolucion(), 
							 					forma.getEvolucion().getHoraEvolucion(), 
							 					medico);
					 
					 if(!resultado.isTrue())
					 {	 
							errores.add("", new ActionMessage("errors.notEspecific",resultado.getDescripcion()));
							transaccion=false;
					 }
					 
					 if(forma.getPlantilla().existeComponente(ConstantesCamposParametrizables.tipoComponenteSignosVitales))
					 {	 
						 for(int i=0;i<forma.getEvolucion().getSignosVitales().size();i++)
						 {
							 
							 if(!forma.getEvolucion().getSignosVitales().get(i).getValorSignoVital().equals(""))
							 {
								 transaccion=Evoluciones.insertarEvolSignosVitales(
										 		con, 
										 		consecutivoEvolucion, 
										 		forma.getEvolucion().getSignosVitales().get(i).getCodigo(),
										 		forma.getEvolucion().getSignosVitales().get(i).getDescripcion(),
										 		forma.getEvolucion().getSignosVitales().get(i).getValorSignoVital());
							 }	 
							 
						 }
					 }
					 
					 
					 logger.info("ADJUNTO >>>>>>>>>>>"+forma.getEsAdjunto());
					 logger.info("FINALIZAR ATENCION >>>>>>>>>>>"+forma.getDeseaFinalizarAtencion());
					 logger.info("MEDICO TRATANTE >>>>>>>>>>>"+forma.getConvertirMedicoATratante());
					 
					 //////Evolucion Vieja
					if (forma.getEsAdjunto()&&forma.getDeseaFinalizarAtencion())
					{
					    request.setAttribute("manejoConjuntoFinalizado", "true");
						//Tres posibles sub-casos:
						//-a. Desea aceptar mï¿½dico tratante 
						//-b. Desea dejar Mï¿½dico tratante pendiente
						//-c. Definitivamente NO quiere ser mï¿½dico tratante

						if (forma.getConvertirMedicoATratante().equals("s"))
						{
							logger.info("Caso 1-a");
							//-a. Desea aceptar mï¿½dico tratante 
							Solicitud solicitud= new Solicitud();
							if (paciente.getExisteAsocio())
							{
								solicitud.cambiosRequeridosAceptacionTratanteCasoAsocio (con, forma.getNumeroSolicitudPidioConvertirMedicoATratante(), paciente.getCodigoCuenta());
							}
							resultado = solicitud.inactivarSolicitudCambioTratante(con, forma.getNumeroSolicitudPidioConvertirMedicoATratante());
							if( !resultado.isTrue() )
							{
								logger.warn("Problemas inactivando la solicitud de cambio de mï¿½dico tratante "+resultado.getDescripcion());
								throw new SQLException ("Problemas inactivando la solicitud de cambio de mï¿½dico tratante : "+resultado.getDescripcion());						
							}
							else
							{
								if(!Utilidades.actualizarAreaCuenta(con,paciente.getCodigoCuenta(),medico.getCodigoCentroCosto()))
					    			errores.add("", new ActionMessage("errors.problemasGenericos","cambiando el ï¿½rea del paciente a "+medico.getCodigoCentroCosto()));
								resultado = solicitud.insertarTratante(con, forma.getNumeroSolicitudPidioConvertirMedicoATratante(), medico.getCodigoPersona(), medico.getCodigoCentroCosto(), true);
								//En este caso NO hay que finalizar la atenciï¿½n de forma 
								//automï¿½tica, esto se hace al final con la nota del mï¿½dico
								ResultadoBoolean resultado2 = solicitud.inactivarSolicitudCambioTratante(con, forma.getNumeroSolicitudPidioConvertirMedicoATratante());
								
								if( !resultado.isTrue()||!resultado2.isTrue())
								{
									logger.warn("Problemas insertando el mï¿½dico tratante "+resultado.getDescripcion());						
									throw new SQLException ("Problemas insertando el mï¿½dico tratante  : "+resultado.getDescripcion());											
								}
							}

							Solicitud finalizarAtencionConjunta=new Solicitud();
							
							if (transaccion)
							{
								finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), forma.getNotaFinalizacionAtencion(), numeroSolicitud, "finalizar");
							}
							else
							{
								finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), forma.getNotaFinalizacionAtencion(), numeroSolicitud, "continuar");
							}

						}
						
						if (forma.getConvertirMedicoATratante().equals("p"))
						{
							logger.info("Caso 1-b");
							//-b. Desea dejar Mï¿½dico tratante -
							Solicitud finalizarAtencionConjunta=new Solicitud();
							
							if (transaccion)
							{
								if(UtilidadValidacion.esMedicoTratante(con,medico,paciente).equals(""))
									finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), 0, forma.getNotaFinalizacionAtencion(), numeroSolicitud, ConstantesBD.finTransaccion);
								else
									finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), forma.getNotaFinalizacionAtencion(), numeroSolicitud, ConstantesBD.finTransaccion);
									
							}
							else
							{
								if(UtilidadValidacion.esMedicoTratante(con,medico,paciente).equals(""))
									finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), 0, forma.getNotaFinalizacionAtencion(), numeroSolicitud, ConstantesBD.continuarTransaccion);
								else
									finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), forma.getNotaFinalizacionAtencion(), numeroSolicitud, ConstantesBD.continuarTransaccion);
							}
							
							UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),request.getSession().getServletContext());
						}
						
						if (forma.getConvertirMedicoATratante().equals("n"))
						{
							logger.info("Caso 1-c");
							//-c. Definitivamente NO quiere ser mï¿½dico tratante
							Solicitud solicitud= new Solicitud();
							resultado = solicitud.inactivarSolicitudCambioTratante(con, forma.getNumeroSolicitudPidioConvertirMedicoATratante());
							if( !resultado.isTrue() )
							{
								logger.warn("Problemas inactivando la solicitud de cambio de mï¿½dico tratante "+resultado.getDescripcion());
								throw new SQLException ("Problemas inactivando la solicitud de cambio de mï¿½dico tratante : "+resultado.getDescripcion());						
							}
						
							//Como los anteriores mï¿½todos no tienen estados transaccionales
							//el ï¿½ltimo mï¿½todo en llamarse es el de finalizar atenciï¿½n
							//conjunta
							Solicitud finalizarAtencionConjunta=new Solicitud();
							if (transaccion)
							{
								finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), forma.getNotaFinalizacionAtencion(), numeroSolicitud, "finalizar");
							}
							else
							{
								finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), forma.getNotaFinalizacionAtencion(), numeroSolicitud, "continuar");
							}
						}
						
					}
					
					//Los siguientes casos corresponden a cuando NO se desea fin.
					//atenciï¿½n (o simplemente No se tenï¿½a atenciï¿½n)
					//Caso lï¿½nea 1: Es adjunto y no desea finalizar atenciï¿½n
					//Caso lï¿½nea 2: No es adjunto
					
					if ((forma.getEsAdjunto()&&!forma.getDeseaFinalizarAtencion())
						||!forma.getEsAdjunto())
					{
						//Tres posibles sub-casos:
						//-a. Desea aceptar mï¿½dico tratante 
						//-b. Desea dejar Mï¿½dico tratante pendiente
						//-c. Definitivamente NO quiere ser mï¿½dico tratante
						//-d. Tratante que desea finalizar manejo conjunto
						
						if (forma.getConvertirMedicoATratante().equals("s"))
						{
							logger.info("Caso 2-a");
							//-a. Desea aceptar mï¿½dico tratante
							Solicitud solicitud= new Solicitud();
							if (paciente.getExisteAsocio())
							{
								solicitud.cambiosRequeridosAceptacionTratanteCasoAsocio (con, forma.getNumeroSolicitudPidioConvertirMedicoATratante(), paciente.getCodigoCuenta());
							}
							resultado = solicitud.inactivarSolicitudCambioTratante(con, forma.getNumeroSolicitudPidioConvertirMedicoATratante());
							if( !resultado.isTrue() )
							{
								logger.warn("Problemas inactivando la solicitud de cambio de mï¿½dico tratante "+resultado.getDescripcion());
								throw new SQLException ("Problemas inactivando la solicitud de cambio de mï¿½dico tratante : "+resultado.getDescripcion());						
							}
							else
							{
								//Se actualiza el ï¿½rea de la cuenta (con el centro de costo del usuario)
					    		if(!Utilidades.actualizarAreaCuenta(con,paciente.getCodigoCuenta(),medico.getCodigoCentroCosto()))
					    			errores.add("", new ActionMessage("errors.problemasGenericos","cambiando el ï¿½rea del paciente a "+medico.getCodigoCentroCosto()));
								resultado = solicitud.insertarTratante(con, forma.getNumeroSolicitudPidioConvertirMedicoATratante(), medico.getCodigoPersona(), medico.getCodigoCentroCosto(), true);
								//En este caso hay que finalizar la atenciï¿½n de forma automï¿½tica
							    request.setAttribute("manejoConjuntoFinalizado", "true");
								solicitud.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), "Finalizaciï¿½n de Atenciï¿½n conjunta generada automï¿½ticamente al aceptar convertirse en Mï¿½dico Tratante", numeroSolicitud, ConstantesBD.continuarTransaccion);
								ResultadoBoolean resultado2 = solicitud.inactivarSolicitudCambioTratante(con, forma.getNumeroSolicitudPidioConvertirMedicoATratante());
								
								//@todo en este punto se hace una actualizacion del estado His cli de la solicitud a la cual se pidio convertir a medico tratante
													
								if( !resultado.isTrue()||!resultado2.isTrue())
								{
									logger.warn("Problemas insertando el mï¿½dico tratante "+resultado.getDescripcion());						
									throw new SQLException ("Problemas insertando el mï¿½dico tratante  : "+resultado.getDescripcion());											
								}
							}
							
						}
						
						//-b. Desea dejar Mï¿½dico tratante pendiente:
						//Se maneja con el primer caso (Todo NO)

						//-c. Definitivamente NO quiere ser mï¿½dico tratante
						if (forma.getConvertirMedicoATratante().equals("n"))
						{
							logger.info("Caso 2-c");
							Solicitud solicitud= new Solicitud();
							resultado = solicitud.inactivarSolicitudCambioTratante(con, forma.getNumeroSolicitudPidioConvertirMedicoATratante());
							if( !resultado.isTrue() )
							{
								logger.warn("Problemas inactivando la solicitud de cambio de mï¿½dico tratante "+resultado.getDescripcion());
								throw new SQLException ("Problemas inactivando la solicitud de cambio de mï¿½dico tratante : "+resultado.getDescripcion());						
							}
						}
						
						//-d. El tratante desea finalizar manejo conjunto
						if (forma.getConvertirMedicoATratante().equals("p")&&!forma.getEsAdjunto() && forma.getDeseaFinalizarAtencion())
						{
							logger.info("Caso 2-d entra!!!!!!!!!!!!");
							Solicitud finalizarAtencionConjunta=new Solicitud();
							if (transaccion)
								finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), 0, forma.getNotaFinalizacionAtencion(), numeroSolicitud, ConstantesBD.finTransaccion);
							else
								finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), 0, forma.getNotaFinalizacionAtencion(), numeroSolicitud, ConstantesBD.continuarTransaccion);
							
							UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),request.getSession().getServletContext());
						}
						
						
						//si es tratante y tiene la opcion de generar ordebn de salida en true entonces se debe finalizar el manejo conjunto
						logger.info("tratante->"+forma.getEsTratante()+" ordensalida->"+forma.getEvolucion().isOrdenSalida()+" tiene manejo conjunto->"+paciente.getManejoConjunto());
						
						if(paciente.getManejoConjunto())
						{	
							if(forma.getEsTratante() && forma.getEvolucion().isOrdenSalida())
							{
								logger.info("si es tratante y tiene la opcion de generar ordebn de salida en true entonces se debe finalizar el manejo conjunto");
								Solicitud finalizarAtencionConjunta=new Solicitud();
								if (transaccion)
									finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), 0, "egreso", numeroSolicitud, ConstantesBD.finTransaccion);
								else
									finalizarAtencionConjunta.finalizarAtencionConjuntaTransaccional(con, paciente.getCodigoCuenta(), 0, "egreso", numeroSolicitud, ConstantesBD.continuarTransaccion);
								UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),request.getSession().getServletContext());
							}
						}	
					}
					
					//////////// Traslado cuidado especial.
					if(forma.getEvolucion().getCodigoConductaSeguir()==ConstantesBD.codigoConductaASeguirCambioTipoMonitoreo)
					{
						transaccion=evolucion.actualizarIngresoCuidadoEspecial(
												con,
												paciente.getCodigoIngreso(),
												forma.getEvolucion().getProcedQuirurgicosObst(),
												consecutivoEvolucion,
												forma.getEvolucion().getCentroCostoMonitoreo(),
												medico);
						
						if(transaccion)
						{
							transaccion=evolucion.actualizarAreaCuenta(con, paciente.getCodigoCuenta(), forma.getEvolucion().getCentroCostoMonitoreo(), medico);
							if(transaccion)
							{
								UtilidadBD.finalizarTransaccion(con);								
								UtilidadesManejoPaciente.cargarPaciente(con, medico, paciente, request);
							}
						}	
						//UtilidadSesion.notificarCambiosObserver(paciente.getCodigoPersona(),request.getSession().getServletContext());
						//UtilidadesManejoPaciente.cargarPaciente(con, medico, paciente, request);
						
					}
					
					
					if(forma.getEvolucion().isOrdenSalida())
					{
						
						forma.setMapaE(Utilidades.consultaUltimoEgresoEvolucion(con, paciente.getCodigoPersona(), paciente.getCodigoUltimaViaIngreso()));
						int minutosParametro=0, minutosT=0;
						
						logger.info("Esta pasando a verificar el REINGRESO antes>>>>>>>>>>"+forma.getMapaE("numRegistros"));
						
						//forma.setIndI(1);
						
						if(Integer.parseInt(forma.getMapaE("numRegistros").toString())>0)
						{
							
							logger.info("Esta pasando a verificar el REINGRESO>>>>>>>>>>");
							
							String fechaInicial=UtilidadFecha.conversionFormatoFechaAAp(forma.getMapaE("fecha_0")+"");
							String horaInicial="";
							if(forma.getMapaE("hora_0").equals(""))
								horaInicial="00:00";
							else
								horaInicial=forma.getMapaE("hora_0").toString();
							String fechaFinal=paciente.getFechaIngreso();
							String horaFinal=paciente.getHoraIngreso();
							if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
							{
								logger.info("Via Hospitalizacion>>>>>>");
								minutosParametro=Utilidades.convertirAEntero(ValoresPorDefecto.getTiempoMaximoReingresoHospitalizacion(medico.getCodigoInstitucionInt()),true);
								logger.info("Via Hospitalizacion Despues>>>>>>"+minutosParametro);
							}
							else
								if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
								{
									logger.info("Via Urgencias>>>>>>");
									minutosParametro=Utilidades.convertirAEntero(ValoresPorDefecto.getTiempoMaximoReingresoUrgencias(medico.getCodigoInstitucionInt()),true);
									logger.info("Via Urgencias Despues>>>>>>"+minutosParametro);
								}	
							minutosT=UtilidadFecha.numeroMinutosEntreFechas(fechaInicial, horaInicial, fechaFinal, horaFinal);
							
							logger.info("minutosT>>>>>>>>>>"+minutosT);
							
							if(minutosT<=minutosParametro)
							{
								logger.info("Esta dando para salir>>>>>>>>>");
								forma.setIndI(1);
							}
						}
						
						ArrayList diagnosticos=forma.getEvolucion().getDiagnosticos();
						Diagnostico diagnosticoRelacionado1=new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado, ConstantesBD.codigoCieDiagnosticoNoSeleccionado, false);
						Diagnostico diagnosticoRelacionado2=new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado, ConstantesBD.codigoCieDiagnosticoNoSeleccionado, false);
						Diagnostico diagnosticoRelacionado3=new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado, ConstantesBD.codigoCieDiagnosticoNoSeleccionado, false);
						for(int i=0; i<diagnosticos.size();i++)
						{
							switch(i)
							{
								case 0:
									diagnosticoRelacionado1=(Diagnostico)diagnosticos.get(i);
								break;
								case 1:
									diagnosticoRelacionado2=(Diagnostico)diagnosticos.get(i);
								break;
								case 2:
									diagnosticoRelacionado3=(Diagnostico)diagnosticos.get(i);
								break;
								
							}
						}
						
						String acronimoComplicacion=forma.getEvolucion().getDiagnosticoComplicacion1().getAcronimo();
						int tipoCieComplicacion=forma.getEvolucion().getDiagnosticoComplicacion1().getTipoCIE();
						String acronimoMuerte=forma.getEvolucion().getDiagnosticoMuerte().getAcronimo();
						int tipoCieMuerte=forma.getEvolucion().getDiagnosticoMuerte().getTipoCIE();
						boolean muerto=false;
						
						if(acronimoComplicacion.equals("")||tipoCieComplicacion==ConstantesBD.codigoNuncaValido)
						{
							acronimoComplicacion=ConstantesBD.acronimoDiagnosticoNoSeleccionado;
							tipoCieComplicacion=ConstantesBD.codigoCieDiagnosticoNoSeleccionado;
						}
						if(acronimoMuerte.equals("")||tipoCieMuerte==ConstantesBD.codigoNuncaValido)
						{
							acronimoMuerte=ConstantesBD.acronimoDiagnosticoNoSeleccionado;
							tipoCieMuerte=ConstantesBD.codigoCieDiagnosticoNoSeleccionado;
						}
						if(forma.getEvolucion().getMuerto().equals("true"))
						{
							muerto=true;
						}
						
						int causaExterna= evolucion.consultarCausaExternaValoracion(con, paciente.getCodigoCuenta(), forma.getEvolucion().getCodigoTipoEvolucion());
						
						transaccion=evolucion.insertarEgresoAutomatico(con, 
												paciente.getCodigoCuenta(), 
												consecutivoEvolucion, 
												muerto, 
												forma.getEvolucion().getCodigoDestinoSalida(), 
												forma.getEvolucion().getOtroDestinoSalida(), 
												"", 
												causaExterna, 
												acronimoMuerte, 
												tipoCieMuerte, 
												forma.getEvolucion().getDiagnosticoPrincipal().getAcronimo(), 
												forma.getEvolucion().getDiagnosticoPrincipal().getTipoCIE(), 
												diagnosticoRelacionado1.getAcronimo(), 
												diagnosticoRelacionado1.getTipoCIE(), 
												diagnosticoRelacionado2.getAcronimo(), 
												diagnosticoRelacionado2.getTipoCIE(), 
												diagnosticoRelacionado3.getAcronimo(), 
												diagnosticoRelacionado3.getTipoCIE(), 
												medico.getCodigoPersona(), 
												acronimoComplicacion, 
												tipoCieComplicacion);
						
						
						
						 if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion&&paciente.getCuentasPacienteArray(0).getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria+"")
									&& (forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaHospitalizar||forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaTrasladoCuidadoEspecial))
						{
							
							 
							////////////////////////////////////////////////////////
							if(egreso.generarEgresoCompleto(
									con,										
									forma.getEvolucion().getFechaEvolucion(), 
									UtilidadFecha.getFechaActual(), 
									forma.getEvolucion().getHoraEvolucion(), 
									UtilidadFecha.getHoraActual(), 
									medico, 
									paciente,
									false))
							{					
								//Se hace un asocio automatico
								Cuenta cuenta=new Cuenta();
								cuenta.init(System.getProperty("TIPOBD"));
								try
								{
									//logger.info("SE ASOCIA LA CUENTA!");
									if (cuenta.asociarCuenta(con, paciente.getCodigoCuenta(),paciente.getCodigoIngreso(), medico.getLoginUsuario()) <=0)
									{
										if (con!=null&&!con.isClosed())
										{
											UtilidadBD.closeConnection(con);
										}
										request.setAttribute("codigoDescripcionError", "errors.problemasBd");
										
									}
								}
								catch (Exception e)
								{
									if (con!=null&&!con.isClosed())
									{
										UtilidadBD.closeConnection(con);
									}
									request.setAttribute("codigoDescripcionError", "errors.problemasBd");
									
								}
								// C?igo necesario para notificar a todos los observadores que la cuentadel paciente en sesi? pudo haber cambiado
								UtilidadesManejoPaciente.cargarPaciente(con, medico, paciente, request);
								
								//Quitar la cama en el encabezado de paciente
								PersonaBasica pacienteActivo = (PersonaBasica)session.getAttribute("pacienteActivo");
								pacienteActivo.setCama("");
								UtilidadesManejoPaciente.cargarPaciente(con, medico, pacienteActivo, request);
								
								if(paciente.getCodigoCama()>0)
								{
									//Si el paciente tenï¿½a cama se finaliza el traslado
									TrasladoCamas traslado = new TrasladoCamas();
									if(!traslado.actualizarFechaHoraFinalizacionNoTransaccional(con,paciente.getCodigoCuenta(),UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),""))
										transaccion = false;
								}
								
								logger.info("Respuesta: Fue Creado el Egreso y el Nuevo Asocio");
								
								//Cambiar estado cama.
								this.cambiarEstadoCama(con, forma, medico, paciente);
							}
							else
								logger.info("Respuesta: No se Genero el Egreso, ni el Asocio");
							
						}
						//si es via ingreso hospitalaizaxion y destino dado de alta o remitido, se debe pasar la cama a con salida
						 else if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion&& (forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaDadoDeAlta||forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaRemitidoOtroNivelComplejidad || forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaVoluntaria))
							{
							//Mt 4942: valida entrada por via de Ingreso Hospitalizacion y tipo de ingreso Cirugia ambulatoria 
							  if((paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion
										 &&paciente.getCuentasPacienteArray(0).getCodigoTipoPaciente().equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria+"")
											&& ((forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaDadoDeAlta
											||forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaVoluntaria)))){
								  //Mt 4942: Actualiza egreso 
								  if(egreso.ActualizarDatosCirugia(con,forma.getEvolucion().getFechaEvolucion(), 
											UtilidadFecha.getFechaActual(), 
											forma.getEvolucion().getHoraEvolucion(), 
											UtilidadFecha.getHoraActual(), 
											medico, 
											paciente));
						        	}
						  		this.cambiarEstadoCama(con, forma, medico, paciente);
							}
						 else if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias && 
									(forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaHospitalizacion || 
											forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaCirugiaAmbulatoria||
											forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaTrasladoCuidadoEspecial||
											forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaDadoDeAlta ||
											//---------modificado por tarea 79740--------------------------------------------------------------------
											forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaVoluntaria ||
											forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaRemitidoOtroNivelComplejidad ||
											forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaOtro
											//--------------------------------------------------------------------------------------------------------
											) 
											&&!paciente.getExisteAsocio())
						{
							 
							 
							 logger.info("VA DAR ASOCIO DE CUENTA A URGENCIAS >>>>>>> "+paciente.getCodigoCama());
							
							 if(egreso.generarEgresoCompleto(
										con,										
										forma.getEvolucion().getFechaEvolucion(), 
										UtilidadFecha.getFechaActual(), 
										forma.getEvolucion().getHoraEvolucion(), 
										UtilidadFecha.getHoraActual(), 
										medico, 
										paciente,
										true))
							 {	
								
							 
								 if(forma.getEvolucion().getCodigoDestinoSalida()!=ConstantesBD.codigoDestinoSalidaDadoDeAlta &&
								   //---------modificado por tarea 79740--------------------------------------------------------------------
									forma.getEvolucion().getCodigoDestinoSalida()!=ConstantesBD.codigoDestinoSalidaVoluntaria &&
									forma.getEvolucion().getCodigoDestinoSalida()!=ConstantesBD.codigoDestinoSalidaRemitidoOtroNivelComplejidad &&
									forma.getEvolucion().getCodigoDestinoSalida()!=ConstantesBD.codigoDestinoSalidaOtro
									//--------------------------------------------------------------------------------------------------------
								  )
								 {
									//Se hace un asocio automatico
									Cuenta cuenta=new Cuenta();
									cuenta.init(System.getProperty("TIPOBD"));
									try
									{										
										if(cuenta.asociarCuenta(con, paciente.getCodigoCuenta(),paciente.getCodigoIngreso(), medico.getLoginUsuario()) <=0)
										{
											if (con!=null&&!con.isClosed())
											{
												UtilidadBD.closeConnection(con);
											}
											request.setAttribute("codigoDescripcionError", "errors.problemasBd");
										}
										else
										{
											logger.info("Se Genero el Asocio!!.");
										}
										
									}
									catch (Exception e)
									{
										if (con!=null&&!con.isClosed())
										{
											UtilidadBD.closeConnection(con);
										}
										request.setAttribute("codigoDescripcionError", "errors.problemasBd");
										
									}
								 }
								// C?igo necesario para notificar a todos los observadores que la cuentadel paciente en sesi? pudo haber cambiado
								UtilidadesManejoPaciente.cargarPaciente(con, medico, paciente, request);
								
								//Quitar la cama en el encabezado de paciente
								PersonaBasica pacienteActivo = (PersonaBasica)session.getAttribute("pacienteActivo");
								pacienteActivo.setCama("");
								UtilidadesManejoPaciente.cargarPaciente(con, medico, pacienteActivo, request);
								
								//Se finaliza la asignacion de cama a observacion
								AdmisionUrgencias admisionUrgencias=new AdmisionUrgencias();
								if(admisionUrgencias.actualizarPorOrdenSalidaTransaccional(con, paciente.getCodigoAdmision(), paciente.getAnioAdmision(), UtilidadFecha.getFechaActual(con), UtilidadFecha.getHoraActual(con), ConstantesBD.continuarTransaccion)<=0)
									transaccion = false;
								
								logger.info("Respuesta: Fue Creado el Egreso y el Nuevo Asocio");
								
								//Cambiar estado cama.
								this.cambiarEstadoCama(con, forma, medico, paciente);
								
							 }	
							else
								logger.info("Respuesta: No se Genero el Egreso");
						}	
						
						if(forma.getEvolucion().getMuerto().equals("true"))
						{
							transaccion=UtilidadValidacion.actualizarPacienteAMuertoTransaccional(con,paciente.getCodigoPersona(),false,forma.getEvolucion().getFechaMuerte(), forma.getEvolucion().getHoraMuerte(), forma.getEvolucion().getCertificadoDefuncion(),ConstantesBD.continuarTransaccion);
						}
						else
						{
							//Se activa el registro de incapacidades***************************************************
							DtoRegistroIncapacidades registro = new DtoRegistroIncapacidades();
							registro.getGrabacion().setUsuarioModifica(medico.getLoginUsuario());
							registro.setIngreso(paciente.getCodigoIngreso());
							registro.setEvolucion(consecutivoEvolucion);
							registro.setAcronimoDiagnostico(forma.getEvolucion().getDiagnosticoPrincipal().getAcronimo());
							registro.setTipoCie(forma.getEvolucion().getDiagnosticoPrincipal().getTipoCIE());
							registro.setEspecialidad(Utilidades.convertirAEntero(forma.getEvolucion().getEspecialidadProfResponde()));
							
							transaccion=RegistroIncapacidades.activarRegistrosIncapacidades(con, registro).isTrue();
							//******************************************************************************************
						}
						
					}
					
				 }	
				 else
				 {
					transaccion=false; 
				 }
				 
			 }
			 else
			 {
				 transaccion=false;
			 }
			 
		 }
		 else
		 {
			 transaccion=false;
		 }
		
		 if(transaccion)
		 {
			//forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
			forma.setCodigoUltimaEvolucion(consecutivoEvolucion);
			
			
			//Almacenan las Curvas de Crecimiento modificadas del paciente
			
			HistoriaClinicaFacade historiaClinicaFacade = new HistoriaClinicaFacade();
			
			@SuppressWarnings("unchecked")
			List<CurvaCrecimientoPacienteDto> curvasCrecimientoPaciente = (List<CurvaCrecimientoPacienteDto>)session.getAttribute("curvasCrecimientoPaciente");
			
			//Verifica que se halla guardado en el componente de Curvas de Crecimiento
			if(curvasCrecimientoPaciente != null){
				//Recorre la Lista de Curvas de Crecimiento del paciente 
				for(CurvaCrecimientoPacienteDto curvaCrecimiento : curvasCrecimientoPaciente){
					//Verifica que la Curva de Crecimiento halla sido modificada
					if(curvaCrecimiento.isGraficaDiligenciada()){
						
						DatosAlmacenarCurvaCrecimientoDto dtoDatosAlmacenarCurvaCrecimiento = new DatosAlmacenarCurvaCrecimientoDto();
						dtoDatosAlmacenarCurvaCrecimiento.setNumeroSolicitud(numeroSolicitud);
						dtoDatosAlmacenarCurvaCrecimiento.setCodigoCurvaParametrizada(curvaCrecimiento.getDtoCurvaCrecimientoParametrizab().getId());
						dtoDatosAlmacenarCurvaCrecimiento.setImagenBase64(curvaCrecimiento.getImagenBase64());
						dtoDatosAlmacenarCurvaCrecimiento.setCodigoImagenParametrizada(curvaCrecimiento.getDtoCurvaCrecimientoParametrizab().getDtoImagenesParametrizadas().getId());
						dtoDatosAlmacenarCurvaCrecimiento.setCoordenadasPuntos(curvaCrecimiento.getCoordenadasCurvaCrecimiento());
						dtoDatosAlmacenarCurvaCrecimiento.setEsValoracion(false);
						try{
							//Se almacena la Curva de Crecimiento asociada a la Evolucion
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
		 }
		 else
		 {
			//forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			 errores.add("", new ActionMessage("errors.notEspecific","Se presento un error durante la insercion de la evolucion.")); 
			UtilidadBD.abortarTransaccion(con);
		 }
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("Error al registrar la evolución: "+e);
		}
		 
		 
		return errores;
	}

		 
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 */		
	 private void cambiarEstadoCama(Connection con, EvolucionesForm forma, UsuarioBasico usuario, PersonaBasica paciente) 
		{
			logger.info("***********************************************************ACTUALIZACION CAMA ******************************************************************");
			logger.info("\n DESTINO SALIDA-->"+forma.getEvolucion().getCodigoDestinoSalida());
			if(forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaDadoDeAlta
			 	|| forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaRemitidoOtroNivelComplejidad
			 	|| forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaVoluntaria
			 	|| forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaHospitalizacion
			 	|| forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaCirugiaAmbulatoria
			 	|| forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaTrasladoCuidadoEspecial
			 	//------- modificado por tarea  79740 --------------------------------------------------------------------
			 	|| forma.getEvolucion().getCodigoDestinoSalida()==ConstantesBD.codigoDestinoSalidaOtro)
				//-----------------------------------------------------------------------------------------------------
			{
				TrasladoCamas traslado= new TrasladoCamas();
				int codigoCama=Camas1.obtenerCamaDadaCuenta(con, paciente.getCodigoCuenta()+"");
				logger.info("CAMA-->"+codigoCama);
				if(codigoCama>0)
				{	
					
					int codigoEstadoCamaActual = Utilidades.obtenerCodigoEstadoCama(con, codigoCama);
					try 
					{
							
						//int trasladoCama=traslado.modificarEstadoCama(con, Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoEstadoCama(usuario.getCodigoInstitucionInt())), codigoCama, usuario.getCodigoInstitucionInt());
						//segun el anexo el estado de la cama debe ser, ConstantesBD.codigoEstadoCamaConSalida
						int trasladoCama;
						if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
						{
							trasladoCama=traslado.modificarEstadoCama(con, Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoEstadoCama(usuario.getCodigoInstitucionInt())), codigoCama, usuario.getCodigoInstitucionInt());
						}
						else
						{
							trasladoCama=traslado.modificarEstadoCama(con, ConstantesBD.codigoEstadoCamaConSalida, codigoCama, usuario.getCodigoInstitucionInt());
						}
						
							logger.info("trasladoCama>>>>>>>>>>"+trasladoCama);
							
								UtilidadesManejoPaciente.insertarLogCambioEstadoCama(
										con, 
										codigoCama, 
										codigoEstadoCamaActual, 
										Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoEstadoCama(usuario.getCodigoInstitucionInt())), 
										UtilidadFecha.getFechaActual(con), 
										UtilidadFecha.getHoraActual(con), 
										usuario.getLoginUsuario(), 
										paciente.getCodigoCuenta()+"", 
										usuario.getCodigoInstitucionInt(), 
										usuario.getCodigoCentroAtencion());
						
						
					} 
					catch (SQLException e) 
					{
						e.printStackTrace();
					}
				}
				//Sila vï¿½a de ingreso es urgencias y no tenï¿½a cama, igual se debe registrar la fecha egreso a observacion
				else if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
				{
					AdmisionUrgencias adminUrgencias = new AdmisionUrgencias();
					try 
					{
						adminUrgencias.actualizarPorOrdenSalidaTransaccional(con, paciente.getCodigoAdmision(), paciente.getAnioAdmision(), forma.getEvolucion().getFechaEvolucion(), forma.getEvolucion().getHoraEvolucion(), ConstantesBD.continuarTransaccion);
					} 
					catch (SQLException e) 
					{
						logger.error("Error registrando la fecha/hora egreso a observacion de la admision de urgencias: "+e);
					}
				}
			}
			logger.info("***********************************************************FIN CAMA ******************************************************************");
		}
		 
	 
	
	 
	 /**
	  * 
	  * @param sol
	  * @param codigoEvolucion
	  * @param fechaEvolucion
	  * @param horaEvolucion
	  * @param medico
	  * @param centroCostoTratante
	  * @param paciente
	  */
	 public void llenarSolicitudEvolucion (SolicitudEvolucion sol, int codigoEvolucion, String fechaEvolucion, String horaEvolucion, UsuarioBasico medico, int centroCostoTratante, PersonaBasica paciente)
		{
			sol.setCodigoEvolucion(codigoEvolucion);
			sol.setFechaSolicitud(fechaEvolucion);
			sol.setHoraSolicitud(horaEvolucion);
			sol.setTipoSolicitud(new InfoDatosInt (ConstantesBD.codigoTipoSolicitudEvolucion, ""));
			sol.setCobrable(true);
			
			sol.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaTodos, ""));
			sol.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoOcupacionMedicaTodos));
			
			//El mï¿½dico que lo crea NO es el solicitante, bï¿½sicamente porque
			//la interconsulta es la responsable de su modificaciï¿½n
			sol.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea(), ""));
			sol.setCentroCostoSolicitado(new InfoDatosInt(paciente.getCodigoArea(), ""));
			sol.setCodigoMedicoSolicitante(medico.getCodigoPersona());
			sol.setCodigoCuenta(paciente.getCodigoCuenta());
			sol.setVaAEpicrisis(false);
			sol.setUrgente(false);
		}
	 
	 
	 
	 
	 
	 
	 
	 
	 
	/**
	 * M?todo para consultar el hist?rico de los signos vitales de registro enfermer?a
	 * ingresados al paciente en las ?ltimas 24 horas
	 * @param con
	 * @param usuario 
	 * @param evolucionesForm
	 * @param codigoInstitucion
	 * @param codigoArea
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 */
	private void accionCargarHistoricoSignosVitales(Connection con, UsuarioBasico usuario, EvolucionesForm evolucionForm, int codigoInstitucion, String cuentas) 
	{
		logger.info("1");
		RegistroEnfermeria mundo=new RegistroEnfermeria();
		logger.info("2");
		//---Se obtiene la fecha y hora del d?a anterior  
		//String fechaInicio = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), -1, false)) + "-" + UtilidadFecha.getHoraActual();
		//String fechaInicio = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), -1, false));
		String fechaInicio=obtenerFechasHistorico(usuario, true);
		logger.info("3");
		
		//-----------Se cargan los tipos de signos vitales parametrizados por institucion centro de costo-----------//
		evolucionForm.setSignosVitalesInstitucionCcosto(mundo.consultarTiposInstitucionCCosto(con, codigoInstitucion, cuentas, 1));
		logger.info("4");
		
	 	//----Se quitan los tipos de signos vitales repetidos en la coleccion------//
		evolucionForm.setSignosVitalesInstitucionCcosto(Utilidades.coleccionSinRegistrosRepetidos(evolucionForm.getSignosVitalesInstitucionCcosto(), "codigo_tipo"));
		logger.info("5");
	 	//------------- Consulta el hist?rico de los signos vitales fijos--------------------------//
		evolucionForm.setSignosVitalesFijosHisto(mundo.consultarSignosVitalesFijosHisto(con, cuentas, fechaInicio, ""));
		logger.info("6");
	 	//------------- Consulta el historico de los signos vitales pamatrizados por institucion centro costo--------------------------//
	 	if(evolucionForm.getSignosVitalesInstitucionCcosto().size()>0)
	 		evolucionForm.setSignosVitalesParamHisto(mundo.consultarSignosVitalesParamHisto(con, cuentas, codigoInstitucion, fechaInicio, ""));
	 	logger.info("7");
	 	//------------- Consulta los codigo historicos, fecha registro y hora registro de los signos vitales fijos y parametrizados--------------------------//
	 	if(evolucionForm.getSignosVitalesFijosHisto().size()>0 || evolucionForm.getSignosVitalesParamHisto().size()>0)
	 		evolucionForm.setSignosVitalesHistoTodos(mundo.consultarSignosVitalesHistoTodos(con, cuentas, codigoInstitucion, fechaInicio, ""));
	 	logger.info("8");
	}
	
	
	
	private String obtenerFechasHistorico(UsuarioBasico usuario, boolean formatoBD)
	{
		String fechaActual=UtilidadFecha.getFechaActual();
		if(formatoBD)
		{
			fechaActual=UtilidadFecha.conversionFormatoFechaABD(fechaActual);
		}
		String horaActual=UtilidadFecha.getHoraActual();
		String fechaInicio="";
		//String fechaFin="";

		String horaPrimerTurno=ValoresPorDefecto.getHoraInicioPrimerTurno(usuario.getCodigoInstitucionInt());
		//String horaUltimoTurno=ValoresPorDefecto.getHoraFinUltimoTurno(usuario.getCodigoInstitucionInt());
		//if(horaActual.equals(horaPrimerTurno))
			//{
			//fechaInicio=fechaActual+"-"+horaActual;
			//}
		//else if(horaActual.compareTo(horaPrimerTurno)>0)
			//{
			//fechaInicio=fechaActual+"-"+horaPrimerTurno;
			//}
		//else if(horaActual.compareTo(horaPrimerTurno)<0)
		{
			if(formatoBD)
			{
				fechaInicio=UtilidadFecha.incrementarDiasAFecha(fechaActual, -2, formatoBD)+"-"+horaPrimerTurno;
			}
			else
			{
				fechaInicio=UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), -2, formatoBD)+"-"+horaPrimerTurno;
			}
		}
		return fechaInicio;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param medico
	 * @return
	 * @throws SQLException 
	 */
	private ActionErrors ValidacionesEvolucion(Connection con, EvolucionesForm forma, PersonaBasica paciente, UsuarioBasico medico) throws SQLException 
	{
		ActionErrors errores = new ActionErrors();
		Evoluciones evoluciones = new Evoluciones();
		
		boolean bandera = false; //bandera que indica si se necesita interpretar primero
		boolean bandera2 = false; //bandera para validar especialidad del medico solicitante y el que va a evolucionar
		
		
		if(paciente == null)
		{
			errores.add("Error paciente no cargado",new ActionMessage("errors.paciente.noCargado"));
		}
		boolean esUrgencias;
		if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		{
			esUrgencias=true;
		}
		else
		{
			esUrgencias=false;
		}
		//Primera Condiciï¿½n: El usuario debe existir
		//la validaciï¿½n de si es mï¿½dico o no solo se hace en insertar
		if( medico == null )
		{
			errores.add("No existe el usuario",new ActionMessage("errors.usuario.noCargado"));
		}
		else if( paciente == null || paciente.getCodigoTipoIdentificacionPersona().equals("") )
		{
			//Segunda Condiciï¿½n: Debe haber un paciente cargado
			errores.add("paciente null o sin  id",new ActionMessage("errors.paciente.noCargado"));
		}
		
		
		//Validaciï¿½n de autoatencion
		ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(medico, paciente);
		
		logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<. Resp: " + respuesta);
		
		
		if(respuesta.isTrue())
			errores.add("", new ActionMessage(respuesta.getDescripcion()));
		
		if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
		{
			errores.add("errors.ingresoEstadoDiferente",new ActionMessage("errors.ingresoEstadoDiferenteAbierto"));
		}
		else if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
		{
			errores.add("errors.paciente.cuentaNoValida",new ActionMessage("errors.paciente.cuentaNoValida"));
		}
		else if(paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoHospitalizacion&&paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoUrgencias)
		{
			errores.add("Solo Urgencias/Hospitalizacion",new ActionMessage("errors.opcionDisponible","pacientes en hospitalizaciï¿½n y/o urgencias"));
		}
		/**
		 * Validar concurrencia
		 * Si ya estï¿½ en proceso de distribucion, no debe dejar entrar
		 **/
		else if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), medico.getLoginUsuario()) )
		{
			errores.add("error.facturacion.cuentaEnProcesoDistribucion",new ActionMessage("error.facturacion.cuentaEnProcesoDistribucion"));
		}
		/**
		 * Validar concurrencia
		 * Si ya estï¿½ en proceso de facturaciï¿½n, no debe dejar entrar
		 **/
		else if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
		{
			errores.add("error.facturacion.cuentaEnProcesoFact", new ActionMessage("error.facturacion.cuentaEnProcesoFact"));
		}

		
		RespuestaValidacion fechaCuentaOAdmision=UtilidadValidacion.tieneFechaAdmisionCuenta(con, paciente.getCodigoCuenta(), paciente.getCodigoAdmision(), paciente.getAnioAdmision());
		forma.setDeboMostrarMensajeCancelacionTratante(UtilidadValidacion.deboMostrarMensajeCancelacionTratanteEnIngresoEvolucion(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto()));
		String arregloFechaCuentaOAdmision[]=new String[0];

		
		if (!fechaCuentaOAdmision.puedoSeguir)
		{
		    //return ComunAction.accionSalirCasoError(mapping, request, con, logger, fechaCuentaOAdmision.textoRespuesta, fechaCuentaOAdmision.textoRespuesta, false) ;
		    errores.add("error.facturacion.cuentaEnProcesoFact", new ActionMessage(fechaCuentaOAdmision.textoRespuesta));
		}
		else
		{
			arregloFechaCuentaOAdmision=fechaCuentaOAdmision.textoRespuesta.split("-");
		}
		
		forma.setFechaCuentaOAdmision(arregloFechaCuentaOAdmision[2] + "/"  + arregloFechaCuentaOAdmision[1] + "/" + arregloFechaCuentaOAdmision[0]  );

		//logger.info("2. NumSol=> "+forma.getNumeroSolicitudPidioConvertirMedicoATratante()+", esAdjunto=> "+forma.getEsAdjunto());
		
		String elementos[]=UtilidadValidacion.obtenerMaximaFechaYHoraEvolucion(con, paciente.getCodigoCuenta());
		if (elementos!=null)
		{
			forma.setFechaMaximaEvolucionFormatoBD(elementos[0]);
			forma.setHoraMaximaEvolucion(elementos[1]);
		}
		else
		{
			//Solo validamos valoraciï¿½n cuando NO hay evoluciones
			elementos=UtilidadValidacion.obtenerMaximaFechaYHoraValoracion(con, paciente.getCodigoCuenta());
			if (elementos!=null)
			{
				forma.setFechaMaximaValoracionFormatoBD(elementos[0]);
				forma.setHoraMaximaValoracion(elementos[1]);
			}
		}
		
		elementos=UtilidadValidacion.obtenerFechaYHoraPrimeraValoracion(con, paciente.getCodigoCuenta());
		if (elementos!=null)
		{
			forma.setFechaPrimeraValoracionFormatoBD(elementos[0]);
			forma.setHoraPrimeraValoracion(elementos[1]);
		}
		
		//Ahora vamos a revisar la existencia de una valoraciï¿½n
		//del mismo centro de costo, pero en caso de asocio cuentas,
		//nos saltamos esa validaciï¿½n
		RespuestaValidacionTratante resp=UtilidadValidacion.validarIngresarEvolucion (con, paciente, medico);
		
		//Si no puedo seguir me voy a la pï¿½gina de error y como descripciï¿½n
		//envio el resultado de la validaciï¿½n
		if (!resp.puedoSeguir)
		{
			errores.add("errors.ingresoEstadoDiferente",new ActionMessage("errors.notEspecific",resp.textoRespuesta));
		}
		else if(resp.textoRespuesta.equals("evolucion"))
		{
			int codigoEvolucion= evoluciones.obtenerCodigoUltimaEvolucion(con, paciente.getCodigoCuenta());
			
			String [] InfoOrdenEgreso= evoluciones.consultarOrdenEgreso(con, codigoEvolucion);
			
			logger.info(">>>>>>FF "+InfoOrdenEgreso[0]);
			logger.info(">>>>>>FF "+InfoOrdenEgreso[1]);
			logger.info(">>>>>>FF "+InfoOrdenEgreso[2]);
			logger.info(">>>>>>FF "+InfoOrdenEgreso[3]);
			
			forma.setCodigoConductaSeguir(Utilidades.convertirAEntero(InfoOrdenEgreso[3]));
			forma.getEvolucion().setOrdenSalida(true);
			forma.setOrdenSalida("true");
			forma.getEvolucion().setCodigoDestinoSalida(Utilidades.convertirAEntero(InfoOrdenEgreso[1]));
			forma.getEvolucion().setEstadoSalida(false);
			forma.getEvolucion().setMuerto("false");
			forma.setModificableAsocio(true);
			
		}
		else
		{
			//El mï¿½dico puede ingresar los datos, vamos a definir
			//si le aparece la opciï¿½n de salida
			if (resp.esTratante)
			{
			    forma.setEsTratante(true);
				//Si el mï¿½dico es tratante, puede dar orden de salida
				//forma.setPuedeDarOrdenSalida("true");
				logger.info("3. NumSol=> "+forma.getNumeroSolicitudPidioConvertirMedicoATratante()+", esAdjunto=> "+forma.getEsAdjunto());
				//En caso que el mï¿½dico tenga toda la autoridad
				//de dar la orden de salida, deben haber tantas
				//evoluciones como dï¿½as desde la primera evoluciï¿½n
				//y si no los hay debemos avisarle al mï¿½dico que
				//faltan

				GregorianCalendar calendario = new GregorianCalendar();
					
				int diaActual  = calendario.get(Calendar.DAY_OF_MONTH);
				//Java maneja el mes empezando en 0, asï¿½ que debemos subirlo
				int mesActual  = calendario.get(Calendar.MONTH)+1;
				int anioActual = calendario.get(Calendar.YEAR);
				
				//logger.info("1");
				if(!paciente.isHospitalDia())
				{	
					RespuestaValidacion resp1=UtilidadValidacion.existenEvolucionesSuficientesOrdenSalida(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), diaActual, mesActual, anioActual, esUrgencias, medico.getCodigoInstitucionInt());
					//logger.info("2");
					if (!resp1.puedoSeguir)
					{
						String warningSalida="No se puede dar orden de salida. Faltan las evoluciones de ";
						//En este caso aviso (NO se puede dar orden de salida)
						//Hay que buscar todas las fechas que faltan
						ArrayList fechasQueFaltan=UtilidadValidacion.obtenerListadoEvolucionesNoLlenadas (con, paciente.getCodigoCuenta(),  diaActual, mesActual, anioActual, resp1.textoRespuesta, paciente.getCodigoCuentaAsocio());
						
						//Cuando no hay ningun elemento en fechas que faltan
						//indica que el usuario estï¿½ dando una fecha anterior 
						
						if (fechasQueFaltan!=null&&fechasQueFaltan.size()>0)
						{
							for (int i=0;i<fechasQueFaltan.size();i++)
							{
								if (i==0)
								{
									warningSalida=warningSalida+ " " + UtilidadFecha.conversionFormatoFechaAAp((String)fechasQueFaltan.get(i)); 
								}
								else
								{
									warningSalida=warningSalida+ ", " + UtilidadFecha.conversionFormatoFechaAAp((String) fechasQueFaltan.get(i)); 
								}
							}
							forma.setWarningOrdenSalida(warningSalida);
						}
					}
				}	
				
				//validaciones de CLAP
				if(paciente.getCodigoSexo()==ConstantesBD.codigoSexoFemenino)
				{
					String cuentaAsociada="";
					if(paciente.getExisteAsocio())
						cuentaAsociada=paciente.getCodigoCuentaAsocio()+"";
					
					/**
					 * MT 7378 y MT 7399
					 * Se agrega la validación de destino de salida para mujeres con solicitudes de parto o cirugía.
					 * 
					 * @author jeilones
					 * @date 16/17/2013
					 * */
					Vector warningCLAPVector= UtilidadValidacion.puedoDarOrdenSalidaXClap(con, paciente.getCodigoCuenta()+"", paciente.getCodigoPersona()+"", cuentaAsociada,forma.getEvolucion().getCodigoDestinoSalida(),false);
					if(warningCLAPVector.size()>0)
					{
						String warningClapStr="No se puede dar orden de salida. Porque: ";
						for(int w=0; w<warningCLAPVector.size(); w++)
						{
							if(w==0)
								warningClapStr+=warningCLAPVector.get(w);
							else
								warningClapStr+=", "+warningCLAPVector.get(w);
						}
						forma.setWarningOrdenSalida(warningClapStr);
						//evolucionForm.setPuedeDarOrdenSalida("false");
					}
				}	
				
				/*if(paciente.getManejoConjunto())
				{
					logger.info("PACIENTE CON MANEJO CONJUNTO!!!!!!!");
					evolucionForm.setWarningOrdenSalida(evolucionForm.getWarningOrdenSalida()+
									"\nNo se puede dar orden de salida ya que " +
									"el paciente tiene manejo conjunto");
					evolucionForm.setPuedeDarOrdenSalida("false");
				}*/
			}
			else
			{
				//forma.setPuedeDarOrdenSalida("false");
				if(forma.getEsAdjunto())
				{
					/*if(paciente.getManejoConjunto())
					{
						logger.info("PACIENTE CON MANEJO CONJUNTO!!!!!!!");
						evolucionForm.setWarningOrdenSalida(evolucionForm.getWarningOrdenSalida()+
										"\nNo se puede dar orden de salida ya que " +
										"el paciente tiene manejo conjunto");*/
						forma.setPuedeDarOrdenSalida("true");
					//}
				}
			}
		}
		
		//this.cargarWarningsReferencia(con, forma, paciente);
		
		
		logger.info("\n\n\n\n evolucionForm.getEsAdjunto()-->"+forma.getEsAdjunto()+"\n\n\n");
		logger.info("\n\n\n\n paciente.getManejoConjunto()-->"+paciente.getManejoConjunto()+"\n\n\n");
		
		//Dependiendo si es hosp o urg se muestran ciertos campos y
		//ciertas opciones
		
		//Primero revisamos si tiene Admision
		if (paciente.getCodigoAdmision()!=0)
		{ 
			
			//Si se tiene anio de admision es de urgencias
			if (paciente.getAnioAdmision()!=0)
			{
				
				if(UtilidadValidacion.estaEnCamaObservacion(con, paciente.getCodigoCuenta()))
				{
					forma.setTipoEvolucion("u");
					
				}
				/**else
				{
				    errores.add("No se puede hacer evoluciï¿½n en urgencias a menos que el paciente este en cama de observaciï¿½n",new ActionMessage("errors.notEspecific","No se puede hacer evoluciï¿½n en urgencias a menos que el paciente este en cama de observaciï¿½n"));
				}**/
			}
			else
			{
				forma.setTipoEvolucion("h");
			}
		}
		else
		{
			//Si no tiene nï¿½mero de admisiï¿½n lo ponemos en 'otros'
			forma.setTipoEvolucion("o");
		}
		
		
		RespuestaValidacion respMotivoRev=UtilidadValidacion.deboMostrarMotivoReversionEgreso(con, paciente.getCodigoCuenta());
		
		if(respMotivoRev.puedoSeguir)
		{				
			
			forma.setDebeDarMotivoRegresionEgreso("true");
			forma.setMotivoReversionEgreso(respMotivoRev.textoRespuesta);
			//Se carga la fecha y hora de la reversiï¿½n
			/*Egreso egreso=new Egreso();
			egreso.setNumeroCuenta(paciente.getCodigoCuenta());
			logger.info("Codigo de la cuenta del paciente=> "+paciente.getCodigoCuenta());
			logger.info("Estado Cargado de datos reversion=> "+egreso.cargarReversionEgreso(con));
			logger.info("Fecha de la reversion => "+egreso.getFechaReversion());
			logger.info("Hora de la reversion => "+egreso.getHoraReversion());*/
			
			//forma.setFechaEvolucion(egreso.getFechaReversion());
			//forma.setHoraEvolucion(egreso.getHoraReversion());
			
		}
		else
		{
			forma.setDebeDarMotivoRegresionEgreso("false");
		}
		
		if(UtilidadValidacion.tipoMonitoreoRequiereValoracion(con, paciente.getCodigoCuenta())&&!UtilidadValidacion.tieneValoracionCuidadoEspecial(con, paciente.getCodigoCuenta()))
		{
			errores.add("descripcion",new ActionMessage("errors.notEspecific","PACIENTE CON VALORACION DE INGRESO A CUIDADOS ESPECIALES PENDIENTE."));
		}
		
		Evolucion evolucion= new Evolucion();
		String parametroControlaInter =Evolucion.consultarParametroControlaInterpretacion(con);
		

		logger.info("\n\n");
		logger.info("VALOR PARAMETRO GENERAL CONTROLA INTERPRETACION ->"+parametroControlaInter);
		logger.info("\n\n");

		
		// PREGUNTO SI ESTA ACTIVO EL PARAMETRO GENERAL SE CONTROLA PROCEDIMIENTOS PARA PERMITIR EVOLUCIONAR
		if(parametroControlaInter.equals(ConstantesBD.acronimoSi)) 
		{
			logger.info("///////////// Parametro Activo - LA CUENTA DEL PACIENTE ES ->"+paciente.getCodigoCuenta());
			
			forma.setEspecialidadesCuentaMapa(evolucion.consultarEspecialidadesCuenta(con, paciente.getCodigoCuenta())); 
			
			logger.info(">>>>> MAPA ESPECIALIDADES ->>>"+forma.getEspecialidadesCuentaMapa());
			
			int numRegEspecial = Utilidades.convertirAEntero(forma.getEspecialidadesCuentaMapa().get("numRegistros").toString());
			
			logger.info("NumReg EspeciaMedico: " + medico.getEspecialidades().length);
			logger.info("NumRegEspecial: " + numRegEspecial);

			
			for(int x=0; x<medico.getEspecialidades().length; x++)
			{
				logger.info("Posicion >>>"+x+" Especialidad ->"+medico.getEspecialidades()[x].getCodigo());
				
				for(int y=0; y<numRegEspecial;y++)
				{
					logger.info("ESPECIALIDADES MEDICO>>>>>>>>>>>>"+medico.getEspecialidades()[x].getCodigo());
					logger.info("ESPECIALIDADES CUENTA>>>>>>>>>>>>"+forma.getEspecialidadesCuentaMapa().get("especialidad_"+y));

					// servicio(s) requiere(n) interpretacion 84889
					if(forma.getEspecialidadesCuentaMapa().get("interpretar_"+y).toString().equals(ConstantesBD.acronimoSi))
					{
						logger.info("el Servicio Requiere Interpretacion");
						bandera = true;
						//errores.add("No se permite Evolucionar, Existen Ordenes sin Interpretar, por Favor Verifique",new ActionMessage("errors.notEspecific","PARA INSERTAR UNA NUEVA EVOLUCION SE REQUIERE INTERPRETAR PROCEDIMIENTOS PENDIENTES. POR FAVOR VERIFIQUE."));

						// Si es TRUE SE debe presentar el LINK a la funcionalidad INTERPRETAR PROCEDIMIENTOS del modulo Ordenes
						//forma.setControlInterpretacion(true);
					}
					
					
					// evolucionar --el medico no posee la especialidad para interpretar con la que se creo la solicitud
					if(medico.getEspecialidades()[x].getCodigo()== Utilidades.convertirAEntero(forma.getEspecialidadesCuentaMapa().get("especialidad_"+y).toString()))
					{
						//errores.add("No se permite Evolucionar, Existen Ordenes sin Interpretar, por Favor Verifique",new ActionMessage("errors.notEspecific","EL PROFESIONAL DE LA SALUD NO POSEE LA ESPECIALIDAD PARA INTERPRETAR."));
						// Si es TRUE SE debe presentar el LINK a la funcionalidad INTERPRETAR PROCEDIMIENTOS del modulo Ordenes
						//forma.setControlInterpretacion(true);
						
						//el medico que va a evolucionar tiene la misma especialidad que el medico que soilcito
						bandera2 = true;
					}
				}
			}
			
			//HashMap temp = new HashMap();
			//temp = evolucion.validarEspecialidadesMedico(con, -1, -1);
			//logger.info("7777777777777777777777777777777777777777777");
			//logger.info("numRegistros : " + temp.get("numRegistros"));
			//Utilidades.imprimirMapa(temp);
			//if(Integer.parseInt(temp.get("numRegistros").toString()) > 0)
				//forma.setControlInterpretacion(true);
			

			if((bandera) && (bandera2) ) {
				forma.setControlInterpretacion(true);
				errores.add("No se permite Evolucionar, Existen Ordenes sin Interpretar, por Favor Verifique",new ActionMessage("errors.notEspecific","PARA INSERTAR UNA NUEVA EVOLUCION SE REQUIERE INTERPRETAR PROCEDIMIENTOS PENDIENTES. POR FAVOR VERIFIQUE."));
			}
			
			
			//if(forma.isControlInterpretacion())
				//errores.add("No se permite Evolucionar, Existen Ordenes sin Interpretar, por Favor Verifique",new ActionMessage("errors.notEspecific","PARA INSERTAR UNA NUEVA EVOLUCION SE REQUIERE INTERPRETAR PROCEDIMIENTOS PENDIENTES. POR FAVOR VERIFIQUE."));
			
			/*if(!forma.isControlInterpretacion())
				errores.add("No se permite Evolucionar, Existen Ordenes sin Interpretar, por Favor Verifique",new ActionMessage("error.evolucion.controlInterpretacion"));*/
			
			medico.getEspecialidades();
		}
		
		/*
		 * Postulaciï¿½n del tipo de diagnï¿½stico principal
		 * ya sea de la ï¿½ltima evoluciï¿½n o de la valoraciï¿½n inicial
		 */
		forma.getEvolucion().setCodigoTipoDiagnosticoPrincipal(UtilidadValidacion.obtenerTipoDiagnosticoPrincipal(con, paciente.getCodigoCuenta(), UtilidadValidacion.tieneEvoluciones(con, paciente.getCodigoCuenta())));

		/*
		 * Postulaciï¿½n del tipo de monitoreo
		 */
		forma.getEvolucion().setProcedQuirurgicosObst(Utilidades.obtenerUltimoTipoMonitoreo(con, paciente.getCodigoCuenta(), medico.getCodigoInstitucionInt(), false));
		
		
		return errores;
		
	}

	
	
	/**
	 * Mï¿½todo para cargar al mapa los diagnosticos relacionados que se encuentren en la estructura de la valoraciï¿½n
	 * @param forma
	 */
	private void asignarDiagnosticosRelacionados(EvolucionesForm forma)
	{
		//forma.diagnosticosRelacionados = new HashMap<String, Object>();
		int contador = 0;
		
		/**
		 * Nota * Cuando este mï¿½todo se llame solo se entrarï¿½ a un FOR dependiendo
		 * de cual tipo de valoraciï¿½n se estï¿½ usando en ese momento
		 */
		for(Diagnostico diagnostico:forma.getEvolucion().getDiagnosticos())
			if(!diagnostico.isPrincipal())
			{
				forma.setDiagnosticosRelacionados(contador+"", diagnostico.getValor());
				//forma.setDiagnosticosRelacionados("valorFicha_"+contador, ""); //se inserta ficha vacï¿½a
				forma.setDiagnosticosRelacionados("checkbox_"+contador, "true"); 
				contador++;
			}
		
		forma.setNumDiagRelacionados(contador);
			
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario 
	 * @param paciente
	 */
	private void cargarDiagnosticosValoracion(Connection con, EvolucionesForm forma, UsuarioBasico usuario, PersonaBasica paciente)
	{
		
		int numeroSolicitud=UtilidadValidacion.getCodigoValoracionInicial(con, paciente.getCodigoCuenta());
		logger.info("numeroSolicitud=> "+numeroSolicitud);
		
		
		Valoraciones mundoValoracion = new Valoraciones();
		mundoValoracion.setNumeroSolicitud(numeroSolicitud+"");
		mundoValoracion.cargarHospitalizacion(con, usuario, paciente, false);
		DtoValoracionHospitalizacion valoracionHospitalizacion= mundoValoracion.getValoracionHospitalizacion();
		
		
		forma.getEvolucion().setDiagnosticoIngreso(valoracionHospitalizacion.getDiagnosticoIngreso());
		
		if(!Utilidades.isEmpty(valoracionHospitalizacion.getDiagnosticos()))
			forma.getEvolucion().setDiagnosticoPrincipal(valoracionHospitalizacion.getDiagnosticos().get(0));
		
		
		try
		{
			String diagnosticosSeleccionados = "";
			//forma.setDiagnosticoDefinitivo("fechaval",valoracion.getFechaValoracion());
			for(int i=1;i<valoracionHospitalizacion.getDiagnosticos().size();i++)
			{
				forma.getEvolucion().getDiagnosticos().add(valoracionHospitalizacion.getDiagnosticos().get(i));
				diagnosticosSeleccionados += (diagnosticosSeleccionados.equals("")?"":",") + "'"+valoracionHospitalizacion.getDiagnosticos().get(i).getAcronimo()+"'";
			}
			//forma.setDiagnosticoDefinitivo("numdiagval",valoracion.getDiagnosticos().size()+"");
			forma.setDiagnosticosSeleccionados(diagnosticosSeleccionados);
			
		}
		catch (Exception e)
		{
			//forma.setDiagnosticoDefinitivo("numdiagval","0");
			logger.error("Error cargando la valoraciï¿½n "+numeroSolicitud+": "+e);
		}
		//logger.info("DIAGNOSTICOS SELECCIONADOS=> "+forma.getDiagnosticosSeleccionados());
	}
	
	
	
	/**
	 * Mï¿½todo que maneja las acciones correspondientes a mostrar
	 * o no el warning, revisa el caso de manejo adjunto cuando el 
	 * mï¿½dico entra a la funcionalidad de insertar una nueva evoluciï¿½n. 
	 * Tambiï¿½n incluye la inicializaciï¿½n del parametro que dice si tiene 
	 * solicitud de cambio de tratante o no
	 * 
	 * @param con Conexiï¿½n con la fuente de datos
	 * @param evolucionForm Forma de Evolucion
	 * @param medico Mï¿½dico que accede a la funcionaldiad
	 * @param paciente Paciente al que pertenece la evoluciï¿½n
	 * a crear
	 * @throws SQLException
	 * @throws IPSException 
	 */	
	public void accionesManejoWarning (Connection con, EvolucionesForm forma, UsuarioBasico medico, PersonaBasica paciente) throws SQLException, IPSException
	{
		logger.info("entra!!!!");
		//Guardamos el nï¿½mero de la solicitud, si este nï¿½mero
		//es mayor que 0, hay que mostrar mensaje
		forma.setNumeroSolicitudPidioConvertirMedicoATratante(UtilidadValidacion.existeSolicitudTransferenciaManejoPreviaDadoCentroCosto(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto()));
		forma.setEsAdjunto(UtilidadValidacion.esAdjuntoCuenta(con, paciente.getCodigoCuenta() ,medico.getLoginUsuario()));
		
		logger.info("777777777777 - es Adjunto !!!! " + forma.getEsAdjunto());

		//Si el mï¿½dico es adjunto revisamos si debemos
		//ponerle el warning
		if (forma.getEsAdjunto())
		{
			logger.info("++++++++ 1");
			if(!forma.getEsTratante())
			{		
				logger.info("+++++++++++ 2");
				ResultadoBoolean respAdjunto=UtilidadValidacion.haySolicitudesIncompletasAdjunto(con, paciente.getCodigoCuenta(), medico.getCodigoCentroCosto(), paciente.getCodigoCuentaAsocio(), medico.getCodigoInstitucionInt());
				if (respAdjunto.isTrue())
				{
					logger.info("+++++++++ 3 ");
					if (forma.getWarningOrdenSalida()!=null&&!forma.getWarningOrdenSalida().equals(""))
					{
						logger.info("+++++++++++ 4");
						forma.setWarningOrdenSalida(forma.getWarningOrdenSalida() + "<br/>No puede Finalizar la Atenciï¿½n porque: " + respAdjunto.getDescripcion());
					}
					else
					{
						logger.info("+++++++++ 5");
						forma.setWarningOrdenSalida("No puede Finalizar la Atenciï¿½n porque: " + respAdjunto.getDescripcion());
					}
				}
			}	
		}
		
		//Ahora vamos a revisar si es el tratante y si se le debe
		//mostrar el mensaje de que no va a poder dar orden
		//de salida SI no tiene todas las solicitudes interpretadas
		//anuladas
		if(UtilidadValidacion.esMedicoTratante(con, medico, paciente).equals(""))
		{
			logger.info("++++++++ 6 ");
			
			//se verifica que el paciente tenga manejo conjunto
			if(paciente.getManejoConjunto()) {
				logger.info("++++++++ 7 ");
				forma.setPuedoFinalizarManejo(true);
			}
				
			ResultadoBoolean resp=UtilidadValidacion.haySolicitudesIncompletasEnCuenta(con, paciente.getCodigoIngreso(), medico.getCodigoInstitucionInt(), true,UtilidadTexto.getBoolean(ValoresPorDefecto.getValidarAdministracionMedEgresoMedico(medico.getCodigoInstitucionInt())));

			if (resp.isTrue())
			{
				logger.info("+++++++++ 8 ");
				if (forma.getWarningOrdenSalida()!=null&&!forma.getWarningOrdenSalida().equals(""))
				{
					logger.info("+++++++++++ 9");
					forma.setWarningOrdenSalida(forma.getWarningOrdenSalida() + "<br/> No puede dar Orden de Salida porque: " + resp.getDescripcion());
					//evolucionForm.setPuedeDarOrdenSalida("false");
				}
				else
				{
					logger.info("++++++++++++ 10");
					forma.setWarningOrdenSalida(" No puede dar Orden de Salida porque: " + resp.getDescripcion());
					//evolucionForm.setPuedeDarOrdenSalida("false");
				}
			}
		}
		
		//se verifica si se puede finalizar manejo
		if(forma.getEsAdjunto()||forma.isPuedoFinalizarManejo()) {
			logger.info("+++++++++++ 11");
			forma.setPuedoFinalizarManejo(true);
		}
		
		
		forma.setTipoPaciente(paciente.getCodigoTipoPaciente());
		forma.setUltimaViaIngreso(paciente.getCodigoUltimaViaIngreso());
		if(forma.getUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			logger.info("+++++++++++ 12 ");
			forma.getEvolucion().setCodigoTipoEvolucion(ConstantesBD.codigoEspecialidadValoracionHospitalizacion);
		}
		else
		{
			logger.info("+++++++++ 13");
			forma.getEvolucion().setCodigoTipoEvolucion(ConstantesBD.codigoEspecialidadValoracionUrgencias);
		}
		
		Evoluciones evoluciones= new Evoluciones();
		
		int codigoEvolucion= evoluciones.obtenerCodigoUltimaEvolucion(con, paciente.getCodigoCuenta());
		if(codigoEvolucion>0)
		{
			logger.info("++++++++++++ 14");

			if((UtilidadValidacion.monitoreoEvolucionRequiereValoracion(con, codigoEvolucion))&&!UtilidadValidacion.tieneIngresoActivoCuidadoEspecial(con, paciente.getCodigoCuenta()))
			{
				logger.info("++++++++++++++ 15");

				forma.setWarningOrdenSalida("PACIENTE CON TRASLADO A CUIDADOS ESPECIALES. PENDIENTE ASIGNACION CAMA. POR FAVOR VERIFICAR.");
				forma.setCodigoConductaSeguir(ConstantesBD.codigoConductaASeguirTrasladoCuidadoEspecial);
			}
			if(UtilidadValidacion.conductaSeguirUltimaEvolucion(con, codigoEvolucion)&&!UtilidadValidacion.tieneIngresoCuidadosFinalizado(con, paciente.getCodigoIngreso()))
			{
				logger.info("++++++++++++++++++ 16");

				forma.setWarningOrdenSalida("PACIENTE CON TRASLADO A PISO. PENDIENTE ASIGNACION CAMA PACIENTE CUIDADO ESPECIAL A PISO. POR FAVOR VERIFICAR.");
				forma.setCodigoConductaSeguir(ConstantesBD.codigoConductaASeguirTrasladarAPisoEvolucion);
			}
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 */
	private void cargarPlantillaEvolucion(Connection con, EvolucionesForm forma, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		
		forma.setPlantilla(Plantillas.cargarPlantillaXEvolucion(con,
											usuario.getCodigoInstitucionInt(), 
											paciente.getCodigoArea(), 
											paciente.getCodigoSexo(), 
											ConstantesBD.codigoNuncaValido,
											false,
											ConstantesBD.codigoNuncaValido,
											ConstantesBD.codigoNuncaValido,
											ConstantesBD.codigoNuncaValido,
											paciente.getCodigoSexo(),
											UtilidadFecha.numeroDiasEntreFechas(paciente.getFechaNacimiento(), UtilidadFecha.getFechaActual(con)),
											true));
		
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 */
	private void cargarPlantillaEvolucionResumen(Connection con, EvolucionesForm forma, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		
		forma.setPlantilla(Plantillas.cargarPlantillaXEvolucion(con,
											usuario.getCodigoInstitucionInt(), 
											ConstantesBD.codigoNuncaValido,
											ConstantesBD.codigoNuncaValido, 
											Plantillas.obtenerCodigoPlantillaXEvolucion(con, forma.getCodigoUltimaEvolucion()),
											true,
											ConstantesBD.codigoNuncaValido,
											ConstantesBD.codigoNuncaValido,
											forma.getCodigoUltimaEvolucion(),
											ConstantesBD.codigoNuncaValido,
											ConstantesBD.codigoNuncaValido,
											false));
		
		
	}
	
	/**
	 * Metodo para cargar los historicos de la dieta 
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param forma
	 * @param tipoInfomacion : es el tipo de informacion que se va a cargar.
	 * @param request
	 * @return
	 * @throws SQLException 
	 */
	private void cargarHistoricosDieta(Connection con, UsuarioBasico usuario, PersonaBasica paciente, EvolucionesForm forma) throws SQLException
	{
		RegistroEnfermeria mundo = new RegistroEnfermeria();
		String fechaFin = "";

			//---Cargar los nobres y los codigos de la informacion parametrizada de liquidos (Administrados y Eliminados). 
			mundo.cargarDietaEvolucion(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaFin, forma.getMapaDieta());

			
			//---Incrementar un dia para Mostrar Todos los Historicos a partir de una fecha determinada. 
			fechaFin = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), -1, false)) + "-" + UtilidadFecha.getHoraActual();
			
			fechaFin = fechaFin + "&&&&" + UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()) + "-" + UtilidadFecha.getHoraActual();
			
			HashMap mp = new HashMap();
			
			//----------------------------Consultar los nombres de los medicamentos Administrados  
			//mp = mundo.cargarHistoricosDieta(con, paciente.getCodigoCuenta(), usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt(), fechaFin, 1, paciente.getCodigoCuentaAsocio());
			mp = mundo.cargarHistoricosDieta(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaFin, 1);
			forma.setMapaDietaHistorico("nroRegMedAdm", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			forma.getMapaDietaHistorico().putAll(mp);
			
			//------Cargar los codigo y los nombres de los liquidos eliminados. (SOLAMENTE HAY PARAMETRIZABLES).
			mp.clear();
			//mp = mundo.cargarHistoricosDieta(con, paciente.getCodigoCuenta(), usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt(), fechaFin, 2, paciente.getCodigoCuentaAsocio());			
			mp = mundo.cargarHistoricosDieta(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaFin, 2);			
			forma.setMapaDietaHistorico("nroRegMedElim", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			forma.getMapaDietaHistorico().putAll(mp);
			
			//------Cargar la informacion registrada de liquidos administados ( parametrizables y no parametrizables ) y eliminados (parametrizados).  
			mp.clear();
			//mp = mundo.cargarHistoricosDieta(con, paciente.getCodigoCuenta(), usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt(), fechaFin, 3, paciente.getCodigoCuentaAsocio());
			mp = mundo.cargarHistoricosDieta(con, UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaFin, 3);
			forma.setMapaDietaHistorico("nroRegBalLiqAdm", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			forma.getMapaDietaHistorico().putAll(mp);
			
			//----Carga el numero de registros eliminados en el mapa.
			mp.clear();
			//mp = mundo.cargarHistoricosDieta(con, paciente.getCodigoCuenta(), usuario.getCodigoCentroCosto(), usuario.getCodigoInstitucionInt(), fechaFin, 4, paciente.getCodigoCuentaAsocio());
			mp = mundo.cargarHistoricosDieta(con,UtilidadesManejoPaciente.obtenerCuentasXIngreso(con, paciente.getCodigoIngreso()+""), paciente.getCodigoArea(), usuario.getCodigoInstitucionInt(), fechaFin, 4);
			forma.setMapaDietaHistorico("nroRegBalLiqElim", mp.get("numRegistros")+"");

			forma.setMapaDietaHistorico("fechaHistoricoDieta","1");
			forma.setMapaDietaHistorico("paginadorLiqAdmin","0");
			forma.setMapaDietaHistorico("paginadorLiqElim","0");
	}
	
	
	/**
	 * Metodo para obtener la entidad centro de costo por si id.
	 * @param idCentroCosto
	 * @return
	 */
	private CentrosCosto obtenerCentroCosto(int idCentroCosto)
	{
		ICentroCostosServicio centroCostosServicio 	= AdministracionFabricaServicio.crearCentroCostosServicio();
		return centroCostosServicio.findById(idCentroCosto);
	}	
	
}
