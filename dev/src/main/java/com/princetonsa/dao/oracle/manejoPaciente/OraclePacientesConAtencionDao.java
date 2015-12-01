package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.PacientesConAtencionDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBasePacientesConAtencionDao;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class OraclePacientesConAtencionDao implements PacientesConAtencionDao
{

	/**
	 * 
	 */
	public HashMap consultarPacientesConAtencion(Connection con, HashMap vo)
    {
        return SqlBasePacientesConAtencionDao.consultarPacientesConAtencion(con, vo);
    }
	
}
