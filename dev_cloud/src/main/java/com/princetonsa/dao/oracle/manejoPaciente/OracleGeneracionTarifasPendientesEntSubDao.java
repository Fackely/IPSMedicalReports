package com.princetonsa.dao.oracle.manejoPaciente;
import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.GeneracionTarifasPendientesEntSubDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseGeneracionTarifasPendientesEntSubDao;


/**
 * @author Julio Hernandez jfhernandez@axioma-md.com
 */
@SuppressWarnings("unchecked")
public class OracleGeneracionTarifasPendientesEntSubDao implements GeneracionTarifasPendientesEntSubDao {
	
	
	public HashMap buscarAutorizaciones(Connection con, HashMap filtros)
	{
		return SqlBaseGeneracionTarifasPendientesEntSubDao.buscarAutorizaciones(con, filtros);
	}
	
	public HashMap buscarServiciosAutorizacion(Connection con, String autorizacion)
	{
		return SqlBaseGeneracionTarifasPendientesEntSubDao.generarTarifaServicio(con, autorizacion);
	}
	
	public HashMap obtenerErroresServicioAut(Connection con, String autorizacion)
	{
		return SqlBaseGeneracionTarifasPendientesEntSubDao.obtenerErroresServicioAut(con, autorizacion);
	}
	
	public HashMap buscarArticulosPedidos(Connection con, HashMap filtros)
	{
		return SqlBaseGeneracionTarifasPendientesEntSubDao.buscarArticulosPedidos(con, filtros);
	}
	
	public HashMap buscarArticulosSolicitudes(Connection con, HashMap filtros)
	{
		return SqlBaseGeneracionTarifasPendientesEntSubDao.buscarArticulosSolicitudes(con, filtros);
	}
	
	public HashMap obtenerErroresPedidos(Connection con, String codigo)
	{
		return SqlBaseGeneracionTarifasPendientesEntSubDao.obtenerErroresPedidos(con, codigo);
	}
	
	public HashMap obtenerErroresSolicitudes(Connection con, String codigo)
	{
		return SqlBaseGeneracionTarifasPendientesEntSubDao.obtenerErroresSolicitudes(con, codigo);
	}
	
	public HashMap obtenerServiciosTarifados(Connection con,HashMap servicios)
	{
		return SqlBaseGeneracionTarifasPendientesEntSubDao.obtenerServiciosTarifados(con, servicios);
	}
	
	public HashMap obtenerPedidosTarifados(Connection con, HashMap pedidos)
	{
		return SqlBaseGeneracionTarifasPendientesEntSubDao.obtenerPedidosTarifados(con, pedidos);
	}
	
	public HashMap obtenerSolicitudesTarifados(Connection con, HashMap solicitudes)
	{
		return SqlBaseGeneracionTarifasPendientesEntSubDao.obtenerSolicitudesTarifados(con, solicitudes);
	}
}