package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.ConsultaDevolucionPedidosDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseConsultaDevolucionPedidosDao;

public class OracleConsultaDevolucionPedidosDao implements ConsultaDevolucionPedidosDao
{
	public HashMap<String, Object> consultaCentroCosto (Connection con, int centroAtencion)
	{
		return SqlBaseConsultaDevolucionPedidosDao.consultaCentroCosto(con, centroAtencion);
	}
	
	public HashMap<String, Object> consultaEstadosDevolucion (Connection con)
	{
		return SqlBaseConsultaDevolucionPedidosDao.consultaEstadosDevolucion(con);
	}
	
	public HashMap<String, Object> consultaMotivosDevolucion (Connection con)
	{
		return SqlBaseConsultaDevolucionPedidosDao.consultaMotivosDevolucion(con);
	}
	
	public HashMap<String, Object> consultaDevoluciones (Connection con, String numeroDevolucion, String centroAtencion, String almacen, String centroCosto, String fechaIni, String fechaFin, String estadoDevolucion, String check, String motivoDevolucion, String usuarioDevuelve, String usuarioRecibe)
	{
		return SqlBaseConsultaDevolucionPedidosDao.consultaDevoluciones(con, numeroDevolucion, centroAtencion, almacen, centroCosto, fechaIni, fechaFin, estadoDevolucion, check, motivoDevolucion, usuarioDevuelve, usuarioRecibe);
	}
	
	public HashMap<String, Object> consultaDetalleDevoluciones (Connection con, String codigoDevolucion)
	{
		return SqlBaseConsultaDevolucionPedidosDao.consultaDetalleDevoluciones(con, codigoDevolucion);
	}
}