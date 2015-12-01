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
	 * Método encargado de persistir la entidad ConceptoNotaPaciente
	 * @param conceptoNotaPaciente
	 * @return boolean - resultado de la operación
	 */
	public boolean guardarConceptoNotaPaciente(ConceptoNotaPaciente conceptoNotaPaciente);
	
	/**
	 * Método encargado de eliminar un registro de ConceptoNotaPaciente
	 * @param conceptoNotaPaciente
	 */
	public void eliminarConceptoNotaPaciente(ConceptoNotaPaciente conceptoNotaPaciente);
	
	/**
	 * Método encargado de modificar la entidad ConceptoNotaPaciente
	 * @param conceptoNotaPaciente
	 * @return ConceptoNotaPaciente
	 */
	public ConceptoNotaPaciente modificarConceptoNotaPaciente(
			ConceptoNotaPaciente conceptoNotaPaciente);
	
	
	/**
	 * Método encargado de buscar un ConceptoNotaPaciente por código
	 * @param codigo
	 * @return ConceptoNotaPaciente
	 */
	public ConceptoNotaPaciente findById(long codigo);
	
	/**
	 * Método encargado de retornar una lista de DtoConceptosNotasPacientes con los 
	 * Conceptos de Notas de Pacientes existentes en el sistema
	 * @return ArrayList<DtoConceptoNotasPacientes>
	 */
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientes();
	
	/**
	 * Método encargado de retornar una lista de DtoConceptosNotasPacientes con los 
	 * Conceptos de Notas de Pacientes existentes en el sistema para la busqueda 
	 * avanzada
	 * @return ArrayList<DtoConceptoNotasPacientes>
	 */
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientesBusquedaAvanzada(
			DtoConceptoNotasPacientes dtoConceptosNotasPacientes);
	
	/**
	 * Método encargado de retornar una lista de DtoConceptosNotasPacientes con los 
	 * Conceptos de Notas de Pacientes según el estado
	 * @param boolean estado
	 * @return ArrayList<DtoConceptoNotasPacientes>
	 */
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientesEstado(boolean estado);
}
