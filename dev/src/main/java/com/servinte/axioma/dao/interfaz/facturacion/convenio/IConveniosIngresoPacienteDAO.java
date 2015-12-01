package com.servinte.axioma.dao.interfaz.facturacion.convenio;

import java.util.List;

import com.princetonsa.dto.facturacion.DtoSeccionConvenioPaciente;
import com.servinte.axioma.orm.ConveniosIngresoPaciente;


/**
 * Esta clase se encarga de definir los métodos de
 * negocio para la entidad ConveniosIngresoPaciente
 * 
 * @author Cristhian Murillo
 *
 */
public interface IConveniosIngresoPacienteDAO {
	
	
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
	
	
	
	
	/**
	 * Retorna los ConveniosIngresoPaciente de un paciente según el contrato
	 * @param codPaciente
	 * @param contrato
	 * @return ConveniosIngresoPaciente
	 * 
	 * @author Cristhian Murillo
	 */
	public ConveniosIngresoPaciente obtenerConvenioIngresoPacientePorContrato (int codPaciente, int contrato);
	
}

