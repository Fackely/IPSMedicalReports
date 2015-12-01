package com.princetonsa.action.manejoPaciente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperPrint;

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
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.ConsultaAutorizacionesCapitacionSubDetalleForm;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorEntidadSubcontratadaCapitacion;
import com.princetonsa.dto.manejoPaciente.DTOAutorizacionCapitacionSubcontratada;
import com.princetonsa.dto.manejoPaciente.DTOAutorizacionIngresoEstancia;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionAdmision;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionAutorizacion;
import com.princetonsa.dto.manejoPaciente.DTOReporteAutorizacionSeccionPaciente;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteArticulosAutorizados;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteIngresoEstancia;
import com.princetonsa.dto.manejoPaciente.DtoGeneralReporteServiciosAutorizados;
import com.princetonsa.dto.manejoPaciente.DtoIngresoEstancia;
import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.dto.manejoPaciente.ArticuloAutorizadoCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionEntregaDto;
import com.servinte.axioma.dto.manejoPaciente.ServicioAutorizadoCapitacionDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionIngresoEstancia.GeneradorReporteFormatoCapitacionAutorIngresoEstancia;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionMedicamentosInsumos.GeneradorReporteFormatoCapitacionAutorArticulos;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionServicios.GeneradorReporteFormatoCapitacionAutorservicio;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.capitacion.CapitacionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IHistoricoCapitacionSubcontratadaServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.IHistoricoIngEstanciaSubcontratadaServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionIngresoEstanciaServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntidadesSubServicio;

/**
 * Esta clase se encarga de de procesar las solicitudes de 
 * la administración de las autorizaciones de capitación por paciente
 * cargado en sesión
 * 
 * @author 
 * @since
 */
public class ConsultaAutorizacionesCapitacionSubDetalleAction extends Action {	
	
	private static final String NOMBRE_ENTIDAD_OTRA = "Otra";
	private final String separador= " - ";
	
	private static final String FORWARD_POPUP_PRIMERA_IMPRESION = "mostrarPopUpPrimeraImpresion";
	private static final String FORWARD_DET_AUTORIZACION_CAPITACION = "detalleAutorizacionCapitacion";
	private static final String FORWARD_DET_AUTORIZACION_INGESTANCIA = "detalleAutorizacionIngEstancia";
	private static final String PROPERTIES_DETALLE_CONSULTA = "com.servinte.mensajes.manejoPaciente.ConsultaAutorizacionesCapitacionSubDetalleForm";
	private static final String KEY_ERROR_NO_ESPECIFIC="errors.notEspecific";
	private static final String ESTADO_ANULADO = "Anulada(o)";
	private static final String KEY_ERROR_PARAM_GRAL_MANEJOPACIENTE = "errores.manejoPaciente.parametroGeneral";
	
	/**
	 * 
	 * Este Método se encarga de ejecutar las acciones sobre las páginas
	 * de Consulta de las autorizaciones de capitación por paciente
	 * 
	 * 
	 * @param  ActionMapping mapping, HttpServletRequest request,
	 *         HttpServletResponse response
	 * @return ActionForward     
	 * @author 
	 *
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request,HttpServletResponse response){


		//Usuario cargado en session
		Connection con = null;
		ActionForward forward=null;
		try{
			InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");

			if (form instanceof ConsultaAutorizacionesCapitacionSubDetalleForm) {

				ConsultaAutorizacionesCapitacionSubDetalleForm forma = (ConsultaAutorizacionesCapitacionSubDetalleForm)form;
				String estado = forma.getEstado();
				Log4JManager.info("estado " + estado);
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");



				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","erros.problemasBd");
					return mapping.findForward("paginaError");
				}
				UtilidadTransaccion.getTransaccion().begin();

				forma.setMostrarPopupPrimeraImpresion(false);
				
				try{
					if(estado.equals("detallePaciente")){

						forma.setConsultaPorRango(false);			
						forma.setPaginaRedireccion("consultaAutorizacionesCapitacionSubPorPaciente.do?estado=consultarPorPaciente");
						forward= empezar(con, request,forma, mapping,paciente,usuario);

					}else if(estado.equals("detalleRango")){

						forma.setConsultaPorRango(true);
						forma.setPaginaRedireccion("consultaAutorizacionesCapitacionSubPorRango.do?estado=consultar");					
						forward= empezar(con, request,forma, mapping,paciente,usuario);			

					}else if(estado.equals("imprimir")){

						forward = validarImpresionAutorizacion(request, forma, mapping, paciente, usuario, con, institucionBasica);

					}else if(estado.equals("imprimirIngresoEstancia")){

						forward = validarImpresionAutorizacion(request, forma, mapping, paciente, usuario, con, institucionBasica);				

					}else if(estado.equals("popUpOtrasAutorizaciones")){
						forma.setAbrirPopUpOtrasAutorizaciones(ConstantesBD.acronimoSi);
						forward= mapping.findForward("detalleAutorizacionIngEstancia");				
					}
					else if(estado.equals("mostrarPopUp")){
						forma.setPaginaRedireccion("consultaAutorizacionesCapitacionSubDetalleIngEstancia.jsp");
						forward= mapping.findForward("popUpOtrasAutorizaciones");
					}				
					//hermorhu - MT5966
					else if(estado.equals("mostrarPopUpPrimeraImpresion")){
						forward = accionCargarPopupEntregaPrimeraImpresion(con, request, forma, mapping, usuario);
					}
					else if(estado.equals("guardarDatosPrimeraImpresion")){
						forward = accionGuardarDatosPrimeraImpresion(con, request, forma, mapping, paciente, usuario, institucionBasica);
					}
					else if(estado.equals("cancelarPopUpPrimeraImpresion")){
						forma.getDtoAutorizacionCapitacion().setAutorizacionEntrega(null);
						forward = mapping.findForward(FORWARD_DET_AUTORIZACION_CAPITACION);
					}				

					UtilidadTransaccion.getTransaccion().commit();
				}catch (Exception e) {
					UtilidadTransaccion.getTransaccion().rollback();
					Log4JManager.error("Error en la consulta de autorizaciones para paciente cargado en sesión", e);
				}		
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return forward;
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param institucion
	 * @param mapping
	 * @param request
	 */	
	private void imprimirAutorizacion(Connection con,ConsultaAutorizacionesCapitacionSubDetalleForm forma,UsuarioBasico usuario,PersonaBasica paciente,InstitucionBasica institucion, ActionMapping mapping,HttpServletRequest request)
	{	
		String formato=ValoresPorDefecto.getFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
		
			if(!formato.trim().isEmpty()&&formato.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionEstandar)){
				
				if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SERVICIOS)
						||forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SERVICIOS_ING_EST))
				{				
					imprimirAutorizacionesEntidadesSubServicio(forma, usuario,institucion);
				}
				else if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_ARTICULOS)
						||forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_ARTICULOS_ING_EST))
				{
					imprimirAutorizacionesEntidadesSubArticulos(forma, usuario, institucion);
				}else{
					if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_INGRESO_ESTANCIA)){
						generarReporteAutorizacionFormatoIngEstancia(forma, usuario, request, paciente,institucion);
					}
				}
			}
		}
	
	/**
	 * 
	 * Este Método se encarga de consultar el detalle de una autorización de capitación o
	 * de ingreso estancia
	 * 
	 * @param AdministracionAutorizacionCapitacionDetalleForm forma, ActionMapping mapping
	 * @return ActionForward
	 * @author, 
	 * @throws IPSException 
	 * @throws SQLException 
	 *
	 */	
	public ActionForward empezar(Connection con, HttpServletRequest request,
			ConsultaAutorizacionesCapitacionSubDetalleForm forma,
			ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario ) throws IPSException, SQLException{
		
		ActionMessages errores=new ActionMessages();
		forma.resetDatosReporte();
		IAutorizacionIngresoEstanciaServicio autorizacionIngEstServicio =
			ManejoPacienteServicioFabrica.crearAutorizacionIngresoEstanciaServicio();
				
		DTOAutorizacionIngresoEstancia dtoAutorIngEstancia = null;
		DTOAutorEntidadSubcontratadaCapitacion dtoAutorEntSub=null;
				
		ManejoPacienteFacade manejoPacienteFacade = null;
				
		if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_INGRESO_ESTANCIA))
		{
			/*Consulta el detalle de Ingreso Estancia por ID de la autorizacion*/
			dtoAutorIngEstancia = new DTOAutorizacionIngresoEstancia();
			dtoAutorIngEstancia.setCodigoPk(forma.getCodigoAutorizacion());
			
			dtoAutorIngEstancia =  autorizacionIngEstServicio.consultarAutorizacionPorID(dtoAutorIngEstancia);			
			forma.setDtoAutorizacionIngEstancia(dtoAutorIngEstancia);
			
			if(dtoAutorIngEstancia!=null)
			{
				//Subir Paciente en session
				if(dtoAutorIngEstancia.getDtoIngresoEstancia() != null 
						&& dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente() != null){
					paciente.setCodigoPersona(dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().getCodigo());
					UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
				}
				/*Consulta las autorizaciones historicas de Ingreso estancia*/
				IHistoricoIngEstanciaSubcontratadaServicio historicoIngEstanciaServicio=
					CapitacionFabricaServicio.crearHistoricoIngEstanciaSubServicio();
				
				dtoAutorIngEstancia.setCodigoPk(forma.getCodigoAutorizacion());
				
				ArrayList<DTOAutorizacionIngresoEstancia> dtoHistoricoIngEstancia=
					historicoIngEstanciaServicio.obtenerHistoricoAutorizEntSubIngEstanciaPorID(dtoAutorIngEstancia);
				
				ArrayList<DTOAutorizacionIngresoEstancia> accionModifica=new ArrayList<DTOAutorizacionIngresoEstancia>();
				ArrayList<DTOAutorizacionIngresoEstancia> accionTemporal=new ArrayList<DTOAutorizacionIngresoEstancia>();
				ArrayList<DTOAutorizacionIngresoEstancia> accionAnulada=new ArrayList<DTOAutorizacionIngresoEstancia>();
				/*
				 * Almacenan los datos de la autorización temporal cuando se creó ingreso estancia
				 */
				Date fechaAutoTemporal=new Date();
				String horaAutoTemporal="";
				String usuarioAutoTemporal="";
				//------------------------------------------------
				
				if(!Utilidades.isEmpty(dtoHistoricoIngEstancia))
				{
					for (DTOAutorizacionIngresoEstancia histoIngEstancia : dtoHistoricoIngEstancia) 
					{					
						if(histoIngEstancia.getTrazabilidadAutorizacionIngEstancia().getAccionRealizada().equals(ConstantesIntegridadDominio.acronimoAccionHistoricaModificar)){
							accionModifica.add(histoIngEstancia);
						}
						
						if(histoIngEstancia.getTrazabilidadAutorizacionIngEstancia().getAccionRealizada().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado)){
							accionAnulada.add(histoIngEstancia);
						}
						
						if(histoIngEstancia.getTrazabilidadAutorizacionIngEstancia().getAccionRealizada().equals(ConstantesIntegridadDominio.acronimoAccionEliminarTemporal))
						{
							histoIngEstancia.getTrazabilidadAutorizacionIngEstancia().setFechaModifica(fechaAutoTemporal);
							histoIngEstancia.getTrazabilidadAutorizacionIngEstancia().setHoraModifica(horaAutoTemporal);
							histoIngEstancia.getTrazabilidadAutorizacionIngEstancia().setUsuarioModifica(usuarioAutoTemporal);
							accionTemporal.add(histoIngEstancia);
						}
						if(histoIngEstancia.getTrazabilidadAutorizacionIngEstancia().getAccionRealizada().equals(ConstantesIntegridadDominio.acronimoAccionHistoricaInsertar)){
							forma.setPrimeraFechaVencimiento(histoIngEstancia.getTrazabilidadAutorizacionIngEstancia().getFechaVencimiento());
							fechaAutoTemporal=histoIngEstancia.getTrazabilidadAutorizacionIngEstancia().getFechaModifica();
							horaAutoTemporal=histoIngEstancia.getTrazabilidadAutorizacionIngEstancia().getHoraModifica();
							usuarioAutoTemporal=histoIngEstancia.getTrazabilidadAutorizacionIngEstancia().getUsuarioModifica();
						}
					}		
					forma.setListaModificaIngEstancia(accionModifica);
					forma.setListaAnuladaIngEstancia(accionAnulada);
					forma.setListaTemporalIngEstancia(accionTemporal);
					forma.setMostrarTituloTrazabilidad(false);
					if(!accionModifica.isEmpty() || !accionAnulada.isEmpty() || !accionTemporal.isEmpty()){
						forma.setMostrarTituloTrazabilidad(true);
					}
				}
/*--------------Consulta Otras Autorizaciones Asociadas a la Autorizacion de Ingreso Estancia----------------------------*/
				paciente=cargarDatosPacienteNoCargadoEnSesion(forma, paciente);
				DTOAdministracionAutorizacion parametros = new DTOAdministracionAutorizacion();	
					parametros.setAdministracionPoblacionCapitada(false);
				DtoPaciente dtoPaciente = new DtoPaciente();
					dtoPaciente.setCodigo(paciente.getCodigoPersona());
				parametros.setPaciente(dtoPaciente);
				parametros.setOrdenarDescendente(true);
				
				IAutorizacionIngresoEstanciaServicio autorizacionIngresoEstanciaServicio= ManejoPacienteServicioFabrica.crearAutorizacionIngresoEstanciaServicio();
				
				ArrayList<DTOAdministracionAutorizacion> listaAutorizacionesIngresoEstancia = new ArrayList<DTOAdministracionAutorizacion>();
				listaAutorizacionesIngresoEstancia.addAll(autorizacionIngresoEstanciaServicio.obtenerAutorizacionesPorPaciente(parametros));
				
				if(Utilidades.isEmpty(listaAutorizacionesIngresoEstancia)){
					forma.setTieneOtrasAutorizaciones(ConstantesBD.acronimoNo);
					forma.setAbrirPopUpOtrasAutorizaciones(ConstantesBD.acronimoNo);
				}
				else
				{	
					forma.setUsuarioCapitadoSeleccionado(new DtoUsuariosCapitados());
					forma.setTieneOtrasAutorizaciones(ConstantesBD.acronimoSi);
					forma.setAbrirPopUpOtrasAutorizaciones(ConstantesBD.acronimoNo);
					
					//Se agrega la lista de autorizaciones al registro seleccionado para mostrar el histórico
					forma.getUsuarioCapitadoSeleccionado().setListaAutorizacionesIngresoEstancia(listaAutorizacionesIngresoEstancia);
					
					String diasVigentesNuevaAutoEstancia = ValoresPorDefecto.getDiasVigentesNuevaAutorizacionEstanciaSerArt(usuario.getCodigoInstitucionInt());
					
					// Se toma el último registro de Autorización para hacer las valiadciones
					DTOAdministracionAutorizacion ultimaAutorizacionIngEstancia = new DTOAdministracionAutorizacion();
					ultimaAutorizacionIngEstancia = listaAutorizacionesIngresoEstancia.get(0);
					
					// Se asigna el ingreso instancia con el que se va a trabajar en caso de que se desee extender la autorización
					forma.getUsuarioCapitadoSeleccionado().setAutorizacionIngresoEstancia(ultimaAutorizacionIngEstancia);
					
					Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
									
						if(!UtilidadTexto.isEmpty(diasVigentesNuevaAutoEstancia))
						{							
							// Fecha Vigencia Autorización = Fecha Vencimiento Autorización + Días Vigentes para solicitar nueva autorización estancia
							Date fechaVigenciaAutorizacion = UtilidadFecha.conversionFormatoFechaStringDate(
									UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(
											ultimaAutorizacionIngEstancia.getFechaVencimientoAutorizacion()), Integer.parseInt(diasVigentesNuevaAutoEstancia), false));
														
							forma.getUsuarioCapitadoSeleccionado().getAutorizacionIngresoEstancia().setFechaVencimientoAutorizacion(fechaVigenciaAutorizacion);
							
							if( (fechaVigenciaAutorizacion.before(fechaActual))  || (fechaVigenciaAutorizacion.equals(fechaActual)) )
							{
								// Se asinga la nueva fecha de vencimiento a la autorización ingreso estancia a extender
								forma.getUsuarioCapitadoSeleccionado().getAutorizacionIngresoEstancia().setFechaVencimientoAutorizacion(fechaVigenciaAutorizacion);								
							}							
						}					
				}					
