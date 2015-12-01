
package com.servinte.axioma.mundo.impl.tesoreria;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

import com.princetonsa.dto.tesoreria.DtoBusquedaCierreArqueo;
import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoCuadreCaja;
import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;
import com.princetonsa.dto.tesoreria.DtoFaltanteSobrante;
import com.princetonsa.dto.tesoreria.DtoFormaPagoDocSoporte;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaArqueoCierreCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ICuadreCajaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IDocSopMovimCajasMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IEntidadesFinancierasMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaCajaMayorMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IEntregaTransportadoraValoresMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IFormasPagoMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo;
import com.servinte.axioma.orm.CuadreCaja;
import com.servinte.axioma.orm.DetFaltanteSobrante;
import com.servinte.axioma.orm.DocSopMovimCajas;
import com.servinte.axioma.orm.EntregaCajaMayor;
import com.servinte.axioma.orm.EntregaTransportadora;
import com.servinte.axioma.orm.FormasPago;
import com.servinte.axioma.orm.MovimientosCaja;

/**
 * Clase encargada de centralizar los procesos de consultas de Arqueos de caja, que son:
 * 
 * Arqueo Caja - 226
 * Arqueo Entrega Parcial - 227
 * Cierre Turno de Caja - 228
 * 
 * Consulta:
 * 
 * Arqueo Entrega Parcial - Entrega Transportadora de Valores
 * 	Anexo Formato Impresi&oacute;n Entrega a Transportadora - 1025
 * 
 * Arqueo Entrega Parcial - Entrega Caja Principal / Mayor
 * 	Anexo Formato Impresi&oacute;n Entrega Caja Mayor/ Principal - 1039
 * 
 * Cierre Turno Caja. Solicitud de Traslado a Caja
 * 	Traslado entre caja - 1024
 * 
 * Cierre Turno Caja. Cierre Turno de Caja
 * 	Anexo Formato Impresi&oacute;n Cierre Turno Caja - 1038
 * 	Apertura Turno Caja	(Consulta Consolidado Cierre Turno Caja	- 1032) Para caja de cierre - 846
 *  Aceptaci&oacute;n Traslado Caja	(Consulta Consolidado Cierre Turno Caja	- 1032) Para caja de solicitud -  929 
 *  
 * 
 * @autor Jorge Armando Agudelo Quintero
 *
 */
public class ConsultaArqueoCierreCajaMundo implements IConsultaArqueoCierreCajaMundo{

	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaArqueoCierreCajaMundo#consultarCierreTurnoCaja(com.servinte.axioma.orm.MovimientosCaja)
	 */
	public DtoInformacionEntrega consultarCierreTurnoCaja(MovimientosCaja movimientoCierreTurno)
	{
		IMovimientosCajaMundo movimientosCajaMundo = TesoreriaFabricaMundo.crearMovimientosCajaMundo();
	
		DtoInformacionEntrega dtoInformacionEntrega = new DtoInformacionEntrega(movimientosCajaMundo.obtenerConsolidadoMovimiento(movimientoCierreTurno));
		
		dtoInformacionEntrega.setResponsable(cargarResponsableProceso(movimientoCierreTurno));
		dtoInformacionEntrega.setObservacionesAceptacion(movimientoCierreTurno.getObservaciones());
		return consultarCuadreCajaMovimiento (movimientoCierreTurno, dtoInformacionEntrega);

	}
	
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaArqueoCierreCajaMundo#consultarArqueoCaja(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public DtoConsolidadoMovimiento consultarArqueoCaja (MovimientosCaja arqueoCaja){
		
		ICuadreCajaMundo cuadreCajaMundo = TesoreriaFabricaMundo.crearCuadreCajaMundo();
		IMovimientosCajaMundo movimientosCajaMundo = TesoreriaFabricaMundo.crearMovimientosCajaMundo();
	
		ArrayList<CuadreCaja> listadoCuadreCaja = (ArrayList<CuadreCaja>) cuadreCajaMundo.consultarCuadreCajaPorMovimiento(arqueoCaja);
		
		DtoConsolidadoMovimiento consolidadoMovimientoDTO = new DtoConsolidadoMovimiento();
		
