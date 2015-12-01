package com.princetonsa.action.tesoreria;


import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;

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
import util.ValoresPorDefecto;

import com.princetonsa.actionform.tesoreria.CierreTurnoCajaForm;
import com.princetonsa.actionform.tesoreria.MovimientosCajaForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladosCajasRecaudo;
import com.princetonsa.dto.tesoreria.DtoCuadreCaja;
import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.princetonsa.dto.tesoreria.DtoFormaPagoDocSoporte;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.generadorReporte.tesoreria.impresionCierreTurnoCaja.GeneradorReporteCierreTurnoCajaDetallado;
import com.servinte.axioma.generadorReporte.tesoreria.impresionCierreTurnoCaja.GeneradorReporteCierreTurnoCajaResumido;
import com.servinte.axioma.generadorReporte.tesoreria.impresionEntregaCajaMayorPrincipal.GeneradorReporteEntregaCajaMayorPrincDetallado;
import com.servinte.axioma.generadorReporte.tesoreria.impresionEntregaCajaMayorPrincipal.GeneradorReporteEntregaCajaMayorPrincResumido;
import com.servinte.axioma.generadorReporte.tesoreria.impresionTrasladoEntreCajas.GeneradorReporteTrasladoEntreCajas;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaArqueoCierreCajaMundo;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.EntregaCajaMayor;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.SolicitudTrasladoCaja;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IConsultaArqueoCierreCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ISolicitudTrasladoCajaServicio;

/**
 * Action que maneja todo lo relacionado con el anexo:
 * 
 * 228 - Cierre Turno de Caja
 * 
 * Los procesos que son comunes entre los Arqueos (Arqueo caja, Arqueo Entrega parcial 
 * y Cierre Turno de Caja) se encuentran definidos en ComunMovimientosCaja y en MovimientosCajaAction
 *
 * @author Jorge Armando Agudelo Quintero 
 * @see ComunMovimientosCaja
 * @see MovimientosCajaAction
 */
 
public class CierreTurnoCaja extends ComunMovimientosCaja{

