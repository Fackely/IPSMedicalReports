/*
 * Creado en May 31, 2006
 * Andr?s Mauricio Ruiz V?lez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

public interface UnidadMedidaDao
{
	/**
	 * M?todo que cargar las unidades de medida parametrizadas a la instituci?n
	 * @param con
	 * @return HashMap
	 */
	public HashMap consultarUnidadesMedidaInstitucion(Connection con);

	/**
	 * M?todo que guarda las nuevas unidades de medida o modifica
	 * las unidades de medida ya ingresadas 
	 * @param con
	 * @param acronimo
	 * @param unidad
	 * @param unidosis
	 * @param activo
	 * @param acronimoAnt
	 * @param esInsertar
	 * @return
	 */
	public int insertarModificarUnidadMedida(Connection con, String acronimo, String unidad, boolean unidosis, boolean activo, String acronimoAnt, boolean esInsertar);

	/**
	 * M?todo que elimina el registro seleccionado de la unidad de medida
	 * @param con
	 * @param acronimo
	 * @return
	 */
	public int eliminarUnidadMedida(Connection con, String acronimo);

}
