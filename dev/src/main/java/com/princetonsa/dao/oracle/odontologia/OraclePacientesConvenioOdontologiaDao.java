package com.princetonsa.dao.oracle.odontologia;

import java.sql.Connection;

import com.princetonsa.dao.odontologia.PacientesConvenioOdontologiaDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBasePacientesConvenioOdontologiaDao;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.odontologia.DtoPacienteConvenioOdo;
import com.princetonsa.mundo.PersonaBasica;

public class OraclePacientesConvenioOdontologiaDao implements PacientesConvenioOdontologiaDao{

	@Override
	public DtoPacienteConvenioOdo consultarConvenio(Connection con, DtoPaciente paciente,int codigoConvenio) {
		return SqlBasePacientesConvenioOdontologiaDao.consultarConvenio(con, paciente, codigoConvenio);
	}

	@Override
	public boolean pacienteTieneTarjetaCliente(Connection con, PersonaBasica paciente, int convenio) {
		return SqlBasePacientesConvenioOdontologiaDao.pacienteTieneTarjetaCliente(con, paciente, convenio);
	}

}