	/**
	 * M&eacute;todo que se encarga de procesar las peticiones realizadas desde la opci&oacute;n 
	 * Cierre Turno de Caja
	 *
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param movimientosCaja
	 * @return
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, MovimientosCaja movimientosCaja) {

		
		if(form instanceof CierreTurnoCajaForm)
		{
			ActionErrors errores=new ActionErrors();
			
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			
			CierreTurnoCajaForm forma=(CierreTurnoCajaForm) form;
			String estado=forma.getEstado();

			InstitucionBasica institucionBasica=(InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			if(institucionBasica!=null){
				
				forma.setInstitucionBasica(institucionBasica);
			}
			
			if(estado.equals("generarArqueo")){
				String findForward=null;
				try{
					HibernateUtil.beginTransaction();
				
					findForward = generarArqueo(forma, movimientosCaja, request, usuario.getCodigoInstitucionInt());
					HibernateUtil.endTransaction();
				}
				catch (Exception e) {
					Log4JManager.error(e);
					HibernateUtil.abortTransaction();
				}
				
				return mapping.findForward(findForward);

			}else if(estado.equals("cuadrarCaja") || estado.equals("mostrarInformeFaltanteSobrante")){
				try{
					HibernateUtil.beginTransaction();
				
					continuarCuadreCaja(forma);
					
					if(registrarCuadreCaja(forma)){
						
						forma.getDtoInformacionEntrega().setResponsable(cargarResponsableProceso(movimientosCaja));
						
						diferenciaFormaPagoPorDocumento(forma);
						
						if(estado.equals("cuadrarCaja")){
							
							forma.getDtoInformacionEntrega().setExisteDiferencia(false);
							forma.setEstadoGuardarMovimiento("");
							
						}else if(estado.equals("mostrarInformeFaltanteSobrante")){
							
							if(forma.getDtoInformacionEntrega().isExisteDiferencia()){
								
								forma.setEstadoGuardarMovimiento("");
								
							}else{
								
								// Se habilita el boton para ingresar al registro de solicitud de traslado a caja de recaudo
								forma.setEstadoGuardarMovimiento(ConstantesBD.acronimoNo);
							}
						}
					}else{
						
						errores.add("valor requerido", new ActionMessage("errors.required", "La informacion de la sección Cuadre Caja "));
						saveErrors(request, errores);
						
						forma.setEstado("detenerProceso");
						forma.setMensajeProceso("");
						forma.getDtoInformacionEntrega().setExisteDiferencia(false);
						forma.setEstadoGuardarMovimiento("");
						forma.setEstadoMostrarCuadreCaja("false");
					}
					
					forma.getDtoInformacionEntrega().setRegistroSolicitudTrasladoCaja(false);
					HibernateUtil.endTransaction();
				}
				catch (Exception e) {
					Log4JManager.error(e);
					HibernateUtil.abortTransaction();
				}
				
				return mapping.findForward("seccionCuadreCaja");

			}else if(estado.equals("recargarSeccionCuadrarCaja")){
				
				return mapping.findForward("seccionCuadreCaja");

			}else if(estado.equals("confirmarFaltanteSobrante")){
				
				// Se habilita el boton para ingresar al registro de solicitud de traslado a caja de recaudo
				if(!"SolicitudTrasladoCaja".equals(forma.getEstadoGuardarMovimiento())){
					forma.setEstadoGuardarMovimiento(ConstantesBD.acronimoNo);
				}
	
				return mapping.findForward("seccionCuadreCaja");
			
			}else if (estado.equals("mostrarDetalle")){
				
				return mapping.findForward("detalleFormaPagoCuadreCaja");
			
			}else if (estado.equals("continuarProceso")){
				try{
					HibernateUtil.beginTransaction();
					
					continuarCuadreCaja (forma);
					
					forma.getDtoInformacionEntrega().setResponsable(cargarResponsableProceso (movimientosCaja));
	
					if(!validacionDocumentoDetalle(forma)){
						
						errores.add("valores requeridos", new ActionMessage("errors.required", "La información por cada uno de los documentos asociados al Cierre Turno de Caja "));
						forma.setEstado("detenerProceso");
					}
	
					saveErrors(request, errores);
					
					forma.getDtoInformacionEntrega().setRegistroSolicitudTrasladoCaja(false);
					
					HibernateUtil.endTransaction();
				}
				catch (Exception e) {
					Log4JManager.error(e);
					HibernateUtil.abortTransaction();
				}
				
				return mapping.findForward("detalleFormaPagoCuadreCaja");
				
			}else if(estado.equals("cargarSolicitudTrasladoCaja")){
				
				try{
					HibernateUtil.beginTransaction();
					if(ConstantesBD.acronimoNo.equals(forma.getEstadoGuardarMovimiento()) || forma.getDtoInformacionEntrega().getListadoDtoFormaPagoDocSoportes().size()==0){
	
						cargarSolicitudTraslado(forma, usuario);
	
						forma.getDtoInformacionEntrega().setRegistroSolicitudTrasladoCaja(false);
					
					}else if("SolicitudTrasladoCaja".equals(forma.getEstadoGuardarMovimiento())){
						
						Cajas cajaRecaudo = forma.getDtoInformacionEntrega().getCajaRecaudo();
						
						if(cajaRecaudo!=null){
							
							forma.setCajaRecaudo(cajaRecaudo);
						}
						
						String loginTestigo = forma.getDtoInformacionEntrega().getLoginTestigo();
						
						if(!UtilidadTexto.isEmpty(loginTestigo)){
							
							forma.setTestigoHelper(loginTestigo);
						}
						
						String observacion = forma.getDtoInformacionEntrega().getObservaciones();
						
						if(!UtilidadTexto.isEmpty(observacion)){
							
							forma.setObservacion(observacion);
						}
					}
					
					continuarCuadreCaja (forma);
					
					//forma.setDtoTempInformacionEntrega((DtoInformacionEntrega) UtilidadClonacion.clonar(forma.getDtoInformacionEntrega()));
					forma.setMostrarParaSeccionEspecial("solicitudTrasladoCaja");
					forma.setFormasPagoRegistroSolicitud(false);
					
					HibernateUtil.endTransaction();
				}
				catch (Exception e) {
					Log4JManager.error(e);
					HibernateUtil.abortTransaction();
				}
				
				return mapping.findForward("solicitudTrasladoCaja");

			}else if (estado.equals("guardarSolicitudTrasladoCaja")){
				
				if (!registrarSolicitudTrasladoCaja(forma)){
					
					errores.add("valor requerido", new ActionMessage("errores.modTesoreria.noRegistroSolicitud"));
					
				}else if(!UtilidadTexto.isEmpty(forma.getObservacion()) && 
						forma.getObservacion().length()>128){
	                
					errores.add("valor requerido", new ActionMessage("errors.maxlength", "La información registrada en el campo observación", "128"));
					
				}else if(forma.getCajaRecaudo()==null){
					
					errores.add("valor requerido", new ActionMessage("errors.required", "La Caja de Recaudo "));
					
				}else if(forma.isParametroRequiereTestigo() && forma.getTestigo()==null){ //forma.getDtoInformacionEntrega().getLoginTestigo().equals(ConstantesBD.codigoNuncaValido+""
					
					errores.add("valor requerido", new ActionMessage("errors.required", "El Testigo "));
					
				}else{
					
					if(!UtilidadTexto.isEmpty(forma.getObservacion())){
						
						forma.getDtoInformacionEntrega().setObservaciones(forma.getObservacion());
					}

					forma.getDtoInformacionEntrega().setCajaRecaudo(forma.getCajaRecaudo());
					
					if(forma.getTestigo()!=null){
						
						forma.getDtoInformacionEntrega().setLoginTestigo(forma.getTestigoHelper());
					}
			
					forma.setEstadoGuardarMovimiento("SolicitudTrasladoCaja");
					forma.setMensajeProceso("Se generó el registro de Solicitud de Traslado a Caja de Recaudo. Debe guardar el cierre para confirmar la solicitud de traslado");
					forma.setEstado("generarArqueo");
					forma.setEstadoMostrarCuadreCaja("true");
					forma.getDtoInformacionEntrega().setRegistroSolicitudTrasladoCaja(true);
					return mapping.findForward("busqueda");
				}
				
				saveErrors(request, errores);
	
				return mapping.findForward("solicitudTrasladoCaja");
				
			}else if (estado.equals("volver")){

				forma.setEstado("generarArqueo");
				forma.setEstadoMostrarCuadreCaja("true");
				
//				if("SolicitudTrasladoCaja".equals(forma.getEstadoGuardarMovimiento())){
//					
//					forma.setDtoTempInformacionEntrega((DtoInformacionEntrega) UtilidadClonacion.clonar(forma.getDtoInformacionEntrega()));
//				}
				
				return mapping.findForward("busqueda");
			
			}else if (estado.equals("cerrarTurnoCaja")){
				
				String findForward = "busqueda";
				
				errores = validarCierreTurno(forma);
				
				if (errores.isEmpty()) {
					findForward = cerrarTurnoCaja (forma, usuario.getCodigoInstitucionInt());
					imprimirCierre(forma);
					return mapping.findForward("consultarCierreTurno");
				}
				else{
					saveErrors(request, errores); 
				}
				
				return mapping.findForward(findForward);
				
			}
			else if(estado.equals("cerrarPopUp")){
				
				//forma.setEstado("exitoso");
				return mapping.findForward("busqueda");
				
			}
			else if(estado.equals("imprimirCierre")){
				
				imprimirCierre(forma);
				forma.setListaNombresReportes(new ArrayList<String>());
				return mapping.findForward("consultarCierreTurno");
				
			}else if(estado.equals("impresion")){
				
				return mapping.findForward("impresionCierreTurnoCaja");
				
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
					
					try{
						HibernateUtil.beginTransaction();
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
						forma.setEstado("exitoso");
						HibernateUtil.endTransaction();
					}
					catch (Exception e) {
						Log4JManager.error(e);
						HibernateUtil.abortTransaction();
					}
					return mapping.findForward("consultarCierreTurno");
				}
			}
			else if(estado.equals("imprimirEntregaCajaMayorPrincipal")){
				
				int codigoInstitucion = usuario.getCodigoInstitucionInt();
				
				imprimirEntregaCajaMayor(forma, codigoInstitucion);
				this.cargarTiposReporteEntregaCajaMayPrinc(forma);
				forma.setListaNombresReportes(new ArrayList<String>());
				return mapping.findForward("consultarEntregaCajaMayorPrincipal");
				
			}else if(estado.equals("imprimirSolicitudTrasladoCaja")){
				try{
					HibernateUtil.beginTransaction();
					imprimirSolicitudTrasladoCajaRecaudo(forma, usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
					HibernateUtil.endTransaction();
				}
				catch (Exception e) {
					Log4JManager.error(e);
					HibernateUtil.abortTransaction();
				}
				
				return mapping.findForward("consultarSolicitudTrasladoCaja");
				
			}else if (estado.equals("recargarSolicitudTraslado")) {
				
				return mapping.findForward("solicitudTrasladoCaja");
			}
			else if(estado.equals("impresionTrasladoEntreCajas")){
				try{
					HibernateUtil.beginTransaction();
					this.consultaImpresionSolicitudTrasladoCajaRecaudo(forma, usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
					this.imprimirSolicitudTrasladoEntreCajas(forma, request, usuario, institucionBasica);
					forma.setEstado("exitoso");
					HibernateUtil.endTransaction();
				}
				catch (Exception e) {
					Log4JManager.error(e);
					HibernateUtil.abortTransaction();
				}
				return mapping.findForward("consultarCierreTurno");
			}
			else{
				
				errores.add("estado invalido", new ActionMessage("errors.estadoInvalido"));
				errores.add("valor requerido", new ActionMessage("errors.usuarioSinRolFuncionalidad",usuario.getNombreUsuario(), "esta funcionalidad"));
				saveErrors(request, errores);
			}
		}
		
		return mapping.findForward("paginaError");
	}

	/**
	 * Este Método se encarga de
	 * @param forma
	 * @return
	 * @author Yennifer Guerrero
	 */
	private ActionErrors validarCierreTurno(CierreTurnoCajaForm forma) {
		
		ActionErrors errores = new ActionErrors();
		
		if(!UtilidadTexto.isEmpty(forma.getDtoInformacionEntrega().getObservacionesAceptacion()) && 
				forma.getDtoInformacionEntrega().getObservacionesAceptacion().length()>128){
            
			errores.add("valor requerido", new ActionMessage("errors.maxlength", "La información registrada en el campo observación", "128"));
			
			forma.setEstado("generarArqueo");
			
		} else if(!forma.getDtoInformacionEntrega().isRegistroSolicitudTrasladoCaja()){
			
			ArrayList<DtoCuadreCaja> formaPagoCierre = forma.getConsolidadoMovimientoDTO().getCuadreCajaDTOs();
			List<String> formaPagoReqTraslado = new ArrayList<String>();
			
			for (DtoCuadreCaja dtoCuadreCaja : formaPagoCierre) {
				if (dtoCuadreCaja.getReqTrasladoCajaRecaudo().equals(ConstantesBD.acronimoSiChar)
						&&!dtoCuadreCaja.getValorBaseEnCaja().equals(dtoCuadreCaja.getValorSistema())) {
					formaPagoReqTraslado.add(dtoCuadreCaja.getFormaPago());
				}
			}
			
			if (formaPagoReqTraslado != null && formaPagoReqTraslado.size() > 0) {
				
				String formasPago = "";
				for (int i = 0; i < formaPagoReqTraslado.size(); i++) {
					formasPago += formaPagoReqTraslado.get(i);
					
					if (i< (formaPagoReqTraslado.size() - 1) && formaPagoReqTraslado.size() > 1) {
						formasPago += ", ";
					}
				}
				
				forma.setEstado("generarArqueo");
				forma.setTipoError("cierreTurno");
				errores.add("valor requerido", new ActionMessage("errors.formaPagoRequeridas", formasPago));
				
			}
		}
		
		return errores;
	}
	
