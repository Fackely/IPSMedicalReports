package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.inventarios.Secciones;
import com.princetonsa.mundo.manejoPaciente.EncuestaCalidadAtencion;

/**
 * Interfaz de EncuestaCalidadAtencionDao
 * @author lgchavez@princetosa.com
 *
 */
public interface EncuestaCalidadAtencionDao 
{
	/** 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultar(Connection con, EncuestaCalidadAtencion encuesta) ;
	
	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @param area
	 * @return
	 */
	public HashMap consultarEncuestas(Connection con, int ingreso, String area) ;
	
	/**
	 * 
	 * @param con
	 * @param encuesta
	 * @return
	 */
	public int guardarEncuestas(Connection con, HashMap encuesta) ;
	
	/**
	 * 
	 * @param con
	 * @param encuesta
	 * @return
	 */
	public int modificarEncuestas(Connection con, HashMap encuesta) ;
	
}
