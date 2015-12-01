package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseAsignacionCamaCuidadoEspecialDao;




/**
 * @author Luis Gabriel Chavez Salazar.
 * lgchavez@princetonsa.com
 */
public interface AsignacionCamaCuidadoEspecialDao
{


	public HashMap consultaPacientes (Connection connection, int tipoMonitoreo);
	public HashMap consultaDetalle (Connection connection, HashMap criterios);
	public int guardarIngresoCuidadosEspeciales(Connection connection, HashMap datos);
	
	
}