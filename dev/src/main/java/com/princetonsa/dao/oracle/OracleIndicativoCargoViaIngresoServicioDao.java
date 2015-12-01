package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.IndicativoCargoViaIngresoServicioDao;
import com.princetonsa.dao.sqlbase.SqlBaseIndicativoCargoViaIngresoServicioDao;

public class OracleIndicativoCargoViaIngresoServicioDao implements
		IndicativoCargoViaIngresoServicioDao
{

	/**
	 * 
	 */
	public HashMap consultarGruposServiciosProcedimientosInstitucion(Connection con, String codigoInstitucion)
	{
		return SqlBaseIndicativoCargoViaIngresoServicioDao.consultarGruposServiciosProcedimientosInstitucion(con,codigoInstitucion);
	}

	/**
	 * 
	 */
	public HashMap consultarServiciosGrupoServicioViaIngreso(Connection con, String grupoServicio, String viaIngreso, String tipoPaciente)
	{
		return SqlBaseIndicativoCargoViaIngresoServicioDao.consultarServiciosGrupoServicioViaIngreso(con,grupoServicio,viaIngreso, tipoPaciente);
	}
	

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
	public boolean actualizarServicioProcedimientoViaIngreso(Connection con, String viaIngreso, String servicio, String institucion, String tipoPaciente, String cargoSolicitud, String cargoProceso)
	{
		String cadena="INSERT INTO cargo_via_ingreso_servicio (codigo,via_ingreso,servicio,institucion,cargo_solicitud,cargo_proceso, tipo_paciente) values(seq_carg_via_ing_serv.nextval,?,?,?,?,?,?)";
		return SqlBaseIndicativoCargoViaIngresoServicioDao.actualizarServicioProcedimientoViaIngreso(con,viaIngreso,servicio,institucion,tipoPaciente, cargoSolicitud,cargoProceso,cadena);
	}

}
