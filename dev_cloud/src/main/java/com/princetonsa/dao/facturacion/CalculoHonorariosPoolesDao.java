package com.princetonsa.dao.facturacion;

import com.princetonsa.dto.facturacion.DtoParametrosBusquedaHonorarios;

import util.InfoPorcentajeValor;

/**
 * 
 * @author axioma
 *
 */
public interface CalculoHonorariosPoolesDao
{
	/**
	 * 
	 * @param servicio
	 * @param tipoLiquidacion
	 * @param pool
	 * @param convenio
	 * @param esquemaTarifario
	 * @param especialidad
	 * @return
	 */
	public InfoPorcentajeValor obtenerHonorarioPoolXTipoLiquidacion (DtoParametrosBusquedaHonorarios dto);
	
	/**
	 * 
	 * @param codigoDetalleCargo
	 * @return
	 */
	public InfoPorcentajeValor obtenerHonorariosPromocionesCargos(double codigoDetalleCargo);
	
	/**
	 * 
	 * @param codigoDetalleCargo
	 * @return
	 */
	public DtoParametrosBusquedaHonorarios cargarParametrosBusquedaHonorarios(double codigoDetalleCargo);
	
}