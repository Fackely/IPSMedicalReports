package com.mercury.dao.odontologia;

import java.sql.Connection;
import java.util.HashMap;

import com.mercury.dto.odontologia.DtoCartaDental;

public interface CartaDentalDao
{
	/**
	 * Inserta la informacion de la carta dental
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public int insertarCartaDental(Connection con,HashMap parametros);
	
	/**
	 * Inserta la informacion del diagnostico de la carta dental
	 *  @param Connection con
	 *  @param HashMap parametros  
	 * */
	public int insertarDiagnosticoCartaDental(Connection con, HashMap parametros);
	
	/**
	 * Inserta la informacion del tratamiento de la carta dental
	 *  @param Connection con
	 *  @param HashMap parametros  
	 * */
	public int insertarTratamientoCartaDental(Connection con, HashMap parametros);
	
	/**
	 * Consulta la informacion de la carta dental
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public DtoCartaDental cargarCartaDental(Connection con, HashMap parametros);
	
	/**
	 * Elimina la informarcion de diagnosticos de la carta dental 
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean eliminarDiagnosticoCartaDental(Connection con, HashMap parametros);
	
	/**
	 * Elimina la informarcion de tratamientos de la carta dental 
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean modificarActivoTratamientosCartaDental(Connection con, HashMap parametros);
	
	/**
	 * Actualiza la informacion del diente en los tratamientos y los diagnosticos
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarDienteDiagTrataCartaDental(Connection con, HashMap parametros);
	
	/**
	 * Actualiza la informacion de la superficie de un diagnostico de la carta dental
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public boolean actualizarSuperficieDiagTrataCartaDental(Connection con, HashMap parametros);
}