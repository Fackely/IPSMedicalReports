package com.princetonsa.action.capitacion;

import java.io.PrintWriter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.capitacion.CierrePresupuestoForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.capitacion.DtoLogCierrePresuCapita;
import com.princetonsa.dto.capitacion.DtoProcesoPresupuestoCapitado;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.odontologia.facturacion.convenio.ConveniosFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IUsuariosMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ICierrePresupuestoCapitacionoMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IProcesoCierrePresupuestoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IContratoMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IConveniosMundo;
import com.servinte.axioma.orm.Contratos;


/**
 * @author Cristhian Murillo
 * Clase usada para controlar los procesos de la funcionalidad.
 */
public class CierrePresupuestoAction extends Action 
{
	/** * Log */
	Logger logger = Logger.getLogger(CierrePresupuestoAction.class);
	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.capitacion.CierrePresupuestoForm");
	
	
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception
	{
		
		if(form instanceof CierrePresupuestoForm)
		{
			CierrePresupuestoForm forma = (CierrePresupuestoForm)form;
			String estado = forma.getEstado(); 
			
			Log4JManager.info("Estado: CierrePresupuestoAction --> "+estado);
			
			UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			if(estado.equals("empezar"))
			{
				forma.reset();
				ActionForward actionForward = new ActionForward();
				actionForward = validarEmpezar(mapping, request, usuario);
				
				if(actionForward != null){
					return actionForward;
				}
				
				return accionEmpezar(mapping, usuario, forma);
			}
			
			else if(estado.equals("mostrarContratosConvenio"))
			{
				return accionMostrarContratosConvenio(forma, mapping); 
			}
			
			else if(estado.equals("procesar"))
			{
				return accionProcesar(forma, usuario, mapping, request); 
			}
			
			if(estado.equals("empezarlog"))
			{
				forma.reset();
				return accionEmpezarLog(mapping, usuario, forma);
			}
			
			else if(estado.equals("buscar"))
			{
				
				return accionBuscar(forma, usuario, mapping, request); 
			}
			
			else if(estado.equals("detalleFechaCierre"))
			{
				
				return accionDetalleFechaCierre(forma, usuario, mapping, request); 
			}
			
			else if(estado.equals("detalleContratoCierre"))
			{
				
				return accionDetalleContratoCierre(forma, usuario, mapping, request); 
			}
			
			else if(estado.equals("ordenar"))
			{
				
				return accionOrdenar(mapping, forma);
			}
			
			else if(estado.equals("ordenarContrato"))
			{
				return accionOrdenar(mapping, forma);
			}
			
			else if(estado.equals("cargarIndicativoNInfo")) {
			
				PrintWriter out = response.getWriter();
				String fechaCierre = UtilidadFecha.conversionFormatoFechaAAp(request.getParameter("fechaCierre"));
				String fechaGeneracion = UtilidadFecha.conversionFormatoFechaAAp(request.getParameter("fechaGeneracion"));
				String horaGeneracion = request.getParameter("horaGeneracion");
				out.print(this.obtenerInfoIndicativoCierre(forma, fechaCierre, fechaGeneracion, horaGeneracion));
				
			}
			
			else if(estado.equals("asignarPropiedad")){
				/*  * Se asigna una propiedad a la forma sin cambiar nada en la presentación  */
				return null;
			}
		}
		return null;
	}
	
	
	public String obtenerInfoIndicativoCierre(CierrePresupuestoForm forma, 
			String fechaCierre, String fechaGeneracion, String horaGeneracion) {

		String contenidoDiv = "";
		
		String procesos="";
		
		for(DtoLogCierrePresuCapita dto:forma.getListaLogsAux())
		{
			if(UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaCierre()).equals(fechaCierre)
					&&UtilidadFecha.conversionFormatoFechaAAp(dto.getFechaGeneracion()).equals(fechaGeneracion)
					&&dto.getHoraGeneracion().equals(horaGeneracion))
			{
				if(dto.getNoInformacion())
				{
					procesos = procesos + dto.getTipoProceso() +ConstantesBD.separadorSplit;
				}
			}
		}
		
