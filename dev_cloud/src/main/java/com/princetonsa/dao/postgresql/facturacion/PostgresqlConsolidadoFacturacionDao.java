package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.ConsolidadoFacturacionDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseConsolidadoFacturacionDao;

/**
 * @author Mauricio Jllo
 * Fecha Junio de 2008
 */

public class PostgresqlConsolidadoFacturacionDao implements ConsolidadoFacturacionDao
{
	/**
	 * Cadena con el Group By de la Consulta de Consolidados de Facturacion
	 */
	private static String strOrdConsolidadoFacturacion = " GROUP BY " +
															"f.convenio, sc.contrato, viaingreso, factura, fecha, f.cod_paciente, estrato, ficha, valorconvenio, valorpaciente, valortotal " +
														"ORDER BY " +
															"nomconvenio, numcontrato, viaingreso, factura, fecha";
	
	
	
	/**
	 * 
	 */
	public HashMap consultarConsolidadoFacturacion(Connection con, HashMap vo)
	{
		return SqlBaseConsolidadoFacturacionDao.consultarConsolidadoFacturacion(con, vo, strOrdConsolidadoFacturacion);
	}
	
	/**
	 * 
	 */
	public void insertarLog(Connection con, HashMap criterios)
	{
		SqlBaseConsolidadoFacturacionDao.insertarLog(con, criterios);
	}

	@Override
	public String obtenerCondiciones(String facturaInicial,
			String facturaFinal, String fechaInicial, String fechaFinal,
			String viaIngreso, String convenio, String contrato) {
		
		return SqlBaseConsolidadoFacturacionDao.obtenerCondiciones( facturaInicial,
				 facturaFinal,  fechaInicial,  fechaFinal,
				 viaIngreso,  convenio,  contrato, strOrdConsolidadoFacturacion);
	}
	
}