package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.EstadisticasIngresosDao;
import com.princetonsa.dao.manejoPaciente.EstadisticasServiciosDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseEstadisticasIngresosDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseEstadisticasServiciosDao;
import com.princetonsa.mundo.manejoPaciente.EstadisticasIngresos;
import com.princetonsa.mundo.manejoPaciente.EstadisticasServicios;

/**
 * 
 * @author axioma
 *
 */
public class PostgresqlEstadisticasIngresosDao implements EstadisticasIngresosDao {

	/**
	 * 
	 */
	public String crearWhereIngresosXConvenio(Connection con, EstadisticasIngresos mundo) {
		return SqlBaseEstadisticasIngresosDao.crearWhereIngresosXConvenio(con, mundo);
	}
	
	/**
	 * 
	 */
	public String crearWhereReingresos(Connection con, EstadisticasIngresos mundo) {
		return SqlBaseEstadisticasIngresosDao.crearWhereReingresos(con, mundo);
	}
	
	/**
	 * 
	 */
	public String crearWhereTotalReingresosXConvenio(Connection con, EstadisticasIngresos mundo) {
		return SqlBaseEstadisticasIngresosDao.crearWhereTotalReingresosXConvenio(con, mundo);
	}
	
	/**
	 * 
	 */
	public String crearConsultaAtencionXRangoEdad(Connection con, EstadisticasIngresos mundo) {
		return SqlBaseEstadisticasIngresosDao.crearConsultaAtencionXRangoEdad(con, mundo);
	}
	
	/**
	 * 
	 */
	public HashMap consultarAtencionXEmpresaYConvenio(Connection con, EstadisticasIngresos mundo) {
		return SqlBaseEstadisticasIngresosDao.consultarAtencionXEmpresaYConvenio(con, mundo);
	}
	
	/**
	 * 
	 */
	public int consultarEgresosMes(Connection con, String anioMes, HashMap filtros) {
		return SqlBaseEstadisticasIngresosDao.consultarEgresosMes(con, anioMes, filtros);
	}
}