package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.ArrayList;
import java.util.HashMap;

import com.servinte.axioma.dto.tesoreria.DtoConcNotaPacCuentaCont;
import com.servinte.axioma.dto.tesoreria.DtoConceptoNotasPacientes;
import com.servinte.axioma.orm.ConceptoNotaPaciente;
import com.servinte.axioma.orm.CuentasContables;
import com.servinte.axioma.orm.EmpresasInstitucion;

/**
 * Define la logica de negocio relacionada con 
 * Conceptos Notas de Paciente
 * @author diecorqu
 * 
 */
public interface IConceptoNotasPacientesMundo {

	/**
	 * Método encargado de persistir la entidad ConceptoNotaPaciente
	 * @param conceptoNotaPaciente
	 * @return boolean - resultado de la operación
	 */
	public boolean guardarConceptoNotaPaciente(ConceptoNotaPaciente conceptoNotaPaciente);
	
	/**
	 * Método encargado de eliminar un registro de ConceptoNotaPaciente
	 * @param codigo ConceptoNotaPaciente
	 */
	public void eliminarConceptoNotaPaciente(long codigo);
	
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
	 * Método encargado de obtener las empresas-instituciones por institución
	 * que existen en el sistema
	 * @param codigoInsitucion
	 * @return ArrayList<EmpresasInstitucion> Lista con las instituciones
	 */
	public ArrayList<EmpresasInstitucion> obtenerEmpresaInstitucion(int codigoInsitucion);
	
	/**
	 * Método encargado de obtener una cuenta contable por código
	 * @param codigo
	 * @return CuentasContables
	 */
	public CuentasContables obtenerCuentaContable(long codigo);
	
	/**
	 * Método encargado de retornar una lista de DtoConceptosNotasPacientes con los 
	 * Conceptos de Notas de Pacientes existentes en el sistema
	 * @return
	 */
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientes();
	
	/**
	 * Método encargado de guardar una lista de conceptos notas pacientes
	 * @param listaConceptosNotasPacientes
	 * @return boolean resultado de la operación
	 */
	public boolean guardarConceptosNotasPacientes(
			ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientes);
	
	/**
	 * Método encargado de modificar una lista de conceptos notas pacientes
	 * @param listaConceptosNotasPacientes
	 * @return boolean resultado de la operación
	 */
	public boolean modificarConceptosNotasPacientes(
			ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientes);
	
	/**
	 * Método encargado de retornar una lista de DtoConceptosNotasPacientes con los 
	 * Conceptos de Notas de Pacientes existentes en el sistema para la busqueda 
	 * avanzada
	 * @return ArrayList<DtoConceptoNotasPacientes>
	 */
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientesBusquedaAvanzada(
			DtoConceptoNotasPacientes dtoConceptosNotasPacientes);
	
	/**
	 * Método encargado de obtener un mapa con las cuentas contables asociadas a cada 
	 * institucion organizadas por la llave con el id del conceptoNotaPaciente 
	 * @param listaDtoConceptos
	 * @return HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>>
	 */
	public HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>> obtenerMapaNotasPacientesCuentasContables(
			ArrayList<DtoConceptoNotasPacientes> listaDtoConceptos);
	
	/**
	 * Método encargado de retornar una lista de DtoConceptosNotasPacientes con los 
	 * Conceptos de Notas de Pacientes según el estado
	 * @param boolean estado
	 * @return ArrayList<DtoConceptoNotasPacientes>
	 */
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientesEstado(boolean estado);
}
