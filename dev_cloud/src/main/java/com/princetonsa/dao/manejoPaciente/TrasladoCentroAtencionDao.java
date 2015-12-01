/*
 * Sep 28, 06
 */
package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

/**
 * @author Sebasti�n G�mez 
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * Traslado de Centros de Atenci�n
 */
public interface TrasladoCentroAtencionDao 
{
	/**
	 * M�todo implementado para realizar las validaciones de ingreso
	 * a la funcionalidad Traslado Centro Atencion
	 * @param con
	 * @param campos
	 * @return
	 */
	public Vector validaciones(Connection con,HashMap campos);
	
	/**
	 * M�todo que desasigna la cama de una admision de urgencias
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int desasignarCamaUrgencias(Connection con,String idCuenta);
	
	/**
	 * M�todo implementado para realizar el traslado por centro de atencion
	 * @param con
	 * @param campos
	 * @return
	 */
	public int realizarTraslado(Connection con,HashMap campos);
}

