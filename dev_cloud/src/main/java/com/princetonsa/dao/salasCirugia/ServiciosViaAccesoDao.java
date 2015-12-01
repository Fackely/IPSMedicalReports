package com.princetonsa.dao.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public interface ServiciosViaAccesoDao {
	
	/**
	 * Metodo para insertar los servicios via acceso
	 * @param con
	 * @param servicio
	 * @param institucion
	 * @param cadenaInsecion
	 * @return
	 */
	public boolean insertarServiciosViaAcceso(Connection con,HashMap vo);
	
	/**
	 * Metodo para la consulta de los servicios de via de acceso
	 * @param con
	 * @return
	 */
	public HashMap consultarServiciosViaAcceso(Connection con);
	
	/**
	 * Metodo para eliminar los servicios de via de acceso
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminarServiciosViaAcceso(Connection con,int codigo);

}
