package com.princetonsa.dao.oracle.interfaz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.interfaz.ReporteMovTipoDocDao;
import com.princetonsa.dao.sqlbase.interfaz.SqlBaseReporteMovTipoDocDao;
import com.princetonsa.dto.interfaz.DtoMovimientoTipoDocumento;

/**
 * @author Jairo Gómez Fecha Julio de 2009
 */

public class OracleReporteMovTipoDocDao implements ReporteMovTipoDocDao 
{
	public ArrayList<DtoMovimientoTipoDocumento> consultarFacturasPacientes (Connection con, HashMap criterios)
	{
		return SqlBaseReporteMovTipoDocDao.consultarFacturasPacientes(con, criterios);
	}
	
	public ArrayList<DtoMovimientoTipoDocumento> consultarIngresos (Connection con, HashMap criterios)
	{
		return SqlBaseReporteMovTipoDocDao.consultarIngresos(con, criterios);
	}
	
	public ArrayList<DtoMovimientoTipoDocumento> consultarRecibosCaja (Connection con, HashMap criterios)
	{
		return SqlBaseReporteMovTipoDocDao.consultarRecibosCaja(con, criterios);
	}
	
	public ArrayList<DtoMovimientoTipoDocumento> consultarFacturasVarias (Connection con, HashMap criterios)
	{
		return SqlBaseReporteMovTipoDocDao.consultarFacturasVarias(con, criterios);
	}
}