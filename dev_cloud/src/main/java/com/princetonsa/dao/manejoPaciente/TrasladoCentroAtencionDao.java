/*
 * Sep 28, 06
 */
package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * Traslado de Centros de Atención
 */
public interface TrasladoCentroAtencionDao 
{
	/**
	 * Método implementado para realizar las validaciones de ingreso
	 * a la funcionalidad Traslado Centro Atencion
	 * @param con
	 * @param campos
	 * @return
	 */
	public Vector validaciones(Connection con,HashMap campos);
	
	/**
	 * Método que desasigna la cama de una admision de urgencias
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int desasignarCamaUrgencias(Connection con,String idCuenta);
	
	/**
	 * Método implementado para realizar el traslado por centro de atencion
	 * @param con
	 * @param campos
	 * @return
	 */
	public int realizarTraslado(Connection con,HashMap campos);
}

