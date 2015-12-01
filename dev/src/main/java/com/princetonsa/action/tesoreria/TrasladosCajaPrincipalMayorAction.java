/**
 * 
 */
package com.princetonsa.action.tesoreria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.tesoreria.TrasladosCajaPrincipalMayorForm;
import com.princetonsa.dto.tesoreria.DtoConsolidadoCierreReporte;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICajasMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.INotaPacienteMundo;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.EntregaCajaMayor;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TrasladoCajaPrincipalMayor;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasCajerosServicio;

/**
 * @author Cristhian Murillo
 * 
 * Traslados de Caja Principal a Mayor
 */
public class TrasladosCajaPrincipalMayorAction extends Action 
{
	
	/** * Contiene la lista de mensajes correspondiente a esta forma */
	private MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.tesoreria.TrasladosCajaPrincipalMayorForm");
	
	
	
	 /**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		if(form instanceof TrasladosCajaPrincipalMayorForm)
		{
			TrasladosCajaPrincipalMayorForm forma = (TrasladosCajaPrincipalMayorForm)form;
			UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
			InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			request.getSession().setAttribute("usuarioBasico", usuario);
			forma.setInstitucionBasica(institucionBasica);
			
			String estado = forma.getEstado();
			Log4JManager.info("estado--->"+estado);
			
			if(estado == null)
			{
				Log4JManager.error("Estado no valido ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("empezar") )
			{
				accionEmpezar(forma, usuario, request);
				
				if(forma.getListaCentrosAtencion().size() == 1)
				{
					forma.setSelectCentroAtencion(forma.getListaCentrosAtencion().get(0).getConsecutivo()+"");
					cambiarCentroAtencion(forma, usuario);
				}
				
				return mapping.findForward("principal");
			}
			else if(estado.equals("asignarValor") )
			{
				return null;
			}
			else if(estado.equals("cambiarCentroAtencion") )
			{
				cambiarCentroAtencion(forma, usuario);
				
				if(forma.isTipoConsulta()){
					return mapping.findForward("principalConsulta");
				}
				else{
					return mapping.findForward("principal");
				}
				
			}
			else if(estado.equals("buscar") )
			{
				buscar(forma, usuario, request, mapping);
				
				if(Utilidades.isEmpty(forma.getListaTraslados()))
				{
					if(forma.isTipoConsulta()){
						return mapping.findForward("principalConsulta");
					}
					else{
						return mapping.findForward("principal");
					}
				}
				else
				{
					if(forma.isTipoConsulta()){
						if(forma.getListaTraslados().size() == 1){
							forma.setPosArray(Integer.parseInt(forma.getListaTraslados().get(0).getConsecutivoTraslado()+""));
							return mostrarDetalle(mapping, forma, usuario);
						}
						else{
							return mapping.findForward("listadoConsultaInicial");
						}
						
					}
					else{
						return mapping.findForward("listado");
					}
				}
			}
			
			else if(estado.equals("trasladar") )
			{
				trasladar(forma, usuario);
				return mapping.findForward("listado");
			}
			
			else if(estado.equals("impresion") )
			{
				if(forma.isTipoConsulta()){
					return mapping.findForward("impresionConsulta");
				}
				else{
					return mapping.findForward("impresion");
				}
			}
			
			else if(estado.equals("volverEmpezar") )
			{
				return mapping.findForward("principal");
			}
			
			else if(estado.equals("volverConsulta") )
			{
				return mapping.findForward("principalConsulta");
			}
			
			else if(estado.equals("volverConsultaInicial") )
			{
				buscar(forma, usuario, request, mapping);
				if(forma.getListaTraslados().size() >1){
					return mapping.findForward("listadoConsultaInicial"); 
				}
				else{
					return mapping.findForward("principalConsulta");
				}
			}
			
			else if(estado.equals("empezarConsulta") )
			{
				accionEmpezarConsulta(forma, usuario, request);
				forma.setTipoConsulta(true);
				
				if(forma.getListaCentrosAtencion().size() == 1)
				{
					forma.setSelectCentroAtencion(forma.getListaCentrosAtencion().get(0).getConsecutivo()+"");
					cambiarCentroAtencion(forma, usuario);
				}
				
				return mapping.findForward("principalConsulta");
			}
			
			else if(estado.equals("detalletraslado"))
			{
				return mostrarDetalle(mapping, forma, usuario);
			}
			
			else if(estado.equals("ordenar"))
			{
				return accionOrdenar(mapping, forma);
			}
			
			else
			{
				Log4JManager.error("Estado no valido");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				return mapping.findForward("paginaError");
			}
		}
	    else
		{
	    	Log4JManager.error("El form no es compatible con el form Forma.");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
		}
	}
	

	/**
	 * Guarda el traslado asociando el movimiento de caja a la caja mayor
	 * @param forma
	 * @param usuario
	 * 
	 * @author Cristhian Murillo
	 */
	private void trasladar(TrasladosCajaPrincipalMayorForm forma, UsuarioBasico usuario) 
	{
		IMovimientosCajaMundo movimientosCajaMundo = TesoreriaFabricaMundo.crearMovimientosCajaMundo();
		boolean trasladoExitoso = true;
			
		UtilidadTransaccion.getTransaccion().begin();
		
		try 
		{
			Usuarios usuarios;
			usuarios = new Usuarios();
			usuarios.setLogin(usuario.getLoginUsuario());
			
			Date fecha 	= UtilidadFecha.getFechaActualTipoBD();
			String hora = UtilidadFecha.getHoraActual();
			
			Cajas cajaMayor;
			cajaMayor = new Cajas();
			cajaMayor.setConsecutivo(Integer.parseInt(forma.getSelectCajaMayor()));
			
			String consecutivoEntregaCajaMayor = UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoEntregaCajaMayor,usuario.getCodigoInstitucionInt());
			Long consecutivo = null;
			
			if(!UtilidadTexto.isEmpty(consecutivoEntregaCajaMayor))
			{
				consecutivo = Long.parseLong(consecutivoEntregaCajaMayor);
				
				Connection conH = UtilidadPersistencia.getPersistencia().obtenerConexion();
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH, 
						ConstantesBD.nombreConsecutivoAutorizacionPoblacionCapitada, Integer.valueOf(usuario.getCodigoInstitucionInt()), 
						consecutivoEntregaCajaMayor, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			}
			
			for(DtoConsolidadoCierreReporte dtoConsolidadoCierreReporte : forma.getListaTraslados())
			{
				Log4JManager.info("Movimiento de caja a trasladar: "+dtoConsolidadoCierreReporte.getMovimientoCaja());
				
				MovimientosCaja movimientoCaja;
				movimientoCaja = new MovimientosCaja();
				movimientoCaja.setCodigoPk(dtoConsolidadoCierreReporte.getMovimientoCaja());
				
				EntregaCajaMayor entregaCajaMayor;
				entregaCajaMayor = new EntregaCajaMayor();
				entregaCajaMayor.setMovimientosCajaByCodigoPk(movimientoCaja);
				entregaCajaMayor.setMovimientoCaja(dtoConsolidadoCierreReporte.getMovimientoCaja());
				
				TrasladoCajaPrincipalMayor trasladoCajaPrincipalMayor;
				trasladoCajaPrincipalMayor = new TrasladoCajaPrincipalMayor();
				
				trasladoCajaPrincipalMayor.setEntregaCajaMayor(entregaCajaMayor);
				trasladoCajaPrincipalMayor.setUsuarios(usuarios);
				trasladoCajaPrincipalMayor.setCajas(cajaMayor);
				trasladoCajaPrincipalMayor.setFecha(fecha);
				trasladoCajaPrincipalMayor.setHora(hora);
				trasladoCajaPrincipalMayor.setConsecutivo(consecutivo);
				
				trasladoExitoso = movimientosCajaMundo.persistTrasladoCajaPrincipalMayor(trasladoCajaPrincipalMayor);
			}
			
			UtilidadTransaccion.getTransaccion().commit();
			
		} 
		catch (Exception e) 
		{
			trasladoExitoso = false;
			UtilidadTransaccion.getTransaccion().rollback();
		}
		
