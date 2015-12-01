package com.servinte.axioma.bl.facturacion.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;

import com.princetonsa.dto.inventario.DTOEsqTarInventarioContrato;
import com.princetonsa.dto.inventario.DTOEsqTarProcedimientoContrato;
import com.servinte.axioma.bl.facturacion.interfaz.ICatalogoFacturacionMundo;
import com.servinte.axioma.common.ErrorMessage;
import com.servinte.axioma.delegate.facturacion.CatalogoFacturacionDelegate;
import com.servinte.axioma.dto.administracion.CentroCostoDto;
import com.servinte.axioma.dto.capitacion.NivelAutorizacionDto;
import com.servinte.axioma.dto.facturacion.BusquedaMontosCobroDto;
import com.servinte.axioma.dto.facturacion.DetalleServicioDto;
import com.servinte.axioma.dto.facturacion.FiltroBusquedaMontosCobroDto;
import com.servinte.axioma.dto.facturacion.GrupoServicioDto;
import com.servinte.axioma.dto.facturacion.InfoMontoDetalladoMedicamentoInsumoDto;
import com.servinte.axioma.dto.facturacion.InfoMontoDetalladoServicioDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;

/**
 * Clase que implementa los servicios de Negocio correspondientes a los
 * catalogos o parámetricas del modulo de Facturación
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:23:59 p.m.
 */
public class CatalogoFacturacionMundo implements ICatalogoFacturacionMundo{
	
