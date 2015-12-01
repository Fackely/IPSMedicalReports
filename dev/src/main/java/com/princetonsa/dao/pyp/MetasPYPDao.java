/*
 * Ago 09, 2006
 */
package com.princetonsa.dao.pyp;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebasti�n G�mez 
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * parametrizaci�n de Metas PYP
 */
public interface MetasPYPDao 
{
	/**
	 * M�todo implementado para consultar las actividades del programa, convenio, y a�o espec�ficos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarActividades(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para consultar los centros de atencion de una actividad, programa, convenio y a�o espec�ficos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarCentrosAtencion(Connection con,HashMap campos);
	
	/**
	 * M�todo que consulta las ocupaciones del centro atencion, actividad, programa, convenio y a�o espec�fico
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarOcupaciones(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para insertar una actividad de un programa, convenio y a�o espec�fico
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarActividades(Connection con,HashMap campos);
	
	/**
	 * M�todo que insertar un centro de atencion para una actividad, programa, convenio y anio espec�fico
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarCentrosAtencion(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para insertar un ocupacion de una actividad, programa, convenio, a�o
	 * espec�ficos
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertarOcupaciones(Connection con,HashMap campos);
	
	/**
	 * M�todo implementado para modificar una actividad, centro atencion o ocupacion m�dica
	 * pertenecientes a un programa, convenio y a�o espec�ficos.
	 * El tipo de modificacion se define con el atributo 'tipo' en el mapa campos
	 * los valor pueden ser : actividad, centroAtencion y ocupacionMedica
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificar(Connection con,HashMap campos);
	
	/**
	 * M�todo que elimina un centro de atencion o una ocupacion pertenecientes a
	 * una actividad, programa , convenio y a�o espec�ficos
	 * El tipo de modificacion se define con el atributo 'tipo' en el mapa campos
	 * los valores pueden ser : centroAtencion o ocupacionMedica
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminar(Connection con,HashMap campos);
	
	/**
	 * M�todo que consulta las actividades de un programa por convenio
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap cargarActividadesProgramaConvenio(Connection con,HashMap campos);
}
