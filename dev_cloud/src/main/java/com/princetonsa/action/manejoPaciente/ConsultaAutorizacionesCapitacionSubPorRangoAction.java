package com.princetonsa.action.manejoPaciente;

import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.ConstantesBDManejoPaciente;

import com.princetonsa.actionform.manejoPaciente.ConsultaAutorizacionesCapitacionSubPorRangoForm;
import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
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
 * @author 
 * @since 
 */
public class ConsultaAutorizacionesCapitacionSubPorRangoAction extends Action {
	
	
	private static final String INDICATIVO_TEMPORAL = " (Temporal)";
	private static final String ESTADO_AUTORIZADA="Autorizada";
	private static final String ESTADO_ANULADA ="Anulada";
	
	private MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.manejoPaciente.ConsultaAutorizacionesCapitacionSubPorRangoForm");
	
	/**
	 * 
	 * Este Método se encarga de ejecutar las acciones sobre las páginas
	 * de administración de las autorizaciones de capitación por rango
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
		
		ActionForward forward=null;
		if (form instanceof ConsultaAutorizacionesCapitacionSubPorRangoForm) {
			
			ConsultaAutorizacionesCapitacionSubPorRangoForm forma = (ConsultaAutorizacionesCapitacionSubPorRangoForm)form;
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());			
			
			UtilidadTransaccion.getTransaccion().begin();
			
			try{
				if(estado.equals("consultarPorRango")){
					forma.reset();
					forward= empezar(request,forma,mapping,usuario );				
					
				}else if(estado.equals("consultar")){
					
					ActionMessages errores = new ActionMessages();
					validacionesBuscarPorRango(forma, errores, request);					
					
					if(errores.isEmpty()){
						forward= consultarAutorizaciones(request,forma, mapping,usuario);
					}else{
						forward= mapping.findForward("principal");	
					}
					
				}else if(estado.equals("ordenar")){
					
					forward= accionOrdenar(forma,usuario,mapping);					
				}else if(estado.equals("cambiarTipoConsecutivo")){
					forward= mapping.findForward("principal");	
				}
				/*else if(estado.equals("imprimir"))
				{
					
				}*/
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
	 * @author, 
	 *
	 */
	public ActionForward empezar(HttpServletRequest request,
			ConsultaAutorizacionesCapitacionSubPorRangoForm forma,
			ActionMapping mapping,UsuarioBasico usuario ){
		
		IEntidadesSubcontratadasServicio entidadServicio = 
			FacturacionServicioFabrica.crearEntidadesSubcontratadasServicio();			
		ArrayList<DtoEntidadSubcontratada> listaEntidadesSub  =			
			entidadServicio.listarEntidadesSubXCentroCostoActivo(ConstantesBD.codigoNuncaValido);
		
		forma.reset();
		
		ArrayList<DtoCheckBox> listaOpcion=new ArrayList<DtoCheckBox>();
		DtoCheckBox opcionLista  = null;
		
		opcionLista  = new DtoCheckBox();
		opcionLista.setCodigo(ConstantesIntegridadDominio.acronimoAutorizado);
		opcionLista.setNombre(ValoresPorDefecto.getIntegridadDominio(opcionLista.getCodigo())+"");
		listaOpcion.add(opcionLista);
		
		opcionLista  = new DtoCheckBox();
		opcionLista.setCodigo(ConstantesIntegridadDominio.acronimoEstadoAnulado); // anulado
		opcionLista.setNombre(ValoresPorDefecto.getIntegridadDominio(opcionLista.getCodigo())+"");
		listaOpcion.add(opcionLista);
		
		opcionLista  = new DtoCheckBox();
		opcionLista.setCodigo(ConstantesIntegridadDominio.acronimoTemporal); // temporal
		opcionLista.setNombre(ValoresPorDefecto.getIntegridadDominio(opcionLista.getCodigo())+"");
		listaOpcion.add(opcionLista);
		
		
		opcionLista  = new DtoCheckBox();
		opcionLista.setCodigo("");
		opcionLista.setNombre("Todas");
		listaOpcion.add(opcionLista);
		
		forma.setListaOpciones(listaOpcion);
		
		obtenerTiposAutorizacion(forma);
		obtenerTiposConsecutivosAutorizacion(forma);
		
		if((!Utilidades.isEmpty(listaEntidadesSub))){
			forma.setListaEntidadesSub(listaEntidadesSub);
		}
		
		
		return mapping.findForward("principal");	
	}
	
	
	


