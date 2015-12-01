package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.ConsultaCierreAperturaIngresoDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseConsultaCierreAperturaIngresoDao;

/**
 * @author Mauricio Jllo
 * Fecha: Mayo de 2008
 */

public class OracleConsultaCierreAperturaIngresoDao implements ConsultaCierreAperturaIngresoDao
{

	/**
	 * 
	 */
	public HashMap consultarCierreAperturaIngresos(Connection con, HashMap vo)
    {
        return SqlBaseConsultaCierreAperturaIngresoDao.consultarCierreAperturaIngresos(con, vo);
    }
	
	/**
	 * 
	 */
	public HashMap detalleCierreAperturaIngreso(Connection con, int codigoCierreApertura, String motivo)
    {
        return SqlBaseConsultaCierreAperturaIngresoDao.detalleCierreAperturaIngreso(con, codigoCierreApertura, motivo);
    }
	
}
