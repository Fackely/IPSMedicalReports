package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.SolicitarAnulacionFacturasDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseSolicitarAnulacionFacturasDao;

public class OracleSolicitarAnulacionFacturasDao implements
		SolicitarAnulacionFacturasDao 
		
{

	
	/**
	 * 
	 */
	public HashMap consultarFactura(Connection con, String consecutivoFactura, int codigoInstitucion) 
	{
		return SqlBaseSolicitarAnulacionFacturasDao.consultarFactura(con, consecutivoFactura, codigoInstitucion);
	}

	/**
	 * 
	 */
	public int insertarSolictud(Connection con, HashMap vo) 
	{
		return SqlBaseSolicitarAnulacionFacturasDao.insertarSolictud(con, vo);
	}

	/**
	 * 
	 */
	public HashMap consultarResumenSolictud(Connection con, int codigoSolicitud) 
	{
		return SqlBaseSolicitarAnulacionFacturasDao.consultarResumenSolictud(con, codigoSolicitud);
	}

	/**
	 * 
	 */
	public HashMap consultarSolictudesAnular(Connection con, String fechaInicial, String fechaFinal, String consecutivoFactura, String codigoSolicitud, String usuarioAutoriza, String motivoAnulacion, String centroAtencion) 
	{
		return SqlBaseSolicitarAnulacionFacturasDao.consultarSolictudesAnular(con, fechaInicial, fechaFinal, consecutivoFactura, codigoSolicitud, usuarioAutoriza, motivoAnulacion, centroAtencion);
	}

	/**
	 * 
	 */
	public HashMap consultaDetalleSolicitud(Connection con,	String numeroSolicitud) 
	{
		return SqlBaseSolicitarAnulacionFacturasDao.consultaDetalleSolicitud(con, numeroSolicitud);
	}

	/**
	 * 
	 */
	public boolean insertarAnulacionSolicitud(Connection con, HashMap vo) 
	{
		return SqlBaseSolicitarAnulacionFacturasDao.insertarAnulacionSolicitud(con, vo);
	}

	
	
}
