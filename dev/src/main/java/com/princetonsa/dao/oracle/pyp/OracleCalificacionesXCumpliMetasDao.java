/*
 * Creado en Aug 8, 2006
 * Andr�s Mauricio Ruiz V�lez
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
	 * M�todo que consulta las calificaciones por cumplimiento de metas ingresadas para el 
	 * r�gimen seleccionado y la instituci�n actual
	 * @param con
	 * @param tipoRegimen
	 * @param codigoInstitucion
	 */
	public HashMap consultarCalificacionesXRegimen(Connection con, String tipoRegimen, int codigoInstitucion)
	{
		return SqlBaseCalificacionesXCumpliMetasDao.consultarCalificacionesXRegimen (con, tipoRegimen, codigoInstitucion);
	}
	
	/**
	 *	M�todo que elimina el registro seleccionado de la tabla
	 * @param con
	 * @param codigoCalificacion
	 * @return
	 */
	public int eliminarCalificacionXCumplimiento(Connection con, int codigoCalificacion)
	{
		return SqlBaseCalificacionesXCumpliMetasDao.eliminarCalificacionXCumplimiento (con, codigoCalificacion);
	}
	
	/**
	 * M�todo que guarda o actualiza las calificaciones de cumplimieto por metas
	 * registradas en un tipo de r�gimen y una instituci�n
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
