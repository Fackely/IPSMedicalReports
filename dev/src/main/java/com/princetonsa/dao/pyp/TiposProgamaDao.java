/*
 * @author armando
 */
package com.princetonsa.dao.pyp;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;

/**
 * 
 * @author armando
 *
 */
public interface TiposProgamaDao 
{

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public abstract HashMap cargarInfomacionBD(Connection con, int institucion);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param activo
	 * @return
	 */
	public abstract boolean insertarRegistro(Connection con, String codigo, int institucion, String descripcion, boolean activo);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param activo
	 * @return
	 */
	public abstract boolean modificarRegistro(Connection con, String codigo, int institucion, String descripcion, boolean activo);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param activo
	 * @return
	 */
	public abstract boolean existeModificacion(Connection con, String codigo, int institucion, String descripcion, boolean activo);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public abstract ResultSetDecorator cargarTipoPrograma(Connection con, String codigo, int institucion);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public abstract boolean eliminarRegistro(Connection con, String codigo, int institucion);

}
