package com.princetonsa.action.manejoPaciente;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.ConstantesBDManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.AdministracionAutorizacionCapitacionRangoForm;
import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.dto.manejoPaciente.ArticuloAutorizadoCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.ServicioAutorizadoCapitacionDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntidadesSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionCapitacionSubServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionIngresoEstanciaServicio;

/**
 * Esta clase se encarga de de procesar las solicitudes de 
 * la administración de las autorizaciones de capitación por rango
 * 
 * @author Angela Maria Aguirre
 * @since 29/12/2010
 */
public class AdministracionAutorizacionCapitacionRangoAction extends Action {
	
	
	private static final String INDICATIVO_TEMPORAL = "(Temporal)";
	
	private MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.manejoPaciente.AdministracionAutorizacionCapitacionRangoForm");
	
	/**
	 * 
	 * Este Método se encarga de ejecutar las acciones sobre las páginas
	 * de administración de las autorizaciones de capitación por rango
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
		if (form instanceof AdministracionAutorizacionCapitacionRangoForm) {
			
			AdministracionAutorizacionCapitacionRangoForm forma = (AdministracionAutorizacionCapitacionRangoForm)form;
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());			
			
			UtilidadTransaccion.getTransaccion().begin();
			
			try{
				if(estado.equals("empezar")){
					forward= empezar(request,forma,mapping,usuario );				
					
				}else if(estado.equals("consultar")){
					
					forward= consultarAutorizaciones(request,forma, mapping,usuario);
					
				}else if(estado.equals("ordenar")){
					
					forward= accionOrdenar(forma,usuario,mapping);
					
				}else if(estado.equals("cambiarTipoConsecutivo")){
					forward= mapping.findForward("principal");	
				}
				UtilidadTransaccion.getTransaccion().commit();
			}catch (Exception e) {
				UtilidadTransaccion.getTransaccion().rollback();
				Log4JManager.error("Error en la consulta de autorizaciones por rango", e);
			}		
		}
		return forward;
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar las autorizaciones de capitación o de 
	 * ingreso estancia del paciente cargado en sesión
	 * 
	 * @param AdministracionAutorizacionCapitacionPacienteForm forma, ActionMapping mapping
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward empezar(HttpServletRequest request,
			AdministracionAutorizacionCapitacionRangoForm forma,
			ActionMapping mapping,UsuarioBasico usuario ){
		
		IEntidadesSubcontratadasServicio entidadServicio = 
			FacturacionServicioFabrica.crearEntidadesSubcontratadasServicio();
		
		ArrayList<DtoEntidadSubcontratada> listaEntidadesSub  =
			entidadServicio.listarEntidadesSubXCentroCostoActivo(ConstantesBD.codigoNuncaValido);
		
		forma.reset();
		
		if(!Utilidades.isEmpty(listaEntidadesSub)){
			forma.setListaEntidadesSub(listaEntidadesSub);
		}
		
		obtenerTiposConsecutivosAutorizacion(forma);
		
		return mapping.findForward("principal");	
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar las autorizaciones de capitación o de 
	 * ingreso estancia del paciente cargado en sesión
	 * 
	 * @param AdministracionAutorizacionCapitacionPacienteForm forma, ActionMapping mapping
	 * @return ActionForward
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ActionForward consultarAutorizaciones(HttpServletRequest request,
			AdministracionAutorizacionCapitacionRangoForm forma,
			ActionMapping mapping,UsuarioBasico usuario ){
		
		ArrayList<DTOAdministracionAutorizacion> listaAutorizaciones = null;
		
		//forma.reset();		
		IAutorizacionIngresoEstanciaServicio autorizacionIngresoServicio= 
			ManejoPacienteServicioFabrica.crearAutorizacionIngresoEstanciaServicio();	
		
		Date fechaVencimientoAutorizacion = null;
		
		forma.getDtoFiltros().setConsecutivoIncioAutorizacion(forma.getConsInAutorizacion());
		forma.getDtoFiltros().setConsecutivoFinAutorizacion(forma.getConsFinAutorizacion());
		
		//Se setea este estado de la autorizacón para que consulte las autorizaciones en estado AUTORIZADA sin importar el indicativo temporal
		forma.getDtoFiltros().setEstadoAutorizacion(ConstantesIntegridadDominio.acronimoAutorizado);
		
		forma.getDtoFiltros().setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		
		//Se setea este valor para que consulte las autorizaciones en estado AUTORIZADA asociada a Autorizacion de Entidad Subcontratata
		//o que estan en estado AUTORIZADA y que en teoria no tienen asociada Autorizacionde Entidad Subcontratada
		forma.getDtoFiltros().setAdministracioCapitacion(true);
		
		ArrayList<DTOAdministracionAutorizacion> listaAutorizacionesIngEstancia = new ArrayList<DTOAdministracionAutorizacion>(0);
		
		//Debe ser consultable sino se selecciona un tipo de consecutivo 
		//o si se selecciona tipo de consecutivo de autorizacion de capitacion ya que los ingresos por estancia no tienen autorizacion de entidades subcontratadas		
		if(forma.getDtoFiltros().getTipoConsecutivoAutorizacion().equals(""+ConstantesBD.codigoNuncaValido)
				||forma.getDtoFiltros().getTipoConsecutivoAutorizacion().equals(""+ConstantesBDManejoPaciente.codigoTipoConsecutivoAutorizacionCapitacion)){
			listaAutorizacionesIngEstancia =  autorizacionIngresoServicio.obtenerAutorizacionesPorRango(forma.getDtoFiltros());
		}
		
		String nombreEntidadSubTemporal="";
		
		if(!Utilidades.isEmpty(listaAutorizacionesIngEstancia)){
			
			ArrayList<DTOAdministracionAutorizacion> listaFinalAutorizacionesIngEstancia =
				new ArrayList<DTOAdministracionAutorizacion>();
			String fechaVencimientoDiasAutorizados="";
			for(DTOAdministracionAutorizacion autorizacion :listaAutorizacionesIngEstancia){					
				
				// se omiten las autorizaciones que de ingreso estancia que tienen
				//asociada una autorización de capitación
				if(autorizacion.getIdIngresoEstanciaCapitacion() == null){
					fechaVencimientoDiasAutorizados = UtilidadFecha.incrementarDiasAFecha(
							UtilidadFecha.conversionFormatoFechaAAp(autorizacion.getFechaInicioAutorizacion()),
							Integer.valueOf(autorizacion.getDiasEstanciaAutorizados()).intValue(), false);
										
					fechaVencimientoAutorizacion =  UtilidadFecha.conversionFormatoFechaStringDate(fechaVencimientoDiasAutorizados);
					autorizacion.setFechaVencimientoAutorizacion(fechaVencimientoAutorizacion);
					
					if(UtilidadTexto.isEmpty(autorizacion.getDescripcionEntidadSubIngEst())){
						nombreEntidadSubTemporal = autorizacion.getEntidadSubcontratada().getRazonSocial();
					}else{
						nombreEntidadSubTemporal = autorizacion.getDescripcionEntidadSubIngEst();
					}
					
					if(autorizacion.getIndicativoTemporal().charAt(0) == ConstantesBD.acronimoSiChar){
						
						nombreEntidadSubTemporal += INDICATIVO_TEMPORAL;
						autorizacion.getEntidadSubcontratada().setRazonSocial(nombreEntidadSubTemporal);
					}
					
					autorizacion.setTipoAutorizacion(ConstantesIntegridadDominio.TIPO_AUTORIZACION_INGRESO_ESTANCIA);
					
					listaFinalAutorizacionesIngEstancia.add(autorizacion);
					
					String tipoTarifario = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(
							usuario.getCodigoInstitucionInt());
					if(UtilidadTexto.isEmpty(tipoTarifario)){
						tipoTarifario=ConstantesBD.codigoTarifarioCups+"";
					}
					
					autorizacion.setListaServiciosAuto(consultarServicios(autorizacion.getCodigoPkAutoEntSub(), Long.parseLong(tipoTarifario)));
					autorizacion.setListaArticulosAuto(consultarArticulos(autorizacion.getCodigoPkAutoEntSub(), usuario.getCodigoInstitucionInt()));
				}
			}
			
			if(!Utilidades.isEmpty(listaFinalAutorizacionesIngEstancia)){
				listaAutorizaciones = new ArrayList<DTOAdministracionAutorizacion>();
				listaAutorizaciones.addAll(listaFinalAutorizacionesIngEstancia);
			}								
		}			
		
		IAutorizacionCapitacionSubServicio autorizacionCapitacionServicio= 
				ManejoPacienteServicioFabrica.crearAutorizacionCapitacionSubServicio();
		
		ArrayList<DTOAdministracionAutorizacion> listaAutorizacionesCapitacion = 
			autorizacionCapitacionServicio.obtenerAutorizacionesPorRango(forma.getDtoFiltros());
		
		if(!Utilidades.isEmpty(listaAutorizacionesCapitacion)){
		
			ArrayList<DTOAdministracionAutorizacion> listaFinalAutorizacionesCapitacion = 
				new ArrayList<DTOAdministracionAutorizacion>(); 
			
			for(DTOAdministracionAutorizacion autorizacion :listaAutorizacionesCapitacion){
				
				if(autorizacion.getFechaVencimientoAutorizacion()!=null){
					
					if(UtilidadTexto.isEmpty(autorizacion.getDescripcionEntidadSubIngEst())){
						nombreEntidadSubTemporal = autorizacion.getEntidadSubcontratada().getRazonSocial();
					}else{
						nombreEntidadSubTemporal = autorizacion.getDescripcionEntidadSubIngEst();
					}
					
					if(autorizacion.getIndicativoTemporal().charAt(0) == ConstantesBD.acronimoSiChar){
						
						nombreEntidadSubTemporal += INDICATIVO_TEMPORAL;
						autorizacion.getEntidadSubcontratada().setRazonSocial(nombreEntidadSubTemporal);
					}
					if(autorizacion.getIdIngresoEstanciaCapitacion() == null){
						if(autorizacion.isEsSolicitudArticulo()){
							autorizacion.setTipoAutorizacion(ConstantesIntegridadDominio.TIPO_AUTORIZACION_ARTICULOS);	
						}else{
							autorizacion.setTipoAutorizacion(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SERVICIOS);
						}	
					}
					else{
						if(autorizacion.isEsSolicitudArticulo()){
							autorizacion.setTipoAutorizacion(ConstantesIntegridadDominio.TIPO_AUTORIZACION_ARTICULOS_ING_EST);	
						}else{
							autorizacion.setTipoAutorizacion(ConstantesIntegridadDominio.TIPO_AUTORIZACION_SERVICIOS_ING_EST);
						}
					}
					
					listaFinalAutorizacionesCapitacion.add(autorizacion);
					
					String tipoTarifario = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(
							usuario.getCodigoInstitucionInt());
					if(UtilidadTexto.isEmpty(tipoTarifario)){
						tipoTarifario=ConstantesBD.codigoTarifarioCups+"";
					}
					
					autorizacion.setListaServiciosAuto(consultarServicios(autorizacion.getCodigoPkAutoEntSub(), Long.parseLong(tipoTarifario)));
					autorizacion.setListaArticulosAuto(consultarArticulos(autorizacion.getCodigoPkAutoEntSub(), usuario.getCodigoInstitucionInt()));
				}				
			}		
			
			if(!Utilidades.isEmpty(listaFinalAutorizacionesCapitacion)){
			
				if(listaAutorizaciones==null){
					listaAutorizaciones = new ArrayList<DTOAdministracionAutorizacion>();
				}
				listaAutorizaciones.addAll(listaFinalAutorizacionesCapitacion);		
			}		
		}			
		
		
		if(!Utilidades.isEmpty(listaAutorizaciones)){
			forma.setListaAutorizaciones(listaAutorizaciones);
			SortGenerico sortG=new SortGenerico("ConsecutivoAutorizacion",true);
			Collections.sort(forma.getListaAutorizaciones(),sortG);
			forma.setSinAutorizaciones(false);
		}		
						
		return mapping.findForward("listadoAutorizaciones");	
		
	}	
	
	/**
	 * Este método se encarga de ordenar las columnas del resultado 
	 * de la búsqueda de autorizaciones
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return ActionForward
	 */
	public ActionForward accionOrdenar(AdministracionAutorizacionCapitacionRangoForm forma, 
			UsuarioBasico usuario, ActionMapping mapping){
		
		boolean ordenamiento = false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
				
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaAutorizaciones(),sortG);

