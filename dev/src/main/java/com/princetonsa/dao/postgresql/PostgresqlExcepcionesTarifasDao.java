/*
 * @(#)PostgresqlExcepcionestarifasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Vector;

import com.princetonsa.dao.ExcepcionesTarifasDao;
import com.princetonsa.dao.sqlbase.SqlBaseExcepcionesTarifasDao;

/**
 * Implementación postgresql de las funciones de acceso a la fuente de datos
 * para una excepcion de tarifas
 *
 * @version 1.0, Oct 20 / 2004
 * @author wrios
 */
public class PostgresqlExcepcionesTarifasDao implements ExcepcionesTarifasDao
{
	/**
	 * sentencia sql para insertar una excepcion
	 */
	 private final static String insertarStr = 	"INSERT INTO excepciones_tarifas " +
	 													"(codigo, " +
	 													"porcentaje, " +
	 													"valor_ajuste, " +
	 													"nueva_tarifa, " +
	 													"via_ingreso, " +
	 													"especialidad, " +
	 													"contrato, " +
	 													"servicio ) " +
	 													"VALUES(nextval('seq_excepciones_tarifas'), ?, ?, ?, ?, ?, ?, ?) ";
	
	/**
	 * Inserta una excepción a una tarifa 
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param porcentaje. double, porcentaje de descuento o aumento de la tarifa
	 * @param valorAjuste. double, valor de ajuste de la tarifa
	 * @param nuevaTarifa. double, nueva tarifa
	 * @param codigoServicio. int, código del servicio asociado esta tarifa, 0 es para todos
	 * @param codigoEspecialidad. int, código de la especialidad asociado esta tarifa, 0 es para todas
	 * @param codigoViaIngreso. int, código de la via de ingreso asociado esta tarifa, 0 es para todas
	 * @param codigoContrato. int, código del contrato para el cual es válida esta excepción
	 * @return boolean 
	 * @see com.princetonsa.dao.ExcepcionTarifaDao#insertar(java.sql.Connection, double, double, double, int, int, int, int)
	 */
	public boolean insertar(		Connection con,
												double porcentaje,
												double valorAjuste,
												double nuevaTarifa,
												int codigoServicio,
												int codigoEspecialidad,
												int codigoViaIngreso,
												int codigoContrato)
	{
		return SqlBaseExcepcionesTarifasDao.insertar(con,porcentaje,valorAjuste,nuevaTarifa,codigoServicio,codigoEspecialidad,codigoViaIngreso,codigoContrato, insertarStr);
	}
	
	/**
	 * Dice si existe o no una excepcion de tarifa dada por la via de ingreso, la especialidad, el contrato y el codigo del servicio asociado
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigoViaIngreso. int, código del esquema tarifario asociado a esta tarifa
	 * @param codigoEspecialidad, int, código de la especialidad de la excepcion
	 * @param codigoContrato, int, código del contrato asociado a la excepcion
	 * @param codigoServicio. int, código del servicio asociado a esta tarifa
	 * @return boolean 
	 * @throws SQLException
	 * @see com.princetonsa.dao.ExcepcionTarifaDao#existeTarifaDefinida(java.sql.Connection, int, int, int, int)
	 */
	public boolean  existeTarifaDefinida(	Connection con, 
															int codigoViaIngreso, 
															int codigoEspecialidad, 
															int codigoContrato, 
															int codigoServicio,
															int codigoExcepcion)
	{
		return SqlBaseExcepcionesTarifasDao.existeTarifaDefinida(con,codigoViaIngreso,codigoEspecialidad, codigoContrato, codigoServicio, codigoExcepcion);
	}
	
	/**
	 * Método que  carga  los datos de una excepción para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator cargarResumen(Connection con)
	{
		return SqlBaseExcepcionesTarifasDao.cargarResumen(con);
	}
	
	/**
	 * Cargar la ultima excepcion; insertada  
	 * @param con Connection con la fuente de datos
	 * @return int ultimoCodigoSequence, 0 no efectivo.
	 */
	public int cargarUltimoCodigoSequence(Connection con)
	{
		return SqlBaseExcepcionesTarifasDao.cargarUltimoCodigoSequence(con);
	}
	
	/**
	 * Método que contiene el Resulset de todas las excepciones  buscadas
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla empresas
	 * @throws SQLException
	 */
	public ResultSetDecorator busqueda(	Connection con,
												int codigoContrato,
												int codigoViaIngreso,
												int codigoServicio,
												String nombreServicio,
												int codigoEspecialidad,
												String nombreEspecialidad,
												double porcentaje,
												double valorAjuste,
												double nuevaTarifa) throws SQLException
	{
		return SqlBaseExcepcionesTarifasDao.busqueda(	con,codigoContrato, codigoViaIngreso, codigoServicio, 
																					nombreServicio, codigoEspecialidad, nombreEspecialidad, 
																					porcentaje,valorAjuste,nuevaTarifa);
	}

	/**
	 * Método que elimina una excepción de tarifa según su código  
	 */
	public boolean eliminar(Connection con, int codigoExcepcion)
	{
		return SqlBaseExcepcionesTarifasDao.eliminar(con, codigoExcepcion);
	}
	
	/**
	 * Modifica una excepción dado su código con los paramétros dados.
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigo, int, codigo de la excepción
	 * @param codigoViaIngreso, int, codigo vía de ingreso
	 * @param codigoEspecialidad
	 * @paramcodigoServicio
	 * @param porcentaje
	 * @param valorAjuste
	 * @param nuevaTarifa
	 * @return  boolean
	 */		
	public boolean modificar(		Connection con, 
												int codigo, 
												int codigoViaIngreso,
												int codigoEspecialidad,
												int codigoServicio,
												double porcentaje,
												double valorAjuste, 
												double nuevaTarifa)
	{
		return SqlBaseExcepcionesTarifasDao.modificar(con,codigo,codigoViaIngreso, codigoEspecialidad, codigoServicio, porcentaje, valorAjuste, nuevaTarifa);
	}
	
	/**
	 * Método que  carga  el resumen de la modificación de N excepciones 
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator cargarResumenModificacion(Connection con, Vector codigos)
	{
		return SqlBaseExcepcionesTarifasDao.cargarResumenModificacion(con,codigos);
	}
}