		if(procesos.equals(""))
		{
			return "";
		}
		
		String[] listadoProcesos = procesos.split(ConstantesBD.separadorSplit);
		
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<DtoIntegridadDominio> listadoPro=Utilidades.generarListadoConstantesIntegridadDominio(
				con, listadoProcesos, false);
		
		UtilidadBD.closeConnection(con);
		
		procesos="";
		
		for(DtoIntegridadDominio dtoInte:listadoPro)
		{
			procesos= procesos + dtoInte.getDescripcion() + "<br>";
		}
		
		try {
			contenidoDiv = 	"<div style=\"width: 200px;\"><table width='100%' border='0' cellpadding='4' cellspacing='1' bgcolor='#006898'>" + 
							"<tr><td class=Subtitulo>" +
							"No se generó información en el Cierre por que no existe información para los siguientes procesos: "+
							"</td></tr>";
			
			contenidoDiv += "<tr bgcolor='#FFFFFF'><td>" +
								procesos +
							"</td></tr>";

			contenidoDiv += "</table></div>";		

		} catch (Exception e) {
			e.printStackTrace();
		}
		return contenidoDiv;
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 *
	 * @autor Cristhian Murillo
	 *
	 */
	private ActionForward accionDetalleContratoCierre(CierrePresupuestoForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		try{
			ICierrePresupuestoCapitacionoMundo cierrePresupuestoCapitacionoMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
			HibernateUtil.beginTransaction();
			DtoProcesoPresupuestoCapitado parametros = new DtoProcesoPresupuestoCapitado();

			parametros.setFechaInicio(forma.getFechaInicio());
			parametros.setFechaFin(forma.getFechaFin());
			parametros.setInstitucion(usuario.getCodigoInstitucionInt());
			parametros.setLoginUsuario(forma.getUsuarioSeleccionado());
			parametros.setCaseParaBusquedaLog(2);
			forma.setLoggySeleccionado( forma.getListaLogsAux().get(forma.getPosArrayAnterior()));
			parametros.setDtoLogCierrePresuCapita(forma.getLoggySeleccionado());

			forma.setListaLogsAux(cierrePresupuestoCapitacionoMundo.obtenerLogs(parametros));

			HibernateUtil.endTransaction();
		}catch (Exception e) {
			HibernateUtil.abortTransaction();
			logger.error("error transaccion Hibernate",e);
		}

		return mapping.findForward("detalleContrato"); 

	}


	/**
	 * accionDetalleFechaCierre
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return ActionForward
	 *
	 * @autor Cristhian Murillo
	 */
	private ActionForward accionDetalleFechaCierre(CierrePresupuestoForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		try{
			HibernateUtil.beginTransaction();
			ICierrePresupuestoCapitacionoMundo cierrePresupuestoCapitacionoMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();
			DtoProcesoPresupuestoCapitado parametros = new DtoProcesoPresupuestoCapitado();

			parametros.setFechaInicio(forma.getFechaInicio());
			parametros.setFechaFin(forma.getFechaFin());
			parametros.setInstitucion(usuario.getCodigoInstitucionInt());
			parametros.setLoginUsuario(forma.getUsuarioSeleccionado());
			parametros.setCaseParaBusquedaLog(1);

			forma.setLoggySeleccionado( forma.getListaLogs().get(forma.getPosArray()));
			parametros.setDtoLogCierrePresuCapita(forma.getLoggySeleccionado());

			forma.setListaLogsAux(cierrePresupuestoCapitacionoMundo.obtenerLogs(parametros));

			HibernateUtil.endTransaction();
		}catch (Exception e) {
			HibernateUtil.abortTransaction();
			logger.error("error transaccion Hibernate ",e);
		}

		return mapping.findForward("detalleConvenio"); 
	}


