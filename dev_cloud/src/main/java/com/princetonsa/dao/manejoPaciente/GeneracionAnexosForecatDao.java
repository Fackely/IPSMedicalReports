/*
 * Abril 30, 2007
 */
package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * Generación Anexos Forecat
 */
public interface GeneracionAnexosForecatDao 
{
	/**
	 * Método que consulta el archivo AA : ACCIDENTES O EVENTOS CATASTRÓFICOS Y TERRORISTAS
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultaAA(Connection con,HashMap campos,boolean esAxRips, boolean esCuentaCobro);
	
	/**
	 * Método que consulta el archivo VH: VEHÍCULOS
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultaVH(Connection con,HashMap campos,boolean esAxRips, boolean esCuentaCobro);
	
	/**
	 * Método que consulta el archivo AV: ATENCIÓN DE LA VÍCTIMA
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultaAV(Connection con,HashMap campos, boolean esAxRips,boolean esCuentaCobro);
}
