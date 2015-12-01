/*
 * Mayo 10, 2010
 */
package com.servinte.axioma.mundo.impl.tesoreria;

import java.math.BigDecimal;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dto.administracion.DtoParametros;
import com.princetonsa.dto.tesoreria.DtoBusquedaCierreArqueo;
import com.princetonsa.dto.tesoreria.DtoConsolidadoCierreReporte;
import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoConsultaEntregaTransportadora;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladoRecaudoMayorEnCierre;
import com.princetonsa.dto.tesoreria.DtoCuadreCaja;
import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.princetonsa.dto.tesoreria.DtoEntregaCaja;
import com.princetonsa.dto.tesoreria.DtoFaltanteSobrante;
import com.princetonsa.dto.tesoreria.DtoFormaPagoDocSoporte;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.princetonsa.dto.tesoreria.DtoReciboDevolucion;
import com.princetonsa.dto.tesoreria.DtoTotalesDocumento;
import com.princetonsa.dto.tesoreria.DtoTotalesParciales;
import com.princetonsa.dto.tesoreria.DtoTrasladoCaja;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IMovimientosCajaDAO;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IAceptacionTrasladoCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IAnulacionRecibosCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaAcepTrasCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaTransportadoraMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaXAnulaReciboCMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaXDevolReciboMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaXEntregaCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICierreCajaXReciboCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaArqueoCierreCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDevolRecibosCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDocSopMovimCajasMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IEntidadesFinancierasMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaCajaMayorMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaTransportadoraValoresMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IFaltanteSobranteMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IFormasPagoMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IRecibosCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ISolicitudTrasladoCajaMundo;
import com.servinte.axioma.orm.AceptacionTrasladoCaja;
import com.servinte.axioma.orm.AnulacionRecibosCaja;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.DetFaltanteSobrante;
import com.servinte.axioma.orm.DocSopMovimCajas;
import com.servinte.axioma.orm.EntregaCajaMayor;
import com.servinte.axioma.orm.EntregaTransportadora;
import com.servinte.axioma.orm.FormasPago;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.RecibosCaja;
import com.servinte.axioma.orm.SolicitudTrasladoCaja;
import com.servinte.axioma.orm.SolicitudTrasladoCajaHome;
import com.servinte.axioma.orm.TrasladoCajaPrincipalMayor;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.persistencia.UtilidadPersistencia;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado con los
 * Movimientos de Caja
 * 
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see IMovimientosCajaMundo
 */
@SuppressWarnings("rawtypes")
public class MovimientosCajaMundo implements IMovimientosCajaMundo {

	/**
	 * Objeto que implementa la interfaz IMovimientosCajaDAO
	 */
	private IMovimientosCajaDAO movimientosCajaDAO;
	
	 
	/**
	 * Constructor de la clase
	 */
	public MovimientosCajaMundo() {
		inicializar();
	}
	
	/**
	 * M&eacute;todo que inicializa un objeto que implementa la interfaz IMovimientosCajaDAO
	 */
	private void inicializar() {
		movimientosCajaDAO = TesoreriaFabricaDAO.crearMovimientosCajaDAO();
	}
	
	
	@Override 
	public MovimientosCaja obtenerUltimoTurnoCierre(Cajas caja, MovimientosCaja movimientoCaja) 
	{
		return movimientosCajaDAO.obtenerUltimoTurnoCierre(caja, movimientoCaja);
	}
	

