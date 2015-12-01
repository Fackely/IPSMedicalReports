/**
 * 
 */
package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoLogImpresionPresupuesto;

/**
 * 
 * @author Wilson Rios 
 *
 * Jun 6, 2010 - 9:03:21 AM
 */
public class PresupuestoLogImpresion 
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
	public static BigDecimal insertar(Connection con, DtoLogImpresionPresupuesto dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPresupuestoLogImpresionDao().insertar(con, dto);
	}
	
}