	/**
	 * Método que se encarga de cargar los tipos de reporte a imprimir
	 * 
	 * @param form
	 * @author Fabián Becerra
	 */
	private void cargarTiposReporte(CierreTurnoCajaForm form, String[] listadoTiposReporte){
		
		form.setListadoTiposReporte(new ArrayList<DtoIntegridadDominio>());
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<DtoIntegridadDominio> listaTiposReporte=Utilidades.generarListadoConstantesIntegridadDominio(
				con, listadoTiposReporte, false);
		
		UtilidadBD.closeConnection(con);
		
		form.setListadoTiposReporte(listaTiposReporte);
		
	}
	
	/**
	 * M&eacute;todo que se encarga de imprimir la Entrega a Caja Mayor generada en el proceso de cierre.
	 * 
	 * @param forma
	 * @param codigoInstitucion
	 */
	private void consultaImpresionEntregaCajaMayor(CierreTurnoCajaForm forma, InstitucionBasica ins) {
		
		//CONSULTAR LA INFORMACION
		EntregaCajaMayor entregaCajaMayor = forma.getCierreTurnoCaja().getEntregaCajaMayorByCodigoPk();
		
		if(entregaCajaMayor!=null && entregaCajaMayor.getMovimientosCajaByCodigoPk().getCodigoPk() > 0){
			
			long codigoEntrega = entregaCajaMayor.getMovimientosCajaByCodigoPk().getCodigoPk();
			try{
				HibernateUtil.beginTransaction();
				IConsultaArqueoCierreCajaMundo consultaArqueoCierreCajaMundo = TesoreriaFabricaMundo.crearConsultaArqueoCierreCajaMundo();
				DtoInformacionEntrega dtoInfo=consultaArqueoCierreCajaMundo.consultarArqueoParcialPorEntrega(codigoEntrega, ETipoMovimiento.ENTREGA_CAJA_MAYOR_PRINCIPAL, ins.getCodigoInstitucionBasica());
				
				forma.getDtoInformacionEntrega().setListadoDtoFormaPagoDocSoportes(dtoInfo.getListadoDtoFormaPagoDocSoportes());
				
				recargarTotalesPorFormasPago(forma);
				forma.setConsulta(true);
				forma.setEstadoGuardarMovimiento("EntregaCajaMayorPrincipal");
				
				HibernateUtil.endTransaction();
			}
			catch (Exception e) {
				Log4JManager.error(e);
				HibernateUtil.abortTransaction();
			}

		}else if(forma.getDtoInformacionEntrega()!=null){
			
			forma.getDtoInformacionEntrega().setListadoDtoFormaPagoDocSoportes(new ArrayList<DtoFormaPagoDocSoporte>());
		}
	}
	
