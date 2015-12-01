package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.ConsolidadoFacturacionDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseConsolidadoFacturacionDao;

/**
 * @author Mauricio Jllo
 * Fecha Junio de 2008
 */

public class OracleConsolidadoFacturacionDao implements ConsolidadoFacturacionDao
{
	/**
	 * Cadena con el Group By de la Consulta de Consolidados de Facturacion
	 */
	private static String strOrdConsolidadoFacturacion = " GROUP BY " +
															"f.convenio, sc.contrato, f.via_ingreso, f.consecutivo_factura, f.fecha, f.cod_paciente, f.estrato_social, sc.nro_carnet, f.valor_convenio, f.valor_bruto_pac, f.valor_total " +
														"ORDER BY " +
															"getnombreconvenio(f.convenio), getnumerocontrato(sc.contrato), getnombreviaingreso(f.via_ingreso), f.consecutivo_factura, fecha";
	
	/**
	 * 
	 */
	public HashMap consultarConsolidadoFacturacion(Connection con, HashMap criterios)
	{
		return SqlBaseConsolidadoFacturacionDao.consultarConsolidadoFacturacion(con, criterios, strOrdConsolidadoFacturacion);
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
