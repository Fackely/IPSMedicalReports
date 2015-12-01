/*
 * Julio 13, 2010
 */
package com.princetonsa.action.tesoreria;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesIntegridadDominio;

import com.princetonsa.actionform.tesoreria.TrasladoAbonoPacienteForm;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.comun.DtoResultado;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladoAbonoPAciente;
import com.princetonsa.dto.tesoreria.DtoGuardarTrasladoAbonoPaciente;
import com.princetonsa.dto.tesoreria.DtoInfoIngresoTrasladoAbonoPaciente;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IIngresosEgresosCajaServicioServicio;


/**
 * @author Cristhian Murillo
 *
 * Clase usada para controlar los procesos de la funcionalidad.
 * 
 */
public class TrasladoAbonoPacienteAction extends Action 
{

	IIngresosEgresosCajaServicioServicio ingresosEgresosCajaServicioServicio 	
			= AdministracionFabricaServicio.crearIIngresosEgresosCajaServicioServicio();
	
	
	/**
	 * M&eacute;todo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception
	{
		if(form instanceof TrasladoAbonoPacienteForm){
			
			TrasladoAbonoPacienteForm forma = (TrasladoAbonoPacienteForm)form;
			String estado = forma.getEstado(); 
			
			
			Log4JManager.info("Estado TrasladoAbonoPacienteAction --> "+estado);
			
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			if(estado == null)
			{
				request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			
			else if( (estado.equals("empezar")) || (estado.equals("resumen")) )
			{
				return accionEmpezar(mapping, forma, usuario, request);
			}
			
			else if(estado.equals("buscarpaciente"))
			{
				return accionBuscarPaciente(mapping, forma, request);
			}
			
			else if(estado.equals("buscarpacientedestino"))
			{
				return accionBuscarPacienteDestino(mapping, forma, request);
			}
			
			else if(estado.equals("eliminarpacdestino"))
			{
				return accionEliminarPacDestino (mapping, forma, request);
			}
			
			else if(estado.equals("calcularTotalOrigen"))
			{
				return accionCalcularTotalOrigen(mapping, forma, request);
			}
			
			else if(estado.equals("realizartraslado"))
			{
				return accionRealizartraslado(mapping, forma, request, usuario);
			}
			
			else if(estado.equals("imprimir"))
			{
				return mapping.findForward("detalleConsultaTraslado");
			}
			
		}
		return null;
	}
	
	



	/*------------------------------*/
	/* ACTION						*/
	/*------------------------------*/
		
