package com.princetonsa.dao.oracle.odontologia;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.odontologia.IngresoPacienteOdontologiaDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseIngresoPacienteOdontologiaDao;
import com.princetonsa.dto.administracion.DtoPaciente;

public class OracleIngresoPacienteOdontologiaDao implements
		IngresoPacienteOdontologiaDao 
{
	
	/**
	 * metodo que consulta una persona como paciente
	 * @param con
	 * @param parametros
	 * @return DtoPaciente
	 */
	public DtoPaciente consultarPersonaPaciente(Connection con, HashMap parametros)
	{
		return SqlBaseIngresoPacienteOdontologiaDao.consultarPersonaPaciente(con, parametros);
	}

	@Override
	public int existenciaPlantillaPaciente(int codigoPaciente, String tipoFuncionalidad) {
		
		return SqlBaseIngresoPacienteOdontologiaDao.existenciaPlantillaPaciente(codigoPaciente,tipoFuncionalidad);
	}
	
}
