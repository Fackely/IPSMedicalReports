/*
 * Created on 12/09/2005
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
public interface CajasDao 
{

	/**
	 * Metodo que realiza la consula de la tabla cajas y retorna un 
	 * ResultSetDecorator con la informacion
	 * @param con
	 * @param institucion
	 * @param centroAtencion
	 * @return ResultSet
	 */
	public ResultSetDecorator cargarInformacion(Connection con,int institucion, int centroAtencion);

	/**
	 * @param con
	 * @param consecutivoCaja
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int consecutivoCaja);

	/**
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param activo
	 * @param valorBase
	 * @return
	 */
	public boolean modificarRegistro(Connection con, int consecutivo, int codigo, String descripcion, int tipo, boolean activo, Double valorBase);

	/**
	 * @param con
	 * @param codigo
	 * @param codigoInstitucionInt
	 * @param descripcion
	 * @param tipo
	 * @param activo
	 * @param centroAtencion
	 * @param valorBase
	 * @return
	 */
	public boolean insertarRegistro(Connection con, int codigo, int codigoInstitucionInt, String descripcion, int tipo, boolean activo, int centroAtencion, Double valorBase);

	/**
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param activo
	 * @param valorBase
	 * @return
	 */
	public boolean existeModificacion(Connection con, int consecutivo, int codigo, String descripcion, int tipo, boolean activo, Double valorBase);

	/**
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public ResultSetDecorator cargarCaja(Connection con, int consecutivo);

}
