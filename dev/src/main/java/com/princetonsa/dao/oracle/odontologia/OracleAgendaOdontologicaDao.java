package com.princetonsa.dao.oracle.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import util.InfoDatosString;

import com.princetonsa.dao.odontologia.AgendaOdontologicaDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseAgendaOdontologicaDao;
import com.princetonsa.dto.odontologia.DtoAgendaOdontologica;

/**
 * 
 * @author Víctor Hugo Gómez L.
 *
 */

public class OracleAgendaOdontologicaDao implements AgendaOdontologicaDao{
	
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
			String tipoCita)
	{
		return SqlBaseAgendaOdontologicaDao.cosultarAgendaOdontologica(con, centroAtencion, unidadConsulta, codigoMedico, fecha, tipoCita);
	}
	
	/**
	 * Convenciones de Colores por Unidad de Agenda
	 * @param con
	 * @param unidadesAgenda
	 * @return
	 */
	public ArrayList<InfoDatosString> convencionColorUnidaAgenda(Connection con, String unidadesAgenda, String fecha)
	{
		return SqlBaseAgendaOdontologicaDao.convencionColorUnidaAgenda(con, unidadesAgenda, fecha);
	}
	
	/**
	 * modificar el numero de cupos de la agenda odontologica
	 * @param con
	 * @param codigoPK
	 * @param usuarioModifica
	 * @param cupos
	 * @return
	 */
	public int modificarCuposAgendaOdontologica(Connection con, 
			int codigoPK,
			String horaInicio,
			String horaFin,
			String usuarioModifica, boolean disminuir, int institucion)
	{
		return SqlBaseAgendaOdontologicaDao.modificarCuposAgendaOdontologica(con, codigoPK, horaInicio, horaFin, usuarioModifica, disminuir, institucion);
	}
}
