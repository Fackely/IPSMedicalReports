/*
 * Nov 14, 2006
 */
package com.princetonsa.dao.pyp;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * Actividades PYP Ejecutadas x Cargar
 */
public interface ActEjecutadasXCargarDao 
{
	/**
	 * Método implementado para cargar las ordenes ambulatorias pendientes por cargar de un convenio específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarOrdenesAmbXConvenio(Connection con,HashMap campos);
	
	/**
	 * Método implementado para cargar información detallada de una orden ambulatoria
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarDetalleOrdenAmb(Connection con,HashMap campos);
}
