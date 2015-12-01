/*
 * Mayo 15, 2007
 */
package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * REFERENCIA
 */
public interface ReferenciaDao 
{
	/**
	 * Método que consulta la referencia
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargar(Connection con,HashMap campos);
	
	/**
	 * Método implementado para guardar información de referencia
	 * bien sea de inserción , eliminacion o modificacion
	 * @param con
	 * @param campos
	 * @return
	 */
	public String guardar(Connection con,HashMap campos);
	
	/**
	 * Método implementado para realizar la búsqueda de las instituciones SIRC
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap busquedaInstitucionesSirc(Connection con,HashMap campos);
	
	/**
	 * Método que realiza la anulacion de referencias externas caducadas
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean anularReferenciasExternasCaducadas(Connection con,HashMap campos);
}
