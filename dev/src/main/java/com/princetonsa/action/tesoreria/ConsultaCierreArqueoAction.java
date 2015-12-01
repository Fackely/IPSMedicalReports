package com.princetonsa.action.tesoreria;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.tesoreria.ConsultaCierreArqueoForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.tesoreria.DtoBusquedaCierreArqueo;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladosCajasRecaudo;
import com.princetonsa.dto.tesoreria.DtoCuadreCaja;
import com.princetonsa.dto.tesoreria.DtoEntregaCaja;
import com.princetonsa.dto.tesoreria.DtoFormaPagoDocSoporte;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.princetonsa.dto.tesoreria.DtoMovimientosCajaEntregaParcial;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.sort.odontologia.SortGenerico;
import com.servinte.axioma.generadorReporte.tesoreria.impresionArqueoCaja.GeneradorReporteArqueoCajaResumido;
import com.servinte.axioma.generadorReporte.tesoreria.impresionCierreTurnoCaja.GeneradorReporteCierreTurnoCajaDetallado;
import com.servinte.axioma.generadorReporte.tesoreria.impresionCierreTurnoCaja.GeneradorReporteCierreTurnoCajaResumido;
import com.servinte.axioma.generadorReporte.tesoreria.impresionEntregaATransportadora.GeneradorReporteEntregaTransportadora;
import com.servinte.axioma.generadorReporte.tesoreria.impresionEntregaCajaMayorPrincipal.GeneradorReporteEntregaCajaMayorPrincDetallado;
import com.servinte.axioma.generadorReporte.tesoreria.impresionEntregaCajaMayorPrincipal.GeneradorReporteEntregaCajaMayorPrincResumido;
import com.servinte.axioma.generadorReporte.tesoreria.impresionTrasladoEntreCajas.GeneradorReporteTrasladoEntreCajas;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaArqueoCierreCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo;
import com.servinte.axioma.orm.EntregaCajaMayor;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.TiposMovimientoCaja;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IConsultaArqueoCierreCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ISolicitudTrasladoCajaServicio;

/**
 * Action que maneja todo lo relacionado con el Anexo 234 - Consulta Cierrea Arqueos.
 * 
 * Los procesos que son comunes entre los Arqueos (Arqueo caja, Arqueo Entrega parcial 
 * y Cierre Turno de Caja) se encuentran definidos en ComunMovimientosCaja y en MovimientosCajaAction
 *
 * @author Jorge Armando Agudelo Quintero 
 * @see ComunMovimientosCaja
 * @see MovimientosCajaAction
 */

public class ConsultaCierreArqueoAction extends Action{

	/**
	 * M&eacute;todo que se encarga de procesar las peticiones realizadas desde la opci&oacute;n
	 * Consultar Arqueos Caja
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 *
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {

		if(form instanceof ConsultaCierreArqueoForm)
		{
			ActionErrors errores=new ActionErrors();
			
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			

			ConsultaCierreArqueoForm forma=(ConsultaCierreArqueoForm) form;
			
			InstitucionBasica institucionBasica=(InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			if(institucionBasica!=null){
				
				forma.setInstitucionBasica(institucionBasica);
			}
			
			String estado=forma.getEstado();
			
			Log4JManager.info("Estado en consulta cierre arqueos action: "+estado);
		
			forma.setListaNombresReportes(new ArrayList<String>());
			
			/*
			 * El estado "generarArqueo" es el siguiente una vez se ingresa a la opci&oacute;n 
			 * Arqueo Caja, aca se realiza una consulta de todos los movimientos registrados para
			 * la caja en un turno espec&iacute;fico y que intervienen en el proceso de arqueo.
			 */
			if(estado.equals("empezar")){
				
				ComunMovimientosCaja.accionEmpezar(forma, request,usuario.getCodigoInstitucionInt(),usuario.getCodigoCentroAtencion());
				
				return mapping.findForward("principalBusqueda");
				
			}else if(estado.equals("selectCajero")){
				
				ComunMovimientosCaja.consultarCajerosPorCajaSeleccionada(forma, request,usuario.getCodigoInstitucionInt(),usuario.getCodigoCentroAtencion());
				
				if(!errores.isEmpty()){
					
					saveErrors(request, errores);
				}
				
				return mapping.findForward("principalBusqueda");

			}else if(estado.equals("consultar")){
				
				return mapping.findForward(consultarCierreArqueo(forma));
				
			}else if(estado.equals("cargarCierreArqueo")){
				
				Log4JManager.info("consecutivo a consultar " + forma.getConsecutivoMovimientoConsultar());
				
				Log4JManager.info("consecutivo a consultar " + forma.getTipoArqueo());
				
				forma.setConsulta(true);
				
				cargarCierreArqueo (forma, usuario.getCodigoInstitucionInt());
				
				return mapping.findForward("resultadoSeleccionCierreArqueo");
				
			}else if(estado.equals("volver")){
				
				forma.reset();
			
				ComunMovimientosCaja.accionEmpezar(forma, request,usuario.getCodigoInstitucionInt(),usuario.getCodigoCentroAtencion());
				
				return mapping.findForward("principalBusqueda");
				
			}else if(estado.equals("volverListadoRegistros")){
	
				return mapping.findForward("resultadoBusqueda");
				
			}
			else if(estado.equals("impresion")){
				ComunMovimientosCaja.asignarTipoFuncionalidad(forma);
				
				if(forma.getTipoFuncionalidad()==ConstantesBD.codigoFuncionalidadTipoArqueoCaja){
					return mapping.findForward("impresionArqueoCaja");
				}else if(forma.getTipoFuncionalidad()==ConstantesBD.codigoFuncionalidadTipoArqueoEntregaParcial){
					return mapping.findForward("impresionArqueoEntregaParcial");
				}else if (forma.getTipoFuncionalidad()==ConstantesBD.codigoFuncionalidadTipoArqueoCierreTurno){
					return mapping.findForward("impresionCierreTurnoCaja");
				}
				
			}
			else if(estado.equals("ordenar")){
				accionOrdenar(forma);
				return mapping.findForward("resultadoBusqueda");
			}
			else if(estado.equals("mostrarPopUpTipoReporte")){
				
				String[] listadoTipoRep = new String[]{ConstantesIntegridadDominio.acronimoTipoReporteImpCierreTurnoResumido,
						ConstantesIntegridadDominio.acronimoTipoReporteImpCierreTurnoDetallado,
						ConstantesIntegridadDominio.acronimoTipoReporteAmbos};
				this.cargarTiposReporte(forma,listadoTipoRep);
				
				return mapping.findForward("popUpTipoReporte");
			}
			else if(estado.equals("mostrarPopUpTipoReporteTrasladoEntrega")){
				
				String[] listadoTipoRep = new String[]{ConstantesIntegridadDominio.acronimoTipoReporteImpEntregaCajaMayPrincResumido,
						ConstantesIntegridadDominio.acronimoTipoReporteImpEntregaCajaMayPrincDetallado,
						ConstantesIntegridadDominio.acronimoTipoReporteAmbos};
				this.cargarTiposReporte(forma,listadoTipoRep);
				consultaImpresionEntregaCajaMayor(forma, institucionBasica);
				
				return mapping.findForward("popUpTipoReporte");
			}
			else if(estado.equals("impresionTipoFormato")){
				ComunMovimientosCaja.asignarTipoFuncionalidad(forma);
				
				if (forma.getTipoFuncionalidad()==ConstantesBD.codigoFuncionalidadTipoArqueoCierreTurno){
					ArrayList<String> listaTipRep=new ArrayList<String>();
					for(DtoIntegridadDominio dto:forma.getListadoTiposReporte())
					{
						listaTipRep.add(dto.getAcronimo());
					}
					
					if(listaTipRep.contains(ConstantesIntegridadDominio.acronimoTipoReporteImpCierreTurnoResumido))
					{
						this.imprimirCierreTurnoCaja(forma, request, usuario, institucionBasica);
					}else
					{
						this.imprimirEntregaCajaMayPrinc(forma, request, usuario, institucionBasica);
					}
					
				}
				else if (forma.getTipoFuncionalidad()==ConstantesBD.codigoFuncionalidadTipoArqueoCaja) {
					
					this.imprimirArqueoCaja(forma, request, usuario, institucionBasica);
					
				} else if(forma.getTipoFuncionalidad()==ConstantesBD.codigoFuncionalidadTipoArqueoEntregaParcial) {
					
					this.imprimirEntregaCajaMayPrincipal(forma, request, usuario, institucionBasica);
					
				}
				return mapping.findForward("resultadoSeleccionCierreArqueo");
			}
			else if(estado.equals("impresionTrasladoEntreCajas")){
				try{
					HibernateUtil.beginTransaction();
					this.consultaImpresionSolicitudTrasladoCajaRecaudo(forma, usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
					this.imprimirSolicitudTrasladoEntreCajas(forma, request, usuario, institucionBasica);
					HibernateUtil.endTransaction();
				}
				catch (Exception e) {
					Log4JManager.error(e);
					HibernateUtil.abortTransaction();
				}
				return mapping.findForward("resultadoSeleccionCierreArqueo");
			}
			else if(estado.equals("impresionTransportadoraValores")){
				this.imprimirEntregaTransportadora(forma, request, usuario, institucionBasica);
				return mapping.findForward("resultadoSeleccionCierreArqueo");
			}
			else if(estado.equals("mostrarPopUpTipoReporteEntregaCajaMayor")){
				String[] listadoTipoRep = new String[]{ConstantesIntegridadDominio.acronimoTipoReporteImpEntregaCajaMayPrincResumido,
						ConstantesIntegridadDominio.acronimoTipoReporteImpEntregaCajaMayPrincDetallado,
						ConstantesIntegridadDominio.acronimoTipoReporteAmbos};
				this.cargarTiposReporte(forma,listadoTipoRep);
				
				return mapping.findForward("popUpTipoReporte");
			}
			else if(estado.equals("mostrarPopUpDocSoporte")){
				return mapping.findForward("popUpDocSoporte");
			}
			else{ //secci&oacute;n de errores
				
				errores.add("estado invalido", new ActionMessage("errors.estadoInvalido"));
				errores.add("valor requerido", new ActionMessage("errors.usuarioSinRolFuncionalidad",usuario.getNombreUsuario(), "esta funcionalidad"));
				saveErrors(request, errores);
			}
		}
		
