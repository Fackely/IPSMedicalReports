/*
 * Mayo 03, 2006
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebasti�n G�mez 
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * parametrizaci�n de Unidad Funcional
 */
public interface UnidadFuncionalDao 
{
	/**
	 * M�todo implementado para consultar todas la unidades funcionales 
	 * de la institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap consultar(Connection con,int institucion);
	
	/**
	 * M�todo que consulta los dats de una unidad funcional
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public HashMap consultar(Connection con,String codigo,int institucion);
	
	/**
	 * M�todo implementado para insertar una nueva unidad funcional
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public int insertar(Connection con,String codigo,String descripcion,boolean activo,int institucion);
	
	/**
	 * M�todo implementado para modificar una unidad funcional
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public int modificar(Connection con,String codigo,String descripcion,boolean activo,int institucion);
	
	/**
	 * M�todo implementado para eliminar una unidad funcional
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public int eliminar(Connection con,String codigo,int institucion);
	

}
