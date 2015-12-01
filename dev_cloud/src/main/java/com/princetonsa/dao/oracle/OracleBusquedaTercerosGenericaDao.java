package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.BusquedaTercerosGenericaDao;
import com.princetonsa.dao.sqlbase.SqlBaseBusquedaTercerosGenericaDao;
/**
 * 
 * @author Juan Sebastian castaño
 * 
 * Clase dao para la ejecucion de acciones de la funcionalidad de busquedad generica terceros sobre una base de datos Oracle 
 *
 */
public class OracleBusquedaTercerosGenericaDao implements
		BusquedaTercerosGenericaDao {

	/**
	 * Metodo de consulta de terceros
	 * @param con
	 * @param institucion
	 * @param nit
	 * @param descripcionTercero
	 * @return
	 */
	public HashMap<String, Object> consultarTerceros (Connection con, int institucion, String nit, String descripcionTercero, String filtrarXEstadoActivo, String filtrarXRelacionEmpresa, String filtrarXDeudor, String tipoTercero)
	{
		return SqlBaseBusquedaTercerosGenericaDao.consultarTerceros(con, institucion, nit, descripcionTercero, filtrarXEstadoActivo, filtrarXRelacionEmpresa, filtrarXDeudor, tipoTercero);
	}
	

	/**
	 * Metodo encargado de consultar los terceros
	 * pudiendo filtar por otros campos
	 * @param con
	 * @param institucion
	 * @param nit
	 * @param descripcionTercero
	 * @param filtrarXEstadoActivo
	 * @param filtrarXRelacionEmpresa
	 * @param filtrarXDeudor
	 * @param razonSocial
	 * @param tipoTercero
	 * @return
	 */
	public HashMap consultarTercerosAvan (Connection con, int institucion, String nit, String descripcionTercero, String  filtrarXEstadoActivo, String tipoTercero,String esEmpresa)
	{
		return SqlBaseBusquedaTercerosGenericaDao.consultarTercerosAvan(con, institucion, nit, descripcionTercero, filtrarXEstadoActivo, tipoTercero,esEmpresa);
	}
	
	/**
	 * Metodo dencargado de obtener los tipos de 
	 * terceros
	 * @param connection
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposTerceros (Connection connection)
	{
		return SqlBaseBusquedaTercerosGenericaDao.obtenerTiposTerceros(connection);
	}
}
