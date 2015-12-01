package com.princetonsa.dao.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

public interface AprobacionAnulacionFacturasVariasDao 
{
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	HashMap consultarFacturasVarias(Connection con);
	
	
	public HashMap busquedaFacturas(Connection con,
			String fechaInicial,
			String fechaFinal,
			String factura,
			String estado,
			String tipoDeudor, 
			String deudor,
			String centroAtencion) ;

	
	public abstract boolean modificar(Connection con,HashMap vo);
	
	/**
	 * indica si una multa de la factura varia esta asociada a otra factura
	 * @param Connection con
	 * @param HashMap mapa
	 * */
	public boolean esMultaAsociadaOtraFactura(Connection con,HashMap mapa);
	
	/**
	 * indica si la factura varia tiene asociado aplicaciones de pago y consecutivos de pagos
	 * @param Connection con
	 * @param HashMap mapa
	 * */
	public boolean esAsociadaPagos(Connection con,HashMap mapa);
	
	/**
	 * actualiza las multas dependientes de una factura varia
	 * @param Connection con
	 * @param HashMap mapa
	 * */
	public boolean actualizarEstadoMultasXFacturaVaria(Connection con,HashMap mapa);	
	
	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public HashMap consultarFacturaVaria(Connection con,HashMap parametros);
	
	
	/**
	 * 
	 * @param con
	 * @param codigoFactua
	 * @param fechaAnulacion
	 * @param estado
	 * @param usuario
	 * @param hora
	 * @return
	 */
	public boolean actualizarEstadoFacturaVentaTarjeta(Connection con,  String codigoFactua, String fechaAnulacion, String estado , String usuario, String hora );
}