/*
 * Created on Aug 30, 2005
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.TiposMonitoreoDao;
import com.princetonsa.dao.sqlbase.SqlBaseTiposMonitoreoDao;

/**
 * @author Sebastián Gómez R
 *
 * Métodos de postgres para el acceso a la fuente de datos para la funcionalidad
 * de Tipos de Monitoreo
 */
public class PostgresqlTiposMonitoreoDao implements TiposMonitoreoDao {
	
	/**
	 * Cadena que inserta un nuevo registro de tipo monitoreo
	 */
	private static final String insertarTipoMonitoreoStr="INSERT INTO tipo_monitoreo (codigo, nombre, prioridad_cobro, servicio, institucion, requiere_valoracion) values (nextval('seq_tipo_monitoreo'),?,?,?,?,?)";
	
	/**
	 * Método usado para cargar los tipos de monitoreo por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarTiposMonitoreo(Connection con,int institucion)
	{
		return SqlBaseTiposMonitoreoDao.cargarTiposMonitoreo(con,institucion);
	}
	
	/**
	 * Método usado para la actualización de los datos de un registros 
	 * en tipos monitoreos
	 * @param con
	 * @param codigo
	 * @param nombre
	 * @param prioridad
	 * @param servicio
	 * @return
	 */
	public int actualizarTipoMonitoreo(Connection con,int codigo,String nombre,int prioridad,int servicio,int centroCosto, String check)
	{
		return SqlBaseTiposMonitoreoDao.actualizarTipoMonitoreo(con,codigo,nombre,prioridad,servicio,centroCosto,check);
	}
	
	/**
	 * Método para insertar un nuevo tipo de monitoreo
	 * @param con
	 * @param nombre
	 * @param prioridad
	 * @param servicio
	 * @param institucion
	 * @param consulta
	 * @return
	 */
	public int insertarTipoMonitoreo(Connection con,String nombre,int prioridad,int servicio,String check, int institucion)
	{
		return SqlBaseTiposMonitoreoDao.insertarTipoMonitoreo(con,nombre,prioridad,servicio,check,institucion,insertarTipoMonitoreoStr);
	}
	
	/**
	 * Método para eliminar un registro de la tabla tipo_monitoreo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarTipoMonitoreo(Connection con,int codigo)
	{
		return SqlBaseTiposMonitoreoDao.eliminarTipoMonitoreo(con,codigo);
	}
	
	/**
	 * Metodo para Eliminar un centro de costo por tipo de monitoreo
	 * @param con
	 * @param tipo
	 * @param centro
	 * @return
	 */
	public int eliminarCentro(Connection con,int tipo, int centro)
	{
		return SqlBaseTiposMonitoreoDao.eliminarCentro(con, tipo, centro);
	}
	
	/**
	 * Método usado para cargar un tipo de monitoreo por su codigo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarTipoMonitoreo(Connection con,int codigo)
	{
		return SqlBaseTiposMonitoreoDao.cargarTipoMonitoreo(con,codigo);
	}
	
	/**
	 * Método usado para revisar si el tipo de monitoreo se está utilizando
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean revisarUsoTipoMonitoreo(Connection con,int codigo)
	{
		return SqlBaseTiposMonitoreoDao.revisarUsoTipoMonitoreo(con,codigo);
	}
	
	/**
	 * Metodo para insertar los centros de Costo por cada tipo de Monitoreo
	 * @param con
	 * @param tipo
	 * @param centro
	 * @return
	 */
	public boolean insertarCentros(Connection con, int tipo, int centro)
	{
		return SqlBaseTiposMonitoreoDao.insertarCentros(con, tipo, centro);
	}
	
	/**
	 * Metodo que consulta los centros por cada tipo de monitoreo
	 */
	public HashMap cargarCentrosPorTipo(Connection con)
	{
		return SqlBaseTiposMonitoreoDao.cargarCentrosPorTipo(con);
	}
}