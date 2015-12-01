package com.princetonsa.dao.capitacion;

import java.sql.Connection;
import java.util.HashMap;

public interface UnidadPagoDao {

	/**
	 * Metodo para consultar la informacion de la funcionalidad.
	 * @param con
	 * @param mapaParam
	 * @return
	 */
	public HashMap consultarInformacion(Connection con, HashMap mapaParam);

	/**
	 * Metodo para insertar / modificar 
	 * @param con
	 * @param tipoAccion
	 * @param codigo 
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param valor
	 * @return
	 */
	public int insertar(Connection con, int tipoAccion, int codigo, String fechaInicial, String fechaFinal, String valor);
}
