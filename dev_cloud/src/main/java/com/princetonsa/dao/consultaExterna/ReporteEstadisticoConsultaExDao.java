package com.princetonsa.dao.consultaExterna;

import java.sql.Connection;
import java.util.HashMap;

public interface ReporteEstadisticoConsultaExDao
{
	/**
	 * Arma la sentencia del Where a partir de los filtros de la busqueda
	 * @param HashMap parametros
	 * */
	public HashMap getCondicionesBuquedaReporte(HashMap parametros);
	
	/**
	 * @param Connection con
	 * @param String where  
	 * */
	public HashMap getCargarDatosBasicosCancelacionCitas(Connection con,HashMap parametros); 
}