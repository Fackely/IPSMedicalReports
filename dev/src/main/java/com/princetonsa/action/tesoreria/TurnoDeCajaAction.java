/*
 * Mayo 10, 2010
 */
package com.princetonsa.action.tesoreria;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.tesoreria.TurnoDeCajaForm;
import com.princetonsa.dto.administracion.DtoParametros;
import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoCuadreCaja;
import com.princetonsa.dto.tesoreria.DtoFormaPagoDocSoporte;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.princetonsa.dto.tesoreria.DtoSolicitudTrasladoPendiente;
import com.princetonsa.dto.tesoreria.DtoTurnoDeCajaApta;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento;
import com.servinte.axioma.mundo.impl.tesoreria.TurnoDeCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaArqueoCierreCajaMundo;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.CajasCajeros;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasCajerosServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITurnoDeCajaServicio;
import com.servinte.axioma.vista.helper.tesoreria.TurnoDeCajaHelper;


/**
 * @author Cristhian Murillo
 *
 * Clase usada para controlar los procesos de la funcionalidad.
 * 
 */
public class TurnoDeCajaAction extends MovimientosCajaAction 
{
	/**
	 * Variable para definir el negocio
	 */
	IMovimientosCajaServicio movimientosCajaServicio 	= TesoreriaFabricaServicio.crearMovimientosCajaServicio();
	ICajasCajerosServicio cajasCajeroServicio 			= TesoreriaFabricaServicio.crearCajasCajerosServicio();
	ICajasServicio cajasServicio						= TesoreriaFabricaServicio.crearCajasServicio();
	DtoTurnoDeCajaApta dtoApto 							= null;
	ITurnoDeCajaServicio turnoCajaServicio				= TesoreriaFabricaServicio.crearTurnoDeCajaServicio();
	
