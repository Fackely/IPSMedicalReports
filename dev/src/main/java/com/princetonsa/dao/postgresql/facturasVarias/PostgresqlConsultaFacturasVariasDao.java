package com.princetonsa.dao.postgresql.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturasVarias.ConsultaFacturasVariasDao;
import com.princetonsa.dao.sqlbase.facturasVarias.SqlBaseConsultaFacturasVariasDao;


public class PostgresqlConsultaFacturasVariasDao implements
		ConsultaFacturasVariasDao
		
{
	
	/**
	 * 
	 */
	public HashMap BusquedaFacturas(Connection con, String fechaInicial, String fechaFinal, String factura, String estadosFactura, String tipoDeudor, String deudor) 
	{
		return SqlBaseConsultaFacturasVariasDao.BusquedaFacturas(con, fechaInicial, fechaFinal, factura, estadosFactura, tipoDeudor, deudor);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultaDetalleFactura(Connection con,int factura, String tipoDeudor) 
	{
		return SqlBaseConsultaFacturasVariasDao.consultaDetalleFactura(con, factura, tipoDeudor);
	}
	
}
