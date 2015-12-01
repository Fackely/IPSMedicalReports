package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

public interface IndicativoCargoViaIngresoServicioDao
{

	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 */
	public abstract HashMap consultarGruposServiciosProcedimientosInstitucion(Connection con, String codigoInstitucion);

	/**
	 * 
	 * @param con
	 * @param grupoServicio
	 * @param viaIngreso
	 * @return
	 */
	public abstract HashMap consultarServiciosGrupoServicioViaIngreso(Connection con, String grupoServicio, String viaIngreso, String tipoPaciente);

	/**
	 * 
	 * @param con
	 * @param viaIngreso
	 * @param servicio
	 * @param institucion
	 * @param cargoSolicitud
	 * @param cargoProceso
	 * @return
	 */
	public abstract boolean actualizarServicioProcedimientoViaIngreso(Connection con, String viaIngreso, String servicio, String institucion, String tipoPaciente, String cargoSolicitud, String cargoProceso);

}
