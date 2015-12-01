package com.princetonsa.dao.odontologia;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dto.administracion.DtoPaciente;

public interface IngresoPacienteOdontologiaDao 
{
	/**
	 * metodo que consulta una persona como paciente
	 * @param con
	 * @param parametros
	 * @return DtoPaciente
	 */
	public DtoPaciente consultarPersonaPaciente(Connection con, HashMap parametros);

	public int existenciaPlantillaPaciente(int codigoPaciente,String tipoFuncionalidad);
}
