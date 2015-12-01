package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jllo
 * Fecha: Mayo de 2008
 */

public interface ConsultaCierreAperturaIngresoDao
{

	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarCierreAperturaIngresos(Connection con, HashMap vo);
	
	/**
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public abstract HashMap detalleCierreAperturaIngreso(Connection con, int codigoCierreApertura, String motivo);

}
