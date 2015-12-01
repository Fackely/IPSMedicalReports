package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Mauricio Jllo
 * Fecha: Mayo de 2008
 */

public interface PacientesHospitalizadosDao
{

	/**
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract HashMap consultarPacientesHospitalizados(Connection con, HashMap vo);

	/**
	 * @param con
	 * @param institucion
	 * @param tipoArea
	 * @param tipoPaciente
	 * @param codigoCentroAtencion
	 * @return
	 */
	public abstract HashMap obtenerCentrosCosto(Connection con, int institucion, String tipoArea, String tipoPaciente, String codigoCentroAtencion);
	
}