		if(listadoCuadreCaja!=null && listadoCuadreCaja.size()>0){
			
			if (arqueoCaja.getIngresaFechaArqueo() == ConstantesBD.acronimoNoChar) {
				consolidadoMovimientoDTO = movimientosCajaMundo.obtenerConsolidadoMovimiento(arqueoCaja);
			} else {
				consolidadoMovimientoDTO = movimientosCajaMundo.obtenerConsolidadoMovimientoArqueoCaja(arqueoCaja);
			}
			
			
			ArrayList<DtoCuadreCaja> cuadreCajaDTOs = new ArrayList<DtoCuadreCaja>();
			
			for (CuadreCaja cuadreCaja : listadoCuadreCaja) {
				
				DtoCuadreCaja dtoCuadreCaja = new DtoCuadreCaja();
				
				dtoCuadreCaja.setCodigo(cuadreCaja.getCodigoPk());
				dtoCuadreCaja.setValorCaja(cuadreCaja.getValorcaja().doubleValue());
				dtoCuadreCaja.setValorSistema(cuadreCaja.getValorsistema().doubleValue());
				dtoCuadreCaja.setValorDiferencia(cuadreCaja.getValordiferencia().doubleValue());
				dtoCuadreCaja.setFormaPago(cuadreCaja.getFormasPago().getDescripcion());
				dtoCuadreCaja.setTipoFormaPago(cuadreCaja.getFormasPago().getConsecutivo());
				dtoCuadreCaja.setTipoDetalleFormaPago(cuadreCaja.getFormasPago().getTiposDetalleFormaPago().getCodigo());
				
				cuadreCajaDTOs.add(dtoCuadreCaja);
			}
			
			consolidadoMovimientoDTO.setCuadreCajaDTOs(cuadreCajaDTOs);
		}
		
