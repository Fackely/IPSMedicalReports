/*
 * Aug 22, 2007
 * Proyect axioma
 * Paquete com.princetonsa.dao.postgresql.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.AnulacionCargosFarmaciaDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseAnulacionCargosFarmaciaDao;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class PostgresqlAnulacionCargosFarmaciaDao implements AnulacionCargosFarmaciaDao 
{

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public HashMap<String, Object> consultarSolicitudes(Connection con, String subCuenta)
	{
		return SqlBaseAnulacionCargosFarmaciaDao.consultarSolicitudes(con,subCuenta);
	}
	
	/**
	 * 
	 */
	public HashMap<String, Object> consultarDetalleSolicitudes(Connection con, String numeroSolicitud,String subCuenta,HashMap<String, Object> ordenes)
	{
		return SqlBaseAnulacionCargosFarmaciaDao.consultarDetalleSolicitudes(con,numeroSolicitud,subCuenta,ordenes);
	}

	/**
	 * 
	 */
	public boolean guardarAnulacion(Connection con, String subCuenta, String numeroSolicitud, String motivoAnulacion, HashMap<String, Object> detalleSolicitudes,String usuario, String codigoCentroCosto, int institucion,String estado,HashMap<String, Object> detalleSolicitudesOrdenes) 
	{
		return SqlBaseAnulacionCargosFarmaciaDao.guardarAnulacion(con,subCuenta,numeroSolicitud,motivoAnulacion,detalleSolicitudes,usuario,codigoCentroCosto,institucion,estado,detalleSolicitudesOrdenes);
	}
	
	/**
	 * @see com.princetonsa.dao.facturacion.AnulacionCargosFarmaciaDao#consultarSolicitudesDetalle(java.sql.Connection, java.lang.String)
	 */
	public  HashMap<String, Object> consultarSolicitudesDetalle(Connection con, String subCuenta){
		return SqlBaseAnulacionCargosFarmaciaDao.consultarSolicitudesDetalle(con, subCuenta);
	}
}
