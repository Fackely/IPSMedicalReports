package com.servinte.axioma.dao.interfaz.tesoreria;

import java.util.ArrayList;

import com.servinte.axioma.dto.tesoreria.DtoConceptoNotasPacientes;
import com.servinte.axioma.orm.ConceptoNotaPaciente;

/**
 * Define la logica de negocio relacionada con 
 * Conceptos Notas de Paciente
 * @author diecorqu
 * 
 */
public interface IConceptoNotasPacientesDAO {

	/**
	 * M�todo encargado de persistir la entidad ConceptoNotaPaciente
	 * @param conceptoNotaPaciente
	 * @return boolean - resultado de la operaci�n
	 */
	public boolean guardarConceptoNotaPaciente(ConceptoNotaPaciente conceptoNotaPaciente);
	
	/**
	 * M�todo encargado de eliminar un registro de ConceptoNotaPaciente
	 * @param conceptoNotaPaciente
	 */
	public void eliminarConceptoNotaPaciente(ConceptoNotaPaciente conceptoNotaPaciente);
	
	/**
	 * M�todo encargado de modificar la entidad ConceptoNotaPaciente
	 * @param conceptoNotaPaciente
	 * @return ConceptoNotaPaciente
	 */
	public ConceptoNotaPaciente modificarConceptoNotaPaciente(
			ConceptoNotaPaciente conceptoNotaPaciente);
	
	
	/**
	 * M�todo encargado de buscar un ConceptoNotaPaciente por c�digo
	 * @param codigo
	 * @return ConceptoNotaPaciente
	 */
	public ConceptoNotaPaciente findById(long codigo);
	
	/**
	 * M�todo encargado de retornar una lista de DtoConceptosNotasPacientes con los 
	 * Conceptos de Notas de Pacientes existentes en el sistema
	 * @return ArrayList<DtoConceptoNotasPacientes>
	 */
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientes();
	
	/**
	 * M�todo encargado de retornar una lista de DtoConceptosNotasPacientes con los 
	 * Conceptos de Notas de Pacientes existentes en el sistema para la busqueda 
	 * avanzada
	 * @return ArrayList<DtoConceptoNotasPacientes>
	 */
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientesBusquedaAvanzada(
			DtoConceptoNotasPacientes dtoConceptosNotasPacientes);
	
	/**
	 * M�todo encargado de retornar una lista de DtoConceptosNotasPacientes con los 
	 * Conceptos de Notas de Pacientes seg�n el estado
	 * @param boolean estado
	 * @return ArrayList<DtoConceptoNotasPacientes>
	 */
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientesEstado(boolean estado);
}
