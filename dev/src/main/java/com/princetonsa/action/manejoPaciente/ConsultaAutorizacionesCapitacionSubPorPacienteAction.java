package com.princetonsa.action.manejoPaciente;


import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.Utilidades;
import util.capitacion.ConstantesCapitacion;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.manejoPaciente.ConsultaAutorizacionesCapitacionSubPorPacienteForm;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.manejoPaciente.DTOAdministracionAutorizacion;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
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
 * @author 
 * @since 
 */
public class ConsultaAutorizacionesCapitacionSubPorPacienteAction extends Action {
	
	
	private static final String INDICATIVO_TEMPORAL = " (Temporal)";
	private static final String ESTADO_AUTORIZADA="Autorizada";
	private static final String ESTADO_ANULADA="Anulada";
	
	/**
	 * 
	 * Este Método se encarga de ejecutar las acciones sobre las páginas
	 * de administración de las autorizaciones de capitación por paciente
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
		if (form instanceof ConsultaAutorizacionesCapitacionSubPorPacienteForm) {
			
			ConsultaAutorizacionesCapitacionSubPorPacienteForm forma = (ConsultaAutorizacionesCapitacionSubPorPacienteForm)form;
			String estado = forma.getEstado();
			Log4JManager.info("estado " + estado);
			UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request
					.getSession());
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			
			UtilidadTransaccion.getTransaccion().begin();
			
			try{
				if(estado.equals("consultarPorPaciente")){
					forward= empezar(request,forma, mapping,paciente,usuario);
									
				}else if(estado.equals("ordenar")){
					
					forward= accionOrdenar(forma,usuario,mapping);
				}
				else if(estado.equals("volver")){
					
					forward= accionVolver(forma, response, request, mapping);
				}
				UtilidadTransaccion.getTransaccion().commit();				
			}catch (Exception e) {
				Log4JManager.error("Error en la consulta de autorizaciones para paciente cargado en sesión", e);
				UtilidadTransaccion.getTransaccion().rollback();
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
			ConsultaAutorizacionesCapitacionSubPorPacienteForm forma,
			ActionMapping mapping,PersonaBasica paciente,UsuarioBasico usuario ){
		
		ArrayList<DTOAdministracionAutorizacion> listaAutorizaciones = null;
		
		DTOAdministracionAutorizacion dtoAdministracion = new DTOAdministracionAutorizacion();
		
		/**
		 * MT 1727: No validar vigencia cargue al consultar autorizaciones
		 * Diana Ruiz
		 */
		if(forma.getEstado().equals("consultarPorPaciente")){
			dtoAdministracion.setEstado(forma.getEstado());
		}		
		
		forma.reset();
		
		if(paciente == null || paciente.getCodigoPersona()<=0){
			Connection con = HibernateUtil.obtenerConexion();			
			Logger logger = Logger.getLogger(AdministracionAutorizacionCapitacionPacienteAction.class);
			
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, 
					"Paciente no cargado", "errors.paciente.noCargado", true);			
			
		}else{
			IAutorizacionIngresoEstanciaServicio autorizacionIngresoServicio= 
				ManejoPacienteServicioFabrica.crearAutorizacionIngresoEstanciaServicio();	
			IAutorizacionCapitacionSubServicio autorizacionCapitacionServicio= 
				ManejoPacienteServicioFabrica.crearAutorizacionCapitacionSubServicio();		
			
			DtoPaciente dtoPaciente = new DtoPaciente();			

			
			dtoPaciente.setCodigo(paciente.getCodigoPersona());
			dtoAdministracion.setPaciente(dtoPaciente);		
			
		
			
			ArrayList<DTOAdministracionAutorizacion> listaAutorizacionesIngEstancia =  
				autorizacionIngresoServicio.obtenerAutorizacionesPorPaciente(dtoAdministracion);									
			
			
			if(!Utilidades.isEmpty(listaAutorizacionesIngEstancia))
			{				
				ArrayList<DTOAdministracionAutorizacion> listaFinalAutorizacionesIngEstancia =
					new ArrayList<DTOAdministracionAutorizacion>();
				
				for(DTOAdministracionAutorizacion autorizacion :listaAutorizacionesIngEstancia)
				{					
					// se omiten las autorizaciones que de ingreso estancia que tienen
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
			
		
			ArrayList<DTOAdministracionAutorizacion> listaAutorizacionesCapitacion = 
					autorizacionCapitacionServicio.obtenerAutorizacionesPorPaciente(dtoAdministracion);								
			
			
			if(!Utilidades.isEmpty(listaAutorizacionesCapitacion))
			{			
				ArrayList<DTOAdministracionAutorizacion> listaFinalAutorizacionesCapitacion = 
					new ArrayList<DTOAdministracionAutorizacion>(); 
				
				for(DTOAdministracionAutorizacion autorizacion :listaAutorizacionesCapitacion)
				{						
											
						if(autorizacion.getEstado().equals(ConstantesIntegridadDominio.acronimoAutorizado))
						{
							autorizacion.setEstado("Autorizada");
						}
						else if (autorizacion.getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
						{
							autorizacion.setEstado("Anulada");
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
			forma.setSinAutorizaciones(false);
		}		
						
		return mapping.findForward("listadoPorPaciente");	
		
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
	public ActionForward accionOrdenar(ConsultaAutorizacionesCapitacionSubPorPacienteForm forma, 
			UsuarioBasico usuario, ActionMapping mapping){
		
		boolean ordenamiento = false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
				
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaAutorizaciones(),sortG);

		return mapping.findForward("listadoPorPaciente");
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
	private ActionForward accionVolver(ConsultaAutorizacionesCapitacionSubPorPacienteForm forma, HttpServletResponse response, HttpServletRequest request, ActionMapping mapping)
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
