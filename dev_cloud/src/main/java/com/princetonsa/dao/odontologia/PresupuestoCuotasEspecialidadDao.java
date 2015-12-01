package com.princetonsa.dao.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoPresupuestoCuotasEspecialidad;

/**
 * 
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 6:18:23 PM
 */
public interface PresupuestoCuotasEspecialidadDao 
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
	public BigDecimal insertar(Connection con, DtoPresupuestoCuotasEspecialidad dto);
	
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
	public boolean eliminar(Connection con, BigDecimal codigoPkPresupuestoContratado);
	
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
	public ArrayList<DtoPresupuestoCuotasEspecialidad> cargar(Connection con, DtoPresupuestoCuotasEspecialidad dto);
	
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
	public DtoPresupuestoCuotasEspecialidad proponerCargar(Connection con, int especialidad, int institucion);
	
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
	public ArrayList<Integer> cargarEspecialidadesProgramaPresupuesto(Connection con, BigDecimal codigoPkPresupuesto);
	
}
