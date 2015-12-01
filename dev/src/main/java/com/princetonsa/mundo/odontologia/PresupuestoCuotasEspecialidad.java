/**
 * 
 */
package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoPresupuestoCuotasEspecialidad;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 6, 2010 - 9:01:47 AM
 */
public class PresupuestoCuotasEspecialidad 
{

	/**
	 * 
	 * Metodo para insertar
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static BigDecimal insertar(Connection con, DtoPresupuestoCuotasEspecialidad dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoCuotasEspecialidadDao().insertar(con, dto);
	}
	
	/**
	 * 
	 * Metodo para eliminar 
	 * @param con
	 * @param codigoPk
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static boolean eliminar(Connection con, BigDecimal codigoPkPresupuestoContratado)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoCuotasEspecialidadDao().eliminar(con, codigoPkPresupuestoContratado);
	}
	
	
	/**
	 * 
	 * Metodo para cargar 
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public ArrayList<DtoPresupuestoCuotasEspecialidad> cargar(Connection con, DtoPresupuestoCuotasEspecialidad dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoCuotasEspecialidadDao().cargar(con, dto);
	}
	
	
	/**
	 * 
	 * Metodo para proponer los datos que vienen de la parametrica de cuotas 
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static DtoPresupuestoCuotasEspecialidad proponerCargar(Connection con, int especialidad, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoCuotasEspecialidadDao().proponerCargar(con, especialidad, institucion);
	}
	
	/**
	 * 
	 * Metodo para cargar las especialidades de los programas del presupuesto contratado o precontratado
	 * @param con
	 * @param codigoPkPresupuesto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static ArrayList<Integer> cargarEspecialidadesProgramaPresupuesto(Connection con, BigDecimal codigoPkPresupuesto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoCuotasEspecialidadDao().cargarEspecialidadesProgramaPresupuesto(con, codigoPkPresupuesto);
	}
}
