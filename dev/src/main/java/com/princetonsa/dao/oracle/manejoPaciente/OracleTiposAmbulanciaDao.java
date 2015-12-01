/*
 * @(#)OracleTiposAmbulanciaDao.java
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

import com.princetonsa.dao.manejoPaciente.TiposAmbulanciaDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseTipoHabitacionDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseTiposAmbulanciaDao;
import com.princetonsa.mundo.manejoPaciente.TiposAmbulancia;

/**  
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 * Funcionalidad descrita en Anexo 430 - Tipos Ambulancia
 */

public class OracleTiposAmbulanciaDao implements TiposAmbulanciaDao
{
	/**
	 * Insertar un registro de tipos ambulancia
	 * @param con
	 * @param TiposAmbulancia tiposambulancia
	 */
	public boolean insertarTiposAmbulancia(Connection con, TiposAmbulancia tiposambulancia, int codigoInstitucion)
	{
		return SqlBaseTiposAmbulanciaDao.insertarTiposAmbulancia(con, tiposambulancia, codigoInstitucion);
	}
	
	/**
	 * Modifica tipos de ambulancia registrada
	 * @param con
	 * @param TiposAmbulancia tiposambulancia 
	 */
	public boolean modificarTiposAmbulancia(Connection con, TiposAmbulancia tiposambulancia, String codigoAntesMod, int codigoInstitucion)
	{
		return SqlBaseTiposAmbulanciaDao.modificarTiposAmbulancia(con, tiposambulancia, codigoAntesMod, codigoInstitucion);
	}
	
	/**
	 * Elimina tipos de ambulancia registradas
	 * @param con
	 * @param String codigo
	 * @param int institucion
	 */
	public boolean eliminarTiposAmbulancia(Connection con, String codigo, int institucion)
	{
		return SqlBaseTiposAmbulanciaDao.eliminarTiposAmbulancia(con, codigo, institucion);
	}
	
	/**
	 * Consulta los tipos ambulancia
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarTiposAmbulancia(Connection con, int codigoInstitucion)
	{
		return SqlBaseTiposAmbulanciaDao.consultarTiposAmbulancia(con, codigoInstitucion);
	}
	
	/**
	 * Consulta el tipos de ambulancia especificada por codigo
	 */
	public HashMap consultarTiposAmbulanciaEspecifico(Connection con, int codigoInstitucion, String codigo)
	{
		return SqlBaseTiposAmbulanciaDao.consultarTiposAmbulanciaEspecifico(con, codigoInstitucion, codigo);
	}
}
