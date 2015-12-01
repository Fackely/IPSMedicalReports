package com.princetonsa.action.tesoreria;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

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
import util.clonacion.UtilidadClonacion;

import com.princetonsa.actionform.tesoreria.ArqueoEntregaParcialForm;
import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoCuadreCaja;
import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.princetonsa.dto.tesoreria.DtoEntregaCaja;
import com.princetonsa.dto.tesoreria.DtoFormaPagoDocSoporte;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.generadorReporte.tesoreria.impresionEntregaATransportadora.GeneradorReporteEntregaTransportadora;
import com.servinte.axioma.generadorReporte.tesoreria.impresionEntregaCajaMayorPrincipal.GeneradorReporteEntregaCajaMayorPrincDetallado;
import com.servinte.axioma.generadorReporte.tesoreria.impresionEntregaCajaMayorPrincipal.GeneradorReporteEntregaCajaMayorPrincResumido;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento;
import com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.CuentasBancarias;
import com.servinte.axioma.orm.EntregaCajaMayor;
import com.servinte.axioma.orm.EntregaTransportadora;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.Terceros;
import com.servinte.axioma.orm.TransportadoraValores;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.orm.delegate.UsuariosDelegate;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.TesoreriaFabricaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICajasServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IConsultaArqueoCierreCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ICuentasBancariasServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IEntidadesFinancierasServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.IMovimientosCajaServicio;
import com.servinte.axioma.servicio.interfaz.tesoreria.ITransportadoraValoresServicio;

/**
 * Action que maneja todo lo relacionado con los anexos:
 * 
 * 227 - Arqueo Entrega Parcial
 * 905 - Registro Entrega a Transportadora de Valores
 * 906 - Entrega a Caja Mayor/Principal
 * 
 * Los procesos que son comunes entre los Arqueos (Arqueo caja, Arqueo Entrega parcial 
 * y Cierre Turno de Caja) se encuentran definidos en ComunMovimientosCaja y en MovimientosCajaAction
 *
 * @author Jorge Armando Agudelo Quintero 
 * @see ComunMovimientosCaja
 * @see MovimientosCajaAction
 */
 
public class ArqueoEntregaParcial extends ComunMovimientosCaja{

	/**
	 * M&eacute;todo que se encarga de procesar las peticiones realizadas desde la opci&oacute;n 
	 * Arqueo Entrega Parcial
	 *
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param movimientosCaja
	 * @return
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response, MovimientosCaja movimientosCaja) {

		if(form instanceof ArqueoEntregaParcialForm)
		{			
			ActionErrors errores=new ActionErrors();
			
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		
			ArqueoEntregaParcialForm forma=(ArqueoEntregaParcialForm) form;

			String estado=forma.getEstado();
			
			InstitucionBasica institucionBasica=(InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			if(institucionBasica!=null){
				
				forma.setInstitucionBasica(institucionBasica);
			}

			if(estado.equals("generarArqueo")){
				try{
					HibernateUtil.beginTransaction();
					
					generarArqueo(movimientosCaja, forma, usuario.getCodigoInstitucionInt());
					
					HibernateUtil.endTransaction();
				}
				catch (Exception e) {
					Log4JManager.error(e);
					HibernateUtil.abortTransaction();
				}
				return mapping.findForward("busqueda");
			
			}else if(estado.equals("cargarEntregaCajaMayorPrincipal")){
				try{				
					HibernateUtil.beginTransaction();
					cargarEntregaCajaMayorPrincipal(movimientosCaja, usuario, forma);
					
					HibernateUtil.endTransaction();
				}
				catch (Exception e) {
					Log4JManager.error(e);
					HibernateUtil.abortTransaction();
				}
				return mapping.findForward("entregaCajaMayorPrincipal");
				
			}else if(estado.equals("cargarEntregaTransportadoraValores")){
	
				//String valorInstitucion = ValoresPorDefecto.getInstitucionManejaEntregaATransportadoraValores(Integer.parseInt(usuario.getCodigoInstitucion()));
			
				//if(valorInstitucion.equals(ConstantesBD.acronimoSi)){
				try{	
					HibernateUtil.beginTransaction();
					
					cargarEntregaTransportadoraValores(movimientosCaja,	usuario, forma);
					HibernateUtil.endTransaction();
				}
				catch (Exception e) {
					Log4JManager.error(e);
					HibernateUtil.abortTransaction();
				}
				return mapping.findForward("entregaTransportadoraValores");
					
				//}else {
					
					//forma.setEstado("generarArqueo");
					//return mapping.findForward("busqueda");
				//}
				
			}else if(estado.equals("cambiarSeleccionFormasPago")){
			
				cambiarSeleccionFormaPago(forma);
				
				return mapping.findForward("seccionTotalesFormaPago");
			
			}else if (estado.equals("continuarProceso") || estado.equals("continuarProcesoParcial")){
				
				boolean registrarEntregaTransportadora = validarEntregaTransportadora (forma, errores, request, 0);
				
				if(registrarEntregaTransportadora){
					try{
						HibernateUtil.beginTransaction();
						continuarEntregaTransportadora (forma);
						if(estado.equals("continuarProceso")){
							forma.setMostrarDetalleEntrega(true);
						}
						HibernateUtil.endTransaction();
					}
					catch (Exception e) {
						Log4JManager.error(e);
						HibernateUtil.abortTransaction();
					}
				}
	
				return mapping.findForward("entregaTransportadoraValores");
				
			}else if (estado.equals("cargarNIT")){
				
				if(request.getParameter("codigo")!=null){
					
					forma.setTransportadoraValoresHelper(Integer.parseInt(request.getParameter("codigo")));
					
					if(forma.getDtoTempInformacionEntrega().getTransportadoraValores()!=null){
				
						request.setAttribute("nombreForm", "ArqueoEntregaParcialForm");
						request.setAttribute("pathAction", "arqueosCaja/arqueoEntregaParcial");
						request.setAttribute("dtoInformacion", "dtoTempInformacionEntrega");
					}
				}
				
				return mapping.findForward("seccionNitTransportadora");
			
			}else if (estado.equals("cargarCuentasBancarias")){

				if(request.getParameter("codigo")!=null && !"".equals(request.getParameter("codigo"))){
					int codigoEntidad = Integer.parseInt(request.getParameter("codigo"));
					
					forma.setEntidadesFinancierasHelper(codigoEntidad);
					
					if(forma.getDtoTempInformacionEntrega().getEntidadFinanciera()!=null){
						
						forma.setCuentaBancaria(null);
						
						request.setAttribute("nombreForm", "ArqueoEntregaParcialForm");
						request.setAttribute("pathAction", "arqueosCaja/arqueoEntregaParcial");
		
						String valor = ValoresPorDefecto.getInstitucionMultiempresa(Integer.parseInt(institucionBasica.getCodigo()));
						try{
							HibernateUtil.beginTransaction();
							ICuentasBancariasServicio cuentasBancariasServicio = TesoreriaFabricaServicio.crearCuentasBancariasServicio();
							if(ConstantesBD.acronimoSi.equals(valor)){
								
								forma.setListadoCuentasBancarias((ArrayList<CuentasBancarias>) cuentasBancariasServicio.obtenerCuentasBancariasPorEntidadYEmpresaInstitucion(codigoEntidad, usuario.getCodigoCentroAtencion()));
								
							}else{
								
								forma.setListadoCuentasBancarias((ArrayList<CuentasBancarias>) cuentasBancariasServicio.obtenerCuentasBancariasPorEntidad(codigoEntidad));
							}
							HibernateUtil.endTransaction();
						}catch (Exception e) {
							Log4JManager.error(e);
							HibernateUtil.abortTransaction();
						}
					}
				}
				
				return mapping.findForward("seccionCuentasBancarias");
			
			}else if(estado.equals("cargarInformacionTransportadora")){
				
				return mapping.findForward("informacionTransportadora");
			
			}else if(estado.equals("guardarEntregaCajaMayorPrincipal")){
				
				if(forma.getCajaMayorPrincipal()==null){
					
					errores.add("valor requerido", new ActionMessage("errors.required", "La Caja Mayor / Principal"));
					
				}else if(!UtilidadTexto.isEmpty(forma.getDtoTempInformacionEntrega().getObservaciones()) && 
						forma.getDtoTempInformacionEntrega().getObservaciones().length()>128){
					                    
					errores.add("valor requerido", new ActionMessage("errors.maxlength", "La información registrada en el campo observación", "128"));
					
				}else {
				
					forma.setDtoInformacionEntrega(forma.getDtoTempInformacionEntrega());
					forma.getDtoInformacionEntrega().setCajaMayorPrincipal(forma.getCajaMayorPrincipal());
					forma.getDtoInformacionEntrega().setETipoMovimiento(ETipoMovimiento.ENTREGA_CAJA_MAYOR_PRINCIPAL);
					forma.setEstadoGuardarMovimiento("EntregaCajaMayorPrincipal");
					forma.setMensajeProceso("Se generó el registro de Entrega a Caja Mayor/Principal. Debe guardar el Arqueo para confirmar la entrega");
					
					forma.setEstado("generarArqueo");
					return mapping.findForward("busqueda");
				}
				
				saveErrors(request, errores);
				
				return mapping.findForward("entregaCajaMayorPrincipal");

			}else if(estado.equals("guardarEntregaTransportadoraValores")){
				
				try{
					HibernateUtil.beginTransaction();
					continuarEntregaTransportadora(forma);
					HibernateUtil.endTransaction();
				}catch (Exception e) {
					Log4JManager.error(e);
					HibernateUtil.abortTransaction();
				}

				boolean registrarEntregaTransportadora = validarEntregaTransportadora (forma, errores, request, 1);
				saveErrors(request, errores);
				
				if(registrarEntregaTransportadora){

					forma.setDtoInformacionEntrega(forma.getDtoTempInformacionEntrega());
					
					if(forma.getCuentaBancaria()!=null){
						
						forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().setCuentasBancarias(forma.getCuentaBancaria());
					}

					forma.getDtoInformacionEntrega().setETipoMovimiento(ETipoMovimiento.ENTREGA_TRANSPORTADORA_VALORES);
					forma.setEstadoGuardarMovimiento("EntregaTransportadora");
					forma.setMensajeProceso("Se generó el registro de Entrega a Transportadora de Valores. Debe guardar el Arqueo para confirmar la entrega");
					forma.setEstado("generarArqueo");
					
					return mapping.findForward("busqueda");
				
				}else{
					
					return mapping.findForward("entregaTransportadoraValores");
				}

			}else if(estado.equals("volver")){
				
				/*
				 *  Se devuelven los valores a la variable temporal antes de que sufriera modificaciones.
				 */
				forma.setDtoTempInformacionEntrega((DtoInformacionEntrega) UtilidadClonacion.clonar(forma.getDtoInformacionEntrega()));
				
