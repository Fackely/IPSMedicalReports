/*
 * @author armando
 */
package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;

/**
 * 
 * @author artotor
 *
 */
public interface CategoriasTriageDao 
{
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public abstract ResultSetDecorator cargarInformacion(Connection con, int institucion);

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public abstract boolean eliminarRegistro(Connection con, int consecutivo);

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param color
	 * @return
	 */
	public abstract boolean existeModificacion(Connection con, int consecutivo, int codigo, String descripcion, int color);

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public abstract ResultSetDecorator cargarCategoriaTriage(Connection con, int consecutivo);

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param codigo
	 * @param descripcion
	 * @param color
	 * @return
	 */
	public abstract boolean modificarRegistro(Connection con, int consecutivo, String codigo, String descripcion, int color);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param color
	 * @return
	 */
	public abstract boolean insertarRegistro(Connection con, String codigo, int institucion, String descripcion, int color);

	/**
	 * 
	 * @param con
	 * @param categoria
	 * @param destinos
	 */
	public abstract boolean actualizarRelacionesCategoriasDestinos(Connection con, int categoria, HashMap destinos);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param color
	 * @return
	 */
	public abstract int obtenerConsecutivoCategoriaTriage(Connection con, String codigo, int institucion, String descripcion, int color);

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @param destinos
	 * @return
	 */
	public abstract boolean insertarRelacionesCategoriasDestinos(Connection con, int consecutivo, HashMap destinos);

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public abstract HashMap cargarRelacionesDestionsCategorias(Connection con, String consecutivo);

}
