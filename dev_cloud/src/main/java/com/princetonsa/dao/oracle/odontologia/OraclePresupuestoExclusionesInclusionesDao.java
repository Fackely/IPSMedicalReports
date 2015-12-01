package com.princetonsa.dao.oracle.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import util.odontologia.InfoDetalleHistoricoIncExcPresupuesto;
import util.odontologia.InfoSeccionInclusionExclusion;

import com.princetonsa.dao.odontologia.PresupuestoExclusionesInclusionesDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBasePresupuestoExclusionesInclusionesDao;
import com.princetonsa.dto.odontologia.DtoExclusionPresupuesto;
import com.princetonsa.dto.odontologia.DtoInclusionesPresupuesto;

/**
 * 
 * @author axioma
 *
 */
public class OraclePresupuestoExclusionesInclusionesDao implements PresupuestoExclusionesInclusionesDao
{
	/**
	 * 
	 * @param planTratamiento
	 * @param estadoProgramasServiciosPlanTratamiento
	 * @param estadoAutorizacion
	 * @param utilizaProgramas
	 * @return
	 */
	public InfoSeccionInclusionExclusion cargarInclusionesPlanTratamiento(
			BigDecimal planTratamiento,
			String estadoProgramasServiciosPlanTratamiento,
			String estadoAutorizacion, boolean utilizaProgramas)
	{
		return SqlBasePresupuestoExclusionesInclusionesDao.cargarInclusionesPlanTratamiento(planTratamiento,estadoProgramasServiciosPlanTratamiento,estadoAutorizacion,utilizaProgramas);
	}

	/**
	 * 
	 * @param codigoPresupuesto
	 * @param estadoProgramasServiciosPlanTratamiento
	 * @param estadoAutorizacion
	 * @param utilizaProgramas
	 * @return
	 */
	public InfoSeccionInclusionExclusion cargarExclusionesPresupuesto(
			BigDecimal codigoPresupuesto,
			String estadoProgramasServiciosPlanTratamiento,
			String estadoAutorizacion, boolean utilizaProgramas)
	{
		return SqlBasePresupuestoExclusionesInclusionesDao.cargarExclusionesPresupuesto(codigoPresupuesto, estadoProgramasServiciosPlanTratamiento, estadoAutorizacion, utilizaProgramas);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public double guardarExclusion(Connection con, DtoExclusionPresupuesto dto  )
	{
		return SqlBasePresupuestoExclusionesInclusionesDao.guardarExclusion(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public double guardarInclusion(Connection con, DtoInclusionesPresupuesto dto  )
	{
		return SqlBasePresupuestoExclusionesInclusionesDao.guardarInclusion(con, dto);
	}

	@Override
	public ArrayList<InfoDetalleHistoricoIncExcPresupuesto> cargarHistoricoExclusiones(
			BigDecimal codigoPresupuesto, int institucion)
	{
		return SqlBasePresupuestoExclusionesInclusionesDao.cargarHistoricoExclusiones(codigoPresupuesto, institucion);
	}

	@Override
	public ArrayList<InfoDetalleHistoricoIncExcPresupuesto> cargarHistoricoInclusiones(
			BigDecimal codigoPresupuesto, int institucion)
	{
		return SqlBasePresupuestoExclusionesInclusionesDao.cargarHistoricoInclusiones(codigoPresupuesto, institucion);
	}

	@Override
	public BigDecimal obtenerTarifaProgramaServicioPresupuesto(
			BigDecimal codigoPresupuesto, Double codigoProgramaServicio,
			boolean utilizaProgramas) {
		return SqlBasePresupuestoExclusionesInclusionesDao.obtenerTarifaProgramaServicioPresupuesto(codigoPresupuesto, codigoProgramaServicio, utilizaProgramas);
	}
}
