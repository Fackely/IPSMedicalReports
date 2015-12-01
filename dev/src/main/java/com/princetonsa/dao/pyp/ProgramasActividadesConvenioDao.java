/*
 * Ago 08/2006
 */
package com.princetonsa.dao.pyp;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * parametrización de programas y actividades por convenio
 */
public interface ProgramasActividadesConvenioDao 
{
	/**
	 * Método implementado para consultar los programas y actividades por convenio
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultar(Connection con,HashMap campos);
	
	/**
	 * Método implementado para insertar un programa y actividad por convenio
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertar(Connection con,HashMap campos);
	
	/**
	 * Método implementado para modificar un programa y actividad por convenio
	 * 
	 * @param con
	 * @return
	 */
	public int modificar(Connection con,HashMap campos);
	
	/**
	 * Método implementado para eliminar un programa y actividad por convenio
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminar(Connection con,HashMap campos);
	
	/**
	 * Método implementado para cargar  las actividades de un programa
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarActividadesPrograma(Connection con,HashMap campos);

}
