/*
 * Creado en May 10, 2006
 * Andr�s Mauricio Ruiz V�lez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.CentroCostoGrupoServicioDao;
import com.princetonsa.dao.sqlbase.SqlBaseCentroCostoGrupoServicioDao;

public class PostgresqlCentroCostoGrupoServicioDao implements CentroCostoGrupoServicioDao
{
	/**
	 * M�todo que cargar los centros de costo x grupos de servicio parametrizados en la instituci�n 
	 * para el centro de atenci�n seleccionado
	 * @param con
	 * @param centroAtencion
	 * @return HashMap
	 */
	public HashMap consultarCentrosCostoGrupoServicio(Connection con, int centroAtencion)
	{
		return SqlBaseCentroCostoGrupoServicioDao.consultarCentrosCostoGrupoServicio (con, centroAtencion);
	}
	
	/**
	 * M�todo que inserta el centros de costo x grupo de servicio al centro de atenci�n seleccionado
	 * @param con
	 * @param centroAtencion
	 * @param grupoServicio
	 * @param centroCosto
	 * @return
	 */
	public int insertarCentroCostoGrupoServicio(Connection con, int centroAtencion, int grupoServicio, int centroCosto, int consecutivo)
	{
		return SqlBaseCentroCostoGrupoServicioDao.insertarCentroCostoGrupoServicio (con, centroAtencion, grupoServicio, centroCosto, consecutivo);
	}
	
	/**
	 * M�todo que elimina el registro seleccionado de centros costo x grupo servicio
	 * @param con
	 * @param centroAtencionElim
	 * @param grupoServicioElim
	 * @param centroCostoElim
	 * @return
	 */
	public int eliminarCentroCostoGrupoServicio(Connection con, int centroAtencionElim, int grupoServicioElim, int centroCostoElim)
	{
		return SqlBaseCentroCostoGrupoServicioDao.eliminarCentroCostoGrupoServicio (con, centroAtencionElim, grupoServicioElim, centroCostoElim);
	}


	/** 61826
	 * M�todo que actualiza el consecutivo del centros de costo x grupo de servicio al centro de atenci�n seleccionado
	 * @param con
	 * @param centroAtencion
	 * @param grupoServicio
	 * @param centroCosto
	 * @param consecutivo
	 * @return
	 */
	public int actualizarCentroCostoGrupoServicio(Connection con, int centroAtencion, int grupoServicio, int centroCosto, int consecutivo)
	{
		return SqlBaseCentroCostoGrupoServicioDao.actualizarCentroCostoGrupoServicio (con, centroAtencion, grupoServicio, centroCosto, consecutivo);
	}
	
	

}