package com.princetonsa.dao.postgresql.tesoreria;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.tesoreria.SqlBaseTrasladosCajaDao;
import com.princetonsa.dao.tesoreria.TrasladosCajaDao;

/**
 * 
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public class PostgresqlTrasladosCajaDao implements TrasladosCajaDao
{
	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @param fechaTraslado
	 * @param cajaPpal
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap busquedaCierresCajasParaTraslado(Connection con, String loginUsuario,  String fechaTraslado, String cajaPpal, int codigoInstitucion)
	{
		return SqlBaseTrasladosCajaDao.busquedaCierresCajasParaTraslado(con, loginUsuario, fechaTraslado, cajaPpal, codigoInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoTraslado
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap resumen(Connection con, String consecutivoTraslado, int codigoInstitucion)
	{
		return SqlBaseTrasladosCajaDao.resumen(con, consecutivoTraslado, codigoInstitucion);
	}
	
	/**
	 * metodo que inserta el traslado basico y devuelve el consecutivo de insercion, si es "" entonces error
	 * @param con
	 * @param codigoInstitucion
	 * @param fechsTraslado
	 * @param cajaPpal
	 * @param cajaMayor
	 * @param loginUsuario
	 * @param fechaGeneracionTraslado
	 * @param horaGeneracionTraslado
	 * @return
	 */
	public String insertarTrasladoCaja(	Connection con,
												int codigoInstitucion, 
												String fechaTraslado, 
												String cajaPpal, 
												String cajaMayor, 
												String loginUsuario, 
												String fechaGeneracionTraslado, 
												String horaGeneracionTraslado)
	{
		return SqlBaseTrasladosCajaDao.insertarTrasladoCaja(con, codigoInstitucion, fechaTraslado, cajaPpal, cajaMayor, loginUsuario, fechaGeneracionTraslado, horaGeneracionTraslado);
	}
	
	/**
	 * 
	 * @param con
	 * @param trasladosCajaMap
	 * @param consecutivoTrasladoCaja
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean insertarDetalleTrasladoCaja(Connection con, HashMap trasladosCajaMap, String consecutivoTrasladoCaja, int codigoInstitucion)
	{
		return SqlBaseTrasladosCajaDao.insertarDetalleTrasladoCaja(con, trasladosCajaMap, consecutivoTrasladoCaja, codigoInstitucion);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param trasladosCajaMap
	 * @param consecutivoTrasladoCaja
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean actualizarCierreCaja(Connection con, HashMap trasladosCajaMap, String consecutivoTrasladoCaja, int codigoInstitucion)
	{
		return SqlBaseTrasladosCajaDao.actualizarCierreCaja(con, trasladosCajaMap, consecutivoTrasladoCaja, codigoInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap listado(Connection con, HashMap criteriosBusquedaMap, int codigoInstitucion)
	{
		return SqlBaseTrasladosCajaDao.listado(con, criteriosBusquedaMap, codigoInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoTrasladoCaja
	 * @param codigoInstitucion
	 * @return
	 */
	public String[] obtenerFechaHoraUsuarioGeneracionTraslado(Connection con, String consecutivoTrasladoCaja, int codigoInstitucion)
	{
		return SqlBaseTrasladosCajaDao.obtenerFechaHoraUsuarioGeneracionTraslado(con, consecutivoTrasladoCaja, codigoInstitucion);
	}
}
