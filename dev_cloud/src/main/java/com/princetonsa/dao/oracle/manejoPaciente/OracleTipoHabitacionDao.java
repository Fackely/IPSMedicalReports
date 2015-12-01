/*
 * @(#)OracleTipoHabitacionDao.java
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

import com.princetonsa.dao.manejoPaciente.TipoHabitacionDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseTipoHabitacionDao;
import com.princetonsa.mundo.manejoPaciente.TipoHabitacion;

/**  
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 * Funcionalidad descrita en Anexo 414 - Tipo Habitacion
 */

public class OracleTipoHabitacionDao implements TipoHabitacionDao
{
	/**
	 * Insertar un registro de tipo habitacion
	 * @param con
	 * @param TipoHabitacion tipohabitacion
	 */
	public boolean insertarTipoHabitacion(Connection con, TipoHabitacion tipohabitacion, int codigoInstitucion)
	{
		return SqlBaseTipoHabitacionDao.insertarTipoHabitacion(con, tipohabitacion, codigoInstitucion);
	}
	
	/**
	 * Modifica un tipo de habitacion registrada
	 * @param con
	 * @param TipoHabitacion tipohabitacion 
	 */
	public boolean modificarTipoHabitacion(Connection con, TipoHabitacion tipohabitacion, String codigoAntesMod, int codigoInstitucion)
	{
		return SqlBaseTipoHabitacionDao.modificarTipoHabitacion(con, tipohabitacion, codigoAntesMod, codigoInstitucion);
	}
	
	/**
	 * Elimina un tipo de habitacion registrada
	 * @param con
	 * @param String codigo
	 * @param int institucion
	 */
	public boolean eliminarTipoHabitacion(Connection con, String codigo, int institucion)
	{
		return SqlBaseTipoHabitacionDao.eliminarTipoHabitacion(con, codigo, institucion);
	}
	
	/**
	 * Consulta el tipo habitacion
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarTipoHabitacion(Connection con, int codigoInstitucion)
	{
		return SqlBaseTipoHabitacionDao.consultarTipoHabitacion(con, codigoInstitucion);
	}
	
	/**
	 * Consulta el tipo de habitacion especifica por codigo
	 */
	public HashMap consultarTipoHabitacionEspecifico(Connection con, int codigoInstitucion, String codigo)
	{
		return SqlBaseTipoHabitacionDao.consultarTipoHabitacionEspecifico(con, codigoInstitucion, codigo);
	}
}
