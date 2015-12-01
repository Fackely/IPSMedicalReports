package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.manejoPaciente.ListadoCamasHospitalizacion;

public interface ListadoCamasHospitalizacionDao 
{

	/**
	 * 
	 * @param mundo
	 * @param oldQuery
	 * @param usuario
	 * @return
	 */
	public HashMap listadoCamasHospitalizacion(Connection con, ListadoCamasHospitalizacion mundo, String condiciones);
	
}
