/*
 * @(#)TiposAmbulanciaDao.java
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

import com.princetonsa.mundo.manejoPaciente.TiposAmbulancia;

/**
* 
* @author Julián Pacheco
* jpacheco@princetonsa.com
*/
public interface TiposAmbulanciaDao 
{
	/**
	 * Insertar un registro de tipos ambulancia
	 * @param con
	 * @param TiposAmbulancia tiposambulancia
	 */
	public boolean insertarTiposAmbulancia(Connection con, TiposAmbulancia tiposambulancia, int codigoInstitucion);

	/**
	 * Modifica tipos de ambulancia registrada
	 * @param con
	 * @param TiposAmbulancia tiposambulancia
	 */
	public boolean modificarTiposAmbulancia(Connection con, TiposAmbulancia tiposambulancia, String codigoAntesMod, int codigoInstitucion);
	
	/**
	 * Elimina tipos de ambulancia registradas
	 * @param con
	 * @String codigo
	 * @int institucion
	 */
	public boolean eliminarTiposAmbulancia(Connection con, String codigo,int institucion);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public HashMap consultarTiposAmbulancia(Connection con, int codigoInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param codigo
	 * @return
	 */
	public HashMap consultarTiposAmbulanciaEspecifico(Connection con, int codigoInstitucion, String codigo);
}
