/*
 * Abril 30, 2007
 */
package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.GeneracionAnexosForecatDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseGeneracionAnexosForecatDao;

/**
 * @author Sebastián Gómez
 *
 * Clase que maneja los métodos propìos de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Generación Anexos Forecat
 */
public class OracleGeneracionAnexosForecatDao implements GeneracionAnexosForecatDao 
{
	/**
	 * Método que consulta el archivo AA : ACCIDENTES O EVENTOS CATASTRÓFICOS Y TERRORISTAS
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultaAA(Connection con,HashMap campos, boolean esAxRips, boolean esCuentaCobro)
	{
		return SqlBaseGeneracionAnexosForecatDao.consultaAA(con, campos, esAxRips, esCuentaCobro);
	}
	
	/**
	 * Método que consulta el archivo VH: VEHÍCULOS
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultaVH(Connection con,HashMap campos,boolean esAxRips, boolean esCuentaCobro)
	{
		return SqlBaseGeneracionAnexosForecatDao.consultaVH(con, campos, esAxRips,  esCuentaCobro);
	}
	
	/**
	 * Método que consulta el archivo AV: ATENCIÓN DE LA VÍCTIMA
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultaAV(Connection con,HashMap campos, boolean esAxRips, boolean esCuentaCobro)
	{
		return SqlBaseGeneracionAnexosForecatDao.consultaAV(con, campos,esAxRips,  esCuentaCobro);
	}
}
