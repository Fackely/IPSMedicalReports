/*
 * Dic 05, 2007
 */
package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author Sebastián Gómez R.
 * Interface comunicación con los objetos DAO para
 * acceder a la fuente de datos
 *
 */
public interface DespachoPedidoQxDao 
{
	/**
	 * Método que consulta las peticiones para realizar el despacho de pedidos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarPeticiones(Connection con,HashMap campos);
	
	/**
	 * Método que consultas los articulos de todos los pedidos de la peticion
	 * @param con
	 * @param numeroPeticion
	 * @param listadoPedidos
	 * @return
	 */
	public HashMap consultaArticulosPedidosPeticion(Connection con,int numeroPeticion,int codigoFarmaciam,String listadoPedidos);
	
	/**
	 * Método implementado para cargar los pedidos de la peticion con su detalle
	 * @param con
	 * @param numeroPeticion
	 * @param codigoFarmacia
	 * @return
	 */
	public HashMap consultaPedidosPeticion(Connection con,int numeroPeticion,int codigoFarmaci, String listadoPedidos);
}
