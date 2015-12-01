package com.princetonsa.dao.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

/**
 * Fecha: Abril de 2008
 * @author axioma
 * Mauricio Jaramillo H.
 */

public interface AprobacionAnulacionPagosFacturasVariasDao 
{

	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public abstract HashMap consultarAplicacionesPagosFacturasVarias(Connection con, int codigoInstitucionInt);

	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarAprobado(Connection con, HashMap vo);
	
	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarAprobadoFacturas(Connection con, HashMap vo);

	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean modificarAnulado(Connection con, HashMap vo);
	
	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap busquedaAvanzada(Connection con, HashMap vo);
	
	/**
	 * @param con
	 * @param codigoDeudor
	 * @param codigoAplicacionPago
	 * @return
	 */
	public abstract HashMap busquedaFacturasDeudores(Connection con, int codigoDeudor, int codigoAplicacionPago);
	
}