	/**
	 * Método que se encarga de cargar los tipos de reporte de entrega caja mayor a imprimir
	 * 
	 * @param form
	 * @author Fabián Becerra
	 */
	private void cargarTiposReporteEntregaCajaMayPrinc(CierreTurnoCajaForm form){
		
		String[] listadoTipoRep = new String[]{ConstantesIntegridadDominio.acronimoTipoReporteImpEntregaCajaMayPrincResumido,
													ConstantesIntegridadDominio.acronimoTipoReporteImpEntregaCajaMayPrincDetallado,
													ConstantesIntegridadDominio.acronimoTipoReporteAmbos};
		
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<DtoIntegridadDominio> listadoTiposReporte=Utilidades.generarListadoConstantesIntegridadDominio(
				con, listadoTipoRep, false);
		
		UtilidadBD.closeConnection(con);
		
		form.setListadoTiposReporte(listadoTiposReporte);
		
	}
	
	private void imprimirCierreTurnoCaja(CierreTurnoCajaForm forma,  HttpServletRequest request, UsuarioBasico usuario, InstitucionBasica ins) {
        
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
        	forma.getDtoFiltroReporte().setCajaMayorPrincipal("("+forma.getDtoInformacionEntrega().getMovimientosCaja().getDetalleMovCajaCierre().getCajas().getCodigo()
        			+") " + forma.getDtoInformacionEntrega().getMovimientosCaja().getDetalleMovCajaCierre().getCajas().getDescripcion()
        			+ " - "+forma.getDtoInformacionEntrega().getMovimientosCaja().getDetalleMovCajaCierre().getCajas().getCentroAtencion().getDescripcion());
        	
			forma.getDtoFiltroReporte().setNombreUsuario(usuarioCompleto.getPersonas().getPrimerNombre()
					+" "+usuarioCompleto.getPersonas().getPrimerApellido()
					+" ("+usuarioCompleto.getLogin()+")"
					);
        	
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
	}


