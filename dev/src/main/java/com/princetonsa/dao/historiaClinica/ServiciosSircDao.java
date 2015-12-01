/**
 * 
 */
package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author axioma
 *
 */
public interface ServiciosSircDao {

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarServicioSirc(Connection con,HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarServicioSirc(Connection con,HashMap vo);
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarServicioSirc(Connection con,HashMap vo);

	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public abstract boolean eliminarRegistro(Connection con, int codigo, int institucion);
	
	/**
	 * Consulta de Servicios Sirc Detalle
	 * @param Connection con
	 * @param HashMap vo 
	 * **/
	public abstract HashMap consultarServicioSircDetalle(Connection con, HashMap vo);
	
	/**
	 * Inserta un registro Detalle de Servicios Sirc
	 * HashMap vo 
	 * **/
	public abstract boolean insertarServiciosSircDetalle(Connection con, HashMap vo);
	
	/**
	 * Eliminar un registro Detalle de Servicios Sirc
	 * @param Connection con
	 * @param HashMap vo
	 * */
	public abstract boolean eliminarServiciosSircDetalle(Connection con, HashMap vo);
}
