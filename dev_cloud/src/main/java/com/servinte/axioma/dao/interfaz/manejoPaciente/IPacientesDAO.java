package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.capitacion.DTOPacienteCapitado;
import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.dto.tesoreria.DtoInfoIngresoPacienteControlarAbonoPacientes;
import com.servinte.axioma.orm.Pacientes;

/**
 * Interfaz donde se define el comportamiento del DAO
 * 
 * @author Cristhian Murillo
 *
 */

public interface IPacientesDAO {
	
	/**
	 * Retorna una persona por su numero de identificacion y tipo
	 * @param identificacionBuscar
	 * @param acronimoTipoIdentificacion
	 * @return {@link DtoPersonas}
	 */
	public DtoPersonas obtenerPaciente(String identificacionBuscar, String acronimoTipoIdentificacion);
	
	
	
	/**
	 * Retorna un boolean que indica si el paciente enviado coincide el estado del 
	 * presupuesto odontológico dado
	 *  
	 * @param idPaciente
	 * @param listaEstadosPresupuesto
	 * @return
	 */
	public boolean tienePacientePresupuestoEnEstados(int idPaciente, String[] listaEstadosPresupuesto);
	
	/**
	 * Carga la información del paciente con su código
	 * @param identificacionBuscar
	 * @param acronimoTipoIdentificacion
	 * @return
	 */
	public DtoPersonas obtenerDatosPaciente(int codPaciente);

	/**
	 * Guardar o modificar la información del paciente
	 * @param pacienteOrm {@link Pacientes} Información del paciente
	 * @param institucion Institución del usuario
	 */
	public void attachDirty(Pacientes pacienteOrm, int institucion);
	
	/**
	 * Carga el paciente
	 * @param id
	 * @return
	 */
	public Pacientes findById(int id);
	
	
	
	/**
	 * Busca un paciente por los parametros enviados.
	 * @param parametrosBusqueda
	 * @return ArrayList<DtoUsuariosCapitados>
	 * 
	 * @author Cristhian Murillo
	 */
	public ArrayList<DtoUsuariosCapitados> buscarPacienteConvenio(DtoUsuariosCapitados parametrosBusqueda);
	
	
	/**
	 * M&eacute;todo encargado de obtener los datos del paciente
	 * asociados a un ingreso estancia
	 * @param codigoIngresoEstancia
	 * @return DTOPacienteCapitado
	 * @author Diana Carolina G
	 */
	public DTOPacienteCapitado buscarPacienteAutorizacionIngresoEstancia(long codigoIngresoEstancia);
	
	/**
	 * Retorna el abono disponible del paciente de un paciente.
	 * @param codPaciente
	 * @return
	 * @author diecorqu
	 */
	public ArrayList<DtoInfoIngresoPacienteControlarAbonoPacientes> cargarAbonoDisponiblePorPaciente(int codPaciente);
	
}