		return mapping.findForward("listadoAutorizaciones");
	}

	public void obtenerTiposConsecutivosAutorizacion(AdministracionAutorizacionCapitacionRangoForm forma){
    	ArrayList<DtoCheckBox> tiposConsecutivoAutorizacion = new ArrayList<DtoCheckBox>();
    	DtoCheckBox tipo = new DtoCheckBox();
    	tipo.setCodigo(String.valueOf(ConstantesBDManejoPaciente.codigoTipoConsecutivoAutorizacionCapitacion));
    	tipo.setNombre(fuenteMensaje.getMessage(
    							"administracionAutorizacionCapitacionRangoForm.tipoConsecutivoAutorizacion"+
    							ConstantesBDManejoPaciente.codigoTipoConsecutivoAutorizacionCapitacion));
    	tiposConsecutivoAutorizacion.add(tipo);
    	
    	tipo= new DtoCheckBox();
    	tipo.setCodigo(String.valueOf(ConstantesBDManejoPaciente.codigoTipoConsecutivoAutorizacionEntidadSub));
    	tipo.setNombre(fuenteMensaje.getMessage(
    							"administracionAutorizacionCapitacionRangoForm.tipoConsecutivoAutorizacion"+
    							ConstantesBDManejoPaciente.codigoTipoConsecutivoAutorizacionEntidadSub));
    	tiposConsecutivoAutorizacion.add(tipo);
    	
    	forma.setListaTiposConsecutivoAutorizacion(tiposConsecutivoAutorizacion);
    }
	
	/**
	 * Consulta los servicios de una autorizacion
	 * 
	 * @param codAutorizacion
	 * @param tipoTarifario
	 * @return
	 * @author jeilones
	 * @created 13/08/2012
	 */
	public List<DtoServiciosAutorizaciones> consultarServicios(long codAutorizacion,long tipoTarifario){
		ManejoPacienteFacade manejoPacienteFacade=new ManejoPacienteFacade();
		List<ServicioAutorizadoCapitacionDto> listaServicios=new ArrayList<ServicioAutorizadoCapitacionDto>(0);
		try {
			listaServicios = manejoPacienteFacade.consultarServiciosAutorizadosCapitacion(codAutorizacion, tipoTarifario, true);
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
				
				//define el origen si es solicitud, peticion u orden ambulatoria 
				if(dtoServicio.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_ORDEN_AMB
						||dtoServicio.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_PETICION
						||dtoServicio.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_SOLICITUD
						||dtoServicio.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_CARGOS_DIRECTOS){
					servicio.setNumeroOrdenLong(dtoServicio.getConsecutivo());
					servicio.setFechaOrden(dtoServicio.getFechaGeneracion());
					servicio.setCodigoPropietario(""+dtoServicio.getCodServ());
					servicio.setDescripcionServicio(dtoServicio.getNomServ());
					servicio.setCantidadAutorizadaServicio(Long.valueOf(dtoServicio.getCantidad()).intValue());
					servicio.setDescripcionNivelAtencion(dtoServicio.getNivelAtencion());
					servicio.setDiagnostico(dtoServicio.getAcronimoDiag());
					servicio.setTipoCieDx(dtoServicio.getTipoCieDiag());
					servicio.setDescripcionDiagnostico(dtoServicio.getDiagDescripcion());
					servicio.setTipoSolicitud(dtoServicio.getTipoSolicitud());
					if(dtoServicio.getValorTarifa()!=null){
						servicio.setValorServicio(new BigDecimal(dtoServicio.getValorTarifa()));
					}
				}
				else{
					//servicios de ingreso estancia
					if(dtoServicio.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_ING_EST){
						servicio.setNumeroOrdenLong(null);
						servicio.setFechaOrden(null);
						servicio.setCodigoPropietario(""+dtoServicio.getCodServ());
						servicio.setDescripcionServicio(dtoServicio.getNomServ());
						servicio.setCantidadAutorizadaServicio(Long.valueOf(dtoServicio.getCantidad()).intValue());
						servicio.setDescripcionNivelAutorizacion(dtoServicio.getNivelAtencion());
						servicio.setDiagnostico(dtoServicio.getAcronimoDiag());
						servicio.setTipoCieDx(dtoServicio.getTipoCieDiag());
						servicio.setDescripcionDiagnostico(dtoServicio.getDiagDescripcion());
						servicio.setTipoSolicitud(dtoServicio.getTipoSolicitud());
						if(dtoServicio.getValorTarifa()!=null){
							servicio.setValorServicio(new BigDecimal(dtoServicio.getValorTarifa()));
						}
					}
				}
				servicios.add(servicio);
			}
		}
		return servicios;
	}
	
	/**
	 * consulta los articulos de una autorizacion
	 * @param codAutorizacion
	 * @param codInsitucion
	 * @return
	 * @author jeilones
	 * @created 13/08/2012
	 */
	public List<DtoArticulosAutorizaciones> consultarArticulos(long codAutorizacion,int codInsitucion){
		
		ManejoPacienteFacade manejoPacienteFacade=new ManejoPacienteFacade();
		List<ArticuloAutorizadoCapitacionDto> listaArticulos =new ArrayList<ArticuloAutorizadoCapitacionDto>(); 
		//detalleArticulo.obtenerDetalleArticulosAutorCapitacion(dtoAutorEntSub.getAutorizacion());
		try {
			listaArticulos = manejoPacienteFacade.consultarArticulosAutorizadosCapitacion(codAutorizacion,codInsitucion,true);
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
				//define el origen si es solicitud, peticion u orden ambulatoria 
				if(dtoArticulo.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_ORDEN_AMB
						||dtoArticulo.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_PETICION
						||dtoArticulo.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_SOLICITUD
						||dtoArticulo.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_CARGOS_DIRECTOS){
					
					// para mostrar la etiqueta correcta en la lista de articulos autorizados
					/*if(!dtoAutorEntSub.isEsOrden()&&!dtoAutorEntSub.isEsSolicitud()&&dtoArticulo.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_ORDEN_AMB){
						dtoAutorEntSub.setEsOrden(true);
						dtoAutorEntSub.setEsSolicitud(false);
					}else{
						if(!dtoAutorEntSub.isEsOrden()&&!dtoAutorEntSub.isEsSolicitud()&&dtoArticulo.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_SOLICITUD){
							dtoAutorEntSub.setEsOrden(false);
							dtoAutorEntSub.setEsSolicitud(true);
						}
					}*/
					
					articulo.setNumeroOrdenLong(dtoArticulo.getConsecutivo());
					articulo.setFechaOrden(dtoArticulo.getFechaGeneracion());
					articulo.setCodigoArticulo(Long.valueOf(dtoArticulo.getCodArt()).intValue());
					articulo.setDescripcionArticulo(dtoArticulo.getNomArt());
					articulo.setNaturalezaArticulo(dtoArticulo.getNaturalezaArticulo());
					articulo.setCantidadAutorizadaArticulo(""+dtoArticulo.getCantidad());
					articulo.setDiagnostico(dtoArticulo.getAcronimoDiag());
					articulo.setTipoCieDx(dtoArticulo.getTipoCieDiag());
					articulo.setDescripcionDiagnostico(dtoArticulo.getDiagDescripcion());
					if(dtoArticulo.getValorTarifa()!=null){
						articulo.setValorArticulo(new BigDecimal(dtoArticulo.getValorTarifa()));
					}
				}
				else{
					//articulos de ingreso estancia
					if(dtoArticulo.getTipoAutorizacion() == ConstantesIntegridadDominio.TIPO_AUTORIZACION_ING_EST){
						articulo.setNumeroOrdenLong(null);
						articulo.setFechaOrden(null);
						articulo.setCodigoArticulo(Long.valueOf(dtoArticulo.getCodArt()).intValue());
						articulo.setDescripcionArticulo(dtoArticulo.getNomArt());
						articulo.setNaturalezaArticulo(dtoArticulo.getNaturalezaArticulo());
						articulo.setCantidadAutorizadaArticulo(""+dtoArticulo.getCantidad());
						articulo.setDiagnostico(dtoArticulo.getAcronimoDiag());
						articulo.setTipoCieDx(dtoArticulo.getTipoCieDiag());
						articulo.setDescripcionDiagnostico(dtoArticulo.getDiagDescripcion());
						if(dtoArticulo.getValorTarifa()!=null){
							articulo.setValorArticulo(new BigDecimal(dtoArticulo.getValorTarifa()));
						}
					}
				}
				articulos.add(articulo);
			}
		}
		return articulos;
	}
}
