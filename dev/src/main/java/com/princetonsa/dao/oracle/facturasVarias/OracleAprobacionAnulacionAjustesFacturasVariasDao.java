package com.princetonsa.dao.oracle.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturasVarias.AprobacionAnulacionAjustesFacturasVariasDao;
import com.princetonsa.dao.sqlbase.facturasVarias.SqlBaseAprobacionAnulacionAjustesFacturasVariasDao;

/**
 * @author Mauricio Jaramillo H.
 * Fecha: Agosto de 2008
 */

public class OracleAprobacionAnulacionAjustesFacturasVariasDao implements AprobacionAnulacionAjustesFacturasVariasDao
{

	/**
	 * 
	 */
	public HashMap consultarAjustesFacturasVarias(Connection con, int codigoInstitucion) 
	{
		return SqlBaseAprobacionAnulacionAjustesFacturasVariasDao.consultarAjustesFacturasVarias(con, codigoInstitucion);
	}
	
	/**
	 * 
	 */
	public boolean actualizarAprobacionAnulacionAjusteFacturasVarias(Connection con, HashMap criterios)
	{
		return SqlBaseAprobacionAnulacionAjustesFacturasVariasDao.actualizarAprobacionAnulacionAjusteFacturasVarias(con, criterios);
	}
	
	/**
	 * 
	 */
	public boolean generarLogAprobacionAnulacion(Connection con, HashMap criterios)
	{
		return SqlBaseAprobacionAnulacionAjustesFacturasVariasDao.generarLogAprobacionAnulacion(con, criterios);
	}
	
	/**
	 * 
	 */
	public String obtenerWhere(HashMap criterios)
	{
		return SqlBaseAprobacionAnulacionAjustesFacturasVariasDao.obtenerWhere(criterios);
	}
	
}