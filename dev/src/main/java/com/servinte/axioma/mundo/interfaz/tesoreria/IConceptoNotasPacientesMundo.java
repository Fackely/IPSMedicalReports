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
	 * M�todo encargado de persistir la entidad ConceptoNotaPaciente
	 * @param conceptoNotaPaciente
	 * @return boolean - resultado de la operaci�n
	 */
	public boolean guardarConceptoNotaPaciente(ConceptoNotaPaciente conceptoNotaPaciente);
	
	/**
	 * M�todo encargado de eliminar un registro de ConceptoNotaPaciente
	 * @param codigo ConceptoNotaPaciente
	 */
	public void eliminarConceptoNotaPaciente(long codigo);
	
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
	 * M�todo encargado de obtener las empresas-instituciones por instituci�n
	 * que existen en el sistema
	 * @param codigoInsitucion
	 * @return ArrayList<EmpresasInstitucion> Lista con las instituciones
	 */
	public ArrayList<EmpresasInstitucion> obtenerEmpresaInstitucion(int codigoInsitucion);
	
	/**
	 * M�todo encargado de obtener una cuenta contable por c�digo
	 * @param codigo
	 * @return CuentasContables
	 */
	public CuentasContables obtenerCuentaContable(long codigo);
	
	/**
	 * M�todo encargado de retornar una lista de DtoConceptosNotasPacientes con los 
	 * Conceptos de Notas de Pacientes existentes en el sistema
	 * @return
	 */
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientes();
	
	/**
	 * M�todo encargado de guardar una lista de conceptos notas pacientes
	 * @param listaConceptosNotasPacientes
	 * @return boolean resultado de la operaci�n
	 */
	public boolean guardarConceptosNotasPacientes(
			ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientes);
	
	/**
	 * M�todo encargado de modificar una lista de conceptos notas pacientes
	 * @param listaConceptosNotasPacientes
	 * @return boolean resultado de la operaci�n
	 */
	public boolean modificarConceptosNotasPacientes(
			ArrayList<DtoConceptoNotasPacientes> listaConceptosNotasPacientes);
	
	/**
	 * M�todo encargado de retornar una lista de DtoConceptosNotasPacientes con los 
	 * Conceptos de Notas de Pacientes existentes en el sistema para la busqueda 
	 * avanzada
	 * @return ArrayList<DtoConceptoNotasPacientes>
	 */
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientesBusquedaAvanzada(
			DtoConceptoNotasPacientes dtoConceptosNotasPacientes);
	
	/**
	 * M�todo encargado de obtener un mapa con las cuentas contables asociadas a cada 
	 * institucion organizadas por la llave con el id del conceptoNotaPaciente 
	 * @param listaDtoConceptos
	 * @return HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>>
	 */
	public HashMap<String, ArrayList<DtoConcNotaPacCuentaCont>> obtenerMapaNotasPacientesCuentasContables(
			ArrayList<DtoConceptoNotasPacientes> listaDtoConceptos);
	
	/**
	 * M�todo encargado de retornar una lista de DtoConceptosNotasPacientes con los 
	 * Conceptos de Notas de Pacientes seg�n el estado
	 * @param boolean estado
	 * @return ArrayList<DtoConceptoNotasPacientes>
	 */
	public ArrayList<DtoConceptoNotasPacientes> listarConceptoNotasPacientesEstado(boolean estado);
}
