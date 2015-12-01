package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.PendienteFacturarDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBasePendienteFacturarDao;

public class PostgresqlPendienteFacturarDao implements PendienteFacturarDao 
{
	
	
	/**
	 * 
	 */
	public HashMap consultarHonorariosPendientes(Connection con, String fechaCorte, String medico) 
	{
		return SqlBasePendienteFacturarDao.consultarHonorariosPendientes(con,fechaCorte,medico);
	}

	
	
}
