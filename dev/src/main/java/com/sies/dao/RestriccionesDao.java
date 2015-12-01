/*
 * Created on 24/05/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sies.dao;

import java.sql.Connection;
import java.util.Collection;

/**
 * @author karenth
 *
 */
public interface RestriccionesDao
{

    /**
	 * Metodo que hace una consulta de todas las restricciones que hay en la base de datos
	 * @param con
	 * @return
	 */
	public Collection consultarRestricciones(Connection con, int tipo);
    
	/*****************************Asignar*************************************/
	
	/**
	 * Método que consulta las restricciones que se encuentran asignadas en el momento 
	 * a determinada categoría
	 * @param con
	 * @param codigo
	 * @return
	 */
	public Collection consultarRestriccionEnfermera(Connection con, int codigoMedico);
	
	
	
	
	/**
	 * Metodo que inserta una los datos en la tabla enfermera_restriccion
	 * @param con
	 * @param codigoRestriccion
	 * @param codigoMedico
	 * @param valor
	 * @param esRestriccion
	 * @param todosDias
	 * @return
	 */
	public int insertarRestriccionPersona(Connection con, int codigoRestriccion, int codigoMedico, String valor, boolean esRestriccion, boolean todosDias);
	
	/**
	 * Metodo que actualiza a la fecha actual la fecha de finalización de la
	 * asociacion de la restriccion a la categoria
	 * @param con
	 * @param codigoRestriccion
	 * @param esRestriccion
	 * @param codigoMedico
	 * @return
	 */
	public int actualizarRestriccionEnfermera(Connection con,  int codigoRestriccion, int codigoCategoria, boolean esRestriccion);
	

	/**
	 * Metodo para consultar las enfermeras que se encuentran activas en el sistema
	 * @param con
	 * @param restriccion
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException
	 */
	public Collection consultarPersonas(Connection con, String restriccion, String codigoInstitucion);
	
	
    
}
 