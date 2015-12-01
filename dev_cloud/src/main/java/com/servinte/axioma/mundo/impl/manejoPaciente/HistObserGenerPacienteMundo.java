package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.dao.fabrica.administracion.especialidad.AdministracionDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.IHistObserGenerPacienteDAO;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IHistObserGenerPacienteMundo;
import com.servinte.axioma.orm.HistObserGenerPaciente;
import com.servinte.axioma.orm.Pacientes;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

/**
 * 
 * @author axioma
 *
 */
public class HistObserGenerPacienteMundo  implements IHistObserGenerPacienteMundo{

	
	private IHistObserGenerPacienteDAO daoHistor;
	
	/**
	 * CONSTRUTOR
	 */
	public HistObserGenerPacienteMundo(){
		daoHistor=AdministracionDAOFabrica.crearHistoriaGeneraPacienteDAO();
	}
	

	@Override
	public HistObserGenerPaciente buscarxId(Long id) {
		return daoHistor.buscarxId(id);
	}

	@Override
	public void eliminar(HistObserGenerPaciente objeto) {
		daoHistor.eliminar(objeto);
	}

	@Override
	public void insertar(HistObserGenerPaciente objeto) {
		
		try {
		
			UtilidadTransaccion.getTransaccion().begin();
			daoHistor.insertar(objeto);
			UtilidadTransaccion.getTransaccion().commit();
		} 
		catch (Exception e) {
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
		
		
	}

	@Override
	public List<HistObserGenerPaciente> listaHistoricoObservaciones(Pacientes paciente) {
		
		List<HistObserGenerPaciente> lista = new ArrayList<HistObserGenerPaciente>();
		
		try {
			UtilidadTransaccion.getTransaccion().begin();
			lista= daoHistor.listaHistoricoObservaciones(paciente);
				
				for(HistObserGenerPaciente dto:lista )
				{
					dto.getUsuarios().getPersonas();
				}
		
			UtilidadTransaccion.getTransaccion().commit();
		
		} catch (Exception e) {
		
			Log4JManager.info(e);
			Log4JManager.error(e);
		}
		
		return lista;
	}
	

	@Override
	public void modificar(HistObserGenerPaciente objeto) {
		daoHistor.modificar(objeto);
	}

}
