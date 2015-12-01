/**
 * 
 */
package com.princetonsa.mundo.carteraPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoSaldosInicialesCarteraPaciente;

/**
 * @author armando
 *
 */
public class SaldosInicialesCarteraPaciente 
{

	/**
	 * 
	 * @param paremetrosBusqueda
	 * @return
	 */
	public static ArrayList<DtoSaldosInicialesCarteraPaciente> busquedaAvanzadaSaldosIncialesCP(HashMap<String, String> paremetrosBusqueda) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSaldosInicialesCarteraPacienteDao().busquedaAvanzadaSaldosIncialesCP(paremetrosBusqueda);
	}

	/**
	 * 
	 * @param con
	 * @param paciente
	 * @return
	 */
	public static int insertarActualizarPersona(Connection con, DtoPaciente paciente) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSaldosInicialesCarteraPacienteDao().insertarActualizarPersona(con,paciente);
	}

	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static DtoDatosFinanciacion cargarDatosFinanciacion(Connection con, DtoDatosFinanciacion dto) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSaldosInicialesCarteraPacienteDao().cargarDatosFinanciacion(con, dto);
	}

	public static boolean actualizarDatosFinanciacion(Connection con, DtoDatosFinanciacion dto) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSaldosInicialesCarteraPacienteDao().actualizarDatosFinanciacion(con, dto);
	}

}
