package com.princetonsa.action.manejoPaciente;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.capitacion.ConstantesCapitacion;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.AutorizacionesIngresoEstanciaForm;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.historiaClinica.DtoValoracionUrgencias;
import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionAdmision;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDiagnosticoBusqueda;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteIngresoEstancia;
import com.princetonsa.dto.manejoPaciente.DtoIngresoEstancia;
import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.servinte.axioma.orm.CentroAtencion;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionIngresoEstancia.GeneradorReporteFormatoCapitacionAutorIngresoEstancia;
import com.servinte.axioma.mundo.impl.manejoPaciente.exepcion.ObtenerEstanciaViaIngresoCentroCostoException;
import com.servinte.axioma.orm.AutorizacionesIngreEstancia;
import com.servinte.axioma.orm.Barrios;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.Diagnosticos;
import com.servinte.axioma.orm.DiagnosticosId;
import com.servinte.axioma.orm.EntidadesSubcontratadas;
import com.servinte.axioma.orm.EstratosSociales;
import com.servinte.axioma.orm.HistoAutorizacionIngEstan;
import com.servinte.axioma.orm.IngresosEstancia;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.orm.Personas;
import com.servinte.axioma.orm.Sexo;
import com.servinte.axioma.orm.TiposAfiliado;
import com.servinte.axioma.orm.TiposCargue;
import com.servinte.axioma.orm.TiposIdentificacion;
import com.servinte.axioma.orm.TiposPersonas;
import com.servinte.axioma.orm.UsuarioXConvenio;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.ViasIngreso;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.capitacion.CapitacionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.ICentroCostosServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IPersonasServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IUsuarioXConvenioServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IUsuariosCapitadosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntidadesSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntregaMedicamentosInsumosEntSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.convenio.IConvenioServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionIngresoEstanciaServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IIngresosEstanciaServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IPacienteServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ITiposAfiliadoServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IViasIngresoServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITiposIdentificacionServicio;


/**
 * @author Cristhian Murillo
 * Clase usada para controlar los procesos de la funcionalidad.
 */