		return consolidadoMovimientoDTO;
	}

	/**
	 * M&eacute;todo que se encarga de realizar las consultas de informaci&oacute;n que son comunes a todos los movimientos de Caja
	 * tales como los recibos de caja, las devoluciones, las entregas, entre otros, que estan asociados al movimiento espec&iacute;fico.
	 * @param movimientoCaja
	 * @param dtoConsultaCierreTurnoCaja
	 * @return
	 */
	private DtoInformacionEntrega consultarCuadreCajaMovimiento (MovimientosCaja movimientoCaja, DtoInformacionEntrega dtoInformacionEntrega)
	{
		IDocSopMovimCajasMundo docSopMovimCajasMundo 			= TesoreriaFabricaMundo.crearDocSopMovimCajasMundo();
		
		List<DocSopMovimCajas> listaDocSopMovimCajas = new ArrayList<DocSopMovimCajas>();
		
		
		listaDocSopMovimCajas.addAll(docSopMovimCajasMundo.obtenerDocSoportePorMovimiento(movimientoCaja.getCodigoPk()));
		
		if(movimientoCaja.getEntregaCajaMayorByCodigoPk()!=null){
			
			listaDocSopMovimCajas.addAll(docSopMovimCajasMundo.obtenerDocSoportePorMovimiento(movimientoCaja.getEntregaCajaMayorByCodigoPk().getMovimientoCaja()));
		}
		
		if(movimientoCaja.getSolicitudTrasladoCajaByCodigoPk()!=null){
			
			listaDocSopMovimCajas.addAll(docSopMovimCajasMundo.obtenerDocSoportePorMovimiento(movimientoCaja.getSolicitudTrasladoCajaByCodigoPk().getMovimientoCaja()));
		}
		
		return totalizarCuadreCajaMovimiento (listaDocSopMovimCajas, dtoInformacionEntrega);
	}


	/**
	 * M&eacute;todo que carga la informaci&oacute;n del responsable del proceso de Cierre de Turno de Caja.
	 * 
	 * @param movimientoCaja
	 * @return {@link DtoUsuarioPersona} con la informaci&oacute;n del responsable del proceso de Cierre de Turno de Caja.
	 */ 
	private DtoUsuarioPersona cargarResponsableProceso (MovimientosCaja movimientoCaja){
		
		DtoUsuarioPersona responsable = new DtoUsuarioPersona();
		responsable.setLogin(movimientoCaja.getTurnoDeCaja().getUsuarios().getLogin());
		responsable.setNombre(movimientoCaja.getTurnoDeCaja().getUsuarios().getPersonas().getPrimerNombre() + " " + movimientoCaja.getTurnoDeCaja().getUsuarios().getPersonas().getSegundoNombre());
		responsable.setApellido(movimientoCaja.getTurnoDeCaja().getUsuarios().getPersonas().getPrimerApellido() + " " + movimientoCaja.getTurnoDeCaja().getUsuarios().getPersonas().getSegundoApellido());
		
		return responsable;
	}

	/**
	 * M&eacute;todo que se encarga de calcular el total de los valores entregados
	 * por forma de pago en el cuadre de caja asociado al movimiento.
	 * 
	 * Si existen registros de Faltante / Sobrante, se calcula el total de la misma forma que
	 * para el cuadre de caja.
	 * 
	 * @param listaDocSopMovimCajas
	 * @param dtoInformacionEntrega 
	 */
	@SuppressWarnings("unchecked")
	private DtoInformacionEntrega totalizarCuadreCajaMovimiento(List<DocSopMovimCajas> listaDocSopMovimCajas, DtoInformacionEntrega dtoInformacionEntrega) {
	
		IFormasPagoMundo formasPagoMundo = TesoreriaFabricaMundo.crearFormasPagoMundo();
		List<FormasPago> formasPago = formasPagoMundo.obtenerFormasPagos();
		
		ArrayList<DtoCuadreCaja> listaDtoCuadreCaja =  new ArrayList<DtoCuadreCaja>();
		ArrayList<DtoFaltanteSobrante> listaDtoFaltanteSobrantes = new ArrayList<DtoFaltanteSobrante>();
		
		double valorRegistrado;
		double valorDiferencia;
		
		long consecutivoFaltante=0;
		
		for (FormasPago formaPago : formasPago) {
			
			valorDiferencia = 0;
			valorRegistrado = 0;
			
			for (DocSopMovimCajas docSopMovimCajas : listaDocSopMovimCajas) {
				
				if(formaPago.getCodigo() == docSopMovimCajas.getFormasPago().getCodigo()){
					
					/*
					 *  Total del valor registrado en el movimiento para la forma de pago espec&iacute;fica.
					 */
					
					if (dtoInformacionEntrega.getValorBase()!= 0 && Integer.parseInt(formaPago.getCodigo()) ==  ConstantesBD.codigoTipoDetalleFormasPagoNinguno)
						valorRegistrado+=docSopMovimCajas.getValor().doubleValue()+ dtoInformacionEntrega.getValorBase();
					else 					
						valorRegistrado+= docSopMovimCajas.getValor().doubleValue();
					
					/*
					 *  Total del faltante / sobrante para la forma de pago espec&iacute;fica.
					 */
					
					Set<DetFaltanteSobrante> set = docSopMovimCajas.getDetFaltanteSobrantes();
					
					for (DetFaltanteSobrante detFaltanteSobrante : set) {
						
						if(detFaltanteSobrante.getValorDiferencia()!=null){
							
							valorDiferencia += detFaltanteSobrante.getValorDiferencia().doubleValue();
							consecutivoFaltante=detFaltanteSobrante.getConsecutivo().longValue();
						}
					}
				}	
			}
			
			if(valorDiferencia!=0){
				
				DtoFaltanteSobrante dtoFaltanteSobrante = new DtoFaltanteSobrante();
				
				dtoFaltanteSobrante.setFormaPago(formaPago.getDescripcion());
				dtoFaltanteSobrante.setTipoFormaPago(formaPago.getConsecutivo());
				dtoFaltanteSobrante.setValorDiferencia(new BigDecimal(valorDiferencia));
				dtoFaltanteSobrante.setIdDetalleFaltanteSobrante(consecutivoFaltante);
				
				if(valorDiferencia<0){
					
					dtoFaltanteSobrante.setTipoDiferencia(ConstantesIntegridadDominio.acronimoDiferenciaFaltante);
					
				}else{
					
					dtoFaltanteSobrante.setTipoDiferencia(ConstantesIntegridadDominio.acronimoDiferenciaSobrante);
				}
				
				listaDtoFaltanteSobrantes.add(dtoFaltanteSobrante);
			}

			if(valorRegistrado>0){
				
				DtoCuadreCaja dtoCuadreCaja = new DtoCuadreCaja();
				
				dtoCuadreCaja.setFormaPago(formaPago.getDescripcion());
				dtoCuadreCaja.setTipoFormaPago(formaPago.getConsecutivo());
				dtoCuadreCaja.setTipoDetalleFormaPago(formaPago.getTiposDetalleFormaPago().getCodigo());
				dtoCuadreCaja.setValorCaja(valorRegistrado);
				
				if(valorDiferencia!=0){
					
					dtoCuadreCaja.setValorDiferencia(valorDiferencia);
					
					dtoCuadreCaja.setValorSistema(valorRegistrado + (valorDiferencia * -1));
				
				}else{
					
					dtoCuadreCaja.setValorSistema(valorRegistrado);
				}
				
				listaDtoCuadreCaja.add(dtoCuadreCaja);
			}
		}

		dtoInformacionEntrega.setCuadreCajaDTOs(listaDtoCuadreCaja);
		dtoInformacionEntrega.setFaltanteSobranteDTOs(listaDtoFaltanteSobrantes);
		
		return dtoInformacionEntrega;
	}
	
	

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaArqueoCierreCajaMundo#consultarArqueoParcialPorEntrega(long, com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento, int)
	 */
	@Override
	public DtoInformacionEntrega consultarArqueoParcialPorEntrega (long codigoEntrega, ETipoMovimiento eTipoMovimiento, int codigoInstitucion){
		
		IDocSopMovimCajasMundo docSopMovimCajasMundo = TesoreriaFabricaMundo.crearDocSopMovimCajasMundo();
		
		MovimientosCaja movimientoArqueo = new MovimientosCaja();
		
		DtoInformacionEntrega dtoInformacionEntrega =  new DtoInformacionEntrega();
		
		if(eTipoMovimiento == ETipoMovimiento.ENTREGA_CAJA_MAYOR_PRINCIPAL){
			
			IEntregaCajaMayorMundo entregaCajaMayorMundo = TesoreriaFabricaMundo.crearEntregaCajaMayorMundo();
			
			EntregaCajaMayor entregaCajaMayor = entregaCajaMayorMundo.obtenerEntregaCajaMayorPorCodigo(codigoEntrega);
			
			
			if(entregaCajaMayor!=null){
		//	la lista de documentos soportes se debe adicionar en el dto que se retorna 
				ArrayList<DtoDetalleDocSopor> valoresEntregaRealizada = (ArrayList<DtoDetalleDocSopor>) docSopMovimCajasMundo.obtenerEntrTransYCajaMayorPpalNoFormaPagoNinguna(entregaCajaMayor.getMovimientosCajaByCodigoPk());
				
				valoresEntregaRealizada.addAll(docSopMovimCajasMundo.obtenerTotalesEntregasFormaPagoNinguno(entregaCajaMayor.getMovimientosCajaByCodigoPk()));
				String usuarioGeneraCierre=entregaCajaMayorMundo.obtenerUsuarioArqueo(codigoEntrega);
				
				dtoInformacionEntrega = obtenerDtoInformacionEntregaImpresion(valoresEntregaRealizada, codigoInstitucion);
				dtoInformacionEntrega.setObservaciones(entregaCajaMayor.getMovimientosCajaByCodigoPk().getObservaciones());
				dtoInformacionEntrega.setCajaMayorPrincipal(entregaCajaMayor.getCajas());
				dtoInformacionEntrega.setNombreUsuarioGeneraCierre(usuarioGeneraCierre);
				movimientoArqueo = entregaCajaMayor.getMovimientosCajaByMovimientoCajaArqueo();
				movimientoArqueo.setEntregaCajaMayorByCodigoPk(entregaCajaMayor);
				
				
			}
	
		}else if(eTipoMovimiento == ETipoMovimiento.ENTREGA_TRANSPORTADORA_VALORES){
			
			IEntregaTransportadoraValoresMundo entregaTransportadoraValoresMundo = TesoreriaFabricaMundo.crearEntregaTransportadoraValoresMundo();
			
			EntregaTransportadora entregaTransportadora = entregaTransportadoraValoresMundo.obtenerEntregaTransportadoraPorCodigo(codigoEntrega);
			
			if(entregaTransportadora!=null){
				
				ArrayList<DtoDetalleDocSopor> valoresEntregaRealizada = (ArrayList<DtoDetalleDocSopor>) docSopMovimCajasMundo.obtenerEntrTransYCajaMayorPpalNoFormaPagoNinguna(entregaTransportadora.getMovimientosCajaByCodigoPk());
				valoresEntregaRealizada.addAll(docSopMovimCajasMundo.obtenerTotalesEntregasFormaPagoNinguno(entregaTransportadora.getMovimientosCajaByCodigoPk()));
				
				
				dtoInformacionEntrega = obtenerDtoInformacionEntregaImpresion(valoresEntregaRealizada, codigoInstitucion);
				
				dtoInformacionEntrega.setTransportadoraValores(entregaTransportadora.getTransportadoraValores());
				
				movimientoArqueo = entregaTransportadora.getMovimientosCajaByCodigoPk();
				movimientoArqueo.setEntregaTransportadoraByCodigoPk(entregaTransportadora);
				
			}
		}
		
		if(dtoInformacionEntrega!=null){
			
			dtoInformacionEntrega.setETipoMovimiento(eTipoMovimiento);
			dtoInformacionEntrega.setMovimientosCaja(movimientoArqueo);
		}
		
		return dtoInformacionEntrega;

	}
	
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaArqueoCierreCajaMundo#obtenerDtoInformacionEntregaImpresion(java.util.ArrayList, int)
	 */
	@Override
	public DtoInformacionEntrega obtenerDtoInformacionEntregaImpresion (ArrayList<DtoDetalleDocSopor> listadoDefinitivoDocSop, int codigoInstitucion) {
		
		IFormasPagoMundo formasPagoMundo = TesoreriaFabricaMundo.crearFormasPagoMundo();
		
		IEntidadesFinancierasMundo entidadesFinancierasMundo = TesoreriaFabricaMundo.crearEntidadesFinancierasMundo();
		
		List<FormasPago> formasPagos = formasPagoMundo.obtenerFormasPagos();
		ArrayList<DtoEntidadesFinancieras> listadoCompletoEntidades = (ArrayList<DtoEntidadesFinancieras>) entidadesFinancierasMundo.obtenerEntidadesPorInstitucion(codigoInstitucion, false);
		
		DtoInformacionEntrega dtoInformacionEntrega =  new DtoInformacionEntrega();
		
		double totalValorFormaPago;
		double totalValorParcialEntidad;
		double totalEntregadoTransportadora = 0;
		
		DtoFormaPagoDocSoporte dtoFormaPagoDocSoporte;
		
		for (FormasPago formaPago : formasPagos) {
			
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
				
			}else {
				
				/*
				 * Otras Formas de pago que no tienen asociadas entidades financieras. Tales como formas de pago
				 * con tipo NINGUNO o tipo BONO
				 */
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
		
		dtoInformacionEntrega.setTotalEntregadoTransportadora(totalEntregadoTransportadora);
		
		return dtoInformacionEntrega;
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaArqueoCierreCajaMundo#consultarCierreArqueo(com.princetonsa.dto.tesoreria.DtoBusquedaCierreArqueo)
	 */
	@Override
	public List<MovimientosCaja> consultarCierreArqueo(DtoBusquedaCierreArqueo dtoBusquedaCierreArqueo) {
		
		IMovimientosCajaMundo movimientosCajaMundo = TesoreriaFabricaMundo.crearMovimientosCajaMundo();
		
		return movimientosCajaMundo.consultarCierreArqueo(dtoBusquedaCierreArqueo);
		
	}



	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaArqueoCierreCajaMundo#consultarEntregaAsociadaArqueoParcial(long)
	 */
	@Override
	public MovimientosCaja consultarEntregaAsociadaArqueoParcial(long codigoMovimientoArqueo) {
	
		IMovimientosCajaMundo movimientosCajaMundo = TesoreriaFabricaMundo.crearMovimientosCajaMundo();
		
		return movimientosCajaMundo.consultarEntregaAsociadaArqueoParcial(codigoMovimientoArqueo);
	}
	
}
