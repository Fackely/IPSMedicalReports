package com.princetonsa.dao.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jaramillo
 * Fecha: Agosto de 2008
 */

public interface ConsultaMovimientoFacturaDao
{

	/**
	 * M�todo que consulta los movimientos de Deudores por factura 
	 * @param con
	 * @param criterios
	 * @return
	 */
	public HashMap consultarMovimientosFactura(Connection con, HashMap criterios);

	/**
	 * M�todo que consulta las condiciones establecidas 
	 * para la consulta de movimientos de deudor por factura
	 * para generar el reporte
	 * @param con
	 * @param criterios
	 * @return
	 */
	public String consultarCondicionesMovimientosFactura(Connection con, HashMap criterios);

	/**
	 * M�todo que consulta la informaci�n del detalle 
	 * en el encabezado de la factura varia seleccionada
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public HashMap consultarDetalleMovimientosFactura(Connection con, String consecutivoFactura);

	/**
	 * M�tdo que consulta la informaci�n de los ajustes
	 * de la factura varia seleccionada
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public HashMap consultarDetalleAjustesFactura(Connection con, String codigoFactura);

	/**
	 * M�todo que consulta la informaci�n de los pagos
	 * de la factura varia seleccionada
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public HashMap consultarDetallePagosFactura(Connection con, String codigoFactura);

	/**
	 * M�todo que consulta la informaci�n del resumen de
	 * movimientos de la factura varia seleccionada
	 * @param con
	 * @param consecutivoFactura
	 * @return
	 */
	public HashMap consultarDetalleResumenMovimientosFactura(Connection con, String codigoFactura);

}