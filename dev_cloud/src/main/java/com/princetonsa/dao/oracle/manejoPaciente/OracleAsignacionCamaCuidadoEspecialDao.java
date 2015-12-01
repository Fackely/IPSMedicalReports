package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.AsignacionCamaCuidadoEspecialAPisoDao;
import com.princetonsa.dao.manejoPaciente.AsignacionCamaCuidadoEspecialDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseAsignacionCamaCuidadoEspecialAPisoDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseAsignacionCamaCuidadoEspecialDao;





/**
 * @author Luis Gabriel Chavez Salazar.
 * lgchavez@princetonsa.com
 */

public class OracleAsignacionCamaCuidadoEspecialDao implements AsignacionCamaCuidadoEspecialDao
{

	public HashMap consultaPacientes(Connection connection, int tipoMonitoreo) {
		
		return SqlBaseAsignacionCamaCuidadoEspecialDao.consultaPacientes(connection, tipoMonitoreo);
	}

	public HashMap consultaDetalle(Connection connection, HashMap criterios) {
		
		return SqlBaseAsignacionCamaCuidadoEspecialDao.consultaDetalle(connection, criterios);
	}

	public int guardarIngresoCuidadosEspeciales(Connection connection, HashMap datos)
	{
		return SqlBaseAsignacionCamaCuidadoEspecialDao.guardarIngresoCuidadosEspeciales(connection, datos);
	}
	
}
	