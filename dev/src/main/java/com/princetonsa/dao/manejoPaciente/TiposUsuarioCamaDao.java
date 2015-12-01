/*
 * @(#)TiposUsuariosCamaDao.java
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

import com.princetonsa.mundo.manejoPaciente.TiposUsuarioCama;

/**
* 
* @author Julián Pacheco
* jpacheco@princetonsa.com
*/
public interface TiposUsuarioCamaDao 
{
	/**
	 * Insertar un registro de tipos usuario cama
	 * @param con
	 * @param TiposUsuarioCama tiposusuariocama
	 */
	public boolean insertarTiposUsuarioCama(Connection con, TiposUsuarioCama tiposusuariocama, int codigoInstitucion);
	
	/**
	 * Modifica tipos de usuario cama registrados
	 * @param con
	 * @param TiposUsuarioCama tiposusuariocama
	 */
	public boolean modificarTiposUsuarioCama(Connection con, TiposUsuarioCama tiposusuariocama, int codigoAntesMod, int codigoInstitucion);
	
	/**
	 * Elimina tipos de usuario cama registrados
	 * @param con
	 * @String codigo
	 * @int institucion
	 */
	public boolean eliminarTiposUsuarioCama(Connection con, int codigo,int institucion);
	
	/**
	 * @param con
	 * @param codigo
	 */
	public HashMap consultarTiposUsuarioCama(Connection con, int codigoInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param codigo
	 * @return
	 */
	public HashMap consultarTiposUsuarioCamaEspecifico(Connection con, int codigoInstitucion, int codigo);

}
