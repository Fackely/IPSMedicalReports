package com.princetonsa.dao.glosas;

import java.sql.Connection;

import java.util.HashMap;

/**
 * Anexo 685
 * Fecha: Septiembre de 2008
 * @author Ing. Felipe Perez Granda
 * @mail lfperez@princetonsa.com
 */


public interface ConceptosEspecificosDao
{
	/**
	 * Metodo encargado de consultar la informacion de
	 * conceptos especificos glosas, en la tabla "conceptos_especificos".
	 * @author Felipe Perez
	 * @param connection
	 * @param criterios
	 * ---------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------
	 * -- institucion --> Requerido
	 * -- action --> Opcional
	 * @return mapa
	 * --------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * --------------------------------
	 * codigo_,consecutivo_,descripcion_,
	 * activo_,estaBd_
	 */
	public  HashMap consultaConceptosEspecificos (Connection connection,HashMap criterios);
	
	/**
	 * Metodo encargado de insetar
	 * los datos de conceptos especificos.
	 * @author Felipe Perez
	 * @param connection
	 * @param datos
	 * -----------------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------------
	 * -- consecutivo --> Requerido
	 * -- descripcion --> Requerido
	 * -- activo --> Requerido
	 * -- institucion --> Requerido
	 * @return false/true
	 */
	public boolean insertarConceptosEspecificos (Connection connection, HashMap datos);
	
	/**
	 * M�todo encargado de actualizar la tabla de "conceptos_especificos".
	 * 
	 * 
	 * @author Felipe P�rez
	 * @param connection
	 * @param datos
	 * ------------------------
	 * KEY'S DEL MAPA DATOS
	 * ------------------------
	 * -- codigo		--> requerido
	 * -- consecutivo	--> requerido
	 * -- descripcion	--> requerido
	 * -- activo		--> requerido
	 * -- institucion	--> requerido
	 * -- usuarioModifica -->  Requerido
	 * @return false/true
	 */
	public boolean actualizaConceptosEspecificos (Connection connection,HashMap datos );
	
	/**
	 * M�todo encargado de eliminar datos de la tabla
	 * conceptos_especificos
	 * @author Felipe P�rez
	 * @param connection
	 * @param datos
	 * ------------------------------------
	 * Keys el mapa de datos
	 * -- codigo 		--> Requerido
	 * -- institucion 	--> Requerido
	 * -----------------------------------
	 * @return true/false
	 */
	public boolean eliminarConceptosEspecificos(Connection connection, HashMap datos);
	
	
	
}