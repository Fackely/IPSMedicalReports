/*
 * Abril 30, 2007
 */
package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebasti�n G�mez 
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * Generaci�n Anexos Forecat
 */
public interface GeneracionAnexosForecatDao 
{
	/**
	 * M�todo que consulta el archivo AA : ACCIDENTES O EVENTOS CATASTR�FICOS Y TERRORISTAS
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultaAA(Connection con,HashMap campos,boolean esAxRips, boolean esCuentaCobro);
	
	/**
	 * M�todo que consulta el archivo VH: VEH�CULOS
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultaVH(Connection con,HashMap campos,boolean esAxRips, boolean esCuentaCobro);
	
	/**
	 * M�todo que consulta el archivo AV: ATENCI�N DE LA V�CTIMA
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultaAV(Connection con,HashMap campos, boolean esAxRips,boolean esCuentaCobro);
}
