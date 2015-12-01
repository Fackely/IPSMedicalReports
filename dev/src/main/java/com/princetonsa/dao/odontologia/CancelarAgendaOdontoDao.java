/**
 * 
 */
package com.princetonsa.dao.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import util.ResultadoBoolean;

import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;
import com.princetonsa.dto.odontologia.DtoCancelarAgendaOdonto;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;

/**
 * @author axioma
 *
 */
public interface CancelarAgendaOdontoDao {
	/**
	 * M�todo que permite consultar la agenda odontologica generada
	 * @param connection
	 * @param dtoCancelarAgendaOdonto
	 * pueden venir vacios siempre y cuando vengan los valores en el dto, 
	 * en caso contrario debe traer los ids separados por comas
	 * @param centrosAtencion 
	 * @param unidadesAgenda
	 * @return
	 */
	public ArrayList<DtoAgendaOdontologica> consultarAgendaOdonto(Connection connection, DtoCancelarAgendaOdonto dtoCancelarAgendaOdonto, String centrosAtencion, String unidadesAgenda);
	
	/**
	 * M�todo que actualiza el campo activo de la tabla odontologia.agenda_odontologica
	 * @param connection
	 * @param codigo_pk
	 * @return
	 */
	public boolean actualizarActivoAgenda(Connection connection, int codigo_pk);
	
	/**
	 * M�todo que inserta el detalle de la cancelacion de agenda odontologica
	 * @param connection
	 * @param dtoCita
	 * @param codigoCancelacion
	 * @return
	 */
	public boolean insertardetalleCancelacionAgenOdo(Connection connection, DtoCitaOdontologica dtoCita, int codigoCancelacion, int numeroSolicitud);
	
	/**
	 * M�todo que inserta en odontologia.cancelacion_agenda_odo
	 * @param connection
	 * @param dtoAgenda
	 * @return
	 */
	public ResultadoBoolean insertarCancelaAgendaOdo(Connection connection, DtoAgendaOdontologica dtoAgenda);
	
	/**
	 * M�todo utilizado para eliminar el registro de las agendas que no tiene citas asignada y son canceladas
	 * @param connection
	 * @param codigoAgenda
	 * @return
	 */
	public boolean eliminarAgendaOdontologica(Connection connection, int codigoAgenda);
	
	/**
	 * M�todo que devuelve el numero de citas por agenda no reservadas ni asignadas
	 * @param connection
	 * @param codigoAgenda
	 * @return
	 */
	public int consultarNoCitasXAgenda (Connection connection, int codigoAgenda);

	public ArrayList<DtoCancelarAgendaOdonto> consultarCancelacionAgendaOdonto(int codigoPk);
}
