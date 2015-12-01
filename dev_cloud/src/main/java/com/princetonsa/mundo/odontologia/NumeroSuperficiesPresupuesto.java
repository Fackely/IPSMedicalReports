package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.odontologia.InfoNumSuperficiesPresupuesto;

import com.princetonsa.dao.DaoFactory;
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
 * May 7, 2010 - 2:24:56 PM
 */
public class NumeroSuperficiesPresupuesto 
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
	public static BigDecimal insertarEncabezado(Connection con, DtoPresupuestoPlanTratamientoNumeroSuperficies dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroSuperficiesPresupuestoDao().insertarEncabezado(con, dto);
	}
	
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
	public static boolean insertarDetalle(Connection con, DtoDetallePresupuestoPlanNumSuperficies dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroSuperficiesPresupuestoDao().insertarDetalle(con, dto);
	}
	
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
	public static boolean eliminarEncabezado(Connection con, BigDecimal codigoPk)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroSuperficiesPresupuestoDao().eliminarEncabezado(con, codigoPk);
	}
	
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
	public static boolean eliminarDetalleXEncabezado(Connection con, BigDecimal codigoEncabezadoPresuPlanTtoNumSuperficies)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroSuperficiesPresupuestoDao().eliminarDetalleXEncabezado(con, codigoEncabezadoPresuPlanTtoNumSuperficies);
	}
	
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
	public static ArrayList<DtoPresupuestoPlanTratamientoNumeroSuperficies> busquedaEncabezado(	Connection con, 
																								DtoPresupuestoPlanTratamientoNumeroSuperficies dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroSuperficiesPresupuestoDao().busquedaEncabezado(con, dto);
	}
	
	/**
	 * 
	 * Metodo para realizar la busqueda x detalle
	 * @param dto1
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<DtoDetallePresupuestoPlanNumSuperficies> busquedaDetalle(Connection con, DtoDetallePresupuestoPlanNumSuperficies dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroSuperficiesPresupuestoDao().busquedaDetalle(con, dto);
	}
	
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
	public static BigDecimal obtenerCodigoPresuPlanTtoProgSer(Connection con, DtoPlanTratamientoPresupuesto dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroSuperficiesPresupuestoDao().obtenerCodigoPresuPlanTtoProgSer(con, dto);
	}
	
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
	public static ArrayList<InfoNumSuperficiesPresupuesto> obtenerInfoNumSuperficiesPresupuesto(Connection con, DtoPresupuestoPlanTratamientoNumeroSuperficies dto, int codigoSuperficie)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroSuperficiesPresupuestoDao().obtenerInfoNumSuperficiesPresupuesto(con, dto, codigoSuperficie);
	}
	
	/**
	 * 
	 * Metodo para cargar la informacion de numSuperficies del plan tratamiento presupuesto, pero solamente x la superficie que recibe x parametro
	 * @param con
	 * @param dto
	 * @param codigoSuperficie
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static InfoNumSuperficiesPresupuesto obtenerInfoNumSuperficiesPresupuestoXSuperfice(Connection con, DtoPresupuestoPlanTratamientoNumeroSuperficies dto, int codigoSuperficie)
	{
		InfoNumSuperficiesPresupuesto retorna= null;
		ArrayList<InfoNumSuperficiesPresupuesto> lista= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroSuperficiesPresupuestoDao().obtenerInfoNumSuperficiesPresupuesto(con, dto, codigoSuperficie);
		for(InfoNumSuperficiesPresupuesto elemento: lista)
		{
			if(codigoSuperficie==elemento.getCodigoSuperficie())
			{
				retorna= elemento;
				break;
			}
		}
		return retorna;
	}
	
	/**
	 * 
	 * Metodo para cargar la informacion de numSuperficies del plan tratamiento presupuesto, pero solamente x la superficie que recibe x parametro y sus "igualdades" del color
	 * @param con
	 * @param dto
	 * @param codigoSuperficie
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<InfoNumSuperficiesPresupuesto> obtenerInfoNumSuperficiesPresupuestoXColorSuperfice(Connection con, DtoPresupuestoPlanTratamientoNumeroSuperficies dto, int codigoSuperficie)
	{
		ArrayList<InfoNumSuperficiesPresupuesto> retorna= new ArrayList<InfoNumSuperficiesPresupuesto>();
		ArrayList<InfoNumSuperficiesPresupuesto> lista= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroSuperficiesPresupuestoDao().obtenerInfoNumSuperficiesPresupuesto(con, dto, codigoSuperficie);
		for(InfoNumSuperficiesPresupuesto elemento: lista)
		{
			if(elemento.isMarcarXDefecto() || elemento.isModificable())
			{
				retorna.add(elemento);
			}
		}
		return retorna;
	}
	
	
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
	public static ArrayList<InfoNumSuperficiesPresupuesto> obtenerInfoNumSuperficiesPlanTratamiento(Connection con, DtoPlanTratamientoNumeroSuperficies dto, int codigoSuperficie)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroSuperficiesPresupuestoDao().obtenerInfoNumSuperficiesPlanTratamiento(con, dto, codigoSuperficie);
	}

	/**
	 * 
	 * Metodo para elimianr
	 * @param con
	 * @param codigoPkPresupuesto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static boolean eliminarXPresupuesto(Connection con, BigDecimal codigoPkPresupuesto)
	{
		DtoPresupuestoPlanTratamientoNumeroSuperficies dto= new DtoPresupuestoPlanTratamientoNumeroSuperficies();
		dto.setPresupuesto(codigoPkPresupuesto);
		ArrayList<DtoPresupuestoPlanTratamientoNumeroSuperficies> listaEncabezado= busquedaEncabezado(con, dto);
		
		for(DtoPresupuestoPlanTratamientoNumeroSuperficies enca: listaEncabezado)
		{	
			if(!eliminarEncabezado(con, enca.getCodigoPk()))
			{
				Log4JManager.error("NO ELIMINA ENCABEZADO -->"+enca.getCodigoPk());
				return false;
			}
		}	
		return true;
	}
	
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
	public static DtoSuperficiesPorPrograma obtenerSuperficieXProgramaPlanTratamiento(Connection con, DtoSuperficiesPorPrograma dtoBusqueda)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroSuperficiesPresupuestoDao().obtenerSuperficieXProgramaPlanTratamiento(con, dtoBusqueda);
	}
	
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
	public static DtoProgHallazgoPieza obtenerProgramaHallazgoPiezaPlanTratamiento(Connection con, DtoProgHallazgoPieza dtoBusqueda)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroSuperficiesPresupuestoDao().obtenerProgramaHallazgoPiezaPlanTratamiento(con, dtoBusqueda);
	}
	
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
	public static boolean guardarSuperficiesPlanTratamiento(Connection con, DtoSuperficiesPorPrograma dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroSuperficiesPresupuestoDao().guardarSuperficiesPlanTratamiento(con, dto);
	}
}
