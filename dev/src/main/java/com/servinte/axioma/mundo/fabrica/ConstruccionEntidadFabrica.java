package com.servinte.axioma.mundo.fabrica;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoCuadreCaja;
import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.princetonsa.dto.tesoreria.DtoFormaPagoDocSoporte;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento;
import com.servinte.axioma.mundo.interfaz.administracion.IUsuariosMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDetallePagosRcMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITiposMovimientoCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITransportadoraValoresMundo;
import com.servinte.axioma.orm.AceptacionTrasladoCaja;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.CuadreCaja;
import com.servinte.axioma.orm.DetFaltanteSobrante;
import com.servinte.axioma.orm.DetalleMovCajaCierre;
import com.servinte.axioma.orm.DetallePagosRc;
import com.servinte.axioma.orm.DetallePagosRcHome;
import com.servinte.axioma.orm.DocSopMovimCajas;
import com.servinte.axioma.orm.EntregaCajaMayor;
import com.servinte.axioma.orm.FaltanteSobrante;
import com.servinte.axioma.orm.FormasPago;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.orm.SolicitudTrasladoCaja;
import com.servinte.axioma.orm.TiposMovimientoCaja;
import com.servinte.axioma.orm.TurnoDeCajaHome;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.persistencia.UtilidadPersistencia;


/**
 * Construye las entidades MovimientosCaja seg&uacute;n el tipo de movimiento de caja espec&iacute;ficado
 * 
 * @author Jorge Armando Agudelo
 * @see MovimientosCaja
 */
public class ConstruccionEntidadFabrica {
	
	
	/**
	 * M&eacute;todo que ayuda a construir un movimiento de caja con la informacion asociada seg&uacute;n el tipo al
	 * que corresponda.
	 * 
	 * @param dtoConsolidadoMovimiento
	 * @return un objeto de tipo MovimientosCaja con la informaci&oacute;n debidamente relacionada y listo para continuar con el
	 * proceso de registro
	 */

	public static MovimientosCaja construirEntidad (DtoConsolidadoMovimiento dtoConsolidadoMovimiento){
		
		MovimientosCaja movimientosCaja = new MovimientosCaja();
		
		switch (dtoConsolidadoMovimiento.getETipoMovimiento()) {
			
		case ARQUEO_CAJA:
		
			movimientosCaja = construirArqueoCaja(dtoConsolidadoMovimiento);
		
			break;

		case ARQUEO_ENTREGA_PARCIAL:
			
			break;
			
		case CIERRE_CAJA:
			
			if(dtoConsolidadoMovimiento instanceof DtoInformacionEntrega)
			{
				DtoInformacionEntrega dtoInformacionEntrega = (DtoInformacionEntrega) dtoConsolidadoMovimiento;
				movimientosCaja = construirCierreTurnoCaja (dtoInformacionEntrega);
			}
			
			break;
			
			
		case ENTREGA_CAJA_MAYOR_PRINCIPAL:

			if(dtoConsolidadoMovimiento instanceof DtoInformacionEntrega)
			{
				DtoInformacionEntrega dtoInformacionEntrega = (DtoInformacionEntrega) dtoConsolidadoMovimiento;
				movimientosCaja = construirEntregaCajaMayorPrincipal (dtoInformacionEntrega);
			}
			
			break;
			
			
		case ACEPTACION_APERTURA_TURNO:
			
			if(dtoConsolidadoMovimiento instanceof DtoInformacionEntrega)
			{
				DtoInformacionEntrega dtoInformacionEntrega = (DtoInformacionEntrega) dtoConsolidadoMovimiento;
				movimientosCaja = construirAceptacionEntrega (dtoInformacionEntrega);
			}
			break;
	
		case ENTREGA_TRANSPORTADORA_VALORES:
			
			if(dtoConsolidadoMovimiento instanceof DtoInformacionEntrega)
			{
				DtoInformacionEntrega dtoInformacionEntrega = (DtoInformacionEntrega) dtoConsolidadoMovimiento;
				movimientosCaja = construirEntregaTransportadoraValores (dtoInformacionEntrega);
			}
			
			break;

		default:
			break;
		}
		
		return movimientosCaja;
	}


	/**
	 * Construye y retorna un movimiento de Caja de tipo Arqueo Caja.
	 * 
	 * @param dtoConsolidadoMovimiento
	 * @return
	 */
	private static MovimientosCaja construirArqueoCaja(DtoConsolidadoMovimiento dtoConsolidadoMovimiento) {
		
		MovimientosCaja movimientosCaja;
		movimientosCaja = dtoConsolidadoMovimiento.getMovimientosCaja();
		
		Date fechaActual = UtilidadFecha.getFechaActualTipoBD();
		
		movimientosCaja.setFecha(fechaActual);
		movimientosCaja.setHora(UtilidadFecha.getHoraActual());
		
		if(movimientosCaja.getFechaMovimiento() == null) {
			movimientosCaja.setFechaMovimiento(fechaActual);
			movimientosCaja.setIngresaFechaArqueo(ConstantesBD.acronimoNoChar);
		} else {
			movimientosCaja.setIngresaFechaArqueo(ConstantesBD.acronimoSiChar);
		}
		
		TurnoDeCajaHome turnoDeCajaHome = new TurnoDeCajaHome();
		turnoDeCajaHome.attachDirty(movimientosCaja.getTurnoDeCaja());
		
		HashSet<CuadreCaja> setCuadreCaja = new HashSet<CuadreCaja>();
		
		for (DtoCuadreCaja dtoCuadreCaja : dtoConsolidadoMovimiento.getCuadreCajaDTOs()) {
			
			CuadreCaja cuadreCaja = new CuadreCaja();
			FormasPago formasPago= new FormasPago();
			formasPago.setConsecutivo(dtoCuadreCaja.getTipoFormaPago());
			formasPago.setDescripcion(dtoCuadreCaja.getFormaPago());
			cuadreCaja.setFormasPago(formasPago);
			cuadreCaja.setValorcaja(new BigDecimal(dtoCuadreCaja.getValorCaja()));
			cuadreCaja.setValorsistema(new BigDecimal(dtoCuadreCaja.getValorSistema()));
			cuadreCaja.setValordiferencia(new BigDecimal(dtoCuadreCaja.getValorDiferencia()));
			cuadreCaja.setMovimientosCaja(movimientosCaja);
			setCuadreCaja.add(cuadreCaja);
		}
	
		movimientosCaja.setCuadreCajas(setCuadreCaja);
		
		return movimientosCaja;
	}
	

