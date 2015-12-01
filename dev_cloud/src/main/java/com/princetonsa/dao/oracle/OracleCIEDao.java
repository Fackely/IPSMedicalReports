/*
 * @(#)OracleCIEDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import util.ResultadoBoolean;

import com.princetonsa.dao.CIEDao;
import com.princetonsa.dao.sqlbase.SqlBaseCIEDao;

/**
 * Implementaci�n Oracle de las funciones de acceso a la fuente de datos
 * para la vigencia de diagn�sticos
 *
 * @version 1.0, Agosto 17 / 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
 */
public class OracleCIEDao implements CIEDao
{//SIN PROBAR FUNC. SECUENCIA
	
	/**
	 *  Insertar una vigencia de diagn�sticos
	 */
	private final static String insertarCIEStr = "INSERT INTO tipos_cie ( codigo, codigo_real, vigencia ) VALUES (seq_tipos_cie.nextval,?,?)";
	
	/**
	 * Inserta una vigencia de diagn�stico
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigoReal, String, para capturar el tipo de CIE
	 * @param vigencia, String, para ingresarl la fecha desde la cu�l estar� vigente el CIE
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public int  insertar(	Connection con,
									String codigoReal,
									String vigencia	)
	{
		return SqlBaseCIEDao.insertar(con,codigoReal,vigencia, insertarCIEStr);
	}
	
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
																			String estado) throws SQLException
	{
		return SqlBaseCIEDao.insertarTransaccional(con,codigoReal,vigencia,estado, insertarCIEStr);
	}
	
	/**
	 * M�todo que  carga  los datos de la vigencia de diagn�stico seg�n el  
	 * c�digo  que llega de la tabla tipos_cie para mostrarlos en el resumen
	 * en una BD OracleSQL o Hsqldb 
	 */
	public ResultSetDecorator cargarResumen(Connection con, int codigo)
	{
		return SqlBaseCIEDao.cargarResumen(con,codigo);
	}
	
	/**
	 * M�todo que  Verifica la existencia de un tipo de CIE (codigo real), ya que no pueden estar duplicados
	 * en una BD OracleSQL o Hsqldb 
	 */
	public ResultSetDecorator existeTipoCIE(Connection con,  String codigoReal)
	{
		return SqlBaseCIEDao.existeTipoCIE(con,codigoReal);
	}
	
	/**
	 * M�todo que  Verifica la existencia de una fecha inicio vigencia de CIE, ya que no pueden estar duplicados
	 * en una BD Oracle 
	 */
	public ResultSetDecorator existeFechaInicioVigenciaCIE(Connection con, String vigencia)
	{
		return SqlBaseCIEDao.existeFechaInicioVigenciaCIE(con,vigencia);
	}
	
	/**
	 * Modifica una vigencia de diagn�stico
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigo, int, para capturar el c�digo de la tabla
	 * @param vigencia, String, para ingresar la fecha desde la cu�l estar� vigente el CIE
	 * @return int, 0 si  no inserta, 1 si inserta
	 */
	public int modificar(	Connection con, 
									int codigo,
									String vigencia )
	{
		return SqlBaseCIEDao.modificar(con,codigo,vigencia);
	}
	
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
																				String estado) throws SQLException
	{
		return SqlBaseCIEDao.modificarTransaccional(con,codigo,vigencia,estado);
	}

	/**
	 * M�todo que borra una vigencia de diagn�stico seg�n su c�digo
	 */
	public int borrarCIE(Connection con, int codigo)
	{
		return SqlBaseCIEDao.borrarCIE(con,codigo);
	}
	
	/**
	 * M�todo que contiene el Resulset de los datos de la tabla tipos_cie
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla tipos_cie
	 * @throws SQLException
	 */
	public   ResultSetDecorator listado(Connection con) throws SQLException
	{
		return SqlBaseCIEDao.listado(con);
	}

}