	/**
	 * 
	 * Este Método se encarga de consultar las autorizaciones de capitación o de 
	 * ingreso estancia del paciente cargado en sesión
	 * 
	 * @param AdministracionAutorizacionCapitacionPacienteForm forma, ActionMapping mapping
	 * @return ActionForward
	 * @author, 
	 *
	 */
	public ActionForward consultarAutorizaciones(HttpServletRequest request,
			ConsultaAutorizacionesCapitacionSubPorRangoForm forma,
			ActionMapping mapping,UsuarioBasico usuario ){
		
		ArrayList<DTOAdministracionAutorizacion> listaAutorizaciones = null;
		
		//Se setea este campo para que no afecte consulta por funcionalidad de Administrar Autorizaciones por Rango ya que debe consultar las autorizadas y temporales
		//y por esta funcionalidad de Consultar Autorizaciones por Rango se puede filtrar por el estado de la autorizacion.
		forma.getDtoFiltros().setEsConsulta(true);
		
		IAutorizacionIngresoEstanciaServicio autorizacionIngresoServicio= 
			ManejoPacienteServicioFabrica.crearAutorizacionIngresoEstanciaServicio();	
		IAutorizacionCapitacionSubServicio autorizacionCapitacionServicio= 
			ManejoPacienteServicioFabrica.crearAutorizacionCapitacionSubServicio();		
		
		
		ArrayList<DTOAdministracionAutorizacion> listaAutorizacionesIngEstancia = new ArrayList<DTOAdministracionAutorizacion>();
		
		String tipo=forma.getDtoFiltros().getCodigoTipoAutorizacion();
		if((tipo == null || tipo.trim().isEmpty() || tipo.equals(ConstantesBDManejoPaciente.codigoTipoAutorizacionIngresoEstancia))		
				//Debe ser consultable sino se selecciona un tipo de consecutivo 
				//o si se selecciona tipo de consecutivo de autorizacion de capitacion ya que los ingresos por estancia no tienen autorizacion de entidades subcontratadas  
					&&(forma.getDtoFiltros().getTipoConsecutivoAutorizacion().equals(""+ConstantesBD.codigoNuncaValido)
					||forma.getDtoFiltros().getTipoConsecutivoAutorizacion().equals(""+ConstantesBDManejoPaciente.codigoTipoConsecutivoAutorizacionCapitacion))){
			listaAutorizacionesIngEstancia = autorizacionIngresoServicio.obtenerAutorizacionesPorRango(forma.getDtoFiltros());
		}
								
		
		
		if(!Utilidades.isEmpty(listaAutorizacionesIngEstancia)){
			
			ArrayList<DTOAdministracionAutorizacion> listaFinalAutorizacionesIngEstancia =
				new ArrayList<DTOAdministracionAutorizacion>();
			
			
			for(DTOAdministracionAutorizacion autorizacion :listaAutorizacionesIngEstancia)
			{	
				// se omiten las autorizaciones de ingreso estancia que tienen
				//asociada una autorización de capitación
				if(autorizacion.getIdIngresoEstanciaCapitacion() == null){
					if(autorizacion.getEstado().equals(ConstantesIntegridadDominio.acronimoAutorizado))
					{
						autorizacion.setEstado(ESTADO_AUTORIZADA);
					}
					else if (autorizacion.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
					{
						autorizacion.setEstado(ESTADO_ANULADA);
					}
					if(autorizacion.getIndicativoTemporal().equals(ConstantesBD.acronimoSi)){
						
						if(UtilidadTexto.isEmpty(autorizacion.getDescripcionEntidadSubIngEst())){
							autorizacion.getEntidadSubcontratada().setRazonSocial(autorizacion.getEntidadSubcontratada().getRazonSocial()+INDICATIVO_TEMPORAL);
						}else{
							autorizacion.getEntidadSubcontratada().setRazonSocial(autorizacion.getDescripcionEntidadSubIngEst()+INDICATIVO_TEMPORAL);
						}
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

		ArrayList<DTOAdministracionAutorizacion> listaAutorizacionesCapitacion = new ArrayList<DTOAdministracionAutorizacion>();
		
		if(tipo == null || tipo.trim().isEmpty() || tipo.equals(ConstantesBDManejoPaciente.codigoTipoAutorizacionServicio)
				|| tipo.equals(ConstantesBDManejoPaciente.codigoTipoAutorizacionMedIns)
				|| tipo.equals(ConstantesBDManejoPaciente.codigoTipoAutorizacionServicioIngre)
				|| tipo.equals(ConstantesBDManejoPaciente.codigoTipoAutorizacionMedInsIngre)){
		
			listaAutorizacionesCapitacion =	autorizacionCapitacionServicio.obtenerAutorizacionesPorRango(forma.getDtoFiltros());
		}
		
		if(!Utilidades.isEmpty(listaAutorizacionesCapitacion))
		{
		
			ArrayList<DTOAdministracionAutorizacion> listaFinalAutorizacionesCapitacion = 
				new ArrayList<DTOAdministracionAutorizacion>(); 
			
			for(DTOAdministracionAutorizacion autorizacion :listaAutorizacionesCapitacion)
			{										
				if(autorizacion.getEstado().equals(ConstantesIntegridadDominio.acronimoAutorizado))
				{
					autorizacion.setEstado(ESTADO_AUTORIZADA);
				}
				else if (autorizacion.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
				{
					autorizacion.setEstado(ESTADO_ANULADA);
				}
				if(autorizacion.getIndicativoTemporal().equals(ConstantesBD.acronimoSi)){
					
					if(UtilidadTexto.isEmpty(autorizacion.getDescripcionEntidadSubIngEst())){
						autorizacion.getEntidadSubcontratada().setRazonSocial(autorizacion.getEntidadSubcontratada().getRazonSocial()+INDICATIVO_TEMPORAL);
					}else{
						autorizacion.getEntidadSubcontratada().setRazonSocial(autorizacion.getDescripcionEntidadSubIngEst()+INDICATIVO_TEMPORAL);
					}
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
				if(tipo != null && !tipo.equals("") && (tipo.equals(ConstantesBDManejoPaciente.codigoTipoAutorizacionServicio) 
							|| tipo.equals(ConstantesBDManejoPaciente.codigoTipoAutorizacionMedIns))){
					// se valida para garantizar que NO este asociado a una Autorización de Ingreso Estancia
					if(autorizacion.getIdIngresoEstanciaCapitacion() == null){
						listaFinalAutorizacionesCapitacion.add(autorizacion);
					}
				}
				else{
					listaFinalAutorizacionesCapitacion.add(autorizacion);
				}
							
			}		
			
			if(!Utilidades.isEmpty(listaFinalAutorizacionesCapitacion)){
			
				if(listaAutorizaciones==null){
					listaAutorizaciones = new ArrayList<DTOAdministracionAutorizacion>();
				}
				listaAutorizaciones.addAll(listaFinalAutorizacionesCapitacion);		
			}		
		}			
		
		
		
		
		if(!Utilidades.isEmpty(listaAutorizaciones))
		{
			
			//----------------------------------------------------------------------------------------------
			// Se filtran para no repetir autorizaciones
			/*ArrayList<Long> listaAutorizacionesAgregadas = new ArrayList<Long>();
			ArrayList<DTOAdministracionAutorizacion> listaAutorizacionesNoRepetidas = new ArrayList<DTOAdministracionAutorizacion>();
			
			for (DTOAdministracionAutorizacion dtoAdministracionAutorizacion : listaAutorizaciones) 
			{
				if(!listaAutorizacionesAgregadas.contains(dtoAdministracionAutorizacion.getCodigoPk())){
					listaAutorizacionesNoRepetidas.add(dtoAdministracionAutorizacion);
					listaAutorizacionesAgregadas.add(dtoAdministracionAutorizacion.getCodigoPk());
				}
			}*/
			forma.setListaAutorizaciones(listaAutorizaciones);
			SortGenerico sortG=new SortGenerico("ConsecutivoAutorizacion",true);
			Collections.sort(forma.getListaAutorizaciones(),sortG);
			//----------------------------------------------------------------------------------------------			
			
			
			//forma.setListaAutorizaciones(listaAutorizaciones);
			forma.setSinAutorizaciones(false);
		}				
		return mapping.findForward("listadoPorRango");			
	}
	
	
	/**
	 * Validaciones varias del boton buscar y de campos requeridos
	 * @param forma
	 * @param errores
	 * @param request
	 * @return ActionErrors
	 */
	private void validacionesBuscarPorRango(ConsultaAutorizacionesCapitacionSubPorRangoForm forma, ActionMessages errores, HttpServletRequest request) 
	{
			
		String mensajeConcreto = null;
		if(forma.getDtoFiltros().getFechaIncioBusqueda() == null)
		{
			mensajeConcreto = fuenteMensaje.getMessage("ConsultaAutorizacionesCapitacionSubPorRangoForm.fechaRequerida", "Inicial");
			retornarErrorEnviado(request, mensajeConcreto, errores);
		}
		if(forma.getDtoFiltros().getFechaFinBusqueda() == null)
		{
			mensajeConcreto = fuenteMensaje.getMessage("ConsultaAutorizacionesCapitacionSubPorRangoForm.fechaRequerida", "Final");
			retornarErrorEnviado(request, mensajeConcreto, errores);
		}
		
		if(errores.isEmpty())
		{
			// Fecha Inicial < Fecha Sistema
			if(forma.getDtoFiltros().getFechaIncioBusqueda().after(UtilidadFecha.getFechaActualTipoBD()))
			{
				mensajeConcreto = fuenteMensaje.getMessage("ConsultaAutorizacionesCapitacionSubPorRangoForm.fechaInicialMayorActual", "Inicial");
				retornarErrorEnviado(request, mensajeConcreto, errores);
			}
			
			// Fecha Inicial < Fecha Fin
			if(forma.getDtoFiltros().getFechaIncioBusqueda().after(forma.getDtoFiltros().getFechaFinBusqueda()))
			{
				mensajeConcreto = fuenteMensaje.getMessage("ConsultaAutorizacionesCapitacionSubPorRangoForm.fechaInicialMayorFinal");
				retornarErrorEnviado(request, mensajeConcreto, errores);
			}
			
			// Fecha Final < Fecha Sistema
			if(forma.getDtoFiltros().getFechaFinBusqueda().after(UtilidadFecha.getFechaActualTipoBD()))
			{
				mensajeConcreto = fuenteMensaje.getMessage("ConsultaAutorizacionesCapitacionSubPorRangoForm.fechaInicialMayorActual", "Final");
				retornarErrorEnviado(request, mensajeConcreto, errores);
			}
			
			
			/**
			 * MT 
			 */
			forma.getDtoFiltros().setConsecutivoIncioAutorizacion(forma.getConsInAutorizacion());
			forma.getDtoFiltros().setConsecutivoFinAutorizacion(forma.getConsFinAutorizacion());
			
			
			if(forma.getDtoFiltros().getConsecutivoIncioAutorizacion() != null
					&& forma.getDtoFiltros().getConsecutivoFinAutorizacion() != null
					&& forma.getDtoFiltros().getConsecutivoFinAutorizacion().longValue() < forma.getDtoFiltros().getConsecutivoIncioAutorizacion().longValue()){
				mensajeConcreto = fuenteMensaje.getMessage("ConsultaAutorizacionesCapitacionSubPorRangoForm.autorizacionFinalMenosInicial");
				retornarErrorEnviado(request, mensajeConcreto, errores);
			}
			
		}
		
	}
	
	/**
	 * Muestra el mensaje de error enviado
	 * @param forma
	 * @param request
	 * @param errores
	 * @autor 
	 */
	private void retornarErrorEnviado(HttpServletRequest request, String mensajeConcreto, ActionMessages errores) 
	{
		errores.add("error_concreto_enviado", new ActionMessage("errors.notEspecific", mensajeConcreto));
		saveErrors(request, errores);
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
	public ActionForward accionOrdenar(ConsultaAutorizacionesCapitacionSubPorRangoForm forma, 
			UsuarioBasico usuario, ActionMapping mapping){
		
		boolean ordenamiento = false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
				
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaAutorizaciones(),sortG);
		
		return mapping.findForward("listadoPorRango");
	}
	
    public void obtenerTiposAutorizacion(ConsultaAutorizacionesCapitacionSubPorRangoForm forma){
    	final String TIPO_AUTORIZACION_PROPERTY="ConsultaAutorizacionesCapitacionSubPorRangoForm.tipoAutorizacion";
    	
    	ArrayList<DtoCheckBox> tiposAutorizacion = new ArrayList<DtoCheckBox>();
    	DtoCheckBox tipo = new DtoCheckBox();
    	tipo.setCodigo(ConstantesBDManejoPaciente.codigoTipoAutorizacionServicio);
    	tipo.setNombre(fuenteMensaje.getMessage(
    							TIPO_AUTORIZACION_PROPERTY+
    							ConstantesBDManejoPaciente.codigoTipoAutorizacionServicio));
    	tiposAutorizacion.add(tipo);
    	tipo= new DtoCheckBox();
    	tipo.setCodigo(ConstantesBDManejoPaciente.codigoTipoAutorizacionMedIns);
    	tipo.setNombre(fuenteMensaje.getMessage(
    							TIPO_AUTORIZACION_PROPERTY+
    							ConstantesBDManejoPaciente.codigoTipoAutorizacionMedIns));
    	tiposAutorizacion.add(tipo);
    	tipo= new DtoCheckBox();
    	tipo.setCodigo(ConstantesBDManejoPaciente.codigoTipoAutorizacionServicioIngre);
    	tipo.setNombre(fuenteMensaje.getMessage(
    							TIPO_AUTORIZACION_PROPERTY+
    							ConstantesBDManejoPaciente.codigoTipoAutorizacionServicioIngre));
    	tiposAutorizacion.add(tipo);
    	tipo= new DtoCheckBox();
    	tipo.setCodigo(ConstantesBDManejoPaciente.codigoTipoAutorizacionMedInsIngre);
    	tipo.setNombre(fuenteMensaje.getMessage(
    							TIPO_AUTORIZACION_PROPERTY+
    							ConstantesBDManejoPaciente.codigoTipoAutorizacionMedInsIngre));
    	tiposAutorizacion.add(tipo);
    	tipo= new DtoCheckBox();
    	tipo.setCodigo(ConstantesBDManejoPaciente.codigoTipoAutorizacionIngresoEstancia);
    	tipo.setNombre(fuenteMensaje.getMessage(
    							TIPO_AUTORIZACION_PROPERTY+
    							ConstantesBDManejoPaciente.codigoTipoAutorizacionIngresoEstancia));
    	tiposAutorizacion.add(tipo);
    	forma.setListaTiposAutorizacion(tiposAutorizacion);
    }
    public void obtenerTiposConsecutivosAutorizacion(ConsultaAutorizacionesCapitacionSubPorRangoForm forma){
    	ArrayList<DtoCheckBox> tiposConsecutivoAutorizacion = new ArrayList<DtoCheckBox>();
    	DtoCheckBox tipo = new DtoCheckBox();
    	tipo.setCodigo(String.valueOf(ConstantesBDManejoPaciente.codigoTipoConsecutivoAutorizacionCapitacion));
    	tipo.setNombre(fuenteMensaje.getMessage(
    							"ConsultaAutorizacionesCapitacionSubPorRangoForm.tipoConsecutivoAutorizacion"+
    							ConstantesBDManejoPaciente.codigoTipoConsecutivoAutorizacionCapitacion));
    	tiposConsecutivoAutorizacion.add(tipo);
    	
    	tipo= new DtoCheckBox();
    	tipo.setCodigo(String.valueOf(ConstantesBDManejoPaciente.codigoTipoConsecutivoAutorizacionEntidadSub));
    	tipo.setNombre(fuenteMensaje.getMessage(
    							"ConsultaAutorizacionesCapitacionSubPorRangoForm.tipoConsecutivoAutorizacion"+
    							ConstantesBDManejoPaciente.codigoTipoConsecutivoAutorizacionEntidadSub));
    	tiposConsecutivoAutorizacion.add(tipo);
    	
    	forma.setListaTiposConsecutivoAutorizacion(tiposConsecutivoAutorizacion);
    }
    
}
