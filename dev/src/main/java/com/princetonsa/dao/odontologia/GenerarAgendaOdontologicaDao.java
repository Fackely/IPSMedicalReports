package com.princetonsa.dao.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.consultaExterna.DtoExcepcionesHorarioAtencion;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.dto.odontologia.DtoExistenciaAgenOdon;
import com.princetonsa.dto.odontologia.DtoGenerarAgenda;
import com.princetonsa.dto.odontologia.DtoHorarioAtencion;

public interface GenerarAgendaOdontologicaDao {
	
	/**
	 * se obtiene un listado de los horarios de atencion que cumple con los parametros 
	 * selecionados para la generacion de la agenda odontologica
	 * @param con
	 * @param centroAtencion
	 * @param unidadConsulta
	 * @param consultorio
	 * @param diaSemana
	 * @param codigoMedico
	 * @return
	 */
	public ArrayList<DtoHorarioAtencion> obtenerHorariosAtencion(Connection con, 
			String centroAtencion, 
			String unidadConsulta, 
			String consultorio,
			String diaSemana,
			String codigoMedico);
	
	/**
	 * verifica las excepciones del centro de atencion para la generacion de la agenda
	 * @param con
	 * @param fecha
	 * @param centroAtencion
	 * @return
	 */
	public String verificarExcepcionCentroAtencion(Connection con, String fecha, int centroAtencion);
	
	/**
	 * se verifica si la agenda odontologica ya fue generada 
	 * @param con
	 * @param centroAtencion
	 * @param unidadAgenda
	 * @param consultorio
	 * @param diaSemana
	 * @param fecha
	 * @param horaIni
	 * @param horaFin
	 * @return ArrayList<DtoExistenciaAgenOdon>
	 */
	public ArrayList<DtoExistenciaAgenOdon> verificarExistenciaAgendaOdontologica(Connection con, 
			int centroAtencion,
			int unidadAgenda,
			int consultorio,
			int diaSemana,
			String fecha);
	
	/**
	 * insertar agenda odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public int insertarAgendaOdontologica(Connection con, DtoAgendaOdontologica dto);
	
	/**
	 * actualizar los tiempos de la agenda odontologica
	 * @param con
	 * @param dto
	 * @param horaIni
	 * @param horaFin
	 * @return
	 */
	public boolean actualizarAgendaOdontologica(Connection con, DtoAgendaOdontologica dto, boolean horaIni, boolean horaFin);
	
	/**
	 * metodo que consulta los datos descriptivos de una agenda odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public DtoAgendaOdontologica consultarAgendaOdontologica(Connection con, DtoAgendaOdontologica dto);
	
	/**
	 * metodo que consulta los datos descriptivos de una agenda odontologica pero a partir del horario
	 * @param con
	 * @param dto
	 * @return
	 */
	public DtoAgendaOdontologica consultarHorarioAtencion(Connection con, DtoAgendaOdontologica dto);
	
	/**
	 * se obtiene un listado de los horarios de atencion para la regeneracin de agenda odontologica
	 * @param con
	 * @param codigoHorarioAtencion
	 * @return
	 */
	public ArrayList<DtoHorarioAtencion> obtenerHorariosAtencion(Connection con, String codigoHorarioAtencion);
	
	/**
	 * Se obtienen consultorios ocupados para impedir generación de agenda
	 * @param con
	 * @param consultorio
	 * @return
	 */
	public boolean existeConsultoriosAgendados (Connection con, 
			int consultorio, int dia, String fecha, String horaInicio, String horaFin, int centroAtencion);
	
	/**
	 * se verifica si la agenda odontologica ya fue generada 
	 * @param con
	 * @param centroAtencion
	 * @param unidadAgenda
	 * @param consultorio
	 * @param diaSemana
	 * @param fecha
	 * @param horaIni
	 * @param horaFin
	 * @return ArrayList<DtoExistenciaAgenOdon>
	 */
	public String isInsertAgendaOdontologica(Connection con, DtoAgendaOdontologica dto);
	
	/**
	 * se obtiene un listado de los horarios de atencion que cumple con los parametros 
	 * selecionados para la generacion de la agenda odontologica
	 * @param con
	 * @param centroAtencion
	 * @param unidadConsulta
	 * @param consultorio
	 * @param diaSemana
	 * @param codigoMedico
	 * @return
	 */
	public ArrayList<DtoAgendaOdontologica> cosultaAgendaOdontologica(Connection con, 
			String centroAtencion, 
			String unidadConsulta, 
			String consultorio,
			String diaSemana,
			String codigoMedico,
			String fechaIni,
			String fechaFin);

	public ArrayList<DtoExcepcionesHorarioAtencion> cosultaExAgendaOdontologica(
			Connection con, String centroAtencion, String unidadAgenda,
			String consultorio, String diaSemana, String profesionalSalud,
			String fechaInicial, String fechaFinal, Boolean esGenerar);
}