	@Override
	public MovimientosCaja obtenerMovimientoCaja(long pk) 
	{
		UtilidadTransaccion.getTransaccion().begin();
		MovimientosCaja movimientosCaja = movimientosCajaDAO.findById(pk);
		UtilidadTransaccion.getTransaccion().commit();
		return movimientosCaja;
	}

	
	@Override
	public SolicitudTrasladoCaja obtenerSolicitudTraslado(long idMovimiento) {
		return movimientosCajaDAO.obtenerSolicitudTraslado(idMovimiento);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo#obtenerConsolidadoMovimiento(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public DtoConsolidadoMovimiento obtenerConsolidadoMovimiento(MovimientosCaja movimientosCaja)
	{
		
		DtoConsolidadoMovimiento consolidadoMovimientoDTO = consultarMovimientosDeCaja(movimientosCaja);
		
		double valorBase = 0;
		
		int institucion =  movimientosCaja.getTurnoDeCaja().getUsuarios().getInstituciones().getCodigo();
		
		if(movimientosCaja.getTurnoDeCaja().getValorBase()!=null){
		
			if(movimientosCaja.getTurnoDeCaja().getValorBase().doubleValue()>0){
				
				valorBase 	= movimientosCaja.getTurnoDeCaja().getValorBase().doubleValue();
			}
		}
		
		consolidadoMovimientoDTO = consolidarInformacionPorFormaPago(consolidadoMovimientoDTO, valorBase, institucion);
		
		return consolidadoMovimientoDTO;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo#obtenerConsolidadoMovimiento(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public DtoConsolidadoMovimiento obtenerConsolidadoMovimientoArqueoCaja(MovimientosCaja movimientosCaja)
	{
		
		DtoConsolidadoMovimiento consolidadoMovimientoDTO = consultarMovimientosDeCajaPorFecha(movimientosCaja);
		
		double valorBase = 0;
		
		int institucion =  movimientosCaja.getTurnoDeCaja().getUsuarios().getInstituciones().getCodigo();
		
		if(movimientosCaja.getTurnoDeCaja().getValorBase()!=null){
		
			if(movimientosCaja.getTurnoDeCaja().getValorBase().doubleValue()>0){
				
				valorBase 	= movimientosCaja.getTurnoDeCaja().getValorBase().doubleValue();
			}
		}
		
		consolidadoMovimientoDTO = consolidarInformacionPorFormaPago(consolidadoMovimientoDTO, valorBase, institucion);
		
		return consolidadoMovimientoDTO;
	}

	
	
	/**
	 * M&eacute;todo que se encarga de realizar la consulta de los movimientos de caja para obtener la informaci&oacute;n 
	 * necesaria para iniciar los procesos de Arqueos (Arqueo Caja, Arqueo Parcial, Cierre Turno de Caja).
	 * 
	 * Los movimientos de caja son:
	 * 
	 * Recibos de caja
	 * Anulaciones de recibos de caja
	 * Devoluciones de Recibos de caja
	 * Egresos
	 * Solicitudes de Traslados de caja realizadas y aceptadas.
	 * Entregas a transportadora de valores o cajas mayor/principal.
	 * 
	 * @param movimientosCaja
	 * @return
	 */
	private DtoConsolidadoMovimiento consultarMovimientosDeCaja(MovimientosCaja movimientosCaja) {
		
		IRecibosCajaMundo recibosCajaMundo 							= TesoreriaFabricaMundo.crearRecibosCajaMundo();
		IDevolRecibosCajaMundo devolRecibosCajaMundo 				= TesoreriaFabricaMundo.crearDevolRecibosCajaMundo();
		ISolicitudTrasladoCajaMundo solicitudTrasladoCajaMundo 		= TesoreriaFabricaMundo.crearSolicitudTrasladoCajaMundo();
		IFaltanteSobranteMundo faltanteSobranteMundo				= TesoreriaFabricaMundo.crearFaltanteSobranteMundo();
		IEntregaCajaMayorMundo entregaCajaMayorMundo				= TesoreriaFabricaMundo.crearEntregaCajaMayorMundo();
		IEntregaTransportadoraValoresMundo entregaTransValoresMundo = TesoreriaFabricaMundo.crearEntregaTransportadoraValoresMundo();
		
		DtoConsolidadoMovimiento consolidadoMovimientoDTO = new DtoConsolidadoMovimiento();

		consolidadoMovimientoDTO.setReciboCajaDTOs((ArrayList<DtoReciboDevolucion>) recibosCajaMundo.obtenerRecibosXMovimientoCaja(movimientosCaja));
		
		consolidadoMovimientoDTO.setDevolucionReciboCajaDTOs((ArrayList<DtoReciboDevolucion>) devolRecibosCajaMundo.obtenerDevolRecibosCajaXMovimientoCaja(movimientosCaja));
		
		consolidadoMovimientoDTO.setTrasladoCajaDTOs((ArrayList<DtoTrasladoCaja>) solicitudTrasladoCajaMundo.obtenerSolicitudesAceptadasXTurnoCajaCajero(movimientosCaja.getTurnoDeCaja()));
		
		consolidadoMovimientoDTO.setEntregaCajaMayorPrincipalDTOs((ArrayList<DtoEntregaCaja>) entregaCajaMayorMundo.obtenerEntregasCajaMayor(movimientosCaja));
		
		consolidadoMovimientoDTO.setEntregaTransportadoraValoresDTOs((ArrayList<DtoEntregaCaja>) entregaTransValoresMundo.obtenerEntregasTransportadoraValores(movimientosCaja));
		
		ordenarTrasladoCajaRecaudoPrincMayorEnCierre(movimientosCaja,consolidadoMovimientoDTO);
		
		consolidadoMovimientoDTO.setTotalesDocumentoDTOs(totalizarDocumentosArqueo (consolidadoMovimientoDTO));
	
		consolidadoMovimientoDTO.setNumeroTotalDocumentos(numeroTotalDocumentosArqueo(consolidadoMovimientoDTO.getTotalesDocumentoDTOs()));
		
		// Aplica solo en las Aceptaciones de Solicitudes de Traslado para la apertura de Turno de Caja
		consolidadoMovimientoDTO.setFaltanteSobranteDTOs((ArrayList<DtoFaltanteSobrante>) faltanteSobranteMundo.obtenerFaltantesSobrantesPorMovimiento(movimientosCaja.getCodigoPk()));

		consolidadoMovimientoDTO.setMovimientosCaja(movimientosCaja);
		
		consolidadoMovimientoDTO = totalesParcialesTrasladosEntregas(consolidadoMovimientoDTO);
		
		return consolidadoMovimientoDTO;
	}
	
	/**
	 * M&eacute;todo que se encarga de realizar la consulta de los movimientos de caja para obtener la informaci&oacute;n 
	 * necesaria para iniciar los procesos de Arqueos (Arqueo Caja, Arqueo Parcial, Cierre Turno de Caja).
	 * 
	 * Los movimientos de caja son:
	 * 
	 * Recibos de caja
	 * Anulaciones de recibos de caja
	 * Devoluciones de Recibos de caja
	 * Egresos
	 * Solicitudes de Traslados de caja realizadas y aceptadas.
	 * Entregas a transportadora de valores o cajas mayor/principal.
	 * 
	 * @param movimientosCaja
	 * @return
	 */
	private DtoConsolidadoMovimiento consultarMovimientosDeCajaPorFecha(MovimientosCaja movimientosCaja) {
		
		IRecibosCajaMundo recibosCajaMundo 							= TesoreriaFabricaMundo.crearRecibosCajaMundo();
		IDevolRecibosCajaMundo devolRecibosCajaMundo 				= TesoreriaFabricaMundo.crearDevolRecibosCajaMundo();
		ISolicitudTrasladoCajaMundo solicitudTrasladoCajaMundo 		= TesoreriaFabricaMundo.crearSolicitudTrasladoCajaMundo();
		IFaltanteSobranteMundo faltanteSobranteMundo				= TesoreriaFabricaMundo.crearFaltanteSobranteMundo();
		IEntregaCajaMayorMundo entregaCajaMayorMundo				= TesoreriaFabricaMundo.crearEntregaCajaMayorMundo();
		IEntregaTransportadoraValoresMundo entregaTransValoresMundo = TesoreriaFabricaMundo.crearEntregaTransportadoraValoresMundo();
		
		DtoConsolidadoMovimiento consolidadoMovimientoDTO = new DtoConsolidadoMovimiento();

		consolidadoMovimientoDTO.setReciboCajaDTOs((ArrayList<DtoReciboDevolucion>) recibosCajaMundo.obtenerRecibosXMovimientoCajaFecha(movimientosCaja));
		
		consolidadoMovimientoDTO.setDevolucionReciboCajaDTOs((ArrayList<DtoReciboDevolucion>) devolRecibosCajaMundo.obtenerDevolRecibosCajaXMovimientoCajaFecha(movimientosCaja));
		
		consolidadoMovimientoDTO.setTrasladoCajaDTOs((ArrayList<DtoTrasladoCaja>) solicitudTrasladoCajaMundo.obtenerSolicitudesAceptadasXTurnoCajaCajero(movimientosCaja.getTurnoDeCaja()));
		
		consolidadoMovimientoDTO.setEntregaCajaMayorPrincipalDTOs((ArrayList<DtoEntregaCaja>) entregaCajaMayorMundo.obtenerEntregasCajaMayorPorFecha(movimientosCaja));
		
		consolidadoMovimientoDTO.setEntregaTransportadoraValoresDTOs((ArrayList<DtoEntregaCaja>) entregaTransValoresMundo.obtenerEntregasTransportadoraValoresPorFecha(movimientosCaja));
		
		ordenarTrasladoCajaRecaudoPrincMayorEnCierre(movimientosCaja,consolidadoMovimientoDTO);
		
		consolidadoMovimientoDTO.setTotalesDocumentoDTOs(totalizarDocumentosArqueo (consolidadoMovimientoDTO));
		
		consolidadoMovimientoDTO.setNumeroTotalDocumentos(numeroTotalDocumentosArqueo(consolidadoMovimientoDTO.getTotalesDocumentoDTOs()));
		
		// Aplica solo en las Aceptaciones de Solicitudes de Traslado para la apertura de Turno de Caja
		consolidadoMovimientoDTO.setFaltanteSobranteDTOs((ArrayList<DtoFaltanteSobrante>) faltanteSobranteMundo.obtenerFaltantesSobrantesPorMovimiento(movimientosCaja.getCodigoPk()));

		consolidadoMovimientoDTO.setMovimientosCaja(movimientosCaja);
		
		consolidadoMovimientoDTO = totalesParcialesTrasladosEntregas(consolidadoMovimientoDTO);
		
		return consolidadoMovimientoDTO;
	}
	
	/**
	 * Consolida la informaci&oacute;n de cada una de las consultas parciales relacionadas con Recibos de Caja, 
	 * Devoluciones, Traslados, Entregas a Transportadora o a Caja mayor.
	 * 
	 * La informaci&oacute;n son los totales de cada uno de los registros anteriores por forma de pago 
	 * registrada. Al final obtenemos un consolidado seg&uacute;n la forma de pago para poder realizar 
	 * el cuadre de caja.
	 * 
	 * A la forma de pago definida en los par&aacute;metros generales, m&oacute;dulo Tesoreria - Forma Pago Efectivo, se le suma 
	 * la base en caja.

	 * @param consolidadoMovimientoDTO
	 * @param valorBase
	 * @param institucion 
	 * @return {@link DtoConsolidadoMovimiento}
	 */
	@Override
	public DtoConsolidadoMovimiento consolidarInformacionPorFormaPago(DtoConsolidadoMovimiento consolidadoMovimientoDTO, double valorBase, int institucion) {

		ArrayList<DtoCuadreCaja> cuadreCajaDTOs = new ArrayList<DtoCuadreCaja>();
		IFormasPagoMundo formasPagoMundo = TesoreriaFabricaMundo.crearFormasPagoMundo();
		List<FormasPago> formasPagos = formasPagoMundo.obtenerFormasPagos();
		
		double totalRecibosCaja = 0;
		double totalDevolRecibosCaja = 0;
		double totalTrasladosCajaRecibido = 0;
		double totalTrasladosCajaTrasladado = 0;
		double totalTrasladosCajaFaltante = 0;
		double totalEntregaTransportadoraCajaSistema = 0;
		double totalEntregaTransportadoraCajaFaltante = 0;
		double totalEntregaTransportadoraCajaEntregado = 0;
		double totalEntregaCajaMayor = 0;
		double valorSistema;
		
		/*
		 * Se recorren todas las formas de pago registradas en el sistema y se obtienen los valores de cada
		 * uno de los grupos de registros de forma de pago igual para totalizarlos, almacenando
		 * la informaci&oacute;n en un objeto DtoCuadreCaja.
		 */
		
		for (FormasPago formaPago : formasPagos) {

			DtoCuadreCaja cuadreCajaDTO = new DtoCuadreCaja();
			cuadreCajaDTO.setTipoFormaPago(formaPago.getConsecutivo());
			cuadreCajaDTO.setTipoDetalleFormaPago(formaPago.getTiposDetalleFormaPago().getCodigo());
			cuadreCajaDTO.setFormaPago(formaPago.getDescripcion());
			cuadreCajaDTO.setValorBaseEnCaja(new Double(0));
			cuadreCajaDTO.setReqTrasladoCajaRecaudo(formaPago.getReqTrasladoCajaRecaudo());
			valorSistema = 0;

			if(formaPago.getTiposDetalleFormaPago().getCodigo() == ConstantesBD.codigoTipoDetalleFormasPagoNinguno){
				
				if(valorBase>0){
				
					if(esFormaPagoEfectivo(institucion, formaPago.getConsecutivo(), valorBase)){
						
						valorSistema += valorBase;
						cuadreCajaDTO.setValorBaseEnCaja(valorBase);
					}
					
//					
//					String consecutivoFormaPago = ValoresPorDefecto.getFormaPagoEfectivo(institucion);
//					
//					if(UtilidadTexto.isNumber(consecutivoFormaPago)){
//					
//						int consecutivo = Integer.parseInt(consecutivoFormaPago);
//						
//						if(formaPago.getConsecutivo() == consecutivo){
//							
							
//						}
//					}
				}
			}
		
			for (DtoReciboDevolucion reciboCajaDTO : consolidadoMovimientoDTO.getReciboCajaDTOs()) {

				if (reciboCajaDTO.getTipoFormaPago() == formaPago.getConsecutivo()) {

					if (reciboCajaDTO.getCodigoEstadoReciboCaja()!=ConstantesBD.codigoEstadoReciboCajaAnulado){
						valorSistema += reciboCajaDTO.getValor();
						totalRecibosCaja += reciboCajaDTO.getValor();
					}
				}
			}
			
			for (DtoReciboDevolucion devolucionReciboCaja : consolidadoMovimientoDTO.getDevolucionReciboCajaDTOs()){

				if (devolucionReciboCaja.getTipoFormaPago() == formaPago.getConsecutivo()){

					if (!devolucionReciboCaja.getAcronimoEstadoDevolucion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoEstadoAnulado)){			
				 
						valorSistema -= devolucionReciboCaja.getValor();
						totalDevolRecibosCaja += devolucionReciboCaja.getValor();
					}
				}
			}

			for (DtoTrasladoCaja trasladoCajaDTO : consolidadoMovimientoDTO.getTrasladoCajaDTOs()) {

				if (trasladoCajaDTO.getTipoFormaPago() == formaPago.getConsecutivo()) {

					valorSistema += trasladoCajaDTO.getValorRecibido();
					totalTrasladosCajaRecibido += trasladoCajaDTO.getValorRecibido();
					totalTrasladosCajaTrasladado += trasladoCajaDTO.getValorTrasladado();
					
					if(trasladoCajaDTO.getValorFaltante() != null){
						totalTrasladosCajaFaltante += trasladoCajaDTO.getValorFaltante().doubleValue();
					}
				}
			}

			for (DtoEntregaCaja entregaTransportadoraDTO : consolidadoMovimientoDTO.getEntregaTransportadoraValoresDTOs()) {

				if (entregaTransportadoraDTO.getTipoFormaPago() == formaPago.getConsecutivo()) {

					if(formaPago.getTiposDetalleFormaPago().getCodigo() == ConstantesBD.codigoTipoDetalleFormasPagoNinguno){
						
						valorSistema -= entregaTransportadoraDTO.getValorEntregado();
						totalEntregaTransportadoraCajaSistema += entregaTransportadoraDTO.getValorEntregado();
						
					}else{
						
						valorSistema -= entregaTransportadoraDTO.getValorSistema();
						totalEntregaTransportadoraCajaSistema += entregaTransportadoraDTO.getValorSistema();
					}
					
					totalEntregaTransportadoraCajaEntregado += entregaTransportadoraDTO.getValorEntregado();
					
					if(entregaTransportadoraDTO.getValorFaltante() != null){
						
						totalEntregaTransportadoraCajaFaltante += entregaTransportadoraDTO.getValorFaltante().doubleValue();
					}
				}
			}
			
			for (DtoEntregaCaja entregaCajaMayorDTO : consolidadoMovimientoDTO.getEntregaCajaMayorPrincipalDTOs()) {

				if (entregaCajaMayorDTO.getTipoFormaPago() == formaPago.getConsecutivo()) {

					valorSistema -= entregaCajaMayorDTO.getValorEntregado();
					totalEntregaCajaMayor += entregaCajaMayorDTO.getValorEntregado();
				}
			}

			// Aceptaciones de Solicitudes de Traslado para la apertura de Turno de Caja
			for (DtoFaltanteSobrante faltanteSobranteDTO : consolidadoMovimientoDTO.getFaltanteSobranteDTOs()) {

				if (faltanteSobranteDTO.getTipoFormaPago() == formaPago.getConsecutivo()) {

					if(faltanteSobranteDTO.getTipoDiferencia()==ConstantesIntegridadDominio.acronimoDiferenciaFaltante){
						
						valorSistema -= faltanteSobranteDTO.getValorDiferencia();
					
					}else{
						
						valorSistema += faltanteSobranteDTO.getValorDiferencia();
					}
				}
			}
			
			if(valorSistema!=0){
	
				cuadreCajaDTO.setValorSistema(valorSistema);
				cuadreCajaDTOs.add(cuadreCajaDTO);
			}
		}
		
		consolidadoMovimientoDTO.setTotalRecibosCaja(totalRecibosCaja);
		consolidadoMovimientoDTO.setTotalDevolRecibosCaja(totalDevolRecibosCaja);
		consolidadoMovimientoDTO.setTotalTrasladosCajaRecibido(totalTrasladosCajaRecibido);
		consolidadoMovimientoDTO.setTotalTrasladosCajaTrasladado(totalTrasladosCajaTrasladado);
		consolidadoMovimientoDTO.setTotalTrasladosCajaFaltante(totalTrasladosCajaFaltante);
		consolidadoMovimientoDTO.setTotalEntregaTransportadoraCajaSistema(totalEntregaTransportadoraCajaSistema);
		consolidadoMovimientoDTO.setTotalEntregaTransportadoraCajaEntregado(totalEntregaTransportadoraCajaEntregado);
		consolidadoMovimientoDTO.setTotalEntregaTransportadoraCajaFaltante(totalEntregaTransportadoraCajaFaltante);
		consolidadoMovimientoDTO.setTotalEntregaCajaMayor(totalEntregaCajaMayor);
		consolidadoMovimientoDTO.setCuadreCajaDTOs(cuadreCajaDTOs);
		consolidadoMovimientoDTO.setValorBase(valorBase);
		
		return consolidadoMovimientoDTO;
	}
	
	/**
	 * Método que permite agrupar los valores de las secciones Registro de Traslado a Caja de Recaudo Realizado en el cierre y 
	 * Registro Entrega Caja Mayor / Principal Realizado en el cierre, de la presentación del cierre turno
	 * @param movimientosCaja
	 * @param consolidadoMovimientoDTO
	 */
	public void ordenarTrasladoCajaRecaudoPrincMayorEnCierre(MovimientosCaja movimientosCaja, DtoConsolidadoMovimiento consolidadoMovimientoDTO)
	{
		ISolicitudTrasladoCajaMundo solicitudTrasladoCajaMundo 		= TesoreriaFabricaMundo.crearSolicitudTrasladoCajaMundo();
		ArrayList<DtoConsultaTrasladoRecaudoMayorEnCierre> listaTraslados=solicitudTrasladoCajaMundo.consultarTrasladosCajaRecaudoMayorEnCierre(movimientosCaja.getTurnoDeCaja());
		
		DtoConsultaTrasladoRecaudoMayorEnCierre dtoConsultaTrasladoRecaudo=null;
		DtoConsultaTrasladoRecaudoMayorEnCierre dtoConsultaTrasladoPrincMayor=null;
		ArrayList<DtoTotalesParciales> listaTotales;
		DtoTotalesParciales total;
		ArrayList<Integer> listaTiposMovimiento= new ArrayList<Integer>();
		double totalTrasladadoCajaRecaudo=0, totalTrasladadoCajaMayor=0;
		
		
		for(DtoConsultaTrasladoRecaudoMayorEnCierre dtoTraslado:listaTraslados)
		{
			if(dtoTraslado.getCodigoTipoMovimiento().equals(ConstantesBD.codigoTipoMovimientoSolicitudTraslado)
					&&!listaTiposMovimiento.contains(dtoTraslado.getCodigoTipoMovimiento()))
			{
				listaTotales= new ArrayList<DtoTotalesParciales>();
				ArrayList<String> listaFormasPago= new ArrayList<String>();
				for(DtoConsultaTrasladoRecaudoMayorEnCierre dtoTrasladoInterno:listaTraslados)
				{
					if(!UtilidadTexto.isEmpty(dtoTrasladoInterno.getDescripcionFormaPago()))
					{
						if(dtoTrasladoInterno.getCodigoTipoMovimiento().equals(ConstantesBD.codigoTipoMovimientoSolicitudTraslado)
								&&!listaFormasPago.contains(dtoTrasladoInterno.getDescripcionFormaPago()))
						{
							double totalPorFormaPago=0;
							for(DtoConsultaTrasladoRecaudoMayorEnCierre dtoTrasladoPorFormaPago:listaTraslados)
							{
								if(dtoTrasladoPorFormaPago.getCodigoTipoMovimiento().equals(ConstantesBD.codigoTipoMovimientoSolicitudTraslado)
										&&dtoTrasladoPorFormaPago.getDescripcionFormaPago().equals(dtoTrasladoInterno.getDescripcionFormaPago()))
								{
									totalPorFormaPago+=dtoTrasladoPorFormaPago.getValorTrasladado().doubleValue();
								}
							}
							total = new DtoTotalesParciales();
							total.setFormaPago(dtoTrasladoInterno.getDescripcionFormaPago());
							total.setTotal(totalPorFormaPago);
							totalTrasladadoCajaRecaudo+=dtoTrasladoInterno.getValorTrasladado().doubleValue();
							listaTotales.add(total);
							listaFormasPago.add(dtoTrasladoInterno.getDescripcionFormaPago());
						}	
					}
				}
				if(!listaTotales.isEmpty())
				{
					dtoTraslado.setTotalesTraslado(listaTotales);
					listaTiposMovimiento.add(dtoTraslado.getCodigoTipoMovimiento());
					dtoConsultaTrasladoRecaudo=dtoTraslado;
				}
			}
			
			if(dtoTraslado.getCodigoTipoMovimiento().equals(ConstantesBD.codigoTipoMovimientoEntregaCajaPrincipalMayor)
					&&!listaTiposMovimiento.contains(dtoTraslado.getCodigoTipoMovimiento()))
			{
				listaTotales= new ArrayList<DtoTotalesParciales>();
				ArrayList<String> listaFormasPago= new ArrayList<String>();
				for(DtoConsultaTrasladoRecaudoMayorEnCierre dtoTrasladoInterno:listaTraslados)
				{
					if(!UtilidadTexto.isEmpty(dtoTrasladoInterno.getDescripcionFormaPago()))
					{
						if(dtoTrasladoInterno.getCodigoTipoMovimiento().equals(ConstantesBD.codigoTipoMovimientoEntregaCajaPrincipalMayor)
								&&!listaFormasPago.contains(dtoTrasladoInterno.getDescripcionFormaPago()))
						{
							double totalPorFormaPago=0;
							for(DtoConsultaTrasladoRecaudoMayorEnCierre dtoTrasladoPorFormaPago:listaTraslados)
							{
								if(dtoTrasladoPorFormaPago.getCodigoTipoMovimiento().equals(ConstantesBD.codigoTipoMovimientoEntregaCajaPrincipalMayor)
										&&dtoTrasladoPorFormaPago.getDescripcionFormaPago().equals(dtoTrasladoInterno.getDescripcionFormaPago()))
								{
									totalPorFormaPago+=dtoTrasladoPorFormaPago.getValorTrasladado().doubleValue();
								}
							}
							total = new DtoTotalesParciales();
							total.setFormaPago(dtoTrasladoInterno.getDescripcionFormaPago());
							total.setTotal(totalPorFormaPago);
							totalTrasladadoCajaMayor+=dtoTrasladoInterno.getValorTrasladado().doubleValue();
							listaTotales.add(total);
							listaFormasPago.add(dtoTrasladoInterno.getDescripcionFormaPago());
						}
					}
				}
				if(!listaTotales.isEmpty())
				{
					dtoTraslado.setTotalesTraslado(listaTotales);
					listaTiposMovimiento.add(dtoTraslado.getCodigoTipoMovimiento());
					dtoConsultaTrasladoPrincMayor=dtoTraslado;
				}
			}
		}
		
		consolidadoMovimientoDTO.setTrasladoCajaRecaudoEnCierre(dtoConsultaTrasladoRecaudo);
		consolidadoMovimientoDTO.setTrasladoCajaMayorEnCierre(dtoConsultaTrasladoPrincMayor);
		consolidadoMovimientoDTO.setTotalTrasladoCajaRecaudoEnCierre(totalTrasladadoCajaRecaudo);
		consolidadoMovimientoDTO.setTotalTrasladoCajaMayorEnCierre(totalTrasladadoCajaMayor);
	}
	
	
	/**
	 * M&eacute;todo que se encarga de totalizar los documentos relacionados en el arqueo seg&uacute;n el estado del documento
	 * 
	 * @param consolidadoMovimientoDTO
	 * @return ArrayList<{@link DtoTotalesDocumento}>
	 */
	public ArrayList<DtoTotalesDocumento> totalizarDocumentosArqueo(DtoConsolidadoMovimiento consolidadoMovimientoDTO) {

		ArrayList<DtoTotalesDocumento> totalesDocumentosArqueo = new ArrayList<DtoTotalesDocumento>();
		HashMap<String, Integer> registros =  new HashMap<String, Integer>();
		
		int contadorAprobados = 0;
		int contadorAnulados = 0;
		
		for (DtoReciboDevolucion reciboCajaDTO : consolidadoMovimientoDTO.getReciboCajaDTOs()) {

			if (reciboCajaDTO.getCodigoEstadoReciboCaja()!=ConstantesBD.codigoEstadoReciboCajaAnulado){
				
				if(!registros.containsKey(reciboCajaDTO.getNumeroReciboCaja()+"")){
					registros.put(reciboCajaDTO.getNumeroReciboCaja()+"", 0);
					contadorAprobados++;
				}
			}else{
				
				if(!registros.containsKey(reciboCajaDTO.getNumeroReciboCaja()+"")){
					registros.put(reciboCajaDTO.getNumeroReciboCaja()+"", 0);
					contadorAnulados++;
				}
			}
		}
		
		totalesDocumentosArqueo.add(crearDTOTotalesDocumentoArqueo("Recibos de Caja Recaudados", 0, contadorAprobados));
		totalesDocumentosArqueo.add(crearDTOTotalesDocumentoArqueo("Recibos de Caja Anulados", 0, contadorAnulados));
		
		contadorAprobados=0;
		contadorAnulados=0;
		registros.clear();
		
		for (DtoReciboDevolucion devolucionReciboCaja : consolidadoMovimientoDTO.getDevolucionReciboCajaDTOs()){

			if (!devolucionReciboCaja.getAcronimoEstadoDevolucion().equalsIgnoreCase(ConstantesIntegridadDominio.acronimoEstadoAnulado)){
				
				if(!registros.containsKey(devolucionReciboCaja.getCodigoDevolucion()+"")){
					registros.put(devolucionReciboCaja.getCodigoDevolucion()+"", 1);
					contadorAprobados++;
				}
				
			}else{
				
				if(!registros.containsKey(devolucionReciboCaja.getCodigoDevolucion()+"")){
					registros.put(devolucionReciboCaja.getCodigoDevolucion()+"", 1);
					contadorAnulados++;
				}
			}
		}
		
		totalesDocumentosArqueo.add(crearDTOTotalesDocumentoArqueo("Devoluciones de Recibos de Caja Aprobadas", 1, contadorAprobados));
		totalesDocumentosArqueo.add(crearDTOTotalesDocumentoArqueo("Devoluciones de Recibos de Caja Anuladas", 1, contadorAnulados));
		registros.clear();
		contadorAprobados=0;
		
		for (DtoTrasladoCaja dtoTrasladoCaja : consolidadoMovimientoDTO.getTrasladoCajaDTOs()) {
			
			if(!registros.containsKey(dtoTrasladoCaja.getIdMovimientoCaja()+"")){
				registros.put(dtoTrasladoCaja.getIdMovimientoCaja()+"", 2);
				contadorAprobados++;
			}
		}
	
		totalesDocumentosArqueo.add(crearDTOTotalesDocumentoArqueo("Traslados de Caja de Recaudo Recibidos", 2, contadorAprobados));
		registros.clear();
		contadorAprobados=0;
		
		for (DtoEntregaCaja dtoEntregaTransCaja : consolidadoMovimientoDTO.getEntregaTransportadoraValoresDTOs()) {
			
			if(!registros.containsKey(dtoEntregaTransCaja.getIdMovimientoCaja()+"")){
				registros.put(dtoEntregaTransCaja.getIdMovimientoCaja()+"", 3);
				contadorAprobados++;
			}
		}
		
		totalesDocumentosArqueo.add(crearDTOTotalesDocumentoArqueo("Entregas a Transportadora de Valores", 3, contadorAprobados));
		registros.clear();
		contadorAprobados=0;
		
		
		for (DtoEntregaCaja dtoEntregaCajaMayor : consolidadoMovimientoDTO.getEntregaCajaMayorPrincipalDTOs()) {
			
			if(!registros.containsKey(dtoEntregaCajaMayor.getIdMovimientoCaja()+"")){
				registros.put(dtoEntregaCajaMayor.getIdMovimientoCaja()+"", 4);
				contadorAprobados++;
			}
		}
		
		totalesDocumentosArqueo.add(crearDTOTotalesDocumentoArqueo("Entregas a Caja Mayor Principal",  4, contadorAprobados));

		return totalesDocumentosArqueo;
	}
	
	/**
	 * M&eacute;todo que se encarga de definir si existen documentos relacionados en el arqueo
	 * 
	 * @param consolidadoMovimientoDTO
	 * @return ArrayList<{@link DtoTotalesDocumento}>
	 */
	public int numeroTotalDocumentosArqueo(ArrayList<DtoTotalesDocumento> dtoTotalesDocumento) {
		int totalDocumentos=0; 
		
		for (DtoTotalesDocumento dtoTotalesDocumentoTemp : dtoTotalesDocumento) {
			totalDocumentos += dtoTotalesDocumentoTemp.getTotal();
		}
		
		return totalDocumentos;
	}
	
	/**
	 * Fabrica que crea objetos {@link DtoTotalesDocumento} con la informaci&oacute;n
	 * pasada como par&aacute;metro.
	 *
	 * @param descripcion
	 * @param tipo
	 * @param total
	 * @return
	 */
	public DtoTotalesDocumento crearDTOTotalesDocumentoArqueo (String descripcion, int tipo, int total){
		
		DtoTotalesDocumento totalesDocumento =  new DtoTotalesDocumento();
		totalesDocumento.setDescripcion(descripcion);
		totalesDocumento.setTotal(total);
		totalesDocumento.setTipo(tipo);

		return totalesDocumento;
	}
	
	
	/**
	 * M&eacute;todo que totaliza los valores parciales por forma de pago de los Traslados de caja recibidos y las 
	 * Entregas a transportadora de valores y las Entregas a Caja Mayor principal realizadas.
	 * 
	 * @param consolidadoMovimientoDTO
	 * @return {@link DtoConsolidadoMovimiento}
	 */
	public DtoConsolidadoMovimiento totalesParcialesTrasladosEntregas (DtoConsolidadoMovimiento consolidadoMovimientoDTO){
		
		IFormasPagoMundo formasPagoMundo = TesoreriaFabricaMundo.crearFormasPagoMundo();
		List<FormasPago> formasPagos = formasPagoMundo.obtenerFormasPagos();
		
		ArrayList<DtoTotalesParciales> totalesParcialesTrasladosDTOs 		= new ArrayList<DtoTotalesParciales>();
		ArrayList<DtoTotalesParciales> totalesParcialesEntrTransDTOs 		= new ArrayList<DtoTotalesParciales>();
	    ArrayList<DtoTotalesParciales> totalesParcialesEntrCajaDTOs 		= new ArrayList<DtoTotalesParciales>();
		
	    double valorParcial;
		double valorParcialRegistrado;
		double valorParcialFaltante;
		
		for (FormasPago formaPago : formasPagos) {
			
			valorParcial = 0;
			valorParcialRegistrado = 0;
			valorParcialFaltante = 0;
			
			for (DtoTrasladoCaja dtoTrasladoCaja : consolidadoMovimientoDTO.getTrasladoCajaDTOs()) {
				
				if (dtoTrasladoCaja.getTipoFormaPago() == formaPago.getConsecutivo()) {
					
					valorParcial+= dtoTrasladoCaja.getValorRecibido();
					valorParcialRegistrado += dtoTrasladoCaja.getValorTrasladado();
					
					if(dtoTrasladoCaja.getValorFaltante() != null){
						valorParcialFaltante += dtoTrasladoCaja.getValorFaltante().doubleValue();
					}
				}
			}
	
			if(valorParcial!=0){
	
				totalesParcialesTrasladosDTOs.add(crearDTOTotalesParciales(0, formaPago, valorParcial, valorParcialRegistrado, valorParcialFaltante));
			}
		
			valorParcial = 0;
			valorParcialRegistrado = 0;
			valorParcialFaltante = 0;
			
			for (DtoEntregaCaja dtoEntregaCajaTrans : consolidadoMovimientoDTO.getEntregaTransportadoraValoresDTOs()) {
							
				if (dtoEntregaCajaTrans.getTipoFormaPago() == formaPago.getConsecutivo()) {
					
					valorParcial+= dtoEntregaCajaTrans.getValorEntregado();
					valorParcialRegistrado += dtoEntregaCajaTrans.getValorSistema();
					
					if(dtoEntregaCajaTrans.getValorFaltante() != null){
						valorParcialFaltante += dtoEntregaCajaTrans.getValorFaltante().doubleValue();
					}
				}
			}

			if(valorParcial!=0){
				
				totalesParcialesEntrTransDTOs.add(crearDTOTotalesParciales(1, formaPago, valorParcial, valorParcialRegistrado, valorParcialFaltante));
			}
			
			/*
			 * En los movimientos de caja siguientes no se registran valores parciales ni se generan faltantes o sobrantes.
			 */
			valorParcial = 0;
			valorParcialRegistrado = 0;
			valorParcialFaltante = 0;
			
			for (DtoEntregaCaja dtoEntregaCajaMayor : consolidadoMovimientoDTO.getEntregaCajaMayorPrincipalDTOs()) {
				
				if (dtoEntregaCajaMayor.getTipoFormaPago() == formaPago.getConsecutivo()) {
					
					valorParcial+= dtoEntregaCajaMayor.getValorEntregado();
				}
			}
			
			if(valorParcial!=0){
				
				totalesParcialesEntrCajaDTOs.add(crearDTOTotalesParciales(2, formaPago, valorParcial, valorParcialRegistrado, valorParcialFaltante));
			}
			
			valorParcial = 0;
			for(DtoFaltanteSobrante faltanteSobrante : consolidadoMovimientoDTO.getFaltanteSobranteDTOs())
			{
				if (faltanteSobrante.getTipoFormaPago() == formaPago.getConsecutivo()) 
				{
					valorParcial+= faltanteSobrante.getValorDiferencia();
				}
			}
			
			if(valorParcial!=0)
			{
				DtoTotalesParciales dtoTotalesParciales = crearDTOTotalesParciales(3, formaPago, valorParcial, 0, 0);
				
				if(valorParcial>0){
					
					dtoTotalesParciales.setTipoDiferencia(ConstantesIntegridadDominio.acronimoDiferenciaSobrante);
				
				}else{
					
					dtoTotalesParciales.setTipoDiferencia(ConstantesIntegridadDominio.acronimoDiferenciaFaltante);
				}
				
				totalesParcialesEntrCajaDTOs.add(dtoTotalesParciales);
			}
		}
	
		consolidadoMovimientoDTO.setTotalesParcialesTrasladosDTOs(totalesParcialesTrasladosDTOs);
		consolidadoMovimientoDTO.setTotalesParcialesEntrTransDTOs(totalesParcialesEntrTransDTOs);
		consolidadoMovimientoDTO.setTotalesParcialesEntrCajaDTOs(totalesParcialesEntrCajaDTOs);
		
		return consolidadoMovimientoDTO;
	}
	
	
	/**
	 * Fabrica que crea objetos {@link DtoTotalesParciales} con la informaci&oacute;n
	 * pasada como par&aacute;metro.
	 * 
	 * @param tipo
	 * @param tipoFormaPago
	 * @param formaPago
	 * @param total
	 * @return {@link DtoTotalesParciales}
	 */
	public DtoTotalesParciales crearDTOTotalesParciales (int tipo, FormasPago formaPago, double total, double totalTraslado, double totalFaltante){
		
		DtoTotalesParciales totalesParciales =  new DtoTotalesParciales();
		totalesParciales.setTipo(tipo);
		totalesParciales.setTipoFormaPago(formaPago.getConsecutivo());
		totalesParciales.setFormaPago(formaPago.getDescripcion());
		totalesParciales.setTipoDetalleFormaPago(formaPago.getTiposDetalleFormaPago().getCodigo());
		totalesParciales.setTotal(total);
		totalesParciales.setTotalTraslado(totalTraslado);
		totalesParciales.setTotalFaltante(totalFaltante);
		return totalesParciales;
	}

	
	/**
	 * Retorna un valor boolean que determina si el movimiento fue registrado satisfactoriamente
	 * @param dtoConsolidadoMovimiento
	 * 
	 * @return boolean que determina si el movimiento fue registrado satisfactoriamente
	 *
	 */
	@Override
	public boolean guardarMovimientoCaja(DtoConsolidadoMovimiento dtoConsolidadoMovimiento) {

		IMovimientosCajaDAO movimientosCajaDAO = TesoreriaFabricaDAO.crearMovimientosCajaDAO();
		//ArrayList<DtoReciboDevolucion> listadoDevolRecibos = null;
		
//		/*
//		 *  Confirma si se realiz&oacute; un registro con forma pago tipo Efectivo
//		 *  Solo se aplica en el caso que el dtoConsolidadoMovimiento sea una instancia de DtoInformacionEntrega, es decir
//		 *  para los movimientos de Arqueo Parcial y Cierre Turno de Caja
//		 */
//		if(dtoConsolidadoMovimiento instanceof DtoInformacionEntrega)
//		{
//			DtoInformacionEntrega dtoInformacionEntrega = (DtoInformacionEntrega) dtoConsolidadoMovimiento;
//			
//			if(dtoInformacionEntrega.isRegistroEfectivo()){
//				
//				Log4JManager.info("------------------------- se registro efectivo ------------");
//				
//				IDevolRecibosCajaMundo devolRecibosCajaMundo = TesoreriaMundoFabrica.crearDevolRecibosCajaMundo();
//				
//				// Se obtienen las devoluciones asociadas al turno y que no han sido registradas en movimientos anteriores
//				double totalDevolucionesEfectivo = devolRecibosCajaMundo.obtenerTotalDevolucionesDisponiblesXEstadoXTurnoCaja(dtoConsolidadoMovimiento.getMovimientosCaja(), ConstantesIntegridadDominio.acronimoEstadoAprobado);
//				
//				if(totalDevolucionesEfectivo>0){
//					
//					/*
//					 * Se verifica el valor a entregar para la forma de pago efectivo y si es igual o mayor
//					 * al valor de las devoluciones, se realiza la consulta de las devoluciones disponibles para este movimiento 
//					 */ 
//					 
//					for (DtoCuadreCaja dtoCuadreCaja : dtoInformacionEntrega.getCuadreCajaDTOs()) {
//						
//						if(dtoCuadreCaja.getTipoDetalleFormaPago() == ConstantesBD.codigoTipoDetalleFormasPagoNinguno){
//							
//							if(dtoCuadreCaja.getValorCaja()>=totalDevolucionesEfectivo){
//								
//								listadoDevolRecibos = (ArrayList<DtoReciboDevolucion>) devolRecibosCajaMundo.obtenerDevolRecibosCajaXMovimientoCaja(dtoInformacionEntrega.getMovimientosCaja());
//							}
//							
//							break;
//						}
//					}
//				}
//			}
//		}
		
		MovimientosCaja movimientosCaja = HelperConstruirEntidad.construirEntidad(dtoConsolidadoMovimiento);	
		
		boolean registroMovimiento = movimientosCajaDAO.guardarMovimientoCaja(movimientosCaja);
		
		dtoConsolidadoMovimiento.setMovimientosCaja(movimientosCaja);
		
		/*
		 * Se actualiza y se asocian todos los movimientos asociados al cierre
		 */
		if(registroMovimiento){
		
//			if(listadoDevolRecibos!=null && listadoDevolRecibos.size()>0){
//				
//				IDevolRecibosCajaMundo devolRecibosCajaMundo = TesoreriaMundoFabrica.crearDevolRecibosCajaMundo();
//				devolRecibosCajaMundo.cambiarEstadoArqueoDevoluciones(listadoDevolRecibos);
//			}
	
			if(dtoConsolidadoMovimiento.getETipoMovimiento() == ETipoMovimiento.CIERRE_CAJA){
				
				actualizarMovimientosRegistrosCierre(movimientosCaja);
			}
			
		}else if(!registroMovimiento){
			
			Log4JManager.info("Error intentando guardar el movimiento de caja: "+ dtoConsolidadoMovimiento.getETipoMovimiento().name());
			
			int institucion = movimientosCaja.getUsuarios().getInstituciones().getCodigo();

			// Se cambian los consecutivos utilizados en la creacion de los detalles de Faltantes / Sobrantes a no usados.
			Iterator iteratorDocSop =  movimientosCaja.getDocSopMovimCajases().iterator();
			
			for (;iteratorDocSop.hasNext();)
			{
				DocSopMovimCajas docSop = (DocSopMovimCajas) iteratorDocSop.next();
				Iterator iteratorDetFal =  docSop.getDetFaltanteSobrantes().iterator();
				
				for (;iteratorDetFal.hasNext();) 
				{
					DetFaltanteSobrante detFaltanteSobrante = (DetFaltanteSobrante) iteratorDetFal.next();
					Connection con=UtilidadBD.abrirConexion();
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoRegistroFaltanteSobrante , institucion, detFaltanteSobrante.getConsecutivo().toString(), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
					UtilidadBD.closeConnection(con);
				}
			}
		}
		
		return registroMovimiento;
	}
	
	/**
	 * M&eacute;todo que se encarga de realizar todo el procedimiento de actualizaci&oacute;n
	 * de los recibos, devoluciones y anulaciones de recibos de caja, asociar las entregas
	 * a Transportadora de Valores, las Aceptaciones y las Solicitudes de traslado, al movimiento de 
	 * cierre realizado.
	 * 
	 * @param movimientosCaja
	 * @param listadoDevolRecibos
	 */
	private void actualizarMovimientosRegistrosCierre(MovimientosCaja movimientosCaja) {
		
		actualizarRecibosAsociados (movimientosCaja);
		actualizarDevolRecibosAsociadosCierre (movimientosCaja);
		actualizarEntregasTransportadoraValores (movimientosCaja);
		actualizarEntregasACajaMayorPrincipal (movimientosCaja);
		actualizarAceptacionesSolicitudTraslado (movimientosCaja);
		
	}


	/**
	 * M&eacute;todo que se encarga de realizar el procedimiento de actualizaci&oacute;n
	 * del estado de los recibos de caja a 'Cierre' y de asociarlos al movimiento de cierre pasado
	 * como par&aacute;metro (incluyendo los anulados)
	 *
	 * @param movimientosCaja
	 */
	private void actualizarRecibosAsociados(MovimientosCaja movimientosCaja) {
		
		IRecibosCajaMundo recibosCajaMundo = TesoreriaFabricaMundo.crearRecibosCajaMundo();
		
		List<RecibosCaja> listaRecibosCajaTurno = recibosCajaMundo.actualizarRecibosAsociadosCierre(movimientosCaja);

		ICierreCajaXReciboCajaMundo cierreCajaXReciboCajaMundo = TesoreriaFabricaMundo.crearCierreCajaXReciboCajaMundo();
		cierreCajaXReciboCajaMundo.asociarReciboCajaConCierreTurno (listaRecibosCajaTurno, movimientosCaja);
		
		IAnulacionRecibosCajaMundo anulacionRecibosCajaMundo = TesoreriaFabricaMundo.crearAnulacionRecibosCajaMundo();
		List<AnulacionRecibosCaja>  listaAnulacionesRecibosCaja = anulacionRecibosCajaMundo.obtenerAnulacionesRecibosCajaXTurnoCaja(movimientosCaja.getTurnoDeCaja());
	
		ICierreCajaXAnulaReciboCMundo cierreCajaXAnulaReciboCMundo = TesoreriaFabricaMundo.crearCierreCajaXAnulaReciboCMundo();
		cierreCajaXAnulaReciboCMundo.asociarAnulacionesRecibosCajaConCierreTurno(listaAnulacionesRecibosCaja, movimientosCaja);
		
	}


	/**
	 * M&eacute;todo que se encarga de realizar el procedimiento de actualizaci&oacute;n
	 * de las devoluciones de recibos de caja y de asociarlos al movimiento de cierre pasado
	 * como par&aacute;metro.
	 *
	 * @param listadoDevolRecibos
	 */
	private void actualizarDevolRecibosAsociadosCierre(MovimientosCaja movimientosCaja) {

		IDevolRecibosCajaMundo devolRecibosCajaMundo = TesoreriaFabricaMundo.crearDevolRecibosCajaMundo();
		
		//Se obtienen todas las devoluciones involucradas en el movimiento de Cierre de Turno de Caja para realizar el proceso de asociaci&oacute;n respectivo
		ArrayList<DtoReciboDevolucion> listadoDevolRecibos = (ArrayList<DtoReciboDevolucion>) devolRecibosCajaMundo.obtenerDevolRecibosCajaXMovimientoCaja (movimientosCaja);
		
		Log4JManager.info("tamaño del listado  ******************  " + listadoDevolRecibos.size());
		
		if(listadoDevolRecibos!=null && listadoDevolRecibos.size()>0){
		
			ICierreCajaXDevolReciboMundo cierreCajaXDevolReciboMundo = TesoreriaFabricaMundo.crearCierreCajaXDevolReciboMundo();
			
			cierreCajaXDevolReciboMundo.asociarDevolucionesCierreCaja(listadoDevolRecibos, movimientosCaja);
		}
	}
	
	/**
	 * M&eacute;todo que se encarga de realizar el procedimiento de actualizaci&oacute;n
	 * de las Entregas a Transportadora de valores asociandolas al movimiento de cierre pasado
	 * como par&aacute;metro.
	 * 
	 * @param movimientosCaja
	 */
	private void actualizarEntregasTransportadoraValores(MovimientosCaja movimientosCaja) {
			
		IEntregaTransportadoraValoresMundo entregaTransportadoraValoresMundo = TesoreriaFabricaMundo.crearEntregaTransportadoraValoresMundo();
		
		List<EntregaTransportadora> listaEntregaTransportadora = entregaTransportadoraValoresMundo.obtenerEntregasTransportadoraXTurnoCaja(movimientosCaja.getTurnoDeCaja());

		if(listaEntregaTransportadora!=null && listaEntregaTransportadora.size()>0){
			
			ICierreCajaTransportadoraMundo cierreCajaTransportadoraMundo = TesoreriaFabricaMundo.crearCierreCajaTransportadoraMundo();
			
			cierreCajaTransportadoraMundo.asociarEntregaTransportadoraCierreCaja (listaEntregaTransportadora, movimientosCaja);
		}
	}
	
	
	/**
	 * M&eacute;todo que se encarga de realizar el procedimiento de actualizaci&oacute;n
	 * de las Entregas a Caja Mayor / Principal asociandolas al movimiento de cierre pasado
	 * como par&aacute;metro.
	 * 
	 * @param movimientosCaja
	 */
	private void actualizarEntregasACajaMayorPrincipal (MovimientosCaja movimientosCaja) {
			
		IEntregaCajaMayorMundo entregaCajaMayorMundo = TesoreriaFabricaMundo.crearEntregaCajaMayorMundo();
		
		List<EntregaCajaMayor> listaEntregasCajaMayorPrincipal = entregaCajaMayorMundo.obtenerEntregasCajaMayorPrincipalXTurnoCaja(movimientosCaja.getTurnoDeCaja());

		if(listaEntregasCajaMayorPrincipal!=null && listaEntregasCajaMayorPrincipal.size()>0){
			
			ICierreCajaXEntregaCajaMundo cierreCajaXEntregaCajaMundo = TesoreriaFabricaMundo.crearCierreCajaXEntregaCajaMundo();
			
			cierreCajaXEntregaCajaMundo.asociarEntregaCajaCierreCaja (listaEntregasCajaMayorPrincipal, movimientosCaja);
		}
	}
	
	/**
	 * M&eacute;todo que se encarga de realizar el procedimiento de actualizaci&oacute;n
	 * de los movimientos de Aceptaci&oacute;n de las Solicitudes de Traslado a Caja asociandolas 
	 * al movimiento de cierre pasado como par&aacute;metro.
	 * 
	 * @param movimientosCaja
	 */
	private void actualizarAceptacionesSolicitudTraslado(MovimientosCaja movimientosCaja) {
		
		IAceptacionTrasladoCajaMundo aceptacionTrasladoCajaMundo = TesoreriaFabricaMundo.crearAceptacionTrasladoCajaMundo();
		
		List<AceptacionTrasladoCaja> listaSolicitudesAceptadas= aceptacionTrasladoCajaMundo.obtenerAceptacionesSolicitudTrasladoPorTurnoCaja(movimientosCaja.getTurnoDeCaja());

		if(listaSolicitudesAceptadas!=null && listaSolicitudesAceptadas.size()>0){
			
			ICierreCajaAcepTrasCajaMundo cierreCajaAcepTrasCajaMundo= TesoreriaFabricaMundo.crearCierreCajaAcepTrasCajaMundo();
			
			cierreCajaAcepTrasCajaMundo.asociarAceptacionesSolicitudTrasladoCierreCaja (listaSolicitudesAceptadas, movimientosCaja);
		}
	}

	
	/**
	 * Retorna un DTO con la informaci&oacute;n disponible para realizar una Entrega a Caja Mayor / Principal o a una Transportadora
	 * de Valores
	 * 
	 * @param movimientosCaja
	 * @param eTipoMovimiento
	 */
	@Override
	public DtoInformacionEntrega consolidadoInformacionEntrega (MovimientosCaja movimientosCaja,  ETipoMovimiento eTipoMovimiento){
		
		IRecibosCajaMundo recibosCajaMundo = TesoreriaFabricaMundo.crearRecibosCajaMundo();
		IDocSopMovimCajasMundo docSopMovimCajasMundo = TesoreriaFabricaMundo.crearDocSopMovimCajasMundo();
		IAceptacionTrasladoCajaMundo aceptacionTrasladoCajaMundo = TesoreriaFabricaMundo.crearAceptacionTrasladoCajaMundo();
		IDevolRecibosCajaMundo devolRecibosCajaMundo = TesoreriaFabricaMundo.crearDevolRecibosCajaMundo();
		
		int codigoInstitucion = movimientosCaja.getTurnoDeCaja().getUsuarios().getInstituciones().getCodigo();
		
		boolean restriccion = false;
		boolean directoBanco = false;
		
		/*
		 * El valor de la base en caja solo aplica para el Movimiento Cierre de Caja
		 */
		double valorBase = 0;
		
		if(eTipoMovimiento == ETipoMovimiento.ENTREGA_CAJA_MAYOR_PRINCIPAL){
			
			restriccion = true;
			//directoBanco = false;
			
		}else if(eTipoMovimiento == ETipoMovimiento.ENTREGA_TRANSPORTADORA_VALORES){
			
			//restriccion = false;
			directoBanco =  true;
			
		}else if(eTipoMovimiento == ETipoMovimiento.CIERRE_CAJA){
			
			restriccion = true;		
			
			if(movimientosCaja.getTurnoDeCaja().getValorBase()!=null){
				
				if(movimientosCaja.getTurnoDeCaja().getValorBase().doubleValue()>0){
					
					valorBase 	= movimientosCaja.getTurnoDeCaja().getValorBase().doubleValue();
				}
			}
		}
		
		/*
		 * Se obtienen los totales de las devoluciones de recibos de caja aprobadas y que fueron registradas 
		 * durante el turno de Caja. Los totales estan agrupados por forma de pago y se resta el valor de la devolución
		 * con su respectiva forma de pago asociada.
		 */
		 List<DtoReciboDevolucion> totalesDevoluciones = devolRecibosCajaMundo.obtenerTotalDevolucionesDisponiblesXEstadoXTurnoCaja(movimientosCaja.getTurnoDeCaja(), ConstantesIntegridadDominio.acronimoEstadoAprobado);
		
		/*
		 * Se obtiene el total (sumatoria) de los detalles de los recibos de caja que no han sido anulados y 
		 * que involucren formas de pago de tipo "Ninguno"
		 */
		List<DtoDetalleDocSopor> totalesRecibosFormaPagoNinguno = recibosCajaMundo.obtenerTotalesRecibosNoAnulFormaPagoNinguno(codigoInstitucion, movimientosCaja.getTurnoDeCaja());
		
		/*
		 * Se obtiene el total registrado en las entregas realizadas a Transportadora de Valores y Caja Mayor / Principal
		 * que tienen formas de pago de tipo "NINGUNO"
		 * 
		 */
		ArrayList<DtoDetalleDocSopor> totalesEntrTransCajaMayorPpalFormaPagoNinguno = (ArrayList<DtoDetalleDocSopor>) docSopMovimCajasMundo.obtenerTotalesEntregasFormaPagoNinguno(movimientosCaja);
		
		/*
		 * Se obtiene el total registrado en las Aceptaciones de Solicitudes de Traslado a Caja de Recaudo
		 * que tienen formas de pago de tipo "NINGUNO"
 		 * 
	 	 */
		ArrayList<DtoDetalleDocSopor> totalesAceptacionSolicitudFormaPagoNinguno = (ArrayList<DtoDetalleDocSopor>) aceptacionTrasladoCajaMundo.obtenerTotalesAceptacionSolicitudFormaPagoNinguno(movimientosCaja.getTurnoDeCaja());

		
		/*
		 * Listado que contiene los totales para las formas de pago de tipo "NINGUNO" que estan disponibles para la entrega.
		 */
		ArrayList<DtoDetalleDocSopor> totalesDefinitivosFormaPagoNinguno = new ArrayList<DtoDetalleDocSopor>();
		
		totalesDefinitivosFormaPagoNinguno.addAll(totalesRecibosFormaPagoNinguno);
		totalesDefinitivosFormaPagoNinguno.addAll(totalesAceptacionSolicitudFormaPagoNinguno);
	
		
		DtoInformacionEntrega dtoInformacionEntrega =  obtenerDtoInformacionEntregaFormaPagoNinguno (totalesEntrTransCajaMayorPpalFormaPagoNinguno, totalesDefinitivosFormaPagoNinguno, totalesDevoluciones, codigoInstitucion, valorBase, restriccion);
		
		dtoInformacionEntrega.setMovimientosCaja(movimientosCaja);
		
		/*
		 * Se tienen en cuenta los recibos de caja que no han sido anulados. Y con detalles que involucren formas de pago
		 * diferentes a tipo "Ninguno"
		 */
		 //TODO Verificar LETRA; PAGARÉ
		ArrayList<DtoDetalleDocSopor> recibosCajaNoAnulNoFormaPagoNinguno = (ArrayList<DtoDetalleDocSopor>) recibosCajaMundo.obtenerRecibosNoAnuladosNoFormaPagoNinguna(codigoInstitucion, movimientosCaja.getTurnoDeCaja(), directoBanco);
		
		/*
		 * Se obtienen los documentos de soporte relacionados a las entregas que no involucren formas de pago de tipo "NINGUNO",
		 * ya que se debe conocer cuales fueron los documentos de soporte (y detalles de pagos) que han sido entregados.
		 * 
		 */
		//TODO Verificar LETRA; PAGARÉ
		ArrayList<DtoDetalleDocSopor> entregasTransCajaMayorPpalNoFormaPagoNinguno = (ArrayList<DtoDetalleDocSopor>) docSopMovimCajasMundo.obtenerEntrTransYCajaMayorPpalNoFormaPagoNinguna(movimientosCaja);
		
		/*
		 * Se obtiene un listado de Aceptaciones de Solicitudes que no involucren formas de pago tipo "NINGUNO"
		 */
		//TODO Verificar LETRA; PAGARÉ
		ArrayList<DtoDetalleDocSopor> aceptacionesNoFormaPagoNinguno = (ArrayList<DtoDetalleDocSopor>) docSopMovimCajasMundo.obtenerAceptacionesCajaNoFormaPagoNinguno(movimientosCaja);
		
		/*
		 * Listado que contiene los documentos de soporte para las formas de pago diferente a "NINGUNO"
		 * que estan disponibles para realizar una entrega.
		 */
		ArrayList<DtoDetalleDocSopor> docSopoDefinitivosNoFormaPagoNinguno = new ArrayList<DtoDetalleDocSopor>();
		
		docSopoDefinitivosNoFormaPagoNinguno = obtenerDocSopDisponibles (recibosCajaNoAnulNoFormaPagoNinguno , entregasTransCajaMayorPpalNoFormaPagoNinguno);
		docSopoDefinitivosNoFormaPagoNinguno.addAll(obtenerDocSopDisponibles (aceptacionesNoFormaPagoNinguno , entregasTransCajaMayorPpalNoFormaPagoNinguno));

		return obtenerDtoInformacionEntregaNoFormaPagoNinguno(dtoInformacionEntrega, docSopoDefinitivosNoFormaPagoNinguno, codigoInstitucion, totalesDevoluciones, restriccion);
	
	}

	
	
	/**
	 * Retorna un {@link DtoInformacionEntrega} con la informaci&oacute;n necesaria para realizar los
	 * Arqueos Parciales (Entregas a Caja Mayor / Principal ó Entregas a Transportadora) y 
	 * Aceptaciones de Solicitudes de Traslado a Caja. Solo se tienen en cuenta las formas de pago
	 * de tipo diferente a "NINGUNO"
	 *
	 * @param listadoDefinitivoDocSop
	 * @param codigoInstitucion
	 * @param totalesDevoluciones 
	 * @param restriccion (este atributo es utilizado para poder recorrer el listado disponible de 
	 * documentos sin tener en cuenta la parametrizaci&oacute;n Consignaci&oacute;n Banco)
	 * @return
	 */

	private DtoInformacionEntrega obtenerDtoInformacionEntregaNoFormaPagoNinguno (DtoInformacionEntrega dtoInformacionEntrega, ArrayList<DtoDetalleDocSopor> listadoDefinitivoDocSop, int codigoInstitucion, List<DtoReciboDevolucion> totalesDevoluciones, boolean restriccion) {
		
		IFormasPagoMundo formasPagoMundo = TesoreriaFabricaMundo.crearFormasPagoMundo();
		
		IEntidadesFinancierasMundo entidadesFinancierasMundo = TesoreriaFabricaMundo.crearEntidadesFinancierasMundo();
		
		List<FormasPago> formasPagos = formasPagoMundo.obtenerFormasPagos();
		ArrayList<DtoEntidadesFinancieras> listadoCompletoEntidades = (ArrayList<DtoEntidadesFinancieras>) entidadesFinancierasMundo.obtenerEntidadesPorInstitucion(codigoInstitucion, false);
		
		double totalValorFormaPago;
		double totalValorParcialEntidad;
		double totalEntregadoTransportadora = 0;
		double totalDevolucionFormaPago;
		
		DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte;
		//TODO Verificar e incluir LETRA; PAGARÉ
		for (FormasPago formaPago : formasPagos) {
			
			totalDevolucionFormaPago = 0;
			
			for (DtoReciboDevolucion devolucion : totalesDevoluciones) {
				
				if(devolucion.getTipoFormaPago() == formaPago.getConsecutivo()){
					
					totalDevolucionFormaPago += devolucion.getValor();
				}
			}

			if(restriccion || formaPago.getIndConsignacion().toString().equals(ConstantesBD.acronimoSi)){

				dtoFormaPagoDocSoporte = new DtoFormaPagoDocSoporte();
				dtoFormaPagoDocSoporte.setConsecutivoFormaPago(formaPago.getConsecutivo());
				dtoFormaPagoDocSoporte.setFormaPago(formaPago.getDescripcion());
				dtoFormaPagoDocSoporte.setCodigoTipoDetalleFormaPago(formaPago.getTiposDetalleFormaPago().getCodigo());
				dtoFormaPagoDocSoporte.setIndicativoTrasladoCajaRecaudo(formaPago.getTrasladoCajaRecaudo().toString());
				dtoFormaPagoDocSoporte.setReqTrasladoCajaRecaudo(formaPago.getReqTrasladoCajaRecaudo());
				dtoFormaPagoDocSoporte.setRegistrarTraslado(formaPago.getReqTrasladoCajaRecaudo().toString());
				
				totalValorFormaPago = 0;
				
				if(formaPago.getTiposDetalleFormaPago().getCodigo() == ConstantesBD.codigoTipoDetalleFormasPagoCheque 
					|| formaPago.getTiposDetalleFormaPago().getCodigo() == ConstantesBD.codigoTipoDetalleFormasPagoTarjeta ){
					
					for (DtoEntidadesFinancieras dtoEntidadesFinancieras : listadoCompletoEntidades) {
						
						totalValorParcialEntidad = 0;
						
						DtoEntidadesFinancieras dtoEntidadFinanciera = new DtoEntidadesFinancieras();

						try {
							dtoEntidadFinanciera = dtoEntidadesFinancieras.getClass().newInstance();
						
						} catch (Exception e) {
			
							e.printStackTrace();
						}
	
						for (DtoDetalleDocSopor dtoDetalleDocSopor : listadoDefinitivoDocSop) {
							
							if(dtoDetalleDocSopor.getConsecutivoFormaPago() == formaPago.getConsecutivo() && dtoDetalleDocSopor.getConsecutivoEntFinanciera() == dtoEntidadesFinancieras.getConsecutivo()){
								
								dtoEntidadFinanciera.getListadoDtoDetDocSoporte().add(dtoDetalleDocSopor);
								
								totalValorParcialEntidad += dtoDetalleDocSopor.getValorSistemaUnico();
							}
						}
		
						if(totalValorParcialEntidad!=0){
							
							dtoEntidadFinanciera.setTotalValorSistema(totalValorParcialEntidad);
							dtoEntidadFinanciera.setDescripcionTercero(dtoEntidadesFinancieras.getDescripcionTercero());
							dtoFormaPagoDocSoporte.getListadoDtoEntidadesFinancieras().add(dtoEntidadFinanciera);
							totalValorFormaPago += totalValorParcialEntidad;
							
						}
					}
					
				}else //if(formaPago.getTiposDetalleFormaPago().getCodigo() == ConstantesBD.codigoTipoDetalleFormasPagoBono){
				{
					
					totalValorParcialEntidad = 0;
					
					DtoEntidadesFinancieras dtoEntidadesFinancieras = new DtoEntidadesFinancieras();
					dtoEntidadesFinancieras.setCodigo(ConstantesBD.acronimoEntidadNoValida);
					
					for (DtoDetalleDocSopor dtoDetalleDocSopor : listadoDefinitivoDocSop) {
						
						if(dtoDetalleDocSopor.getConsecutivoFormaPago() == formaPago.getConsecutivo()){
							
							dtoEntidadesFinancieras.getListadoDtoDetDocSoporte().add(dtoDetalleDocSopor);
							totalValorParcialEntidad += dtoDetalleDocSopor.getValorSistemaUnico();
						}
					}
					
					if(totalValorParcialEntidad!=0){
						
						dtoEntidadesFinancieras.setTotalValorSistema(totalValorParcialEntidad);
						dtoFormaPagoDocSoporte.getListadoDtoEntidadesFinancieras().add(dtoEntidadesFinancieras);
						
						totalValorFormaPago = totalValorParcialEntidad;
					}
				}
				
				
				totalValorFormaPago -= totalDevolucionFormaPago;
				
				if(totalValorFormaPago > 0){
					
					DtoCuadreCaja dtoCuadreCaja = new DtoCuadreCaja();
					dtoCuadreCaja.setTipoFormaPago(formaPago.getConsecutivo());
					dtoCuadreCaja.setTipoDetalleFormaPago(formaPago.getTiposDetalleFormaPago().getCodigo());
					dtoCuadreCaja.setFormaPago(formaPago.getDescripcion());
					dtoCuadreCaja.setValorSistema(totalValorFormaPago);

					dtoFormaPagoDocSoporte.setDtoCuadreCaja(dtoCuadreCaja);
					dtoInformacionEntrega.getCuadreCajaDTOs().add(dtoCuadreCaja);
					dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes().add(dtoFormaPagoDocSoporte);
					totalEntregadoTransportadora += totalValorFormaPago;
				}
			}
		}
		
		dtoInformacionEntrega.setTotalEntregadoTransportadora(totalEntregadoTransportadora);
		
		return dtoInformacionEntrega;
	}
	
	/**
	 * 
	 * M&eacute;todo que se encarga de complementar la informaci&oacute;n relacionada especificamente para las 
	 * formas de pago de tipo "NINGUNO" que puede ser asociada en un Arqueo Entrega Parcial
	 * 
	 * @param dtoInformacionEntrega
	 * @param totalesEntrTransCajaMayorPpalFormaPagoNinguno
	 * @param totalesDefinitivosFormaPagoNinguno
	 * @param valorBaseEnCaja 
	 * @param restriccion
	 * @return
	 */
	private DtoInformacionEntrega obtenerDtoInformacionEntregaFormaPagoNinguno (ArrayList<DtoDetalleDocSopor> totalesEntrTransCajaMayorPpalFormaPagoNinguno, 
			ArrayList<DtoDetalleDocSopor> totalesDefinitivosFormaPagoNinguno, List<DtoReciboDevolucion> totalesDevoluciones, int codigoInstitucion, double valorBaseEnCaja, boolean restriccion){
		
		IFormasPagoMundo formasPagoMundo = TesoreriaFabricaMundo.crearFormasPagoMundo();
		
		double totalEntregado;
		double totalRecaudado;
		double totalDevolucionFormaPago;
		boolean formaPagoEfectivo;
		
		DtoInformacionEntrega dtoInformacionEntrega =  new DtoInformacionEntrega();
			
		DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte;
		
		List<FormasPago> formasPagos = formasPagoMundo.obtenerFormasPagos();
		
		for (FormasPago formaPago : formasPagos) {
			
			if(restriccion || formaPago.getIndConsignacion().toString().equals(ConstantesBD.acronimoSi)){
				
				formaPagoEfectivo = false;
				
				totalDevolucionFormaPago = 0;
				
				for (DtoReciboDevolucion devolucion : totalesDevoluciones) {
					
					if(devolucion.getTipoFormaPago() == formaPago.getConsecutivo()){
						
						totalDevolucionFormaPago += devolucion.getValor();
					}
				}
				
				if(valorBaseEnCaja>0){
					
					String consecutivoFormaPago = ValoresPorDefecto.getFormaPagoEfectivo(codigoInstitucion);
					
					if(UtilidadTexto.isNumber(consecutivoFormaPago)){
					
						int consecutivo = Integer.parseInt(consecutivoFormaPago);
						
						if(formaPago.getConsecutivo() == consecutivo){
							
							formaPagoEfectivo = true;
						}
					}
				}
				
				dtoFormaPagoDocSoporte = new DtoFormaPagoDocSoporte();
				dtoFormaPagoDocSoporte.setConsecutivoFormaPago(formaPago.getConsecutivo());
				dtoFormaPagoDocSoporte.setFormaPago(formaPago.getDescripcion());
				dtoFormaPagoDocSoporte.setCodigoTipoDetalleFormaPago(formaPago.getTiposDetalleFormaPago().getCodigo());
				dtoFormaPagoDocSoporte.setIndicativoTrasladoCajaRecaudo(formaPago.getTrasladoCajaRecaudo().toString());
				dtoFormaPagoDocSoporte.setReqTrasladoCajaRecaudo(formaPago.getReqTrasladoCajaRecaudo());
				dtoFormaPagoDocSoporte.setRegistrarTraslado(formaPago.getReqTrasladoCajaRecaudo().toString());
				
				totalEntregado = 0;
				totalRecaudado = 0;
				
				DtoEntidadesFinancieras dtoEntidadesFinancieras = new DtoEntidadesFinancieras();
				dtoEntidadesFinancieras.setCodigo(ConstantesBD.acronimoEntidadNoValida);
				
				
				for (DtoDetalleDocSopor dtoDetalleDocSopor : totalesEntrTransCajaMayorPpalFormaPagoNinguno) {
					
					if(dtoDetalleDocSopor.getConsecutivoFormaPago() == formaPago.getConsecutivo()){

						totalEntregado += dtoDetalleDocSopor.getValorSistemaUnico();
					}
				}
				
				for (DtoDetalleDocSopor dtoDetalleDocSopor : totalesDefinitivosFormaPagoNinguno) {
									
					if(dtoDetalleDocSopor.getConsecutivoFormaPago() == formaPago.getConsecutivo()){

						totalRecaudado += dtoDetalleDocSopor.getValorSistemaUnico();
					}
				}
				
				totalRecaudado -= (totalDevolucionFormaPago + totalEntregado);
				
				if(totalRecaudado > 0 || (totalRecaudado == 0 && valorBaseEnCaja > 0 && formaPagoEfectivo)){
					
					DtoDetalleDocSopor dtoDetalleDocSopor = new DtoDetalleDocSopor();
					DtoCuadreCaja dtoCuadreCaja = new DtoCuadreCaja();
					
					if(formaPagoEfectivo){
						
						totalRecaudado += valorBaseEnCaja;
						dtoCuadreCaja.setValorBaseEnCaja(valorBaseEnCaja);
					}
					
					dtoDetalleDocSopor.setConsecutivoFormaPago(formaPago.getConsecutivo());
					dtoDetalleDocSopor.setFormaPago(formaPago.getDescripcion());
					dtoDetalleDocSopor.setTiposDetalleFormaPago(formaPago.getTiposDetalleFormaPago().getCodigo());
					dtoDetalleDocSopor.setValorSistema(totalRecaudado);
			
					dtoEntidadesFinancieras.getListadoDtoDetDocSoporte().add(dtoDetalleDocSopor);
					
					dtoEntidadesFinancieras.setTotalValorSistema(totalRecaudado);
					dtoFormaPagoDocSoporte.getListadoDtoEntidadesFinancieras().add(dtoEntidadesFinancieras);
					
					dtoCuadreCaja.setTipoFormaPago(formaPago.getConsecutivo());
					dtoCuadreCaja.setTipoDetalleFormaPago(formaPago.getTiposDetalleFormaPago().getCodigo());
					dtoCuadreCaja.setFormaPago(formaPago.getDescripcion());
					dtoCuadreCaja.setValorSistema(totalRecaudado);
					
					dtoFormaPagoDocSoporte.setDtoCuadreCaja(dtoCuadreCaja);
					dtoInformacionEntrega.getCuadreCajaDTOs().add(dtoCuadreCaja);
					dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes().add(dtoFormaPagoDocSoporte);
				}
			}
		}

		return dtoInformacionEntrega;
		
	}
	

	/**
	 * 
	 * Listado con los documentos de soporte disponibles para realizar un Arqueo Entrega Parcial
	 * (Entrega a Caja Mayor / Principal o a Transportadora de valores). Aplica solo en el caso de 
	 * las formas de pago de tipo diferente a ninguno.
	 * 
	 * @param listadoDetalleDocSoporte
	 * @param listadoDocSopEntregados
	 * @return
	 */
	private ArrayList<DtoDetalleDocSopor> obtenerDocSopDisponibles(ArrayList<DtoDetalleDocSopor> listadoDetalleDocSoporte, ArrayList<DtoDetalleDocSopor> listadoDocSopEntregados) {
		
		ArrayList<DtoDetalleDocSopor> listadoDocSopDisponibles;
	
		HashMap<Integer, String> hashCodigoDocSoporte = obtenerCodigosDocSoporte(listadoDocSopEntregados);
		
		if(listadoDocSopEntregados.size()!=0){
			
			listadoDocSopDisponibles = new ArrayList<DtoDetalleDocSopor>();
			
			for (DtoDetalleDocSopor detalleDocSoporte : listadoDetalleDocSoporte) {
				
				if(!hashCodigoDocSoporte.containsKey(detalleDocSoporte.getDetallePagosId()) && 
						detalleDocSoporte.getValorSistemaUnico() > 0){
					
					listadoDocSopDisponibles.add(detalleDocSoporte);
				}
			}
			
		}else{
			
			return listadoDetalleDocSoporte;
		}
		
		return listadoDocSopDisponibles;
	}

	
	/**
	 * Retorna un HashMap con los c&oacute;digos de los documentos de soporte relacionados en Arqueos
	 * parciales (Entregas a Caja Mayor o Entregas a Transportadora de valores)
	 * 
	 * @param listadoDocSopEntregados
	 * @return HashMap con los c&oacute;digos de los documentos de soporte relacionados en Arqueos
	 * parciales
	 */
	private HashMap<Integer, String> obtenerCodigosDocSoporte (ArrayList<DtoDetalleDocSopor> listadoDocSopEntregados){
	
		HashMap<Integer, String> hashCodigoDocSoporte = new HashMap<Integer, String>();
		
		for (DtoDetalleDocSopor dtoDetalleDocSopor : listadoDocSopEntregados) {
			
			if(!hashCodigoDocSoporte.containsKey(dtoDetalleDocSopor.getDetallePagosId())){ 
				
				hashCodigoDocSoporte.put(dtoDetalleDocSopor.getDetallePagosId(), dtoDetalleDocSopor.getNroDocumentoEntregado());
			}
		}
		return hashCodigoDocSoporte;
	}

	

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo#completarConsolidadoInformacionAceptacion(com.princetonsa.dto.tesoreria.DtoInformacionEntrega, com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento)
	 */
	public DtoInformacionEntrega completarConsolidadoInformacionAceptacion(DtoInformacionEntrega informacionEntregaDTO, ETipoMovimiento eTipoMovimiento)
	{
		double valorDeEntrega 		= 0;
		double valorDeAceptacion	= 0;
		double totalAceptacion		= 0;
		double totalRecibido		= 0;
		double valorDiferenciaEntidad = 0;

		informacionEntregaDTO.setExisteDiferencia(false);
		informacionEntregaDTO.getCuadreCajaDTOs().clear();
		
		for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte: informacionEntregaDTO.getListadoDtoFormaPagoDocSoportes()) 
		{
			if(dtoFormaPagoDocSoporte.getCodigoTipoDetalleFormaPago() == ConstantesBD.codigoTipoDetalleFormasPagoNinguno){
				
				valorDeEntrega 		= dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorSistema();
				valorDeAceptacion 	= dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorCaja();
	
				dtoFormaPagoDocSoporte.getDtoCuadreCaja().setValorDiferencia(valorDeAceptacion - valorDeEntrega);
				dtoFormaPagoDocSoporte.getDtoCuadreCaja().setTipodiferencia(obtenerTipoDiferencia(valorDeEntrega, valorDeAceptacion));			

			}else{
				
				totalAceptacion		= 0;
				valorDeAceptacion 	= 0;
				valorDeEntrega		= 0;
				valorDiferenciaEntidad = 0;
				
				for (DtoEntidadesFinancieras dtoEntidadesFinancieras : dtoFormaPagoDocSoporte.getListadoDtoEntidadesFinancieras()){
					
					totalRecibido = 0;//
					dtoEntidadesFinancieras.setTotalValorDiferencia(0); //limpiar las diferencias de cada una de las entidades
					
					for (DtoDetalleDocSopor dtoDetalleDocSopor : dtoEntidadesFinancieras.getListadoDtoDetDocSoporte()) 
					{
						/*
						 * Resetear valores si viene marcado como no entregado 
						 */
						if(dtoDetalleDocSopor.getIndicativoNoRecibido().equals(ConstantesBD.acronimoSi))
						{
							
							dtoDetalleDocSopor.setValorActual(new BigDecimal(0));
							dtoDetalleDocSopor.setNroDocumentoRecibido("");
							dtoDetalleDocSopor.setValorDiferencia(new BigDecimal(0));
							
						}

						if(dtoDetalleDocSopor.getValorActual() != null)
						{
							valorDeAceptacion 	=  dtoDetalleDocSopor.getValorActual().doubleValue();
							totalAceptacion		+= valorDeAceptacion;
						}
						else{
							
							valorDeAceptacion = 0;
						}
							
						valorDeEntrega = dtoDetalleDocSopor.getValorSistemaUnico();
						valorDiferenciaEntidad += valorDeAceptacion - valorDeEntrega;
						dtoDetalleDocSopor.setValorDiferencia(new BigDecimal(valorDeAceptacion - valorDeEntrega));
						dtoDetalleDocSopor.setTipodiferencia(obtenerTipoDiferencia(valorDeEntrega, valorDeAceptacion));
						
						totalRecibido += valorDeAceptacion;
					}
					
					dtoEntidadesFinancieras.setTotalValorRecibido(totalRecibido);
					dtoEntidadesFinancieras.setTotalValorDiferencia(valorDiferenciaEntidad);
					
					valorDiferenciaEntidad = 0;
				}
				
				dtoFormaPagoDocSoporte.getDtoCuadreCaja().setValorCaja(totalAceptacion);
				
				valorDeEntrega 		= dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorSistema();
				valorDeAceptacion 	= dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorCaja();
				
				dtoFormaPagoDocSoporte.getDtoCuadreCaja().setValorDiferencia(valorDeAceptacion - valorDeEntrega);
				dtoFormaPagoDocSoporte.getDtoCuadreCaja().setTipodiferencia(obtenerTipoDiferencia(valorDeEntrega, valorDeAceptacion));
				
			}
			
			if(!informacionEntregaDTO.isExisteDiferencia()){
				
				if(dtoFormaPagoDocSoporte.getDtoCuadreCaja().getTipodiferencia()!=null){
					
					informacionEntregaDTO.setExisteDiferencia(true);
				}
			}
			
			informacionEntregaDTO.getCuadreCajaDTOs().add(dtoFormaPagoDocSoporte.getDtoCuadreCaja());
		}
		
		return informacionEntregaDTO;
	}
	
	
	/**
	 * M&eacute;todo utilizado para calcular los valores de diferencia, valores recaudados en caja y registro correcto
	 * de informaci&oacute;n, necesario para continuar con las operaciones de un movimiento espec&iacute;fico.
	 * 
	 * @param dtoInformacionEntrega
	 * @param eTipoMovimiento
	 * @return
	 */
	@Override
	public DtoInformacionEntrega calcularValoresParaArqueos(DtoInformacionEntrega dtoInformacionEntrega, ETipoMovimiento eTipoMovimiento)
	{
		double valorSistema 		= 0;
		double valorRecaudadoCaja	= 0;
		double totalRecaudadoCaja		= 0;
		double totalRecaudadoEntidad		= 0;
		double valorDiferenciaEntidad = 0;

		dtoInformacionEntrega.setExisteDiferencia(false);
		dtoInformacionEntrega.getCuadreCajaDTOs().clear();
		
		boolean registrarEnCierreTurno = true;

		for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte: dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes()) 
		{
			if(dtoFormaPagoDocSoporte.getCodigoTipoDetalleFormaPago() == ConstantesBD.codigoTipoDetalleFormasPagoNinguno){
				
				valorSistema 		= dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorSistema();
				valorRecaudadoCaja 	= dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorCaja();
				
				double valorBaseEnCaja = 0;
				
				/*
				 * Solo se tiene en cuenta la base en caja cuando se va a realizar un movimiento de caja
				 * de tipo Cierre Turno de Caja.
				 */
				if(eTipoMovimiento == ETipoMovimiento.CIERRE_CAJA){
					
					MovimientosCaja movimientosCaja = dtoInformacionEntrega.getMovimientosCaja();

					if(movimientosCaja!=null && movimientosCaja.getTurnoDeCaja()!=null){
						
						int codigoInstitucion = movimientosCaja.getTurnoDeCaja().getCajas().getInstituciones().getCodigo();
						
						BigDecimal valor = movimientosCaja.getTurnoDeCaja().getValorBase();
						
						if(valor!=null){

							if(esFormaPagoEfectivo(codigoInstitucion, dtoFormaPagoDocSoporte.getConsecutivoFormaPago(), valor.doubleValue())){
								
								valorBaseEnCaja = valor.doubleValue();
							}
						}
					}
					
					dtoFormaPagoDocSoporte.getDtoCuadreCaja().setValorDiferencia(valorRecaudadoCaja - valorSistema);
					dtoFormaPagoDocSoporte.getDtoCuadreCaja().setTipodiferencia(obtenerTipoDiferencia(valorSistema, valorRecaudadoCaja));
					dtoFormaPagoDocSoporte.getDtoCuadreCaja().setValorBaseEnCaja(valorBaseEnCaja);
					dtoFormaPagoDocSoporte.setRegistrarEnCierreTurno(true);
				
				}else{
					
					dtoFormaPagoDocSoporte.getDtoCuadreCaja().setValorDiferencia(new Double(ConstantesBD.codigoNuncaValidoDouble));
					dtoFormaPagoDocSoporte.getDtoCuadreCaja().setTipodiferencia(null);
				}

			}else{
				
				totalRecaudadoCaja		= 0;
				valorRecaudadoCaja 		= 0;
				valorSistema			= 0;
				valorDiferenciaEntidad 	= 0;
				
				for (DtoEntidadesFinancieras dtoEntidadesFinancieras : dtoFormaPagoDocSoporte.getListadoDtoEntidadesFinancieras()){
					
					totalRecaudadoEntidad = 0;
					dtoEntidadesFinancieras.setTotalValorDiferencia(0); //limpiar las diferencias de cada una de las entidades
					
					for (DtoDetalleDocSopor dtoDetalleDocSopor : dtoEntidadesFinancieras.getListadoDtoDetDocSoporte()) 
					{
						/*
						 * Resetear valores si viene marcado como no entregado 
						 */
						if(dtoDetalleDocSopor.getIndicativoNoRecibido().equals(ConstantesBD.acronimoSi))
						{
							
							dtoDetalleDocSopor.setValorActual(new BigDecimal(0));
							dtoDetalleDocSopor.setNroDocumentoRecibido("");
							dtoDetalleDocSopor.setValorDiferencia(new BigDecimal(0));
							
						}else if(eTipoMovimiento == ETipoMovimiento.CIERRE_CAJA){
							
							/*
							 * Esto se hace con el fin de validar que se ingrese adecuadamente toda la información para poder registrar 
							 * la forma de pago en el cierre de caja
							 */
							
							if(dtoDetalleDocSopor.getIndicativoNoRecibido().equals(ConstantesBD.acronimoNo) && registrarEnCierreTurno){
							
								if(dtoDetalleDocSopor.getValorActual() == null || "".equals(dtoDetalleDocSopor.getNroDocumentoRecibido()))
								{
									registrarEnCierreTurno = false;
								}
							}
						}

						if(dtoDetalleDocSopor.getValorActual() != null)
						{
							valorRecaudadoCaja 	=  dtoDetalleDocSopor.getValorActual().doubleValue();
							totalRecaudadoCaja  += valorRecaudadoCaja;
						
						}else{
							
							valorRecaudadoCaja = 0;
						}
							
						valorSistema = dtoDetalleDocSopor.getValorSistemaUnico();
						valorDiferenciaEntidad += valorRecaudadoCaja - valorSistema;
						dtoDetalleDocSopor.setValorDiferencia(new BigDecimal(valorRecaudadoCaja - valorSistema));
						dtoDetalleDocSopor.setTipodiferencia(obtenerTipoDiferencia(valorSistema, valorRecaudadoCaja));
						
						totalRecaudadoEntidad += valorRecaudadoCaja;
					}
					
					dtoEntidadesFinancieras.setTotalValorRecibido(totalRecaudadoEntidad);
					dtoEntidadesFinancieras.setTotalValorDiferencia(valorDiferenciaEntidad);
					
					valorDiferenciaEntidad = 0;
				}
				
				dtoFormaPagoDocSoporte.getDtoCuadreCaja().setValorCaja(totalRecaudadoCaja);
				
				valorSistema 		= dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorSistema();
				valorRecaudadoCaja 	= dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorCaja();
				
				dtoFormaPagoDocSoporte.getDtoCuadreCaja().setValorDiferencia(valorRecaudadoCaja - valorSistema);
				dtoFormaPagoDocSoporte.getDtoCuadreCaja().setTipodiferencia(obtenerTipoDiferencia(valorSistema, valorRecaudadoCaja));
				
				/*
				 * Esto se hace con el fin de validar que se ingrese adecuadamente toda la información para poder registrar 
				 * la forma de pago en el cierre de caja
				 */
				dtoFormaPagoDocSoporte.setRegistrarEnCierreTurno(registrarEnCierreTurno);
		
			}
			
			if(!dtoInformacionEntrega.isExisteDiferencia()){
				
				if(dtoFormaPagoDocSoporte.getDtoCuadreCaja().getTipodiferencia()!=null){
					
					dtoInformacionEntrega.setExisteDiferencia(true);
				}
			}
			
			dtoInformacionEntrega.getCuadreCajaDTOs().add(dtoFormaPagoDocSoporte.getDtoCuadreCaja());
		}
		
