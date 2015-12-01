/*
 * Nov 14, 2006
 */
package com.princetonsa.dao.pyp;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebasti�n G�mez 
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * Actividades PYP Ejecutadas x Cargar
 */
public interface ActEjecutadasXCargarDao 
{
	/**
	 * M�todo implementado para cargar las ordenes ambulatorias pendientes por cargar de un convenio espec�fico
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarOrdenesAmbXConvenio(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para cargar informaci�n detallada de una orden ambulatoria
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarDetalleOrdenAmb(Connection con,HashMap campos);
}
