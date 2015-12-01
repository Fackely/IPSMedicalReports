package com.princetonsa.dao.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jllo
 * Fecha: Agosto de 2008
 */
public interface ConsultaMovimientoDeudorDao
{

	/**
	 * Método que consulta los movimientos de Deudores
	 * @param con
	 * @param criterios
	 * @return
	 */
	public HashMap consultarMovimientosDeudores(Connection con, HashMap criterios);

	/**
 	 * Método que consulta las condiciones establecidas 
	 * para la consulta de movimientos de deudor para
	 * generar el reporte
	 * @param con
	 * @param criterios
	 * @return
	 */
	public String consultarCondicionesMovimientosDeudor(Connection con, HashMap criterios);

	/**
	 * Método que consulta el detallle de los 
	 * movimientos de un deudor seleccionado
	 * @param con
	 * @param deudor
	 * @return
	 */
	public HashMap consultarDetalleMovimientosDeudor(Connection con, int deudor, String fechaInicial, String fechaFinal);

}
