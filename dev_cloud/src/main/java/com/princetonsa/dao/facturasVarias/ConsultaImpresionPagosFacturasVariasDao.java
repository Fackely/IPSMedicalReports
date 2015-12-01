package com.princetonsa.dao.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;

/**
 * Fecha: Abril de 2008
 * @author Mauricio Jaramillo
 */

public interface ConsultaImpresionPagosFacturasVariasDao 
{

	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @return
	 */
	public abstract HashMap buscarPagosFacturasVarias(Connection con, int codigoInstitucionInt, HashMap criterios);

	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @param codigoAplicacion
	 * @return
	 */
	public abstract HashMap buscarConceptos(Connection con, int codigoInstitucionInt, int codigoAplicacion);

	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @param codigoAplicacion
	 * @return
	 */
	public abstract HashMap buscarFacturas(Connection con, int codigoInstitucionInt, int codigoAplicacion);
	
}