/*---------------------------------------------------------------------------------------------------------------*/
				Date fechaVencimientoAutorizacion =  UtilidadFecha.conversionFormatoFechaStringDate(
						UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(dtoAutorIngEstancia.getFechaAutorizacion()),
								dtoAutorIngEstancia.getDiasEstanciaAutorizados(), false));
				
				String tipoNroID = dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().getTipoIdentificacion() + " "+
				dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().getNumeroIdentificacion();
				
				dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().setNumeroIdentificacion(tipoNroID);
				
				String nombrePaciente = dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().getPrimerNombre() + " "+
					(UtilidadTexto.isEmpty(dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().getSegundoNombre())?"":
						dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().getSegundoNombre()) + " " +
						dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().getPrimerApellido() + " " +
						(UtilidadTexto.isEmpty(dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().getSegundoApellido())?"":
							dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().getSegundoApellido());				
				
				String nombreUsuarioAutoriza = dtoAutorIngEstancia.getUsuarioAutoriza().getNombre() + " " +
												dtoAutorIngEstancia.getUsuarioAutoriza().getApellido();				
				dtoAutorIngEstancia.getUsuarioAutoriza().setNombre(nombreUsuarioAutoriza);
				dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().setPrimerNombre(nombrePaciente);
				
				dtoAutorIngEstancia.setFechaVencimiento(fechaVencimientoAutorizacion);
				dtoAutorIngEstancia.setCodigoPk(forma.getCodigoAutorizacion());
							
				//Se comenta ya que se repite la fecha admisión MT 3347
				/*String fechaHoraAdmision=UtilidadFecha.conversionFormatoFechaAAp(dtoAutorIngEstancia.getDtoIngresoEstancia().getFechaAdmision())+"  -  "+
											dtoAutorIngEstancia.getDtoIngresoEstancia().getHoraAdmision();
				dtoAutorIngEstancia.getDtoIngresoEstancia().setHoraAdmision(fechaHoraAdmision);*/
				
				if(!UtilidadTexto.isEmpty(dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().getFechaNacimiento())){
					
					/**La edad del paciente se calcula sobre la fecha de la autorización no sobre la fecha actual 
					 * DCU 1104 V1.3 - Diana Ruiz*/
										
					String fechaAutorizacion= UtilidadFecha.conversionFormatoFechaAAp(dtoAutorIngEstancia.getFechaAutorizacion());
					String edadPaciente=UtilidadFecha.calcularEdadDetallada(dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().getFechaNacimiento(), fechaAutorizacion);
					
					dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().setEdadPacienteCapitado(edadPaciente);
					
					//dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().setEdadPaciente(
						//	UtilidadFecha.calcularEdad(dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().getFechaNacimiento()));					
				}
				
				if(!UtilidadTexto.isEmpty(dtoAutorIngEstancia.getEstado())){
					String estado=(String)ValoresPorDefecto.getIntegridadDominio(dtoAutorIngEstancia.getEstado());
					dtoAutorIngEstancia.setEstado(estado);
				}
				
				forma.setDiasEstanciaActualizados(dtoAutorIngEstancia.getDiasEstanciaAutorizados());
				
				forma.setDtoAutorizacionIngEstancia(new DTOAutorizacionIngresoEstancia());
				forma.setDtoAutorizacionIngEstancia(dtoAutorIngEstancia);
			}else{
				
				return ComunAction.accionSalirCasoError(mapping, request, null, null, 
						"No se encontro Detalle Integridad Datos", "errors.detalleAutorizacion.noAutorizacion", true);
			}
			
			return mapping.findForward("detalleAutorizacionIngEstancia");	
			
		}else{
			manejoPacienteFacade = new ManejoPacienteFacade();
			AutorizacionEntregaDto autorizacionEntregaDto = null;
			
			dtoAutorEntSub = new DTOAutorEntidadSubcontratadaCapitacion();
			dtoAutorEntSub.setAutorCapitacion(new DTOAutorizacionCapitacionSubcontratada());
			dtoAutorEntSub.getAutorCapitacion().setCodigoPK(forma.getCodigoAutorizacion());
			dtoAutorEntSub.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaStringDate(forma.getFechaAutorizacion()));
			IAutorizacionesEntidadesSubServicio autorizacionEntSubServicio =
				ManejoPacienteServicioFabrica.crearAutorizacionEntidadesSubServicio();	
			if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SERVICIOS_ING_EST)
					|| forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_ARTICULOS_ING_EST))
			{
				dtoAutorEntSub = autorizacionEntSubServicio.obtenerAutorizacionEntidadSubCapitacionIngEstanciaPorID(dtoAutorEntSub);
								
			}else{
				dtoAutorEntSub = autorizacionEntSubServicio.obtenerAutorizacionEntidadSubCapitacionPorID(dtoAutorEntSub);
			}
			
			if(dtoAutorEntSub==null){
				return ComunAction.accionSalirCasoError(mapping, request, null, null, 
						"No se encontro Detalle Integridad Datos", "errors.detalleAutorizacion.noAutorizacion", true);
			}
			
			//hermorhu - MT5966
			//Si existe 'Autorizacion de Entidad Subcontratada' se debe validar que existan los datos de entrega de autorizacion
			if(dtoAutorEntSub.getConsecutivoAutorizacion() != null	&& (!dtoAutorEntSub.getConsecutivoAutorizacion().isEmpty() && !dtoAutorEntSub.getConsecutivoAutorizacion().equals(String.valueOf(ConstantesBD.codigoNuncaValido)))) {
				try {
					autorizacionEntregaDto = manejoPacienteFacade.consultarEntregaAutorizacionEntidadSubContratada(dtoAutorEntSub.getAutorizacion());
					dtoAutorEntSub.setAutorizacionEntrega(autorizacionEntregaDto);
				} catch (IPSException e) {
					Log4JManager.error(e.getMessage(),e);
				}
			}
			
			//Subir Paciente en session
			if(dtoAutorEntSub.getDtoPaciente() != null){
				paciente.setCodigoPersona(dtoAutorEntSub.getDtoPaciente().getCodigo());
				UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
			}
			/*Consulta las autorizaciones historicas de Capitacion Subcontratada*/
			IHistoricoCapitacionSubcontratadaServicio historicoCapiSubServicio =
				CapitacionFabricaServicio.crearHistoricoCapitacionSubServicio();
			
			dtoAutorEntSub.getAutorCapitacion().setCodigoPK(forma.getCodigoAutorizacion());
			
			ArrayList<DTOAutorEntidadSubcontratadaCapitacion> dtoHistorico=
				historicoCapiSubServicio.obtenerHistoricoAutorizacionEntidadSubCapitacionPorID(dtoAutorEntSub);
		
			ArrayList<DTOAutorEntidadSubcontratadaCapitacion> accionProrroga=new ArrayList<DTOAutorEntidadSubcontratadaCapitacion>();
			ArrayList<DTOAutorEntidadSubcontratadaCapitacion> accionTemporal=new ArrayList<DTOAutorEntidadSubcontratadaCapitacion>();
			ArrayList<DTOAutorEntidadSubcontratadaCapitacion> accionAnulada=new ArrayList<DTOAutorEntidadSubcontratadaCapitacion>();
			
			DTOAutorEntidadSubcontratadaCapitacion dtoAutorHistorico=null; 
			int tamano=dtoHistorico.size();
			
			if(!Utilidades.isEmpty(dtoHistorico))
			{	for (int i=0;i<dtoHistorico.size();i++)
				{						
					if(dtoHistorico.get(i).getTrazabilidadAutorizacion().getAccionRealizada().equals(ConstantesIntegridadDominio.acronimoAccionHistoricaInsertar))
					{
						dtoAutorHistorico= new DTOAutorEntidadSubcontratadaCapitacion();
						dtoAutorHistorico.getTrazabilidadAutorizacion().setFechaVencimiento(dtoHistorico.get(i).getTrazabilidadAutorizacion().getFechaVencimiento());
						dtoAutorHistorico.getTrazabilidadAutorizacion().setFechaModifica(dtoHistorico.get(i).getTrazabilidadAutorizacion().getFechaModifica());
						dtoAutorHistorico.getTrazabilidadAutorizacion().setHoraModifica(dtoHistorico.get(i).getTrazabilidadAutorizacion().getHoraModifica());
						dtoAutorHistorico.getTrazabilidadAutorizacion().setUsuarioModifica(dtoHistorico.get(i).getTrazabilidadAutorizacion().getUsuarioModifica());
					}
					else
					if(dtoHistorico.get(i).getTrazabilidadAutorizacion().getAccionRealizada().equals(ConstantesIntegridadDominio.acronimoAccionProrrogar))
					{
						if(dtoAutorHistorico!=null)
						{
							if(dtoAutorHistorico.getTrazabilidadAutorizacion().getFechaVencimiento()!=null)
							{
								dtoAutorHistorico.getTrazabilidadAutorizacion().setNuevaFechaVencimiento(dtoHistorico.get(i).getTrazabilidadAutorizacion().getFechaVencimiento());
								accionProrroga.add(dtoAutorHistorico);
								dtoAutorHistorico= new DTOAutorEntidadSubcontratadaCapitacion();
								dtoAutorHistorico.getTrazabilidadAutorizacion().setFechaVencimiento(dtoHistorico.get(i).getTrazabilidadAutorizacion().getFechaVencimiento());
								dtoAutorHistorico.getTrazabilidadAutorizacion().setFechaModifica(dtoHistorico.get(i).getTrazabilidadAutorizacion().getFechaModifica());
								dtoAutorHistorico.getTrazabilidadAutorizacion().setHoraModifica(dtoHistorico.get(i).getTrazabilidadAutorizacion().getHoraModifica());
								dtoAutorHistorico.getTrazabilidadAutorizacion().setUsuarioModifica(dtoHistorico.get(i).getTrazabilidadAutorizacion().getUsuarioModifica());
							}
						}
					}
					else
					if(dtoHistorico.get(i).getTrazabilidadAutorizacion().getAccionRealizada().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado)){
						accionAnulada.add(dtoHistorico.get(i));
					}else{
						if(dtoHistorico.get(i).getTrazabilidadAutorizacion().getAccionRealizada().equals(ConstantesIntegridadDominio.acronimoAccionEliminarTemporal)){
							accionTemporal.add(dtoHistorico.get(i));	
						}
					}
					
					
				}	
				
				//guardar el ultimo registro de prorroga
				if(accionProrroga.size()>0)
				{
					dtoAutorHistorico.getTrazabilidadAutorizacion().setFechaModifica(dtoHistorico.get(tamano-1).getTrazabilidadAutorizacion().getFechaModifica());
					dtoAutorHistorico.getTrazabilidadAutorizacion().setHoraModifica(dtoHistorico.get(tamano-1).getTrazabilidadAutorizacion().getHoraModifica());
					dtoAutorHistorico.getTrazabilidadAutorizacion().setUsuarioModifica(dtoHistorico.get(tamano-1).getTrazabilidadAutorizacion().getUsuarioModifica());
					dtoAutorHistorico.getTrazabilidadAutorizacion().setNuevaFechaVencimiento(dtoAutorEntSub.getFechaVencimiento());
					accionProrroga.add(dtoAutorHistorico);
				}
				
				forma.setListaProrroga(accionProrroga);
				forma.setListaAnulada(accionAnulada);
				forma.setListaTemporal(accionTemporal);
				forma.setMostrarTituloTrazabilidad(false);
				if(!accionProrroga.isEmpty() || !accionAnulada.isEmpty() || !accionTemporal.isEmpty()){
					forma.setMostrarTituloTrazabilidad(true);
				}
			}				
			
			if(dtoAutorEntSub!=null)
			{				
				String tipoNroID =dtoAutorEntSub.getDtoPaciente().getTipoIdentificacion() + " " +
				dtoAutorEntSub.getDtoPaciente().getNumeroIdentificacion();
				
				String nombrePaciente = dtoAutorEntSub.getDtoPaciente().getPrimerNombre() + " " +
					(UtilidadTexto.isEmpty(dtoAutorEntSub.getDtoPaciente().getSegundoNombre())?"":
						dtoAutorEntSub.getDtoPaciente().getSegundoNombre()) + " " +
						dtoAutorEntSub.getDtoPaciente().getPrimerApellido() + " " +
						(UtilidadTexto.isEmpty(dtoAutorEntSub.getDtoPaciente().getSegundoApellido())?"":
							dtoAutorEntSub.getDtoPaciente().getSegundoApellido());
				
				String nombreUsuarioAutoriza = dtoAutorEntSub.getUsuarioAutoriza().getNombre() + " " +
				dtoAutorEntSub.getUsuarioAutoriza().getApellido();
				
				if(!UtilidadTexto.isEmpty(dtoAutorEntSub.getDtoPaciente().getFechaNacimiento()))
				{		
					/**La edad del paciente se calcula sobre la fecha de la autorización no sobre la fecha actual 
					 * DCU 1104 V1.3 - Diana Ruiz*/
										
					String fechaAutorizacion= UtilidadFecha.conversionFormatoFechaAAp(dtoAutorEntSub.getFechaAutorizacion().toString());
					String edadPaciente=UtilidadFecha.calcularEdadDetallada(dtoAutorEntSub.getDtoPaciente().getFechaNacimiento(), fechaAutorizacion);
					dtoAutorEntSub.getDtoPaciente().setEdadPacienteCapitado(edadPaciente);
					
					//dtoAutorEntSub.getDtoPaciente().setEdadPaciente(
						//	UtilidadFecha.calcularEdad(dtoAutorEntSub.getDtoPaciente().getFechaNacimiento()));	
				}	
				
				
				//Se consulta las solicitudes de la autorizacion y que provienen de una orden ambulatoria
				try {
					forma.setListaSolicitudesOrdenesPeticiones(manejoPacienteFacade.obtenerSolicitudesAutorizacionConOrdenAmbulatoria(dtoAutorEntSub));
				} catch(IPSException ipse){
					errores.add("ERROR Negocio", new ActionMessage(ipse.getErrorCode().toString()));
				}
				catch(Exception e){
					Log4JManager.error(e);
					errores.add("ERROR no Controlado", new ActionMessage("errors.notEspecific", e.getMessage()));
				}
				if(!errores.isEmpty()){
					saveErrors(request, errores);
				}
				
				
				
				//Se comenta ya que el tipo afiliado y clasificación se cargan de la autorización
				/*if(UtilidadTexto.isEmpty(dtoAutorEntSub.getDtoPaciente().getClasificacionSocioEconomica()))
				{	
					Cuenta cuenta= new Cuenta();
		        	cuenta.cargarCuenta(con, paciente.getCodigoCuenta()+"");
		        	dtoAutorEntSub.getDtoPaciente().setClasificacionSocioEconomica(cuenta.getEstrato());
		        	
		        	if(UtilidadTexto.isEmpty(dtoAutorEntSub.getDtoPaciente().getTipoAfiliado()))
		        		dtoAutorEntSub.getDtoPaciente().setTipoAfiliado(cuenta.getTipoAfiliado()); 
				}*/
		        
				dtoAutorEntSub.getUsuarioAutoriza().setNombre(nombreUsuarioAutoriza);
				dtoAutorEntSub.getDtoPaciente().setPrimerNombre(nombrePaciente);
				dtoAutorEntSub.getDtoPaciente().setNumeroIdentificacion(tipoNroID);								
				dtoAutorEntSub.getAutorCapitacion().setCodigoPK(forma.getCodigoAutorizacion());
				
				String estadoAutorizacion = dtoAutorEntSub.getEstado();
				
				if(!UtilidadTexto.isEmpty(dtoAutorEntSub.getEstado())){
					String estado=(String)ValoresPorDefecto.getIntegridadDominio(dtoAutorEntSub.getEstado());
					dtoAutorEntSub.setEstado(estado);
				}
											
				forma.setDtoAutorizacionCapitacion(dtoAutorEntSub);
				
				//la descripcion se debe mostrar de acuerdo a lo definido en el parámetro general 
				//para el modulo de Administración 'Codigo Manual estandar Búsqueda de Servicios'
								
				String tipoTarifario = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(
						usuario.getCodigoInstitucionInt());
				if(UtilidadTexto.isEmpty(tipoTarifario)){
					tipoTarifario=ConstantesBD.codigoTarifarioCups+"";
				}
					
				List<ServicioAutorizadoCapitacionDto> listaServicios=new ArrayList<ServicioAutorizadoCapitacionDto>(0);		
				
				try {
					if(estadoAutorizacion.equals(ConstantesIntegridadDominio.acronimoEstadoAnulado) 
							&& !forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SERVICIOS_ING_EST)
							){
						listaServicios = manejoPacienteFacade.consultarServiciosAnuladosCapitacion(dtoAutorEntSub.getAutorizacion(), Long.parseLong(tipoTarifario), true);
					} else{
						listaServicios = manejoPacienteFacade.consultarServiciosAutorizadosCapitacion(dtoAutorEntSub.getAutorizacion(), Long.parseLong(tipoTarifario), true);
					}	
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					Log4JManager.error("Error en la consulta de servicios de una autorizacion", e);
				} catch (IPSException e) {
					// TODO Auto-generated catch block
					Log4JManager.error("Error en la consulta de servicios de una autorizacion", e);
				}
				//MT6703
				BigDecimal valorAutorizacion= new BigDecimal(0);
				ArrayList<DtoServiciosAutorizaciones>servicios=new ArrayList<DtoServiciosAutorizaciones>();
				if(listaServicios != null && !listaServicios.isEmpty()){
					for(ServicioAutorizadoCapitacionDto dtoServicio:listaServicios){
						DtoServiciosAutorizaciones servicio=new DtoServiciosAutorizaciones();
						
						servicio.setTipoAutorizacion(dtoServicio.getTipoAutorizacion());
						servicio.setCodigoOrdenSolPet(dtoServicio.getCodigo());
						
												
						servicio.setCodigoPropietario(""+dtoServicio.getCodServ());
						servicio.setCodigoServicio(Long.valueOf(dtoServicio.getIdServicio()).intValue());
						
						final int posicionDescripcion=0;
						String descripcionServicion ="";
						try {
							descripcionServicion =manejoPacienteFacade.obtenerDescripcionServicioAutorizar(
									
									Utilidades.convertirAEntero(String.valueOf(dtoServicio.getIdServicio())), 
									Utilidades.convertirAEntero(tipoTarifario), true)[posicionDescripcion];
						} catch (IPSException e) {
							// TODO Auto-generated catch block
							Log4JManager.error("Error en la consulta de descripcion de servicios de una autorizacion", e);
						}
						
						servicio.setDescripcionServicio(descripcionServicion);
						
						servicio.setCantidadAutorizadaServicio(Long.valueOf(dtoServicio.getCantidad()).intValue());
						servicio.setDescripcionNivelAtencion(dtoServicio.getNivelAtencion());
						servicio.setDiagnostico(dtoServicio.getAcronimoDiag());
						servicio.setTipoCieDx(dtoServicio.getTipoCieDiag());
						servicio.setDescripcionDiagnostico(dtoServicio.getDiagDescripcion());
						servicio.setTipoSolicitud(dtoServicio.getTipoSolicitud());
						if(dtoServicio.getValorTarifa()!=null){
							servicio.setValorServicio(new BigDecimal(dtoServicio.getValorTarifa()));
							servicio.setValorAutorizado(new BigDecimal(dtoServicio.getValorTarifa()*dtoServicio.getCantidad()));
						}
						
						//define el origen si es solicitud, peticion u orden ambulatoria 
						if(dtoServicio.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_ORDEN_AMB
								||dtoServicio.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_PETICION
								||dtoServicio.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_SOLICITUD
								||dtoServicio.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_CARGOS_DIRECTOS){
							servicio.setNumeroOrdenLong(dtoServicio.getConsecutivo());
							servicio.setFechaOrden(dtoServicio.getFechaGeneracion());
						}
						else{
							//servicios de ingreso estancia
							if(dtoServicio.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_ING_EST){
								servicio.setNumeroOrdenLong(null);
								servicio.setFechaOrden(null);
								
							}
						}
						//MT6703
						valorAutorizacion=valorAutorizacion.add(new BigDecimal(dtoServicio.getValorTarifa()*dtoServicio.getCantidad()));
						servicios.add(servicio);
					}
				}
				
				List<ArticuloAutorizadoCapitacionDto> listaArticulos =new ArrayList<ArticuloAutorizadoCapitacionDto>(); 
				//detalleArticulo.obtenerDetalleArticulosAutorCapitacion(dtoAutorEntSub.getAutorizacion());
				try {
					if(estadoAutorizacion.equals(ConstantesIntegridadDominio.acronimoEstadoAnulado) 
							&& !forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_ARTICULOS_ING_EST)
							){
						listaArticulos = manejoPacienteFacade.consultarArticulosAnuladosCapitacion(dtoAutorEntSub.getAutorizacion(),usuario.getCodigoInstitucionInt(),true);
					} else{
						listaArticulos = manejoPacienteFacade.consultarArticulosAutorizadosCapitacion(dtoAutorEntSub.getAutorizacion(),usuario.getCodigoInstitucionInt(),true);
					}	
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					Log4JManager.error("Error en la consulta de articulos de una autorizacion", e);
				} catch (IPSException e) {
					// TODO Auto-generated catch block
					Log4JManager.error("Error en la consulta de articulos de una autorizacion", e);
				}
				ArrayList<DtoArticulosAutorizaciones>articulos=new ArrayList<DtoArticulosAutorizaciones>();
				if(listaArticulos != null && !listaArticulos.isEmpty()){
					for(ArticuloAutorizadoCapitacionDto dtoArticulo:listaArticulos){
						DtoArticulosAutorizaciones articulo=new DtoArticulosAutorizaciones();
						
						articulo.setTipoAutorizacion(dtoArticulo.getTipoAutorizacion());
						articulo.setCodigoOrdenSolPet(dtoArticulo.getCodigo());
						
						articulo.setTipoSolicitud(dtoArticulo.getTipoSolicitud());
						final int posicionDescripcion=0;
						String descripcionArticulo ="";
						try {
							descripcionArticulo =manejoPacienteFacade.obtenerDescripcionMedicamentoInsumoAutorizar(
									
									Utilidades.convertirAEntero(String.valueOf(dtoArticulo.getCodArt())), 
									Utilidades.convertirAEntero(tipoTarifario), true)[posicionDescripcion];
						} catch (IPSException e) {
							// TODO Auto-generated catch block
							Log4JManager.error("Error en la consulta de descripcion de articulos de una autorizacion", e);
						}
						
						articulo.setDescripcionArticulo(descripcionArticulo);
						
						articulo.setCodigoArticulo(Long.valueOf(dtoArticulo.getCodArt()).intValue());
						
						articulo.setNaturalezaArticulo(dtoArticulo.getNaturalezaArticulo());
						articulo.setCantidadAutorizadaArticulo(""+dtoArticulo.getCantidad());
						articulo.setDiagnostico(dtoArticulo.getAcronimoDiag());
						articulo.setTipoCieDx(dtoArticulo.getTipoCieDiag());
						articulo.setDescripcionDiagnostico(dtoArticulo.getDiagDescripcion());
						
						articulo.setConcentracionArticulo(dtoArticulo.getConcentracion());
						articulo.setFormaFarmaceuticaArticulo(dtoArticulo.getFormaFarmaceutica());
						articulo.setUnidadMedidaArticulo(dtoArticulo.getUnidadMedida());
						
						if(dtoArticulo.getValorTarifa()!=null){
							articulo.setValorArticulo(new BigDecimal(dtoArticulo.getValorTarifa()));
							articulo.setValorAutorizado(new BigDecimal(dtoArticulo.getValorTarifa()*dtoArticulo.getCantidad()));
						}
						
						//define el origen si es solicitud, peticion u orden ambulatoria 
						if(dtoArticulo.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_ORDEN_AMB
								||dtoArticulo.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_PETICION
								||dtoArticulo.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_SOLICITUD){
							
							// para mostrar la etiqueta correcta en la lista de articulos autorizados
							if(!dtoAutorEntSub.isEsOrden()&&!dtoAutorEntSub.isEsSolicitud()&&dtoArticulo.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_ORDEN_AMB){
								dtoAutorEntSub.setEsOrden(true);
								dtoAutorEntSub.setEsSolicitud(false);
							}else{
								if(!dtoAutorEntSub.isEsOrden()&&!dtoAutorEntSub.isEsSolicitud()&&dtoArticulo.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_SOLICITUD){
									dtoAutorEntSub.setEsOrden(false);
									dtoAutorEntSub.setEsSolicitud(true);
								}
							}
							
							articulo.setNumeroOrdenLong(dtoArticulo.getConsecutivo());
							articulo.setFechaOrden(dtoArticulo.getFechaGeneracion());
						}
						else{
							//articulos de ingreso estancia
							if(dtoArticulo.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_ING_EST){
								articulo.setNumeroOrdenLong(null);
								articulo.setFechaOrden(null);
								
							}
						}
						//MT6703
						valorAutorizacion=valorAutorizacion.add(new BigDecimal(dtoArticulo.getValorTarifa()*dtoArticulo.getCantidad()));
						articulos.add(articulo);
					}
				}
				
				forma.setListaServicios(servicios);
				forma.setListaArticulos(articulos);
				
				//MT 6703 
				
				Boolean isConvenioManejaMonto = manejoPacienteFacade.consultarSiConvenioManejaMontos(paciente.getCodigoConvenio());
				DTOAutorEntidadSubcontratadaCapitacion dtoAutorizacion=forma.getDtoAutorizacionCapitacion();
				
				if(!isConvenioManejaMonto){
					Boolean isPacientePagaAtencion = manejoPacienteFacade.consultarSiPacientePagaAtencion(paciente.getCodigoContrato(),true);
					if(isPacientePagaAtencion)
						{
						     dtoAutorizacion.getDtoPaciente().setValorMontoCobro(valorAutorizacion);
						}  
					else
					    {
						   dtoAutorizacion.getDtoPaciente().setValorMontoCobro(new BigDecimal(0.0));
					    }
						    	 
			}						
				
			}						
			return mapping.findForward("detalleAutorizacionCapitacion");	
		}
	}
	
		
 	/**IMPRIMIR SERVICIOS
 	 * 
 	 * @param con
 	 * @param forma
 	 * @param listaServicios
 	 * @param paciente
 	 * @param usuarioSesion
 	 * @param request
 	 */	
	private void imprimirAutorizacionesEntidadesSubServicio (ConsultaAutorizacionesCapitacionSubDetalleForm forma,
			UsuarioBasico usuario,InstitucionBasica institucion)
	{		
		String nombreReporte = "AUTORIZACION CAPITACION SUBCONTRATADA";
		String nombreArchivo = "";
		
		DtoGeneralReporteServiciosAutorizados dtoReporte = new DtoGeneralReporteServiciosAutorizados();
		GeneradorReporteFormatoCapitacionAutorservicio generadorReporte 	= new GeneradorReporteFormatoCapitacionAutorservicio(dtoReporte);
		DTOReporteAutorizacionSeccionPaciente dtoPaciente = new DTOReporteAutorizacionSeccionPaciente();
		DTOReporteAutorizacionSeccionAutorizacion dtoInfoAutorizacion = new DTOReporteAutorizacionSeccionAutorizacion();
		
		String infoEncabezadoPagina 	= ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
		String infoPiePagina			= ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
		String reporteMediaCarta = ValoresPorDefecto.getImpresionMediaCarta(usuario.getCodigoInstitucionInt());
		if(reporteMediaCarta== null || reporteMediaCarta.trim().isEmpty()){
			reporteMediaCarta=ConstantesBD.acronimoNo;
		}
		
		//AutorizacionCapitacionDto dtoAutorizacion=forma.getAutorizacionCapitacion();
		DTOAutorEntidadSubcontratadaCapitacion dtoAutorizacion=forma.getDtoAutorizacionCapitacion();
		
		dtoPaciente.setNombrePaciente(dtoAutorizacion.getDtoPaciente().getPrimerNombre());
		dtoPaciente.setTipoDocPaciente(dtoAutorizacion.getDtoPaciente().getNumeroIdentificacion());
		dtoPaciente.setNumeroDocPaciente("");
		dtoPaciente.setTipoContratoPaciente(dtoAutorizacion.getDtoPaciente().getConvenio().getTiposContrato().getNombre());
		dtoPaciente.setEdadPaciente(dtoAutorizacion.getDtoPaciente().getEdadPacienteCapitado());
		dtoPaciente.setConvenioPaciente(dtoAutorizacion.getDtoPaciente().getConvenio().getNombre());
		dtoPaciente.setCategoriaSocioEconomica(dtoAutorizacion.getDtoPaciente().getClasificacionSocioEconomica());
		if(dtoAutorizacion.getAutorCapitacion().getOtroConvenioRecobro()!=null
				&&!dtoAutorizacion.getAutorCapitacion().getOtroConvenioRecobro().trim().isEmpty()){
			dtoPaciente.setRecobro(ConstantesBD.acronimoSi);
			dtoPaciente.setEntidadRecobro(dtoAutorizacion.getAutorCapitacion().getOtroConvenioRecobro());
		}
		else{
			dtoPaciente.setRecobro(ConstantesBD.acronimoNo);
			if(dtoAutorizacion.getAutorCapitacion().getOtroConvenioRecobro()!=null){
				dtoPaciente.setEntidadRecobro(dtoAutorizacion.getAutorCapitacion().getOtroConvenioRecobro().trim());
			}else{
				dtoPaciente.setEntidadRecobro("");
			}
		}
		dtoPaciente.setTipoAfiliado(dtoAutorizacion.getDtoPaciente().getTipoAfiliado());
		
		DecimalFormat decimalFormat= new DecimalFormat(ConstantesBD.formatoMonetarioBeanWrite);
		BigDecimal valor=(dtoAutorizacion.getDtoPaciente().getValorPorcentajeMontoCobro()!=null?
				(dtoAutorizacion.getDtoPaciente().getValorPorcentajeMontoCobro()):
					(dtoAutorizacion.getDtoPaciente().getValorMontoCobro()!=null?
							dtoAutorizacion.getDtoPaciente().getValorMontoCobro():null));
		String valorString="";
		if(valor!=null){
			valorString=decimalFormat.format(valor);
		}
		
		dtoPaciente.setMontoCobro((dtoAutorizacion.getDtoPaciente().getTipoMontoCobro()!=null?
				dtoAutorizacion.getDtoPaciente().getTipoMontoCobro():"")
				+" "+valorString+(dtoAutorizacion.getDtoPaciente().getValorPorcentajeMontoCobro()!=null?" %":""));
		
		dtoInfoAutorizacion.setNumeroAutorizacion(dtoAutorizacion.getConsecutivoAutorizacion());
		dtoInfoAutorizacion.setEntidadSub(dtoAutorizacion.getDtoEntidadSubcontratada().getRazonSocial());
		if(dtoAutorizacion.getDtoEntidadSubcontratada().getRazonSocial().equals(NOMBRE_ENTIDAD_OTRA)){ 
			dtoInfoAutorizacion.setDireccionEntidadSub(dtoAutorizacion.getDtoEntidadSubcontratada().getDireccionotra());
			dtoInfoAutorizacion.setTelefonoEntidadSub(dtoAutorizacion.getDtoEntidadSubcontratada().getTelefonootra());
		}else{
			dtoInfoAutorizacion.setDireccionEntidadSub(dtoAutorizacion.getDtoEntidadSubcontratada().getDireccion());
			dtoInfoAutorizacion.setTelefonoEntidadSub(dtoAutorizacion.getDtoEntidadSubcontratada().getTelefono());
		}
		dtoInfoAutorizacion.setEntidadAutoriza(usuario.getInstitucion());
		dtoInfoAutorizacion.setUsuarioAutoriza(dtoAutorizacion.getUsuarioAutoriza().getNombre());
		SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");
		dtoInfoAutorizacion.setFechaAutorizacion(format.format(dtoAutorizacion.getFechaAutorizacion()));
		dtoInfoAutorizacion.setFechaVencimiento(format.format(dtoAutorizacion.getFechaVencimiento()));
		//String nombreEstado = (String)ValoresPorDefecto.getIntegridadDominio(dtoAutorizacion.getEstado());
		dtoInfoAutorizacion.setEstadoAutorizacion(dtoAutorizacion.getEstado());
		dtoInfoAutorizacion.setObservaciones(dtoAutorizacion.getObservaciones());
		
		dtoReporte.setDatosEncabezado(infoEncabezadoPagina);
		dtoReporte.setDatosPie(infoPiePagina);
		dtoReporte.setRutaLogo(institucion.getLogoJsp());
		dtoReporte.setUbicacionLogo(institucion.getUbicacionLogo());
		dtoReporte.setDtoPaciente(dtoPaciente);
		dtoReporte.setDtoAutorizacion(dtoInfoAutorizacion);
		dtoReporte.setRazonSocial(institucion.getRazonSocial());
		dtoReporte.setNit(institucion.getNit());
		dtoReporte.setActividadEconomica(institucion.getActividadEconomica());
		dtoReporte.setDireccion(institucion.getDireccion()+separador+institucion.getTelefono());
		//dtoReporte.setTelefono(institucion.getTelefono());
		StringBuilder nombreUsuario= new StringBuilder();
		nombreUsuario.append(usuario.getNombreUsuario());
		nombreUsuario.append(" (");
		nombreUsuario.append(usuario.getLoginUsuario());
		nombreUsuario.append(")");
		dtoReporte.setUsuario(nombreUsuario.toString());
		dtoReporte.setTipoReporteMediaCarta(reporteMediaCarta);
		
		dtoReporte.setObservaciones(dtoAutorizacion.getObservaciones());
		
		ArrayList<DtoServiciosAutorizaciones> listaServicios = new ArrayList<DtoServiciosAutorizaciones>();
		for(DtoServiciosAutorizaciones servicio:forma.getListaServicios()){
			DtoServiciosAutorizaciones servicioAutorizado=new DtoServiciosAutorizaciones();
			servicioAutorizado.setConsecutivoOrdenMed(servicio.getNumeroOrdenLong()!=null?servicio.getNumeroOrdenLong().intValue():null);
			servicioAutorizado.setFechaOrden(servicio.getFechaOrden());
			servicioAutorizado.setCodigoPropietario(servicio.getCodigoPropietario());
			servicioAutorizado.setDescripcionServicio(servicio.getDescripcionServicio());
			servicioAutorizado.setCantidadSolicitada(servicio.getCantidadAutorizadaServicio());
			servicioAutorizado.setDescripcionNivelAutorizacion(servicio.getDescripcionNivelAtencion());
			StringBuffer diagnostico=new StringBuffer();
			if(servicio.getDiagnostico() != null && servicio.getDescripcionDiagnostico() != null){
				diagnostico.append(servicio.getDiagnostico());
				diagnostico.append(separador);
				diagnostico.append(servicio.getTipoCieDx());
			}
			servicioAutorizado.setDiagnostico(diagnostico.toString());
			listaServicios.add(servicioAutorizado);
		}
		dtoReporte.setListaServicios(listaServicios);
		
		//hermorhu - MT5966
		//Dependiendo el valor de 'esImpresionOriginal' se envia al reporte el tipo de impresion
		if(forma.getEsImpresionOriginal() != null && forma.getEsImpresionOriginal() == true) {
			dtoReporte.setTipoImpresion("Original");
			dtoReporte.setFechaHoraEntrega("");
		} else if(forma.getEsImpresionOriginal() != null && forma.getEsImpresionOriginal() == false) {
			dtoReporte.setTipoImpresion("Copia");
			dtoReporte.setFechaHoraEntrega("Fecha Entrega: "+ forma.getDtoAutorizacionCapitacion().getAutorizacionEntrega().getFechaEntrega() +" "+forma.getDtoAutorizacionCapitacion().getAutorizacionEntrega().getHoraEntrega());
		} else {
			dtoReporte.setTipoImpresion("");
		}
		
		JasperPrint reporte = generadorReporte.generarReporte();
		nombreArchivo = generadorReporte.exportarReportePDF(reporte, nombreReporte);
		if(!nombreArchivo.trim().isEmpty())
		{
			List<String>nombresReportes=new ArrayList<String>(0);
			nombresReportes.add(nombreArchivo);
			forma.setListaNombresReportes((ArrayList<String>)nombresReportes);
			/*forma.setNombreReporte(nombreArchivo);
			forma.setMostrarReporte(true);*/
		}
	}
	
	
	/**IMPRIMIR ARTICULOS
	 * 		
	 * @param con
	 * @param forma
	 * @param listaArticulos
	 * @param paciente
	 * @param usuarioSesion
	 * @param request
	 */	
	private void imprimirAutorizacionesEntidadesSubArticulos(ConsultaAutorizacionesCapitacionSubDetalleForm forma,
			UsuarioBasico usuario,InstitucionBasica institucion) 
	{			
		
		String nombreReporte = "AUTORIZACION CAPITACION SUBCONTRATADA";
		String nombreArchivo = "";
		DtoGeneralReporteArticulosAutorizados dtoReporte = new DtoGeneralReporteArticulosAutorizados();
		DTOReporteAutorizacionSeccionPaciente dtoPaciente = new DTOReporteAutorizacionSeccionPaciente();
		DTOReporteAutorizacionSeccionAutorizacion dtoInfoAutorizacion = new DTOReporteAutorizacionSeccionAutorizacion();
		GeneradorReporteFormatoCapitacionAutorArticulos generadorReporte = new GeneradorReporteFormatoCapitacionAutorArticulos(dtoReporte);
		String infoEncabezadoPagina = ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
		String infoPiePagina=ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
		String reporteMediaCarta = ValoresPorDefecto.getImpresionMediaCarta(usuario.getCodigoInstitucionInt());
		if(reporteMediaCarta== null || reporteMediaCarta.trim().isEmpty()){
			reporteMediaCarta=ConstantesBD.acronimoNo;
		}
		
		//AutorizacionCapitacionDto dtoAutorizacion=forma.getAutorizacionCapitacion();
		DTOAutorEntidadSubcontratadaCapitacion dtoAutorizacion=forma.getDtoAutorizacionCapitacion();
		
		dtoPaciente.setNombrePaciente(dtoAutorizacion.getDtoPaciente().getPrimerNombre());
		dtoPaciente.setTipoDocPaciente(dtoAutorizacion.getDtoPaciente().getNumeroIdentificacion());
		dtoPaciente.setNumeroDocPaciente("");
		dtoPaciente.setTipoContratoPaciente(dtoAutorizacion.getDtoPaciente().getConvenio().getTiposContrato().getNombre());
		dtoPaciente.setEdadPaciente(dtoAutorizacion.getDtoPaciente().getEdadPacienteCapitado());
		dtoPaciente.setConvenioPaciente(dtoAutorizacion.getDtoPaciente().getConvenio().getNombre());
		dtoPaciente.setCategoriaSocioEconomica(dtoAutorizacion.getDtoPaciente().getClasificacionSocioEconomica());
		if(dtoAutorizacion.getAutorCapitacion().getOtroConvenioRecobro()!=null
				&&!dtoAutorizacion.getAutorCapitacion().getOtroConvenioRecobro().trim().isEmpty()){
			dtoPaciente.setRecobro(ConstantesBD.acronimoSi);
			dtoPaciente.setEntidadRecobro(dtoAutorizacion.getAutorCapitacion().getOtroConvenioRecobro());
		}
		else{
			dtoPaciente.setRecobro(ConstantesBD.acronimoNo);
			if(dtoAutorizacion.getAutorCapitacion().getOtroConvenioRecobro()!=null){
				dtoPaciente.setEntidadRecobro(dtoAutorizacion.getAutorCapitacion().getOtroConvenioRecobro().trim());
			}else{
				dtoPaciente.setEntidadRecobro("");
			}
		}
		
		dtoPaciente.setTipoAfiliado(dtoAutorizacion.getDtoPaciente().getTipoAfiliado());
		
		DecimalFormat decimalFormat= new DecimalFormat(ConstantesBD.formatoMonetarioBeanWrite);
		BigDecimal valor=(dtoAutorizacion.getDtoPaciente().getValorPorcentajeMontoCobro()!=null?
				(dtoAutorizacion.getDtoPaciente().getValorPorcentajeMontoCobro()):
					(dtoAutorizacion.getDtoPaciente().getValorMontoCobro()!=null?
							dtoAutorizacion.getDtoPaciente().getValorMontoCobro():null));
		String valorString="";
		if(valor!=null){
			valorString=decimalFormat.format(valor);
		}
		
		dtoPaciente.setMontoCobro((dtoAutorizacion.getDtoPaciente().getTipoMontoCobro()!=null?
				dtoAutorizacion.getDtoPaciente().getTipoMontoCobro():"")
				+" "+valorString+(dtoAutorizacion.getDtoPaciente().getValorPorcentajeMontoCobro()!=null?" %":""));
		
		dtoInfoAutorizacion.setNumeroAutorizacion(dtoAutorizacion.getConsecutivoAutorizacion());
		dtoInfoAutorizacion.setEntidadSub(dtoAutorizacion.getDtoEntidadSubcontratada().getRazonSocial());
		if(dtoAutorizacion.getDtoEntidadSubcontratada().getRazonSocial().equals(NOMBRE_ENTIDAD_OTRA)){ 
			dtoInfoAutorizacion.setDireccionEntidadSub(dtoAutorizacion.getDtoEntidadSubcontratada().getDireccionotra());
			dtoInfoAutorizacion.setTelefonoEntidadSub(dtoAutorizacion.getDtoEntidadSubcontratada().getTelefonootra());
		}else{
			dtoInfoAutorizacion.setDireccionEntidadSub(dtoAutorizacion.getDtoEntidadSubcontratada().getDireccion());
			dtoInfoAutorizacion.setTelefonoEntidadSub(dtoAutorizacion.getDtoEntidadSubcontratada().getTelefono());
		}
		dtoInfoAutorizacion.setEntidadAutoriza(usuario.getInstitucion());
		dtoInfoAutorizacion.setUsuarioAutoriza(dtoAutorizacion.getUsuarioAutoriza().getNombre());
		SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy");
		dtoInfoAutorizacion.setFechaAutorizacion(format.format(dtoAutorizacion.getFechaAutorizacion()));
		dtoInfoAutorizacion.setFechaVencimiento(format.format(dtoAutorizacion.getFechaVencimiento()));
		//String nombreEstado = (String)ValoresPorDefecto.getIntegridadDominio(dtoAutorizacion.getEstado());
		dtoInfoAutorizacion.setEstadoAutorizacion(dtoAutorizacion.getEstado());
		dtoInfoAutorizacion.setObservaciones(dtoAutorizacion.getObservaciones());
		
		dtoReporte.setDatosEncabezado(infoEncabezadoPagina);
		dtoReporte.setDatosPie(infoPiePagina);
		dtoReporte.setRutaLogo(institucion.getLogoJsp());
		dtoReporte.setUbicacionLogo(institucion.getUbicacionLogo());
		dtoReporte.setDtoPaciente(dtoPaciente);
		dtoReporte.setDtoAutorizacion(dtoInfoAutorizacion);
		dtoReporte.setRazonSocial(institucion.getRazonSocial());
		dtoReporte.setNit(institucion.getNit());
		dtoReporte.setActividadEconomica(institucion.getActividadEconomica());
		dtoReporte.setDireccion(institucion.getDireccion()+separador+institucion.getTelefono());
		//dtoReporte.setTelefono(institucion.getTelefono());
		StringBuilder nombreUsuario= new StringBuilder();
		nombreUsuario.append(usuario.getNombreUsuario());
		nombreUsuario.append(" (");
		nombreUsuario.append(usuario.getLoginUsuario());
		nombreUsuario.append(")");
		dtoReporte.setUsuario(nombreUsuario.toString());
		dtoReporte.setTipoReporteMediaCarta(reporteMediaCarta);
			 	
		dtoReporte.setObservaciones(dtoAutorizacion.getObservaciones());
		
		ArrayList<DtoArticulosAutorizaciones> listaMedicamentosInsumo = new ArrayList<DtoArticulosAutorizaciones>();
		for(DtoArticulosAutorizaciones articulo:forma.getListaArticulos()){
			DtoArticulosAutorizaciones dtoMedicamento= new DtoArticulosAutorizaciones();
			dtoMedicamento.setNumeroOrden(articulo.getNumeroOrdenLong()!=null?articulo.getNumeroOrdenLong().intValue():null);
			dtoMedicamento.setFechaOrden(articulo.getFechaOrden());
			dtoMedicamento.setCodigoArticulo(Integer.valueOf(articulo.getCodigoArticulo()));
			/*StringBuffer descripcionArticulo=new StringBuffer();
			descripcionArticulo.append(articulo.getDescripcionArticulo());
			descripcionArticulo.append(" ");
			descripcionArticulo.append(articulo.getConcentracionArticulo());
			descripcionArticulo.append(" ");
			descripcionArticulo.append(articulo.getFormaFarmaceuticaArticulo());
			descripcionArticulo.append(" ");
			descripcionArticulo.append(articulo.getUnidadMedidaArticulo());*/
			//dtoMedicamento.setDescripcionArticulo(descripcionArticulo.toString());
			dtoMedicamento.setDescripcionArticulo(articulo.getDescripcionArticulo());
			dtoMedicamento.setNaturalezaArticulo(articulo.getNaturalezaArticulo());
			if(articulo.getCantidadAutorizadaArticulo()!=null&&!articulo.getCantidadAutorizadaArticulo().trim().isEmpty()){
				dtoMedicamento.setCantidadSolicitada(Integer.parseInt(articulo.getCantidadAutorizadaArticulo()));
			}
			StringBuffer diagnostico=new StringBuffer();
			if(articulo.getDiagnostico() != null && articulo.getDescripcionDiagnostico() != null){
				diagnostico.append(articulo.getDiagnostico());
				diagnostico.append(separador);
				diagnostico.append(articulo.getTipoCieDx());
			}
			dtoMedicamento.setDiagnostico(diagnostico.toString());
			listaMedicamentosInsumo.add(dtoMedicamento);
		}
		dtoReporte.setListaArticulos(listaMedicamentosInsumo);
		
		//hermorhu - MT5966
		//Dependiendo el valor de 'esImpresionOriginal' se envia al reporte el tipo de impresion
		if(forma.getEsImpresionOriginal() != null && forma.getEsImpresionOriginal() == true) {
			dtoReporte.setTipoImpresion("Original");
			dtoReporte.setFechaHoraEntrega("");
		} else if(forma.getEsImpresionOriginal() != null && forma.getEsImpresionOriginal() == false) {
			dtoReporte.setTipoImpresion("Copia");
			dtoReporte.setFechaHoraEntrega("Fecha Entrega: "+ forma.getDtoAutorizacionCapitacion().getAutorizacionEntrega().getFechaEntrega() +" "+forma.getDtoAutorizacionCapitacion().getAutorizacionEntrega().getHoraEntrega());
		} else {
			dtoReporte.setTipoImpresion("");
		}
		
		JasperPrint reporte = generadorReporte.generarReporte();
		nombreArchivo = generadorReporte.exportarReportePDF(reporte, nombreReporte);
		if(!nombreArchivo.trim().isEmpty())
		{
			List<String>nombresReportes=new ArrayList<String>(0);
			nombresReportes.add(nombreArchivo);
			forma.setListaNombresReportes((ArrayList<String>)nombresReportes);
			/*forma.setNombreReporte(nombreArchivo);
			forma.setMostrarReporte(true);*/
		}
	}
	
	/**
	 * M&eacute;todo encargado de generar el formato de impresion de autorizaciones de ingreso estancia
	 * 
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param paciente
	 * @author jeilones
	 * @param institucion 
	 * @created 27/08/2012
	 */
	@SuppressWarnings("deprecation")
	private void generarReporteAutorizacionFormatoIngEstancia(ConsultaAutorizacionesCapitacionSubDetalleForm forma,
			UsuarioBasico usuario, HttpServletRequest request,PersonaBasica paciente, InstitucionBasica institucion) {
		
		
		String nombreReporte="AUTORIZACION INGRESO ESTANCIA";
		String nombreArchivo ="";
		DtoGeneralReporteIngresoEstancia dtoReporte = new DtoGeneralReporteIngresoEstancia();
		GeneradorReporteFormatoCapitacionAutorIngresoEstancia generadorReporte =
			new GeneradorReporteFormatoCapitacionAutorIngresoEstancia(dtoReporte);
		
		ArrayList<String> listaNombresReportes 								= new ArrayList<String>();
		DTOReporteAutorizacionSeccionAdmision dtoAdmisionIngresoEstancia 	= new DTOReporteAutorizacionSeccionAdmision();
		DTOReporteAutorizacionSeccionAutorizacion dtoAutorizacion 			= new DTOReporteAutorizacionSeccionAutorizacion();
		DtoEntidadSubcontratada dtoEntidadSubAlmacenada 					= new DtoEntidadSubcontratada();

		/** Datos almacenados**/
		DtoUsuariosCapitados datos=new DtoUsuariosCapitados();
			datos.setPrimerNombre(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getPrimerNombre());
			datos.setPrimerApellido(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getPrimerApellido());
			datos.setSegundoNombre(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getSegundoNombre());
			datos.setSegundoApellido(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getSegundoApellido());		
		String nombrePaciente= "";
			datos.setFechaNacimiento(UtilidadFecha.conversionFormatoFechaStringDate(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getFechaNacimiento()));
			datos.setTipoIdentificacion(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getTipoId());
			datos.setNumeroIdentificacion(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getNumeroId());
			datos.setNombreTipoContrato(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getConvenio().getTiposContrato().getNombre());
			datos.setNombreConvenio(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getConvenio().getNombre());
			datos.setDescripcionEstratoSocial(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getClasificacionSocioEconomica());
			datos.setNombreTipoAfiliado(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getTipoAfiliado());
			datos.setDescripcionOtraEntidadRecobrar(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getOtroConvenioRecobro());			
		
		DtoIngresoEstancia datosAdmision=new DtoIngresoEstancia();
			datosAdmision.setHoraAdmision(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getHoraAdmision());
			datosAdmision.setFechaAdmision(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getFechaAdmision());
			datosAdmision.setViaIngreso(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getViaIngreso());
			datosAdmision.setMedicoSolicitante(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getMedicoSolicitante());
			datosAdmision.setObservaciones(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getObservaciones());
			
		DTOAdministracionAutorizacion datosAutorizacion	=new DTOAdministracionAutorizacion(); 
			datosAutorizacion.setFechaInicioAutorizacion(forma.getDtoAutorizacionIngEstancia().getFechaAutorizacion());
			datosAutorizacion.setDiasEstanciaAutorizados(forma.getDtoAutorizacionIngEstancia().getDiasEstanciaAutorizados());
			datosAutorizacion.setFechaVencimientoAutorizacion(forma.getDtoAutorizacionIngEstancia().getFechaVencimiento());
				 			     			
	    if ((!UtilidadTexto.isEmpty(datos.getSegundoNombre())) && (!UtilidadTexto.isEmpty(datos.getSegundoApellido())))
	    {
	    	String []nombrePacienteTemp=datos.getPrimerNombre().split(" ") ; 
	    	nombrePaciente = nombrePacienteTemp[0] + " " + 
				datos.getSegundoNombre() + " " + datos.getPrimerApellido()+	" " + datos.getSegundoApellido();	    	
	    } else{
	    	nombrePaciente = datos.getPrimerNombre() ;	    	
	    }
			 			     			
		
		/** Datos entidad subcontratada Otra y Existentes **/		
		if(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getEntidadSubcontratada().getRazonSocial().equals(NOMBRE_ENTIDAD_OTRA))
		{
			dtoEntidadSubAlmacenada.setRazonSocial(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getEntidadSubcontratada().getRazonSocial());
			dtoEntidadSubAlmacenada.setTelefono(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getEntidadSubcontratada().getTelefonootra());
			dtoEntidadSubAlmacenada.setDireccion(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getEntidadSubcontratada().getDireccionotra());
			
		}else{
			dtoEntidadSubAlmacenada.setRazonSocial(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getEntidadSubcontratada().getRazonSocial());
			dtoEntidadSubAlmacenada.setTelefono(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getEntidadSubcontratada().getTelefono());
			dtoEntidadSubAlmacenada.setDireccion(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getEntidadSubcontratada().getDireccion());
		}	
		
		/** Datos secci&oacute;n paciente**/
		dtoReporte.setNombrePaciente(nombrePaciente);
		//dtoReporte.setTipoDocPaciente(datos.getTipoIdentificacion());
		dtoReporte.setNumeroDocPaciente(datos.getNumeroIdentificacion());
		
		dtoReporte.setEdadPaciente(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getEdadPacienteCapitado());
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
		
		String nombreDiagnosticoPrincipal = forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDxPrincipal().getAcronimoDiagnostico() + " " + 
		forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDxPrincipal().getTipoCieDiagnostico() + " " +
		forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDxPrincipal().getDescripcionDiagnostico();
		
		String nombreDiagnosticoComplicacion = forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDxComplicacion().getAcronimoDiagnostico() + " " + 
		forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDxComplicacion().getTipoCieDiagnostico() + " " +
		forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDxComplicacion().getDescripcionDiagnostico();
		
		dtoAdmisionIngresoEstancia.setViaIngresoAdmision(datosAdmision.getViaIngreso());
		
		/** Datos secci&oacute;n admisi&oacute;n **/
		//String []fechaHoraAdmision=datosAdmision.getHoraAdmision().split(" ");
		dtoAdmisionIngresoEstancia.setFechaAdmision(UtilidadFecha.conversionFormatoFechaAAp(datosAdmision.getFechaAdmision()));
		dtoAdmisionIngresoEstancia.setHoraAdmision(datosAdmision.getHoraAdmision());
		
		//dtoAdmisionIngresoEstancia.setViaIngresoAdmision(viasIngreso.getNombre());
		dtoAdmisionIngresoEstancia.setViaIngresoAdmision(datosAdmision.getViaIngreso());
		
		dtoAdmisionIngresoEstancia.setDxPrincipalAdmision(nombreDiagnosticoPrincipal);
		dtoAdmisionIngresoEstancia.setDxComplicacionAdmision(nombreDiagnosticoComplicacion);
		dtoAdmisionIngresoEstancia.setMedicoSolicitanteAdmision(datosAdmision.getMedicoSolicitante());
		dtoAdmisionIngresoEstancia.setObservacionesAdmision(datosAdmision.getObservaciones());
				
		/** Datos secci&oacute;n autorizaci&oacute;n **/
		dtoAutorizacion.setEntidadSub(dtoEntidadSubAlmacenada.getRazonSocial());
		dtoAutorizacion.setNumeroAutorizacion(String.valueOf(forma.getDtoAutorizacionIngEstancia().getConsecutivoAutorizacion()));
		dtoAutorizacion.setDireccionEntidadSub(dtoEntidadSubAlmacenada.getDireccion());
		dtoAutorizacion.setTelefonoEntidadSub(dtoEntidadSubAlmacenada.getTelefono());
		dtoAutorizacion.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp(datosAutorizacion.getFechaInicioAutorizacion()));
		dtoAutorizacion.setDiasEstanciaAutorizados(datosAutorizacion.getDiasEstanciaAutorizados());
		dtoAutorizacion.setFechaInicioAutorizacion(UtilidadFecha.conversionFormatoFechaAAp(datosAutorizacion.getFechaInicioAutorizacion()));
		dtoAutorizacion.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaAAp(datosAutorizacion.getFechaVencimientoAutorizacion()));
		dtoAutorizacion.setEstadoAutorizacion(forma.getDtoAutorizacionIngEstancia().getEstado());
		dtoAutorizacion.setEntidadAutoriza(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getInsitucionAutorizada());
		dtoAutorizacion.setUsuarioAutoriza(forma.getDtoAutorizacionIngEstancia().getUsuarioAutoriza().getNombre());
		dtoAutorizacion.setObservaciones(forma.getDtoAutorizacionIngEstancia().getObservaciones());		
		
		dtoReporte.setDtoAdmisionIngresoEstancia(dtoAdmisionIngresoEstancia);
		dtoReporte.setDtoAutorizacion(dtoAutorizacion);
		
		String infoEncabezadoPagina 	= ValoresPorDefecto.getEncFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
		String infoPiePagina			= ValoresPorDefecto.getPiePagFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
		String reporteMediaCarta = ValoresPorDefecto.getImpresionMediaCarta(usuario.getCodigoInstitucionInt());
		if(reporteMediaCarta== null || reporteMediaCarta.trim().isEmpty()){
			reporteMediaCarta=ConstantesBD.acronimoNo;
		}
		
		dtoReporte.setDatosEncabezado(infoEncabezadoPagina);
		dtoReporte.setDatosPie(infoPiePagina);
		dtoReporte.setRutaLogo(institucion.getLogoJsp());
		dtoReporte.setUbicacionLogo(institucion.getUbicacionLogo());
		dtoReporte.setRazonSocial(institucion.getRazonSocial());
		dtoReporte.setNit(institucion.getNit());
		dtoReporte.setActividadEconomica(institucion.getActividadEconomica());
		dtoReporte.setDireccion(institucion.getDireccion()+separador+institucion.getTelefono());
		//dtoReporte.setTelefono(institucion.getTelefono());
		StringBuilder nombreUsuario= new StringBuilder();
		nombreUsuario.append(usuario.getNombreUsuario());
		nombreUsuario.append(" (");
		nombreUsuario.append(usuario.getLoginUsuario());
		nombreUsuario.append(")");
		dtoReporte.setUsuario(nombreUsuario.toString());
		dtoReporte.setTipoReporteMediaCarta(reporteMediaCarta);
		
		JasperPrint reporte = generadorReporte.generarReporte();
		nombreArchivo = generadorReporte.exportarReportePDF(reporte, nombreReporte);
		
		//JasperViewer.viewReport(reporte, false);	
		listaNombresReportes.add(nombreArchivo);
		
		if(listaNombresReportes!=null && listaNombresReportes.size()>0){
			forma.setListaNombresReportes(listaNombresReportes);
			//forma.setMostrarImprimirAutorizacion(true);
		}
	} 

	/**
	 * Metodo que se encarga de cargar los datos del paciente en caso de que la busqueda se por Rangos, y por lo tanto 
	 * no se cargan los datos de la persona cargada en sesion.
	 * @param forma
	 * @param paciente
	 */
	@SuppressWarnings("deprecation")
	private PersonaBasica cargarDatosPacienteNoCargadoEnSesion(ConsultaAutorizacionesCapitacionSubDetalleForm forma,PersonaBasica paciente)
	{
		if(!(paciente.getCodigoPersona()>0))
    	{
			if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_INGRESO_ESTANCIA))
			{ 				
	    		paciente.setCodigoPersona(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getCodigo());
				paciente.setPrimerNombre(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getPrimerNombre());        	
	        	paciente.setPrimerApellido(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getPrimerApellido());
	        	paciente.setSegundoNombre(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getSegundoNombre());
	        	paciente.setSegundoApellido(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getSegundoApellido());
	        	paciente.setTelefono(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getTelefono());
	        	paciente.setNumeroIdentificacionPersona(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getNumeroId());
	        	paciente.setFechaNacimiento(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getFechaNacimiento());
	        	paciente.setEdad(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getEdadPaciente());
	        	paciente.setTipoAfiliado(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getTipoAfiliado());
	        	paciente.setCodigoCuenta(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getIdCuenta());
	        	paciente.setCodigoIngreso(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getIdIngreso());
	        	paciente.setCodigoUltimaViaIngreso(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getCodigoViaIngreso());
	        	paciente.setCodigoTipoPaciente(forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getDtoPaciente().getAcronimotipoPaciente());
	        		forma.setPaciente(paciente);
	    	}else{
	    		paciente.setCodigoPersona(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getCodigo());
	    		paciente.setPrimerNombre(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getPrimerNombre());        	
	        	paciente.setPrimerApellido(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getPrimerApellido());
	        	paciente.setSegundoNombre(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getSegundoNombre());
	        	paciente.setSegundoApellido(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getSegundoApellido());
	        	paciente.setTelefono(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getTelefono());
	        	paciente.setNumeroIdentificacionPersona(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getNumeroId());
	        	paciente.setFechaNacimiento(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getFechaNacimiento());
	        	paciente.setEdad(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getEdadPaciente());
	        	paciente.setTipoAfiliado(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getTipoAfiliado());
	        	paciente.setCodigoCuenta(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getIdCuenta());
	        	paciente.setCodigoIngreso(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getIdIngreso());
	        	paciente.setCodigoUltimaViaIngreso(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getCodigoViaIngreso());
	        	paciente.setCodigoTipoPaciente(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getAcronimotipoPaciente());
	        		forma.setPaciente(paciente);
	    	}
			return forma.getPaciente();
		}
		return paciente;
	}
	
	/**
	 * Metodo que se encarga de realizar las validaciones para la impresion de una autorización
	 * 
	 * @param request
	 * @param forma
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param con
	 * @param institucion
	 * @return
	 * @author hermorhu
	 * @created 25-feb-2013
	 */
	public ActionForward validarImpresionAutorizacion(HttpServletRequest request,
			ConsultaAutorizacionesCapitacionSubDetalleForm forma,
			ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, Connection con, InstitucionBasica institucion){
		
		ActionMessages errores = new ActionMessages();
		String forward = "";
		
		if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_INGRESO_ESTANCIA)){
			forward = FORWARD_DET_AUTORIZACION_INGESTANCIA;
		} else {
			forward = FORWARD_DET_AUTORIZACION_CAPITACION;
		}
		
		if(forma.isConsultaPorRango()) {	
			//Cuando se consulta por rango el paciente no se carga ya que se tomaba del que se introduce por 'Busqueda de paciente'
			if(forma.getPaciente().getCodigoPersona()!=ConstantesBD.codigoNuncaValido) {	
				paciente=forma.getPaciente();
				paciente=cargarDatosPacienteNoCargadoEnSesion(forma, paciente);
			} else {	
				paciente=new PersonaBasica();
				paciente=cargarDatosPacienteNoCargadoEnSesion(forma, paciente);
			}
		} else {
			paciente=cargarDatosPacienteNoCargadoEnSesion(forma, paciente);
		}
		
		String formato = ValoresPorDefecto.getFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
		
		if(formato == null || formato.trim().isEmpty()) {
			
			MessageResources mensajes = MessageResources.getMessageResources(PROPERTIES_DETALLE_CONSULTA);	
			errores.add("", new ActionMessage(KEY_ERROR_PARAM_GRAL_MANEJOPACIENTE, mensajes.getMessage("consultaAutorizacionesCapitacionSubDetalleForm.errorDefinirFormatoImpresion")));
		
		} else {
			//Si existe Autorizacion de Entidad Subcontratada
			if(forma.getDtoAutorizacionCapitacion().getConsecutivoAutorizacion() != null && (!forma.getDtoAutorizacionCapitacion().getConsecutivoAutorizacion().isEmpty() && !forma.getDtoAutorizacionCapitacion().getConsecutivoAutorizacion().equals(String.valueOf(ConstantesBD.codigoNuncaValido)))) {
				//Si no existe la Entrega de la Autorizacion se muestra popup para ingresar datos de entrega
				if(forma.getDtoAutorizacionCapitacion().getAutorizacionEntrega() == null) {
					if(!forma.getDtoAutorizacionCapitacion().getEstado().equals(ESTADO_ANULADO)) {
						forma.setMostrarPopupPrimeraImpresion(true);
						forma.setEsImpresionOriginal(true);
					} else {
						//No se muestra popup de primera impresion
						forma.setMostrarPopupPrimeraImpresion(false);
						//No se muestra el label en la impresion
						forma.setEsImpresionOriginal(null);	
						
						imprimirAutorizacion(con, forma, usuario, paciente, institucion, mapping, request);
					}	
				} else {
					//No se muestra popup de primera impresion
					forma.setMostrarPopupPrimeraImpresion(false);
					//Si existe la Entrega de la autorizacion se imprime el label 'Copia'
					forma.setEsImpresionOriginal(false);
					
					imprimirAutorizacion(con, forma, usuario, paciente, institucion, mapping, request);
				}
			} 
			//Si no existe Autorizacion Entidad Subcontratada o el estado es anulado
			else {
				//No se muestra popup de primera impresion
				forma.setMostrarPopupPrimeraImpresion(false);
				//No se muestra el label en la impresion
				forma.setEsImpresionOriginal(null);	
				
				imprimirAutorizacion(con, forma, usuario, paciente, institucion, mapping, request);
			}
			
		}
		
		if(!errores.isEmpty()) {
			saveErrors(request, errores);
		}
		
		return mapping.findForward(forward);
	}
	
	/**
	 * Metodo encargado de guardar los datos de la impresion original de la Autorizacion 
	 * 
	 * @param con
	 * @param request
	 * @param forma
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param institucion
	 * @return
	 * @author hermorhu
	 * @created 25-feb-2013
	 */
	public ActionForward accionGuardarDatosPrimeraImpresion(Connection con, HttpServletRequest request,	ConsultaAutorizacionesCapitacionSubDetalleForm forma, 
			ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, InstitucionBasica institucion) {
		
		ActionMessages errores = new ActionMessages();

		MessageResources mensajes = MessageResources.getMessageResources(PROPERTIES_DETALLE_CONSULTA);	
			
		if(UtilidadTexto.isEmpty(forma.getPersonaRecibeAutorizacion())) {
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, mensajes.getMessage("consultaAutorizacionesCapitacionSubDetalleForm.errorPersonaRecibeAutorizacionRequerido")));	
			forma.getDtoAutorizacionCapitacion().setAutorizacionEntrega(null);
			
		} else {
		
			try {
				ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
				
				forma.getDtoAutorizacionCapitacion().getAutorizacionEntrega().setPersonaRecibe(forma.getPersonaRecibeAutorizacion());
				forma.getDtoAutorizacionCapitacion().getAutorizacionEntrega().setObservaciones(forma.getObservacionesEntregaAutorizacion());
				
				//guarda los datos de entrega de la autorizacion original 
				manejoPacienteFacade.guardarEntregaAutorizacionEntidadSubContratadaOriginal(forma.getDtoAutorizacionCapitacion().getAutorizacionEntrega());
				
				//imprime la autorizacion
				imprimirAutorizacion(con, forma, usuario, paciente, institucion, mapping, request);
			} catch (IPSException ipse) {
				Log4JManager.error(ipse);
				errores.add("", new ActionMessage(ipse.getErrorCode().toString()));
			} catch (Exception e) {
				Log4JManager.error(e);
				errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, e.getMessage()));
			}
		}
			
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		
		return mapping.findForward(FORWARD_DET_AUTORIZACION_CAPITACION);	
	}
	
	/**
	 * Metodo encargado de cargar los datos para abrir el popup de Entrega de Autorizacion 
	 * 
	 * @param con
	 * @param request
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return
	 * @author hermorhu
	 * @created 21-feb-2013
	 */
	public ActionForward accionCargarPopupEntregaPrimeraImpresion (Connection con, HttpServletRequest request,
			ConsultaAutorizacionesCapitacionSubDetalleForm forma, ActionMapping mapping, UsuarioBasico usuario){
		
		forma.getDtoAutorizacionCapitacion().setAutorizacionEntrega(new AutorizacionEntregaDto());
		
		//carga de info necesaria para mostrar el popup de Entrega de autorizacion
		forma.getDtoAutorizacionCapitacion().getAutorizacionEntrega().setFechaEntrega(UtilidadFecha.getFechaActual(con));
		forma.getDtoAutorizacionCapitacion().getAutorizacionEntrega().setHoraEntrega(UtilidadFecha.getHoraActual(con));
		forma.getDtoAutorizacionCapitacion().getAutorizacionEntrega().setIdAutorizacionEntidadSub(forma.getDtoAutorizacionCapitacion().getAutorizacion());

		DtoUsuarioPersona usuarioPersona = new DtoUsuarioPersona();
		usuarioPersona.setLogin(usuario.getLoginUsuario());
		usuarioPersona.setNombreOrganizado(usuario.getNombreUsuario());
		forma.getDtoAutorizacionCapitacion().getAutorizacionEntrega().setUsuarioEntrega(usuarioPersona);
		
		forma.getDtoAutorizacionCapitacion().getAutorizacionEntrega().setObservaciones("");
		forma.getDtoAutorizacionCapitacion().getAutorizacionEntrega().setPersonaRecibe("");
		
		request.setAttribute("formaPopUpPrimeraImpresion", "ConsultaAutorizacionesCapitacionSubDetalleForm");
		
		return mapping.findForward(FORWARD_POPUP_PRIMERA_IMPRESION);	
	}
	
}
