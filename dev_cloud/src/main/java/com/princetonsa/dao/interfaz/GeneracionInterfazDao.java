/*
 * Junio 27, 2006
 */
package com.princetonsa.dao.interfaz;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author sgomez
 *	Interface usada para gestionar los métoidos de acceso a la base
 * de datos en la fucnionalidad Generacion Interfaz Facturación
 */
public interface GeneracionInterfazDao 
{
	/**
	 * Método implementado para consultar las facturas/anulaciones
	 * según los campos parametrizados
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarFacturasAnulaciones(Connection con,HashMap campos);
	
	/**
	 * Método implementado para consultar las generaciones de interfaz previas
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarGeneracionesInterfazPrevias(Connection con,HashMap campos);
	
	/**
	 * Método implementado para consultar los datos de parametrizacion de los 
	 * campos de un registro interfaz
	 * @param con
	 * @param codigoRegistro
	 * @return
	 */
	public HashMap consultarCamposRegistroInterfaz(Connection con,String codigoRegistro);
	
	/**
	 * Método implementado para consultar la cuenta parametrizable 
	 * según el tipo y los parámetros enviados por el mapa paramCuentas
	 * @param con
	 * @param paramCuentas
	 * @return
	 */
	public String consultarCuentaParametrizable(Connection con,HashMap paramCuentas);
	
	/**
	 * Método implementado para consultar el detalle de sevicios/asocios
	 * de una factura
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public HashMap consultaDetalleServiciosFactura(Connection con,String codigoFactura);
	
	/**
	 * Método implementado para consultar el detalle de artículos
	 * de una factura
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public HashMap consultaDetalleArticulosFactura(Connection con,String codigoFactura);
	
	/**
	 * Método implementado para ingresar información de auditoria
	 * sobre la generación de interfaz
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarAuditoriaGeneracion(Connection con,HashMap campos);
}
