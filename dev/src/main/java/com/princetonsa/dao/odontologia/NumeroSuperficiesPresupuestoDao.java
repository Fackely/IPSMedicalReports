package com.princetonsa.dao.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import util.odontologia.InfoNumSuperficiesPresupuesto;

import com.princetonsa.dto.odontologia.DtoDetallePresupuestoPlanNumSuperficies;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoNumeroSuperficies;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoPresupuesto;
import com.princetonsa.dto.odontologia.DtoPresupuestoPlanTratamientoNumeroSuperficies;
import com.princetonsa.dto.odontologia.DtoProgHallazgoPieza;
import com.princetonsa.dto.odontologia.DtoSuperficiesPorPrograma;

/**
 * 
 * 
 * @author Wilson Rios 
 *
 * May 7, 2010 - 2:01:14 PM
 */
public interface NumeroSuperficiesPresupuestoDao 
{
	/**
	 * 
	 * Metodo para insertar el encabezado
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public BigDecimal insertarEncabezado(Connection con, DtoPresupuestoPlanTratamientoNumeroSuperficies dto);
	
	/**
	 * 
	 * Metodo para insertar el detalle
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public boolean insertarDetalle(Connection con, DtoDetallePresupuestoPlanNumSuperficies dto);
	
	/**
	 * 
	 * Metodo para eliminar los detalle x codigo encabezado
	 * @param con
	 * @param codigoEncabezadoPresuPlanTtoNumSuperficies
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public boolean eliminarEncabezado(Connection con, BigDecimal codigoPk);
	
	/**
	 * 
	 * Metodo para eliminar los detalle x codigo encabezado
	 * @param con
	 * @param codigoEncabezadoPresuPlanTtoNumSuperficies
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public boolean eliminarDetalleXEncabezado(Connection con, BigDecimal codigoEncabezadoPresuPlanTtoNumSuperficies);
	
	/**
	 * 
	 * Metodo para realizar la busqueda de la estructura de numero de superficies
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public ArrayList<DtoPresupuestoPlanTratamientoNumeroSuperficies> busquedaEncabezado(	Connection con, 
																								DtoPresupuestoPlanTratamientoNumeroSuperficies dto);
	
	/**
	 * 
	 * Metodo para realizar la busqueda x detalle
	 * @param dto1
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public ArrayList<DtoDetallePresupuestoPlanNumSuperficies> busquedaDetalle(Connection con, DtoDetallePresupuestoPlanNumSuperficies dto);
	
	/**
	 * 
	 * Metodo para .......
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public BigDecimal obtenerCodigoPresuPlanTtoProgSer(Connection con, DtoPlanTratamientoPresupuesto dto);
	
	/**
	 * 
	 * Metodo para cargar la informacion de numSuperficies del plan tratamiento presupuesto
	 * @param con
	 * @param dto
	 * @param codigoSuperficie
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public ArrayList<InfoNumSuperficiesPresupuesto> obtenerInfoNumSuperficiesPresupuesto(Connection con, DtoPresupuestoPlanTratamientoNumeroSuperficies dto, int codigoSuperficie);
	
	/**
	 * 
	 * Metodo para .......
	 * @param con
	 * @param dto
	 * @param codigoSuperficie
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public ArrayList<InfoNumSuperficiesPresupuesto> obtenerInfoNumSuperficiesPlanTratamiento(Connection con, DtoPlanTratamientoNumeroSuperficies dto, int codigoSuperficie);
	
	/**
	 * 
	 * Metodo para .......
	 * @param con
	 * @param dtoBusqueda
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public DtoSuperficiesPorPrograma obtenerSuperficieXProgramaPlanTratamiento(Connection con, DtoSuperficiesPorPrograma dtoBusqueda);
	
	/**
	 * 
	 * Metodo para .......
	 * @param con
	 * @param dtoBusqueda
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public DtoProgHallazgoPieza obtenerProgramaHallazgoPiezaPlanTratamiento(Connection con, DtoProgHallazgoPieza dtoBusqueda);
	
	/**
	 * 
	 * Metodo para .......
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public boolean guardarSuperficiesPlanTratamiento(Connection con, DtoSuperficiesPorPrograma dto);
}
