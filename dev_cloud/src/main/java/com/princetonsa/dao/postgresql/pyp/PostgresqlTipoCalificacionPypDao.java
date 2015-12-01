/*
 * Creado el Aug 8, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.postgresql.pyp;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.pyp.TipoCalificacionPypDao;
import com.princetonsa.dao.sqlbase.pyp.SqlBaseTipoCalificacionDao;

public class PostgresqlTipoCalificacionPypDao implements TipoCalificacionPypDao {


	/**
	 * Metodo para cargar la información General de la Funcionalidad 
	 * @param con
	 * @param HasMap
	 * @return
	 */
	public HashMap consultarInformacion(Connection con, HashMap mapaParam)
	{
		return SqlBaseTipoCalificacionDao.consultarInformacion(con, mapaParam);
	}
	/**
	 * Para Insertar los tipos de calificacion de Metas de PYP 
	 * @param con
	 * @param tipoOperacion
	 * @param consecutivo
	 * @param codigo
	 * @param nombre
	 * @param institucion
	 * @param activo
	 * @return
	 */
	public int insertarTipoCalificacion(Connection con, int tipoOperacion, int consecutivo, String codigo, String nombre, int institucion, boolean activo)
	{
		return SqlBaseTipoCalificacionDao.insertarTipoCalificacion(con, tipoOperacion, consecutivo, codigo, nombre, institucion, activo);
	}
	
	/**
	 * Metodo para eliminar un tipo de Calificacion de Metas de PYP
	 * @param con
	 * @param codigoTipoCal
	 * @return
	 */
	public int eliminarTipoCalificacion(Connection con, int codigoTipoCal)
	{
		return SqlBaseTipoCalificacionDao.eliminarTipoCalificacion(con, codigoTipoCal);
	}


}
