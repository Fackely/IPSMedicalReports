package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.odontologia.InfoDefinirSolucitudDsctOdon;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologicoDescuento;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
/**
 * 
 * @author axioma
 *
 */
public class AutorizacionDescuentosOdon{
	
	static Logger logger = Logger.getLogger(AutorizacionDescuentosOdon.class);
	
	/**
	 * 
	 * @param presupuesto
	 * @return
	 */
	public static ArrayList<DtoServicioOdontologico> consultarDetalleProgramasPresupuesto(double presupuesto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionDescuentosOdonDao().consultarDetalleProgramasPresupuesto(presupuesto, institucion);
	}
	
	/**
	 * 
	 * @param presupuesto
	 * @return
	 */
	public static ArrayList<DtoServicioOdontologico> consultarDetalleServiciosPresupuesto(double presupuesto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionDescuentosOdonDao().consultarDetalleServiciosPresupuesto(presupuesto, institucion);
	}
	
	/**
	 * 
	 * @param inclusion
	 * @return
	 */
	public static ArrayList<DtoServicioOdontologico> consultarDetalleProgramasInclusion(double presupuesto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionDescuentosOdonDao().consultarDetalleProgramasInclusion(presupuesto, institucion);
	}
	
	/**
	 * 
	 * @param inclusion
	 * @return
	 */
	public static ArrayList<DtoServicioOdontologico> consultarDetalleServiciosInclusion(double presupuesto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionDescuentosOdonDao().consultarDetalleServiciosInclusion(presupuesto, institucion);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  ArrayList<InfoDefinirSolucitudDsctOdon> cargarDefinicionSolicitudesDescuento(
			InfoDefinirSolucitudDsctOdon dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionDescuentosOdonDao().cargarDefinicionSolicitudesDescuento(dto);
	}
	
		/**
	 * 
	 */
	public static ArrayList<DtoPresupuestoOdontologicoDescuento> consultarHistoricoSolAutorizacionDcto(BigDecimal presupuestoDctoOdon)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionDescuentosOdonDao().consultarHistoricoSolAutorizacionDcto(presupuestoDctoOdon);
	}
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  boolean modificarPresupuestoDescuento(
			DtoPresupuestoOdontologicoDescuento dto) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionDescuentosOdonDao().modificarPresupuestoDescuento(dto);
	}
	
	/**
	 * 
	 */
	public static boolean tieneNivelAutorizacion(String loginUsuario, BigDecimal codigoDetDescuento, String actividadTipoUsuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionDescuentosOdonDao().tieneNivelAutorizacion(loginUsuario,codigoDetDescuento, actividadTipoUsuario);
	}
	
	/**
	 * Vigencia en dias por valor o presupuesto 
	 */
	public static int vigenciaDiasPresupuesto(BigDecimal tmpCodigoPresupuesto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionDescuentosOdonDao().vigenciaDiasPresupuesto(tmpCodigoPresupuesto);
	}
	
	
	/**
	 * Tiene Nivel Autorizacion Centro Atencion  
	 * @param loginUsuario
	 * @param codigoDetDescuento
	 * @param codigoInstitucion
	 * @return
	 */
	public static  boolean tieneNivelAutorizacionCentroAtencion(String loginUsuario,
														BigDecimal codigoDetDescuento, 
														int codigoInstitucion,
														String actividadTipoUsuario) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAutorizacionDescuentosOdonDao().tieneNivelAutorizacionCentroAtencion(loginUsuario, codigoDetDescuento, codigoInstitucion, actividadTipoUsuario);
	}

	
	
	
}