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
	 * M�todo constructor de la clase
	 * 
	 * @author Camilo G�mez
	 */
	public LogSubirPacientesHibernateDAO(){
		delegate = new LogSubirPacientesDelegate();
	}
	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de log de subir pacientes
	 * 
	 * @param logSubirPacientes 
	 * @return boolean
	 * @author Camilo G�mez
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