	/**
	 * Crea un movimiento con toda la informaci&oacute;n necesaria para registrar un movimiento de caja
	 * Entrega a Caja Mayor / Principal
	 * 
	 * @param dtoInformacionEntrega
	 * 
	 */
	private static MovimientosCaja construirEntregaCajaMayorPrincipal(DtoInformacionEntrega dtoInformacionEntrega) {
		
		MovimientosCaja movimientosCaja;
		
		movimientosCaja = dtoInformacionEntrega.getMovimientosCaja();
		ITiposMovimientoCajaMundo tiposMovimientoCajaMundo = TesoreriaFabricaMundo.crearTiposMovimientoCajaMundo();
		IDetallePagosRcMundo detallePagosRcMundo = TesoreriaFabricaMundo.crearDetallePagosRcMundo();
		
		movimientosCaja.setFecha(UtilidadFecha.getFechaActualTipoBD());
		movimientosCaja.setHora(UtilidadFecha.getHoraActual());
		
		movimientosCaja.setIngresaFechaArqueo(ConstantesBD.acronimoSiChar);
		
		TurnoDeCajaHome turnoDeCajaHome = new TurnoDeCajaHome();
		turnoDeCajaHome.attachDirty(movimientosCaja.getTurnoDeCaja());
		
		movimientosCaja.getEntregaCajaMayorByCodigoPk().getMovimientosCajaByCodigoPk().setFecha(UtilidadFecha.getFechaActualTipoBD());
		movimientosCaja.getEntregaCajaMayorByCodigoPk().getMovimientosCajaByCodigoPk().setHora(UtilidadFecha.getHoraActual());
		movimientosCaja.getEntregaCajaMayorByCodigoPk().getMovimientosCajaByCodigoPk().setFechaMovimiento(movimientosCaja.getFechaMovimiento());
		movimientosCaja.getEntregaCajaMayorByCodigoPk().getMovimientosCajaByCodigoPk().setTiposMovimientoCaja(tiposMovimientoCajaMundo.findById(ConstantesBD.codigoTipoMovimientoEntregaCajaPrincipalMayor));
		movimientosCaja.getEntregaCajaMayorByCodigoPk().getMovimientosCajaByCodigoPk().setUsuarios(movimientosCaja.getUsuarios());
		movimientosCaja.getEntregaCajaMayorByCodigoPk().getMovimientosCajaByCodigoPk().setTurnoDeCaja(movimientosCaja.getTurnoDeCaja());
		movimientosCaja.getEntregaCajaMayorByCodigoPk().getMovimientosCajaByCodigoPk().setContabilizado('N');
		movimientosCaja.getEntregaCajaMayorByCodigoPk().getMovimientosCajaByCodigoPk().setIngresaFechaArqueo(ConstantesBD.acronimoSiChar);
		movimientosCaja.getEntregaCajaMayorByCodigoPk().getMovimientosCajaByCodigoPk().setObservaciones(dtoInformacionEntrega.getObservaciones());
		movimientosCaja.getEntregaCajaMayorByCodigoPk().setCajas(dtoInformacionEntrega.getCajaMayorPrincipal());
		movimientosCaja.getEntregaCajaMayorByCodigoPk().setEstado(ConstantesIntegridadDominio.acronimoEstadoEntregado);
		
		movimientosCaja.getEntregaCajaMayorByCodigoPk().setMovimientosCajaByMovimientoCajaArqueo(movimientosCaja);
		
		HashSet<DocSopMovimCajas> docSopMovimCajases = new HashSet<DocSopMovimCajas>();
		
		for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte : dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes()) {

			if(ConstantesBD.acronimoSi.equalsIgnoreCase(dtoFormaPagoDocSoporte.getSeleccionado())){
				
				FormasPago formaPago = new FormasPago();
				
				for (DtoEntidadesFinancieras entidadesFinancieras : dtoFormaPagoDocSoporte.getListadoDtoEntidadesFinancieras()) {
									
					if(dtoFormaPagoDocSoporte.getCodigoTipoDetalleFormaPago() == ConstantesBD.codigoTipoDetalleFormasPagoNinguno) {
							
						DocSopMovimCajas docSopMovimCajas = new DocSopMovimCajas();
						docSopMovimCajas.setMovimientosCaja(movimientosCaja.getEntregaCajaMayorByCodigoPk().getMovimientosCajaByCodigoPk());
						formaPago.setConsecutivo(dtoFormaPagoDocSoporte.getConsecutivoFormaPago());							
						formaPago.setDescripcion(dtoFormaPagoDocSoporte.getFormaPago());
						docSopMovimCajas.setFormasPago(formaPago);
						docSopMovimCajas.setValor(new BigDecimal(dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorSistema()));
						docSopMovimCajas.setCodigoPk(0);
						
//						for (DtoDetalleDocSopor dtoDetalleDocSopor : entidadesFinancieras.getListadoDtoDetDocSoporte()) {
//							
//							HashSet<DocSopMovimCajas> docSopMovimCajasesTemp = new HashSet<DocSopMovimCajas>();
//							
//							DetallePagosRc detallePagosRc = detallePagosRcMundo.findById(dtoDetalleDocSopor.getDetallePagosId());
//							
//							detallePagosRc.setConsecutivo(dtoDetalleDocSopor.getDetallePagosId());
//						
//							docSopMovimCajasesTemp.add(docSopMovimCajas);
//							detallePagosRc.setDocSopMovimCajases(docSopMovimCajasesTemp);
//						
//							detallePagosRcs.add(detallePagosRc);
//						}
						
						//docSopMovimCajas.setDetallePagosRcs(detallePagosRcs);
						docSopMovimCajases.add(docSopMovimCajas);
						
					}else{

						for (DtoDetalleDocSopor dtoDetalleDocSopor : entidadesFinancieras.getListadoDtoDetDocSoporte()){
							
							HashSet<DetallePagosRc> detallePagosRcs = new HashSet<DetallePagosRc>();
							
							HashSet<DocSopMovimCajas> docSopMovimCajasesAux = new HashSet<DocSopMovimCajas>();
							
							DocSopMovimCajas docSopMovimCajas = new DocSopMovimCajas();
							docSopMovimCajas.setMovimientosCaja(movimientosCaja.getEntregaCajaMayorByCodigoPk().getMovimientosCajaByCodigoPk());
							formaPago.setConsecutivo(dtoDetalleDocSopor.getConsecutivoFormaPago());							
							formaPago.setDescripcion(dtoFormaPagoDocSoporte.getFormaPago());
							docSopMovimCajas.setFormasPago(formaPago);
							docSopMovimCajas.setCodigoPk(0);
							docSopMovimCajas.setNroDocumento(dtoDetalleDocSopor.getNroDocumentoEntregado());
							docSopMovimCajas.setValor(new BigDecimal(dtoDetalleDocSopor.getValorSistemaUnico()));
							
							DetallePagosRc detallePagosRc = detallePagosRcMundo.findById(dtoDetalleDocSopor.getDetallePagosId());
						
							docSopMovimCajasesAux.add(docSopMovimCajas);
							detallePagosRc.setDocSopMovimCajases(docSopMovimCajasesAux);
						
							detallePagosRcs.add(detallePagosRc);
							
							docSopMovimCajas.setDetallePagosRcs(detallePagosRcs);
							docSopMovimCajases.add(docSopMovimCajas);
				
						}
					}
				}
			}
		}
		
		movimientosCaja.getEntregaCajaMayorByCodigoPk().getMovimientosCajaByCodigoPk().setDocSopMovimCajases(docSopMovimCajases);
	
