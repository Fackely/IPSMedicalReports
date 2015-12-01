package com.princetonsa.dao.postgresql.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturasVarias.ConsultaMovimientoFacturaDao;
import com.princetonsa.dao.sqlbase.facturasVarias.SqlBaseConsultaMovimientoFacturaDao;

/**
 * @author Mauricio Jaramillo
 * Fecha: Agosto de 2008
 */

public class PostgresqlConsultaMovimientoFacturaDao implements ConsultaMovimientoFacturaDao
{

	/**
	 * 
	 */
	public HashMap consultarMovimientosFactura(Connection con, HashMap criterios) 
	{
		return SqlBaseConsultaMovimientoFacturaDao.consultarMovimientosFactura(con, criterios);
	}
	
	/**
	 * 
	 */
	public String consultarCondicionesMovimientosFactura(Connection con, HashMap criterios)
	{
		return SqlBaseConsultaMovimientoFacturaDao.consultarCondicionesMovimientosFactura(con, criterios);
	}
	
	/**
	 * 
	 */
	public HashMap consultarDetalleMovimientosFactura(Connection con, String consecutivoFactura)
	{
		return SqlBaseConsultaMovimientoFacturaDao.consultarDetalleMovimientosFactura(con, consecutivoFactura);
	}
	
	/**
	 * 
	 */
	public HashMap consultarDetalleAjustesFactura(Connection con, String codigoFactura)
	{
		return SqlBaseConsultaMovimientoFacturaDao.consultarDetalleAjustesFactura(con, codigoFactura);
	}
	
	/**
	 * 
	 */
	public HashMap consultarDetallePagosFactura(Connection con, String codigoFactura)
	{
		return SqlBaseConsultaMovimientoFacturaDao.consultarDetallePagosFactura(con, codigoFactura);
	}
	
	/**
	 * 
	 */
	public HashMap consultarDetalleResumenMovimientosFactura(Connection con, String codigoFactura)
	{
		return SqlBaseConsultaMovimientoFacturaDao.consultarDetalleResumenMovimientosFactura(con, codigoFactura);
	}
	
}