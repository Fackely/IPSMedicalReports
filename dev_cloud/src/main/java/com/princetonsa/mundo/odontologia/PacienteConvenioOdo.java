package com.princetonsa.mundo.odontologia;

import java.sql.Connection;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.odontologia.PacientesConvenioOdontologiaDao;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.odontologia.DtoPacienteConvenioOdo;
import com.princetonsa.mundo.PersonaBasica;

public class PacienteConvenioOdo {
	
	private static PacientesConvenioOdontologiaDao dao;

	static 
	{
		if(dao==null)
		{
			dao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPacientesConvenioOdontologiaDao();
		}
	}		
	
	public static DtoPacienteConvenioOdo consultarConvenio(Connection con, DtoPaciente paciente, int codigoConvenio)
	{
		return dao.consultarConvenio(con, paciente,codigoConvenio);
	}

	/**
	 * Valida si el paciente es beneficiario de una tarjeta cliente
	 * @param con
	 * @param paciente
	 * @param convenio 
	 * @return true en caso de tener tarjeta activa
	 */
	public static boolean pacienteTieneTarjetaCliente(Connection con, PersonaBasica paciente, int convenio) {
		return dao.pacienteTieneTarjetaCliente(con, paciente, convenio);
	}
}
