package util.Administracion;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.util.UtilConversionMonedasDao;
import com.princetonsa.dto.administracion.DtoTiposMoneda;

/**
 * 
 * @author wilson
 *
 */
public class UtilConversionMonedas 
{
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param mostrarMonedaManejaInstitucion
	 * @return
	 */
	public static HashMap obtenerTiposMonedaTagMap( int codigoInstitucion, boolean mostrarMonedaManejaInstitucion)
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap mapa= utilConversionMonedasDao().obtenerTiposMonedaTagMap(con, codigoInstitucion, mostrarMonedaManejaInstitucion);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public static DtoTiposMoneda obtenerTipoMonedaManejaInstitucion(int codigoInstitucion)
	{
		Connection con= UtilidadBD.abrirConexion();
		DtoTiposMoneda dto= utilConversionMonedasDao().obtenerTipoMonedaManejaInstitucion(con, codigoInstitucion);
		UtilidadBD.closeConnection(con);
		return dto;
	}
	
	
	/**
	 * dao
	 * @return
	 */
	public static UtilConversionMonedasDao utilConversionMonedasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilConversionMonedasDao();
	}
}
