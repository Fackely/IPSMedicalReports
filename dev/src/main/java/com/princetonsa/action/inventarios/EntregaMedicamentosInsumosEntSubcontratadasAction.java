package com.princetonsa.action.inventarios;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;

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
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.inventarios.EntregaMedicamentosInsumosEntSubcontratadasForm;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.AlmacenParametros;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.ICentroCostosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntregaMedicamentosInsumosEntSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IAurorizacionesEntSubCapitacionServicio;


/**
 * @author Cristhian Murillo
 * Clase usada para controlar los procesos de la funcionalidad.
 */
public class EntregaMedicamentosInsumosEntSubcontratadasAction extends Action
{
	
	/**
	 * Variable para definir el negocio
	 */
	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.inventarios.EntregaMedicamentosInsumosEntSubcontratadasForm");
	
	Logger logger = Logger.getLogger(EntregaMedicamentosInsumosEntSubcontratadasAction.class);
	
	IEntregaMedicamentosInsumosEntSubcontratadasServicio entregaMedicamentosInsumosEntSubcontratadasServicio
														= FacturacionServicioFabrica.crearEntregaMedicamentosInsumosEntSubcontratadasServicio();
	
	IAurorizacionesEntSubCapitacionServicio aurorizacionesEntSubCapitacionServicio
														= ManejoPacienteServicioFabrica.crearAurorizacionesEntSubCapitacionServicio();
	
