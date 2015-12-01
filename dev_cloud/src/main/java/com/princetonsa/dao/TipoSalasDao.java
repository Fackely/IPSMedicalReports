/*
 * Created on Sep 1, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebasti�n G�mez R
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * parametrizaci�n Tipos de Salas
 */
public interface TipoSalasDao {
	
	/**
	 * M�todo usado para cargar todos los tipos de salas existentes
	 * seg�n la instituci�n
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarTiposSalas(Connection con,int institucion);
	
	/**
	 * M�todo usado para ingresar al sistema un nuevo tipo de sala
	 * @param con
	 * @param descripcion
	 * @param quirurgica
	 * @param urgencias
	 * @param institucion
	 * @return
	 */
	public int insertarTipoSala(Connection con,String descripcion,boolean quirurgica,boolean urgencias,int institucion);
	
	/**
	 * M�todo usado para actualizar los datos de un registro
	 * en la tabla tipos_salas
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param quirurgica
	 * @return
	 */
	public int actualizarTipoSala(Connection con,int codigo,String descripcion,boolean quirurgica, boolean urgencias);
	
	/**
	 * M�todo usado para eliminar un registro en la tabla tipos_salas
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarTipoSala(Connection con,int codigo);
	
	/**
	 * M�todo usado para cargar los datos de un tipo de sala
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap cargarTipoSala(Connection con,int codigo);
	
	/**
	 * M�todo encargado de consultar los datos tipo salas que est�n usados en grupo servicios
	 * @param Connection con
	 * @param int codigo
	 * @return HashMap
	 */
	public HashMap consultaTipoSalasGruposServicios(Connection con, int codigo);
	

}
