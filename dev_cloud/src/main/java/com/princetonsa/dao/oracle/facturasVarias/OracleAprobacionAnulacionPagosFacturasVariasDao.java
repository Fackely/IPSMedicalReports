package com.princetonsa.dao.oracle.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturasVarias.AprobacionAnulacionPagosFacturasVariasDao;
import com.princetonsa.dao.sqlbase.facturasVarias.SqlBaseAprobacionAnulacionPagosFacturasVariasDao;

/**
 * Fecha: Abril de 2008
 * @author axioma
 * Mauricio Jaramillo H.
 */

public class OracleAprobacionAnulacionPagosFacturasVariasDao implements AprobacionAnulacionPagosFacturasVariasDao 
{

	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap consultarAplicacionesPagosFacturasVarias(Connection con, int codigoInstitucionInt)
    {
        return SqlBaseAprobacionAnulacionPagosFacturasVariasDao.consultarAplicacionesPagosFacturasVarias(con, codigoInstitucionInt);
    }
	
	/**
	 * 
	 */
	public boolean modificarAnulado(Connection con, HashMap vo)
	{
		return SqlBaseAprobacionAnulacionPagosFacturasVariasDao.modificarAnulado(con, vo);
	}
	
	/**
	 * 
	 */
	public boolean modificarAprobado(Connection con, HashMap vo)
	{
		return SqlBaseAprobacionAnulacionPagosFacturasVariasDao.modificarAprobado(con, vo);
	}
	
	/**
	 * 
	 */
	public boolean modificarAprobadoFacturas(Connection con, HashMap vo)
	{
		return SqlBaseAprobacionAnulacionPagosFacturasVariasDao.modificarAprobadoFacturas(con, vo);
	}
	
	/**
	 * 
	 */
	public HashMap busquedaAvanzada(Connection con, HashMap vo)
	{
		return SqlBaseAprobacionAnulacionPagosFacturasVariasDao.busquedaAvanzada(con, vo);
	}
	
	/**
	 * 
	 */
	public HashMap busquedaFacturasDeudores(Connection con, int codigoDeudor, int codigoAplicacionPago)
	{
		return SqlBaseAprobacionAnulacionPagosFacturasVariasDao.busquedaFacturasDeudores(con, codigoDeudor, codigoAplicacionPago);
	}
	
}