	/**
	 * Accion Empezar
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, TrasladoAbonoPacienteForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		forma.reset();
		
		UtilidadTransaccion.getTransaccion().begin();
		
		String[] listaTipoDeTiposIdentificacion = { 
				ConstantesIntegridadDominio.acronimoTipoTipoIdentificacionPersona,
				ConstantesIntegridadDominio.acronimoTipoTipoIdentificacionAmbos
		};
		
		forma.setListaTiposIdentificacion(ingresosEgresosCajaServicioServicio.listarTiposIdentificacionPorTipo(listaTipoDeTiposIdentificacion));
		
		UtilidadTransaccion.getTransaccion().commit();
		
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionBuscarPaciente(ActionMapping mapping, TrasladoAbonoPacienteForm forma, HttpServletRequest request)
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		forma.resetMostrarAbonos();
		forma.setPacienteOrigen(ingresosEgresosCajaServicioServicio.obtenerPaciente(
				forma.getIdentificacionBuscar(), forma.getAcronimoTipoIdentificacion()));
		
		if(forma.getPacienteOrigen() == null)
		{
			forma.setMostrarInformacionOrigen(false);
			mostrarPacienteNoEncontrado(request, forma, "origen");
		}
		else
		{
			forma.setMostrarBuscarPacienteDestino(true);
			forma.setValidacionesOrigen(ingresosEgresosCajaServicioServicio.validarPacienteParaTrasladoAbono(forma.getPacienteOrigen()));
			organizarPresentacionOrigen(forma, request);
			forma.setMostrarInformacionOrigen(true);
		}
		
		UtilidadTransaccion.getTransaccion().commit();
		
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private boolean validarTotalesTrasladoAbono(ActionMapping mapping, TrasladoAbonoPacienteForm forma, HttpServletRequest request)
	{
		boolean pasoValidaciones = true;
		if(forma.getValidacionesOrigen().isParametroControlaAbonoPorIngreso())
		{	// por ingreso
			if(forma.getTotalTraslado() > 0)
			{
			
				double sumatoria = 0;
				for (DtoInfoIngresoTrasladoAbonoPaciente ingreso : forma.getListaDtoInfoIngresoTrasladoAbonoPaciente()) 
				{
					 if(ingreso.getAbonoTrasladar() > ingreso.getAbonoDisponible()){
						 pasoValidaciones = false;
						 mostrarErrorValorTrasladoIngreso(request, forma); 
					 }
					 sumatoria += ingreso.getAbonoTrasladar();
					
				}
				
				if(forma.getTotalTraslado() > sumatoria)
				{
					pasoValidaciones = false;
					mostrarErrorTrasladoInvalido(request, forma, "El valor a trasladar no puede ser superior al disponible");
				}
				else
				{
					pasoValidaciones = true;
				}
			}
			else
			{
				pasoValidaciones = false;
				mostrarErrorTrasladoInvalido(request, forma, "El valor a trasladar debe ser superior a cero");
			}
		}
		else
		{	// totalizado
			double totalTrasladar 	= 0;
			double totalDisponible 	= 0;
			totalTrasladar			= forma.getTotalTraslado();
			totalDisponible			= forma.getListaDtoInfoIngresoTrasladoAbonoPaciente().get(0).getAbonoDisponible();
			
			if(totalTrasladar > 0)
			{
				if(totalTrasladar > totalDisponible)
				{
					pasoValidaciones = false;
					mostrarErrorTrasladoInvalido(request, forma, "El valor a trasladar no puede ser superior al disponible");
				}
				else 
				{
					pasoValidaciones = true;
					forma.setTotalAbonoTrasladar(totalTrasladar);
				}
			}
			else
			{
				pasoValidaciones = false;
				mostrarErrorTrasladoInvalido(request, forma, "El valor a trasladar debe ser superior a cero");
			}
		}
		
		return pasoValidaciones;
	}

	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private ActionForward accionBuscarPacienteDestino(ActionMapping mapping, TrasladoAbonoPacienteForm forma, HttpServletRequest request)
	{
		if(validarTotalesTrasladoAbono(mapping, forma, request))
		{
			forma.setMostrarBotonTrasladar(true);
			UtilidadTransaccion.getTransaccion().begin();
			
			DtoPersonas pacienteDestino = ingresosEgresosCajaServicioServicio.obtenerPaciente(
					forma.getIdentificacionBuscarDestino(), forma.getAcronimoTipoIdentificacionDestino());
			
			if(pacienteDestino == null)
			{
				mostrarPacienteNoEncontrado(request, forma, "destino");
			}
			else
			{	
				boolean continuar = true;
				for (DtoInfoIngresoTrasladoAbonoPaciente pacienteYaCargado : forma.getListaDtoInfoIngresoTrasladoAbonoPacienteDestino()) 
				{
					if( (pacienteYaCargado.getNumeroId().equalsIgnoreCase(pacienteDestino.getNumeroId())) 
							&& (pacienteYaCargado.getTipoId().equalsIgnoreCase(pacienteDestino.getTipoId())) )
					{
						continuar = false;
						mostrarErrorCargarMismoPaciente(request, forma);
					}
				}
				
				
				if( (forma.getPacienteOrigen().getNumeroId().equalsIgnoreCase(pacienteDestino.getNumeroId())) 
						&& (forma.getPacienteOrigen().getTipoId().equalsIgnoreCase(pacienteDestino.getTipoId())) )
				{
					continuar = false;
					mostrarErrorMismoPacienteOrigen(request, forma);
				}
				
				
				if(continuar == true)
				{
					forma.setValidacionesDestino(ingresosEgresosCajaServicioServicio.validarPacienteParaTrasladoAbonoDestino(pacienteDestino));
					organizarPresentacionDestino(forma, request, pacienteDestino);
				}
			}
			
			UtilidadTransaccion.getTransaccion().commit();
		}
		else
		{
			forma.setMostrarBotonTrasladar(false);
		}
		
		
		
		if(forma.getListaDtoInfoIngresoTrasladoAbonoPacienteDestino().size() == 0)
		{
			forma.setMostrarBotonTrasladar(false);
		}
		else
		{
			forma.setMostrarBotonTrasladar(true);
		}
		
		return mapping.findForward("recargarZona");
	}
	
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEliminarPacDestino(ActionMapping mapping, TrasladoAbonoPacienteForm forma, HttpServletRequest request)
	{
		forma.getListaDtoInfoIngresoTrasladoAbonoPacienteDestino().remove(forma.getPosListaPacDestino());
		return mapping.findForward("recargarZona");
	}
	
	
	
	
	/**
	 * Calcula el total del valor a trasladar
	 * @param mapping
	 * @param forma
	 * @param request
	 * @return
	 */
	private ActionForward accionCalcularTotalOrigen(ActionMapping mapping, TrasladoAbonoPacienteForm forma, HttpServletRequest request) 
	{
		double total = 0;
		for (DtoInfoIngresoTrasladoAbonoPaciente obj : forma.getListaDtoInfoIngresoTrasladoAbonoPaciente()) 
		{
			total += obj.getAbonoTrasladar();
		}
		forma.setTotalTraslado(total);
		
		return mapping.findForward("totalATrasladar");
	}
	
	
	
	
	
