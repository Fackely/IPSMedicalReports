package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

public interface ConsultaDevolucionPedidosDao
{
	
	/**
	 * Metodo de consulta de los centros de costo
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> consultaCentroCosto (Connection con, int centroAtencion);
	
	/**
	 * Metodo de consulta de los estados Devolucion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> consultaEstadosDevolucion (Connection con);
	
	/**
	 * Metodo de consulta de los Motivos Devolucion
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> consultaMotivosDevolucion (Connection con);
	
	/**
	 * Metodo de consulta de las Devoluciones
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> consultaDevoluciones (Connection con, String numeroDevolucion, String centroAtencion, String almacen, String centroCosto, String fechaIni, String fechaFin, String estadoDevolucion, String check, String motivoDevolucion, String usuarioDevuelve, String usuarioRecibe);
	
	/**
	 * Metodo de consulta de los Detalles de las Devoluciones
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> consultaDetalleDevoluciones (Connection con, String codigoDevolucion);
}