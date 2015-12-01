/*
 * Creado en Aug 8, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao.pyp;

import java.sql.Connection;
import java.util.HashMap;

public interface CalificacionesXCumpliMetasDao
{
	/**
	 * Método que consulta las calificaciones por cumplimiento de metas ingresadas para el 
	 * régimen seleccionado y la institución actual
	 * @param con
	 * @param tipoRegimen
	 * @param codigoInstitucion
	 */
	public HashMap consultarCalificacionesXRegimen(Connection con, String tipoRegimen, int codigoInstitucion);

	/**
	 *	Método que elimina el registro seleccionado de la tabla
	 * @param con
	 * @param codigoCalificacion
	 * @return
	 */
	public int eliminarCalificacionXCumplimiento(Connection con, int codigoCalificacion);

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
	public int insertarModificarCalificacionsXCumplimiento(Connection con, int codigo, String tipoRegimen, String meta, String rangoInicial, String rangoFinal, int tipoCalificacion, boolean activo, int codigoInstitucion, boolean esInsertar);

}
