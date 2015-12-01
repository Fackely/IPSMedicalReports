/**
 * 
 */
package com.princetonsa.dao.oracle.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.odontologia.ReasignarProfesionalOdontoDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseReasignarProfesionalOdontoDao;
import com.princetonsa.dto.odontologia.DtoInfoFechaUsuario;
import com.princetonsa.dto.odontologia.DtoReasignarProfesionalOdonto;

/**
 * @author Jairo Andrés Gómez
 * noviembre 9 / 2009
 *
 */
public class OracleReasignarProfesionalOdontoDao implements ReasignarProfesionalOdontoDao {
	
	/**
	 * Método que consulta la especialidad de una unidad de agenda(consulta).
	 * @param connection
	 * @param unidadAgenda
	 * @return
	 */
	public int consultarEspecialidadUnidadAgenda (Connection connection, int unidadAgenda){
		return SqlBaseReasignarProfesionalOdontoDao.consultarEspecialidadUnidadAgenda(connection, unidadAgenda);
	}
	
	/**
	 * Método que retorna el conjunto de registros resultado de la busqueda de servicios para reasignar profesional
	 * @param connection
	 * @param dtoReasignarProfesionalOdonto
	 * @return
	 */
	public ArrayList<DtoReasignarProfesionalOdonto> consultarServiciosAReasignar (Connection connection, DtoReasignarProfesionalOdonto dtoReasignarProfesionalOdonto){
		return SqlBaseReasignarProfesionalOdontoDao.consultarServiciosAReasignar(connection, dtoReasignarProfesionalOdonto);
	}
	
	/**
	 * Método que consulta si un prodfesional de la salud ya está agendado en una fecha y una hora determinada.
	 * retorna true en caso de que este disponible
	 * @param connection
	 * @param codigoMedico
	 * @param fecha
	 * @param horaIni
	 * @param horaFin
	 * @return
	 */
	public boolean confirmacionDisponibilidadAgenda (Connection connection, int codigoMedico, String fecha, String horaIni, String horaFin){
		return SqlBaseReasignarProfesionalOdontoDao.confirmacionDisponibilidadAgenda(connection, codigoMedico, fecha, horaIni, horaFin);
	}
	
	/**
	 * Método que actualiza el codigo del medico que esta realacionado con derterminada agenda
	 * @param connection
	 * @param infoFechaUsuario
	 * @param codigoMedico
	 * @param codigoPk
	 * @return
	 */
	public boolean actualizarAgendaOdontologica(Connection connection, DtoInfoFechaUsuario infoFechaUsuario, int codigoMedico, int codigoPk){
		return SqlBaseReasignarProfesionalOdontoDao.actualizarAgendaOdontologica(connection, infoFechaUsuario, codigoMedico, codigoPk);
	}
	
	/**
	 * Método que inserta un nuevo registro en el log de reasignacion de profesionales 
	 * @param connection
	 * @param dto
	 * @return
	 */
	public boolean insertarLogReasignarProfesionalOdonto(Connection connection, DtoReasignarProfesionalOdonto dto){
		return SqlBaseReasignarProfesionalOdontoDao.insertarLogReasignarProfesionalOdonto(connection, dto);
	}
	
	/**
	 * Método que consulta la informacion almacenada en el log de reasignaciones de profesional
	 * @param connection
	 * @param dtoReasignarProfesionalOdonto
	 * @return
	 */
	public ArrayList<DtoReasignarProfesionalOdonto> consultarLogReasignacionProfesional (Connection connection, DtoReasignarProfesionalOdonto dtoReasignarProfesionalOdonto){
		return SqlBaseReasignarProfesionalOdontoDao.consultarLogReasignacionProfesional(connection, dtoReasignarProfesionalOdonto);
	}
}
