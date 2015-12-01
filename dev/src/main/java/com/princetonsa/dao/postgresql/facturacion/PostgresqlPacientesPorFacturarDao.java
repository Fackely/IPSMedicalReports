package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.PacientesPorFacturarDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBasePacientesPorFacturarDao;

/**
 * @author Mauricio Jaramillo
 * Fecha: Agosto de 2008
 */

public class PostgresqlPacientesPorFacturarDao implements PacientesPorFacturarDao
{

	/**
	 * 
	 */
	public HashMap consultarConsumosPorFacturar(Connection con, HashMap criterios)
	{
		return SqlBasePacientesPorFacturarDao.consultarConsumosPorFacturar(con, criterios);
	}
	
}