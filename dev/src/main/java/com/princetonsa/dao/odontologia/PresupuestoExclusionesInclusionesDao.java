package com.princetonsa.dao.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import util.odontologia.InfoDetalleHistoricoIncExcPresupuesto;
import util.odontologia.InfoSeccionInclusionExclusion;

import com.princetonsa.dto.odontologia.DtoExclusionPresupuesto;
import com.princetonsa.dto.odontologia.DtoInclusionesPresupuesto;

/**
 * 
 * @author axioma
 *
 */
public interface PresupuestoExclusionesInclusionesDao
{
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public double guardarExclusion(Connection con, DtoExclusionPresupuesto dto  );
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public double guardarInclusion(Connection con, DtoInclusionesPresupuesto dto  );

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
			String estadoAutorizacion, boolean utilizaProgramas);

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
			String estadoAutorizacion, boolean utilizaProgramas);

	
	/**
	 * 
	 * @param codigoPresupuesto
	 * @param codigoProgramaServicio
	 * @param utilizaProgramas
	 * @return
	 */
	public BigDecimal obtenerTarifaProgramaServicioPresupuesto(	BigDecimal codigoPresupuesto, Double codigoProgramaServicio,boolean utilizaProgramas); 
	
	
	/**
	 * 
	 * @param codigoPresupuesto
	 * @return
	 */
	public ArrayList<InfoDetalleHistoricoIncExcPresupuesto> cargarHistoricoInclusiones(
			BigDecimal codigoPresupuesto, int institucion);

	/**
	 * 
	 * @param codigoPresupuesto
	 * @return
	 */
	public ArrayList<InfoDetalleHistoricoIncExcPresupuesto> cargarHistoricoExclusiones(
			BigDecimal codigoPresupuesto, int institucion);
}
