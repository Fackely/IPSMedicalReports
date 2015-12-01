package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

public interface PendienteFacturarDao 
{
	
	/**
	 * 
	 * @param con
	 * @param fechaCorte
	 * @param medico
	 * @return
	 */
	HashMap consultarHonorariosPendientes(Connection con, String fechaCorte, String medico);

}
