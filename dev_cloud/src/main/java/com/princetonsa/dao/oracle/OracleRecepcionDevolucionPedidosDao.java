/*
 * Created on 27-sep-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.Collection;

import com.princetonsa.dao.RecepcionDevolucionPedidosDao;
import com.princetonsa.dao.sqlbase.SqlBaseRecepcionDevolucionPedidos;

/**
 * @author armando
 *
 * Princeton 27-sep-2004
 * Clase para manejar la recepcion de devoluciones de pedidos
 */
public class OracleRecepcionDevolucionPedidosDao implements RecepcionDevolucionPedidosDao{
	
	/**
     * Metodo que realiza la consulta de todas las devoluciones en estado
     * Generada.
     * @param con, Conexxion
     * @return, colecciion, coleccion con los datos de la consulta.
     */
    public Collection consultarDevoluciones(Connection con,int centroCostoSolicitado,int institucion)
    {
    	return SqlBaseRecepcionDevolucionPedidos.consultarDevoluciones(con,centroCostoSolicitado,institucion);
		
    }

    /**
     * Metodo para realizar la consulta de una devolucion
     * @param con, Conexion
     * @param devolucion, Devolucion que se desa consultar
     * @return ResultSet, resultset con los datos.
     */
	
	public Collection consultarUnaDevolucion(Connection con, int devolucion) 
	{
		return SqlBaseRecepcionDevolucionPedidos.consultarUnaDevolucion(con,devolucion);
	}
	
	/**
     * Método que realiza la consulta del detalle de una devolucion quirúrgica
     * @param con
     * @param devolucion
     * @return
     */
    public Collection consultarUnaDevolucionQx(Connection con,int devolucion)
    {
    	return SqlBaseRecepcionDevolucionPedidos.consultarUnaDevolucionQx(con, devolucion);
    }
	
	/**
	 * Metodo para realizar la insercion de una recepcion
	 * @param con
	 * @param devolucion
	 * @param area
	 * @param fecha
	 * @param hora
	 * @param usuario
	 * 
	 */
	public int insertarRecepcionMaestro(Connection con,int devolucion,String fecha,String hora,String usuario)
	{
		return SqlBaseRecepcionDevolucionPedidos.insertarRecepcionMaestro(con,devolucion,fecha,hora,usuario);
	}
	
	/**
	 * Metodo para realizar la insercion del detalle de una recepcion devolucion pedido
	 * @param con
	 * @param devolucion
	 * @param pedido
	 * @param articulo
	 * @param cantidaddevuelta
	 * @param cantidadrecibida
	 * @param lote
	 * @param fechaVencimiento
	 * 
	 */
    public int insertarRecepcionDetalle(Connection con,int codigo,int devolucion,int cantidadRecibida,String lote,String fechaVencimiento,int articulo, String tipoRecepcion, String almacenConsignacion, String proveedorCompra, String proveedorCatalog)
	{
    	return SqlBaseRecepcionDevolucionPedidos.insertarRecepcionDetalle(con,codigo,devolucion,cantidadRecibida,lote,fechaVencimiento,articulo,tipoRecepcion,almacenConsignacion,proveedorCompra,proveedorCatalog);
	}
	/**
	 * Metodo que actualiza una recepcion de devolucion.
	 * @param con
	 * @return
	 */
	public int actualizarEstadoDevolucion(Connection con, int devolucion,int estadoDevolucion)
	{
		return SqlBaseRecepcionDevolucionPedidos.actualizarEstadoDevolucion(con,devolucion,estadoDevolucion);
	}
   
	/**
     * Metodo para realizar la consulta de una recepcion de una devolucion
     * @param con, Conexion
     * @param devolucion, Devolucion que se desa consultar
     * @return Resultset,Resultado de la consulta
     */
    public ResultSetDecorator consultarMaestroRecepcion(Connection con,int devolucion)
    {
    	return SqlBaseRecepcionDevolucionPedidos.consultarMaestroRecepcion(con,devolucion);
    }
    /**
     * Metodo que realiza la consulta del detalle de una recepcion de una devolucion
     * @param con
     * @param devolucion
     * @return Collection. Informacion obtenidad de la B.D.
     */
    public Collection consultarDetalleRecepcion(Connection con,int devolucion)
    {
    	return SqlBaseRecepcionDevolucionPedidos.consultarDetalleRecepcion(con,devolucion);
    }
    
    /**
     * Metodo que realiza la consulta del detalle de una recepcion de una devolucion Qx
     * @param con
     * @param devolucion
     * @return Collection. Informacion obtenidad de la B.D.
     */
    public Collection consultarDetalleRecepcionQx(Connection con,int devolucion)
    {
    	return SqlBaseRecepcionDevolucionPedidos.consultarDetalleRecepcionQx(con, devolucion);
    }
    
    /**
	 * Método implementado para obtener el costo unitario del despacho del pedido
	 */
	public double obtenerCostoUnitarioDespacho(Connection con,int pedido,int articulo)
	{
		return SqlBaseRecepcionDevolucionPedidos.obtenerCostoUnitarioDespacho(con,pedido,articulo);
	}
	
	/**
	 * Método implementado para actualizar el costo unitario de un articulo
	 * de una devolucion
	 * @param con
	 * @param devolucion
	 * @param pedido
	 * @param articulo
	 * @param costo
	 * @return
	 */
	public int actualizarCostoUnitarioDevolucion(Connection con,int devolucion,
			int pedido,int articulo,double costo)
	{
		return SqlBaseRecepcionDevolucionPedidos.actualizarCostoUnitarioDevolucion(con,devolucion,pedido,articulo,costo);
	}
	
	/**
	 * Método implementado para actualizar el costo unitario de un articulo
	 * de una devolucion
	 * @param con
	 * @param devolucion
	 * @param pedido
	 * @param articulo
	 * @param costo
	 * @return
	 */
	public int actualizarDevolucion(Connection con,int devolucion,int pedido,int articulo,int cantidadDevolucion)
	{
		return SqlBaseRecepcionDevolucionPedidos.actualizarDevolucion(con,devolucion,pedido,articulo,cantidadDevolucion);
	}

}