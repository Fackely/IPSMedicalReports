package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.actionform.capitacion.ConsultaLogSubirPacientesForm;
import com.princetonsa.dto.capitacion.DTOConsultaLogSubirPacientes;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.ILogSubirPacientesDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.ILogSubirPacientesMundo;
import com.servinte.axioma.orm.LogSubirPacientes;

public class LogSubirPacientesMundo implements ILogSubirPacientesMundo{

	ILogSubirPacientesDAO dao;
	
	
	/**
	 * M�todo constructor de la clase
	 * 
	 * @author Camilo G�mez
	 */
	public LogSubirPacientesMundo(){
		dao=CapitacionFabricaDAO.crearLogSubirPacientesDAO();
	}
	
	
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
	public boolean guardarLogSubirPacientes(LogSubirPacientes logSubirPacientes){
		return dao.guardarLogSubirPacientes(logSubirPacientes);
	}
	
	
	
	/**
	 * 
	 * Este M�todo se encarga de consultar en la base de datos
	 * el log de subir pacientes
	 * 
	 * @param  ConsultaLogSubirPacientesForm
	 * @return ArrayList<DTOSubirPacientesInconsistencias>
	 * @author Camilo G�mez
	 *
	 */
	public ArrayList<DTOConsultaLogSubirPacientes> consultarLog(ConsultaLogSubirPacientesForm paramteros){
		return dao.consultarLog(paramteros);
		
	}
	
}
