/*
 * Marzo 28, del 2007
 */
package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * de consulta articulos x fecha de vencimiento
 */
public interface ArticulosFechaVencimientoDao 
{
	/**
	 * Método implementado para consultar las existencias de una articulo por fecha de vencimiento
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarArticulosXFecha(Connection con,HashMap campos);
	
	/**
	 * Método implementado para consultar la impresion de las existencias de una articulo por fecha de vencimiento y por almacen
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultaImpresionArticulosXFecha(Connection con,HashMap campos);
}
