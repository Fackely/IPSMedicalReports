/*
 * @(#)TipoHabitacionDao.java
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

import com.princetonsa.mundo.manejoPaciente.TipoHabitacion;

/**
* 
* @author Julián Pacheco
* jpacheco@princetonsa.com
*/

public interface TipoHabitacionDao 
{
	/**
	 * Insertar un registro de tipo habitacion
	 * @param con
	 * @param TipoHabitacion tipohabitacion
	 */
	public boolean insertarTipoHabitacion(Connection con, TipoHabitacion tipohabitacion, int codigoInstitucion);
	
	/**
	 * Modifica un tipo de habitacion registrada
	 * @param con
	 * @param TipoHabitacion tipohabitacion
	 */
	public boolean modificarTipoHabitacion(Connection con, TipoHabitacion tipohabitacion, String codigoAntesMod, int codigoInstitucion);
	
	/**
	 * Elimina un tipo de habitacion registrada
	 * @param con
	 * @String codigo
	 * @int institucion
	 */
	public boolean eliminarTipoHabitacion(Connection con, String codigo,int institucion);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public HashMap consultarTipoHabitacion(Connection con, int codigoInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param codigo
	 * @return
	 */
	public HashMap consultarTipoHabitacionEspecifico(Connection con, int codigoInstitucion, String codigo);
	
	
}