	ICentroCostosServicio centroCostosServicio 			= AdministracionFabricaServicio.crearCentroCostosServicio();
	
	
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception
								{

		Connection con = null;
		try{

			if(form instanceof EntregaMedicamentosInsumosEntSubcontratadasForm){

				EntregaMedicamentosInsumosEntSubcontratadasForm forma = (EntregaMedicamentosInsumosEntSubcontratadasForm)form;
				String estado = forma.getEstado(); 

				Log4JManager.info("Estado: EntregaMedicamentosInsumosEntSubcontratadasAction --> "+estado);

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");



				//------------------------------------------------------------------------------
				// Se valida el paciente cargado en sessión

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				if(paciente==null || paciente.getCodigoPersona()<1){
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no cargado", "errors.paciente.noCargado", true);
				}
				UtilidadBD.closeConnection(con);
				//------------------------------------------------------------------------------


				if( (estado.equals("empezar")) || (estado.equals("resumen")) )
				{
					forma.reset();
					return accionEmpezar(mapping, forma, request, usuario);
				}

				else if( (estado.equals("buscarautorizacion")))
				{
					return accionBuscarAutorizacion(mapping, forma, request, usuario, paciente);
				}

				else if( (estado.equals("detalleautorizacion")))
				{
					return accionDetalleAutorizacion(mapping, forma, request, usuario, paciente, response);
				}

				else if(estado.equals("ordenar"))
				{
					return accionOrdenar(mapping, forma);
				}

				else if(estado.equals("guardarautorizacion"))
				{
					return guardarAutorizacion(mapping, forma, request, usuario, paciente, response);
				}

			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
}
	



	/*------------------------------*/
	/* ACTION						*/
	/*------------------------------*/
	
	/**
	 * Accion inicial inicial donde se realizan todas las validaciones de ingreso a la funcionalidad.
	 * Estas validaciones son: Paciente cargado, Centro de costo del usuario en session y permisos para 
	 * entrega de medicamentos e insumos.
	 * 
	 * @param mapping
	 * @param forma
	 * @param request 
	 * @param usuario 
	 * @author Cristhian Murillo
	 */
	private ActionForward accionEmpezar(ActionMapping mapping,EntregaMedicamentosInsumosEntSubcontratadasForm forma, 
			HttpServletRequest request, UsuarioBasico usuario) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		Connection con = null;
    	con = UtilidadBD.abrirConexion();
    	
		CentrosCosto centroCosto = obtenerCentroCostoUsuarioSession(usuario);

		boolean centroCostoValido = false;
		
		if(centroCosto == null){
			Log4JManager.info("no se pudo cargar el centro de costo del usuario en sesión: "+usuario.getCodigoCentroCosto());
			centroCostoValido = false;
		}
		else{
			Log4JManager.info("Tipo entidad ejecuta del centro de costo ["+centroCosto.getCodigo()+"]: "+centroCosto.getTipoEntidadEjecuta());
			if(centroCosto.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoInterna))
			{
				String mensajeConcreto = fuenteMensaje.getMessage("EntregaMedicamentosInsumosEntSubcontratadasForm.centroCostoInvalido");
				centroCostoValido = false;
				UtilidadBD.closeConnection(con);
				UtilidadTransaccion.getTransaccion().rollback();
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, mensajeConcreto, mensajeConcreto, false);
			}
			else{
				if( (centroCosto.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoExterna))
					|| (centroCosto.getTipoEntidadEjecuta().equals(ConstantesIntegridadDominio.acronimoAmbos)) ){
					centroCostoValido = true;
				}
			}
		}
		
		
		if(centroCostoValido){
			
			ArrayList<DtoEntidadSubcontratada> listaEntidadesSubCon = new ArrayList<DtoEntidadSubcontratada>();
			
			// Se buscan las entidades subcontratadas activas asociadas al centro de costo activo.
			listaEntidadesSubCon = entregaMedicamentosInsumosEntSubcontratadasServicio.listarEntidadesSubXCentroCostoActivo(usuario.getCodigoCentroCosto());
			
			if(Utilidades.isEmpty(listaEntidadesSubCon))
			{
				String mensajeConcreto = fuenteMensaje.getMessage("EntregaMedicamentosInsumosEntSubcontratadasForm.noEntidadesAsociadas");
				UtilidadBD.closeConnection(con);
				UtilidadTransaccion.getTransaccion().rollback();
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, mensajeConcreto, mensajeConcreto, false);
			}
			else{
				
				ArrayList<DtoEntidadSubcontratada> listaConPermisos = new ArrayList<DtoEntidadSubcontratada>();
				
				// Por cada entidad se valida qué el usuario tenga permisos sobre ella
				for (DtoEntidadSubcontratada dtoEntidadSubcontratada : listaEntidadesSubCon) 
				{
					if(!Utilidades.isEmpty(entregaMedicamentosInsumosEntSubcontratadasServicio.buscarUsuariosEntidadSubPorUsuarioEntidad(
							usuario.getLoginUsuario(), dtoEntidadSubcontratada.getCodigoPk())))
					{
						// Se agregan solo las que tienen permiso
						listaConPermisos.add(dtoEntidadSubcontratada);
					}
				}
				
				if(Utilidades.isEmpty(listaConPermisos)){
					String mensajeConcreto = fuenteMensaje.getMessage("EntregaMedicamentosInsumosEntSubcontratadasForm.noPermisoEntrega");
					UtilidadBD.closeConnection(con);
					UtilidadTransaccion.getTransaccion().rollback();
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, mensajeConcreto, mensajeConcreto, false);
				}
				else{
					// Se muestra solamente las entidades que el usuario tiene permiso
					
					if(Utilidades.isEmpty(listaConPermisos)){
						String mensajeConcreto = fuenteMensaje.getMessage("EntregaMedicamentosInsumosEntSubcontratadasForm.noRegistroAuto");
						mostrarErrorEnviado(forma, request, mensajeConcreto);
						UtilidadBD.closeConnection(con);
						UtilidadTransaccion.getTransaccion().rollback();
						return mapping.findForward("principal");
					}
					else{
						forma.setListaEntidadesSubcontratadas(listaConPermisos);
					}
				}
			}
		}
		
		UtilidadBD.closeConnection(con);
		
		UtilidadTransaccion.getTransaccion().commit();
		
		return mapping.findForward("principal");
	}


	
	
	/**
	 * Busqueda de autorizaciones tomando la entidad subcontratada seleccionada
	 * @param mapping
	 * @param forma
	 * @param request 
	 * @param usuario 
	 * @param paciente
	 * @autor Cristhian Murillo
	 */
	private ActionForward accionBuscarAutorizacion(ActionMapping mapping,EntregaMedicamentosInsumosEntSubcontratadasForm forma, 
			HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		forma.setListaAutorizacionesEntSub(new ArrayList<DtoAutorizacionEntSubcontratadasCapitacion>());
		
		if(UtilidadTexto.isEmpty(forma.getEntidadSubcontratadaSelect()))
		{
			mostrarErrorNoEntidadSub(forma, request);
		}
		else
		{
			UtilidadTransaccion.getTransaccion().begin();
			
			DtoAutorizacionEntSubcontratadasCapitacion dtoBusqueda = new DtoAutorizacionEntSubcontratadasCapitacion();
			dtoBusqueda.setCodigoEntidadSubcontratada(Long.parseLong(forma.getEntidadSubcontratadaSelect()));
			dtoBusqueda.setEstado(ConstantesIntegridadDominio.acronimoAutorizado);
			dtoBusqueda.setEstado2(ConstantesIntegridadDominio.acronimoEstadoPendiente);
			dtoBusqueda.setCodigoPaciente(paciente.getCodigoPersona());
			dtoBusqueda.setValorGenerico(ConstantesIntegridadDominio.acronimoArticulo);
			
			forma.setListaAutorizacionesEntSub(entregaMedicamentosInsumosEntSubcontratadasServicio.obtenerAutorizacionesPorEntSub(dtoBusqueda));
			
			UtilidadTransaccion.getTransaccion().commit();
			
			// Se verifica si se cargaron autorizaciones
			if(Utilidades.isEmpty(forma.getListaAutorizacionesEntSub()))
			{
				String mensajeConcreto = fuenteMensaje.getMessage("EntregaMedicamentosInsumosEntSubcontratadasForm.noExistenAutorizaciones");
				mostrarErrorEnviado(forma, request, mensajeConcreto);
			}
			else{
				for (DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacion : forma.getListaAutorizacionesEntSub()) 
				{
					dtoAutorizacionEntSubcontratadasCapitacion = cargarDetallesAutorizacion(dtoAutorizacionEntSubcontratadasCapitacion);
				}
			}
		}
		
		return mapping.findForward("principal");
	}

	

	/**
	 * Busqueda el detalle de la autorizacion seleccionada
	 * @param mapping
	 * @param forma
	 * @param request 
	 * @param usuario 
	 * @param paciente
	 * @param response
	 * @autor Cristhian Murillo
	 */
	private ActionForward accionDetalleAutorizacion(ActionMapping mapping, EntregaMedicamentosInsumosEntSubcontratadasForm forma, 
			HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente, HttpServletResponse response) 
	{
		Log4JManager.info("Posición seleccionada: "+forma.getPosArray());
		
		Connection con = null;
    	con = UtilidadBD.abrirConexion();
    	forma.setMostrarDetalle(false);
    	
    	UtilidadTransaccion.getTransaccion().begin();
    	
		
		forma.setAutorizacionSeleccionada(new DtoAutorizacionEntSubcontratadasCapitacion());
		forma.setAutorizacionSeleccionada(forma.getListaAutorizacionesEntSub().get(forma.getPosArray()));
		
		// Se valida que se tenga una Solicitud asociada (Orden Médica)
		if(  (forma.getAutorizacionSeleccionada().getNumeroOrden() != null) && (forma.getAutorizacionSeleccionada().getNumeroOrden() >0) )
		{
			CentrosCosto centroCosto = obtenerCentroCostoUsuarioSession(usuario);
			if(centroCosto.getTiposArea().getCodigo() != ConstantesBD.codigoTipoAreaSubalmacen)
			{
				String mensajeConcreto = fuenteMensaje.getMessage("EntregaMedicamentosInsumosEntSubcontratadasForm.noPermisoEntregaCentro");
				mostrarErrorEnviado(forma, request, mensajeConcreto);
				UtilidadBD.closeConnection(con);
				UtilidadTransaccion.getTransaccion().rollback();
				return mapping.findForward("principal");
			}
			else{
				
				boolean manejaExistenciasNegativa = true;
				
				//almacen_parametros
				manejaExistenciasNegativa = AlmacenParametros.manejaExistenciasNegativasCentroAten(
						con, centroCosto.getCodigo(), usuario.getCodigoInstitucionInt());			
				
				if (!manejaExistenciasNegativa) 
				{
					String mensajeConcreto = fuenteMensaje.getMessage("EntregaMedicamentosInsumosEntSubcontratadasForm.noExistenciaNegativa");
					mostrarErrorEnviado(forma, request, mensajeConcreto);
					UtilidadBD.closeConnection(con);
					UtilidadTransaccion.getTransaccion().rollback();
					return mapping.findForward("principal");
				}
				else
				{
					UtilidadBD.closeConnection(con);
					UtilidadTransaccion.getTransaccion().rollback();
					llamarRutaDespachoMedicamentos(paciente, response, request, mapping, forma);
				}
			}
		}
		else{
			// Si no tiene una Solicitud asociada(Orden Médica)
			
			forma.setMostrarDetalle(true);
			
			forma.setAutorizacionSeleccionada(cargarDetallesAutorizacion(forma.getAutorizacionSeleccionada()));
			
			forma.getListaAutorizacionesEntSub().set(forma.getPosArray(), forma.getAutorizacionSeleccionada());
		}
		

		UtilidadTransaccion.getTransaccion().commit();
		
		UtilidadBD.closeConnection(con);
		
		return mapping.findForward("principal");
	}

	
	
	
	/**
	 * Carga los detalles de la autorización
	 * @param dtoAutorizacionEntSubcontratadasCapitacion
	 * @return DtoAutorizacionEntSubcontratadasCapitacion
	 * @autor Cristhian Murillo
	 */
	private DtoAutorizacionEntSubcontratadasCapitacion cargarDetallesAutorizacion(DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacion)
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		ArrayList<DtoArticulosAutorizaciones> listaArticulos = new ArrayList<DtoArticulosAutorizaciones>();
		listaArticulos = aurorizacionesEntSubCapitacionServicio.listarautorizacionesEntSubArticuPorAutoEntSub(dtoAutorizacionEntSubcontratadasCapitacion);
		
		if(!Utilidades.isEmpty(listaArticulos))
		{
			dtoAutorizacionEntSubcontratadasCapitacion.setListaArticulos(listaArticulos);
			aurorizacionesEntSubCapitacionServicio.validarListaArticulos(dtoAutorizacionEntSubcontratadasCapitacion);
			dtoAutorizacionEntSubcontratadasCapitacion.setEntregaDirectaPaciente(true);
		}
		
		UtilidadTransaccion.getTransaccion().commit();
		
		return dtoAutorizacionEntSubcontratadasCapitacion;
	}
	

	/**
	 * Guarda la autorizacion seleccionada, la solicitud de pedido, el despacho del pedido 
	 * y el registro de la entrega de medicamentos e insumos.
	 * 
	 * @param mapping
	 * @param forma
	 * @param request 
	 * @param usuario 
	 * @param paciente
	 * @param response
	 * @autor Cristhian Murillo
	 */
	private ActionForward guardarAutorizacion(ActionMapping mapping,EntregaMedicamentosInsumosEntSubcontratadasForm forma, 
			HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente, HttpServletResponse response) 
	{
		ActionErrors errores = validacionesParaGuardar(forma, request);
		
		if(errores.isEmpty())
		{
			UtilidadTransaccion.getTransaccion().begin();
			
			boolean save = entregaMedicamentosInsumosEntSubcontratadasServicio.guardarEntregaMedicamentoInsumoEntSub(forma.getAutorizacionSeleccionada(), usuario, paciente.getCodigoPersona());
			
			if(save){
				UtilidadTransaccion.getTransaccion().commit();
				forma.setEstado("resumen");
			}
			else{
				UtilidadTransaccion.getTransaccion().rollback();
			}
		}
		else{
			saveErrors(request, errores);
			forma.setEstado("detalleautorizacion");
		}	
		return mapping.findForward("principal");
	}

	
	/**
	 * Validaciones requeridas del anexo 1099 para guardar la entrega de medicamentos en insumos
	 * @param dtoAutorizacionEntSubcontratadasCapitacion
	 * @return ActionForward
	 * @author Cristhian Murillo
	 */
	private ActionErrors validacionesParaGuardar(EntregaMedicamentosInsumosEntSubcontratadasForm forma,HttpServletRequest request)
	{
		/*
		 	Se debe validar que el despacho a realizar + los despachos anteriores sea < = al campo Total Unidades de la columna Formulación:
			Si la S de despachos es > = al Total Unidades, se debe asignar automáticamente cantidad entrega 0.
			Si la S de despachos es < al Total Unidades, se permite la captura de este campo.
			
			Se debe validar que el despacho a realizar + los despachos anteriores sea < = al campo Cantidad Solicitada:
			Si la S de despachos es > = al Cantidad Solicitada, se debe asignar automáticamente cantidad entrega 0.
			Si la S de despachos es < al Cantidad Solicitada, se permite la captura de este campo.
			
			NOTA: Total Unidades de la columna Formulación = Cantidad Solicitada
		 */
		
		ActionErrors errores = new ActionErrors();
		
		for (DtoArticulosAutorizaciones dtoArticulosAutorizaciones : forma.getAutorizacionSeleccionada().getListaArticulos()) 
		{
			if(dtoArticulosAutorizaciones.getDespachoEntregar() != null)
			{
				if(dtoArticulosAutorizaciones.getDespachoEntregar() >= 0)
				{
					int sumaDespacho = dtoArticulosAutorizaciones.getDespachoTotal() +  dtoArticulosAutorizaciones.getDespachoEntregar();
					if(sumaDespacho > dtoArticulosAutorizaciones.getTotalUnidadesFormulacion() )
					{
						String mensajeConcreto = fuenteMensaje.getMessage("EntregaMedicamentosInsumosEntSubcontratadasForm.despachoMayorTotal", dtoArticulosAutorizaciones.getCodigoArticulo() +"-"+ dtoArticulosAutorizaciones.getDescripcionArticulo());
						errores.add("error no_entidad_sub", new ActionMessage("errors.notEspecific", mensajeConcreto));
					}
				}
				else{
					String mensajeConcreto = fuenteMensaje.getMessage("EntregaMedicamentosInsumosEntSubcontratadasForm.despachoMayorCero", dtoArticulosAutorizaciones.getCodigoArticulo() +"-"+ dtoArticulosAutorizaciones.getDescripcionArticulo());
					errores.add("error no_entidad_sub", new ActionMessage("errors.notEspecific", mensajeConcreto));
				}
			}
			else{
				String mensajeConcreto = fuenteMensaje.getMessage("EntregaMedicamentosInsumosEntSubcontratadasForm.despachoMayorCero", dtoArticulosAutorizaciones.getCodigoArticulo() +"-"+ dtoArticulosAutorizaciones.getDescripcionArticulo());
				errores.add("error no_entidad_sub", new ActionMessage("errors.notEspecific", mensajeConcreto));
			}
		}
		
		return errores;
	}
	
	
	
	
	/*--------------------------------------*/
	/* UTILIDADES PROPIAS					*/
	/*--------------------------------------*/
	
	/**
	 * Ordena la presentación de als autorizaciones
	 * @param mapping
	 * @param forma
	 * @return ActionForward
	 * @autor Cristhian Murillo
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,	EntregaMedicamentosInsumosEntSubcontratadasForm forma) 
	{
		boolean ordenamiento= false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaAutorizacionesEntSub(),sortG);
		
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * Retorna una instancia cmopelta del centro de costo del usuario en session lista para validaciones
	 * @return CentrosCosto
	 * @autor Cristhian Murillo
	 */
	private CentrosCosto obtenerCentroCostoUsuarioSession(UsuarioBasico usuario)
	{
		CentrosCosto centroCosto;
		centroCosto = new CentrosCosto();
		centroCosto = centroCostosServicio.findById(usuario.getCodigoCentroCosto());
		return centroCosto;
	}
	
	
	
	/**
	 * Llama la funcionalidad de Despacho de Medicamentos e Insumos
	 * @param paciente
	 * @param response
	 * @param request
	 * @param mapping
	 * @param forma
	 * 
	 * @author Cristhian Murillo
	 */
	private ActionForward llamarRutaDespachoMedicamentos( PersonaBasica paciente, HttpServletResponse response, 
			HttpServletRequest request, ActionMapping mapping, EntregaMedicamentosInsumosEntSubcontratadasForm forma)
	{
		String ruta = 
			request.getContextPath()+"/despachoMedicamentos/despacho.do?" +
			"estado=detalleSolicitudOtraFuncionalidad&" +
			"tipoListado=paciente&" +	
			"codigoCentroCostoSolicitado="+forma.getAutorizacionSeleccionada().getCodigoCentroCostoSolicitado()+"&" + 
			"nombreCentroCostoSolicitado="+forma.getAutorizacionSeleccionada().getNombreCentroCostoSolicitado()+"&" +
			"codigoAutorizacionEntidadSubcontratada="+forma.getAutorizacionSeleccionada().getAutorizacion()+"&" + 
			"numeroSolicitud="+forma.getAutorizacionSeleccionada().getNumeroOrden()+"&" + 
			"&urlRetorno="+request.getContextPath()+"/entregaMedicamentosInsumosEntSubcontratada/entregaMedicamentosInsumosEntSubcontratada.do?estado=empezar";
		
			Log4JManager.info("Ruta llamar: "+ruta);
		
		try 
		{
			response.sendRedirect(ruta);
			return null;
		} 
		catch (IOException e) 
		{
			logger.error("Error haciendo la redirección ",e);
			ActionErrors errores=new ActionErrors();
			errores.add("error_redireccionando",new ActionMessage("errors.notEspecific", "Error redireccionando a Despacho de Medicamentos e Insumos"));
			saveErrors(request, errores);
			return mapping.findForward("paginaError");
		}
	}
	
	
	
	/*--------------------------------------*/
	/* METODOS DE MENSAJES DE ERRORE 		*/
	/*--------------------------------------*/
	
	/**
	 * Muestra un error si no se ha seleccionado la entidad subcontratada
	 * @param forma
	 * @param request
	 * @autor Cristhian Murillo
	 */
	private void mostrarErrorNoEntidadSub(EntregaMedicamentosInsumosEntSubcontratadasForm forma, HttpServletRequest request) 
	{
		String mensajeConcreto = fuenteMensaje.getMessage("EntregaMedicamentosInsumosEntSubcontratadasForm.noEntidadSub");
		ActionErrors errores = new ActionErrors();
		errores.add("error no_entidad_sub", new ActionMessage("errors.notEspecific", mensajeConcreto));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	
	/**
	 * Muestra el mensaje de error enviado
	 * @param forma
	 * @param request
	 * @autor Cristhian Murillo
	 */
	private void mostrarErrorEnviado(EntregaMedicamentosInsumosEntSubcontratadasForm forma, HttpServletRequest request, String mensajeConcreto) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error_concreto_enviado", new ActionMessage("errors.notEspecific", mensajeConcreto));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	
}
