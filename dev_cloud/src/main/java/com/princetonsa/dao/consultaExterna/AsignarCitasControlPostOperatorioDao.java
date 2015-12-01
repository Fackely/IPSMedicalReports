package com.princetonsa.dao.consultaExterna;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jllo
 * Fecha Mayo de 2008
 */

public interface AsignarCitasControlPostOperatorioDao
{

	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarCitasReservadas(Connection con, int codigoPaciente);

	/**
	 * @param con
	 * @param usuario
	 * @param mapaSubcuenta
	 * @return
	 */
	public abstract int insertarSubCuenta(Connection con, String usuario, HashMap datosSubCuenta);

	/**
	 * @param con
	 * @param idIngreso
	 * @param updateInsert
	 * @return
	 */
	public abstract boolean controlPostOperatorio(Connection con, int idIngreso);
	
}
