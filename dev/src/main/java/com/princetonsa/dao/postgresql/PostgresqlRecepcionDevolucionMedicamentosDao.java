/*
 * Created on 11-oct-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.dao.RecepcionDevolucionMedicamentosDao;
import com.princetonsa.dao.sqlbase.SqlBaseRecepcionDevolucionMedicamentos;

/**
 * @author armando
 *
 * Princeton 11-oct-2004
 */
public class PostgresqlRecepcionDevolucionMedicamentosDao implements RecepcionDevolucionMedicamentosDao
{
	/**
     * Metodo que realiza la consulta de todas las devoluciones en estado
     * Generada.
     * @param con, Conexxion
     * @return, colecciion, coleccion con los datos de la consulta.
     */
    public Collection consultarDevoluciones(Connection con,int centroCostoSolicitado,int codigoPaciente,HashMap<String, Object> vo)
    {
    	return SqlBaseRecepcionDevolucionMedicamentos.consultarDevoluciones(con,centroCostoSolicitado,codigoPaciente,vo);
    }
    /**
     * Metodo para realizar la consulta de una devolucion
     * @param con, Conexion
     * @param devolucion, Devolucion que se desa consultar
     * @return collection,Coleccion con los datos
     */
    public Collection consultarUnaDevolucion(Connection con,int devolucion)
    {
    	return SqlBaseRecepcionDevolucionMedicamentos.consultarUnaDevolucion(con,devolucion);
    }
    /**
     * Metodo que realiza la insercion de un registro
     * @param con
     * @param devolucion
     * @param tipo
     * @param farmacia
     * @param fecha
     * @param hora
     * @param usuario
     * 
     */
    public int insertarRecepcionMaestro(Connection con,int devolucion,String fecha,String hora,String usuario)
    {
    	return SqlBaseRecepcionDevolucionMedicamentos.insertarRecepcionMaestro(con,devolucion,fecha,hora,usuario);
    }
    /**
     * Metodo que realiza la insercion de un registro
     * @param con
     * @param codigo, del detalle de la devolucion de medicamentos
     * @param tipo, del detalle de la devolucion de medicamentos
     * @param devolucion,
     * @param articulo
     * @param numeroSolicitud.
     * @param cantidaddevuelta
     * @param cantidadrecibida
     * 
     */
    public int insertarRecepcionDetalle(Connection con,int codigo, int devolucion,int cantidadRecibida, String lote, String fechaVencimiento,int articulo, String tipoRecepcion, String almacenConsignacion, String proveedorCompra, String proveedorCatalogo)
	{
    	return SqlBaseRecepcionDevolucionMedicamentos.insertarRecepcionDetalle(con,codigo,devolucion,cantidadRecibida,lote,fechaVencimiento,articulo,tipoRecepcion,almacenConsignacion,proveedorCompra,proveedorCatalogo);
	}
    /**
	 * Metodo que actualiza el estado de una devolucion.
	 * @param con, conexion
	 * @return
	 */
	public  int actualizarEstadoDevolucion(Connection con,int devolucion,int estadoDevolucion) 
	{
		return SqlBaseRecepcionDevolucionMedicamentos.actualizarEstadoDevolucion(con,devolucion,estadoDevolucion);
	}
    /**
     * Metodo para realizar la consulta de una recepcion de una devolucion
     * @param con, Conexion
     * @param devolucion, Devolucion que se desa consultar
     * @return Resultset,Resultado de la consulta
     */
    
    public ResultSetDecorator consultarMaestroRecepcion(Connection con,int devolucion)
    {
    	return SqlBaseRecepcionDevolucionMedicamentos.consultarMaestroRecepcion(con,devolucion);
    }
    /**
     * Metodo que realiza la consulta del detalle de una recepcion de una devolucion
     * @param con
     * @param devolucion
     * @return Collection. Informacion obtenidad de la B.D.
     */
    public Collection consultarDetalleRecepcion(Connection con,int devolucion)
    {
    	return SqlBaseRecepcionDevolucionMedicamentos.consultarDetalleRecepcion(con,devolucion);
    }
	
}