	/**
	 * Proceso que recopila toda la informaci&oacute;n necesaria para realizar el movimiento de cierre de turno de caja
	 * 
	 * @param forma
	 * @param movimientosCaja
	 * @param request
	 * @param codigoInstitucion
	 * @return
	 */
	private String generarArqueo (CierreTurnoCajaForm forma, MovimientosCaja movimientosCaja, HttpServletRequest request, int codigoInstitucion){
		
		IMovimientosCajaServicio movimientosCajaServicio = getMovimientosCajaServicio();
		
		DtoConsolidadoMovimiento consolidadoMovimientoDTO = movimientosCajaServicio.obtenerConsolidadoMovimiento(movimientosCaja);
		
		consolidadoMovimientoDTO.setETipoMovimiento(ETipoMovimiento.CIERRE_CAJA);
		
		forma.setDtoInformacionEntrega(movimientosCajaServicio.consolidadoInformacionEntrega(movimientosCaja, ETipoMovimiento.CIERRE_CAJA)); // true
		
		forma.setConsolidadoMovimientoDTO(consolidadoMovimientoDTO);
		
		boolean existenDocumentos = consolidadoMovimientoDTO.getNumeroTotalDocumentos() > 0 ? true : false;
		
		forma.setExistenDocumentos(existenDocumentos);
		
		//forma.getDtoInformacionEntrega().setCuadreCajaDTOs(consolidadoMovimientoDTO.getCuadreCajaDTOs());
		
		forma.setCajaMayorPrincipal(((MovimientosCajaForm)(request.getSession().getAttribute("MovimientosCajaForm"))).getCajaMayorPrincipal());

		String valorInstitucion = ValoresPorDefecto.getInstitucionManejaTrasladoOtraCajaRecaudo(codigoInstitucion);

		if(valorInstitucion.equals(ConstantesBD.acronimoSi)){
			
			forma.setManejaTrasladoCajaRecaudo(true);
		}
		
		return "busqueda";
	}
	
	
	/**
	 * M&eacute;todo utilizado para cargar la informaci&oacute;n necesaria para realizar la solicitud de traslado a caja de recaudo
	 */
	private void cargarSolicitudTraslado (CierreTurnoCajaForm forma, UsuarioBasico usuario){
		
		ICajasServicio cajasServicio = TesoreriaFabricaServicio.crearCajasServicio();
		IMovimientosCajaServicio movimientosCajaServicio = getMovimientosCajaServicio();
		
		forma.setListadoCajasRecaudo((ArrayList<Cajas>) cajasServicio.listarCajasPorCentrosAtencionPorTipoCaja(usuario.getCodigoCentroAtencion(), ConstantesBD.codigoTipoCajaRecaudado));
		forma.setCajaRecaudo(null);
		
		boolean requiereTestigo = UtilidadTexto.getBoolean(ValoresPorDefecto
					.getEsRequeridoTestigoSolicitudAceptacionTrasladoCaja(usuario.getCodigoInstitucionInt()));
		
		forma.setListadoTestigos((ArrayList<DtoUsuarioPersona>)movimientosCajaServicio.obtenerUsuariosActivosDiferenteDe(usuario.getCodigoInstitucionInt(), usuario.getLoginUsuario(), false));
		forma.setTestigo(null);
		
		forma.setObservacion("");
		forma.setParametroRequiereTestigo(requiereTestigo);
		forma.getDtoInformacionEntrega().setLoginTestigo(ConstantesBD.codigoNuncaValido+"");
		forma.getDtoInformacionEntrega().setObservaciones("");
		forma.setEstadoGuardarMovimiento(ConstantesBD.acronimoNo);
		forma.setMensajeProceso("No se generó el registro de Solicitud de Traslado a Caja de Recaudo");
	}
	
	
	/**
	 * Recalcula los valores por forma de pago que se realizaron en la
	 * Entrega a Caja Mayor / Principal en el movimiento de Cierre de Turno de Caja
	 * 
	 * @param forma
	 */
	private void recargarTotalesPorFormasPago (CierreTurnoCajaForm forma) {
		
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
	
	/*
	 *  private boolean cargarDTOCuadreCajaParaSolicitud(CierreTurnoCajaForm forma){
    	
    	boolean registrarSolicitudTrasladoCaja = false;
    	
 		forma.getDtoInformacionEntrega().setRegistroEfectivo(false);
 		
 		//forma.getDtoInformacionEntrega().getCuadreCajaDTOs().clear();
 		
 		for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte : forma.getDtoInformacionEntrega().getListadoDtoFormaPagoDocSoportes()) {
 		
 			if(ConstantesBD.acronimoSi.equalsIgnoreCase(dtoFormaPagoDocSoporte.getIndicativoTrasladoCajaRecaudo())){

 				if(dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorCaja()>0){
	 				
 					if(dtoFormaPagoDocSoporte.getCodigoTipoDetalleFormaPago() == ConstantesBD.codigoTipoDetalleFormasPagoNinguno){

	 					if(dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorCaja()>dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorBaseEnCaja()){
	 						
	 						registrarSolicitudTrasladoCaja = true;
	 						forma.getDtoInformacionEntrega().setRegistroEfectivo(true);
	 					}
 					}else {
	 					registrarSolicitudTrasladoCaja = true;
	 				}
 				}
 			}
 		}
 		
 		return registrarSolicitudTrasladoCaja;
    }
	 */
	
	/**
	 * M&eacute;todo que determina si es posible o no el registro de la solicitud de traslado a caja de recaudo que se esta
	 * realizando en el movimiento de caja Cierre Turno de Caja
	 * 
	 * @return boolean que determina si es posible o no el registro de la solicitud de traslado a caja de recaudo
	 */
    private boolean registrarSolicitudTrasladoCaja(CierreTurnoCajaForm forma){
	
    	forma.getDtoInformacionEntrega().setRegistroSolicitudTrasladoCaja(false);
 		
 		for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte : forma.getDtoInformacionEntrega().getListadoDtoFormaPagoDocSoportes()) {
 		
 			if(ConstantesBD.acronimoSi.equalsIgnoreCase(dtoFormaPagoDocSoporte.getIndicativoTrasladoCajaRecaudo())
 					&& dtoFormaPagoDocSoporte.getRegistrarTraslado().equals(ConstantesBD.acronimoSi)){

 				if(dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorCaja()>0){
 					
 					if(dtoFormaPagoDocSoporte.getCodigoTipoDetalleFormaPago() == ConstantesBD.codigoTipoDetalleFormasPagoNinguno){

	 					if(dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorCaja()>dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorBaseEnCaja()){
	 						
	 						return true; 
	 					}
	 					
 					}else{
 						
 						return true;
 					}
 				}
 			}
 		}

 		return false;
    }
     

//	/**
//	 * Actualiza las formas de pago segun el DTOCuadreCaja
//	 * 
//	 * @param forma
//	 */
//	private void actualizarFormasPagoCierre(CierreTurnoCajaForm forma) {
//		
//		DtoInformacionEntrega dtoInformacionEntrega = new DtoInformacionEntrega();
//		
//		for (DtoCuadreCaja dtoCuadreCaja : forma.getConsolidadoMovimientoDTO().getCuadreCajaDTOs()) {
//			
//			for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte : forma.getDtoInformacionEntrega().getListadoDtoFormaPagoDocSoportes()) {
//
//				if(dtoCuadreCaja.getTipoFormaPago() == dtoFormaPagoDocSoporte.getConsecutivoFormaPago() && dtoCuadreCaja.getValorDiferencia()!=0){
//					
//					dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes().add(dtoFormaPagoDocSoporte);
//				}	
//			}
//		}
//
//		forma.setDtoInformacionEntrega(dtoInformacionEntrega);
//	}
	
	
	/**
	 * 
	 * Recorre cada uno de los cuadres de caja por forma de pago para determinar si se gener&oacute; una diferencia a nivel de documento
	 * 
	 * @param forma
	 * @return
	 */
	private void diferenciaFormaPagoPorDocumento (CierreTurnoCajaForm forma){
	
		Log4JManager.info("el tamaño antes de la diferencia es de " + forma.getDtoInformacionEntrega().getCuadreCajaDTOs().size());
		
		forma.getDtoInformacionEntrega().getCuadreCajaDTOs().clear();

		for (DtoFormaPagoDocSoporte formaPagoDocSoporte : forma.getDtoInformacionEntrega().getListadoDtoFormaPagoDocSoportes()) {
			
			formaPagoDocSoporte.getDtoCuadreCaja().setAlerta("");
			
			if(formaPagoDocSoporte.getDtoCuadreCaja().getTipodiferencia()==null && !"".equals(formaPagoDocSoporte.getDtoCuadreCaja().getTipodiferencia())){
				
				for (DtoEntidadesFinancieras entidadFinanciera : formaPagoDocSoporte.getListadoDtoEntidadesFinancieras()) {
					
					for (DtoDetalleDocSopor dtoDetalleDocSopor : entidadFinanciera.getListadoDtoDetDocSoporte()) {
						
						// Si se cumple está condición no es necesario seguir evaluando mas documentos para esta forma de pago
						if(dtoDetalleDocSopor.getValorDiferencia() !=null && dtoDetalleDocSopor.getValorDiferencia().doubleValue() < 0){
							
							formaPagoDocSoporte.getDtoCuadreCaja().setAlerta("Diferencia generada en Documento");
							break;
						}
					}
					
					// Si se cumple está condición no es necesario seguir evaluando mas entidades para esta forma de pago
					if(!"".equals(formaPagoDocSoporte.getDtoCuadreCaja().getAlerta())){ 
						break;
					}
				}
			}
			
			forma.getDtoInformacionEntrega().getCuadreCajaDTOs().add(formaPagoDocSoporte.getDtoCuadreCaja());
		}
		
		Log4JManager.info("el tamaño despues de la diferencia es de " + forma.getDtoInformacionEntrega().getCuadreCajaDTOs().size());
	}

	
	
