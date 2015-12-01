package com.princetonsa.dao.oracle.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturasVarias.ConsultaMovimientoDeudorDao;
import com.princetonsa.dao.sqlbase.facturasVarias.SqlBaseConsultaMovimientoDeudorDao;

/**
 * @author Mauricio Jllo
 * Fecha: Agosto de 2008
 */
public class OracleConsultaMovimientoDeudorDao implements ConsultaMovimientoDeudorDao
{

	/**
	 * 
	 */
	public HashMap consultarMovimientosDeudores(Connection con, HashMap criterios) 
	{
		return SqlBaseConsultaMovimientoDeudorDao.consultarMovimientosDeudores(con, criterios);
	}
	
	/**
	 * 
	 */
	public String consultarCondicionesMovimientosDeudor(Connection con, HashMap criterios) 
	{
		return SqlBaseConsultaMovimientoDeudorDao.consultarCondicionesMovimientosDeudor(con, criterios);
	}
	
	/**
	 * 
	 */
	public HashMap consultarDetalleMovimientosDeudor(Connection con, int deudor, String fechaInicial, String fechaFinal)
	{
		return SqlBaseConsultaMovimientoDeudorDao.consultarDetalleMovimientosDeudor(con, deudor, fechaInicial, fechaFinal);
	}
	
}
