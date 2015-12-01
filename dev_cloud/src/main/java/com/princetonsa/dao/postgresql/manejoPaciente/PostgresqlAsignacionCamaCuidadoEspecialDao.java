package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;
import com.princetonsa.dao.manejoPaciente.AsignacionCamaCuidadoEspecialDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseAsignacionCamaCuidadoEspecialDao;




/**
 * @author Luis Gabriel Chavez Salazar.
 * lgchavez@pricetonsa.com
 **/

public class PostgresqlAsignacionCamaCuidadoEspecialDao implements AsignacionCamaCuidadoEspecialDao
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
	