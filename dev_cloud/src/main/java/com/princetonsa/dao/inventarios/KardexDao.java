/*
 * Creado el 27-dic-2005
 * por Joan López
 */
package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Joan López
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
public interface KardexDao 
{
	 /**
     * metodo para realizar la busqueda avanzada de articulos
     * @param con Connection
     * @param vo HashMap 
     * @return HashMap     
     */
    public abstract HashMap ejecutarBusquedaAvanzada(Connection con, HashMap vo);
    /**
     * metodo para consultar el detalle de los
     * articulos
     * @param con Connection
     * @param almacen 
     * @param articulo String
     * @param fechaInicial String
     * @param fechaFinal String
     * @return HashMap
     */
    public abstract HashMap ejecutarConsultaDetalleArticulos(Connection con, int almacen, String articulo,String fechaInicial,String fechaFinal);
    /**
     * metodo para realizar la consulta del 
     * detalle de cierres
     * @param con Connection
     * @param codigoCierre String
     * @param codigoArticulo int
     * @param codigoAlmacen int
     * @return HashMap
     */
    public abstract HashMap ejecutarConsultaDetalleCierres(Connection con,String codigoCierre,int codigoArticulo,int codigoAlmacen);
    /**
     * metodo para consultar los ultimos movimientos
     * de un articulo para generar el kardex
     * @param con Connection 
     * @param fechaInicial String 
     * @param fechaFinal String
     * @param articulo String
     * @param almacen String
     * @return HashMap
     */
    public abstract HashMap consultarUltimosMovimientosArticulo(Connection con,String fechaInicial,String fechaFinal,String articulo,String almacen);
    
    /**
     * 
     * @param con
     * @param vo
     * @return
     */
	public abstract HashMap accionEjecutarBusquedaArticulosLote(Connection con, HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param codigoCierre
	 * @param codigoArticulo
	 * @param codigoAlmacen
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public abstract HashMap ejecutarConsultaDetalleCierresLote(Connection con, String codigoCierre, int codigoArticulo, int codigoAlmacen, String lote, String fechaVencimiento);
	
	/**
	 * 
	 * @param con
	 * @param string
	 * @param string2
	 * @param articulo
	 * @param almacen
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public abstract HashMap consultarUltimosMovimientosArticuloLote(Connection con,String fechaInicial,String fechaFinal,String articulo,String almacen, String lote, String fechaVencimiento);
	
	/**
	 * 
	 * @param con
	 * @param almacen
	 * @param articulo
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param lote
	 * @param fechaVencimiento
	 * @return
	 */
	public abstract HashMap ejecutarConsultaDetalleArticulosLote(Connection con, int almacen, String articulo,String fechaInicial,String fechaFinal, String lote, String fechaVencimiento);
}