		return mapping.findForward("paginaError");
	}
	
	/**
	 * M&eacute;todo que se encarga de imprimir la Entrega a Caja Mayor generada en el proceso de cierre.
	 * 
	 * @param forma
	 * @param codigoInstitucion
	 */
	private void consultaImpresionEntregaCajaMayor(ConsultaCierreArqueoForm forma, InstitucionBasica ins) {
		
		//CONSULTAR LA INFORMACION
		try{
			HibernateUtil.beginTransaction();
			IConsultaArqueoCierreCajaMundo consultaArqueoCierreCajaMundo = TesoreriaFabricaMundo.crearConsultaArqueoCierreCajaMundo();
			
			EntregaCajaMayor entregaCajaMayor = forma.getCierreTurnoCaja().getEntregaCajaMayorByCodigoPk();
			
			if(entregaCajaMayor!=null && entregaCajaMayor.getMovimientosCajaByCodigoPk().getCodigoPk() > 0){
				
				long codigoEntrega = entregaCajaMayor.getMovimientosCajaByCodigoPk().getCodigoPk();
				
				DtoInformacionEntrega dtoInfo=consultaArqueoCierreCajaMundo.consultarArqueoParcialPorEntrega(codigoEntrega, ETipoMovimiento.ENTREGA_CAJA_MAYOR_PRINCIPAL, ins.getCodigoInstitucionBasica());
				
				forma.getDtoInformacionEntrega().setListadoDtoFormaPagoDocSoportes(dtoInfo.getListadoDtoFormaPagoDocSoportes());
				
				recargarTotalesPorFormasPago(forma);
				forma.setConsulta(true);
				forma.setEstadoGuardarMovimiento("EntregaCajaMayorPrincipal");
			
			}else if(forma.getDtoInformacionEntrega()!=null){
				
				forma.getDtoInformacionEntrega().setListadoDtoFormaPagoDocSoportes(new ArrayList<DtoFormaPagoDocSoporte>());
			}
			
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR consultaImpresionEntregaCajaMayor",e);
			HibernateUtil.abortTransaction();
		}
	}
	
	/**
	 * M&eacute;todo que se encarga de realizar la impresi&oacute;n de la Solicitud de Traslado a Caja de Recaudo
	 * asociada al movimiento de Cierre de Turno de Caja.
	 * 
	 * @param forma
	 * @param codigoInstitucion
	 * @param consecutivoCentroAtencion
	 */
	private void consultaImpresionSolicitudTrasladoCajaRecaudo(ConsultaCierreArqueoForm forma, int codigoInstitucion, int consecutivoCentroAtencion) {
		ISolicitudTrasladoCajaServicio solicitudTrasladoServicio = TesoreriaFabricaServicio.crearSolicitudTrasladoCaja();
		IMovimientosCajaServicio movimientosCajaServicio 	= TesoreriaFabricaServicio.crearMovimientosCajaServicio();

		if(forma.getCierreTurnoCaja().getSolicitudTrasladoCajaByCodigoPk()!=null){
			
			long consecutivoSolicitud = forma.getCierreTurnoCaja().getSolicitudTrasladoCajaByCodigoPk().getMovimientosCajaByCodigoPk().getConsecutivo();
			
			Log4JManager.info("------------------ consecutivo solicitud:-" + consecutivoSolicitud);
			
			DtoConsultaTrasladosCajasRecaudo dtoConsultaTrasladosCajasRecaudo = new DtoConsultaTrasladosCajasRecaudo();
			dtoConsultaTrasladosCajasRecaudo.setConsecutivoSolicitud(consecutivoSolicitud);
			
			ArrayList<DtoConsultaTrasladosCajasRecaudo> listaSolicitud = solicitudTrasladoServicio.consultarRegistrosSolicitudTrasladoCaja(dtoConsultaTrasladosCajasRecaudo);
			
			if(listaSolicitud!=null && listaSolicitud.size()>0){
				
				dtoConsultaTrasladosCajasRecaudo = listaSolicitud.get(0);
				forma.setDtoConsultaTrasladosCajasRecaudo(dtoConsultaTrasladosCajasRecaudo);
				
				DtoInformacionEntrega dtoInformacionEntrega = movimientosCajaServicio.consolidarInformacionSolicitudTrasCajaRealizada(codigoInstitucion, dtoConsultaTrasladosCajasRecaudo.getCodigoPk());
				
				Log4JManager.info("------------------ tamaño del listado de la solicitud:- " + dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes().size());
				
				forma.getDtoInformacionEntrega().setCuadreCajaDTOs(dtoInformacionEntrega.getCuadreCajaDTOs());
				forma.getDtoInformacionEntrega().setListadoDtoFormaPagoDocSoportes(dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes());
				forma.getDtoInformacionEntrega().setTotalEntregadoTransportadora(dtoInformacionEntrega.getTotalEntregadoTransportadora());
//				
//				if(dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes().size() > 0){
//					
//					recargarTotalesPorFormasPago(forma);
//					
					forma.setConsulta(true);
					
//				}else{
//					
//					forma.getDtoInformacionEntrega().getListadoDtoFormaPagoDocSoportes().clear();
//				}
			}
		}else if(forma.getDtoInformacionEntrega()!=null){
			
			forma.getDtoInformacionEntrega().setListadoDtoFormaPagoDocSoportes(new ArrayList<DtoFormaPagoDocSoporte>());
		}
	}
	
	/**
	 * Recalcula los valores por forma de pago que se realizaron en la
	 * Entrega a Caja Mayor / Principal en el movimiento de Cierre de Turno de Caja
	 * 
	 * @param forma
	 */
	private void recargarTotalesPorFormasPago (ConsultaCierreArqueoForm forma) {
		
//		forma.getConsolidadoMovimientoDTO().getCuadreCajaDTOs().clear();
//		
//		for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte : forma.getDtoInformacionEntrega().getListadoDtoFormaPagoDocSoportes()) {
//		
//			if(ConstantesBD.acronimoSi.equalsIgnoreCase(dtoFormaPagoDocSoporte.getSeleccionado())){
//
//				forma.getConsolidadoMovimientoDTO().getCuadreCajaDTOs().add(dtoFormaPagoDocSoporte.getDtoCuadreCaja());
//			}
//		}
		
		forma.getDtoInformacionEntrega().getCuadreCajaDTOs().clear();
		
		for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte : forma.getDtoInformacionEntrega().getListadoDtoFormaPagoDocSoportes()) {
		
			if(ConstantesBD.acronimoSi.equalsIgnoreCase(dtoFormaPagoDocSoporte.getSeleccionado())){

				forma.getDtoInformacionEntrega().getCuadreCajaDTOs().add(dtoFormaPagoDocSoporte.getDtoCuadreCaja());
			}
		}
	}
	
	
	/**
	 * Método que se encarga de recopilar la información seleccionada por el usuario
	 * y de realizar la respectiva búsqueda de los movimientos de caja de tipo Arqueo, Arqueo Entrega
	 * Parcial y de Cierre de Turno de Caja.
	 * 
	 * @param forma
	 * @return 
	 */
	private String consultarCierreArqueo(ConsultaCierreArqueoForm forma) {
		
		DtoBusquedaCierreArqueo dtoBusquedaCierreArqueo = new DtoBusquedaCierreArqueo();
		
		if(forma.getCajero()!=null){
			dtoBusquedaCierreArqueo.setCajero(forma.getCajero().getLogin());
		}
		
		//Se adiciona validación para que cuando en la busqueda no se selecciona tipo de Arqueo deba volver a consultar
		//todos los registros
		if(forma.getTipoArqueo()!=null)
		{
			if(UtilidadTexto.isEmpty(forma.getTipoArqueo().getDescripcion()))
			{
				dtoBusquedaCierreArqueo.setCodigoTipoMovimiento(ConstantesBD.codigoNuncaValido);
				forma.setTipoArqueo(null);
			}else{
			
				dtoBusquedaCierreArqueo.setCodigoTipoMovimiento(forma.getTipoArqueoHelper());
			}
		}else
		{
			dtoBusquedaCierreArqueo.setCodigoTipoMovimiento(forma.getTipoArqueoHelper());
		}
		dtoBusquedaCierreArqueo.setConsecutivoCaja(forma.getCajaHelper());
		dtoBusquedaCierreArqueo.setFechaFinal(forma.getFechaFinal());
		dtoBusquedaCierreArqueo.setFechaInicial(forma.getFechaInicial());
		try{
			HibernateUtil.beginTransaction();
			IConsultaArqueoCierreCajaServicio consultaArqueoCierreCajaServicio = TesoreriaFabricaServicio.crearConsultaArqueoCierreCajaServicio();
			
			forma.setRegistrosCierreArqueo((ArrayList<MovimientosCaja>) consultaArqueoCierreCajaServicio.consultarCierreArqueo(dtoBusquedaCierreArqueo));
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR consultarCierreArqueo",e);
			HibernateUtil.abortTransaction();
		}
		return "resultadoBusqueda";
	}
	
	/**
	 * Método que se encarga de cargar los tipos de reporte a imprimir
	 * 
	 * @param form
	 * @author Fabián Becerra
	 */
	private void cargarTiposReporte(ConsultaCierreArqueoForm form, String[] listadoTiposReporte){
		
		form.setListadoTiposReporte(new ArrayList<DtoIntegridadDominio>());
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<DtoIntegridadDominio> listaTiposReporte=Utilidades.generarListadoConstantesIntegridadDominio(
				con, listadoTiposReporte, false);
		
		UtilidadBD.closeConnection(con);
		
		form.setListadoTiposReporte(listaTiposReporte);
		
	}

	/**
	 * Método que se encarga de consultar la información relacionada con el movimiento
	 * seleccionado del listado de registros de la busqueda realizada.
	 * 
	 * @param forma
	 */
	private void cargarCierreArqueo(ConsultaCierreArqueoForm forma, int codigoInstitucion) {
		try{
			HibernateUtil.beginTransaction();
		
			IConsultaArqueoCierreCajaServicio consultaArqueoCierreCajaServicio = TesoreriaFabricaServicio.crearConsultaArqueoCierreCajaServicio();
			
			MovimientosCaja movimientosCaja = obtenerMovimientoCajaSeleccionado(forma);
			
			TiposMovimientoCaja tipoMovimiento=new TiposMovimientoCaja();
			
			if(movimientosCaja!=null){
				
				if(movimientosCaja.getTiposMovimientoCaja().getCodigo() == ConstantesBD.codigoTipoMovimientoArqueoCaja){
					
					forma.setConsolidadoMovimientoDTO(consultaArqueoCierreCajaServicio.consultarArqueoCaja(movimientosCaja));
					
					boolean existenDocumentos = (forma.getConsolidadoMovimientoDTO().getNumeroTotalDocumentos() > 0) ? true : false ;
					
					forma.setExistenDocumentos(existenDocumentos);
					
					forma.setImprimeCuadreCaja(true);
					
					forma.setMovimientoImprimir("arqueoCaja");
					
					if(forma.getTipoArqueo()==null)
					{
						tipoMovimiento.setCodigo(ConstantesBD.codigoTipoMovimientoArqueoCaja);
						forma.setTipoArqueo(tipoMovimiento);
					}
					
				}else if(movimientosCaja.getTiposMovimientoCaja().getCodigo() == ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial){
					
					long codigoEntrega = ConstantesBD.codigoNuncaValidoLong;
					
					MovimientosCaja movimientoEntrega = consultaArqueoCierreCajaServicio.consultarEntregaAsociadaArqueoParcial(movimientosCaja.getCodigoPk());
					
					IMovimientosCajaMundo movimientosCajaMundo = TesoreriaFabricaMundo.crearMovimientosCajaMundo();
					DtoInformacionEntrega dtoInformacionEntrega = new DtoInformacionEntrega(movimientosCajaMundo.obtenerConsolidadoMovimiento(movimientosCaja));
					
					Log4JManager.info("----------------- nombre completo cajero "  + forma.getNombreCompletoCajero());
					
					if(movimientoEntrega!=null){
						
						boolean existenDocumentos = (dtoInformacionEntrega.getNumeroTotalDocumentos() > 0) ? true : false ;
						forma.setExistenDocumentos(existenDocumentos);
						
						if(movimientoEntrega.getEntregaCajaMayorByCodigoPk()!=null){
							
							Log4JManager.info("consecutivo para Entrega Caja Mayor " + codigoEntrega );
							
							codigoEntrega = movimientoEntrega.getEntregaCajaMayorByCodigoPk().getMovimientoCaja();
							
							forma.setDtoInformacionEntrega(consultaArqueoCierreCajaServicio.consultarArqueoParcialPorEntrega(codigoEntrega, ETipoMovimiento.ENTREGA_CAJA_MAYOR_PRINCIPAL, codigoInstitucion));
							
							forma.setEstadoGuardarMovimiento("EntregaCajaMayorPrincipal");
							
						}else if (movimientoEntrega.getEntregaTransportadoraByCodigoPk()!=null){
							
							Log4JManager.info("consecutivo para Entrega Transportadora " + codigoEntrega );
							
							codigoEntrega = movimientoEntrega.getEntregaTransportadoraByCodigoPk().getMovimientoCaja();
							
							forma.setDtoInformacionEntrega(consultaArqueoCierreCajaServicio.consultarArqueoParcialPorEntrega(codigoEntrega, ETipoMovimiento.ENTREGA_TRANSPORTADORA_VALORES, codigoInstitucion));
							String nombreUsuarioGeneraArqueo=movimientosCaja.getUsuarios().getPersonas().getPrimerApellido()+" "+(UtilidadTexto.isEmpty(movimientosCaja.getUsuarios().getPersonas().getSegundoApellido())?"":movimientosCaja.getUsuarios().getPersonas().getSegundoApellido())+" "
																+movimientosCaja.getUsuarios().getPersonas().getPrimerNombre()+" "+(UtilidadTexto.isEmpty(movimientosCaja.getUsuarios().getPersonas().getSegundoNombre())?"":movimientosCaja.getUsuarios().getPersonas().getSegundoNombre())+" "
																+" ("+movimientosCaja.getUsuarios().getLogin()+")";
							forma.getDtoInformacionEntrega().setNombreUsuarioGeneraCierre(nombreUsuarioGeneraArqueo);
							forma.getDtoInformacionEntrega().setEntregaTransportadoraValoresDTOs(dtoInformacionEntrega.getEntregaTransportadoraValoresDTOs());
							forma.getDtoInformacionEntrega().setTotalesParcialesEntrTransDTOs(dtoInformacionEntrega.getTotalesParcialesEntrTransDTOs());
							forma.getDtoInformacionEntrega().setTotalEntregaTransportadoraCajaEntregado(dtoInformacionEntrega.getTotalEntregaTransportadoraCajaEntregado());
							ArrayList<Integer> tiposFormaPago= new ArrayList<Integer>();
							for(DtoEntregaCaja dtoEntregaTrans:forma.getDtoInformacionEntrega().getEntregaTransportadoraValoresDTOs())
							{
								if(!tiposFormaPago.contains(dtoEntregaTrans.getTipoFormaPago())&&dtoEntregaTrans.getIdMovimientoCaja()==forma.getDtoInformacionEntrega().getMovimientosCaja().getCodigoPk())
								{
									BigDecimal valorFaltante=new BigDecimal(0.0);
									double valorSistema=0.0;
									for(DtoEntregaCaja dtoEntregaTransInterno:forma.getDtoInformacionEntrega().getEntregaTransportadoraValoresDTOs())
									{
										if(dtoEntregaTrans.getTipoFormaPago()==dtoEntregaTransInterno.getTipoFormaPago()
												&&dtoEntregaTransInterno.getIdMovimientoCaja()==forma.getDtoInformacionEntrega().getMovimientosCaja().getCodigoPk())
										{
											valorFaltante=valorFaltante.add((dtoEntregaTransInterno.getValorFaltante()!=null?dtoEntregaTransInterno.getValorFaltante():new BigDecimal(0.0)));
											valorSistema+=dtoEntregaTransInterno.getValorSistema();
										}
									}
									tiposFormaPago.add(dtoEntregaTrans.getTipoFormaPago());
									for(DtoCuadreCaja dtoCuadreCaja:forma.getDtoInformacionEntrega().getCuadreCajaDTOs())
									{
										if(dtoEntregaTrans.getTipoFormaPago()==dtoCuadreCaja.getTipoFormaPago())
										{
											dtoCuadreCaja.setValorDiferencia(valorFaltante.doubleValue());
											dtoCuadreCaja.setValorCaja(valorSistema);
										}
									}
								}
								
							}
							
							forma.setEstadoGuardarMovimiento("EntregaTransportadora");
						}
					}
					
					forma.getDtoInformacionEntrega().setTotalesDocumentoDTOs(dtoInformacionEntrega.getTotalesDocumentoDTOs());
					forma.getDtoInformacionEntrega().setCajaMayorPrincipal(forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getCajas());
					forma.getDtoInformacionEntrega().setMovimientoCajaAsignado(movimientosCaja);
					
					forma.setMovimientoImprimir("arqueoEntregaParcial");
					
					if(forma.getTipoArqueo()==null)
					{
						tipoMovimiento.setCodigo(ConstantesBD.codigoTipoMovimientoArqueoEntregaParcial);
						forma.setTipoArqueo(tipoMovimiento);
					}
					
				}else if(movimientosCaja.getTiposMovimientoCaja().getCodigo() == ConstantesBD.codigoTipoMovimientoArqueoCierreTurno){
					
					forma.setDtoInformacionEntrega(consultaArqueoCierreCajaServicio.consultarCierreTurnoCaja(movimientosCaja));
					
					if(forma.getDtoInformacionEntrega()!=null){
						
						boolean existenDocumentos = (forma.getDtoInformacionEntrega().getNumeroTotalDocumentos() > 0) ? true : false ;
						
						forma.setExistenDocumentos(existenDocumentos);
						forma.setCierreTurnoCaja(movimientosCaja);
						forma.getDtoInformacionEntrega().setMovimientosCaja(movimientosCaja);
					}
					
					forma.setMovimientoImprimir("cierreTurnoCaja");
					
					if(forma.getTipoArqueo()==null)
					{
						tipoMovimiento.setCodigo(ConstantesBD.codigoTipoMovimientoArqueoCierreTurno);
						forma.setTipoArqueo(tipoMovimiento);
					}
				}
			}
			
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR cargarCierreArqueo",e);
			HibernateUtil.abortTransaction();
		}
	}
	
	/**
	 * Método que se encarga de encontrar el movimiento seleccionado del listado
	 * de registros de la búsqueda realizada.
	 * 
	 * @param forma
	 * @return
	 */
	private MovimientosCaja obtenerMovimientoCajaSeleccionado(ConsultaCierreArqueoForm forma){
		
		for (MovimientosCaja movimientoCaja : forma.getRegistrosCierreArqueo()) {
			
			if(movimientoCaja.getCodigoPk() == forma.getCodigoPkMovimientoConsultar()){
				
				return movimientoCaja;
			}
		}
		
		return null;
	}
	
	/**
	 * Metodo encargado de ordenar la lista de motivos
	 * @param forma
	 */
	private void accionOrdenar(ConsultaCierreArqueoForm forma) 
	{
		boolean ordenamiento = false;
		
		
		
		//VALIDACION DE ELEMENTOS A ORDENAR ASC O DESC
		if(forma.getEsDescendente().equals(forma.getPatronOrdenar()+"descendente"))
		{
			ordenamiento = true;
		}

		if(!forma.getPatronOrdenar().equals("CodigoCaja") &&
				!forma.getPatronOrdenar().equals("CajeroSort") &&
				!forma.getPatronOrdenar().equals("TipoSort") ){
			SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
			Collections.sort(forma.getRegistrosCierreArqueo(),sortG);
		}else if (forma.getPatronOrdenar().equals("CodigoCaja")){
			ArrayList<MovimientosCaja> listaCierreArqueo= new ArrayList<MovimientosCaja>();
			ArrayList<DtoMovimientosCajaEntregaParcial> listaDtoMovimientosCaja = new ArrayList<DtoMovimientosCajaEntregaParcial>();

			listaCierreArqueo=forma.getRegistrosCierreArqueo();
			for (int i = 0; i < listaCierreArqueo.size(); i++) {
				DtoMovimientosCajaEntregaParcial dtoMovimientosCajaEntregaParcial = new DtoMovimientosCajaEntregaParcial();
				dtoMovimientosCajaEntregaParcial.setMovimientosCaja(listaCierreArqueo.get(i));
				dtoMovimientosCajaEntregaParcial.setCodigoCaja(listaCierreArqueo.get(i).getTurnoDeCaja().getCajas().getCodigo());
				listaDtoMovimientosCaja.add(dtoMovimientosCajaEntregaParcial);
			}

			SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
			Collections.sort(listaDtoMovimientosCaja,sortG);

			listaCierreArqueo= new ArrayList<MovimientosCaja>();
			for (int i = 0; i < listaDtoMovimientosCaja.size(); i++) {
				listaCierreArqueo.add(listaDtoMovimientosCaja.get(i).getMovimientosCaja());
			}
			forma.setRegistrosCierreArqueo(listaCierreArqueo);
			
			
			
		}else if (forma.getPatronOrdenar().equals("CajeroSort")){
			ArrayList<MovimientosCaja> listaCierreArqueo= new ArrayList<MovimientosCaja>();
			ArrayList<DtoMovimientosCajaEntregaParcial> listaDtoMovimientosCaja = new ArrayList<DtoMovimientosCajaEntregaParcial>();
			listaCierreArqueo=forma.getRegistrosCierreArqueo();
			
			
			
			for (int i = 0; i < listaCierreArqueo.size(); i++) {
				DtoMovimientosCajaEntregaParcial dtoMovimientosCajaEntregaParcial = new DtoMovimientosCajaEntregaParcial();
				dtoMovimientosCajaEntregaParcial.setMovimientosCaja(listaCierreArqueo.get(i));
				
				
				String cajero =  listaCierreArqueo.get(i).getTurnoDeCaja().getUsuarios().getPersonas().getPrimerApellido()
				+ " " + (listaCierreArqueo.get(i).getTurnoDeCaja().getUsuarios().getPersonas().getSegundoApellido()!=null?listaCierreArqueo.get(i).getTurnoDeCaja().getUsuarios().getPersonas().getSegundoApellido():"") +
				" " + listaCierreArqueo.get(i).getTurnoDeCaja().getUsuarios().getPersonas().getPrimerNombre() + " "
				+ (listaCierreArqueo.get(i).getTurnoDeCaja().getUsuarios().getPersonas().getSegundoNombre()!=null?listaCierreArqueo.get(i).getTurnoDeCaja().getUsuarios().getPersonas().getSegundoNombre():"")+
				" ( " +  listaCierreArqueo.get(i).getTurnoDeCaja().getUsuarios().getLogin()  + " ) ";
				
				dtoMovimientosCajaEntregaParcial.setCajeroSort(cajero);
				listaDtoMovimientosCaja.add(dtoMovimientosCajaEntregaParcial);
			}

			
			
			
			SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
			Collections.sort(listaDtoMovimientosCaja,sortG);

			listaCierreArqueo= new ArrayList<MovimientosCaja>();
			for (int i = 0; i < listaDtoMovimientosCaja.size(); i++) {
				listaCierreArqueo.add(listaDtoMovimientosCaja.get(i).getMovimientosCaja());
			}
			forma.setRegistrosCierreArqueo(listaCierreArqueo);
		}else if (forma.getPatronOrdenar().equals("TipoSort")){
			
			ArrayList<MovimientosCaja> listaCierreArqueo= new ArrayList<MovimientosCaja>();
			ArrayList<DtoMovimientosCajaEntregaParcial> listaDtoMovimientosCaja = new ArrayList<DtoMovimientosCajaEntregaParcial>();
			listaCierreArqueo=forma.getRegistrosCierreArqueo();
			
			
			
			for (int i = 0; i < listaCierreArqueo.size(); i++) {
				DtoMovimientosCajaEntregaParcial dtoMovimientosCajaEntregaParcial = new DtoMovimientosCajaEntregaParcial();
				dtoMovimientosCajaEntregaParcial.setMovimientosCaja(listaCierreArqueo.get(i));
				
				
				String tipo =  listaCierreArqueo.get(i).getTiposMovimientoCaja().getDescripcion();
				
				dtoMovimientosCajaEntregaParcial.setTipoSort(tipo);
				listaDtoMovimientosCaja.add(dtoMovimientosCajaEntregaParcial);
			}

			
			
			
			SortGenerico sortG=new SortGenerico(forma.getPatronOrdenar(),ordenamiento);
			Collections.sort(listaDtoMovimientosCaja,sortG);

			listaCierreArqueo= new ArrayList<MovimientosCaja>();
			for (int i = 0; i < listaDtoMovimientosCaja.size(); i++) {
				listaCierreArqueo.add(listaDtoMovimientosCaja.get(i).getMovimientosCaja());
			}
			forma.setRegistrosCierreArqueo(listaCierreArqueo);
			
			
		}


	}
	
	/**
	 * Método que genera el reporte resumido, detallado o ambos de cierre turno de caja DCU 1038
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param ins
	 */
	private void imprimirCierreTurnoCaja(ConsultaCierreArqueoForm forma,  HttpServletRequest request, UsuarioBasico usuario, InstitucionBasica ins) {
        
        String tipoSalida         = forma.getTipoReporte();
        String nombreArchivo="";
        forma.setListaNombresReportes(new ArrayList<String>());
        try{
        	HibernateUtil.beginTransaction();
	        if (!tipoSalida.equals(ConstantesBD.codigoNuncaValido+"")&&!UtilidadTexto.isEmpty(tipoSalida)) {
	
	        	UsuariosDelegate usu= new UsuariosDelegate();
				Usuarios usuarioCompleto=usu.findById(usuario.getLoginUsuario());
				
	        	//TITULO REPORTE
	    		String razonSocial = ins.getRazonSocial();
	    		forma.getDtoFiltroReporte().setRazonSocial(razonSocial);
	    		forma.getDtoFiltroReporte().setNit(ins.getNit());
	        	forma.getDtoFiltroReporte().setUbicacionLogo(ins.getUbicacionLogo());
	        	forma.getDtoFiltroReporte().setActividadEconomica(ins.getActividadEconomica());
	        	forma.getDtoFiltroReporte().setDireccion(ins.getDireccion());
	        	forma.getDtoFiltroReporte().setTelefono(ins.getTelefono());
	        	forma.getDtoFiltroReporte().setCentroAtencion(usuario.getCentroAtencion());
	        	
	        	//ENCABEZADO REPORTE
	       		forma.getDtoFiltroReporte().setNroConsecutivo(forma.getDtoInformacionEntrega().getMovimientosCaja().getConsecutivo()+"");
				
				String fecha=UtilidadFecha.conversionFormatoFechaAAp(forma.getDtoInformacionEntrega().getMovimientosCaja().getFecha());
				String hora	 = forma.getDtoInformacionEntrega().getMovimientosCaja().getHora();
	        	forma.getDtoFiltroReporte().setFechaGeneracion(fecha);
	        	forma.getDtoFiltroReporte().setHoraGeneracion(hora);
	        	forma.getDtoFiltroReporte().setUsuarioGeneracion(forma.getDtoInformacionEntrega().getMovimientosCaja().getUsuarios().getPersonas().getPrimerApellido()
	        			+" "+forma.getDtoInformacionEntrega().getMovimientosCaja().getUsuarios().getPersonas().getPrimerNombre()
	        			+" - "+forma.getDtoInformacionEntrega().getMovimientosCaja().getUsuarios().getLogin());
	        	forma.getDtoFiltroReporte().setFechaCierre(UtilidadFecha.conversionFormatoFechaAAp(forma.getDtoInformacionEntrega().getMovimientosCaja().getFechaMovimiento()));
	        	Usuarios cajero = forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getUsuarios();
	        	forma.getDtoFiltroReporte().setCajero(cajero.getPersonas().getPrimerApellido()
	        			+" "+cajero.getPersonas().getPrimerNombre()
	        			+" - "+cajero.getLogin());
	        	forma.getDtoFiltroReporte().setCaja("(" + forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getCajas().getCodigo() 
	        			+ ") " +forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getCajas().getDescripcion()
	        			+ " - "+forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getCajas().getCentroAtencion().getDescripcion());
	        	forma.getDtoFiltroReporte().setCajaMayorPrincipal("("+forma.getDtoInformacionEntrega().getMovimientosCaja().getDetalleMovCajaCierre().getCajas().getCodigo()
	        			+") " + forma.getDtoInformacionEntrega().getMovimientosCaja().getDetalleMovCajaCierre().getCajas().getDescripcion()
	        			+ " - "+forma.getDtoInformacionEntrega().getMovimientosCaja().getDetalleMovCajaCierre().getCajas().getCentroAtencion().getDescripcion());
	        	
				forma.getDtoFiltroReporte().setNombreUsuario(usuarioCompleto.getPersonas().getPrimerNombre()
						+" "+usuarioCompleto.getPersonas().getPrimerApellido()
						+" ("+usuarioCompleto.getLogin()+")"
						);
				forma.getDtoFiltroReporte().setEsConsulta(true);
				
		    	String rutaLogo = ins.getLogoJsp();
		    	forma.getDtoFiltroReporte().setRutaLogo(rutaLogo);
		    		
		    	JasperReportBuilder reporte=null;
				if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteImpCierreTurnoResumido))
	            {
					GeneradorReporteCierreTurnoCajaResumido generadorReporte =
	    	            new GeneradorReporteCierreTurnoCajaResumido(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega());
	            		reporte = generadorReporte.generarReporte();
	            		nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ImpresionCierreTurnoResumido");
	            		forma.getListaNombresReportes().add(nombreArchivo);
	            }else
	            	if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteImpCierreTurnoDetallado))
	            	{
	            		GeneradorReporteCierreTurnoCajaDetallado generadorReporte =
	            			new GeneradorReporteCierreTurnoCajaDetallado(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega());
	            		reporte = generadorReporte.generarReporte();
	            		nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ImpresionCierreTurnoDetallado");
	            		forma.getListaNombresReportes().add(nombreArchivo);
	            	}else
	            		if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteAmbos))
	            		{
	            			GeneradorReporteCierreTurnoCajaResumido generadorReporteRes =
	            	            new GeneradorReporteCierreTurnoCajaResumido(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega());
	                		reporte = generadorReporteRes.generarReporte();
	                		String nombreArchivoRes = generadorReporteRes.exportarReportePDF(reporte, "ImpresionCierreTurnoResumido");
	                		
	                		GeneradorReporteCierreTurnoCajaDetallado generadorReporteDeta =
	                			new GeneradorReporteCierreTurnoCajaDetallado(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega());
	                		reporte = generadorReporteDeta.generarReporte();
	                		String nombreArchivoDet = generadorReporteDeta.exportarReportePDF(reporte, "ImpresionCierreTurnoDetallado");
	                		
	                		forma.getListaNombresReportes().add(nombreArchivoRes);
	                		forma.getListaNombresReportes().add(nombreArchivoDet);
	            		}
		            
				forma.setTipoReporte(ConstantesBD.codigoNuncaValido+"");
		         
		     }
	        HibernateUtil.endTransaction();
        }
        catch (Exception e) {
			Log4JManager.error("ERROR imprimirCierreTurnoCaja", e);
			HibernateUtil.abortTransaction();
		}
	}
	
	/**
	 * Método que genera el reporte resumido, detallado o ambos de la entrega a caja mayor principal DCU 1039 llamado desde el cierre turno
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param ins
	 */
	private void imprimirEntregaCajaMayPrinc(ConsultaCierreArqueoForm forma,  HttpServletRequest request, UsuarioBasico usuario, InstitucionBasica ins) {
		
		//GENERAR EL REPORTE
		String tipoSalida         = forma.getTipoReporte();
	    String nombreArchivo="";
	    forma.setListaNombresReportes(new ArrayList<String>());
	    try{
	    	HibernateUtil.beginTransaction();
	    	if (!tipoSalida.equals(ConstantesBD.codigoNuncaValido+"")&&!UtilidadTexto.isEmpty(tipoSalida)) {
		
		    	UsuariosDelegate usu= new UsuariosDelegate();
				Usuarios usuarioCompleto=usu.findById(usuario.getLoginUsuario());
				
		    	//TITULO REPORTE
				String razonSocial = ins.getRazonSocial();
				forma.getDtoFiltroReporte().setRazonSocial(razonSocial);
				forma.getDtoFiltroReporte().setNit(ins.getNit());
		    	forma.getDtoFiltroReporte().setUbicacionLogo(ins.getUbicacionLogo());
		    	forma.getDtoFiltroReporte().setActividadEconomica(ins.getActividadEconomica());
		    	forma.getDtoFiltroReporte().setDireccion(ins.getDireccion());
		    	forma.getDtoFiltroReporte().setTelefono(ins.getTelefono());
		    	forma.getDtoFiltroReporte().setCentroAtencion(usuario.getCentroAtencion());
		    	
		    	//ENCABEZADO REPORTE
		   		forma.getDtoFiltroReporte().setNroConsecutivo(forma.getDtoInformacionEntrega().getMovimientosCaja().getConsecutivo()+"");
				
				String fecha=UtilidadFecha.conversionFormatoFechaAAp(forma.getDtoInformacionEntrega().getMovimientosCaja().getFecha());
				String hora	 = forma.getDtoInformacionEntrega().getMovimientosCaja().getHora();
		    	forma.getDtoFiltroReporte().setFechaGeneracion(fecha);
		    	forma.getDtoFiltroReporte().setHoraGeneracion(hora);
		    	forma.getDtoFiltroReporte().setUsuarioGeneracion(forma.getDtoInformacionEntrega().getMovimientosCaja().getUsuarios().getPersonas().getPrimerApellido()
		    			+" "+forma.getDtoInformacionEntrega().getMovimientosCaja().getUsuarios().getPersonas().getPrimerNombre()
		    			+" - "+forma.getDtoInformacionEntrega().getMovimientosCaja().getUsuarios().getLogin());
		    	forma.getDtoFiltroReporte().setFechaCierre(UtilidadFecha.conversionFormatoFechaAAp(forma.getDtoInformacionEntrega().getMovimientosCaja().getFechaMovimiento()));
		    	Usuarios cajero = forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getUsuarios();
		    	forma.getDtoFiltroReporte().setCajero(cajero.getPersonas().getPrimerApellido()
		    			+" "+cajero.getPersonas().getPrimerNombre()
		    			+" - "+cajero.getLogin());
		    	forma.getDtoFiltroReporte().setCaja("(" + forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getCajas().getCodigo() 
		    			+ ") " +forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getCajas().getDescripcion()
		    			+ " - "+forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getCajas().getCentroAtencion().getDescripcion());
		    	
				forma.getDtoFiltroReporte().setNombreUsuario(usuarioCompleto.getPersonas().getPrimerNombre()
						+" "+usuarioCompleto.getPersonas().getPrimerApellido()
						+" ("+usuarioCompleto.getLogin()+")"
						);
		    	
		    	String rutaLogo = ins.getLogoJsp();
		    	forma.getDtoFiltroReporte().setRutaLogo(rutaLogo);
		    		
		    	JasperReportBuilder reporte=null;
				if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteImpEntregaCajaMayPrincResumido))
		        {
					GeneradorReporteEntregaCajaMayorPrincResumido generadorReporte =
			            new GeneradorReporteEntregaCajaMayorPrincResumido(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega(),false,"");
		        		reporte = generadorReporte.generarReporte();
		        		nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ImpresionEntregaCajaMayPrincResumido");
		        		forma.getListaNombresReportes().add(nombreArchivo);
		        }else
		        	if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteImpEntregaCajaMayPrincDetallado))
		        	{
		        		GeneradorReporteEntregaCajaMayorPrincDetallado generadorReporte =
		        			new GeneradorReporteEntregaCajaMayorPrincDetallado(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega(),false,"");
		        		reporte = generadorReporte.generarReporte();
		        		nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ImpresionEntregaCajaMayPrincDetallado");
		        		forma.getListaNombresReportes().add(nombreArchivo);
		        	}else
		        		if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteAmbos))
		        		{
		        			GeneradorReporteEntregaCajaMayorPrincResumido generadorReporteRes =
		        	            new GeneradorReporteEntregaCajaMayorPrincResumido(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega(),false,"");
		            		reporte = generadorReporteRes.generarReporte();
		            		String nombreArchivoRes = generadorReporteRes.exportarReportePDF(reporte, "ImpresionEntregaCajaMayPrincResumido");
		            		
		            		GeneradorReporteEntregaCajaMayorPrincDetallado generadorReporteDeta =
		            			new GeneradorReporteEntregaCajaMayorPrincDetallado(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega(),false,"");
		            		reporte = generadorReporteDeta.generarReporte();
		            		String nombreArchivoDet = generadorReporteDeta.exportarReportePDF(reporte, "ImpresionEntregaCajaMayPrincDetallado");
		            		
		            		forma.getListaNombresReportes().add(nombreArchivoRes);
		            		forma.getListaNombresReportes().add(nombreArchivoDet);
		        		}
		            
				forma.setTipoReporte(ConstantesBD.codigoNuncaValido+"");
		     }
	    	HibernateUtil.endTransaction();
	    }
	    catch (Exception e) {
			Log4JManager.error("ERROR imprimirEntregaCajaMayPrinc", e);
			HibernateUtil.abortTransaction();
		}

	}
	
	/**
	 * Método que genera el reporte arqueo caja resumido DCU 1127 Formato A
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param ins
	 */
	private void imprimirArqueoCaja(ConsultaCierreArqueoForm forma,  HttpServletRequest request, UsuarioBasico usuario, InstitucionBasica ins) {

		try{
			HibernateUtil.beginTransaction();
			String nombreArchivo="";
			forma.setListaNombresReportes(new ArrayList<String>());
	
			UsuariosDelegate usu= new UsuariosDelegate();
			Usuarios usuarioCompleto=usu.findById(usuario.getLoginUsuario());
	
			//TITULO REPORTE
			String razonSocial = ins.getRazonSocial();
			forma.getDtoFiltroReporte().setRazonSocial(razonSocial);
			forma.getDtoFiltroReporte().setNit(ins.getNit());
			forma.getDtoFiltroReporte().setUbicacionLogo(ins.getUbicacionLogo());
			forma.getDtoFiltroReporte().setActividadEconomica(ins.getActividadEconomica());
			forma.getDtoFiltroReporte().setDireccion(ins.getDireccion());
			forma.getDtoFiltroReporte().setTelefono(ins.getTelefono());
			forma.getDtoFiltroReporte().setCentroAtencion(usuario.getCentroAtencion());
	
			forma.getDtoFiltroReporte().setExito(true);
	
			//ENCABEZADO REPORTE
			forma.getDtoFiltroReporte().setNroConsecutivo(forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getConsecutivo()+"");
			String fecha = UtilidadFecha.getFechaActual();
			String hora	 = UtilidadFecha.getHoraActual();
			if(forma.isEsConsultaConsolidadoCierre())
			{
				fecha = UtilidadFecha.conversionFormatoFechaAAp(forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getFecha());
				hora  = forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getHora();
			}
			if(forma.isConsulta())
			{
				fecha = UtilidadFecha.conversionFormatoFechaAAp(forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getFecha());
				hora  = forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getHora();
			}
	
			String fechaArqueo = (forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getFechaMovimiento() == null || forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getIngresaFechaArqueo() == ConstantesBD.acronimoNoChar) ? 
					UtilidadFecha.conversionFormatoFechaAAp(forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getTurnoDeCaja().getFechaApertura()) + " - " + fecha : 
						UtilidadFecha.conversionFormatoFechaAAp(forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getFechaMovimiento()); 
			forma.getDtoFiltroReporte().setFechaArqueo(fechaArqueo);
	
			forma.getDtoFiltroReporte().setFechaGeneracion(fecha);
			forma.getDtoFiltroReporte().setHoraGeneracion(hora);
	
			forma.getDtoFiltroReporte().setUsuarioGeneracion(usuarioCompleto.getPersonas().getPrimerApellido()
					+" "+usuarioCompleto.getPersonas().getPrimerNombre()
					+" - "+usuarioCompleto.getLogin());
	
			Usuarios cajero = forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getTurnoDeCaja().getUsuarios();
			forma.getDtoFiltroReporte().setCajero(cajero.getPersonas().getPrimerApellido()
					+" "+cajero.getPersonas().getPrimerNombre()
					+" - "+cajero.getLogin());
			forma.getDtoFiltroReporte().setCaja("(" + forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getTurnoDeCaja().getCajas().getCodigo() 
					+ ") " +forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getTurnoDeCaja().getCajas().getDescripcion()
					+ " - "+forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getTurnoDeCaja().getCajas().getCentroAtencion().getDescripcion());
	
	
			forma.getDtoFiltroReporte().setNombreUsuario(usuarioCompleto.getPersonas().getPrimerNombre()
					+" "+usuarioCompleto.getPersonas().getPrimerApellido()
					+" ("+usuarioCompleto.getLogin()+")"
			);
	
			String rutaLogo = ins.getLogoJsp();
			forma.getDtoFiltroReporte().setRutaLogo(rutaLogo);
	
			JasperReportBuilder reporte=null;
			GeneradorReporteArqueoCajaResumido generadorReporte =
				new GeneradorReporteArqueoCajaResumido(forma.getDtoFiltroReporte(), forma.getConsolidadoMovimientoDTO());
			reporte = generadorReporte.generarReporte();
			nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ImpresionArqueoCajaResumido");
			forma.getListaNombresReportes().add(nombreArchivo);
	
			forma.setTipoReporte(ConstantesBD.codigoNuncaValido+"");
	
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR imprimirArqueoCaja", e);
			HibernateUtil.abortTransaction();
		}
	}
	
	
	/**
	 * Método que genera el reporte traslado entre cajas DCU 1024
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param ins
	 */
	private void imprimirSolicitudTrasladoEntreCajas(ConsultaCierreArqueoForm forma,  HttpServletRequest request, UsuarioBasico usuario, InstitucionBasica ins) {
		String nombreArchivo="";
		forma.setListaNombresReportes(new ArrayList<String>());

		UsuariosDelegate usu= new UsuariosDelegate();
		Usuarios usuarioCompleto=usu.findById(usuario.getLoginUsuario());

		//TITULO REPORTE
		String razonSocial = ins.getRazonSocial();
		forma.getDtoFiltroReporte().setRazonSocial(razonSocial);
		forma.getDtoFiltroReporte().setNit(ins.getNit());
		forma.getDtoFiltroReporte().setUbicacionLogo(ins.getUbicacionLogo());
		forma.getDtoFiltroReporte().setActividadEconomica(ins.getActividadEconomica());
		forma.getDtoFiltroReporte().setDireccion(ins.getDireccion());
		forma.getDtoFiltroReporte().setTelefono(ins.getTelefono());
		forma.getDtoFiltroReporte().setCentroAtencion(usuario.getCentroAtencion());

		//ENCABEZADO REPORTE
		forma.getDtoFiltroReporte().setNroConsecutivo(forma.getDtoConsultaTrasladosCajasRecaudo().getConsecutivoSolicitud()+"");
		forma.getDtoFiltroReporte().setFechaGeneracion(UtilidadFecha.conversionFormatoFechaAAp(forma.getDtoConsultaTrasladosCajasRecaudo().getFechaInicial()));
		forma.getDtoFiltroReporte().setHoraGeneracion(forma.getDtoConsultaTrasladosCajasRecaudo().getHoraSolicitud());
		forma.getDtoFiltroReporte().setEstadoSolicitud(forma.getDtoConsultaTrasladosCajasRecaudo().getEstadoSolicitud());
		forma.getDtoFiltroReporte().setCajaTraslada(forma.getDtoConsultaTrasladosCajasRecaudo().getDescripcionCajaOrigen());


		forma.getDtoFiltroReporte().setCajeroTraslada(forma.getDtoConsultaTrasladosCajasRecaudo().getLoginCajeroSolicitante());


		forma.getDtoFiltroReporte().setFechaAceptacion(UtilidadFecha.conversionFormatoFechaAAp(forma.getDtoConsultaTrasladosCajasRecaudo().getFechaAceptacion()));
		forma.getDtoFiltroReporte().setHoraAceptacion(forma.getDtoConsultaTrasladosCajasRecaudo().getHoraAceptacion());

		if(!UtilidadTexto.isEmpty(forma.getDtoConsultaTrasladosCajasRecaudo().getDescripcionCajaDestino()))
			forma.getDtoFiltroReporte().setCajaAcepta(forma.getDtoConsultaTrasladosCajasRecaudo().getDescripcionCajaDestino());

		if(!UtilidadTexto.isEmpty(forma.getDtoConsultaTrasladosCajasRecaudo().getLoginCajeroAcepta()))
			forma.getDtoFiltroReporte().setCajeroAcepta(forma.getDtoConsultaTrasladosCajasRecaudo().getLoginCajeroAcepta());


		forma.getDtoFiltroReporte().setNombreUsuario(usuarioCompleto.getPersonas().getPrimerNombre()
				+" "+usuarioCompleto.getPersonas().getPrimerApellido()
				+" ("+usuarioCompleto.getLogin()+")"
		);

		String rutaLogo = ins.getLogoJsp();
		forma.getDtoFiltroReporte().setRutaLogo(rutaLogo);

		JasperReportBuilder reporte=null;
		GeneradorReporteTrasladoEntreCajas generadorReporte =
			new GeneradorReporteTrasladoEntreCajas(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega(), forma.getDtoConsultaTrasladosCajasRecaudo());
		reporte = generadorReporte.generarReporte();
		nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ImpresionTrasladoEntreCajas");
		forma.getListaNombresReportes().add(nombreArchivo);

		forma.setTipoReporte(ConstantesBD.codigoNuncaValido+"");

		
	}
	
	/**
	 * Obtiene el movimoento de caja asignado a reporte
	 * @param forma
	 */
	public void obtenerMovimientCajaAsociado(ConsultaCierreArqueoForm forma){
		for (MovimientosCaja movimientoCaja : forma.getRegistrosCierreArqueo()) {
			if(movimientoCaja.getConsecutivo()==forma.getConsecutivoMovimientoConsultar()){
				forma.getDtoInformacionEntrega().setMovimientoCajaAsignado(movimientoCaja);
			}
		}
	}
	
	/**
	 * Método que genera el reporte entrega caja mayor principal llamado desde el arqueo parcial
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param ins
	 */
	private void imprimirEntregaCajaMayPrincipal(ConsultaCierreArqueoForm forma,  HttpServletRequest request, UsuarioBasico usuario, InstitucionBasica ins) {
		
		try{
			HibernateUtil.beginTransaction();
			//GENERAR EL REPORTE
			String tipoSalida         = forma.getTipoReporte();
		    String nombreArchivo="";
		    forma.setListaNombresReportes(new ArrayList<String>());
		    
		    if (!tipoSalida.equals(ConstantesBD.codigoNuncaValido+"")&&!UtilidadTexto.isEmpty(tipoSalida)) {
		
		    	UsuariosDelegate usu= new UsuariosDelegate();
				Usuarios usuarioCompleto=usu.findById(usuario.getLoginUsuario());
				
		    	//TITULO REPORTE
				String razonSocial = ins.getRazonSocial();
				forma.getDtoFiltroReporte().setRazonSocial(razonSocial);
				forma.getDtoFiltroReporte().setNit(ins.getNit());
		    	forma.getDtoFiltroReporte().setUbicacionLogo(ins.getUbicacionLogo());
		    	forma.getDtoFiltroReporte().setActividadEconomica(ins.getActividadEconomica());
		    	forma.getDtoFiltroReporte().setDireccion(ins.getDireccion());
		    	forma.getDtoFiltroReporte().setTelefono(ins.getTelefono());
		    	forma.getDtoFiltroReporte().setCentroAtencion(usuario.getCentroAtencion());
		    	
		    	//ENCABEZADO REPORTE
		   		forma.getDtoFiltroReporte().setNroConsecutivo(forma.getDtoInformacionEntrega().getMovimientosCaja().getConsecutivo()+"");
				
				String fecha=UtilidadFecha.conversionFormatoFechaAAp(forma.getDtoInformacionEntrega().getMovimientosCaja().getFecha());
				String hora	 = forma.getDtoInformacionEntrega().getMovimientosCaja().getHora();
		    	forma.getDtoFiltroReporte().setFechaGeneracion(fecha);
		    	forma.getDtoFiltroReporte().setHoraGeneracion(hora);
		    	forma.getDtoFiltroReporte().setUsuarioGeneracion(forma.getDtoInformacionEntrega().getMovimientosCaja().getUsuarios().getPersonas().getPrimerApellido()
		    			+" "+forma.getDtoInformacionEntrega().getMovimientosCaja().getUsuarios().getPersonas().getPrimerNombre()
		    			+" - "+forma.getDtoInformacionEntrega().getMovimientosCaja().getUsuarios().getLogin());
		    	forma.getDtoFiltroReporte().setFechaCierre(UtilidadFecha.conversionFormatoFechaAAp(forma.getDtoInformacionEntrega().getMovimientosCaja().getFechaMovimiento()));
		    	Usuarios cajero = forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getUsuarios();
		    	forma.getDtoFiltroReporte().setCajero(cajero.getPersonas().getPrimerApellido()
		    			+" "+cajero.getPersonas().getPrimerNombre()
		    			+" - "+cajero.getLogin());
		    	forma.getDtoFiltroReporte().setCaja("(" + forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getCajas().getCodigo() 
		    			+ ") " +forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getCajas().getDescripcion()
		    			+ " - "+forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getCajas().getCentroAtencion().getDescripcion());
		    	
				forma.getDtoFiltroReporte().setNombreUsuario(usuarioCompleto.getPersonas().getPrimerNombre()
						+" "+usuarioCompleto.getPersonas().getPrimerApellido()
						+" ("+usuarioCompleto.getLogin()+")"
						);
				
				forma.getDtoFiltroReporte().setEsConsulta(true);
		    	
		    	String rutaLogo = ins.getLogoJsp();
		    	forma.getDtoFiltroReporte().setRutaLogo(rutaLogo);
		    	
		    	JasperReportBuilder reporte=null;
				if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteImpEntregaCajaMayPrincResumido))
		        {
					GeneradorReporteEntregaCajaMayorPrincResumido generadorReporte =
			            new GeneradorReporteEntregaCajaMayorPrincResumido(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega(), forma.isConsulta(), ConstantesBD.acronimoNo);
		        		reporte = generadorReporte.generarReporte();
		        		nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ImpresionEntregaCajaMayPrincResumido");
		        		forma.getListaNombresReportes().add(nombreArchivo);
		        }else
		        	if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteImpEntregaCajaMayPrincDetallado))
		        	{
		        		GeneradorReporteEntregaCajaMayorPrincDetallado generadorReporte =
		        			new GeneradorReporteEntregaCajaMayorPrincDetallado(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega(), forma.isConsulta(), ConstantesBD.acronimoNo);
		        		reporte = generadorReporte.generarReporte();
		        		nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ImpresionEntregaCajaMayPrincDetallado");
		        		forma.getListaNombresReportes().add(nombreArchivo);
		        	}else
		        		if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteAmbos))
		        		{
		        			GeneradorReporteEntregaCajaMayorPrincResumido generadorReporteRes =
		        	            new GeneradorReporteEntregaCajaMayorPrincResumido(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega(), forma.isConsulta(), ConstantesBD.acronimoNo);
		            		reporte = generadorReporteRes.generarReporte();
		            		String nombreArchivoRes = generadorReporteRes.exportarReportePDF(reporte, "ImpresionEntregaCajaMayPrincResumido");
		            		
		            		GeneradorReporteEntregaCajaMayorPrincDetallado generadorReporteDeta =
		            			new GeneradorReporteEntregaCajaMayorPrincDetallado(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega(), forma.isConsulta(), ConstantesBD.acronimoNo);
		            		reporte = generadorReporteDeta.generarReporte();
		            		String nombreArchivoDet = generadorReporteDeta.exportarReportePDF(reporte, "ImpresionEntregaCajaMayPrincDetallado");
		            		
		            		forma.getListaNombresReportes().add(nombreArchivoRes);
		            		forma.getListaNombresReportes().add(nombreArchivoDet);
		        		}
		            
				forma.setTipoReporte(ConstantesBD.codigoNuncaValido+"");
		     }
		    HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR imprimirEntregaCajaMayPrincipal", e);
			HibernateUtil.abortTransaction();
		}

	}
	
	/**
	 * Método que genera el reporte de entrega transportadora DCU 1025
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param ins
	 */
	private void imprimirEntregaTransportadora(ConsultaCierreArqueoForm forma,  HttpServletRequest request, UsuarioBasico usuario, InstitucionBasica ins) {
		try{
			HibernateUtil.beginTransaction();
			//GENERAR EL REPORTE
		    String nombreArchivo="";
		    forma.setListaNombresReportes(new ArrayList<String>());
		    
		
	    	UsuariosDelegate usu= new UsuariosDelegate();
			Usuarios usuarioCompleto=usu.findById(usuario.getLoginUsuario());
			
	    	//TITULO REPORTE
			String razonSocial = ins.getRazonSocial();
			forma.getDtoFiltroReporte().setRazonSocial(razonSocial);
			forma.getDtoFiltroReporte().setNit(ins.getNit());
	    	forma.getDtoFiltroReporte().setUbicacionLogo(ins.getUbicacionLogo());
	    	forma.getDtoFiltroReporte().setActividadEconomica(ins.getActividadEconomica());
	    	forma.getDtoFiltroReporte().setDireccion(ins.getDireccion());
	    	forma.getDtoFiltroReporte().setTelefono(ins.getTelefono());
	    	forma.getDtoFiltroReporte().setCentroAtencion(usuario.getCentroAtencion());
	    	
	    	//ENCABEZADO REPORTE
	   		forma.getDtoFiltroReporte().setNroConsecutivo(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk().getConsecutivo()+"");
			
			String fecha=UtilidadFecha.conversionFormatoFechaAAp(forma.getDtoInformacionEntrega().getMovimientosCaja().getFecha());
			String hora	 = forma.getDtoInformacionEntrega().getMovimientosCaja().getHora();
	    	forma.getDtoFiltroReporte().setFechaGeneracion(fecha);
	    	forma.getDtoFiltroReporte().setHoraGeneracion(hora);
	    	
	    	forma.getDtoFiltroReporte().setCaja(forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getCajas().getCodigo()+ 
	    			") " + forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getCajas().getDescripcion() + 
	    			" - " + forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getCajas().getCentroAtencion().getDescripcion());
	    	
	    	
	    	Usuarios cajero = forma.getDtoInformacionEntrega().getMovimientosCaja().getTurnoDeCaja().getUsuarios();
	    	forma.getDtoFiltroReporte().setCajero(cajero.getPersonas().getPrimerApellido()
	    			+" "+cajero.getPersonas().getPrimerNombre()
	    			+" - "+cajero.getLogin());
	    	
	    	forma.getDtoFiltroReporte().setTransportadora(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getTransportadoraValores().getTerceros().getDescripcion());
	    	forma.getDtoFiltroReporte().setNitTransportadora(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getTransportadoraValores().getTerceros().getNumeroIdentificacion());
	    	
	    	forma.getDtoFiltroReporte().setResponsableRecibe(UtilidadTexto.isEmpty(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getResponsable())?"":forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getResponsable());
	    	forma.getDtoFiltroReporte().setNumCarnet((UtilidadTexto.isEmpty(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCarnet())?"":forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCarnet()));
	    	
	    	forma.getDtoFiltroReporte().setNumGuia((UtilidadTexto.isEmpty(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getGuia())?"":forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getGuia()));
	    	forma.getDtoFiltroReporte().setNumCarro((UtilidadTexto.isEmpty(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCarro())?"":forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCarro()));
	    	
	    	if(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCuentasBancarias()!=null)
	    	{
	    		forma.getDtoFiltroReporte().setEntidadFinanciera(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCuentasBancarias().getEntidadesFinancieras().getTerceros().getDescripcion());
	        	forma.getDtoFiltroReporte().setTipoNroCuentaBancaria(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCuentasBancarias().getTipoCuentaBancaria().getDescripcion() + 
	    				" - " +	forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCuentasBancarias().getNumCuentaBan());
	    	}
	    	
	    	
			forma.getDtoFiltroReporte().setNombreUsuario(usuarioCompleto.getPersonas().getPrimerNombre()
					+" "+usuarioCompleto.getPersonas().getPrimerApellido()
					+" ("+usuarioCompleto.getLogin()+")"
					);
	    	
	    	String rutaLogo = ins.getLogoJsp();
	    	forma.getDtoFiltroReporte().setRutaLogo(rutaLogo);
	    	
	    	JasperReportBuilder reporte=null;
			
	    	GeneradorReporteEntregaTransportadora generadorReporte =
		            new GeneradorReporteEntregaTransportadora(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega(), forma.isConsulta(), forma.getMostrarParaSeccionEspecial());
	        		reporte = generadorReporte.generarReporte();
	        		nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ImpresionEntregaTransportadora");
	        		forma.getListaNombresReportes().add(nombreArchivo);
	        
	            
			forma.setTipoReporte(ConstantesBD.codigoNuncaValido+"");
	            
			HibernateUtil.endTransaction();
			
		}
		catch (Exception e) {
			Log4JManager.error("ERROR imprimirEntregaTransportadora", e);
			HibernateUtil.abortTransaction();
		}
		
	  }
}