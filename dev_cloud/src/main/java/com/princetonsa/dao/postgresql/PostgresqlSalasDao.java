/*
 * Sep 05/2005
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.SalasDao;
import com.princetonsa.dao.sqlbase.SqlBaseSalasDao;

/**
 *@author Sebasti�n G�mez
 *
 * Clase que maneja los m�todos prop�os de Postgres para el acceso a la fuente
 * de datos en la funcionalidad Parametrizaci�n de Salas
 */
public class PostgresqlSalasDao implements SalasDao {
	
	/**
	 * Cadena para insertar un registro nuevo en la tabla salas
	 */
	private static final String insertarSalaStr="INSERT INTO salas " +
			"(consecutivo,codigo,institucion,tipo_sala,activo,descripcion,centro_atencion,medico) " +
			"VALUES(nextval('seq_salas'),?,?,?,?,?,?,?)";
	/**
	 * M�todo usado para cargar el listado de salas existentes
	 * por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarSalas(Connection con,int institucion,int centroAtencion)
	{
		return SqlBaseSalasDao.cargarSalas(con,institucion, centroAtencion);
	}
	
	/**
	 * M�todo para insertar una nueva sala
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param tipoSala
	 * @param activo
	 * @param descripcion
	 * @param insertarSalasStr
	 * @return
	 */
	public int insertarSala(Connection con,String codigo,
			int institucion,int tipoSala,boolean activo,String descripcion, int centroAtencion, String medico)
	{
		return SqlBaseSalasDao.insertarSala(con,codigo,institucion,tipoSala,activo,descripcion,insertarSalaStr, centroAtencion, medico);
	}
	
	/**
	 * M�todo usado para actualizar los datos de una sala
	 * @param con
	 * @param codigo
	 * @param tipoSala
	 * @param activo
	 * @param descripcion
	 * @param consecutivo
	 * @return
	 */
	public String actualizarSala(Connection con,String codigo,int tipoSala,boolean activo,String descripcion,int consecutivo, int centroAtencion, String medico)
	{
		return SqlBaseSalasDao.actualizarSala(con,codigo,tipoSala,activo,descripcion,consecutivo,centroAtencion, medico);
	}
	
	/**
	 * M�todo usado para eliminar una sala
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public int eliminarSala(Connection con,int consecutivo)
	{
		return SqlBaseSalasDao.eliminarSala(con,consecutivo);
	}
	
	/**
	 * M�todo usado para cargar una sala de las tablas existentes
	 * de acuerdo a su consecutivo interno de Axioma
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public HashMap cargarSala(Connection con,int consecutivo)
	{
		return SqlBaseSalasDao.cargarSala(con,consecutivo);
	}
	
	/**
	 * M�todo usado para cargar la disponiblidad de la sala
	 * @param con
	 * @param consecutivoSala
	 * @return HashMap con la disponibilidad de la sala
	 */
	public HashMap cargarDisponibilidadSala(Connection con, String consecutivoSala)
	{
		return SqlBaseSalasDao.cargarDisponibilidadSala (con, consecutivoSala);
	}
	
	/**
	 * M�todo usado para actualizar un rango de disponibilidad para una sala espec�fica
	 * @param con
	 * @param codigoSala
	 * @param rangoInicial
	 * @param rangoFinal
	 * @param rangoInicialAnterior
	 * @param rangoFinalAnterior
	 * @return 
	 */
	public int actualizarDisponibilidadSala (Connection con, String codigoSala, String rangoInicial, String rangoFinal, String rangoInicialAnterior, String rangoFinalAnterior)
	{
		return SqlBaseSalasDao.actualizarDisponibilidadSala (con, codigoSala, rangoInicial, rangoFinal, rangoInicialAnterior, rangoFinalAnterior);
	}
	
	/**
	 * M�todo para insertar un nuevo rango de disponibilidad de la sala
	 * @param con
	 * @param codigoSala
	 * @param rangoInicial
	 * @param rangoFinal
	 * @return
	 */
	public String insertarDisponibilidadSala(Connection con, String codigoSala, String rangoInicial, String rangoFinal)
	{
		return SqlBaseSalasDao.insertarDisponibilidadSala (con, codigoSala, rangoInicial, rangoFinal);
	}

	
	/**
	 * @param con
	 * @param Codigo Sala
	 * @param Rango Inicial
	 * @param Rango Final
	 * @return
	 */

	public int accionEliminarRango(Connection con, String codigoSala, String rangoInicial, String rangoFinal)
	{
		return SqlBaseSalasDao.accionEliminarRango(con,codigoSala, rangoInicial, rangoFinal);
	}
}
