package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public interface PacientesConAtencionDao
{

	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarPacientesConAtencion(Connection con, HashMap vo);

}
