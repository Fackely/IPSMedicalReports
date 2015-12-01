package com.princetonsa.dao.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;


/**
 * 
 * @author Juan Sebastian Castaño
 * Interface DAO de la funcionalidad Asocios X Tipo de Servicio
 */

public interface AsociosXTipoServicioDao {
	/**
	 * Metodo de consulta de servicios asociados
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap<String, Object> cargarServiciosAsocios(Connection con, int institucion);
	
	/**
	 * Metodo para cargar todos los tipos de servicio en un select
	 * @param con
	 * @return
	 */	
	public HashMap<String, Object> cargarTiposServicio(Connection con);
	
	/**
	 * Metodo para cargar todos los tipos de asocio dependiendo de la institucion en un select
	 * @param con
	 * @return
	 */	
	public HashMap<String, Object> cargarTiposAsocios(Connection con, int institucion);
	
	/**
	 * Metodo de insercion de un nuevo registro de asocios por tipo de servicio
	 * @param con
	 * @param tipo_servicio
	 * @param asocio
	 * @param servicio
	 * @param activo
	 * @param institucion
	 * @param usuario
	 * @return
	 */
	public boolean insertarTiposAsocios(Connection con,	String tipo_servicio,int asocio,int servicio,int institucion,String usuario);
	
	/**
	 * Metodo de eliminacion de un servicio Asocio
	 * @param con
	 * @param institucion
	 * @param codigo
	 * @return
	 */
	public boolean eliminarServAsocio(Connection con, int institucion, int codigo);
	
	
	/**
	 * Metodo de modificacion de un registro de servicio asocios
	 * @param con
	 * @param codigo
	 * @param tipo_servicio
	 * @param asocio
	 * @param servicio
	 * @param activo
	 * @param institucion
	 * @param usuario
	 * @return
	 */
	public boolean modificarServAsocio (Connection con, 
															int codigo,
															String tipo_servicio,
															int asocio,
															int servicio,
															int institucion, 
															String usuario);

}
