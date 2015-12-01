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
	 * Método constructor de la clase
	 * 
	 * @author Camilo Gómez
	 */
	public LogSubirPacientesMundo(){
		dao=CapitacionFabricaDAO.crearLogSubirPacientesDAO();
	}
	
	
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
	public boolean guardarLogSubirPacientes(LogSubirPacientes logSubirPacientes){
		return dao.guardarLogSubirPacientes(logSubirPacientes);
	}
	
	
	
	/**
	 * 
	 * Este Método se encarga de consultar en la base de datos
	 * el log de subir pacientes
	 * 
	 * @param  ConsultaLogSubirPacientesForm
	 * @return ArrayList<DTOSubirPacientesInconsistencias>
	 * @author Camilo Gómez
	 *
	 */
	public ArrayList<DTOConsultaLogSubirPacientes> consultarLog(ConsultaLogSubirPacientesForm paramteros){
		return dao.consultarLog(paramteros);
		
	}
	
}
