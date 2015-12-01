package com.princetonsa.dao.glosas;

import java.sql.Connection;
import java.util.HashMap;

public interface AprobarGlosasDao
{
	/**
	 * Metodo que consulta los Convenios
	 * @param con
	 * @return
	 */
	public HashMap consultarConvenios(Connection con);
	
	/**
	 * Metodo que valida el detalle de la factura y la suma del valor de la Glosa
	 * @param con
	 * @param codGlosa
	 * @param valor
	 * @return
	 */
	public boolean validarAprobarGlosa(Connection con, String codGlosa, String valor);

	/**
	 * Guardar GlosaS
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean guardar(Connection con, HashMap<String, Object> criterios);
}