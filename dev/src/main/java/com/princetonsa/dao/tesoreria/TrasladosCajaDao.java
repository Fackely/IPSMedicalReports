package com.princetonsa.dao.tesoreria;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public interface TrasladosCajaDao 
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
	public HashMap busquedaCierresCajasParaTraslado(Connection con, String loginUsuario,  String fechaTraslado, String cajaPpal, int codigoInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param consecutivoTraslado
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap resumen(Connection con, String consecutivoTraslado, int codigoInstitucion);

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
												String horaGeneracionTraslado);
	
	/**
	 * 
	 * @param con
	 * @param trasladosCajaMap
	 * @param consecutivoTrasladoCaja
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean insertarDetalleTrasladoCaja(Connection con, HashMap trasladosCajaMap, String consecutivoTrasladoCaja, int codigoInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param trasladosCajaMap
	 * @param consecutivoTrasladoCaja
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean actualizarCierreCaja(Connection con, HashMap trasladosCajaMap, String consecutivoTrasladoCaja, int codigoInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap listado(Connection con, HashMap criteriosBusquedaMap, int codigoInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param consecutivoTrasladoCaja
	 * @param codigoInstitucion
	 * @return
	 */
	public String[] obtenerFechaHoraUsuarioGeneracionTraslado(Connection con, String consecutivoTrasladoCaja, int codigoInstitucion);
}
