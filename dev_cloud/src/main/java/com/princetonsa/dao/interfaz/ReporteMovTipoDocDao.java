package com.princetonsa.dao.interfaz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.interfaz.SqlBaseReporteMovTipoDocDao;
import com.princetonsa.dto.interfaz.DtoMovimientoTipoDocumento;


/**
 * @author Jairo Gómez Fecha Julio de 2009
 */

public interface ReporteMovTipoDocDao 
{
	/**
	 * Metodo que consulta la información requerida de las facturas de pacientes para el reporte
	 * @param con
	 * @param criterios
	 * @return
	 */
	public ArrayList<DtoMovimientoTipoDocumento> consultarFacturasPacientes (Connection con, HashMap criterios);
	
	/**
	 * Metodo que consulta la información requerida de los ingresos para el reporte
	 * @param con
	 * @param criterios
	 * @return
	 */
	public ArrayList<DtoMovimientoTipoDocumento> consultarIngresos (Connection con, HashMap criterios);
	
	/**
	 * Metodo que consulta la información requerida de los recibos de caja para el reporte
	 * @param con
	 * @param criterios
	 * @return
	 */
	public ArrayList<DtoMovimientoTipoDocumento> consultarRecibosCaja (Connection con, HashMap criterios);
	
	/**
	 * Metodo que consulta la información requerida de las facturas varias para el reporte
	 * @param con
	 * @param criterios
	 * @return
	 */
	public ArrayList<DtoMovimientoTipoDocumento> consultarFacturasVarias (Connection con, HashMap criterios);
	
}
