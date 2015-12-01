package com.princetonsa.dao.oracle.util;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.util.SqlBaseUtilConversionMonedasDao;
import com.princetonsa.dao.util.UtilConversionMonedasDao;
import com.princetonsa.dto.administracion.DtoTiposMoneda;

/**
 * 
 * @author wilson
 *
 */
public class OracleUtilConversionMonedasDao implements UtilConversionMonedasDao 
{
	/**
	 * 
	 */
	public HashMap obtenerTiposMonedaTagMap( Connection con, int codigoInstitucion, boolean mostrarMonedaManejaInstitucion)
	{
		return SqlBaseUtilConversionMonedasDao.obtenerTiposMonedaTagMap(con, codigoInstitucion, mostrarMonedaManejaInstitucion, DaoFactory.ORACLE);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public DtoTiposMoneda obtenerTipoMonedaManejaInstitucion(Connection con, int codigoInstitucion)
	{
		return SqlBaseUtilConversionMonedasDao.obtenerTipoMonedaManejaInstitucion(con, codigoInstitucion, DaoFactory.ORACLE);
	}
}
