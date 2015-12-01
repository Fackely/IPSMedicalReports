package com.princetonsa.dao.oracle.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.odontologia.GenerarAgendaOdontologicaDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseGenerarAgendaOdontologicaDao;
import com.princetonsa.dto.consultaExterna.DtoExcepcionesHorarioAtencion;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.dto.odontologia.DtoExistenciaAgenOdon;
import com.princetonsa.dto.odontologia.DtoHorarioAtencion;

/**
 * 
 * @author Víctor Hugo Gómez L.
 *
 */
public class OracleGenerarAgendaOdontologicaDao implements GenerarAgendaOdontologicaDao
{
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
			String codigoMedico)
	{
		return SqlBaseGenerarAgendaOdontologicaDao.obtenerHorariosAtencion(con, centroAtencion, unidadConsulta, consultorio, diaSemana, codigoMedico);
	}
	
	/**
	 * verifica las excepciones del centro de atencion para la generacion de la agenda
	 * @param con
	 * @param fecha
	 * @param centroAtencion
	 * @return
	 */
	public String verificarExcepcionCentroAtencion(Connection con, String fecha, int centroAtencion)
	{
		return SqlBaseGenerarAgendaOdontologicaDao.verificarExcepcionCentroAtencion(con, fecha, centroAtencion);
	}
	
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
			String fecha)
	{
		return SqlBaseGenerarAgendaOdontologicaDao.verificarExistenciaAgendaOdontologica(con, centroAtencion, unidadAgenda, consultorio, diaSemana, fecha);
	}

	/**
	 * insertar agenda odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public int insertarAgendaOdontologica(Connection con, DtoAgendaOdontologica dto)
	{
		return SqlBaseGenerarAgendaOdontologicaDao.insertarAgendaOdontologica(con, dto);
	}
	
	/**
	 * actualizar los tiempos de la agenda odontologica
	 * @param con
	 * @param dto
	 * @param horaIni
	 * @param horaFin
	 * @return
	 */
	public boolean actualizarAgendaOdontologica(Connection con, DtoAgendaOdontologica dto, boolean horaIni, boolean horaFin)
	{
		return SqlBaseGenerarAgendaOdontologicaDao.actualizarAgendaOdontologica(con, dto, horaIni, horaFin);
	}
	
	/**
	 * metodo que consulta los datos descriptivos de una agenda odontologica
	 * @param con
	 * @param dto
	 * @return
	 */
	public DtoAgendaOdontologica consultarAgendaOdontologica(Connection con, DtoAgendaOdontologica dto)
	{
		return SqlBaseGenerarAgendaOdontologicaDao.consultarAgendaOdontologica(con, dto);
	}
	
	/**
	 * metodo que consulta los datos descriptivos de una agenda odontologica pero a partir del horario
	 * @param con
	 * @param dto
	 * @return
	 */
	public DtoAgendaOdontologica consultarHorarioAtencion(Connection con, DtoAgendaOdontologica dto)
	{
		return SqlBaseGenerarAgendaOdontologicaDao.consultarHorarioAtencion(con, dto);
	}
	
	/**
	 * se obtiene un listado de los horarios de atencion para la regeneracin de agenda odontologica
	 * @param con
	 * @param codigoHorarioAtencion
	 * @return
	 */
	public ArrayList<DtoHorarioAtencion> obtenerHorariosAtencion(Connection con, String codigoHorarioAtencion)
	{
		return SqlBaseGenerarAgendaOdontologicaDao.obtenerHorariosAtencion(con, codigoHorarioAtencion);
	}
	
	/**
	 * Se obtienen consultorios ocupados para impedir generación de agenda
	 */
	public boolean existeConsultoriosAgendados(Connection con, 
			int consultorio, int dia, String fecha, String horaInicio, String horaFin, int centroAtencion)
	{
		return SqlBaseGenerarAgendaOdontologicaDao.existeConsultoriosAgendados(con, consultorio, dia, fecha, horaInicio, horaFin, centroAtencion);
	}
	
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
	public String isInsertAgendaOdontologica(Connection con, DtoAgendaOdontologica dto)
	{
		return SqlBaseGenerarAgendaOdontologicaDao.isInsertAgendaOdontologica(con, dto);
	}
	
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
			String fechaFin)
	{
		return SqlBaseGenerarAgendaOdontologicaDao.cosultaAgendaOdontologica(con, centroAtencion, unidadConsulta, consultorio, diaSemana, codigoMedico, fechaIni, fechaFin);
	}
	
	public ArrayList<DtoExcepcionesHorarioAtencion> cosultaExAgendaOdontologica(
			Connection con, String centroAtencion, String unidadAgenda,
			String consultorio, String diaSemana, String profesionalSalud,
			String fechaInicial, String fechaFinal, Boolean esGenerar)
	{
		return SqlBaseGenerarAgendaOdontologicaDao.cosultaExAgendaOdontologica(
				con, centroAtencion, unidadAgenda,
				consultorio, diaSemana, profesionalSalud,
				fechaInicial, fechaFinal, esGenerar);
	}
}