	/**
	 * accionAsignarFechaFinal
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return ActionForward
	 *
	 * @autor Cristhian Murillo
	 */
	private ActionForward accionProcesar(CierrePresupuestoForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores	= new ActionErrors();
		errores = validarCamposRequeridosAutorizar(forma, request);
		IProcesoCierrePresupuestoMundo procesoCierrePresupuestoMundo = CapitacionFabricaMundo.crearProcesoCierrePresupuestoMundo();
		
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		
		else
		{
			DtoProcesoPresupuestoCapitado presupuestoCapitado = new DtoProcesoPresupuestoCapitado();
			
			presupuestoCapitado.setFechaInicio(forma.getFechaInicio());
			presupuestoCapitado.setFechaFin(forma.getFechaFin());
			presupuestoCapitado.setObservaciones(forma.getObservaciones());
			presupuestoCapitado.setInstitucion(usuario.getCodigoInstitucionInt());
			presupuestoCapitado.setLoginUsuario(usuario.getLoginUsuario());
			
			if(!UtilidadTexto.isEmpty(forma.getCodigoConvenio())){
				presupuestoCapitado.setConvenio(Integer.parseInt(forma.getCodigoConvenio()));
			}
			
			if(!UtilidadTexto.isEmpty(forma.getCodigoContrato())){
				presupuestoCapitado.setContrato(Integer.parseInt(forma.getCodigoContrato()));
			}
			
			procesoCierrePresupuestoMundo.generarCierrePresupuesto(presupuestoCapitado);
			
			if(presupuestoCapitado.getErrores().isEmpty()){
				forma.setEstado("resumen");
			}
			else{
				saveErrors(request, presupuestoCapitado.getErrores());
			}
		}
		
		return mapping.findForward("principal");
	}

	
	/**
	 * Valida los campos requeridos para la funcionalidad en el momento de procesar
	 * @param forma
	 * @param request
	 * @return ActionErrors
	 * 
	 * @author Cristhian Murillo
	 */
	private ActionErrors validarCamposRequeridosAutorizar(CierrePresupuestoForm forma, HttpServletRequest request)
	{
		ActionErrors errores	= new ActionErrors();
		String mensajeConcreto 	= "";
		
		validacionesGenerales(forma, request, errores);
		
		if(UtilidadTexto.isEmpty(forma.getObservaciones())){
			mensajeConcreto = fuenteMensaje.getMessage("CierrePresupuestoForm.campoRequerido", "Observaciónes");
			errores = retornarErrorEnviado(mensajeConcreto, errores);
		}
		else if(forma.getObservaciones().length() > 255)
		{
			mensajeConcreto = fuenteMensaje.getMessage("CierrePresupuestoForm.observacionesTamano");
			errores = retornarErrorEnviado(mensajeConcreto, errores);
		}
		
		return errores;
	}
	
	
	/**
	 * Valida los campos requeridos para la funcionalidad en el momento de buscar
	 * @param forma
	 * @param request
	 * @return ActionErrors
	 * 
	 * @author Cristhian Murillo
	 */
	private ActionErrors validarCamposRequeridosBuscar(CierrePresupuestoForm forma, HttpServletRequest request)
	{
		ActionErrors errores	= new ActionErrors();
		
		validacionesGeneralesBusqueda(forma, request, errores);
		
		return errores;
	}
	
	
	/**
	 * Método de ordenamiento generico
	 * @param mapping
	 * @param forma
	 * @return ActionForward
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,	CierrePresupuestoForm forma) 
	{
		boolean ordenamiento = false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaLogs(),sortG);

		if(forma.getEstado().equals("ordenarContrato")){
			return mapping.findForward("detalleContrato");
		}
		else {
			return mapping.findForward("principal");
		}
		
	}
	
	
	/**
	 * Validaciones generales para procesar y buscar
	 * @param forma
	 * @param request
	 * @param errores
	 *
	 * @autor Cristhian Murillo
	 */
	private void validacionesGenerales(CierrePresupuestoForm forma, HttpServletRequest request, ActionErrors errores)
	{
		String mensajeConcreto = null;
		Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
		
		if(UtilidadTexto.isEmpty(forma.getFechaInicio()+""))
		{
			mensajeConcreto = fuenteMensaje.getMessage("CierrePresupuestoForm.campoRequerido", "Fecha Inicio");
			errores = retornarErrorEnviado(mensajeConcreto, errores);
		}else if(forma.getFechaInicio().after(fechaActual)||forma.getFechaInicio().equals(fechaActual))
		{
			mensajeConcreto = fuenteMensaje.getMessage("CierrePresupuestoForm.fechaMayorActual", "Fecha Inicial");
			errores = retornarErrorEnviado(mensajeConcreto, errores);
		}
		
		if(UtilidadTexto.isEmpty(forma.getFechaFin()+""))
		{
			mensajeConcreto = fuenteMensaje.getMessage("CierrePresupuestoForm.campoRequerido", "Fecha Fin");
			errores = retornarErrorEnviado(mensajeConcreto, errores);
		}else
		{ 
			if(forma.getFechaFin().after(fechaActual)||forma.getFechaFin().equals(fechaActual))
			{
				mensajeConcreto = fuenteMensaje.getMessage("CierrePresupuestoForm.fechaMayorActual", "Fecha Final");
				errores = retornarErrorEnviado(mensajeConcreto, errores);
			}
			
			if(!UtilidadTexto.isEmpty(forma.getFechaInicio()+""))
			{
				if(forma.getFechaFin().before(forma.getFechaInicio()))
				{
					mensajeConcreto = fuenteMensaje.getMessage("CierrePresupuestoForm.fechaFinalMayorInicial");
					errores = retornarErrorEnviado(mensajeConcreto, errores);
				}
			}
		}
	}
	
	
	/**
	 * Validaciones generales para procesar y buscar
	 * @param forma
	 * @param request
	 * @param errores
	 *
	 * @autor Cristhian Murillo
	 */
	private void validacionesGeneralesBusqueda(CierrePresupuestoForm forma, HttpServletRequest request, ActionErrors errores)
	{
		String mensajeConcreto = null;
		Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
		
		if(UtilidadTexto.isEmpty(forma.getFechaInicio()+""))
		{
			mensajeConcreto = fuenteMensaje.getMessage("CierrePresupuestoForm.campoRequerido", "Fecha Inicio");
			errores = retornarErrorEnviado(mensajeConcreto, errores);
		}else if(forma.getFechaInicio().after(fechaActual))
		{
			mensajeConcreto = fuenteMensaje.getMessage("CierrePresupuestoForm.fechaMayorActual", "Fecha Inicial");
			errores = retornarErrorEnviado(mensajeConcreto, errores);
		}
		
		if(UtilidadTexto.isEmpty(forma.getFechaFin()+""))
		{
			mensajeConcreto = fuenteMensaje.getMessage("CierrePresupuestoForm.campoRequerido", "Fecha Fin");
			errores = retornarErrorEnviado(mensajeConcreto, errores);
		}else
		{ 
			if(forma.getFechaFin().after(fechaActual))
			{
				mensajeConcreto = fuenteMensaje.getMessage("CierrePresupuestoForm.fechaMayorActual", "Fecha Final");
				errores = retornarErrorEnviado(mensajeConcreto, errores);
			}
			
			if(!UtilidadTexto.isEmpty(forma.getFechaInicio()+""))
			{
				if(forma.getFechaFin().before(forma.getFechaInicio()))
				{
					mensajeConcreto = fuenteMensaje.getMessage("CierrePresupuestoForm.fechaFinalMayorInicial");
					errores = retornarErrorEnviado(mensajeConcreto, errores);
				}
			}
		}
	}
	
	
	/**
	 * Retorna el mensaje de error enviado
	 * @param mensajeConcreto
	 * @param errores
	 * 
	 * @autor Cristhian Murillo
	 */
	private ActionErrors retornarErrorEnviado(String mensajeConcreto, ActionErrors errores) 
	{
		errores.add("error_concreto_enviado", new ActionMessage("errors.notEspecific", mensajeConcreto));
		return errores;
	}
	
	
	
