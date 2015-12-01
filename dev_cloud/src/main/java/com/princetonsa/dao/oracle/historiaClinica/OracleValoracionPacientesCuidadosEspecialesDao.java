package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.historiaClinica.ValoracionPacientesCuidadosEspecialesDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseEventosAdversosDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseValoracionPacientesCuidadosEspecialesDao;
import com.princetonsa.mundo.historiaClinica.ValoracionPacientesCuidadosEspeciales;

/**
 * 
 * @author axioma
 *
 */
public class OracleValoracionPacientesCuidadosEspecialesDao implements ValoracionPacientesCuidadosEspecialesDao {

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