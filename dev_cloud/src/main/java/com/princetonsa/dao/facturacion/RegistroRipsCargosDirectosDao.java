/*
 * 11 Abril, 2008
 */
package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * registro Rips Cargos Directos
 */
public interface RegistroRipsCargosDirectosDao 
{
	/**
	 * Método para consultar el listado de cuentas que tienen soolicitudes de cargos directos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> listadoCuentas(Connection con,HashMap campos);
	
	/**
	 * Método que consulta el listado de solicitudes de cargos directos de una cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> listadoSolicitudes(Connection con,HashMap campos);
}
