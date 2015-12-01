package com.princetonsa.dao.consultaExterna;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ResultadoBoolean;

import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseMultasDao;
import com.princetonsa.dto.consultaExterna.DtoCitasNoRealizadas;
import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.princetonsa.dto.consultaExterna.DtoServiciosCitas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Jairo Gómez Fecha Junio de 2009
 */

public interface MultasDao {

	public ArrayList<DtoCitasNoRealizadas> buscarCitasNoRealizadasRango(Connection connection,
			HashMap criterios, UsuarioBasico usuario);
	
	public ArrayList<DtoCitasNoRealizadas> buscarCitasNoRealizadasPaciente(
			Connection connection, HashMap criterios, UsuarioBasico usuario, PersonaBasica paciente);
	
	public ArrayList<DtoServiciosCitas> buscarDetalleCitasRango(Connection connection,
			UsuarioBasico usuario, DtoCitasNoRealizadas dtoCitasNoRealizadas);

	public ArrayList<DtoConceptoFacturaVaria> setArrayConcepFact(
			Connection connection, UsuarioBasico usuario);

	public int insertarMulta(Connection connection,
			UsuarioBasico usuario, String convenioManejaMulta, String valorMultaConvenio, int codCita);

	/**
	 * Modificar el estado multa cita
	 * @param Connection connection
	 * @param HashMap parametros
	 * @return boolean
	 */
	public boolean actualizarEstadoMultaCita(Connection connection, HashMap parametros);
	
	/**
	 * Verificar el estado de la cita
	 * @param Connection connection
	 * @param HashMap parametros
	 * @return boolean
	 */
	public boolean verificarEstadoMultaCita(Connection connection, HashMap parametros);
	
	/**
	 * se Obtiene los conceptos que estan activo
	 * @param Connection connection
	 * @param UsuarioBasico usuario
	 * @return ArrayList<DtoConceptoFacturaVaria>
	 */
	public ArrayList<DtoConceptoFacturaVaria> getArrayConcepFactVarias(
			Connection connection, UsuarioBasico usuario);

	public ResultadoBoolean actualizarEstadoMedicoSolicitudYDetCargos(
			Connection con, int numSolicitud);
}
