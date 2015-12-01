package com.servinte.axioma.bl.facturacion.interfaz;

import java.util.Date;
import java.util.List;

import com.princetonsa.dto.inventario.DTOEsqTarInventarioContrato;
import com.princetonsa.dto.inventario.DTOEsqTarProcedimientoContrato;
import com.servinte.axioma.dto.administracion.CentroCostoDto;
import com.servinte.axioma.dto.capitacion.NivelAutorizacionDto;
import com.servinte.axioma.dto.facturacion.BusquedaMontosCobroDto;
import com.servinte.axioma.dto.facturacion.DetalleServicioDto;
import com.servinte.axioma.dto.facturacion.FiltroBusquedaMontosCobroDto;
import com.servinte.axioma.dto.facturacion.GrupoServicioDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Interface que provee los servicios de Negocio correspondientes a los
 * catalogos o parámetricas del modulo de Facturación
 * 
 * @author ricruico
 * @version 1.0
 * @created 20-jun-2012 02:24:00 p.m.
 */
public interface ICatalogoFacturacionMundo {

	
	/**
	 * Metodo encargado de obtener los montos de cobro para el convenio seleccionado.<br/>
	 * DCU 986
	 * @param filtroBusquedaMontosCobro
	 * @param requiereTransaccion
	 * @return
	 */
	List<BusquedaMontosCobroDto> buscarMontosCobro(FiltroBusquedaMontosCobroDto filtroBusquedaMontosCobro,boolean rquiereTransaccion) throws IPSException;
	
	/**
	 * Método encargado de obtener los diferentes centros de costo o farmacias de acuerdo a los grupos de servicio
	 * o unidad de consulta de los servicios de las ordenes

	 * @param centroAtencionPaciente
	 * @param ordenesPorAutorizar
	 * @param nivelesAutorizacion
	 * @return
	 * @throws IPSException
	 */
	List<CentroCostoDto> obtenerCentrosCostoEntidadSubcontratadaPorOrdenes(int centroAtencionPaciente, List<OrdenAutorizacionDto> ordenesPorAutorizar, List<NivelAutorizacionDto> nivelesAutorizacion) throws IPSException;
	
	/**
	 * Método que obtiene la parametrización del detalle del servicio de acuerdo
	 * al tarifario oficial
	 * 
	 * @param codigoServicio
	 * @param codigotarifario
	 * @return
	 * @throws IPSException
	 */
	DetalleServicioDto obtenerDetalleServicioXTarifarioOficial(int codigoServicio, int codigotarifario) throws IPSException;
	
	/**
	 * Metodo encagado de realizar el calculo del valor a cobrar paciente por medicamentos o insumos
	 * @param montoCobro
	 * @param listaMedicamentosInsumos
	 * @throws IPSException 
	 */
	public double valorCobrarPacienteMedicamentosInsumos(
			BusquedaMontosCobroDto montoCobro, List<MedicamentoInsumoAutorizacionOrdenDto> listaMedicamentosInsumos) throws IPSException;
	
	/**
	 * Metodo encagado de realizar el calculo del valor a cobrar paciente por servicios
	 * @param listaServicios
	 * @param montoCobro
	 */
	public double valorCobrarPacienteServicios(BusquedaMontosCobroDto montoCobro, List<ServicioAutorizacionOrdenDto> listaServicios) throws IPSException;
	
	/**
	 * Metodo encargado de obtener informacion del esquema tarifario de procedimientos por contrato
	 * 
	 * @param codigoContrato
	 * @param centroAtencion
	 * @param fechaVigencia
	 * @return DTOEsqTarProcedimientoContrato
	 * @throws IPSException
	 * @author jeilones
	 * @created 18/09/2012
	 */
	DTOEsqTarProcedimientoContrato obtenerEsquemaTarifarioProcedimientosContrato(
			int codigoContrato, int centroAtencion, Date fechaVigencia) throws IPSException;
	
	/**
	 * Metodo encargado de obtener informacion del esquema tarifario de inventarios por contrato
	 * 
	 * @param codigoContrato
	 * @param centroAtencion
	 * @param fechaVigencia
	 * @return DTOEsqTarInventarioContrato
	 * @throws IPSException
	 * @author jeilones
	 * @created 18/09/2012
	 */
	DTOEsqTarInventarioContrato obtenerEsquemaTarifarioInventariosContrato(
			int codigoContrato, int centroAtencion, Date fechaVigencia) throws IPSException;
	
	/**
	 * Metodo encargado de obtener el grupo de servicio de un servicio 
	 * 
	* @param codigoServicio
	 * @return GrupoServicioDto
	 * @throws IPSException
	 * @author ginsotfu
	 * @created 20/11/2012
	 */
	GrupoServicioDto consultaGrupoServicioxServicio(int codigoServicio) throws IPSException;
	
}