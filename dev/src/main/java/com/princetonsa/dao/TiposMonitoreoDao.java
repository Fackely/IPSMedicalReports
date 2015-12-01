/* Created on Aug 30, 2005
 * 
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebasti�n G�mez R
 *
 * Interface usada para gestionar los m�toidos de acceso a la base
 * de datos en la fucnionalidad Tipos de Monitoreo
 */
public interface TiposMonitoreoDao {

	/**
	 * M�todo usado para cargar los tipos de monitoreo por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarTiposMonitoreo(Connection con,int institucion);
	
	/**
	 * M�todo usado para la actualizaci�n de los datos de un registros 
	 * en tipos monitoreos
	 * @param con
	 * @param codigo
	 * @param nombre
	 * @param prioridad
	 * @param servicio
	 * @return
	 */
	public int actualizarTipoMonitoreo(Connection con,int codigo,String nombre,int prioridad,int servicio,int centroCosto, String check);
	
	/**
	 * M�todo para insertar un nuevo tipo de monitoreo
	 * @param con
	 * @param nombre
	 * @param prioridad
	 * @param servicio
	 * @param institucion
	 * @param consulta
	 * @return
	 */
	public int insertarTipoMonitoreo(Connection con,String nombre,int prioridad,int servicio, String check, int institucion);
	
	/**
	 * M�todo para eliminar un registro de la tabla tipo_monitoreo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarTipoMonitoreo(Connection con,int codigo);
	
	/**
	 * Metodo para eliminar un centro de costo por Tipo de monitoreo
	 * @param con
	 * @param tipo
	 * @param centro
	 * @return
	 */
	public int eliminarCentro(Connection con, int tipo, int centro);
	
	/**
	 * M�todo usado para cargar un tipo de monitoreo por su codigo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarTipoMonitoreo(Connection con,int codigo);
	
	/**
	 * M�todo usado para revisar si el tipo de monitoreo se est� utilizando
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean revisarUsoTipoMonitoreo(Connection con,int codigo);
	
	/**
	 * Metodo para insertar los centros de Costo por cada tipo de Monitoreo
	 * @param con
	 * @param tipo
	 * @param centro
	 * @return
	 */
	public boolean insertarCentros(Connection con, int tipo, int centro);
	
	/**
	 * Metodo que consulta los centros por cada tipo de Monitoreo
	 * @param con
	 * @return
	 */
	public HashMap cargarCentrosPorTipo(Connection con);
}