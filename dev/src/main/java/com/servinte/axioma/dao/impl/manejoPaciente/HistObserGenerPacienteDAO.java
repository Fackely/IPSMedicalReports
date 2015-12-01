package com.servinte.axioma.dao.impl.manejoPaciente;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.dao.interfaz.manejoPaciente.IHistObserGenerPacienteDAO;
import com.servinte.axioma.orm.HistObserGenerPaciente;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.orm.delegate.manejoPaciente.HistObserGenerPacienteDelegate;


/**
 * 
 * @author axioma
 *
 */
public class HistObserGenerPacienteDAO  implements IHistObserGenerPacienteDAO{

	
	private HistObserGenerPacienteDelegate delegate;
	
	/**
	 * Construtor 
	 */
	public HistObserGenerPacienteDAO(){
		delegate= new HistObserGenerPacienteDelegate();
	}

	
	
	@Override
	public List<HistObserGenerPaciente> listaHistoricoObservaciones(
			Pacientes paciente) {
		
		List<HistObserGenerPaciente> listTmp= new ArrayList<HistObserGenerPaciente>(); 
		try {
			listTmp=delegate.listaHistoricoObservaciones(paciente);
		} catch (Exception e) {
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
		return listTmp;
	}

	@Override
	public HistObserGenerPaciente buscarxId(Number id) {
		
		return delegate.findById(id.longValue());
	}

	@Override
	public void eliminar(HistObserGenerPaciente objeto) {
		delegate.delete(objeto);
		
	}

	@Override
	public void insertar(HistObserGenerPaciente objeto) {
		delegate.persist(objeto);
		
	}

	
	@Override
	public void modificar(HistObserGenerPaciente objeto) {
		delegate.attachDirty(objeto);
	}

}
