package com.princetonsa.dao.postgresql.consultaExterna;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ResultadoBoolean;

import com.princetonsa.dao.consultaExterna.MultasDao;
import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseMultasDao;
import com.princetonsa.dto.consultaExterna.DtoCitasNoRealizadas;
import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.princetonsa.dto.consultaExterna.DtoServiciosCitas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Jairo Gómez Fecha Junio de 2009
 */

public class PostgresqlMultasDao implements MultasDao {
	/**
	 * 
	 */
	public ArrayList<DtoCitasNoRealizadas> buscarCitasNoRealizadasRango(
			Connection connection, HashMap criterios, UsuarioBasico usuario) {

		return SqlBaseMultasDao.buscarCitasNoRealizadasRango(connection,
				criterios, usuario);
	}
	
	public ArrayList<DtoCitasNoRealizadas> buscarCitasNoRealizadasPaciente(
			Connection connection, HashMap criterios, UsuarioBasico usuario, PersonaBasica paciente) {

		return SqlBaseMultasDao.buscarCitasNoRealizadasPaciente(connection,
				criterios, usuario, paciente);
	}

	public ArrayList<DtoServiciosCitas> buscarDetalleCitasRango(
			Connection connection, UsuarioBasico usuario,
			DtoCitasNoRealizadas dtoCitasNoRealizadas) {

		return SqlBaseMultasDao.buscarDetalleCitasRango(connection, usuario,
				dtoCitasNoRealizadas);
	}

	public ArrayList<DtoConceptoFacturaVaria> setArrayConcepFact(
			Connection connection, UsuarioBasico usuario) {
		return SqlBaseMultasDao.setArrayConcepFact(connection, usuario);
	}
	
	public int insertarMulta(Connection connection,
			UsuarioBasico usuario, String convenioManejaMulta,
			String valorMultaConvenio, int codCita) {
		return SqlBaseMultasDao.insertarMulta(connection, usuario,
				convenioManejaMulta, valorMultaConvenio, codCita);
	}
	
	/**
	 * Modificar el estado multa cita
	 * @param Connection connection
	 * @param HashMap parametros
	 * @return boolean
	 */
	public boolean actualizarEstadoMultaCita(Connection connection, HashMap parametros)
	{
		return SqlBaseMultasDao.actualizarEstadoMultaCita(connection, parametros); 
	}
	
	/**
	 * Verificar el estado de la cita
	 * @param Connection connection
	 * @param HashMap parametros
	 * @return boolean
	 */
	public boolean verificarEstadoMultaCita(Connection connection, HashMap parametros)
	{
		return SqlBaseMultasDao.verificarEstadoMultaCita(connection, parametros);
	}
	
	/**
	 * se Obtiene los conceptos que estan activo
	 * @param Connection connection
	 * @param UsuarioBasico usuario
	 * @return ArrayList<DtoConceptoFacturaVaria>
	 */
	public ArrayList<DtoConceptoFacturaVaria> getArrayConcepFactVarias(Connection connection, UsuarioBasico usuario)
	{
		return SqlBaseMultasDao.getArrayConcepFactVarias(connection, usuario);
	}
	
	public ResultadoBoolean actualizarEstadoMedicoSolicitudYDetCargos (Connection con, int numSolicitud)
	{
		return SqlBaseMultasDao.actualizarEstadoMedicoSolicitudYDetCargos(con, numSolicitud);
	}
}