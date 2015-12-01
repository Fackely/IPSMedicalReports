package com.princetonsa.dao.postgresql.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.EventosAdversosDao;
import com.princetonsa.dao.historiaClinica.ValoracionPacientesCuidadosEspecialesDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseEventosAdversosDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseValoracionPacientesCuidadosEspecialesDao;
import com.princetonsa.mundo.historiaClinica.ValoracionPacientesCuidadosEspeciales;
import com.princetonsa.mundo.manejoPaciente.EventosAdversos;

/**
 * 
 * @author axioma
 *
 */
public class PostgresqlValoracionPacientesCuidadosEspecialesDao implements ValoracionPacientesCuidadosEspecialesDao {

	/**
	 * 
	 */
	public HashMap consultar(Connection con, ValoracionPacientesCuidadosEspeciales mundo) {
		return SqlBaseValoracionPacientesCuidadosEspecialesDao.consultar(con, mundo);
	}
	/**
	 * 
	 */
	public HashMap consultarTiposMonitoreo(Connection con, ValoracionPacientesCuidadosEspeciales mundo) {
		return SqlBaseValoracionPacientesCuidadosEspecialesDao.consultarTiposMonitoreo(con, mundo);
	}
}