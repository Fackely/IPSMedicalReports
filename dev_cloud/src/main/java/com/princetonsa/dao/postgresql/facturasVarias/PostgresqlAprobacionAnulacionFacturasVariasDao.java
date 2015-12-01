package com.princetonsa.dao.postgresql.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturasVarias.AprobacionAnulacionFacturasVariasDao;
import com.princetonsa.dao.sqlbase.facturasVarias.SqlBaseAprobacionAnulacionFacturasVariasDao;

public class PostgresqlAprobacionAnulacionFacturasVariasDao implements
		AprobacionAnulacionFacturasVariasDao 

{

	/**
	 * 
	 */
	public HashMap consultarFacturasVarias(Connection con) 
	{
		return SqlBaseAprobacionAnulacionFacturasVariasDao.consultarFacturasVarias(con);
	}
	
	/**
	 * 
	 */
	public HashMap busquedaFacturas(Connection con,
			String fechaInicial,
			String fechaFinal,
			String factura,
			String estado,
			String tipoDeudor, 
			String deudor,
			String centroAtencion) 
	{
		return SqlBaseAprobacionAnulacionFacturasVariasDao.busquedaFacturas(
				con, 
				fechaInicial,
				fechaFinal,
				factura,
				estado,
				tipoDeudor, 
				deudor,
				centroAtencion);
	}

	
	public boolean modificar(Connection con, HashMap vo)
	{
		return SqlBaseAprobacionAnulacionFacturasVariasDao.modificar(con, vo);
	}
	
	/**
	 * indica si una multa de la factura varia esta asociada a otra factura
	 * @param Connection con
	 * @param HashMap mapa
	 * */
	public boolean esMultaAsociadaOtraFactura(Connection con,HashMap mapa)
	{
		return SqlBaseAprobacionAnulacionFacturasVariasDao.esMultaAsociadaOtraFactura(con, mapa);
	}
	
	/**
	 * indica si la factura varia tiene asociado aplicaciones de pago y consecutivos de pagos
	 * @param Connection con
	 * @param HashMap mapa
	 * */
	public boolean esAsociadaPagos(Connection con,HashMap mapa)
	{
		return SqlBaseAprobacionAnulacionFacturasVariasDao.esAsociadaPagos(con, mapa);
	}
	
	/**
	 * actualiza las multas dependientes de una factura varia
	 * @param Connection con
	 * @param HashMap mapa
	 * */
	public boolean actualizarEstadoMultasXFacturaVaria(Connection con,HashMap mapa)
	{
		return SqlBaseAprobacionAnulacionFacturasVariasDao.actualizarEstadoMultasXFacturaVaria(con, mapa);
	}
	
	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public HashMap consultarFacturaVaria(Connection con,HashMap parametros)
	{
		return SqlBaseAprobacionAnulacionFacturasVariasDao.consultarFacturaVaria(con, parametros);
	}

	@Override
	public boolean actualizarEstadoFacturaVentaTarjeta(Connection con,
			String codigoFactua, String fechaAnulacion, String estado,
			String usuario, String hora) {
		
		return SqlBaseAprobacionAnulacionFacturasVariasDao.actualizarEstadoFacturaVentaTarjeta(con, codigoFactua, fechaAnulacion, estado, usuario, hora);
	}

	
}