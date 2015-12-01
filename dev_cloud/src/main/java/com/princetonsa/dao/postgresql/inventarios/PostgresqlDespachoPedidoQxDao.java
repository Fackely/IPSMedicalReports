/*
 * Dic 05, 2007
 */
package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.DespachoPedidoQxDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseDespachoPedidoQxDao;

/**
 * 
 * @author Sebastián Gómez R.
 * Clase que implementa los metodos
 * para accede a la BD Postgres
 *
 */
public class PostgresqlDespachoPedidoQxDao implements DespachoPedidoQxDao 
{
	/**
	 * Método que consulta las peticiones para realizar el despacho de pedidos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarPeticiones(Connection con,HashMap campos)
	{
		return SqlBaseDespachoPedidoQxDao.consultarPeticiones(con, campos);
	}
	
	/**
	 * Método que consultas los articulos de todos los pedidos de la peticion
	 * @param con
	 * @param numeroPeticion
	 * @param listadoPedidos
	 * @return
	 */
	public HashMap consultaArticulosPedidosPeticion(Connection con,int numeroPeticion,int codigoFarmacia,String listadoPedidos)
	{
		return SqlBaseDespachoPedidoQxDao.consultaArticulosPedidosPeticion(con, numeroPeticion,codigoFarmacia,listadoPedidos);
	}
	
	/**
	 * Método implementado para cargar los pedidos de la peticion con su detalle
	 * @param con
	 * @param numeroPeticion
	 * @param codigoFarmacia
	 * @param listadoPedidos
	 * @return
	 */
	public HashMap consultaPedidosPeticion(Connection con,int numeroPeticion,int codigoFarmacia,String listadoPedidos)
	{
		return SqlBaseDespachoPedidoQxDao.consultaPedidosPeticion(con, numeroPeticion, codigoFarmacia, listadoPedidos);
	}
}
