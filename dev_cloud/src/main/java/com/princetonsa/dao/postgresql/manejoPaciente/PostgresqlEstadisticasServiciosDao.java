package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.EventosAdversosDao;
import com.princetonsa.dao.manejoPaciente.EstadisticasServiciosDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseEventosAdversosDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseEstadisticasServiciosDao;
import com.princetonsa.mundo.manejoPaciente.EstadisticasServicios;
import com.princetonsa.mundo.manejoPaciente.EventosAdversos;

/**
 * 
 * @author axioma
 *
 */
public class PostgresqlEstadisticasServiciosDao implements EstadisticasServiciosDao {

	/**
	 * 
	 */
	public String crearWhereServiciosRealizados(Connection con, EstadisticasServicios mundo) {
		return SqlBaseEstadisticasServiciosDao.crearWhereServiciosRealizados(con, mundo);
	}
	
	/**
	 * 
	 */
	public String crearWhereServiciosRealizadosXConvenio(Connection con, EstadisticasServicios mundo) {
		return SqlBaseEstadisticasServiciosDao.crearWhereServiciosRealizadosXConvenio(con, mundo);
	}
	
	/**
	 * 
	 */
	public String crearWhereServiciosRealizadosXEspecialidad(Connection con, EstadisticasServicios mundo) {
		return SqlBaseEstadisticasServiciosDao.crearWhereServiciosRealizadosXEspecialidad(con, mundo);
	}
	
	/**
	 * 
	 */
	public String crearCampoEgresosPorMes(Connection con, EstadisticasServicios mundo) {
		return SqlBaseEstadisticasServiciosDao.crearCampoEgresosPorMes(con, mundo);
	}
	
	/**
	 * 
	 */
	public boolean registrarGeneracionReporte(Connection con, EstadisticasServicios mundo) {
		return SqlBaseEstadisticasServiciosDao.registrarGeneracionReporte(con, mundo);
	}
}