/*
 * Sep 09 / 2005
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez R
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * parametrización de Porcentajes Cirugías Múltiples
 */
public interface PorcentajesCxMultiplesDao {
	
	/**
	 * Método para cargar el listado de los procentajes de cirugías
	 * múltiples parametrizadas por institución
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarEncaPorcentajes(Connection con,int institucion,HashMap parametros);
	
	/**
	 * Método para cargar el listado de los procentajes de cirugías
	 * múltiples parametrizadas por institución
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarPorcentajes(Connection con,int institucion,HashMap parametros);
	
	/**
	 * Método usado para modificar un registros de encabezado de porcentajes
	 * de cirugías múltiples
	 * @param con
	 * @param HashMap parametros	  
	 * @return
	 */
	public int actualizarEncaPorcentaje(Connection con,HashMap parametros);
	
	/**
	 * Método usado para modificar un registros de los porcentajes
	 * de cirugías múltiples
	 * @param con
	 * @param HashMap parametros	  
	 * @return
	 */
	public int actualizarPorcentaje(Connection con,HashMap parametros);
	
	/**
	 * Método para eliminar un porcentaje de cirugía múltiple
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarEncaPorcentaje(Connection con,int codigo);
	
	/**
	 * Método para eliminar un porcentaje de cirugía múltiple
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarPorcentaje(Connection con,int codigo);
	
	
	/**
	 * Método usado para insertar un nuevo Encabezado de Porcentraje de cirugia múltiple
	 * @param con
	 * @param HashMap parametros
	 * @return retorna -1 o 0 si la transacción no fue exitosa
	 */
	public int insertarEncaPorcentaje(Connection con,HashMap parametros);
	
	
	/**
	 * Método usado para insertar un nuevo Porcentraje de cirugia múltiple
	 * @param con
	 * @param HashMap parametros
	 * @return retorna -1 o 0 si la transacción no fue exitosa
	 */
	public int insertarPorcentaje(Connection con,HashMap parametros);
	
	
	/**
	 * Método usao para cargar un registro de los porcentajes
	 * de cirugías múltiples por su código
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarPorcentaje(Connection con,int codigo);
	
	
	/**
	 * Método usado para la búsqueda de registros en 
	 * Consultar Porcentajes Cx Múltiples.
	 * @param con
	 * @param esGeneral
	 * @param esquemaTarifario
	 * @param tipoCirugia
	 * @param asocio
	 * @param medico
	 * @param via
	 * @param liquidacion
	 * @param politra
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public HashMap busquedaPorcentajes(
			Connection con,
			String esGeneral,
			int esquemaTarifario,
			String tipoCirugia,
			int asocio,
			int medico,
			int via,
			float liquidacion,
			float adicional,
			float politra,
			int institucion);	
}