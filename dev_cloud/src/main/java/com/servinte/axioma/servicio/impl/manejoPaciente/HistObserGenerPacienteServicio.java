package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.List;

import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IHistObserGenerPacienteMundo;
import com.servinte.axioma.orm.HistObserGenerPaciente;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IHistObserGenerPacienteServicio;


/**
 * 
 * @author axioma
 *
 */
public class HistObserGenerPacienteServicio  implements IHistObserGenerPacienteServicio {
	
	
	private IHistObserGenerPacienteMundo historiMundo;
	
	
	/**
	 * CONSTRUTOR 
	 */
	public HistObserGenerPacienteServicio(){
		historiMundo= AdministracionFabricaMundo.crearHistObservacionGenerPacieneMundo();
	}
	
	
	@Override
	public HistObserGenerPaciente buscarxId(Long id) {
		
		return historiMundo.buscarxId(id);
	}

	@Override
	public void eliminar(HistObserGenerPaciente objeto) {
		historiMundo.eliminar(objeto);
		
	}

	@Override
	public void insertar(HistObserGenerPaciente objeto) {
		historiMundo.insertar(objeto);
	}

	@Override
	public List<HistObserGenerPaciente> listaHistoricoObservaciones(
			Pacientes paciente) {
		return historiMundo.listaHistoricoObservaciones(paciente);
	}

	
	@Override
	public void modificar(HistObserGenerPaciente objeto) {
		historiMundo.modificar(objeto);
	}


	@Override
	public List<HistObserGenerPaciente> listaHistoricoObservaciones(int codigoPaciente) {
		
		Pacientes paciente = new Pacientes();
		paciente.setCodigoPaciente(codigoPaciente);
		return historiMundo.listaHistoricoObservaciones(paciente);
	}
	
	
	

}
