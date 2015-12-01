/*
 * Created on 15/09/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao;

import java.sql.Connection;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author artotor
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface CajasCajerosDao 
{

	/**
	 * @param con
	 * @param codigoInstitucionInt
	 * @param centroAtencion
	 * @return
	 */
	public ResultSetDecorator cargarInformacion(Connection con, int codigoInstitucionInt, int centroAtencion);

	/**
	 * @param con
	 * @param consecutivoCaja
	 * @param loginUsuario
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int consecutivoCaja, String loginUsuario);

	/**
	 * @param con
	 * @param consecutivoCaja
	 * @param loginUsuario
	 * @param activo
	 * @return
	 */
	public boolean existeModificacion(Connection con, int consecutivoCaja, String loginUsuario, boolean activo);

	/**
	 * @param con
	 * @param consecutivoCaja
	 * @param loginUsuario
	 * @param tempActivo
	 * @return
	 */
	public boolean modificarRegistro(Connection con, int consecutivoCaja, String loginUsuario, boolean tempActivo);

	/**
	 * @param con
	 * @param caja
	 * @param loginUsuario
	 * @param activo
	 * @param codigoInstitucionInt
	 * @return
	 */
	public boolean insertarRegistro(Connection con, int caja, String loginUsuario, boolean activo, int codigoInstitucionInt);

}
