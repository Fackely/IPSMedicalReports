package com.servinte.axioma.delegate.facturacion;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;

import com.princetonsa.dto.inventario.DTOEsqTarInventarioContrato;
import com.princetonsa.dto.inventario.DTOEsqTarProcedimientoContrato;
import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;
import com.servinte.axioma.dto.administracion.CentroCostoDto;
import com.servinte.axioma.dto.facturacion.BusquedaMontosCobroDto;
import com.servinte.axioma.dto.facturacion.DetalleServicioDto;
import com.servinte.axioma.dto.facturacion.DetalleValorMontoMedicamentoInsumoDto;
import com.servinte.axioma.dto.facturacion.DetalleValorMontoServicioDto;
import com.servinte.axioma.dto.facturacion.FiltroBusquedaMontosCobroDto;
import com.servinte.axioma.dto.facturacion.GrupoServicioDto;
import com.servinte.axioma.dto.facturacion.InfoMontoDetalladoMedicamentoInsumoDto;
import com.servinte.axioma.dto.facturacion.InfoMontoDetalladoServicioDto;
import com.servinte.axioma.dto.facturacion.MontoCobroDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;
import com.servinte.axioma.orm.ContratosEntidadesSub;
import com.servinte.axioma.orm.EntidadesSubcontratadas;

/**
 * Clase que permite el acceso a datos para las parametricas y catalogos
 * del módulo de Manejo Paciente
 * 
 * @author ricruico
 * @version 1.0
 * @created 04-jul-2012 02:23:59 p.m.
 */
