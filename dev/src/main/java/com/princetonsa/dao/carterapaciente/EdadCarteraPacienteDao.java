package com.princetonsa.dao.carterapaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoReporteEdadCarteraPaciente;

public interface EdadCarteraPacienteDao
{
	/**
	 * Consulta listado de edad glosa
	 * @param Connection con
	 * @param  HashMap parametros
	 * */
	public ArrayList<DtoDatosFinanciacion> getListadoEdadGlosa(Connection con, HashMap parametros);
}