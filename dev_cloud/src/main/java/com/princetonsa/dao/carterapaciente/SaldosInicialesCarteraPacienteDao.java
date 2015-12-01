package com.princetonsa.dao.carterapaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoSaldosInicialesCarteraPaciente;

/**
 * 
 * @author armando
 *
 */
public interface SaldosInicialesCarteraPacienteDao 
{
	/**
	 * 
	 * @param parametros
	 * @return
	 */
	public abstract ArrayList<DtoSaldosInicialesCarteraPaciente> busquedaAvanzadaSaldosIncialesCP(HashMap<String, String> parametros);

	/**
	 * 
	 * @param con 
	 * @param paciente
	 * @return
	 */
	public abstract int insertarActualizarPersona(Connection con, DtoPaciente paciente);

	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public abstract DtoDatosFinanciacion cargarDatosFinanciacion(Connection con, DtoDatosFinanciacion dto);

	public abstract boolean actualizarDatosFinanciacion(Connection con,
			DtoDatosFinanciacion dto);

}
