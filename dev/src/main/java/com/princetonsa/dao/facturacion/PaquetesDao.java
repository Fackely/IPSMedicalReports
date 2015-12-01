package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

public interface PaquetesDao {
	
	
	/**
	 * Consulta los paquetes existentes por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */

	public abstract HashMap consultarPaquetesExistentes(Connection con, HashMap vo);
	
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 */
	
	
	public abstract boolean insertar(Connection con, HashMap vo);
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	
	public abstract boolean modificar(Connection con,HashMap vo);
	
	
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param codigoPaquete
	 * @return
	 */
	
	public abstract boolean eliminarRegistro(Connection con, int institucion, String codigoPaquete);
	
}
	
	
