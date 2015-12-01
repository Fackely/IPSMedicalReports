/*
 * Marzo 28, del 2007
 */
package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.ArticulosFechaVencimientoDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseArticulosFechaVencimientoDao;

/**
 * @author Sebastián Gómez 
 *
 * Clase que maneja los métodos propìos de Postgres para el acceso a la fuente
 * de datos en la funcionalidad de consultar articulos x fecha de vencimiento
 */
public class PostgresqlArticulosFechaVencimientoDao implements
		ArticulosFechaVencimientoDao {
	
	/**
	 * Método implementado para consultar las existencias de una articulo por fecha de vencimiento
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarArticulosXFecha(Connection con,HashMap campos)
	{
		return SqlBaseArticulosFechaVencimientoDao.consultarArticulosXFecha(con, campos);
	}
	
	/**
	 * Método implementado para consultar la impresion de las existencias de una articulo por fecha de vencimiento y por almacen
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultaImpresionArticulosXFecha(Connection con,HashMap campos)
	{
		return SqlBaseArticulosFechaVencimientoDao.consultaImpresionArticulosXFecha(con, campos);
	}

}
