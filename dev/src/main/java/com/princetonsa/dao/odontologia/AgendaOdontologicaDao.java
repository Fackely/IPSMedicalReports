package com.princetonsa.dao.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import util.InfoDatosString;

import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;

/**
 * 
 * @author Víctor Hugo Gómez L.
 *
 */

public interface AgendaOdontologicaDao {
	
	/**
	 * se obtiene un listado de los horarios de atencion que cumple con los parametros selecionados
	 * @param con
	 * @param centroAtencion
	 * @param unidadConsulta
	 * @param codigoMedico
	 * @return
	 */
	public ArrayList<DtoAgendaOdontologica> cosultarAgendaOdontologica(Connection con, 
			String centroAtencion, 
			String unidadConsulta, 
			String codigoMedico,
			String fecha,
			String tipoCita);

	/**
	 * Convenciones de Colores por Unidad de Agenda
	 * @param con
	 * @param unidadesAgenda
	 * @return
	 */
	public ArrayList<InfoDatosString> convencionColorUnidaAgenda(Connection con, String unidadesAgenda, String fecha);
	
	/**
	 * modificar el numero de cupos de la agenda odontologica
	 * @param con
	 * @param codigoPK
	 * @param horaFin 
	 * @param usuarioModifica
	 * @param institucion 
	 * @param cupos
	 * @return
	 */
	public int modificarCuposAgendaOdontologica(Connection con, 
			int codigoPK,
			String horaInicio,
			String horaFin,
			String usuarioModifica, boolean disminuir, int institucion);
}
