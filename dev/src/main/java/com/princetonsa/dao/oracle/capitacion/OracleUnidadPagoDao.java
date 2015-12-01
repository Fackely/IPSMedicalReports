package com.princetonsa.dao.oracle.capitacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.capitacion.UnidadPagoDao;
import com.princetonsa.dao.sqlbase.capitacion.SqlBaseUnidadPagoDao;

public class OracleUnidadPagoDao implements UnidadPagoDao {

	/**
	 * Funcion para cosultar informacion 
	 * @param con
	 * @param mapaParam
	 * @return
	 */
	public HashMap consultarInformacion(Connection con, HashMap mapaParam)
	{
		return SqlBaseUnidadPagoDao.consultarInformacion(con, mapaParam);
	}
	
	/**
	 * Metodo para insertar / modificar 
	 * @param con
	 * @param tipoAccion
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param valor
	 * @return
	 */
	public int insertar(Connection con, int tipoAccion, int codigo, String fechaInicial, String fechaFinal, String valor)
	{
		return SqlBaseUnidadPagoDao.insertar(con, tipoAccion, codigo, fechaInicial, fechaFinal, valor);
	}

}
