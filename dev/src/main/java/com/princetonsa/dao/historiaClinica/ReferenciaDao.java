/*
 * Mayo 15, 2007
 */
package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebasti�n G�mez 
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * REFERENCIA
 */
public interface ReferenciaDao 
{
	/**
	 * M�todo que consulta la referencia
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargar(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para guardar informaci�n de referencia
	 * bien sea de inserci�n , eliminacion o modificacion
	 * @param con
	 * @param campos
	 * @return
	 */
	public String guardar(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para realizar la b�squeda de las instituciones SIRC
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap busquedaInstitucionesSirc(Connection con,HashMap campos);
	
	/**
	 * M�todo que realiza la anulacion de referencias externas caducadas
	 * @param con
	 * @param campos
	 * @return
	 */
	public boolean anularReferenciasExternasCaducadas(Connection con,HashMap campos);
}
