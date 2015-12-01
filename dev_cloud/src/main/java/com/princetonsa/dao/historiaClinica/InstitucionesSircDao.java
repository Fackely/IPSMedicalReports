/*
 * Nov 09, 2006
 */
package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * parametrización de Instituciones SIRC
 */
public interface InstitucionesSircDao 
{
	/**
	 * Método que carga las instituciones SIRC por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarInstituciones(Connection con,int institucion);
	
	/**
	 * Método que carga los datos de una institucion SIRC
	 * @param con
	 * @param campos (codigo e institucion)
	 * @return
	 */
	public HashMap cargarInstitucion(Connection con,HashMap campos);
	
	/**
	 * Método que inserta una institucion SIRC
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertar(Connection con,HashMap campos);
	
	/**
	 * Método que modifica una institucion SIRC
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificar(Connection con,HashMap campos);
	
	
	/**
	 * Método que elimina una institucion SIRC
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public int eliminar(Connection con,String codigo, int institucion);
		
	
	/**
	 * Método que carga los niveles de servicio por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarNivelesServicio(Connection con,int institucion);
}
