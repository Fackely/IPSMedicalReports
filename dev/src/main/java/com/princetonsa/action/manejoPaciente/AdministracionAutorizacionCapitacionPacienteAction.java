package com.princetonsa.action.manejoPaciente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.AdministracionAutorizacionCapitacionPacienteForm;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoServiciosAutorizaciones;
import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.dto.manejoPaciente.ArticuloAutorizadoCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.ServicioAutorizadoCapitacionDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionCapitacionSubServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionIngresoEstanciaServicio;

/**
 * Esta clase se encarga de de procesar las solicitudes de 
 * la administración de las autorizaciones de capitación por paciente
 * cargado en sesión
 * 
 * @author Angela Maria Aguirre
 * @since 29/12/2010
 */
public class AdministracionAutorizacionCapitacionPacienteAction extends Action {
	
	
	private static final String INDICATIVO_TEMPORAL = "(Temporal)";
		
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
		if (form instanceof AdministracionAutorizacionCapitacionPacienteForm) {
			
			AdministracionAutorizacionCapitacionPacienteForm forma = (AdministracionAutorizacionCapitacionPacienteForm)form;
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			
			UtilidadTransaccion.getTransaccion().begin();
			
			try{
				if(estado.equals("empezar")){
					
					forward= empezar(request,forma, mapping,paciente,usuario);
					
				}else if(estado.equals("ordenar")){
					forward= accionOrdenar(forma,usuario,mapping);
				}
				UtilidadTransaccion.getTransaccion().commit();
			}catch (Exception e) {
				UtilidadTransaccion.getTransaccion().rollback();
				Log4JManager.error("Error en la consulta de autorizaciones para paciente cargado en sesión", e);
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
			AdministracionAutorizacionCapitacionPacienteForm forma,
			ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario ){
		
		ArrayList<DTOAdministracionAutorizacion> listaAutorizaciones = null;
		
		forma.reset();
		
		if(paciente == null || paciente.getCodigoPersona()<=0){
			Connection con = HibernateUtil.obtenerConexion();			
			Logger logger = Logger.getLogger(AdministracionAutorizacionCapitacionPacienteAction.class);
			
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, 
					"Paciente no cargado", "errors.paciente.noCargado", true);			
			
		}else{
			IAutorizacionIngresoEstanciaServicio autorizacionIngresoServicio= 
				ManejoPacienteServicioFabrica.crearAutorizacionIngresoEstanciaServicio();	
			
			DTOAdministracionAutorizacion dtoAdministracion = new DTOAdministracionAutorizacion();
			dtoAdministracion.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			DtoPaciente dtoPaciente = new DtoPaciente();			
			Date fechaVencimientoAutorizacion;
			
			dtoPaciente.setCodigo(paciente.getCodigoPersona());
			dtoAdministracion.setPaciente(dtoPaciente);
			dtoAdministracion.setAdministracionPoblacionCapitada(true);
						
			ArrayList<DTOAdministracionAutorizacion> listaAutorizacionesIngEstancia =  
				autorizacionIngresoServicio.obtenerAutorizacionesPorPaciente(dtoAdministracion);
									
			String nombreEntidadSubTemporal ="";
			
			if(!Utilidades.isEmpty(listaAutorizacionesIngEstancia)){
				
				ArrayList<DTOAdministracionAutorizacion> listaFinalAutorizacionesIngEstancia =
					new ArrayList<DTOAdministracionAutorizacion>();
				
				for(DTOAdministracionAutorizacion autorizacion :listaAutorizacionesIngEstancia){					
					// se omiten las autorizaciones que de ingreso estancia que tienen
					//asociada una autorización de capitación
					if(autorizacion.getIdIngresoEstanciaCapitacion() == null){
						String fechaVencimientoDiasAutorizados = UtilidadFecha.incrementarDiasAFecha(
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
				autorizacionCapitacionServicio.obtenerAutorizacionesPorPaciente(dtoAdministracion);
			
			if(!Utilidades.isEmpty(listaAutorizacionesCapitacion)){
			
				ArrayList<DTOAdministracionAutorizacion> listaFinalAutorizacionesCapitacion = 
					new ArrayList<DTOAdministracionAutorizacion>(); 
				
				for(DTOAdministracionAutorizacion autorizacion :listaAutorizacionesCapitacion){
					
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
				}		
				
				if(!Utilidades.isEmpty(listaFinalAutorizacionesCapitacion)){
				
					if(listaAutorizaciones==null){
						listaAutorizaciones = new ArrayList<DTOAdministracionAutorizacion>();
					}
					
					listaAutorizaciones.addAll(listaFinalAutorizacionesCapitacion);		
				}		
			}			
		}
		
		if(!Utilidades.isEmpty(listaAutorizaciones)){
			//----------------------------------------------------------------------------------------------
			// Se filtran para no repetir autorizaciones
			ArrayList<Long> listaAutorizacionesAgregadas = new ArrayList<Long>();
			ArrayList<DTOAdministracionAutorizacion> listaAutorizacionesNoRepetidas = new ArrayList<DTOAdministracionAutorizacion>();

			String tipoTarifario = ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(
					usuario.getCodigoInstitucionInt());
			if(UtilidadTexto.isEmpty(tipoTarifario)){
				tipoTarifario=ConstantesBD.codigoTarifarioCups+"";
			}
			
			for (DTOAdministracionAutorizacion dtoAdministracionAutorizacion : listaAutorizaciones) 
			{
				if(!listaAutorizacionesAgregadas.contains(dtoAdministracionAutorizacion.getCodigoPk())){
					if(!dtoAdministracionAutorizacion.getTipoAutorizacion().equals(ConstantesIntegridadDominio.TIPO_AUTORIZACION_INGRESO_ESTANCIA)){
						List<DtoArticulosAutorizaciones>autoArticulos=consultarArticulos(dtoAdministracionAutorizacion.getCodigoPkAutoEntSub(), usuario.getCodigoInstitucionInt());
						
						List<DtoServiciosAutorizaciones>autoServicios=consultarServicios(dtoAdministracionAutorizacion.getCodigoPkAutoEntSub(), Long.parseLong(tipoTarifario));
						
						dtoAdministracionAutorizacion.setListaArticulosAuto(autoArticulos);
						dtoAdministracionAutorizacion.setListaServiciosAuto(autoServicios);
					}
					
					listaAutorizacionesNoRepetidas.add(dtoAdministracionAutorizacion);
					listaAutorizacionesAgregadas.add(dtoAdministracionAutorizacion.getCodigoPk());
				}
			}
			forma.setListaAutorizaciones(listaAutorizacionesNoRepetidas);
			SortGenerico sortG=new SortGenerico("ConsecutivoAutorizacion",true);
			Collections.sort(forma.getListaAutorizaciones(),sortG);
			forma.setSinAutorizaciones(false);
		}		
						
		return mapping.findForward("listadoOrdenesAutorizadas");	
		
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
	public ActionForward accionOrdenar(AdministracionAutorizacionCapitacionPacienteForm forma, 
			UsuarioBasico usuario, ActionMapping mapping){
		
		boolean ordenamiento = false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
				
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaAutorizaciones(),sortG);

		return mapping.findForward("listadoOrdenesAutorizadas");
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
