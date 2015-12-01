/**
 * 
 */
package com.servinte.axioma.bl.consultaExterna.interfaz;

import java.util.List;

import com.servinte.axioma.dto.administracion.ProfesionalSaludDto;
import com.servinte.axioma.dto.consultaExterna.CitaDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author jeilones
 * @created 23/10/2012
 *
 */
public interface ICitasMundo {

	
	/**
	 * Consulta las citas que han sido atendidas por el un profesional X a un paciente Y
	 * 
	 * @param loginUsuario
	 * @param codigoIngreso
	 * @param codigoPaciente
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 23/10/2012
	 */
	List<CitaDto> consultarCitasAtentidas(String loginUsuario,int codigoIngreso,int codigoPaciente) throws IPSException;
	
	/**
	 * Consulta todos los profesionales que han atendido citas
	 * 
	 * @param codigoInsitucion
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 15/11/2012
	 */
	List<ProfesionalSaludDto> consultarProfesionalesAtiendenCitas(int codigoInsitucion) throws IPSException;
}
