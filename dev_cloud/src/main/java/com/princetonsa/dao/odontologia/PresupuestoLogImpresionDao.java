/**
 * 
 */
package com.princetonsa.dao.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;

import com.princetonsa.dto.odontologia.DtoLogImpresionPresupuesto;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 6:25:06 PM
 */
public interface PresupuestoLogImpresionDao 
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
	public BigDecimal insertar(Connection con, DtoLogImpresionPresupuesto dto);
	
}