	/**
	 * accionMostrarContratosConvenio
	 * @param forma
	 * @param mapping
	 * @return ActionForward
	 *
	 * @autor Cristhian Murillo
	 */
	private ActionForward accionMostrarContratosConvenio(CierrePresupuestoForm forma, ActionMapping mapping) 
	{
		try{
			IContratoMundo contratoMundo	= FacturacionFabricaMundo.crearContratoMundo();;
			if(!UtilidadTexto.isEmpty(forma.getCodigoConvenio()))
			{
				HibernateUtil.beginTransaction();

				forma.setListaContratoConvenio(contratoMundo.listarContratosPorConvenio(Integer.parseInt(forma.getCodigoConvenio())));

				HibernateUtil.endTransaction();
			}
			else{
				forma.setCodigoContrato(null);
				forma.setListaContratoConvenio(new ArrayList<Contratos>());
			}
		}catch (Exception e) {
			HibernateUtil.abortTransaction();
			logger.error("error transaccion Hibernate ",e);
		}

		return mapping.findForward("principal");
	}


	/**
	 * accionEmpezar
	 * @param mapping
	 * @param usuario
	 * @param forma
	 * @return ActionForward
	 *
	 * @autor Cristhian Murillo
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, UsuarioBasico usuario, CierrePresupuestoForm forma) 
	{
		try{
			IConveniosMundo convenioMundo 	= ConveniosFabricaMundo.crearConveniosMundo();
			HibernateUtil.beginTransaction();
			forma.setListaConveniosInstitucion(convenioMundo.listarConveniosActivosPorInstitucion(usuario.getCodigoInstitucionInt()));
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			logger.error("error hibernate ",e);
		}

		return mapping.findForward("principal");
	}
	
	
	/**
	 * accionEmpezarLog
	 * @param mapping
	 * @param usuario
	 * @param forma
	 * @return ActionForward
	 *
	 * @autor Cristhian Murillo
	 */
	private ActionForward accionEmpezarLog(ActionMapping mapping, UsuarioBasico usuario, CierrePresupuestoForm forma) 
	{
		try{
			IUsuariosMundo usuariosMundo	= TesoreriaFabricaMundo.crearUsuariosMundo();
			HibernateUtil.beginTransaction();
			forma.setListaUsuarios((ArrayList<DtoUsuarioPersona>)usuariosMundo.obtenerUsuariosActivosDiferenteDe(usuario.getCodigoInstitucionInt(), null, false));
			HibernateUtil.endTransaction();
		}catch (Exception e) {
			HibernateUtil.abortTransaction();
			logger.error("error transaccion de hibernate ",e);
		}
		return mapping.findForward("principal");
	}

	
	
