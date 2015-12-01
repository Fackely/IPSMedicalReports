package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.actionform.inventarios.MovimientosAlmacenConsignacionForm;
import com.princetonsa.mundo.inventarios.MovimientosAlmacenConsignacion;

/**
 * Anexo 684
 * Creado el 9 de Octubre de 2008
 * @author Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */

public interface MovimientosAlmacenConsignacionDao 
{

	/**
	 * Método encargando de consultar las transacciones
	 * @param con
	 * @return
	 */
	public HashMap consultarTransacciones(Connection con, int codInstitucion);
	
	/**
	 * Método encargado de consultar los proveedores
	 * @param con
	 * @return
	 */
	public HashMap consultarProveedores(Connection con, String proveedor);
	
	/**
	 * Método encargado de consultar los movimientos
	 * @param con
	 * @param criterios
	 * @return
	 */
	public HashMap consultarMovimientos(Connection con, MovimientosAlmacenConsignacionForm forma, MovimientosAlmacenConsignacion mundo, HashMap criterios);

	/**
	 * @param con
	 * @param criterios
	 */
	public void insertarLog(Connection con, HashMap criterios);

	/**
	 * Método encargado de obtener la consulta
	 * @param con
	 * @param forma
	 * @param mundo 
	 * @param codigoAImprimir 
	 * @param bandera 
	 * @return
	 */
	public String obtenerConsulta(Connection con,MovimientosAlmacenConsignacionForm forma, MovimientosAlmacenConsignacion mundo, String codigoAImprimir, int bandera);

}