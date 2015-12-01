package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.historiaClinica.RegistroEventosAdversos;
import com.princetonsa.mundo.inventarios.Secciones;
import com.princetonsa.mundo.manejoPaciente.EncuestaCalidadAtencion;

/**
 * Interfaz de EncuestaCalidadAtencionDao
 * @author lgchavez@princetosa.com
 *
 */
public interface RegistroEventosAdversosDao 
{
	/** 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultar(Connection con, RegistroEventosAdversos encuesta) ;
	
	/** 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarDetalleXCuenta(Connection con, RegistroEventosAdversos encuesta) ;
	
	/** 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarDetalleXCuenta2(Connection con, RegistroEventosAdversos encuesta) ;
	
	/**
	 * 
	 * @param con
	 * @param encuesta
	 * @return
	 */
	public int guardarEvento(Connection con, HashMap filtro) ;
	
	/**
	 * 
	 * @param con
	 * @param encuesta
	 * @return
	 */
	public int modificar(Connection con, HashMap filtro) ;
	
}