	/**
	 * Retorna un boolena indicando si es posible realizar el registro completo de informaci&oacute;n
	 * por forma de pago en el detalle
	 * 
	 * @param forma
	 * @return
	 */
	private boolean validacionDocumentoDetalle(CierreTurnoCajaForm forma) {
		
		boolean registroDetalle = true;
		int consecutivoFormaPagoSeleccionada = ConstantesBD.codigoNuncaValido;
		
		if(!"".equals(forma.getConsecutivoFormaPagoSeleccionada())){
			
			consecutivoFormaPagoSeleccionada = Integer.parseInt(forma.getConsecutivoFormaPagoSeleccionada());
		
			for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte: forma.getDtoInformacionEntrega().getListadoDtoFormaPagoDocSoportes()) 
			{
				if(dtoFormaPagoDocSoporte.getConsecutivoFormaPago() == consecutivoFormaPagoSeleccionada){
					
					for (DtoEntidadesFinancieras dtoEntidadesFinancieras : dtoFormaPagoDocSoporte.getListadoDtoEntidadesFinancieras()) 
					{
						for (DtoDetalleDocSopor dtoDetalleDocSopor : dtoEntidadesFinancieras.getListadoDtoDetDocSoporte()) 
						{
							if(dtoDetalleDocSopor.getIndicativoNoRecibido().equals(ConstantesBD.acronimoNo))
							{
								if(dtoDetalleDocSopor.getValorActual() == null || "".equals(dtoDetalleDocSopor.getNroDocumentoRecibido()))
								{
									return registroDetalle = false ;
								}
							}
						}
					}
				}
			}
		}else{
			
			registroDetalle = false ;
		}
		
