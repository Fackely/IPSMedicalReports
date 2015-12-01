/**
 * 
 */
package com.princetonsa.dao.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoPresupuestoContratado;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 6:09:30 PM
 */
public interface PresupuestoContratadoDao 
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
	public BigDecimal insertar(Connection con, DtoPresupuestoContratado dto);
	
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
	public boolean eliminar(Connection con, DtoPresupuestoContratado dto);
	
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
	public ArrayList<DtoPresupuestoContratado> cargar(Connection con, DtoPresupuestoContratado dto);
}
