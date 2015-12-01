package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.actionform.capitacion.ConsultaLogSubirPacientesForm;
import com.princetonsa.dto.capitacion.DTOConsultaLogSubirPacientes;
import com.servinte.axioma.dao.interfaz.capitacion.ILogSubirPacientesDAO;
import com.servinte.axioma.orm.LogSubirPacientes;
import com.servinte.axioma.orm.delegate.capitacion.LogSubirPacientesDelegate;

public class LogSubirPacientesHibernateDAO implements ILogSubirPacientesDAO {

	LogSubirPacientesDelegate delegate;
	
	
	/**
	 * Método constructor de la clase
	 * 
	 * @author Camilo Gómez
	 */
	public LogSubirPacientesHibernateDAO(){
		delegate = new LogSubirPacientesDelegate();
	}
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de log de subir pacientes
	 * 
	 * @param logSubirPacientes 
	 * @return boolean
	 * @author Camilo Gómez
	 *
	 */
	public boolean guardarLogSubirPacientes(LogSubirPacientes logSubirPacientes){
		return delegate.guardarLogSubirPacientes(logSubirPacientes);
	}
	
	public ArrayList<DTOConsultaLogSubirPacientes> consultarLog(ConsultaLogSubirPacientesForm paramteros)
	{
		return delegate.consultarLog(paramteros);
	}
	
}