	/**
	 * Guarda el trasladode abono
	 * @param mapping
	 * @param forma
	 * @param request
	 * @return
	 */
	private ActionForward accionRealizartraslado(ActionMapping mapping,	TrasladoAbonoPacienteForm forma, HttpServletRequest request, UsuarioBasico usuario) 
	{
		// FIXME llamar al proceso de validacion de contarseña
		
		double sumatoriaDestino = 0;
		for (DtoInfoIngresoTrasladoAbonoPaciente pacDestino : forma.getListaDtoInfoIngresoTrasladoAbonoPacienteDestino()) 
		{
			sumatoriaDestino += pacDestino.getAbonoTrasladar();
		}
		
		if (sumatoriaDestino != forma.getTotalTraslado()) 
		{
			mostrarErrorTotalTrasladoDestino(request, forma);
		}
		else{
		
			UtilidadTransaccion.getTransaccion().begin();
			
			DtoGuardarTrasladoAbonoPaciente dtoGuardarTrasladoAbonoPaciente = new DtoGuardarTrasladoAbonoPaciente();
			dtoGuardarTrasladoAbonoPaciente.setOrigen(forma.getPacienteOrigen());
			dtoGuardarTrasladoAbonoPaciente.setTotalMovimiento(forma.getTotalAbonoTrasladar());
			dtoGuardarTrasladoAbonoPaciente.setParametroControlAbonoPorIngreso(forma.getValidacionesOrigen().isParametroControlaAbonoPorIngreso());
			dtoGuardarTrasladoAbonoPaciente.setListaIngresosPacOrigen(forma.getListaDtoInfoIngresoTrasladoAbonoPaciente());
			dtoGuardarTrasladoAbonoPaciente.setListaIngresosPacDestino(forma.getListaDtoInfoIngresoTrasladoAbonoPacienteDestino());
			
			dtoGuardarTrasladoAbonoPaciente.setLoginUsuarioActual(usuario.getLoginUsuario());
			dtoGuardarTrasladoAbonoPaciente.setCentroAtencionActual(usuario.getCodigoCentroAtencion());
			dtoGuardarTrasladoAbonoPaciente.setInstitucion(usuario.getCodigoInstitucionInt());
			
			DtoResultado resultado = ingresosEgresosCajaServicioServicio.guardarTrasladoAbono(dtoGuardarTrasladoAbonoPaciente);
			if(!resultado.isExitoso())
			{
				UtilidadTransaccion.getTransaccion().rollback();
				mostrarErrorNoTraslado(request, forma);
			}
			else
			{
				forma.setEstado("resumen");
				Log4JManager.info(">>>>>>>>>>>>>>> codigo pk asignado al traslado: "+resultado.getPk());
				UtilidadTransaccion.getTransaccion().commit();
				
				DtoConsultaTrasladoAbonoPAciente dtoConsulta = new DtoConsultaTrasladoAbonoPAciente();
				dtoConsulta.setIdTrasladoAbonos(Long.parseLong(resultado.getPk().trim()));
				
				
				// Consulta de resumen
				UtilidadTransaccion.getTransaccion().begin();
				forma.setListaDtoConsultaTrasladoAbonoPAciente((ArrayList<DtoConsultaTrasladoAbonoPAciente>)ingresosEgresosCajaServicioServicio.obtenerDetallesTrasladoAbonos(dtoConsulta));
				UtilidadTransaccion.getTransaccion().commit();
			}
		}
		
		return mapping.findForward("principal");
	}

	
	
	
	
	
	
	
	/*------------------------------*/
	/* METODOS VARIOS 				*/
	/*------------------------------*/
	