		return dtoInformacionEntrega;
	}
	
	
	
	/**
	 * Determina cual es el tipo de diferencia entre el valor entregados/cargados desde el sistama 
	 * y el valor recibido que se encuentra actualmente en la caja. Retorna nulo si no existe diferencia alguna
	 * 
	 * @param valorEntregaSistema
	 * @param valorActualCaja
	 * @return
	 */
	@Override
	public String obtenerTipoDiferencia(double valorEntregaSistema, double valorActualCaja)
	{
		double resultadoDiferencia = valorActualCaja - valorEntregaSistema;
		
		if (resultadoDiferencia < 0)
		{
			return ConstantesIntegridadDominio.acronimoDiferenciaFaltante;
		
		}else if (resultadoDiferencia > 0)
		{
			return ConstantesIntegridadDominio.acronimoDiferenciaSobrante;
		}
		
		return null;
	}
	
	/**
	 * Retorna un valor boolean que determina si el movimiento fue registrado satisfactoriamente
	 * @param listaDtoConsolidadoMovimiento
	 */
	@Override
	public boolean guardarMovimientoCaja(ArrayList<DtoInformacionEntrega>  listaDtoInformacionEntrega) 
	{
		IMovimientosCajaMundo movimientosCajaMundo 	= TesoreriaFabricaMundo.crearMovimientosCajaMundo(); 
		
		MovimientosCaja movimientosCaja = new MovimientosCaja();
		boolean problemas = false;
		ArrayList<BigDecimal> listaConsecutivosDisponiblesIniciados = new ArrayList<BigDecimal>();
		
		for (DtoInformacionEntrega dtoInformacionEntrega : listaDtoInformacionEntrega) 
		{
			movimientosCaja 	= new MovimientosCaja();
			movimientosCaja 	= HelperConstruirEntidad.construirEntidad(dtoInformacionEntrega);
			Log4JManager.info(">>>>>>>>>>>>>>>>>>>>>> Cantidad de documentos de soporte: "+movimientosCaja.getDocSopMovimCajases().size());
			
			for (Iterator iteratorDocSop =  movimientosCaja.getDocSopMovimCajases().iterator(); iteratorDocSop.hasNext();) 
			{
				DocSopMovimCajas docSop = (DocSopMovimCajas) iteratorDocSop.next();
				for (Iterator iteratorDetFal =  docSop.getDetFaltanteSobrantes().iterator(); iteratorDetFal.hasNext();) 
				{
					DetFaltanteSobrante dedFal = (DetFaltanteSobrante) iteratorDetFal.next();
					listaConsecutivosDisponiblesIniciados.add(dedFal.getConsecutivo());
				}
			}
			
			//--
			/*
			int institucion = dtoInformacionEntrega.getInstitucionActual();
			String nombreConsecutivo = ConstantesBD.nombreConsecutivoXXXXXXX;
			BigDecimal consecutivo = new BigDecimal(UtilidadBD.obtenerValorConsecutivoDisponible(nombreConsecutivo, institucion));
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(nombreConsecutivo,institucion, consecutivo.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			movimientosCaja.setConsecutivo(consecutivo.longValue());
			*/
			//---

			if(!movimientosCajaDAO.guardarMovimientoCaja(movimientosCaja))
			{
				Log4JManager.info("Error intentando guardar movimiento de caja: "+dtoInformacionEntrega.getETipoMovimiento().name());
				problemas	= true;
				break; // Salir del for
			}
			else{
				// --> Cambio de estado a la solicitud
				SolicitudTrasladoCaja solicitudTrasladoCaja = movimientosCajaMundo.obtenerSolicitudTraslado(dtoInformacionEntrega.getIdMovimientoCaja());
				solicitudTrasladoCaja.setEstado(ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaAceptado);
				
				try {
					new SolicitudTrasladoCajaHome().attachDirty(solicitudTrasladoCaja);
				} catch (Exception e) {
					problemas	= true;
					Log4JManager.info("Error intentando actualizar el estado de la solicitud #"+solicitudTrasladoCaja.getMovimientoCaja());
					break;
				}
			}
		}
		
		if(problemas == true)
		{
			for (BigDecimal consecutivo : listaConsecutivosDisponiblesIniciados) 
			{
				int institucion = listaDtoInformacionEntrega.get(0).getInstitucionActual();
				Connection con=UtilidadBD.abrirConexion();
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoTrasladoAbonosPaciente, institucion, consecutivo.toString(), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
				UtilidadBD.closeConnection(con);
			}
			return false;
		}
		else 
		{
			return true;
		}
	}
	
	@Override
	public List<DtoInformacionEntrega> consultaEntregaTransportadora(DtoConsultaEntregaTransportadora dtoConsultaEntregaTransportadora) {
		
		IConsultaArqueoCierreCajaMundo consultaArqueoCierreCajaMundo = TesoreriaFabricaMundo.crearConsultaArqueoCierreCajaMundo();
		
		/*
		 * Carga los movimientos dependiendo de los par&aacute;metros de b&uacute;squeda definidos por el DtoConsultaEntregaTransportadora
		 */
		List<MovimientosCaja> listadoMovimientosCaja = movimientosCajaDAO.consultaEntregaTransportadora(dtoConsultaEntregaTransportadora);
		
		/*
		 * Mundo de DocSoporteMovimientos Cajas
		 */
		//IDocSopMovimCajasMundo docSopMovimCajasMundo = TesoreriaMundoFabrica.crearDocSopMovimCajasMundo();
		
		/*
		 * Lista que retorna el DtoInformacionEntrega
		 */
		List<DtoInformacionEntrega> listaDtoInformacionEntrega=new  ArrayList<DtoInformacionEntrega>();
		
		/*
		 * Validar si existe movimientos
		 */
		if(listadoMovimientosCaja.size()>0){
			
			
			/*
			 *Iterar la lista de movimientos 
			 */
			for(MovimientosCaja movimientosCaja:listadoMovimientosCaja){
				/*
				 * Mundo de cajas 
				 */
							
				int codigoInstitucion = movimientosCaja.getTurnoDeCaja().getUsuarios().getInstituciones().getCodigo();
				
				/*
				 * DtoInformacionEntrega
				 */
				DtoInformacionEntrega dtoInformacionEntrega = new DtoInformacionEntrega();
				
				/*
				 * Instancia de movimientos entrega
				 */
				//MovimientosCaja movimientosCajaEntrega = new MovimientosCaja();
			
				/*
				 * Tipos de movimiento entrega a transportadora 
				 */
//				ArrayList<Integer> codigosTipoMovimiento = new ArrayList<Integer>();
//				codigosTipoMovimiento.add(ConstantesBD.codigoTipoMovimientoEntregaTransportadora);
				
				/*
				 * Asignacion de movimientos 
				 */
				//movimientosCajaEntrega = movimientosCaja.getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk();
				
				// esto se cambio por modificaciones a la funcionalidad. en proceso de verificación.
				/*
				 * 1.Cargar listado de entregas
				 */
				//ArrayList<DtoDetalleDocSopor> listadoEntregasTransportadora = (ArrayList<DtoDetalleDocSopor>) docSopMovimCajasMundo.obtenerEntrTransYCajaMayorPpalNoFormaPagoNinguna(movimientosCajaEntrega);
				
				/*
				 * 2.Obtener DtoEntrega 
				 */
				dtoInformacionEntrega = consultaArqueoCierreCajaMundo.consultarArqueoParcialPorEntrega(movimientosCaja.getEntregaTransportadoraByCodigoPk().getMovimientoCaja(), ETipoMovimiento.ENTREGA_TRANSPORTADORA_VALORES, codigoInstitucion);
				dtoInformacionEntrega.setIdMovimientoCaja(movimientosCaja.getCodigoPk());
				dtoInformacionEntrega.setConsecutivoMovimientoCaja(movimientosCaja.getConsecutivo());
				//dtoInformacionEntrega =  obtenerDtoInformacionEntrega(listadoEntregasTransportadora, codigoInstitucion, true);
				
				/*
				 * 3. Adaptar movimientos caja al Dto informacion entrega
				 */
				//dtoInformacionEntrega.setMovimientosCaja(movimientosCaja);
						
				/*
				 * 4. Adaptar las transportadoras
				 */
				//dtoInformacionEntrega.setTransportadoraValores(movimientosCaja.getEntregaTransportadoraByCodigoPk().getTransportadoraValores());
				
				/*
				 * 5. Adaptar DtoInformacionEntrega
				 */
				listaDtoInformacionEntrega.add(dtoInformacionEntrega) ;
				
			}//Fin for de iteracion de lista de movimientos

		}//Fin si
		
		return listaDtoInformacionEntrega;
	}
	
	
	/**
	 * Retorna un DTO con la informaci&oacute;n de una Solicitud de Traslado a Caja de Recaudo
	 * realizada y que se encuentra asociada a un movimiento de Cierre de Turno de caja.
	 *  
	 * @param codigoInstitucion
	 * @param codigoSolicitud
	 * @return DtoInformacionEntrega
	 * 
	 */
	@Override
	public DtoInformacionEntrega consolidarInformacionSolicitudTrasCajaRealizada (int codigoInstitucion, long codigoSolicitud){ //consolidarInformacionSolicitudTrasCajaRealizada
		
		ISolicitudTrasladoCajaMundo solicitudTrasladoCajaMundo = TesoreriaFabricaMundo.crearSolicitudTrasladoCajaMundo();
		IConsultaArqueoCierreCajaMundo consultaArqueoCierreCajaMundo =  TesoreriaFabricaMundo.crearConsultaArqueoCierreCajaMundo();

		ArrayList<DtoDetalleDocSopor> documentosSoporte = (ArrayList<DtoDetalleDocSopor>) solicitudTrasladoCajaMundo.obtenerDocSopSolicitudTrasladoCaja(codigoSolicitud);

		DtoInformacionEntrega dtoInformacionEntrega = consultaArqueoCierreCajaMundo.obtenerDtoInformacionEntregaImpresion(documentosSoporte, codigoInstitucion);

		return dtoInformacionEntrega;
		
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener la fecha m&aacute;xima registrada
	 * para un movimiento de caja. Teniendo en cuenta que los movimientos de caja son:
	 * 
	 * Recibos de caja
	 * Anulaciones de recibos de caja
	 * Devoluciones de Recibos de caja
	 * Egresos
	 * Solicitudes de Traslados de caja realizadas y aceptadas.
	 * Entregas a transportadora de valores o cajas mayor/principal.
	 * 
	 * @param turnoDeCaja
	 * @return
	 */
	public long obtenerUltimaFechaMovimientoCaja (TurnoDeCaja turnoDeCaja){
		
		IRecibosCajaMundo recibosCajaMundo = TesoreriaFabricaMundo.crearRecibosCajaMundo();
		IDevolRecibosCajaMundo devolRecibosCajaMundo = TesoreriaFabricaMundo.crearDevolRecibosCajaMundo();
		IAnulacionRecibosCajaMundo anulacionRecibosCajaMundo = TesoreriaFabricaMundo.crearAnulacionRecibosCajaMundo();
		ISolicitudTrasladoCajaMundo solicitudTrasladoCajaMundo = TesoreriaFabricaMundo.crearSolicitudTrasladoCajaMundo();
		IEntregaTransportadoraValoresMundo entregaTransportadoraValoresMundo = TesoreriaFabricaMundo.crearEntregaTransportadoraValoresMundo();
		IEntregaCajaMayorMundo entregaCajaMayorMundo = TesoreriaFabricaMundo.crearEntregaCajaMayorMundo();
		
		Date fechaSistema = UtilidadFecha.getFechaActualTipoBD();
		
		ArrayList<Date> fechasMovimiento = new ArrayList<Date>();
		
		Date fechaMayor = null;

		fechasMovimiento.add(recibosCajaMundo.obtenerFechaUltimoMovimientoRecibo(turnoDeCaja) );
		fechasMovimiento.add(devolRecibosCajaMundo.obtenerFechaUltimoMovimientoDevoluciones(turnoDeCaja));
		fechasMovimiento.add(anulacionRecibosCajaMundo.obtenerFechaUltimoMovimientoAnulaciones(turnoDeCaja));
		fechasMovimiento.add(solicitudTrasladoCajaMundo.obtenerFechaUltimoMovimientoSolicitudAcept(turnoDeCaja));
		fechasMovimiento.add(entregaTransportadoraValoresMundo.obtenerFechaUltimoMovimientoEntTransValores(turnoDeCaja));
		fechasMovimiento.add(entregaCajaMayorMundo.obtenerFechaUltimoMovimientoEntregaCaja(turnoDeCaja));
	
		for (Date fechaMovimiento : fechasMovimiento) {
			
			if(fechaMovimiento!=null){
				
				if(fechaMayor==null){
					
					fechaMayor = new Date(fechaMovimiento.getTime());
				
				}else {
					
					if(fechaMovimiento.getTime() > fechaMayor.getTime()){
						
						fechaMayor = fechaMovimiento;
					}
					
					if(fechaMayor.getTime() >= fechaSistema.getTime()){
						
						return fechaSistema.getTime();
					}
				}
			}
		}
		
		if(fechaMayor!=null){
			
			return fechaMayor.getTime();
			
		}else{
			
			return ConstantesBD.codigoNuncaValidoLong;
		}
	}

	@Override
	public DtoParametros validarDefinicionesParametrosAperturaTurno(UsuarioBasico usuario) 
	{
		DtoParametros dtoParametros = new DtoParametros();
	
		Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
		BigDecimal consecutivo = new BigDecimal(UtilidadBD.obtenerValorActualTablaConsecutivos(conH, ConstantesBD.nombreConsecutivoRegistroFaltanteSobrante, usuario.getCodigoInstitucionInt()));
		
		
		if( (consecutivo.equals(new BigDecimal(ConstantesBD.codigoNuncaValido)))
				|| (consecutivo == new BigDecimal(ConstantesBD.codigoNuncaValido)) )
		{
			dtoParametros.setTieneDefinidoConsecutivoFaltanteSobrante(false);
		}
		else
		{
			dtoParametros.setTieneDefinidoConsecutivoFaltanteSobrante(true);
		}
		
		return dtoParametros;
		
	}
	
	
	/**
	 * M&eacute;todo que determina si la forma de pago est&aacute; parametrizada
	 * como forma de pago Efectivo
	 * 
	 * @param codigoInstitucion
	 * @param consecutivoForma
	 * @param valorBase
	 * @return
	 */
	private boolean esFormaPagoEfectivo (int codigoInstitucion, int consecutivoForma,  double valorBase){
		
		boolean resultado = false;
		
		if(valorBase > 0){
			
			String consecutivoFormaPago = ValoresPorDefecto.getFormaPagoEfectivo(codigoInstitucion);
			
			if(UtilidadTexto.isNumber(consecutivoFormaPago)){
			
				int consecutivo = Integer.parseInt(consecutivoFormaPago);
				
				if(consecutivoForma == consecutivo){
					
					resultado = true;
				}
			}
		}
		
		return resultado;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo#consultarCierreArqueo(com.princetonsa.dto.tesoreria.DtoBusquedaCierreArqueo)
	 */
	@Override
	public List<MovimientosCaja> consultarCierreArqueo(	DtoBusquedaCierreArqueo dtoBusquedaCierreArqueo) {
	
		
		return movimientosCajaDAO.consultarCierreArqueo(dtoBusquedaCierreArqueo);
	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo#consultarEntregaAsociadaArqueoParcial(long)
	 */
	@Override
	public MovimientosCaja consultarEntregaAsociadaArqueoParcial(long codigoMovimientoArqueo) {
		
		return movimientosCajaDAO.consultarEntregaAsociadaArqueoParcial(codigoMovimientoArqueo);
	}
	
	
	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo#consultarconsolidadoCierre(java.lang.String, java.lang.String, java.lang.String, java.util.List)
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultarconsolidadoCierre(String fecha,String centroAtencion,String empresaInstitucion,List<FormasPago> formasPago ){
		Date fechaIngresada=null;
		try {
			if (fecha!=null && !fecha.equals("")) {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				fechaIngresada = df.parse(fecha);
			}

		} catch (ParseException e) {
			Log4JManager.warning("No se pudo abrir la conexión"+e.toString());
		}
		return movimientosCajaDAO.consultarconsolidadoCierre(fechaIngresada,centroAtencion,empresaInstitucion,formasPago);
	}
	
	
	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo#consultarconsolidadoCierreCajaCajero(java.lang.String, java.lang.String, java.lang.String, java.util.List)
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultarconsolidadoCierreCajaCajero(String fecha,String centroAtencion,String empresaInstitucion,List<FormasPago> formasPago){
		Date fechaIngresada=null;
		try {
			if (fecha!=null && !fecha.equals("")) {
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				fechaIngresada = df.parse(fecha);
			}

		} catch (ParseException e) {
			Log4JManager.warning("No se pudo abrir la conexión"+e.toString());
		}
		return movimientosCajaDAO.consultarconsolidadoCierreCajaCajero(fechaIngresada,centroAtencion,empresaInstitucion,formasPago);
	}
	
	@Override
	public ArrayList<DtoConsolidadoCierreReporte> consultaTrasladoCajaMayorPrincipal(DtoConsolidadoCierreReporte parametros) 
	{
		return movimientosCajaDAO.consultaTrasladoCajaMayorPrincipal(parametros);
	}
	
	@Override
	public ArrayList<DtoConsolidadoCierreReporte> consultaTrasladoHaciaCajaMayor(DtoConsolidadoCierreReporte parametros) 
	{
		return movimientosCajaDAO.consultaTrasladoHaciaCajaMayor(parametros);
	}

	@Override
	public boolean persistTrasladoCajaPrincipalMayor(TrasladoCajaPrincipalMayor transientInstance) {
		return movimientosCajaDAO.persistTrasladoCajaPrincipalMayor(transientInstance);
	}
	
	

//	/**
//	 * Retorna un DTO con la informaci&oacute;n relacionada a una Entrega a Transportadora de valores o a una Caja Mayor Principal
//	 * que ya fue realizada. Se pasa como par&aacute;metro el codigo de la entrega realizada junto con el tipo de movimiento de 
//	 * caja asociado.
//	 *
//	 * @param codigoEntrega
//	 * @param eTipoMovimiento
//	 * @param codigoInstitucion
//	 * @return
//	 */
//	@Override
//	public DtoInformacionEntrega obtenerInformacionEntregaPorMovimiento (long codigoEntrega, ETipoMovimiento eTipoMovimiento, int codigoInstitucion){
//		
//		IDocSopMovimCajasMundo docSopMovimCajasMundo = TesoreriaMundoFabrica.crearDocSopMovimCajasMundo();
//		
//		//int codigoInstitucion = movimiento.getTurnoDeCaja().getUsuarios().getInstituciones().getCodigo();
//		
//		MovimientosCaja movimientoArqueo = new MovimientosCaja();
//		
//		DtoInformacionEntrega dtoInformacionEntrega =  new DtoInformacionEntrega();
//		
//		if(eTipoMovimiento == ETipoMovimiento.ENTREGA_CAJA_MAYOR_PRINCIPAL){
//			
//			IEntregaCajaMayorMundo entregaCajaMayorMundo = TesoreriaMundoFabrica.crearEntregaCajaMayorMundo();
//			
//			//movimiento = movimientosCaja.getEntregaCajaMayorByCodigoPk().getMovimientosCajaByCodigoPk();
//
//			//Log4JManager.info("---------------------------- " + movimiento.getCodigoPk());
//			
//			EntregaCajaMayor entregaCajaMayor = entregaCajaMayorMundo.obtenerEntregaCajaMayorPorCodigo(codigoEntrega);
//			
//			if(entregaCajaMayor!=null){
//			
//				ArrayList<DtoDetalleDocSopor> valoresEntregaRealizada = (ArrayList<DtoDetalleDocSopor>) docSopMovimCajasMundo.obtenerEntrTransYCajaMayorPpalNoFormaPagoNinguna(entregaCajaMayor.getMovimientosCajaByCodigoPk());
//				
//				valoresEntregaRealizada.addAll(docSopMovimCajasMundo.obtenerTotalesEntregasFormaPagoNinguno(entregaCajaMayor.getMovimientosCajaByCodigoPk()));
//			
//				dtoInformacionEntrega = obtenerDtoInformacionEntregaImpresion(valoresEntregaRealizada, codigoInstitucion);
//				dtoInformacionEntrega.setObservaciones(entregaCajaMayor.getMovimientosCajaByCodigoPk().getObservaciones());
//				dtoInformacionEntrega.setCajaMayorPrincipal(entregaCajaMayor.getCajas());
//				
//				movimientoArqueo = entregaCajaMayor.getMovimientosCajaByMovimientoCajaArqueo();
//				movimientoArqueo.setEntregaCajaMayorByCodigoPk(entregaCajaMayor);
//				
//				
//			}
//	
//		}else if(eTipoMovimiento == ETipoMovimiento.ENTREGA_TRANSPORTADORA_VALORES){
//			
//			IEntregaTransportadoraValoresMundo entregaTransportadoraValoresMundo = TesoreriaMundoFabrica.crearEntregaTransportadoraValoresMundo();
//			
//			//movimiento = movimientosCaja.getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk();
//			
//			EntregaTransportadora entregaTransportadora = entregaTransportadoraValoresMundo.obtenerEntregaTransportadoraPorCodigo(codigoEntrega);
//			
//			if(entregaTransportadora!=null){
//				
//				ArrayList<DtoDetalleDocSopor> valoresEntregaRealizada = (ArrayList<DtoDetalleDocSopor>) docSopMovimCajasMundo.obtenerEntrTransYCajaMayorPpalNoFormaPagoNinguna(entregaTransportadora.getMovimientosCajaByCodigoPk());
//				valoresEntregaRealizada.addAll(docSopMovimCajasMundo.obtenerTotalesEntregasFormaPagoNinguno(entregaTransportadora.getMovimientosCajaByCodigoPk()));
//				
//				dtoInformacionEntrega = obtenerDtoInformacionEntregaImpresion(valoresEntregaRealizada, codigoInstitucion);
//				
//				dtoInformacionEntrega.setTransportadoraValores(entregaTransportadora.getTransportadoraValores());
//				
//				movimientoArqueo = entregaTransportadora.getMovimientosCajaByMovimientoCajaArqueo();
//				movimientoArqueo.setEntregaTransportadoraByCodigoPk(entregaTransportadora);
//				
//			}
//		}
//		
//		if(dtoInformacionEntrega!=null){
//			
//			dtoInformacionEntrega.setETipoMovimiento(eTipoMovimiento);
//			dtoInformacionEntrega.setMovimientosCaja(movimientoArqueo);
//		}
//		
//		return dtoInformacionEntrega;
//
////		DtoInformacionEntrega dtoInformacionEntrega = new DtoInformacionEntrega();
////		
////		MovimientosCaja movimientosCajaEntrega = new MovimientosCaja();
////		ArrayList<Integer> codigosTipoMovimiento = new ArrayList<Integer>();
////		
////		if(eTipoMovimiento == ETipoMovimiento.ENTREGA_CAJA_MAYOR_PRINCIPAL){
////			
////			codigosTipoMovimiento.add(ConstantesBD.codigoTipoMovimientoEntregaCajaPrincipalMayor);
////			movimientosCajaEntrega = movimientosCaja.getEntregaCajaMayorByCodigoPk().getMovimientosCajaByCodigoPk();
////			
////		}else if(eTipoMovimiento == ETipoMovimiento.ENTREGA_TRANSPORTADORA_VALORES){
////			
////			codigosTipoMovimiento.add(ConstantesBD.codigoTipoMovimientoEntregaTransportadora);
////			movimientosCajaEntrega = movimientosCaja.getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk();
////		}
////		/ esto se cambio por modificaciones a la funcionalidad. en proceso de verificación.
////		ArrayList<DtoDetalleDocSopor> listadoEntregasTransCajaMayorPrincipal = (ArrayList<DtoDetalleDocSopor>) docSopMovimCajasMundo.obtenerEntrTransYCajaMayorPpalNoFormaPagoNinguna(movimientosCajaEntrega);
////		
////		dtoInformacionEntrega =  obtenerDtoInformacionEntrega(listadoEntregasTransCajaMayorPrincipal, codigoInstitucion, true);
////		dtoInformacionEntrega.setMovimientosCaja(movimientosCaja);
////
////		 esto se cambio para probar
////		if(listadoEntregasTransCajaMayorPrincipal.size()>0){
////			
////			dtoInformacionEntrega = agregarTotalEfectivoEntregado(dtoInformacionEntrega, codigosTipoMovimiento, movimientosCajaEntrega.getCodigoPk());
////		}
////	
////		//Esto se hace para completar la informacíon que se va a mostrar en presentaci&oacute;n
////		if(eTipoMovimiento == ETipoMovimiento.ENTREGA_CAJA_MAYOR_PRINCIPAL){
////			
////			dtoInformacionEntrega.setObservaciones(movimientosCaja.getEntregaCajaMayorByCodigoPk().getMovimientosCajaByCodigoPk().getObservaciones());
////			dtoInformacionEntrega.setCajaMayorPrincipal(movimientosCaja.getEntregaCajaMayorByCodigoPk().getCajas());
////			
////		}else if(eTipoMovimiento == ETipoMovimiento.ENTREGA_TRANSPORTADORA_VALORES){
////			
////			dtoInformacionEntrega.setTransportadoraValores(movimientosCaja.getEntregaTransportadoraByCodigoPk().getTransportadoraValores());
////		}
////
////		dtoInformacionEntrega.setETipoMovimiento(eTipoMovimiento);
////		
////		return dtoInformacionEntrega;
//	}
//	
//	
//	/**
//	 * Retorna un {@link DtoInformacionEntrega} con la informaci&oacute;n necesaria para realizar los
//	 * Arqueos Parciales (Entregas a Caja Mayor / Principal ó Entregas a Transportadora) y 
//	 * Aceptaciones de Solicitudes de Traslado a Caja. Solo se tienen en cuenta las formas de pago
//	 * de tipo diferente a "NINGUNO"
//	 *
//	 * @param listadoDefinitivoDocSop
//	 * @param codigoInstitucion
//	 * @param totalesDevoluciones 
//	 * @param restriccion (este atributo es utilizado para poder recorrer el listado disponible de 
//	 * documentos sin tener en cuenta la parametrizaci&oacute;n Consignaci&oacute;n Banco)
//	 * @return
//	 */
//	@Override
//	public DtoInformacionEntrega obtenerDtoInformacionEntregaImpresion (ArrayList<DtoDetalleDocSopor> listadoDefinitivoDocSop, int codigoInstitucion) {
//		
//		IFormasPagoMundo formasPagoMundo = TesoreriaMundoFabrica.crearFormasPagoMundo();
//		
//		IEntidadesFinancierasMundo entidadesFinancierasMundo = TesoreriaMundoFabrica.crearEntidadesFinancierasMundo();
//		
//		List<FormasPago> formasPagos = formasPagoMundo.obtenerFormasPagos();
//		ArrayList<DtoEntidadesFinancieras> listadoCompletoEntidades = (ArrayList<DtoEntidadesFinancieras>) entidadesFinancierasMundo.obtenerEntidadesPorInstitucion(codigoInstitucion, false);
//		
//		DtoInformacionEntrega dtoInformacionEntrega =  new DtoInformacionEntrega();
//		
//		double totalValorFormaPago;
//		double totalValorParcialEntidad;
//		double totalEntregadoTransportadora = 0;
//		
//		DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte;
//		
//		for (FormasPago formaPago : formasPagos) {
//			
//			Log4JManager.info("recorriendo formas de pago  ****************************************");
//			
//			
//			dtoFormaPagoDocSoporte = new DtoFormaPagoDocSoporte();
//			dtoFormaPagoDocSoporte.setConsecutivoFormaPago(formaPago.getConsecutivo());
//			dtoFormaPagoDocSoporte.setFormaPago(formaPago.getDescripcion());
//			dtoFormaPagoDocSoporte.setCodigoTipoDetalleFormaPago(formaPago.getTiposDetalleFormaPago().getCodigo());
//			dtoFormaPagoDocSoporte.setIndicativoTrasladoCajaRecaudo(formaPago.getTrasladoCajaRecaudo().toString());
//			
//			totalValorFormaPago = 0;
//			
//			if(formaPago.getTiposDetalleFormaPago().getCodigo() == ConstantesBD.codigoTipoDetalleFormasPagoCheque 
//				|| formaPago.getTiposDetalleFormaPago().getCodigo() == ConstantesBD.codigoTipoDetalleFormasPagoTarjeta ){
//				
//				for (DtoEntidadesFinancieras dtoEntidadesFinancieras : listadoCompletoEntidades) {
//					
//					totalValorParcialEntidad = 0;
//					
//					DtoEntidadesFinancieras dtoEntidadFinanciera = new DtoEntidadesFinancieras();
//
//					try {
//						dtoEntidadFinanciera = dtoEntidadesFinancieras.getClass().newInstance();
//					
//					} catch (Exception e) {
//		
//						e.printStackTrace();
//					}
//
//					for (DtoDetalleDocSopor dtoDetalleDocSopor : listadoDefinitivoDocSop) {
//						
//						if(dtoDetalleDocSopor.getConsecutivoFormaPago() == formaPago.getConsecutivo() && dtoDetalleDocSopor.getConsecutivoEntFinanciera() == dtoEntidadesFinancieras.getConsecutivo()){
//							
//							dtoEntidadFinanciera.getListadoDtoDetDocSoporte().add(dtoDetalleDocSopor);
//							
//							totalValorParcialEntidad += dtoDetalleDocSopor.getValorSistemaUnico();
//						}
//					}
//	
//					if(totalValorParcialEntidad!=0){
//						
//						dtoEntidadFinanciera.setTotalValorSistema(totalValorParcialEntidad);
//						dtoEntidadFinanciera.setDescripcionTercero(dtoEntidadesFinancieras.getDescripcionTercero());
//						dtoFormaPagoDocSoporte.getListadoDtoEntidadesFinancieras().add(dtoEntidadFinanciera);
//						totalValorFormaPago += totalValorParcialEntidad;
//						
//					}
//				}
//				
//			}else { 
//				
//				Log4JManager.info("entre forma de pago diferente ****************************************");
//				/*
//				 * Otras Formas de pago que no tienen asociadas entidades financieras. Tales como formas de pago
//				 * con tipo NINGUNO o tipo BONO
//				 */
//				totalValorParcialEntidad = 0;
//				
//				DtoEntidadesFinancieras dtoEntidadesFinancieras = new DtoEntidadesFinancieras();
//				dtoEntidadesFinancieras.setCodigo(ConstantesBD.acronimoEntidadNoValida);
//				
//				for (DtoDetalleDocSopor dtoDetalleDocSopor : listadoDefinitivoDocSop) {
//					
//					if(dtoDetalleDocSopor.getConsecutivoFormaPago() == formaPago.getConsecutivo()){
//						
//						dtoEntidadesFinancieras.getListadoDtoDetDocSoporte().add(dtoDetalleDocSopor);
//						totalValorParcialEntidad += dtoDetalleDocSopor.getValorSistemaUnico();
//					}
//				}
//				
//				if(totalValorParcialEntidad!=0){
//					
//					dtoEntidadesFinancieras.setTotalValorSistema(totalValorParcialEntidad);
//					dtoFormaPagoDocSoporte.getListadoDtoEntidadesFinancieras().add(dtoEntidadesFinancieras);
//					
//					totalValorFormaPago = totalValorParcialEntidad;
//				}
//			}
//			
//			if(totalValorFormaPago > 0){
//				
//				DtoCuadreCaja dtoCuadreCaja = new DtoCuadreCaja();
//				dtoCuadreCaja.setTipoFormaPago(formaPago.getConsecutivo());
//				dtoCuadreCaja.setTipoDetalleFormaPago(formaPago.getTiposDetalleFormaPago().getCodigo());
//				dtoCuadreCaja.setFormaPago(formaPago.getDescripcion());
//				dtoCuadreCaja.setValorSistema(totalValorFormaPago);
//
//				dtoFormaPagoDocSoporte.setDtoCuadreCaja(dtoCuadreCaja);
//				dtoInformacionEntrega.getCuadreCajaDTOs().add(dtoCuadreCaja);
//				dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes().add(dtoFormaPagoDocSoporte);
//				totalEntregadoTransportadora += totalValorFormaPago;
//			}
//			
//		}
//		
//		dtoInformacionEntrega.setTotalEntregadoTransportadora(totalEntregadoTransportadora);
//		
//		return dtoInformacionEntrega;
//	}
	
	

//	/**
//	 * 
//	 * @param consultaEntregaTransp
//	 * @return
//	 */
//	
//	
//	public DtoInformacionEntrega consultarEntregaTransportadora (DtoConsultaEntregaTransportadora consultaEntregaTransp)  {
//		
//		IDocSopMovimCajasMundo docSopMovimCajasMundo = TesoreriaMundoFabrica.crearDocSopMovimCajasMundo();
//
//		DtoInformacionEntrega dtoInformacionEntrega =new  DtoInformacionEntrega();
//		
//		ETipoMovimiento eTipoMovimiento = ETipoMovimiento.ENTREGA_TRANSPORTADORA_VALORES;
//
//		MovimientosCaja movimientosCajaEntrega = new MovimientosCaja();
//		
//		ArrayList<Integer> codigosTipoMovimiento = new ArrayList<Integer>();
//		
//		codigosTipoMovimiento.add(ConstantesBD.codigoTipoMovimientoEntregaTransportadora);
//		
//		movimientosCajaEntrega = movimientosCajaEntrega.getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk();
//
//		ArrayList<DtoDetalleDocSopor> listadoEntregasTrans = (ArrayList<DtoDetalleDocSopor>) docSopMovimCajasMundo.obtenerEntregasTransportadoraYCajaMayorPrincipal(movimientosCajaEntrega);
//		dtoInformacionEntrega =  obtenerDtoInformacionEntrega(listadoEntregasTrans, consultaEntregaTransp.getCodigoCentroAtencion(), true);
//		dtoInformacionEntrega.setMovimientosCaja(movimientosCajaEntrega);
//		
//		dtoInformacionEntrega.setTransportadoraValores(movimientosCajaEntrega.getEntregaTransportadoraByCodigoPk().getTransportadoraValores());
//		
//		dtoInformacionEntrega.setETipoMovimiento(eTipoMovimiento);
//		
//		return dtoInformacionEntrega;
//		
//	}
	
	/*
	 * /**
	 * Retorna un {@link DtoInformacionEntrega} con la informaci&oacute;n necesaria para realizar los
	 * Arqueos Parciales (Entregas a Caja Mayor / Principal ó Entregas a Transportadora) y 
	 * Aceptaciones de Solicitudes de Traslado a Caja.
	 *
	 * @param listadoDefinitivoDocSop
	 * @param codigoInstitucion
	 * @param restriccion (este atributo es utilizado para poder recorrer el listado disponible de 
	 * documentos sin tener en cuenta la parametrizaci&oacute;n Consignaci&oacute;n Banco)
	 * @return
	 *
	@Override
	public DtoInformacionEntrega obtenerDtoInformacionEntrega(ArrayList<DtoDetalleDocSopor> listadoDefinitivoDocSop, int codigoInstitucion, boolean restriccion) {
		
		IFormasPagoMundo formasPagoMundo = TesoreriaMundoFabrica.crearFormasPagoMundo();
		
		IEntidadesFinancierasMundo entidadesFinancierasMundo = TesoreriaMundoFabrica.crearEntidadesFinancierasMundo();
		
		List<FormasPago> formasPagos = formasPagoMundo.obtenerFormasPagos();
		ArrayList<DtoEntidadesFinancieras> listadoCompletoEntidades = (ArrayList<DtoEntidadesFinancieras>) entidadesFinancierasMundo.obtenerEntidadesPorInstitucion(codigoInstitucion, false);
		
		DtoInformacionEntrega dtoInformacionEntrega =  new DtoInformacionEntrega();
		
		double totalValorFormaPago;
		double totalValorParcialEntidad;
		double totalEntregadoTransportadora = 0;
		
		DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte;
		
		for (FormasPago formaPago : formasPagos) {
			
			if(restriccion || formaPago.getIndConsignacion().toString().equals(ConstantesBD.acronimoSi)){

				dtoFormaPagoDocSoporte = new DtoFormaPagoDocSoporte();
				dtoFormaPagoDocSoporte.setConsecutivoFormaPago(formaPago.getConsecutivo());
				dtoFormaPagoDocSoporte.setFormaPago(formaPago.getDescripcion());
				dtoFormaPagoDocSoporte.setCodigoTipoDetalleFormaPago(formaPago.getTiposDetalleFormaPago().getCodigo());
				dtoFormaPagoDocSoporte.setIndicativoTrasladoCajaRecaudo(formaPago.getTrasladoCajaRecaudo().toString());
				
				totalValorFormaPago = 0;
				
				if(formaPago.getTiposDetalleFormaPago().getCodigo() == ConstantesBD.codigoTipoDetalleFormasPagoCheque 
					|| formaPago.getTiposDetalleFormaPago().getCodigo() == ConstantesBD.codigoTipoDetalleFormasPagoTarjeta ){
					
					for (DtoEntidadesFinancieras dtoEntidadesFinancieras : listadoCompletoEntidades) {
						
						totalValorParcialEntidad = 0;
						
						DtoEntidadesFinancieras dtoEntidadFinanciera = new DtoEntidadesFinancieras();

						try {
							dtoEntidadFinanciera = dtoEntidadesFinancieras.getClass().newInstance();
						
						} catch (Exception e) {
			
							e.printStackTrace();
						}
	
						for (DtoDetalleDocSopor dtoDetalleDocSopor : listadoDefinitivoDocSop) {
							
							if(dtoDetalleDocSopor.getConsecutivoFormaPago() == formaPago.getConsecutivo() && dtoDetalleDocSopor.getConsecutivoEntFinanciera() == dtoEntidadesFinancieras.getConsecutivo()){
								
								dtoEntidadFinanciera.getListadoDtoDetDocSoporte().add(dtoDetalleDocSopor);
								
								totalValorParcialEntidad += dtoDetalleDocSopor.getValorSistemaUnico();
							}
						}
		
						if(totalValorParcialEntidad!=0){
							
							dtoEntidadFinanciera.setTotalValorSistema(totalValorParcialEntidad);
							dtoEntidadFinanciera.setDescripcionTercero(dtoEntidadesFinancieras.getDescripcionTercero());
							dtoFormaPagoDocSoporte.getListadoDtoEntidadesFinancieras().add(dtoEntidadFinanciera);
							totalValorFormaPago += totalValorParcialEntidad;
							
						}
					}
					
				}else { // Forma de pago igual a Bono y Efectivo (efectivo solo lo recaudado m&aacute;s no lo aceptado)
					
					totalValorParcialEntidad = 0;
					
					DtoEntidadesFinancieras dtoEntidadesFinancieras = new DtoEntidadesFinancieras();
					dtoEntidadesFinancieras.setCodigo(ConstantesBD.acronimoEntidadNoValida);
					
					for (DtoDetalleDocSopor dtoDetalleDocSopor : listadoDefinitivoDocSop) {
						
						if(dtoDetalleDocSopor.getConsecutivoFormaPago() == formaPago.getConsecutivo()){
							
							dtoEntidadesFinancieras.getListadoDtoDetDocSoporte().add(dtoDetalleDocSopor);
							totalValorParcialEntidad += dtoDetalleDocSopor.getValorSistemaUnico();
						}
					}
					
//					esto es para el valor base en caso de necesitarse
//					if(formaPago.getTiposDetalleFormaPago().getCodigo() == ConstantesBD.codigoTipoDetalleFormasPagoNinguno){
//						
//						totalValorParcialEntidad += valorBase;
//					}
					
					if(totalValorParcialEntidad!=0){
						
						dtoEntidadesFinancieras.setTotalValorSistema(totalValorParcialEntidad);
						dtoFormaPagoDocSoporte.getListadoDtoEntidadesFinancieras().add(dtoEntidadesFinancieras);
						
						totalValorFormaPago = totalValorParcialEntidad;
					}
				}
				
				if(totalValorFormaPago!=0){
					
					DtoCuadreCaja dtoCuadreCaja = new DtoCuadreCaja();
					dtoCuadreCaja.setTipoFormaPago(formaPago.getConsecutivo());
					dtoCuadreCaja.setTipoDetalleFormaPago(formaPago.getTiposDetalleFormaPago().getCodigo());
					dtoCuadreCaja.setFormaPago(formaPago.getDescripcion());
					dtoCuadreCaja.setValorSistema(totalValorFormaPago);

					dtoFormaPagoDocSoporte.setDtoCuadreCaja(dtoCuadreCaja);
					dtoInformacionEntrega.getCuadreCajaDTOs().add(dtoCuadreCaja);
					dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes().add(dtoFormaPagoDocSoporte);
					totalEntregadoTransportadora += totalValorFormaPago;
				}
			}
		}
		
		dtoInformacionEntrega.setTotalEntregadoTransportadora(totalEntregadoTransportadora);
		
		return dtoInformacionEntrega;
	}
	 */
	
	
//	/**
//	 * Este m&eacute;todo se encarga de adicionar a la forma de pago efectivo para realizar la entrega, las aceptaciones 
//	 * realizadas de efectivo que no han sido entregadas previamente.
//	 * 
//	 * @param dtoInformacionEntrega
//	 * @param listadoAceptaciones
//	 * @param codigosTipoMovimiento
//	 * @return
//	 */
//	public DtoInformacionEntrega agregarValorEfectivoAceptacion (DtoInformacionEntrega dtoInformacionEntrega, ArrayList<DtoDetalleDocSopor> listadoAceptaciones, ArrayList<Integer> codigosTipoMovimiento, long codigoMovimiento){
//		
//		IDocSopMovimCajasMundo docSopMovimCajasMundo = TesoreriaMundoFabrica.crearDocSopMovimCajasMundo();
//		
//		List<DocSopMovimCajas> listadoDocSopMovSoporte = docSopMovimCajasMundo.obtenerListaDocSoporEfectPorTipoMovimiento(dtoInformacionEntrega.getMovimientosCaja().getTurnoDeCaja(), codigosTipoMovimiento, codigoMovimiento);
//		
//		double valorAceptadoEfectivo = 0;
//		
//		boolean asignado = false;
//		
//		for (DocSopMovimCajas docSopMovimCajas : listadoDocSopMovSoporte) {
//			
//			valorAceptadoEfectivo += docSopMovimCajas.getValor().doubleValue();
//		}
//	
//		Log4JManager.info("valor del efectivo " + valorAceptadoEfectivo);
//		
//		for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte : dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes()) {
//			
//			if(dtoFormaPagoDocSoporte.getCodigoTipoDetalleFormaPago() == ConstantesBD.codigoTipoDetalleFormasPagoNinguno){
//				
//				double valorSistema = dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorSistema();
//				
//				valorSistema += valorAceptadoEfectivo;
//				
//				dtoFormaPagoDocSoporte.getDtoCuadreCaja().setValorSistema(valorSistema);
//				
//				dtoFormaPagoDocSoporte.getListadoDtoEntidadesFinancieras().get(0).getListadoDtoDetDocSoporte().addAll(listadoAceptaciones);
//				
//				asignado = true;
//				
//				break;
//			}
//		}
//		
//		
//		/* Cuando no se asigna en el for anterior, significa que no existe una forma de pago asociada al efectivo, por lo tanto
//		 * se debe crear una forma de pago y asociarle los documentos de soporte respectivos
//		 */
//		
//		if(!asignado){
//			
//			IFormasPagoMundo formasPagoMundo = TesoreriaMundoFabrica.crearFormasPagoMundo();
//			
//			List<FormasPago> listadoFormasPago = formasPagoMundo.obtenerFormasPagos();
//			
//			for (FormasPago formaPago : listadoFormasPago) {
//				
//				if(formaPago.getTiposDetalleFormaPago().getCodigo() == ConstantesBD.codigoTipoDetalleFormasPagoNinguno){
//					
//					DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte = new DtoFormaPagoDocSoporte();
//				
//					dtoFormaPagoDocSoporte.setConsecutivoFormaPago(formaPago.getConsecutivo());
//					dtoFormaPagoDocSoporte.setFormaPago(formaPago.getDescripcion());
//					dtoFormaPagoDocSoporte.setCodigoTipoDetalleFormaPago(formaPago.getTiposDetalleFormaPago().getCodigo());
//					dtoFormaPagoDocSoporte.setIndicativoTrasladoCajaRecaudo(formaPago.getTrasladoCajaRecaudo().toString());
//					
//					DtoCuadreCaja dtoCuadreCaja = new DtoCuadreCaja();
//					dtoCuadreCaja.setTipoFormaPago(formaPago.getConsecutivo());
//					dtoCuadreCaja.setFormaPago(formaPago.getDescripcion());
//					dtoCuadreCaja.setTipoDetalleFormaPago(formaPago.getTiposDetalleFormaPago().getCodigo());
//					dtoCuadreCaja.setValorSistema(valorAceptadoEfectivo);
//					
//					dtoFormaPagoDocSoporte.setDtoCuadreCaja(dtoCuadreCaja);
//					
//					DtoEntidadesFinancieras dtoEntidadesFinancieras = new DtoEntidadesFinancieras();
//					dtoEntidadesFinancieras.setCodigo(ConstantesBD.acronimoEntidadNoValida);
//					
//					dtoEntidadesFinancieras.getListadoDtoDetDocSoporte().addAll(listadoAceptaciones);
//				
//					dtoEntidadesFinancieras.setTotalValorSistema(valorAceptadoEfectivo);
//					
//					dtoFormaPagoDocSoporte.getListadoDtoEntidadesFinancieras().add(dtoEntidadesFinancieras);
//
//					dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes().add(0, dtoFormaPagoDocSoporte);
//					
//					break;
//				}
//			}
//		}
//		
//		return dtoInformacionEntrega;
//	}
//	
	
//	/**
//	 * Este m&eacute;todo se encarga de totalizar el valor entregado (Transportadora de valores o Caja Mayor / Principal)
//	 *  por forma de pago Efectivo
//	 * 
//	 * @param dtoInformacionEntrega
//	 * @param lcodigoMovimiento 
//	 * @param listadoAceptaciones
//	 * @return
//	 */
//	public DtoInformacionEntrega agregarTotalEfectivoEntregado (DtoInformacionEntrega dtoInformacionEntrega, ArrayList<Integer> codigosTipoMovimiento, long codigoMovimiento){
//		
//		IDocSopMovimCajasMundo docSopMovimCajasMundo = TesoreriaMundoFabrica.crearDocSopMovimCajasMundo();
//		
//		List<DocSopMovimCajas> listadoDocSopMovSoporte = docSopMovimCajasMundo.obtenerListaDocSoporEfectPorTipoMovimiento(dtoInformacionEntrega.getMovimientosCaja().getTurnoDeCaja(), codigosTipoMovimiento, codigoMovimiento);
//		
//		double valorAceptadoEfectivo = 0;
//		
//		if(listadoDocSopMovSoporte.size()>0){
//			
//			for (DocSopMovimCajas docSopMovimCajas : listadoDocSopMovSoporte) {
//				
//				valorAceptadoEfectivo += docSopMovimCajas.getValor().doubleValue();
//			}
//		
//			for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte : dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes()) {
//				
//				if(dtoFormaPagoDocSoporte.getCodigoTipoDetalleFormaPago() == ConstantesBD.codigoTipoDetalleFormasPagoNinguno){
//					
//					dtoFormaPagoDocSoporte.getDtoCuadreCaja().setValorSistema(valorAceptadoEfectivo);
//					
//					break;
//				}
//			}
//		}
//
//		return dtoInformacionEntrega;
//	}
	
}
