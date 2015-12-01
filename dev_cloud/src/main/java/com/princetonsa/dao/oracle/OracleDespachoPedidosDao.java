/*
 * @(#)OracleDespachoPedidosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;

import com.princetonsa.dao.DespachoPedidosDao;
import com.princetonsa.dao.sqlbase.SqlBaseDespachoPedidosDao;


/**
 * Implementación Oracle de las funciones de acceso a la fuente de datos
 * para despacho de pedidos
 *
 * @version 1.0, Septiembre 29 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class OracleDespachoPedidosDao implements DespachoPedidosDao
{
	/**
	 * Carga el Listado de los pedidos en estado TERMINADO filtrados deacuerdo a la farmacia
	 * solicitada en el pedido versus el centro de costo del usuario que ingresa a la opción  
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param centroCostoUser, int, codigo del centro costo del user
	 * @return ResulSet list
	 */
	public ResultSetDecorator listadoPedidos(Connection con, int centroCostoUser)
	{
		return SqlBaseDespachoPedidosDao.listadoPedidos(con,centroCostoUser);
	}
	
	/**
	 * Carga el detalle de un pedido Part 1  
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroPedido, Código del pedido (table=pedido)
	 * @param almacen
	 * @param institucion
	 * @return ResulSet list
	 */
	public ResultSetDecorator detallePedidoPart1(Connection con, int numeroPedido, int almacen, int institucion)
	{
		return SqlBaseDespachoPedidosDao.detallePedidoPart1(con,numeroPedido,almacen,institucion);
	}
	
	/**
	 * Carga el detalle de un pedido Part 2  
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroPedido, Código del pedido (table=pedido)
	 * @return ResulSet list
	 */
	public ResultSetDecorator detallePedidoPart2(Connection con, int numeroPedido)
	{
		return SqlBaseDespachoPedidosDao.detallePedidoPart2(con,numeroPedido);
	}
	
	/**
	 * Cambia el estado del pedido a DESPACHADO
	 * @param con
	 * @param numeroPedido
	 * @return
	 */
	public int cambiarEstadoPedido(Connection con, int numeroPedido)
	{
		return SqlBaseDespachoPedidosDao.cambiarEstadoPedido(con,numeroPedido);
	}
	
	/**
	 * Inserta un despacho de pedido
	 * @param con, Connection
	 * @param numeroPedido, int
	 * @param usuario, String
	 * @return int (0 -ultimoCodigoSequence) 
	 */
	public int insertarDespachoBasico (Connection con, int numeroPedido,	String usuario)
	{
		return SqlBaseDespachoPedidosDao.insertarDespachoBasico(con,numeroPedido,usuario);
	}
	
	/**
	 * Inserta el DETALLE del despacho de pedidos
	 * @param con, Connection
	 * @param numeroPedido, int
	 * @param articulo, int
	 * @param cantidad, int
	 * @param costo, float
	 * @return
	 */
	public int insertarDetalleDespachoPedido(Connection con, int  numeroPedido,int articulo,int cantidad,float costo, String lote, String fechaVencimiento,String tipoDespacho,String almacenConsignacion,String proveedorCompra,String proveedorCatalogo)
	{
		return SqlBaseDespachoPedidosDao.insertarDetalleDespachoPedido(con,numeroPedido,articulo,cantidad,costo,lote,fechaVencimiento,tipoDespacho,almacenConsignacion,proveedorCompra,proveedorCatalogo);
	}
	
	/**
	 * Carga el resumen del despacho de pedidos  
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroPedido, Código del pedido (table=pedido)
	 * @return ResulSet list
	 */
	public ResultSetDecorator resumen(Connection con, int numeroPedido)
	{
		return SqlBaseDespachoPedidosDao.resumen(con,numeroPedido);
	}
}
