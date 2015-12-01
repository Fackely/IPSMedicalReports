package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

public interface SoportesFacturasDao{
	
	
	/**
	 * Metodo para consultar los tipos de soporte parametrizados
	 * @param con
	 * @param mundo
	 * @return
	 */
	public HashMap obtenerTiposSoporte(Connection con);

	/**
	 * Metodo para guardar los soportes de facturas
	 * @param con
	 * @param mapaOrganizado
	 * @param codigoInstitucionInt
	 * @param loginUsuario
	 * @return
	 */
	public boolean guardar(Connection con, HashMap<String, Object> mapaOrganizado, int codigoInstitucionInt, String loginUsuario);

	/**
	 * Método para consultar
	 * @param con
	 * @param soportesFacturasMap
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap<String, Object> consultar(Connection con, HashMap<String, Object> tipoSoporteMap, HashMap<String, Object> soportesFacturasMap, int codigoInstitucionInt);

	/**
	 * obtener Tipos Soporte Parametrizados por el usuario
	 * @param con
	 * @param soportesFacturasMap
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap<String, Object> obtenerTiposSoporteXConvenio(Connection con, int institucion, int viaIngreso, int convenio);
}