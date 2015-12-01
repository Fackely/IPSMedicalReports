package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.AutorizarAnulacionFacturasDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseAutorizarAnulacionFacturasDao;

public class OracleAutorizarAnulacionFacturasDao implements
		AutorizarAnulacionFacturasDao 

{

	
	/**
	 * 
	 */
	public HashMap consultarListadoSolicitudes(Connection con, String loginUsuario, int centroAtencion, int codigoInstitucion) 
	{
		return SqlBaseAutorizarAnulacionFacturasDao.consultarListadoSolicitudes(con, loginUsuario, centroAtencion, codigoInstitucion);
	}

	/**
	 * 
	 */
	public HashMap consultarDetalleSolicitud(Connection con, String codigoSolicitud) 
	{
		return SqlBaseAutorizarAnulacionFacturasDao.consultarDetalleSolicitud(con, codigoSolicitud);
	}

	/**
	 * 
	 */
	public HashMap consultarResumenAutorizar(Connection con, String codigoSolicitud) 
	{
		return SqlBaseAutorizarAnulacionFacturasDao.consultarResumenAutorizar(con, codigoSolicitud);
	}

	/**
	 * 
	 */
	public boolean insertarAutorizacion(Connection con, HashMap vo) 
	{
		return SqlBaseAutorizarAnulacionFacturasDao.insertarAutorizacion(con, vo);
	}

	/**
	 * 
	 */
	public HashMap consultarListadoAprobadas(Connection con, String fechaInicial, String fechaFinal, String consecutivoFactura, String numeroAutorizacion, String usuarioAutoriza) 
	{
		return SqlBaseAutorizarAnulacionFacturasDao.consultarListadoAprobadas(con, fechaInicial, fechaFinal, consecutivoFactura, numeroAutorizacion, usuarioAutoriza);
	}

	/**
	 * 
	 */
	public boolean insertarAnulacionAutorizacion(Connection con, HashMap vo) 
	{
		return SqlBaseAutorizarAnulacionFacturasDao.insertarAnulacionAutorizacion(con, vo);
	}

	/**
	 * 
	 */
	public HashMap consultaListadoAutorizaciones(Connection con, String fechaInicial, String fechaFinal, String consecutivoFactura, String estadoAutorizacion, String usuarioAutoriza, String motivoAnulacion, String centroAtencion) 
	{
		return SqlBaseAutorizarAnulacionFacturasDao.consultaListadoAutorizaciones(con, fechaInicial, fechaFinal, consecutivoFactura, estadoAutorizacion, usuarioAutoriza, motivoAnulacion, centroAtencion);
	}

	/**
	 * 
	 */
	public HashMap consultaDetalleAutorizacion(Connection con, String numeroSolicitud) 
	{
		return SqlBaseAutorizarAnulacionFacturasDao.consultaDetalleAutorizacion(con, numeroSolicitud);
	}

	
	
}
