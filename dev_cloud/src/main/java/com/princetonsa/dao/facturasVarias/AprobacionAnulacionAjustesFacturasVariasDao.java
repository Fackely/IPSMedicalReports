package com.princetonsa.dao.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jaramillo H.
 * Fecha: Agosto de 2008
 */

public interface AprobacionAnulacionAjustesFacturasVariasDao
{

	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public HashMap consultarAjustesFacturasVarias(Connection con, int codigoInstitucionInt);

	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean actualizarAprobacionAnulacionAjusteFacturasVarias(Connection con, HashMap criterios);

	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean generarLogAprobacionAnulacion(Connection con, HashMap criterios);

	/**
	 * @param criterios
	 * @param ajusTodos
	 * @return
	 */
	public String obtenerWhere(HashMap criterios);

}