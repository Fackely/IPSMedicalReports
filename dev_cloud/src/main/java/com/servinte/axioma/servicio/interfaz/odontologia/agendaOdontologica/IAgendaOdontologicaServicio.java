package com.servinte.axioma.servicio.interfaz.odontologia.agendaOdontologica;

import java.util.List;

import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.princetonsa.dto.manejoPaciente.DtoCentroCostosVista;
import com.servinte.axioma.orm.CitasAsociadasAProgramada;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;


/**
 * Servicio que le delega al negocio las operaciones de turno de caja
 * 
 * @author Cristhian Murillo
 * @see com.servinte.axioma.servicio.impl.tesoreria.MovimientosCajaServicio
 */
public interface IAgendaOdontologicaServicio {
	

	/**
	 * Retorna los ingresos de un paciente en el estado indicado
	 * 
	 * @param codPaciente
	 * @param listaEstadosIngreso
	 * @return  List<ConveniosIngresoPaciente>
	 */
	public List<ConveniosIngresoPaciente> obtenerConveniosIngresoPacientePorEstado (int codPaciente, char acronimoEstadoActivo);
	
	
	/**
	 * Retorna los convenios del ignreso de un paciente en el estado indicado
	 * 
	 * @param codPaciente
	 * @param listaEstadosIngreso
	 * @return  List<DtoSeccionConvenioPaciente>
	 */
	public List<DtoSeccionConvenioPaciente> obtenerDtoConveniosIngresoPacientePorEstado (int codPaciente, char acronimoEstadoActivo);
	
	
	
	
	/**
	 * Lista los centros de costo por Centro de Atención
	 * 
	 * @author Cristhian Murillo
	 *  
	 * @param consecutivoCentroAttencion
	 * @param tipoArea
	 * 
	 * @return  List<DtoCentroCostosVista>
	 */
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorCentroAtencion(int consecutivoCentroAttencion, int tipoArea);
	
	
	
	
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
	
	
	
	/**
	 * Retorna los ConveniosIngresoPaciente de un paciente según el contrato
	 * @param codPaciente
	 * @param contrato
	 * @return ConveniosIngresoPaciente
	 * 
	 * @author Cristhian Murillo
	 */
	public ConveniosIngresoPaciente obtenerConvenioIngresoPacientePorContrato (int codPaciente, int contrato);
	
	
	
	
	/**
	 * Guarda la entidad enviada
	 * @param CitasAsociadasAProgramada
	 * @return boolean
	 * @author Cristhian Murillo
	 */
	public boolean guardarCitaAsociadasProgramada(CitasAsociadasAProgramada transientInstance);
	
}
