/*
 * @(#)CIEDao.java
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

import util.ResultadoBoolean;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 *  Interfaz para el acceder a la fuente de datos de una Vigencia de diagnóstico
 *
 * @version 1.0, Abril 29 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface CIEDao 
{
	/**
	 * Inserta una vigencia de diagnóstico
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigoReal, String, para capturar el tipo de CIE
	 * @param vigencia, String, para ingresarl la fecha desde la cuál estará vigente el CIE
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public int  insertar(	Connection con,
									String codigoReal,
									String vigencia	);
	
	/**
	 * Inserta una vigencia de diagnóstico En una transacción dado su estado
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigoReal, String, para capturar el tipo de CIE
	 * @param vigencia, String, para ingresarl la fecha desde la cuál estará vigente el CIE
	 * @param estado. String, estado dentro de la transacción
	 * @return  ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * 				de lo contrario
	 */
	public ResultadoBoolean insertarTransaccional(	Connection con,
																			String codigoReal,
																			String vigencia,
																			String estado) throws SQLException;
	
	/**
	 * Método que  carga  los datos de la vigencia de diagnóstico según el  
	 * código  que llega de la tabla tipos_cie para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator cargarResumen(Connection con, int codigo) ;
	
	/**
	 * Método que  Verifica la existencia de un tipo de CIE, ya que no pueden estar duplicados
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator existeTipoCIE(Connection con, String codigoReal); 
	
	/**
	 * Método que  Verifica la existencia de una fecha inicio vigencia de CIE, ya que no pueden estar duplicados
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator existeFechaInicioVigenciaCIE(Connection con, String vigencia) ;
	
	/**
	 * Modifica una vigencia de diagnóstico
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigo, int, para capturar el código de la tabla
	 * @param vigencia, String, para ingresar la fecha desde la cuál estará vigente el CIE
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public int modificar(	Connection con, 
									int codigo,
									String vigencia );
	
	/**
	 * Modifica una vigencia de diagnóstico en una transacción según su estado
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param codigo, int, para capturar el código de la tabla
	 * @param vigencia, String, para ingresar la fecha desde la cuál estará vigente el CIE
	 * @param estado. String, estado dentro de la transacción 
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public ResultadoBoolean modificarTransaccional(	Connection con, 
																				int codigo,
																				String vigencia,
																				String estado) throws SQLException;	

	/**
	 * Método que borra una vigencia de diagnóstico según su código
	 */
	public int borrarCIE(Connection con, int codigo);
	
	/**
	 * Método que contiene el Resulset de los datos de la tabla tipos_cie
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla tipos_cie
	 * @throws SQLException
	 */
	public   ResultSetDecorator listado(Connection con) throws SQLException;

}
