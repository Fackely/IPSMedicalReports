package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

/**
 * 
 * @author wilson
 *
 */
public interface AnulacionFacturasDao 
{
	/**
	 * Metodo para insertar la anulacion de facturas
	 * @param con
	 * @param codigoFactura
	 * @param consecutivoAnulacion
	 * @param motivoAnulacion
	 * @param observaciones
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @param contabilizado
	 * @return
	 */
	public boolean  insertarAnulacionFactura(	Connection con,
												int codigoFactura,	
												String consecutivoAnulacion,
												int motivoAnulacion, 
												String observaciones,
												String loginUsuario,
												int codigoInstitucion,
												String contabilizado
											);
	
	/**
	 * Metodo que carga el Detalle de anulación factura
	 * @param con
	 * @param codigoFacturaAnulacion
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator detalleAnulacionFactura(Connection con, int codigoFacturaAnulacion);
	
	/**
	 * Metodo que carga el Listado de anulación factura
	 * @param con
	 * @param codigoFacturaAnulacion
	 * @param codigoInstitucion
	 * @param empresaInstitucion 
	 * @return
	 * @throws SQLException
	 */
	public ResultSetDecorator listadoAnulacionFactura(	Connection con, int codigoInstitucion, 
												String consecutivoFactura, String consecutivoAnulacion,
												String fechaInicialAnulacion, String fechaFinalAnulacion,
												String loginUsuarioAnulacion,
												int codigoCentroAtencion, String empresaInstitucion);
	
	/**
	 * indica si la factura esta anulada o no
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean estaFacturaAnulada(Connection con, int codigoFactura);
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean actualizarEstadoDetalleCargo(Connection con, String codigoFactura);
}