	/**
	 * Valida las precondiciones para empezar la funcionalidad
	 * @param mapping
	 * @param request
	 * @param usuario
	 * @return ActionForward
	 * 
	 * @author Cristhian Murillo
	 */
	private ActionForward validarEmpezar(ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
	{
		// Validación párametro general
		String fechaInicioCierreOrdenMedica = ValoresPorDefecto.getFechaInicioCierreOrdenMedica(usuario.getCodigoInstitucionInt());		
		if(UtilidadTexto.isEmpty(fechaInicioCierreOrdenMedica))
		{
			String mensajeConcreto = fuenteMensaje.getMessage("CierrePresupuestoForm.noParametroFechaInicioCierre");
			Connection con = null;
	    	con = UtilidadBD.abrirConexion();
	    	return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No Parametro Fecha Inicio Cierre", mensajeConcreto, false);
	    	
		}
		
		return null;
	}
	
	
	
	
	/**
	 * accionBuscar
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return ActionForward
	 *
	 * @autor Cristhian Murillo
	 */
	private ActionForward accionBuscar(CierrePresupuestoForm forma, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores	= new ActionErrors();
		errores = validarCamposRequeridosBuscar(forma, request);
		ICierrePresupuestoCapitacionoMundo cierrePresupuestoCapitacionoMundo = CapitacionFabricaMundo.crearCierrePresupuestoCapitacionoMundo();

		try{
			if(!errores.isEmpty()){
				saveErrors(request, errores);
			}

			else
			{
				HibernateUtil.beginTransaction();

				DtoProcesoPresupuestoCapitado parametros = new DtoProcesoPresupuestoCapitado();

				parametros.setFechaInicio(forma.getFechaInicio());
				parametros.setFechaFin(forma.getFechaFin());
				parametros.setInstitucion(usuario.getCodigoInstitucionInt());
				parametros.setLoginUsuario(forma.getUsuarioSeleccionado());
				parametros.setCaseParaBusquedaLog(null);

				ArrayList<DtoLogCierrePresuCapita> listaLogs = cierrePresupuestoCapitacionoMundo.obtenerLogs(parametros);

				//Ordenar Logs para el caso que haya uno parcial y otro finalizado generado en el mismo cierre
				ArrayList<DtoLogCierrePresuCapita> listaDefinitivaLogs = new ArrayList<DtoLogCierrePresuCapita>();
				ArrayList<String> logsProcesados= new ArrayList<String>();
				for(DtoLogCierrePresuCapita dtoLog:listaLogs)
				{
					if(!logsProcesados.contains(dtoLog.getFechaGeneracion()+ConstantesBD.separadorSplit+
							dtoLog.getFechaCierre()+ConstantesBD.separadorSplit+
							dtoLog.getHoraGeneracion()))
					{
						boolean guardaParcial=false;
						for(DtoLogCierrePresuCapita dtoLogInterno:listaLogs)
						{
							if(dtoLog.getFechaGeneracion().equals(dtoLogInterno.getFechaGeneracion())
									&&dtoLog.getFechaCierre().equals(dtoLogInterno.getFechaCierre())
									&&dtoLog.getHoraGeneracion().equals(dtoLogInterno.getHoraGeneracion())
									&&dtoLogInterno.getEstado().equals(ConstantesIntegridadDominio.acronimoLactanciaParcial))
							{
								listaDefinitivaLogs.add(dtoLogInterno);
								guardaParcial=true;
							}
						}
						if(!guardaParcial)
							listaDefinitivaLogs.add(dtoLog);

						logsProcesados.add(dtoLog.getFechaGeneracion()+ConstantesBD.separadorSplit+
								dtoLog.getFechaCierre()+ConstantesBD.separadorSplit+
								dtoLog.getHoraGeneracion());
					}

				}

				//forma.setListaLogs(cierrePresupuestoCapitacionoMundo.obtenerLogs(parametros));
				forma.setListaLogs(listaDefinitivaLogs);

				forma.setListaLogsAux(cierrePresupuestoCapitacionoMundo.obtenerLogsParaIndicativo(parametros));

				HibernateUtil.endTransaction();

			}

			if(forma.getListaLogs().size() == 1)
			{	
				/* Solo se muestra directamente el detalle cuando el log no es exitoso */
				if(!forma.getListaLogs().get(0).getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoFinalizado))
				{
					forma.setPosArray(0);
					return accionDetalleFechaCierre(forma, usuario, mapping, request);
				}
			}
		}catch (Exception e) {
			HibernateUtil.abortTransaction();
			logger.error("error consulta hibernate ",e);
		}

		return mapping.findForward("principal");
	}
	
}
