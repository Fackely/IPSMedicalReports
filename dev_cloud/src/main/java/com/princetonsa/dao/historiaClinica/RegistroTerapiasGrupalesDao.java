/*
 * Oct 22, 2007
 * Proyect axioma_oct0707
 * Paquete com.princetonsa.dao.historiaClinica
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.dao.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public interface RegistroTerapiasGrupalesDao 
{

	/**
	 * 
	 * @param con
	 * @param centroAtencion 
	 * @param codigoSexo 
	 * @param facha
	 */
	public abstract HashMap<String, Object> getpacientesTerapiasGrupales(Connection con, String fecha, int centroAtencion, int codigoSexo);

	
	/**
	 * 
	 * @param con
	 * @param codigoTerapia 
	 * @param vo
	 * @return
	 */
	public abstract boolean insertarCuentasTerapia(Connection con, int codigoTerapia, HashMap<String, Object> vo);
	
	/**
	 * 
	 * @param con
	 * @param fecha
	 * @param centroAtencion
	 * @param codigoSexo
	 * @param centroCosto
	 * @return
	 */
	public abstract HashMap<String, Object> getpacientesTerapiasGrupalesCentro(Connection con, String fecha, int centroAtencion, int codigoSexo, String centroCosto);



}
