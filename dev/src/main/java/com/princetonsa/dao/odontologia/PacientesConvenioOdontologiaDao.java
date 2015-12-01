package com.princetonsa.dao.odontologia;

import java.sql.Connection;

import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.odontologia.DtoPacienteConvenioOdo;
import com.princetonsa.mundo.PersonaBasica;


public interface PacientesConvenioOdontologiaDao {
	
	/**
	 * 

	 * @param con
	 * @param paciente
	 * @param codigoConvenio
	 * @return
	 */
	public abstract DtoPacienteConvenioOdo consultarConvenio(Connection con, DtoPaciente paciente, int codigoConvenio);
	/**
	 * 
	 * 
	 * @param con
	 * @param paciente
	 * @param convenio
	 * @return
	 */
	public abstract boolean pacienteTieneTarjetaCliente(Connection con, PersonaBasica paciente, int convenio);
}
