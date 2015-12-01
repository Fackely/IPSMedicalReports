/*
 * Abril 30, 2007
 */
package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.GeneracionAnexosForecatDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseGeneracionAnexosForecatDao;

/**
 * @author Sebasti�n G�mez
 *
 * Clase que maneja los m�todos prop�os de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Generaci�n Anexos Forecat
 */
public class OracleGeneracionAnexosForecatDao implements GeneracionAnexosForecatDao 
{
	/**
	 * M�todo que consulta el archivo AA : ACCIDENTES O EVENTOS CATASTR�FICOS Y TERRORISTAS
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultaAA(Connection con,HashMap campos, boolean esAxRips, boolean esCuentaCobro)
	{
		return SqlBaseGeneracionAnexosForecatDao.consultaAA(con, campos, esAxRips, esCuentaCobro);
	}
	
	/**
	 * M�todo que consulta el archivo VH: VEH�CULOS
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultaVH(Connection con,HashMap campos,boolean esAxRips, boolean esCuentaCobro)
	{
		return SqlBaseGeneracionAnexosForecatDao.consultaVH(con, campos, esAxRips,  esCuentaCobro);
	}
	
	/**
	 * M�todo que consulta el archivo AV: ATENCI�N DE LA V�CTIMA
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultaAV(Connection con,HashMap campos, boolean esAxRips, boolean esCuentaCobro)
	{
		return SqlBaseGeneracionAnexosForecatDao.consultaAV(con, campos,esAxRips,  esCuentaCobro);
	}
}
