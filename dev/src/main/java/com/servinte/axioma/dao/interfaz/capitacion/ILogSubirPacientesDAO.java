package com.servinte.axioma.dao.interfaz.capitacion;

import java.util.ArrayList;

import com.princetonsa.actionform.capitacion.ConsultaLogSubirPacientesForm;
import com.princetonsa.dto.capitacion.DTOConsultaLogSubirPacientes;
import com.servinte.axioma.orm.LogSubirPacientes;

public interface ILogSubirPacientesDAO {

	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de log de subir pacientes Masivo
	 * 
	 * @param logSubirPacientes 
	 * @return boolean
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
	 * @author Ricardo Ruiz
	 *
	 */
	public ArrayList<DTOConsultaLogSubirPacientes> consultarLog(ConsultaLogSubirPacientesForm paramteros);
}
