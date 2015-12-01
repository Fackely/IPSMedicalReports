/*
 * Sep 09 / 2005
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebasti�n G�mez R
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * parametrizaci�n de Porcentajes Cirug�as M�ltiples
 */
public interface PorcentajesCxMultiplesDao {
	
	/**
	 * M�todo para cargar el listado de los procentajes de cirug�as
	 * m�ltiples parametrizadas por instituci�n
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarEncaPorcentajes(Connection con,int institucion,HashMap parametros);
	
	/**
	 * M�todo para cargar el listado de los procentajes de cirug�as
	 * m�ltiples parametrizadas por instituci�n
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarPorcentajes(Connection con,int institucion,HashMap parametros);
	
	/**
	 * M�todo usado para modificar un registros de encabezado de porcentajes
	 * de cirug�as m�ltiples
	 * @param con
	 * @param HashMap parametros	  
	 * @return
	 */
	public int actualizarEncaPorcentaje(Connection con,HashMap parametros);
	
	/**
	 * M�todo usado para modificar un registros de los porcentajes
	 * de cirug�as m�ltiples
	 * @param con
	 * @param HashMap parametros	  
	 * @return
	 */
	public int actualizarPorcentaje(Connection con,HashMap parametros);
	
	/**
	 * M�todo para eliminar un porcentaje de cirug�a m�ltiple
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarEncaPorcentaje(Connection con,int codigo);
	
	/**
	 * M�todo para eliminar un porcentaje de cirug�a m�ltiple
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarPorcentaje(Connection con,int codigo);
	
	
	/**
	 * M�todo usado para insertar un nuevo Encabezado de Porcentraje de cirugia m�ltiple
	 * @param con
	 * @param HashMap parametros
	 * @return retorna -1 o 0 si la transacci�n no fue exitosa
	 */
	public int insertarEncaPorcentaje(Connection con,HashMap parametros);
	
	
	/**
	 * M�todo usado para insertar un nuevo Porcentraje de cirugia m�ltiple
	 * @param con
	 * @param HashMap parametros
	 * @return retorna -1 o 0 si la transacci�n no fue exitosa
	 */
	public int insertarPorcentaje(Connection con,HashMap parametros);
	
	
	/**
	 * M�todo usao para cargar un registro de los porcentajes
	 * de cirug�as m�ltiples por su c�digo
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarPorcentaje(Connection con,int codigo);
	
	
	/**
	 * M�todo usado para la b�squeda de registros en 
	 * Consultar Porcentajes Cx M�ltiples.
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