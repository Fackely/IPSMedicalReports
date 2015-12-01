package com.princetonsa.dao.postgresql.facturacion;

import util.InfoPorcentajeValor;

import com.princetonsa.dao.facturacion.CalculoHonorariosPoolesDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseCalculoHonorariosPoolesDao;
import com.princetonsa.dto.facturacion.DtoParametrosBusquedaHonorarios;

/**
 * 
 * @author axioma
 *
 */
public class PostgresqlCalculoHonorariosPoolesDao implements CalculoHonorariosPoolesDao
{
	@Override
	public InfoPorcentajeValor obtenerHonorarioPoolXTipoLiquidacion(DtoParametrosBusquedaHonorarios dto)
	{
		return SqlBaseCalculoHonorariosPoolesDao.obtenerHonorarioPoolXTipoLiquidacion(dto);
	}

	@Override
	public InfoPorcentajeValor obtenerHonorariosPromocionesCargos(
			double codigoDetalleCargo)
	{
		return SqlBaseCalculoHonorariosPoolesDao.obtenerHonorariosPromocionesCargos(codigoDetalleCargo);
	}

	@Override
	public DtoParametrosBusquedaHonorarios cargarParametrosBusquedaHonorarios(
			double codigoDetalleCargo)
	{
		return SqlBaseCalculoHonorariosPoolesDao.cargarParametrosBusquedaHonorarios(codigoDetalleCargo);
	}
}