package com.servinte.axioma.servicio.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.capitacion.DTOPacienteCapitado;
import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.orm.Pacientes;

/**
 * 
 * @author axioma
 *
 */
public interface IPacienteServicio {
	
	

	/**
	 * Retorna una persona por su numero de identificacion y tipo
	 * @param identificacionBuscar
	 * @param acronimoTipoIdentificacion
	 * @return {@link DtoPersonas}
	 */
	public DtoPersonas obtenerPaciente(String identificacionBuscar, String acronimoTipoIdentificacion);
	
	
	
	/**
	 * Retorna un boolean que indica si el paciente enviado coincide el estado del 
	 * presupuesto odontologico dado
	 *  
	 * @param idPaciente
	 * @param listaEstadosPresupuesto
	 * @return
	 */
	public boolean tienePacientePresupuestoEnEstados(int idPaciente, String[] listaEstadosPresupuesto);
	
	
	/**
	 * Carga la informacion del apciente con su codigo
	 * @param identificacionBuscar
	 * @param acronimoTipoIdentificacion
	 * @return
	 */
	public DtoPersonas obtenerDatosPaciente(int codPaciente);
	
	
	/**
	 * Metodo que cargar el paciente completo
	 * @param identificacionBuscar
	 * @param acronimoTipoIdentificacion
	 * @return
	 */
	public DtoPaciente cargarPacienteCompleto(String identificacionBuscar, String acronimoTipoIdentificacion);
	
	
	
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
	 * Insertar o modificar los datos del paciente
	 * @param pacienteOrm {@link Pacientes}
	 * @param institucion Institución en la cual se ingresa el usuario
	 */
	public void attachDirty(Pacientes pacienteOrm, int institucion);
	
	
	/**
	 * M&eacute;todo encargado de obtener los datos del paciente
	 * asociados a un ingreso estancia
	 * @param codigoIngresoEstancia
	 * @return DTOPacienteCapitado
	 * @author Diana Carolina G
	 */
	public DTOPacienteCapitado buscarPacienteAutorizacionIngresoEstancia(long codigoIngresoEstancia);
	
}
