/*Julio 23 de 2007 
*/

package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Andr�s Eugenio Silva Monsalve 
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * reporte Procedimientos Esteticos
 */
public interface ReporteProcedimientosEsteticosDao 
{
	/**
	 * M�todo implementado para consultar los Procedimientos Esteticos
	 * de la parametrizacion de reportes procedimientos esteticos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarProcedimientosEsteticos(Connection con, HashMap campos);
	
	/**
	 * M�todo que consulta el detalle del cargo de una solicitud est�tica
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consultarDetalleCargoEstetico(Connection con,HashMap campos);
	
	/**
	 * M�todo para cargar los materiales especiales de la solicitud de cirug�a
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consultarMaterialesEspecialesEstetico(Connection con,HashMap campos);
}