/*
 * @(#)PostgresqlConsultoriosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK jdk1.5.0_07
 *
 */

package com.princetonsa.dao.postgresql.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.PisosDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBasePisosDao;
import com.princetonsa.mundo.manejoPaciente.Pisos;

/**
 * 
 * @author Julián Pacheco
 * jpacheco@princetonsa.com
 * Funcionalidad descrita en Anexo 401 - Pisos
 */
public class PostgresqlPisosDao implements PisosDao
{	
	
	/**
	 * Consulta los n pisos x centro atencion 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public HashMap pisosXCentroAtencionTipo(Connection con, int centroAtencion, int codigoInstitucion)
	{
		return SqlBasePisosDao.pisosXCentroAtencionTipo(con, centroAtencion, codigoInstitucion); 
	}
	
	/**
	 * Insertar un registro de pisos
	 * @param con
	 * @param Pisos pisos
	 */
	public boolean insertarPisos(Connection con, Pisos pisos, int codigoInstitucion)
	{
		return SqlBasePisosDao.insertarPisos(con, pisos, codigoInstitucion);
	}
	
	/**
	 * Modifica un piso registrado
	 * @param con
	 * @param Pisos pisos
	 */
	public boolean modificarPisos(Connection con, Pisos pisos)
	{
		return SqlBasePisosDao.modificarPisos(con, pisos);
	}
	
	/**
	 * Elimina un piso registrado
	 * @param con
	 * @param String codigo
	 * @param int institucion
	 */
	public boolean eliminarPisos(Connection con, int codigo)
	{
		return SqlBasePisosDao.eliminarPisos(con, codigo);
	}
	
	/**
	 * Consulta los pisos
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarPisos(Connection con, int codigoInstitucion)
	{
		return SqlBasePisosDao.consultarPisos(con, codigoInstitucion);
	}
	
	/**
	 * Consulta los pisos por medio de codigo
	 */
	public HashMap consultarPisosEspecifico(Connection con, int codigo)
	{
		return SqlBasePisosDao.consultarPisosEspecifico(con, codigo);
	}

}