	/**
	 * (non-Javadoc)
	 * @see com.servinte.axioma.bl.facturacion.interfaz.ICatalogoFacturacionMundo#buscarMontosCobro(com.servinte.axioma.dto.facturacion.FiltroBusquedaMontosCobroDto)
	 */
	public List<BusquedaMontosCobroDto> buscarMontosCobro(FiltroBusquedaMontosCobroDto filtroBusquedaMontosCobro,
			boolean requiereTransaccion) throws IPSException {
		
		Date fecha = new Date();
		List<BusquedaMontosCobroDto> listaMontosCobro = new ArrayList<BusquedaMontosCobroDto>();
		SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
		
		try {
		
			fecha = formatoFecha.parse(formatoFecha.format(fecha));
			
			if(filtroBusquedaMontosCobro.isCuentaAbierta() && 
					filtroBusquedaMontosCobro.getFechaAperturaCuenta() != null) {
				fecha = filtroBusquedaMontosCobro.getFechaAperturaCuenta();
			}
			
			filtroBusquedaMontosCobro.setFechaBusqueda(fecha);
			
			if (requiereTransaccion){
				HibernateUtil.beginTransaction();
			}
			CatalogoFacturacionDelegate delegate = new CatalogoFacturacionDelegate();
			listaMontosCobro = delegate.obtenerMontosCobro(filtroBusquedaMontosCobro);
			if (requiereTransaccion){
				HibernateUtil.beginTransaction();
			}
			
		} catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		} catch (ParseException e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_PARSE, e);
		} catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
		return listaMontosCobro;
	}
	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.facturacion.interfaz.ICatalogoFacturacionMundo#obtenerCentrosCostoEntidadSubcontratadaPorOrdenes(int, java.util.List, java.util.List)
	 * @author ricruico
	 */
	@Override
	public List<CentroCostoDto> obtenerCentrosCostoEntidadSubcontratadaPorOrdenes(int centroAtencionPaciente,
									List<OrdenAutorizacionDto> ordenesPorAutorizar,
									List<NivelAutorizacionDto> nivelesAutorizacion) throws IPSException {
		List<CentroCostoDto> centrosCosto=null;
		try{
			HibernateUtil.beginTransaction();
			CatalogoFacturacionDelegate delegate = new CatalogoFacturacionDelegate();
			List<Integer> prioridades = new ArrayList<Integer>();
			//Se obtienen los códigos de las prioridades
			for(NivelAutorizacionDto nivelDto:nivelesAutorizacion){
				prioridades.addAll(nivelDto.getPrioridades());
			}
			//Se evalua si las ordenes son de servicios o de medicamentos
			//para saber de que forma se obtienen los centros de costo el tipo codigoTipoOrdenAmbulatoriaServicios 
			//incluye las peticiones
			if(ordenesPorAutorizar.get(0).getTipoOrden()==ConstantesBD.codigoTipoOrdenAmbulatoriaServicios){
				List<Integer> gruposServicios=new ArrayList<Integer>();
				List<Integer> unidadesConsultaServicios=new ArrayList<Integer>();
				CatalogoFacturacionDelegate catalogoFacturacionDelegate = new CatalogoFacturacionDelegate();
				for(OrdenAutorizacionDto dtoOrden:ordenesPorAutorizar){
					for(ServicioAutorizacionOrdenDto dtoServicio:dtoOrden.getServiciosPorAutorizar()){
						//Se valida si es una peticion de Cirugia o una Orden Ambulatoria
						if(dtoOrden.getClaseOrden()==ConstantesBD.claseOrdenPeticion){
							//Se valida solamente el servicio de cirugia principal
							if(dtoServicio.isCirugiaPrincipal()){
								if(dtoServicio.getAcronimoTipoServicio().equals(ConstantesIntegridadDominio.acronimoTipoServicioConsulta)){
									//Se obtienen las unidades de consulta del servicio
									unidadesConsultaServicios.addAll(catalogoFacturacionDelegate.obtenerUnidadesConsultaServicio(dtoServicio.getCodigo()));
								}
								else{
									gruposServicios.add(dtoServicio.getCodigoGrupoServicio());
								}
							}
						}
						else{
							if(dtoServicio.getAcronimoTipoServicio().equals(ConstantesIntegridadDominio.acronimoTipoServicioConsulta)){
								//Se obtienen las unidades de consulta del servicio
								unidadesConsultaServicios.addAll(catalogoFacturacionDelegate.obtenerUnidadesConsultaServicio(dtoServicio.getCodigo()));
							}
							else{
								gruposServicios.add(dtoServicio.getCodigoGrupoServicio());
							}
						}
					}
				}
				centrosCosto=delegate.obtenerCentrosCostoServicios(ConstantesBD.codigoTipoAreaDirecto, prioridades, centroAtencionPaciente, 
								gruposServicios, unidadesConsultaServicios);
			}
			else{
				centrosCosto=delegate.obtenerCentrosCostoMedicamentosInsumos(ConstantesBD.codigoTipoAreaSubalmacen, prioridades, centroAtencionPaciente);
			}
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return centrosCosto;
	}	

	
	/** (non-Javadoc)
	 * @see com.servinte.axioma.bl.facturacion.interfaz.ICatalogoFacturacionMundo#obtenerDetalleServicioXTarifarioOficial(int, int)
	 * @author ricruico
	 */
	@Override
	public DetalleServicioDto obtenerDetalleServicioXTarifarioOficial(
			int codigoServicio, int codigotarifario) throws IPSException {
		DetalleServicioDto detalleServicio=null;
		try{
			HibernateUtil.beginTransaction();
			CatalogoFacturacionDelegate delegate = new CatalogoFacturacionDelegate();
			detalleServicio=delegate.obtenerDetalleServicioXTarifarioOficial(codigoServicio, codigotarifario);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return detalleServicio;
	}

	/**
	 * Metodo encagado de realizar el calculo del valor a cobrar paciente por medicamentos o insumos
	 * @param montoCobro
	 * @param listaMedicamentosInsumos
	 * @return valor a cobrar paciente
	 * @throws IPSException 
	 */
	public double valorCobrarPacienteMedicamentosInsumos(
			BusquedaMontosCobroDto montoCobro, List<MedicamentoInsumoAutorizacionOrdenDto> listaMedicamentosInsumos) throws IPSException{
		
		InfoMontoDetalladoMedicamentoInsumoDto infoMedicamento = null;
		CatalogoFacturacionDelegate delegate = null;
		Map<Integer, InfoMontoDetalladoMedicamentoInsumoDto> mapaArticulosEspecificos = 
				new HashMap<Integer, InfoMontoDetalladoMedicamentoInsumoDto>();
		Map<Integer, InfoMontoDetalladoMedicamentoInsumoDto> mapaArticulosAgrupados = 
				new HashMap<Integer, InfoMontoDetalladoMedicamentoInsumoDto>();
		Double montoCobrarXRegistro = Double.valueOf(0) ;
		Double valorCobrarPaciente = 0D;
		
		try {
			
			delegate = new CatalogoFacturacionDelegate();
			
			/*Se itera la lista de medicamentos pasada por parametro*/
			for (MedicamentoInsumoAutorizacionOrdenDto medicamentoInsumoAutorizacionOrdenDto : listaMedicamentosInsumos) {
				
				if (medicamentoInsumoAutorizacionOrdenDto.isAutorizado() && 
						medicamentoInsumoAutorizacionOrdenDto.isAutorizar()){

					infoMedicamento = delegate.obtenerMontoDetalladoMedicamentoInsumo(
							montoCobro.getDetalleCodigo() , medicamentoInsumoAutorizacionOrdenDto.getCodigo());
					
					if (infoMedicamento != null) {
						/*Se realiza la agrupacion de los medicamentos o insumos con monto detallado especifico*/
						if (!infoMedicamento.isPorAgrupacion()) {

							if (mapaArticulosEspecificos.containsKey(infoMedicamento.getCodigo())) {

								mapaArticulosEspecificos.get(infoMedicamento.getCodigo()).setSumatoriaArticulos(
										mapaArticulosEspecificos.get(infoMedicamento.getCodigo()).getSumatoriaArticulos() + 1);

							} else {

								InfoMontoDetalladoMedicamentoInsumoDto infoMedicamentoTemp = 
										new InfoMontoDetalladoMedicamentoInsumoDto();

								infoMedicamentoTemp.setCodigo(infoMedicamento.getCodigo());
								infoMedicamentoTemp.setArticulo(infoMedicamento.getArticulo());
								infoMedicamentoTemp.setCantidadArticulos(infoMedicamento.getCantidadArticulos());
								infoMedicamentoTemp.setCantidadMonto(infoMedicamento.getCantidadMonto());
								infoMedicamentoTemp.setValorMonto(infoMedicamento.getValorMonto());
								infoMedicamentoTemp.setSumatoriaArticulos(1);
								
								mapaArticulosEspecificos.put(infoMedicamento.getCodigo(), infoMedicamentoTemp);

								infoMedicamentoTemp = null;
							}

						} else {
							/*Se realiza la agrupacion de los medicamentos o insumos con monto detallado por agrupacion*/
							if (mapaArticulosAgrupados.containsKey(infoMedicamento.getCodigo())) {

								mapaArticulosAgrupados.get(infoMedicamento.getCodigo()).setSumatoriaArticulos(
										mapaArticulosAgrupados.get(infoMedicamento.getCodigo()).getSumatoriaArticulos() + 1);

							} else {

								InfoMontoDetalladoMedicamentoInsumoDto infoMedicamentoTemp = 
										new InfoMontoDetalladoMedicamentoInsumoDto();

								infoMedicamentoTemp.setCodigo(infoMedicamento.getCodigo());
								infoMedicamentoTemp.setClase(infoMedicamento.getClase());
								infoMedicamentoTemp.setGrupo(infoMedicamento.getGrupo());
								infoMedicamentoTemp.setSubGrupo(infoMedicamento.getSubGrupo());
								infoMedicamentoTemp.setNaturaleza(infoMedicamento.getNaturaleza());
								infoMedicamentoTemp.setCantidadArticulos(infoMedicamento.getCantidadArticulos());
								infoMedicamentoTemp.setCantidadMonto(infoMedicamento.getCantidadMonto());
								infoMedicamentoTemp.setValorMonto(infoMedicamento.getValorMonto());
								infoMedicamentoTemp.setSumatoriaArticulos(1);

								mapaArticulosAgrupados.put(infoMedicamento.getCodigo(), infoMedicamentoTemp);

								infoMedicamentoTemp = null;
							}
						}
						
					} else {
						/*Si no existe parametrizacion para el medicamento o insumo se envia el respectivo mensaje de error*/
						medicamentoInsumoAutorizacionOrdenDto.setAutorizado(false);
						String codMedicamento="";
						if (medicamentoInsumoAutorizacionOrdenDto.getCodigoPropietario()==null){
							codMedicamento=medicamentoInsumoAutorizacionOrdenDto.getCodigo()+"";
						}
						ErrorMessage error = new ErrorMessage("errors.autorizacion.noExisteMontoDetalladoServicioMedicamento",
								codMedicamento);
						medicamentoInsumoAutorizacionOrdenDto.setMensajeError(error);
						//Si no se puede autorizar se retorna -1 para mostrar el mensaje respectivo
						//donde se realice la llamada al método
						valorCobrarPaciente = ConstantesBD.codigoNuncaValidoDoubleNegativo;
					}
				} else {
					//Si no se puede autorizar se retorna -1 para mostrar el mensaje respectivo
					//donde se realice la llamada al método
					valorCobrarPaciente = ConstantesBD.codigoNuncaValidoDoubleNegativo;
				}
			}
			
			if (!mapaArticulosEspecificos.isEmpty()) {
				valorCobrarPaciente=0D;
				/*Se realiza el calculo del valor a cobrar paciente*/
				for (Map.Entry<Integer, InfoMontoDetalladoMedicamentoInsumoDto> entry : 
						mapaArticulosEspecificos.entrySet()) {
					
					if (mapaArticulosEspecificos.get(entry.getKey()).getCantidadArticulos() != null && 
							mapaArticulosEspecificos.get(entry.getKey()).getCantidadArticulos() > 0) {
						
						montoCobrarXRegistro = 
								Math.ceil((double)mapaArticulosEspecificos.get(entry.getKey()).getSumatoriaArticulos() / 
										mapaArticulosEspecificos.get(entry.getKey()).getCantidadArticulos().doubleValue()) *
										mapaArticulosEspecificos.get(entry.getKey()).getCantidadMonto().doubleValue();
					} else {
						//Si la cantidad de articulos en el monto no existe o es 0 la cantidad de montoCobrarXRegistro
						//es igual a la cantidad del monto
						montoCobrarXRegistro = mapaArticulosEspecificos.get(entry.getKey()).getCantidadMonto().doubleValue();
					}
					
					valorCobrarPaciente +=  
							montoCobrarXRegistro * mapaArticulosEspecificos.get(entry.getKey()).getValorMonto();
				}
			}
			
			if (!mapaArticulosAgrupados.isEmpty()) {
				/*Se realiza el calculo del valor a cobrar paciente*/
				for (Map.Entry<Integer, InfoMontoDetalladoMedicamentoInsumoDto> entry : 
						mapaArticulosAgrupados.entrySet()) {
					
					if (mapaArticulosAgrupados.get(entry.getKey()).getCantidadArticulos() != null && 
							mapaArticulosAgrupados.get(entry.getKey()).getCantidadArticulos() > 0) {
						
						montoCobrarXRegistro = 
								 Math.ceil((double)mapaArticulosAgrupados.get(entry.getKey()).getSumatoriaArticulos() / 
											mapaArticulosAgrupados.get(entry.getKey()).getCantidadArticulos().doubleValue()) *
											mapaArticulosAgrupados.get(entry.getKey()).getCantidadMonto().doubleValue();
					} else {
						//Si la cantidad de articulos en el monto no existe o es 0 la cantidad de montoCobrarXRegistro
						//es igual a la cantidad del monto
						montoCobrarXRegistro = mapaArticulosAgrupados.get(entry.getKey()).getCantidadMonto().doubleValue();
					}
					
					valorCobrarPaciente += 
							montoCobrarXRegistro * mapaArticulosAgrupados.get(entry.getKey()).getValorMonto();
					
				}
			}
			
			return valorCobrarPaciente.doubleValue();
			
		} catch (IPSException ipse) {
			throw ipse;
		} catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		
	}

	/**
	 * Metodo encagado de realizar el calculo del valor a cobrar paciente por servicios
	 * @param listaServicios
	 * @param montoCobro
	 * @return valor a cobrar paciente
	 * @throws IPSException 
	 */
	public double valorCobrarPacienteServicios(BusquedaMontosCobroDto montoCobro, List<ServicioAutorizacionOrdenDto> listaServicios) throws IPSException {
		InfoMontoDetalladoServicioDto infoServicio = null;
		CatalogoFacturacionDelegate delegate = null;
		Map<Integer, InfoMontoDetalladoServicioDto> mapaServiciosEspecificos = 
				new HashMap<Integer, InfoMontoDetalladoServicioDto>();
		Map<Integer, InfoMontoDetalladoServicioDto> mapaServiciosAgrupados = 
				new HashMap<Integer, InfoMontoDetalladoServicioDto>();
		Double montoCobrarXRegistro = Double.valueOf(0);
		Double valorCobrarPaciente = 0D;
		
		try {
			
			delegate = new CatalogoFacturacionDelegate();
			
			/*Se itera la lista de servicios pasada por parametro*/
			for (ServicioAutorizacionOrdenDto servicioAutorizacionOrdenDto : listaServicios) {
				
				if (servicioAutorizacionOrdenDto.isAutorizado() && servicioAutorizacionOrdenDto.isAutorizar()){
					
					infoServicio = delegate.obtenerMontoDetalladoServicio(
							montoCobro.getDetalleCodigo() , servicioAutorizacionOrdenDto.getCodigo());
					
					if (infoServicio != null) {
						/*Se realiza la agrupacion de los servicios con monto detallado especifico*/
						if (!infoServicio.isPorAgrupacion()) {

							if (mapaServiciosEspecificos.containsKey(infoServicio.getCodigo())) {

								mapaServiciosEspecificos.get(infoServicio.getCodigo()).setSumatoriaServicios(
										mapaServiciosEspecificos.get(infoServicio.getCodigo()).getSumatoriaServicios() + 1);

							} else {

								InfoMontoDetalladoServicioDto infoServicioTemp = 
										new InfoMontoDetalladoServicioDto();

								infoServicioTemp.setCodigo(infoServicio.getCodigo());
								infoServicioTemp.setServicio(infoServicio.getServicio());
								infoServicioTemp.setCantidadServicios(infoServicio.getCantidadServicios());
								infoServicioTemp.setCantidadMonto(infoServicio.getCantidadMonto());
								infoServicioTemp.setValorMonto(infoServicio.getValorMonto());
								infoServicioTemp.setSumatoriaServicios(1);
								
								mapaServiciosEspecificos.put(infoServicio.getCodigo(), infoServicioTemp);

								infoServicioTemp = null;
							}

						} else {
							/*Se realiza la agrupacion de los servicios con monto detallado por agrupacion*/
							if (mapaServiciosAgrupados.containsKey(infoServicio.getCodigo())) {

								mapaServiciosAgrupados.get(infoServicio.getCodigo()).setSumatoriaServicios(
										mapaServiciosAgrupados.get(infoServicio.getCodigo()).getSumatoriaServicios() + 1);

							} else {

								InfoMontoDetalladoServicioDto infoServicioTemp = 
										new InfoMontoDetalladoServicioDto();

								infoServicioTemp.setCodigo(infoServicio.getCodigo());
								infoServicioTemp.setGrupoServicio(infoServicio.getGrupoServicio());
								infoServicioTemp.setTipoServicio(infoServicio.getTipoServicio());
								infoServicioTemp.setEspecialidad(infoServicio.getEspecialidad());
								infoServicioTemp.setCantidadServicios(infoServicio.getCantidadServicios());
								infoServicioTemp.setCantidadMonto(infoServicio.getCantidadMonto());
								infoServicioTemp.setValorMonto(infoServicio.getValorMonto());
								infoServicioTemp.setSumatoriaServicios(1);

								mapaServiciosAgrupados.put(infoServicio.getCodigo(), infoServicioTemp);

								infoServicioTemp = null;
							}
						}
						
					} else {
						/*Si no existe parametrizacion para el servicio se envia el respectivo mensaje de error*/
						servicioAutorizacionOrdenDto.setAutorizado(false);
						ErrorMessage error = new ErrorMessage("errors.autorizacion.noExisteMontoDetalladoServicioMedicamento",
								servicioAutorizacionOrdenDto.getCodigoPropietario());
						servicioAutorizacionOrdenDto.setMensajeError(error);
						//Si no se puede autorizar se retorna -1 para mostrar el mensaje respectivo
						//donde se realice la llamada al método
						valorCobrarPaciente = ConstantesBD.codigoNuncaValidoDoubleNegativo;
					}
				} else {
					//Si no se puede autorizar se retorna -1 para mostrar el mensaje respectivo
					//donde se realice la llamada al método
					valorCobrarPaciente = ConstantesBD.codigoNuncaValidoDoubleNegativo;
				}
			}
			
			if (!mapaServiciosEspecificos.isEmpty()) {
				/*Se realiza el calculo del valor a cobrar paciente*/
				for (Map.Entry<Integer, InfoMontoDetalladoServicioDto>  entry: mapaServiciosEspecificos.entrySet()) {
					
					if (mapaServiciosEspecificos.get(entry.getKey()).getCantidadServicios() != null && 
							mapaServiciosEspecificos.get(entry.getKey()).getCantidadServicios() > 0) {
						
						montoCobrarXRegistro = 
								Math.ceil((double)mapaServiciosEspecificos.get(entry.getKey()).getSumatoriaServicios() / 
										mapaServiciosEspecificos.get(entry.getKey()).getCantidadServicios().doubleValue()) *
										mapaServiciosEspecificos.get(entry.getKey()).getCantidadMonto().doubleValue();
					} else {
						//Si la cantidad de servicios en el monto no existe o es 0 la cantidad de montoCobrarXRegistro
						//es igual a la cantidad del monto
						montoCobrarXRegistro = mapaServiciosEspecificos.get(entry.getKey()).getCantidadMonto().doubleValue();
					}
					
					valorCobrarPaciente += 
							montoCobrarXRegistro * mapaServiciosEspecificos.get(entry.getKey()).getValorMonto();
					
				}
			}
			
			if (!mapaServiciosAgrupados.isEmpty()) {
				/*Se realiza el calculo del valor a cobrar paciente*/
				for (Map.Entry<Integer, InfoMontoDetalladoServicioDto> entry : 
						mapaServiciosAgrupados.entrySet()) {
					
					if (mapaServiciosAgrupados.get(entry.getKey()).getCantidadServicios() != null && 
							mapaServiciosAgrupados.get(entry.getKey()).getCantidadServicios() > 0) {
						
						montoCobrarXRegistro = 
								Math.ceil((double)mapaServiciosAgrupados.get(entry.getKey()).getSumatoriaServicios() / 
										mapaServiciosAgrupados.get(entry.getKey()).getCantidadServicios().doubleValue()) *
										mapaServiciosAgrupados.get(entry.getKey()).getCantidadMonto().doubleValue();
					}else {
						//Si la cantidad de servicios en el monto no existe o es 0 la cantidad de montoCobrarXRegistro
						//es igual a la cantidad del monto
						montoCobrarXRegistro = mapaServiciosAgrupados.get(entry.getKey()).getCantidadMonto().doubleValue();
					}
					
					valorCobrarPaciente += 
							montoCobrarXRegistro * mapaServiciosAgrupados.get(entry.getKey()).getValorMonto();
					
				}
			}
			return valorCobrarPaciente.doubleValue();
			
		} catch (IPSException ipse) {
			HibernateUtil.abortTransaction();
			throw ipse;
		} catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.facturacion.interfaz.ICatalogoFacturacionMundo#obtenerEsquemaTarifarioProcedimientosContrato(int, int, java.util.Date)
	 */
	public DTOEsqTarProcedimientoContrato obtenerEsquemaTarifarioProcedimientosContrato(
			int codigoContrato, int centroAtencion, Date fechaVigencia) throws IPSException{
		CatalogoFacturacionDelegate delegate=null;
		try{
			delegate=new CatalogoFacturacionDelegate();
			return delegate.obtenerEsquemaTarifarioProcedimientosContrato(codigoContrato, centroAtencion, fechaVigencia);
		} catch (IPSException ipse) {
			HibernateUtil.abortTransaction();
			throw ipse;
		} catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.bl.facturacion.interfaz.ICatalogoFacturacionMundo#obtenerEsquemaTarifarioInventariosContrato(int, int, java.util.Date)
	 */
	public DTOEsqTarInventarioContrato obtenerEsquemaTarifarioInventariosContrato(
			int codigoContrato, int centroAtencion, Date fechaVigencia) throws IPSException{
		CatalogoFacturacionDelegate delegate=null;
		try{
			delegate=new CatalogoFacturacionDelegate();
			return delegate.obtenerEsquemaTarifarioInventariosContrato(codigoContrato, centroAtencion, fechaVigencia);
		} catch (IPSException ipse) {
			HibernateUtil.abortTransaction();
			throw ipse;
		} catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}
	
	public GrupoServicioDto consultaGrupoServicioxServicio (int codigoServicio) throws IPSException{
		GrupoServicioDto grupoServicio=null;
		try {
			HibernateUtil.beginTransaction();
			CatalogoFacturacionDelegate delegate = new  CatalogoFacturacionDelegate();
			grupoServicio=delegate.consultaGrupoServicioxServicio(codigoServicio);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipse){
			HibernateUtil.abortTransaction();
			throw ipse;	
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
		return grupoServicio;
	}
	
}