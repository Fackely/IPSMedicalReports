package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

public interface ConsumosCentrosCostoDao
{
	
	/**
	 * Metodo de consulta de los centros de costo
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> consultaCentroCosto (Connection con, int centroAtencion);
	
	/**
	 * Metodo de consulta de las Clases
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap<String, Object> consultaClases (Connection con);
	
	/**
	 * Metodo de consulta de los Pedidos
	 * @return
	 */
	public HashMap<String, Object> consultaPedidos (Connection con, String fechaInicial, String fechaFinal, int centroAtencion, String almacen, String centroCosto, String clase);
	
}