public class AutorizacionesIngresoEstanciaAction extends Action 
{
	/** * Log */
	Logger logger = Logger.getLogger(AutorizacionesIngresoEstanciaAction.class);
	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.manejoPaciente.AutorizacionesIngresoEstanciaForm");
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response )throws Exception
	{
		
		if(form instanceof AutorizacionesIngresoEstanciaForm)
		{
			AutorizacionesIngresoEstanciaForm forma = (AutorizacionesIngresoEstanciaForm)form;
			String estado = forma.getEstado(); 
			
			Log4JManager.info("Estado: AutorizacionesIngresoEstanciaAction --> "+estado);
			
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			if(estado.equals("empezar"))
			{
				
				forma.reset();
				ActionForward actionForward = new ActionForward();
				actionForward = validarEmpezar(mapping, request, usuario);
				
				if(actionForward != null){
					return actionForward;
				}
				
				forma.resetParametrosBusqueda();
				llenarForma(forma, usuario);
				
				return mapping.findForward("principal");
			}
			
			else if(estado.equals("buscar"))
			{
				accionRealizarBusqueda(mapping, forma, request, usuario);
				return mapping.findForward("principal");
			}
			
			else if(estado.equals("ordenar"))
			{
				accionOrdenar(mapping, forma); 
				return mapping.findForward("principal");
				
			}
			
			else if(estado.equals("detalle"))
			{
				validacionesDetalle(mapping, forma, request, usuario);
				
				// Si no puede extender la autorización se carga directamente la pagina para ingresar una nueva
				if(!forma.isExtenderAutorizacion()){
					return mapping.findForward("detalleAutorizacion");
				}
				else{
					return mapping.findForward("principal");
				}
			}
			
			else if(estado.equals("extenderAutorizacion"))
			{
				llenarDatosParaPosponer(forma, usuario);
				return mapping.findForward("detalleAutorizacion");
			}
			
			else if(estado.equals("nuevaAutorizacion"))
			{
				// Se limpia el registro de la última autorización para crear una nueva
				forma.getUsuarioCapitadoSeleccionado().setAutorizacionIngresoEstancia(new DTOAdministracionAutorizacion());
				forma.getUsuarioCapitadoSeleccionado().setDatosAdmision(new DtoIngresoEstancia());
				forma.getUsuarioCapitadoSeleccionado().setDescripcionEntidadSubOtra("");
				forma.getUsuarioCapitadoSeleccionado().setTelefonoEntidadSubOtra("");
				forma.getUsuarioCapitadoSeleccionado().setDireccionEntidadSubOtra("");
				return mapping.findForward("detalleAutorizacion");
			}
			
			else if( estado.equals("cambioEntidadSubcontratada"))
			{
				cambioEntidadSubcontratada(forma, request, usuario);
				return mapping.findForward("seccionAutorizacion");
			}
			
			else if( estado.equals("cambioDiasEstanciaAutorizados"))
			{
				cambioDiasEstanciaAutorizados(forma, request, usuario);
				return mapping.findForward("seccionAutorizacion");
			}
			
			else if(estado.equals("asignarPropiedad")){
				/*  * Se asigna una propiedad a la forma sin 
				 * cambiar nada en la presentación  */
				return null;
			}
			
			else if( estado.equals("cambioRecobro"))
			{
				if(!forma.getUsuarioCapitadoSeleccionado().isManejaRecobro()){
					forma.getUsuarioCapitadoSeleccionado().setSelectConvenioEntidadRecobro(null);
				}
				return mapping.findForward("seccionAutorizacion");
			}
			
			else if( estado.equals("cambioConvenioEntidadRecobro"))
			{
				return mapping.findForward("seccionAutorizacion");
			}
			
			else if( estado.equals("cambioViaIngreso"))
			{
				cambioViaIngreso(forma, request, usuario);
				return mapping.findForward("seccionAutorizacion");
			}
			
			else if( estado.equals("autorizarIngresoEstancia"))
			{
				autorizarIngresoEstancia(forma, request, usuario);
				return mapping.findForward("detalleAutorizacion");
			}
			
			else if( estado.equals("volverDetalle"))
			{
				forma.setExtenderAutorizacion(false);
				forma.setEntroPorPosponer(false);
				return mapping.findForward("principal");
			}
			else if(estado.equals("imprimirAutorizacion")){
				validarFormatoImpresion(mapping,forma,request,usuario);
				return mapping.findForward("detalleAutorizacion");
			}
			else if(estado.equals("volver")){
				
				return accionVolver(forma, response, request, mapping);
			}
			
			
		}
		return null;
	}
	
	


	/**
	 * Carga el detalle del elemento seleccionado
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * 
	 *  @author Cristhian Murillo
	*/
	private void validacionesDetalle(ActionMapping mapping, AutorizacionesIngresoEstanciaForm forma, HttpServletRequest request, UsuarioBasico usuario) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		forma.setNuevaAutorizacion(false);
		forma.setExtenderAutorizacion(false);
		forma.setAutorizacionVigente(false);
		
		if(forma.getPosArray()>= 0)
		{
			// Se asigna el registro seleccionado
			forma.setUsuarioCapitadoSeleccionado(new DtoUsuariosCapitados());
			forma.setUsuarioCapitadoSeleccionado(forma.getListaUsuariosCapitados().get(forma.getPosArray()));
			
			// --> Diagnosticos
			Connection con = null;
	    	con = UtilidadBD.abrirConexion();
			forma.getUsuarioCapitadoSeleccionado().getDtoDiagnosticoBusqueda().setTiposDiagnostico(new ArrayList<HashMap<String,Object>>());
			forma.getUsuarioCapitadoSeleccionado().getDtoDiagnosticoBusqueda().setTiposDiagnostico(UtilidadesHistoriaClinica.cargarTiposDiagnostico(con));
			
			Diagnostico diagnosticoNoSeleccionado = new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado,ConstantesBD.codigoCieDiagnosticoNoSeleccionado);
			Diagnostico diagnosticoNoSeleccionadoComplicaicion = new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado,ConstantesBD.codigoCieDiagnosticoNoSeleccionado); 
			forma.getUsuarioCapitadoSeleccionado().getDtoDiagnosticoBusqueda().getValoracionUrgencias().setDiagnosticos(new ArrayList<Diagnostico>());
			forma.getUsuarioCapitadoSeleccionado().getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().add(diagnosticoNoSeleccionado);
			forma.getUsuarioCapitadoSeleccionado().getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().add(diagnosticoNoSeleccionadoComplicaicion);
			forma.getUsuarioCapitadoSeleccionado().setSelectEntidadSubcontratada(null);
			
			UtilidadBD.closeConnection(con);
			
			/* Verificar si el usuario capitado seleccionado tiene Autorizaciones previas de Ingreso/Estancia:
		 		si existe como paciente posiblemente tenga resitro de autorizaciones */
			if(forma.getUsuarioCapitadoSeleccionado().getCodigoPaciente() != null)
			{
				DTOAdministracionAutorizacion parametros = new DTOAdministracionAutorizacion();
				parametros.setAdministracionPoblacionCapitada(false);
				DtoPaciente dtoPaciente = new DtoPaciente();
				dtoPaciente.setCodigo(forma.getUsuarioCapitadoSeleccionado().getCodigoPaciente());
				parametros.setPaciente(dtoPaciente);
				parametros.setOrdenarDescendente(true);
				IAutorizacionIngresoEstanciaServicio autorizacionIngresoEstanciaServicio= ManejoPacienteServicioFabrica.crearAutorizacionIngresoEstanciaServicio();
				ArrayList<DTOAdministracionAutorizacion> listaAutorizacionesIngresoEstancia = new ArrayList<DTOAdministracionAutorizacion>();
				ArrayList<DTOAdministracionAutorizacion> autorizacionesIngresoEstanciaPaciente=autorizacionIngresoEstanciaServicio.obtenerAutorizacionesPorPaciente(parametros);
				if(autorizacionesIngresoEstanciaPaciente != null && !autorizacionesIngresoEstanciaPaciente.isEmpty()){
					for(DTOAdministracionAutorizacion autorizacionIngresoEstanciaDto:autorizacionesIngresoEstanciaPaciente){
						if(autorizacionIngresoEstanciaDto.getEstado().equals(ConstantesIntegridadDominio.acronimoAutorizado)){
							listaAutorizacionesIngresoEstancia.add(autorizacionIngresoEstanciaDto);
						}
					}
				}
				if(Utilidades.isEmpty(listaAutorizacionesIngresoEstancia)){
					forma.setNuevaAutorizacion(true);
				}
				else
				{
					// Se agrega la lista de autorizaciones al registro seleccionado para mostrar el histórico
					forma.getUsuarioCapitadoSeleccionado().setListaAutorizacionesIngresoEstancia(listaAutorizacionesIngresoEstancia);
					
					String diasVigentesNuevaAutoEstancia = ValoresPorDefecto.getDiasVigentesNuevaAutorizacionEstanciaSerArt(usuario.getCodigoInstitucionInt());
					
					// Se toma el último registro de Autorización para hacer las valiadciones
					DTOAdministracionAutorizacion ultimaAutorizacionIngEstancia = new DTOAdministracionAutorizacion();
					ultimaAutorizacionIngEstancia = listaAutorizacionesIngresoEstancia.get(0);
					
					// Se asigna el ingreso instancia con el que se va a trabajar en caso de que se desee extender la autorización
					forma.getUsuarioCapitadoSeleccionado().setAutorizacionIngresoEstancia(ultimaAutorizacionIngEstancia);
					
					Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
					
					// Verificar si la Autorización de Ingreso / Estancia esta vigente.
					boolean autorizacionVigente = false;
					if((ultimaAutorizacionIngEstancia.getFechaVencimientoAutorizacion().after(fechaActual))  ||
							(ultimaAutorizacionIngEstancia.getFechaVencimientoAutorizacion().equals(fechaActual)) )
					{
						autorizacionVigente = true;
					}
					
					forma.setAutorizacionVigente(autorizacionVigente);
					
					if(autorizacionVigente)
					{
						llenarDatosParaPosponer(forma, usuario);
						forma.setNuevaAutorizacion(false);
						String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.autorizacionVigente");
						mostrarErrorEnviado(forma, request, mensajeConcreto);
					}
					else
					{
						if(UtilidadTexto.isEmpty(diasVigentesNuevaAutoEstancia))
						{
							forma.setNuevaAutorizacion(true);
						}
						else
						{
							/*Fecha Vigencia Autorización = Fecha Vencimiento Autorización + Días Vigentes para solicitar nueva autorización estancia.
								 Unicamente indica si la autorización está vigente para extenderla.	*/
							Date fechaVigenciaAutorizacion = UtilidadFecha.conversionFormatoFechaStringDate(
									UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(
											ultimaAutorizacionIngEstancia.getFechaVencimientoAutorizacion()), Integer.parseInt(diasVigentesNuevaAutoEstancia), false));
							
							if( (fechaVigenciaAutorizacion.after(fechaActual))  || (fechaVigenciaAutorizacion.equals(fechaActual)) )
							{
								/* Se asinga la nueva fecha de Inicio a la autorización ingreso estancia a extender. 
									Es un día despues del vencimiento de la última autorización */
								Date fechaInicioAutorizacion = UtilidadFecha.conversionFormatoFechaStringDate(
										UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(
												ultimaAutorizacionIngEstancia.getFechaVencimientoAutorizacion()), 1, false));
								forma.getUsuarioCapitadoSeleccionado().getAutorizacionIngresoEstancia().setFechaInicioAutorizacion(fechaInicioAutorizacion);
																
								forma.setExtenderAutorizacion(true);
								forma.setNuevaAutorizacion(true);
							}
							else{
								forma.setNuevaAutorizacion(true);
							}
						}
					}
				}
			}
			else
			{
				forma.setNuevaAutorizacion(true);
			}
		}
		
		UtilidadTransaccion.getTransaccion().commit();
		
	}



	/**
	 * Llama las validaciones de los campos requeridos apra la busqueda y realiza la busqueda de usuarios capitados
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * 
	 * @author Cristhian Murillo
	 */
	private void accionRealizarBusqueda(ActionMapping mapping, AutorizacionesIngresoEstanciaForm forma, HttpServletRequest request, UsuarioBasico usuario) 
	{
		ActionMessages errores	= new ActionMessages();
		forma.setEntroPorPosponer(false);
		
		// Validar campos requeridos
		errores = validarCamposRequeridosBusqueda(forma, request);

		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		else
		{
			UtilidadTransaccion.getTransaccion().begin();
			IPacienteServicio pacienteServicio 										= com.servinte.axioma.servicio.fabrica.administracion.AdministracionFabricaServicio.crearPacienteServicio();
			forma.setListaUsuariosCapitados(new ArrayList<DtoUsuariosCapitados>());
			
			//hermorhu - MT5967
			//Quitar validacion de Ingreso en estado Cerrado
//			forma.getParametrosBusqueda().setEstadoIngreso(ConstantesIntegridadDominio.acronimoEstadoCerrado);
			
			ArrayList<DtoUsuariosCapitados> listaUsuariosPacienteCapitado = new ArrayList<DtoUsuariosCapitados>();
			
			// Buscar primero como paciente
			listaUsuariosPacienteCapitado.addAll(pacienteServicio.buscarPacienteConvenio(forma.getParametrosBusqueda()));
			
			forma.setListaUsuariosCapitados(listaUsuariosPacienteCapitado);
			
			// Si no se encuentra. Buscar como usuarios capitados
			if(Utilidades.isEmpty(listaUsuariosPacienteCapitado))
			{
				IUsuariosCapitadosServicio usuariosCapitadosServicio					= CapitacionFabricaServicio.crearUsuariosCapitadosServicio();
				listaUsuariosPacienteCapitado = new ArrayList<DtoUsuariosCapitados>();

				listaUsuariosPacienteCapitado.addAll(usuariosCapitadosServicio.buscarUsuariosCapitados(forma.getParametrosBusqueda()));
				
				forma.setListaUsuariosCapitados(listaUsuariosPacienteCapitado);
			}
			
			UtilidadTransaccion.getTransaccion().commit();
		}
		
		forma.setListaNombresReportes(new ArrayList<String>());
	}


	
	
	/**
	 * Valida los campos requeridos para la funcionalidad en el momento de realizar la búsqueda
	 * @param forma
	 * @param request
	 * @return ActionErrors
	 * 
	 * @author Cristhian Murillo
	 */
	private ActionMessages validarCamposRequeridosBusqueda(AutorizacionesIngresoEstanciaForm forma, HttpServletRequest request)
	{
		ActionMessages errores	= new ActionMessages();
		String mensajeConcreto 	= "";
		
		if(UtilidadTexto.isEmpty(forma.getParametrosBusqueda().getConvenio())){
			mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.campoRequerido", "Convenio");
			errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
		}
		
		/* 
		 * Se debe verificar que por lo menos se tengan parametros apra alguno de los siguientes campos:
		 * Tipo ID, Número ID,Primer Apellido o Primer Nombre.  
		*/
		boolean todosNulos = false;
		if(UtilidadTexto.isEmpty(forma.getParametrosBusqueda().getTipoIdentificacion())){
			if(UtilidadTexto.isEmpty(forma.getParametrosBusqueda().getNumeroIdentificacion())){
				if(UtilidadTexto.isEmpty(forma.getParametrosBusqueda().getPrimerApellido())){
					if(UtilidadTexto.isEmpty(forma.getParametrosBusqueda().getPrimerNombre())){
						todosNulos = true;
		}}}}
		
		if(todosNulos){
			mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.algunoRequerido");
			errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
		}
		
		return errores;
	}
	

	/**
	 * Llena los datos de la forma para empezar la funcionalidad
	 * @param forma
	 * @param usuario
	 * 
	 * @author Cristhian Murillo
	 */
	private void llenarForma(AutorizacionesIngresoEstanciaForm forma, UsuarioBasico usuario) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		IConvenioServicio convenioServicio 										= FacturacionServicioFabrica.crearConvenioServicio();
		ITiposAfiliadoServicio tiposAfiliadoServicio 							= ManejoPacienteServicioFabrica.crearTiposAfiliadoServicio();
		ITiposIdentificacionServicio tiposIdentificacionServicio				= AdministracionFabricaServicio.crearTiposIdentificacionServicio();
		IEntregaMedicamentosInsumosEntSubcontratadasServicio entregaMedicamentosInsumosEntSubcontratadasServicio = FacturacionServicioFabrica.crearEntregaMedicamentosInsumosEntSubcontratadasServicio();
		IViasIngresoServicio viasIngresoServicio								= ManejoPacienteServicioFabrica.crearViasIngresoServicio();
		
		forma.setEntroPorPosponer(false);
		
		forma.setListaUsuariosCapitados(new ArrayList<DtoUsuariosCapitados>());
		
		// --> Convenios / Entidad de Recobro
		ArrayList<Convenios> listaConveniosCapitados = new ArrayList<Convenios>();
		listaConveniosCapitados.addAll(convenioServicio.listarConveniosCapitadosActivosPorInstitucion(usuario.getCodigoInstitucionInt()));
		forma.setListaConvenios(listaConveniosCapitados);
		
		// --> Tipos Afiliado
		ArrayList<TiposAfiliado> listaTiposAfiliado = new ArrayList<TiposAfiliado>();
		listaTiposAfiliado.addAll(tiposAfiliadoServicio.obtenerTiposAfiliado());
		forma.setListaTiposAfiliado(listaTiposAfiliado);
		
		// --> Tipos Identificación
		String[] listaTipoDeTiposIdentificacion = { 
				ConstantesIntegridadDominio.acronimoTipoTipoIdentificacionPersona,
				ConstantesIntegridadDominio.acronimoTipoTipoIdentificacionAmbos,
				ConstantesIntegridadDominio.acronimoTipoTipoIdentificacionTercero
		};
		ArrayList<TiposIdentificacion> listaTiposIdentificacion = new ArrayList<TiposIdentificacion>();
		listaTiposIdentificacion.addAll(tiposIdentificacionServicio.listarTiposIdentificacionPorTipo(listaTipoDeTiposIdentificacion));
		forma.setListaTiposIdentificacion(listaTiposIdentificacion);
		
		// --> Vias de Ingreso
		ArrayList<ViasIngreso> listaViasIngreso = new ArrayList<ViasIngreso>();
		listaViasIngreso.addAll(viasIngresoServicio.buscarViasIngreso()); 
		forma.setListaViasIngreso(listaViasIngreso);
	
		// -> Entidades subcontratadas
		ArrayList<DtoEntidadSubcontratada> listaEntidadesSubcontratadas = new ArrayList<DtoEntidadSubcontratada>();
		listaEntidadesSubcontratadas = entregaMedicamentosInsumosEntSubcontratadasServicio.listarEntidadesSubXCentroCostoActivo(ConstantesBD.codigoNuncaValido);
		forma.setListaEntidadesSubcontratadas(listaEntidadesSubcontratadas);
				/* El sistema también debe mostrar la Opción Otra: */
		DtoEntidadSubcontratada dtoEntidadSubcontratadaOtra = new DtoEntidadSubcontratada();
		dtoEntidadSubcontratadaOtra.setCodigoPk(ConstantesBD.codigoNuncaValidoLong);
		dtoEntidadSubcontratadaOtra.setRazonSocial("Otra");
		forma.getListaEntidadesSubcontratadas().add(dtoEntidadSubcontratadaOtra); 
		//-----------------------------------------------
		
		// --> Convenios / Entidad de Recobro
		ArrayList<Convenios> listaConvenioEntidadRecobro = new ArrayList<Convenios>();
		listaConvenioEntidadRecobro.addAll(convenioServicio.listarConveniosActivosPorInstitucion(usuario.getCodigoInstitucionInt()));
		Convenios convenioOtro; convenioOtro = new Convenios();
		convenioOtro.setNombre("Otra");
		convenioOtro.setCodigo(ConstantesBD.codigoNuncaValido);
		listaConvenioEntidadRecobro.add(convenioOtro);
		forma.setListaConvenioEntidadRecobro(listaConvenioEntidadRecobro);
		// --------------------------------------------------------------------------------------
		
		UtilidadTransaccion.getTransaccion().begin();
	}

	
	/**
	 * Valida las precondiciones para empezas la funcionalidad
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return ActionForward
	 * 
	 * @author Cristhian Murillo
	 */
	private ActionForward validarEmpezar(ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
	{
		// Validación consecutivo disponible
		int codigoInstitucion = usuario.getCodigoInstitucionInt();
		Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
		String consecutivoAutoCapitaSub = UtilidadBD.obtenerValorActualTablaConsecutivos(conH, ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada, codigoInstitucion);
		
		if(UtilidadTexto.isEmpty(consecutivoAutoCapitaSub))
		{
			String mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.noExisteConsecutivoDisponible");
			Connection con = null;
	    	con = UtilidadBD.abrirConexion();
	    	return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no cargado", mensajeConcreto, false);
		}
		
		return null;
	}
	
	
	
	
	
	
	/*-------------------------------------------------------*/
	/* Otros Métodos / Genéricos
	/*-------------------------------------------------------*/
	
	/**
	 * Muestra el mensaje de error enviado
	 * @param forma
	 * @param request
	 * 
	 * @autor Cristhian Murillo
	 */
	private void mostrarErrorEnviado(AutorizacionesIngresoEstanciaForm forma, HttpServletRequest request, String mensajeConcreto) 
	{
		ActionMessages errores = new ActionMessages();
		errores.add("error_concreto_enviado", new ActionMessage("errors.notEspecific", mensajeConcreto));
		saveErrors(request, errores);
	}
	
	
	/**
	 * Muestra el mensaje de error enviado
	 * @param forma
	 * @param request
	 * @param errores
	 * 
	 * @autor Cristhian Murillo
	 */
	private ActionMessages retornarErrorEnviado(AutorizacionesIngresoEstanciaForm forma, HttpServletRequest request, String mensajeConcreto, ActionMessages errores) 
	{
		errores.add("error_concreto_enviado", new ActionMessage("errors.notEspecific", mensajeConcreto));
		saveErrors(request, errores);
		return errores;
	}
	
	
	/**
	 * Método de ordenamiento generico
	 * @param mapping
	 * @param forma
	 * @return ActionForward
	 * 
	 * @author Cristhian Murillo
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,	AutorizacionesIngresoEstanciaForm forma) 
	{
		boolean ordenamiento = false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaUsuariosCapitados(),sortG);

		//return retornoSegunTipo(forma, mapping);
		return null;
	}
	
	
	
	/**
	 * Cambia la fecha de vencimiento para la autorización 
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * 
	 * @author Cristhian Murillo
	 */
	private void cambioDiasEstanciaAutorizados(AutorizacionesIngresoEstanciaForm forma, HttpServletRequest request, UsuarioBasico usuario) 
	{
		forma.getUsuarioCapitadoSeleccionado().getAutorizacionIngresoEstancia().setFechaVencimientoAutorizacion(null);
		
		ActionMessages errores	= new ActionMessages();
		String mensajeConcreto 	= "";
		
		if(forma.getUsuarioCapitadoSeleccionado().getAutorizacionIngresoEstancia().getFechaInicioAutorizacion() != null)
		{
			if(forma.getUsuarioCapitadoSeleccionado().getAutorizacionIngresoEstancia().getDiasEstanciaAutorizados() > 0)
			{
				Date fechaVencimientoAutorizacion = UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.incrementarDiasAFecha(
						UtilidadFecha.conversionFormatoFechaAAp(forma.getUsuarioCapitadoSeleccionado().getAutorizacionIngresoEstancia().getFechaInicioAutorizacion()), forma.getUsuarioCapitadoSeleccionado().getAutorizacionIngresoEstancia().getDiasEstanciaAutorizados(), false));
				
				forma.getUsuarioCapitadoSeleccionado().getAutorizacionIngresoEstancia().setFechaVencimientoAutorizacion(fechaVencimientoAutorizacion);
			}
			else{
				mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.campoRequerido", "Días Estancia Autorizados");
				errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
			}
		}
		else{
			mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.campoRequerido", "Fecha Inicio Autorización");
			errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
		}
		
		
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
	}
	
     
	
	/**
	 * Cambia y valida la entidad subcontratada si s otra o no para determinar 
	 * que datos deben ser cargados en los campos de entidad subcontratada.
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * 
	 * @author Cristhian Murillo
	 */
	private void cambioEntidadSubcontratada(AutorizacionesIngresoEstanciaForm forma, HttpServletRequest request, UsuarioBasico usuario) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		forma.getUsuarioCapitadoSeleccionado().resetEntidadSubOtra();
		
		// Asignar EntidadSubcontratada Seleccionada(forma);
		if(forma.getUsuarioCapitadoSeleccionado().getSelectEntidadSubcontratada()!=null)
		{
			if(forma.getUsuarioCapitadoSeleccionado().getSelectEntidadSubcontratada()!=ConstantesBD.codigoNuncaValidoLong )
			{
				for (DtoEntidadSubcontratada dtoEntidadSubcontratada : forma.getListaEntidadesSubcontratadas()) 
				{
					if(dtoEntidadSubcontratada.getCodigoPk() == forma.getUsuarioCapitadoSeleccionado().getSelectEntidadSubcontratada().longValue())
					{
						forma.getUsuarioCapitadoSeleccionado().setDescripcionEntidadSubOtra(dtoEntidadSubcontratada.getRazonSocial());
						forma.getUsuarioCapitadoSeleccionado().setTelefonoEntidadSubOtra(dtoEntidadSubcontratada.getTelefono());
						forma.getUsuarioCapitadoSeleccionado().setDireccionEntidadSubOtra(dtoEntidadSubcontratada.getDireccion());
						break;
					}
				}
			}
		}
		else{
			forma.getUsuarioCapitadoSeleccionado().resetEntidadSubOtra();
		}
			
		UtilidadTransaccion.getTransaccion().commit();
	}

	
	
	/**
	 * Al cambiar la vía de ingreso actualiza las entidades subcontratadas teniendo en cuenta la vía de ingreso seleccionada.
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * 
	 * @author Cristhian Murillo
	 */
	private void cambioViaIngreso(AutorizacionesIngresoEstanciaForm forma, HttpServletRequest request, UsuarioBasico usuario) 
	{
		ArrayList<DtoEntidadSubcontratada> listaEntidadesSubcontratadas = new ArrayList<DtoEntidadSubcontratada>();
		forma.setListaEntidadesSubcontratadas(listaEntidadesSubcontratadas);
		if(forma.getUsuarioCapitadoSeleccionado() != null && forma.getUsuarioCapitadoSeleccionado().getDatosAdmision() != null
				&& forma.getUsuarioCapitadoSeleccionado().getDatosAdmision().getViaIngreso() != null
				&& !forma.getUsuarioCapitadoSeleccionado().getDatosAdmision().getViaIngreso().trim().isEmpty()){
			UtilidadTransaccion.getTransaccion().begin();
			IEntidadesSubcontratadasServicio entidadesSubcontratadasServicio 		= FacturacionServicioFabrica.crearEntidadesSubcontratadasServicio();
			DtoEntidadSubcontratada parametros = new DtoEntidadSubcontratada();
			parametros.setViaIngreso(Integer.parseInt(forma.getUsuarioCapitadoSeleccionado().getDatosAdmision().getViaIngreso()));
			parametros.setPermiteEstanciaPaciente(true);
			
			listaEntidadesSubcontratadas = entidadesSubcontratadasServicio.listarEntidadesSubXViaIngreso(parametros);
			
			forma.setListaEntidadesSubcontratadas(listaEntidadesSubcontratadas);
			
			/* El sistema también debe mostrar la Opción Otra: */
			DtoEntidadSubcontratada dtoEntidadSubcontratadaOtra = new DtoEntidadSubcontratada();
			dtoEntidadSubcontratadaOtra.setCodigoPk(ConstantesBD.codigoNuncaValidoLong);
			dtoEntidadSubcontratadaOtra.setRazonSocial("Otra");
			forma.getListaEntidadesSubcontratadas().add(dtoEntidadSubcontratadaOtra); 
			//-----------------------------------------------
			
			UtilidadTransaccion.getTransaccion().commit();
		}
	}
		
	
	
	
	/**
	 * Realiza el proceso de Autorizacion de Ingreso Estancia haciendo als validaciones necesarias y realizando el guardado.
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * 
	 * @author Cristhian Murillo
	 */
	private void autorizarIngresoEstancia(AutorizacionesIngresoEstanciaForm forma, HttpServletRequest request, UsuarioBasico usuario) 
	{
		// validar requeridos
		ActionMessages errores	= new ActionMessages();
		
		// Validar campos requeridos
		errores = validarCamposRequeridosAutorizar(forma, request);

		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}

		else
		{
			UtilidadTransaccion.getTransaccion().begin();
			IPacienteServicio pacienteServicio 										= com.servinte.axioma.servicio.fabrica.administracion.AdministracionFabricaServicio.crearPacienteServicio();
			IIngresosEstanciaServicio ingresosEstanciaServicio						= ManejoPacienteServicioFabrica.crearIngresosEstancia();
			Pacientes paciente;	paciente = new Pacientes();
			
			String consecutivoCapitacion = UtilidadBD.obtenerValorConsecutivoDisponible(
					ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada,
					Integer.valueOf(usuario.getCodigoInstitucionInt()));

			Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
			
			try {
				/* Si el usuario capitado aún no es un Paciente del sistema se debe: */
				if(forma.getUsuarioCapitadoSeleccionado().getCodigoPaciente() == null)
				{
					paciente = guardarPacienteCapitado(forma, usuario, paciente);
					forma.getUsuarioCapitadoSeleccionado().setCodigoPaciente(paciente.getCodigoPaciente());
				}
				else{
					paciente = pacienteServicio.findById(forma.getUsuarioCapitadoSeleccionado().getCodigoPaciente());
				}
				
				IngresosEstancia ingresosEstancia;	ingresosEstancia = new IngresosEstancia();
				if(forma.isEntroPorPosponer()){
					ingresosEstancia = ingresosEstanciaServicio.findById(forma.getUsuarioCapitadoSeleccionado().getAutorizacionIngresoEstancia().getCodIngresoEstancia());
				}
				else{
					ingresosEstancia = guardarIngresoEstancia(forma, paciente, usuario, ingresosEstancia);
				}
				
				guardarAutorizacionIngresoEstancia(forma, usuario, ingresosEstancia, consecutivoCapitacion);
				
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH, ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada, usuario.getCodigoInstitucionInt(), consecutivoCapitacion, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
				UtilidadTransaccion.getTransaccion().commit();
				//generarReporteAutorizacionFormatoVersalles(forma, usuario, request);
				
				forma.setEstado("resumen");
				
			} catch (Exception e) {
				Log4JManager.error("No se guardo registro", e);
				UtilidadTransaccion.getTransaccion().rollback();
				UtilidadTransaccion.getTransaccion().begin();
				conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH, ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada, usuario.getCodigoInstitucionInt(), consecutivoCapitacion, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				UtilidadTransaccion.getTransaccion().commit();
			}
			
		}
	}

	
	
	/**
	 * Guarda el registro de Autorización de Ingreso Estancia.
	 * 
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param ingresosEstancia
	 * @param consecutivoCapitacion
	 * 
	 * @author Cristhian Murillo
	 */
	private void guardarAutorizacionIngresoEstancia(AutorizacionesIngresoEstanciaForm forma, UsuarioBasico usuarioBasico, IngresosEstancia ingresosEstancia, String consecutivoCapitacion) throws ObtenerEstanciaViaIngresoCentroCostoException 
	{
		IAutorizacionIngresoEstanciaServicio autorizacionIngresoEstanciaServicio= ManejoPacienteServicioFabrica.crearAutorizacionIngresoEstanciaServicio();
		ICentroCostosServicio centroCostosServicio								= AdministracionFabricaServicio.crearCentroCostosServicio();
		
		DtoUsuariosCapitados datos = forma.getUsuarioCapitadoSeleccionado();
				
		CentrosCosto centrosCosto;	centrosCosto = new CentrosCosto();
		DTOEstanciaViaIngCentroCosto parametros = new DTOEstanciaViaIngCentroCosto();
		parametros.setEntidadSubcontratada(datos.getSelectEntidadSubcontratada());
		parametros.setViaIngreso(Integer.parseInt(datos.getDatosAdmision().getViaIngreso()));
		centrosCosto = centroCostosServicio.obtenerCentrosCostoPorViaIngresoEntSub(parametros); // Centro de costo que ejecuta	
		Long numeroAutorizacion = ConstantesBD.codigoNuncaValidoLong;
		
		if(datos.getSelectEntidadSubcontratada()!=ConstantesBD.codigoNuncaValidoLong )
		{
			if(centrosCosto == null)
			{
				throw new ObtenerEstanciaViaIngresoCentroCostoException();
			}
			
		}
		
		
		
		AutorizacionesIngreEstancia autorizacionesIngreEstancia;	autorizacionesIngreEstancia = new AutorizacionesIngreEstancia();
		/*
			Convenios convenio; convenio = new Convenios();
			convenio.setCodigo(datos.getConvenioInt());
		autorizacionesIngreEstancia.setConvenios(convenio);
		*/
		
		autorizacionesIngreEstancia.setIngresosEstancia(ingresosEstancia);
		
		if(centrosCosto != null){
			autorizacionesIngreEstancia.setCentrosCosto(centrosCosto);
		}

		autorizacionesIngreEstancia.setConsecutivoAdmision(Long.parseLong(consecutivoCapitacion));
		
		autorizacionesIngreEstancia.setDiasEstanciaAutorizados(datos.getAutorizacionIngresoEstancia().getDiasEstanciaAutorizados());
		autorizacionesIngreEstancia.setUsuarioContacta(datos.getAutorizacionIngresoEstancia().getUsuarioContacta());
		autorizacionesIngreEstancia.setObservaciones(datos.getAutorizacionIngresoEstancia().getObservaciones());
		autorizacionesIngreEstancia.setEstado(ConstantesIntegridadDominio.acronimoAutorizado);
		autorizacionesIngreEstancia.setCargoUsuContacta(datos.getAutorizacionIngresoEstancia().getCargoUsuarioContacta());
		autorizacionesIngreEstancia.setFechaInicioAutorizacion(datos.getAutorizacionIngresoEstancia().getFechaInicioAutorizacion());
		
		/**
		 * Se debe guardar en el registro de estancia el usuario, fecha y hora en que se guarda el registro.
		 * DCU 1094 Versi&oacute;n 1.1
		 * @author Diana Ruiz
		 * @since 05/07/2010
		 */
		
		Usuarios usuario = new Usuarios();
		usuario.setLogin(usuarioBasico.getLoginUsuario());
		autorizacionesIngreEstancia.setUsuarios(usuario);		
		autorizacionesIngreEstancia.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.getFechaActual()));
		autorizacionesIngreEstancia.setHoraAutorizacion(UtilidadFecha.getHoraActual());	
		
		// Se verifica si es Entidad Autoriza = Otra para decinir si es temporal o no
		if(forma.getUsuarioCapitadoSeleccionado().getSelectEntidadSubcontratada() == ConstantesBD.codigoNuncaValidoLong){
			autorizacionesIngreEstancia.setIndicativoTemporal(ConstantesBD.acronimoSiChar);
		} else{
			autorizacionesIngreEstancia.setIndicativoTemporal(ConstantesBD.acronimoNoChar);
		}
		
		// Si maneja recobro y se selecciono 'Otro' se guarda la descripción
		if(datos.isManejaRecobro()){
			if(datos.getSelectConvenioEntidadRecobro() == ConstantesBD.codigoNuncaValido){
				autorizacionesIngreEstancia.setOtroConvenioRecobro(datos.getDescripcionOtraEntidadRecobrar());
			}
			else{
					Convenios convenio; convenio = new Convenios();
					convenio.setCodigo(datos.getSelectConvenioEntidadRecobro());
				autorizacionesIngreEstancia.setConvenios(convenio);
			}
		}
		
		//-------------------------------------------------------------------------
		// Histórico
		HistoAutorizacionIngEstan histoAutorizacionIngEstan;		histoAutorizacionIngEstan = new HistoAutorizacionIngEstan();
		llenarHistoricoAutorizacionIngEstan(autorizacionesIngreEstancia, histoAutorizacionIngEstan, usuarioBasico);
		HashSet<HistoAutorizacionIngEstan> setHistoAutorizacionIngEstan = new HashSet<HistoAutorizacionIngEstan>();
		setHistoAutorizacionIngEstan.add(histoAutorizacionIngEstan);
		autorizacionesIngreEstancia.setHistoAutorizacionIngEstans(setHistoAutorizacionIngEstan);
		//-------------------------------------------------------------------------
		
		autorizacionIngresoEstanciaServicio.attachDirty(autorizacionesIngreEstancia);
	
		
		if (autorizacionesIngreEstancia.getCodigoPk() > 0){
			numeroAutorizacion = autorizacionesIngreEstancia.getCodigoPk();
			forma.setNumeroAutorizacion(numeroAutorizacion);
		}
		
		Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
		UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH, ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada, usuarioBasico.getCodigoInstitucionInt(), consecutivoCapitacion, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		
	}




	/**
	 * Llena los datos para el historico del las Autorizaciones Ingreso Estancia.
	 * 
	 * @param autorizacionesIngreEstancia
	 * @param histoAutorizacionIngEstan
	 * @param usuario
	 * 
	 * @param Cristhian Murillo
	 */
	private void llenarHistoricoAutorizacionIngEstan(AutorizacionesIngreEstancia autorizacionesIngreEstancia, HistoAutorizacionIngEstan histoAutorizacionIngEstan, UsuarioBasico usuario) 
	{ 
		histoAutorizacionIngEstan.setAutorizacionesIngreEstancia(autorizacionesIngreEstancia);
			Usuarios usuarios; usuarios = new Usuarios();
			usuarios.setLogin(usuario.getLoginUsuario());
		histoAutorizacionIngEstan.setUsuarios(usuarios);
		histoAutorizacionIngEstan.setFechaInicioAutorizacion(autorizacionesIngreEstancia.getFechaInicioAutorizacion());
		histoAutorizacionIngEstan.setConsecutivoAdmision(autorizacionesIngreEstancia.getConsecutivoAdmision());
		histoAutorizacionIngEstan.setDiasEstanciaAutorizados(autorizacionesIngreEstancia.getDiasEstanciaAutorizados());
		histoAutorizacionIngEstan.setUsuarioContacta(autorizacionesIngreEstancia.getUsuarioContacta());
		histoAutorizacionIngEstan.setCargoUsuarioContacta(autorizacionesIngreEstancia.getCargoUsuContacta());
		histoAutorizacionIngEstan.setObservaciones(autorizacionesIngreEstancia.getObservaciones());
		histoAutorizacionIngEstan.setIndicativoTemporal(autorizacionesIngreEstancia.getIndicativoTemporal());
		histoAutorizacionIngEstan.setEstado(autorizacionesIngreEstancia.getEstado());
		histoAutorizacionIngEstan.setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
		histoAutorizacionIngEstan.setHoraModifica(UtilidadFecha.getHoraActual());
		histoAutorizacionIngEstan.setAccionRealizada(ConstantesIntegridadDominio.acronimoAccionHistoricaInsertar);
		
		if(autorizacionesIngreEstancia.getCentrosCosto() != null){
			histoAutorizacionIngEstan.setCentroCostoSolicitante(autorizacionesIngreEstancia.getCentrosCosto().getCodigo());
		}
		
		if(autorizacionesIngreEstancia.getConvenios() == null){
			histoAutorizacionIngEstan.setOtroConvenioRecobro(autorizacionesIngreEstancia.getOtroConvenioRecobro());
		}
		else{
			histoAutorizacionIngEstan.setConvenioRecobro(autorizacionesIngreEstancia.getConvenios().getCodigo());
		}
		
	}



	/**
	 * Guarda el registro del ingreso estancia.
	 * 
	 * @param forma
	 * @param paciente
	 * @param usuario
	 * @param ingresosEstancia
	 * 
	 * @author Cristhian Murillo
	*/
	private IngresosEstancia guardarIngresoEstancia(AutorizacionesIngresoEstanciaForm forma, Pacientes paciente, UsuarioBasico usuario, IngresosEstancia ingresosEstancia) 
	{
		DtoUsuariosCapitados datos = forma.getUsuarioCapitadoSeleccionado();
		
		ingresosEstancia 				     = new IngresosEstancia();
		
			Diagnosticos diagnosticosByFkIeDxPpal;		diagnosticosByFkIeDxPpal = new Diagnosticos();
			DiagnosticosId diagnosticosId;	diagnosticosId = new DiagnosticosId();
			diagnosticosId.setAcronimo(datos.getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(0).getAcronimo());
			diagnosticosId.setTipoCie(datos.getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(0).getTipoCIE());
			diagnosticosByFkIeDxPpal.setId(diagnosticosId);
		ingresosEstancia.setDiagnosticosByFkIeDxPpal(diagnosticosByFkIeDxPpal);
		
		Diagnostico diagnosticoNoSeleccionado = new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado,ConstantesBD.codigoCieDiagnosticoNoSeleccionado);
		if(!datos.getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(1).getAcronimo().equals(diagnosticoNoSeleccionado.getAcronimo())){
			if(datos.getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(1).getTipoCIE() != diagnosticoNoSeleccionado.getTipoCIE())
			{
					Diagnosticos diagnosticosByFkIeDxCompli;		diagnosticosByFkIeDxCompli = new Diagnosticos();
					diagnosticosId = new DiagnosticosId();
					diagnosticosId.setAcronimo(datos.getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(1).getAcronimo());
					diagnosticosId.setTipoCie(datos.getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(1).getTipoCIE());
					diagnosticosByFkIeDxCompli.setId(diagnosticosId);
				ingresosEstancia.setDiagnosticosByFkIeDxCompli(diagnosticosByFkIeDxCompli);
			}
		}
		
		ingresosEstancia.setPacientes(paciente);
		
			ViasIngreso viasIngreso;	viasIngreso = new ViasIngreso();
			viasIngreso.setCodigo(Integer.parseInt(datos.getDatosAdmision().getViaIngreso()));
		ingresosEstancia.setViasIngreso(viasIngreso);
			
		if(datos.getTipoAfiliado() != null){
				TiposAfiliado tiposAfiliado; tiposAfiliado = new TiposAfiliado();
				tiposAfiliado.setAcronimo(datos.getTipoAfiliado());
			ingresosEstancia.setTiposAfiliado(tiposAfiliado);
		}
		
		/**
		 * Se guarda en el ingreso la clasificaci&oacute;n socioeconomica
		 * DCU 1094 Versi&oacute;n de cambio 1.1
		 * @author Diana Ruiz
		 * @since 05/07/2011
		 */
		
		if(datos.getCodigoEstratoSocial() != null){			
			EstratosSociales estratosSociales=new EstratosSociales();
			estratosSociales.setCodigo(datos.getCodigoEstratoSocial());			
			ingresosEstancia.setEstratosSociales(estratosSociales);			
		}	
			
			Instituciones instituciones;	instituciones = new  Instituciones();
			instituciones.setCodigo(usuario.getCodigoInstitucionInt());
		ingresosEstancia.setInstituciones(instituciones);
		
		ingresosEstancia.setFechaAdmision(datos.getDatosAdmision().getFechaAdmision());
		ingresosEstancia.setHoraAdmision(datos.getDatosAdmision().getHoraAdmision());
		ingresosEstancia.setMedicoSolicitante(datos.getDatosAdmision().getMedicoSolicitante());
		if(!UtilidadTexto.isEmpty(datos.getDatosAdmision().getObservaciones())){
			ingresosEstancia.setObservaciones(datos.getDatosAdmision().getObservaciones());
		}
		
		// Se verifica si es Entidad Autoriza = Otra para guardar la seleccionada o los datos d euna nueva
		if(datos.getSelectEntidadSubcontratada() == ConstantesBD.codigoNuncaValidoLong)
		{
			ingresosEstancia.setDescripcionEntidadSub(datos.getDescripcionEntidadSubOtra());
			ingresosEstancia.setTelefonoEntidadSub(datos.getTelefonoEntidadSubOtra());
			ingresosEstancia.setDireccionEntidadSub(datos.getDireccionEntidadSubOtra());
		}
		
			EntidadesSubcontratadas entidadesSubcontratadas;	entidadesSubcontratadas = new EntidadesSubcontratadas();
			entidadesSubcontratadas.setCodigoPk(datos.getSelectEntidadSubcontratada());
		ingresosEstancia.setEntidadesSubcontratadas(entidadesSubcontratadas);
		IIngresosEstanciaServicio ingresosEstanciaServicio						= ManejoPacienteServicioFabrica.crearIngresosEstancia();
		ingresosEstanciaServicio.attachDirty(ingresosEstancia);
		
		return ingresosEstancia;
	}



	/**
	 * Crear el Paciente tomando la información de Usuarios Capitados. 
	 * Guarda el paciente en todas las estructuras asociadas a los pacientes capitados.
	 * 
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * 
	 * @author Cristhian Murillo
	 */
	private Pacientes guardarPacienteCapitado(AutorizacionesIngresoEstanciaForm forma, UsuarioBasico usuarioBasico, Pacientes paciente)
	{
		/*Crear el Paciente tomando la información de Usuarios Capitados. 
		 * Una vez este creado el Paciente se debe eliminar el Usuario de Usuarios Capitados (Tabla temporal). 
			Adicionar el paciente en todas las estructuras asociadas a los pacientes capitados.
		 */
		DtoUsuariosCapitados datos = forma.getUsuarioCapitadoSeleccionado();
		IPersonasServicio personasServicio										= com.servinte.axioma.servicio.fabrica.administracion.AdministracionFabricaServicio.crearPersonaServicio();
		IUsuarioXConvenioServicio usuarioXConvenioServicio 						= CapitacionFabricaServicio.crearUsuarioXConvenioServicio();
		IPacienteServicio pacienteServicio 										= com.servinte.axioma.servicio.fabrica.administracion.AdministracionFabricaServicio.crearPacienteServicio();
		// Llenar Persona
		Personas persona;	persona = new Personas();
		persona.setPrimerNombre(datos.getPrimerNombre());
		persona.setSegundoNombre(datos.getSegundoNombre());
		persona.setPrimerApellido(datos.getPrimerApellido());
		persona.setSegundoApellido(datos.getSegundoApellido());
			TiposIdentificacion tiposIdentificacion = new TiposIdentificacion();
			tiposIdentificacion.setAcronimo(datos.getTipoIdentificacion());
		persona.setTiposIdentificacion(tiposIdentificacion);
		persona.setNumeroIdentificacion(datos.getNumeroIdentificacion());
		persona.setDireccion(datos.getDireccion());
		//Mt 5365 Anexa datos a persona como barrio, sexo y centro de atención
		Barrios barrio = new Barrios();
        barrio.setCodigo(datos.getCodigoBarrio());
		persona.setBarrios(barrio);
		Sexo sex = new Sexo();
		if(datos.getCodigoSexo() != null){
		    sex.setCodigo(datos.getCodigoSexo());
		}
		persona.setSexo(sex);
		persona.setFechaNacimiento(datos.getFechaNacimiento());
			TiposPersonas tiposPersonas;	tiposPersonas = new TiposPersonas();	
			tiposPersonas.setCodigo(ConstantesBD.codigoTipoPersonaNatural);
		persona.setTiposPersonas(tiposPersonas);
		persona.setIndicativoInterfaz(ConstantesBD.acronimoNo);
		
		personasServicio.attachDirty(persona);
		
		
		// Llenar Paciente
		//Pacientes paciente;	paciente = new Pacientes();
		paciente = null;
		paciente = new Pacientes();
			Convenios convenio; convenio = new Convenios();
			convenio.setCodigo(datos.getConvenioInt());
		paciente.setConvenios(convenio);
		paciente.setEstaVivo(true);
		paciente.setGrupoPoblacional(ConstantesIntegridadDominio.acronimoOtrosGruposPoblacionales);
		paciente.setActivo(ConstantesBD.acronimoSi);
		
		//MT 5365 anexa centro atencion a paciente
		CentroAtencion centroAtencion = new CentroAtencion();
		if(datos.getCentroAtencionConsecutivo() != null){
			centroAtencion.setConsecutivo(datos.getCentroAtencionConsecutivo());
		 }
		paciente.setCentroAtencionByCentroAtencionPyp(centroAtencion);
		
		paciente.setPersonas(persona); 
		pacienteServicio.attachDirty(paciente, usuarioBasico.getCodigoInstitucionInt());
		
		
		/* =================================================================================== 
		 El tipo de Afiliado y el Estrato social son datos que se tienen, 
			pero solo se pueden asociar a una subcuenta
		
			EstratosSociales estratosSociales; estratosSociales = new EstratosSociales();
			estratosSociales.setCodigo(datos.getCodigoEstratoSocial());
		paciente.setEstratosSociales(estratosSociales);
			TiposAfiliado tiposAfiliado; tiposAfiliado = new TiposAfiliado();
			tiposAfiliado.setAcronimo(datos.getTipoAfiliado());
		paciente.setTipoAfiliado(tiposAfiliado);
		=================================================================================== */
		
		UsuarioXConvenio usuarioXConvenio;	usuarioXConvenio = new UsuarioXConvenio();
		usuarioXConvenio.setFechaInicial(datos.getFechaInicial());
		usuarioXConvenio.setFechaFinal(datos.getFechaFinal());
		usuarioXConvenio.setUsuario(usuarioBasico.getLoginUsuario());
		usuarioXConvenio.setPersonas(persona);
			Contratos contrato;	contrato = new Contratos();
			contrato.setCodigo(datos.getCodigoContrato());
		usuarioXConvenio.setContratos(contrato);	
			TiposCargue tiposCargue;	tiposCargue = new TiposCargue();
			tiposCargue.setCodigo(datos.getCodigoTipoCargue());
		usuarioXConvenio.setTiposCargue(tiposCargue);
		usuarioXConvenio.setFechaCargue(datos.getFechaInicial());
		usuarioXConvenio.setActivo(ConstantesBD.acronimoSi);
		
		if(datos.getCodigoEstratoSocial() != null){
				EstratosSociales estratosSociales; estratosSociales = new EstratosSociales();
				estratosSociales.setCodigo(datos.getCodigoEstratoSocial());
			usuarioXConvenio.setEstratosSociales(estratosSociales);
		}
		
		usuarioXConvenioServicio.attachDirty(usuarioXConvenio);
		
		eliminarUsuarioCapitado(datos);
		
		return paciente;
	}
	
	
	
	/**
	 * Una vez este creado el Paciente se debe eliminar el Usuario de Usuarios Capitados (Tabla temporal).
	 * @param datos
	 * 
	 * @author Cristhian Murillo
	 */
	private void eliminarUsuarioCapitado(DtoUsuariosCapitados datos) 
	{
		IUsuariosCapitadosServicio usuariosCapitadosServicio					= CapitacionFabricaServicio.crearUsuariosCapitadosServicio();
		usuariosCapitadosServicio.delete(usuariosCapitadosServicio.findById(datos.getCodigoUsuarioCapitado()));
	}



	/**
	 * Valida los campos requeridos para la funcionalidad en el momento de realizar la autorización
	 * @param forma
	 * @param request
	 * @return ActionErrors
	 * 
	 * @author Cristhian Murillo
	 */
	private ActionMessages validarCamposRequeridosAutorizar(AutorizacionesIngresoEstanciaForm forma, HttpServletRequest request)
	{
		ActionMessages errores	= new ActionMessages();
		String mensajeConcreto 	= "";
		
		
		// Validaciones Datos Admisión =============================================================
		if(forma.getUsuarioCapitadoSeleccionado().getDatosAdmision().getFechaAdmision() == null){
			mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.campoRequerido", "Fecha Autorización");
			errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
		}
		
		if(UtilidadTexto.isEmpty(forma.getUsuarioCapitadoSeleccionado().getDatosAdmision().getHoraAdmision())){
			mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.campoRequerido", "Hora Autorización");
			errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
		}
		
		if(UtilidadTexto.isEmpty(forma.getUsuarioCapitadoSeleccionado().getDatosAdmision().getViaIngreso())){
			mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.campoRequerido", "Vía de Ingreso");
			errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
		}
		
		Diagnostico diagnosticoNoSeleccionado = new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado,ConstantesBD.codigoCieDiagnosticoNoSeleccionado);
		if(Utilidades.isEmpty(forma.getUsuarioCapitadoSeleccionado().getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos())){
			mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.campoRequerido", "Dx Ingreso Principal");
			errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
		}
		else if (forma.getUsuarioCapitadoSeleccionado().getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(0).getAcronimo().equals(diagnosticoNoSeleccionado.getAcronimo())){
			if (forma.getUsuarioCapitadoSeleccionado().getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(0).getTipoCIE() == diagnosticoNoSeleccionado.getTipoCIE()){
				mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.campoRequerido", "Dx Ingreso Principal");
				errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
			}
		}
		
		if(UtilidadTexto.isEmpty(forma.getUsuarioCapitadoSeleccionado().getDatosAdmision().getMedicoSolicitante())){
			mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.campoRequerido", "Médico Solicitante");
			errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
		}
		
		
		// Validaciones Datos Autorización =============================================================
		if(forma.getUsuarioCapitadoSeleccionado().getSelectEntidadSubcontratada() == null){
			mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.campoRequerido", "Entidad Autorizada");
			errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
		}
		
		// Se verifica si es Entidad Autoriza = Otra
		if(forma.getUsuarioCapitadoSeleccionado().getSelectEntidadSubcontratada() == ConstantesBD.codigoNuncaValidoLong)
		{
			if(UtilidadTexto.isEmpty(forma.getUsuarioCapitadoSeleccionado().getDescripcionEntidadSubOtra())){
				mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.campoRequerido", "Descripción Entidad");
				errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
			}
			if(UtilidadTexto.isEmpty(forma.getUsuarioCapitadoSeleccionado().getDireccionEntidadSubOtra())){
				mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.campoRequerido", "Dirección Entidad");
				errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
			}
			if(UtilidadTexto.isEmpty(forma.getUsuarioCapitadoSeleccionado().getTelefonoEntidadSubOtra())){
				mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.campoRequerido", "Teléfono Entidad");
				errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
			}
		}
		
		if(forma.getUsuarioCapitadoSeleccionado().getAutorizacionIngresoEstancia().getFechaInicioAutorizacion() == null){
			mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.campoRequerido", "Fecha Inicio Autorización");
			errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
		}
		
		if(forma.getUsuarioCapitadoSeleccionado().getAutorizacionIngresoEstancia().getDiasEstanciaAutorizados() <= 0){
			mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.campoRequerido", "Días Estancia Autorizados");
			errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
		}
		
		// Si maneja recobro se hacen las validaciones d elos campos
		if(forma.getUsuarioCapitadoSeleccionado().isManejaRecobro())
		{
			if(forma.getUsuarioCapitadoSeleccionado().getSelectConvenioEntidadRecobro() == null){
				mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.campoRequerido", "Entidad a Recobrar");
				errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
			}
			else if(forma.getUsuarioCapitadoSeleccionado().getSelectConvenioEntidadRecobro() == ConstantesBD.codigoNuncaValido)
			{
				if(UtilidadTexto.isEmpty(forma.getUsuarioCapitadoSeleccionado().getDescripcionOtraEntidadRecobrar()))
				{
					mensajeConcreto = fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.campoRequerido", "Descripción Entidad a Recobrar");
					errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
				}
			}
		}
		
		return errores;
	}
	
	
	
	

	/**
	 * Carga los datos para mostrar cuando se desea posponer
	 * @param forma
	 * 
	 * @author Cristhian Murillo
	 */
	private void llenarDatosParaPosponer(AutorizacionesIngresoEstanciaForm forma, UsuarioBasico usuarioBasico) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		forma.setEntroPorPosponer(true);
		
		
		forma.getUsuarioCapitadoSeleccionado().setAutorizacionIngresoEstancia(forma.getUsuarioCapitadoSeleccionado().getListaAutorizacionesIngresoEstancia().get(0));
		
		//--> Datos Admisión
		IIngresosEstanciaServicio ingresosEstanciaServicio						= ManejoPacienteServicioFabrica.crearIngresosEstancia();
			IngresosEstancia ingresosEstancia = new IngresosEstancia();
			ingresosEstancia = ingresosEstanciaServicio.findById(forma.getUsuarioCapitadoSeleccionado().getAutorizacionIngresoEstancia().getCodIngresoEstancia());
		
			DtoIngresoEstancia datosAdmision = new DtoIngresoEstancia();
			
			datosAdmision.setFechaAdmision(ingresosEstancia.getFechaAdmision());
			datosAdmision.setHoraAdmision(ingresosEstancia.getHoraAdmision());
			datosAdmision.setViaIngreso(ingresosEstancia.getViasIngreso().getCodigo()+"");
			datosAdmision.setMedicoSolicitante(ingresosEstancia.getMedicoSolicitante());
			datosAdmision.setObservaciones(ingresosEstancia.getObservaciones());
			
			
			//-- Seccion Diagnosticos
			DtoValoracionUrgencias valoracionUrgencias = new DtoValoracionUrgencias();
			valoracionUrgencias.setDiagnosticos(new ArrayList<Diagnostico>());
			
			Diagnostico diagnostico = new Diagnostico();
			diagnostico.setTipoCIE(ingresosEstancia.getDiagnosticosByFkIeDxPpal().getId().getTipoCie());
			diagnostico.setAcronimo(ingresosEstancia.getDiagnosticosByFkIeDxPpal().getId().getAcronimo());
			diagnostico.setNombre(ingresosEstancia.getDiagnosticosByFkIeDxPpal().getNombre());
			valoracionUrgencias.getDiagnosticos().add(0, diagnostico);
			
			
			if(ingresosEstancia.getDiagnosticosByFkIeDxCompli() != null)
			{
				diagnostico = new Diagnostico();
				diagnostico.setTipoCIE(ingresosEstancia.getDiagnosticosByFkIeDxCompli().getId().getTipoCie());
				diagnostico.setAcronimo(ingresosEstancia.getDiagnosticosByFkIeDxCompli().getId().getAcronimo());
				diagnostico.setNombre(ingresosEstancia.getDiagnosticosByFkIeDxCompli().getNombre());
				valoracionUrgencias.getDiagnosticos().add(1, diagnostico);
			}
			else{
				Diagnostico diagnosticoNoSeleccionadoComplicaicion = new Diagnostico(ConstantesBD.acronimoDiagnosticoNoSeleccionado,ConstantesBD.codigoCieDiagnosticoNoSeleccionado);
				valoracionUrgencias.getDiagnosticos().add(1, diagnosticoNoSeleccionadoComplicaicion);
			}
			
			DtoDiagnosticoBusqueda dtoDiagnosticoBusqueda = new DtoDiagnosticoBusqueda();
			dtoDiagnosticoBusqueda.setValoracionUrgencias(valoracionUrgencias);
			
		forma.getUsuarioCapitadoSeleccionado().setDtoDiagnosticoBusqueda(dtoDiagnosticoBusqueda);
		forma.getUsuarioCapitadoSeleccionado().setDatosAdmision(datosAdmision);
		
		
		//--> Datos Autorización
		IAutorizacionIngresoEstanciaServicio autorizacionIngresoEstanciaServicio= ManejoPacienteServicioFabrica.crearAutorizacionIngresoEstanciaServicio();
			AutorizacionesIngreEstancia autorizacionesIngreEstancia = new AutorizacionesIngreEstancia(); 
			autorizacionesIngreEstancia = autorizacionIngresoEstanciaServicio.findById(forma.getUsuarioCapitadoSeleccionado().getAutorizacionIngresoEstancia().getCodigoPk());
			
			forma.getUsuarioCapitadoSeleccionado().setSelectEntidadSubcontratada(ingresosEstancia.getEntidadesSubcontratadas().getCodigoPk());
			if(ingresosEstancia.getEntidadesSubcontratadas().getCodigoPk()  == ConstantesBD.codigoNuncaValidoLong){
				forma.getUsuarioCapitadoSeleccionado().setDescripcionEntidadSubOtra(ingresosEstancia.getDescripcionEntidadSub());
				forma.getUsuarioCapitadoSeleccionado().setDireccionEntidadSubOtra(ingresosEstancia.getDireccionEntidadSub());
				forma.getUsuarioCapitadoSeleccionado().setTelefonoEntidadSubOtra(ingresosEstancia.getTelefonoEntidadSub());
			}else
			{
				for (DtoEntidadSubcontratada dtoEntidadSubcontratada : forma.getListaEntidadesSubcontratadas()) 
				{
					if(dtoEntidadSubcontratada.getCodigoPk() == forma.getUsuarioCapitadoSeleccionado().getSelectEntidadSubcontratada().longValue())
					{
						forma.getUsuarioCapitadoSeleccionado().setDescripcionEntidadSubOtra(dtoEntidadSubcontratada.getRazonSocial());
						forma.getUsuarioCapitadoSeleccionado().setTelefonoEntidadSubOtra(dtoEntidadSubcontratada.getTelefono());
						forma.getUsuarioCapitadoSeleccionado().setDireccionEntidadSubOtra(dtoEntidadSubcontratada.getDireccion());
						break;
					}
				}
			}
			
			DTOAdministracionAutorizacion autorizacionIngresoEstancia = new DTOAdministracionAutorizacion();
			autorizacionIngresoEstancia.setDiasEstanciaAutorizados(autorizacionesIngreEstancia.getDiasEstanciaAutorizados());
			autorizacionIngresoEstancia.setUsuarioContacta(autorizacionesIngreEstancia.getUsuarioContacta());
			autorizacionIngresoEstancia.setCargoUsuarioContacta(autorizacionesIngreEstancia.getCargoUsuContacta());
			autorizacionIngresoEstancia.setObservaciones(autorizacionesIngreEstancia.getObservaciones());
			autorizacionIngresoEstancia.setCodIngresoEstancia(autorizacionesIngreEstancia.getIngresosEstancia().getCodigoPk());
			
				Date fechaVencimiento = UtilidadFecha.conversionFormatoFechaStringDate(
					UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(
							autorizacionesIngreEstancia.getFechaInicioAutorizacion()), autorizacionesIngreEstancia.getDiasEstanciaAutorizados(), false));
			//autorizacionIngresoEstancia.setFechaVencimientoAutorizacion(fechaVencimiento);
			
				Date fechaInicio = UtilidadFecha.conversionFormatoFechaStringDate(
					UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(
							fechaVencimiento), 1, false));
			autorizacionIngresoEstancia.setFechaInicioAutorizacion(fechaInicio);
			
			/**
			 * Se debe guardar en el registro de estancia el usuario, fecha y hora en que se guarda el registro.
			 * DCU 1094 Versi&oacute;n 1.1
			 * @author Diana Ruiz
			 * @since 05/07/2010
			 */
			
			Usuarios usuario = new Usuarios();
			usuario.setLogin(usuarioBasico.getLoginUsuario());
			autorizacionesIngreEstancia.setUsuarios(usuario);		
			autorizacionesIngreEstancia.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaStringDate(UtilidadFecha.getFechaActual()));
			autorizacionesIngreEstancia.setHoraAutorizacion(UtilidadFecha.getHoraActual());	
			
			forma.getUsuarioCapitadoSeleccionado().setManejaRecobro(false);
			if(autorizacionIngresoEstancia.getConvenio().getCodigo() > 0)
			{
				forma.getUsuarioCapitadoSeleccionado().setManejaRecobro(true);
				forma.getUsuarioCapitadoSeleccionado().setSelectConvenioEntidadRecobro(autorizacionIngresoEstancia.getConvenio().getCodigo());
				if(autorizacionIngresoEstancia.getConvenio().getCodigo()  == ConstantesBD.codigoNuncaValido)
				{
					forma.getUsuarioCapitadoSeleccionado().setDescripcionOtraEntidadRecobrar(autorizacionesIngreEstancia.getOtroConvenioRecobro());
				}
			}
		
		forma.getUsuarioCapitadoSeleccionado().setAutorizacionIngresoEstancia(autorizacionIngresoEstancia);
		
		UtilidadTransaccion.getTransaccion().commit();
		
	}
	
	
	/**
	 * M&eacute;todo encargado de generar el formato Versalles
	 * para las autorizaciones generadas de ingreso estancia
	 * @param AutorizacionesIngresoEstanciaForm forma,
			  UsuarioBasico usuarioSesion,
			  HttpServletRequest request
			  
	 * @author Diana Carolina G
	 * 
	 * Nuevo Formato Se modifica el formato Versalles y se renombra a Capitacion (Estandar).
	 * MT 3488
	 * @author Camilo Gomez
	 */
	
	private void generarReporteAutorizacionFormatoCapitacion(AutorizacionesIngresoEstanciaForm forma,
			UsuarioBasico usuarioSesion, HttpServletRequest request) {
		
		
		String nombreReporte="AUTORIZACION INGRESO ESTANCIA";
		String nombreArchivo ="";
		DtoGeneralReporteIngresoEstancia dtoReporte = new DtoGeneralReporteIngresoEstancia();
		GeneradorReporteFormatoCapitacionAutorIngresoEstancia generadorReporte =
			new GeneradorReporteFormatoCapitacionAutorIngresoEstancia(dtoReporte);
		
		
		ArrayList<String> listaNombresReportes 								= new ArrayList<String>();
		DTOReporteAutorizacionSeccionAdmision dtoAdmisionIngresoEstancia 	= new DTOReporteAutorizacionSeccionAdmision();
		DTOReporteAutorizacionSeccionAutorizacion dtoAutorizacion 			= new DTOReporteAutorizacionSeccionAutorizacion();
		DtoEntidadSubcontratada dtoEntidadSubAlmacenada 					= new DtoEntidadSubcontratada();
		EntidadesSubcontratadas entidadSubAlmacenada 						= new EntidadesSubcontratadas();
		ViasIngreso viasIngreso												= new ViasIngreso();	
		
		/** Datos almacenados**/
		DtoUsuariosCapitados datos 						= forma.getUsuarioCapitadoSeleccionado();
		DtoIngresoEstancia datosAdmision 				= forma.getUsuarioCapitadoSeleccionado().getDatosAdmision();
		DTOAdministracionAutorizacion datosAutorizacion	= datos.getAutorizacionIngresoEstancia();
		String nombrePaciente							= "";
		
		
	    InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				 			     			
	    if ((!UtilidadTexto.isEmpty(datos.getSegundoNombre())) && (!UtilidadTexto.isEmpty(datos.getSegundoApellido()))){
	    	nombrePaciente = datos.getPrimerNombre() + " " + 
			datos.getSegundoNombre() + " " + datos.getPrimerApellido()+
			" " + datos.getSegundoApellido();
	    	
	    } else{
	    	nombrePaciente = datos.getPrimerNombre() + " " +
	    	datos.getPrimerApellido();
	    	
	    }
			 			     			
		String infoEncabezado = ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(
				usuarioSesion.getCodigoInstitucionInt());
		
		String infoPiePagina=ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(
				usuarioSesion.getCodigoInstitucionInt());
		
		String reporteMediaCarta = ValoresPorDefecto.getImpresionMediaCarta(
				usuarioSesion.getCodigoInstitucionInt());
		
		if(UtilidadTexto.isEmpty(reporteMediaCarta)){
			reporteMediaCarta=ConstantesBD.acronimoNo;
		}	
		
		UtilidadTransaccion.getTransaccion().begin();
		
		/** Datos entidad subcontratada Otra y Existentes **/
		if(datos.getSelectEntidadSubcontratada() == ConstantesBD.codigoNuncaValidoLong)
		{
			dtoEntidadSubAlmacenada.setRazonSocial(datos.getDescripcionEntidadSubOtra());
			dtoEntidadSubAlmacenada.setTelefono(datos.getTelefonoEntidadSubOtra());
			dtoEntidadSubAlmacenada.setDireccion(datos.getDireccionEntidadSubOtra());
			
		}else{
				IEntidadesSubcontratadasServicio entidadesSubcontratadasServicio 		= FacturacionServicioFabrica.crearEntidadesSubcontratadasServicio();
				long codigoPkEntidad = datos.getSelectEntidadSubcontratada();
				entidadSubAlmacenada = entidadesSubcontratadasServicio.obtenerEntidadesSubcontratadasporId(codigoPkEntidad);
				dtoEntidadSubAlmacenada.setRazonSocial(entidadSubAlmacenada.getRazonSocial());
				dtoEntidadSubAlmacenada.setTelefono(entidadSubAlmacenada.getTelefono());
				dtoEntidadSubAlmacenada.setDireccion(entidadSubAlmacenada.getDireccion());
		}
		
		/** Datos via de ingreso seleccionada **/
		IViasIngresoServicio viasIngresoServicio								= ManejoPacienteServicioFabrica.crearViasIngresoServicio();
		int codigoViaIngreso = new Integer(datosAdmision.getViaIngreso());
		viasIngreso = viasIngresoServicio.findbyId(codigoViaIngreso);
		
		UtilidadTransaccion.getTransaccion().commit();
		
		/** Datos secci&oacute;n paciente**/
		dtoReporte.setNombrePaciente(nombrePaciente);
		dtoReporte.setTipoDocPaciente(datos.getTipoIdentificacion());
		dtoReporte.setNumeroDocPaciente(datos.getNumeroIdentificacion());
		
		int edadPaciente= UtilidadFecha.calcularEdad(UtilidadFecha.conversionFormatoFechaAAp(datos.getFechaNacimiento()));
		dtoReporte.setEdadPaciente(String.valueOf(edadPaciente)+" Años");
		dtoReporte.setTipoContratoPaciente(datos.getNombreTipoContrato());	
		dtoReporte.setConvenioPaciente(datos.getNombreConvenio());
		dtoReporte.setCategoriaSocioEconomica(datos.getDescripcionEstratoSocial());
		dtoReporte.setTipoAfiliado(datos.getNombreTipoAfiliado());
		
		/** Datos entidad recobro**/
		if(datos.isManejaRecobro()){
			dtoReporte.setRecobro(ConstantesBD.acronimoSi);
			dtoReporte.setEntidadRecobro(datos.getDescripcionOtraEntidadRecobrar());
			
		} else{
			dtoReporte.setRecobro(ConstantesBD.acronimoNo);
			
		}
		
		String nombreDiagnosticoPrincipal = datos.getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(0).getAcronimo() + " " + 
		datos.getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(0).getTipoCIE() + " " +
		datos.getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(0).getNombre();
		//MT 5402 validacion diagnostico complicación no se encuentre vacio
		String nombreDiagnosticoComplicacion="";
		if(datos.getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(1).getAcronimo() != null
				&& !datos.getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(1).getAcronimo().isEmpty()
				&& datos.getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(1).getTipoCIE() > 0
				&&datos.getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(1).getNombre() != null
						&& !datos.getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(1).getNombre().isEmpty()){
			nombreDiagnosticoComplicacion = datos.getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(1).getAcronimo() + " " + 
			datos.getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(1).getTipoCIE() + " " +
			datos.getDtoDiagnosticoBusqueda().getValoracionUrgencias().getDiagnosticos().get(1).getNombre();
		}
		dtoAdmisionIngresoEstancia.setViaIngresoAdmision(datosAdmision.getViaIngreso());
		
		/** Datos secci&oacute;n admisi&oacute;n **/
		dtoAdmisionIngresoEstancia.setFechaAdmision(UtilidadFecha.conversionFormatoFechaAAp(datosAdmision.getFechaAdmision()));
		dtoAdmisionIngresoEstancia.setHoraAdmision(datosAdmision.getHoraAdmision());
		dtoAdmisionIngresoEstancia.setViaIngresoAdmision(viasIngreso.getNombre());
		dtoAdmisionIngresoEstancia.setDxPrincipalAdmision(nombreDiagnosticoPrincipal);
		dtoAdmisionIngresoEstancia.setDxComplicacionAdmision(nombreDiagnosticoComplicacion);
		dtoAdmisionIngresoEstancia.setMedicoSolicitanteAdmision(datosAdmision.getMedicoSolicitante());
		dtoAdmisionIngresoEstancia.setObservacionesAdmision(datosAdmision.getObservaciones());
				
		/** Datos secci&oacute;n autorizaci&oacute;n **/
		dtoAutorizacion.setEntidadSub(dtoEntidadSubAlmacenada.getRazonSocial());
		dtoAutorizacion.setNumeroAutorizacion(String.valueOf(forma.getNumeroAutorizacion()));
		dtoAutorizacion.setDireccionEntidadSub(dtoEntidadSubAlmacenada.getDireccion());
		dtoAutorizacion.setTelefonoEntidadSub(dtoEntidadSubAlmacenada.getTelefono());
		dtoAutorizacion.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp(datosAutorizacion.getFechaInicioAutorizacion()));
		dtoAutorizacion.setDiasEstanciaAutorizados(datosAutorizacion.getDiasEstanciaAutorizados());
		dtoAutorizacion.setFechaInicioAutorizacion(UtilidadFecha.conversionFormatoFechaAAp(datosAutorizacion.getFechaInicioAutorizacion()));
		dtoAutorizacion.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaAAp(datosAutorizacion.getFechaVencimientoAutorizacion()));
		dtoAutorizacion.setEstadoAutorizacion((String)ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoAutorizado));	 
		dtoAutorizacion.setEntidadAutoriza(usuarioSesion.getInstitucion());
		dtoAutorizacion.setUsuarioAutoriza(usuarioSesion.getLoginUsuario());
		dtoAutorizacion.setObservaciones(datosAutorizacion.getObservaciones());
		
		dtoReporte.setTipoReporteMediaCarta(reporteMediaCarta);
		dtoReporte.setDatosEncabezado(infoEncabezado);
		dtoReporte.setRazonSocial(institucion.getRazonSocial());
		dtoReporte.setNit("NIT - "+institucion.getNit());
		dtoReporte.setDireccion("Dir:  "+institucion.getDireccion() +"  -  Tel: "+institucion.getTelefono());
		dtoReporte.setActividadEconomica(institucion.getActividadEconomica());
		dtoReporte.setUsuario(usuarioSesion.getNombreUsuario()+" ("+usuarioSesion.getLoginUsuario()+")");
		dtoReporte.setDatosPie(infoPiePagina);
		dtoReporte.setRutaLogo(institucion.getLogoJsp());
		dtoReporte.setUbicacionLogo(institucion.getUbicacionLogo());
		dtoReporte.setDtoAdmisionIngresoEstancia(dtoAdmisionIngresoEstancia);
		dtoReporte.setDtoAutorizacion(dtoAutorizacion);
		
		JasperPrint reporte = generadorReporte.generarReporte();
		nombreArchivo = generadorReporte.exportarReportePDF(reporte, nombreReporte);
		
		//JasperViewer.viewReport(reporte, false);	
		listaNombresReportes.add(nombreArchivo);
		forma.setNombreArchivoGenerado(nombreArchivo);
		
		if(listaNombresReportes!=null && listaNombresReportes.size()>0){
			forma.setListaNombresReportes(listaNombresReportes);
			forma.setMostrarImprimirAutorizacion(true);
		} 

	} 

	/**
	 * Metodo que evalua si se encuentra definido el parametro del formato
	 * de Impresion Autorizacion Capitacion Subcontratada
	 * MT 3488
	 * 
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuarioSesion
	 * @author Camilo Gómez
	 */
	private void validarFormatoImpresion(ActionMapping mapping, AutorizacionesIngresoEstanciaForm forma,HttpServletRequest request, UsuarioBasico usuarioSesion)
	{
		ActionMessages errores	=new ActionMessages();
		
		String tipoFormatoImpresion = ValoresPorDefecto.getFormatoImpresionAutorEntidadSub(
					usuarioSesion.getCodigoInstitucionInt()); 
			
		if(UtilidadTexto.isEmpty(tipoFormatoImpresion))
		{
			errores.add("Falta definir Formato Impresion", new ActionMessage("errors.notEspecific", 
					fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.mensajeFaltaDefinirFormato")));
			saveErrors(request, errores);
		}else{
			//Nuevo Formato Se modifica el formato Versalles y se renombra a Capitacion (estandar).
			if(tipoFormatoImpresion.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionEstandar))
			{
				generarReporteAutorizacionFormatoCapitacion(forma, usuarioSesion, request);
				forma.setFormatoImpresionDefinido(true);
			}else{
				errores.add("Falta definir Formato Impresion", new ActionMessage("errors.notEspecific", 
						fuenteMensaje.getMessage("AutorizacionesIngresoEstanciaForm.mensajeFaltaDefinirFormato")));
				saveErrors(request, errores);
			}
		}
	}
	
	/**
	 * Ruta del botón volver de acuerdo a desde donde haya sido iniciada la funcionlidad
	 * 
	 * @param forma
	 * @param response
	 * @param request
	 * @param mapping
	 * 
	 * @return ActionForward
	 * 
	 * @autor Ricardo Ruiz
	 */
	private ActionForward accionVolver(AutorizacionesIngresoEstanciaForm forma, HttpServletResponse response, HttpServletRequest request, ActionMapping mapping)
	{
		try {
			String ruta=request.getContextPath()+"/autorizacionesCapitacionSubcontratada/autorizacionesCapitacionSubcontratada.jsp";
			if(forma.getIntegracion() != null && forma.getIntegracion().trim().equals(ConstantesCapitacion.INTEGRACION_ORDENES_PACIENTE)){
				ruta=request.getContextPath()+"/autorizacionesCapitacionSubcontratada/autorizacionesCapitacionSubcontratada.do"+"?estado=initOrdenesPorPaciente";
			}
			response.sendRedirect(ruta);
			forma.setIntegracion("");
			return null;
		} 
		catch (IOException e) 
		{
			Log4JManager.error("Error haciendo la redirección ",e);
			ActionMessages errores=new ActionMessages();
			errores.add("llamado_funcionalidad",new ActionMessage("errors.notEspecific", "No se pudo llamar a la funcionalidad de Volver"));
			saveErrors(request, errores);
			return mapping.findForward("paginaError");
		}
	}
	
}
