 package com.princetonsa.action.enfermeria;

import java.sql.Connection;
import java.util.ArrayList;

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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.enfermeria.ProgramacionCuidadosEnfermeriaForm;
import com.princetonsa.dto.enfermeria.DtoCuidadosEnfermeria;
import com.princetonsa.dto.enfermeria.DtoDetalleCuidadosEnfermeria;
import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.enfermeria.ProgramacionCuidadoEnfer;
import com.princetonsa.mundo.ordenesmedicas.OrdenMedica;

public class ProgramacionCuidadosEnfermeriaAction extends Action 
{
	Logger logger =Logger.getLogger(ProgramacionCuidadosEnfermeriaAction.class);		
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
		if (form instanceof ProgramacionCuidadosEnfermeriaForm) 
		{
			ProgramacionCuidadosEnfermeriaForm forma=(ProgramacionCuidadosEnfermeriaForm) form;
			
			String estado=forma.getEstado();
			
			logger.info("Estado -->"+estado);
			
			con=UtilidadBD.abrirConexion();
			HttpSession session=request.getSession();
			UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
			PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
			ProgramacionCuidadoEnfer mundo = new ProgramacionCuidadoEnfer();
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de ProgramacionCuidadosEnfermeriaAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("empezar"))
			{
				forma.reset();
				forma.setProgramarPaciente(ConstantesBD.acronimoNo);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("empezarPaciente"))
			{
				forma.reset();
				forma.setProgramarPaciente(ConstantesBD.acronimoSi);
				ActionForward forward = new ActionForward();
				forward=accionValidarProgrmacionPaciente(con, forma, mundo, paciente, usuario, request, mapping);
				if(forward != null)
					return forward;
				
				forma.setCuidados(mundo.consultarFrecuenciaCuidado(
						con,
						paciente.getCodigoIngreso()+"",
						ConstantesBD.codigoNuncaValido,
						true));
				forma.setCuidadosTemporal(mundo.consultarFrecuenciaCuidado(con,	paciente.getCodigoIngreso()+"", ConstantesBD.codigoNuncaValido, true));
				forma.setArrayTipoFrecuencias(mundo.consultarTipoFrecuenciaInst(con, usuario.getCodigoInstitucionInt()));
			
				UtilidadBD.closeConnection(con);
				return mapping.findForward("listadoCuidados");
			}
			else if(estado.equals("consultaListado"))
			{
				forma.setMapaListadoPacientes(mundo.consultarListadoPacientes(con, forma.getAreaFiltro(), forma.getPisoFiltro(), forma.getHabitacionFiltro(), forma.getProgramados()));
				UtilidadBD.closeConnection(con);
				return mapping.findForward("listadoPacientes");
			}
			else if (estado.equals("listadoCuidados"))
			{				
				logger.info("INDICE >>>>> "+forma.getIndicePaciente());
				logger.info("CODIGO INGRESO >>>>> "+forma.getMapaListadoPacientes().get("codigoingreso_"+forma.getIndicePaciente())+"");
				paciente.setCodigoPersona(Utilidades.convertirAEntero(forma.getMapaListadoPacientes().get("codigopaciente_"+forma.getIndicePaciente())+""));
				UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);	
				forma.setCuidados(mundo.consultarFrecuenciaCuidado(
						con,
						paciente.getCodigoIngreso()+"",
						ConstantesBD.codigoNuncaValido,
						true));
				forma.setCuidadosTemporal(mundo.consultarFrecuenciaCuidado(con,	paciente.getCodigoIngreso()+"", ConstantesBD.codigoNuncaValido, true));
				forma.setArrayTipoFrecuencias(mundo.consultarTipoFrecuenciaInst(con, usuario.getCodigoInstitucionInt()));
				UtilidadBD.closeConnection(con);
				return mapping.findForward("listadoCuidados");
			}
			else if(estado.equals("cargarProgramacionCuidadoHistorial"))
			{
				accionCargarProgramacionCuidadoHistorial(con,forma,paciente);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("consultaProCuiEnfExterno");
			}
			else if(estado.equals("programacionCuidadosEnferExterno"))
			{
				accionProgramacionCuidadosEnfer(con,request, forma, paciente);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("progCuiEnfExterno");
			}
			else if(estado.equals("generarProgrCuidadosEnferExterno"))
			{
				accionGenerarProgrCuidadosEnfer(con,request,forma,usuario,paciente);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("progCuiEnfExterno");
			}
			else if(estado.equals("consultaProgramacionCuidados"))
			{
				accionConsultaProgramacionCuidadoHistorial(con,forma,paciente);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("consultaProCuiEnfExterno");
			}
			else if(estado.equals("programacionCuidadosEnfer"))
			{
				accionProgramacionCuidados(con, request, forma, paciente);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("progCuiEnfExterno");
			}
			else if(estado.equals("modificarFrecuenciaCuidados"))
			{
				ActionErrors errores  = ProgramacionCuidadoEnfer.validacionesDatosFrecuenciaPeriodo(forma.getCuidados());
				forma.setPuedeProgramar(ConstantesBD.acronimoNo);
				
				if(errores.isEmpty())
				{				
					insertarModificarCuidadosEnfermeria(con, forma, mundo, usuario, paciente,  request);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoCuidados");
				}
				else
				{
					saveErrors(request, errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoCuidados");
				}
			}
			else if(estado.equals("programarListado"))
			{
				forma.setProgramarMasivo(ConstantesBD.acronimoSi);
				
				forma.getFrecuenciaCuidados().setFechaInicioProgramacion(UtilidadFecha.getFechaActual());
				forma.getFrecuenciaCuidados().setHoraInicioProgramacion(UtilidadFecha.getHoraActual());
				forma.getFrecuenciaCuidados().setObservaciones("");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("progCuiEnfExterno");
			}
			else if(estado.equals("generarProgramarMasivo"))
			{
				accionGenerarProgramarMasivo(con, request, forma, usuario, paciente);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("progCuiEnfExterno");
			}
			else if(estado.equals("marcarSelecionados"))
			{
				ActionErrors errores  = ProgramacionCuidadoEnfer.validacionesDatosFrecuenciaPeriodo(forma.getCuidados());				
				if(errores.isEmpty())
				{
					validacionesProgramados(con, forma, mundo, usuario, request);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoCuidados");
				}
				else
				{
					forma.setPuedeProgramar(ConstantesBD.acronimoNo);
					saveErrors(request, errores);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoCuidados");
				}
			}
			else
			{
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de CONSULTA FACTURAS VARIAS ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
		}
		else
		{
			logger.error("El form no es compatible con el form de ConsultaFacturasVariasForm");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
		}
		
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param request
	 */
	private void validacionesProgramados(Connection con,
			ProgramacionCuidadosEnfermeriaForm forma,
			ProgramacionCuidadoEnfer mundo, UsuarioBasico usuario,
			HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		if (forma.getCuidados() != null)
		{
			for(int i=0; i<forma.getCuidados().size();i++)
			{				
				if(UtilidadTexto.getBoolean(forma.getCuidados().get(i).getSeleccionado()))
				{
					//logger.info("valor de las comparaciones >> "+forma.getCuidadosTemporal().get(i).getFrecuencia()+" "+forma.getCuidados().get(i).getFrecuencia()+" "+forma.getCuidadosTemporal().get(i).getTipoFrecuencia()+" "+forma.getCuidados().get(i).getTipoFrecuencia()+" "+forma.getCuidadosTemporal().get(i).getPeriodo()+" "+forma.getCuidados().get(i).getPeriodo()+" "+forma.getCuidadosTemporal().get(i).getTipoFrecuenciaPeriodo()+" "+forma.getCuidados().get(i).getTipoFrecuenciaPeriodo());
				
					if((forma.getCuidadosTemporal().get(i).getFrecuencia()!=forma.getCuidados().get(i).getFrecuencia())
								|| (forma.getCuidadosTemporal().get(i).getTipoFrecuencia()!=forma.getCuidados().get(i).getTipoFrecuencia())
									|| (forma.getCuidadosTemporal().get(i).getPeriodo()!=forma.getCuidados().get(i).getPeriodo()) 
										|| (forma.getCuidadosTemporal().get(i).getTipoFrecuenciaPeriodo()!=forma.getCuidados().get(i).getTipoFrecuenciaPeriodo()))
					{
						errores.add("descripcion",new ActionMessage("errors.notEspecific","Hay cambios en los datos en los items seleccionados, por favor primero modifique!!! "));
					}
				}	
			}
		}
		if(errores.isEmpty())
			forma.setPuedeProgramar(ConstantesBD.acronimoSi);
		else
			forma.setPuedeProgramar(ConstantesBD.acronimoNo);
		
		saveErrors(request, errores);
	}

	//****************************************************************************************************
	
	
	/**
	 * 
	 */
	private ActionForward accionValidarProgrmacionPaciente(Connection con,
			ProgramacionCuidadosEnfermeriaForm forma, ProgramacionCuidadoEnfer mundo, PersonaBasica paciente,
			UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		
		if(paciente==null || paciente.getCodigoPersona()<=0)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noCargado", "errors.paciente.noCargado", true);
		}
		else if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.ingresoEstadoDiferente", "errors.ingresoEstadoDiferenteAbierto", true);
		}
		return null;
		
	}
	
	//****************************************************************************************************

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param paciente 
	 * @param request
	 */
	private void insertarModificarCuidadosEnfermeria(
			Connection con,
			ProgramacionCuidadosEnfermeriaForm forma,
			ProgramacionCuidadoEnfer mundo,
			UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request)
	{
		
		ActionErrors errores = new ActionErrors();
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		if (forma.getCuidados() != null)
		{
			for(int i=0; i<forma.getCuidados().size();i++)
			{
				
				if((forma.getCuidadosTemporal().get(i).getFrecuencia()!=forma.getCuidados().get(i).getFrecuencia())
							|| (forma.getCuidadosTemporal().get(i).getTipoFrecuencia()!=forma.getCuidados().get(i).getTipoFrecuencia())
								|| (forma.getCuidadosTemporal().get(i).getPeriodo()!=forma.getCuidados().get(i).getPeriodo()) 
									|| forma.getCuidadosTemporal().get(i).getTipoFrecuenciaPeriodo()!=forma.getCuidados().get(i).getTipoFrecuenciaPeriodo())
				{
					
					//Se actualiza el anterior registro
					if(ProgramacionCuidadoEnfer.actualizarEstadoFrecuenciasCuidadosRegEnfer(
							con,
							forma.getCuidados().get(i).getCodigoPk(),
							false,
							usuario.getLoginUsuario()))
					{
						//Se ingresa un nuevo registro por el cambio
						if(ProgramacionCuidadoEnfer.insertarFrecuenciasCuidados(
								con,
								forma.getCuidados().get(i).getCodigoIngreso(),
								forma.getCuidados().get(i).getCodigoCuidadoEnferCcInst(),
								forma.getCuidados().get(i).getCodigoOtroCuidado(),
								forma.getCuidados().get(i).getFrecuencia(),
								forma.getCuidados().get(i).getTipoFrecuencia(),
								true,
								forma.getCuidados().get(i).getPeriodo(),
								forma.getCuidados().get(i).getTipoFrecuenciaPeriodo(),
								usuario.getLoginUsuario()) < 0)
						{
							errores.add("descripcion",new ActionMessage("errors.notEspecific","No se inserto la informacion de la nueva frecuencia"));
							transaccion=false;
						}	
					}
					else
					{	
						errores.add("descripcion",new ActionMessage("errors.notEspecific","No se pudo actualizar la informacion del estado de la frecuencia"));
						transaccion=false;
					}	
				}
				else 
					logger.info("valor de la pos >> "+i+" frec "+forma.getCuidadosTemporal().get(i).getFrecuencia()+" "+forma.getCuidados().get(i).getFrecuencia()+" tipofre "+forma.getCuidadosTemporal().get(i).getTipoFrecuencia()+" "+forma.getCuidados().get(i).getTipoFrecuencia()+" per "+forma.getCuidadosTemporal().get(i).getPeriodo()+"  "+forma.getCuidados().get(i).getPeriodo()+" tipoper "+forma.getCuidadosTemporal().get(i).getTipoFrecuenciaPeriodo()+" "+forma.getCuidados().get(i).getTipoFrecuenciaPeriodo());
				
			}
		}	
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
			forma.setCuidados(mundo.consultarFrecuenciaCuidado(
					con,
					paciente.getCodigoIngreso()+"",
					ConstantesBD.codigoNuncaValido,
					true));
			forma.setCuidadosTemporal(mundo.consultarFrecuenciaCuidado(con,	paciente.getCodigoIngreso()+"", ConstantesBD.codigoNuncaValido, true));
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
			
		saveErrors(request, errores);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param request
	 * @param forma
	 * @param paciente
	 */
	private void accionProgramacionCuidados(Connection con, HttpServletRequest request, ProgramacionCuidadosEnfermeriaForm forma, PersonaBasica paciente) 
	{
		
		logger.info("VALOR INGRESO >>>>>> "+forma.getCuidados().get(forma.getIndiceCuidado()).getCodigoIngreso());
		logger.info("CUIDADO ENFERMERIA >>>>> "+forma.getCuidados().get(forma.getIndiceCuidado()).getCodigoPk());
		
		forma.setProgramacionCuidados(new ArrayList<DtoCuidadosEnfermeria>());
		forma.setFrecuenciaCuidados(new DtoFrecuenciaCuidadoEnferia());
		forma.setCodigoPkFrecCuidadosEnferFiltro(forma.getCuidados().get(forma.getIndiceCuidado()).getCodigoPk()+"");
		forma.setProgramarMasivo(ConstantesBD.acronimoNo);
		
		forma.setFrecuenciaCuidados(
				ProgramacionCuidadoEnfer.consultarFrecuenciaCuidado(
					con,
					forma.getCuidados().get(forma.getIndiceCuidado()).getCodigoIngreso()+"",
					forma.getCuidados().get(forma.getIndiceCuidado()).getCodigoPk(),
					true).get(0));
		
		//realiza las validaciones de la informacion del periodo y frecuencia
		//saveErrors(request,ProgramacionCuidadoEnfer.validacionesInfoFrePerProgramacion(forma.getFrecuenciaCuidados()));
		
		//Actualiza la información de la fecha y hora
		forma.getFrecuenciaCuidados().setFechaInicioProgramacion(UtilidadFecha.getFechaActual());
		forma.getFrecuenciaCuidados().setHoraInicioProgramacion(UtilidadFecha.getHoraActual());
		
	}

	
	/**
	 * Carga la programacion de uncuidado especial desde funcionalidad.
	 * @param con
	 * @param forma
	 * @param paciente
	 */
	private void accionConsultaProgramacionCuidadoHistorial(Connection con, ProgramacionCuidadosEnfermeriaForm forma, PersonaBasica paciente) 
	{
		
		logger.info("VALOR INDICE >>>>> "+forma.getIndiceCuidado());
		logger.info("VALOR INGRESO >>>> "+forma.getCuidados().get(forma.getIndiceCuidado()).getCodigoIngreso());
		logger.info("VALOR CUIDADO ENFERMERIA >>>> "+forma.getCuidados().get(forma.getIndiceCuidado()).getCodigoCuidadoEnfermeria());
		logger.info("VALOR OTRO CUIDADO >>>>> "+forma.getCuidados().get(forma.getIndiceCuidado()).isEsOtroCuidado());
		forma.getCuidados().get(forma.getIndiceCuidado()).setNombrePaciente(paciente.getNombrePersona());
		
		//Carga la informacion de la programacion del cuidado de enfermeria junto al historial
		forma.setProgramacionCuidados(ProgramacionCuidadoEnfer.consultarProgCuidadosEnfer(
				con, 
				forma.getCuidados().get(forma.getIndiceCuidado()).getCodigoIngreso(), 
				ConstantesBD.codigoNuncaValido, 
				forma.getCuidados().get(forma.getIndiceCuidado()).isEsOtroCuidado()?forma.getCuidados().get(forma.getIndiceCuidado()).getCodigoOtroCuidado():forma.getCuidados().get(forma.getIndiceCuidado()).getCodigoCuidadoEnferCcInst(),
				"",
				true, 
				forma.getCuidados().get(forma.getIndiceCuidado()).isEsOtroCuidado(),
				false,
				false));
	}
	

	/**
	 * Accion carga la programacion de un cuidado
	 * @param Connection con
	 * @param ProgramacionCuidadosEnfermeriaForm forma
	 * */
	private void accionCargarProgramacionCuidadoHistorial(Connection con,ProgramacionCuidadosEnfermeriaForm forma,PersonaBasica paciente)
	{
		//Carga la informacion de la programacion del cuidado de enfermeria junto al historial
		forma.setProgramacionCuidados(new ArrayList<DtoCuidadosEnfermeria>());
		forma.setProgramacionCuidados(ProgramacionCuidadoEnfer.consultarProgCuidadosEnfer(
				con,
				paciente.getCodigoIngreso(),
				ConstantesBD.codigoNuncaValido,
				Utilidades.convertirAEntero(forma.getCodigoCuidadoEnferFiltro()),
				"",
				true,
				forma.isEsOtroCuidadoFiltro(),
				false,
				true));
	}
	
	//****************************************************************************************************
	
	/**
	 * Accion carga la programacion de un cuidado
	 * @param Connection con
	 * @param HttpServletRequest request
	 * @param ProgramacionCuidadosEnfermeriaForm forma
	 * @param PersonaBasica paciente
	 * */
	private void accionProgramacionCuidadosEnfer(Connection con,HttpServletRequest request, ProgramacionCuidadosEnfermeriaForm forma,PersonaBasica paciente)
	{
		forma.setProgramacionCuidados(new ArrayList<DtoCuidadosEnfermeria>());
		forma.setFrecuenciaCuidados(new DtoFrecuenciaCuidadoEnferia());
		forma.setProgramarMasivo(ConstantesBD.acronimoNo);
		forma.setPuedeProgramar(ConstantesBD.acronimoNo);
		
		//Si pasa la validacion indica que se generara una programacion sin frecuencia, ni periodo
		if(Utilidades.convertirAEntero(forma.getCodigoPkFrecCuidadosEnferFiltro()) >= 0)
		{
			//carga la información de la parametrización de la frecuencia.
			forma.setFrecuenciaCuidados(
					ProgramacionCuidadoEnfer.consultarFrecuenciaCuidado(
						con,
						paciente.getCodigoIngreso()+"",
						Utilidades.convertirAEntero(forma.getCodigoPkFrecCuidadosEnferFiltro()),
						true).get(0));
				
		//realiza las validaciones de la informacion del periodo y frecuencia
		saveErrors(request,ProgramacionCuidadoEnfer.validacionesInfoFrePerProgramacion(forma.getFrecuenciaCuidados()));
		
		}
		
		//Actualiza la información de la fecha y hora
		forma.getFrecuenciaCuidados().setFechaInicioProgramacion(UtilidadFecha.getFechaActual());
		forma.getFrecuenciaCuidados().setHoraInicioProgramacion(UtilidadFecha.getHoraActual());
	}
	
	//****************************************************************************************************
	
	/**
	 * 
	 */
	private void accionGenerarProgramarMasivo(
			Connection con,
			HttpServletRequest request,
			ProgramacionCuidadosEnfermeriaForm forma,
			UsuarioBasico usuario,
			PersonaBasica paciente)
	{
		ActionErrors errores = new ActionErrors();
		
		boolean transaccion = UtilidadBD.iniciarTransaccion(con);
		
		
		for(int i=0; i<forma.getCuidados().size(); i++)
		{	
		
			logger.info("VALOR EN SELECCIONADO >>>> "+forma.getCuidados().get(i).getSeleccionado());
			
			if(UtilidadTexto.getBoolean(forma.getCuidados().get(i).getSeleccionado()))
			{	
				int consecutivo = ProgramacionCuidadoEnfer.insertarProgCuidadosEnfer(
															con, 
															forma.getCuidados().get(i).getCodigoPk(),
															forma.getFrecuenciaCuidados().getFechaInicioProgramacion(),
															forma.getFrecuenciaCuidados().getHoraInicioProgramacion(),
															forma.getFrecuenciaCuidados().getObservaciones(),
															usuario.getLoginUsuario());
				
				if(consecutivo >= 0)
				{
					//Captura el detalle de la programacion
					ArrayList<DtoDetalleCuidadosEnfermeria> array =  ProgramacionCuidadoEnfer.programarAdministracion(
																		forma.getFrecuenciaCuidados().getFechaInicioProgramacion(),
																		forma.getFrecuenciaCuidados().getHoraInicioProgramacion(),
																		forma.getCuidados().get(i).getFrecuencia(),
																		forma.getCuidados().get(i).getTipoFrecuencia(),
																		forma.getCuidados().get(i).getPeriodo(),
																		forma.getCuidados().get(i).getTipoFrecuenciaPeriodo(),
																		consecutivo);
					
					//Inserta todos los detalles de la programacion
					if(array.size() > 0 
							&& ProgramacionCuidadoEnfer.insertarDetalleProgCuidadosEnfer(con, array))
					{
						logger.info("Inserto Correctamente Detalle Programacion");
					}
					else
					{
						transaccion=false;						
						if(array.size() == 0)
							errores.add("descripcion",new ActionMessage("errors.notEspecific","No se genero Fechas/Horas de Programación, favor revisar rangos de fechas, frecuencias y periodos. "));
						else
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Generar el detalle de la programación."));							
					}
				}
				else
				{
					transaccion=false;
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Generar el encabezado de la programación."));
				}
			}	
		}
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			forma.setEstado("operacionExistosa");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		saveErrors(request, errores);	
	}
	
	
	/**
	 * Genera la programacion de cuidados de enfermeria
	 * @param Connection con
	 * @param HttpServletRequest request
	 * @param ProgramacionCuidadosEnfermeriaForm forma
	 * @param PersonaBasica paciente
	 * */
	private void accionGenerarProgrCuidadosEnfer(
			Connection con,
			HttpServletRequest request,
			ProgramacionCuidadosEnfermeriaForm forma,
			UsuarioBasico usuario,
			PersonaBasica paciente)
	{
		OrdenMedica orden = new OrdenMedica();
		ActionErrors errores = new ActionErrors();
		
		if(orden.cargarOrdenMedica(con,paciente.getCodigoCuenta(), false))
		{
			//validación de fecha/Hora inicio programación
			errores = ProgramacionCuidadoEnfer.validacionesFechaHoraProgr(
					con,
					forma.getFrecuenciaCuidados().getFechaInicioProgramacion(),
					forma.getFrecuenciaCuidados().getHoraInicioProgramacion(),
					paciente.getFechaIngreso(),
					paciente.getHoraIngreso(),
					orden.getFechaOrden(),
					orden.getHoraOrden());
			
			if(errores.isEmpty())
			{
				UtilidadBD.iniciarTransaccion(con);
				
				int consecutivo = ProgramacionCuidadoEnfer.insertarProgCuidadosEnfer(
															con, 
															Utilidades.convertirAEntero(forma.getCodigoPkFrecCuidadosEnferFiltro()),
															forma.getFrecuenciaCuidados().getFechaInicioProgramacion(),
															forma.getFrecuenciaCuidados().getHoraInicioProgramacion(),
															forma.getFrecuenciaCuidados().getObservaciones(),
															usuario.getLoginUsuario());
				
				if(consecutivo >= 0)
				{
					//Captura el detalle de la programacion
					ArrayList<DtoDetalleCuidadosEnfermeria> array =  ProgramacionCuidadoEnfer.programarAdministracion(
																		forma.getFrecuenciaCuidados().getFechaInicioProgramacion(),
																		forma.getFrecuenciaCuidados().getHoraInicioProgramacion(),
																		forma.getFrecuenciaCuidados().getFrecuencia(),
																		forma.getFrecuenciaCuidados().getTipoFrecuencia(),
																		forma.getFrecuenciaCuidados().getPeriodo(),
																		forma.getFrecuenciaCuidados().getTipoFrecuenciaPeriodo(),
																		consecutivo);
					
					//Inserta todos los detalles de la programacion
					if(array.size() > 0 
							&& ProgramacionCuidadoEnfer.insertarDetalleProgCuidadosEnfer(con, array))
					{
						UtilidadBD.finalizarTransaccion(con);
						forma.setEstado("operacionExistosa");
						
						//Inicia las observaciones
						forma.getFrecuenciaCuidados().setObservaciones("");
						
						//consulta la informacion para el resumen
						forma.setProgramacionCuidados(ProgramacionCuidadoEnfer.consultarProgCuidadosEnfer(
								con,
								paciente.getCodigoIngreso(),
								consecutivo, //Con este valor basta
								ConstantesBD.codigoNuncaValido,
								"",
								true,
								false,
								false,
								false));
					}
					else
					{
						UtilidadBD.abortarTransaccion(con);						
						if(array.size() == 0)
							errores.add("descripcion",new ActionMessage("errors.notEspecific","No se genero Fechas/Horas de Programación, favor revisar rangos de fechas, frecuencias y periodos. "));
						else
							errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Generar el detalle de la programación."));							
					}
				}
				else
				{
					UtilidadBD.abortarTransaccion(con);		
					errores.add("descripcion",new ActionMessage("errors.notEspecific","Error al Generar el encabezado de la programación."));
				}
			}
		}
		else
			errores.add("descripcion",new ActionMessage("errors.notEspecific","No se ha generado orden médica. Por favor verifique"));
		
		saveErrors(request, errores);	
	}
	
	//****************************************************************************************************
}	 