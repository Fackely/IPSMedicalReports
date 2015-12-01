package com.servinte.axioma.mundo.interfaz.odontologia.agendaOdontologica;

import java.util.List;

import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de Agenda Odontológica
 * 
 * @author Cristhian Murillo
 *
 */
public interface IAgendaOdontologicaMundo 
{

	/**
	 * Retorna los ingresos de un paciente en el estado indicado
	 * 
	 * @param codPaciente
	 * @param listaEstadosIngreso
	 * @return  List<ConveniosIngresoPaciente>
	 */
	public List<ConveniosIngresoPaciente> obtenerConveniosIngresoPacientePorEstado (int codPaciente, char acronimoEstadoActivo);
	
	
	/**
	 * Retorna los ingresos de un paciente en el estado indicado
	 * 
	 * @param codPaciente
	 * @param listaEstadosIngreso
	 * @return  List<DtoSeccionConvenioPaciente>
	 */
	public List<DtoSeccionConvenioPaciente> obtenerDtoConveniosIngresoPacientePorEstado (int codPaciente, char acronimoEstadoActivo);
	

	
	/**
	 * Retorna los convenios asociados al ingreso de un paciente que cumplan con las validaciones de asignación de cita:
	 *  -Tipo tarjeta cliente = NO
	 *  -Tipo tarjeta cliente = SI los cuales:
	 *  	-Tengan la tarjeta en estado Activa
	 * 
	 * @param codPaciente
	 * @return  List<DtoSeccionConvenioPaciente>
	 */
	public List<DtoSeccionConvenioPaciente> obtenerDtoConveniosIngresoPacienteValidacionesAsignacioncita (int codPaciente);
	
	
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.orm.ConveniosHome#findById(int)
	 */
	public Convenios findByIdConvenio(int id);
}