	/**
	 * Define la informacion enviada a la presentacion de la funcionalidad
	 * @param forma
	 * @param request
	 */
	private void organizarPresentacionOrigen (TrasladoAbonoPacienteForm forma, HttpServletRequest request)
	{
		if(!forma.getValidacionesOrigen().isPacienteTieneSaldo()){
			forma.setMostrarBuscarPacienteDestino(false);
			mostrarErrorPacienteSinSaldo(request, forma);
		}
		else
		{
			if(!forma.getValidacionesOrigen().isPacienteTieneRegistrosDeIngreso())
			{
				forma.setMostrarBuscarPacienteDestino(false);
				mostrarErrorPacienteSinIngresos(request, forma, "El/La paciente"); 
			}
			
			if(forma.getValidacionesOrigen().isParametroManejoEspecialInstiOdonto())
			{
				if(forma.getValidacionesOrigen().isParametroTrasAbonoPacSaldoPresuContratado())
				{
					if(!forma.getValidacionesOrigen().isPacienteTienePresupuestoEstadoCorrecto())
					{
						forma.setMostrarBuscarPacienteDestino(false);
						mostrarErrorEstadoPresupuestoInvalido(request, forma, "origen", "contratado-contratado");
						return;
					}
				}
			}
			
			
			boolean listarPorIngreso = false;
			
			if(forma.getValidacionesOrigen().isParametroControlaAbonoPorIngreso() == true)
			{	// POR INGRESO
				Log4JManager.info("Abono por Ingreso Detallado");
				forma.setMostrarAbonosPorIngreso(true);
				listarPorIngreso = true;
				
				forma.getListaDtoInfoIngresoTrasladoAbonoPaciente()
				.addAll(ingresosEgresosCajaServicioServicio.obtenerIngresosParaTrasladoPorPaciente(forma.getPacienteOrigen().getCodigo(), listarPorIngreso));
			}
			else
			{ 	// TOTALIZADO
				Log4JManager.info("Abono por Ingreso totalizado");
				forma.setMostrarAbonosTotalizados(true);
				listarPorIngreso = false;
				
				//jum?
				forma.getListaDtoInfoIngresoTrasladoAbonoPaciente()
				.addAll(ingresosEgresosCajaServicioServicio.obtenerIngresosParaTrasladoPorPaciente(forma.getPacienteOrigen().getCodigo(), listarPorIngreso));
				
				DtoInfoIngresoTrasladoAbonoPaciente dtoInfoIngresoTrasladoAbonoPaciente =  
					ingresosEgresosCajaServicioServicio.obtenerUltimoIngresoAbiertoPaciente(
						forma.getPacienteOrigen().getCodigo(), forma.getValidacionesOrigen().isParametroManejoEspecialInstiOdonto());
				
				if(dtoInfoIngresoTrasladoAbonoPaciente != null)
				{
					for (DtoInfoIngresoTrasladoAbonoPaciente ingresoOrigen : forma.getListaDtoInfoIngresoTrasladoAbonoPaciente()) 
					{
						ingresoOrigen.setCentroAtencion(dtoInfoIngresoTrasladoAbonoPaciente.getCentroAtencion());
						ingresoOrigen.setNombreCentroAtencion(dtoInfoIngresoTrasladoAbonoPaciente.getNombreCentroAtencion());
					}
				}
			}
			
			
		}
	}
	

	
	/**
	 * Define la informacion enviada a la presentacion de la funcionalidad
	 * @param forma
	 * @param request
	 */
	private void organizarPresentacionDestino (TrasladoAbonoPacienteForm forma, HttpServletRequest request, DtoPersonas pacienteDestino)
	{
		
		if(forma.getValidacionesDestino().isPacienteTieneTodosIngresosCerrados())
		{
			mostrarErrorPacienteSinIngresos(request, forma, "El/La paciente en estado Abierto");
		}
		else
		{
			if(forma.getValidacionesDestino().isParametroManejoEspecialInstiOdonto())
			{
				if(forma.getValidacionesDestino().isParametroTrasAbonoPacSaldoPresuContratado())
				{
					if(!forma.getValidacionesDestino().isPacienteTienePresupuestoEstadoCorrecto())
					{
						mostrarErrorEstadoPresupuestoInvalido(request, forma, "destino ", "contratado-contratado");
					}
					else
					{
						// -->formato
						adicionarPacienteDestino(pacienteDestino, forma, request);
						return;
					}
				}
				else
				{
					// -->formato
					adicionarPacienteDestino(pacienteDestino, forma, request);
					return;
				}
			}
			else
			{
				// -->formato
				adicionarPacienteDestino(pacienteDestino, forma, request);
				return;
			}
		}
	}
	
	
	
