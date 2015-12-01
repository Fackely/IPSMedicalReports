/*Julio 23 de 2007 
*/

package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Andrés Eugenio Silva Monsalve 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * reporte Procedimientos Esteticos
 */
public interface ReporteProcedimientosEsteticosDao 
{
	/**
	 * Método implementado para consultar los Procedimientos Esteticos
	 * de la parametrizacion de reportes procedimientos esteticos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarProcedimientosEsteticos(Connection con, HashMap campos);
	
	/**
	 * Método que consulta el detalle del cargo de una solicitud estética
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consultarDetalleCargoEstetico(Connection con,HashMap campos);
	
	/**
	 * Método para cargar los materiales especiales de la solicitud de cirugía
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consultarMaterialesEspecialesEstetico(Connection con,HashMap campos);
}