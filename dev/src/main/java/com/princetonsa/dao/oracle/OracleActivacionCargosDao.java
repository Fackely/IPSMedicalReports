/*
 * Creado en 2/08/2004
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;

import util.ResultadoBoolean;
import com.princetonsa.dao.ActivacionCargosDao;
import com.princetonsa.dao.sqlbase.SqlBaseActivacionCargosDao;

/**
 * @author Juan David Ramírez López
 *
 * Princeton S.A.
 */
public class OracleActivacionCargosDao implements ActivacionCargosDao
{
  

	/**
	 * Listar las solicitudes cargadas o inactivas
	 * @return Collection con las solicitudes en estado cargado o pendiente
	 */
	public HashMap listar(Connection con, String idSubCuenta)
	{
		return SqlBaseActivacionCargosDao.listar(con, idSubCuenta);
	}

	/**
	 * Cargar el detalle de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 */
	public HashMap<String, Object> detallarSolicitud(Connection con, HashMap campos)
	{
		return SqlBaseActivacionCargosDao.detallarSolicitud(con, campos);
	}
	
	/**
	 * Método implementado para realizar la acitvacion/inactivacion del cargo
	 * @param con
	 * @param campos
	 * @return
	 */
	public ResultadoBoolean activarInactivarCargo(Connection con,HashMap campos)
	{
		campos.put("secuenciaSolicitudesSubCuenta","seq_sol_sub_cuentas.nextval");
		campos.put("secuenciaDetCargos","seq_det_cargos.nextval");
		campos.put("secuenciaInactivacionesCargos","seq_inactivaciones_cargos.nextval");
		
		return SqlBaseActivacionCargosDao.activarInactivarCargo(con, campos);
	}
	
	
	/**
	 * Método que realiza la consulta de los cargos de una solicitud cirugia para una subcuenta específica
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String,Object> consultarDetalleCargosCirugia(Connection con,HashMap campos)
	{
		return SqlBaseActivacionCargosDao.consultarDetalleCargosCirugia(con, campos);
	}
	
	/**
	 * Método que realiza la consulta de las inactivaciones de cargos
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> consultarInactivacionesCargos(Connection con,HashMap campos)
	{
		return SqlBaseActivacionCargosDao.consultarInactivacionesCargos(con, campos);
	}
	
	
	

}
