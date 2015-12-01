package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.actionform.capitacion.ConsultaLogSubirPacientesForm;
import com.princetonsa.dto.capitacion.DTOConsultaLogSubirPacientes;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.ILogSubirPacientesMundo;
import com.servinte.axioma.orm.LogSubirPacientes;
import com.servinte.axioma.servicio.interfaz.capitacion.ILogSubirPacientesServicio;

public class LogSubirPacientesServicio implements ILogSubirPacientesServicio{
	
	ILogSubirPacientesMundo mundo;
	
	/**
	 * M�todo Constructor de la clase
	 * 
	 * @author Camilo G�mez
	 */
	public LogSubirPacientesServicio (){
		mundo=CapitacionFabricaMundo.crearLogSubirPacientesMundo();
	}

	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de LogSubirPacientes
	 * 
	 * @param LogSubirPacientes 
	 * @return boolean
	 * @author Camilo G�mez
	 *
	 */
	public boolean guardarLogSubirPacientes(LogSubirPacientes logSubirPacientes){
		return mundo.guardarLogSubirPacientes(logSubirPacientes);
	}
	
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
	public ArrayList<DTOConsultaLogSubirPacientes> consultarLog(ConsultaLogSubirPacientesForm parametros)
	{
		return mundo.consultarLog(parametros);
	}
	
}
