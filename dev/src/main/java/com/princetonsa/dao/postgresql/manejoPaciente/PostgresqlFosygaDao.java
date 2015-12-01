package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.FosygaDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseFosygaDao;

/**
 * 
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public class PostgresqlFosygaDao implements FosygaDao
{
	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 */
	public HashMap busquedaAvanzada(Connection con, HashMap criteriosBusquedaMap)
	{
		return SqlBaseFosygaDao.busquedaAvanzada(con, criteriosBusquedaMap);
	}
	
	/**
	 * 
	 */
	public String obtenerQueryAnexoGastosTransporte(String codigo, boolean esAccidenteTransito) 
	{
		return SqlBaseFosygaDao.obtenerQueryAnexoGastosTransporte(codigo, esAccidenteTransito);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param esRegistroAccidenteTransito
	 * @return
	 */
	public boolean existeInfoGastosTransporteMovilizacionVictima(Connection con, int codigo, boolean esRegistroAccidenteTransito)
	{
		return SqlBaseFosygaDao.existeInfoGastosTransporteMovilizacionVictima(con, codigo, esRegistroAccidenteTransito);
	}

}
