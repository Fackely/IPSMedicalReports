/*
 * @(#)ExcepcionesTarifasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 *  Interfaz para acceder a la fuente de datos de un contrato
 *
 * @version 1.0, Octu 20 / 2004
 * @author wrios
 */
public interface ExcepcionesTarifasDao 
{
	/**
	 * Inserta una excepci�n a una tarifa 
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param porcentaje. double, porcentaje de descuento o aumento de la tarifa
	 * @param valorAjuste. double, valor de ajuste de la tarifa
	 * @param nuevaTarifa. double, nueva tarifa
	 * @param codigoServicio. int, c�digo del servicio asociado esta tarifa, 0 es para todos
	 * @param codigoEspecialidad. int, c�digo de la especialidad asociado esta tarifa, 0 es para todas
	 * @param codigoViaIngreso. int, c�digo de la via de ingreso asociado esta tarifa, 0 es para todas
	 * @param codigoContrato. int, c�digo del contrato para el cual es v�lida esta excepci�n
	 * @return int 
	 * @see com.princetonsa.dao.ExcepcionTarifaDao#insertar(java.sql.Connection, double, double, double, int, int, int, int)
	 */
	public boolean  insertar(		Connection con,
												double porcentaje,
												double valorAjuste,
												double nuevaTarifa,
												int codigoServicio,
												int codigoEspecialidad,
												int codigoViaIngreso,
												int codigoContrato);
	
	/**
	 * Dice si existe o no una excepcion de tarifa dada por la via de ingreso, la especialidad, el contrato y el codigo del servicio asociado
	 * @param con, Connection, conexi�n abierta con la fuente de datos
	 * @param codigoViaIngreso. int, c�digo del esquema tarifario asociado a esta tarifa
	 * @param codigoEspecialidad, int, c�digo de la especialidad de la excepcion
	 * @param codigoContrato, int, c�digo del contrato asociado a la excepcion
	 * @param codigoServicio. int, c�digo del servicio asociado a esta tarifa
	 * @return boolean 
	 * @throws SQLException
	 * @see com.princetonsa.dao.ExcepcionTarifaDao#existeTarifaDefinida(java.sql.Connection, int, int, int, int)
	 */
	public boolean  existeTarifaDefinida(	Connection con, 
															int codigoViaIngreso, 
															int codigoEspecialidad, 
															int codigoContrato, 
															int codigoServicio,
															int codigoExcepcion); 
	
	/**
	 * M�todo que  carga  los datos de una excepci�n para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator cargarResumen(Connection con); 
	
	/**
	 * Cargar la ultima excepcion; insertada  
	 * @param con Connection con la fuente de datos
	 * @return int ultimoCodigoSequence, 0 no efectivo.
	 */
	public int cargarUltimoCodigoSequence(Connection con);
	
	/**
	 * M�todo que contiene el Resulset de todas las excepciones  buscadas
	 * @param con, Connection, conexi�n abierta con una fuente de datos
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
												double nuevaTarifa) throws SQLException;
	
	/**
	 * M�todo que elimina una excepci�n de tarifa seg�n su c�digo  
	 */
	public boolean eliminar(Connection con, int codigoExcepcion);
	
	/**
	 * Modifica una excepci�n dado su c�digo con los param�tros dados.
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigo, int, codigo de la excepci�n
	 * @param codigoViaIngreso, int, codigo v�a de ingreso
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
												double nuevaTarifa);
	
	/**
	 * M�todo que  carga  el resumen de la modificaci�n de N excepciones 
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator cargarResumenModificacion(Connection con, Vector codigos); 
	
}
