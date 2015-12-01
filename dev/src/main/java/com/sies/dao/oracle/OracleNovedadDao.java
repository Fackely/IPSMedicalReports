/*
 * Created on 13/04/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.sies.dao.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import com.sies.dao.NovedadDao;

import com.sies.dao.sqlbase.SqlBaseNovedadDao;


/**
 * @author karenth
 *
 */
public class OracleNovedadDao implements NovedadDao{

	public int insertarNovedad(Connection con, String nombre, String descripcion, boolean nomina, boolean activo)
	{
		return SqlBaseNovedadDao.insertarNovedad(con, nombre, descripcion, nomina, activo);
	}
	
	public  Collection<HashMap<String, Object>> consultarEsta(Connection con, String nombre)
	{
		return SqlBaseNovedadDao.consultarEsta(con, nombre);
	}
	
	public Collection<HashMap<String, Object>> consultarNovedades(Connection con)
	{
		return SqlBaseNovedadDao.consultarNovedades(con);
	}
	
	
	public  ResultSet consultarModificar(Connection con, int codigo)
	{
	 return SqlBaseNovedadDao.consultarModificar(con, codigo);
	}

	
	public void modificar(Connection con, int codigo, String nombre, String descripcion, boolean nomina, boolean activo)
	{
		SqlBaseNovedadDao.modificar(con, codigo, nombre, descripcion, nomina, activo);
	}
	
	/*******************  Asignar Novedad a Enfermera **************************/
	
	/**
	 * Consulta las novedades que concuerda con la enfermera y una fecha
	 */
	public Collection<HashMap<String, Object>> consultarNovedadEnfermera(Connection con, int codigoMedico, String fechaProgramacion)
	{
	    return SqlBaseNovedadDao.consultarNovedadEnfermera(con, codigoMedico, fechaProgramacion);
	}
	
	/**
	 * Método que permite insertar una nueva asociación
	 * de una enfermera a una novedad
	 */
	public int insertarNovedadEnfermera(Connection con, int codigoEnfermera, int codigo, boolean prioridad, String fechaProgramacion, String observacion)
	{
	    return SqlBaseNovedadDao.insertarNovedadEnfermera(con, codigoEnfermera, codigo, prioridad, fechaProgramacion, observacion);
	}
	
	
	
	/**
	 * Metodo que llama el sql base para cambiar de estado
	 *  una asociación de novedad a Enfermera
	 * 
	 */
	public int inactivarAsociacion(Connection con, int codigoAsociacion, boolean activo)
	{
	    return SqlBaseNovedadDao.cambiarEstadoAsociacion(con, codigoAsociacion, activo);
	}
	
	/**
	 * Consulta las novedades que se encuentran asociadas a una enfermera, que aun est activas
	 * y que su fecha de programación es mayor o igual al día de hoy
	 */
	public Collection<HashMap<String, Object>> consultarNovEnfermera(Connection con, int codigoEnfermera, String fechaInicio, String fechaFin)
	{
	    return SqlBaseNovedadDao.consultarNovEnfermera(con, codigoEnfermera, fechaInicio, fechaFin);
	}
	
	/**
	 * Consulta todas las novedades que tiene una enfermera
	 */
	public Collection<HashMap<String, Object>> consultarTodasNovedadesEnfermera(Connection con, int codigoMedico)
	{
		return SqlBaseNovedadDao.consultarTodasNovedadesEnfermera(con, codigoMedico);
	}

	
	/**
	 * Metodo para consultar una asociacion específicamente
	 */
	public Collection<HashMap<String, Object>> consultarEstaNovEnf(Connection con, int codigoAsociacion)
	{
	    return SqlBaseNovedadDao.consultarEstaNovEnf(con, codigoAsociacion);
	}
	
	/**
	 * Metodo para modificar una asociación de novedad a enfermera
	 */
	public int modificarNovedadEnfermera(Connection con, int codigoPersona, int codigo, String fechaProgramacion, boolean prioridad, String observacion)
	{
	    return SqlBaseNovedadDao.modificarNovedadEnfermera(con, codigoPersona, codigo, fechaProgramacion, prioridad, observacion);
	}

	/**
	 * Metodo para consultar las novedades que tiene asociada una enfermera en un periodo determinado
	 * incluida la opcion de todas las enfermeras
	 * @param con
	 * @param codigoProfesional
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoInstitucion
	 * @return
	 */
	public Collection<HashMap<String, Object>> consultarTurnosReporteNov(Connection con, int codigoProfesional, String fechaInicio, String fechaFin, String fechaProgramacion, int codigoNovedad)
	{
		return SqlBaseNovedadDao.consultarTurnosReporteNov(con, codigoProfesional, fechaInicio, fechaFin, fechaProgramacion, codigoNovedad);
	}

	public boolean consultarNovedadEnfermeraTieneTurno(Connection con, int codigoRegistro) throws SQLException
	{
		return SqlBaseNovedadDao.consultarNovedadEnfermeraTieneTurno(con, codigoRegistro);
	}

	public int eliminarNovedad(Connection con, int codigo) {
		return SqlBaseNovedadDao.eliminarNovedad(con, codigo);
	}

	public int cambiarEstadoAsociacion(Connection con, String fechaInicio, String fechaFin, int codigoProfesional, boolean activo)
	{
		return SqlBaseNovedadDao.cambiarEstadoAsociacion(con, fechaInicio, fechaFin, codigoProfesional, activo);
	}
}