	/**
	 * Mï¿½todo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, 
	 * HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
								HttpServletResponse response )throws Exception
	{
		try
		{
			HibernateUtil.beginTransaction();
			if(form instanceof TurnoDeCajaForm){
				
				TurnoDeCajaForm forma = (TurnoDeCajaForm)form;
				String estado = forma.getEstado(); 
				
				
				Log4JManager.info("Estado TurnoDeCajaAction --> "+estado);
				
				UsuarioBasico usuario 	= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				
				if(estado == null)
				{
					request.setAttribute("codigoDescripcionError","errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
				
				
				//----------- TURNO DE CAJA
				else if( (estado.equals("empezar")) || (estado.equals("resumen")) )
				{
					limpiarForma(forma);
					return accionEmpezar(mapping, forma, usuario, request);
				}
				
				else if(estado.equals("nuevaapertura"))
				{
					return accionNuevaApertura(mapping, forma, usuario, request);
				}
				
				else if(estado.equals("cancelarapertura"))
				{
					forma.setEstado("empezar");
					return accionEmpezar(mapping,forma, usuario, request);
				}
				
				else if(estado.equals("confirmarapertura"))
				{
					return accionRealizarApertura(mapping, forma, usuario, request);
				}
				
				else if(estado.equals("cambiarcaja"))
				{
					forma.reset();
					forma.resetSolicitudesPendientes();
					limpiarForma(forma);
					return accionNuevaApertura(mapping, forma, usuario, request);
				}
				
				
						
				//----------- SOLICITUDES DE TRASLADO
				else if(estado.equals("solicitudtraslado"))
				{
					return accionSolicitudtraslado(mapping,forma, request);
				}
	
				else if(estado.equals("realizaraceptacion"))
				{
					return accionRealizarAceptacion(mapping,forma, request, response, usuario);
				}
				
				else if(estado.equals("continuarProceso")){
					
					return accionContinuarAceptacion(mapping, forma, request, usuario);
				}
	
				else if(estado.equals("aceptarDetalleDocumentos")){
					return accionAceptarDetalleSolicitud(mapping, forma, request, usuario);
				}
				
	
				//----------- CONSOLIDADO DE CIERRE
				else if(estado.equals("consultaconsolidado"))
				{
					return accionConsolidadoCierre(mapping, forma, request, usuario);
				}
				
				else if(estado.equals("consultaconsolidadosolicitud"))
				{
					return accionConsolidadoCierreSolicitud(mapping, forma, request, usuario);
				}
				
				/**
				 * Inc 2191: Dos cajeros no puedes acceder a la misma caja al tiempo
				 * Libera la caja cuando se encuentra en proceso de apertura y este no se lleva a cabo exitosamente
				 * saliendose de la p&aacute;gina o cerrando el explorador
				 * @author Diana Ruiz
				 * 
				 */			
				else if(estado.equals("cancelarProcesoProcesoAperturaTurnoCaja"))
				{
					cancelarAperturaTurnoCaja(usuario);
					return null;
				}	
			}
		}
		catch (Exception e)
		{
			HibernateUtil.abortTransaction();
			Log4JManager.error(e);
			e.printStackTrace();
		}
		finally
		{
			HibernateUtil.endTransaction();
		}
		return null;
	}
	



	/*------------------------------*/
	/* ACTION						*/
	/*------------------------------*/
	
	/**
	 * Realiza la aceptacion temporal de la solicitud con los detalles d elos valores y diferencias
	 * 
	 * @param mapping
	 * @param forma
	 * @param request 
	 * @param usuario 
	 */
	private ActionForward accionAceptarDetalleSolicitud(ActionMapping mapping,TurnoDeCajaForm forma, 
			HttpServletRequest request, UsuarioBasico usuario) {
		
		boolean faltaTestigo = false;
		
		if(forma.isParametroRequiereTestigo()){
			if(UtilidadTexto.isEmpty(forma.getDtoInformacionEntrega().getLoginTestigo())){
				faltaTestigo = true;
			}
		}
		
		if(faltaTestigo)
		{
			mostrarRequiereTestigoRequerido(forma, request);
			return mapping.findForward("principal");
		}
		else
		{
			forma.getListaSolicitudesPendientes().get(forma.getPosListaSolPendiente()).setAceptadoTemporalmente(true);

			forma.setMostrarDetalleAceptacion(false);
			forma.setMostrarCuadreCaja(false);
			forma.setEstado("nuevaapertura");
			forma.setLimpiarAceptaciones(false);
			return accionNuevaApertura(mapping, forma, usuario, request);
		}
	}


	
	
	/**
	 * Realiza la operaciï¿½n de las diferencias de los valores y los totales por formas de pago del cuade de caja
	 * 
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 */
	private ActionForward accionContinuarAceptacion(ActionMapping mapping, TurnoDeCajaForm forma,
			HttpServletRequest request,UsuarioBasico usuario) 
	{
		forma.setMostrarConsolidadoCierre(false);
		UtilidadTransaccion.getTransaccion().begin();
		
		forma.setDtoInformacionEntrega(movimientosCajaServicio.completarConsolidadoInformacionAceptacion(forma.getDtoInformacionEntrega(), ETipoMovimiento.ACEPTACION_APERTURA_TURNO));
		
		// consecutivo faltante sobrante
		DtoParametros dtoParametros = new DtoParametros();
		dtoParametros= movimientosCajaServicio.validarDefinicionesParametrosAperturaTurno(usuario);
		
		if(!dtoParametros.isTieneDefinidoConsecutivoFaltanteSobrante())
		{
			boolean mostrarErrorNoConsecutivoFaltanteSobrante = false;
			
			for(DtoCuadreCaja cuadreCaja: forma.getDtoInformacionEntrega().getCuadreCajaDTOs()){
				if(!UtilidadTexto.isEmpty(cuadreCaja.getTipodiferencia())){
					mostrarErrorNoConsecutivoFaltanteSobrante = true;
				}
			}
			
			if(mostrarErrorNoConsecutivoFaltanteSobrante)
			{
				mostrarErrorFaltaDefinir(forma, request);
				return mapping.findForward("principal");
			}
		}
		//--
		
		
		verificarListadoSolicitudes (forma);
		
		boolean requiereTestigo = false;
		requiereTestigo = UtilidadTexto.getBoolean(ValoresPorDefecto
					.getEsRequeridoTestigoSolicitudAceptacionTrasladoCaja(usuario.getCodigoInstitucionInt()));
		
		if(requiereTestigo)
		{
			forma.setListaTestigos((ArrayList<DtoUsuarioPersona>)movimientosCajaServicio.obtenerUsuariosActivosDiferenteDe(usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario(), false));
		}
		
		forma.setParametroRequiereTestigo(requiereTestigo);
		forma.setMostrarCuadreCaja(true);
		
		UtilidadTransaccion.getTransaccion().commit();
		
		return mapping.findForward("principal");
	}

	/**
	 * Muestra los detalles de la solicitud que va a ser aceptada
	 * 
	 * @param mapping
	 * @param forma
	 * @param response 
	 * @param usuario
	 * @return
	 */
	private ActionForward accionRealizarAceptacion(ActionMapping mapping, TurnoDeCajaForm forma, HttpServletRequest request, HttpServletResponse response, UsuarioBasico usuario)
	{
		UtilidadTransaccion.getTransaccion().begin();
		DtoSolicitudTrasladoPendiente solicitudPorAceptar 	= forma.getListaSolicitudesPendientes().get(forma.getPosListaSolPendiente());
		MovimientosCaja movimientoCaja 						= movimientosCajaServicio.obtenerMovimientoCaja(solicitudPorAceptar.getMovimientoCaja());
		DtoInformacionEntrega informacionEntregaDTO		 	= movimientosCajaServicio.consolidarInformacionAceptacion(movimientoCaja.getCodigoPk(), usuario.getCodigoInstitucionInt());
		
		DtoUsuarioPersona responsable = new DtoUsuarioPersona();
		responsable.setLogin(movimientoCaja.getTurnoDeCaja().getUsuarios().getLogin());
		responsable.setNombre(movimientoCaja.getTurnoDeCaja().getUsuarios().getPersonas().getPrimerNombre());
		responsable.setApellido(movimientoCaja.getTurnoDeCaja().getUsuarios().getPersonas().getPrimerApellido());
		informacionEntregaDTO.setResponsable(responsable);
		
		forma.setDtoInformacionEntrega(informacionEntregaDTO);
		
		DtoConsolidadoMovimiento consolidadoMovimientoDTO 	= new DtoConsolidadoMovimiento();
		forma.setConsolidadoMovimientoDTO(consolidadoMovimientoDTO);
		forma.getConsolidadoMovimientoDTO().setMovimientosCaja(movimientoCaja);
		
		for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte : forma.getDtoInformacionEntrega().getListadoDtoFormaPagoDocSoportes()) 
		{
			forma.getConsolidadoMovimientoDTO().getCuadreCajaDTOs().add(dtoFormaPagoDocSoporte.getDtoCuadreCaja());
		}
		
		forma.setMostrarDetalleAceptacion(true);
		
		UtilidadTransaccion.getTransaccion().commit();
		
		return mapping.findForward("principal");
	}
	
	
	
	/**
	 * Muestra las solicitudes de traslado
	 * @param mapping
	 * @param forma
	 * @param request
	 * @return
	 */
	private ActionForward accionSolicitudtraslado(ActionMapping mapping, TurnoDeCajaForm forma, HttpServletRequest request)
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		forma.setListaSolicitudesPendientes(movimientosCajaServicio.obtenerSolicitudesPendientes(forma.getCajaSeleccionada()));
		forma.setMostrarMensajeSolicitudes(true);
		
		UtilidadTransaccion.getTransaccion().commit();
		
		return mapping.findForward("principal");
	}
	
	
	/**
	 * Muestra el consolidado de cierre de la caja que abre el turno
	 * 
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionConsolidadoCierre(ActionMapping mapping, TurnoDeCajaForm forma, HttpServletRequest request, UsuarioBasico usuario)
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		MovimientosCaja movimientoCaja = movimientosCajaServicio.obtenerUltimoTurnoCierre(forma.getCajaSeleccionada(), null);
		
		
		if(movimientoCaja != null)
		{
			IConsultaArqueoCierreCajaMundo consultaArqueoCierreCajaMundo 	= TesoreriaFabricaMundo.crearConsultaArqueoCierreCajaMundo();
			DtoInformacionEntrega dtoInformacionEntrega 					= consultaArqueoCierreCajaMundo.consultarCierreTurnoCaja(movimientoCaja);
			
			if(dtoInformacionEntrega!=null){
				forma.setDtoInformacionEntregaConsolidado(dtoInformacionEntrega);
				forma.setMostrarConsolidadoCierre(true);
				forma.setEsConsultaConsolidadoCierre(true);
				forma.setConsolidadoMovimientoDTO(movimientosCajaServicio.obtenerConsolidadoMovimiento(movimientoCaja));
				forma.getConsolidadoMovimientoDTO().setMovimientosCaja(movimientoCaja);
			}
			else {
				mostrarErrorNoExisteUltimocierre(forma, request);
			}
		}else {
			mostrarErrorNoExisteUltimocierre(forma, request);
		}
		
		
		UtilidadTransaccion.getTransaccion().commit();
		
		return mapping.findForward("principal");
	}
	
	
	
	
	/**
	 * Muestra el consolidado de cierre de la solicitud de traslado abierta
	 * @param mapping
	 * @param forma
	 * @param request
	 * @param usuario
	 * @return
	 */
	private ActionForward accionConsolidadoCierreSolicitud(ActionMapping mapping, TurnoDeCajaForm forma, HttpServletRequest request, UsuarioBasico usuario)
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		DtoSolicitudTrasladoPendiente solicitudPorAceptar 	= forma.getListaSolicitudesPendientes().get(forma.getPosListaSolPendiente());
		MovimientosCaja movimientoCaja = new MovimientosCaja();
		movimientoCaja.setCodigoPk(solicitudPorAceptar.getMovimientoCaja());
		movimientoCaja = movimientosCajaServicio.obtenerUltimoTurnoCierre(null, movimientoCaja); 
		
		if(movimientoCaja != null)
		{
			IConsultaArqueoCierreCajaMundo consultaArqueoCierreCajaMundo 	= TesoreriaFabricaMundo.crearConsultaArqueoCierreCajaMundo();
			DtoInformacionEntrega dtoInformacionEntrega 					= consultaArqueoCierreCajaMundo.consultarCierreTurnoCaja(movimientoCaja);
			
			if(dtoInformacionEntrega!=null){
				forma.setDtoInformacionEntregaConsolidado(dtoInformacionEntrega);
				forma.setMostrarConsolidadoCierre(true);
				forma.setEsConsultaConsolidadoCierre(true);
				forma.setConsolidadoMovimientoDTO(movimientosCajaServicio.obtenerConsolidadoMovimiento(movimientoCaja));
				forma.getConsolidadoMovimientoDTO().setMovimientosCaja(movimientoCaja);
			}
			else {
				mostrarErrorNoExisteUltimocierre(forma, request);
			}
		}else {
			mostrarErrorNoExisteUltimocierre(forma, request);
		}
		
		UtilidadTransaccion.getTransaccion().commit();
		
		return mapping.findForward("principal");
	}
	

	
	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, TurnoDeCajaForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		
		/**
		 * Inc 2191
		 * Se debe validar que dos usuarios no tomen la misma caja
		 * Al terminar el proceso de apertura de caja o salir de la funcionalidad se debe
		 * actualizar es estado, para que en caso de que no se tenga apertura de caja esta
		 * sea liberada para que otro usuario pueda hacer la apertura de turno				
		 * Diana Ruiz 
		 */
		try {
			UtilidadTransaccion.getTransaccion().begin();
			
			if (forma.getCajaSeleccionada() != null){
				TurnoDeCajaMundo turnoCaja = new TurnoDeCajaMundo();									
				ArrayList<CajasCajeros> listaCajaCajero = new ArrayList<CajasCajeros>();		
				listaCajaCajero = turnoCaja.obtenerTodasCajasCajero(forma.getCajaSeleccionada().getConsecutivo());
								
				for (CajasCajeros cajasCajeros : listaCajaCajero) {	
					if (cajasCajeros.getUsuarios().getLogin().equals(usuario.getLoginUsuario())){						
						cajasCajeros.setAperturaTurno("");				
						turnoCaja.merge(cajasCajeros);
						UtilidadTransaccion.getTransaccion().commit();
						break;						
					}
				}			
			}		
			
			UtilidadTransaccion.getTransaccion().begin();		
			forma.resetMensajes();			
			forma.reset();
			forma.setCajaSeleccionada(null);
			this.dtoApto = null;
			
			if(cajasCajeroServicio.validarUsuarioEsCajero(usuario))
			{ 
				int numeroCajas=llenarListaCajasActivasCajero(forma, usuario, request);
				if(numeroCajas==1)
				{
					forma.setCajaSeleccionada(forma.getListaCajas().get(0));
					return accionNuevaApertura(mapping, forma, usuario, request);
				}
			}
			else 
			{
				mostrarErrorPermisosUsuario(forma, request, usuario);
			}	
			
			UtilidadTransaccion.getTransaccion().commit();		
		
		} catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			e.printStackTrace();
		}		
		
		return mapping.findForward("principal");
	}	
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private ActionForward accionNuevaApertura(ActionMapping mapping, TurnoDeCajaForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		// consecutivo faltante sobrante
		
		
		try {
			
			UtilidadTransaccion.getTransaccion().begin();
			
			/**
			 * Inc 2191
			 * Se debe validar que dos usuarios no tomen la misma caja
			 * Diana Ruiz
			 */
			TurnoDeCajaMundo turnoCaja = new TurnoDeCajaMundo();			
			ArrayList<CajasCajeros> listaCajaCajero = new ArrayList<CajasCajeros>();	
			listaCajaCajero = turnoCaja.obtenerTodasCajasCajero(forma.getCajaSeleccionada().getConsecutivo());	
			boolean cajaEnApertura=false;
						
			/**
			 * Validar si la caja seleccionada se encuentra en proceso de apertura de turno
			 */		
			
			for (CajasCajeros cajasCajeros : listaCajaCajero) {	
				if (!UtilidadTexto.isEmpty(cajasCajeros.getAperturaTurno())){
					cajaEnApertura=true;
				}
			}
			
			if (cajaEnApertura==false){
				
				for (CajasCajeros cajasCajeros : listaCajaCajero) {					
						/**
						 * verifico si la caja validada es la del usuario en sesi&oacute;n 
						 */
						if (cajasCajeros.getUsuarios().getLogin().equals(usuario.getLoginUsuario())){					
							
							cajasCajeros.setAperturaTurno(ConstantesBD.estadoAperturaTurno);
							turnoCaja.merge(cajasCajeros);				
							UtilidadTransaccion.getTransaccion().commit();
							
							UtilidadTransaccion.getTransaccion().begin();
							
							DtoParametros dtoParametros = new DtoParametros();
							dtoParametros= movimientosCajaServicio.validarDefinicionesParametrosAperturaTurno(usuario);
							
							if(!dtoParametros.isTieneDefinidoConsecutivoFaltanteSobrante())
							{
								forma.setMostrarMensajeConsecutivoFaltanteSobrante(true);
							}
							
							
							if(forma.isLimpiarAceptaciones() &&  forma.isTodasSolicitudesAceptadasTemporalmente() == false)
							{
								limpiarForma(forma);
							}
							forma.reset();
							
							if(forma.getCajaSeleccionada() != null)
							{
								if(this.dtoApto == null)
								{
									dtoApto= new DtoTurnoDeCajaApta();
									dtoApto = movimientosCajaServicio.esCajaAptaParaApertura(usuario, forma.getCajaSeleccionada());
								}
								habilitarAccionesCajaApta(forma, this.dtoApto, usuario, request);
								UtilidadTransaccion.getTransaccion().commit();
								break;
							}
							else
							{
								forma.reset();
								dtoApto= null;
								forma.resetSolicitudesPendientes();
								UtilidadTransaccion.getTransaccion().rollback();
								mostrarErrorCaja(forma, request);
								break;
							}					
						}
										
					}							
			  }	
			else {
				mostrarProcesoAperturaCaja(forma, request);				
				UtilidadTransaccion.getTransaccion().rollback();			
			}
		  
		} catch (Exception e) {			
			UtilidadTransaccion.getTransaccion().rollback();
			e.printStackTrace();
		}
		
		
		llenarListaCajasActivasCajero(forma, usuario, request);		
		return mapping.findForward("principal");
	
	}
	
	
	/**
	 * Limpia las aceptaciones de la forma
	 * @param forma
	 */
	private void limpiarForma(TurnoDeCajaForm forma)
	{
		forma.resetSolicitudesPendientes();
	}
	
	
	/**
	 * @param mapping
	 * @param forma
	 * @param request // Tiene solicitudes pendientes
	 * @param usuario
	 * @return
	 */
	private ActionForward accionRealizarApertura(ActionMapping mapping,	TurnoDeCajaForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		// FIXME Llamar al proceso de validacon de contrasenia - ANEXO 846 pag 5 
			// Toca validar el id del testigo, si existe o no para que este ingrese su contrasenia
		guardarTurno(mapping, forma, usuario,request);	
		llenarListaCajasActivasCajero(forma, usuario, request);
		
		/**
		 * Inc 2191
		 * Se debe validar que dos usuarios no tomen la misma caja
		 * Diana Ruiz
		 */
		try {
			UtilidadTransaccion.getTransaccion().begin();
			
			/**
			 * Al terminar el proceso de apertura de caja o salir de la funcionalidad se debe
			 * actualizar es estado, para que en caso de que no se tenga apertura de caja esta
			 * sea liberada para que otro usuario pueda hacer la apertura de turno
			 */		
			
			TurnoDeCajaMundo turnoCaja = new TurnoDeCajaMundo();			
			ArrayList<CajasCajeros> listaCajaCajero = new ArrayList<CajasCajeros>();		
			listaCajaCajero = turnoCaja.obtenerTodasCajasCajero(forma.getCajaSeleccionada().getConsecutivo());			
			
			for (CajasCajeros cajasCajeros : listaCajaCajero) {		
				
				if(cajasCajeros.getUsuarios().getLogin().equals(usuario.getLoginUsuario())){						
					cajasCajeros.setAperturaTurno("");	
					turnoCaja.merge(cajasCajeros);				
					UtilidadTransaccion.getTransaccion().commit();
					break;
				}
			}		
						
		} catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			e.printStackTrace();
		}	
			
		return mapping.findForward("principal");
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @param request
	 * @return
	 */
	private void guardarTurno (ActionMapping mapping, TurnoDeCajaForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		if(forma.getDtoInformacionEntrega() == null){
			forma.resetSolicitudesPendientes();
		}
		
		UtilidadTransaccion.getTransaccion().begin();
		
		// -- En el caso de que sea una sola solicitud se debe limpiar la lista para no guardar registros duplicados
		Log4JManager.info(">>>>>>>>>>>>>>>>> Cantidad de solicitudes pendientes cargadas: "+forma.getListaSolicitudesPendientes().size());
		
		ArrayList<Long> codigosSolicitudes = new ArrayList<Long>();
		
		if(forma.getListaSolicitudesPendientes().size() == 1)
		{
			DtoInformacionEntrega infoEntrega = new DtoInformacionEntrega();
			infoEntrega = forma.getListaDtoInformacionEntrega().get(0);
			forma.getListaDtoInformacionEntrega().clear();
			forma.getListaDtoInformacionEntrega().add(infoEntrega);
			
			codigosSolicitudes.add(infoEntrega.getIdMovimientoCaja());
			
		}else{
			
			for (DtoInformacionEntrega infoEntrega : forma.getListaDtoInformacionEntrega()) {
				
				codigosSolicitudes.add(infoEntrega.getIdMovimientoCaja());
			}
		}
		// -- -------------------------------
		
		forma.getDtoInformacionEntrega().setInstitucionActual(usuario.getCodigoInstitucionInt());
			
		if(movimientosCajaServicio.guardarTurno(forma.getDto(), forma.getListaDtoInformacionEntrega()))
		{
			UtilidadTransaccion.getTransaccion().commit();
			forma.reset();
			forma.setEstado("resumen");
		}
		else
		{
			UtilidadTransaccion.getTransaccion().rollback();
			mostrarErroOperacionNoRealizada(forma, request);
		}
		
	}
	
	
	
	
	/*------------------------------*/
	/* METODOS VARIOS 				*/
	/*------------------------------*/
	
	
	/**
	 * Cambia el comportamiento de la forma dependiendo de los 
	 * turnos de cajero y las solicitudes de traslado de caja
	 * @param forma
	 * @param dtoApto
	 * @param usuario
	 */
	private void habilitarAccionesCajaApta(TurnoDeCajaForm forma, DtoTurnoDeCajaApta dtoApto, UsuarioBasico usuario, HttpServletRequest request) 
	{
		if(dtoApto.isCompletamenteApto()){				// No existe solicitud y el turno de la caja/cajero esta cerrado
			habilitarAbrirTurno(forma, request);
		}
		else {
			forma.setMostrarBotonConfirmar(false);
			if(!dtoApto.isTieneTurnoCajeroCerrado()){	// Tiene un turno abierto cajero
				tieneTurnoCajero(forma, usuario, request);
			}
			else{
				if(!dtoApto.isTieneTurnoCajaCerrado()){	// Tiene un turno abierto caja
					tieneTurnoCaja(forma, usuario, request);
				}
				else{									// Tiene solicitudes pendientes
					tieneSolicitud(forma, dtoApto);
				}
			}
		}
	}
	
	
	
	/**
	 * Habilita las condiciones para la presentacion
	 * @param forma
	 */
	private void habilitarAbrirTurno(TurnoDeCajaForm forma, HttpServletRequest request)
	{
		iniciarTurno(forma, request);
		forma.setMostrarFormularioIngreso(true);
		forma.setMostrarBotonConfirmar(true);
	}
	
	
	/**
	 * Llama el mensaje que indica que el cajero ya tiene un turno abierto
	 * @param forma
	 */
	private void tieneTurnoCajero(TurnoDeCajaForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		forma.reset();
		dtoApto= null;
		forma.resetSolicitudesPendientes();
		mostrarErrorTurnosAbiertosCajero(forma, request, usuario);
	}
	
	
	/**
	 * Llama el mensaje que indica que la caja ya tiene un turno abierto
	 * @param forma
	 */
	private void tieneTurnoCaja(TurnoDeCajaForm forma, UsuarioBasico usuario, HttpServletRequest request)
	{
		forma.reset();
		dtoApto= null;
		forma.resetSolicitudesPendientes();
		mostrarErrorTurnosAbiertosCaja(forma, request, usuario);
	}
	
	
	
	/**
	 * La caja tiene solicitudes pendientes por aceptar
	 * 
	 * @param forma
	 * @param dtoApto
	 */
	private void tieneSolicitud(TurnoDeCajaForm forma, DtoTurnoDeCajaApta dtoApto)
	{
		forma.setMostrarMensajeSolicitudes(true);
	
		UtilidadTransaccion.getTransaccion().begin();
		
		if(Utilidades.isEmpty(forma.getListaSolicitudesPendientes()))
		{
			forma.setListaSolicitudesPendientes(movimientosCajaServicio.obtenerSolicitudesPendientes(forma.getCajaSeleccionada()));
		}
		else
		{
			boolean aceptadasTemporalmente = true;
			
			for (DtoSolicitudTrasladoPendiente solicitud : forma.getListaSolicitudesPendientes()) 
			{
				if(solicitud.isAceptadoTemporalmente() == false){
					aceptadasTemporalmente = false;
				}
				
			}

			/* Si todas las cajas han sido aceptadas temporalmente
			 	se dice que la caja no tiene solicitudes de traslado  */
			if(aceptadasTemporalmente == true){
				dtoApto.setTieneSolicitudDeTraslado(false);
				forma.setTodasSolicitudesAceptadasTemporalmente(true);
				this.dtoApto.setTieneSolicitudDeTraslado(false);
			}
		}
		
		UtilidadTransaccion.getTransaccion().commit();
	}
	
	
	/**
	 * Inc 2191: Dos cajeros no puedes acceder a la misma caja al tiempo
	 * Libera la caja cuando se encuentra en proceso de apertura y este no se lleva a cabo exitosamente
	 * saliendose de la p&aacute;gina o cerrando el explorador
	 * @author Diana Ruiz
	 * 
	 */
	
	
	public void cancelarAperturaTurnoCaja(UsuarioBasico usuario){
		
		try {
			UtilidadTransaccion.getTransaccion().begin();
			TurnoDeCajaMundo turnoCaja = new TurnoDeCajaMundo();									
			ArrayList<CajasCajeros> listaCajaCajero = new ArrayList<CajasCajeros>();		
			listaCajaCajero = turnoCaja.obtenerCajasCajero(usuario);
			
			for (CajasCajeros cajasCajeros : listaCajaCajero) {	
				if (!UtilidadTexto.isEmpty(cajasCajeros.getAperturaTurno())){
					if (cajasCajeros.getAperturaTurno().equals(ConstantesBD.estadoAperturaTurno)){	
						cajasCajeros.setAperturaTurno("");				
						turnoCaja.merge(cajasCajeros);
						UtilidadTransaccion.getTransaccion().commit();
						break;
					}					
				}								
			}		
		} catch (Exception e) {
			UtilidadTransaccion.getTransaccion().rollback();
			e.printStackTrace();
		}	
	}
	
	
	
	/*------------------------------*/
	/* METODOS DE INICIACION		*/
	/*------------------------------*/
		
	/** 
	 * Carga una lista de artefactos necesarios en la forma
	 * @param usuario
	 * @param forma
	 * @param request
	 * @return N&uacute;mero de cajas encontradas 
	 */
	private int llenarListaCajasActivasCajero(TurnoDeCajaForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		
		/*
		ArrayList<Cajas> listaCajas = cajasServicio
			.listarCajasPorCajeroActivasXInstitucionXCentroAtencion(usuario, ConstantesBD.codigoTipoCajaRecaudado);
		*/
		
		ArrayList<Cajas> listaCajas = cajasServicio.listarCajasPorCajeroActivasXInstitucionXCentroAtencionTurno(usuario, ConstantesBD.codigoTipoCajaRecaudado, 
				ConstantesIntegridadDominio.acronimoEstadoAperturaDeCajaAbierto);
		
		if(listaCajas == null)
		{
			/* - */ //mostrarErrorCaja(forma, request);
			/* + */ mostrarErrorUsuarioNoRegCaja(forma, request, usuario);
		}
		else
		{
			forma.setListaCajas(listaCajas);
			listaCajas = null;
			return forma.getListaCajas().size();
		}
		UtilidadTransaccion.getTransaccion().commit();
		return 0;
	}
	
	
	/**
	 * Inicializa el dto con los valores a mostrar
	 * @param forma
	 */
	private void iniciarTurno(TurnoDeCajaForm forma, HttpServletRequest request)
	{
		forma.setDto(TurnoDeCajaHelper.iniciarTurno(forma.getCajaSeleccionada(), request.getSession(false)));
	}
	
	
	/**
	 * Método que se encarga de validar que no exista el mismo regisgtro
	 * de Solicitud de Traslado a Caja de Recaudo para su aceptación.
	 * 
	 * @param forma
	 */
	private void verificarListadoSolicitudes(TurnoDeCajaForm forma) {
		
		boolean existe= false;
		if(forma.getListaDtoInformacionEntrega() == null){
			forma.setListaDtoInformacionEntrega(new ArrayList<DtoInformacionEntrega>());
		}
		
		long codigoSolicitud = forma.getListaSolicitudesPendientes().get(forma.getPosListaSolPendiente()).getMovimientoCaja();
		
		for (DtoInformacionEntrega informacionEntrega : forma.getListaDtoInformacionEntrega()) {
				
			if(informacionEntrega.getIdMovimientoCaja() == codigoSolicitud ){
				
				existe = true;
				break;
			}
		}
		
		if(!existe){
			
			forma.getDtoInformacionEntrega().setIdMovimientoCaja(codigoSolicitud);
			forma.getListaDtoInformacionEntrega().add(forma.getDtoInformacionEntrega());
		}
	}
	
	
	/*--------------------------------------*/
	/* METODOS DE MENSAJES DE ERRORE 		*/
	/*--------------------------------------*/
	
	/**
	 * Si no se ha seleccionado la caja, muestra una pagina de error
	 * @param forma
	 * @param request
	 */
	private void mostrarErrorCaja(TurnoDeCajaForm forma, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error caja_no_seleccionada", new ActionMessage("errors.faltaDefinirCajaParaUsuario"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}

	
	/**
	 * Si no se ha seleccionado la caja, muestra una pagina de error
	 * @param forma
	 * @param request
	 */
	private void mostrarErrorUsuarioNoRegCaja(TurnoDeCajaForm forma, HttpServletRequest request, UsuarioBasico usuario) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error caja_no_seleccionada", new ActionMessage("errores.modTesoreria.usuNoRegCaja", usuario.getLoginUsuario()));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	
	
	/**
	 * Si el usuario no tiene permisos para realizar una apertura, muestra una pagina de error
	 * @param forma
	 * @param request
	 */
	private void mostrarErrorPermisosUsuario(TurnoDeCajaForm forma, HttpServletRequest request, UsuarioBasico usuario) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error permisos_usuario_egreso", new ActionMessage("errors.usuarioSinRolFuncionalidad",usuario.getLoginUsuario(),"Apertura de Caja")); 
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	
	/**
	 * Si el usuario tiene otros turnos de caja abiertos, muestra una pagina de error
	 * @param forma
	 * @param request
	 * @param usuario
	 */
	private void mostrarErrorTurnosAbiertosCajero(TurnoDeCajaForm forma, HttpServletRequest request, UsuarioBasico usuario) 
	{
		ActionErrors errores = new ActionErrors();
		UtilidadTransaccion.getTransaccion().begin();
		
		
		String centroAtencionTurnoAbierto = "";
		int ultimoRegistro = 0;
		
		TurnoDeCaja turnoAbierto;
		turnoAbierto = new TurnoDeCaja();
		
		ArrayList<TurnoDeCaja> listaTurnos = new ArrayList<TurnoDeCaja>();
		listaTurnos = turnoCajaServicio.obtenerTurnoCajaAbiertoPorCajero(usuario);
		
		ultimoRegistro = listaTurnos.size() -1;
		turnoAbierto = listaTurnos.get(ultimoRegistro); 
		centroAtencionTurnoAbierto = turnoAbierto.getCentroAtencion().getDescripcion();
		
		UtilidadTransaccion.getTransaccion().commit();
		
		if (listaTurnos.isEmpty()) {
			errores.add("error_turno_ya_abierto", new ActionMessage("error.ingresoTurno.repetido", 
					"El turno de la caja ("+ forma.getCajaSeleccionada().getDescripcion() +"), Centro de Atencion (" +centroAtencionTurnoAbierto+") "));
		} else {
			errores.add("error cajero_con_turno_ya_abierto", new ActionMessage("error.cajero.turno",centroAtencionTurnoAbierto));
		}
		saveErrors(request, errores);
		
		forma.setEstado("empezar"); 
	}
	
	
	/**
	 * Si el usuario tiene otros turnos de caja abiertos, muestra una pagina de error
	 * @param forma
	 * @param request
	 * @param usuario
	 */
	private void mostrarErrorTurnosAbiertosCaja(TurnoDeCajaForm forma, HttpServletRequest request, UsuarioBasico usuario) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error turno_ya_abierto", new ActionMessage("errores.modTesoreria.CajaAbierta", 
				forma.getCajaSeleccionada().getDescripcion(), 
				forma.getCajaSeleccionada().getInstituciones().getRazonSocial())
		);
		
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	
	/**
	 * Si no existe un consolidado de cierre anterior indica con un mensaje  
	 * @param forma
	 * @param request
	 */
	private void mostrarErrorNoExisteUltimocierre(TurnoDeCajaForm forma, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error no_existe_consolidado", new ActionMessage("error.arqueos.consolidadoCierre"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	
	/**
	 * Error generico que indica que no se pudo realizar la opreacion
	 * @param forma
	 * @param request
	 */
	private void mostrarErroOperacionNoRealizada(TurnoDeCajaForm forma, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error operacion_no_realizada", new ActionMessage("errors.procesoNoExitoso"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	
	
	/**
	 * Error generico que indica que el testigo es requerido
	 * @param forma
	 * @param request
	 */
	private void mostrarRequiereTestigoRequerido(TurnoDeCajaForm forma, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error operacion_no_realizada", new ActionMessage("errors.required","El Testigo"));
		saveErrors(request, errores);
		forma.setEstado("continuarProceso");
	}
	
	
	/**
	 * Indica que valor o parametro falta por definir
	 * @param forma
	 * @param request
	 */
	private void mostrarErrorFaltaDefinir(TurnoDeCajaForm forma, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error operacion_no_realizada", new ActionMessage("errores.modTesoreria.FaltaDefinirConsecutivo","Detalle Faltante/Sobrante","Apertura de Caja"));
		saveErrors(request, errores);
		forma.setEstado("continuarProceso");
	}
	
	
	/**
	 * Si no se ha seleccionado la caja, muestra una pagina de error
	 * @param forma
	 * @param request
	 */
	private void mostrarProcesoAperturaCaja(TurnoDeCajaForm forma, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores.add("error proceso_apertura_turno", new ActionMessage("errors.proceso_apertura_turno"));
		saveErrors(request, errores);
		forma.setEstado("empezar");
	}
	
	
	
}
