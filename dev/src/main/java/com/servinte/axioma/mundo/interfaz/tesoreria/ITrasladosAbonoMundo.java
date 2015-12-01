package com.servinte.axioma.mundo.interfaz.tesoreria;

import java.util.List;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.comun.DtoResultado;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladoAbonoPAciente;
import com.princetonsa.dto.tesoreria.DtoGuardarTrasladoAbonoPaciente;
import com.princetonsa.dto.tesoreria.DtoValidacionesTrasladoAbonoPaciente;

/**
 * Define la l&oacute;gica de negocio relacionada con los ingresos y egresos de caja
 * 
 * @author Cristhian Murillo
 * @see com.servinte.axioma.mundo.impl.tesoreria.TrasladosAbonoMundo
 */

public interface ITrasladosAbonoMundo {

	
	/**
	 * Valida so un paciente es valido para realizar un traslado de abonos.
	 * Retorna una lista de errores en el caso de que existan;
	 * 
	 * @param paciente
	 * @return
	 */
	public DtoValidacionesTrasladoAbonoPaciente validarPacienteParaTrasladoAbono(DtoPersonas paciente);
	
	
	
	/**
	 * Valida so un paciente es valido para realizar un traslado de abonos.
	 * Retorna una lista de errores en el caso de que existan;
	 * 
	 * @param paciente
	 * @return
	 */
	public DtoValidacionesTrasladoAbonoPaciente validarPacienteParaTrasladoAbonoDestino(DtoPersonas paciente);
	
	
	
	
	/**
	 * Guarda un traslado de abonos
	 * 
	 * @param paciente
	 * @return
	 */
	public DtoResultado guardarTrasladoAbono(DtoGuardarTrasladoAbonoPaciente dtoGuardarTrasladoAbonoPaciente);
	
	
	
	
	/**
	 * Retorna los detalles  de un traslado de abonos enviando como parametro el codigo del traslado
	 * @param dtoConsulta
	 */
	public List<DtoConsultaTrasladoAbonoPAciente> obtenerDetallesTrasladoAbonos(DtoConsultaTrasladoAbonoPAciente dtoConsulta);
}
