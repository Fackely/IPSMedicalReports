/*
 * Junio 27, 2006
 */
package com.princetonsa.dao.oracle.interfaz;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.interfaz.GeneracionInterfazDao;
import com.princetonsa.dao.sqlbase.interfaz.SqlBaseGeneracionInterfazDao;

/**
 * 
 * @author sgomez
 * M�todos de oracle para el acceso a la fuente de datos para la funcionalidad
 * Generaci�n Interfaz Facturaci�n
 */
public class OracleGeneracionInterfazDao implements GeneracionInterfazDao
{
	/**
	 * M�todo implementado para consultar las facturas/anulaciones
	 * seg�n los campos parametrizados
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarFacturasAnulaciones(Connection con,HashMap campos)
	{
		return SqlBaseGeneracionInterfazDao.consultarFacturasAnulaciones(con,campos);
	}
	
	/**
	 * M�todo implementado para consultar las generaciones de interfaz previas
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarGeneracionesInterfazPrevias(Connection con,HashMap campos)
	{
		return SqlBaseGeneracionInterfazDao.consultarGeneracionesInterfazPrevias(con,campos);
	}
	
	/**
	 * M�todo implementado para consultar los datos de parametrizacion de los 
	 * campos de un registro interfaz
	 * @param con
	 * @param codigoRegistro
	 * @return
	 */
	public HashMap consultarCamposRegistroInterfaz(Connection con,String codigoRegistro)
	{
		return SqlBaseGeneracionInterfazDao.consultarCamposRegistroInterfaz(con,codigoRegistro);
	}
	
	/**
	 * M�todo implementado para consultar la cuenta parametrizable 
	 * seg�n el tipo y los par�metros enviados por el mapa paramCuentas
	 * @param con
	 * @param paramCuentas
	 * @return
	 */
	public String consultarCuentaParametrizable(Connection con,HashMap paramCuentas)
	{
		return SqlBaseGeneracionInterfazDao.consultarCuentaParametrizable(con,paramCuentas);
	}
	/**
	 * M�todo implementado para consultar el detalle de sevicios/asocios
	 * de una factura
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public HashMap consultaDetalleServiciosFactura(Connection con,String codigoFactura)
	{
		return SqlBaseGeneracionInterfazDao.consultaDetalleServiciosFactura(con,codigoFactura);
	}
	
	/**
	 * M�todo implementado para consultar el detalle de art�culos
	 * de una factura
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public HashMap consultaDetalleArticulosFactura(Connection con,String codigoFactura)
	{
		return SqlBaseGeneracionInterfazDao.consultaDetalleArticulosFactura(con,codigoFactura);
	}
	
	/**
	 * M�todo implementado para ingresar informaci�n de auditoria
	 * sobre la generaci�n de interfaz
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarAuditoriaGeneracion(Connection con,HashMap campos)
	{
		campos.put("secuencia","seq_generaciones_interfaz.nextval");
		return SqlBaseGeneracionInterfazDao.insertarAuditoriaGeneracion(con,campos);
	}
}
