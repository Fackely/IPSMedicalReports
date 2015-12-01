/*
 * Creado en Aug 8, 2006
 * Andr�s Mauricio Ruiz V�lez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao.pyp;

import java.sql.Connection;
import java.util.HashMap;

public interface CalificacionesXCumpliMetasDao
{
	/**
	 * M�todo que consulta las calificaciones por cumplimiento de metas ingresadas para el 
	 * r�gimen seleccionado y la instituci�n actual
	 * @param con
	 * @param tipoRegimen
	 * @param codigoInstitucion
	 */
	public HashMap consultarCalificacionesXRegimen(Connection con, String tipoRegimen, int codigoInstitucion);

	/**
	 *	M�todo que elimina el registro seleccionado de la tabla
	 * @param con
	 * @param codigoCalificacion
	 * @return
	 */
	public int eliminarCalificacionXCumplimiento(Connection con, int codigoCalificacion);

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
	public int insertarModificarCalificacionsXCumplimiento(Connection con, int codigo, String tipoRegimen, String meta, String rangoInicial, String rangoFinal, int tipoCalificacion, boolean activo, int codigoInstitucion, boolean esInsertar);

}
