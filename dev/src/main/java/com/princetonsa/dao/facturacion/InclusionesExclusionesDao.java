package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosString;

public interface InclusionesExclusionesDao {
	
	/**
	 * Consulta los paquetes existentes por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */

	public abstract HashMap consultarIncluExcluExistentes(Connection con, HashMap vo);
	
	
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
	
	public abstract boolean eliminarRegistro(Connection con, int institucion, String codigo);

	

	/**
	 * 
	 * @param con
	 * @param institucion
	 */
	public abstract Vector<InfoDatosString> consultarIncluExcluExistentes(Connection con, int institucion);
	
	
}