	/**
	 * Agrega el paciente que ha pasado las validaciones a la lista de pacientes
	 * destino que recibiran el traslado de abonos
	 * 
	 * @param pacienteDestino
	 * @param request
	 */
	@SuppressWarnings("deprecation")
	private void adicionarPacienteDestino(DtoPersonas pacienteDestino, TrasladoAbonoPacienteForm forma,  HttpServletRequest request)
	{
		DtoInfoIngresoTrasladoAbonoPaciente dtoInfoIngresoTrasladoAbonoPaciente =  
			ingresosEgresosCajaServicioServicio.obtenerUltimoIngresoAbiertoPaciente(
				pacienteDestino.getCodigo(), forma.getValidacionesDestino().isParametroManejoEspecialInstiOdonto());
		
		if(dtoInfoIngresoTrasladoAbonoPaciente != null)
		{
			dtoInfoIngresoTrasladoAbonoPaciente.setTipoId(pacienteDestino.getTipoId());
			dtoInfoIngresoTrasladoAbonoPaciente.setNumeroId(pacienteDestino.getNumeroId());
			dtoInfoIngresoTrasladoAbonoPaciente.setPrimerNombre(pacienteDestino.getPrimerNombre());
			dtoInfoIngresoTrasladoAbonoPaciente.setSegundoNombre(pacienteDestino.getSegundoNombre());
			dtoInfoIngresoTrasladoAbonoPaciente.setPrimerApellido(pacienteDestino.getPrimerApellido());
			dtoInfoIngresoTrasladoAbonoPaciente.setSegundoApellido(pacienteDestino.getSegundoApellido());
			
			forma.getListaDtoInfoIngresoTrasladoAbonoPacienteDestino().add(dtoInfoIngresoTrasladoAbonoPaciente);
		}
		else
		{
			mostrarErrorNoCargoPaciente(request, forma);
		}
	}
	
	
	
	
	
	
	/*--------------------------------------*/
	/* MENSAJES DE ERROR					*/
	/*--------------------------------------*/
	
