/*
 * @(#)PisosDao.java
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


import com.princetonsa.mundo.manejoPaciente.Pisos;

/**
* 
* @author Julián Pacheco
* jpacheco@princetonsa.com
*/
public interface PisosDao 
{	
	
	/**
	 * Consulta los n pisos x centro atencion 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap pisosXCentroAtencionTipo(Connection con, int centroAtencion, int codigoInstitucion);
	
	/**
	 * Insertar un registro de pisos
	 * @param con
	 * @param Pisos pisos
	 */
	public boolean insertarPisos(Connection con, Pisos pisos, int codigoInstitucion);
	
	/**
	 * Modifica un piso registrado
	 * @param con
	 * @param Pisos pisos
	 */
	public boolean modificarPisos(Connection con, Pisos pisos);
	
	/**
	 * Elimina un piso registrado
	 * @param con
	 * @param String codigo
	 * @param int institucion
	 */
	public boolean eliminarPisos(Connection con, int codigo);
	
	/**
	 * Consulta los pisos
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarPisos(Connection con, int codigoInstitucion);
	
	/**
	 * Consulta los pisos por medio de codigo
	 */
	public HashMap consultarPisosEspecifico(Connection con, int codigo);
	
}
