package com.princetonsa.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Juan Sebastian castaño castro
 * 
 * Clase dao de la funcionalidad de busqueda generica de terceros
 *
 */

public interface BusquedaTercerosGenericaDao {
	
	/**
	 * Metodo de consulta de terceros
	 * @param con
	 * @param institucion
	 * @param nit
	 * @param descripcionTercero
	 * @return
	 */
	public HashMap<String, Object> consultarTerceros (Connection con, int institucion, String nit, String descripcionTercero, String filtrarXEstadoActivo, String filtrarXRelacionEmpresa, String filtrarXDeudor,  String tipoTercero );
	
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
	public HashMap consultarTercerosAvan (Connection con, int institucion, String nit, String descripcionTercero, String  filtrarXEstadoActivo, String tipoTercero,String esEmpresa);
	
	
	/**
	 * Metodo dencargado de obtener los tipos de 
	 * terceros
	 * @param connection
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerTiposTerceros (Connection connection);




	

}
