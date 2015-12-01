/*
 * Nov 09, 2006
 */
package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebasti�n G�mez 
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * parametrizaci�n de Instituciones SIRC
 */
public interface InstitucionesSircDao 
{
	/**
	 * M�todo que carga las instituciones SIRC por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarInstituciones(Connection con,int institucion);
	
	/**
	 * M�todo que carga los datos de una institucion SIRC
	 * @param con
	 * @param campos (codigo e institucion)
	 * @return
	 */
	public HashMap cargarInstitucion(Connection con,HashMap campos);
	
	/**
	 * M�todo que inserta una institucion SIRC
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertar(Connection con,HashMap campos);
	
	/**
	 * M�todo que modifica una institucion SIRC
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificar(Connection con,HashMap campos);
	
	
	/**
	 * M�todo que elimina una institucion SIRC
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public int eliminar(Connection con,String codigo, int institucion);
		
	
	/**
	 * M�todo que carga los niveles de servicio por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarNivelesServicio(Connection con,int institucion);
}
