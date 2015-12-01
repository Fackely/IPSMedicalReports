/*Julio 16 de 2007 
*/

package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Andr�s Eugenio Silva Monsalve 
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * parametrizaci�n de Condiciones por Servicio
 */
public interface CondicionesXServicioDao 
{
	/**
	 * M�todo implementado para consultar el consecutivo x servicio
	 * de la parametrizacion de Condiciones por Servicio
	 * @param con
	 * @param campos
	 * @return
	 */
	public int consultarConsecutivoXServicio(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para consultar la parametrizacion de una Condicion de Servicios por consecutivo
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarCondicionServiciosXConsecutivo(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para guardar la Condicion de Servicios
	 * @param con
	 * @param campos
	 * @return
	 */
	public int guardar(Connection con,HashMap campos);
	
	/**
	 * M�todo que consulta las condiciones para la toma del servicio
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> obtenerCondicionesTomaXServicio(Connection con,HashMap campos);
}