/*
 * Ago 02, 2006
 */
package com.princetonsa.dao.oracle.pyp;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.pyp.ActividadesPypDao;
import com.princetonsa.dao.sqlbase.pyp.SqlBaseActividadesPypDao;

/**
 * @author Sebastián Gómez
 *
 * Clase que maneja los métodos propìos de Oracle para el acceso a la fuente
 * de datos en la funcionalidad Parametrización de Actividades de Promoción y Prevención
 */
public class OracleActividadesPypDao implements ActividadesPypDao 
{
	/**
	 * Método implementado para consultar actividades de promoción y prevención
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultar(Connection con,HashMap campos)
	{
		return SqlBaseActividadesPypDao.consultar(con,campos);
	}
	
	/**
	 * Método implementado para insertar una actividad de promoción y prevención
	 * @param con
	 * @param campos
	 * @return
	 */
	public int insertar(Connection con,HashMap campos)
	{
		campos.put("secuencia","seq_actividades_pyp.nextval");
		return SqlBaseActividadesPypDao.insertar(con,campos);
	}
	
	/**
	 * Método implementado para modificar una actividad de promoción y prevención
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificar(Connection con,HashMap campos)
	{
		return SqlBaseActividadesPypDao.modificar(con,campos);
	}
	
	/**
	 * Método implementado para eliminar una actividad de promoción y prevención
	 * @param con
	 * @param campos
	 * @return
	 */
	public int eliminar(Connection con,HashMap campos)
	{
		return SqlBaseActividadesPypDao.eliminar(con,campos);
	}



	
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//-------------------------------------------------------------------------------------------------------------------------------------------------------
	//--------------------------------Funciones de La funcionalidad de Actividades de Promocion Y Prevencion por Centros de Atención-------------------------
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
