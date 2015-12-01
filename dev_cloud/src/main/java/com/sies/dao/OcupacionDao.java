/*
 * Creado el 29/03/2007
 */
package com.sies.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author mono
 */
public interface OcupacionDao {
	
    /**
     * Método que inserta una ocupacion
     * @param con
     * @param ocupacion
     * @param fecha_inicio
     * @param fecha_fin
     * @return
     */
	public int insertarOcupacion (Connection con, int codigo, String nombre);
	
	/**
	 * Metodo que modifica una ocupacion
	 * @param con
     * @param ocupacion
     * @param fecha_inicio
     * @param fecha_fin
	 */
	public void modificarOcupacion (Connection con, int codigo, String nombre);
	
	/**
	 * Metodo que consulta todas las ocupaciones existentes
	 * @param con
	 * @param nombre
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarOcupacion(Connection con);
	
	/**
	 * Metodo que elimina una ocupacion
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarOcupacion (Connection con, int codigo);
	
	/**
	 * Metodo que consulta si una ocupacion existe
	 * @param con
	 * @param nombre
	 */
	public boolean consultaOcupacionExiste(Connection con, String nombre);
		
	/************************** Asignar Salario Base a Ocupación *****************/
	
	/**
	 * Método que modifica el salario o las fechas de una ocupacion
	 * @param con
	 * @param ocupacion
	 * @param valor
	 * @param fechaInicio
	 * @param fechaFin
	 * @param tipoVinculacion
	 * @return
	 */
	public void modificarOcupacionSalarioBase(Connection con, int ocupacion, int valor, String fechaInicio, String fechaFin, int tipoVinculacion);
	
	/**
	 * Método que consulta las Ocupacion con su salario base, fecha inicio y fecha fin
	 * @param con
	 * @param ocupacion
	 */
	public Collection consultarOcupacionSalarioBase(Connection con, int ocupacion);
	
	/**
	 * Metodo que lista las vinculaciones existentes
	 * @param con
	 */
	public Collection<HashMap<String, Object>> listadoTiposVinculaciones(Connection con);
	
	/**
	 * Lista los tipos de turnos existentes
	 * @param con
	 */
	public Collection<HashMap<String, Object>> listadoTiposTurnos (Connection con);
	
}
	