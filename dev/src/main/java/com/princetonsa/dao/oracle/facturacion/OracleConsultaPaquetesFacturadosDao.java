package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.ConsultaPaquetesFacturadosDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseConsultaPaquetesFacturadosDao;

public class OracleConsultaPaquetesFacturadosDao implements
		ConsultaPaquetesFacturadosDao 
		
{

	
	/**
	 * 
	 */
	public HashMap consultarPaquetesFacturados(Connection con, String codigoConvenio, String codigoPaquete, String fechaInicial, String fechaFinal) 
	{
		return SqlBaseConsultaPaquetesFacturadosDao.consultarPaquetesFacturados(con, codigoConvenio, codigoPaquete, fechaInicial, fechaFinal);
	}
	
	/**
	 * 
	 */
	public HashMap consultarDetallePaquetes(Connection con, String detalleFactura) 
	{
		return SqlBaseConsultaPaquetesFacturadosDao.consultarDetallePaquetes(con, detalleFactura);
	}
	
	/**
	 * 
	 */
	public HashMap consultarAsociosCirugia(Connection con, String solicitud, String servicioCx, String codDetFactura) 
	{
		return SqlBaseConsultaPaquetesFacturadosDao.consultarAsociosCirugia(con, solicitud, servicioCx, codDetFactura);
	}
	
	
}
