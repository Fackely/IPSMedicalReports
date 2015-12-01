package com.princetonsa.dao.oracle.glosas;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.glosas.ConfirmarAnularGlosasDao;
import com.princetonsa.dao.sqlbase.glosas.SqlBaseConfirmarAnularGlosasDao;


public class OracleConfirmarAnularGlosasDao implements ConfirmarAnularGlosasDao
{
	/**
	 * Metodo para consultar los Convenios
	 */
	public HashMap consultarConvenios(Connection con)
	{
		return SqlBaseConfirmarAnularGlosasDao.consultarConvenios(con);
	}
	
	/**
	 * Metodo para validar detalle y suma valor Glosa
	 * @param con
	 * @param codGlosa
	 * @param valor
	 * @return
	 */
	public boolean validarAnuConfGlosa(Connection con, String codGlosa, String valor)
	{
		return SqlBaseConfirmarAnularGlosasDao.validarAnuConfGlosa(con, codGlosa, valor);
	}
	
	/**
	 * Metodo que actualiza la glosa segun estado
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean guardar(Connection con, HashMap criterios)
	{
		return SqlBaseConfirmarAnularGlosasDao.guardar(con, criterios);
	}
	
	/**
	 * Metodo que consulta las Faturas de una Glosa 
	 * @param con
	 * @param codGlosa
	 * @return
	 */
	public HashMap consultarFacturasGlosa(Connection con, String codGlosa)
	{
		return SqlBaseConfirmarAnularGlosasDao.consultarFacturasGlosa(con, codGlosa);
	}
	
	/**
	 * Metodo para Consultar las solicitudes asociadas a una factura de la Glosa
	 * @param connection
	 * @return
	 */
	public int consultaDetalleFacturaGlosa(Connection con, String codFac)
	{
		return SqlBaseConfirmarAnularGlosasDao.consultaDetalleFacturaGlosa(con, codFac);
	}
}