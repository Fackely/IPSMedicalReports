/*Julio 23 de 2007
 * 
 */
package com.princetonsa.dao.oracle.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.ReporteProcedimientosEsteticosDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseReporteProcedimientosEsteticosDao;

/**
 * @author Andr�s Eugenio Silva Monsalve
 *
 * Clase que maneja los m�todos prop�os de Oracle para el acceso a la fuente
 * de datos en la funcionalidad de Reporte Procedimientos Esteticos
 */
public class OracleReporteProcedimientosEsteticosDao implements ReporteProcedimientosEsteticosDao 
{
	/**
	 * M�todo implementado para consultar los Procedimientos Esteticos
	 * de la parametrizacion de Reporte Procedimientos Esteticos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarProcedimientosEsteticos(Connection con,HashMap campos)
	{
		return SqlBaseReporteProcedimientosEsteticosDao.consultarProcedimientosEsteticos(con, campos);
	}
	
	/**
	 * M�todo que consulta el detalle del cargo de una solicitud est�tica
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consultarDetalleCargoEstetico(Connection con,HashMap campos)
	{
		return SqlBaseReporteProcedimientosEsteticosDao.consultarDetalleCargoEstetico(con, campos);
	}
	
	/**
	 * M�todo para cargar los materiales especiales de la solicitud de cirug�a
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> consultarMaterialesEspecialesEstetico(Connection con,HashMap campos)
	{
		return SqlBaseReporteProcedimientosEsteticosDao.consultarMaterialesEspecialesEstetico(con, campos);
	}
	
}
