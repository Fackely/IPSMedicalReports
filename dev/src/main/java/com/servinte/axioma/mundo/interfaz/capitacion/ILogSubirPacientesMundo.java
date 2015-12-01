package com.servinte.axioma.mundo.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.actionform.capitacion.ConsultaLogSubirPacientesForm;
import com.princetonsa.dto.capitacion.DTOConsultaLogSubirPacientes;
import com.servinte.axioma.orm.LogSubirPacientes;

public interface ILogSubirPacientesMundo {

	
	/**
	 * 
	 * Este Método se encarga de guardar en la base de datos
	 * el log de subir pacientes
	 * 
	 * @param  ConsultaLogSubirPacientesForm
	 * @return ArrayList<DTOSubirPacientesInconsistencias>
	 * @author Camilo Gómez
	 *
	 */
	public boolean guardarLogSubirPacientes(LogSubirPacientes logSubirPacientes);
	
	/**
	 * 
	 * Este Método se encarga de consultar en la base de datos
	 * el log de subir pacientes
	 * 
	 * @param  ConsultaLogSubirPacientesForm
	 * @return ArrayList<DTOConsultaLogSubirPacientes>
	 * @author Camilo Gómez
	 *
	 */
	public ArrayList<DTOConsultaLogSubirPacientes> consultarLog(ConsultaLogSubirPacientesForm paramteros);
	
}
