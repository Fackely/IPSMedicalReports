/*
 * Created on 13/04/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sies.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author karenth
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface NovedadDao {
	public int insertarNovedad(Connection con, String nombre, String descripcion, boolean nomina, boolean activo);
	public Collection<HashMap<String, Object>> consultarEsta(Connection con, String nombre);
	public void modificar(Connection con, int codigo, String nombre, String descripcion, boolean nomina, boolean activo);
	public Collection<HashMap<String, Object>> consultarNovedades(Connection con);
	public ResultSet consultarModificar(Connection con, int codigo);
	public int eliminarNovedad(Connection con, int codigo);
	
	/***********Asignar Novedad Enfermera*******************/
	
	/**
	 * Consulta las novedades que concuerda con la enfermera y una fecha
	 */
	public Collection<HashMap<String, Object>> consultarNovedadEnfermera(Connection con, int codigoEnfermera, String fechaProgramacion);
	
	
	/**
	 * Metodo que permite insertar una nueva asociacion
	 * de una enfermera a una novedad
	 * @param con
	 * @param codigoEnfermera
	 * @param codigo
	 * @param prioridad
	 * @param fechaProgramacion
	 * @param fechaRegistro
	 * @param activoAsociacion
	 * @return
	 */
	public int insertarNovedadEnfermera(Connection con, int codigoEnfermera, int codigo, boolean prioridad, String fechaProgramacion, String observacion);
	
	
	/**
	 * Este método permite cambiar de estado una asociación de una novedad a una nfermera
	 * @return
	 */
	public int inactivarAsociacion(Connection con, int codigoAsociacion, boolean activo );
	

	/**
	 * Consulta las novedades que se encuentran asociadas a una enfermera, que aun est activas
	 * y que su fecha de programación es mayor o igual al día de hoy
	 * @param con
	 * @param codigoEnfermera
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarNovEnfermera(Connection con, int codigoEnfermera, String fechaInicio, String fechaFin);
	
	/**
	 * Consulta todas las novedades que tiene una enfermera
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarTodasNovedadesEnfermera(Connection con, int codigoMedico);
	
	
	/**
	 * Metodo para consultar una asociacion específicamente
	 * @param con
	 * @param codigoAsociacion
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarEstaNovEnf(Connection con, int codigoAsociacion);
	
	/**
	 * Metodo para modificar los datos de la tabla novedad_enfermera
	 * @param con
	 * @param codigoPersona
	 * @param codigo
	 * @param fechaProgramacion
	 * @param prioridad
	 * @param observacion
	 * @return
	 */
	public int modificarNovedadEnfermera(Connection con, int codigoPersona, int codigo, String fechaProgramacion, boolean prioridad, String observacion);
	
	/**
	 * Método para consultar las novedades que tiene asociada una enfermera en un periodo determinado
	 * incluida la opcion de todas las enfermeras
	 * @param con
	 * @param codigoProfesional
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoInstitucion
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarTurnosReporteNov(Connection con, int codigoProfesional, String fechaInicio, String fechaFin, String fechaProgramacion, int codigoNovedad);
	
	public boolean consultarNovedadEnfermeraTieneTurno(Connection con, int codigoRegistro) throws SQLException;

	/**
	 * Método que cambia las novedades de una persona en un rango de fechas específico
	 * @param con
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoProfesional
	 * @param activo
	 * @return
	 */
	public int cambiarEstadoAsociacion(Connection con, String fechaInicio, String fechaFin, int codigoProfesional, boolean activo);
}
