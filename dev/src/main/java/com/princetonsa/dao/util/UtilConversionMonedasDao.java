package com.princetonsa.dao.util;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dto.administracion.DtoTiposMoneda;

/**
 * 
 * @author wilson
 *
 */
public interface UtilConversionMonedasDao 
{
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param mostrarMonedaManejaInstitucion
	 * @return
	 */
	public HashMap obtenerTiposMonedaTagMap( Connection con, int codigoInstitucion, boolean mostrarMonedaManejaInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public DtoTiposMoneda obtenerTipoMonedaManejaInstitucion(Connection con, int codigoInstitucion);

	
}
