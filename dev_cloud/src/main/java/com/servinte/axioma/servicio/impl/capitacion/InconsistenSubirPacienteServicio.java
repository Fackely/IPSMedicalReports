package com.servinte.axioma.servicio.impl.capitacion;

import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.IInconsistenSubirPacienteMundo;
import com.servinte.axioma.orm.InconsistenSubirPaciente;
import com.servinte.axioma.servicio.interfaz.capitacion.IInconsistenSubirPacienteServicio;

public class InconsistenSubirPacienteServicio implements IInconsistenSubirPacienteServicio{

	IInconsistenSubirPacienteMundo mundo;
	
	/**
	 * Metodo constructor de la clase
	 * @author Camilo G�mez
	 */
	public InconsistenSubirPacienteServicio(){
		mundo = CapitacionFabricaMundo.crearInconsistenSubirPacienteMundo();
	}
	

	
	/**
	 * 
	 * Este M�todo se encarga de insertar en la base de datos
	 * un registro de InconsistenSubirPaciente
	 * 
	 * @param InconsistenSubirPaciente 
	 * @return boolean
	 * @author Camilo G�mez
	 *
	 */
	public boolean guardarInconsistenciaSubirPaciente(InconsistenSubirPaciente inconsistenSubirPaciente){
		return mundo.guardarInconsistenciaSubirPaciente(inconsistenSubirPaciente);
	}
}