public class CatalogoFacturacionDelegate {
	
	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	/**
	 * Método encargado de buscar una entidad subcontratada por el código de la misma
	 * 
	 * @param codigoEntidadSubContratada
	 * @return EntidadesSubcontratadas
	 * @throws BDException
	 */
	public EntidadesSubcontratadas buscarEntidadSubContratadaXId(long codigoEntidadSubContratada) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			return (EntidadesSubcontratadas)persistenciaSvc.find(EntidadesSubcontratadas.class, codigoEntidadSubContratada);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Servicio encargado de obtener el contrato vigente de la entidad subcontratada
	 * 
	 * @param codigoEntidadSubContratada
	 * @param fechaValidacion
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	public ContratosEntidadesSub obtenerContratoVigenteEntidadSubContratada(long codigoEntidadSubContratada, Date fechaValidacion) throws BDException{
		try{
			
			persistenciaSvc= new PersistenciaSvc();
			Map<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoEntidadSubContratada", codigoEntidadSubContratada);
			parameters.put("fechaInicial", fechaValidacion);
			parameters.put("fechaFinal", fechaValidacion);
			return (ContratosEntidadesSub)
					persistenciaSvc.createNamedQueryUniqueResult("catalogoFacturacion.obtenerContratoVigenteEntidadSubContratada", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método que obtiene la parametrización del detalle del servicio de acuerdo
	 * al tarifario oficial
	 * 
	 * @param codigoServicio
	 * @param codigotarifario
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	public DetalleServicioDto obtenerDetalleServicioXTarifarioOficial(int codigoServicio, int codigotarifario) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoServicio", codigoServicio);
			parameters.put("codigoTarifario", codigotarifario);
			return (DetalleServicioDto)persistenciaSvc.
								createNamedQueryUniqueResult("catalogoFacturacion.obtenerDetalleServicioXTarifarioOficial", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método encargado de obtener las diferentes farmacias parametrizadas para los medicamentos/insumos
	 * de las ordenes
	 * 
	 * @param tipoArea
	 * @param prioridades
	 * @param centroAtencionPaciente
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<CentroCostoDto> obtenerCentrosCostoMedicamentosInsumos(int tipoArea, List<Integer> prioridades, int centroAtencionPaciente) throws BDException{
		try{
			List<CentroCostoDto> centrosCostoDto= new ArrayList<CentroCostoDto>();
			persistenciaSvc= new PersistenciaSvc();
			Map<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("tipoArea", tipoArea);
			parameters.put("centroAtencion", centroAtencionPaciente);
			parameters.put("prioridades", prioridades);
			List<Object[]> centrosCosto=(List<Object[]>)persistenciaSvc.
								createNamedQuery("catalogoFacturacion.obtenerCentrosCostoMedicamentosInsumos", parameters);
			if(centrosCosto != null && !centrosCosto.isEmpty()){
				for(Object[] centroCosto:centrosCosto){
					CentroCostoDto dto = new CentroCostoDto();
					dto.setCodigo((Integer)centroCosto[0]);
					dto.setNombre((String)centroCosto[1]);
					dto.setTipoEntidadEjecuta((String)centroCosto[2]);
					centrosCostoDto.add(dto);
				}
			}
			return centrosCostoDto;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Método encargado de obtener los diferentes centros de costo de acuerdo a los grupos de servicio
	 * o unidad de consulta de los servicios de las ordenes
	 * 
	 * @param tipoArea
	 * @param prioridades
	 * @param centroAtencionPaciente
	 * @param gruposServicios
	 * @param unidadesConsultaServicios
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<CentroCostoDto> obtenerCentrosCostoServicios(int tipoArea, List<Integer> prioridades, int centroAtencionPaciente, 
												List<Integer> gruposServicios, List<Integer> unidadesConsultaServicios) throws BDException{
		try{
			List<CentroCostoDto> centrosCostoDto= new ArrayList<CentroCostoDto>();
			persistenciaSvc= new PersistenciaSvc();
			Map<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("tipoArea", tipoArea);
			parameters.put("centroAtencion", centroAtencionPaciente);
			parameters.put("prioridades", prioridades);
			List<Object[]> centrosCosto = new ArrayList<Object[]>();
			if(!gruposServicios.isEmpty() && !unidadesConsultaServicios.isEmpty()){
				parameters.put("gruposServicios", gruposServicios);
				parameters.put("unidadesConsultaServicios", unidadesConsultaServicios);
				centrosCosto=(List<Object[]>)persistenciaSvc.
								createNamedQuery("catalogoFacturacion.obtenerCentrosCostoPorGrupoUnidadConsultaServicios", parameters);
			}
			else{
				if(!gruposServicios.isEmpty()){
					parameters.put("gruposServicios", gruposServicios);
					centrosCosto=(List<Object[]>)persistenciaSvc.
									createNamedQuery("catalogoFacturacion.obtenerCentrosCostoPorGrupoServicios", parameters);
				}
				else if(!unidadesConsultaServicios.isEmpty()){
					parameters.put("unidadesConsultaServicios", unidadesConsultaServicios);
					centrosCosto=(List<Object[]>)persistenciaSvc.
									createNamedQuery("catalogoFacturacion.obtenerCentrosCostoPorUnidadConsultaServicios", parameters);
				}
			}
			if(centrosCosto != null && !centrosCosto.isEmpty()){
				for(Object[] centroCosto:centrosCosto){
					CentroCostoDto dto = new CentroCostoDto();
					dto.setCodigo((Integer)centroCosto[0]);
					dto.setNombre((String)centroCosto[1]);
					dto.setTipoEntidadEjecuta((String)centroCosto[2]);
					centrosCostoDto.add(dto);
				}
			}
			return centrosCostoDto;
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}

	/**
	 * 
	 * @param medicamentoInsumoMontoDto
	 * @param montoCobro
	 */
	public DetalleValorMontoMedicamentoInsumoDto obtenerMontoCobroDetMedicamentoInsumo(DetalleValorMontoMedicamentoInsumoDto medicamentoInsumoMontoDto, MontoCobroDto montoCobro){
		return null;
	}

	/**
	 * 
	 * @param montoCobro
	 * @param servicioMontoDto
	 */
	public DetalleValorMontoServicioDto obtenerMontoCobroDetServicio(MontoCobroDto montoCobro, DetalleValorMontoServicioDto servicioMontoDto){
		return null;
	}
	
	/**
	 * Metodo encargado de obtener los montos de cobro para el convenio seleccionado.<br/>
	 * DCU 986
	 * 
	 * @author diecorqu
	 * @param filtroBusquedaMontosCobro
	 * @return List<BusquedaMontosCobroDto>
	 */
	@SuppressWarnings("unchecked")
	public List<BusquedaMontosCobroDto> obtenerMontosCobro(FiltroBusquedaMontosCobroDto filtroBusquedaMontosCobro) throws BDException {
		
		Map<String, Object> parametros = new HashMap<String, Object>();
		List<Integer> montos = null;
		
		try {
			
			persistenciaSvc = new PersistenciaSvc(); 
			
			montos = this.obtenerMontosCobroXConvenioFecha(
						filtroBusquedaMontosCobro.getConvenio(), 
						filtroBusquedaMontosCobro.getFechaBusqueda());
			
			if (montos != null && !montos.isEmpty()) {
				
				parametros.put("listaMontos", montos);
				parametros.put("estado", filtroBusquedaMontosCobro.isActivo());
				parametros.put("viaIngreso", filtroBusquedaMontosCobro.getViaIngreso());
				parametros.put("tipoAfiliado", filtroBusquedaMontosCobro.getTipoAfiliado());
				parametros.put("tipoPaciente", filtroBusquedaMontosCobro.getTipoPaciente());
				parametros.put("clasificacionSocioEconomica", filtroBusquedaMontosCobro.getClasificacionSocioEconomica());
				
				if (!filtroBusquedaMontosCobro.isCuentaAbierta() && 
						filtroBusquedaMontosCobro.isAutorizacionCapitacionSubcontratada()) {
					
					parametros.put("tipoMonto", filtroBusquedaMontosCobro.getTipoMonto());
					
					if (filtroBusquedaMontosCobro.getNaturalezaPaciente() == ConstantesBD.codigoNuncaValido || 
							filtroBusquedaMontosCobro.getNaturalezaPaciente() == 0) {

						return (List<BusquedaMontosCobroDto>)persistenciaSvc.
								createNamedQuery("catalogoFacturacion.obtenerMontosCobroAutorizacionesNaturalezaNull", parametros);

					} else {

						parametros.put("naturalezaPaciente", filtroBusquedaMontosCobro.getNaturalezaPaciente());
						return (List<BusquedaMontosCobroDto>)persistenciaSvc.
								createNamedQuery("catalogoFacturacion.obtenerMontosCobroAutorizaciones", parametros);

					}
					
				} else {
					
					if (filtroBusquedaMontosCobro.getNaturalezaPaciente() == ConstantesBD.codigoNuncaValido || 
							filtroBusquedaMontosCobro.getNaturalezaPaciente() == 0) {

						return (List<BusquedaMontosCobroDto>)persistenciaSvc.
								createNamedQuery("catalogoFacturacion.obtenerMontosCobroNaturalezaNull", parametros);

					} else {

						parametros.put("naturalezaPaciente", filtroBusquedaMontosCobro.getNaturalezaPaciente());
						return (List<BusquedaMontosCobroDto>)persistenciaSvc.
								createNamedQuery("catalogoFacturacion.obtenerMontosCobro", parametros);

					}

				}
				
			}
			
		} catch (Exception e) {
			
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
			
		} 
		return null;
	}

	/**
	 * Metodo encargado de obtener los montos de cobro para el convenio y fecha seleccionados.<br/>
	 * DCU 986
	 * 
	 * @author diecorqu
	 * @param filtroBusquedaMontosCobro
	 * @return List<Integer>
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> obtenerMontosCobroXConvenioFecha(int codigoConvenio, Date fecha) throws BDException {
		try {
			
			persistenciaSvc = new PersistenciaSvc(); 
			
			Map<String, Object> datosConvenioFecha = new HashMap<String, Object>();
			datosConvenioFecha.put("codigoConvenio", codigoConvenio);
			datosConvenioFecha.put("fecha", fecha);
			
			return (List<Integer>)persistenciaSvc.
					createNamedQuery("catalogoFacturacion.obtenerMontosCobroXConvenioFecha", datosConvenioFecha);
			
		} catch (Exception e) {
			
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
			
		} 
	}
	
	/**
	 * Metodo encargado de obtener la información del valor del monto especifico para
	 * el medicamento o insumo pasado por parámetro.<br/>
	 * DCU 986
	 * 
	 * @author diecorqu
	 * @param filtroBusquedaMontosCobro
	 * @return InfoMontoDetalladoMedicamentoInsumoDto
	 */
	public InfoMontoDetalladoMedicamentoInsumoDto obtenerMontoDetalladoMedicamentoInsumo(int codigoDetalleMonto, int codigoArticulo) throws BDException {
		
		InfoMontoDetalladoMedicamentoInsumoDto montoMedicamentoInsumo = null;
		Object[] montoAgrupadoMedicamentoInsumo = null; 
		
		try {
			
			persistenciaSvc = new PersistenciaSvc(); 
			
			Map<String, Object> filtro = new HashMap<String, Object>();
			filtro.put("codigoDetalleMonto", codigoDetalleMonto);
			filtro.put("codigoArticulo", codigoArticulo);
			
			/*Se realiza la busqueda del monto especifico para el medicamento o insumo relacionado*/
			montoMedicamentoInsumo = (InfoMontoDetalladoMedicamentoInsumoDto)persistenciaSvc.
					createNamedQueryFirstResult("catalogoFacturacion.obtenerMontoMedicamentoInsumoEspecifico", filtro);
			
			if (montoMedicamentoInsumo != null) {
				/*Si el monto para el articulo es especifico*/
				montoMedicamentoInsumo.setPorAgrupacion(false);
			} else {
				/*Si no se encuentra monto especifico se realiza busqueda por agrupacion*/
				montoAgrupadoMedicamentoInsumo = (Object[])persistenciaSvc.
						createNamedQueryUniqueResult("catalogoFacturacion.obtenerMontoAgrupadoMedicamentoInsumo", filtro);
				
				if(montoAgrupadoMedicamentoInsumo != null) {
					
					montoMedicamentoInsumo = new InfoMontoDetalladoMedicamentoInsumoDto(
							(Integer)montoAgrupadoMedicamentoInsumo[0],
							(Integer)montoAgrupadoMedicamentoInsumo[1],
							(Integer)montoAgrupadoMedicamentoInsumo[2],
							(Integer)montoAgrupadoMedicamentoInsumo[3],
							(String)montoAgrupadoMedicamentoInsumo [4],
							(Integer)montoAgrupadoMedicamentoInsumo[5],
							(Integer)montoAgrupadoMedicamentoInsumo[6],
							(Double)montoAgrupadoMedicamentoInsumo [7]);
					/*Si el monto para el articulo es agrupado*/
					montoMedicamentoInsumo.setPorAgrupacion(true);
					
				}
			}
			
			return montoMedicamentoInsumo;
			
		} catch (Exception e) {
			
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
			
		} 
	}
	
	/**
	 * Metodo encargado de obtener la información del valor del monto especifico para
	 * el servicio pasado por parámetro.<br/>
	 * DCU 986
	 * @author diecorqu
	 * @param filtroBusquedaMontosCobro
	 * @return InfoMontoDetalladoServicioDto
	 */
	public InfoMontoDetalladoServicioDto obtenerMontoDetalladoServicio(int codigoDetalleMonto, int codigoServicio) throws BDException {
		
		InfoMontoDetalladoServicioDto montoServicio = null;
		Object[] montoAgrupadoServicio = null; 
		final int POSICION_CODIGO = 0;
		final int POSICION_GRUPOSERVICIO = 1;
		final int POSICION_TIPOSERVICIO = 2;
		final int POSICION_ESPECIALIDAD = 3;
		final int POSICION_CANTIDADSERVICIOS = 4;
		final int POSICION_CANTIDADMONTO = 5;
		final int POSICION_VALORMONTO = 6;
		
		try {

			persistenciaSvc = new PersistenciaSvc(); 

			Map<String, Object> filtro = new HashMap<String, Object>();
			filtro.put("codigoDetalleMonto", codigoDetalleMonto);
			filtro.put("codigoServicio", codigoServicio);

			/*Se realiza la busqueda del monto especifico para el servicio relacionado*/
			montoServicio = (InfoMontoDetalladoServicioDto)persistenciaSvc.
					createNamedQueryFirstResult("catalogoFacturacion.obtenerMontoServicioEspecifico", filtro);

			if (montoServicio != null) {
				montoServicio.setPorAgrupacion(false);
			} else {
				/*Si no se encuentra monto especifico se realiza busqueda por agrupacion*/
				montoAgrupadoServicio = (Object[])persistenciaSvc.
						createNamedQueryUniqueResult("catalogoFacturacion.obtenerMontoAgrupadoServicio", filtro);
				
				if(montoAgrupadoServicio != null) {
					
					montoServicio = new InfoMontoDetalladoServicioDto(
							(Integer)montoAgrupadoServicio[POSICION_CODIGO],
							(Integer)montoAgrupadoServicio[POSICION_GRUPOSERVICIO],
							(String)montoAgrupadoServicio [POSICION_TIPOSERVICIO],
							(Integer)montoAgrupadoServicio[POSICION_ESPECIALIDAD],
							(Integer)montoAgrupadoServicio[POSICION_CANTIDADSERVICIOS],
							(Integer)montoAgrupadoServicio[POSICION_CANTIDADMONTO],
							(Double)montoAgrupadoServicio [POSICION_VALORMONTO]);
					montoServicio.setPorAgrupacion(true);
				}
			}

			return montoServicio;

		} catch (Exception e) {

			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);

		} 
	}
	
	/**
	 * Metodo encargado de obtener el grupo de servicio 
	 * al cual está asociado el servicio de la Orden. 
	 * 
	 * @author diecorqu
	 * @param codigoServicio
	 * @return
	 * @throws BDException
	 */
	public GrupoServicioDto obtenerGrupoServicioXServicio(int codigoServicio) throws BDException{
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoServicio", codigoServicio);
			return (GrupoServicioDto)persistenciaSvc.
								createNamedQueryUniqueResult(
										"catalogoFacturacion.obtenerGrupoServicioXServicio", 
										parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Servicio encargado de obtener los codigosPK de las unidades de consulta del servicio
	 * 
	 * @param codigoServicio
	 * @return
	 * @throws BDException
	 * @author ricruico
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> obtenerUnidadesConsultaServicio(int codigoServicio) throws BDException{
		List<Integer> codigosUnidades= new ArrayList<Integer>();
		try{
			
			persistenciaSvc= new PersistenciaSvc();
			Map<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoServicio", codigoServicio);
			codigosUnidades=(List<Integer>)
					persistenciaSvc.createNamedQuery("catalogoFacturacion.obtenerUnidadesConsultaServicio", parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return codigosUnidades;
	}
	
	/**
	 * Metodo encargado de obtener los codigos de los centros de costo
	 * por entidad subcontrada, estado activo, y tipo de area. 
	 * 
	 * @author diecorqu
	 * @param codigoServicio
	 * @return
	 * @throws BDException
	 */
	@SuppressWarnings("unchecked")
	public List<DtoCentroCosto> obtenerCentrosCostoEntidadSubContratadaXTipoArea(
			long codigoEntidadSubContratada, int tipoArea) throws BDException{
		
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoEntidadSubContratada", codigoEntidadSubContratada);
			parameters.put("esActivo", true);
			parameters.put("tipoArea", tipoArea);
			return (List<DtoCentroCosto>)persistenciaSvc.
					createNamedQuery(
							"catalogoFacturacion.obtenerCentrosCostoEntidadSubContratadaXTipoArea",
							parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	/**
	 * Metodo encargado de obtener informacion del esquema tarifario de procedimientos por contrato
	 * 
	 * @param codigoContrato
	 * @param centroAtencion
	 * @param fechaVigencia
	 * @return DTOEsqTarProcedimientoContrato
	 * @throws BDException
	 * @author jeilones
	 * @created 18/09/2012
	 */
	public DTOEsqTarProcedimientoContrato obtenerEsquemaTarifarioProcedimientosContrato(
			int codigoContrato, int centroAtencion, Date fechaVigencia) throws BDException{
		
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoContrato", codigoContrato);
			parameters.put("codigoCentroAtencion", centroAtencion);
			parameters.put("fechaVigencia", fechaVigencia);
			return (DTOEsqTarProcedimientoContrato)persistenciaSvc.
					createNamedQueryFirstResult(
							"catalogoFacturacion.obtenerEsquemaTarifarioProcedimientosContrato",
							parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	
	/**
	 * Metodo encargado de obtener informacion del esquema tarifario de inventarios por contrato
	 * 
	 * @param codigoContrato
	 * @param centroAtencion
	 * @param fechaVigencia
	 * @return DTOEsqTarInventarioContrato
	 * @throws BDException
	 * @author jeilones
	 * @created 18/09/2012
	 */
	public DTOEsqTarInventarioContrato obtenerEsquemaTarifarioInventariosContrato(
			int codigoContrato, int centroAtencion, Date fechaVigencia) throws BDException{
		
		try{
			persistenciaSvc= new PersistenciaSvc();
			Map<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("codigoContrato", codigoContrato);
			parameters.put("codigoCentroAtencion", centroAtencion);
			parameters.put("fechaVigencia", fechaVigencia);
			return (DTOEsqTarInventarioContrato)persistenciaSvc.
					createNamedQueryFirstResult(
							"catalogoFacturacion.obtenerEsquemaTarifarioInventariosContrato",
							parameters);
		}
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
	}
	
	
	public GrupoServicioDto consultaGrupoServicioxServicio (int codigoServicio) throws IPSException{
		GrupoServicioDto grupoServicio=null;
		try {
			persistenciaSvc= new PersistenciaSvc();
			HashMap<String, Object> parametro = new HashMap<String, Object>(0);
			parametro.put("codigoServicio", codigoServicio);
			grupoServicio=(GrupoServicioDto) persistenciaSvc.createNamedQueryFirstResult("catalogoFacturacion.consultaGrupoServicioxServicio",parametro);
			return grupoServicio;
		}
		
		catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		
			
	}
}
