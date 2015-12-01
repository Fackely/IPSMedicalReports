/*
 * Creado en Aug 8, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao.oracle.pyp;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.pyp.CalificacionesXCumpliMetasDao;
import com.princetonsa.dao.sqlbase.pyp.SqlBaseCalificacionesXCumpliMetasDao;

public class OracleCalificacionesXCumpliMetasDao implements CalificacionesXCumpliMetasDao
{ 

	/**
	 * Método que consulta las calificaciones por cumplimiento de metas ingresadas para el 
	 * régimen seleccionado y la institución actual
	 * @param con
	 * @param tipoRegimen
	 * @param codigoInstitucion
	 */
	public HashMap consultarCalificacionesXRegimen(Connection con, String tipoRegimen, int codigoInstitucion)
	{
		return SqlBaseCalificacionesXCumpliMetasDao.consultarCalificacionesXRegimen (con, tipoRegimen, codigoInstitucion);
	}
	
	/**
	 *	Método que elimina el registro seleccionado de la tabla
	 * @param con
	 * @param codigoCalificacion
	 * @return
	 */
	public int eliminarCalificacionXCumplimiento(Connection con, int codigoCalificacion)
	{
		return SqlBaseCalificacionesXCumpliMetasDao.eliminarCalificacionXCumplimiento (con, codigoCalificacion);
	}
	
	/**
	 * Método que guarda o actualiza las calificaciones de cumplimieto por metas
	 * registradas en un tipo de régimen y una institución
	 * @param con
	 * @param codigo
	 * @param tipoRegimen
	 * @param meta
	 * @param rangoInicial
	 * @param rangoFinal
	 * @param tipoCalificacion
	 * @param activo
	 * @param codigoInstitucion
	 * @param esInsertar
	 * @return
	 */
	public int insertarModificarCalificacionsXCumplimiento(Connection con, int codigo, String tipoRegimen, String meta, String rangoInicial, String rangoFinal, int tipoCalificacion, boolean activo, int codigoInstitucion, boolean esInsertar)
	{
		return SqlBaseCalificacionesXCumpliMetasDao.insertarModificarCalificacionsXCumplimiento (con, codigo, tipoRegimen, meta, rangoInicial, rangoFinal, tipoCalificacion, activo, codigoInstitucion, esInsertar);
	}
	
}
