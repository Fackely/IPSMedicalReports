package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

public interface ParametrizacionPlantillasDao 
{
	
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	HashMap consultarEspecialidades(Connection con);
	
	/**
	 * 
	 * @param con
	 * @param plantilla
	 * @param centroCosto
	 * @param sexo
	 * @return
	 */
	HashMap consultarPlantillas(Connection con, String plantilla, String centroCosto, String sexo);
	
	/**
	 * 
	 * @param con
	 * @param plantillaBase
	 * @param codigoInstitucion
	 * @return
	 */
	HashMap consultaSeccionesFijas(Connection con, String plantillaBase, String codigoInstitucion);

	
	
}