				if("EntregaCajaMayorPrincipal".equals(forma.getEstadoGuardarMovimiento())){
					
					recargarTotalFormasPago(forma, forma.getDtoInformacionEntrega().getListadoDtoFormaPagoDocSoportes());
				}

				forma.setEstado("generarArqueo");
				
				return mapping.findForward("busqueda");
				
			}else if(estado.equals("guardarArqueoEntregaParcial")){
				
				String mensajeProceso = "";
				
				boolean resultadoProceso = false;
				
				if(!"".equals(forma.getEstadoGuardarMovimiento()) && !forma.getEstadoGuardarMovimiento().equals(ConstantesBD.acronimoNo)){
					try{
						HibernateUtil.beginTransaction();
						resultadoProceso = guardarArqueoEntregaParcial(forma, usuario, errores, request);
						HibernateUtil.endTransaction();
					}catch (Exception e) {
						Log4JManager.error(e);
						HibernateUtil.abortTransaction();
					}
				}
				
				if(resultadoProceso){
					
					forma.setEstado("exitoso");
					forma.setMensajeProceso("Arqueo Entrega Parcial registrado exitosamente");
					int codigoInstitucion = usuario.getCodigoInstitucionInt();
					try{
						HibernateUtil.beginTransaction();
						imprimirEntrega(forma, codigoInstitucion);
						HibernateUtil.endTransaction();
					}catch (Exception e) {
						Log4JManager.error(e);
						HibernateUtil.abortTransaction();
					}
					return mapping.findForward("consultarEntrega");
					
				}else{
					
					forma.setEstado("NoExitoso");
					
					mensajeProceso = "No se registró el Arqueo de Entrega Parcial";
					
					if(forma.getDtoInformacionEntrega().getETipoMovimiento() == ETipoMovimiento.ENTREGA_TRANSPORTADORA_VALORES){
						
						if(!forma.isExisteConsecutivoFaltante()){
							
							mensajeProceso = "Falta definir el consecutivo de Faltantes/Sobrantes. No se puede continuar con el Arqueo Entrega Parcial";
						}
					}
					
					forma.setMensajeProceso(mensajeProceso);
				}
				
				return mapping.findForward("busqueda");
				
			}else if(estado.equals("imprimirEntrega")){
				
				UtilidadTransaccion.getTransaccion().begin();
				
				int codigoInstitucion = usuario.getCodigoInstitucionInt();
				try{
					HibernateUtil.beginTransaction();
					imprimirEntrega(forma, codigoInstitucion);
					HibernateUtil.endTransaction();
				}catch (Exception e) {
					Log4JManager.error(e);
					HibernateUtil.abortTransaction();
				}
				return mapping.findForward("consultarEntrega");
				
			}else if(estado.equals("impresion")){
				
				return mapping.findForward("impresionArqueoEntregaParcial");
			}
			else if(estado.equals("mostrarPopUpTipoReporteEntregaCajaMayor")){
				String[] listadoTipoRep = new String[]{ConstantesIntegridadDominio.acronimoTipoReporteImpEntregaCajaMayPrincResumido,
						ConstantesIntegridadDominio.acronimoTipoReporteImpEntregaCajaMayPrincDetallado,
						ConstantesIntegridadDominio.acronimoTipoReporteAmbos};
				this.cargarTiposReporte(forma,listadoTipoRep);
				return mapping.findForward("popUpTipoReporte");
			}
			else if(estado.equals("impresionTipoFormato")){
				try{
					HibernateUtil.beginTransaction();
					this.generarArqueo(movimientosCaja, forma, usuario.getCodigoInstitucionInt());//ACTUALIZAR DTO PARA MT 2776
					this.imprimirEntregaCajaMayPrinc(forma, request, usuario, institucionBasica);
					forma.setEstado("exitoso");
					forma.setMensajeProceso("Arqueo Entrega Parcial registrado exitosamente");
					HibernateUtil.endTransaction();
				}catch (Exception e) {
					Log4JManager.error(e);
					HibernateUtil.abortTransaction();
				}
				return mapping.findForward("consultarEntrega");
			}
			else if(estado.equals("impresionEntregaCajaMayor")){
				try{
					HibernateUtil.beginTransaction();
					this.imprimirEntregaCajaMayPrinc(forma, request, usuario, institucionBasica);
					forma.setEstado("exitoso");
					forma.setMensajeProceso("Arqueo Entrega Parcial registrado exitosamente");
					HibernateUtil.endTransaction();
				}catch (Exception e) {
					Log4JManager.error(e);
					HibernateUtil.abortTransaction();
				}
				return mapping.findForward("consultarEntrega");
			}
			else if(estado.equals("impresionTransportadoraValores")){
				try{
					HibernateUtil.beginTransaction();
					this.imprimirEntregaTransportadora(forma, request, usuario, institucionBasica);
					forma.setEstado("exitoso");
					forma.setMensajeProceso("Arqueo Entrega Parcial registrado exitosamente");
					HibernateUtil.endTransaction();
				}catch (Exception e) {
					Log4JManager.error(e);
					HibernateUtil.abortTransaction();
				}
				return mapping.findForward("consultarEntrega");
			}
			else if(estado.equals("mostrarPopUpDocSoporte"))
			{
				return mapping.findForward("popUpDocSoporte");
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
	 * Método que se encarga de cargar los tipos de reporte a imprimir
	 * 
	 * @param form
	 * @param listadoTiposReporte lista con acronimos de integridad dominio
	 * @author Fabián Becerra
	 */
	private void cargarTiposReporte(ArqueoEntregaParcialForm form, String[] listadoTiposReporte){
		
		form.setListadoTiposReporte(new ArrayList<DtoIntegridadDominio>());
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<DtoIntegridadDominio> listaTiposReporte=Utilidades.generarListadoConstantesIntegridadDominio(
				con, listadoTiposReporte, false);
		
		UtilidadBD.closeConnection(con);
		
		form.setListadoTiposReporte(listaTiposReporte);
		
	}
	
	/**
	 * M&eacute;todo que se encarga de reunir toda la informaci&oacute;n necesaria
	 * para realizar el proceso de Arqueo Entrega Parcial.
	 * 
	 * @param movimientosCaja
	 * @param forma
	 */
	private void generarArqueo(MovimientosCaja movimientosCaja,	ArqueoEntregaParcialForm forma, int codigoInstitucion) {
		
		IMovimientosCajaServicio movimientosCajaServicio = getMovimientosCajaServicio();
		
		DtoConsolidadoMovimiento consolidadoMovimientoDTO = movimientosCajaServicio.obtenerConsolidadoMovimiento(movimientosCaja);
		consolidadoMovimientoDTO.setETipoMovimiento(ETipoMovimiento.ARQUEO_ENTREGA_PARCIAL);
		
		forma.setConsolidadoMovimientoDTO(consolidadoMovimientoDTO);
		
		boolean existenDocumentos = consolidadoMovimientoDTO.getNumeroTotalDocumentos() > 0 ? true : false;
		
		forma.setExistenDocumentos(existenDocumentos);

		String valorInstitucion = ValoresPorDefecto.getInstitucionManejaEntregaATransportadoraValores(codigoInstitucion);
		
		if(valorInstitucion.equals(ConstantesBD.acronimoSi)){
			
			forma.setManejaEntregaTransportadora(true);
			
		}else{
			
			forma.setManejaEntregaTransportadora(false);
		}
	}


	
	/**
	 * Carga la informaci&oacute;n necesaria para realizar la entrega a Transportadora de Valores
	 * 
	 * @param movimientosCaja
	 * @param usuario
	 * @param forma
	 */
	private void cargarEntregaTransportadoraValores(MovimientosCaja movimientosCaja, UsuarioBasico usuario,	ArqueoEntregaParcialForm forma) {
		
		
		if(forma.getEstadoGuardarMovimiento().equals(ConstantesBD.acronimoNo) || forma.getDtoInformacionEntrega().getListadoDtoFormaPagoDocSoportes().size()==0){
			
			ITransportadoraValoresServicio transportadoraValoresServicio = TesoreriaFabricaServicio.crearTransportadoraValoresServicio();
			IMovimientosCajaServicio movimientosCajaServicio = getMovimientosCajaServicio();
			IEntidadesFinancierasServicio entidadesFinancierasServicio = TesoreriaFabricaServicio.crearEntidadesFinancierasServicio();
			
			TransportadoraValores transportadoraValores = new TransportadoraValores();
			
			transportadoraValores.setTerceros(new Terceros());
			
			transportadoraValores.setActivo((ConstantesBD.acronimoSi).charAt(0));
			
			forma.setListadoTransportadoraValores((ArrayList<TransportadoraValores>) transportadoraValoresServicio.listarTodos(transportadoraValores, usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion()));
			
			forma.setListadoEntidadesFinancieras((ArrayList<DtoEntidadesFinancieras>) entidadesFinancierasServicio.obtenerEntidadesPorInstitucion(usuario.getCodigoInstitucionInt(), true));

			forma.setListadoCuentasBancarias(null);
			
			forma.setDtoInformacionEntrega(movimientosCajaServicio.consolidadoInformacionEntrega(movimientosCaja, ETipoMovimiento.ENTREGA_TRANSPORTADORA_VALORES)); // false
			
			forma.getDtoInformacionEntrega().setTransportadoraValores(transportadoraValores);
			
			forma.getDtoInformacionEntrega().getMovimientosCaja().setEntregaTransportadoraByCodigoPk(new EntregaTransportadora());

			forma.setEstadoGuardarMovimiento(ConstantesBD.acronimoNo);
			
			forma.setMensajeProceso("No se generó el registro de Entrega a Transportadora de Valores");
			
			forma.setMostrarDetalleEntrega(false);
			
		}else if("EntregaTransportadora".equals(forma.getEstadoGuardarMovimiento())){
			
			continuarEntregaTransportadora(forma);
			
			forma.setMostrarDetalleEntrega(true);
			
			if(forma.getDtoInformacionEntrega().getEntidadFinanciera()!=null){
				
				ICuentasBancariasServicio cuentasBancariasServicio = TesoreriaFabricaServicio.crearCuentasBancariasServicio();
				forma.setListadoCuentasBancarias((ArrayList<CuentasBancarias>) cuentasBancariasServicio.obtenerCuentasBancariasPorEntidad(forma.getDtoInformacionEntrega().getEntidadFinanciera().getConsecutivo()));
			}

			CuentasBancarias cuentasBancaria = forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getCuentasBancarias();
			
			if(cuentasBancaria!=null){
				
				forma.setCuentaBancariaHelper(cuentasBancaria.getCodigo());
				forma.setCuentaBancaria(cuentasBancaria);
			}
		}
		
		forma.setDtoTempInformacionEntrega((DtoInformacionEntrega) UtilidadClonacion.clonar(forma.getDtoInformacionEntrega()));

		recargarTotalFormasPago(forma, forma.getDtoTempInformacionEntrega().getListadoDtoFormaPagoDocSoportes());
		
		forma.setMostrarParaSeccionEspecial("entregaTransportadoraValores");
	}

	/**
	 * Carga la informaci&oacute;n necesaria para realizar la entrega a Caja Mayor / Principal
	 *  
	 * @param movimientosCaja
	 * @param usuario
	 * @param forma
	 */
	private void cargarEntregaCajaMayorPrincipal(MovimientosCaja movimientosCaja, UsuarioBasico usuario,ArqueoEntregaParcialForm forma) {
		
		/*
		 * Si el valor del atributo estadoGuardarMovimiento es igual a N, o el tamaño del listado de las formas de pago a entregar es igual a 0,
		 * quiere decir que no se ha realizado una entrega a Caja Mayor / Principal, por lo cual los atributos particulares de esta entrega
		 * deben ser inicializados.
		 */
		if(forma.getEstadoGuardarMovimiento().equals(ConstantesBD.acronimoNo) || forma.getDtoInformacionEntrega().getListadoDtoFormaPagoDocSoportes().size()==0
				||UtilidadTexto.isEmpty(forma.getEstadoGuardarMovimiento())){
			
			String valorInstitucion = ValoresPorDefecto.getInstitucionManejaCajaPrincipal(Integer.parseInt(usuario.getCodigoInstitucion()));
			ICajasServicio cajasServicio = TesoreriaFabricaServicio.crearCajasServicio();
			IMovimientosCajaServicio movimientosCajaServicio = getMovimientosCajaServicio();
			
			if(valorInstitucion.equals(ConstantesBD.acronimoSi)){
				
				forma.setListadoCajasPrincipalMayor(cajasServicio.listarCajasPorCentrosAtencionPorTipoCaja(usuario.getCodigoCentroAtencion(), ConstantesBD.codigoTipoCajaPpal));
				
			}else {

				forma.setListadoCajasPrincipalMayor((ArrayList<Cajas>) cajasServicio.listarCajasPorInstitucionPorTipoCaja (usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoCajaMayor));
			}

			forma.setDtoInformacionEntrega(movimientosCajaServicio.consolidadoInformacionEntrega(movimientosCaja, ETipoMovimiento.ENTREGA_CAJA_MAYOR_PRINCIPAL));

			forma.setEstadoGuardarMovimiento(ConstantesBD.acronimoNo);
			
			forma.setCajaMayorPrincipal(null);
			
			forma.getDtoInformacionEntrega().setCajaMayorPrincipal(null);
			
			forma.setMensajeProceso("No se generó el registro de Entrega a Caja Mayor/Principal");
			
			forma.getDtoInformacionEntrega().setObservaciones("");
		}
		
		
		forma.setDtoTempInformacionEntrega((DtoInformacionEntrega) UtilidadClonacion.clonar(forma.getDtoInformacionEntrega()));

		recargarTotalFormasPago(forma, forma.getDtoTempInformacionEntrega().getListadoDtoFormaPagoDocSoportes());
		
		forma.setMostrarParaSeccionEspecial("entregaCajaMayorPrincipal");
		
		forma.setCajaMayorPrincipal(forma.getDtoTempInformacionEntrega().getCajaMayorPrincipal());
		
	}


	/**
	 * Realiza el proceso necesario para almacenar la informaci&oacute;n del Arqueo Entrega Parcial.
	 * Retorna un boolean indicando si el proceso fue exitoso o no.
	 * 
	 * @param forma
	 * @param usuario
	 * @return boolean indicando si el proceso fue exitoso o no.
	 */
	private boolean guardarArqueoEntregaParcial(ArqueoEntregaParcialForm forma,	UsuarioBasico usuario, ActionErrors errores, HttpServletRequest request) {
		
		IMovimientosCajaServicio movimientosCajaServicio = TesoreriaFabricaServicio.crearMovimientosCajaServicio();
		
		BigDecimal consecutivoArqueo=new BigDecimal(Utilidades.convertirADouble(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoArqueosEntregaParcial, usuario.getCodigoInstitucionInt())));
		forma.getDtoInformacionEntrega().getMovimientosCaja().setConsecutivo(consecutivoArqueo.longValue());
		BigDecimal consecutivoEntrega = null;
		boolean registroMovimiento = false;
		
		if(forma.getDtoInformacionEntrega().getETipoMovimiento() == ETipoMovimiento.ENTREGA_CAJA_MAYOR_PRINCIPAL){
			
			if(recargarTotalFormasPago(forma, forma.getDtoInformacionEntrega().getListadoDtoFormaPagoDocSoportes())){
				
				consecutivoEntrega=new BigDecimal(Utilidades.convertirADouble(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoEntregaCajaMayor, usuario.getCodigoInstitucionInt())));
				
				EntregaCajaMayor entregaCajaMayor = new EntregaCajaMayor();
				
				MovimientosCaja movimientosCajaEntrega =  new MovimientosCaja();
				
				movimientosCajaEntrega.setConsecutivo(consecutivoEntrega.longValue());
				
				entregaCajaMayor.setMovimientosCajaByCodigoPk(movimientosCajaEntrega);
				
				forma.getDtoInformacionEntrega().getMovimientosCaja().setEntregaCajaMayorByCodigoPk(entregaCajaMayor);
				
				registroMovimiento = movimientosCajaServicio.guardarMovimientoCaja(forma.getDtoInformacionEntrega());
				
				if(registroMovimiento){
					Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
					// si se insertó bien entonces se cambia el uso de los consecutivos a Si, Si
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH,ConstantesBD.nombreConsecutivoArqueosEntregaParcial, usuario.getCodigoInstitucionInt(), consecutivoArqueo.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH,ConstantesBD.nombreConsecutivoEntregaCajaMayor, usuario.getCodigoInstitucionInt(), consecutivoEntrega.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);

				}else{
					Connection con=UtilidadBD.abrirConexion();
					// si no se insertó, entonces se cambia el uso de los consecutivos a No, No
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoArqueosEntregaParcial, usuario.getCodigoInstitucionInt(), consecutivoArqueo.toString(), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoEntregaCajaMayor, usuario.getCodigoInstitucionInt(), consecutivoEntrega.toString(), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				}
			}
			
		}else if(forma.getDtoInformacionEntrega().getETipoMovimiento() == ETipoMovimiento.ENTREGA_TRANSPORTADORA_VALORES){

			forma.setExisteConsecutivoFaltante(consecutivoFaltanteSobranteParametrizado(usuario.getCodigoInstitucionInt()));
			
			if(!forma.getDtoInformacionEntrega().isExisteDiferencia() || (forma.getDtoInformacionEntrega().isExisteDiferencia() && forma.isExisteConsecutivoFaltante())){
				
				boolean registrarEntregaTransportadora = validarEntregaTransportadora (forma, errores, request, 1);
				
				if(registrarEntregaTransportadora){

					consecutivoEntrega=new BigDecimal(Utilidades.convertirADouble(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoEntregaTransportadora, usuario.getCodigoInstitucionInt())));

					MovimientosCaja movimientosCajaEntregaTransportadora =  new MovimientosCaja();
					
					movimientosCajaEntregaTransportadora.setConsecutivo(consecutivoEntrega.longValue());;

					forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().setMovimientosCajaByCodigoPk(movimientosCajaEntregaTransportadora);
					
					registroMovimiento = movimientosCajaServicio.guardarMovimientoCaja(forma.getDtoInformacionEntrega());
					
					if(registroMovimiento){
						Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
						// si se insertó bien entonces se cambia el uso de los consecutivos a Si, Si
						UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH,ConstantesBD.nombreConsecutivoArqueosEntregaParcial, usuario.getCodigoInstitucionInt(), consecutivoArqueo.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
						UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH,ConstantesBD.nombreConsecutivoEntregaTransportadora, usuario.getCodigoInstitucionInt(), consecutivoEntrega.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
					
					}else{
						Connection con=UtilidadBD.abrirConexion();
						// si no se insertó, entonces se cambia el uso de los consecutivos a No, No
						UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoArqueosEntregaParcial, usuario.getCodigoInstitucionInt(), consecutivoArqueo.toString(), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
						UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoEntregaTransportadora, usuario.getCodigoInstitucionInt(), consecutivoEntrega.toString(), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
						UtilidadBD.closeConnection(con);
					}
				}
			}
		}
		
		return registroMovimiento;
	}
	

	/**
	 * Retorna un boolean indicando si el proceso de entrega a la transportadora de valores contiene
	 * la informaci&oacute;n necesaria para realizar el respectivo registro.
	 * 
	 * Cuando el par&aacute;metro tipoValidacion es igual a 1, se tiene en cuenta las validaciones propias
	 * de la secci&oacute;n de informaci&oacute;n de la entrega a la transportadora de valores
	 * 
	 * @param forma
	 * @return boolean
	 */
	private boolean validarEntregaTransportadora(ArqueoEntregaParcialForm forma, ActionErrors errores, HttpServletRequest request, int tipoValidacion) {
		
		// Se limpian los errores asociados
		getErrors(request).clear();
		
		String validacionNoFormaNinguno = validarInfoEntregaTransNoFormaNinguno (forma);
		String validacionFormaNinguno = validarInfoEntregaTransFormaNinguno (forma);

		if (!"".equals(validacionFormaNinguno) && !"noRegistroInformacion".equals(validacionFormaNinguno)){

			errores.add("valor superado", new ActionMessage("errores.modTesoreria.valorFormaPagoNingunoSuperada", validacionFormaNinguno));
			forma.setMostrarDetalleEntrega(true);
			forma.setEstado("continuarProceso");
		
		}else if("noRegistroInformacion".equals(validacionNoFormaNinguno) && "noRegistroInformacion".equals(validacionFormaNinguno)){

			errores.add("no existe informacion", new ActionMessage("errores.modTesoreria.necesarioInformacionFormaPago"));
			forma.setEstado("detenerProceso");
		}
		
		if(!"".equals(validacionNoFormaNinguno) && !"noRegistroInformacion".equals(validacionNoFormaNinguno)){
			
			errores.add("valores requeridos", new ActionMessage("errors.required", validacionNoFormaNinguno));
			forma.setMostrarDetalleEntrega(true);
			forma.setEstado("continuarProceso");
		}

		if(tipoValidacion == 1){
			
			if(forma.getDtoTempInformacionEntrega().getTransportadoraValores() == null){
				
				errores.add("valor requerido", new ActionMessage("errors.required", "La Transportadora de Valores"));
				
			}else if("".equals(forma.getDtoTempInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getResponsable().trim())){
				
				errores.add("valor requerido", new ActionMessage("errors.required", "El Responsable"));
			
			}else if(!UtilidadTexto.isEmpty(forma.getDtoTempInformacionEntrega().getObservaciones()) && 
					forma.getDtoTempInformacionEntrega().getObservaciones().length()>128){
                
				errores.add("valor requerido", new ActionMessage("errors.maxlength", "La información registrada en el campo observación", "128"));
			
			}else if(forma.getDtoTempInformacionEntrega().getEntidadFinanciera()!=null){
				
				if(forma.getCuentaBancaria()==null){
					
					errores.add("valor requerido", new ActionMessage("errors.required", "El Tipo y Número de la Cuenta Bancaria"));
				}
			}
		}
		
		saveErrors(request, errores);
		
		if(!getErrors(request).isEmpty()){
			
			forma.setEstadoGuardarMovimiento(ConstantesBD.acronimoNo);
			
			forma.setMensajeProceso("No se generó el registro de Entrega a Transportadora de Valores");

			return false;
			
		}else{
			
			return true;
		}
	}

	
	/**
	 * Retorna un String indicando si el proceso de entrega a la transportadora de valores contiene
	 * la informaci&oacute;n necesaria para realizar el respectivo registro.
	 * Solo se tiene en cuenta las formas diferentes de tipo "NINGUNO" para la verificaci&oacute;n,
	 * aunque se determina si existen otras formas de pago para la entrega.
	 * 
	 * @param forma
	 * @return String
	 */
	private String validarInfoEntregaTransNoFormaNinguno(ArqueoEntregaParcialForm forma) {
		
		String mensajeValidacion = "";
		boolean existenOtrasFormas = false;
		boolean registrarOtrasFormas = false;
		
		for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte: forma.getDtoTempInformacionEntrega().getListadoDtoFormaPagoDocSoportes())
		{
			if(dtoFormaPagoDocSoporte.getCodigoTipoDetalleFormaPago() != ConstantesBD.codigoTipoDetalleFormasPagoNinguno)
			{
				for (DtoEntidadesFinancieras dtoEntidadesFinancieras : dtoFormaPagoDocSoporte.getListadoDtoEntidadesFinancieras()) 
				{
					for (DtoDetalleDocSopor dtoDetalleDocSopor : dtoEntidadesFinancieras.getListadoDtoDetDocSoporte()) 
					{
						existenOtrasFormas = true;
						/*
						 * Esto quiere decir que se van a registrar otras formas de pago
						 */
						if(dtoDetalleDocSopor.getValorActual() != null || !"".equals(dtoDetalleDocSopor.getNroDocumentoRecibido())){
							registrarOtrasFormas = true;
						}
						
						if((dtoDetalleDocSopor.getValorActual() != null && "".equals(dtoDetalleDocSopor.getNroDocumentoRecibido())) ||	
							(dtoDetalleDocSopor.getValorActual() == null && !"".equals(dtoDetalleDocSopor.getNroDocumentoRecibido())))
						{
							mensajeValidacion+= "Forma de Pago: " +  dtoFormaPagoDocSoporte.getFormaPago() + " - Documento de Soporte: " + dtoDetalleDocSopor.getNroDocumentoEntregado() + " - ";
						
						}
					}
				}
			}
		}
		
		if(!existenOtrasFormas || (existenOtrasFormas && !registrarOtrasFormas)){
	
			return mensajeValidacion = "noRegistroInformacion";
		
		}
		
		return mensajeValidacion;
	}
	
	
	/**
	 * Retorna un String indicando si el proceso de entrega a la transportadora de valores contiene
	 * la informaci&oacute;n necesaria para realizar el respectivo registro. 
	 * Solo se tiene en cuenta las formas de pago de tipo "NINGUNO"
	 * 
	 * @param forma
	 * @return String
	 */
	private String validarInfoEntregaTransFormaNinguno(ArqueoEntregaParcialForm forma) {
		
		String mensajeValidacion = "";
		boolean existenFormasNinguno = false;
		
		for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte: forma.getDtoTempInformacionEntrega().getListadoDtoFormaPagoDocSoportes())
		{
			if(dtoFormaPagoDocSoporte.getCodigoTipoDetalleFormaPago() == ConstantesBD.codigoTipoDetalleFormasPagoNinguno)
			{
				existenFormasNinguno =  true;
				
				if(dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorCaja()<=0 && !ConstantesBD.acronimoSi.equals(mensajeValidacion)){
					
					mensajeValidacion = "noRegistroInformacion";
					
				}else if(dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorCaja() > dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorSistema()){

					return dtoFormaPagoDocSoporte.getFormaPago();
					
				}else if(dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorCaja() > 0){
					
					mensajeValidacion = ConstantesBD.acronimoSi;
				}
			}
		}

		if(existenFormasNinguno && ConstantesBD.acronimoSi.equals(mensajeValidacion)){
		
			return "";
		}
		
		return mensajeValidacion;
	}
	
		
	/**
	 * Cambia el estado de selecci&oacute;n de las formas de pago
	 * disponibles para ser entregadas en la Entrega a Caja Mayor / Principal
	 * 
	 * @param forma
	 */
	private void cambiarSeleccionFormaPago (ArqueoEntregaParcialForm forma){
		
		for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte : forma.getDtoTempInformacionEntrega().getListadoDtoFormaPagoDocSoportes()) { // forma.getDtoInformacionEntrega().getListadoDtoFormaPagoDocSoportes()
			
			if(dtoFormaPagoDocSoporte.getDtoCuadreCaja().getTipoFormaPago() == forma.getFormaPagoCheckeada()){
				
				if(ConstantesBD.acronimoSi.equalsIgnoreCase(dtoFormaPagoDocSoporte.getSeleccionado())){
					
					dtoFormaPagoDocSoporte.setSeleccionado(ConstantesBD.acronimoNo);
					
				}else{
					
					dtoFormaPagoDocSoporte.setSeleccionado(ConstantesBD.acronimoSi);
				}
				
				break;
			}
		}
		
		recargarTotalFormasPago(forma, forma.getDtoTempInformacionEntrega().getListadoDtoFormaPagoDocSoportes());
		forma.setFormaPagoCheckeada(ConstantesBD.codigoNuncaValido);
	}
	
	/**
	 * Recalcula los valores por forma de pago a entregar y determina
	 * si existe informaci&oacute;n para realizar la Entrega
	 * 
	 * @param forma
	 * @param listadoDtoFormaPagoDocSoportes 
	 * @return boolean que determina si existe informaci&oacute;n para realizar la Entrega
	 */
	private boolean recargarTotalFormasPago(ArqueoEntregaParcialForm forma, ArrayList<DtoFormaPagoDocSoporte> listadoDtoFormaPagoDocSoportes) {
		
		boolean registrarEntregaCaja = false; 
		
		forma.getConsolidadoMovimientoDTO().getCuadreCajaDTOs().clear();
		
		for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte : listadoDtoFormaPagoDocSoportes) {
		
			if(ConstantesBD.acronimoSi.equalsIgnoreCase(dtoFormaPagoDocSoporte.getSeleccionado())){

				forma.getConsolidadoMovimientoDTO().getCuadreCajaDTOs().add(dtoFormaPagoDocSoporte.getDtoCuadreCaja());
				registrarEntregaCaja = true;
			}
		}
		
		return registrarEntregaCaja;
	}
	
	/**
	 * Cambia el estado para continuar de diligenciar la informaci&oacute;n correspondiente
	 * a la Entrega a Transportadora de Valores.
	 * 
	 * @param forma
	 */
	private void continuarEntregaTransportadora(ArqueoEntregaParcialForm forma) {

		IMovimientosCajaServicio movimientosCajaServicio = getMovimientosCajaServicio();
		
		forma.setDtoTempInformacionEntrega(movimientosCajaServicio.calcularValoresParaArqueos(forma.getDtoTempInformacionEntrega(), ETipoMovimiento.ARQUEO_ENTREGA_PARCIAL));
		
		forma.setEstado("continuarProceso");
	}
	
	

	/**
	 * M&eacute;todo que se encarga de consultar la Entrega realizada para su impresi&oacute;n
	 * 
	 * @param forma
	 * @param codigoInstitucion
	 */
	private void imprimirEntrega(ArqueoEntregaParcialForm forma, int codigoInstitucion) {

		IConsultaArqueoCierreCajaServicio consultaArqueoCierreCajaServicio = TesoreriaFabricaServicio.crearConsultaArqueoCierreCajaServicio();
		
		IMovimientosCajaMundo movimientosCajaMundo = TesoreriaFabricaMundo.crearMovimientosCajaMundo();
		DtoInformacionEntrega dtoInformacionEntrega = new DtoInformacionEntrega(movimientosCajaMundo.obtenerConsolidadoMovimiento(forma.getDtoInformacionEntrega().getMovimientosCaja()));
		long codigoEntrega = ConstantesBD.codigoNuncaValidoLong;
		
		if(forma.getDtoInformacionEntrega().getETipoMovimiento() == ETipoMovimiento.ENTREGA_CAJA_MAYOR_PRINCIPAL){
			
			if(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaCajaMayorByCodigoPk()!=null){
				
				codigoEntrega = forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaCajaMayorByCodigoPk().getMovimientoCaja();
			}
			
		}else if (forma.getDtoInformacionEntrega().getETipoMovimiento() == ETipoMovimiento.ENTREGA_TRANSPORTADORA_VALORES){
			
			if(forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk()!=null){
				
				codigoEntrega = forma.getDtoInformacionEntrega().getMovimientosCaja().getEntregaTransportadoraByCodigoPk().getMovimientoCaja();
			}
			
			MovimientosCaja movCaja=forma.getDtoInformacionEntrega().getMovimientosCaja();
			forma.setDtoInformacionEntrega(consultaArqueoCierreCajaServicio.consultarArqueoParcialPorEntrega(codigoEntrega, forma.getDtoInformacionEntrega().getETipoMovimiento(), codigoInstitucion));
			
			forma.getDtoInformacionEntrega().setMovimientoCajaAsignado(movCaja);
			forma.getDtoInformacionEntrega().setMovimientosCaja(movCaja);
			String nombreUsuarioGeneraArqueo=forma.getDtoInformacionEntrega().getMovimientosCaja().getUsuarios().getPersonas().getPrimerApellido()+" "+(UtilidadTexto.isEmpty(forma.getDtoInformacionEntrega().getMovimientosCaja().getUsuarios().getPersonas().getSegundoApellido())?"":forma.getDtoInformacionEntrega().getMovimientosCaja().getUsuarios().getPersonas().getSegundoApellido())+" "
					+forma.getDtoInformacionEntrega().getMovimientosCaja().getUsuarios().getPersonas().getPrimerNombre()+" "+(UtilidadTexto.isEmpty(forma.getDtoInformacionEntrega().getMovimientosCaja().getUsuarios().getPersonas().getSegundoNombre())?"":forma.getDtoInformacionEntrega().getMovimientosCaja().getUsuarios().getPersonas().getSegundoNombre())+" "
					+" ("+forma.getDtoInformacionEntrega().getMovimientosCaja().getUsuarios().getLogin()+")";
			forma.getDtoInformacionEntrega().setNombreUsuarioGeneraCierre(nombreUsuarioGeneraArqueo);
			forma.getDtoInformacionEntrega().setEntregaTransportadoraValoresDTOs(dtoInformacionEntrega.getEntregaTransportadoraValoresDTOs());
			forma.getDtoInformacionEntrega().setTotalesParcialesEntrTransDTOs(dtoInformacionEntrega.getTotalesParcialesEntrTransDTOs());
			forma.getDtoInformacionEntrega().setTotalEntregaTransportadoraCajaEntregado(dtoInformacionEntrega.getTotalEntregaTransportadoraCajaEntregado());
			
			ArrayList<Integer> tiposFormaPago= new ArrayList<Integer>();
			for (DtoEntregaCaja dtoEntregaTrans : forma.getDtoInformacionEntrega()
					.getEntregaTransportadoraValoresDTOs()) {
				if (!tiposFormaPago.contains(dtoEntregaTrans.getTipoFormaPago())
						&& dtoEntregaTrans.getIdMovimientoCaja() == forma
								.getDtoInformacionEntrega().getMovimientosCaja()
								.getEntregaTransportadoraByCodigoPk().getMovimientoCaja()) {
					BigDecimal valorFaltante = new BigDecimal(0.0);
					double valorSistema = 0.0;
					for (DtoEntregaCaja dtoEntregaTransInterno : forma
							.getDtoInformacionEntrega()
							.getEntregaTransportadoraValoresDTOs()) {
						if (dtoEntregaTrans.getTipoFormaPago() == dtoEntregaTransInterno.getTipoFormaPago()
								&&dtoEntregaTransInterno.getIdMovimientoCaja()==forma
								.getDtoInformacionEntrega().getMovimientosCaja()
								.getEntregaTransportadoraByCodigoPk().getMovimientoCaja()) {
							valorFaltante = valorFaltante
									.add((dtoEntregaTransInterno.getValorFaltante() != null ? dtoEntregaTransInterno
											.getValorFaltante() : new BigDecimal(
											0.0)));
							valorSistema += dtoEntregaTransInterno
									.getValorSistema();
						}
					}
					tiposFormaPago.add(dtoEntregaTrans.getTipoFormaPago());
					for (DtoCuadreCaja dtoCuadreCaja : forma
							.getDtoInformacionEntrega().getCuadreCajaDTOs()) {
						if (dtoEntregaTrans.getTipoFormaPago() == dtoCuadreCaja
								.getTipoFormaPago()) {
							dtoCuadreCaja.setValorDiferencia(valorFaltante
									.doubleValue());
							dtoCuadreCaja.setValorCaja(valorSistema);
						}
					}
				}
		}
		
		}		
		forma.setConsulta(true);
	}
	
	/**
	 * Retorna la implementaci&oacute;n del servicio de Movimientos de Caja
	 * @return IMovimientosCajaServicio - Implementaci&oacute;n del servicio de Movimientos de Caja
	 */
	private IMovimientosCajaServicio getMovimientosCajaServicio() {
		
		return TesoreriaFabricaServicio.crearMovimientosCajaServicio();
	}
	
	/**
	 * Método que genera el reporte resumido, detallado o ambos de entrega
	 * caja mayor principal
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param institucion 
	 */
	private void imprimirEntregaCajaMayPrinc(ArqueoEntregaParcialForm forma,  HttpServletRequest request, UsuarioBasico usuario, InstitucionBasica ins) {
		
		String tipoSalida         = forma.getTipoReporte();
	    String nombreArchivo="";
	    forma.setListaNombresReportes(new ArrayList<String>());
	    
	    if (!tipoSalida.equals(ConstantesBD.codigoNuncaValido+"")&&!UtilidadTexto.isEmpty(tipoSalida)) {
	
	    	UsuariosDelegate usu= new UsuariosDelegate();
			Usuarios usuarioCompleto=usu.findById(usuario.getLoginUsuario());
			
	    	//--------------TITULO REPORTE---------------------------
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
	    	//-------------------------------------------------------
	    	
	    	//--------------ENCABEZADO REPORTE-----------------------
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
	    	forma.getDtoInformacionEntrega().setTotalesDocumentoDTOs(forma.getConsolidadoMovimientoDTO().getTotalesDocumentoDTOs());
	    	forma.getDtoInformacionEntrega().setCajaMayorPrincipal(forma.getDtoTempInformacionEntrega().getCajaMayorPrincipal());
	    	//-----------------------------------------------------
	    	
	    	JasperReportBuilder reporte=null;
			if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteImpEntregaCajaMayPrincResumido))
	        {
				GeneradorReporteEntregaCajaMayorPrincResumido generadorReporte =
		            new GeneradorReporteEntregaCajaMayorPrincResumido(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega(), forma.isConsulta(), forma.getMostrarParaSeccionEspecial());
	        		reporte = generadorReporte.generarReporte();
	        		nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ImpresionEntregaCajaMayPrincResumido");
	        		forma.getListaNombresReportes().add(nombreArchivo);
	        }else
	        	if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteImpEntregaCajaMayPrincDetallado))
	        	{
	        		GeneradorReporteEntregaCajaMayorPrincDetallado generadorReporte =
	        			new GeneradorReporteEntregaCajaMayorPrincDetallado(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega(), forma.isConsulta(), forma.getMostrarParaSeccionEspecial());
	        		reporte = generadorReporte.generarReporte();
	        		nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ImpresionEntregaCajaMayPrincDetallado");
	        		forma.getListaNombresReportes().add(nombreArchivo);
	        	}else
	        		if(forma.getTipoReporte().equals(ConstantesIntegridadDominio.acronimoTipoReporteAmbos))
	        		{
	        			GeneradorReporteEntregaCajaMayorPrincResumido generadorReporteRes =
	        	            new GeneradorReporteEntregaCajaMayorPrincResumido(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega(), forma.isConsulta(), forma.getMostrarParaSeccionEspecial());
	            		reporte = generadorReporteRes.generarReporte();
	            		String nombreArchivoRes = generadorReporteRes.exportarReportePDF(reporte, "ImpresionEntregaCajaMayPrincResumido");
	            		
	            		GeneradorReporteEntregaCajaMayorPrincDetallado generadorReporteDeta =
	            			new GeneradorReporteEntregaCajaMayorPrincDetallado(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega(), forma.isConsulta(), forma.getMostrarParaSeccionEspecial());
	            		reporte = generadorReporteDeta.generarReporte();
	            		String nombreArchivoDet = generadorReporteDeta.exportarReportePDF(reporte, "ImpresionEntregaCajaMayPrincDetallado");
	            		
	            		forma.getListaNombresReportes().add(nombreArchivoRes);
	            		forma.getListaNombresReportes().add(nombreArchivoDet);
	        		}
	            
			forma.setTipoReporte(ConstantesBD.codigoNuncaValido+"");
	            
	     }

	}
	
	/**
	 * Método que genera el reporte de Entrega Transportadora
	 * 
	 * @param forma
	 * @param request
	 * @param usuario
	 * @param ins
	 */
	private void imprimirEntregaTransportadora(ArqueoEntregaParcialForm forma,  HttpServletRequest request, UsuarioBasico usuario, InstitucionBasica ins) {
		
	    String nombreArchivo="";
	    forma.setListaNombresReportes(new ArrayList<String>());
	    
    	UsuariosDelegate usu= new UsuariosDelegate();
		Usuarios usuarioCompleto=usu.findById(usuario.getLoginUsuario());
		
		//--------------TITULO REPORTE---------------------------
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
    	//-------------------------------------------------------
    	
    	//--------------ENCABEZADO REPORTE-----------------------
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
    	forma.getDtoInformacionEntrega().setTotalesDocumentoDTOs(forma.getConsolidadoMovimientoDTO().getTotalesDocumentoDTOs());
    	forma.getDtoInformacionEntrega().setCajaMayorPrincipal(forma.getDtoTempInformacionEntrega().getCajaMayorPrincipal());
    	//----------------------------------------------------
    	
    	JasperReportBuilder reporte=null;
		
    	GeneradorReporteEntregaTransportadora generadorReporte =
	            new GeneradorReporteEntregaTransportadora(forma.getDtoFiltroReporte(), forma.getDtoInformacionEntrega(), forma.isConsulta(), forma.getMostrarParaSeccionEspecial());
        reporte = generadorReporte.generarReporte();
        nombreArchivo = generadorReporte.exportarReportePDF(reporte, "ImpresionEntregaTransportadora");
        forma.getListaNombresReportes().add(nombreArchivo);

        forma.setTipoReporte(ConstantesBD.codigoNuncaValido+"");
            
     }

}