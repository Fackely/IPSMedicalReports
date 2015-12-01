/*
 * Ago 02, 2006
 */
package com.princetonsa.dao.pyp;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * parametrización de actividades de promoción y prevención
 */
public interface ActividadesPypDao 
{
	/**
	 * Método implementado para consultar actividades de promoción y prevención
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultar(Connection con,HashMap campos);
	
	/**
	 * Método implementado para insertar una actividad de promoción y prevención
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertar(Connection con,HashMap campos);
	
	/**
	 * Método implementado para modificar una actividad de promoción y prevención
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificar(Connection con,HashMap campos);
	
	/**
	 * Método implementado para eliminar una actividad de promoción y prevención
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminar(Connection con,HashMap campos);
	
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------Funciones de La funcionalidad de Actividades de Promocion Y Prevencion por Centros de Atención-------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo para consultar toda la informacion relacionada con la funcionalidad.
	 */
	HashMap consultarInformacion(Connection con, HashMap mapaParam);

	/**
	 * Metodo para insertar Actividades PYP por Centro de Atencion.
	 * @param con
	 * @param tipoOperacion
	 * @param centroAtencion
	 * @param codigoActividad
	 * @param institucion
	 * @param activo 
	 * @return
	 */
	int insertarActividadesCentroAtencion(Connection con, int tipoOperacion, int centroAtencion, int codigoActividad, int institucion, boolean activo);

	/**
	 * Metodo para eliminar una Actividad PYP para un centro de Atencion Especifico.
	 * @param con
	 * @param centroAtencion
	 * @param codigoActividad
	 * @param institucion
	 * @return
	 */
	int eliminarActividadesCentroAtencion(Connection con, int centroAtencion, int codigoActividad, int institucion);

}
