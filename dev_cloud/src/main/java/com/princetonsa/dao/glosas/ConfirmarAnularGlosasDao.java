package com.princetonsa.dao.glosas;

import java.sql.Connection;
import java.util.HashMap;

public interface ConfirmarAnularGlosasDao
{
	/**
	 * Metodo que consulta los Convenios
	 * @param con
	 * @return
	 */
	public HashMap consultarConvenios(Connection con);
	
	/**
	 * Metodo que valida el detalle y la suma del valor de la Glosa
	 * @param con
	 * @param codGlosa
	 * @param valor
	 * @return
	 */
	public boolean validarAnuConfGlosa(Connection con, String codGlosa, String valor);
	
	/**
	 * Metodo que actualiza la glosa segun estado
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean guardar(Connection con, HashMap criterios);
	
	/**
	 * Metodo que consulta las Faturas de una Glosa 
	 * @param con
	 * @param codGlosa
	 * @return
	 */
	public HashMap consultarFacturasGlosa(Connection con, String codGlosa);
	
	/**
	 * Metodo para Consultar las solicitudes asociadas a una factura de la Glosa
	 * @param connection
	 * @return
	 */
	public int consultaDetalleFacturaGlosa(Connection con, String codFac);
}