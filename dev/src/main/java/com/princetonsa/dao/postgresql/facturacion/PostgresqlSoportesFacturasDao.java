package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;
import com.princetonsa.dao.facturacion.SoportesFacturasDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseSoportesFacturasDao;

/**
 * 
 * @author axioma
 *
 */
public class PostgresqlSoportesFacturasDao implements SoportesFacturasDao {

	/**
	 * Metodo para consultar los tipos de soporte parametrizados
	 * @param con
	 * @param mundo
	 * @return
	 */
	public HashMap obtenerTiposSoporte(Connection con) {
		return SqlBaseSoportesFacturasDao.obtenerTiposSoporte(con);
	}
	
	/**
	 * Metodo para guardar los soportes de facturas
	 * @param con
	 * @param mapaOrganizado
	 * @param codigoInstitucionInt
	 * @param loginUsuario
	 * @return
	 */
	public boolean guardar(Connection con, HashMap<String, Object> mapaOrganizado, int codigoInstitucionInt, String loginUsuario){
		return SqlBaseSoportesFacturasDao.guardar(con, mapaOrganizado, codigoInstitucionInt, loginUsuario);
	}
	
	/**
	 * Método para consultar
	 * @param con
	 * @param soportesFacturasMap
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap<String, Object> consultar(Connection con, HashMap<String, Object> tiposSoporteMap, HashMap<String, Object> soportesFacturasMap, int codigoInstitucionInt){
		return SqlBaseSoportesFacturasDao.consultar(con, tiposSoporteMap, soportesFacturasMap, codigoInstitucionInt);
	}
	
	/**
	 * obtener Tipos Soporte Parametrizados por el usuario
	 * @param con
	 * @param soportesFacturasMap
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap<String, Object> obtenerTiposSoporteXConvenio(Connection con, int institucion, int viaIngreso, int convenio){
		return SqlBaseSoportesFacturasDao.obtenerTiposSoporteXConvenio(con, institucion, viaIngreso, convenio);
	}
	
}