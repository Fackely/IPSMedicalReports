package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.EventosAdversosDao;
import com.princetonsa.dao.manejoPaciente.CalidadAtencionDao;
import com.princetonsa.dao.manejoPaciente.EstadisticasServiciosDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseEventosAdversosDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseCalidadAtencionDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseEstadisticasServiciosDao;
import com.princetonsa.mundo.manejoPaciente.CalidadAtencion;
import com.princetonsa.mundo.manejoPaciente.EstadisticasServicios;
import com.princetonsa.mundo.manejoPaciente.EventosAdversos;

/**
 * 
 * @author axioma
 *
 */
public class PostgresqlCalidadAtencionDao implements CalidadAtencionDao {

	/**
	 * 
	 */
	public String crearWhereSatisfaccionGeneral(Connection con, CalidadAtencion mundo) {
		return SqlBaseCalidadAtencionDao.crearWhereSatisfaccionGeneral(con, mundo);
	}
	
	/**
	 * 
	 */
	public String crearWhereMotCalificacionCalidadAtencion(Connection con, CalidadAtencion mundo) {
		return SqlBaseCalidadAtencionDao.crearWhereMotCalificacionCalidadAtencion(con, mundo);
	}
	
	/**
	 * 
	 */
	public boolean registrarGeneracionReporte(Connection con, CalidadAtencion mundo) {
		return SqlBaseCalidadAtencionDao.registrarGeneracionReporte(con, mundo);
	}
}