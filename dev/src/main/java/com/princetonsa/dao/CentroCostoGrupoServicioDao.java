/*
 * Creado en May 10, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

public interface CentroCostoGrupoServicioDao
{

	/**
	 * Método que cargar los centros de costo x grupos de servicio parametrizados en la institución 
	 * para el centro de atención seleccionado
	 * @param con
	 * @param centroAtencion
	 * @return HashMap
	 */
	public HashMap consultarCentrosCostoGrupoServicio(Connection con, int centroAtencion);

	/**
	 * Método que inserta el centros de costo x grupo de servicio al centro de atención seleccionado
	 * @param con
	 * @param centroAtencion
	 * @param grupoServicio
	 * @param centroCosto
	 * @return
	 */
	public int insertarCentroCostoGrupoServicio(Connection con, int centroAtencion, int grupoServicio, int centroCosto, int consecutivo);

	
	/**
	 * Método que elimina el registro seleccionado de centros costo x grupo servicio
	 * @param con
	 * @param centroAtencionElim
	 * @param grupoServicioElim
	 * @param centroCostoElim
	 * @return
	 */
	public int eliminarCentroCostoGrupoServicio(Connection con, int centroAtencionElim, int grupoServicioElim, int centroCostoElim);

	
	/**  61826
	 * Método que inserta el centros de costo x grupo de servicio al centro de atención seleccionado
	 * @param con
	 * @param centroAtencion
	 * @param grupoServicio
	 * @param centroCosto
	 * @return
	 */
	public int actualizarCentroCostoGrupoServicio(Connection con, int centroAtencion, int grupoServicio, int centroCosto, int consecutivo);
	
}