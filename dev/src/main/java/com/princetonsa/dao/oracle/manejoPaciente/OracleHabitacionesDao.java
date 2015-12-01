/*
 * @(#)OracleHabitacionesDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */
package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.HabitacionesDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseHabitacionesDao;
import com.princetonsa.mundo.manejoPaciente.Habitaciones;


/**
 * 
 * @author Julián Pacheco
 * jpacheco@princetonsa.com
 * Funcionalidad descrita en Anexo 415 - Habitaciones
 */
public class OracleHabitacionesDao implements HabitacionesDao
{
	/**
	 * Consulta las n habitaciones x centro atencion 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap habitacionesXCentroAtencionTipo(Connection con, int centroAtencion,int codigoInstitucion)
	{
		return SqlBaseHabitacionesDao.habitacionesXCentroAtencionTipo(con, centroAtencion, codigoInstitucion);
	}
	
	/**
	 * Insertar un registro de habitaciones
	 * @param con
	 * @param Habitaciones habitaciones
	 */
	public boolean insertarHabitaciones(Connection con, Habitaciones habitaciones, int codigoInstitucion)
	{
		return SqlBaseHabitacionesDao.insertarHabitaciones(con, habitaciones, codigoInstitucion);
	}
	
	/**
	 * Modifica una habitacion registrada
	 * @param con
	 * @param  Habitaciones habitaciones 
	 */
	public boolean modificarHabitaciones(Connection con, Habitaciones habitaciones)
	{
		return SqlBaseHabitacionesDao.modificarHabitaciones(con, habitaciones);
	}
	
	/**
	 * Elimina una habitacion registrada
	 * @param con
	 * @param int codigo
	 * @param int institucion
	 */
	public boolean eliminarHabitaciones(Connection con, int codigo)
	{
		return SqlBaseHabitacionesDao.eliminarHabitaciones(con, codigo);
	}
	
	/**
	 * Consulta las habitaciones por centro de atencion
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarHabitaciones(Connection con, int codigoInstitucion) 
	{
		return SqlBaseHabitacionesDao.consultarHabitaciones(con, codigoInstitucion);
	}
	
	/**
	 * Consulta las habitaciones por centro de atencion por medio del codigo
	 */
	public HashMap consultarHabitacionesEspecifico(Connection con, int codigo)
	{
		return SqlBaseHabitacionesDao.consultarHabitacionesEspecifico(con, codigo);
	}
	
	/**
	 * Consulta avanzada de habitaciones por cada uno de los campos
	 * @param con
	 * @param  
	 * */
	/*public HashMap consultaHabitacionesAvanzada(Connection con,HashMap condicion)
	{
		return SqlBaseHabitacionesDao.consultaHabitacionesAvanzada(con, condicion);		
	}*/
}
