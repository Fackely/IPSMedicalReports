/*
 * Ago 02, 2006
 */
package com.princetonsa.dao.pyp;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebasti�n G�mez 
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * parametrizaci�n de actividades de promoci�n y prevenci�n
 */
public interface ActividadesPypDao 
{
	/**
	 * M�todo implementado para consultar actividades de promoci�n y prevenci�n
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultar(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para insertar una actividad de promoci�n y prevenci�n
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertar(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para modificar una actividad de promoci�n y prevenci�n
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificar(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para eliminar una actividad de promoci�n y prevenci�n
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminar(Connection con,HashMap campos);
	
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------Funciones de La funcionalidad de Actividades de Promocion Y Prevencion por Centros de Atenci�n-------------------------
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
