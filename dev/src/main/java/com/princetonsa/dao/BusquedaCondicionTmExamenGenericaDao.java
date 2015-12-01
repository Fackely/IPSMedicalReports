package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;


/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>BusquedaCondicionTmExamenGenerica</code>.
 * 
 * @author Jose Eduardo Arias Doncel   
 */

public interface BusquedaCondicionTmExamenGenericaDao
{
	
	/**
	 * Consulta Generica de Condiciones de Toma de Examen
	 * @param Connection con
	 * @param Int codigoExamenCt
	 * @param Int institucion
	 * @param String descripcionExamenCt
	 * @param String activo	 
	 * */
	public HashMap consultarCondicionesTmExamenBasica(Connection con, String codigoExamenCt, int institucion, String descripcionExamenCt, String activo, String codigosExamenesInsertados);		
}