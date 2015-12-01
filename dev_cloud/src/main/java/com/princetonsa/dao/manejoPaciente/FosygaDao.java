package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

/**
 * Interface de fosyga
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public interface FosygaDao 
{
	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 */
	public HashMap busquedaAvanzada(Connection con, HashMap criteriosBusquedaMap);

	/**
	 * 
	 * @param codigo
	 * @param esAccidenteTransito
	 * @return
	 */
	public String obtenerQueryAnexoGastosTransporte(String codigo, boolean esAccidenteTransito);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param esRegistroAccidenteTransito
	 * @return
	 */
	public boolean existeInfoGastosTransporteMovilizacionVictima(Connection con, int codigo, boolean esRegistroAccidenteTransito);

}
