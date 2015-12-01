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
 *  Interfaz para el acceder a la fuente de datos de una Vigencia de diagn�stico
 *
 * @version 1.0, Abril 29 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
 */
public interface CIEDao 
{
	/**
	 * Inserta una vigencia de diagn�stico
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigoReal, String, para capturar el tipo de CIE
	 * @param vigencia, String, para ingresarl la fecha desde la cu�l estar� vigente el CIE
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public int  insertar(	Connection con,
									String codigoReal,
									String vigencia	);
	
	/**
	 * Inserta una vigencia de diagn�stico En una transacci�n dado su estado
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigoReal, String, para capturar el tipo de CIE
	 * @param vigencia, String, para ingresarl la fecha desde la cu�l estar� vigente el CIE
	 * @param estado. String, estado dentro de la transacci�n
	 * @return  ResultadoBoolean, true si la inserci�n fue exitosa, false y con la descripci�n 
	 * 				de lo contrario
	 */
	public ResultadoBoolean insertarTransaccional(	Connection con,
																			String codigoReal,
																			String vigencia,
																			String estado) throws SQLException;
	
	/**
	 * M�todo que  carga  los datos de la vigencia de diagn�stico seg�n el  
	 * c�digo  que llega de la tabla tipos_cie para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator cargarResumen(Connection con, int codigo) ;
	
	/**
	 * M�todo que  Verifica la existencia de un tipo de CIE, ya que no pueden estar duplicados
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator existeTipoCIE(Connection con, String codigoReal); 
	
	/**
	 * M�todo que  Verifica la existencia de una fecha inicio vigencia de CIE, ya que no pueden estar duplicados
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator existeFechaInicioVigenciaCIE(Connection con, String vigencia) ;
	
	/**
	 * Modifica una vigencia de diagn�stico
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigo, int, para capturar el c�digo de la tabla
	 * @param vigencia, String, para ingresar la fecha desde la cu�l estar� vigente el CIE
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public int modificar(	Connection con, 
									int codigo,
									String vigencia );
	
	/**
	 * Modifica una vigencia de diagn�stico en una transacci�n seg�n su estado
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigo, int, para capturar el c�digo de la tabla
	 * @param vigencia, String, para ingresar la fecha desde la cu�l estar� vigente el CIE
	 * @param estado. String, estado dentro de la transacci�n 
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public ResultadoBoolean modificarTransaccional(	Connection con, 
																				int codigo,
																				String vigencia,
																				String estado) throws SQLException;	

	/**
	 * M�todo que borra una vigencia de diagn�stico seg�n su c�digo
	 */
	public int borrarCIE(Connection con, int codigo);
	
	/**
	 * M�todo que contiene el Resulset de los datos de la tabla tipos_cie
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla tipos_cie
	 * @throws SQLException
	 */
	public   ResultSetDecorator listado(Connection con) throws SQLException;

}