		return movimientosCaja;
	}
	
	/**
	 * Crea un movimiento con toda la informaci&oacute;n necesaria para registrar un movimiento de caja
	 * Entrega a Transportadora de Valores
	 * 
	 * @param dtoInformacionEntrega
	 * @return
	 */
	private static MovimientosCaja construirEntregaTransportadoraValores(DtoInformacionEntrega dtoInformacionEntrega) {
		
		MovimientosCaja movimientosCaja;
		
		movimientosCaja = dtoInformacionEntrega.getMovimientosCaja();
		ITiposMovimientoCajaMundo tiposMovimientoCajaMundo = TesoreriaFabricaMundo.crearTiposMovimientoCajaMundo();
		ITransportadoraValoresMundo transportadoraValoresMundo = TesoreriaFabricaMundo.crearTransportadoraValoresMundo();
		
		movimientosCaja.setFecha(UtilidadFecha.getFechaActualTipoBD());
		movimientosCaja.setHora(UtilidadFecha.getHoraActual());
		movimientosCaja.setIngresaFechaArqueo(ConstantesBD.acronimoSiChar);
		
		TurnoDeCajaHome turnoDeCajaHome = new TurnoDeCajaHome();
		turnoDeCajaHome.attachDirty(movimientosCaja.getTurnoDeCaja());
		
		movimientosCaja.getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk().setFecha(UtilidadFecha.getFechaActualTipoBD());
		movimientosCaja.getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk().setHora(UtilidadFecha.getHoraActual());
		movimientosCaja.getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk().setFechaMovimiento(movimientosCaja.getFechaMovimiento());
		movimientosCaja.getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk().setTiposMovimientoCaja(tiposMovimientoCajaMundo.findById(ConstantesBD.codigoTipoMovimientoEntregaTransportadora));
		movimientosCaja.getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk().setUsuarios(movimientosCaja.getUsuarios());
		movimientosCaja.getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk().setTurnoDeCaja(movimientosCaja.getTurnoDeCaja());
		movimientosCaja.getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk().setContabilizado('N');
		movimientosCaja.getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk().setIngresaFechaArqueo(ConstantesBD.acronimoSiChar);
		movimientosCaja.getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk().setObservaciones(dtoInformacionEntrega.getObservaciones());
		movimientosCaja.getEntregaTransportadoraByCodigoPk().setEstado(ConstantesIntegridadDominio.acronimoEstadoEntregaTransportadoraValores);
		
		movimientosCaja.getEntregaTransportadoraByCodigoPk().setTransportadoraValores(transportadoraValoresMundo.consultarTransportadoraValores(dtoInformacionEntrega.getTransportadoraValores().getCodigoPk()));
 
		movimientosCaja.getEntregaTransportadoraByCodigoPk().setMovimientosCajaByMovimientoCajaArqueo(movimientosCaja);
		
		crearSetDocSopMovimCajas(dtoInformacionEntrega, movimientosCaja.getEntregaTransportadoraByCodigoPk().getMovimientosCajaByCodigoPk(), ETipoMovimiento.ENTREGA_TRANSPORTADORA_VALORES);

		return movimientosCaja;
	}

	
	
	
	/**
	 * Crea un movimiento de tipo aceptacion de solicitud en el momento de hacer apertura de turno
	 * 
	 * @param dtoInformacionEntrega
	 */
	private static MovimientosCaja construirAceptacionEntrega(DtoInformacionEntrega dtoInformacionEntrega) 
	{
		// -->  Variables necesarias para la construccion
		IMovimientosCajaMundo movimientosCajaMundo 			= TesoreriaFabricaMundo.crearMovimientosCajaMundo(); 
		
		long codigoPkInvalido	= 0;
		FormasPago formasPago	= null;
		
		// --> Variables para la totalizacion del efectivo
		double totalEntregadoEfect		= 0;
		double totalAceptadoEfect		= 0;
		int consecutivoFormaPagoEfect 	= 0;
		String descripcionFormaPagoEfect = "";
				
		// --> Creacion y relacion de Movimiento Caja y Turno de Caja
		MovimientosCaja movimientosCaja = new MovimientosCaja();
		movimientosCaja.setFecha(dtoInformacionEntrega.getMovimientosCaja().getTurnoDeCaja().getFechaApertura());
		movimientosCaja.setHora(dtoInformacionEntrega.getMovimientosCaja().getTurnoDeCaja().getHoraApertura());
		movimientosCaja.setUsuarios(dtoInformacionEntrega.getMovimientosCaja().getTurnoDeCaja().getUsuarios());
		movimientosCaja.setObservaciones(dtoInformacionEntrega.getObservacionesAceptacion());
		movimientosCaja.setContabilizado(ConstantesBD.acronimoNo.charAt(0)); //'N'
		TiposMovimientoCaja tiposMovimientoCaja = new TiposMovimientoCaja();
		tiposMovimientoCaja.setCodigo(ConstantesBD.codigoTipoMovimientoAperturaTurnoCaja);
		movimientosCaja.setTiposMovimientoCaja(tiposMovimientoCaja);
		movimientosCaja.setFechaMovimiento(UtilidadFecha.getFechaActualTipoBD());
		movimientosCaja.setIngresaFechaArqueo(ConstantesBD.acronimoSiChar);
		
		HashSet<MovimientosCaja> setMovimientosCaja = new HashSet<MovimientosCaja>();
		setMovimientosCaja.add(movimientosCaja);

		movimientosCaja.setTurnoDeCaja(dtoInformacionEntrega.getMovimientosCaja().getTurnoDeCaja());
		movimientosCaja.getTurnoDeCaja().setMovimientosCajas(setMovimientosCaja);
		
		// --> Creacion de listas para registro de Documentos de Soporte y Faltantes Sobrante
		ArrayList<DocSopMovimCajas> listaDocSopMovimCajasEfect		= new ArrayList<DocSopMovimCajas>();

		//  --> Creacion de la relacion de la Solicitud con la Aceptacion Traslado Caja
		Usuarios testigo	= new Usuarios(); 
		if(!UtilidadTexto.isEmpty(dtoInformacionEntrega.getLoginTestigo())){
			testigo.setLogin(dtoInformacionEntrega.getLoginTestigo());
		}
		else{
			testigo = null;
		}
				
		AceptacionTrasladoCaja aceptacionTrasladoCaja 	= new AceptacionTrasladoCaja();
		aceptacionTrasladoCaja.setUsuarios(testigo);
		
		movimientosCaja.setAceptacionTrasladoCaja(aceptacionTrasladoCaja);
		aceptacionTrasladoCaja.setMovimientosCaja(movimientosCaja);
		
		SolicitudTrasladoCaja solicitudTrasladoCaja 	= new SolicitudTrasladoCaja();
		solicitudTrasladoCaja.setMovimientoCaja(dtoInformacionEntrega.getIdMovimientoCaja());
		
		HashSet<AceptacionTrasladoCaja> setAceptacionTrasladoCaja = new HashSet<AceptacionTrasladoCaja>();
		setAceptacionTrasladoCaja.add(aceptacionTrasladoCaja);
		
		aceptacionTrasladoCaja.setSolicitudTrasladoCaja(solicitudTrasladoCaja);
		solicitudTrasladoCaja.setAceptacionTrasladoCajas(setAceptacionTrasladoCaja);
		//-----------------------------------

		// --> Creacion del Faltante Sobrante en caso de que se necesite
		FaltanteSobrante faltanteSobrante = new FaltanteSobrante();
		Usuarios responsable = new Usuarios();
		responsable.setLogin(dtoInformacionEntrega.getResponsable().getLogin());
		
		faltanteSobrante.setUsuarios(movimientosCaja.getTurnoDeCaja().getUsuarios());
		faltanteSobrante.setFechaModifica(movimientosCaja.getTurnoDeCaja().getFechaApertura());
		faltanteSobrante.setHoraModifica(movimientosCaja.getTurnoDeCaja().getHoraApertura());
		faltanteSobrante.setEstado(ConstantesIntegridadDominio.acronimoEstadoFaltanteSobranteGenerado);

		// --> Detalles Pagos Rc
		//ArrayList<DetallePagosRc> listaDetallePagosRcsEfect = new ArrayList<DetallePagosRc>();
		DetallePagosRc detallePagosRc = null;
		
		HashSet<DetFaltanteSobrante> setDetFaltanteSobranteTotal = new HashSet<DetFaltanteSobrante>();
		HashSet<DocSopMovimCajas> setDocSopMovimCajasTotal = new HashSet<DocSopMovimCajas>();
		
		// Variables utilizadas para asignacion de consecutivos por isntitucion
		int institucion = dtoInformacionEntrega.getInstitucionActual();
		
		for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte: dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes()) 
		{
			DocSopMovimCajas docSopMovimCajas;
			DetFaltanteSobrante detFaltanteSobrante;
			formasPago			= new FormasPago();
			formasPago.setConsecutivo(dtoFormaPagoDocSoporte.getConsecutivoFormaPago());
			formasPago.setDescripcion(dtoFormaPagoDocSoporte.getFormaPago());
			
			for (DtoEntidadesFinancieras dtoEntidadesFinancieras : dtoFormaPagoDocSoporte.getListadoDtoEntidadesFinancieras())
			{
				// --> Forma de pago Efectivo
				if(dtoFormaPagoDocSoporte.getCodigoTipoDetalleFormaPago() == ConstantesBD.codigoTipoDetalleFormasPagoNinguno) 
				{
					docSopMovimCajas = new DocSopMovimCajas();
					
					totalEntregadoEfect	+= dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorSistema();
					totalAceptadoEfect	+= dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorCaja();
					consecutivoFormaPagoEfect = dtoFormaPagoDocSoporte.getConsecutivoFormaPago();
					descripcionFormaPagoEfect = dtoFormaPagoDocSoporte.getFormaPago();
	
					// -->si se tiene que crear un documento de soporte detallado efectivo es aqui y agregar a la lista
					docSopMovimCajas.setFormasPago(formasPago);
					docSopMovimCajas.setMovimientosCaja(movimientosCaja);
					docSopMovimCajas.setValor(new BigDecimal(totalAceptadoEfect));
					
					listaDocSopMovimCajasEfect.add(docSopMovimCajas);
					
					// --> Lista de los detalles pago rc de cada uno de los efectivos
					/*
					for (DtoDetalleDocSopor dtoDetalleDocSopor : dtoEntidadesFinancieras.getListadoDtoDetDocSoporte()) 
					{
						detallePagosRc = new DetallePagosRcHome().findById(dtoDetalleDocSopor.getDetallePagosId());
						listaDetallePagosRcsEfect.add(detallePagosRc);
					}
					*/
				}
				else
				{
					// --> Otras Formas de pago diferente a efectivo (Bono, cheque, Tarjeta)
					for (DtoDetalleDocSopor dtoDetalleDocSopor : dtoEntidadesFinancieras.getListadoDtoDetDocSoporte()) 
					{
						//doc con enti
						docSopMovimCajas = new DocSopMovimCajas();
						
						docSopMovimCajas.setFormasPago(formasPago);
						docSopMovimCajas.setMovimientosCaja(movimientosCaja);
						
						BigDecimal valor = new BigDecimal(0); 
						if(dtoDetalleDocSopor.getValorActual() != null){
							valor = dtoDetalleDocSopor.getValorActual();
						}
						docSopMovimCajas.setValor(valor);
						
						docSopMovimCajas.setNroDocumento(dtoDetalleDocSopor.getNroDocumentoRecibido());
						
						detallePagosRc = new DetallePagosRc();
						detallePagosRc = new DetallePagosRcHome().findById(dtoDetalleDocSopor.getDetallePagosId());
								
						// -->o Detalle pagos Rc de cada uno de los documentos de soporte
						HashSet<DocSopMovimCajas> setDocSopMovimCajas = new HashSet<DocSopMovimCajas>();
						setDocSopMovimCajas.add(docSopMovimCajas);
								
						detallePagosRc.setDocSopMovimCajases(setDocSopMovimCajas);
													
						HashSet<DetallePagosRc> setDetallePagosRc = new HashSet<DetallePagosRc>();
						setDetallePagosRc.add(detallePagosRc);
						
						docSopMovimCajas.setDetallePagosRcs(setDetallePagosRc);
						
						// validar diferencias de numeros documentos?
						
						if(dtoDetalleDocSopor.getTipodiferencia() != null)
						{
							detFaltanteSobrante = new DetFaltanteSobrante();
							detFaltanteSobrante.setFaltanteSobrante(faltanteSobrante);
							detFaltanteSobrante.setDocSopMovimCajas(docSopMovimCajas);
							detFaltanteSobrante.setValorDiferencia(dtoDetalleDocSopor.getValorDiferencia());
							detFaltanteSobrante.setTipoDiferencia(dtoDetalleDocSopor.getTipodiferencia());
							detFaltanteSobrante.setRecibido(dtoDetalleDocSopor.getIndicativoNoRecibido().charAt(0));
							
							detFaltanteSobrante.setContabilizado(ConstantesBD.acronimoNo.charAt(0));
							detFaltanteSobrante.setUsuarios(responsable); 
							BigDecimal consecutivo = new BigDecimal(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoRegistroFaltanteSobrante, institucion));
							Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
							UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH,ConstantesBD.nombreConsecutivoRegistroFaltanteSobrante, institucion, consecutivo.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
							detFaltanteSobrante.setConsecutivo(consecutivo);
							
							HashSet<DetFaltanteSobrante> setDetFaltanteSobrante = new HashSet<DetFaltanteSobrante>();
							setDetFaltanteSobrante.add(detFaltanteSobrante);
							
							docSopMovimCajas.setDetFaltanteSobrantes(setDetFaltanteSobrante);
							
							setDetFaltanteSobranteTotal.add(detFaltanteSobrante);
						}
						
						setDocSopMovimCajasTotal.add(docSopMovimCajas);
					}
				}
			}
		
		} // fin del for
		
		
		// Armar documentos por forma de pago efectivo (un solo documento para todo el efectivo con todos sus detalles rc)
		if(totalEntregadoEfect != 0)
		{
			// --> organizando efectivos");
			formasPago 				= new FormasPago();
			formasPago.setConsecutivo(consecutivoFormaPagoEfect);
			formasPago.setDescripcion(descripcionFormaPagoEfect);
			String tipodiferencia 	= null;
			
			// --> Documento de soporte unico para todo el efectivo
			DocSopMovimCajas docSopMovimCajas;
			docSopMovimCajas		= new DocSopMovimCajas(codigoPkInvalido, formasPago, movimientosCaja);
			docSopMovimCajas.setValor(new BigDecimal(totalAceptadoEfect));
			/*
			HashSet<DetallePagosRc> detallePagosRcEfec = new HashSet<DetallePagosRc>();
			detallePagosRcEfec.addAll(listaDetallePagosRcsEfect);
			docSopMovimCajas.setDetallePagosRcs(detallePagosRcEfec);
			
			HashSet<DetallePagosRc> setDetallePagosRcEfect = new HashSet<DetallePagosRc>();
			setDetallePagosRcEfect.addAll(listaDetallePagosRcsEfect);
			
			docSopMovimCajas.setDetallePagosRcs(setDetallePagosRcEfect);
			*/
			
			// --> Existe diferencia con el total del efectivo
			if(totalEntregadoEfect != totalAceptadoEfect)
			{
				tipodiferencia 	= movimientosCajaMundo.obtenerTipoDiferencia(totalEntregadoEfect, totalAceptadoEfect);
				DetFaltanteSobrante detFaltanteSobrante = new DetFaltanteSobrante();
				detFaltanteSobrante = new DetFaltanteSobrante();
				detFaltanteSobrante.setFaltanteSobrante(faltanteSobrante);
				detFaltanteSobrante.setDocSopMovimCajas(docSopMovimCajas);
				
				detFaltanteSobrante.setValorDiferencia(new BigDecimal(totalAceptadoEfect -totalEntregadoEfect));
				
				detFaltanteSobrante.setTipoDiferencia(tipodiferencia);
				//detFaltanteSobrante.setRecibido(dtoDetalleDocSopor.getIndicativoNoRecibido().charAt(0));
				
				detFaltanteSobrante.setContabilizado(ConstantesBD.acronimoNo.charAt(0));
				detFaltanteSobrante.setUsuarios(responsable); 
				BigDecimal consecutivo = new BigDecimal(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoRegistroFaltanteSobrante, institucion));
				Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
				UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH,ConstantesBD.nombreConsecutivoRegistroFaltanteSobrante, institucion, consecutivo.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
				detFaltanteSobrante.setConsecutivo(consecutivo);

				HashSet<DetFaltanteSobrante> setDetFaltanteSobrante = new HashSet<DetFaltanteSobrante>();
				setDetFaltanteSobrante.add(detFaltanteSobrante);
				docSopMovimCajas.setDetFaltanteSobrantes(setDetFaltanteSobrante);
				setDetFaltanteSobranteTotal.add(detFaltanteSobrante);
			}
			setDocSopMovimCajasTotal.add(docSopMovimCajas);
		}

		// Si existen detalles de faltantes sobrante, los relaciono con el faltante sobrante
		if(!setDetFaltanteSobranteTotal.isEmpty())
		{
			for (DetFaltanteSobrante detFaltanteSobranteDefinitivo : setDetFaltanteSobranteTotal) 
			{
				detFaltanteSobranteDefinitivo.setFaltanteSobrante(faltanteSobrante);
			}
			
			faltanteSobrante.setDetFaltanteSobrantes(setDetFaltanteSobranteTotal);
		}
		
		movimientosCaja.setDocSopMovimCajases(setDocSopMovimCajasTotal);
		return movimientosCaja;
	}
	
	
	
	/**
	 * Crea un movimiento con toda la informaci&oacute;n necesaria para registrar un movimiento de caja
	 * Cierre Turno de Caja ( incluye la solicitud de traslado a caja y la Entrega a Caja Mayor / Principal
	 * 
	 * @param dtoInformacionEntrega
	 * @return
	 */
	private static MovimientosCaja construirCierreTurnoCaja(DtoInformacionEntrega dtoInformacionEntrega) {
	
		MovimientosCaja movimientosCaja;
		movimientosCaja = dtoInformacionEntrega.getMovimientosCaja();
		
		movimientosCaja.setFecha(UtilidadFecha.getFechaActualTipoBD());
		movimientosCaja.setHora(UtilidadFecha.getHoraActual());
	
		TurnoDeCajaHome turnoDeCajaHome = new TurnoDeCajaHome();
		turnoDeCajaHome.attachDirty(movimientosCaja.getTurnoDeCaja());
	
		movimientosCaja.getTurnoDeCaja().setEstado(ConstantesIntegridadDominio.acronimoEstadoAperturaDeCajaCerrado);

		String observacionesCierre = dtoInformacionEntrega.getObservacionesAceptacion();
		
		if(dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes().size()==0){

			if(!"".equals(observacionesCierre.trim())){
				
				observacionesCierre+=". ";
			}
			
			observacionesCierre+= "Cierre de turno sin movimientos relacionados";
		}
		
		movimientosCaja.setObservaciones(observacionesCierre);
		
		movimientosCaja.setIngresaFechaArqueo(ConstantesBD.acronimoSiChar);
		
		DetalleMovCajaCierre detalleMovCajaCierre = new DetalleMovCajaCierre();
		detalleMovCajaCierre.setCajas(dtoInformacionEntrega.getCajaMayorPrincipal());
		detalleMovCajaCierre.setMovimientosCaja(movimientosCaja);
		
		movimientosCaja.setDetalleMovCajaCierre(detalleMovCajaCierre);
		
		if(dtoInformacionEntrega.isRegistroSolicitudTrasladoCaja()){
			
			inicializarSolicitudTrasladoCaja(dtoInformacionEntrega, movimientosCaja);
			
			/*
			 *  Al movimiento de Solicitud de Traslado a Caja se le asocian todos los documentos de soporte 
			 *  marcados como recibidos en el detalle de documentos por formas de pago a entregar
			 *  y que corresponden a formas de pago que pueden enviarse en una Solicitud de traslado
			 *  
			*/
			crearSetDocSopMovimCajas(dtoInformacionEntrega, movimientosCaja.getSolicitudTrasladoCajaByCodigoPk().getMovimientosCajaByCodigoPk(), ETipoMovimiento.SOLICITUD_TRASLADO);
		}
			

		// Creacion de la informacion para realizar la entrega a caja mayor principal
		BigDecimal consecutivoEntrega=new BigDecimal(Utilidades.convertirADouble(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoEntregaCajaMayor, movimientosCaja.getUsuarios().getInstituciones().getCodigo())));

		MovimientosCaja movimientosCajaEntrega =  new MovimientosCaja();
		
		movimientosCajaEntrega.setConsecutivo(consecutivoEntrega.longValue());
		movimientosCajaEntrega.setTurnoDeCaja(movimientosCaja.getTurnoDeCaja());
		movimientosCajaEntrega.setUsuarios(movimientosCaja.getUsuarios());
		
		/*
		 *  Al movimiento de Entrega a Caja Mayor / Principal se le asocian todos los documentos de soporte 
		 *  marcados como recibidos en el detalle de documentos por formas de pago a entregar
		 *  y que corresponden a formas de pago que pueden ser entregadas a Caja Mayor / Principal
		 *  
		*/
		HashSet<DocSopMovimCajas> docSopMovimCajasesCajaMayor = crearSetDocSopMovimCajas(dtoInformacionEntrega, movimientosCajaEntrega , ETipoMovimiento.ENTREGA_CAJA_MAYOR_PRINCIPAL);
	
		if(docSopMovimCajasesCajaMayor!=null && docSopMovimCajasesCajaMayor.size()>0){

			inicializarEntregaCajaMayorPrincipal(dtoInformacionEntrega.getCajaMayorPrincipal(), movimientosCaja, movimientosCajaEntrega);
		}

		/*
		 *  Al movimiento de Cierre de Turno de Caja se le asocian todos los documentos de soporte 
		 *  marcados como no recibidos en el detalle de documentos por formas de pago a entregar
		 *  
		*/
		
		crearSetDocSopMovimCajas(dtoInformacionEntrega, movimientosCaja, ETipoMovimiento.CIERRE_CAJA);
		
		return movimientosCaja;
	}

	/**
	 * M&eacute;todo utilizado para inicializar la Solicitud de Traslado a Caja de Recaudo.
	 * Realizando todas las asociaciones necesarias para realizar su persistencia.
	 * 
	 * @param dtoInformacionEntrega
	 * @param movimientosCaja
	 */
	private static void inicializarSolicitudTrasladoCaja(DtoInformacionEntrega dtoInformacionEntrega, MovimientosCaja movimientosCaja) {
		
		ITiposMovimientoCajaMundo tiposMovimientoCajaMundo = TesoreriaFabricaMundo.crearTiposMovimientoCajaMundo();
		IUsuariosMundo  usuariosMundo = TesoreriaFabricaMundo.crearUsuariosMundo();
		
		movimientosCaja.getSolicitudTrasladoCajaByCodigoPk().getMovimientosCajaByCodigoPk().setFecha(UtilidadFecha.getFechaActualTipoBD());
		movimientosCaja.getSolicitudTrasladoCajaByCodigoPk().getMovimientosCajaByCodigoPk().setHora(UtilidadFecha.getHoraActual());
		movimientosCaja.getSolicitudTrasladoCajaByCodigoPk().getMovimientosCajaByCodigoPk().setFechaMovimiento(movimientosCaja.getFechaMovimiento());
		movimientosCaja.getSolicitudTrasladoCajaByCodigoPk().getMovimientosCajaByCodigoPk().setTiposMovimientoCaja(tiposMovimientoCajaMundo.findById(ConstantesBD.codigoTipoMovimientoSolicitudTraslado));
		movimientosCaja.getSolicitudTrasladoCajaByCodigoPk().getMovimientosCajaByCodigoPk().setUsuarios(movimientosCaja.getUsuarios());
		movimientosCaja.getSolicitudTrasladoCajaByCodigoPk().getMovimientosCajaByCodigoPk().setTurnoDeCaja(movimientosCaja.getTurnoDeCaja());
		movimientosCaja.getSolicitudTrasladoCajaByCodigoPk().getMovimientosCajaByCodigoPk().setContabilizado('N');
		movimientosCaja.getSolicitudTrasladoCajaByCodigoPk().getMovimientosCajaByCodigoPk().setIngresaFechaArqueo(ConstantesBD.acronimoSiChar);
		movimientosCaja.getSolicitudTrasladoCajaByCodigoPk().getMovimientosCajaByCodigoPk().setObservaciones(dtoInformacionEntrega.getObservaciones());
		
		movimientosCaja.getSolicitudTrasladoCajaByCodigoPk().setEstado(ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaSolicitado);
		movimientosCaja.getSolicitudTrasladoCajaByCodigoPk().setMovimientosCajaByMovimientoCajaCierre(movimientosCaja);
		movimientosCaja.getSolicitudTrasladoCajaByCodigoPk().setCajas(dtoInformacionEntrega.getCajaRecaudo());

		if(!dtoInformacionEntrega.getLoginTestigo().equals(ConstantesBD.codigoNuncaValido+"")){
			
			Usuarios testigo = usuariosMundo.buscarPorLogin(dtoInformacionEntrega.getLoginTestigo());
			movimientosCaja.getSolicitudTrasladoCajaByCodigoPk().setUsuarios(testigo);
		}
	}


	/**
	 * M&eacute;todo utilizado para inicializar la Entrega a Caja Mayor Principal. Realizando todas
	 * las asociaciones necesarias para realizar su persistencia.
	 * 
	 * @param cajaMayorPrincipal
	 * @param movimientosCaja
	 * @param movimientosCajaEntrega
	 */
	private static void inicializarEntregaCajaMayorPrincipal(Cajas cajaMayorPrincipal, MovimientosCaja movimientosCaja, MovimientosCaja movimientosCajaEntrega) {
		
		EntregaCajaMayor entregaCajaMayor = new EntregaCajaMayor();
		
		ITiposMovimientoCajaMundo tiposMovimientoCajaMundo = TesoreriaFabricaMundo.crearTiposMovimientoCajaMundo();
		
		movimientosCajaEntrega.setFecha(UtilidadFecha.getFechaActualTipoBD());
		movimientosCajaEntrega.setHora(UtilidadFecha.getHoraActual());
		movimientosCajaEntrega.setFechaMovimiento(movimientosCaja.getFechaMovimiento());
		movimientosCajaEntrega.setTiposMovimientoCaja(tiposMovimientoCajaMundo.findById(ConstantesBD.codigoTipoMovimientoEntregaCajaPrincipalMayor));
		movimientosCajaEntrega.setContabilizado('N');
		movimientosCajaEntrega.setIngresaFechaArqueo(ConstantesBD.acronimoSiChar);
		movimientosCajaEntrega.setObservaciones("");
		
		entregaCajaMayor.setCajas(cajaMayorPrincipal);
		entregaCajaMayor.setEstado(ConstantesIntegridadDominio.acronimoEstadoEntregado);
		entregaCajaMayor.setMovimientosCajaByMovimientoCajaArqueo(movimientosCaja);
		entregaCajaMayor.setMovimientosCajaByCodigoPk(movimientosCajaEntrega);
		
		movimientosCaja.setEntregaCajaMayorByCodigoPk(entregaCajaMayor);

	}

	/**
	 * M&eacute;todo que retorna el HashSet <{@link DocSopMovimCajas}> con toda la informaci&oacute;n de 
	 * los documentos de soporte que van a ser asociados a una Solicitud de Traslado a Caja, o a una Entrega a caja Mayor 
	 * o a un Cierre de Caja en el proceso del Cierre de Turno de Caja.
	 * 
	 * La Entrega a Caja Mayor / Principal solo genera registro de faltante / sobrante en el caso del Cierre de
	 * Turno de Caja, por lo cual una Entrega a Caja Mayor / Principal en un movimiento Arqueo Entrega Parcial no podr&aacute; utilizar
	 * este m&eacute;todo para asociar los documentos de soporte al movimiento respectivo.
	 * 
	 * @param dtoInformacionEntrega
	 * @param movimientosCaja
	 * @param eTipoMovimiento
	 * @return
	 */
	private static HashSet<DocSopMovimCajas> crearSetDocSopMovimCajas (DtoInformacionEntrega dtoInformacionEntrega, MovimientosCaja movimientosCaja, ETipoMovimiento eTipoMovimiento){
		
		IDetallePagosRcMundo detallePagosRcMundo = TesoreriaFabricaMundo.crearDetallePagosRcMundo();
		
		IMovimientosCajaMundo movimientosCajaMundo 	= TesoreriaFabricaMundo.crearMovimientosCajaMundo(); 
		
		HashSet<DocSopMovimCajas> setDocSopMovimCajas = new HashSet<DocSopMovimCajas>();
		HashSet<DetFaltanteSobrante> setDetFaltanteSobrante = new HashSet<DetFaltanteSobrante>();
		
		FaltanteSobrante faltanteSobrante = new FaltanteSobrante();
		
		Usuarios responsable = movimientosCaja.getTurnoDeCaja().getUsuarios();
		
		faltanteSobrante.setUsuarios(movimientosCaja.getUsuarios());
		faltanteSobrante.setFechaModifica(UtilidadFecha.getFechaActualTipoBD());
		faltanteSobrante.setHoraModifica(UtilidadFecha.getHoraActual());
		faltanteSobrante.setEstado(ConstantesIntegridadDominio.acronimoEstadoFaltanteSobranteGenerado);
		
		/*
		 * Solo se inhabilita la restricci&oacute;n cuando es un movimiento Cierre Turno de Caja, ya que a este se asocian 
		 * solo los documentos de soporte que han sido marcados como no recibidos sin importar si se ha realizado una solicitud de 
		 * Traslado a Caja de Recaudo o una Entrega a Caja Mayor / Principal y sin tener en cuenta la forma de pago.
		 */
		
		boolean sinRestriccion = false;
		
		if (eTipoMovimiento == ETipoMovimiento.CIERRE_CAJA) {
			
			sinRestriccion = true;
		}

		for (DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte : dtoInformacionEntrega.getListadoDtoFormaPagoDocSoportes()) {

			FormasPago formaPago = new FormasPago();
			
			boolean registrar = agregarDocSopMovimCaja(eTipoMovimiento, dtoFormaPagoDocSoporte.getIndicativoTrasladoCajaRecaudo(), 
					dtoInformacionEntrega.isRegistroSolicitudTrasladoCaja(), dtoFormaPagoDocSoporte.getRegistrarTraslado());
			
			if(registrar){

				for (DtoEntidadesFinancieras entidadesFinancieras : dtoFormaPagoDocSoporte.getListadoDtoEntidadesFinancieras()) {
					
					if(dtoFormaPagoDocSoporte.getCodigoTipoDetalleFormaPago() == ConstantesBD.codigoTipoDetalleFormasPagoNinguno) {
						
						Log4JManager.info("-----------------------------------------------  forma de pago: " + dtoFormaPagoDocSoporte.getFormaPago());
						
						
						/* Para un movimiento de Cierre de Turno de Caja, solo se registra la forma de pago de tipo efectivo en el
						 * caso que el valor registrado en la caja no supera la base. Por esto, el registro del faltante queda 
						 * registrado en el movimiento de cierre. 
						 */
						double valorRegistroFormaTipoNinguno = dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorCaja();
						
						/*
						 * Cuando se est&aacute; realizando un registro de Entrega a Caja Mayor / Principal, es necesario que el valor
						 * registrado en caja supere el valor de la base, igualmente cuando se est&aacute; realizando una 
						 * Solicitud de Traslado a Caja de Recaudo. Esto solo aplica para el proceso de Cierre de Turno de Caja.
						 * Por esto se debe restar al valor registrado para la forma de pago Tipo NINGUNO,
						 * el valor de la base ya que si el valor resultante no lo supera no es posible hacer el registro.
						 * 
						 * El Valor en Base es diferente de 0, solo para la forma de pago Tipo Ninguno definida según el parámetro general
						 * como forma de pago efectivo.
						 */
						if(eTipoMovimiento == ETipoMovimiento.SOLICITUD_TRASLADO || eTipoMovimiento == ETipoMovimiento.ENTREGA_CAJA_MAYOR_PRINCIPAL
								|| dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorBaseEnCaja().equals(dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorSistema())){
							
							valorRegistroFormaTipoNinguno -= dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorBaseEnCaja();
						}
		
						if(sinRestriccion || valorRegistroFormaTipoNinguno > 0){
							
							//HashSet<DetallePagosRc> detallePagosRcs = new HashSet<DetallePagosRc>();
							
							for (DtoDetalleDocSopor dtoDetalleDocSopor : entidadesFinancieras.getListadoDtoDetDocSoporte()) {
								
								if(dtoDetalleDocSopor.getIndicativoRegistroMovimiento()==ConstantesBD.codigoNuncaValido){
									
									//HashSet<DocSopMovimCajas> docSopMovimCajasesTemp = new HashSet<DocSopMovimCajas>();
									
									//DetallePagosRc detallePagosRc = detallePagosRcMundo.findById(dtoDetalleDocSopor.getDetallePagosId());
									
									//detallePagosRc.setConsecutivo(dtoDetalleDocSopor.getDetallePagosId());
								
									//docSopMovimCajasesTemp.add(docSopMovimCajas);
									
									//detallePagosRc.setDocSopMovimCajases(docSopMovimCajasesTemp);
								
									//detallePagosRcs.add(detallePagosRc);
									
									DocSopMovimCajas docSopMovimCajas = new DocSopMovimCajas();
									
									formaPago.setConsecutivo(dtoFormaPagoDocSoporte.getConsecutivoFormaPago());							
									formaPago.setDescripcion(dtoFormaPagoDocSoporte.getFormaPago());
									docSopMovimCajas.setFormasPago(formaPago);
									docSopMovimCajas.setMovimientosCaja(movimientosCaja);
									docSopMovimCajas.setValor(new BigDecimal(valorRegistroFormaTipoNinguno));
									docSopMovimCajas.setCodigoPk(0);
									
									if(dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorDiferencia()!=0){
										
										double valorSistema = dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorSistema();

										String tipodiferencia 	= movimientosCajaMundo.obtenerTipoDiferencia(valorSistema, valorRegistroFormaTipoNinguno);

										if(!UtilidadTexto.isEmpty(tipodiferencia))
										{
											DetFaltanteSobrante detFaltanteSobrante;
			
											detFaltanteSobrante = crearDetFaltanteSobrante(docSopMovimCajas, movimientosCaja.getUsuarios().getInstituciones().getCodigo(), responsable);
											
											detFaltanteSobrante.setFaltanteSobrante(faltanteSobrante);
											detFaltanteSobrante.setValorDiferencia(new BigDecimal(dtoFormaPagoDocSoporte.getDtoCuadreCaja().getValorDiferencia())); //new BigDecimal(valorRegistroFormaTipoNinguno - valorSistema)
											detFaltanteSobrante.setTipoDiferencia(tipodiferencia);
											
											setDetFaltanteSobrante.add(detFaltanteSobrante);
										}
									}
								
									dtoDetalleDocSopor.setIndicativoRegistroMovimiento(1);
									
									setDocSopMovimCajas.add(docSopMovimCajas);
									
									break;
								}
							}
						}
					}else{
	
						for(DtoDetalleDocSopor dtoDetalleDocSopor : entidadesFinancieras.getListadoDtoDetDocSoporte()){
							
							if(dtoDetalleDocSopor.getIndicativoRegistroMovimiento()==ConstantesBD.codigoNuncaValido){

								if(sinRestriccion || dtoDetalleDocSopor.getIndicativoNoRecibido().equals(ConstantesBD.acronimoNo)){
									
									if(dtoDetalleDocSopor.getValorActual()!=null){

										HashSet<DetallePagosRc> detallePagosRcs = new HashSet<DetallePagosRc>();
									
										HashSet<DocSopMovimCajas> docSopMovimCajasesAux = new HashSet<DocSopMovimCajas>();
										
										DocSopMovimCajas docSopMovimCajas = new DocSopMovimCajas();
										docSopMovimCajas.setMovimientosCaja(movimientosCaja);
										formaPago.setConsecutivo(dtoDetalleDocSopor.getConsecutivoFormaPago());							
										formaPago.setDescripcion(dtoFormaPagoDocSoporte.getFormaPago());
										docSopMovimCajas.setFormasPago(formaPago);
										docSopMovimCajas.setCodigoPk(0);
										
										docSopMovimCajas.setNroDocumento(dtoDetalleDocSopor.getNroDocumentoRecibido());
										docSopMovimCajas.setValor(dtoDetalleDocSopor.getValorActual());
						
										DetallePagosRc detallePagosRc = detallePagosRcMundo.findById(dtoDetalleDocSopor.getDetallePagosId());
									
										docSopMovimCajasesAux.add(docSopMovimCajas);
										detallePagosRc.setDocSopMovimCajases(docSopMovimCajasesAux);
										detallePagosRcs.add(detallePagosRc);
										
										docSopMovimCajas.setDetallePagosRcs(detallePagosRcs);

										if(dtoDetalleDocSopor.getTipodiferencia() != null)
										{
											DetFaltanteSobrante detFaltanteSobrante;

											detFaltanteSobrante = crearDetFaltanteSobrante(docSopMovimCajas, movimientosCaja.getUsuarios().getInstituciones().getCodigo(), responsable);
											
											detFaltanteSobrante.setValorDiferencia(dtoDetalleDocSopor.getValorDiferencia());
											detFaltanteSobrante.setTipoDiferencia(dtoDetalleDocSopor.getTipodiferencia());
											
											detFaltanteSobrante.setFaltanteSobrante(faltanteSobrante);
											detFaltanteSobrante.setRecibido(dtoDetalleDocSopor.getIndicativoNoRecibido().charAt(0));

											setDetFaltanteSobrante.add(detFaltanteSobrante);
										}

										setDocSopMovimCajas.add(docSopMovimCajas);
										dtoDetalleDocSopor.setIndicativoRegistroMovimiento(1);
									}
								}
							}
						}
					}
				}
			}
		}

		// Si existen detalles de faltantes sobrante, se relacionan con el faltante sobrante
		if(!setDetFaltanteSobrante.isEmpty()){
			
			for (DetFaltanteSobrante detFaltanteSobranteDefinitivo : setDetFaltanteSobrante) 
			{
				detFaltanteSobranteDefinitivo.setFaltanteSobrante(faltanteSobrante);
			}
			
			faltanteSobrante.setDetFaltanteSobrantes(setDetFaltanteSobrante);
		}
		
		if(setDocSopMovimCajas!=null && setDocSopMovimCajas.size()>0){
			
			movimientosCaja.setDocSopMovimCajases(setDocSopMovimCajas);
		}

		return setDocSopMovimCajas;
	}


	/**
	 * M&eacute;todo que determina si se puede asociar el {@link DocSopMovimCajas} al movimiento de caja
	 * que realiza el proceso.
	 * 
	 * @param eTipoMovimiento
	 * @param indicativoTrasladoCajaRecaudo
	 * @param registroSolicitudTrasladoCaja
	 * @param registrarTraslado 
	 * @return
	 */
	private static boolean  agregarDocSopMovimCaja(ETipoMovimiento eTipoMovimiento, String indicativoTrasladoCajaRecaudo, boolean registroSolicitudTrasladoCaja, String registrarTraslado) {
		
		boolean registrar  = false;
		
		if(eTipoMovimiento.equals(ETipoMovimiento.SOLICITUD_TRASLADO)){
			
			if(registroSolicitudTrasladoCaja){
				
				if(indicativoTrasladoCajaRecaudo.equals(ConstantesBD.acronimoSi)
						&& registrarTraslado.equals(ConstantesBD.acronimoSi)){

					registrar = true;
				}
			}
		}else if(eTipoMovimiento.equals(ETipoMovimiento.ENTREGA_CAJA_MAYOR_PRINCIPAL)){
			
			if(registroSolicitudTrasladoCaja){
				
				if(indicativoTrasladoCajaRecaudo.equals(ConstantesBD.acronimoNo) && 
						registrarTraslado.equals(ConstantesBD.acronimoNo)){

					registrar = true;
				}
			}else{
		
				registrar = true;
			}
			
		}else if(eTipoMovimiento.equals(ETipoMovimiento.ENTREGA_TRANSPORTADORA_VALORES) || eTipoMovimiento.equals(ETipoMovimiento.CIERRE_CAJA)){
			
			registrar = true;
		
		}
		
		return registrar;
	}
	
	
	
	/**
	 * M&eacute;todo que crea el {@link DetFaltanteSobrante} respectivo de acuerdo a la informaci&oacute;n
	 * pasada como par&aacute;metro
	 * 
	 * @param docSopMovimCajas
	 * @param codigoInstitucion
	 * @param responsable
	 * @return
	 */
	private static DetFaltanteSobrante crearDetFaltanteSobrante (DocSopMovimCajas docSopMovimCajas, int codigoInstitucion, Usuarios responsable){
		
		DetFaltanteSobrante detFaltanteSobrante =  new DetFaltanteSobrante();
		
		detFaltanteSobrante.setDocSopMovimCajas(docSopMovimCajas);
		
		detFaltanteSobrante.setContabilizado(ConstantesBD.acronimoNo.charAt(0));
		detFaltanteSobrante.setUsuarios(responsable);
	
		BigDecimal consecutivo = new BigDecimal(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoRegistroFaltanteSobrante,  codigoInstitucion));
		Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
		UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH, ConstantesBD.nombreConsecutivoRegistroFaltanteSobrante, codigoInstitucion, consecutivo.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		detFaltanteSobrante.setConsecutivo(consecutivo);
		
		HashSet<DetFaltanteSobrante> setDetFaltanteSobrante = new HashSet<DetFaltanteSobrante>();
		setDetFaltanteSobrante.add(detFaltanteSobrante);
		
		docSopMovimCajas.setDetFaltanteSobrantes(setDetFaltanteSobrante);

		return detFaltanteSobrante;
	}
	
	
}
