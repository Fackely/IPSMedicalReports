package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.List;

import com.servinte.axioma.dao.interfaz.IBaseDAO;
import com.servinte.axioma.orm.HistObserGenerPaciente;
import com.servinte.axioma.orm.Pacientes;

/**
 * 
 * @author axioma
 *
 */
public interface IHistObserGenerPacienteDAO extends IBaseDAO<HistObserGenerPaciente> {
	
	
	/**
	 * Lista Historico Observaciones
	 * @param paciente
	 * @return
	 */
	public List<HistObserGenerPaciente> listaHistoricoObservaciones(Pacientes paciente);
	

}