	/**
	 * Indica que no se encontraron registros para los parametros de busqueda
	 * @param request
	 * @param forma
	 * @param mensaje
	 */
	private void mostrarPacienteNoEncontrado(HttpServletRequest request, TrasladoAbonoPacienteForm forma, String mensaje) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error no_existe_paciente", new ActionMessage("errores.modTesoreria.pacienteNoEncontrado", mensaje));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	/**
	 * Indica que el paciente no tiene saldo
	 * @param request
	 * @param forma
	 */
	private void mostrarErrorPacienteSinSaldo(HttpServletRequest request, TrasladoAbonoPacienteForm forma) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error no_tiene_saldo", new ActionMessage("errores.modTesoreria.pacienteNoTieneSaldo","paciente","traslado de abonos"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	/**
	 * Indica que el paciente no tiene registros de ingreso
	 * @param request
	 * @param forma
	 * @param mensaje
	 */
	private void mostrarErrorPacienteSinIngresos(HttpServletRequest request, TrasladoAbonoPacienteForm forma, String mensaje) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error no_tiene_ingresos", new ActionMessage("errores.modTesoreria.pacienteNoTieneIngre", mensaje));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	/**
	 * Indica que el valor ingresado para hacer el traslado no es valido
	 * @param request
	 * @param forma
	 * @param mensaje
	 */
	private void mostrarErrorTrasladoInvalido(HttpServletRequest request, TrasladoAbonoPacienteForm forma, String mensaje) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error no_traslado_invalido", new ActionMessage("errores.modTesoreria.trasladoAbonoInvalid", mensaje));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	/**
	 * Indica que no tiene el estado del presupuesto indicado
	 * @param request
	 * @param forma
	 * @param mensaje_0
	 * @param mensaje_1
	 */
	private void mostrarErrorEstadoPresupuestoInvalido(HttpServletRequest request, TrasladoAbonoPacienteForm forma, String mensaje_0, String mensaje_1) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error no_estado_presupuesto", new ActionMessage("errores.modTesoreria.pacienteNoEstadoPresupuesto", mensaje_0, mensaje_1));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	/**
	 * Indica que no se pudo cargar el paciente
	 * 
	 * @param request
	 * @param forma
	 */
	private void mostrarErrorNoCargoPaciente(HttpServletRequest request, TrasladoAbonoPacienteForm forma) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error no_cargo_paciente", new ActionMessage("errores.modTesoreria.NoCargoPaciente", "Puede qué no tenga ingresos para realizar el traslado"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	
	/**
	 * Indica que se intenta cargar un paciente que ya esta en la lista de destino
	 * 
	 * @param request
	 * @param forma
	 */
	private void mostrarErrorCargarMismoPaciente(HttpServletRequest request, TrasladoAbonoPacienteForm forma) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error cargor_mismo_paciente", new ActionMessage("errores.modTesoreria.mismoPaciente"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	
	
	/**
	 * Indica que no se pudo realizar el traslado de abonos del paciente
	 * 
	 * @param request
	 * @param forma
	 */
	private void mostrarErrorNoTraslado(HttpServletRequest request, TrasladoAbonoPacienteForm forma) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error no_realizo_traslado_abono", new ActionMessage("errores.modTesoreria.noRealizoTraslado", " de abonos"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	
	
	/**
	 * Indica el total a trasladar a pacientes destino no es igual al seleccionado para trasladar
	 * 
	 * @param request
	 * @param forma
	 */
	private void mostrarErrorTotalTrasladoDestino(HttpServletRequest request, TrasladoAbonoPacienteForm forma) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error total_traslado_diferente", new ActionMessage("errores.modTesoreria.noTotalTraslado"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	
	
	/**
	 * Indica que se intenta cargar como destino el mismo paciente origen
	 * 
	 * @param request
	 * @param forma
	 */
	private void mostrarErrorMismoPacienteOrigen(HttpServletRequest request, TrasladoAbonoPacienteForm forma) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error cargar_mismo_paciente_origen", new ActionMessage("errores.modTesoreria.pacDestinoIgualOrigen"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	

	
	/**
	 * Indica que el valor seleccionado a trasladar no aplica para el disponible por ingreso
	 * 
	 * @param request
	 * @param forma
	 */
	private void mostrarErrorValorTrasladoIngreso(HttpServletRequest request, TrasladoAbonoPacienteForm forma) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error valor_traslado_diferente", new ActionMessage("errores.modTesoreria.valorTrasladoInvalidoIngreso"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
}
