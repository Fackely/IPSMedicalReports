package com.servinte.axioma.servicio.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.actionform.capitacion.ConsultaLogSubirPacientesForm;
import com.princetonsa.dto.capitacion.DTOConsultaLogSubirPacientes;
import com.servinte.axioma.orm.LogSubirPacientes;

public interface ILogSubirPacientesServicio {

	/**
	 * 
	 * Este M�todo se encarga de guardar en la base de datos
	 * el log de subir pacientes
	 * 
	 * @param  ConsultaLogSubirPacientesForm
	 * @return ArrayList<DTOSubirPacientesInconsistencias>
	 * @author Camilo G�mez
	 *
	 */
	public boolean guardarLogSubirPacientes(LogSubirPacientes logSubirPacientes);  
		
	/**
	 * 
	 * Este M�todo se encarga de consultar en la base de datos
	 * el log de subir pacientes
	 * 
	 * @param  ConsultaLogSubirPacientesForm
	 * @return ArrayList<DTOConsultaLogSubirPacientes>
	 * @author Camilo G�mez
	 *
	 */
	public ArrayList<DTOConsultaLogSubirPacientes> consultarLog(ConsultaLogSubirPacientesForm parametros);
	
}
