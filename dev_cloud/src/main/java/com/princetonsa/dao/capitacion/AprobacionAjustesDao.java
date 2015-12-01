/*
 * Creado el Jun 30, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.capitacion;

import java.sql.Connection;
import java.util.HashMap;

public interface AprobacionAjustesDao {

	
	/**
	 * Metodo Para Cargar Informacion Parametrizada.
	 * @param con
	 * @param mapaParam
	 * @return
	 */
	public HashMap cargarInformacion(Connection con, HashMap mapaParam);

	/**
	 * Metodo para Aprobar el Ajuste Seleccionado.
	 * @param con
	 * @param mapaParam
	 * @return
	 */
	public int aprobarAjuste(Connection con, HashMap mapaParam);

}
