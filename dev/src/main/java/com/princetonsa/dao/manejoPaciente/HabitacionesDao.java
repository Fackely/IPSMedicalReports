/*
 * @(#)HabitacionesDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.mundo.manejoPaciente.Habitaciones;


/**
* 
* @author Julián Pacheco
* jpacheco@princetonsa.com
*/
public interface HabitacionesDao 
{
	/**
	 * Consulta las n habitaciones x centro atencion 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap habitacionesXCentroAtencionTipo(Connection con, int centroAtencion,int codigoInstitucion);
	
	/**
	 * Insertar un registro de habitaciones
	 * @param con
	 * @param Habitaciones habitaciones
	 */
	public boolean insertarHabitaciones(Connection con, Habitaciones habitaciones, int codigoInstitucion); 
	

	/**
	 * Modifica una habitacion registrada
	 * @param con
	 * @param Habitaciones habitaciones
	 */
	public boolean modificarHabitaciones(Connection con, Habitaciones habitaciones);
	
	/**
	 * Elimina una habitacion registrada
	 * @param con
	 * @int codigo
	 * @int institucion
	 */
	public boolean eliminarHabitaciones(Connection con, int codigo);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public HashMap consultarHabitaciones(Connection con, int codigoInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param codigohabitac
	 * @return
	 */
	public HashMap consultarHabitacionesEspecifico(Connection con, int codigo);
	
	/**
	 * Consulta avanzada de habitaciones por cada uno de los campos
	 * @param con
	 * @param  
	 * */
	/*public HashMap consultaHabitacionesAvanzada(Connection con,HashMap condicion);*/
}
