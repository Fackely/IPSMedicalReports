package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

public interface MovimientosAlmacenesDao 
{

	/**
	 * @param con
	 * @return
	 */
	public HashMap consultarTransacciones(Connection con, String entradaSalida);
	
	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public HashMap consultarMovimientos(Connection con, HashMap criterios);

	/**
	 * @param con
	 * @param criterios
	 */
	public void insertarLog(Connection con, HashMap criterios);

	/**
	 * 
	 * @param centroAtencion
	 * @param almacen
	 * @param indicativoES
	 * @param codigoTransacciones
	 * @param transaccionInicial
	 * @param transaccionFinal
	 * @param tipoReporte
	 * @param codigoAImprimir
	 * @return
	 */
	public String obtenerSqlReporte(String centroAtencion, String almacen,
			String indicativoES, String codigoTransacciones,
			String transaccionInicial, String transaccionFinal,
			String tipoReporte, String codigoAImprimir);

}