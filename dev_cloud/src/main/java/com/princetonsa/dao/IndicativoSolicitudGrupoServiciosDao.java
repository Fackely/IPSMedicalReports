package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

public interface IndicativoSolicitudGrupoServiciosDao
{

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @param tipo
	 */
	public abstract HashMap consultarGruposServiciosInstitucion(Connection con, String institucion, String tipo);

	/**
	 * 
	 * @param con
	 * @param grupoServicio
	 * @param tipoServicio
	 * @return
	 */
	public abstract HashMap consultarServiciosGrupoServicioTipo(Connection con, String grupoServicio, String tipoServicio);

	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param tomaMuestra
	 * @param respuestaMultiple
	 * @return
	 */
	public abstract boolean actualizarServicioProcedimiento(Connection con, String codigoServicio, String tomaMuestra, String respuestaMultiple);

}
