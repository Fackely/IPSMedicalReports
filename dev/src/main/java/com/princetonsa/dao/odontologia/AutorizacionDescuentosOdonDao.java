package com.princetonsa.dao.odontologia;

import java.math.BigDecimal;
import java.util.ArrayList;

import util.odontologia.InfoDefinirSolucitudDsctOdon;

import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;

import com.princetonsa.dto.odontologia.DtoServicioOdontologico;

public interface  AutorizacionDescuentosOdonDao
{
	/**
	 * 
	 * @param presupuesto
	 * @return
	 */
	public ArrayList<DtoServicioOdontologico> consultarDetalleProgramasPresupuesto(double presupuesto, int institucion);
	

	/**
	 * 
	 * @param presupuesto
	 * @return
	 */
	public ArrayList<DtoServicioOdontologico> consultarDetalleServiciosPresupuesto(double presupuesto, int institucion);
	
	
	/**
	 * 
	 * @param inclusion
	 * @return
	 */
	public ArrayList<DtoServicioOdontologico> consultarDetalleProgramasInclusion(double presupuesto, int institucion);
	
	/**
	 * 
	 * @param inclusion
	 * @return
	 */
	public ArrayList<DtoServicioOdontologico> consultarDetalleServiciosInclusion(double presupuesto, int institucion);
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public ArrayList<InfoDefinirSolucitudDsctOdon> cargarDefinicionSolicitudesDescuento(InfoDefinirSolucitudDsctOdon dto);
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  boolean modificarPresupuestoDescuento(DtoPresupuestoOdontologicoDescuento dto );

	/**
	 * 
	 */
	public ArrayList<DtoPresupuestoOdontologicoDescuento> consultarHistoricoSolAutorizacionDcto(BigDecimal presupuestoDctoOdon);
	
	/**
	 * 
	 * @param loginUsuario
	 * @param codigoDetDescuento
	 * @return
	 */
	public boolean tieneNivelAutorizacion(String loginUsuario, BigDecimal codigoDetDescuento, String actividadTipoUsuario);
	
	/**
	 * 
	 * @param loginUsuario
	 * @param codigoDetDescuento
	 * @return
	 */
	public int vigenciaDiasPresupuesto(BigDecimal tmpCodigoPresupuesto);
	
	
	
	/**
	 * 
	 * @param loginUsuario
	 * @param codigoDetDescuento
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean tieneNivelAutorizacionCentroAtencion(String loginUsuario, BigDecimal codigoDetDescuento, int codigoInstitucion, String actividadTipoUsuario);
	
}