		return registroDetalle;
	}
	
	
	/**
	 * Analiza la informaci&oacute;n ingresada desde la presentaci&oacute;n 
	 * y cambia el estado para continuar de diligenciar la informaci&oacute;n correspondiente
	 * al Cuadre de Caja.
	 * 
	 * @param forma
	 * @return
	 */
	private void continuarCuadreCaja(CierreTurnoCajaForm forma) {

		IMovimientosCajaServicio movimientosCajaServicio = getMovimientosCajaServicio();
		
		forma.setDtoInformacionEntrega(movimientosCajaServicio.calcularValoresParaArqueos(forma.getDtoInformacionEntrega(), ETipoMovimiento.CIERRE_CAJA));
		
		forma.setEstado("continuarProceso");
		forma.setEstadoMostrarCuadreCaja("true");
	}
	
	
	/**
	 * M&eacute;todo que carga la informaci&oacute;n del responsable del proceso de Cierre de Turno de Caja.
	 * 
	 * @param movimientoCaja
	 * @return {@link DtoUsuarioPersona} con la informaci&oacute;n del responsable del proceso de Cierre de Turno de Caja.
	 */ 
	private DtoUsuarioPersona cargarResponsableProceso (MovimientosCaja movimientosCaja){
		
		DtoUsuarioPersona responsable = new DtoUsuarioPersona();
		responsable.setLogin(movimientosCaja.getTurnoDeCaja().getUsuarios().getLogin());
		responsable.setNombre(movimientosCaja.getTurnoDeCaja().getUsuarios().getPersonas().getPrimerNombre() + " " + movimientosCaja.getTurnoDeCaja().getUsuarios().getPersonas().getSegundoNombre());
		responsable.setApellido(movimientosCaja.getTurnoDeCaja().getUsuarios().getPersonas().getPrimerApellido() + " " + movimientosCaja.getTurnoDeCaja().getUsuarios().getPersonas().getSegundoApellido());
		
		//forma.getDtoInformacionEntrega().setResponsableEntrega(responsableEntrega);
		
		return responsable;
	}
	
	
	/**
	 * Determina si se registr&oacute; al menos un valor por forma de pago en la secci&oacute;n cuadre de 
	 * caja, la cual es requerida para este movimiento
	 * 
	 * @param forma
	 * @return
	 */
	
	private boolean registrarCuadreCaja(CierreTurnoCajaForm forma){

		for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte : forma.getDtoInformacionEntrega().getListadoDtoFormaPagoDocSoportes()) {
			
			if(!dtoFormaPagoDocSoporte.isRegistrarEnCierreTurno()){
				
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * M&eacute;todo que se encarga de realizar todo el proceso de Cierre de Turno de Caja
	 * 
	 * @param forma
	 * @return
	 */
	private String cerrarTurnoCaja(CierreTurnoCajaForm forma, int codigoInstitucion) {
		
		boolean resultadoProceso = false;
		forma.setEstado("NoExitoso");
		
		if(forma.getDtoInformacionEntrega().isExisteDiferencia() && !forma.isExisteConsecutivoFaltante()){ //!consecutivoFaltanteSobranteParametrizado(codigoInstitucion)

			forma.setMensajeProceso("Falta definir consecutivo de Faltantes / Sobrantes. No se puede continuar con el cierre de turno de caja");
		
			return "busqueda";
			
		}else{

			if(!UtilidadTexto.isEmpty(forma.getDtoInformacionEntrega().getLoginTestigo())){
				
				//Aca se debe hacer el llamado al proceso de confirmacion de contraseña
			}
				
			// Sigue con el proceso como si no existiera testigo o como si se hubiese logueado exitosamente
			UtilidadTransaccion.getTransaccion().begin();
			if(registrarCuadreCaja(forma)){
				
				IMovimientosCajaServicio movimientosCajaServicio = getMovimientosCajaServicio();
				
				BigDecimal consecutivoSolicitudTraslado = null;
				
				BigDecimal consecutivoCierre=new BigDecimal(Utilidades.convertirADouble(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoCierreCaja, codigoInstitucion)));

				forma.getDtoInformacionEntrega().getMovimientosCaja().setConsecutivo(consecutivoCierre.longValue());
				forma.getDtoInformacionEntrega().setETipoMovimiento(ETipoMovimiento.CIERRE_CAJA);
				forma.getDtoInformacionEntrega().setCajaMayorPrincipal(forma.getCajaMayorPrincipal());
				
				if(forma.getDtoInformacionEntrega().isRegistroSolicitudTrasladoCaja()){
					
					consecutivoSolicitudTraslado = new BigDecimal(Utilidades.convertirADouble(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoTrasladoCajas, codigoInstitucion)));
				
					SolicitudTrasladoCaja solicitudTrasladoCaja = new SolicitudTrasladoCaja();
					
					MovimientosCaja movimientoCajaSolicitud =  new MovimientosCaja();
					
					movimientoCajaSolicitud.setConsecutivo(consecutivoSolicitudTraslado.longValue());
					
					solicitudTrasladoCaja.setMovimientosCajaByCodigoPk(movimientoCajaSolicitud);
					
					forma.getDtoInformacionEntrega().getMovimientosCaja().setSolicitudTrasladoCajaByCodigoPk(solicitudTrasladoCaja);
				}

				boolean registrado = movimientosCajaServicio.guardarMovimientoCaja(forma.getDtoInformacionEntrega());
				
				if(registrado){
					Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
					// si se insertó bien entonces se cambia el uso de los consecutivos a Si, Si
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH, ConstantesBD.nombreConsecutivoCierreCaja, codigoInstitucion, consecutivoCierre.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
					
					if(consecutivoSolicitudTraslado!=null){
						UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH, ConstantesBD.nombreConsecutivoTrasladoCajas, codigoInstitucion, consecutivoSolicitudTraslado.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
					}
					
					resultadoProceso = true;
				
				}else{
					Connection con=UtilidadBD.abrirConexion();
					// si no se insertó, entonces se cambia el uso de los consecutivos a No, No
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoCierreCaja, codigoInstitucion, consecutivoCierre.toString(), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
					
					if(consecutivoSolicitudTraslado!=null){
						UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoTrasladoCajas, codigoInstitucion, consecutivoSolicitudTraslado.toString(), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
					}
					UtilidadBD.closeConnection(con);
					resultadoProceso = false;
				}
				
				/* MT 1714 - Se comenta esta linea ya que siempre estaba haciendo commit así ocurrieran errores en el proceso.
				 * Generando así que los consecutivos quedaran descuadrados por no cambiarles el estado. Cristhian Murillo*/
				//resultadoProceso = true; MT 1714
			
			}else{
				// no se puede guardar por que no se ha registrado informacion para las formas de pago
			}
		
			if(resultadoProceso){
				
				forma.setEstado("exitoso");
				TurnoDeCaja turnoDeCaja = forma.getConsolidadoMovimientoDTO().getMovimientosCaja().getTurnoDeCaja();
				forma.setMensajeProceso("Proceso de cierre de turno de caja : ("+turnoDeCaja.getCajas().getConsecutivo() + ") " + turnoDeCaja.getCajas().getDescripcion() +" concluido satisfactoriamente");
				forma.setCierreTurnoCaja(forma.getDtoInformacionEntrega().getMovimientosCaja());
				UtilidadTransaccion.getTransaccion().commit();
				
			}else{
	
				forma.setMensajeProceso("Proceso de Cierre de Turno Caja No se concluyó satisfactoriamente");
				UtilidadTransaccion.getTransaccion().rollback();
			}
		}
		return "busqueda";
	}

	
	
	/**
	 * M&eacute;todo que se encarga de imprimir el cierre de turno de caja generado
	 * @param forma
	 */
	private void imprimirCierre(CierreTurnoCajaForm forma) {
		try{
			HibernateUtil.beginTransaction();
			forma.setConsulta(true);
	
			IConsultaArqueoCierreCajaServicio consultaArqueoCierreCajaServicio = TesoreriaFabricaServicio.crearConsultaArqueoCierreCajaServicio();
			DtoInformacionEntrega dtoInformacionEntrega = consultaArqueoCierreCajaServicio.consultarCierreTurnoCaja(forma.getCierreTurnoCaja());
			
			if(dtoInformacionEntrega!=null){
				
				forma.setDtoInformacionEntrega(dtoInformacionEntrega);
			}
			HibernateUtil.endTransaction();
		}
		catch (Exception e) {
			Log4JManager.error("ERROR imprimir cierre",e);
			HibernateUtil.abortTransaction();
		}
	}
	
	/**
	 * M&eacute;todo que se encarga de imprimir la Entrega a Caja Mayor generada en el proceso de cierre.
	 * 
	 * @param forma
	 * @param codigoInstitucion
	 */
	private void imprimirEntregaCajaMayor(CierreTurnoCajaForm forma, int codigoInstitucion) {
		
		EntregaCajaMayor entregaCajaMayor = forma.getCierreTurnoCaja().getEntregaCajaMayorByCodigoPk();
		
		if(entregaCajaMayor!=null && entregaCajaMayor.getMovimientosCajaByCodigoPk().getCodigoPk() > 0){
			try{
				HibernateUtil.beginTransaction();
				long codigoEntrega = entregaCajaMayor.getMovimientosCajaByCodigoPk().getCodigoPk();
				IConsultaArqueoCierreCajaMundo consultaArqueoCierreCajaMundo = TesoreriaFabricaMundo.crearConsultaArqueoCierreCajaMundo();
				forma.setDtoInformacionEntrega(consultaArqueoCierreCajaMundo.consultarArqueoParcialPorEntrega(codigoEntrega, ETipoMovimiento.ENTREGA_CAJA_MAYOR_PRINCIPAL, codigoInstitucion));
				recargarTotalesPorFormasPago(forma);
				forma.setConsulta(true);
				forma.setEstadoGuardarMovimiento("EntregaCajaMayorPrincipal");
				HibernateUtil.endTransaction();
			}
			catch (Exception e) {
				Log4JManager.error(e);
				HibernateUtil.endTransaction();
			}
		}else if(forma.getDtoInformacionEntrega()!=null){
			
			forma.getDtoInformacionEntrega().setListadoDtoFormaPagoDocSoportes(new ArrayList<DtoFormaPagoDocSoporte>());
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
	private void imprimirSolicitudTrasladoCajaRecaudo(CierreTurnoCajaForm forma, int codigoInstitucion, int consecutivoCentroAtencion) {
		
		ISolicitudTrasladoCajaServicio solicitudTrasladoServicio = TesoreriaFabricaServicio.crearSolicitudTrasladoCaja();
		IMovimientosCajaServicio movimientosCajaServicio 	= getMovimientosCajaServicio();

		if(forma.getCierreTurnoCaja().getSolicitudTrasladoCajaByCodigoPk()!=null){
			
			long consecutivoSolicitud = forma.getCierreTurnoCaja().getSolicitudTrasladoCajaByCodigoPk().getMovimientosCajaByCodigoPk().getConsecutivo();
			
			Log4JManager.info("------------------ consecutivo solicitud:-" + consecutivoSolicitud);
			
			DtoConsultaTrasladosCajasRecaudo dtoConsultaTrasladosCajasRecaudo = new DtoConsultaTrasladosCajasRecaudo();
			dtoConsultaTrasladosCajasRecaudo.setConsecutivoSolicitud(consecutivoSolicitud);
			dtoConsultaTrasladosCajasRecaudo.setConsecutivoCentroAtencion(consecutivoCentroAtencion);
			
			ArrayList<DtoConsultaTrasladosCajasRecaudo> listaSolicitud = solicitudTrasladoServicio.consultarRegistrosSolicitudTrasladoCaja(dtoConsultaTrasladosCajasRecaudo);
			
			if(listaSolicitud!=null && listaSolicitud.size()>0){
				
				dtoConsultaTrasladosCajasRecaudo = listaSolicitud.get(0);
				forma.setDtoConsultaTrasladosCajasRecaudo(dtoConsultaTrasladosCajasRecaudo);
				
				DtoInformacionEntrega dtoInformacionEntrega = movimientosCajaServicio.consolidarInformacionSolicitudTrasCajaRealizada(codigoInstitucion, dtoConsultaTrasladosCajasRecaudo.getCodigoPk());
				
				Log4JManager.info("------------------ tamaño del listado de la solicitud:- " + dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes().size());
				
				forma.setDtoInformacionEntrega(dtoInformacionEntrega);
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
	 * Retorna la implementaci&oacute;n del servicio de Movimientos de Caja
	 * @return IMovimientosCajaServicio - Implementaci&oacute;n del servicio de Movimientos de Caja
	 */
	private IMovimientosCajaServicio getMovimientosCajaServicio() {
		
		return TesoreriaFabricaServicio.crearMovimientosCajaServicio();
	}
	
	/**
	 * Método que genera el reporte resumido, detallado o ambos de entrega caja mayor principal DCU 1039
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param ins
	 */
	private void imprimirEntregaCajaMayPrinc(CierreTurnoCajaForm forma,  HttpServletRequest request, UsuarioBasico usuario, InstitucionBasica ins) {
		
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
	    	String rutaLogo = ins.getLogoJsp();
	    	forma.getDtoFiltroReporte().setRutaLogo(rutaLogo);
	    	
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

	}
	
	/**
	 * M&eacute;todo que se encarga de realizar la impresi&oacute;n de la Solicitud de Traslado a Caja de Recaudo
	 * asociada al movimiento de Cierre de Turno de Caja.
	 * 
	 * @param forma
	 * @param codigoInstitucion
	 * @param consecutivoCentroAtencion
	 */
	private void consultaImpresionSolicitudTrasladoCajaRecaudo(CierreTurnoCajaForm forma, int codigoInstitucion, int consecutivoCentroAtencion) {
		
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
				forma.setConsulta(true);
			}
		}else if(forma.getDtoInformacionEntrega()!=null){
			
			forma.getDtoInformacionEntrega().setListadoDtoFormaPagoDocSoportes(new ArrayList<DtoFormaPagoDocSoporte>());
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
	private void imprimirSolicitudTrasladoEntreCajas(CierreTurnoCajaForm forma,  HttpServletRequest request, UsuarioBasico usuario, InstitucionBasica ins) {
        
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
    	String rutaLogo = ins.getLogoJsp();
    	forma.getDtoFiltroReporte().setRutaLogo(rutaLogo);
    	
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
    	
    	JasperReportBuilder reporte=null;
    	GeneradorReporteTrasladoEntreCajas generadorReporte =
            new GeneradorReporteTrasladoEntreCajas(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega(), forma.getDtoConsultaTrasladosCajasRecaudo());
		reporte = generadorReporte.generarReporte();
		nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ImpresionTrasladoEntreCajas");
		forma.getListaNombresReportes().add(nombreArchivo);
        
		forma.setTipoReporte(ConstantesBD.codigoNuncaValido+"");
	            
	  }
}
