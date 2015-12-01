package com.princetonsa.dao.postgresql.facturacion;


import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.CentrosCostoEntidadesSubcontratadasDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseCentrosCostoEntidadesSubcontratadasDao;

/**
 * 
 * @author Angela María Angel amangel@axioma-md.com
 *
 */

public class PostgresqlCentrosCostoEntidadesSubcontratadasDao implements CentrosCostoEntidadesSubcontratadasDao
{
	/**
	 * Metodo para consultar los centro de consto por entidad subcontratada
	 * @return
	 */
	public HashMap consultarCentrosCostoEntiSub(int centroAtencion){
		return SqlBaseCentrosCostoEntidadesSubcontratadasDao.consultarCentrosCostoEntiSub(centroAtencion);
	}

	/**
	 * Metodo para insertar un nuevo registro de centro de costo por entidada subcontratada
	 * @param criterios
	 * @return
	 */
	public int insertarNuevoRegistro(HashMap criterios){
		return SqlBaseCentrosCostoEntidadesSubcontratadasDao.insertarNuevoRegistro(criterios);
	}
	
	/**
	 * Metodo para actualizar un registro de centro costo por entidad subcontratada
	 * @param criterios
	 * @return
	 */
	public boolean actualizarCentroCostoEntiSub(HashMap criterios){
		return SqlBaseCentrosCostoEntidadesSubcontratadasDao.actualizarCentroCostoEntiSub(criterios);
	}
	
	/**
	 * Metodo que elimina un registro de centro de costo por entidad subcontratada
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarCentroCostoEntiSub(int consecutivo)
	{
		return SqlBaseCentrosCostoEntidadesSubcontratadasDao.eliminarCentroCostoEntiSub(consecutivo);
	}
	
	/**
	 * Metodo que guarda un log tipo BD para modificaciones
	 * @param criterios
	 * @return
	 */
	public boolean guardarLogCentrosCostoEntiSub (HashMap criterios){
		return SqlBaseCentrosCostoEntidadesSubcontratadasDao.guardarLogCentrosCostoEntiSub(criterios);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar las prioridades
	 * de las entidades subcontradas
	 * 
	 * @param int centroAtencion
	 * @return HashMap
	 * @author, Angela Maria Aguirre
	 *
	 */
	public HashMap obtenerPrioridadCentrosCostoEntiSub(int centroAtencion){
		return SqlBaseCentrosCostoEntidadesSubcontratadasDao.obtenerPrioridadCentrosCostoEntiSub(centroAtencion);
	}

	@Override
	public HashMap consultarEntidadesSubSinInterna(Connection con, int codigoEntidadSubInterna) {
		return SqlBaseCentrosCostoEntidadesSubcontratadasDao.consultarEntidadesSubSinInterna(con, codigoEntidadSubInterna);
	}
}