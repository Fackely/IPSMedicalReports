package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jllo
 * Fecha Junio de 2008
 */

public interface ConsolidadoFacturacionDao
{

	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarConsolidadoFacturacion(Connection con, HashMap vo);

	/**
	 * @param con
	 * @param criterios
	 */
	public abstract void insertarLog(Connection con, HashMap criterios);

	/**
	 * 
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param viaIngreso
	 * @param convenio
	 * @param contrato
	 * @return
	 */
	public abstract String obtenerCondiciones(String facturaInicial,
			String facturaFinal, String fechaInicial, String fechaFinal,
			String viaIngreso, String convenio, String contrato);

}
