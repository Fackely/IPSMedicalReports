/*
 * Creado en May 31, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.UnidadMedidaDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseUnidadMedidaDao;

public class OracleUnidadMedidaDao implements UnidadMedidaDao
{
	/**
	 * Método que cargar las unidades de medida parametrizadas a la institución
	 * @param con
	 * @return HashMap
	 */
	public HashMap consultarUnidadesMedidaInstitucion(Connection con)
	{
		return SqlBaseUnidadMedidaDao.consultarUnidadesMedidaInstitucion (con);
	}
	
	/**
	 * Método que guarda las nuevas unidades de medida o modifica
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
	public int insertarModificarUnidadMedida(Connection con, String acronimo, String unidad, boolean unidosis, boolean activo, String acronimoAnt, boolean esInsertar)
	{
		return SqlBaseUnidadMedidaDao.insertarModificarUnidadMedida (con, acronimo, unidad, unidosis, activo, acronimoAnt, esInsertar);
	}
	
	/**
	 * Método que elimina el registro seleccionado de la unidad de medida
	 * @param con
	 * @param acronimo
	 * @return
	 */
	public int eliminarUnidadMedida(Connection con, String acronimo)
	{
		return SqlBaseUnidadMedidaDao.eliminarUnidadMedida (con, acronimo);
	}
}