		forma.setTrasladoExitoso(trasladoExitoso);
	}

	
	/**
	 * Realiza la búsqueda de movimientos
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param mapping
	 * @author Cristhian Murillo
	 */
	private void buscar(TrasladosCajaPrincipalMayorForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		ActionErrors errores = new ActionErrors();
		errores = validarCamporRequeridos(forma, request);
		
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
		}
		else
		{
			asignarElementosSeleccionados(forma);
			
			if(forma.isTipoConsulta())
			{
				/* Como se va a buscar el listado inicial, se debe agrupar por el consecutivo del traslado */
				forma.setAgruparSumadoPorConsecutivoTraslado(true);
				consultarTrasladosCajaMayor(forma, usuario, mapping); 
			}
			else{
				consultarTrasladosCajaPrincipal(forma, usuario); 
			}
		}
		
		forma.setTrasladoExitoso(false);
	}

	
	/**
	 * Constula los traslado de caja de recaudo a principales
	 * @param forma
	 * @param usuario
	 */
	@SuppressWarnings("rawtypes")
	private void consultarTrasladosCajaPrincipal(TrasladosCajaPrincipalMayorForm forma, UsuarioBasico usuario) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		IConsultaConsolidadoCierresMundo consultaConsolidadoCierresMundo = TesoreriaFabricaMundo.crearConsolidadoCierreMundo();
		forma.setListaTraslados(new ArrayList<DtoConsolidadoCierreReporte>());
		
		forma.setFormasPago(consultaConsolidadoCierresMundo.consultarFormaPago());
		
		if(!Utilidades.isEmpty((ArrayList) forma.getFormasPago())){
			forma.setCantidadColSpanCierres(forma.getFormasPago().size() + forma.getColumnasAdicionales());
		}
		
		DtoConsolidadoCierreReporte parametros = new DtoConsolidadoCierreReporte();
		parametros.setFechaSeleccionada(forma.getFechaCierre());
		parametros.setCentroAtencion(forma.getSelectCentroAtencion());
		parametros.setEmpresaInstitucion(usuario.getCodigoInstitucionInt()+"");
		parametros.setCaja(forma.getSelectCajaPrincipal());
		parametros.setFormasPago(forma.getFormasPago());
		
		ArrayList<DtoConsolidadoCierreReporte> listaTraslados = new ArrayList<DtoConsolidadoCierreReporte>();
		listaTraslados = consultaConsolidadoCierresMundo.consultaTrasladoCajaMayorPrincipal(parametros);

		/* se organiza la lista de forma desendente por hora de apertura */
		SortGenerico sortG = new SortGenerico(ConstantesBD.ordenamientoPorHoraAperura, false);
		Collections.sort(listaTraslados, sortG);

		forma.setListaTraslados(listaTraslados);
		
		UtilidadTransaccion.getTransaccion().commit();
	}


	/**
	 * Asigna el objeto completo del id seleccionado en la lista
	 * @author Cristhian Murillo
	 * @param forma
	 */
	private void asignarElementosSeleccionados(TrasladosCajaPrincipalMayorForm forma) 
	{
		if(UtilidadTexto.isEmpty(forma.getSelectCentroAtencion())){
			forma.setCentroAtencionSeleccionado(new CentroAtencion());
		}
		
		for(CentroAtencion centroAtencion:forma.getListaCentrosAtencion()){
			if(forma.getSelectCentroAtencion().equals(centroAtencion.getConsecutivo()+"")){
				forma.setCentroAtencionSeleccionado(centroAtencion);
				break;
			}
		}
		
		for(Cajas cajaPpal:forma.getListaCajasPrincipal()){
			if(forma.getSelectCajaPrincipal().equals(cajaPpal.getConsecutivo()+"")){
				forma.setCajaPrincipalSeleccionada(cajaPpal);
				break;
			}
		}
		
		for(Cajas cajaMayor:forma.getListaCajasMayor()){
			if(forma.getSelectCajaMayor().equals(cajaMayor.getConsecutivo()+"")){
				forma.setCajaMayorSeleccionada(cajaMayor);
				break;
			}
		}
	}


	/**
	 * Valida los campos requeridos para realizar la búsqueda
	 * @param forma
	 * @param request
	 * @author Cristhian Murillo
	 * 
	 * @return ActionErrors
	 */
	private ActionErrors validarCamporRequeridos(TrasladosCajaPrincipalMayorForm forma, HttpServletRequest request) 
	{
		ActionErrors errores	= new ActionErrors();
		String mensajeConcreto 	= "";
		
		if(forma.isTipoConsulta())
		{
			boolean existeAlgunParametroDeBusqueda = false;
			
			if(forma.getSelectCentroAtencion() != null && !forma.getSelectCentroAtencion().equals(ConstantesBD.codigoNuncaValido+"")){
				existeAlgunParametroDeBusqueda = true;
			}
			
			if(forma.getSelectCajaPrincipal() != null && !forma.getSelectCajaPrincipal().equals(ConstantesBD.codigoNuncaValido+"")){
				existeAlgunParametroDeBusqueda = true;
			}
			
			if(forma.getSelectCajaMayor() != null && !forma.getSelectCajaMayor().equals(ConstantesBD.codigoNuncaValido+"")){
				existeAlgunParametroDeBusqueda = true;
			}
				
			if(forma.getFechaInicio() != null){
				existeAlgunParametroDeBusqueda = true;
				// Fecha Inicial < FechaA Sistema
				if(forma.getFechaInicio().after(UtilidadFecha.getFechaActualTipoBD()))
				{
					mensajeConcreto = fuenteMensaje.getMessage("TrasladosCajaPrincipalMayorForm.fechaInicialMayor");
					errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
				}
			}
			
			if(forma.getFechaFin() != null){
				existeAlgunParametroDeBusqueda = true;
				// Validar que  fecha ingresada >= Fecha Inicial Traslado y < = fecha del sistema.
				if(forma.getFechaInicio() != null){
					if(forma.getFechaInicio().after(forma.getFechaFin()))
					{
						mensajeConcreto = fuenteMensaje.getMessage("TrasladosCajaPrincipalMayorForm.fechaInicialMayorFin");
						errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
					}
				}
			}
			
			if(!UtilidadTexto.isEmpty(forma.getNumeroInicioTraslado())){
				existeAlgunParametroDeBusqueda = true;
			}
			
			if(!UtilidadTexto.isEmpty(forma.getNumeroFinTraslado())){
				existeAlgunParametroDeBusqueda = true;
				if(!UtilidadTexto.isEmpty(forma.getNumeroInicioTraslado()))
				{
					long numeroInicial = Long.parseLong(forma.getNumeroInicioTraslado().trim());
					long numeroFinal = Long.parseLong(forma.getNumeroFinTraslado().trim());
					if(numeroFinal < numeroInicial){
						mensajeConcreto = fuenteMensaje.getMessage("TrasladosCajaPrincipalMayorForm.numeroFinalInicial");
						errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
					}
				}
			}
			
			if(!existeAlgunParametroDeBusqueda){
				mensajeConcreto = fuenteMensaje.getMessage("TrasladosCajaPrincipalMayorForm.sinParametrosBusqueda");
				errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
			}
		}
		else
		{
			if(forma.getSelectCentroAtencion() == null || forma.getSelectCentroAtencion().equals(ConstantesBD.codigoNuncaValido+""))
			{
				mensajeConcreto = fuenteMensaje.getMessage("TrasladosCajaPrincipalMayorForm.centroAtencion");
				errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
			}
			
			if(forma.getSelectCajaPrincipal() == null 
					|| forma.getSelectCajaPrincipal().equals(ConstantesBD.codigoNuncaValido+""))
			{
				mensajeConcreto = fuenteMensaje.getMessage("TrasladosCajaPrincipalMayorForm.cajaPrincipal");
				errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
			}
			
			if(forma.getSelectCajaMayor() == null 
					|| forma.getSelectCajaMayor().equals(ConstantesBD.codigoNuncaValido+""))
			{
				mensajeConcreto = fuenteMensaje.getMessage("TrasladosCajaPrincipalMayorForm.cajaMayor");
				errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
			}
				
			if(forma.getFechaCierre() == null)
			{
				mensajeConcreto = fuenteMensaje.getMessage("TrasladosCajaPrincipalMayorForm.fechaCierre");
				errores = retornarErrorEnviado(forma, request, mensajeConcreto, errores);
			}
		}
		
		return errores;
	}

	
	/**
	 * Muestra el mensaje de error enviado
	 * @param forma
	 * @param request
	 * @param errores
	 * @autor Cristhian Murillo
	 * 
	 * @return ActionErrors
	 */
	private ActionErrors retornarErrorEnviado(TrasladosCajaPrincipalMayorForm forma, HttpServletRequest request, String mensajeConcreto, ActionErrors errores) 
	{
		errores.add("error_concreto_enviado", new ActionMessage("errors.notEspecific", mensajeConcreto));
		saveErrors(request, errores);
		return errores;
	}

	
	/**
	 * Realiza las consultas por el centro de atención seleccionado
	 * @param forma
	 * @param usuario
	 */
	private void cambiarCentroAtencion(TrasladosCajaPrincipalMayorForm forma, UsuarioBasico usuario) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		ICajasMundo cajasMundo = TesoreriaFabricaMundo.crearCajasMundo();
		
		ArrayList<Cajas> listaCajasPrincipal 	= new ArrayList<Cajas>();
		ArrayList<Cajas> listaCajasMayor		= new ArrayList<Cajas>();
		
		if(forma.getSelectCentroAtencion() != null)
		{
			if(!forma.getSelectCentroAtencion().equals(ConstantesBD.codigoNuncaValido+""))
			{
				/* Se llenan los parametros para la búsqueda */
				UsuarioBasico usuarioParametros;
				usuarioParametros = new UsuarioBasico();
				usuarioParametros.setCodigoCentroAtencion(Integer.parseInt(forma.getSelectCentroAtencion()));
				usuarioParametros.setLoginUsuario(usuario.getLoginUsuario());
				usuarioParametros.setCodigoInstitucion(usuario.getCodigoInstitucionInt()+"");
				/* ----------------------------------------- */
				
				listaCajasPrincipal = cajasMundo.listarCajasPorCajeroActivasXInstitucionXCentroAtencion(usuarioParametros, ConstantesBD.codigoTipoCajaPpal);
				
				usuarioParametros.setLoginUsuario(null);
				listaCajasMayor 	= cajasMundo.listarCajasPorCajeroActivasXInstitucionXCentroAtencion(usuarioParametros, ConstantesBD.codigoTipoCajaMayor);
			}
		}
		
		forma.setListaCajasMayor(listaCajasMayor);
		forma.setListaCajasPrincipal(listaCajasPrincipal);
		
		UtilidadTransaccion.getTransaccion().commit();
	}
	
	
	/**
	 * Inicia la funcionalidad.
	 * 
	 * @param forma
	 * @param usuario
	 * @param request
	 * @author Cristhian Murillo
	 */
	private void accionEmpezar(TrasladosCajaPrincipalMayorForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		forma.reset();
		
		UtilidadTransaccion.getTransaccion().begin();
		
		if(validarUsuarioEsCajero(usuario))
		{
			if(validarTieneCajasAsociadas(usuario))
			{
				if(validarConsecutivoTraslado(usuario))
				{
					llenarForma(forma, usuario); 
				}
				else
				{
					forma.setPasoValidacionesIniciales(false);
					String mensajeConcreto = fuenteMensaje.getMessage("TrasladosCajaPrincipalMayorForm.noConsecutivoTrasladoCajaMayor");
					mostrarErrorEnviado(request, mensajeConcreto);
				}
			}
			else{
				forma.setPasoValidacionesIniciales(false);
				String mensajeConcreto = fuenteMensaje.getMessage("TrasladosCajaPrincipalMayorForm.noCajasAsociadas", usuario.getLoginUsuario());
				mostrarErrorEnviado(request, mensajeConcreto);
			}
		}
		else
		{
			forma.setPasoValidacionesIniciales(false);
			String mensajeConcreto = fuenteMensaje.getMessage("TrasladosCajaPrincipalMayorForm.usuarioNoCajero");
			mostrarErrorEnviado(request, mensajeConcreto);
		}
		
		UtilidadTransaccion.getTransaccion().commit();
	}
	

	/**
	 * Inicia los datos de la forma
	 * @param forma
	 * @param usuario
	 * @author Cristhian Murillo
	 */
	private void llenarForma(TrasladosCajaPrincipalMayorForm forma, UsuarioBasico usuario) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		INotaPacienteMundo notaPacienteMundo = TesoreriaFabricaMundo.crearNotaPacienteMundo();
		forma.setListaCentrosAtencion(notaPacienteMundo.obtenerCentrosAtencionActivosUsuario(usuario.getLoginUsuario()));

		UtilidadTransaccion.getTransaccion().commit();
	}
	

	/**
	 * Valida si el usuario enviado es cajero
	 * @param usuario
	 * @return boolean
	 * @author Cristhian Murillo
	 */
	private boolean validarUsuarioEsCajero(UsuarioBasico usuario)
	{
		ICajasCajerosServicio cajasCajeroServicio = TesoreriaFabricaServicio.crearCajasCajerosServicio();
		return cajasCajeroServicio.validarUsuarioEsCajero(usuario);
	}
	
	
	/**
	 * Valida si el usuario tiene cajas de recaudo asociadas
	 * @param usuario
	 * @return boolean
	 */
	private boolean validarTieneCajasAsociadas(UsuarioBasico usuario) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		ICajasMundo cajasMundo = TesoreriaFabricaMundo.crearCajasMundo();
		
		ArrayList<Cajas> listaCajasPrincipal 	= new ArrayList<Cajas>();
		boolean tieneCajasAsociadas = false;
		
		/* Se llenan los parametros para la búsqueda */
		UsuarioBasico usuarioParametros;
		usuarioParametros = new UsuarioBasico();
		usuarioParametros.setCodigoCentroAtencion(ConstantesBD.codigoNuncaValido);
		usuarioParametros.setLoginUsuario(usuario.getLoginUsuario());
		usuarioParametros.setCodigoInstitucion(usuario.getCodigoInstitucionInt()+"");
		/* ----------------------------------------- */
		
		listaCajasPrincipal = cajasMundo.listarCajasPorCajeroActivasXInstitucionXCentroAtencion(usuarioParametros, ConstantesBD.codigoTipoCajaPpal);
		
		if(Utilidades.isEmpty(listaCajasPrincipal)){
			tieneCajasAsociadas = false;
		}
		else{
			tieneCajasAsociadas = true;
		}
		
		UtilidadTransaccion.getTransaccion().commit();
		
		return tieneCajasAsociadas;
	}
	
	
	/**
	 * Muestra el mensaje de error enviado.
	 * 
	 * @param request
	 * @autor Cristhian Murillo
	 */
	private void mostrarErrorEnviado(HttpServletRequest request, String mensajeConcreto) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error_concreto_enviado", new ActionMessage("errors.notEspecific", mensajeConcreto));
		saveErrors(request, errores);
	}
	
	
	/**
	 * Valida si existe consecutivo para entrega a caja mayor definido
	 * @param usuario
	 * @author Cristhian Murillo
	 * 
	 * @return existeConsecutivo
	 */
	private boolean validarConsecutivoTraslado(UsuarioBasico usuario) 
	{
		Connection conH = UtilidadPersistencia.getPersistencia().obtenerConexion();
		String consecutivoEntregaCajaMayor = UtilidadBD.obtenerValorActualTablaConsecutivos(conH, ConstantesBD.nombreConsecutivoEntregaCajaMayor,usuario.getCodigoInstitucionInt());
		boolean existeConsecutivo = false;
		
		if(!UtilidadTexto.isEmpty(consecutivoEntregaCajaMayor))
		{
			if(consecutivoEntregaCajaMayor.equals(ConstantesBD.codigoNuncaValido+"")){
				existeConsecutivo = false;
			}
			else{
				existeConsecutivo = true;
			}
		}
		
		return existeConsecutivo;
	}
	
	
	/**
	 * Inicia la funcionalidad.
	 * 
	 * @param forma
	 * @param usuario
	 * @param request
	 * @author Cristhian Murillo
	 */
	private void accionEmpezarConsulta(TrasladosCajaPrincipalMayorForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		forma.reset();
		llenarForma(forma, usuario);
	}
	
	
	/**
	 * Constula los traslado de caja mayor.
	 * @param forma
	 * @param usuario
	 * @param mapping
	 * 
	 * @author Cristhian Murillo
	 */
	@SuppressWarnings("rawtypes")
	private void consultarTrasladosCajaMayor(TrasladosCajaPrincipalMayorForm forma, UsuarioBasico usuario, ActionMapping mapping) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		IConsultaConsolidadoCierresMundo consultaConsolidadoCierresMundo = TesoreriaFabricaMundo.crearConsolidadoCierreMundo();
		forma.setListaTraslados(new ArrayList<DtoConsolidadoCierreReporte>());
		
		forma.setFormasPago(consultaConsolidadoCierresMundo.consultarFormaPago());
		
		if(!Utilidades.isEmpty((ArrayList) forma.getFormasPago())){
			forma.setCantidadColSpanCierres(forma.getFormasPago().size() + forma.getColumnasAdicionales());
		}
		
		DtoConsolidadoCierreReporte parametros = new DtoConsolidadoCierreReporte();
		parametros.setFechaInicio(forma.getFechaInicio());	// fecha (TrasladoCajaPrincipalMayor)
		parametros.setFechaFin(forma.getFechaFin());		// fecha (TrasladoCajaPrincipalMayor)
		parametros.setCentroAtencion(forma.getSelectCentroAtencion());
		parametros.setEmpresaInstitucion(usuario.getCodigoInstitucionInt()+"");
		parametros.setCaja(forma.getSelectCajaPrincipal()); // Caja Principal (EntregaCajaMayor)
		parametros.setCaja2(forma.getSelectCajaMayor());	// Caja Mayor (TrasladoCajaPrincipalMayor)
		parametros.setFormasPago(forma.getFormasPago());
		parametros.setNumeroInicioTraslado(forma.getNumeroInicioTraslado()); 
		parametros.setNumeroFinTraslado(forma.getNumeroFinTraslado());
		parametros.setAgruparSumadoPorConsecutivoTraslado(forma.isAgruparSumadoPorConsecutivoTraslado());
		
		if(forma.isFiltrarPorconsecutivo()){
			if(forma.getPosArray() != ConstantesBD.codigoNuncaValido){
				parametros.setConsecutivoTraslado(Long.parseLong(forma.getPosArray()+""));
			}
		}
		
		ArrayList<DtoConsolidadoCierreReporte> listaTraslados = new ArrayList<DtoConsolidadoCierreReporte>();
		
		listaTraslados = consultaConsolidadoCierresMundo.consultaTrasladoHaciaCajaMayor(parametros);
		
		/* se organiza la lista de forma desendente por hora de apertura */
		SortGenerico sortG = new SortGenerico(ConstantesBD.ordenamientoPorHoraAperura, false);
		Collections.sort(listaTraslados, sortG);

		forma.setListaTraslados(listaTraslados);
		
		UtilidadTransaccion.getTransaccion().commit();
		
		
		/* Se resetean las variables que definen comportamientos en los criterios de búsqueda. */
		forma.setAgruparSumadoPorConsecutivoTraslado(false);
		forma.setFiltrarPorconsecutivo(false);
		
	}
	
	
	/**
	 * Método de ordenamiento generico
	 * @param mapping
	 * @param forma
	 * @return ActionForward
	 */
	private ActionForward accionOrdenar(ActionMapping mapping,	TrasladosCajaPrincipalMayorForm forma) 
	{
		boolean ordenamiento = false;
		
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}
		
		SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
		Collections.sort(forma.getListaTraslados(),sortG);

		return mapping.findForward("listadoConsultaInicial"); 
	}
	
	
	/**
	 * Constula los traslado de caja mayor.
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * 
	 * @author Cristhian Murillo
	 */
	private ActionForward mostrarDetalle(ActionMapping mapping, TrasladosCajaPrincipalMayorForm forma, UsuarioBasico usuario) 
	{
		Log4JManager.info("Consecutivo de traslado seleccionado: "+forma.getPosArray());
		
		DtoConsolidadoCierreReporte trasladoSeleccionado = new DtoConsolidadoCierreReporte();
		if(forma.getPosArray() != ConstantesBD.codigoNuncaValido){
			Long consecutivotrasladoSeleccionado = new Long(forma.getPosArray()+"");
			for (DtoConsolidadoCierreReporte dtoConsolidadoCierreReporte : forma.getListaTraslados()) 
			{
				if(dtoConsolidadoCierreReporte.getConsecutivoTraslado().equals(consecutivotrasladoSeleccionado)){
					trasladoSeleccionado = dtoConsolidadoCierreReporte;
					break;
				}
			}
		}
		forma.setTrasladoSeleccionado(trasladoSeleccionado);
		
		/* Solo en este caso se realiza la búsqueda por el consecutivo delt raslado */
		forma.setFiltrarPorconsecutivo(true);
		consultarTrasladosCajaMayor(forma, usuario, mapping);
		
		
		asignarValoresParaMostrarDetalle(forma, usuario);
		
		return mapping.findForward("listadoConsulta"); 
	}


	/**
	 * Asigna los valores para mostrar en la página de impresión
	 * @param forma
	 * @param usuario
	 *
	 * @autor Cristhian Murillo
	 *
	 */
	private void asignarValoresParaMostrarDetalle(TrasladosCajaPrincipalMayorForm forma, UsuarioBasico usuario) 
	{
		Cajas cajaPrincipalSeleccionada 			= new Cajas();
		Cajas cajaMayorSeleccionada 				= new Cajas();
		//CentroAtencion centroAtencionSeleccionado	= new CentroAtencion();
		
		cajaPrincipalSeleccionada.setCodigo(forma.getTrasladoSeleccionado().getCajaInicialCodigo());
		cajaPrincipalSeleccionada.setDescripcion(forma.getTrasladoSeleccionado().getCajaInicialDescripcion());
		
		cajaMayorSeleccionada.setCodigo(forma.getTrasladoSeleccionado().getCajaFinalCodigo());
		cajaMayorSeleccionada.setDescripcion(forma.getTrasladoSeleccionado().getCajaFinalDescripcion());
		
		forma.setCajaMayorSeleccionada(cajaMayorSeleccionada);
		forma.setCajaPrincipalSeleccionada(cajaPrincipalSeleccionada);
		//forma.setCentroAtencionSeleccionado(centroAtencionSeleccionado);
		forma.setFechaCierre(forma.getTrasladoSeleccionado().getFecha());
		forma.setFechaTraslado(forma.getTrasladoSeleccionado().getFechaTraslado());
	}
}
