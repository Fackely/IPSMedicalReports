/*
 * Aug 22, 2007
 * Proyect axioma
 * Paquete com.princetonsa.dao.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public interface AnulacionCargosFarmaciaDao 
{

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public abstract HashMap<String, Object> consultarSolicitudes(Connection con, String subCuenta);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param subCuenta 
	 * @return
	 */
	public abstract HashMap<String, Object> consultarDetalleSolicitudes(Connection con, String numeroSolicitud, String subCuenta,HashMap<String, Object> ordenes);

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param numeroSolicitud
	 * @param motivoAnulacion
	 * @param detalleSolicitudes
	 * @param usuario 
	 * @param codigoCentroCosto 
	 * @param institucion 
	 * @return
	 */
	public abstract boolean guardarAnulacion(Connection con, String subCuenta, String numeroSolicitud, String motivoAnulacion, HashMap<String, Object> detalleSolicitudes, String usuario, String codigoCentroCosto, int institucion,String estado,HashMap<String, Object> detalleSolicitudesOrdenes);
	
	/**
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public  HashMap<String, Object> consultarSolicitudesDetalle(Connection con, String subCuenta);

}
