package com.servinte.axioma.servicio.interfaz.manejoPaciente;

import java.util.List;

import com.servinte.axioma.orm.HistObserGenerPaciente;
import com.servinte.axioma.orm.Pacientes;


/**
 * 
 * @author axioma
 *
 */
public interface IHistObserGenerPacienteServicio {
	
	/**
	 * METODO QUE RECIBE UN OBJETO  Y LO INSERTA EN LA BASE DE DATOS
	 * @author 
	 * @param objecto
	 */
	public void insertar(HistObserGenerPaciente objeto);

	
	/**
	 * METODO QUE RECIBE UN OBJETO Y LOS MODIFICA EN LA BASE DE DATOS 
	 * @author 
	 * @param objecto
	 */
	public void modificar(HistObserGenerPaciente objeto);
	
	
	/**
	 * METODO QUE RECIBE UN OBJETO Y LOS ELIMINA DE LA BASE DE DATOS
	 * @author 
	 * @param objecto
	 */
	public void eliminar( HistObserGenerPaciente objeto);

	
	/**
	 * METOD QUE RECIBE UN ID Y RETORNA UN TIPO DE OBJETO DE LA BASE DE DATOS
	 * @author 
	 * @param objeto
	 * @param id
	 * @return
	 */
	public HistObserGenerPaciente buscarxId(Long id);
	
	

	/**
	 * Lista Historico Observaciones
	 * @param paciente
	 * @return
	 */
	public List<HistObserGenerPaciente> listaHistoricoObservaciones(Pacientes paciente);
	
	
	/**
	 * Lista Historico Observaciones
	 * @param paciente
	 * @return
	 */
	public List<HistObserGenerPaciente> listaHistoricoObservaciones(int codigoPaciente);
	
	
	
	

}
