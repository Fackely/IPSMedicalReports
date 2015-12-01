/*
 * Ago 02, 2006
 */
package com.princetonsa.dao.postgresql.pyp;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.pyp.ActividadesPypDao;
import com.princetonsa.dao.sqlbase.pyp.SqlBaseActividadesPypDao;
/**
 * @author Sebasti�n G�mez 
 *
 * Clase que maneja los m�todos prop�os de Postgres para el acceso a la fuente
 * de datos en la funcionalidad Actividades de promoci�n y prevenci�n
 */
public class PostgresqlActividadesPypDao implements ActividadesPypDao 
{
	/**
	 * M�todo implementado para consultar actividades de promoci�n y prevenci�n
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultar(Connection con,HashMap campos)
	{
		return SqlBaseActividadesPypDao.consultar(con,campos);
	}
	
	/**
	 * M�todo implementado para insertar una actividad de promoci�n y prevenci�n
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertar(Connection con,HashMap campos)
	{
		campos.put("secuencia","nextval('seq_actividades_pyp')");
		return SqlBaseActividadesPypDao.insertar(con,campos);
	}
	
	/**
	 * M�todo implementado para eliminar una actividad de promoci�n y prevenci�n
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminar(Connection con,HashMap campos)
	{
		return SqlBaseActividadesPypDao.eliminar(con,campos);
	}
	
	/**
	 * M�todo implementado para modificar una actividad de promoci�n y prevenci�n
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificar(Connection con,HashMap campos)
	{
		return SqlBaseActividadesPypDao.modificar(con,campos);
	}


	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------Funciones de La funcionalidad de Actividades de Promocion Y Prevencion por Centros de Atenci�n-------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	
	
	/**
	 * Metodo para consultar toda la informacion relacionada con la funcionalidad.
	 */
	public HashMap consultarInformacion(Connection con, HashMap mapaParam)
	{
		return SqlBaseActividadesPypDao.consultarInformacion(con, mapaParam);
	}

	/**
	 * Metodo para insertar Actividades PYP por Centro de Atencion.
	 * @param con
	 * @param centroAtencion
	 * @param codigoActividad
	 * @param institucion
	 * @return
	 */
	public int insertarActividadesCentroAtencion(Connection con, int tipoOperacion, int centroAtencion, int codigoActividad, int institucion, boolean activo)
	{
		return SqlBaseActividadesPypDao.insertarActividadesCentroAtencion(con, tipoOperacion, centroAtencion, codigoActividad, institucion, activo);
	}
	
	/**
	 * Metodo para eliminar una Actividad PYP para un centro de Atencion Especifico.
	 * @param con
	 * @param centroAtencion
	 * @param codigoActividad
	 * @param institucion
	 * @return
	 */
	public int eliminarActividadesCentroAtencion(Connection con, int centroAtencion, int codigoActividad, int institucion)
	{
		return SqlBaseActividadesPypDao.eliminarActividadesCentroAtencion(con, centroAtencion, codigoActividad, institucion);
	}
	
	
}
