/*
 * Ago 09, 2006
 */
package com.princetonsa.dao.pyp;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * parametrización de Metas PYP
 */
public interface MetasPYPDao 
{
	/**
	 * Método implementado para consultar las actividades del programa, convenio, y año específicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarActividades(Connection con,HashMap campos);
	
	/**
	 * Método implementado para consultar los centros de atencion de una actividad, programa, convenio y año específicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarCentrosAtencion(Connection con,HashMap campos);
	
	/**
	 * Método que consulta las ocupaciones del centro atencion, actividad, programa, convenio y año específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarOcupaciones(Connection con,HashMap campos);
	
	/**
	 * Método implementado para insertar una actividad de un programa, convenio y año específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarActividades(Connection con,HashMap campos);
	
	/**
	 * Método que insertar un centro de atencion para una actividad, programa, convenio y anio específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarCentrosAtencion(Connection con,HashMap campos);
	
	/**
	 * Método implementado para insertar un ocupacion de una actividad, programa, convenio, año
	 * específicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarOcupaciones(Connection con,HashMap campos);
	
	/**
	 * Método implementado para modificar una actividad, centro atencion o ocupacion médica
	 * pertenecientes a un programa, convenio y año específicos.
	 * El tipo de modificacion se define con el atributo 'tipo' en el mapa campos
	 * los valor pueden ser : actividad, centroAtencion y ocupacionMedica
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificar(Connection con,HashMap campos);
	
	/**
	 * Método que elimina un centro de atencion o una ocupacion pertenecientes a
	 * una actividad, programa , convenio y año específicos
	 * El tipo de modificacion se define con el atributo 'tipo' en el mapa campos
	 * los valores pueden ser : centroAtencion o ocupacionMedica
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminar(Connection con,HashMap campos);
	
	/**
	 * Método que consulta las actividades de un programa por convenio
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarActividadesProgramaConvenio(Connection con,HashMap campos);
}
