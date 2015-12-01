/*
 * Oct 4, 2005
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;



/**
 * @author Luis Gabriel Chavez Salazar
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * Consulta Materiales Qx
 */
public interface ConsultaImpresionMaterialesQxDao 
{
	/**
	 * 
	 * @param con
	 * @param criterios
	 * @param espaciente
	 * @return
	 */
	public HashMap consultaIngresosPaciente(Connection con, HashMap criterios);
	
	/**
	 * 
	 * @param con
	 * @param criterios
	 * @return
	 */        
	public HashMap cargarSolicitudesCx(Connection con, HashMap criterios, boolean espaciente);
	
	/**
	 * 
	 * @param con
	 * @param criterios
	 * @param esacto
	 * @return
	 */
	public HashMap cargarConsumoMaterialesCirugias(Connection con, HashMap criterios, boolean esacto);
	
}
