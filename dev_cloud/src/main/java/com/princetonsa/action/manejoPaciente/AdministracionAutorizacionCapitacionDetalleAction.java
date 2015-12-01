package com.princetonsa.action.manejoPaciente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import util.facturacion.InfoCobertura;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.AdministracionAutorizacionCapitacionDetalleForm;
import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalArticulo;
import com.princetonsa.dto.capitacion.DTOBusquedaCierreTemporalServicio;
import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoArticulos;
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
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.bl.capitacion.facade.CapitacionFacade;
import com.servinte.axioma.bl.capitacion.impl.NivelAutorizacionMundo;
import com.servinte.axioma.bl.facturacion.impl.CoberturaMundo;
import com.servinte.axioma.bl.inventario.facade.InventarioFacade;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.bl.manejoPaciente.impl.AutorizacionCapitacionMundo;
import com.servinte.axioma.bl.manejoPaciente.interfaz.IAutorizacionCapitacionMundo;
import com.servinte.axioma.bl.ordenes.facade.OrdenesFacade;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.fabrica.inventario.InventarioDAOFabrica;
import com.servinte.axioma.dao.interfaz.inventario.IArticuloDAO;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IHistoAutorizacionCapitaSubDAO;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.facturacion.EntidadSubContratadaDto;
import com.servinte.axioma.dto.inventario.ClaseInventarioDto;
import com.servinte.axioma.dto.manejoPaciente.ArticuloAutorizadoCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionEntregaDto;
import com.servinte.axioma.dto.manejoPaciente.DatosPacienteAutorizacionDto;
import com.servinte.axioma.dto.manejoPaciente.ServicioAutorizadoCapitacionDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAmbulatoriaDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.dto.salascirugia.PeticionQxDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionIngresoEstancia.GeneradorReporteFormatoCapitacionAutorIngresoEstancia;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionMedicamentosInsumos.GeneradorReporteFormatoCapitacionAutorArticulos;
import com.servinte.axioma.generadorReporte.manejoPaciente.formatosCapitacion.formatosAutorizacionServicios.GeneradorReporteFormatoCapitacionAutorservicio;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempClaseInvArtMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierreTempNivelAteClaseInvArtMundo;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.AutorizacionesCapitacionSub;
import com.servinte.axioma.orm.AutorizacionesEntSubArticu;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.AutorizacionesIngreEstancia;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.orm.CierreTempClaseInvArt;
import com.servinte.axioma.orm.CierreTempGrupoServicio;
import com.servinte.axioma.orm.CierreTempNivAteClInvArt;
import com.servinte.axioma.orm.CierreTempNivelAteGruServ;
import com.servinte.axioma.orm.CierreTempNivelAtenArt;
import com.servinte.axioma.orm.CierreTempNivelAtenServ;
import com.servinte.axioma.orm.CierreTempServArt;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.EntidadesSubcontratadas;
import com.servinte.axioma.orm.HistoAutorizacionCapitaSub;
import com.servinte.axioma.orm.HistoAutorizacionIngEstan;
import com.servinte.axioma.orm.IngresosEstancia;
import com.servinte.axioma.orm.NivelAutorizacion;
import com.servinte.axioma.orm.Servicios;
import com.servinte.axioma.orm.Solicitudes;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.capitacion.CapitacionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.ICentroCostosServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempGrupoServicioServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAteGruServServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAtenArtServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempNivelAtenServServicio;
import com.servinte.axioma.servicio.interfaz.capitacion.ICierreTempServArtServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntidadesSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IServiciosServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionCapitacionSubServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionIngresoEstanciaServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntidadesSubServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IIngresosEstanciaServicio;

/**
 * Esta clase se encarga de de procesar las solicitudes de 
 * la administración de las autorizaciones de capitación por paciente
 * cargado en sesión
 * 
 * @author Angela Maria Aguirre
 * @since 29/12/2010
 */
public class AdministracionAutorizacionCapitacionDetalleAction extends Action {	

	private static int ESTADO_PENDIENTE_PRORROGA=0;
	private static int ESTADO_PENDIENTE_ANULACION=1;
	private static final String NOMBRE_ENTIDAD_OTRA = "Otra";
	private final String separado=" - ";
	private String propertiesDetalleAdministracion="com.servinte.mensajes.manejoPaciente.DetalleAdministracionAutorizacionForm";
	
	private static final String FORWARD_DET_AUTORIZACION_CAPITACION = "verDetalleAutorizacionCapitacion";
	private static final String FORWARD_DET_AUTORIZACION_INGESTANCIA = "verDetalleAutorizacionIngEstancia";
	private static final String FORWARD_POPUP_PRIMERA_IMPRESION = "mostrarPopUpPrimeraImpresion";
	private static final String KEY_ERROR_NO_ESPECIFIC="errors.notEspecific";
	private static final String KEY_ERROR_PARAM_GRAL_MANEJOPACIENTE = "errores.manejoPaciente.parametroGeneral";
	private static final String ESTADO_ANULADO = "Anulada(o)";
	
