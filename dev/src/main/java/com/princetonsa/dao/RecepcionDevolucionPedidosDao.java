/*
 * Created on 27-sep-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author armando
 *
 * Princeton 27-sep-2004
 * Interface que contine los metodos para eln manejo de las devolucion de pedidos
 */
public interface RecepcionDevolucionPedidosDao {
	
	/**
     * Metodo que realiza la consulta de todas las devoluciones en estado
     * Generada.
     * @param con, Conexxion
     * @return, colecciion, coleccion con los datos de la consulta.
     */
     public Collection consultarDevoluciones(Connection con,int centroCostoSolicitado,int institucion);
     
     /**
      * Metodo para realizar la consulta de una devolucion
      * @param con, Conexion
      * @param devolucion, Devolucion que se desa consultar
      * @return ResultSet, resultset con los datos.
      */
     public Collection consultarUnaDevolucion(Connection con,int devolucion);
     
     /**
      * Método que realiza la consulta del detalle de una devolucion quirúrgica
      * @param con
      * @param devolucion
      * @return
      */
     public Collection consultarUnaDevolucionQx(Connection con,int devolucion);
     
     /**
      * Metodo para realizar la insercion de un registro
      * @param con
      * @param devolucion
      * @param area
      * @param fecha
      * @param hora
      * @param usuario
      */
     
     public int insertarRecepcionMaestro(Connection con,int devolucion,String fecha,String hora,String usuario);
     
     
 	/**
 	 * Metodo para realizar la insercion del detalle de una recepcion devolucion pedido
 	 * @param con
 	 * @param devolucion
 	 * @param articulo 
 	 * @param pedido
 	 * @param articulo
 	 * @param proveedorCatalog 
 	 * @param proveedorCompra 
 	 * @param almacenConsignacion 
 	 * @param tipoRecepcion 
 	 * @param cantidaddevuelta
 	 * @param cantidadrecibida
 	 * 
 	 */
     public int insertarRecepcionDetalle(Connection con,int codigo,int devolucion,int cantidadRecibida,String lote,String fechaVencimiento, int articulo, String tipoRecepcion, String almacenConsignacion, String proveedorCompra, String proveedorCatalog);

	/**
	 * Metodo que actualiza una recepcion de devolucion.
	 * @param con
	 * @return
	 */
	public int actualizarEstadoDevolucion(Connection con, int devolucion,int estadoDevolucion);
	/**
     * Metodo para realizar la consulta de una recepcion de una devolucion
     * @param con, Conexion
     * @param devolucion, Devolucion que se desa consultar
     * @return Resultset,Resultado de la consulta
     */
    public ResultSetDecorator consultarMaestroRecepcion(Connection con,int devolucion);
	
    /**
     * Metodo que realiza la consulta del detalle de una recepcion de una devolucion
     * @param con
     * @param devolucion
     * @return Collection. Informacion obtenidad de la B.D.
     */
    public Collection consultarDetalleRecepcion(Connection con,int devolucion);
    
    /**
     * Metodo que realiza la consulta del detalle de una recepcion de una devolucion Qx
     * @param con
     * @param devolucion
     * @return Collection. Informacion obtenidad de la B.D.
     */
    public Collection consultarDetalleRecepcionQx(Connection con,int devolucion);
    
    /**
	 * Método implementado para obtener el costo unitario del despacho del pedido
	 */
	public double obtenerCostoUnitarioDespacho(Connection con,int pedido,int articulo);
	
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
	public int actualizarCostoUnitarioDevolucion(Connection con,int devolucion,int pedido,int articulo,double costo);
	
	/**
	 * Método implementado para actualizar 
	 * la cantidad de recepcion en la devolucion
	 * @param con
	 * @param devolucion
	 * @param pedido
	 * @param articulo
	 * @param cantidadDevolucion
	 * @return
	 */
	public int actualizarDevolucion(Connection con, int devolucion, int pedido, int articulo, int cantidadDevolucion);
	    
}
