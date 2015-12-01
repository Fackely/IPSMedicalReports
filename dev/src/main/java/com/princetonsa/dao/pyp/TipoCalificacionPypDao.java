/*
 * Creado el Aug 8, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.pyp;

import java.sql.Connection;
import java.util.HashMap;

public interface TipoCalificacionPypDao  {

	
	/**
	 * Metodo para cargar la información General de la Funcionalidad 
	 * @param con
	 * @param HasMap
	 * @return
	 */
	public HashMap consultarInformacion(Connection con, HashMap mapaParam);

	/**
	 * Para Insertar los tipos de calificacion de Metas de PYP 
	 * @param con
	 * @param tipoOperacion
	 * @param consecutivo
	 * @param codigo
	 * @param nombre
	 * @param institucion
	 * @param activo
	 * @return
	 */
	public int insertarTipoCalificacion(Connection con, int tipoOperacion, int consecutivo, String codigo, String nombre, int institucion, boolean activo);

	/**
	 * Metodo para eliminar un tipo de Calificacion de Metas de PYP
	 * @param con
	 * @param codigoTipoCal
	 * @return
	 */
	public int eliminarTipoCalificacion(Connection con, int codigoTipoCal);

}