	/**
	 * 
	 * Este Método se encarga de ejecutar las acciones sobre las páginas
	 * de administración de las autorizaciones de capitación por paciente
	 * 
	 * 
	 * @param  ActionMapping mapping, HttpServletRequest request,
	 *         HttpServletResponse response
	 * @return ActionForward     
	 * @author Angela Maria Aguirre
	 *
	 */
	@Override
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request,HttpServletResponse response){
		
		ActionForward forward=null;
		Connection con=null;
		if (form instanceof AdministracionAutorizacionCapitacionDetalleForm) {
			
			AdministracionAutorizacionCapitacionDetalleForm forma = (AdministracionAutorizacionCapitacionDetalleForm)form;
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			UtilidadTransaccion.getTransaccion().begin();
			
			try{
				con=UtilidadBD.abrirConexion();
				forma.setNombreReporte("");
				forma.setAbrirPopPupAnulacion(ConstantesBD.acronimoNo);
				forma.setAbrirPopPupEntidadesSub(ConstantesBD.acronimoNo);
				forma.setAbrirPopPupProrroga(ConstantesBD.acronimoNo);
				forma.setAbrirPopUpConfirmarAutoEntSub(ConstantesBD.acronimoNo);
				forma.setAbrirPopUpModificacionDiasEstancia(ConstantesBD.acronimoNo);
				forma.setProcesoExitoso(false);
				
				forma.setMostrarPopupPrimeraImpresion(false);
				
				if(estado.equals("verDetallePaciente")){
					forma.setPaginaRedireccion("administrarAutorizacionCapitacionPaciente.do?estado=empezar");//MANTIS 247 no permitir anular N veces la misma autorización al volver al listado
					forward= empezar(con, request,forma, mapping,paciente,usuario);
					
				}else if(estado.equals("verDetalleRango")){
					forma.setPaginaRedireccion("administrarAutorizacionCapitacionRango.do?estado=consultar");//MANTIS 247 no permitir anular N veces la misma autorización al volver al listado
					forward= empezar(con, request,forma, mapping,paciente,usuario);
					
				}else if(estado.equals("recargarFechaVencimiento")){
					
					forward= calcularFechaVencimiento(request,forma, mapping,paciente,usuario);
					
				}else if(estado.equals("guardarAutorizacionIngEstancia")){
					
					forward= guardarAutorizacionIngEstancia(request,forma, mapping,paciente,usuario);
					
				}else if(estado.equals("validarTemporalIngEstancia") || estado.equals("validarTemporalCapitacion")){
					
					forward= validarAutorizacionTemporal(con, request,forma, mapping,paciente,usuario);
					
				}else if(estado.equals("realizarAutorizacion")){
					
					forward= realizarAutorizacionTemporal(con, request,forma,mapping,paciente,usuario);					
				}else if(estado.equals("validarProrrogaAutorizacion")){
					forma.setAbrirPopPupAnulacion(ConstantesBD.acronimoNo);
					forma.setAbrirPopPupEntidadesSub(ConstantesBD.acronimoNo);
					forma.setAbrirPopPupProrroga(ConstantesBD.acronimoNo);
					forma.setAbrirPopUpModificacionDiasEstancia(ConstantesBD.acronimoNo);
					forma.setAbrirPopUpConfirmarAutoEntSub(ConstantesBD.acronimoNo);
					forward= validarProrrogaAutorizacion(request,forma,mapping,paciente,usuario);
					
				}else if(estado.equals("mostrarPopUpProrroga")){
					
					forward= mapping.findForward("mostrarPopUpProrroga");	
					
				}else if(estado.equals("prorrogarAutorizacion")){
					
					forward= prorrogarAutorizacion(request, forma, mapping, paciente, usuario);
					
				}else if(estado.equals("validarAnulacionAutorizacionIngEstancia")){
					forward= validarAnulacionAutorizacionIngEstancia(request, forma, mapping, paciente, usuario);
					
				}else if(estado.equals("validarAnulacionAutorizacionCapitacion")){
					forma.setAbrirPopPupAnulacion(ConstantesBD.acronimoNo);
					forma.setAbrirPopPupEntidadesSub(ConstantesBD.acronimoNo);
					forma.setAbrirPopPupProrroga(ConstantesBD.acronimoNo);
					forma.setAbrirPopUpModificacionDiasEstancia(ConstantesBD.acronimoNo);
					forma.setAbrirPopUpConfirmarAutoEntSub(ConstantesBD.acronimoNo);
					forward= validarAnulacionAutorizacionCapitacion(con, request, forma, mapping, paciente, usuario);				
					
				}else if(estado.equals("anularAutorizacionCapitacion")){					
					forward= anularAutorizacionCapitacion(request, forma, mapping, usuario);
					
				}else if(estado.equals("anularAutorizacionIngEstancia")){					
					forward= anularAutorizacionIngresoEstancia(request, forma, mapping, usuario);
					
				}else if(estado.equals("mostrarPopUpAnulacion")){
					
					forward= mapping.findForward("mostrarPopUpAnulacion");	
					
				}else if(estado.equals("mostrarPopuPupEntidadesSub")){
					
					forward= mapping.findForward("mostrarPopUpEntidadesSub");				
				}
				else if(estado.equals("imprimirAutorizacion")){
					if(con == null){
					   request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					   forward= mapping.findForward("paginaError");
					}
					forward = validarImpresionAutorizacion(request, forma, mapping, paciente, usuario, con);
				}
				else if(estado.equals("autorizarEntidadSubcontratada")){
					if(realizarAutorizacionEntidadSubcontratada(request,forma,mapping,paciente,usuario)){
						forward= mapping.findForward("verDetalleAutorizacionCapitacion");
					}else{
						forward= mapping.findForward("mostrarPopUpConfirmarAutoEntidadesSub");
					}
				}else if(estado.equals("mostrarModificarDiasEstancia")){
					forward=mapping.findForward("mostrarPopUpModificarDiasEstancia");
				}else if(estado.equals("abrirModificarDiasEstancia")){
					forward=abrirModificarDiasEstancia(forma,mapping);
				}else if(estado.equals("cerrarModificarDiasEstancia")){
					forward=cerrarModificarDiasEstancia(forma,mapping);
				}else if(estado.equals("abrirConfirmarAutoEntSub")){
					forward=abrirConfirmacionAutoEntSub(con, request, forma, mapping, usuario);
				}else if(estado.equals("mostrarPopUpConfirmarAutoEntSub")){
					forward=mapping.findForward("mostrarPopUpConfirmarAutoEntidadesSub");
				}
				else if(estado.equals("cerrarConfirmarAutoEntSub")){
					forward=cerrarConfirmacionAutoEntSub(forma,mapping);
				}
				//hermorhu - MT5966
				else if(estado.equals("mostrarPopUpPrimeraImpresion")){
					forward = accionCargarPopupEntregaPrimeraImpresion(con, request, forma, mapping, usuario);
				}
				else if(estado.equals("guardarDatosPrimeraImpresion")){
					forward = accionGuardarDatosPrimeraImpresion(con, request, forma, mapping, paciente, usuario);
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
			finally{
				UtilidadBD.closeConnection(con);
			}
		}
		return forward;
	}
	
	
	/**
	 * Abre el popup de modificar dias de estancia
	 * 
	 * @param forma
	 * @param mapping
	 * @return 
	 * @author jeilones
	 * @created 14/08/2012
	 */
	private ActionForward abrirModificarDiasEstancia(AdministracionAutorizacionCapitacionDetalleForm forma, ActionMapping mapping) {
		forma.setAbrirPopPupAnulacion(ConstantesBD.acronimoNo);
		forma.setAbrirPopPupEntidadesSub(ConstantesBD.acronimoNo);
		forma.setAbrirPopPupProrroga(ConstantesBD.acronimoNo);
		forma.setAbrirPopUpConfirmarAutoEntSub(ConstantesBD.acronimoNo);
		forma.setAbrirPopUpModificacionDiasEstancia(ConstantesBD.acronimoSi);
		forma.setDiasEstanciaActualizados(forma.getDtoAutorizacionIngEstancia().getDiasEstanciaAutorizados());
		forma.getDtoAutorizacionIngEstancia().setNuevaFechaVencimiento(forma.getDtoAutorizacionIngEstancia().getFechaVencimiento());
		return mapping.findForward("verDetalleAutorizacionIngEstancia");
	}
	
	/**
	 * Abre el popup de modificar dias de estancia
	 * 
	 * @param forma
	 * @param mapping
	 * @return 
	 * @author jeilones
	 * @param usuario 
	 * @param con 
	 * @created 14/08/2012
	 */
	private ActionForward abrirConfirmacionAutoEntSub(Connection con,HttpServletRequest request, AdministracionAutorizacionCapitacionDetalleForm forma, ActionMapping mapping, UsuarioBasico usuario) {
		
		forma.setAbrirPopPupAnulacion(ConstantesBD.acronimoNo);
		forma.setAbrirPopPupEntidadesSub(ConstantesBD.acronimoNo);
		forma.setAbrirPopPupProrroga(ConstantesBD.acronimoNo);
		forma.setAbrirPopUpModificacionDiasEstancia(ConstantesBD.acronimoNo);
		
		if(validarGeneracionAutoEntSub(con, request, forma, usuario)){
			forma.setAbrirPopUpConfirmarAutoEntSub(ConstantesBD.acronimoSi);
		}else{
			forma.setAbrirPopUpConfirmarAutoEntSub(ConstantesBD.acronimoNo);
		}
		return mapping.findForward("verDetalleAutorizacionCapitacion"); 
	}
	
	public boolean validarGeneracionAutoEntSub(Connection con,HttpServletRequest request, AdministracionAutorizacionCapitacionDetalleForm forma,UsuarioBasico usuario){
		ActionMessages messages=new ActionMessages();
		MessageResources mensajes=MessageResources.getMessageResources(
				propertiesDetalleAdministracion);
		
		String consecutivoAutoEntSub = UtilidadBD.obtenerValorActualTablaConsecutivos(
			       con, ConstantesBD.nombreConsecutivoAutorizacionEntiSub, usuario.getCodigoInstitucionInt());
		
		if(consecutivoAutoEntSub==null||consecutivoAutoEntSub.trim().isEmpty()){
			messages.add("",new ActionMessage(
					mensajes.getMessage("detalleAdministracionAutorizacion.errorNoDefinidoConsecutivoAutorizacionEntidadSub")));
		}
		if(!messages.isEmpty()){
			saveErrors(request, messages);
		}
		return messages.isEmpty();
	}
	/**
	 * Cierra el popup de modificar dias de estancia
	 * 
	 * @param forma
	 * @param mapping
	 * @return
	 * @author jeilones
	 * @created 14/08/2012
	 */
	private ActionForward cerrarModificarDiasEstancia(AdministracionAutorizacionCapitacionDetalleForm forma, ActionMapping mapping) {
		forma.setAbrirPopPupAnulacion(ConstantesBD.acronimoNo);
		forma.setAbrirPopPupEntidadesSub(ConstantesBD.acronimoNo);
		forma.setAbrirPopPupProrroga(ConstantesBD.acronimoNo);
		forma.setAbrirPopUpModificacionDiasEstancia(ConstantesBD.acronimoNo);
		forma.setAbrirPopUpConfirmarAutoEntSub(ConstantesBD.acronimoNo);
		return mapping.findForward("verDetalleAutorizacionIngEstancia");
	}
	
	/**
	 * Cierra el popup de confirmacion de autorizacion de entidad subcontratada
	 * 
	 * @param forma
	 * @param mapping
	 * @return
	 * @author jeilones
	 * @created 15/08/2012
	 */
	private ActionForward cerrarConfirmacionAutoEntSub(AdministracionAutorizacionCapitacionDetalleForm forma, ActionMapping mapping) {
		forma.setAbrirPopPupAnulacion(ConstantesBD.acronimoNo);
		forma.setAbrirPopPupEntidadesSub(ConstantesBD.acronimoNo);
		forma.setAbrirPopPupProrroga(ConstantesBD.acronimoNo);
		forma.setAbrirPopUpModificacionDiasEstancia(ConstantesBD.acronimoNo);
		forma.setAbrirPopUpConfirmarAutoEntSub(ConstantesBD.acronimoNo);
		return mapping.findForward("verDetalleAutorizacionCapitacion");
	}


	/**
	 * 
	 * Este Método se encarga de validar una autorización de capitación
	 * para su anulación
	 * 
	 * @param HttpServletRequest request,
	 *		  AdministracionAutorizacionCapitacionDetalleForm forma,
	 *		  ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario
	 * @return ActionForward			
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward anularAutorizacionIngresoEstancia(HttpServletRequest request,
			AdministracionAutorizacionCapitacionDetalleForm forma,
			ActionMapping mapping,UsuarioBasico usuario)throws Exception{
		
		ActionMessages errores = new ActionMessages();
		MessageResources mensajes=MessageResources.getMessageResources(
		propertiesDetalleAdministracion);	
		boolean procesoExitoso = false;
			
		if(UtilidadTexto.isEmpty(forma.getMotivoAnulacion())){
			
			errores.add("El motivo de anulación es requerido", new ActionMessage("errors.notEspecific", 
					mensajes.getMessage("detalleAdministracionAutorizacion.errorMotivoAnulacionRequerido")));	
		}else{
			IAutorizacionIngresoEstanciaServicio autorizacionServicio =
				ManejoPacienteServicioFabrica.crearAutorizacionIngresoEstanciaServicio();
			
			DTOAutorizacionIngresoEstancia dtoAutorizacion = forma.getDtoAutorizacionIngEstancia();					
			AutorizacionesIngreEstancia autorizacion = autorizacionServicio.buscarAutorizacionIngresoEstanciaPorID(dtoAutorizacion.getCodigoPk());
			
			HistoAutorizacionIngEstan historico = new HistoAutorizacionIngEstan();
			historico.setAutorizacionesIngreEstancia(autorizacion);
			historico.setFechaInicioAutorizacion(autorizacion.getFechaInicioAutorizacion());
			historico.setConsecutivoAdmision(autorizacion.getConsecutivoAdmision());
			historico.setDiasEstanciaAutorizados(autorizacion.getDiasEstanciaAutorizados());
			historico.setUsuarioContacta(autorizacion.getUsuarioContacta());
			historico.setCargoUsuarioContacta(autorizacion.getCargoUsuContacta());
			if(autorizacion.getConvenios()!=null){
				historico.setConvenioRecobro(autorizacion.getConvenios().getCodigo());
			}
			historico.setOtroConvenioRecobro(autorizacion.getOtroConvenioRecobro());
			historico.setIndicativoTemporal(autorizacion.getIndicativoTemporal());
			if(autorizacion.getCentrosCosto()!=null){
				historico.setCentroCostoSolicitante(autorizacion.getCentrosCosto().getCodigo());
			}
			historico.setEstado(autorizacion.getEstado());
			
			Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
			String horaActual = UtilidadFecha.getHoraActual();
			
			historico.setFechaModifica(fechaActual);
			historico.setHoraModifica(horaActual);		
			
			Usuarios usuarioModifica = new Usuarios();
			usuarioModifica.setLogin(usuario.getLoginUsuario());
			
			historico.setUsuarios(usuarioModifica);
			historico.setAccionRealizada(
					ConstantesIntegridadDominio.acronimoEstadoAnulado);
			historico.setObservaciones(forma.getMotivoAnulacion());
			
			Set<HistoAutorizacionIngEstan> setHistorico = new HashSet<HistoAutorizacionIngEstan>(0);
			setHistorico.add(historico);					
			autorizacion.setHistoAutorizacionIngEstans(setHistorico);
			autorizacion.setEstado(ConstantesIntegridadDominio.acronimoEstadoAnulado);
			procesoExitoso = autorizacionServicio.actualizarAutorizacionIngresoEstancia(autorizacion);						
		}
		
		if(procesoExitoso){
			String estado=(String)ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoEstadoAnulado);
			forma.getDtoAutorizacionIngEstancia().setEstado(estado);
			forma.setMostrarBotonAnular(false);
			forma.setMostrarBotonProrroga(false);
			//forma.getDtoAutorizacionIngEstancia().setIndicativoTemporal(ConstantesBD.acronimoNoChar);
		}
		
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		
		forma.setProcesoExitoso(procesoExitoso);
		forma.setAbrirPopPupAnulacion(ConstantesBD.acronimoNo);
		
		return mapping.findForward("verDetalleAutorizacionIngEstancia");
	}
	
	/**
	 * 
	 * Este Método se encarga de validar una autorización de capitación
	 * para su anulación
	 * 
	 * @param HttpServletRequest request,
	 *		  AdministracionAutorizacionCapitacionDetalleForm forma,
	 *		  ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario
	 * @return ActionForward			
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward anularAutorizacionCapitacion(HttpServletRequest request,
			AdministracionAutorizacionCapitacionDetalleForm forma,
			ActionMapping mapping,UsuarioBasico usuario)throws Exception{
		
		ActionMessages errores = new ActionMessages();
		MessageResources mensajes=MessageResources.getMessageResources(
		propertiesDetalleAdministracion);	
		boolean procesoExitoso = false;
			
		if(UtilidadTexto.isEmpty(forma.getMotivoAnulacion())){
			
			errores.add("El motivo de anulación es requerido", new ActionMessage("errors.notEspecific", 
					mensajes.getMessage("detalleAdministracionAutorizacion.errorMotivoAnulacionRequerido")));	
		}else{
			DTOAutorEntidadSubcontratadaCapitacion dtoAutorEntSub=forma.getDtoAutorizacionCapitacion();
			IAutorizacionCapitacionSubServicio capitacionServicio = 
				ManejoPacienteServicioFabrica.crearAutorizacionCapitacionSubServicio();
			IAutorizacionesEntidadesSubServicio entSubServicio = 
				ManejoPacienteServicioFabrica.crearAutorizacionEntidadesSubServicio();	
			
			AutorizacionesEntidadesSub autorizacionEntSub = entSubServicio.obtenerAutorizacionesEntidadesSubPorId(
					dtoAutorEntSub.getAutorizacion());
			
			AutorizacionesCapitacionSub autorizacionCapitacion =
				capitacionServicio.findById(dtoAutorEntSub.getAutorCapitacion().getCodigoPK());
					
			Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
			String horaActual = UtilidadFecha.getHoraActual();
			
			autorizacionEntSub.setEstado(ConstantesIntegridadDominio.acronimoEstadoAnulado);
			autorizacionEntSub.setFechaAnulacion(fechaActual);
			autorizacionEntSub.setHoraAnulacion(horaActual);
			autorizacionEntSub.setMotivoAnulacion(forma.getMotivoAnulacion());
			
			Usuarios usuarioAnula = new Usuarios();
			usuarioAnula.setLogin(usuario.getLoginUsuario());
			
			autorizacionEntSub.setUsuarios(usuarioAnula);
			procesoExitoso =  entSubServicio.actualizarAutorizacionEntidadSub(autorizacionEntSub);
			
			if(procesoExitoso){
				
				procesoExitoso = guardarHistorialAutorCapitacionSub(autorizacionCapitacion, usuario, autorizacionEntSub, 
						ConstantesIntegridadDominio.acronimoEstadoAnulado,forma);
				
				if(procesoExitoso){
					String estado=(String)ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoEstadoAnulado);
					
					forma.getDtoAutorizacionCapitacion().setEstado(estado);
					// Se setean estos valores para que no se muestre los botones de prorrogar, anular y autorizar temporal
					forma.setMostrarBotonAnular(false);
					forma.setMostrarBotonProrroga(false);
					forma.setMostrarBotonAutorizarEntSub(false);
					
					//La disminucion de cierre se hace sobre la tabal temporal al dia actual, 
					//no implica que la autorizacion se haya realziado el mismo dia
					//forma.getDtoAutorizacionCapitacion().getAutorCapitacion().setIndicativoTemporal(ConstantesBD.acronimoNoChar);
					//Calendar fechaAutorizacion = Calendar.getInstance();
					//Calendar fechaActualCal = Calendar.getInstance();
					//fechaAutorizacion.setTime(dtoAutorEntSub.getFechaAutorizacion());
					//Se valida si la fecha de anualación corresponde con al fecha de autorizacion para disminuir 
					//el valor de los cierres temporales
					/*if((fechaAutorizacion.get(Calendar.YEAR) == fechaActualCal.get(Calendar.YEAR))
							&& (fechaAutorizacion.get(Calendar.MONTH) == fechaActualCal.get(Calendar.MONTH))
							&& (fechaAutorizacion.get(Calendar.DAY_OF_MONTH) == fechaActualCal.get(Calendar.DAY_OF_MONTH))){*/
						//Se resta de la Temporal de Cierre de Ordenes Medicas el valor de la Autorización Anulada
						disminuirAcumuladoCierreTemporal(forma);
					//}
				}
			}
		}
		
		forma.setProcesoExitoso(procesoExitoso);
		forma.setAbrirPopPupAnulacion(ConstantesBD.acronimoNo);
		if(errores!=null && !errores.isEmpty()){
     		saveErrors(request, errores);
     	}
		
		return mapping.findForward("verDetalleAutorizacionCapitacion");		
	}
	
	/**
	 * 
	 * Este Método se encarga de validar una autorización de capitación
	 * para su anulación
	 * 
	 * @param HttpServletRequest request,
	 *		  AdministracionAutorizacionCapitacionDetalleForm forma,
	 *		  ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario
	 * @return ActionForward			
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward validarAnulacionAutorizacionCapitacion(Connection con, HttpServletRequest request,
			AdministracionAutorizacionCapitacionDetalleForm forma,
			ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario){
		
		ActionMessages errores = new ActionMessages();
		MessageResources mensajes=MessageResources.getMessageResources(
		propertiesDetalleAdministracion);	
		
		/**DCU 1098 v1.4**/
		
		try {
			
			
			if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SERVICIOS)
					||forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SERVICIOS_ING_EST)){

				validarEstadosServicios(forma, errores, mensajes,ESTADO_PENDIENTE_ANULACION);
			}else{
				if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_ARTICULOS)
						||forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_ARTICULOS_ING_EST)){
					
					validarEstadosArticulos(forma, errores, mensajes);
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			Log4JManager.error(e);
			errores.add("", new ActionMessage("errors.notEspecific", e.getMessage()));
			
		} catch (IPSException e) {
			errores.add("", new ActionMessage(e.getErrorCode().toString()));
		}
		
		/**FIN DCU 1098 v1.4**/
		
		if(errores.isEmpty()){
			forma.setAbrirPopPupAnulacion(ConstantesBD.acronimoSi);
     	}else{
     		saveErrors(request, errores);
     	}
		return mapping.findForward("verDetalleAutorizacionCapitacion");
	}
	
	
	/**
	 * 
	 * Este Método se encarga de validar una autorización de ingreso estancia
	 * para su anulación
	 * 
	 * @param HttpServletRequest request,
	 *		  AdministracionAutorizacionCapitacionDetalleForm forma,
	 *		  ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario
	 * @return ActionForward			
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward validarAnulacionAutorizacionIngEstancia(HttpServletRequest request,
			AdministracionAutorizacionCapitacionDetalleForm forma,
			ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario){
		
		ActionMessages errores = new ActionMessages();
		MessageResources mensajes=MessageResources.getMessageResources(
		propertiesDetalleAdministracion);	
		DTOAutorizacionIngresoEstancia dtoAutorIngEstancia = forma.getDtoAutorizacionIngEstancia();
				
		forma.setAbrirPopPupAnulacion(ConstantesBD.acronimoNo);
		forma.setAbrirPopPupEntidadesSub(ConstantesBD.acronimoNo);
		forma.setAbrirPopPupProrroga(ConstantesBD.acronimoNo);
		forma.setAbrirPopUpModificacionDiasEstancia(ConstantesBD.acronimoNo);
		forma.setAbrirPopUpConfirmarAutoEntSub(ConstantesBD.acronimoNo);
		
		boolean cumple=true;
		
		ManejoPacienteFacade manejoPacienteFacade=new ManejoPacienteFacade();
		try {
			List<AutorizacionesEntidadesSub>autorizacionesEntSub=manejoPacienteFacade.obtenerAutorizacionEntSubDeIngEstancia(
					dtoAutorIngEstancia.getCodigoPk(), true);
			for(AutorizacionesEntidadesSub autoEntSub:autorizacionesEntSub){
				/*Se valida si la autorización de Servicios/Insumos se encuentra en  estado diferente a ANULADA*/
				if(!autoEntSub.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado)){
					cumple=false;
					break;
				}
			}
			if(cumple){
				forma.setAbrirPopPupAnulacion(ConstantesBD.acronimoSi);
			}else{
				errores.add("La autorizacion tiene asociada autorizacion de capitación asocidada", new ActionMessage("errors.notEspecific", 
						mensajes.getMessage("detalleAdministracionAutorizacion.errorAnulacionAutorIngresoEstancia")));
			}
		} catch (IPSException e) {
			// TODO Auto-generated catch block
			errores.add("", new ActionMessage(e.getErrorCode().toString()));
		}
		
		
		if(errores!=null && !errores.isEmpty()){
     		saveErrors(request, errores);
     	}
		
		return mapping.findForward("verDetalleAutorizacionIngEstancia");
	}
	
	
	
	/**
	 * 
	 * Este Método se encarga de prorrogar una autorización de 
	 * capitación con orden asociada
	 * 
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward prorrogarAutorizacion(HttpServletRequest request,
			AdministracionAutorizacionCapitacionDetalleForm forma,
			ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario)throws Exception{
				
		ActionMessages errores = new ActionMessages();
		MessageResources mensajes=MessageResources.getMessageResources(
		propertiesDetalleAdministracion);		
		boolean esServicio =false;
		boolean procesoExito=false;
		if(!Utilidades.isEmpty(forma.getListaServicios())){
			esServicio = true;
		}
		if(esServicio){
			if(UtilidadTexto.isEmpty(forma.getFechaProrrogaServicio())){
				errores.add("", new ActionMessage("errors.notEspecific", 
						mensajes.getMessage("detalleAdministracionAutorizacion.errorFechaProrrogaServicio")));	
			}
		}else{			
			if(UtilidadTexto.isEmpty(forma.getFechaProrrogaArticulo())){
				errores.add("", new ActionMessage("errors.notEspecific", 
						mensajes.getMessage("detalleAdministracionAutorizacion.errorFechaProrrogaArticilo")));	
			}			
		}
		
		if(errores.isEmpty()){
			String paramDiasProrroga = null;
     		
     		
     		String fechaVencimientoProrrogaString=null;
     		//UtilidadFecha.conversionFormatoFechaAAp(fechaVencimientoProrroga);
     		if(esServicio){
     		
     			paramDiasProrroga= ValoresPorDefecto.getDiasCalcularFechaVencAutorizacionServicio(
						usuario.getCodigoInstitucionInt());
     			
     			Date fechaVencimientoProrroga =  UtilidadFecha.conversionFormatoFechaStringDate(
    					UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(),
    							Integer.valueOf(paramDiasProrroga).intValue(), false)); 
     			
     			fechaVencimientoProrrogaString=UtilidadFecha.conversionFormatoFechaAAp(fechaVencimientoProrroga);
     			
     			if((UtilidadFecha.esFechaMenorQueOtraReferencia(
     					forma.getFechaProrrogaServicio()
     					,UtilidadFecha.getFechaActual()))
     					||!(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
     	    					forma.getFechaProrrogaServicio(),fechaVencimientoProrrogaString))){
    				errores.add("Fecha de prórroga ingresada no permitida", new ActionMessage("errors.notEspecific", 
    						mensajes.getMessage("detalleAdministracionAutorizacion.errorFechaNuevaProrroga").replace("{0}", fechaVencimientoProrrogaString)));
    			}
     			
     			else{
    				procesoExito = actualizarProrrogaAutorizacionCapitacion(forma,forma.getFechaProrrogaServicio(),usuario);
    			}
     		}else{
     			
     			paramDiasProrroga= ValoresPorDefecto.getDiasCalcularFechaVencAutorizacionArticulo(
						usuario.getCodigoInstitucionInt());
     			
     			Date fechaVencimientoProrroga =  UtilidadFecha.conversionFormatoFechaStringDate(
    					UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(),
    							Integer.valueOf(paramDiasProrroga).intValue(), false)); 
     			
     			fechaVencimientoProrrogaString=UtilidadFecha.conversionFormatoFechaAAp(fechaVencimientoProrroga);
     			
     			if((UtilidadFecha.esFechaMenorQueOtraReferencia(
     					forma.getFechaProrrogaArticulo()
     					,UtilidadFecha.getFechaActual()))
     					||!(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
     							forma.getFechaProrrogaArticulo(),fechaVencimientoProrrogaString))){
    				errores.add("Fecha de prórroga ingresada no permitida", new ActionMessage("errors.notEspecific", 
    						mensajes.getMessage("detalleAdministracionAutorizacion.errorFechaNuevaProrroga").replace("{0}", fechaVencimientoProrrogaString)));
    			}
     			else{
    				procesoExito = actualizarProrrogaAutorizacionCapitacion(forma,forma.getFechaProrrogaArticulo(),usuario);
    			}
     		}
     	}
		
		if(!errores.isEmpty()){
			saveErrors(request, errores);
     	}
		
		forma.setProcesoExitoso(procesoExito);
		forma.setAbrirPopPupProrroga(ConstantesBD.acronimoNo);
		return mapping.findForward("verDetalleAutorizacionCapitacion");	
	}
	
	
	/**
	 * 
	 * Este Método se encarga de guardar el registro de la autorización de 
	 * entidad subcontratada con la nueva fecha de vencimiento
	 * @param AdministracionAutorizacionCapitacionDetalleForm forma,
	 *		  Date fechaVencimiento,UsuarioBasico usuario
	 * @return  boolean
	 * @author Angela Maria Aguirre
	 *
	 */
	private boolean actualizarProrrogaAutorizacionCapitacion(AdministracionAutorizacionCapitacionDetalleForm forma,
			String fechaVencimiento,UsuarioBasico usuario)throws Exception{
		
		boolean procesoExitoso = false;
		DTOAutorEntidadSubcontratadaCapitacion dtoAutorEntSub=forma.getDtoAutorizacionCapitacion();
		IAutorizacionCapitacionSubServicio capitacionServicio = 
			ManejoPacienteServicioFabrica.crearAutorizacionCapitacionSubServicio();
		IAutorizacionesEntidadesSubServicio entSubServicio = 
			ManejoPacienteServicioFabrica.crearAutorizacionEntidadesSubServicio();	
		
		AutorizacionesEntidadesSub autorizacionEntSub = entSubServicio.obtenerAutorizacionesEntidadesSubPorId(
				dtoAutorEntSub.getAutorizacion());
		
		AutorizacionesCapitacionSub autorizacionCapitacion =
			capitacionServicio.findById(dtoAutorEntSub.getAutorCapitacion().getCodigoPK());
				
		autorizacionEntSub.setFechaVencimiento(UtilidadFecha.conversionFormatoFechaStringDate(fechaVencimiento));
		
		procesoExitoso =  entSubServicio.actualizarAutorizacionEntidadSub(autorizacionEntSub);
		if(procesoExitoso){
			procesoExitoso = guardarHistorialAutorCapitacionSub(autorizacionCapitacion, usuario, autorizacionEntSub, 
					ConstantesIntegridadDominio.acronimoAccionProrrogar,forma);
			
			forma.getDtoAutorizacionCapitacion().setFechaVencimiento(UtilidadFecha.conversionFormatoFechaStringDate(fechaVencimiento));
			
		}	
		return procesoExitoso;
		
	}
	
	/**
	 * 
	 * Este Método se encarga de realizar las validaciones para prorrogar una autorización de 
	 * capitación con orden asociada
	 * 
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward validarProrrogaAutorizacion(HttpServletRequest request,
			AdministracionAutorizacionCapitacionDetalleForm forma,
			ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario){
		
		ActionMessages errores = new ActionMessages();
		MessageResources mensajes=MessageResources.getMessageResources(
		propertiesDetalleAdministracion);		
		
		/**DCU 1098 v1.4**/
		
		try {
						
			if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SERVICIOS)
					||forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SERVICIOS_ING_EST)){
				
				validarEstadosServicios(forma, errores, mensajes,ESTADO_PENDIENTE_PRORROGA);
				if(errores.isEmpty()){
					String paramDiasMaxProrrogaAutoServicio = ValoresPorDefecto.getDiasMaxProrrogaAutorizacionServicio(
							usuario.getCodigoInstitucionInt());
					
					if(!UtilidadTexto.isEmpty(paramDiasMaxProrrogaAutoServicio)
							&&Integer.valueOf(paramDiasMaxProrrogaAutoServicio).intValue()>0){
						
						String fechaVencimientoDiasAutorServicio = UtilidadFecha.incrementarDiasAFecha(
								UtilidadFecha.conversionFormatoFechaAAp(forma.getDtoAutorizacionCapitacion().getFechaVencimiento()),Integer.valueOf(
										paramDiasMaxProrrogaAutoServicio).intValue(),false);
						//Si ya vencio la fecha max para hacer prorrogas
						//MT 5600
						if (!UtilidadFecha.getFechaActual().equals(fechaVencimientoDiasAutorServicio)){
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(
									fechaVencimientoDiasAutorServicio,
									UtilidadFecha.getFechaActual())){
								//Colocar el mensaje nuevo que diga que ya no se pueden hacer prorrogas
								errores.add("No se ha definido parametro dias maximo de prorroga", new ActionMessage("errors.notEspecific", 
										mensajes.getMessage("detalleAdministracionAutorizacion.errorSuperaDiasMaxProrrogacionAutorizacionServicios")));
							}
						}
						
					}else{
						errores.add("No se ha definido parametro dias maximo de prorroga", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("detalleAdministracionAutorizacion.errorDiasMaxProrrogaServicios")));
					}
					
					String paramDiasFechaVtoAutorServicio = ValoresPorDefecto.getDiasCalcularFechaVencAutorizacionServicio(
							usuario.getCodigoInstitucionInt());
					
					if(!UtilidadTexto.isEmpty(paramDiasFechaVtoAutorServicio)){
						
						String fechaVencimientoDiasAutorServicio = UtilidadFecha.incrementarDiasAFecha(
								UtilidadFecha.getFechaActual(),Integer.valueOf(
										paramDiasFechaVtoAutorServicio).intValue(),false);
						
						forma.setFechaProrrogaServicio(fechaVencimientoDiasAutorServicio);
					}
				}
			}else{
				if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_ARTICULOS)
						||forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_ARTICULOS_ING_EST)){
					
					validarEstadosArticulos(forma, errores, mensajes);
					
					if(errores.isEmpty()){
						String paramDiasMaxProrrogaAutoArticulo = ValoresPorDefecto.getDiasMaxProrrogaAutorizacionArticulo(
								usuario.getCodigoInstitucionInt());
						
						if(!UtilidadTexto.isEmpty(paramDiasMaxProrrogaAutoArticulo)
								&&Integer.valueOf(paramDiasMaxProrrogaAutoArticulo).intValue()>0){
							
							String fechaVencimientoDiasAutorArticulo = UtilidadFecha.incrementarDiasAFecha(
									UtilidadFecha.conversionFormatoFechaAAp(forma.getDtoAutorizacionCapitacion().getFechaVencimiento()),Integer.valueOf(
											paramDiasMaxProrrogaAutoArticulo).intValue(),false);
							//Si ya vencio la fecha max para hacer prorrogas
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(
									fechaVencimientoDiasAutorArticulo,
									UtilidadFecha.getFechaActual())){
								//Colocar el mensaje nuevo que diga que ya no se pueden hacer prorrogas
								errores.add("No se ha definido parametro dias maximo de prorroga", new ActionMessage("errors.notEspecific", 
										mensajes.getMessage("detalleAdministracionAutorizacion.errorSuperaDiasMaxProrrogacionAutorizacionArticulos")));
							}
						}else{
							errores.add("No se ha definido parametro dias maximo de prorroga", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("detalleAdministracionAutorizacion.errorDiasMaxProrrogaArticulos")));
						}
						
						String paramDiasFechaVtoAutorArticulo = ValoresPorDefecto.getDiasCalcularFechaVencAutorizacionArticulo(
								usuario.getCodigoInstitucionInt());
						
						if(!UtilidadTexto.isEmpty(paramDiasFechaVtoAutorArticulo)){
							
							String fechaVencimientoDiasAutorArticulo = UtilidadFecha.incrementarDiasAFecha(
									UtilidadFecha.getFechaActual(),Integer.valueOf(
											paramDiasFechaVtoAutorArticulo).intValue(),false);
							
							forma.setFechaProrrogaArticulo(fechaVencimientoDiasAutorArticulo);
						}
					}
					
					
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			Log4JManager.error(e);
			errores.add("", new ActionMessage("errors.notEspecific", e.getMessage()));
			
		} catch (IPSException e) {
			errores.add("", new ActionMessage(e.getErrorCode().toString()));
		}
		
		/**FIN DCU 1098 v1.4**/
		
		
		
		
		
		if(errores.isEmpty()){	
			forma.setAbrirPopPupProrroga(ConstantesBD.acronimoSi);
		}else{
     		saveErrors(request, errores);
     	}
		return mapping.findForward("verDetalleAutorizacionCapitacion");	
	}
	
	
	/**
	 * Valida que los estados no esten respondidos
	 * 
	 * @param forma
	 * @param errores
	 * @param mensajes
	 * @throws NumberFormatException
	 * @throws IPSException
	 * @author jeilones
	 * @created 22/08/2012
	 */
	private void validarEstadosArticulos(
			AdministracionAutorizacionCapitacionDetalleForm forma,
			ActionMessages errores,
			MessageResources mensajes) throws NumberFormatException, IPSException {
		
		boolean esRespondido=false;
		ManejoPacienteFacade manejoPacienteFacade=new ManejoPacienteFacade();
		
		List<DtoArticulosAutorizaciones>listaArticulos=forma.getListaArticulos();
		for(DtoArticulosAutorizaciones articulo:listaArticulos){
			
			/*
			 * Si alguno de los servicios ha sido solicitado, ninguna solicitud puede tener en historia clinita los estados:
			 * Respondida , Interpretada, Toma de muestra o  En proceso
			 */
			if(articulo.getTipoAutorizacion()==ConstantesIntegridadDominio.TIPO_AUTORIZACION_SOLICITUD
					||articulo.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_CARGOS_DIRECTOS){
				Solicitudes solicitud=manejoPacienteFacade.obtenerSolicitudPorId(Integer.valueOf(""+articulo.getCodigoOrdenSolPet()));
				if(solicitud!=null ){
					int estadoSolicitud=solicitud.getEstadoHistoriaClinica();
					esRespondido=estadoSolicitud==ConstantesBD.codigoEstadoHCDespachada
							||estadoSolicitud==ConstantesBD.codigoEstadoHCAdministrada;
					if(esRespondido){
						errores.add("El medicamento o insumo ya fué respondido", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("detalleAdministracionAutorizacion.errorArticuloRespondido")));
						break;
					}
				}
			}else{
				if(articulo.getTipoAutorizacion()==ConstantesIntegridadDominio.TIPO_AUTORIZACION_ORDEN_AMB){
					//validar si tiene registro de entrega de medicamentos
					List<AutorizacionesEntSubArticu> listaAutorizacionesEntSubArticu=manejoPacienteFacade.obtenerAutorizacionEntSubArticulo(forma.getDtoAutorizacionCapitacion().getCodigoAutorizacionEntSub(), 
							articulo.getCodigoArticulo().intValue(), true);
					for(AutorizacionesEntSubArticu autorizacionesEntSubArticu:listaAutorizacionesEntSubArticu){
						if(autorizacionesEntSubArticu!=null
								&&!autorizacionesEntSubArticu.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoPendiente)){
							errores.add("El medicamento o insumo ya fué respondido", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("detalleAdministracionAutorizacion.errorArticuloRespondido")));
							break;
						}
					}
				}
			}
		}
	}


	/**
	 * Valida que los estados no esten respondidos
	 * 
	 * @param forma
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param errores
	 * @param mensajes
	 * @throws NumberFormatException
	 * @throws IPSException
	 * @author jeilones
	 * @param estado 
	 * @created 22/08/2012
	 */
	private void validarEstadosServicios(AdministracionAutorizacionCapitacionDetalleForm forma,
			ActionMessages errores,MessageResources mensajes, int estado) throws NumberFormatException, IPSException {
		
		boolean esRespondido=false;
		ManejoPacienteFacade manejoPacienteFacade=new ManejoPacienteFacade();
		
		List<DtoServiciosAutorizaciones>listaServicios=forma.getListaServicios();
		for(DtoServiciosAutorizaciones servicio:listaServicios){
			/*
			 * Si alguno de los servicios ha sido solicitado, ninguna solicitud puede tener en historia clinita los estados:
			 * Respondida , Interpretada, Toma de muestra o  En proceso
			 */
			if(servicio.getTipoAutorizacion()==ConstantesIntegridadDominio.TIPO_AUTORIZACION_SOLICITUD
					||servicio.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_CARGOS_DIRECTOS){
				Solicitudes solicitud=manejoPacienteFacade.obtenerSolicitudPorId(Integer.valueOf(""+servicio.getCodigoOrdenSolPet()));
				if(solicitud!=null ){
					int estadoSolicitud=solicitud.getEstadoHistoriaClinica();
					esRespondido=estadoSolicitud==ConstantesBD.codigoEstadoHCRespondida
							||estadoSolicitud==ConstantesBD.codigoEstadoHCInterpretada
							||estadoSolicitud==ConstantesBD.codigoEstadoHCTomaDeMuestra
							||estadoSolicitud==ConstantesBD.codigoEstadoHCEnProceso;
					if(esRespondido){
						errores.add("El servicio ya fué respondido", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("detalleAdministracionAutorizacion.errorServicioRespondido")));
						break;
					}
				}
			}else{
				/*
				 * Si alguno de los servicios ha sido ordenado, ninguna orden puede tener estados:
				 * Respondida 
				 */
				if(servicio.getTipoAutorizacion()==ConstantesIntegridadDominio.TIPO_AUTORIZACION_ORDEN_AMB){
					OrdenAmbulatoriaDto ordenAmb=manejoPacienteFacade.obtenerEstadoOrdenesAmbulatoriasPorId(servicio.getCodigoOrdenSolPet());
					esRespondido=ordenAmb.getCodigoEstado().intValue()==ConstantesBD.codigoEstadoOrdenAmbulatoriaRespondida;
					if(esRespondido){
						errores.add("El servicio ya fué respondido", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("detalleAdministracionAutorizacion.errorServicioRespondido")));
						break;
					}
				}else{
					/*
					 * Si la Peticion NO esta en estado Pendiente, Programada o Reprogramada se debe mostrar el siguiente mensaje y cancelar el proceso:
					 * */
					if(servicio.getTipoAutorizacion()==ConstantesIntegridadDominio.TIPO_AUTORIZACION_PETICION){
						PeticionQxDto peticionQx=manejoPacienteFacade.obtenerEstadoPeticionQxPorId(servicio.getCodigoOrdenSolPet());
						esRespondido=peticionQx.getCodigoEstado()==ConstantesBD.codigoEstadoPeticionAtendida
								||peticionQx.getCodigoEstado()==ConstantesBD.codigoEstadoPeticionAnulada;
						if(esRespondido){
							if(estado==ESTADO_PENDIENTE_PRORROGA){
								errores.add("El servicio ya fué respondido", new ActionMessage("errors.notEspecific", 
									mensajes.getMessage("detalleAdministracionAutorizacion.errorPeticionAtendidaOAnuladaProrroga").replaceAll("{0}", peticionQx.getNombreEstado())));
							}else{
								if(estado==ESTADO_PENDIENTE_ANULACION){
									errores.add("El servicio ya fué respondido", new ActionMessage("errors.notEspecific", 
										mensajes.getMessage("detalleAdministracionAutorizacion.errorPeticionAtendidaOAnuladaAnulacion").replaceAll("{0}", peticionQx.getNombreEstado())));
								}
							}
							break;
						}
					}
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
	 * @author, Angela Maria Aguirre
	 * @throws SQLException 
	 *
	 */
	public ActionForward empezar(Connection con, HttpServletRequest request,
			AdministracionAutorizacionCapitacionDetalleForm forma,
			ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario ) throws SQLException{
		
		ActionMessages errores=new ActionMessages();
		
		IAutorizacionIngresoEstanciaServicio autorizacionIngEstServicio =
			ManejoPacienteServicioFabrica.crearAutorizacionIngresoEstanciaServicio();
				
		ManejoPacienteFacade manejoPacienteFacade = null;
				
		DTOAutorizacionIngresoEstancia dtoAutorIngEstancia = null;
		DTOAutorEntidadSubcontratadaCapitacion dtoAutorEntSub=null;
		
		forma.reset();	
		forma.setNombreUsuarioAnula(usuario.getNombreUsuario());
		forma.setFechaActual(UtilidadFecha.getFechaActual());
		
		if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_INGRESO_ESTANCIA)){
			
			dtoAutorIngEstancia = new DTOAutorizacionIngresoEstancia();
			dtoAutorIngEstancia.setCodigoPk(forma.getCodigoAutorizacion());			
			dtoAutorIngEstancia =  autorizacionIngEstServicio.consultarAutorizacionPorID(dtoAutorIngEstancia);			
			
			if(dtoAutorIngEstancia!=null){				
				//Subir Paciente en session
				if(dtoAutorIngEstancia.getDtoIngresoEstancia() != null 
						&& dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente() != null){
					paciente.setCodigoPersona(dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().getCodigo());
					UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
				}
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
				
				if(!UtilidadTexto.isEmpty(dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().getFechaNacimiento())){			
					
					
					
					String fechaAutorizacion= UtilidadFecha.conversionFormatoFechaAAp(dtoAutorIngEstancia.getFechaAutorizacion());
					String edadPaciente=UtilidadFecha.calcularEdadDetallada(dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().getFechaNacimiento(), fechaAutorizacion);
					
					dtoAutorIngEstancia.getDtoIngresoEstancia().getDtoPaciente().setEdadPacienteCapitado(edadPaciente);
				}
				
				if(!UtilidadTexto.isEmpty(dtoAutorIngEstancia.getEstado())){
					if(dtoAutorIngEstancia.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado)){
						forma.setMostrarBotonProrroga(false);
						forma.setMostrarBotonAnular(false);
						dtoAutorIngEstancia.setIndicativoTemporal(ConstantesBD.acronimoNoChar);
					}
					else{
						forma.setMostrarBotonProrroga(true);
						forma.setMostrarBotonAnular(true);
					}
					String estado=(String)ValoresPorDefecto.getIntegridadDominio(dtoAutorIngEstancia.getEstado());
					dtoAutorIngEstancia.setEstado(estado);
				}
				
				forma.setDiasEstanciaActualizados(dtoAutorIngEstancia.getDiasEstanciaAutorizados());
				
				forma.setDtoAutorizacionIngEstancia(dtoAutorIngEstancia);
			}
			else{
				return ComunAction.accionSalirCasoError(mapping, request, null, null, 
						"No se encontro Detalle Integridad Datos", "errors.detalleAutorizacion.noAutorizacion", true);
			}
			return mapping.findForward("verDetalleAutorizacionIngEstancia");	
			
		}else{
			IAutorizacionesEntidadesSubServicio autorizacionEntSubServicio =
					ManejoPacienteServicioFabrica.crearAutorizacionEntidadesSubServicio();	
			
			manejoPacienteFacade = new ManejoPacienteFacade();
			AutorizacionEntregaDto autorizacionEntregaDto = null;
			
			dtoAutorEntSub = new DTOAutorEntidadSubcontratadaCapitacion();
			dtoAutorEntSub.setAutorCapitacion(new DTOAutorizacionCapitacionSubcontratada());
			dtoAutorEntSub.getAutorCapitacion().setCodigoPK(forma.getCodigoAutorizacion());
			// Se valida si la autorización de capitación tiene asociado un ingreso estancia
			if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_ARTICULOS_ING_EST)
					|| forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SERVICIOS_ING_EST)){
				dtoAutorEntSub = autorizacionEntSubServicio.obtenerAutorizacionEntidadSubCapitacionIngEstanciaPorID(dtoAutorEntSub);
				if(dtoAutorEntSub==null){
					return ComunAction.accionSalirCasoError(mapping, request, null, null, 
							"No se encontro Detalle Integridad Datos", "errors.detalleAutorizacion.noAutorizacion", true);
				}
				forma.setMostrarBotonProrroga(false);
				forma.setMostrarBotonAnular(false);
				forma.setMostrarBotonAutorizarEntSub(false);
			}else{
				dtoAutorEntSub = autorizacionEntSubServicio.obtenerAutorizacionEntidadSubCapitacionPorID(dtoAutorEntSub);
				if(dtoAutorEntSub==null){
					return ComunAction.accionSalirCasoError(mapping, request, null, null, 
							"No se encontro Detalle Integridad Datos", "errors.detalleAutorizacion.noAutorizacion", true);
				}
				forma.setMostrarBotonProrroga(true);
				forma.setMostrarBotonAnular(true);
				//Se valida si la autorización de capitación es de las que en teoria no tienen
				//asociada autorización de entidad subcontratada pero que en la realidad si lo tienen
				//esto porque se habia definido de esta manera para no cambiar la estructura de datos
				if((dtoAutorEntSub.getConsecutivoAutorizacion() == null 
						|| dtoAutorEntSub.getConsecutivoAutorizacion().isEmpty())
						//&& (dtoAutorEntSub.isTieneOrdenAmb() || dtoAutorEntSub.isTienePeticion())
						&&dtoAutorEntSub.getAutorCapitacion().getIndicativoTemporal()==ConstantesBD.acronimoNoChar
						){
					forma.setMostrarBotonAutorizarEntSub(true);
				}
				else{
					forma.setMostrarBotonAutorizarEntSub(false);
				}
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
			
			if(dtoAutorEntSub.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado)){
				forma.setMostrarBotonAnular(false);
				forma.setMostrarBotonProrroga(false);
				forma.setMostrarBotonAutorizarEntSub(false);
				dtoAutorEntSub.getAutorCapitacion().setIndicativoTemporal(ConstantesBD.acronimoNoChar);
			}
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
			
			if(!UtilidadTexto.isEmpty(dtoAutorEntSub.getDtoPaciente().getFechaNacimiento())){			
				
				String fechaAutorizacion= UtilidadFecha.conversionFormatoFechaAAp(dtoAutorEntSub.getFechaAutorizacion().toString());
				String edadPaciente=UtilidadFecha.calcularEdadDetallada(dtoAutorEntSub.getDtoPaciente().getFechaNacimiento(), fechaAutorizacion);
				dtoAutorEntSub.getDtoPaciente().setEdadPacienteCapitado(edadPaciente);					
			}
			
			if(UtilidadTexto.isEmpty(dtoAutorEntSub.getDtoPaciente().getClasificacionSocioEconomica()))
			{	
				Cuenta cuenta= new Cuenta();
	        	cuenta.cargarCuenta(con, paciente.getCodigoCuenta()+"");
	        	dtoAutorEntSub.getDtoPaciente().setClasificacionSocioEconomica(cuenta.getEstrato());
	        	
	        	if(UtilidadTexto.isEmpty(dtoAutorEntSub.getDtoPaciente().getTipoAfiliado()))
	        		dtoAutorEntSub.getDtoPaciente().setTipoAfiliado(cuenta.getTipoAfiliado()); 
			}
			
			dtoAutorEntSub.getUsuarioAutoriza().setNombre(nombreUsuarioAutoriza);
			dtoAutorEntSub.getDtoPaciente().setPrimerNombre(nombrePaciente);
			dtoAutorEntSub.getDtoPaciente().setNumeroIdentificacion(tipoNroID);								
			dtoAutorEntSub.getAutorCapitacion().setCodigoPK(forma.getCodigoAutorizacion());
			
			if(!UtilidadTexto.isEmpty(dtoAutorEntSub.getEstado())){
				String estado=(String)ValoresPorDefecto.getIntegridadDominio(dtoAutorEntSub.getEstado());
				dtoAutorEntSub.setEstado(estado);
			}
			
			forma.setDtoAutorizacionCapitacion(dtoAutorEntSub);
			
			String tipoTarifario = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(
					usuario.getCodigoInstitucionInt());

			/*Consulta de los servicios y articulos relacionados con la autorizacion de capitación*/
			List<ServicioAutorizadoCapitacionDto> listaServicios=new ArrayList<ServicioAutorizadoCapitacionDto>(0);
			try {
				listaServicios = manejoPacienteFacade.consultarServiciosAutorizadosCapitacion(dtoAutorEntSub.getAutorizacion(), Long.parseLong(tipoTarifario), true);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				Log4JManager.error("Error en la consulta de servicios de una autorizacion", e);
			} catch (IPSException e) {
				// TODO Auto-generated catch block
				Log4JManager.error("Error en la consulta de servicios de una autorizacion", e);
			}
			
			ArrayList<DtoServiciosAutorizaciones>servicios=new ArrayList<DtoServiciosAutorizaciones>();
			if(listaServicios != null && !listaServicios.isEmpty()){
				for(ServicioAutorizadoCapitacionDto dtoServicio:listaServicios){
					DtoServiciosAutorizaciones servicio=new DtoServiciosAutorizaciones();
					
					servicio.setTipoAutorizacion(dtoServicio.getTipoAutorizacion());
					servicio.setCodigoOrdenSolPet(dtoServicio.getCodigo());
					servicio.setCodigoServicio(Long.valueOf(dtoServicio.getIdServicio()).intValue());
					
					servicio.setCodigoPropietario(""+dtoServicio.getCodServ());
					
					servicio.setEspecialidad(dtoServicio.getEspecialidad());
					servicio.setTipoServicio(dtoServicio.getTipoServicio());
					servicio.setGrupoServicio(dtoServicio.getGrupoServicio());
					
					servicio.setDescripcionServicio(dtoServicio.getNomServ());
					servicio.setCantidadAutorizadaServicio(Long.valueOf(dtoServicio.getCantidad()).intValue());
					servicio.setDescripcionNivelAtencion(dtoServicio.getNivelAtencion());
					servicio.setDiagnostico(dtoServicio.getAcronimoDiag());
					servicio.setTipoCieDx(dtoServicio.getTipoCieDiag());
					servicio.setDescripcionDiagnostico(dtoServicio.getDiagDescripcion());
					servicio.setCodigoViaIngreso(dtoServicio.getViaIngreso());
					servicio.setIdIngreso(dtoServicio.getIdIngreso());
					servicio.setCodigoCuenta(dtoServicio.getCodigoCuenta());
					servicio.setTipoPaciente(dtoServicio.getTipoPaciente());
					servicio.setTipoSolicitud(dtoServicio.getTipoSolicitud());
					servicio.setPyp(UtilidadTexto.getBoolean(dtoServicio.getPyp()));
					
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
					servicios.add(servicio);
				}
			}
			
			List<ArticuloAutorizadoCapitacionDto> listaArticulos =new ArrayList<ArticuloAutorizadoCapitacionDto>(); 
			//detalleArticulo.obtenerDetalleArticulosAutorCapitacion(dtoAutorEntSub.getAutorizacion());
			try {
				listaArticulos = manejoPacienteFacade.consultarArticulosAutorizadosCapitacion(dtoAutorEntSub.getAutorizacion(),usuario.getCodigoInstitucionInt(),true);
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
					
					articulo.setCodigoArticulo(Long.valueOf(dtoArticulo.getCodArt()).intValue());

					articulo.setTipoSolicitud(dtoArticulo.getTipoSolicitud());
					articulo.setPyp(UtilidadTexto.getBoolean(dtoArticulo.getPyp()));
					
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
					
					articulo.setNaturalezaArticulo(dtoArticulo.getNaturalezaArticulo());
					
					articulo.setAcronimoNaturalezaArticulo(dtoArticulo.getAcronimoNatArt());
					articulo.setCodigoSubGrupoArticulo(dtoArticulo.getCodigoSubgrupo());
					
					articulo.setCantidadAutorizadaArticulo(""+dtoArticulo.getCantidad());
					articulo.setDiagnostico(dtoArticulo.getAcronimoDiag());
					articulo.setTipoCieDx(dtoArticulo.getTipoCieDiag());
					articulo.setDescripcionDiagnostico(dtoArticulo.getDiagDescripcion());
					
					articulo.setCodigoViaIngreso(dtoArticulo.getViaIngreso());
					
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
					articulos.add(articulo);
				}
			}
			
			forma.setListaServicios(servicios);
			forma.setListaArticulos(articulos);
			
			try{
				cargarAutorizacionCapitacionDto(forma, usuario);
			} catch (IPSException e) {
				// TODO Auto-generated catch block
				errores.add("", new ActionMessage(e.getErrorCode().toString()));
			}
			
			if(!errores.isEmpty()){
				saveErrors(request, errores);
			}
			
			return mapping.findForward("verDetalleAutorizacionCapitacion");	
		}
		
	}	
	
	/**
	 * @param forma
	 * @param usuario
	 * @throws IPSException
	 * @author jeilones
	 * @created 20/09/2012
	 */
	private void cargarAutorizacionCapitacionDto(AdministracionAutorizacionCapitacionDetalleForm forma, UsuarioBasico usuario) throws IPSException{
		
		InventarioFacade inventarioFacade = new InventarioFacade();
		
		AutorizacionCapitacionDto autorizacionCapitacionDto=new AutorizacionCapitacionDto();
		
		autorizacionCapitacionDto.setTipoAutorizacion(forma.getDtoAutorizacionCapitacion().getAutorCapitacion().getTipoAutorizacion());
		autorizacionCapitacionDto.setLoginUsuario(usuario.getLoginUsuario());
		autorizacionCapitacionDto.setCodigoPersonaUsuario(usuario.getCodigoPersona());
		autorizacionCapitacionDto.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		autorizacionCapitacionDto.setCentroAtencion(usuario.getCodigoCentroAtencion());
		//OK autorizacionCapitacionDto.setCentroAtencion(centroAtencion)
		//OK pasar la EntidadSubAutorizarCapitacion seleccionada
		
		DatosPacienteAutorizacionDto datosPaciente=new DatosPacienteAutorizacionDto();
		datosPaciente.setNombresPaciente(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getNombreCompleto());
		if(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getTipoAfiliadoChar()!=null){
			datosPaciente.setTipoAfiliado(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getTipoAfiliadoChar().toString());
		}
		if(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getCodigoClasificacionSE()!=null){
			datosPaciente.setClasificacionSocieconomica(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getCodigoClasificacionSE());
		}
		//OK datosPaciente.setCuenta(cuenta)
		//OK datosPaciente.setTipoPaciente(forma.getDtoAutorizacionCapitacion().getDtoPaciente().getTipoPersona())
		
		autorizacionCapitacionDto.setDatosPacienteAutorizar(datosPaciente);
		
		if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_ARTICULOS_ING_EST)
				||forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SERVICIOS_ING_EST)){
			autorizacionCapitacionDto.setAutoServArtIngresoEstancia(true);
		}else{
			autorizacionCapitacionDto.setAutoServArtIngresoEstancia(false);
		}
		
		String viaIngOrden=ValoresPorDefecto.getViaIngresoValidacionesOrdenesAmbulatorias(usuario.getCodigoInstitucionInt());
		String viaIngPeticion=ValoresPorDefecto.getViaIngresoValidacionesPeticiones(usuario.getCodigoInstitucionInt());
		int codigoViaIngOrden=viaIngOrden!=null&&!viaIngOrden.trim().isEmpty()?Integer.parseInt(viaIngOrden):ConstantesBD.codigoNuncaValido;
		int codigoViaIngPeticion=viaIngPeticion!=null&&!viaIngPeticion.trim().isEmpty()?Integer.parseInt(viaIngPeticion):ConstantesBD.codigoNuncaValido;
		
		List<OrdenAutorizacionDto> listaOrdenes=new ArrayList<OrdenAutorizacionDto>(0);
		
		OrdenesFacade ordenesFacade=new OrdenesFacade();
		
		if(!forma.getListaServicios().isEmpty()){
			HashMap<Long, OrdenAutorizacionDto>mapaOrdenes=new HashMap<Long, OrdenAutorizacionDto>(0);
			
			for(DtoServiciosAutorizaciones servicioAutorizaciones:forma.getListaServicios()){
				ServicioAutorizacionOrdenDto servicioAutorizacionOrdenDto=new ServicioAutorizacionOrdenDto();
				servicioAutorizacionOrdenDto.setCodigo(servicioAutorizaciones.getCodigoServicio());
				servicioAutorizacionOrdenDto.setCodigoEspecialidad(servicioAutorizaciones.getEspecialidad());
				servicioAutorizacionOrdenDto.setAcronimoTipoServicio(servicioAutorizaciones.getTipoServicio());
				servicioAutorizacionOrdenDto.setCodigoGrupoServicio(servicioAutorizaciones.getGrupoServicio());
				servicioAutorizacionOrdenDto.setCantidad((long)servicioAutorizaciones.getCantidadAutorizadaServicio());
				
				/*
				 * Se define la cuenta por cualquiera de los servicios puesto que la esta cuenta debe ser la misma para todas
				 * las ordenes que la genero  
				 * */
				if(datosPaciente.getCuenta()<=0){
					datosPaciente.setCuenta(Long.valueOf(servicioAutorizaciones.getCodigoCuenta()).intValue());
				}
				
				
				OrdenAutorizacionDto ordenAutorizacionDto=null;
				List<ServicioAutorizacionOrdenDto> listaServiciosOrden=null;
				if(mapaOrdenes.containsKey(servicioAutorizaciones.getNumeroOrdenLong())){
					ordenAutorizacionDto=mapaOrdenes.get(servicioAutorizaciones.getNumeroOrdenLong());
					if(ordenAutorizacionDto.getServiciosPorAutorizar()!=null){
						listaServiciosOrden=ordenAutorizacionDto.getServiciosPorAutorizar();
					}
				}else{
					ordenAutorizacionDto=new OrdenAutorizacionDto();
					ordenAutorizacionDto.setContrato(null);
					listaServiciosOrden=new ArrayList<ServicioAutorizacionOrdenDto>(0);
					ordenAutorizacionDto.setServiciosPorAutorizar(listaServiciosOrden);
					ordenAutorizacionDto.setContrato(null);
					listaOrdenes.add(ordenAutorizacionDto);
				}
				
				ordenAutorizacionDto.setConsecutivoOrden(String.valueOf(servicioAutorizaciones.getNumeroOrdenLong()));
				ordenAutorizacionDto.setTipoOrden(servicioAutorizaciones.getTipoSolicitud());
				ordenAutorizacionDto.setEsPyp(servicioAutorizaciones.isPyp());
				
				ContratoDto contratoDto=null;
				
				if(servicioAutorizaciones.getTipoAutorizacion()==Long.valueOf(ConstantesIntegridadDominio.TIPO_AUTORIZACION_PETICION).longValue()){
					ordenAutorizacionDto.setCodigoViaIngreso(codigoViaIngPeticion);
					ordenAutorizacionDto.setClaseOrden( ConstantesBD.claseOrdenOrdenMedica );
					
					if(ordenAutorizacionDto.getContrato()==null){
						contratoDto=ordenesFacade.obtenerConvenioContratoPorPeticion(Long.valueOf(servicioAutorizaciones.getCodigoOrdenSolPet()).intValue());
					}
					if(datosPaciente.getTipoPaciente()==null){
						datosPaciente.setTipoPaciente(ValoresPorDefecto.getTipoPacienteValidacionesPeticiones(usuario.getCodigoInstitucionInt()));
					}
				}else{
					if(servicioAutorizaciones.getTipoAutorizacion()==Long.valueOf(ConstantesIntegridadDominio.TIPO_AUTORIZACION_ORDEN_AMB).longValue()){
						ordenAutorizacionDto.setCodigoViaIngreso(codigoViaIngOrden);
						ordenAutorizacionDto.setClaseOrden( ConstantesBD.claseOrdenOrdenMedica );
						
						if(ordenAutorizacionDto.getContrato()==null){
							contratoDto=ordenesFacade.obtenerConvenioContratoPorOrdenAmbulatoria(servicioAutorizaciones.getCodigoOrdenSolPet(), ConstantesBD.codigoTipoOrdenAmbulatoriaServicios);
						}
						if(datosPaciente.getTipoPaciente()==null){
							datosPaciente.setTipoPaciente(ValoresPorDefecto.getTipoPacienteValidacionesOrdenesAmbulatorias(usuario.getCodigoInstitucionInt()));
						}
					}else{
						ordenAutorizacionDto.setCodigoViaIngreso(servicioAutorizaciones.getCodigoViaIngreso());
						if(servicioAutorizaciones.getTipoAutorizacion()==Long.valueOf(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SOLICITUD).longValue()
								||servicioAutorizaciones.getTipoAutorizacion()==Long.valueOf(ConstantesIntegridadDominio.TIPO_AUTORIZACION_CARGOS_DIRECTOS).longValue()){
							int claseOrden=ConstantesBD.claseOrdenCargoDirecto;
							if(servicioAutorizaciones.getTipoAutorizacion()==Long.valueOf(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SOLICITUD).longValue()){
								claseOrden=ConstantesBD.claseOrdenOrdenMedica;
							}
							ordenAutorizacionDto.setClaseOrden( claseOrden );
							
							if(ordenAutorizacionDto.getContrato()==null){
								contratoDto=ordenesFacade.obtenerConvenioContratoPorOrdenMedica(Long.valueOf(servicioAutorizaciones.getCodigoOrdenSolPet()).intValue(), claseOrden, servicioAutorizaciones.getTipoSolicitud());
							}
							if(datosPaciente.getTipoPaciente()==null){
								//se pasa el tipo paciente que proviene de la solicitud 
								datosPaciente.setTipoPaciente(servicioAutorizaciones.getTipoPaciente());
							}
						}
						
						/*
						 * Para autorizaciones de servicios/insumos de ingreso estancia no se requiere ya que para poder realizar
						 * estas autorizzaciones se obliga a definir la entidad subcontratada de la autorizacion de ingreso estancia
						 **/
					}
				}
				
				ordenAutorizacionDto.setContrato(contratoDto);
				
				listaServiciosOrden.add(servicioAutorizacionOrdenDto);
			}
			
			
			
		}else{			
			if(!forma.getListaArticulos().isEmpty()){
				HashMap<Long, OrdenAutorizacionDto>mapaOrdenes=new HashMap<Long, OrdenAutorizacionDto>(0);
				
				//List<MedicamentoInsumoAutorizacionOrdenDto> listaArticulosOrden=new ArrayList<MedicamentoInsumoAutorizacionOrdenDto>(0);
				for(DtoArticulosAutorizaciones articuloAutorizaciones:forma.getListaArticulos()){
					MedicamentoInsumoAutorizacionOrdenDto articuloAutorizacionOrdenDto=new MedicamentoInsumoAutorizacionOrdenDto();
					articuloAutorizacionOrdenDto.setCodigo(articuloAutorizaciones.getCodigoArticulo());
					articuloAutorizacionOrdenDto.setAcronimoNaturaleza(articuloAutorizaciones.getAcronimoNaturalezaArticulo());
					articuloAutorizacionOrdenDto.setSubGrupoInventario(articuloAutorizaciones.getCodigoSubGrupoArticulo());
					
					ClaseInventarioDto dtoClaseInv=inventarioFacade.obtenerClaseInventarioPorSubGrupo(articuloAutorizacionOrdenDto.getSubGrupoInventario().intValue());
					if(dtoClaseInv != null){
						articuloAutorizacionOrdenDto.setClaseInventario(dtoClaseInv.getCodigo());
						articuloAutorizacionOrdenDto.setNombreClaseInventario(dtoClaseInv.getNombre());
					}
					
					if(articuloAutorizaciones.getCantidadAutorizadaArticulo()!=null){
						articuloAutorizacionOrdenDto.setCantidadSolicitada(Integer.parseInt(articuloAutorizaciones.getCantidadAutorizadaArticulo()));
					}
					
					OrdenAutorizacionDto ordenAutorizacionDto=null;
					List<MedicamentoInsumoAutorizacionOrdenDto> listaArticulosOrden=null;
					if(mapaOrdenes.containsKey(articuloAutorizaciones.getNumeroOrdenLong())){
						ordenAutorizacionDto=mapaOrdenes.get(articuloAutorizaciones.getNumeroOrdenLong());
						if(ordenAutorizacionDto.getMedicamentosInsumosPorAutorizar()!=null){
							listaArticulosOrden=ordenAutorizacionDto.getMedicamentosInsumosPorAutorizar();
						}
					}else{
						ordenAutorizacionDto=new OrdenAutorizacionDto();
						listaArticulosOrden=new ArrayList<MedicamentoInsumoAutorizacionOrdenDto>(0);
						ordenAutorizacionDto.setMedicamentosInsumosPorAutorizar(listaArticulosOrden);
						ordenAutorizacionDto.setContrato(null);
						listaOrdenes.add(ordenAutorizacionDto);
					}
					
					ordenAutorizacionDto.setConsecutivoOrden(String.valueOf(articuloAutorizaciones.getNumeroOrdenLong()));
					ordenAutorizacionDto.setTipoOrden(articuloAutorizaciones.getTipoSolicitud());
					ordenAutorizacionDto.setEsPyp(articuloAutorizaciones.isPyp());
					
					ContratoDto contratoDto=null;
					
					if(articuloAutorizaciones.getTipoAutorizacion()==Long.valueOf(ConstantesIntegridadDominio.TIPO_AUTORIZACION_PETICION).longValue()){
						ordenAutorizacionDto.setCodigoViaIngreso(codigoViaIngPeticion);
						ordenAutorizacionDto.setClaseOrden( ConstantesBD.claseOrdenPeticion );
						if(ordenAutorizacionDto.getContrato()==null){
							contratoDto=ordenesFacade.obtenerConvenioContratoPorPeticion(Long.valueOf(articuloAutorizaciones.getCodigoOrdenSolPet()).intValue());
						}
					}else{
						if(articuloAutorizaciones.getTipoAutorizacion()==Long.valueOf(ConstantesIntegridadDominio.TIPO_AUTORIZACION_ORDEN_AMB).longValue()){
							ordenAutorizacionDto.setClaseOrden( ConstantesBD.claseOrdenOrdenAmbulatoria );
							ordenAutorizacionDto.setCodigoViaIngreso(codigoViaIngOrden);
							if(ordenAutorizacionDto.getContrato()==null){
								contratoDto=ordenesFacade.obtenerConvenioContratoPorOrdenAmbulatoria(articuloAutorizaciones.getCodigoOrdenSolPet(), ConstantesBD.codigoTipoOrdenAmbulatoriaArticulos);
							}
						}else{
							ordenAutorizacionDto.setCodigoViaIngreso(articuloAutorizaciones.getCodigoViaIngreso());
							if(articuloAutorizaciones.getTipoAutorizacion()==Long.valueOf(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SOLICITUD).longValue()
									||articuloAutorizaciones.getTipoAutorizacion()==Long.valueOf(ConstantesIntegridadDominio.TIPO_AUTORIZACION_CARGOS_DIRECTOS).longValue()){
								int claseOrden=ConstantesBD.claseOrdenCargoDirecto;
								if(articuloAutorizaciones.getTipoAutorizacion()==Long.valueOf(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SOLICITUD).longValue()){
									
									claseOrden=ConstantesBD.claseOrdenOrdenMedica;
								}
								ordenAutorizacionDto.setClaseOrden( claseOrden );
								
								if(ordenAutorizacionDto.getContrato()==null){
									contratoDto=ordenesFacade.obtenerConvenioContratoPorOrdenMedica(Long.valueOf(articuloAutorizaciones.getCodigoOrdenSolPet()).intValue(), claseOrden, articuloAutorizaciones.getTipoSolicitud());
								}
							}
							/*
							 * Para autorizaciones de servicios/insumos de ingreso estancia no se requiere ya que para poder realizar
							 * estas autorizzaciones se obliga a definir la entidad subcontratada de la autorizacion de ingreso estancia
							 **/
						}
					}
					
					ordenAutorizacionDto.setContrato(contratoDto);
					listaArticulosOrden.add(articuloAutorizacionOrdenDto);
				}
			}
		}
		for(OrdenAutorizacionDto ordenAutorizacionDto:listaOrdenes){
			if(ordenAutorizacionDto.getContrato()==null){
				ordenAutorizacionDto.setContrato(new ContratoDto());
			}
		//MT6050 se agrega la validación 
		
		if(forma.getDtoAutorizacionCapitacion().getDtoContrato() != null && forma.getDtoAutorizacionCapitacion().getDtoContrato().getCodigo()==ConstantesBD.codigoNuncaValido){
							
				forma.getDtoAutorizacionCapitacion().getDtoContrato().setCodigo(ordenAutorizacionDto.getContrato().getCodigo());
			}
		}
		autorizacionCapitacionDto.setOrdenesAutorizar(listaOrdenes);
		
		forma.setAutorizacionCapitacionDto(autorizacionCapitacionDto);
	}
	
	/**
	 * 
	 * Este Método se encarga de calcular la fecha de vencimiento de la autorización
	 * de ingreso estancia según los días de estancia ingresados por el usuario
	 * 
	 * @param AdministracionAutorizacionCapitacionDetalleForm forma, ActionMapping mapping
	 * @return ActionForward 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward calcularFechaVencimiento(HttpServletRequest request,
			AdministracionAutorizacionCapitacionDetalleForm forma,
			ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario){
		
		ActionMessages errores = new ActionMessages();
		
		
			DTOAutorizacionIngresoEstancia dtoAutorIngEstancia = forma.getDtoAutorizacionIngEstancia();
			
			if(dtoAutorIngEstancia !=null&&validarNuevosDiasEstancia(errores, forma.getDiasEstanciaActualizados(), dtoAutorIngEstancia.getDiasEstanciaAutorizados())){
				
				Date fechaVencimientoAutorizacion =  UtilidadFecha.conversionFormatoFechaStringDate(
						UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(dtoAutorIngEstancia.getFechaAutorizacion()),
								forma.getDiasEstanciaActualizados() , false));
				
				dtoAutorIngEstancia.setNuevaFechaVencimiento(fechaVencimientoAutorizacion);
				
			}
		
		if(errores!=null && !errores.isEmpty()){
     		saveErrors(request, errores);
     	}
		
		return mapping.findForward("mostrarPopUpModificarDiasEstancia");	
		
	}
	
	/**
	 * Valida que los nuevos dias de estancia cumplan con las condiciones  de ser mayor a cero
	 * y menor a los dias autorizados actualmente
	 * 
	 * @param errores
	 * @param nuevosDiasEstancia
	 * @param diasAutorizados
	 * @return
	 * @author jeilones
	 * @created 16/08/2012
	 */
	private boolean validarNuevosDiasEstancia(ActionMessages errores, int nuevosDiasEstancia,int diasAutorizados){
		MessageResources mensajes=MessageResources.getMessageResources(
				"com.servinte.mensajes.manejoPaciente.DetalleAdministracionAutorizacionIngEstanciaForm");
		
		if(nuevosDiasEstancia<=0||nuevosDiasEstancia>=diasAutorizados){
			
			errores.add("Días autorizados no permitidos", new ActionMessage("errors.notEspecific", 
					mensajes.getMessage("detalleAutorizacionIngEstanciaForm.errorDiasAutor")));	
			
		}
		
		return errores.isEmpty();
	}
	
	/**
	 * 
	 * Este Método se encarga de actualizar una autorización de ingreso estancia
	 * 
	 * @param HttpServletRequest request,
	 *		  AdministracionAutorizacionCapitacionDetalleForm forma,
	 *		  ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario
	 * @return ActionForward			
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward guardarAutorizacionIngEstancia(HttpServletRequest request,
			AdministracionAutorizacionCapitacionDetalleForm forma,
			ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario){
		
		ActionMessages errores = new ActionMessages();
		
		boolean procesoExitoso=false;
		
		if(validarNuevosDiasEstancia(errores, forma.getDiasEstanciaActualizados(), forma.getDtoAutorizacionIngEstancia().getDiasEstanciaAutorizados())){
			
			DTOAutorizacionIngresoEstancia dtoAutorIngEstancia = forma.getDtoAutorizacionIngEstancia();
			IAutorizacionIngresoEstanciaServicio servicio = ManejoPacienteServicioFabrica.crearAutorizacionIngresoEstanciaServicio();
			
			
			AutorizacionesIngreEstancia autorizacion = servicio.buscarAutorizacionIngresoEstanciaPorID(dtoAutorIngEstancia.getCodigoPk());
			
			if(autorizacion!=null){
				//Se guarda el histórico
				HistoAutorizacionIngEstan historico = new HistoAutorizacionIngEstan();
				historico.setAutorizacionesIngreEstancia(autorizacion);
				historico.setFechaInicioAutorizacion(autorizacion.getFechaInicioAutorizacion());
				historico.setConsecutivoAdmision(autorizacion.getConsecutivoAdmision());
				historico.setDiasEstanciaAutorizados(autorizacion.getDiasEstanciaAutorizados());
				historico.setUsuarioContacta(autorizacion.getUsuarioContacta());
				historico.setCargoUsuarioContacta(autorizacion.getCargoUsuContacta());
				if(autorizacion.getConvenios()!=null){
					historico.setConvenioRecobro(autorizacion.getConvenios().getCodigo());
				}
				historico.setOtroConvenioRecobro(autorizacion.getOtroConvenioRecobro());
				historico.setIndicativoTemporal(autorizacion.getIndicativoTemporal());
				if(autorizacion.getCentrosCosto()!=null){
					historico.setCentroCostoSolicitante(autorizacion.getCentrosCosto().getCodigo());
				}
				historico.setEstado(autorizacion.getEstado());
				
				Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
				String horaActual = UtilidadFecha.getHoraActual();
				
				historico.setFechaModifica(fechaActual);
				historico.setHoraModifica(horaActual);		
				
				Usuarios usuarioModifica = new Usuarios();
				usuarioModifica.setLogin(usuario.getLoginUsuario());
				
				historico.setUsuarios(usuarioModifica);
				historico.setAccionRealizada(
						ConstantesIntegridadDominio.acronimoAccionHistoricaModificar);
				historico.setObservaciones(forma.getMotivoAnulacion());
				
				Set<HistoAutorizacionIngEstan> setHistorico = new HashSet<HistoAutorizacionIngEstan>(0);
				setHistorico.add(historico);					
				autorizacion.setHistoAutorizacionIngEstans(setHistorico);
							
				autorizacion.setDiasEstanciaAutorizados(forma.getDiasEstanciaActualizados());
				procesoExitoso = servicio.actualizarAutorizacionIngresoEstancia(autorizacion);
				
				if(procesoExitoso){
					forma.setAbrirPopPupAnulacion(ConstantesBD.acronimoNo);
					forma.setAbrirPopPupEntidadesSub(ConstantesBD.acronimoNo);
					forma.setAbrirPopPupProrroga(ConstantesBD.acronimoNo);
					forma.setAbrirPopUpConfirmarAutoEntSub(ConstantesBD.acronimoNo);
					forma.setAbrirPopUpModificacionDiasEstancia(ConstantesBD.acronimoNo);
					
					dtoAutorIngEstancia.setDiasEstanciaAutorizados(forma.getDiasEstanciaActualizados());
					
					Date fechaVencimientoAutorizacion =  UtilidadFecha.conversionFormatoFechaStringDate(
						UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.conversionFormatoFechaAAp(dtoAutorIngEstancia.getFechaAutorizacion()),
								dtoAutorIngEstancia.getDiasEstanciaAutorizados(), false));
					
					dtoAutorIngEstancia.setFechaVencimiento(fechaVencimientoAutorizacion);
				}
			}
		}else{
			saveErrors(request, errores);
		}
					
		
		forma.setProcesoExitoso(procesoExitoso);
		
		return mapping.findForward("verDetalleAutorizacionIngEstancia");
		
	}
	
	/**
	 * En este método se realizan las validaciones previas a la autorización de 
	 * de un registro en estado temporal 
	 * 
	 * @param request
	 * @param forma
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 */
	public ActionForward validarAutorizacionTemporal(Connection con, HttpServletRequest request,
			AdministracionAutorizacionCapitacionDetalleForm forma,
			ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario){
		
		forma.setAbrirPopPupAnulacion(ConstantesBD.acronimoNo);
		forma.setAbrirPopPupEntidadesSub(ConstantesBD.acronimoNo);
		forma.setAbrirPopPupProrroga(ConstantesBD.acronimoNo);
		forma.setAbrirPopUpModificacionDiasEstancia(ConstantesBD.acronimoNo);
		forma.setAbrirPopUpConfirmarAutoEntSub(ConstantesBD.acronimoNo);
		
		MessageResources mensajes=MessageResources.getMessageResources(
		propertiesDetalleAdministracion);
		ActionMessages errores = new ActionMessages();
		String consecutivoDiasIndicativoTemporal="";
		String consecutivoCapitacion="";
		String reglaNavegacion="";
		Date fechaVencimientoAutorizacion=null;
		DTOAutorizacionIngresoEstancia dtoAutorIngEstancia=null;
		DTOAutorEntidadSubcontratadaCapitacion dtoAutorEntSub=null;
		
		if(forma.getEstado().equals("validarTemporalIngEstancia")){
			reglaNavegacion = "verDetalleAutorizacionIngEstancia";
			dtoAutorIngEstancia = forma.getDtoAutorizacionIngEstancia();
			
			String fechaVencimiento = UtilidadFecha.incrementarDiasAFecha(
					UtilidadFecha.conversionFormatoFechaAAp(dtoAutorIngEstancia.getFechaAutorizacion()),
					Integer.valueOf(dtoAutorIngEstancia.getDiasEstanciaAutorizados()).intValue(), false);
			
			fechaVencimientoAutorizacion =  UtilidadFecha.conversionFormatoFechaStringDate(fechaVencimiento);
			
		}else{
			reglaNavegacion = "verDetalleAutorizacionCapitacion";
			dtoAutorEntSub = forma.getDtoAutorizacionCapitacion();
			fechaVencimientoAutorizacion =  dtoAutorEntSub.getFechaVencimiento();
			
			consecutivoCapitacion = UtilidadBD.obtenerValorConsecutivoDisponible(
					ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada,
					Integer.valueOf(usuario.getCodigoInstitucionInt()));
			
			if(UtilidadTexto.isEmpty(consecutivoCapitacion)){
				errores.add("consecutivo autorización no definido", new ActionMessage("errors.notEspecific", 
						mensajes.getMessage("detalleAdministracionAutorizacion.errorConsecutivoCapitacion")));
			}				
		}		
		
		if((forma.getEstado().equals("validarTemporalIngEstancia") && errores.isEmpty())
				|| forma.getEstado().equals("validarTemporalCapitacion")){
			
			consecutivoDiasIndicativoTemporal = ValoresPorDefecto.getDiasVigenciaAutorIndicativoTemp(
					usuario.getCodigoInstitucionInt());
			if(UtilidadTexto.isEmpty(consecutivoDiasIndicativoTemporal)){
				errores.add("consecutivo dias con indicativo temporal no definido", new ActionMessage("errors.notEspecific", 
						mensajes.getMessage("detalleAdministracionAutorizacion.errorConsecutivoDiasIndTemporal")));			
			}else{
				
				String fechaVencimientoDiasTemporal = UtilidadFecha.incrementarDiasAFecha(
						UtilidadFecha.conversionFormatoFechaAAp(fechaVencimientoAutorizacion),
						Integer.valueOf(consecutivoDiasIndicativoTemporal).intValue(), false);
				
				if(!(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						UtilidadFecha.getFechaActual(),UtilidadFecha.conversionFormatoFechaAAp(fechaVencimientoDiasTemporal)))){
					errores.add("Fecha vencimiento autorización no válido", new ActionMessage("errors.notEspecific", 
							mensajes.getMessage("detalleAdministracionAutorizacion.errorFechaAutorizacion")));
				}else{
					
					consultarEntidadesSubcontratadas(con, forma,usuario,errores,mensajes);
					if(!forma.getEntidadesSubcontratadas().isEmpty()){
						forma.setAbrirPopPupEntidadesSub(ConstantesBD.acronimoSi);
					}
					
				}
				
			}
		}		
		if(errores!=null && !errores.isEmpty()){
     		saveErrors(request, errores);     		
     	}
		
		return mapping.findForward(reglaNavegacion);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar las entidades subcontratdas
	 * según el tipo de autorización: de ingreso estancia o de capitación
	 * 
	 * @author, Angela Maria Aguirre
	 * @param errores 
	 * @param mensajes 
	 *
	 */
	private void consultarEntidadesSubcontratadas(Connection con, AdministracionAutorizacionCapitacionDetalleForm forma,
			UsuarioBasico usuario, ActionMessages errores, MessageResources mensajes){
		
		ArrayList<DtoEntidadSubcontratada> listaEntidadesSub=null;
		IEntidadesSubcontratadasServicio servicioEntSub = FacturacionServicioFabrica.crearEntidadesSubcontratadasServicio();
		
		if(forma.getEstado().equals("validarTemporalIngEstancia")){
			DtoEntidadSubcontratada dtoParametros=new DtoEntidadSubcontratada();
			DTOAutorizacionIngresoEstancia dtoAutorIngEstancia = forma.getDtoAutorizacionIngEstancia();
			
			dtoParametros.setPermiteEstanciaPaciente(true);
			dtoParametros.setViaIngreso(dtoAutorIngEstancia.getDtoIngresoEstancia().getCodigoViaIngreso());
			
			listaEntidadesSub = servicioEntSub.listarEntidadesSubXViaIngreso(dtoParametros);
			List<EntidadSubContratadaDto>listaEntidadSubContratadaDtos=new ArrayList<EntidadSubContratadaDto>(0);
			for(DtoEntidadSubcontratada dtoEntidadSubcontratada:listaEntidadesSub){
				EntidadSubContratadaDto entidadSubContratadaDto=new EntidadSubContratadaDto();
				entidadSubContratadaDto.setRazonSocial(dtoEntidadSubcontratada.getRazonSocial());
				entidadSubContratadaDto.setCodContratoEntidadSub(dtoEntidadSubcontratada.getContratoEntSub());
				entidadSubContratadaDto.setCodEntidadSubcontratada(dtoEntidadSubcontratada.getCodigoPk());
				entidadSubContratadaDto.setDireccionEntidad(dtoEntidadSubcontratada.getDireccion());
				entidadSubContratadaDto.setNumeroPrioridad(dtoEntidadSubcontratada.getNroPrioridad());
				entidadSubContratadaDto.setTelefonoEntidad(dtoEntidadSubcontratada.getTelefono());
				entidadSubContratadaDto.setTipoTarifa(dtoEntidadSubcontratada.getTipotarifa());
				listaEntidadSubContratadaDtos.add(entidadSubContratadaDto);
			}
			
			forma.setEntidadesSubcontratadas(listaEntidadSubContratadaDtos);
			//forma.setListadoEntidadesSub(listaEntidadesSub);
			
		}else{
			DTOAutorEntidadSubcontratadaCapitacion dtoAutorEntSub = forma.getDtoAutorizacionCapitacion();
			
			
			try {
				/********************Se cargan las entidades dado el DCU 1105*******************/
				NivelAutorizacionMundo nivelAutorizacionMundo=new NivelAutorizacionMundo();
				
				
				AutorizacionCapitacionDto autorizacionCapitacionDto= forma.getAutorizacionCapitacionDto();
				
				CapitacionFacade capitacionFacade=new CapitacionFacade();
				
				forma.setNivelesAutorizacionUsuario(capitacionFacade.consultarNivelesAutorizacionUsuario(
						usuario.getLoginUsuario(), 
						usuario.getCodigoPersona(), 
						ConstantesIntegridadDominio.acronimoTipoAutorizacionManual, 
						true));
				
				ManejoPacienteFacade manejoPacienteFacade=new ManejoPacienteFacade();
				
				List<EntidadSubContratadaDto>listaEntidades=manejoPacienteFacade.obtenerEntidadesSubContratadasExternas(
						dtoAutorEntSub.getDtoPaciente().getCodigoCentroCosto(), 
						forma.getNivelesAutorizacionUsuario());
				
				List<EntidadSubContratadaDto>listaEntidadesCumplenPrioridad=new ArrayList<EntidadSubContratadaDto>(0);
				for(EntidadSubContratadaDto entidadSubContratadaDto:listaEntidades){
					autorizacionCapitacionDto.setEntidadSubAutorizarCapitacion(entidadSubContratadaDto);
					
					//Se llama la validacion de niveles del DCU 1105
					nivelAutorizacionMundo.validarNivelesAutorizacionAutorizacionesPoblacionCapitada(autorizacionCapitacionDto, true, true);
					
					if(autorizacionCapitacionDto.isProcesoExitoso()){
						listaEntidadesCumplenPrioridad.add(entidadSubContratadaDto);
					}
				}
				
				forma.setAutorizacionCapitacionDto(autorizacionCapitacionDto);
				
				/////////////////////////////////////////////////
				
				
				if(listaEntidadesCumplenPrioridad != null && !listaEntidadesCumplenPrioridad.isEmpty()){
					validarCoberturaEntidadesSubcontratadas(con, forma,usuario,listaEntidadesCumplenPrioridad);
				}
			
				if(forma.getEntidadesSubcontratadas().isEmpty()){
					errores.add("",new ActionMessage("errors.notEspecific",mensajes.getMessage("detalleAdministracionAutorizacion.errorNoEntidadesSubcontratadas")));
				}
			
			} catch (IPSException e) {
				// TODO Auto-generated catch block
				errores.add("", new ActionMessage(e.getErrorCode().toString()));
			}
		}	
		
	}
	
	
	/**
	 * Valida las EntidadesSubcontratadas para los Servicios y Articulos de la orden.
	 * 
	 * @param AdministracionAutorizacionCapitacionDetalleForm forma, 
	 *		  UsuarioBasico usuario, 	ArrayList<DtoServiciosAutorizaciones> listaServicios,
	 *		  ArrayList<DtoArticulosAutorizaciones> listaArticulos,ArrayList<DtoEntidadSubcontratada> listaEntidadesSub
	 * @throws IPSException 
	 */
	private void validarCoberturaEntidadesSubcontratadas(Connection con, AdministracionAutorizacionCapitacionDetalleForm forma, 
			UsuarioBasico usuario, 	List<EntidadSubContratadaDto> listaEntidades) throws IPSException{
		
		InfoCobertura cobertura =null;
		List<EntidadSubContratadaDto> listaEntidadesSubValidas = new ArrayList<EntidadSubContratadaDto>();
		
		int codViaIngreso				= forma.getDtoAutorizacionCapitacion().getDtoPaciente().getCodigoViaIngreso();
		int codigoNaturalezaPaciente	= forma.getDtoAutorizacionCapitacion().getDtoPaciente().getCodigoNaturalezaPaciente();
		String tipoPaciente				= forma.getDtoAutorizacionCapitacion().getDtoPaciente().getAcronimotipoPaciente();
		
		forma.setListadoEntidadesSub(new ArrayList<DtoEntidadSubcontratada>());
	
		// Validar Servicios
		if(!Utilidades.isEmpty(forma.getListaServicios())){
			for(EntidadSubContratadaDto entidadSub : listaEntidades){
				for (DtoServiciosAutorizaciones servicio : forma.getListaServicios()) 
				{
					//Se invoca Anexo 800
					CoberturaMundo coberturaMundo=new CoberturaMundo();
					cobertura=coberturaMundo.validacionCoberturaServicioEntidadSub(entidadSub.getCodContratoEntidadSub(), codViaIngreso, tipoPaciente, servicio.getCodigoServicio(), 
							codigoNaturalezaPaciente, usuario.getCodigoInstitucionInt());
					
					if(cobertura.existe()){
						listaEntidadesSubValidas.add(entidadSub);
					}
				}
			}
		}else{
			
			// Validar Articulos
			if(!Utilidades.isEmpty(forma.getListaArticulos())){
				for(EntidadSubContratadaDto entidadSub : listaEntidades){
					for (DtoArticulosAutorizaciones articulo : forma.getListaArticulos()) 
					{
						//Se invoca Anexo 800
						CoberturaMundo coberturaMundo=new CoberturaMundo();
						cobertura=coberturaMundo.validacionCoberturaArticuloEntidadSub(entidadSub.getCodContratoEntidadSub(), codViaIngreso, tipoPaciente, articulo.getCodigoArticulo(), 
								codigoNaturalezaPaciente, usuario.getCodigoInstitucionInt());
						
						if(cobertura.existe()){
							listaEntidadesSubValidas.add(entidadSub);
						}
					}
				}		
			}
		}
		
		forma.setEntidadesSubcontratadas(listaEntidadesSubValidas);
	}
	
		
	/**
	 * En este método se realizan las validaciones previas a la autorización de 
	 * de un registro en estado temporal 
	 * 
	 * @param request
	 * @param forma
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 */
	public ActionForward realizarAutorizacionTemporal(Connection con, HttpServletRequest request,
			AdministracionAutorizacionCapitacionDetalleForm forma,
			ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario) throws Exception{
		MessageResources mensajes=MessageResources.getMessageResources(
		propertiesDetalleAdministracion);
		ActionMessages errores = new ActionMessages();
		String reglaNavegacion="";
		boolean procesoExitoso=false;	
		if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_INGRESO_ESTANCIA)){			
			reglaNavegacion = "verDetalleAutorizacionIngEstancia";			
		}else{			
			reglaNavegacion = "verDetalleAutorizacionCapitacion";
		}			
		if(forma.getEntidadSubSeleccionada().getCodigoPk()!=ConstantesBD.codigoNuncaValidoLong){
			
			/**Pasar la entidad seleccionada a la autorizacion **/
			
			for(EntidadSubContratadaDto entidadSubContratadaDto:forma.getEntidadesSubcontratadas()){
				if(entidadSubContratadaDto.getCodEntidadSubcontratada()==forma.getEntidadSubSeleccionada().getCodigoPk()){
					forma.getAutorizacionCapitacionDto().setEntidadSubAutorizarCapitacion(entidadSubContratadaDto);
					break;
				}
			}
			
			/****************************************************/
			
				if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_INGRESO_ESTANCIA)){
					IAutorizacionIngresoEstanciaServicio autorizacionServicio =
						ManejoPacienteServicioFabrica.crearAutorizacionIngresoEstanciaServicio();					
					IIngresosEstanciaServicio ingresoServicio = ManejoPacienteServicioFabrica.crearIngresosEstancia();
					
					IEntidadesSubcontratadasServicio servicioEntSubcontratada = FacturacionServicioFabrica.crearEntidadesSubcontratadasServicio();		
					
					DTOEstanciaViaIngCentroCosto parametros = new DTOEstanciaViaIngCentroCosto();
					ICentroCostosServicio centroCostosServicio	= AdministracionFabricaServicio.crearCentroCostosServicio();
					
					DTOAutorizacionIngresoEstancia dtoAutorizacion = forma.getDtoAutorizacionIngEstancia();					
					AutorizacionesIngreEstancia autorizacion = autorizacionServicio.buscarAutorizacionIngresoEstanciaPorID(dtoAutorizacion.getCodigoPk());
										
					IngresosEstancia ingreso = ingresoServicio.obtenerIngresoEstanciaPorId(
							dtoAutorizacion.getDtoIngresoEstancia().getCodigoPk());

					EntidadesSubcontratadas entidadSubcontratada = null;
					entidadSubcontratada =servicioEntSubcontratada.obtenerEntidadesSubcontratadasporId(forma.getEntidadSubSeleccionada().getCodigoPk());
					
					ingreso.setEntidadesSubcontratadas(entidadSubcontratada);
										
					ingresoServicio.actualizarIngresoEstancia(ingreso);
					
					//Se obtiene el cetnro de costo asociado a la entidad subcontratada seleccionada
					parametros.setEntidadSubcontratada(entidadSubcontratada.getCodigoPk());
					parametros.setViaIngreso(dtoAutorizacion.getDtoIngresoEstancia().getCodigoViaIngreso());
					
					CentrosCosto centroCosto = null;
					centroCosto = centroCostosServicio.obtenerCentrosCostoPorViaIngresoEntSub(parametros);	
					
					if(centroCosto!=null){
						autorizacion.setCentrosCosto(centroCosto);	
					}
										
					HistoAutorizacionIngEstan historico = new HistoAutorizacionIngEstan();
					historico.setAutorizacionesIngreEstancia(autorizacion);
					historico.setFechaInicioAutorizacion(autorizacion.getFechaInicioAutorizacion());
					historico.setConsecutivoAdmision(autorizacion.getConsecutivoAdmision());
					historico.setDiasEstanciaAutorizados(autorizacion.getDiasEstanciaAutorizados());
					historico.setUsuarioContacta(autorizacion.getUsuarioContacta());
					historico.setCargoUsuarioContacta(autorizacion.getCargoUsuContacta());
					historico.setObservaciones(autorizacion.getObservaciones());
					if(autorizacion.getConvenios()!=null){
						historico.setConvenioRecobro(autorizacion.getConvenios().getCodigo());
					}
					historico.setOtroConvenioRecobro(autorizacion.getOtroConvenioRecobro());
					historico.setIndicativoTemporal(autorizacion.getIndicativoTemporal());
					historico.setCentroCostoSolicitante(autorizacion.getCentrosCosto().getCodigo());
					historico.setEstado(autorizacion.getEstado());
					
					Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
					String horaActual = UtilidadFecha.getHoraActual();
					
					historico.setFechaModifica(fechaActual);
					historico.setHoraModifica(horaActual);		
					
					Usuarios usuarioModifica = new Usuarios();
					usuarioModifica.setLogin(usuario.getLoginUsuario());
					
					historico.setUsuarios(usuarioModifica);
					historico.setAccionRealizada(
							ConstantesIntegridadDominio.acronimoAccionEliminarTemporal);
					
					Set<HistoAutorizacionIngEstan> setHistorico = new HashSet<HistoAutorizacionIngEstan>(0);
					setHistorico.add(historico);					
					autorizacion.setHistoAutorizacionIngEstans(setHistorico);
					autorizacion.setIndicativoTemporal(ConstantesBD.acronimoNoChar);
															
					procesoExitoso = autorizacionServicio.actualizarAutorizacionIngresoEstancia(autorizacion);					
					if(procesoExitoso){
						forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getEntidadSubcontratada().setCodigoPk(entidadSubcontratada.getCodigoPk());
						forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getEntidadSubcontratada().setCodigo(entidadSubcontratada.getCodigo());
						forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getEntidadSubcontratada().setRazonSocial(entidadSubcontratada.getRazonSocial());
						forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getEntidadSubcontratada().setDireccion(entidadSubcontratada.getDireccion());
						forma.getDtoAutorizacionIngEstancia().getDtoIngresoEstancia().getEntidadSubcontratada().setTelefono(entidadSubcontratada.getTelefono());
						forma.getDtoAutorizacionIngEstancia().setIndicativoTemporal(ConstantesBD.acronimoNoChar);
					}					
					
				}else{	
					
					/******************Cualculo de tarifa DCU 1098******************/
					
					EntidadSubContratadaDto entidadSubContratadaDto=new EntidadSubContratadaDto();
					
					for(EntidadSubContratadaDto entidadSubContratadaDtoTemp:forma.getEntidadesSubcontratadas()){
						if(entidadSubContratadaDtoTemp.getCodEntidadSubcontratada()==forma.getEntidadSubSeleccionada().getCodigoPk()){
							entidadSubContratadaDto=entidadSubContratadaDtoTemp;
							break;
						}
					}
					
					forma.getAutorizacionCapitacionDto().setEntidadSubAutorizarCapitacion(entidadSubContratadaDto);
					
					ManejoPacienteFacade manejoPacienteFacade=new ManejoPacienteFacade();
					manejoPacienteFacade.generarAutorizacionServicioTemporal(forma.getAutorizacionCapitacionDto(), false);
					
					procesoExitoso=forma.getAutorizacionCapitacionDto().isProcesoExitoso();
					
					AutorizacionCapitacionDto autorizacionEntSubCapita=forma.getAutorizacionCapitacionDto();
					if(procesoExitoso){						
						if (autorizacionEntSubCapita.getEntidadSubAutorizarCapitacion().getCodEntidadSubcontratada() <= 0){
							for (OrdenAutorizacionDto odenesAutorizar : autorizacionEntSubCapita.getOrdenesAutorizar()) {
								//Verifico si se autorizan servicios
								if (odenesAutorizar.getServiciosPorAutorizar() !=null &&
										!odenesAutorizar.getServiciosPorAutorizar().isEmpty()){
									for (ServicioAutorizacionOrdenDto serviciosAutorizar: odenesAutorizar.getServiciosPorAutorizar()) {
										for(DtoServiciosAutorizaciones servicio:forma.getListaServicios()){
											if(serviciosAutorizar.isAutorizado()){
												if(servicio.getCodigoServicio().intValue()==serviciosAutorizar.getCodigo()){
													servicio.setValorServicio(serviciosAutorizar.getValorTarifa());
													break;
												}
											}
										}
									}
								}else {
									for (MedicamentoInsumoAutorizacionOrdenDto medicamentosAutorizar: odenesAutorizar.getMedicamentosInsumosPorAutorizar()) {
										for(DtoArticulosAutorizaciones articulo:forma.getListaArticulos()){
											if(medicamentosAutorizar.isAutorizado()){
												if(articulo.getCodigoArticulo().intValue()==medicamentosAutorizar.getCodigo()){
													articulo.setValorArticulo(medicamentosAutorizar.getValorTarifa());
													break;
												}
											}
										}
									}
								}
							} 
						}
					}
					
					List<AutorizacionCapitacionDto> listaAutorizacionCapitacion = new ArrayList<AutorizacionCapitacionDto>();
					listaAutorizacionCapitacion.add(autorizacionEntSubCapita);
					obtenerMensajesError(listaAutorizacionCapitacion, errores);
					
					if(procesoExitoso){
						String consecutivoAutorEntSub = "";
						String anioConsecutivo = null;
						boolean existeConsecutivo=true;
						if(forma.getDtoAutorizacionCapitacion().isTieneSolicitud()){
							consecutivoAutorEntSub = UtilidadBD.obtenerValorConsecutivoDisponible(
									ConstantesBD.nombreConsecutivoAutorizacionEntiSub,
									usuario.getCodigoInstitucionInt());
							if(UtilidadTexto.isEmpty(consecutivoAutorEntSub)){
								errores.add("consecutivo entidad subcontratada no asignado", new ActionMessage("errors.notEspecific", 
										mensajes.getMessage("detalleAutorizacionIngEstanciaForm.errorDiasAutor")));	
								existeConsecutivo=false;
							}
							else{
								anioConsecutivo = UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoAutorizacionEntiSub,
										usuario.getCodigoInstitucionInt(), consecutivoAutorEntSub); 
							}
						}else{
							consecutivoAutorEntSub = null;
						}
						if(existeConsecutivo){
							IEntidadesSubcontratadasServicio servicioEntSubcontratada = FacturacionServicioFabrica.crearEntidadesSubcontratadasServicio();		
							EntidadesSubcontratadas entidadSubcontratada = null; 
							entidadSubcontratada = servicioEntSubcontratada.obtenerEntidadesSubcontratadasporId(forma.getEntidadSubSeleccionada().getCodigoPk());
							procesoExitoso = actualizarAutorizacionEntSubCapitacion(forma,usuario, consecutivoAutorEntSub, anioConsecutivo, entidadSubcontratada);
							
							if(procesoExitoso){
								forma.getDtoAutorizacionCapitacion().getDtoEntidadSubcontratada().setCodigoPk(entidadSubcontratada.getCodigoPk());
								forma.getDtoAutorizacionCapitacion().getDtoEntidadSubcontratada().setCodigo(entidadSubcontratada.getCodigo());
								forma.getDtoAutorizacionCapitacion().getDtoEntidadSubcontratada().setRazonSocial(entidadSubcontratada.getRazonSocial());
								forma.getDtoAutorizacionCapitacion().getDtoEntidadSubcontratada().setDireccion(entidadSubcontratada.getDireccion());
								forma.getDtoAutorizacionCapitacion().getDtoEntidadSubcontratada().setTelefono(entidadSubcontratada.getTelefono());
								forma.getDtoAutorizacionCapitacion().getAutorCapitacion().setIndicativoTemporal(ConstantesBD.acronimoNoChar);
								
								if(forma.getAutorizacionCapitacionDto().isProcesoExitoso()){
									manejoPacienteFacade.generarAcumuladoCierreTemporal(forma.getAutorizacionCapitacionDto(), false);
								}
							}
						}
					}
					/************************************/
				}			
		}else{
			errores.add("Entidad subcontratada no seleccionada", new ActionMessage("errors.notEspecific", 
					mensajes.getMessage("detalleAdministracionAutorizacion.errorEntidadesSubNoSeleccionada")));
		}
		
		if(!errores.isEmpty()){
			saveErrors(request, errores); 
		}
		forma.setProcesoExitoso(procesoExitoso);
		
		forma.setAbrirPopPupEntidadesSub(ConstantesBD.acronimoNo);
		return mapping.findForward(reglaNavegacion);
	}
	
	/**
	 * Método que obtiene los mensajes de error despúes de ejecutar el proceso de autorización
	 * 
	 * @param dtoAutorizacion
	 * @param errores
	 */
	private void obtenerMensajesError(List<AutorizacionCapitacionDto> listDtoAutorizacion, ActionMessages errores) throws IPSException {
		IAutorizacionCapitacionMundo autorizacionCapitacionMundo=new AutorizacionCapitacionMundo();
		autorizacionCapitacionMundo.obtenerMensajesError(listDtoAutorizacion, errores);
	}
	
	/**
	 * 
	 * Este Método se encarga de borrar los registros de la tabla de cierre temporal dode la fecha
	 * de cierre se anterior a la fecha del sistema
	 * 
	 * @author, Angela Maria Aguirre
	 * @throws BDException 
	 *
	 */
	public boolean eliminarAcumuladoCierreTemporal() throws BDException{
		
		ICierreTempServArtServicio cierreServArtServicio = CapitacionFabricaServicio.crearCierreTempServArtServicio();
		ICierreTempNivelAtenServServicio cierreNivelAtenServServicio = CapitacionFabricaServicio.crearCierreTempNivelAtenServServicio();
		ICierreTempGrupoServicioServicio cierreGrupoServServicio = CapitacionFabricaServicio.crearCierreTempGrupoServicioServicio();
		ICierreTempNivelAteGruServServicio cierreNivelGrupoSerServicio = CapitacionFabricaServicio.crearCierreTempNivelAteGruServServicio();
		
		ICierreTempClaseInvArtMundo cierreClaseInvArticuloMundo = CapitacionFabricaMundo.crearCierreTempClaseInvArtMundo();
		ICierreTempNivelAtenArtServicio cierreNivelArticuloServicio = CapitacionFabricaServicio.crearCierreTempNivelAtenArtServicio();
		ICierreTempNivelAteClaseInvArtMundo cierreNivelAteClaseInvArticuloMundo = CapitacionFabricaMundo.crearCierreTempNivelAteClaseInvArtMundo();
		
		String fechaActual = UtilidadFecha.getFechaActual();
		DTOBusquedaCierreTemporalServicio dtoParametrosServicios = null ;
		DTOBusquedaCierreTemporalArticulo dtoParametrosArticulos = null;
		
		
		ArrayList<CierreTempServArt> cierreServicioArticulo = cierreServArtServicio.buscarCierreTemporalServicioArticulo(dtoParametrosServicios);
		ArrayList<CierreTempNivelAtenServ> cierreNivelServicio = cierreNivelAtenServServicio.buscarCierreTemporalNivelAtencion(dtoParametrosServicios);
		ArrayList<CierreTempGrupoServicio> cierreGrupoServicio = cierreGrupoServServicio.buscarCierreTemporalNivelAtencion(dtoParametrosServicios);
		ArrayList<CierreTempNivelAteGruServ> cierreNivelGrupoServicio =  cierreNivelGrupoSerServicio.buscarCierreTemporalNivelAtencionGrupoServicio(dtoParametrosServicios);
		
		ArrayList<CierreTempClaseInvArt> cierreClaseInventarioArticulo = cierreClaseInvArticuloMundo.buscarCierreTemporalClaseInventarioArticulo(dtoParametrosArticulos);
		ArrayList<CierreTempNivelAtenArt> cierreNivelAtencionArticulo = cierreNivelArticuloServicio.buscarCierreTemporalNivelAtencion(dtoParametrosArticulos);
		ArrayList<CierreTempNivAteClInvArt> cierreNivelAteClaseInvArticulo =  cierreNivelAteClaseInvArticuloMundo.buscarCierreTemporalNivelAtencionClaseInventarioArticulo(dtoParametrosArticulos);
		boolean eliminacion=false;
		
		//Se eliminan los registros de los acumulados de las tablas temporales,
		//donde la fecha de cierre sea anterior a la fecha actual
		if(!Utilidades.isEmpty(cierreServicioArticulo)){
			
			for (CierreTempServArt cierre : cierreServicioArticulo) {
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(
						UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
					cierreServArtServicio.eliminarRegistro(cierre);
					
				}	
			}
		}
		
		if(!Utilidades.isEmpty(cierreNivelServicio)){
			
			for (CierreTempNivelAtenServ cierre : cierreNivelServicio) {
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(
						UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
					cierreNivelAtenServServicio.eliminarRegistro(cierre);
					
				}	
			}
		}
		
		if(!Utilidades.isEmpty(cierreGrupoServicio)){
			
			for (CierreTempGrupoServicio cierre : cierreGrupoServicio) {
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(
						UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
					cierreGrupoServServicio.eliminarRegistro(cierre);
					
				}	
			}
		}	
		
		if(!Utilidades.isEmpty(cierreNivelGrupoServicio)){
			
			for (CierreTempNivelAteGruServ cierre : cierreNivelGrupoServicio) {
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(
						UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
					cierreNivelGrupoSerServicio.eliminarRegistro(cierre);
					
				}	
			}
		}	
		
		if(!Utilidades.isEmpty(cierreClaseInventarioArticulo)){
			
			for (CierreTempClaseInvArt cierre : cierreClaseInventarioArticulo) {
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(
						UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
					cierreClaseInvArticuloMundo.eliminarRegistro(cierre);
					
				}	
			}
		}
		
		if(!Utilidades.isEmpty(cierreNivelAtencionArticulo)){
			
			for (CierreTempNivelAtenArt cierre : cierreNivelAtencionArticulo) {
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(
						UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
					cierreNivelArticuloServicio.eliminarRegistro(cierre);
					
				}	
			}
		}
		
		if(!Utilidades.isEmpty(cierreNivelAteClaseInvArticulo)){
			
			for (CierreTempNivAteClInvArt cierre : cierreNivelAteClaseInvArticulo) {
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(
						UtilidadFecha.conversionFormatoFechaAAp(cierre.getFechaCierre()), fechaActual)){
					cierreNivelAteClaseInvArticuloMundo.eliminarRegistro(cierre);
					
				}	
			}
		}
		
		return eliminacion;
	}
	
	/***
	 * Este método se encarga de actualizar la autorización de capitación y la de
	 * entidad subcontratada con la nueva entidad subcontratada seleccionada
	 * 
	 * @param forma
	 * @param usuario
	 * @throws Exception
	 * @author, Angela Maria Aguirre
	 * @modified jeilones
	 * @modifiedDate 28/08/12 
	 */
	private boolean actualizarAutorizacionEntSubCapitacion(AdministracionAutorizacionCapitacionDetalleForm forma,
			UsuarioBasico usuario, String consecutivo, String anioConsecutivo, EntidadesSubcontratadas entidadSubcontratada)throws Exception{
		
		DTOAutorEntidadSubcontratadaCapitacion dtoAutorEntSub = forma.getDtoAutorizacionCapitacion();
		IAutorizacionCapitacionSubServicio capitacionServicio = 
			ManejoPacienteServicioFabrica.crearAutorizacionCapitacionSubServicio();
		
		IAutorizacionesEntidadesSubServicio entSubServicio = 
			ManejoPacienteServicioFabrica.crearAutorizacionEntidadesSubServicio();	
		
		boolean procesoExitoso=false;
		
		AutorizacionesEntidadesSub autorizacionEntSub = entSubServicio.obtenerAutorizacionesEntidadesSubPorId(
				dtoAutorEntSub.getAutorizacion());
		
		AutorizacionesCapitacionSub autorizacionCapitacion =
			capitacionServicio.findById(dtoAutorEntSub.getAutorCapitacion().getCodigoPK());
				
		autorizacionCapitacion.setIndicativoTemporal(ConstantesBD.acronimoNoChar);
		procesoExitoso = capitacionServicio.actualizarAutorizacionCapitacion(autorizacionCapitacion);
		
		
		
		
		if(procesoExitoso){
			
			/*************Se actualizan los niveles de autorizacion de entidades subcontratada por articulos/servicios*************/
			
			ManejoPacienteFacade manejoPacienteFacade=new ManejoPacienteFacade();
			AutorizacionCapitacionDto autorizacionCapitacionDto=forma.getAutorizacionCapitacionDto(); 
			
			if(autorizacionCapitacionDto.getMontoCobroAutorizacion().getValorMontoCalculado()!=null){
				forma.getDtoAutorizacionCapitacion().getDtoPaciente().setValorMontoCobro(
						BigDecimal.valueOf(autorizacionCapitacionDto.getMontoCobroAutorizacion().getValorMontoCalculado()));
			}else{
				if(autorizacionCapitacionDto.getMontoCobroAutorizacion().getPorcentajeMonto()!=null){
					forma.getDtoAutorizacionCapitacion().getDtoPaciente().setValorPorcentajeMontoCobro(
						BigDecimal.valueOf(autorizacionCapitacionDto.getMontoCobroAutorizacion().getPorcentajeMonto()));
				}
			}
					
			
			for(OrdenAutorizacionDto ordenAutorizacionDto:autorizacionCapitacionDto.getOrdenesAutorizar()){
				if(!ordenAutorizacionDto.getServiciosPorAutorizar().isEmpty()){
					for(ServicioAutorizacionOrdenDto servicio:ordenAutorizacionDto.getServiciosPorAutorizar()){
						
						for(DtoServiciosAutorizaciones dtoServicio:forma.getListaServicios()){
							if(dtoServicio.getNumeroOrdenLong()==Long.parseLong(ordenAutorizacionDto.getConsecutivoOrden())
									&&dtoServicio.getCodigoServicio().intValue()==servicio.getCodigo()){
								dtoServicio.setValorServicio(servicio.getValorTarifa());
							}
						}
						
						AutorizacionesEntSubServi entSubServi=new AutorizacionesEntSubServi();
						entSubServi.setAutorizacionesEntidadesSub(autorizacionEntSub);
						Servicios servicios=new Servicios();
						servicios.setCodigo(servicio.getCodigo());
						entSubServi.setServicios(servicios);
						entSubServi.setValorTarifa(servicio.getValorTarifa());
						
						NivelAutorizacion nivelAutorizacion=new  NivelAutorizacion();
						nivelAutorizacion.setCodigoPk(servicio.getNivelAutorizacion().getCodigo());
						
						manejoPacienteFacade.actualizarNivelAutorizacionAutoEntSubServicio(entSubServi, nivelAutorizacion , false);
					}
				}else{
					if(!ordenAutorizacionDto.getMedicamentosInsumosPorAutorizar().isEmpty()){
						for(MedicamentoInsumoAutorizacionOrdenDto articulo:ordenAutorizacionDto.getMedicamentosInsumosPorAutorizar()){
							AutorizacionesEntSubArticu entSubArticulo=new AutorizacionesEntSubArticu();
							entSubArticulo.setAutorizacionesEntidadesSub(autorizacionEntSub);
							Articulo articuloEntidad=new Articulo();
							articuloEntidad.setCodigo(articulo.getCodigo());
							entSubArticulo.setArticulo(articuloEntidad);
							entSubArticulo.setValorTarifa(articulo.getValorTarifa());
							
							NivelAutorizacion nivelAutorizacion=new  NivelAutorizacion();
							nivelAutorizacion.setCodigoPk(articulo.getNivelAutorizacion().getCodigo());
							
							manejoPacienteFacade.actualizarNivelAutorizacionAutoEntSubArticulo(entSubArticulo, nivelAutorizacion , false);
						}
					}
				}
			}
			
			/*
			for (AutorizacionesEntSubServi autorizacionServicioAutori : listaAutoServicios) {
				autorizacionesEntSubServi.setCodigoPk(autorizacionServicioAutori.getCodigoPk());
				autorizacionesEntSubServi.setValorTarifa(forma.getListaServicios().get(0).getValorServicio());					
				autorizacionServiDAO.modificarServicioAutorizacionEntidadSub(autorizacionesEntSubServi);
				
			}*/
			/**********************************************************************************************************************/
				
			
			//autorizacionCapitacion.setIndicativoTemporal(ConstantesBD.acronimoSiChar);
			procesoExitoso = guardarHistorialAutorCapitacionSub(autorizacionCapitacion,usuario,
					autorizacionEntSub,ConstantesIntegridadDominio.acronimoAccionEliminarTemporal,forma);
			
			if(procesoExitoso){
				autorizacionEntSub.setEntidadesSubcontratadas(entidadSubcontratada);
				if(consecutivo!=null && !consecutivo.isEmpty()){
					autorizacionEntSub.setConsecutivoAutorizacion(consecutivo);
				}
				if(anioConsecutivo!=null && !consecutivo.isEmpty()){
					autorizacionEntSub.setAnioConsecutivo(anioConsecutivo);
				}
				procesoExitoso = entSubServicio.actualizarAutorizacionEntidadSub(autorizacionEntSub);
				
				manejoPacienteFacade.generarAutorizacionEntSubMontos(autorizacionCapitacionDto, autorizacionEntSub, false);
			}
		}			
		return procesoExitoso;
	}
	
	/**
	 * 		
	 * Este Método se encarga de registrar un histórico de la autorización de 
	 * capitación subcontratada
	 * 
	 * @param AutorizacionesCapitacionSub autorizacionCapitacion,
			  UsuarioBasico usuario,AutorizacionesEntidadesSub autorizacion
	 * @author, Angela Maria Aguirre
	 *
	 */
	private boolean guardarHistorialAutorCapitacionSub(AutorizacionesCapitacionSub autorizacionCapitacion,
			UsuarioBasico usuario,AutorizacionesEntidadesSub autorizacion, 
			String accionRealizada, AdministracionAutorizacionCapitacionDetalleForm forma)throws Exception{
		
		IHistoAutorizacionCapitaSubDAO dao = ManejoPacienteDAOFabrica.crearHistoAutorizacionCapitaSub();
						
		Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
		String horaActual = UtilidadFecha.getHoraActual();
		HistoAutorizacionCapitaSub historial = new HistoAutorizacionCapitaSub();
		
		historial.setAutorizacionesEntidadesSub(autorizacion);
		historial.setAutorizacionesCapitacionSub(autorizacionCapitacion);
		historial.setConsecutivo(autorizacionCapitacion.getConsecutivo());
		historial.setTipoAutorizacion(autorizacionCapitacion.getTipoAutorizacion());
		
		//Se asigna el convenio recobro
		if(autorizacionCapitacion.getConvenios()!=null){
			
			Convenios convenioRecobro = new Convenios();
			convenioRecobro.setCodigo(autorizacionCapitacion.getConvenios().getCodigo());
			historial.setConvenios(convenioRecobro);
			
		}
		if(!UtilidadTexto.isEmpty(autorizacionCapitacion.getOtroConvenioRecobro())){
			historial.setOtroConvenioRecobro(autorizacionCapitacion.getOtroConvenioRecobro());
		}		
		
		historial.setIndicativoTemporal(autorizacionCapitacion.getIndicativoTemporal());
	
		if(autorizacionCapitacion.getIndicadorPrioridad()!=null && 
				autorizacionCapitacion.getIndicadorPrioridad()>0){
			historial.setIndicadorPrioridad(autorizacionCapitacion.getIndicadorPrioridad());
		}
		
		historial.setFechaModifica(fechaActual);
		historial.setHoraModifica(horaActual);		
		
		Usuarios usuarioModifica = new Usuarios();
		usuarioModifica.setLogin(usuario.getLoginUsuario());
		
		historial.setUsuarios(usuarioModifica);
		historial.setAccionRealizada(accionRealizada);
		historial.setObservaciones(forma.getMotivoAnulacion());
		
		if(accionRealizada.equals(ConstantesIntegridadDominio.acronimoAccionProrrogar)){
			historial.setFechaVencimiento(forma.getDtoAutorizacionCapitacion().getFechaVencimiento());
			
		}else{
			historial.setFechaVencimiento(autorizacion.getFechaVencimiento());
		}
		
		return dao.guardarAutorizacionEntidadSubcontratada(historial);
	}
	
	/**
	 * M&eacute;todo encargado de generar el reporte de la o las 
     * autorizaciones
	 * @param Connection con, OrdenesAmbulatoriasForm forma, UsuarioBasico usuario, PersonaBasica paciente 
			HttpServletRequest request 
	 * @author Ricardo Ruiz
	 */

	private void accionImprimirAutorizacion(Connection con, AdministracionAutorizacionCapitacionDetalleForm forma, UsuarioBasico usuario,
						PersonaBasica paciente, HttpServletRequest request) {
		
		String formato=ValoresPorDefecto.getFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
		
			InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			if(!formato.trim().isEmpty()&&formato.equals(ConstantesIntegridadDominio.acronimoFormatoImpresionEstandar)){
				
				if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SERVICIOS)
						||forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SERVICIOS_ING_EST)){				
					imprimirAutorizacionesEntidadesSubServicio(con, forma, paciente, request,usuario,institucion);
				}
				else if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_ARTICULOS)
						||forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_ARTICULOS_ING_EST))
				{
					imprimirAutorizacionesEntidadesSubArticulos(con,forma, paciente, usuario, request,institucion);
				}
				else if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_INGRESO_ESTANCIA)){
					generarReporteAutorizacionFormatoIngEstancia(forma, usuario, request, paciente);
				}
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
	private void imprimirAutorizacionesEntidadesSubServicio (Connection con,AdministracionAutorizacionCapitacionDetalleForm forma,
    		PersonaBasica paciente,HttpServletRequest request,UsuarioBasico usuario,InstitucionBasica institucion)
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
		dtoPaciente.setEdadPaciente(""+dtoAutorizacion.getDtoPaciente().getEdadPacienteCapitado());
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
		dtoReporte.setDireccion(institucion.getDireccion()+separado+institucion.getTelefono());
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
				diagnostico.append(separado);
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
			/*List<String>nombresReportes=new ArrayList<String>(0);
			nombresReportes.add(nombreArchivo);*/
			//forma.setListaNombresReportes((ArrayList<String>)nombresReportes);
			forma.setNombreReporte(nombreArchivo);
			//forma.setMostrarReporte(true);
		}
	}
	
	/**IMPRIMIR ARTICULOS
	 * 		
	 * @param con
	 * @param forma
	 * @param paciente
	 * @param usuarioSesion
	 * @param request
	 */	
	private void imprimirAutorizacionesEntidadesSubArticulos(Connection con, AdministracionAutorizacionCapitacionDetalleForm forma,
			PersonaBasica paciente,UsuarioBasico usuario,
			HttpServletRequest request,InstitucionBasica institucion) 
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
		dtoReporte.setDireccion(institucion.getDireccion()+separado+institucion.getTelefono());
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
				diagnostico.append(separado);
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
			/*List<String>nombresReportes=new ArrayList<String>(0);
			nombresReportes.add(nombreArchivo);*/
			//forma.setListaNombresReportes((ArrayList<String>)nombresReportes);
			forma.setNombreReporte(nombreArchivo);
			//forma.setMostrarReporte(true);
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
	 * @created 27/08/2012
	 */
	@SuppressWarnings("deprecation")
	private void generarReporteAutorizacionFormatoIngEstancia(AdministracionAutorizacionCapitacionDetalleForm forma,
			UsuarioBasico usuario, HttpServletRequest request,PersonaBasica paciente) {
		
		
		String nombreReporte="AUTORIZACION INGRESO ESTANCIA";
		String nombreArchivo ="";
		DtoGeneralReporteIngresoEstancia dtoReporte = new DtoGeneralReporteIngresoEstancia();
		GeneradorReporteFormatoCapitacionAutorIngresoEstancia generadorReporte =
			new GeneradorReporteFormatoCapitacionAutorIngresoEstancia(dtoReporte);
		
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
		
				
	    InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				 			     			
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
		dtoReporte.setDireccion(institucion.getDireccion()+separado+institucion.getTelefono());
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
		
		if(!nombreArchivo.trim().isEmpty()){
			forma.setNombreReporte(nombreArchivo);
			//forma.setListaNombresReportes(listaNombresReportes);
			//forma.setMostrarImprimirAutorizacion(true);
		}
	} 
	
	/**
	 * 
	 * Este Método se encarga de generar el acumulado del cierre temporal de
	 * presupuesto
	 * 
	 * @param ArrayList<DTOAutorEntidadSubcontratadaCapitacion> listaAutorizaciones
	 * @author, Angela Maria Aguirre
	 * @throws BDException 
	 *
	 */
	private void disminuirAcumuladoCierreTemporal(AdministracionAutorizacionCapitacionDetalleForm forma) throws BDException{
		boolean esServicio = false;
		ICierreTempServArtServicio cierreServArtServicio = CapitacionFabricaServicio.crearCierreTempServArtServicio();
		ICierreTempNivelAtenServServicio cierreNivelAtenServServicio = CapitacionFabricaServicio.crearCierreTempNivelAtenServServicio();
		ICierreTempGrupoServicioServicio cierreGrupoServServicio = CapitacionFabricaServicio.crearCierreTempGrupoServicioServicio();
		ICierreTempNivelAteGruServServicio cierreNivelGrupoSerServicio = CapitacionFabricaServicio.crearCierreTempNivelAteGruServServicio();
		
		ICierreTempClaseInvArtMundo cierreClaseInvArticuloMundo = CapitacionFabricaMundo.crearCierreTempClaseInvArtMundo();
		ICierreTempNivelAtenArtServicio cierreNivelArticuloServicio = CapitacionFabricaServicio.crearCierreTempNivelAtenArtServicio();
		ICierreTempNivelAteClaseInvArtMundo cierreNivelAteClaseInvArticuloMundo = CapitacionFabricaMundo.crearCierreTempNivelAteClaseInvArtMundo();
		IServiciosServicio servServicio = FacturacionServicioFabrica.crearServiciosServicio();
		IArticuloDAO articuloDAO = InventarioDAOFabrica.crearArticuloDAO();
				
		DTOBusquedaCierreTemporalServicio dtoParametrosServicios = null;
		DTOBusquedaCierreTemporalArticulo dtoParametrosArticulos = null;		
		BigDecimal valorTotal=BigDecimal.ZERO;
		Servicios servicio =null;		
		int cantidadSolicitada=1;
		ArrayList<CierreTempNivelAteGruServ> listaCierreGrupoNivelServicio =null;
		CierreTempNivelAteGruServ cierreGrupoNivelServicio =null;
		ArrayList<CierreTempGrupoServicio> listaCierreGrupoServicio = null;
		CierreTempGrupoServicio cierreGrupoServicio = null;
		ArrayList<CierreTempNivelAtenServ> listaCierreNivelServicio = null;
		CierreTempNivelAtenServ cierreNivelServicio = null;
		ArrayList<CierreTempServArt> listaCierreServArt=null;
		CierreTempServArt cierreServArt = null;
		DtoArticulos dtoArt = null;		
		ArrayList<CierreTempClaseInvArt> listaCierreClaseInvArt =null;
		CierreTempClaseInvArt cierreClaseInvArt =null;
		ArrayList<CierreTempNivelAtenArt> listaCierreNivelAtencion=null;
		CierreTempNivelAtenArt cierreNivelAtencion=null;
		ArrayList<CierreTempNivAteClInvArt> listaCierreNivelAteClaseInvArticulo =null;
		CierreTempNivAteClInvArt cierreNivelAteClaseInvArticulo =null;
		DTOAutorEntidadSubcontratadaCapitacion dtoAutorizacionCapitacion = forma.getDtoAutorizacionCapitacion();
		
		if(!Utilidades.isEmpty(forma.getListaServicios())){
			esServicio=true;
		}
		
		if(esServicio){
			for (DtoServiciosAutorizaciones dtoServicio : forma.getListaServicios()) {
				
				dtoParametrosServicios = new DTOBusquedaCierreTemporalServicio();					
				
				servicio = servServicio.obtenerServicioPorId(dtoServicio.getCodigoServicio());
				
				dtoParametrosServicios.setCierreServicio(ConstantesBD.acronimoSiChar);
				dtoParametrosServicios.setCodigoContrato(dtoAutorizacionCapitacion.getDtoContrato().getCodigo());					
				listaCierreServArt = cierreServArtServicio.buscarCierreTemporalServicioArticulo(dtoParametrosServicios);
				
				if(!Utilidades.isEmpty(listaCierreServArt)){
					cierreServArt = listaCierreServArt.get(0);
					cantidadSolicitada = dtoServicio.getCantidadAutorizadaServicio();
					valorTotal = dtoServicio.getValorServicio().multiply(new BigDecimal(cantidadSolicitada));
					cierreServArt.setValorAcumulado(cierreServArt.getValorAcumulado() - valorTotal.doubleValue());
					cierreServArtServicio.attachDirty(cierreServArt);
				}	
					
				
				dtoParametrosServicios.setCodigoNivelAtencion(servicio.getNivelAtencion().getConsecutivo());					
				listaCierreNivelServicio = cierreNivelAtenServServicio.buscarCierreTemporalNivelAtencion(dtoParametrosServicios);
				
				if(!Utilidades.isEmpty(listaCierreNivelServicio)){
					cierreNivelServicio = listaCierreNivelServicio.get(0);
					cantidadSolicitada = dtoServicio.getCantidadAutorizadaServicio();
					valorTotal = dtoServicio.getValorServicio().multiply(new BigDecimal(cantidadSolicitada));
					cierreNivelServicio.setValorAcumulado(cierreNivelServicio.getValorAcumulado() -	valorTotal.doubleValue());
					cierreNivelAtenServServicio.sincronizarCierreTemporal(cierreNivelServicio);
				}
				
				dtoParametrosServicios.setCodigoGrupoServicio(servicio.getGruposServicios().getCodigo());
				listaCierreGrupoServicio = cierreGrupoServServicio.buscarCierreTemporalNivelAtencion(dtoParametrosServicios);
			
				if(!Utilidades.isEmpty(listaCierreGrupoServicio)){
					cierreGrupoServicio=listaCierreGrupoServicio.get(0);
					cantidadSolicitada = dtoServicio.getCantidadAutorizadaServicio();
					valorTotal = dtoServicio.getValorServicio().multiply(new BigDecimal(cantidadSolicitada));
					cierreGrupoServicio.setValorAcumulado(cierreGrupoServicio.getValorAcumulado() -	valorTotal.doubleValue());
					cierreGrupoServServicio.sincronizarCierreTemporal(cierreGrupoServicio);
				}
				
				dtoParametrosServicios.setCodigoGrupoServicio(servicio.getGruposServicios().getCodigo());
				
				listaCierreGrupoNivelServicio = 
					cierreNivelGrupoSerServicio.buscarCierreTemporalNivelAtencionGrupoServicio(dtoParametrosServicios);
				
				if(!Utilidades.isEmpty(listaCierreGrupoNivelServicio)){
					cierreGrupoNivelServicio = listaCierreGrupoNivelServicio.get(0);
					cantidadSolicitada = dtoServicio.getCantidadAutorizadaServicio();
					valorTotal = dtoServicio.getValorServicio().multiply(new BigDecimal(cantidadSolicitada));
					cierreGrupoNivelServicio.setValorAcumulado(cierreGrupoNivelServicio.getValorAcumulado() - valorTotal.doubleValue());
					cierreNivelGrupoSerServicio.sincronizarCierreTemporal(cierreGrupoNivelServicio);
				}
				cantidadSolicitada=1;
			}
		}else{
			
			for (DtoArticulosAutorizaciones dtoArticulo : forma.getListaArticulos()) {
				
				dtoParametrosArticulos = new DTOBusquedaCierreTemporalArticulo();
				dtoParametrosServicios = new DTOBusquedaCierreTemporalServicio();
				
				dtoArt = articuloDAO.consultarArticuloPorID(dtoArticulo.getCodigoArticulo());
				
				dtoParametrosServicios.setCierreServicio(ConstantesBD.acronimoNoChar);
				dtoParametrosServicios.setCodigoContrato(dtoAutorizacionCapitacion.getDtoContrato().getCodigo());
				
				listaCierreServArt = cierreServArtServicio.buscarCierreTemporalServicioArticulo(dtoParametrosServicios);
				
				if(!Utilidades.isEmpty(listaCierreServArt)){
					cierreServArt = listaCierreServArt.get(0);
					cantidadSolicitada = Integer.parseInt(dtoArticulo.getCantidadAutorizadaArticulo());
					valorTotal = dtoArticulo.getValorArticulo().multiply(new BigDecimal(cantidadSolicitada));
					cierreServArt.setValorAcumulado(cierreServArt.getValorAcumulado() - valorTotal.doubleValue());
					cierreServArtServicio.attachDirty(cierreServArt);
				}
				
				dtoParametrosArticulos.setCodigoContrato(dtoAutorizacionCapitacion.getDtoContrato().getCodigo());
				dtoParametrosArticulos.setCodigoClaseInventario(dtoArticulo.getCodigoClaseInvArticulo());
				
				listaCierreClaseInvArt = cierreClaseInvArticuloMundo.buscarCierreTemporalClaseInventarioArticulo(dtoParametrosArticulos);
				
				if(!Utilidades.isEmpty(listaCierreClaseInvArt)){
					cierreClaseInvArt = listaCierreClaseInvArt.get(0);
					cantidadSolicitada = Integer.parseInt(dtoArticulo.getCantidadAutorizadaArticulo());
					valorTotal = dtoArticulo.getValorArticulo().multiply(new BigDecimal(cantidadSolicitada));
					cierreClaseInvArt.setValorAcumulado(cierreClaseInvArt.getValorAcumulado().subtract(valorTotal));
					cierreClaseInvArticuloMundo.sincronizarCierreTemporal(cierreClaseInvArt);
				}
					
				if(dtoArt.getNivelAtencion()!= null){
					dtoParametrosArticulos.setCodigoNivelAtencion(dtoArt.getNivelAtencion());
					listaCierreNivelAtencion = cierreNivelArticuloServicio.buscarCierreTemporalNivelAtencion(dtoParametrosArticulos);
					
					if(!Utilidades.isEmpty(listaCierreNivelAtencion)){
						cierreNivelAtencion = listaCierreNivelAtencion.get(0);
						cantidadSolicitada = Integer.parseInt(dtoArticulo.getCantidadAutorizadaArticulo());
						valorTotal = dtoArticulo.getValorArticulo().multiply(new BigDecimal(cantidadSolicitada));
						cierreNivelAtencion.setValorAcumulado(cierreNivelAtencion.getValorAcumulado() - valorTotal.doubleValue());
						cierreNivelArticuloServicio.sincronizarCierreTemporal(cierreNivelAtencion);
					}
					
					listaCierreNivelAteClaseInvArticulo = 
						cierreNivelAteClaseInvArticuloMundo.buscarCierreTemporalNivelAtencionClaseInventarioArticulo(dtoParametrosArticulos);
					
					if(!Utilidades.isEmpty(listaCierreNivelAteClaseInvArticulo)){
						cierreNivelAteClaseInvArticulo = listaCierreNivelAteClaseInvArticulo.get(0);
						cantidadSolicitada = Integer.parseInt(dtoArticulo.getCantidadAutorizadaArticulo());
						valorTotal = dtoArticulo.getValorArticulo().multiply(new BigDecimal(cantidadSolicitada));
						cierreNivelAteClaseInvArticulo.setValorAcumulado(cierreNivelAteClaseInvArticulo.getValorAcumulado().subtract(valorTotal));
						cierreNivelAteClaseInvArticuloMundo.sincronizarCierreTemporal(cierreNivelAteClaseInvArticulo);
					}	
				}
				cantidadSolicitada=1;
			}
		}
	}
	
	/**
	 * En este método se realizan la autorización de Entidad subcontratada, para las  
	 * 
	 * @param request
	 * @param forma
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 */
	public boolean realizarAutorizacionEntidadSubcontratada(HttpServletRequest request,
			AdministracionAutorizacionCapitacionDetalleForm forma,
			ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario) throws Exception{
		MessageResources mensajes=MessageResources.getMessageResources(
		propertiesDetalleAdministracion);
		ActionMessages errores = new ActionMessages();
		boolean procesoExitoso=false;	
		String consecutivoAutorEntSub = UtilidadBD.obtenerValorConsecutivoDisponible(
				ConstantesBD.nombreConsecutivoAutorizacionEntiSub,
				usuario.getCodigoInstitucionInt());
		Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
		String horaActual = UtilidadFecha.getHoraActual();
		
		if(UtilidadTexto.isEmpty(consecutivoAutorEntSub)){
			
			errores.add("consecutivo entidad subcontratada no asignado", new ActionMessage("errors.notEspecific", 
					mensajes.getMessage("detalleAutorizacionIngEstanciaForm.errorDiasAutor")));	
			
		}else{
			/*String anioConsecutivo = UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoAutorizacionEntiSub,
					usuario.getCodigoInstitucionInt(), consecutivoAutorEntSub);*/
			IAutorizacionesEntidadesSubServicio entSubServicio = 
				ManejoPacienteServicioFabrica.crearAutorizacionEntidadesSubServicio();	
			
			AutorizacionesEntidadesSub autorizacionEntSub = entSubServicio.obtenerAutorizacionesEntidadesSubPorId(
					forma.getDtoAutorizacionCapitacion().getAutorizacion());
			autorizacionEntSub.setEstado(ConstantesIntegridadDominio.acronimoAutorizado);
			autorizacionEntSub.setFechaAutorizacion(fechaActual);
			autorizacionEntSub.setHoraAutorizacion(horaActual);
			autorizacionEntSub.setConsecutivoAutorizacion(consecutivoAutorEntSub);
			
			//autorizacionEntSub.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoManual);
			
			CentroAtencion centroAtencion=new CentroAtencion();
			centroAtencion.setConsecutivo(usuario.getCodigoCentroAtencion());
			
			autorizacionEntSub.setCentroAtencion(centroAtencion);
			
			autorizacionEntSub.setFechaModificacion(fechaActual);
			autorizacionEntSub.setHoraModificacion(horaActual);
			autorizacionEntSub.setUsuarioModificacion(usuario.getLoginUsuario());
			autorizacionEntSub.setContabilizado(ConstantesBD.acronimoNo);
			autorizacionEntSub.setObservaciones(forma.getDtoAutorizacionCapitacion().getObservaciones());
			
			procesoExitoso = entSubServicio.actualizarAutorizacionEntidadSub(autorizacionEntSub);
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores); 
			forma.setImprimirAutorizacion(false);
		}
		forma.setProcesoExitoso(procesoExitoso);
		if(procesoExitoso){
			forma.getDtoAutorizacionCapitacion().setFechaAutorizacion(fechaActual);
			forma.getDtoAutorizacionCapitacion().setConsecutivoAutorizacion(consecutivoAutorEntSub);
			forma.getDtoAutorizacionCapitacion().setEstado(ConstantesIntegridadDominio.acronimoAutorizado);
			forma.setMostrarBotonAutorizarEntSub(false);
			//forma.setImprimirAutorizacion(true);
			forma.setAbrirPopPupAnulacion(ConstantesBD.acronimoNo);
			forma.setAbrirPopPupEntidadesSub(ConstantesBD.acronimoNo);
			forma.setAbrirPopPupProrroga(ConstantesBD.acronimoNo);
			forma.setAbrirPopUpConfirmarAutoEntSub(ConstantesBD.acronimoNo);
			forma.setAbrirPopUpModificacionDiasEstancia(ConstantesBD.acronimoNo);
		}
		return procesoExitoso;
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
	 * @return
	 * @author hermorhu
	 * @created 21-feb-2013
	 */
	public ActionForward validarImpresionAutorizacion(HttpServletRequest request,
			AdministracionAutorizacionCapitacionDetalleForm forma,
			ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario, Connection con){
		
		ActionMessages errores = new ActionMessages();
		String forward = "";

		if(forma.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_INGRESO_ESTANCIA)){
			forward = FORWARD_DET_AUTORIZACION_INGESTANCIA;
		} else {
			forward = FORWARD_DET_AUTORIZACION_CAPITACION;
		}
		
		String formato = ValoresPorDefecto.getFormatoImpresionAutorEntidadSub(usuario.getCodigoInstitucionInt());
		
		if(formato == null || formato.trim().isEmpty()) {
			
			MessageResources mensajes = MessageResources.getMessageResources(propertiesDetalleAdministracion);	
			errores.add("", new ActionMessage(KEY_ERROR_PARAM_GRAL_MANEJOPACIENTE, mensajes.getMessage("detalleAdministracionAutorizacion.errorDefinirFormatoImpresion")));
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
						
						accionImprimirAutorizacion(con, forma,usuario,paciente, request);
					}
				} else {
					//No se muestra popup de primera impresion
					forma.setMostrarPopupPrimeraImpresion(false);
					//Si existe la Entrega de la autorizacion se imprime el label 'Copia'
					forma.setEsImpresionOriginal(false);
					
					accionImprimirAutorizacion(con, forma,usuario,paciente, request);
				}
			} 
			//Si no existe Autorizacion Entidad Subcontratada
			else {
				//No se muestra popup de primera impresion
				forma.setMostrarPopupPrimeraImpresion(false);
				//No se muestra el label en la impresion
				forma.setEsImpresionOriginal(null);	
				
				accionImprimirAutorizacion(con, forma, usuario, paciente, request);
			}
			
		}	
		
		if(!errores.isEmpty()){
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
	 * @return
	 * @author hermorhu
	 * @created 21-feb-2013
	 */
	public ActionForward accionGuardarDatosPrimeraImpresion(Connection con, HttpServletRequest request,	AdministracionAutorizacionCapitacionDetalleForm forma, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario) {
		
		ActionMessages errores = new ActionMessages();

		MessageResources mensajes = MessageResources.getMessageResources(propertiesDetalleAdministracion);	
			
		if(UtilidadTexto.isEmpty(forma.getPersonaRecibeAutorizacion())) {
			errores.add("", new ActionMessage(KEY_ERROR_NO_ESPECIFIC, mensajes.getMessage("detalleAdministracionAutorizacion.errorPersonaRecibeAutorizacionRequerido")));	
			forma.getDtoAutorizacionCapitacion().setAutorizacionEntrega(null);
			
		} else {
		
			try {
				ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
				
				forma.getDtoAutorizacionCapitacion().getAutorizacionEntrega().setPersonaRecibe(forma.getPersonaRecibeAutorizacion());
				forma.getDtoAutorizacionCapitacion().getAutorizacionEntrega().setObservaciones(forma.getObservacionesEntregaAutorizacion());
				
				//guarda los datos de entrega de la autorizacion original 
				manejoPacienteFacade.guardarEntregaAutorizacionEntidadSubContratadaOriginal(forma.getDtoAutorizacionCapitacion().getAutorizacionEntrega());
			
				//imprime la autorizacion
				accionImprimirAutorizacion(con, forma, usuario, paciente, request);
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
			AdministracionAutorizacionCapitacionDetalleForm forma, ActionMapping mapping, UsuarioBasico usuario){
		
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
		
		request.setAttribute("formaPopUpPrimeraImpresion", "AdministracionAutorizacionCapitacionDetalleForm");
		
		return mapping.findForward(FORWARD_POPUP_PRIMERA_IMPRESION);	
	}
	
}
