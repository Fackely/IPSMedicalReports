/*
 * @(#)OracleTiposUsuarioCamaDao.java
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

import com.princetonsa.dao.manejoPaciente.TiposUsuarioCamaDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseTiposUsuarioCamaDao;
import com.princetonsa.mundo.manejoPaciente.TiposUsuarioCama;

/**  
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 * Funcionalidad descrita en Anexo 413 - Tipos Usuario Cama
 */
public class OracleTiposUsuarioCamaDao implements TiposUsuarioCamaDao 
{

	/**
	 * Insertar un registro de tipos usuario cama
	 * @param con
	 * @param TiposUsuarioCama tiposusuariocama
	 */
	public boolean insertarTiposUsuarioCama(Connection con, TiposUsuarioCama tiposusuariocama, int codigoInstitucion)
	{
		return SqlBaseTiposUsuarioCamaDao.insertarTiposUsuarioCama(con, tiposusuariocama, codigoInstitucion);
	}
	
	/**
	 * Consulta los tipos usuario cama
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarTiposUsuarioCama(Connection con, int codigoInstitucion)
	{
		return SqlBaseTiposUsuarioCamaDao.consultarTiposUsuarioCama(con, codigoInstitucion);
	}
	
	/**
	 * Modifica tipos de usuario cama registrados
	 * @param con
	 * @param TiposUsuarioCama tiposusuariocama 
	 */
	public boolean modificarTiposUsuarioCama(Connection con, TiposUsuarioCama tiposusuariocama, int codigoAntesMod, int codigoInstitucion)
	{
		return SqlBaseTiposUsuarioCamaDao.modificarTiposUsuarioCama(con, tiposusuariocama, codigoAntesMod, codigoInstitucion);
	}
	
	/**
	 * Elimina tipos de usuario cama registrados
	 * @param con
	 * @param String codigo
	 * @param int institucion
	 */
	public boolean eliminarTiposUsuarioCama(Connection con, int codigo, int institucion)
	{
		return SqlBaseTiposUsuarioCamaDao.eliminarTiposUsuarioCama(con, codigo, institucion);
	}
	
	/**
	 * Consulta de los tipos de usuario cama especifica por codigo
	 */
	public HashMap consultarTiposUsuarioCamaEspecifico(Connection con, int codigoInstitucion, int codigo)
	{
		return SqlBaseTiposUsuarioCamaDao.consultarTiposUsuarioCamaEspecifico(con, codigoInstitucion, codigo);
	}
}
